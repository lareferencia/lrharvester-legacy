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
public class LengthContentValidationRule extends BaseContentValidationRule {
	
	public static String RULE_ID="Length";
	
	public LengthContentValidationRule() {
		super();
	}

	private Integer minLength = 0;
	private Integer maxLength = Integer.MAX_VALUE;

	public LengthContentValidationRule(Integer min,  Integer max) {
		super();
		this.maxLength = max;
		this.minLength = min;
	}

	@Override
	public ContentValidationResult validate(String content) {
		
		ContentValidationResult result = new ContentValidationResult();
		//result.setRuleID(RULE_ID);		
		//result.setExpectedValue( minLength.toString() + " >= Length >= " + maxLength.toString()) ;
		
		result.setRuleName(this.name);
		
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
