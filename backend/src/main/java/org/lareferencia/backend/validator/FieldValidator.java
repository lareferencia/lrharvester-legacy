package org.lareferencia.backend.validator;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.HarvesterRecord;

@Getter
@Setter
public class FieldValidator implements IValidator{
	
	private List<IContentValidationRule> contentRules;
	private String fieldName;
	private Integer minOccurences = 0;
	private Integer maxOccurences = Integer.MAX_VALUE;
	
	public FieldValidator(String fieldName, List<IContentValidationRule> contentRules) {
		super();
		this.contentRules = contentRules;
		this.fieldName = fieldName;
	}

	public FieldValidator() {
		super();
	}

	@Override
	public ValidationResult validate(HarvesterRecord record) {
		
		// Se obtienen todas las ocurrencias de ese campo en el registro
		List<String> occurrences = record.getFieldOcurrences(fieldName);
		
		boolean isFieldValid = true;
	
		// Se evaluan las reglas para esa ocurrencia
		for (IContentValidationRule rule:contentRules) {
			
			int validOccurrencesCount = 0;
			
			for (String content:  occurrences) {	
				
				boolean validOccurence = rule.validate(content);
				validOccurrencesCount += validOccurence ? 1:0;
				
				//TODO: Resultado de la regla;
			}
			
			boolean isRuleValid = validOccurrencesCount >= rule.getMinValidOccurrences() &&
					validOccurrencesCount <= rule.getMaxValidOccurrences();
		
			isFieldValid &= (isRuleValid || !rule.isMandatory());
			
		}
		
		return null;
	}
	
	
	

}
