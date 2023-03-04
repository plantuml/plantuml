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
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.routespl__c.makeStraightEdge;
import static gen.lib.common.routespl__c.routepolylines;
import static gen.lib.common.routespl__c.routesplines;
import static gen.lib.common.routespl__c.routesplinesinit;
import static gen.lib.common.routespl__c.routesplinesterm;
import static gen.lib.common.routespl__c.simpleSplineRoute;
import static gen.lib.common.splines__c.add_box;
import static gen.lib.common.splines__c.beginpath;
import static gen.lib.common.splines__c.clip_and_install;
import static gen.lib.common.splines__c.endpath;
import static gen.lib.common.splines__c.getsplinepoints;
import static gen.lib.common.splines__c.makeSelfEdge;
import static gen.lib.common.utils__c.updateBB;
import static gen.lib.dotgen.cluster__c.mark_lowclusters;
import static h.ST_pointf.add_pointf;
import static h.ST_pointf.pointfof;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.qsort;
import static smetana.core.Macro.BETWEEN;
import static smetana.core.Macro.BOTTOM;
import static smetana.core.Macro.EDGETYPEMASK;
import static smetana.core.Macro.EDGE_LABEL;
import static smetana.core.Macro.ED_adjacent;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_tree_index;
import static smetana.core.Macro.ET_CURVED;
import static smetana.core.Macro.ET_LINE;
import static smetana.core.Macro.ET_NONE;
import static smetana.core.Macro.ET_SPLINE;
import static smetana.core.Macro.FLATEDGE;
import static smetana.core.Macro.FLATORDER;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_nodesep;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.GVSPLINES;
import static smetana.core.Macro.IGNORED;
import static smetana.core.Macro.MAKEFWDEDGE;
import static smetana.core.Macro.M_PI;
import static smetana.core.Macro.ND_alg;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_mval;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.REGULAREDGE;
import static smetana.core.Macro.ROUND;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.VIRTUAL;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agedgepair_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Ppoly_t;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_path;
import h.ST_pathend_t;
import h.ST_pointf;
import h.ST_port;
import h.ST_rank_t;
import h.ST_spline_info_t;
import h.ST_splines;
import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.Globals;
import smetana.core.Memory;
import smetana.core.ZType;
import smetana.core.__ptr__;

public class dotsplines__c {




//3 1vvsta5i8of59frav6uymguav
// static inline boxf boxfof(double llx, double lly, double urx, double ury) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="boxfof", key="1vvsta5i8of59frav6uymguav", definition="static inline boxf boxfof(double llx, double lly, double urx, double ury)")
public static ST_boxf boxfof(double llx, double lly, double urx, double ury) {
// WARNING!! STRUCT
return boxfof_w_(llx, lly, urx, ury).copy();
}
private static ST_boxf boxfof_w_(double llx, double lly, double urx, double ury) {
ENTERING("1vvsta5i8of59frav6uymguav","boxfof");
try {
    final ST_boxf b = new ST_boxf();
    b.LL.x = llx;
    b.LL.y = lly;
    b.UR.x = urx;
    b.UR.y = ury;
    return b;
} finally {
LEAVING("1vvsta5i8of59frav6uymguav","boxfof");
}
}



//3 dobhmc46zwtvv8rg3ywntl91j
// static edge_t* getmainedge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="getmainedge", key="dobhmc46zwtvv8rg3ywntl91j", definition="static edge_t* getmainedge(edge_t * e)")
public static ST_Agedge_s getmainedge(ST_Agedge_s e) {
ENTERING("dobhmc46zwtvv8rg3ywntl91j","getmainedge");
try {
    ST_Agedge_s le = e;
    while (ED_to_virt(le)!=null)
	le = ED_to_virt(le);
    while (ED_to_orig(le)!=null)
	le = ED_to_orig(le);
    return le;
} finally {
LEAVING("dobhmc46zwtvv8rg3ywntl91j","getmainedge");
}
}




public static CFunction spline_merge = new CFunctionAbstract("spline_merge") {
	
	public Object exe(Globals zz, Object... args) {
		return spline_merge((ST_Agnode_s)args[0]);
	}};
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="spline_merge", key="ddeny5ht7w8b16ztj5zt840ld", definition="static boolean spline_merge(node_t * n)")
public static boolean spline_merge(ST_Agnode_s n) {
ENTERING("ddeny5ht7w8b16ztj5zt840ld","spline_merge");
try {
    return ((ND_node_type(n) == 1)
	    && ((ND_in(n).size > 1) || (ND_out(n).size > 1)));
} finally {
LEAVING("ddeny5ht7w8b16ztj5zt840ld","spline_merge");
}
}




public static CFunction swap_ends_p = new CFunctionAbstract("swap_ends_p") {
	
	public Object exe(Globals zz, Object... args) {
		return swap_ends_p((ST_Agedge_s)args[0]);
	}};
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="swap_ends_p", key="36ofw2qfqlh5ci8gc8cfkqgg3", definition="static boolean swap_ends_p(edge_t * e)")
public static boolean swap_ends_p(ST_Agedge_s e) {
ENTERING("36ofw2qfqlh5ci8gc8cfkqgg3","swap_ends_p");
try {
    while (ED_to_orig(e)!=null)
	e = ED_to_orig(e);
    if (ND_rank(aghead(e)) > ND_rank(agtail(e)))
	return false;
    if (ND_rank(aghead(e)) < ND_rank(agtail(e)))
	return true;
    if (ND_order(aghead(e)) >= ND_order(agtail(e)))
	return false;
    return true;
} finally {
LEAVING("36ofw2qfqlh5ci8gc8cfkqgg3","swap_ends_p");
}
}





//3 3krohso3quojiv4fveh1en7o6
// int portcmp(port p0, port p1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="portcmp", key="3krohso3quojiv4fveh1en7o6", definition="int portcmp(port p0, port p1)")
public static int portcmp(final ST_port p0, final ST_port p1) {
// WARNING!! STRUCT
return portcmp_w_(p0.copy(), p1.copy());
}
private static int portcmp_w_(final ST_port p0, final ST_port p1) {
ENTERING("3krohso3quojiv4fveh1en7o6","portcmp");
try {
    int rv;
    if (p1.defined == false)
	return (p0.defined ? 1 : 0);
    if (p0.defined == false)
	return -1;
    rv = (int) (p0.p.x - p1.p.x);
    if (rv == 0)
	rv = (int) (p0.p.y - p1.p.y);
    return rv;
} finally {
LEAVING("3krohso3quojiv4fveh1en7o6","portcmp");
}
}




//3 10wbtt4gwnxgqutinpj4ymjpk
// static void swap_bezier(bezier * old, bezier * new) 
@Unused
@Difficult
@Reviewed(when = "16/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="swap_bezier", key="10wbtt4gwnxgqutinpj4ymjpk", definition="static void swap_bezier(bezier * old, bezier * new)")
public static void swap_bezier(CArray<ST_bezier> old, CArray<ST_bezier> new_) {
ENTERING("10wbtt4gwnxgqutinpj4ymjpk","swap_bezier");
try {
	CArray<ST_pointf> list;
	CArray<ST_pointf> lp;
	CArray<ST_pointf> olp;
    int i, sz;
    
    sz = old.get__(0).size;
	list = CArray.<ST_pointf>ALLOC__(sz, ZType.ST_pointf);
    lp = list;
    olp = old.get__(0).list.plus_(sz - 1);
    for (i = 0; i < sz; i++) {	/* reverse list of points */
	lp.get__(0).___(olp.get__(0));
	lp=lp.plus_(1);
	olp=olp.plus_(-1);
    }
    
    new_.get__(0).list = list;
    new_.get__(0).size = sz;
    new_.get__(0).sflag = old.get__(0).eflag;
    new_.get__(0).eflag = old.get__(0).sflag;
    new_.get__(0).sp.___(old.get__(0).ep);
    new_.get__(0).ep.___(old.get__(0).sp);
} finally {
LEAVING("10wbtt4gwnxgqutinpj4ymjpk","swap_bezier");
}
}




//3 3256l3e2huarsy29dd97vqj85
// static void swap_spline(splines * s) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="swap_spline", key="3256l3e2huarsy29dd97vqj85", definition="static void swap_spline(splines * s)")
public static void swap_spline(ST_splines s) {
ENTERING("3256l3e2huarsy29dd97vqj85","swap_spline");
try {
	CArray<ST_bezier> list;
	CArray<ST_bezier> lp;
	CArray<ST_bezier> olp;
    int i, sz;
    sz = s.size;
    list = CArray.<ST_bezier>ALLOC__(sz, ZType.ST_bezier);
	//list = new CStar<ST_bezier>(sz, ST_bezier.class);
    lp = list;
    olp = s.list.plus_(sz - 1);
    for (i = 0; i < sz; i++) {	/* reverse and swap list of beziers */
	swap_bezier(olp, lp);
	olp = olp.plus_(-1);
	lp = lp.plus_(1);
    }
    /* free old structures */
    for (i = 0; i < sz; i++)
	Memory.free(s.list.get__(i).list);
    Memory.free(s.list);
    s.list = list;
} finally {
LEAVING("3256l3e2huarsy29dd97vqj85","swap_spline");
}
}




//3 dgkssqjj566ifra0xy7m46qsb
// static void edge_normalize(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="edge_normalize", key="dgkssqjj566ifra0xy7m46qsb", definition="static void edge_normalize(graph_t * g)")
public static void edge_normalize(Globals zz, ST_Agraph_s g) {
ENTERING("dgkssqjj566ifra0xy7m46qsb","edge_normalize");
try {
    ST_Agedge_s e;
    ST_Agnode_s n;
    for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n)) {
	for (e = agfstout(zz, g, n); e!=null; e = agnxtout(zz, g, e)) {
	    if ((Boolean)zz.sinfo.swapEnds.exe(zz, e) && ED_spl(e)!=null)
		swap_spline(ED_spl(e));
	}
    }
} finally {
LEAVING("dgkssqjj566ifra0xy7m46qsb","edge_normalize");
}
}




//3 bwzdgdea9suuagzueyw8ztx42
// static void resetRW (graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="resetRW", key="bwzdgdea9suuagzueyw8ztx42", definition="static void resetRW (graph_t * g)")
public static Object resetRW(Object... arg_) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("c4vqsmfolc1meewxoebfkyppx"); // resetRW (graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ma9kdgag30w5ofv1niitxbro"); //     node_t* n;
UNSUPPORTED("7wq24g054kmx3aw25vk5ksj4"); //     for (n = agfstnode(g); n; n = agnxtnode(g,n)) {
UNSUPPORTED("b83f20tdode2lz5a49mhmn9ei"); // 	if (ND_other(n).list) {
UNSUPPORTED("ez6pf4w4vi7z6fqq43v5i3gpv"); // 	    double tmp = ND_rw(n);
UNSUPPORTED("24qsh566odunv14qzj2zan7bz"); // 	    ND_rw(n) = ND_mval(n);
UNSUPPORTED("cgxuqwflawrd9e6dmnsh3sbwa"); // 	    ND_mval(n) = tmp;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




private final static int NSUB = 9;
private final static int CHUNK = 128;

private final static int MINW = 16;
private final static int HALFMINW = 8;

private final static int FWDEDGE = 16;
private final static int BWDEDGE = 32;

private final static int MAINGRAPH = 64;
private final static int AUXGRAPH = 128;


/* _dot_splines:
 * Main spline routing code.
 * The normalize parameter allows this function to be called by the
 * recursive call in make_flat_edge without normalization occurring,
 * so that the edge will only be normalized once in the top level call
 * of dot_splines.
 */
//3 6agx6m2qof9lg57co232lwakj
// static void _dot_splines(graph_t * g, int normalize) 
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="_dot_splines", key="6agx6m2qof9lg57co232lwakj", definition="static void _dot_splines(graph_t * g, int normalize)")
static void _dot_splines(Globals zz, ST_Agraph_s g, int normalize)
{
ENTERING("6agx6m2qof9lg57co232lwakj","_dot_splines");
try {
    int i, j, k, n_nodes, n_edges, ind, cnt;
    ST_Agnode_s n;
    final ST_Agedgeinfo_t fwdedgeai = new ST_Agedgeinfo_t(), fwdedgebi = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedgea = new ST_Agedgepair_s(), fwdedgeb = new ST_Agedgepair_s();
    ST_Agedge_s e, e0, e1, ea, eb, le0, le1;
    CArrayOfStar<ST_Agedge_s> edges;
    ST_path P;
    final ST_spline_info_t sd = new ST_spline_info_t();
    int et = (GD_flags(g) & (7 << 1));
    fwdedgea.out.base.data = fwdedgeai;
    fwdedgeb.out.base.data = fwdedgebi;
    
    if (et == ET_NONE) return; 

    if (et == ET_CURVED) {
	resetRW (g);
	if ((GD_has_labels(g) & EDGE_LABEL)!=0) {
UNSUPPORTED("4k888z8ymdp2b653twxc1ugbu"); // 	    agerr (AGWARN, "edge labels with splines=curved not supported in dot - use xlabels\n");
	}
	for (n = agfstnode (zz, g); n!=null; n = agnxtnode(zz, g, n)) {
	    for (e = agfstout(zz, g, n); e!=null; e = agnxtout(zz, g,e)) {
		makeStraightEdge(g, e, et, zz.sinfo);
	    }
	}
UNSUPPORTED("46btiag50nczzur103eqhjcup"); // 	goto finish;
    } 
    
    
    mark_lowclusters(zz, g);
    if (routesplinesinit(zz)!=0) return;
    P = new ST_path();
    /* FlatHeight = 2 * GD_nodesep(g); */
    sd.Splinesep = GD_nodesep(g) / 4;
    sd.Multisep = GD_nodesep(g);
    edges = CArrayOfStar.<ST_Agedge_s>ALLOC(CHUNK, ZType.ST_Agedge_s);
    
    /* compute boundaries and list of splines */
    sd.RightBound = 0;
    sd.LeftBound = 0;
    n_edges = n_nodes = 0;
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
	n_nodes += GD_rank(g).get__(i).n;
	if ((n = GD_rank(g).get__(i).v.get_(0))!=null)
	    sd.LeftBound = (int)Math.min(sd.LeftBound, (ND_coord(n).x - ND_lw(n)));
	if (GD_rank(g).get__(i).n!=0 && (n = GD_rank(g).get__(i).v.get_(GD_rank(g).get__(i).n - 1))!=null)
	    sd.RightBound = (int)Math.max(sd.RightBound, (ND_coord(n).x + ND_rw(n)));
	sd.LeftBound -= MINW;
	sd.RightBound += MINW;
	
	for (j = 0; j < GD_rank(g).get__(i).n; j++) {
	    n = GD_rank(g).get__(i).v.get_(j);
		/* if n is the label of a flat edge, copy its position to
		 * the label.
		 */
	    if (ND_alg(n)!=null) {
		ST_Agedge_s fe = (ST_Agedge_s) ND_alg(n);
		assert (ED_label(fe)!=null);
		ED_label(fe).pos.___(ND_coord(n));
		ED_label(fe).set= false ? 0 : 1;
	    }
	    if ((ND_node_type(n) != NORMAL) &&
		((Boolean)zz.sinfo.splineMerge.exe(zz, n) == false))
		continue;
	    for (k = 0; (e = ND_out(n).list.get_(k))!=null; k++) {
		if ((ED_edge_type(e) == FLATORDER)
		    || (ED_edge_type(e) == IGNORED))
		    continue;
		setflags(e, REGULAREDGE, FWDEDGE, MAINGRAPH);
		edges.set_(n_edges++, e);
		if (n_edges % CHUNK == 0)
		    edges = CArrayOfStar.<ST_Agedge_s>REALLOC(n_edges + CHUNK, edges, ZType.ST_Agedge_s);
	    }
	    if (ND_flat_out(n).list!=null)
		for (k = 0; (e = ND_flat_out(n).list.get_(k))!=null; k++) {
		    setflags(e, FLATEDGE, 0, AUXGRAPH);
			edges.set_(n_edges++, e);
		    if (n_edges % CHUNK == 0)
			    edges = CArrayOfStar.<ST_Agedge_s>REALLOC(n_edges + CHUNK, edges, ZType.ST_Agedge_s);
		}
	    if (ND_other(n).list!=null) {
		/* In position, each node has its rw stored in mval and,
                 * if a node is part of a loop, rw may be increased to
                 * reflect the loops and associated labels. We restore
                 * the original value here. 
                 */
		if (ND_node_type(n) == NORMAL) {
		    double tmp = ND_rw(n);
		    ND_rw(n, ND_mval(n));
		    ND_mval(n, tmp);
		}
		for (k = 0; (e = ND_other(n).list.get_(k))!=null; k++) {
		    setflags(e, 0, 0, AUXGRAPH);
			edges.set_(n_edges++, e);
		    if (n_edges % CHUNK == 0)
			    edges = CArrayOfStar.<ST_Agedge_s>REALLOC(n_edges + CHUNK, edges, ZType.ST_Agedge_s);
		}
	    }
	}
    }
    /* Sort so that equivalent edges are contiguous. 
     * Equivalence should basically mean that 2 edges have the
     * same set {(tailnode,tailport),(headnode,headport)}, or
     * alternatively, the edges would be routed identically if
     * routed separately.
     */
    LOG2("_dot_splines::n_edges="+n_edges);
    qsort(zz, edges, n_edges,
    dotsplines__c.edgecmp);
    

    /* FIXME: just how many boxes can there be? */
    P.boxes = ST_boxf.malloc(n_nodes + 20 * 2 * NSUB);
    sd.Rank_box = ST_boxf.malloc(i);
    
    if (et == ET_LINE) {
    /* place regular edge labels */
	for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	    if ((ND_node_type(n) == VIRTUAL) && (ND_label(n)!=null)) {
		place_vnlabel(n);
	    }
	}
    }
    
    for (i = 0; i < n_edges;) {
 	boolean havePorts;
	ind = i;
	le0 = getmainedge((e0 = edges.get_(i++)));
	if (ED_tail_port(e0).defined || ED_head_port(e0).defined) {
	    havePorts = true;
	    ea = e0;
	} else {
	    havePorts = false;
	    ea =  le0;
	}
	if ((ED_tree_index(ea) & BWDEDGE)!=0) {
	    MAKEFWDEDGE(fwdedgea.out, ea);
	    ea = fwdedgea.out;
	}

	for (cnt = 1; i < n_edges; cnt++, i++) {
	    if ((le0 != (le1 = getmainedge((e1 = edges.get_(i))))))
		break;
	    if (ED_adjacent(e0)!=0) continue; /* all flat adjacent edges at once */
	    if (ED_tail_port(e1).defined || ED_head_port(e1).defined) {
		if (!havePorts) break;
		else
		    eb = e1;
	    } else {
		if (havePorts) break;
		else
		    eb = le1;
	    }
	    if ((ED_tree_index(eb) & BWDEDGE)!=0) {
		MAKEFWDEDGE(fwdedgeb.out, eb);
		eb = fwdedgeb.out;
	    }
	    if (portcmp(ED_tail_port(ea), ED_tail_port(eb))!=0)
		break;
	    if (portcmp(ED_head_port(ea), ED_head_port(eb))!=0)
		break;
	    if ((ED_tree_index(e0) & EDGETYPEMASK) == FLATEDGE
		&& (ED_label(e0) != ED_label(e1)))
		break;
	    if ((ED_tree_index(edges.get_(i)) & MAINGRAPH)!=0)	/* Aha! -C is on */
		break;
	}
	
	if (agtail(e0) == aghead(e0)) {
	    int b, sizey, r;
	    n = agtail(e0);
	    r = ND_rank(n);
	    if (r == GD_maxrank(g)) {
		if (r > 0)
		    sizey = (int) (ND_coord(GD_rank(g).get__(r-1).v.get_(0)).y - ND_coord(n).y);
		else
		    sizey = (int) ND_ht(n);
	    }
	    else if (r == GD_minrank(g)) {
		sizey = (int)(ND_coord(n).y - ND_coord(GD_rank(g).get__(r+1).v.get_(0)).y);
	    }
	    else {
		int upy = (int) (ND_coord(GD_rank(g).get__(r-1).v.get_(0)).y - ND_coord(n).y);
		int dwny = (int) (ND_coord(n).y - ND_coord(GD_rank(g).get__(r+1).v.get_(0)).y);
		sizey = Math.min(upy, dwny);
	    }
	    makeSelfEdge(zz, P, edges, ind, cnt, sd.Multisep, sizey/2, zz.sinfo);
	    for (b = 0; b < cnt; b++) {
		e = edges.get_(ind+b);
		if (ED_label(e)!=null)
		    updateBB(g, ED_label(e));
	    }
	}
	else if (ND_rank(agtail(e0)) == ND_rank(aghead(e0))) {
	    make_flat_edge(zz, g, sd, P, edges, ind, cnt, et);
	}
	else
	    make_regular_edge(zz, g, sd, P, edges, ind, cnt, et);
    }
    /* place regular edge labels */
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if ((ND_node_type(n) == VIRTUAL) && (ND_label(n))!=null) {
	    place_vnlabel(n);
	    updateBB(g, ND_label(n));
	}
    }

    /* normalize splines so they always go from tail to head */
    /* place_portlabel relies on this being done first */
    if (normalize!=0)
	edge_normalize(zz, g);
finish :
    /* vladimir: place port labels */
    /* FIX: head and tail labels are not part of cluster bbox */
    if ((zz.E_headlabel!=null || zz.E_taillabel!=null) && (zz.E_labelangle!=null || zz.E_labeldistance!=null)) {
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("54jm4bbgzyl0txauszo1mappu"); // 	    if (E_headlabel) {
UNSUPPORTED("dw8yu56thd0wpolmtby8r5doo"); // 		for (e = agfstin(g, n); e; e = agnxtin(g, e))
UNSUPPORTED("4h39n1vkqyqszgmg2o7d2pw7z"); // 		    if (ED_head_label(AGMKOUT(e))) {
UNSUPPORTED("ex7h0x60y0l4oi3dsqg7sfk1t"); // 			place_portlabel(AGMKOUT(e), NOT(0));
UNSUPPORTED("9u960ou1xs0cdsdn0qe8a13ye"); // 			updateBB(g, ED_head_label(AGMKOUT(e)));
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("cqi8doquf015wo1bw8on36qct"); // 	    if (E_taillabel) {
UNSUPPORTED("6gnq0yj07udpwxbuc86k8yysb"); // 		for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("ejz8yrj8mh0l0gdl5zeaht1ex"); // 		    if (ED_tail_label(e)) {
UNSUPPORTED("9pdg7peez077ldl84zfh73o1w"); // 			if (place_portlabel(e, 0))
UNSUPPORTED("6jup6d9gfnx4b1wptmtw09n6w"); // 			    updateBB(g, ED_tail_label(e));
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    /* end vladimir */
    if (et != ET_CURVED) {
	Memory.free(edges);
	Memory.free(P.boxes);
	Memory.free(P);
	Memory.free(sd.Rank_box);
	routesplinesterm(zz);
    } 
    zz.State = GVSPLINES;
    zz.EdgeLabelsDone = 1;
} finally {
LEAVING("6agx6m2qof9lg57co232lwakj","_dot_splines");
}
}




//3 5n306wbdfjbfnimdo9lg6jjaa
// void dot_splines(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="dot_splines", key="5n306wbdfjbfnimdo9lg6jjaa", definition="void dot_splines(graph_t * g)")
public static void dot_splines(Globals zz, ST_Agraph_s g) {
ENTERING("5n306wbdfjbfnimdo9lg6jjaa","dot_splines");
try {
    _dot_splines (zz, g, 1);
} finally {
LEAVING("5n306wbdfjbfnimdo9lg6jjaa","dot_splines");
}
}




//3 8jja9ukzsq8tlb9yy7uzavg91
// static void  place_vnlabel(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="place_vnlabel", key="8jja9ukzsq8tlb9yy7uzavg91", definition="static void  place_vnlabel(node_t * n)")
public static void place_vnlabel(ST_Agnode_s n) {
ENTERING("8jja9ukzsq8tlb9yy7uzavg91","place_vnlabel");
try {
    final ST_pointf dimen = new ST_pointf();
    double width;
    ST_Agedge_s e;
    if (ND_in(n).size == 0)
	return;			/* skip flat edge labels here */
    for (e = (ST_Agedge_s) ND_out(n).list.get_(0); ED_edge_type(e) != 0;
	 e = ED_to_orig(e));
    dimen.___(ED_label(e).dimen);
    width = GD_flip(agraphof(n)) ? dimen.y : dimen.x;
    ED_label(e).pos.x = ND_coord(n).x + width / 2.0;
    ED_label(e).pos.y = ND_coord(n).y;
    ED_label(e).set= false ? 0 : 1;
} finally {
LEAVING("8jja9ukzsq8tlb9yy7uzavg91","place_vnlabel");
}
}




//3 598jn37hjkm7j0kcg2nmdvlwq
// static void  setflags(edge_t *e, int hint1, int hint2, int f3) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="setflags", key="598jn37hjkm7j0kcg2nmdvlwq", definition="static void  setflags(edge_t *e, int hint1, int hint2, int f3)")
public static void setflags(ST_Agedge_s e, int hint1, int hint2, int f3) {
ENTERING("598jn37hjkm7j0kcg2nmdvlwq","setflags");
try {
    int f1, f2;
    if (hint1 != 0)
	f1 = hint1;
    else {
	if (agtail(e) == aghead(e))
	    if (ED_tail_port(e).defined || ED_head_port(e).defined)
		f1 = 4;
	    else
		f1 = 8;
	else if (ND_rank(agtail(e)) == ND_rank(aghead(e)))
	    f1 = 2;
	else
	    f1 = 1;
    }
    if (hint2 != 0)
	f2 = hint2;
    else {
	if (f1 == 1)
	    f2 = (ND_rank(agtail(e)) < ND_rank(aghead(e))) ? 16 : 32;
	else if (f1 == 2)
	    f2 = (ND_order(agtail(e)) < ND_order(aghead(e))) ?  16 : 32;
	else			/* f1 == SELF*EDGE */
	    f2 = 16;
    }
    ED_tree_index(e, (f1 | f2 | f3));
} finally {
LEAVING("598jn37hjkm7j0kcg2nmdvlwq","setflags");
}
}


public static CFunction edgecmp = new CFunctionAbstract("edgecmp") {
	
	public Object exe(Globals zz, Object... args) {
		return edgecmp((CArrayOfStar<ST_Agedge_s>)args[0], (CArrayOfStar<ST_Agedge_s>)args[1]);
	}};

/* edgecmp:
 * lexicographically order edges by
 *  - edge type
 *  - |rank difference of nodes|
 *  - |x difference of nodes|
 *  - id of witness edge for equivalence class
 *  - port comparison
 *  - graph type
 *  - labels if flat edges
 *  - edge id
 */
//3 1nf1s6wkn35ptjn884ii56fh
// static int edgecmp(edge_t** ptr0, edge_t** ptr1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="edgecmp", key="1nf1s6wkn35ptjn884ii56fh", definition="static int edgecmp(edge_t** ptr0, edge_t** ptr1)")
public static int edgecmp(CArrayOfStar<ST_Agedge_s> ptr0, CArrayOfStar<ST_Agedge_s> ptr1) {
ENTERING("1nf1s6wkn35ptjn884ii56fh","edgecmp");
try {
    final ST_Agedgeinfo_t fwdedgeai = new ST_Agedgeinfo_t(), fwdedgebi = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedgea = new ST_Agedgepair_s(), fwdedgeb = new ST_Agedgepair_s();
    ST_Agedge_s e0, e1, ea, eb, le0, le1;
    int et0, et1, v0, v1, rv;
    double t0, t1;
    fwdedgea.out.base.data = fwdedgeai;
    fwdedgeb.out.base.data = fwdedgebi;
    e0 = ptr0.get_(0);
    e1 = ptr1.get_(0);
    et0 = ED_tree_index(e0) & 15;
    et1 = ED_tree_index(e1) & 15;
    if (et0 != et1)
	return (et1 - et0);
    le0 = getmainedge(e0);
    le1 = getmainedge(e1);
    t0 = ND_rank(agtail(le0)) - ND_rank(aghead(le0));
    t1 = ND_rank(agtail(le1)) - ND_rank(aghead(le1));
    v0 = Math.abs((int)t0);   /* ugly, but explicit as to how we avoid equality tests on fp numbers */
    v1 = Math.abs((int)t1);
    if (v0 != v1)
	return (v0 - v1);
    t0 = ND_coord(agtail(le0)).x - ND_coord(aghead(le0)).x;
    t1 = ND_coord(agtail(le1)).x - ND_coord(aghead(le1)).x;
    v0 = Math.abs((int)t0);
    v1 = Math.abs((int)t1);
    if (v0 != v1)
	return (v0 - v1);
    /* This provides a cheap test for edges having the same set of endpoints.  */
    if (le0.tag.seq != le1.tag.seq)
	return (le0.tag.seq - le1.tag.seq);
    ea = (ED_tail_port(e0).defined || ED_head_port(e0).defined) ? e0 : le0;
    if ((ED_tree_index(ea) & 32)!=0) {
	MAKEFWDEDGE(fwdedgea.out, ea);
	ea = (ST_Agedge_s) fwdedgea.out;
    }
    eb = (ED_tail_port(e1).defined || ED_head_port(e1).defined) ? e1 : le1;
    if ((ED_tree_index(eb) & 32)!=0) {
	MAKEFWDEDGE(fwdedgeb.out, eb);
	eb = (ST_Agedge_s) fwdedgeb.out;
    }
    if ((rv = portcmp(ED_tail_port(ea), ED_tail_port(eb)))!=0)
	return rv;
    if ((rv = portcmp(ED_head_port(ea), ED_head_port(eb)))!=0)
	return rv;
    et0 = ED_tree_index(e0) & 192;
    et1 = ED_tree_index(e1) & 192;
    if (et0 != et1)
	return (et0 - et1);
    if (et0 == 2 && (ED_label(e0) != ED_label(e1)))
	 UNSUPPORTED("return (int) (ED_label(e0) - ED_label(e1))");
    return (e0.tag.seq - e1.tag.seq);
} finally {
LEAVING("1nf1s6wkn35ptjn884ii56fh","edgecmp");
}
}



public static CFunction edgelblcmpfn = new CFunctionAbstract("edgelblcmpfn") {
	
	public Object exe(Globals zz, Object... args) {
		return edgelblcmpfn((CArrayOfStar<ST_Agedge_s>)args[0], (CArrayOfStar<ST_Agedge_s>)args[1]);
	}};

@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="edgelblcmpfn", key="bmsa24i3avg14po4sp17yh89k", definition="static int edgelblcmpfn(edge_t** ptr0, edge_t** ptr1)")
public static int edgelblcmpfn(CArrayOfStar<ST_Agedge_s> ptr0, CArrayOfStar<ST_Agedge_s> ptr1) {
ENTERING("bmsa24i3avg14po4sp17yh89k","edgelblcmpfn");
try {
    ST_Agedge_s e0, e1;
    final ST_pointf sz0 = new ST_pointf(), sz1 = new ST_pointf();
    e0 = ptr0.get_(0);
    e1 = ptr1.get_(0);
    if (ED_label(e0)!=null) {
	if (ED_label(e1)!=null) {
	    sz0.___(ED_label(e0).dimen);
	    sz1.___(ED_label(e1).dimen);
	    if (sz0.x > sz1.x) return -1;
	    else if (sz0.x < sz1.x) return 1;
	    else if (sz0.y > sz1.y) return -1;
	    else if (sz0.y < sz1.y) return 1;
	    else return 0;
	}
	else
	    return -1;
    }
    else if (ED_label(e1)!=null) {
 	return 1;
    }
    else
 	return 0;
} finally {
LEAVING("bmsa24i3avg14po4sp17yh89k","edgelblcmpfn");
}
}



/* makeSimpleFlatLabels:
 * This handles the second simplest case for flat edges between
 * two adjacent nodes. We still invoke a dot on a rotated problem
 * to handle edges with ports. This usually works, but fails for
 * records because of their weird nature.
 */
//3 3xmylrnypvoqrj2yrxnomsj5k
// static void makeSimpleFlatLabels (node_t* tn, node_t* hn, edge_t** edges, int ind, int cnt, int et, int n_lbls) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="makeSimpleFlatLabels", key="3xmylrnypvoqrj2yrxnomsj5k", definition="static void makeSimpleFlatLabels (node_t* tn, node_t* hn, edge_t** edges, int ind, int cnt, int et, int n_lbls)")
public static void makeSimpleFlatLabels(Globals zz, ST_Agnode_s tn, ST_Agnode_s hn, CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, int et, int n_lbls) {
ENTERING("3xmylrnypvoqrj2yrxnomsj5k","makeSimpleFlatLabels");
try {
	CArray<ST_pointf> ps;
    final ST_Ppoly_t poly = new ST_Ppoly_t();
    int pn[] = new int[1];
	ST_Agedge_s e = edges.get_(ind);
    final CArray<ST_pointf> points = CArray.<ST_pointf>ALLOC__(10, ZType.ST_pointf);
    final ST_pointf tp = new ST_pointf(), hp = new ST_pointf();
    int i, pointn;
    double leftend, rightend, ctrx=0, ctry=0, miny, maxy;
    double uminx=0, umaxx=0;
    double lminx=0, lmaxx=0;
    
    CArrayOfStar<ST_Agedge_s> earray = CArrayOfStar.<ST_Agedge_s>ALLOC(cnt, ZType.ST_Agedge_s);
    
    for (i = 0; i < cnt; i++) {
	earray.set_(i, edges.get_(ind + i));
    }
    
    qsort(zz, earray,
    cnt,
    dotsplines__c.edgelblcmpfn);
    tp.___(add_pointf(ND_coord(tn), ED_tail_port(e).p));
    hp.___(add_pointf(ND_coord(hn), ED_head_port(e).p));
    
    leftend = tp.x+ND_rw(tn);
    rightend = hp.x-ND_lw(hn);
    ctrx = (leftend + rightend)/2.0;
    
    /* do first edge */
    e = earray.get_(0);
    pointn = 0;
    points.get__(pointn++).___(tp);
    points.get__(pointn++).___(tp);
    points.get__(pointn++).___(hp);
    points.get__(pointn++).___(hp);
    clip_and_install(zz, e, aghead(e), points, pointn, zz.sinfo);
    ED_label(e).pos.x = ctrx;
    ED_label(e).pos.y = (tp.y + (ED_label(e).dimen.y+6)/2.0);
    ED_label(e).set = 1;
    
    miny = tp.y + 6/2.0;
    maxy = miny + ED_label(e).dimen.y;
    uminx = ctrx - (ED_label(e).dimen.x)/2.0;
    umaxx = ctrx + (ED_label(e).dimen.x)/2.0;
    
    for (i = 1; i < n_lbls; i++) {
	e = earray.get_(i);
	if (i%2!=0) {  /* down */
	    if (i == 1) {
		lminx = ctrx - (ED_label(e).dimen.x)/2.0;
		lmaxx = ctrx + (ED_label(e).dimen.x)/2.0;
		}
	    miny -= 6 + ED_label(e).dimen.y;
	    points.get__(0).___(tp);
	    points.get__(1).x = tp.x;
	    points.get__(1).y = miny - 6;
	    points.get__(2).x = hp.x;
	    points.get__(2).y = points.get__(1).y;
	    points.get__(3).___(hp);
	    points.get__(4).x = lmaxx;
	    points.get__(4).y = hp.y;
	    points.get__(5).x = lmaxx;
	    points.get__(5).y = miny;
	    points.get__(6).x = lminx;
	    points.get__(6).y = miny;
	    points.get__(7).x = lminx;
	    points.get__(7).y = tp.y;
	    ctry = miny + (ED_label(e).dimen.y)/2.0;
	}
	else {   /* up */
	    points.get__(0).___(tp);
	    points.get__(1).x = uminx;
	    points.get__(1).y = tp.y;
	    points.get__(2).x = uminx;
	    points.get__(2).y = maxy;
	    points.get__(3).x = umaxx;
	    points.get__(3).y = maxy;
	    points.get__(4).x = umaxx;
	    points.get__(4).y = hp.y;
	    points.get__(5).x = hp.x;
	    points.get__(5).y = hp.y;
	    points.get__(6).x = hp.x;
	    points.get__(6).y = maxy + 6;
	    points.get__(7).x = tp.x;
	    points.get__(7).y = maxy + 6;
	    ctry =  maxy + (ED_label(e).dimen.y)/2.0 + 6;
	    maxy += ED_label(e).dimen.y + 6;
	}
	poly.pn = 8;
	poly.ps = points;
	ps = simpleSplineRoute (zz, tp, hp, poly, pn, et == (3 << 1));
	if (pn[0] == 0) return;
	ED_label(e).pos.x = ctrx;
	ED_label(e).pos.y = ctry;
	ED_label(e).set= false ? 0 : 1;
	clip_and_install(zz, e, aghead(e), ps, pn[0], zz.sinfo);
    }
    
    /* edges with no labels */
    for (; i < cnt; i++) {
	e = earray.get_(i);
	if (i%2!=0) {  /* down */
	    if (i == 1) {
		lminx = (2*leftend + rightend)/3.0;
		lmaxx = (leftend + 2*rightend)/3.0;
	    }
	    miny -= 6;
	    points.get__(0).___(tp);
	    points.get__(1).x = tp.x;
	    points.get__(1).y = miny - 6;
	    points.get__(2).x = hp.x;
	    points.get__(2).y = points.get__(1).y;
	    points.get__(3).___(hp);
	    points.get__(4).x = lmaxx;
	    points.get__(4).y = hp.y;
	    points.get__(5).x = lmaxx;
	    points.get__(5).y = miny;
	    points.get__(6).x = lminx;
	    points.get__(6).y = miny;
	    points.get__(7).x = lminx;
	    points.get__(7).y = tp.y;
	}
	else {   /* up */
	    points.get__(0).___(tp);
	    points.get__(1).x = uminx;
	    points.get__(1).y = tp.y;
	    points.get__(2).x = uminx;
	    points.get__(2).y = maxy;
	    points.get__(3).x = umaxx;
	    points.get__(3).y = maxy;
	    points.get__(4).x = umaxx;
	    points.get__(4).y = hp.y;
	    points.get__(5).x = hp.x;
	    points.get__(5).y = hp.y;
	    points.get__(6).x = hp.x;
	    points.get__(6).y = maxy + 6;
	    points.get__(7).x = tp.x;
	    points.get__(7).y = maxy + 6;
	    maxy += + 6;
	}
	poly.pn = 8;
	poly.ps = points;
	ps = simpleSplineRoute (zz, tp, hp, poly, pn, et == (3 << 1));
	if (pn[0] == 0) return;
	clip_and_install(zz, e, aghead(e), ps, pn[0], zz.sinfo);
    }
    
    Memory.free (earray);
} finally {
LEAVING("3xmylrnypvoqrj2yrxnomsj5k","makeSimpleFlatLabels");
}
}




/* makeSimpleFlat:
 */
//3 8kqyzk43ovc2sq6jegua6ytp
// static void makeSimpleFlat (node_t* tn, node_t* hn, edge_t** edges, int ind, int cnt, int et) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="makeSimpleFlat", key="8kqyzk43ovc2sq6jegua6ytp", definition="static void makeSimpleFlat (node_t* tn, node_t* hn, edge_t** edges, int ind, int cnt, int et)")
public static void makeSimpleFlat(Globals zz, ST_Agnode_s tn, ST_Agnode_s hn, CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, int et) {
ENTERING("8kqyzk43ovc2sq6jegua6ytp","makeSimpleFlat");
try {
    ST_Agedge_s e = edges.get_(ind);
    final CArray<ST_pointf> points = CArray.<ST_pointf>ALLOC__(10, ZType.ST_pointf);
    final ST_pointf tp = new ST_pointf(), hp = new ST_pointf();
    int i, pointn;
    double stepy, dy;

    tp.___(add_pointf(ND_coord(tn), ED_tail_port(e).p));
    hp.___(add_pointf(ND_coord(hn), ED_head_port(e).p));
    
    stepy = (cnt > 1) ? ND_ht(tn) / (double)(cnt - 1) : 0.;
    dy = tp.y - ((cnt > 1) ? ND_ht(tn) / 2. : 0.);
    
    for (i = 0; i < cnt; i++) {
	e = edges.get_(ind + i);
	pointn = 0;
	
	if ((et == ET_SPLINE) || (et == ET_LINE)) {
	    points.get__(pointn++).___(tp);
	    points.get__(pointn++).___(pointfof((2 * tp.x + hp.x) / 3, dy));
	    points.get__(pointn++).___(pointfof((2 * hp.x + tp.x) / 3, dy));
	    points.get__(pointn++).___(hp);
	}
	else {   /* ET_PLINE */
UNSUPPORTED("cai7diqq01v8vb92u4mx1xh38"); // 	    points[pointn++] = tp;
UNSUPPORTED("cai7diqq01v8vb92u4mx1xh38"); // 	    points[pointn++] = tp;
UNSUPPORTED("bnd7kao912fmo940u1gz3cmws"); // 	    points[pointn++] = pointfof((2 * tp.x + hp.x) / 3, dy);
UNSUPPORTED("bnd7kao912fmo940u1gz3cmws"); // 	    points[pointn++] = pointfof((2 * tp.x + hp.x) / 3, dy);
UNSUPPORTED("bnd7kao912fmo940u1gz3cmws"); // 	    points[pointn++] = pointfof((2 * tp.x + hp.x) / 3, dy);
UNSUPPORTED("akir2i0ddpv2ombgdzzs9qqqa"); // 	    points[pointn++] = pointfof((2 * hp.x + tp.x) / 3, dy);
UNSUPPORTED("akir2i0ddpv2ombgdzzs9qqqa"); // 	    points[pointn++] = pointfof((2 * hp.x + tp.x) / 3, dy);
UNSUPPORTED("akir2i0ddpv2ombgdzzs9qqqa"); // 	    points[pointn++] = pointfof((2 * hp.x + tp.x) / 3, dy);
UNSUPPORTED("59cwjy3j5e0igp278migykzi"); // 	    points[pointn++] = hp;
UNSUPPORTED("59cwjy3j5e0igp278migykzi"); // 	    points[pointn++] = hp;
	}
	dy += stepy;
	clip_and_install(zz, e, aghead(e), points, pointn, zz.sinfo);
    }
} finally {
LEAVING("8kqyzk43ovc2sq6jegua6ytp","makeSimpleFlat");
}
}




//3 bhnjospwghq4plid12757c928
// static void make_flat_adj_edges(graph_t* g, path* P, edge_t** edges, int ind, int cnt, edge_t* e0,                     int et) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="make_flat_adj_edges", key="bhnjospwghq4plid12757c928", definition="static void make_flat_adj_edges(graph_t* g, path* P, edge_t** edges, int ind, int cnt, edge_t* e0,                     int et)")
public static void make_flat_adj_edges(Globals zz, ST_Agraph_s g, ST_path P, CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, ST_Agedge_s e0, int et) {
ENTERING("bhnjospwghq4plid12757c928","make_flat_adj_edges");
try {
    ST_Agnode_s n;
    ST_Agnode_s tn, hn;
    ST_Agedge_s e;
    int labels = 0, ports = 0;
    ST_Agraph_s auxg;
    ST_Agraph_s subg;
    ST_Agnode_s auxt, auxh;
    ST_Agedge_s auxe;
    int     i, j, midx, midy, leftx, rightx;
    final ST_pointf del = new ST_pointf();
    ST_Agedge_s hvye = null;
    __ptr__ attrs;
    tn = agtail(e0); hn = aghead(e0);
    for (i = 0; i < cnt; i++) {
	e = edges.get_(ind + i);
	if (ED_label(e)!=null) labels++;
	if (ED_tail_port(e).defined || ED_head_port(e).defined) ports = 1;
    }
    if (ports == 0) {
	/* flat edges without ports and labels can go straight left to right */
	if (labels == 0) {
	    makeSimpleFlat (zz, tn, hn, edges, ind, cnt, et);
	}
	/* flat edges without ports but with labels take more work */
	else {
	    makeSimpleFlatLabels (zz, tn, hn, edges, ind, cnt, et, labels);
	}
	return;
    }
UNSUPPORTED("3ua0mgcwxnpymnpiv77owaur2"); //     attrs = (attr_state_t*)zmalloc(sizeof(attr_state_t));
UNSUPPORTED("5qcpchn65culafc5t2ibioksb"); //     auxg = cloneGraph (g, attrs);
UNSUPPORTED("cwolismpef6l1w4xj5etx8w09"); //     subg = agsubg (auxg, "xxx",1);
UNSUPPORTED("eocu0fte9egz381w3t8y5m64t"); //     agbindrec(subg, "Agraphinfo_t", sizeof(Agraphinfo_t), NOT(0));
UNSUPPORTED("er19m8huvnjjn1v6rreiisdf6"); //     agset (subg, "rank", "source");
UNSUPPORTED("du7zwfglureqnv5g2wiammuuu"); //     rightx = ND_coord(hn).x;
UNSUPPORTED("5qa24lmumrg29fbtl0fo4d4z1"); //     leftx = ND_coord(tn).x;
UNSUPPORTED("b2x6j7m1cmmkcmdl5jo9wn0ap"); //     if (GD_flip(g)) {
UNSUPPORTED("2elvc001uux0vbe7sv4098e0c"); //         node_t* n;
UNSUPPORTED("6m8k99c09zmhwgcdsgo33dugz"); //         n = tn;
UNSUPPORTED("cenlfgoeymhe1am5z6632xniq"); //         tn = hn;
UNSUPPORTED("cp2gf6c1xawd2yjbqerw98we3"); //         hn = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1e98fnf9lsk7hwgjt70gh5r55"); //     auxt = cloneNode(subg, tn, GD_flip(g)); 
UNSUPPORTED("a4xa8i1nw83hhnknv84280z00"); //     auxh = cloneNode(auxg, hn, GD_flip(g)); 
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("8hwholxjp08y4q0zbb561684q"); // 	e = edges[ind + i];
UNSUPPORTED("b3w40fpgfj0j66yi6j582thq2"); // 	for (; ED_edge_type(e) != 0; e = ED_to_orig(e));
UNSUPPORTED("bcu59ji1apn0rvv7aisl6e4pw"); // 	if (agtail(e) == tn)
UNSUPPORTED("4o3mq2ztiyxlbkpgcyygj4lny"); // 	    auxe = cloneEdge (auxg, auxt, auxh, e);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("3a7m578mxun4c8zv0jurzh7ox"); // 	    auxe = cloneEdge (auxg, auxh, auxt, e);
UNSUPPORTED("e6khamu7fhu5m7il9r2v06mui"); // 	ED_alg(e) = auxe;
UNSUPPORTED("64u9xrw8iwy3mtuupoktw7xoz"); // 	if (!hvye && !ED_tail_port(e).defined && !ED_head_port(e).defined) {
UNSUPPORTED("6t23nfzzldtl419g582pr0wjn"); // 	    hvye = auxe;
UNSUPPORTED("3fi2uxwkmau5664gnhzyosan5"); // 	    ED_alg(hvye) = e;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("co4n3cv6ybhgvm341r3re61vu"); //     if (!hvye) {
UNSUPPORTED("a0sxhnruv0ip65j05wt7dciak"); // 	hvye = agedge (auxg, auxt, auxh,NULL,1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("26l323zwxztuc6sfnh36x7i9f"); //     agxset (hvye, E_weight, "10000");
UNSUPPORTED("4pj2d4pwf7wi9fcu4v0byk83z"); //     GD_gvc(auxg) = GD_gvc(g);
UNSUPPORTED("b6dpjp9wgjzgoy0tq7b9kms74"); //     GD_dotroot(auxg) = auxg;
UNSUPPORTED("e08t9bx8ldb1d5e0582wg82sx"); //     setEdgeType (auxg, et);
UNSUPPORTED("8y4433lgybq35f4t7viqsvz5v"); //     dot_init_node_edge(auxg);
UNSUPPORTED("13c2nt4jwumpvtwudz38a4sb3"); //     dot_rank(auxg, 0);
UNSUPPORTED("6onnznyz9fof6r8we5z1e4r63"); //     dot_mincross(auxg, 0);
UNSUPPORTED("4ry85qx5xmahkm0mdv9s65azo"); //     dot_position(auxg, 0);
UNSUPPORTED("9921qakdgil2jvrvinke44xsz"); //     /* reposition */
UNSUPPORTED("ang8wy78872hvqkzvgfjwqwfl"); //     midx = (ND_coord(tn).x - ND_rw(tn) + ND_coord(hn).x + ND_lw(hn))/2;
UNSUPPORTED("2r2ywte92l423tbcdfvbhz3zh"); //     midy = (ND_coord(auxt).x + ND_coord(auxh).x)/2;
UNSUPPORTED("b8juh1dggb6sz4d9df42i150k"); //     for (n = GD_nlist(auxg); n; n = ND_next(n)) {
UNSUPPORTED("4prtyy13ox1fif8vil3g86g87"); // 	if (n == auxt) {
UNSUPPORTED("7gvpia4yo3bfvh5dl7uwr3dbe"); // 	    ND_coord(n).y = rightx;
UNSUPPORTED("2zsspnn0nfrdf9fx8sh3ld8j4"); // 	    ND_coord(n).x = midy;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ba2aaoh9ujy4ww9luial39sya"); // 	else if (n == auxh) {
UNSUPPORTED("aoy1ze6hhjuym44mkasdt19qp"); // 	    ND_coord(n).y = leftx;
UNSUPPORTED("2zsspnn0nfrdf9fx8sh3ld8j4"); // 	    ND_coord(n).x = midy;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("80o8q2qh1hr671ladbyqzglnt"); // 	else ND_coord(n).y = midx;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("48hn94qz2y2kb6lqc3462vlco"); //     dot_sameports(auxg);
UNSUPPORTED("bglhuc9grqkk9ozb97oeh0m3b"); //     _dot_splines(auxg, 0);
UNSUPPORTED("euvfot8f7xxnibd223qgsylew"); //     dotneato_postprocess(auxg);
UNSUPPORTED("4mg60pszj0s1anwg8zp0tq0ra"); //        /* copy splines */
UNSUPPORTED("b2x6j7m1cmmkcmdl5jo9wn0ap"); //     if (GD_flip(g)) {
UNSUPPORTED("baukf0b2l91bkgnfdc04rvuxe"); // 	del.x = ND_coord(tn).x - ND_coord(auxt).y;
UNSUPPORTED("96sq686mw9bvj7dli7wb241au"); // 	del.y = ND_coord(tn).y + ND_coord(auxt).x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("bxg8i1hqms0izrryxh2at6p7b"); // 	del.x = ND_coord(tn).x - ND_coord(auxt).x;
UNSUPPORTED("3xqgvdcxmu8e4eqtnhaa256tk"); // 	del.y = ND_coord(tn).y - ND_coord(auxt).y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("47ogn0fae1yuy3kefpawcgcmx"); // 	bezier* auxbz;
UNSUPPORTED("4mujfxtr0wy7m1rbm57rwigo4"); // 	bezier* bz;
UNSUPPORTED("8hwholxjp08y4q0zbb561684q"); // 	e = edges[ind + i];
UNSUPPORTED("b3w40fpgfj0j66yi6j582thq2"); // 	for (; ED_edge_type(e) != 0; e = ED_to_orig(e));
UNSUPPORTED("3ii7nnxg3x8jpdk6lcijbizvf"); // 	auxe = (edge_t*)ED_alg(e);
UNSUPPORTED("5rc7uvn4xqfetekcci1uwri5v"); // 	if ((auxe == hvye) & !ED_alg(auxe)) continue; /* pseudo-edge */
UNSUPPORTED("52vy3jbu67nebznqkhxxqthih"); // 	auxbz = ED_spl(auxe)->list;
UNSUPPORTED("azr1tv8blnu3i6b7wstpqv2tc"); // 	bz = new_spline(e, auxbz->size);
UNSUPPORTED("df84ie4egl6l1i82tz1zp7hg"); // 	bz->sflag = auxbz->sflag;
UNSUPPORTED("49bzonc32mbuchcsmw7csexdw"); // 	bz->sp = transformf(auxbz->sp, del, GD_flip(g));
UNSUPPORTED("9g1glpjjy5wmh7dyr6u0nb2cn"); // 	bz->eflag = auxbz->eflag;
UNSUPPORTED("44tftrsdjjuh1qjmxjpysmee3"); // 	bz->ep = transformf(auxbz->ep, del, GD_flip(g));
UNSUPPORTED("w0xl6ghxoii2ze2xmbbunv4d"); // 	for (j = 0; j <  auxbz->size; ) {
UNSUPPORTED("22zm6ljvhpz7j2kiqee31nx61"); // 	    pointf cp[4];
UNSUPPORTED("4c88sva3ojadip5w3ehu782tp"); // 	    cp[0] = bz->list[j] = transformf(auxbz->list[j], del, GD_flip(g));
UNSUPPORTED("5jqtd9htl25cd3if3pjtl8dpo"); // 	    j++;
UNSUPPORTED("duk541biqshfomah8rlxeyigr"); // 	    if ( j >= auxbz->size ) 
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("2qrbm7mhicvoxzk91h9x3gq64"); // 	    cp[1] = bz->list[j] = transformf(auxbz->list[j], del, GD_flip(g));
UNSUPPORTED("5jqtd9htl25cd3if3pjtl8dpo"); // 	    j++;
UNSUPPORTED("f4x9unnnofgnvy91ejtm90kw"); // 	    cp[2] = bz->list[j] = transformf(auxbz->list[j], del, GD_flip(g));
UNSUPPORTED("5jqtd9htl25cd3if3pjtl8dpo"); // 	    j++;
UNSUPPORTED("6pldfj31dg88qzsa012v8auxn"); // 	    cp[3] = transformf(auxbz->list[j], del, GD_flip(g));
UNSUPPORTED("2xvehdctlpogpd7xhdf1pku00"); // 	    update_bb_bz(&GD_bb(g), cp);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("8bokoh0vcnmvjcahn7de1z0kd"); // 	if (ED_label(e)) {
UNSUPPORTED("8ido4wfhvlqvczqfq5xz33kak"); // 	    ED_label(e)->pos = transformf(ED_label(auxe)->pos, del, GD_flip(g));
UNSUPPORTED("3tkba5lhpnujfu8lcz8lewsyn"); // 	    ED_label(e)->set = NOT(0);
UNSUPPORTED("c62p0r1jj71ceakgzrv2gorvu"); // 	    updateBB(g, ED_label(e));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("22prbnbyxmapn1fcer1sktez7"); //     cleanupCloneGraph (auxg, attrs);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("bhnjospwghq4plid12757c928","make_flat_adj_edges");
}
}




//3 fybar4mljnmkh3kure5k1eod
// static void makeFlatEnd (graph_t* g, spline_info_t* sp, path* P, node_t* n, edge_t* e, pathend_t* endp,              boolean isBegin) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="makeFlatEnd", key="fybar4mljnmkh3kure5k1eod", definition="static void makeFlatEnd (graph_t* g, spline_info_t* sp, path* P, node_t* n, edge_t* e, pathend_t* endp,              boolean isBegin)")
public static void makeFlatEnd(Globals zz, ST_Agraph_s g, ST_spline_info_t sp, ST_path P, ST_Agnode_s n, ST_Agedge_s e, ST_pathend_t endp, boolean isBegin) {
ENTERING("fybar4mljnmkh3kure5k1eod","makeFlatEnd");
try {
    final ST_boxf b = new ST_boxf();
    b.___(maximal_bbox(g, sp, n, null, e));
    endp.nb.___(b);
    endp.sidemask = (1<<2);
    if (isBegin) beginpath(zz, P, e, 2, endp, false);
    else endpath(zz, P, e, 2, endp, false);
    b.UR.y = endp.boxes[endp.boxn[0] - 1].UR.y;
    b.LL.y = endp.boxes[endp.boxn[0] - 1].LL.y;
    b.___(makeregularend((ST_boxf) b, (1<<2), ND_coord(n).y + GD_rank(g).get__(ND_rank(n)).ht2));
    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("cmjm4y40vf7wklmgz0ae4k36v"); // 	endp->boxes[endp->boxn++] = b;
} finally {
LEAVING("fybar4mljnmkh3kure5k1eod","makeFlatEnd");
}
}




//3 w8ptjibydq995d2lexg85mku
// static void make_flat_labeled_edge(graph_t* g, spline_info_t* sp, path* P, edge_t* e, int et) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="make_flat_labeled_edge", key="w8ptjibydq995d2lexg85mku", definition="static void make_flat_labeled_edge(graph_t* g, spline_info_t* sp, path* P, edge_t* e, int et)")
public static void make_flat_labeled_edge(Globals zz, ST_Agraph_s g, ST_spline_info_t sp, ST_path P, ST_Agedge_s e, int et) {
ENTERING("w8ptjibydq995d2lexg85mku","make_flat_labeled_edge");
try {
    ST_Agnode_s tn, hn, ln;
    CArray<ST_pointf> ps = null;
    final ST_pathend_t tend = new ST_pathend_t(), hend = new ST_pathend_t();
    final ST_boxf lb = new ST_boxf();
    int boxn, i, ydelta;
    int pn[] = new int[1];
    ST_Agedge_s f;
    final CArray<ST_pointf> points = CArray.<ST_pointf>ALLOC__(7, ZType.ST_pointf);
    tn = agtail(e);
    hn = aghead(e);
    for (f = ED_to_virt(e); ED_to_virt(f)!=null; f = ED_to_virt(f));
    ln = agtail(f);
    ED_label(e).pos.___(ND_coord(ln));
    ED_label(e).set= false ? 0 : 1;
    if (et == (1 << 1)) {
UNSUPPORTED("ataaqkehwb736ts2x6olzqokx"); // 	pointf startp, endp, lp;
UNSUPPORTED("48h6vk2e5niep4dmpua377248"); // 	startp = add_pointf(ND_coord(tn), ED_tail_port(e).p);
UNSUPPORTED("8fokfn1629eyiqnzi7mey9o2q"); // 	endp = add_pointf(ND_coord(hn), ED_head_port(e).p);
UNSUPPORTED("8zpih1olxdgp3sxk2br3lezzx"); //         lp = ED_label(e)->pos;
UNSUPPORTED("1sblcap44288tmc3cr80iqpj5"); // 	lp.y -= (ED_label(e)->dimen.y)/2.0;
UNSUPPORTED("8ew9p3k3j0b33dnq0ntgbb6x8"); // 	points[1] = points[0] = startp;
UNSUPPORTED("16h577fdghqtaeot735guew4e"); // 	points[2] = points[3] = points[4] = lp;
UNSUPPORTED("9zjfovamhwwehbviws68s5woy"); // 	points[5] = points[6] = endp;
UNSUPPORTED("68ojpcos92c96bc0i5ag1yb6v"); // 	ps = points;
UNSUPPORTED("1uunj4jbr2uhiqxwor6rzmr3j"); // 	pn = 7;
    }
    else {
	lb.LL.x = ND_coord(ln).x - ND_lw(ln);
	lb.UR.x = ND_coord(ln).x + ND_rw(ln);
	lb.UR.y = ND_coord(ln).y + ND_ht(ln)/2;
	ydelta = (int)(ND_coord(ln).y - GD_rank(g).get__(ND_rank(tn)).ht1 -
		ND_coord(tn).y + GD_rank(g).get__(ND_rank(tn)).ht2);
	ydelta = (int)(ydelta / 6.);
	lb.LL.y = lb.UR.y - Math.max(5.,ydelta); 
	boxn = 0;
	makeFlatEnd (zz, g, sp, P, tn, e,  tend, true);
	makeFlatEnd (zz, g, sp, P, hn, e,  hend, false);
	zz.boxes[boxn].LL.x = tend.boxes[tend.boxn[0] - 1].LL.x; 
	zz.boxes[boxn].LL.y = tend.boxes[tend.boxn[0] - 1].UR.y; 
	zz.boxes[boxn].UR.x = lb.LL.x;
	zz.boxes[boxn].UR.y = lb.LL.y;
	boxn++;
	zz.boxes[boxn].LL.x = tend.boxes[tend.boxn[0] - 1].LL.x; 
	zz.boxes[boxn].LL.y = lb.LL.y;
	zz.boxes[boxn].UR.x = hend.boxes[hend.boxn[0] - 1].UR.x;
	zz.boxes[boxn].UR.y = lb.UR.y;
	boxn++;
	zz.boxes[boxn].LL.x = lb.UR.x;
	zz.boxes[boxn].UR.y = lb.LL.y;
	zz.boxes[boxn].LL.y = hend.boxes[hend.boxn[0] - 1].UR.y; 
	zz.boxes[boxn].UR.x = hend.boxes[hend.boxn[0] - 1].UR.x;
	boxn++;
	for (i = 0; i < tend.boxn[0]; i++) add_box(P, tend.boxes[i]);
	for (i = 0; i < boxn; i++) add_box(P, zz.boxes[i]);
	for (i = hend.boxn[0] - 1; i >= 0; i--) add_box(P, hend.boxes[i]);
	if (et == (5 << 1)) ps = routesplines(zz, P, pn);
	else ps = routepolylines(zz, P, pn);
	if (pn[0] == 0) return;
    }
    clip_and_install(zz, e, aghead(e), ps, pn[0], zz.sinfo);
} finally {
LEAVING("w8ptjibydq995d2lexg85mku","make_flat_labeled_edge");
}
}




//3 d97ga7gm34rs6r0jo494nhhuo
// static void make_flat_bottom_edges(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int  	ind, int cnt, edge_t* e, int splines) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="make_flat_bottom_edges", key="d97ga7gm34rs6r0jo494nhhuo", definition="static void make_flat_bottom_edges(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int  	ind, int cnt, edge_t* e, int splines)")
public static Object make_flat_bottom_edges(Object... arg_) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("bkp39vt080is6iiqobsw59sk9"); // make_flat_bottom_edges(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int 
UNSUPPORTED("2gat0ltxh7j57lspwmm9s9x7j"); // 	ind, int cnt, edge_t* e, int splines)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5np4z3gp6cfmicyuz91nownp8"); //     node_t *tn, *hn;
UNSUPPORTED("3zgmm04g7gq65c57gco0id1eb"); //     int j, i, r;
UNSUPPORTED("5wdd96amz7w1sym59mg5nfj51"); //     double stepx, stepy, vspace;
UNSUPPORTED("63ozqzusft8gpehyaqrdjyr1"); //     rank_t* nextr;
UNSUPPORTED("7hps2kejtrotcphg5gymma43b"); //     int pn;
UNSUPPORTED("2rkzhui0essisp5zlw44vx4j9"); //     pointf *ps;
UNSUPPORTED("32b8td88encjfj6yd1sp8bef"); //     pathend_t tend, hend;
UNSUPPORTED("d0x4emyekwlcpua99voy3764p"); //     tn = agtail(e);
UNSUPPORTED("axoua6xpo7xc40u2oj0a8dmr9"); //     hn = aghead(e);
UNSUPPORTED("63jjxtcojh1aq4na9u4w2a6xe"); //     r = ND_rank(tn);
UNSUPPORTED("atjnavwfg3xe4ygorslb4vvtv"); //     if (r < GD_maxrank(g)) {
UNSUPPORTED("6b35lbhficoaf5216p3sdq1cj"); // 	nextr = GD_rank(g) + (r+1);
UNSUPPORTED("bj0cch0dmhpux7wm3fg4zg368"); // 	vspace = ND_coord(tn).y - GD_rank(g)[r].pht1 -
UNSUPPORTED("dqcrkagghulp8u9iuoyckepf7"); // 		(ND_coord(nextr->v[0]).y + nextr->pht2);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("1sai54q4zfayczfcrklgw4x17"); // 	vspace = GD_ranksep(g);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dyxkx5ftlgbeem7089pcu1qo4"); //     stepx = ((double)(sp->Multisep)) / (cnt+1); 
UNSUPPORTED("63qqo8mqbn6alfa5k3gfalz53"); //     stepy = vspace / (cnt+1);
UNSUPPORTED("euw24sa4cgazdf9z7a3sretis"); //     makeBottomFlatEnd (g, sp, P, tn, e, &tend, NOT(0));
UNSUPPORTED("c8ip2hla7pb8tz4fhsj5m34wk"); //     makeBottomFlatEnd (g, sp, P, hn, e, &hend, 0);
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("3smlz0a87gj8w58rwau2ewq23"); // 	int boxn;
UNSUPPORTED("bg5tj2hjixth58v04043jb3do"); // 	boxf b;
UNSUPPORTED("8hwholxjp08y4q0zbb561684q"); // 	e = edges[ind + i];
UNSUPPORTED("cpcqkqkhkaviy153h92apd82e"); // 	boxn = 0;
UNSUPPORTED("4wkbaj2424ug9j97efwu1r5mc"); // 	b = tend.boxes[tend.boxn - 1];
UNSUPPORTED("3eyfj1o1bqxlfqw7tq76qiiiz"); //  	boxes[boxn].LL.x = b.LL.x; 
UNSUPPORTED("dwlejn7xasxh4yyoq2xodqnyj"); // 	boxes[boxn].UR.y = b.LL.y; 
UNSUPPORTED("10o1e4r5ux8d7cr6zg379ss59"); // 	boxes[boxn].UR.x = b.UR.x + (i + 1) * stepx;
UNSUPPORTED("79f4mryzkqqs3t4sxnet9jc7b"); // 	boxes[boxn].LL.y = b.LL.y - (i + 1) * stepy;
UNSUPPORTED("cbrsymd5wpvadg3ziz4dypa50"); // 	boxn++;
UNSUPPORTED("7n7f256w4ewtzexwfxjo28b2"); // 	boxes[boxn].LL.x = tend.boxes[tend.boxn - 1].LL.x; 
UNSUPPORTED("6in0ql0upnjg00mgj5k8av2m4"); // 	boxes[boxn].UR.y = boxes[boxn-1].LL.y;
UNSUPPORTED("46ftsiqovpzo9r0mnlc4nf83"); // 	boxes[boxn].UR.x = hend.boxes[hend.boxn - 1].UR.x;
UNSUPPORTED("4bjnn4mhczzd1un9rbib5glch"); // 	boxes[boxn].LL.y = boxes[boxn].UR.y - stepy;
UNSUPPORTED("cbrsymd5wpvadg3ziz4dypa50"); // 	boxn++;
UNSUPPORTED("6oycftap8apw4glb1s5jzineu"); // 	b = hend.boxes[hend.boxn - 1];
UNSUPPORTED("77c1sajrw9kfc7avomly90gm8"); // 	boxes[boxn].UR.x = b.UR.x;
UNSUPPORTED("e94nxsepfdq17q69drbro41je"); // 	boxes[boxn].UR.y = b.LL.y;
UNSUPPORTED("6cipt7th0uv2xfwyh4rvwignj"); // 	boxes[boxn].LL.x = b.LL.x - (i + 1) * stepx;
UNSUPPORTED("50uajtfbvgkrruywpjq9m4zoj"); // 	boxes[boxn].LL.y = boxes[boxn-1].UR.y;
UNSUPPORTED("cbrsymd5wpvadg3ziz4dypa50"); // 	boxn++;
UNSUPPORTED("eu29s7oeoca2yo9trhfhb9juy"); // 	for (j = 0; j < tend.boxn; j++) add_box(P, tend.boxes[j]);
UNSUPPORTED("b8xs00axp4lkksof6hmim3n3x"); // 	for (j = 0; j < boxn; j++) add_box(P, boxes[j]);
UNSUPPORTED("ahci7z910ypoj0e54wchvk54v"); // 	for (j = hend.boxn - 1; j >= 0; j--) add_box(P, hend.boxes[j]);
UNSUPPORTED("7r9nsxbfy7w3csc04nxo97xpq"); // 	if (splines) ps = routesplines(P, &pn);
UNSUPPORTED("dfku80ag90r137s9az9493oow"); // 	else ps = routepolylines(P, &pn);
UNSUPPORTED("919jubmuis4tp1c1f87rvfhog"); // 	if (pn == 0)
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("62oxk9cc5wf0f8gqbothfte1f"); // 	clip_and_install(e, aghead(e), ps, pn, &sinfo);
UNSUPPORTED("l1nh40fo9oar4mz31h14bc9i"); // 	P->nbox = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




/* make_flat_edge:
 * Construct flat edges edges[ind...ind+cnt-1]
 * There are 4 main cases:
 *  - all edges between a and b where a and b are adjacent 
 *  - one labeled edge
 *  - all non-labeled edges with identical ports between non-adjacent a and b 
 *     = connecting bottom to bottom/left/right - route along bottom
 *     = the rest - route along top
 */
//3 6yr3jfkljl5w0z6dv354ryx63
// static void make_flat_edge(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int ind, int cnt, int et) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="make_flat_edge", key="6yr3jfkljl5w0z6dv354ryx63", definition="static void make_flat_edge(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int ind, int cnt, int et)")
public static void make_flat_edge(Globals zz, ST_Agraph_s g, ST_spline_info_t sp, ST_path P, CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, int et) {
ENTERING("6yr3jfkljl5w0z6dv354ryx63","make_flat_edge");
try {
    ST_Agnode_s tn, hn;
    final ST_Agedgeinfo_t fwdedgei = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedge = new ST_Agedgepair_s();
    ST_Agedge_s e;
    int j, i, r, isAdjacent;
    double stepx, stepy, vspace;
    int tside, hside;
    int pn[] = new int[] {0};
    CArray<ST_pointf> ps;
    final ST_pathend_t tend = new ST_pathend_t(), hend = new ST_pathend_t();
    
    fwdedge.out.base.data = fwdedgei;
    
    /* Get sample edge; normalize to go from left to right */
    e = edges.get_(ind);
    isAdjacent = ED_adjacent(e);
    if ((ED_tree_index(e) & 32)!=0) {
	MAKEFWDEDGE(fwdedge.out, e);
	e = (ST_Agedge_s) fwdedge.out;
    }
    for (i = 1; i < cnt; i++) {
	if (ED_adjacent(edges.get_(ind+i))!=0) {
	    isAdjacent = 1;
	    break;
	}
    }
    /* The lead edge edges[ind] might not have been marked earlier as adjacent,
     * so check them all.
     */
    if (isAdjacent!=0) {
	make_flat_adj_edges (zz, g, P, edges, ind, cnt, e, et);
	return;
    }
    if (ED_label(e)!=null) {  /* edges with labels aren't multi-edges */
	make_flat_labeled_edge (zz, g, sp, P, e, et);
	return;
    }
    
    if (et == (1 << 1)) {
	makeSimpleFlat (zz, agtail(e), aghead(e), edges, ind, cnt, et);
	return;
    }
    
    tside = ED_tail_port(e).side;
    hside = ED_head_port(e).side;
    if (((tside == (1<<0)) && (hside != (1<<2))) ||
        ((hside == (1<<0)) && (tside != (1<<2)))) {
	make_flat_bottom_edges (g, sp, P, edges, ind, cnt, e, et == (5 << 1));
	return;
    }
    
    tn = agtail(e);
    hn = aghead(e);
    r = ND_rank(tn);
    if (r > 0) {
    	CArray<ST_rank_t> prevr;
	if ((GD_has_labels(g) & (1 << 0))!=0)
	    prevr = GD_rank(g).plus_(r-2);
	else
	    prevr = GD_rank(g).plus_(r-1);
	vspace = ND_coord(prevr.get__(0).v.get_(0)).y - prevr.get__(0).ht1 - ND_coord(tn).y - GD_rank(g).get__(r).ht2;
    }
    else {
	vspace = GD_ranksep(g);
    }
    stepx = ((double)sp.Multisep) / (cnt+1); 
    stepy = vspace / (cnt+1);
    makeFlatEnd (zz, g, sp, P, tn, e, tend, true);
    makeFlatEnd (zz, g, sp, P, hn, e, hend, false);
    
    for (i = 0; i < cnt; i++) {
	int boxn;
	final ST_boxf b = new ST_boxf();
	e = edges.get_(ind + i);
	boxn = 0;
	b.___(tend.boxes[tend.boxn[0] - 1]);
 	zz.boxes[boxn].LL.x = b.LL.x; 
	zz.boxes[boxn].LL.y = b.UR.y; 
	zz.boxes[boxn].UR.x = b.UR.x + (i + 1) * stepx;
	zz.boxes[boxn].UR.y = b.UR.y + (i + 1) * stepy;
	boxn++;
	zz.boxes[boxn].LL.x = (tend.boxes[tend.boxn[0] - 1]).LL.x; 
	zz.boxes[boxn].LL.y = (zz.boxes[boxn-1]).UR.y;
	zz.boxes[boxn].UR.x = (hend.boxes[hend.boxn[0] - 1]).UR.x;
	zz.boxes[boxn].UR.y = zz.boxes[boxn].LL.y + stepy;
	boxn++;
	b.___(hend.boxes[hend.boxn[0] - 1]);
	zz.boxes[boxn].UR.x = b.UR.x;
	zz.boxes[boxn].LL.y = b.UR.y;
	zz.boxes[boxn].LL.x = b.LL.x - (i + 1) * stepx;
	zz.boxes[boxn].UR.y = (zz.boxes[boxn-1]).LL.y;
	boxn++;
	for (j = 0; j < tend.boxn[0]; j++) add_box(P, tend.boxes[j]);
	for (j = 0; j < boxn; j++) add_box(P, zz.boxes[j]);
	for (j = hend.boxn[0] - 1; j >= 0; j--) add_box(P, hend.boxes[j]);
	if (et == (5 << 1)) ps = routesplines(zz, P, pn);
	else ps = routepolylines(zz, P, pn);
	if (pn[0] == 0)
	    return;
	clip_and_install(zz, e, aghead(e), ps, pn[0], zz.sinfo);
	P.nbox = 0;
    }
} finally {
LEAVING("6yr3jfkljl5w0z6dv354ryx63","make_flat_edge");
}
}



//3 2n9bpvx34fnukqu1f9u4v7v6n
// static int  makeLineEdge(graph_t* g, edge_t* fe, pointf* points, node_t** hp) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="makeLineEdge", key="2n9bpvx34fnukqu1f9u4v7v6n", definition="static int  makeLineEdge(graph_t* g, edge_t* fe, pointf* points, node_t** hp)")
public static int makeLineEdge(ST_Agraph_s g, ST_Agedge_s fe, __ptr__ points, __ptr__ hp) {
ENTERING("2n9bpvx34fnukqu1f9u4v7v6n","makeLineEdge");
try {
 UNSUPPORTED("d9cz56vtrl0ri6hz88cumukuf"); // static int 
UNSUPPORTED("1act0rov08v6lg5ydqldks7d1"); // makeLineEdge(graph_t* g, edge_t* fe, pointf* points, node_t** hp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("awgar8whid8l1c7lyj8w53w3i"); //     int delr, pn;
UNSUPPORTED("ewlm0dtv1gpudqhfx5nsr9d77"); //     node_t* hn;
UNSUPPORTED("cncpk333257fxnc9w8kfl8m70"); //     node_t* tn;
UNSUPPORTED("etezq0kgy6mttiwr53pna62d7"); //     edge_t* e = fe;
UNSUPPORTED("3ll9r1mvzzbhqrs4dwsduif44"); //     pointf startp, endp, lp;
UNSUPPORTED("bgjjpl6jaaa122twwwd0vif6x"); //     pointf dimen;
UNSUPPORTED("gdtli7sq5cifvdpg3ecrudew"); //     double width, height;
UNSUPPORTED("1pa1p9mnpooqack43qfpnoio2"); //     while (ED_edge_type(e) != 0)
UNSUPPORTED("bdmai1d040qmubf08ds339v9x"); // 	e = ED_to_orig(e);
UNSUPPORTED("axoua6xpo7xc40u2oj0a8dmr9"); //     hn = aghead(e);
UNSUPPORTED("d0x4emyekwlcpua99voy3764p"); //     tn = agtail(e);
UNSUPPORTED("b4x4vd9cei4o3tjifzw7pdec6"); //     delr = ABS(ND_rank(hn)-ND_rank(tn));
UNSUPPORTED("447fk34earndyf1qvvzttsxtk"); //     if ((delr == 1) || ((delr == 2) && (GD_has_labels(g) & (1 << 0))))
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("cbnarpi971ox21zdds7a60axh"); //     if (agtail(fe) == agtail(e)) {
UNSUPPORTED("o7usaxh5vv7rla55dxzkfmk7"); // 	*hp = hn;
UNSUPPORTED("48h6vk2e5niep4dmpua377248"); // 	startp = add_pointf(ND_coord(tn), ED_tail_port(e).p);
UNSUPPORTED("8fokfn1629eyiqnzi7mey9o2q"); // 	endp = add_pointf(ND_coord(hn), ED_head_port(e).p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("3uxftvwe3olgfi579s8izpl76"); //  	*hp = tn; 
UNSUPPORTED("akhrhgryddg74jqispnh1j1nn"); // 	startp = add_pointf(ND_coord(hn), ED_head_port(e).p);
UNSUPPORTED("dqdai9l5thna19ynowkfwstuh"); // 	endp = add_pointf(ND_coord(tn), ED_tail_port(e).p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("brrmfllffwlnqxljgrwy8x5pq"); //     if (ED_label(e)) {
UNSUPPORTED("et7phi46cskfaxzpfo97e1s5s"); // 	dimen = ED_label(e)->dimen;
UNSUPPORTED("z26zaj60vrdy38k1c2vk7law"); // 	if (GD_flip(agraphof(hn))) {
UNSUPPORTED("6mcl7j0bp09d061f0dywd1ru8"); // 	    width = dimen.y;
UNSUPPORTED("eax7dzvpgqzsiffa2dn2idrxe"); // 	    height = dimen.x;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("de3o94hui5i4dbq9moun6h57f"); // 	    width = dimen.x;
UNSUPPORTED("41ge1nnwfokwi43f6tlkgmvb0"); // 	    height = dimen.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("d9umd1qjkl4kkybpd7se993ij"); // 	lp = ED_label(e)->pos, lp;
UNSUPPORTED("arxilpbzpy5a2s8skwl644jj2"); // 	if (leftOf (endp,startp,lp)) {
UNSUPPORTED("7bfb4rovu9obr4fje0hi95e67"); // 	    lp.x += width/2.0;
UNSUPPORTED("7y2ejp6vr760aqh5qp4v5zdc8"); // 	    lp.y -= height/2.0;
UNSUPPORTED("9l9roc1u8a03ljwm2mmeaiqq2"); // 	}    
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("a4spf3n199lr00qkdb3vb8i6c"); // 	    lp.x -= width/2.0;
UNSUPPORTED("f15m64yxj1oq475bvrt63d5gf"); // 	    lp.y += height/2.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8ew9p3k3j0b33dnq0ntgbb6x8"); // 	points[1] = points[0] = startp;
UNSUPPORTED("16h577fdghqtaeot735guew4e"); // 	points[2] = points[3] = points[4] = lp;
UNSUPPORTED("9zjfovamhwwehbviws68s5woy"); // 	points[5] = points[6] = endp;
UNSUPPORTED("1uunj4jbr2uhiqxwor6rzmr3j"); // 	pn = 7;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("8ew9p3k3j0b33dnq0ntgbb6x8"); // 	points[1] = points[0] = startp;
UNSUPPORTED("aztkfl5qctibiu6en7xf6xf3e"); // 	points[3] = points[2] = endp;
UNSUPPORTED("5rmgdadjn23z8djo32eh9bhzd"); // 	pn = 4;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("abnopd2ggrpdb538wf7zsmrhq"); //     return pn;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("2n9bpvx34fnukqu1f9u4v7v6n","makeLineEdge");
}
}


private static final int NUMPTS = 2000;

//3 30wfq1dby4t07hft9io52nq6z
// static void make_regular_edge(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int ind, int cnt, int et) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="make_regular_edge", key="30wfq1dby4t07hft9io52nq6z", definition="static void make_regular_edge(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int ind, int cnt, int et)")
public static void make_regular_edge(Globals zz, ST_Agraph_s g, ST_spline_info_t sp, ST_path P, CArrayOfStar<ST_Agedge_s> edges, int ind, int cnt, int et) {
ENTERING("30wfq1dby4t07hft9io52nq6z","make_regular_edge");
try {
    ST_Agnode_s tn, hn = null;
    final ST_Agedgeinfo_t fwdedgeai = new ST_Agedgeinfo_t(), fwdedgebi = new ST_Agedgeinfo_t(), fwdedgei = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedgea = new ST_Agedgepair_s(), fwdedgeb = new ST_Agedgepair_s(), fwdedge = new ST_Agedgepair_s();
    ST_Agedge_s e, fe, le, segfirst;
    CArray<ST_pointf> ps = null;
    final ST_pathend_t tend = new ST_pathend_t(), hend = new ST_pathend_t();
    final ST_boxf b = new ST_boxf();
    int boxn, sl, si, i, j, dx, hackflag, longedge;
    boolean smode;
    int pn[] = new int[] {0};
    int pointn[] = new int[] {0};
   
    fwdedgea.out.base.data = fwdedgeai;
    fwdedgeb.out.base.data = fwdedgebi;
    fwdedge.out.base.data = fwdedgei;
    
    if ((zz.pointfs) == null) {
	zz.pointfs = CArray.<ST_pointf>ALLOC__(NUMPTS, ZType.ST_pointf);
   	zz.pointfs2 = CArray.<ST_pointf>ALLOC__(NUMPTS, ZType.ST_pointf);
	zz.numpts = NUMPTS;
	zz.numpts2 = NUMPTS;
    }
    sl = 0;
    e = edges.get_(ind);
    hackflag = 0;
    if (Math.abs(ND_rank(agtail(e)) - ND_rank(aghead(e))) > 1) {
UNSUPPORTED("8f17srpa5iisomehrb4b01h51"); // 	fwdedgeai = *(Agedgeinfo_t*)e->base.data;
UNSUPPORTED("97znyysf99vzzwpgnqcpp5yek"); // 	fwdedgea.out = *e;
UNSUPPORTED("b6jipryp9p354gtq9lwa35lzj"); // 	fwdedgea.out.base.data = (Agrec_t*)&fwdedgeai;
UNSUPPORTED("568s5ftes1chv9n1s98g9cncf"); // 	if (ED_tree_index(e) & 32) {
UNSUPPORTED("9hw2l0eu91vauhvj3cxf3andc"); // 	    MAKEFWDEDGE(&fwdedgeb.out, e);
UNSUPPORTED("1rql0qzotc0yyozcfkj9p8xkm"); // 	    agtail(&fwdedgea.out) = aghead(e);
UNSUPPORTED("dw3p473qmkgjvxewsr8pimi2h"); // 	    ED_tail_port(&fwdedgea.out) = ED_head_port(e);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("2gys0bodxz4fbasfnrvx6ivg2"); // 	    fwdedgebi = *(Agedgeinfo_t*)e->base.data;
UNSUPPORTED("1qqbo2mfls7xhbdno0no8xq54"); // 	    fwdedgeb.out = *e;
UNSUPPORTED("980ksnsma7kvvr9755ge8bhzh"); // 	    fwdedgeb.out.base.data = (Agrec_t*)&fwdedgebi;
UNSUPPORTED("6le0rehxs2odmv3zg1qg5wvd4"); // 	    agtail(&fwdedgea.out) = agtail(e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3p0d08nntark676jlv1jl0j27"); // 	le = getmainedge(e);
UNSUPPORTED("6tmwmfqoz3y8k44xamrpv82tl"); // 	while (ED_to_virt(le))
UNSUPPORTED("1c19c7ftue4zoibf7d2tm6uxy"); // 	    le = ED_to_virt(le);
UNSUPPORTED("d29k9lzj5g3d8dfxigwogdnoe"); // 	aghead(&fwdedgea.out) = aghead(le);
UNSUPPORTED("36l0czce101bg0wbmu68xjd7z"); // 	ED_head_port(&fwdedgea.out).defined = 0;
UNSUPPORTED("497rb9p6jdgdoyem0y42ecy6c"); // 	ED_edge_type(&fwdedgea.out) = 1;
UNSUPPORTED("4tjj1vbw4mog2qlouazrdirvw"); // 	ED_head_port(&fwdedgea.out).p.x = ED_head_port(&fwdedgea.out).p.y = 0;
UNSUPPORTED("8kdma1vi9aibo7isrge0lunrh"); // 	ED_to_orig(&fwdedgea.out) = e;
UNSUPPORTED("eih8eaai768x1un5mixrtgstp"); // 	e = &fwdedgea.out;
UNSUPPORTED("bxkpl0bp0qhtxaj6rspd19d1k"); // 	hackflag = NOT(0);
    } else {
	if ((ED_tree_index(e) & BWDEDGE)!=0) {
	    MAKEFWDEDGE(fwdedgea.out, e);
	    e = fwdedgea.out;
	}
    }
    fe = e;
    
    /* compute the spline points for the edge */
    
    if ((et == ET_LINE) && (pointn[0] = makeLineEdge (g, fe, zz.pointfs, hn.unsupported()))!=0) {
    }
    else {
	boolean splines = (et == ET_SPLINE);
	boxn = 0;
	pointn[0] = 0;
	segfirst = e;
	tn = agtail(e);
	hn = aghead(e);
	b.___(maximal_bbox(g, sp, tn, null, e));
	tend.nb.___(b);
	beginpath(zz, P, e, REGULAREDGE, tend, spline_merge(tn));
	b.UR.y = tend.boxes[tend.boxn[0] - 1].UR.y;
	b.LL.y = tend.boxes[tend.boxn[0] - 1].LL.y;
	b.___(makeregularend(b, BOTTOM,
	    	   ND_coord(tn).y - GD_rank(g).get__(ND_rank(tn)).ht1));
	if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
	    tend.boxes[tend.boxn[0]++].___(b);
	longedge = 0;
	smode = false; si = -1;
	while (ND_node_type(hn) == VIRTUAL && !((Boolean)zz.sinfo.splineMerge.exe(zz, hn)).booleanValue()) {
	    longedge = 1;
	    zz.boxes[boxn++].___(rank_box(sp, g, ND_rank(tn)));
	    if (!smode
	        && ((sl = straight_len(hn)) >=
	    	((GD_has_labels(g) & EDGE_LABEL)!=0 ? 4 + 1 : 2 + 1))) {
	        smode = true;
	        si = 1; sl -= 2;
	    }
	    if (!smode || si > 0) {
	        si--;
	        zz.boxes[boxn++].___(maximal_bbox(g, sp, hn, e, ND_out(hn).list.get_(0)));
	        e = ND_out(hn).list.get_(0);
	        tn = agtail(e);
	        hn = aghead(e);
	        continue;
	    }
	    hend.nb.___(maximal_bbox(g, sp, hn, e, ND_out(hn).list.get_(0)));
	    endpath(zz, P, e, 1, hend, spline_merge(aghead(e)));
	    b.___(makeregularend(hend.boxes[hend.boxn[0] - 1], (1<<2),
	    	       ND_coord(hn).y + GD_rank(g).get__(ND_rank(hn)).ht2));
	    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("1crhubfzekx1qi2ti9ajqsfoc"); // 	        hend.boxes[hend.boxn++] = b;
	    P.end.theta = M_PI / 2;
	    P.end.constrained= true;
	    completeregularpath(P, segfirst, e, tend, hend, zz.boxes, boxn, 1);
	    if (splines) ps = routesplines(zz, P, pn);
	    else {
UNSUPPORTED("biyp75vm751j3qmqacagfej4b"); // 		ps = routepolylines (P, &pn);
UNSUPPORTED("4fmdmgg43suu7ppgrcaofwqzh"); // 		if ((et == (1 << 1)) && (pn > 4)) {
UNSUPPORTED("2ftwbx9nsy10ldzds3ej4hxi7"); // 		    ps[1] = ps[0];
UNSUPPORTED("2fedie0btpk43e74p462n0y4n"); // 		    ps[3] = ps[2] = ps[pn-1];
UNSUPPORTED("3fwatldph3opctz8aieqd214d"); // 		    pn = 4;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
	    }
	    if (pn[0] == 0)
	        return;
	    if (pointn[0] + pn[0] > zz.numpts) {
                /* This should be enough to include 3 extra points added by
                 * straight_path below.
                 */
UNSUPPORTED("k37sqlxbjikqg4xdonnvefo3"); // 		numpts = 2*(pointn+pn); 
UNSUPPORTED("8kbxhk7qirj3tr7hn1ukwar3h"); // 		pointfs = RALLOC(numpts, pointfs, pointf);
	    }
	    for (i = 0; i < pn[0]; i++) {
		zz.pointfs.get__(pointn[0]++).___(ps.get__(i));
	    }
	    e = straight_path((ST_Agedge_s)ND_out(hn).list.get_(0), sl, zz.pointfs, pointn);
	    recover_slack(zz, segfirst, P);
	    segfirst = e;
	    tn = agtail(e);
	    hn = aghead(e);
	    boxn = 0;
	    tend.nb.___(maximal_bbox(g, sp, tn, (ST_Agedge_s) ND_in(tn).list.get_(0), e));
	    beginpath(zz, P, e, 1, tend, spline_merge(tn));
	    b.___(makeregularend(tend.boxes[tend.boxn[0] - 1], (1<<0),
	    	       ND_coord(tn).y - GD_rank(g).get__(ND_rank(tn)).ht1));
	    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("cjx6tldge3otk1pk6ks1pkn2w"); // 	        tend.boxes[tend.boxn++] = b;
	    P.start.theta = -M_PI / 2;
	    P.start.constrained= true;
	    smode = false;
	}
	zz.boxes[boxn++].___(rank_box(sp, g, ND_rank(tn)));
	b.___(maximal_bbox(g, sp, hn, e, null));
	hend.nb.___(b);
	endpath(zz, P, (ST_Agedge_s) (hackflag!=0 ? fwdedgeb.out : e), 1, hend, spline_merge(aghead(e)));
	b.UR.y = hend.boxes[hend.boxn[0] - 1].UR.y;
	b.LL.y = hend.boxes[hend.boxn[0] - 1].LL.y;
	b.___(makeregularend(b, (1<<2),
	    	   ND_coord(hn).y + GD_rank(g).get__(ND_rank(hn)).ht2));
	if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
	    {
	    hend.boxes[hend.boxn[0]].___(b);
	    hend.boxn[0] = hend.boxn[0]+1;
	    }
	completeregularpath(P, segfirst, e, tend, hend, zz.boxes, boxn,
	    		longedge);
	if (splines) ps = routesplines(zz, P, pn);
	else ps = routepolylines (zz, P, pn);
	if ((et == ET_LINE) && (pn[0] > 4)) {
	    /* Here we have used the polyline case to handle
	     * an edge between two nodes on adjacent ranks. If the
	     * results really is a polyline, straighten it.
	     */
UNSUPPORTED("1u7e6lrkiipml54kkm7ylw56t"); // 	    ps[1] = ps[0];
UNSUPPORTED("76bh6z0xok01d0gdybxcx4ful"); // 	    ps[3] = ps[2] = ps[pn-1];
UNSUPPORTED("95b6xp8h5ai070bekyjhmiehh"); // 	    pn = 4;
        }
	if (pn[0] == 0)
	    return;
	if (pointn[0] + pn[0] > zz.numpts) {
UNSUPPORTED("c6ux5effs02grz7xh3k8ernda"); // 	    numpts = 2*(pointn+pn); 
UNSUPPORTED("bedaqcn9h03q6ia6zbezuee1m"); // 	    pointfs = RALLOC(numpts, pointfs, pointf);
	}
	for (i = 0; i < pn[0]; i++) {
	    zz.pointfs.get__(pointn[0]).___(ps.get__(i));
	    pointn[0]++;
	}
	recover_slack(zz, segfirst, P);
	hn = hackflag!=0 ? aghead(fwdedgeb.out) : aghead(e);
    }
    /* make copies of the spline points, one per multi-edge */
    if (cnt == 1) {
	clip_and_install(zz, fe, hn, zz.pointfs, pointn[0], zz.sinfo);
	return;
    }
    dx = sp.Multisep * (cnt - 1) / 2;
    for (i = 1; i < pointn[0] - 1; i++)
	zz.pointfs.get__(i).x = zz.pointfs.get__(i).x - dx;
    if (zz.numpts > zz.numpts2) {
UNSUPPORTED("9ubr4m7bdv5f5ldk2ta6yw3up"); // 	numpts2 = numpts; 
UNSUPPORTED("8qwp7ddy5ztgam63fzfjmu890"); // 	pointfs2 = RALLOC(numpts2, pointfs2, pointf);
    }
    for (i = 0; i < pointn[0]; i++)
	zz.pointfs2.get__(i).___(zz.pointfs.get__(i));
    clip_and_install(zz, fe, hn, zz.pointfs2, pointn[0], zz.sinfo);
    for (j = 1; j < cnt; j++) {
	e = edges.get_(ind + j);
	if ((ED_tree_index(e) & 32)!=0) {
	    MAKEFWDEDGE(fwdedge.out, e);
	    e = fwdedge.out;
	}
	for (i = 1; i < pointn[0] - 1; i++)
	    zz.pointfs.get__(i).x = zz.pointfs.get__(i).x + sp.Multisep;
	for (i = 0; i < pointn[0]; i++)
	    zz.pointfs2.get__(i).___(zz.pointfs.get__(i));
	clip_and_install(zz, e, aghead(e), zz.pointfs2, pointn[0], zz.sinfo);
    }
} finally {
LEAVING("30wfq1dby4t07hft9io52nq6z","make_regular_edge");
}
}




//3 va61hggynvb6z6j34w7otmab
// static void completeregularpath(path * P, edge_t * first, edge_t * last, 		    pathend_t * tendp, pathend_t * hendp, boxf * boxes, 		    int boxn, int flag) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="completeregularpath", key="va61hggynvb6z6j34w7otmab", definition="static void completeregularpath(path * P, edge_t * first, edge_t * last, 		    pathend_t * tendp, pathend_t * hendp, boxf * boxes, 		    int boxn, int flag)")
public static void completeregularpath(ST_path P, ST_Agedge_s first, ST_Agedge_s last, ST_pathend_t tendp, ST_pathend_t hendp, ST_boxf[] boxes, int boxn, int flag) {
ENTERING("va61hggynvb6z6j34w7otmab","completeregularpath");
try {
    ST_Agedge_s uleft, uright, lleft, lright;
    int i, fb, lb;
    ST_splines spl;
    CArray<ST_pointf> pp;
    int pn;
    
    fb = lb = -1;
    uleft = uright = null;
    uleft = top_bound(first, -1);
    uright = top_bound(first, 1);
    if (uleft!=null) {
	if ((spl = getsplinepoints(uleft)) == null) return;
	pp = spl.list.get__(0).list;
       	pn = spl.list.get__(0).size;
    }
    if (uright!=null) {
	if ((spl = getsplinepoints(uright)) == null) return;
	pp = spl.list.get__(0).list;
       	pn = spl.list.get__(0).size;
    }
    lleft = lright = null;
    lleft = bot_bound(last, -1);
    lright = bot_bound(last, 1);
    if (lleft!=null) {
	if ((spl = getsplinepoints(lleft)) == null) return;
	pp = spl.list.get__(spl.size - 1).list;
       	pn = spl.list.get__(spl.size - 1).size;
    }
    if (lright!=null) {
	if ((spl = getsplinepoints(lright)) == null) return;
	pp = spl.list.get__(spl.size - 1).list;
       	pn = spl.list.get__(spl.size - 1).size;
    }
    for (i = 0; i < tendp.boxn[0]; i++)
	add_box(P, (tendp).boxes[i]);
    fb = P.nbox + 1;
    lb = fb + boxn - 3;
    for (i = 0; i < boxn; i++)
	add_box(P, boxes[i]);
    for (i = hendp.boxn[0] - 1; i >= 0; i--)
	add_box(P, (hendp).boxes[i]);
    adjustregularpath(P, fb, lb);
} finally {
LEAVING("va61hggynvb6z6j34w7otmab","completeregularpath");
}
}




//3 3wwhczhpkcnflwr1l9wcga7tq
// static boxf makeregularend(boxf b, int side, double y) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="makeregularend", key="3wwhczhpkcnflwr1l9wcga7tq", definition="static boxf makeregularend(boxf b, int side, double y)")
public static ST_boxf makeregularend(final ST_boxf b, int side, double y) {
// WARNING!! STRUCT
return makeregularend_w_(b.copy(), side, y).copy();
}
private static ST_boxf makeregularend_w_(final ST_boxf b, int side, double y) {
ENTERING("3wwhczhpkcnflwr1l9wcga7tq","makeregularend");
try {
    final ST_boxf newb = new ST_boxf();
    switch (side) {
    case (1<<0):
	newb.___(boxfof(b.LL.x, y, b.UR.x, b.LL.y));
	break;
    case (1<<2):
	newb.___(boxfof(b.LL.x, b.UR.y, b.UR.x, y));
	break;
    }
    return newb;
} finally {
LEAVING("3wwhczhpkcnflwr1l9wcga7tq","makeregularend");
}
}




//3 88xrlzjovkxcnay9b2y5zyiid
// static void adjustregularpath(path * P, int fb, int lb) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="adjustregularpath", key="88xrlzjovkxcnay9b2y5zyiid", definition="static void adjustregularpath(path * P, int fb, int lb)")
public static void adjustregularpath(ST_path P, int fb, int lb) {
ENTERING("88xrlzjovkxcnay9b2y5zyiid","adjustregularpath");
try {
	ST_boxf bp1, bp2;
    int i, x;
    for (i = fb-1; i < lb+1; i++) {
	bp1 = (ST_boxf) P.boxes[i];
	if ((i - fb) % 2 == 0) {
	    if (bp1.LL.x >= bp1.UR.x) {
		x = (int)((bp1.LL.x + bp1.UR.x) / 2);
		bp1.LL.x = x - 8;
		bp1.UR.x = x + 8;
	    }
	} else {
	    if (bp1.LL.x + 16 > bp1.UR.x) {
		x = (int)((bp1.LL.x + bp1.UR.x) / 2);
		bp1.LL.x = x - 8;
		bp1.UR.x = x + 8;
	    }
	}
    }
    for (i = 0; i < P.nbox - 1; i++) {
	bp1 = (ST_boxf) P.boxes[i];
	bp2 = (ST_boxf) P.boxes[i+1];
	if (i >= fb && i <= lb && (i - fb) % 2 == 0) {
	    if (bp1.LL.x + 16 > bp2.UR.x)
		bp2.UR.x = bp1.LL.x + 16;
	    if (bp1.UR.x - 16 < bp2.LL.x)
		bp2.LL.x = bp1.UR.x - 16;
	} else if (i + 1 >= fb && i < lb && (i + 1 - fb) % 2 == 0) {
	    if (bp1.LL.x + 16 > bp2.UR.x)
		bp1.LL.x = bp2.UR.x - 16;
	    if (bp1.UR.x - 16 < bp2.LL.x)
		bp1.UR.x = bp2.LL.x + 16;
	} 
    }
} finally {
LEAVING("88xrlzjovkxcnay9b2y5zyiid","adjustregularpath");
}
}




//3 bajn5vx0isu427n6dh131b985
// static boxf rank_box(spline_info_t* sp, graph_t * g, int r) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="rank_box", key="bajn5vx0isu427n6dh131b985", definition="static boxf rank_box(spline_info_t* sp, graph_t * g, int r)")
public static ST_boxf rank_box(ST_spline_info_t sp, ST_Agraph_s g, int r) {
// WARNING!! STRUCT
return rank_box_w_(sp, g, r).copy();
}
private static ST_boxf rank_box_w_(ST_spline_info_t sp, ST_Agraph_s g, int r) {
ENTERING("bajn5vx0isu427n6dh131b985","rank_box");
try {
    final ST_boxf b = new ST_boxf();
    ST_Agnode_s /* *right0, *right1, */  left0, left1;
    b.___(sp.Rank_box[r]);
    if (b.LL.x == b.UR.x) {
	left0 = (ST_Agnode_s) GD_rank(g).get__(r).v.get_(0);
	/* right0 = GD_rank(g)[r].v[GD_rank(g)[r].n - 1]; */
	left1 = (ST_Agnode_s) GD_rank(g).get__(r + 1).v.get_(0);
	/* right1 = GD_rank(g)[r + 1].v[GD_rank(g)[r + 1].n - 1]; */
	b.LL.x = sp.LeftBound;
	b.LL.y = ND_coord(left1).y + GD_rank(g).get__(r + 1).ht2;
	b.UR.x = sp.RightBound;
	b.UR.y = ND_coord(left0).y - GD_rank(g).get__(r).ht1;
	sp.Rank_box[r].___(b);
    }
    return b;
} finally {
LEAVING("bajn5vx0isu427n6dh131b985","rank_box");
}
}




//3 6qwcnugx2ytjrvi5rgxzyzg5i
// static int straight_len(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="straight_len", key="6qwcnugx2ytjrvi5rgxzyzg5i", definition="static int straight_len(node_t * n)")
public static int straight_len(ST_Agnode_s n) {
ENTERING("6qwcnugx2ytjrvi5rgxzyzg5i","straight_len");
try {
    int cnt = 0;
    ST_Agnode_s v;
    v = n;
    while (true) {
	v = (ST_Agnode_s) aghead(ND_out(v).list.get_(0));
	if (ND_node_type(v) != 1)
	    break;
	if ((ND_out(v).size != 1) || (ND_in(v).size != 1))
	    break;
	if (ND_coord(v).x != ND_coord(n).x)
	    break;
	cnt++;
    }
    return cnt;
} finally {
LEAVING("6qwcnugx2ytjrvi5rgxzyzg5i","straight_len");
}
}




//3 15pgjjuil2c1rjldu29j07gbz
// static edge_t *straight_path(edge_t * e, int cnt, pointf * plist, int *np) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="", key="15pgjjuil2c1rjldu29j07gbz", definition="static edge_t *straight_path(edge_t * e, int cnt, pointf * plist, int *np)")
public static ST_Agedge_s straight_path(ST_Agedge_s e, int cnt, CArray<ST_pointf> plist, int np[]) {
ENTERING("15pgjjuil2c1rjldu29j07gbz","straight_path");
try {
    int n = np[0];
    ST_Agedge_s f = e;
    while ((cnt--)!=0)
	f = (ST_Agedge_s) ND_out(aghead(f)).list.get_(0);
    plist.get__(np[0]++).___(plist.get__(n - 1));
    plist.get__(np[0]++).___(plist.get__(n - 1));
    plist.get__(np[0]).___(ND_coord(agtail(f)));  /* will be overwritten by next spline */
    return f;
} finally {
LEAVING("15pgjjuil2c1rjldu29j07gbz","straight_path");
}
}




//3 4ilkzqtegd5uffawb4qcjthu1
// static void recover_slack(edge_t * e, path * p) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="recover_slack", key="4ilkzqtegd5uffawb4qcjthu1", definition="static void recover_slack(edge_t * e, path * p)")
public static void recover_slack(Globals zz, ST_Agedge_s e, ST_path p) {
ENTERING("4ilkzqtegd5uffawb4qcjthu1","recover_slack");
try {
    int b;
    ST_Agnode_s vn;
    b = 0;			/* skip first rank box */
    for (vn = aghead(e);
	 ND_node_type(vn) == 1 && !((Boolean)zz.sinfo.splineMerge.exe(zz, vn)).booleanValue();
	 vn = aghead(ND_out(vn).list.get_(0))) {
	while ((b < p.nbox) && (p.boxes[b].LL.y > ND_coord(vn).y))
	    b++;
	if (b >= p.nbox)
	    break;
	if (p.boxes[b].UR.y < ND_coord(vn).y)
	    continue;
	if (ND_label(vn)!=null)
	    resize_vn(vn, (int)p.boxes[b].LL.x, (int)p.boxes[b].UR.x,
		      (int)(p.boxes[b].UR.x + ND_rw(vn)));
	else
	    resize_vn(vn, (int)p.boxes[b].LL.x, (int)((p.boxes[b].LL.x +
					     p.boxes[b].UR.x) / 2),
		      (int)p.boxes[b].UR.x);
    }
} finally {
LEAVING("4ilkzqtegd5uffawb4qcjthu1","recover_slack");
}
}




//3 3vmg1q1r0eb14etvjdk4cukpd
// static void resize_vn(vn, lx, cx, rx) node_t *vn
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="resize_vn", key="3vmg1q1r0eb14etvjdk4cukpd", definition="static void resize_vn(vn, lx, cx, rx) node_t *vn")
public static void resize_vn(ST_Agnode_s vn, int lx, int cx, int rx) {
ENTERING("3vmg1q1r0eb14etvjdk4cukpd","resize_vn");
try {
    ND_coord(vn).x = cx;
    ND_lw(vn, cx - lx);
    ND_rw(vn, rx - cx);
} finally {
LEAVING("3vmg1q1r0eb14etvjdk4cukpd","resize_vn");
}
}




//3 9t0v5wicmjuc3ij9hko6iawle
// static edge_t *top_bound(edge_t * e, int side) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="", key="9t0v5wicmjuc3ij9hko6iawle", definition="static edge_t *top_bound(edge_t * e, int side)")
public static ST_Agedge_s top_bound(ST_Agedge_s e, int side) {
ENTERING("9t0v5wicmjuc3ij9hko6iawle","top_bound");
try {
    ST_Agedge_s f, ans = null;
    int i;
    for (i = 0; (f = (ST_Agedge_s) ND_out(agtail(e)).list.get_(i))!=null; i++) {
	if (side * (ND_order(aghead(f)) - ND_order(aghead(e))) <= 0)
	    continue;
	if ((ED_spl(f) == null)
	    && ((ED_to_orig(f) == null) || (ED_spl(ED_to_orig(f)) == null)))
	    continue;
	if ((ans == null)
	    || (side * (ND_order(aghead(ans)) - ND_order(aghead(f))) > 0))
	    ans = f;
    }
    return ans;
} finally {
LEAVING("9t0v5wicmjuc3ij9hko6iawle","top_bound");
}
}




//3 9fsg0uiyhtrayd4mimmc0i25e
// static edge_t *bot_bound(edge_t * e, int side) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="", key="9fsg0uiyhtrayd4mimmc0i25e", definition="static edge_t *bot_bound(edge_t * e, int side)")
public static ST_Agedge_s bot_bound(ST_Agedge_s e, int side) {
ENTERING("9fsg0uiyhtrayd4mimmc0i25e","bot_bound");
try {
    ST_Agedge_s f, ans = null;
    int i;
    for (i = 0; (f = (ST_Agedge_s) ND_in(aghead(e)).list.get_(i))!=null; i++) {
	if (side * (ND_order(agtail(f)) - ND_order(agtail(e))) <= 0)
	    continue;
	if ((ED_spl(f) == null)
	    && ((ED_to_orig(f) == null) || (ED_spl(ED_to_orig(f)) == null)))
	    continue;
	if ((ans == null)
	    || (side * (ND_order(agtail(ans)) - ND_order(agtail(f))) > 0))
	    ans = f;
    }
    return ans;
} finally {
LEAVING("9fsg0uiyhtrayd4mimmc0i25e","bot_bound");
}
}




//3 65uvkv1mextaah5m997ibe3qs
// static int cl_vninside(graph_t * cl, node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="cl_vninside", key="65uvkv1mextaah5m997ibe3qs", definition="static int cl_vninside(graph_t * cl, node_t * n)")
public static boolean cl_vninside(ST_Agraph_s cl, ST_Agnode_s n) {
ENTERING("65uvkv1mextaah5m997ibe3qs","cl_vninside");
try {
    return (BETWEEN(GD_bb(cl).LL.x, (double)(ND_coord(n).x), GD_bb(cl).UR.x) &&
	    BETWEEN(GD_bb(cl).LL.y, (double)(ND_coord(n).y), GD_bb(cl).UR.y));
} finally {
LEAVING("65uvkv1mextaah5m997ibe3qs","cl_vninside");
}
}




//3 dzvvmxkya868w5x78lkvchigk
// static Agraph_t *cl_bound(graph_t* g,  node_t *n, node_t *adj) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="", key="dzvvmxkya868w5x78lkvchigk", definition="static Agraph_t *cl_bound(graph_t* g,  node_t *n, node_t *adj)")
public static ST_Agraph_s cl_bound(ST_Agraph_s g,  ST_Agnode_s n, ST_Agnode_s adj) {
ENTERING("dzvvmxkya868w5x78lkvchigk","cl_bound");
try {
    ST_Agraph_s rv, cl, tcl, hcl;
    ST_Agedge_s orig;
    rv = null;
    if (ND_node_type(n) == 0)
	tcl = hcl = ND_clust(n);
    else {
	orig = ED_to_orig(ND_out(n).list.get_(0));
	tcl = ND_clust(agtail(orig));
	hcl = ND_clust(aghead(orig));
    }
    if (ND_node_type(adj) == 0) {
	cl = ( ND_clust(adj) == g ? null:ND_clust(adj));
	if (cl!=null && (cl != tcl) && (cl != hcl))
	    rv = cl;
    } else {
	orig = ED_to_orig(ND_out(adj).list.get_(0));
	cl = ( ND_clust(agtail(orig)) == g ? null:ND_clust(agtail(orig)));
	if (cl!=null && (cl != tcl) && (cl != hcl) && cl_vninside(cl, adj))
	    rv = cl;
	else {
	    cl = ( ND_clust(aghead(orig)) == g ? null:ND_clust(aghead(orig)));
	    if (cl!=null && (cl != tcl) && (cl != hcl) && cl_vninside(cl, adj))
		rv = cl;
	}
    }
    return rv;
} finally {
LEAVING("dzvvmxkya868w5x78lkvchigk","cl_bound");
}
}


private final static int FUDGE = 4;

//3 6qwgl36ugfnduc5x59ohuewv1
// static boxf maximal_bbox(graph_t* g, spline_info_t* sp, node_t* vn, edge_t* ie, edge_t* oe) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="maximal_bbox", key="6qwgl36ugfnduc5x59ohuewv1", definition="static boxf maximal_bbox(graph_t* g, spline_info_t* sp, node_t* vn, edge_t* ie, edge_t* oe)")
public static ST_boxf maximal_bbox(ST_Agraph_s g, ST_spline_info_t sp, ST_Agnode_s vn, ST_Agedge_s ie, ST_Agedge_s oe) {
// WARNING!! STRUCT
return maximal_bbox_w_(g, sp, vn, ie, oe).copy();
}
private static ST_boxf maximal_bbox_w_(ST_Agraph_s g, ST_spline_info_t sp, ST_Agnode_s vn, ST_Agedge_s ie, ST_Agedge_s oe) {
ENTERING("6qwgl36ugfnduc5x59ohuewv1","maximal_bbox");
try {
    double b, nb;
    ST_Agraph_s left_cl, right_cl;
    ST_Agnode_s left, right;
    final ST_boxf rv = new ST_boxf();
    
    left_cl = right_cl = null;
    
    /* give this node all the available space up to its neighbors */
    b = (double)(ND_coord(vn).x - ND_lw(vn) - FUDGE);
    if ((left = neighbor(g, vn, ie, oe, -1))!=null) {
	if ((left_cl = cl_bound(g, vn, left))!=null)
	    nb = GD_bb(left_cl).UR.x + (double)(sp.Splinesep);
	else {
	    nb = (double)(ND_coord(left).x + ND_mval(left));
	    if (ND_node_type(left) == NORMAL)
		nb += GD_nodesep(g) / 2.;
	    else
		nb += (double)(sp.Splinesep);
	}
	if (nb < b)
	    b = nb;
	rv.LL.x = ROUND(b);
    } else
	rv.LL.x = Math.min(ROUND(b), sp.LeftBound);
    
    /* we have to leave room for our own label! */
    if ((ND_node_type(vn) == VIRTUAL) && (ND_label(vn)!=null))
	b = (double)(ND_coord(vn).x + 10);
    else
	b = (double)(ND_coord(vn).x + ND_rw(vn) + FUDGE);
    if ((right = neighbor(g, vn, ie, oe, 1))!=null) {
	if ((right_cl = cl_bound(g, vn, right))!=null)
	    nb = GD_bb(right_cl).LL.x - (double)(sp.Splinesep);
	else {
	    nb = ND_coord(right).x - ND_lw(right);
	    if (ND_node_type(right) == NORMAL)
		nb -= GD_nodesep(g) / 2.;
	    else
		nb -= (double)(sp.Splinesep);
	}
	if (nb > b)
	    b = nb;
	rv.UR.x = ROUND(b);
    } else
	rv.UR.x = Math.max(ROUND(b), sp.RightBound);
    
    if ((ND_node_type(vn) == VIRTUAL) && (ND_label(vn)!=null)) {
	rv.UR.x = rv.UR.x - ND_rw(vn);
	if (rv.UR.x < rv.LL.x) rv.UR.x = ND_coord(vn).x;
    }
    
    rv.LL.y = ND_coord(vn).y - GD_rank(g).get__(ND_rank(vn)).ht1;
    rv.UR.y = ND_coord(vn).y + GD_rank(g).get__(ND_rank(vn)).ht2;
    return rv;
} finally {
LEAVING("6qwgl36ugfnduc5x59ohuewv1","maximal_bbox");
}
}




//3 18pm6r3xcy90f0xi5hpm9jdhk
// static node_t * neighbor(graph_t* g, node_t *vn, edge_t *ie, edge_t *oe, int dir) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="neighbor", key="18pm6r3xcy90f0xi5hpm9jdhk", definition="static node_t * neighbor(graph_t* g, node_t *vn, edge_t *ie, edge_t *oe, int dir)")
public static ST_Agnode_s neighbor(ST_Agraph_s g, ST_Agnode_s vn, ST_Agedge_s ie, ST_Agedge_s oe, int dir) {
ENTERING("18pm6r3xcy90f0xi5hpm9jdhk","neighbor");
try {
    int i;
    ST_Agnode_s n, rv = null;
    CArray<ST_rank_t> rank = GD_rank(g).plus_(ND_rank(vn));
    for (i = ND_order(vn) + dir; ((i >= 0) && (i < rank.get__(0).n)); i += dir) {
	n = rank.get__(0).v.get_(i);
	if ((ND_node_type(n) == 1) && (ND_label(n)!=null)) {
	    rv = n;
	    break;
	}
	if (ND_node_type(n) == 0) {
	    rv = n;
	    break;
	}
	if (pathscross(n, vn, ie, oe) == false) {
	    rv = n;
	    break;
	}
    }
    return rv;
} finally {
LEAVING("18pm6r3xcy90f0xi5hpm9jdhk","neighbor");
}
}




//3 f4q0oqe165s9pl5k0th5noeyv
// static boolean pathscross(n0, n1, ie1, oe1) node_t *n0, *n1
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotsplines.c", name="pathscross", key="f4q0oqe165s9pl5k0th5noeyv", definition="static boolean pathscross(n0, n1, ie1, oe1) node_t *n0, *n1")
public static boolean pathscross(ST_Agnode_s n0, ST_Agnode_s n1, ST_Agedge_s ie1, ST_Agedge_s oe1) {
ENTERING("f4q0oqe165s9pl5k0th5noeyv","pathscross");
try {
    ST_Agedge_s e0, e1;
    ST_Agnode_s na, nb;
    boolean order;
    int cnt;
    order = (ND_order(n0) > ND_order(n1));
    if ((ND_out(n0).size != 1) && (ND_out(n0).size != 1))
	return false;
    e1 = oe1;
    if (ND_out(n0).size == 1 && e1!=null) {
	e0 = (ST_Agedge_s) ND_out(n0).list.get_(0);
	for (cnt = 0; cnt < 2; cnt++) {
	    if ((na = aghead(e0)) == (nb = aghead(e1)))
		break;
	    if (order != (ND_order(na) > ND_order(nb)))
		return true;
	    if ((ND_out(na).size != 1) || (ND_node_type(na) == 0))
		break;
	    e0 = (ST_Agedge_s) ND_out(na).list.get_(0);
	    if ((ND_out(nb).size != 1) || (ND_node_type(nb) == 0))
		break;
	    e1 = (ST_Agedge_s) ND_out(nb).list.get_(0);
	}
    }
    e1 = ie1;
    if (ND_in(n0).size == 1 && e1!=null) {
	e0 = (ST_Agedge_s) ND_in(n0).list.get_(0);
	for (cnt = 0; cnt < 2; cnt++) {
	    if ((na = agtail(e0))== (nb = agtail(e1)))
		break;
	    if (order != (ND_order(na) > ND_order(nb)))
		return true;
	    if ((ND_in(na).size != 1) || (ND_node_type(na) == 0))
		break;
	    e0 = (ST_Agedge_s) ND_in(na).list.get_(0);
	    if ((ND_in(nb).size != 1) || (ND_node_type(nb) == 0))
		break;
	    e1 = (ST_Agedge_s) ND_in(nb).list.get_(0);
	}
    }
    return false;
} finally {
LEAVING("f4q0oqe165s9pl5k0th5noeyv","pathscross");
}
}


}
