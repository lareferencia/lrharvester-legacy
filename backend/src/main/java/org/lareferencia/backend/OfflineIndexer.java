package org.lareferencia.backend;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.InvalidOccurrenceLogEntry;
import org.lareferencia.backend.domain.ValidationType;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.repositories.InvalidOccurrenceLogRepository;
import org.lareferencia.backend.stats.StatsManager;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.FieldValidationResult;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Component
public  class OfflineIndexer {

	private static final int PAGE_SIZE = 250;

	@Autowired
	public NationalNetworkRepository repository;
	
	@Autowired
	public OAIRecordRepository recordRepository;
	
	
	@Autowired
	public InvalidOccurrenceLogRepository rlogRepository;
	
	
	public OfflineIndexer() {
		
	}
	
	public OAIRecordRepository getRecordRepository() {
		return recordRepository;
	}
	
	public NationalNetworkRepository getRepository() {
		return repository;
	}

	
	public static void main(String[] args) throws TransformerConfigurationException {
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
		
		File stylesheet = new File("indexer.xsl");
		Transformer trf = MedatadaDOMHelper.buildXSLTTransformer(stylesheet);

		OfflineIndexer m = context.getBean("offlineIndexer",OfflineIndexer.class);
		
		IValidator validator = context.getBean("validator", IValidator.class);
		ITransformer trasnformer = context.getBean("transformer", ITransformer.class);
		
		Page<OAIRecord> page = m.recordRepository.findAll( new PageRequest(0, PAGE_SIZE) ); 
		int totalPages = page.getTotalPages();
		
		for (int i=0; i<totalPages; i++) {
			page =  m.recordRepository.findAll( new PageRequest(i, PAGE_SIZE) );
			System.out.println( "Página: " + i + " de: " + totalPages);
			
			for (OAIRecord orecord:page.getContent() ) {
				
				try {
					
					HarvesterRecord hrecord = new HarvesterRecord(orecord.getIdentifier(), 
							MedatadaDOMHelper.parseXML(orecord.getOriginalXML().replace("&#", "#")));
					
					hrecord.addFieldOcurrence("identifier", orecord.getIdentifier());
					
					System.out.println( hrecord.getMetadataXmlString() );
					
					StringWriter sw = new StringWriter();
					Result output = new StreamResult(sw);
					trf.transform( new DOMSource(hrecord.getMetadataDOMnode()), output);
					System.out.println(sw.toString());
					
					
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		}
		
	}	


}
