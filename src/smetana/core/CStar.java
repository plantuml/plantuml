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

public class CStar<O> extends UnsupportedC {

	private final Class cl;
	private final List<O> data;
	private final int offset;

//	public CStar(int size, Class cl) {
//		this.data = new ArrayList<O>();
//		this.offset = 0;
//		this.cl = cl;
//		realloc(size);
//	}
	
	public static <O> CStar ALLOC__(int size, Class<O> cl) {
		final CStar<O> result = new CStar<O>(new ArrayList<O>() , 0, cl);
		result.reallocWithStructure(size);
		return result;
	}

	public static <O> CStar REALLOC__(int size, CStar<O> old, Class<O> cl) {
		if (old==null) {
			return ALLOC__(size, cl);
		}
		old.reallocWithStructure(size);
		return old;
	}


	private CStar(List<O> data, int offset, Class cl) {
		this.data = data;
		this.offset = offset;
		this.cl = cl;
	}

	public CStar<O> plus_(int delta) {
		return new CStar(data, offset + delta, cl);
	}
	
	public int minus_(CStar<O> other) {
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

	private void reallocWithoutData(int size) {
		if (offset != 0) {
			throw new IllegalStateException();
		}
		try {
			for (int i = 0; i < size; i++) {
				data.add(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
	}


}
