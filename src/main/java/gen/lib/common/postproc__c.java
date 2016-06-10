/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.geom__c.ccwrotatepf;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_label;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_label;
import static smetana.core.Macro.ED_xlabel;
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
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_xlabel;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.Agedge_s;
import h.Agnode_s;
import h.Agraph_s;
import h.Agsym_s;
import h.bezier;
import h.boxf;
import h.label_params_t;
import h.object_t;
import h.pointf;
import h.textlabel_t;
import h.xlabel_t;
import smetana.core.Z;
import smetana.core.__struct__;

public class postproc__c {
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
public static __struct__<pointf> pointfof(double x, double y) {
// WARNING!! STRUCT
return pointfof_w_(x, y).copy();
}
private static __struct__<pointf> pointfof_w_(double x, double y) {
ENTERING("c1s4k85p1cdfn176o3uryeros","pointfof");
try {
    final __struct__<pointf> r = __struct__.from(pointf.class);
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
public static __struct__<boxf> boxfof(double llx, double lly, double urx, double ury) {
// WARNING!! STRUCT
return boxfof_w_(llx, lly, urx, ury).copy();
}
private static __struct__<boxf> boxfof_w_(double llx, double lly, double urx, double ury) {
ENTERING("1vvsta5i8of59frav6uymguav","boxfof");
try {
    final __struct__<boxf> b = __struct__.from(boxf.class);
    b.getStruct("LL").setDouble("x", llx);
    b.getStruct("LL").setDouble("y", lly);
    b.getStruct("UR").setDouble("x", urx);
    b.getStruct("UR").setDouble("y", ury);
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
public static __struct__<pointf> add_pointf(final __struct__<pointf> p, final __struct__<pointf> q) {
// WARNING!! STRUCT
return add_pointf_w_(p.copy(), q.copy()).copy();
}
private static __struct__<pointf> add_pointf_w_(final __struct__<pointf> p, final __struct__<pointf> q) {
ENTERING("arrsbik9b5tnfcbzsm8gr2chx","add_pointf");
try {
    final __struct__<pointf> r = __struct__.from(pointf.class);
    r.setDouble("x", p.getDouble("x") + q.getDouble("x"));
    r.setDouble("y", p.getDouble("y") + q.getDouble("y"));
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


//1 1b0s7t28jl5ftrxccb8fsopp4
// static int Rankdir
//private static int Rankdir;

//1 3oo1lt5jtd6kuqjr6qqlnkutt
// static boolean Flip
//private static boolean Flip;

//1 ejooa1m5uoq0ue852wtuerpy
// static pointf Offset
//private final static __struct__<pointf> Offset = __struct__.from(pointf.class);



//3 dajapw16wus3rwimkrk5ihi2b
// static pointf map_point(pointf p) 
public static __struct__<pointf> map_point(final __struct__<pointf> p) {
// WARNING!! STRUCT
return map_point_w_(p.copy()).copy();
}
private static __struct__<pointf> map_point_w_(final __struct__<pointf> p) {
ENTERING("dajapw16wus3rwimkrk5ihi2b","map_point");
try {
    p.____(ccwrotatepf(p, Z.z().Rankdir * 90));
    p.setDouble("x", p.getDouble("x") - Z.z().Offset.getDouble("x"));
    p.setDouble("y", p.getDouble("y") - Z.z().Offset.getDouble("y"));
    return p;
} finally {
LEAVING("dajapw16wus3rwimkrk5ihi2b","map_point");
}
}




//3 bvq3vvonvotn47mfe5zsvchie
// static void map_edge(edge_t * e) 
public static void map_edge(Agedge_s e) {
ENTERING("bvq3vvonvotn47mfe5zsvchie","map_edge");
try {
    int j, k;
    final __struct__<bezier> bz = __struct__.from(bezier.class);
    if (ED_spl(e) == null) {
	if ((Z.z().Concentrate == false) && (ED_edge_type(e) != 6))
	    System.err.println("lost %s %s edge\n"+ agnameof(agtail(e))+
		  agnameof(aghead(e)));
	return;
    }
    for (j = 0; j < ED_spl(e).getInt("size"); j++) {
	bz.____(ED_spl(e).getArrayOfPtr("list").plus(j).getStruct());
	for (k = 0; k < bz.getInt("size"); k++) {
	    bz.getArrayOfPtr("list").plus(k).setStruct(map_point(bz.getArrayOfPtr("list").plus(k).getStruct()));
	}
	if (bz.getBoolean("sflag"))
UNSUPPORTED("7894dgzvk2um2w1a5ph2r0bcc"); // 	    ED_spl(e)->list[j].sp = map_point(ED_spl(e)->list[j].sp);
	if (bz.getBoolean("eflag")) {
	    ED_spl(e).getArrayOfPtr("list").plus(j).getStruct("ep").____(map_point(ED_spl(e).getArrayOfPtr("list").plus(j).getStruct("ep")));
    }
    }
    if (ED_label(e)!=null)
	ED_label(e).getStruct("pos").____(map_point(ED_label(e).getStruct("pos")));
    if (ED_xlabel(e)!=null)
UNSUPPORTED("al3tnq9zjjqeq1ll7qdxyu3ja"); // 	ED_xlabel(e)->pos = map_point(ED_xlabel(e)->pos);
    /* vladimir */
    if (ED_head_label(e)!=null)
UNSUPPORTED("6ntujaf13k6emf6cuf7ox8ath"); // 	ED_head_label(e)->pos = map_point(ED_head_label(e)->pos);
    if (ED_tail_label(e)!=null)
UNSUPPORTED("2is3ug7jbugrkl9bu6nfnz2lt"); // 	ED_tail_label(e)->pos = map_point(ED_tail_label(e)->pos);
} finally {
LEAVING("bvq3vvonvotn47mfe5zsvchie","map_edge");
}
}




//3 a3hf82rxsojxbunj6p8a6bkse
// void translate_bb(graph_t * g, int rankdir) 
public static void translate_bb(Agraph_s g, int rankdir) {
ENTERING("a3hf82rxsojxbunj6p8a6bkse","translate_bb");
try {
    int c;
    final __struct__<boxf> bb = __struct__.from(boxf.class), new_bb = __struct__.from(boxf.class);
    bb.____(GD_bb(g));
    if (rankdir == 1 || rankdir == 2) {
UNSUPPORTED("d4wrtj0h7lkb0e0vernd9czq9"); // 	new_bb.LL = map_point(pointfof(bb.LL.x, bb.UR.y));
UNSUPPORTED("crysiae5zxc69cj3v2ygfs8xn"); // 	new_bb.UR = map_point(pointfof(bb.UR.x, bb.LL.y));
    } else {
	new_bb.getStruct("LL").____(map_point(pointfof(bb.getStruct("LL").getDouble("x"), bb.getStruct("LL").getDouble("y"))));
	new_bb.getStruct("UR").____(map_point(pointfof(bb.getStruct("UR").getDouble("x"), bb.getStruct("UR").getDouble("y"))));
    }
    GD_bb(g).____(new_bb);
    if (GD_label(g)!=null) {
	GD_label(g).setStruct("pos", map_point(GD_label(g).getStruct("pos")));
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	translate_bb((Agraph_s) GD_clust(g).plus(c).getPtr(), rankdir);
} finally {
LEAVING("a3hf82rxsojxbunj6p8a6bkse","translate_bb");
}
}




//3 h4i5qxnd7hlrew919abswd13
// static void translate_drawing(graph_t * g) 
public static void translate_drawing(Agraph_s g) {
ENTERING("h4i5qxnd7hlrew919abswd13","translate_drawing");
try {
    Agnode_s v;
    Agedge_s e;
    boolean shift = (Z.z().Offset.getDouble("x")!=0.0 || Z.z().Offset.getDouble("y")!=0.0);
    if (N(shift) && N(Z.z().Rankdir))
	return;
    for (v = agfstnode(g); v!=null; v = agnxtnode(g, v)) {
	if (Z.z().Rankdir!=0)
UNSUPPORTED("e0j848r4j1j7sojfht5gwikvi"); // 	    gv_nodesize(v, 0);
	ND_coord(v).____(map_point(ND_coord(v)));
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




//3 52s5qfdn5zxeyuodfq8tgxwaa
// static void place_root_label(graph_t * g, pointf d) 
public static Object place_root_label(Object... arg) {
UNSUPPORTED("8m0j9ogp2h20aphxjuxn7copg"); // static void place_root_label(graph_t * g, pointf d)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("2gqofhemdqlul2pxjvuyly8gr"); //     if (GD_label_pos(g) & 4) {
UNSUPPORTED("5y20n2wgxe7hnzxnkg9144bhl"); // 	p.x = GD_bb(g).UR.x - d.x / 2;
UNSUPPORTED("2892hrfudeikl7o70w4nofhny"); //     } else if (GD_label_pos(g) & 2) {
UNSUPPORTED("2wafl9f5ytdr13m270aknhkub"); // 	p.x = GD_bb(g).LL.x + d.x / 2;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("aktxwmc3uefegw0cs334i9dw8"); // 	p.x = (GD_bb(g).LL.x + GD_bb(g).UR.x) / 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dpev8t1rdycpdnwjscmowsvaf"); //     if (GD_label_pos(g) & 1) {
UNSUPPORTED("1d6n5uhjoy1y3mfj3ddrdt6rc"); // 	p.y = GD_bb(g).UR.y - d.y / 2;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("d6z40q4ju0vq45te2z2prcsua"); // 	p.y = GD_bb(g).LL.y + d.y / 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dgs75zvkk4komxfg61cia1t82"); //     GD_label(g)->pos = p;
UNSUPPORTED("8dkewv20cdne73egfv2tn37lb"); //     GD_label(g)->set = NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2i713kmewjct2igf3lwm80462
// static pointf centerPt (xlabel_t* xlp) 
public static Object centerPt(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("1pd2hqj3zbktacr9dt2vdvkgr"); // centerPt (xlabel_t* xlp) {
UNSUPPORTED("3f7r93jimpwvyc6atnkppttgl"); //   pointf p;
UNSUPPORTED("6jtpe3khjpc9oogxx6kerapem"); //   p = xlp->pos;
UNSUPPORTED("7rsewd63gsf3h9d5aj5v7x66c"); //   p.x += (xlp->sz.x)/2.0;
UNSUPPORTED("3c70xgshcb3nvyi64kr041yjz"); //   p.y += (xlp->sz.y)/2.0;
UNSUPPORTED("bft6601q2uop0mu5y59jg4c81"); //   return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2ozwtuocg4cz5ghqemmche3vt
// static int printData (object_t* objs, int n_objs, xlabel_t* lbls, int n_lbls, 	   label_params_t* params) 
public static Object printData(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("ckiu0galpa1852h3cty8ba6r7"); // printData (object_t* objs, int n_objs, xlabel_t* lbls, int n_lbls,
UNSUPPORTED("dzs60ka6anviw625w47fdcq2z"); // 	   label_params_t* params) {
UNSUPPORTED("327a0i7b6rqjqj8wdl0h29mnc"); //   int i;
UNSUPPORTED("2v2z0ng0lije7nd5xnnwpvhsk"); //   xlabel_t* xp;
UNSUPPORTED("8b4vu28i0jzto8xj8y5xidmfi"); //   fprintf (stderr, "%d objs %d xlabels force=%d bb=(%.02f,%.02f) (%.02f,%.02f)\n",
UNSUPPORTED("4brfvoc74eb0wrjbv0jim84br"); // 	   n_objs, n_lbls, params->force, params->bb.LL.x, params->bb.LL.y,
UNSUPPORTED("bg9iybxqjpzhrq7ake4qd022a"); // 	   params->bb.UR.x, params->bb.UR.y);
UNSUPPORTED("cqr6z8w761iogdatyx6tujxrj"); //   if (Verbose < 2) return 0;
UNSUPPORTED("3k661zowi8f52at88thx4gq3c"); //   fprintf(stderr, "objects\n");
UNSUPPORTED("a0du2scf7lcyviw12x1mpntj5"); //   for (i = 0; i < n_objs; i++) {
UNSUPPORTED("7ljpm4lk7dhgz8jneyiurqcqj"); //     xp = objs->lbl;
UNSUPPORTED("6synbitl0b8qre39c8hfx9ji7"); //     fprintf (stderr, " [%d] (%.02f,%.02f) (%.02f,%.02f) %p \"%s\"\n",
UNSUPPORTED("5fncs0x8f05ep5nr4lds4lyfi"); // 	    i, objs->pos.x,objs->pos.y,objs->sz.x,objs->sz.y, objs->lbl, 
UNSUPPORTED("csy4r7zdyqrnbp3yvfpm66w59"); // 	    (xp?((textlabel_t*)(xp->lbl))->text:""));
UNSUPPORTED("bd3gftycfqozopsoe1ptqueov"); //     objs++;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("2kq3xkqpgi5kr4141ab3bukkd"); //   fprintf(stderr, "xlabels\n");
UNSUPPORTED("9dzxzlqrz9th6wohjjvd93rtd"); //   for (i = 0; i < n_lbls; i++) {
UNSUPPORTED("6e093u52b50denwzlx0m7a36a"); //     fprintf (stderr, " [%d] %p set %d (%.02f,%.02f) (%.02f,%.02f) %s\n",
UNSUPPORTED("bb908gbwccyotc72hfeopef2e"); // 	     i, lbls,  lbls->set, lbls->pos.x,lbls->pos.y, lbls->sz.x,lbls->sz.y, ((textlabel_t*)lbls->lbl)->text);  
UNSUPPORTED("30hn2ulrrh2sxlt3rd89e1nja"); //     lbls++;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("bid671dovx1rdiquw5vm3fttj"); //   return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 95pnpuiq4khinrz2bqkci9nfg
// static pointf edgeTailpoint (Agedge_t* e) 
public static Object edgeTailpoint(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("b5i1gh69zn27sn9j8kpmvtbeb"); // edgeTailpoint (Agedge_t* e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6tl9mepc2bett364jduh2q4mf"); //     splines *spl;
UNSUPPORTED("3hs99atzl1l857khumt6ycmbh"); //     bezier *bez;
UNSUPPORTED("26m18ntdxgq9wp5vlh2x7auh5"); //     if ((spl = getsplinepoints(e)) == NULL) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("ezy0ey6dn5uqp6peuorn615x6"); // 	p.x = p.y = 0;
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4djdvk87infum4r43tuhnm5mp"); //     bez = &spl->list[0];
UNSUPPORTED("45rq0m21hutb3z6f4npw7ke9f"); //     if (bez->sflag) {
UNSUPPORTED("9ttezx014gi1oy9xopnihsnac"); // 	return bez->sp;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("c00cyqleu301qclgim7szyf7"); // 	return bez->list[0];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 av67wf2xi70ncgl90j1ttrjjs
// static pointf edgeHeadpoint (Agedge_t* e) 
public static Object edgeHeadpoint(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("ckf4uk77aptax4a60w2nhrdzl"); // edgeHeadpoint (Agedge_t* e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6tl9mepc2bett364jduh2q4mf"); //     splines *spl;
UNSUPPORTED("3hs99atzl1l857khumt6ycmbh"); //     bezier *bez;
UNSUPPORTED("26m18ntdxgq9wp5vlh2x7auh5"); //     if ((spl = getsplinepoints(e)) == NULL) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("ezy0ey6dn5uqp6peuorn615x6"); // 	p.x = p.y = 0;
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("mjw3aaq1rghmemky1iymklp4"); //     bez = &spl->list[spl->size - 1];
UNSUPPORTED("5g1i4pbq9il9iba3urs6bxfa0"); //     if (bez->eflag) {
UNSUPPORTED("5vt6gwb8d8689fuwqbt5uhb12"); // 	return bez->ep;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6qzm0hh4pxrspfbvxearcz9z8"); // 	return bez->list[bez->size - 1];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ca6fh8ns5bgzfzcz8al4eh4k
// static boxf adjustBB (object_t* objp, boxf bb) 
public static Object adjustBB(Object... arg) {
UNSUPPORTED("d5qt6s97burjfu5qe0oxyyrmr"); // static boxf
UNSUPPORTED("2gtud943baz3kfj1vqqhjeaj4"); // adjustBB (object_t* objp, boxf bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ilrhzyqh05f2u3j3vzg0ys8u"); //     pointf ur;
UNSUPPORTED("a4qac74i3mtrli231q9zmy8pn"); //     /* Adjust bounding box */
UNSUPPORTED("cgmwicrpoafwh8qt3zob8r2ye"); //     bb.LL.x = MIN(bb.LL.x, objp->pos.x);
UNSUPPORTED("dqb32jehkpiyfzbiwusv0ex1n"); //     bb.LL.y = MIN(bb.LL.y, objp->pos.y);
UNSUPPORTED("coywjj9bhu737b59inwiumkbc"); //     ur.x = objp->pos.x + objp->sz.x;
UNSUPPORTED("4zg7x4gv3ox92n323b2vzaq32"); //     ur.y = objp->pos.y + objp->sz.y;
UNSUPPORTED("p351si3o2tnvdcb1o5i8et1b"); //     bb.UR.x = MAX(bb.UR.x, ur.x);
UNSUPPORTED("4a11bd6b7vdmcwc7r71y9k37z"); //     bb.UR.y = MAX(bb.UR.y, ur.y);
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3mefe722uemyoa0czmkkw6hjb
// static void addXLabel (textlabel_t* lp, object_t* objp, xlabel_t* xlp, int initObj, pointf pos) 
public static Object addXLabel(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("as13cbda9pe3uxi34emdcyw49"); // addXLabel (textlabel_t* lp, object_t* objp, xlabel_t* xlp, int initObj, pointf pos)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("47i4tbdfy05npmfnxoa2ljezv"); //     if (initObj) {
UNSUPPORTED("4brnnjtxt9czl9vlvf1hi62hq"); // 	objp->sz.x = 0;
UNSUPPORTED("8vps14u07wyyud2ryypqvjgog"); // 	objp->sz.y = 0;
UNSUPPORTED("eiw9ykn654ml54rs1bw0lf55b"); // 	objp->pos = pos;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7e3xcf5dxjm2knry7yygcx3zv"); //     if (Flip) {
UNSUPPORTED("99tzt7erbvtfsbo0jbdz0lc8m"); // 	xlp->sz.x = lp->dimen.y;
UNSUPPORTED("6v5t3ysaisj27bwc0r9zg3rpd"); // 	xlp->sz.y = lp->dimen.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("3fr3ccpgshd8wywufcfat4rf5"); // 	xlp->sz = lp->dimen;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("jd8wxj8hvqwupmayd743a386"); //     xlp->lbl = lp;
UNSUPPORTED("1z3b8kb1emm8lvcqvu8sm8r7j"); //     xlp->set = 0;
UNSUPPORTED("a1r2pwxgnrpltol41p04axtld"); //     objp->lbl = xlp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dwxd5kvlanbcxqfuncjg0ea54
// static boxf addLabelObj (textlabel_t* lp, object_t* objp, boxf bb) 
public static Object addLabelObj(Object... arg) {
UNSUPPORTED("d5qt6s97burjfu5qe0oxyyrmr"); // static boxf
UNSUPPORTED("9yg6tco97jfdkxvya77inw8xx"); // addLabelObj (textlabel_t* lp, object_t* objp, boxf bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7e3xcf5dxjm2knry7yygcx3zv"); //     if (Flip) {
UNSUPPORTED("6z2yrwq81gtsk3q9c5pofow1x"); // 	objp->sz.x = lp->dimen.y; 
UNSUPPORTED("8xsm9kavrekjrsydqe1wh1pu"); // 	objp->sz.y = lp->dimen.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("40zw1ce6j4iw8dzvp9musrk6g"); // 	objp->sz.x = lp->dimen.x; 
UNSUPPORTED("3kmv74u3ihq63ptaixci1tlt5"); // 	objp->sz.y = lp->dimen.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ekb3vmkpcdegpnlbuvebyijm8"); //     objp->pos = lp->pos;
UNSUPPORTED("6c49iw60twquhten0558iva1c"); //     objp->pos.x -= (objp->sz.x) / 2.0;
UNSUPPORTED("alrfakfz0nqemd3xl3m9q2gm4"); //     objp->pos.y -= (objp->sz.y) / 2.0;
UNSUPPORTED("5r3oym45e21gwxn3nsjucxlbi"); //     return adjustBB(objp, bb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b8tjygxnwny5qoiir1mha1d62
// static boxf addNodeObj (node_t* np, object_t* objp, boxf bb) 
public static Object addNodeObj(Object... arg) {
UNSUPPORTED("d5qt6s97burjfu5qe0oxyyrmr"); // static boxf
UNSUPPORTED("cdh6qqtv45t605q7je7xomi2j"); // addNodeObj (node_t* np, object_t* objp, boxf bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7e3xcf5dxjm2knry7yygcx3zv"); //     if (Flip) {
UNSUPPORTED("1ri5uimcd1z58iix8tp528l1m"); // 	objp->sz.x = ((ND_height(np))*(double)72);
UNSUPPORTED("6r5gwwhz3sjxrssh8yo3v5c3v"); // 	objp->sz.y = ((ND_width(np))*(double)72);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("6cuxjl9g4nxwyz58c201qdb94"); // 	objp->sz.x = ((ND_width(np))*(double)72);
UNSUPPORTED("e3zk2j9kbexxv2xbsgu3pser6"); // 	objp->sz.y = ((ND_height(np))*(double)72);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d6gkdf4gkfagwtb6mkhxxvqrc"); //     objp->pos = ND_coord(np);
UNSUPPORTED("6c49iw60twquhten0558iva1c"); //     objp->pos.x -= (objp->sz.x) / 2.0;
UNSUPPORTED("alrfakfz0nqemd3xl3m9q2gm4"); //     objp->pos.y -= (objp->sz.y) / 2.0;
UNSUPPORTED("5r3oym45e21gwxn3nsjucxlbi"); //     return adjustBB(objp, bb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6kx3lin2ig9o2otk2bqzdvd4t
// static cinfo_t addClusterObj (Agraph_t* g, cinfo_t info) 
public static Object addClusterObj(Object... arg) {
UNSUPPORTED("91ncv8p43nko0ygysclvv77j"); // static cinfo_t
UNSUPPORTED("bmfjbc1td1mizemu2aa81cyli"); // addClusterObj (Agraph_t* g, cinfo_t info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("6o81thi0rqvkah0s4zkn2fcg4"); // 	info = addClusterObj (GD_clust(g)[c], info);
UNSUPPORTED("1ke0hve63v76yb4shi7jfrp6x"); //     if ((g != agroot(g)) && (GD_label(g)) && GD_label(g)->set) {
UNSUPPORTED("dcgq2zlh4t0m1gno12t6h7ouy"); // 	object_t* objp = info.objp;
UNSUPPORTED("ddz79zm5235krd6smukq1gza0"); // 	info.bb = addLabelObj (GD_label(g), objp, info.bb);
UNSUPPORTED("be25qc3x3muxo4l87ji01t3kd"); // 	info.objp++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9kt6o7m6t7fgdh41zfez84fmv"); //     return info;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2tdbzvdtkwxp75kj0iufsynm5
// static int countClusterLabels (Agraph_t* g) 
public static Object countClusterLabels(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("6o4fsu24jc0ezulf31fsi9bce"); // countClusterLabels (Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2q4dlnxpy4lj011whzcbyq3xj"); //     int c, i = 0;
UNSUPPORTED("68odyos1g0n4tbs7c77r0f9wn"); //     if ((g != agroot(g)) && (GD_label(g)) && GD_label(g)->set)
UNSUPPORTED("chd2f5z6rt19lbaye25ej7q6j"); // 	i++;
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("adgz5sd2oklf51or5uq3wduuz"); // 	i += countClusterLabels (GD_clust(g)[c]);
UNSUPPORTED("ahwo5hst5k1gyq20ve63ahe81"); //     return i;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d4215jd9wukfn6t0iknwzjcof
// static void addXLabels(Agraph_t * gp) 
public static void addXLabels(Agraph_s gp) {
ENTERING("d4215jd9wukfn6t0iknwzjcof","addXLabels");
try {
    Agnode_s np;
    Agedge_s ep;
    int cnt, i, n_objs, n_lbls;
    int n_nlbls = 0;		/* # of unset node xlabels */
    int n_elbls = 0;		/* # of unset edge labels or xlabels */
    int n_set_lbls = 0;		/* # of set xlabels and edge labels */
    int n_clbls = 0;		/* # of set cluster labels */
    final __struct__<boxf> bb = __struct__.from(boxf.class);
    final __struct__<pointf> ur = __struct__.from(pointf.class);
    textlabel_t lp;
    final __struct__<label_params_t> params = __struct__.from(label_params_t.class);
    object_t objs;
    xlabel_t lbls;
    object_t objp;
    xlabel_t xlp;
    Agsym_s force;
    int et = (GD_flags(gp) & (7 << 1));
    if (N(GD_has_labels(gp) & (1 << 4)) &&
	N(GD_has_labels(gp) & (1 << 5)) &&
	N(GD_has_labels(gp) & (1 << 2)) &&
	N(GD_has_labels(gp) & (1 << 1)) &&
	(N(GD_has_labels(gp) & (1 << 0)) || Z.z().EdgeLabelsDone!=0))
	return;
UNSUPPORTED("27ppdplfezcqw6rdrkzyrr8yg"); //     for (np = agfstnode(gp); np; np = agnxtnode(gp, np)) {
UNSUPPORTED("eezvruvdh9ueqsgad8k5xzbqi"); // 	if (ND_xlabel(np)) {
UNSUPPORTED("6oje33bnpp4jv5mclsrrhl005"); // 	    if (ND_xlabel(np)->set)
UNSUPPORTED("cfkrw6t4lrs7dfgx86sgrz26"); // 		n_set_lbls++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("26eewzzknvqt2nbcrqds5fmti"); // 		n_nlbls++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("41dpbku41lh3gpb30ds9ex6aa"); // 	for (ep = agfstout(gp, np); ep; ep = agnxtout(gp, ep)) {
UNSUPPORTED("9c5vwy3kfweqsgk827cpj3d6q"); // 	    if (ED_xlabel(ep)) {
UNSUPPORTED("appkettxihy2o612jk6fahbnh"); // 		if (ED_xlabel(ep)->set)
UNSUPPORTED("8k2rclvg6eaoph9r2pz4620xq"); // 		    n_set_lbls++;
UNSUPPORTED("14y6caappoxe17mogr979qf75"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL)))
UNSUPPORTED("q3t8uxncrxc4n8rtuabtzxya"); // 		    n_elbls++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("1mk50yh8pbs8jzn7h8otaonfd"); // 	    if (ED_head_label(ep)) {
UNSUPPORTED("4xstfnjw4gi0ja4inv8o0n8z"); // 		if (ED_head_label(ep)->set)
UNSUPPORTED("8k2rclvg6eaoph9r2pz4620xq"); // 		    n_set_lbls++;
UNSUPPORTED("14y6caappoxe17mogr979qf75"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL)))
UNSUPPORTED("q3t8uxncrxc4n8rtuabtzxya"); // 		    n_elbls++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4micw28bcx68bfiqihi9ruani"); // 	    if (ED_tail_label(ep)) {
UNSUPPORTED("abwl715n01quq34u2qs1kn9xn"); // 		if (ED_tail_label(ep)->set)
UNSUPPORTED("8k2rclvg6eaoph9r2pz4620xq"); // 		    n_set_lbls++;
UNSUPPORTED("14y6caappoxe17mogr979qf75"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL)))
UNSUPPORTED("q3t8uxncrxc4n8rtuabtzxya"); // 		    n_elbls++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("92f340ohb0u21xl6jgpc2hieo"); // 	    if (ED_label(ep)) {
UNSUPPORTED("5skvrpmqqjq5cj6a8uiylmjsw"); // 		if (ED_label(ep)->set)
UNSUPPORTED("8k2rclvg6eaoph9r2pz4620xq"); // 		    n_set_lbls++;
UNSUPPORTED("14y6caappoxe17mogr979qf75"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL)))
UNSUPPORTED("q3t8uxncrxc4n8rtuabtzxya"); // 		    n_elbls++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2uqg2vrduqxs9hk9hmamj7wlu"); //     if (GD_has_labels(gp) & (1 << 3))
UNSUPPORTED("4z3bgexjlrmdrfgpdaajems8q"); // 	n_clbls = countClusterLabels (gp);
UNSUPPORTED("19labxj7f93ljlhkpmrde0zgt"); //     /* A label for each unpositioned external label */
UNSUPPORTED("5kla6rwar4q1ig2olguocajn6"); //     n_lbls = n_nlbls + n_elbls;
UNSUPPORTED("apjqar4agmf5jlfd88kbn4f4d"); //     if (n_lbls == 0) return;
UNSUPPORTED("4j54hc42lgojcuorb9p42tlr"); //     /* An object for each node, each positioned external label, any cluster label, 
UNSUPPORTED("adxf9gd6tasafb302px9vqgum"); //      * and all unset edge labels and xlabels.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("6id92o3db6qu5zuuj8q40n3zn"); //     n_objs = agnnodes(gp) + n_set_lbls + n_clbls + n_elbls;
UNSUPPORTED("3e10kvi719e134x675xgwi1tj"); //     objp = objs = (object_t*)zmalloc((n_objs)*sizeof(object_t));
UNSUPPORTED("6z86cmeenod2nx8ej72n0qotk"); //     xlp = lbls = (xlabel_t*)zmalloc((n_lbls)*sizeof(xlabel_t));
UNSUPPORTED("f2nzvg1xnr11v28w2feg923cs"); //     bb.LL = pointfof(INT_MAX, INT_MAX);
UNSUPPORTED("7tttoj8cnxfqgnq2aagnnav48"); //     bb.UR = pointfof(-INT_MAX, -INT_MAX);
UNSUPPORTED("27ppdplfezcqw6rdrkzyrr8yg"); //     for (np = agfstnode(gp); np; np = agnxtnode(gp, np)) {
UNSUPPORTED("38hh82sue091x6ybm9e34wy61"); // 	bb = addNodeObj (np, objp, bb);
UNSUPPORTED("ex5hwora23t1cl8hpjo4uvphm"); // 	if ((lp = ND_xlabel(np))) {
UNSUPPORTED("d5pjy3dwui27jfdz550cy0cln"); // 	    if (lp->set) {
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
UNSUPPORTED("3zy3jhlqyioeyh9mlrspjjgc6"); // 		bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2msn58w2dse7pbq2esv7awk4r"); // 		addXLabel (lp, objp, xlp, 0, ur); 
UNSUPPORTED("1zpq9rd3nn9kjrmun8ivs9zx5"); // 		xlp++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bhbvoj12subdn6905juhiubo2"); // 	objp++;
UNSUPPORTED("41dpbku41lh3gpb30ds9ex6aa"); // 	for (ep = agfstout(gp, np); ep; ep = agnxtout(gp, ep)) {
UNSUPPORTED("9zaprre819fwswan5wvid0h6g"); // 	    if ((lp = ED_label(ep))) {
UNSUPPORTED("5dapykbxjvnhw0dpi7jfpcazk"); // 		if (lp->set) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dfnmpe0hri6ksye0gnxssi4zz"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL))) {
UNSUPPORTED("9ffmrymv8cg4h4b3ea97t9qbp"); // 		    addXLabel (lp, objp, xlp, 1, edgeMidpoint(gp, ep)); 
UNSUPPORTED("808184nt3k6cxj5dsg27yvpvg"); // 		    xlp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("3ia66n3hqrwmh3hybkoh6f8wa"); // 		    agerr(AGWARN, "no position for edge with label %s",
UNSUPPORTED("9npeksy1st7v005znerttzzzv"); // 			    ED_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d23ocobgp22a33eopdnqe9o4u"); // 	        objp++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("a5mn7dpum21w95ku1l27m9rpk"); // 	    if ((lp = ED_tail_label(ep))) {
UNSUPPORTED("5dapykbxjvnhw0dpi7jfpcazk"); // 		if (lp->set) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dfnmpe0hri6ksye0gnxssi4zz"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL))) {
UNSUPPORTED("bqc6ukxlmt6l3osbpsmqbzutc"); // 		    addXLabel (lp, objp, xlp, 1, edgeTailpoint(ep)); 
UNSUPPORTED("808184nt3k6cxj5dsg27yvpvg"); // 		    xlp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("5ixexxcbcix5hrfl43td7pj4s"); // 		    agerr(AGWARN, "no position for edge with tail label %s",
UNSUPPORTED("cf9qaysecgkvv4165la4uu6cb"); // 			    ED_tail_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6rvkuvq5nj7p2za2zqxf74l3c"); // 	    if ((lp = ED_head_label(ep))) {
UNSUPPORTED("5dapykbxjvnhw0dpi7jfpcazk"); // 		if (lp->set) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dfnmpe0hri6ksye0gnxssi4zz"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL))) {
UNSUPPORTED("7gewvtwwzj3unxzrtbqpwduzg"); // 		    addXLabel (lp, objp, xlp, 1, edgeHeadpoint(ep)); 
UNSUPPORTED("808184nt3k6cxj5dsg27yvpvg"); // 		    xlp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("8nrkavpg9ifts9yylhfijn9rp"); // 		    agerr(AGWARN, "no position for edge with head label %s",
UNSUPPORTED("a5omwtwd411hsfrc37d8t6m8b"); // 			    ED_head_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("1pb88fhz51khiwboqgii8qayw"); // 	    if ((lp = ED_xlabel(ep))) {
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
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5zntyu3vcfnjveucnumrmbjig"); //     if (n_clbls) {
UNSUPPORTED("48ipxdlv7xlti99g0yhi5zuai"); // 	cinfo_t info;
UNSUPPORTED("7c5iohb8t706p273ae1lxal8r"); // 	info.bb = bb;
UNSUPPORTED("b1474fakrbyw7p5ja42jgv90c"); // 	info.objp = objp;
UNSUPPORTED("6ygw8idplugc5u6w7ro3gakmb"); // 	info = addClusterObj (gp, info);
UNSUPPORTED("1l2cwgzediv4hztjhtm052rl3"); // 	bb = info.bb;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("79ifzl4zpx3wbd5szboa2x2vb"); //     force = (agattr(gp,AGRAPH,"forcelabels",NULL));
UNSUPPORTED("1if7tdbcs4rwvb0polsdlbfh4"); //     params.force = late_bool(gp, force, NOT(0));
UNSUPPORTED("2ol916ffwy0e2vxinxn4v4sgt"); //     params.bb = bb;
UNSUPPORTED("25rb35acbkepp55u3bkjxb1gc"); //     placeLabels(objs, n_objs, lbls, n_lbls, &params);
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("4iypau1fdov37qnq2ub6iq5ra"); // 	printData(objs, n_objs, lbls, n_lbls, &params);
UNSUPPORTED("52mefujap7scy273ud7nyj9hn"); //     xlp = lbls;
UNSUPPORTED("3wtn792c3ql5yhn77alu6r5d8"); //     cnt = 0;
UNSUPPORTED("30yvif5t111f94y1fs2gd8crq"); //     for (i = 0; i < n_lbls; i++) {
UNSUPPORTED("w0kpfap6pb5scjkqkgsfira0"); // 	if (xlp->set) {
UNSUPPORTED("7hl03wjg5yryhvbe4ar0i0b8g"); // 	    cnt++;
UNSUPPORTED("8xqwhcveb6ivragr1ebkp4pfh"); // 	    lp = (textlabel_t *) (xlp->lbl);
UNSUPPORTED("a5h8ktnl3raui7zo5kcjzd2e0"); // 	    lp->set = 1;
UNSUPPORTED("4lub8ddx8vt0gove63lajjr4s"); // 	    lp->pos = centerPt(xlp);
UNSUPPORTED("d613i6370zjpynl7n5caiorig"); // 	    updateBB (gp, lp);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1966qdxqc520zc0itk8al0xus"); // 	xlp++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("dy42jv2urndusl1b1jrir300t"); // 	fprintf (stderr, "%d out of %d labels positioned.\n", cnt, n_lbls);
UNSUPPORTED("1jtgut2015ohnwt6qfisxgbs5"); //     else if (cnt != n_lbls)
UNSUPPORTED("9hqu9h8q1a2xl4ty48ct0fdyp"); // 	agerr(AGWARN, "%d out of %d exterior labels positioned.\n", cnt, n_lbls);
UNSUPPORTED("baez6nmarx9nht65vulvjojic"); //     free(objs);
UNSUPPORTED("ayak2o9js1lmsa5vuzul2sdxs"); //     free(lbls);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("d4215jd9wukfn6t0iknwzjcof","addXLabels");
}
}




//3 8fc0zxg8y7hec3n4evx3jw6cq
// void gv_postprocess(Agraph_t * g, int allowTranslation) 
public static void gv_postprocess(Agraph_s g, int allowTranslation) {
ENTERING("8fc0zxg8y7hec3n4evx3jw6cq","gv_postprocess");
try {
    double diff;
    final __struct__<pointf> dimen = __struct__.from(pointf.class);
    Z.z().Rankdir = GD_rankdir(g);
    Z.z().Flip = GD_flip(g)!=0;
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
    if (GD_label(g)!=null && N(GD_label(g).getPtr("set"))) {
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
	    Z.z().Offset.____(GD_bb(g).getStruct("LL"));
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
    if (GD_label(g)!=null && N(GD_label(g).getPtr("set")))
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
public static void dotneato_postprocess(Agraph_s g) {
ENTERING("3qbbvlnq1b06ylgr0yj2slbhm","dotneato_postprocess");
try {
    gv_postprocess(g, 1);
} finally {
LEAVING("3qbbvlnq1b06ylgr0yj2slbhm","dotneato_postprocess");
}
}




//3 ehe7n8wkl4thn86tisjzdotpq
// static void place_flip_graph_label(graph_t * g) 
public static Object place_flip_graph_label(Object... arg) {
UNSUPPORTED("3zsjtcmcfxhkmaagi0on4dy20"); // static void place_flip_graph_label(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("1wiyv8zjadpaaw6l7rs3o7g1w"); //     pointf p, d;
UNSUPPORTED("3aye33sd5gwxvun5g9nvgb2py"); //     if ((g != agroot(g)) && (GD_label(g)) && !GD_label(g)->set) {
UNSUPPORTED("bb9kbz7bijh4xjt97fdn2q90k"); // 	if (GD_label_pos(g) & 1) {
UNSUPPORTED("9fqnrxwwa66oa8qe31y1gf37u"); // 	    d = GD_border(g)[1];
UNSUPPORTED("cgv3bcg9c274cdwxi1y0sja3p"); // 	    p.x = GD_bb(g).UR.x - d.x / 2;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("16roor3488xb2g2wr6oh8dqpx"); // 	    d = GD_border(g)[3];
UNSUPPORTED("7ictv9eqmjvxjii5lqlyw8nu"); // 	    p.x = GD_bb(g).LL.x + d.x / 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2qpji9cqj2p2czgcug3wvnqpl"); // 	if (GD_label_pos(g) & 4) {
UNSUPPORTED("2xa4n9ca16xpf1kahaycmkl4r"); // 	    p.y = GD_bb(g).LL.y + d.y / 2;
UNSUPPORTED("blrmgi2c43f98h1nso1k757hi"); // 	} else if (GD_label_pos(g) & 2) {
UNSUPPORTED("a7anlx7s8s2pqd73q59ep0kpf"); // 	    p.y = GD_bb(g).UR.y - d.y / 2;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("378pj84d79yuezjebpqcchyut"); // 	    p.y = (GD_bb(g).LL.y + GD_bb(g).UR.y) / 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ptjqgn3loi94u957cup0fi1"); // 	GD_label(g)->pos = p;
UNSUPPORTED("5ezl5j9dxa3ewoj8hxw72wn4n"); // 	GD_label(g)->set = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("d388zyk4c9le0jg4fcfx0b2"); // 	place_flip_graph_label(GD_clust(g)[c]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 72zw1alhd5vd0g6mhum507rvx
// void place_graph_label(graph_t * g) 
public static void place_graph_label(Agraph_s g) {
ENTERING("72zw1alhd5vd0g6mhum507rvx","place_graph_label");
try {
    int c;
    final __struct__<pointf> p = __struct__.from(pointf.class), d = __struct__.from(pointf.class);
    if (NEQ(g, agroot(g)) && (GD_label(g)!=null) && N(GD_label(g).getInt("set"))) {
	if ((GD_label_pos(g) & 1)!=0) {
	    d.____(GD_border(g).plus(2).getStruct());
	    p.setDouble("y", GD_bb(g).getStruct("UR").getDouble("y") - d.getDouble("y") / 2);
	} else {
UNSUPPORTED("1w38no4welthbwa0i10hei16b"); // 	    d = GD_border(g)[0];
UNSUPPORTED("2xa4n9ca16xpf1kahaycmkl4r"); // 	    p.y = GD_bb(g).LL.y + d.y / 2;
	}
	if ((GD_label_pos(g) & 4)!=0) {
UNSUPPORTED("cgv3bcg9c274cdwxi1y0sja3p"); // 	    p.x = GD_bb(g).UR.x - d.x / 2;
	} else if ((GD_label_pos(g) & 2)!=0) {
UNSUPPORTED("7ictv9eqmjvxjii5lqlyw8nu"); // 	    p.x = GD_bb(g).LL.x + d.x / 2;
	} else {
	    p.setDouble("x", (GD_bb(g).getStruct("LL").getDouble("x") + GD_bb(g).getStruct("UR").getDouble("x")) / 2);
	}
	GD_label(g).setStruct("pos", p);
	GD_label(g).setBoolean("set", NOT(false));
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	place_graph_label((Agraph_s) GD_clust(g).plus(c).getPtr());
} finally {
LEAVING("72zw1alhd5vd0g6mhum507rvx","place_graph_label");
}
}


}
