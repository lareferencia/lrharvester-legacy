package org.lareferencia.backend.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * NationalNetwork Entity
 */
@Entity
@Getter
@Setter
public class NationalNetwork extends AbstractEntity {
	
	@Column(nullable = false)
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="network_id")
	private Collection<OAIOrigin> origins = new LinkedHashSet<OAIOrigin>();
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="country_id")
	private Country country;
    
	
}
