package org.lareferencia.backend.indexer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import lombok.Synchronized;

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
import org.springframework.transaction.annotation.Transactional;

public class IndexerImpl implements IIndexer{

	private File stylesheet;
	
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	private static final int PAGE_SIZE = 100;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	private String solrURL;
	
	public IndexerImpl(String xslFileName, String solrURL) throws IndexerException {
		this.stylesheet = new File(xslFileName);
		this.solrURL = solrURL;
		
	}
	
	private Transformer buildTransformer() throws IndexerException {
		
		Transformer trf; 

		try {
		
			StreamSource stylesource = new StreamSource(stylesheet); 
	        trf = xformFactory.newTransformer(stylesource);
			
			trf = MedatadaDOMHelper.buildXSLTTransformer(stylesheet);
			trf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trf.setOutputProperty(OutputKeys.INDENT, "no");
			trf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
		} catch (TransformerConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} 
		
		return trf;
		
	}
	
	
	/* Este método es syncronized para asegurar que no se superpongan dos indexaciones y los commits solr (not isolated) se produzan*/
	@Transactional(readOnly = true)
	public synchronized boolean index(NetworkSnapshot snapshot) {
		
		 HttpSolrServer server = new HttpSolrServer(solrURL);
		 
		 try {
			
			Transformer trf = buildTransformer();
			trf.setParameter("country_iso", snapshot.getNetwork().getCountry().getIso() );
			trf.setParameter("country", snapshot.getNetwork().getCountry().getName() );
			 
			// Borrado de los docs del país del snapshot
			this.sendUpdateToSolr(server, "<delete><query>country_iso:" + snapshot.getNetwork().getCountry().getIso() +"</query></delete>");
			
			// Update de los registros de a 1000
			Page<OAIRecord> page = recordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();

			for (int i = 0; i < totalPages; i++) {
				page = recordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(i, PAGE_SIZE));
				
				System.out.println( "Indexando Snapshot: " + snapshot.getId() + " de: " + snapshot.getNetwork().getName() + " página: " + i + " de: " + totalPages);
								
				String solrRecordsXmlString = "";
				
				for (OAIRecord record : page.getContent()) {
					
					OAIRecordMetadata domRecord = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
					StringWriter stringWritter = new StringWriter();
					Result output = new StreamResult(stringWritter);
					
					trf.setParameter("register_id", record.getSnapshot().getId() + "_" + record.getId()  );
					trf.transform( new DOMSource(domRecord.getDOMDocument()), output);
					
					solrRecordsXmlString += stringWritter.toString();

				}
				
				this.sendUpdateToSolr(server, "<add>" + solrRecordsXmlString + "</add>");

			}
			
			// commit de los cambios
			server.commit();		
				 
		} catch (Exception e) {
			e.printStackTrace();
			try {
				server.rollback();
			} catch (SolrServerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}

		return true;
	}
	
	
	private void sendUpdateToSolr(HttpSolrServer server, String data) throws SolrServerException, IOException {
		DirectXmlRequest request = new DirectXmlRequest("/update", data);
		server.request(request);
		
	}
	
	
}
