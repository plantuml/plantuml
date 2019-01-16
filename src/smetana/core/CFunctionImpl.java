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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import smetana.core.amiga.Area;

public class CFunctionImpl extends UnsupportedC implements CFunction, Area {

	private final Class codingClass;
	private final String name;
	private final Method method;

	public static CFunctionImpl create(Class codingClass, String name) {
		return new CFunctionImpl(codingClass, name);
	}

	private CFunctionImpl(Class codingClass, String name) {
		this.codingClass = codingClass;
		this.name = name;
		for (Method m : codingClass.getMethods()) {
			if (m.getName().equals(name)) {
				this.method = m;
				return;
			}
		}
		JUtils.LOG("CANNOT FIND METHOD " + name + " IN " + codingClass);
		throw new IllegalStateException("codingClass=" + codingClass + " name=" + name);
	}

	@Override
	public String toString() {
		return codingClass.getName() + "::" + name;
	}

	public Object exe(Object... args) {
		JUtils.LOG("-------");
		for (Object arg : args) {
			JUtils.LOG("arg=" + arg);
		}
		JUtils.LOG("method="+method);
		try {
			return this.method.invoke(null, args);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(toString());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(toString());
		}
	}

	public String getName() {
		return name;
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException();
	}
	
}
