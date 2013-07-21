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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

@Getter
@Setter
public class FieldValidator {
	

	private String fieldName;
	private boolean mandatory;
	private List<IContentValidationRule> rules;

	public FieldValidator(String fieldName, boolean isMandatory) {
		super();
		this.rules = new ArrayList<IContentValidationRule>();
		this.fieldName = fieldName;
		this.mandatory = isMandatory;
	}

	public FieldValidator() {
		super();
		this.rules = new ArrayList<IContentValidationRule>();
	}

	public FieldValidationResult validate(OAIRecordMetadata metadata) {
		
		FieldValidationResult result = new FieldValidationResult();
		result.setFieldName(fieldName);
		result.setMandatory(mandatory ); 
		
		List<String> occurrences = metadata.getFieldOcurrences(fieldName);
		
		/** Inicializa un diccionarios para registrar las ocurrencias válidas para alguna regla
		 *  y los resultados para ocurrencias inválidas.
		 */
		Map<Integer, Boolean> validByOccurrenceIndex = new HashMap<Integer, Boolean>(occurrences.size());
		Map<Integer, List<ContentValidationResult>> resultByInvalidOccurrenceIndex = new HashMap<Integer, List<ContentValidationResult>>(occurrences.size());
		for (int i=0; i<occurrences.size(); i++) {
			validByOccurrenceIndex.put(i, false);
			resultByInvalidOccurrenceIndex.put(i, new ArrayList<ContentValidationResult>());
		}
			
		boolean isFieldValid = true;
	
		for (IContentValidationRule rule:rules) {
			
			int validOccurrencesCount = 0;
			
			for (int i=0; i<occurrences.size(); i++) {	
				
				ContentValidationResult ruleResult = rule.validate( occurrences.get(i) );
				boolean validOccurence = ruleResult.isValid();
				validOccurrencesCount += validOccurence ? 1:0;
				
				/** actualiza el diccionario de validez de cada ocurrencia si es valida
				 * y registra el resultado de la regla sólo en caso de evaluar válida
				 */
				if (validOccurence) {
					validByOccurrenceIndex.put(i, true);
					result.getResults().add(ruleResult);
				} else {
					resultByInvalidOccurrenceIndex.get(i).add(ruleResult);
				}
			}
			
			boolean isRuleValid; 
			
			if  ( rule.getQuantifier().equals(IContentValidationRule.QUANTIFIER_ONE_ONLY) ) 
				isRuleValid = validOccurrencesCount == 1;
			else if ( rule.getQuantifier().equals(IContentValidationRule.QUANTIFIER_ONE_OR_MORE) ) 
				isRuleValid = validOccurrencesCount >= 1;			
			else if ( rule.getQuantifier().equals(IContentValidationRule.QUANTIFIER_ZERO_OR_MORE) ) 
				isRuleValid = validOccurrencesCount >= 0;
			else 
				isRuleValid = false;
				
			isFieldValid &= isRuleValid;	
		}
		
		/** En caso de campo inválido 
		 * solo se registran resultados de reglas inválidas para aquellas ocurrencias que no hayan resultado 
		 * válidas para ninguna regla (evita que ocurrencias válidas aparezcan como errores en otra regla) **/
		if ( !isFieldValid )
			for (int i=0; i<occurrences.size(); i++) {	
				if (!validByOccurrenceIndex.get(i)) {
					result.getResults().addAll( resultByInvalidOccurrenceIndex.get(i) );
				}
			}
			
		result.setValid(isFieldValid);
		
		return result;
	}
	
}
