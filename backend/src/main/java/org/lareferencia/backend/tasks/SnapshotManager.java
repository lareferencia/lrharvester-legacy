/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.repositories.NetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.TaskScheduler;

@ManagedResource(objectName = "backend:name=snapshotManager", description = "Administrador de snapshots")
public class SnapshotManager {
	
	@Autowired
	private NetworkRepository networkRepository;
	
	@Autowired
	private NetworkSnapshotRepository snapshotRepository;
	
	@Autowired
	private TaskScheduler scheduler;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	
	private ConcurrentHashMap<Long, ISnapshotWorker> workersBySnapshotID;
	
	public SnapshotManager() {
		workersBySnapshotID = new ConcurrentHashMap<Long, ISnapshotWorker>();
	}
	
	@ManagedAttribute
	public List<String> getActiveSnapshots() {
		
		List<String> result = new ArrayList<String>();
		
		for ( Long snapshotID: workersBySnapshotID.keySet() ) {
		
			result.add( snapshotID.toString() + " :: " + workersBySnapshotID.get(snapshotID).getStatus() );
		}
		return result;
	}
	
	@ManagedAttribute
	public Integer getActiveSnapshotsCount() {	
		return workersBySnapshotID.size();
	}
	
	@Autowired 
	public void setNationalNetworkRepository(NetworkRepository networkRepository) {
		this.networkRepository = networkRepository;
		
		scheduleAllNetworks();
	}
	
	
	public void registerWorkerBeginSnapshot(Long snapshotID, ISnapshotWorker worker) {
		workersBySnapshotID.put(snapshotID, worker);
	}
	
	public void registerWorkerEndSnapshot(Long snapshotID) {
		workersBySnapshotID.remove(snapshotID);
	}
	
	/**
	 * Consulta el repositorio, obtiene las redes, y actualiza el estado de los procesos
	 */	
	private synchronized void scheduleAllNetworks() {
		
		/** TODO: Hay que implementar una política de refresh más completa,
		 *  Son varios los casos a analizar e iran siendo contemplados en futuras
		 *  iteraciones.
		 */
		Collection<Network> storedNetworks = networkRepository.findAll();
		
		for ( Network storedNetwork:storedNetworks ) {
			ISnapshotWorker worker = (ISnapshotWorker) applicationContext.getBean("snapshotWorker");
			worker.setNetworkID(storedNetwork.getId());
			worker.setManager(this);
			scheduler.schedule(worker, new SnapshotCronTrigger(storedNetwork) );
		}
	}
	
	
	public synchronized void stopHarvesting(Long snapshotID) {
		try {
			workersBySnapshotID.get(snapshotID).stop();
		} catch (NullPointerException e) {
			
			// En caso de que no sea un snapshot en actividad pero tenga status harvesting lo pasa a status stopped
			NetworkSnapshot snapshot = snapshotRepository.findOne(snapshotID);
			
			if ( snapshot != null 
					&& (    snapshot.getStatus().equals( SnapshotStatus.HARVESTING ) || 
							snapshot.getStatus().equals( SnapshotStatus.INDEXING ) ||
							snapshot.getStatus().equals( SnapshotStatus.RETRYING )  
						)
				)
							{
				snapshot.setStatus( SnapshotStatus.HARVESTING_STOPPED );
				snapshotRepository.save(snapshot);
				snapshotRepository.flush();
			}
			
			System.err.print("No existe el snapshot");
		}
	}
	
	public synchronized void lauchHarvesting(Long networkD) {
		//TODO: debiera chequear la existencia de la red

		ISnapshotWorker worker = (ISnapshotWorker) applicationContext.getBean("snapshotWorker");
		worker.setHarvestBySet(false);
		worker.setNetworkID(networkD);
		worker.setManager(this);
		scheduler.schedule(worker, new Date());
	}
	
	public synchronized void lauchSetBySetHarvesting(Long networkD) {
		//TODO: debiera chequear la existencia de la red

		ISnapshotWorker worker = (ISnapshotWorker) applicationContext.getBean("snapshotWorker");
		worker.setHarvestBySet(true);
		worker.setNetworkID(networkD);
		worker.setManager(this);
		scheduler.schedule(worker, new Date());
	}
	
	public synchronized void relauchHarvesting(Long snapshotID) {

		NetworkSnapshot snapshot = snapshotRepository.findOne(snapshotID);
		
		if ( snapshot != null && snapshot.getStatus() == SnapshotStatus.HARVESTING_STOPPED ) {
			ISnapshotWorker worker = (ISnapshotWorker) applicationContext.getBean("snapshotWorker");
			worker.setSnapshotID(snapshotID);
			worker.setManager(this);
			scheduler.schedule(worker, new Date());
		}
		//TODO: Hbaría que tirar una except acá
	}
	
	
}
