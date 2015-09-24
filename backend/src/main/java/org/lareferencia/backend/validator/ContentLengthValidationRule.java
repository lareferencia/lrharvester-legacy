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
package org.lareferencia.backend.validator;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ContentLengthValidationRule extends AbstractValidatorFieldContentRule {
	
	public static String RULE_ID="ContentLengthValidationRule";
	public static String RULE_NAME="ContentLengthValidationRule";
	
	@JsonProperty("minLength")
	private Integer minLength = 0;
	
	@JsonProperty("maxLength")
	private Integer maxLength = Integer.MAX_VALUE;

	public ContentLengthValidationRule() {
	}


	@Override
	public OccurrenceValidationResult validate(String content) {
		
		OccurrenceValidationResult result = new OccurrenceValidationResult();
		
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue( new Integer(content.length()).toString() );
			result.setValid( content.length() >= minLength && content.length() <= maxLength );
		}
			
		return result;
	}


	@Override
	public String toString() {
		return "ContentLengthValidationRule [minLength=" + minLength
				+ ", maxLength=" + maxLength + ", name=" + name
				+ ", description=" + description + ", mandatory=" + mandatory
				+ ", quantifier=" + quantifier + "]";
	}
	
}
