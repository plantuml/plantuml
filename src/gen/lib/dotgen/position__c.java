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
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.graph__c.agnnodes;
import static gen.lib.cgraph.obj__c.agcontains;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.ns__c.rank;
import static gen.lib.common.splines__c.selfRightSpace;
import static gen.lib.common.utils__c.late_int;
import static gen.lib.dotgen.cluster__c.mark_lowclusters;
import static gen.lib.dotgen.conc__c.dot_concentrate;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.fast_edge;
import static gen.lib.dotgen.fastgr__c.find_fast_edge;
import static gen.lib.dotgen.fastgr__c.virtual_node;
import static gen.lib.dotgen.fastgr__c.zapinlist;
import static gen.lib.dotgen.flat__c.flat_edges;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.USHRT_MAX;
import static smetana.core.JUtils.atof;
import static smetana.core.JUtils.enumAsInt;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ALLOC_allocated_ST_Agnode_s;
import static smetana.core.Macro.ED_dist;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_border;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_drawing;
import static smetana.core.Macro.GD_exact_ranksep;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_ht1;
import static smetana.core.Macro.GD_ht2;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.GD_ln;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_nodesep;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.GD_rn;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_alg;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_inleaf;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_mval;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_outleaf;
import static smetana.core.Macro.ND_prev;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_save_in;
import static smetana.core.Macro.ND_save_out;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.ROUND;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.aghead;
import static smetana.core.Macro.agtail;
import static smetana.core.Macro.alloc_elist;
import static smetana.core.Macro.free_list;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agedgepair_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_aspect_t;
import h.ST_point;
import h.ST_pointf;
import h.ST_rank_t;
import h.ratio_t;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;

public class position__c {
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




//3 6knxbdrmgk6294aw61s2lpvvf
// static double largeMinlen (double l) 
public static double largeMinlen(double l) {
ENTERING("6knxbdrmgk6294aw61s2lpvvf","largeMinlen");
try {
 UNSUPPORTED("lt6cippjix5bbvyhkcpl8g7g"); // static double
UNSUPPORTED("e2f0xhw6om2fpgt48xyjjg3i"); // largeMinlen (double l)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dad2o3vzemegi5fywxl7hcezk"); //     agerr (AGERR, "Edge length %f larger than maximum %u allowed.\nCheck for overwide node(s).\n", l, USHRT_MAX); 
UNSUPPORTED("dlasv24dnuygpwagcamhyg15w"); //     return (double)USHRT_MAX;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("6knxbdrmgk6294aw61s2lpvvf","largeMinlen");
}
}




//3 ccowbxkwmrj75tojopmhcmubx
// static void connectGraph (graph_t* g) 
public static void connectGraph(ST_Agraph_s g) {
ENTERING("ccowbxkwmrj75tojopmhcmubx","connectGraph");
try {
    int i, j, r, found;
    ST_Agnode_s tp;
    ST_Agnode_s hp;
    ST_Agnode_s sn;
    ST_Agedge_s e;
    ST_rank_t.Array2 rp;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	rp = GD_rank(g).plus(r);
	found =0;
        tp = null;
	for (i = 0; i < rp.getPtr().n; i++) {
	    tp = (ST_Agnode_s) rp.getPtr().v.get(i);
	    if (ND_save_out(tp).listNotNull()) {
        	for (j = 0; (e = (ST_Agedge_s) ND_save_out(tp).getFromList(j))!=null; j++) {
		    if ((ND_rank(aghead(e)) > r) || (ND_rank(agtail(e)) > r)) {
			found = 1;
			break;
		    }
        	}
		if (found!=0) break;
	    }
	    if (ND_save_in(tp).listNotNull()) {
        	for (j = 0; (e = (ST_Agedge_s) ND_save_in(tp).getFromList(j))!=null; j++) {
		    if ((ND_rank(agtail(e)) > r) || (ND_rank(aghead(e)) > r)) {
			found = 1;
			break;
		    }
        	}
		if (found!=0) break;
	    }
	}
	if (found!=0 || N(tp)) continue;
	tp = (ST_Agnode_s) rp.getPtr().v.get(0);
	if (r < GD_maxrank(g)) hp = (ST_Agnode_s) rp.get(1).v.get(0);
	else hp = (ST_Agnode_s) rp.plus(-1).getPtr().v.get(0);
	//assert (hp);
	sn = virtual_node(g);
	ND_node_type(sn, 2);
	make_aux_edge(sn, tp, 0, 0);
	make_aux_edge(sn, hp, 0, 0);
	ND_rank(sn, MIN(ND_rank(tp), ND_rank(hp)));
    }
} finally {
LEAVING("ccowbxkwmrj75tojopmhcmubx","connectGraph");
}
}




//3 33snzyd9z0loienur06dnily9
// void dot_position(graph_t * g, aspect_t* asp) 
public static void dot_position(ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("33snzyd9z0loienur06dnily9","dot_position");
try {
    if (GD_nlist(g) == null)
	return;			/* ignore empty graph */
    mark_lowclusters(g);	/* we could remove from splines.c now */
    set_ycoords(g);
    if (Z.z().Concentrate)
	dot_concentrate(g);
    expand_leaves(g);
    if (flat_edges(g)!=0)
	set_ycoords(g);
    create_aux_edges(g);
    if (rank(g, 2, nsiter2(g))!=0) { /* LR balance == 2 */
	connectGraph (g);
	//assert(rank(g, 2, nsiter2(g)) == 0);
    }
    set_xcoords(g);
    set_aspect(g, asp);
    remove_aux_edges(g);	/* must come after set_aspect since we now
				 * use GD_ln and GD_rn for bbox width.
				 */
} finally {
LEAVING("33snzyd9z0loienur06dnily9","dot_position");
}
}




//3 90vn63m6v0w9fn9a2dgfxxx3h
// static int nsiter2(graph_t * g) 
public static int nsiter2(ST_Agraph_s g) {
ENTERING("90vn63m6v0w9fn9a2dgfxxx3h","nsiter2");
try {
    int maxiter = INT_MAX;
    CString s;
    if ((s = agget(g, new CString("nslimit")))!=null)
	maxiter = (int)(atof(s) * agnnodes(g));
    return maxiter;
} finally {
LEAVING("90vn63m6v0w9fn9a2dgfxxx3h","nsiter2");
}
}




//3 5bax8ut6nnk4pr7yxdumk9chl
// static int go(node_t * u, node_t * v) 
public static boolean go(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("5bax8ut6nnk4pr7yxdumk9chl","go");
try {
    int i;
    ST_Agedge_s e;
    if (EQ(u, v))
	return NOT(false);
    for (i = 0; (e = (ST_Agedge_s) ND_out(u).getFromList(i))!=null; i++) {
	if (go(aghead(e), v))
	    return NOT(false);
    }
    return false;
} finally {
LEAVING("5bax8ut6nnk4pr7yxdumk9chl","go");
}
}




//3 9xz8numztzj4qsq85pziahv1k
// static int canreach(node_t * u, node_t * v) 
public static boolean canreach(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("9xz8numztzj4qsq85pziahv1k","canreach");
try {
    return go(u, v);
} finally {
LEAVING("9xz8numztzj4qsq85pziahv1k","canreach");
}
}




//3 4cvgiatny97ou6mhqoq6aqwek
// edge_t *make_aux_edge(node_t * u, node_t * v, double len, int wt) 
public static ST_Agedge_s make_aux_edge(ST_Agnode_s u, ST_Agnode_s v, double len, int wt) {
ENTERING("4cvgiatny97ou6mhqoq6aqwek","make_aux_edge");
try {
    ST_Agedge_s e;
    ST_Agedgepair_s e2 = new ST_Agedgepair_s();
    AGTYPE(e2.in, AGINEDGE);
    AGTYPE(e2.out, AGOUTEDGE);
    e2.out.base.setPtr("data", new ST_Agedgeinfo_t());
    e = (ST_Agedge_s) e2.out;
    agtail(e, u);
    aghead(e, v);
    if (len > USHRT_MAX)
	len = largeMinlen (len);
    ED_minlen(e, ROUND(len));
    ED_weight(e, wt);
    fast_edge(e);
    return e;
} finally {
LEAVING("4cvgiatny97ou6mhqoq6aqwek","make_aux_edge");
}
}




//3 53fvij7oun7aezlb7x66vzuyb
// static void allocate_aux_edges(graph_t * g) 
public static void allocate_aux_edges(ST_Agraph_s g) {
ENTERING("53fvij7oun7aezlb7x66vzuyb","allocate_aux_edges");
try {
    int i, j, n_in;
    ST_Agnode_s n;
    /* allocate space for aux edge lists */
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	ND_save_in(n, ND_in(n));
	ND_save_out(n, ND_out(n));
	for (i = 0; ND_out(n).getFromList(i)!=null; i++);
	for (j = 0; ND_in(n).getFromList(j)!=null; j++);
	n_in = i + j;
	alloc_elist(n_in + 3, ND_in(n));
	alloc_elist(3, ND_out(n));
    }
} finally {
LEAVING("53fvij7oun7aezlb7x66vzuyb","allocate_aux_edges");
}
}




//3 ah28nr6mxpjeosr85bhmzd3si
// static void  make_LR_constraints(graph_t * g) 
public static void make_LR_constraints(ST_Agraph_s g) {
ENTERING("ah28nr6mxpjeosr85bhmzd3si","make_LR_constraints");
try {
    int i, j, k;
    int sw;			/* self width */
    int m0, m1;
    double width;
    int sep[] = new int[2];
    int nodesep;      /* separation between nodes on same rank */
    ST_Agedge_s e, e0, e1, ff;
    ST_Agnode_s u, v, t0, h0;
    ST_rank_t.Array2 rank = GD_rank(g);
    /* Use smaller separation on odd ranks if g has edge labels */
    if ((GD_has_labels(g) & (1 << 0))!=0) {
	sep[0] = GD_nodesep(g);
	sep[1] = 5;
    }
    else {
	sep[1] = sep[0] = GD_nodesep(g);
    }
    /* make edges to constrain left-to-right ordering */
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
	double last;
	ND_rank(rank.get(i).v.get(0), 0);
	last = 0;
	nodesep = sep[i & 1];
	for (j = 0; j < rank.get(i).n; j++) {
	    u = (ST_Agnode_s) rank.get(i).v.get(j);
	    ND_mval(u, ND_rw(u));	/* keep it somewhere safe */
	    if (ND_other(u).size > 0) {	/* compute self size */
		/* FIX: dot assumes all self-edges go to the right. This
                 * is no longer true, though makeSelfEdge still attempts to
                 * put as many as reasonable on the right. The dot code
                 * should be modified to allow a box reflecting the placement
                 * of all self-edges, and use that to reposition the nodes.
                 * Note that this would not only affect left and right
                 * positioning but may also affect interrank spacing.
                 */
		sw = 0;
		for (k = 0; (e = (ST_Agedge_s) ND_other(u).getFromList(k))!=null; k++) {
		    if (EQ(agtail(e), aghead(e))) {
			sw += selfRightSpace (e);
		    }
		}
		ND_rw(u, ND_rw(u) + sw);	/* increment to include self edges */
	    }
	    v = (ST_Agnode_s) rank.get(i).v.plus(j + 1).getPtr();
	    if (v!=null) {
		width = ND_rw(u) + ND_lw(v) + nodesep;
		e0 = make_aux_edge(u, v, width, 0);
		ND_rank(v, (int)(last + width));
		last = (int)(last + width);
	    }
	    /* constraints from labels of flat edges on previous rank */
	    if ((e = (ST_Agedge_s) ND_alg(u))!=null) {
		e0 = (ST_Agedge_s) ND_save_out(u).getFromList(0);
		e1 = (ST_Agedge_s) ND_save_out(u).getFromList(1);
		if (ND_order(aghead(e0)) > ND_order(aghead(e1))) {
		    ff = e0;
		    e0 = e1;
		    e1 = ff;
		}
		m0 = (ED_minlen(e) * GD_nodesep(g)) / 2;
		m1 = m0 + ((int)(ND_rw(aghead(e0)) + ND_lw(agtail(e0))));
		/* these guards are needed because the flat edges
		 * work very poorly with cluster layout */
		if (canreach(agtail(e0), aghead(e0)) == false)
		    make_aux_edge(aghead(e0), agtail(e0), m1,
			ED_weight(e));
		m1 = m0 + ((int)(ND_rw(agtail(e1)) + ND_lw(aghead(e1))));
		if (canreach(aghead(e1), agtail(e1)) == false)
		    make_aux_edge(agtail(e1), aghead(e1), m1,
			ED_weight(e));
	    }
	    /* position flat edge endpoints */
	    for (k = 0; k < ND_flat_out(u).size; k++) {
		e = (ST_Agedge_s) ND_flat_out(u).getFromList(k);
		if (ND_order(agtail(e)) < ND_order(aghead(e))) {
		    t0 = agtail(e);
		    h0 = aghead(e);
		} else {
		    t0 = aghead(e);
		    h0 = agtail(e);
		}
		width = ND_rw(t0) + ND_lw(h0);
		m0 = (int) (ED_minlen(e) * GD_nodesep(g) + width);
		if ((e0 = find_fast_edge(t0, h0))!=null) {
		    /* flat edge between adjacent neighbors 
                     * ED_dist contains the largest label width.
                     */
		    m0 = MAX(m0, (int)(width + GD_nodesep(g) + ROUND(ED_dist(e))));
		    if (m0 > USHRT_MAX)
			m0 = (int) largeMinlen (m0);
		    ED_minlen(e0, MAX(ED_minlen(e0), m0));
		    ED_weight(e0, MAX(ED_weight(e0), ED_weight(e)));
		}
		else if (N(ED_label(e))) {
		    /* unlabeled flat edge between non-neighbors 
		     * ED_minlen(e) is max of ED_minlen of all equivalent 
                     * edges.
                     */
		    make_aux_edge(t0, h0, m0, ED_weight(e));
		}
		/* labeled flat edges between non-neighbors have already
                 * been constrained by the label above. 
                 */ 
	    }
	}
    }
} finally {
LEAVING("ah28nr6mxpjeosr85bhmzd3si","make_LR_constraints");
}
}




//3 6uruo8mutxgcni9fm8jcrw4cr
// static void make_edge_pairs(graph_t * g) 
public static void make_edge_pairs(ST_Agraph_s g) {
ENTERING("6uruo8mutxgcni9fm8jcrw4cr","make_edge_pairs");
try {
    int i, m0, m1;
    ST_Agnode_s n, sn;
    ST_Agedge_s e;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if (ND_save_out(n).listNotNull())
	    for (i = 0; (e = (ST_Agedge_s) ND_save_out(n).getFromList(i))!=null; i++) {
		sn = virtual_node(g);
		ND_node_type(sn, 2);
		m0 = (int)(ED_head_port(e).p.x - ED_tail_port(e).p.x);
		if (m0 > 0)
		    m1 = 0;
		else {
		    m1 = -m0;
		    m0 = 0;
		}
		make_aux_edge(sn, agtail(e), m0 + 1, ED_weight(e));
		make_aux_edge(sn, aghead(e), m1 + 1, ED_weight(e));
		ND_rank(sn,
		    MIN(ND_rank(agtail(e)) - m0 - 1,
			ND_rank(aghead(e)) - m1 - 1));
	    }
    }
} finally {
LEAVING("6uruo8mutxgcni9fm8jcrw4cr","make_edge_pairs");
}
}




//3 79v3omwzni0nm3h05l3onjsbz
// static void contain_clustnodes(graph_t * g) 
public static void contain_clustnodes(ST_Agraph_s g) {
ENTERING("79v3omwzni0nm3h05l3onjsbz","contain_clustnodes");
try {
    int c;
    ST_Agedge_s e;
    if (NEQ(g, dot_root(g))) {
	contain_nodes(g);
	if ((e = find_fast_edge(GD_ln(g),GD_rn(g)))!=null)	/* maybe from lrvn()?*/
	    ED_weight(e, ED_weight(e) + 128);
	else
	    make_aux_edge(GD_ln(g), GD_rn(g), 1, 128);	/* clust compaction edge */
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	contain_clustnodes((ST_Agraph_s) GD_clust(g).get(c).getPtr());
} finally {
LEAVING("79v3omwzni0nm3h05l3onjsbz","contain_clustnodes");
}
}




//3 24yfgklubun581fbfyx62lzsm
// static int vnode_not_related_to(graph_t * g, node_t * v) 
public static boolean vnode_not_related_to(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("24yfgklubun581fbfyx62lzsm","vnode_not_related_to");
try {
    ST_Agedge_s e;
    if (ND_node_type(v) != 1)
	return false;
    for (e = (ST_Agedge_s) ND_save_out(v).getFromList(0); ED_to_orig(e)!=null; e = ED_to_orig(e));
    if (agcontains(g, agtail(e)))
	return false;
    if (agcontains(g, aghead(e)))
	return false;
    return NOT(false);
} finally {
LEAVING("24yfgklubun581fbfyx62lzsm","vnode_not_related_to");
}
}




//3 73cdgjl47ohty2va766evbo4
// static void keepout_othernodes(graph_t * g) 
public static void keepout_othernodes(ST_Agraph_s g) {
ENTERING("73cdgjl47ohty2va766evbo4","keepout_othernodes");
try {
    int i, c, r, margin;
    ST_Agnode_s u, v;
    margin = late_int (g, Z.z().G_margin, 8, 0);
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	if (GD_rank(g).get(r).n == 0)
	    continue;
	v = (ST_Agnode_s) GD_rank(g).get(r).v.get(0);
	if (v == null)
	    continue;
	for (i = ND_order(v) - 1; i >= 0; i--) {
	    u = (ST_Agnode_s) GD_rank(dot_root(g)).get(r).v.get(i);
	    /* can't use "is_a_vnode_of" because elists are swapped */
	    if ((ND_node_type(u) == 0) || vnode_not_related_to(g, u)) {
		make_aux_edge(u, GD_ln(g), margin + ND_rw(u), 0);
		break;
	    }
	}
	for (i = ND_order(v) + GD_rank(g).get(r).n; i < GD_rank(dot_root(g)).get(r).n;
	     i++) {
	    u = (ST_Agnode_s) GD_rank(dot_root(g)).get(r).v.get(i);
	    if ((ND_node_type(u) == 0) || vnode_not_related_to(g, u)) {
		make_aux_edge(GD_rn(g), u, margin + ND_lw(u), 0);
		break;
	    }
	}
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	keepout_othernodes((ST_Agraph_s) GD_clust(g).get(c).getPtr());
} finally {
LEAVING("73cdgjl47ohty2va766evbo4","keepout_othernodes");
}
}




//3 c734mx1638sfqtl7vh7itaxyx
// static void contain_subclust(graph_t * g) 
public static void contain_subclust(ST_Agraph_s g) {
ENTERING("c734mx1638sfqtl7vh7itaxyx","contain_subclust");
try {
    int margin, c;
    ST_Agraph_s subg;
    margin = late_int (g, Z.z().G_margin, 8, 0);
    make_lrvn(g);
    for (c = 1; c <= GD_n_cluster(g); c++) {
	subg = (ST_Agraph_s) GD_clust(g).get(c).getPtr();
	make_lrvn(subg);
	make_aux_edge(GD_ln(g), GD_ln(subg),
		      margin + GD_border(g)[3].x, 0);
	make_aux_edge(GD_rn(subg), GD_rn(g),
		      margin + GD_border(g)[1].x, 0);
	contain_subclust(subg);
    }
} finally {
LEAVING("c734mx1638sfqtl7vh7itaxyx","contain_subclust");
}
}




//3 6oruu1p1b7kxr5moh3kmcmvr3
// static void separate_subclust(graph_t * g) 
public static void separate_subclust(ST_Agraph_s g) {
ENTERING("6oruu1p1b7kxr5moh3kmcmvr3","separate_subclust");
try {
    int i, j, margin;
    ST_Agraph_s low, high;
    ST_Agraph_s left, right;
    margin = late_int (g, Z.z().G_margin, 8, 0);
    for (i = 1; i <= GD_n_cluster(g); i++)
	make_lrvn((ST_Agraph_s) GD_clust(g).get(i).getPtr());
    for (i = 1; i <= GD_n_cluster(g); i++) {
	for (j = i + 1; j <= GD_n_cluster(g); j++) {
	    low = (ST_Agraph_s) GD_clust(g).get(i).getPtr();
	    high = (ST_Agraph_s) GD_clust(g).get(j).getPtr();
	    if (GD_minrank(low) > GD_minrank(high)) {
		ST_Agraph_s temp = low;
		low = high;
		high = temp;
	    }
	    if (GD_maxrank(low) < GD_minrank(high))
		continue;
	    if (ND_order(GD_rank(low).get(GD_minrank(high)).v.get(0))
		< ND_order(GD_rank(high).get(GD_minrank(high)).v.get(0))) {
		left = low;
		right = high;
	    } else {
		left = high;
		right = low;
	    }
	    make_aux_edge(GD_rn(left), GD_ln(right), margin, 0);
	}
	separate_subclust((ST_Agraph_s) GD_clust(g).get(i).getPtr());
    }
} finally {
LEAVING("6oruu1p1b7kxr5moh3kmcmvr3","separate_subclust");
}
}




//3 8f8gs2zivo4pnd3hmtb9g23x4
// static void pos_clusters(graph_t * g) 
public static void pos_clusters(ST_Agraph_s g) {
ENTERING("8f8gs2zivo4pnd3hmtb9g23x4","pos_clusters");
try {
    if (GD_n_cluster(g) > 0) {
	contain_clustnodes(g);
	keepout_othernodes(g);
	contain_subclust(g);
	separate_subclust(g);
    }
} finally {
LEAVING("8f8gs2zivo4pnd3hmtb9g23x4","pos_clusters");
}
}




//3 fywsxto7yvl5wa2dfu7u7jj1
// static void compress_graph(graph_t * g) 
public static void compress_graph(ST_Agraph_s g) {
ENTERING("fywsxto7yvl5wa2dfu7u7jj1","compress_graph");
try {
    double x;
    ST_pointf p = new ST_pointf();
    if (GD_drawing(g).ratio_kind != enumAsInt(ratio_t.class, "R_COMPRESS"))
	return;
UNSUPPORTED("79oeaf0u32si2chjcpas5whjl"); //     p = GD_drawing(g)->size;
UNSUPPORTED("6a2ue1i6kvwvpgapb4z8l27jn"); //     if (p.x * p.y <= 1)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("5f3k9yz6btwxc8r5t8exytqqt"); //     contain_nodes(g);
UNSUPPORTED("4mvbrmj6dfhaz3burnpac7zsx"); //     if (GD_flip(g) == 0)
UNSUPPORTED("dzkztznjq2andjnjzqh8i5tij"); // 	x = p.x;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("ddzjdkqij3y4gq9i3hikpoqvj"); // 	x = p.y;
UNSUPPORTED("e1xij2jh66kyaiikenemo1qza"); //     /* Guard against huge size attribute since max. edge length is USHRT_MAX
UNSUPPORTED("5ilmnsqirjhzn5q6s3f9pkgi3"); //      * A warning might be called for. Also, one could check that the graph
UNSUPPORTED("1nhxkt9jijvhw5gsp2pluh8g8"); //      * already fits GD_drawing(g)->size and return immediately.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("dkqac1chvtsaao23vr43xqs5r"); //     x = MIN(x,USHRT_MAX); 
UNSUPPORTED("5es2j3xrdatvha5uea2wlqcxp"); //     make_aux_edge(GD_ln(g), GD_rn(g), x, 1000);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("fywsxto7yvl5wa2dfu7u7jj1","compress_graph");
}
}




//3 b7y0htx4svbhaqb1a12dihlue
// static void create_aux_edges(graph_t * g) 
public static void create_aux_edges(ST_Agraph_s g) {
ENTERING("b7y0htx4svbhaqb1a12dihlue","create_aux_edges");
try {
    allocate_aux_edges(g);
    make_LR_constraints(g);
    make_edge_pairs(g);
    pos_clusters(g);
    compress_graph(g);
} finally {
LEAVING("b7y0htx4svbhaqb1a12dihlue","create_aux_edges");
}
}




//3 euzeilq92ry8a4tcrij5s52t5
// static void remove_aux_edges(graph_t * g) 
public static void remove_aux_edges(ST_Agraph_s g) {
ENTERING("euzeilq92ry8a4tcrij5s52t5","remove_aux_edges");
try {
    int i;
    ST_Agnode_s n, nnext, nprev;
    ST_Agedge_s e;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).getFromList(i))!=null; i++) {
	    Memory.free(e.base.data);
	    Memory.free(e);
	}
	free_list(ND_out(n));
	free_list(ND_in(n));
	ND_out(n, ND_save_out(n));
	ND_in(n, ND_save_in(n));
    }
    /* cannot be merged with previous loop */
    nprev = null;
    for (n = GD_nlist(g); n!=null; n = nnext) {
	nnext = ND_next(n);
	if (ND_node_type(n) == 2) {
	    if (nprev!=null)
		ND_next(nprev, nnext);
	    else
		GD_nlist(g, nnext);
	    Memory.free(n.base.data);
	    Memory.free(n);
	} else
	    nprev = n;
    }
    ND_prev(GD_nlist(g), null);
} finally {
LEAVING("euzeilq92ry8a4tcrij5s52t5","remove_aux_edges");
}
}




//3 1oobmglea9t819y95xeel37h8
// static void  set_xcoords(graph_t * g) 
public static void set_xcoords(ST_Agraph_s g) {
ENTERING("1oobmglea9t819y95xeel37h8","set_xcoords");
try {
    int i, j;
    ST_Agnode_s v;
    ST_rank_t.Array2 rank = GD_rank(g);
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
	for (j = 0; j < rank.get(i).n; j++) {
	    v = (ST_Agnode_s) rank.get(i).v.get(j);
	    ND_coord(v).x = ND_rank(v);
	    ND_rank(v, i);
	}
    }
} finally {
LEAVING("1oobmglea9t819y95xeel37h8","set_xcoords");
}
}




//3 6mip7s1k9xt9tp6x6nnsbt34p
// static void adjustSimple(graph_t * g, int delta, int margin_total) 
public static Object adjustSimple(Object... arg) {
UNSUPPORTED("6b5zs45snahyh3bzod53vasjx"); // static void adjustSimple(graph_t * g, int delta, int margin_total)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4dxxcgj8emjoyp2vlrcw5xmwl"); //     int r, bottom, deltop, delbottom;
UNSUPPORTED("6i5o0pm41tgebv48wcngsxwqd"); //     graph_t *root = dot_root(g);
UNSUPPORTED("2clvms28da6dp56nhcz1fw6l8"); //     rank_t *rank = GD_rank(root);
UNSUPPORTED("3fkjf35jwcy291suh6fv3lnu"); //     int maxr = GD_maxrank(g);
UNSUPPORTED("be5w8b0upg8swanznmjl6ml56"); //     int minr = GD_minrank(g);
UNSUPPORTED("enzyy8zeeg6bsgsuwm86u5bwb"); //     bottom = (delta+1) / 2;
UNSUPPORTED("9jp6b6z7el15spss761fh4rgx"); //     delbottom = GD_ht1(g) + bottom - (rank[maxr].ht1 - margin_total);
UNSUPPORTED("ckra1b9nceo8q21bwo5iu3rap"); //     if (delbottom > 0) {
UNSUPPORTED("3jotxrk6oermksl3is78mj0e2"); // 	for (r = maxr; r >= minr; r--) {
UNSUPPORTED("b905w55w58349wk9aktevaljm"); // 	    if (rank[r].n > 0)
UNSUPPORTED("bjuzvkc8hhlni54hkaef0j0xa"); // 		ND_coord(rank[r].v[0]).y += delbottom;
UNSUPPORTED("e9yhkch195gia8gdiu6vb4rbn"); //  	}
UNSUPPORTED("bdmop8bk7tvltc7z16xj07kqn"); // 	deltop = GD_ht2(g) + (delta-bottom) + delbottom - (rank[minr].ht2 - margin_total);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("cw3vv20i8np95bafexnuhhpdg"); // 	deltop = GD_ht2(g) + (delta-bottom) - (rank[minr].ht2 - margin_total);
UNSUPPORTED("49prm57gvpenehfkcxz9xuxu4"); //     if (deltop > 0) {
UNSUPPORTED("axexjgvfow9k3x1l6j2my6cd"); // 	for (r = minr-1; r >= GD_minrank(root); r--) {
UNSUPPORTED("b905w55w58349wk9aktevaljm"); // 	    if (rank[r].n > 0)
UNSUPPORTED("1nebaz06hwqknqrqcnwf03z2"); // 		ND_coord(rank[r].v[0]).y += deltop;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7o49wizb9vp55xhz88lycavep"); //     GD_ht2(g) += (delta - bottom);
UNSUPPORTED("4mq1sot7a9bxpobj9602hyyl5"); //     GD_ht1(g) += bottom;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9sv968rni5moio662r9kjzai3
// static void adjustRanks(graph_t * g, int margin_total) 
public static Object adjustRanks(Object... arg) {
UNSUPPORTED("47ywar141vmwjartmboxs38cz"); // static void adjustRanks(graph_t * g, int margin_total)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cbyzaxb8pmtp6lo2y4tajk45s"); //     double lht;			/* label height */
UNSUPPORTED("3r7yt838qvvliagprkv30uka1"); //     double rht;			/* height between top and bottom ranks */
UNSUPPORTED("n5e6qzpiyv4zvlemheyvbky7"); //     int maxr, minr, margin;
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("7p11k11wgq46szos3u4lfa5a4"); //     double delta, ht1, ht2;
UNSUPPORTED("39w5issxrjmte1dn2qnt8gpbj"); //     rank_t *rank = GD_rank(dot_root(g));
UNSUPPORTED("aerooskq9gwysw6euktdqjmaq"); //     if (g == dot_root(g))
UNSUPPORTED("8tm7x791luf0uq7v2q53kfuqk"); // 	margin = 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("182pb9igel0aoytgtoiguek4x"); // 	margin = late_int (g, G_margin, 8, 0);
UNSUPPORTED("8bcey7cvczgf3t3ominh7ye1b"); //     ht1 = GD_ht1(g);
UNSUPPORTED("1ac5605x86vam4z4spx361rpd"); //     ht2 = GD_ht2(g);
UNSUPPORTED("99d9j6m0161wdv2tu4wbf3ifi"); //     for (c = 1; c <= GD_n_cluster(g); c++) {
UNSUPPORTED("6wacplh886lzcey0jjkfo5jcn"); // 	graph_t *subg = GD_clust(g)[c];
UNSUPPORTED("6jkyxr322md2frf0pic57ak6e"); // 	adjustRanks(subg, margin+margin_total);
UNSUPPORTED("41b09kckthwa070wznf9tneej"); // 	if (GD_maxrank(subg) == GD_maxrank(g))
UNSUPPORTED("7gbj2fwymn5k7pywbjc1kbhmb"); // 	    ht1 = MAX(ht1, GD_ht1(subg) + margin);
UNSUPPORTED("clsf4pg9bdlkzghw3oq7fpo7c"); // 	if (GD_minrank(subg) == GD_minrank(g))
UNSUPPORTED("9vmpt65t8kavsz1zoorcr5ik"); // 	    ht2 = MAX(ht2, GD_ht2(subg) + margin);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9lx88c3pk4z5zxca5orq7v2yf"); //     GD_ht1(g) = ht1;
UNSUPPORTED("2p0gs3kpty9mg9ymk70kjekh0"); //     GD_ht2(g) = ht2;
UNSUPPORTED("34p29mvxtmpuzcgzh4s03lixs"); //     if ((g != dot_root(g)) && GD_label(g)) {
UNSUPPORTED("ak9x1j2bu2d8zr8tls942u42r"); // 	lht = MAX(GD_border(g)[3].y, GD_border(g)[1].y);
UNSUPPORTED("ehkev01oc7ynzwkbjbzclaqj6"); // 	maxr = GD_maxrank(g);
UNSUPPORTED("2906nnve3dmqroo2jsq13wfex"); // 	minr = GD_minrank(g);
UNSUPPORTED("8s9tksg226ob1ti8o5ljo1gbg"); // 	rht = ND_coord(rank[minr].v[0]).y - ND_coord(rank[maxr].v[0]).y;
UNSUPPORTED("b0dxtg7jama7mgqccujsbb97n"); // 	delta = lht - (rht + ht1 + ht2);
UNSUPPORTED("bfapr2l9berlmq4ubgk0zp7qf"); // 	if (delta > 0) {
UNSUPPORTED("3zllny0pp0nf7gzw7829oqexo"); // 	    adjustSimple(g, delta, margin_total);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a86aoq62oyhhkqpyzz0um3pvq"); //     /* update the global ranks */
UNSUPPORTED("1pmq9pca155672xcv1ow2cbfy"); //     if (g != dot_root(g)) {
UNSUPPORTED("1kgp3b6qifeit9n2i2k8oc8qv"); // 	rank[GD_minrank(g)].ht2 = MAX(rank[GD_minrank(g)].ht2, GD_ht2(g));
UNSUPPORTED("8f7gccnlt6m8v3k1ijaznaew3"); // 	rank[GD_maxrank(g)].ht1 = MAX(rank[GD_maxrank(g)].ht1, GD_ht1(g));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 emtrqv582hdma5aajqtjd76m1
// static int clust_ht(Agraph_t * g) 
public static int clust_ht(ST_Agraph_s g) {
ENTERING("emtrqv582hdma5aajqtjd76m1","clust_ht");
try {
    int c;
    double ht1, ht2;
    ST_Agraph_s subg;
    ST_rank_t.Array2 rank = GD_rank(dot_root(g));
    int margin, haveClustLabel = 0;
    if (EQ(g, dot_root(g))) 
	margin = 8;
    else
	margin = late_int (g, Z.z().G_margin, 8, 0);
    ht1 = GD_ht1(g);
    ht2 = GD_ht2(g);
    /* account for sub-clusters */
    for (c = 1; c <= GD_n_cluster(g); c++) {
	subg = (ST_Agraph_s) GD_clust(g).get(c).getPtr();
	haveClustLabel |= clust_ht(subg);
	if (GD_maxrank(subg) == GD_maxrank(g))
	    ht1 = MAX(ht1, GD_ht1(subg) + margin);
	if (GD_minrank(subg) == GD_minrank(g))
	    ht2 = MAX(ht2, GD_ht2(subg) + margin);
    }
    /* account for a possible cluster label in clusters */
    /* room for root graph label is handled in dotneato_postprocess */
    if (NEQ(g, dot_root(g)) && GD_label(g)!=null) {
	haveClustLabel = 1;
	if (N(GD_flip(agroot(g)))) {
	    ht1 += GD_border(g)[0].y;
	    ht2 += GD_border(g)[2].y;
	}
    }
    GD_ht1(g, ht1);
    GD_ht2(g, ht2);
    /* update the global ranks */
    if (NEQ(g, dot_root(g))) {
	rank.plus(GD_minrank(g)).setDouble("ht2", MAX(rank.get(GD_minrank(g)).ht2, ht2));
	rank.plus(GD_maxrank(g)).setDouble("ht1", MAX(rank.get(GD_maxrank(g)).ht1, ht1));
    }
    return haveClustLabel;
} finally {
LEAVING("emtrqv582hdma5aajqtjd76m1","clust_ht");
}
}




//3 bp8vmol4ncadervcfossysdtd
// static void set_ycoords(graph_t * g) 
public static void set_ycoords(ST_Agraph_s g) {
ENTERING("bp8vmol4ncadervcfossysdtd","set_ycoords");
try {
    int i, j, r;
    double ht2, maxht, delta, d0, d1;
    ST_Agnode_s n;
    ST_Agedge_s e;
    ST_rank_t.Array2 rank = GD_rank(g);
    ST_Agraph_s clust;
    int lbl;
    ht2 = maxht = 0;
    /* scan ranks for tallest nodes.  */
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	for (i = 0; i < rank.get(r).n; i++) {
	    n = (ST_Agnode_s) rank.get(r).v.get(i);
	    /* assumes symmetry, ht1 = ht2 */
	    ht2 = ND_ht(n) / 2;
	    /* have to look for high self-edge labels, too */
	    if (ND_other(n).listNotNull())
		for (j = 0; (e = (ST_Agedge_s) ND_other(n).getFromList(j))!=null; j++) {
		    if (EQ(agtail(e), aghead(e))) {
			if (ED_label(e)!=null)
			    ht2 = MAX(ht2, ED_label(e).dimen.y / 2);
		    }
		}
	    /* update global rank ht */
	    if (rank.get(r).pht2 < ht2) {
		rank.plus(r).setDouble("ht2", ht2);
		rank.plus(r).setDouble("pht2", ht2);
		}
	    if (rank.get(r).pht1 < ht2) {
		rank.plus(r).setDouble("ht1", ht2);
		rank.plus(r).setDouble("pht1", ht2);
		}
	    /* update nearest enclosing cluster rank ht */
	    if ((clust = ND_clust(n))!=null) {
		int yoff = (clust == g ? 0 : late_int (clust, Z.z().G_margin, 8, 0));
		if (ND_rank(n) == GD_minrank(clust))
		    GD_ht2(clust, MAX(GD_ht2(clust), ht2 + yoff));
		if (ND_rank(n) == GD_maxrank(clust))
		    GD_ht1(clust, MAX(GD_ht1(clust), ht2 + yoff));
	    }
	}
    }
    /* scan sub-clusters */
    lbl = clust_ht(g);
    /* make the initial assignment of ycoords to leftmost nodes by ranks */
    maxht = 0;
    r = GD_maxrank(g);
    (ND_coord(rank.get(r).v.get(0))).setDouble("y", rank.get(r).ht1);
    while (--r >= GD_minrank(g)) {
	d0 = rank.get(r + 1).pht2 + rank.get(r).pht1 + GD_ranksep(g);	/* prim node sep */
	d1 = rank.get(r + 1).ht2 + rank.get(r).ht1 + 8;	/* cluster sep */
	delta = MAX(d0, d1);
	if (rank.get(r).n > 0)	/* this may reflect some problem */
		(ND_coord(rank.get(r).v.get(0))).setDouble("y", (ND_coord(rank.plus(r + 1).getPtr().v.get(0))).y + delta);
	maxht = MAX(maxht, delta);
    }
    /* If there are cluster labels and the drawing is rotated, we need special processing to
     * allocate enough room. We use adjustRanks for this, and then recompute the maxht if
     * the ranks are to be equally spaced. This seems simpler and appears to work better than
     * handling equal spacing as a special case.
     */
    if (lbl!=0 && GD_flip(g)!=0) {
UNSUPPORTED("bxjqk5nu40mwo1156dicr9tur"); // 	adjustRanks(g, 0);
UNSUPPORTED("6vy9qfed3u61pmvy12724s9l4"); // 	if (GD_exact_ranksep(g)) {  /* recompute maxht */
UNSUPPORTED("74f5n6u4x39ngn0gsan7fgzyr"); // 	    maxht = 0;
UNSUPPORTED("2pd9g1n9b0746fgt892degls3"); // 	    r = GD_maxrank(g);
UNSUPPORTED("8dils3hlxottsbf2iuapvhqeq"); // 	    d0 = (ND_coord(rank[r].v[0])).y;
UNSUPPORTED("cw5accmrcan3lqfc789udgcka"); // 	    while (--r >= GD_minrank(g)) {
UNSUPPORTED("6bxo7bknt38qh9t31zr7p6kie"); // 		d1 = (ND_coord(rank[r].v[0])).y;
UNSUPPORTED("b1ta7vjm5i7swyklhfwy27w35"); // 		delta = d1 - d0;
UNSUPPORTED("65l8hg0imd48bfdu614k2kylt"); // 		maxht = MAX(maxht, delta);
UNSUPPORTED("5irf6cp6xdzi2ik033azsbauo"); // 		d0 = d1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    /* re-assign if ranks are equally spaced */
    if (GD_exact_ranksep(g)!=0) {
UNSUPPORTED("cyxbyjrdzywkc46nl8lkrngai"); // 	for (r = GD_maxrank(g) - 1; r >= GD_minrank(g); r--)
UNSUPPORTED("5sd5ltavyp6llt0t2t0xmqwj5"); // 	    if (rank[r].n > 0)	/* this may reflect the same problem :-() */
UNSUPPORTED("5zoeqpznt31feqxjcx2rg0o1f"); // 			(ND_coord(rank[r].v[0])).y=
UNSUPPORTED("e6dfx5uesysjaefb0djyfp7f"); // 		    (ND_coord(rank[r + 1].v[0])).y + maxht;
    }
    /* copy ycoord assignment from leftmost nodes to others */
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
    ND_coord(n).y = ND_coord(rank.plus(ND_rank(n)).getPtr().v.get(0)).y;
} finally {
LEAVING("bp8vmol4ncadervcfossysdtd","set_ycoords");
}
}




//3 9ay2xnnmh407i32pfokujfda5
//static void dot_compute_bb(graph_t * g, graph_t * root) 
public static void dot_compute_bb(ST_Agraph_s g, ST_Agraph_s root) {
ENTERING("9ay2xnnmh407i32pfokujfda5","dot_compute_bb");
try {
 int r, c;
 double x, offset;
 ST_Agnode_s v;
 final ST_pointf LL = new ST_pointf();
 final ST_pointf UR = new ST_pointf();
 if (EQ(g, dot_root(g))) {
	LL.x = INT_MAX;
	UR.x = -((double)INT_MAX);
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    int rnkn = GD_rank(g).get(r).n;
	    if (rnkn == 0)
		continue;
	    if ((v = (ST_Agnode_s) GD_rank(g).get(r).v.get(0)) == null)
		continue;
	    for (c = 1; (ND_node_type(v) != 0) && c < rnkn; c++)
		v = (ST_Agnode_s) GD_rank(g).get(r).v.get(c);
	    if (ND_node_type(v) == 0) {
		x = ND_coord(v).x - ND_lw(v);
		LL.setDouble("x", MIN(LL.x, x));
	    }
	    else continue;
		/* At this point, we know the rank contains a NORMAL node */
	    v = (ST_Agnode_s) GD_rank(g).get(r).v.plus(rnkn - 1).getPtr();
	    for (c = rnkn-2; ND_node_type(v) != 0; c--)
		v = (ST_Agnode_s) GD_rank(g).get(r).v.get(c);
	    x = ND_coord(v).x + ND_rw(v);
	    UR.x = MAX(UR.x, x);
	}
	offset = 8;
	for (c = 1; c <= GD_n_cluster(g); c++) {
	    x = (double)(GD_bb(GD_clust(g).plus(c)).LL.x - offset);
	    LL.x = MIN(LL.x, x);
	    x = (double)(GD_bb(GD_clust(g).plus(c)).UR.x + offset);
	    UR.x = MAX(UR.x, x);
	}
 } else {
	LL.x = (double)(ND_rank(GD_ln(g)));
	UR.x = (double)(ND_rank(GD_rn(g)));
 }
 LL.y = ND_coord(GD_rank(root).plus(GD_maxrank(g)).getPtr().v.get(0)).y - GD_ht1(g);
 UR.y = ND_coord(GD_rank(root).plus(GD_minrank(g)).getPtr().v.get(0)).y + GD_ht2(g);
 GD_bb(g).setStruct("LL", LL);
 GD_bb(g).setStruct("UR", UR);
} finally {
LEAVING("9ay2xnnmh407i32pfokujfda5","dot_compute_bb");
}
}





//3 dlbpiimh9g9ff9w7wjoabf817
// static void rec_bb(graph_t * g, graph_t * root) 
public static void rec_bb(ST_Agraph_s g, ST_Agraph_s root) {
ENTERING("dlbpiimh9g9ff9w7wjoabf817","rec_bb");
try {
    int c;
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_bb((ST_Agraph_s) GD_clust(g).get(c).getPtr(), root);
    dot_compute_bb(g, root);
} finally {
LEAVING("dlbpiimh9g9ff9w7wjoabf817","rec_bb");
}
}




//3 2p3nacev7k2jft9xov90nke50
// static void scale_bb(graph_t * g, graph_t * root, double xf, double yf) 
public static Object scale_bb(Object... arg) {
UNSUPPORTED("9zkkf6x8cj12vuzqrjaoxfn8s"); // static void scale_bb(graph_t * g, graph_t * root, double xf, double yf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("7leh3r5v7roon4liia73wue7e"); // 	scale_bb(GD_clust(g)[c], root, xf, yf);
UNSUPPORTED("duz0iqmos1i372cztpjtsemab"); //     GD_bb(g).LL.x *= xf;
UNSUPPORTED("ja25041s2x99bhwj5wcliok0"); //     GD_bb(g).LL.y *= yf;
UNSUPPORTED("7mpnp4j7m1ymn1zdbxzeh5xxd"); //     GD_bb(g).UR.x *= xf;
UNSUPPORTED("4ahigy1a10wsl6lvc90lpax5e"); //     GD_bb(g).UR.y *= yf;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 53z9yhpfixulhgqsauulkllvc
// static void adjustAspectRatio (graph_t* g, aspect_t* asp) 
public static Object adjustAspectRatio(Object... arg) {
UNSUPPORTED("ezldb6r0csirv1fmkq5itw1v2"); // static void adjustAspectRatio (graph_t* g, aspect_t* asp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7rtldqefvdgcf4u50kulbxtvn"); //     double AR = (GD_bb(g).UR.x - GD_bb(g).LL.x)/(GD_bb(g).UR.y - GD_bb(g).LL.y);
UNSUPPORTED("cve2on8gll5i0vomy8lnwhai2"); //     if (Verbose) {
UNSUPPORTED("6z0q1m3yc6o11ejsa59eghqag"); //         fprintf(stderr, "AR=%0.4lf\t Area= %0.4lf\t", AR, (double)(GD_bb(g).UR.x - GD_bb(g).LL.x)*(GD_bb(g).UR.y - GD_bb(g).LL.y)/10000.0);
UNSUPPORTED("2uealcdkjdgg8ne1cijkbagpu"); //         fprintf(stderr, "Dummy=%d\n", countDummyNodes(g));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("96mqnzcfbfghxkxkn1x8kdh8i"); //     if (AR > 1.1*asp->targetAR) {
UNSUPPORTED("6gq7uj15zh138zyae50c8nh2z"); //       asp->nextIter = (int)(asp->targetAR * (double)(asp->curIterations - asp->prevIterations)/(AR));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e1443w2cx49ogpsf5m59zy4fq"); //     else if (AR <= 0.8 * asp->targetAR) {
UNSUPPORTED("5awa0x1pxpta5wou27bzrtvoc"); //       asp->nextIter = -1;
UNSUPPORTED("5xdo0sx20rmxgmdkrm1giaige"); //       if (Verbose)
UNSUPPORTED("du5ztjo6nfo54ailmk1tqs05b"); //         fprintf(stderr, "Going to apply another expansion.\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("757eq4638npmb5w5e39iemxfo"); // 	asp->nextIter = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("29wdml7g4931q8kgah8hgwjd0"); //         fprintf(stderr, "next#iter=%d\n", asp->nextIter);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7effq6z6ur101wrch6ttozr26
// static void set_aspect(graph_t * g, aspect_t* asp) 
public static void set_aspect(ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("7effq6z6ur101wrch6ttozr26","set_aspect");
try {
    double xf = 0.0, yf = 0.0, actual, desired;
    ST_Agnode_s n;
    boolean scale_it, filled;
    ST_point sz = new ST_point();
    rec_bb(g, g);
    if ((GD_maxrank(g) > 0) && (GD_drawing(g).ratio_kind!=0)) {
UNSUPPORTED("5wbmy4x78flo2ztfabki9lyjf"); // 	sz.x = GD_bb(g).UR.x - GD_bb(g).LL.x;
UNSUPPORTED("catd6eu5oc282ln95k9zz52f3"); // 	sz.y = GD_bb(g).UR.y - GD_bb(g).LL.y;	/* normalize */
UNSUPPORTED("21zvq2qx1j34j1i1879zyhzpj"); // 	if (GD_flip(g)) {
UNSUPPORTED("d55uzald1tvs7xodnua67pxv6"); // 	    int t = sz.x;
UNSUPPORTED("47s1klx0pfzda4e311w53ou7e"); // 	    sz.x = sz.y;
UNSUPPORTED("3tx1mj7j0rqw33y24a0gu4ali"); // 	    sz.y = t;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4ct8ztqxnsskgphsp1v3aw5ec"); // 	scale_it = NOT(0);
UNSUPPORTED("8v772n1u4wqlmskqfswlzcz2o"); // 	if (GD_drawing(g)->ratio_kind == R_AUTO)
UNSUPPORTED("8mtmgag5dxj8ttlcabbpd865p"); // 	    filled = idealsize(g, .5);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("bxtk5e5ls8qsd36eucvhufg9y"); // 	    filled = (GD_drawing(g)->ratio_kind == R_FILL);
UNSUPPORTED("2atgu691bmn6h9jvk8lve5qzc"); // 	if (filled) {
UNSUPPORTED("1zx5etcjofceqjvogfn8urkdj"); // 	    /* fill is weird because both X and Y can stretch */
UNSUPPORTED("7ezqjon4u21dwg4qvxssrnwfc"); // 	    if (GD_drawing(g)->size.x <= 0)
UNSUPPORTED("dao0fnoi65upcdtr9csqhhy41"); // 		scale_it = 0;
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("6ifkww34s7php908n9wg0oiju"); // 		xf = (double) GD_drawing(g)->size.x / (double) sz.x;
UNSUPPORTED("6faiikvxwdge2ydblv90976hb"); // 		yf = (double) GD_drawing(g)->size.y / (double) sz.y;
UNSUPPORTED("5xkzvdrdnfd5afhhxgajbywya"); // 		if ((xf < 1.0) || (yf < 1.0)) {
UNSUPPORTED("cvkvyq93xp1itpomhj1r2xlzy"); // 		    if (xf < yf) {
UNSUPPORTED("capfpf4tncicsp81elmwvf0l"); // 			yf = yf / xf;
UNSUPPORTED("7sdzyzqj65rbq6edfgf5x6xht"); // 			xf = 1.0;
UNSUPPORTED("d86r93g8nz9a1kfzgi7f8j8nh"); // 		    } else {
UNSUPPORTED("emwaipsi6kyqbpk2y26k3cxfw"); // 			xf = xf / yf;
UNSUPPORTED("1s91x56ftedjsc3m32dqgspqn"); // 			yf = 1.0;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("c48w89y9jw5baxqqucmiyfha7"); // 	} else if (GD_drawing(g)->ratio_kind == R_EXPAND) {
UNSUPPORTED("7ezqjon4u21dwg4qvxssrnwfc"); // 	    if (GD_drawing(g)->size.x <= 0)
UNSUPPORTED("dao0fnoi65upcdtr9csqhhy41"); // 		scale_it = 0;
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("akfs904fsk7cyl8wbv0x7fnvz"); // 		xf = (double) GD_drawing(g)->size.x /
UNSUPPORTED("1ewqbc4kglc2kg1n13euxrxzh"); // 		    (double) GD_bb(g).UR.x;
UNSUPPORTED("6fmkpg9ypaxceugi24gklvdra"); // 		yf = (double) GD_drawing(g)->size.y /
UNSUPPORTED("8mskb0mqou89myfbiihsjpbg6"); // 		    (double) GD_bb(g).UR.y;
UNSUPPORTED("a2jzgqbpoanzqdqc7vjk32vmz"); // 		if ((xf > 1.0) && (yf > 1.0)) {
UNSUPPORTED("6np8qfg5qnlaypikhw0bdx84j"); // 		    double scale = MIN(xf, yf);
UNSUPPORTED("272bmuv1row7l9tla5bhot840"); // 		    xf = yf = scale;
UNSUPPORTED("738mi6h8ef0itznt34ngxe25o"); // 		} else
UNSUPPORTED("b5qs3ho2fcywk3sd5cw3m88kw"); // 		    scale_it = 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("d7deewhp6akdb13j7ry364hbd"); // 	} else if (GD_drawing(g)->ratio_kind == R_VALUE) {
UNSUPPORTED("fpejwrlknxizaxxibhuyaxdt"); // 	    desired = GD_drawing(g)->ratio;
UNSUPPORTED("douwz2voka0puoeooqjn8kijk"); // 	    actual = ((double) sz.y) / ((double) sz.x);
UNSUPPORTED("7u9yvuqazzo19geppiphd9rfh"); // 	    if (actual < desired) {
UNSUPPORTED("apqq3m2rezfl96zbvk7lut02"); // 		yf = desired / actual;
UNSUPPORTED("8po0oizki4figodjv9xku16gq"); // 		xf = 1.0;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("84c3pp9xgnii11clyyxblqmy6"); // 		xf = actual / desired;
UNSUPPORTED("1tr1b9rp1b00pcafss87kadfe"); // 		yf = 1.0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6to1esmb8qfrhzgtr7jdqleja"); // 	} else
UNSUPPORTED("csvnhx5mo535o6ue1tg3ktjhs"); // 	    scale_it = 0;
UNSUPPORTED("bh2d68e9s7cr7k1bl0h9fmr9a"); // 	if (scale_it) {
UNSUPPORTED("b8symsgdtoq84y3j1151pv0g4"); // 	    if (GD_flip(g)) {
UNSUPPORTED("bi983gfofc0blj8r4yetj14kb"); // 		double t = xf;
UNSUPPORTED("69pmocxfvmk0urni4fg0x4na5"); // 		xf = yf;
UNSUPPORTED("cyt895z1pa5arxz4d1kv0hqgq"); // 		yf = t;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("crtcqz91ff5l8ntjbne40b5x4"); // 	    for (n = GD_nlist(g); n; n = ND_next(n)) {
UNSUPPORTED("brs6nych5z9m0a75ixbe5l80o"); // 		ND_coord(n).x = ROUND(ND_coord(n).x * xf);
UNSUPPORTED("cpe0pjsilppgrp2ofysn4y54w"); // 		ND_coord(n).y = ROUND(ND_coord(n).y * yf);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8f0d3etdet1pk8ikvltmz5h2s"); // 	    scale_bb(g, g, xf, yf);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    if (asp!=null) adjustAspectRatio (g, asp);
} finally {
LEAVING("7effq6z6ur101wrch6ttozr26","set_aspect");
}
}




//3 41mmud7cyx1rwhd0k7g7weaf8
// static point resize_leaf(node_t * leaf, point lbound) 
public static Object resize_leaf(Object... arg) {
UNSUPPORTED("alcqddrml4aj55twvzwyyvfjh"); // static point resize_leaf(node_t * leaf, point lbound)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dnj5vqe4cb2yzzx75ubwrxvav"); //     gv_nodesize(leaf, GD_flip(agraphof(leaf)));
UNSUPPORTED("bxig9t3r62l84xejntt3x96kv"); //     ND_coord(leaf).y = lbound.y;
UNSUPPORTED("bx00ps5hied5a63922sqqpvhi"); //     ND_coord(leaf).x = lbound.x + ND_lw(leaf);
UNSUPPORTED("euhijhn76fi66yqoh8gxk5nq0"); //     lbound.x = lbound.x + ND_lw(leaf) + ND_rw(leaf) + GD_nodesep(agraphof(leaf));
UNSUPPORTED("1xmj5l8g8znua01mljujyh61i"); //     return lbound;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4jx34mwc4aby8v45omelmfluv
// static point place_leaf(graph_t* ing, node_t * leaf, point lbound, int order) 
public static Object place_leaf(Object... arg) {
UNSUPPORTED("cetlk9m8mjd69vuru3rjho0lw"); // static point place_leaf(graph_t* ing, node_t * leaf, point lbound, int order)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b1ht34ftmnpsuxflrqun4ptkr"); //     node_t *leader;
UNSUPPORTED("aqfu9qyb55o3lehhaqmxag540"); //     graph_t *g = dot_root(ing);
UNSUPPORTED("1b5u4ut4j70lu60geogmkv572"); //     leader = UF_find(leaf);
UNSUPPORTED("3urvo2e4w7312r8scg44o0hqw"); //     if (leaf != leader)
UNSUPPORTED("azdkjz4shu49j3fzjpazxrpwz"); // 	fast_nodeapp(leader, leaf);
UNSUPPORTED("95h5y689udztblrtgdwc8y2ei"); //     ND_order(leaf) = order;
UNSUPPORTED("cvsh6pf3vxzxd2zn1u8ykd8hx"); //     ND_rank(leaf) = ND_rank(leader);
UNSUPPORTED("6k8r94a4chbol18r2rnbi9haj"); //     GD_rank(g)[ND_rank(leaf)].v[ND_order(leaf)] = leaf;
UNSUPPORTED("7ku2dbhi28rhi6dtag1v4zung"); //     return resize_leaf(leaf, lbound);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1lejhh3evsa10auyj7cgqj8ub
// static void make_leafslots(graph_t * g) 
public static void make_leafslots(ST_Agraph_s g) {
ENTERING("1lejhh3evsa10auyj7cgqj8ub","make_leafslots");
try {
    int i, j, r;
    ST_Agnode_s v;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	j = 0;
	for (i = 0; i < GD_rank(g).get(r).n; i++) {
	    v = (ST_Agnode_s) GD_rank(g).get(r).v.get(i);
	    ND_order(v, j);
	    if (ND_ranktype(v) == 6)
		j = j + ND_UF_size(v);
	    else
		j++;
	}
	if (j <= GD_rank(g).get(r).n)
	    continue;
	GD_rank(g).plus(r).setPtr("v", ALLOC_allocated_ST_Agnode_s((ST_Agnode_s.ArrayOfStar) GD_rank(g).get(r).v, j + 1));
	for (i = GD_rank(g).get(r).n - 1; i >= 0; i--) {
	    v = (ST_Agnode_s) GD_rank(g).get(r).v.get(i);
	    GD_rank(g).get(r).v.plus(ND_order(v)).setPtr(v);
	}
	GD_rank(g).plus(r).setInt("n", j);
	GD_rank(g).get(r).v.plus(j).setPtr(null);
    }
} finally {
LEAVING("1lejhh3evsa10auyj7cgqj8ub","make_leafslots");
}
}




//3 wb2wvxthkr2sp9u8113go3j3
// static void do_leaves(graph_t * g, node_t * leader) 
public static Object do_leaves(Object... arg) {
UNSUPPORTED("5nmyuqyhfqfwbmgdj5aot9fp4"); // static void do_leaves(graph_t * g, node_t * leader)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bs0wcp6367dz1o5x166ec7l8"); //     int j;
UNSUPPORTED("4bxv0pw87c31sbbz6x6s1cq9d"); //     point lbound;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("b9upgllg8zjx49090hr3afv91"); //     if (ND_UF_size(leader) <= 1)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("9m0hiwybw3dr0lcxmgq833heo"); //     lbound.x = ND_coord(leader).x - ND_lw(leader);
UNSUPPORTED("ev0phf24gpqz3xtvtueq72f7g"); //     lbound.y = ND_coord(leader).y;
UNSUPPORTED("90r9xqe4faj7b1g8907ord1x3"); //     lbound = resize_leaf(leader, lbound);
UNSUPPORTED("66ue8mvk3axhgbkcg3xqo94tb"); //     if (ND_out(leader).size > 0) {	/* in-edge leaves */
UNSUPPORTED("9lq5udq73fgfeqyqsxw6i3pgm"); // 	n = aghead(ND_out(leader).list[0]);
UNSUPPORTED("4vp9ny4udt1jcmibfgpwgrnqo"); // 	j = ND_order(leader) + 1;
UNSUPPORTED("3ml0tasns5tz6d5xc2xdb6nc"); // 	for (e = agfstin(g, n); e; e = agnxtin(g, e)) {
UNSUPPORTED("e2y71fdc15yxylowp6ohlal9a"); // 	    edge_t *e1 = AGMKOUT(e);
UNSUPPORTED("ew4udmdawt257gbk5kzmi1n1"); // 	    if ((agtail(e1) != leader) && (UF_find(agtail(e1)) == leader)) {
UNSUPPORTED("d2r94m7xk4qa9hn6s2td5nb6h"); // 		lbound = place_leaf(g, agtail(e1), lbound, j++);
UNSUPPORTED("5xk9d4ra447xucksge6c9mgos"); // 		unmerge_oneway(e1);
UNSUPPORTED("4dqun0n52lcyerkebva2hxh15"); // 		elist_append(e1, ND_in(aghead(e1)));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6owoaz61uf3928omhuf3rqjoa"); //     } else {			/* out edge leaves */
UNSUPPORTED("4lbkem8hb4jy3gdg6nt3cbtl9"); // 	n = agtail(ND_in(leader).list[0]);
UNSUPPORTED("4vp9ny4udt1jcmibfgpwgrnqo"); // 	j = ND_order(leader) + 1;
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("38a98cy0214odvsa98hgyce8q"); // 	    if ((aghead(e) != leader) && (UF_find(aghead(e)) == leader)) {
UNSUPPORTED("9j9o79r2bdc6npidu38aq0cym"); // 		lbound = place_leaf(g, aghead(e), lbound, j++);
UNSUPPORTED("6wbwzuqqh5vxume7ga2kuejcf"); // 		unmerge_oneway(e);
UNSUPPORTED("dv9vv9pfcd3cibfjn258toxyv"); // 		elist_append(e, ND_out(agtail(e)));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 alpljm8o6nsam95ly6leelnbp
// int ports_eq(edge_t * e, edge_t * f) 
public static boolean ports_eq(ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("alpljm8o6nsam95ly6leelnbp","ports_eq");
try {
    return ((ED_head_port(e).defined == ED_head_port(f).defined)
	    && (((ED_head_port(e).p.x == ED_head_port(f).p.x) &&
		 (ED_head_port(e).p.y == ED_head_port(f).p.y))
		|| (ED_head_port(e).defined == 0))
	    && (((ED_tail_port(e).p.x == ED_tail_port(f).p.x) &&
		 (ED_tail_port(e).p.y == ED_tail_port(f).p.y))
		|| (ED_tail_port(e).defined == 0))
	);
} finally {
LEAVING("alpljm8o6nsam95ly6leelnbp","ports_eq");
}
}




//3 cfotmdif5xv7n6oauyvzv0qwa
// static void expand_leaves(graph_t * g) 
public static void expand_leaves(ST_Agraph_s g) {
ENTERING("cfotmdif5xv7n6oauyvzv0qwa","expand_leaves");
try {
    int i, d;
    ST_Agnode_s n;
    ST_Agedge_s e, f;
    make_leafslots(g);
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if (ND_inleaf(n)!=null)
	    do_leaves(g, ND_inleaf(n));
	if (ND_outleaf(n)!=null)
	    do_leaves(g, ND_outleaf(n));
	if (ND_other(n).listNotNull())
	    for (i = 0; (e = (ST_Agedge_s) ND_other(n).getFromList(i))!=null; i++) {
		if ((d = ND_rank(aghead(e)) - ND_rank(aghead(e))) == 0)
		    continue;
		f = ED_to_orig(e);
		if (ports_eq(e, f) == false) {
		    zapinlist(ND_other(n), e);
		    if (d == 1)
			fast_edge(e);
		    /*else unitize(e); ### */
		    i--;
		}
	    }
    }
} finally {
LEAVING("cfotmdif5xv7n6oauyvzv0qwa","expand_leaves");
}
}




//3 d4b57ugpwxy567pfmxn14ed8d
// static void make_lrvn(graph_t * g) 
public static void make_lrvn(ST_Agraph_s g) {
ENTERING("d4b57ugpwxy567pfmxn14ed8d","make_lrvn");
try {
    ST_Agnode_s ln, rn;
    if (GD_ln(g)!=null)
	return;
    ln = virtual_node(dot_root(g));
    ND_node_type(ln, 2);
    rn = virtual_node(dot_root(g));
    ND_node_type(rn, 2);
    if (GD_label(g)!=null && NEQ(g, dot_root(g)) && N(GD_flip(agroot(g)))) {
	int w = MAX((int)GD_border(g)[0].x, (int)GD_border(g)[2].x);
	make_aux_edge(ln, rn, w, 0);
    }
    GD_ln(g, ln);
    GD_rn(g, rn);
} finally {
LEAVING("d4b57ugpwxy567pfmxn14ed8d","make_lrvn");
}
}




//3 daz786541idcxnywckcbncazb
// static void contain_nodes(graph_t * g) 
public static void contain_nodes(ST_Agraph_s  g) {
ENTERING("daz786541idcxnywckcbncazb","contain_nodes");
try {
    int margin, r;
    ST_Agnode_s ln, rn, v;
    margin = late_int (g, Z.z().G_margin, 8, 0);
    make_lrvn(g);
    ln = GD_ln(g);
    rn = GD_rn(g);
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	if (GD_rank(g).get(r).n == 0)
	    continue;
	v = (ST_Agnode_s) GD_rank(g).get(r).v.get(0);
	if (v == null) {
UNSUPPORTED("1f2esoodtcrdhljk1cq1klyao"); // 	    agerr(AGERR, "contain_nodes clust %s rank %d missing node\n",
UNSUPPORTED("7w6lv4ywtczwz2y1mg0p3jdav"); // 		  agnameof(g), r);
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
	}
	make_aux_edge(ln, v,
		      ND_lw(v) + margin + GD_border(g)[3].x, 0);
	v = (ST_Agnode_s) GD_rank(g).get(r).v.plus(GD_rank(g).get(r).n - 1).getPtr();
	make_aux_edge(v, rn,
		      ND_rw(v) + margin + GD_border(g)[1].x, 0);
    }
} finally {
LEAVING("daz786541idcxnywckcbncazb","contain_nodes");
}
}




//3 betdvb8dk8icvqkn6e6y5h94x
// static boolean idealsize(graph_t * g, double minallowed) 
public static Object idealsize(Object... arg) {
UNSUPPORTED("74m5dgfkgpr7bq7elj8bk5w6z"); // static boolean idealsize(graph_t * g, double minallowed)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bp9bpmgkh2gsdynqe5wio0i32"); //     double xf, yf, f, R;
UNSUPPORTED("8404v5b2qtn2x2mlgbyxidbx4"); //     pointf b, relpage, margin;
UNSUPPORTED("a3ke28asz3w87p4n8bgil541i"); //     /* try for one page */
UNSUPPORTED("avi96122npbhja9hwa4goidp"); //     relpage = GD_drawing(g)->page;
UNSUPPORTED("19uch6jfal0x385xni41gqam6"); //     if (relpage.x < 0.001 || relpage.y < 0.001)
UNSUPPORTED("52kuioyxcuboss35kg15wudvt"); // 	return 0;		/* no page was specified */
UNSUPPORTED("d6i2metpbkzakq2abzgvcdzle"); //     margin = GD_drawing(g)->margin;
UNSUPPORTED("asgqm8o7cruxxpfx9lobsif1c"); //     relpage = sub_pointf(relpage, margin);
UNSUPPORTED("asgqm8o7cruxxpfx9lobsif1c"); //     relpage = sub_pointf(relpage, margin);
UNSUPPORTED("aqhjsr7rv4ig7d1vagm74jpmo"); //     b.x = GD_bb(g).UR.x;
UNSUPPORTED("30xemhqjxz13hiyv2o2r8mol1"); //     b.y = GD_bb(g).UR.y;
UNSUPPORTED("cv2s8vkuet41gdoms8vue473z"); //     xf = relpage.x / b.x;
UNSUPPORTED("rvfej1l4o57i17le73vt80cf"); //     yf = relpage.y / b.y;
UNSUPPORTED("4f2ksj9hf0fi9wnzrz20eswmo"); //     if ((xf >= 1.0) && (yf >= 1.0))
UNSUPPORTED("e64anddwrzfgr4xoopjelwqg1"); // 	return 0;		/* fits on one page */
UNSUPPORTED("9vakpuy3iri2q9upsuy7lqhln"); //     f = MIN(xf, yf);
UNSUPPORTED("ccu6nxd64lwxm1h8x0sbrkdv7"); //     xf = yf = MAX(f, minallowed);
UNSUPPORTED("yc64sd7yr28zgjqrr734fc7i"); //     R = ceil((xf * b.x) / relpage.x);
UNSUPPORTED("aht5rw6872hwmg7vaop0eairh"); //     xf = ((R * relpage.x) / b.x);
UNSUPPORTED("asw2ato73u7m4kb07x2mr2snv"); //     R = ceil((yf * b.y) / relpage.y);
UNSUPPORTED("76j6b15q20gswxka8cglflbym"); //     yf = ((R * relpage.y) / b.y);
UNSUPPORTED("49gpvau7p2bvvkp6oemyqyjev"); //     GD_drawing(g)->size.x = b.x * xf;
UNSUPPORTED("7gewumkvzgtqon1jqxp8yf0kk"); //     GD_drawing(g)->size.y = b.y * yf;
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
