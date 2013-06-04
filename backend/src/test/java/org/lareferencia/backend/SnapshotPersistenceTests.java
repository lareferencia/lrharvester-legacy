package org.lareferencia.backend;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.Country;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SnapshotPersistenceTests {


	@Autowired
	NationalNetworkRepository repository;
	
	@Autowired
	NetworkSnapshotRepository nsrepository;

	@Test
	@Transactional
	public void testSave() throws Exception {
		NationalNetwork nn = new NationalNetwork();
		nn.setName("A name");
		
		OAIOrigin o = new OAIOrigin();
		o.setName("Origin");
		o.setUri("http://Test.com");
		nn.getOrigins().add(o);
		
		OAISet s = new OAISet();
		s.setSpec("set1");
		s.setName("la descripción");
		
		o.getSets().add(s);
	
		Country c = new Country();
		c.setIso("AR");
		c.setName("Argentina");
		nn.setCountry(c);
		
		NetworkSnapshot ns = new NetworkSnapshot();
		nn.getSnapshots().add(ns);
		
		OAIRecord record = new OAIRecord("oai:test/0001", "<xml/>");

	
		record.setSnapshot(ns);
		
		repository.save(nn);
		assertNotNull(ns.getId());	
	}
	
	/*
	@Test
	@Transactional
	public void testLGK() throws Exception {

		NetworkSnapshot ns = nsrepository.findLastGoodKnowByNetworkID(4L);
		System.out.println(ns.getId());
		
		
		assertNotNull(ns);	

	}
	
	*/
	
/*
	@Test
	@Transactional
	public void testSaveAndGet() throws Exception {
		NationalNetwork nn = new NationalNetwork();
		nn.setName("A name");
		
		OAIOrigin o = new OAIOrigin();
		o.setUri("test.uri");
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
