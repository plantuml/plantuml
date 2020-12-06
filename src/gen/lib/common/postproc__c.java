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
import static gen.lib.cgraph.attr__c.agattr;
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.graph__c.agnnodes;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.geom__c.ccwrotatepf;
import static gen.lib.common.splines__c.edgeMidpoint;
import static gen.lib.common.splines__c.getsplinepoints;
import static gen.lib.common.utils__c.late_bool;
import static gen.lib.common.utils__c.updateBB;
import static gen.lib.label.xlabels__c.placeLabels;
import static h.ST_pointf.pointfof;
import static smetana.core.JUtils.NEQ;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.EDGE_LABEL;
import static smetana.core.Macro.EDGE_XLABEL;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_label;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_label;
import static smetana.core.Macro.ED_xlabel;
import static smetana.core.Macro.ET_NONE;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_border;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.GD_label_pos;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_rankdir;
import static smetana.core.Macro.GRAPH_LABEL;
import static smetana.core.Macro.HEAD_LABEL;
import static smetana.core.Macro.IGNORED;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_height;
import static smetana.core.Macro.ND_width;
import static smetana.core.Macro.ND_xlabel;
import static smetana.core.Macro.NODE_XLABEL;
import static smetana.core.Macro.NOTI;
import static smetana.core.Macro.TAIL_LABEL;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsym_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_cinfo_t;
import h.ST_label_params_t;
import h.ST_object_t;
import h.ST_pointf;
import h.ST_splines;
import h.ST_textlabel_t;
import h.ST_xlabel_t;
import smetana.core.CArray;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;

public class postproc__c {

//3 dajapw16wus3rwimkrk5ihi2b
// static pointf map_point(pointf p) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="map_point", key="dajapw16wus3rwimkrk5ihi2b", definition="static pointf map_point(pointf p)")
public static ST_pointf map_point(final ST_pointf p) {
// WARNING!! STRUCT
return map_point_w_(p.copy()).copy();
}
private static ST_pointf map_point_w_(final ST_pointf p) {
ENTERING("dajapw16wus3rwimkrk5ihi2b","map_point");
try {
    p.___(ccwrotatepf(p, Z.z().Rankdir * 90));
    p.x -= Z.z().Offset.x;
    p.y -= Z.z().Offset.y;
    return p;
} finally {
LEAVING("dajapw16wus3rwimkrk5ihi2b","map_point");
}
}




//3 bvq3vvonvotn47mfe5zsvchie
// static void map_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="map_edge", key="bvq3vvonvotn47mfe5zsvchie", definition="static void map_edge(edge_t * e)")
public static void map_edge(ST_Agedge_s e) {
ENTERING("bvq3vvonvotn47mfe5zsvchie","map_edge");
try {
    int j, k;
    final ST_bezier bz = new ST_bezier();
    
    if (ED_spl(e) == null) {
	if ((Z.z().Concentrate == false) && (ED_edge_type(e) != IGNORED))
	    System.err.println("lost %s %s edge\n"+ agnameof(agtail(e))+
		  agnameof(aghead(e)));
	return;
    }
    for (j = 0; j < ED_spl(e).size; j++) {
	bz.___(ED_spl(e).list.get__(j));
	for (k = 0; k < bz.size; k++) {
	    bz.list.get__(k).___(map_point(bz.list.get__(k)));
	}
	if (bz.sflag!=0)
	    ED_spl(e).list.get__(j).sp.___(map_point(ED_spl(e).list.get__(j).sp));
	if (bz.eflag!=0) {
	    ED_spl(e).list.get__(j).ep.___(map_point(ED_spl(e).list.get__(j).ep));
    }
    }
    if (ED_label(e)!=null)
	ED_label(e).pos.___(map_point(ED_label(e).pos));
    if (ED_xlabel(e)!=null)
UNSUPPORTED("al3tnq9zjjqeq1ll7qdxyu3ja"); // 	ED_xlabel(e)->pos = map_point(ED_xlabel(e)->pos);
    /* vladimir */
    if (ED_head_label(e)!=null)
    	ED_head_label(e).pos.___(map_point(ED_head_label(e).pos));
    if (ED_tail_label(e)!=null)
    	ED_tail_label(e).pos.___(map_point(ED_tail_label(e).pos));
} finally {
LEAVING("bvq3vvonvotn47mfe5zsvchie","map_edge");
}
}




//3 a3hf82rxsojxbunj6p8a6bkse
// void translate_bb(graph_t * g, int rankdir) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="translate_bb", key="a3hf82rxsojxbunj6p8a6bkse", definition="void translate_bb(graph_t * g, int rankdir)")
public static void translate_bb(ST_Agraph_s g, int rankdir) {
ENTERING("a3hf82rxsojxbunj6p8a6bkse","translate_bb");
try {
    int c;
    final ST_boxf bb = new ST_boxf(), new_bb = new ST_boxf();
    bb.___(GD_bb(g));
    if (rankdir == 1 || rankdir == 2) {
UNSUPPORTED("d4wrtj0h7lkb0e0vernd9czq9"); // 	new_bb.LL = map_point(pointfof(bb.LL.x, bb.UR.y));
UNSUPPORTED("crysiae5zxc69cj3v2ygfs8xn"); // 	new_bb.UR = map_point(pointfof(bb.UR.x, bb.LL.y));
    } else {
	new_bb.LL.___(map_point(pointfof(bb.LL.x, bb.LL.y)));
	new_bb.UR.___(map_point(pointfof(bb.UR.x, bb.UR.y)));
    }
    GD_bb(g).___(new_bb);
    if (GD_label(g)!=null) {
	GD_label(g).pos.___(map_point(GD_label(g).pos));
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	translate_bb((ST_Agraph_s) GD_clust(g).get_(c), rankdir);
} finally {
LEAVING("a3hf82rxsojxbunj6p8a6bkse","translate_bb");
}
}




//3 h4i5qxnd7hlrew919abswd13
// static void translate_drawing(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="translate_drawing", key="h4i5qxnd7hlrew919abswd13", definition="static void translate_drawing(graph_t * g)")
public static void translate_drawing(ST_Agraph_s g) {
ENTERING("h4i5qxnd7hlrew919abswd13","translate_drawing");
try {
    ST_Agnode_s v;
    ST_Agedge_s e;
    boolean shift = (Z.z().Offset.x!=0.0 || Z.z().Offset.y!=0.0);
    if (N(shift) && N(Z.z().Rankdir))
	return;
    for (v = agfstnode(g); v!=null; v = agnxtnode(g, v)) {
	if (Z.z().Rankdir!=0)
UNSUPPORTED("e0j848r4j1j7sojfht5gwikvi"); // 	    gv_nodesize(v, 0);
	ND_coord(v).___(map_point(ND_coord(v)));
	if (ND_xlabel(v)!=null)
UNSUPPORTED("3fy0l7w2v24hzrvlpstpknwl7"); // 	    ND_xlabel(v)->pos = map_point(ND_xlabel(v)->pos);
	if (Z.z().State == 1)
	    for (e = agfstout(g, v); e!=null; e = agnxtout(g, e))
		map_edge(e);
    }
    translate_bb(g, GD_rankdir(g));
} finally {
LEAVING("h4i5qxnd7hlrew919abswd13","translate_drawing");
}
}






/* centerPt:
 * Calculate the center point of the xlabel. The returned positions for
 * xlabels always correspond to the lower left corner. 
 */
//3 2i713kmewjct2igf3lwm80462
// static pointf centerPt (xlabel_t* xlp) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="centerPt", key="2i713kmewjct2igf3lwm80462", definition="static pointf centerPt (xlabel_t* xlp)")
public static ST_pointf centerPt(CArray<ST_xlabel_t> xlp) {
ENTERING("2i713kmewjct2igf3lwm80462","centerPt");
try {
   final ST_pointf p = new ST_pointf();
   p.___(xlp.get__(0).pos);
   p.x += (xlp.get__(0).sz.x)/2.0;
   p.y += (xlp.get__(0).sz.y)/2.0;
   return p;
} finally {
LEAVING("2i713kmewjct2igf3lwm80462","centerPt");
}
}



//3 95pnpuiq4khinrz2bqkci9nfg
// static pointf edgeTailpoint (Agedge_t* e) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="edgeTailpoint", key="95pnpuiq4khinrz2bqkci9nfg", definition="static pointf edgeTailpoint (Agedge_t* e)")
public static ST_pointf edgeTailpoint(ST_Agedge_s e) {
ENTERING("95pnpuiq4khinrz2bqkci9nfg","edgeTailpoint");
try {
	return edgeTailpoint_(e).copy();
} finally {
LEAVING("95pnpuiq4khinrz2bqkci9nfg","edgeTailpoint");
}
}
private static ST_pointf edgeTailpoint_(ST_Agedge_s e) {
	ST_splines spl;
	ST_bezier bez;
	spl = getsplinepoints(e);
	if (spl == null) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("ezy0ey6dn5uqp6peuorn615x6"); // 	p.x = p.y = 0;
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
    }
	bez = spl.list.get__(0);
    //     bez = &spl->list[0];
if (bez.sflag!=0) {
	return bez.sp;
} else {
	return bez.list.get__(0);
   // 	return bez->list[0];
 }

}




//3 av67wf2xi70ncgl90j1ttrjjs
// static pointf edgeHeadpoint (Agedge_t* e) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="edgeHeadpoint", key="av67wf2xi70ncgl90j1ttrjjs", definition="static pointf edgeHeadpoint (Agedge_t* e)")
public static ST_pointf edgeHeadpoint(ST_Agedge_s e) {
ENTERING("av67wf2xi70ncgl90j1ttrjjs","edgeHeadpoint");
try {
	return edgeHeadpoint_(e).copy();
} finally {
LEAVING("av67wf2xi70ncgl90j1ttrjjs","edgeHeadpoint");
}
}
private static ST_pointf edgeHeadpoint_(ST_Agedge_s e) {
	ST_splines spl;
	ST_bezier bez;
	spl = getsplinepoints(e);
if (spl == null) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("ezy0ey6dn5uqp6peuorn615x6"); // 	p.x = p.y = 0;
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
}
bez = spl.list.get__(spl.size - 1);
//     bez = &spl->list[spl->size - 1];
if (bez.eflag!=0) {
	return bez.ep;
} else {
	return bez.list.get__(bez.size - 1);
// 	return bez->list[bez->size - 1];
}
}




//3 1ca6fh8ns5bgzfzcz8al4eh4k
// static boxf adjustBB (object_t* objp, boxf bb) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="adjustBB", key="1ca6fh8ns5bgzfzcz8al4eh4k", definition="static boxf adjustBB (object_t* objp, boxf bb)")
public static ST_boxf adjustBB(CArray<ST_object_t> objp, ST_boxf bb) {
ENTERING("1ca6fh8ns5bgzfzcz8al4eh4k","adjustBB");
try {
	return adjustBB_(objp, bb.copy()).copy();
} finally {
LEAVING("1ca6fh8ns5bgzfzcz8al4eh4k","adjustBB");
}
}
private static ST_boxf adjustBB_(CArray<ST_object_t> objp, ST_boxf bb) {
	final ST_pointf ur = new ST_pointf();
	/* Adjust bounding box */
	bb.LL.x = MIN(bb.LL.x, objp.get__(0).pos.x);
	bb.LL.y = MIN(bb.LL.y, objp.get__(0).pos.y);
	ur.x = objp.get__(0).pos.x + objp.get__(0).sz.x;
	ur.y = objp.get__(0).pos.y + objp.get__(0).sz.y;
	bb.UR.x = MAX(bb.UR.x, ur.x);
	bb.UR.y = MAX(bb.UR.y, ur.y);
	return bb;
}




//3 3mefe722uemyoa0czmkkw6hjb
// static void addXLabel (textlabel_t* lp, object_t* objp, xlabel_t* xlp, int initObj, pointf pos) 
/* addXLabel:
 * Set up xlabel_t object and connect with related object.
 * If initObj is set, initialize the object.
 */
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="addXLabel", key="3mefe722uemyoa0czmkkw6hjb", definition="static void addXLabel (textlabel_t* lp, object_t* objp, xlabel_t* xlp, int initObj, pointf pos)")
public static void addXLabel(ST_textlabel_t lp, CArray<ST_object_t> objp, CArray<ST_xlabel_t> xlp, int initObj, ST_pointf pos) {
ENTERING("3mefe722uemyoa0czmkkw6hjb","addXLabel");
try {
	addXLabel_(lp, objp, xlp, initObj, pos.copy());
} finally {
LEAVING("3mefe722uemyoa0czmkkw6hjb","addXLabel");
}
}
private static void addXLabel_(ST_textlabel_t lp, CArray<ST_object_t> objp, CArray<ST_xlabel_t> xlp, int initObj, ST_pointf pos) {
	if (initObj!=0) {
		objp.get__(0).sz.x = 0;
		objp.get__(0).sz.y = 0;
		objp.get__(0).pos.___(pos);
	}
	
	if (Z.z().Flip) {
	UNSUPPORTED("99tzt7erbvtfsbo0jbdz0lc8m"); // 	xlp->sz.x = lp->dimen.y;
	UNSUPPORTED("6v5t3ysaisj27bwc0r9zg3rpd"); // 	xlp->sz.y = lp->dimen.x;
	}
	else {
	xlp.get__(0).sz.___(lp.dimen);
	}
	xlp.get__(0).lbl = lp;
	xlp.get__(0).set = 0;
	objp.get__(0).lbl = xlp;
}




/* addLabelObj:
 * Set up obstacle object based on set external label.
 * This includes dot edge labels.
 * Use label information to determine size and position of object.
 * Then adjust given bounding box bb to include label and return new bb.
 */
//3 dwxd5kvlanbcxqfuncjg0ea54
// static boxf addLabelObj (textlabel_t* lp, object_t* objp, boxf bb) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="addLabelObj", key="dwxd5kvlanbcxqfuncjg0ea54", definition="static boxf addLabelObj (textlabel_t* lp, object_t* objp, boxf bb)")
public static ST_boxf addLabelObj(ST_textlabel_t lp, CArray<ST_object_t> objp, final ST_boxf bb) {
	// WARNING!! STRUCT
	return addLabelObj_(lp, objp, bb.copy()).copy();
}
private static ST_boxf addLabelObj_(ST_textlabel_t lp, CArray<ST_object_t> objp, final ST_boxf bb) {
ENTERING("dwxd5kvlanbcxqfuncjg0ea54","addLabelObj");
try {
	if (Z.z().Flip) {
	UNSUPPORTED("6z2yrwq81gtsk3q9c5pofow1x"); // 	objp->sz.x = lp->dimen.y; 
	UNSUPPORTED("8xsm9kavrekjrsydqe1wh1pu"); // 	objp->sz.y = lp->dimen.x;
	}
	else {
	objp.get__(0).sz.x = lp.dimen.x; 
	objp.get__(0).sz.y = lp.dimen.y;
	}
	objp.get__(0).pos.___(lp.pos);
	objp.get__(0).pos.x -= (objp.get__(0).sz.x) / 2.0;
	objp.get__(0).pos.y -= (objp.get__(0).sz.y) / 2.0;

	return adjustBB(objp, bb);
} finally {
	LEAVING("dwxd5kvlanbcxqfuncjg0ea54","addLabelObj");
}
}




//3 b8tjygxnwny5qoiir1mha1d62
// static boxf addNodeObj (node_t* np, object_t* objp, boxf bb) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="addNodeObj", key="b8tjygxnwny5qoiir1mha1d62", definition="static boxf addNodeObj (node_t* np, object_t* objp, boxf bb)")
public static ST_boxf addNodeObj(ST_Agnode_s np, CArray<ST_object_t> objp, final ST_boxf bb) {
	// WARNING!! STRUCT
	return addNodeObj_(np, objp, bb.copy()).copy();
}
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="", key="", definition="")
public static ST_boxf addNodeObj_(ST_Agnode_s np, CArray<ST_object_t> objp, final ST_boxf bb) {
ENTERING("b8tjygxnwny5qoiir1mha1d62","addNodeObj");
try {
	if (Z.z().Flip) {
	UNSUPPORTED("1ri5uimcd1z58iix8tp528l1m"); // 	objp->sz.x = ((ND_height(np))*(double)72);
	UNSUPPORTED("6r5gwwhz3sjxrssh8yo3v5c3v"); // 	objp->sz.y = ((ND_width(np))*(double)72);
	}
	else {
		  objp.get__(0).sz.x = ((ND_width(np))*(double)72);
		  objp.get__(0).sz.y = ((ND_height(np))*(double)72);
	}
	objp.get__(0).pos.___(ND_coord(np));
	objp.get__(0).pos.x -= (objp.get__(0).sz.x) / 2.0;
	objp.get__(0).pos.y -= (objp.get__(0).sz.y) / 2.0;

	return adjustBB(objp, bb);
} finally {
	LEAVING("b8tjygxnwny5qoiir1mha1d62","addNodeObj");
}
}




//3 6kx3lin2ig9o2otk2bqzdvd4t
// static cinfo_t addClusterObj (Agraph_t* g, cinfo_t info) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="addClusterObj", key="6kx3lin2ig9o2otk2bqzdvd4t", definition="static cinfo_t addClusterObj (Agraph_t* g, cinfo_t info)")
public static ST_cinfo_t addClusterObj(ST_Agraph_s g, ST_cinfo_t info) {
ENTERING("6kx3lin2ig9o2otk2bqzdvd4t","addClusterObj");
try {
	return addClusterObj_(g, info.copy()).copy();
} finally {
LEAVING("6kx3lin2ig9o2otk2bqzdvd4t","addClusterObj");
}
}
private static ST_cinfo_t addClusterObj_(ST_Agraph_s g, ST_cinfo_t info) {
     int c;
     for (c = 1; c <= GD_n_cluster(g); c++)
 	info.___(addClusterObj (GD_clust(g).get_(c), info));
     if (NEQ(g, agroot(g)) && (GD_label(g)!=null) && GD_label(g).set!=0) {
    	 CArray<ST_object_t> objp = info.objp;
    	 info.bb.___(addLabelObj (GD_label(g), objp, info.bb));
    	 info.objp = info.objp.plus_(1);
//UNSUPPORTED("dcgq2zlh4t0m1gno12t6h7ouy"); // 	object_t* objp = info.objp;
//UNSUPPORTED("ddz79zm5235krd6smukq1gza0"); // 	info.bb = addLabelObj (GD_label(g), objp, info.bb);
//UNSUPPORTED("be25qc3x3muxo4l87ji01t3kd"); // 	info.objp++;
     }
     return info;
}




@Reviewed(when = "16/11/2020")
@Original(version="2.38.0", path="lib/common/postproc.c", name="countClusterLabels", key="2tdbzvdtkwxp75kj0iufsynm5", definition="static int countClusterLabels (Agraph_t* g)")
public static int countClusterLabels(ST_Agraph_s g) {
ENTERING("2tdbzvdtkwxp75kj0iufsynm5","countClusterLabels");
try {
	int c, i = 0;
	if (NEQ(g, agroot(g)) && GD_label(g)!=null && GD_label(g).set!=0)
	i++;
	for (c = 1; c <= GD_n_cluster(g); c++)
	i += countClusterLabels(GD_clust(g).get_(c));
	return i;
} finally {
LEAVING("2tdbzvdtkwxp75kj0iufsynm5","countClusterLabels");
}

}



private static boolean HAVE_EDGE(ST_Agedge_s ep, int et) {
	return (et != ET_NONE) && (ED_spl(ep) != null);
}

//3 d4215jd9wukfn6t0iknwzjcof
// static void addXLabels(Agraph_t * gp) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="addXLabels", key="d4215jd9wukfn6t0iknwzjcof", definition="static void addXLabels(Agraph_t * gp)")
public static void addXLabels(ST_Agraph_s gp) {
ENTERING("d4215jd9wukfn6t0iknwzjcof","addXLabels");
try {
    ST_Agnode_s np;
    ST_Agedge_s ep;
    int cnt, i, n_objs, n_lbls;
    int n_nlbls = 0;		/* # of unset node xlabels */
    int n_elbls = 0;		/* # of unset edge labels or xlabels */
    int n_set_lbls = 0;		/* # of set xlabels and edge labels */
    int n_clbls = 0;		/* # of set cluster labels */
    final ST_boxf bb = new ST_boxf();
    final ST_pointf ur = new ST_pointf();
    ST_textlabel_t lp;
    final ST_label_params_t params = new ST_label_params_t();
    CArray<ST_object_t> objs;
    CArray<ST_xlabel_t> lbls;
    CArray<ST_object_t> objp;
    CArray<ST_xlabel_t> xlp;
    ST_Agsym_s force;
    int et = (GD_flags(gp) & (7 << 1));
    
    if (N(GD_has_labels(gp) & NODE_XLABEL) &&
	N(GD_has_labels(gp) & EDGE_XLABEL) &&
	N(GD_has_labels(gp) & TAIL_LABEL) &&
	N(GD_has_labels(gp) & HEAD_LABEL) &&
	(N(GD_has_labels(gp) & EDGE_LABEL) || Z.z().EdgeLabelsDone!=0))
	return;
    
	for (np = agfstnode(gp); np!=null; np = agnxtnode(gp, np)) {
	if (ND_xlabel(np)!=null) {
UNSUPPORTED("6oje33bnpp4jv5mclsrrhl005"); // 	    if (ND_xlabel(np)->set)
UNSUPPORTED("cfkrw6t4lrs7dfgx86sgrz26"); // 		n_set_lbls++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("26eewzzknvqt2nbcrqds5fmti"); // 		n_nlbls++;
	}
	for (ep = agfstout(gp, np); ep!=null; ep = agnxtout(gp, ep)) {
		if (ED_xlabel(ep)!=null) {
UNSUPPORTED("appkettxihy2o612jk6fahbnh"); // 		if (ED_xlabel(ep)->set)
UNSUPPORTED("8k2rclvg6eaoph9r2pz4620xq"); // 		    n_set_lbls++;
UNSUPPORTED("14y6caappoxe17mogr979qf75"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL)))
UNSUPPORTED("q3t8uxncrxc4n8rtuabtzxya"); // 		    n_elbls++;
		}
		if (ED_head_label(ep)!=null) {
			if (ED_head_label(ep).set!=0)
				n_set_lbls++;
			else if (HAVE_EDGE(ep, et))
				n_elbls++;
		}
		if (ED_tail_label(ep)!=null) {
			if (ED_tail_label(ep).set!=0)
				n_set_lbls++;
			else if (HAVE_EDGE(ep, et))
				n_elbls++;
		}
		if (ED_label(ep)!=null) {
			if (ED_label(ep).set!=0)
				n_set_lbls++;
			else if (HAVE_EDGE(ep, et))
				n_elbls++;
		}
		}
	}
	if ((GD_has_labels(gp) & GRAPH_LABEL)!=0)
		n_clbls = countClusterLabels (gp);
	/* A label for each unpositioned external label */
	n_lbls = n_nlbls + n_elbls;
	if (n_lbls == 0) return;
	/* An object for each node, each positioned external label, any cluster label, 
	 * and all unset edge labels and xlabels.
	 */
	n_objs = agnnodes(gp) + n_set_lbls + n_clbls + n_elbls;
	objs = CArray.<ST_object_t>ALLOC__(n_objs, ST_object_t.class);
	objp = objs;
	lbls = CArray.<ST_xlabel_t>ALLOC__(n_lbls, ST_xlabel_t.class);
	xlp = lbls;
	bb.LL.___(pointfof(INT_MAX, INT_MAX));
	bb.UR.___(pointfof(-INT_MAX, -INT_MAX));
	for (np = agfstnode(gp); np!=null; np = agnxtnode(gp, np)) {
		bb.___(addNodeObj (np, objp, bb));
		lp = ND_xlabel(np);
		if (lp != null) {
UNSUPPORTED("d5pjy3dwui27jfdz550cy0cln"); // 	    if (lp->set) {
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
UNSUPPORTED("3zy3jhlqyioeyh9mlrspjjgc6"); // 		bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2msn58w2dse7pbq2esv7awk4r"); // 		addXLabel (lp, objp, xlp, 0, ur); 
UNSUPPORTED("1zpq9rd3nn9kjrmun8ivs9zx5"); // 		xlp++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
    }
	objp = objp.plus_(1);
    for (ep = agfstout(gp, np); ep!=null; ep = agnxtout(gp, ep)) {
    	lp = ED_label(ep);
    	if (lp != null) {
    		if (lp.set!=0) {
    			bb.___(addLabelObj (lp, objp, bb));
    	    }
    		else if (HAVE_EDGE(ep, et)) {
 		    addXLabel (lp, objp, xlp, 1, edgeMidpoint(gp, ep)); 
 		    xlp = xlp.plus_(1);
}
else {
UNSUPPORTED("3ia66n3hqrwmh3hybkoh6f8wa"); // 		    agerr(AGWARN, "no position for edge with label %s",
UNSUPPORTED("9npeksy1st7v005znerttzzzv"); // 			    ED_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
}
objp = objp.plus_(1);
    	}
    	lp = ED_tail_label(ep);
    	if (lp != null) {
if (lp.set!=0) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
}
 		else if (HAVE_EDGE(ep, et)) {
 		    addXLabel (lp, objp, xlp, 1, edgeTailpoint(ep)); 
 		    xlp = xlp.plus_(1);
 		}
 		else {
UNSUPPORTED("5ixexxcbcix5hrfl43td7pj4s"); // 		    agerr(AGWARN, "no position for edge with tail label %s",
UNSUPPORTED("cf9qaysecgkvv4165la4uu6cb"); // 			    ED_tail_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
 		}
			objp = objp.plus_(1);
    	}
    	lp = ED_head_label(ep);
    	if (lp != null) {
if (lp.set!=0) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
}
else if (HAVE_EDGE(ep, et)) {
addXLabel (lp, objp, xlp, 1, edgeHeadpoint(ep));
xlp = xlp.plus_(1);
}
else {
UNSUPPORTED("8nrkavpg9ifts9yylhfijn9rp"); // 		    agerr(AGWARN, "no position for edge with head label %s",
UNSUPPORTED("a5omwtwd411hsfrc37d8t6m8b"); // 			    ED_head_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
}
		objp = objp.plus_(1);
    }
    lp = ED_xlabel(ep);
    if (lp != null) {
UNSUPPORTED("5dapykbxjvnhw0dpi7jfpcazk"); // 		if (lp->set) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dfnmpe0hri6ksye0gnxssi4zz"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL))) {
UNSUPPORTED("9ffmrymv8cg4h4b3ea97t9qbp"); // 		    addXLabel (lp, objp, xlp, 1, edgeMidpoint(gp, ep)); 
UNSUPPORTED("808184nt3k6cxj5dsg27yvpvg"); // 		    xlp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("dbskad3xgu5oqqhwl1cr9f88g"); // 		    agerr(AGWARN, "no position for edge with xlabel %s",
UNSUPPORTED("dtpynjioyrbt2xfca2o46eb0j"); // 			    ED_xlabel(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
    	}
    }
}
if (n_clbls!=0) {
    final ST_cinfo_t info = new ST_cinfo_t();
    info.bb.___(bb);
    info.objp = objp;
    info.___(addClusterObj (gp, info));
    bb.___(info.bb);
}
force = (agattr(gp,AGRAPH,new CString("forcelabels"),null));
params.force = late_bool(gp, force, 1);
params.bb.___(bb);
placeLabels(objs, n_objs, lbls, n_lbls, params);
//     if (Verbose)
// 	printData(objs, n_objs, lbls, n_lbls, &params);
     xlp = lbls;
     cnt = 0;
     for (i = 0; i < n_lbls; i++) {
 	if (xlp.get__(0).set!=0) {
 	    cnt++;
 	    lp = xlp.get__(0).lbl;
 	    lp.set = 1;
 	    lp.pos.___(centerPt(xlp));
 	    updateBB (gp, lp);
 	}
 	xlp = xlp.plus_(1);
     }
//     if (Verbose)
// 	fprintf (stderr, "%d out of %d labels positioned.\n", cnt, n_lbls);
if (cnt != n_lbls)
UNSUPPORTED("9hqu9h8q1a2xl4ty48ct0fdyp"); // 	agerr(AGWARN, "%d out of %d exterior labels positioned.\n", cnt, n_lbls);
	 Memory.free(objs);
     Memory.free(lbls);
} finally {
LEAVING("d4215jd9wukfn6t0iknwzjcof","addXLabels");
}
}






//3 8fc0zxg8y7hec3n4evx3jw6cq
// void gv_postprocess(Agraph_t * g, int allowTranslation) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="gv_postprocess", key="8fc0zxg8y7hec3n4evx3jw6cq", definition="void gv_postprocess(Agraph_t * g, int allowTranslation)")
public static void gv_postprocess(ST_Agraph_s g, int allowTranslation) {
ENTERING("8fc0zxg8y7hec3n4evx3jw6cq","gv_postprocess");
try {
    double diff;
    final ST_pointf dimen = new ST_pointf();
    Z.z().Rankdir = GD_rankdir(g);
    Z.z().Flip = GD_flip(g);
    /* Handle cluster labels */
    if (Z.z().Flip)
UNSUPPORTED("4hxky2sp978rmy6018sfmts6m"); // 	place_flip_graph_label(g);
    else
	place_graph_label(g);
    /* Everything has been placed except the root graph label, if any.
     * The graph positions have not yet been rotated back if necessary.
     */
    addXLabels(g);
    /* Add space for graph label if necessary */
    if (GD_label(g)!=null && N(GD_label(g).set)) {
UNSUPPORTED("crj0py2wme4b5l8apvbxqcmqa"); // 	dimen = GD_label(g)->dimen;
UNSUPPORTED("22jhn709g4c5wh0gb6v40rh19"); // 	{((dimen).x += 4*4); ((dimen).y += 2*4);};
UNSUPPORTED("9k69y89jybam5elefg45va3ey"); // 	if (Flip) {
UNSUPPORTED("andsvpqa42ef9h5dkn3uyv6tj"); // 	    if (GD_label_pos(g) & 1) {
UNSUPPORTED("65ggem18g4zgz2yx552vi2n4v"); // 		GD_bb(g).UR.x += dimen.y;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("9dm14vohn1tuwqrwprpdywylr"); // 		GD_bb(g).LL.x -= dimen.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("94nnj9ien92542qanqtyo8qzq"); // 	    if (dimen.x > (GD_bb(g).UR.y - GD_bb(g).LL.y)) {
UNSUPPORTED("awekuk9gokwsbb49j41hvhqt4"); // 		diff = dimen.x - (GD_bb(g).UR.y - GD_bb(g).LL.y);
UNSUPPORTED("5856jxlve8fb2pennnazjjkij"); // 		diff = diff / 2.;
UNSUPPORTED("3t8m6fustsc50cpggxiadcjax"); // 		GD_bb(g).LL.y -= diff;
UNSUPPORTED("7c25kl7mn9jd5x5x2uflcql86"); // 		GD_bb(g).UR.y += diff;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("andsvpqa42ef9h5dkn3uyv6tj"); // 	    if (GD_label_pos(g) & 1) {
UNSUPPORTED("7x8evhhttjy9mwgcpfpb3l7lm"); // 		if (Rankdir == 0)
UNSUPPORTED("45a42yl5qj83sj5mzdd6k6wcj"); // 		    GD_bb(g).UR.y += dimen.y;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("6i55hrio04eg5ilg5i01jw8vv"); // 		    GD_bb(g).LL.y -= dimen.y;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("7x8evhhttjy9mwgcpfpb3l7lm"); // 		if (Rankdir == 0)
UNSUPPORTED("6i55hrio04eg5ilg5i01jw8vv"); // 		    GD_bb(g).LL.y -= dimen.y;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("45a42yl5qj83sj5mzdd6k6wcj"); // 		    GD_bb(g).UR.y += dimen.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("p1mrpzvl0adzwcw3lialn8v3"); // 	    if (dimen.x > (GD_bb(g).UR.x - GD_bb(g).LL.x)) {
UNSUPPORTED("3ie0x59qavcqpnvy7kci31lgc"); // 		diff = dimen.x - (GD_bb(g).UR.x - GD_bb(g).LL.x);
UNSUPPORTED("5856jxlve8fb2pennnazjjkij"); // 		diff = diff / 2.;
UNSUPPORTED("anqdsrkl2qs1pqbuivrdz6fnt"); // 		GD_bb(g).LL.x -= diff;
UNSUPPORTED("c0ah0pvnkczqdg5jt0u955wns"); // 		GD_bb(g).UR.x += diff;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    if (allowTranslation!=0) {
	switch (Z.z().Rankdir) {
	case 0:
	    Z.z().Offset.___(GD_bb(g).LL);
	    break;
	case 1:
UNSUPPORTED("5t3j9lrc86kd5ouaqgzvz3qcm"); // 	    Offset = pointfof(-GD_bb(g).UR.y, GD_bb(g).LL.x);
	    break;
	case 2:
UNSUPPORTED("96ajwnh79ja9g57xmut6dmh9d"); // 	    Offset = pointfof(GD_bb(g).LL.x, -GD_bb(g).UR.y);
	    break;
	case 3:
UNSUPPORTED("3xtu7zkpqq7nsx9oe68oi6ebt"); // 	    Offset = pointfof(GD_bb(g).LL.y, GD_bb(g).LL.x);
	    break;
	}
	translate_drawing(g);
    }
    if (GD_label(g)!=null && N(GD_label(g).set))
UNSUPPORTED("6dds0zsvqw48u510zcy954fh1"); // 	place_root_label(g, dimen);
    if (Z.z().Show_boxes!=null) {
UNSUPPORTED("8c7x8di5w36ib05qan9z4sl9"); // 	char buf[BUFSIZ];
UNSUPPORTED("83qqprhiseoxlwtwi991aag0c"); // 	if (Flip)
UNSUPPORTED("86tova7pv19alt02nlk0d17oj"); // 	    sprintf(buf, "/pathbox {\n    /X exch neg %.5g sub def\n    /Y exch %.5g sub def\n    /x exch neg %.5g sub def\n    /y exch %.5g sub def\n    newpath x y moveto\n    X y lineto\n    X Y lineto\n    x Y lineto\n    closepath stroke\n} def\n", Offset.x, Offset.y, Offset.x, Offset.y);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("79tu9xkxv4v48uko4cxz7v04t"); // 	    sprintf(buf, "/pathbox {\n    /Y exch %.5g sub def\n    /X exch %.5g sub def\n    /y exch %.5g sub def\n    /x exch %.5g sub def\n    newpath x y moveto\n    X y lineto\n    X Y lineto\n    x Y lineto\n    closepath stroke\n } def\n/dbgstart { gsave %.5g %.5g translate } def\n/arrowlength 10 def\n/arrowwidth arrowlength 2 div def\n/arrowhead {\n    gsave\n    rotate\n    currentpoint\n    newpath\n    moveto\n    arrowlength arrowwidth 2 div rlineto\n    0 arrowwidth neg rlineto\n    closepath fill\n    grestore\n} bind def\n/makearrow {\n    currentpoint exch pop sub exch currentpoint pop sub atan\n    arrowhead\n} bind def\n/point {    newpath    2 0 360 arc fill} def/makevec {\n    /Y exch def\n    /X exch def\n    /y exch def\n    /x exch def\n    newpath x y moveto\n    X Y lineto stroke\n    X Y moveto\n    x y makearrow\n} def\n", Offset.y, Offset.x, Offset.y, Offset.x,
UNSUPPORTED("aow79vde4xjqtwexymr5ocjl6"); // 		    -Offset.x, -Offset.y);
UNSUPPORTED("6g3g36v7l0tyfootyy8mzv3t8"); // 	Show_boxes[0] = strdup(buf);
    }
} finally {
LEAVING("8fc0zxg8y7hec3n4evx3jw6cq","gv_postprocess");
}
}




//3 3qbbvlnq1b06ylgr0yj2slbhm
// void dotneato_postprocess(Agraph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="dotneato_postprocess", key="3qbbvlnq1b06ylgr0yj2slbhm", definition="void dotneato_postprocess(Agraph_t * g)")
public static void dotneato_postprocess(ST_Agraph_s g) {
ENTERING("3qbbvlnq1b06ylgr0yj2slbhm","dotneato_postprocess");
try {
    gv_postprocess(g, 1);
} finally {
LEAVING("3qbbvlnq1b06ylgr0yj2slbhm","dotneato_postprocess");
}
}







//3 72zw1alhd5vd0g6mhum507rvx
// void place_graph_label(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/common/postproc.c", name="place_graph_label", key="72zw1alhd5vd0g6mhum507rvx", definition="void place_graph_label(graph_t * g)")
public static void place_graph_label(ST_Agraph_s g) {
ENTERING("72zw1alhd5vd0g6mhum507rvx","place_graph_label");
try {
    int c;
    final ST_pointf p = new ST_pointf(), d = new ST_pointf();
    if (NEQ(g, agroot(g)) && (GD_label(g)!=null) && N(GD_label(g).set)) {
	if ((GD_label_pos(g) & 1)!=0) {
	    d.___(GD_border(g)[2]);
	    p.y = GD_bb(g).UR.y - d.y / 2;
	} else {
UNSUPPORTED("1w38no4welthbwa0i10hei16b"); // 	    d = GD_border(g)[0];
UNSUPPORTED("2xa4n9ca16xpf1kahaycmkl4r"); // 	    p.y = GD_bb(g).LL.y + d.y / 2;
	}
	if ((GD_label_pos(g) & 4)!=0) {
UNSUPPORTED("cgv3bcg9c274cdwxi1y0sja3p"); // 	    p.x = GD_bb(g).UR.x - d.x / 2;
	} else if ((GD_label_pos(g) & 2)!=0) {
UNSUPPORTED("7ictv9eqmjvxjii5lqlyw8nu"); // 	    p.x = GD_bb(g).LL.x + d.x / 2;
	} else {
	    p.x = ((GD_bb(g).LL.x + GD_bb(g).UR.x) / 2);
	}
	GD_label(g).pos.___(p);
	GD_label(g).set= NOTI(false);
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	place_graph_label((ST_Agraph_s) GD_clust(g).get_(c));
} finally {
LEAVING("72zw1alhd5vd0g6mhum507rvx","place_graph_label");
}
}


}
