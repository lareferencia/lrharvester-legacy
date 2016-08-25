/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.Transient;

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
public class Network extends AbstractEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String institutionName;

	@Column(nullable = false, length = 20, unique = true)
	private String acronym;

	@OneToMany(cascade = CascadeType.ALL/*, orphanRemoval=true*/)
	@JoinColumn(name = "network_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	// Si es LAZY genera problemas durante el procesamiento
	private Collection<OAIOrigin> origins = new LinkedHashSet<OAIOrigin>();

	@OneToMany(cascade = CascadeType.ALL /*, orphanRemoval=true*/)
	@JoinColumn(name = "network_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<NetworkSnapshot> snapshots = new LinkedHashSet<NetworkSnapshot>();

	@Column(nullable = false)
	private boolean published = false;
	/*
	 * @Column(nullable = false) private boolean runIndexing = true;
	 * 
	 * @Column(nullable = false) private boolean runValidation = true;
	 * 
	 * @Column(nullable = false) private boolean runTransformation = true;
	 * 
	 * @Column(nullable = false) private boolean runStats = false;
	 * 
	 * @Column(nullable = false) private boolean runXOAI = false;
	 */

	private String scheduleCronExpression;

	@OneToMany(cascade = CascadeType.ALL/*, orphanRemoval=true*/)
	@JoinColumn(name = "network_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<NetworkProperty> properties = new LinkedHashSet<NetworkProperty>();;

	@Getter
	@Setter
	@ManyToOne()
	@JoinColumn(name = "validator_id", nullable = true)
	private Validator validator;

	@Getter
	@Setter
	@ManyToOne()
	@JoinColumn(name = "transformer_id", nullable = true)
	private Transformer transformer;

	/***
	 * Método de ayuda para lectura de propiedade booleanas si la propieda
	 * existe devuelve su valor o false en otro caso
	 * 
	 * @param propertyName
	 *            nombre de la propiedad
	 * @return
	 ***/
	@Transient
	public Boolean getBooleanPropertyValue(String propertyName) {

		Boolean retValue = false;

		for (NetworkProperty property : this.getProperties())
			if (property.getName().equals(propertyName))
				return property.getValue();

		return retValue;
	}

	// Métodos abreviado para obtener el valor de propiedades manteniendo las
	// interfaces anteriors
	@Transient
	public boolean mustRunValidation() {
		return getBooleanPropertyValue(Property.RUN_VALIDATION);
	}

	@Transient
	public boolean mustRunVufindIndexing() {
		return getBooleanPropertyValue(Property.RUN_VUFIND_INDEXING);
	}

	@Transient
	public boolean mustRunXOAIIndexing() {
		return getBooleanPropertyValue(Property.RUN_XOAI_INDEXING);
	}

	@Transient
	public boolean mustRunTransformation() {
		return getBooleanPropertyValue(Property.RUN_TRANSFORMATION);
	}

}
