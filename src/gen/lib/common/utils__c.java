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
package gen.lib.common;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.attr__c.agxget;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.refstr__c.aghtmlstr;
import static gen.lib.common.labels__c.make_label;
import static gen.lib.common.shapes__c.bind_shape;
import static gen.lib.common.shapes__c.shapeOf;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.atoi;
import static smetana.core.JUtils.enumAsInt;
import static smetana.core.JUtils.isdigit;
import static smetana.core.JUtils.strchr;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtils.strtod;
import static smetana.core.JUtils.strtol;
import static smetana.core.JUtils.tolower;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.DIST2;
import static smetana.core.Macro.ED_head_label;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_label_ontop;
import static smetana.core.Macro.ED_tail_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_UF_parent;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_height;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_id;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_shape;
import static smetana.core.Macro.ND_showboxes;
import static smetana.core.Macro.ND_width;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.fabs;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsym_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_fontinfo;
import h.ST_nodequeue;
import h.ST_pointf;
import h.ST_port;
import h.ST_splines;
import h.ST_textlabel_t;
import h.shape_kind;
import smetana.core.CFunction;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class utils__c {
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




//3 c7cptalfn8k75wyfirbnptnav
// nodequeue *new_queue(int sz) 
public static ST_nodequeue new_queue(int sz) {
ENTERING("c7cptalfn8k75wyfirbnptnav","new_queue");
try {
    ST_nodequeue q = new ST_nodequeue();
    if (sz <= 1)
	sz = 2;
	final ST_Agnode_s.ArrayOfStar tmp__ = new ST_Agnode_s.ArrayOfStar(sz);
	q.setPtr("store", tmp__);
	q.setPtr("tail", tmp__);
	q.setPtr("head", tmp__);
	q.setPtr("limit", tmp__.plus(sz));
    return q;
} finally {
LEAVING("c7cptalfn8k75wyfirbnptnav","new_queue");
}
}




//3 1uj5nmdvwmuklnpd3v5py547f
// void free_queue(nodequeue * q) 
public static void free_queue(ST_nodequeue q) {
ENTERING("1uj5nmdvwmuklnpd3v5py547f","free_queue");
try {
    Memory.free(q.store);
    Memory.free(q);
} finally {
LEAVING("1uj5nmdvwmuklnpd3v5py547f","free_queue");
}
}




//3 20pwd6i141q3o25lfvrdqytot
// void enqueue(nodequeue * q, node_t * n) 
public static void enqueue(ST_nodequeue q, ST_Agnode_s n) {
ENTERING("20pwd6i141q3o25lfvrdqytot","enqueue");
try {
    q.tail.plus(0).setPtr(n);
    q.setPtr("tail", q.tail.plus(1));
    if (q.tail.comparePointer(q.limit) >= 0)
	q.setPtr("tail", q.store);
} finally {
LEAVING("20pwd6i141q3o25lfvrdqytot","enqueue");
}
}




//3 b612nmtf16au96ztbs8pike9r
// node_t *dequeue(nodequeue * q) 
public static ST_Agnode_s dequeue(ST_nodequeue q) {
ENTERING("b612nmtf16au96ztbs8pike9r","dequeue");
try {
    ST_Agnode_s n;
    if (EQ(q.head, q.tail))
	n = null;
    else {
	n = (ST_Agnode_s) q.head.get(0);
	q.setPtr("head", q.head.plus(1));
	if (q.head.comparePointer(q.limit) >= 0)
	    q.setPtr("head", q.store);
    }
    return n;
} finally {
LEAVING("b612nmtf16au96ztbs8pike9r","dequeue");
}
}




//3 6nydxv4f2m7jcfh8ljs0neu0x
// int late_int(void *obj, attrsym_t * attr, int def, int low) 
public static int late_int(__ptr__ obj, ST_Agsym_s attr, int def, int low) {
ENTERING("6nydxv4f2m7jcfh8ljs0neu0x","late_int");
try {
    CString p;
    CString endp[] = new CString[1];
    int rv;
    if (attr == null)
	return def;
    p = agxget(obj,attr);
    if (N(p) || p.length()==0)
	return def;
    rv = strtol (p, endp, 10);
    if (EQ(p, endp[0])) return def;  /* invalid int format */
    if (rv < low) return low;
    else return rv;
} finally {
LEAVING("6nydxv4f2m7jcfh8ljs0neu0x","late_int");
}
}




//3 d68314e4f20r79tt0cnmxugme
// double late_double(void *obj, attrsym_t * attr, double def, double low) 
public static double late_double(__ptr__ obj, ST_Agsym_s attr, double def, double low) {
ENTERING("d68314e4f20r79tt0cnmxugme","late_double");
try {
    CString p;
    CString endp[] = new CString[1];
    double rv;
    if (N(attr) || N(obj))
	return def;
    p = agxget(obj,attr);
    if (N(p) || p.charAt(0) == '\0')
	return def;
    rv = strtod (p, endp);
    if (p == endp[0]) return def;  /* invalid double format */
    if (rv < low) return low;
    else return rv;
} finally {
LEAVING("d68314e4f20r79tt0cnmxugme","late_double");
}
}




//3 70otpdqcum1z6ht6udvuxl7r2
// double get_inputscale (graph_t* g) 
public static Object get_inputscale(Object... arg) {
UNSUPPORTED("9ufij3opw003w1a5l59wehdi6"); // double get_inputscale (graph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8ne6p4d5pykwl1d3xk0yg0ipb"); //     double d;
UNSUPPORTED("c3jy0hsf1pj5mu65gn0341dbk"); //     if (PSinputscale > 0) return PSinputscale;  /* command line flag prevails */
UNSUPPORTED("1l7wr8a4r1ml3ghihovvwsll0"); //     d = late_double(g, (agattr(g,AGRAPH,"inputscale",(void *)0)), -1, 0);
UNSUPPORTED("464faf5rk766qz197xbucjy1q"); //     if (d == 0) return 72; 
UNSUPPORTED("34ovcwkctb1p5v0nns43aj54s"); //     else return d;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 83xm6yc9q5h1bzufhsnv0v2up
// char *late_string(void *obj, attrsym_t * attr, char *def) 
public static CString late_string(__ptr__ obj, ST_Agsym_s attr, CString def) {
ENTERING("83xm6yc9q5h1bzufhsnv0v2up","late_string");
try {
    if (N(attr) || N(obj))
	return def;
    return agxget(obj, attr);
} finally {
LEAVING("83xm6yc9q5h1bzufhsnv0v2up","late_string");
}
}




//3 8oon4q1mrublaru177xfntqgd
// char *late_nnstring(void *obj, attrsym_t * attr, char *def) 
public static CString late_nnstring(__ptr__ obj, ST_Agsym_s attr, CString def) {
ENTERING("8oon4q1mrublaru177xfntqgd","late_nnstring");
try {
    CString rv = late_string(obj, attr, def);
    if (N(rv) || (rv.charAt(0) == '\0'))
	rv = def;
    return rv;
} finally {
LEAVING("8oon4q1mrublaru177xfntqgd","late_nnstring");
}
}




//3 87ifze04q7qzigjj1fb9y9by2
// boolean late_bool(void *obj, attrsym_t * attr, int def) 
public static boolean late_bool(__ptr__ obj, ST_Agsym_s attr, int def) {
ENTERING("87ifze04q7qzigjj1fb9y9by2","late_bool");
try {
if (attr == null)
	return def!=0;
UNSUPPORTED("a0kh1y5n8u59z0xo7mag3zmt6"); //     return mapbool(agxget(obj, attr));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("87ifze04q7qzigjj1fb9y9by2","late_bool");
}
}




//3 aeq0acpkhfv3gd5jx8op4jf18
// node_t *UF_find(node_t * n) 
public static ST_Agnode_s UF_find(ST_Agnode_s n) {
ENTERING("aeq0acpkhfv3gd5jx8op4jf18","UF_find");
try {
    while (ND_UF_parent(n)!=null && NEQ(ND_UF_parent(n), n)) {
	if (ND_UF_parent(ND_UF_parent(n))!=null)
	    ND_UF_parent(n, ND_UF_parent(ND_UF_parent(n)));
	n = ND_UF_parent(n);
    }
    return n;
} finally {
LEAVING("aeq0acpkhfv3gd5jx8op4jf18","UF_find");
}
}




//3 9ldxwfr4vvijrvfcvs1hvdzrt
// node_t *UF_union(node_t * u, node_t * v) 
public static ST_Agnode_s UF_union(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("9ldxwfr4vvijrvfcvs1hvdzrt","UF_union");
try {
    if (EQ(u, v))
	return u;
    if (ND_UF_parent(u) == null) {
	ND_UF_parent(u, u);
	ND_UF_size(u, 1);
    } else
UNSUPPORTED("35c97tyk6tzw1g527j6rp6xoo"); // 	u = UF_find(u);
    if (ND_UF_parent(v) == null) {
	ND_UF_parent(v, v);
	ND_UF_size(v, 1);
    } else
	v = UF_find(v);
    if (ND_id(u) > ND_id(v)) {
UNSUPPORTED("2igr3ntnkm6svji4pbnjlp54e"); // 	ND_UF_parent(u) = v;
UNSUPPORTED("3lht90i6tvxbr10meir8nvcic"); // 	ND_UF_size(v) += ND_UF_size(u);
    } else {
	ND_UF_parent(v, u);
	ND_UF_size(u, ND_UF_size(u) + ND_UF_size(v));
	v = u;
    }
    return v;
} finally {
LEAVING("9ldxwfr4vvijrvfcvs1hvdzrt","UF_union");
}
}




//3 3j3pbee3o14ctlm51gqul3y1b
// void UF_remove(node_t * u, node_t * v) 
public static Object UF_remove(Object... arg) {
UNSUPPORTED("4mdu14gpibvzmm5t9g0h7oaek"); // void UF_remove(node_t * u, node_t * v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dnvt20f8swe5kyjjj2g4vpx46"); //     assert(ND_UF_size(u) == 1);
UNSUPPORTED("crtqvbx6u5amvtj4nhuhui0p0"); //     ND_UF_parent(u) = u;
UNSUPPORTED("4gdqcsr8w5gtc1qbdht2l23g2"); //     ND_UF_size(v) -= ND_UF_size(u);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 22k0u1imxyw06k9rizqlfz153
// void UF_singleton(node_t * u) 
public static void UF_singleton(ST_Agnode_s u) {
ENTERING("22k0u1imxyw06k9rizqlfz153","UF_singleton");
try {
    ND_UF_size(u, 1);
    ND_UF_parent(u, null);
    ND_ranktype(u, 0);
} finally {
LEAVING("22k0u1imxyw06k9rizqlfz153","UF_singleton");
}
}




//3 e0fn8xuzkdt0q8xoofl6j1txb
// void UF_setname(node_t * u, node_t * v) 
public static void UF_setname(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("e0fn8xuzkdt0q8xoofl6j1txb","UF_setname");
try {
    assert(EQ(u, UF_find(u)));
    ND_UF_parent(u, v);
    ND_UF_size(v, ND_UF_size(v) + ND_UF_size(u));
} finally {
LEAVING("e0fn8xuzkdt0q8xoofl6j1txb","UF_setname");
}
}




//3 31rkmp5c1ie1pfzbkla6b4ag
// pointf coord(node_t * n) 
public static Object coord(Object... arg) {
UNSUPPORTED("880b2lkxm37hksgryyrw8qqvj"); // pointf coord(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("evwdz7pwy2u67oqi6hg38wal1"); //     r.x = 72 * ND_pos(n)[0];
UNSUPPORTED("1dwnra1p0vsse07037h9fdfgp"); //     r.y = 72 * ND_pos(n)[1];
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6p0ey2c2ujk2o7h221p0b4xon
// pointf Bezier(pointf * V, int degree, double t, pointf * Left, pointf * Right) 
public static ST_pointf Bezier(final ST_pointf.Array V, int degree, double t, __ptr__ Left, __ptr__ Right) {
// WARNING!! STRUCT
return Bezier_w_(V, degree, t, Left, Right).copy();
}
private static ST_pointf Bezier_w_(final ST_pointf.Array V, int degree, double t, __ptr__ Left, __ptr__ Right) {
ENTERING("6p0ey2c2ujk2o7h221p0b4xon","Bezier");
try {
    int i, j;			/* Index variables      */
    final ST_pointf.Array Vtemp[] = new ST_pointf.Array[] { new ST_pointf.Array( 5+1),
    new ST_pointf.Array( 5+1),
    new ST_pointf.Array( 5+1),
    new ST_pointf.Array( 5+1),
    new ST_pointf.Array( 5+1),
    new ST_pointf.Array( 5+1) };
    /* Copy control points  */
    for (j = 0; j <= degree; j++) {
	Vtemp[0].plus(j).setStruct(V.plus(j).getStruct());
    }
    /* Triangle computation */
    for (i = 1; i <= degree; i++) {
	for (j = 0; j <= degree - i; j++) {
	    Vtemp[i].plus(j).setDouble("x",
		(1.0 - t) * Vtemp[i - 1].get(j).x + t * Vtemp[i - 1].get(j + 1).x);
	    Vtemp[i].plus(j).setDouble("y",
		(1.0 - t) * Vtemp[i - 1].get(j).y + t * Vtemp[i - 1].get(j + 1).y);
	}
    }
    if (Left != null)
	for (j = 0; j <= degree; j++)
	    Left.plus(j).setStruct(Vtemp[j].plus(0).getStruct());
    if (Right != null)
	for (j = 0; j <= degree; j++)
	    Right.plus(j).setStruct(Vtemp[degree - j].plus(j).getStruct());
    return (ST_pointf) (Vtemp[degree].plus(0).getStruct());
} finally {
LEAVING("6p0ey2c2ujk2o7h221p0b4xon","Bezier");
}
}




//3 3ly0ylecb4k9xk5b7ffrlolt9
// char *Fgets(FILE * fp) 
public static Object Fgets(Object... arg) {
UNSUPPORTED("4q07n5lkhirby89lrss95tlri"); // char *Fgets(FILE * fp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f2la3gescrvdrwclkw6jqpel5"); //     static int bsize = 0;
UNSUPPORTED("8kywmm7f51dmrjngxwvnpsd4o"); //     static char *buf;
UNSUPPORTED("ecmuo8avgj1iywuapxmibw016"); //     char *lp;
UNSUPPORTED("dwe86466ugstemepdfk8yzphz"); //     int len;
UNSUPPORTED("cl8iuel2v9wmaxtyhnvq5trol"); //     len = 0;
UNSUPPORTED("53kc41p479auwcycfsbhw8xix"); //     do {
UNSUPPORTED("94pefdj9c9wxznl2ybw0eaxlu"); // 	if (bsize - len < BUFSIZ) {
UNSUPPORTED("3f9vu9nd93ckzyw89aeu7qtpz"); // 	    bsize += BUFSIZ;
UNSUPPORTED("6zqvad9bp7g8thltaf4fgbiqe"); // 	    buf = grealloc(buf, bsize);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6javvrure63s3d9t1t1g3jjb3"); // 	lp = fgets(buf + len, bsize - len, fp);
UNSUPPORTED("5s6nvinfb0elfdlq0b4hvn087"); // 	if (lp == 0)
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("83wm9z0kqu2eg7994ilmkvhsy"); // 	len += strlen(lp);	/* since lp != NULL, len > 0 */
UNSUPPORTED("cpr2w7m7b9d0k3v79b2ed57cv"); //     } while (buf[len - 1] != '\n');
UNSUPPORTED("6qgvu9gyei3rz9oa1j4onpev8"); //     if (len > 0)
UNSUPPORTED("60rrsbs3bgak2rhurj1kw71av"); // 	return buf;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 24n59f0dfjbrcciik6yp7reuz
// static char** mkDirlist (const char* list, int* maxdirlen) 
public static Object mkDirlist(Object... arg) {
UNSUPPORTED("52rxugfl5uyz0px71ixefqszi"); // static char** mkDirlist (const char* list, int* maxdirlen)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("behjm5bjsenezpg3f7cncvteu"); //     int cnt = 0;
UNSUPPORTED("8b1p8gk30j7o435ywb280jum2"); //     char* s = strdup (list);
UNSUPPORTED("35u91xpv8pyr56o5nch0azwdl"); //     char* dir;
UNSUPPORTED("9b5fnnkm94rlgnqwv15ibu46r"); //     char** dirs = (void *)0;
UNSUPPORTED("2cb6784tzmjr65il4amoj2f6h"); //     int maxlen = 0;
UNSUPPORTED("bnhqg9audjylbv3i0tj26rrcp"); //     for (dir = strtok (s, ":"); dir; dir = strtok ((void *)0, ":")) {
UNSUPPORTED("aqf6niag46yhhhj5sccv41hrk"); // 	dirs = ALLOC (cnt+2,dirs,char*);
UNSUPPORTED("f5047pr5l6jv6zl8latgfspry"); // 	dirs[cnt++] = dir;
UNSUPPORTED("13d902xfof2syshtj70d02plc"); // 	maxlen = MAX(maxlen, strlen (dir));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3qszpnaufz3i0zkcsuaqfcbap"); //     dirs[cnt] = (void *)0;
UNSUPPORTED("a10ctcqm0sjufygu9dh0cc6aa"); //     *maxdirlen = maxlen;
UNSUPPORTED("5os9u9oj7fjpbstveie2w0yxq"); //     return dirs;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 49gli3uq79rprx3u3opdhcx1m
// static char* findPath (char** dirs, int maxdirlen, const char* str) 
public static Object findPath(Object... arg) {
UNSUPPORTED("a5bmudkw4k7jkxvyb8gptf73c"); // static char* findPath (char** dirs, int maxdirlen, const char* str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("93jals27u5ur9t8vy0noookm0"); //     static char *safefilename = (void *)0;
UNSUPPORTED("bpsbmb5c3iu1rp98h1ouv1rgr"); //     char** dp;
UNSUPPORTED("81a5ilslogj5wcerq31e8qoh9"); // 	/* allocate a buffer that we are sure is big enough
UNSUPPORTED("a6c5zuexn89b87yrrru9wgnwg"); //          * +1 for null character.
UNSUPPORTED("eaa4q86pe1s8k4qb4tkhw73zl"); //          * +1 for directory separator character.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("15klhvefamyul36it624epw39"); //     safefilename = realloc(safefilename, (maxdirlen + strlen(str) + 2));
UNSUPPORTED("2zug3hj9q9xtnh9p792zu5xh8"); //     for (dp = dirs; *dp; dp++) {
UNSUPPORTED("bhh1cl0s26fu2nevbn685qotj"); // 	sprintf (safefilename, "%s%s%s", *dp, "/", str);
UNSUPPORTED("c3hxsxu6vm5rsmcvu94zjfhmz"); // 	if (access (safefilename, R_OK) == 0)
UNSUPPORTED("bvviyr83kx89qfvn2tmv1fj2x"); // 	    return safefilename;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bhge6w48v1uv92wmsde2jqqs0"); //     return (void *)0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//static boolean onetime = (!(0));
//static char *pathlist = (void *)0;
//static int maxdirlen;
//static char** dirs;
//3 3xll2b0v9nthwvx9dafq49t8s
// const char *safefile(const char *filename) 
public static CString safefile(CString filename) {
ENTERING("3xll2b0v9nthwvx9dafq49t8s","safefile");
try {
	return null;
} finally {
LEAVING("3xll2b0v9nthwvx9dafq49t8s","safefile");
}
}




//3 2ihv17oajyaaaycirwsbgz1m7
// int maptoken(char *p, char **name, int *val) 
public static int maptoken(CString p, CString name[], int val[]) {
ENTERING("2ihv17oajyaaaycirwsbgz1m7","maptoken");
try {
    int i;
    CString q;
    for (i = 0; (q = name[i]) != null; i++)
	if (p!=null && (N(strcmp(p,q))))
	    break;
    return val[i];
} finally {
LEAVING("2ihv17oajyaaaycirwsbgz1m7","maptoken");
}
}




//3 4esyuq2yqdaqoddgfqs24m5m3
// boolean mapBool(char *p, boolean dflt) 
public static boolean mapBool(CString p, boolean dflt) {
ENTERING("4esyuq2yqdaqoddgfqs24m5m3","mapBool");
try {
    if (N(p) || (p.charAt(0) == '\0'))
	return dflt;
    if (N(strcasecmp(p, new CString("false"))))
	return false;
    if (N(strcasecmp(p, new CString("no"))))
	return false;
    if (N(strcasecmp(p, new CString("true"))))
	return (N(0));
    if (N(strcasecmp(p, new CString("yes"))))
	return (N(0));
    if (isdigit(p.charAt(0)))
	return atoi(p)!=0;
    else
	return dflt;
} finally {
LEAVING("4esyuq2yqdaqoddgfqs24m5m3","mapBool");
}
}




//3 ehkvqh6bwf4jw3mj1w5p7a8m8
// boolean mapbool(char *p) 
public static boolean mapbool(CString p) {
ENTERING("ehkvqh6bwf4jw3mj1w5p7a8m8","mapbool");
try {
    return mapBool (p, false);
} finally {
LEAVING("ehkvqh6bwf4jw3mj1w5p7a8m8","mapbool");
}
}




//3 37hgj44s94wf9bmz16he85pgq
// pointf dotneato_closest(splines * spl, pointf pt) 
public static ST_pointf dotneato_closest(ST_splines spl, final ST_pointf pt) {
ENTERING("37hgj44s94wf9bmz16he85pgq","dotneato_closest");
try {
    return (ST_pointf) dotneato_closest_ (spl, (ST_pointf) pt.copy()).copy();
} finally {
LEAVING("37hgj44s94wf9bmz16he85pgq","dotneato_closest");
}	
}
private static ST_pointf dotneato_closest_(ST_splines spl, final ST_pointf pt) {
     int i, j, k, besti, bestj;
     double bestdist2, d2, dlow2, dhigh2; /* squares of distances */
     double low, high, t;
     // final ST_pointf c[] = new ST_pointf[] {new ST_pointf(),new ST_pointf(),new ST_pointf(),new ST_pointf()};
	 final ST_pointf.Array c = new ST_pointf.Array( 4);
     final ST_pointf pt2 = new ST_pointf();
     final ST_bezier bz = new ST_bezier();
     besti = bestj = -1;
     bestdist2 = 1e+38;
     for (i = 0; i < spl.size; i++) {
 	bz.____(spl.list.get(i));
 	for (j = 0; j < bz.size; j++) {
 	    final ST_pointf b = new ST_pointf();
 	    b.x = bz.list.get(j).x;
 	    b.y = bz.list.get(j).y;
 	    d2 = DIST2(b, pt);
 	    if ((bestj == -1) || (d2 < bestdist2)) {
 		besti = i;
 		bestj = j;
 		bestdist2 = d2;
 	    }
 	}
     }
  	bz.____(spl.list.get(besti));
     /* Pick best Bezier. If bestj is the last point in the B-spline, decrement.
      * Then set j to be the first point in the corresponding Bezier by dividing
      * then multiplying be 3. Thus, 0,1,2 => 0; 3,4,5 => 3, etc.
      */
     if (bestj == bz.size-1)
 	bestj--;
     j = 3*(bestj / 3);
     for (k = 0; k < 4; k++) {
 	  	c.plus(k).setDouble("x", bz.list.get(j + k).x);
 	  	c.plus(k).setDouble("y", bz.list.get(j + k).y);
     }
     low = 0.0;
     high = 1.0;
     dlow2 = DIST2(c.get(0), pt);
     dhigh2 = DIST2(c.get(3), pt);
     do {
 	t = (low + high) / 2.0;
 	pt2.___(Bezier(c, 3, t, null, null));
 	if (fabs(dlow2 - dhigh2) < 1.0)
 	    break;
 	if (fabs(high - low) < .00001)
 	    break;
UNSUPPORTED("6apa9aoby9j8a0eanbfhy5mn2"); // 	if (dlow2 < dhigh2) {
UNSUPPORTED("6jttyuryfaxa193mme86dqf58"); // 	    high = t;
UNSUPPORTED("6avwplrum9i9qi3g9wl6yvz04"); // 	    dhigh2 = DIST2(pt2, pt);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("556vs5i22602clhs1p3htz7vk"); // 	    low = t;
UNSUPPORTED("507tgq81szei2ge3miiak4b1v"); // 	    dlow2 = DIST2(pt2, pt);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
     } while (true);
      return pt2;
}




//3 8v6t685mjc3ps5m4i5m7kj5r5
// pointf spline_at_y(splines * spl, double y) 
public static Object spline_at_y(Object... arg) {
UNSUPPORTED("6z59zacy0w6awy33d9vj3ka38"); // pointf spline_at_y(splines * spl, double y)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dzpsknrwv8qkqq20hjnjpjn68"); //     int i, j;
UNSUPPORTED("c7xj151i9l4snnjn7ibtdupr2"); //     double low, high, d, t;
UNSUPPORTED("10vmrpj8mfz9opwwvwpolaopd"); //     pointf c[4], p;
UNSUPPORTED("m46gfclfoxui2qhyykvkqk0a"); //     static bezier bz;
UNSUPPORTED("aqjwfeuociuhtcp9z6qs0chn8"); // /* this caching seems to prevent p.x from getting set from bz.list[0].x
UNSUPPORTED("epvvl2xzzwpc4zmue865xosgp"); // 	- optimizer problem ? */
UNSUPPORTED("b9epibfipw1wsk324v5epkerh"); // 	for (i = 0; i < spl->size; i++) {
UNSUPPORTED("3ykobnao35x0n6r6go2stj9o5"); // 	    bz = spl->list[i];
UNSUPPORTED("clxoycrurqc4oq0d7v2fp7imo"); // 	    if (BETWEEN(bz.list[bz.size - 1].y, y, bz.list[0].y))
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3en4490pkykfcbc2szvzzx27g"); //     if (y > bz.list[0].y)
UNSUPPORTED("e1te1nqw2aru9ro9sqdjhhttv"); // 	p = bz.list[0];
UNSUPPORTED("24izeylrlk4gqqilirqwty8r9"); //     else if (y < bz.list[bz.size - 1].y)
UNSUPPORTED("5cxp04l4lo39n1p1wsvav460c"); // 	p = bz.list[bz.size - 1];
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("cj02uw2oxm47gdexf3z5axxlw"); // 	for (i = 0; i < bz.size; i += 3) {
UNSUPPORTED("djfbmqatbjckruruv8rxs6uav"); // 	    for (j = 0; j < 3; j++) {
UNSUPPORTED("5b9ziw2aes1kmllxgwkfpmr7p"); // 		if ((bz.list[i + j].y <= y) && (y <= bz.list[i + j + 1].y))
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("amh6xmny0k57y6wqy4ms0zyqe"); // 		if ((bz.list[i + j].y >= y) && (y >= bz.list[i + j + 1].y))
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9fvhco56e60yr7h2c6ceqf1p5"); // 	    if (j < 3)
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eoqh4atx94pamqo1963pcrq50"); // 	assert(i < bz.size);
UNSUPPORTED("ac0jvph9p2m1i3zdywfxmq4eg"); // 	for (j = 0; j < 4; j++) {
UNSUPPORTED("ajjpb9st186njpdxscd9bmg80"); // 	    c[j].x = bz.list[i + j].x;
UNSUPPORTED("2lnyqxk5vopusie575n54x590"); // 	    c[j].y = bz.list[i + j].y;
UNSUPPORTED("5n0qrttzwlxb3x8t4ngkjz31j"); // 	    /* make the spline be monotonic in Y, awful but it works for now */
UNSUPPORTED("ath2bi6h6bk6mkwqk00fweao2"); // 	    if ((j > 0) && (c[j].y > c[j - 1].y))
UNSUPPORTED("a6ze9ec2noj595b0aw7s8h7id"); // 		c[j].y = c[j - 1].y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2zhwm9ypobvcurq5wram1qiil"); // 	low = 0.0;
UNSUPPORTED("2ee14osydbs80okw31an3ngjg"); // 	high = 1.0;
UNSUPPORTED("8vxyvy38lzpbd83cu26nejaan"); // 	do {
UNSUPPORTED("aclx3e6mwmv4x6wtctvdpafcb"); // 	    t = (low + high) / 2.0;
UNSUPPORTED("c609yg89bcwhbtz2sjavo4mo0"); // 	    p = Bezier(c, 3, t, (void *)0, (void *)0);
UNSUPPORTED("7tmwl314qsfq3dcvb5u9uyknb"); // 	    d = p.y - y;
UNSUPPORTED("2b3fbztokpi7ure6eux7qupf8"); // 	    if (ABS(d) <= 1)
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("d8gdi8364l2gqf1f2o8j20nhe"); // 	    if (d < 0)
UNSUPPORTED("ckgl1j6wm8pbqhae0gpdab4qk"); // 		high = t;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("12xca5z3d0sj7sp9cgyqxop4f"); // 		low = t;
UNSUPPORTED("6agdm6388u3r4v7kc6ho2hxhu"); // 	} while (1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5jdhcgi82gtmvn690v78zmkpe"); //     p.y = y;
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4ws2d4r4z8psnleuxber6j3s6
// pointf neato_closest(splines * spl, pointf p) 
public static Object neato_closest(Object... arg) {
UNSUPPORTED("8hg1y8i9xy00n3blyechtqvvh"); // pointf neato_closest(splines * spl, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cickuxw2k5um9ssxcufe5ny0h"); // /* this is a stub so that we can share a common emit.c between dot and neato */
UNSUPPORTED("b5hsyvf8h7g1m8n1qzbi0hrry"); //     return spline_at_y(spl, p.y);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 8s6oop1ayag35eusof2rgdb5b
// static int Tflag




//3 ak4q69g3z2si69q7q6mmtp9qi
// void gvToggle(int s) 
public static Object gvToggle(Object... arg) {
UNSUPPORTED("46ptgxtzrw0bu7ggbzxrodqnw"); // void gvToggle(int s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dxdyekcrbykzppszumnvmrogc"); //     Tflag = !Tflag;
UNSUPPORTED("9zus728u8e05ja3xahcrlzmsr"); //     signal(SIGUSR1, gvToggle);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6h6t4myqjvz5d7nv9s4fme7ol
// int test_toggle() 
public static Object test_toggle(Object... arg) {
UNSUPPORTED("66b64g196uijyd7jhapf68wvg"); // int test_toggle()
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7op0qt5n8bs6z3ptnpbps1ikb"); //     return Tflag;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cr81drt18h5feqzxyh3jb0u49
// void common_init_node(node_t * n) 
public static void common_init_node(ST_Agnode_s n) {
ENTERING("cr81drt18h5feqzxyh3jb0u49","common_init_node");
try {
	ST_fontinfo fi = new ST_fontinfo();
    CString str;
    ND_width(n,
	late_double(n, Z.z().N_width, 0.75, 0.01));
    ND_height(n,
	late_double(n, Z.z().N_height, 0.5, 0.02));
    ND_shape(n,
	bind_shape(late_nnstring(n, Z.z().N_shape, new CString("ellipse")), n));
    str = agxget(n, Z.z().N_label);
    fi.setDouble("fontsize", late_double(n, Z.z().N_fontsize, 14.0, 1.0));
    fi.setPtr("fontname", late_nnstring(n, Z.z().N_fontname, new CString("Times-Roman")));
    fi.setPtr("fontcolor", late_nnstring(n, Z.z().N_fontcolor, new CString("black")));
    ND_label(n, make_label(n, str,
	        ((aghtmlstr(str)!=0 ? (1 << 1) : (0 << 1)) | ( (shapeOf(n) == enumAsInt(shape_kind.class, "SH_RECORD")) ? (2 << 1) : (0 << 1))),
		fi.fontsize, fi.fontname, fi.fontcolor));
    if (Z.z().N_xlabel!=null && (str = agxget(n, Z.z().N_xlabel))!=null && (str.charAt(0)!='\0')) {
UNSUPPORTED("4ua9vld76wpovsm1celv2ff6e"); // 	ND_xlabel(n) = make_label((void*)n, str, (aghtmlstr(str) ? (1 << 1) : (0 << 1)),
UNSUPPORTED("b0zm6fkpjlt9jacykbgugjodg"); // 				fi.fontsize, fi.fontname, fi.fontcolor);
UNSUPPORTED("ail0d4qmxj2aqh2q721inwgqu"); // 	GD_has_labels(agraphof(n)) |= (1 << 4);
    }
    ND_showboxes(n, late_int(n, Z.z().N_showboxes, 0, 0));
    ND_shape(n).fns.initfn.exe(n);
} finally {
LEAVING("cr81drt18h5feqzxyh3jb0u49","common_init_node");
}
}




//3 d2v8l80y27ue2fag5c0qplah8
// static void initFontEdgeAttr(edge_t * e, struct fontinfo *fi) 
public static void initFontEdgeAttr(ST_Agedge_s e, ST_fontinfo fi) {
ENTERING("d2v8l80y27ue2fag5c0qplah8","initFontEdgeAttr");
try {
    fi.setDouble("fontsize", late_double(e, Z.z().E_fontsize, 14.0, 1.0));
    fi.setPtr("fontname", late_nnstring(e, Z.z().E_fontname, new CString("Times-Roman")));
    fi.setPtr("fontcolor", late_nnstring(e, Z.z().E_fontcolor, new CString("black")));
} finally {
LEAVING("d2v8l80y27ue2fag5c0qplah8","initFontEdgeAttr");
}
}




//3 ak3pxrdrq900wymudwnjmbito
// static void initFontLabelEdgeAttr(edge_t * e, struct fontinfo *fi, 		      struct fontinfo *lfi) 
public static void initFontLabelEdgeAttr(ST_Agedge_s e, ST_fontinfo fi, ST_fontinfo lfi) {
ENTERING("ak3pxrdrq900wymudwnjmbito","initFontLabelEdgeAttr");
try {
	if (N(fi.fontname)) initFontEdgeAttr(e, fi);
    lfi.setDouble("fontsize", late_double(e, Z.z().E_labelfontsize, fi.fontsize, 1.0));
    lfi.setPtr("fontname", late_nnstring(e, Z.z().E_labelfontname, fi.fontname));
    lfi.setPtr("fontcolor", late_nnstring(e, Z.z().E_labelfontcolor, fi.fontcolor));
} finally {
LEAVING("ak3pxrdrq900wymudwnjmbito","initFontLabelEdgeAttr");
}
}




//3 bgnk1zwht9rwx6thmly98iofb
// static boolean  noClip(edge_t *e, attrsym_t* sym) 
public static boolean noClip(ST_Agedge_s e, ST_Agsym_s sym) {
ENTERING("bgnk1zwht9rwx6thmly98iofb","noClip");
try {
    CString str;
    boolean		rv = false;
    if (sym!=null) {	/* mapbool isn't a good fit, because we want "" to mean true */
	str = agxget(e,sym);
	if (str!=null && str.charAt(0)!='\0') rv = !mapbool(str);
	else rv = false;
    }
    return rv;
} finally {
LEAVING("bgnk1zwht9rwx6thmly98iofb","noClip");
}
}




//3 9vnr1bc7p533acazoxbhbfmx3
// static port chkPort (port (*pf)(node_t*, char*, char*), node_t* n, char* s) 
public static ST_port chkPort(CFunction pf, ST_Agnode_s n, CString s) {
// WARNING!! STRUCT
return chkPort_w_(pf, n, s).copy();
}
private static ST_port chkPort_w_(CFunction pf, ST_Agnode_s n, CString s) {
ENTERING("9vnr1bc7p533acazoxbhbfmx3","chkPort");
try {
    final ST_port pt = new ST_port();
	CString cp=null;
	if(s!=null)
		cp= strchr(s,':');
    if (cp!=null) {
UNSUPPORTED("cbuf05ko7kaxq2n9zw35l5v2h"); // 	*cp = '\0';
UNSUPPORTED("7ofc3q8txvlvus6qwefbnbaxu"); // 	pt = pf(n, s, cp+1);
UNSUPPORTED("971i954brvgqb35cftazlqhon"); // 	*cp = ':';
UNSUPPORTED("2o9oidtrr5gspl1dh6vnz7mlz"); // 	pt.name = cp+1;
    }
    else
	pt.___((ST_port) pf.exe(n, s, null));
	pt.name = s;
    return pt;
} finally {
LEAVING("9vnr1bc7p533acazoxbhbfmx3","chkPort");
}
}




//3 3aqh64lxwv4da2snfe7fvr45b
// int common_init_edge(edge_t * e) 
public static int common_init_edge(ST_Agedge_s e) {
ENTERING("3aqh64lxwv4da2snfe7fvr45b","common_init_edge");
try {
    CString str;
    int r = 0;
    final ST_fontinfo fi = new ST_fontinfo();
    final ST_fontinfo lfi = new ST_fontinfo();
    ST_Agraph_s sg = agraphof(agtail(e));
    fi.setPtr("fontname", null);
    lfi.setPtr("fontname", null);
    if (Z.z().E_label!=null && (str = agxget(e, Z.z().E_label))!=null && (str.charAt(0)!='\0')) {
	r = 1;
	initFontEdgeAttr(e, fi);
	ED_label(e, make_label(e, str, (aghtmlstr(str)!=0 ? (1 << 1) : (0 << 1)),
				fi.fontsize, fi.fontname, fi.fontcolor));
	GD_has_labels(sg, GD_has_labels(sg) | (1 << 0));
	ED_label_ontop(e,
	    mapbool(late_string(e, Z.z().E_label_float, new CString("false"))));
    }
    if (Z.z().E_xlabel!=null && (str = agxget(e, Z.z().E_xlabel))!=null && (str.charAt(0)!='\0')) {
UNSUPPORTED("1j3mhgq7abuh3n19q2jtjddbc"); // 	if (!fi.fontname)
UNSUPPORTED("bmqo2g5g107quod3h31r8iudr"); // 	    initFontEdgeAttr(e, &fi);
UNSUPPORTED("3s7kg9x748riuy3tm697s6e8t"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->xlabel) = make_label((void*)e, str, (aghtmlstr(str) ? (1 << 1) : (0 << 1)),
UNSUPPORTED("b0zm6fkpjlt9jacykbgugjodg"); // 				fi.fontsize, fi.fontname, fi.fontcolor);
UNSUPPORTED("c078bypfszv0nsvp1nc0x28wx"); // 	(((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->has_labels) |= (1 << 5);
    }
    /* vladimir */
    if (Z.z().E_headlabel!=null && (str = agxget(e, Z.z().E_headlabel))!=null && (str.charAt(0)!='\0')) {
    	initFontLabelEdgeAttr(e, fi, lfi);
    	ED_head_label(e, make_label(e, str, (aghtmlstr(str)!=0 ? (1 << 1) : (0 << 1)),
				lfi.fontsize, lfi.fontname, lfi.fontcolor));
    	GD_has_labels(sg, GD_has_labels(sg) | (1 << 1));
    }
    if (Z.z().E_taillabel!=null && (str = agxget(e, Z.z().E_taillabel))!=null && (str.charAt(0)!='\0')) {
    	initFontLabelEdgeAttr(e, fi, lfi);
    	ED_tail_label(e, make_label(e, str, (aghtmlstr(str)!=0 ? (1 << 1) : (0 << 1)),
				lfi.fontsize, lfi.fontname, lfi.fontcolor));
    	GD_has_labels(sg, GD_has_labels(sg) | (1 << 2));
    }
    /* end vladimir */
    /* We still accept ports beginning with colons but this is deprecated 
     * That is, we allow tailport = ":abc" as well as the preferred 
     * tailport = "abc".
     */
    str = agget(e, new CString("tailport"));
    /* libgraph always defines tailport/headport; libcgraph doesn't */
    if (N(str)) str = new CString("");
    if (str!=null && str.charAt(0)!='\0')
UNSUPPORTED("j71lo2acx1ydov0uj7xjjce"); // 	(((Agnodeinfo_t*)(((Agobj_t*)(((((((Agobj_t*)(e))->tag).objtype) == 3?(e):((e)+1))->node)))->data))->has_port) = (!(0));

    ED_tail_port(e, chkPort ((CFunction) ND_shape(agtail(e)).fns.portfn, agtail(e), str));
    if (noClip(e, Z.z().E_tailclip))
UNSUPPORTED("cg4z67u0dm6h9nrcx8kkalnlt"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->tail_port).clip = 0;
    str = agget(e, new CString("headport"));
    /* libgraph always defines tailport/headport; libcgraph doesn't */
    if (N(str)) str = new CString("");
    if (str!=null && str.charAt(0)!='\0')
UNSUPPORTED("542y57dbsosmjvsmdnzon2qb5"); // 	(((Agnodeinfo_t*)(((Agobj_t*)(((((((Agobj_t*)(e))->tag).objtype) == 2?(e):((e)-1))->node)))->data))->has_port) = (!(0));

    ED_head_port(e, chkPort((CFunction) ND_shape(aghead(e)).fns.portfn, aghead(e), str));

    if (noClip(e, Z.z().E_headclip))
UNSUPPORTED("ayqscz30ekhcje94wh4ib1hcu"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->head_port).clip = 0;
    return r;
} finally {
LEAVING("3aqh64lxwv4da2snfe7fvr45b","common_init_edge");
}
}




//3 3mkqvtbyq9j8ktzil6t7vakg5
// static boxf addLabelBB(boxf bb, textlabel_t * lp, boolean flipxy) 
public static ST_boxf addLabelBB(final ST_boxf bb, ST_textlabel_t lp, boolean flipxy) {
// WARNING!! STRUCT
return addLabelBB_w_(bb.copy(), lp, flipxy).copy();
}
private static ST_boxf addLabelBB_w_(final ST_boxf bb, ST_textlabel_t lp, boolean flipxy) {
ENTERING("3mkqvtbyq9j8ktzil6t7vakg5","addLabelBB");
try {
    double width, height;
    final ST_pointf p = new ST_pointf();
    p.___(lp.pos);
    double min, max;
    if (flipxy) {
	height = lp.dimen.x;
	width = lp.dimen.y;
    }
    else {
	width = lp.dimen.x;
	height = lp.dimen.y;
    }
    min = p.x - width / 2.;
    max = p.x + width / 2.;
    if (min < bb.LL.x)
	bb.LL.x = min;
    if (max > bb.UR.x)
	bb.UR.x = max;
    min = p.y - height / 2.;
    max = p.y + height / 2.;
    if (min < bb.LL.y)
	bb.LL.y = min;
    if (max > bb.UR.y)
	bb.UR.y = max;
    return bb;
} finally {
LEAVING("3mkqvtbyq9j8ktzil6t7vakg5","addLabelBB");
}
}




//3 abydt85ykexa59r4o9fw9r77o
// boxf polyBB (polygon_t* poly) 
public static Object polyBB(Object... arg) {
UNSUPPORTED("eog6k627mwt0j7tauh94xvup8"); // boxf
UNSUPPORTED("a4oqb702qwzmhj4ubv8nvwnut"); // polyBB (polygon_t* poly)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9qkiyn1vv2k6crbg8xakskx7r"); //     int i, sides = poly->sides;
UNSUPPORTED("wqb7ise36l84z7bv4eo0jk8e"); //     int peris = MAX(poly->peripheries,1);
UNSUPPORTED("f32j3p304siaf3gv4nw2fjxyd"); //     pointf* verts = poly->vertices + (peris-1)*sides;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("b6nw09v79k31a4rwwrm78jena"); //     bb.LL = bb.UR = verts[0];
UNSUPPORTED("c3ab28lpww2nrulbu0e7eei8t"); //     for (i = 1; i < sides; i++) {
UNSUPPORTED("docqulnbkxq1u4xzeygy173ha"); // 	bb.LL.x = MIN(bb.LL.x,verts[i].x);
UNSUPPORTED("162k3p74x02qk563wm0i4f5wy"); // 	bb.LL.y = MIN(bb.LL.y,verts[i].y);
UNSUPPORTED("2puay77reaetlg69b3esmertj"); // 	bb.UR.x = MAX(bb.UR.x,verts[i].x);
UNSUPPORTED("f4uggsyc6kisqh04h7ykmypit"); // 	bb.UR.y = MAX(bb.UR.y,verts[i].y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bz7kjecium6a7xa39qfobwwnc
// void updateBB(graph_t * g, textlabel_t * lp) 
public static void updateBB(ST_Agraph_s g, ST_textlabel_t lp) {
ENTERING("bz7kjecium6a7xa39qfobwwnc","updateBB");
try {
    GD_bb(g).___(addLabelBB(GD_bb(g), lp, GD_flip(g)!=0));
} finally {
LEAVING("bz7kjecium6a7xa39qfobwwnc","updateBB");
}
}




//3 2dhrilz05n4iopa5go0ir09tq
// void compute_bb(graph_t * g) 
public static Object compute_bb(Object... arg) {
UNSUPPORTED("43z8pxvn2vwb16wsrtd6eb4x4"); // void compute_bb(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("84lrde5ocrrgm9zv1ge9e8pwa"); //     boxf b, bb;
UNSUPPORTED("bzz7vodjegzgwxp8jzgkq3uti"); //     boxf BF;
UNSUPPORTED("5tn3u3gu9sfzv423lmms9ruht"); //     pointf ptf, s2;
UNSUPPORTED("dzpsknrwv8qkqq20hjnjpjn68"); //     int i, j;
UNSUPPORTED("331ilq8vsdj6hvow2dqcknbw9"); //     if ((agnnodes(g) == 0) && (GD_n_cluster(g) ==0)) {
UNSUPPORTED("6v3sw7pm6nazbrivryo4463ge"); // 	bb.LL = pointfof(0, 0);
UNSUPPORTED("8fv5furomnqnewjrz8lsz5ks5"); // 	bb.UR = pointfof(0, 0);
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f2nzvg1xnr11v28w2feg923cs"); //     bb.LL = pointfof(INT_MAX, INT_MAX);
UNSUPPORTED("7tttoj8cnxfqgnq2aagnnav48"); //     bb.UR = pointfof(-INT_MAX, -INT_MAX);
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("bp0gyw9zhjswqzd12yj9rxiao"); // 	ptf = coord(n);
UNSUPPORTED("9vcwhl1ztgy395x3g3p27b3vp"); // 	s2.x = ND_xsize(n) / 2.0;
UNSUPPORTED("97s3sp93769x79ow3rretvxt6"); // 	s2.y = ND_ysize(n) / 2.0;
UNSUPPORTED("ecbpdzff34gb4naalyrxe5vzo"); // 	b.LL = sub_pointf(ptf, s2);
UNSUPPORTED("52nsauaebr58m4f8afw1zu6mi"); // 	b.UR = add_pointf(ptf, s2);
UNSUPPORTED("1p5j3as8potjryco1p28zv0ns"); // 	EXPANDBB(bb,b);
UNSUPPORTED("1nfehzcu9dg4m0zqniggw0myh"); // 	if (ND_xlabel(n) && ND_xlabel(n)->set) {
UNSUPPORTED("34rij3kl1x5ia5ytk2qaqgw7i"); // 	    bb = addLabelBB(bb, ND_xlabel(n), GD_flip(g));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("919e52wzxh255quxj0nzwukfl"); // 	    if (ED_spl(e) == 0)
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("ah9ldbdg46psh3ic9qv1v1w1h"); // 	    for (i = 0; i < ED_spl(e)->size; i++) {
UNSUPPORTED("96deowae58qes8jszwjgwchpi"); // 		for (j = 0; j < (((Agedgeinfo_t*)AGDATA(e))->spl)->list[i].size; j++) {
UNSUPPORTED("bm6ktup8tdejk1tq38xkq18zx"); // 		    ptf = ED_spl(e)->list[i].list[j];
UNSUPPORTED("8xyn6e9qgao43nsgse8ya790b"); // 		    EXPANDBP(bb,ptf);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("bbijzcnlrgsrkf9c3lvyq1j3w"); // 	    if (ED_label(e) && ED_label(e)->set) {
UNSUPPORTED("2d569ynk5qigji0zshsuvzop9"); // 		bb = addLabelBB(bb, ED_label(e), GD_flip(g));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7l6ztc2uyzzyyw47jkbmwrvpq"); // 	    if (ED_head_label(e) && ED_head_label(e)->set) {
UNSUPPORTED("298664pi1qebzix2j1gdtf6rl"); // 		bb = addLabelBB(bb, ED_head_label(e), GD_flip(g));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("bs3i08itd9j5yeohefoglkqh5"); // 	    if (ED_tail_label(e) && ED_tail_label(e)->set) {
UNSUPPORTED("f4hck0g41boxn60os1w41wcok"); // 		bb = addLabelBB(bb, ED_tail_label(e), GD_flip(g));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ceshtn0f3py2v4gcpqi8gemwj"); // 	    if (ED_xlabel(e) && ED_xlabel(e)->set) {
UNSUPPORTED("9ssocbm4ep6gx5dquinuttjh4"); // 		bb = addLabelBB(bb, ED_xlabel(e), GD_flip(g));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7naa6f8pevjidfr7m41eli6xj"); //     for (i = 1; i <= GD_n_cluster(g); i++) {
UNSUPPORTED("ayvgjv5zau71cgcmznfyselk7"); // 	B2BF(GD_bb(GD_clust(g)[i]), BF);
UNSUPPORTED("43e6y2c5ngcxuspv36aq0fc8k"); // 	EXPANDBB(bb,BF);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6rirb177r8kbx1j7f9jeot4rl"); //     if (GD_label(g) && GD_label(g)->set) {
UNSUPPORTED("8n0mzjgupx6e90manf0a8tmtc"); // 	bb = addLabelBB(bb, GD_label(g), GD_flip(g));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8pjanfm12ixxbeb7k86g3z5p4"); //     GD_bb(g) = bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ihcngl4nnl0l3a4lazjawjak
// int is_a_cluster (Agraph_t* g) 
public static Object is_a_cluster(Object... arg) {
UNSUPPORTED("c7v1kpsifryrniar3pr9lj2vb"); // int is_a_cluster (Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ey170unvby4qz57rfy4vs0l9f"); //     return ((g == g->root) || (!strncasecmp(agnameof(g), "cluster", 7)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3eb5x8fxszk05rs03aw3w8bal
// Agsym_t *setAttr(graph_t * g, void *obj, char *name, char *value, 			Agsym_t * ap) 
public static Object setAttr(Object... arg) {
UNSUPPORTED("7hwd7388n90vnhk5ry6nk24pv"); // Agsym_t *setAttr(graph_t * g, void *obj, char *name, char *value,
UNSUPPORTED("4gdje67ttlpied9791ewdlrd0"); // 			Agsym_t * ap)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cofg42y3teruvnmgv4yvovh4w"); //     if (ap == (void *)0) {
UNSUPPORTED("6wt2bbghw1qt1af7ak7o9gfgo"); // 	switch (agobjkind(obj)) {
UNSUPPORTED("9t6es77h0301xk4n035emz6o"); // 	case AGRAPH:
UNSUPPORTED("84ratfisjulkdxpfwy3c9htyz"); // 	    ap = agattr(g, AGRAPH,name, "");
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4ih7nalu307xv8wvdpmgy537r"); // 	case AGNODE:
UNSUPPORTED("71ugdg0a02c8zpt280ynq3cea"); // 	    ap = agattr(g,AGNODE, name, "");
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5lnuird1e17irkx6ami31gay1"); // 	case AGEDGE:
UNSUPPORTED("8xoc5xyqthnaqwgkuofzm1lup"); // 	    ap = agattr(g,AGEDGE, name, "");
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("brfoeqd4pjsygr0tiki0f7wch"); //     agxset(obj, ap, value);
UNSUPPORTED("b8cimfvcp1vea97hyfr4m9nix"); //     return ap;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 68c4u42h1oec8puw4huxzsram
// static node_t *clustNode(node_t * n, graph_t * cg, agxbuf * xb, 			 graph_t * clg) 
public static Object clustNode(Object... arg) {
UNSUPPORTED("expuexpqhy52jx8egr4nmadbq"); // static node_t *clustNode(node_t * n, graph_t * cg, agxbuf * xb,
UNSUPPORTED("83ctmg1k8dzy4himebqtr1m1y"); // 			 graph_t * clg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cn1mqtf5jsxe76q0gt0zfucwm"); //     node_t *cn;
UNSUPPORTED("dyafhx3n373qe83tgg0ggtqew"); //     static int idx = 0;
UNSUPPORTED("f1winj1vhwrg5thteiqcqi29i"); //     char num[100];
UNSUPPORTED("5mmb3522ptm4mcf9evx60ajew"); //     agxbput(xb, "__");
UNSUPPORTED("e2g1diasmnmoy22hb3tpytznw"); //     sprintf(num, "%d", idx++);
UNSUPPORTED("2k35glrj7msg2hog9ut7u9nr7"); //     agxbput(xb, num);
UNSUPPORTED("9jhngs0d5bkz6h4vpq5lnoyga"); //     ((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)':')));
UNSUPPORTED("a5rh1qp81ztiwxfywpk0vcapj"); //     agxbput(xb, agnameof(cg));
UNSUPPORTED("67zj9gekg6ygql1z3r0ph931y"); //     cn = agnode(agroot(cg), (((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'\0'))),(char*)((xb)->ptr = (xb)->buf)), 1);
UNSUPPORTED("1xux6s5s5ce4cl1ihunt2957f"); //     agbindrec(cn, "Agnodeinfo_t", sizeof(Agnodeinfo_t), (!(0)));
UNSUPPORTED("8pf78abddkqif7nnus196p6w9"); //     (ND_clustnode(cn) = (!(0)));
UNSUPPORTED("952l8hivvgcu9neihd5oejkne"); // 	agsubnode(cg,cn,1);
UNSUPPORTED("7rpor9cu2a5ytz59ky3yzzf3e"); // 	//aginsert(cg, cn);
UNSUPPORTED("bnwdscnff1ajygbrinebw5tzy"); // 	agsubnode(clg,n,1);
UNSUPPORTED("b4oovl1t2xbj981x521u2wqw9"); // 	//aginsert(clg, n);
UNSUPPORTED("8l8f1yq18trxwvv7v0vnjksyn"); //     /* set attributes */
UNSUPPORTED("9xlhovy1a3tm3x50qm5at98wv"); //     N_label = setAttr(agraphof(cn), cn, "label", "", N_label);
UNSUPPORTED("ejig5v0kmrb3iq04qsqioadrf"); //     N_style = setAttr(agraphof(cn), cn, "style", "invis", N_style);
UNSUPPORTED("3x5fnrqc7r2ikzr4lb8pn0wc5"); //     N_shape = setAttr(agraphof(cn), cn, "shape", "box", N_shape);
UNSUPPORTED("a1e5vm1ljhru5haksozb6im5i"); //     /* N_width = setAttr (cn->graph, cn, "width", "0.0001", N_width); */
UNSUPPORTED("f03wmos529in1bb9hvup5hhai"); //     return cn;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c1dr4tu5yewu3tcstfq3jkcfg
// static int cmpItem(Dt_t * d, void *p1[], void *p2[], Dtdisc_t * disc) 
public static Object cmpItem(Object... arg) {
UNSUPPORTED("2l8537eo2smrl3yniwkv96fhy"); // static int cmpItem(Dt_t * d, void *p1[], void *p2[], Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6z29omss9ay00bqf6xael7t6t"); //     (void) d;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("5dlkskltbjfd7l3iojdmxbfe7"); //     if (p1[0] < p2[0])
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("6eiz5nxr8kgzbn75d0p98o9d3"); //     else if (p1[0] > p2[0])
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("bbxeh2ijuksbga6h6yu50yytg"); //     else if (p1[1] < p2[1])
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("6abzwq2ia723vgf1rf1i65f5d"); //     else if (p1[1] > p2[1])
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bsharuh63hyx8gytgj0drcbxn
// static void *newItem(Dt_t * d, item * objp, Dtdisc_t * disc) 
public static Object newItem(Object... arg) {
UNSUPPORTED("akr8nubtu1wjzyw77dyu7l818"); // static void *newItem(Dt_t * d, item * objp, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("99a4mnjdm29dtq83hvtof51ni"); //     item *newp = (item*)zmalloc(sizeof(item));
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("7o3keent8kekj52p9qb43ethl"); //     newp->p[0] = objp->p[0];
UNSUPPORTED("3wxc6pdiqxckjf8y7mwlj0am9"); //     newp->p[1] = objp->p[1];
UNSUPPORTED("8gwlx3jj25pxng8pmk9zrtusa"); //     newp->t = objp->t;
UNSUPPORTED("9fhk60i1gsni0emh9jwo0y1ts"); //     newp->h = objp->h;
UNSUPPORTED("4enxnv484o23jodrkf81rxdg0"); //     return newp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9k3952c7spf98nnxwfwnp246t
// static void freeItem(Dt_t * d, item * obj, Dtdisc_t * disc) 
public static Object freeItem(Object... arg) {
UNSUPPORTED("8rxgun8stoo6nah2bndbm87b9"); // static void freeItem(Dt_t * d, item * obj, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8are8jqzwfj87yj4tkaohi3tf"); //     free(obj);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 3q539ycox1bllayps038bsksa
// static Dtdisc_t mapDisc = 




//3 cmped1c3ho0jglvwjjj6a228t
// static edge_t *cloneEdge(edge_t * e, node_t * ct, node_t * ch) 
public static Object cloneEdge(Object... arg) {
UNSUPPORTED("d4tyhi7zzf9xmns1lnu336v3r"); // static edge_t *cloneEdge(edge_t * e, node_t * ct, node_t * ch)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3uzp3aqejf5j0yv2yj4jw5aj"); //     graph_t *g = agraphof(ct);
UNSUPPORTED("7k2dgu104ye2c3d50mokkgwhz"); //     edge_t *ce = agedge(g, ct, ch,(void *)0,1);
UNSUPPORTED("1927pqk9hk6k5d7t1k9he6aa9"); //     agbindrec(ce, "Agedgeinfo_t", sizeof(Agedgeinfo_t), (!(0)));
UNSUPPORTED("a8urs3zuslesi9orp6p4z1i6f"); //     agcopyattr(e, ce);
UNSUPPORTED("8yr3irjwjiayt2t3izgwmnj9g"); //     return ce;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c5a3lmz7tpara4c6bxtihr7m6
// static void insertEdge(Dt_t * map, void *t, void *h, edge_t * e) 
public static Object insertEdge(Object... arg) {
UNSUPPORTED("9k64e2wql9m602qa681rgo7i7"); // static void insertEdge(Dt_t * map, void *t, void *h, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7lw765nir8wljj9gzis4zqedg"); //     item dummy;
UNSUPPORTED("8nu1ezwxni2hxz2f9e8v2cbb8"); //     dummy.p[0] = t;
UNSUPPORTED("bc8q41s8rexxkeopidu04qewp"); //     dummy.p[1] = h;
UNSUPPORTED("4tdxi6d9w3ukmzktg7xfyiq7q"); //     dummy.t = agtail(e);
UNSUPPORTED("7382qi1yckci5zkxxyjphhdt1"); //     dummy.h = aghead(e);
UNSUPPORTED("bdurkecw4pa63pn37lkh7haq3"); //     (*(((Dt_t*)(map))->searchf))((map),(void*)(&dummy),0000001);
UNSUPPORTED("bb527tszlzw34gp1ih1xn1iar"); //     dummy.p[0] = h;
UNSUPPORTED("47zbtmrffd896ojei9okz9ed1"); //     dummy.p[1] = t;
UNSUPPORTED("9vuag0zig6ymzak8fo6r3sda1"); //     dummy.t = aghead(e);
UNSUPPORTED("6kdaphe8fi7139t83macqveyb"); //     dummy.h = agtail(e);
UNSUPPORTED("bdurkecw4pa63pn37lkh7haq3"); //     (*(((Dt_t*)(map))->searchf))((map),(void*)(&dummy),0000001);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dkpu53gf0kuy7km1pxs1quv6w
// static item *mapEdge(Dt_t * map, edge_t * e) 
public static Object mapEdge(Object... arg) {
UNSUPPORTED("8o6iypv3kzhmwkk0ssw2py2yj"); // static item *mapEdge(Dt_t * map, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2mkazi3rdjs334ce8xp82ihtk"); //     void *key[2];
UNSUPPORTED("dhcqq0dsnymbpdjw0w7s03fp"); //     key[0] = agtail(e);
UNSUPPORTED("djzhgnjk5pxpyo8hhiad0bwfd"); //     key[1] = aghead(e);
UNSUPPORTED("42lnw96k38ctgmg8vg0dkcazj"); //     return (item *) (*(((Dt_t*)(map))->searchf))((map),(void*)(&key),0001000);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 evei1rlt3rpux3ayqs9tyofmw
// static void checkCompound(edge_t * e, graph_t * clg, agxbuf * xb, Dt_t * map, Dt_t* cmap) 
public static Object checkCompound(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("y76yzntmrne9d5t1m4t7ott3"); // checkCompound(edge_t * e, graph_t * clg, agxbuf * xb, Dt_t * map, Dt_t* cmap)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("24xfyvovg3es787o11thrjc92"); //     graph_t *tg;
UNSUPPORTED("a3ojwgag8ldublzh73whg427o"); //     graph_t *hg;
UNSUPPORTED("cn1mqtf5jsxe76q0gt0zfucwm"); //     node_t *cn;
UNSUPPORTED("apkwzmnbl05ohutf7wes9igj2"); //     node_t *cn1;
UNSUPPORTED("7jj1xtfi4cbw5y6yhlzh0mli4"); //     node_t *t = agtail(e);
UNSUPPORTED("7sdvsuo2rbo6of75bzleek8qf"); //     node_t *h = aghead(e);
UNSUPPORTED("2dc0glyu5z80juidpelk52ugz"); //     edge_t *ce;
UNSUPPORTED("11hik2e4x4z9iutby72hbrzgx"); //     item *ip;
UNSUPPORTED("dvxbl4pteylrj225li1lb6hx2"); //     if ((ND_clustnode(h))) return;
UNSUPPORTED("6mxhjii0vool4e7v7ro88ozt"); //     tg = (strncmp(agnameof(t),"cluster",7)?(void *)0:findCluster(cmap,agnameof(t)));
UNSUPPORTED("9h0q9kw3f1tenf4puz9oi64l"); //     hg = (strncmp(agnameof(h),"cluster",7)?(void *)0:findCluster(cmap,agnameof(h)));
UNSUPPORTED("h9gulavvyc3ipss9cgex8x3k"); //     if (!tg && !hg)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("60xmxr4vqgqctofdynktmr7o1"); //     if (tg == hg) {
UNSUPPORTED("dr3nbzpzpx0aupm54j364opox"); // 	agerr(AGWARN, "cluster cycle %s -- %s not supported\n", agnameof(t),
UNSUPPORTED("4t2kykme4iu9016m9s14od6yh"); // 	      agnameof(t));
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5hnveujdt658bi20dkn93pkes"); //     ip = mapEdge(map, e);
UNSUPPORTED("bct0h7obrwmyxyjpdlga0lo14"); //     if (ip) {
UNSUPPORTED("6tq7d69sgwkznt9jdlhl5jzdj"); // 	cloneEdge(e, ip->t, ip->h);
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("85v9wkuzgj377kagk2q32243s"); //     if (hg) {
UNSUPPORTED("8esnbcsvq7wtwxyn2ds3qnzxr"); // 	if (tg) {
UNSUPPORTED("18jmtdvbt3bs4fj4fu02ry30e"); // 	    if (agcontains(hg, tg)) {
UNSUPPORTED("97u64gola1vplk8cvf8wg60bh"); // 		agerr(AGWARN, "tail cluster %s inside head cluster %s\n",
UNSUPPORTED("369r4tljymt875kkmgpkpyt8l"); // 		      agnameof(tg), agnameof(hg));
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8o34cjwrgpnrquyxl2o3bb8zk"); // 	    if (agcontains(tg, hg)) {
UNSUPPORTED("9cr5cpb8befiu9fovqfiiljjl"); // 		agerr(AGWARN, "head cluster %s inside tail cluster %s\n",
UNSUPPORTED("fxxb0u8d3qtcacdofimmuqac"); // 		      agnameof(hg),agnameof(tg));
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("71igoahydbe2dr812u1rb188n"); // 	    cn = clustNode(t, tg, xb, clg);
UNSUPPORTED("9tutq5zmo2t0l31gwnu2vwrup"); // 	    cn1 = clustNode(h, hg, xb, clg);
UNSUPPORTED("3cqe6v0gfzo0dwfrf8ns9l15c"); // 	    ce = cloneEdge(e, cn, cn1);
UNSUPPORTED("puotk7g3k7fy9jnp2axq0qqz"); // 	    insertEdge(map, t, h, ce);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("apxu8gru4cebxzthha8fwtfum"); // 	    if (agcontains(hg, t)) {
UNSUPPORTED("ch79b4wihrmywtrraz048h29q"); // 		agerr(AGWARN, "tail node %s inside head cluster %s\n",
UNSUPPORTED("c0gja8mg2a5tllq0cl44kobvl"); // 		      agnameof(t), agnameof(hg));
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("446wbqmos7b2zyj2ps1r6kuhl"); // 	    cn = clustNode(h, hg, xb, clg);
UNSUPPORTED("bhxc2l94cwt5l1wth28b2o5pp"); // 	    ce = cloneEdge(e, t, cn);
UNSUPPORTED("puotk7g3k7fy9jnp2axq0qqz"); // 	    insertEdge(map, t, h, ce);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("2ps8erulnhdu23ou4uniz2hu6"); // 	if (agcontains(tg, h)) {
UNSUPPORTED("1cjnfqs77zdcnsm0o72f90ksk"); // 	    agerr(AGWARN, "head node %s inside tail cluster %s\n", agnameof(h),
UNSUPPORTED("67ucv9k4148ltlnjqhl4rz86a"); // 		  agnameof(tg));
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2sanft0kxtxdy21i6ukf4c05t"); // 	cn = clustNode(t, tg, xb, clg);
UNSUPPORTED("5fxyznzitqqrmw44h44veynfw"); // 	ce = cloneEdge(e, cn, h);
UNSUPPORTED("c7jpyix9gxyo3vaiavoxyh155"); // 	insertEdge(map, t, h, ce);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dwutwgt4iv7io73xbnfs967wi
// int processClusterEdges(graph_t * g) 
public static Object processClusterEdges(Object... arg) {
UNSUPPORTED("4oyug57mkqcdxkzes2u1byitf"); // int processClusterEdges(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("a9ekq0g92dftp5ha575c6yu2u"); //     node_t *nxt;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("c7d686aym108b09btq8wtbaqb"); //     graph_t *clg;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("ae7vbw9q2eczpdja7cbbq50pn"); //     Dt_t *map;
UNSUPPORTED("6vtk1euczoz6qoydjb6zi2aw7"); //     Dt_t *cmap = mkClustMap (g);
UNSUPPORTED("h0or3v13348vfl22jqz895yc"); //     unsigned char buf[128];
UNSUPPORTED("ez0qg0y6sdmbdyttclx339epy"); //     map = dtopen(&mapDisc, Dtoset);
UNSUPPORTED("cpyda14dogcucergo5ci8essn"); //     clg = agsubg(g, "__clusternodes",1);
UNSUPPORTED("7gy5iqkef9rpt221qibtgi0bf"); //     agbindrec(clg, "Agraphinfo_t", sizeof(Agraphinfo_t), (!(0)));
UNSUPPORTED("ci65k77x1b3nq6luu69s87oup"); //     agxbinit(&xb, 128, buf);
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("89a8vf7uwx9v2vh8f8epycx86"); // 	if ((ND_clustnode(n))) continue;
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("7recxsc5i7btnkw1u0i1zrcke"); // 	    checkCompound(e, clg, &xb, map, cmap);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("b6u4k91yj41tlxefwoh6asd20"); //     dtclose(map);
UNSUPPORTED("bdsudu527hktokp1kvhusfmoe"); //     rv = agnnodes(clg);
UNSUPPORTED("22i66qr930abhwgxbm0wq87o9"); //     for (n = agfstnode(clg); n; n = nxt) {
UNSUPPORTED("f4noczumg69t18r8ssfanvoks"); // 	nxt = agnxtnode(clg, n);
UNSUPPORTED("d2695chnyayerv0xcynrkg7yd"); // 	agdelete(g, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d7dekled5ml4ke75zmk09qqsx"); //     agclose(clg);
UNSUPPORTED("dgwcr0xmkos3gxsam25rktyub"); //     if (rv)
UNSUPPORTED("6pk8ar1h2wlfngow4ehgw89jz"); // 	(GD_flags(g) |= 1);
UNSUPPORTED("5gkwwtk0stiam8elj7z38rq47"); //     dtclose(cmap);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5kmpn0ajfqqlgj5cw9vpfasej
// static node_t *mapN(node_t * n, graph_t * clg) 
public static Object mapN(Object... arg) {
UNSUPPORTED("buha873k6dpip6wp1k6884zn8"); // static node_t *mapN(node_t * n, graph_t * clg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8cudi8s29q4uqmilbu8c75v3p"); //     node_t *nn;
UNSUPPORTED("8i0d7frxcvvmzhidle0zin9i0"); //     char *name;
UNSUPPORTED("3mtc40uaa6dfx8pj5lh3cyn1l"); //     graph_t *g = agraphof(n);
UNSUPPORTED("av59ae8c4mws46sf5dumz1k0s"); //     Agsym_t *sym;
UNSUPPORTED("bnb5n48z4b233ir0fewnxiecu"); //     if (!((ND_clustnode(n))))
UNSUPPORTED("bp96fem54xcxrw16cmnlpell9"); // 	return n;
UNSUPPORTED("412fabs39sm0oyvvs0n9tys7h"); //     agsubnode(clg, n, 1);
UNSUPPORTED("4qyzymwl4mdlxpdvh64mgshjy"); //     name = strchr(agnameof(n), ':');
UNSUPPORTED("265kxn69043hh3vmr1ma8pbpg"); //     assert(name);
UNSUPPORTED("etbl775rsebn18g7vfu6j5ffu"); //     name++;
UNSUPPORTED("4y7kt4c0ur5ptmeorwazg3xbz"); //     if ((nn = (agnode(g,name,0))))
UNSUPPORTED("7dxo4qnacudla7r7y8669i5pg"); // 	return nn;
UNSUPPORTED("75euvmm5puodd8n68kvjjnyub"); //     nn = agnode(g, name, 1);
UNSUPPORTED("a510dla8b91um8smr1ik3n40i"); //     agbindrec(nn, "Agnodeinfo_t", sizeof(Agnodeinfo_t), (!(0)));
UNSUPPORTED("dhxjy107eberr96dmzpl8canc"); //     /* Set all attributes to default */
UNSUPPORTED("606mifyx9hb1bpagnmzxrlk9z"); //     for (sym = agnxtattr(g, AGNODE, (void *)0); sym;  (sym = agnxtattr(g, AGNODE, sym))) {
UNSUPPORTED("7rxjvkrh1kkw8g1ntegrpmqly"); // 	if (agxget(nn, sym) != sym->defval)
UNSUPPORTED("bfyji5ohpzlfb4najikk4a9cw"); // 	    agxset(nn, sym, sym->defval);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ey1y9gcof82mu9xr88pebu8s3"); //     return nn;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 789ww738n8t5kluf6zeo8zwsj
// static void undoCompound(edge_t * e, graph_t * clg) 
public static Object undoCompound(Object... arg) {
UNSUPPORTED("4gm4tyoex45q7hsr07asvlb3v"); // static void undoCompound(edge_t * e, graph_t * clg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7jj1xtfi4cbw5y6yhlzh0mli4"); //     node_t *t = agtail(e);
UNSUPPORTED("7sdvsuo2rbo6of75bzleek8qf"); //     node_t *h = aghead(e);
UNSUPPORTED("e43x1qhepqnkibbp8aphz9g47"); //     node_t *ntail;
UNSUPPORTED("9cji9jzlewigresgaoc0ejw5f"); //     node_t *nhead;
UNSUPPORTED("uleojnk3a2mlkreb46gqu0hr"); //     if (!((ND_clustnode(t)) || (ND_clustnode(h))))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("55xujazl71m0logk60wzmjvtq"); //     ntail = mapN(t, clg);
UNSUPPORTED("7g2iurz37ys33fcir84rjj0w0"); //     nhead = mapN(h, clg);
UNSUPPORTED("9bery3hjtmldzatlddlbzc00j"); //     cloneEdge(e, ntail, nhead);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6s7x7ut8o7wrwuw5nfdbknslk
// void undoClusterEdges(graph_t * g) 
public static Object undoClusterEdges(Object... arg) {
UNSUPPORTED("xsvmylok7lqoljd2ftvt8eki"); // void undoClusterEdges(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("c7d686aym108b09btq8wtbaqb"); //     graph_t *clg;
UNSUPPORTED("cpyda14dogcucergo5ci8essn"); //     clg = agsubg(g, "__clusternodes",1);
UNSUPPORTED("cxzb8zj9uk5xkq6lyiraqyh5o"); // 	agbindrec(clg, "Agraphinfo_t", sizeof(Agraphinfo_t), (!(0)));
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("e133h9i48sw1xkk4miz3q5qw6"); // 	    undoCompound(e, clg);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6fhvmrn719v1oxev0ugv8gead"); //     for (n = agfstnode(clg); n; n = agnxtnode(clg, n)) {
UNSUPPORTED("d2695chnyayerv0xcynrkg7yd"); // 	agdelete(g, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d7dekled5ml4ke75zmk09qqsx"); //     agclose(clg);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6dwase854hhoz11vcuphugbij
// attrsym_t* safe_dcl(graph_t * g, int obj_kind, char *name, char *def) 
public static Object safe_dcl(Object... arg) {
UNSUPPORTED("850d7lqcvt2dszn9wl9f6zef0"); // attrsym_t*
UNSUPPORTED("4dafq6zrc7d2eg2y3pxdhhp6k"); // safe_dcl(graph_t * g, int obj_kind, char *name, char *def)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1z0y79inqwletzkfgolwkh2mm"); //     attrsym_t *a = agattr(g,obj_kind,name, (void *)0);
UNSUPPORTED("53xvsqb1bmjlv8zo5ey6uznyf"); //     if (!a)	/* attribute does not exist */		
UNSUPPORTED("avuy3kulsc2bvd7gtxiznnkl"); // 	a = agattr(g,obj_kind,name,def);
UNSUPPORTED("3gfohtnqgemf2e1akg4je944a"); //     return a;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8vn95uhvbccfyutd9itvpk8vy
// static int comp_entities(const void *e1, const void *e2) 
public static Object comp_entities(Object... arg) {
UNSUPPORTED("28uhwy0iibw08ehww04k9s3le"); // static int comp_entities(const void *e1, const void *e2) {
UNSUPPORTED("3c7l2zx25w9zqhkqe16urhizu"); //   return strcmp(((struct entities_s *)e1)->name, ((struct entities_s *)e2)->name);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7t94y8iigozdjfx36lfzyak08
// char* scanEntity (char* t, agxbuf* xb) 
public static Object scanEntity(Object... arg) {
UNSUPPORTED("bz1schmt8gz4xlf2x79u589jz"); // char* scanEntity (char* t, agxbuf* xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ecs59z0l3j60cvfc1n8vabb0n"); //     char*  endp = strchr (t, ';');
UNSUPPORTED("bzu1jieuhoy6jahlnld5cybzk"); //     struct entities_s key, *res;
UNSUPPORTED("coa0oz6l6jdsodktc0tdnc2cb"); //     int    len;
UNSUPPORTED("90hm5x3gtehrl08u4s3bb5h5s"); //     char   buf[8+1];
UNSUPPORTED("egvn4me3pgt2km0tpawwhhk2t"); //     ((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'&')));
UNSUPPORTED("981k5uhzqy8u4eweluhu3ajcc"); //     if (!endp) return t;
UNSUPPORTED("405o5srxruhaurslvb8fxibcs"); //     if (((len = endp-t) > 8) || (len < 2)) return t;
UNSUPPORTED("1m465yt5bfo53jvd170k1s0uf"); //     strncpy (buf, t, len);
UNSUPPORTED("3w01p3l63zt07scumxz12foyc"); //     buf[len] = '\0';
UNSUPPORTED("22hhi6szy0artz9sx31yn993t"); //     key.name =  buf;
UNSUPPORTED("1p9rtkc428xvwvxqu9ulqllbq"); //     res = bsearch(&key, entities, 252,
UNSUPPORTED("6ucfe24q86v1s0pnugf94k0o1"); //         sizeof(entities[0]), comp_entities);
UNSUPPORTED("9degteg0v0woertf357m3p670"); //     if (!res) return t;
UNSUPPORTED("62f6a0uh98flc5r4sp8ye5una"); //     sprintf (buf, "%d", res->value);
UNSUPPORTED("9kxympmoesjmhxkjfyrue546g"); //     ((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'#')));
UNSUPPORTED("5q33wkzo42rb2ovinz8n32geq"); //     agxbput(xb, buf);
UNSUPPORTED("ylj9ttyordcfgahnlqhhobod"); //     ((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)';')));
UNSUPPORTED("29skqtq6yonorv6xk81t19v7j"); //     return (endp+1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6ol0iqfokq31kpiqngns9cnkm
// static int htmlEntity (char** s) 
public static Object htmlEntity(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("61r6m6shucv4zvnmx9obkeu9e"); // htmlEntity (char** s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("bzu1jieuhoy6jahlnld5cybzk"); //     struct entities_s key, *res;
UNSUPPORTED("22sdohyy8v7963m1iifyxam9d"); //     char entity_name_buf[8+1];
UNSUPPORTED("cha3ynahkzif8mjf2aasyan4p"); //     unsigned char* str = *(unsigned char**)s;
UNSUPPORTED("6iar0dtf0ozvirclx3rsu5ywk"); //     unsigned int byte;
UNSUPPORTED("4q8bcbceil82j5ssfuh8q4fhu"); //     int i, n = 0;
UNSUPPORTED("8xwfccpasv03cb2aijwrkxrt8"); //     byte = *str;
UNSUPPORTED("f1y8qnn6iohzeg8fx1fsww8wz"); //     if (byte == '#') {
UNSUPPORTED("47brdazpxh26941mcbd57m5w"); // 	byte = *(str + 1);
UNSUPPORTED("8lhwl4wznsahr1vgtzqnvsgqp"); // 	if (byte == 'x' || byte == 'X') {
UNSUPPORTED("9kdayjiewvd52f72h664pmhy4"); // 	    for (i = 2; i < 8; i++) {
UNSUPPORTED("58bvsjm19jatr5xgbuetim3c"); // 		byte = *(str + i);
UNSUPPORTED("7ry0uw9esv0a55bciqzqr7dt0"); // 		if (byte >= 'A' && byte <= 'F')
UNSUPPORTED("efm984116x5zq1dui5ll11r0k"); //                     byte = byte - 'A' + 10;
UNSUPPORTED("30cf1s08yhe1dm0wfrzai7qj9"); // 		else if (byte >= 'a' && byte <= 'f')
UNSUPPORTED("dmoz94y1j59da7u2zxu75l949"); //                     byte = byte - 'a' + 10;
UNSUPPORTED("9sus494ncxh3tbij66aefjaji"); // 		else if (byte >= '0' && byte <= '9')
UNSUPPORTED("70r2dn1krhunm4u7uw43urh5b"); //                     byte = byte - '0';
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("ai4db41odnnfnokairbrylj2a"); // 		n = (n * 16) + byte;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("bkj5d6ozqtq7ttrkpmp5p719j"); // 	    for (i = 1; i < 8; i++) {
UNSUPPORTED("58bvsjm19jatr5xgbuetim3c"); // 		byte = *(str + i);
UNSUPPORTED("2rd77g61lvh7l0oq5bxbu866w"); // 		if (byte >= '0' && byte <= '9')
UNSUPPORTED("c8u6dbnnbecfd71yts7yps8l8"); // 		    n = (n * 10) + (byte - '0');
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5kruz0luqhpkxlt2rqdj7qe9y"); // 	if (byte == ';') {
UNSUPPORTED("e7p6jds2xupttn34j7k97koje"); // 	    str += i+1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("902oeel2kkuedgae5ujakzjhy"); // 	    n = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("e63mn7kr8jrvjhx7i9grnwgnq"); // 	key.name = p = entity_name_buf;
UNSUPPORTED("avuwvye3hioreemrm282n46zz"); // 	for (i = 0; i <  8; i++) {
UNSUPPORTED("ax9o0naqvr50ld6sknp2j1b44"); // 	    byte = *(str + i);
UNSUPPORTED("ddy91zgs9bt560h592o4j4zww"); // 	    if (byte == '\0') break;
UNSUPPORTED("a4ymj4wcljef0f889dm6ypyc6"); // 	    if (byte == ';') {
UNSUPPORTED("9hm2gc1ly30ctjxthh7ce9kr4"); // 		*p++ = '\0';
UNSUPPORTED("3i427o0072j16pqppnwmfigbx"); // 		res = bsearch(&key, entities, 252,
UNSUPPORTED("d4pg41mh9zro8muax20xcn7rt"); // 		    sizeof(entities[0]), *comp_entities);
UNSUPPORTED("4b5oorrowb2d761jp0u4kh8n1"); // 		if (res) {
UNSUPPORTED("xetj9qmi9xudhpu0fa8ucl4h"); // 		    n = res->value;
UNSUPPORTED("99m08trqtau3p70at3g9nkyj6"); // 		    str += i+1;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("d79b803cv2twmevybf99njur6"); // 	    *p++ = byte;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("84mp0v655u5xocqmax8egxonv"); //     *s = (char*)str;
UNSUPPORTED("69hc24ic55i66g8tf2ne42327"); //     return n;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e3fdq03jg6298zgwjnftvhivy
// static unsigned char cvtAndAppend (unsigned char c, agxbuf* xb) 
public static Object cvtAndAppend(Object... arg) {
UNSUPPORTED("at0aua2ntxsp0j1h4yidmr4si"); // static unsigned char
UNSUPPORTED("dh6zi66v19z0wdg8u346fccp4"); // cvtAndAppend (unsigned char c, agxbuf* xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aqjwq3fk7zmi0i48c6b3bdyn1"); //     char buf[2];
UNSUPPORTED("yiuh599p05f2mpu2e3pesu2o"); //     char* s;
UNSUPPORTED("a4px33i4moqe8ybwatz0g8k6"); //     char* p;
UNSUPPORTED("dwe86466ugstemepdfk8yzphz"); //     int len;
UNSUPPORTED("5q4ts22itevk0jgej86bk287q"); //     buf[0] = c;
UNSUPPORTED("nw02pwbnc00xo3a3qlx6r8r9"); //     buf[1] = '\0';
UNSUPPORTED("20haqm26n7i0pwyh3s5hhdatk"); //     p = s = latin1ToUTF8 (buf);
UNSUPPORTED("2xuf8h1bzawprd2j4asj2e8ja"); //     len = strlen(s);
UNSUPPORTED("3l8ud5i4y8a5dq0mtvlvixp91"); //     while (len-- > 1)
UNSUPPORTED("6pe2vbb93li7px1jaorf811ph"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)*p++)));
UNSUPPORTED("3rr9o0qklmhj4jn7bft2nr8td"); //     c = *p;
UNSUPPORTED("f1430a029xzg8cabffg2k9l6j"); //     free (s);
UNSUPPORTED("bskm24m9z4b23box60oxnymv"); //     return c;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9yungx7uxqkmzfh2ub6gs9l48
// char* htmlEntityUTF8 (char* s, graph_t* g) 
public static CString htmlEntityUTF8(CString s, ST_Agraph_s g) {
ENTERING("9yungx7uxqkmzfh2ub6gs9l48","htmlEntityUTF8");
try {
	LOG2("htmlEntityUTF8 "+s);
if (s!=null) return s.duplicate();
UNSUPPORTED("1xtgr84lklglr4gz1i1m3t30"); // char* htmlEntityUTF8 (char* s, graph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a86rc8dlb4bgtjxjhv43cnmup"); //     static graph_t* lastg;
UNSUPPORTED("1gn88eczuyt54egtiulqe7y7h"); //     static boolean warned;
UNSUPPORTED("3rzf3h52xn02xrxie111286a0"); //     char*  ns;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("10sir32iwi5l2jyfgp65pihto"); //     unsigned char c;
UNSUPPORTED("4urrp9tny84a3cm8ycya896x3"); //     unsigned int v;
UNSUPPORTED("d5druw9z4e87khtgyeivjngvc"); //     int ignored;
UNSUPPORTED("4fymyfhfc3ddededhxw7cs671"); //     int uc;
UNSUPPORTED("d6z43cxggqxq7iq4puyluzkfn"); //     int ui;
UNSUPPORTED("4pgl4pn1cad2whf242bntmjre"); //     (void) ignored;
UNSUPPORTED("t65eqheg8dxzi237a648t66j"); //     if (lastg != g) {
UNSUPPORTED("emyoumradju26mhebq2bewtva"); // 	lastg = g;
UNSUPPORTED("9ys85d2ctjb1a9ra0n11o2a2r"); // 	warned = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3jwm77zyv02ukrvjv9jppejf7"); //     agxbinit(&xb, BUFSIZ, buf);
UNSUPPORTED("11zj3p308ttro04hrzowx0vrh"); //     while ((c = *(unsigned char*)s++)) {
UNSUPPORTED("3xqp191v0egfea0z3ds5p1mjt"); //         if (c < 0xC0)
UNSUPPORTED("4j200801m87vnfrkblygi6ucj"); // 	    /*
UNSUPPORTED("1egwnjpl995mff91kquf9ikvl"); // 	     * Handles properly formed UTF-8 characters between
UNSUPPORTED("8rfd02x8qlye0oo8ro9u9g8ya"); // 	     * 0x01 and 0x7F.  Also treats \0 and naked trail
UNSUPPORTED("49xkviec8w4s3zvlq13991yqh"); // 	     * bytes 0x80 to 0xBF as valid characters representing
UNSUPPORTED("bg2026u05g8jo9nm9pr39cknl"); // 	     * themselves.
UNSUPPORTED("20m1lc1moer8x00tx9ceto0iw"); // 	     */
UNSUPPORTED("6g3zbtp7zrl9i7jz1if5yi7rj"); //             uc = 0;
UNSUPPORTED("5ks80mtyizjvlnrh1bwebqrx7"); //         else if (c < 0xE0)
UNSUPPORTED("2gr59wt9ibszrzwii40dqyd5b"); //             uc = 1;
UNSUPPORTED("2su1o4swg92stlgi53k4ydm5u"); //         else if (c < 0xF0)
UNSUPPORTED("c0zrulbhqoupyvbwpwapfpc70"); //             uc = 2;
UNSUPPORTED("dveaae8p8nhz8gosmtiftudrz"); //         else if (c < 0xF8)
UNSUPPORTED("a4c1bzq46y652vgwpxsruptth"); //             uc = 3;
UNSUPPORTED("3jir07ymknf0hmb9pv9x4dr3o"); //         else {
UNSUPPORTED("ar52jlyh4qqazbcbvntg2wet6"); //             uc = -1;
UNSUPPORTED("ame11lb7ylv3rp1nhtuq383du"); //             if (!warned) {
UNSUPPORTED("7acv020k6kt3q8tholp3ex0qa"); //                 agerr(AGWARN, "UTF8 codes > 4 bytes are not currently supported (graph %s) - treated as Latin-1. Perhaps \"-Gcharset=latin1\" is needed?\n", agnameof(g));
UNSUPPORTED("qr4o1w9xvn1ayc52y0f4c8bh"); //                 warned = 1;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("ejqmh8ox9uoy02anzqhcxcrro"); //             c = cvtAndAppend (c, &xb);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("8n9ts84r09bm29qdb7v2og087"); // 	    if (uc == 0 && c == '&') {
UNSUPPORTED("3cziqu94ggcy3m9kpbpdefqgk"); // 		/* replace html entity sequences like: &amp;
UNSUPPORTED("1c1fqb40szvpdlekj4as5qcqq"); // 		 * and: &#123; with their UTF8 equivalents */
UNSUPPORTED("buyl72klnvh12cf4m578joukc"); // 	        v = htmlEntity (&s);
UNSUPPORTED("1fj0k3ba7vy9tjmjnc4d4mujb"); // 	        if (v) {
UNSUPPORTED("aso1t4v0cars70ngqafalmoeq"); // 		    if (v < 0x7F) /* entity needs 1 byte in UTF8 */
UNSUPPORTED("777bke4pyf77uol5s5d6qk0i6"); // 			c = v;
UNSUPPORTED("4wqu3fuzmckazc7eb1vvoxspn"); // 		    else if (v < 0x07FF) { /* entity needs 2 bytes in UTF8 */
UNSUPPORTED("2b9x6g0k8a00ty06llcyp6cqu"); // 			ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v >> 6) | 0xC0)));
UNSUPPORTED("el4sow483b296l5o1hy6oqkzp"); // 			c = (v & 0x3F) | 0x80;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("163d4s8voz31qrt0e4c8ysn9e"); // 		    else { /* entity needs 3 bytes in UTF8 */
UNSUPPORTED("4tpuwv4i0wslspyymoqhdxsvd"); // 			ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v >> 12) | 0xE0)));
UNSUPPORTED("8unuta6ydloexb267kdf96wi"); // 			ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)((v >> 6) & 0x3F) | 0x80)));
UNSUPPORTED("el4sow483b296l5o1hy6oqkzp"); // 			c = (v & 0x3F) | 0x80;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("atwq5cvh75c4mpqr6f8bepwkd"); //         else /* copy n byte UTF8 characters */
UNSUPPORTED("75h3d3pcb66cff2xszmbwxny0"); //             for (ui = 0; ui < uc; ++ui)
UNSUPPORTED("75m8b2zfuuay6o25xl08y168m"); //                 if ((*s & 0xC0) == 0x80) {
UNSUPPORTED("2bav6kbg19gemwcsyepjeg52u"); //                     ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("5kkh713qn8pc4dhd3omuop8qk"); //                     c = *(unsigned char*)s++;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("69mmu86j5iw8x34fdfo0k59ff"); //                 else { 
UNSUPPORTED("cjh6htddtrrxjuyqzavdlw01o"); // 		            if (!warned) {
UNSUPPORTED("8ljhi9erpokpqsnveckujskly"); // 		                agerr(AGWARN, "Invalid %d-byte UTF8 found in input of graph %s - treated as Latin-1. Perhaps \"-Gcharset=latin1\" is needed?\n", uc + 1, agnameof(g));
UNSUPPORTED("8sgutsruuu83a337z05bvytk0"); // 		                warned = 1;
UNSUPPORTED("3d2mow5zy6q4vrtc38f78ucgh"); // 		            }
UNSUPPORTED("8jh5xw3y1bjy4poswq4h2wk4n"); // 		            c = cvtAndAppend (c, &xb);
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("g2y6e9pld3899aejuqyr2x25"); // 	            }
UNSUPPORTED("28mab50dtpxfjz5h216ox1q6w"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3g7d9dp3ny3ggno64pwi7nmkg"); //     ns = strdup ((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)));
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("98aa6ybsfiu5u7r3j6fsv3snz"); //     return ns;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("9yungx7uxqkmzfh2ub6gs9l48","htmlEntityUTF8");
}
}




//3 6spvz5rdt5uhtcpz0ypysuf8j
// char* latin1ToUTF8 (char* s) 
public static Object latin1ToUTF8(Object... arg) {
UNSUPPORTED("6k189l7y3kfe03zj65a0hi02l"); // char* latin1ToUTF8 (char* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3rzf3h52xn02xrxie111286a0"); //     char*  ns;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("7yeyn4giwkk2r8xfjbwuqwdgq"); //     unsigned int  v;
UNSUPPORTED("d5druw9z4e87khtgyeivjngvc"); //     int ignored;
UNSUPPORTED("4pgl4pn1cad2whf242bntmjre"); //     (void) ignored;
UNSUPPORTED("3jwm77zyv02ukrvjv9jppejf7"); //     agxbinit(&xb, BUFSIZ, buf);
UNSUPPORTED("4z3yoswxkrk1x38246p01ai17"); //     /* Values are either a byte (<= 256) or come from htmlEntity, whose
UNSUPPORTED("7a461v6k1cu23svait528w042"); //      * values are all less than 0x07FF, so we need at most 3 bytes.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("8fjxnjq1i6zgxuaaty7wolmz2"); //     while ((v = *(unsigned char*)s++)) {
UNSUPPORTED("3tv51fniyuldzqwej04vf24jp"); // 	if (v == '&') {
UNSUPPORTED("ci2g1lymqorgpcbacxprxut4f"); // 	    v = htmlEntity (&s);
UNSUPPORTED("cdvw86q5dy4dmwr7iig71kx78"); // 	    if (!v) v = '&';
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("26nwakev47c8vp1v45upgjyp3"); // 	if (v < 0x7F)
UNSUPPORTED("bcv1n4sa19kis6cblb97g8sgx"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)v)));
UNSUPPORTED("e97czqogsdpsth2jrh2kufwur"); // 	else if (v < 0x07FF) {
UNSUPPORTED("a5h07gnh3tqz7f4mrbknarch7"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v >> 6) | 0xC0)));
UNSUPPORTED("9a4rvaow9xmxkoagejrjrt84r"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v & 0x3F) | 0x80)));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("9s2nvoskqz83rf7mkroywr1sr"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v >> 12) | 0xE0)));
UNSUPPORTED("c3mv09jqjhqt1y3zrjw9ox53o"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)((v >> 6) & 0x3F) | 0x80)));
UNSUPPORTED("9a4rvaow9xmxkoagejrjrt84r"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v & 0x3F) | 0x80)));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3g7d9dp3ny3ggno64pwi7nmkg"); //     ns = strdup ((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)));
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("98aa6ybsfiu5u7r3j6fsv3snz"); //     return ns;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 913nrt0i8mr2gg2mz9qxdqh8o
// char* utf8ToLatin1 (char* s) 
public static Object utf8ToLatin1(Object... arg) {
UNSUPPORTED("cqm25rponse4rsi686sbn1lo0"); // char*
UNSUPPORTED("8jj111wbaa8z4z3poc1q0t8y5"); // utf8ToLatin1 (char* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3rzf3h52xn02xrxie111286a0"); //     char*  ns;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("10sir32iwi5l2jyfgp65pihto"); //     unsigned char c;
UNSUPPORTED("gmcqqf9woz72oix0r5rjuoto"); //     unsigned char outc;
UNSUPPORTED("d5druw9z4e87khtgyeivjngvc"); //     int ignored;
UNSUPPORTED("4pgl4pn1cad2whf242bntmjre"); //     (void) ignored;
UNSUPPORTED("3jwm77zyv02ukrvjv9jppejf7"); //     agxbinit(&xb, BUFSIZ, buf);
UNSUPPORTED("11zj3p308ttro04hrzowx0vrh"); //     while ((c = *(unsigned char*)s++)) {
UNSUPPORTED("dbsfo5gpcng9982vxr7ikbf0i"); // 	if (c < 0x7F)
UNSUPPORTED("28mab50dtpxfjz5h216ox1q6w"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("1ppkx26s53neuwlkgoyl230ya"); // 	    outc = (c & 0x03) << 6;
UNSUPPORTED("9d685hmukj0hf94zcu28sy09x"); // 	    c = *(unsigned char*)s++;
UNSUPPORTED("55iewluciyo7pfuwv8aez09pb"); // 	    outc = outc | (c & 0x3F);
UNSUPPORTED("bb5kgu9v5ko417jskeknpttmq"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)outc)));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3g7d9dp3ny3ggno64pwi7nmkg"); //     ns = strdup ((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)));
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("98aa6ybsfiu5u7r3j6fsv3snz"); //     return ns;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9bvrwdmh7tm5thehp9lgdr6xd
// boolean overlap_node(node_t *n, boxf b) 
public static Object overlap_node(Object... arg) {
UNSUPPORTED("adn6r7oz1h6uvg69bvfoypzf2"); // boolean overlap_node(node_t *n, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("91ymmlazxnlu6a10ivd58lrnx"); //     inside_t ictxt;
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("2n2uez00ytva0jwosdu5d5x7k"); //     if (! OVERLAP(b, ND_bb(n)))
UNSUPPORTED("egywkvzo2t847qnathqnanvcj"); //         return 0;
UNSUPPORTED("2grr4abi33fuyx3ex5soymquw"); // /*  FIXME - need to do something better about CLOSEENOUGH */
UNSUPPORTED("42wj7lxtvc06e9sfoqihjt9xw"); //     p = sub_pointf(ND_coord(n), mid_pointf(b.UR, b.LL));
UNSUPPORTED("22mv67l7z7wr03u30xgx36klq"); //     ictxt.s.n = n;
UNSUPPORTED("6hiyxv9qcrh42rkfd32ag1cgg"); //     ictxt.s.bp = (void *)0;
UNSUPPORTED("1hqspal43fzxoaa3kkrp9xcb"); //     return ND_shape(n)->fns->insidefn(&ictxt, p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1iephta6pfgcjwjxaz7n7hg3h
// boolean overlap_label(textlabel_t *lp, boxf b) 
public static Object overlap_label(Object... arg) {
UNSUPPORTED("91umgryo5zqgish79s8i949au"); // boolean overlap_label(textlabel_t *lp, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ddb68hn23b0v9gldt5ym699jm"); //     pointf s;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("b8b14mpt61iyo7ntzox4didg"); //     s.x = lp->dimen.x / 2.;
UNSUPPORTED("etn8w0yctba3qwmwjypkjuh0b"); //     s.y = lp->dimen.y / 2.;
UNSUPPORTED("5ezm2f39sm5ob27ufs99ubibn"); //     bb.LL = sub_pointf(lp->pos, s);
UNSUPPORTED("1o378zsemmdu353iu9zkmnew"); //     bb.UR = add_pointf(lp->pos, s);
UNSUPPORTED("50ldl8awv60ft3ojv05xoapv8"); //     return OVERLAP(b, bb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1pi7b3b4i7f0w0nru6z6tl31b
// static boolean overlap_arrow(pointf p, pointf u, double scale, int flag, boxf b) 
public static Object overlap_arrow(Object... arg) {
UNSUPPORTED("2hwfhh60l88kcz3nw2gniuiic"); // static boolean overlap_arrow(pointf p, pointf u, double scale, int flag, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("d96dzwqsrlhqx70677lolr70x"); //     if (OVERLAP(b, arrow_bb(p, u, scale, flag))) {
UNSUPPORTED("6pyod80f1wnsh68enzsnqykcp"); // 	/* FIXME - check inside arrow shape */
UNSUPPORTED("3adr32h5e1fehu4g7j2u24asz"); // 	return (!(0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7ic0hdq8fpa9fby43hy9p96n
// static boolean overlap_bezier(bezier bz, boxf b) 
public static Object overlap_bezier(Object... arg) {
UNSUPPORTED("awzwsadmtjnsr4l5ln0gwca6f"); // static boolean overlap_bezier(bezier bz, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("2qhxg8kfts3rqa4n8y1q8tk3r"); //     pointf p, u;
UNSUPPORTED("1imabyiof6ysdoof2g58v139w"); //     assert(bz.size);
UNSUPPORTED("2cz7hdhlhljnbt4ey99pfq3mj"); //     u = bz.list[0];
UNSUPPORTED("9mjgetoviljxmz4j3h18bhwv"); //     for (i = 1; i < bz.size; i++) {
UNSUPPORTED("4nhsr51p4z60folp6yedkan7p"); // 	p = bz.list[i];
UNSUPPORTED("1q7o633q5ykuwzssixlzk97w1"); // 	if (lineToBox(p, u, b) != -1)
UNSUPPORTED("euj7k5raviuazd8jahwobe0r3"); // 	    return (!(0));
UNSUPPORTED("2zppgvcdlcmvkarerwttpl0cl"); // 	u = p;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c30ht2dw1jfnc98f1lxaoduea"); //     /* check arrows */
UNSUPPORTED("dtt0q54ov71fji9i2ae24aeyc"); //     if (bz.sflag) {
UNSUPPORTED("6d0os47s8k0liv0ij1c9ifh5h"); // 	if (overlap_arrow(bz.sp, bz.list[0], 1, bz.sflag, b))
UNSUPPORTED("euj7k5raviuazd8jahwobe0r3"); // 	    return (!(0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4o7tep38o3pxavxbzw15khp2r"); //     if (bz.eflag) {
UNSUPPORTED("bxk1lvdajxp9q7k9dzq4jjalp"); // 	if (overlap_arrow(bz.ep, bz.list[bz.size - 1], 1, bz.eflag, b))
UNSUPPORTED("euj7k5raviuazd8jahwobe0r3"); // 	    return (!(0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4usdn4gxfza3j6zxmsnoslmsu
// boolean overlap_edge(edge_t *e, boxf b) 
public static Object overlap_edge(Object... arg) {
UNSUPPORTED("dfrqxx7kxp0xo56gn56prf49k"); // boolean overlap_edge(edge_t *e, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6tl9mepc2bett364jduh2q4mf"); //     splines *spl;
UNSUPPORTED("cqwl7s9yvzr8n5v8svuuv1a1q"); //     textlabel_t *lp;
UNSUPPORTED("2c3cg84bl0xam4mk6g5f31jj0"); //     spl = ED_spl(e);
UNSUPPORTED("7hvxias4hzevaqzopdofxqtf7"); //     if (spl && boxf_overlap(spl->bb, b))
UNSUPPORTED("8u449ocpkq8pw6x28ydsauw7k"); //         for (i = 0; i < spl->size; i++)
UNSUPPORTED("2igld6ya1et1v6v507mv6oou4"); //             if (overlap_bezier(spl->list[i], b))
UNSUPPORTED("41w3xzxk3po8alpl4v9kz395f"); //                 return (!(0));
UNSUPPORTED("n9pj1f2ecz41q7q378oisbjt"); //     lp = ED_label(e);
UNSUPPORTED("26tbjilm0lmi9lsxtc9qh7qr"); //     if (lp && overlap_label(lp, b))
UNSUPPORTED("a3ueikge1f8p4avpat19zoh3a"); //         return (!(0));
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ckavkcnz5rcrqs17lleds1uxu
// int edgeType (char* s, int dflt) 
public static int edgeType(CString s, int dflt) {
ENTERING("ckavkcnz5rcrqs17lleds1uxu","edgeType");
try {
	UNSUPPORTED("h9kzapvoxea4esxgom157wc0"); // int edgeType (char* s, int dflt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("26e08yupzx95a4pzp1af0t6og"); //     int et;
UNSUPPORTED("73z43mn6ha09hbnvzynnbkvqg"); //     if (!s || (*s == '\0')) return dflt;
UNSUPPORTED("527zd48lq0ay6p16b2whyuafo"); //     et = (0 << 1);
UNSUPPORTED("1ctayzw7ya308i4wpppul6b9o"); //     switch (*s) {
UNSUPPORTED("acwxya6p4cjrbqeuf7gymcmx2"); //     case '0' :    /* false */
UNSUPPORTED("18fcibo027r3vczxrvtju3nah"); // 	et = (1 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("no8amccag0mew2zmsprfyekp"); //     case '1' :    /* true */
UNSUPPORTED("8to0pat5o6zmyuqjfl01xs9xc"); //     case '2' :
UNSUPPORTED("c7icptpasun232whn2nn5gydx"); //     case '3' :
UNSUPPORTED("44xov6gwt91mlesh02z3zvxx"); //     case '4' :
UNSUPPORTED("4dwlps5sjcl550fvks2ibv2fi"); //     case '5' :
UNSUPPORTED("cr0jhqsceb5y1hcmvtjd1ttgu"); //     case '6' :
UNSUPPORTED("8jq47j7ezu18niwotmuj92cz3"); //     case '7' :
UNSUPPORTED("ami8xk8243o5ku0cyeqxoeiut"); //     case '8' :
UNSUPPORTED("3onv8t8a6v1tmfaz8y7hk9lvv"); //     case '9' :
UNSUPPORTED("8m599inlx0lbuns9r3iiokwxw"); // 	et = (5 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("vwxe2prs0tywhf20ycwdwa8o"); //     case 'c' :
UNSUPPORTED("e2ux7lqsbmsyyrououuijooiy"); //     case 'C' :
UNSUPPORTED("8zxim9f3q8qdl919cv1v3jf8e"); // 	if (!strcasecmp (s+1, "urved"))
UNSUPPORTED("azc7d85av8k7f1to3mr59m3mz"); // 	    et = (2 << 1);
UNSUPPORTED("b7i0q9ysed6zrjftn8ilgtn0a"); // 	else if (!strcasecmp (s+1, "ompound"))
UNSUPPORTED("aihlhslp3nd26f10vuyjlnb3q"); // 	    et = (6 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("2ix1d2vw6unhjetclv9vkaw1p"); //     case 'f' :
UNSUPPORTED("2chzjgs8kmwelk00c6469lpx2"); //     case 'F' :
UNSUPPORTED("42jngi39nkk27q16s1sa7sftl"); // 	if (!strcasecmp (s+1, "alse"))
UNSUPPORTED("7xut5zuu25vrpn9gt0f3kc5hz"); // 	    et = (1 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7ozigs1hjxmhvwgapx2in25cy"); //     case 'l' :
UNSUPPORTED("c2gttjqnkmx1rnuyjknw7segb"); //     case 'L' :
UNSUPPORTED("96lnofxeiqa1g3g7s02b86h6z"); // 	if (!strcasecmp (s+1, "ine"))
UNSUPPORTED("7xut5zuu25vrpn9gt0f3kc5hz"); // 	    et = (1 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("5o5i90c7m363f5yyxamxuzok6"); //     case 'n' :
UNSUPPORTED("3ttrfea54jmrshv2796w3a9h2"); //     case 'N' :
UNSUPPORTED("6qibxt06dimtp2r5spwgriorn"); // 	if (!strcasecmp (s+1, "one")) return et;
UNSUPPORTED("bqi51jfycttyx733ls9qw2c18"); // 	if (!strcasecmp (s+1, "o")) return (1 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("18t59gw7hrgsezibz7bbm0ng3"); //     case 'o' :
UNSUPPORTED("4q6jdsek20d4i9sc5ftmm3mdl"); //     case 'O' :
UNSUPPORTED("8scb0vjws7o3davin33k87o2p"); // 	if (!strcasecmp (s+1, "rtho"))
UNSUPPORTED("48rqxx6odtdnqf676ffe1ll7g"); // 	    et = (4 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("5cc40qlotkkym6enwcv916835"); //     case 'p' :
UNSUPPORTED("al1clonjqyw2bo1z0li974ijp"); //     case 'P' :
UNSUPPORTED("68l1a5153ouil03qaammm1zty"); // 	if (!strcasecmp (s+1, "olyline"))
UNSUPPORTED("5ytop08aei3hhllfd12904hh7"); // 	    et = (3 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("20wayzvdomwexzhjzj4wojf4d"); //     case 's' :
UNSUPPORTED("boxft69fzv6rof5elda0zs33z"); //     case 'S' :
UNSUPPORTED("3qs8m2esm62d50tk701b8m0xz"); // 	if (!strcasecmp (s+1, "pline"))
UNSUPPORTED("5l4kd6c21h4bjm98grnqqwra6"); // 	    et = (5 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("ce41quxcxpj3oi50zybc75b9r"); //     case 't' :
UNSUPPORTED("8drchetff3h6zpsu3m08rqi0q"); //     case 'T' :
UNSUPPORTED("7ln0pymv14hb45h3ypy5955nk"); // 	if (!strcasecmp (s+1, "rue"))
UNSUPPORTED("5l4kd6c21h4bjm98grnqqwra6"); // 	    et = (5 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3d55ucqxr8xg0otty2j39hkgz"); //     case 'y' :
UNSUPPORTED("7oihco3xpq1kek2q2dnrfxmcx"); //     case 'Y' :
UNSUPPORTED("679wmbnx0dakltwkxx2svg5ex"); // 	if (!strcasecmp (s+1, "es"))
UNSUPPORTED("5l4kd6c21h4bjm98grnqqwra6"); // 	    et = (5 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ckjgbybnvrshn8g32qqpy0ppd"); //     if (!et) {
UNSUPPORTED("79f40sxqwmzmgk4ktfha59mxf"); // 	agerr(AGWARN, "Unknown \"splines\" value: \"%s\" - ignored\n", s);
UNSUPPORTED("mjiefsvltip3uasxic0uipa9"); // 	et = dflt;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("68yadra75shcc0tia9wr9acr4"); //     return et;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("ckavkcnz5rcrqs17lleds1uxu","edgeType");
}
}




//3 13cpqbf2ztcjdfz4a6v7nv00u
// void setEdgeType (graph_t* g, int dflt) 
public static void setEdgeType(ST_Agraph_s g, int dflt) {
ENTERING("13cpqbf2ztcjdfz4a6v7nv00u","setEdgeType");
try {
    CString s = agget(g, new CString("splines"));
    int et;
    if (N(s)) {
	et = dflt;
    }
    else if (s.charAt(0) == '\0') {
	et = (0 << 1);
    }
    else et = edgeType (s, dflt);
    GD_flags(g, GD_flags(g) | et);
} finally {
LEAVING("13cpqbf2ztcjdfz4a6v7nv00u","setEdgeType");
}
}




//3 azj18si1ncbqf4nggo3u0iudc
// void get_gradient_points(pointf * A, pointf * G, int n, float angle, int flags) 
public static Object get_gradient_points(Object... arg) {
UNSUPPORTED("4l4q9435jsq43snp6e2muhph9"); // void get_gradient_points(pointf * A, pointf * G, int n, float angle, int flags)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("28vm7zytm26lbctmzklz8u6q4"); //     double rx, ry;
UNSUPPORTED("7ab1r7u9bshphtezjbwilzdmk"); //     pointf min,max,center;
UNSUPPORTED("38fmmbbh3d9td4hgcigqek69h"); //     int isRadial = flags & 1;
UNSUPPORTED("6do922drsqikinsq2qnkg7i9s"); //     int isRHS = flags & 2;
UNSUPPORTED("cc4iipfhbkh9s7bgnq075nve2"); //     if (n == 2) {
UNSUPPORTED("2fbod9hk6xm53a1498zx6wkml"); //       rx = A[1].x - A[0].x;
UNSUPPORTED("znji09lgzos7bzfy4sn5z6so"); //       ry = A[1].y - A[0].y;
UNSUPPORTED("4rfhu73bo5qnu4zlhm9409rov"); //       min.x = A[0].x - rx;
UNSUPPORTED("e7n9ri71momrymdb7nem5dv3i"); //       max.x = A[0].x + rx;
UNSUPPORTED("5t50nyboj2kmqnva1b3oy3lej"); //       min.y = A[0].y - ry;
UNSUPPORTED("2pccas4c05y6rl29xqoxu9es1"); //       max.y = A[0].y + ry;
UNSUPPORTED("5ja432xa5mdtoxr8hjvwurrzk"); //     }    
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("2xg1e592fyexnv8p3cfuhf387"); //       min.x = max.x = A[0].x;
UNSUPPORTED("ae0uuyalysg56vdqam5vuysda"); //       min.y = max.y = A[0].y;
UNSUPPORTED("11ja3nzvfl5nh4sczk6fecswp"); //       for (i = 0; i < n; i++){
UNSUPPORTED("accly2buq5n3okrrptwc1ejg"); // 	min.x = MIN(A[i].x,min.x);
UNSUPPORTED("5ovf4hu3x4sjij6tz3pru127w"); // 	min.y = MIN(A[i].y,min.y);
UNSUPPORTED("9rhqopfwckc0sw5yyxaqkkxcd"); // 	max.x = MAX(A[i].x,max.x);
UNSUPPORTED("7qzfxgzccflltalu1yvmxzmv9"); // 	max.y = MAX(A[i].y,max.y);
UNSUPPORTED("dquo3qofk56ds5xl95lhvcthf"); //       }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("250db9ylxbfvnh9zwkamxovj9"); //       center.x = min.x + (max.x - min.x)/2;
UNSUPPORTED("485zu25v4z1ve1h6ajqnq4b3f"); //       center.y = min.y + (max.y - min.y)/2;
UNSUPPORTED("56w11dzx832thi17odfzwz5yl"); //     if (isRadial) {
UNSUPPORTED("20xmg9sm748etklimkd6243x5"); // 	double inner_r, outer_r;
UNSUPPORTED("5mry6u88h53908rs4hu6henpm"); // 	outer_r = sqrt((center.x - min.x)*(center.x - min.x) +
UNSUPPORTED("9bjr1zixvhgja23zwt2uvkyh4"); // 		      (center.y - min.y)*(center.y - min.y));
UNSUPPORTED("em02v3ahdnobwbukkna301r8e"); // 	inner_r = outer_r /4.;
UNSUPPORTED("bu17v8cb3igywmv0cbn25hlkg"); // 	if (isRHS) {
UNSUPPORTED("cfoc49xh4ohe1lpmz0h45p97f"); // 	    G[0].y = center.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("20h8j01n1ez8o51xea4v9tr2u"); // 	    G[0].y = -center.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5pfxs9x6r4mmobo90mjhvy2u2"); // 	G[0].x = center.x;
UNSUPPORTED("8mq1zl94lnuovvotzai3cjvh7"); // 	G[1].x = inner_r;
UNSUPPORTED("bbqmfpxsc6hzfuqmzu5hq1ks2"); // 	G[1].y = outer_r;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("78bc6w2jovc5xciuazkg4x65m"); // 	double half_x = max.x - center.x;
UNSUPPORTED("by2s59uka1v65wg0w519d9g5s"); // 	double half_y = max.y - center.y;
UNSUPPORTED("bgr3ej4c4109djoqphiiccj8j"); // 	double sina = sin(angle);
UNSUPPORTED("5w9swecikv2aa0cs4z4mft1ew"); // 	double cosa = cos(angle);
UNSUPPORTED("bu17v8cb3igywmv0cbn25hlkg"); // 	if (isRHS) {
UNSUPPORTED("4bj7eyt9p2z0zs62et351z8b7"); // 	    G[0].y = center.y - half_y * sina;
UNSUPPORTED("c4p6sses023prnrx3l3pnphpg"); // 	    G[1].y = center.y + half_y * sina;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("awhdug43x0ih3ky3e3ll0lutf"); // 	    G[0].y = -center.y + (max.y - center.y) * sin(angle);
UNSUPPORTED("62xeog3j69hyv8qha3n575e6d"); // 	    G[1].y = -center.y - (center.y - min.y) * sin(angle);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("expdhhgumrgtud45srwse0d78"); // 	G[0].x = center.x - half_x * cosa;
UNSUPPORTED("31bwc01mjpx1u8s4htbm6u5ey"); // 	G[1].x = center.x + half_x * cosa;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 22n1uekxezky6gx3cn22ansew
// int strcasecmp(const char *s1, const char *s2) 
public static int strcasecmp(CString s1, CString s2) {
ENTERING("22n1uekxezky6gx3cn22ansew","strcasecmp");
try {
    while ((s1.charAt(0) != '\0')
	   && (tolower(s1.charAt(0)) ==
	       tolower(s2.charAt(0)))) {
	s1=s1.plus(1);
	s2=s2.plus(1);
    }
    return tolower(s1.charAt(0)) - tolower(s2.charAt(0));
} finally {
LEAVING("22n1uekxezky6gx3cn22ansew","strcasecmp");
}
}




//3 6fpqvqq5eso7d44vai4lz77jd
// int strncasecmp(const char *s1, const char *s2, unsigned int n) 
public static Object strncasecmp(Object... arg) {
UNSUPPORTED("41sf831iel4ggk6nxgerc7lrz"); // int strncasecmp(const char *s1, const char *s2, unsigned int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f0os7tzuki1s9mllsml3zu2fd"); //     if (n == 0)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("18ggb7ihy0resf5qhplc7cqol"); //     while ((n-- != 0)
UNSUPPORTED("7tdf84gz2hakxs756j3v0w4iv"); // 	   && (tolower(*(unsigned char *) s1) ==
UNSUPPORTED("co97u0db3a8mz1pp77hkxq0h3"); // 	       tolower(*(unsigned char *) s2))) {
UNSUPPORTED("2jr0cuzm9i39xecgxx0ih0ez4"); // 	if (n == 0 || *s1 == '\0' || *s2 == '\0')
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("2hh1h5gydepd3ut3g43bzn51g"); // 	s1++;
UNSUPPORTED("7x8zh0pm8zj83pbc2d812jz90"); // 	s2++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1jrt0skm3a5djo3vfej4kwffc"); //     return tolower(*(unsigned char *) s1) - tolower(*(unsigned char *) s2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 952xm45ro3rezebjyrjins8hi
// void gv_free_splines(edge_t * e) 
public static Object gv_free_splines(Object... arg) {
UNSUPPORTED("20npjsygvjocwl1s38vrpf2nb"); // void gv_free_splines(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("4pdkymy0wuxj1yn9xqqzp2x4h"); //     if (ED_spl(e)) {
UNSUPPORTED("45jnnlrng64otru7xh43mwqbj"); //         for (i = 0; i < ED_spl(e)->size; i++)
UNSUPPORTED("aayg4ghew6m2hhtoo2f683101"); //             free(ED_spl(e)->list[i].list);
UNSUPPORTED("414vktawl0gg37cwl3atku9jm"); //         free(ED_spl(e)->list);
UNSUPPORTED("dg2iniztq3s88wtzc2oelmxcd"); //         free(ED_spl(e));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1ozc9ue0zbqu2i9gg2i5kfxx6"); //     ED_spl(e) = (void *)0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4tdxbfozyzm9hv9jau5qpr18r
// void gv_cleanup_edge(edge_t * e) 
public static Object gv_cleanup_edge(Object... arg) {
UNSUPPORTED("b1scm8t8tgb3dnua8wogcy076"); // void gv_cleanup_edge(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("puteitw4ghqbb5aix3nip4fk"); //     free (ED_path(e).ps);
UNSUPPORTED("dai9db3cozhpqeenab065cnfo"); //     gv_free_splines(e);
UNSUPPORTED("292fa7prffihy9rqhmv5lsc3f"); //     free_label(ED_label(e));
UNSUPPORTED("7l4r0amzt9h9sq0i6sirjn529"); //     free_label(ED_xlabel(e));
UNSUPPORTED("4uxpgfln4r8sqyb7odq506b46"); //     free_label(ED_head_label(e));
UNSUPPORTED("drx4uk8ssgsg67cvkt9t4u4kd"); //     free_label(ED_tail_label(e));
UNSUPPORTED("ab9j2jm37m5q6awql0tn2e5qh"); // 	/*FIX HERE , shallow cleaning may not be enough here */
UNSUPPORTED("dw3siytye4cixcrzqmb5i07qs"); // 	agdelrec(e, "Agedgeinfo_t");	
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c7yb1wfh6sfz3dklp3914m81m
// void gv_cleanup_node(node_t * n) 
public static Object gv_cleanup_node(Object... arg) {
UNSUPPORTED("d6dkt4wezkpueb74an06bmm1k"); // void gv_cleanup_node(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6fltl5u4u22i3870pyofjxwdn"); //     if (ND_pos(n)) free(ND_pos(n));
UNSUPPORTED("4wkmsp7365vb7u5fqtm2buotu"); //     if (ND_shape(n))
UNSUPPORTED("2ae2cnq7s4f15b6d6fp5i6o26"); //         ND_shape(n)->fns->freefn(n);
UNSUPPORTED("dv63sk5dujcwfkf99o6ponzqm"); //     free_label(ND_label(n));
UNSUPPORTED("5or6zu6ycx4zage9ggy1o9it4"); //     free_label(ND_xlabel(n));
UNSUPPORTED("ab9j2jm37m5q6awql0tn2e5qh"); // 	/*FIX HERE , shallow cleaning may not be enough here */
UNSUPPORTED("4tuw9fzcb28wodb3z6f4gkx19"); // 	agdelrec(n, "Agnodeinfo_t");	
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 80q488y0eqojtsm7osnfydmo5
// void gv_nodesize(node_t * n, boolean flip) 
public static void gv_nodesize(ST_Agnode_s n, int flip) {
ENTERING("80q488y0eqojtsm7osnfydmo5","gv_nodesize");
try {
    double w;
    if (flip!=0) {
        w = ((ND_height(n))*(double)72);
        ND_rw(n, w / 2);
        ND_lw(n, w / 2);
        ND_ht(n, ((ND_width(n))*(double)72));
    } 
    else {
        w = ((ND_width(n))*(double)72);
        ND_rw(n, w / 2);
        ND_lw(n, w / 2);
        ND_ht(n, ((ND_height(n))*(double)72));
    }
} finally {
LEAVING("80q488y0eqojtsm7osnfydmo5","gv_nodesize");
}
}




//3 7uxrf3fkz919opkirpd9xtuv
// double drand48(void) 
public static Object drand48(Object... arg) {
UNSUPPORTED("6vlwifmfxk6zn6tvonk13jp3i"); // double drand48(void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8ne6p4d5pykwl1d3xk0yg0ipb"); //     double d;
UNSUPPORTED("54tfrbdpedydawj9r47qspj9z"); //     d = rand();
UNSUPPORTED("1ljgw8hzttj2a3x36s83rr5be"); //     d = d / RAND_MAX;
UNSUPPORTED("3r3o80n61nmy2jv0ezi9xg2xp"); //     return d;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8lktrcvj65bvhh04y89vecvo2
// static void free_clust (Dt_t* dt, clust_t* clp, Dtdisc_t* disc) 
public static Object free_clust(Object... arg) {
UNSUPPORTED("5hvfjwzuuptbbsu4s4tmqioey"); // static void free_clust (Dt_t* dt, clust_t* clp, Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f496klm2k3ykus5018nsatnwr"); //     free (clp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 5kz2qt153vtr25aib7hx2zxma
// static Dtdisc_t strDisc = 




//3 eedsifpflx8hq0boycnhkyhwi
// static void fillMap (Agraph_t* g, Dt_t* map) 
public static Object fillMap(Object... arg) {
UNSUPPORTED("4bysjhruz2e3wqk783h9g1lup"); // static void fillMap (Agraph_t* g, Dt_t* map)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("btyorslmplj34yhlb1jx1qpbb"); //     Agraph_t* cl;
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("yiuh599p05f2mpu2e3pesu2o"); //     char* s;
UNSUPPORTED("16mpl6p9b2dpo53253q50m3sb"); //     clust_t* ip;
UNSUPPORTED("99d9j6m0161wdv2tu4wbf3ifi"); //     for (c = 1; c <= GD_n_cluster(g); c++) {
UNSUPPORTED("2teg6xg4qm17ntpo76fewfpsa"); // 	cl = GD_clust(g)[c];
UNSUPPORTED("cg72wznei3zdlwj34nrwr1p4d"); // 	s = agnameof(cl);
UNSUPPORTED("1guobssrhsi4q6tl1y3pj2ggr"); // 	if ((*(((Dt_t*)(map))->searchf))((map),(void*)(s),0001000)) {
UNSUPPORTED("dhd8f5xt5vuyyr6x2nb5v7n68"); // 	    agerr(AGWARN, "Two clusters named %s - the second will be ignored\n", s);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("1fldfpmb7y3tka24kxydupd92"); // 	    ip = (clust_t*)zmalloc(sizeof(clust_t));
UNSUPPORTED("2fjoz0gazdhoglv425xw1oq02"); // 	    ip->name = s;
UNSUPPORTED("3rtsjkui7fbtpw31kbxrcuq62"); // 	    ip->clp = cl;
UNSUPPORTED("9432myi3rgwzkx4n9bneu0s0k"); // 	    (*(((Dt_t*)(map))->searchf))((map),(void*)(ip),0000001);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("alhil57f405k0c1urfqtlldt7"); // 	fillMap (cl, map);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 82wzptlrwslbvgp3xyj03p9ba
// Dt_t* mkClustMap (Agraph_t* g) 
public static Object mkClustMap(Object... arg) {
UNSUPPORTED("1q58ugun4bvkmr2ue91rmuq8"); // Dt_t* mkClustMap (Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f34gp449n6keyz0bhwdxqsd7m"); //     Dt_t* map = dtopen (&strDisc, Dtoset);
UNSUPPORTED("5wsd9dcx8jqzp5baly29hubma"); //     fillMap (g, map);
UNSUPPORTED("e0iohuc39sfqukdjj9eddourq"); //     return map;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aohw5khae06vhhp2t4cczvcbv
// Agraph_t* findCluster (Dt_t* map, char* name) 
public static Object findCluster(Object... arg) {
UNSUPPORTED("6fo3oeygde19o95996mbrkjdk"); // Agraph_t*
UNSUPPORTED("43my0gnzq82k0lsp86rb9j31r"); // findCluster (Dt_t* map, char* name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cwq8c9jrtxtalbyxfo1tg66ys"); //     clust_t* clp = (*(((Dt_t*)(map))->searchf))((map),(void*)(name),0001000);
UNSUPPORTED("7szzt9uu8wexjhhnks6jhega5"); //     if (clp)
UNSUPPORTED("12yrb65kdfz9bwd2j2l8nqc07"); // 	return clp->clp;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("45tfw7tcm68298aro2tdiv8pc"); // 	return (void *)0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7q676xlzj32nbxuf2qlgu9xgc
// Agnodeinfo_t* ninf(Agnode_t* n) 
public static Object ninf(Object... arg) {
UNSUPPORTED("e4ol03qir8voknrrta1ulkew3"); // Agnodeinfo_t* ninf(Agnode_t* n) {return (Agnodeinfo_t*)AGDATA(n);}

throw new UnsupportedOperationException();
}




//3 3gguivz30v6fwn9nun51m5652
// Agraphinfo_t* ginf(Agraph_t* g) 
public static Object ginf(Object... arg) {
UNSUPPORTED("cjr1gck7jmlygsn7321ppbe2o"); // Agraphinfo_t* ginf(Agraph_t* g) {return (Agraphinfo_t*)AGDATA(g);}

throw new UnsupportedOperationException();
}




//3 5nsm1cj6268trw4hp3gljvk83
// Agedgeinfo_t* einf(Agedge_t* e) 
public static Object einf(Object... arg) {
UNSUPPORTED("3zxj8s1l4qy0pf2wpn6vy5ix3"); // Agedgeinfo_t* einf(Agedge_t* e) {return (Agedgeinfo_t*)AGDATA(e);}

throw new UnsupportedOperationException();
}


}
