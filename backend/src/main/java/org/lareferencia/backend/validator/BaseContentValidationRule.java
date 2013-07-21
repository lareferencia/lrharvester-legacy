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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * La clase abstracta BaseContentVRule implementa los mecanismos comunes para la evaluación de contenidos de distintas
 * ocurrencias de un mismo metadato.
 * @author lmatas
 *
 */
public abstract class BaseContentValidationRule implements IContentValidationRule {
	
	protected static int MAX_EXPECTED_LENGTH = 255; 
	
	protected String quantifier = IContentValidationRule.QUANTIFIER_ZERO_OR_MORE;
	protected String name = "";
	
	public BaseContentValidationRule() {
	}
	
	
	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param item
	 * @return
	 */
	public abstract ContentValidationResult validate(String content);

}
