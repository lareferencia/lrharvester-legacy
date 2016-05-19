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
package org.lareferencia.backend;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.util.datatable.ColumnDescription;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.util.datatable.HtmlRenderer;
import org.lareferencia.backend.util.datatable.JsonRenderer;
import org.lareferencia.backend.util.datatable.TypeMismatchException;
import org.lareferencia.backend.util.datatable.ValueType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsTests {

	@Test()
	public void testDatatable() {

		DataTable data = new DataTable();
		ArrayList<ColumnDescription> cd = new ArrayList<ColumnDescription>();
		cd.add(new ColumnDescription("name", ValueType.TEXT, "Animal name"));
		cd.add(new ColumnDescription("link", ValueType.TEXT,
				"Link to wikipedia"));
		cd.add(new ColumnDescription("population", ValueType.NUMBER,
				"Population size"));
		cd.add(new ColumnDescription("vegeterian", ValueType.BOOLEAN,
				"Vegetarian?"));

		data.addColumns(cd);

		// Fill the data table.
		try {
			data.addRowFromValues("Aye-aye",
					"http://en.wikipedia.org/wiki/Aye-aye", 100, true);
			data.addRowFromValues("Sloth",
					"http://en.wikipedia.org/wiki/Sloth", 300, true);
			data.addRowFromValues("Leopard",
					"http://en.wikipedia.org/wiki/Leopard", 50, false);
			data.addRowFromValues("Tiger",
					"http://en.wikipedia.org/wiki/Tiger", 80, false);
		} catch (TypeMismatchException e) {
			System.out.println("Invalid type!");
		}

		System.out
				.println(JsonRenderer.renderDataTable(data, true, true, true));
		System.out.println(HtmlRenderer.renderDataTable(data, null));
	}

}
