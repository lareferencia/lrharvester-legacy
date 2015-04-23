package org.lareferencia.backend.util;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lareferencia.backend.harvester.OAIRecordMetadata;


public class OAIMetadataXSLTransformer {
	
	Transformer trf = null;
	
	public OAIMetadataXSLTransformer(String stylesheetFileName) throws TransformerConfigurationException {
		super();
		File stylesheetFile = new File(stylesheetFileName);
			
		trf = MedatadaDOMHelper.buildXSLTTransformer(stylesheetFile);
		trf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trf.setOutputProperty(OutputKeys.INDENT, "no");
		trf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	}
	
	public void setParameter(String name, String value) {
		trf.setParameter(name,value);
	}


	public String transform(OAIRecordMetadata metadata) throws TransformerException {
		
		StringWriter stringWritter = new StringWriter();
		Result output = new StreamResult(stringWritter);
		trf.transform( new DOMSource(metadata.getDOMDocument()), output);
		return stringWritter.toString();
	
	}
	

}
