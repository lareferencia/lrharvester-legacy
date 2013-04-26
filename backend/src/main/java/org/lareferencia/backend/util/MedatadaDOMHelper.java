package org.lareferencia.backend.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class MedatadaDOMHelper  {

	private static Element namespaceElement = null;
	private static DocumentBuilderFactory factory;
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();
	private static HashMap<String, DocumentBuilder> builderMap = new HashMap<String, DocumentBuilder>();

	static {
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			
			DOMImplementation impl = obtainThreadBuider().getDOMImplementation();
	        Document namespaceHolder = impl.createDocument("http://www.openarchives.org/OAI/2.0/oai_dc","oaidc:namespaceHolder", null);
	        
	        /**
	         * TODO: Este listado comprende los namespaces que es capaza de reconocer, esta lista debe ampliarse pues limita los metadatos
	         * que pueden manejarse, actualmente están declarados solo los NS de DC
	         */
	        namespaceElement = namespaceHolder.getDocumentElement();
	        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:oai20", "http://www.openarchives.org/OAI/2.0/");
	        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
	        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:dc", "http://purl.org/dc/elements/1.1/");
	     			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static NodeList getNodeList(Node node, String xpath)
			throws TransformerException {
		return XPathAPI.selectNodeList(node, xpath, namespaceElement);
	}

	public static String getSingleString(Node node, String xpath)
			throws TransformerException {
		return XPathAPI.eval(node, xpath, namespaceElement).str();
	}

	public static XObject getSingleXObjet(Node node, String xpath)
			throws TransformerException {
		return XPathAPI.eval(node, xpath, namespaceElement);
	}

	public static Document parseXML(String xmlstring)
			throws ParserConfigurationException, SAXException, IOException {
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlstring));

		return obtainThreadBuider().parse(is);
	}

	protected static DocumentBuilder obtainThreadBuider() {

		DocumentBuilder builder = builderMap.get(Thread.currentThread()
				.getName());
		if (builder == null) {
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return null;
			}
			builderMap.put(Thread.currentThread().getName(), builder);
		}
		return builder;
	}

	public static String Node2XMLString(Node node) throws TransformerException {

		StringWriter sw = new StringWriter();
		Result output = new StreamResult(sw);
		Transformer idTransformer = xformFactory.newTransformer();
		idTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		idTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
		idTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		idTransformer.transform(new DOMSource(node), output);
		return sw.toString();
	}
	
	public static Transformer buildXSLTTransformer(String xlstString) throws TransformerConfigurationException {
		
		StringReader reader = new StringReader(xlstString);
		StreamSource stylesource = new StreamSource(reader); 
        return xformFactory.newTransformer(stylesource);
	}
	
	public static Transformer buildXSLTTransformer(File stylefile) throws TransformerConfigurationException {
		
		StreamSource stylesource = new StreamSource(stylefile); 
        return xformFactory.newTransformer(stylesource);
	}

}
