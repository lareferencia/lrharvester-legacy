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
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * Property Entity
 */
@Entity
@Getter
@Setter
public class Property {

	public static final String RUN_VUFIND_INDEXING = "RUN_VUFIND_INDEXING";
	public static final String RUN_XOAI_INDEXING = "RUN_XOAI_INDEXING";
	public static final String RUN_VALIDATION = "RUN_VALIDATION";
	public static final String RUN_TRANSFORMATION = "RUN_TRANSFORMATION";

	@Id
	@Column(nullable = false, unique = true)
	private String id;

	@Transient
	public String getName() {
		return id;
	};

	@Column
	private String description;
}
