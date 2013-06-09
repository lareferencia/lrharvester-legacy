package org.lareferencia.backend;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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


