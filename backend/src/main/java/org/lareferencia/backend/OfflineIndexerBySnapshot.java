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
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.validator.IValidator;
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

	private static final int PAGE_SIZE = 1000;
	private static final int SOLR_FILE_SIZE = 1000;
	private static Transformer toStringTransformer;
	
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder docBuilder;

	static {
		try {
			toStringTransformer = TransformerFactory.newInstance().newTransformer();
			toStringTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			toStringTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			toStringTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			factory = DocumentBuilderFactory.newInstance();
			docBuilder = factory.newDocumentBuilder();

			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	public NationalNetworkRepository repository;

	@Autowired
	public OAIRecordRepository recordRepository;
	
	@Autowired
	public NetworkSnapshotRepository snapshotRepository;

	public OfflineIndexerBySnapshot() {

	}

	public static Document createSolrDocument() {
		
		Document doc = docBuilder.newDocument();
		doc.appendChild( doc.createElement("add") );
		
		return doc;
	}
	
	public static void addSolrDocToSolrAdd(Document solrDoc, Document solrAdd) {
		
		try {
			
			Node solrDocNode = XPathAPI.selectSingleNode(solrDoc, "//doc");
			solrAdd.adoptNode(solrDocNode);
			
			Node solrAddNode = XPathAPI.selectSingleNode(solrAdd, "//add");
			solrAddNode.appendChild(solrDocNode);
			
			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void saveXmlDocument(Document document, String filename) {
		try {
			StreamResult result = new StreamResult(new File(filename));
			toStringTransformer.transform(new DOMSource(document), result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}	
		
	}
	

	public static void main(String[] args) throws TransformerConfigurationException, TransformerFactoryConfigurationError {

		System.out.println("Iniciando ...");
		Logger.getRootLogger().setLevel(Level.OFF);
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/spring/app-context.xml");

		OfflineIndexerBySnapshot m = context.getBean("offlineIndexerBySnapshot",
				OfflineIndexerBySnapshot.class);

		IValidator validator = context.getBean("validator", IValidator.class);
		ITransformer trasnformer = context.getBean("transformer",ITransformer.class);
		IIndexer indexer = context.getBean("indexer", IIndexer.class);
		
		if ( args.length != 1 ) {
			
			System.out.println( "command [snapshotID]" );

			
			for (NationalNetwork network : m.repository.findAll()) {
				for (NetworkSnapshot snapshot : network.getSnapshots()) {
					System.out.println( "Red: " + network.getName() + " \t\tSnapID: " + snapshot.getId() + "\t Records: " + snapshot.getSize() + "\tFinalizado en: " + snapshot.getEndTime() + " Status: " + snapshot.getStatus() );
				}
			}
			
		}
		else {
		
			
			Long snapID = Long.parseLong( args[0] );	
			NetworkSnapshot snapshot = m.snapshotRepository.findOne(snapID);
			NationalNetwork network = snapshot.getNetwork();
			
			
			int actualRecordCount = 0;
			int actualPacket = 0;
			Document actualDocument = createSolrDocument();
			
			
			Page<OAIRecord> page = m.recordRepository.findBySnapshot(snapshot, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();

			for (int i = 0; i < totalPages; i++) {
				
				System.out.println( "Procesando Snapshot/Red: " + snapID + " / " + network.getName() + " paquete: " + i+1 + " de " + totalPages + " " + actualRecordCount);

				page = m.recordRepository.findBySnapshot(snapshot,
						new PageRequest(i, PAGE_SIZE));

				for (OAIRecord record : page.getContent()) {

					try {
						

						// Si no es válido trata de transformarlo
						if (!validator.validate(record).isValid())
							trasnformer.transform(record);

						if (validator.validate(record).isValid()) {
							addSolrDocToSolrAdd(indexer.transform(record, network),actualDocument);
							actualRecordCount++;
							
							if ( actualRecordCount == SOLR_FILE_SIZE ) {
								saveXmlDocument(actualDocument, network.getCountry().getIso() + "_" + actualPacket++ + ".solr.xml");
								actualDocument = createSolrDocument();
								actualRecordCount = 0;
							}
							
						}

					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0); // Si hay un error no continua
					}
				}
			}
			
			// Grabación de la cola de registros de la red actual que no alcanzó SOLR_FILE_SIZE
			if (actualRecordCount != 0) {
				saveXmlDocument(actualDocument, network.getCountry().getIso() + "_" + actualPacket++ + ".solr.xml");
			}
			
		}
		

	}

}


