package org.lareferencia.backend;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.tasks.ISnapshotWorker;
import org.lareferencia.backend.tasks.SnapshotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public  class OfflineHarvesterByNetwork {

	@Autowired
	public NationalNetworkRepository networkRepository;
	

	public OfflineHarvesterByNetwork() {
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Iniciando ...");
		Logger.getRootLogger().setLevel(Level.OFF);
		
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		OfflineHarvesterByNetwork ofh =  context.getBean("offlineHarvesterByNetwork",OfflineHarvesterByNetwork.class);
		
		
		if ( args.length != 1 ) {
			
			System.out.println( "command [networkID]" );
			
			for (NationalNetwork network : ofh.networkRepository.findAll()) {
				System.out.println( "networkID: " + network.getId() + " name: " + network.getName() ); 
			}
				
		} else {
			
			NationalNetwork network = ofh.networkRepository.findOne( Long.parseLong(args[0]) );
			ISnapshotWorker processor = context.getBean("snapshotWorker", ISnapshotWorker.class);
			processor.setNetworkID(network.getId());
			processor.run();
			
		}
		
	}
		
		

}
