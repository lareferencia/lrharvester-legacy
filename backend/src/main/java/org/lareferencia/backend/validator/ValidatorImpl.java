package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

@Getter
@Setter
public class ValidatorImpl implements IValidator {
	
	List<FieldValidator> fieldValidators;
	List<FieldValidator> belongsToCollectionFieldValidators;
	
	public ValidatorImpl() {
		super();
		fieldValidators = new ArrayList<FieldValidator>();
		belongsToCollectionFieldValidators = new ArrayList<FieldValidator>();
	}
	
	private ValidationResult validate(OAIRecordMetadata metadata, List<FieldValidator> validators) {
	
		ValidationResult result = new ValidationResult();
		boolean isRecordValid = true;
		
		for (FieldValidator validator:validators) {
			
			String fieldName = validator.getFieldName();
			
			FieldValidationResult fieldResult = validator.validate(metadata);
			result.getFieldResults().put( fieldName, fieldResult );
			
			isRecordValid &= ( fieldResult.isValid() || !validator.isMandatory() );
		}
		
		result.setValid(isRecordValid);
		
		return result;
	}
	
	@Override
	public ValidationResult testIfBelongsToCollection(OAIRecordMetadata metadata) {
		return validate(metadata, belongsToCollectionFieldValidators);
	}
	
	@Override
	public ValidationResult validate(OAIRecordMetadata metadata) {
		return validate(metadata, fieldValidators);
	}

}
