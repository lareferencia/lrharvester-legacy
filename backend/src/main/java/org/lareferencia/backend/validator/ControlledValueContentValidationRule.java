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
package org.lareferencia.backend.validator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ControlledValueContentValidationRule extends BaseContentValidationRule {
	
	private static final int MAX_PRINTED_LINES = 25;
	
	public static String RULE_ID="ControlledValue";
	
	private List<String> controlledValues;
	
	public ControlledValueContentValidationRule() {
		super();
		controlledValues = new ArrayList<String>();
	}

	public ControlledValueContentValidationRule(List<String> controlledValues) {
		super();
		this.controlledValues = controlledValues;
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
	
	public ControlledValueContentValidationRule(List<String> controlledValues, String quantifier) {
		super();
		this.controlledValues = controlledValues;
		this.quantifier = quantifier;
	}

	@Override
	public ContentValidationResult validate(String content) {
		
		ContentValidationResult result = new ContentValidationResult();
		//result.setRuleID(RULE_ID);
		
		// Se recorta el diccionario si resulta muy grande, enumerando solo los primeros 255 chars
		//String expected = controlledValues.toString();
		//result.setExpectedValue( expected.length() > MAX_EXPECTED_LENGTH ? expected.substring(0, MAX_EXPECTED_LENGTH) : expected ) ;
		
		result.setRuleName(this.name);
		
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue( content.length() > MAX_EXPECTED_LENGTH ? content.substring(0, MAX_EXPECTED_LENGTH) : content);
			result.setValid( this.controlledValues.contains(content) );
		}
		
		/**
		System.out.println("\n\n");
		if ( this.controlledValues.size() < 20 )
			for (String contr:this.controlledValues)
				System.out.println(contr + " --> " + contr.equals(content) );
		
		System.out.println( ".." + content + " :: " + this.controlledValues.contains(content) );
		**/
		
		
		return result;
	}


	
}
