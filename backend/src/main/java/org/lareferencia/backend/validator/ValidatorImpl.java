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
package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

@Getter
@Setter
public class ValidatorImpl implements IValidator {
	
	List<FieldValidator> fieldValidators;
	//List<FieldValidator> belongsToCollectionFieldValidators;
	
	public ValidatorImpl() {
		super();
		fieldValidators = new ArrayList<FieldValidator>();
		//belongsToCollectionFieldValidators = new ArrayList<FieldValidator>();
	}
	
	private ValidationResult validate(OAIRecordMetadata metadata, List<FieldValidator> validators) {
	
		ValidationResult result = new ValidationResult();
		boolean isRecordValid = true;
		
		for (FieldValidator validator:validators) {
			
			String fieldName = validator.getFieldName();
			
			FieldValidationResult fieldResult = validator.validate(metadata);
			result.getFieldResults().put( fieldName, fieldResult );
			
			isRecordValid &= ( fieldResult.isValid() || !validator.isMandatory() );
		}
		
		result.setValid(isRecordValid);
		
		return result;
	}
	
	/*
	@Override
	public ValidationResult testIfBelongsToCollection(OAIRecordMetadata metadata) {
		return validate(metadata, belongsToCollectionFieldValidators);
	}*/
	
	@Override
	public ValidationResult validate(OAIRecordMetadata metadata) {
		return validate(metadata, fieldValidators);
	}

}
