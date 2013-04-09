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
import org.lareferencia.backend.validator.FieldValidator;
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
		
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
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
			
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
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
	}
	
	@Test
	public void testFieldValidator() throws Exception {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
	
		ArrayList<String> typeList1 = new ArrayList<String>();
		typeList1.add("info:eu-repo/semantics/article");
		
		ArrayList<String> typeList2 = new ArrayList<String>();
		typeList2.add("info:eu-repo/semantics/publishedVersion");
				
		FieldValidator fvalidator = new FieldValidator("dc:type");
		fvalidator.addRule( FieldValidator.QUANTIFIER_ONE_OR_MORE, new ControlledValueContentValidationRule(typeList1));
		fvalidator.addRule( FieldValidator.QUANTIFIER_ONE_OR_MORE, new ControlledValueContentValidationRule(typeList2));
	
		System.out.println( fvalidator.validate(record) );		
	}
	
	@Test
	public void testRecordValidator() throws Exception {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
	
		ArrayList<String> typeList1 = new ArrayList<String>();
		typeList1.add("info:eu-repo/semantics/article");
		
		ArrayList<String> typeList2 = new ArrayList<String>();
		typeList2.add("info:eu-repo/semantics/publishedVersion");
				
		FieldValidator type_validator = new FieldValidator("dc:type");
		type_validator.addRule( FieldValidator.QUANTIFIER_ONE_OR_MORE, new ControlledValueContentValidationRule(typeList1));
		type_validator.addRule( FieldValidator.QUANTIFIER_ONE_OR_MORE, new ControlledValueContentValidationRule(typeList2));
	
		FieldValidator identifier_validator = new FieldValidator("dc:identifier");
		identifier_validator.addRule( FieldValidator.QUANTIFIER_ONE_OR_MORE, new RegexContentValidationRule("^http.*") );
		
		
		IValidator validator = new ValidatorImpl();
		validator.addFieldValidator("dc:type", type_validator, true);
		validator.addFieldValidator("dc:identifier", identifier_validator, true);

		
		System.out.println( validator.validate(record) );		
	}
	
}
