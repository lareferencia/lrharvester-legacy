/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend.harvester;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import lombok.Getter;
import lombok.Setter;

import org.apache.xpath.XPathAPI;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class OAIRecordMetadata {
	
	private static Element namespaceElement = null;
	private static DocumentBuilderFactory factory;
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();
	private static HashMap<String, DocumentBuilder> builderMap = new HashMap<String, DocumentBuilder>();

	
	/***** Static ******/
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
	/***** Fin Static ******/
	
	private Document DOMDocument;
	
	@Getter
	private String identifier;
	
	@Getter
	@Setter
	private String origin;
	
	@Getter
	@Setter
	private String setSpec;
	

	public Document getDOMDocument() {
		return DOMDocument;
	}

	public OAIRecordMetadata(String identifier, String xmlString) throws OAIRecordMetadataParseException {
		
		this.identifier = identifier;
		
		try {
			
			DOMDocument = parseXML(xmlString);
			
		} catch (ParserConfigurationException e) {
			throw new OAIRecordMetadataParseException("Error  en: " + this.getClass().toString(), e);
			
		} catch (SAXException e) {
			throw new OAIRecordMetadataParseException("Error en xml parse en: " + this.getClass().toString(), e );
		} catch (IOException e) {
			throw new OAIRecordMetadataParseException("Error  en: " + this.getClass().toString(), e);
		}
	}
	
	public OAIRecordMetadata(Document domDocument) {
		
		this.DOMDocument = domDocument;
	}
	
	
	public List<String> getFieldOcurrences(String fieldName) {	
		try {
			NodeList nodelist = XPathAPI.selectNodeList(DOMDocument, "//" + fieldName, namespaceElement);
								
			List<String> contents = new ArrayList<String>(nodelist.getLength());
			for (int i=0; i<nodelist.getLength(); i++) {
				try {
					
					if ( nodelist.item(i).hasChildNodes() )
						contents.add( nodelist.item(i).getFirstChild().getNodeValue() );
				}
				catch (NullPointerException e) {
					// Esto no debiera ocurrir nunca
					//TODO: mejorar el tratamiento de esto
					System.err.println( "Error obteniendo occurrencias: " + MedatadaDOMHelper.Node2XMLString(nodelist.item(i)) );
				}
			}
			return contents;
			
		} catch (TransformerException e) {
			//TODO: mejorar el tratamiento de esto
			e.printStackTrace();
			return new ArrayList<String>(0);
		}
	}
	
	public void addFieldOcurrence(String fieldName, String content) {
		
			
		Element elem = DOMDocument.createElementNS("http://purl.org/dc/elements/1.1/", fieldName);
		elem.setTextContent(content);
		
		try {
			NodeList nodelist = XPathAPI.selectNodeList(DOMDocument,  "//oai_dc:dc", namespaceElement);					
			nodelist.item(0).appendChild(elem);
			//XPathAPI.selectSingleNode(metadataDOMnode, "//oai_dc:dc").appendChild(elem);
		} catch (DOMException e) {
			//TODO: mejorar el tratamiento de esto
			e.printStackTrace();
		} catch (TransformerException e) {
			//TODO: mejorar el tratamiento de esto
			e.printStackTrace();
		}
	}
	
	public void removeFieldNode(Node node) {
		Node fieldNode = node.getParentNode();
		fieldNode.removeChild(node);
	}
	
	
	public List<Node> getFieldNodes(String fieldName) {

		try {
			
			NodeList nodelist = XPathAPI.selectNodeList(DOMDocument, "//" + fieldName, namespaceElement);
		
			List<Node> result = new ArrayList<Node>(nodelist.getLength());
			for (int i=0; i<nodelist.getLength(); i++) {
				if ( nodelist.item(i).hasChildNodes() )
					result.add( nodelist.item(i) );

		}
		return result;

		} catch (TransformerException e) {
			//TODO: mejorar el tratamiento de esto
			e.printStackTrace();

			return new ArrayList<Node>(0);
		}
	}
	

	
	public static Document parseXML(String xmlstring) throws ParserConfigurationException, SAXException, IOException {
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlstring));
		return obtainThreadBuider().parse(is);
	}


	@Override
	public String toString() {
		
		try {
			StringWriter sw = new StringWriter();
			Result output = new StreamResult(sw);
			Transformer idTransformer = xformFactory.newTransformer();
			idTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			idTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			idTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			idTransformer.transform(new DOMSource(DOMDocument), output);
			return sw.toString();
			
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error de transformación de DOM a XML";
		}
	}
	
	
	
	public class OAIRecordMetadataParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2279626648050333938L;

		public OAIRecordMetadataParseException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public OAIRecordMetadataParseException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public OAIRecordMetadataParseException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public OAIRecordMetadataParseException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}
		
	}
	
}
