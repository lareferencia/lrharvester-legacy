package org.lareferencia.backend.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

public class OAIMetadataXSLTransformer {

	// ///////////////////////////// STATIC
	// /////////////////////////////////////////
	// private static TransformerFactory xformFactory =
	// TransformerFactory.newInstance();
	// / Ahora se usa saxon para ofrecer xslt2.0
	private static TransformerFactory xformFactory = new net.sf.saxon.TransformerFactoryImpl();

	private static Transformer buildXSLTTransformer(String xlstString) throws TransformerConfigurationException {

		StringReader reader = new StringReader(xlstString);
		StreamSource stylesource = new StreamSource(reader);
		return xformFactory.newTransformer(stylesource);
	}

	private static Transformer buildXSLTTransformer(File stylefile) throws TransformerConfigurationException {

		StreamSource stylesource = new StreamSource(stylefile);
		return xformFactory.newTransformer(stylesource);
	}

	// ///////////////////////////////// FIN STATIC
	// ////////////////////////////////////////////////////////

	private Transformer trf = null;

	public OAIMetadataXSLTransformer(String stylesheetFileName) throws TransformerConfigurationException {
		File stylesheetFile = new File(stylesheetFileName);

		trf = buildXSLTTransformer(stylesheetFile);
		trf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trf.setOutputProperty(OutputKeys.INDENT, "no");
		trf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	}

	public void setParameter(String name, String value) {
		trf.setParameter(name, value);
	}

	public String transform(OAIRecordMetadata metadata) throws TransformerException {

		StringWriter stringWritter = new StringWriter();
		Result output = new StreamResult(stringWritter);
		trf.transform(new DOMSource(metadata.getDOMDocument()), output);
		return stringWritter.toString();

	}

}
