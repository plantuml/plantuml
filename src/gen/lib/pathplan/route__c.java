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
import static gen.lib.pathplan.solvers__c.solve3;
import static smetana.core.JUtils.setjmp;
import static smetana.core.JUtils.sqrt;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.DISTSQ;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Pedge_t;
import h.ST_Ppoly_t;
import h.ST_pointf;
import h.ST_tna_t;
import smetana.core.Z;
import smetana.core.__ptr__;
import smetana.core.jmp_buf;

public class route__c {
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();

//1 3k2f2er3efsrl0210su710vf
// static Ppoint_t *ops
//static private __ptr__ ops;

//1 ds2k0zdfzruet3qxk0duytkjx
// static int opn, opl
//private static int opn;
//private static int opl;



//3 9stmrdqlmufyk2wutp3totr5j
// int Proutespline(Pedge_t * edges, int edgen, Ppolyline_t input, 		 Ppoint_t * evs, Ppolyline_t * output) 
public static int Proutespline(ST_Pedge_t.Array edges, int edgen, ST_Ppoly_t input, __ptr__ evs, ST_Ppoly_t output) {
// WARNING!! STRUCT
return Proutespline_w_(edges, edgen, (ST_Ppoly_t) input.copy(), evs, output);
}
private static int Proutespline_w_(ST_Pedge_t.Array edges, int edgen, final ST_Ppoly_t input, __ptr__ evs, ST_Ppoly_t output) {
ENTERING("9stmrdqlmufyk2wutp3totr5j","Proutespline");
try {
	ST_pointf.Array inps;
    int inpn;
    /* unpack into previous format rather than modify legacy code */
    inps = input.ps;
    inpn = input.pn;
    if (setjmp(jbuf)!=0)
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
    /* generate the splines */
    evs.plus(0).getStruct().___(normv((ST_pointf) evs.plus(0).getStruct()));
    evs.plus(1).getStruct().___(normv((ST_pointf) evs.plus(1).getStruct()));
    Z.z().opl = 0;
    growops(4);
    Z.z().ops_route.plus(Z.z().opl).setStruct(inps.plus(0).getStruct());
    Z.z().opl++;
    if (reallyroutespline(edges, edgen, inps, inpn, (ST_pointf)evs.plus(0).getStruct(), (ST_pointf)evs.plus(1).getStruct()) == -1)
	return -1;
    output.setInt("pn", Z.z().opl);
    output.ps = Z.z().ops_route;
    return 0;
} finally {
LEAVING("9stmrdqlmufyk2wutp3totr5j","Proutespline");
}
}




//3 13dxqzbgtpl4ubnnvw6ehzzi9
// static int reallyroutespline(Pedge_t * edges, int edgen, 			     Ppoint_t * inps, int inpn, Ppoint_t ev0, 			     Ppoint_t ev1) 
//private static __ptr__ tnas;
//private static int tnan;
public static int reallyroutespline(ST_Pedge_t.Array edges, int edgen, ST_pointf.Array inps, int inpn, final ST_pointf ev0, final ST_pointf ev1) {
// WARNING!! STRUCT
return reallyroutespline_w_(edges, edgen, inps, inpn, ev0.copy(), ev1.copy());
}
private static int reallyroutespline_w_(ST_Pedge_t.Array edges, int edgen, ST_pointf.Array inps, int inpn, final ST_pointf ev0, final ST_pointf ev1) {
ENTERING("13dxqzbgtpl4ubnnvw6ehzzi9","reallyroutespline");
try {
    final ST_pointf p1 = new ST_pointf(), p2 = new ST_pointf(), cp1 = new ST_pointf(), cp2 = new ST_pointf(), p = new ST_pointf();
    final ST_pointf v1 = new ST_pointf(), v2 = new ST_pointf(), splitv = new ST_pointf(), splitv1 = new ST_pointf(), splitv2 = new ST_pointf();
    double maxd, d, t;
    int maxi, i, spliti;
    if (Z.z().tnan < inpn) {
	if (N(Z.z().tnas)) {
	    if (N(Z.z().tnas = new ST_tna_t.Array(inpn)))
		return -1;
	} else {
	    if (N(Z.z().tnas = Z.z().tnas.reallocJ(inpn)))
		return -1;
	}
	Z.z().tnan = inpn;
    }
    Z.z().tnas.plus(0).getStruct().setDouble("t", 0);
    for (i = 1; i < inpn; i++)
	Z.z().tnas.plus(i).getStruct().setDouble("t", Z.z().tnas.plus(i-1).getStruct().t + dist((ST_pointf)inps.plus(i).getStruct(), (ST_pointf)inps.plus(i-1).getStruct()));
    for (i = 1; i < inpn; i++)
	Z.z().tnas.plus(i).getStruct().setDouble("t", Z.z().tnas.plus(i).getStruct().t / Z.z().tnas.plus(inpn - 1).getStruct().t);
    for (i = 0; i < inpn; i++) {
	((ST_tna_t)Z.z().tnas.plus(i).getStruct()).a[0].____(scale(ev0, B1(Z.z().tnas.plus(i).getStruct().t)));
	((ST_tna_t)Z.z().tnas.plus(i).getStruct()).a[1].____(scale(ev1, B2(Z.z().tnas.plus(i).getStruct().t)));
    }
    if (mkspline(inps, inpn, Z.z().tnas, ev0, ev1, p1, v1, p2, v2) == -1)
	return -1;
    if (splinefits(edges, edgen, p1, v1, p2, v2, inps, inpn)!=0)
	return 0;
    cp1.___(add(p1, scale(v1, 1 / 3.0)));
    cp2.___(sub(p2, scale(v2, 1 / 3.0)));
    for (maxd = -1, maxi = -1, i = 1; i < inpn - 1; i++) {
	t = Z.z().tnas.get(i).t;
	p.setDouble("x", B0(t) * p1.x + B1(t) * cp1.x + B2(t) * cp2.x + B3(t) * p2.x);
	p.setDouble("y", B0(t) * p1.y + B1(t) * cp1.y + B2(t) * cp2.y + B3(t) * p2.y);
	if ((d = dist(p, (ST_pointf) inps.plus(i).getStruct())) > maxd)
	    {maxd = d; maxi = i;}
    }
    spliti = maxi;
    splitv1.___(normv(sub((ST_pointf)inps.plus(spliti).getStruct(), (ST_pointf)inps.plus(spliti - 1).getStruct())));
    splitv2.___(normv(sub((ST_pointf)inps.plus(spliti + 1).getStruct(), (ST_pointf)inps.plus(spliti).getStruct())));
    splitv.___(normv(add(splitv1, splitv2)));
    reallyroutespline(edges, edgen, inps, spliti + 1, ev0, splitv);
    reallyroutespline(edges, edgen, inps.plus(spliti), inpn - spliti, splitv,
		      ev1);
    return 0;
} finally {
LEAVING("13dxqzbgtpl4ubnnvw6ehzzi9","reallyroutespline");
}
}




//3 29sok6jkfyobf83q130snkhmh
// static int mkspline(Ppoint_t * inps, int inpn, tna_t * tnas, Ppoint_t ev0, 		    Ppoint_t ev1, Ppoint_t * sp0, Ppoint_t * sv0, 		    Ppoint_t * sp1, Ppoint_t * sv1) 
public static int mkspline(__ptr__ inps, int inpn, ST_tna_t.Array tnas, final ST_pointf ev0, final ST_pointf ev1, __ptr__ sp0, __ptr__ sv0, __ptr__ sp1, __ptr__ sv1) {
// WARNING!! STRUCT
return mkspline_w_(inps, inpn, tnas, ev0.copy(), ev1.copy(), sp0, sv0, sp1, sv1);
}
private static int mkspline_w_(__ptr__ inps, int inpn, ST_tna_t.Array tnas, final ST_pointf ev0, final ST_pointf ev1, __ptr__ sp0, __ptr__ sv0, __ptr__ sp1, __ptr__ sv1) {
ENTERING("29sok6jkfyobf83q130snkhmh","mkspline");
try {
    final ST_pointf tmp = new ST_pointf();
    double c[][] = new double[2][2];
    double x[] = new double[2];
    double det01, det0X, detX1;
    double d01, scale0, scale3;
    int i;
    scale0 = scale3 = 0.0;
    c[0][0] = c[0][1] = c[1][0] = c[1][1] = 0.0;
    x[0] = x[1] = 0.0;
    for (i = 0; i < inpn; i++) {
	c[0][0] += dot(((ST_tna_t)tnas.plus(i).getStruct()).a[0], ((ST_tna_t)tnas.plus(i).getStruct()).a[0]);
	c[0][1] += dot(((ST_tna_t)tnas.plus(i).getStruct()).a[0], ((ST_tna_t)tnas.plus(i).getStruct()).a[1]);
	c[1][0] = c[0][1];
	c[1][1] += dot(((ST_tna_t)tnas.plus(i).getStruct()).a[1], ((ST_tna_t)tnas.plus(i).getStruct()).a[1]);
	tmp.___(sub((ST_pointf) inps.plus(i).getStruct(), add(scale((ST_pointf) inps.plus(0).getStruct(), B01(tnas.plus(i).getStruct().t)),
			       scale((ST_pointf) inps.plus(inpn - 1).getStruct(), B23(tnas.plus(i).getStruct().t)))));
	x[0] += dot(((ST_tna_t)tnas.plus(i).getStruct()).a[0], tmp);
	x[1] += dot(((ST_tna_t)tnas.plus(i).getStruct()).a[1], tmp);
    }
    det01 = c[0][0] * c[1][1] - c[1][0] * c[0][1];
    det0X = c[0][0] * x[1] - c[0][1] * x[0];
    detX1 = x[0] * c[1][1] - x[1] * c[0][1];
    if (((det01) >= 0 ? (det01) : -(det01)) >= 1e-6) {
	scale0 = detX1 / det01;
	scale3 = det0X / det01;
    }
    if (((det01) >= 0 ? (det01) : -(det01)) < 1e-6 || scale0 <= 0.0 || scale3 <= 0.0) {
	d01 = dist((ST_pointf)inps.plus(0).getStruct(), (ST_pointf)inps.plus(inpn - 1).getStruct()) / 3.0;
	scale0 = d01;
	scale3 = d01;
    }
    sp0.setStruct(inps.plus(0).getStruct());
    sv0.setStruct(scale(ev0, scale0));
    sp1.setStruct(inps.plus(inpn - 1).getStruct());
    sv1.setStruct(scale(ev1, scale3));
    return 0;
} finally {
LEAVING("29sok6jkfyobf83q130snkhmh","mkspline");
}
}




//3 ea6jsc0rwfyjtmmuxax6r5ngk
// static double dist_n(Ppoint_t * p, int n) 
public static double dist_n(ST_pointf.Array p, int n) {
ENTERING("ea6jsc0rwfyjtmmuxax6r5ngk","dist_n");
try {
    int i;
    double rv;
    rv = 0.0;
    for (i = 1; i < n; i++) {
	rv +=
	    sqrt((p.plus(i).getStruct().x - p.plus(i - 1).getStruct().x) * (p.plus(i).getStruct().x - p.plus(i - 1).getStruct().x)+
		 (p.plus(i).getStruct().y - p.plus(i - 1).getStruct().y) * (p.plus(i).getStruct().y - p.plus(i - 1).getStruct().y));
    }
    return rv;
} finally {
LEAVING("ea6jsc0rwfyjtmmuxax6r5ngk","dist_n");
}
}




//3 987ednrgu5qo9dzhpiox47mhb
// static int splinefits(Pedge_t * edges, int edgen, Ppoint_t pa, 		      Pvector_t va, Ppoint_t pb, Pvector_t vb, 		      Ppoint_t * inps, int inpn) 
public static int splinefits(ST_Pedge_t.Array edges, int edgen, final ST_pointf pa, final ST_pointf va, final ST_pointf pb, final ST_pointf vb, ST_pointf.Array inps, int inpn) {
// WARNING!! STRUCT
return splinefits_w_(edges, edgen, pa.copy(), va.copy(), pb.copy(), vb.copy(), inps, inpn);
}
private static int splinefits_w_(ST_Pedge_t.Array edges, int edgen, final ST_pointf pa, final ST_pointf va, final ST_pointf pb, final ST_pointf vb, ST_pointf.Array inps, int inpn) {
ENTERING("987ednrgu5qo9dzhpiox47mhb","splinefits");
try {
    final ST_pointf.Array sps = new ST_pointf.Array( 4);
    double a, b;
    int pi;
    int forceflag;
    int first = 1;
    forceflag = (inpn == 2 ? 1 : 0);
    a = b = 4;
    for (;;) {
	sps.plus(0).getStruct().setDouble("x", pa.x);
	sps.plus(0).getStruct().setDouble("y", pa.y);
	sps.plus(1).getStruct().setDouble("x", pa.x + a * va.x / 3.0);
	sps.plus(1).getStruct().setDouble("y", pa.y + a * va.y / 3.0);
	sps.plus(2).getStruct().setDouble("x", pb.x - b * vb.x / 3.0);
	sps.plus(2).getStruct().setDouble("y", pb.y - b * vb.y / 3.0);
	sps.plus(3).getStruct().setDouble("x", pb.x);
	sps.plus(3).getStruct().setDouble("y", pb.y);
	/* shortcuts (paths shorter than the shortest path) not allowed -
	 * they must be outside the constraint polygon.  this can happen
	 * if the candidate spline intersects the constraint polygon exactly
	 * on sides or vertices.  maybe this could be more elegant, but
	 * it solves the immediate problem. we could also try jittering the
	 * constraint polygon, or computing the candidate spline more carefully,
	 * for example using the path. SCN */
	if (first!=0 && (dist_n(sps.asPtr(), 4) < (dist_n(inps, inpn) - 1E-3)))
	    return 0;
	first = 0;
	if (splineisinside(edges, edgen, sps)) {
	    growops(Z.z().opl + 4);
	    for (pi = 1; pi < 4; pi++) {
		Z.z().ops_route.plus(Z.z().opl).setDouble("x", sps.plus(pi).getStruct().x);
		Z.z().ops_route.plus(Z.z().opl).setDouble("y", sps.plus(pi).getStruct().y);
		Z.z().opl++;
		}
	    return 1;
	}
	if (a == 0 && b == 0) {
	    if (forceflag!=0) {
		growops(Z.z().opl + 4);
		for (pi = 1; pi < 4; pi++)
		{
			Z.z().ops_route.plus(Z.z().opl).setDouble("x", sps.plus(pi).getStruct().x);
			Z.z().ops_route.plus(Z.z().opl).setDouble("y", sps.plus(pi).getStruct().y);
		    Z.z().opl++;
		}
		return 1;
	    }
	    break;
	}
	if (a > .01)
	{
	    a /= 2;
	    b /= 2;
	}
	else
	    a = b = 0;
    }
    return 0;
} finally {
LEAVING("987ednrgu5qo9dzhpiox47mhb","splinefits");
}
}




//3 b6eghkeu16aum3l778ig52ht1
// static int splineisinside(Pedge_t * edges, int edgen, Ppoint_t * sps) 
public static boolean splineisinside(ST_Pedge_t.Array edges, int edgen, ST_pointf.Array sps) {
ENTERING("b6eghkeu16aum3l778ig52ht1","splineisinside");
try {
    double roots[] = new double[4];
    int rooti, rootn;
    int ei;
    final ST_pointf.Array lps = new ST_pointf.Array( 2);
    final ST_pointf ip = new ST_pointf();
    double t, ta, tb, tc, td;
    for (ei = 0; ei < edgen; ei++) {
	lps.plus(0).setStruct(edges.get(ei).a);
	lps.plus(1).setStruct(edges.get(ei).b);
	/* if ((rootn = splineintersectsline (sps, lps, roots)) == 4)
	   return 1; */
	if ((rootn = splineintersectsline(sps, lps, roots)) == 4)
	    continue;
	for (rooti = 0; rooti < rootn; rooti++) {
	    if (roots[rooti] < 1E-6 || roots[rooti] > 1 - 1E-6)
		continue;
	    t = roots[rooti];
	    td = t * t * t;
	    tc = 3 * t * t * (1 - t);
	    tb = 3 * t * (1 - t) * (1 - t);
	    ta = (1 - t) * (1 - t) * (1 - t);
	    ip.setDouble("x", ta * sps.get(0).x + tb * sps.get(1).x +
		tc * sps.get(2).x + td * sps.get(3).x);
	    ip.setDouble("y", ta * sps.get(0).y + tb * sps.get(1).y +
		tc * sps.get(2).y + td * sps.get(3).y);
	    if (DISTSQ(ip, lps.plus(0).getStruct()) < 1E-3 ||
		DISTSQ(ip, lps.plus(1).getStruct()) < 1E-3)
		continue;
	    return false;
	}
    }
    return true;
} finally {
LEAVING("b6eghkeu16aum3l778ig52ht1","splineisinside");
}
}




//3 32nc8itszi77u36la8npt2870
// static int splineintersectsline(Ppoint_t * sps, Ppoint_t * lps, 				double *roots) 
public static int splineintersectsline(ST_pointf.Array sps, ST_pointf.Array lps, double roots[]) {
ENTERING("32nc8itszi77u36la8npt2870","splineintersectsline");
try {
    double scoeff[] = new double[4];
    double xcoeff[] = new double[2];
    double ycoeff[] = new double[2];
    double xroots[] = new double[3];
    double yroots[] = new double[3];
    double tv, sv, rat;
    int rootn[] = new int[]{0};
    int xrootn, yrootn, i, j;
    xcoeff[0] = lps.plus(0).getStruct().x;
    xcoeff[1] = lps.plus(1).getStruct().x - lps.plus(0).getStruct().x;
    ycoeff[0] = lps.plus(0).getStruct().y;
    ycoeff[1] = lps.plus(1).getStruct().y - lps.plus(0).getStruct().y;
    rootn[0] = 0;
    if (xcoeff[1] == 0) {
 	if (ycoeff[1] == 0) {
	    points2coeff(sps.plus(0).getStruct().x, sps.plus(1).getStruct().x, sps.plus(2).getStruct().x, sps.plus(3).getStruct().x, scoeff);
	    scoeff[0] -= xcoeff[0];
	    xrootn = solve3(scoeff, xroots);
	    points2coeff(sps.plus(0).getStruct().y, sps.plus(1).getStruct().y, sps.plus(2).getStruct().y, sps.plus(3).getStruct().y, scoeff);
	    scoeff[0] -= ycoeff[0];
	    yrootn = solve3(scoeff, yroots);
	    if (xrootn == 4)
		if (yrootn == 4)
		    return 4;
		else
		    for (j = 0; j < yrootn; j++)
			addroot(yroots[j], roots, rootn);
	    else if (yrootn == 4)
		for (i = 0; i < xrootn; i++)
		    addroot(xroots[i], roots, rootn);
	    else
		for (i = 0; i < xrootn; i++)
		    for (j = 0; j < yrootn; j++)
			if (xroots[i] == yroots[j])
			    addroot(xroots[i], roots, rootn);
	    return rootn[0];
	} else {
 	    points2coeff(sps.plus(0).getStruct().x, sps.plus(1).getStruct().x, sps.plus(2).getStruct().x, sps.plus(3).getStruct().x, scoeff);
 	    scoeff[0] -= xcoeff[0];
	    xrootn = solve3(scoeff, xroots);
	    if (xrootn == 4)
		return 4;
	    for (i = 0; i < xrootn; i++) {
		tv = xroots[i];
		if (tv >= 0 && tv <= 1) {
		    points2coeff(sps.plus(0).getStruct().y, sps.plus(1).getStruct().y, sps.plus(2).getStruct().y, sps.plus(3).getStruct().y,
				 scoeff);
		    sv = scoeff[0] + tv * (scoeff[1] + tv *
					   (scoeff[2] + tv * scoeff[3]));
		    sv = (sv - ycoeff[0]) / ycoeff[1];
		    if ((0 <= sv) && (sv <= 1))
			addroot(tv, roots, rootn);
		}
	    }
	    return rootn[0];
	}
    } else {
	rat = ycoeff[1] / xcoeff[1];
	points2coeff(sps.plus(0).getStruct().y - rat * sps.plus(0).getStruct().x, sps.plus(1).getStruct().y - rat * sps.plus(1).getStruct().x,
		     sps.plus(2).getStruct().y - rat * sps.plus(2).getStruct().x, sps.plus(3).getStruct().y - rat * sps.plus(3).getStruct().x,
		     scoeff);
	scoeff[0] += rat * xcoeff[0] - ycoeff[0];
	xrootn = solve3(scoeff, xroots);
	if (xrootn == 4)
	    return 4;
	for (i = 0; i < xrootn; i++) {
	    tv = xroots[i];
	    if (tv >= 0 && tv <= 1) {
		points2coeff(sps.plus(0).getStruct().x, sps.plus(1).getStruct().x, sps.plus(2).getStruct().x, sps.plus(3).getStruct().x,
			     scoeff);
		sv = scoeff[0] + tv * (scoeff[1] +
				       tv * (scoeff[2] + tv * scoeff[3]));
		sv = (sv - xcoeff[0]) / xcoeff[1];
		if ((0 <= sv) && (sv <= 1))
		    addroot(tv, roots, rootn);
	    }
	}
	return rootn[0];
	}
} finally {
LEAVING("32nc8itszi77u36la8npt2870","splineintersectsline");
}
}




//3 9011b45d42bhwfxzhgxqnlfhp
// static void points2coeff(double v0, double v1, double v2, double v3, 			 double *coeff) 
public static void points2coeff(double v0, double v1, double v2, double v3, double coeff[]) {
ENTERING("9011b45d42bhwfxzhgxqnlfhp","points2coeff");
try {
    coeff[3] = v3 + 3 * v1 - (v0 + 3 * v2);
    coeff[2] = 3 * v0 + 3 * v2 - 6 * v1;
    coeff[1] = 3 * (v1 - v0);
    coeff[0] = v0;
} finally {
LEAVING("9011b45d42bhwfxzhgxqnlfhp","points2coeff");
}
}




//3 6ldk438jjflh0huxkg4cs8kwu
// static void addroot(double root, double *roots, int *rootnp) 
public static void addroot(double root, double roots[], int rootnp[]) {
ENTERING("6ldk438jjflh0huxkg4cs8kwu","addroot");
try {
    if (root >= 0 && root <= 1) {
	roots[rootnp[0]] = root;
	rootnp[0]++;
	}
} finally {
LEAVING("6ldk438jjflh0huxkg4cs8kwu","addroot");
}
}




//3 3i8m1m9fg7qmnt8jloorwlu8e
// static Pvector_t normv(Pvector_t v) 
public static ST_pointf normv(final ST_pointf v) {
// WARNING!! STRUCT
return normv_w_(v.copy()).copy();
}
private static ST_pointf normv_w_(final ST_pointf v) {
ENTERING("3i8m1m9fg7qmnt8jloorwlu8e","normv");
try {
    double d;
    d = v.x * v.x + v.y * v.y;
    if (d > 1e-6) {
	d = sqrt(d);
	v.setDouble("x", v.x / d);
	v.setDouble("y", v.y / d);
    }
    return v;
} finally {
LEAVING("3i8m1m9fg7qmnt8jloorwlu8e","normv");
}
}




//3 d59jcnpi1y0wr8e9uwxny2fvk
// static void growops(int newopn) 
public static void growops(int newopn) {
ENTERING("d59jcnpi1y0wr8e9uwxny2fvk","growops");
try {
    if (newopn <= Z.z().opn_route)
	return;
    if (N(Z.z().ops_route)) {
	if (N(Z.z().ops_route = new ST_pointf.Array(newopn))) {
UNSUPPORTED("413an1hqgkb4ezaec6qdsdplx"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\route.c", 32, ("cannot malloc ops"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    } else {
	if (N(Z.z().ops_route = Z.z().ops_route.reallocJ(newopn))) {
UNSUPPORTED("8u0qgahxvk5pplf90thmhwxhl"); // 	    fprintf (stderr, "libpath/%s:%d: %s\n", "graphviz-2.38.0\\lib\\pathplan\\route.c", 32, ("cannot realloc ops"));
UNSUPPORTED("1r6uhbnmxv8c6msnscw07w0qx"); // 	    longjmp(jbuf,1);
	}
    }
    Z.z().opn_route = newopn;
} finally {
LEAVING("d59jcnpi1y0wr8e9uwxny2fvk","growops");
}
}




//3 f4a7nt247bokdwr2owda050of
// static Ppoint_t add(Ppoint_t p1, Ppoint_t p2) 
public static ST_pointf add(final ST_pointf p1, final ST_pointf p2) {
// WARNING!! STRUCT
return add_w_(p1.copy(), p2.copy()).copy();
}
private static ST_pointf add_w_(final ST_pointf p1, final ST_pointf p2) {
ENTERING("f4a7nt247bokdwr2owda050of","add");
try {
    p1.setDouble("x", p1.x + p2.x);
    p1.setDouble("y", p1.y + p2.y);
    return p1;
} finally {
LEAVING("f4a7nt247bokdwr2owda050of","add");
}
}




//3 c4l1gvlkv2s4mi6os7r9dh89f
// static Ppoint_t sub(Ppoint_t p1, Ppoint_t p2) 
public static ST_pointf sub(final ST_pointf p1, final  ST_pointf p2) {
// WARNING!! STRUCT
return sub_w_(p1.copy(), p2.copy()).copy();
}
private static ST_pointf sub_w_(final ST_pointf p1, final  ST_pointf p2) {
ENTERING("c4l1gvlkv2s4mi6os7r9dh89f","sub");
try {
    p1.setDouble("x", p1.x - p2.x);
    p1.setDouble("y", p1.y - p2.y);
    return p1;
} finally {
LEAVING("c4l1gvlkv2s4mi6os7r9dh89f","sub");
}
}




//3 dqnlz0tceriykws4ngudl94w9
// static double dist(Ppoint_t p1, Ppoint_t p2) 
public static double dist(final ST_pointf p1, final ST_pointf p2) {
// WARNING!! STRUCT
return dist_w_(p1.copy(), p2.copy());
}
private static double dist_w_(final ST_pointf p1, final ST_pointf p2) {
ENTERING("dqnlz0tceriykws4ngudl94w9","dist");
try {
    double dx, dy;
    dx = p2.x - p1.x;
    dy = p2.y - p1.y;
    return sqrt(dx * dx + dy * dy);
} finally {
LEAVING("dqnlz0tceriykws4ngudl94w9","dist");
}
}




//3 19149pdllzhplvew0bsh5v6hy
// static Ppoint_t scale(Ppoint_t p, double c) 
public static ST_pointf scale(final ST_pointf p, double c) {
// WARNING!! STRUCT
return scale_w_(p.copy(), c).copy();
}
private static ST_pointf scale_w_(final ST_pointf p, double c) {
ENTERING("19149pdllzhplvew0bsh5v6hy","scale");
try {
    p.setDouble("x", p.x * c);
    p.setDouble("y", p.y * c);
    return p;
} finally {
LEAVING("19149pdllzhplvew0bsh5v6hy","scale");
}
}




//3 7ebsa2s1eoopqj1pp43bh5fw
// static double dot(Ppoint_t p1, Ppoint_t p2) 
public static double dot(final ST_pointf p1, final ST_pointf p2) {
// WARNING!! STRUCT
return dot_w_(p1.copy(), p2.copy());
}
private static double dot_w_(final ST_pointf p1, final ST_pointf p2) {
ENTERING("7ebsa2s1eoopqj1pp43bh5fw","dot");
try {
    return p1.x * p2.x + p1.y * p2.y;
} finally {
LEAVING("7ebsa2s1eoopqj1pp43bh5fw","dot");
}
}




//3 73nhv3cuxqa9va0puve0ji2d5
// static double B0(double t) 
public static double B0(double t) {
ENTERING("73nhv3cuxqa9va0puve0ji2d5","B0");
try {
    double tmp = 1.0 - t;
    return tmp * tmp * tmp;
} finally {
LEAVING("73nhv3cuxqa9va0puve0ji2d5","B0");
}
}




//3 jkfs4ak0xr5pzwye7qnm6irp
// static double B1(double t) 
public static double B1(double t) {
ENTERING("jkfs4ak0xr5pzwye7qnm6irp","B1");
try {
    double tmp = 1.0 - t;
    return 3 * t * tmp * tmp;
} finally {
LEAVING("jkfs4ak0xr5pzwye7qnm6irp","B1");
}
}




//3 9ziajuqys2xceftdw0vac02g9
// static double B2(double t) 
public static double B2(double t) {
ENTERING("9ziajuqys2xceftdw0vac02g9","B2");
try {
    double tmp = 1.0 - t;
    return 3 * t * t * tmp;
} finally {
LEAVING("9ziajuqys2xceftdw0vac02g9","B2");
}
}




//3 5sjstsgkvoou9grsty3y0cnvg
// static double B3(double t) 
public static double B3(double t) {
ENTERING("5sjstsgkvoou9grsty3y0cnvg","B3");
try {
    return t * t * t;
} finally {
LEAVING("5sjstsgkvoou9grsty3y0cnvg","B3");
}
}




//3 9hzfapzxcesobeegq4aokksbp
// static double B01(double t) 
public static double B01(double t) {
ENTERING("9hzfapzxcesobeegq4aokksbp","B01");
try {
    double tmp = 1.0 - t;
    return tmp * tmp * (tmp + 3 * t);
} finally {
LEAVING("9hzfapzxcesobeegq4aokksbp","B01");
}
}




//3 571cxp9l20eyvqjwdoy9vnc6t
// static double B23(double t) 
public static double B23(double t) {
ENTERING("571cxp9l20eyvqjwdoy9vnc6t","B23");
try {
    double tmp = 1.0 - t;
    return t * t * (3 * tmp + t);
} finally {
LEAVING("571cxp9l20eyvqjwdoy9vnc6t","B23");
}
}


}
