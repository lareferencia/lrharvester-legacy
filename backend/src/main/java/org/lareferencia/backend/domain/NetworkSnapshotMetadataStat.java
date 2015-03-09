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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Type;
import org.lareferencia.backend.util.JsonMetadataStatSerializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"id","snapshot"})
@JsonSerialize(using=JsonMetadataStatSerializer.class)
public class NetworkSnapshotMetadataStat extends AbstractEntity  {
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)	
	@JoinColumn(name="snapshot_id")
	private NetworkSnapshot snapshot;
	
	@Column(nullable = false)
	private String statID;
	
	@Type(type="org.hibernate.type.StringClobType")
	private String JSONString;
		
	public NetworkSnapshotMetadataStat() {
		super();
	}
	
	public NetworkSnapshotMetadataStat(String statID, String JSONString) {
		super();	
		this.statID = statID;
		this.JSONString = JSONString;
	}
}
