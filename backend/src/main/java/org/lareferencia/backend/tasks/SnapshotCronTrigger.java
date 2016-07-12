/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend.tasks;

import java.util.Date;

import org.lareferencia.backend.domain.Network;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

public class SnapshotCronTrigger implements Trigger {

	CronTrigger cronTrigger;
	Network network;

	public SnapshotCronTrigger(Network network) {
		/**
		 * TODO: SnapshotCronTrigger no es una extensión de CronTrigger porque
		 * resta implementar un mecanismo que sincronize nextExecutionTime con
		 * los valores (probablemente cambiantes) de Schedule. Es posible
		 * refrescar network cada vez y cambiar entonces la instancia de
		 * cronTrigger (por eso no es práctica la herencia) , de esa manera
		 * podría modificarse las futuras ejecuciones, mas no la ya programada.
		 */

		this.network = network;

		if (network.getScheduleCronExpression() != null && !network.getScheduleCronExpression().trim().equals("")) {
			try {
				cronTrigger = new CronTrigger(network.getScheduleCronExpression());
			} catch (java.lang.IllegalArgumentException e) {
				// TODO: handle exception
				System.out.println("Problemas con el trigger de:" + network.getName());
			}
		}

	}

	@Override
	public Date nextExecutionTime(TriggerContext ctx) {

		Date execTime = null;

		if (cronTrigger != null) {
			System.out.println(network.getName() + " next exec: " + cronTrigger.nextExecutionTime(ctx));
			execTime = cronTrigger.nextExecutionTime(ctx);
		}
		// Si retorna null entonce

		return execTime;
	}

}
