
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
public class OAISet extends AbstractEntity {
	
	private String description;
	
	@Column(nullable = false)
	private String name;
	
	

}
