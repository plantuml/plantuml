/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program.
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package smetana.core.amiga;

import smetana.core.AllH;
import smetana.core.CString;
import smetana.core.__array_of_cstring__;
import smetana.core.__array_of_integer__;
import smetana.core.__array_of_ptr__;
import smetana.core.__array_of_struct__;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public interface StarStruct extends Area, AllH, InternalData {

	public boolean isSameThan(StarStruct other);

	public Class getRealClass();

	public __struct__ getStruct();

	public String getUID36();

	public String getDebug(String fieldName);

	public void setInt(String fieldName, int data);

	public void setBoolean(String fieldName, boolean data);

	public void setDouble(String fieldName, double data);

	public int getInt(String fieldName);

	public double getDouble(String fieldName);

	public boolean getBoolean(String fieldName);

	public __ptr__ plus(int pointerMove);

	public CString getCString(String fieldName);

	public __array_of_struct__ getArrayOfStruct(String fieldName);

	public __array_of_ptr__ getArrayOfPtr(String fieldName);

	public __array_of_cstring__ getArrayOfCString(String fieldName);

	public __array_of_integer__ getArrayOfInteger(String fieldName);

	public __struct__ getStruct(String fieldName);

	public __ptr__ getPtr(String fieldName);

	public void setStruct(String fieldName, __struct__ newData);

	public void setCString(String fieldName, CString newData);

	public __ptr__ setPtr(String fieldName, __ptr__ newData);

	public void memcopyFrom(Area source);

	public void copyDataFrom(__struct__ other);

	public void setStruct(__struct__ value);

	public void copyDataFrom(__ptr__ arg);

	public Object call(String fieldName, Object... args);

	public __ptr__ castTo(Class dest);

	public Object addVirtualBytes(int virtualBytes);

}
