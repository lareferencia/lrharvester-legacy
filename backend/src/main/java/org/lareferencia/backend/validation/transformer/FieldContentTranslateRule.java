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
package org.lareferencia.backend.validation.transformer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validation.validator.ContentValidatorResult;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FieldContentTranslateRule extends AbstractTransformerRule {

	@Getter
	@JsonIgnore
	Map<String, String> translationMap;

	@Getter
	List<Translation> translationArray;

	@Setter
	@Getter
	String testFieldName;

	@Setter
	@Getter
	String writeFieldName;

	@Setter
	@Getter
	Boolean replaceOccurrence = true;

	@Setter
	@Getter
	Boolean testValueAsPrefix = false;

	public FieldContentTranslateRule() {
		this.translationMap = new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
	}

	public void setTranslationArray(List<Translation> list) {
		this.translationArray = list;

		for (Translation t : list) {
			this.translationMap.put(t.search, t.replace);
		}

		System.out.println(list);
	}

	public void setTranslationMapFileName(String filename) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));

			String line = br.readLine();

			int lineNumber = 1;
			while (line != null) {

				String[] parsedLine = line.split("\\t");

				if (parsedLine.length != 2)
					throw new Exception("Formato de archivo " + filename + " incorrecto!! linea: " + lineNumber);

				this.translationMap.put(parsedLine[0], parsedLine[1]);

				System.out.println("cargado: " + line);
				line = br.readLine();
				lineNumber++;
			}

			br.close();

		} catch (FileNotFoundException e) {
			System.err.println("!!!!!! No se encontró el archivo de valores controlados:" + filename);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public boolean transform(OAIRecord record) {

		OAIRecordMetadata metadata = record.getMetadata();
		boolean wasTransformed = false;

		// recorre las ocurrencias del campo de test
		for (Node node : metadata.getFieldNodes(testFieldName)) {

			String occr = node.getFirstChild().getNodeValue();

			// Busca el valor completo, no el prefijo
			if (!testValueAsPrefix) {

				// Si encuentra el valor realiza la trasformación y registra que
				// ocurrió
				if (translationMap.containsKey(occr)) {

					String translatedOccr = translationMap.get(occr); // se
																		// obtiene
																		// el
																		// valor
																		// de
																		// reemplazo
					wasTransformed |= !occr.equals(translatedOccr); // registra
																	// que será
																	// transformado

					if (replaceOccurrence) { // Si esta marcao el reemplazo del
												// valor
						// borra la ocurrencia del test node
						metadata.removeFieldNode(node);
					}

					metadata.addFieldOcurrence(writeFieldName, translatedOccr);
				}

			} else { // Busca el prefijo

				Boolean found = false;
				// recorre los valores del diccionarrio de reemplazo
				for (String testValue : translationMap.keySet()) {

					// si el valor del diccionario de reemplazo es prefijo de la
					// ocurrencia
					if (!found && occr.startsWith(testValue)) {

						wasTransformed = true;
						String translatedOccr = translationMap.get(testValue); // se
																				// obtiene
																				// el
																				// valor
																				// de
																				// reemplazo

						if (replaceOccurrence) { // Si esta marcao el reemplazo
													// del valor
							// borra la ocurrencia del test node
							metadata.removeFieldNode(node);
						}

						metadata.addFieldOcurrence(writeFieldName, translatedOccr);

					}
				}
			}
		}

		return wasTransformed;
	}

	public void setTranslationMap(Map<String, String> translationMap) {
		this.translationMap = new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
		this.translationMap.putAll(translationMap);
	}

	static class CaseInsensitiveComparator implements Comparator<String> {
		public static final CaseInsensitiveComparator INSTANCE = new CaseInsensitiveComparator();

		public int compare(String first, String second) {
			// some null checks
			return first.compareToIgnoreCase(second);
		}
	}

}
