package org.lareferencia.backend.indexer;

import java.io.File;

import org.apache.bcel.generic.SWITCH;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class IntelligoIndexer implements IIndexer{

	private File stylesheet;
	private String outputPath;
	
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	private static final int PAGE_SIZE = 500;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	
	public IntelligoIndexer(String xslFileName, String langProfileDirectory, String outputPath) throws IndexerException {
		this.stylesheet = new File(xslFileName);
		this.outputPath = outputPath;
		
		try {
			DetectorFactory.loadProfile(langProfileDirectory);
		} catch (LangDetectException e) {
			System.err.println("!!!! Error al cargar los perfiles del detector de idiomas en: " + langProfileDirectory);
		}

	}
	
	private Transformer buildTransformer() throws IndexerException {
		
		Transformer trf; 

		try {
		
			StreamSource stylesource = new StreamSource(stylesheet); 
	        trf = xformFactory.newTransformer(stylesource);
			
			trf = MedatadaDOMHelper.buildXSLTTransformer(stylesheet);
			trf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trf.setOutputProperty(OutputKeys.INDENT, "yes");
			trf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
		} catch (TransformerConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} 
		
		return trf;
		
	}
	
	
	/* Este método es syncronized para asegurar que no se superpongan dos indexaciones y los commits solr (not isolated) se produzan*/
	public synchronized boolean index(NetworkSnapshot snapshot) {
		
		 
		 try {	
			// Borrado de los docs del país del snapshot
			 
				
			Page<OAIRecord> page = recordRepository.findBySnapshotIdAndStatus(snapshot.getId(), RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();
						

			for (int i = 0; i < totalPages; i++) {
							
				Transformer trf = buildTransformer();
				
				//trf.setParameter("country", snapshot.getNetwork().getName() );
				
				page = recordRepository.findBySnapshotIdAndStatus(snapshot.getId(), RecordStatus.VALID, new PageRequest(i, PAGE_SIZE) );
				
				System.out.println( "Indexando Snapshot: " + snapshot.getId() + " de: " + snapshot.getNetwork().getName() + " página: " + i + " de: " + totalPages);
								
				StringBuffer strBuf = new StringBuffer();
				List<OAIRecord> records = page.getContent();

					strBuf.append("<add>");

					for (OAIRecord record : records) {
						
						OAIRecordMetadata metadata = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
						StringWriter stringWritter = new StringWriter();
						Result output = new StreamResult(stringWritter);
									
						// id unico pero mutable para solr
						trf.setParameter("solr_id",  record.getId().toString()  );
						
						/////// DC:DESCRIPTION - Detección y división de idiomas 
						String ab_es = "";
						String ab_en = "";
						String ab_pt = "";
						for ( String ab : metadata.getFieldOcurrences("dc:description") ) {	
							String lang = detectLang(ab);				
							switch (lang) {
								case "es": ab_es += ab; break;
								case "en": ab_en += ab; break;
								case "pt": ab_pt += ab; break;		
							}
						}	
						trf.setParameter("ab_es",  ab_es);
						trf.setParameter("ab_en",  ab_en);
						trf.setParameter("ab_pt",  ab_pt);
						/////////////////////////////////////////////////////////////
						
						/////// DC:title - Detección y división de idiomas 
						String ti_es = "";
						String ti_en = "";
						String ti_pt = "";
						for ( String ti : metadata.getFieldOcurrences("dc:title") ) {	
							String lang = detectLang(ti);				
							switch (lang) {
								case "es": ti_es += ti; break;
								case "en": ti_en += ti; break;
								case "pt": ti_pt += ti; break;		
							}
						}	
						trf.setParameter("ti_es",  ti_es);
						trf.setParameter("ti_en",  ti_en);
						trf.setParameter("ti_pt",  ti_pt);
						/////////////////////////////////////////////////////////////
						
						
						// Se transforma y genera el string del registro
						trf.transform( new DOMSource(metadata.getDOMDocument()), output);
						strBuf.append(stringWritter.toString());						
					}
				
					strBuf.append("</add>");
					
			        BufferedWriter out = new BufferedWriter(new FileWriter(outputPath + "/" + snapshot.getId() + "_" + i + ".solr.xml"));
			        out.write( strBuf.toString() );
			        out.close();
			}
				 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	private String detectLang(String text) {
		
		Detector detector;
		try {
			detector = DetectorFactory.create();
			detector.append(text);
			return detector.detect();
			
		} catch (LangDetectException e) {
			System.err.print("Error creando detector de idioma.");
			return "00";
		}
	
	}
	
}
