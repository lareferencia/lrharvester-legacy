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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentLengthValidationRule extends BaseContentValidatorRule {
	
	public static String RULE_ID="ContentLengthValidationRule";
	public static String RULE_NAME="ContentLengthValidationRule";

	protected static final String PARAM_MAX_LENGTH = "maxLength";
	protected static final String PARAM_MIN_LENGTH = "minLength";
	
	private Integer minLength = 0;
	private Integer maxLength = Integer.MAX_VALUE;

	public ContentLengthValidationRule(boolean mandatory, String quantifier) {
		super(RULE_ID, RULE_NAME, mandatory, quantifier);
		
		this.parameters.put(PARAM_MAX_LENGTH, UNDEFINED_PARAM_VALUE);
		this.parameters.put(PARAM_MIN_LENGTH, UNDEFINED_PARAM_VALUE);
	}
	
	@Override
	protected void updateParameters() {
		
		super.updateParameters();
		
		try {
		
			if ( getParameterValue(PARAM_MAX_LENGTH) != UNDEFINED_PARAM_VALUE )
				this.maxLength = Integer.decode( getParameterValue(PARAM_MAX_LENGTH) );
			
			if ( getParameterValue(PARAM_MIN_LENGTH) != UNDEFINED_PARAM_VALUE )
				this.minLength = Integer.decode( getParameterValue(PARAM_MIN_LENGTH) );
	
		} catch (NumberFormatException e) {
			System.err.println( "Parámetros inválidos en definición de regla:" + this.ruleID );
			System.err.println( this.parameters );
		}
	
	};


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
	
}
