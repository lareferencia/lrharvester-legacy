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

import java.util.Map;

import org.lareferencia.backend.harvester.OAIRecordMetadata;


public interface IValidatorRule {
	
	public static final String QUANTIFIER_ZERO = "0..0";
	public static final String QUANTIFIER_ALL = "ALL";
	public static final String QUANTIFIER_ZERO_OR_MORE = "0..*";
	public static final String QUANTIFIER_ONE_OR_MORE = "1..*";
	public static final String QUANTIFIER_ONE_ONLY = "1..1";

	public ValidationRuleResult validate(OAIRecordMetadata metadata);
		
	public void setRuleID(String id);
	public String getRuleID();

	public void setName(String name);
	public String getName();
	
	public void setDescription(String description);
	public String getDescription();
	
	public void setMandatory(Boolean mandatory);
	public Boolean getMandatory();
	
	public void setQuantifier(String quantifier);
	public String getQuantifier();
	
	public Map<String, String> getParameters();
	public void setParameters(Map<String,String> parameters);
	
	public String getParameterValue(String pname);
}
