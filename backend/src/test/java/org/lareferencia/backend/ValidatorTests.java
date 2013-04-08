package org.lareferencia.backend;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.lareferencia.backend.validator.LengthContentValidationRule;
import org.lareferencia.backend.validator.ControlledValueContentValidationRule;
import org.lareferencia.backend.validator.IContentValidationRule;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.RegexContentValidationRule;
import org.lareferencia.backend.validator.ValidatorImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	
	@Test
	public void testLengthRule() throws Exception {
		
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
		LengthContentValidationRule clrule = new LengthContentValidationRule();
		clrule.setMinLength(2);
		clrule.setMaxLength(100);
	
		
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertTrue("dc:type  " + clrule, clrule.validate(content) == content.length() >= 2 && content.length() <= 100  );
		}
		
		for (String content: record.getFieldOcurrences("dc:title")) {	
			assertTrue("dc:title  " + clrule, clrule.validate(content) == content.length() >= 2 && content.length() <= 100  );
		}
	
	}
	
	
	@Test
	public void testControlledValueRule() throws Exception {
		
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
		ControlledValueContentValidationRule ccrule = new ControlledValueContentValidationRule();

		ArrayList<String> cclist = new ArrayList<String>();
		cclist.add("info:eu-repo/semantics/publishedVersion");
		cclist.add("info:eu-repo/semantics/article");
		
		
		ccrule.setControlledValues(cclist);
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertTrue("dc:type  " + ccrule, ccrule.validate(content)  );
		}
		
		for (String content: record.getFieldOcurrences("dc:title")) {	
			assertFalse("dc:type  " + ccrule, ccrule.validate(content)  );
		}
	}
	
	@Test
	public void testRegexRule() throws Exception {
		
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
		RegexContentValidationRule rerule = new RegexContentValidationRule();

		rerule.setRegexString("info.*");
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertTrue("dc:type  " + rerule, rerule.validate(content)  );
		}
		
		rerule.setRegexString("noamatchexpresion");
		for (String content: record.getFieldOcurrences("dc:type")) {	
			assertFalse("dc:type  " + rerule, rerule.validate(content)  );
		}
	}
	
	@Test
	public void testValidator() throws Exception {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
	
		ValidatorImpl validator = new ValidatorImpl();
		
		Map<String, List<IContentValidationRule>> rulesPerField = new HashMap<String, List<IContentValidationRule>>();
		
		ArrayList<String> cclist = new ArrayList<String>();
		cclist.add("info:eu-repo/semantics/publishedVersion");
		
		ArrayList<IContentValidationRule> type_rules = new ArrayList<IContentValidationRule>(); 
		
		IContentValidationRule cvrule = new ControlledValueContentValidationRule(cclist,true);
		IContentValidationRule clrule = new LengthContentValidationRule(1, 255, true);
		
		type_rules.add(cvrule);
		type_rules.add(clrule);
		
		rulesPerField.put("dc:type", type_rules);
		
		validator.setRulesPerField(rulesPerField);
		
		// dc:type tiene un valor que no esta dentro de los controlados
		assertFalse( validator.validate(record).isValid() );
		
		// dc:type ahora no es requerido
		cvrule.setMandatory(false);
		assertTrue( validator.validate(record).isValid() );

		// agregado del valor faltante y vuelve a ser requerido
		cvrule.setMandatory(true);
		cclist.add("info:eu-repo/semantics/article");
		assertTrue( validator.validate(record).isValid() );

		
		
	}
	
}
