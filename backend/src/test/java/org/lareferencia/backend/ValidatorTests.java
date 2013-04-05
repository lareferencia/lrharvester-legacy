package org.lareferencia.backend;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.lareferencia.backend.validator.FieldContentLengthVRule;
import org.lareferencia.backend.validator.FieldContentRegexVRule;
import org.lareferencia.backend.validator.FieldOccurrenceRangeVRule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

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
	public void testOccurrenceRule() throws Exception {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
		
		
		FieldOccurrenceRangeVRule orule = new FieldOccurrenceRangeVRule();
		
		orule.setMinOccurences(2);
		orule.setMaxOccurences(100);
		orule.setFieldName("dc:type");
		
		assertTrue( orule.validate(record) );
		
		orule.setFieldName("dc:title");
		assertFalse( orule.validate(record) );
		
		orule.setFieldName("noexistendfield");
		assertFalse( orule.validate(record) );
		
	}
	
	
	@Test
	public void testFieldContentLengthRule() throws Exception {
		
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
		FieldContentLengthVRule clrule = new FieldContentLengthVRule();
		clrule .setMinLength(2);
		clrule.setMaxLength(100);
		clrule.setFieldName("dc:type");
		
		assertTrue( clrule.validate(record) );
		
		clrule.setMinLength(2);
		clrule.setMaxLength(2);
		clrule.setFieldName("dc:title");
		assertFalse( clrule.validate(record) );
		
		clrule.setMinLength(100);
		clrule.setMaxLength(255);
		clrule.setFieldName("dc:title");
		assertFalse( clrule.validate(record) );
		
		clrule.setFieldName("noexistendfield");
		assertFalse( clrule.validate(record) );
		
		clrule.setAllOccurrencesMustBeValid(true);
		clrule.setMinLength(30);
		clrule.setMaxLength(255);
		clrule.setFieldName("dc:type");
		assertTrue( clrule.validate(record) );
		
		clrule.setAllOccurrencesMustBeValid(true);
		clrule.setMinLength(31);
		clrule.setMaxLength(255);
		clrule.setFieldName("dc:type");
		assertFalse( clrule.validate(record) );
	}
	
	@Test
	public void testFieldContentControlledValueRule() throws Exception {
		
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);
		
		HarvesterRecord record = new HarvesterRecord("dumyid",doc);
		
		FieldContentRegexVRule rerule = new FieldContentRegexVRule();
		rerule  .setFieldName("dc:type");
		rerule.setRegexString(".*");
		assertTrue( rerule.validate(record) );
		
		rerule.setFieldName("dc:type");
		rerule.setAllOccurrencesMustBeValid(true);
		rerule.setRegexString("info.*");
		assertTrue( rerule.validate(record) );
		
		rerule.setFieldName("dc:type");
		rerule.setAllOccurrencesMustBeValid(false);
		rerule.setRegexString("noamatchexpresion");
		assertFalse( rerule.validate(record) );
		
		
	}
}
