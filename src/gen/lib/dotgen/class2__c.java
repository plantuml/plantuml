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
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.utils__c.UF_find;
import static gen.lib.dotgen.cluster__c.build_skeleton;
import static gen.lib.dotgen.cluster__c.mark_clusters;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.fast_node;
import static gen.lib.dotgen.fastgr__c.find_fast_edge;
import static gen.lib.dotgen.fastgr__c.flat_edge;
import static gen.lib.dotgen.fastgr__c.merge_oneway;
import static gen.lib.dotgen.fastgr__c.other_edge;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static gen.lib.dotgen.fastgr__c.virtual_node;
import static gen.lib.dotgen.mincross__c.virtual_weight;
import static gen.lib.dotgen.position__c.ports_eq;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.Macro.CLUSTER;
import static smetana.core.Macro.CLUSTER_EDGE;
import static smetana.core.Macro.ED_conc_opp_flag;
import static smetana.core.Macro.ED_count;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_label_ontop;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_nodesep;
import static smetana.core.Macro.GD_rankleader;
import static smetana.core.Macro.IGNORED;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_weight_class;
import static smetana.core.Macro.agfindedge;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.HasND_Rank;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Todo;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_pointf;
import smetana.core.CArrayOfStar;
import smetana.core.Z;

/* classify edges for mincross/nodepos/splines, using given ranks */
public class class2__c {

//3 2zn7c6ulmwwzaibdxo127jf04
// static node_t* label_vnode(graph_t * g, edge_t * orig) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="label_vnode", key="2zn7c6ulmwwzaibdxo127jf04", definition="static node_t* label_vnode(graph_t * g, edge_t * orig)")
public static ST_Agnode_s label_vnode(ST_Agraph_s g, ST_Agedge_s orig) {
ENTERING("2zn7c6ulmwwzaibdxo127jf04","label_vnode");
try {
    ST_Agnode_s v;
    final ST_pointf dimen = new ST_pointf();
    dimen.___(ED_label(orig).dimen);
    v = virtual_node(g);
    ND_label(v, ED_label(orig));
    ND_lw(v, GD_nodesep(agroot(v)));
    if (N(ED_label_ontop(orig))) {
	if (GD_flip(agroot(g))) {
	    ND_ht(v, dimen.x);
	    ND_rw(v, dimen.y);
	} else {
	    ND_ht(v, dimen.y);
	    ND_rw(v, dimen.x);
	}
    }
    return v;
} finally {
LEAVING("2zn7c6ulmwwzaibdxo127jf04","label_vnode");
}
}




//3 49tji49jdm9uges7v8lf2j2rn
// static void  incr_width(graph_t * g, node_t * v) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="incr_width", key="49tji49jdm9uges7v8lf2j2rn", definition="static void  incr_width(graph_t * g, node_t * v)")
public static void incr_width(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("49tji49jdm9uges7v8lf2j2rn","incr_width");
try {
    int width = GD_nodesep(g) / 2;
    ND_lw(v, ND_lw(v) + width);
    ND_rw(v, ND_rw(v) + width);
} finally {
LEAVING("49tji49jdm9uges7v8lf2j2rn","incr_width");
}
}




//3 xujihq6vep3ez275shtrbilo
// static node_t* plain_vnode(graph_t * g, edge_t * orig) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="plain_vnode", key="xujihq6vep3ez275shtrbilo", definition="static node_t* plain_vnode(graph_t * g, edge_t * orig)")
public static ST_Agnode_s plain_vnode(ST_Agraph_s g, ST_Agedge_s orig) {
ENTERING("xujihq6vep3ez275shtrbilo","plain_vnode");
try {
    ST_Agnode_s v;
    orig = orig;
    v = virtual_node(g);
    incr_width(g, v);
    return v;
} finally {
LEAVING("xujihq6vep3ez275shtrbilo","plain_vnode");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="leader_of", key="9fmfj1b2jik7skv6ms0657t8r", definition="static node_t* leader_of(graph_t * g, node_t * v)")
public static ST_Agnode_s leader_of(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("9fmfj1b2jik7skv6ms0657t8r","leader_of");
try {
    ST_Agraph_s clust;
    ST_Agnode_s rv;
    
    if (ND_ranktype(v) != CLUSTER) {
	/*assert(v == UF_find(v));  could be leaf, so comment out */
	rv = UF_find(v);
    } else {
	clust = ND_clust(v);
	rv = GD_rankleader(clust).get_(ND_rank(v));
    }
    return rv;
} finally {
LEAVING("9fmfj1b2jik7skv6ms0657t8r","leader_of");
}
}




//3 6sbvlvurvkodunw2qt1ug70c2
// static void  make_chain(graph_t * g, node_t * from, node_t * to, edge_t * orig) 
@Unused
@HasND_Rank
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="make_chain", key="6sbvlvurvkodunw2qt1ug70c2", definition="static void  make_chain(graph_t * g, node_t * from, node_t * to, edge_t * orig)")
public static void make_chain(ST_Agraph_s g, ST_Agnode_s from, ST_Agnode_s to, ST_Agedge_s orig) {
ENTERING("6sbvlvurvkodunw2qt1ug70c2","make_chain");
try {
    int r, label_rank;
    ST_Agnode_s u, v=null;
    ST_Agedge_s e;
    u = from;
    if (ED_label(orig)!=null)
	label_rank = (ND_rank(from) + ND_rank(to)) / 2;
    else
	label_rank = -1;
    //assert(ED_to_virt(orig) == NULL);
    for (r = ND_rank(from) + 1; r <= ND_rank(to); r++) {
	if (r < ND_rank(to)) {
	    if (r == label_rank)
		v = label_vnode(g, orig);
	    else
		v = plain_vnode(g, orig);
	    ND_rank(v, r);
	} else
	    v = to;
	e = virtual_edge(u, v, orig);
	virtual_weight(e);
	u = v;
    }
    // assert(ED_to_virt(orig) != NULL);
} finally {
LEAVING("6sbvlvurvkodunw2qt1ug70c2","make_chain");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="interclrep", key="659ld5tcseo3l0hopxb3pf0vv", definition="static void  interclrep(graph_t * g, edge_t * e)")
public static void interclrep(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("659ld5tcseo3l0hopxb3pf0vv","interclrep");
try {
    ST_Agnode_s t, h;
    ST_Agedge_s ve;
    
    t = leader_of(g, agtail(e));
    h = leader_of(g, aghead(e));
    if (ND_rank(t) > ND_rank(h)) {
	ST_Agnode_s t0 = t;
	t = h;
	h = t0;
    }
    if (NEQ(ND_clust(t), ND_clust(h))) {
	if ((ve = find_fast_edge(t, h))!=null) {
	    merge_chain(g, e, ve, true);
	    return;
	}
	if (ND_rank(t) == ND_rank(h))
	    return;
	make_chain(g, t, h, e);
	
	/* mark as cluster edge */
	for (ve = ED_to_virt(e); ve!=null && (ND_rank(aghead(ve)) <= ND_rank(h));
	     ve = (ST_Agedge_s) ND_out(aghead(ve)).list.get_(0))
	    ED_edge_type(ve, CLUSTER_EDGE);
    }
    /* else ignore intra-cluster edges at this point */
} finally {
LEAVING("659ld5tcseo3l0hopxb3pf0vv","interclrep");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="is_cluster_edge", key="c0cx00ki1i1kx0bp84e7xjg8d", definition="static int  is_cluster_edge(edge_t * e)")
public static boolean is_cluster_edge(ST_Agedge_s e) {
ENTERING("c0cx00ki1i1kx0bp84e7xjg8d","is_cluster_edge");
try {
    return ((ND_ranktype(agtail(e)) == CLUSTER)
	    || (ND_ranktype(aghead(e)) == CLUSTER));
} finally {
LEAVING("c0cx00ki1i1kx0bp84e7xjg8d","is_cluster_edge");
}
}




//3 c45973dtaighb3u0auuekcs1y
// void merge_chain(graph_t * g, edge_t * e, edge_t * f, int flag) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="merge_chain", key="c45973dtaighb3u0auuekcs1y", definition="void merge_chain(graph_t * g, edge_t * e, edge_t * f, int flag)")
public static void merge_chain(ST_Agraph_s g, ST_Agedge_s e, ST_Agedge_s f, boolean flag) {
ENTERING("c45973dtaighb3u0auuekcs1y","merge_chain");
try {
    ST_Agedge_s rep;
    int lastrank = MAX(ND_rank(agtail(e)), ND_rank(aghead(e)));
    //assert(ED_to_virt(e) == NULL);
    ED_to_virt(e, f);
    rep = f;
    do {
	/* interclust multi-edges are not counted now */
	if (flag)
	    ED_count(rep, ED_count(rep) + ED_count(e));
	ED_xpenalty(rep, ED_xpenalty(rep) + ED_xpenalty(e));
	ED_weight(rep, ED_weight(rep) + ED_weight(e));
	if (ND_rank(aghead(rep)) == lastrank)
	    break;
	incr_width(g, aghead(rep));
	rep = (ST_Agedge_s) ND_out(aghead(rep)).list.get_(0);
    } while (rep!=null);
} finally {
LEAVING("c45973dtaighb3u0auuekcs1y","merge_chain");
}
}




@Todo(what = "Check label equals ?")
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="mergeable", key="bg5r9wlego0d8pv0hr96zt45c", definition="int mergeable(edge_t * e, edge_t * f)")
public static boolean mergeable(ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("bg5r9wlego0d8pv0hr96zt45c","mergeable");
try {
    if (e!=null && f!=null && EQ(agtail(e), agtail(f)) && EQ(aghead(e), aghead(f)) &&
	EQ(ED_label(e), ED_label(f)) && ports_eq(e, f))
	return true;
    return false;
} finally {
LEAVING("bg5r9wlego0d8pv0hr96zt45c","mergeable");
}
}




//3 d0bxlkysxucmww7t74u9krrgz
@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/class2.c", name="class2", key="d0bxlkysxucmww7t74u9krrgz", definition="void class2(graph_t * g)")
public static void class2(ST_Agraph_s g) {
ENTERING("d0bxlkysxucmww7t74u9krrgz","class2");
try {
    int c;
    ST_Agnode_s n, t, h;
    ST_Agedge_s e, prev, opp;
    
    GD_nlist(g, null);
    
    GD_n_nodes(g, 0);		/* new */
    
    mark_clusters(g);
    for (c = 1; c <= GD_n_cluster(g); c++)
	build_skeleton(g, GD_clust(g).get_(c));
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n))
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
	    if (ND_weight_class(aghead(e)) <= 2)
		ND_weight_class(aghead(e), ND_weight_class(aghead(e)) + 1);
	    if (ND_weight_class(agtail(e)) <= 2)
		ND_weight_class(agtail(e), ND_weight_class(agtail(e)) + 1);
	}
    
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {

	if ((ND_clust(n) == null) && (EQ(n, UF_find(n)))) {
	    fast_node(g, n);
	    GD_n_nodes(g, GD_n_nodes(g) + 1);
	}
	prev = null;
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
		
	    /* already processed */
	    if (ED_to_virt(e)!=null) {
		prev = e;
		continue;
	    }
	    
	    /* edges involving sub-clusters of g */
	    if (is_cluster_edge(e)) {
		/* following is new cluster multi-edge code */
		if (mergeable(prev, e)) {
		    if (ED_to_virt(prev)!=null) {
			merge_chain(g, e, ED_to_virt(prev), false);
			other_edge(e);
		    } else if (ND_rank(agtail(e)) == ND_rank(aghead(e))) {
			merge_oneway(e, prev);
			other_edge(e);
		    }
		    /* else is an intra-cluster edge */
		    continue;
		}
		interclrep(g, e);
		prev = e;
		continue;
	    }
	    /* merge multi-edges */
	    if (prev!=null && EQ(agtail(e), agtail(prev)) && EQ(aghead(e), aghead(prev))) {
		if (ND_rank(agtail(e)) == ND_rank(aghead(e))) {
		    merge_oneway(e, prev);
		    other_edge(e);
		    continue;
		}
		if ((ED_label(e) == null) && (ED_label(prev) == null)
		    && ports_eq(e, prev)) {
		    if (Z.z().Concentrate)
			ED_edge_type(e, IGNORED);
		    else {
			merge_chain(g, e, ED_to_virt(prev), true);
			other_edge(e);
		    }
		    continue;
		}
		/* parallel edges with different labels fall through here */
	    }
	    
	    /* self edges */
	    if (EQ(agtail(e), aghead(e))) {
		other_edge(e);
		prev = e;
		continue;
	    }
	    
	    
	    t = UF_find(agtail(e));
	    h = UF_find(aghead(e));
	    
	    /* non-leader leaf nodes */
	    if (NEQ(agtail(e), t) || NEQ(aghead(e), h)) {
		/* FIX need to merge stuff */
		continue;
	    }
	    
	    /* flat edges */
	    if (ND_rank(agtail(e)) == ND_rank(aghead(e))) {
		flat_edge(g, e);
		prev = e;
		continue;
	    }
	    
	    /* forward edges */
	    if (ND_rank(aghead(e)) > ND_rank(agtail(e))) {
		make_chain(g, agtail(e), aghead(e), e);
		prev = e;
		continue;
	    }
	    /* backward edges */
	    else {
		/*other_edge(e); */
		/* avoid when opp==e in undirected graph */
		if ((opp = agfindedge(g, aghead(e), agtail(e)))!=null && NEQ(aghead(opp), aghead(e))) {
		    /* shadows a forward edge */
		    if (ED_to_virt(opp) == null)
			make_chain(g, agtail(opp), aghead(opp), opp);
		    if ((ED_label(e) == null) && (ED_label(opp) == null)
			&& ports_eq(e, opp)) {
			if (Z.z().Concentrate) {
			    ED_edge_type(e, IGNORED);
			    ED_conc_opp_flag(opp, true);
			} else {	/* see above.  this is getting out of hand */
			    other_edge(e);
			    merge_chain(g, e, ED_to_virt(opp), true);
			}
			continue;
		    }
		}
		make_chain(g, aghead(e), agtail(e), e);
		prev = e;
	    }
	}

    }
    /* since decompose() is not called on subgraphs */
    if (NEQ(g, dot_root(g))) {
    GD_comp(g).list = CArrayOfStar.<ST_Agnode_s>REALLOC(1, GD_comp(g).list, ST_Agnode_s.class);
	GD_comp(g).list.set_(0, GD_nlist(g));
    }
} finally {
LEAVING("d0bxlkysxucmww7t74u9krrgz","class2");
}
}






}
