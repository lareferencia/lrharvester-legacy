package org.lareferencia.backend.tasks;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.TransformerException;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.harvester.HarvestingEvent;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.harvester.IHarvesterRecord;
import org.lareferencia.backend.harvester.IHarvestingEventListener;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordDAO;
import org.lareferencia.backend.util.MedatadaDOMHelper;
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
	
	private Long _network_id;

	private NationalNetwork network;

	private NetworkSnapshot snapshot;

	public void setNetworkID(Long networkID) {
		this._network_id = networkID;
	}

	public SnapshotWorker() {
		
		/** 
		 * TODO: // El domHelper es el encargado de interpretar el dom de acuerdo a cada
		 * schema de metadatatos, puede implementarse un factory en spring para crearlos
		 * e injectarlos. Solo se haría necesario para soportar otros schemas de metadatos.
		 * Incluso puede definirse en runtime para cada red, permitiendo distintas redes
		 * con distintos metadatos.
		 * Al ser un requerimiento de baja prioridad queda para el final o futuras implementaciones.
		 * 
		 * **/
	};
	
	
	/**
	 * TODO: Podría ser Async, pero no tiene sentido empezar un nuevo proceso de harvesting para una misma red si el anterior
	 * no terminó. Hay que cuidar los bloqueos!!! TODO: Podría implemetarse la limpieza de procesos inactivos para evitar problemas
	 */
	@Override
	public void run() {
		
		network = networkRepository.findOne( _network_id );
		
		launchInicialization();
		launchHarvesting();
		
		// Cierre del Snapshot
		snapshot.setStatus( SnapshotStatus.FINISHED );
		snapshot.setEndTime( new Date() );
		snapshotRepository.save(snapshot);
		
		// Flush y llamados al GC
		networkRepository.flush();
		snapshotRepository.flush();
		System.gc();
	}
	
	/********************* Inicialization ************************/
	
	private void launchInicialization() {
		
		snapshot = new NetworkSnapshot();
		network.getSnapshots().add(snapshot);
	
		snapshotRepository.save(snapshot);
		networkRepository.save(network);
		
	}
	
	/*************************************************************/
	
	/********************* Harvesting ************************/
	
	@Autowired
	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		harvester.addEventListener(this);		
	}
	
	
	@Override
	@Transactional
	public void harvestingEventOccurred(HarvestingEvent event) {
		
		System.out.println( network.getName() + "Evento recibido: " + event.getStatus() );
			
		switch ( event.getStatus() ) {
			case OK:			
				// Agrega los records al snapshot actual
				
				for (IHarvesterRecord  hRecord:event.getRecords() ) {
		  
					try {
						OAIRecord record = new OAIRecord(hRecord.getIdentifier(), 
														 MedatadaDOMHelper.Node2XMLString(hRecord.getDomNode())
						);
						record.setSnapshot(snapshot);		
			    		recordDAO.store(record);
						
					} catch (TransformerException e) {
						e.printStackTrace();
					}
				}
				
				//recordDAO.store( event.getRecords(), snapshot );
				
				snapshot.setSize( snapshot.getSize() + event.getRecords().size() );	
				snapshotRepository.save(snapshot);
				
			
				
			
				// Este flush mantine mas o menos constante el uso de memoria, quitando los records
				                       
				System.out.println( network.getName() + ":" + snapshot.getSize() );
			break;
			
			case ERROR_RETRY:
				System.out.println( event.getMessage() );
			break;
			
			case ERROR_FATAL:
				System.out.println( event.getMessage() );
			break;

			default:
				/**
				 * TODO: Definir que se hace en caso de eventos sin status conocido
				 */	
			break;
		}	
	}
	
	private void launchHarvesting() {
		
		// pasa el estado del snapshot a harvesting
		snapshot.setStatus( SnapshotStatus.HARVESTING );
		snapshotRepository.save(snapshot);
		
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
	}
	/******************** Fin Harvesting *********************/
	
}
