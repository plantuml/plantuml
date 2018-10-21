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

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_pointf;
import h.ST_splines;
import h.ST_textlabel_t;
import smetana.core.Macro;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class DebugUtils {

	public static void printDebugEdge(ST_Agedge_s e) {
		System.err.println("*********** PRINT EDGE ********** " + getUID(e));
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) Macro.AGDATA(e).castTo(ST_Agedgeinfo_t.class);
		final ST_splines splines = (ST_splines) data.spl;
		ST_boxf bb = (ST_boxf) splines.getStruct("bb");
		// final bezier list = (bezier) splines.getPtr("list");
		System.err.println("splines.UID=" + ((StarStruct) splines).getUID36());
		System.err.println("splines.size=" + splines.size);
		System.err.println("bb.LL=" + pointftoString((ST_pointf) bb.getStruct("LL")));
		System.err.println("bb.UR=" + pointftoString((ST_pointf) bb.getStruct("UR")));
		printDebugBezier((ST_bezier) splines.getPtr("list").getPtr());

		ST_textlabel_t label = (ST_textlabel_t) data.label;
		if (label != null) {
			System.err.println("LABEL dimen=" + pointftoString((ST_pointf) label.dimen));
			System.err.println("LABEL space=" + pointftoString((ST_pointf) label.space));
			System.err.println("LABEL pos=" + pointftoString((ST_pointf) label.getStruct("pos")));
		}

	}

	public static String getUID(Object o) {
		return ((StarStruct) o).getUID36();
	}

	public static void printDebugBezier(ST_bezier bezier) {
		System.err.println("bezier.size=" + bezier.size);
		System.err.println("bezier.sflag=" + bezier.getInt("sflag"));
		System.err.println("splines.eflag=" + bezier.getInt("eflag"));
		System.err.println("bezier.sp=" + pointftoString((ST_pointf) bezier.getStruct("sp")));
		System.err.println("bezier.ep=" + pointftoString((ST_pointf) bezier.getStruct("ep")));
		System.err.println("bezier.list=" + getUID(bezier.getPtr("list")));
		for (int i = 0; i < bezier.size; i++) {
			final __ptr__ pt = bezier.getPtr("list").plus(i).getPtr();
			System.err.println("pt=" + pointftoString(pt));
		}

	}

	public static void printDebugNode(ST_Agnode_s n) {
		System.err.println("*********** PRINT NODE ********** ");
		final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) Macro.AGDATA(n).castTo(ST_Agnodeinfo_t.class);
		System.err.println("width=" + data.getDouble("width"));
		System.err.println("height=" + data.getDouble("height"));
		System.err.println("ht=" + data.getDouble("ht"));
		System.err.println("lw=" + data.getDouble("lw"));
		System.err.println("rw=" + data.getDouble("rw"));
		System.err.println("coord=" + pointftoString((ST_pointf) data.getStruct("coord")));

		ST_boxf bb = (ST_boxf) data.getStruct("bb");
		System.err.println("bb.LL=" + pointftoString((ST_pointf) bb.getStruct("LL")));
		System.err.println("bb.UR=" + pointftoString((ST_pointf) bb.getStruct("UR")));
		// TODO Auto-generated method stub
	}

	public static String pointftoString(ST_pointf point) {
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
