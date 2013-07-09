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
		
		if ( network.getScheduleCronExpression() != null && !network.getScheduleCronExpression().trim().equals("")) {
			try {
				cronTrigger = new CronTrigger( network.getScheduleCronExpression() );
			} catch (java.lang.IllegalArgumentException e) {
				// TODO: handle exception
				System.out.println("Problemas con el trigger de:" + network.getName() );
			}
		}
		
	}

	@Override
	public Date nextExecutionTime(TriggerContext ctx) {
		
		Date execTime = null;
		
		if ( cronTrigger != null ) {
			System.out.println( network.getName()  + " next exec: " + cronTrigger.nextExecutionTime(ctx) );
			execTime = cronTrigger.nextExecutionTime(ctx);
		}
		// Si retorna null entonce 
		
		
		return execTime;
	}

}
