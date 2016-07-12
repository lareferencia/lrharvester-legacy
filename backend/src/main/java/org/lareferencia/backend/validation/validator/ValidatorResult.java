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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatorResult {

	private boolean valid;
	private List<ValidatorRuleResult> rulesResults;

	public ValidatorResult() {
		rulesResults = new ArrayList<ValidatorRuleResult>();
	}

	public String getValidationContentDetails() {

		StringBuilder sb = new StringBuilder();

		for (ValidatorRuleResult entry : rulesResults) {

			for (ContentValidatorResult result : entry.getResults()) {
				// Solo detalla los valores inválidos o válidos, según el caso
				sb.append(entry.getRule().getRuleId() + ":" + result.getReceivedValue());

				sb.append(";");
			}
		}

		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';')
			sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	@Override
	public String toString() {

		String toStr = "Validation: ";
		toStr += " record valid=" + valid + "\n\n";

		for (ValidatorRuleResult entry : rulesResults) {

			toStr += entry.getRule().getRuleId() + ":\n";
			toStr += entry.toString() + "\n\n";
		}
		return toStr;
	}

}
