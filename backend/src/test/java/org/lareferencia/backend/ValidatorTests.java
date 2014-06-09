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
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.validator.ControlledValueContentValidationRule;
import org.lareferencia.backend.validator.FieldValidator;
import org.lareferencia.backend.validator.IContentValidationRule;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.LengthContentValidationRule;
import org.lareferencia.backend.validator.RegexContentValidationRule;
import org.lareferencia.backend.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorTests {
	
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
			//"<dc:language xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">es</dc:language>" +
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
			//"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">doctoralthesis</dc:type>"+
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">artículo</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">published</dc:type>" +
			//"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Articulo</dc:type>" +
			"<dc:date xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">2005</dc:date>" +
			"<dc:rights xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/openAccess</dc:rights>" +
			"<dc:format xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">text/plain</dc:format>" +
			"</oai_dc:dc>" +
			"</metadata>";
	
	@Autowired
	IValidator validator;
	
	@Autowired
	@Qualifier("defaultTransformer")
	ITransformer transformer;
	
	@Test
	public void testLengthRule() throws Exception {
		
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",xmlstring);
		
		LengthContentValidationRule clrule = new LengthContentValidationRule();
		clrule.setMinLength(2);
		clrule.setMaxLength(100);
	
		
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertTrue("dc:type  " + clrule, clrule.validate(content).isValid() == content.length() >= 2 && content.length() <= 100  );
			System.out.println( clrule.validate(content) );
		}
		
		for (String content: record.getFieldOcurrences("dc:title")) {	
			assertTrue("dc:title  " + clrule, clrule.validate(content).isValid() == content.length() >= 2 && content.length() <= 100  );
			System.out.println( clrule.validate(content) );

		}
	
	}
	
	
	@Test
	public void testControlledValueRule() throws Exception {
		
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",xmlstring);

		
		ControlledValueContentValidationRule ccrule = new ControlledValueContentValidationRule();

		ArrayList<String> cclist = new ArrayList<String>();
		cclist.add("info:eu-repo/semantics/publishedVersion");
		cclist.add("info:eu-repo/semantics/article");
		
		
		ccrule.setControlledValues(cclist);
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertTrue("dc:type  " + ccrule, ccrule.validate(content).isValid()  );
			System.out.println( ccrule.validate(content) );

		}
		
		for (String content: record.getFieldOcurrences("dc:title")) {	
			assertFalse("dc:type  " + ccrule, ccrule.validate(content).isValid()  );
			System.out.println( ccrule.validate(content) );

		}
	}
	
	@Test
	public void testRegexRule() throws Exception {
			
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",xmlstring);

		
		RegexContentValidationRule rerule = new RegexContentValidationRule();

		rerule.setRegexString("info.*");
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertTrue("dc:type  " + rerule, rerule.validate(content).isValid()  );
			System.out.println( rerule.validate(content) );		
		}
		
		rerule.setRegexString("noamatchexpresion");
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertFalse("dc:type  " + rerule, rerule.validate(content).isValid()  );
			System.out.println( rerule.validate(content) );
		}
		
		rerule.setRegexString("^(http|https)\\://((?!hdl\\.handle\\.net/123456789.*$)[a-zA-Z0-9/\\-\\.\\s\\_\\?\\=\\&\\;\\:\\@])+");
		assertTrue(rerule.validate("http://hdl.handle.net/10915/121 85").isValid()  );
		assertTrue(rerule.validate("https://server.com:8080/janium-bin/janium_zui.pl?jzd=/janium/fotos/bpp-f-009/0502.jzd&amp;amp;fn=8502").isValid());
		assertFalse(rerule.validate("http://hdl.handle.net/123456789/15").isValid()  );
		assertTrue(rerule.validate("http://www.maxwell.lambda.ele.puc-rio.br/Busca_etds.php?strSecao=resultado&nrSeq=11212@1").isValid());
		assertTrue(rerule.validate("http://creativa.uaslp.mx/creativa.pl?d=-2&id=7841&t=TESU").isValid() );		
		
		
		rerule.setRegexString("(^\\d{4}$)|(^\\d{4}-\\d{2}$)|(^\\d{4}-\\d{2}-\\d{2}$)|(^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([+-]\\d{2}:\\d{2}|Z)$)");
		assertTrue(rerule.validate("2000").isValid()  );
		assertTrue(rerule.validate("2000-02").isValid()  );
		assertTrue(rerule.validate("2000-02-02").isValid()  );
		assertFalse(rerule.validate("200").isValid()  );
		assertFalse(rerule.validate("200a-02-02").isValid()  );	
		
		
		
		
	}
	
	@Test
	public void testFieldValidator() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",xmlstring);

	
		ArrayList<String> typeList1 = new ArrayList<String>();
		typeList1.add("info:eu-repo/semantics/article");
		
		ArrayList<String> typeList2 = new ArrayList<String>();
		typeList2.add("info:eu-repo/semantics/publishedVersion");
				
		FieldValidator fvalidator = new FieldValidator("dc:type",true);
		fvalidator.getRules().add(  new ControlledValueContentValidationRule(typeList1, IContentValidationRule.QUANTIFIER_ONE_OR_MORE) );
		fvalidator.getRules().add(  new ControlledValueContentValidationRule(typeList2, IContentValidationRule.QUANTIFIER_ONE_OR_MORE) );
	
		System.out.println( fvalidator.validate(record) );		
	}
	

	
	@Test
	public void testRecordValidator() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",xmlstring);

	
		ArrayList<String> typeList1 = new ArrayList<String>();
		typeList1.add("info:eu-repo/semantics/article");
		
		ArrayList<String> typeList2 = new ArrayList<String>();
		typeList2.add("info:eu-repo/semantics/publishedVersion");
				
		FieldValidator type_validator = new FieldValidator("dc:type",true);
		type_validator.getRules().add( new ControlledValueContentValidationRule(typeList1, IContentValidationRule.QUANTIFIER_ONE_OR_MORE) );
		type_validator.getRules().add( new ControlledValueContentValidationRule(typeList2, IContentValidationRule.QUANTIFIER_ONE_OR_MORE));
	
		FieldValidator identifier_validator = new FieldValidator("dc:identifier",true);
		identifier_validator.getRules().add( new RegexContentValidationRule("^http.*",IContentValidationRule.QUANTIFIER_ONE_OR_MORE) );
		
		
		IValidator validator = new ValidatorImpl();
		validator.getFieldValidators().add( type_validator );
		validator.getFieldValidators().add(identifier_validator);

		
		System.out.println( validator.validate(record) );		
	}
	
	@Test
	public void testRecordWiredDriverValidator() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",validRecord);

		
		System.out.println( validator.validate(record) );	
		assertTrue( validator.validate(record).isValid() );		

	}
	
	@Test
	public void testRecordWiredDriverTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",invalidRecord);
		
		assertFalse( validator.validate(record).isValid() );	
		System.out.println( validator.validate(record) );	
		
		transformer.transform(record, validator.validate(record));
		
		System.out.println( validator.validate(record) );	

		assertTrue( validator.validate(record).isValid() );		
		
		System.out.println( record.toString() );
		
	}
	
	
	@Test
	public void testReplaceTransformer() throws Exception {
		
		OAIRecordMetadata record = new OAIRecordMetadata("dumyid",validRecord);
		
		transformer.transform(record, validator.validate(record));
				
		System.out.println( record.toString() );
		System.out.println( "Remove" );
		
		System.out.println( validator.validate(record) );	

		
	}
	
	
}
