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
package gen.lib.dotgen;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.mincross__c.rec_reset_vlists;
import static gen.lib.dotgen.mincross__c.rec_save_vlists;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ED_adjacent;
import static smetana.core.Macro.ED_dist;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.UNSUPPORTED;
import h.Agedge_s;
import h.Agnode_s;
import h.Agraph_s;
import h.boxf;
import h.pointf;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public class flat__c {
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




//3 e0gtvsxlvztmwu8yy44wfvf97
// static node_t *make_vn_slot(graph_t * g, int r, int pos) 
public static Object make_vn_slot(Object... arg) {
UNSUPPORTED("6m2xf6fxxaql7vem92hzjc8fp"); // static node_t *make_vn_slot(graph_t * g, int r, int pos)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("k6mzus8dzfcrn2mimw2889gf"); //     node_t **v, *n;
UNSUPPORTED("9volkyra7bou8xk4n58fmgayg"); //     v = GD_rank(g)[r].v =
UNSUPPORTED("98pm80154jco555m8h96szolv"); // 	ALLOC(GD_rank(g)[r].n + 2, GD_rank(g)[r].v, node_t *);
UNSUPPORTED("1t4az8z4cu09ad732rhmg2zs9"); //     for (i = GD_rank(g)[r].n; i > pos; i--) {
UNSUPPORTED("d653sou5aavushonpsq3rojar"); // 	v[i] = v[i - 1];
UNSUPPORTED("419bemae3pyxex9tkuozq4mcs"); // 	ND_order(v[i])++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5x919dkpsk0dy889enmq6whfw"); //     n = v[pos] = virtual_node(g);
UNSUPPORTED("coce01g8l9wwvhm336qyr01pr"); //     ND_order(n) = pos;
UNSUPPORTED("721r8n7jqzrn8p615jhj6e1nd"); //     ND_rank(n) = r;
UNSUPPORTED("3aa4n8p5f3gmsm3ncf3kt0ppb"); //     v[++(GD_rank(g)[r].n)] = NULL;
UNSUPPORTED("c5ot8tl1vuasdwmcuvxfbqr9s"); //     return v[pos];
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d64wt9oqphauv3hp4axbg2ep3
// static void findlr(node_t * u, node_t * v, int *lp, int *rp) 
public static Object findlr(Object... arg) {
UNSUPPORTED("681bcbcuif21qisamphs133uy"); // static void findlr(node_t * u, node_t * v, int *lp, int *rp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("arkr7i4jzac7qk3u7gyj5ne5f"); //     int l, r;
UNSUPPORTED("cfkl23mbdzjad4hcxx376f56l"); //     l = ND_order(u);
UNSUPPORTED("b3yo6msovxcqte14ymf7bdjyd"); //     r = ND_order(v);
UNSUPPORTED("7l6ijucuxeogy3xuz0xg9v6m8"); //     if (l > r) {
UNSUPPORTED("6rwd1uzwub1i7fdj732xxx3pz"); // 	int t = l;
UNSUPPORTED("82gycmzjz3d4nhrib9iyrg9e6"); // 	l = r;
UNSUPPORTED("db6wv16tk7pw4usb2wkcu2qoq"); // 	r = t;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8wqb8gv26n6bf4mwr5kuni32i"); //     *lp = l;
UNSUPPORTED("dg3t88wsqmpoz8cxundhay27h"); //     *rp = r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bwjjmaydx5a2fnpeoligkha0r
// static void setbounds(node_t * v, int *bounds, int lpos, int rpos) 
public static Object setbounds(Object... arg) {
UNSUPPORTED("eh12vgz846rsgrkapf0el54dq"); // static void setbounds(node_t * v, int *bounds, int lpos, int rpos)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8sfd94fmvgnux6fpkv2mh1fyr"); //     int i, l, r, ord;
UNSUPPORTED("6ag74vz3kjmohe4bp89fktey4"); //     edge_t *f;
UNSUPPORTED("6pgq59rfcqra1p4y9gfqbbe6f"); //     if (ND_node_type(v) == 1) {
UNSUPPORTED("z9v7wcosgqr4lahnn5p124fl"); // 	ord = ND_order(v);
UNSUPPORTED("5ler26nqdkle00g8mf3hmjk7y"); // 	if (ND_in(v).size == 0) {	/* flat */
UNSUPPORTED("68mw0v10p28v1l9wiev8wiwwe"); // 	    assert(ND_out(v).size == 2);
UNSUPPORTED("2p6ryz10nmc2nnb3wjeqv59u7"); // 	    findlr(aghead(ND_out(v).list[0]), aghead(ND_out(v).list[1]), &l,
UNSUPPORTED("6l0e20kyakuyzpdg48nc14sct"); // 		   &r);
UNSUPPORTED("9cryh0v2a4y3bxo8ymkchbs7o"); // 	    /* the other flat edge could be to the left or right */
UNSUPPORTED("7sgnk9izzhdr8zcxc3kr69f9k"); // 	    if (r <= lpos)
UNSUPPORTED("bnb5eabiuho72dql5vn30qj9j"); // 		bounds[2] = bounds[0] = ord;
UNSUPPORTED("epc2twtjdh3r3wo9xn28i7pyx"); // 	    else if (l >= rpos)
UNSUPPORTED("4dlbkbik5ngvtp3qnxsxmwzai"); // 		bounds[3] = bounds[1] = ord;
UNSUPPORTED("4vrvr66gereeur03utb20qgp6"); // 	    /* could be spanning this one */
UNSUPPORTED("4feswozh0ao93z431h9ujklum"); // 	    else if ((l < lpos) && (r > rpos));	/* ignore */
UNSUPPORTED("18i9llnvinqtpimsosit4m40r"); // 	    /* must have intersecting ranges */
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("acar424w2sae3hmeex20r8527"); // 		if ((l < lpos) || ((l == lpos) && (r < rpos)))
UNSUPPORTED("5uufcofg08a5z9og8eqr33rpo"); // 		    bounds[2] = ord;
UNSUPPORTED("7n9lwgeo2dn1xk207ig8ym9jh"); // 		if ((r > rpos) || ((r == rpos) && (l > lpos)))
UNSUPPORTED("5lgtzk9e0kqeju0ljn7bdjnav"); // 		    bounds[3] = ord;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5ec6i27os7k6h5wbpf1m0dw3r"); // 	} else {		/* forward */
UNSUPPORTED("dxsyfhmm2n87tu1rcz4ibn7ln"); // 	    boolean onleft, onright;
UNSUPPORTED("1tis7oczmmke48lchz9ywixrq"); // 	    onleft = onright = 0;
UNSUPPORTED("c0ju79jfpf8rwox0n1r1y87uu"); // 	    for (i = 0; (f = ND_out(v).list[i]); i++) {
UNSUPPORTED("30ffttqa49q5i7t4t5td9snec"); // 		if (ND_order(aghead(f)) <= lpos) {
UNSUPPORTED("3w4qosbmlbp893czpb4ylcpm"); // 		    onleft = NOT(0);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("c5edwmybh587pr07dirbdi31h"); // 		if (ND_order(aghead(f)) >= rpos) {
UNSUPPORTED("er4yvpcdf0st411gonh1gbqts"); // 		    onright = NOT(0);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("d1zigzczzi72fqgney93wg4dj"); // 	    if (onleft && (onright == 0))
UNSUPPORTED("e017pvlz8jlng4axrlfzpx08o"); // 		bounds[0] = ord + 1;
UNSUPPORTED("7kc4dw4aciei6s2witludqcjc"); // 	    if (onright && (onleft == 0))
UNSUPPORTED("dombbs101g2x93io2heyqos7a"); // 		bounds[1] = ord - 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3bc4otcsxj1dujj49ydbb19oa
// static int flat_limits(graph_t * g, edge_t * e) 
public static Object flat_limits(Object... arg) {
UNSUPPORTED("66a89nfyth1x37pd0k1jazsux"); // static int flat_limits(graph_t * g, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("53tr3vwgqtyyblccnlzyf011y"); //     int lnode, rnode, r, bounds[4], lpos, rpos, pos;
UNSUPPORTED("6q6egelp2q8lyq1cmef6qxo0k"); //     node_t **rank;
UNSUPPORTED("9a26ouzlqhoigdeuk7yhrabi"); //     r = ND_rank(agtail(e)) - 1;
UNSUPPORTED("4gxqkprq99assc62ze8q8xd2h"); //     rank = GD_rank(g)[r].v;
UNSUPPORTED("9gigo865vqoreozo35b83joab"); //     lnode = 0;
UNSUPPORTED("dtzzlc138kaz1nln0d8e5yvq5"); //     rnode = GD_rank(g)[r].n - 1;
UNSUPPORTED("81lya4jfbr35c9sm0wgyaukvb"); //     bounds[0] = bounds[2] = lnode - 1;
UNSUPPORTED("1svpqg6r9odrita8rebc0hpuv"); //     bounds[1] = bounds[3] = rnode + 1;
UNSUPPORTED("62if5g6i8b97mw27pyeswbdrz"); //     findlr(agtail(e), aghead(e), &lpos, &rpos);
UNSUPPORTED("2nl8wgr8777ej3q2mxtgh1dcm"); //     while (lnode <= rnode) {
UNSUPPORTED("1rdhgaynvpu54ynu73e6wsye"); // 	setbounds(rank[lnode], bounds, lpos, rpos);
UNSUPPORTED("gpt0y61czu1whc5svspdecor"); // 	if (lnode != rnode)
UNSUPPORTED("j7s178ehkq0r3k5qu7xui5ya"); // 	    setbounds(rank[rnode], bounds, lpos, rpos);
UNSUPPORTED("9bdpmfv605lazcrg4sngt84sp"); // 	lnode++;
UNSUPPORTED("9hw99ncl5q35tiabdnhmxagxn"); // 	rnode--;
UNSUPPORTED("aeu1tyfcxhxvlb3iqnn8vrzyn"); // 	if (bounds[1] - bounds[0] <= 1)
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d81zqerpraa7vwwgfyzs8q0gy"); //     if (bounds[0] <= bounds[1])
UNSUPPORTED("ei1uwic1hm0hwxnd0ll5fhwtv"); // 	pos = (bounds[0] + bounds[1] + 1) / 2;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("a7fy9h7b2xsivnzfdzcwggo2p"); // 	pos = (bounds[2] + bounds[3] + 1) / 2;
UNSUPPORTED("2kr5zds5y6oiaubm8wiunqccr"); //     return pos;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4cw9yo9ap8ze1r873v6jat4yc
// static void  flat_node(edge_t * e) 
public static Object flat_node(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("49p8slcuawyhvoi4g2a74s4ho"); // flat_node(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8qht7xq85blwlgkrcz5qeyndi"); //     int r, place, ypos, h2;
UNSUPPORTED("djl3ek9tn11htu3vj4zglczzz"); //     graph_t *g;
UNSUPPORTED("4q3je7zvvatv7v4lmw10j50te"); //     node_t *n, *vn;
UNSUPPORTED("5wf092biicw6tx6dwyi4yipi0"); //     edge_t *ve;
UNSUPPORTED("bgjjpl6jaaa122twwwd0vif6x"); //     pointf dimen;
UNSUPPORTED("92p9dkk43p8eerc9e4hnz3oy1"); //     if (ED_label(e) == NULL)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("e2npof9ijzmrweahohhn7ps53"); //     g = dot_root(agtail(e));
UNSUPPORTED("6gwhfstthx3ytwdf54bl9tpj2"); //     r = ND_rank(agtail(e));
UNSUPPORTED("bevzt1r0xvtfqac7dsrqatbjb"); //     place = flat_limits(g, e);
UNSUPPORTED("9f9l4bvgmkm055cwc3bmaccje"); //     /* grab ypos = LL.y of label box before make_vn_slot() */
UNSUPPORTED("b8jt668wd0my9zwca820tgrv7"); //     if ((n = GD_rank(g)[r - 1].v[0]))
UNSUPPORTED("kftn4cdef7vxdc0gjlfu9vsz"); // 	ypos = ND_coord(n).y - GD_rank(g)[r - 1].ht1;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("6hnggl4qcfpbzppsqmc2lfenv"); // 	n = GD_rank(g)[r].v[0];
UNSUPPORTED("8gjvue6cjnbib3urxldv4u7ze"); // 	ypos = ND_coord(n).y + GD_rank(g)[r].ht2 + GD_ranksep(g);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2uz3d1ierno37z4cjhti84b90"); //     vn = make_vn_slot(g, r - 1, place);
UNSUPPORTED("aqtjv7x919xwb5qa2wgdrtd84"); //     dimen = ED_label(e)->dimen;
UNSUPPORTED("b2x6j7m1cmmkcmdl5jo9wn0ap"); //     if (GD_flip(g)) {
UNSUPPORTED("bb5bobot4on1o96n5vhntudv4"); // 	double f = dimen.x;
UNSUPPORTED("53h4udsxhgfufqzscgozfggnr"); // 	dimen.x = dimen.y;
UNSUPPORTED("czlnr9kn3tq349xzvrxiot7gp"); // 	dimen.y = f;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("50w4ek594me8kon99e6erqgrj"); //     ND_ht(vn) = dimen.y;
UNSUPPORTED("s101g5jij322x1f503gciz76"); //     h2 = ND_ht(vn) / 2;
UNSUPPORTED("auyd4muoijj5auwjni74ovt6i"); //     ND_lw(vn) = ND_rw(vn) = dimen.x / 2;
UNSUPPORTED("3gi02gfemg51637nu9my6zrpq"); //     ND_label(vn) = ED_label(e);
UNSUPPORTED("39kxxbtw2e2k2pptslp2nn7yu"); //     ND_coord(vn).y = ypos + h2;
UNSUPPORTED("5na79r81ha358zo42t63fbwi8"); //     ve = virtual_edge(vn, agtail(e), e);	/* was NULL? */
UNSUPPORTED("8d6roexcfrqd86jdah39se2bh"); //     ED_tail_port(ve).p.x = -ND_lw(vn);
UNSUPPORTED("a3qbjas1ycrf1jnc2dgpgkntm"); //     ED_head_port(ve).p.x = ND_rw(agtail(e));
UNSUPPORTED("2lqjbl8hyt57q35tmf4s57ian"); //     ED_edge_type(ve) = 4;
UNSUPPORTED("2dslllojmalf8ybsfjksfoez7"); //     ve = virtual_edge(vn, aghead(e), e);
UNSUPPORTED("81ug9g73sutw6zm92jw3mjtbp"); //     ED_tail_port(ve).p.x = ND_rw(vn);
UNSUPPORTED("22by8eq72a8ksqmgbpqdt08o"); //     ED_head_port(ve).p.x = ND_lw(aghead(e));
UNSUPPORTED("2lqjbl8hyt57q35tmf4s57ian"); //     ED_edge_type(ve) = 4;
UNSUPPORTED("cm5zrllnnvehfc2j1zsylk92i"); //     /* another assumed symmetry of ht1/ht2 of a label node */
UNSUPPORTED("7xf3qxblcaoay1tsec9gitv2d"); //     if (GD_rank(g)[r - 1].ht1 < h2)
UNSUPPORTED("6ou8m1qzeww97m2i3ohisiqbm"); // 	GD_rank(g)[r - 1].ht1 = h2;
UNSUPPORTED("ej9mvs4jjlev601ns30mewwm7"); //     if (GD_rank(g)[r - 1].ht2 < h2)
UNSUPPORTED("f5aexaptotw72se1bvn2rn0e8"); // 	GD_rank(g)[r - 1].ht2 = h2;
UNSUPPORTED("3y0eydvctg6zt8ij8567rri9d"); //     ND_alg(vn) = e;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1lopavodoru6ee52snd5l6swd
// static void abomination(graph_t * g) 
public static Object abomination(Object... arg) {
UNSUPPORTED("3p0ff5y32m4c29nxc3wzf3vmp"); // static void abomination(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("p6jnh7nvcpnl3zbz636pskbs"); //     int r;
UNSUPPORTED("7b7au08xae4b7xjfppefklfes"); //     rank_t *rptr;
UNSUPPORTED("3jycy0d207jrxwu5brhja45ih"); //     assert(GD_minrank(g) == 0);
UNSUPPORTED("560kbxcnrl4xmypr8ct811ecl"); //     /* 3 = one for new rank, one for sentinel, one for off-by-one */
UNSUPPORTED("dcrwjeosho321989vheyd3f24"); //     r = GD_maxrank(g) + 3;
UNSUPPORTED("946x5990us5a5enmn7rfa7c6m"); //     rptr = ALLOC(r, GD_rank(g), rank_t);
UNSUPPORTED("8v6x3666mzgcsspv0qcq699j0"); //     GD_rank(g) = rptr + 1;
UNSUPPORTED("7lsxe517wqqg8hav1ydoenpr2"); //     for (r = GD_maxrank(g); r >= 0; r--)
UNSUPPORTED("8l1are42fil0nxqk865ope67x"); // 	GD_rank(g)[r] = GD_rank(g)[r - 1];
UNSUPPORTED("dc83xf8i9yoizasey2ikpjrhl"); //     GD_rank(g)[r].n = GD_rank(g)[r].an = 0;
UNSUPPORTED("bg0m21jdi57yb6ckltvnrp1qo"); //     GD_rank(g)[r].v = GD_rank(g)[r].av = (node_t **)zmalloc((2)*sizeof(node_t *));
UNSUPPORTED("6vkt3w1t9jqw3cyznn3tye1fk"); //     GD_rank(g)[r].flat = NULL;
UNSUPPORTED("4ekrtr9xsu5a9k77f4lxotpy8"); //     GD_rank(g)[r].ht1 = GD_rank(g)[r].ht2 = 1;
UNSUPPORTED("8ufuk0l3ohor1iik35q18n600"); //     GD_rank(g)[r].pht1 = GD_rank(g)[r].pht2 = 1;
UNSUPPORTED("g577nwvng3nqlr1ejqd8tiqz"); //     GD_minrank(g)--;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ctujx6e8k3rzv08h6gswdcaqs
// static void checkFlatAdjacent (edge_t* e) 
public static void checkFlatAdjacent(Agedge_s e) {
ENTERING("ctujx6e8k3rzv08h6gswdcaqs","checkFlatAdjacent");
try {
    Agnode_s tn = agtail(e);
    Agnode_s hn = aghead(e);
    int i, lo, hi;
    Agnode_s n;
    __ptr__ rank;
    if (ND_order(tn) < ND_order(hn)) {
	lo = ND_order(tn);
	hi = ND_order(hn);
    }
    else {
	lo = ND_order(hn);
	hi = ND_order(tn);
    }
    rank = GD_rank(dot_root(tn)).plus(ND_rank(tn));
    for (i = lo + 1; i < hi; i++) {
	n = (Agnode_s) rank.getArrayOfPtr("v").plus(i).getPtr();
	if ((ND_node_type(n) == 1 && ND_label(n)!=null) || 
             ND_node_type(n) == 0)
	    break;
    }
    if (i == hi) {  /* adjacent edge */
	do {
	    ED_adjacent(e, 1);
	    e = ED_to_virt(e);
	} while (e!=null); 
    }
} finally {
LEAVING("ctujx6e8k3rzv08h6gswdcaqs","checkFlatAdjacent");
}
}




//3 bjwwj6ftkm0gv04cf1edqeaw6
// int  flat_edges(graph_t * g) 
public static int flat_edges(Agraph_s g) {
ENTERING("bjwwj6ftkm0gv04cf1edqeaw6","flat_edges");
try {
    int i, j, reset = 0;
    Agnode_s n;
    Agedge_s e;
    int found = 0;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if (ND_flat_out(n).getPtr("list")!=null) {
	    for (j = 0; (e = (Agedge_s) ND_flat_out(n).getArrayOfPtr("list").plus(j).getPtr())!=null; j++) {
		checkFlatAdjacent (e);
	    }
	}
	for (j = 0; j < ND_other(n).getInt("size"); j++) {
	    e = (Agedge_s) ND_other(n).getArrayOfPtr("list").plus(j).getPtr();
	    if (ND_rank(aghead(e)) == ND_rank(agtail(e)))
		checkFlatAdjacent (e);
	}
    }
    if ((GD_rank(g).plus(0).getPtr("flat")!=null) || (GD_n_cluster(g) > 0)) {
	for (i = 0; (n = (Agnode_s) GD_rank(g).plus(0).getArrayOfPtr("v").plus(i).getPtr())!=null; i++) {
	    for (j = 0; (e = (Agedge_s) ND_flat_in(n).getArrayOfPtr("list").plus(j).getPtr())!=null; j++) {
		if ((ED_label(e)!=null) && N(ED_adjacent(e))) {
		    abomination(g);
		    found = 1;
		    break;
		}
	    }
	    if (found!=0)
		break;
	}
    }
    rec_save_vlists(g);
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
          /* if n is the tail of any flat edge, one will be in flat_out */
	if (ND_flat_out(n).getPtr("list")!=null) {
	    for (i = 0; (e = (Agedge_s) ND_flat_out(n).getArrayOfPtr("list").plus(i).getPtr())!=null; i++) {
		if (ED_label(e)!=null) {
		    if (ED_adjacent(e)!=0) {
			if (GD_flip(g)!=0) ED_dist(e, ED_label(e).getStruct("dimen").getDouble("y"));
			else ED_dist(e, ED_label(e).getStruct("dimen").getDouble("x")); 
		    }
		    else {
			reset = 1;
			flat_node(e);
		    }
		}
	    }
		/* look for other flat edges with labels */
	    for (j = 0; j < ND_other(n).getInt("size"); j++) {
		Agedge_s le;
		e = (Agedge_s) ND_other(n).getArrayOfPtr("list").plus(j).getPtr();
		if (ND_rank(agtail(e)) != ND_rank(aghead(e))) continue;
		if (EQ(agtail(e), aghead(e))) continue; /* skip loops */
		le = e;
		while (ED_to_virt(le)!=null) le = ED_to_virt(le);
		ED_adjacent(e, ED_adjacent(le)); 
		if (ED_label(e)!=null) {
		    if (ED_adjacent(e)!=0) {
			double lw;
			if (GD_flip(g)!=0) lw = ED_label(e).getStruct("dimen").getDouble("y");
			else lw = ED_label(e).getStruct("dimen").getDouble("x"); 
			ED_dist(le, MAX(lw,ED_dist(le)));
		    }
		    else {
			reset = 1;
			flat_node(e);
		    }
		}
	    }
	}
    }
    if (reset!=0)
	rec_reset_vlists(g);
    return reset;
} finally {
LEAVING("bjwwj6ftkm0gv04cf1edqeaw6","flat_edges");
}
}


}
