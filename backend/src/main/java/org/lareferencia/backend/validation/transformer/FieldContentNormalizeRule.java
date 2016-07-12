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

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.validator.ContentValidatorResult;
import org.lareferencia.backend.validation.validator.IValidatorFieldContentRule;
import org.w3c.dom.Node;

@Getter
@Setter
public class FieldContentNormalizeRule extends AbstractTransformerRule {

	@Override
	public String toString() {
		return "FieldContentNormalizeRule [validationRule=" + validationRule + ", fieldName=" + fieldName + ", removeInvalidOccurrences=" + removeInvalidOccurrences
				+ ", removeDuplicatedOccurrences=" + removeDuplicatedOccurrences + "]";
	}

	private IValidatorFieldContentRule validationRule;

	private String fieldName;

	private Boolean removeInvalidOccurrences = false;
	private Boolean removeDuplicatedOccurrences = false;

	public FieldContentNormalizeRule() {
	}

	@Override
	public boolean transform(OAIRecord record) {

		OAIRecordMetadata metadata = record.getMetadata();

		ContentValidatorResult result;
		boolean wasTransformed = false;
		Set<String> occurencesHistory = new HashSet<String>();

		// Ciclo de búsqueda
		for (Node node : metadata.getFieldNodes(fieldName)) {

			String occr = node.getFirstChild().getNodeValue();

			if (removeInvalidOccurrences) {

				result = validationRule.validate(occr);

				wasTransformed |= !result.isValid();

				if (!result.isValid()) {
					metadata.removeFieldNode(node);
				}
			}

			if (removeDuplicatedOccurrences) {

				if (occurencesHistory.contains(occr)) {
					wasTransformed |= true;
					metadata.removeFieldNode(node);
				}

				occurencesHistory.add(occr);

			}

		}

		return wasTransformed;
	}

}
