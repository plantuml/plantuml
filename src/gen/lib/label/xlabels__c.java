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
package gen.lib.label;
import static gen.lib.cdt.dtclose__c.dtclose;
import static gen.lib.cdt.dtopen__c.dtopen;
import static gen.lib.label.index__c.RTreeClose;
import static gen.lib.label.index__c.RTreeInsert;
import static gen.lib.label.index__c.RTreeLeafListFree;
import static gen.lib.label.index__c.RTreeOpen;
import static gen.lib.label.index__c.RTreeSearch;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_BestPos_t;
import h.ST_HDict_t;
import h.ST_LeafList_t;
import h.ST_Node_t___;
import h.ST_RTree;
import h.ST_Rect_t;
import h.ST_XLabels_t;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_label_params_t;
import h.ST_object_t;
import h.ST_point;
import h.ST_pointf;
import h.ST_xlabel_t;
import smetana.core.CArray;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.Globals;
import smetana.core.Memory;


public class xlabels__c {

public static CFunction icompare = new CFunctionAbstract("icompare") {
	
	public Object exe(Globals zz, Object... args) {
		return icompare((ST_dt_s)args[0], (Object)args[1], (Object)args[2], (ST_dtdisc_s)args[3]);
	}};
	
//3 5p3ac8qk4gnne5hj1dc21ysi
// static int icompare(Dt_t * dt, void * v1, void * v2, Dtdisc_t * disc) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="icompare", key="5p3ac8qk4gnne5hj1dc21ysi", definition="static int icompare(Dt_t * dt, void * v1, void * v2, Dtdisc_t * disc)")
public static int icompare(ST_dt_s dt, Object v1, Object v2, ST_dtdisc_s disc) {
ENTERING("5p3ac8qk4gnne5hj1dc21ysi","icompare");
try {
	Integer k1 = (Integer) v1;
	Integer k2 = (Integer) v2;
    return k1 - k2;
} finally {
LEAVING("5p3ac8qk4gnne5hj1dc21ysi","icompare");
}
}




//3 88mbfm305igsr7cew5qx6yldp
// static XLabels_t *xlnew(object_t * objs, int n_objs, 			xlabel_t * lbls, int n_lbls, 			label_params_t * params) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="", key="88mbfm305igsr7cew5qx6yldp", definition="static XLabels_t *xlnew(object_t * objs, int n_objs, 			xlabel_t * lbls, int n_lbls, 			label_params_t * params)")
public static ST_XLabels_t xlnew(Globals zz, CArray<ST_object_t> objs, int n_objs, CArray<ST_xlabel_t> lbls, int n_lbls, ST_label_params_t params) {
ENTERING("88mbfm305igsr7cew5qx6yldp","xlnew");
try {
ST_XLabels_t xlp;
xlp = new ST_XLabels_t();
/* used to load the rtree in hilbert space filling curve order */
xlp.hdx = dtopen(zz, zz.Hdisc, zz.Dtobag);
if ((xlp.hdx) == null) {
UNSUPPORTED("4t1y5iinm4310lkpvbal1spve"); // 	fprintf(stderr, "out of memory\n");
UNSUPPORTED("3m406diamp5s5kwcqtwo4pshf"); // 	goto bad;
}
/* for querying intersection candidates */
xlp.spdx = RTreeOpen();
if ((xlp.spdx) == null) {
UNSUPPORTED("4t1y5iinm4310lkpvbal1spve"); // 	fprintf(stderr, "out of memory\n");
UNSUPPORTED("3m406diamp5s5kwcqtwo4pshf"); // 	goto bad;
     }
/* save arg pointers in the handle */
xlp.objs = objs;
xlp.n_objs = n_objs;
xlp.lbls = lbls;
xlp.n_lbls = n_lbls;
xlp.params = params;
return (ST_XLabels_t) xlp;
} finally {
LEAVING("88mbfm305igsr7cew5qx6yldp","xlnew");
}
//UNSUPPORTED("98zx7s9vt8t1s5x9l35evcxnz"); //   bad:
//UNSUPPORTED("66s40csd2ivd8rx4h2ut8oai5"); //     if (xlp->hdx)
//UNSUPPORTED("8mg8tqxa78f1nfk4jh9drw2n0"); // 	dtclose(xlp->hdx);
//UNSUPPORTED("b9uy03exphaovgyz5t4gru409"); //     if (xlp->spdx)
//UNSUPPORTED("4cfpl6hom6vo3btrjlhfmn6mi"); // 	RTreeClose(xlp->spdx);
//UNSUPPORTED("dms04fhv1vao18v2p0lmk80xf"); //     free(xlp);
//UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
//UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
}




//3 apvhod2s1yjb8717rb7gie2kb
// static void xlfree(XLabels_t * xlp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlfree", key="apvhod2s1yjb8717rb7gie2kb", definition="static void xlfree(XLabels_t * xlp)")
public static void xlfree(ST_XLabels_t xlp) {
ENTERING("apvhod2s1yjb8717rb7gie2kb","xlfree");
try {
     RTreeClose((ST_RTree) xlp.spdx);
     Memory.free(xlp);
} finally {
LEAVING("apvhod2s1yjb8717rb7gie2kb","xlfree");
}
}




//3 6lz36gkh8fla3z6f0lxniy368
// static int floorLog2(unsigned int n) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="floorLog2", key="6lz36gkh8fla3z6f0lxniy368", definition="static int floorLog2(unsigned int n)")
public static int floorLog2(int n) {
ENTERING("6lz36gkh8fla3z6f0lxniy368","floorLog2");
try {
    int pos = 0;
    if (n == 0)
	return -1;
    if (n >= 1 << 16) {
	n >>= 16;
	pos += 16;
    }
    if (n >= 1 << 8) {
	n >>= 8;
	pos += 8;
    }
    if (n >= 1 << 4) {
	n >>= 4;
	pos += 4;
    }
    if (n >= 1 << 2) {
	n >>= 2;
	pos += 2;
    }
    if (n >= 1 << 1) {
	pos += 1;
    }
    return pos;
} finally {
LEAVING("6lz36gkh8fla3z6f0lxniy368","floorLog2");
}
}




//3 uvnzthcpf4xiih05gxie2rx1
// unsigned int xlhorder(XLabels_t * xlp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlhorder", key="uvnzthcpf4xiih05gxie2rx1", definition="unsigned int xlhorder(XLabels_t * xlp)")
public static int xlhorder(ST_XLabels_t xlp) {
ENTERING("uvnzthcpf4xiih05gxie2rx1","xlhorder");
try {
    double maxx = xlp.params.bb.UR.x;
    double maxy = xlp.params.bb.UR.y;
    return floorLog2(maxx > maxy ? (int)maxx : (int)maxy) + 1;
} finally {
LEAVING("uvnzthcpf4xiih05gxie2rx1","xlhorder");
}
}




//3 9lkyvq87bawe3yon7bdwvcjzq
// static unsigned int hd_hil_s_from_xy(point p, int n) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="hd_hil_s_from_xy", key="9lkyvq87bawe3yon7bdwvcjzq", definition="static unsigned int hd_hil_s_from_xy(point p, int n)")
public static int hd_hil_s_from_xy(ST_point p, int n) {
ENTERING("9lkyvq87bawe3yon7bdwvcjzq","hd_hil_s_from_xy");
try {
	return hd_hil_s_from_xy_((ST_point) p.copy(), n);
} finally {
LEAVING("9lkyvq87bawe3yon7bdwvcjzq","hd_hil_s_from_xy");
}
}
private static int hd_hil_s_from_xy_(ST_point p, int n) {
     int i, x = p.x, y = p.y, xi, yi;
     int s;
     s = 0;			/* Initialize. */
     for (i = n - 1; i >= 0; i--) {
 	xi = (x >> i) & 1;	/* Get bit i of x. */
 	yi = (y >> i) & 1;	/* Get bit i of y. */
 	s = 4 * s + 2 * xi + (xi ^ yi);	/* Append two bits to s. */
 	x = x ^ y;		/* These 3 lines swap */
 	y = y ^ (x & (yi - 1));	/* x and y if yi = 0. */
 	x = x ^ y;
 	x = x ^ (-xi & (yi - 1));	/* Complement x and y if */
 	y = y ^ (-xi & (yi - 1));	/* xi = 1 and yi = 0. */
     }
     return s;
 }




//3 bpkzwylrchx5wta1qhytfgbtr
// static double aabbaabb(Rect_t * r, Rect_t * s) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="aabbaabb", key="bpkzwylrchx5wta1qhytfgbtr", definition="static double aabbaabb(Rect_t * r, Rect_t * s)")
public static double aabbaabb(ST_Rect_t r, ST_Rect_t s) {
ENTERING("bpkzwylrchx5wta1qhytfgbtr","aabbaabb");
try {
     /* per dimension if( max < omin || min > omax) */
     double iminx, iminy, imaxx, imaxy;
     if (r.boundary[2] < s.boundary[0] || r.boundary[0] > s.boundary[2])
 	return 0;
     if (r.boundary[3] < s.boundary[1] || r.boundary[1] > s.boundary[3])
  	return 0;
     /* if we get here we have an intersection */
     /* rightmost left edge of the 2 rectangles */
     iminx =
 	r.boundary[0] > s.boundary[0] ? r.boundary[0] : s.boundary[0];
     /* upmost bottom edge */
     iminy =
 	r.boundary[1] > s.boundary[1] ? r.boundary[1] : s.boundary[1];
     /* leftmost right edge */
     imaxx =
 	r.boundary[2] < s.boundary[2] ? r.boundary[2] : s.boundary[2];
     /* downmost top edge */
     imaxy =
 	r.boundary[3] < s.boundary[3] ? r.boundary[3] : s.boundary[3];
     return (imaxx - iminx) * (imaxy - iminy);
} finally {
LEAVING("bpkzwylrchx5wta1qhytfgbtr","aabbaabb");
}
}




//3 2g71cq6f8w5jbmbnn2x9y5qfq
// static int lblenclosing(object_t * objp, object_t * objp1) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="lblenclosing", key="2g71cq6f8w5jbmbnn2x9y5qfq", definition="static int lblenclosing(object_t * objp, object_t * objp1)")
public static boolean lblenclosing(ST_object_t objp, ST_object_t objp1) {
ENTERING("2g71cq6f8w5jbmbnn2x9y5qfq","lblenclosing");
try {
	CArray<ST_xlabel_t> xlp = objp.lbl;
//   assert(objp1->sz.x == 0 && objp1->sz.y == 0);
   if((xlp) == null) return false;
      return objp1.pos.x > xlp.get__(0).pos.x &&
    		  objp1.pos.x < (xlp.get__(0).pos.x + xlp.get__(0).sz.y) &&
    		  objp1.pos.y > xlp.get__(0).pos.y &&
    		  objp1.pos.y < (xlp.get__(0).pos.y + xlp.get__(0).sz.y);
//   UNSUPPORTED("exdts7f2bpam5122kabq2b86c"); //   return objp1->pos.x > xlp->pos.x &&
//   UNSUPPORTED("99uxf5dqw5nzdymlzfj764uol"); // 	 objp1->pos.x < (xlp->pos.x + xlp->sz.x) &&
//   UNSUPPORTED("epx7s3oiw75fuioasz208w1k1"); // 	 objp1->pos.y > xlp->pos.y &&
//   UNSUPPORTED("29g3tye8vebllnv9b91xyntzi"); // 	 objp1->pos.y < (xlp->pos.y + xlp->sz.y);
//   UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
} finally {
LEAVING("2g71cq6f8w5jbmbnn2x9y5qfq","lblenclosing");
}
}





//3 dq1wkb4oxshdggv6cwtgas6m
// static void objp2rect(object_t * op, Rect_t * r) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="objp2rect", key="dq1wkb4oxshdggv6cwtgas6m", definition="static void objp2rect(object_t * op, Rect_t * r)")
public static void objp2rect(ST_object_t op, ST_Rect_t r) {
ENTERING("dq1wkb4oxshdggv6cwtgas6m","objp2rect");
try {
	r.boundary[0]=((int)op.pos.x);
	r.boundary[1]=((int)op.pos.y);
	r.boundary[2]=((int)(op.pos.x+op.sz.x));
	r.boundary[3]=((int)(op.pos.y+op.sz.y));
} finally {
LEAVING("dq1wkb4oxshdggv6cwtgas6m","objp2rect");
}
}




//3 71b5ttp3xs7lo9fqgb7ypyqgx
// static void objplp2rect(object_t * objp, Rect_t * r) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="objplp2rect", key="71b5ttp3xs7lo9fqgb7ypyqgx", definition="static void objplp2rect(object_t * objp, Rect_t * r)")
public static void objplp2rect(ST_object_t objp, ST_Rect_t r) {
ENTERING("71b5ttp3xs7lo9fqgb7ypyqgx","objplp2rect");
try {
	CArray<ST_xlabel_t> lp = objp.lbl;
    r.boundary[0]=((int)lp.get__(0).pos.x);
    r.boundary[1]=((int)lp.get__(0).pos.y);
    r.boundary[2]=((int)(lp.get__(0).pos.x+lp.get__(0).sz.x));
    r.boundary[3]=((int)(lp.get__(0).pos.y+lp.get__(0).sz.y));
} finally {
LEAVING("71b5ttp3xs7lo9fqgb7ypyqgx","objplp2rect");
}
}




//3 ksqjbiie0e6vvaeawdxriie5
// static Rect_t objplpmks(XLabels_t * xlp, object_t * objp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="objplpmks", key="ksqjbiie0e6vvaeawdxriie5", definition="static Rect_t objplpmks(XLabels_t * xlp, object_t * objp)")
public static ST_Rect_t objplpmks(ST_XLabels_t xlp, ST_object_t objp) {
ENTERING("ksqjbiie0e6vvaeawdxriie5","objplpmks");
try {
     final ST_Rect_t rect = new ST_Rect_t();
     final ST_pointf p = new ST_pointf();
     p.x = 0;
     p.y = 0;
     if (objp.lbl!=null)
    	 p.___(objp.lbl.get__(0).sz);
     rect.boundary[0]=((int) Math.floor(objp.pos.x - p.x));
     rect.boundary[1]=((int) Math.floor(objp.pos.y - p.y));
     rect.boundary[2]=((int) Math.ceil(objp.pos.x + objp.sz.x + p.x));
     //     assert(rect.boundary[2] < INT_MAX);
     rect.boundary[3]=((int) Math.ceil(objp.pos.y + objp.sz.y + p.y));
     //     assert(rect.boundary[3] < INT_MAX);
     return rect;
} finally {
LEAVING("ksqjbiie0e6vvaeawdxriie5","objplpmks");
}
}




//3 calnhom3s9dqvvi6crrz3h2wp
// static int getintrsxi(XLabels_t * xlp, object_t * op, object_t * cp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="getintrsxi", key="calnhom3s9dqvvi6crrz3h2wp", definition="static int getintrsxi(XLabels_t * xlp, object_t * op, object_t * cp)")
public static int getintrsxi(ST_XLabels_t xlp, ST_object_t  op, ST_object_t cp) {
ENTERING("calnhom3s9dqvvi6crrz3h2wp","getintrsxi");
try {
     int i = -1;
     CArray<ST_xlabel_t> lp = op.lbl, clp = cp.lbl;
     assert(lp != clp);
     if (lp.get__(0).set == 0 || clp.get__(0).set == 0)
 	return i;
UNSUPPORTED("bofpvwtmumoe1ckgnlgbwg8bt"); //     if ((op->pos.x == 0.0 && op->pos.y == 0.0) ||
UNSUPPORTED("f039op8rn0jopi9r8kora4cwz"); // 	(cp->pos.x == 0.0 && cp->pos.y == 0.0))
UNSUPPORTED("11drvggon8u61uz4iw75insly"); // 	return i;
UNSUPPORTED("5t4dji6xy0hdyx5wguao13hy5"); //     if (cp->pos.y < op->pos.y)
UNSUPPORTED("kx0a7gocqji2ms0neepjemp0"); // 	if (cp->pos.x < op->pos.x)
UNSUPPORTED("al11nyatzn20y82ligyxw6doz"); // 	    i = 0;
UNSUPPORTED("6u28hrhgdf9tpy6v85nmoma8q"); // 	else if (cp->pos.x > op->pos.x)
UNSUPPORTED("3nbwdw3wfkpmnrf8loezvflkx"); // 	    i = 2;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("akdfo982ws6tlct2c4wlm7hdb"); // 	    i = 1;
UNSUPPORTED("6h2iz13c1pog9oqz0a4f1sqth"); //     else if (cp->pos.y > op->pos.y)
UNSUPPORTED("kx0a7gocqji2ms0neepjemp0"); // 	if (cp->pos.x < op->pos.x)
UNSUPPORTED("2rdl000508m132a9u1h076lba"); // 	    i = 6;
UNSUPPORTED("6u28hrhgdf9tpy6v85nmoma8q"); // 	else if (cp->pos.x > op->pos.x)
UNSUPPORTED("7p6vu0p80gdkcsj4l5hz59dqb"); // 	    i = 8;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("69apu46xpi0gdi4dt1tir6dbh"); // 	    i = 7;
UNSUPPORTED("73lwftxejfwwrm0v22inmutya"); //     else if (cp->pos.x < op->pos.x)
UNSUPPORTED("9t0t38axcdz9uuxxaxd4e5oi2"); // 	i = 3;
UNSUPPORTED("an605gtk4m1ql9srrrj5k6dah"); //     else if (cp->pos.x > op->pos.x)
UNSUPPORTED("cmp9l0lizmu0ars1jf1oxdms"); // 	i = 5;
UNSUPPORTED("ahwo5hst5k1gyq20ve63ahe81"); //     return i;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("calnhom3s9dqvvi6crrz3h2wp","getintrsxi");
}
}




//3 52awwxu810dg9a2pjq45aomam
// static double recordointrsx(XLabels_t * xlp, object_t * op, object_t * cp, Rect_t * rp, 	      double a, object_t * intrsx[9]) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="recordointrsx", key="52awwxu810dg9a2pjq45aomam", definition="static double recordointrsx(XLabels_t * xlp, object_t * op, object_t * cp, Rect_t * rp, 	      double a, object_t * intrsx[9])")
public static double recordointrsx(ST_XLabels_t xlp, ST_object_t op, ST_object_t cp, ST_Rect_t rp, double a, ST_object_t[] intrsx) {
ENTERING("52awwxu810dg9a2pjq45aomam","recordointrsx");
try {
     int i = getintrsxi(xlp, op, cp);
     if (i < 0)
 	i = 5;
     if (intrsx[i] != null) {
 	double sa, maxa = 0.0;
 	final ST_Rect_t srect = new ST_Rect_t();
 	/* keep maximally overlapping object */
 	objp2rect(intrsx[i], srect);
 	sa = aabbaabb(rp, srect);
 	if (sa > a)
 	    maxa = sa;
 	/*keep maximally overlapping label */
 	if (intrsx[i].lbl!=null) {
 	    objplp2rect(intrsx[i], srect);
 	    sa = aabbaabb(rp, srect);
 	    if (sa > a)
 		maxa = sa > maxa ? sa : maxa;
 	}
 	if (maxa > 0.0)
 	    return maxa;
 	/*replace overlapping label/object pair */
 	intrsx[i] = cp;
 	return a;
     }
     intrsx[i] = cp;
     return a;
} finally {
LEAVING("52awwxu810dg9a2pjq45aomam","recordointrsx");
}
}




//3 2umrncgkunxoyeuc8i9hd5pwa
// static double recordlintrsx(XLabels_t * xlp, object_t * op, object_t * cp, Rect_t * rp, 	      double a, object_t * intrsx[9]) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="recordlintrsx", key="2umrncgkunxoyeuc8i9hd5pwa", definition="static double recordlintrsx(XLabels_t * xlp, object_t * op, object_t * cp, Rect_t * rp, 	      double a, object_t * intrsx[9])")
public static double recordlintrsx(ST_XLabels_t xlp, ST_object_t op, ST_object_t cp, ST_Rect_t rp, double a, ST_object_t intrsx[]) {
ENTERING("2umrncgkunxoyeuc8i9hd5pwa","recordlintrsx");
try {
     int i = getintrsxi(xlp, op, cp);
     if (i < 0)
 	i = 5;
     if (intrsx[i] != null) {
 	double sa, maxa = 0.0;
 	final ST_Rect_t srect = new ST_Rect_t();
 	/* keep maximally overlapping object */
 	objp2rect(intrsx[i], srect);
 	sa = aabbaabb(rp, srect);
 	if (sa > a)
 	    maxa = sa;
 	/*keep maximally overlapping label */
 	if (intrsx[i].lbl!=null) {
 	    objplp2rect(intrsx[i], srect);
 	    sa = aabbaabb(rp, srect);
 	    if (sa > a)
 		maxa = sa > maxa ? sa : maxa;
 	}
 	if (maxa > 0.0)
 	    return maxa;
 	/*replace overlapping label/object pair */
 	intrsx[i] = cp;
 	return a;
     }
  	intrsx[i] = cp;
  	return a;
} finally {
LEAVING("2umrncgkunxoyeuc8i9hd5pwa","recordlintrsx");
}
}




/* find the objects and labels intersecting lp */
//3 2td62i5hus8obwt8j1lo3ddj9
// static BestPos_t xlintersections(XLabels_t * xlp, object_t * objp, object_t * intrsx[9]) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlintersections", key="2td62i5hus8obwt8j1lo3ddj9", definition="static BestPos_t xlintersections(XLabels_t * xlp, object_t * objp, object_t * intrsx[9])")
public static ST_BestPos_t xlintersections(ST_XLabels_t xlp, ST_object_t objp, ST_object_t intrsx[]) {
ENTERING("2td62i5hus8obwt8j1lo3ddj9","xlintersections");
try {
	return (ST_BestPos_t) xlintersections_(xlp, objp, intrsx).copy();
} finally {
LEAVING("2td62i5hus8obwt8j1lo3ddj9","xlintersections");
}
}

private static ST_BestPos_t xlintersections_(ST_XLabels_t xlp, ST_object_t objp, ST_object_t intrsx[]) {
	int i;
	ST_LeafList_t ilp, llp;
	final ST_Rect_t rect = new ST_Rect_t(), srect = new ST_Rect_t();
	final ST_BestPos_t bp = new ST_BestPos_t();
		
//     assert(objp->lbl);
		
    bp.n = 0;
    bp.area = 0.0;
    bp.pos.___(objp.lbl.get__(0).pos);

     for(i=0; i<xlp.n_objs; i++) {
    	       if (objp == xlp.objs.get__(i)) continue;
       if(xlp.objs.get__(i).sz.x > 0 && xlp.objs.get__(i).sz.y > 0) continue;
       if(lblenclosing(objp, xlp.objs.get__(i))) {
    	 	  bp.n = bp.n+1;
       }
     }
     
     objplp2rect(objp, rect);
     
     llp = RTreeSearch(xlp.spdx, xlp.spdx.root, rect);
     if ((llp) == null)
 	return bp;
     
     for (ilp = llp; ilp!=null; ilp = ilp.next) {
 	double a, ra;
 	// WARNING FOR TRANSLATION
 	// In the C code, "data" was used. However ST_Branch_t is very close to ST_Leaf_t
 	// So in Java version, ST_Leaf_t has been removed and ST_Branch_t is used instead
 	ST_object_t cp = (ST_object_t) ilp.leaf.child;
 	
 	if (cp == objp)
 	    continue;
 	
   /*label-object intersect */
 	objp2rect(cp, srect);
 	a = aabbaabb(rect, srect);
 	if (a > 0.0) {
 	  ra = recordointrsx(xlp, objp, cp, rect, a, intrsx);
	  bp.n++;
	  bp.area += ra;
 	}
 	/*label-label intersect */
 	if ((cp.lbl) == null || cp.lbl.get__(0).set == 0)
 	    continue;
 	objplp2rect(cp, srect);
 	a = aabbaabb(rect, srect);
 	if (a > 0.0) {
 	  ra = recordlintrsx(xlp, objp, cp, rect, a, intrsx);
	  bp.n++;
	  bp.area += ra;
 	}
     }
     RTreeLeafListFree(llp);
     return bp;
}




//3 8rxvucqsqnqej6h8p1osfnk4b
// static BestPos_t xladjust(XLabels_t * xlp, object_t * objp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xladjust", key="8rxvucqsqnqej6h8p1osfnk4b", definition="static BestPos_t xladjust(XLabels_t * xlp, object_t * objp)")
public static ST_BestPos_t xladjust(ST_XLabels_t xlp, ST_object_t objp) {
ENTERING("8rxvucqsqnqej6h8p1osfnk4b","xladjust");
try {
	return (ST_BestPos_t) xladjust_(xlp, objp).copy();	
} finally {
LEAVING("8rxvucqsqnqej6h8p1osfnk4b","xladjust");
}
}
private static ST_BestPos_t xladjust_(ST_XLabels_t xlp, ST_object_t objp) {
	CArray<ST_xlabel_t> lp = objp.lbl; // ST_xlabel_t
    double xincr = ((2 * lp.get__(0).sz.x + objp.sz.x)) / 8;
    double yincr = ((2 * lp.get__(0).sz.y + objp.sz.y)) / 2;
    ST_object_t intrsx[] = new ST_object_t[9];
    final ST_BestPos_t bp = new ST_BestPos_t();
    final ST_BestPos_t nbp = new ST_BestPos_t();
    //     assert(objp->lbl);
    //     memset(intrsx, 0, sizeof(intrsx));
     /*x left */
    lp.get__(0).pos.x = objp.pos.x - lp.get__(0).sz.x;
     /*top */
    lp.get__(0).pos.y = objp.pos.y + objp.sz.y;
    bp.___(xlintersections(xlp, objp, intrsx));
     if (bp.n == 0)
 	return bp;
     /*mid */
     lp.get__(0).pos.y = objp.pos.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
     if (nbp.n == 0)
 	return nbp;
     if (nbp.area < bp.area)
 	bp.___(nbp);
     /*bottom */
     lp.get__(0).pos.y = objp.pos.y - lp.get__(0).sz.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
     if (nbp.n == 0)
 	return nbp;
     if (nbp.area < bp.area)
 	bp.___(nbp);
     /*x mid */
     lp.get__(0).pos.x = objp.pos.x;
     /*top */
     lp.get__(0).pos.y = objp.pos.y + objp.sz.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
     if (nbp.n == 0)
       return nbp;
     if (nbp.area < bp.area)
       bp.___(nbp);
     /*bottom */
	lp.get__(0).pos.y = objp.pos.y - lp.get__(0).sz.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
     if (nbp.n == 0)
       return nbp;
     if (nbp.area < bp.area)
       bp.___(nbp);
     /*x right */
 	lp.get__(0).pos.x = objp.pos.x + objp.sz.x;
     /*top */
 	lp.get__(0).pos.y = objp.pos.y + objp.sz.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
if (nbp.n == 0)
return nbp;
if (nbp.area < bp.area)
    bp.___(nbp);
     /*mid */
	lp.get__(0).pos.y = objp.pos.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
if (nbp.n == 0)
return nbp;
if (nbp.area < bp.area)
    bp.___(nbp);
     /*bottom */
	lp.get__(0).pos.y = objp.pos.y - lp.get__(0).sz.y;
     nbp.___(xlintersections(xlp, objp, intrsx));
if (nbp.n == 0)
return nbp;
if (nbp.area < bp.area)
    bp.___(nbp);
     /*sliding from top left */
     if (intrsx[6]!=null || intrsx[7]!=null || intrsx[8]!=null || intrsx[3]!=null || intrsx[0]!=null) {	/* have to move */
UNSUPPORTED("c5acs4cl77hgu5j6nmhk090uz"); // 	if (!intrsx[7] && !intrsx[8]) {	/* some room right? */
UNSUPPORTED("ez8sbh237xdqdjeewev2ys74y"); // 	    /* slide along upper edge */
UNSUPPORTED("ajgw2jggmwmofm77lgbx5eo2n"); // 	    for (lp->pos.x = objp->pos.x - lp->sz.x,
UNSUPPORTED("nwfhss2g67z5exjkorj0brd"); // 		 lp->pos.y = objp->pos.y + objp->sz.y;
UNSUPPORTED("eav75dwa3f3ieedr9mnov3a6u"); // 		 lp->pos.x <= (objp->pos.x + objp->sz.x);
UNSUPPORTED("38u334kkfej5n3r31uv1afw9q"); // 		 lp->pos.x += xincr) {
UNSUPPORTED("8pbum2o0fs86ceaiuxv4efbb6"); // 		nbp = xlintersections(xlp, objp, intrsx);
UNSUPPORTED("5xx7o143ftoj0rhyhg1hqgioa"); // 		if (nbp.n == 0)
UNSUPPORTED("eqanxe9w90oki7yqvwyzpfcw1"); // 		    return nbp;
UNSUPPORTED("8yd6g3a3f0g09gmu5f67vah0x"); // 		if (nbp.area < bp.area)
UNSUPPORTED("d6ac36j6lh6qspxwnn3vi0uc3"); // 		    bp = nbp;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4w0qgfnt6kpuhb381pmra3ln4"); // 	if (!intrsx[3] && !intrsx[0]) {	/* some room down? */
UNSUPPORTED("6st33ex0udr4mk4sa9vd119uz"); // 	    /* slide down left edge */
UNSUPPORTED("ajgw2jggmwmofm77lgbx5eo2n"); // 	    for (lp->pos.x = objp->pos.x - lp->sz.x,
UNSUPPORTED("nwfhss2g67z5exjkorj0brd"); // 		 lp->pos.y = objp->pos.y + objp->sz.y;
UNSUPPORTED("eolwmrajeldp9gf9ug6ue0gx2"); // 		 lp->pos.y >= (objp->pos.y - lp->sz.y);
UNSUPPORTED("bz3822dbe0xnwj4nm7pf39kae"); // 		 lp->pos.y -= yincr) {
UNSUPPORTED("8pbum2o0fs86ceaiuxv4efbb6"); // 		nbp = xlintersections(xlp, objp, intrsx);
UNSUPPORTED("5xx7o143ftoj0rhyhg1hqgioa"); // 		if (nbp.n == 0)
UNSUPPORTED("eqanxe9w90oki7yqvwyzpfcw1"); // 		    return nbp;
UNSUPPORTED("8yd6g3a3f0g09gmu5f67vah0x"); // 		if (nbp.area < bp.area)
UNSUPPORTED("d6ac36j6lh6qspxwnn3vi0uc3"); // 		    bp = nbp;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
     }
     /*sliding from bottom right */
 	lp.get__(0).pos.x = objp.pos.x + objp.sz.x;
	lp.get__(0).pos.y = objp.pos.y - lp.get__(0).sz.y;
     if (intrsx[2]!=null || intrsx[1]!=null || intrsx[0]!=null || intrsx[5]!=null || intrsx[8]!=null) {	/* have to move */
 	if ((intrsx[1]) == null && (intrsx[0]) == null) {	/* some room left? */
 	    /* slide along lower edge */
 	    for (lp.get__(0).pos.x = (objp.pos.x + objp.sz.x),
 		 lp.get__(0).pos.y = objp.pos.y - lp.get__(0).sz.y;
 		 lp.get__(0).pos.x >= (objp.pos.x - lp.get__(0).sz.x);
 		 lp.get__(0).pos.x = (lp.get__(0).pos.x - xincr)) {
 		nbp.___(xlintersections(xlp, objp, intrsx));
 		if (nbp.n == 0)
 			return nbp;
 			if (nbp.area < bp.area)
 			    bp.___(nbp);
 	    }
 	}
 	if ((intrsx[5]) == null && (intrsx[8]) == null) {	/* some room up? */
UNSUPPORTED("4bcpk3ixfwrlr0yzrjv2efigj"); // 	    /* slide up right edge */
UNSUPPORTED("7natp0873pfsvymhhhvki1sev"); // 	    for (lp->pos.x = objp->pos.x + objp->sz.x,
UNSUPPORTED("56ycmo0ljb7e86mnezdx3regh"); // 		 lp->pos.y = objp->pos.y - lp->sz.y;
UNSUPPORTED("9cepfi69gtzy7muk9d27es8oc"); // 		 lp->pos.y <= (objp->pos.y + objp->sz.y);
UNSUPPORTED("az41g895dhl39ckrigxl1c5jl"); // 		 lp->pos.y += yincr) {
UNSUPPORTED("8pbum2o0fs86ceaiuxv4efbb6"); // 		nbp = xlintersections(xlp, objp, intrsx);
UNSUPPORTED("5xx7o143ftoj0rhyhg1hqgioa"); // 		if (nbp.n == 0)
UNSUPPORTED("eqanxe9w90oki7yqvwyzpfcw1"); // 		    return nbp;
UNSUPPORTED("8yd6g3a3f0g09gmu5f67vah0x"); // 		if (nbp.area < bp.area)
UNSUPPORTED("d6ac36j6lh6qspxwnn3vi0uc3"); // 		    bp = nbp;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
 	}
     }
     return bp;
}




//3 e29g2hwlogx0zchsnvi464c7t
// static int xlhdxload(XLabels_t * xlp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlhdxload", key="e29g2hwlogx0zchsnvi464c7t", definition="static int xlhdxload(XLabels_t * xlp)")
public static int xlhdxload(Globals zz, ST_XLabels_t xlp) {
ENTERING("e29g2hwlogx0zchsnvi464c7t","xlhdxload");
try {
     int i;
     int order = xlhorder(xlp);
      for (i = 0; i < xlp.n_objs; i++) {
    	  ST_HDict_t hp;
    	  final ST_point pi = new ST_point();
    	  hp = new ST_HDict_t();
    	  hp.d.child = xlp.objs.get__(i);
    	  hp.d.rect.___(objplpmks(xlp, xlp.objs.get__(i)));
          /* center of the labeling area */
    	  pi.x = hp.d.rect.boundary[0] +
    	   (hp.d.rect.boundary[2] - hp.d.rect.boundary[0]) / 2;
		  pi.y = hp.d.rect.boundary[1] +
		   (hp.d.rect.boundary[3] - hp.d.rect.boundary[1]) / 2;
		  
		  hp.key = hd_hil_s_from_xy(pi, order);
		  if ((xlp.hdx.searchf.exe(zz, xlp.hdx, hp, 0000001)) == null)
			  
 	    return -1;
     }
     return 0;
} finally {
LEAVING("e29g2hwlogx0zchsnvi464c7t","xlhdxload");
}
}




//3 26qpvnyd6tmdut8i2wo4itza3
// static void xlhdxunload(XLabels_t * xlp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlhdxunload", key="26qpvnyd6tmdut8i2wo4itza3", definition="static void xlhdxunload(XLabels_t * xlp)")
public static void xlhdxunload(ST_XLabels_t xlp) {
ENTERING("26qpvnyd6tmdut8i2wo4itza3","xlhdxunload");
try {
//UNSUPPORTED("4bsnj74f63qe288s6be1xmrzw"); // static void xlhdxunload(XLabels_t * xlp)
//UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
//UNSUPPORTED("9sqtid8yyo2ws8avf33utdebf"); //   int size=dtsize(xlp->hdx), freed=0;
//UNSUPPORTED("dm7bmt2fwlkdskmcebdm1ctqj"); //   while(dtsize(xlp->hdx) ) {
//UNSUPPORTED("a3jz6ykhoarsdfviidq7beb9z"); //     void*vp=(((Dt_t*)(xlp->hdx))->data->here ? (((Dt_t*)((xlp->hdx)))->disc->link < 0 ? ((Dthold_t*)((((Dt_t*)(xlp->hdx))->data->here)))->obj : (void*)((char*)((((Dt_t*)(xlp->hdx))->data->here)) - ((Dt_t*)((xlp->hdx)))->disc->link) ):(void*)(0));
//UNSUPPORTED("2covsywkzvpkysnjf34nmr1uw"); //     assert(vp);
//UNSUPPORTED("a9jmr8hmnwcky7f1e1qorwnjv"); //     if(vp) {
//UNSUPPORTED("d2vfgb4tj6x8x7cq0kv0qgk23"); //       (*(((Dt_t*)(xlp->hdx))->searchf))((xlp->hdx),(void*)(vp),0010000);
//UNSUPPORTED("7f4abg2jsf465jjlnp95d9sk6"); //       free(vp);
//UNSUPPORTED("5hklxkrkvjgi37kkm6v9xajwc"); //       freed++;
//UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
//UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
//UNSUPPORTED("bifp41eoqo8l51crrab087z21"); //   assert(size==freed);
//UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
} finally {
LEAVING("26qpvnyd6tmdut8i2wo4itza3","xlhdxunload");
}
}




//3 3wrxwwd3y5ts0ekr32o8vhuvv
// static int xlspdxload(XLabels_t * xlp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlspdxload", key="3wrxwwd3y5ts0ekr32o8vhuvv", definition="static int xlspdxload(XLabels_t * xlp)")
public static int xlspdxload(Globals zz, ST_XLabels_t xlp) {
ENTERING("3wrxwwd3y5ts0ekr32o8vhuvv","xlspdxload");
try {
     ST_HDict_t op=null;
     for (op = (ST_HDict_t) xlp.hdx.searchf.exe(zz, xlp.hdx, null, 0000200); op!=null;
    		 op = (ST_HDict_t) xlp.hdx.searchf.exe(zz, xlp.hdx, op, 0000010)) {
   	/*          tree       rectangle    data        node             lvl */
//    	 	RTreeInsert(xlp->spdx, &op->d.rect, op->d.data, &xlp->spdx->root, 0);
    	 // WARNING ARRAY
     	final ST_Node_t___[] tmp = new ST_Node_t___[] {(ST_Node_t___) xlp.spdx.root};
		RTreeInsert((ST_RTree) xlp.spdx, (ST_Rect_t)op.d.rect,
     			op.d.child,
     			tmp, 0);
		xlp.spdx.root = tmp[0];
     }
     return 0;
} finally {
LEAVING("3wrxwwd3y5ts0ekr32o8vhuvv","xlspdxload");
}
}




//3 6d3fqrllm55toeo3wscwvv4ty
// static int xlinitialize(XLabels_t * xlp) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="xlinitialize", key="6d3fqrllm55toeo3wscwvv4ty", definition="static int xlinitialize(XLabels_t * xlp)")
public static int xlinitialize(Globals zz, ST_XLabels_t xlp) {
ENTERING("6d3fqrllm55toeo3wscwvv4ty","xlinitialize");
try {
     int r=0;
     if ((r = xlhdxload(zz, xlp)) < 0)
 	return r;
     if ((r = xlspdxload(zz, xlp)) < 0)
 	return r;
     xlhdxunload(xlp);
     return dtclose(zz, (ST_dt_s) xlp.hdx);
} finally {
LEAVING("6d3fqrllm55toeo3wscwvv4ty","xlinitialize");
}
}




//3 brqgbskh3z4ah8infjompibvu
// int placeLabels(object_t * objs, int n_objs, 	    xlabel_t * lbls, int n_lbls, label_params_t * params) 
@Unused
@Original(version="2.38.0", path="lib/label/xlabels.c", name="placeLabels", key="brqgbskh3z4ah8infjompibvu", definition="int placeLabels(object_t * objs, int n_objs, 	    xlabel_t * lbls, int n_lbls, label_params_t * params)")
public static int placeLabels(Globals zz, CArray<ST_object_t> objs, int n_objs, CArray<ST_xlabel_t> lbls, int n_lbls, ST_label_params_t params) {
ENTERING("brqgbskh3z4ah8infjompibvu","placeLabels");
try {
int r, i;
final ST_BestPos_t bp = new ST_BestPos_t();
ST_XLabels_t xlp = xlnew(zz, objs, n_objs, lbls, n_lbls, params);
     if ((r = xlinitialize(zz, xlp)) < 0)
 	return r;
     /* Place xlabel_t* lp near lp->obj so that the rectangle whose lower-left
      * corner is lp->pos, and size is lp->sz does not intersect any object
      * in objs (by convention, an object consisting of a single point
      * intersects nothing) nor any other label, if possible. On input,
      * lp->set is 0.
      *
      * On output, any label with a position should have this stored in
      * lp->pos and have lp->set non-zero.
      *
      * If params->force is true, all labels must be positioned, even if
      * overlaps are necessary.
      *
      * Return 0 if all labels could be placed without overlap;
      * non-zero otherwise.
      */
     r = 0;
     for (i = 0; i < n_objs; i++) {
 	if (objs.get__(i).lbl == null)
 	    continue;
 	bp.___(xladjust(xlp, objs.get__(i)));
 	if (bp.n == 0) {
 	    objs.get__(i).lbl.get__(0).set = 1;
 	} else if(bp.area == 0) {
 	    objs.get__(i).lbl.get__(0).pos.x = bp.pos.x;
 	    objs.get__(i).lbl.get__(0).pos.y = bp.pos.y;
 	    objs.get__(i).lbl.get__(0).set = 1;
 	} else if (params.force) {
 	    objs.get__(i).lbl.get__(0).pos.x = bp.pos.x;
 	    objs.get__(i).lbl.get__(0).pos.y = bp.pos.y;
 	    objs.get__(i).lbl.get__(0).set = 1;
 	} else {
 	    r = 1;
 	}
     }
     xlfree(xlp);
     return r;
} finally {
LEAVING("brqgbskh3z4ah8infjompibvu","placeLabels");
}
}


}
