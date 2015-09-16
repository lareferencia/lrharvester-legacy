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

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString(exclude={"pattern"})
public class RegexContentValidationRule extends BaseContentValidatorRule {
	
	private static final int MAX_EXPECTED_LENGTH = 255;

	public static String RULE_ID = "ContentRegexValidationRule";
	public static String RULE_NAME = "ContentRegexValidationRule";


	@Getter
	@JsonProperty("name")
	private String regexString;
	
	private Pattern pattern;
	
	
	public void setRegexString(String reString) {
		this.regexString = reString;
		this.pattern = Pattern.compile(reString);
	}

	@Override
	public OccurrenceValidationResult validate(String content) {
	
		OccurrenceValidationResult result = new OccurrenceValidationResult();
		
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue(content.length() > MAX_EXPECTED_LENGTH ? content.substring(0, MAX_EXPECTED_LENGTH) : content);
			result.setValid( pattern.matcher(content).matches() );
		}
			
		return result;
	}

	
}
