package org.lareferencia.backend.tasks;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.harvester.HarvestingEvent;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.harvester.IHarvestingEventListener;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class SnapshotProcessor implements Processor, IHarvestingEventListener {
	
	@Autowired
	public NationalNetworkRepository repository;
	
	IHarvester harvester;

	private NationalNetwork network;
	
	
	public void setNetwork(NationalNetwork network) {
		this.network = network;
	}


	public SnapshotProcessor() {
	};
	
	/**
	 * TODO: Podría ser Async, pero no tiene sentido empezar un nuevo proceso de harvesting para una misma red si el anterior
	 * no terminó. Hay que cuidar los bloqueos!!! TODO: Podría implemetarse el suicidio de procesos para evitar problemas
	 */
	@Override
	public void run() {
		
		/** 
		 * TODO: Es probable que deba refrescarse la red desde la bd todas las corridas, es posible que cambie y genere incosistencias
		 */
		if ( network != null ) {
			try {
			System.out.println("Procesing:" + network.getName() );
			harvester.harvest(network);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Autowired
	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		harvester.addEventListener(this);		
	}
	
	@Override
	public void harvestingEventOccurred(HarvestingEvent event) {
		System.out.println( this.network.getName() + "Evento recibido: " + event.getStatus() );
	}
}
