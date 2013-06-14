package org.lareferencia.backend.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
	
	@Column(nullable = false, length = 2)
	private String countryISO;
	
	@Column(nullable = false)
	private boolean published = false;
		
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="schedule_id")
	private Schedule schedule;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="network_id")
	@LazyCollection(LazyCollectionOption.FALSE)  // Si es LAZY genera problemas durante el procesamiento
	private Collection<OAIOrigin> origins = new LinkedHashSet<OAIOrigin>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="network_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<NetworkSnapshot> snapshots = new LinkedHashSet<NetworkSnapshot>();
	
	@Column(nullable = false)
	private boolean runIndexing = true;
	
	@Column(nullable = false)
	private boolean runValidation = true;
	
	@Column(nullable = false)
	private boolean runTransformation = true;
}
