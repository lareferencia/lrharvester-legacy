/*
* Derived from code included in the Fedora Repository System:
*
* <p>Copyright &copy; 2008 Fedora Commons, Inc.<br />
* <p>Copyright &copy; 2002-2007 The Rector and Visitors of the University of 
* Virginia and Cornell University<br /> 
* All rights reserved.</p>
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

public class NoRecordsMatchException extends ProtocolException
{
   private static final String USER_MESSAGE = "The combination of the values of the from, until, set and metadataPrefix arguments results in an empty list.";

   public NoRecordsMatchException(final String message)
   {
      super(message + " " + USER_MESSAGE);
   }

   public NoRecordsMatchException()
   {
      super(USER_MESSAGE);
   }

   public String getCode()
   {
      return "noRecordsMatch";
   }
}