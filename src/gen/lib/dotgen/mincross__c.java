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
package gen.lib.dotgen;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.graph__c.agnedges;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agcontains;
import static gen.lib.common.utils__c.dequeue;
import static gen.lib.common.utils__c.enqueue;
import static gen.lib.common.utils__c.free_queue;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.new_queue;
import static gen.lib.dotgen.class2__c.class2;
import static gen.lib.dotgen.cluster__c.expand_cluster;
import static gen.lib.dotgen.cluster__c.install_cluster;
import static gen.lib.dotgen.cluster__c.mark_lowclusters;
import static gen.lib.dotgen.decomp__c.decompose;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.delete_flat_edge;
import static gen.lib.dotgen.fastgr__c.flat_edge;
import static gen.lib.dotgen.fastgr__c.merge_oneway;
import static gen.lib.dotgen.fastgr__c.new_virtual_edge;
import static smetana.core.JUtils.atof;
import static smetana.core.JUtils.qsort;
import static smetana.core.JUtils.qsortInt;
import static smetana.core.Macro.ALLOC_INT;
import static smetana.core.Macro.CLUSTER;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.FLATORDER;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_flat_edges;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_rankleader;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_has_port;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_low;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_mval;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_onstack;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_prev;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_weight_class;
import static smetana.core.Macro.NEW_RANK;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.VIRTUAL;
import static smetana.core.Macro.elist_append;
import static smetana.core.Macro.flatindex;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_adjmatrix_t;
import h.ST_elist;
import h.ST_nodequeue;
import h.ST_rank_t;
import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.Memory;
import smetana.core.ZType;

/* 
 * dot_mincross(g) takes a ranked graphs, and finds an ordering
 * that avoids edge crossings.  clusters are expanded.
 * N.B. the rank structure is global (not allocated per cluster)
 * because mincross may compare nodes in different clusters.
 */
public class mincross__c {



/* dot_mincross:
 * Minimize edge crossings
 * Note that nodes are not placed into GD_rank(g) until mincross()
 * is called.
 */
@Unused
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="dot_mincross", key="e876vp4hgkzshluz6qk77cjwk", definition="void dot_mincross(graph_t * g, int doBalance)")
public static void dot_mincross(Globals zz, ST_Agraph_s g, boolean doBalance) {
ENTERING("e876vp4hgkzshluz6qk77cjwk","dot_mincross");
try {
    int c, nc;
    CString s;
    
    init_mincross(zz, g);
    
    for (nc = c = 0; c < GD_comp(g).size; c++) {
	init_mccomp(g, c);
	nc += mincross_(zz, g, 0, 2, doBalance);
    }
    
    merge2(zz, g);
    
    /* run mincross on contents of each cluster */
    for (c = 1; c <= GD_n_cluster(g); c++) {
	nc += mincross_clust(zz, g, GD_clust(g).get_(c), doBalance);
    }
    
    
    
    if ((GD_n_cluster(g) > 0)
	&& (((s = agget(zz, g, new CString("remincross"))))==null || (mapbool(s)))) {
	mark_lowclusters(zz, g);
	zz.ReMincross = true;
	nc = mincross_(zz, g, 2, 2, doBalance);
    }
    cleanup2(zz, g, nc);
} finally {
LEAVING("e876vp4hgkzshluz6qk77cjwk","dot_mincross");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="756bre1tpxb1tq68p7xhkrxkc", definition="static adjmatrix_t *new_matrix(int i, int j)")
public static ST_adjmatrix_t new_matrix(int i, int j) {
ENTERING("756bre1tpxb1tq68p7xhkrxkc","new_matrix");
try {
	ST_adjmatrix_t rv = new ST_adjmatrix_t();
    rv.nrows = i;
    rv.ncols = j;
    // Arnaud 15/09/2022: the j+1 is needed in some case to avoid ArrayIndexOutOfBoundsException
    // Arnaud 02/03/2023: the j+3 is needed in some case to avoid ArrayIndexOutOfBoundsException
    // Arnaud 15/03/2023: the i+3 is needed in some case to avoid ArrayIndexOutOfBoundsException when LR direction
    // Arnaud 20/12/2024: Even bigger
    rv.data = new int[Math.max(i, j) + 8][Math.max(i, j) + 8];
    return rv;
} finally {
LEAVING("756bre1tpxb1tq68p7xhkrxkc","new_matrix");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="free_matrix", key="1n1e0k0wtlbugwm3cb4na6av6", definition="static void free_matrix(adjmatrix_t * p)")
public static void free_matrix(ST_adjmatrix_t p) {
ENTERING("1n1e0k0wtlbugwm3cb4na6av6","free_matrix");
try {
    if (p!=null) {
	Memory.free(p.data);
	Memory.free(p);
    }
} finally {
LEAVING("1n1e0k0wtlbugwm3cb4na6av6","free_matrix");
}
}




@Difficult
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="init_mccomp", key="49vw7fkn99wbojtfksugvuruh", definition="static void init_mccomp(graph_t * g, int c)")
public static void init_mccomp(ST_Agraph_s g, int c) {
ENTERING("49vw7fkn99wbojtfksugvuruh","init_mccomp");
try {
    int r;
    
    GD_nlist(g, GD_comp(g).list.get_(c));
    if (c > 0) {
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    GD_rank(g).get__(r).v = GD_rank(g).get__(r).v.plus_(GD_rank(g).get__(r).n);
	    GD_rank(g).get__(r).n = 0;
	}
    }
} finally {
LEAVING("49vw7fkn99wbojtfksugvuruh","init_mccomp");
}
}




/* ordered_edges:
 * handle case where graph specifies edge ordering
 * If the graph does not have an ordering attribute, we then
 * check for nodes having the attribute.
 * Note that, in this implementation, the value of G_ordering
 * dominates the value of N_ordering.
 */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="ordered_edges", key="hzoz2czb672i0nbjvjhbc3na", definition="static void ordered_edges(graph_t * g)")
public static void ordered_edges(Globals zz, ST_Agraph_s g) {
ENTERING("hzoz2czb672i0nbjvjhbc3na","ordered_edges");
try {
    CString ordering;
    if ((zz.G_ordering) == null && (zz.N_ordering) == null)
	return;
UNSUPPORTED("98iqppixxkdndoz210i5ejppy"); //     if ((ordering = late_string(g, G_ordering, NULL))) {
UNSUPPORTED("lhhw62wj3on36enoy6ug6u1p"); // 	if ((*(ordering)==*("out")&&!strcmp(ordering,"out")))
UNSUPPORTED("ctbadiyd3jjtlt2f5t9p94au7"); // 	    do_ordering(g, NOT(0));
UNSUPPORTED("259y9uhqwc8w6jhrpm1eaa7hf"); // 	else if ((*(ordering)==*("in")&&!strcmp(ordering,"in")))
UNSUPPORTED("312ij4z5d42rye9s30l9io097"); // 	    do_ordering(g, 0);
UNSUPPORTED("25yw5chxmy54pmo88tre1w2ir"); // 	else if (ordering[0])
UNSUPPORTED("5ugg9zy8378pijqru97mxmen5"); // 	    agerr(AGERR, "ordering '%s' not recognized.\n", ordering);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("50itbchatjp61haxvg3xnhvoo"); // 	graph_t *subg;
UNSUPPORTED("evzjoqkm2xukapjvpccu2f23e"); // 	for (subg = agfstsubg(g); subg; subg = agnxtsubg(subg)) {
UNSUPPORTED("1016hqxaexp4j33vmkzvdpq80"); // 	    /* clusters are processed by separate calls to ordered_edges */
UNSUPPORTED("7ah0zlu96u6g6cquxebenj4z3"); // 	    if (!is_cluster(subg))
UNSUPPORTED("7sqzaehh40dvpm11hlxhef6cw"); // 		ordered_edges(subg);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("h2b3ao18r091ie7yk9v20pqq"); // 	if (N_ordering) do_ordering_for_nodes (g);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("hzoz2czb672i0nbjvjhbc3na","ordered_edges");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross_clust", key="crwc5qe7fmrpgcqh1a80toyvo", definition="static int mincross_clust(graph_t * par, graph_t * g, int doBalance)")
public static int mincross_clust(Globals zz, ST_Agraph_s par, ST_Agraph_s g, boolean doBalance) {
ENTERING("crwc5qe7fmrpgcqh1a80toyvo","mincross_clust");
try {
    int c, nc;
    
    expand_cluster(zz, g);
    ordered_edges(zz, g);
    flat_breakcycles(zz, g);
    flat_reorder(zz, g);
    nc = mincross_(zz, g, 2, 2, doBalance);
    
    for (c = 1; c <= GD_n_cluster(g); c++)
	nc += mincross_clust(zz, g, GD_clust(g).get_(c), doBalance);
    
    save_vlist(g);
    return nc;
} finally {
LEAVING("crwc5qe7fmrpgcqh1a80toyvo","mincross_clust");
}
}




//3 657v773m21j5w3g3v94o7464t
// static int left2right(graph_t * g, node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="left2right", key="657v773m21j5w3g3v94o7464t", definition="static int left2right(graph_t * g, node_t * v, node_t * w)")
public static boolean left2right(Globals zz, ST_Agraph_s g, ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("657v773m21j5w3g3v94o7464t","left2right");
try {
	ST_adjmatrix_t M;
    boolean rv=false;
    /* CLUSTER indicates orig nodes of clusters, and vnodes of skeletons */
    if (zz.ReMincross == false) {
	if ((ND_clust(v) != ND_clust(w)) && (ND_clust(v)!=null) && (ND_clust(w)!=null)) {
	    /* the following allows cluster skeletons to be swapped */
	    if ((ND_ranktype(v) == 7)
		&& (ND_node_type(v) == 1))
		return false;
	    if ((ND_ranktype(w) == 7)
		&& (ND_node_type(w) == 1))
		return false;
	    return true;
	    /*return ((ND_ranktype(v) != CLUSTER) && (ND_ranktype(w) != CLUSTER)); */
	}
    } else {
 	if ((ND_clust(v) != ND_clust(w)))
		return true;
    }
    M = GD_rank(g).get__(ND_rank(v)).flat;
    if (M == null)
	rv = false;
    else {
	if (GD_flip(g)) {
	    ST_Agnode_s t = v;
	    v = w;
	    w = t;
	}
	rv = M.data[ND_low(v)][ND_low(w)]!=0;
    }
    return rv;
} finally {
LEAVING("657v773m21j5w3g3v94o7464t","left2right");
}
}




//3 daknncpjy7g5peiicolbmh55i
// static int in_cross(node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="in_cross", key="daknncpjy7g5peiicolbmh55i", definition="static int in_cross(node_t * v, node_t * w)")
public static int in_cross(ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("daknncpjy7g5peiicolbmh55i","in_cross");
try {
    CArrayOfStar<ST_Agedge_s> e2_ = ND_in(w).list;
    int inv, cross = 0, t;
    for (int ie2 = 0; e2_.get_(ie2)!=null; ie2++) {
	int cnt = ED_xpenalty(e2_.get_(ie2));		
	inv = ND_order((agtail(e2_.get_(ie2))));
    CArrayOfStar<ST_Agedge_s> e1_ = ND_in(v).list;
	for (int ie1 = 0; e1_.get_(ie1)!=null; ie1++) {
	    t = ND_order(agtail(e1_.get_(ie1))) - inv;
	    if ((t > 0)
		|| ((t == 0)
		    && (  ED_tail_port(e1_.get_(ie1)).p.x > ED_tail_port(e2_.get_(ie2)).p.x)))
		cross += ED_xpenalty(e1_.get_(ie1)) * cnt;
	}
    }
    return cross;
} finally {
LEAVING("daknncpjy7g5peiicolbmh55i","in_cross");
}
}




//3 b7mf74np8ewrgzwd5u0o8fqod
// static int out_cross(node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="out_cross", key="b7mf74np8ewrgzwd5u0o8fqod", definition="static int out_cross(node_t * v, node_t * w)")
public static int out_cross(ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("b7mf74np8ewrgzwd5u0o8fqod","out_cross");
try {
 	CArrayOfStar<ST_Agedge_s> e2_ = ND_out(w).list;
    int inv, cross = 0, t;
    for (int ie2 = 0; e2_.get_(ie2)!=null; ie2++) {
	int cnt = ED_xpenalty(e2_.get_(ie2));
	inv = ND_order(aghead(e2_.get_(ie2)));
    CArrayOfStar<ST_Agedge_s> e1_ = ND_out(v).list;
	for (int ie1 = 0; e1_.get_(ie1)!=null; ie1++) {
	    t = ND_order(aghead(e1_.get_(ie1))) - inv;
	    if ((t > 0)
		|| ((t == 0)
		    && ((ED_head_port(e1_.get_(ie1))).p.x) > (ED_head_port(e2_.get_(ie2))).p.x))
		cross += ((ED_xpenalty(e1_.get_(ie1))) * cnt);
	}
    }
    return cross;
} finally {
LEAVING("b7mf74np8ewrgzwd5u0o8fqod","out_cross");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="exchange", key="ba4tbr57wips1dzpgxzx3b6ja", definition="static void exchange(node_t * v, node_t * w)")
public static void exchange(Globals zz, ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("ba4tbr57wips1dzpgxzx3b6ja","exchange");
try {
    int vi, wi, r;
    
    r = ND_rank(v);
    vi = ND_order(v);
    wi = ND_order(w);
    ND_order(v, wi);
    GD_rank(zz.Root).get__(r).v.set_(wi, v);
    ND_order(w, vi);
    GD_rank(zz.Root).get__(r).v.set_(vi, w);
} finally {
LEAVING("ba4tbr57wips1dzpgxzx3b6ja","exchange");
}
}





//3 72rj5xs4qh00oh2yi1h5qaadu
// static int balance(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="balance", key="72rj5xs4qh00oh2yi1h5qaadu", definition="static int balance(graph_t * g)")
public static Object balance(Object... arg_) {
UNSUPPORTED("4223t6rekw3qd8eksvz9kjcqh"); // static int balance(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("970giwpms1ljmt0px5djlqs0z"); //     int i, c0, c1, rv;
UNSUPPORTED("8xqnhjfdqnlwlyld1ep6hpnmb"); //     node_t *v, *w;
UNSUPPORTED("p6jnh7nvcpnl3zbz636pskbs"); //     int r;
UNSUPPORTED("yy0fwjrmxw3jbi85mvwrka7f"); //     rv = 0;
UNSUPPORTED("9ey19t6tw2srzaedeng4sq96z"); //     for (r = GD_maxrank(g); r >= GD_minrank(g); r--) {
UNSUPPORTED("2nlemgjbnd1ygry42bcbuwo9y"); // 	GD_rank(g)[r].candidate = 0;
UNSUPPORTED("91gzxbmcoy5xbvf8nlhmlj4eh"); // 	for (i = 0; i < GD_rank(g)[r].n - 1; i++) {
UNSUPPORTED("5ekdr3i8niy7x2ohi7ftgmjke"); // 	    v = GD_rank(g)[r].v[i];
UNSUPPORTED("efwyjm6j3e7byt6z83lrhgd1r"); // 	    w = GD_rank(g)[r].v[i + 1];
UNSUPPORTED("8ey6umhtefcrb61ncgis82hw6"); // 	    assert(ND_order(v) < ND_order(w));
UNSUPPORTED("ddkyq74d5rjfcofczt4hmkxsj"); // 	    if (left2right(g, v, w))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("cdpfb5fmlhe0831jf96pjp9s0"); // 	    c0 = c1 = 0;
UNSUPPORTED("3l676z6ajeugxvie4r957jap3"); // 	    if (r > 0) {
UNSUPPORTED("ennnd5cl9xhh55j2bni58iuxv"); // 		c0 += in_cross(v, w);
UNSUPPORTED("bk4dl2r7nw7j8xvkpg7g7x0z9"); // 		c1 += in_cross(w, v);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("f0ordylgf44hx2l90eof067yo"); // 	    if (GD_rank(g)[r + 1].n > 0) {
UNSUPPORTED("d945airzpo5xbu0fz68ylmgql"); // 		c0 += out_cross(v, w);
UNSUPPORTED("e6ez0uwca50uwo7i7i4vak4f2"); // 		c1 += out_cross(w, v);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("884yfwkgyw57w5ivpfrggmq15"); // 	    if (c1 <= c0) {
UNSUPPORTED("c2sevxb1gtyc2olq8akmvs40u"); // 		balanceNodes(g, r, v, w);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bxwzx4m9ejausu58u7abr6fm0
// static int transpose_step(graph_t * g, int r, int reverse) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="transpose_step", key="bxwzx4m9ejausu58u7abr6fm0", definition="static int transpose_step(graph_t * g, int r, int reverse)")
public static int transpose_step(Globals zz, ST_Agraph_s g, int r, boolean reverse) {
ENTERING("bxwzx4m9ejausu58u7abr6fm0","transpose_step");
try {
    int i, c0, c1, rv;
    ST_Agnode_s v, w;
    rv = 0;
    GD_rank(g).get__(r).candidate= false;
    for (i = 0; i < GD_rank(g).get__(r).n - 1; i++) {
	v = GD_rank(g).get__(r).v.get_(i);
	w = GD_rank(g).get__(r).v.get_(i + 1);
	//assert(ND_order(v) < ND_order(w));
	if (left2right(zz, g, v, w))
	    continue;
	c0 = c1 = 0;
	if (r > 0) {
	    c0 += in_cross(v, w);
	    c1 += in_cross(w, v);
	}
	if (GD_rank(g).get__(r + 1).n > 0) {
	    c0 += out_cross(v, w);
	    c1 += out_cross(w, v);
	}
	if ((c1 < c0) || ((c0 > 0) && reverse && (c1 == c0))) {
	    exchange(zz, v, w);
	    rv += (c0 - c1);
	    GD_rank(zz.Root).get__(r).valid= 0;
	    GD_rank(g).get__(r).candidate= true;
	    if (r > GD_minrank(g)) {
		GD_rank(zz.Root).get__(r - 1).valid= 0;
		GD_rank(g).get__(r - 1).candidate= true;
	    }
	    if (r < GD_maxrank(g)) {
		GD_rank(zz.Root).get__(r + 1).valid= 0;
		GD_rank(g).get__(r + 1).candidate= true;
	    }
	}
    }
    return rv;
} finally {
LEAVING("bxwzx4m9ejausu58u7abr6fm0","transpose_step");
}
}




//3 2i22bxgg5y7v5c5d40k5zppky
// static void transpose(graph_t * g, int reverse) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="transpose", key="2i22bxgg5y7v5c5d40k5zppky", definition="static void transpose(graph_t * g, int reverse)")
public static void transpose(Globals zz, ST_Agraph_s g, boolean reverse) {
ENTERING("2i22bxgg5y7v5c5d40k5zppky","transpose");
try {
    int r, delta;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++)
	GD_rank(g).get__(r).candidate= true;
    do {
	delta = 0;
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    if (GD_rank(g).get__(r).candidate) {
		delta += transpose_step(zz, g, r, reverse);
	    }
	}
	/*} while (delta > ncross(g)*(1.0 - Convergence)); */
    } while (delta >= 1);
} finally {
LEAVING("2i22bxgg5y7v5c5d40k5zppky","transpose");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross", key="7lrk2rxqnwwdau8cx85oqkpmv", definition="static int mincross(graph_t * g, int startpass, int endpass, int doBalance)")
public static int mincross_(Globals zz, ST_Agraph_s g, int startpass, int endpass, boolean doBalance) {
ENTERING("7lrk2rxqnwwdau8cx85oqkpmv","mincross_");
try {
    int maxthispass=0, iter, trying, pass;
    int cur_cross, best_cross;
    
    if (startpass > 1) {
	cur_cross = best_cross = ncross(zz, g);
	save_best(g);
    } else
	cur_cross = best_cross = INT_MAX;
    for (pass = startpass; pass <= endpass; pass++) {
	if (pass <= 1) {
	    maxthispass = Math.min(4, zz.MaxIter);
	    if (g == dot_root(g))
		build_ranks(zz, g, pass);
	    if (pass == 0)
		flat_breakcycles(zz, g);
	    flat_reorder(zz, g);
	    
	    if ((cur_cross = ncross(zz, g)) <= best_cross) {
		save_best(g);
		best_cross = cur_cross;
	    }
	    trying = 0;
	} else {
	    maxthispass = zz.MaxIter;
	    if (cur_cross > best_cross)
		restore_best(zz, g);
	    cur_cross = best_cross;
	}
	trying = 0;
	for (iter = 0; iter < maxthispass; iter++) {
	    /*if (Verbose)
		fprintf(stderr,
			"mincross: pass %d iter %d trying %d cur_cross %d best_cross %d\n",
			pass, iter, trying, cur_cross, best_cross);*/
	    if (trying++ >= zz.MinQuit)
		break;
	    if (cur_cross == 0)
		break;
	    mincross_step(zz, g, iter);
	    if ((cur_cross = ncross(zz, g)) <= best_cross) {
		save_best(g);
		if (cur_cross < zz.Convergence * best_cross)
		    trying = 0;
		best_cross = cur_cross;
	    }
	}
	if (cur_cross == 0)
	    break;
    }
    if (cur_cross > best_cross)
	restore_best(zz, g);
    if (best_cross > 0) {
	transpose(zz, g, false);
	best_cross = ncross(zz, g);
    }
    if (doBalance) {
	for (iter = 0; iter < maxthispass; iter++)
	    balance(g);
    }
    
    return best_cross;
} finally {
LEAVING("7lrk2rxqnwwdau8cx85oqkpmv","mincross_");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="restore_best", key="520049zkz9mafaeklgvm6s8e5", definition="static void restore_best(graph_t * g)")
public static void restore_best(Globals zz, ST_Agraph_s g) {
ENTERING("520049zkz9mafaeklgvm6s8e5","restore_best");
try {
    ST_Agnode_s n;
    int r;
    
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
	ND_order(n, (int)ND_coord(n).x);
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	GD_rank(zz.Root).get__(r).valid= 0;
    qsort(zz, GD_rank(g).get__(r).v,
    	    GD_rank(g).get__(r).n,
    	    mincross__c.nodeposcmpf);
    }
} finally {
LEAVING("520049zkz9mafaeklgvm6s8e5","restore_best");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="save_best", key="8uyqc48j0oul206l3np85wj9p", definition="static void save_best(graph_t * g)")
public static void save_best(ST_Agraph_s g) {
ENTERING("8uyqc48j0oul206l3np85wj9p","save_best");
try {
    ST_Agnode_s n;
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
    (ND_coord(n)).x = ND_order(n);
} finally {
LEAVING("8uyqc48j0oul206l3np85wj9p","save_best");
}
}




/* merges the connected components of g */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="merge_components", key="6d08fwi4dsk6ikk5d0gy6rq2h", definition="static void merge_components(graph_t * g)")
public static void merge_components(Globals zz, ST_Agraph_s g) {
ENTERING("6d08fwi4dsk6ikk5d0gy6rq2h","merge_components");
try {
    int c;
    ST_Agnode_s u, v;
    
    if (GD_comp(g).size <= 1)
	return;
    u = null;
    for (c = 0; c < GD_comp(g).size; c++) {
    v = (ST_Agnode_s) GD_comp(g).list.get_(c);
	if (u!=null)
	    ND_next(u, v);
	ND_prev(v, u);
	while (ND_next(v)!=null) {
	    v = ND_next(v);
	}
	u = v;
    }
    GD_comp(g).size = 1;
    GD_nlist(g, GD_comp(g).list.get_(0));
    GD_minrank(g, zz.GlobalMinRank);
    GD_maxrank(g, zz.GlobalMaxRank);
} finally {
LEAVING("6d08fwi4dsk6ikk5d0gy6rq2h","merge_components");
}
}




/* merge connected components, create globally consistent rank lists */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="merge2", key="91vebcdl3q3y0uyxef0iw71n9", definition="static void merge2(graph_t * g)")
public static void merge2(Globals zz, ST_Agraph_s g) {
ENTERING("91vebcdl3q3y0uyxef0iw71n9","merge2");
try {
    int i, r;
    ST_Agnode_s v;
    
    /* merge the components and rank limits */
    merge_components(zz, g);
    
    /* install complete ranks */
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	GD_rank(g).get__(r).n = GD_rank(g).get__(r).an;
	GD_rank(g).get__(r).v = GD_rank(g).get__(r).av;
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    v = GD_rank(g).get__(r).v.get_(i);
	    if (v == null) {
		/*if (Verbose)
		    fprintf(stderr,
			    "merge2: graph %s, rank %d has only %d < %d nodes\n",
			    agnameof(g), r, i, GD_rank(g)[r].n);*/
		GD_rank(g).get__(r).n = i;
		break;
	    }
	    ND_order(v, i);
	}
    }
} finally {
LEAVING("91vebcdl3q3y0uyxef0iw71n9","merge2");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="cleanup2", key="3cwiyyk1d1jkoo9iqwb5bge4x", definition="static void cleanup2(graph_t * g, int nc)")
public static void cleanup2(Globals zz, ST_Agraph_s g, int nc) {
ENTERING("3cwiyyk1d1jkoo9iqwb5bge4x","cleanup2");
try {
    int i, j, r, c;
    ST_Agnode_s v;
    ST_Agedge_s e;
    
    if (zz.TI_list!=null) {
	Memory.free(zz.TI_list);
	zz.TI_list = null;
    }
    if (zz.TE_list!=null) {
	Memory.free(zz.TE_list);
	zz.TE_list = null;
    }
    /* fix vlists of clusters */
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_reset_vlists(zz, GD_clust(g).get_(c));
    
    /* remove node temporary edges for ordering nodes */
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    v = GD_rank(g).get__(r).v.get_(i);
	    ND_order(v, i);
	    if (ND_flat_out(v).list!=null) {
		for (j = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(j))!=null; j++)
		    if (ED_edge_type(e) == FLATORDER) {
			delete_flat_edge(e);
			Memory.free(e.base.data);
			Memory.free(e);
			j--;
		    }
	    }
	}
	free_matrix(GD_rank(g).get__(r).flat);
    }
    /*if (Verbose)
	fprintf(stderr, "mincross %s: %d crossings, %.2f secs.\n",
		agnameof(g), nc, elapsed_sec());*/
} finally {
LEAVING("3cwiyyk1d1jkoo9iqwb5bge4x","cleanup2");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="arax68kzcf86dr2xu0gp962gq", definition="static node_t *neighbor(node_t * v, int dir)")
public static ST_Agnode_s neighbor(Globals zz, ST_Agnode_s v, int dir) {
ENTERING("arax68kzcf86dr2xu0gp962gq","neighbor");
try {
    ST_Agnode_s rv;
    
    rv = null;
assert(v!=null);
    if (dir < 0) {
	if (ND_order(v) > 0)
	    rv = GD_rank(zz.Root).get__(ND_rank(v)).v.get_(ND_order(v) - 1);
    } else
	rv = GD_rank(zz.Root).get__(ND_rank(v)).v.get_(ND_order(v) + 1);
assert((rv == null) || (ND_order(rv)-ND_order(v))*dir > 0);
    return rv;
} finally {
LEAVING("arax68kzcf86dr2xu0gp962gq","neighbor");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="is_a_normal_node_of", key="1waqm8z71hi389dt1wqh0bmhr", definition="static int is_a_normal_node_of(graph_t * g, node_t * v)")
public static boolean is_a_normal_node_of(Globals zz, ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("1waqm8z71hi389dt1wqh0bmhr","is_a_normal_node_of");
try {
    return ((ND_node_type(v) == NORMAL) && agcontains(zz, g, v));
} finally {
LEAVING("1waqm8z71hi389dt1wqh0bmhr","is_a_normal_node_of");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="is_a_vnode_of_an_edge_of", key="9f8atyi1unmleplge3rijdt4s", definition="static int is_a_vnode_of_an_edge_of(graph_t * g, node_t * v)")
public static boolean is_a_vnode_of_an_edge_of(Globals zz, ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("9f8atyi1unmleplge3rijdt4s","is_a_vnode_of_an_edge_of");
try {
    if ((ND_node_type(v) == VIRTUAL)
	&& (ND_in(v).size == 1) && (ND_out(v).size == 1)) {
	ST_Agedge_s e = (ST_Agedge_s) ND_out(v).list.get_(0);
	while (ED_edge_type(e) != NORMAL)
	    e = ED_to_orig(e);
	if (agcontains(zz, g, e))
	    return true;
    }
    return false;
} finally {
LEAVING("9f8atyi1unmleplge3rijdt4s","is_a_vnode_of_an_edge_of");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="inside_cluster", key="eo7ulc8vwmoaig0j479yapve2", definition="static int inside_cluster(graph_t * g, node_t * v)")
public static boolean inside_cluster(Globals zz, ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("eo7ulc8vwmoaig0j479yapve2","inside_cluster");
try {
    return (is_a_normal_node_of(zz, g, v) | is_a_vnode_of_an_edge_of(zz, g, v));
} finally {
LEAVING("eo7ulc8vwmoaig0j479yapve2","inside_cluster");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="8xkmkt4r6gfqj8gk0mokszoz0", definition="static node_t *furthestnode(graph_t * g, node_t * v, int dir)")
public static ST_Agnode_s furthestnode(Globals zz, ST_Agraph_s g, ST_Agnode_s v, int dir) {
ENTERING("8xkmkt4r6gfqj8gk0mokszoz0","furthestnode");
try {
    ST_Agnode_s u, rv;
    
    rv = u = v;
    while ((u = neighbor(zz, u, dir))!=null) {
	if (is_a_normal_node_of(zz, g, u))
	    rv = u;
	else if (is_a_vnode_of_an_edge_of(zz, g, u))
	    rv = u;
    }
    return rv;
} finally {
LEAVING("8xkmkt4r6gfqj8gk0mokszoz0","furthestnode");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="save_vlist", key="bwmu2hkwud40601oq5vgo2f1h", definition="void save_vlist(graph_t * g)")
public static void save_vlist(ST_Agraph_s g) {
ENTERING("bwmu2hkwud40601oq5vgo2f1h","save_vlist");
try {
    int r;
    
    if (GD_rankleader(g)!=null)
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    GD_rankleader(g).set_(r, GD_rank(g).get__(r).v.get_(0));
	}
} finally {
LEAVING("bwmu2hkwud40601oq5vgo2f1h","save_vlist");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="rec_save_vlists", key="hwdxg97sefkuyd25x2q4pgzg", definition="void rec_save_vlists(graph_t * g)")
public static void rec_save_vlists(ST_Agraph_s g) {
ENTERING("hwdxg97sefkuyd25x2q4pgzg","rec_save_vlists");
try {
    int c;
    
    save_vlist(g);
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_save_vlists(GD_clust(g).get_(c));
} finally {
LEAVING("hwdxg97sefkuyd25x2q4pgzg","rec_save_vlists");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="rec_reset_vlists", key="f3b4wat4uxn5oil720i5mwq4v", definition="void rec_reset_vlists(graph_t * g)")
public static void rec_reset_vlists(Globals zz, ST_Agraph_s g) {
ENTERING("f3b4wat4uxn5oil720i5mwq4v","rec_reset_vlists");
try {
    int r, c;
    ST_Agnode_s u, v, w;
    
    /* fix vlists of sub-clusters */
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_reset_vlists(zz, GD_clust(g).get_(c));
    
    if (GD_rankleader(g)!=null)
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    v = GD_rankleader(g).get_(r);
	    u = furthestnode(zz, g, v, -1);
	    w = furthestnode(zz, g, v, 1);
	    GD_rankleader(g).set_(r, u);
	    GD_rank(g).get__(r).v = GD_rank(dot_root(g)).get__(r).v.plus_(ND_order(u));
	    GD_rank(g).get__(r).n = ND_order(w) - ND_order(u) + 1;
	}
} finally {
LEAVING("f3b4wat4uxn5oil720i5mwq4v","rec_reset_vlists");
}
}




//3 pv0rbbdopo4hmkbl5916qys1
// static Agraph_t* realFillRanks (Agraph_t* g, int rnks[], int rnks_sz, Agraph_t* sg) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="realFillRanks", key="pv0rbbdopo4hmkbl5916qys1", definition="static Agraph_t* realFillRanks (Agraph_t* g, int rnks[], int rnks_sz, Agraph_t* sg)")
public static ST_Agraph_s realFillRanks(ST_Agraph_s g, int[] rnks, int rnks_sz, ST_Agraph_s sg) {
ENTERING("pv0rbbdopo4hmkbl5916qys1","realFillRanks");
try {
 UNSUPPORTED("2o2sf6xi2aumo5k0vglp4ik2y"); // static Agraph_t*
UNSUPPORTED("3pmsfb0uhqmy5u141c932dtn1"); // realFillRanks (Agraph_t* g, int rnks[], int rnks_sz, Agraph_t* sg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("apiu3v3f9zs9yrq8e25nd2rti"); //     int i, c;
UNSUPPORTED("6yramhpyls8c6kexupyqip8oq"); //     Agedge_t* e;
UNSUPPORTED("ci2zh69w6nhi0q816i1ixuy9k"); //     Agnode_t* n;
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("wqxqmv4he79njsw6r7sludv"); // 	sg = realFillRanks (GD_clust(g)[c], rnks, rnks_sz, sg);
UNSUPPORTED("8htor72y55gsejjdeh0e8gdrs"); //     if (dot_root(g) == g)
UNSUPPORTED("bjae04ek2s3o50399zti5a1jh"); // 	return sg;
UNSUPPORTED("bqfhaopbxlwv8h4zfae1lhx6s"); //     memset (rnks, 0, sizeof(int)*rnks_sz);
UNSUPPORTED("7wq24g054kmx3aw25vk5ksj4"); //     for (n = agfstnode(g); n; n = agnxtnode(g,n)) {
UNSUPPORTED("do7v5atqor4gnpwgo18ibgjha"); // 	rnks[ND_rank(n)] = 1;
UNSUPPORTED("9fo44dng5uojukw2kj0z8vvdu"); // 	for (e = agfstout(g,n); e; e = agnxtout(g,e)) {
UNSUPPORTED("emlgvc6yzuboae72y6vxpn8z7"); // 	    for (i = ND_rank(n)+1; i <= ND_rank(aghead(e)); i++) 
UNSUPPORTED("d3fd9x50ix980jjw3old2jrcr"); // 		rnks[i] = 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7gfo0rr0un3w2026kt6ipknty"); //     for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
UNSUPPORTED("6mjvdb94lywg8d7ui6873f18p"); // 	if (rnks[i] == 0) {
UNSUPPORTED("8g0zxrf8w2zly4wln02j3lbnb"); // 	    if (!sg) {
UNSUPPORTED("eqx93butvb7swnxbz9j29winp"); // 		sg = agsubg (dot_root(g), "_new_rank", 1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9h7suegjozc071a939rh9apw1"); // 	    n = agnode (sg, NULL, 1);
UNSUPPORTED("8kclviftszfxkowsmlqq31u8"); // 	    agbindrec(n, "Agnodeinfo_t", sizeof(Agnodeinfo_t), NOT(0));
UNSUPPORTED("8wiry1vcdwy6bzdp98nmxss7c"); // 	    ND_rank(n) = i;
UNSUPPORTED("dy16sxtk3jj6127wavfcdx4yw"); // 	    ND_lw(n) = ND_rw(n) = 0.5;
UNSUPPORTED("6jr3li9af4sp0uxnpnoe8dqh8"); // 	    ND_ht(n) = 1;
UNSUPPORTED("c47fetnb62mx46qzuk9ag3qmu"); // 	    ND_UF_size(n) = 1;
UNSUPPORTED("899cyghezv8oxs0iomu58zncj"); // 	    alloc_elist(4, ND_in(n));
UNSUPPORTED("btfqtg58rqwdh7o4cc0gbaaai"); // 	    alloc_elist(4, ND_out(n));
UNSUPPORTED("9zap4z819hsiogrgmekh15f00"); // 	    agsubnode (g, n, 1);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1jw141gg4omv8r72xcbl5ln0o"); //     return sg;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("pv0rbbdopo4hmkbl5916qys1","realFillRanks");
}
}




//3 1qy9bupreg1pax62owznr98k
// static void fillRanks (Agraph_t* g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="fillRanks", key="1qy9bupreg1pax62owznr98k", definition="static void fillRanks (Agraph_t* g)")
public static void fillRanks(ST_Agraph_s g) {
ENTERING("1qy9bupreg1pax62owznr98k","fillRanks");
try {
    ST_Agraph_s sg;
    int rnks_sz = GD_maxrank(g) + 2;
    int[] rnks = new int[rnks_sz];
    sg = realFillRanks (g, rnks, rnks_sz, null);
    Memory.free (rnks);
} finally {
LEAVING("1qy9bupreg1pax62owznr98k","fillRanks");
}
}




@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="init_mincross", key="7fy4chyk12o7bgp1rv3h27yl3", definition="static void init_mincross(graph_t * g)")
public static void init_mincross(Globals zz, ST_Agraph_s g) {
ENTERING("7fy4chyk12o7bgp1rv3h27yl3","init_mincross");
try {
    int size;
    //if (Verbose)
	//start_timer();
    
    zz.ReMincross = false;
    zz.Root = g;
    /* alloc +1 for the null terminator usage in do_ordering() */
    /* also, the +1 avoids attempts to alloc 0 sizes, something
       that efence complains about */
    size = agnedges(zz, dot_root(g)) + 1;
    zz.TE_list = CArrayOfStar.<ST_Agedge_s>ALLOC(size, ZType.ST_Agedge_s);
    zz.TI_list = new int[size];
    mincross_options(zz, g);
    if ((GD_flags(g) & NEW_RANK)!=0)
	fillRanks (g);
    class2(zz, g);
    decompose(zz, g, 1);
    allocate_ranks(zz, g);
    ordered_edges(zz, g);
    zz.GlobalMinRank = GD_minrank(g);
    zz.GlobalMaxRank = GD_maxrank(g);
} finally {
LEAVING("7fy4chyk12o7bgp1rv3h27yl3","init_mincross");
}
}




//3 6fprrp93vmz0jn3l4ro0iropp
// void flat_rev(Agraph_t * g, Agedge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_rev", key="6fprrp93vmz0jn3l4ro0iropp", definition="void flat_rev(Agraph_t * g, Agedge_t * e)")
public static void flat_rev(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("6fprrp93vmz0jn3l4ro0iropp","flat_rev");
try {
    int j;
    ST_Agedge_s rev;
    if (((ND_flat_out(aghead(e)).list))==null)
	rev = null;
    else
	for (j = 0; (rev = (ST_Agedge_s) ND_flat_out(aghead(e)).list.get_(j))!=null; j++)
	    if (aghead(rev) == agtail(e))
		break;
    if (rev!=null) {
	merge_oneway(e, rev);
	if (ED_to_virt(e) == null)
	    ED_to_virt(e, rev);
	if ((ED_edge_type(rev) == 4)
	    && (ED_to_orig(rev) == null))
	    ED_to_orig(rev, e);
	elist_append(e, ND_other(agtail(e)));
    } else {
	rev = new_virtual_edge(aghead(e), agtail(e), e);
	if (ED_edge_type(e) == 4)
	    ED_edge_type(rev, 4);
	else
	    ED_edge_type(rev, 3);
	ED_label(rev, ED_label(e));
	flat_edge(g, rev);
    }
} finally {
LEAVING("6fprrp93vmz0jn3l4ro0iropp","flat_rev");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_search", key="63ol0ch6cgln1nvl5oiz6n1v0", definition="static void flat_search(graph_t * g, node_t * v)")
public static void flat_search(Globals zz, ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("63ol0ch6cgln1nvl5oiz6n1v0","flat_search");
try {
    int i;
    boolean hascl;
    ST_Agedge_s e;
    ST_adjmatrix_t M = GD_rank(g).get__(ND_rank(v)).flat;
    
    ND_mark(v, true);
    ND_onstack(v, true);
    hascl = (GD_n_cluster(dot_root(g)) > 0);
    if (ND_flat_out(v).list!=null)
	for (i = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(i))!=null; i++) {
	    if (hascl
		&& !(agcontains(zz, g, agtail(e)) && agcontains(zz, g, aghead(e))))
		continue;
	    if (ED_weight(e) == 0)
		continue;
	    if (ND_onstack(aghead(e)) == true) {
		assert(ND_low(aghead(e)) < M.nrows);
		assert(ND_low(agtail(e)) < M.ncols);
		M.data[ND_low(aghead(e))][ND_low(agtail(e))]=1;
		delete_flat_edge(e);
		i--;
		if (ED_edge_type(e) == FLATORDER)
		    continue;
		flat_rev(g, e);
	    } else {
		assert(ND_low(aghead(e)) < M.nrows);
		assert(ND_low(agtail(e)) < M.ncols);
		M.data[ND_low(agtail(e))][ND_low(aghead(e))]=1;
		if (ND_mark(aghead(e)) == 0)
		    flat_search(zz, g, aghead(e));
	    }
	}
    ND_onstack(v, 0);
} finally {
LEAVING("63ol0ch6cgln1nvl5oiz6n1v0","flat_search");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_breakcycles", key="3v5h7z4vqivibvpt913lg8at0", definition="static void flat_breakcycles(graph_t * g)")
public static void flat_breakcycles(Globals zz, ST_Agraph_s g) {
ENTERING("3v5h7z4vqivibvpt913lg8at0","flat_breakcycles");
try {
    int i, r, flat;
    ST_Agnode_s v;
    
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	flat = 0;
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    v = GD_rank(g).get__(r).v.get_(i);
	    ND_mark(v, 0); ND_onstack(v, 0);
	    flatindex(v, i);
	    if ((ND_flat_out(v).size > 0) && (flat == 0)) {
		GD_rank(g).get__(r).flat =
		    new_matrix(GD_rank(g).get__(r).n, GD_rank(g).get__(r).n);
		flat = 1;
	    }
	}
	if (flat!=0) {
	    for (i = 0; i < GD_rank(g).get__(r).n; i++) {
		v = GD_rank(g).get__(r).v.get_(i);
		if (ND_mark(v) == 0)
		    flat_search(zz, g, v);
	    }
	}
    }
} finally {
LEAVING("3v5h7z4vqivibvpt913lg8at0","flat_breakcycles");
}
}



/* allocate_ranks:
 * Allocate rank structure, determining number of nodes per rank.
 * Note that no nodes are put into the structure yet.
 */
@Difficult
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="allocate_ranks", key="d5vb6jiw8mhkaa8gjwn4eqfyn", definition="void allocate_ranks(graph_t * g)")
public static void allocate_ranks(Globals zz, ST_Agraph_s g) {
ENTERING("d5vb6jiw8mhkaa8gjwn4eqfyn","allocate_ranks");
try {
    int r, low, high;
    int cn[];
    ST_Agnode_s n;
    ST_Agedge_s e;
    
	cn = new int[GD_maxrank(g) + 2];
	/* must be 0 based, not GD_minrank */
    for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n)) {
   	cn[ND_rank(n)]++;
	for (e = agfstout(zz, g, n); e!=null; e = agnxtout(zz, g, e)) {
	    low = ND_rank(agtail(e));
	    high = ND_rank(aghead(e));
	    if (low > high) {
		int t = low;
		low = high;
		high = t;
	    }
	    for (r = low + 1; r < high; r++)
			cn[r]++;
	}
    }
    GD_rank(g, CArray.<ST_rank_t>ALLOC__(GD_maxrank(g) + 2, ZType.ST_rank_t));
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	GD_rank(g).get__(r).n = cn[r];
	GD_rank(g).get__(r).an = cn[r];
	CArrayOfStar<ST_Agnode_s> tmp = CArrayOfStar.<ST_Agnode_s>ALLOC(cn[r] + 1, ZType.ST_Agnode_s);
	GD_rank(g).get__(r).v = tmp;
	GD_rank(g).get__(r).av = tmp;
    }
    Memory.free(cn);
} finally {
LEAVING("d5vb6jiw8mhkaa8gjwn4eqfyn","allocate_ranks");
}
}




/* install a node at the current right end of its rank */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="install_in_rank", key="3lxoqxhiri9fgt20zc5jz3aa5", definition="void install_in_rank(graph_t * g, node_t * n)")
public static void install_in_rank(Globals zz, ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("3lxoqxhiri9fgt20zc5jz3aa5","install_in_rank");
try {
    int i, r;
    
    r = ND_rank(n);
    i = GD_rank(g).get__(r).n;
    if (GD_rank(g).get__(r).an <= 0) {
	UNSUPPORTED("8qk1xhvvb994zhv9aq10k4v12"); // 	agerr(AGERR, "install_in_rank, line %d: %s %s rank %d i = %d an = 0\n",
	UNSUPPORTED("53h8d82ax23hys2k21hjswp72"); // 	      1034, agnameof(g), agnameof(n), r, i);
	return;
    }
    
    GD_rank(g).get__(r).v.set_(i, n);
    ND_order(n, i);
    GD_rank(g).get__(r).n++;
    assert(GD_rank(g).get__(r).n <= GD_rank(g).get__(r).an);
    if (ND_order(n) > GD_rank(zz.Root).get__(r).an) {
	UNSUPPORTED("399szcw1txekt1xssyw7s2x07"); // 	agerr(AGERR, "install_in_rank, line %d: ND_order(%s) [%d] > GD_rank(Root)[%d].an [%d]\n",
	UNSUPPORTED("9puojrmsk6vb1qc0jtr8ge4g8"); // 	      1052, agnameof(n), ND_order(n), r, GD_rank(Root)[r].an);
	return;
    }
    if ((r < GD_minrank(g)) || (r > GD_maxrank(g))) {
	UNSUPPORTED("7o1thnqda767wqpe2lh9mj03t"); // 	agerr(AGERR, "install_in_rank, line %d: rank %d not in rank range [%d,%d]\n",
	UNSUPPORTED("d2ugluzf7bmj7osicgitgy3sr"); // 	      1057, r, GD_minrank(g), GD_maxrank(g));
	return;
    }
    if (GD_rank(g).get__(r).v.plus_(ND_order(n)).comparePointer_(
	GD_rank(g).get__(r).av.plus_(GD_rank(zz.Root).get__(r).an))>0) {
	UNSUPPORTED("3eb32nc5czs5auwzz5p5mtl04"); // 	agerr(AGERR, "install_in_rank, line %d: GD_rank(g)[%d].v + ND_order(%s) [%d] > GD_rank(g)[%d].av + GD_rank(Root)[%d].an [%d]\n",
	UNSUPPORTED("3qe3qpw5h6vse39xs1ca9sjmo"); // 	      1062, r, agnameof(n),GD_rank(g)[r].v + ND_order(n), r, r, GD_rank(g)[r].av+GD_rank(Root)[r].an);
	return;
    }
} finally {
LEAVING("3lxoqxhiri9fgt20zc5jz3aa5","install_in_rank");
}
}




/*	install nodes in ranks. the initial ordering ensure that series-parallel
 *	graphs such as trees are drawn with no crossings.  it tries searching
 *	in- and out-edges and takes the better of the two initial orderings.
 */
@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="build_ranks", key="7t49bz6lfcbd9v63ds2x3518z", definition="void build_ranks(graph_t * g, int pass)")
public static void build_ranks(Globals zz, ST_Agraph_s g, int pass) {
ENTERING("7t49bz6lfcbd9v63ds2x3518z","build_ranks");
try {
    int i, j;
    ST_Agnode_s n, n0 = null;
    CArrayOfStar<ST_Agedge_s> otheredges;
    ST_nodequeue q;
    
    q = new_queue(GD_n_nodes(g));
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
	ND_mark(n, 0);
    
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++)
	GD_rank(g).get__(i).n = 0;
    
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	otheredges = ((pass == 0) ? ND_in(n).list : ND_out(n).list);
	if (otheredges.get_(0)!= null)
	    continue;
	if (ND_mark(n) == 0) {
	    ND_mark(n,  1);
	    enqueue(q, n);
	    while ((n0 = dequeue(q))!=null) {
		if (ND_ranktype(n0) != CLUSTER) {
		    install_in_rank(zz, g, n0);
		    enqueue_neighbors(q, n0, pass);
		} else {
		    install_cluster(zz, g, n0, pass, q);
		}
	    }
	}
    }
    if (dequeue(q)!=null)
    UNSUPPORTED("1b3hbd5artrq77i58q2o9kgz3"); // 	agerr(AGERR, "surprise\n");
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
	GD_rank(zz.Root).get__(i).valid = 0;
	if (GD_flip(g) && (GD_rank(g).get__(i).n > 0)) {
	    int nn, ndiv2;
	    CArrayOfStar<ST_Agnode_s> vlist = GD_rank(g).get__(i).v;
	    nn = GD_rank(g).get__(i).n - 1;
	    ndiv2 = nn / 2;
	    for (j = 0; j <= ndiv2; j++)
		exchange(zz, vlist.get_(j), vlist.get_(nn - j));
	}
    }
    
    
    if (g == dot_root(g) && ncross(zz, g) > 0)
	transpose(zz, g, false);
    free_queue(q);
} finally {
LEAVING("7t49bz6lfcbd9v63ds2x3518z","build_ranks");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="enqueue_neighbors", key="bmjlneqxex6a9silzkkidkx6s", definition="void enqueue_neighbors(nodequeue * q, node_t * n0, int pass)")
public static void enqueue_neighbors(ST_nodequeue q, ST_Agnode_s n0, int pass) {
ENTERING("bmjlneqxex6a9silzkkidkx6s","enqueue_neighbors");
try {
    int i;
    ST_Agedge_s e;
    
    if (pass == 0) {
	for (i = 0; i < ND_out(n0).size; i++) {
	    e = (ST_Agedge_s) ND_out(n0).list.get_(i);
	    if ((ND_mark(aghead(e))) == 0) {
		ND_mark(aghead(e), 1);
		enqueue(q, aghead(e));
	    }
	}
    } else {
	for (i = 0; i < ND_in(n0).size; i++) {
	    e = (ST_Agedge_s) ND_in(n0).list.get_(i);
	    if (((ND_mark(agtail(e)))) == 0) {
		ND_mark(agtail(e), 1);
		enqueue(q, agtail(e));
	    }
	}
    }
} finally {
LEAVING("bmjlneqxex6a9silzkkidkx6s","enqueue_neighbors");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="constraining_flat_edge", key="c8nqj0v20api63pikohsbx92u", definition="static int constraining_flat_edge(Agraph_t *g, Agnode_t *v, Agedge_t *e)")
public static boolean constraining_flat_edge(Globals zz, ST_Agraph_s g, ST_Agnode_s v, ST_Agedge_s e) {
ENTERING("c8nqj0v20api63pikohsbx92u","constraining_flat_edge");
try {
	if (ED_weight(e) == 0) return false;
	if (!inside_cluster(zz, g,agtail(e))) return false;
	if (!inside_cluster(zz, g,aghead(e))) return false;
	return true;
} finally {
LEAVING("c8nqj0v20api63pikohsbx92u","constraining_flat_edge");
}
}




/* construct nodes reachable from 'here' in post-order.
* This is the same as doing a topological sort in reverse order.
*/
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="postorder", key="46to0pkk188af2dlkik2ab7e3", definition="static int postorder(graph_t * g, node_t * v, node_t ** list, int r)")
public static int postorder(Globals zz, ST_Agraph_s g, ST_Agnode_s v, CArrayOfStar<ST_Agnode_s> list, int r) {
ENTERING("46to0pkk188af2dlkik2ab7e3","postorder");
try {
    ST_Agedge_s e;
    int i, cnt = 0;
    
    ND_mark(v, true);
    if (ND_flat_out(v).size > 0) {
	for (i = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(i))!=null; i++) {
	    if (!constraining_flat_edge(zz, g,v,e)) continue;
	    if ((ND_mark(aghead(e))) == 0)
		cnt += postorder(zz, g, aghead(e), list.plus_(cnt), r);
	}
    }
    assert(ND_rank(v) == r);
    list.set_(cnt++, v);
    return cnt;
} finally {
LEAVING("46to0pkk188af2dlkik2ab7e3","postorder");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_reorder", key="zuxoswju917kyl08a5f0gtp6", definition="static void flat_reorder(graph_t * g)")
public static void flat_reorder(Globals zz, ST_Agraph_s g) {
ENTERING("zuxoswju917kyl08a5f0gtp6","flat_reorder");
try {
    int i, j, r, pos, n_search, local_in_cnt, local_out_cnt, base_order;
    ST_Agnode_s v, t;
    CArrayOfStar<ST_Agnode_s> left, right;
    CArrayOfStar<ST_Agnode_s> temprank = null;
    ST_Agedge_s flat_e, e;
    
    if (GD_has_flat_edges(g) == 0)
	return;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	if (GD_rank(g).get__(r).n == 0) continue;
	base_order = ND_order(GD_rank(g).get__(r).v.get_(0));
	for (i = 0; i < GD_rank(g).get__(r).n; i++)
	    ND_mark(GD_rank(g).get__(r).v.get_(i), 0);
	temprank = CArrayOfStar.<ST_Agnode_s>REALLOC(i + 1, temprank, ZType.ST_Agnode_s);
	pos = 0;
	
	/* construct reverse topological sort order in temprank */
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    if (GD_flip(g)) v = (ST_Agnode_s) GD_rank(g).get__(r).v.get_(i);
	    else v = GD_rank(g).get__(r).v.get_(GD_rank(g).get__(r).n - i - 1);
	    
	    local_in_cnt = local_out_cnt = 0;
	    for (j = 0; j < ND_flat_in(v).size; j++) {
		flat_e = (ST_Agedge_s) ND_flat_in(v).list.get_(j);
		if (constraining_flat_edge(zz, g,v,flat_e)) local_in_cnt++;
	    }
	    for (j = 0; j < ND_flat_out(v).size; j++) {
		flat_e = (ST_Agedge_s) ND_flat_out(v).list.get_(j);
		if (constraining_flat_edge(zz, g,v,flat_e)) local_out_cnt++;
	    }
	    if ((local_in_cnt == 0) && (local_out_cnt == 0))
		temprank.set_(pos++, v);
	    else {
		if ((ND_mark(v) == 0) && (local_in_cnt == 0)) {
		    left = temprank.plus_(pos);
		    n_search = postorder(zz, g, v, left, r);
		    pos += n_search;
		}
	    }
	}
	
	if (pos!=0) {
	    if (GD_flip(g) == false) {
		left = temprank;
		right = temprank.plus_(pos - 1);
		while (left.comparePointer_(right) < 0) {
		    t = left.get_(0);
		    left.set_(0, right.get_(0));
		    right.set_(0, t);
		    left = left.plus_(1);
		    right = right.plus_(-1);
		}
	    }
	    for (i = 0; i < GD_rank(g).get__(r).n; i++) {
		v = temprank.get_(i);
		GD_rank(g).get__(r).v.set_(i, v);
		ND_order(v, i + base_order);
	    }
	    
	    /* nonconstraint flat edges must be made LR */
	    for (i = 0; i < GD_rank(g).get__(r).n; i++) {
		v = GD_rank(g).get__(r).v.get_(i);
		if (ND_flat_out(v).list!=null) {
		    for (j = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(j))!=null; j++) {
			if ( ((GD_flip(g) == false) && (ND_order(aghead(e)) < ND_order(agtail(e)))) ||
				 ( (GD_flip(g)) && (ND_order(aghead(e)) > ND_order(agtail(e)) ))) {
			    assert(constraining_flat_edge(zz, g,v,e) == false);
			    delete_flat_edge(e);
			    j--;
			    flat_rev(g, e);
			}
		    }
		}
	    }
	    /* postprocess to restore intended order */
	}
	/* else do no harm! */
	GD_rank(zz.Root).get__(r).valid = 0;
    }
    if (temprank!=null)
	Memory.free(temprank);
} finally {
LEAVING("zuxoswju917kyl08a5f0gtp6","flat_reorder");
}
}




@Reviewed(when = "16/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="reorder", key="inv6wazjcnh4xkzzphsdcmg4", definition="static void reorder(graph_t * g, int r, int reverse, int hasfixed)")
public static void reorder(Globals zz, ST_Agraph_s g, int r, boolean reverse, boolean hasfixed) {
ENTERING("inv6wazjcnh4xkzzphsdcmg4","reorder");
try {
    boolean changed = false;
    int nelt;
    boolean muststay, sawclust;
    CArrayOfStar<ST_Agnode_s> vlist = GD_rank(g).get__(r).v;
    CArrayOfStar<ST_Agnode_s> lp, rp=null, ep = vlist.plus_(GD_rank(g).get__(r).n);
    
    for (nelt = GD_rank(g).get__(r).n - 1; nelt >= 0; nelt--) {
	lp = vlist;
	while (lp.comparePointer_(ep)<0) {
	    /* find leftmost node that can be compared */
	    while ((lp.comparePointer_(ep) < 0) && (ND_mval(lp.get_(0)) < 0))
		lp = lp.plus_(1);
	    if (lp.comparePointer_(ep) >= 0)
		break;
	    /* find the node that can be compared */
	    sawclust = muststay = false;
	    for (rp = lp.plus_(1); rp.comparePointer_(ep) < 0; rp=rp.plus_(1)) {
		if (sawclust && ND_clust(rp.get_(0))!=null)
		    continue;	/* ### */
		if (left2right(zz, g, lp.get_(0), rp.get_(0))) {
		    muststay = true;
		    break;
		}
		if (ND_mval(rp.get_(0)) >= 0)
		    break;
		if (ND_clust(rp.get_(0))!=null)
		    sawclust = true;	/* ### */
	    }
	    if (rp.comparePointer_(ep) >= 0)
		break;
	    if (muststay == false) {
		int p1 = (int) (ND_mval(lp.get_(0)));
		int p2 = (int) (ND_mval(rp.get_(0)));
		if ((p1 > p2) || ((p1 == p2) && (reverse))) {
		    exchange(zz, lp.get_(0), rp.get_(0));
		    changed=true;
		}
	    }
	    lp = rp;
	}
	if ((hasfixed == false) && (reverse == false))
	    ep = ep.plus_(-1);
    }
    if (changed) {
	GD_rank(zz.Root).get__(r).valid= 0;
	if (r > 0)
	    GD_rank(zz.Root).get__(r - 1).valid= 0;
    }
} finally {
LEAVING("inv6wazjcnh4xkzzphsdcmg4","reorder");
}
}




//3 14t80owwvm7io4ou6czb9ba9
// static void mincross_step(graph_t * g, int pass) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross_step", key="14t80owwvm7io4ou6czb9ba9", definition="static void mincross_step(graph_t * g, int pass)")
public static void mincross_step(Globals zz, ST_Agraph_s g, int pass) {
ENTERING("14t80owwvm7io4ou6czb9ba9","mincross_step");
try {
    int r, other, first, last, dir;
    boolean hasfixed, reverse;
    if ((pass % 4) < 2)
	reverse = true;
    else
	reverse = false;
    if ((pass % 2)!=0) {
	r = GD_maxrank(g) - 1;
	dir = -1;
    } /* up pass */
    else {
	r = 1;
	dir = 1;
    }				/* down pass */
    if (pass % 2 == 0) {	/* down pass */
	first = GD_minrank(g) + 1;
	if (GD_minrank(g) > GD_minrank(zz.Root))
	    first--;
	last = GD_maxrank(g);
	dir = 1;
    } else {			/* up pass */
	first = GD_maxrank(g) - 1;
	last = GD_minrank(g);
	if (GD_maxrank(g) < GD_maxrank(zz.Root))
	    first++;
	dir = -1;
    }
    for (r = first; r != last + dir; r += dir) {
	other = r - dir;
	hasfixed = medians(zz, g, r, other);
	reorder(zz, g, r, reverse, hasfixed);
    }
    transpose(zz, g, !reverse);
} finally {
LEAVING("14t80owwvm7io4ou6czb9ba9","mincross_step");
}
}




//3 aq18oa4k4grixvfjx7r2qnl6r
@Reviewed(when = "01/12/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="local_cross", key="aq18oa4k4grixvfjx7r2qnl6r", definition="static int local_cross(elist l, int dir)")
public static int local_cross(final ST_elist l, int dir) {
// WARNING!! STRUCT
return local_cross_w_(l.copy(), dir);
}
private static int local_cross_w_(final ST_elist l, int dir) {
ENTERING("aq18oa4k4grixvfjx7r2qnl6r","local_cross");
try {
    int i, j;
    boolean is_out = false;
    int cross = 0;
    ST_Agedge_s e, f;
    if (dir > 0)
	is_out = true;
    else
	is_out = false;
    for (i = 0; (e = l.list.get_(i))!=null; i++) {
	if (is_out)
	    for (j = i + 1; (f = l.list.get_(j))!=null; j++) {
		if ((ND_order(aghead(f)) - ND_order(aghead(e)))
			 * (ED_tail_port(f).p.x - ED_tail_port(e).p.x) < 0)
UNSUPPORTED("bw8rwv11yqzss88pad7ljil8a"); // 		    cross += ED_xpenalty(e) * ED_xpenalty(f);
	} else
	    for (j = i + 1; (f = l.list.get_(j))!=null; j++) {
UNSUPPORTED("bza83c6rmihrkzyllwf0jm4tn"); // 		if ((ND_order(agtail(f)) - ND_order(agtail(e)))
UNSUPPORTED("csrxg0y1azmvde7t833lm13sp"); // 			* (ED_head_port(f).p.x - ED_head_port(e).p.x) < 0)
UNSUPPORTED("bw8rwv11yqzss88pad7ljil8a"); // 		    cross += ED_xpenalty(e) * ED_xpenalty(f);
	    }
    }
    return cross;
} finally {
LEAVING("aq18oa4k4grixvfjx7r2qnl6r","local_cross");
}
}




//static __ptr__ Count;
//static int C;
//3 bk5nklhfqgg0uwkv7tv6dn8r2
// static int rcross(graph_t * g, int r) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="rcross", key="bk5nklhfqgg0uwkv7tv6dn8r2", definition="static int rcross(graph_t * g, int r)")
public static int rcross(Globals zz, ST_Agraph_s g, int r) {
ENTERING("bk5nklhfqgg0uwkv7tv6dn8r2","rcross");
try {
    int top, bot, cross, max, i, k;
    ST_Agnode_s v;
    CArrayOfStar<ST_Agnode_s> rtop;
    
    cross = 0;
    max = 0;
    rtop = GD_rank(g).get__(r).v;
    
    if (zz.C <= GD_rank(zz.Root).get__(r + 1).n) {
	zz.C = GD_rank(zz.Root).get__(r + 1).n + 1;
	zz.Count = ALLOC_INT(zz.C, zz.Count);
    }
    
    for (i = 0; i < GD_rank(g).get__(r + 1).n; i++)
    	zz.Count[i] = 0;
    
    for (top = 0; top < GD_rank(g).get__(r).n; top++) {
	ST_Agedge_s e;
	if (max > 0) {
	    for (i = 0; (e = (ST_Agedge_s) ND_out(rtop.get_(top)).list.get_(i))!=null; i++) {
		for (k = ND_order(aghead(e)) + 1; k <= max; k++)
		    cross += zz.Count[k] * ED_xpenalty(e);
	    }
	}
	for (i = 0; (e = (ST_Agedge_s) ND_out(rtop.get_(top)).list.get_(i))!=null; i++) {
	    int inv = ND_order(aghead(e));
	    if (inv > max)
		max = inv;
	    zz.Count[inv] += ED_xpenalty(e);
	}
    }
    for (top = 0; top < GD_rank(g).get__(r).n; top++) {
	v = (ST_Agnode_s) GD_rank(g).get__(r).v.get_(top);
	if (ND_has_port(v))
	    cross += local_cross(ND_out(v), 1);
    }
    for (bot = 0; bot < GD_rank(g).get__(r + 1).n; bot++) {
	v = (ST_Agnode_s) GD_rank(g).get__(r + 1).v.get_(bot);
	if (ND_has_port(v))
	    cross += local_cross(ND_in(v), -1);
    }
    return cross;
} finally {
LEAVING("bk5nklhfqgg0uwkv7tv6dn8r2","rcross");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="ncross", key="dbjmz2tnii2pn9sxg26ap6w5r", definition="int ncross(graph_t * g)")
public static int ncross(Globals zz, ST_Agraph_s g) {
ENTERING("dbjmz2tnii2pn9sxg26ap6w5r","ncross");
try {
    int r, count, nc;
    
    g = zz.Root;
    count = 0;
    for (r = GD_minrank(g); r < GD_maxrank(g); r++) {
	if (GD_rank(g).get__(r).valid!=0)
	    count += GD_rank(g).get__(r).cache_nc;
	else {
	    nc = rcross(zz, g, r);
	    GD_rank(g).get__(r).cache_nc = nc;
	    count += nc;
	    GD_rank(g).get__(r).valid = 1;
	}
    }
    return count;
} finally {
LEAVING("dbjmz2tnii2pn9sxg26ap6w5r","ncross");
}
}




public static CFunction ordercmpf = new CFunctionAbstract("ordercmpf") {
	
	public Object exe(Globals zz, Object... args) {
		return ordercmpf((Integer)args[0], (Integer)args[1]);
	}};

@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="ordercmpf", key="8wrsq8a2vooekcm3cdtv5x3ke", definition="static int ordercmpf(int *i0, int *i1)")
public static int ordercmpf(Integer i0, Integer i1) {
ENTERING("8wrsq8a2vooekcm3cdtv5x3ke","ordercmpf");
try {
    return (i0) - (i1);
} finally {
LEAVING("8wrsq8a2vooekcm3cdtv5x3ke","ordercmpf");
}
}




//3 7397kynkpqf2m1jkpmi8pgf0n
// static int flat_mval(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_mval", key="7397kynkpqf2m1jkpmi8pgf0n", definition="static int flat_mval(node_t * n)")
public static boolean flat_mval(ST_Agnode_s n) {
ENTERING("7397kynkpqf2m1jkpmi8pgf0n","flat_mval");
try {
    int i;
    ST_Agedge_s e;
    CArrayOfStar<ST_Agedge_s> fl;
    ST_Agnode_s nn;
    if (ND_flat_in(n).size > 0) {
	fl = ND_flat_in(n).list;
	nn = agtail(fl.get_(0));
	for (i = 1; (e = fl.get_(i))!=null; i++)
	    if (ND_order(agtail(e)) > ND_order(nn))
		nn = agtail(e);
	if (ND_mval(nn) >= 0) {
	    ND_mval(n, ND_mval(nn) + 1);
	    return false;
	}
    } else if (ND_flat_out(n).size > 0) {
	fl = ND_flat_out(n).list;
	nn = aghead(fl.get_(0));
	for (i = 1; (e = fl.get_(i))!=null; i++)
	    if (ND_order(aghead(e)) < ND_order(nn))
		nn = aghead(e);
	if (ND_mval(nn) > 0) {
	    ND_mval(n, ND_mval(nn) - 1);
	    return false;
	}
    }
    return true;
} finally {
LEAVING("7397kynkpqf2m1jkpmi8pgf0n","flat_mval");
}
}




//3 azvdpixwwxspl31wp7f4k4fmh
// static boolean medians(graph_t * g, int r0, int r1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="medians", key="azvdpixwwxspl31wp7f4k4fmh", definition="static boolean medians(graph_t * g, int r0, int r1)")
public static boolean medians(Globals zz, ST_Agraph_s g, int r0, int r1) {
ENTERING("azvdpixwwxspl31wp7f4k4fmh","medians");
try {
    int i, j, j0, lm, rm, lspan, rspan;
    int[] list;
    ST_Agnode_s n;
    CArrayOfStar<ST_Agnode_s> v;
    ST_Agedge_s e;
    boolean hasfixed = false;
    list = zz.TI_list;
    v = GD_rank(g).get__(r0).v;
    for (i = 0; i < GD_rank(g).get__(r0).n; i++) {
	n = v.get_(i);
	j = 0;
	if (r1 > r0)
	    for (j0 = 0; (e = ND_out(n).list.get_(j0))!=null; j0++) {
		if (ED_xpenalty(e) > 0)
			list[j++] = ((256 * ND_order(aghead(e)) + (ED_head_port(e)).order));
	} else
	    for (j0 = 0; (e = ND_in(n).list.get_(j0))!=null; j0++) {
		if (ED_xpenalty(e) > 0)
			list[j++] = ((256 * ND_order(agtail(e)) + (ED_tail_port(e)).order));
	    }
	switch (j) {
	case 0:
	    ND_mval(n, -1);
	    break;
	case 1:
	    ND_mval(n, list[0]);
	    break;
	case 2:
	    ND_mval(n, (list[0] + list[1]) / 2);
	    break;
	default:
	    qsortInt(zz, list,
	    	    j,
	    	    mincross__c.ordercmpf);
	    if (j % 2!=0)
		ND_mval(n, list[j / 2]);
	    else {
		/* weighted median */
		rm = j / 2;
		lm = rm - 1;
		rspan = list[j - 1] - list[rm];
		lspan = list[lm] - list[0];
		if (lspan == rspan)
		    ND_mval(n, (list[lm] + list[rm]) / 2);
		else {
		    int w = list[lm] * rspan + list[rm] * lspan;
		    ND_mval(n, w / (lspan + rspan));
		}
	    }
	}
    }
    for (i = 0; i < GD_rank(g).get__(r0).n; i++) {
	n = (ST_Agnode_s) v.get_(i);
	if ((ND_out(n).size == 0) && (ND_in(n).size == 0))
	    hasfixed |= flat_mval(n);
    }
    return hasfixed;
} finally {
LEAVING("azvdpixwwxspl31wp7f4k4fmh","medians");
}
}




public static CFunction nodeposcmpf = new CFunctionAbstract("nodeposcmpf") {
	
	public Object exe(Globals zz, Object... args) {
		return nodeposcmpf((CArrayOfStar<ST_Agnode_s>)args[0], (CArrayOfStar<ST_Agnode_s>)args[1]);
	}};

@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="nodeposcmpf", key="2vdhpcykq508ma83aif8sxcbd", definition="static int nodeposcmpf(node_t ** n0, node_t ** n1)")
public static Object nodeposcmpf(CArrayOfStar<ST_Agnode_s> n0, CArrayOfStar<ST_Agnode_s> n1) {
ENTERING("2vdhpcykq508ma83aif8sxcbd","nodeposcmpf");
try {
    return (ND_order(n0.get_(0)) - ND_order(n1.get_(0)));
} finally {
LEAVING("2vdhpcykq508ma83aif8sxcbd","nodeposcmpf");
}
}



//1 40as9opn4mzq4gp4nkmp4dj8w
// static int table[3][3] = 
private static int table[][] = new int[][]{
    /* ordinary */ {1, 1, 1},
    /* singleton */ {1, 2, 2},
    /* virtual */ {1, 2, 4}
};



//3 7j638prioxd97f74v1v4adbsf
// static int endpoint_class(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="endpoint_class", key="7j638prioxd97f74v1v4adbsf", definition="static int endpoint_class(node_t * n)")
public static int endpoint_class(ST_Agnode_s n) {
ENTERING("7j638prioxd97f74v1v4adbsf","endpoint_class");
try {
    if (ND_node_type(n) == 1)
	return 2;
    if (ND_weight_class(n) <= 1)
	return 1;
    return 0;
} finally {
LEAVING("7j638prioxd97f74v1v4adbsf","endpoint_class");
}
}




//3 es57bn7ga4wc9tqtcixpn0451
// void virtual_weight(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="virtual_weight", key="es57bn7ga4wc9tqtcixpn0451", definition="void virtual_weight(edge_t * e)")
public static void virtual_weight(ST_Agedge_s e) {
ENTERING("es57bn7ga4wc9tqtcixpn0451","virtual_weight");
try {
    int t;
    t = table[endpoint_class(agtail(e))][endpoint_class(aghead(e))];
    ED_weight(e, ED_weight(e) * t);
} finally {
LEAVING("es57bn7ga4wc9tqtcixpn0451","virtual_weight");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross_options", key="7ru09oqbudpeofsthzveig2m2", definition="static void mincross_options(graph_t * g)")
public static void mincross_options(Globals zz, ST_Agraph_s g) {
ENTERING("7ru09oqbudpeofsthzveig2m2","mincross_options");
try {
    CString p;
    double f;
    
    /* set default values */
    zz.MinQuit = 8;
    zz.MaxIter = 24;
    zz.Convergence = .995;
    
    p = agget(zz, g, new CString("mclimit"));
    if (p!=null && ((f = atof(p)) > 0.0)) {
UNSUPPORTED("4iu53eiz077u6joqgwawca8ya"); // 	MinQuit = ((1)>(MinQuit * f)?(1):(MinQuit * f));
UNSUPPORTED("38po81l36cibw6jc3qlsscpcu"); // 	MaxIter = ((1)>(MaxIter * f)?(1):(MaxIter * f));
    }   
} finally {
LEAVING("7ru09oqbudpeofsthzveig2m2","mincross_options");
}
}


}
