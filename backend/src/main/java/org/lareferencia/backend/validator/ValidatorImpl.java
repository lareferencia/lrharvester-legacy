package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.HarvesterRecord;

@Getter
@Setter
public class ValidatorImpl implements IValidator {
	
	List<FieldValidator> fieldValidators;
	
	public ValidatorImpl() {
		super();
		fieldValidators = new ArrayList<FieldValidator>();
	}
	
	@Override
	public ValidationResult validate(HarvesterRecord record) {
	
		ValidationResult result = new ValidationResult();
		boolean isRecordValid = true;
		
		for (FieldValidator validator:fieldValidators) {
			
			String fieldName = validator.getFieldName();
			
			FieldValidationResult fieldResult = validator.validate(record);
			result.getFieldResults().put( fieldName, fieldResult );
			
			isRecordValid &= ( fieldResult.isValid() || !validator.isMandatory() );
		}
		
		result.setValid(isRecordValid);
		
		return result;
	}

}
