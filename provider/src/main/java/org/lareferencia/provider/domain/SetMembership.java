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

public class SetMembership
{
   private Log log = LogFactory.getLog(getClass());	
   private String setSpec = null;
   private String setName = null;
   
   /* Constructors
   ***************************************************************************/

   public SetMembership()
   {
   }

   /* Accessors/Mutators
   ***************************************************************************/

   public void setSetSpec(final String setSpec)
   {
      this.setSpec = setSpec;
   }

   public String getSetSpec()
   {
      return this.setSpec;
   }

   public void setSetName(final String setName)
   {
      this.setName = setName;
   }

   public String getSetName()
   {
      return this.setName;
   }

  /* Local Methods
   ***************************************************************************/
}