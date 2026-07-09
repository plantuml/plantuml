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
 * This translation is distributed under the same License as the original C program.
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

final public class CArrayOfStar<O> extends UnsupportedC {

	private final Object[] data;
	private final int offset;

	@Override
	public String toString() {
		return "*Array offset=" + offset + " [" + data.length + "]" + data;
	}

	private CArrayOfStar(Object data[], int offset) {
		this.data = data;
		this.offset = offset;
	}

	public static <O> CArrayOfStar<O> ALLOC(int size, ZType type) {
		final CArrayOfStar<O> result = new CArrayOfStar<O>(new Object[size], 0);
		return result;
	}

	public static <O> CArrayOfStar<O> REALLOC(int size, CArrayOfStar<O> old, ZType type) {
		if (old == null)
			return ALLOC(size, type);

		if (size <= old.data.length)
			return old;

		if (old.offset != 0)
			throw new IllegalStateException();

		final CArrayOfStar<O> result = ALLOC(size, type);
		System.arraycopy(old.data, 0, result.data, 0, old.data.length);
		return result;
	}

	public int comparePointer_(CArrayOfStar<O> other) {
		if (this.data != other.data)
			throw new IllegalArgumentException();

		return this.offset - other.offset;
	}

	public O get_(int i) {
		return (O) data[i + offset];
	}

	public void set_(int i, O value) {
		data[i + offset] = value;
	}

	public CArrayOfStar<O> plus_(int delta) {
		return new CArrayOfStar<O>(data, offset + delta);
	}

	public void _swap(int i, int j) {
		// Arnaud/Claude 2026-07-08 (SMETANA.md, zdev.Test_12 root cause): this used
		// to swap data[i]/data[j] directly (ignoring `offset`) and defensively throw
		// for any non-zero-offset array, since swapping unshifted indices on such an
		// array would silently corrupt the WRONG physical slots. But get_(i)/set_(i)
		// right above already correctly use data[i + offset] -- _swap just never got
		// the same treatment. This was dormant for years because no caller ever
		// exercised an actual swap (i.e. a real out-of-order adjacent pair) on a
		// non-zero-offset array -- e.g. mincross__c.restore_best()'s qsort() call on
		// an expanded cluster's GD_rank(g)[r].v (which aliases GD_rank(root)[r].v at
		// a non-zero offset via merge_ranks) only started actually needing to swap
		// once restore_best's ND_order-restore loop was fixed (Test_7 case study) to
		// stop being a no-op. The correct fix is to make _swap offset-aware, exactly
		// like get_/set_, not to forbid non-zero offsets.
		final Object tmp = data[i + offset];
		data[i + offset] = data[j + offset];
		data[j + offset] = tmp;
	}

}
