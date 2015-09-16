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
package org.lareferencia.backend.transformer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.OccurrenceValidationResult;
import org.lareferencia.backend.validator.IValidatorRule;
import org.lareferencia.backend.validator.AbstractValidatorRule;
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
				// Solo aplica la transformación si ese campo no resultó válido o si se especifica expresamente
				if ( transformer.isApplyIfValid() || !validationResult.getFieldResults().get( transformer.getFieldName() ).isValid() )
					anyTransformationOccurred |= transformer.transform(metadata);
			}
			catch (Exception e) {
				throw new Exception("Ocurrio un problema durante la transformacion de " + metadata.getIdentifier(), e);
			}
		}	
		
		return anyTransformationOccurred;
	}
		
		
		
}	


