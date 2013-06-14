package org.lareferencia.backend.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.Setter;

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
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordDAO;
import org.lareferencia.backend.repositories.OAIRecordRepository;
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
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private OAIRecordDAO recordDAO;
	
	private IHarvester harvester;
	
	@Autowired
	private IValidator validator;
	
	@Autowired
	private ITransformer transformer;
	
	@Autowired
	private IIndexer indexer;
	
	@Setter
	private Long networkID;
	
	@Setter
	private Long snapshotID;

	private NationalNetwork network;
	private NetworkSnapshot snapshot;

	
	public SnapshotWorker() {
	};
	
	public NetworkSnapshot getSnapshot() {
		return snapshot;
	}

	/**
	 * TODO: Podría ser Async, pero no tiene sentido empezar un nuevo proceso de harvesting para una misma red si el anterior
	 * no terminó. Hay que cuidar los bloqueos!!! TODO: Podría implemetarse la limpieza de procesos inactivos para evitar problemas
	 * @throws Exception 
	 */
	@Override
	public void run() {
		
		
		boolean newSnapshotCreated = true;
		
		if ( snapshotID != null ) { // Este es el caso en que se retorma un snapshot bloqueado 
	
			snapshot = snapshotRepository.findOne( snapshotID );
			
			if ( snapshot != null && snapshot.getStatus().equals(SnapshotStatus.HARVESTING_STOPPED) ) { // tiene que estar detenido
				
				network = snapshot.getNetwork(); 
				newSnapshotCreated = false;
				
			} else {
				System.err.println("El snapshot no existe o está ya está siendo procesado. El worker no puede continuar");
				return;
			}
						
	
		} else { // este es el caso donde se inicia un nuevo snapshot
			
			network = networkRepository.findOne( networkID );
				
			if ( network != null ) {			
				snapshot = new NetworkSnapshot();
				snapshot.setNetwork(network);		
			} else {
				System.err.println("La Red no existe!! El worker no puede continuar");
				return;
			}
				
		}
		
		snapshot.setStatus( SnapshotStatus.HARVESTING );
		snapshotRepository.save(snapshot);

		// harvest de la red
		if ( newSnapshotCreated ) // caso de harvesting desde cero
			harvestEntireNetwork();
		else // caso de retomar desde un rt anterior
			harvestNetworkFromRT(snapshot.getResumptionToken());

		
		// Si no generó errores terminó exitoso la cosecha
		if ( snapshot.getStatus() != SnapshotStatus.HARVESTING_FINISHED_ERROR ) {
			
			// Lo setea como cosechado exitosamente
			snapshot.setStatus( SnapshotStatus.HARVESTING_FINISHED_VALID );
			snapshot.setStatus( SnapshotStatus.INDEXING );
			
			snapshot.setEndTime( new Date() );
			snapshotRepository.save(snapshot);
			
			if ( network.isRunIndexing() ) {
				
				// Indexa
				boolean isSuccesfullyIndexed = indexer.index(snapshot);
				
				// Si el indexado es exitoso marca el snap válido
				if ( isSuccesfullyIndexed )
					snapshot.setStatus( SnapshotStatus.VALID );
				else
					snapshot.setStatus( SnapshotStatus.INDEXING_FINISHED_ERROR );
			}
		}
	
		snapshot.setEndTime( new Date() );
		snapshotRepository.save(snapshot);
		
		// Flush y llamados al GC
		snapshotRepository.flush();
		System.gc();
	}
	
	/*************************************************************/
	
	private void harvestNetworkFromRT(String resumptionToken) {	
		// Se recorren los orígenes evaluando de cual era el rt TODO: Hay que guardar el origen corriente en el snapshot
		for ( OAIOrigin origin:network.getOrigins() ) {
			harvester.harvest(origin.getUri(), null, null, null, origin.getMetadataPrefix(), resumptionToken);
		}
	}
	
	private void harvestEntireNetwork() {
		// Ciclo principal de procesamiento, dado por la estructura de la red nacional
		// Se recorren los orígenes 
		for ( OAIOrigin origin:network.getOrigins() ) {
			
			// si tiene sets declarados solo va a cosechar 
			if ( origin.getSets().size() > 0 ) {
				for ( OAISet set: origin.getSets() ) {
					harvester.harvest(origin.getUri(), null, null, set.getSpec(), origin.getMetadataPrefix(), null);
				}
			}
			// si no hay set declarado cosecha todo
			else {
				harvester.harvest(origin.getUri(), null, null, null, origin.getMetadataPrefix(), null);
			}	
		}
	}
	
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
				
				
				List<OAIRecord> records = new ArrayList<OAIRecord>(100);
			
				// Agrega los records al snapshot actual			
				for (OAIRecordMetadata  metadata:event.getRecords() ) {
					
					try {
						OAIRecord record = new OAIRecord(metadata.getIdentifier(), metadata.toString());
						// registra el snapshot al que pertenece
						record.setSnapshot(snapshot);
						
						snapshot.incrementSize();
						
						if ( network.isRunValidation() ) {
						
							// prevalidación
							ValidationResult validationResult = validator.validate(metadata);
							
							// si no es válido lo transforma
							if ( !validationResult.isValid() && network.isRunTransformation() ) {
								snapshot.incrementTransformedSize();
								transformer.transform(metadata, validationResult);
								// lo vuelve a validar
								validationResult = validator.validate(metadata);
							}
							
							// registra si resultó válido o es irrecuperable (inválido)
							if ( validationResult.isValid() ) {
								record.setStatus( RecordStatus.VALID );
								snapshot.incrementValidSize();
							} else {
								record.setStatus( RecordStatus.INVALID );
							}
								
							// Se almacena la metadata transformada para los registros válidos
							if ( validationResult.isValid() ) 
								record.setPublishedXML( metadata.toString() );
						} 
						
						else { // Si no se valida entonces todo puesto para publicación
							// TODO: Chequear esto
							record.setStatus( RecordStatus.UNTESTED );
							record.setPublishedXML( metadata.toString() );
						}
						
						records.add(record);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				recordRepository.save(records);

				
				snapshot.setStatus( SnapshotStatus.HARVESTING );
				snapshot.setEndTime( new Date() );
				snapshot.setResumptionToken( event.getResumptionToken() );
				snapshotRepository.save(snapshot);
				
				recordRepository.flush();
				snapshotRepository.flush();
								                       
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
				snapshot.setStatus( SnapshotStatus.HARVESTING_FINISHED_ERROR );
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
