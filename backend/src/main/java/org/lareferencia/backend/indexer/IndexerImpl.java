package org.lareferencia.backend.indexer;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.apache.xpath.XPathAPI;
import org.lareferencia.backend.domain.Country;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@Component
public class IndexerImpl implements IIndexer{

	private File stylesheet; 
	private Transformer trf; 
	
	private DocumentBuilder builder; 
	   
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
	
	public Document transform(OAIRecord record, NationalNetwork network) throws IndexerException {
		
		
		Document srcDocument;
		Document dstDocument = builder.newDocument();

		try {
			trf.transform( new DOMSource(record.getMetadataDOMnode()), new DOMResult(dstDocument));
			
			Country country = network.getCountry();
		    addSolrField(dstDocument, "country", country.getName());
		    addSolrField(dstDocument, "country_iso", country.getIso());
		    addSolrField(dstDocument, "id", country.getIso() + "_" + record.getSnapshot().getId() + "_" + record.getId() );		
			
			

		} catch (TransformerException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
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
