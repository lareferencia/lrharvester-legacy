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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.mapping.Array;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.RecordValidationResult;
import org.lareferencia.backend.domain.Validator;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.repositories.RecordValidationResultRepository;
import org.lareferencia.backend.validation.validator.IValidator;
import org.lareferencia.backend.validation.validator.ValidatorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordValidationSolrTests {

	@Autowired
	RecordValidationResultRepository repository;
	
	@Autowired
	IValidator validator;
	
	static String validRecord = "<metadata xmlns=\"http://www.openarchives.org/OAI/2.0/\">" +
			"<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">" +
			"<dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Grieco y Bavio, Alfredo</dc:creator>" +
			"<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Nec Substantiam sed Culpam : el cervantismo alegórico de Alicia Parodi y el caso del Licenciado Vidriera</dc:title>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Humanidades</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Letras</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Literatura</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Crítica literaria</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Cervantes; novelas ejemplares; alegoría; crítica textual</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Cervantes; allegory; text critique</dc:subject>" +
			"<dc:language xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">spa</dc:language>" +
			"<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">2005</dc:date>" +
			"<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">20</dc:date>" +

			"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://creativa.uaslp.mx/creativa.pl?d=-2&amp;id=7841&amp;t=TESU</dc:identifier>" +
			//"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://hdl.handle.net/10915/12185@5</dc:identifier>" +
			//"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://www.memoria.fahce.unlp.edu.ar/art_revistas/pr.3374/pr.3374.pdf</dc:identifier>" +
			"<dc:relation xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Olivar</dc:relation>" +
			"<dc:relation xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">vol. 6, no. 6</dc:relation>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Alicia Parodi encuentra en el sentido alegórico de Cervantes la historia de la salvación. Su crítica se define como alegórica. No obstante, el interés y la preocupación por la crítica textual está presente en todos sus textos.</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Número monográfico. El cervantismo argentino : una historia tentativa</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Monographic issue. El cervantismo argentino : una historia tentativa</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Volume monográfico. El cervantismo argentino : una historia tentativa</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Centro de Estudios de Teoría y Crítica Literaria</dc:description>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">artículo</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/publishedVersion</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Articulo</dc:type>" +
			"<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">2005</dc:date>" +
			"<dc:rights xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/openAccess</dc:rights>" +
			"<dc:format xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">p. 115-134</dc:format>" +
			"</oai_dc:dc>" +
			"</metadata>";
	

	@Test
	@Transactional
	public void testSave() throws Exception {
		
		
		
		
		
		/*
		
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
		
	*/
	}
	

	@Test
	public void testRecordWiredDriverValidator() throws Exception {
		/*
		Network network = new Network();
		network.setAcronym("TEST");
		network.setInstitutionName("Inst");
		network.setName("Repo");
		
		NetworkSnapshot snapshot = new NetworkSnapshot();
		
		snapshot.setNetwork(network);
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",validRecord);

		ValidatorResult vresult = validator.validate(record);
		
		RecordValidationResult rvresult = new RecordValidationResult(snapshot, record, vresult);
		
		repository.save(rvresult);

		System.out.println( );	
		assertTrue( validator.validate(record).isValid() );		*/

	}
	
}
	
	