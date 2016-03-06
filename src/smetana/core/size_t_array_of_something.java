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

package smetana.core;

import smetana.core.amiga.StarArrayOfPtr;

public class size_t_array_of_something implements size_t {

	final private int nb;
	final private Class cl;

	public size_t_array_of_something(Class cl, int nb) {
		this.nb = nb;
		this.cl = cl;
	}

	public boolean isZero() {
		return nb == 0;
	}

	public __ptr__ malloc() {
		return new StarArrayOfPtr(__array_of_ptr__.malloc_allocated(cl, nb));
	}

	public size_t negate() {
		throw new UnsupportedOperationException();
	}

	public size_t plus(int length) {
		throw new UnsupportedOperationException();
	}

	public boolean isStrictPositive() {
		throw new UnsupportedOperationException();
	}

	public boolean isStrictNegative() {
		throw new UnsupportedOperationException();
	}

	public __ptr__ realloc(Object old) {
		((StarArrayOfPtr) old).realloc(nb);
		return (__ptr__) old;
	}

	public int getNb() {
		return nb;
	}

}
