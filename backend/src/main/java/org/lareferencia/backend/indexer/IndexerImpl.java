package org.lareferencia.backend.indexer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.xpath.XPathAPI;
import org.lareferencia.backend.domain.Country;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.harvester.OAIRecordMetadata.OAIRecordMetadataParseException;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class IndexerImpl implements IIndexer{

	private File stylesheet; 
	private Transformer trf; 
	
	private DocumentBuilder builder; 
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	public IndexerImpl() throws IndexerException {
		
		stylesheet = new File("indexer.xsl");
		try {
			trf = MedatadaDOMHelper.buildXSLTTransformer(stylesheet);
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (TransformerConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} catch (ParserConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		}
	}
	
	
	public void index(NetworkSnapshot snapshot) {
		
		 snapshot = networkSnapshotRepository.findOne(16L);	
		 String url = "http://localhost:8983/solr/core0";
		 
		 
		 try {
			HttpSolrServer server = new HttpSolrServer(url);
		
			Page<OAIRecord> records = recordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(1, 100));
			
			DirectXmlRequest request;
			
			request = new DirectXmlRequest("/update", "<delete><query>*:*</query></delete>");
			server.request(request);

			for (OAIRecord record : records.getContent()) {
				System.out.println( record.getId() );
				
				request = new DirectXmlRequest("/update", "<add>" + MedatadaDOMHelper.Node2XMLString(this.transform(record, snapshot.getNetwork()))  + "</add>");
				server.request(request);
				
			}
			
			request = new DirectXmlRequest("/update", "<commit/>");
			server.request(request);

			

				 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	public Document transform(OAIRecord record, NationalNetwork network) throws IndexerException {
		
		
		Document dstDocument = builder.newDocument();
		
		if ( record.getPublishedXML() == null )
			throw new IndexerException("El registro: " + record.getId() + "  no tiene publibledXML definido");
		
		

		try {
			
			OAIRecordMetadata domRecord = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
			
			trf.transform( new DOMSource(domRecord.getDOMDocument()), new DOMResult(dstDocument));
			
			Country country = network.getCountry();
		    //addSolrField(dstDocument, "country", country.getName());
		    //addSolrField(dstDocument, "country_iso", country.getIso());
		    addSolrField(dstDocument, "id", country.getIso() + "_" + record.getSnapshot().getId() + "_" + record.getId()  );		
			
			

		} catch (TransformerException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} catch (OAIRecordMetadataParseException e) {
			e.printStackTrace();
		}
		
		
		return dstDocument;
		
	}
	
	private void addSolrField(Document document, String fieldName, String content) throws DOMException, TransformerException {
		Element elem = document.createElement("field");
		elem.setAttribute("name", fieldName);
		elem.setTextContent(content);
		
		XPathAPI.selectSingleNode(document, "//doc").appendChild(elem);
	}
	
	
}
