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
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.arrows__c.arrowEndClip;
import static gen.lib.common.arrows__c.arrowStartClip;
import static gen.lib.common.arrows__c.arrow_flags;
import static gen.lib.common.emit__c.update_bb_bz;
import static gen.lib.common.shapes__c.resolvePort;
import static gen.lib.common.utils__c.Bezier;
import static gen.lib.common.utils__c.dotneato_closest;
import static h.ST_pointf.add_pointf;
import static h.ST_pointf.pointfof;
import static smetana.core.JUtils.EQ;
import static smetana.core.Macro.ABS;
import static smetana.core.Macro.APPROXEQPT;
import static smetana.core.Macro.BOTTOM;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.FLATEDGE;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.LEFT;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MILLIPOINT;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_shape;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.NOTI;
import static smetana.core.Macro.REGULAREDGE;
import static smetana.core.Macro.RIGHT;
import static smetana.core.Macro.SELFEDGE;
import static smetana.core.Macro.TOP;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Comment;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Todo;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_inside_t;
import h.ST_path;
import h.ST_pathend_t;
import h.ST_pointf;
import h.ST_splineInfo;
import h.ST_splines;
import h.ST_textlabel_t;
import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.CFunction;
import smetana.core.__ptr__;

public class splines__c {



//3 6izm0fbkejw7odmiw4zaw1ycp
// static void arrow_clip(edge_t * fe, node_t * hn, 	   pointf * ps, int *startp, int *endp, 	   bezier * spl, splineInfo * info) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="arrow_clip", key="6izm0fbkejw7odmiw4zaw1ycp", definition="static void arrow_clip(edge_t * fe, node_t * hn, 	   pointf * ps, int *startp, int *endp, 	   bezier * spl, splineInfo * info)")
public static void arrow_clip(ST_Agedge_s fe, ST_Agnode_s hn, CArray<ST_pointf> ps, int startp[], int endp[], ST_bezier spl, ST_splineInfo info) {
ENTERING("6izm0fbkejw7odmiw4zaw1ycp","arrow_clip");
try {
    ST_Agedge_s e;
    int i;
    boolean j;
    int sflag[] = new int[]{0};
    int eflag[] = new int[]{0};
    for (e = fe; ED_to_orig(e)!=null; e = ED_to_orig(e));
    if (info.ignoreSwap)
	j = false;
    else
	j = (Boolean) info.swapEnds.exe(e);
    arrow_flags(e, sflag, eflag);
    if ((Boolean) info.splineMerge.exe(hn))
	eflag[0] = 0;
    if ((Boolean) info.splineMerge.exe(agtail(fe)))
	sflag[0] = 0;
    /* swap the two ends */
    if (j) {
	i = sflag[0];
	sflag[0] = eflag[0];
	eflag[0] = i;
    }
    if (info.isOrtho) {
UNSUPPORTED("7a3lmojyfh13d6shkviuogx2c"); // 	if (eflag || sflag)
UNSUPPORTED("dzbrwr2ulubtjkbd8j2o4yyov"); // 	    arrowOrthoClip(e, ps, *startp, *endp, spl, sflag, eflag);
    }
    else {
	if (sflag[0]!=0)
		startp[0] =
		arrowStartClip(e, ps, startp[0], endp[0], spl, sflag[0]);
	if (eflag[0]!=0)
	    endp[0] =
		arrowEndClip(e, ps, startp[0], endp[0], spl, eflag[0]);
    }
} finally {
LEAVING("6izm0fbkejw7odmiw4zaw1ycp","arrow_clip");
}
}




/* bezier_clip
 * Clip bezier to shape using binary search.
 * The details of the shape are passed in the inside_context;
 * The function providing the inside test is passed as a parameter.
 * left_inside specifies that sp[0] is inside the node, 
 * else sp[3] is taken as inside.
 * The points p are in node coordinates.
 */
//3 q4t1ywnk3wm1vyh5seoj7xye
// void bezier_clip(inside_t * inside_context, 		 boolean(*inside) (inside_t * inside_context, pointf p), 		 pointf * sp, boolean left_inside) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="bezier_clip", key="q4t1ywnk3wm1vyh5seoj7xye", definition="void bezier_clip(inside_t * inside_context, 		 boolean(*inside) (inside_t * inside_context, pointf p), 		 pointf * sp, boolean left_inside)")
public static void bezier_clip(__ptr__ inside_context, __ptr__ inside, CArray<ST_pointf> sp, boolean left_inside) {
ENTERING("q4t1ywnk3wm1vyh5seoj7xye","bezier_clip");
try {
    final CArray<ST_pointf> seg = CArray.<ST_pointf>ALLOC__(4, ST_pointf.class);
    final CArray<ST_pointf> best = CArray.<ST_pointf>ALLOC__(4, ST_pointf.class);
    final ST_pointf pt = new ST_pointf(), opt = new ST_pointf();
    CArray<ST_pointf> left, right;
    double t;
    final double low[] = new double[] {0}, high[] = new double[] {0};
    double idir[] = null, odir[] = null;

    boolean found;
    int i;
    
    if (left_inside) {
	left = null;
	right = seg;
	pt.___(sp.get__(0));
	idir = low;
	odir = high;
    } else {
	left = seg;
	right = null;
	pt.___(sp.get__(3));
	idir = high;
	odir = low;
    }
    found = false;
    low[0] = 0.0;
    high[0] = 1.0;
    do {
	opt.___(pt);
	t = (high[0] + low[0]) / 2.0;
	pt.___(Bezier(sp, 3, t, left, right));
	if ((Boolean) ((CFunction)inside).exe(inside_context, pt)) {
	    idir[0] = t;
	} else {
	    for (i = 0; i < 4; i++)
		best.get__(i).___(seg.get__(i));
	    found = true;
	    odir[0] = t;
	}
    } while (ABS(opt.x - pt.x) > .5 || ABS(opt.y - pt.y) > .5);
    if (found)
	for (i = 0; i < 4; i++)
	    sp.get__(i).___(best.get__(i));
    else
	for (i = 0; i < 4; i++)
	    sp.get__(i).___(seg.get__(i));
} finally {
LEAVING("q4t1ywnk3wm1vyh5seoj7xye","bezier_clip");
}
}




/* shape_clip0:
 * Clip Bezier to node shape using binary search.
 * left_inside specifies that curve[0] is inside the node, else
 * curve[3] is taken as inside.
 * Assumes ND_shape(n) and ND_shape(n)->fns->insidefn are non-NULL.
 * See note on shape_clip.
 */
//3 1fjkj1ydhtlf13pqj5r041orq
// static void shape_clip0(inside_t * inside_context, node_t * n, pointf curve[4], 	    boolean left_inside) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="shape_clip0", key="1fjkj1ydhtlf13pqj5r041orq", definition="static void shape_clip0(inside_t * inside_context, node_t * n, pointf curve[4], 	    boolean left_inside)")
public static void shape_clip0(__ptr__ inside_context, ST_Agnode_s n, CArray<ST_pointf> curve, boolean left_inside) {
ENTERING("1fjkj1ydhtlf13pqj5r041orq","shape_clip0");
try {
    int i;
    double save_real_size;
    final CArray<ST_pointf> c = CArray.<ST_pointf>ALLOC__(4, ST_pointf.class);
    
    save_real_size = ND_rw(n);
    for (i = 0; i < 4; i++) {
	c.get__(i).x = curve.get__(i).x - ND_coord(n).x;
	c.get__(i).y = curve.get__(i).y - ND_coord(n).y;
    }
    
    bezier_clip(inside_context, ND_shape(n).fns.insidefn, c,
		left_inside);
    
    for (i = 0; i < 4; i++) {
	curve.get__(i).x = c.get__(i).x + ND_coord(n).x;
	curve.get__(i).y = c.get__(i).y + ND_coord(n).y;
    }
    ND_rw(n, save_real_size);
} finally {
LEAVING("1fjkj1ydhtlf13pqj5r041orq","shape_clip0");
}
}




/* new_spline:
 * Create and attach a new bezier of size sz to the edge d
 */
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="", key="bdirexg1qdtophlh0ofjvsmj7", definition="bezier *new_spline(edge_t * e, int sz)")
public static ST_bezier new_spline(ST_Agedge_s e, int sz) {
ENTERING("bdirexg1qdtophlh0ofjvsmj7","new_spline");
try {
    ST_bezier rv;
    while (ED_edge_type(e) != 0)
	e = ED_to_orig(e);
    if (ED_spl(e) == null)
	ED_spl(e, new ST_splines());
    ED_spl(e).list = CArray.<ST_bezier> REALLOC__(ED_spl(e).size + 1, ED_spl(e).list, ST_bezier.class);
    rv = ED_spl(e).list.get__(ED_spl(e).size++);
    rv.list = CArray.<ST_pointf>ALLOC__(sz, ST_pointf.class);
    rv.size = sz;
    rv.sflag = 0;
    rv.eflag = 0;
    rv.sp.x = 0;
    rv.sp.y = 0;
    rv.ep.x = 0;
    rv.ep.y = 0;
    return rv;
} finally {
LEAVING("bdirexg1qdtophlh0ofjvsmj7","new_spline");
}
}




/* clip_and_install:
 * Given a raw spline (pn control points in ps), representing
 * a path from edge agtail(fe) ending in node hn, clip the ends to
 * the node boundaries and attach the resulting spline to the
 * edge.
 */
//3 duednxyuvf6xrff752uuv620f
// void clip_and_install(edge_t * fe, node_t * hn, pointf * ps, int pn, 		 splineInfo * info) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="clip_and_install", key="duednxyuvf6xrff752uuv620f", definition="void clip_and_install(edge_t * fe, node_t * hn, pointf * ps, int pn, 		 splineInfo * info)")
public static void clip_and_install(ST_Agedge_s fe, ST_Agnode_s hn, CArray<ST_pointf> ps, int pn, ST_splineInfo info) {
ENTERING("duednxyuvf6xrff752uuv620f","clip_and_install");
try {
    final ST_pointf p2 = new ST_pointf();
    ST_bezier newspl;
    ST_Agnode_s tn;
    int start[] = new int[] {0};
    int end[] = new int[] {0};
    int i;
    boolean clipTail=false, clipHead=false;
    ST_Agraph_s g;
    ST_Agedge_s orig;
    ST_boxf tbox=null, hbox=null;
    final ST_inside_t inside_context = new ST_inside_t();
    
    tn = agtail(fe);
    g = agraphof(tn);
    newspl = new_spline(fe, pn);
    
    for (orig = fe; ED_edge_type(orig) != NORMAL; orig = ED_to_orig(orig));

    /* may be a reversed flat edge */
    if (N(info.ignoreSwap) && (ND_rank(tn) == ND_rank(hn)) && (ND_order(tn) > ND_order(hn))) {
	ST_Agnode_s tmp;
	tmp = hn;
	hn = tn;
	tn = tmp;
    }
    if (EQ(tn, agtail(orig))) {
	clipTail = ED_tail_port(orig).clip;
	clipHead = ED_head_port(orig).clip;
	tbox = ED_tail_port(orig).bp;
	hbox = ED_head_port(orig).bp;
    }
    else { /* fe and orig are reversed */
 	clipTail = ED_head_port(orig).clip;
 	clipHead = ED_tail_port(orig).clip;
 	hbox = ED_tail_port(orig).bp;
 	tbox = ED_head_port(orig).bp;
    }
    
    /* spline may be interior to node */
    if(clipTail && ND_shape(tn)!=null && ND_shape(tn).fns.insidefn!=null) {
	inside_context.s_n = tn;
	inside_context.s_bp = tbox;
	for (start[0] = 0; start[0] < pn - 4; start[0] += 3) {
	    p2.x = ps.get__(start[0] + 3).x - ND_coord(tn).x;
	    p2.y = ps.get__(start[0] + 3).y - ND_coord(tn).y;
	    if (((Boolean)ND_shape(tn).fns.insidefn.exe(inside_context, p2)) == false)
		break;
	}
	shape_clip0(inside_context, tn, ps.plus_(start[0]), true);
    } else
	start[0] = 0;
    if(clipHead && ND_shape(hn)!=null && ND_shape(hn).fns.insidefn!=null) {
	inside_context.s_n = hn;
	inside_context.s_bp = hbox;
	for (end[0] = pn - 4; end[0] > 0; end[0] -= 3) {
	    p2.x = ps.get__(end[0]).x - ND_coord(hn).x;
	    p2.y = ps.get__(end[0]).y - ND_coord(hn).y;
	    if (((Boolean)ND_shape(hn).fns.insidefn.exe(inside_context, p2)) == false)
		break;
	}
	shape_clip0(inside_context, hn, ps.plus_(end[0]), false);
    } else
	end[0] = pn - 4;
    for (; start[0] < pn - 4; start[0] += 3) 
	if (N(APPROXEQPT(ps.get__(start[0]), ps.get__(start[0] + 3), MILLIPOINT)))
	    break;
    for (; end[0] > 0; end[0] -= 3)
	if (N(APPROXEQPT(ps.get__(end[0]), ps.get__(end[0] + 3), MILLIPOINT)))
	    break;
   arrow_clip(fe, hn, ps, start, end, newspl, info);

    for (i = start[0]; i < end[0] + 4; ) {
	final CArray<ST_pointf> cp = CArray.<ST_pointf>ALLOC__(4, ST_pointf.class);
//	if (UnsupportedStarStruct.SPY_ME!=null)
//		System.err.println("TOTO 41 =" + UnsupportedStarStruct.SPY_ME + " " +ps.get__(i).UID);
	newspl.list.get__(i - start[0]).___(ps.get__(i));
//	if (UnsupportedStarStruct.SPY_ME!=null)
//		System.err.println("TOTO 42 =" + UnsupportedStarStruct.SPY_ME + " " +ps.get__(i).UID);
	cp.get__(0).___(ps.get__(i));
	i++;
	if ( i >= end[0] + 4)
	    break;
	newspl.list.get__(i - start[0]).___(ps.get__(i));
	cp.get__(1).___(ps.get__(i));
	i++;
	newspl.list.get__(i - start[0]).___(ps.get__(i));
	cp.get__(2).___(ps.get__(i));
	i++;
	cp.get__(3).___(ps.get__(i));
	update_bb_bz(GD_bb(g), cp);
    }
    newspl.size = end[0] - start[0] + 4;
} finally {
LEAVING("duednxyuvf6xrff752uuv620f","clip_and_install");
}
}




//3 25ndy15kghfrogsv0b0o0xkgv
// static double  conc_slope(node_t* n) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="conc_slope", key="25ndy15kghfrogsv0b0o0xkgv", definition="static double  conc_slope(node_t* n)")
public static double conc_slope(ST_Agnode_s n) {
ENTERING("25ndy15kghfrogsv0b0o0xkgv","conc_slope");
try {
 UNSUPPORTED("e388y3vtrp8f6spgh9q4wx37w"); // static double 
UNSUPPORTED("4yxpid2dxvb387487trn1umlw"); // conc_slope(node_t* n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9669xuley9dxylr00ex9kbzg7"); //     double s_in, s_out, m_in, m_out;
UNSUPPORTED("wfd0ht8utdwwqctf47l4dtrz"); //     int cnt_in, cnt_out;
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("apjf2mf9d7qj0eo9o2x5yli2e"); //     s_in = s_out = 0.0;
UNSUPPORTED("7mc6shwmvz25mz9inwj97lqk6"); //     for (cnt_in = 0; (e = ND_in(n).list[cnt_in]); cnt_in++)
UNSUPPORTED("cb1h5cx7oxhtdkm5l0k6qrx2z"); // 	s_in += ND_coord(agtail(e)).x;
UNSUPPORTED("hjfqfqmtdqdrp9z80ebrpthm"); //     for (cnt_out = 0; (e = ND_out(n).list[cnt_out]); cnt_out++)
UNSUPPORTED("2iidrr9ljv8ap9s2g6gj3q1o3"); // 	s_out += ND_coord(aghead(e)).x;
UNSUPPORTED("2yeio9xc9oorju7qqnhilwujx"); //     p.x = ND_coord(n).x - (s_in / cnt_in);
UNSUPPORTED("87jzl9isj7w9kgyr05inw33s5"); //     p.y = ND_coord(n).y - ND_coord(agtail(ND_in(n).list[0])).y;
UNSUPPORTED("6y2pc9af2xxdqajbpykvca9eg"); //     m_in = atan2(p.y, p.x);
UNSUPPORTED("ruwz5svpk33ucfgs4wx0xolm"); //     p.x = (s_out / cnt_out) - ND_coord(n).x;
UNSUPPORTED("8vif8c37lbo7ww4vwfrcxgpmr"); //     p.y = ND_coord(aghead(ND_out(n).list[0])).y - ND_coord(n).y;
UNSUPPORTED("ez8z3gbteryfhktbqkwmzhhzs"); //     m_out = atan2(p.y, p.x);
UNSUPPORTED("ej1ftaglexa47x955elb88yh2"); //     return ((m_in + m_out) / 2.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("25ndy15kghfrogsv0b0o0xkgv","conc_slope");
}
}




//3 egq4f4tmy1dhyj6jpj92r7xhu
// void add_box(path * P, boxf b) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="add_box", key="egq4f4tmy1dhyj6jpj92r7xhu", definition="void add_box(path * P, boxf b)")
public static void add_box(ST_path P, final ST_boxf b) {
// WARNING!! STRUCT
add_box_w_(P, b.copy());
}
private static void add_box_w_(ST_path P, final ST_boxf b) {
ENTERING("egq4f4tmy1dhyj6jpj92r7xhu","add_box");
try {
    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
    {
	P.boxes[P.nbox].___(b);
	P.nbox = P.nbox+1;
	}
} finally {
LEAVING("egq4f4tmy1dhyj6jpj92r7xhu","add_box");
}
}




/* beginpath:
 * Set up boxes near the tail node.
 * For regular nodes, the result should be a list of contiguous rectangles 
 * such that the last one has the smallest LL.y and its LL.y is above
 * the bottom of the rank (rank.ht1).
 * 
 * For flat edges, we assume endp->sidemask has been set. For regular
 * edges, we set this, but it doesn't appear to be needed any more.
 * 
 * In many cases, we tweak the x or y coordinate of P->start.p by 1.
 * This is because of a problem in the path routing code. If the starting
 * point actually lies on the polygon, in some cases, the router gets
 * confused and routes the path outside the polygon. So, the offset ensures
 * the starting point is in the polygon.
 *
 * FIX: Creating the initial boxes only really works for rankdir=TB and
 * rankdir=LR. For the others, we rely on compassPort flipping the side
 * and then assume that the node shape has top-bottom symmetry. Since we
 * at present only put compass points on the bounding box, this works.
 * If we attempt to implement compass points on actual node perimeters,
 * something major will probably be necessary. Doing the coordinate
 * flip (postprocess) before spline routing will be too disruptive. The
 * correct solution is probably to have beginpath/endpath create the
 * boxes assuming an inverted node. Note that compassPort already does
 * some flipping. Even better would be to allow the *_path function
 * to provide a polygon.
 *
 * The extra space provided by FUDGE-2 prevents the edge from getting
 * too close the side of the node.
 *
 */

@Unused
@Todo(what = "bug72?")
@Reviewed(when = "02/12/2020")
@Comment(comment = "Side choice?")
@Original(version="2.38.0", path="lib/common/splines.c", name="beginpath", key="7pc43ifcw5g56449d101qf590", definition="void beginpath(path * P, edge_t * e, int et, pathend_t * endp, boolean merge)")
public static void beginpath(ST_path P, ST_Agedge_s e, int et, final ST_pathend_t endp, boolean merge) {
ENTERING("7pc43ifcw5g56449d101qf590","beginpath");
try {
    int side, mask;
    ST_Agnode_s n;
    CFunction pboxfn;
    
    n = agtail(e);
    
    if (ED_tail_port(e).dyna)
	ED_tail_port(e, resolvePort(agtail(e), aghead(e), ED_tail_port(e)));
    if (ND_shape(n)!=null)
	pboxfn = (CFunction) ND_shape(n).fns.pboxfn;
    else
	pboxfn = null;
    P.start.p.___(add_pointf(ND_coord(n), ED_tail_port(e).p));
    if (merge) {
	/*P->start.theta = - M_PI / 2; */
	P.start.theta = (conc_slope(agtail(e)));
	P.start.constrained= true;
    } else {
	if (ED_tail_port(e).constrained) {
	    P.start.theta = ED_tail_port(e).theta;
	    P.start.constrained= true;
	} else
	    P.start.constrained= false;
    }
    P.nbox = 0;
    P.data = e;
    endp.np.___(P.start.p);
    if ((et == REGULAREDGE) && (ND_node_type(n) == NORMAL) && ((side = ED_tail_port(e).side)!=0)) {
	ST_Agedge_s orig;
	final ST_boxf b0 = new ST_boxf(), b = endp.nb.copy();
	if ((side & TOP)!=0) {
UNSUPPORTED("1r4lctdj9z1ivlz3uqpcj1yzf"); // 	    endp->sidemask = 1<<2;
UNSUPPORTED("arq09sf82lsjuxwfkesprcrcv"); // 	    if (P->start.p.x < ND_coord(n).x) { /* go left */
UNSUPPORTED("bj4z8gwgs6j5fax8k6l3u6mv3"); // 		b0.LL.x = b.LL.x - 1;
UNSUPPORTED("54rmdm0xwy361tjs4aj6cv401"); // 		/* b0.LL.y = ND_coord(n).y + HT2(n); */
UNSUPPORTED("11ax5pxz4q2uh0nzsrs1qs7ck"); // 		b0.LL.y = P->start.p.y;
UNSUPPORTED("5xsapgq04e1hslq2835500q6k"); // 		b0.UR.x = b.UR.x;
UNSUPPORTED("9ro8mx52kgsoogvlgfubgn4p0"); // 		b0.UR.y = ND_coord(n).y + (ND_ht(n)/2) + GD_ranksep(agraphof(n))/2;
UNSUPPORTED("6p2nw1nh0qwn5ro3dltmd6w6c"); // 		b.UR.x = ND_coord(n).x - ND_lw(n) - (2-2);
UNSUPPORTED("czvxm3loj0won7ye2b3xrfbv4"); // 		b.UR.y = b0.LL.y;
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("w9wsmby4dawn9npux1jrd9gl"); // 		b.LL.x -= 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("7sk0msbospwpwupzwpu8v87qt"); // 		b0.LL.x = b.LL.x;
UNSUPPORTED("11ax5pxz4q2uh0nzsrs1qs7ck"); // 		b0.LL.y = P->start.p.y;
UNSUPPORTED("54rmdm0xwy361tjs4aj6cv401"); // 		/* b0.LL.y = ND_coord(n).y + HT2(n); */
UNSUPPORTED("4e5ydpfmxn1wuhnp78arn3f9x"); // 		b0.UR.x = b.UR.x+1;
UNSUPPORTED("9ro8mx52kgsoogvlgfubgn4p0"); // 		b0.UR.y = ND_coord(n).y + (ND_ht(n)/2) + GD_ranksep(agraphof(n))/2;
UNSUPPORTED("3f26r03ydc7aq52vcqpgxawgy"); // 		b.LL.x = ND_coord(n).x + ND_rw(n) + (2-2);
UNSUPPORTED("czvxm3loj0won7ye2b3xrfbv4"); // 		b.UR.y = b0.LL.y;
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("bqk56pohk8hpgn91lv4m2zkv0"); // 		b.UR.x += 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("196ta4n5nsqizd83y6oo7z8a2"); // 	    } 
UNSUPPORTED("b7lioq6g7moe5otds46c8hrc"); // 	    P->start.p.y += 1;
UNSUPPORTED("4v7mmisc358r5tpq14qp4dx0f"); // 	    endp->boxn = 2;
	}
	else if ((side & BOTTOM)!=0) {
	    endp.sidemask = BOTTOM;
	    b.UR.y = MAX(b.UR.y,P.start.p.y);
	    endp.boxes[0].___(b);
	    endp.boxn[0] = 1;
	    P.start.p.y -= 1;
	}
	else if ((side & LEFT)!=0) {
	    endp.sidemask = LEFT;
	    b.UR.x = P.start.p.x;
	    b.LL.y = ND_coord(n).y - HT2(n);
	    b.UR.y = P.start.p.y;
	    endp.boxes[0].___(b);
	    endp.boxn[0] = 1;
	    P.start.p.x -= 1;
	}
	else {
	    endp.sidemask = RIGHT;
	    b.LL.x = P.start.p.x;
	    b.LL.y = ND_coord(n).y - HT2(n);
	    b.UR.y = P.start.p.y;
	    endp.boxes[0].___(b);
	    endp.boxn[0] = 1;
	    P.start.p.x += 1;
	}
	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
	if (EQ(n, agtail(orig)))
	    ED_tail_port(orig).clip = false;
	else
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
 	return;
    }
    if ((et == FLATEDGE) && ((side = ED_tail_port(e).side)!=0)) {
UNSUPPORTED("ew7nyfe712nsiphifeztwxfop"); // 	boxf b0, b = endp->nb;
UNSUPPORTED("a7lrhlfwr0y475aqjk6abhb3b"); // 	edge_t* orig;
UNSUPPORTED("ait3wtnnvt134z2k87lvhq4ek"); // 	if (side & (1<<2)) {
UNSUPPORTED("d7fd91oymbo1kkxfqhtbe2jky"); // 	    b.LL.y = MIN(b.LL.y,P->start.p.y);
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("b7lioq6g7moe5otds46c8hrc"); // 	    P->start.p.y += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3s4re3z7asydnnotdylt94t1d"); // 	else if (side & (1<<0)) {
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("7vjialx9rln6cj2y0ni5nc2gi"); // 		b0.UR.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("4e5ydpfmxn1wuhnp78arn3f9x"); // 		b0.UR.x = b.UR.x+1;
UNSUPPORTED("1zpea73m3d4hdldoc5sypz1ag"); // 		b0.LL.x = P->start.p.x;
UNSUPPORTED("esamvv08qn005uqko6caft2u"); // 		b0.LL.y = b0.UR.y - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("3f26r03ydc7aq52vcqpgxawgy"); // 		b.LL.x = ND_coord(n).x + ND_rw(n) + (2-2);
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("bqk56pohk8hpgn91lv4m2zkv0"); // 		b.UR.x += 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("93chrd1duv0atudbvr439u7t4"); // 		endp->boxn = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8yftboq798vpnzuxkx6yuea18"); // 		b.UR.y = MAX(b.UR.y,P->start.p.y);
UNSUPPORTED("at4jfrag6jtwm7rxu8p4p8d46"); // 		endp->boxes[0] = b;
UNSUPPORTED("ev1muhahxwb1cntbhsb3c9aid"); // 		endp->boxn = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8pyl2559euuaxrntsyzj1ve8w"); // 	    P->start.p.y -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bn3pwhefgp4zdatx3g60lj0ou"); // 	else if (side & (1<<3)) {
UNSUPPORTED("bihp3ojpe2nsmh297nosihedn"); // 	    b.UR.x = P->start.p.x+1;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("afqhibyplfg1fftlkny8jq78t"); // 		b.LL.y = P->start.p.y-1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("4no3qn8v4vx6rk2in60hgr8w6"); // 		b.UR.y = P->start.p.y+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("celmm9njwdxhpvd56zon98hrr"); // 	    P->start.p.x -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("cysdxceleujmu3rckrhibxaqd"); // 	    b.LL.x = P->start.p.x;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("5oh26jb6vz012qke7865hz5h7"); // 		b.LL.y = P->start.p.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("4no3qn8v4vx6rk2in60hgr8w6"); // 		b.UR.y = P->start.p.y+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("1n8o29xgguq4cce4rf04o5rke"); // 	    P->start.p.x += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4gatpb3u0rq9nikm6rjtejp85"); // 	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
UNSUPPORTED("askl6l2rq6b2bznfxj7ydvz5t"); // 	if (n == agtail(orig))
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
UNSUPPORTED("8jqn3kj2hrrlcifbw3x9sf6qu"); // 	endp->sidemask = side;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    
    if (et == REGULAREDGE) side = BOTTOM;
    else side = endp.sidemask;  /* for flat edges */
    if (pboxfn!=null
	&& (mask = (Integer) pboxfn.exe(n, ED_tail_port(e), side, endp.boxes[0], endp.boxn))!=0)
	endp.sidemask = mask;
    else {
    endp.boxes[0].___(endp.nb);
	endp.boxn[0] = 1;
	switch (et) {
	case SELFEDGE:
	/* moving the box UR.y by + 1 avoids colinearity between
	   port point and box that confuses Proutespline().  it's
	   a bug in Proutespline() but this is the easiest fix. */
UNSUPPORTED("9rnob8jdqqdjwzanv53yxc47u"); // 	    assert(0);  /* at present, we don't use beginpath for selfedges */
UNSUPPORTED("46vb5zg9vm9n0q21g53nj66v3"); // 	    endp->boxes[0].UR.y = P->start.p.y - 1;
UNSUPPORTED("auefgwb39x5hzqqc9b1zgl239"); // 	    endp->sidemask = 1<<0;
	    break;
	case FLATEDGE:
	    if (endp.sidemask == TOP)
		endp.boxes[0].LL.y = P.start.p.y;
	    else
	    	endp.boxes[0].UR.y = P.start.p.y;
	    break;
	case REGULAREDGE:
	    endp.boxes[0].UR.y = P.start.p.y;
	    endp.sidemask = BOTTOM;
	    P.start.p.y -= 1;
	    break;
	}    
    }    
} finally {
LEAVING("7pc43ifcw5g56449d101qf590","beginpath");
}
}



private static final int FUDGE = 2;
private static double HT2(ST_Agnode_s n) {
	return ND_ht(n)/2;
}




//3 79dr5om55xs3n5lgai1sf58vu
// void endpath(path * P, edge_t * e, int et, pathend_t * endp, boolean merge) 
@Unused
@Reviewed(when = "02/12/2020")
@Original(version="2.38.0", path="lib/common/splines.c", name="endpath", key="79dr5om55xs3n5lgai1sf58vu", definition="void endpath(path * P, edge_t * e, int et, pathend_t * endp, boolean merge)")
public static void endpath(ST_path P, ST_Agedge_s e, int et, final ST_pathend_t endp, boolean merge) {
ENTERING("79dr5om55xs3n5lgai1sf58vu","endpath");
try {
    int side, mask;
    ST_Agnode_s n;
    CFunction pboxfn;
    
    n = aghead(e);
    
    if (ED_head_port(e).dyna) 
	ED_head_port(e, resolvePort(aghead(e), agtail(e), ED_head_port(e)));
    if (ND_shape(n)!=null)
	pboxfn = (CFunction) ND_shape(n).fns.pboxfn;
    else
	pboxfn = null;
    P.end.p.___(add_pointf(ND_coord(n), ED_head_port(e).p));
    if (merge) {
UNSUPPORTED("cproejwusj67kuugolh6tbkwz"); // 	/*P->end.theta = M_PI / 2; */
UNSUPPORTED("65vhfvz1d1tub3f85tdsgg2g5"); // 	P->end.theta = conc_slope(aghead(e)) + M_PI;
UNSUPPORTED("du4hwt6pjf3bmkvowssm7b0uo"); // 	assert(P->end.theta < 2 * M_PI);
UNSUPPORTED("2w0c22i5xgcch77xd9jg104nw"); // 	P->end.constrained = NOT(0);
    } else {
	if (ED_head_port(e).constrained) {
	    P.end.theta = ED_head_port(e).theta;
	    P.end.constrained = true;
	} else
	    P.end.constrained = false;
    }
    endp.np.___(P.end.p);
    if ((et == REGULAREDGE) && (ND_node_type(n) == NORMAL) && ((side = ED_head_port(e).side)!=0)) {
	ST_Agedge_s orig;
	final ST_boxf b0 = new ST_boxf(), b = endp.nb.copy();
	if ((side & TOP)!=0) {
	    endp.sidemask = TOP;
	    b.LL.y = MIN(b.LL.y,P.end.p.y);
	    endp.boxes[0].___(b);
	    endp.boxn[0] = 1;
	    P.end.p.y += 1;
	}
	else if ((side & BOTTOM)!=0) {
UNSUPPORTED("auefgwb39x5hzqqc9b1zgl239"); // 	    endp->sidemask = 1<<0;
UNSUPPORTED("4tlqpclu7x0szo1rszndqau0d"); // 	    if (P->end.p.x < ND_coord(n).x) { /* go left */
UNSUPPORTED("80ypgtfgfrgq8j7whkaueouh5"); // 		b0.LL.x = b.LL.x-1;
UNSUPPORTED("4ikkdf5k4ubwp4ou51rth0q41"); // 		/* b0.UR.y = ND_coord(n).y - HT2(n); */
UNSUPPORTED("baysgwgvs09ywaufn74gq6m0a"); // 		b0.UR.y = P->end.p.y;
UNSUPPORTED("5xsapgq04e1hslq2835500q6k"); // 		b0.UR.x = b.UR.x;
UNSUPPORTED("7ut9yqcephghob5a3yo8af293"); // 		b0.LL.y = ND_coord(n).y - (ND_ht(n)/2) - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("6p2nw1nh0qwn5ro3dltmd6w6c"); // 		b.UR.x = ND_coord(n).x - ND_lw(n) - (2-2);
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("w9wsmby4dawn9npux1jrd9gl"); // 		b.LL.x -= 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("7sk0msbospwpwupzwpu8v87qt"); // 		b0.LL.x = b.LL.x;
UNSUPPORTED("baysgwgvs09ywaufn74gq6m0a"); // 		b0.UR.y = P->end.p.y;
UNSUPPORTED("4ikkdf5k4ubwp4ou51rth0q41"); // 		/* b0.UR.y = ND_coord(n).y - HT2(n); */
UNSUPPORTED("4e5ydpfmxn1wuhnp78arn3f9x"); // 		b0.UR.x = b.UR.x+1;
UNSUPPORTED("7ut9yqcephghob5a3yo8af293"); // 		b0.LL.y = ND_coord(n).y - (ND_ht(n)/2) - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("3f26r03ydc7aq52vcqpgxawgy"); // 		b.LL.x = ND_coord(n).x + ND_rw(n) + (2-2);
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("bqk56pohk8hpgn91lv4m2zkv0"); // 		b.UR.x += 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("196ta4n5nsqizd83y6oo7z8a2"); // 	    } 
UNSUPPORTED("4v7mmisc358r5tpq14qp4dx0f"); // 	    endp->boxn = 2;
UNSUPPORTED("6kjd8mut2dn2xv1k1zr63qp0s"); // 	    P->end.p.y -= 1;
	}
	else if ((side & LEFT)!=0) {
UNSUPPORTED("2lmjkw07sr4x9a3xxrcb3yj07"); // 	    endp->sidemask = 1<<3;
UNSUPPORTED("4e2bsroer72trfy5dl5k8f5s8"); // 	    b.UR.x = P->end.p.x;
UNSUPPORTED("3rsswd4vcybmrbhoqt0aldqds"); // 	    b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("7m86tfoixpamdnl1ywyaz9uzy"); // 	    b.LL.y = P->end.p.y;
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("5j92wv3nt0b7hnlf3ktengoom"); // 	    P->end.p.x -= 1;
	}
	else {
	    endp.sidemask = RIGHT;
	    b.LL.x = P.end.p.x;
	    b.UR.y = ND_coord(n).y + HT2(n);
	    b.LL.y = P.end.p.y;
	    endp.boxes[0].___(b);
	    endp.boxn[0] = 1;
	    P.end.p.x += 1;
	}
	for (orig = e; ED_edge_type(orig) != NORMAL; orig = ED_to_orig(orig));
	if (EQ(n, aghead(orig)))
	    ED_head_port(orig).clip = false;
	else
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
	endp.sidemask = side;
	return;
    }
    if ((et == FLATEDGE) && ((side = ED_head_port(e).side)!=0)) {
UNSUPPORTED("ew7nyfe712nsiphifeztwxfop"); // 	boxf b0, b = endp->nb;
UNSUPPORTED("a7lrhlfwr0y475aqjk6abhb3b"); // 	edge_t* orig;
UNSUPPORTED("ait3wtnnvt134z2k87lvhq4ek"); // 	if (side & (1<<2)) {
UNSUPPORTED("cropv6s2edu614uzt364nepfo"); // 	    b.LL.y = MIN(b.LL.y,P->end.p.y);
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("c91rvfjkunah0qffpuo47eshu"); // 	    P->end.p.y += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3s4re3z7asydnnotdylt94t1d"); // 	else if (side & (1<<0)) {
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("80ypgtfgfrgq8j7whkaueouh5"); // 		b0.LL.x = b.LL.x-1;
UNSUPPORTED("7vjialx9rln6cj2y0ni5nc2gi"); // 		b0.UR.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("e403abqgqxgss6h01127ebeil"); // 		b0.UR.x = P->end.p.x;
UNSUPPORTED("esamvv08qn005uqko6caft2u"); // 		b0.LL.y = b0.UR.y - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("29fp8dba1xqbt5ire1m3oad6c"); // 		b.UR.x = ND_coord(n).x - ND_lw(n) - 2;
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("w9wsmby4dawn9npux1jrd9gl"); // 		b.LL.x -= 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("93chrd1duv0atudbvr439u7t4"); // 		endp->boxn = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8yftboq798vpnzuxkx6yuea18"); // 		b.UR.y = MAX(b.UR.y,P->start.p.y);
UNSUPPORTED("at4jfrag6jtwm7rxu8p4p8d46"); // 		endp->boxes[0] = b;
UNSUPPORTED("ev1muhahxwb1cntbhsb3c9aid"); // 		endp->boxn = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6kjd8mut2dn2xv1k1zr63qp0s"); // 	    P->end.p.y -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bn3pwhefgp4zdatx3g60lj0ou"); // 	else if (side & (1<<3)) {
UNSUPPORTED("46ayak01kn7y7w3yaoreb6w1l"); // 	    b.UR.x = P->end.p.x+1;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("a3bb90cu4chg4dv4xfsx8r8ek"); // 		b.LL.y = P->end.p.y-1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("20q189zumqwpltcod94td3f"); // 		b.UR.y = P->end.p.y+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("5j92wv3nt0b7hnlf3ktengoom"); // 	    P->end.p.x -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("9tx1p6meq5zi4ce5essw11ikg"); // 	    b.LL.x = P->end.p.x-1;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("a3bb90cu4chg4dv4xfsx8r8ek"); // 		b.LL.y = P->end.p.y-1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("181rv2y41gamwqbbccj0rnb57"); // 		b.UR.y = P->end.p.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("44vy3z49e2oo6613r15tcgn8h"); // 	    P->end.p.x += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4gatpb3u0rq9nikm6rjtejp85"); // 	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
UNSUPPORTED("e8cujr3gqet8mj2n5h5jfogm1"); // 	if (n == aghead(orig))
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
UNSUPPORTED("8jqn3kj2hrrlcifbw3x9sf6qu"); // 	endp->sidemask = side;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    
    if (et == REGULAREDGE) side = TOP;
    else side = endp.sidemask;  /* for flat edges */
    if (pboxfn!=null
	&& (mask = (Integer) pboxfn.exe(n, ED_head_port(e), side, endp.boxes[0], endp.boxn))!=0)
	endp.sidemask = mask;
    else {
    	endp.boxes[0].___(endp.nb);
	endp.boxn[0] = 1;
	
	switch (et) {
	case SELFEDGE:
	    /* offset of -1 is symmetric w.r.t. beginpath() 
	     * FIXME: is any of this right?  what if self-edge 
	     * doesn't connect from BOTTOM to TOP??? */
UNSUPPORTED("bhkhf4i9pvxtxyka4sobszg33"); // 	    assert(0);  /* at present, we don't use endpath for selfedges */
UNSUPPORTED("db6vmvnse8bawy8qwct7l24u8"); // 	    endp->boxes[0].LL.y = P->end.p.y + 1;
UNSUPPORTED("1r4lctdj9z1ivlz3uqpcj1yzf"); // 	    endp->sidemask = 1<<2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
	case FLATEDGE:
	    if (endp.sidemask == TOP)
	    	endp.boxes[0].LL.y = P.end.p.y;
	    else
	    	endp.boxes[0].UR.y = P.end.p.y;
	    break;
	case REGULAREDGE:
		endp.boxes[0].LL.y = P.end.p.y;
	    endp.sidemask = TOP;
	    P.end.p.y += 1;
	    break;
	}
    }
} finally {
LEAVING("79dr5om55xs3n5lgai1sf58vu","endpath");
}
}




//3 3g7alj6eirl5b2hlhluiqvaax
// static int convert_sides_to_points(int tail_side, int head_side) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="convert_sides_to_points", key="3g7alj6eirl5b2hlhluiqvaax", definition="static int convert_sides_to_points(int tail_side, int head_side)")
public static int convert_sides_to_points(int tail_side, int head_side) {
ENTERING("3g7alj6eirl5b2hlhluiqvaax","convert_sides_to_points");
int vertices[] = new int[] {12,4,6,2,3,1,9,8};  //the cumulative side value of each node point
int i, tail_i, head_i;
int pair_a[][] = new int[][] {	    //array of possible node point pairs
{11,12,13,14,15,16,17,18},
{21,22,23,24,25,26,27,28},
{31,32,33,34,35,36,37,38},
{41,42,43,44,45,46,47,48},
{51,52,53,54,55,56,57,58},
{61,62,63,64,65,66,67,68},
{71,72,73,74,75,76,77,78},
{81,82,83,84,85,86,87,88}
};
try {
 tail_i = head_i = -1;
	for(i=0;i< 8; i++){
		if(head_side == vertices[i]){
			head_i = i;
			break;
		}
	}
	for(i=0;i< 8; i++){
		if(tail_side == vertices[i]){
			tail_i = i;
			break;
		}
	}
if( tail_i < 0 || head_i < 0)
  return 0;
else
  return pair_a[tail_i][head_i];
} finally {
LEAVING("3g7alj6eirl5b2hlhluiqvaax","convert_sides_to_points");
}
}




//3 7l37y1w97mt6n5pd9x5dzgwud
// static void selfBottom (edge_t* edges[], int ind, int cnt, 	double sizex, double stepy, splineInfo* sinfo)  
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="selfBottom", key="7l37y1w97mt6n5pd9x5dzgwud", definition="static void selfBottom (edge_t* edges[], int ind, int cnt, 	double sizex, double stepy, splineInfo* sinfo)")
public static Object selfBottom(Object... arg_) {
UNSUPPORTED("5mldqfen59kshqgaknayjc5ox"); // static void selfBottom (edge_t* edges[], int ind, int cnt,
UNSUPPORTED("e0472i5ngodtv68y0hdhq1azu"); // 	double sizex, double stepy, splineInfo* sinfo) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7a2vzpy4tpc2fpmuf12nhtfca"); //     pointf tp, hp, np;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("2131r3ibxv7drmcz6f2j5d9c2"); //     int i, sgn, point_pair;
UNSUPPORTED("de1bz9yfc9w49kc4vy1ge2ltd"); //     double hy, ty, stepx, dx, dy, width, height; 
UNSUPPORTED("cutkizwxyuykhmayeb60m22av"); //     pointf points[1000];
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("e3wy3x07xdsusfbgecfcqg5lj"); //     e = edges[ind];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("43yzlf5354g6qlugyzpmr745t"); //     stepx = (sizex / 2.) / cnt;
UNSUPPORTED("brakcbw9hvzlljogqwzlhgb0v"); //     stepx = MAX(stepx,2.);
UNSUPPORTED("dko3xt785e372nj0fiocjfas"); //     pointn = 0;
UNSUPPORTED("dqazhjgevh1spyg3xzwb3bcks"); //     np = ND_coord(n);
UNSUPPORTED("ehf9o80lfi02no07wz207kyp6"); //     tp = ED_tail_port(e).p;
UNSUPPORTED("f18822xrptoagri7001gamxwh"); //     tp.x += np.x;
UNSUPPORTED("pcmp8bdd8677mjvvef7kfh5y"); //     tp.y += np.y;
UNSUPPORTED("b4mfdkjjk3n78ssy4h80g5lc6"); //     hp = ED_head_port(e).p;
UNSUPPORTED("e7rhhgc42h5z6kvvnkz6wfn0r"); //     hp.x += np.x;
UNSUPPORTED("bisu3qji6rw3wu3srdv8vhrxb"); //     hp.y += np.y;
UNSUPPORTED("2c8kmvidaqx92wd2mq1ys6753"); //     if (tp.x >= hp.x) sgn = 1;
UNSUPPORTED("cvln1r5ffbp1z1sq0y6ago4og"); //     else sgn = -1;
UNSUPPORTED("7squuk10wt6xrbp24obpx41bw"); //     dy = ND_ht(n)/2., dx = 0.;
UNSUPPORTED("7sojr831wk2u8c86xerkjyojd"); //     // certain adjustments are required for some point_pairs in order to improve the 
UNSUPPORTED("byuachd2fjte06s7xwnbmxlcx"); //     // display of the edge path between them
UNSUPPORTED("eje36stfd9p7ulgo4qk6gjwvx"); //     point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
UNSUPPORTED("2qmvjd6iwnaqwop679caoaxnn"); //     switch(point_pair){
UNSUPPORTED("8c31t4u50f9yjnlb8ii84ts3w"); //       case 67:  sgn = -sgn;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("5vhsnixpf0pg2oz10ps2valyn"); //       default:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5n29oadzd6emvd2fwjisx6ovk"); //     ty = MIN(dy, 3*(tp.y + dy - np.y));
UNSUPPORTED("dly5hufg66dgb6zn5lqcerae1"); //     hy = MIN(dy, 3*(hp.y + dy - np.y));
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("a0u9ggni4r8gikqyyxf6wgaa5"); //         e = edges[ind++];
UNSUPPORTED("bgymnp4yekw8tzr70cnzzn9ez"); //         dy += stepy, ty += stepy, hy += stepy, dx += sgn*stepx;
UNSUPPORTED("8tkxpvgpxpilkes33cj73nr8o"); //         pointn = 0;
UNSUPPORTED("2j93ajzz3i9adm0syj177su98"); //         points[pointn++] = tp;
UNSUPPORTED("15uyub8ah85dmbdmc0lqgjqb"); //         points[pointn++] = pointfof(tp.x + dx, tp.y - ty / 3);
UNSUPPORTED("bh0lpazk6gpagl57bydccqkv4"); //         points[pointn++] = pointfof(tp.x + dx, np.y - dy);
UNSUPPORTED("381vppahpairjja0hahm7lktb"); //         points[pointn++] = pointfof((tp.x+hp.x)/2, np.y - dy);
UNSUPPORTED("n63wd0j09ndu0hiaxhwx7izb"); //         points[pointn++] = pointfof(hp.x - dx, np.y - dy);
UNSUPPORTED("dzdgwa3zfedg3kys9pd8mp5qm"); //         points[pointn++] = pointfof(hp.x - dx, hp.y - hy / 3);
UNSUPPORTED("6t0sueo9zyoccfzqit4c7pvcy"); //         points[pointn++] = hp;
UNSUPPORTED("6nhnbriaxn7zi0ab1z8bkbzd"); //         if (ED_label(e)) {
UNSUPPORTED("a7ea1ybpt7lv8fk1pc1outbs5"); // 	if (GD_flip(agraphof(agtail(e)))) {
UNSUPPORTED("7d83ym7h1stime4wbmifcx809"); //     	    width = ED_label(e)->dimen.y;
UNSUPPORTED("44m5sni7g3n6fnk6ca57u9dc2"); //     	    height = ED_label(e)->dimen.x;
UNSUPPORTED("s8koz5x85ytpnff1o94rlxqy"); //     	} else {
UNSUPPORTED("66vu2joy64r1yrkvp3oolz1ws"); //     	    width = ED_label(e)->dimen.x;
UNSUPPORTED("d6bobo1f6gxkxa2fffvmn41g0"); //     	    height = ED_label(e)->dimen.y;
UNSUPPORTED("klxoy56t7b20wxnwqm0qoofz"); //     	}
UNSUPPORTED("cot4bdvsbrav4yex2yesffgd9"); //     	ED_label(e)->pos.y = ND_coord(n).y - dy - height / 2.0;
UNSUPPORTED("9wg1yftg90g8jld2m2p5m31ro"); //     	ED_label(e)->pos.x = ND_coord(n).x;
UNSUPPORTED("7efx4yevu8176mmuqjtk4bfss"); //     	ED_label(e)->set = NOT(0);
UNSUPPORTED("13o3f1bpjm731ee8hpa8d3f5y"); //     	if (height > stepy)
UNSUPPORTED("alt1jvhdhimr8iltoxg7dycq1"); //     	    dy += height - stepy;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5fvid2bi7fy5jv5dyttfprpzj"); //         clip_and_install(e, aghead(e), points, pointn, sinfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2thwh4ase1jdq8ghhf0oqyql5
// static void selfTop (edge_t* edges[], int ind, int cnt, double sizex, double stepy,            splineInfo* sinfo)  
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="selfTop", key="2thwh4ase1jdq8ghhf0oqyql5", definition="static void selfTop (edge_t* edges[], int ind, int cnt, double sizex, double stepy,            splineInfo* sinfo)")
public static Object selfTop(Object... arg_) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("32kq3vfpd1msv3v0nv0uqavzh"); // selfTop (edge_t* edges[], int ind, int cnt, double sizex, double stepy,
UNSUPPORTED("2t4o7k97lw32u08cs5j96r7if"); //            splineInfo* sinfo) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2131r3ibxv7drmcz6f2j5d9c2"); //     int i, sgn, point_pair;
UNSUPPORTED("1suoh1r8nnndqo9txafuch8az"); //     double hy, ty,  stepx, dx, dy, width, height; 
UNSUPPORTED("7a2vzpy4tpc2fpmuf12nhtfca"); //     pointf tp, hp, np;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("cutkizwxyuykhmayeb60m22av"); //     pointf points[1000];
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("e3wy3x07xdsusfbgecfcqg5lj"); //     e = edges[ind];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("43yzlf5354g6qlugyzpmr745t"); //     stepx = (sizex / 2.) / cnt;
UNSUPPORTED("7199vb689fs8rdn6j40wpw2py"); //     stepx = MAX(stepx, 2.);
UNSUPPORTED("dko3xt785e372nj0fiocjfas"); //     pointn = 0;
UNSUPPORTED("dqazhjgevh1spyg3xzwb3bcks"); //     np = ND_coord(n);
UNSUPPORTED("ehf9o80lfi02no07wz207kyp6"); //     tp = ED_tail_port(e).p;
UNSUPPORTED("f18822xrptoagri7001gamxwh"); //     tp.x += np.x;
UNSUPPORTED("pcmp8bdd8677mjvvef7kfh5y"); //     tp.y += np.y;
UNSUPPORTED("b4mfdkjjk3n78ssy4h80g5lc6"); //     hp = ED_head_port(e).p;
UNSUPPORTED("e7rhhgc42h5z6kvvnkz6wfn0r"); //     hp.x += np.x;
UNSUPPORTED("bisu3qji6rw3wu3srdv8vhrxb"); //     hp.y += np.y;
UNSUPPORTED("2c8kmvidaqx92wd2mq1ys6753"); //     if (tp.x >= hp.x) sgn = 1;
UNSUPPORTED("cvln1r5ffbp1z1sq0y6ago4og"); //     else sgn = -1;
UNSUPPORTED("7squuk10wt6xrbp24obpx41bw"); //     dy = ND_ht(n)/2., dx = 0.;
UNSUPPORTED("7sojr831wk2u8c86xerkjyojd"); //     // certain adjustments are required for some point_pairs in order to improve the 
UNSUPPORTED("byuachd2fjte06s7xwnbmxlcx"); //     // display of the edge path between them
UNSUPPORTED("eje36stfd9p7ulgo4qk6gjwvx"); //     point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
UNSUPPORTED("2qmvjd6iwnaqwop679caoaxnn"); //     switch(point_pair){
UNSUPPORTED("6mjalqxwnjw8e27c2ioujowul"); // 	case 15:	
UNSUPPORTED("5vvzajt4nlp9tr9qagb46uzw0"); // 		dx = sgn*(ND_rw(n) - (hp.x-np.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("av3gl91zikst7e3hby657df3z"); // 	case 38:
UNSUPPORTED("d2wzrbnbuinus07v39wtrzg6k"); // 		dx = sgn*(ND_lw(n)-(np.x-hp.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dk2te1ff65z24g7yge6td5w1h"); // 	case 41:
UNSUPPORTED("54zp7hq4t1477ra0toi6nfc3s"); // 		dx = sgn*(ND_rw(n)-(tp.x-np.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("eyz8046vmrhfd05uo35ud2o26"); // 	case 48:
UNSUPPORTED("54zp7hq4t1477ra0toi6nfc3s"); // 		dx = sgn*(ND_rw(n)-(tp.x-np.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("90hjo1ph35lg8jy4yywzro3nf"); // 	case 14:
UNSUPPORTED("23d0sltghssogk5wk9024lh41"); // 	case 37:
UNSUPPORTED("3dvppfwsy4t6h54uecu5i9hry"); // 	case 47:
UNSUPPORTED("95n009mwo78h9zg1mx5yc3j7l"); // 	case 51:
UNSUPPORTED("8ytmvd73zq9qu5c4ku4jcap4a"); // 	case 57:
UNSUPPORTED("1tbpkq9m2taj7n3fj63cocjyn"); // 	case 58:
UNSUPPORTED("u36w11cbjvnwnr2a9aukmfop"); // 		dx = sgn*((((ND_lw(n)-(np.x-tp.x)) + (ND_rw(n)-(hp.x-np.x)))/3.));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("a92kp8x7ej800lliiwzfuobem"); // 	case 73:
UNSUPPORTED("c22dsvqh8h2a9v76t3u9dzyi4"); //  		dx = sgn*(ND_lw(n)-(np.x-tp.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("3ijo2dao8lyum56ai3jujbmap"); // 	case 83:
UNSUPPORTED("bjovxk89tmb4rsuvw09nszp4c"); // 		dx = sgn*(ND_lw(n)-(np.x-tp.x));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("f4rvirpst5ft3uksqp3okyjcf"); // 	case 84:
UNSUPPORTED("1i3gur9btuj2u5s8ybbgmd87y"); // 		dx = sgn*((((ND_lw(n)-(np.x-tp.x)) + (ND_rw(n)-(hp.x-np.x)))/2.) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("72fiv7451m2qnkzbfjcwv7pgx"); // 	case 74:
UNSUPPORTED("1yel8f4unntut6w7bgdhgsvq1"); // 	case 75:
UNSUPPORTED("93kdallci9a743giye6pd0y0f"); // 	case 85:
UNSUPPORTED("919n1grj4s92nolxlmoqx4qqw"); // 		dx = sgn*((((ND_lw(n)-(np.x-tp.x)) + (ND_rw(n)-(hp.x-np.x)))/2.) + 2*stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7jtwt2insvwb55tafkqx3boot"); //     ty = MIN(dy, 3*(np.y + dy - tp.y));
UNSUPPORTED("7vwwj5yxukw3e1k0twakhhgg3"); //     hy = MIN(dy, 3*(np.y + dy - hp.y));
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("a0u9ggni4r8gikqyyxf6wgaa5"); //         e = edges[ind++];
UNSUPPORTED("bgymnp4yekw8tzr70cnzzn9ez"); //         dy += stepy, ty += stepy, hy += stepy, dx += sgn*stepx;
UNSUPPORTED("8tkxpvgpxpilkes33cj73nr8o"); //         pointn = 0;
UNSUPPORTED("2j93ajzz3i9adm0syj177su98"); //         points[pointn++] = tp;
UNSUPPORTED("810s5qsu6it4vef0j2l5blqdm"); //         points[pointn++] = pointfof(tp.x + dx, tp.y + ty / 3);
UNSUPPORTED("r9y9vrfhtcn0ly9mxyipodbo"); //         points[pointn++] = pointfof(tp.x + dx, np.y + dy);
UNSUPPORTED("576fgxddv6rfxjwqc4ziex02m"); //         points[pointn++] = pointfof((tp.x+hp.x)/2, np.y + dy);
UNSUPPORTED("7wozarouo08hg5qnrcqmlrzv1"); //         points[pointn++] = pointfof(hp.x - dx, np.y + dy);
UNSUPPORTED("6z0fdvc1cxk34nwjps2o0vy9e"); //         points[pointn++] = pointfof(hp.x - dx, hp.y + hy / 3);
UNSUPPORTED("6t0sueo9zyoccfzqit4c7pvcy"); //         points[pointn++] = hp;
UNSUPPORTED("6nhnbriaxn7zi0ab1z8bkbzd"); //         if (ED_label(e)) {
UNSUPPORTED("95cz173vhlho6qxwqiafjznd6"); // 	    if (GD_flip(agraphof(agtail(e)))) {
UNSUPPORTED("5tq797micincut6x05g6eokxk"); // 		width = ED_label(e)->dimen.y;
UNSUPPORTED("2wpl3ja2mlxynjamnyblux5j"); // 		height = ED_label(e)->dimen.x;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("5oxmxe34kl5iq4p27e8r7k11y"); // 		width = ED_label(e)->dimen.x;
UNSUPPORTED("4eunm5kqgzuzko60febalr1gg"); // 		height = ED_label(e)->dimen.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8ygvy3gas9hrwh99z44j4vw14"); // 	    ED_label(e)->pos.y = ND_coord(n).y + dy + height / 2.0;
UNSUPPORTED("89l2ovblsu6gnx97clo8ev1yk"); // 	    ED_label(e)->pos.x = ND_coord(n).x;
UNSUPPORTED("3tkba5lhpnujfu8lcz8lewsyn"); // 	    ED_label(e)->set = NOT(0);
UNSUPPORTED("df1lpvk1x9s2nna4dimpv5ixv"); // 	    if (height > stepy)
UNSUPPORTED("anykz2jqihvnza16edujzsmnm"); // 		dy += height - stepy;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("iyaed8bkc8xb16vcnxvc7d6s"); //        clip_and_install(e, aghead(e), points, pointn, sinfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b9185t6i77ez1ac587ul8ndnc"); //     return;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3sr8gvj4141qql0v12lb89cyt
// static void selfRight (edge_t* edges[], int ind, int cnt, double stepx, double sizey,            splineInfo* sinfo)  
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="selfRight", key="3sr8gvj4141qql0v12lb89cyt", definition="static void selfRight (edge_t* edges[], int ind, int cnt, double stepx, double sizey,            splineInfo* sinfo)")
public static void selfRight(CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, double stepx, double sizey, ST_splineInfo sinfo) {
ENTERING("3sr8gvj4141qql0v12lb89cyt","selfRight");
try {
    int i, sgn, point_pair;
    double hx, tx, stepy, dx, dy, width, height; 
    final ST_pointf tp = new ST_pointf(), hp = new ST_pointf(), np = new ST_pointf();
    ST_Agnode_s n;
    ST_Agedge_s e;
    final CArray<ST_pointf> points = CArray.<ST_pointf>ALLOC__(1000, ST_pointf.class);
    int pointn;
    e = edges.get_(ind);
    n = agtail(e);
    stepy = (sizey / 2.) / cnt;
    stepy = MAX(stepy, 2.);
    pointn = 0;
    np.___(ND_coord(n));
    tp.___(ED_tail_port(e).p);
    tp.x = tp.x + np.x;
    tp.y = tp.y + np.y;
    hp.___(ED_head_port(e).p);
    hp.x = hp.x + np.x;
    hp.y = hp.y + np.y;
    if (tp.y >= hp.y) sgn = 1;
    else sgn = -1;
    dx = ND_rw(n);
    dy = 0;
    // certain adjustments are required for some point_pairs in order to improve the 
    // display of the edge path between them
    point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
    switch(point_pair){
      case 32: 
      case 65:	if(tp.y == hp.y)
		  sgn = -sgn;
		break;
      default:
		break;
    }
    tx = MIN(dx, 3*(np.x + dx - tp.x));
    hx = MIN(dx, 3*(np.x + dx - hp.x));
    for (i = 0; i < cnt; i++) {
        e = edges.get_(ind++);
        dx += stepx; tx += stepx; hx += stepx; dy += sgn*stepy;
        pointn = 0;
        points.get__(pointn++).___(tp);
        points.get__(pointn++).___(pointfof(tp.x + tx / 3, tp.y + dy));
        points.get__(pointn++).___(pointfof(np.x + dx, tp.y + dy));
        points.get__(pointn++).___(pointfof(np.x + dx, (tp.y+hp.y)/2));
        points.get__(pointn++).___(pointfof(np.x + dx, hp.y - dy));
        points.get__(pointn++).___(pointfof(hp.x + hx / 3, hp.y - dy));
        points.get__(pointn++).___(hp);
        if (ED_label(e)!=null) {
	    if (GD_flip(agraphof(agtail(e)))) {
		width = ED_label(e).dimen.y;
		height = ED_label(e).dimen.x;
	    } else {
		width = ED_label(e).dimen.x;
		height = ED_label(e).dimen.y;
	    }
	    ED_label(e).pos.x = ND_coord(n).x + dx + width / 2.0;
	    ED_label(e).pos.y = ND_coord(n).y;
	    ED_label(e).set= NOTI(false);
	    if (width > stepx)
		dx += width - stepx;
        }
	clip_and_install(e, aghead(e), points, pointn, sinfo);
    }
    return;
} finally {
LEAVING("3sr8gvj4141qql0v12lb89cyt","selfRight");
}
}




//3 pb3pqqgfs6pzscxz9g4ip66b
// static void selfLeft (edge_t* edges[], int ind, int cnt, double stepx, double sizey,           splineInfo* sinfo)  
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="selfLeft", key="pb3pqqgfs6pzscxz9g4ip66b", definition="static void selfLeft (edge_t* edges[], int ind, int cnt, double stepx, double sizey,           splineInfo* sinfo)")
public static Object selfLeft(Object... arg_) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("e1xon7wncs6szxkut7r3ylg8a"); // selfLeft (edge_t* edges[], int ind, int cnt, double stepx, double sizey,
UNSUPPORTED("304grcrgelbk1tnep5avkaylv"); //           splineInfo* sinfo) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5kzmtbtnct9t7sugzyg0t1505"); //     int i, sgn,point_pair;
UNSUPPORTED("9oe5un4g42lfm6s5sruhajt5n"); //     double hx, tx, stepy, dx, dy, width, height; 
UNSUPPORTED("7a2vzpy4tpc2fpmuf12nhtfca"); //     pointf tp, hp, np;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("cutkizwxyuykhmayeb60m22av"); //     pointf points[1000];
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("e3wy3x07xdsusfbgecfcqg5lj"); //     e = edges[ind];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("2biq5cfn3eflyc9vcakp8z40j"); //     stepy = (sizey / 2.) / cnt;
UNSUPPORTED("ag6m3hxmkt2fwxfbd09gtse84"); //     stepy = MAX(stepy,2.);
UNSUPPORTED("dko3xt785e372nj0fiocjfas"); //     pointn = 0;
UNSUPPORTED("dqazhjgevh1spyg3xzwb3bcks"); //     np = ND_coord(n);
UNSUPPORTED("ehf9o80lfi02no07wz207kyp6"); //     tp = ED_tail_port(e).p;
UNSUPPORTED("f18822xrptoagri7001gamxwh"); //     tp.x += np.x;
UNSUPPORTED("pcmp8bdd8677mjvvef7kfh5y"); //     tp.y += np.y;
UNSUPPORTED("b4mfdkjjk3n78ssy4h80g5lc6"); //     hp = ED_head_port(e).p;
UNSUPPORTED("e7rhhgc42h5z6kvvnkz6wfn0r"); //     hp.x += np.x;
UNSUPPORTED("bisu3qji6rw3wu3srdv8vhrxb"); //     hp.y += np.y;
UNSUPPORTED("9pq7cc11wf5inm1gtl9nubola"); //     if (tp.y >= hp.y) sgn = 1;
UNSUPPORTED("cvln1r5ffbp1z1sq0y6ago4og"); //     else sgn = -1;
UNSUPPORTED("5t4m5gzysfvdd5gfy1snezlv1"); //     dx = ND_lw(n), dy = 0.;
UNSUPPORTED("7sojr831wk2u8c86xerkjyojd"); //     // certain adjustments are required for some point_pairs in order to improve the 
UNSUPPORTED("byuachd2fjte06s7xwnbmxlcx"); //     // display of the edge path between them
UNSUPPORTED("eje36stfd9p7ulgo4qk6gjwvx"); //     point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
UNSUPPORTED("2qmvjd6iwnaqwop679caoaxnn"); //     switch(point_pair){
UNSUPPORTED("1ztn6qfhzw55cdorxgbs8mvaw"); //       case 12:
UNSUPPORTED("5nakmzm2t38aw7gowxf3597ny"); //       case 67:
UNSUPPORTED("bvy8vwcvwtkz9nqaq8173x6bh"); // 		if(tp.y == hp.y)
UNSUPPORTED("cffqbosum7o1l5iposy2evrfl"); // 		  sgn = -sgn;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("5vhsnixpf0pg2oz10ps2valyn"); //       default:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("p2znjsd0rhcqyou2d4tcp4m3"); //     tx = MIN(dx, 3*(tp.x + dx - np.x));
UNSUPPORTED("9fc36i8mydvpx5fb9s7oomgg9"); //     hx = MIN(dx, 3*(hp.x + dx - np.x));
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("a0u9ggni4r8gikqyyxf6wgaa5"); //         e = edges[ind++];
UNSUPPORTED("corxl7j4p1epemy3mlhnxuh7f"); //         dx += stepx, tx += stepx, hx += stepx, dy += sgn*stepy;
UNSUPPORTED("8tkxpvgpxpilkes33cj73nr8o"); //         pointn = 0;
UNSUPPORTED("2j93ajzz3i9adm0syj177su98"); //         points[pointn++] = tp;
UNSUPPORTED("1d3rn5phdxf8hhlmh3b3wp7lh"); //         points[pointn++] = pointfof(tp.x - tx / 3, tp.y + dy);
UNSUPPORTED("2wxmjkn0pmrslgogz96iftqs0"); //         points[pointn++] = pointfof(np.x - dx, tp.y + dy);
UNSUPPORTED("clocavnhfvokhhthg9cujkqa0"); //         points[pointn++] = pointfof(np.x - dx, (tp.y+hp.y)/2);
UNSUPPORTED("6tz9mqs3ff68mo5r1xmq2zyc4"); //         points[pointn++] = pointfof(np.x - dx, hp.y - dy);
UNSUPPORTED("a6oh2uv36d620c50ery1vvmd7"); //         points[pointn++] = pointfof(hp.x - hx / 3, hp.y - dy);
UNSUPPORTED("6t0sueo9zyoccfzqit4c7pvcy"); //         points[pointn++] = hp;
UNSUPPORTED("6nhnbriaxn7zi0ab1z8bkbzd"); //         if (ED_label(e)) {
UNSUPPORTED("7ewy2tc2zfli5k6dghdnao8tw"); //     	if (GD_flip(agraphof(agtail(e)))) {
UNSUPPORTED("7d83ym7h1stime4wbmifcx809"); //     	    width = ED_label(e)->dimen.y;
UNSUPPORTED("44m5sni7g3n6fnk6ca57u9dc2"); //     	    height = ED_label(e)->dimen.x;
UNSUPPORTED("s8koz5x85ytpnff1o94rlxqy"); //     	} else {
UNSUPPORTED("66vu2joy64r1yrkvp3oolz1ws"); //     	    width = ED_label(e)->dimen.x;
UNSUPPORTED("d6bobo1f6gxkxa2fffvmn41g0"); //     	    height = ED_label(e)->dimen.y;
UNSUPPORTED("klxoy56t7b20wxnwqm0qoofz"); //     	}
UNSUPPORTED("e7au5qlazz8i26lvbl9c5k657"); //     	ED_label(e)->pos.x = ND_coord(n).x - dx - width / 2.0;
UNSUPPORTED("dfo4prcp2cafipoufh8bql0id"); //     	ED_label(e)->pos.y = ND_coord(n).y;
UNSUPPORTED("7efx4yevu8176mmuqjtk4bfss"); //     	ED_label(e)->set = NOT(0);
UNSUPPORTED("8ivpntotxg2rgw585hkdgsixh"); //     	if (width > stepx)
UNSUPPORTED("34ujzn0u4l056cgabsn09ncw8"); //     	    dx += width - stepx;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5fvid2bi7fy5jv5dyttfprpzj"); //         clip_and_install(e, aghead(e), points, pointn, sinfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 678whq05s481ertx02jloteu3
// int selfRightSpace (edge_t* e) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="selfRightSpace", key="678whq05s481ertx02jloteu3", definition="int selfRightSpace (edge_t* e)")
public static int selfRightSpace(ST_Agedge_s e) {
ENTERING("678whq05s481ertx02jloteu3","selfRightSpace");
try {
    int sw=0;
    double label_width;
    ST_textlabel_t l = ED_label(e);
    if ((N(ED_tail_port(e).defined) && N(ED_head_port(e).defined)) ||
        (
		N(ED_tail_port(e).side & (1<<3)) && 
         N(ED_head_port(e).side & (1<<3)) &&
          ((ED_tail_port(e).side != ED_head_port(e).side) || 
          (N(ED_tail_port(e).side & ((1<<2)|(1<<0)))))
		  )) {
	sw = 18;
	if (l!=null) {
	    label_width = GD_flip(agraphof(aghead(e))) ? l.dimen.y : l.dimen.x;
	    sw += label_width;
    }
    }
    else sw = 0;
    return sw;
} finally {
LEAVING("678whq05s481ertx02jloteu3","selfRightSpace");
}
}




//3 bt3fwgprixbc5rceeewozdqr9
// void makeSelfEdge(path * P, edge_t * edges[], int ind, int cnt, double sizex, 	     double sizey, splineInfo * sinfo) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="makeSelfEdge", key="bt3fwgprixbc5rceeewozdqr9", definition="void makeSelfEdge(path * P, edge_t * edges[], int ind, int cnt, double sizex, 	     double sizey, splineInfo * sinfo)")
public static void makeSelfEdge(ST_path P, CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, double sizex, double sizey, ST_splineInfo sinfo) {
ENTERING("bt3fwgprixbc5rceeewozdqr9","makeSelfEdge");
try {
    ST_Agedge_s e;
    e = edges.get_(ind);
    /* self edge without ports or
     * self edge with all ports inside, on the right, or at most 1 on top 
     * and at most 1 on bottom 
     */
    if ((N(ED_tail_port(e).defined) && N(ED_head_port(e).defined)) ||
        (
		N(ED_tail_port(e).side & (1<<3)) && 
         N(ED_head_port(e).side & (1<<3)) &&
          ((ED_tail_port(e).side != ED_head_port(e).side) || 
          (N(ED_tail_port(e).side & ((1<<2)|(1<<0))))))) {
	selfRight(edges, ind, cnt, sizex, sizey, sinfo);
    }
    /* self edge with port on left side */
    else if ((ED_tail_port(e).side & (1<<3))!=0 || (ED_head_port(e).side & (1<<3))!=0) {
	/* handle L-R specially */
	if ((ED_tail_port(e).side & (1<<1))!=0 || (ED_head_port(e).side & (1<<1))!=0) {
	    selfTop(edges, ind, cnt, sizex, sizey, sinfo);
	}
	else {
	    selfLeft(edges, ind, cnt, sizex, sizey, sinfo);
	}
    }
    /* self edge with both ports on top side */
    else if ((ED_tail_port(e).side & (1<<2))!=0) {
	selfTop(edges, ind, cnt, sizex, sizey, sinfo);
    }
    else if ((ED_tail_port(e).side & (1<<0))!=0) {
	selfBottom(edges, ind, cnt, sizex, sizey, sinfo);
    }
    else assert(false);
} finally {
LEAVING("bt3fwgprixbc5rceeewozdqr9","makeSelfEdge");
}
}





/* endPoints:
 * Extract the actual end points of the spline, where
 * they touch the node.
 */@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="endPoints", key="7wyn51o9k6u7joaq9k18boffh", definition="static void endPoints(splines * spl, pointf * p, pointf * q)")
public static void endPoints(ST_splines spl, ST_pointf p, ST_pointf q) {
ENTERING("7wyn51o9k6u7joaq9k18boffh","endPoints");
try {
     final ST_bezier bz = new ST_bezier();
     
     bz.___(spl.list.get__(0));
     if (bz.sflag!=0) {
UNSUPPORTED("4wazlko0bxmzxoobqacij1btk"); // 	*p = bz.sp;
     }
     else {
    	p.___(bz.list.get__(0));
     }
     bz.___(spl.list.get__(spl.size-1));
     if (bz.eflag!=0) {
UNSUPPORTED("78u9nvs8u7rxturidz5nf8hn4"); // 	*q = bz.ep;
     }
     else {
		q.___(bz.list.get__(bz.size-1));
     }
} finally {
LEAVING("7wyn51o9k6u7joaq9k18boffh","endPoints");
}
}


//3 8hpmwzlqbj1nii32zubbe9hru
// pointf edgeMidpoint (graph_t* g, edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="edgeMidpoint", key="8hpmwzlqbj1nii32zubbe9hru", definition="pointf edgeMidpoint (graph_t* g, edge_t * e)")
public static ST_pointf edgeMidpoint(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("8hpmwzlqbj1nii32zubbe9hru","edgeMidpoint");
try {
	return edgeMidpoint_(g, e).copy();
} finally {
LEAVING("8hpmwzlqbj1nii32zubbe9hru","edgeMidpoint");
}
}
	
	
private static ST_pointf edgeMidpoint_(ST_Agraph_s g, ST_Agedge_s e) {
     int et = (GD_flags(g) & (7 << 1));
     final ST_pointf d = new ST_pointf();
     final ST_pointf spf = new ST_pointf();
     final ST_pointf p = new ST_pointf();
     final ST_pointf q = new ST_pointf();
     endPoints((ST_splines) ED_spl(e), p, q);
     if (APPROXEQPT(p, q, MILLIPOINT)) { /* degenerate spline */
UNSUPPORTED("7i8m5mpfnv7m9uqxh015zfdaj"); // 	spf = p;
     }
     else if ((et == (5 << 1)) || (et == (2 << 1))) {
 	d.x = (q.x + p.x) / 2.;
 	d.y = (p.y + q.y) / 2.;
 	spf.___(dotneato_closest((ST_splines)ED_spl(e), d));
     }
     else {   /* ET_PLINE, ET_ORTHO or ET_LINE */
UNSUPPORTED("6he3hi05vusuthrchn4enk7o6"); // 	spf = polylineMidpoint (ED_spl(e), &p, &q);
     }
     return spf;
}





//3 2tbz9tbkzx8os72qiyhgnby67
// splines *getsplinepoints(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/splines.c", name="", key="2tbz9tbkzx8os72qiyhgnby67", definition="splines *getsplinepoints(edge_t * e)")
public static ST_splines getsplinepoints(ST_Agedge_s e) {
ENTERING("2tbz9tbkzx8os72qiyhgnby67","getsplinepoints");
try {
    ST_Agedge_s le;
    ST_splines sp;
    for (le = e; N(sp = ED_spl(le)) && ED_edge_type(le) != 0;
	 le = ED_to_orig(le));
    if (sp == null) 
UNSUPPORTED("8oq6gemxrb07hmmw0gtux7os5"); // 	agerr (AGERR, "getsplinepoints: no spline points available for edge (%s,%s)\n",
// UNSUPPORTED("bw49w8tpkv5eblsevof4kelef"); // 	    agnameof(agtail(e)), agnameof(aghead(e)));
    return sp;
} finally {
LEAVING("2tbz9tbkzx8os72qiyhgnby67","getsplinepoints");
}
}


}
