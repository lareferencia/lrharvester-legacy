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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Record
{
   private Log log = LogFactory.getLog(getClass());	

   private List<String> sets = new ArrayList<String>();
   private String date = null;
   private String identifier = null;
   private boolean isDeleted = false;
   private String metadata = null;
   
   /* Constructors
   ***************************************************************************/

   public Record()
   {
   }

   /* Accessors/Mutators
   ***************************************************************************/
   
   public String getIdentifier()
   {
      return this.identifier;
   }

   public void setIdentifier(final String identifier)
   {
      this.identifier = identifier;
   }

   public String getDate()
   {
      return this.date;
   }

   public void setDate(final String date)
   {
      this.date = date;
   }

   public List<String> getSets()
   {
      return this.sets;
   }
   
   public void setSets(final Collection<String> sets)
   {
      this.sets.clear();
      this.sets.addAll(sets);
   }
   
   public void addSet(final String name)
   {
      this.sets.add(name);
   }
   
   public void removeSet(final String name)
   {
      this.sets.remove(name);
   }
   
   public void clearSets()
   {
      this.sets.clear();
   }
   
   public boolean getDeleted()
   {
      return this.isDeleted;
   }
   
   public void setDeleted(final boolean isDeleted)
   {
      this.isDeleted = isDeleted;
   }
   
   public String getMetadata()
   {
      return this.metadata;
   }
   
   public void setMetadata(final String metadata)
   {
      if(metadata == null)
         throw new IllegalArgumentException("'Record.setMetadata()' expects a non-null array of bytes.");

      this.metadata = metadata;
   }

   public String toString()
   {
      final ToStringBuilder builder = new ToStringBuilder(this).
         append("identifier", identifier).
         append("deleted", isDeleted);
      
      if(date != null)
         builder.append("date", date);

      if(sets != null && sets.size() > 0)
         builder.append("sets", sets);
         
      return builder.toString();
   }
}
