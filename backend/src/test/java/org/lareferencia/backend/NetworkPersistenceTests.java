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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.repositories.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class NetworkPersistenceTests {


	@Autowired
	NetworkRepository repository;

	@Test
	@Transactional
	public void testSave() throws Exception {
		Network nn = new Network();
		nn.setName("A name");
		
		OAIOrigin o = new OAIOrigin();
		o.setName("Origin");
		o.setUri("http://Test.com");
		nn.getOrigins().add(o);
		
		OAISet s = new OAISet();
		s.setSpec("set1");
		s.setName("la descripción");
		
		o.getSets().add(s);
	
		
		nn.setAcronym("AR");
		
		repository.save(nn);
		
		assertNotNull(nn.getId());
	}

	@Test
	@Transactional
	public void testSaveAndGet() throws Exception {
		Network nn = new Network();
		nn.setName("A name");
		
		OAIOrigin o = new OAIOrigin();
		o.setUri("test.uri");
		nn.getOrigins().add(o);
		
		
		OAISet s = new OAISet();
		s.setSpec("set1");
		s.setName("la descripción");
		
		o.getSets().add(s);
		
	
		
		repository.saveAndFlush(nn);
	
		Network loadedNN =  repository.findOne( nn.getId() );
	
		assertEquals(nn,loadedNN);
		
		assertEquals(1, loadedNN.getOrigins().size());
		assertEquals(o, loadedNN.getOrigins().iterator().next());	
		
		assertEquals(s, loadedNN.getOrigins().iterator().next().getSets().iterator().next());	
		
		
	}

	

}
