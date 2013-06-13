package org.lareferencia.backend.transformer;

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ValidationResult;

public interface ITransformer {
	
	public List<FieldTransformer> getFieldTransformers();
	public void setFieldTransformers( List<FieldTransformer> validators);
	//TODO: Implementar Exceptions específicas
	public void transform(OAIRecordMetadata metadata, ValidationResult validationResult) throws Exception;

}
