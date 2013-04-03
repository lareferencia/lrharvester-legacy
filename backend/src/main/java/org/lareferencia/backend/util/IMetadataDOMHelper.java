package org.lareferencia.backend.util;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public interface IMetadataDOMHelper {
	
	 public NodeList getNodeList(Node node, String xpath) throws TransformerException;
	
	 public String getSingleString(Node node, String xpath) throws TransformerException;
	 
	 public XObject getSingleXObjet(Node node, String xpath) throws TransformerException;
	 
	 public Document parseXML(String xmlstring) throws ParserConfigurationException, SAXException, IOException;
	 
	 public String Node2XMLString(Node node) throws TransformerException ;

}
