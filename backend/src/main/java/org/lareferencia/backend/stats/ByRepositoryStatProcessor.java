package org.lareferencia.backend.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.stats.BaseCountByFieldStatProcessor.DataIndex;
import org.lareferencia.backend.util.datatable.ColumnDescription;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.util.datatable.TypeMismatchException;
import org.lareferencia.backend.util.datatable.ValueType;
import org.lareferencia.backend.validator.ValidationResult;

import lombok.Getter;


public class ByRepositoryStatProcessor extends BaseStatProcessor {
	
	
	String repositoryNameField = "dc:source";
	String repositoryNamePrefix = "reponame:";
	
	
	protected Map<String, Integer[]> countMap;
	
	protected enum DataIndex { ALL, VALID, TRANSFORMED }
	
	
	protected void updateCounter(String key, DataIndex index) {
		
		Integer[] data = countMap.get(key);
		
		if ( data == null ) {
			Integer[] zero_data = {0,0,0};
			countMap.put(key,zero_data);
			data = zero_data;
		}
		
		data[index.ordinal()] = data[index.ordinal()] + 1;
	}

	@Override
	public void addObservation(OAIRecordMetadata metadata, ValidationResult validationResult, Boolean wasTransformed) {

		for (String occr: metadata.getFieldOcurrences(repositoryNameField) ) {
			
			if ( occr.startsWith(repositoryNameField) ) {
				
				String repoName = occr.substring( repositoryNameField.length() );
				
				updateCounter(repoName, DataIndex.ALL);
				
				if (validationResult.isValid())
					updateCounter(repoName, DataIndex.VALID);
				
				if (wasTransformed)
					updateCounter(repoName, DataIndex.TRANSFORMED);	
			}
		}
	}

	@Override
	public DataTable getStats() {
		
		DataTable resultTable = new DataTable();
		
		// Construye las columnas de la tablas de resultados
		resultTable.addColumn( new ColumnDescription("C1", ValueType.TEXT , "Field") );
		resultTable.addColumn( new ColumnDescription("C2", ValueType.NUMBER , "Total") );
		resultTable.addColumn( new ColumnDescription("C3", ValueType.NUMBER , "Valid") );
		resultTable.addColumn( new ColumnDescription("C4", ValueType.NUMBER , "Trasformed") );

		// Para cada campo
		for ( String repository: countMap.keySet() ) {			
			
			// Obtiene el registros de conteos para ese campo
			Integer[] counts = countMap.get(repository);
			
			// Calcula los porcentajes relativos
			Integer total = counts[DataIndex.ALL.ordinal()];
			Integer valid = counts[DataIndex.VALID.ordinal()];
			Integer transformed = counts[DataIndex.TRANSFORMED.ordinal()];

			
			
			// Agrega una fila con los resultados de los conteos
			try {
				resultTable.addRowFromValues(repository, total, valid, transformed);
				
			} catch (TypeMismatchException e) {
				e.printStackTrace();
				System.err.println("Error en los tipos de columnas al contruir DataTable StatProcessor");
			}			
		}
		
		return resultTable;
	}
}
