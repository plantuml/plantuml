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
package gen.lib.pathplan;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.setjmp;
import static smetana.core.Macro.HUGE_VAL;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_Ppoly_t;
import h.ST_pointf;
import h.ST_pointnlink_t;
import h.ST_triangle_t;
import smetana.core.CArray;
import smetana.core.Globals;
import smetana.core.ZType;
import smetana.core.__ptr__;
import smetana.core.jmp_buf;

public class shortest__c {
    // ::remove folder when __HAXE__
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();



/* Pshortestpath:
 * Find a shortest path contained in the polygon polyp going between the
 * points supplied in eps. The resulting polyline is stored in output.
 * Return 0 on success, -1 on bad input, -2 on memory allocation problem. 
 */
//3 2gub5b19vo2qexn56nw23wage
// int Pshortestpath(Ppoly_t * polyp, Ppoint_t * eps, Ppolyline_t * output) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="Pshortestpath", key="2gub5b19vo2qexn56nw23wage", definition="int Pshortestpath(Ppoly_t * polyp, Ppoint_t * eps, Ppolyline_t * output)")
public static int Pshortestpath(Globals zz, ST_Ppoly_t polyp, CArray<ST_pointf> eps, ST_Ppoly_t output) {
ENTERING("2gub5b19vo2qexn56nw23wage","Pshortestpath");
try {
    int pi, minpi;
    double minx;
    final ST_pointf p1 = new ST_pointf(), p2 = new ST_pointf(), p3 = new ST_pointf();
    int trii, trij, ftrii, ltrii;
    int ei;
    final ST_pointnlink_t[]  epnls = new ST_pointnlink_t[] {new ST_pointnlink_t(),new ST_pointnlink_t()};
    ST_pointnlink_t lpnlp=null, rpnlp=null, pnlp=null;
    CArray<ST_triangle_t> trip;
    int splitindex;
    
    if (setjmp(jbuf)!=0)
	return -2;
    /* make space */
    growpnls(zz, polyp.pn);
    zz.pnll = 0;
    zz.tril = 0;
    growdq(zz, polyp.pn * 2);
    zz.dq.fpnlpi = zz.dq.pnlpn / 2;
    zz.dq.lpnlpi = zz.dq.fpnlpi - 1;
    
    
    /* make sure polygon is CCW and load pnls array */
    for (pi = 0, minx = HUGE_VAL, minpi = -1; pi < polyp.pn; pi++) {
	if (minx > polyp.ps.get__(pi).x)
	    { minx = polyp.ps.get__(pi).x;
		minpi = pi; }
    }
    p2.___(polyp.ps.get__(minpi));
    p1.___(polyp.ps.get__(((minpi == 0) ? polyp.pn - 1 : minpi - 1)));
    p3.___(polyp.ps.get__(((minpi == polyp.pn - 1) ? 0 : minpi + 1)));
    if (((p1.x == p2.x && p2.x == p3.x) && (p3.y > p2.y)) ||
	ccw(p1, p2, p3) != 1) {
	for (pi = polyp.pn - 1; pi >= 0; pi--) {
	    if (pi < polyp.pn - 1
		&& polyp.ps.get__(pi).x == polyp.ps.get__(pi+1).x
		&& polyp.ps.get__(pi).y == polyp.ps.get__(pi+1).y)
		continue;
	    zz.pnls[zz.pnll].pp = polyp.ps.get__(pi);
	    zz.pnls[zz.pnll].link = zz.pnls[zz.pnll % polyp.pn];
	    zz.pnlps[zz.pnll] = zz.pnls[zz.pnll];
	    zz.pnll++;
	}
    } else {
	for (pi = 0; pi < polyp.pn; pi++) {
	    if (pi > 0 && polyp.ps.get__(pi).x == polyp.ps.get__(pi - 1).x &&
		polyp.ps.get__(pi).y == polyp.ps.get__(pi - 1).y)
		continue;
	    zz.pnls[zz.pnll].pp = polyp.ps.get__(pi);
	    zz.pnls[zz.pnll].link = zz.pnls[zz.pnll % polyp.pn];
	    zz.pnlps[zz.pnll] = zz.pnls[zz.pnll];
	    zz.pnll++;
	}
    }
    
    
    /* generate list of triangles */
    triangulate(zz, zz.pnlps, zz.pnll);
    
    /* connect all pairs of triangles that share an edge */
    for (trii = 0; trii < zz.tril; trii++)
	for (trij = trii + 1; trij < zz.tril; trij++)
	    connecttris(zz, trii, trij);
    
    /* find first and last triangles */
    for (trii = 0; trii < zz.tril; trii++)
	if (pointintri(zz, trii, eps.get__(0)))
	    break;
    if (trii == zz.tril) {
UNSUPPORTED("4ma3y8l4lmjcsw49kmsgknig6"); // 	fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("source point not in any triangle"));
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
    }
    ftrii = trii;
    for (trii = 0; trii < zz.tril; trii++)
	if (pointintri(zz, trii, eps.get__(1)))
	    break;
    if (trii == zz.tril) {
        System.err.println("libpath/%s:%d: %s\n" + "graphviz-2.38.0\\lib\\pathplan\\shortest.c" + 26 + ("destination point not in any triangle"));
        return -1;
    }
    ltrii = trii;
    
    /* mark the strip of triangles from eps[0] to eps[1] */
    if (!marktripath(zz, ftrii, ltrii)) {
	System.err.println("libpath/%s:%d: %s" + "graphviz-2.38.0\\lib\\pathplan\\shortest.c" + 26 + ("cannot find triangle path"));
	/* a straight line is better than failing */
	growops(zz, 2);
	output.pn = 2;
	zz.ops_shortest.get__(0).___(eps.get__(0));
	zz.ops_shortest.get__(1).___(eps.get__(1));
	output.ps = zz.ops_shortest;
	return 0;
    }
    
    /* if endpoints in same triangle, use a single line */
    if (ftrii == ltrii) {
UNSUPPORTED("2nnkwrdxg0ma2m482dqarlbz6"); // 	growops(2);
UNSUPPORTED("5penbn9ky80i7jw02belk2zoj"); // 	output->pn = 2;
UNSUPPORTED("8i925e1tnbqn909027lqcg3fi"); // 	ops[0] = eps[0], ops[1] = eps[1];
UNSUPPORTED("3rcg6c9s9nmostq9c3r5n6x4h"); // 	output->ps = ops;
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
    }
    
    /* build funnel and shortest path linked list (in add2dq) */
    epnls[0].pp = eps.get__(0);
    epnls[0].link = null;
    epnls[1].pp = eps.get__(1);
    epnls[1].link = null;
    add2dq(zz, 1, epnls[0]);
    zz.dq.apex = zz.dq.fpnlpi;
    trii = ftrii;
    while (trii != -1) {
	trip = zz.tris.plus_(trii);
	trip.get__(0).mark = 2;
	
	/* find the left and right points of the exiting edge */
	for (ei = 0; ei < 3; ei++)
	    if (trip.get__(0).e[ei].rtp!=null &&
	    		trip.get__(0).e[ei].rtp.get__(0).mark == 1)
		break;
	if (ei == 3) {		/* in last triangle */
	    if (ccw(eps.get__(1), zz.dq.pnlps[zz.dq.fpnlpi].pp,
		    zz.dq.pnlps[zz.dq.lpnlpi].pp) == 1)
		{
		lpnlp = zz.dq.pnlps[zz.dq.lpnlpi];
		rpnlp = epnls[1];
	    } else {
		lpnlp = epnls[1];
		rpnlp = zz.dq.pnlps[zz.dq.lpnlpi];
		}
	} else {
	    pnlp = trip.get__(0).e[(ei + 1) % 3].pnl1p;
	    if (ccw(trip.get__(0).e[ei].pnl0p.pp, pnlp.pp,
	    		trip.get__(0).e[ei].pnl1p.pp) == 1)
UNSUPPORTED("2cii65lhw4wb8nyvjv702v7md"); // 		lpnlp = trip->e[ei].pnl1p, rpnlp = trip->e[ei].pnl0p;
	    else
		{
		  lpnlp = trip.get__(0).e[ei].pnl0p;
		  rpnlp = trip.get__(0).e[ei].pnl1p;
		}
	}
	
	/* update deque */
	if (trii == ftrii) {
	    add2dq(zz, 2, lpnlp);
	    add2dq(zz, 1, rpnlp);
	} else {
	    if ((zz.dq.pnlps[zz.dq.fpnlpi] != rpnlp)
		&& (zz.dq.pnlps[zz.dq.lpnlpi] != rpnlp)) {
		/* add right point to deque */
		splitindex = finddqsplit(zz, rpnlp);
		splitdq(zz, 2, splitindex);
		add2dq(zz, 1, rpnlp);
		/* if the split is behind the apex, then reset apex */
		if (splitindex > zz.dq.apex)
		    zz.dq.apex = splitindex;
	    } else {
		/* add left point to deque */
		splitindex = finddqsplit(zz, lpnlp);
		splitdq(zz, 1, splitindex);
		add2dq(zz, 2, lpnlp);
		/* if the split is in front of the apex, then reset apex */
		if (splitindex < zz.dq.apex)
		    zz.dq.apex = splitindex;
	    }
	}
	trii = -1;
	for (ei = 0; ei < 3; ei++)
	    if (trip.get__(0).e[ei].rtp!=null && 
	    		trip.get__(0).e[ei].rtp.get__(0).mark == 1) {
		trii = trip.get__(0).e[ei].rtp.minus_(zz.tris);
		break;
	    }
    }
    
    
    for (pi = 0, pnlp = epnls[1]; pnlp!=null; pnlp = pnlp.link)
	pi++;
    growops(zz, pi);
    output.pn = pi;
    for (pi = pi - 1, pnlp = epnls[1]; pnlp!=null; pi--, pnlp = pnlp.link)
	zz.ops_shortest.get__(pi).___(pnlp.pp);
    output.ps = zz.ops_shortest;
    
    return 0;
} finally {
LEAVING("2gub5b19vo2qexn56nw23wage","Pshortestpath");
}
}




//3 73cr7m3mqvtuotpzrmaw2y8zm
// static void triangulate(pointnlink_t ** pnlps, int pnln) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="triangulate", key="73cr7m3mqvtuotpzrmaw2y8zm", definition="static void triangulate(pointnlink_t ** pnlps, int pnln)")
public static void triangulate(Globals zz, ST_pointnlink_t pnlps[], int pnln) {
ENTERING("73cr7m3mqvtuotpzrmaw2y8zm","triangulate");
try {
    int pnli, pnlip1, pnlip2;
    LOG2("triangulate "+pnln);
	if (pnln > 3) 
	{
		for (pnli = 0; pnli < pnln; pnli++) 
		{
			pnlip1 = (pnli + 1) % pnln;
			pnlip2 = (pnli + 2) % pnln;
			if (isdiagonal(pnli, pnlip2, pnlps, pnln)) 
			{
				loadtriangle(zz, pnlps[pnli], pnlps[pnlip1], pnlps[pnlip2]);
				for (pnli = pnlip1; pnli < pnln - 1; pnli++)
					pnlps[pnli] = pnlps[pnli + 1];
				triangulate(zz, pnlps, pnln - 1);
				return;
			}
		}
		throw new IllegalStateException("libpath/%s:%d: %s\n" + "graphviz-2.38.0\\lib\\pathplan\\shortest.c" + 26 + ("triangulation failed"));
    } 
	else
		loadtriangle(zz, pnlps[0], pnlps[1], pnlps[2]);
} finally {
LEAVING("73cr7m3mqvtuotpzrmaw2y8zm","triangulate");
}
}




//3 72of3cd7shtwokglxapw04oe9
// static int isdiagonal(int pnli, int pnlip2, pointnlink_t ** pnlps, 		      int pnln) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="isdiagonal", key="72of3cd7shtwokglxapw04oe9", definition="static int isdiagonal(int pnli, int pnlip2, pointnlink_t ** pnlps, 		      int pnln)")
public static boolean isdiagonal(int pnli, int pnlip2, ST_pointnlink_t[] pnlps, int pnln) {
ENTERING("72of3cd7shtwokglxapw04oe9","isdiagonal");
try {
    int pnlip1, pnlim1, pnlj, pnljp1;
    boolean res;
    /* neighborhood test */
    pnlip1 = (pnli + 1) % pnln;
    pnlim1 = (pnli + pnln - 1) % pnln;
    /* If P[pnli] is a convex vertex [ pnli+1 left of (pnli-1,pnli) ]. */
    if (ccw(pnlps[pnlim1].pp, pnlps[pnli].pp, pnlps[pnlip1].pp) ==
	1)
	res =
	    (ccw(pnlps[pnli].pp, pnlps[pnlip2].pp, pnlps[pnlim1].pp) ==
	     1)
	    && (ccw(pnlps[pnlip2].pp, pnlps[pnli].pp, pnlps[pnlip1].pp)
		== 1);
    /* Assume (pnli - 1, pnli, pnli + 1) not collinear. */
    else
	res = ccw(pnlps[pnli].pp, pnlps[pnlip2].pp,
		   pnlps[pnlip1].pp) == 2;
    if (!res)
	return false;
    /* check against all other edges */
    for (pnlj = 0; pnlj < pnln; pnlj++) {
	pnljp1 = (pnlj + 1) % pnln;
	if (!((pnlj == pnli) || (pnljp1 == pnli) ||
	  (pnlj == pnlip2) || (pnljp1 == pnlip2)))
	    if (intersects(pnlps[pnli].pp, pnlps[pnlip2].pp,
			   pnlps[pnlj].pp, pnlps[pnljp1].pp))
		return false;
    }
    return true;
} finally {
LEAVING("72of3cd7shtwokglxapw04oe9","isdiagonal");
}
}




//3 7vf9jtj9i8rg0cxrstbqswuck
// static void loadtriangle(pointnlink_t * pnlap, pointnlink_t * pnlbp, 			 pointnlink_t * pnlcp) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="loadtriangle", key="7vf9jtj9i8rg0cxrstbqswuck", definition="static void loadtriangle(pointnlink_t * pnlap, pointnlink_t * pnlbp, 			 pointnlink_t * pnlcp)")
public static void loadtriangle(Globals zz, __ptr__ pnlap, __ptr__ pnlbp, __ptr__ pnlcp) {
ENTERING("7vf9jtj9i8rg0cxrstbqswuck","loadtriangle");
try {
	CArray<ST_triangle_t> trip;
    int ei;
    /* make space */
    if (zz.tril >= zz.trin)
	growtris(zz, zz.trin + 20);
    trip = zz.tris.plus_(zz.tril++);
    trip.get__(0).mark = 0;
    trip.get__(0).e[0].pnl0p = (ST_pointnlink_t) pnlap;
    trip.get__(0).e[0].pnl1p = (ST_pointnlink_t) pnlbp;
    trip.get__(0).e[0].rtp = null;
    trip.get__(0).e[1].pnl0p = (ST_pointnlink_t) pnlbp;
    trip.get__(0).e[1].pnl1p = (ST_pointnlink_t) pnlcp;
    trip.get__(0).e[1].rtp = null;
    trip.get__(0).e[2].pnl0p = (ST_pointnlink_t) pnlcp;
    trip.get__(0).e[2].pnl1p = (ST_pointnlink_t) pnlap;
    trip.get__(0).e[2].rtp = null;
    for (ei = 0; ei < 3; ei++)
	trip.get__(0).e[ei].lrp = trip;
} finally {
LEAVING("7vf9jtj9i8rg0cxrstbqswuck","loadtriangle");
}
}




//3 6coujw0qksrgu5sxj0r39qm1u
// static void connecttris(int tri1, int tri2) 
/* connect a pair of triangles at their common edge (if any) */
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="connecttris", key="6coujw0qksrgu5sxj0r39qm1u", definition="static void connecttris(int tri1, int tri2)")
public static void connecttris(Globals zz, int tri1, int tri2) {
ENTERING("6coujw0qksrgu5sxj0r39qm1u","connecttris");
try {
	CArray<ST_triangle_t> tri1p;
	CArray<ST_triangle_t> tri2p;
    int ei, ej;
    for (ei = 0; ei < 3; ei++) {
	for (ej = 0; ej < 3; ej++) {
	    tri1p = zz.tris.plus_(tri1);
	    tri2p = zz.tris.plus_(tri2);
	    if ((tri1p.get__(0).e[ei].pnl0p.pp == tri2p.get__(0).e[ej].pnl0p.pp &&
		 tri1p.get__(0).e[ei].pnl1p.pp == tri2p.get__(0).e[ej].pnl1p.pp) ||
		(tri1p.get__(0).e[ei].pnl0p.pp == tri2p.get__(0).e[ej].pnl1p.pp &&
		 tri1p.get__(0).e[ei].pnl1p.pp == tri2p.get__(0).e[ej].pnl0p.pp))
		 {
	    	tri1p.get__(0).e[ei].rtp = tri2p;
	    	tri2p.get__(0).e[ej].rtp = tri1p;
		 }
	}
    }
} finally {
LEAVING("6coujw0qksrgu5sxj0r39qm1u","connecttris");
}
}




//3 3waxf5wy3mwt12wpg5hxg3o9c
// static int marktripath(int trii, int trij) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="marktripath", key="3waxf5wy3mwt12wpg5hxg3o9c", definition="static int marktripath(int trii, int trij)")
public static boolean marktripath(Globals zz, int trii, int trij) {
ENTERING("3waxf5wy3mwt12wpg5hxg3o9c","marktripath");
try {
    int ei;
    
    if (zz.tris.get__(trii).mark!=0)
	return false;
    zz.tris.get__(trii).mark = 1;
    if (trii == trij)
	return true;
    for (ei = 0; ei < 3; ei++)
	if ((zz.tris.get__(trii).e[ei].rtp!=null &&
	    marktripath(zz, zz.tris.get__(trii).e[ei].rtp.minus_(zz.tris), trij)))
	    return true;
    zz.tris.get__(trii).mark = 0;
    return false;
} finally {
LEAVING("3waxf5wy3mwt12wpg5hxg3o9c","marktripath");
}
}




//3 44szdl31mg8mt5qrfj70kb2sn
// static void add2dq(int side, pointnlink_t * pnlp) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="add2dq", key="44szdl31mg8mt5qrfj70kb2sn", definition="static void add2dq(int side, pointnlink_t * pnlp)")
public static void add2dq(Globals zz, int side, ST_pointnlink_t pnlp) {
ENTERING("44szdl31mg8mt5qrfj70kb2sn","add2dq");
try {
    if (side == 1) {
	if (zz.dq.lpnlpi - zz.dq.fpnlpi >= 0)
	    pnlp.link = zz.dq.pnlps[zz.dq.fpnlpi];
	    /* shortest path links */
	zz.dq.fpnlpi = zz.dq.fpnlpi-1;
	zz.dq.pnlps[zz.dq.fpnlpi] = pnlp;
    } else {
	if (zz.dq.lpnlpi - zz.dq.fpnlpi >= 0)
	    pnlp.link = zz.dq.pnlps[zz.dq.lpnlpi];
	    /* shortest path links */
	zz.dq.lpnlpi = zz.dq.lpnlpi+1;
	zz.dq.pnlps[zz.dq.lpnlpi] = pnlp;
    }
} finally {
LEAVING("44szdl31mg8mt5qrfj70kb2sn","add2dq");
}
}




//3 572sssdz1se16w790xceiy5vr
// static void splitdq(int side, int index) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="splitdq", key="572sssdz1se16w790xceiy5vr", definition="static void splitdq(int side, int index)")
public static void splitdq(Globals zz, int side, int index) {
ENTERING("572sssdz1se16w790xceiy5vr","splitdq");
try {
    if (side == 1)
	zz.dq.lpnlpi = index;
    else
	zz.dq.fpnlpi = index;
} finally {
LEAVING("572sssdz1se16w790xceiy5vr","splitdq");
}
}




//3 9dnrc8vqpffp5t3bmsackgqtl
// static int finddqsplit(pointnlink_t * pnlp) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="finddqsplit", key="9dnrc8vqpffp5t3bmsackgqtl", definition="static int finddqsplit(pointnlink_t * pnlp)")
public static int finddqsplit(Globals zz, ST_pointnlink_t pnlp) {
ENTERING("9dnrc8vqpffp5t3bmsackgqtl","finddqsplit");
try {
    int index;
    for (index = zz.dq.fpnlpi; index < zz.dq.apex; index++)
	if (ccw(zz.dq.pnlps[index + 1].pp, zz.dq.pnlps[index].pp, pnlp.pp) ==
	    1)
	    return index;
    for (index = zz.dq.lpnlpi; index > zz.dq.apex; index--)
	if (ccw(zz.dq.pnlps[index - 1].pp, zz.dq.pnlps[index].pp, pnlp.pp) ==
	    2)
	    return index;
    return zz.dq.apex;
} finally {
LEAVING("9dnrc8vqpffp5t3bmsackgqtl","finddqsplit");
}
}




//3 72h03s8inxtto2ekvmuqjtj3d
// static int ccw(Ppoint_t * p1p, Ppoint_t * p2p, Ppoint_t * p3p) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="ccw", key="72h03s8inxtto2ekvmuqjtj3d", definition="static int ccw(Ppoint_t * p1p, Ppoint_t * p2p, Ppoint_t * p3p)")
public static int ccw(ST_pointf p1p, ST_pointf p2p, ST_pointf p3p) {
ENTERING("72h03s8inxtto2ekvmuqjtj3d","ccw");
try {
    double d;
    d = ((p1p.y - p2p.y) * (p3p.x - p2p.x)) -
	((p3p.y - p2p.y) * (p1p.x - p2p.x));
    return (d > 0) ? 1 : ((d < 0) ? 2 : 3);
} finally {
LEAVING("72h03s8inxtto2ekvmuqjtj3d","ccw");
}
}




//3 22a9ajg9t8ovqsigk3tyu3rkd
// static int intersects(Ppoint_t * pap, Ppoint_t * pbp, 		      Ppoint_t * pcp, Ppoint_t * pdp) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="intersects", key="22a9ajg9t8ovqsigk3tyu3rkd", definition="static int intersects(Ppoint_t * pap, Ppoint_t * pbp, 		      Ppoint_t * pcp, Ppoint_t * pdp)")
public static boolean intersects(ST_pointf pap, ST_pointf pbp, ST_pointf pcp, ST_pointf pdp) {
ENTERING("22a9ajg9t8ovqsigk3tyu3rkd","intersects");
try {
    int ccw1, ccw2, ccw3, ccw4;
    if (ccw(pap, pbp, pcp) == 3 || ccw(pap, pbp, pdp) == 3 ||
	ccw(pcp, pdp, pap) == 3 || ccw(pcp, pdp, pbp) == 3) {
	if (between(pap, pbp, pcp) || between(pap, pbp, pdp) ||
	    between(pcp, pdp, pap) || between(pcp, pdp, pbp))
	    return ((!(false)));
    } else {
	ccw1 = (ccw(pap, pbp, pcp) == 1) ? 1 : 0;
	ccw2 = (ccw(pap, pbp, pdp) == 1) ? 1 : 0;
	ccw3 = (ccw(pcp, pdp, pap) == 1) ? 1 : 0;
	ccw4 = (ccw(pcp, pdp, pbp) == 1) ? 1 : 0;
	return (ccw1 ^ ccw2)!=0 && (ccw3 ^ ccw4)!=0;
    }
    return false;
} finally {
LEAVING("22a9ajg9t8ovqsigk3tyu3rkd","intersects");
}
}




//3 uh5n18rzyevtb4cwpni70qpc
// static int between(Ppoint_t * pap, Ppoint_t * pbp, Ppoint_t * pcp) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="between", key="uh5n18rzyevtb4cwpni70qpc", definition="static int between(Ppoint_t * pap, Ppoint_t * pbp, Ppoint_t * pcp)")
public static boolean between(ST_pointf pap, ST_pointf pbp, ST_pointf pcp) {
ENTERING("uh5n18rzyevtb4cwpni70qpc","between");
try {
    final ST_pointf p1 = new ST_pointf(), p2 = new ST_pointf();
    p1.x = pbp.x - pap.x;
    p1.y = pbp.y - pap.y;
    p2.x = pcp.x - pap.x;
    p2.y = pcp.y - pap.y;
    if (ccw(pap, pbp, pcp) != 3)
	return false;
    return (p2.x * p1.x + p2.y * p1.y >= 0) &&
	(p2.x * p2.x + p2.y * p2.y <= p1.x * p1.x + p1.y * p1.y);
} finally {
LEAVING("uh5n18rzyevtb4cwpni70qpc","between");
}
}




//3 zti1mzm2m7tr2xwnbf7e8u3d
// static int pointintri(int trii, Ppoint_t * pp) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="pointintri", key="zti1mzm2m7tr2xwnbf7e8u3d", definition="static int pointintri(int trii, Ppoint_t * pp)")
public static boolean pointintri(Globals zz, int trii, ST_pointf pp) {
ENTERING("zti1mzm2m7tr2xwnbf7e8u3d","pointintri");
try {
    int ei, sum;
    for (ei = 0, sum = 0; ei < 3; ei++)
	if (ccw(zz.tris.get__(trii).e[ei].pnl0p.pp,
			zz.tris.get__(trii).e[ei].pnl1p.pp, pp) != 2)
	    sum++;
    return (sum == 3 || sum == 0);
} finally {
LEAVING("zti1mzm2m7tr2xwnbf7e8u3d","pointintri");
}
}




//3 85wstb60jkjd0kbh9tyninm4h
// static void growpnls(int newpnln) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="growpnls", key="85wstb60jkjd0kbh9tyninm4h", definition="static void growpnls(int newpnln)")
public static void growpnls(Globals zz, int newpnln) {
ENTERING("85wstb60jkjd0kbh9tyninm4h","growpnls");
try {
    if (newpnln <= zz.pnln)
	return;
    if ((zz.pnls) == null) {
	if ((zz.pnls = malloc(newpnln)) == null) {
UNSUPPORTED("9zyfc4bjg3i6rrna9vqf8doys"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
	if ((zz.pnlps = malloc(newpnln)) == null) {
UNSUPPORTED("1etar0wd2cbbvqo4jnmbvjiz4"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc pnlps"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (((zz.pnls = realloc(zz.pnls, newpnln)))==null) {
UNSUPPORTED("105nogpkt0qqut0yu4alvkk1u"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
	if (((zz.pnlps = realloc(zz.pnlps, newpnln)))==null) {
UNSUPPORTED("be84alh84ub40x4um989aj20d"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc pnlps"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    zz.pnln = newpnln;
} finally {
LEAVING("85wstb60jkjd0kbh9tyninm4h","growpnls");
}
}

private static ST_pointnlink_t[] malloc(int nb) {
	ST_pointnlink_t result[] = new ST_pointnlink_t[nb];
	for (int i = 0; i < result.length; i++) {
		result[i] = new ST_pointnlink_t();
	}
	return result;
}
private static ST_pointnlink_t[] realloc(ST_pointnlink_t orig[], int nb) {
	if (orig.length >= nb) {
		return orig;
	}
	ST_pointnlink_t result[] = malloc(nb);
	for (int i = 0; i < orig.length; i++) {
		result[i] = orig[i];
	}
	return result;
}



//3 c5q8ult6w26jppe5ifzgcoq82
// static void growtris(int newtrin) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="growtris", key="c5q8ult6w26jppe5ifzgcoq82", definition="static void growtris(int newtrin)")
public static void growtris(Globals zz, int newtrin) {
ENTERING("c5q8ult6w26jppe5ifzgcoq82","growtris");
try {
    if (newtrin <= zz.trin)
	return;
    if ((zz.tris) == null) {
	if ((zz.tris = CArray.<ST_triangle_t>ALLOC__(newtrin, ZType.ST_triangle_t)) == null) {
	UNSUPPORTED("5782e28cjpaa3dpf8up4zmtq7"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc tris"));
	UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (((zz.tris = CArray.<ST_triangle_t>REALLOC__(newtrin, zz.tris, ZType.ST_triangle_t)))==null) {
	UNSUPPORTED("d3fgu54pn5tydfhn7z73v73ra"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc tris"));
	UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    zz.trin = newtrin;
} finally {
LEAVING("c5q8ult6w26jppe5ifzgcoq82","growtris");
}
}


private static ST_triangle_t[] malloc2(int nb) {
	ST_triangle_t result[] = new ST_triangle_t[nb];
	for (int i = 0; i < result.length; i++) {
		result[i] = new ST_triangle_t();
	}
	return result;
}
private static ST_triangle_t[] realloc2(ST_triangle_t orig[], int nb) {
	if (orig.length >= nb) {
		return orig;
	}
	ST_triangle_t result[] = malloc2(nb);
	for (int i = 0; i < orig.length; i++) {
		result[i] = orig[i];
	}
	return result;
}



//3 bzym9u6dtatii1vp4hcmofc80
// static void growdq(int newdqn) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="growdq", key="bzym9u6dtatii1vp4hcmofc80", definition="static void growdq(int newdqn)")
public static void growdq(Globals zz, int newdqn) {
ENTERING("bzym9u6dtatii1vp4hcmofc80","growdq");
try {
    if (newdqn <= zz.dq.pnlpn)
	return;
    if ((zz.dq.pnlps) == null) {
	if (!zz.dq.malloc(newdqn)) {
UNSUPPORTED("88fwpb40wz9jc8jiz7u032s4t"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc dq.pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (!zz.dq.realloc(newdqn)) {
UNSUPPORTED("exqx4ck7h15m8whgip6xpnhoo"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc dq.pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    zz.dq.pnlpn = newdqn;
} finally {
LEAVING("bzym9u6dtatii1vp4hcmofc80","growdq");
}
}




//3 d7vtt8xqxbdnx9kwtt1zzof75
// static void growops(int newopn) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/shortest.c", name="growops", key="d7vtt8xqxbdnx9kwtt1zzof75", definition="static void growops(int newopn)")
public static void growops(Globals zz, int newopn) {
ENTERING("d7vtt8xqxbdnx9kwtt1zzof75","growops");
try {
    if (newopn <= zz.opn_shortest)
	return;
    if ((zz.ops_shortest) == null) {
	if ((zz.ops_shortest = CArray.<ST_pointf>ALLOC__(newopn, ZType.ST_pointf)) == null) {
UNSUPPORTED("7wxgcgah7iy6vetj5yszoq4k4"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc ops"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (((zz.ops_shortest = CArray.<ST_pointf>REALLOC__(newopn, zz.ops_shortest, ZType.ST_pointf)))==null) {
UNSUPPORTED("7azrdo5s3kc44taqmtmeu1s33"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc ops"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
    }
    }
    zz.opn_shortest = newopn;
} finally {
LEAVING("d7vtt8xqxbdnx9kwtt1zzof75","growops");
}
}


}
