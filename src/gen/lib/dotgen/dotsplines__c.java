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
 * (C) Copyright 2009-2020, Arnaud Roques
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
import static gen.lib.common.memory__c.zmalloc;
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
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.function;
import static smetana.core.JUtils.qsort;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ABS;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.ALLOC_allocated_ST_Agedge_s;
import static smetana.core.Macro.BETWEEN;
import static smetana.core.Macro.ED_adjacent;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_tree_index;
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
import static smetana.core.Macro.MAKEFWDEDGE;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.M_PI;
import static smetana.core.Macro.N;
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
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.NOTI;
import static smetana.core.Macro.ROUND;
import static smetana.core.Macro.UNSUPPORTED;
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
import h.attr_state_t;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class dotsplines__c {
//1 2digov3edok6d5srhgtlmrycs
// extern lt_symlist_t lt_preloaded_symbols[]


//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 9k44uhd5foylaeoekf3llonjq
// extern Dtmethod_t* 	Dtset


//1 1ahfywsmzcpcig2oxm7pt9ihj
// extern Dtmethod_t* 	Dtbag


//1 anhghfj3k7dmkudy2n7rvt31v
// extern Dtmethod_t* 	Dtoset


//1 5l6oj1ux946zjwvir94ykejbc
// extern Dtmethod_t* 	Dtobag


//1 2wtf222ak6cui8cfjnw6w377z
// extern Dtmethod_t*	Dtlist


//1 d1s1s6ibtcsmst88e3057u9r7
// extern Dtmethod_t*	Dtstack


//1 axa7mflo824p6fspjn1rdk0mt
// extern Dtmethod_t*	Dtqueue


//1 ega812utobm4xx9oa9w9ayij6
// extern Dtmethod_t*	Dtdeque


//1 cyfr996ur43045jv1tjbelzmj
// extern Dtmethod_t*	Dtorder


//1 wlofoiftbjgrrabzb2brkycg
// extern Dtmethod_t*	Dttree


//1 12bds94t7voj7ulwpcvgf6agr
// extern Dtmethod_t*	Dthash


//1 9lqknzty480cy7zsubmabkk8h
// extern Dtmethod_t	_Dttree


//1 bvn6zkbcp8vjdhkccqo1xrkrb
// extern Dtmethod_t	_Dthash


//1 9lidhtd6nsmmv3e7vjv9e10gw
// extern Dtmethod_t	_Dtlist


//1 34ujfamjxo7xn89u90oh2k6f8
// extern Dtmethod_t	_Dtqueue


//1 3jy4aceckzkdv950h89p4wjc8
// extern Dtmethod_t	_Dtstack


//1 8dfqgf3u1v830qzcjqh9o8ha7
// extern Agmemdisc_t AgMemDisc


//1 18k2oh2t6llfsdc5x0wlcnby8
// extern Agiddisc_t AgIdDisc


//1 a4r7hi80gdxtsv4hdoqpyiivn
// extern Agiodisc_t AgIoDisc


//1 bnzt5syjb7mgeru19114vd6xx
// extern Agdisc_t AgDefaultDisc


//1 35y2gbegsdjilegaribes00mg
// extern Agdesc_t Agdirected, Agstrictdirected, Agundirected,     Agstrictundirected


//1 c2rygslq6bcuka3awmvy2b3ow
// typedef Agsubnode_t	Agnoderef_t


//1 xam6yv0dcsx57dtg44igpbzn
// typedef Dtlink_t	Agedgeref_t


//1 nye6dsi1twkbddwo9iffca1j
// extern char *Version


//1 65mu6k7h7lb7bx14jpiw7iyxr
// extern char **Files


//1 2rpjdzsdyrvomf00zcs3u3dyn
// extern const char **Lib


//1 6d2f111lntd2rsdt4gswh5909
// extern char *CmdName


//1 a0ltq04fpeg83soa05a2fkwb2
// extern char *specificFlags


//1 1uv30qeqq2jh6uznlr4dziv0y
// extern char *specificItems


//1 7i4hkvngxe3x7lmg5h6b3t9g3
// extern char *Gvfilepath


//1 9jp96pa73kseya3w6sulxzok6
// extern char *Gvimagepath


//1 40ylumfu7mrvawwf4v2asvtwk
// extern unsigned char Verbose


//1 93st8awjy1z0h07n28qycbaka
// extern unsigned char Reduce


//1 f2vs67ts992erf8onwfglurzp
// extern int MemTest


//1 c6f8whijgjwwagjigmxlwz3gb
// extern char *HTTPServerEnVar


//1 cp4hzj7p87m7arw776d3bt7aj
// extern char *Output_file_name


//1 a3rqagofsgraie6mx0krzkgsy
// extern int graphviz_errors


//1 5up05203r4kxvjn1m4njcgq5x
// extern int Nop


//1 umig46cco431x14b3kosde2t
// extern double PSinputscale


//1 52bj6v8fqz39khasobljfukk9
// extern int Syntax_errors


//1 9ekf2ina8fsjj6y6i0an6somj
// extern int Show_cnt


//1 38di5qi3nkxkq65onyvconk3r
// extern char** Show_boxes


//1 6ri6iu712m8mpc7t2670etpcw
// extern int CL_type


//1 bomxiw3gy0cgd1ydqtek7fpxr
// extern unsigned char Concentrate


//1 cqy3gqgcq8empdrbnrhn84058
// extern double Epsilon


//1 64slegfoouqeg0rmbyjrm8wgr
// extern int MaxIter


//1 88wdinpnmfs4mab4aw62yb0bg
// extern int Ndim


//1 8bbad3ogcelqnnvo5br5s05gq
// extern int State


//1 17rnd8q45zclfn68qqst2vxxn
// extern int EdgeLabelsDone


//1 ymx1z4s8cznjifl2d9f9m8jr
// extern double Initial_dist


//1 a33bgl0c3uqb3trx419qulj1x
// extern double Damping


//1 d9lvrpjg1r0ojv40pod1xwk8n
// extern int Y_invert


//1 71efkfs77q5tq9ex6y0f4kanh
// extern int GvExitOnUsage


//1 4xy2dkdkv0acs2ue9eca8hh2e
// extern Agsym_t 	*G_activepencolor, *G_activefillcolor, 	*G_selectedpencolor, *G_selectedfillcolor, 	*G_visitedpencolor, *G_visitedfillcolor, 	*G_deletedpencolor, *G_deletedfillcolor, 	*G_ordering, *G_peripheries, *G_penwidth, 	*G_gradientangle, *G_margin


//1 9js5gxgzr74eakgtfhnbws3t9
// extern Agsym_t 	*N_height, *N_width, *N_shape, *N_color, *N_fillcolor, 	*N_activepencolor, *N_activefillcolor, 	*N_selectedpencolor, *N_selectedfillcolor, 	*N_visitedpencolor, *N_visitedfillcolor, 	*N_deletedpencolor, *N_deletedfillcolor, 	*N_fontsize, *N_fontname, *N_fontcolor, *N_margin, 	*N_label, *N_xlabel, *N_nojustify, *N_style, *N_showboxes, 	*N_sides, *N_peripheries, *N_ordering, *N_orientation, 	*N_skew, *N_distortion, *N_fixed, *N_imagescale, *N_layer, 	*N_group, *N_comment, *N_vertices, *N_z, 	*N_penwidth, *N_gradientangle


//1 anqllp9sj7wo45w6bm11j8trn
// extern Agsym_t 	*E_weight, *E_minlen, *E_color, *E_fillcolor, 	*E_activepencolor, *E_activefillcolor, 	*E_selectedpencolor, *E_selectedfillcolor, 	*E_visitedpencolor, *E_visitedfillcolor, 	*E_deletedpencolor, *E_deletedfillcolor, 	*E_fontsize, *E_fontname, *E_fontcolor, 	*E_label, *E_xlabel, *E_dir, *E_style, *E_decorate, 	*E_showboxes, *E_arrowsz, *E_constr, *E_layer, 	*E_comment, *E_label_float, 	*E_samehead, *E_sametail, 	*E_arrowhead, *E_arrowtail, 	*E_headlabel, *E_taillabel, 	*E_labelfontsize, *E_labelfontname, *E_labelfontcolor, 	*E_labeldistance, *E_labelangle, 	*E_tailclip, *E_headclip, 	*E_penwidth


//1 bh0z9puipqw7gymjd5h5b8s6i
// extern struct fdpParms_s* fdp_parms




//3 ciez0pfggxdljedzsbklq49f0
// static inline point pointof(int x, int y) 
public static Object pointof(Object... arg) {
UNSUPPORTED("8e4tj258yvfq5uhsdpk37n5eq"); // static inline point pointof(int x, int y)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0j3k9xv06332q98k2pgpacto"); //     point r;
UNSUPPORTED("12jimkrzqxavaie0cpapbx18c"); //     r.x = x;
UNSUPPORTED("7ivmviysahgsc5nn9gtp7q2if"); //     r.y = y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c1s4k85p1cdfn176o3uryeros
// static inline pointf pointfof(double x, double y) 
public static ST_pointf pointfof(double x, double y) {
// WARNING!! STRUCT
return pointfof_w_(x, y).copy();
}
private static ST_pointf pointfof_w_(double x, double y) {
ENTERING("c1s4k85p1cdfn176o3uryeros","pointfof");
try {
    final ST_pointf r = new ST_pointf();
    r.setDouble("x", x);
    r.setDouble("y", y);
    return r;
} finally {
LEAVING("c1s4k85p1cdfn176o3uryeros","pointfof");
}
}




//3 7cufnfitrh935ew093mw0i4b7
// static inline box boxof(int llx, int lly, int urx, int ury) 
public static Object boxof(Object... arg) {
UNSUPPORTED("3lzesfdd337h31jrlib1czocm"); // static inline box boxof(int llx, int lly, int urx, int ury)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("52u27kayecy1i1e8bbo8f7s9r"); //     box b;
UNSUPPORTED("cylhjlutoc0sc0uy7g98m9fb8"); //     b.LL.x = llx, b.LL.y = lly;
UNSUPPORTED("242of6revxzx8hpe7yerrchz6"); //     b.UR.x = urx, b.UR.y = ury;
UNSUPPORTED("2vmm1j57brhn455f8f3iyw6mo"); //     return b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1vvsta5i8of59frav6uymguav
// static inline boxf boxfof(double llx, double lly, double urx, double ury) 
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




//3 1n5xl70wxuabyf97mclvilsm6
// static inline point add_point(point p, point q) 
public static Object add_point(Object... arg) {
UNSUPPORTED("6iamka1fx8fk1rohzzse8phte"); // static inline point add_point(point p, point q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0j3k9xv06332q98k2pgpacto"); //     point r;
UNSUPPORTED("3n2sizjd0civbzm6iq7su1s2p"); //     r.x = p.x + q.x;
UNSUPPORTED("65ygdo31w09i5i6bd2f7azcd3"); //     r.y = p.y + q.y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 arrsbik9b5tnfcbzsm8gr2chx
// static inline pointf add_pointf(pointf p, pointf q) 
public static ST_pointf add_pointf(final ST_pointf p, final ST_pointf q) {
// WARNING!! STRUCT
return add_pointf_w_(p.copy(), q.copy()).copy();
}
private static ST_pointf add_pointf_w_(final ST_pointf p, final ST_pointf q) {
ENTERING("arrsbik9b5tnfcbzsm8gr2chx","add_pointf");
try {
    final ST_pointf r = new ST_pointf();
    r.setDouble("x", p.x + q.x);
    r.setDouble("y", p.y + q.y);
    return r;
} finally {
LEAVING("arrsbik9b5tnfcbzsm8gr2chx","add_pointf");
}
}




//3 ai2dprak5y6obdsflguh5qbd7
// static inline point sub_point(point p, point q) 
public static Object sub_point(Object... arg) {
UNSUPPORTED("cd602849h0bce8lu9xegka0ia"); // static inline point sub_point(point p, point q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0j3k9xv06332q98k2pgpacto"); //     point r;
UNSUPPORTED("4q4q9dveah93si8ajfv59gz27"); //     r.x = p.x - q.x;
UNSUPPORTED("9f90ik0o2yqhanzntpy3d2ydy"); //     r.y = p.y - q.y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 16f6pyogcv3j7n2p0n8giqqgh
// static inline pointf sub_pointf(pointf p, pointf q) 
public static Object sub_pointf(Object... arg) {
UNSUPPORTED("dmufj44lddsnj0wjyxsg2fcso"); // static inline pointf sub_pointf(pointf p, pointf q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("4q4q9dveah93si8ajfv59gz27"); //     r.x = p.x - q.x;
UNSUPPORTED("9f90ik0o2yqhanzntpy3d2ydy"); //     r.y = p.y - q.y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9k50jgrhc4f9824vf8ony74rw
// static inline point mid_point(point p, point q) 
public static Object mid_point(Object... arg) {
UNSUPPORTED("evy44tdsmu3erff9dp2x835u2"); // static inline point mid_point(point p, point q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0j3k9xv06332q98k2pgpacto"); //     point r;
UNSUPPORTED("1a6p6fm57o0wt5ze2btsx06c7"); //     r.x = (p.x + q.x) / 2;
UNSUPPORTED("1kbj5tgdmfi6kf4jgg6skhr6e"); //     r.y = (p.y + q.y) / 2;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 59c4f7im0ftyowhnzzq2v9o1x
// static inline pointf mid_pointf(pointf p, pointf q) 
public static Object mid_pointf(Object... arg) {
UNSUPPORTED("381o63o9kb04d7gzg65v0r3q"); // static inline pointf mid_pointf(pointf p, pointf q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("c5vboetlr3mf43wns7iik6m1w"); //     r.x = (p.x + q.x) / 2.;
UNSUPPORTED("bcdf562ldr3bjn78hcay5xd63"); //     r.y = (p.y + q.y) / 2.;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5r18p38gisvcx3zsvbb9saixx
// static inline pointf interpolate_pointf(double t, pointf p, pointf q) 
public static Object interpolate_pointf(Object... arg) {
UNSUPPORTED("894yimn33kmtm454llwdaotu8"); // static inline pointf interpolate_pointf(double t, pointf p, pointf q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ef2acl8wa2ooqcb5vz3098maz"); //     pointf r; 
UNSUPPORTED("5tpwuyf5iidesy80v8o4nwkmk"); //     r.x = p.x + t * (q.x - p.x);
UNSUPPORTED("ewnrc5uloj3w5jbmsjcn3wja0"); //     r.y = p.y + t * (q.y - p.y);
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bxzrv2ghq04qk5cbyy68s4mol
// static inline point exch_xy(point p) 
public static Object exch_xy(Object... arg) {
UNSUPPORTED("2vxya0v2fzlv5e0vjaa8d414"); // static inline point exch_xy(point p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0j3k9xv06332q98k2pgpacto"); //     point r;
UNSUPPORTED("60cojdwc2h7f0m51s9jdwvup7"); //     r.x = p.y;
UNSUPPORTED("evp2x66oa4s1tlnc0ytxq2qbq"); //     r.y = p.x;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9lt3e03tac6h6sydljrcws8fd
// static inline pointf exch_xyf(pointf p) 
public static Object exch_xyf(Object... arg) {
UNSUPPORTED("8qamrobrqi8jsvvfrxkimrsnw"); // static inline pointf exch_xyf(pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("60cojdwc2h7f0m51s9jdwvup7"); //     r.x = p.y;
UNSUPPORTED("evp2x66oa4s1tlnc0ytxq2qbq"); //     r.y = p.x;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8l9qhieokthntzdorlu5zn29b
// static inline box box_bb(box b0, box b1) 
public static Object box_bb(Object... arg) {
UNSUPPORTED("36et5gmnjrby6o7bq9sgh1hx6"); // static inline box box_bb(box b0, box b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("52u27kayecy1i1e8bbo8f7s9r"); //     box b;
UNSUPPORTED("8mr2c9xitsqi8z1plbp7ox1hu"); //     b.LL.x = MIN(b0.LL.x, b1.LL.x);
UNSUPPORTED("2egu55ef4u1i03nwz01k7kcrl"); //     b.LL.y = MIN(b0.LL.y, b1.LL.y);
UNSUPPORTED("9n6ei3odbgefwfxvql9whcpe"); //     b.UR.x = MAX(b0.UR.x, b1.UR.x);
UNSUPPORTED("19ocysbuh4pxyft2bqhyhigr1"); //     b.UR.y = MAX(b0.UR.y, b1.UR.y);
UNSUPPORTED("2vmm1j57brhn455f8f3iyw6mo"); //     return b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 clws9h3bbjm0lw3hexf8nl4c4
// static inline boxf boxf_bb(boxf b0, boxf b1) 
public static Object boxf_bb(Object... arg) {
UNSUPPORTED("dyrqu4ww9osr9c86gqgmifcp6"); // static inline boxf boxf_bb(boxf b0, boxf b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c57pq0f87j6dnbcvygu7v6k84"); //     boxf b;
UNSUPPORTED("8mr2c9xitsqi8z1plbp7ox1hu"); //     b.LL.x = MIN(b0.LL.x, b1.LL.x);
UNSUPPORTED("2egu55ef4u1i03nwz01k7kcrl"); //     b.LL.y = MIN(b0.LL.y, b1.LL.y);
UNSUPPORTED("9n6ei3odbgefwfxvql9whcpe"); //     b.UR.x = MAX(b0.UR.x, b1.UR.x);
UNSUPPORTED("19ocysbuh4pxyft2bqhyhigr1"); //     b.UR.y = MAX(b0.UR.y, b1.UR.y);
UNSUPPORTED("2vmm1j57brhn455f8f3iyw6mo"); //     return b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bit6ycxo1iqd2al92y8gkzlvb
// static inline box box_intersect(box b0, box b1) 
public static Object box_intersect(Object... arg) {
UNSUPPORTED("34gv28cldst09bl71itjgviue"); // static inline box box_intersect(box b0, box b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("52u27kayecy1i1e8bbo8f7s9r"); //     box b;
UNSUPPORTED("9slu7bixuymxttjic76ha2nl2"); //     b.LL.x = MAX(b0.LL.x, b1.LL.x);
UNSUPPORTED("3uv943c2f82yuif249pf5azob"); //     b.LL.y = MAX(b0.LL.y, b1.LL.y);
UNSUPPORTED("74tf5h16bc9zabq3s3dyny543"); //     b.UR.x = MIN(b0.UR.x, b1.UR.x);
UNSUPPORTED("d99gcv3i7xes7y7rqf8ii20ux"); //     b.UR.y = MIN(b0.UR.y, b1.UR.y);
UNSUPPORTED("2vmm1j57brhn455f8f3iyw6mo"); //     return b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8gfybie7k6pgb3o1a6llgpwng
// static inline boxf boxf_intersect(boxf b0, boxf b1) 
public static Object boxf_intersect(Object... arg) {
UNSUPPORTED("ape22b8z6jfg17gvo42hok9eb"); // static inline boxf boxf_intersect(boxf b0, boxf b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c57pq0f87j6dnbcvygu7v6k84"); //     boxf b;
UNSUPPORTED("9slu7bixuymxttjic76ha2nl2"); //     b.LL.x = MAX(b0.LL.x, b1.LL.x);
UNSUPPORTED("3uv943c2f82yuif249pf5azob"); //     b.LL.y = MAX(b0.LL.y, b1.LL.y);
UNSUPPORTED("74tf5h16bc9zabq3s3dyny543"); //     b.UR.x = MIN(b0.UR.x, b1.UR.x);
UNSUPPORTED("d99gcv3i7xes7y7rqf8ii20ux"); //     b.UR.y = MIN(b0.UR.y, b1.UR.y);
UNSUPPORTED("2vmm1j57brhn455f8f3iyw6mo"); //     return b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7z8j2quq65govaaejrz7b4cvb
// static inline int box_overlap(box b0, box b1) 
public static Object box_overlap(Object... arg) {
UNSUPPORTED("1e9k599x7ygct7r4cfdxlk9u9"); // static inline int box_overlap(box b0, box b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7a9wwpu7dhdphd08y1ecw54w5"); //     return OVERLAP(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4z0suuut2acsay5m8mg9dqjdu
// static inline int boxf_overlap(boxf b0, boxf b1) 
public static Object boxf_overlap(Object... arg) {
UNSUPPORTED("905nejsewihwhhc3bhnrz9nwo"); // static inline int boxf_overlap(boxf b0, boxf b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7a9wwpu7dhdphd08y1ecw54w5"); //     return OVERLAP(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dd34swz5rmdgu3a2np2a4h1dy
// static inline int box_contains(box b0, box b1) 
public static Object box_contains(Object... arg) {
UNSUPPORTED("aputfc30fjkvy6jx4otljaczq"); // static inline int box_contains(box b0, box b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("87ap80vrh2a4gpprbxr33lrg3"); //     return CONTAINS(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8laj1bspbu2i1cjd9upr7xt32
// static inline int boxf_contains(boxf b0, boxf b1) 
public static Object boxf_contains(Object... arg) {
UNSUPPORTED("7ccnttkiwt834yfyw0evcm18v"); // static inline int boxf_contains(boxf b0, boxf b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("87ap80vrh2a4gpprbxr33lrg3"); //     return CONTAINS(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4wf5swkz24xx51ja2dynbycu1
// static inline pointf perp (pointf p) 
public static Object perp(Object... arg) {
UNSUPPORTED("567wpqlg9rv63ynyvxd9sgkww"); // static inline pointf perp (pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("2fyydy6t6yifjsczccsb9szeg"); //     r.x = -p.y;
UNSUPPORTED("evp2x66oa4s1tlnc0ytxq2qbq"); //     r.y = p.x;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6dtlpzv4mvgzb9o0b252yweuv
// static inline pointf scale (double c, pointf p) 
public static Object scale(Object... arg) {
UNSUPPORTED("c1ngytew34bmkdb7vps5h3dh8"); // static inline pointf scale (double c, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("dznf7nac14snww4usquyd6r3r"); //     r.x = c * p.x;
UNSUPPORTED("33kk73m8vjcux5tnjl8co2pe6"); //     r.y = c * p.y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 8vvgiozsykdn0hhbvaem6lifn
// static boxf boxes[1000]
//private static __array_of_struct__ boxes = __array_of_struct__.malloc(boxf.class, 1000);



//3 dobhmc46zwtvv8rg3ywntl91j
// static edge_t* getmainedge(edge_t * e) 
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




//3 ddeny5ht7w8b16ztj5zt840ld
// static boolean spline_merge(node_t * n) 
public static boolean spline_merge(ST_Agnode_s n) {
ENTERING("ddeny5ht7w8b16ztj5zt840ld","spline_merge");
try {
    return ((ND_node_type(n) == 1)
	    && ((ND_in(n).size > 1) || (ND_out(n).size > 1)));
} finally {
LEAVING("ddeny5ht7w8b16ztj5zt840ld","spline_merge");
}
}




//3 36ofw2qfqlh5ci8gc8cfkqgg3
// static boolean swap_ends_p(edge_t * e) 
public static boolean swap_ends_p(ST_Agedge_s e) {
ENTERING("36ofw2qfqlh5ci8gc8cfkqgg3","swap_ends_p");
try {
    while (ED_to_orig(e)!=null)
	e = ED_to_orig(e);
    if (ND_rank(aghead(e)) > ND_rank(agtail(e)))
	return false;
    if (ND_rank(aghead(e)) < ND_rank(agtail(e)))
	return NOT(false);
    if (ND_order(aghead(e)) >= ND_order(agtail(e)))
	return false;
    return NOT(false);
} finally {
LEAVING("36ofw2qfqlh5ci8gc8cfkqgg3","swap_ends_p");
}
}


//1 300wnvw9sndobgke752j9u139
// static splineInfo sinfo = 
/*static final __struct__<splineInfo> sinfo = JUtils.from(splineInfo.class);
static {
	sinfo.setPtr("swapEnds", function(dotsplines__c.class, "swap_ends_p"));
	sinfo.setPtr("splineMerge", function(dotsplines__c.class, "spline_merge"));
}*/




//3 3krohso3quojiv4fveh1en7o6
// int portcmp(port p0, port p1) 
public static int portcmp(final ST_port p0, final ST_port p1) {
// WARNING!! STRUCT
return portcmp_w_(p0.copy(), p1.copy());
}
private static int portcmp_w_(final ST_port p0, final ST_port p1) {
ENTERING("3krohso3quojiv4fveh1en7o6","portcmp");
try {
    int rv;
    if (p1.defined == 0)
	return (p0.defined!=0 ? 1 : 0);
    if (p0.defined == 0)
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
public static void swap_bezier(ST_bezier.Array2 old, ST_bezier.Array2 new_) {
ENTERING("10wbtt4gwnxgqutinpj4ymjpk","swap_bezier");
try {
	ST_pointf.Array list;
	ST_pointf.Array lp;
    ST_pointf.Array olp;
    int i, sz;
    sz = old.getStruct().size;
	list = new ST_pointf.Array(sz);
    lp = list;
    olp = old.getStruct().list.plus(sz - 1);
    for (i = 0; i < sz; i++) {	/* reverse list of points */
	lp.getStruct().___(olp.getStruct());
	lp=lp.plus(1);
	olp=olp.plus(-1);
    }
    new_.setPtr("list", list);
    new_.setInt("size", sz);
    new_.setInt("sflag", old.getStruct().eflag);
    new_.setInt("eflag", old.getStruct().sflag);
    new_.setStruct("sp", old.getStruct().ep);
    new_.setStruct("ep", old.getStruct().sp);
} finally {
LEAVING("10wbtt4gwnxgqutinpj4ymjpk","swap_bezier");
}
}




//3 3256l3e2huarsy29dd97vqj85
// static void swap_spline(splines * s) 
public static void swap_spline(ST_splines s) {
ENTERING("3256l3e2huarsy29dd97vqj85","swap_spline");
try {
	ST_bezier.Array2 list;
	ST_bezier.Array2 lp;
	ST_bezier.Array2 olp;
    int i, sz;
    sz = s.size;
	list = new ST_bezier.Array2(sz);
    lp = list;
    olp = s.list.plus(sz - 1);
    for (i = 0; i < sz; i++) {	/* reverse and swap list of beziers */
	swap_bezier(olp, lp);
	olp = olp.plus(-1);
	lp = lp.plus(1);
    }
    /* free old structures */
    for (i = 0; i < sz; i++)
	Memory.free(((ST_bezier)s.list.get(i)).getPtr().list);
    Memory.free(s.list);
    s.list = (ST_bezier.Array2) list;
} finally {
LEAVING("3256l3e2huarsy29dd97vqj85","swap_spline");
}
}




//3 dgkssqjj566ifra0xy7m46qsb
// static void edge_normalize(graph_t * g) 
public static void edge_normalize(ST_Agraph_s g) {
ENTERING("dgkssqjj566ifra0xy7m46qsb","edge_normalize");
try {
    ST_Agedge_s e;
    ST_Agnode_s n;
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
	    if ((Boolean)Z.z().sinfo.swapEnds.exe(e) && ED_spl(e)!=null)
		swap_spline(ED_spl(e));
	}
    }
} finally {
LEAVING("dgkssqjj566ifra0xy7m46qsb","edge_normalize");
}
}




//3 bwzdgdea9suuagzueyw8ztx42
// static void resetRW (graph_t * g) 
public static Object resetRW(Object... arg) {
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




//3 9co1bgu5603fx30juwb01faf
// static void setEdgeLabelPos (graph_t * g) 
public static Object setEdgeLabelPos(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("7y94r9t4hf6d0ltbie4f323al"); // setEdgeLabelPos (graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ma9kdgag30w5ofv1niitxbro"); //     node_t* n;
UNSUPPORTED("1o9j4rz3dizwsgefis1axd6uw"); //     textlabel_t* l;
UNSUPPORTED("cbzhyr6lytrowd5gxn3tg000a"); //     /* place regular edge labels */
UNSUPPORTED("8g62mxpap4eeua2lkn9a1iosi"); //     for (n = GD_nlist(g); n; n = ND_next(n)) {
UNSUPPORTED("53rvntgqit26uu0ydhawavshp"); // 	if (ND_node_type(n) == 1) {
UNSUPPORTED("7sijld2wh9ulkpkumhl6dqqaj"); // 	    if (ND_alg(n)) {   // label of non-adjacent flat edge
UNSUPPORTED("5ccfgna84rl1jtgmuk8nbffqy"); // 		edge_t* fe = (edge_t*)ND_alg(n);
UNSUPPORTED("8rofnso8jnaa77ukvfpwruvyx"); // 		assert ((l = ED_label(fe)));
UNSUPPORTED("ak1kh1v4u9s5kof1svwbc6ssr"); // 		l->pos = ND_coord(n);
UNSUPPORTED("9ehteylkrnipypq5s9c6jjm67"); // 		l->set = NOT(0);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("15c8rehrm31gtqtwrj9r863k5"); // 	    else if ((l = ND_label(n))) {// label of regular edge
UNSUPPORTED("3i64wd6mr21h7x0hadumabd5r"); // 		place_vnlabel(n);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("mlknwb70zhu4paqbncp6enq9"); // 	    if (l) updateBB(g, l);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6agx6m2qof9lg57co232lwakj
// static void _dot_splines(graph_t * g, int normalize) 
static void _dot_splines(ST_Agraph_s g, int normalize)
{
ENTERING("6agx6m2qof9lg57co232lwakj","_dot_splines");
try {
    int i, j, k, n_nodes, n_edges, ind, cnt;
    ST_Agnode_s n;
    final ST_Agedgeinfo_t fwdedgeai = new ST_Agedgeinfo_t(), fwdedgebi = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedgea = new ST_Agedgepair_s(), fwdedgeb = new ST_Agedgepair_s();
    ST_Agedge_s e, e0, e1, ea, eb, le0, le1;
    ST_Agedge_s.ArrayOfStar edges;
    ST_path P;
    final ST_spline_info_t sd = new ST_spline_info_t();
    int et = (GD_flags(g) & (7 << 1));
    fwdedgea.out.base.setPtr("data", fwdedgeai);
    fwdedgeb.out.base.setPtr("data", fwdedgebi);
    if (et == (0 << 1)) return; 
    if (et == (2 << 1)) {
	resetRW (g);
	if ((GD_has_labels(g) & (1 << 0))!=0) {
UNSUPPORTED("4k888z8ymdp2b653twxc1ugbu"); // 	    agerr (AGWARN, "edge labels with splines=curved not supported in dot - use xlabels\n");
	}
	for (n = agfstnode (g); n!=null; n = agnxtnode(g, n)) {
	    for (e = agfstout(g, n); e!=null; e = agnxtout(g,e)) {
		makeStraightEdge(g, e, et, Z.z().sinfo);
	    }
	}
UNSUPPORTED("46btiag50nczzur103eqhjcup"); // 	goto finish;
    } 
    mark_lowclusters(g);
    if (routesplinesinit()!=0) return;
    P = (ST_path) zmalloc(sizeof(ST_path.class));
    /* FlatHeight = 2 * GD_nodesep(g); */
    sd.setInt("Splinesep", GD_nodesep(g) / 4);
    sd.setInt("Multisep", GD_nodesep(g));
    edges = new ST_Agedge_s.ArrayOfStar(128);
    /* compute boundaries and list of splines */
    sd.setInt("RightBound", 0);
    sd.setInt("LeftBound", 0);
    n_edges = n_nodes = 0;
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
	n_nodes += GD_rank(g).get(i).n;
	if ((n = (ST_Agnode_s) GD_rank(g).get(i).v.get(0))!=null)
	    sd.setInt("LeftBound", (int)MIN(sd.LeftBound, (ND_coord(n).x - ND_lw(n))));
	if (GD_rank(g).get(i).n!=0 && (n = (ST_Agnode_s) GD_rank(g).get(i).v.plus(GD_rank(g).get(i).n - 1).getPtr())!=null)
	    sd.setInt("RightBound", (int)MAX(sd.RightBound, (ND_coord(n).x + ND_rw(n))));
	sd.setInt("LeftBound", sd.LeftBound - 16);
	sd.setInt("RightBound", sd.RightBound + 16);
	for (j = 0; j < GD_rank(g).get(i).n; j++) {
	    n = (ST_Agnode_s) GD_rank(g).get(i).v.get(j);
		/* if n is the label of a flat edge, copy its position to
		 * the label.
		 */
	    if (ND_alg(n)!=null) {
		ST_Agedge_s fe = (ST_Agedge_s) ND_alg(n);
		assert (ED_label(fe)!=null);
		ED_label(fe).setStruct("pos", ND_coord(n));
		ED_label(fe).set= NOTI(false);
	    }
	    if ((ND_node_type(n) != 0) &&
		((Boolean)Z.z().sinfo.splineMerge.exe(n) == false))
		continue;
	    for (k = 0; (e = (ST_Agedge_s) ND_out(n).getFromList(k))!=null; k++) {
		if ((ED_edge_type(e) == 4)
		    || (ED_edge_type(e) == 6))
		    continue;
		setflags(e, 1, 16, 64);
		edges.plus(n_edges++).setPtr(e);
		if (n_edges % 128 == 0)
		    edges = ALLOC_allocated_ST_Agedge_s (edges, n_edges + 128);
	    }
	    if (ND_flat_out(n).listNotNull())
		for (k = 0; (e = (ST_Agedge_s) ND_flat_out(n).getFromList(k))!=null; k++) {
		    setflags(e, 2, 0, 128);
			edges.plus(n_edges++).setPtr(e);
		    if (n_edges % 128 == 0)
			    edges = ALLOC_allocated_ST_Agedge_s (edges, n_edges + 128);
		}
	    if (ND_other(n).listNotNull()) {
		/* In position, each node has its rw stored in mval and,
                 * if a node is part of a loop, rw may be increased to
                 * reflect the loops and associated labels. We restore
                 * the original value here. 
                 */
		if (ND_node_type(n) == 0) {
		    double tmp = ND_rw(n);
		    ND_rw(n, ND_mval(n));
		    ND_mval(n, tmp);
		}
		for (k = 0; (e = (ST_Agedge_s) ND_other(n).getFromList(k))!=null; k++) {
		    setflags(e, 0, 0, 128);
			edges.plus(n_edges++).setPtr(e);
		    if (n_edges % 128 == 0)
			    edges = ALLOC_allocated_ST_Agedge_s (edges, n_edges + 128);
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
    qsort(edges,
    n_edges,
    function(dotsplines__c.class, "edgecmp"));
    /* FIXME: just how many boxes can there be? */
    P.boxes = ST_boxf.malloc(n_nodes + 20 * 2 * 9);
    sd.Rank_box = ST_boxf.malloc(i);
    if (et == (1 << 1)) {
    /* place regular edge labels */
	for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	    if ((ND_node_type(n) == 1) && (ND_label(n)!=null)) {
		place_vnlabel(n);
	    }
	}
    }
    for (i = 0; i < n_edges;) {
 	boolean havePorts;
	ind = i;
	le0 = getmainedge((e0 = (ST_Agedge_s) edges.plus(i++).getPtr()));
	if (ED_tail_port(e0).defined!=0 || ED_head_port(e0).defined!=0) {
	    havePorts = NOT(false);
	    ea = e0;
	} else {
	    havePorts = false;
	    ea =  le0;
	}
	if ((ED_tree_index(ea) & 32)!=0) {
	    MAKEFWDEDGE(fwdedgea.out, ea);
	    ea = (ST_Agedge_s) fwdedgea.out;
	}
	for (cnt = 1; i < n_edges; cnt++, i++) {
	    if (NEQ(le0, (le1 = getmainedge((e1 = (ST_Agedge_s) edges.get(i))))))
		break;
	    if (ED_adjacent(e0)!=0) continue; /* all flat adjacent edges at once */
	    if (ED_tail_port(e1).defined!=0 || ED_head_port(e1).defined!=0) {
		if (N(havePorts)) break;
		else
		    eb = e1;
	    } else {
		if (havePorts) break;
		else
		    eb = le1;
	    }
	    if ((ED_tree_index(eb) & 32)!=0) {
		MAKEFWDEDGE(fwdedgeb.out, eb);
		eb = (ST_Agedge_s) fwdedgeb.out;
	    }
	    if (portcmp(ED_tail_port(ea), ED_tail_port(eb))!=0)
		break;
	    if (portcmp(ED_head_port(ea), ED_head_port(eb))!=0)
		break;
	    if ((ED_tree_index(e0) & 15) == 2
		&& NEQ(ED_label(e0), ED_label(e1)))
		break;
	    if ((ED_tree_index(edges.get(i)) & 64)!=0)	/* Aha! -C is on */
		break;
	}
	if (EQ(agtail(e0), aghead(e0))) {
	    int b, sizey, r;
	    n = agtail(e0);
	    r = ND_rank(n);
	    if (r == GD_maxrank(g)) {
		if (r > 0)
		    sizey = (int) (ND_coord(GD_rank(g).get(r-1).v.get(0)).y - ND_coord(n).y);
		else
		    sizey = (int) ND_ht(n);
	    }
	    else if (r == GD_minrank(g)) {
		sizey = (int)(ND_coord(n).y - ND_coord(GD_rank(g).get(r+1).v.get(0)).y);
	    }
	    else {
		int upy = (int) (ND_coord(GD_rank(g).get(r-1).v.get(0)).y - ND_coord(n).y);
		int dwny = (int) (ND_coord(n).y - ND_coord(GD_rank(g).get(r+1).v.get(0)).y);
		sizey = MIN(upy, dwny);
	    }
	    makeSelfEdge(P, edges, ind, cnt, sd.Multisep, sizey/2, Z.z().sinfo);
	    for (b = 0; b < cnt; b++) {
		e = (ST_Agedge_s) edges.plus(ind+b).getPtr();
		if (ED_label(e)!=null)
		    updateBB(g, ED_label(e));
	    }
	}
	else if (ND_rank(agtail(e0)) == ND_rank(aghead(e0))) {
	    make_flat_edge(g, sd, P, edges, ind, cnt, et);
	}
	else
	    make_regular_edge(g, sd, P, edges, ind, cnt, et);
    }
    /* place regular edge labels */
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if ((ND_node_type(n) == 1) && (ND_label(n))!=null) {
	    place_vnlabel(n);
	    updateBB(g, ND_label(n));
	}
    }
    /* normalize splines so they always go from tail to head */
    /* place_portlabel relies on this being done first */
    if (normalize!=0)
	edge_normalize(g);
finish :
    /* vladimir: place port labels */
    /* FIX: head and tail labels are not part of cluster bbox */
    if ((Z.z().E_headlabel!=null || Z.z().E_taillabel!=null) && (Z.z().E_labelangle!=null || Z.z().E_labeldistance!=null)) {
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
    if (et != (2 << 1)) {
	Memory.free(edges);
	Memory.free(P.boxes);
	Memory.free(P);
	Memory.free(sd.Rank_box);
	routesplinesterm();
    } 
    Z.z().State = 1;
    Z.z().EdgeLabelsDone = 1;
} finally {
LEAVING("6agx6m2qof9lg57co232lwakj","_dot_splines");
}
}




//3 5n306wbdfjbfnimdo9lg6jjaa
// void dot_splines(graph_t * g) 
public static void dot_splines(ST_Agraph_s g) {
ENTERING("5n306wbdfjbfnimdo9lg6jjaa","dot_splines");
try {
    _dot_splines (g, 1);
} finally {
LEAVING("5n306wbdfjbfnimdo9lg6jjaa","dot_splines");
}
}




//3 8jja9ukzsq8tlb9yy7uzavg91
// static void  place_vnlabel(node_t * n) 
public static void place_vnlabel(ST_Agnode_s n) {
ENTERING("8jja9ukzsq8tlb9yy7uzavg91","place_vnlabel");
try {
    final ST_pointf dimen = new ST_pointf();
    double width;
    ST_Agedge_s e;
    if (ND_in(n).size == 0)
	return;			/* skip flat edge labels here */
    for (e = (ST_Agedge_s) ND_out(n).getFromList(0); ED_edge_type(e) != 0;
	 e = ED_to_orig(e));
    dimen.___(ED_label(e).dimen);
    width = GD_flip(agraphof(n))!=0 ? dimen.y : dimen.x;
    ED_label(e).pos.setDouble("x", ND_coord(n).x + width / 2.0);
    ED_label(e).pos.setDouble("y", ND_coord(n).y);
    ED_label(e).set= NOTI(false);
} finally {
LEAVING("8jja9ukzsq8tlb9yy7uzavg91","place_vnlabel");
}
}




//3 598jn37hjkm7j0kcg2nmdvlwq
// static void  setflags(edge_t *e, int hint1, int hint2, int f3) 
public static void setflags(ST_Agedge_s e, int hint1, int hint2, int f3) {
ENTERING("598jn37hjkm7j0kcg2nmdvlwq","setflags");
try {
    int f1, f2;
    if (hint1 != 0)
	f1 = hint1;
    else {
	if (EQ(agtail(e), aghead(e)))
	    if (ED_tail_port(e).defined!=0 || ED_head_port(e).defined!=0)
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




//3 1nf1s6wkn35ptjn884ii56fh
// static int edgecmp(edge_t** ptr0, edge_t** ptr1) 
public static int edgecmp(__ptr__ ptr0, __ptr__ ptr1) {
ENTERING("1nf1s6wkn35ptjn884ii56fh","edgecmp");
try {
    final ST_Agedgeinfo_t fwdedgeai = new ST_Agedgeinfo_t(), fwdedgebi = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedgea = new ST_Agedgepair_s(), fwdedgeb = new ST_Agedgepair_s();
    ST_Agedge_s e0, e1, ea, eb, le0, le1;
    int et0, et1, v0, v1, rv;
    double t0, t1;
    fwdedgea.out.base.setPtr("data", fwdedgeai);
    fwdedgeb.out.base.setPtr("data", fwdedgebi);
    e0 = (ST_Agedge_s) ptr0.getPtr();
    e1 = (ST_Agedge_s) ptr1.getPtr();
    et0 = ED_tree_index(e0) & 15;
    et1 = ED_tree_index(e1) & 15;
    if (et0 != et1)
	return (et1 - et0);
    le0 = getmainedge(e0);
    le1 = getmainedge(e1);
    t0 = ND_rank(agtail(le0)) - ND_rank(aghead(le0));
    t1 = ND_rank(agtail(le1)) - ND_rank(aghead(le1));
    v0 = ABS((int)t0);   /* ugly, but explicit as to how we avoid equality tests on fp numbers */
    v1 = ABS((int)t1);
    if (v0 != v1)
	return (v0 - v1);
    t0 = ND_coord(agtail(le0)).x - ND_coord(aghead(le0)).x;
    t1 = ND_coord(agtail(le1)).x - ND_coord(aghead(le1)).x;
    v0 = ABS((int)t0);
    v1 = ABS((int)t1);
    if (v0 != v1)
	return (v0 - v1);
    /* This provides a cheap test for edges having the same set of endpoints.  */
    if (AGSEQ(le0) != AGSEQ(le1))
	return (AGSEQ(le0) - AGSEQ(le1));
    ea = (ED_tail_port(e0).defined!=0 || ED_head_port(e0).defined!=0) ? e0 : le0;
    if ((ED_tree_index(ea) & 32)!=0) {
	MAKEFWDEDGE(fwdedgea.out, ea);
	ea = (ST_Agedge_s) fwdedgea.out;
    }
    eb = (ED_tail_port(e1).defined!=0 || ED_head_port(e1).defined!=0) ? e1 : le1;
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
    if (et0 == 2 && NEQ(ED_label(e0), ED_label(e1)))
	 UNSUPPORTED("return (int) (ED_label(e0) - ED_label(e1))");
    return (AGSEQ(e0) - AGSEQ(e1));
} finally {
LEAVING("1nf1s6wkn35ptjn884ii56fh","edgecmp");
}
}




//3 djq8tev8thshox7bob64vi0tf
// static void setState (graph_t* auxg, attr_state_t* attr_state) 
public static Object setState(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("7ovh16jwyjc9wtu0rfxnlws9r"); // setState (graph_t* auxg, attr_state_t* attr_state)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ko3jd94xpoli03tk73ardy84"); //     /* save state */
UNSUPPORTED("3ibgalw3fchq736xx1ozbvh27"); //     attr_state->E_constr = E_constr;
UNSUPPORTED("e9yatqfh98i71w3v1jcx1agi9"); //     attr_state->E_samehead = E_samehead;
UNSUPPORTED("57rkvwknyk9urdhck24todttg"); //     attr_state->E_sametail = E_sametail;
UNSUPPORTED("4y5i0lqx0ze46l88ehzdh2x1z"); //     attr_state->E_weight = E_weight;
UNSUPPORTED("3yryq1e1hmam5hqciu93ywjj2"); //     attr_state->E_minlen = E_minlen;
UNSUPPORTED("1n91lxusirg2v758j4v9o9hcg"); //     attr_state->E_fontcolor = E_fontcolor;
UNSUPPORTED("1ssj9fq3o1sm21ggttj4r3mo0"); //     attr_state->E_fontname = E_fontname;
UNSUPPORTED("45ntpz46gx7j2hfu2r6ceescw"); //     attr_state->E_fontsize = E_fontsize;
UNSUPPORTED("6vxi0wxhhjg065ishw2vebsqv"); //     attr_state->E_headclip = E_headclip;
UNSUPPORTED("aqu557ovwt2k4j894b5p9dssa"); //     attr_state->E_headlabel = E_headlabel;
UNSUPPORTED("6shb2vynxxgck2x8e8oz63ye"); //     attr_state->E_label = E_label;
UNSUPPORTED("84eq22wy1zlxwofo8cwwbddbb"); //     attr_state->E_label_float = E_label_float;
UNSUPPORTED("czndn89byb90jujvx2sudb6d6"); //     attr_state->E_labelfontcolor = E_labelfontcolor;
UNSUPPORTED("385rz6p4kom75dmgvepqmxlrq"); //     attr_state->E_labelfontname = E_labelfontname;
UNSUPPORTED("397fs88n9xnlcqo4z8at7j66s"); //     attr_state->E_labelfontsize = E_labelfontsize;
UNSUPPORTED("f2nu6sif9lmukmlj2um7gnxns"); //     attr_state->E_tailclip = E_tailclip;
UNSUPPORTED("3ctu9gb3ojun885w9ymnch0er"); //     attr_state->E_taillabel = E_taillabel;
UNSUPPORTED("qiafl7ru9bq54qol6lov5rgm"); //     attr_state->E_xlabel = E_xlabel;
UNSUPPORTED("ap9hj5xd4dfmwws8egpgvk5ti"); //     attr_state->N_height = N_height;
UNSUPPORTED("ctvaloqgyn45in0jdkkvdpf6g"); //     attr_state->N_width = N_width;
UNSUPPORTED("75ey3ud905155tfmw9zapeawj"); //     attr_state->N_shape = N_shape;
UNSUPPORTED("19bnzpge8znnwzna8ub6otjze"); //     attr_state->N_style = N_style;
UNSUPPORTED("6242e8neunx553zs7jb52e2i3"); //     attr_state->N_fontsize = N_fontsize;
UNSUPPORTED("1rjcall3fixy49t0s4bcdgoho"); //     attr_state->N_fontname = N_fontname;
UNSUPPORTED("1bj2vtyz3gettntmktngyjv02"); //     attr_state->N_fontcolor = N_fontcolor;
UNSUPPORTED("1mj5q5f1eft9otmsuyiadl54z"); //     attr_state->N_label = N_label;
UNSUPPORTED("5lkkhmgpur2i2arkoxbpb8lse"); //     attr_state->N_xlabel = N_xlabel;
UNSUPPORTED("3c9tzymzuj2wkftyepa2epzur"); //     attr_state->N_showboxes = N_showboxes;
UNSUPPORTED("9urvz64idw6yth9938puegx1d"); //     attr_state->N_ordering = N_ordering;
UNSUPPORTED("9wqn3lttroloie8t4urgw9dwl"); //     attr_state->N_sides = N_sides;
UNSUPPORTED("7aovr97vmuic712quqt6n4gkd"); //     attr_state->N_peripheries = N_peripheries;
UNSUPPORTED("dc9knw1esnnr7j77so6k8zblk"); //     attr_state->N_skew = N_skew;
UNSUPPORTED("eoium8gqo2cgsjv87c20ixx5k"); //     attr_state->N_orientation = N_orientation;
UNSUPPORTED("aq6is6cduc5wvclu1hjacuyk0"); //     attr_state->N_distortion = N_distortion;
UNSUPPORTED("3w13k0cqxkk8gniu6ydy3qii6"); //     attr_state->N_fixed = N_fixed;
UNSUPPORTED("32p2drvqb6h9n0118du912gv0"); //     attr_state->N_nojustify = N_nojustify;
UNSUPPORTED("e2hsbuo1kd2fghtopje0lfp2b"); //     attr_state->N_group = N_group;
UNSUPPORTED("74w0uzukx3derbgpgi60dzyyg"); //     attr_state->State = State;
UNSUPPORTED("936fjorw02qiz50qpj7y6en2m"); //     attr_state->G_ordering = G_ordering;
UNSUPPORTED("2l0v2tecrgmws3to99rcimxzm"); //     E_constr = NULL;
UNSUPPORTED("93dfyiz6pxq59j6ujx97hdd0c"); //     E_samehead = agattr(auxg,AGEDGE, "samehead", NULL);
UNSUPPORTED("7kovxag1wgr874sgxwpz7ls6a"); //     E_sametail = agattr(auxg,AGEDGE, "sametail", NULL);
UNSUPPORTED("295z0g5v309fbrrdopfy66rf4"); //     E_weight = agattr(auxg,AGEDGE, "weight", NULL);
UNSUPPORTED("3uo53r92k5fuzy9gb2i1k7612"); //     if (!E_weight)
UNSUPPORTED("4oq1f4cnu0hk0xm34kx9m79le"); // 	E_weight = agattr (auxg,AGEDGE,"weight", "");
UNSUPPORTED("aoqamti27wg8hvpyho5xmdc9"); //     E_minlen = NULL;
UNSUPPORTED("8jzaf5sdfgbpqx8y0squconvr"); //     E_fontcolor = NULL;
UNSUPPORTED("25csaeghkl1rd5cha609fm2vm"); //     E_fontname = (agattr(auxg,AGEDGE,"fontname",NULL));
UNSUPPORTED("d9ivs4hv5xdhsxwh8oz1dri02"); //     E_fontsize = (agattr(auxg,AGEDGE,"fontsize",NULL));
UNSUPPORTED("djpd3vd7suatk0n76mplhzog4"); //     E_headclip = (agattr(auxg,AGEDGE,"headclip",NULL));
UNSUPPORTED("8fy5jrgw22q72jvvjmqh6ajjb"); //     E_headlabel = NULL;
UNSUPPORTED("93a15wlfj0tmijeeyd1qb12v6"); //     E_label = (agattr(auxg,AGEDGE,"label",NULL));
UNSUPPORTED("2l6gmyi1pz4cv9i29k4u3mpjf"); //     E_label_float = (agattr(auxg,AGEDGE,"label_float",NULL));
UNSUPPORTED("dcp5qxpq37yer9cipch7q2oc4"); //     E_labelfontcolor = NULL;
UNSUPPORTED("133ni5qwdb96od0wcma7hj05h"); //     E_labelfontname = (agattr(auxg,AGEDGE,"labelfontname",NULL));
UNSUPPORTED("cq9n4kf29qgqy1ll6gandld7f"); //     E_labelfontsize = (agattr(auxg,AGEDGE,"labelfontsize",NULL));
UNSUPPORTED("2l9pqb0sug1sr8dlojy1gvik0"); //     E_tailclip = (agattr(auxg,AGEDGE,"tailclip",NULL));
UNSUPPORTED("9a173t42nyif5cx0ee6c4qos2"); //     E_taillabel = NULL;
UNSUPPORTED("pu58ta2e8cevjt84brxtyecs"); //     E_xlabel = NULL;
UNSUPPORTED("2p7j1fghgsib6tkic2cc1t601"); //     N_height = (agattr(auxg,AGNODE,"height",NULL));
UNSUPPORTED("49eok3z6e4piel4m6f7rk8fb6"); //     N_width = (agattr(auxg,AGNODE,"width",NULL));
UNSUPPORTED("8cyc904mtcb0zlwybzrqxujrv"); //     N_shape = (agattr(auxg,AGNODE,"shape",NULL));
UNSUPPORTED("1ggb495lty2zumaw3qh2d3ssd"); //     N_style = NULL;
UNSUPPORTED("6hcb194pxn8upijardzdy3v2a"); //     N_fontsize = (agattr(auxg,AGNODE,"fontsize",NULL));
UNSUPPORTED("e1cg6m56w3uwo3m2dfdh71o5x"); //     N_fontname = (agattr(auxg,AGNODE,"fontname",NULL));
UNSUPPORTED("1gjzz1tv51zd3vsgyc6b8dfdz"); //     N_fontcolor = NULL;
UNSUPPORTED("42y8wmut30tkdxrxdvh5v8xtz"); //     N_label = (agattr(auxg,AGNODE,"label",NULL));
UNSUPPORTED("7jni9lzv0nagk8mnil6vsgzpt"); //     N_xlabel = NULL;
UNSUPPORTED("5hshk2a2mwuk7cjxsc4mzky7g"); //     N_showboxes = NULL;
UNSUPPORTED("1j63bq1sarpj53iwvo4ynyeck"); //     N_ordering = (agattr(auxg,AGNODE,"ordering",NULL));
UNSUPPORTED("7yaoaugb8cqh7ksl8w4i4utlv"); //     N_sides = (agattr(auxg,AGNODE,"sides",NULL));
UNSUPPORTED("15x8xrchzah7wcc5ukej42hut"); //     N_peripheries = (agattr(auxg,AGNODE,"peripheries",NULL));
UNSUPPORTED("dcu49fws0uvo8falspy1lno4k"); //     N_skew = (agattr(auxg,AGNODE,"skew",NULL));
UNSUPPORTED("bvjg1ch1pmuvfhvf5ubyrqhl3"); //     N_orientation = (agattr(auxg,AGNODE,"orientation",NULL));
UNSUPPORTED("b67lzfds2wabbxqa4qaoj0iv8"); //     N_distortion = (agattr(auxg,AGNODE,"distortion",NULL));
UNSUPPORTED("yet2s8h61hoih7vba5lhvbn"); //     N_fixed = (agattr(auxg,AGNODE,"fixed",NULL));
UNSUPPORTED("dv9ccs2r6bgkfuzvquyym1z1k"); //     N_nojustify = NULL;
UNSUPPORTED("7kwmsq3m3f4oe4jyi2hzkq544"); //     N_group = NULL;
UNSUPPORTED("3lqtt3u3nf2945mjpkjcw6s48"); //     G_ordering = (agattr(auxg,AGRAPH,"ordering",NULL));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 14hhte1l3zo45pzop5ugwahiv
// static graph_t* cloneGraph (graph_t* g, attr_state_t* attr_state) 
public static Object cloneGraph(Object... arg) {
UNSUPPORTED("9bkg8lvj5zr49zg08g7iijeg0"); // static graph_t*
UNSUPPORTED("4uq8e6159vvwjuygnnrtonzjr"); // cloneGraph (graph_t* g, attr_state_t* attr_state)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("d4lkyrfdivg76lnwroshuwy1r"); //     Agsym_t* sym;
UNSUPPORTED("8scmnlh4aqjfli9hjn7snnhb2"); //     graph_t* auxg;
UNSUPPORTED("9wn75gbfikchs3m5ip8uqbqp9"); //     if (agisdirected(g))
UNSUPPORTED("bc2kavqqh860759qs0rw39g9w"); // 	auxg = agopen ("auxg",Agdirected, ((Agdisc_t *)0));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("41dm8ksorga6af1u82vvp2o08"); // 	auxg = agopen ("auxg",Agundirected, ((Agdisc_t *)0));
UNSUPPORTED("dvx3r3ba8ihb3ho0irr3iflz9"); //     agbindrec(auxg, "Agraphinfo_t", sizeof(Agraphinfo_t), NOT(0));
UNSUPPORTED("8o4tyxwwt6nurmyulizd8yx4h"); //     agattr(auxg, AGRAPH, "rank", "");
UNSUPPORTED("4azamavz8yei4klxixxjkjdzy"); //     GD_drawing(auxg) = (layout_t*)zmalloc(sizeof(layout_t));
UNSUPPORTED("bl2bbfcsejbdva171i3uuah14"); //     GD_drawing(auxg)->quantum = GD_drawing(g)->quantum; 
UNSUPPORTED("96jzn981deono0qtt4353wjnd"); //     GD_drawing(auxg)->dpi = GD_drawing(g)->dpi;
UNSUPPORTED("am2ry0w98859w4vlnhjmoegj2"); //     GD_charset(auxg) = GD_charset (g);
UNSUPPORTED("8qqdurbqk0iszxbs1xz13bx72"); //     if (GD_flip(g))
UNSUPPORTED("dldh0wwkfscuwx8vmqd78zne6"); // 	(GD_rankdir2(auxg) = 0);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("a1gxjxg6ob083fwqk6gac69va"); // 	(GD_rankdir2(auxg) = 1);
UNSUPPORTED("94df1x35koj4u57phwmdtoipp"); //     GD_nodesep(auxg) = GD_nodesep(g);
UNSUPPORTED("m7a4df7fb58fj3qpf7ocrnth"); //     GD_ranksep(auxg) = GD_ranksep(g);
UNSUPPORTED("ehand70vpsfxy8xo2usw3sdpd"); // 	//copy node attrs to auxg
UNSUPPORTED("ee6l5a5uekrqgzpa1tzzvn8lu"); //     sym=agnxtattr(agroot(g),AGNODE,NULL); //get the first attr.
UNSUPPORTED("e9gle5ohw021fnltrv101u94f"); //     for (; sym; sym = agnxtattr(agroot(g),AGNODE,sym))
UNSUPPORTED("4mgrl284otjjxa13ifyvsix9o"); // 	agattr (auxg, AGNODE,sym->name, sym->defval);
UNSUPPORTED("8l0npxgg53xbodt3h5bsxaf1u"); // 	//copy edge attributes
UNSUPPORTED("d0l2zhgn9atu49ot95sjvnjx7"); //     sym=agnxtattr(agroot(g),AGEDGE,NULL); //get the first attr.
UNSUPPORTED("2syhqyxjtl43g8k8nszcgv3qa"); //     for (; sym; sym = agnxtattr(agroot(g),AGEDGE,sym))
UNSUPPORTED("7ne23vf4nlppvlnjcbkj9xj7s"); // 	agattr (auxg, AGEDGE,sym->name, sym->defval);
UNSUPPORTED("dbdwjxpzch7ieese5oq712h84"); //     if (!agattr(auxg,AGEDGE, "headport", NULL))
UNSUPPORTED("8e9itu32gwm54e46x9vefuuc"); // 	agattr(auxg,AGEDGE, "headport", "");
UNSUPPORTED("8d6592shcigbk3cnbs3g6zd5o"); //     if (!agattr(auxg,AGEDGE, "tailport", NULL))
UNSUPPORTED("1afdsvho7rpgcscq77epwq7h0"); // 	agattr(auxg,AGEDGE, "tailport", "");
UNSUPPORTED("8g7yfvh0djzbxpsk11iys8s73"); //     setState (auxg, attr_state);
UNSUPPORTED("84c7zokshe6lxrxoh1l48oh0b"); //     return auxg;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 75bd23hms71i6w46oddi0v7c7
// static void cleanupCloneGraph (graph_t* g, attr_state_t* attr_state) 
public static Object cleanupCloneGraph(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("8l276u9l2gpbol4c2u457ic4o"); // cleanupCloneGraph (graph_t* g, attr_state_t* attr_state)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("81hz17qm480gekbimxzqp5917"); //     /* restore main graph syms */
UNSUPPORTED("dyp7mrvrp7pgbjcxvjp3k0rb0"); //     E_constr = attr_state->E_constr;
UNSUPPORTED("8x6a3xddh4y42i73tahevx314"); //     E_samehead = attr_state->E_samehead;
UNSUPPORTED("cr53qmp35pahyiy1lksf7pc7o"); //     E_sametail = attr_state->E_sametail;
UNSUPPORTED("3x6ebo3n7gp5ys2ei418ehbmt"); //     E_weight = attr_state->E_weight;
UNSUPPORTED("1ho97fsth1t5vvxc5xlwbtvyp"); //     E_minlen = attr_state->E_minlen;
UNSUPPORTED("bfo2q1yt9zej3021f132mqwe6"); //     E_fontcolor = attr_state->E_fontcolor;
UNSUPPORTED("caxxsouex07crddgxredl1u2p"); //     E_fontname = attr_state->E_fontname;
UNSUPPORTED("etmu0sib2tphqwe1wujlnfrup"); //     E_fontsize = attr_state->E_fontsize;
UNSUPPORTED("6gb56lwdjjgzcqilrpq8m22ns"); //     E_headclip = attr_state->E_headclip;
UNSUPPORTED("7arkupsho8bk69qdlx5osz58q"); //     E_headlabel = attr_state->E_headlabel;
UNSUPPORTED("8dbqaxdoyj9z0pkuwsg9vl0eu"); //     E_label = attr_state->E_label;
UNSUPPORTED("k2wg526i4loudvbn7p6a8e68"); //     E_label_float = attr_state->E_label_float;
UNSUPPORTED("2tux1z7sjyb58ty6c0bvgfn7u"); //     E_labelfontcolor = attr_state->E_labelfontcolor;
UNSUPPORTED("exlraaiion456s8e3xwpxozb4"); //     E_labelfontname = attr_state->E_labelfontname;
UNSUPPORTED("31yhqytdbu2odz0oi3cudffo1"); //     E_labelfontsize = attr_state->E_labelfontsize;
UNSUPPORTED("frcohw4vy68cah25ckm1cobw"); //     E_tailclip = attr_state->E_tailclip;
UNSUPPORTED("2z89ojbnte8kwtr9jotk499fc"); //     E_taillabel = attr_state->E_taillabel;
UNSUPPORTED("3ujqfey1gvz17vmhn9w63qxob"); //     E_xlabel = attr_state->E_xlabel;
UNSUPPORTED("atq6bae3mv1qr8egljjfsrit6"); //     N_height = attr_state->N_height;
UNSUPPORTED("aiqf0nz6dfedq2jwgmajnranb"); //     N_width = attr_state->N_width;
UNSUPPORTED("dt3zktdkst02y23fr3owwe9b9"); //     N_shape = attr_state->N_shape;
UNSUPPORTED("6pul6vxncmyc96hnj7hoh9xlv"); //     N_style = attr_state->N_style;
UNSUPPORTED("bbr363noiuq4fiv6dc11hrgki"); //     N_fontsize = attr_state->N_fontsize;
UNSUPPORTED("5jr71bz21o8ex4piwuj7cyf35"); //     N_fontname = attr_state->N_fontname;
UNSUPPORTED("muitbco7ytv8yr71bth3vd45"); //     N_fontcolor = attr_state->N_fontcolor;
UNSUPPORTED("gz675ugjp8z2xgply33d6llr"); //     N_label = attr_state->N_label;
UNSUPPORTED("3l56ed3d2ocqttxq2mwcaxrs"); //     N_xlabel = attr_state->N_xlabel;
UNSUPPORTED("2fmh1t4tflmptq1d2wqygd7qt"); //     N_showboxes = attr_state->N_showboxes;
UNSUPPORTED("br7qgmqsjpzi772b9wc7g00x0"); //     N_ordering = attr_state->N_ordering;
UNSUPPORTED("cpjgwylhr8d4gvztykf36mf71"); //     N_sides = attr_state->N_sides;
UNSUPPORTED("dzvcridjcoqlimn9odmlcn9ao"); //     N_peripheries = attr_state->N_peripheries;
UNSUPPORTED("7wd3t9ok31e37hdqtnffwum66"); //     N_skew = attr_state->N_skew;
UNSUPPORTED("bkocxpinr1crae21mcidps0"); //     N_orientation = attr_state->N_orientation;
UNSUPPORTED("bni1bp9we328ofu8dqwnws3b8"); //     N_distortion = attr_state->N_distortion;
UNSUPPORTED("9ni0gtudygio7exe31uyvwwxy"); //     N_fixed = attr_state->N_fixed;
UNSUPPORTED("2ep79qyr9wgfu8hyateqngx4o"); //     N_nojustify = attr_state->N_nojustify;
UNSUPPORTED("77t5uu3931h080u18f50zr0qw"); //     N_group = attr_state->N_group;
UNSUPPORTED("1czw51da8yggj3mvb5v6dircs"); //     G_ordering = attr_state->G_ordering;
UNSUPPORTED("60kkiysc7uy667s1kr04csskc"); //     State = attr_state->State;
UNSUPPORTED("9wfeu3yikah30i1eq789pb1dc"); //     free (attr_state);
UNSUPPORTED("2tv8es032rxzavamw0dgvm53h"); //     dot_cleanup(g);
UNSUPPORTED("ego3r1tvwrpv2hie8y9p8vq4z"); //     agclose(g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 19tqa2ag8idq1y6l3zslax8e8
// static node_t* cloneNode (graph_t* g, node_t* orign, int flipped) 
public static Object cloneNode(Object... arg) {
UNSUPPORTED("b9dd3satxbh59hljdxzcxecc"); // static node_t*
UNSUPPORTED("o807sh2podu4gxrp6mweyg3p"); // cloneNode (graph_t* g, node_t* orign, int flipped)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9mlzre34kwhn9wm44mck7e8li"); //     node_t* n = agnode(g, agnameof(orign),1);
UNSUPPORTED("21wqfabmz2qk7g2vnunyyw1n8"); //     agbindrec(n, "Agnodeinfo_t", sizeof(Agnodeinfo_t), NOT(0));
UNSUPPORTED("80tteudx6zbkyxy4erhh0fqeo"); //     agcopyattr (orign, n);
UNSUPPORTED("65o4lsnpkxraq4wik0gzbv3g3"); //     if (shapeOf(orign) == SH_RECORD) {
UNSUPPORTED("7w0wn4e9gizbzrsqcre4pnx7"); // 	int lbllen = strlen(ND_label(orign)->text);
UNSUPPORTED("3avq2zh3wpi2nrhofz3l9esev"); //         char* buf = (char*)gmalloc((lbllen+3)*sizeof(char));
UNSUPPORTED("bbilsao6wizinl5kripcoya9d"); //         sprintf (buf, "{%s}", ND_label(orign)->text);
UNSUPPORTED("e93wd087tiy2p8rvqlme93ynm"); // 	agset (n, "label", buf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("69hc24ic55i66g8tf2ne42327"); //     return n;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eqzwnj9u1k6ij1842mph3kly9
// static edge_t* cloneEdge (graph_t* g, node_t* tn, node_t* hn, edge_t* orig) 
public static Object cloneEdge(Object... arg) {
UNSUPPORTED("adyfsyiyu158mwhrtm33biik2"); // static edge_t*
UNSUPPORTED("cpg8ogbdq9zzrqiadh2q271ky"); // cloneEdge (graph_t* g, node_t* tn, node_t* hn, edge_t* orig)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("mdnixwzb9bm22acv2mbc4yo2"); //     edge_t* e = agedge(g, tn, hn,NULL,1);
UNSUPPORTED("ce403szny1a2xoli6uh7ocakq"); //     /* for (; ED_edge_type(orig) != NORMAL; orig = ED_to_orig(orig)); */
UNSUPPORTED("554nt9t4qnutd0wixwbpok522"); //     agbindrec(e, "Agedgeinfo_t", sizeof(Agedgeinfo_t), NOT(0));
UNSUPPORTED("6yfr23jllhpfychz887mht80b"); //     agcopyattr (orig, e);
UNSUPPORTED("4v614d3uabme2jyn0anuritbb"); // /*
UNSUPPORTED("jgkgn8228j52jq4wc7yo2q8t"); //     if (orig->tail != ND_alg(tn)) {
UNSUPPORTED("86dayio2rrehbyot92z4xh4d1"); // 	char* hdport = agget (orig, HEAD_ID);
UNSUPPORTED("39vbjr08q96wugxyva077qpok"); // 	char* tlport = agget (orig, TAIL_ID);
UNSUPPORTED("3z0livr6yj8gkkuxtvqwrqy95"); // 	agset (e, TAIL_ID, (hdport ? hdport : ""));
UNSUPPORTED("dilecd9q5c2tj0pnl7tfwgixx"); // 	agset (e, HEAD_ID, (tlport ? tlport : ""));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bnetqzovnscxile7ao44kc0qd"); // */
UNSUPPORTED("2bswif6w6ot01ynlvkimntfly"); //     return e;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3zb2qrqaqzr6n3m7dgxp4pmrp
// static pointf transformf (pointf p, pointf del, int flip) 
public static Object transformf(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("a59oomtgi9wbd42kpsaaref6e"); // transformf (pointf p, pointf del, int flip)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9wlzvfxuhvsgkkq6lvlrw01d2"); //     if (flip) {
UNSUPPORTED("3vhnqvj1whk5vk9hofkhjtksr"); // 	double i = p.x;
UNSUPPORTED("9sla4yejg4e4tlklijoil9k4u"); // 	p.x = p.y;
UNSUPPORTED("688l858wkw5j2uqosjyesr15h"); // 	p.y = -i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("890s36h0fh77q487p72cjtyz"); //     return add_pointf(p, del);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bmsa24i3avg14po4sp17yh89k
// static int edgelblcmpfn(edge_t** ptr0, edge_t** ptr1) 
public static int edgelblcmpfn(__ptr__ ptr0, __ptr__ ptr1) {
ENTERING("bmsa24i3avg14po4sp17yh89k","edgelblcmpfn");
try {
    ST_Agedge_s e0, e1;
    final ST_pointf sz0 = new ST_pointf(), sz1 = new ST_pointf();
    e0 = (ST_Agedge_s) ptr0.getPtr();
    e1 = (ST_Agedge_s) ptr1.getPtr();
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




//3 3xmylrnypvoqrj2yrxnomsj5k
// static void makeSimpleFlatLabels (node_t* tn, node_t* hn, edge_t** edges, int ind, int cnt, int et, int n_lbls) 
public static void makeSimpleFlatLabels(ST_Agnode_s tn, ST_Agnode_s hn, ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, int et, int n_lbls) {
ENTERING("3xmylrnypvoqrj2yrxnomsj5k","makeSimpleFlatLabels");
try {
	ST_pointf.Array ps;
    final ST_Ppoly_t poly = new ST_Ppoly_t();
    int pn[] = new int[1];
	ST_Agedge_s e = (ST_Agedge_s) edges.get(ind);
    final ST_pointf.Array points = new ST_pointf.Array(10);
    final ST_pointf tp = new ST_pointf(), hp = new ST_pointf();
    int i, pointn;
    double leftend, rightend, ctrx=0, ctry=0, miny, maxy;
    double uminx=0, umaxx=0;
    double lminx=0, lmaxx=0;
    ST_Agedge_s.ArrayOfStar earray = new ST_Agedge_s.ArrayOfStar(cnt);
    for (i = 0; i < cnt; i++) {
	earray.plus(i).setPtr(edges.plus(ind + i).getPtr());
    }
    qsort(earray,
    cnt,
    function(dotsplines__c.class, "edgelblcmpfn"));
    tp.___(add_pointf(ND_coord(tn), (ST_pointf) ED_tail_port(e).p));
    hp.___(add_pointf(ND_coord(hn), (ST_pointf) ED_head_port(e).p));
    leftend = tp.x+ND_rw(tn);
    rightend = hp.x-ND_lw(hn);
    ctrx = (leftend + rightend)/2.0;
    /* do first edge */
    e = (ST_Agedge_s) earray.get(0);
    pointn = 0;
    points.plus(pointn++).setStruct(tp);
    points.plus(pointn++).setStruct(tp);
    points.plus(pointn++).setStruct(hp);
    points.plus(pointn++).setStruct(hp);
    clip_and_install(e, aghead(e), points.asPtr(), pointn, Z.z().sinfo);
    ED_label(e).pos.setDouble("x", ctrx);
    ED_label(e).pos.setDouble("y", tp.y + (ED_label(e).dimen.y+6)/2.0);
    ED_label(e).set= NOTI(false);
    miny = tp.y + 6/2.0;
    maxy = miny + ED_label(e).dimen.y;
    uminx = ctrx - (ED_label(e).dimen.x)/2.0;
    umaxx = ctrx + (ED_label(e).dimen.x)/2.0;
    for (i = 1; i < n_lbls; i++) {
	e = (ST_Agedge_s) earray.get(i);
	if (i%2!=0) {  /* down */
	    if (i == 1) {
		lminx = ctrx - (ED_label(e).dimen.x)/2.0;
		lmaxx = ctrx + (ED_label(e).dimen.x)/2.0;
		}
	    miny -= 6 + ED_label(e).dimen.y;
	    points.plus(0).setStruct(tp);
	    points.plus(1).setDouble("x", tp.x);
	    points.plus(1).setDouble("y", miny - 6);
	    points.plus(2).setDouble("x", hp.x);
	    points.plus(2).setDouble("y", points.get(1).y);
	    points.plus(3).setStruct(hp);
	    points.plus(4).setDouble("x", lmaxx);
	    points.plus(4).setDouble("y", hp.y);
	    points.plus(5).setDouble("x", lmaxx);
	    points.plus(5).setDouble("y", miny);
	    points.plus(6).setDouble("x", lminx);
	    points.plus(6).setDouble("y", miny);
	    points.plus(7).setDouble("x", lminx);
	    points.plus(7).setDouble("y", tp.y);
	    ctry = miny + (ED_label(e).dimen.y)/2.0;
	}
	else {   /* up */
UNSUPPORTED("7owdudualx55z2cnm9x3iio0w"); // 	    points[0] = tp;
UNSUPPORTED("43w0zont6q3y1axlcy96rzm5x"); // 	    points[1].x = uminx;
UNSUPPORTED("285u4l65puy5nr3pgq6acl4i2"); // 	    points[1].y = tp.y;
UNSUPPORTED("uctdrwzmec4w6vmirs9on197"); // 	    points[2].x = uminx;
UNSUPPORTED("d9b8e2upja8koam9memys7nj1"); // 	    points[2].y = maxy;
UNSUPPORTED("e8a8ucdpq7sgmbi3qyldleb1s"); // 	    points[3].x = umaxx;
UNSUPPORTED("d7xd9vzbdbezltrxsp9a3byuc"); // 	    points[3].y = maxy;
UNSUPPORTED("59abeyxxk9ow1g6m45t4ahih7"); // 	    points[4].x = umaxx;
UNSUPPORTED("2lzgl5468xguophz9d5wyer2x"); // 	    points[4].y = hp.y;
UNSUPPORTED("41u0uroraw4xanvpgg6l74kyh"); // 	    points[5].x = hp.x;
UNSUPPORTED("cwkk5bnko3e1udrx4cb720zss"); // 	    points[5].y = hp.y;
UNSUPPORTED("eev7hf3617k74bnq18uiedyb0"); // 	    points[6].x = hp.x;
UNSUPPORTED("aqxt1dvgfq5zcptjwgx1b3mmq"); // 	    points[6].y = maxy + 6;
UNSUPPORTED("5s3o04yf5lzca6ruuygfxngj2"); // 	    points[7].x = tp.x;
UNSUPPORTED("56zheeat0b8mo4uvlcbvgbu80"); // 	    points[7].y = maxy + 6;
UNSUPPORTED("ehcig8trxc4ble6pcochubv7z"); // 	    ctry =  maxy + (ED_label(e)->dimen.y)/2.0 + 6;
UNSUPPORTED("e2zgxycgqefryde9nbv6cqz1u"); // 	    maxy += ED_label(e)->dimen.y + 6;
	}
	poly.pn = 8;
	poly.ps = points;
	ps = simpleSplineRoute (tp, hp, poly, pn, et == (3 << 1));
	if (pn[0] == 0) return;
	ED_label(e).pos.setDouble("x", ctrx);
	ED_label(e).pos.setDouble("y", ctry);
	ED_label(e).set= NOTI(false);
	clip_and_install(e, aghead(e), ps, pn[0], Z.z().sinfo);
    }
    /* edges with no labels */
    for (; i < cnt; i++) {
	e = (ST_Agedge_s) earray.get(i);
	if (i%2!=0) {  /* down */
	    if (i == 1) {
		lminx = (2*leftend + rightend)/3.0;
		lmaxx = (leftend + 2*rightend)/3.0;
	    }
	    miny -= 6;
	    points.plus(0).setStruct(tp);
	    points.plus(1).setDouble("x", tp.x);
	    points.plus(1).setDouble("y", miny - 6);
	    points.plus(2).setDouble("x", hp.x);
	    points.plus(2).setDouble("y", points.get(1).y);
	    points.plus(3).setStruct(hp);
	    points.plus(4).setDouble("x", lmaxx);
	    points.plus(4).setDouble("y", hp.y);
	    points.plus(5).setDouble("x", lmaxx);
	    points.plus(5).setDouble("y", miny);
	    points.plus(6).setDouble("x", lminx);
	    points.plus(6).setDouble("y", miny);
	    points.plus(7).setDouble("x", lminx);
	    points.plus(7).setDouble("y", tp.y);
	}
	else {   /* up */
UNSUPPORTED("7owdudualx55z2cnm9x3iio0w"); // 	    points[0] = tp;
UNSUPPORTED("43w0zont6q3y1axlcy96rzm5x"); // 	    points[1].x = uminx;
UNSUPPORTED("285u4l65puy5nr3pgq6acl4i2"); // 	    points[1].y = tp.y;
UNSUPPORTED("uctdrwzmec4w6vmirs9on197"); // 	    points[2].x = uminx;
UNSUPPORTED("d9b8e2upja8koam9memys7nj1"); // 	    points[2].y = maxy;
UNSUPPORTED("e8a8ucdpq7sgmbi3qyldleb1s"); // 	    points[3].x = umaxx;
UNSUPPORTED("d7xd9vzbdbezltrxsp9a3byuc"); // 	    points[3].y = maxy;
UNSUPPORTED("59abeyxxk9ow1g6m45t4ahih7"); // 	    points[4].x = umaxx;
UNSUPPORTED("2lzgl5468xguophz9d5wyer2x"); // 	    points[4].y = hp.y;
UNSUPPORTED("41u0uroraw4xanvpgg6l74kyh"); // 	    points[5].x = hp.x;
UNSUPPORTED("cwkk5bnko3e1udrx4cb720zss"); // 	    points[5].y = hp.y;
UNSUPPORTED("eev7hf3617k74bnq18uiedyb0"); // 	    points[6].x = hp.x;
UNSUPPORTED("aqxt1dvgfq5zcptjwgx1b3mmq"); // 	    points[6].y = maxy + 6;
UNSUPPORTED("5s3o04yf5lzca6ruuygfxngj2"); // 	    points[7].x = tp.x;
UNSUPPORTED("56zheeat0b8mo4uvlcbvgbu80"); // 	    points[7].y = maxy + 6;
UNSUPPORTED("7un2qk34mmmhqi296vl50bacs"); // 	    maxy += + 6;
	}
	poly.pn = 8;
	poly.ps = points;
	ps = simpleSplineRoute (tp, hp, poly, pn, et == (3 << 1));
	if (pn[0] == 0) return;
	clip_and_install(e, aghead(e), ps, pn[0], Z.z().sinfo);
    }
    Memory.free (earray);
} finally {
LEAVING("3xmylrnypvoqrj2yrxnomsj5k","makeSimpleFlatLabels");
}
}




//3 8kqyzk43ovc2sq6jegua6ytp
// static void makeSimpleFlat (node_t* tn, node_t* hn, edge_t** edges, int ind, int cnt, int et) 
public static void makeSimpleFlat(ST_Agnode_s tn, ST_Agnode_s hn, ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, int et) {
ENTERING("8kqyzk43ovc2sq6jegua6ytp","makeSimpleFlat");
try {
    ST_Agedge_s e = (ST_Agedge_s) edges.get(ind);
    final ST_pointf.Array points = new ST_pointf.Array( 10);
    final ST_pointf tp = new ST_pointf(), hp = new ST_pointf();
    int i, pointn;
    double stepy, dy;
    tp.___(add_pointf(ND_coord(tn), (ST_pointf) ED_tail_port(e).p));
    hp.___(add_pointf(ND_coord(hn), (ST_pointf) ED_head_port(e).p));
    stepy = (cnt > 1) ? ND_ht(tn) / (double)(cnt - 1) : 0.;
    dy = tp.y - ((cnt > 1) ? ND_ht(tn) / 2. : 0.);
    for (i = 0; i < cnt; i++) {
	e = (ST_Agedge_s) edges.plus(ind + i).getPtr();
	pointn = 0;
	if ((et == (5 << 1)) || (et == (1 << 1))) {
	    points.plus(pointn++).setStruct(tp);
	    points.plus(pointn++).setStruct(pointfof((2 * tp.x + hp.x) / 3, dy));
	    points.plus(pointn++).setStruct(pointfof((2 * hp.x + tp.x) / 3, dy));
	    points.plus(pointn++).setStruct(hp);
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
	clip_and_install(e, aghead(e), points.asPtr(), pointn, Z.z().sinfo);
    }
} finally {
LEAVING("8kqyzk43ovc2sq6jegua6ytp","makeSimpleFlat");
}
}




//3 bhnjospwghq4plid12757c928
// static void make_flat_adj_edges(graph_t* g, path* P, edge_t** edges, int ind, int cnt, edge_t* e0,                     int et) 
public static void make_flat_adj_edges(ST_Agraph_s g, ST_path P, ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, ST_Agedge_s e0, int et) {
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
    attr_state_t attrs;
    tn = agtail(e0); hn = aghead(e0);
    for (i = 0; i < cnt; i++) {
	e = (ST_Agedge_s) edges.plus(ind + i).getPtr();
	if (ED_label(e)!=null) labels++;
	if (ED_tail_port(e).defined!=0 || ED_head_port(e).defined!=0) ports = 1;
    }
    if (ports == 0) {
	/* flat edges without ports and labels can go straight left to right */
	if (labels == 0) {
	    makeSimpleFlat (tn, hn, edges, ind, cnt, et);
	}
	/* flat edges without ports but with labels take more work */
	else {
	    makeSimpleFlatLabels (tn, hn, edges, ind, cnt, et, labels);
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
public static void makeFlatEnd(ST_Agraph_s g, ST_spline_info_t sp, ST_path P, ST_Agnode_s n, ST_Agedge_s e, ST_pathend_t endp, boolean isBegin) {
ENTERING("fybar4mljnmkh3kure5k1eod","makeFlatEnd");
try {
    final ST_boxf b = new ST_boxf();
    b.___(maximal_bbox(g, sp, n, null, e));
    endp.setStruct("nb", b);
    endp.setInt("sidemask", 1<<2);
    if (isBegin) beginpath(P, e, 2, endp, false);
    else endpath(P, e, 2, endp, false);
    b.UR.y = endp.boxes[endp.boxn - 1].UR.y;
    b.LL.y = endp.boxes[endp.boxn - 1].LL.y;
    b.___(makeregularend((ST_boxf) b, (1<<2), ND_coord(n).y + GD_rank(g).get(ND_rank(n)).ht2));
    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("cmjm4y40vf7wklmgz0ae4k36v"); // 	endp->boxes[endp->boxn++] = b;
} finally {
LEAVING("fybar4mljnmkh3kure5k1eod","makeFlatEnd");
}
}




//3 rfro0b2nsukdjenux3f6osgj
// static void makeBottomFlatEnd (graph_t* g, spline_info_t* sp, path* P, node_t* n, edge_t* e,  	pathend_t* endp, boolean isBegin) 
public static Object makeBottomFlatEnd(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("8ai1jk8aigoqod7sj7pw1phw3"); // makeBottomFlatEnd (graph_t* g, spline_info_t* sp, path* P, node_t* n, edge_t* e, 
UNSUPPORTED("25ij2tn6de7b481tpf1ac644s"); // 	pathend_t* endp, boolean isBegin)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c57pq0f87j6dnbcvygu7v6k84"); //     boxf b;
UNSUPPORTED("458vjrt5zjt6am715aiuxdlpo"); //     b = endp->nb = maximal_bbox(g, sp, n, NULL, e);
UNSUPPORTED("9wzfokn3t7zckgoaxgywy6cbz"); //     endp->sidemask = (1<<0);
UNSUPPORTED("c3u0b2z8mb4le2v1g6q8amjt"); //     if (isBegin) beginpath(P, e, 2, endp, 0);
UNSUPPORTED("1e0zyjb03r85tgqddfnlk9e9"); //     else endpath(P, e, 2, endp, 0);
UNSUPPORTED("7pwqqvagpkzye4cdmjlbrmt7a"); //     b.UR.y = endp->boxes[endp->boxn - 1].UR.y;
UNSUPPORTED("1mefjjoto40rfwb1qhzvobhje"); //     b.LL.y = endp->boxes[endp->boxn - 1].LL.y;
UNSUPPORTED("4sn8dc58suk2d3oqlu13cr7ic"); //     b = makeregularend(b, (1<<0), ND_coord(n).y - GD_rank(g)[ND_rank(n)].ht2);
UNSUPPORTED("6xj9sv3inmj1rwtz76qp6p30n"); //     if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("cmjm4y40vf7wklmgz0ae4k36v"); // 	endp->boxes[endp->boxn++] = b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 w8ptjibydq995d2lexg85mku
// static void make_flat_labeled_edge(graph_t* g, spline_info_t* sp, path* P, edge_t* e, int et) 
public static void make_flat_labeled_edge(ST_Agraph_s g, ST_spline_info_t sp, ST_path P, ST_Agedge_s e, int et) {
ENTERING("w8ptjibydq995d2lexg85mku","make_flat_labeled_edge");
try {
    ST_Agnode_s tn, hn, ln;
    ST_pointf.Array ps = null;
    final ST_pathend_t tend = new ST_pathend_t(), hend = new ST_pathend_t();
    final ST_boxf lb = new ST_boxf();
    int boxn, i, ydelta;
    int pn[] = new int[1];
    ST_Agedge_s f;
    final ST_pointf.Array points = new ST_pointf.Array( 7);
    tn = agtail(e);
    hn = aghead(e);
    for (f = ED_to_virt(e); ED_to_virt(f)!=null; f = ED_to_virt(f));
    ln = agtail(f);
    ED_label(e).setStruct("pos", ND_coord(ln));
    ED_label(e).set= NOTI(false);
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
	ydelta = (int)(ND_coord(ln).y - GD_rank(g).get(ND_rank(tn)).ht1 -
		ND_coord(tn).y + GD_rank(g).get(ND_rank(tn)).ht2);
	ydelta = (int)(ydelta / 6.);
	lb.LL.y = lb.UR.y - MAX(5.,ydelta); 
	boxn = 0;
	makeFlatEnd (g, sp, P, tn, e,  tend, NOT(false));
	makeFlatEnd (g, sp, P, hn, e,  hend, false);
	((Z.z().boxes[boxn])).LL.x = (tend).boxes[tend.boxn - 1].LL.x; 
	((Z.z().boxes[boxn])).LL.y = (tend).boxes[tend.boxn - 1].UR.y; 
	((Z.z().boxes[boxn])).UR.x = lb.LL.x;
	((Z.z().boxes[boxn])).UR.y = lb.LL.y;
	boxn++;
	((Z.z().boxes[boxn])).LL.x = (tend).boxes[tend.boxn - 1].LL.x; 
	((Z.z().boxes[boxn])).LL.y = lb.LL.y;
	((Z.z().boxes[boxn])).UR.x = (hend).boxes[hend.boxn - 1].UR.x;
	((Z.z().boxes[boxn])).UR.y = lb.UR.y;
	boxn++;
	((Z.z().boxes[boxn])).LL.x = lb.UR.x;
	((Z.z().boxes[boxn])).UR.y = lb.LL.y;
	((Z.z().boxes[boxn])).LL.y = (hend).boxes[hend.boxn - 1].UR.y; 
	((Z.z().boxes[boxn])).UR.x = (hend).boxes[hend.boxn - 1].UR.x;
	boxn++;
	for (i = 0; i < tend.boxn; i++) add_box(P, (tend).boxes[i]);
	for (i = 0; i < boxn; i++) add_box(P, Z.z().boxes[i]);
	for (i = hend.boxn - 1; i >= 0; i--) add_box(P, (hend).boxes[i]);
	if (et == (5 << 1)) ps = routesplines(P, pn);
	else ps = routepolylines(P, pn);
	if (pn[0] == 0) return;
    }
    clip_and_install(e, aghead(e), ps, pn[0], Z.z().sinfo);
} finally {
LEAVING("w8ptjibydq995d2lexg85mku","make_flat_labeled_edge");
}
}




//3 d97ga7gm34rs6r0jo494nhhuo
// static void make_flat_bottom_edges(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int  	ind, int cnt, edge_t* e, int splines) 
public static Object make_flat_bottom_edges(Object... arg) {
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




//3 6yr3jfkljl5w0z6dv354ryx63
// static void make_flat_edge(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int ind, int cnt, int et) 
public static void make_flat_edge(ST_Agraph_s g, ST_spline_info_t sp, ST_path P, ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, int et) {
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
    ST_pointf.Array ps;
    final ST_pathend_t tend = new ST_pathend_t(), hend = new ST_pathend_t();
    fwdedge.out.base.setPtr("data", fwdedgei);
    /* Get sample edge; normalize to go from left to right */
    e = (ST_Agedge_s) edges.get(ind);
    isAdjacent = ED_adjacent(e);
    if ((ED_tree_index(e) & 32)!=0) {
	MAKEFWDEDGE(fwdedge.out, e);
	e = (ST_Agedge_s) fwdedge.out;
    }
    for (i = 1; i < cnt; i++) {
	if (ED_adjacent(edges.plus(ind+i).getPtr())!=0) {
	    isAdjacent = 1;
	    break;
	}
    }
    /* The lead edge edges[ind] might not have been marked earlier as adjacent,
     * so check them all.
     */
    if (isAdjacent!=0) {
	make_flat_adj_edges (g, P, edges, ind, cnt, e, et);
	return;
    }
    if (ED_label(e)!=null) {  /* edges with labels aren't multi-edges */
	make_flat_labeled_edge (g, sp, P, e, et);
	return;
    }
    if (et == (1 << 1)) {
	makeSimpleFlat (agtail(e), aghead(e), edges, ind, cnt, et);
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
    	ST_rank_t.Array2 prevr;
	if ((GD_has_labels(g) & (1 << 0))!=0)
	    prevr = GD_rank(g).plus(r-2);
	else
	    prevr = GD_rank(g).plus(r-1);
	vspace = ND_coord(prevr.getPtr().v.get(0)).y - prevr.getPtr().ht1
	   - ND_coord(tn).y - GD_rank(g).get(r).ht2;
    }
    else {
	vspace = GD_ranksep(g);
    }
    stepx = ((double)sp.Multisep) / (cnt+1); 
    stepy = vspace / (cnt+1);
    makeFlatEnd (g, sp, P, tn, e, tend, true);
    makeFlatEnd (g, sp, P, hn, e, hend, false);
    for (i = 0; i < cnt; i++) {
	int boxn;
	final ST_boxf b = new ST_boxf();
	e = (ST_Agedge_s) edges.plus(ind + i).getPtr();
	boxn = 0;
	b.___((tend).boxes[tend.boxn - 1]);
 	((Z.z().boxes[boxn])).LL.x = b.LL.x; 
	((Z.z().boxes[boxn])).LL.y = b.UR.y; 
	((Z.z().boxes[boxn])).UR.x = b.UR.x + (i + 1) * stepx;
	((Z.z().boxes[boxn])).UR.y = b.UR.y + (i + 1) * stepy;
	boxn++;
	((Z.z().boxes[boxn])).LL.x = ((ST_boxf)((tend).boxes[tend.boxn - 1])).LL.x; 
	((Z.z().boxes[boxn])).LL.y = (Z.z().boxes[boxn-1]).UR.y;
	((Z.z().boxes[boxn])).UR.x = ((ST_boxf)((hend).boxes[hend.boxn - 1])).UR.x;
	((Z.z().boxes[boxn])).UR.y = ((Z.z().boxes[boxn])).LL.y + stepy;
	boxn++;
	b.___((hend).boxes[hend.boxn - 1]);
	((Z.z().boxes[boxn])).UR.x = b.UR.x;
	((Z.z().boxes[boxn])).LL.y = b.UR.y;
	((Z.z().boxes[boxn])).LL.x = b.LL.x - (i + 1) * stepx;
	((Z.z().boxes[boxn])).UR.y = (Z.z().boxes[boxn-1]).LL.y;
	boxn++;
	for (j = 0; j < tend.boxn; j++) add_box(P, (tend).boxes[j]);
	for (j = 0; j < boxn; j++) add_box(P, Z.z().boxes[j]);
	for (j = hend.boxn - 1; j >= 0; j--) add_box(P, (hend).boxes[j]);
	if (et == (5 << 1)) ps = routesplines(P, pn);
	else ps = routepolylines(P, pn);
	if (pn[0] == 0)
	    return;
	clip_and_install(e, aghead(e), ps, pn[0], Z.z().sinfo);
	P.nbox = 0;
    }
} finally {
LEAVING("6yr3jfkljl5w0z6dv354ryx63","make_flat_edge");
}
}




//3 78oy7e2xm3t4de66du11ej05j
// static int leftOf (pointf p1, pointf p2, pointf p3) 
public static Object leftOf(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("iqhj2moads3aafqx5xa8j44g"); // leftOf (pointf p1, pointf p2, pointf p3)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5azgw3jpyk2yccpp3p4s3q817"); //     int d;
UNSUPPORTED("42n6sq1mbcvm3tugypozccbua"); //     d = ((p1.y - p2.y) * (p3.x - p2.x)) -
UNSUPPORTED("7y9r79gtx363mln6v9yaisvz6"); //         ((p3.y - p2.y) * (p1.x - p2.x));
UNSUPPORTED("8y4f8e0mnow6139qoguzsxojx"); //     return (d > 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2n9bpvx34fnukqu1f9u4v7v6n
// static int  makeLineEdge(graph_t* g, edge_t* fe, pointf* points, node_t** hp) 
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




//private static __ptr__ pointfs;
//private static __ptr__ pointfs2;
//private static int numpts;
//private static int numpts2;
//3 30wfq1dby4t07hft9io52nq6z
// static void make_regular_edge(graph_t* g, spline_info_t* sp, path * P, edge_t ** edges, int ind, int cnt, int et) 
public static void make_regular_edge(ST_Agraph_s g, ST_spline_info_t sp, ST_path P, ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, int et) {
ENTERING("30wfq1dby4t07hft9io52nq6z","make_regular_edge");
try {
    ST_Agnode_s tn, hn = null;
    final ST_Agedgeinfo_t fwdedgeai = new ST_Agedgeinfo_t(), fwdedgebi = new ST_Agedgeinfo_t(), fwdedgei = new ST_Agedgeinfo_t();
    final ST_Agedgepair_s fwdedgea = new ST_Agedgepair_s(), fwdedgeb = new ST_Agedgepair_s(), fwdedge = new ST_Agedgepair_s();
    ST_Agedge_s e, fe, le, segfirst;
    ST_pointf.Array ps = null;
    final ST_pathend_t tend = new ST_pathend_t(), hend = new ST_pathend_t();
    final ST_boxf b = new ST_boxf();
    int boxn, sl, si, i, j, dx, hackflag, longedge;
    boolean smode;
    int pn[] = new int[] {0};
    int pointn[] = new int[] {0};
    fwdedgea.out.base.setPtr("data", fwdedgeai);
    fwdedgeb.out.base.setPtr("data", fwdedgebi);
    fwdedge.out.base.setPtr("data", fwdedgei);
    if (N(Z.z().pointfs)) {
	Z.z().pointfs = new ST_pointf.Array(2000);
   	Z.z().pointfs2 = new ST_pointf.Array(2000);
	Z.z().numpts = 2000;
	Z.z().numpts2 = 2000;
    }
    sl = 0;
    e = (ST_Agedge_s) edges.get(ind);
    hackflag = 0;
    if (ABS(ND_rank(agtail(e)) - ND_rank(aghead(e))) > 1) {
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
	if ((ED_tree_index(e) & 32)!=0) {
	    MAKEFWDEDGE(fwdedgea.out, e);
	    e = fwdedgea.out;
	}
    }
    fe = e;
    /* compute the spline points for the edge */
    if ((et == (1 << 1)) && (pointn[0] = makeLineEdge (g, fe, Z.z().pointfs, hn.unsupported()))!=0) {
    }
    else {
	boolean splines = (et == (5 << 1));
	boxn = 0;
	pointn[0] = 0;
	segfirst = e;
	tn = agtail(e);
	hn = aghead(e);
	b.___(maximal_bbox(g, sp, tn, null, e));
	tend.nb.___(b);
	beginpath(P, e, 1, tend, spline_merge(tn));
	b.UR.y = 
			(tend).boxes[tend.boxn - 1].UR.y;
	b.LL.y = 
			(tend).boxes[tend.boxn - 1].LL.y;
	b.___(makeregularend(b, (1<<0),
	    	   ND_coord(tn).y - GD_rank(g).get(ND_rank(tn)).ht1));
	if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
	{
	    tend.boxes[tend.boxn].___(b);
	    tend.setInt("boxn", tend.boxn + 1);
	}
	longedge = 0;
	smode = false; si = -1;
	while (ND_node_type(hn) == 1 && N(((Boolean)Z.z().sinfo.splineMerge.exe(hn)).booleanValue())) {
	    longedge = 1;
	    Z.z().boxes[boxn++].___(rank_box(sp, g, ND_rank(tn)));
	    if (N(smode)
	        && ((sl = straight_len(hn)) >=
	    	((GD_has_labels(g) & (1 << 0))!=0 ? 4 + 1 : 2 + 1))) {
	        smode = NOT(false);
	        si = 1; sl -= 2;
	    }
	    if (N(smode) || si > 0) {
	        si--;
	        Z.z().boxes[boxn++].___(maximal_bbox(g, sp, hn, e, (ST_Agedge_s) ND_out(hn).getFromList(0)));
	        e = (ST_Agedge_s) ND_out(hn).getFromList(0);
	        tn = agtail(e);
	        hn = aghead(e);
	        continue;
	    }
	    hend.setStruct("nb", maximal_bbox(g, sp, hn, e, (ST_Agedge_s) ND_out(hn).getFromList(0)));
	    endpath(P, e, 1, hend, spline_merge(aghead(e)));
	    b.___(makeregularend((hend).boxes[hend.boxn - 1], (1<<2),
	    	       ND_coord(hn).y + GD_rank(g).get(ND_rank(hn)).ht2));
	    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("1crhubfzekx1qi2ti9ajqsfoc"); // 	        hend.boxes[hend.boxn++] = b;
	    P.end.setDouble("theta", M_PI / 2);
	    P.end.constrained= NOTI(false);
	    completeregularpath(P, segfirst, e, tend, hend, Z.z().boxes, boxn, 1);
	    if (splines) ps = routesplines(P, pn);
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
	    if (pointn[0] + pn[0] > Z.z().numpts) {
                /* This should be enough to include 3 extra points added by
                 * straight_path below.
                 */
UNSUPPORTED("k37sqlxbjikqg4xdonnvefo3"); // 		numpts = 2*(pointn+pn); 
UNSUPPORTED("8kbxhk7qirj3tr7hn1ukwar3h"); // 		pointfs = RALLOC(numpts, pointfs, pointf);
	    }
	    for (i = 0; i < pn[0]; i++) {
		Z.z().pointfs.plus(pointn[0]++).setStruct(ps.plus(i).getStruct());
	    }
	    e = straight_path((ST_Agedge_s)ND_out(hn).getFromList(0), sl, Z.z().pointfs, pointn);
	    recover_slack(segfirst, P);
	    segfirst = e;
	    tn = agtail(e);
	    hn = aghead(e);
	    boxn = 0;
	    tend.setStruct("nb", maximal_bbox(g, sp, tn, (ST_Agedge_s) ND_in(tn).getFromList(0), e));
	    beginpath(P, e, 1, tend, spline_merge(tn));
	    b.___(makeregularend((tend).boxes[tend.boxn - 1], (1<<0),
	    	       ND_coord(tn).y - GD_rank(g).get(ND_rank(tn)).ht1));
	    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
UNSUPPORTED("cjx6tldge3otk1pk6ks1pkn2w"); // 	        tend.boxes[tend.boxn++] = b;
	    P.start.setDouble("theta", -M_PI / 2);
	    P.start.constrained= NOTI(false);
	    smode = false;
	}
	Z.z().boxes[boxn++].___(rank_box(sp, g, ND_rank(tn)));
	b.___(maximal_bbox(g, sp, hn, e, null));
	hend.nb.___(b);
	endpath(P, (ST_Agedge_s) (hackflag!=0 ? fwdedgeb.out : e), 1, hend, spline_merge(aghead(e)));
	b.UR.y = (hend).boxes[hend.boxn - 1].UR.y;
	b.LL.y = (hend).boxes[hend.boxn - 1].LL.y;
	b.___(makeregularend(b, (1<<2),
	    	   ND_coord(hn).y + GD_rank(g).get(ND_rank(hn)).ht2));
	if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
	    {
	    hend.boxes[hend.boxn].___(b);
	    hend.setInt("boxn", hend.boxn+1);
	    }
	completeregularpath(P, segfirst, e, tend, hend, Z.z().boxes, boxn,
	    		longedge);
	if (splines) ps = routesplines(P, pn);
	else ps = routepolylines (P, pn);
	if ((et == (1 << 1)) && (pn[0] > 4)) {
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
	if (pointn[0] + pn[0] > Z.z().numpts) {
UNSUPPORTED("c6ux5effs02grz7xh3k8ernda"); // 	    numpts = 2*(pointn+pn); 
UNSUPPORTED("bedaqcn9h03q6ia6zbezuee1m"); // 	    pointfs = RALLOC(numpts, pointfs, pointf);
	}
	for (i = 0; i < pn[0]; i++) {
	    Z.z().pointfs.plus(pointn[0]).setStruct(ps.plus(i).getStruct());
	    pointn[0]++;
	}
	recover_slack(segfirst, P);
	hn = hackflag!=0 ? aghead(fwdedgeb.out) : aghead(e);
    }
    /* make copies of the spline points, one per multi-edge */
    if (cnt == 1) {
	clip_and_install(fe, hn, Z.z().pointfs, pointn[0], Z.z().sinfo);
	return;
    }
    dx = sp.Multisep * (cnt - 1) / 2;
    for (i = 1; i < pointn[0] - 1; i++)
	Z.z().pointfs.plus(i).setDouble("x", Z.z().pointfs.get(i).x - dx);
    if (Z.z().numpts > Z.z().numpts2) {
UNSUPPORTED("9ubr4m7bdv5f5ldk2ta6yw3up"); // 	numpts2 = numpts; 
UNSUPPORTED("8qwp7ddy5ztgam63fzfjmu890"); // 	pointfs2 = RALLOC(numpts2, pointfs2, pointf);
    }
    for (i = 0; i < pointn[0]; i++)
	Z.z().pointfs2.plus(i).setStruct(Z.z().pointfs.plus(i).getStruct());
    clip_and_install(fe, hn, Z.z().pointfs2, pointn[0], Z.z().sinfo);
    for (j = 1; j < cnt; j++) {
	e = (ST_Agedge_s) edges.plus(ind + j).getPtr();
	if ((ED_tree_index(e) & 32)!=0) {
	    MAKEFWDEDGE(fwdedge.out, e);
	    e = (ST_Agedge_s) fwdedge.out;
	}
	for (i = 1; i < pointn[0] - 1; i++)
	    Z.z().pointfs.plus(i).setDouble("x", Z.z().pointfs.get(i).x + sp.Multisep);
	for (i = 0; i < pointn[0]; i++)
	    Z.z().pointfs2.plus(i).setStruct(Z.z().pointfs.plus(i).getStruct());
	clip_and_install(e, aghead(e), Z.z().pointfs2, pointn[0], Z.z().sinfo);
    }
} finally {
LEAVING("30wfq1dby4t07hft9io52nq6z","make_regular_edge");
}
}




//3 va61hggynvb6z6j34w7otmab
// static void completeregularpath(path * P, edge_t * first, edge_t * last, 		    pathend_t * tendp, pathend_t * hendp, boxf * boxes, 		    int boxn, int flag) 
public static void completeregularpath(ST_path P, ST_Agedge_s first, ST_Agedge_s last, ST_pathend_t tendp, ST_pathend_t hendp, ST_boxf[] boxes, int boxn, int flag) {
ENTERING("va61hggynvb6z6j34w7otmab","completeregularpath");
try {
    ST_Agedge_s uleft, uright, lleft, lright;
    int i, fb, lb;
    ST_splines spl;
    ST_pointf.Array pp;
    int pn;
    fb = lb = -1;
    uleft = uright = null;
    uleft = top_bound(first, -1);
    uright = top_bound(first, 1);
    if (uleft!=null) {
	if (N(spl = getsplinepoints(uleft))) return;
	pp = ((ST_bezier)spl.list.get(0).getPtr()).list;
       	pn = spl.list.get(0).size;
    }
    if (uright!=null) {
	if (N(spl = getsplinepoints(uright))) return;
	pp = ((ST_bezier)spl.list.get(0).getPtr()).list;
       	pn = spl.list.get(0).size;
    }
    lleft = lright = null;
    lleft = bot_bound(last, -1);
    lright = bot_bound(last, 1);
    if (lleft!=null) {
	if (N(spl = getsplinepoints(lleft))) return;
	pp = ((ST_bezier)spl.list.plus(spl.size - 1).getPtr().getPtr()).list;
       	pn = spl.list.plus(spl.size - 1).getPtr().size;
    }
    if (lright!=null) {
	if (N(spl = getsplinepoints(lright))) return;
	pp = ((ST_bezier)spl.list.plus(spl.size - 1).getPtr().getPtr()).list;
       	pn = spl.list.plus(spl.size - 1).getPtr().size;
    }
    for (i = 0; i < tendp.boxn; i++)
	add_box(P, (tendp).boxes[i]);
    fb = P.nbox + 1;
    lb = fb + boxn - 3;
    for (i = 0; i < boxn; i++)
	add_box(P, boxes[i]);
    for (i = hendp.boxn - 1; i >= 0; i--)
	add_box(P, (hendp).boxes[i]);
    adjustregularpath(P, fb, lb);
} finally {
LEAVING("va61hggynvb6z6j34w7otmab","completeregularpath");
}
}




//3 3wwhczhpkcnflwr1l9wcga7tq
// static boxf makeregularend(boxf b, int side, double y) 
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
		bp1.LL.setDouble("x", x - 8);
		bp1.UR.setDouble("x", x + 8);
	    }
	} else {
	    if (bp1.LL.x + 16 > bp1.UR.x) {
		x = (int)((bp1.LL.x + bp1.UR.x) / 2);
		bp1.LL.setDouble("x", x - 8);
		bp1.UR.setDouble("x", x + 8);
	    }
	}
    }
    for (i = 0; i < P.nbox - 1; i++) {
	bp1 = (ST_boxf) P.boxes[i];
	bp2 = (ST_boxf) P.boxes[i+1];
	if (i >= fb && i <= lb && (i - fb) % 2 == 0) {
	    if (bp1.LL.x + 16 > bp2.UR.x)
		bp2.UR.setDouble("x", bp1.LL.x + 16);
	    if (bp1.UR.x - 16 < bp2.LL.x)
		bp2.LL.setDouble("x", bp1.UR.x - 16);
	} else if (i + 1 >= fb && i < lb && (i + 1 - fb) % 2 == 0) {
	    if (bp1.LL.x + 16 > bp2.UR.x)
		bp1.LL.setDouble("x", bp2.UR.x - 16);
	    if (bp1.UR.x - 16 < bp2.LL.x)
		bp1.UR.setDouble("x", bp2.LL.x + 16);
	} 
    }
} finally {
LEAVING("88xrlzjovkxcnay9b2y5zyiid","adjustregularpath");
}
}




//3 bajn5vx0isu427n6dh131b985
// static boxf rank_box(spline_info_t* sp, graph_t * g, int r) 
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
	left0 = (ST_Agnode_s) GD_rank(g).get(r).v.get(0);
	/* right0 = GD_rank(g)[r].v[GD_rank(g)[r].n - 1]; */
	left1 = (ST_Agnode_s) GD_rank(g).get(r + 1).v.get(0);
	/* right1 = GD_rank(g)[r + 1].v[GD_rank(g)[r + 1].n - 1]; */
	b.LL.x = sp.LeftBound;
	b.LL.y = ND_coord(left1).y + GD_rank(g).get(r + 1).ht2;
	b.UR.x = sp.RightBound;
	b.UR.y = ND_coord(left0).y - GD_rank(g).get(r).ht1;
	sp.Rank_box[r].setStruct(b);
    }
    return b;
} finally {
LEAVING("bajn5vx0isu427n6dh131b985","rank_box");
}
}




//3 6qwcnugx2ytjrvi5rgxzyzg5i
// static int straight_len(node_t * n) 
public static int straight_len(ST_Agnode_s n) {
ENTERING("6qwcnugx2ytjrvi5rgxzyzg5i","straight_len");
try {
    int cnt = 0;
    ST_Agnode_s v;
    v = n;
    while (true) {
	v = (ST_Agnode_s) aghead(ND_out(v).getFromList(0));
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
public static ST_Agedge_s straight_path(ST_Agedge_s e, int cnt, __ptr__ plist, int np[]) {
ENTERING("15pgjjuil2c1rjldu29j07gbz","straight_path");
try {
    int n = np[0];
    ST_Agedge_s f = e;
    while ((cnt--)!=0)
	f = (ST_Agedge_s) ND_out(aghead(f)).getFromList(0);
    plist.plus(np[0]++).setStruct(plist.plus(n - 1).getStruct());
    plist.plus(np[0]++).setStruct(plist.plus(n - 1).getStruct());
    plist.plus(np[0]).setStruct(ND_coord(agtail(f)));  /* will be overwritten by next spline */
    return f;
} finally {
LEAVING("15pgjjuil2c1rjldu29j07gbz","straight_path");
}
}




//3 4ilkzqtegd5uffawb4qcjthu1
// static void recover_slack(edge_t * e, path * p) 
public static void recover_slack(ST_Agedge_s e, ST_path p) {
ENTERING("4ilkzqtegd5uffawb4qcjthu1","recover_slack");
try {
    int b;
    ST_Agnode_s vn;
    b = 0;			/* skip first rank box */
    for (vn = aghead(e);
	 ND_node_type(vn) == 1 && N(((Boolean)Z.z().sinfo.splineMerge.exe(vn)).booleanValue());
	 vn = aghead(ND_out(vn).getFromList(0))) {
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
public static void resize_vn(ST_Agnode_s vn, int lx, int cx, int rx) {
ENTERING("3vmg1q1r0eb14etvjdk4cukpd","resize_vn");
try {
    ND_coord(vn).setDouble("x", cx);
    ND_lw(vn, cx - lx);
    ND_rw(vn, rx - cx);
} finally {
LEAVING("3vmg1q1r0eb14etvjdk4cukpd","resize_vn");
}
}




//3 9t0v5wicmjuc3ij9hko6iawle
// static edge_t *top_bound(edge_t * e, int side) 
public static ST_Agedge_s top_bound(ST_Agedge_s e, int side) {
ENTERING("9t0v5wicmjuc3ij9hko6iawle","top_bound");
try {
    ST_Agedge_s f, ans = null;
    int i;
    for (i = 0; (f = (ST_Agedge_s) ND_out(agtail(e)).getFromList(i))!=null; i++) {
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
public static ST_Agedge_s bot_bound(ST_Agedge_s e, int side) {
ENTERING("9fsg0uiyhtrayd4mimmc0i25e","bot_bound");
try {
    ST_Agedge_s f, ans = null;
    int i;
    for (i = 0; (f = (ST_Agedge_s) ND_in(aghead(e)).getFromList(i))!=null; i++) {
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
public static ST_Agraph_s cl_bound(ST_Agraph_s g,  ST_Agnode_s n, ST_Agnode_s adj) {
ENTERING("dzvvmxkya868w5x78lkvchigk","cl_bound");
try {
    ST_Agraph_s rv, cl, tcl, hcl;
    ST_Agedge_s orig;
    rv = null;
    if (ND_node_type(n) == 0)
	tcl = hcl = ND_clust(n);
    else {
	orig = ED_to_orig(ND_out(n).getFromList(0));
	tcl = ND_clust(agtail(orig));
	hcl = ND_clust(aghead(orig));
    }
    if (ND_node_type(adj) == 0) {
	cl = ( EQ(ND_clust(adj), g) ? null:ND_clust(adj));
	if (cl!=null && NEQ(cl, tcl) && NEQ(cl, hcl))
	    rv = cl;
    } else {
	orig = ED_to_orig(ND_out(adj).getFromList(0));
	cl = ( EQ(ND_clust(agtail(orig)), g) ? null:ND_clust(agtail(orig)));
	if (cl!=null && NEQ(cl, tcl) && NEQ(cl, hcl) && cl_vninside(cl, adj))
	    rv = cl;
	else {
	    cl = ( EQ(ND_clust(aghead(orig)), g) ? null:ND_clust(aghead(orig)));
	    if (cl!=null && NEQ(cl, tcl) && NEQ(cl, hcl) && cl_vninside(cl, adj))
		rv = cl;
	}
    }
    return rv;
} finally {
LEAVING("dzvvmxkya868w5x78lkvchigk","cl_bound");
}
}




//3 6qwgl36ugfnduc5x59ohuewv1
// static boxf maximal_bbox(graph_t* g, spline_info_t* sp, node_t* vn, edge_t* ie, edge_t* oe) 
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
    b = (double)(ND_coord(vn).x - ND_lw(vn) - 4);
    if ((left = neighbor(g, vn, ie, oe, -1))!=null) {
	if ((left_cl = cl_bound(g, vn, left))!=null)
	    nb = GD_bb(left_cl).UR.x + (double)(sp.Splinesep);
	else {
	    nb = (double)(ND_coord(left).x + ND_mval(left));
	    if (ND_node_type(left) == 0)
		nb += GD_nodesep(g) / 2.;
	    else
		nb += (double)(sp.Splinesep);
	}
	if (nb < b)
	    b = nb;
	rv.LL.x = ROUND(b);
    } else
	rv.LL.x = MIN(ROUND(b), sp.LeftBound);
    /* we have to leave room for our own label! */
    if ((ND_node_type(vn) == 1) && (ND_label(vn)!=null))
	b = (double)(ND_coord(vn).x + 10);
    else
	b = (double)(ND_coord(vn).x + ND_rw(vn) + 4);
    if ((right = neighbor(g, vn, ie, oe, 1))!=null) {
	if ((right_cl = cl_bound(g, vn, right))!=null)
	    nb = GD_bb(right_cl).LL.x - (double)(sp.Splinesep);
	else {
	    nb = ND_coord(right).x - ND_lw(right);
	    if (ND_node_type(right) == 0)
		nb -= GD_nodesep(g) / 2.;
	    else
		nb -= (double)(sp.Splinesep);
	}
	if (nb > b)
	    b = nb;
	rv.UR.x = ROUND(b);
    } else
	rv.UR.x = MAX(ROUND(b), sp.RightBound);
    if ((ND_node_type(vn) == 1) && (ND_label(vn)!=null)) {
	rv.UR.x = rv.UR.x - ND_rw(vn);
	if (rv.UR.x < rv.LL.x) rv.UR.x = ND_coord(vn).x;
    }
    rv.LL.y = ND_coord(vn).y - GD_rank(g).get(ND_rank(vn)).ht1;
    rv.UR.y = ND_coord(vn).y + GD_rank(g).get(ND_rank(vn)).ht2;
    return rv;
} finally {
LEAVING("6qwgl36ugfnduc5x59ohuewv1","maximal_bbox");
}
}




//3 18pm6r3xcy90f0xi5hpm9jdhk
// static node_t * neighbor(graph_t* g, node_t *vn, edge_t *ie, edge_t *oe, int dir) 
public static ST_Agnode_s neighbor(ST_Agraph_s g, ST_Agnode_s vn, ST_Agedge_s ie, ST_Agedge_s oe, int dir) {
ENTERING("18pm6r3xcy90f0xi5hpm9jdhk","neighbor");
try {
    int i;
    ST_Agnode_s n, rv = null;
    ST_rank_t.Array2 rank = GD_rank(g).plus(ND_rank(vn));
    for (i = ND_order(vn) + dir; ((i >= 0) && (i < rank.getPtr().n)); i += dir) {
	n = (ST_Agnode_s) rank.getPtr().v.get(i);
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
	e0 = (ST_Agedge_s) ND_out(n0).getFromList(0);
	for (cnt = 0; cnt < 2; cnt++) {
	    if (EQ(na = aghead(e0), nb = aghead(e1)))
		break;
	    if (order != (ND_order(na) > ND_order(nb)))
		return NOT(false);
	    if ((ND_out(na).size != 1) || (ND_node_type(na) == 0))
		break;
	    e0 = (ST_Agedge_s) ND_out(na).getFromList(0);
	    if ((ND_out(nb).size != 1) || (ND_node_type(nb) == 0))
		break;
	    e1 = (ST_Agedge_s) ND_out(nb).getFromList(0);
	}
    }
    e1 = ie1;
    if (ND_in(n0).size == 1 && e1!=null) {
	e0 = (ST_Agedge_s) ND_in(n0).getFromList(0);
	for (cnt = 0; cnt < 2; cnt++) {
	    if (EQ(na = agtail(e0), nb = agtail(e1)))
		break;
	    if (order != (ND_order(na) > ND_order(nb)))
		return NOT(false);
	    if ((ND_in(na).size != 1) || (ND_node_type(na) == 0))
		break;
	    e0 = (ST_Agedge_s) ND_in(na).getFromList(0);
	    if ((ND_in(nb).size != 1) || (ND_node_type(nb) == 0))
		break;
	    e1 = (ST_Agedge_s) ND_in(nb).getFromList(0);
	}
    }
    return false;
} finally {
LEAVING("f4q0oqe165s9pl5k0th5noeyv","pathscross");
}
}


}
