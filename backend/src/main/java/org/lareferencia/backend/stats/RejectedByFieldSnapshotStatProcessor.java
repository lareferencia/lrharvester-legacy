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
package org.lareferencia.backend.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lareferencia.backend.domain.NetworkSnapshotStat;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ValidationResult;

public class RejectedByFieldSnapshotStatProcessor implements IStatProcessor {
	
	static String description = "Cantidad de registros rechazados por cada metadato"; 
	public static final Long ID = 1L;
	public String getDescription() {return description; }
	
	Map<String,Integer> aggregationMap;
	List<String> mapKeys = new ArrayList<String>( Arrays.asList( "dc:type", "dc:identifier", "dc:title", "dc:creator", "dc:date" ) );
	
	public RejectedByFieldSnapshotStatProcessor() {
		super();
		aggregationMap = new HashMap<String, Integer>();
		for (String key:mapKeys) {
			aggregationMap.put(key,0);
		}	
	}
		
	@Override
	public void process(OAIRecordMetadata metadata, ValidationResult valResult) {
		
		if ( !valResult.isValid() ) // solo considera registros rechazados
			for ( String field : mapKeys) {
				if (  !valResult.getFieldResults().get(field).isValid() )
					aggregationMap.put(field, aggregationMap.get(field) + 1) ;
			}
	}
	@Override
	public List<NetworkSnapshotStat> getResults() {
		
		List<NetworkSnapshotStat> result = new ArrayList<NetworkSnapshotStat>(mapKeys.size());
		
		for ( String field : mapKeys) {
			result.add( new NetworkSnapshotStat(ID, field, aggregationMap.get(field)) );
		}
	
		return result; 	
	}

}
