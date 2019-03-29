/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
import h.ST_pointf;
import h.ST_splines;
import h.ST_textlabel_t;
import smetana.core.Macro;
import smetana.core.amiga.StarStruct;

public class DebugUtils {

	public static void printDebugEdge(ST_Agedge_s e) {
		System.err.println("*********** PRINT EDGE ********** " + getUID(e));
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) Macro.AGDATA(e).castTo(ST_Agedgeinfo_t.class);
		final ST_splines splines = (ST_splines) data.spl;
		//ST_boxf bb = (ST_boxf) splines.bb;
		// final bezier list = (bezier) splines.getPtr("list");
		System.err.println("splines.UID=" + ((StarStruct) splines).getUID36());
		System.err.println("splines.size=" + splines.size);
		//System.err.println("bb.LL=" + pointftoString((ST_pointf) bb.LL));
		//System.err.println("bb.UR=" + pointftoString((ST_pointf) bb.UR));
		printDebugBezier(splines.list.getPtr());

		ST_textlabel_t label = data.label;
		if (label != null) {
			System.err.println("LABEL dimen=" + pointftoString(label.dimen));
			System.err.println("LABEL space=" + pointftoString(label.space));
			System.err.println("LABEL pos=" + pointftoString(label.pos));
		}

	}

	public static String getUID(Object o) {
		return ((StarStruct) o).getUID36();
	}

	public static void printDebugBezier(ST_bezier bezier) {
		System.err.println("bezier.size=" + bezier.size);
		System.err.println("bezier.sflag=" + bezier.sflag);
		System.err.println("splines.eflag=" + bezier.eflag);
		System.err.println("bezier.sp=" + pointftoString((ST_pointf) bezier.sp));
		System.err.println("bezier.ep=" + pointftoString((ST_pointf) bezier.ep));
		System.err.println("bezier.list=" + getUID(bezier.list.getPtr()));
		for (int i = 0; i < bezier.size; i++) {
			final ST_pointf pt = bezier.list.get(i);
			System.err.println("pt=" + pointftoString(pt));
		}

	}

	public static void printDebugNode(ST_Agnode_s n) {
		System.err.println("*********** PRINT NODE ********** ");
		final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) Macro.AGDATA(n).castTo(ST_Agnodeinfo_t.class);
		System.err.println("width=" + data.width);
		System.err.println("height=" + data.height);
		System.err.println("ht=" + data.ht);
		System.err.println("lw=" + data.lw);
		System.err.println("rw=" + data.rw);
		System.err.println("coord=" + pointftoString((ST_pointf) data.coord));

		//ST_boxf bb = (ST_boxf) data.bb;
		//System.err.println("bb.LL=" + pointftoString((ST_pointf) bb.LL));
		//System.err.println("bb.UR=" + pointftoString((ST_pointf) bb.UR));
		// TODO Auto-generated method stub
	}

	public static String pointftoString(ST_pointf point) {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(point.x);
		sb.append(" ; ");
		sb.append(point.y);
		sb.append(")");
		return sb.toString();

	}
}
