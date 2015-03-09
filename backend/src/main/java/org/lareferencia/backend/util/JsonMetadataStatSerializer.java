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
package org.lareferencia.backend.util;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lareferencia.backend.domain.NetworkSnapshotMetadataStat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


@Component
public class JsonMetadataStatSerializer extends JsonSerializer<NetworkSnapshotMetadataStat>{

   @Override
   public void serialize(NetworkSnapshotMetadataStat metadataStat, JsonGenerator jgen, SerializerProvider provider)
           throws IOException, JsonProcessingException {
	   
	   
	  jgen.writeStartObject();
	  jgen.writeStringField("statID", metadataStat.getStatID() );
	  jgen.writeStringField("jsonData", metadataStat.getJSONString());
	  jgen.writeEndObject();
	   
   }



}
