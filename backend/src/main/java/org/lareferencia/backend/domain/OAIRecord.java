
package org.lareferencia.backend.domain;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import lombok.EqualsAndHashCode;
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
	@Column(nullable = false)
	private Date datestamp;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String originalXML;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String publishedXML;
	
	@Column(nullable = false)
	private RecordStatus status;
	
	@ManyToOne(fetch=FetchType.LAZY,optional=false)	
	@JoinColumn(name="snapshot_id")
	private NetworkSnapshot snapshot;

	public OAIRecord() {
		super();
		this.status = RecordStatus.UNTESTED;
	}
	
	public OAIRecord(String identifier, String xmlstring) {
		super();
		this.status = RecordStatus.UNTESTED;
		this.identifier = identifier;
		this.datestamp = new DateTime().toDate();
		this.originalXML = xmlstring;
		
	}
		
}
