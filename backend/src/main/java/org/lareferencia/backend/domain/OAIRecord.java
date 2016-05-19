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

import java.util.Date;
import java.util.regex.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.RepositoryNameHelper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 */
@Getter
@Entity
@JsonIgnoreProperties({ "publishedXML", "snapshot", "datestamp", "metadata" })
public class OAIRecord extends AbstractEntity {

	@Transient
	private OAIRecordMetadata metadata;

	@Column(nullable = false)
	private String identifier;

	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datestamp;

	@Type(type = "org.hibernate.type.StringClobType")
	private String publishedXML;

	@Setter
	@Column(nullable = false)
	private RecordStatus status;

	@Setter
	@Column(nullable = false)
	private boolean wasTransformed;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "snapshot_id")
	private NetworkSnapshot snapshot;

	public OAIRecord() {
		super();
		this.status = RecordStatus.UNTESTED;
		// this.belongsToCollection = false;
		// this.belongsToCollectionDetails = "";
		this.datestamp = new DateTime().toDate();
	}

	public OAIRecord(NetworkSnapshot snapshot, OAIRecordMetadata metadata) {
		super();
		this.snapshot = snapshot;
		this.status = RecordStatus.UNTESTED;
		this.datestamp = new DateTime().toDate();
		this.metadata = metadata;
		updateIdentifier();
	}

	private void updateIdentifier() {
		this.identifier = metadata.getIdentifier();
	}

	public void setSnapshot(NetworkSnapshot snapshot) {
		this.snapshot = snapshot;
		updateIdentifier();
	}

	public String getFingerprint() {

		if (this.snapshot != null)
			return this.snapshot.getNetwork().getAcronym() + "_"
					+ DigestUtils.md5Hex(identifier);
		else
			return "00" + "_" + DigestUtils.md5Hex(identifier);
	}

	@PrePersist
	private void updatePublishedXML() {

		/* El XML publicado será la versión de la metadata si no es null */
		if (metadata != null)
			publishedXML = metadata.toString();
	}
}
