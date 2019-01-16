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
import static gen.lib.cgraph.edge__c.agedge;
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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
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
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_pointf;
import smetana.core.Z;

public class class2__c {
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




//3 2zn7c6ulmwwzaibdxo127jf04
// static node_t* label_vnode(graph_t * g, edge_t * orig) 
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
	if (GD_flip(agroot(g))!=0) {
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




//3 9fmfj1b2jik7skv6ms0657t8r
// static node_t* leader_of(graph_t * g, node_t * v) 
public static ST_Agnode_s leader_of(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("9fmfj1b2jik7skv6ms0657t8r","leader_of");
try {
    ST_Agraph_s clust;
    ST_Agnode_s rv;
    if (ND_ranktype(v) != 7) {
	/*assert(v == UF_find(v));  could be leaf, so comment out */
	rv = UF_find(v);
    } else {
	clust = ND_clust(v);
	rv = (ST_Agnode_s) GD_rankleader(clust).plus(ND_rank(v)).getPtr();
    }
    return rv;
} finally {
LEAVING("9fmfj1b2jik7skv6ms0657t8r","leader_of");
}
}




//3 6sbvlvurvkodunw2qt1ug70c2
// static void  make_chain(graph_t * g, node_t * from, node_t * to, edge_t * orig) 
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




//3 659ld5tcseo3l0hopxb3pf0vv
// static void  interclrep(graph_t * g, edge_t * e) 
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
	    merge_chain(g, e, ve, NOT(false));
	    return;
	}
	if (ND_rank(t) == ND_rank(h))
	    return;
	make_chain(g, t, h, e);
	/* mark as cluster edge */
	for (ve = ED_to_virt(e); ve!=null && (ND_rank(aghead(ve)) <= ND_rank(h));
	     ve = (ST_Agedge_s) ND_out(aghead(ve)).getFromList(0))
	    ED_edge_type(ve, 5);
    }
    /* else ignore intra-cluster edges at this point */
} finally {
LEAVING("659ld5tcseo3l0hopxb3pf0vv","interclrep");
}
}




//3 c0cx00ki1i1kx0bp84e7xjg8d
// static int  is_cluster_edge(edge_t * e) 
public static boolean is_cluster_edge(ST_Agedge_s e) {
ENTERING("c0cx00ki1i1kx0bp84e7xjg8d","is_cluster_edge");
try {
    return ((ND_ranktype(agtail(e)) == 7)
	    || (ND_ranktype(aghead(e)) == 7));
} finally {
LEAVING("c0cx00ki1i1kx0bp84e7xjg8d","is_cluster_edge");
}
}




//3 c45973dtaighb3u0auuekcs1y
// void merge_chain(graph_t * g, edge_t * e, edge_t * f, int flag) 
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
	rep = (ST_Agedge_s) ND_out(aghead(rep)).getFromList(0);
    } while (rep!=null);
} finally {
LEAVING("c45973dtaighb3u0auuekcs1y","merge_chain");
}
}




//3 bg5r9wlego0d8pv0hr96zt45c
// int mergeable(edge_t * e, edge_t * f) 
public static boolean mergeable(ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("bg5r9wlego0d8pv0hr96zt45c","mergeable");
try {
    if (e!=null && f!=null && EQ(agtail(e), agtail(f)) && EQ(aghead(e), aghead(f)) &&
	EQ(ED_label(e), ED_label(f)) && ports_eq(e, f))
	return NOT(false);
    return false;
} finally {
LEAVING("bg5r9wlego0d8pv0hr96zt45c","mergeable");
}
}




//3 d0bxlkysxucmww7t74u9krrgz
// void class2(graph_t * g) 
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
	build_skeleton(g, (ST_Agraph_s) GD_clust(g).get(c).getPtr());
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n))
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
	    if (ND_weight_class(aghead(e)) <= 2)
		ND_weight_class(aghead(e), ND_weight_class(aghead(e))+1);
	    if (ND_weight_class(agtail(e)) <= 2)
		ND_weight_class(agtail(e), ND_weight_class(agtail(e))+1);
	}
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	if ((ND_clust(n) == null) && (EQ(n, UF_find(n)))) {
	    fast_node(g, n);
	    GD_n_nodes(g, GD_n_nodes(g)+1);
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
			ED_edge_type(e, 6);
		    else {
			merge_chain(g, e, ED_to_virt(prev), NOT(false));
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
		if ((opp = (agedge(g,aghead(e),agtail(e),null,false)))!=null && NEQ(aghead(opp), aghead(e))) {
		    /* shadows a forward edge */
		    if (ED_to_virt(opp) == null)
			make_chain(g, agtail(opp), aghead(opp), opp);
		    if ((ED_label(e) == null) && (ED_label(opp) == null)
			&& ports_eq(e, opp)) {
			if (Z.z().Concentrate) {
			    ED_edge_type(e, 6);
			    ED_conc_opp_flag(opp, NOT(false));
			} else {	/* see above.  this is getting out of hand */
			    other_edge(e);
			    merge_chain(g, e, ED_to_virt(opp), NOT(false));
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
    	GD_comp(g).reallocEmpty(1);
    	GD_comp(g).setInList(0, GD_nlist(g));
    }
} finally {
LEAVING("d0bxlkysxucmww7t74u9krrgz","class2");
}
}


}
