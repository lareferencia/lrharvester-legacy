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

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseValidatorRule implements IValidatorRule {
	
	protected String  ruleID;
	protected String  name;
	protected String  description;
	protected Boolean mandatory = false;
	protected String  quantifier = IValidatorRule.QUANTIFIER_ONE_OR_MORE;
	
	protected Map<String,String> parameters = new HashMap<String, String>();

	public BaseValidatorRule() {
		this.ruleID = "EMPTY";
		this.name = "EMPTY";
		this.mandatory = false;
		this.quantifier = IValidatorRule.QUANTIFIER_ONE_OR_MORE;
		parameters = new HashMap<String, String>();
	};
	
	public BaseValidatorRule(String ruleID, String name, Boolean mandatory, String quantifier) {
		this.ruleID = ruleID;
		this.name = name;
		this.mandatory = mandatory;
		this.quantifier = quantifier;
		parameters = new HashMap<String, String>();
	}
	
	public String getParameterValue(String pname) {
		return parameters.get(pname);
	}
}
