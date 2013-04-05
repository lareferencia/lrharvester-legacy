package org.lareferencia.backend;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.tasks.ISnapshotWorker;
import org.lareferencia.backend.tasks.SnapshotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;


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
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		DevMain dao =  context.getBean("devMain",DevMain.class);
		
		NationalNetworkRepository nrepo = dao.getRepository();
		NationalNetwork network = nrepo.findOne(1L);
		
		ISnapshotWorker processor = context.getBean("snapshotWorker", ISnapshotWorker.class);
		processor.setNetworkID(network.getId());
	
		processor.run();
		/*
		SnapshotManager snapshotManager =  context.getBean("snapshotManager",SnapshotManager.class);
		snapshotManager.refresh();
		*/
		
		/*
		NationalNetworkRepository nrepo = dao.getRepository();
		OAIRecordRepository rrepo = dao.getRecordRepository();
		
		NationalNetwork nn = new NationalNetwork();
		nn.setName("IBICT Brasil");
		
		OAIOrigin o = new OAIOrigin();
		o.setName("Origen principal");
		o.setUri("http://repositoriosdigitales.mincyt.gob.ar:8280/is/mvc/oai/oai.do");
		
		Country c = new Country();
		c.setName("Brasil");
		c.setIso("BR");
		
		Schedule sch = new Schedule();
		sch.setCronExpression("0/5 * * * * ?");
		
		nn.setSchedule(sch);
		nn.getOrigins().add(o);
		nn.setCountry(c);
				
		nrepo.saveAndFlush(nn);
		*/
		
	}

	public TaskScheduler getScheduler() {
		return scheduler;
	}

}
