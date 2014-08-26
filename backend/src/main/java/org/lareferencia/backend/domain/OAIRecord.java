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
	
	private static final String 
	  DOMAIN_NAME_PATTERN_STR = "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z-]{2,})",
	  NAME_PATTERN_STR = "[A-Za-z0-9-]{4,}";

	private static final Pattern DomainNamePattern = Pattern.compile(DOMAIN_NAME_PATTERN_STR);
	private static final Pattern NamePattern = Pattern.compile(NAME_PATTERN_STR);

	
	@Column(nullable = false)
	private String identifier;
	
	@Setter
	@Column(nullable = true)
	private String repositoryDomain;
	
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datestamp;
	
	@Setter
	@Type(type="org.hibernate.type.StringClobType")
	private String publishedXML;
	
	@Setter
	@Column(nullable = false)
	private RecordStatus status;
	
	@Setter
	@Column(nullable = false)
	private boolean wasTransformed;
	
	/*@Column(nullable = false)
	private boolean belongsToCollection;
	
	@Lob
	@Column(nullable = false) 
	private String belongsToCollectionDetails;*/
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
	
	public OAIRecord(String identifier, String originalXMLString) {
		super();
		this.status = RecordStatus.UNTESTED;
		this.identifier = identifier;
		this.datestamp = new DateTime().toDate();
		//this.belongsToCollection = false;
		//this.belongsToCollectionDetails = "";
		this.publishedXML = originalXMLString;
		
		updateRepositoryDomain(identifier);

	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;	
		
		updateRepositoryDomain(identifier);
	}
	
	private void updateRepositoryDomain(String identifier) {
		
		this.repositoryDomain = "UNK";
		
		Matcher matcher = DomainNamePattern.matcher(this.identifier);
	
		if ( matcher.find() )
		
			this.repositoryDomain = matcher.group();
		
		else {
			
			matcher = NamePattern.matcher(this.identifier);
				
			while ( matcher.find() )
				if ( matcher.group().length() > this.repositoryDomain.length() )
					this.repositoryDomain = matcher.group();
		}
	}
	
	
	
}
