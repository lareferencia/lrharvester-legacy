package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;

@Getter
@Setter
public class FieldValidator {
	

	private String fieldName;
	private boolean mandatory;
	private List<IContentValidationRule> contentRules;

	public FieldValidator(String fieldName, boolean isMandatory) {
		super();
		this.contentRules = new ArrayList<IContentValidationRule>();
		this.fieldName = fieldName;
		this.mandatory = isMandatory;
	}

	public FieldValidator() {
		super();
		this.contentRules = new ArrayList<IContentValidationRule>();
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
	
		for (IContentValidationRule rule:contentRules) {
			
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
					result.getContentResults().add(ruleResult);
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
					result.getContentResults().addAll( resultByInvalidOccurrenceIndex.get(i) );
				}
			}
			
		result.setValid(isFieldValid);
		
		return result;
	}
	
}
