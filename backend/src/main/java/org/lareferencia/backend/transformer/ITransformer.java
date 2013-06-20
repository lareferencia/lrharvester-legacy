package org.lareferencia.backend.transformer;

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ValidationResult;

public interface ITransformer {
	
	public List<FieldTransformer> getFieldTransformers();
	public void setFieldTransformers( List<FieldTransformer> validators);
	
	/**
	 * 
	 * @param  metadata 
	 * @param  validationResult
	 * @return Retorna true si hizo alguna transformación
	 * @throws Exception
	 */
	public boolean transform(OAIRecordMetadata metadata, ValidationResult validationResult) throws Exception;
	//TODO: Implementar Exceptions específicas
	
}
