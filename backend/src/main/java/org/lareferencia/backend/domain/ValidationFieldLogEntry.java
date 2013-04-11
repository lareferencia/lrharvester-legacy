
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
public class ValidationFieldLogEntry extends AbstractEntity {
		
	@Column(nullable = false)
	private ValidationType type;
	
	@Column(nullable = false)
	private Long recordID;
	
	@Column(nullable = false)
	private String fieldName;
	
	@Column(nullable = false)
	private boolean valid;
	
	@Column(nullable = false)
	private boolean mandatory;

	public ValidationFieldLogEntry() {
		super();
	}
	

	public ValidationFieldLogEntry(ValidationType type, Long recordID,
			String fieldName, boolean valid, boolean mandatory) {
		super();
		this.type = type;
		this.recordID = recordID;
		this.fieldName = fieldName;
		this.valid = valid;
		this.mandatory = mandatory;
	}
		
}
