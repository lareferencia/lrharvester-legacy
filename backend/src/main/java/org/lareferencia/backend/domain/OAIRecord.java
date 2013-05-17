
package org.lareferencia.backend.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;

import org.apache.xpath.XPathAPI;
import org.joda.time.DateTime;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 */
@Entity
@Getter @Setter
public class OAIRecord extends AbstractEntity {
	
	@Column(nullable = false)
	private String identifier;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datestamp;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String originalXML;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String publishedXML;
	
	@Column(nullable = false)
	private RecordStatus status;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)	
	@JoinColumn(name="snapshot_id")
	private NetworkSnapshot snapshot;
	
	

	public OAIRecord() {
		super();
		this.status = RecordStatus.UNTESTED;
	}
	
	public OAIRecord(String identifier, String originalXMLString) {
		super();
		this.status = RecordStatus.UNTESTED;
		this.identifier = identifier;
		this.datestamp = new DateTime().toDate();
		
		this.originalXML = originalXMLString;
	}
	
	/*
	public OAIRecord(String identifier, Document domDocument) {
		super();
		this.status = RecordStatus.UNTESTED;
		this.identifier = identifier;
		this.datestamp = new DateTime().toDate();
		
		domHelper = new DomRecordHelper(domDocument);

		this.originalXML = domHelper.toString();
	}
	*/
	
	
//	/***************** Acceso y modificación del xml via dom  ******************/
//	
//	@Transient
//	public List<String> getFieldOcurrences(String fieldName) {
//		
//		if ( this.metadataDOMnode == null ) {
//			parseNodeXml();
//		}
//		
//		try {
//			NodeList nodelist = MedatadaDOMHelper.getNodeList( this.metadataDOMnode, "//" + fieldName);
//			
//			List<String> contents = new ArrayList<String>(nodelist.getLength());
//			for (int i=0; i<nodelist.getLength(); i++) {
//				try {
//					
//					if ( nodelist.item(i).hasChildNodes() )
//						contents.add( nodelist.item(i).getFirstChild().getNodeValue() );
//				}
//				catch (NullPointerException e) {
//					// Esto no debiera ocurrir nunca
//					//TODO: mejorar el tratamiento de esto
//					System.err.println( "Error obteniendo occurrencias: " + MedatadaDOMHelper.Node2XMLString(nodelist.item(i)) );
//				}
//			}
//			return contents;
//			
//		} catch (TransformerException e) {
//			//TODO: mejorar el tratamiento de esto
//			e.printStackTrace();
//			return new ArrayList<String>(0);
//		}
//	}
//	
//	public void addFieldOcurrence(String fieldName, String content) {
//		
//		if ( this.metadataDOMnode == null ) {
//			parseNodeXml();
//		}
//				
//		Document doc = metadataDOMnode.getOwnerDocument();
//		
//		Element elem = doc.createElementNS("http://purl.org/dc/elements/1.1/", fieldName);
//		elem.setTextContent(content);
//		
//		try {
//			NodeList nodelist = MedatadaDOMHelper.getNodeList( this.metadataDOMnode, "//oai_dc:dc" );
//			nodelist.item(0).appendChild(elem);
//			//XPathAPI.selectSingleNode(metadataDOMnode, "//oai_dc:dc").appendChild(elem);
//		} catch (DOMException e) {
//			//TODO: mejorar el tratamiento de esto
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			//TODO: mejorar el tratamiento de esto
//			e.printStackTrace();
//		}
//	}
//	
//	
//	public List<Node> getFieldNodes(String fieldName) {
//		
//		if ( this.metadataDOMnode == null ) {
//			parseNodeXml();
//		}
//		
//		try {
//			NodeList nodelist = MedatadaDOMHelper.getNodeList( this.metadataDOMnode, "//" + fieldName);
//
//			List<Node> result = new ArrayList<Node>(nodelist.getLength());
//			for (int i=0; i<nodelist.getLength(); i++) {
//				if ( nodelist.item(i).hasChildNodes() )
//					result.add( nodelist.item(i) );
//
//			}
//			return result;
//
//		} catch (TransformerException e) {
//			//TODO: mejorar el tratamiento de esto
//			e.printStackTrace();
//
//			return new ArrayList<Node>(0);
//		}
//	}
//	
//	 /************************ Manejo de la coherencia entre el dom y xmlstring       */ 
//     @PreUpdate
//	 @PrePersist
//	 protected void prePersist() { 
//		 try {
//			this.publishedXML = MedatadaDOMHelper.Node2XMLString( metadataDOMnode );
//		} catch (TransformerException e) {
//			//TODO: Manejar esta exception
//			e.printStackTrace();
//		}	 
//     }
//	
//     @PostLoad
//	 private void parseNodeXml() {
//    	 
//    	String xmlstring = null;
//    	
//    	//if (this.publishedXML != null )
//    	//	xmlstring = this.publishedXML;
//    	//else
//    		xmlstring = this.originalXML;  	
//    	 
//		try {
//			/*TODO: Esto hay que revisarlo*/
//			this.metadataDOMnode = MedatadaDOMHelper.parseXML( xmlstring.replace("&#", "#")).getFirstChild() ;
//		} catch (ParserConfigurationException e) {
//			//TODO: Manejar esta exception
//			e.printStackTrace();
//		} catch (SAXException e) {
//			//TODO: Manejar esta exception
//			e.printStackTrace();
//		} catch (IOException e) {
//			//TODO: Manejar esta exception
//			e.printStackTrace();
//		}
//	 }
		
}
