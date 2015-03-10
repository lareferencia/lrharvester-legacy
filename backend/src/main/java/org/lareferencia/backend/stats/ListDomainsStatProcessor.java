package org.lareferencia.backend.stats;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.RepositoryNameHelper;
import org.lareferencia.backend.util.datatable.ColumnDescription;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.util.datatable.TypeMismatchException;
import org.lareferencia.backend.util.datatable.ValueType;
import org.lareferencia.backend.validator.FieldValidationResult;
import org.lareferencia.backend.validator.ValidationResult;


public class ListDomainsStatProcessor extends BaseStatProcessor {
	
	private static RepositoryNameHelper RNHelper = new RepositoryNameHelper();
	
	private Map<String, Integer> domainCountMap;
	
	@Getter
	@Setter
	private String field = "dc:identifier";
	
	public ListDomainsStatProcessor() {
		super();
		domainCountMap = new HashMap<String, Integer>();
	}

	@Override
	public void addObservation(OAIRecordMetadata metadata, ValidationResult validationResult) {
		
		for (String url: metadata.getFieldOcurrences(this.field) ) {
			
			String domainName = RNHelper.detectRepositoryDomain(url);
			
			if ( domainCountMap.get(domainName) == null ) 
				domainCountMap.put(domainName, 1);
			else 
				domainCountMap.put(domainName, domainCountMap.get(domainName) + 1 );
			
		}
	}

	@Override
	public DataTable getStats() {
		
		DataTable resultTable = new DataTable();
		
		// Construye las columnas de la tablas de resultados
		resultTable.addColumn( new ColumnDescription("C1", ValueType.TEXT , "Domain") );
		resultTable.addColumn( new ColumnDescription("C2", ValueType.NUMBER , "Count") );

		// Para cada campo
		for (Map.Entry<String, Integer> entry: domainCountMap.entrySet()) {			
			
			// Agrega una fila con los resultados de los conteos
			try {
				resultTable.addRowFromValues( entry.getKey(), entry.getValue() );
				
			} catch (TypeMismatchException e) {
				e.printStackTrace();
				System.err.println("Error en los tipos de columnas al contruir DataTable StatProcessor - Domain");
			}			
		}
		
		return resultTable;
	}
	
}
