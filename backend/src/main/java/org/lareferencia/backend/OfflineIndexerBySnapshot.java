package org.lareferencia.backend;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OfflineIndexerBySnapshot {


	@Autowired
	public NationalNetworkRepository nationalNetworkRepository;

	@Autowired
	public OAIRecordRepository oaiRecordRepository;
	
	@Autowired
	public NetworkSnapshotRepository networkSnapshotRepository;

	public OfflineIndexerBySnapshot() {

	}


	public static void main(String[] args) throws TransformerConfigurationException, TransformerFactoryConfigurationError {

		System.out.println("Iniciando ...");
		Logger.getRootLogger().setLevel(Level.OFF);
			
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		OfflineIndexerBySnapshot m = context.getBean("offlineIndexerBySnapshot", OfflineIndexerBySnapshot.class);

		IIndexer indexer = context.getBean("indexer", IIndexer.class);
		
		indexer.index( m.networkSnapshotRepository.findOne( Long.parseLong(args[0])) );
	}

}


