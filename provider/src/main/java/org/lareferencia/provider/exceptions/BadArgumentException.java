/*
* Derived from code included in the Fedora Repository System:
*
* <p>Copyright &copy; 2008 Fedora Commons, Inc.<br />
* <p>Copyright &copy; 2002-2007 The Rector and Visitors of the University of 
* Virginia and Cornell University<br /> 
* All rights reserved.</p>
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

package org.lareferencia.provider.exceptions;

public class BadArgumentException extends ProtocolException
{
   public BadArgumentException()
   {
      super("The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax.");
   }

   public BadArgumentException(final String message)
   {
      super(message);
   }

   public String getCode()
   {
      return "badArgument";
   }
}