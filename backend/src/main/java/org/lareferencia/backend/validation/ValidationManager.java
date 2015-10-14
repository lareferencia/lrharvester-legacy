package org.lareferencia.backend.validation;

import org.lareferencia.backend.domain.Transformer;
import org.lareferencia.backend.domain.TransformerRule;
import org.lareferencia.backend.domain.Validator;
import org.lareferencia.backend.domain.ValidatorRule;
import org.lareferencia.backend.validation.transformer.ITransformer;
import org.lareferencia.backend.validation.transformer.ITransformerRule;
import org.lareferencia.backend.validation.transformer.TransformerImpl;
import org.lareferencia.backend.validation.validator.IValidator;
import org.lareferencia.backend.validation.validator.IValidatorRule;
import org.lareferencia.backend.validation.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 
 * ValidatorManager toma como parámetro objetos del modelo, validator y tranformer
 * y usando RuleSerializer devuelve objetos validatores y transformadores para se usandos
 * en los procesos de validación y transformación del worker
 * 
 * @author lmatas
 *
 */
@Component
public class ValidationManager {
	
	
	@Autowired 
	private RuleSerializer serializer;
	
	
	/**
	 * Crea un validador a partir de un validador modelo
	 * 
	 * @param vmodel 
	 * @return
	 */
	public IValidator createValidatorFromModel(Validator vmodel) {
		
		IValidator validator = new ValidatorImpl(); 
		
		for (ValidatorRule vrule: vmodel.getRules()) {
			
			IValidatorRule rule = serializer.deserializeValidatorFromJsonString( vrule.getJSONSerialization() );
			validator.getRules().add(rule);
			
		}
		
		return validator;
	}
	
	/**
	 * Crea un transformador a partir de un validador modelo
	 * 
	 * @param vmodel 
	 * @return
	 */
	public ITransformer createTransformerFromModel(Transformer tmodel) {
		
		ITransformer transformer = new TransformerImpl();
		
		for (TransformerRule trule: tmodel.getRules()) {
			
			ITransformerRule rule = serializer.deserializeTransformerFromJsonString( trule.getJSONSerialization() );
			transformer.getRules().add(rule);
			
		}
		
		return transformer;
	}
	

}
