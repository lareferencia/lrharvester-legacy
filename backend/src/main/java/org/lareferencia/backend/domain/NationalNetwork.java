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
		
	private String scheduleCronExpression;	
	
	@OneToMany(cascade=CascadeType.ALL/*, orphanRemoval=true*/)
	@JoinColumn(name="network_id")
	@LazyCollection(LazyCollectionOption.FALSE)  // Si es LAZY genera problemas durante el procesamiento
	private Collection<OAIOrigin> origins = new LinkedHashSet<OAIOrigin>();
	
	@OneToMany(cascade=CascadeType.ALL/*,orphanRemoval=true*/)
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
