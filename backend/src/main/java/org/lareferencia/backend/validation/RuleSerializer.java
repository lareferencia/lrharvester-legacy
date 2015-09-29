package org.lareferencia.backend.validation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.hibernate.bytecode.instrumentation.spi.AbstractFieldInterceptor;
import org.lareferencia.backend.validation.transformer.AbstractTransformerRule;
import org.lareferencia.backend.validation.transformer.ITransformerRule;
import org.lareferencia.backend.validation.validator.AbstractValidatorFieldContentRule;
import org.lareferencia.backend.validation.validator.AbstractValidatorRule;
import org.lareferencia.backend.validation.validator.IValidatorFieldContentRule;
import org.lareferencia.backend.validation.validator.IValidatorRule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;


public class RuleSerializer {

	@Getter
	private List<IValidatorRule> validatorPrototypes;
	
	@Getter
	private List<ITransformerRule> transformerPrototypes;
	
	// JsonObject Mapper
	private ObjectMapper mapper;
	
	public RuleSerializer() {
		validatorPrototypes = new ArrayList<IValidatorRule>();
		transformerPrototypes = new ArrayList<ITransformerRule>();

	}
	
	@SuppressWarnings("unchecked")
	/***
	 * Esta metodo carga en el mapper creado los subtipos de la clase AbstractValidatorRule 
	 * desde los prototipos provisto por la lista, esto permite serializar y reconocer 
	 * los objetos desde/hacia JSON
	 */
	private void updateObjectMapper() {
		mapper = new ObjectMapper();
	    
		// Set con las clases de los prototipos declarados
		Set<Class<? extends AbstractValidatorRule>> aValidationRuleSubTypes = new HashSet<Class<? extends AbstractValidatorRule>>(); 	    
				
		for (IValidatorRule rule : validatorPrototypes) {
			// TODO: Ojo que esto puede ser problematico si algun de las reglas no es derivada de AbstractValidationRule
			aValidationRuleSubTypes.add((Class<? extends AbstractValidatorRule>) rule.getClass());
		}
		mapper.registerSubtypes(aValidationRuleSubTypes.toArray(new Class<?>[aValidationRuleSubTypes.size()]));	      
	
		
		// Set con las clases de los prototipos declarados
		Set<Class<? extends AbstractTransformerRule>> aTransformerRuleSubTypes = new HashSet<Class<? extends AbstractTransformerRule>>(); 	    
				
		for (ITransformerRule rule : transformerPrototypes) {
			// TODO: Ojo que esto puede ser problematico si algun de las reglas no es derivada de AbstractRule
			aTransformerRuleSubTypes.add((Class<? extends AbstractTransformerRule>) rule.getClass());
		}
		mapper.registerSubtypes(aTransformerRuleSubTypes.toArray(new Class<?>[aTransformerRuleSubTypes.size()]));	   
		
		
		
	}

	
	public void setValidatorPrototypes(List<IValidatorRule> prototypes) {
		this.validatorPrototypes = prototypes;
		
		// Cada vez que la lista de prototipos cambia hay que reconstruir el mapper
		updateObjectMapper();
	}
	
	public void setTransformerPrototypes(List<ITransformerRule> prototypes) {
		this.transformerPrototypes = prototypes;
		
		// Cada vez que la lista de prototipos cambia hay que reconstruir el mapper
		updateObjectMapper();
	}
	
	public String serializeTransformerToJsonString(ITransformerRule rule) {
		
		 try {
			return mapper.writeValueAsString(rule);
		} catch (JsonProcessingException e) {
			// TODO Serialize rule exceptions
			e.printStackTrace();
		}
		return null;
	}
	
	public String serializeValidatorToJsonString(IValidatorRule rule) {
		
		 try {
			return mapper.writeValueAsString(rule);
		} catch (JsonProcessingException e) {
			// TODO Serialize rule exceptions
			e.printStackTrace();
		}
		return null;
	}
	
	public IValidatorRule deserializeValidatorFromJsonString(String jsonString) {
		
		
			try {
				return mapper.readValue(jsonString, AbstractValidatorRule.class);
			} catch (JsonParseException e) {
				
				e.printStackTrace();
			} catch (JsonMappingException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	
		return null;
	}
	
	public ITransformerRule deserializeTransformerFromJsonString(String jsonString) {
		
		
		try {
			return mapper.readValue(jsonString, AbstractTransformerRule.class);
		} catch (JsonParseException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	return null;
}
	
	
	

}
