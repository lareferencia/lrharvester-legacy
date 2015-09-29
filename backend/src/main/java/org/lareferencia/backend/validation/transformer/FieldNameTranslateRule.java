/*******************************************************************************
 * Copyright (c) 2015
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 * 
 ******************************************************************************/

package org.lareferencia.backend.validation.transformer;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.w3c.dom.Node;

public class FieldNameTranslateRule extends AbstractTransformerRule {

	@Getter
	Map<String, String> translationMap;

	@Setter
	@Getter
	String sourceFieldName;
	
	@Setter
	@Getter
	String targetFieldName;
	

	public FieldNameTranslateRule() {
	}

	@Override
	public boolean transform(OAIRecordMetadata metadata) {

		boolean wasTransformed = false;
	
		// ciclo de reemplazo
		// recorre las ocurrencias del campo de nombre source creando instancias con nombre target
		for (Node node : metadata.getFieldNodes(this.getSourceFieldName())) {
			
			// Agrega instancia target con el contenido a reemplazar
			String occr = node.getFirstChild().getNodeValue();
			metadata.addFieldOcurrence(this.getTargetFieldName(), occr);
			
			// Remueve la actual
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
			
			// si entra al ciclo al menos una vez entonces transformó
			wasTransformed = true;
		}

		return wasTransformed;
	}

	

}
