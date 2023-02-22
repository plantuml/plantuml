/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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

import com.plantuml.api.cheerpj.WasmLog;

final public class CArray<O> extends UnsupportedC {

	private final ZType type;
	private final Object[] data;
	private final int offset;

	@Override
	public String toString() {
		return "Array " + type + " offset=" + offset + " [" + data.length + "]" + data;
	}

	public static <O> CArray<O> ALLOC__(int size, ZType type) {
		// WasmLog.log("ALLOC " + size);
		final CArray<O> result = new CArray<O>(new Object[size], 0, type);
		for (int i = 0; i < size; i++)
			result.data[i] = type.create();
		return result;
	}

	public static <O> CArray<O> REALLOC__(int size, CArray<O> old, ZType type) {
		// WasmLog.log("REALLOC " + size);
		if (old == null)
			return ALLOC__(size, type);

		if (size <= old.data.length)
			return old;

		if (old.offset != 0)
			throw new IllegalStateException();

		WasmLog.log("Realloc from " + old.data.length + " to " + size);

		final CArray<O> result = new CArray<O>(new Object[size], 0, type);
		System.arraycopy(old.data, 0, result.data, 0, old.data.length);
		for (int i = old.data.length; i < result.data.length; i++)
			result.data[i] = type.create();
		return result;
	}

	private CArray(Object[] data, int offset, ZType type) {
		this.data = data;
		this.offset = offset;
		this.type = type;
	}

	public CArray<O> plus_(int delta) {
		return new CArray<O>(data, offset + delta, type);
	}

	public int minus_(CArray<O> other) {
		if (this.data != other.data) {
			throw new IllegalArgumentException();
		}
		return this.offset - other.offset;
	}

	public O get__(int i) {
		return (O) data[i + offset];
	}

}
