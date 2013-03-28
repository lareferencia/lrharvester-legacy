package org.lareferencia.backend.tasks;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.lareferencia.backend.domain.NationalNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Scope(value="prototype")
public class SnapshotProcessor implements Processor {

	NationalNetwork network;

	public SnapshotProcessor(NationalNetwork network) {
		this.network = network;
	}
	
	@Override
    @Async
	public void run() {
				
		String threadName = network.getName() + "-"+ Thread.currentThread().getName();
	
	    System.out.println(  threadName + " beginning work. Processing: " );
	    try {
	           Thread.sleep(10000); // simulates work
	        }
	        catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        System.out.println( threadName + " completed work on "); 
	}

	
}
