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
package org.lareferencia.backend;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OfflineIndexerBySnapshot {


	@Autowired
	public NationalNetworkRepository nationalNetworkRepository;

	@Autowired
	public OAIRecordRepository oaiRecordRepository;
	
	@Autowired
	public NetworkSnapshotRepository networkSnapshotRepository;

	public OfflineIndexerBySnapshot() {

	}


	public static void main(String[] args) throws TransformerConfigurationException, TransformerFactoryConfigurationError {

		System.out.println("Iniciando ...");
		Logger.getRootLogger().setLevel(Level.OFF);
			
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		OfflineIndexerBySnapshot m = context.getBean("offlineIndexerBySnapshot", OfflineIndexerBySnapshot.class);

		IIndexer indexer = context.getBean("indexer", IIndexer.class);
		
		indexer.index( m.networkSnapshotRepository.findOne( Long.parseLong(args[0])) );
	}

}


