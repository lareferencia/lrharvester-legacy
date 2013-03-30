package org.lareferencia.backend.tasks;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.harvester.HarvestingEvent;
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
	
	private IHarvester harvester;
	
	private NetworkSnapshot snapshot;

	private NationalNetwork network;
	
	
	public void setNetwork(NationalNetwork network) {
		this.network = network;
	}


	public SnapshotWorker() {
	};
	
	/**
	 * TODO: Podría ser Async, pero no tiene sentido empezar un nuevo proceso de harvesting para una misma red si el anterior
	 * no terminó. Hay que cuidar los bloqueos!!! TODO: Podría implemetarse el suicidio de procesos para evitar problemas
	 */
	@Override
	public void run() {
		
		snapshot = new NetworkSnapshot();
		snapshotRepository.save(snapshot);
	
		/** 
		 * TODO: Es probable que deba refrescarse la red desde la bd todas las corridas, es posible que cambie y genere incosistencias
		 */
		if ( network != null ) {
			try {
				launchHarvesting();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/********************* Harvesting ************************/
	
	@Autowired
	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		harvester.addEventListener(this);		
	}
	
	@Override
	@Transactional
	public void harvestingEventOccurred(HarvestingEvent event) {
		System.out.println( this.network.getName() + "Evento recibido: " + event.getStatus() );
		
		snapshot.getRecords().addAll(event.getRecords());
		
		
		
	}
	
	public void launchHarvesting() {
		
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
