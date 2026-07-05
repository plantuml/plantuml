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

package smetana.core.debug;

import java.util.LinkedHashMap;
import java.util.Map;

import gen.lib.cgraph.id__c;
import h.ST_Agobj_s;
import smetana.core.Globals;

public final class SmetanaDebug {
	// ::remove folder when __HAXE__
	static private final Map<String, String> methods = new LinkedHashMap<String, String>();

	static private java.io.PrintWriter traceWriter;

	// [DEBUG-2735] Writes debug traces to smetana.txt instead of System.out/err,
	// because System.out/err don't seem to show up reliably from this test run
	// (probably swallowed/redirected by the JUnit runner). File is truncated once
	// per JVM run (first call), then appended to and flushed after every line so
	// content survives even if the test crashes.
	static public synchronized void TRACE(String s) {
		try {
			if (traceWriter == null) {
				traceWriter = new java.io.PrintWriter(new java.io.FileWriter("smetana.txt", false));
			}
			traceWriter.println(s);
			traceWriter.flush();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	// [DEBUG-2735] Safe replacement for agnameof() in debug traces.
	// Some nodes (e.g. fastgr__c.virtual_node()) are built with `new ST_Agnode_s()`
	// directly, bypassing agnode()/idmap(), so their tag.id is never set (stays 0).
	// agnameof() on such a node calls idprint -> Memory.fromIdentityHashCode(zz, 0),
	// which finds nothing in zz.all and throws UnsupportedOperationException.
	// This is a real (separate, pre-existing) translation gap, but it's not what
	// we're hunting right now -- so debug traces fall back to a readable
	// placeholder instead of crashing the whole test.
	static public String safeName(Globals zz, Object obj) {
		try {
			Object name = id__c.agnameof(zz, (ST_Agobj_s) obj);
			return name == null ? ("<anon:" + System.identityHashCode(obj) + ">") : name.toString();
		} catch (Throwable t) {
			return "<unnamed:" + System.identityHashCode(obj) + " (" + t.getClass().getSimpleName() + ")>";
		}
	}

	static public void LOG(String s) {

	}

	static public void ENTERING(String signature, String methodName) {
//		if (methods.containsKey(methodName) == false)
//			methods.put(methodName, methodName);
	}

	static public void LIST_METHODS() {
		int i = 0;
		for (String s : methods.keySet()) {
			System.err.println("i=" + i + " " + s);
			i++;
		}
	}

	static public void LEAVING(String signature, String methodName) {
	}

	public static void reset() {
	}

	public static void printMe() {
	}

}
