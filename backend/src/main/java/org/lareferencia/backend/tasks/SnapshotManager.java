package org.lareferencia.backend.tasks;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;

public class SnapshotManager {
	
	private NationalNetworkRepository networkRepository;
	
	@Autowired
	private TaskScheduler scheduler;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	private ConcurrentLinkedQueue<ISnapshotWorker> workers;
	
	
	public SnapshotManager() {
		workers = new ConcurrentLinkedQueue<ISnapshotWorker>();
	}
	
	@Autowired 
	public void setNationalNetworkRepository(NationalNetworkRepository networkRepository) {
		this.networkRepository = networkRepository;
		
		refresh();
	}
	
	
	public Collection<ISnapshotWorker> getWorkers() {
		return workers;
	}

	
	/**
	 * Consulta el repositorio, obtiene las redes, y actualiza el estado de los procesos
	 */	
	private synchronized void refresh() {
		
		/** TODO: Hay que implementar una política de refresh más completa,
		 *  Son varios los casos a analizar e iran siendo contemplados en futuras
		 *  iteraciones.
		 */
		Collection<NationalNetwork> storedNetworks = networkRepository.findAll();
		
		for ( NationalNetwork storedNetwork:storedNetworks ) {
			ISnapshotWorker worker = (ISnapshotWorker) applicationContext.getBean("snapshotWorker");
			worker.setNetworkID(storedNetwork.getId());
			workers.add(worker);
			scheduler.schedule(worker, new SnapshotCronTrigger(storedNetwork) );
		}
	}
	
	public synchronized void lauchHarvesting(Long networkD) {
		//TODO: debiera chequear la existencia de la red

		ISnapshotWorker worker = (ISnapshotWorker) applicationContext.getBean("snapshotWorker");
		worker.setNetworkID(networkD);
		scheduler.schedule(worker, new Date());
	}
	
	
}
