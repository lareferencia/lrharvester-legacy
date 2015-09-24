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

import java.util.List;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ValidationResult;

public interface ITransformer {
	
	public List<AbstractTransformerRule> getFieldTransformers();
	public void setFieldTransformers( List<AbstractTransformerRule> validators);
	
	/**
	 * 
	 * @param  metadata 
	 * @param  validationResult
	 * @return Retorna true si hizo alguna transformación
	 * @throws Exception
	 */
	public boolean transform(OAIRecordMetadata metadata, ValidationResult validationResult) throws Exception;
	//TODO: Implementar Exceptions específicas
	
}
