package org.lareferencia.backend.stats;

import java.util.Map;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.datatable.ColumnDescription;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.util.datatable.TypeMismatchException;
import org.lareferencia.backend.util.datatable.ValueType;
import org.lareferencia.backend.validation.validator.ValidationResult;
import org.lareferencia.backend.validation.validator.ValidationRuleResult;


public class RegisterCountByFieldStatProcessor extends BaseCountByFieldStatProcessor {

	@Override
	public void addObservation(OAIRecordMetadata metadata, ValidationResult validationResult,  Boolean wasTransformed) {
		
		// Obtiene el resultado de validacions de cada campo
		Map<String, ValidationRuleResult> fvResultMap = validationResult.getFieldResults();

		// Para cada campo 
		for (String fieldName : fieldNames) {
			
			// Obtiene los datos del campo, un array de posiciones indicadas por DataIndex
			Integer[] tmp = countMap.get(fieldName);
			
			// Incremento del contador correspondiente a registros procesados
			incrementFieldCounter(fieldName, DataIndex.ALL);
			
			// Si corresponde Incrementa el correspondiente a conteo de registros con campos no vacíos
			if ( metadata.getFieldOcurrences(fieldName).size() > 0 )
				incrementFieldCounter(fieldName, DataIndex.NONEMPTY);

			// Si corresponde Incrementa el correspondiente a conteo de registros con campos no válidos
			if ( fvResultMap.get(fieldName) != null && fvResultMap.get(fieldName).isValid() )
				incrementFieldCounter(fieldName, DataIndex.VALID);				
		}	
	}

	@Override
	public DataTable getStats() {
		
		DataTable resultTable = new DataTable();
		
		// Construye las columnas de la tablas de resultados
		resultTable.addColumn( new ColumnDescription("C1", ValueType.TEXT , "Field") );
		resultTable.addColumn( new ColumnDescription("C2", ValueType.NUMBER , "Total") );
		resultTable.addColumn( new ColumnDescription("C3", ValueType.NUMBER , "NonEmpty") );
		resultTable.addColumn( new ColumnDescription("C4", ValueType.NUMBER , "Valid") );
		resultTable.addColumn( new ColumnDescription("C5", ValueType.TEXT , "NonEmptyOverTotal") );
		resultTable.addColumn( new ColumnDescription("C6", ValueType.TEXT , "ValidOverTotal") );

		
		// Para cada campo
		for (String fieldName: fieldNames) {			
			
			// Obtiene el registros de conteos para ese campo
			Integer[] counts = countMap.get(fieldName);
			
			// Calcula los porcentajes relativos
			Integer total = counts[DataIndex.ALL.ordinal()];
			Integer nonempty = counts[DataIndex.NONEMPTY.ordinal()];
			Integer valid = counts[DataIndex.VALID.ordinal()];
			Integer noempyOtotal = (int) ((double) nonempty * 100 / total);
			Integer validOtotal =  (int) ((double) valid * 100 / total);

			
			
			// Agrega una fila con los resultados de los conteos
			try {
				resultTable.addRowFromValues(fieldName, total, nonempty, valid, 
														noempyOtotal.toString() + "%",
														validOtotal.toString() + "%" );
				
			} catch (TypeMismatchException e) {
				e.printStackTrace();
				System.err.println("Error en los tipos de columnas al contruir DataTable StatProcessor");
			}			
		}
		
		return resultTable;
	}
	
	

}
