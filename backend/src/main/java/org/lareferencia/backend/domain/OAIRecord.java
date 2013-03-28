
package org.lareferencia.backend.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

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

	@Lob @Basic(fetch=FetchType.LAZY)
	private String originalXML;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String publishedXML;
	
	@Column(nullable = false)
	private RecordStatus status;

	public OAIRecord() {
		this.status = RecordStatus.UNTESTED;
	}
		
}
