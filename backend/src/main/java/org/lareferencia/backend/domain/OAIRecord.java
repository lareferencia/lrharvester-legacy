
package org.lareferencia.backend.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
public class OAIRecord extends AbstractEntity {
	
	@Column(nullable = false, unique = true)
	private String identifier;

	@Lob @Basic(fetch=FetchType.LAZY)
	private String originalXML;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private String publishedXML;
}
