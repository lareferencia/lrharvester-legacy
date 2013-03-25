package org.lareferencia.backend;

import org.lareferencia.backend.domain.Country;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public  class DevMain {

	@Autowired
	public NationalNetworkRepository repository;
	
	@Autowired
	public OAIRecordRepository recordRepository;
	
	public OAIRecordRepository getRecordRepository() {
		return recordRepository;
	}



	public DevMain() {
		
	}
	
	
	
	public NationalNetworkRepository getRepository() {
		return repository;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
		  
		
		DevMain dao =  context.getBean("devMain",DevMain.class);
		NationalNetworkRepository nrepo = dao.getRepository();
		OAIRecordRepository rrepo = dao.getRecordRepository();
		
		
		NationalNetwork nn = new NationalNetwork();
		
		
		OAIOrigin o = new OAIOrigin();
		o.setName("primer origen");
		o.setUri("http://test.com/");
		
		OAISet s = new OAISet();
		s.setName("set1");
		s.setDescription("la descripción");
		
		o.getSets().add(s);
		
		Country c = new Country();
		c.setName("Argentina");
		c.setIso("AR");
		
		nn.getOrigins().add(o);
		nn.setCountry(c);
		
		nrepo.save(nn);
		
		
		OAIRecord record = new OAIRecord();
		record.setIdentifier("oai:test/0001");
		record.setOriginalXML("<xml/>");
		record.setPublishedXML("<xml/>");
	
		rrepo.save(record);

	}

}
