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

/**
 * 
 */
@Entity
@Getter 
@Setter
public class OAIRecordValidationResult extends AbstractEntity {
	
	@Column(nullable = false)
	private String field;
		
	@ManyToOne(fetch=FetchType.EAGER,optional=false)	
	@JoinColumn(name="snapshot_id")
	private NetworkSnapshot snapshot;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)	
	@JoinColumn(name="record_id")
	private OAIRecord record;
	
	public OAIRecordValidationResult() {
		super();
	}
	
	public OAIRecordValidationResult(String field) {
		this.field = field;
	}
	
}
