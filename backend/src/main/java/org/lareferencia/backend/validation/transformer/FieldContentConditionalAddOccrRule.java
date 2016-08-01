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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.FieldExpressionEvaluator;
import org.lareferencia.backend.validation.validator.QuantifierValues;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldContentConditionalAddOccrRule extends AbstractTransformerRule {

	FieldExpressionEvaluator evaluator;
	
	@Setter
	@Getter
	@JsonProperty("fieldName")
	String fieldName;
	
	@Setter
	@Getter
	@JsonProperty("valueToAdd")
	String valueToAdd;
	
	@Setter
	@Getter
	@JsonProperty("conditionalExpression")
	private String conditionalExpression;
	
	@JsonIgnore
	protected QuantifierValues quantifier = QuantifierValues.ONE_OR_MORE;


	public FieldContentConditionalAddOccrRule() {
		evaluator = new FieldExpressionEvaluator(this.quantifier);
	}

	@Setter
	@Getter
	@JsonProperty("removeDuplicatedOccurrences")
	private Boolean removeDuplicatedOccurrences = false;



	@Override
	public boolean transform(OAIRecord record) {

		OAIRecordMetadata metadata = record.getMetadata();
		
		boolean isExpressionValid = evaluator.evaluate(conditionalExpression, metadata);		
		boolean wasTransformed = false;
		
		
		if ( isExpressionValid ) {
			wasTransformed = true;
			metadata.addFieldOcurrence(fieldName, valueToAdd);
		}
		
		
		Set<String> occrSet  = new HashSet<String>();
		List<Node> removeList = new ArrayList<Node>();
		
		if ( removeDuplicatedOccurrences ) {

			// recorre las ocurrencias del campo de test
			for ( Node node : metadata.getFieldNodes(fieldName) ) {
	
				String occr = node.getFirstChild().getNodeValue();
	
				if ( occrSet.contains(occr) )
					removeList.add(node);
				else
					occrSet.add(occr);
			}
			
			for (Node node: removeList) 
				metadata.removeFieldNode(node);
		}

		return wasTransformed;
	}

	

}
