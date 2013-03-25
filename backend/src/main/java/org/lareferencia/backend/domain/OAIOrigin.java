
package org.lareferencia.backend.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
public class OAIOrigin extends AbstractEntity {
	
	@Column(nullable = false)
	private String uri;
	
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="origin_id")
	private Collection<OAISet> sets = new LinkedHashSet<OAISet>();

}
