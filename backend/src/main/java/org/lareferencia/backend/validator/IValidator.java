package org.lareferencia.backend.validator;

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;


public interface IValidator {
	
	
	public ValidationResult validate(OAIRecordMetadata metadata);
	public ValidationResult testIfBelongsToCollection(OAIRecordMetadata metadata);
	
	// validadores para la determinación de la validez de registro
	public List<FieldValidator> getFieldValidators();
	public void setFieldValidators( List<FieldValidator> validators);
	
	// validadores para la determinación del inclusión en la colección
	public void setBelongsToCollectionFieldValidators(List<FieldValidator> validators);
	public List<FieldValidator> getBelongsToCollectionFieldValidators();
	

}
