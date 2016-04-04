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
import javax.persistence.ManyToOne;
import javax.persistence.Transient;


import lombok.Getter;
import lombok.Setter;

/**
 * NetworkProperty Entity
 */
@Entity
public class NetworkProperty extends AbstractEntity {
	
	@Getter
    @Setter
	@ManyToOne
    private Property property;
   
    @Getter
    @Setter
    @ManyToOne
    private Network network;
    
    @Getter
    @Setter
	@Column(nullable = false)
    private Boolean value;
	
    @Transient 
    public String getName() {	
    	if (property != null)
    		return property.getName();
    	else return "";
    };
    
    @Transient 
    public String getDescription() {
    	if (property != null)
    	    	return property.getDescription();
    	else return "";
    };
}
