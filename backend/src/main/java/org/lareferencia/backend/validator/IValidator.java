package org.lareferencia.backend.validator;

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;


public interface IValidator {
	
	public List<FieldValidator> getFieldValidators();
	public void setFieldValidators( List<FieldValidator> validators);
	public ValidationResult validate(OAIRecordMetadata metadata);

}
