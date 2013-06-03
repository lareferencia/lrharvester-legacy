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

package org.lareferencia.provider.controller;

public class Arguments
{
   private String url = null;
   private String verb = null;
   private String identifier = null;
   private String from = null;
   private String until = null;
   private String metadataPrefix = null;
   private String set = null;
   private String resumptionToken = null;
   
   public Arguments()
   {
   }
   
   public void setVerb(final String verb)
   {
      this.verb = verb;
   }

   public String getVerb()
   {
      return this.verb;
   }

   public void setUrl(final String url)
   {
      this.url = url;
   }

   public String getUrl()
   {
      return this.url;
   }

   public void setIdentifier(final String identifier)
   {
      this.identifier = identifier;
   }

   public String getIdentifier()
   {
      return this.identifier;
   }

   public void setFrom(final String from)
   {
      this.from = from;
   }

   public String getFrom()
   {
      return this.from;
   }

   public void setUntil(final String until)
   {
      this.until = until;
   }

   public String getUntil()
   {
      return this.until;
   }

   public void setMetadataPrefix(final String metadataPrefix)
   {
      this.metadataPrefix = metadataPrefix;
   }

   public String getMetadataPrefix()
   {
      return this.metadataPrefix;
   }

   public void setSet(final String set)
   {
      this.set = set;
   }

   public String getSet()
   {
      return this.set;
   }

   public void setResumptionToken(final String resumptionToken)
   {
      this.resumptionToken = resumptionToken;
   }

   public String getResumptionToken()
   {
      return this.resumptionToken;
   }
}
