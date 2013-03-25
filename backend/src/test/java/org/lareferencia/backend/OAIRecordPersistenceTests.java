package org.lareferencia.backend;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class OAIRecordPersistenceTests {


	@Autowired
	OAIRecordRepository repository;

	@Test
	@Transactional
	public void testSave() throws Exception {
		
		OAIRecord record = new OAIRecord();
		record.setIdentifier("oai:test/0001");
		record.setOriginalXML("<xml/>");
		record.setPublishedXML("<xml/>");
	
		repository.save(record);
		
		assertNotNull(record.getId());
	}
	
	@Test
	@Transactional
	public void testSaveEmptyLob() throws Exception {
	/*Bug in Hibernate 3.6 solved in 3.6.10*/	
		OAIRecord record = new OAIRecord();
		record.setIdentifier("oai:test/0001");
		record.setOriginalXML("");
		record.setPublishedXML("");
		
		
		repository.save(record);
		
		assertNotNull(record.getId());
	}
	

	@Test(expected=DataIntegrityViolationException.class)
	@Transactional
	public void testSaveDuplicateIdentifier() throws Exception {
		
		OAIRecord record1 = new OAIRecord();
		record1.setIdentifier("oai:test/0001");

		OAIRecord record2 = new OAIRecord();
		record2.setIdentifier("oai:test/0001");
		
		repository.save(record1);
		repository.save(record2);	
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	@Transactional
	public void testSaveNullIdentifier() throws Exception {
		
		OAIRecord record1 = new OAIRecord();
		
		repository.save(record1);
	}
/*
	@Test
	@Transactional
	public void testSaveAndGet() throws Exception {
		NationalNetwork nn = new NationalNetwork();
		
		OAIOrigin o = new OAIOrigin();
		nn.getOrigins().add(o);
		
		OAISet s = new OAISet();
		s.setName("set1");
		s.setDescription("la descripción");
		
		o.getSets().add(s);
		
		Country c = new Country();
		c.setIso("AR");
		c.setName("Argentina");
		nn.setCountry(c);
		
		repository.saveAndFlush(nn);
	
		NationalNetwork loadedNN =  repository.findOne( nn.getId() );
	
		assertEquals(nn,loadedNN);
		
		assertEquals(1, loadedNN.getOrigins().size());
		assertEquals(o, loadedNN.getOrigins().iterator().next());	
		
		assertEquals(s, loadedNN.getOrigins().iterator().next().getSets().iterator().next());	
		
		assertEquals(c, loadedNN.getCountry());
		
	}
*/
	

}
