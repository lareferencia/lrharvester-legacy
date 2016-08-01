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
package org.lareferencia.backend.validation.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.FieldExpressionEvaluator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * La clase abstracta BaseContentVRule implementa los mecanismos comunes para la
 * evaluación de contenidos de distintas ocurrencias de un mismo metadato.
 * 
 * @author lmatas
 * 
 */

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
public class FieldExpressionValidatorRule extends AbstractValidatorRule {

	@JsonProperty("expression")
	private String expression;

	FieldExpressionEvaluator evaluator;

	public FieldExpressionValidatorRule() {
		evaluator = new FieldExpressionEvaluator(this.quantifier);
	}

	/**
	 * 
	 */
	public ValidatorRuleResult validate(OAIRecord record) {

		OAIRecordMetadata metadata = record.getMetadata();

		ValidatorRuleResult result = new ValidatorRuleResult();

		boolean isRuleValid = evaluator.evaluate(expression, metadata);

		result.setRule(this);
		result.setResults(evaluator.getEvaluationResults());
		result.setValid(isRuleValid);
		return result;

	}

}
