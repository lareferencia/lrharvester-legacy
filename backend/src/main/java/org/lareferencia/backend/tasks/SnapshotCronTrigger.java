package org.lareferencia.backend.tasks;

import java.util.Date;

import org.lareferencia.backend.domain.NationalNetwork;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

public class SnapshotCronTrigger implements Trigger {
	
	
	CronTrigger cronTrigger;
	NationalNetwork network;
	
	public SnapshotCronTrigger(NationalNetwork network) {
	/** 
	 * TODO: SnapshotCronTrigger no es una extensión de CronTrigger porque resta implementar un mecanismo que sincronize nextExecutionTime
	 * con los valores (probablemente cambiantes) de Schedule.
	 * Es posible refrescar network cada vez y cambiar entonces la instancia de cronTrigger (por eso no es práctica la herencia)
	 * , de esa manera podría modificarse las futuras ejecuciones, mas no la ya programada.  
	 */
		
		
		this.network = network;
		
		if ( network.getSchedule() != null ) {
			cronTrigger = new CronTrigger( network.getSchedule().getCronExpression() );
		}
		
	}

	@Override
	public Date nextExecutionTime(TriggerContext ctx) {
		
		Date execTime = null;
		System.out.println( network.getName()  + " next exec: " + cronTrigger.nextExecutionTime(ctx) );
		
		if ( cronTrigger != null )
			execTime = cronTrigger.nextExecutionTime(ctx);
		// Si retorna null entonce 
		
		
		return execTime;
	}

}
