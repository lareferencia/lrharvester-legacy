
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
public class InvalidOccurrenceLogEntry extends AbstractEntity {

	
	@Column(nullable = false)
	private Long snapID;
	
	@Column(nullable = false)
	private String fieldName;
	
	@Column(nullable = false)
	private String expected;
	
	@Column(nullable = false)
	private String received;

	public InvalidOccurrenceLogEntry() {
		super();
	}

	public InvalidOccurrenceLogEntry(Long snapID,
			String fieldName, String expected, String received) {
		super();
		this.snapID = snapID;
		this.fieldName = fieldName;
		this.expected = expected;
		this.received = received;
	}	
}
