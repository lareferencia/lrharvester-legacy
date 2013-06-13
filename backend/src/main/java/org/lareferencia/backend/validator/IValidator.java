package org.lareferencia.backend.validator;

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;


public interface IValidator {
	
	public List<FieldValidator> getFieldValidators();
	public void setFieldValidators( List<FieldValidator> transformers);
	public ValidationResult validate(OAIRecordMetadata metadata);

}
