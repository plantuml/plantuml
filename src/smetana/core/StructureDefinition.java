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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StructureDefinition {

	private static final Map<Class, StructureDefinition> all = new HashMap<Class, StructureDefinition>();

	public static StructureDefinition from(Class cl) {
		if (cl == null) {
			throw new IllegalArgumentException();
		}
		StructureDefinition result = all.get(cl);
		if (result == null) {
			result = new StructureDefinition(cl);
			all.put(cl, result);
		}
		return result;
	}

	private final Class cl;
	private Map<String, Bucket> buckets; // = new LinkedHashMap<String, StructureDefinition.Bucket>();

	private StructureDefinition(Class cl) {
		// JUtils.LOG("BUIDLING StructureDefinition " + cl);
		this.cl = cl;
	}

	public String toString() {
		return (cl == null ? "NO_CLASS" : cl.getName()) + " " + buckets;
	}

	private Map<String, Bucket> buckets() {
		if (buckets == null) {
			final List<String> definition = CType.getDefinition(cl);
			JUtils.LOG("StructureDefinition::run for " + cl);
			JUtils.LOG("def=" + definition);
			JUtils.LOG("first=" + definition.get(0));

			buckets = new LinkedHashMap<String, Bucket>();

			if (definition.get(0).equals("typedef enum")) {
				final String last = definition.get(definition.size() - 1);
				if (last.matches("\\w+") == false) {
					throw new UnsupportedOperationException();
				}
				buckets.put(last, Bucket.buildEnum(last, definition));
				return buckets;
			}

			if (definition.get(0).equals("typedef struct gvplugin_active_textlayout_s") == false
					&& definition.get(0).equals("typedef struct color_s") == false
					&& definition.get(0).equals("typedef struct") == false
					&& definition.get(0).equals("typedef struct pointf_s") == false
					&& definition.get(0).equals("typedef struct gvplugin_active_layout_s") == false
					&& definition.get(0).equals("typedef struct GVCOMMON_s") == false
					&& definition.get(0).equals("struct " + cl.getSimpleName()) == false
					&& definition.get(0).equals("typedef struct " + cl.getSimpleName()) == false
					&& definition.get(0).equals("typedef struct " + cl.getSimpleName().replaceFirst("_t", "_s")) == false
					&& definition.get(0).equals("typedef union " + cl.getSimpleName()) == false) {
				throw new IllegalStateException("<struct " + cl.getSimpleName() + "> VERSUS <" + definition.get(0)
						+ ">");
			}
			if (definition.get(1).equals("{") == false) {
				throw new IllegalStateException();
			}

			int last = definition.size() - 1;
			if (definition.get(definition.size() - 2).equals("}")
					&& definition.get(definition.size() - 1).equals(cl.getSimpleName())) {
				last--;
			}

			if (definition.get(last).equals("}") == false) {
				throw new IllegalStateException();
			}

			for (Iterator<String> it = definition.subList(2, last).iterator(); it.hasNext();) {
				buckets.putAll(Bucket.buildSome(it));
			}
		}
		return buckets;
	}

	public Set<String> getFields() {
		return buckets().keySet();
	}

	public Collection<Bucket> getBuckets() {
		return buckets().values();
	}

	public Map<String, Bucket> getBucketsMap() {
		return buckets();
	}

	public Bucket getBucket(String field) {
		final Bucket result = buckets().get(field);
		if (result == null) {
			throw new IllegalArgumentException(field);
		}
		return result;
	}

	public Class getTheClass() {
		return cl;
	}

	public boolean containsFieldName(String fieldName) {
		return buckets().keySet().contains(fieldName);
	}

}
