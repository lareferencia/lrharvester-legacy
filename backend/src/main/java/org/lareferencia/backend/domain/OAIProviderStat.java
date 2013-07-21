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
import javax.persistence.Id;

import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"id"})
public class OAIProviderStat implements Identifiable<String>  {
	
	@Id
	@Column(nullable = false)
	private String ipAddress;
	
	@Column(nullable = false)
	private Long requestCount = 0L;
	
	public void incrementRequestCounter() {
		requestCount++;
	}

	@Override
	public String getId() {
		return ipAddress;
	}

	public OAIProviderStat(String ipAddress) {
		super();
		this.ipAddress = ipAddress;
		this.requestCount = 0L;
	}
	
	public OAIProviderStat() {
		super();
		this.ipAddress = "unknown";
		this.requestCount = 0L;
	}
}
