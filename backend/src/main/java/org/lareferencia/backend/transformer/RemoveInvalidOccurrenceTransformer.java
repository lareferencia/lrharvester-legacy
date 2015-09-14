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

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.OccurrenceValidationResult;
import org.w3c.dom.Node;

public class RemoveInvalidOccurrenceTransformer extends FieldTransformer {
	
	public RemoveInvalidOccurrenceTransformer() {
		applyIfValid = true;
	}
	
	@Override
	public boolean transform(OAIRecordMetadata metadata) {
		
		
		OccurrenceValidationResult result;
		boolean anyInvalid = false;
		
		// Ciclo de búsqueda
		for (Node node: metadata.getFieldNodes(fieldName) ) {
			
			String occr = node.getFirstChild().getNodeValue();			
			result = validationRule.validate(occr);
			
			anyInvalid |= !result.isValid();
			
			if ( !result.isValid() ) {
				Node fieldNode = node.getParentNode();
				fieldNode.removeChild(node);				
			}
			
		}
			
		return anyInvalid;
	}
	

}
