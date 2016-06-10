/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
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
import smetana.core.UnsupportedC;
import smetana.core.__array_of_ptr__;
import smetana.core.__array_of_struct__;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public class StarArrayOfPtr extends UnsupportedC implements Area, AllH {

	private final __array_of_ptr__ array;

	public StarArrayOfPtr(__array_of_ptr__ array) {
		this.array = array;
	}

	public void realloc(int nb) {
		array.realloc(nb);
	}

	public String getUID36() {
		return array.getUID36();
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException();
	}

	public final __array_of_ptr__ getInternalArray() {
		return array;
	}

	public __ptr__ plus(int pointerMove) {
		return new StarArrayOfPtr(array.move(pointerMove));
	}

	public void setInt(int value) {
		array.setInt(value);
	}

	public int getInt() {
		return array.getInt();
	}

	public __struct__ getStruct() {
		return array.getStruct();
	}

	public void copyDataFrom(__struct__ other) {
		((StarStruct) array.getInternal(0)).copyDataFrom(other);
	}

	public void setPtr(__ptr__ value) {
		array.setPtr(value);
	}

	public int minus(__ptr__ other) {
		StarArrayOfPtr other2 = (StarArrayOfPtr) other;
		int res = array.comparePointerInternal(other2.array);
		return res;
	}

	public void setStruct(__struct__ value) {
		array.setStruct(value);
	}

	public __ptr__ getPtr() {
		return array.getPtr();
	}

	public __ptr__ getPtrForEquality() {
		return array.getStruct().amp();
	}

	public int comparePointer(__ptr__ other) {
		return array.comparePointerInternal(((StarArrayOfPtr) other).array);
	}

	public boolean isSameThan(StarArrayOfPtr other) {
		return array.comparePointerInternal(other.array) == 0;
	}

	// Fieldname
	public void setStruct(String fieldName, __struct__ data) {
		((__ptr__) array.getInternal(0)).setStruct(fieldName, data);
	}

	public __ptr__ setPtr(String fieldName, __ptr__ data) {
		final Area tmp1 = array.getInternal(0);
		if (tmp1 instanceof __struct__) {
			return ((__struct__) tmp1).setPtr(fieldName, data);
		}
		return ((__ptr__) tmp1).setPtr(fieldName, data);
	}

	public void setInt(String fieldName, int data) {
		((__ptr__) array.getInternal(0)).setInt(fieldName, data);
	}

	public __ptr__ getPtr(String fieldName) {
		final Area tmp1 = array.getInternal(0);
		if (tmp1 instanceof __struct__) {
			return ((__struct__) tmp1).getPtr(fieldName);
		}
		return ((__ptr__) tmp1).getPtr(fieldName);
	}

	public __struct__ getStruct(String fieldName) {
		return ((__ptr__) array.getInternal(0)).getStruct(fieldName);
	}

	public void setDouble(String fieldName, double data) {
		((__ptr__) array.getInternal(0)).setDouble(fieldName, data);
	}

	public int getInt(String fieldName) {
		final Area tmp1 = array.getInternal(0);
		if (tmp1 instanceof __struct__) {
			return ((__struct__) tmp1).getInt(fieldName);
		}
		return ((__ptr__) tmp1).getInt(fieldName);
	}

	public __array_of_struct__ getArrayOfStruct(String fieldName) {
		return ((__ptr__) array.getInternal(0)).getArrayOfStruct(fieldName);
	}

	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		return ((__ptr__) array.getInternal(0)).getArrayOfPtr(fieldName);
	}

	public boolean getBoolean(String fieldName) {
		return ((__ptr__) array.getInternal(0)).getBoolean(fieldName);
	}
	
	public void setBoolean(String fieldName, boolean value) {
		((__ptr__) array.getInternal(0)).setBoolean(fieldName, value);
	}


	public __ptr__ castTo(Class dest) {
		return ((__ptr__) array.getInternal(0)).castTo(dest);
	}


	public double getDouble(String fieldName) {
		final Area tmp1 = array.getInternal(0);
		if (tmp1 instanceof __struct__) {
			return ((__struct__) tmp1).getDouble(fieldName);
		}
		return ((__ptr__) tmp1).getDouble(fieldName);
	}

}
