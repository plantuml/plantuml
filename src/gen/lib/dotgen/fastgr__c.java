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
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ED_count;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.GD_has_flat_edges;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.M_aghead;
import static smetana.core.Macro.M_agtail;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_prev;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.UNSURE_ABOUT;
import static smetana.core.Macro.VIRTUAL;
import static smetana.core.Macro.alloc_elist;
import static smetana.core.Macro.elist_append;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Todo;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agedgepair_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_elist;

public class fastgr__c {


/*
 * operations on the fast internal graph.
 */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="econbrl314rr46qnvvw5e32j7", definition="static edge_t *ffe(node_t * u, elist uL, node_t * v, elist vL)")
public static ST_Agedge_s ffe(ST_Agnode_s u, final ST_elist uL, ST_Agnode_s v, final ST_elist vL) {
// WARNING!! STRUCT
return ffe_w_(u, uL.copy(), v, vL.copy());
}
private static ST_Agedge_s ffe_w_(ST_Agnode_s u, final ST_elist uL, ST_Agnode_s v, final ST_elist vL) {
ENTERING("econbrl314rr46qnvvw5e32j7","ffe");
try {
    int i;
    ST_Agedge_s e = null;
    
    if ((uL.size > 0) && (vL.size > 0)) {
	if (uL.size < vL.size) {
	    for (i = 0; (e = (ST_Agedge_s) uL.list.get_(i))!=null; i++)
		if (EQ(aghead(e), v))
		    break;
	} else {
	    for (i = 0; (e = (ST_Agedge_s) vL.list.get_(i))!=null; i++)
		if (EQ(agtail(e), u))
		    break;
	}
    } else
	e = null;
    return e;
} finally {
LEAVING("econbrl314rr46qnvvw5e32j7","ffe");
}
}



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="1uygfrgur73lfy9vsjozwwupm", definition="edge_t *find_fast_edge(node_t * u, node_t * v)")
public static ST_Agedge_s find_fast_edge(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("1uygfrgur73lfy9vsjozwwupm","find_fast_edge");
try {
    return ffe(u, ND_out(u), v, ND_in(v));
} finally {
LEAVING("1uygfrgur73lfy9vsjozwwupm","find_fast_edge");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="find_fast_node", key="1yw7ahdnxnexnicj552zqyyej", definition="static node_t* find_fast_node(graph_t * g, node_t * n)")
public static ST_Agnode_s find_fast_node(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("1yw7ahdnxnexnicj552zqyyej","find_fast_node");
try {
    ST_Agnode_s v;
    for (v = GD_nlist(g); v!=null; v = ND_next(v))
	if (EQ(v, n))
	    break;
    return v;
} finally {
LEAVING("1yw7ahdnxnexnicj552zqyyej","find_fast_node");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="bf1j97keudu416avridkj9fpb", definition="edge_t *find_flat_edge(node_t * u, node_t * v)")
public static ST_Agedge_s find_flat_edge(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("bf1j97keudu416avridkj9fpb","find_flat_edge");
try {
    return ffe(u, ND_flat_out(u), v, ND_flat_in(v));
} finally {
LEAVING("bf1j97keudu416avridkj9fpb","find_flat_edge");
}
}



@Todo(what = "Strange that elist_append(e, (*L)) is never called")
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="safe_list_append", key="cttswsffgmw1g710jzvdd3wzn", definition="static void  safe_list_append(edge_t * e, elist * L)")
public static void safe_list_append(ST_Agedge_s e, ST_elist L) {
ENTERING("cttswsffgmw1g710jzvdd3wzn","safe_list_append");
try {
     int i;
     
     for (i = 0; i < L.size; i++)
     if (EQ(e, L.list.get_(i)))
 	 	return;
     UNSUPPORTED("cslejjtgepjdwlcykfas4fmvz"); //     elist_append(e, (*L));
} finally {
LEAVING("cttswsffgmw1g710jzvdd3wzn","safe_list_append");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="8t6gpubo908pz1pqnt1s88lnt", definition="edge_t *fast_edge(edge_t * e)")
public static ST_Agedge_s fast_edge(ST_Agedge_s e) {
ENTERING("8t6gpubo908pz1pqnt1s88lnt","fast_edge");
try {
    elist_append(e, ND_out(agtail(e)));
    elist_append(e, ND_in(aghead(e)));
    return e;
} finally {
LEAVING("8t6gpubo908pz1pqnt1s88lnt","fast_edge");
}
}




//3 dxb0q8ajb7iv25aj6zdqnbwh5
// void zapinlist(elist * L, edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="zapinlist", key="dxb0q8ajb7iv25aj6zdqnbwh5", definition="void zapinlist(elist * L, edge_t * e)")
public static void zapinlist(ST_elist L, ST_Agedge_s e) {
ENTERING("dxb0q8ajb7iv25aj6zdqnbwh5","zapinlist");
try {
    int i;
    for (i = 0; i < L.size; i++) {
	if (EQ(L.list.get_(i), e)) {
	    L.size = L.size-1;
	    L.list.set_(i, L.list.get_(L.size));
	    L.list.set_(L.size, null);
	    break;
	}
    }
} finally {
LEAVING("dxb0q8ajb7iv25aj6zdqnbwh5","zapinlist");
}
}




//3 dkv97rr4ytpehp291etaxe9gc
// void delete_fast_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="delete_fast_edge", key="dkv97rr4ytpehp291etaxe9gc", definition="void delete_fast_edge(edge_t * e)")
public static void delete_fast_edge(ST_Agedge_s e) {
ENTERING("dkv97rr4ytpehp291etaxe9gc","delete_fast_edge");
try {
    //assert(e != NULL);
    zapinlist((ND_out(agtail(e))), e);
    zapinlist((ND_in(aghead(e))), e);
} finally {
LEAVING("dkv97rr4ytpehp291etaxe9gc","delete_fast_edge");
}
}





@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="other_edge", key="73oebfcfiescklohgt8mddswc", definition="void other_edge(edge_t * e)")
public static void other_edge(ST_Agedge_s e) {
ENTERING("73oebfcfiescklohgt8mddswc","other_edge");
try {
    elist_append(e, ND_other(agtail(e)));
} finally {
LEAVING("73oebfcfiescklohgt8mddswc","other_edge");
}
}




@Difficult
@Reviewed(when = "15/11/2020")
@Todo(what = "Review &(ND_other(agtail(e))")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="safe_other_edge", key="4zg1fp1b7bhnx2tbeaij8yeel", definition="void safe_other_edge(edge_t * e)")
public static void safe_other_edge(ST_Agedge_s e) {
ENTERING("4zg1fp1b7bhnx2tbeaij8yeel","safe_other_edge");
try {
	UNSURE_ABOUT("safe_list_append(e, &(ND_other(agtail(e))));");
	// Review &(ND_other(agtail(e))
    safe_list_append(e, ND_other(agtail(e)));	
} finally {
LEAVING("4zg1fp1b7bhnx2tbeaij8yeel","safe_other_edge");
}
}



/* new_virtual_edge:
 * Create and return a new virtual edge e attached to orig.
 * ED_to_orig(e) = orig
 * ED_to_virt(orig) = e if e is the first virtual edge attached.
 * orig might be an input edge, reverse of an input edge, or virtual edge
 */
@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="4gd9tmpq70q0rij5otj0k6sn2", definition="edge_t *new_virtual_edge(node_t * u, node_t * v, edge_t * orig)")
public static ST_Agedge_s new_virtual_edge(ST_Agnode_s u, ST_Agnode_s v, ST_Agedge_s orig) {
ENTERING("4gd9tmpq70q0rij5otj0k6sn2","new_virtual_edge");
try {
    ST_Agedge_s e;
    
    ST_Agedgepair_s e2 = new ST_Agedgepair_s();
    AGTYPE(e2.in, AGINEDGE);
    AGTYPE(e2.out, AGOUTEDGE);
    e2.out.base.data = new ST_Agedgeinfo_t();
    e = e2.out;
    M_agtail(e, u);
    M_aghead(e, v);
    ED_edge_type(e, VIRTUAL);
    
    
    if (orig!=null) {
	AGSEQ(e, AGSEQ(orig));
	AGSEQ(e2.in, AGSEQ(orig));
	ED_count(e, ED_count(orig));
	ED_xpenalty(e, ED_xpenalty(orig));
	ED_weight(e, ED_weight(orig));
	ED_minlen(e, ED_minlen(orig));
	if (EQ(agtail(e), agtail(orig)))
	    ED_tail_port(e, ED_tail_port(orig));
	else if (EQ(agtail(e), aghead(orig)))
	    ED_tail_port(e, ED_head_port(orig));
	if (EQ(aghead(e), aghead(orig)))
	    ED_head_port(e, ED_head_port(orig));
	else if (EQ(aghead(e), agtail(orig)))
	    ED_head_port(e, ED_tail_port(orig));
	
	
	if (ED_to_virt(orig) == null)
	    ED_to_virt(orig, e);
	ED_to_orig(e, orig);
    } else {
	ED_minlen(e, 1);
	ED_count(e, 1);
	ED_xpenalty(e, 1);
	ED_weight(e, 1);
	}
    return e;
} finally {
LEAVING("4gd9tmpq70q0rij5otj0k6sn2","new_virtual_edge");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="9obdfflzw4cs2z9r0dng26mvw", definition="edge_t *virtual_edge(node_t * u, node_t * v, edge_t * orig)")
public static ST_Agedge_s virtual_edge(ST_Agnode_s u, ST_Agnode_s v, ST_Agedge_s orig) {
ENTERING("9obdfflzw4cs2z9r0dng26mvw","virtual_edge");
try {
    return fast_edge(new_virtual_edge(u, v, orig));
} finally {
LEAVING("9obdfflzw4cs2z9r0dng26mvw","virtual_edge");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="fast_node", key="98hkec8t6fjk10bjpstumw0ey", definition="void fast_node(graph_t * g, Agnode_t * n)")
public static void fast_node(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("98hkec8t6fjk10bjpstumw0ey","fast_node");
try {
    ND_next(n, GD_nlist(g));
    if (ND_next(n)!=null)
	ND_prev(ND_next(n), n);
    GD_nlist(g, n);
    ND_prev(n, null);
    assert(NEQ(n, ND_next(n)));
} finally {
LEAVING("98hkec8t6fjk10bjpstumw0ey","fast_node");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="delete_fast_node", key="emsq7b6s5100lscckzy3ileqd", definition="void delete_fast_node(graph_t * g, node_t * n)")
public static void delete_fast_node(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("emsq7b6s5100lscckzy3ileqd","delete_fast_node");
try {
    assert(find_fast_node(g, n)!=null);
    if (ND_next(n)!=null)
	ND_prev(ND_next(n), ND_prev(n));
    if (ND_prev(n)!=null)
	ND_next(ND_prev(n), ND_next(n));
    else
	GD_nlist(g, ND_next(n));
} finally {
LEAVING("emsq7b6s5100lscckzy3ileqd","delete_fast_node");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="eg08ajzojsm0224btmfi7kdxt", definition="node_t *virtual_node(graph_t * g)")
public static ST_Agnode_s virtual_node(ST_Agraph_s g) {
ENTERING("eg08ajzojsm0224btmfi7kdxt","virtual_node");
try {
	ST_Agnode_s n;
	
    n = new ST_Agnode_s();
//  agnameof(n) = "virtual";
    AGTYPE(n, AGNODE);
    n.base.data = new ST_Agnodeinfo_t();
    n.root = agroot(g);
    ND_node_type(n, VIRTUAL);
    ND_rw(n, 1); ND_lw(n, 1);
    ND_ht(n, 1);
    ND_UF_size(n, 1);
    alloc_elist(4, ND_in(n));
    alloc_elist(4, ND_out(n));
    fast_node(g, n);
    GD_n_nodes(g, GD_n_nodes(g)+1);
    return n;
} finally {
LEAVING("eg08ajzojsm0224btmfi7kdxt","virtual_node");
}
}




@Difficult
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="flat_edge", key="8dvukicq96g5t3xgdl0ue35mj", definition="void flat_edge(graph_t * g, edge_t * e)")
public static void flat_edge(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("8dvukicq96g5t3xgdl0ue35mj","flat_edge");
try {
    elist_append(e, ND_flat_out(agtail(e)));
    elist_append(e, ND_flat_in(aghead(e)));
    GD_has_flat_edges(g, true);
    GD_has_flat_edges(dot_root(g), true);
} finally {
LEAVING("8dvukicq96g5t3xgdl0ue35mj","flat_edge");
}
}




//3 clspalhiuedfnk9g9rlvfqpg7
// void delete_flat_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="delete_flat_edge", key="clspalhiuedfnk9g9rlvfqpg7", definition="void delete_flat_edge(edge_t * e)")
public static void delete_flat_edge(ST_Agedge_s e) {
ENTERING("clspalhiuedfnk9g9rlvfqpg7","delete_flat_edge");
try {
    assert(e != null);
    if (ED_to_orig(e)!=null && EQ(ED_to_virt(ED_to_orig(e)), e))
	ED_to_virt(ED_to_orig(e), null);
    zapinlist((ND_flat_out(agtail(e))), e);
    zapinlist((ND_flat_in(aghead(e))), e);
} finally {
LEAVING("clspalhiuedfnk9g9rlvfqpg7","delete_flat_edge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="basic_merge", key="dcfpol11cvlt6aaa6phqbp6fo", definition="static void  basic_merge(edge_t * e, edge_t * rep)")
public static void basic_merge(ST_Agedge_s e, ST_Agedge_s rep) {
ENTERING("dcfpol11cvlt6aaa6phqbp6fo","basic_merge");
try {
    if (ED_minlen(rep) < ED_minlen(e))
	ED_minlen(rep, ED_minlen(e));
    while (rep!=null) {
	ED_count(rep, ED_count(rep) + ED_count(e));
	ED_xpenalty(rep, ED_xpenalty(rep) + ED_xpenalty(e));
	ED_weight(rep, ED_weight(rep) + ED_weight(e));
	rep = ED_to_virt(rep);
    }
} finally {
LEAVING("dcfpol11cvlt6aaa6phqbp6fo","basic_merge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="merge_oneway", key="6dxgtoii76tmonlnvz4rmiytd", definition="void  merge_oneway(edge_t * e, edge_t * rep)")
public static void merge_oneway(ST_Agedge_s e, ST_Agedge_s rep) {
ENTERING("6dxgtoii76tmonlnvz4rmiytd","merge_oneway");
try {
    if (EQ(rep, ED_to_virt(e))) {
UNSUPPORTED("84xxsh1cgsif42hgizyxw36ul"); // 	agerr(AGWARN, "merge_oneway glitch\n");
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    //assert(ED_to_virt(e) == NULL);
    ED_to_virt(e, rep);
    basic_merge(e, rep);
} finally {
LEAVING("6dxgtoii76tmonlnvz4rmiytd","merge_oneway");
}
}




}
