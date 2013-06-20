package org.lareferencia.backend.transformer;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.FieldValidationResult;
import org.lareferencia.backend.validator.IContentValidationRule;

@Getter
@Setter
public abstract class FieldTransformer {
	
	protected String fieldName;
	protected Map<String,String> options;
	protected IContentValidationRule validationRule;
	protected String defaultFieldValue;
		
	public FieldTransformer() {
		options = new HashMap<String, String>();
	}

	/**
	 * 
	 * @param metadata
	 * @return Retorna true si fue necesario aplicar una transformación
	 */
	abstract boolean transform(OAIRecordMetadata metadata);
}
