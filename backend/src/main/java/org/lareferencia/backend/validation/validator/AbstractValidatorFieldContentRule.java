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

import org.lareferencia.backend.harvester.OAIRecordMetadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;


/**
 * La clase abstracta BaseContentVRule implementa los mecanismos comunes para la evaluación de contenidos de distintas
 * ocurrencias de un mismo metadato.
 * @author lmatas
 *
 */

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
public abstract class AbstractValidatorFieldContentRule extends AbstractValidatorRule implements IValidatorFieldContentRule {
	
	@JsonProperty("fieldname")
	private String fieldname;


	public AbstractValidatorFieldContentRule() {
	}

	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param metadata
	 * @return
	 */
	public ValidatorRuleResult validate(OAIRecordMetadata metadata) {
		
		ValidatorRuleResult result = new ValidatorRuleResult();
		
		List<FieldContentValidatorResult> results = new ArrayList<FieldContentValidatorResult>();
		int validOccurrencesCount = 0;
			
		List<String> occurrences = metadata.getFieldOcurrences( fieldname  );

		for (int i=0; i<occurrences.size(); i++) {	
			
			// Se valida cada ocurrencia y se obtiene el resultado
			FieldContentValidatorResult occurrenceResult = this.validate( occurrences.get(i) );
			
			// Se agrega a la lista de ocurrencias
			results.add(occurrenceResult);
			
			// Se suman las ocurrencias válidas
			validOccurrencesCount += occurrenceResult.isValid() ? 1:0;	
		 }
		
					
		boolean isRuleValid; 
		
		switch (quantifier) {
		
		case ONE_ONLY :
			isRuleValid = validOccurrencesCount == 1;
			break;
			
		case ONE_OR_MORE:
			isRuleValid = validOccurrencesCount >= 1;
			break;
			
		case ZERO_OR_MORE:
			isRuleValid = validOccurrencesCount >= 0;
			break;
			
		case ZERO_ONLY:
			isRuleValid = validOccurrencesCount == 0;
			break;
			
		case ALL:
			isRuleValid = validOccurrencesCount == occurrences.size();
			break;
		
		default:
			isRuleValid = false;
			break;
		}
						
		result.setRule(this);
		result.setResults(results);
		result.setValid(isRuleValid);
		return result;
		
	
	}

	

	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param String
	 * @return
	 */
	public abstract FieldContentValidatorResult validate(String string);	
}
