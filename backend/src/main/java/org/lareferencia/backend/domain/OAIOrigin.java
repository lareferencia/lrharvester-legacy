
package org.lareferencia.backend.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
public class OAIOrigin extends AbstractEntity {
	
	public OAIOrigin() {
		super();
		this.metadataPrefix = "oai_dc";
	}
	
	@Column(nullable = false)
	private String uri;
	
	@Column(nullable = false)
	private String metadataPrefix;
	
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="origin_id")
	@LazyCollection(LazyCollectionOption.FALSE)  // Si es LAZY genera problemas durante el procesamiento
	private Collection<OAISet> sets = new LinkedHashSet<OAISet>();
	


}
