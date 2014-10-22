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
package org.lareferencia.backend.transformer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.w3c.dom.Node;

public class TranslateContentFieldTransformer extends FieldTransformer {
	
	private static final int MAX_PRINTED_LINES = 30;
	
	@Getter @Setter
	protected boolean deleteDuplicateOccurences = false;
	
	@Getter @Setter
	protected boolean deleteInvalidOccurences = false;
		
	@Getter
	Map<String,String> translationMap;
	
	public TranslateContentFieldTransformer() {
		this.translationMap = new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
		this.applyIfValid = false;
	}
	
	public void setTranslationMapFileName(String filename) {
		
	    try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));

	    	System.out.println("\n\nCargando transformador: reemplazo de valores: " + filename);

			
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        int lineNumber = 1;
	        while (line != null) {
	        	
	        	String[] parsedLine = line.split("\\t"); 
	        	
	        	if ( parsedLine.length != 2)
	        		throw new Exception("Formato de archivo " + filename + " incorrecto!! linea: " + lineNumber);
	        	
	        	this.translationMap.put( parsedLine[0], parsedLine[1]);
	        	
	        	if ( lineNumber < MAX_PRINTED_LINES )
	        		System.out.println(filename + " : " + line);
	        	else
	        		System.out.print(".");
	            
	        	line = br.readLine();
	            lineNumber++;
	        }
	        
	    	System.out.println("\n\nFin carga transformador: reemplazo de valores: " + filename);

	        br.close();
	  	  
	    }
	    catch  ( FileNotFoundException e ) {
	    	System.err.println("!!!!!! No se encontró el archivo de valores controlados:" + filename);	   
	    	e.printStackTrace();
	    } 
	    catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
	}
	
	@Override
	public boolean transform(OAIRecordMetadata metadata) {
		
		boolean wasTransformed = false;
		boolean isFieldValid = false;
		
		// ciclo de reemplazo y revalidación
		for (Node node: metadata.getFieldNodes( this.getFieldName() ) ) {
			
			String occr = node.getFirstChild().getNodeValue();
			
			// Si encuentra el valor realiza la trasformación y registra que ocurrió
			if ( translationMap.containsKey(occr) ) {
				String translatedOccr = translationMap.get(occr); //reemplazo del valor
				wasTransformed |= !occr.equals(translatedOccr);
				
				node.getFirstChild().setNodeValue(translatedOccr);
				occr = translatedOccr;
			}	
			
			// luego de reemplazar evalua nuevamente la validez y la registra 
			boolean isOccurenceValid = this.getValidationRule().validate(occr).isValid();
			isFieldValid |= isOccurenceValid;

			// Si no es válida la ocurrencia y está indicado remover ocurrencias inválidas
			if (!isOccurenceValid && this.isDeleteInvalidOccurences()) {
				Node fieldNode = node.getParentNode();
				fieldNode.removeChild(node);
			}
			
		}
		
		
		// creación del campo con el valor por defecto en caso de no haber reemplazos válidos, solo cuando hay valor por defecto declarado
	    if ( !isFieldValid && this.getDefaultFieldValue() != null ) {
	    	metadata.addFieldOcurrence(fieldName, defaultFieldValue);
	    	wasTransformed = true;
	    }
		
		
		if ( this.isDeleteDuplicateOccurences() ) {
			
			Set<String> values =  new TreeSet<String>(CaseInsensitiveComparator.INSTANCE);
				
			// ciclo de detección de duplicados
			for (Node node: metadata.getFieldNodes( this.getFieldName() ) ) {
				
				String occr = node.getFirstChild().getNodeValue();
				
				// Si encuentra repetido elimina el nodo
				if ( values.contains(occr) ) {
					Node fieldNode = node.getParentNode();
					fieldNode.removeChild(node);
				}
				
				values.add(occr);
			}	
		}
		
		return wasTransformed;
	}
	
	
	public void setTranslationMap(Map<String, String> translationMap) {
		this.translationMap = new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
		this.translationMap.putAll(translationMap);
	}


	static class CaseInsensitiveComparator implements Comparator<String> {
	    public static final CaseInsensitiveComparator INSTANCE = 
	           new CaseInsensitiveComparator();

	    public int compare(String first, String second) {
	         // some null checks
	         return first.compareToIgnoreCase(second);
	    }
	}

}
