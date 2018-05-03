/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 * 
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the Eclipse Public License.
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
 */
package net.sourceforge.plantuml.jdot;

import h.Agedge_s;
import h.Agedgeinfo_t;
import h.Agnode_s;
import h.Agnodeinfo_t;
import h.bezier;
import h.boxf;
import h.pointf;
import h.splines;
import h.textlabel_t;
import smetana.core.Macro;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class DebugUtils {

	public static void printDebugEdge(Agedge_s e) {
		System.err.println("*********** PRINT EDGE ********** " + getUID(e));
		final Agedgeinfo_t data = (Agedgeinfo_t) Macro.AGDATA(e).castTo(Agedgeinfo_t.class);
		final splines splines = (splines) data.getPtr("spl");
		__struct__<boxf> bb = splines.getStruct("bb");
		// final bezier list = (bezier) splines.getPtr("list");
		System.err.println("splines.UID=" + ((StarStruct) splines).getUID36());
		System.err.println("splines.size=" + splines.getInt("size"));
		System.err.println("bb.LL=" + pointftoString(bb.getStruct("LL")));
		System.err.println("bb.UR=" + pointftoString(bb.getStruct("UR")));
		printDebugBezier((bezier) splines.getPtr("list").getPtr());

		textlabel_t label = (textlabel_t) data.getPtr("label");
		if (label != null) {
			System.err.println("LABEL dimen=" + pointftoString(label.getStruct("dimen")));
			System.err.println("LABEL space=" + pointftoString(label.getStruct("space")));
			System.err.println("LABEL pos=" + pointftoString(label.getStruct("pos")));
		}

	}

	public static String getUID(Object o) {
		if (o instanceof StarArrayOfPtr) {
			return ((StarArrayOfPtr) o).getUID36();
		}
		return ((StarStruct) o).getUID36();
	}

	public static void printDebugBezier(bezier bezier) {
		System.err.println("bezier.size=" + bezier.getInt("size"));
		System.err.println("bezier.sflag=" + bezier.getInt("sflag"));
		System.err.println("splines.eflag=" + bezier.getInt("eflag"));
		System.err.println("bezier.sp=" + pointftoString(bezier.getStruct("sp")));
		System.err.println("bezier.ep=" + pointftoString(bezier.getStruct("ep")));
		System.err.println("bezier.list=" + getUID(bezier.getPtr("list")));
		for (int i = 0; i < bezier.getInt("size"); i++) {
			final __ptr__ pt = bezier.getPtr("list").plus(i).getPtr();
			System.err.println("pt=" + pointftoString(pt));
		}

	}

	public static void printDebugNode(Agnode_s n) {
		System.err.println("*********** PRINT NODE ********** ");
		final Agnodeinfo_t data = (Agnodeinfo_t) Macro.AGDATA(n).castTo(Agnodeinfo_t.class);
		System.err.println("width=" + data.getDouble("width"));
		System.err.println("height=" + data.getDouble("height"));
		System.err.println("ht=" + data.getDouble("ht"));
		System.err.println("lw=" + data.getDouble("lw"));
		System.err.println("rw=" + data.getDouble("rw"));
		System.err.println("coord=" + pointftoString(data.getStruct("coord")));

		__struct__<boxf> bb = data.getStruct("bb");
		System.err.println("bb.LL=" + pointftoString(bb.getStruct("LL")));
		System.err.println("bb.UR=" + pointftoString(bb.getStruct("UR")));
		// TODO Auto-generated method stub
	}

	public static String pointftoString(__struct__<pointf> point) {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(point.getDouble("x"));
		sb.append(" ; ");
		sb.append(point.getDouble("y"));
		sb.append(")");
		return sb.toString();

	}

	public static String pointftoString(__ptr__ point) {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(point.getDouble("x"));
		sb.append(" ; ");
		sb.append(point.getDouble("y"));
		sb.append(")");
		return sb.toString();
	}
}
