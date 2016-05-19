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
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class OAIRecordPaginationTests {

	private static final int PAGE_SIZE = 5;

	@Autowired
	OAIRecordRepository oaiRecordRepository;

	@Autowired
	NetworkSnapshotRepository networkSnapshotRepository;

	@Test
	@Transactional
	public void test() throws Exception {

		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(9L);

		Page<OAIRecord> page = oaiRecordRepository.findBySnapshotId(
				snapshot.getId(), new PageRequest(0, PAGE_SIZE));
		int totalPages = page.getTotalPages();

		List<Long> allIdsNotOptimized = new ArrayList<Long>();
		List<Long> allIdsOptimized = new ArrayList<Long>();

		for (int i = 0; i < totalPages; i++) {
			page = oaiRecordRepository.findBySnapshotId(snapshot.getId(),
					new PageRequest(i, PAGE_SIZE));

			for (OAIRecord record : page.getContent())
				allIdsNotOptimized.add(record.getId());
		}

		/*
		 * page = oaiRecordRepository.findBySnapshotId(snapshot.getId(), new
		 * PageRequest(0, PAGE_SIZE)); totalPages = page.getTotalPages();
		 * 
		 * Long lastId = -1L;
		 * 
		 * for (int i = 0; i < totalPages; i++) {
		 * 
		 * page = oaiRecordRepository.findBySnapshotIdLimited(snapshot.getId(),
		 * lastId, new PageRequest(0, PAGE_SIZE));
		 * 
		 * List<OAIRecord> records = page.getContent(); for (OAIRecord
		 * record:records) allIdsOptimized.add( record.getId() );
		 * 
		 * lastId = records.get( records.size()-1 ).getId(); }
		 * 
		 * assertEquals( allIdsNotOptimized.size(), allIdsOptimized.size() );
		 * 
		 * for ( int i=0; i<allIdsNotOptimized.size(); i++) { assertEquals(
		 * allIdsNotOptimized.get(i), allIdsOptimized.get(i) ); }
		 */

	}

}
