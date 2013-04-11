package org.lareferencia.backend.harvester;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@Getter
@Setter
public class HarvesterRecord {
	
	private Node metadataDOMnode;	
	private String identifier;

	public HarvesterRecord() {
		super();
	}

	public HarvesterRecord(String identifier, Node metadataDOMnode) {
		this.metadataDOMnode = metadataDOMnode;
		// se asegura normalizar los contenidos de los campos
		this.metadataDOMnode.normalize(); 
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<String> getFieldOcurrences(String fieldName) {
		
		
		try {
			NodeList nodelist = MedatadaDOMHelper.getNodeList( this.metadataDOMnode, "//" + fieldName);
			

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
	
	
	public String getMetadataXmlString() {
		try {
			return MedatadaDOMHelper.Node2XMLString( metadataDOMnode );
		} catch (TransformerException e) {
			//TODO: mejorar el tratamiento de esto
			e.printStackTrace();
			return "";
		}
	}

	public List<Node> getFieldNodes(String fieldName) {
		try {
			NodeList nodelist = MedatadaDOMHelper.getNodeList( this.metadataDOMnode, "//" + fieldName);
			
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
	

	private Node getRootNode() {
		try {
			NodeList nodelist = MedatadaDOMHelper.getNodeList( this.metadataDOMnode, "//oai_dc:dc" );
			return nodelist.item(0);
			
			
		} catch (TransformerException e) {
			//TODO: mejorar el tratamiento de esto
			e.printStackTrace();

			return null;
		}
	}
	
	public void addFieldOcurrence(String fieldName, String content) {
		Document doc = (Document) metadataDOMnode;
		Element e = doc.createElementNS("http://purl.org/dc/elements/1.1/", fieldName);
		e.setTextContent(content);
		this.getRootNode().appendChild(e);
	}

}
