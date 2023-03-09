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

import java.util.concurrent.atomic.AtomicInteger;

public class UnsupportedStarStruct implements __struct__, __ptr__ {

	// ::revert when __CORE__
	public final static AtomicInteger CPT = new AtomicInteger();
	// public static int CPT;
	// ::done
	public final int UID;

	public static UnsupportedStarStruct SPY_ME;

	public UnsupportedStarStruct() {
		// ::revert when __CORE__
		this.UID = CPT.incrementAndGet();
		// this.UID = CPT++;
		// ::done
	}

	final public __ptr__ unsupported() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public boolean isSameThan(__ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ castTo(Class dest) {
		System.err.println("I am " + toString() + " " + UID);
		throw new UnsupportedOperationException(dest + " " + getClass().toString());
	}

	public Object getTheField(FieldOffset virtualBytes) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __struct__ copy() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void ___(__struct__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

}
