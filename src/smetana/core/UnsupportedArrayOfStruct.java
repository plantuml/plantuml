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

public class UnsupportedArrayOfStruct {

	public String getUID36() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void swap(int i, int j) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void realloc(int nb) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ asPtr() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public Area getInternal(int idx) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setInternalByIndex(int idx, Area value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ getPtr() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __struct__ getStruct() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setStruct(__struct__ value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public double getDouble(String fieldName) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setDouble(String fieldName, double value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __struct__ getStruct(String fieldName) {
		throw new UnsupportedOperationException(getClass().toString());
	}

}
