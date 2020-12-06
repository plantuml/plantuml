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
package gen.lib.dotgen;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static gen.lib.dotgen.fastgr__c.virtual_node;
import static gen.lib.dotgen.mincross__c.rec_reset_vlists;
import static gen.lib.dotgen.mincross__c.rec_save_vlists;
import static smetana.core.JUtils.EQ;
import static smetana.core.Macro.ED_adjacent;
import static smetana.core.Macro.ED_dist;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.FLATORDER;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_alg;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.VIRTUAL;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.HasND_Rank;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_pointf;
import h.ST_rank_t;
import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.debug.SmetanaDebug;

public class flat__c {






//3 e0gtvsxlvztmwu8yy44wfvf97
// static node_t *make_vn_slot(graph_t * g, int r, int pos) 
@Unused
@HasND_Rank
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="", key="e0gtvsxlvztmwu8yy44wfvf97", definition="static node_t *make_vn_slot(graph_t * g, int r, int pos)")
public static ST_Agnode_s make_vn_slot(ST_Agraph_s g, int r, int pos) {
ENTERING("e0gtvsxlvztmwu8yy44wfvf97","make_vn_slot");
try {
    int i;
    CArrayOfStar<ST_Agnode_s> v;
    
    ST_Agnode_s n;
	v = CArrayOfStar.<ST_Agnode_s>REALLOC(GD_rank(g).get__(r).n + 2, GD_rank(g).get__(r).v, ST_Agnode_s.class);
    GD_rank(g).get__(r).v = v;
    for (i = GD_rank(g).get__(r).n; i > pos; i--) {
	v.set_(i, v.get_(i - 1));
	ND_order(v.get_(i), ND_order(v.get_(i))+1);
    }
    n = virtual_node(g);
    v.set_(pos, n);
    ND_order(n, pos);
    ND_rank(n, r);
    v.set_(++GD_rank(g).get__(r).n, null);
    return v.get_(pos);
} finally {
LEAVING("e0gtvsxlvztmwu8yy44wfvf97","make_vn_slot");
}
}




//3 d64wt9oqphauv3hp4axbg2ep3
// static void findlr(node_t * u, node_t * v, int *lp, int *rp) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="findlr", key="d64wt9oqphauv3hp4axbg2ep3", definition="static void findlr(node_t * u, node_t * v, int *lp, int *rp)")
public static void findlr(ST_Agnode_s u, ST_Agnode_s v, int lp[], int rp[]) {
ENTERING("d64wt9oqphauv3hp4axbg2ep3","findlr");
try {
    int l, r;
    l = ND_order(u);
    r = ND_order(v);
    if (l > r) {
	int t = l;
	l = r;
	r = t;
    }
    lp[0] = l;
    rp[0] = r;
} finally {
LEAVING("d64wt9oqphauv3hp4axbg2ep3","findlr");
}
}




//3 bwjjmaydx5a2fnpeoligkha0r
// static void setbounds(node_t * v, int *bounds, int lpos, int rpos) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="setbounds", key="bwjjmaydx5a2fnpeoligkha0r", definition="static void setbounds(node_t * v, int *bounds, int lpos, int rpos)")
public static void setbounds(ST_Agnode_s v, int bounds[], int lpos[], int rpos[]) {
ENTERING("bwjjmaydx5a2fnpeoligkha0r","setbounds");
try {
    int i, ord;
    int[] l = new int[1], r = new int[1];
    ST_Agedge_s f;
    if (ND_node_type(v) == 1) {
	ord = ND_order(v);
	if (ND_in(v).size == 0) {	/* flat */
	    assert(ND_out(v).size == 2);
	    findlr((ST_Agnode_s) aghead(ND_out(v).list.get_(0)), (ST_Agnode_s) aghead(ND_out(v).list.get_(1)), l,
		   r);
	    /* the other flat edge could be to the left or right */
	    if (r[0] <= lpos[0])
		bounds[2] = bounds[0] = ord;
	    else if (l[0] >= rpos[0])
		bounds[3] = bounds[1] = ord;
	    /* could be spanning this one */
	    else if ((l[0] < lpos[0]) && (r[0] > rpos[0]));	/* ignore */
	    /* must have intersecting ranges */
	    else {
		if ((l[0] < lpos[0]) || ((l[0] == lpos[0]) && (r[0] < rpos[0])))
		    bounds[2] = ord;
		if ((r[0] > rpos[0]) || ((r[0] == rpos[0]) && (l[0] > lpos[0])))
		    bounds[3] = ord;
	    }
	} else {		/* forward */
	    boolean onleft, onright;
	    onleft = onright = false;
	    for (i = 0; (f = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++) {
		if (ND_order(aghead(f)) <= lpos[0]) {
		    onleft = true;
		    continue;
		}
		if (ND_order(aghead(f)) >= rpos[0]) {
		    onright = true;
		    continue;
		}
	    }
	    if (onleft && (onright == false))
		bounds[0] = ord + 1;
	    if (onright && (onleft == false))
		bounds[1] = ord - 1;
	}
    }
} finally {
LEAVING("bwjjmaydx5a2fnpeoligkha0r","setbounds");
}
}


private static final int HLB = 0;	/* hard left bound */
private static final int HRB = 1;	/* hard right bound */
private static final int SLB = 2;	/* soft left bound */
private static final int SRB = 3;	/* soft right bound */


@Reviewed(when = "16/11/2020")
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="flat_limits", key="3bc4otcsxj1dujj49ydbb19oa", definition="static int flat_limits(graph_t * g, edge_t * e)")
public static int flat_limits(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("3bc4otcsxj1dujj49ydbb19oa","flat_limits");
try {
    int lnode, rnode, r, pos;
    int[] lpos = new int[1], rpos = new int[1];
    int bounds[] = new int[4];
    CArrayOfStar<ST_Agnode_s> rank;
    
    r = ND_rank(agtail(e)) - 1;
    if (r<0) {
    	SmetanaDebug.LOG("flat_limits r="+r); // Set xt07
    }
    rank = GD_rank(g).get__(r).v;
    lnode = 0;
    rnode = GD_rank(g).get__(r).n - 1;
    bounds[HLB] = bounds[SLB] = lnode - 1;
    bounds[HRB] = bounds[SRB] = rnode + 1;
    findlr(agtail(e), aghead(e), lpos, rpos);
    while (lnode <= rnode) {
	setbounds(rank.get_(lnode), bounds, lpos, rpos);
	if (lnode != rnode)
	    setbounds(rank.get_(rnode), bounds, lpos, rpos);
	lnode++;
	rnode--;
	if (bounds[HRB] - bounds[HLB] <= 1)
	    break;
    }
    if (bounds[HLB] <= bounds[HRB])
	pos = (bounds[HLB] + bounds[HRB] + 1) / 2;
    else
	pos = (bounds[SLB] + bounds[SRB] + 1) / 2;
    return pos;
} finally {
LEAVING("3bc4otcsxj1dujj49ydbb19oa","flat_limits");
}
}




/* flat_node:
 * Create virtual node representing edge label between
 * actual ends of edge e. 
 * This node is characterized by being virtual and having a non-NULL
 * ND_alg pointing to e.
 */
@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="flat_node", key="4cw9yo9ap8ze1r873v6jat4yc", definition="static void  flat_node(edge_t * e)")
public static void flat_node(ST_Agedge_s e) {
ENTERING("4cw9yo9ap8ze1r873v6jat4yc","flat_node");
try {
    int r, place, ypos, h2;
    ST_Agraph_s g;
    ST_Agnode_s n, vn;
    ST_Agedge_s ve;
    final ST_pointf dimen = new ST_pointf();
    
    SmetanaDebug.LOG("Flat node for "+e.NAME);
    if (ED_label(e) == null)
	return;
    g = dot_root(agtail(e));
    r = ND_rank(agtail(e));
    
    place = flat_limits(g, e);
    /* grab ypos = LL.y of label box before make_vn_slot() */
    if ((n = GD_rank(g).get__(r - 1).v.get_(0))!=null)
	ypos = (int)(ND_coord(n).y - GD_rank(g).get__(r - 1).ht1);
    else {
	n = GD_rank(g).get__(r).v.get_(0);
	ypos = (int)(ND_coord(n).y + GD_rank(g).get__(r).ht2 + GD_ranksep(g));
    }
    vn = make_vn_slot(g, r - 1, place);
    dimen.___(ED_label(e).dimen);
    if (GD_flip(g)) {
	double f = dimen.x;
	dimen.x = dimen.y;
	dimen.y = f;
    }
    ND_ht(vn, dimen.y);
    h2 = (int)(ND_ht(vn) / 2);
    ND_rw(vn, dimen.x / 2);
    ND_lw(vn, dimen.x / 2);
    ND_label(vn, ED_label(e));
    ND_coord(vn).y = ypos + h2;
    ve = virtual_edge(vn, agtail(e), e);	/* was NULL? */
    ED_tail_port(ve).p.x = -ND_lw(vn);
    ED_head_port(ve).p.x = ND_rw(agtail(e));
    ED_edge_type(ve, FLATORDER);
    ve = virtual_edge(vn, aghead(e), e);
    ED_tail_port(ve).p.x = ND_rw(vn);
    ED_head_port(ve).p.x = ND_lw(aghead(e));
    ED_edge_type(ve, FLATORDER);
    /* another assumed symmetry of ht1/ht2 of a label node */
    if (GD_rank(g).get__(r - 1).ht1 < h2)
	GD_rank(g).get__(r - 1).ht1 = h2;
    if (GD_rank(g).get__(r - 1).ht2 < h2)
	GD_rank(g).get__(r - 1).ht2 = h2;
    ND_alg(vn, e);
} finally {
LEAVING("4cw9yo9ap8ze1r873v6jat4yc","flat_node");
}
}




//3 1lopavodoru6ee52snd5l6swd
// static void abomination(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="abomination", key="1lopavodoru6ee52snd5l6swd", definition="static void abomination(graph_t * g)")
public static void abomination(ST_Agraph_s g) {
ENTERING("1lopavodoru6ee52snd5l6swd","abomination");
try {
    int r;
    CArray<ST_rank_t> rptr;
    
    assert(GD_minrank(g) == 0);
    /* 3 = one for new rank, one for sentinel, one for off-by-one */
    r = GD_maxrank(g) + 3;
    
    rptr = CArray.<ST_rank_t> REALLOC__(r, GD_rank(g), ST_rank_t.class);
    GD_rank(g, rptr.plus_(1));
    for (r = GD_maxrank(g); r >= 0; r--)
	GD_rank(g).get__(r).___(GD_rank(g).get__(r - 1));
    GD_rank(g).get__(r).n = 0;
    GD_rank(g).get__(r).an = 0;
    GD_rank(g).get__(r).v = CArrayOfStar.<ST_Agnode_s>ALLOC(2, ST_Agnode_s.class);
    GD_rank(g).get__(r).av = GD_rank(g).get__(r).v;
    GD_rank(g).get__(r).flat = null;
    GD_rank(g).get__(r).ht1 = 1;
    GD_rank(g).get__(r).ht2 = 1;
    GD_rank(g).get__(r).pht1 = 1;
    GD_rank(g).get__(r).pht2 = 1;
    GD_minrank(g, GD_minrank(g)-1);
} finally {
LEAVING("1lopavodoru6ee52snd5l6swd","abomination");
}
}




/* checkFlatAdjacent:
 * Check if tn and hn are adjacent. 
 * If so, set adjacent bit on all related edges.
 * Assume e is flat.
 */
@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="checkFlatAdjacent", key="ctujx6e8k3rzv08h6gswdcaqs", definition="static void checkFlatAdjacent (edge_t* e)")
public static void checkFlatAdjacent(ST_Agedge_s e) {
ENTERING("ctujx6e8k3rzv08h6gswdcaqs","checkFlatAdjacent");
try {
	SmetanaDebug.LOG("checkFlatAdjacent "+e.NAME);
    ST_Agnode_s tn = agtail(e);
    ST_Agnode_s hn = aghead(e);
    int i, lo, hi;
    ST_Agnode_s n;
    CArray<ST_rank_t> rank;
    
    if (ND_order(tn) < ND_order(hn)) {
	lo = ND_order(tn);
	hi = ND_order(hn);
    }
    else {
	lo = ND_order(hn);
	hi = ND_order(tn);
    }
    rank = GD_rank(dot_root(tn)).plus_(ND_rank(tn));
    for (i = lo + 1; i < hi; i++) {
	n = rank.get__(0).v.get_(i);
	if ((ND_node_type(n) == VIRTUAL && ND_label(n)!=null) || 
             ND_node_type(n) == NORMAL)
	    break;
    }
    if (i == hi) {  /* adjacent edge */
	do {
	    ED_adjacent(e, 1);
	    e = ED_to_virt(e);
	} while (e!=null); 
    }
} finally {
LEAVING("ctujx6e8k3rzv08h6gswdcaqs","checkFlatAdjacent");
}
}




/* flat_edges:
 * Process flat edges.
 * First, mark flat edges as having adjacent endpoints or not.
 *
 * Second, if there are edge labels, nodes are placed on ranks 0,2,4,...
 * If we have a labeled flat edge on rank 0, add a rank -1.
 *
 * Finally, create label information. Add a virtual label node in the 
 * previous rank for each labeled, non-adjacent flat edge. If this is 
 * done for any edge, return true, so that main code will reset y coords.
 * For labeled adjacent flat edges, store label width in representative edge.
 * FIX: We should take into account any extra height needed for the latter
 * labels.
 * 
 * We leave equivalent flat edges in ND_other. Their ED_virt field should
 * still point to the class representative.
 */
@Reviewed(when = "16/11/2020")
@Original(version="2.38.0", path="lib/dotgen/flat.c", name="flat_edges", key="bjwwj6ftkm0gv04cf1edqeaw6", definition="int  flat_edges(graph_t * g)")
public static boolean flat_edges(ST_Agraph_s g) {
ENTERING("bjwwj6ftkm0gv04cf1edqeaw6","flat_edges");
try {
    int i, j; boolean reset = false;
    ST_Agnode_s n;
    ST_Agedge_s e;
    boolean found = false;
    
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if (ND_flat_out(n).list!=null) {
	    for (j = 0; (e = ND_flat_out(n).list.get_(j))!=null; j++) {
		checkFlatAdjacent (e);
	    }
	}
	for (j = 0; j < ND_other(n).size; j++) {
	    e = ND_other(n).list.get_(j);
	    if (ND_rank(aghead(e)) == ND_rank(agtail(e)))
		checkFlatAdjacent (e);
	}
    }
    
    if ((GD_rank(g).get__(0).flat!=null) || (GD_n_cluster(g) > 0)) {
	for (i = 0; (n = GD_rank(g).get__(0).v.get_(i))!=null; i++) {
	    for (j = 0; (e = ND_flat_in(n).list.get_(j))!=null; j++) {
		if ((ED_label(e)!=null) && N(ED_adjacent(e))) {
		    abomination(g);
		    found = true;
		    break;
		}
	    }
	    if (found)
		break;
	}
    }
    
    rec_save_vlists(g);
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
          /* if n is the tail of any flat edge, one will be in flat_out */
	if (ND_flat_out(n).list!=null) {
	    for (i = 0; (e = ND_flat_out(n).list.get_(i))!=null; i++) {
		if (ED_label(e)!=null) {
			SmetanaDebug.LOG("Aie1 for "+e.NAME+" "+ED_adjacent(e));
		    if (ED_adjacent(e)!=0) {
			if (GD_flip(g)) ED_dist(e, ED_label(e).dimen.y);
			else ED_dist(e, ED_label(e).dimen.x); 
		    }
		    else {
		    	SmetanaDebug.LOG("reset1 true");
			reset = true;
			flat_node(e);
		    }
		}
	    }
		/* look for other flat edges with labels */
	    for (j = 0; j < ND_other(n).size; j++) {
		ST_Agedge_s le;
		e = ND_other(n).list.get_(j);
		SmetanaDebug.LOG("e="+e.NAME);
		if (ND_rank(agtail(e)) != ND_rank(aghead(e))) continue;
		if (EQ(agtail(e), aghead(e))) continue; /* skip loops */
		le = e;
		while (ED_to_virt(le)!=null) le = ED_to_virt(le);
		ED_adjacent(e, ED_adjacent(le)); 
		if (ED_label(e)!=null) {
			SmetanaDebug.LOG("Aie2 for "+e.NAME+" "+ED_adjacent(e));
			SmetanaDebug.LOG("e2="+le.NAME);
			SmetanaDebug.LOG("le2="+le.NAME);
		    if (ED_adjacent(e)!=0) {
			double lw;
			if (GD_flip(g)) lw = ED_label(e).dimen.y;
			else lw = ED_label(e).dimen.x; 
			ED_dist(le, MAX(lw,ED_dist(le)));
		    }
		    else {
		    	SmetanaDebug.LOG("reset2 true");
			reset = true;
			flat_node(e);
		    }
		}
	    }
	}
    }
    if (reset)
	rec_reset_vlists(g);
    return reset;
} finally {
LEAVING("bjwwj6ftkm0gv04cf1edqeaw6","flat_edges");
}
}


}
