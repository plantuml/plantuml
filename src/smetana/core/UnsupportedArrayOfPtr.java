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
 * (C) Copyright 2009-2020, Arnaud Roques
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

package smetana.core;

import smetana.core.amiga.Area;
import smetana.core.amiga.StarStruct;

public class UnsupportedArrayOfPtr implements __array_of_ptr__ {

	public __ptr__ unsupported() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int comparePointer(__ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int minus(__ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int getInt() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setInt(int value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public double getDouble() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setDouble(double value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ getPtr() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setPtr(__ptr__ value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public boolean isSameThan(StarStruct other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public Class getRealClass() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __struct__ getStruct() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public Area getArea(String name) {
		throw new UnsupportedOperationException(name + " " + getClass().toString());
	}

	public String getUID36() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public String getDebug(String fieldName) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setInt(String fieldName, int data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setDouble(String fieldName, double data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setStruct(String fieldName, __struct__ newData) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void copyDataFrom(__struct__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setStruct(__struct__ value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void copyDataFrom(__ptr__ arg) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ castTo(Class dest) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public Object addVirtualBytes(int virtualBytes) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void swap(int i, int j) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void realloc(int nb) {
		throw new UnsupportedOperationException(getClass().toString());
	}
	
	public void realloc(size_t nb) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ asPtr() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int comparePointerInternal(__array_of_ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __array_of_ptr__ move(int delta) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public Area getInternal(int idx) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setInternalByIndex(int idx, Area value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __array_of_ptr__ plus(int delta) {
		throw new UnsupportedOperationException(getClass().toString());
	}


}
