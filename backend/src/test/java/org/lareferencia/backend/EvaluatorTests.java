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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.validator.FieldExpressionEvaluator;
import org.lareferencia.backend.validation.validator.QuantifierValues;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fathzer.soft.javaluator.AbstractEvaluator;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class EvaluatorTests {

	static String xmlstring = "<metadata xmlns=\"http://www.openarchives.org/OAI/2.0/\">"
			+ "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">"
			+ "<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Discovering geographic services from textual use cases</dc:title>"
			+ "<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://sedici.unlp.edu.ar/handle/10915/9670</dc:identifier>"
			+ "<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>"
			+ "<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/publishedVersion</dc:type>"
			+ "</oai_dc:dc>" + "</metadata>";

	@Test
	public void test() throws Exception {

		OAIRecordMetadata metadata = new OAIRecordMetadata("dumyid", xmlstring);

		FieldExpressionEvaluator evaluator = new FieldExpressionEvaluator(QuantifierValues.ONE_OR_MORE);

		System.out
				.println("dc:type=info:eu-repo/semantics/article -> "
						+ evaluator
								.evaluate(
										"dc:type=='info:eu-repo/semantics/article' OR dc:type=='info:eu-repo/semantics/doctoralThesis' OR dc:type=='info:eu-repo/semantics/masterThesis' OR dc:type=='info:eu-repo/semantics/report'",
										metadata));

	}

}
