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
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="network_id")
	private Collection<NetworkSnapshot> snapshots = new LinkedHashSet<NetworkSnapshot>();
	
	
	/** TODO: Hay que revisar la regla cascade para pais, es debil o fuerte respecto de la red,
	 *  Es decir, si sacamos la red, tiene sentido que exista el pais en nuestro dominio?
	 */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="country_id")
	private Country country;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="schedule_id")
	private Schedule schedule;
    
	
}
