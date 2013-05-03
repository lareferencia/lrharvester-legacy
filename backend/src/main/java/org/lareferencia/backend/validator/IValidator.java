package org.lareferencia.backend.validator;

import java.util.List;

import org.lareferencia.backend.domain.OAIRecord;


public interface IValidator {
	
	public ValidationResult validate(OAIRecord record);
	public List<FieldValidator> getFieldValidators();
	public void setFieldValidators( List<FieldValidator> validators);

}
