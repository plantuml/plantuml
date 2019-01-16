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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OFFSET {

	private static int CPT = 10000;
	private static Map<Integer, OFFSET> byID = new HashMap<Integer, OFFSET>();
	private static Map<Object, OFFSET> primaryKey = new HashMap<Object, OFFSET>();

	private final Class cl;
	private final String field;
	private final int id;

	private OFFSET(Class cl, String field) {
		this.cl = cl;
		this.field = field;
		this.id = CPT++;
		JUtils.LOG("REAL CREATING OF " + this);
	}

	@Override
	public String toString() {
		return cl.getName() + "::" + field;
	}

	public static OFFSET create(Class cl, String field) {
		final Object key = Arrays.asList(cl, field);
		JUtils.LOG("getting OFFSET " + key);
		OFFSET result = primaryKey.get(key);
		if (result != null) {
			JUtils.LOG("FOUND!");
			return result;
		}
		result = new OFFSET(cl, field);
		byID.put(result.id, result);
		primaryKey.put(key, result);
		return result;
	}

	public int toInt() {
		return id;
	}

	public static OFFSET fromInt(int value) {
		final OFFSET result = byID.get(value);
		if (result == null) {
			throw new IllegalArgumentException("value=" + value);
		}
		return result;
	}

	public final Class getTheClass() {
		return cl;
	}

	public final String getField() {
		return field;
	}

}
