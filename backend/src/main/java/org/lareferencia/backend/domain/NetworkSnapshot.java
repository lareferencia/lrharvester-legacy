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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.joda.time.DateTime;
import org.lareferencia.backend.util.JsonDateSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 */
@Entity
@JsonIgnoreProperties({ "network" })
@JsonAutoDetect
public class NetworkSnapshot extends AbstractEntity {

	@Getter
	@Setter
	@Column(nullable = false)
	private SnapshotStatus status;

	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private java.util.Date startTime;

	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date endTime;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer size;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer validSize;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer transformedSize;

	@Getter
	@Setter
	@Column
	private String resumptionToken;

	@Getter
	@Setter
	@ManyToOne()
	@JoinColumn(name = "network_id"/* , nullable=false */)
	private Network network;

	@Getter
	@Setter
	boolean deleted;

	public NetworkSnapshot() {
		super();
		this.status = SnapshotStatus.INITIALIZED;
		startTime = new DateTime().toDate();
		this.size = 0;
		this.validSize = 0;
		this.transformedSize = 0;
		this.deleted = false;
	}

	public void incrementSize() {
		size++;
	}

	public void incrementValidSize() {
		validSize++;
	}

	public void incrementTransformedSize() {
		transformedSize++;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public java.util.Date getStartTime() {
		return startTime;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public java.util.Date getEndTime() {
		return endTime;
	}

}
