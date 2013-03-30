
package org.lareferencia.backend.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
public class OAIRecord extends AbstractEntity {
	
	@Column(nullable = false)
	private String identifier;
	
	@Temporal(TemporalType.TIMESTAMP)
	//@Column(nullable = false)
	private Date datestamp;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String originalXML;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String publishedXML;
	
	@Column(nullable = false)
	private RecordStatus status;

	public OAIRecord() {
		this.status = RecordStatus.UNTESTED;
	}
	
	public OAIRecord(String identifier, Date datestamp, String xmlstring) {
		this.status = RecordStatus.UNTESTED;
		this.identifier = identifier;
		this.datestamp = datestamp;
		this.originalXML = xmlstring;
		
	}
		
}
