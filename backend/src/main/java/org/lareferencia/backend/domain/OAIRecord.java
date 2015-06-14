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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 */
@Entity
@Getter 
@JsonIgnoreProperties({"publishedXML","snapshot","datestamp"})
public class OAIRecord extends AbstractEntity {
	
	
	@Column(nullable = false)
	private String identifier;
	
	@Column(nullable = false)
	private String fingerprint;
	
	@Setter
	@Column(nullable = true)
	private String repositoryDomain;
	
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datestamp;
	
	@Type(type="org.hibernate.type.StringClobType")
	private String publishedXML;
	
	@Setter
	@Column(nullable = false)
	private RecordStatus status;
	
	@Setter
	@Column(nullable = false)
	private boolean wasTransformed;
	
	@Setter
	@ManyToOne(fetch=FetchType.EAGER,optional=false)	
	@JoinColumn(name="snapshot_id")
	private NetworkSnapshot snapshot;
	
	public OAIRecord() {
		super();
		this.status = RecordStatus.UNTESTED;
		//this.belongsToCollection = false;
		//this.belongsToCollectionDetails = "";
		this.datestamp = new DateTime().toDate();
	}
	
	/*
	public OAIRecord(String identifier) {
		super();
		this.status = RecordStatus.UNTESTED;
		this.datestamp = new DateTime().toDate();
		//this.belongsToCollection = false;
		//this.belongsToCollectionDetails = "";
		this.setIdentifier(identifier);
		
	}*/

	public void setIdentifier(String identifier) {
		this.identifier = identifier;		
		
		if ( this.snapshot != null )
			this.fingerprint = this.snapshot.getNetwork().getAcronym() + "_" +  DigestUtils.md5Hex( identifier );
		else
			this.fingerprint = "00" + "_" +  DigestUtils.md5Hex( identifier );
	}
	

	public void setPublishedXML(String publishedXML) {
		this.publishedXML = publishedXML;
		
		/**if ( this.snapshot != null )
			this.fingerprint = this.snapshot.getNetwork().getAcronym() + "_" +  DigestUtils.md5Hex( publishedXML );
		else
			this.fingerprint = "NULL";**/
	}
	
	
	
}
