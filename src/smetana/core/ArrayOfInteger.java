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

public class ArrayOfInteger implements __array_of_integer__ {

	private final int[] data;
	private final int position;

	public ArrayOfInteger(int[] data, int position) {
		this.data = data;
		this.position = position;

	}

	public String getUID36() {
		throw new UnsupportedOperationException();
	}

	public void swap(int i, int j) {
		throw new UnsupportedOperationException();
	}

	public void realloc(int nb) {
		throw new UnsupportedOperationException();
	}

	public int comparePointerInternal(__array_of_integer__ other) {
		throw new UnsupportedOperationException();
	}

	public final __array_of_integer__ move(int delta) {
		return plus(delta);
	}

	public __array_of_integer__ plus(int delta) {
		return new ArrayOfInteger(data, position + delta);
	}

	public Area getInternal(int idx) {
		throw new UnsupportedOperationException();
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException();
	}

	public int getInt() {
		return data[position];
	}

	public void setInt(int value) {
		data[position] = value;
	}

}
