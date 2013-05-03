package org.lareferencia.backend.tasks;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.harvester.HarvestingEvent;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.harvester.IHarvestingEventListener;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordDAO;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(value="prototype")
public class SnapshotWorker implements ISnapshotWorker, IHarvestingEventListener {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NationalNetworkRepository networkRepository;
		
	@Autowired
	private NetworkSnapshotRepository snapshotRepository;
	
	@Autowired
	private OAIRecordDAO recordDAO;
	
	private IHarvester harvester;
	
	@Autowired
	private IValidator validator;
	
	@Autowired
	private ITransformer transformer;
	
	private Long _network_id;
	
	private NationalNetwork network;

	private NetworkSnapshot snapshot;

	public void setNetworkID(Long networkID) {
		this._network_id = networkID;
	}

	public SnapshotWorker() {
	};
	
	
	/**
	 * TODO: Podría ser Async, pero no tiene sentido empezar un nuevo proceso de harvesting para una misma red si el anterior
	 * no terminó. Hay que cuidar los bloqueos!!! TODO: Podría implemetarse la limpieza de procesos inactivos para evitar problemas
	 */
	@Override
	public void run() {
		
		network = networkRepository.findOne( _network_id );
		
		// Crea el snapshot de la red
		snapshot = new NetworkSnapshot();
		snapshot.setNetwork(network);
		snapshot.setStatus( SnapshotStatus.PROCESSING );
		snapshotRepository.save(snapshot);

		// Ciclo principal de procesamiento, dado por la estructura de la red nacional
		// Se recorren los orígenes 
		for ( OAIOrigin origin:network.getOrigins() ) {
			
			// si tiene sets declarados solo va a cosechar 
			if ( origin.getSets().size() > 0 ) {
			
				for ( OAISet set: origin.getSets() ) {
					harvester.harvest(origin.getUri(), null, null, set.getSpec(), origin.getMetadataPrefix());
				}
			}
			// si no hay set declarado cosecha todo
			else {
				harvester.harvest(origin.getUri(), null, null, null, origin.getMetadataPrefix());
			}	
		}
		
		// Cierre del Snapshot
		// Si no generó errores terminó exitoso
		if ( snapshot.getStatus() != SnapshotStatus.FINISHED_ERROR )
			snapshot.setStatus( SnapshotStatus.FINISHED_VALID );
		
		
		snapshot.setEndTime( new Date() );
		snapshotRepository.save(snapshot);
		
		// Flush y llamados al GC
		snapshotRepository.flush();
		System.gc();
	}
	
	/*************************************************************/
	
	
	@Autowired
	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		harvester.addEventListener(this);		
	}
	
	
	@Override
	@Transactional
	public void harvestingEventOccurred(HarvestingEvent event) {
		
		System.out.println( network.getName() + "  HarvestingEvent recibido: " + event.getStatus() );
			
		switch ( event.getStatus() ) {
			case OK:			
				
				
				// Agrega los records al snapshot actual			
				for (OAIRecord  record:event.getRecords() ) {
					
					try {
					
						// registra el snapshot al que pertenece
						record.setSnapshot(snapshot);
						snapshot.incrementSize();
						
						// prevalidación
						ValidationResult validationResult = validator.validate(record);
							
						if ( validationResult.isValid() ) {
							
							// registra si es válido sin necesitad de transformar
							record.setStatus( RecordStatus.VALID_PRE );
						}	
						else {	
							// si no es válido lo transforma
							transformer.transform(record);
							
							// lo vuelve a validar
							validationResult = validator.validate(record);
							
							// registra si resultó válido o es irrecuperable (inválido)
							if ( validationResult.isValid() ) {
								record.setStatus( RecordStatus.VALID_POS );
							} else {					
								record.setStatus( RecordStatus.INVALID );
							}
						}
		
						// Si resultó valido incrementa la cuenta del snapshot
						if ( record.getStatus() != RecordStatus.INVALID )
							snapshot.incrementValidSize();
						
						recordDAO.store(record);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				snapshot.setStatus( SnapshotStatus.PROCESSING );
				snapshot.setEndTime( new Date() );
				snapshotRepository.save(snapshot);
								                       
				//System.out.println( network.getName() + ":" + snapshot.getSize() );
			break;
			
			case ERROR_RETRY:
				System.out.println( event.getMessage() );
				
				snapshot.setStatus( SnapshotStatus.RETRYING );
				snapshot.setEndTime( new Date() );
				snapshotRepository.save(snapshot);
			break;
			
			case ERROR_FATAL:
				System.out.println( event.getMessage() );

				snapshot.setStatus( SnapshotStatus.FINISHED_ERROR );
				snapshotRepository.save(snapshot);
			break;

			default:
				/**
				 * TODO: Definir que se hace en caso de eventos sin status conocido
				 */	
			break;
		}	
	}
	
	/******************** Fin Harvesting *********************/
	
}
