package org.lareferencia.backend.indexer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IndexerImpl implements IIndexer{

	private File stylesheet;
	
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	private static final int PAGE_SIZE = 1000;

	private DocumentBuilder builder; 
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	private HttpSolrServer server;

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

			
			
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (TransformerConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} catch (ParserConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		}
		
		
		return trf;
		
	}
	
	
	@Transactional(readOnly = true)
	public boolean index(NetworkSnapshot snapshot) {
		
		 DirectXmlRequest request;
		 server = new HttpSolrServer(solrURL);
		 
		 try {
			
			Transformer trf = buildTransformer();
 
			 
			// Borrado de los docs del país del snapshot
			this.sendUpdateToSolr("<delete><query>country_iso:" + snapshot.getNetwork().getCountry().getIso() +"</query></delete>");
			
			// Update de los registros de a 1000
			Page<OAIRecord> page = recordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();

			for (int i = 0; i < totalPages; i++) {
				page = recordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(i, PAGE_SIZE));
				
				System.out.println( "Indexando Snapshot: " + snapshot.getId() + " de: " + snapshot.getNetwork().getName() + " página: " + i + " de: " + totalPages);

				String xmlSolrDocsString = null;
								
				for (OAIRecord record : page.getContent()) {
					//xmlSolrDocsString += MedatadaDOMHelper.Node2XMLString(this.transform(record, snapshot.getNetwork()));
					
					
					OAIRecordMetadata domRecord = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
					StringWriter stringWritter = new StringWriter();
					Result output = new StreamResult(stringWritter);
					

					trf.transform( new DOMSource(domRecord.getDOMDocument()), output);

					xmlSolrDocsString = stringWritter.toString();
					
					this.sendUpdateToSolr("<add>" + xmlSolrDocsString + "</add>");
					//System.out.println(record.getIdentifier());

				}
				
				//this.sendUpdateToSolr("<add>" + xmlSolrDocsString + "</add>");
				
				//recordRepository.flush();

			}
			
			
			// commit de los cambios
			this.sendUpdateToSolr("<commit/>");			
				 
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
	
	
	private void sendUpdateToSolr(String data) throws SolrServerException, IOException {
		DirectXmlRequest request = new DirectXmlRequest("/update", data);
		server.request(request);
		
	}
	
	/*
	
	public String transform(OAIRecord record, NationalNetwork network) throws IndexerException {
		
		
		//Document dstDocument = builder.newDocument();
		StringWriter stringWritter = new StringWriter();
		Result output = new StreamResult(stringWritter);
		
		
		
		if ( record.getPublishedXML() == null )
			throw new IndexerException("El registro: " + record.getId() + "  no tiene publibledXML definido");
		
		try {
			
			OAIRecordMetadata domRecord = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
			
			trf.transform( new DOMSource(domRecord.getDOMDocument()), output);
			
			Country country = network.getCountry();
		   // addSolrField(dstDocument, "country", country.getName());
		   // addSolrField(dstDocument, "country_iso", country.getIso());
		   // addSolrField(dstDocument, "id", country.getIso() + "_" + record.getSnapshot().getId() + "_" + record.getId()  );		
			
			

		} catch (TransformerException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} catch (OAIRecordMetadataParseException e) {
			e.printStackTrace();
		}
		
		
		return stringWritter.toString();

		
	}
	
	private void addSolrField(Document document, String fieldName, String content) throws DOMException, TransformerException {
		Element elem = document.createElement("field");
		elem.setAttribute("name", fieldName);
		elem.setTextContent(content);
		
		XPathAPI.selectSingleNode(document, "//doc").appendChild(elem);
	}
	
	*/
}
