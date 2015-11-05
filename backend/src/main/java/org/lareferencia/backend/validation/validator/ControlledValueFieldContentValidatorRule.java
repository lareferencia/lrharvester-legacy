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
package org.lareferencia.backend.validation.validator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ControlledValueFieldContentValidatorRule extends AbstractValidatorFieldContentRule {
	
	private static final int MAX_PRINTED_LINES = 25;
	private static final int MAX_EXPECTED_LENGTH = 255;
	
	public static String RULE_ID="ControlledValueValidationRule";
	public static String RULE_NAME="ControlledValueValidationRule";	

	@JsonProperty("controlledValues")
	private List<String> controlledValues;
	
	public ControlledValueFieldContentValidatorRule() {
		super();
		this.controlledValues = new ArrayList<String>();
	}
	
	
	@Override
	public FieldContentValidatorResult validate(String content) {
		
		FieldContentValidatorResult result = new FieldContentValidatorResult();
				
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue( content.length() > MAX_EXPECTED_LENGTH ? content.substring(0, MAX_EXPECTED_LENGTH) : content);
			result.setValid( this.controlledValues.contains(content) );
		}
			
		return result;	
	}
	
	
	public void setControlledValuesFromCsvString(String csvString) {
	    	
    	System.out.println("\n\nCargando validador: valores controlados desde cadena CSV");
    	
		String[] values = csvString.split(";;");

	    for(int i=0; i<values.length; i++) {
        	
        	this.controlledValues.add( values[i] );
        	
        	if ( i < MAX_PRINTED_LINES )
        		System.out.println( values[i] );
        	else
        		System.out.print(".");
        }
        	        
    	System.out.println("\nFin Carga validador: valores controlados desde cadena CSV"); 
	}

	public void setControlledValuesFileName(String filename) {
		
	    try {
	    	
	    	System.out.println("\n\nCargando validador: valores controlados: " + filename);
	    	
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
	    	
	        String line = br.readLine();
	        
	        int lineNumber = 0;

	        while (line != null) {
	        	
	        	this.controlledValues.add(line);
	        	
	        	if ( lineNumber++ < MAX_PRINTED_LINES )
	        		System.out.println( filename + " : " + line);
	        	else
	        		System.out.print(".");

	        	
	            line = br.readLine();
	        }
	        
	        br.close();
	        
	    	System.out.println("\nFin Carga validador: valores controlados: " + filename + "\n\n");
	  	  
	    }
	    catch  ( FileNotFoundException e ) {
	    	System.err.println("!!!!!! No se encontró el archivo de valores controlados:" + filename);	   
	    	e.printStackTrace();
	    } 
	    catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String toString() {
		return "ControlledValueContentValidationRule [controlledValues="
				+ controlledValues + ", name=" + name + ", description="
				+ description + ", mandatory=" + mandatory + ", quantifier="
				+ quantifier + "]";
	}




	
	
}
