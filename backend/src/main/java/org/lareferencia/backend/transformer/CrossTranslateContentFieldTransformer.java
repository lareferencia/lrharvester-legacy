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
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.w3c.dom.Node;

public class CrossTranslateContentFieldTransformer extends FieldTransformer {

	@Getter
	Map<String, String> translationMap;

	@Setter
	@Getter
	String testFieldName;

	public CrossTranslateContentFieldTransformer() {
		this.translationMap = new TreeMap<String, String>(
				CaseInsensitiveComparator.INSTANCE);
		this.applyIfValid = false;
	}

	public void setTranslationMapFileName(String filename) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename), "UTF8"));

			String line = br.readLine();

			int lineNumber = 1;
			while (line != null) {

				String[] parsedLine = line.split("\\t");

				if (parsedLine.length != 2)
					throw new Exception("Formato de archivo " + filename
							+ " incorrecto!! linea: " + lineNumber);

				this.translationMap.put(parsedLine[0], parsedLine[1]);

				System.out.println("cargado: " + line);
				line = br.readLine();
				lineNumber++;
			}

			br.close();

		} catch (FileNotFoundException e) {
			System.err
					.println("!!!!!! No se encontró el archivo de valores controlados:"
							+ filename);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public boolean transform(OAIRecordMetadata metadata) {

		ContentValidationResult result;
		boolean found = false;

		// ciclo de reemplazo
		// recorre las ocurrencias del campo de test
		for (Node node : metadata.getFieldNodes(this.getTestFieldName())) {

			String occr = node.getFirstChild().getNodeValue();

			// recorre los valores del diccionarrio de reemplazo
			for (String testValue : translationMap.keySet()) {

				// si el valor del diccionario de reemplazo es prefijo de la
				// ocurrencia
				if (!found && occr.startsWith(testValue)) {

					// agrega la occurrencia en el campo a modificar de acuerdo
					// al valor del diccionario en la clave detectada como
					// prefijo
					metadata.addFieldOcurrence(fieldName, translationMap.get(testValue));

					found = true;
				}
			}
		}

		// creación del campo con el valor por defecto en caso de no haber sido
		// encontrado, solo cuando hay valor por defecto declarado
		if (!found && this.getDefaultFieldValue() != null)
			metadata.addFieldOcurrence(fieldName, defaultFieldValue);

		return found;
	}

	public void setTranslationMap(Map<String, String> translationMap) {
		this.translationMap = new TreeMap<String, String>(
				CaseInsensitiveComparator.INSTANCE);
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
