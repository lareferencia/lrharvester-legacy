/*******************************************************************************
 * Copyright (c) 2013 lmatas.
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
/*
* Copyright 2008 the original author or authors.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/ 

package org.lareferencia.provider.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MetadataFormat
{
   private Log log = LogFactory.getLog(getClass());	
   private String metadataPrefix = null;
   private String schema = null;
   private String metadataNamespace = null;
   
   /* Constructors
   ***************************************************************************/

   public MetadataFormat()
   {
   }

   /* Accessors/Mutators
   ***************************************************************************/

   public void setMetadataPrefix(final String metadataPrefix)
   {
      this.metadataPrefix = metadataPrefix;
   }

   public String getMetadataPrefix()
   {
      return this.metadataPrefix;
   }

   public void setSchema(final String schema)
   {
      this.schema = schema;
   }

   public String getSchema()
   {
      return this.schema;
   }

   public void setMetadataNamespace(final String metadataNamespace)
   {
      this.metadataNamespace = metadataNamespace;
   }

   public String getMetadataNamespace()
   {
      return this.metadataNamespace;
   }

   /* Local Methods
   ***************************************************************************/
}
