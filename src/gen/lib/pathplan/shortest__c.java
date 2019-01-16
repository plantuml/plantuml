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
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.setjmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.HUGE_VAL;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Ppoly_t;
import h.ST_pointf;
import h.ST_pointnlink_t;
import h.ST_triangle_t;
import smetana.core.Z;
import smetana.core.__ptr__;
import smetana.core.jmp_buf;

public class shortest__c {
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();

//1 3gj35j1owzho1bj8qrr8j520a
// static pointnlink_t *pnls, **pnlps
//private static pointnlink_t pnls;
//private static __ptr__ pnlps;

//1 6r0oj01m6zabkua5f5x778wq6
// static int pnln, pnll
//private static int pnln, pnll;

//1 aywrlzd2nea9kkv8kzuctk7pt
// static triangle_t *tris
//private static __ptr__ tris;

//1 a6aizijrgkmhf8hoyeqj49b60
// static int trin, tril
//private static int trin, tril;

//1 cc2hmcygbtg3adbwgkunssdhx
// static deque_t dq
//private final static __struct__<deque_t> dq = JUtils.from(deque_t.class);

//1 3k2f2er3efsrl0210su710vf
// static Ppoint_t *ops
//static private __ptr__ ops;

//1 68lm8qk1d212p1ngzu26gudjc
// static int opn
//private static int opn;



//3 2gub5b19vo2qexn56nw23wage
// int Pshortestpath(Ppoly_t * polyp, Ppoint_t * eps, Ppolyline_t * output) 
public static int Pshortestpath(ST_Ppoly_t polyp, ST_pointf.Array eps, ST_Ppoly_t output) {
ENTERING("2gub5b19vo2qexn56nw23wage","Pshortestpath");
try {
    int pi, minpi;
    double minx;
    final ST_pointf p1 = new ST_pointf(), p2 = new ST_pointf(), p3 = new ST_pointf();
    int trii, trij, ftrii, ltrii;
    int ei;
    final ST_pointnlink_t[]  epnls = new ST_pointnlink_t[] {new ST_pointnlink_t(),new ST_pointnlink_t()};
    ST_pointnlink_t lpnlp=null, rpnlp=null, pnlp=null;
    ST_triangle_t.Array trip;
    int splitindex;
    if (setjmp(jbuf)!=0)
	return -2;
    /* make space */
    growpnls(polyp.pn);
    Z.z().pnll = 0;
    Z.z().tril = 0;
    growdq(polyp.pn * 2);
    Z.z().dq.fpnlpi = Z.z().dq.pnlpn / 2;
    Z.z().dq.lpnlpi = Z.z().dq.fpnlpi - 1;
    /* make sure polygon is CCW and load pnls array */
    for (pi = 0, minx = HUGE_VAL, minpi = -1; pi < polyp.pn; pi++) {
	if (minx > polyp.ps.get(pi).x)
	    { minx = polyp.ps.get(pi).x;
		minpi = pi; }
    }
    p2.____(polyp.ps.plus(minpi));
    p1.____(polyp.ps.plus(((minpi == 0) ? polyp.pn - 1 : minpi - 1)));
    p3.____(polyp.ps.plus(((minpi == polyp.pn - 1) ? 0 : minpi + 1)));
    if (((p1.x == p2.x && p2.x == p3.x) && (p3.y > p2.y)) ||
	ccw(p1, p2, p3) != 1) {
	for (pi = polyp.pn - 1; pi >= 0; pi--) {
	    if (pi < polyp.pn - 1
		&& polyp.ps.get(pi).x == polyp.ps.get(pi+1).x
		&& polyp.ps.get(pi).y == polyp.ps.get(pi+1).y)
		continue;
	    Z.z().pnls[Z.z().pnll].pp = (ST_pointf) ((ST_pointf)polyp.ps.get(pi));
	    Z.z().pnls[Z.z().pnll].link = Z.z().pnls[Z.z().pnll % polyp.pn];
	    Z.z().pnlps[Z.z().pnll] = Z.z().pnls[Z.z().pnll];
	    Z.z().pnll++;
	}
    } else {
	for (pi = 0; pi < polyp.pn; pi++) {
	    if (pi > 0 && polyp.ps.get(pi).x == polyp.ps.get(pi - 1).x &&
		polyp.ps.get(pi).y == polyp.ps.get(pi - 1).y)
		continue;
	    Z.z().pnls[Z.z().pnll].pp = (ST_pointf) ((ST_pointf)polyp.ps.get(pi));
	    Z.z().pnls[Z.z().pnll].link = Z.z().pnls[Z.z().pnll % polyp.pn];
	    Z.z().pnlps[Z.z().pnll] = Z.z().pnls[Z.z().pnll];
	    Z.z().pnll++;
	}
    }
    /* generate list of triangles */
    triangulate(Z.z().pnlps, Z.z().pnll);
    /* connect all pairs of triangles that share an edge */
    for (trii = 0; trii < Z.z().tril; trii++)
	for (trij = trii + 1; trij < Z.z().tril; trij++)
	    connecttris(trii, trij);
    /* find first and last triangles */
    for (trii = 0; trii < Z.z().tril; trii++)
	if (pointintri(trii, eps.plus(0).getStruct()))
	    break;
    if (trii == Z.z().tril) {
UNSUPPORTED("4ma3y8l4lmjcsw49kmsgknig6"); // 	fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("source point not in any triangle"));
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
    }
    ftrii = trii;
    for (trii = 0; trii < Z.z().tril; trii++)
	if (pointintri(trii, eps.plus(1).getStruct()))
	    break;
    if (trii == Z.z().tril) {
        System.err.println("libpath/%s:%d: %s\n" + "graphviz-2.38.0\\lib\\pathplan\\shortest.c" + 26 + ("destination point not in any triangle"));
        return -1;
    }
    ltrii = trii;
    /* mark the strip of triangles from eps[0] to eps[1] */
    if (N(marktripath(ftrii, ltrii))) {
	System.err.println("libpath/%s:%d: %s" + "graphviz-2.38.0\\lib\\pathplan\\shortest.c" + 26 + ("cannot find triangle path"));
	/* a straight line is better than failing */
	growops(2);
	output.pn = 2;
	Z.z().ops_shortest.plus(0).setStruct(eps.plus(0).getStruct());
	Z.z().ops_shortest.plus(1).setStruct(eps.plus(1).getStruct());
	output.setPtr("ps", Z.z().ops_shortest);
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
    epnls[0].pp = eps.get(0);
    epnls[0].link = null;
    epnls[1].pp = eps.get(1);
    epnls[1].link = null;
    add2dq(1, epnls[0]);
    Z.z().dq.apex = Z.z().dq.fpnlpi;
    trii = ftrii;
    while (trii != -1) {
	trip = Z.z().tris.plusJ(trii);
	trip.get(0).mark = 2;
	/* find the left and right points of the exiting edge */
	for (ei = 0; ei < 3; ei++)
	    if (trip.get(0).e[ei].rtp!=null &&
	    		trip.get(0).e[ei].rtp.get(0).mark == 1)
		break;
	if (ei == 3) {		/* in last triangle */
	    if (ccw(eps.get(1), Z.z().dq.pnlps[Z.z().dq.fpnlpi].pp,
		    Z.z().dq.pnlps[Z.z().dq.lpnlpi].pp) == 1)
		{
		lpnlp = Z.z().dq.pnlps[Z.z().dq.lpnlpi];
		rpnlp = epnls[1];
	    } else {
		lpnlp = epnls[1];
		rpnlp = Z.z().dq.pnlps[Z.z().dq.lpnlpi];
		}
	} else {
	    pnlp = trip.get(0).e[(ei + 1) % 3].pnl1p;
	    if (ccw(trip.get(0).e[ei].pnl0p.pp, pnlp.pp,
	    		trip.get(0).e[ei].pnl1p.pp) == 1)
UNSUPPORTED("2cii65lhw4wb8nyvjv702v7md"); // 		lpnlp = trip->e[ei].pnl1p, rpnlp = trip->e[ei].pnl0p;
	    else
		{
		  lpnlp = trip.get(0).e[ei].pnl0p;
		  rpnlp = trip.get(0).e[ei].pnl1p;
		}
	}
	/* update deque */
	if (trii == ftrii) {
	    add2dq(2, lpnlp);
	    add2dq(1, rpnlp);
	} else {
	    if (NEQ(Z.z().dq.pnlps[Z.z().dq.fpnlpi], rpnlp)
		&& NEQ(Z.z().dq.pnlps[Z.z().dq.lpnlpi], rpnlp)) {
		/* add right point to deque */
		splitindex = finddqsplit(rpnlp);
		splitdq(2, splitindex);
		add2dq(1, rpnlp);
		/* if the split is behind the apex, then reset apex */
		if (splitindex > Z.z().dq.apex)
		    Z.z().dq.apex = splitindex;
	    } else {
		/* add left point to deque */
		splitindex = finddqsplit(lpnlp);
		splitdq(1, splitindex);
		add2dq(2, lpnlp);
		/* if the split is in front of the apex, then reset apex */
		if (splitindex < Z.z().dq.apex)
		    Z.z().dq.apex = splitindex;
	    }
	}
	trii = -1;
	for (ei = 0; ei < 3; ei++)
	    if (trip.get(0).e[ei].rtp!=null && 
	    		trip.get(0).e[ei].rtp.get(0).mark == 1) {
		trii = trip.get(0).e[ei].rtp.minus(Z.z().tris);
		break;
	    }
    }
    for (pi = 0, pnlp = epnls[1]; pnlp!=null; pnlp = pnlp.link)
	pi++;
    growops(pi);
    output.pn = pi;
    for (pi = pi - 1, pnlp = epnls[1]; pnlp!=null; pi--, pnlp = pnlp.link)
	Z.z().ops_shortest.get(pi).____(pnlp.pp);
    output.ps = Z.z().ops_shortest;
    return 0;
} finally {
LEAVING("2gub5b19vo2qexn56nw23wage","Pshortestpath");
}
}




//3 73cr7m3mqvtuotpzrmaw2y8zm
// static void triangulate(pointnlink_t ** pnlps, int pnln) 
public static void triangulate(ST_pointnlink_t pnlps[], int pnln) {
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
				loadtriangle(pnlps[pnli], pnlps[pnlip1], pnlps[pnlip2]);
				for (pnli = pnlip1; pnli < pnln - 1; pnli++)
					pnlps[pnli] = pnlps[pnli + 1];
				triangulate(pnlps, pnln - 1);
				return;
			}
		}
		throw new IllegalStateException("libpath/%s:%d: %s\n" + "graphviz-2.38.0\\lib\\pathplan\\shortest.c" + 26 + ("triangulation failed"));
    } 
	else
		loadtriangle(pnlps[0], pnlps[1], pnlps[2]);
} finally {
LEAVING("73cr7m3mqvtuotpzrmaw2y8zm","triangulate");
}
}




//3 72of3cd7shtwokglxapw04oe9
// static int isdiagonal(int pnli, int pnlip2, pointnlink_t ** pnlps, 		      int pnln) 
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
	res = (ccw(pnlps[pnli].pp, pnlps[pnlip2].pp,
		   pnlps[pnlip1].pp) == 2);
    if (N(res))
	return false;
    /* check against all other edges */
    for (pnlj = 0; pnlj < pnln; pnlj++) {
	pnljp1 = (pnlj + 1) % pnln;
	if (N((pnlj == pnli) || (pnljp1 == pnli) ||
	      (pnlj == pnlip2) || (pnljp1 == pnlip2)))
	    if (intersects(pnlps[pnli].pp, pnlps[pnlip2].pp,
			   pnlps[pnlj].pp, pnlps[pnljp1].pp))
		return false;
    }
    return ((N(false)));
} finally {
LEAVING("72of3cd7shtwokglxapw04oe9","isdiagonal");
}
}




//3 7vf9jtj9i8rg0cxrstbqswuck
// static void loadtriangle(pointnlink_t * pnlap, pointnlink_t * pnlbp, 			 pointnlink_t * pnlcp) 
public static void loadtriangle(__ptr__ pnlap, __ptr__ pnlbp, __ptr__ pnlcp) {
ENTERING("7vf9jtj9i8rg0cxrstbqswuck","loadtriangle");
try {
    ST_triangle_t.Array trip;
    int ei;
    /* make space */
    if (Z.z().tril >= Z.z().trin)
	growtris(Z.z().trin + 20);
    trip = Z.z().tris.plusJ(Z.z().tril++);
    trip.get(0).mark = 0;
    trip.get(0).e[0].pnl0p = (ST_pointnlink_t) pnlap;
    trip.get(0).e[0].pnl1p = (ST_pointnlink_t) pnlbp;
    trip.get(0).e[0].rtp = null;
    trip.get(0).e[1].pnl0p = (ST_pointnlink_t) pnlbp;
    trip.get(0).e[1].pnl1p = (ST_pointnlink_t) pnlcp;
    trip.get(0).e[1].rtp = null;
    trip.get(0).e[2].pnl0p = (ST_pointnlink_t) pnlcp;
    trip.get(0).e[2].pnl1p = (ST_pointnlink_t) pnlap;
    trip.get(0).e[2].rtp = null;
    for (ei = 0; ei < 3; ei++)
	trip.get(0).e[ei].lrp = trip;
} finally {
LEAVING("7vf9jtj9i8rg0cxrstbqswuck","loadtriangle");
}
}




//3 6coujw0qksrgu5sxj0r39qm1u
// static void connecttris(int tri1, int tri2) 
public static void connecttris(int tri1, int tri2) {
ENTERING("6coujw0qksrgu5sxj0r39qm1u","connecttris");
try {
	ST_triangle_t.Array tri1p;
	ST_triangle_t.Array tri2p;
    int ei, ej;
    for (ei = 0; ei < 3; ei++) {
	for (ej = 0; ej < 3; ej++) {
	    tri1p = Z.z().tris.plusJ(tri1);
	    tri2p = Z.z().tris.plusJ(tri2);
	    if ((EQ(tri1p.get(0).e[ei].pnl0p.pp,
	    		tri2p.get(0).e[ej].pnl0p.pp) &&
		 EQ(tri1p.get(0).e[ei].pnl1p.pp,
				 tri2p.get(0).e[ej].pnl1p.pp)) ||
		(EQ(tri1p.get(0).e[ei].pnl0p.pp,
				tri2p.get(0).e[ej].pnl1p.pp) &&
		 EQ(tri1p.get(0).e[ei].pnl1p.pp,
				 tri2p.get(0).e[ej].pnl0p.pp)))
		 {
	    	tri1p.get(0).e[ei].rtp = tri2p;
	    	tri2p.get(0).e[ej].rtp = tri1p;
		 }
	}
    }
} finally {
LEAVING("6coujw0qksrgu5sxj0r39qm1u","connecttris");
}
}




//3 3waxf5wy3mwt12wpg5hxg3o9c
// static int marktripath(int trii, int trij) 
public static boolean marktripath(int trii, int trij) {
ENTERING("3waxf5wy3mwt12wpg5hxg3o9c","marktripath");
try {
    int ei;
    if (Z.z().tris.plusJ(trii).get(0).mark!=0)
	return false;
    Z.z().tris.plusJ(trii).get(0).mark = 1;
    if (trii == trij)
	return ((!(false)));
    for (ei = 0; ei < 3; ei++)
	if ((Z.z().tris.plusJ(trii).get(0).e[ei].rtp!=null &&
	    marktripath(Z.z().tris.plusJ(trii).get(0).e[ei].rtp.minus(Z.z().tris), trij)))
	    return ((!(false)));
    Z.z().tris.plusJ(trii).get(0).mark = 0;
    return false;
} finally {
LEAVING("3waxf5wy3mwt12wpg5hxg3o9c","marktripath");
}
}




//3 44szdl31mg8mt5qrfj70kb2sn
// static void add2dq(int side, pointnlink_t * pnlp) 
public static void add2dq(int side, ST_pointnlink_t pnlp) {
ENTERING("44szdl31mg8mt5qrfj70kb2sn","add2dq");
try {
    if (side == 1) {
	if (Z.z().dq.lpnlpi - Z.z().dq.fpnlpi >= 0)
	    pnlp.link = Z.z().dq.pnlps[Z.z().dq.fpnlpi];
	    /* shortest path links */
	Z.z().dq.fpnlpi = Z.z().dq.fpnlpi-1;
	Z.z().dq.pnlps[Z.z().dq.fpnlpi] = pnlp;
    } else {
	if (Z.z().dq.lpnlpi - Z.z().dq.fpnlpi >= 0)
	    pnlp.link = Z.z().dq.pnlps[Z.z().dq.lpnlpi];
	    /* shortest path links */
	Z.z().dq.lpnlpi = Z.z().dq.lpnlpi+1;
	Z.z().dq.pnlps[Z.z().dq.lpnlpi] = pnlp;
    }
} finally {
LEAVING("44szdl31mg8mt5qrfj70kb2sn","add2dq");
}
}




//3 572sssdz1se16w790xceiy5vr
// static void splitdq(int side, int index) 
public static void splitdq(int side, int index) {
ENTERING("572sssdz1se16w790xceiy5vr","splitdq");
try {
    if (side == 1)
	Z.z().dq.lpnlpi = index;
    else
	Z.z().dq.fpnlpi = index;
} finally {
LEAVING("572sssdz1se16w790xceiy5vr","splitdq");
}
}




//3 9dnrc8vqpffp5t3bmsackgqtl
// static int finddqsplit(pointnlink_t * pnlp) 
public static int finddqsplit(ST_pointnlink_t pnlp) {
ENTERING("9dnrc8vqpffp5t3bmsackgqtl","finddqsplit");
try {
    int index;
    for (index = Z.z().dq.fpnlpi; index < Z.z().dq.apex; index++)
	if (ccw(Z.z().dq.pnlps[index + 1].pp, Z.z().dq.pnlps[index].pp, pnlp.pp) ==
	    1)
	    return index;
    for (index = Z.z().dq.lpnlpi; index > Z.z().dq.apex; index--)
	if (ccw(Z.z().dq.pnlps[index - 1].pp, Z.z().dq.pnlps[index].pp, pnlp.pp) ==
	    2)
	    return index;
    return Z.z().dq.apex;
} finally {
LEAVING("9dnrc8vqpffp5t3bmsackgqtl","finddqsplit");
}
}




//3 72h03s8inxtto2ekvmuqjtj3d
// static int ccw(Ppoint_t * p1p, Ppoint_t * p2p, Ppoint_t * p3p) 
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
public static boolean between(ST_pointf pap, ST_pointf pbp, ST_pointf pcp) {
ENTERING("uh5n18rzyevtb4cwpni70qpc","between");
try {
    final ST_pointf p1 = new ST_pointf(), p2 = new ST_pointf();
    p1.setDouble("x", pbp.x - pap.x);
    p1.setDouble("y", pbp.y - pap.y);
    p2.setDouble("x", pcp.x - pap.x);
    p2.setDouble("y", pcp.y - pap.y);
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
public static boolean pointintri(int trii, ST_pointf pp) {
ENTERING("zti1mzm2m7tr2xwnbf7e8u3d","pointintri");
try {
    int ei, sum;
    for (ei = 0, sum = 0; ei < 3; ei++)
	if (ccw(Z.z().tris.plusJ(trii).get(0).e[ei].pnl0p.pp,
			Z.z().tris.plusJ(trii).get(0).e[ei].pnl1p.pp, pp) != 2)
	    sum++;
    return (sum == 3 || sum == 0);
} finally {
LEAVING("zti1mzm2m7tr2xwnbf7e8u3d","pointintri");
}
}




//3 85wstb60jkjd0kbh9tyninm4h
// static void growpnls(int newpnln) 
public static void growpnls(int newpnln) {
ENTERING("85wstb60jkjd0kbh9tyninm4h","growpnls");
try {
    if (newpnln <= Z.z().pnln)
	return;
    if (N(Z.z().pnls)) {
	if (N(Z.z().pnls = malloc(newpnln))) {
UNSUPPORTED("9zyfc4bjg3i6rrna9vqf8doys"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
	if (N(Z.z().pnlps = malloc(newpnln))) {
UNSUPPORTED("1etar0wd2cbbvqo4jnmbvjiz4"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc pnlps"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (N(Z.z().pnls = realloc(Z.z().pnls, newpnln))) {
UNSUPPORTED("105nogpkt0qqut0yu4alvkk1u"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
	if (N(Z.z().pnlps = realloc(Z.z().pnlps, newpnln))) {
UNSUPPORTED("be84alh84ub40x4um989aj20d"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc pnlps"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    Z.z().pnln = newpnln;
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
public static void growtris(int newtrin) {
ENTERING("c5q8ult6w26jppe5ifzgcoq82","growtris");
try {
    if (newtrin <= Z.z().trin)
	return;
    if (N(Z.z().tris)) {
	if (N(Z.z().tris = new ST_triangle_t.Array(newtrin))) {
UNSUPPORTED("5782e28cjpaa3dpf8up4zmtq7"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc tris"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (N(Z.z().tris = Z.z().tris.reallocJ(newtrin))) {
UNSUPPORTED("d3fgu54pn5tydfhn7z73v73ra"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc tris"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    Z.z().trin = newtrin;
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
public static void growdq(int newdqn) {
ENTERING("bzym9u6dtatii1vp4hcmofc80","growdq");
try {
    if (newdqn <= Z.z().dq.pnlpn)
	return;
    if (N(Z.z().dq.pnlps)) {
	if (N
	    (Z.z().dq.malloc(newdqn))) {
UNSUPPORTED("88fwpb40wz9jc8jiz7u032s4t"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc dq.pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (N(Z.z().dq.realloc(newdqn))) {
UNSUPPORTED("exqx4ck7h15m8whgip6xpnhoo"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc dq.pnls"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    Z.z().dq.pnlpn = newdqn;
} finally {
LEAVING("bzym9u6dtatii1vp4hcmofc80","growdq");
}
}




//3 d7vtt8xqxbdnx9kwtt1zzof75
// static void growops(int newopn) 
public static void growops(int newopn) {
ENTERING("d7vtt8xqxbdnx9kwtt1zzof75","growops");
try {
    if (newopn <= Z.z().opn_shortest)
	return;
    if (N(Z.z().ops_shortest)) {
	if (N(Z.z().ops_shortest = new ST_pointf.Array(newopn))) {
UNSUPPORTED("7wxgcgah7iy6vetj5yszoq4k4"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot malloc ops"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (N(Z.z().ops_shortest = Z.z().ops_shortest.reallocJ(newopn))) {
UNSUPPORTED("7azrdo5s3kc44taqmtmeu1s33"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\shortest.c", 26, ("cannot realloc ops"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
    }
    }
    Z.z().opn_shortest = newopn;
} finally {
LEAVING("d7vtt8xqxbdnx9kwtt1zzof75","growops");
}
}


}
