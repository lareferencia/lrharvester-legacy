package org.lareferencia.backend.transformer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.IContentValidationRule;
import org.lareferencia.backend.validator.FieldValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
public class TransformerImpl implements ITransformer {
	
	List<FieldTransformer> fieldTransformers;


	@Override
	public List<FieldTransformer> getFieldTransformers() {
		return fieldTransformers;
	}


	@Override
	public void setFieldTransformers(List<FieldTransformer> transformers) {
		this.fieldTransformers = transformers;	
	}


	@Override
	public boolean transform(OAIRecordMetadata metadata, ValidationResult validationResult) throws Exception {
		
		boolean anyTransformationOccurred = false; 
		
		for (FieldTransformer transformer: fieldTransformers) {
			
			try {
				// Solo aplica la transformación si ese campo no resultó válido
				if ( !validationResult.getFieldResults().get( transformer.getFieldName() ).isValid() )
					anyTransformationOccurred |= transformer.transform(metadata);
			}
			catch (Exception e) {
				throw new Exception("Ocurrio un problema durante la transformacion de " + metadata.getIdentifier(), e);
			}
		}	
		
		return anyTransformationOccurred;
	}
		
		
		
}	


