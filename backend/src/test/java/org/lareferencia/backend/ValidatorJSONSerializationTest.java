/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.Validator;
import org.lareferencia.backend.domain.ValidatorRule;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.repositories.ValidatorRepository;
import org.lareferencia.backend.repositories.ValidatorRuleRepository;
import org.lareferencia.backend.util.parameters.AbstractProperty;
import org.lareferencia.backend.util.parameters.PropertyA;
import org.lareferencia.backend.validator.AbstractValidatorRule;
import org.lareferencia.backend.validator.ContentLengthValidationRule;
import org.lareferencia.backend.validator.ControlledValueContentValidationRule;
import org.lareferencia.backend.validator.IValidatorRule;
import org.lareferencia.backend.validator.QuantifierValues;
import org.lareferencia.backend.validator.ValidationRuleSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
  



@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorJSONSerializationTest {
	
	
	@Autowired
	private ValidatorRuleRepository ruleRepository;
	
	@Autowired
	private ValidatorRepository validatorRepository;
	
	
	@Test
	public void testPropertySerialization() throws Exception {
		
		   String outputFile = "test.json";
		
		   ControlledValueContentValidationRule rule = new ControlledValueContentValidationRule();
		   
		   rule.setName("DescriptionNotZero");
		   rule.setDescription("Chequea que no se description vacia");
		   rule.setFieldname("dc:date");
		   rule.setQuantifier(QuantifierValues.ONE_OR_MORE);
	       
	       ArrayList<String> valores = new ArrayList<String>();
	       valores.add("valor1");
	       valores.add("valor2");
	       
	       rule.setControlledValues(valores);
	       
	       ObjectMapper mapper = new ObjectMapper();
	       mapper.registerSubtypes( ContentLengthValidationRule.class, ContentLengthValidationRule.class );

	       mapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(new File(outputFile)), rule);

	}
	
	@Test
	public void testPropertyDeserialization() throws Exception {
		
			String outputFile = "test.json";
		
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerSubtypes( ContentLengthValidationRule.class, ContentLengthValidationRule.class );
	        
	        IValidatorRule rule = mapper.readValue(FileUtils.readFileToByteArray(new File(outputFile)), AbstractValidatorRule.class);
	        System.out.println(rule);
	
	}
	
	@Test
	public void testValidatorRuleSerializationPersistence() throws Exception {
		
		
		   ControlledValueContentValidationRule rule = new ControlledValueContentValidationRule();
		   
		   rule.setName("DescriptionNotZero");
		   rule.setDescription("Chequea que no se description vacia");
		   rule.setFieldname("dc:date");
		   rule.setQuantifier(QuantifierValues.ONE_OR_MORE);
	       
	       ArrayList<String> valores = new ArrayList<String>();
	       valores.add("valor1");
	       valores.add("valor2");
	       
	       rule.setControlledValues(valores);
	       
	       ObjectMapper mapper = new ObjectMapper();
	       mapper.registerSubtypes( ContentLengthValidationRule.class, ContentLengthValidationRule.class );

	       String jsonString = mapper.writeValueAsString(rule);
	       
	       ValidatorRule vrule = new ValidatorRule();
	       vrule.setName(rule.getName());
	       vrule.setDescription(rule.getDescription());
	       vrule.setJSONSerialization(jsonString);
	       
	       Validator vvalidator = new Validator();
	       vvalidator.setName("Validator");
	       vvalidator.setDescription("El validador default");
	       vvalidator.getRules().add(vrule);
	       
	       validatorRepository.save(vvalidator);
	       validatorRepository.flush();
	       
	       System.out.println(jsonString);

	}
	
	
	@Test
	public void testValidatorRuleSerializaer() throws Exception {
		
		
		   ControlledValueContentValidationRule rule = new ControlledValueContentValidationRule();
		   ContentLengthValidationRule rule2 = new ContentLengthValidationRule();
		   
		   ValidationRuleSerializer serializer = new ValidationRuleSerializer();
		   List<IValidatorRule> rules = new ArrayList<IValidatorRule>();
		   rules.add(rule);
		   rules.add(rule2);
		   serializer.setPrototypes(rules);
		   
		 
		   rule.setName("DescriptionNotZero");
		   rule.setDescription("Chequea que no se description vacia");
		   rule.setFieldname("dc:date");
		   rule.setQuantifier(QuantifierValues.ONE_OR_MORE);
	       
	       ArrayList<String> valores = new ArrayList<String>();
	       valores.add("valor1");
	       valores.add("valor2");
	       
	       rule.setControlledValues(valores);
	       
	       String jsonString = serializer.serializeToJsonString(rule);
	       
	       System.out.println(jsonString);
	       
	       System.out.println(serializer.deserializeFromJsonString(jsonString));
	       
	}
	
	
	
	
	
	
}
