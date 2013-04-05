package org.lareferencia.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DOMHelperTests {

	static String xmlstring = "<metadata xmlns=\"http://www.openarchives.org/OAI/2.0/\">" +
			"<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">" +
			"<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">Discovering geographic services from textual use cases</dc:title>" +
			"<dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">http://sedici.unlp.edu.ar/handle/10915/9670</dc:identifier>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/article</dc:type>" +
			"<dc:type xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns=\"http://www.driver-repository.eu/\">info:eu-repo/semantics/publishedVersion</dc:type>" +
			"</oai_dc:dc>" +
			"</metadata>";
	
	@Test()
	public void testParse() throws ParserConfigurationException, SAXException, IOException {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);		
		assertNotNull( doc ); 	
	}
	
	@Test()
	public void testParseAndXpathSingleString() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);	
		
		assertEquals("Discovering geographic services from textual use cases",  MedatadaDOMHelper.getSingleString(doc, "//dc:title") );	
	}
	
	@Test()
	public void testParseAndXpathNodeList() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		Document doc = MedatadaDOMHelper.parseXML(xmlstring);	
		
		assertEquals(2,  MedatadaDOMHelper.getNodeList(doc, "//dc:type").getLength() );
		
		assertEquals("info:eu-repo/semantics/article",  MedatadaDOMHelper.getNodeList(doc, "//dc:type").item(0).getFirstChild().getNodeValue() );	
	}

	

}
