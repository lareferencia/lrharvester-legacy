package org.lareferencia.backend.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

public abstract class BaseMedatadaDOMHelper implements IMetadataDOMHelper {
	
	 protected Element namespaceElement = null;
	 protected DocumentBuilderFactory factory;
	 protected TransformerFactory xformFactory = TransformerFactory.newInstance();
	 protected DocumentBuilder builder; 

	 
	 public BaseMedatadaDOMHelper() {
		 
		 try {

			 	xformFactory = TransformerFactory.newInstance();
			 	
				factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				
				builder = factory.newDocumentBuilder();
				//builder.setErrorHandler(new DefaultErrorHandler());

			
			 
		 } catch (ParserConfigurationException e) {
				e.printStackTrace();
		 }
		 
	  }
	
	protected void setNamespaceElement(Element namespaceElement) {
		this.namespaceElement = namespaceElement;
	}

	public NodeList getNodeList(Node node, String xpath) throws TransformerException {
	        return XPathAPI.selectNodeList(node, xpath, namespaceElement);
	 }
	 
	 public String getSingleString(Node node, String xpath) throws TransformerException {
			return XPathAPI.eval(node, xpath, namespaceElement).str();
	 }
	 
	 public XObject getSingleXObjet(Node node, String xpath) throws TransformerException {
			return XPathAPI.eval(node, xpath, namespaceElement);
	 }
	 
	 public Document parseXML(String xmlstring) throws ParserConfigurationException, SAXException, IOException {
		 	InputSource is = new InputSource();
		    is.setCharacterStream( new StringReader(xmlstring));
	        return builder.parse(is);
	 }
	 
	 public String Node2XMLString(Node node) throws TransformerException  {
				
			StringWriter sw = new StringWriter();
		    Result output = new StreamResult(sw);
			Transformer idTransformer = xformFactory.newTransformer();
	        idTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        idTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        idTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        idTransformer.transform( new DOMSource(node), output);
			return sw.toString();	
	 }

}
