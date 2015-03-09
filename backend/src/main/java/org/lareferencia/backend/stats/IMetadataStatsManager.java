/*******************************************************************************
 * Copyright (c) 2015 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 * 
 ******************************************************************************/
package org.lareferencia.backend.stats;

import java.util.List;
import java.util.Map;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.validator.ValidationResult;

public interface IMetadataStatsManager  {
	
	// agrega una nueva observación 
	public void addMetadataObservation(OAIRecordMetadata metadata, ValidationResult validationResult);
		
	// agrega / lista los procesadores de estadísticas de los metadatsos originales y post transformación
	public List<IMetadataStatProcessor> getMetadataStatProcessors();
	public void setMetadataStatProcessors( List<IMetadataStatProcessor> procesors );
	
	// obtiene los resultados
	public Map<String, DataTable> getStats();
	
}
