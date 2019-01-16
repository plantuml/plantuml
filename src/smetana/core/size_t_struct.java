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

/**
 * "Pseudo size" of a C structure. In C, this is the actual size of the structure. In Java, this is an indication to
 * know which structure we are going to allocate.
 * 
 * @author Arnaud Roques
 * 
 */
public class size_t_struct implements size_t {

	public final Class tobeAllocated;
	private boolean positive = true;

	public size_t_struct(Class tobeAllocated) {
		this.tobeAllocated = tobeAllocated;
	}


	public size_t_struct negate() {
		final size_t_struct result = new size_t_struct(tobeAllocated);
		result.positive = !result.positive;
		return result;
	}


	public size_t_struct multiply(int sz) {
		throw new UnsupportedOperationException();
	}

//	@Override
//	public String toString() {
//		return super.toString() + " size_t(" + tobeAllocated + "*" + arraySize + ", bytes=" + bytes + ")";
//	}

	public boolean isStrictPositive() {
		return positive;
	}

	public boolean isStrictNegative() {
		return !positive;
	}



	public final Class getTobeAllocated() {
		return tobeAllocated;
	}

	public __ptr__ malloc() {
		if (tobeAllocated != null) {
			return Memory.malloc(tobeAllocated);
		}
		return (__ptr__) new CObject(-1, tobeAllocated);
	}

	public size_t_struct plus(int strlen) {
//		throw new UnsupportedOperationException();
		JUtils.LOG("adding " + strlen + " to " + this);
		return this;
	}

	public boolean isZero() {
		return false;
	}

	
	public __ptr__ realloc(Object old) {
		throw new UnsupportedOperationException();
	}


	public int getInternalNb() {
		throw new UnsupportedOperationException();
	}


}
