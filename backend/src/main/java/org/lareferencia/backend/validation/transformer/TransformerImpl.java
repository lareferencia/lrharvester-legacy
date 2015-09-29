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
package org.lareferencia.backend.validation.transformer;

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.validator.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class TransformerImpl implements ITransformer {
	
	List<AbstractTransformerRule> rules;


	@Override
	public List<AbstractTransformerRule> getFieldTransformers() {
		return rules;
	}


	@Override
	public void setFieldTransformers(List<AbstractTransformerRule> transformers) {
		this.rules = transformers;	
	}


	@Override
	public boolean transform(OAIRecordMetadata metadata, ValidationResult validationResult) throws Exception {
		
		boolean anyTransformationOccurred = false; 
		
		for (AbstractTransformerRule transformer: rules) {
			
			try {
				// Solo aplica la transformación si ese campo no resultó válido o si se especifica expresamente
					anyTransformationOccurred |= transformer.transform(metadata);
			}
			catch (Exception e) {
				throw new Exception("Ocurrio un problema durante la transformacion de " + metadata.getIdentifier(), e);
			}
		}	
		
		return anyTransformationOccurred;
	}
		
		
		
}	


