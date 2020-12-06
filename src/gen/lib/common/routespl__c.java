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
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.pathplan.route__c.Proutespline;
import static gen.lib.pathplan.shortest__c.Pshortestpath;
import static gen.lib.pathplan.util__c.make_polyline;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.cos;
import static smetana.core.JUtils.sin;
import static smetana.core.Macro.ABS;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.INT_MIN;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Pedge_t;
import h.ST_Ppoly_t;
import h.ST_boxf;
import h.ST_path;
import h.ST_pointf;
import smetana.core.CArray;
import smetana.core.Memory;
import smetana.core.Z;

public class routespl__c {



//3 7ebl6qohcfpf1b9ucih5r9qgp
// pointf* simpleSplineRoute (pointf tp, pointf hp, Ppoly_t poly, int* n_spl_pts,     int polyline) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="simpleSplineRoute", key="7ebl6qohcfpf1b9ucih5r9qgp", definition="pointf* simpleSplineRoute (pointf tp, pointf hp, Ppoly_t poly, int* n_spl_pts,     int polyline)")
public static CArray<ST_pointf> simpleSplineRoute(final ST_pointf tp, final ST_pointf hp, final ST_Ppoly_t poly, int[] n_spl_pts, boolean polyline) {
// WARNING!! STRUCT
return simpleSplineRoute_w_(tp.copy(), hp.copy(), (ST_Ppoly_t) poly.copy(), n_spl_pts, polyline);
}
private static CArray<ST_pointf> simpleSplineRoute_w_(final ST_pointf tp, final ST_pointf hp, final ST_Ppoly_t poly, int[] n_spl_pts, boolean polyline) {
ENTERING("7ebl6qohcfpf1b9ucih5r9qgp","simpleSplineRoute");
try {
    final ST_Ppoly_t pl = new ST_Ppoly_t(), spl = new ST_Ppoly_t();
    final CArray<ST_pointf> eps = CArray.<ST_pointf>ALLOC__(2, ST_pointf.class);
    final CArray<ST_pointf> evs = CArray.<ST_pointf>ALLOC__(2, ST_pointf.class);
    int i;
    eps.get__(0).x = tp.x;
    eps.get__(0).y = tp.y;
    eps.get__(1).x = hp.x;
    eps.get__(1).y = hp.y;
    if (Pshortestpath(poly, eps, pl) < 0)
        return null;
    if (polyline)
	make_polyline (pl, spl);
    else {
	if (poly.pn > Z.z().edgen) {
	    Z.z().edges = CArray.<ST_Pedge_t>REALLOC__(poly.pn, Z.z().edges, ST_Pedge_t.class);
	    Z.z().edgen = poly.pn;
	}
	for (i = 0; i < poly.pn; i++) {
	    Z.z().edges.get__(i).a.___(poly.ps.get__(i));
	    Z.z().edges.get__(i).b.___(poly.ps.get__((i + 1) % poly.pn));
	}
	    evs.get__(0).x = 0;
	    evs.get__(0).y = 0;
	    evs.get__(1).x = 0;
	    evs.get__(1).y = 0;
	if (Proutespline(Z.z().edges, poly.pn, pl, evs, spl) < 0)
            return null;
    }
    if (mkspacep(spl.pn))
	return null;
    for (i = 0; i < spl.pn; i++) {
        Z.z().ps.get__(i).___(spl.ps.get__(i));
    }
    n_spl_pts[0] = spl.pn;
    return Z.z().ps;
} finally {
LEAVING("7ebl6qohcfpf1b9ucih5r9qgp","simpleSplineRoute");
}
}




/* routesplinesinit:
 * Data initialized once until matching call to routeplineterm
 * Allows recursive calls to dot
 */
//3 bfsrazjf3vkf12stnke48vc8t
// int routesplinesinit() 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="routesplinesinit", key="bfsrazjf3vkf12stnke48vc8t", definition="int routesplinesinit()")
public static int routesplinesinit() {
ENTERING("bfsrazjf3vkf12stnke48vc8t","routesplinesinit");
try {
    if (++Z.z().routeinit > 1) return 0;
    if (N(Z.z().ps = CArray.<ST_pointf>ALLOC__(300, ST_pointf.class))) {
UNSUPPORTED("2qoo3na2ur9oh7hmvt6xv1txd"); // 	agerr(AGERR, "routesplinesinit: cannot allocate ps\n");
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
    }
    Z.z().maxpn = 300;
    Z.z().nedges = 0;
    Z.z().nboxes = 0;
    /*if (Verbose)
	start_timer();*/
    return 0;
} finally {
LEAVING("bfsrazjf3vkf12stnke48vc8t","routesplinesinit");
}
}




//3 55j3tny5cxemrsvrt3m21jxg8
// void routesplinesterm() 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="routesplinesterm", key="55j3tny5cxemrsvrt3m21jxg8", definition="void routesplinesterm()")
public static void routesplinesterm() {
ENTERING("55j3tny5cxemrsvrt3m21jxg8","routesplinesterm");
try {
    if (--Z.z().routeinit > 0) return;
    Memory.free(Z.z().ps);
    /*if (Verbose)
	fprintf(stderr,
		"routesplines: %d edges, %d boxes %.2f sec\n",
		nedges, nboxes, elapsed_sec());*/
} finally {
LEAVING("55j3tny5cxemrsvrt3m21jxg8","routesplinesterm");
}
}




//3 cu8ssjizw7ileqe9u7tcclq7k
// static void limitBoxes (boxf* boxes, int boxn, pointf *pps, int pn, int delta) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="limitBoxes", key="cu8ssjizw7ileqe9u7tcclq7k", definition="static void limitBoxes (boxf* boxes, int boxn, pointf *pps, int pn, int delta)")
public static void limitBoxes(ST_boxf boxes[], int boxn, CArray<ST_pointf> pps, int pn, int delta) {
ENTERING("cu8ssjizw7ileqe9u7tcclq7k","limitBoxes");
try {
    int bi, si, splinepi;
    double t;
    final CArray<ST_pointf> sp = CArray.<ST_pointf>ALLOC__(4, ST_pointf.class);
    int num_div = delta * boxn;
    for (splinepi = 0; splinepi + 3 < pn; splinepi += 3) {
	for (si = 0; si <= num_div; si++) {
	    t = si / (double)num_div;
	    sp.get__(0).___(pps.get__(splinepi));
	    sp.get__(1).___(pps.get__(splinepi+1));
	    sp.get__(2).___(pps.get__(splinepi+2));
	    sp.get__(3).___(pps.get__(splinepi+3));
	    sp.get__(0).x = sp.get__(0).x + t * (sp.get__(1).x - sp.get__(0).x);
	    sp.get__(0).y = sp.get__(0).y + t * (sp.get__(1).y - sp.get__(0).y);
	    sp.get__(1).x = sp.get__(1).x + t * (sp.get__(2).x - sp.get__(1).x);
	    sp.get__(1).y = sp.get__(1).y + t * (sp.get__(2).y - sp.get__(1).y);
	    sp.get__(2).x = sp.get__(2).x + t * (sp.get__(3).x - sp.get__(2).x);
	    sp.get__(2).y = sp.get__(2).y + t * (sp.get__(3).y - sp.get__(2).y);
 	    sp.get__(0).x = sp.get__(0).x + t * (sp.get__(1).x - sp.get__(0).x);
	    sp.get__(0).y = sp.get__(0).y + t * (sp.get__(1).y - sp.get__(0).y);
	    sp.get__(1).x = sp.get__(1).x + t * (sp.get__(2).x - sp.get__(1).x);
	    sp.get__(1).y = sp.get__(1).y + t * (sp.get__(2).y - sp.get__(1).y);
	    sp.get__(0).x = sp.get__(0).x + t * (sp.get__(1).x - sp.get__(0).x);
	    sp.get__(0).y = sp.get__(0).y + t * (sp.get__(1).y - sp.get__(0).y);
	    for (bi = 0; bi < boxn; bi++) {
/* this tested ok on 64bit machines, but on 32bit we need this FUDGE
 *     or graphs/directed/records.gv fails */
		if (sp.get__(0).y <= boxes[bi].UR.y+.0001 && sp.get__(0).y >= boxes[bi].LL.y-.0001) {
		    if (boxes[bi].LL.x > sp.get__(0).x)
			boxes[bi].LL.x = sp.get__(0).x;
		    if (boxes[bi].UR.x < sp.get__(0).x)
			boxes[bi].UR.x = sp.get__(0).x;
		}
	    }
	}
    }
} finally {
LEAVING("cu8ssjizw7ileqe9u7tcclq7k","limitBoxes");
}
}




//3 3mcnemqisisnqtd4mr72ej76y
// static pointf *_routesplines(path * pp, int *npoints, int polyline) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="", key="3mcnemqisisnqtd4mr72ej76y", definition="static pointf *_routesplines(path * pp, int *npoints, int polyline)")
public static CArray<ST_pointf> _routesplines(ST_path pp, int npoints[], int polyline) {
ENTERING("3mcnemqisisnqtd4mr72ej76y","_routesplines");
try {
    final ST_Ppoly_t poly = new ST_Ppoly_t();
    final ST_Ppoly_t pl  = new ST_Ppoly_t(), spl = new ST_Ppoly_t();
    int splinepi;
    final CArray<ST_pointf> eps = CArray.<ST_pointf>ALLOC__(2, ST_pointf.class);
    final CArray<ST_pointf> evs = CArray.<ST_pointf>ALLOC__(2, ST_pointf.class);
    int edgei, prev, next;
    int pi=0, bi;
    ST_boxf[] boxes;
    int boxn;
    ST_Agedge_s realedge;
    int flip;
    int loopcnt, delta = 10;
    boolean unbounded;
    
    Z.z().nedges++;
    Z.z().nboxes += pp.nbox;
    
    for (realedge = (ST_Agedge_s) pp.data;
	 realedge!=null && ED_edge_type(realedge) != 0;
	 realedge = ED_to_orig(realedge));
    if (N(realedge)) {
	UNSUPPORTED("agerr(AGERR, _in routesplines, cannot find NORMAL edge");
	return null;
    }
    
    boxes = pp.boxes;
    boxn = pp.nbox;
    
    if (checkpath(boxn, boxes, pp)!=0)
	return null;
    
    if (boxn * 8 > Z.z().polypointn) {
	Z.z().polypoints = CArray.<ST_pointf>REALLOC__(boxn * 8, Z.z().polypoints, ST_pointf.class);
	Z.z().polypointn = boxn * 8;
    }
    
    if ((boxn > 1) && (((ST_boxf)boxes[0]).LL.y > ((ST_boxf)boxes[1]).LL.y)) {
        flip = 1;
	for (bi = 0; bi < boxn; bi++) {
	    double v = ((ST_boxf)boxes[bi]).UR.y;
	    ((ST_boxf)boxes[bi]).UR.y= -1*((ST_boxf)boxes[bi]).LL.y;
	    ((ST_boxf)boxes[bi]).LL.y = -v;
	}
    }
    else flip = 0;
    
    if (NEQ(agtail(realedge), aghead(realedge))) {
	/* I assume that the path goes either down only or
	   up - right - down */
	for (bi = 0, pi = 0; bi < boxn; bi++) {
	    next = prev = 0;
	    if (bi > 0)
		prev = (((ST_boxf)boxes[bi]).LL.y > ((ST_boxf)boxes[bi-1]).LL.y) ? -1 : 1;
	    if (bi < boxn - 1)
		next = (((ST_boxf)boxes[bi+1]).LL.y > ((ST_boxf)boxes[bi]).LL.y) ? 1 : -1;
	    if (prev != next) {
		if (next == -1 || prev == 1) {
		    Z.z().polypoints.get__(pi).x = boxes[bi].LL.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].UR.y;
		    Z.z().polypoints.get__(pi).x = boxes[bi].LL.x;
		    Z.z().polypoints.get__(pi++).y =boxes[bi].LL.y;
		} else {
		    Z.z().polypoints.get__(pi).x = boxes[bi].UR.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].LL.y;
		    Z.z().polypoints.get__(pi).x = boxes[bi].UR.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].UR.y;
		}
	    }
	    else if (prev == 0) { /* single box */
UNSUPPORTED("2bfai79qe7cec0rljrn56jg2f"); // 		polypoints[pi].x = boxes[bi].LL.x;
UNSUPPORTED("cjppvcr7k9pknjrjugccsky56"); // 		polypoints[pi++].y = boxes[bi].UR.y;
UNSUPPORTED("2bfai79qe7cec0rljrn56jg2f"); // 		polypoints[pi].x = boxes[bi].LL.x;
UNSUPPORTED("99xeozpks5v0iza4sv2occuuq"); // 		polypoints[pi++].y = boxes[bi].LL.y;
	    } 
	    else {
 		if (N(prev == -1 && next == -1)) {
UNSUPPORTED("cgpvvfb9phbipyhij0cjh1nmi"); // 		    agerr(AGERR, "in routesplines, illegal values of prev %d and next %d, line %d\n", prev, next, 444);
UNSUPPORTED("9idk92zg2ysz316lfwzvvvde6"); // 		    return NULL;
 		}
	    }
	}
	for (bi = boxn - 1; bi >= 0; bi--) {
	    next = prev = 0;
	    if (bi < boxn - 1)
		prev = (((ST_boxf)boxes[bi]).LL.y > ((ST_boxf)boxes[bi+1]).LL.y) ? -1 : 1;
	    if (bi > 0)
		next = (((ST_boxf)boxes[bi-1]).LL.y > ((ST_boxf)boxes[bi]).LL.y) ? 1 : -1;
	    if (prev != next) {
		if (next == -1 || prev == 1 ) {
		    Z.z().polypoints.get__(pi).x = boxes[bi].LL.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].UR.y;
		    Z.z().polypoints.get__(pi).x = boxes[bi].LL.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].LL.y;
		} else {
		    Z.z().polypoints.get__(pi).x = boxes[bi].UR.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].LL.y;
		    Z.z().polypoints.get__(pi).x = boxes[bi].UR.x;
		    Z.z().polypoints.get__(pi++).y = boxes[bi].UR.y;
		}
	    } 
	    else if (prev == 0) { /* single box */
UNSUPPORTED("ya84m81ogarx28l99om39lba"); // 		polypoints[pi].x = boxes[bi].UR.x;
UNSUPPORTED("99xeozpks5v0iza4sv2occuuq"); // 		polypoints[pi++].y = boxes[bi].LL.y;
UNSUPPORTED("ya84m81ogarx28l99om39lba"); // 		polypoints[pi].x = boxes[bi].UR.x;
UNSUPPORTED("cjppvcr7k9pknjrjugccsky56"); // 		polypoints[pi++].y = boxes[bi].UR.y;
	    }
	    else {
		if (N(prev == -1 && next == -1)) {
UNSUPPORTED("87y5d0ts6xdjyx905bha50f3s"); // 		    /* it went badly, e.g. degenerate box in boxlist */
UNSUPPORTED("1qt7hixteu3pt64wk1sqw352a"); // 		    agerr(AGERR, "in routesplines, illegal values of prev %d and next %d, line %d\n", prev, next, 476);
UNSUPPORTED("35untdbpd42pt4c74gjbxqx7q"); // 		    return NULL; /* for correctness sake, it's best to just stop */
		}
		Z.z().polypoints.get__(pi).x = boxes[bi].UR.x;
		Z.z().polypoints.get__(pi++).y = boxes[bi].LL.y;
		Z.z().polypoints.get__(pi).x = boxes[bi].UR.x;
		Z.z().polypoints.get__(pi++).y = boxes[bi].UR.y;
		Z.z().polypoints.get__(pi).x = boxes[bi].LL.x;
		Z.z().polypoints.get__(pi++).y = boxes[bi].UR.y;
		Z.z().polypoints.get__(pi).x = boxes[bi].LL.x;
		Z.z().polypoints.get__(pi++).y = boxes[bi].LL.y;
	    }
	}
    }
    else {
UNSUPPORTED("1izvmtfwbnl5xq4u2x5fdraxp"); // 	agerr(AGERR, "in routesplines, edge is a loop at %s\n", agnameof(aghead(realedge)));
UNSUPPORTED("11hwqop4xebvtcskop4uhpp01"); // 	return NULL;
    }
    
    if (flip!=0) {
	int i;
	for (bi = 0; bi < boxn; bi++) {
	    int v = (int) boxes[bi].UR.y;
	    boxes[bi].UR.y = -1*((ST_boxf)boxes[bi]).LL.y;
	    boxes[bi].LL.y = -v;
	}
	for (i = 0; i < pi; i++)
	    Z.z().polypoints.get__(i).y = -1 * Z.z().polypoints.get__(i).y;
    }
    for (bi = 0; bi < boxn; bi++) {
	boxes[bi].LL.x = INT_MAX;
	boxes[bi].UR.x = INT_MIN;
	}
    poly.ps = Z.z().polypoints;
    poly.pn = pi;
    eps.get__(0).x = pp.start.p.x;
    eps.get__(0).y = pp.start.p.y;
    eps.get__(1).x = pp.end.p.x;
    eps.get__(1).y = pp.end.p.y;
    if (Pshortestpath(poly, eps, pl) < 0) {
		System.err.println("in routesplines, Pshortestpath failed\n");
		return null;
    }
    
    
    if (polyline!=0) {
	make_polyline (pl, spl);
    }
    else {
	if (poly.pn > Z.z().edgen) {
	    Z.z().edges = CArray.<ST_Pedge_t>REALLOC__(poly.pn, Z.z().edges, ST_Pedge_t.class);
	    Z.z().edgen = poly.pn;
	}
	for (edgei = 0; edgei < poly.pn; edgei++) {
	    Z.z().edges.get__(edgei).a.___(Z.z().polypoints.get__(edgei));
	    Z.z().edges.get__(edgei).b.___(Z.z().polypoints.get__((edgei + 1) % poly.pn));
	}
	if (pp.start.constrained) {
 	    evs.get__(0).x = cos(pp.start.theta);
 	    evs.get__(0).y = sin(pp.start.theta);
	} else
	{
	    evs.get__(0).x = evs.get__(0).y = 0;
    }
	if (pp.end.constrained) {
 	    evs.get__(1).x = -cos(pp.end.theta);
 	    evs.get__(1).y = -sin(pp.end.theta);
	} else
	{
	    evs.get__(1).x = evs.get__(1).y = 0;
	}
	
	
	if (Proutespline(Z.z().edges, poly.pn, pl, evs, spl) < 0) {
UNSUPPORTED("elkeyywrfd4hq75w7toc94rzs"); // 	    agerr(AGERR, "in routesplines, Proutespline failed\n");
UNSUPPORTED("7t3fvwp9cv90qu5bdjdglcgtk"); // 	    return NULL;
	}
    }
    if (mkspacep(spl.pn))
UNSUPPORTED("7x5kpcbvg4va887hky7ufm45y"); // 	return NULL;  /* Bailout if no memory left */
    for (bi = 0; bi < boxn; bi++) {
    	boxes[bi].LL.x = INT_MAX;
    	boxes[bi].UR.x = INT_MIN;
    }
    unbounded = true;
    for (splinepi = 0; splinepi < spl.pn; splinepi++) {
	Z.z().ps.get__(splinepi).___(spl.ps.get__(splinepi));
    }
    
    
    for (loopcnt = 0; unbounded && (loopcnt < 15); loopcnt++) {
	limitBoxes (boxes, boxn, Z.z().ps, spl.pn, delta);
	
    /* The following check is necessary because if a box is not very 
     * high, it is possible that the sampling above might miss it.
     * Therefore, we make the sample finer until all boxes have
     * valid values. cf. bug 456. Would making sp[] pointfs help?
     */
	for (bi = 0; bi < boxn; bi++) {
	/* these fp equality tests are used only to detect if the
	 * values have been changed since initialization - ok */
	    if ((boxes[bi].LL.x == INT_MAX) || (boxes[bi].UR.x == INT_MIN)) {
		delta *= 2; /* try again with a finer interval */
		if (delta > INT_MAX/boxn) /* in limitBoxes, boxn*delta must fit in an int, so give up */
		    loopcnt = 15;
		break;
	    }
	}
	if (bi == boxn)
	    unbounded = false;
    }
    if (unbounded) {  
	/* Either an extremely short, even degenerate, box, or some failure with the path
         * planner causing the spline to miss some boxes. In any case, use the shortest path 
	 * to bound the boxes. This will probably mean a bad edge, but we avoid an infinite
	 * loop and we can see the bad edge, and even use the showboxes scaffolding.
	 */
	final ST_Ppoly_t polyspl = new ST_Ppoly_t();
	System.err.println("Unable to reclaim box space in spline routing for edge \"%s\" -> \"%s\". Something is probably seriously wrong.\n");
	make_polyline (pl, polyspl);
	limitBoxes (boxes, boxn, polyspl.ps, polyspl.pn, 10);
	Memory.free (polyspl.ps);
    }
    
    npoints[0] = spl.pn;
    return Z.z().ps;
} finally {
LEAVING("3mcnemqisisnqtd4mr72ej76y","_routesplines");
}
}




//3 axqoytp2rpr8crajhkuvns6q9
// pointf *routesplines(path * pp, int *npoints) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="", key="axqoytp2rpr8crajhkuvns6q9", definition="pointf *routesplines(path * pp, int *npoints)")
public static CArray<ST_pointf> routesplines(ST_path pp, int npoints[]) {
ENTERING("axqoytp2rpr8crajhkuvns6q9","routesplines");
try {
    return _routesplines (pp, npoints, 0);
} finally {
LEAVING("axqoytp2rpr8crajhkuvns6q9","routesplines");
}
}




//3 2v22s41xitwnnsljk9n01nrcy
// pointf *routepolylines(path * pp, int *npoints) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="", key="2v22s41xitwnnsljk9n01nrcy", definition="pointf *routepolylines(path * pp, int *npoints)")
public static CArray<ST_pointf> routepolylines(ST_path pp, int npoints[]) {
ENTERING("2v22s41xitwnnsljk9n01nrcy","routepolylines");
try {
    return _routesplines (pp, npoints, 1);
} finally {
LEAVING("2v22s41xitwnnsljk9n01nrcy","routepolylines");
}
}




//3 65qv6x7ghwyt6hey5qd8cgizn
// static int overlap(int i0, int i1, int j0, int j1) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="overlap", key="65qv6x7ghwyt6hey5qd8cgizn", definition="static int overlap(int i0, int i1, int j0, int j1)")
public static int overlap(double i0, double i1, double j0, double j1) {return overlap((int) i0, (int) i1, (int) j0, (int) j1);}
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="", key="", definition="")
public static int overlap(int i0, int i1, int j0, int j1) {
ENTERING("65qv6x7ghwyt6hey5qd8cgizn","overlap");
try {
    /* i'll bet there's an elegant way to do this */
    if (i1 <= j0)
	return 0;
    if (i0 >= j1)
	return 0;
    if ((j0 <= i0) && (i0 <= j1))
	return (j1 - i0);
    if ((j0 <= i1) && (i1 <= j1))
	return (i1 - j0);
    return MIN(i1 - i0, j1 - j0);
} finally {
LEAVING("65qv6x7ghwyt6hey5qd8cgizn","overlap");
}
}




//3 dxqjhiid5f58b9gjxp0v3j97b
// static int checkpath(int boxn, boxf* boxes, path* thepath) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="checkpath", key="dxqjhiid5f58b9gjxp0v3j97b", definition="static int checkpath(int boxn, boxf* boxes, path* thepath)")
public static int checkpath(int boxn, ST_boxf[] boxes, ST_path thepath) {
ENTERING("dxqjhiid5f58b9gjxp0v3j97b","checkpath");
try {
	ST_boxf ba, bb;
    int bi, i, errs, l, r, d, u;
    int xoverlap, yoverlap;
    /* remove degenerate boxes. */
    i = 0;
    for (bi = 0; bi < boxn; bi++) {
	if (ABS(((ST_boxf)boxes[bi]).LL.y - ((ST_boxf)boxes[bi]).UR.y) < .01)
	    continue;
	if (ABS(((ST_boxf)boxes[bi]).LL.x - ((ST_boxf)boxes[bi]).UR.x) < .01)
	    continue;
	if (i != bi)
	    boxes[i].___(boxes[bi]);
	i++;
    }
    boxn = i;
    ba = (ST_boxf) boxes[0];
    if (ba.LL.x > ba.UR.x || ba.LL.y > ba.UR.y) {
UNSUPPORTED("39tznwvf6k5lgj78jp32p0kfl"); // 	agerr(AGERR, "in checkpath, box 0 has LL coord > UR coord\n");
UNSUPPORTED("evdvb9esh16y8zeoczxhcz7xm"); // 	printpath(thepath);
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
    }
    for (bi = 0; bi < boxn - 1; bi++) {
	ba = (ST_boxf) boxes[bi];
	bb = (ST_boxf) boxes[bi + 1];
	if (bb.LL.x > bb.UR.x || bb.LL.y > bb.UR.y) {
UNSUPPORTED("c8oodo0ge4n4dglb28fvf610v"); // 	    agerr(AGERR, "in checkpath, box %d has LL coord > UR coord\n",
UNSUPPORTED("929pkk2ob1lh7hfe4scuoo5pn"); // 		  bi + 1);
UNSUPPORTED("2m9o6g4nneiul4gt8xb9yb9zi"); // 	    printpath(thepath);
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
	}
	l = (ba.UR.x < bb.LL.x) ? 1 : 0;
	r = (ba.LL.x > bb.UR.x) ? 1 : 0;
	d = (ba.UR.y < bb.LL.y) ? 1 : 0;
	u = (ba.LL.y > bb.UR.y) ? 1 : 0;
	errs = l + r + d + u;
	/*if (errs > 0 && Verbose) {
	    fprintf(stderr, "in checkpath, boxes %d and %d don't touch\n",
		    bi, bi + 1);
	    printpath(thepath);
	}*/
	if (errs > 0) {
	    int xy;
	    if (l == 1)
	    {
		xy = (int) ba.UR.x;
		ba.UR.x = bb.LL.x;
		bb.LL.x = xy;
		l = 0;
		}
	    else if (r == 1) {
	    	xy = (int)(ba.LL.x);
	    	ba.LL.x = bb.UR.x;
	    	bb.UR.x = xy;
	    	r = 0;
	    }
	    else if (d == 1) {
	    	xy = (int)(ba.UR.y);
	    	ba.UR.y = bb.LL.y;
	    	bb.LL.y = xy;
	    	d = 0;
	    }
	    else if (u == 1)
UNSUPPORTED("5kcd52bwvbxxs0md0enfs100u"); // 		xy = ba.LL.y, ba.LL.y = bb.UR.y, bb.UR.y = xy, u = 0;
	    for (i = 0; i < errs - 1; i++) {
UNSUPPORTED("as3p2ldwbg3rbgy64oxx5phar"); // 		if (l == 1)
UNSUPPORTED("efz1z5cfywki1k6q6avldku9z"); // 		    xy = (ba.UR.x + bb.LL.x) / 2.0 + 0.5, ba.UR.x =
UNSUPPORTED("6dfh7cf1xptapqd1mcqtxjrxa"); // 			bb.LL.x = xy, l = 0;
UNSUPPORTED("ang3qytu77fd5owijwbnmkdav"); // 		else if (r == 1)
UNSUPPORTED("67ehof0qqlk339zgl0sqwfu5r"); // 		    xy = (ba.LL.x + bb.UR.x) / 2.0 + 0.5, ba.LL.x =
UNSUPPORTED("llmwvndoq1ne9c62ohtstkwa"); // 			bb.UR.x = xy, r = 0;
UNSUPPORTED("3ce9i9asrqbuog7v1tdurqo6e"); // 		else if (d == 1)
UNSUPPORTED("3mibjrb2jtfextkg9ac5k9spl"); // 		    xy = (ba.UR.y + bb.LL.y) / 2.0 + 0.5, ba.UR.y =
UNSUPPORTED("bccpbv2n38c5utkfh7msoc2y"); // 			bb.LL.y = xy, d = 0;
UNSUPPORTED("7302rnmwdji9n7txquk8k36to"); // 		else if (u == 1)
UNSUPPORTED("9oqpoodvpheztihe63p40guof"); // 		    xy = (ba.LL.y + bb.UR.y) / 2.0 + 0.5, ba.LL.y =
UNSUPPORTED("2cnb1bdjh6y26f98vonla73qa"); // 			bb.UR.y = xy, u = 0;
	    }
	}
	/* check for overlapping boxes */
	xoverlap = overlap(ba.LL.x, ba.UR.x, bb.LL.x, bb.UR.x);
	yoverlap = overlap(ba.LL.y, ba.UR.y, bb.LL.y, bb.UR.y);
	if (xoverlap!=0 && yoverlap!=0) {
 	    if (xoverlap < yoverlap) {
 	    	if (ba.UR.x - ba.LL.x > bb.UR.x - bb.LL.x) {
 	    		/* take space from ba */
UNSUPPORTED("5dqxf3gq05pjtobtnru1g2tuj"); // 		    if (ba.UR.x < bb.UR.x)
UNSUPPORTED("8gz6k803qp9zyw9s459cpp039"); // 			ba.UR.x = bb.LL.x;
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("5r6ck8hfb1cxywn9go61se9kx"); // 			ba.LL.x = bb.UR.x;
 	    	} else {
 	    		/* take space from bb */
 	    		if (ba.UR.x < bb.UR.x)
 	    			bb.LL.x = ba.UR.x;
 	    		else
 	    			bb.UR.x = ba.LL.x;
 	    	}
 	    } else {		/* symmetric for y coords */
 	    	if (ba.UR.y - ba.LL.y > bb.UR.y - bb.LL.y) {
 	    		/* take space from ba */
 	    		if (ba.UR.y < bb.UR.y)
 	    			ba.UR.y = bb.LL.y;
 	    		else
 	    			ba.LL.y = bb.UR.y;
 	    	} else {
 	    		/* take space from bb */
 	    		if (ba.UR.y < bb.UR.y)
 	    			bb.LL.y = ba.UR.y;
 	    		else
 	    			bb.UR.y = ba.LL.y;
 	    	}
	    }
	}
    }
    if (thepath.start.p.x < ((ST_boxf)boxes[0]).LL.x
	|| thepath.start.p.x > ((ST_boxf)boxes[0]).UR.x
	|| thepath.start.p.y < ((ST_boxf)boxes[0]).LL.y
	|| thepath.start.p.y > ((ST_boxf)boxes[0]).UR.y) {
	/*if (Verbose) {
	    fprintf(stderr, "in checkpath, start port not in first box\n");
	    printpath(thepath);
	}*/
	if (thepath.start.p.x < ((ST_boxf)boxes[0]).LL.x)
	    thepath.start.p.x = (((ST_boxf)boxes[0]).LL.x);
	if (thepath.start.p.x > ((ST_boxf)boxes[0]).UR.x)
	    thepath.start.p.x = (((ST_boxf)boxes[0]).UR.x);
	if (thepath.start.p.y < ((ST_boxf)boxes[0]).LL.y)
	    thepath.start.p.y = (((ST_boxf)boxes[0]).LL.y);
	if (thepath.start.p.y > ((ST_boxf)boxes[0]).UR.y)
	    thepath.start.p.y = (((ST_boxf)boxes[0]).UR.y);
    }
    if (thepath.end.p.x < ((ST_boxf)boxes[boxn - 1]).LL.x
	|| thepath.end.p.x > ((ST_boxf)boxes[boxn - 1]).UR.x
	|| thepath.end.p.y < ((ST_boxf)boxes[boxn - 1]).LL.y
	|| thepath.end.p.y > ((ST_boxf)boxes[boxn - 1]).UR.y) {
	/*if (Verbose) {
	    fprintf(stderr, "in checkpath, end port not in last box\n");
	    printpath(thepath);
	}*/
	if (thepath.end.p.x < ((ST_boxf)boxes[boxn - 1]).LL.x)
	    thepath.end.p.x = (((ST_boxf)boxes[boxn - 1]).LL.x);
	if (thepath.end.p.x > ((ST_boxf)boxes[boxn - 1]).UR.x)
	    thepath.end.p.x = (((ST_boxf)boxes[boxn - 1]).UR.x);
	if (thepath.end.p.y < ((ST_boxf)boxes[boxn - 1]).LL.y)
	    thepath.end.p.y = (((ST_boxf)boxes[boxn - 1]).LL.y);
	if (thepath.end.p.y > ((ST_boxf)boxes[boxn - 1]).UR.y)
	    thepath.end.p.y = (((ST_boxf)boxes[boxn - 1]).UR.y);
    }
    return 0;
} finally {
LEAVING("dxqjhiid5f58b9gjxp0v3j97b","checkpath");
}
}




//3 de6jvvw786rx88318tuuqywgq
// static int mkspacep(int size) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="mkspacep", key="de6jvvw786rx88318tuuqywgq", definition="static int mkspacep(int size)")
public static boolean mkspacep(int size) {
ENTERING("de6jvvw786rx88318tuuqywgq","mkspacep");
try {
    if (size > Z.z().maxpn) {
	int newmax = Z.z().maxpn + (size / 300 + 1) * 300;
	Z.z().ps = CArray.<ST_pointf>REALLOC__(newmax, Z.z().ps, ST_pointf.class);
	if (N(Z.z().ps)) {
UNSUPPORTED("ds2v91aohji00tc7zmjuc3v6q"); // 	    agerr(AGERR, "cannot re-allocate ps\n");
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
	}
	Z.z().maxpn = newmax;
    }
    return false;
} finally {
LEAVING("de6jvvw786rx88318tuuqywgq","mkspacep");
}
}



//3 2erpr561ggowmq2m9465p5kqd
// void  makeStraightEdge(graph_t * g, edge_t * e, int et, splineInfo* sinfo) 
@Unused
@Original(version="2.38.0", path="lib/common/routespl.c", name="makeStraightEdge", key="2erpr561ggowmq2m9465p5kqd", definition="void  makeStraightEdge(graph_t * g, edge_t * e, int et, splineInfo* sinfo)")
public static Object makeStraightEdge(Object... arg_) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("89t1p0jilkexnvk5i1vv1arc0"); // makeStraightEdge(graph_t * g, edge_t * e, int et, splineInfo* sinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7pmvaj5a5w3pee9zkh22s4jt5"); //     edge_t *e0;
UNSUPPORTED("821nfzu5iwd6ayryeggj37hj2"); //     edge_t** edges;
UNSUPPORTED("19s88cb2xh6yjpqg0ip3ovi08"); //     edge_t* elist[20];
UNSUPPORTED("5dd4r3fk1fwybt8tvqt4z4ta3"); //     int i, e_cnt;
UNSUPPORTED("310og4kvqsrlm4vs26zqw8c8p"); //     e_cnt = 1;
UNSUPPORTED("63kfcznl8sc62h1pj6k83cjn3"); //     e0 = e;
UNSUPPORTED("1451mr2njzp5m6mj64k6m36tf"); //     while ((e0 = ED_to_virt(e0))) e_cnt++;
UNSUPPORTED("1qovtxs3b3fa6ztj4wx1ahkf2"); //     if (e_cnt <= 20)
UNSUPPORTED("bp3rco5i77vkqs7wua0k7dbfo"); // 	edges = elist;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("8zwb4z35iwpyfy9d9n4b51yvh"); // 	edges = (edge_t**)zmalloc((e_cnt)*sizeof(edge_t*));
UNSUPPORTED("63kfcznl8sc62h1pj6k83cjn3"); //     e0 = e;
UNSUPPORTED("tf4qi3e2hsjxi603z57w6hx6"); //     for (i = 0; i < e_cnt; i++) {
UNSUPPORTED("106t1hs57atf24mgepcp9wwjw"); // 	edges[i] = e0;
UNSUPPORTED("dfdtts0ddwzo6ffy5m1pso8t6"); // 	e0 = ED_to_virt(e0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("db2lohkib2plgiw7i90nxgkjk"); //     makeStraightEdges (g, edges, e_cnt, et, sinfo);
UNSUPPORTED("b1xlizpp11lvumjyajfk6mxky"); //     if (e_cnt > 20) free (edges);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




}
