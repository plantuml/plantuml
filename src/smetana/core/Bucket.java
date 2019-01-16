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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bucket {

	final public String name;
	final public CType ctype;
	final private boolean inlineStruct;

	private Bucket addPrefix(String prefix) {
		return new Bucket(prefix + "." + name, ctype, inlineStruct);
	}

	public static Bucket buildEnum(String name, List<String> definition) {
		return new Bucket(name, new CType("enum"), false);
	}

	Bucket(String name, String type, boolean inlineStruct) {
		this(name, new CType(type), inlineStruct);
	}

	private Bucket(String name, CType ctype, boolean inlineStruct) {
		this.name = name;
		this.ctype = ctype;
		this.inlineStruct = inlineStruct;
	}

	@Override
	public String toString() {
		return "[" + ctype + "==" + name + "]";
	}

	public boolean inlineStruct() {
		return inlineStruct;
	}

	public boolean functionPointer() {
		if (ctype == null) {
			return false;
		}
		return ctype.functionPointer();
	}

	public static Map<String, Bucket> buildSome(Iterator<String> it) {

		String def = it.next();
		JUtils.LOG("DEF1=" + def);
		def = def.replaceAll("unsigned int", "int");
		def = def.replaceAll("unsigned long", "int");
		def = def.replaceAll("unsigned short", "short");
		def = def.replaceAll("unsigned char", "char");
		def = def.replaceAll("const ", "");
		def = def.replaceAll("struct ", "");
		def = def.replaceAll("\\[\\s*[+]1\\s*[+]1\\s*[+]1\\s*[+]1\\s*[+]1\\s*\\]", "[5]");
		// JUtils.LOG("DEF2=" + def);
		// int foo

		if (def.equals("union")) {
			return createStruct(it);
		} else if (def.equals("struct")) {
			return createStruct(it);
		}

		final Map<String, Bucket> result = new LinkedHashMap<String, Bucket>();

		// Dummy foo;
		Pattern p1 = Pattern.compile("^(\\w+)\\s+(\\w+)$");
		Matcher m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			final boolean inline = CType.isPrimitive(type) == false;
			result.put(m1.group(2), new Bucket(m1.group(2), type, inline));
			return result;
		}
		// Dummy A, B
		p1 = Pattern.compile("^(\\w+)\\s+(\\w+),\\s*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			final boolean inline = CType.isPrimitive(type) == false;
			result.put(m1.group(2), new Bucket(m1.group(2), type, inline));
			result.put(m1.group(3), new Bucket(m1.group(3), type, inline));
			return result;
		}
		// Dummy A, B, C
		p1 = Pattern.compile("^(\\w+)\\s+(\\w+),\\s*(\\w+),\\s*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			final boolean inline = CType.isPrimitive(type) == false;
			result.put(m1.group(2), new Bucket(m1.group(2), type, inline));
			result.put(m1.group(3), new Bucket(m1.group(3), type, inline));
			result.put(m1.group(4), new Bucket(m1.group(4), type, inline));
			return result;
		}
		// Dummy A, B, C, D
		p1 = Pattern.compile("^(\\w+)\\s+(\\w+),\\s*(\\w+),\\s*(\\w+),\\s*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			final boolean inline = CType.isPrimitive(type) == false;
			result.put(m1.group(2), new Bucket(m1.group(2), type, inline));
			result.put(m1.group(3), new Bucket(m1.group(3), type, inline));
			result.put(m1.group(4), new Bucket(m1.group(4), type, inline));
			result.put(m1.group(5), new Bucket(m1.group(5), type, inline));
			return result;
		}
		// Dummy A, B, C, D, E
		p1 = Pattern.compile("^(\\w+)\\s+(\\w+),\\s*(\\w+),\\s*(\\w+),\\s*(\\w+),\\s*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			final boolean inline = CType.isPrimitive(type) == false;
			result.put(m1.group(2), new Bucket(m1.group(2), type, inline));
			result.put(m1.group(3), new Bucket(m1.group(3), type, inline));
			result.put(m1.group(4), new Bucket(m1.group(4), type, inline));
			result.put(m1.group(5), new Bucket(m1.group(5), type, inline));
			result.put(m1.group(6), new Bucket(m1.group(6), type, inline));
			return result;
		}
		// int (*foo)()
		p1 = Pattern.compile("^.*(\\(\\*(\\w+)\\)).*$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(1), new Bucket(m1.group(2), def, false));
			return result;
		}
		// void *data
		p1 = Pattern.compile("^(void)\\s+\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(2), new Bucket(m1.group(2), "void*", false));
			return result;
		}
		// char *data
		p1 = Pattern.compile("^(char)\\s+\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(2), new Bucket(m1.group(2), "CString", false));
			return result;
		}
		// int *data
		p1 = Pattern.compile("^(int)\\s+\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(2), new Bucket(m1.group(2), "int*", false));
			return result;
		}
		// char **data
		p1 = Pattern.compile("^(char)\\s+\\*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(2), new Bucket(m1.group(2), "CString[]", false));
			return result;
		}
		// void* foo
		p1 = Pattern.compile("^(void)(\\s+\\*|\\*\\s+)(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = "void*";
			// final Class cl = CType.getClassFrom(type);
			result.put(m1.group(3), new Bucket(m1.group(3), type, false));
			return result;
		}
		// dummy* foo
		p1 = Pattern.compile("^(\\w+)(\\s+\\*|\\*\\s+)(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			// final Class cl = CType.getClassFrom(type);
			result.put(m1.group(3), new Bucket(m1.group(3), type, false));
			return result;
		}
		// foo *A, *B
		p1 = Pattern.compile("^(\\w+)\\s+\\*(\\w+),\\s*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			// final Class cl = CType.getClassFrom(type);
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			result.put(m1.group(3), new Bucket(m1.group(3), type, false));
			return result;
		}
		// foo *A, *B, *C
		p1 = Pattern.compile("^(\\w+)\\s+\\*(\\w+),\\s*\\*(\\w+),\\s*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			// final Class cl = CType.getClassFrom(type);
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			result.put(m1.group(3), new Bucket(m1.group(3), type, false));
			result.put(m1.group(4), new Bucket(m1.group(4), type, false));
			return result;
		}
		// foo *A, *B, *C, *D
		p1 = Pattern.compile("^(\\w+)\\s+\\*(\\w+),\\s*\\*(\\w+),\\s*\\*(\\w+),\\s*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			// final Class cl = CType.getClassFrom(type);
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			result.put(m1.group(3), new Bucket(m1.group(3), type, false));
			result.put(m1.group(4), new Bucket(m1.group(4), type, false));
			result.put(m1.group(5), new Bucket(m1.group(5), type, false));
			return result;
		}
		p1 = Pattern.compile("^(unsigned|int)\\s+(\\w+):([-sizeof ntunsged()0-9*]+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(2), new Bucket(m1.group(2), "int", false));
			return result;
		}
		// Dummy foo[3]
		p1 = Pattern.compile("^(\\w+)\\s+(\\w+)\\[\\d+\\]$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			result.put(m1.group(1), new Bucket(m1.group(2), def, true));
			return result;
		}
		// Dummy *foo[3]
		p1 = Pattern.compile("^(\\w+)\\s+\\*(\\w+)\\[\\d+\\]$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final Bucket tmp = new Bucket(m1.group(2), def, false);
			JUtils.LOG("size=" + tmp.getArrayLength());
			result.put(m1.group(1), tmp);
			return result;
		}
		// graph_t **clust
		p1 = Pattern.compile("^(\\w+)\\s+\\*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1) + "*";
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			return result;
		}
		// node_t **store, **limit, **head, **tail
		p1 = Pattern.compile("^(\\w+)\\s+\\*\\*(\\w+),\\s*\\*\\*(\\w+),\\s*\\*\\*(\\w+),\\s*\\*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1) + "*";
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			result.put(m1.group(3), new Bucket(m1.group(3), type, false));
			result.put(m1.group(4), new Bucket(m1.group(4), type, false));
			result.put(m1.group(5), new Bucket(m1.group(5), type, false));
			return result;
		}
		// foo *A, **B
		p1 = Pattern.compile("^(\\w+)\\s+\\*(\\w+),\\s*\\*\\*(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1);
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			result.put(m1.group(3), new Bucket(m1.group(3), type + "*", false));
			return result;
		}
		// foo **B
		p1 = Pattern.compile("^(\\w+)\\*\\*\\s+(\\w+)$");
		m1 = p1.matcher(def);
		if (m1.find()) {
			final String type = m1.group(1) + "*";
			result.put(m1.group(2), new Bucket(m1.group(2), type, false));
			return result;
		}
		if (def.equals("double **dist, **spring, **sum_t, ***t")) {
			// Let's skip this one...
			return result;
		}
		if (def.equals("double *pos, dist")) {
			result.put("pos", new Bucket("pos", "double*", false));
			result.put("dist", new Bucket("dist", "double", false));
			return result;
		}
		// p1 = Pattern.compile("^unsigned\\s+long\\s+(\\w+)$");
		// m1 = p1.matcher(def);
		// if (m1.find()) {
		// mem.put(m1.group(1), new Bucket(m1.group(1), "int", false, 4, false));
		// return;
		// }
		JUtils.LOG("def=" + def);
		throw new UnsupportedOperationException(def);
	}

	private static Map<String, Bucket> createStruct(Iterator<String> it) {
		String current = it.next();
		if (current.equals("{") == false) {
			throw new UnsupportedOperationException("current=" + current);
		}
		final Map<String, Bucket> tmp = new LinkedHashMap<String, Bucket>();
		final List<String> inner = new ArrayList<String>();
		while (true) {
			current = it.next();
			if (current.equals("union") || current.equals("struct")) {
				Map<String, Bucket> internal = createStruct(it);
				JUtils.LOG("internal=" + internal);
				tmp.putAll(internal);
			} else if (current.endsWith("}")) {
				final String name = it.next();
				JUtils.LOG("name=" + name);
				JUtils.LOG("inner=" + inner);
				// final Map<String, Bucket> some = new LinkedHashMap<String, Bucket>();
				final Iterator<String> innerIt = inner.iterator();
				while (innerIt.hasNext()) {
					tmp.putAll(buildSome(innerIt));
				}
				final Map<String, Bucket> result = new LinkedHashMap<String, Bucket>();
				for (Map.Entry<String, Bucket> ent : tmp.entrySet()) {
					final String n = ent.getKey();
					final Bucket bucket = ent.getValue().addPrefix(name);
					JUtils.LOG("n=" + n);
					JUtils.LOG("bucket=" + bucket);
					result.put(name + "." + n, bucket);
				}
				return result;
			} else {
				inner.add(current);
			}
		}
		// JUtils.LOG("some=" + some);
		// throw new UnsupportedOperationException();

	}

	public int getArrayLength() {
		if (ctype == null) {
			return 0;
		}
		return ctype.getArrayLength();
	}

}
