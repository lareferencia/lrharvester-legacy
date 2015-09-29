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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.RepositoryNameHelper;
import org.lareferencia.backend.validation.transformer.FieldContentNormalizeRule;
import org.lareferencia.backend.validation.transformer.FieldContentTranslateRule;
import org.lareferencia.backend.validation.transformer.FieldNameTranslateRule;
import org.lareferencia.backend.validation.transformer.ITransformerRule;
import org.lareferencia.backend.validation.validator.ContentLengthValidationRule;
import org.lareferencia.backend.validation.validator.ControlledValueContentValidationRule;
import org.lareferencia.backend.validation.validator.IValidatorRule;
import org.lareferencia.backend.validation.validator.QuantifierValues;
import org.lareferencia.backend.validation.validator.RegexContentValidationRule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TransformerTests {
	
	static String xmlstring = "<metadata xmlns=\"http://www.openarchives.org/OAI/2.0/\">" +
			"<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">" +
			"<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Discovering geographic services from textual use cases</dc:title>" +
			"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://sedici.unlp.edu.ar/handle/10915/9670</dc:identifier>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/publishedVersion</dc:type>" +
			"</oai_dc:dc>" +
			"</metadata>";
	
	
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
	
	
	static String invalidRecord = "<metadata xmlns=\"http://www.openarchives.org/OAI/2.0/\">" +
			"<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">" +
			"<dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Grieco y Bavio, Alfredo</dc:creator>" +
			"<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Nec Substantiam sed Culpam : el cervantismo alegórico de Alicia Parodi y el caso del Licenciado Vidriera</dc:title>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Humanidades</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Letras</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Literatura</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Crítica literaria</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Cervantes; novelas ejemplares; alegoría; crítica textual</dc:subject>" +
			"<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Cervantes; allegory; text critique</dc:subject>" +
			"<dc:language xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">SPA</dc:language>" +
			"<dc:language xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">JOSE</dc:language>" +
			"<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">2005</dc:date>" +
			"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://sedici.unlp.edu.ar/handle/10915/12185</dc:identifier>" +
			"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://hdl.handle.net/10915/12185</dc:identifier>" +
			"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://www.memoria.fahce.unlp.edu.ar/art_revistas/pr.3374/pr.3374.pdf</dc:identifier>" +
			"<dc:relation xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Olivar</dc:relation>" +
			"<dc:relation xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">vol. 6, no. 6</dc:relation>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Alicia Parodi encuentra en el sentido alegórico de Cervantes la historia de la salvación. Su crítica se define como alegórica. No obstante, el interés y la preocupación por la crítica textual está presente en todos sus textos.</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Número monográfico. El cervantismo argentino : una historia tentativa</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Monographic issue. El cervantismo argentino : una historia tentativa</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Volume monográfico. El cervantismo argentino : una historia tentativa</dc:description>" +
			"<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Centro de Estudios de Teoría y Crítica Literaria</dc:description>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">artículo</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">published</dc:type>" +
			//"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Articulo</dc:type>" +
			"<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">2005</dc:date>" +
			"<dc:rights xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/openAccess</dc:rights>" +
			"<dc:format xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">text/plain</dc:format>" +
			"</oai_dc:dc>" +
			"</metadata>";
	
	
	static String brRecord = "<metadata xmlns=\"http://www.openarchives.org/OAI/2.0/\">" +
			"<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">" +
			"<dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Laia, Marconi Martins de</dc:creator>"
			+ "<dc:contributor xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Cabral, Ana Maria de Rezende</dc:contributor>" +
            "<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\">2009</dc:date>" +
            "<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\">2013-12-09T19:39:52Z</dc:date>" +
            "<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\">2013-12-09T19:39:52Z</dc:date>" +
            "<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\">http://www.repositorio.fjp.mg.gov.br/handle/123456789/225</dc:identifier>" +
            "<dc:description xmlns:dc=\"http://purl.org/dc/elements/1.1/\">LAIA, Marconi Martins de. Pol??ticas de governo eletr??nico em Estados</dc:description>" +
            "<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\">doctoralThesis</dc:type>" +
            "<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Pol??ticas de governo eletr??nico em estados da federa????o brasileira: uma contribui????o para a an??lise segundo a perspectiva neoinstitucional</dc:title>" +
            "<dc:language xmlns:dc=\"http://purl.org/dc/elements/1.1/\">por</dc:language>" +
            "<dc:rights xmlns:dc=\"http://purl.org/dc/elements/1.1/\">openAccess</dc:rights>" +
            "<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Governo eletr??nico</dc:subject>" +
            "<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\">e-Government</dc:subject>" +
			"</oai_dc:dc>" +
			"</metadata>";
	
	
	/**	
	@Autowired
	IValidator validator;
	
	@Autowired
	@Qualifier("defaultTransformer")
	ITransformer transformer;
	
	@Autowired
	@Qualifier("langFieldTransformer")
	TranslateContentFieldTransformer langFieldTransformer;
	**/
	
	
	
	@Test
	public void testSameFieldContentTranslateTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",invalidRecord);
		
		FieldContentTranslateRule trule = new FieldContentTranslateRule();
		trule.setTestFieldName("dc:type");
		trule.setWriteFieldName("dc:type");
		trule.getTranslationMap().put("artículo", "Article");
	
		trule.transform( record );
		
		System.out.println( record.toString() );
		
		

		Boolean found = false;
		for (String occr : record.getFieldOcurrences("dc:type") ) {
			found |= occr.equals("artículo");
		}
		
		assertFalse(found);	
		
		found = false;
		for (String occr : record.getFieldOcurrences("dc:type") ) {
			found |= occr.equals("Article");
		}
		
		assertTrue(found);	
	}
	

	@Test
	public void testDifferentFieldContentTranslateTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",invalidRecord);
		
		FieldContentTranslateRule trule = new FieldContentTranslateRule();
		trule.setTestFieldName("dc:type");
		trule.setWriteFieldName("dc:test");
		trule.getTranslationMap().put("artículo", "Article");
		trule.setReplaceOccurrence(false);
	
		trule.transform( record );
		
		System.out.println( record.toString() );
		
		Boolean found = false;
		for (String occr : record.getFieldOcurrences("dc:type") ) {
			found |= occr.equals("artículo");
		}	
		assertTrue(found);	
		
		found = false;
		for (String occr : record.getFieldOcurrences("dc:test") ) {
			found |= occr.equals("Article");
		}
		assertTrue(found);	
	}
	
	@Test
	public void testDifferentPrefixFieldContentTranslateTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",invalidRecord);
		
		FieldContentTranslateRule trule = new FieldContentTranslateRule();
		trule.setTestFieldName("dc:type");
		trule.setWriteFieldName("dc:test");
		trule.getTranslationMap().put("artí", "Article");
		trule.setReplaceOccurrence(true);
	
		trule.transform( record );

		Boolean found = false;
		for (String occr : record.getFieldOcurrences("dc:test") ) {
			found |= occr.equals("Article");
		}
		assertFalse(found);	
		
		////////////
		
		trule.setTestValueAsPrefix(true);
		trule.transform( record );

		
		found = false;
		for (String occr : record.getFieldOcurrences("dc:test") ) {
			found |= occr.equals("Article");
		}
		assertTrue(found);	

	}
	
	@Test
	public void testFieldNameTranslateTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",invalidRecord);
		
		FieldNameTranslateRule trule = new FieldNameTranslateRule();
		trule.setSourceFieldName("dc:type");
		trule.setTargetFieldName("dc:test");

		Boolean found = false;
		for (String occr : record.getFieldOcurrences("dc:test") ) {
			found = true;
		}
		assertFalse(found);	
		
	
		trule.transform( record );
			

		found = false;
		for (String occr : record.getFieldOcurrences("dc:test") ) {
			found = true;
		}
		assertTrue(found);	
	}
	
	@Test
	public void testFieldContentNormalizeTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",invalidRecord);
		
		int k = 0;
		for (String occr : record.getFieldOcurrences("dc:type") ) {

			if ( occr.equals("info:eu-repo/semantics/article") )
				k++;
		
		}
		assertTrue(k==2);	
		
		
		ControlledValueContentValidationRule ccrule = new ControlledValueContentValidationRule();
		ccrule.setQuantifier(QuantifierValues.ALL);
		ccrule.setFieldname("dc:type");


		ArrayList<String> cclist = new ArrayList<String>();
		cclist.add("info:eu-repo/semantics/article");
		ccrule.setControlledValues(cclist);
		
		FieldContentNormalizeRule trule = new FieldContentNormalizeRule();
		trule.setFieldName("dc:type");
		trule.setRemoveDuplicatedOccurrences(true);
		trule.setRemoveInvalidOccurrences(true);
		trule.setValidationRule(ccrule);

		trule.transform(record);
	
		int occurrencesSize = 0;
		k = 0;
		for (String occr : record.getFieldOcurrences("dc:type") ) {

			occurrencesSize++;
			
			if ( occr.equals("info:eu-repo/semantics/article") )
				k++;
		
		}
		
		assertTrue(k==1);
		assertTrue(occurrencesSize==1);	
		
		
		System.out.println( record.toString() );		
	}
	
	

	
	
	
}
