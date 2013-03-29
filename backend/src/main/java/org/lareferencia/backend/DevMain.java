package org.lareferencia.backend;

import java.util.Date;

import org.lareferencia.backend.domain.Country;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.domain.Schedule;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.tasks.SnapshotCronTrigger;
import org.lareferencia.backend.tasks.Processor;
import org.lareferencia.backend.tasks.SnapshotManager;
import org.lareferencia.backend.tasks.SnapshotProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;


public  class DevMain {

	@Autowired
	public NationalNetworkRepository repository;
	
	@Autowired
	public OAIRecordRepository recordRepository;
	
	@Autowired
	public TaskScheduler scheduler;
	
	
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
		/*
		NationalNetworkRepository nrepo = dao.getRepository();
		
		SnapshotProcessor processor = context.getBean("snapshotProcessor", SnapshotProcessor.class);
		processor.setNetwork(nrepo.findOne(1L));
		
		processor.run();
		*/
		
		
		SnapshotManager snapshotManager =  context.getBean("snapshotManager",SnapshotManager.class);
		snapshotManager.refresh();

		/*
		NationalNetworkRepository nrepo = dao.getRepository();
		OAIRecordRepository rrepo = dao.getRecordRepository();
		
		NationalNetwork nn = new NationalNetwork();
		nn.setName("IBICT Brasil");
		
		OAIOrigin o = new OAIOrigin();
		o.setName("primer origen");
		o.setUri("http://test.com/");
		
		OAISet s = new OAISet();
		s.setName("set1");
		s.setDescription("la descripción");
		
		o.getSets().add(s);
		
		Country c = new Country();
		c.setName("Brasil");
		c.setIso("BR");
		
		Schedule sch = new Schedule();
		sch.setCronExpression("0/5 * * * * ?");
		
		nn.setSchedule(sch);
		
		nn.getOrigins().add(o);
		nn.setCountry(c);
		
		OAIRecord record = new OAIRecord();
		record.setIdentifier("oai:test/0001");
		record.setOriginalXML("<xml/>");
		record.setPublishedXML("<xml/>");
	
		NetworkSnapshot ns = new NetworkSnapshot();
		ns.getRecords().add(record);
	
		nn.getSnapshots().add(ns);
			
		nrepo.saveAndFlush(nn);
		
		*/
	}

	public TaskScheduler getScheduler() {
		return scheduler;
	}

}
