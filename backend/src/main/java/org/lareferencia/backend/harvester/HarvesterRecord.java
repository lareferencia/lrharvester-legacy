package org.lareferencia.backend.harvester;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.util.MedatadaDOMHelper;
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
				contents.add( nodelist.item(i).getFirstChild().getNodeValue() );
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

}
