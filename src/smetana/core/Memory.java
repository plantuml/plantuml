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

public class Memory {

	public static __ptr__ malloc(Class theClass) {
		JUtils.LOG("MEMORY::malloc " + theClass);
		return JUtils.create(theClass, null);
	}

	public static __ptr__ malloc(size_t size) {
		return (__ptr__) size.malloc();
	}

	public static __ptr__ realloc(__ptr__ old, size_t size) {
		throw new UnsupportedOperationException("" + old.getClass());
	}

	public static void free(Object arg) {
	}

	public static int identityHashCode(CString data) {
		if (data == null) {
			return 0;
		}
		// int result = 2 * System.identityHashCode(data);
		int result = data.getUid();
		Z.z().all.put(result, data);
		// System.err.println("Memory::identityHashCode data=" + data);
		// System.err.println("Memory::identityHashCode result=" + result + " " + Z.z().all.size());
		return result;
	}

	public static Object fromIdentityHashCode(int hash) {
		// System.err.println("Memory::fromIdentityHashCode hash=" + hash);
		if (hash % 2 != 0) {
			throw new IllegalArgumentException();
		}
		Object result = Z.z().all.get(hash);
		// System.err.println("Memory::fromIdentityHashCode result=" + result);
		if (result == null) {
			throw new UnsupportedOperationException();
		}
		return result;
	}

}
