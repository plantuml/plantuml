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
 * (C) Copyright 2009-2022, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program:
 * 
 *************************************************************************
 * Copyright (c) 2011 AT&T Intellectual Property 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: See CVS logs. Details at http://www.graphviz.org/
 *************************************************************************
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
package gen.lib.common;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.common.geom__c.ptToLine2;
import static gen.lib.common.utils__c.Bezier;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agraph_s;
import h.ST_boxf;
import h.ST_pointf;
import smetana.core.CArray;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.ZType;
import smetana.core.__ptr__;

public class emit__c {




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/emit.c", name="init_xdot", key="7udip7yo3ddkp9ocjftokpm9y", definition="void* init_xdot (Agraph_t* g)")
public static __ptr__ init_xdot(Globals zz, ST_Agraph_s g) {
ENTERING("7udip7yo3ddkp9ocjftokpm9y","init_xdot");
try {
    CString p;
    __ptr__ xd = null;
	if (!((p = agget(zz, g, new CString("_background")))!=null && p.charAt(0)!='\0')) {
	if (!((p = agget(zz, g, new CString("_draw_")))!=null  && p.charAt(0)!='\0')) {
	    return null;
	}
    }
UNSUPPORTED("16fu50ud9qppkwxzdy0nde3lm"); //     xd = parseXDotF (p, NULL, sizeof (exdot_op));
UNSUPPORTED("1x2xrqe9on9i2dlb07gj02n65"); //     if (!xd) {
UNSUPPORTED("b4emzm37tsv7edlai0fhwoul1"); // 	agerr(AGWARN, "Could not parse \"_background\" attribute in graph %s\n", agnameof(g));
UNSUPPORTED("72v3r8ey2hvh1o9qskrji8im4"); // 	agerr(AGPREV, "  \"%s\"\n", p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("jujeh27uxxeyas8n09tnlnbu"); //     return xd;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("7udip7yo3ddkp9ocjftokpm9y","init_xdot");
}
}



/* check_control_points:
 * check_control_points function checks the size of quadrilateral
 * formed by four control points
 * returns 1 if four points are in line (or close to line)
 * else return 0
 */
//3 7nqmdkcnal35ollpstkk707t8
// static int check_control_points(pointf *cp) 
@Unused
@Original(version="2.38.0", path="lib/common/emit.c", name="check_control_points", key="7nqmdkcnal35ollpstkk707t8", definition="static int check_control_points(pointf *cp)")
public static boolean check_control_points(CArray<ST_pointf> cp) {
ENTERING("7nqmdkcnal35ollpstkk707t8","check_control_points");
try {
    double dis1 = ptToLine2 (cp.get__(0), cp.get__(3), cp.get__(1));
    double dis2 = ptToLine2 (cp.get__(0), cp.get__(3), cp.get__(2));
    if (dis1 < 2.0*2.0 && dis2 < 2.0*2.0)
        return true;
    else
        return false;
} finally {
LEAVING("7nqmdkcnal35ollpstkk707t8","check_control_points");
}
}




/* update bounding box to contain a bezier segment */
//3 5wldemr88fdxl6101ugewclw9
// void update_bb_bz(boxf *bb, pointf *cp) 
@Unused
@Original(version="2.38.0", path="lib/common/emit.c", name="update_bb_bz", key="5wldemr88fdxl6101ugewclw9", definition="void update_bb_bz(boxf *bb, pointf *cp)")
public static void update_bb_bz(ST_boxf bb, CArray<ST_pointf> cp) {
ENTERING("5wldemr88fdxl6101ugewclw9","update_bb_bz");
try {
    /* if any control point of the segment is outside the bounding box */
    if (cp.get__(0).x > bb.UR.x || cp.get__(0).x < bb.LL.x ||
        cp.get__(0).y > bb.UR.y || cp.get__(0).y < bb.LL.y ||
        cp.get__(1).x > bb.UR.x || cp.get__(1).x < bb.LL.x ||
        cp.get__(1).y > bb.UR.y || cp.get__(1).y < bb.LL.y ||
        cp.get__(2).x > bb.UR.x || cp.get__(2).x < bb.LL.x ||
        cp.get__(2).y > bb.UR.y || cp.get__(2).y < bb.LL.y ||
        cp.get__(3).x > bb.UR.x || cp.get__(3).x < bb.LL.x ||
        cp.get__(3).y > bb.UR.y || cp.get__(3).y < bb.LL.y) {
    	
        /* if the segment is sufficiently refined */
        if (check_control_points(cp)) {        
            int i;
            /* expand the bounding box */
            for (i = 0; i < 4; i++) {
                if (cp.get__(i).x > bb.UR.x)
                    bb.UR.x = cp.get__(i).x;
                else if (cp.get__(i).x < bb.LL.x)
                    bb.LL.x = cp.get__(i).x;
                if (cp.get__(i).y > bb.UR.y)
                    bb.UR.y = cp.get__(i).y;
                else if (cp.get__(i).y < bb.LL.y)
                    bb.LL.y = cp.get__(i).y;
            }
        }
        else { /* else refine the segment */
		    final CArray<ST_pointf> left = CArray.<ST_pointf>ALLOC__(4, ZType.ST_pointf);
		    final CArray<ST_pointf> right = CArray.<ST_pointf>ALLOC__(4, ZType.ST_pointf);
            Bezier (cp, 3, 0.5, left, right);
            update_bb_bz(bb, left);
            update_bb_bz(bb, right);
        }
    }
} finally {
LEAVING("5wldemr88fdxl6101ugewclw9","update_bb_bz");
}
}




@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/common/emit.c", name="gv_fixLocale", key="31vgctm6ydke1b6e0s06x85og", definition="void gv_fixLocale (int set)")
public static void gv_fixLocale(int set) {
ENTERING("31vgctm6ydke1b6e0s06x85og","gv_fixLocale");
try {
	// System.out.println("SKIPPING gv_fixLocale");
} finally {
LEAVING("31vgctm6ydke1b6e0s06x85og","gv_fixLocale");
}
}



}
