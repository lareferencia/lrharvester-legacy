package org.lareferencia.backend.validator;

import java.util.List;

import org.lareferencia.backend.harvester.HarvesterRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public interface IValidator {
	
	public ValidationResult validate(HarvesterRecord record);
	public List<FieldValidator> getFieldValidators();
	public void setFieldValidators( List<FieldValidator> validators);

}
