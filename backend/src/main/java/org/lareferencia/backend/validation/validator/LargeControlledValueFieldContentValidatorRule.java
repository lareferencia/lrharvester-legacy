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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "controlledValues" })
public class LargeControlledValueFieldContentValidatorRule extends ControlledValueFieldContentValidatorRule {

	private static final int MAX_PRINTED_LINES = 25;
	private static final String CSV_SEPARATOR = ";";

	@JsonProperty("controlledValuesCSV")
	private String controlledValuesCSV;

	public String getControlledValuesCSV() {
		if (controlledValuesCSV == null)
			return getCSVStringFromControlledValues(this.controlledValues);
		else
			return controlledValuesCSV;
	}

	public void setControlledValuesCSV(String controlledValuesCSV) {
		this.controlledValuesCSV = controlledValuesCSV;
		this.setControlledValuesFromCsvString(controlledValuesCSV);
	}

	public void setControlledValuesFromCsvString(String csvString) {

		System.out.println("\n\nCargando validador: valores controlados desde cadena CSV");

		String[] values = csvString.split(CSV_SEPARATOR);

		for (int i = 0; i < values.length; i++) {

			this.controlledValues.add(values[i]);

			if (i < MAX_PRINTED_LINES)
				System.out.println(values[i]);
			else
				System.out.print(".");
		}

		System.out.println("\nFin Carga validador: valores controlados desde cadena CSV");
	}

	private String getCSVStringFromControlledValues(List<String> controlledList) {
		// TODO: Cambiar por un String Buffer para mejorar performance
		String result = "";

		for (int i = 0; i < controlledList.size(); i++) {

			result += controlledList.get(i);
			if (i < controlledList.size() - 1)
				result += CSV_SEPARATOR;
		}

		return result;
	}

	public void setControlledValuesFileName(String filename) {

		try {

			System.out.println("\n\nCargando validador: valores controlados: " + filename);

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));

			String line = br.readLine();

			int lineNumber = 0;

			while (line != null) {

				this.controlledValues.add(line);

				if (lineNumber++ < MAX_PRINTED_LINES)
					System.out.println(filename + " : " + line);
				else
					System.out.print(".");

				line = br.readLine();
			}

			br.close();

			System.out.println("\nFin Carga validador: valores controlados: " + filename + "\n\n");

		} catch (FileNotFoundException e) {
			System.err.println("!!!!!! No se encontró el archivo de valores controlados:" + filename);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
