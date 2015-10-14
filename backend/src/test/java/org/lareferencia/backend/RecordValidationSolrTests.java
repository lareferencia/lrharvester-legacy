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
package org.lareferencia.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.mapping.Array;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.RecordValidationResult;
import org.lareferencia.backend.repositories.RecordValidationResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordValidationSolrTests {

	@Autowired
	RecordValidationResultRepository repository;

	@Test
	@Transactional
	public void testSave() throws Exception {
		
		
		System.out.print("TEST");
		
		RecordValidationResult result = new RecordValidationResult();
		result.setId("0001");
	
		Map<String, List<String>> dfieldmap = new HashMap<String, List<String>>();
		List<String> l1 = new ArrayList<String>();
		List<String> l2 = new ArrayList<String>();
		l1.add("HolaL1-1");
		l1.add("HolaL1-2");
		l2.add("HolaL2-1");
		l2.add("HolaL2-2");
		dfieldmap.put("f1", l1);
		dfieldmap.put("f2", l2);
		
		
		repository.save(result);
		
	
	}
	
}
	
	