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

public class CArrayOfStar<O> extends UnsupportedC {

	private final List<O> data;
	private final int offset;
	
	
	@Override
	public String toString() {
		return "*Array offset=" + offset + " [" + data.size() + "]" + data;
	}


	private CArrayOfStar(List<O> data, int offset) {
		this.data = data;
		this.offset = offset;
	}

	public static <O> CArrayOfStar<O> ALLOC(int size, Class cl) {
		final CArrayOfStar<O> result = new CArrayOfStar<O>(new ArrayList<O>(), 0);
		result.realloc(size);
		return result;
	}

	public static <O> CArrayOfStar<O> REALLOC(int size, CArrayOfStar<O> old, Class<O> cl) {
		if (old==null) {
			return ALLOC(size, cl);
		}
		old.realloc(size);
		return old;
	}


	public int comparePointer_(CArrayOfStar<O> other) {
		if (this.data != other.data) {
			throw new IllegalArgumentException();
		}
		return this.offset - other.offset;
	}

	public O get_(int i) {
		return data.get(i + offset);
	}

	public void set_(int i, O value) {
		data.set(i + offset, value);
	}

	public void realloc(int size) {
		if (offset != 0) {
			throw new IllegalStateException();
		}
		for (int i = 0; i < size; i++) {
			data.add(null);
		}
	}

	public CArrayOfStar<O> plus_(int delta) {
		return new CArrayOfStar<O>(data, offset + delta);
	}

	public void _swap(int i, int j) {
		if (offset != 0) {
			throw new IllegalStateException();
		}
		final O e1 = data.get(i);
		final O e2 = data.get(j);
		data.set(i, e2);
		data.set(j, e1);
	}

}
