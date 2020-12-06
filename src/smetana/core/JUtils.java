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

import h.ST_Agattr_s;
import h.ST_Agclos_s;
import h.ST_Agdatadict_s;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agedgepair_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agsubnode_s;
import h.ST_Agsym_s;
import h.ST_bezier;
import h.ST_dtdata_s;
import h.ST_dthold_s;
import h.ST_path;
import h.ST_pointf;
import h.ST_splines;
import smetana.core.debug.SmetanaDebug;

// http://docs.oracle.com/javase/specs/jls/se5.0/html/expressions.html#15.7.4
// http://www.jbox.dk/sanos/source/lib/string.c.html

public class JUtils {

	public static int USHRT_MAX = 65535;

	public static size_t sizeof(Class cl) {
		return new size_t(cl);
	}

	public static size_t sizeof(String name, int sz) {
		throw new UnsupportedOperationException();
	}

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

	public static int strlen(CString s) {
		return s.length();
	}

	public static double abs(double x) {
		return Math.abs(x);
	}

	public static double cos(double x) {
		return Math.cos(x);
	}

	public static double sin(double x) {
		return Math.sin(x);
	}

	public static double sqrt(double x) {
		return Math.sqrt(x);
	}

	public static double atan2(double a, double b) {
		return Math.atan2(a, b);
	}

	public static double pow(double a, double b) {
		return Math.pow(a, b);
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

	public static boolean EQ(Object o1, Object o2) {
		final boolean result = EQ_(o1, o2);
		if (o1 instanceof UnsupportedStarStruct && o2 instanceof UnsupportedStarStruct) {
			UnsupportedStarStruct ooo1 = (UnsupportedStarStruct) o1;
			UnsupportedStarStruct ooo2 = (UnsupportedStarStruct) o2;
			if ((ooo1.UID == ooo2.UID) != result) {
				throw new UnsupportedOperationException();
			}
		}
		return result;
	}

	private static boolean EQ_(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null && o2 != null) {
			return false;
		}
		if (o2 == null && o1 != null) {
			return false;
		}
		if (o1 instanceof CString && o2 instanceof CString) {
			return ((CString) o1).isSameThan((CString) o2);
		}
		if (o1 instanceof CArrayOfStar && o2 instanceof CArrayOfStar) {
			return ((CArrayOfStar) o1).comparePointer_((CArrayOfStar) o2) == 0;
		}
		if (o1 instanceof __ptr__ && o2 instanceof __ptr__) {
			return ((__ptr__) o1).isSameThan((__ptr__) o2);
		}

		System.err.println("o1=" + o1.getClass() + " " + o1);
		System.err.println("o2=" + o2.getClass() + " " + o2);
		throw new UnsupportedOperationException();
	}

	public static boolean NEQ(Object o1, Object o2) {
		return EQ(o1, o2) == false;
	}

	public static void qsort1(CArrayOfStar array, int nb, CFunction compare) {
		SmetanaDebug.LOG("qsort1 "+nb);
		boolean change;
		do {
			change = false;
			for (int i = 0; i < nb - 1; i++) {
				__ptr__ element1 = array.plus_(i);
				__ptr__ element2 = array.plus_(i + 1);
				Integer cmp = (Integer) compare.exe(element1, element2);
				if (cmp.intValue() > 0) {
					change = true;
					array._swap(i, i + 1);
				}
			}
		} while (change);
		SmetanaDebug.LOG("qsort1 ok");

	}

	public static void qsort2(int array[], int nb, CFunction compare) {
		SmetanaDebug.LOG("qsort2 "+nb);
		boolean change;
		do {
			change = false;
			for (int i = 0; i < nb - 1; i++) {
				Integer element1 = array[i];
				Integer element2 = array[i + 1];
				Integer cmp = (Integer) compare.exe(element1, element2);
				if (cmp.intValue() > 0) {
					change = true;
					final int tmp = array[i];
					array[i] = array[i + 1];
					array[i + 1] = tmp;
				}
			}
		} while (change);
		SmetanaDebug.LOG("qsort2 ok");

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
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) Macro.AGDATA(e).castTo(ST_Agedgeinfo_t.class);
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

	public static __ptr__ create(Class theClass, __ptr__ parent) {
		if (theClass == ST_Agedgepair_s.class) {
			return new ST_Agedgepair_s();
		}
		if (theClass == ST_Agsym_s.class) {
			return new ST_Agsym_s();
		}
		if (theClass == ST_dthold_s.class) {
			return new ST_dthold_s();
		}
		if (theClass == ST_path.class) {
			return new ST_path();
		}
		if (theClass == ST_Agedgeinfo_t.class) {
			return new ST_Agedgeinfo_t();
		}
		if (theClass == ST_Agnodeinfo_t.class) {
			return new ST_Agnodeinfo_t();
		}
		if (theClass == ST_Agraphinfo_t.class) {
			return new ST_Agraphinfo_t();
		}
		if (theClass == ST_Agattr_s.class) {
			return new ST_Agattr_s();
		}
		if (theClass == ST_Agdatadict_s.class) {
			return new ST_Agdatadict_s();
		}
		if (theClass == ST_dtdata_s.class) {
			return new ST_dtdata_s();
		}
		if (theClass == ST_Agraph_s.class) {
			return new ST_Agraph_s();
		}
		if (theClass == ST_Agsubnode_s.class) {
			return new ST_Agsubnode_s();
		}
		if (theClass == ST_Agnode_s.class) {
			return new ST_Agnode_s();
		}
		if (theClass == ST_Agclos_s.class) {
			return new ST_Agclos_s();
		}
		throw new UnsupportedOperationException(theClass.toString());
	}

}
