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

import java.util.ArrayList;
import java.util.List;

public class CArray<O> extends UnsupportedC {

	private final Class<O> cl;
	private final List<O> data;
	private final int offset;

	@Override
	public String toString() {
		return "Array " + cl + " offset=" + offset + " [" + data.size() + "]" + data;
	}

	public static <O> CArray<O> ALLOC__(int size, Class<O> cl) {
		final CArray<O> result = new CArray<O>(new ArrayList<O>(), 0, cl);
		result.reallocWithStructure(size);
		return result;
	}

	public static <O> CArray<O> REALLOC__(int size, CArray<O> old, Class<O> cl) {
		if (old == null) {
			return ALLOC__(size, cl);
		}
		old.reallocWithStructure(size);
		return old;
	}

	private CArray(List<O> data, int offset, Class<O> cl) {
		if (offset > 0) {
			// JUtilsDebug.LOG("offset=" + offset);
		}
		this.data = data;
		this.offset = offset;
		this.cl = cl;
	}

	public CArray<O> plus_(int delta) {
		return new CArray<O>(data, offset + delta, cl);
	}

	public int minus_(CArray<O> other) {
		if (this.data != other.data) {
			throw new IllegalArgumentException();
		}
		return this.offset - other.offset;
	}

	public O get__(int i) {
		return data.get(i + offset);
	}

	private void reallocWithStructure(int size) {
		if (offset != 0) {
			throw new IllegalStateException();
		}
		try {
			for (int i = 0; i < size; i++) {
				data.add((O) cl.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
	}

}
