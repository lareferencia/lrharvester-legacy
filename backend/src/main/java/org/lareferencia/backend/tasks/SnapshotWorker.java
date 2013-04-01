package org.lareferencia.backend.tasks;

import java.util.Collection;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.harvester.HarvestingEvent;
import org.lareferencia.backend.harvester.HarvestingEventStatus;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.harvester.IHarvestingEventListener;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(value="prototype")
public class SnapshotWorker implements ISnapshotWorker, IHarvestingEventListener {
	
	@Autowired
	public NationalNetworkRepository networkRepository;
		
	@Autowired
	public NetworkSnapshotRepository snapshotRepository;
	
	@Autowired
	public OAIRecordRepository recordRepository;
	
	private IHarvester harvester;
	
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
		
		if (network != null) {
			
			launchInicialization();
			launchHarvesting();
		}
		
		

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
	public void harvestingEventOccurred(HarvestingEvent event) {
		
		
		System.out.println( network.getName() + "Evento recibido: " + event.getStatus() );
	
	
		if ( event.getStatus() == HarvestingEventStatus.OK ) {
			
			// Agrega los records al snapshot actual
			
			//TODO: Es probable que la persistencia de registros deba implementare saltando el ORM para mejorar la performance.
			recordRepository.save( event.getRecords() );
			snapshot.getRecords().addAll( event.getRecords() );			
			
			snapshotRepository.save(snapshot);
			
			snapshot.setSize( snapshot.getRecords().size() );
			
			System.out.println( network.getName() + ":" + snapshot.getSize() );
	
		} else if ( event.getStatus() == HarvestingEventStatus.ERROR) {
			
		}
		//TODO: Definir que se hace en caso de eventos sin status conocido
		
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
