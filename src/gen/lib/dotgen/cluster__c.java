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
import static gen.lib.cgraph.edge__c.agfstedge;
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtedge;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agcontains;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.utils__c.UF_setname;
import static gen.lib.common.utils__c.UF_singleton;
import static gen.lib.dotgen.class2__c.class2;
import static gen.lib.dotgen.class2__c.mergeable;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.delete_fast_edge;
import static gen.lib.dotgen.fastgr__c.delete_fast_node;
import static gen.lib.dotgen.fastgr__c.fast_node;
import static gen.lib.dotgen.fastgr__c.find_fast_edge;
import static gen.lib.dotgen.fastgr__c.find_flat_edge;
import static gen.lib.dotgen.fastgr__c.flat_edge;
import static gen.lib.dotgen.fastgr__c.merge_oneway;
import static gen.lib.dotgen.fastgr__c.other_edge;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static gen.lib.dotgen.fastgr__c.virtual_node;
import static gen.lib.dotgen.mincross__c.allocate_ranks;
import static gen.lib.dotgen.mincross__c.build_ranks;
import static gen.lib.dotgen.mincross__c.enqueue_neighbors;
import static gen.lib.dotgen.mincross__c.install_in_rank;
import static gen.lib.dotgen.position__c.ports_eq;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGMKOUT;
import static smetana.core.Macro.ED_count;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_expanded;
import static smetana.core.Macro.GD_installed;
import static smetana.core.Macro.GD_leader;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_rankleader;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_nodequeue;
import h.ST_pointf;
import smetana.core.__ptr__;

public class cluster__c {
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




//3 8bd317q0mykfu6wpr3e4cxmh2
// static node_t* map_interclust_node(node_t * n) 
public static ST_Agnode_s map_interclust_node(ST_Agnode_s n) {
ENTERING("8bd317q0mykfu6wpr3e4cxmh2","map_interclust_node");
try {
    ST_Agnode_s rv;
    if ((ND_clust(n) == null) || (  GD_expanded(ND_clust(n))) )
	rv = n;
    else
	rv = (ST_Agnode_s) GD_rankleader(ND_clust(n)).plus(ND_rank(n)).getPtr();
    return rv;
} finally {
LEAVING("8bd317q0mykfu6wpr3e4cxmh2","map_interclust_node");
}
}




//3 5ib4nnt2ah5fdd22zs0xds29r
// static void  make_slots(graph_t * root, int r, int pos, int d) 
public static void make_slots(ST_Agraph_s root, int r, int pos, int d) {
ENTERING("5ib4nnt2ah5fdd22zs0xds29r","make_slots");
try {
    int i;
    ST_Agnode_s v;
    ST_Agnode_s.ArrayOfStar vlist;
    vlist = GD_rank(root).get(r).v;
    if (d <= 0) {
	for (i = pos - d + 1; i < GD_rank(root).get(r).n; i++) {
	    v = (ST_Agnode_s) vlist.get(i);
	    ND_order(v, i + d - 1);
	    vlist.plus(ND_order(v)).setPtr(v);
	}
	for (i = GD_rank(root).get(r).n + d - 1; i < GD_rank(root).get(r).n; i++)
	    vlist.plus(i).setPtr(null);
    } else {
/*assert(ND_rank(root)[r].n + d - 1 <= ND_rank(root)[r].an);*/
	for (i = GD_rank(root).get(r).n - 1; i > pos; i--) {
	    v = (ST_Agnode_s) vlist.get(i);
	    ND_order(v, i + d - 1);
	    vlist.plus(ND_order(v)).setPtr(v);
	}
	for (i = pos + 1; i < pos + d; i++)
	    vlist.plus(i).setPtr(null);
    }
    GD_rank(root).get(r).setInt("n", GD_rank(root).get(r).n + d - 1);
} finally {
LEAVING("5ib4nnt2ah5fdd22zs0xds29r","make_slots");
}
}




//3 d4mwxesl56uh9dyttg9cjlq70
// static node_t*  clone_vn(graph_t * g, node_t * vn) 
public static ST_Agnode_s clone_vn(ST_Agraph_s g, ST_Agnode_s vn) {
ENTERING("d4mwxesl56uh9dyttg9cjlq70","clone_vn");
try {
    ST_Agnode_s rv;
    int r;
    r = ND_rank(vn);
    make_slots(g, r, ND_order(vn), 2);
    rv = virtual_node(g);
    ND_lw(rv, ND_lw(vn));
    ND_rw(rv, ND_rw(vn));
    ND_rank(rv, ND_rank(vn));
UNSUPPORTED("adc0qfdhup29vh8qu1cwl5jgj"); //     GD_rank(g)[r].v[ND_order(rv)] = rv;
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("d4mwxesl56uh9dyttg9cjlq70","clone_vn");
}
}




//3 6o86r59v2ujlxqcw7761y6o5b
// static void  map_path(node_t * from, node_t * to, edge_t * orig, edge_t * ve, int type) 
public static void map_path(ST_Agnode_s from, ST_Agnode_s to, ST_Agedge_s orig, ST_Agedge_s ve, int type) {
ENTERING("6o86r59v2ujlxqcw7761y6o5b","map_path");
try {
    int r;
    ST_Agnode_s u, v;
    ST_Agedge_s e;
    assert(ND_rank(from) < ND_rank(to));
    if (EQ(agtail(ve), from) && EQ(aghead(ve), to))
	return;
    if (ED_count(ve) > 1) {
	ED_to_virt(orig, null);
	if (ND_rank(to) - ND_rank(from) == 1) {
	    if ((e = find_fast_edge(from, to))!=null && (ports_eq(orig, e))) {
		merge_oneway(orig, e);
		if ((ND_node_type(from) == 0)
		    && (ND_node_type(to) == 0))
		    other_edge(orig);
		return;
	    }
	}
	u = from;
	for (r = ND_rank(from); r < ND_rank(to); r++) {
	    if (r < ND_rank(to) - 1)
		v = clone_vn(dot_root(from), aghead(ve));
	    else
		v = to;
	    e = virtual_edge(u, v, orig);
	    ED_edge_type(e, type);
	    u = v;
	    ED_count(ve, ED_count(ve) - 1);
	    ve = (ST_Agedge_s) ND_out(aghead(ve)).getFromList(0);
	}
    } else {
	if (ND_rank(to) - ND_rank(from) == 1) {
	    if ((ve = find_fast_edge(from, to))!=null && (ports_eq(orig, ve))) {
		/*ED_to_orig(ve) = orig; */
		ED_to_virt(orig, ve);
		ED_edge_type(ve, type);
		ED_count(ve, ED_count(ve)+1);
		if ((ND_node_type(from) == 0)
		    && (ND_node_type(to) == 0))
		    other_edge(orig);
	    } else {
		ED_to_virt(orig, null);
		ve = virtual_edge(from, to, orig);
		ED_edge_type(ve, type);
	    }
	}
	if (ND_rank(to) - ND_rank(from) > 1) {
	    e = ve;
	    if (NEQ(agtail(ve), from)) {
		ED_to_virt(orig, null);
		e = virtual_edge(from, aghead(ve), orig);
		ED_to_virt(orig, e);
		delete_fast_edge(ve);
	    } else
		e = ve;
	    while (ND_rank(aghead(e)) != ND_rank(to))
		e = (ST_Agedge_s) ND_out(aghead(e)).getFromList(0);
	    if (NEQ(aghead(e), to)) {
		ve = e;
		e = virtual_edge(agtail(e), to, orig);
		ED_edge_type(e, type);
		delete_fast_edge(ve);
	    }
	}
    }
} finally {
LEAVING("6o86r59v2ujlxqcw7761y6o5b","map_path");
}
}




//3 69xbflgja0gvrsl5xcv7o7dia
// static void  make_interclust_chain(graph_t * g, node_t * from, node_t * to, edge_t * orig) 
public static void make_interclust_chain(ST_Agraph_s g, ST_Agnode_s from, ST_Agnode_s to, ST_Agedge_s orig) {
ENTERING("69xbflgja0gvrsl5xcv7o7dia","make_interclust_chain");
try {
    int newtype;
    ST_Agnode_s u, v;
    u = map_interclust_node(from);
    v = map_interclust_node(to);
    if (EQ(u, from) && EQ(v, to))
	newtype = 1;
    else
	newtype = 5;
    map_path(u, v, orig, ED_to_virt(orig), newtype);
} finally {
LEAVING("69xbflgja0gvrsl5xcv7o7dia","make_interclust_chain");
}
}




//3 6g2m2y44x66lajznvnon2gubv
// void interclexp(graph_t * subg) 
public static void interclexp(ST_Agraph_s subg) {
ENTERING("6g2m2y44x66lajznvnon2gubv","interclexp");
try {
    ST_Agraph_s g;
    ST_Agnode_s n;
    ST_Agedge_s e, prev, next;
    g = dot_root(subg);
    for (n = agfstnode(subg); n!=null; n = agnxtnode(subg, n)) {
	/* N.B. n may be in a sub-cluster of subg */
	prev = null;
	for (e = agfstedge(g, n); e!=null; e = next) {
	    next = agnxtedge(g, e, n);
	    if (agcontains(subg, e))
		continue;
	    /* canonicalize edge */
	    e = AGMKOUT(e);
	    /* short/flat multi edges */
	    if (mergeable(prev, e)) {
	    	if (ND_rank(agtail(e)) == ND_rank(aghead(e)))
			    ED_to_virt(e, prev);
	    	else
	    		ED_to_virt(e, null);
 		if (ED_to_virt(prev) == null)
 			continue;	/* internal edge */
UNSUPPORTED("8d5mw7m9lzlseqbyx8a8mncgs"); // 		merge_chain(subg, e, ED_to_virt(prev), 0);
UNSUPPORTED("87mmnlsj8quzlzg0vxax15kt2"); // 		safe_other_edge(e);
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
	    }
	    /* flat edges */
	    if (ND_rank(agtail(e)) == ND_rank(aghead(e))) {
		ST_Agedge_s fe;
		if ((fe = find_flat_edge(agtail(e), aghead(e))) == null) {
		    flat_edge(g, e);
		    prev = e;
		} else if (NEQ(e, fe)) {
UNSUPPORTED("ckfinb4h4twp1ry02y9peyhz"); // 		    safe_other_edge(e);
UNSUPPORTED("dg3e0udctqa7xtfynplc7wdpj"); // 		    if (!ED_to_virt(e)) merge_oneway(e, fe);
		}
		continue;
	    }
	    /* forward edges */
	    if (ND_rank(aghead(e)) > ND_rank(agtail(e))) {
		make_interclust_chain(g, agtail(e), aghead(e), e);
		prev = e;
		continue;
	    }
	    /* backward edges */
	    else {
/*
I think that make_interclust_chain should create call other_edge(e) anyway 
				if (agcontains(subg,agtail(e))
					&& agfindedge(g,aghead(e),agtail(e))) other_edge(e);
*/
		make_interclust_chain(g, aghead(e), agtail(e), e);
		prev = e;
	    }
	}
    }
} finally {
LEAVING("6g2m2y44x66lajznvnon2gubv","interclexp");
}
}




//3 85nhs7tnmwunw0fsjj1kxao7l
// static void  merge_ranks(graph_t * subg) 
public static void merge_ranks(ST_Agraph_s subg) {
ENTERING("85nhs7tnmwunw0fsjj1kxao7l","merge_ranks");
try {
    int i, d, r, pos, ipos;
    ST_Agnode_s v;
    ST_Agraph_s root;
    root = dot_root(subg);
    if (GD_minrank(subg) > 0)
	GD_rank(root).plus(GD_minrank(subg) - 1).getPtr().setInt("valid", 0);
    for (r = GD_minrank(subg); r <= GD_maxrank(subg); r++) {
	d = GD_rank(subg).get(r).n;
	ipos = pos = ND_order(GD_rankleader(subg).get(r));
	make_slots(root, r, pos, d);
	for (i = 0; i < GD_rank(subg).get(r).n; i++) {
	    v = (ST_Agnode_s) GD_rank(subg).get(r).v.get(i);
	    GD_rank(root).get(r).v.plus(pos).setPtr(v);
	    ND_order(v, pos++);
	/* real nodes automatically have v->root = root graph */
	    if (ND_node_type(v) == 1)
		v.setPtr("root", agroot(root));
	    delete_fast_node(subg, v);
	    fast_node(root, v);
	    GD_n_nodes(root, GD_n_nodes(root)+1);
	}
	GD_rank(subg).get(r).setPtr("v", GD_rank(root).get(r).v.plus(ipos));
	GD_rank(root).get(r).setInt("valid", 0);
    }
    if (r < GD_maxrank(root))
	GD_rank(root).get(r).setInt("valid", 0);
    GD_expanded(subg, NOT(false));
} finally {
LEAVING("85nhs7tnmwunw0fsjj1kxao7l","merge_ranks");
}
}




//3 c9p7dm16i13qktnh95os0sv58
// static void  remove_rankleaders(graph_t * g) 
public static void remove_rankleaders(ST_Agraph_s g) {
ENTERING("c9p7dm16i13qktnh95os0sv58","remove_rankleaders");
try {
    int r;
    ST_Agnode_s v;
    ST_Agedge_s e;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	v = (ST_Agnode_s) GD_rankleader(g).get(r);
	/* remove the entire chain */
	while ((e = (ST_Agedge_s) ND_out(v).getFromList(0))!=null)
	    delete_fast_edge(e);
	while ((e = (ST_Agedge_s) ND_in(v).getFromList(0))!=null)
	    delete_fast_edge(e);
	delete_fast_node(dot_root(g), v);
	GD_rankleader(g).plus(r).setPtr(null);
    }
} finally {
LEAVING("c9p7dm16i13qktnh95os0sv58","remove_rankleaders");
}
}




//3 ecrplg8hsyl484f9kxc5xp0go
// void expand_cluster(graph_t * subg) 
public static void expand_cluster(ST_Agraph_s subg) {
ENTERING("ecrplg8hsyl484f9kxc5xp0go","expand_cluster");
try {
    /* build internal structure of the cluster */
    class2(subg);
    GD_comp(subg).size = 1;
    // GD_comp(subg).list.plus(0).setPtr(GD_nlist(subg));
    GD_comp(subg).setInList(0, GD_nlist(subg));
    allocate_ranks(subg);
    build_ranks(subg, 0);
    merge_ranks(subg);
    /* build external structure of the cluster */
    interclexp(subg);
    remove_rankleaders(subg);
} finally {
LEAVING("ecrplg8hsyl484f9kxc5xp0go","expand_cluster");
}
}




//3 cxuirggihlap2iv2khmb1w5l5
// void mark_clusters(graph_t * g) 
public static void mark_clusters(ST_Agraph_s g) {
ENTERING("cxuirggihlap2iv2khmb1w5l5","mark_clusters");
try {
    int c;
    ST_Agnode_s n, nn=null, vn;
    ST_Agedge_s orig, e;
    ST_Agraph_s clust;
    /* remove sub-clusters below this level */
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	if (ND_ranktype(n) == 7)
	    UF_singleton(n);
	ND_clust(n, null);
    }
    for (c = 1; c <= GD_n_cluster(g); c++) {
	clust = (ST_Agraph_s) GD_clust(g).get(c).getPtr();
	for (n = agfstnode(clust); n!=null; n = nn) {
		nn = agnxtnode(clust,n);
	    if (ND_ranktype(n) != 0) {
UNSUPPORTED("5l8jenkv77ax02t47zzxyv1k0"); // 		agerr(AGWARN,
UNSUPPORTED("2ipl4umxgijawr7756ysp9hhd"); // 		      "%s was already in a rankset, deleted from cluster %s\n",
UNSUPPORTED("7r0ulsiau9cygesawzzjnpt5j"); // 		      agnameof(n), agnameof(g));
UNSUPPORTED("4zqc8357rwnd9xe7zaoqooqv3"); // 		agdelete(clust,n);
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
	    }
	    UF_setname(n, GD_leader(clust));
	    ND_clust(n, clust);
	    ND_ranktype(n, 7);
	    /* here we mark the vnodes of edges in the cluster */
	    for (orig = agfstout(clust, n); orig!=null;
		 orig = agnxtout(clust, orig)) {
		if ((e = ED_to_virt(orig))!=null) {
		    while (e!=null && ND_node_type(vn =aghead(e)) == 1) {
			ND_clust(vn, clust);
			e = (ST_Agedge_s) ND_out(aghead(e)).getFromList(0);
			/* trouble if concentrators and clusters are mixed */
		    }
		}
	    }
	}
    }
} finally {
LEAVING("cxuirggihlap2iv2khmb1w5l5","mark_clusters");
}
}




//3 bwrw5u0gi2rgah1cn9h0glpse
// void build_skeleton(graph_t * g, graph_t * subg) 
public static void build_skeleton(ST_Agraph_s g, ST_Agraph_s subg) {
ENTERING("bwrw5u0gi2rgah1cn9h0glpse","build_skeleton");
try {
    int r;
    ST_Agnode_s v, prev, rl;
    ST_Agedge_s e;
    prev = null;
    // GD_rankleader(subg, zmalloc(sizeof_starstar_empty(Agnode_s.class, GD_maxrank(subg) + 2)));
    GD_rankleader(subg, new ST_Agnode_s.Array(GD_maxrank(subg) + 2));
    for (r = GD_minrank(subg); r <= GD_maxrank(subg); r++) {
	v = virtual_node(g);
	GD_rankleader(subg).plus(r).setPtr(v);
	ND_rank(v, r);
	ND_ranktype(v, 7);
	ND_clust(v, subg);
	if (prev!=null) {
	    e = virtual_edge(prev, v, null);
	    ED_xpenalty(e, ED_xpenalty(e) * 1000);
	}
	prev = v;
    }
    /* set the counts on virtual edges of the cluster skeleton */
    for (v = agfstnode(subg); v!=null; v = agnxtnode(subg, v)) {
	rl = (ST_Agnode_s) GD_rankleader(subg).plus(ND_rank(v)).getPtr();
	ND_UF_size(rl, ND_UF_size(rl)+1);
	for (e = agfstout(subg, v); e!=null; e = agnxtout(subg, e)) {
	    for (r = ND_rank(agtail(e)); r < ND_rank(aghead(e)); r++) {
		ED_count(ND_out(rl).getFromList(0), ED_count(ND_out(rl).getFromList(0))+1);
	    }
	}
    }
    for (r = GD_minrank(subg); r <= GD_maxrank(subg); r++) {
	rl = (ST_Agnode_s) GD_rankleader(subg).get(r);
	if (ND_UF_size(rl) > 1)
	    ND_UF_size(rl, ND_UF_size(rl)-1);
    }
} finally {
LEAVING("bwrw5u0gi2rgah1cn9h0glpse","build_skeleton");
}
}




//3 75yt3xwcwnxipi827t1r8zcmn
// void install_cluster(graph_t * g, node_t * n, int pass, nodequeue * q) 
public static void install_cluster(ST_Agraph_s g, ST_Agnode_s n, int pass, ST_nodequeue q) {
ENTERING("75yt3xwcwnxipi827t1r8zcmn","install_cluster");
try {
    int r;
    ST_Agraph_s clust;
    clust = ND_clust(n);
    if (GD_installed(clust) != pass + 1) {
	for (r = GD_minrank(clust); r <= GD_maxrank(clust); r++)
	    install_in_rank(g, (ST_Agnode_s) GD_rankleader(clust).get(r));
	for (r = GD_minrank(clust); r <= GD_maxrank(clust); r++)
	    enqueue_neighbors(q, (ST_Agnode_s) GD_rankleader(clust).get(r), pass);
	GD_installed(clust, pass + 1);
    }
} finally {
LEAVING("75yt3xwcwnxipi827t1r8zcmn","install_cluster");
}
}




//3 4muksvb3ec03mt6cvaqpb5c7a
// void mark_lowclusters(Agraph_t * root) 
public static void mark_lowclusters(ST_Agraph_s root) {
ENTERING("4muksvb3ec03mt6cvaqpb5c7a","mark_lowclusters");
try {
    ST_Agnode_s n, vn;
    ST_Agedge_s orig, e;
    /* first, zap any previous cluster labelings */
    for (n = agfstnode(root); n!=null; n = agnxtnode(root, n)) {
	ND_clust(n, null);
	for (orig = agfstout(root, n); orig!=null; orig = agnxtout(root, orig)) {
	    if ((e = ED_to_virt(orig))!=null) {
		while (e!=null && (ND_node_type(vn = aghead(e))) == 1) {
		    ND_clust(vn, null);
		    e = (ST_Agedge_s) ND_out(aghead(e)).getFromList(0);
		}
	    }
	}
    }
    /* do the recursion */
    mark_lowcluster_basic(root);
} finally {
LEAVING("4muksvb3ec03mt6cvaqpb5c7a","mark_lowclusters");
}
}




//3 48j6fdymvkcgeh4wde060ctac
// static void mark_lowcluster_basic(Agraph_t * g) 
public static void mark_lowcluster_basic(ST_Agraph_s g) {
ENTERING("48j6fdymvkcgeh4wde060ctac","mark_lowcluster_basic");
try {
    ST_Agraph_s clust;
    ST_Agnode_s n, vn;
    ST_Agedge_s orig, e;
    int c;
    for (c = 1; c <= GD_n_cluster(g); c++) {
	clust = (ST_Agraph_s) GD_clust(g).get(c).getPtr();
	mark_lowcluster_basic(clust);
    }
    /* see what belongs to this graph that wasn't already marked */
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	if (ND_clust(n) == null)
	    ND_clust(n, g);
	for (orig = agfstout(g, n); orig!=null; orig = agnxtout(g, orig)) {
	    if ((e = ED_to_virt(orig))!=null) {
		while (e!=null && (ND_node_type(vn = aghead(e))) == 1) {
		    if (ND_clust(vn) == null)
			ND_clust(vn, g);
		    e = (ST_Agedge_s) ND_out(aghead(e)).getFromList(0);
		}
	    }
	}
    }
} finally {
LEAVING("48j6fdymvkcgeh4wde060ctac","mark_lowcluster_basic");
}
}


}
