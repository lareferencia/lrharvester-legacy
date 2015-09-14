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
import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;


/**
 * La clase abstracta BaseContentVRule implementa los mecanismos comunes para la evaluación de contenidos de distintas
 * ocurrencias de un mismo metadato.
 * @author lmatas
 *
 */
public abstract class BaseContentValidatorRule extends BaseValidatorRule {
	

	protected static final String PARAM_FIEDLNAME = "fieldname";
	protected static final String UNDEFINED_PARAM_VALUE = "NoDefinido";
	private String fieldname;


	public BaseContentValidatorRule(String ruleID, String name, Boolean mandatory, String quantifier) {
		super(ruleID, name, mandatory, quantifier);
		this.parameters.put(PARAM_FIEDLNAME, UNDEFINED_PARAM_VALUE);
	}

	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param metadata
	 * @return
	 */
	public ValidationRuleResult validate(OAIRecordMetadata metadata) {
		
		
		// Primero se llama a la función update parameters, 
		// esta función debe cargar todos los parámetros dentro de las variables para permitir
		// el procesamiento correcto de las clases derivadas. 
		updateParameters();
		
		ValidationRuleResult result = new ValidationRuleResult();
		
		List<OccurrenceValidationResult> results = new ArrayList<OccurrenceValidationResult>();
		int validOccurrencesCount = 0;
		
		result.setMandatory(mandatory); 
		result.setQuantifier(quantifier);
		result.setRuleID( this.getRuleID() );
		
		List<String> occurrences = metadata.getFieldOcurrences( fieldname  );

		
		for (int i=0; i<occurrences.size(); i++) {	
			
			// Se valida cada ocurrencia y se obtiene el resultado
			OccurrenceValidationResult occurrenceResult = this.validate( occurrences.get(i) );
			
			// Se agrega a la lista de ocurrencias
			results.add(occurrenceResult);
			
			// Se suman las ocurrencias válidas
			validOccurrencesCount += occurrenceResult.isValid() ? 1:0;	
		 }
			
		boolean isRuleValid; 
		
		if  ( quantifier.equals(IValidatorRule.QUANTIFIER_ONE_ONLY) ) 
			isRuleValid = validOccurrencesCount == 1;
		else if ( quantifier.equals(IValidatorRule.QUANTIFIER_ONE_OR_MORE) ) 
			isRuleValid = validOccurrencesCount >= 1;			
		else if ( quantifier.equals(IValidatorRule.QUANTIFIER_ZERO_OR_MORE) ) 
			isRuleValid = validOccurrencesCount >= 0;
		else if ( quantifier.equals(IValidatorRule.QUANTIFIER_ZERO) ) 
			isRuleValid = validOccurrencesCount == 0;
		else if ( quantifier.equals(IValidatorRule.QUANTIFIER_ALL) ) 
			isRuleValid = validOccurrencesCount == occurrences.size();
		else
			isRuleValid = false;
				
		result.setResults(results);
		result.setValid(isRuleValid);
		return result;
	}

	
	protected void updateParameters() {
		this.fieldname = getParameterValue(PARAM_FIEDLNAME);
	}

	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param String
	 * @return
	 */
	protected abstract OccurrenceValidationResult validate(String string);	
}
