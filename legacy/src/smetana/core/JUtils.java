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

import com.plantuml.api.cheerpj.WasmLog;

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_bezier;
import h.ST_pointf;
import h.ST_splines;

// http://docs.oracle.com/javase/specs/jls/se5.0/html/expressions.html#15.7.4
// http://www.jbox.dk/sanos/source/lib/string.c.html

final public class JUtils {

	public static int USHRT_MAX = 65535;

	public static int strcmp(CString s1, CString s2) {
		return s1.strcmp(s2);
	}

	public static int strncmp(CString s1, CString s2, int n) {
		return s1.strcmp(s2, n);
	}

	public static CString strstr(CString s1, CString s2) {
		throw new UnsupportedOperationException("s1=" + s1 + " s2=" + s2);
	}

	public static void strncpy(CString destination, CString source, int nb) {
		destination.copyFrom(source, nb);
	}

	public static CString strchr(CString str, char c) {
		return str.strchr(c);
	}

	public static int strtol(CString str, CString[] endptr, int base) {
		if (base != 10) {
			throw new IllegalArgumentException();
		}
		CString end = str;
		int result = Integer.parseInt(end.getContent());
		endptr[0] = end.plus_(("" + result).length());
		return result;
	}

	public static double strtod(CString str, CString[] endptr) {
		final double result = Double.parseDouble(str.getContent());
		return result;
	}

	public static double atof(CString str) {
		return Double.parseDouble(str.getContent());
	}

	public static int memcmp(__ptr__ s1, __ptr__ s2, int sz) {
		throw new UnsupportedOperationException("s1=" + s1 + " s2=" + s2 + " sz=" + sz);
	}

	public static void memset(__ptr__ obj, int value, size_t nbytes) {
		if (value != 0) {
			throw new UnsupportedOperationException();
		}
	}

	public static boolean isdigit(char c) {
		return Character.isDigit(c);
	}

	public static int atoi(CString s) {
		return Integer.parseInt(s.getContent());
	}

	public static char tolower(char c) {
		return Character.toLowerCase(c);
	}

	public static CString getenv(CString var) {
		return null;
	}

	public static void LOG(String s) {
		// System.err.println(s);
	}

	public static void LOG2(String s) {
		// System.err.println(s);
	}

	public static boolean EQ_ARRAY(CArrayOfStar o1, CArrayOfStar o2) {
		if (o1 == o2)
			return true;
		if (o1 != null && o2 != null)
			return o1.comparePointer_(o2) == 0;
		return false;
	}

	public static boolean EQ_CSTRING(CString o1, CString o2) {
		if (o1 == o2)
			return true;
		if (o1 != null && o2 != null)
			return o1.isSameThan(o2);
		return false;
	}

	public static void qsort(Globals zz, CArrayOfStar array, int nb, CFunction compare) {
		WasmLog.log("bubble sort objects " + nb);
		try {
			for (int pass = 0; pass < nb - 1; pass++) {
				boolean change = false;
				for (int i = 0; i < nb - 1; i++) {
					final __ptr__ element1 = array.plus_(i);
					final __ptr__ element2 = array.plus_(i + 1);
					final Integer cmp = (Integer) compare.exe(zz, element1, element2);
					if (cmp.intValue() > 0) {
						change = true;
						array._swap(i, i + 1);
					}
				}
				if (change == false)
					return;
			}
		} finally {
			WasmLog.log("sort done");
			// ::comment when __CORE__
			for (int i = 0; i < nb - 1; i++) {
				final __ptr__ element1 = array.plus_(i);
				final __ptr__ element2 = array.plus_(i + 1);
				final Integer cmp = (Integer) compare.exe(zz, element1, element2);
				if (cmp.intValue() > 0)
					throw new IllegalStateException();
			}
			// ::done
		}

	}

	public static void qsortInt(Globals zz, int array[], int nb, CFunction compare) {
		WasmLog.log("bubble sort int[] " + nb);
		try {
			for (int pass = 0; pass < nb - 1; pass++) {
				boolean change = false;
				for (int i = 0; i < nb - 1; i++) {
					final Integer element1 = array[i];
					final Integer element2 = array[i + 1];
					final Integer cmp = (Integer) compare.exe(zz, element1, element2);
					if (cmp.intValue() > 0) {
						change = true;
						final int tmp = array[i];
						array[i] = array[i + 1];
						array[i + 1] = tmp;
					}
				}
				if (change == false)
					return;
			}
		} finally {
			WasmLog.log("sort done");
			// ::comment when __CORE__
			for (int i = 0; i < nb - 1; i++) {
				final Integer element1 = array[i];
				final Integer element2 = array[i + 1];
				final Integer cmp = (Integer) compare.exe(zz, element1, element2);
				if (cmp.intValue() > 0)
					throw new IllegalStateException();
			}
			// ::done
		}

	}

	static public int setjmp(jmp_buf jmp) {
		// if (jmp.hasBeenCalled()) {
		// throw new UnsupportedOperationException();
		// }
		jmp.saveCallingEnvironment();
		return 0;
	}

	// DEBUG

	public static void printDebugEdge(ST_Agedge_s e) {
		System.err.println("*********** PRINT EDGE ********** ");
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) e.data.castTo(ST_Agedgeinfo_t.class);
		final ST_splines splines = (ST_splines) data.spl;
		// ST_boxf bb = (ST_boxf) splines.bb;
		final ST_bezier list = splines.list.get__(0);
		System.err.println("splines.size=" + splines.size);
		// System.err.println("bb.LL=" + pointftoString(bb.LL));
		// System.err.println("bb.UR=" + pointftoString(bb.UR));
		printDebugBezier((ST_bezier) splines.list.get__(0));

	}

	private static String pointftoString(ST_pointf point) {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(point.x);
		sb.append(" ; ");
		sb.append(point.y);
		sb.append(")");
		return sb.toString();
	}

	private static void printDebugBezier(ST_bezier bezier) {
		System.err.println("bezier.size=" + bezier.size);
		System.err.println("bezier.sflag=" + bezier.sflag);
		System.err.println("splines.eflag=" + bezier.eflag);
		System.err.println("bezier.sp=" + pointftoString(bezier.sp));
		System.err.println("bezier.ep=" + pointftoString(bezier.ep));
		for (int i = 0; i < bezier.size; i++) {
			final ST_pointf pt = bezier.list.get__(i);
			System.err.println("pt=" + pointftoString(pt));
		}
	}

}
