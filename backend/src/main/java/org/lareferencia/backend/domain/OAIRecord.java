
package org.lareferencia.backend.domain;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 */
@Entity
@Getter @Setter
@JsonIgnoreProperties({"publishedXML","originalXML","snapshot","datestamp"})
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
	
	@Column(nullable = false)
	private boolean belongsToCollection;
	
	@Lob
	@Column(nullable = false) 
	private String belongsToCollectionDetails;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)	
	@JoinColumn(name="snapshot_id")
	private NetworkSnapshot snapshot;
	
	public OAIRecord() {
		super();
		this.status = RecordStatus.UNTESTED;
		this.belongsToCollection = false;
		this.belongsToCollectionDetails = "";
		this.datestamp = new DateTime().toDate();
	}
	
	public OAIRecord(String identifier, String originalXMLString) {
		super();
		this.status = RecordStatus.UNTESTED;
		this.identifier = identifier;
		this.datestamp = new DateTime().toDate();
		this.belongsToCollection = false;
		this.belongsToCollectionDetails = "";
		this.originalXML = originalXMLString;
	}
}
