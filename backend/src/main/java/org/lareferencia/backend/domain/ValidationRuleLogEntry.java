
package org.lareferencia.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
public class ValidationRuleLogEntry extends AbstractEntity {
		
	@Column(nullable = false)
	private ValidationType type;
	
	@Column(nullable = false)
	private Long recordID;
	
	@Column(nullable = false)
	private String fieldName;
	
	@Column(nullable = false)
	private boolean valid;
	
	@Column(nullable = false)
	private String expected;
	
	@Column(nullable = false)
	private String received;

	

	public ValidationRuleLogEntry() {
		super();
	}

	public ValidationRuleLogEntry(ValidationType type, Long recordID,
			String fieldName, boolean valid, String expected, String received) {
		super();
		this.type = type;
		this.recordID = recordID;
		this.fieldName = fieldName;
		this.valid = valid;
		this.expected = expected;
		this.received = received;
	}	
}
