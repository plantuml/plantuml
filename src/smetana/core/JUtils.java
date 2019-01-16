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

import h.Agcbstack_s;
import h.Agdstate_s;
import h.Agiodisc_s;
import h.GVCOMMON_t;
import h.LeafList_t;
import h.ST_Agattr_s;
import h.ST_Agcbstack_s;
import h.ST_Agclos_s;
import h.ST_Agdatadict_s;
import h.ST_Agdesc_s;
import h.ST_Agdisc_s;
import h.ST_Agdstate_s;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agedgepair_s;
import h.ST_Agiddisc_s;
import h.ST_Agiodisc_s;
import h.ST_Agmemdisc_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agrec_s;
import h.ST_Agsubnode_s;
import h.ST_Agsym_s;
import h.ST_GVCOMMON_t;
import h.ST_GVC_s;
import h.ST_HDict_t;
import h.ST_IMapEntry_t;
import h.ST_LeafList_t;
import h.ST_Node_t___;
import h.ST_Pedge_t;
import h.ST_Ppoly_t;
import h.ST_RTree;
import h.ST_XLabels_t;
import h.ST_arrowtype_t;
import h.ST_aspect_t;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_cinfo_t;
import h.ST_dt_s;
import h.ST_dtdata_s;
import h.ST_dtdisc_s;
import h.ST_dthold_s;
import h.ST_dtlink_s;
import h.ST_dtmethod_s;
import h.ST_elist;
import h.ST_fontinfo;
import h.ST_label_params_t;
import h.ST_layout_t;
import h.ST_nlist_t;
import h.ST_nodequeue;
import h.ST_object_t;
import h.ST_pack_info;
import h.ST_path;
import h.ST_pathend_t;
import h.ST_pointf;
import h.ST_pointnlink_t;
import h.ST_polygon_t;
import h.ST_port;
import h.ST_rank_t;
import h.ST_refstr_t;
import h.ST_shape_desc;
import h.ST_shape_functions;
import h.ST_splineInfo;
import h.ST_spline_info_t;
import h.ST_splines;
import h.ST_tedge_t;
import h.ST_textfont_t;
import h.ST_textlabel_t;
import h.ST_textspan_t;
import h.ST_tna_t;
import h.ST_triangle_t;
import h.ST_xlabel_t;
import h.ST_dthold_s;
import h.layout_t;
import h.rank_t;
import h.tedge_t;

import java.util.LinkedHashSet;
import java.util.Set;

import smetana.core.amiga.StarArrayOfInteger;
import smetana.core.amiga.StarStruct;

// http://docs.oracle.com/javase/specs/jls/se5.0/html/expressions.html#15.7.4
// http://www.jbox.dk/sanos/source/lib/string.c.html

public class JUtils {

	public static int USHRT_MAX = 65535;

	public static size_t sizeof(Class cl) {
		return new size_t_struct(cl);
	}

	public static size_t sizeof(String name, int sz) {
		if (name.equals("char*")) {
			return new size_t_array_of_charstars(sz);
		}
		throw new UnsupportedOperationException();
	}

	public static size_t size_t_array_of_integer(int nb) {
		return new size_t_array_of_integer(nb);
	}

	public static int strcmp(CString s1, CString s2) {
		return s1.compareTo(s2);
	}

	public static int strncmp(CString s1, CString s2, int n) {
		return s1.compareTo(s2, n);
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
		endptr[0] = end.plus(("" + result).length());
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

	public static CFunction function(Class codingClass, String name) {
		return CFunctionImpl.create(codingClass, name);
	}

	public static int enumAsInt(Class enumClass, String name) {
		CEnumInterpretor interpretor = new CEnumInterpretor(enumClass);
		final int result = interpretor.valueOf(name);
		JUtils.LOG("result for " + name + " is " + result);
		return result;
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
				// throw new UnsupportedOperationException();
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
		// if (o1 instanceof AreaArray && o2 instanceof AreaArray) {
		// return ((AreaArray) o1).isSameThan((AreaArray) o2);
		// }
		if (o1 instanceof ST_pointnlink_t && o2 instanceof ST_pointnlink_t) {
			return ((ST_pointnlink_t) o1).isSameThan((ST_pointnlink_t) o2);
		}
		if (o1 instanceof ST_object_t && o2 instanceof ST_object_t) {
			return ((ST_object_t) o1).isSameThan((ST_object_t) o2);
		}
		if (o1 instanceof StarStruct && o2 instanceof StarStruct) {
			// System.err.println("o1="+o1.getClass());
			// System.err.println("o2="+o2.getClass());
			return ((StarStruct) o1).isSameThan((StarStruct) o2);
		}
		if (o1 instanceof CString && o2 instanceof CString) {
			return ((CString) o1).isSameThan((CString) o2);
		}
		if (o1 instanceof ST_Agnode_s.ArrayOfStar && o2 instanceof ST_Agnode_s.ArrayOfStar) {
			return ((ST_Agnode_s.ArrayOfStar) o1).isSameThan2((ST_Agnode_s.ArrayOfStar) o2);
		}
		// if (o1 instanceof StarStar && o2 instanceof StarArrayOfPtr) {
		// __ptr__ o1b = ((StarStar) o1).getPtr();
		// __ptr__ o2b = ((StarArrayOfPtr) o2).getPtr();
		// if (((StarStruct) o1b).getRealClass() != ((StarStruct) o2b).getRealClass()) {
		// throw new UnsupportedOperationException();
		// }
		// return EQ(o1b, o2b);
		// }
		System.err.println("o1=" + o1.getClass() + " " + o1);
		System.err.println("o2=" + o2.getClass() + " " + o2);
		throw new UnsupportedOperationException();
	}

	public static boolean NEQ(Object o1, Object o2) {
		return EQ(o1, o2) == false;
	}

	public static void qsort(__ptr__ array, int nb, CFunction compare) {
		if (nb <= 1) {
			return;
		}
		JUtils.LOG("array=" + array);
		JUtils.LOG("nb=" + nb);
		JUtils.LOG("compare=" + compare);
		boolean change;
		do {
			change = false;
			for (int i = 0; i < nb - 1; i++) {
				__ptr__ element1 = array.plus(i);
				__ptr__ element2 = array.plus(i + 1);
				Integer cmp = (Integer) compare.exe(element1, element2);
				JUtils.LOG("cmp=" + cmp);
				if (cmp.intValue() > 0) {
					change = true;
					if (array instanceof StarArrayOfInteger) {
						((StarArrayOfInteger) array).swap(i, i + 1);
						// } else if (array instanceof STStarArrayOfPointer) {
						// ((STStarArrayOfPointer) array).swap(i, i + 1);
					} else if (array instanceof ST_Agnode_s.ArrayOfStar) {
						((ST_Agnode_s.ArrayOfStar) array).swap(i, i + 1);
					} else if (array instanceof ST_Agedge_s.ArrayOfStar) {
						((ST_Agedge_s.ArrayOfStar) array).swap(i, i + 1);
					} else {
						throw new UnsupportedOperationException();
						// ((StarStar) array).swap(i, i + 1);
					}
				}
			}
		} while (change);
		for (int i = 0; i < nb - 1; i++) {
			__ptr__ element1 = array.plus(i);
			__ptr__ element2 = array.plus(i + 1);
			JUtils.LOG("element1=" + element1);
			JUtils.LOG("element2=" + element2);
			Integer cmp = (Integer) compare.exe(element1, element2);
			JUtils.LOG("cmp=" + cmp);
			if (cmp.intValue() > 0) {
				throw new IllegalStateException();
			}
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
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) Macro.AGDATA(e).castTo(ST_Agedgeinfo_t.class);
		final ST_splines splines = (ST_splines) data.spl;
		// ST_boxf bb = (ST_boxf) splines.bb;
		final ST_bezier list = splines.list.getPtr();
		System.err.println("splines.size=" + splines.size);
		//System.err.println("bb.LL=" + pointftoString(bb.LL));
		//System.err.println("bb.UR=" + pointftoString(bb.UR));
		printDebugBezier((ST_bezier) splines.list.getPtr());

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
			final ST_pointf pt = bezier.list.get(i);
			System.err.println("pt=" + pointftoString(pt));
		}
	}

	public static <C extends __ptr__> __struct__<C> from(Class<C> theClass) {
		if (theClass == ST_dtmethod_s.class) {
			return new ST_dtmethod_s();
		}
		if (theClass == ST_dtdisc_s.class) {
			return new ST_dtdisc_s();
		}
		if (theClass == ST_Agdesc_s.class) {
			return new ST_Agdesc_s();
		}
		// if (theClass == Agtag_s.class) {
		// return new ST_Agtag_s();
		// }
		if (theClass == ST_Agiddisc_s.class) {
			return new ST_Agiddisc_s();
		}
		if (theClass == ST_Agmemdisc_s.class) {
			return new ST_Agmemdisc_s();
		}
		if (theClass == ST_nlist_t.class) {
			return new ST_nlist_t();
		}
		if (theClass == ST_arrowtype_t.class) {
			return new ST_arrowtype_t();
		}
		if (theClass == ST_elist.class) {
			return new ST_elist();
		}
		if (theClass == ST_pointf.class) {
			return new ST_pointf();
		}
		if (theClass == ST_boxf.class) {
			return new ST_boxf();
		}
		if (theClass == ST_port.class) {
			return new ST_port();
		}
		if (theClass == ST_polygon_t.class) {
			return new ST_polygon_t();
		}
		if (theClass == ST_shape_functions.class) {
			return new ST_shape_functions();
		}
		if (theClass == ST_shape_desc.class) {
			return new ST_shape_desc();
		}
		// if (theClass == deque_t.class) {
		// return new ST_deque_t();
		// }
		// if (theClass == pointnlink_t.class) {
		// return new ST_pointnlink_t();
		// }
		if (theClass == ST_Ppoly_t.class) {
			return new ST_Ppoly_t();
		}
		if (theClass == ST_splineInfo.class) {
			return new ST_splineInfo();
		}
		if (theClass == ST_textfont_t.class) {
			return new ST_textfont_t();
		}
		//
		// if (UmlDiagram.SMETANA_BETA) {
		if (theClass == ST_Agsubnode_s.class) {
			return new ST_Agsubnode_s();
		}
		if (theClass == ST_dtlink_s.class) {
			return new ST_dtlink_s();
		}
		if (theClass == ST_refstr_t.class) {
			return new ST_refstr_t();
		}
		if (theClass == ST_refstr_t.class) {
			return new ST_refstr_t();
		}
		if (theClass == ST_Agsym_s.class) {
			return new ST_Agsym_s();
		}
		if (theClass == ST_Agedge_s.class) {
			return new ST_Agedge_s();
		}
		if (theClass == ST_Agobj_s.class) {
			return new ST_Agobj_s();
		}
		if (theClass == ST_Agrec_s.class) {
			return new ST_Agrec_s();
		}
		if (theClass == ST_Agraph_s.class) {
			return new ST_Agraph_s();
		}
		if (theClass == ST_Agclos_s.class) {
			return new ST_Agclos_s();
		}
		if (theClass == ST_Agdisc_s.class) {
			return new ST_Agdisc_s();
		}
		if (theClass == Agdstate_s.class) {
			return new ST_Agdstate_s();
		}
		if (theClass == Agiodisc_s.class) {
			return new ST_Agiodisc_s();
		}
		if (theClass == ST_dt_s.class) {
			return new ST_dt_s();
		}
		if (theClass == ST_dtdata_s.class) {
			return new ST_dtdata_s();
		}
		if (theClass == ST_Agdatadict_s.class) {
			return new ST_Agdatadict_s();
		}
		if (theClass == ST_Agattr_s.class) {
			return new ST_Agattr_s();
		}
		if (theClass == Agcbstack_s.class) {
			return new ST_Agcbstack_s();
		}
		if (theClass == ST_Agnode_s.class) {
			return new ST_Agnode_s();
		}
		if (theClass == ST_Agedgepair_s.class) {
			return new ST_Agedgepair_s();
		}
		if (theClass == ST_Agraphinfo_t.class) {
			return new ST_Agraphinfo_t();
		}
		if (theClass == ST_GVC_s.class) {
			return new ST_GVC_s();
		}
		if (theClass == GVCOMMON_t.class) {
			return new ST_GVCOMMON_t();
		}
		// if (theClass == ST_gvlayout_engine_s.class) {
		// return new ST_gvlayout_engine_s();
		// }
		// if (theClass == gvlayout_features_t.class) {
		// return new ST_gvlayout_features_t();
		// }
		// if (theClass == gvplugin_active_layout_t.class) {
		// return new ST_gvplugin_active_layout_t();
		// }
		// if (theClass == gvplugin_installed_t.class) {
		// return new ST_gvplugin_installed_t();
		// }
		if (theClass == layout_t.class) {
			return new ST_layout_t();
		}
		if (theClass == ST_Agnodeinfo_t.class) {
			return new ST_Agnodeinfo_t();
		}
		if (theClass == ST_textlabel_t.class) {
			return new ST_textlabel_t();
		}
		if (theClass == ST_textspan_t.class) {
			return new ST_textspan_t();
		}
		if (theClass == rank_t.class) {
			return new ST_rank_t();
		}
		// if (theClass == adjmatrix_t.class) {
		// return new ST_adjmatrix_t();
		// }
		if (theClass == ST_Agedgeinfo_t.class) {
			return new ST_Agedgeinfo_t();
		}
		if (theClass == ST_splines.class) {
			return new ST_splines();
		}
		if (theClass == ST_bezier.class) {
			return new ST_bezier();
		}
		if (theClass == ST_dthold_s.class) {
			return new ST_dthold_s();
		}
		//
		if (theClass == ST_pack_info.class) {
			return new ST_pack_info();
		}
		if (theClass == ST_aspect_t.class) {
			return new ST_aspect_t();
		}
		if (theClass == ST_fontinfo.class) {
			return new ST_fontinfo();
		}
		if (theClass == ST_IMapEntry_t.class) {
			return new ST_IMapEntry_t();
		}
		// if (theClass == point.class) {
		// return new ST_point();
		// }
		if (theClass == ST_nodequeue.class) {
			return new ST_nodequeue();
		}
		if (theClass == ST_spline_info_t.class) {
			return new ST_spline_info_t();
		}
		if (theClass == ST_path.class) {
			return new ST_path();
		}
		if (theClass == ST_pathend_t.class) {
			return new ST_pathend_t();
		}
		// if (theClass == inside_t.class) {
		// return new ST_inside_t();
		// }
		if (theClass == ST_triangle_t.class) {
			return new ST_triangle_t();
		}
		if (theClass == tedge_t.class) {
			return new ST_tedge_t();
		}
		if (theClass == ST_Pedge_t.class) {
			return new ST_Pedge_t();
		}
		if (theClass == ST_tna_t.class) {
			return new ST_tna_t();
		}
		if (theClass == ST_label_params_t.class) {
			return new ST_label_params_t();
		}
		if (theClass == ST_object_t.class) {
			return new ST_object_t();
		}
		if (theClass == ST_xlabel_t.class) {
			return new ST_xlabel_t();
		}
		if (theClass == ST_XLabels_t.class) {
			return new ST_XLabels_t();
		}
		if (theClass == ST_HDict_t.class) {
			return new ST_HDict_t();
		}
		if (theClass == ST_RTree.class) {
			return new ST_RTree();
		}
		if (theClass == ST_Node_t___.class) {
			return new ST_Node_t___();
		}
		if (theClass == ST_cinfo_t.class) {
			return new ST_cinfo_t();
		}
		// }
		notFound(theClass);
		throw new UnsupportedOperationException();
	}

	public static StarStruct create(Class theClass, StarStruct parent) {
		if (theClass == ST_dtmethod_s.class) {
			throw new IllegalArgumentException(theClass.toString());
		}
		if (theClass == ST_dtdisc_s.class) {
			return new ST_dtdisc_s(parent);
		}
		if (theClass == ST_Agdesc_s.class) {
			return new ST_Agdesc_s(parent);
		}
		if (theClass == ST_nlist_t.class) {
			return new ST_nlist_t(parent);
		}
		if (theClass == ST_elist.class) {
			return new ST_elist(parent);
		}
		if (theClass == ST_pointf.class) {
			return new ST_pointf(parent);
		}
		if (theClass == ST_boxf.class) {
			return new ST_boxf(parent);
		}
		if (theClass == ST_port.class) {
			return new ST_port(parent);
		}
		if (theClass == ST_polygon_t.class) {
			return new ST_polygon_t(parent);
		}
		if (theClass == ST_shape_functions.class) {
			return new ST_shape_functions(parent);
		}
		if (theClass == ST_shape_desc.class) {
			return new ST_shape_desc(parent);
		}
		// if (theClass == deque_t.class) {
		// return new ST_deque_t(parent);
		// }
		// if (theClass == pointnlink_t.class) {
		// return new ST_pointnlink_t(parent);
		// }
		if (theClass == ST_Ppoly_t.class) {
			return new ST_Ppoly_t(parent);
		}
		if (theClass == ST_splineInfo.class) {
			return new ST_splineInfo(parent);
		}
		if (theClass == ST_textfont_t.class) {
			return new ST_textfont_t(parent);
		}
		//
		// if (UmlDiagram.SMETANA_BETA) {
		if (theClass == ST_Agsubnode_s.class) {
			return new ST_Agsubnode_s(parent);
		}
		if (theClass == ST_dtlink_s.class) {
			return new ST_dtlink_s(parent);
		}
		if (theClass == ST_refstr_t.class) {
			return new ST_refstr_t();
		}
		if (theClass == ST_refstr_t.class) {
			return new ST_refstr_t();
		}
		if (theClass == ST_Agsym_s.class) {
			return new ST_Agsym_s(parent);
		}
		if (theClass == ST_Agedge_s.class) {
			return new ST_Agedge_s(parent);
		}
		if (theClass == ST_Agobj_s.class) {
			return new ST_Agobj_s(parent);
		}
		if (theClass == ST_Agrec_s.class) {
			return new ST_Agrec_s(parent);
		}
		if (theClass == ST_Agraph_s.class) {
			return new ST_Agraph_s(parent);
		}
		if (theClass == ST_Agclos_s.class) {
			return new ST_Agclos_s(parent);
		}
		if (theClass == ST_Agdisc_s.class) {
			return new ST_Agdisc_s(parent);
		}
		if (theClass == Agdstate_s.class) {
			return new ST_Agdstate_s(parent);
		}
		if (theClass == ST_dt_s.class) {
			return new ST_dt_s(parent);
		}
		if (theClass == ST_dtdata_s.class) {
			return new ST_dtdata_s(parent);
		}
		if (theClass == ST_Agdatadict_s.class) {
			return new ST_Agdatadict_s(parent);
		}
		if (theClass == ST_Agattr_s.class) {
			return new ST_Agattr_s(parent);
		}
		if (theClass == Agcbstack_s.class) {
			return new ST_Agcbstack_s(parent);
		}
		if (theClass == ST_Agnode_s.class) {
			return new ST_Agnode_s(parent);
		}
		if (theClass == ST_Agedgepair_s.class) {
			return new ST_Agedgepair_s(parent);
		}
		if (theClass == ST_Agraphinfo_t.class) {
			return new ST_Agraphinfo_t(parent);
		}
		if (theClass == ST_GVC_s.class) {
			return new ST_GVC_s(parent);
		}
		if (theClass == GVCOMMON_t.class) {
			return new ST_GVCOMMON_t(parent);
		}
		// if (theClass == ST_gvlayout_engine_s.class) {
		// throw new UnsupportedOperationException();
		// // return new ST_gvlayout_engine_s(parent);
		// }
		// if (theClass == gvlayout_features_t.class) {
		// throw new UnsupportedOperationException();
		// // return new ST_gvlayout_features_t(parent);
		// }
		// if (theClass == gvplugin_active_layout_t.class) {
		// throw new UnsupportedOperationException();
		// // return new ST_gvplugin_active_layout_t(parent);
		// }
		// if (theClass == gvplugin_installed_t.class) {
		// return new ST_gvplugin_installed_t(parent);
		// }
		if (theClass == layout_t.class) {
			return new ST_layout_t(parent);
		}
		if (theClass == ST_Agnodeinfo_t.class) {
			return new ST_Agnodeinfo_t(parent);
		}
		if (theClass == ST_textlabel_t.class) {
			return new ST_textlabel_t(parent);
		}
		if (theClass == ST_textspan_t.class) {
			return new ST_textspan_t(parent);
		}
		if (theClass == rank_t.class) {
			return new ST_rank_t(parent);
		}
		// if (theClass == adjmatrix_t.class) {
		// return new ST_adjmatrix_t(parent);
		// }
		if (theClass == ST_Agedgeinfo_t.class) {
			return new ST_Agedgeinfo_t(parent);
		}
		if (theClass == ST_splines.class) {
			return new ST_splines(parent);
		}
		if (theClass == ST_bezier.class) {
			return new ST_bezier(parent);
		}
		if (theClass == ST_dthold_s.class) {
			return new ST_dthold_s(parent);
		}
		//
		if (theClass == ST_pack_info.class) {
			return new ST_pack_info(parent);
		}
		if (theClass == ST_aspect_t.class) {
			return new ST_aspect_t(parent);
		}
		if (theClass == ST_fontinfo.class) {
			return new ST_fontinfo(parent);
		}
		if (theClass == ST_IMapEntry_t.class) {
			return new ST_IMapEntry_t(parent);
		}
		// if (theClass == point.class) {
		// return new ST_point(parent);
		// }
		if (theClass == ST_nodequeue.class) {
			return new ST_nodequeue(parent);
		}
		if (theClass == ST_spline_info_t.class) {
			return new ST_spline_info_t(parent);
		}
		if (theClass == ST_path.class) {
			return new ST_path(parent);
		}
		if (theClass == ST_pathend_t.class) {
			return new ST_pathend_t(parent);
		}
		// if (theClass == inside_t.class) {
		// return new ST_inside_t(parent);
		// }
		if (theClass == ST_triangle_t.class) {
			return new ST_triangle_t(parent);
		}
		if (theClass == tedge_t.class) {
			return new ST_tedge_t(parent);
		}
		if (theClass == ST_Pedge_t.class) {
			return new ST_Pedge_t(parent);
		}
		if (theClass == ST_tna_t.class) {
			return new ST_tna_t(parent);
		}
		if (theClass == ST_label_params_t.class) {
			return new ST_label_params_t(parent);
		}
		if (theClass == ST_object_t.class) {
			return new ST_object_t(parent);
		}
		if (theClass == ST_xlabel_t.class) {
			return new ST_xlabel_t(parent);
		}
		if (theClass == ST_XLabels_t.class) {
			return new ST_XLabels_t(parent);
		}
		if (theClass == ST_HDict_t.class) {
			return new ST_HDict_t(parent);
		}
		if (theClass == ST_RTree.class) {
			return new ST_RTree(parent);
		}
		if (theClass == ST_Node_t___.class) {
			return new ST_Node_t___(parent);
		}
		if (theClass == ST_cinfo_t.class) {
			return new ST_cinfo_t(parent);
		}
		// }
		notFound(theClass);
		throw new UnsupportedOperationException();
	}

	private static final Set<String> todo = new LinkedHashSet<String>();

	private static void notFound(Class theClass) {
		todo.add(theClass.getName());
		System.err.println("todo=" + todo);
	}

}
