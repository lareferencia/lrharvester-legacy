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
package org.lareferencia.backend.indexer;

import lombok.Setter;

import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.repositories.NetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class IndexerWorker implements Runnable {

	@Setter
	private Long networkID;

	@Setter
	private boolean deleteNetworkWithoutReindexing = false;

	@Autowired
	private NetworkRepository networkRepository;

	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;

	@Setter
	IIndexer indexer;

	public IndexerWorker() {
	};

	@Override
	public void run() {

		if (indexer == null) {
			System.out.println("Indexer worker llamado con indexer null");
		} else {

			// TODO: Generar excepciones por network null o incorrecto
			Network network = networkRepository.findOne(this.networkID);
			NetworkSnapshot snapshot = null;

			if (network == null)
				System.out.println("Indexer Worker - El id de red es inválido");

			System.out.println("Borrando red del índice - " + network.getName() + "  - IndexerWorker");

			if (!deleteNetworkWithoutReindexing) { // Si no es un borrado sin
													// reindexación

				// Se recupera el LGK Snapshot
				snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(networkID);

				if (snapshot == null)
					System.out.println("Indexer Worker - La Red no tiene LGK Snapshot es válido");

				System.out.println("Indexando LGK Snapshot RED: - " + network.getName() + "  - IndexerWorker");
			}

			indexer.index(network, snapshot, deleteNetworkWithoutReindexing);

		}
	}

}
