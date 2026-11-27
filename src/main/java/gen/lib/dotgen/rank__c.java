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
 * This translation is distributed under the same License as the original C program:
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
import static gen.lib.cgraph.edge__c.agsubedge;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agcontains;
import static gen.lib.cgraph.obj__c.agdelete;
import static gen.lib.cgraph.subg__c.agfstsubg;
import static gen.lib.cgraph.subg__c.agnxtsubg;
import static gen.lib.common.input__c.do_graph_label;
import static gen.lib.common.ns__c.rank;
import static gen.lib.common.utils__c.UF_find;
import static gen.lib.common.utils__c.UF_singleton;
import static gen.lib.common.utils__c.UF_union;
import static gen.lib.common.utils__c.maptoken;
import static gen.lib.dotgen.acyclic__c.acyclic_;
import static gen.lib.dotgen.acyclic__c.reverse_edge;
import static gen.lib.dotgen.aspect__c.rank3;
import static gen.lib.dotgen.class1__c.class1_;
import static gen.lib.dotgen.decomp__c.decompose;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static smetana.core.JUtils.strncmp;
import static smetana.core.Macro.CLUSTER;
import static smetana.core.Macro.EDGE_LABEL;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_leader;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_maxset;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_minset;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_parent;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.GD_set_type;
import static smetana.core.Macro.LEAFSET;
import static smetana.core.Macro.LOCAL;
import static smetana.core.Macro.MAXRANK;
import static smetana.core.Macro.MAXSHORT;
import static smetana.core.Macro.MINRANK;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.NEW_RANK;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.SAMERANK;
import static smetana.core.Macro.SINKRANK;
import static smetana.core.Macro.SOURCERANK;
import static smetana.core.Macro.UNSUPPORTED;
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
import h.ST_aspect_t;
import h.ST_elist;
import h.ST_point;
import smetana.core.CArrayOfStar;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.Memory;
import smetana.core.ZType;

public class rank__c {


	
	
	
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="renewlist", key="3f1re3nfkhxwjjb90kppwuupr", definition="static void  renewlist(elist * L)")
public static void renewlist(ST_elist L) {
ENTERING("3f1re3nfkhxwjjb90kppwuupr","renewlist");
try {
    int i;
    for (i = L.size; i >= 0; i--)
	L.list.set_(i, null);
    L.size = 0;
} finally {
LEAVING("3f1re3nfkhxwjjb90kppwuupr","renewlist");
}
}




@Difficult
@Reviewed(when = "14/11/2020")
@Todo(what = "check why GD_comp(g).resetList comes from GD_comp(g).list = NULL")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="cleanup1", key="1xov2qhuxj1f9nbzu3xsa6679", definition="static void  cleanup1(graph_t * g)")
public static void cleanup1(Globals zz, ST_Agraph_s g) {
ENTERING("1xov2qhuxj1f9nbzu3xsa6679","cleanup1");
try {
    ST_Agnode_s n;
    ST_Agedge_s e, f;
    int c;
    
    for (c = 0; c < GD_comp(g).size; c++) {
    GD_nlist(g, GD_comp(g).list.get_(c));
	for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	    renewlist(ND_in(n));
	    renewlist(ND_out(n));
	    ND_mark(n, 0);
	}
    }
    for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n)) {
	for (e = agfstout(zz, g, n); e!=null; e = agnxtout(zz, g, e)) {
	    f = ED_to_virt(e);
	    /* Null out any other references to f to make sure we don't 
	     * handle it a second time. For example, parallel multiedges 
	     * share a virtual edge.
	     */
	    if (f!=null && (e == ED_to_orig(f))) {
		ST_Agedge_s e1, f1;
		ST_Agnode_s n1;
		for (n1 = agfstnode(zz, g); n1!=null; n1 = agnxtnode(zz, g, n1)) {
		    for (e1 = agfstout(zz, g, n1); e1!=null; e1 = agnxtout(zz, g, e1)) {
			if ((e != e1)) {
			    f1 = ED_to_virt(e1);
			    if (f1!=null && f == f1) {
				ED_to_virt(e1, null);
			    }
			}
		    }
		}
		Memory.free(f.base.data);
		Memory.free(f);
	    }
	    ED_to_virt(e, null);
	}
    }
    Memory.free(GD_comp(g).list);
    GD_comp(g).list = null;
    GD_comp(g).size = 0;
} finally {
LEAVING("1xov2qhuxj1f9nbzu3xsa6679","cleanup1");
}
}



/* When there are edge labels, extra ranks are reserved here for the virtual
 * nodes of the labels.  This is done by doubling the input edge lengths.
 * The input rank separation is adjusted to compensate.
 */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="edgelabel_ranks", key="bxjf5g7g953ii1hfodl1j0y4u", definition="static void  edgelabel_ranks(graph_t * g)")
public static void edgelabel_ranks(Globals zz, ST_Agraph_s g) {
ENTERING("bxjf5g7g953ii1hfodl1j0y4u","edgelabel_ranks");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    
    if ((GD_has_labels(g) & EDGE_LABEL)!=0) {
	for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n))
	    for (e = agfstout(zz, g, n); e!=null; e = agnxtout(zz, g, e))
		ED_minlen(e, ED_minlen(e) * 2);
	GD_ranksep(g, (GD_ranksep(g) + 1) / 2);
    }
} finally {
LEAVING("bxjf5g7g953ii1hfodl1j0y4u","edgelabel_ranks");
}
}




//3 9kjpoxcxoy3nhqd9rflwclo7c
// static void  collapse_rankset(graph_t * g, graph_t * subg, int kind) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="collapse_rankset", key="9kjpoxcxoy3nhqd9rflwclo7c", definition="static void  collapse_rankset(graph_t * g, graph_t * subg, int kind)")
public static void collapse_rankset(Globals zz, ST_Agraph_s g, ST_Agraph_s subg, int kind) {
ENTERING("9kjpoxcxoy3nhqd9rflwclo7c","collapse_rankset");
try {
    ST_Agnode_s u, v; //     node_t *u, *v;
    u = v = agfstnode(zz, subg); //     u = v = agfstnode(subg);
    if (u!=null) { //     if (u) {
	ND_ranktype(u, kind); // 	ND_ranktype(u) = kind;
	while ((v = agnxtnode(zz, subg, v))!=null) { // 	while ((v = agnxtnode(subg, v))) {
	    UF_union(u, v); // 	    UF_union(u, v);
	    ND_ranktype(v, ND_ranktype(u)); // 	    ND_ranktype(v) = ND_ranktype(u);
	} // 	}
	switch (kind) { // 	switch (kind) {
	case MINRANK: // 	case 2:
	case SOURCERANK: // 	case 3:
	    if (GD_minset(g) == null) // 	    if (GD_minset(g) == NULL)
		GD_minset(g, u); // 		GD_minset(g) = u;
	    else // 	    else
		GD_minset(g, UF_union((ST_Agnode_s) GD_minset(g), u)); // 		GD_minset(g) = UF_union(GD_minset(g), u);
	    break; // 	    break;
	case MAXRANK: // 	case 4:
	case SINKRANK: // 	case 5:
	    if (GD_maxset(g) == null) // 	    if (GD_maxset(g) == NULL)
		GD_maxset(g, u); // 		GD_maxset(g) = u;
	    else // 	    else
		GD_maxset(g, UF_union((ST_Agnode_s) GD_maxset(g), u)); // 		GD_maxset(g) = UF_union(GD_maxset(g), u);
	    break; // 	    break;
	} // 	}
	switch (kind) { // 	switch (kind) {
	case SOURCERANK: // 	case 3:
	    ND_ranktype((ST_Agnode_s) GD_minset(g), kind); // 	    ND_ranktype(GD_minset(g)) = kind;
	    break; // 	    break;
	case SINKRANK: // 	case 5:
	    ND_ranktype((ST_Agnode_s) GD_maxset(g), kind); // 	    ND_ranktype(GD_maxset(g)) = kind;
	    break; // 	    break;
	} // 	}
    } //     }
} finally {
LEAVING("9kjpoxcxoy3nhqd9rflwclo7c","collapse_rankset");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="rank_set_class", key="65qi5f0bxp6d6vahhlcolpk88", definition="static int  rank_set_class(graph_t * g)")
public static int rank_set_class(Globals zz, ST_Agraph_s g) {
ENTERING("65qi5f0bxp6d6vahhlcolpk88","rank_set_class");
try {
    CString name[] = new CString[] { new CString("same"), new CString("min"), new CString("source"), new CString("max"), new CString("sink"), null };
    int class_[] = new int[] 
    { SAMERANK, MINRANK, SOURCERANK, MAXRANK, SINKRANK, 0 };
    int val;
    
    if (is_cluster(zz, g))
	return CLUSTER;
    val = maptoken(agget(zz, g, new CString("rank")), name, class_);
    GD_set_type(g, val);
    return val;
} finally {
LEAVING("65qi5f0bxp6d6vahhlcolpk88","rank_set_class");
}
}



@Difficult
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="make_new_cluster", key="5189iviqj57iztftckz86y6jj", definition="static int  make_new_cluster(graph_t * g, graph_t * subg)")
public static int make_new_cluster(Globals zz, ST_Agraph_s g, ST_Agraph_s subg) {
ENTERING("5189iviqj57iztftckz86y6jj","make_new_cluster");
try {
    int cno;
    GD_n_cluster(g, GD_n_cluster(g)+1);
    cno = GD_n_cluster(g);
    GD_clust(g, CArrayOfStar.<ST_Agraph_s>REALLOC(cno + 1, GD_clust(g), ZType.ST_Agraph_s));
    GD_clust(g).set_(cno, subg);
    do_graph_label(zz, subg);
    return cno;
} finally {
LEAVING("5189iviqj57iztftckz86y6jj","make_new_cluster");
}
}



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="node_induce", key="9lvm2ufqjzl2bsbpo0zg9go58", definition="static void  node_induce(graph_t * par, graph_t * g)")
public static void node_induce(Globals zz, ST_Agraph_s par, ST_Agraph_s g) {
ENTERING("9lvm2ufqjzl2bsbpo0zg9go58","node_induce");
try {
    ST_Agnode_s n, nn;
    ST_Agedge_s e;
    int i;

    /* enforce that a node is in at most one cluster at this level */
    for (n = agfstnode(zz, g); n!=null; n = nn) {
	nn = agnxtnode(zz, g, n);
	if (ND_ranktype(n)!=0) {
	    agdelete(g, n);
	    continue;
	}
	for (i = 1; i < GD_n_cluster(par); i++)
	    if (agcontains(zz, GD_clust(par).get_(i), n))
		break;
	if (i < GD_n_cluster(par))
	    agdelete(g, n);
	ND_clust(n, null);
    }
    
    for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n)) {
	for (e = agfstout(zz, dot_root(g), n); e!=null; e = agnxtout(zz, dot_root(g), e)) {
	    if (agcontains(zz, g, aghead(e)))
		agsubedge(zz, g,e,true);
	}
    }
} finally {
LEAVING("9lvm2ufqjzl2bsbpo0zg9go58","node_induce");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="cluster_leader", key="2rbs5deyvlh5s7lkhv6zouqbe", definition="static void cluster_leader(graph_t * clust)")
public static void cluster_leader(Globals zz, ST_Agraph_s clust) {
ENTERING("2rbs5deyvlh5s7lkhv6zouqbe","cluster_leader");
try {
    ST_Agnode_s leader, n;
    int maxrank = 0;
    
    /* find number of ranks and select a leader */
    leader = null;
    for (n = GD_nlist(clust); n!=null; n = ND_next(n)) {
	if ((ND_rank(n) == 0) && (ND_node_type(n) == NORMAL))
	    leader = n;
	if (maxrank < ND_rank(n))
	    maxrank = ND_rank(n);
    }
    assert(leader != null);
    GD_leader(clust, leader);
    
    for (n = agfstnode(zz, clust); n!=null; n = agnxtnode(zz, clust, n)) {
	//assert((ND_UF_size(n) <= 1) || (n == leader));
	UF_union(n, leader);
	ND_ranktype(n, CLUSTER);
    }
} finally {
LEAVING("2rbs5deyvlh5s7lkhv6zouqbe","cluster_leader");
}
}



/*
 * A cluster is collapsed in three steps.
 * 1) The nodes of the cluster are ranked locally.
 * 2) The cluster is collapsed into one node on the least rank.
 * 3) In class1(), any inter-cluster edges are converted using
 *    the "virtual node + 2 edges" trick.
 */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="collapse_cluster", key="f3sl627dqmre3kru883bpdxc3", definition="static void  collapse_cluster(graph_t * g, graph_t * subg)")
public static void collapse_cluster(Globals zz, ST_Agraph_s g, ST_Agraph_s subg) {
ENTERING("f3sl627dqmre3kru883bpdxc3","collapse_cluster");
try {
    if (GD_parent(subg)!=null) {
	return;
    }
    GD_parent(subg, g);
    node_induce(zz, g, subg);
    if (agfstnode(zz, subg) == null)
	return;
    make_new_cluster(zz, g, subg);
    if (zz.CL_type == LOCAL) {
	dot1_rank(zz, subg, null);
	cluster_leader(zz, subg);
    } else
    UNSUPPORTED("1os84mtyrb110i4sd8bdjrwk"); // 	dot_scan_ranks(subg);
    
    
} finally {
LEAVING("f3sl627dqmre3kru883bpdxc3","collapse_cluster");
}
}



/* Execute union commands for "same rank" subgraphs and clusters. */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="collapse_sets", key="din4qnipewrwnelaimzvlplft", definition="static void  collapse_sets(graph_t *rg, graph_t *g)")
public static void collapse_sets(Globals zz, ST_Agraph_s rg, ST_Agraph_s g) {
ENTERING("din4qnipewrwnelaimzvlplft","collapse_sets");
try {
    int c;
    ST_Agraph_s  subg;
    
    for (subg = agfstsubg(zz, g); subg!=null; subg = agnxtsubg(zz, subg)) {
	c = rank_set_class(zz, subg);
	if (c!=0) {
	    if ((c == CLUSTER) && zz.CL_type == LOCAL)
		collapse_cluster(zz, rg, subg);
	    else
		collapse_rankset(zz, rg, subg, c);
	}
	else collapse_sets(zz, rg, subg);
	
	
	
    }
} finally {
LEAVING("din4qnipewrwnelaimzvlplft","collapse_sets");
}
}




//3 5n9mgh7vlru5mb1j9oienvbvs
// static void  find_clusters(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="find_clusters", key="5n9mgh7vlru5mb1j9oienvbvs", definition="static void  find_clusters(graph_t * g)")
public static Object find_clusters(Object... arg_) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("cdsgmo50taekqgk95mfn25930"); // find_clusters(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8uujemixuhlf040icq3zsh7j8"); //     graph_t *subg;
UNSUPPORTED("39msf2samfrjyh2h1a0nh0bnq"); //     for (subg = agfstsubg(dot_root(g)); subg; subg = agnxtsubg(subg)) {
UNSUPPORTED("zmexivcsx1b4oppz6cjwhzd9"); // 	if (GD_set_type(subg) == 7)
UNSUPPORTED("xqwyd1xyo86onxfw4s7p8at4"); // 	    collapse_cluster(g, subg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="set_minmax", key="12fw0esv4unfin6waf9mknc1o", definition="static void  set_minmax(graph_t * g)")
public static void set_minmax(ST_Agraph_s g) {
ENTERING("12fw0esv4unfin6waf9mknc1o","set_minmax");
try {
    int c;
    
    GD_minrank(g, GD_minrank(g) + ND_rank(GD_leader(g)));
    GD_maxrank(g, GD_maxrank(g) + ND_rank(GD_leader(g)));
    for (c = 1; c <= GD_n_cluster(g); c++)
	set_minmax(GD_clust(g).get_(c));
} finally {
LEAVING("12fw0esv4unfin6waf9mknc1o","set_minmax");
}
}



/* To ensure that min and max rank nodes always have the intended rank
 * assignment, reverse any incompatible edges.
 */
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="minmax_edges", key="3bcr1748gqnu8ogb73jeja7ly", definition="static point  minmax_edges(graph_t * g)")
public static ST_point minmax_edges(ST_Agraph_s g) {
// WARNING!! STRUCT
return (ST_point) minmax_edges_w_(g).copy();
}
private static ST_point minmax_edges_w_(ST_Agraph_s g) {
ENTERING("3bcr1748gqnu8ogb73jeja7ly","minmax_edges");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    final ST_point slen = new ST_point();
    
    slen.x = slen.y = 0;
    if ((GD_maxset(g) == null) && (GD_minset(g) == null))
	return slen;
    if (GD_minset(g) != null) //     if (GD_minset(g) != NULL)
	GD_minset(g, UF_find((ST_Agnode_s) GD_minset(g))); // 	GD_minset(g) = UF_find(GD_minset(g));
    if (GD_maxset(g) != null) //     if (GD_maxset(g) != NULL)
	GD_maxset(g, UF_find((ST_Agnode_s) GD_maxset(g))); // 	GD_maxset(g) = UF_find(GD_maxset(g));
    if ((n = (ST_Agnode_s) GD_maxset(g))!=null) { //     if ((n = GD_maxset(g))) {
	slen.y = (ND_ranktype((ST_Agnode_s) GD_maxset(g)) == SINKRANK) ? 1 : 0; // 	slen.y = (ND_ranktype(GD_maxset(g)) == SINKRANK);
	// The C list is NULL terminated: "while ((e = ND_out(n).list[0]))" loops until it is empty
	while (ND_out(n).size > 0) { // 	while ((e = ND_out(n).list[0])) {
	    e = ND_out(n).list.get_(0);
	    // 	    assert(agtail(e) == UF_find(agtail(e)));
	    reverse_edge(e); // 	    reverse_edge(e);
	} // 	}
    } //     }
    if ((n = (ST_Agnode_s) GD_minset(g))!=null) { //     if ((n = GD_minset(g))) {
	slen.x = (ND_ranktype((ST_Agnode_s) GD_minset(g)) == SOURCERANK) ? 1 : 0; // 	slen.x = (ND_ranktype(GD_minset(g)) == SOURCERANK);
	while (ND_in(n).size > 0) { // 	while ((e = ND_in(n).list[0])) {
	    e = ND_in(n).list.get_(0);
	    // 	    assert(aghead(e) == UF_find(aghead(e)));
	    reverse_edge(e); // 	    reverse_edge(e);
	} // 	}
    } //     }
    return slen; //     return slen;
} finally {
LEAVING("3bcr1748gqnu8ogb73jeja7ly","minmax_edges");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="minmax_edges2", key="1rmlm1wo3t94wyet9rlwrmith", definition="static int  minmax_edges2(graph_t * g, point slen)")
public static boolean minmax_edges2(Globals zz, ST_Agraph_s g, final ST_point slen) {
// WARNING!! STRUCT
return minmax_edges2_w_(zz, g, (ST_point) slen.copy());
}
private static boolean minmax_edges2_w_(Globals zz, ST_Agraph_s g, final ST_point slen) {
ENTERING("1rmlm1wo3t94wyet9rlwrmith","minmax_edges2");
try {
    ST_Agnode_s n;
    ST_Agedge_s e = null;
    if ((GD_maxset(g)!=null) || (GD_minset(g)!=null)) {
	for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n)) { // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
	    if (n != UF_find(n)) // 	    if (n != UF_find(n))
		continue; // 		continue;
	    if ((ND_out(n).size == 0) && (GD_maxset(g)!=null) && (n != GD_maxset(g))) { // 	    if ((ND_out(n).size == 0) && GD_maxset(g) && (n != GD_maxset(g))) {
		e = virtual_edge(n, (ST_Agnode_s) GD_maxset(g), null); // 		e = virtual_edge(n, GD_maxset(g), NULL);
		ED_minlen(e, slen.y); // 		ED_minlen(e) = slen.y;
		ED_weight(e, 0); // 		ED_weight(e) = 0;
	    } // 	    }
	    if ((ND_in(n).size == 0) && (GD_minset(g)!=null) && (n != GD_minset(g))) { // 	    if ((ND_in(n).size == 0) && GD_minset(g) && (n != GD_minset(g))) {
		e = virtual_edge((ST_Agnode_s) GD_minset(g), n, null); // 		e = virtual_edge(GD_minset(g), n, NULL);
		ED_minlen(e, slen.x); // 		ED_minlen(e) = slen.x;
		ED_weight(e, 0); // 		ED_weight(e) = 0;
	    } // 	    }
	} // 	}
    }
    return (e != null);
} finally {
LEAVING("1rmlm1wo3t94wyet9rlwrmith","minmax_edges2");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="rank1", key="3vpthwso788idvycelpnqijys", definition="void rank1(graph_t * g)")
public static void rank1(Globals zz, ST_Agraph_s g) {
ENTERING("3vpthwso788idvycelpnqijys","rank1");
try {
    int maxiter = Integer.MAX_VALUE;
    int c;
    CString s;
    
    if ((s = agget(zz, g, new CString("nslimit1")))!=null)
    UNSUPPORTED("9tp2zk1tsr4ce9rwsr0is9u3o"); // 	maxiter = atof(s) * agnnodes(g);
    for (c = 0; c < GD_comp(g).size; c++) {
	GD_nlist(g, GD_comp(g).list.get_(c));
	rank(zz, g, (GD_n_cluster(g) == 0 ? 1 : 0), maxiter);	/* TB balance */
    }
} finally {
LEAVING("3vpthwso788idvycelpnqijys","rank1");
}
}




/* 
 * Assigns ranks of non-leader nodes.
 * Expands same, min, max rank sets.
 * Leaf sets and clusters remain merged.
 * Sets minrank and maxrank appropriately.
 */
@Reviewed(when = "14/11/2020")
@HasND_Rank
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="expand_ranksets", key="cdh8wnb99v90dy6efpbzmrjix", definition="static void expand_ranksets(graph_t * g, aspect_t* asp)")
public static void expand_ranksets(Globals zz, ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("cdh8wnb99v90dy6efpbzmrjix","expand_ranksets");
try {
    int c;
    ST_Agnode_s n, leader;
    
    if ((n = agfstnode(zz, g))!=null) {
	GD_minrank(g, MAXSHORT);
	GD_maxrank(g, -1);
	while (n!=null) {
	    leader = UF_find(n);
	    /* The following works because ND_rank(n) == 0 if n is not in a
	     * cluster, and ND_rank(n) = the local rank offset if n is in
	     * a cluster. */
	    if ((leader != n) && ((asp) == null || (ND_rank(n) == 0)))
		ND_rank(n, ND_rank(n) + ND_rank(leader));
	    
	    if (GD_maxrank(g) < ND_rank(n))
		GD_maxrank(g, ND_rank(n));
	    if (GD_minrank(g) > ND_rank(n))
		GD_minrank(g, ND_rank(n));
	    
	    if (ND_ranktype(n)!=0 && (ND_ranktype(n) != LEAFSET))
		UF_singleton(n);
	    n = agnxtnode(zz, g, n);
	}
	if (g == dot_root(g)) {
	    if (zz.CL_type == LOCAL) {
		for (c = 1; c <= GD_n_cluster(g); c++)
		    set_minmax(GD_clust(g).get_(c));
	    } else {
		find_clusters(g);
	    }
	}
    } else {
	GD_maxrank(g, 0);
	GD_minrank(g, 0);
    }
} finally {
LEAVING("cdh8wnb99v90dy6efpbzmrjix","expand_ranksets");
}
}



/* dot1_rank:
 * asp != NULL => g is root
 */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="dot1_rank", key="2o4rmb4o6f6zh46ak3se91rwr", definition="static void dot1_rank(graph_t * g, aspect_t* asp)")
public static void dot1_rank(Globals zz, ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("2o4rmb4o6f6zh46ak3se91rwr","dot1_rank");
try {
    final ST_point p = new ST_point();
    
    edgelabel_ranks(zz, g);
    
    if (asp!=null) {
	UNSUPPORTED("kh7e20nqwuserrnpf3zpvuyl"); // 	init_UF_size(g);
	UNSUPPORTED("d88j5oswhz0d3yvd4wlvxohmu"); // 	initEdgeTypes(g);
    }
    
    
    collapse_sets(zz, g,g);
    /*collapse_leaves(g); */
    class1_(zz, g);
    p.___(minmax_edges(g));
    decompose(zz, g, 0);
    if (asp!=null && ((GD_comp(g).size > 1)||(GD_n_cluster(g) > 0))) {
	UNSUPPORTED("evcjt85irnaa02v8cam07i009"); // 	asp->badGraph = 1;
	UNSUPPORTED("45nxv6kczal9hnytkfcyt2jk8"); // 	asp = NULL;
    }
    acyclic_(g);
    if (minmax_edges2(zz, g, p))
	decompose(zz, g, 0); // 	decompose(g, 0);
    if (asp!=null)
	rank3(g, asp);
    else
	rank1(zz, g);
    
    expand_ranksets(zz, g, asp);
    cleanup1(zz, g);
} finally {
LEAVING("2o4rmb4o6f6zh46ak3se91rwr","dot1_rank");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="dot_rank", key="asyfujgwqa407ffvqn5psbtsc", definition="void dot_rank(graph_t * g, aspect_t* asp)")
public static void dot_rank(Globals zz, ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("asyfujgwqa407ffvqn5psbtsc","dot_rank");
try {
    if (agget (zz, g, new CString("newrank"))!=null) {
	GD_flags(g, GD_flags(g) | NEW_RANK);
	dot2_rank (g, asp);
    }
    else
	dot1_rank (zz, g, asp);
    //if (Verbose)
	//fprintf (stderr, "Maxrank = %d, minrank = %d\n", (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxrank), (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minrank));
} finally {
LEAVING("asyfujgwqa407ffvqn5psbtsc","dot_rank");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="is_cluster", key="cdncou6d2ng5i48rd1mk2cpnw", definition="int is_cluster(graph_t * g)")
public static boolean is_cluster(Globals zz, ST_Agraph_s g) {
ENTERING("cdncou6d2ng5i48rd1mk2cpnw","is_cluster");
try {
    return (strncmp(agnameof(zz, g), new CString("cluster"), 7) == 0);
} finally {
LEAVING("cdncou6d2ng5i48rd1mk2cpnw","is_cluster");
}
}





//3 590k5zi3mrpwbc3lib0w3rmr2
// void dot2_rank(graph_t * g, aspect_t* asp) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/rank.c", name="dot2_rank", key="590k5zi3mrpwbc3lib0w3rmr2", definition="void dot2_rank(graph_t * g, aspect_t* asp)")
public static Object dot2_rank(Object... arg_) {
UNSUPPORTED("d8gu9ua6rerpv9vz9ctco1ca2"); // void dot2_rank(graph_t * g, aspect_t* asp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("23k65agnd27tv4ix9teds9e2t"); //     int ssize;
UNSUPPORTED("dx1unsp79t4ji8dh8idt48jrc"); //     int ncc, maxiter = INT_MAX;
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("dxlxz9md3d6r12wog4x5sc7td"); //     graph_t *Xg;
UNSUPPORTED("hibhvgkp511r6u6ips8yb0un"); //     Last_node = NULL;
UNSUPPORTED("ey4p0fjtw4ac18jh9svmzjs23"); //     Xg = agopen("level assignment constraints", Agstrictdirected, 0);
UNSUPPORTED("e4j7z7nfe33svydzyn4w6abcy"); //     agbindrec(Xg,"level graph rec",sizeof(Agraphinfo_t),NOT(0));
UNSUPPORTED("4j4bkw2k5v7xlf7ycqcrz8qip"); //     agpushdisc(Xg,&mydisc,infosizes);
UNSUPPORTED("d4pjn5ef0ywzmhe2fshhm8bvn"); //     edgelabel_ranks(g);
UNSUPPORTED("e0rdg08m66a12fiixgkjnyrbj"); //     if ((s = agget(g, "nslimit1")))
UNSUPPORTED("9tp2zk1tsr4ce9rwsr0is9u3o"); // 	maxiter = atof(s) * agnnodes(g);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("dapt7hf1vwq593la2oydyrv27"); // 	maxiter = INT_MAX;
UNSUPPORTED("62k95fm4s1z2wzcyg28ir0x7u"); //     compile_samerank(g, 0);
UNSUPPORTED("bh9imh5owlj1c9ad7mime392x"); //     compile_nodes(g, Xg);
UNSUPPORTED("3kxtahvovojtzi6qqnrricpoo"); //     compile_edges(g, Xg);
UNSUPPORTED("9twf7u3r2hzeic9w0gmvh10bc"); //     compile_clusters(g, Xg, 0, 0);
UNSUPPORTED("cwrov5g30logh4g9omvkblonh"); //     break_cycles(Xg);
UNSUPPORTED("3ficrpbhiwichejg6n1hshz7k"); //     ncc = connect_components(Xg);
UNSUPPORTED("9x72se4xuqwfv27jlqpmivrwb"); //     add_fast_edges (Xg);
UNSUPPORTED("2yazmwrpb1ni51wuck3ruvi2j"); //     if (asp) {
UNSUPPORTED("8ow3lzc6gh107g9bcn4szm7hj"); // 	init_UF_size(Xg);
UNSUPPORTED("9503vlimf1i2zv76ua88ooepc"); // 	initEdgeTypes(Xg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b65fc1791mzxccp9zzxi8vk12"); //     if ((s = agget(g, "searchsize")))
UNSUPPORTED("aqd144wenl3zq15bwc41u9aha"); // 	ssize = atoi(s);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2n4z8w1w3il45lik0kraspkud"); // 	ssize = -1;
UNSUPPORTED("aotd35u0hficqt6hlkw8xof03"); //     rank2(Xg, 1, maxiter, ssize);
UNSUPPORTED("4x9mvgxbdou6xj4n98rwzucgi"); // /* fastgr(Xg); */
UNSUPPORTED("8un6x92pzddrzsnq8y95af4m6"); //     readout_levels(g, Xg, ncc);
UNSUPPORTED("6rs6sp7mefzzbf02kfmvycnaq"); //     agclose(Xg);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
