package org.lareferencia.backend.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.ValidationType;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.FieldValidationResult;
import org.lareferencia.backend.validator.ValidationResult;

public class StatsManager {
	
	/** TODO
	 *  Esta es la clase que centralizará el cálculo de estadísticas por red, de momento es muy sencilla y 
	 *  estática, acotada a las necesidades de dignóstico planificadas para el milestone1
	 */

	
	Map<Long, Map<String,Integer>> validationMap;
	List<String> validationMapKeys;
	
	Map<Long, Map<String,Integer>> rejectedByFieldPreMap;
	Map<Long, Map<String,Integer>> rejectedByFieldPosMap;
	List<String> rejectedByFieldMapKeys;
	
	List<String> acceptedByDriverTypeMapKeys;
	Map<Long, Map<String,Integer>> acceptedByDriverTypePreMap;
	Map<Long, Map<String,Integer>> acceptedByDriverTypePosMap;
	
	List<String> occurrencesByFieldMapKeys;
	Map<Long, Map<String,Integer>> occurrencesByFieldMap;


	Map<Long, Map<String, Map<String, Integer>>> examplesByField;
	
	Map<Long, String> networkNamesBySnapId;
	
	public StatsManager(Map<Long,String> networkNamesBySnapId) {
		
		this.networkNamesBySnapId = networkNamesBySnapId;
		
		validationMap = new HashMap<Long, Map<String,Integer>>();
		validationMapKeys = new ArrayList<String>( Arrays.asList( "#total", "#valid_pre", "#valid_pos" ) );
		
		rejectedByFieldPreMap = new HashMap<Long, Map<String,Integer>>();
		rejectedByFieldPosMap = new HashMap<Long, Map<String,Integer>>();
		rejectedByFieldMapKeys = new ArrayList<String>( Arrays.asList( "dc:type", "dc:identifier", "dc:title", "dc:creator", "dc:date" ) );

		acceptedByDriverTypePosMap = new HashMap<Long, Map<String,Integer>>();
		acceptedByDriverTypePreMap = new HashMap<Long, Map<String,Integer>>();
		acceptedByDriverTypeMapKeys = new ArrayList<String>( Arrays.asList("info:eu-repo/semantics/article","info:eu-repo/semantics/masterThesis","info:eu-repo/semantics/doctoralThesis","info:eu-repo/semantics/report"));
		
		occurrencesByFieldMapKeys = new ArrayList<String>( Arrays.asList(
				"dc:contributor",
				"dc:coverage",
				"dc:creator",
				"dc:date",
				"dc:description",
				"dc:format",
				"dc:identifier",
				"dc:language",
				"dc:publisher",
				"dc:relation",
				"dc:rights",
				"dc:source",
				"dc:subject",
				"dc:title",
				"dc:type"
		));
		occurrencesByFieldMap = new HashMap<Long, Map<String,Integer>>();
		
		examplesByField = new HashMap<Long, Map<String,Map<String,Integer>>>();
		
		for (Long snapId:networkNamesBySnapId.keySet()) {
			initMap(snapId, validationMap, validationMapKeys);
			
			initMap(snapId, rejectedByFieldPreMap, rejectedByFieldMapKeys);
			initMap(snapId, rejectedByFieldPosMap, rejectedByFieldMapKeys);
			
			initMap(snapId, acceptedByDriverTypePreMap, acceptedByDriverTypeMapKeys);
			initMap(snapId, acceptedByDriverTypePosMap, acceptedByDriverTypeMapKeys);
			
			initMap(snapId, occurrencesByFieldMap, occurrencesByFieldMapKeys);
			
			/*
			Map<String,Map<String,Integer>> aux = new HashMap<String, Map<String,Integer>>();
			for (String key: rejectedByFieldMapKeys) {
				Map<String,Integer> inner = new HashMap<String, Integer>();
				aux.put(key,inner);
			}
			examplesByField.put(snapId, aux);
			*/
		}	
	}
	
	private void initMap(Long snapId, Map<Long, Map<String,Integer>> map, List<String> keys) {
		
		Map<String,Integer> aux = new HashMap<String, Integer>();
		for (String key:keys) {
			aux.put(key,0);
		}
		map.put(snapId, aux);
	}

	public void addToStats(OAIRecord record, HarvesterRecord hrecord, ValidationResult result, ValidationType type) {
		
		Long snapId = record.getSnapshot().getId();
		
		switch (type) {
		case PREVALIDATION:

			Integer nvalue = validationMap.get(snapId).get("#total") + 1;
			validationMap.get(snapId).put("#total", nvalue);
			
			if  ( result.isValid() ) {
				// count as valid
				validationMap.get(snapId).put("#valid_pre", validationMap.get(snapId).get("#valid_pre") + 1);
				
				// accepted by driver
				ContentValidationResult driverResult = null;
				for ( ContentValidationResult cr: result.getFieldResults().get("dc:type").getContentResults() ) {
					if ( acceptedByDriverTypeMapKeys.contains( cr.getReceivedValue() ) )
						driverResult = cr;
				}
				acceptedByDriverTypePreMap.get(snapId).put(driverResult.getReceivedValue(), acceptedByDriverTypePreMap.get(snapId).get(driverResult.getReceivedValue()) + 1);

				
			}
			else {
				// rejected by type
				for ( String field : rejectedByFieldMapKeys) {
					if ( !result.getFieldResults().get(field).isValid() )
						rejectedByFieldPreMap.get(snapId).put(field, rejectedByFieldPreMap.get(snapId).get(field) + 1);
				}	
			}
		
			break;

		case POSTVALIDATION:
			
			if  ( result.isValid() ) {
				
				// count as valid
				validationMap.get(snapId).put("#valid_pos", validationMap.get(snapId).get("#valid_pos") + 1);
				
				// accepted by driver
				ContentValidationResult driverResult = null;
				for ( ContentValidationResult cr: result.getFieldResults().get("dc:type").getContentResults() ) {
					if ( acceptedByDriverTypeMapKeys.contains( cr.getReceivedValue() ) )
						driverResult = cr;
				}
				acceptedByDriverTypePosMap.get(snapId).put(driverResult.getReceivedValue(), acceptedByDriverTypePosMap.get(snapId).get(driverResult.getReceivedValue()) + 1);

				// occurrences by field
				for (String field:occurrencesByFieldMapKeys) {
					occurrencesByFieldMap.get(snapId).put(field, occurrencesByFieldMap.get(snapId).get(field) + hrecord.getFieldOcurrences(field).size() );
				}
				
			}
			else {
				
				// rejected by type
				for ( String field : rejectedByFieldMapKeys) {
					if ( !result.getFieldResults().get(field).isValid() )
						rejectedByFieldPosMap.get(snapId).put(field, rejectedByFieldPosMap.get(snapId).get(field) + 1);
				}
				
			}
		
			
			break;
	
		}
		
		
	}
	
	
	private String printMap(Map<Long, Map<String,Integer>> map, List<String> keys) {
		
		String result = "";
		
		result += "id\tname\t\t";
		for (String key: keys ) {
			result += key + "\t";
		}
		result += "\n";			
		
		for (Long snapId : map.keySet() ) {
			result += snapId + "\t" + networkNamesBySnapId.get(snapId) + "\t\t";
			
			for (String key: keys ) {
				result += map.get(snapId).get(key) + "\t";
			}
			result += "\n";			
		}
		
		return result;
	}


	@Override
	public String toString() {
		
		String result = "";
		
		result += "Registros Totales por Red Nacional\n";
		result += printMap(validationMap, validationMapKeys);
		result += "\n\n";
		result += "Registros rechazados por Red Nacional S/campo obligatorio (Prevalidación)\n";
		result += printMap(rejectedByFieldPreMap, rejectedByFieldMapKeys);
		result += "\n\n";
		result += "Registros rechazados por Red Nacional S/campo obligatorio (Postvalidación)\n";
		result += printMap(rejectedByFieldPosMap, rejectedByFieldMapKeys);
		result += "\n\n";
		result += "Registros aceptados por Red Nacional S/tipo driver (Prevalidación)\n";
		result += printMap(acceptedByDriverTypePreMap, acceptedByDriverTypeMapKeys);
		result += "\n\n";
		result += "Registros aceptados por Red Nacional S/tipo driver (Postvalidación)\n";
		result += printMap(acceptedByDriverTypePosMap, acceptedByDriverTypeMapKeys);
		result += "\n\n";
		result += "Cantidad de ocurrencias de cada metadato por Red Nacional (válidos Postvalidación)\n";
		result += printMap(occurrencesByFieldMap, occurrencesByFieldMapKeys);
		
		return result;
	}
	
	
	

}
