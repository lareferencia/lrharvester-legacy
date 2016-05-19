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

@Getter
@Setter
public class ValidatorImpl implements IValidator {

	List<IValidatorRule> rules;

	public ValidatorImpl() {
		super();
		rules = new ArrayList<IValidatorRule>();
	}

	public ValidatorResult validate(OAIRecord record) {

		ValidatorResult result = new ValidatorResult();
		boolean isRecordValid = true;

		for (IValidatorRule rule : rules) {
			ValidatorRuleResult ruleResult = rule.validate(record);
			result.getRulesResults().add(ruleResult);
			isRecordValid &= (ruleResult.getValid() || !rule.getMandatory());
		}

		result.setValid(isRecordValid);

		return result;
	}

}
