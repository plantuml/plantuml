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
package gen.lib.common;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.common.utils__c.dequeue;
import static gen.lib.common.utils__c.enqueue;
import static gen.lib.common.utils__c.free_queue;
import static gen.lib.common.utils__c.new_queue;
import static smetana.core.JUtils.atoi;
import static smetana.core.JUtils.setjmp;
import static smetana.core.Macro.ED_cutvalue;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_tree_index;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_lim;
import static smetana.core.Macro.ND_low;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_par;
import static smetana.core.Macro.ND_priority;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_tree_in;
import static smetana.core.Macro.ND_tree_out;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.SEARCHSIZE;
import static smetana.core.Macro.SEQ;
import static smetana.core.Macro.SLACK;
import static smetana.core.Macro.TREE_EDGE;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.free_list;
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
import h.ST_nodequeue;
import smetana.core.CArrayOfStar;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.Memory;
import smetana.core.ZType;
import smetana.core.jmp_buf;


/* 
 * Network Simplex Algorithm for Ranking Nodes of a DAG
 */
public class ns__c {



//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();


@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/common/ns.c", name="add_tree_edge", key="6au5htcaxhw0blmx5c48v03u0", definition="static void add_tree_edge(edge_t * e)")
public static void add_tree_edge(Globals zz, ST_Agedge_s e) {
ENTERING("6au5htcaxhw0blmx5c48v03u0","add_tree_edge");
try {
    ST_Agnode_s n;
    if (TREE_EDGE(e)) {
	UNSUPPORTED("cq4nqjjxvb0dtdfy4c7pwpqai"); // 	agerr(AGERR, "add_tree_edge: missing tree edge\n");
	UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
    ED_tree_index(e, zz.Tree_edge.size);
    
    zz.Tree_edge.list.set_(zz.Tree_edge.size++, e);
        
    if (ND_mark(agtail(e)) == 0)
    zz.Tree_node.list.set_(zz.Tree_node.size++, agtail(e));
    if (ND_mark(aghead(e)) == 0)
	zz.Tree_node.list.set_(zz.Tree_node.size++, aghead(e));
    n = agtail(e);
    ND_mark(n, 1);
    ND_tree_out(n).list.set_(ND_tree_out(n).size++, e);
    ND_tree_out(n).list.set_(ND_tree_out(n).size, null);
    
    if (ND_out(n).list.get_(ND_tree_out(n).size - 1) == null) {
UNSUPPORTED("9src34zgmgy8yvdzfs1ozlh0w"); // 	agerr(AGERR, "add_tree_edge: empty outedge list\n");
UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
    n = aghead(e);
    ND_mark(n, 1);
    
    ND_tree_in(n).list.set_(ND_tree_in(n).size++, e);
    ND_tree_in(n).list.set_(ND_tree_in(n).size, null);

    if (ND_in(n).list.get_(ND_tree_in(n).size - 1) == null) {
UNSUPPORTED("f0uri98pv606g2qjpy9k385cy"); // 	agerr(AGERR, "add_tree_edge: empty inedge list\n");
UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
} finally {
LEAVING("6au5htcaxhw0blmx5c48v03u0","add_tree_edge");
}
}




//3 9b7b78pmafynmvffztrqnlxtn
// static void exchange_tree_edges(edge_t * e, edge_t * f) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="exchange_tree_edges", key="9b7b78pmafynmvffztrqnlxtn", definition="static void exchange_tree_edges(edge_t * e, edge_t * f)")
public static void exchange_tree_edges(Globals zz, ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("9b7b78pmafynmvffztrqnlxtn","exchange_tree_edges");
try {
    int i, j;
    ST_Agnode_s n;
    ED_tree_index(f, ED_tree_index(e));
    zz.Tree_edge.list.set_(ED_tree_index(e), f);
    ED_tree_index(e, -1);
    n = agtail(e);
    ND_tree_out(n).size = ND_tree_out(n).size -1;
    i = ND_tree_out(n).size;
    for (j = 0; j <= i; j++)
	if (ND_tree_out(n).list.get_(j) == e)
	    break;
    ND_tree_out(n).list.set_(j, ND_tree_out(n).list.get_(i));
    ND_tree_out(n).list.set_(i, null);
    n = aghead(e);
    ND_tree_in(n).size = ND_tree_in(n).size -1;
    i = ND_tree_in(n).size;
    for (j = 0; j <= i; j++)
	if (ND_tree_in(n).list.get_(j) == e)
	    break;
    ND_tree_in(n).list.set_(j, ND_tree_in(n).list.get_(i));
    ND_tree_in(n).list.set_(i, null);
    n = agtail(f);
    ND_tree_out(n).list.set_(ND_tree_out(n).size, f);
    ND_tree_out(n).size = ND_tree_out(n).size +1;
    ND_tree_out(n).list.set_(ND_tree_out(n).size, null);
    n = aghead(f);
    ND_tree_in(n).list.set_(ND_tree_in(n).size, f);
    ND_tree_in(n).size = ND_tree_in(n).size +1;
    ND_tree_in(n).list.set_(ND_tree_in(n).size, null);
} finally {
LEAVING("9b7b78pmafynmvffztrqnlxtn","exchange_tree_edges");
}
}




//3 dbxco6m0mabzhsqfo3pb8nctk
// static void init_rank(void) 
@Unused
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="init_rank", key="dbxco6m0mabzhsqfo3pb8nctk", definition="static void init_rank(void)")
public static void init_rank(Globals zz) {
ENTERING("dbxco6m0mabzhsqfo3pb8nctk","init_rank");
try {
    int i, ctr;
    ST_nodequeue Q;
    ST_Agnode_s v;
    ST_Agedge_s e;
    Q = new_queue(zz.N_nodes);
    ctr = 0;
    for (v = GD_nlist(zz.G_ns); v!=null; v = ND_next(v)) {
	if (ND_priority(v) == 0)
	    enqueue(Q, v);
    }
    while ((v = dequeue(Q))!=null) {
	ND_rank(v, 0);
	ctr++;
	for (i = 0; (e = ND_in(v).list.get_(i))!=null; i++)
	    ND_rank(v, Math.max(ND_rank(v), ND_rank(agtail(e)) + ED_minlen(e)));
	for (i = 0; (e = ND_out(v).list.get_(i))!=null; i++) {
	    ND_priority(aghead(e), ND_priority(aghead(e)) -1 );
	    if ((ND_priority(aghead(e))) <= 0)
		enqueue(Q, aghead(e));
	}
    }
    if (ctr != zz.N_nodes) {
UNSUPPORTED("7sgp99x1l3hzfks5wykxa87gf"); // 	agerr(AGERR, "trouble in init_rank\n");
UNSUPPORTED("bwwunxmw4kgz6qntbn6xp0cur"); // 	for (v = (((Agraphinfo_t*)(((Agobj_t*)(G))->data))->nlist); v; v = (((Agnodeinfo_t*)(((Agobj_t*)(v))->data))->next))
UNSUPPORTED("3dk132mz1u2pf0tla64kl6hv0"); // 	    if ((((Agnodeinfo_t*)(((Agobj_t*)(v))->data))->priority))
UNSUPPORTED("916bi45h6sjvte1rgig12b1v2"); // 		agerr(AGPREV, "\t%s %d\n", agnameof(v), (((Agnodeinfo_t*)(((Agobj_t*)(v))->data))->priority));
    }
    free_queue(Q);
} finally {
LEAVING("dbxco6m0mabzhsqfo3pb8nctk","init_rank");
}
}




//3 bj7ux5kz8ls2lnfh0ix6i00b9
// static node_t *incident(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="bj7ux5kz8ls2lnfh0ix6i00b9", definition="static node_t *incident(edge_t * e)")
public static ST_Agnode_s incident(ST_Agedge_s e) {
ENTERING("bj7ux5kz8ls2lnfh0ix6i00b9","incident");
try {
    if (ND_mark(agtail(e))!=0) {
	if (ND_mark(aghead(e)) == 0)
	    return agtail(e);
    } else {
	if (ND_mark(aghead(e))!=0)
	    return aghead(e);
    }
    return null;
} finally {
LEAVING("bj7ux5kz8ls2lnfh0ix6i00b9","incident");
}
}





@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="4i9tcvid2iql874c6k70s9aqm", definition="static edge_t *leave_edge(void)")
public static ST_Agedge_s leave_edge(Globals zz) {
ENTERING("4i9tcvid2iql874c6k70s9aqm","leave_edge");
try {
    ST_Agedge_s f, rv = null;
    int j, cnt = 0;
    
    j = zz.S_i;
    while (zz.S_i < zz.Tree_edge.size) {
	if (ED_cutvalue(f = (ST_Agedge_s) zz.Tree_edge.list.get_(zz.S_i)) < 0) {
	    if (rv!=null) {
		if (ED_cutvalue(rv) > ED_cutvalue(f))
		    rv = f;
	    } else
		rv = (ST_Agedge_s) zz.Tree_edge.list.get_(zz.S_i);
	    if (++cnt >= zz.Search_size)
		return rv;
	}
	zz.S_i++;
    }
    if (j > 0) {
	zz.S_i = 0;
	while (zz.S_i < j) {
	    if (ED_cutvalue(f = (ST_Agedge_s) zz.Tree_edge.list.get_(zz.S_i)) < 0) {
		if (rv!=null) {
		    if (ED_cutvalue(rv) > ED_cutvalue(f))
			rv = f;
		} else
		    rv = (ST_Agedge_s) zz.Tree_edge.list.get_(zz.S_i);
		if (++cnt >= zz.Search_size)
		    return rv;
	    }
	    zz.S_i++;
	}
    }
    return rv;
} finally {
LEAVING("4i9tcvid2iql874c6k70s9aqm","leave_edge");
}
}


//1 3wm7ej298st1xk7rbhbtnbk64
// static edge_t *Enter
//private static Agedge_s Enter;

//1 dx9f0e947f5kjhc2eftn43t90
// static int Low, Lim, Slack
//private static int Low, Lim, Slack;



//3 10lkpr4y40luvy2idlozfiva3
// static void dfs_enter_outedge(node_t * v) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_enter_outedge", key="10lkpr4y40luvy2idlozfiva3", definition="static void dfs_enter_outedge(node_t * v)")
public static void dfs_enter_outedge(Globals zz, ST_Agnode_s v) {
ENTERING("10lkpr4y40luvy2idlozfiva3","dfs_enter_outedge");
try {
    int i, slack;
    ST_Agedge_s e;
    for (i = 0; (e = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++) {
	if (TREE_EDGE(e) == false) {
	    if (!SEQ(zz.Low, ND_lim(aghead(e)), zz.Lim)) {
		slack = SLACK(e);
		if ((slack < zz.Slack) || (zz.Enter == null)) {
		    zz.Enter = e;
		    zz.Slack = slack;
		}
	    }
	} else if (ND_lim(aghead(e)) < ND_lim(v))
	    dfs_enter_outedge(zz, aghead(e));
    }
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null && (zz.Slack > 0); i++)
	if (ND_lim(agtail(e)) < ND_lim(v))
	    dfs_enter_outedge(zz, agtail(e));
} finally {
LEAVING("10lkpr4y40luvy2idlozfiva3","dfs_enter_outedge");
}
}




//3 2z9nii6380p8qlql8nznzgvof
// static void dfs_enter_inedge(node_t * v) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_enter_inedge", key="2z9nii6380p8qlql8nznzgvof", definition="static void dfs_enter_inedge(node_t * v)")
public static void dfs_enter_inedge(Globals zz, ST_Agnode_s v) {
ENTERING("2z9nii6380p8qlql8nznzgvof","dfs_enter_inedge");
try {
    int i, slack;
    ST_Agedge_s e;
    for (i = 0; (e = (ST_Agedge_s) ND_in(v).list.get_(i))!=null; i++) {
	if (TREE_EDGE(e) == false) {
	    if (!SEQ(zz.Low, ND_lim(agtail(e)), zz.Lim)) {
		slack = SLACK(e);
		if ((slack < zz.Slack) || (zz.Enter == null)) {
		    zz.Enter = e;
		    zz.Slack = slack;
		}
	    }
	} else if (ND_lim(agtail(e)) < ND_lim(v))
	    dfs_enter_inedge(zz, agtail(e));
    }
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null && (zz.Slack > 0); i++)
	if (ND_lim(aghead(e)) < ND_lim(v))
	    dfs_enter_inedge(zz, aghead(e));
} finally {
LEAVING("2z9nii6380p8qlql8nznzgvof","dfs_enter_inedge");
}
}




//3 aeu2po1o1rvibmafk0k8dw0fh
// static edge_t *enter_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="aeu2po1o1rvibmafk0k8dw0fh", definition="static edge_t *enter_edge(edge_t * e)")
public static ST_Agedge_s enter_edge(Globals zz, ST_Agedge_s e) {
ENTERING("aeu2po1o1rvibmafk0k8dw0fh","enter_edge");
try {
    ST_Agnode_s v;
    int outsearch;
    /* v is the down node */
    if (ND_lim(agtail(e)) < ND_lim(aghead(e))) {
	v = agtail(e);
	outsearch = 0;
    } else {
	v = aghead(e);
	outsearch = 1;
    }
    zz.Enter = null;
    zz.Slack = INT_MAX;
    zz.Low = ND_low(v);
    zz.Lim = ND_lim(v);
    if (outsearch!=0)
	dfs_enter_outedge(zz, v);
    else
	dfs_enter_inedge(zz, v);
    return zz.Enter;
} finally {
LEAVING("aeu2po1o1rvibmafk0k8dw0fh","enter_edge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="treesearch", key="1gvyafmercq92v3lg6gb33cbt", definition="static int treesearch(node_t * v)")
public static boolean treesearch(Globals zz, ST_Agnode_s v) {
ENTERING("1gvyafmercq92v3lg6gb33cbt","treesearch");
try {
    int i;
    ST_Agedge_s e;
    
    for (i = 0; (e = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++) {
	if ((ND_mark(aghead(e)) == 0) && (SLACK(e) == 0)) {
	    add_tree_edge(zz, e);
	    if ((zz.Tree_edge.size == zz.N_nodes - 1) || treesearch(zz, aghead(e)))
		return true;
	}
    }
    for (i = 0; (e = (ST_Agedge_s) ND_in(v).list.get_(i))!=null; i++) {
	if ((ND_mark(agtail(e)) == 0) && (SLACK(e) == 0)) {
	    add_tree_edge(zz, e);
	    if ((zz.Tree_edge.size == zz.N_nodes - 1) || treesearch(zz, agtail(e)))
		return true;
	}
    }
    return false;
} finally {
LEAVING("1gvyafmercq92v3lg6gb33cbt","treesearch");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="tight_tree", key="c98bj1u8j43cdezeczn33mec0", definition="static int tight_tree(void)")
public static int tight_tree(Globals zz) {
ENTERING("c98bj1u8j43cdezeczn33mec0","tight_tree");
try {
    int i;
    ST_Agnode_s n;
    
    for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n)) {
	ND_mark(n, 0);
	ND_tree_in(n).list.set_(0, null);
	ND_tree_out(n).list.set_(0, null);
	ND_tree_in(n).size = ND_tree_out(n).size = 0;
    }
    for (i = 0; i < zz.Tree_edge.size; i++)
	ED_tree_index(zz.Tree_edge.list.get_(i), -1);
    
    zz.Tree_node.size = zz.Tree_edge.size = 0;
    for (n = GD_nlist(zz.G_ns); n!=null && (zz.Tree_edge.size == 0); n = ND_next(n))
	treesearch(zz, n);
    return zz.Tree_node.size;
} finally {
LEAVING("c98bj1u8j43cdezeczn33mec0","tight_tree");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="init_cutvalues", key="10o7oe8d097fx7swmpqd4tf0h", definition="static void init_cutvalues(void)")
public static void init_cutvalues(Globals zz) {
ENTERING("10o7oe8d097fx7swmpqd4tf0h","init_cutvalues");
try {
    dfs_range(GD_nlist(zz.G_ns), null, 1);
    dfs_cutval(GD_nlist(zz.G_ns), null);
} finally {
LEAVING("10o7oe8d097fx7swmpqd4tf0h","init_cutvalues");
}
}




@Reviewed(when = "14/11/2020")
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="feasible_tree", key="756raqohoxdeiddqbyr37h7ig", definition="static int feasible_tree(void)")
public static int feasible_tree(Globals zz) {
ENTERING("756raqohoxdeiddqbyr37h7ig","feasible_tree");
try {
    int i, delta;
    ST_Agnode_s n;
    ST_Agedge_s e, f;
    
    if (zz.N_nodes <= 1)
	return 0;
    while (tight_tree(zz) < zz.N_nodes) {
	e = null;
	for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n)) {
	    for (i = 0; (f = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++) {
		if ((TREE_EDGE(f) == false) && incident(f)!=null && ((e == null)
							       || (SLACK(f)
								   <
								   SLACK
								   (e))))
		    e = f;
	    }
	}
	
	
	if (e!=null) {
	    delta = SLACK(e);
	    if (delta!=0) {
		if (incident(e) == aghead(e))
		    delta = -delta;
		for (i = 0; i < zz.Tree_node.size; i++)
		    ND_rank(zz.Tree_node.list.get_(i), ND_rank(zz.Tree_node.list.get_(i)) + delta);
	    }
	} else {
	    return 1;
	}
    }
    init_cutvalues(zz);
    return 0;
} finally {
LEAVING("756raqohoxdeiddqbyr37h7ig","feasible_tree");
}
}




//3 49un8m43odrf89cedvin3wz3r
// static node_t *treeupdate(node_t * v, node_t * w, int cutvalue, int dir) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="49un8m43odrf89cedvin3wz3r", definition="static node_t *treeupdate(node_t * v, node_t * w, int cutvalue, int dir)")
public static ST_Agnode_s treeupdate(ST_Agnode_s v, ST_Agnode_s w, int cutvalue, boolean dir) {
ENTERING("49un8m43odrf89cedvin3wz3r","treeupdate");
try {
    ST_Agedge_s e;
    boolean d;
    while (!SEQ(ND_low(v), ND_lim(w), ND_lim(v))) {
	e = (ST_Agedge_s) ND_par(v);
	if (v == agtail(e))
	    d = dir;
	else
	    d = !dir;
	if (d)
	    ED_cutvalue(e, ED_cutvalue(e) + cutvalue);
	else
	    ED_cutvalue(e, ED_cutvalue(e) - cutvalue);
	if (ND_lim(agtail(e)) > ND_lim(aghead(e)))
	    v = agtail(e);
	else
	    v = aghead(e);
    }
    return v;
} finally {
LEAVING("49un8m43odrf89cedvin3wz3r","treeupdate");
}
}




//3 e66n8gern1fejjsn8nefypo0g
// static void rerank(node_t * v, int delta) 
@Unused
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="rerank", key="e66n8gern1fejjsn8nefypo0g", definition="static void rerank(node_t * v, int delta)")
public static void rerank(ST_Agnode_s v, int delta) {
ENTERING("e66n8gern1fejjsn8nefypo0g","rerank");
try {
    int i;
    ST_Agedge_s e;
    ND_rank(v, ND_rank(v) - delta);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null; i++)
	if (e != ND_par(v))
	    rerank(aghead(e), delta);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null; i++)
	if (e != ND_par(v))
	    rerank(agtail(e), delta);
} finally {
LEAVING("e66n8gern1fejjsn8nefypo0g","rerank");
}
}




//3 xww1p8bentf1qk7mgfhi1q6m
// static void  update(edge_t * e, edge_t * f) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="update", key="xww1p8bentf1qk7mgfhi1q6m", definition="static void  update(edge_t * e, edge_t * f)")
public static void update(Globals zz, ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("xww1p8bentf1qk7mgfhi1q6m","update");
try {
    int cutvalue, delta;
    ST_Agnode_s lca;
    delta = SLACK(f);
    /* "for (v = in nodes in tail side of e) do ND_rank(v) -= delta;" */
    if (delta > 0) {
	int s;
	s = ND_tree_in(agtail(e)).size + ND_tree_out(agtail(e)).size;
	if (s == 1)
	    rerank(agtail(e), delta);
	else {
	    s = ND_tree_in(aghead(e)).size + ND_tree_out(aghead(e)).size;
	    if (s == 1)
		rerank(aghead(e), -delta);
	    else {
		if (ND_lim(agtail(e)) < ND_lim(aghead(e)))
		    rerank(agtail(e), delta);
		else
		    rerank(aghead(e), -delta);
	    }
	}
    }
    cutvalue = ED_cutvalue(e);
    lca = treeupdate(agtail(f), aghead(f), cutvalue, true);
    if ((treeupdate(aghead(f), agtail(f), cutvalue, false) != lca)) {
UNSUPPORTED("f2l4c6yhnwnfer3vrasf55fio"); // 	agerr(AGERR, "update: mismatched lca in treeupdates\n");
UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
    ED_cutvalue(f, -cutvalue);
    ED_cutvalue(e, 0);
    exchange_tree_edges(zz, e, f);
    dfs_range(lca, ND_par(lca), ND_low(lca));
} finally {
LEAVING("xww1p8bentf1qk7mgfhi1q6m","update");
}
}




@Reviewed(when = "14/11/2020")
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="scan_and_normalize", key="3yw7w42hz7af67d6qse3b2172", definition="static void scan_and_normalize(void)")
public static void scan_and_normalize(Globals zz) {
ENTERING("3yw7w42hz7af67d6qse3b2172","scan_and_normalize");
try {
    ST_Agnode_s n;
    
    zz.Minrank = Integer.MAX_VALUE;
    zz.Maxrank = -Integer.MAX_VALUE;
    for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n)) {
	if (ND_node_type(n) == NORMAL) {
	    zz.Minrank = Math.min(zz.Minrank, ND_rank(n));
	    zz.Maxrank = Math.max(zz.Maxrank, ND_rank(n));
	}
    }
    if (zz.Minrank != 0) {
	for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n))
	    ND_rank(n, ND_rank(n) - zz.Minrank);
	zz.Maxrank -= zz.Minrank;
	zz.Minrank = 0;
    }
} finally {
LEAVING("3yw7w42hz7af67d6qse3b2172","scan_and_normalize");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="freeTreeList", key="7eg6zti36nbg4tqyo8yunh86r", definition="static void freeTreeList (graph_t* g)")
public static void freeTreeList(Globals zz, ST_Agraph_s g) {
ENTERING("7eg6zti36nbg4tqyo8yunh86r","freeTreeList");
try {
    ST_Agnode_s n;
    for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n)) {
	free_list(ND_tree_in(n));
	free_list(ND_tree_out(n));
	ND_mark(n, 0);
    }
} finally {
LEAVING("7eg6zti36nbg4tqyo8yunh86r","freeTreeList");
}
}




//3 9gx8p7md3v3mzp640xdjj814a
// static void LR_balance(void) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="LR_balance", key="9gx8p7md3v3mzp640xdjj814a", definition="static void LR_balance(void)")
public static void LR_balance(Globals zz) {
ENTERING("9gx8p7md3v3mzp640xdjj814a","LR_balance");
try {
    int i, delta;
    ST_Agedge_s e, f;
    for (i = 0; i < zz.Tree_edge.size; i++) {
	e = (ST_Agedge_s) zz.Tree_edge.list.get_(i);
	if (ED_cutvalue(e) == 0) {
	    f = enter_edge(zz, e);
	    if (f == null)
		continue;
	    delta = SLACK(f);
	    if (delta <= 1)
		continue;
	    if (ND_lim(agtail(e)) < ND_lim(aghead(e)))
		rerank(agtail(e), delta / 2);
	    else
		rerank(aghead(e), -delta / 2);
	}
    }
    freeTreeList (zz, zz.G_ns);
} finally {
LEAVING("9gx8p7md3v3mzp640xdjj814a","LR_balance");
}
}



@Difficult
@HasND_Rank
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="TB_balance", key="5c01jnao2ubmy4l0vi5jol0jz", definition="static void TB_balance(void)")
public static void TB_balance(Globals zz) {
ENTERING("5c01jnao2ubmy4l0vi5jol0jz","TB_balance");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    int i, low, high, choice;
    int[] nrank;
    int inweight, outweight;
    
    scan_and_normalize(zz);
    
    /* find nodes that are not tight and move to less populated ranks */
    nrank = new int[zz.Maxrank + 1];
    for (i = 0; i <= zz.Maxrank; i++)
    nrank[i] = 0;
    for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n))
	if (ND_node_type(n) == NORMAL)
		nrank[ND_rank(n)]++;
    for (n = GD_nlist(zz.G_ns); n!=null; n = ND_next(n)) {
	if (ND_node_type(n) != NORMAL)
	    continue;
	inweight = outweight = 0;
	low = 0;
	high = zz.Maxrank;
	for (i = 0; (e = (ST_Agedge_s) ND_in(n).list.get_(i))!=null; i++) {
	    inweight += ED_weight(e);
	    low = Math.max(low, ND_rank(agtail(e)) + ED_minlen(e));
	}
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++) {
	    outweight += ED_weight(e);
	    high = Math.min(high, ND_rank(aghead(e)) - ED_minlen(e));
	}
	if (low < 0)
	    low = 0;		/* vnodes can have ranks < 0 */
	if (inweight == outweight) {
	    choice = low;
	    for (i = low + 1; i <= high; i++)
		if (nrank[i] < nrank[choice])
		    choice = i;
	    nrank[ND_rank(n)]--;
	    nrank[choice]++;
	    ND_rank(n, choice);
	}
	free_list(ND_tree_in(n));
	free_list(ND_tree_out(n));
	ND_mark(n, 0);
    }
    Memory.free(nrank);
} finally {
LEAVING("5c01jnao2ubmy4l0vi5jol0jz","TB_balance");
}
}







@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/common/ns.c", name="init_graph", key="37hg5w7ywmyljdiebgp5ltl22", definition="static int init_graph(graph_t * g)")
public static int init_graph(Globals zz, ST_Agraph_s g) {
ENTERING("37hg5w7ywmyljdiebgp5ltl22","init_graph");
try {
    int i, feasible;
    ST_Agnode_s n;
    ST_Agedge_s e;
    
    
    zz.G_ns = g;
    zz.N_nodes = zz.N_edges = zz.S_i = 0;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	ND_mark(n, 0);
	zz.N_nodes++;
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++)
	    zz.N_edges++;
    }
    
    
    zz.Tree_node.list = CArrayOfStar.<ST_Agnode_s>REALLOC(zz.N_nodes, zz.Tree_node.list, ZType.ST_Agnode_s); 
    zz.Tree_node.size =  0;
    zz.Tree_edge.list = CArrayOfStar.<ST_Agedge_s>REALLOC(zz.N_nodes, zz.Tree_edge.list, ZType.ST_Agedge_s);
    zz.Tree_edge.size = 0;
    
    
    feasible = 1;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	ND_priority(n, 0);
	for (i = 0; (e = (ST_Agedge_s) ND_in(n).list.get_(i))!=null; i++) {
	    ND_priority(n, ND_priority(n)+1);
	    ED_cutvalue(e, 0);
	    ED_tree_index(e, -1);
	    if (feasible!=0
		&& (ND_rank(aghead(e)) - ND_rank(agtail(e)) < ED_minlen(e)))
		feasible = 0;
	}
	ND_tree_in(n).list = CArrayOfStar.<ST_Agedge_s> ALLOC(i + 1, ZType.ST_Agedge_s);
	ND_tree_in(n).size = 0;
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++);
	ND_tree_out(n).list = CArrayOfStar.<ST_Agedge_s> ALLOC(i + 1, ZType.ST_Agedge_s);
	ND_tree_out(n).size = 0;
    }
    return feasible;
} finally {
LEAVING("37hg5w7ywmyljdiebgp5ltl22","init_graph");
}
}





/* rank:
 * Apply network simplex to rank the nodes in a graph.
 * Uses ED_minlen as the internode constraint: if a->b with minlen=ml,
 * rank b - rank a >= ml.
 * Assumes the graph has the following additional structure:
 *   A list of all nodes, starting at GD_nlist, and linked using ND_next.
 *   Out and in edges lists stored in ND_out and ND_in, even if the node
 *  doesn't have any out or in edges.
 * The node rank values are stored in ND_rank.
 * Returns 0 if successful; returns 1 if `he graph was not connected;
 * returns 2 if something seriously wrong;
 */
@Difficult
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="rank2", key="5n0ipwzhr8urlx0fsdzr02gwq", definition="int rank2(graph_t * g, int balance, int maxiter, int search_size)")
public static int rank2(Globals zz, ST_Agraph_s g, int balance, int maxiter, int search_size) {
ENTERING("5n0ipwzhr8urlx0fsdzr02gwq","rank2");
try {
    int iter = 0, feasible;
    CString ns = new CString("network simplex: ");
    ST_Agedge_s e, f;
    
    
    /*if (Verbose) {
	int nn, ne;
	graphSize (g, &nn, &ne);
	fprintf(stderr, "%s %d nodes %d edges maxiter=%d balance=%d\n", ns,
	    nn, ne, maxiter, balance);
	start_timer();
    }*/
    feasible = init_graph(zz, g);
    if (feasible == 0)
	init_rank(zz);
    if (maxiter <= 0) {
	freeTreeList (zz, g);
	return 0;
    }
    
    
    if (search_size >= 0)
	zz.Search_size = search_size;
    else
	zz.Search_size = SEARCHSIZE;
    
    
    if (setjmp (jbuf)!=0) {
	return 2;
    }
    
    
    if (feasible_tree(zz)!=0) {
	freeTreeList (zz, g);
	return 1;
    }
    while ((e = leave_edge(zz))!=null) {
	f = enter_edge(zz, e);
	update(zz, e, f);
	iter++;
	/*if (Verbose && (iter % 100 == 0)) {
	    if (iter % 1000 == 100)
		fputs(ns, stderr);
	    fprintf(stderr, "%d ", iter);
	    if (iter % 1000 == 0)
		fputc('\n', stderr);
	}*/
	if (iter >= maxiter)
	    break;
    }
    switch (balance) {
    case 1:
	TB_balance(zz);
	break;
    case 2:
	LR_balance(zz);
	break;
    default:
	scan_and_normalize(zz);
	freeTreeList (zz, zz.G_ns);
	break;
    }
    /*if (Verbose) {
	if (iter >= 100)
	    fputc('\n', stderr);
	fprintf(stderr, "%s%d nodes %d edges %d iter %.2f sec\n",
		ns, N_nodes, N_edges, iter, elapsed_sec());
    }*/
    return 0;
} finally {
LEAVING("5n0ipwzhr8urlx0fsdzr02gwq","rank2");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="rank", key="aqly8eniwjr5bmh4hzwc7ftdr", definition="int rank(graph_t * g, int balance, int maxiter)")
public static int rank(Globals zz, ST_Agraph_s g, int balance, int maxiter) {
ENTERING("aqly8eniwjr5bmh4hzwc7ftdr","rank");
try {
    CString s;
    int search_size;
    
    if ((s = agget(zz, g, new CString("searchsize")))!=null)
	search_size = atoi(s);
    else
	search_size = SEARCHSIZE;
    
    return rank2 (zz, g, balance, maxiter, search_size);
} finally {
LEAVING("aqly8eniwjr5bmh4hzwc7ftdr","rank");
}
}




/* set cut value of f, assuming values of edges on one side were already set */
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="x_cutval", key="2q59mz8qtn0biifbezb8uxz17", definition="static void x_cutval(edge_t * f)")
public static void x_cutval(ST_Agedge_s f) {
ENTERING("2q59mz8qtn0biifbezb8uxz17","x_cutval");
try {
    ST_Agnode_s v=null;
    ST_Agedge_s e;
    int i, sum, dir=0;
    
    /* set v to the node on the side of the edge already searched */
    if (ND_par(agtail(f)) == f) {
	v = agtail(f);
	dir = 1;
    } else {
	v = aghead(f);
	dir = -1;
    }
    
    sum = 0;
    for (i = 0; (e = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++)
	sum += x_val(e, v, dir);
    for (i = 0; (e = (ST_Agedge_s) ND_in(v).list.get_(i))!=null; i++)
	sum += x_val(e, v, dir);
    ED_cutvalue(f, sum);
} finally {
LEAVING("2q59mz8qtn0biifbezb8uxz17","x_cutval");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="x_val", key="bfeafmsmmnblgizs37qj03dy4", definition="static int x_val(edge_t * e, node_t * v, int dir)")
public static int x_val(ST_Agedge_s e, ST_Agnode_s v, int dir) {
ENTERING("bfeafmsmmnblgizs37qj03dy4","x_val");
try {
    ST_Agnode_s other;
    int d=0, rv=0, f=0;
    
    if (agtail(e) == v)
	other = aghead(e);
    else
	other = agtail(e);
    if (!SEQ(ND_low(v), ND_lim(other), ND_lim(v))) {
	f = 1;
	rv = ED_weight(e);
    } else {
	f = 0;
	if (TREE_EDGE(e))
	    rv = ED_cutvalue(e);
	else
	    rv = 0;
	rv -= ED_weight(e);
    }
    if (dir > 0) {
	if (aghead(e) == v)
	    d = 1;
	else
	    d = -1;
    } else {
	if (agtail(e) == v)
	    d = 1;
	else
	    d = -1;
    }
    if (f!=0)
	d = -d;
    if (d < 0)
	rv = -rv;
    return rv;
} finally {
LEAVING("bfeafmsmmnblgizs37qj03dy4","x_val");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_cutval", key="ah65iqmwa5j0qwotm6amhijlg", definition="static void dfs_cutval(node_t * v, edge_t * par)")
public static void dfs_cutval(ST_Agnode_s v, ST_Agedge_s par) {
ENTERING("ah65iqmwa5j0qwotm6amhijlg","dfs_cutval");
try {
    int i;
    ST_Agedge_s e;
    
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null; i++)
	if (e != par)
	    dfs_cutval(aghead(e), e);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null; i++)
	if (e != par)
	    dfs_cutval(agtail(e), e);
    if (par!=null)
	x_cutval(par);
} finally {
LEAVING("ah65iqmwa5j0qwotm6amhijlg","dfs_cutval");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_range", key="cgqr48qol9p8bsqjnryo5z5x9", definition="static int dfs_range(node_t * v, edge_t * par, int low)")
public static int dfs_range(ST_Agnode_s v, ST_Agedge_s par, int low) {
ENTERING("cgqr48qol9p8bsqjnryo5z5x9","dfs_range");
try {
    ST_Agedge_s e;
    int i, lim;
    
    lim = low;
    ND_par(v, par);
    ND_low(v, low);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null; i++)
	if (e != par)
	    lim = dfs_range(aghead(e), e, lim);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null; i++)
	if (e != par)
	    lim = dfs_range(agtail(e), e, lim);
    ND_lim(v, lim);
    return lim + 1;
} finally {
LEAVING("cgqr48qol9p8bsqjnryo5z5x9","dfs_range");
}
}


}
