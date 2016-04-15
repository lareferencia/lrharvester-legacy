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
package org.lareferencia.backend.validation.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
public class ControlledValueFieldContentValidatorRule extends AbstractValidatorFieldContentRule {
	
	private static final int MAX_EXPECTED_LENGTH = 255;

	protected List<String> controlledValues;
	
	public ControlledValueFieldContentValidatorRule() {
		super();
		this.controlledValues = new ArrayList<String>();
	}
	
	
	@Override
	public FieldContentValidatorResult validate(String content) {
		
		FieldContentValidatorResult result = new FieldContentValidatorResult();
				
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue( content.length() > MAX_EXPECTED_LENGTH ? content.substring(0, MAX_EXPECTED_LENGTH) : content);
			result.setValid( this.controlledValues.contains(content) );
		}
			
		return result;	
	}
	

	@Override
	public String toString() {
		return "ControlledValueContentValidationRule [controlledValues="
				+ controlledValues + ", id=" + ruleId + ", mandatory=" + mandatory + ", quantifier="
				+ quantifier + "]";
	}
	
}
