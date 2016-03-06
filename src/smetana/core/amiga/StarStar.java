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

import smetana.core.UnsupportedC;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;

public class StarStar extends UnsupportedC implements Area {

	private Area area;

	StarStar(Area area) {
		this.area = area;
	}

	public static StarStar array_of_array_of_something_empty(final Class cl, int nb) {
		// if (allocated) {
		// return new StarStar(__array__.malloc(cl, nb));
		// }
		return new StarStar(__array_of_ptr__.malloc_empty(nb));
	}

	public String toString() {
		return "->" + area;
	}

	Area getArea() {
		return area;
	}

	public void swap(int i, int j) {
		((__array_of_ptr__) area).swap(i, j);

	}

	public void memcopyFrom(Area source) {
		StarStar other = (StarStar) source;
		this.area = other.area;
	}

	// public __ptr__ getBracket(int idx) {
	// return ((AreaArray) area).getBracket(idx);
	// }
	//
	public void realloc(int nb) {
		((__array_of_ptr__) area).realloc(nb);
	}

	//
	// // __c__
	// public void setBracket(int idx, Object data) {
	// ((AreaArray) area).setBracket(idx, data);
	// }
	//
	// public __ptr__ plus(int pointerMove) {
	// return ((AreaArray) area).plus(pointerMove);
	// }

	public __ptr__ plus(int pointerMove) {
		return new StarStar(((__array_of_ptr__) area).move(pointerMove));
	}

	public __ptr__ getPtr(String fieldName) {
		return ((__array_of_ptr__) area).asPtr().getPtr(fieldName);
	}

	public __ptr__ getPtr() {
		return (__ptr__) ((__array_of_ptr__) area).getInternal(0);
	}

	public void setPtr(__ptr__ value) {
		((__array_of_ptr__) area).setInternalByIndex(0, (Area) value);
	}

	public void setDouble(String fieldName, double data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int comparePointer(__ptr__ other) {
		return ((__array_of_ptr__) area).comparePointerInternal(((__array_of_ptr__) ((StarStar) other).area));
	}

}
