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
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.geom__c.ccwrotatepf;
import static gen.lib.common.utils__c.late_double;
import static gen.lib.common.utils__c.late_int;
import static gen.lib.common.utils__c.late_string;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.safefile;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.abs;
import static smetana.core.JUtils.atan2;
import static smetana.core.JUtils.cos;
import static smetana.core.JUtils.enumAsInt;
import static smetana.core.JUtils.sin;
import static smetana.core.JUtils.sqrt;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_rankdir;
import static smetana.core.Macro.INSIDE;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.M_PI;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_has_port;
import static smetana.core.Macro.ND_height;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_shape;
import static smetana.core.Macro.ND_shape_info;
import static smetana.core.Macro.ND_width;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.RADIANS;
import static smetana.core.Macro.ROUND;
import static smetana.core.Macro.SQR;
import static smetana.core.Macro.SQRT2;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.fabs;
import static smetana.core.Macro.hypot;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agobj_s;
import h.ST_Agraphinfo_t;
import h.ST_boxf;
import h.ST_inside_t;
import h.ST_point;
import h.ST_pointf;
import h.ST_polygon_t;
import h.ST_port;
import h.ST_shape_desc;
import h.shape_kind;
import smetana.core.CFunctionImpl;
import smetana.core.CString;
import smetana.core.Z;
import smetana.core.__ptr__;

public class shapes__c {
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


//1 8h06z4a8bluhfqji3ysnlr3q8
// static port Center = 
/*private final static __struct__<port> Center = JUtils.from(port.class);
static {
Center.p.setDouble("x", 0);
Center.p.setDouble("y", 0);
Center.setDouble("theta", -1);
Center.setPtr("bp", null);
Center.setInt("defined", 0);
Center.setInt("constrained", 0);
Center.setInt("clip", 1);
Center.setInt("dyna", 0);
Center.setInt("order", 0);
Center.setInt("side", 0);
}*/

//1 ankops6rt7vi2sp8uptwl13x8
// static char *point_style[3] = 


//1 bdp4y4mfwmo49z2gqqtn77uhl
// static poly_desc_t star_gen = 


//1 wr8d5qdzgrixltx1i3jol1p
// static polygon_t p_polygon = 


//1 606ee1uued1p0d2o7h96efu9d
// static polygon_t p_ellipse = 
/*public static final __struct__<polygon_t> p_ellipse = JUtils.from(polygon_t.class);
static {
p_ellipse.setInt("regular", 0);
p_ellipse.setInt("peripheries", 1);
p_ellipse.setInt("sides", 1);
p_ellipse.setDouble("orientation", 0.);
p_ellipse.setDouble("distortion", 0.);
p_ellipse.setDouble("skew", 0.);
}*/

//1 7tbw5besp7yern6vgsh7q9kop
// static polygon_t p_circle = 


//1 ugo4ltv5o559q27tqn3r5bkl
// static polygon_t p_egg = 


//1 9ftn1ffly0e6ffwzth9q47uz1
// static polygon_t p_triangle = 


//1 bw9fxu2ppyosdc0fayd10ik29
// static polygon_t p_box = 
/*public final static __struct__<polygon_t> p_box = JUtils.from(polygon_t.class);
static {
p_box.setInt("regular", 0);
p_box.setInt("peripheries", 1);
p_box.setInt("sides", 4);
p_box.setDouble("orientation", 0.);
p_box.setDouble("distortion", 0.);
p_box.setDouble("skew", 0.);
}*/

//1 8y2jx4eiuwgzz2taa1yvgkxrr
// static polygon_t p_square = 


//1 cd8ymd9cpth6be42bx9hneg3k
// static polygon_t p_plaintext = 


//1 3rr68w4p4ndvjnd8v8t8kyvof
// static polygon_t p_diamond = 


//1 2vq6zitonpglah4otrj51bc
// static polygon_t p_trapezium = 


//1 1cmprhux7ohd0kvyuccronajx
// static polygon_t p_parallelogram = 


//1 cviu1ws3i3zudpmfyw9qde1g
// static polygon_t p_house = 


//1 a2801adj9tjun8kavpqhec32e
// static polygon_t p_pentagon = 


//1 bqxkc5i7w4qux0yj6fcvk6i3j
// static polygon_t p_hexagon = 


//1 9dfxrmf5irbiyglpnjva9shjs
// static polygon_t p_septagon = 


//1 8dintdmurw1k99hvel0eylkx
// static polygon_t p_octagon = 


//1 u1t73h3kmbe3dl3nesrd3lli
// static polygon_t p_note = 


//1 7rueooxoen3szgjos715fhyug
// static polygon_t p_tab = 


//1 96p5brfwqh960bh90nko57q5y
// static polygon_t p_folder = 


//1 asszwikv8vsd8r82g9g2dpib0
// static polygon_t p_box3d = 


//1 6svqv9h6e8w1upvle3kvqalpz
// static polygon_t p_component = 


//1 eupkzejle0xh3lxkum31v0cym
// static polygon_t p_underline = 


//1 dhsf0ibx3e26whwyfd0b6vwxe
// static polygon_t p_doublecircle = 


//1 bnqceu0ie276qgntzbxk8rasy
// static polygon_t p_invtriangle = 


//1 b4qh6rkxlk3dhq3za50nz70g9
// static polygon_t p_invtrapezium = 


//1 6975h7vqnuq4k5wgxu223q48a
// static polygon_t p_invhouse = 


//1 1y504l91pfltem8lf4b9yugmh
// static polygon_t p_doubleoctagon = 


//1 dejwm44vd6au8zngxmnsctb6h
// static polygon_t p_tripleoctagon = 


//1 cm0diez4xczfsq1e49z5xyx9l
// static polygon_t p_Mdiamond =     


//1 apcunukhbaji0umf6h2rajwjk
// static polygon_t p_Msquare = 


//1 4aby821sp86fcw8lihjx1u1t5
// static polygon_t p_Mcircle =     


//1 8w3de46truvv9a11sqxbzi3gd
// static polygon_t p_star = 


//1 2q69w7roanbt7ldossjvr2zpp
// static polygon_t p_promoter = 


//1 8lsvyuy90dk6sxfcfd1g751u5
// static polygon_t p_cds = 


//1 73gtceras7h3x5rqur5zh20pm
// static polygon_t p_terminator = 


//1 1ggglfhw5mb7uo3xo8fjy78ly
// static polygon_t p_utr = 


//1 88ze1f92hnc02v5rtzevj6laf
// static polygon_t p_insulator = 


//1 4gnk4h7twwbbkfsm2v2z5kpm3
// static polygon_t p_ribosite = 


//1 9p4ragp400660i10e7kw35j6h
// static polygon_t p_rnastab = 


//1 cj4q7wai5wonk0e937ggwye7a
// static polygon_t p_proteasesite = 


//1 cyxbjzhf18t5oktxygjnjdzvr
// static polygon_t p_proteinstab = 


//1 9ddzvwdbr4wdohmir5y8a0fvd
// static polygon_t p_primersite = 


//1 9istclblyhg67yp72g9xs9bwg
// static polygon_t p_restrictionsite = 


//1 11kgrzyf2g5uc9sq4sbrd2smb
// static polygon_t p_fivepoverhang = 


//1 6wf3da8bdlf8f8mo2u6czpmh0
// static polygon_t p_threepoverhang = 


//1 97vug445f04zvds36aoa0xylg
// static polygon_t p_noverhang = 


//1 86ma4kod9amw5w024mnxyhfkj
// static polygon_t p_assembly = 


//1 23k9m2gdw1fj7q7pdlrcsawl2
// static polygon_t p_signature = 


//1 ab97s73vl8iehclhx49x8wby5
// static polygon_t p_rpromoter = 


//1 1vm63ixobrd8mt967caec1jb
// static polygon_t p_rarrow = 


//1 7k1qamsg50xfpik4fmzl26vs1
// static polygon_t p_larrow = 


//1 659vs9ww96a1ojqko4w4ezt71
// static polygon_t p_lpromoter = 


//1 dkgul6r2xulzqk2twms3pswmy
// static shape_functions poly_fns = 
/*public final static __struct__<shape_functions> poly_fns =  JUtils.from(shape_functions.class);
static {
poly_fns.setPtr("initfn", function(shapes__c.class, "poly_init"));
poly_fns.setPtr("freefn", function(shapes__c.class, "poly_free"));
poly_fns.setPtr("portfn", function(shapes__c.class, "poly_port"));
poly_fns.setPtr("insidefn", function(shapes__c.class, "poly_inside"));
poly_fns.setPtr("pboxfn", function(shapes__c.class, "poly_path"));
poly_fns.setPtr("codefn", function(shapes__c.class, "poly_gencode"));
}*/

//1 10ii4j68l49hzbm38qspv8azn
// static shape_functions point_fns = 


//1 dkmcmdu75stv4q1ti875sba5e
// static shape_functions record_fns = 


//1 4im5kxw9mpwlfxnlet8fn1lbb
// static shape_functions epsf_fns = 


//1 6hffa4ola6av0i6ls8q0b11fp
// static shape_functions star_fns = 


//1 dkylqbkoarh3lf3lwhnbg2k6n
// static shape_desc Shapes[] = 
/*public static shape_desc Shapes[] = {
__Shapes__("box", poly_fns.amp(), p_box.amp()),
__Shapes__("ellipse", poly_fns.amp(), p_ellipse.amp()),
__Shapes__(null, null, null)
};
private static shape_desc __Shapes__(String s, shape_functions shape_functions, polygon_t polygon) {
	shape_desc result = (shape_desc) Memory.malloc(shape_desc.class);
	result.setPtr("name", s==null?null:new CString(s));
	result.setPtr("fns", shape_functions);
	result.setPtr("polygon", polygon);
	return result;
}*/



//3 a7copj498to9ai2kxtg728mex
// static void unrecognized(node_t * n, char *p) 
public static Object unrecognized(Object... arg) {
UNSUPPORTED("b1ov3x4b2q0ypsqw7wt22050l"); // static void unrecognized(node_t * n, char *p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5otcb2ylyazlaj74cmk4zgv4b"); //     agerr(AGWARN, "node %s, port %s unrecognized\n", agnameof(n), p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1nifps8w2xj8fe3s0e934h9oo
// static double quant(double val, double q) 
public static Object quant(Object... arg) {
UNSUPPORTED("d05378246jv81jc6hotxpmh7r"); // static double quant(double val, double q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("c1slbwawuf9uwqrj6g1sbwrg8"); //     i = val / q;
UNSUPPORTED("7erjq82ff3yxvdt20sqrud101"); //     if (i * q + .00001 < val)
UNSUPPORTED("chd2f5z6rt19lbaye25ej7q6j"); // 	i++;
UNSUPPORTED("8wst82emkqjm2k0x2velpv3yg"); //     return i * q;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eb4jyrh981apg1fy13fczexdl
// static int same_side(pointf p0, pointf p1, pointf L0, pointf L1) 
public static boolean same_side(final ST_pointf p0, final ST_pointf p1, final ST_pointf L0, final ST_pointf L1) {
// WARNING!! STRUCT
return same_side_w_(p0.copy(), p1.copy(), L0.copy(), L1.copy());
}
private static boolean same_side_w_(final ST_pointf p0, final ST_pointf p1, final ST_pointf L0, final ST_pointf L1) {
ENTERING("eb4jyrh981apg1fy13fczexdl","same_side");
try {
    boolean s0, s1;
    double a, b, c;
    /* a x + b y = c */
    a = -(L1.y - L0.y);
    b = (L1.x - L0.x);
    c = a * L0.x + b * L0.y;
    s0 = (a * p0.x + b * p0.y - c >= 0);
    s1 = (a * p1.x + b * p1.y - c >= 0);
    return (s0 == s1);
} finally {
LEAVING("eb4jyrh981apg1fy13fczexdl","same_side");
}
}




//3 52h7f4vpu1pj6fqh78wme87uk
// static char* penColor(GVJ_t * job, node_t * n) 
public static Object penColor(Object... arg) {
UNSUPPORTED("9y7ivrr4l5s7wx7hbbkjpd2fr"); // static
UNSUPPORTED("eghunof84ahzb2mfw01ut6dd0"); // char* penColor(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5zltq70xm6o2q24ddyqe6noyn"); //     char *color;
UNSUPPORTED("bxrfaqnhsarmdux9xb7mmda3s"); //     color = late_nnstring(n, N_color, "");
UNSUPPORTED("2fh160md1qjnnrenoofa70yeb"); //     if (!color[0])
UNSUPPORTED("ehe6g5s4foni0psldwx2ikwnq"); // 	color = "black";
UNSUPPORTED("47mxcz4gh3xivg50vcji1zkle"); //     gvrender_set_pencolor(job, color);
UNSUPPORTED("alz5uk378ys9cjg2kl4rkoa4b"); //     return color;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7nofo7jo7hj6ehl6tswgfwzif
// static char *findFillDflt(node_t * n, char *dflt) 
public static Object findFillDflt(Object... arg) {
UNSUPPORTED("9y7ivrr4l5s7wx7hbbkjpd2fr"); // static
UNSUPPORTED("1nwxf9hr4zzdjisa91w1o50qu"); // char *findFillDflt(node_t * n, char *dflt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5zltq70xm6o2q24ddyqe6noyn"); //     char *color;
UNSUPPORTED("ccgrxng9qnylk5i99hjlnht74"); //     color = late_nnstring(n, N_fillcolor, "");
UNSUPPORTED("7c0tonis6b4g7x5vn96sh0eyj"); //     if (!color[0]) {
UNSUPPORTED("bsd93fqmnbkaqhs33r2qn4gwf"); // 	/* for backward compatibilty, default fill is same as pen */
UNSUPPORTED("25wuessppk24dv6psp4kvxhr"); // 	color = late_nnstring(n, N_color, "");
UNSUPPORTED("9bxthpzdy9nuxnf8yj7gaypx6"); // 	if (!color[0]) {
UNSUPPORTED("f32mf5kh8wtwe3dkjadu7hhm8"); // 	    color = dflt;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("alz5uk378ys9cjg2kl4rkoa4b"); //     return color;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dkh0fr3loytegfs1lz9qi1sd6
// static char *findFill(node_t * n) 
public static Object findFill(Object... arg) {
UNSUPPORTED("9y7ivrr4l5s7wx7hbbkjpd2fr"); // static
UNSUPPORTED("999wldpj7kbc3jf5qrtjs3kac"); // char *findFill(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2s92vukfnygadoexil3zao2b"); //     return (findFillDflt(n, "lightgrey"));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b6vz4rofh7tr27tzdul6mqinz
// char *findAttrColor(void *obj, attrsym_t *colorattr, char *dflt)
public static Object findAttrColor(Object... arg) {
UNSUPPORTED("5v9kutabxqrex87ezkjge03mz"); // char *findAttrColor(void *obj, attrsym_t *colorattr, char *dflt){
UNSUPPORTED("5zltq70xm6o2q24ddyqe6noyn"); //     char *color;
UNSUPPORTED("ec2uw9vki5v50hgtkdhn7xx5a"); //     if(colorattr != NULL)
UNSUPPORTED("3mxnhdjc8yzcnelc8jqfu1vp9"); //       color = late_nnstring(obj, colorattr, dflt);
UNSUPPORTED("14ipy30gtp782xdiwpi735st"); //     else if(dflt != NULL && dflt[0])
UNSUPPORTED("2xj2lztryr9os0vo3pmwf6f6p"); //       color = dflt;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("1s8rhhkx6hn7y21eh14g1douw"); //       color = "lightgrey";
UNSUPPORTED("alz5uk378ys9cjg2kl4rkoa4b"); //     return color;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 arypshfdif4tg03g6bv68e74s
// static int isBox (node_t* n) 
public static Object isBox(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("1fh5whcpicn37jal4or4fzmhe"); // isBox (node_t* n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("599v2ggusx2npjbgg129z2mwn"); //     polygon_t *p;
UNSUPPORTED("euj05ebp04ciftzza0knizzej"); //     if ((p = ND_shape(n)->polygon)) {
UNSUPPORTED("6brfvvb4tyn70jzd3jn6c1gwo"); // 	return (p->sides == 4 && (ROUND(p->orientation) % 90) == 0 && p->distortion == 0. && p->skew == 0.);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ddsagt9m8yt7n8h3nk19jnk2p
// static int isEllipse(node_t* n) 
public static Object isEllipse(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("dj5on95sulam3ulc2tw8duoeq"); // isEllipse(node_t* n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("599v2ggusx2npjbgg129z2mwn"); //     polygon_t *p;
UNSUPPORTED("euj05ebp04ciftzza0knizzej"); //     if ((p = ND_shape(n)->polygon)) {
UNSUPPORTED("advye1fx4h5jqr6x6lq6qsjnb"); // 	return (p->sides <= 2);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e6hjh17xf1s408w7nmf95mnvn
// static char **checkStyle(node_t * n, int *flagp) 
public static Object checkStyle(Object... arg) {
UNSUPPORTED("ez47sxvuvz2y431fczf0ih52b"); // static char **checkStyle(node_t * n, int *flagp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("byo308l1rmve5rmx8wt32juux"); //     char *style;
UNSUPPORTED("1m69eum6fcxl62kbxksis88or"); //     char **pstyle = 0;
UNSUPPORTED("1y4qclfm9xrlqe6oi5k512dih"); //     int istyle = 0;
UNSUPPORTED("7b0667dpeiekddi69gpywx92t"); //     polygon_t *poly;
UNSUPPORTED("8pjk3xuss8ew2p7e7t0djo8qp"); //     style = late_nnstring(n, N_style, "");
UNSUPPORTED("4ouo3ttcnk1yyzsz5wrt88zw"); //     if (style[0]) {
UNSUPPORTED("h7xdp8l98vgmu8dkibee5kcm"); // 	char **pp;
UNSUPPORTED("9uleufnyvtzwmq2wbcnmeyp3a"); // 	char **qp;
UNSUPPORTED("1ys4x1uj0hoyf2yuvrmddwh9i"); // 	char *p;
UNSUPPORTED("6akvfrhnmmgier60pl8bkt42r"); // 	pp = pstyle = parse_style(style);
UNSUPPORTED("9vj0v0tutidnb2hxprdosjm39"); // 	while ((p = *pp)) {
UNSUPPORTED("9e8iza5oo9qnprgvfs80p1cmq"); // 	    if ((*(p)==*("filled")&&!strcmp(p,"filled"))) {
UNSUPPORTED("5uu1d9grveb0bmsov10mqrtj3"); // 		istyle |= (1 << 0);
UNSUPPORTED("5lcmi10wuwp3fyn36cloc29y3"); // 		pp++;
UNSUPPORTED("3uvy87owpxhpcjijxbeue7ml9"); // 	    } else if ((*(p)==*("rounded")&&!strcmp(p,"rounded"))) {
UNSUPPORTED("dtj2r1tvn8fuoefvhmbg7k8es"); // 		istyle |= (1 << 2);
UNSUPPORTED("8dbdrjhappnkp11tqkg67i250"); // 		qp = pp;	/* remove rounded from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("2dnl06ux4flpusn9o6uoauj6f"); // 		    *(qp - 1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("3c5cnairumxqyot9agywkupmf"); // 	    } else if ((*(p)==*("diagonals")&&!strcmp(p,"diagonals"))) {
UNSUPPORTED("esqbhmned54nx3htomtcxqdvl"); // 		istyle |= (1 << 3);
UNSUPPORTED("36pjs10mstioxkt5rmh4iask6"); // 		qp = pp;	/* remove diagonals from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("2dnl06ux4flpusn9o6uoauj6f"); // 		    *(qp - 1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("st5o1gk88otja1grsb6lfacw"); // 	    } else if ((*(p)==*("invis")&&!strcmp(p,"invis"))) {
UNSUPPORTED("9r65ku75by3pt573pvd9je65n"); // 		istyle |= (1 << 5);
UNSUPPORTED("5lcmi10wuwp3fyn36cloc29y3"); // 		pp++;
UNSUPPORTED("e2fhjqum3y3u10hfayo9loxxn"); // 	    } else if ((*(p)==*("radial")&&!strcmp(p,"radial"))) {
UNSUPPORTED("9gjvnf8gjbb6f0zv7dyeerzjy"); // 		istyle |= ((1 << 1)|(1 << 0));
UNSUPPORTED("cumhwwp133cqb8wq8izofb58h"); // 		qp = pp;	/* remove radial from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("2dnl06ux4flpusn9o6uoauj6f"); // 		    *(qp - 1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("dnu7ns5prehtumzxvv0s4smtc"); // 	    } else if ((*(p)==*("striped")&&!strcmp(p,"striped")) && isBox(n)) {
UNSUPPORTED("2u98nt7rt9sd9aapq55s8ew7h"); // 		istyle |= (1 << 6);
UNSUPPORTED("24ciiasyae2z41bjr8zmt7b2o"); // 		qp = pp;	/* remove striped from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("2dnl06ux4flpusn9o6uoauj6f"); // 		    *(qp - 1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("eqw80tpippiilbiit1pnwqkdk"); // 	    } else if ((*(p)==*("wedged")&&!strcmp(p,"wedged")) && isEllipse(n)) {
UNSUPPORTED("bszmjhb5tngdseiirkqdspwzj"); // 		istyle |= (1 << 9);
UNSUPPORTED("11bhw5dod5c1mltace9v23vvq"); // 		qp = pp;	/* remove wedged from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("2dnl06ux4flpusn9o6uoauj6f"); // 		    *(qp - 1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("5lcmi10wuwp3fyn36cloc29y3"); // 		pp++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a951gj8apiferiznswhme5q28"); //     if ((poly = ND_shape(n)->polygon))
UNSUPPORTED("enpv6kdyy99zkq0pgqfvhe4c1"); // 	istyle |= poly->option;
UNSUPPORTED("8wc6gkmat3lo0h81wd7xv879k"); //     *flagp = istyle;
UNSUPPORTED("iuoxnbeo1cwfgz97k92ylegx"); //     return pstyle;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cmfha87baduerx07enpoxpl0i
// static int stylenode(GVJ_t * job, node_t * n) 
public static Object stylenode(Object... arg) {
UNSUPPORTED("2zooek35a9pjrcqa212ed7d4d"); // static int stylenode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1lh27pielrns99pzh9yww5tc3"); //     char **pstyle, *s;
UNSUPPORTED("at5a8doos9xnvfmdynzdnj22m"); //     int istyle;
UNSUPPORTED("75w3zx2oz7s1yf7arcxf48heo"); //     double penwidth;
UNSUPPORTED("45vqmqwfbzihkp69fkdmro84u"); //     if ((pstyle = checkStyle(n, &istyle)))
UNSUPPORTED("5ewqs6gfz9r9n25gcsbn054ej"); // 	gvrender_set_style(job, pstyle);
UNSUPPORTED("aajxt23i7arb7fnkpedw53kj3"); //     if (N_penwidth && ((s = agxget(n, N_penwidth)) && s[0])) {
UNSUPPORTED("9ahr3ul831dw5ggnbkxpjttyu"); // 	penwidth = late_double(n, N_penwidth, 1.0, 0.0);
UNSUPPORTED("a85jezrt5nu63vkla8bslhzcx"); // 	gvrender_set_penwidth(job, penwidth);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8jt5dmpnjjn7vbyh8jm3z92dd"); //     return istyle;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 484y5dade0mh0rhmlpq9sbgyj
// static void Mcircle_hack(GVJ_t * job, node_t * n) 
public static Object Mcircle_hack(Object... arg) {
UNSUPPORTED("14yetc0y5dql9a1m4hjimwery"); // static void Mcircle_hack(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4g01jt8p980itgxzog49s8ur3"); //     double x, y;
UNSUPPORTED("73immmni8bd7nbea87pqqtsss"); //     pointf AF[2], p;
UNSUPPORTED("91olkqdwuv079mwgafkjm2pvm"); //     y = .7500;
UNSUPPORTED("1ij13nw7td2w3yobqltvugja4"); //     x = .6614;			/* x^2 + y^2 = 1.0 */
UNSUPPORTED("idkjly9lhs1takhai4jmwuyq"); //     p.y = y * ND_ht(n) / 2.0;
UNSUPPORTED("751pbavi8681zqnswp9rwue5o"); //     p.x = ND_rw(n) * x;		/* assume node is symmetric */
UNSUPPORTED("7mid2xd4xh4xgdhe907o6g6l4"); //     AF[0] = add_pointf(p, ND_coord(n));
UNSUPPORTED("a2250pkc00wvt8bx8azxq1ug0"); //     AF[1].y = AF[0].y;
UNSUPPORTED("cqt3b70row8txs0n26fb4un7u"); //     AF[1].x = AF[0].x - 2 * p.x;
UNSUPPORTED("9lgjyllrdl3lrvuyrlz3hkakl"); //     gvrender_polyline(job, AF, 2);
UNSUPPORTED("fim2su3akeazbrjzelz70baa"); //     AF[0].y -= 2 * p.y;
UNSUPPORTED("a2250pkc00wvt8bx8azxq1ug0"); //     AF[1].y = AF[0].y;
UNSUPPORTED("9lgjyllrdl3lrvuyrlz3hkakl"); //     gvrender_polyline(job, AF, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ei77gihju88eavrphjd4217d0
// void round_corners(GVJ_t * job, pointf * AF, int sides, int style, int filled) 
public static Object round_corners(Object... arg) {
UNSUPPORTED("4zly0db3w4vfht908qlfjvgr1"); // void round_corners(GVJ_t * job, pointf * AF, int sides, int style, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3z10hnxy3lvnkbnaai7c7ccl"); //     pointf *B, C[5], *D, p0, p1;
UNSUPPORTED("81yo6al7e3nxtf9ekuqa4n6xc"); //     double rbconst, d, dx, dy, t;
UNSUPPORTED("b9m8kb2lfopred94uilevubat"); //     int i, seg, mode, shape;
UNSUPPORTED("48n1zwofayobr58hhiz0y5wfp"); //     pointf* pts;
UNSUPPORTED("erycpwhkizj18xsodfbapugg"); //     shape = style & (127 << 24);
UNSUPPORTED("17t8enyxf5yj5x4qmggz27rpj"); //     if (style & (1 << 3))
UNSUPPORTED("209ali7eb7s6xpcplnxc1n105"); // 	mode = (1 << 3);
UNSUPPORTED("2jjd9xuwi3k89b2qz48p9qiij"); //     else if (style & (127 << 24))
UNSUPPORTED("69pqj1vo4pr13r0r02kyk8rtq"); // 	mode = shape;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("b3q1yyf621vvgphjqrr5ryare"); // 	mode = (1 << 2);
UNSUPPORTED("6ssyjdjxo2ulqor8iwagn9d2f"); //     B = (pointf*)zmalloc((4 * sides + 4)*sizeof(pointf));
UNSUPPORTED("9z3er49pc4h2rxja5r9grdo0h"); //     i = 0;
UNSUPPORTED("8r6c7fd8ovc5djxlr8ewm50r2"); //     /* rbconst is distance offset from a corner of the polygon.
UNSUPPORTED("2gs01cac7peva58o5tfygbktx"); //      * It should be the same for every corner, and also never
UNSUPPORTED("7jv69y6bs582woz54uifrosyf"); //      * bigger than one-third the length of a side.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("3y9cq08hyl44nlpgdz8bq9x5y"); //     rbconst = 12;
UNSUPPORTED("a194n3zgdgtwg7jus6dt8zt8m"); //     for (seg = 0; seg < sides; seg++) {
UNSUPPORTED("8u84gg4ehm9gt01gwlriapp7p"); // 	p0 = AF[seg];
UNSUPPORTED("jrhdb2qs0vcycqv19gjkoxpb"); // 	if (seg < sides - 1)
UNSUPPORTED("3a621ve4bfuqz53sahxjnie0n"); // 	    p1 = AF[seg + 1];
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("a2k7gegucha0lsv4ojjyg8bry"); // 	    p1 = AF[0];
UNSUPPORTED("5wygoyf2uskp90f77rq77i4sl"); // 	dx = p1.x - p0.x;
UNSUPPORTED("crmaptew4roh76io36shmz912"); // 	dy = p1.y - p0.y;
UNSUPPORTED("4tigxgedxe60aamdjuhhrwsx4"); // 	d = sqrt(dx * dx + dy * dy);
UNSUPPORTED("corgbpn6r0vxd6q04wn8gq4gq"); // 	rbconst = MIN(rbconst, d / 3.0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a194n3zgdgtwg7jus6dt8zt8m"); //     for (seg = 0; seg < sides; seg++) {
UNSUPPORTED("8u84gg4ehm9gt01gwlriapp7p"); // 	p0 = AF[seg];
UNSUPPORTED("jrhdb2qs0vcycqv19gjkoxpb"); // 	if (seg < sides - 1)
UNSUPPORTED("3a621ve4bfuqz53sahxjnie0n"); // 	    p1 = AF[seg + 1];
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("a2k7gegucha0lsv4ojjyg8bry"); // 	    p1 = AF[0];
UNSUPPORTED("5wygoyf2uskp90f77rq77i4sl"); // 	dx = p1.x - p0.x;
UNSUPPORTED("crmaptew4roh76io36shmz912"); // 	dy = p1.y - p0.y;
UNSUPPORTED("4tigxgedxe60aamdjuhhrwsx4"); // 	d = sqrt(dx * dx + dy * dy);
UNSUPPORTED("4ohox9viufh5hz01z2sjq8eo6"); // 	t = rbconst / d;
UNSUPPORTED("7w7drxi1wvf81230kfb4tx9sf"); // 	if (shape == (4 << 24) || shape == (5 << 24))
UNSUPPORTED("5xzdqgzfi0wjoiatowdjh64gy"); // 	    t /= 3;
UNSUPPORTED("a67ot847ab7bmt4df22rcjqjh"); // 	else if (shape == (1 << 24))
UNSUPPORTED("7crlw5k29pn4silm51hmqn309"); // 	    t /= 2;
UNSUPPORTED("tt3u4mg8fwhys49ftrmyhoo8"); // 	if (mode != (1 << 2))
UNSUPPORTED("8z8165i54px2guqyp4r3iivm"); // 	    B[i++] = p0;
UNSUPPORTED("df5svfeo1imlc8qm6azeqkkmh"); // 	else 
UNSUPPORTED("1c0c66imkoa2k4geseydtrlks"); // 	    B[i++] = interpolate_pointf(.5 * t, p0, p1);
UNSUPPORTED("3tiws3vikebhgs3g79mlh5s0a"); // 	B[i++] = interpolate_pointf(t, p0, p1);
UNSUPPORTED("ck1de7cjc9kz82t8n4b3kaply"); // 	B[i++] = interpolate_pointf(1.0 - t, p0, p1);
UNSUPPORTED("5ecb0sv32aezfln59kwr7n54j"); // 	if (mode == (1 << 2))
UNSUPPORTED("4yqnq3vzqlqucce76lf2i48sh"); // 	    B[i++] = interpolate_pointf(1.0 - .5 * t, p0, p1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("456xy9zxbdvxkgw63cphz32yv"); //     B[i++] = B[0];
UNSUPPORTED("a3zl8b5gohcvf0aamq9zy3r2z"); //     B[i++] = B[1];
UNSUPPORTED("1rw55m45llp1oiu1b664bdtt7"); //     B[i++] = B[2];
UNSUPPORTED("1n2j212wuyr8t1oi45d91ta7r"); //     switch (mode) {
UNSUPPORTED("2buge9zhgso34sq694br4vpn6"); //     case (1 << 2):
UNSUPPORTED("4zyg1qcp15ft55bhiug326st9"); // 	pts = (pointf*)gmalloc((6 * sides + 2)*sizeof(pointf));
UNSUPPORTED("5or0zebpgtvozlmal8j2q1ymh"); // 	i = 0;
UNSUPPORTED("2b4hmqn28jyawsresopw3imez"); // 	for (seg = 0; seg < sides; seg++) {
UNSUPPORTED("4dwf576bgn14ur203rpzfmvcj"); // 	    pts[i++] = B[4 * seg];
UNSUPPORTED("bal6cvz4s8gwpl3au15cp6gut"); // 	    pts[i++] = B[4 * seg+1];
UNSUPPORTED("bal6cvz4s8gwpl3au15cp6gut"); // 	    pts[i++] = B[4 * seg+1];
UNSUPPORTED("9qw0jg5a0jzxhnoz56auikdah"); // 	    pts[i++] = B[4 * seg+2];
UNSUPPORTED("9qw0jg5a0jzxhnoz56auikdah"); // 	    pts[i++] = B[4 * seg+2];
UNSUPPORTED("7u491erkgkpheh23pmb7ay6tm"); // 	    pts[i++] = B[4 * seg+3];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bmzoxzu4zo3s8tlzc1m3x4e1v"); // 	pts[i++] = pts[0];
UNSUPPORTED("3e5hosajkvxgm8usnlbhxcuir"); // 	pts[i++] = pts[1];
UNSUPPORTED("93i0re7fzom2m1gwn8dnxcylj"); // 	gvrender_beziercurve(job, pts+1, i-1, 0, 0, filled);
UNSUPPORTED("dgq0qjac3ho7gugxw0mqbv96"); // 	free (pts);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("14wf8f4gfb2l2ihvry7d2x38l"); //     case (1 << 3):
UNSUPPORTED("azp5izjs4eyq3uy0ah00hm8n4"); // 	/* diagonals are weird.  rewrite someday. */
UNSUPPORTED("5j9owdiqcj7bt8mcr301dht8q"); // 	gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("2b4hmqn28jyawsresopw3imez"); // 	for (seg = 0; seg < sides; seg++) {
UNSUPPORTED("9ryfluah120xc0oke5t971wtf"); // 	    C[0] = B[3 * seg + 2];
UNSUPPORTED("cqr6f1xlnm7nng28pnfor5h9b"); // 	    C[1] = B[3 * seg + 4];
UNSUPPORTED("b0gt3f20500y9ckfsqr07bwn9"); // 	    gvrender_polyline(job, C, 2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9fjpgho6bfz7z8s29gy8e6uoc"); //     case (1 << 24):
UNSUPPORTED("8wy6a8izye4sz73njhv8j9znc"); // 	/* Add the cutoff edge. */
UNSUPPORTED("b5f90htb83l32kamg97jm1sux"); // 	D = (pointf*)zmalloc((sides + 1)*sizeof(pointf));
UNSUPPORTED("aofwnlg1s6y08lf7550rpgt6b"); // 	for (seg = 1; seg < sides; seg++)
UNSUPPORTED("6sqx87841cn900qusanuebotn"); // 	    D[seg] = AF[seg];
UNSUPPORTED("aoly4odhqt3ydqpni1b56wx0a"); // 	D[0] = B[3 * (sides - 1) + 4];
UNSUPPORTED("dike7lk77zy9w7gw1blk27kjr"); // 	D[sides] = B[3 * (sides - 1) + 2];
UNSUPPORTED("6u1z6h8gfy4ofbsa9gidwp2v9"); // 	gvrender_polygon(job, D, sides + 1, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("eoq3dhyls1a02czm0kv7mk15"); // 	/* Draw the inner edge. */
UNSUPPORTED("b0pvjrkdu25er5c08tx04ycvn"); // 	seg = sides - 1;
UNSUPPORTED("aojv0k5pq5qdtvzl90n7o2z4a"); // 	C[0] = B[3 * seg + 2];
UNSUPPORTED("ak39zpyk8ew0qkrfdq4o6vulb"); // 	C[1] = B[3 * seg + 4];
UNSUPPORTED("zasq52jvz91zaqrusqeppioc"); // 	C[2].x = C[1].x + (C[0].x - B[3 * seg + 3].x);
UNSUPPORTED("183xcjzzdjqiylg3dyj2bm9p0"); // 	C[2].y = C[1].y + (C[0].y - B[3 * seg + 3].y);
UNSUPPORTED("1f9f75c5ajnw72fk4rhidjvk9"); // 	gvrender_polyline(job, C + 1, 2);
UNSUPPORTED("frux7sbbz1c07p68288k7bjt"); // 	C[1] = C[2];
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("10iw0jyewk2q2w6qy33amd83c"); //     case (2 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("2ke5cay4uy9rpguirhm4wa6h"); // 	 *  D[3] +--+ D[2]
UNSUPPORTED("4zc493k26lywwhtoilkgc1l7x"); // 	 *       |  |          B[1]
UNSUPPORTED("2m2wzvksfyug1w1bxq51fnx8t"); // 	 *  B[3] +  +----------+--+ AF[0]=B[0]=D[0]
UNSUPPORTED("5n95z8suw2ujdfytdrngbd1an"); // 	 *       |  B[2]=D[1]     |
UNSUPPORTED("bxixf1yy90einvhfmisdbksm8"); // 	 *  B[4] +                |
UNSUPPORTED("5at9s03omerhkkh28ytchzp8p"); // 	 *       |                |
UNSUPPORTED("9hfhbws20bnfssgim1z95d6c"); // 	 *  B[5] +                |
UNSUPPORTED("at59m33r1w0mw5cp7ympbfal6"); // 	 *       +----------------+
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("5kvv3jvgrdk38xmgwem87tcf9"); // 	/* Add the tab edges. */
UNSUPPORTED("70701yzl3gbwgttd66404h5y0"); // 	D = (pointf*)zmalloc((sides + 2)*sizeof(pointf));
UNSUPPORTED("bf9oppknir8tl1n0mty1c2mou"); // 	D[0] = AF[0];
UNSUPPORTED("c66e72qch46j50w8wi2ex9igr"); // 	D[1] = B[2];
UNSUPPORTED("500mpct3i9wgs3px8hsgnd7m"); // 	D[2].x = B[2].x + (B[3].x - B[4].x) / 3;
UNSUPPORTED("4mbi2yyoztsb8r3tezo2a6bdn"); // 	D[2].y = B[2].y + (B[3].y - B[4].y) / 3;
UNSUPPORTED("ciue6jcuvx8w3rhusaybjn0js"); // 	D[3].x = B[3].x + (B[3].x - B[4].x) / 3;
UNSUPPORTED("ewjqnbklefpknisen36ov0eor"); // 	D[3].y = B[3].y + (B[3].y - B[4].y) / 3;
UNSUPPORTED("97qpn7goedd7t1yn8vv7l2wta"); // 	for (seg = 4; seg < sides + 2; seg++)
UNSUPPORTED("37s6t8j0ylr7pgcyrb42nnzru"); // 	    D[seg] = AF[seg - 2];
UNSUPPORTED("d4ps5t7sfp45tctsjawfhtbnx"); // 	gvrender_polygon(job, D, sides + 2, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("eoq3dhyls1a02czm0kv7mk15"); // 	/* Draw the inner edge. */
UNSUPPORTED("du4dccw5ec6jy7zfkw1ke8w5o"); // 	C[0] = B[3];
UNSUPPORTED("66sh6zqifln1cd8fag6neksct"); // 	C[1] = B[2];
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3ylqzc8858ud3k78im7cnmmcy"); //     case (3 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("8f9cn4lj1t7xhfy5h96toa6z7"); // 	 *            D[2] +----+ D[1]
UNSUPPORTED("3jbilbyrjvqxazi6ya1u20wl"); // 	 *  B[3]=         /      	 *  D[4] +--+----+     +  + AF[0]=B[0]=D[0]
UNSUPPORTED("9ef8paf9orf5t71eo10jr5730"); // 	 *       |  B[2] D[3] B[1]|
UNSUPPORTED("bxixf1yy90einvhfmisdbksm8"); // 	 *  B[4] +                |
UNSUPPORTED("5at9s03omerhkkh28ytchzp8p"); // 	 *       |                |
UNSUPPORTED("9hfhbws20bnfssgim1z95d6c"); // 	 *  B[5] +                |
UNSUPPORTED("at59m33r1w0mw5cp7ympbfal6"); // 	 *       +----------------+
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("1eenvw2caym3k7merjrt3657j"); // 	/* Add the folder edges. */
UNSUPPORTED("exmy2ulb6j7va8q6lm6tnworf"); // 	D = (pointf*)zmalloc((sides + 3)*sizeof(pointf));
UNSUPPORTED("bf9oppknir8tl1n0mty1c2mou"); // 	D[0] = AF[0];
UNSUPPORTED("dtt4p6jk6p6dp18x3u65bzmpw"); // 	D[1].x = AF[0].x - (AF[0].x - B[1].x) / 4;
UNSUPPORTED("1md1kmylraqzyaczi97hwwslr"); // 	D[1].y = AF[0].y + (B[3].y - B[4].y) / 3;
UNSUPPORTED("9yn6qa7smu43hw0zjjaqtdtud"); // 	D[2].x = AF[0].x - 2 * (AF[0].x - B[1].x);
UNSUPPORTED("dmuv0ci8g28j2wpq47bkjo7p6"); // 	D[2].y = D[1].y;
UNSUPPORTED("3w8e9xzd5au6dbo2ceoce7ecd"); // 	D[3].x = AF[0].x - 2.25 * (AF[0].x - B[1].x);
UNSUPPORTED("2z54uncfysm85pzpofpflla2n"); // 	D[3].y = B[3].y;
UNSUPPORTED("bppsqpbzfnhxthbppwlvtxlmq"); // 	D[4].x = B[3].x;
UNSUPPORTED("blpuc391svsq7x2xbrue0e7q9"); // 	D[4].y = B[3].y;
UNSUPPORTED("53hurg1p6mqqqgokwg5t04k5u"); // 	for (seg = 4; seg < sides + 3; seg++)
UNSUPPORTED("1v8cof9jpvwyuadpsdaqvu9f6"); // 	    D[seg] = AF[seg - 3];
UNSUPPORTED("66hq5hkfg6ixw5enzwwhsontq"); // 	gvrender_polygon(job, D, sides + 3, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9zpoxalkfa99z3f69jfqdwd33"); //     case (4 << 24):
UNSUPPORTED("10wchrvt3c3lld3qfda19dzc2"); // 	assert(sides == 4);
UNSUPPORTED("5jncdktfe0besyw426v5gyk97"); // 	/* Adjust for the cutoff edges. */
UNSUPPORTED("70701yzl3gbwgttd66404h5y0"); // 	D = (pointf*)zmalloc((sides + 2)*sizeof(pointf));
UNSUPPORTED("bf9oppknir8tl1n0mty1c2mou"); // 	D[0] = AF[0];
UNSUPPORTED("c66e72qch46j50w8wi2ex9igr"); // 	D[1] = B[2];
UNSUPPORTED("34xwb19muwsw7nh47ifxqeinh"); // 	D[2] = B[4];
UNSUPPORTED("m2dcvmgpwt6s3w1ik1eo8wtz"); // 	D[3] = AF[2];
UNSUPPORTED("87b41ff5btb6uyil4uwshlxm"); // 	D[4] = B[8];
UNSUPPORTED("dus787h2fbafocu5jw9x5tvry"); // 	D[5] = B[10];
UNSUPPORTED("d4ps5t7sfp45tctsjawfhtbnx"); // 	gvrender_polygon(job, D, sides + 2, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("cbntf28jgu8mfc1hfsmxmxd0a"); // 	/* Draw the inner vertices. */
UNSUPPORTED("cue3s8g57y8c0yaa1lf26dj0m"); // 	C[0].x = B[1].x + (B[11].x - B[0].x);
UNSUPPORTED("2cmi2wfte2qh9gijl68bakhsb"); // 	C[0].y = B[1].y + (B[11].y - B[0].y);
UNSUPPORTED("dxiovj41sjesdzgvqycs8n69d"); // 	C[1] = B[4];
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("9pl28lk4wdwe76r2auimzv3uh"); // 	C[1] = B[8];
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("86prrkb6o2eadx8wt21jmkhbv"); // 	C[1] = B[0];
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("5kno8ta0hm2ddpbudyyjtro1m"); //     case (5 << 24):
UNSUPPORTED("10wchrvt3c3lld3qfda19dzc2"); // 	assert(sides == 4);
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("buae5rybq9lw4j80le921um4r"); // 	 *  D[1] +----------------+ D[0]
UNSUPPORTED("5at9s03omerhkkh28ytchzp8p"); // 	 *       |                |
UNSUPPORTED("czyzy0uakecldzx0ct8ivruuq"); // 	 *  3+---+2               |
UNSUPPORTED("2xugwq0paym82tfnux69txz9i"); // 	 *   |                    |
UNSUPPORTED("4ol1bofwhi5div9o31p9wjtj4"); // 	 *  4+---+5               |
UNSUPPORTED("5at9s03omerhkkh28ytchzp8p"); // 	 *       |                |
UNSUPPORTED("5u2sqowe8zkdk3y4bln5j5zhp"); // 	 *  7+---+6               |
UNSUPPORTED("2xugwq0paym82tfnux69txz9i"); // 	 *   |                    |
UNSUPPORTED("4oztb9whw0lnxt2i3c0fve4bg"); // 	 *  8+---+9               |
UNSUPPORTED("5at9s03omerhkkh28ytchzp8p"); // 	 *       |                |
UNSUPPORTED("awyck47phrgtt6emr4jcnw7v8"); // 	 *     10+----------------+ D[11]
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("24ixpw0s6fz9ly7ogkwntlkc5"); // 	D = (pointf*)zmalloc((sides + 8)*sizeof(pointf));
UNSUPPORTED("bf9oppknir8tl1n0mty1c2mou"); // 	D[0] = AF[0];
UNSUPPORTED("65vl8eczvd624e78x152zuae8"); // 	D[1] = AF[1];
UNSUPPORTED("809tuda28xqouetq11u936beb"); // 	D[2].x = B[3].x + (B[4].x - B[3].x);
UNSUPPORTED("cm9zr706ma61pgcr1xawvkeu8"); // 	D[2].y = B[3].y + (B[4].y - B[3].y);
UNSUPPORTED("cwrn6kyomgttqmgbjkzbsfdb6"); // 	D[3].x = D[2].x + (B[3].x - B[2].x);
UNSUPPORTED("2btthvnyzi5zr69hcdxsmzy6b"); // 	D[3].y = D[2].y + (B[3].y - B[2].y);
UNSUPPORTED("j05ovjq3e57jkpjgbgn2a93n"); // 	D[4].x = D[3].x + (B[4].x - B[3].x);
UNSUPPORTED("kp5e1lgz2klpjavxggb5uxtx"); // 	D[4].y = D[3].y + (B[4].y - B[3].y);
UNSUPPORTED("9xz12l85tya02764n9dek9tsj"); // 	D[5].x = D[4].x + (D[2].x - D[3].x);
UNSUPPORTED("3mjb8ks0do270kur1velhlfhg"); // 	D[5].y = D[4].y + (D[2].y - D[3].y);
UNSUPPORTED("amtvzdzubjy4pclq29xzao8nz"); // 	D[9].x = B[6].x + (B[5].x - B[6].x);
UNSUPPORTED("62igzc0qeet9vup11kzlaoi9b"); // 	D[9].y = B[6].y + (B[5].y - B[6].y);
UNSUPPORTED("5ya7ec3f9qo5kmci76122v75u"); // 	D[8].x = D[9].x + (B[6].x - B[7].x);
UNSUPPORTED("eld0yh2bct5ke7mg09giyed8n"); // 	D[8].y = D[9].y + (B[6].y - B[7].y);
UNSUPPORTED("cg32k9flso7918lk33auqxwva"); // 	D[7].x = D[8].x + (B[5].x - B[6].x);
UNSUPPORTED("8dugkdcnkq8sp07se4h7s8cfv"); // 	D[7].y = D[8].y + (B[5].y - B[6].y);
UNSUPPORTED("2e85hrv7fq481gbwdw9hllum1"); // 	D[6].x = D[7].x + (D[9].x - D[8].x);
UNSUPPORTED("7ov0p9nbyd0ax3sgivwszaqv7"); // 	D[6].y = D[7].y + (D[9].y - D[8].y);
UNSUPPORTED("5aoma5n0p035f1aanu4wtjtxs"); // 	D[10] = AF[2];
UNSUPPORTED("evru57wt3y6nkqeeb7pcra08r"); // 	D[11] = AF[3];
UNSUPPORTED("ex1blattp3opv6e82reqp2xg3"); // 	gvrender_polygon(job, D, sides + 8, filled);
UNSUPPORTED("2qqdd1xd8qq7chyfugqx34cfn"); // 	/* Draw the internal vertices. */
UNSUPPORTED("3pw0en5rkazex1cmysfqudqev"); // 	C[0] = D[2];
UNSUPPORTED("ddq28zei7j5vvk0razqli8xd9"); // 	C[1].x = D[2].x - (D[3].x - D[2].x);
UNSUPPORTED("bahi2ks1geofx4oonmrkc71q3"); // 	C[1].y = D[2].y - (D[3].y - D[2].y);
UNSUPPORTED("1gc8dsojnchx8ehnj224y08tj"); // 	C[2].x = C[1].x + (D[4].x - D[3].x);
UNSUPPORTED("8a1l8vtinne40c8bmzgmpco5d"); // 	C[2].y = C[1].y + (D[4].y - D[3].y);
UNSUPPORTED("ah24wzf48tz9bax9stnbxw3jg"); // 	C[3] = D[5];
UNSUPPORTED("4vmc7riub70iswxlzm4mg5xlh"); // 	gvrender_polyline(job, C, 4);
UNSUPPORTED("57xyz6r1s5isw5botqrkasfk1"); // 	C[0] = D[6];
UNSUPPORTED("9e9zf6fuxc8ovfj3jsryqlylv"); // 	C[1].x = D[6].x - (D[7].x - D[6].x);
UNSUPPORTED("bsnau3s4c5hnmmj1shw8ouokg"); // 	C[1].y = D[6].y - (D[7].y - D[6].y);
UNSUPPORTED("ewm5xz38hsb9hh3wxf6j214dp"); // 	C[2].x = C[1].x + (D[8].x - D[7].x);
UNSUPPORTED("bv41aedgjg4w0zc4ccxrl657z"); // 	C[2].y = C[1].y + (D[8].y - D[7].y);
UNSUPPORTED("77p9f8uavqa0bo2uzwrgul8fo"); // 	C[3] = D[9];
UNSUPPORTED("4vmc7riub70iswxlzm4mg5xlh"); // 	gvrender_polyline(job, C, 4);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("beokd0nvtuwlde2ahiaxzmwo0"); //     case (6 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("ixqwyi3z0ndmfrpmbx8hb747"); // 	 * L-shaped arrow on a center line, scales in the x direction
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("4bfue86t6gftlcgwzff6qshk7"); // 	 *  
UNSUPPORTED("8udetwc0o6gakybvc61qq6kjd"); // 	 *      D[1]	          |	 *       +----------------+ 	 *       |	        D[0] 	 *       |                    	 *       |                    /    
UNSUPPORTED("cp87959b34no4267o38ifrckk"); // 	 *       |             D[5]  /
UNSUPPORTED("722lri4xki5u0btwl5flr6qxu"); // 	 *       |        +-------+ /
UNSUPPORTED("8kn1gxbxamlotuexrxky3o76q"); // 	 *       |	  |       |/
UNSUPPORTED("dscqqxbpov7i3z8kvqhzn45ew"); // 	 *	 +--------+
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("5kvv3jvgrdk38xmgwem87tcf9"); // 	/* Add the tab edges. */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("9f16ni612qewna0ipjhg14bai"); // 	//the arrow's thickness is (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("8y1urr2asyrlu62q42pgcsmhp"); // 	D = (pointf*)zmalloc((sides + 5)*sizeof(pointf));
UNSUPPORTED("b70z64iz7ervr0w25bypaar6w"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (AF[0].x - AF[1].x)/8; //x_center + width
UNSUPPORTED("cflv5tiys03frg7ni6wrlnf54"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)*3/2; //D[4].y + width
UNSUPPORTED("8mk9mjo89wwb2k197bo1oci28"); // 	D[1].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (AF[0].x - AF[1].x)/4; //x_center - 2*width
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("301q7fj403dfv6ubg9vwg4ise"); // 	D[2].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("3zldfbr9i1rzlmv4z7y0pfsui"); // 	D[3].x = D[2].x + (B[2].x - B[3].x)/2; //D[2].x + width
UNSUPPORTED("1c9pkdgt2yqgbrfhtob6o1anm"); // 	D[3].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("dm1gyw5qetggvcc8zstksyyg2"); // 	D[4].x = D[3].x;
UNSUPPORTED("5om1l6r4v0k3wqbmirwpiyqba"); // 	D[4].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y); //highest cds point 
UNSUPPORTED("5452plq9tg8pl7nqgqe17wfok"); // 	D[5].x = D[0].x;
UNSUPPORTED("4nf8s53n53wddx4yf7audzxu1"); // 	D[5].y = D[4].y; //highest cds point
UNSUPPORTED("a6zbxb7jxf9qdy8dvdmo9bql6"); // 	D[6].x = D[0].x;
UNSUPPORTED("5cl1c2plgkvkq7g0rvwvdg7vt"); // 	D[6].y = D[4].y - (B[3].y-B[4].y)/4; //D[4].y - width/2 
UNSUPPORTED("gi12lyfrur4z2lxxndbbbifq"); // 	D[7].x = D[6].x + (B[2].x - B[3].x); //D[6].x + 2*width
UNSUPPORTED("5pkfhqy0ycejkkhrrm5hggks4"); // 	D[7].y = D[6].y + (B[3].y - B[4].y)/2; //D[6].y + width
UNSUPPORTED("cvcvatp3h1huxwxo27xm89hir"); // 	D[8].x = D[0].x;
UNSUPPORTED("61oo13fi0rf1r3bzy45p7mzr"); // 	D[8].y = D[0].y + (B[3].y - B[4].y)/4;//D[0].y + width/2
UNSUPPORTED("9v8e5z6hrq6fr5p16pg80kc21"); // 	gvrender_polygon(job, D, sides + 5, filled);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("e5j165s4lbb8e75y1ejspv7up"); // 	free(D);			
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3pfp7u39hqhl9b4pzx94kdmtp"); //     case (7 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("8colhcg9wmqo1rtyzhmaa4ek6"); // 	 * arrow without the protrusions, scales normally
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("4bfue86t6gftlcgwzff6qshk7"); // 	 *  
UNSUPPORTED("d9imuj2p14rbbmjs7wvofspna"); // 	 *      D[1] = AF[1]      
UNSUPPORTED("4z7qsayd2i6g803t3ab2q4cnr"); // 	 *       +----------------+	 *       |		D[0]	 *       |                   	 *       |                   /    
UNSUPPORTED("csz54pl4jc7c08mjs4ll2ud72"); // 	 *       |                  /
UNSUPPORTED("c0psno66sh97gcrf8bvy26xm7"); // 	 *       +----------------+/
UNSUPPORTED("8zy5cfbfoig4ao910eycpxx1x"); // 	 *	  	          D[3]
UNSUPPORTED("79f8fxsn2gta6r8d9x9n38rzy"); // 	 *	 
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("b5f90htb83l32kamg97jm1sux"); // 	D = (pointf*)zmalloc((sides + 1)*sizeof(pointf));
UNSUPPORTED("3iplasxynfhfozy7oqwqkceet"); // 	D[0].x = B[1].x;
UNSUPPORTED("d1eg8u95ffvpn8cj0t175qe8"); // 	D[0].y = B[1].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("45vton2uqw835epqg2o0j1qee"); // 	D[1].x = B[3].x;
UNSUPPORTED("2so1ozst5cwd02o33408hxi1n"); // 	D[1].y = B[3].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("2y7ipfj9drc5mfgjluf5y6k6p"); // 	D[2].x = AF[2].x;
UNSUPPORTED("bopqgv9t3o6ppbojdunc5qjfl"); // 	D[2].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("8xoigox3do4ruwo7d6ew3o7af"); // 	D[3].x = B[1].x;
UNSUPPORTED("94cxjx74gts01u98ngcns0j0o"); // 	D[3].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("dv68lnnw5js80t8w4vzw3er4z"); // 	D[4].y = AF[0].y - (AF[0].y - AF[3].y)/2;
UNSUPPORTED("b5nri6sa2qx33fqvp8xfgckb0"); // 	D[4].x = AF[0].x;
UNSUPPORTED("6u1z6h8gfy4ofbsa9gidwp2v9"); // 	gvrender_polygon(job, D, sides + 1, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3thdjaa2jp4igqqd3un2gwo6r"); //     case (8 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("q99ojp3xn0cfe2yzxdzb5y6l"); // 	* T-shape, does not scale, always in the center
UNSUPPORTED("11dzfjbtxkn4alvdn9s9waab8"); // 	*
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("6z84agdr458on26oqsta195b2"); // 	*      D[4]      
UNSUPPORTED("1w6ol3zmeqn5mywt6a4z6qo41"); // 	*       +----------------+
UNSUPPORTED("9onfqvue4pbpmccmodd1mtx7g"); // 	*       |		D[3]
UNSUPPORTED("c45nqrjd35z7gq6ei63b1c7yg"); // 	*       |                |
UNSUPPORTED("4de8jijppluyewmllch44bu23"); // 	*       |                |    
UNSUPPORTED("a3ks7z4s5ja7lvrmq3silkmjl"); // 	*       |  D[6]    D[1]  |
UNSUPPORTED("31k91o6maa0q4udou617gn5p5"); // 	*   D[5]+---+       +----+ D[2]
UNSUPPORTED("dkhq92412a9i5utfrnc2dhnz"); // 	*	    |	    |     
UNSUPPORTED("4cml58ti5mwtjgtzmwgiw6900"); // 	*	    +-------+ D[0]
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("7uqzacf8x5tg89bgh6rziw1s3"); // 	D = (pointf*)zmalloc((sides + 4)*sizeof(pointf));
UNSUPPORTED("5810col2r6hfkxov4ipmdth4n"); // 	D[0].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)/4; //x_center + width/2
UNSUPPORTED("db5a23r3dbv1lvqcjuh4j9hwi"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("53lzmbxoyk9sss54apowy1jcj"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("3lc8br5sy3j31bj2kq0vy78xd"); // 	D[2].x = D[1].x + (B[2].x-B[3].x)/2;
UNSUPPORTED("dmuv0ci8g28j2wpq47bkjo7p6"); // 	D[2].y = D[1].y;
UNSUPPORTED("5ihipbue45drjma9vxlpgy615"); // 	D[3].x = D[2].x;
UNSUPPORTED("21mj1uiiddzjovkjk6zsb88pe"); // 	D[3].y = D[2].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("6xkr5j9ysazz1yh6zkpfshldm"); // 	D[4].x = AF[1].x + (AF[0].x-AF[1].x)/2 - (B[2].x-B[3].x)*3/4; //D[3].y mirrowed across the center
UNSUPPORTED("40nf0bg6glqtx5viwakfdv7dw"); // 	D[4].y = D[3].y;
UNSUPPORTED("4ihzpkh7w4ranwnvy0w5gcgnf"); // 	D[5].x = D[4].x;
UNSUPPORTED("d78lk5o8y51l9z7neypp202ok"); // 	D[5].y = D[2].y;
UNSUPPORTED("bu70m5u8xmzjlg97y4dzx2qvn"); // 	D[6].x = AF[1].x + (AF[0].x-AF[1].x)/2 - (B[2].x-B[3].x)/4; //D[1].x mirrowed across the center
UNSUPPORTED("8y0iwlff2lpkouqnvi8duicdw"); // 	D[6].y = D[1].y;
UNSUPPORTED("1dhue1c5y4ho0ct8ks14zgh2q"); // 	D[7].x = D[6].x;
UNSUPPORTED("bmnuosohj6kpaougdrcg1l2ut"); // 	D[7].y = D[0].y;
UNSUPPORTED("519fp4pixwl2w7d4ssm51lram"); // 	gvrender_polygon(job, D, sides + 4, filled);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3s9ef7jzymofqi316ojrcjxzd"); //     case (9 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("dhbze4ofczxyy7yamogd6ti3a"); // 	 * half-octagon with line, does not scale, always in center
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("2q9d6wex3idlomfcu4pkk398x"); // 	 *  D[3]
UNSUPPORTED("4xh336uvfc358ms133bm0s52b"); // 	 *     _____  D[2] 
UNSUPPORTED("bfk0n04f7r4qzjubnfyu2xh1k"); // 	 *    /     	 *   /       \ D[1]
UNSUPPORTED("5psrhywjfkorktkmgorilauv5"); // 	 *   |       |
UNSUPPORTED("3lg9sq0fw542uhjrpospx6jy2"); // 	 *   -----------
UNSUPPORTED("3hbh5rz6pydqncnacff0j6bck"); // 	 *              D[0]   
UNSUPPORTED("dfamfnhl22etjpal7hadbncq"); // 	 *      
UNSUPPORTED("18xb6dplfcolp2jrg1kn8kys7"); // 	 *	
UNSUPPORTED("6b0g1rzk4g1o9c2we59or2tun"); // 	 *	          
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("70701yzl3gbwgttd66404h5y0"); // 	D = (pointf*)zmalloc((sides + 2)*sizeof(pointf));
UNSUPPORTED("7m6kkbjz3yjj1zf1qc45ktcv7"); // 	D[0].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)*3/4; //x_center+width	
UNSUPPORTED("db5a23r3dbv1lvqcjuh4j9hwi"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("1r3hx6z8u8r8abljwwm9aeskf"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/4; //D[0].y+width/2
UNSUPPORTED("4fcwgyabif5z28bihmxf7x4tb"); // 	D[2].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)/4; //x_center+width/2
UNSUPPORTED("54amw68beuf1he27cp7569llg"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2; //D[1].y+width
UNSUPPORTED("54ovwnsyt9kbwtqf47f7c2ch3"); // 	D[3].x = AF[1].x + (AF[0].x-AF[1].x)/2 - (B[2].x-B[3].x)/4; //D[2].x mirrowed across the center
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("6rpcfql0js4c3hg279dfw3na0"); // 	D[4].x = AF[1].x + (AF[0].x-AF[1].x)/2 - (B[2].x-B[3].x)*3/4;
UNSUPPORTED("2q1cxgb3rckor7bxppmobumh0"); // 	D[4].y = D[1].y;
UNSUPPORTED("4ihzpkh7w4ranwnvy0w5gcgnf"); // 	D[5].x = D[4].x;
UNSUPPORTED("bgu3mbmk36rgraaak3oxce0r8"); // 	D[5].y = D[0].y;
UNSUPPORTED("d4ps5t7sfp45tctsjawfhtbnx"); // 	gvrender_polygon(job, D, sides + 2, filled);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("api2h9s3b85sv9rd97y8rgtud"); //     case (10 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("28rdr11lzywp8e0rmvk2kob5x"); // 	* half arrow shape, scales in the x-direction
UNSUPPORTED("qqfabopc9ur04mms6juzgp3x"); // 	*                 D[1]
UNSUPPORTED("e46ybyktily44t0b7fyholuq2"); // 	*		    |	*		    | 	*		    |  	*	------------    	*	|		 	*	------------------\ D[0]			 
UNSUPPORTED("c1eovxh5oerfbxr449dk7o87u"); // 	*				
UNSUPPORTED("30zs4hbrokoqtcgihv0hj9b3o"); // 	*   --------------------------------
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("b5f90htb83l32kamg97jm1sux"); // 	D = (pointf*)zmalloc((sides + 1)*sizeof(pointf));
UNSUPPORTED("7f72fcloe1t7qscdonrjz8qii"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (B[2].x-B[3].x);//x_center + width*2
UNSUPPORTED("aojkb4cjtphbwmuxkdt5y13al"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/4;//y_center + 1/2 width
UNSUPPORTED("6esajn79a3aj38f6eb3xf9wfj"); // 	D[1].x = D[0].x - (B[2].x-B[3].x); //x_center
UNSUPPORTED("9t3k5f7xx5vksm96d80h9fxwx"); // 	D[1].y = D[0].y + (B[3].y-B[4].y);
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("1ewf0yhdjldw3pxdsfgzwkuuq"); // 	D[2].y = D[0].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("ch7qqmpmawmffg66esm43wmvl"); // 	D[3].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (AF[0].x - AF[1].x)/4;//x_center - 2*(scalable width)
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("dm1gyw5qetggvcc8zstksyyg2"); // 	D[4].x = D[3].x;
UNSUPPORTED("1fs3e43f2oljbqgl2gtjqo4xn"); // 	D[4].y = D[0].y;
UNSUPPORTED("6u1z6h8gfy4ofbsa9gidwp2v9"); // 	gvrender_polygon(job, D, sides + 1, filled);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d3qtw2cyrswp62e4psl3ww6qy"); //     case (11 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("asi2pc1274ap2839mbipmof4"); // 	* zigzag shape, scales in the x-direction (only the middle section)
UNSUPPORTED("11dzfjbtxkn4alvdn9s9waab8"); // 	*
UNSUPPORTED("6ng5df4tnn838bpcedbdkwgik"); // 	*		 
UNSUPPORTED("ebmsq3svyy5vfr02vuesdb7om"); // 	*   ----D[2]	 
UNSUPPORTED("474tiy3ppnarbx3tem6pex5wl"); // 	*   |   |________ D[0]
UNSUPPORTED("br8aj42mw70mad09j8u79ty50"); // 	*   |            |____
UNSUPPORTED("435gbgj841wxffxgc43eficn0"); // 	*   ----------	 |
UNSUPPORTED("72xs5i64zgn8illkas18bwl77"); // 	*   D[4]      --- D[7]
UNSUPPORTED("c1eovxh5oerfbxr449dk7o87u"); // 	*				
UNSUPPORTED("7ltzne5zulu744fj39ucj4ohf"); // 	*   
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("7uqzacf8x5tg89bgh6rziw1s3"); // 	D = (pointf*)zmalloc((sides + 4)*sizeof(pointf));
UNSUPPORTED("8puozbi1qbdhenklno39sad43"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (AF[0].x - AF[1].x)/8 + (B[2].x-B[3].x)/2;//x_center + scalable_width + width
UNSUPPORTED("aojkb4cjtphbwmuxkdt5y13al"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/4;//y_center + 1/2 width
UNSUPPORTED("d4aju7h8k010npgrrxffdkebj"); // 	D[1].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (AF[0].x - AF[1].x)/8; //x_center - width
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("1gxksvxg269nccy3ki80smka3"); // 	D[3].x = D[2].x - (B[2].x-B[3].x)/2; //D[2].x - width
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("dm1gyw5qetggvcc8zstksyyg2"); // 	D[4].x = D[3].x;
UNSUPPORTED("7lfc7py2ka8mdt2ptx9nm6vrb"); // 	D[4].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)/4; //y_center - 1/2(width)
UNSUPPORTED("2jcyrcu5t9kbekchzlvw8g996"); // 	D[5].x = D[0].x - (B[2].x-B[3].x)/2;
UNSUPPORTED("9h546osr54mx5910v00kmvmo3"); // 	D[5].y = D[4].y;
UNSUPPORTED("a2qj96305wxo9gce1tujcuecz"); // 	D[6].x = D[5].x;
UNSUPPORTED("4orb58bnb2ascibwd7xd0y4hf"); // 	D[6].y = D[5].y - (B[3].y-B[4].y)/2;
UNSUPPORTED("5hppmiblaazms70xhasaspb22"); // 	D[7].x = D[0].x;
UNSUPPORTED("afqmp4euhvvfkjpszw9u76l4r"); // 	D[7].y = D[6].y;
UNSUPPORTED("519fp4pixwl2w7d4ssm51lram"); // 	gvrender_polygon(job, D, sides + 4, filled);
UNSUPPORTED("2r3im7kh170rpplkwcoi8l6jp"); // 	/*dsDNA line left half*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("7eokez2yp2825neik2f0yb69s"); // 	C[1].x = D[4].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("eum17d4kc12w09ka3gn4xnjkl"); // 	/*dsDNA line right half*/
UNSUPPORTED("3mga7r2ky1w3m12hajx82ljg6"); // 	C[0].x = D[7].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("bpepkqrqcoslkcywz6fwjoemb"); // 	gvrender_polyline(job, C, 2);			    
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("303w43enaucmyhixkc1xhs652"); // 	break;	
UNSUPPORTED("8xtw70fmxvc3slz5c6ct4iqq"); //     case (12 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("bynfyyael4om2fd0roolnhg11"); // 	*  does not scale, on the left side
UNSUPPORTED("11dzfjbtxkn4alvdn9s9waab8"); // 	*
UNSUPPORTED("9jluf518ja9nhvnco2qeyyukh"); // 	*  D[3]------D[2]	 
UNSUPPORTED("796ehiftmwg66mguye5i6gdl9"); // 	*  |          |
UNSUPPORTED("anbjdqkop5jdxo0pqszbmsil0"); // 	*  D[0]------D[1]
UNSUPPORTED("3b7cclindb323xc5dyldebt79"); // 	*        -----  ------------
UNSUPPORTED("ekoc25w3kdsjlqotz5zserik"); // 	*        |    |
UNSUPPORTED("d246t2abb3qcdptpl7t388cet"); // 	*       D[0]--D[1]
UNSUPPORTED("c1eovxh5oerfbxr449dk7o87u"); // 	*				
UNSUPPORTED("7ltzne5zulu744fj39ucj4ohf"); // 	*   
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("o7xplnda870crsqrrtxoeovw"); // 	D[0].x = AF[1].x;//the very left edge
UNSUPPORTED("97g2igromxtixmscljv7c9wu6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/8;//y_center + 1/4 width
UNSUPPORTED("5vjhczya0wmraeexdugfakfz8"); // 	D[1].x = D[0].x + 2*(B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("ekpqju1yk89s6h95eac20r1gq"); // 	/*second, lower shape*/
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("bbttbv2k8ll2jqajk92f8cjw5"); // 	D[0].x = AF[1].x + (B[2].x-B[3].x);
UNSUPPORTED("8t3rx5v8pl369i9s1bav2htbd"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)*5/8; //y_center - 5/4 width
UNSUPPORTED("9pkvrpz0i5eo220nag6ovrbfn"); // 	D[1].x = D[0].x + (B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("eum17d4kc12w09ka3gn4xnjkl"); // 	/*dsDNA line right half*/
UNSUPPORTED("8uxhxokvb62d8vqgpxrqqprm7"); // 	C[0].x = D[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("bpepkqrqcoslkcywz6fwjoemb"); // 	gvrender_polyline(job, C, 2);			    
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("303w43enaucmyhixkc1xhs652"); // 	break;	
UNSUPPORTED("5jt28oeq5aqdeuwsfmezn51lt"); //     case (13 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("b68kolhfp5nwayr9aru8m8e5c"); // 	*  does not scale, on the right side
UNSUPPORTED("11dzfjbtxkn4alvdn9s9waab8"); // 	*
UNSUPPORTED("b3es50hturnmkxnq7e5ol9ain"); // 	*	   D[2]------D[1]	 
UNSUPPORTED("f3ui2tnmq3tyd5y0zaxgalibr"); // 	*	   |          |
UNSUPPORTED("8da12absy3kplnjgp306ow2r1"); // 	*----------D[3]------D[0]
UNSUPPORTED("8al119ctyx2l5odyoc6w4up0r"); // 	*	   -----  D[1]
UNSUPPORTED("ec20mvh529jwauxvcvvcv9hjb"); // 	*          |    |
UNSUPPORTED("d0q7vfsuc2e3fts98jrfkk3on"); // 	*          D[3]--D[0]
UNSUPPORTED("c1eovxh5oerfbxr449dk7o87u"); // 	*				
UNSUPPORTED("7ltzne5zulu744fj39ucj4ohf"); // 	*   
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("7a5jojmk4vack2tnjyo1dnv5c"); // 	D[0].x = AF[0].x;//the very right edge
UNSUPPORTED("97g2igromxtixmscljv7c9wu6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/8;//y_center + 1/4 width
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("53lzmbxoyk9sss54apowy1jcj"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("5ggtybq4huk2l27co2brcnn7a"); // 	D[2].x = D[1].x - 2*(B[3].y-B[4].y);
UNSUPPORTED("dmuv0ci8g28j2wpq47bkjo7p6"); // 	D[2].y = D[1].y;
UNSUPPORTED("5ihipbue45drjma9vxlpgy615"); // 	D[3].x = D[2].x;
UNSUPPORTED("b9duody7fe2aysvdbb7u2vz0h"); // 	D[3].y = D[0].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("ekpqju1yk89s6h95eac20r1gq"); // 	/*second, lower shape*/
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("1koli020v5df0l9ozg6qbxov9"); // 	D[0].x = AF[0].x - (B[2].x-B[3].x);
UNSUPPORTED("8t3rx5v8pl369i9s1bav2htbd"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)*5/8; //y_center - 5/4 width
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("53lzmbxoyk9sss54apowy1jcj"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("1tvqu076kbr7qcxd1pporcrlw"); // 	D[2].x = D[1].x - (B[3].y-B[4].y);
UNSUPPORTED("dmuv0ci8g28j2wpq47bkjo7p6"); // 	D[2].y = D[1].y;
UNSUPPORTED("5ihipbue45drjma9vxlpgy615"); // 	D[3].x = D[2].x;
UNSUPPORTED("b9duody7fe2aysvdbb7u2vz0h"); // 	D[3].y = D[0].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("2r3im7kh170rpplkwcoi8l6jp"); // 	/*dsDNA line left half*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("ogtpwtelm2m435skybbigshn"); // 	C[1].x = D[3].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("bpepkqrqcoslkcywz6fwjoemb"); // 	gvrender_polyline(job, C, 2);			    
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("303w43enaucmyhixkc1xhs652"); // 	break;	
UNSUPPORTED("b00adjh71tebnwktqy2c7qen"); //     case (14 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("167ktpr6jgq2ir87knm58dnxw"); // 	*  does not scale
UNSUPPORTED("11dzfjbtxkn4alvdn9s9waab8"); // 	*
UNSUPPORTED("3llrhwcbgncbf9dwdut8nbop8"); // 	*     D[3]------D[2]   D[3]------D[2]    
UNSUPPORTED("3bkrw2bi9jt34caj90fh17xk"); // 	*     |          |      |          |
UNSUPPORTED("e8rezojipvcbc7wl7lkb5gtwm"); // 	*  ---D[0]------D[1]   D[0]------D[1]----
UNSUPPORTED("3llrhwcbgncbf9dwdut8nbop8"); // 	*     D[3]------D[2]   D[3]------D[2]    
UNSUPPORTED("dsr5jaoof7rw4c3tkgfx86yor"); // 	*     |          |	|          |
UNSUPPORTED("73xc7e0yi8z88q6x4x9s8f50a"); // 	*     D[0]------D[1]   D[0]------D[1]
UNSUPPORTED("53z8rhlvldkx4ydcoaqfnm4tk"); // 	*        
UNSUPPORTED("c1eovxh5oerfbxr449dk7o87u"); // 	*				
UNSUPPORTED("7ltzne5zulu744fj39ucj4ohf"); // 	*   
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("caiifq15qssrojo1lp5kd761t"); // 	/*upper left rectangle*/
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("f2j5si3mjcrfchan7ja10w14r"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x)*9/8; //x_center - 2*width - 1/4*width
UNSUPPORTED("97g2igromxtixmscljv7c9wu6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/8;//y_center + 1/4 width
UNSUPPORTED("9pkvrpz0i5eo220nag6ovrbfn"); // 	D[1].x = D[0].x + (B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("c8aakjie0dovvmj2y1cv9pqvi"); // 	/*lower, left rectangle*/
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("f2j5si3mjcrfchan7ja10w14r"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x)*9/8; //x_center - 2*width - 1/4*width
UNSUPPORTED("12qbz2yet5sxyqb1w92s4ndn6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)*5/8;//y_center - width - 1/4 width
UNSUPPORTED("9pkvrpz0i5eo220nag6ovrbfn"); // 	D[1].x = D[0].x + (B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("5zdu7hjxl21oye4glrh3qki87"); // 	/*lower, right rectangle*/
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("jnnrld5fecpgw2spcpy1owlt"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (B[2].x-B[3].x)/8; //x_center + 1/4*width
UNSUPPORTED("12qbz2yet5sxyqb1w92s4ndn6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)*5/8;//y_center - width - 1/4 width
UNSUPPORTED("9pkvrpz0i5eo220nag6ovrbfn"); // 	D[1].x = D[0].x + (B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("d6ndnv9bfbxsy039deiideukm"); // 	/*upper, right rectangle*/
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("jnnrld5fecpgw2spcpy1owlt"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (B[2].x-B[3].x)/8; //x_center + 1/4*width
UNSUPPORTED("8u02sf2xpgku2gvq8y3cyvv3"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/8;//y_center - width - 1/4 width
UNSUPPORTED("9pkvrpz0i5eo220nag6ovrbfn"); // 	D[1].x = D[0].x + (B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("eum17d4kc12w09ka3gn4xnjkl"); // 	/*dsDNA line right half*/
UNSUPPORTED("8uxhxokvb62d8vqgpxrqqprm7"); // 	C[0].x = D[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("2r3im7kh170rpplkwcoi8l6jp"); // 	/*dsDNA line left half*/
UNSUPPORTED("5slbe4qz5t6sovatfjm8ccmcs"); // 	C[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x)*9/8; //D[0].x of of the left rectangles
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("der143k1nuqx7trw59k0efgrl"); // 	C[1].x = AF[1].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("bpepkqrqcoslkcywz6fwjoemb"); // 	gvrender_polyline(job, C, 2);			    
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("303w43enaucmyhixkc1xhs652"); // 	break;	
UNSUPPORTED("14ei614r723mcxpmqhkpr8ijk"); //     case (15 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("167ktpr6jgq2ir87knm58dnxw"); // 	*  does not scale
UNSUPPORTED("11dzfjbtxkn4alvdn9s9waab8"); // 	*
UNSUPPORTED("4j349xa0inyf0oh36zva5zr7l"); // 	*      D[3]----------D[2]	 
UNSUPPORTED("7b4dzyad9f7brkvbovf9r6hqw"); // 	*      |               |
UNSUPPORTED("79i3zed3ho3ky8mkm7skk5hwz"); // 	*     D[0]----------D[1]
UNSUPPORTED("alv06nc1uxctts8p1qmc51lbk"); // 	* ----                  ---------
UNSUPPORTED("4j349xa0inyf0oh36zva5zr7l"); // 	*      D[3]----------D[2]	 
UNSUPPORTED("7b4dzyad9f7brkvbovf9r6hqw"); // 	*      |               |
UNSUPPORTED("79i3zed3ho3ky8mkm7skk5hwz"); // 	*     D[0]----------D[1]
UNSUPPORTED("dhhao73bupb3qih7po05x8qcs"); // 	*  
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("awwgll9ed6viac317lvwpn71d"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x); //x_center - 2*width
UNSUPPORTED("97g2igromxtixmscljv7c9wu6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/8;//y_center + 1/4 width
UNSUPPORTED("5vjhczya0wmraeexdugfakfz8"); // 	D[1].x = D[0].x + 2*(B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("ekpqju1yk89s6h95eac20r1gq"); // 	/*second, lower shape*/
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("awwgll9ed6viac317lvwpn71d"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x); //x_center - 2*width
UNSUPPORTED("12qbz2yet5sxyqb1w92s4ndn6"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)*5/8;//y_center - width - 1/4 width
UNSUPPORTED("5vjhczya0wmraeexdugfakfz8"); // 	D[1].x = D[0].x + 2*(B[2].x-B[3].x);
UNSUPPORTED("377dvf6krrmq87z68ekoyk1nr"); // 	D[1].y = D[0].y;
UNSUPPORTED("cnpipkg905bpnnnnoa0lopvzy"); // 	D[2].x = D[1].x;
UNSUPPORTED("crefk31pbp2yilanj44au7qbe"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("esx26fefr0mrz7t4p720sf58a"); // 	D[3].y = D[2].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("eum17d4kc12w09ka3gn4xnjkl"); // 	/*dsDNA line right half*/
UNSUPPORTED("8uxhxokvb62d8vqgpxrqqprm7"); // 	C[0].x = D[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("2r3im7kh170rpplkwcoi8l6jp"); // 	/*dsDNA line left half*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("dvcsbjrm41adw6focml1eh8zc"); // 	C[1].x = D[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("303w43enaucmyhixkc1xhs652"); // 	break;	
UNSUPPORTED("48w5mwwikj3xri88x1ws7mspe"); //     case (16 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("7ltzne5zulu744fj39ucj4ohf"); // 	*   
UNSUPPORTED("79o67pgmuajgw0oy68fg9bbvd"); // 	* 
UNSUPPORTED("h0rc5xn5b50bnx2xer2z5stp"); // 	*   +--------------+
UNSUPPORTED("ex2ghpokf7935zc3r3j6788pu"); // 	*   |		   |
UNSUPPORTED("25narjhny8esxdam3pqsc62n1"); // 	*   |x		   |
UNSUPPORTED("h0cdk6heuz85qgmbro6tzuqz"); // 	*   |_____________ |
UNSUPPORTED("h0rc5xn5b50bnx2xer2z5stp"); // 	*   +--------------+
UNSUPPORTED("4gn10s3n3ms2z2jz5zpxmp5ay"); // 	*/
UNSUPPORTED("31z12vlharu09ldqvmirdvp2n"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("azjvg240odj44iv9ekoxye94d"); // 	//the thickness is subituted with (AF[0].x - AF[1].x)/8 to make it scalable in the y with label length
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("8bi8jgvnnbgjoe0rtiovk3kka"); // 	D[0].x = AF[0].x;
UNSUPPORTED("d1eg8u95ffvpn8cj0t175qe8"); // 	D[0].y = B[1].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("45vton2uqw835epqg2o0j1qee"); // 	D[1].x = B[3].x;
UNSUPPORTED("2so1ozst5cwd02o33408hxi1n"); // 	D[1].y = B[3].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("2y7ipfj9drc5mfgjluf5y6k6p"); // 	D[2].x = AF[2].x;
UNSUPPORTED("bopqgv9t3o6ppbojdunc5qjfl"); // 	D[2].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("7h94vcqfqaq3o7bcei0wollyt"); // 	D[3].x = AF[0].x;
UNSUPPORTED("94cxjx74gts01u98ngcns0j0o"); // 	D[3].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("etbsuecrxy2aywu5r3ylnasws"); // 	/* "\" of the X*/
UNSUPPORTED("dn2kymbyi2rzk23hu0fvmtheo"); // 	C[0].x = AF[1].x + (B[2].x-B[3].x)/4;
UNSUPPORTED("19k6gzn1ryigjdj1df6gv5pif"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/8; //y_center + 1/4 width
UNSUPPORTED("a0w8w98rrt5pm4y0l5qdfxkow"); // 	C[1].x = C[0].x + (B[2].x-B[3].x)/4;//C[0].x + width/2
UNSUPPORTED("d2o4uwqoui3s0bmux80abbqmd"); // 	C[1].y = C[0].y - (B[3].y-B[4].y)/4;//C[0].y - width/2
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("4ysg2usfft6m36ffns83iwtmd"); // 	/*"/" of the X*/
UNSUPPORTED("dn2kymbyi2rzk23hu0fvmtheo"); // 	C[0].x = AF[1].x + (B[2].x-B[3].x)/4;
UNSUPPORTED("6pzw020qiqzzlzrh4wycp229t"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[3].y-B[4].y)/8; //y_center - 1/4 width
UNSUPPORTED("a0w8w98rrt5pm4y0l5qdfxkow"); // 	C[1].x = C[0].x + (B[2].x-B[3].x)/4;//C[0].x + width/2
UNSUPPORTED("66ksrj9hxsvy0vw9c9ocnihdo"); // 	C[1].y = C[0].y + (B[3].y-B[4].y)/4;//C[0].y + width/2
UNSUPPORTED("3fkc6rtflns56swh4at4mk3m5"); // 	gvrender_polyline(job, C, 2);	
UNSUPPORTED("ak4ihrj8sm9s5bbs7ifbiiqgz"); // 	/*bottom line*/
UNSUPPORTED("dn2kymbyi2rzk23hu0fvmtheo"); // 	C[0].x = AF[1].x + (B[2].x-B[3].x)/4;
UNSUPPORTED("4u65ge9q81w5f8vq91hnk25j0"); // 	C[0].y = AF[2].y + (B[3].y-B[4].y)*3/4;
UNSUPPORTED("3hxysqi93h8rxowvw7regc5gd"); // 	C[1].x = AF[0].x - (B[2].x-B[3].x)/4;
UNSUPPORTED("142esq47csev8pt9cyj31tqm1"); // 	C[1].y = C[0].y;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("303w43enaucmyhixkc1xhs652"); // 	break;	
UNSUPPORTED("7vbkukru4dk2jdofrvin4riun"); //     case (17 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("cn7c2ju32wqc45avu1irffys"); // 	 * double square
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("athofuzkxb9foobkjvbgruits"); // 	 *  +-----+
UNSUPPORTED("btl3kzf1m40fjx35r7s13dak8"); // 	 *--| ___ |---
UNSUPPORTED("3bewsg8hd510shli6vxd282t3"); // 	 *  | |_| |
UNSUPPORTED("athofuzkxb9foobkjvbgruits"); // 	 *  +-----+
UNSUPPORTED("6b0g1rzk4g1o9c2we59or2tun"); // 	 *	          
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("dk2ovejnwrhnobz5s8tu0j10r"); // 	D = (pointf*)zmalloc((sides)*sizeof(pointf));
UNSUPPORTED("cwgeibey8unr1moojtfgv2q7x"); // 	D[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (B[2].x-B[3].x)/2; //x_center+width	
UNSUPPORTED("5q4z1vahwevjgs3lngw7t3ejs"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[2].x-B[3].x)/2; //y_center
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("hdzi6ghb3chmpb4kat6cn5o5"); // 	D[1].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[2].x-B[3].x)/2; //D[0].y- width
UNSUPPORTED("c8u2dyot9xat0mx55arvw7g0y"); // 	D[2].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x)/2; //x_center-width
UNSUPPORTED("dmuv0ci8g28j2wpq47bkjo7p6"); // 	D[2].y = D[1].y;
UNSUPPORTED("5ihipbue45drjma9vxlpgy615"); // 	D[3].x = D[2].x;
UNSUPPORTED("b9duody7fe2aysvdbb7u2vz0h"); // 	D[3].y = D[0].y;
UNSUPPORTED("d68sdlvrto6k9daq3v4bffw5y"); // 	gvrender_polygon(job, D, sides, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("alinyx8nih1q4xkxk0zr6ks0g"); // 	/*outer square line*/
UNSUPPORTED("esoqwn3fruttfk0xaif261aqh"); // 	C[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (B[2].x-B[3].x)*3/4; //x_center+1.5*width	
UNSUPPORTED("bne0arbunp5lxwkzz28ssqsek"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[2].x-B[3].x)*3/4; //y_center
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("9up5g41z0f2g93bts1dsx63o4"); // 	C[1].y = AF[2].y + (AF[1].y - AF[2].y)/2 - (B[2].x-B[3].x)*3/4; //y_center- 1.5*width
UNSUPPORTED("439vzjsgoxct3lgumvqsuq0s1"); // 	C[2].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x)*3/4; //x_center-1.5*width
UNSUPPORTED("76tt8cgoymi6trgixufzzkaf0"); // 	C[2].y = C[1].y;
UNSUPPORTED("6tyxrag4xqp6g766cux9cx264"); // 	C[3].x = C[2].x;
UNSUPPORTED("4ho7h0h9c2lk1inv8t3opclga"); // 	C[3].y = C[0].y;
UNSUPPORTED("300ab5adr2vnr421lamy5ct24"); // 	C[4] = C[0];
UNSUPPORTED("41173djchd1fq40lkht8mrq1m"); // 	gvrender_polyline(job, C, 5);		        
UNSUPPORTED("eum17d4kc12w09ka3gn4xnjkl"); // 	/*dsDNA line right half*/
UNSUPPORTED("8wls9in1sb16jer08b5ssunvn"); // 	C[0].x = AF[1].x + (AF[0].x - AF[1].x)/2 + (B[2].x-B[3].x)*3/4;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("2r3im7kh170rpplkwcoi8l6jp"); // 	/*dsDNA line left half*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("8hqchqf2s017c644hehhcaxlo"); // 	C[1].x = AF[1].x + (AF[0].x - AF[1].x)/2 - (B[2].x-B[3].x)*3/4;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("55yooxu10kqgg7dajhsezqkfq"); //     case (18 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("46e0z9y77phnj064p0tf5bf94"); // 	 * X with a dashed line on the bottom
UNSUPPORTED("rj2e0ukg50e63tpjapabomkh"); // 	 * 
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("dlcnxc753jrol481tmux9lswq"); // 	 *           X
UNSUPPORTED("81manefx32xoejcl5dhw4vc4s"); // 	 *	     |
UNSUPPORTED("cz2kcexrt76ppdvw9mtzwl6l6"); // 	 *	------------          
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("29c55ze4t4mczxh3x1veeu005"); // 	D = (pointf*)zmalloc((sides + 12)*sizeof(pointf)); //12-sided x
UNSUPPORTED("bfjnm29zjvd3n7do4y87yuimk"); // 	D[0].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)/4; //x_center+widtht/2 , lower right corner of the x
UNSUPPORTED("6fadfx3cd7603xzoiam5zyl67"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/2; //y_center + width
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("4v3l9q24gxtxrar8btl47zoow"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/8; //D[0].y +width/4
UNSUPPORTED("6rgdcvz0zwjdie9l63ipu46l4"); // 	D[2].x = D[0].x - (B[2].x-B[3].x)/8; //D[0].x- width/4 //right nook of the x
UNSUPPORTED("ex033y5oj49zuzra10asffkia"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/8; //D[0].y+width/2 or D[1].y+width/4
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("1t32ie8e7he01k3ty0s11cjff"); // 	D[3].y = D[2].y + (B[3].y-B[4].y)/8; //D[2].y + width/4
UNSUPPORTED("bzw7esfwo40sfa8iehrj4pyr8"); // 	D[4].x = D[0].x;
UNSUPPORTED("12jatkjezhxoyrcbj7qw0kzrn"); // 	D[4].y = D[3].y + (B[3].y-B[4].y)/8; //top right corner of the x
UNSUPPORTED("e9ohdk7a5hiy6qw6kdxic8ih0"); // 	D[5].x = D[2].x;
UNSUPPORTED("9h546osr54mx5910v00kmvmo3"); // 	D[5].y = D[4].y;
UNSUPPORTED("926tc948ppa1dj5yf7ltaex6"); // 	D[6].x = AF[1].x + (AF[0].x - AF[1].x)/2; //x_center
UNSUPPORTED("4na1446f7z8re7jmm7p0szkgh"); // 	D[6].y = D[3].y; //top nook
UNSUPPORTED("2iw65hw9js3r2zpvqu9k4s5f3"); // 	D[7].x = D[6].x - (B[2].x-B[3].x)/8; //D[5] mirrowed across y
UNSUPPORTED("cdcewcs3o64fkxoeknvdtdd6h"); // 	D[7].y = D[5].y;
UNSUPPORTED("4gh2f7t5fi2gir99lnocdxmp0"); // 	D[8].x = D[7].x - (B[2].x-B[3].x)/8;//top left corner
UNSUPPORTED("agbg6evpctdfnyu3qhrm0w5ej"); // 	D[8].y = D[7].y;
UNSUPPORTED("ah5kglnqaow2xvlpo51wluq5y"); // 	D[9].x = D[8].x;
UNSUPPORTED("7eul86jxr4ht4r5bn3fgfljdq"); // 	D[9].y = D[3].y;
UNSUPPORTED("dvxukn20gcceywf45rsdlgwjk"); // 	D[10].x = D[8].x + (B[2].x-B[3].x)/8;
UNSUPPORTED("ealz2oalp4aksi0zz739qke65"); // 	D[10].y = D[2].y;
UNSUPPORTED("5pyd0cxb8ujx2mcsu0n68e5xt"); // 	D[11].x = D[8].x;
UNSUPPORTED("7yttvltea377p6nfz3hviflv7"); // 	D[11].y = D[1].y;
UNSUPPORTED("ed3f8qa9fqvj7h3ir97ic1dlc"); // 	D[12].x = D[8].x;
UNSUPPORTED("dg27h65j67kiidrf7m8vrmssw"); // 	D[12].y = D[0].y;
UNSUPPORTED("88b0xpldaxzgp2ib4r60x41bf"); // 	D[13].x = D[10].x;
UNSUPPORTED("ancic9a9poikrwxxwm0350zyt"); // 	D[13].y = D[12].y;
UNSUPPORTED("8to7w6h0h7szl9z3sky187uk"); // 	D[14].x = D[6].x; //bottom nook
UNSUPPORTED("4f8lav87663g2osgtdmm67fax"); // 	D[14].y = D[1].y;
UNSUPPORTED("4srifebhh66qjppjrk5fgx7my"); // 	D[15].x = D[2].x;
UNSUPPORTED("aojyz5q7eulsrjo7xi4v61pas"); // 	D[15].y = D[0].y;
UNSUPPORTED("eec4an2ita1w8buwmp3e0y9ri"); // 	gvrender_polygon(job, D, sides + 12, filled);
UNSUPPORTED("ezhguv4sszgbsjctxn3dkls1d"); // 	//2-part dash line
UNSUPPORTED("6c9mdcqeputx4llge5t3ro9r8"); // 	/*line below the x, bottom dash*/
UNSUPPORTED("1qnbpwnfgz995h9ijphe5qq4g"); // 	C[0].x = D[14].x; //x_center
UNSUPPORTED("1xkx02f07j9c7v00hsy0c15je"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("deqh8fifsu2we0oawe0cc0oa6"); // 	C[1].y = C[0].y + (B[3].y-B[4].y)/8; //y_center + 1/4*width
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("3x5sj9iyejrerw6luuq6elt8u"); // 	/*line below the x, top dash*/
UNSUPPORTED("1qnbpwnfgz995h9ijphe5qq4g"); // 	C[0].x = D[14].x; //x_center
UNSUPPORTED("2ggw25mqydk24dag1i69ct4yg"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/4;
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("e0n5uvucrlja6280zegwvnw5l"); // 	C[1].y = C[0].y + (B[3].y-B[4].y)/8;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("de8hrmjzk9llxqf5n9hseeur"); //     case (19 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("67e50jeeiwiw2yyny8x835sxw"); // 	 * hexagon with a dashed line on the bottom
UNSUPPORTED("rj2e0ukg50e63tpjapabomkh"); // 	 * 
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("d10jdrlg1miyun96p57ha1zcq"); // 	 *           O
UNSUPPORTED("81manefx32xoejcl5dhw4vc4s"); // 	 *	     |
UNSUPPORTED("cz2kcexrt76ppdvw9mtzwl6l6"); // 	 *	------------          
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("bce0986uths99en9ujtallv74"); // 	D = (pointf*)zmalloc((sides + 4)*sizeof(pointf)); //12-sided x
UNSUPPORTED("1r0dyice1nwwjxx60euskvs22"); // 	D[0].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)/8; //x_center+widtht/8 , lower right corner of the hexagon
UNSUPPORTED("6fadfx3cd7603xzoiam5zyl67"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/2; //y_center + width
UNSUPPORTED("7vtpwdfs55eauxhtxly0fq9yo"); // 	D[1].x = D[0].x + (B[2].x-B[3].x)/8;
UNSUPPORTED("4v3l9q24gxtxrar8btl47zoow"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/8; //D[0].y +width/4
UNSUPPORTED("80iff0mad95x21gnzwaa6r2du"); // 	D[2].x = D[1].x; //D[0].x- width/4
UNSUPPORTED("e85qlsqp5ec0c39ukmurh01le"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/4; //D[1].y+width/2
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("1t32ie8e7he01k3ty0s11cjff"); // 	D[3].y = D[2].y + (B[3].y-B[4].y)/8; //D[2].y + width/4
UNSUPPORTED("8so3hvsavyaik1857ci06i28n"); // 	D[4].x = D[3].x - (B[2].x-B[3].x)/4;
UNSUPPORTED("eplzuryql5ru5fo39zx6qq098"); // 	D[4].y = D[3].y; //top of the hexagon
UNSUPPORTED("85tgwpkocchmr8656hsvvujbk"); // 	D[5].x = D[4].x - (B[2].x-B[3].x)/8;
UNSUPPORTED("d78lk5o8y51l9z7neypp202ok"); // 	D[5].y = D[2].y;
UNSUPPORTED("a2qj96305wxo9gce1tujcuecz"); // 	D[6].x = D[5].x;
UNSUPPORTED("au3tvlkozqb147n50te46l8da"); // 	D[6].y = D[1].y; //left side
UNSUPPORTED("45h8dltyrq4aavcyjz923uke9"); // 	D[7].x = D[4].x;
UNSUPPORTED("3toyc6445kpow5ul101muzie7"); // 	D[7].y = D[0].y; //bottom
UNSUPPORTED("519fp4pixwl2w7d4ssm51lram"); // 	gvrender_polygon(job, D, sides + 4, filled);
UNSUPPORTED("ezhguv4sszgbsjctxn3dkls1d"); // 	//2-part dash line
UNSUPPORTED("6c9mdcqeputx4llge5t3ro9r8"); // 	/*line below the x, bottom dash*/
UNSUPPORTED("4fj9vuy8q45y638ge1ixleprj"); // 	C[0].x = AF[1].x + (AF[0].x - AF[1].x)/2; //x_center
UNSUPPORTED("1xkx02f07j9c7v00hsy0c15je"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("deqh8fifsu2we0oawe0cc0oa6"); // 	C[1].y = C[0].y + (B[3].y-B[4].y)/8; //y_center + 1/4*width
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("3x5sj9iyejrerw6luuq6elt8u"); // 	/*line below the x, top dash*/
UNSUPPORTED("4fj9vuy8q45y638ge1ixleprj"); // 	C[0].x = AF[1].x + (AF[0].x - AF[1].x)/2; //x_center
UNSUPPORTED("2ggw25mqydk24dag1i69ct4yg"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/4;
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("e0n5uvucrlja6280zegwvnw5l"); // 	C[1].y = C[0].y + (B[3].y-B[4].y)/8;
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("eezw6z44jruvicwzff6onzjsf"); //     case (20 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("4oo44mzmfh78pq3lzqwtmsfz4"); // 	 * X with a solid line on the bottom
UNSUPPORTED("rj2e0ukg50e63tpjapabomkh"); // 	 * 
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("dlcnxc753jrol481tmux9lswq"); // 	 *           X
UNSUPPORTED("81manefx32xoejcl5dhw4vc4s"); // 	 *	     |
UNSUPPORTED("cz2kcexrt76ppdvw9mtzwl6l6"); // 	 *	------------          
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("29c55ze4t4mczxh3x1veeu005"); // 	D = (pointf*)zmalloc((sides + 12)*sizeof(pointf)); //12-sided x
UNSUPPORTED("bfjnm29zjvd3n7do4y87yuimk"); // 	D[0].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)/4; //x_center+widtht/2 , lower right corner of the x
UNSUPPORTED("6fadfx3cd7603xzoiam5zyl67"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/2; //y_center + width
UNSUPPORTED("34tp01hd6y8buifqu81fp7kah"); // 	D[1].x = D[0].x;
UNSUPPORTED("4v3l9q24gxtxrar8btl47zoow"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/8; //D[0].y +width/4
UNSUPPORTED("6rgdcvz0zwjdie9l63ipu46l4"); // 	D[2].x = D[0].x - (B[2].x-B[3].x)/8; //D[0].x- width/4 //right nook of the x
UNSUPPORTED("ex033y5oj49zuzra10asffkia"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/8; //D[0].y+width/2 or D[1].y+width/4
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("1t32ie8e7he01k3ty0s11cjff"); // 	D[3].y = D[2].y + (B[3].y-B[4].y)/8; //D[2].y + width/4
UNSUPPORTED("bzw7esfwo40sfa8iehrj4pyr8"); // 	D[4].x = D[0].x;
UNSUPPORTED("12jatkjezhxoyrcbj7qw0kzrn"); // 	D[4].y = D[3].y + (B[3].y-B[4].y)/8; //top right corner of the x
UNSUPPORTED("e9ohdk7a5hiy6qw6kdxic8ih0"); // 	D[5].x = D[2].x;
UNSUPPORTED("9h546osr54mx5910v00kmvmo3"); // 	D[5].y = D[4].y;
UNSUPPORTED("926tc948ppa1dj5yf7ltaex6"); // 	D[6].x = AF[1].x + (AF[0].x - AF[1].x)/2; //x_center
UNSUPPORTED("4na1446f7z8re7jmm7p0szkgh"); // 	D[6].y = D[3].y; //top nook
UNSUPPORTED("2iw65hw9js3r2zpvqu9k4s5f3"); // 	D[7].x = D[6].x - (B[2].x-B[3].x)/8; //D[5] mirrowed across y
UNSUPPORTED("cdcewcs3o64fkxoeknvdtdd6h"); // 	D[7].y = D[5].y;
UNSUPPORTED("4gh2f7t5fi2gir99lnocdxmp0"); // 	D[8].x = D[7].x - (B[2].x-B[3].x)/8;//top left corner
UNSUPPORTED("agbg6evpctdfnyu3qhrm0w5ej"); // 	D[8].y = D[7].y;
UNSUPPORTED("ah5kglnqaow2xvlpo51wluq5y"); // 	D[9].x = D[8].x;
UNSUPPORTED("7eul86jxr4ht4r5bn3fgfljdq"); // 	D[9].y = D[3].y;
UNSUPPORTED("dvxukn20gcceywf45rsdlgwjk"); // 	D[10].x = D[8].x + (B[2].x-B[3].x)/8;
UNSUPPORTED("ealz2oalp4aksi0zz739qke65"); // 	D[10].y = D[2].y;
UNSUPPORTED("5pyd0cxb8ujx2mcsu0n68e5xt"); // 	D[11].x = D[8].x;
UNSUPPORTED("7yttvltea377p6nfz3hviflv7"); // 	D[11].y = D[1].y;
UNSUPPORTED("ed3f8qa9fqvj7h3ir97ic1dlc"); // 	D[12].x = D[8].x;
UNSUPPORTED("dg27h65j67kiidrf7m8vrmssw"); // 	D[12].y = D[0].y;
UNSUPPORTED("88b0xpldaxzgp2ib4r60x41bf"); // 	D[13].x = D[10].x;
UNSUPPORTED("ancic9a9poikrwxxwm0350zyt"); // 	D[13].y = D[12].y;
UNSUPPORTED("8to7w6h0h7szl9z3sky187uk"); // 	D[14].x = D[6].x; //bottom nook
UNSUPPORTED("4f8lav87663g2osgtdmm67fax"); // 	D[14].y = D[1].y;
UNSUPPORTED("4srifebhh66qjppjrk5fgx7my"); // 	D[15].x = D[2].x;
UNSUPPORTED("aojyz5q7eulsrjo7xi4v61pas"); // 	D[15].y = D[0].y;
UNSUPPORTED("eec4an2ita1w8buwmp3e0y9ri"); // 	gvrender_polygon(job, D, sides + 12, filled);
UNSUPPORTED("5hl7s3qlipucjwhwahk282sx8"); // 	/*line below the x*/
UNSUPPORTED("d55sgple6c28z0c52aycup5at"); // 	C[0] = D[14];
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("4a3l5xe9qdenhns41qoiytki5"); // 	C[1].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7ea60jessa65lyhvl39qnzgmi"); //     case (21 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("67e50jeeiwiw2yyny8x835sxw"); // 	 * hexagon with a dashed line on the bottom
UNSUPPORTED("rj2e0ukg50e63tpjapabomkh"); // 	 * 
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("d10jdrlg1miyun96p57ha1zcq"); // 	 *           O
UNSUPPORTED("81manefx32xoejcl5dhw4vc4s"); // 	 *	     |
UNSUPPORTED("cz2kcexrt76ppdvw9mtzwl6l6"); // 	 *	------------          
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("dbi6282o67zdvjpioemev8l1g"); // 	//x_center is AF[1].x + (AF[0].x - AF[1].x)/2
UNSUPPORTED("3kvpch1u0mkkh8mvlzphvxdio"); // 	//y_center is AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("agoljir79z51apt1tmk0f0sc7"); // 	//width units are (B[2].x-B[3].x)/2 or (B[3].y-B[4].y)/2;
UNSUPPORTED("bce0986uths99en9ujtallv74"); // 	D = (pointf*)zmalloc((sides + 4)*sizeof(pointf)); //12-sided x
UNSUPPORTED("1r0dyice1nwwjxx60euskvs22"); // 	D[0].x = AF[1].x + (AF[0].x-AF[1].x)/2 + (B[2].x-B[3].x)/8; //x_center+widtht/8 , lower right corner of the hexagon
UNSUPPORTED("6fadfx3cd7603xzoiam5zyl67"); // 	D[0].y = AF[2].y + (AF[1].y - AF[2].y)/2 + (B[3].y-B[4].y)/2; //y_center + width
UNSUPPORTED("7vtpwdfs55eauxhtxly0fq9yo"); // 	D[1].x = D[0].x + (B[2].x-B[3].x)/8;
UNSUPPORTED("4v3l9q24gxtxrar8btl47zoow"); // 	D[1].y = D[0].y + (B[3].y-B[4].y)/8; //D[0].y +width/4
UNSUPPORTED("80iff0mad95x21gnzwaa6r2du"); // 	D[2].x = D[1].x; //D[0].x- width/4
UNSUPPORTED("e85qlsqp5ec0c39ukmurh01le"); // 	D[2].y = D[1].y + (B[3].y-B[4].y)/4; //D[1].y+width/2
UNSUPPORTED("78e3c23lx1btcjbe2srm6rcal"); // 	D[3].x = D[0].x;
UNSUPPORTED("1t32ie8e7he01k3ty0s11cjff"); // 	D[3].y = D[2].y + (B[3].y-B[4].y)/8; //D[2].y + width/4
UNSUPPORTED("8so3hvsavyaik1857ci06i28n"); // 	D[4].x = D[3].x - (B[2].x-B[3].x)/4;
UNSUPPORTED("eplzuryql5ru5fo39zx6qq098"); // 	D[4].y = D[3].y; //top of the hexagon
UNSUPPORTED("85tgwpkocchmr8656hsvvujbk"); // 	D[5].x = D[4].x - (B[2].x-B[3].x)/8;
UNSUPPORTED("d78lk5o8y51l9z7neypp202ok"); // 	D[5].y = D[2].y;
UNSUPPORTED("a2qj96305wxo9gce1tujcuecz"); // 	D[6].x = D[5].x;
UNSUPPORTED("au3tvlkozqb147n50te46l8da"); // 	D[6].y = D[1].y; //left side
UNSUPPORTED("45h8dltyrq4aavcyjz923uke9"); // 	D[7].x = D[4].x;
UNSUPPORTED("3toyc6445kpow5ul101muzie7"); // 	D[7].y = D[0].y; //bottom
UNSUPPORTED("519fp4pixwl2w7d4ssm51lram"); // 	gvrender_polygon(job, D, sides + 4, filled);
UNSUPPORTED("5hl7s3qlipucjwhwahk282sx8"); // 	/*line below the x*/
UNSUPPORTED("axr0pn9n2azga1eiee2sfhf90"); // 	C[0].x = AF[1].x + (AF[0].x - AF[1].x)/2;
UNSUPPORTED("cyy862hp2el6mhfkug2y3f1ca"); // 	C[0].y = D[0].y;
UNSUPPORTED("ec2n0yx6hife8hckl0aiamvcw"); // 	C[1].x = C[0].x;
UNSUPPORTED("4a3l5xe9qdenhns41qoiytki5"); // 	C[1].y = AF[2].y + (AF[1].y - AF[2].y)/2; //y_center
UNSUPPORTED("borq5nu8vs8newp4f3zf2ti2x"); // 	gvrender_polyline(job, C, 2);
UNSUPPORTED("hjs1b12h76k9zk31lumwwf9f"); // 	/*dsDNA line*/
UNSUPPORTED("dcf6dzx1g3i2ccb6d7iwrzof8"); // 	C[0].x = AF[1].x;
UNSUPPORTED("550o47jm3dqr37h7j0k434zzb"); // 	C[0].y = AF[2].y + (AF[1].y - AF[2].y)/2;
UNSUPPORTED("5pw7rn49ku3rfnvp5lbqoivb7"); // 	C[1].x = AF[0].x;
UNSUPPORTED("e0hztszh4dg3gbas5k24di6x9"); // 	C[1].y = AF[2].y + (AF[0].y - AF[3].y)/2;
UNSUPPORTED("7vyejosjncamgazhav0j15x5q"); // 	gvrender_polyline(job, C, 2);				
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("414355qtxrrdrp88lqmu8xuq8"); // 	break;        
UNSUPPORTED("i1a2vh2der7lv63hy4pg74j2"); //     case (22 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("4bfue86t6gftlcgwzff6qshk7"); // 	 *  
UNSUPPORTED("2hxqoj7zl8r8xv6e4z6djhz03"); // 	 *      D[1] = AF[1]      |	 *       +----------------+ 	 *       |	        D[0] 	 *       |                    	 *       |                    /    
UNSUPPORTED("43leeaho94cuu8h6ufftsvw4m"); // 	 *       |                   /
UNSUPPORTED("722lri4xki5u0btwl5flr6qxu"); // 	 *       |        +-------+ /
UNSUPPORTED("8kn1gxbxamlotuexrxky3o76q"); // 	 *       |	  |       |/
UNSUPPORTED("dscqqxbpov7i3z8kvqhzn45ew"); // 	 *	 +--------+
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("5kvv3jvgrdk38xmgwem87tcf9"); // 	/* Add the tab edges. */
UNSUPPORTED("6t4waazc6b2bir3lb3a09si65"); // 	D = (pointf*)zmalloc((sides + 5)*sizeof(pointf)); /*5 new points*/
UNSUPPORTED("c6x47uv986ws1wlxw5sjqn6n0"); // 	D[0].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("d1eg8u95ffvpn8cj0t175qe8"); // 	D[0].y = B[1].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("45vton2uqw835epqg2o0j1qee"); // 	D[1].x = B[3].x;
UNSUPPORTED("2so1ozst5cwd02o33408hxi1n"); // 	D[1].y = B[3].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("2y7ipfj9drc5mfgjluf5y6k6p"); // 	D[2].x = AF[2].x;
UNSUPPORTED("bul5ue4x9puypr5rmyrlxz24r"); // 	D[2].y = AF[2].y;
UNSUPPORTED("7p7sbu9cltsgpiplr8l952qz1"); // 	D[3].x = B[2].x + (B[2].x - B[3].x)/2;
UNSUPPORTED("5bb26b07x6ootbr2j1wrams3"); // 	D[3].y = AF[2].y;
UNSUPPORTED("b8bego41qu7w8qkom46we6jth"); // 	D[4].x = B[2].x + (B[2].x - B[3].x)/2;
UNSUPPORTED("6mxx226hjtdwefdnjzy5zlzs5"); // 	D[4].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("ekx1o9xcvdl245i9hyyocg56v"); // 	D[5].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("d282nefakffro2sjsbh4yuptv"); // 	D[5].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("55sv0um7zomjrn48wiyob6gxo"); // 	D[6].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("9ubgi9tlkqj3h0i4575ls3ev0"); // 	D[6].y = AF[3].y;
UNSUPPORTED("eg7fjcwjh1jq1riv8kcy7dei0"); // 	D[7].y = AF[0].y - (AF[0].y - AF[3].y)/2; /*triangle point */
UNSUPPORTED("dwd8nqni0j130xliflvrlc5ty"); // 	D[7].x = AF[0].x; /*triangle point */
UNSUPPORTED("8pqi0dq2ugizqvkq5c093agrm"); // 	D[8].y = AF[0].y;
UNSUPPORTED("1vws7hcvciczirrj9548bn4f2"); // 	D[8].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("9v8e5z6hrq6fr5p16pg80kc21"); // 	gvrender_polygon(job, D, sides + 5, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("6bkffeiw7fqij7bq5hs7k51hl"); //     case (23 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("4bfue86t6gftlcgwzff6qshk7"); // 	 *  
UNSUPPORTED("1kf6abx7iat1puo0g8swxlr2j"); // 	 *      D[1] = AF[1]      |	 *       +----------------+ 	 *       |		D[0] 	 *       |                    	 *       |                    /    
UNSUPPORTED("43leeaho94cuu8h6ufftsvw4m"); // 	 *       |                   /
UNSUPPORTED("apyndmlwtw3z8vzadqn20r8hu"); // 	 *       +----------------+ /
UNSUPPORTED("4teonqvrubwrmkqajn3x92uqz"); // 	 *	  	          |/
UNSUPPORTED("79f8fxsn2gta6r8d9x9n38rzy"); // 	 *	 
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("5kvv3jvgrdk38xmgwem87tcf9"); // 	/* Add the tab edges. */
UNSUPPORTED("9capq7a8vc2wzqyavbvsqo0js"); // 	D = (pointf*)zmalloc((sides + 3)*sizeof(pointf)); /*3 new points*/
UNSUPPORTED("c6x47uv986ws1wlxw5sjqn6n0"); // 	D[0].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("d1eg8u95ffvpn8cj0t175qe8"); // 	D[0].y = B[1].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("45vton2uqw835epqg2o0j1qee"); // 	D[1].x = B[3].x;
UNSUPPORTED("2so1ozst5cwd02o33408hxi1n"); // 	D[1].y = B[3].y - (B[3].y - B[4].y)/2;
UNSUPPORTED("2y7ipfj9drc5mfgjluf5y6k6p"); // 	D[2].x = AF[2].x;
UNSUPPORTED("bopqgv9t3o6ppbojdunc5qjfl"); // 	D[2].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("1q0xgxya7e8fzpb1l2p14fk7g"); // 	D[3].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("94cxjx74gts01u98ngcns0j0o"); // 	D[3].y = AF[2].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("21elcwekxeowj7i0450kbasb4"); // 	D[4].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("8eiljnxcc6es7v531vw3p943t"); // 	D[4].y = AF[3].y;
UNSUPPORTED("av1198agtncjj9dsyfskbhrt"); // 	D[5].y = AF[0].y - (AF[0].y - AF[3].y)/2;/*triangle point*/
UNSUPPORTED("evuqotctuiu9zd74xdrh07pft"); // 	D[5].x = AF[0].x; /*triangle point */
UNSUPPORTED("96e1uuucniuu6eztjwb5aua2b"); // 	D[6].y = AF[0].y;
UNSUPPORTED("55sv0um7zomjrn48wiyob6gxo"); // 	D[6].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("66hq5hkfg6ixw5enzwwhsontq"); // 	gvrender_polygon(job, D, sides + 3, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("bi7entdk2ummrdev9ic5z88zh"); //     case (24 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("4bfue86t6gftlcgwzff6qshk7"); // 	 *  
UNSUPPORTED("2vf6dvre5x62f4afex2v3wssm"); // 	 *      /|     
UNSUPPORTED("7at1abf1biy3h7owo91fj09sq"); // 	 *     / +----------------+ 
UNSUPPORTED("758ct1qslb1outqo15t8qplkx"); // 	 *    /                   |        
UNSUPPORTED("8a7qsgirg007q526nvsyh566t"); // 	 *    \                   |   
UNSUPPORTED("asz6zd87dnrnppnilt2vmd742"); // 	 *     \ +----------------+ 
UNSUPPORTED("2kg8z59q8qzdxgf73c76yxsbr"); // 	 *	\| 	          
UNSUPPORTED("79f8fxsn2gta6r8d9x9n38rzy"); // 	 *	 
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("5kvv3jvgrdk38xmgwem87tcf9"); // 	/* Add the tab edges. */
UNSUPPORTED("9capq7a8vc2wzqyavbvsqo0js"); // 	D = (pointf*)zmalloc((sides + 3)*sizeof(pointf)); /*3 new points*/
UNSUPPORTED("8bi8jgvnnbgjoe0rtiovk3kka"); // 	D[0].x = AF[0].x;
UNSUPPORTED("3nnpsf5gr04nj80fm603hxrew"); // 	D[0].y = AF[0].y - (B[3].y-B[4].y)/2;
UNSUPPORTED("equct6fawt1qt33fcaa2vbx6d"); // 	D[1].x = B[2].x + (B[2].x - B[3].x)/2;
UNSUPPORTED("5je1a4102rsjz8djkg50vxsld"); // 	D[1].y = AF[0].y - (B[3].y-B[4].y)/2;/*D[0].y*/
UNSUPPORTED("604g9voy0i4aesl8z4qkl3yua"); // 	D[2].x = B[2].x + (B[2].x - B[3].x)/2;/*D[1].x*/
UNSUPPORTED("1ivg7d69vao0ni5v39q7i7ibq"); // 	D[2].y = B[2].y;
UNSUPPORTED("2fmhbcdtkklozhbyqm15zba5v"); // 	D[3].x = AF[1].x; /*triangle point*/
UNSUPPORTED("eqycx49fc5p5s83bled63dukr"); // 	D[3].y = AF[1].y - (AF[1].y - AF[2].y)/2; /*triangle point*/
UNSUPPORTED("c8m3lk83ymc1l1swiupvjucow"); // 	D[4].x = B[2].x + (B[2].x - B[3].x)/2;/*D[1].x*/
UNSUPPORTED("5gvqnechiy5h1orxyla8m94qt"); // 	D[4].y = AF[2].y;
UNSUPPORTED("bx6z8nzgcgr5pc8q8l8tqs1su"); // 	D[5].y = AF[2].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("f4icccql1zjta9sdefq8pvulw"); // 	D[5].x = B[2].x + (B[2].x - B[3].x)/2;/*D[1].x*/
UNSUPPORTED("29rdh1i6t6ac606dh48ug09fe"); // 	D[6].y = AF[3].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("d1t9t9l13vuttc72cjjt70czg"); // 	D[6].x = AF[0].x;/*D[0]*/
UNSUPPORTED("66hq5hkfg6ixw5enzwwhsontq"); // 	gvrender_polygon(job, D, sides + 3, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9uxvjt1cntxds9hia35xi2zrn"); //     case (25 << 24):
UNSUPPORTED("59hm5rqv13343impaqd8bjv9p"); // 	/*
UNSUPPORTED("11hn7suih6n97ek5ack6vv7ni"); // 	 * Adjust the perimeter for the protrusions.
UNSUPPORTED("cmrglhtmovtl6mh8dyeurkeed"); // 	 *
UNSUPPORTED("4bfue86t6gftlcgwzff6qshk7"); // 	 *  
UNSUPPORTED("2vf6dvre5x62f4afex2v3wssm"); // 	 *      /|     
UNSUPPORTED("7at1abf1biy3h7owo91fj09sq"); // 	 *     / +----------------+ 
UNSUPPORTED("di3oj6qvy7yx7ce8epceo9gcl"); // 	 *    /   		D[0] 
UNSUPPORTED("7qliw4diraiwyyzmhk2fkvkbg"); // 	 *   /                    |    
UNSUPPORTED("eexvhoejrrctvbe726qrvjb8s"); // 	 *   \                    |        
UNSUPPORTED("8a7qsgirg007q526nvsyh566t"); // 	 *    \                   |   
UNSUPPORTED("n2k5o28c2p32kiem5mklm8ba"); // 	 *     \ +--------+       + 
UNSUPPORTED("2uh322uvvif8yj3w2dy2rb7na"); // 	 *	\| 	  |       |
UNSUPPORTED("6eycfkuag7hal8zbmntmzcu9e"); // 	 *	          +-------+
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("5kvv3jvgrdk38xmgwem87tcf9"); // 	/* Add the tab edges. */
UNSUPPORTED("421a2mdhv0gef0ua8a8hrh1gr"); // 	D = (pointf*)zmalloc((sides + 5)*sizeof(pointf)); /*3 new points*/
UNSUPPORTED("8bi8jgvnnbgjoe0rtiovk3kka"); // 	D[0].x = AF[0].x;
UNSUPPORTED("3nnpsf5gr04nj80fm603hxrew"); // 	D[0].y = AF[0].y - (B[3].y-B[4].y)/2;
UNSUPPORTED("equct6fawt1qt33fcaa2vbx6d"); // 	D[1].x = B[2].x + (B[2].x - B[3].x)/2;
UNSUPPORTED("5je1a4102rsjz8djkg50vxsld"); // 	D[1].y = AF[0].y - (B[3].y-B[4].y)/2;/*D[0].y*/
UNSUPPORTED("604g9voy0i4aesl8z4qkl3yua"); // 	D[2].x = B[2].x + (B[2].x - B[3].x)/2;/*D[1].x*/
UNSUPPORTED("1ivg7d69vao0ni5v39q7i7ibq"); // 	D[2].y = B[2].y;
UNSUPPORTED("2fmhbcdtkklozhbyqm15zba5v"); // 	D[3].x = AF[1].x; /*triangle point*/
UNSUPPORTED("eqycx49fc5p5s83bled63dukr"); // 	D[3].y = AF[1].y - (AF[1].y - AF[2].y)/2; /*triangle point*/
UNSUPPORTED("c8m3lk83ymc1l1swiupvjucow"); // 	D[4].x = B[2].x + (B[2].x - B[3].x)/2;/*D[1].x*/
UNSUPPORTED("5gvqnechiy5h1orxyla8m94qt"); // 	D[4].y = AF[2].y;
UNSUPPORTED("bx6z8nzgcgr5pc8q8l8tqs1su"); // 	D[5].y = AF[2].y + (B[3].y-B[4].y)/2;
UNSUPPORTED("f4icccql1zjta9sdefq8pvulw"); // 	D[5].x = B[2].x + (B[2].x - B[3].x)/2;/*D[1].x*/
UNSUPPORTED("29rdh1i6t6ac606dh48ug09fe"); // 	D[6].y = AF[3].y + (B[3].y - B[4].y)/2;
UNSUPPORTED("55sv0um7zomjrn48wiyob6gxo"); // 	D[6].x = B[1].x - (B[2].x - B[3].x)/2;
UNSUPPORTED("46jv04wtkylcmc9s9pv56lr3c"); // 	D[7].x = B[1].x - (B[2].x - B[3].x)/2;/*D[6].x*/
UNSUPPORTED("7588ab6v6tp8x8bpjy3d79jex"); // 	D[7].y = AF[3].y;
UNSUPPORTED("3jown0xzpispkq8oomn10deyz"); // 	D[8].x = AF[3].x;
UNSUPPORTED("39c46sl6j7nxvcb2pae8rs398"); // 	D[8].y = AF[3].y;
UNSUPPORTED("9v8e5z6hrq6fr5p16pg80kc21"); // 	gvrender_polygon(job, D, sides + 5, filled);
UNSUPPORTED("cgk7heai5pdojykyc6x6f5pzz"); // 	free(D);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9yor276xqtqe07q4gsz71t5o4"); //     free(B);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 acyez4b7guim63q8crmnyi18
// static double userSize(node_t * n) 
public static Object userSize(Object... arg) {
UNSUPPORTED("8yl20lmgb1f7rteptp4jmmd4w"); // static double userSize(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cldfniz1yu2rzamoi5a3tri2q"); //     double w, h;
UNSUPPORTED("7nla3nwmcqssau6gicpyolcww"); //     w = late_double(n, N_width, 0.0, 0.01);
UNSUPPORTED("14rygobuxmhem0qjemmp7zwup"); //     h = late_double(n, N_height, 0.0, 0.02);
UNSUPPORTED("92gsjf70pdi57i20e9rszpm1o"); //     return (ROUND((MAX(w, h))*72));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5gahokttzv65lspm84ao1le37
// shape_kind shapeOf(node_t * n) 
public static int shapeOf(ST_Agnode_s n) {
ENTERING("5gahokttzv65lspm84ao1le37","shapeOf");
try {
	ST_shape_desc sh = (ST_shape_desc) ND_shape(n);
    CFunctionImpl ifn; //void (*ifn) (node_t *);
    if (N(sh))
	return enumAsInt(shape_kind.class, "SH_UNSET");
    ifn = (CFunctionImpl) ND_shape(n).fns.initfn;
    if (ifn.getName().equals("poly_init"))
	return enumAsInt(shape_kind.class, "SH_POLY");
UNSUPPORTED("251a710sgr57bnrs3uh7ppfpi"); //     else if (ifn == record_init)
UNSUPPORTED("uubga3e6j1jsmn61hfok2zwk"); // 	return SH_RECORD;
UNSUPPORTED("cpzx2lwu889clk2f1d0k4c9jd"); //     else if (ifn == point_init)
UNSUPPORTED("f4x4vap21dff1trk1lrzzb8u5"); // 	return SH_POINT;
UNSUPPORTED("alkskrmw3fjn82qi1t2kyi6uh"); //     else if (ifn == epsf_init)
UNSUPPORTED("5hp3oli47xj0s4fk7yj1dairi"); // 	return SH_EPSF;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("7if5cqgy6h2m78kwe6gagv7p"); // 	return SH_UNSET;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("5gahokttzv65lspm84ao1le37","shapeOf");
}
}




//3 e8riwo21j5t1g1tewsbo39z48
// boolean isPolygon(node_t * n) 
public static Object isPolygon(Object... arg) {
UNSUPPORTED("6tth154tjxb21b1rr8m3w5lbj"); // boolean isPolygon(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9uh2xgxrn4veetmkfkbe9iq7y"); //     return (ND_shape(n) && (ND_shape(n)->fns->initfn == poly_init));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a11xv6duihbr3d6gkgo2ye2j5
// static void poly_init(node_t * n) 
public static void poly_init(ST_Agnode_s n) {
ENTERING("a11xv6duihbr3d6gkgo2ye2j5","poly_init");
try {
    final ST_pointf dimen = new ST_pointf(), min_bb = new ST_pointf(), bb = new ST_pointf();
    final ST_point imagesize = new ST_point();
    final ST_pointf P = new ST_pointf(), Q = new ST_pointf(), R = new ST_pointf();
    ST_pointf.Array vertices = null;
    CString p, sfile, fxd;
    double temp, alpha, beta, gamma;
    double orientation, distortion, skew;
    double sectorangle, sidelength, skewdist, gdistortion, gskew;
    double angle, sinx, cosx, xmax=0, ymax=0, scalex, scaley;
    double width=0, height=0, marginx, marginy, spacex;
    boolean regular; int peripheries, sides;
    boolean isBox; int i, j, outp;
    ST_polygon_t poly = new ST_polygon_t();
    regular = ND_shape(n).polygon.regular!=0;
    peripheries = ND_shape(n).polygon.peripheries;
    sides = ND_shape(n).polygon.sides;
    orientation = ND_shape(n).polygon.orientation;
    skew = ND_shape(n).polygon.skew;
    distortion = ND_shape(n).polygon.distortion;
    regular |= mapbool(agget(n, new CString("regular")));
    /* all calculations in floating point POINTS */
    /* make x and y dimensions equal if node is regular
     *   If the user has specified either width or height, use the max.
     *   Else use minimum default value.
     * If node is not regular, use the current width and height.
     */
    if (regular) {
UNSUPPORTED("637wuscax8bj094m73fwq3n00"); // 	double sz = userSize(n);
UNSUPPORTED("6wtpde1bxbwd70jz3vuemuvqe"); // 	if (sz > 0.0)
UNSUPPORTED("c4cn28kuy1f86y4d8rbxy1a4g"); // 	    width = height = sz;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("mafr2nmpj8xgeranqldzxhtb"); // 	    width = (((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->width);
UNSUPPORTED("l2a1m5p66005ftc6gdbosugj"); // 	    height = (((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->height);
UNSUPPORTED("61qxt5l8ums7d9os9ungn3rao"); // 	    width = height = ((((((width)<(height)?(width):(height)))*72>=0)?(int)((((width)<(height)?(width):(height)))*72 + .5):(int)((((width)<(height)?(width):(height)))*72 - .5)));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    } else {
	width = (ROUND((ND_width(n))*72));
	height = (ROUND((ND_height(n))*72));
    }
    peripheries = late_int(n, Z.z().N_peripheries, peripheries, 0);
    orientation += late_double(n, Z.z().N_orientation, 0.0, -360.0);
    if (sides == 0) {		/* not for builtins */
UNSUPPORTED("dd178b113cb8130tl6q70lcp3"); // 	skew = late_double(n, N_skew, 0.0, -100.0);
UNSUPPORTED("cp83hdn3dp0a7rp9bauc3bgki"); // 	sides = late_int(n, N_sides, 4, 0);
UNSUPPORTED("abmuc3vqirf3i48480fj0k14g"); // 	distortion = late_double(n, N_distortion, 0.0, -100.0);
    }
    /* get label dimensions */
    dimen.___(ND_label(n).dimen);
    /* minimal whitespace around label */
    if (ROUND(abs(dimen.x))!=0 || ROUND(abs(dimen.y))!=0) {
    	/* padding */
	if ((p = agget(n, new CString("margin")))!=null) {
UNSUPPORTED("4dlqwm3pklzgz2e777dm56n03"); // 	    marginx = marginy = 0;
UNSUPPORTED("r186dwelv54pq63p2yo4czig"); // 	    i = sscanf(p, "%lf,%lf", &marginx, &marginy);
UNSUPPORTED("bjp5a2wbzhormf75ov5fumqto"); // 	    if (marginx < 0)
UNSUPPORTED("3tsws28ifjzq7ju8xs3ye4x18"); // 		marginx = 0;
UNSUPPORTED("7gfecwqgqof787z8u1mgh7qoj"); // 	    if (marginy < 0)
UNSUPPORTED("1xpqznf0i4ljd2b5j81ipsvtg"); // 		marginy = 0;
UNSUPPORTED("1lcx62wzgnn34tk5li0sgoqwm"); // 	    if (i > 0) {
UNSUPPORTED("dau1s6m0w92gp7bvqz3f63wnp"); // 		dimen.x += 2 * ((((marginx)*72>=0)?(int)((marginx)*72 + .5):(int)((marginx)*72 - .5)));
UNSUPPORTED("79iqlz01of88ftxysvivw2hgw"); // 		if (i > 1)
UNSUPPORTED("6opppos2hcjet4cn76130ykba"); // 		    dimen.y += 2 * ((((marginy)*72>=0)?(int)((marginy)*72 + .5):(int)((marginy)*72 - .5)));
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("8bwwxn4jop0urcsfnygjofg9s"); // 		    dimen.y += 2 * ((((marginx)*72>=0)?(int)((marginx)*72 + .5):(int)((marginx)*72 - .5)));
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("87bdwkkwbzyswxnepdd9bj8mb"); // 		{((dimen).x += 4*4); ((dimen).y += 2*4);};
	} else
	    {
		((ST_pointf) dimen).x = dimen.x + 4*4;
		((ST_pointf) dimen).y = dimen.y + 2*4;
	    };
    }
    spacex = dimen.x - ND_label(n).dimen.x;
    /* quantization */
    if ((temp = ((ST_Agraphinfo_t)agraphof(n).castTo_ST_Agobj_s().data.castTo(ST_Agraphinfo_t.class)).drawing.quantum) > 0.0) {
UNSUPPORTED("3nqb0s5rkwj3igt71vooj8asd"); // 	temp = ((((temp)*72>=0)?(int)((temp)*72 + .5):(int)((temp)*72 - .5)));
UNSUPPORTED("5fxtqwy8liyvnx1yvsou5hb4o"); // 	dimen.x = quant(dimen.x, temp);
UNSUPPORTED("et885f1jcqpske6ip856arouv"); // 	dimen.y = quant(dimen.y, temp);
    }
    imagesize.setInt("x", 0);
    imagesize.setInt("y", 0);
    if (ND_shape(n).usershape) {
	/* custom requires a shapefile
	 * not custom is an adaptable user shape such as a postscript
	 * function.
	 */
UNSUPPORTED("7jbvoylyb27di8f54ufxj4mbk"); // 	if ((*((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->name)==*("custom")&&!strcmp((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->name,"custom"))) {
UNSUPPORTED("cnfv2ayyl46ohdl5p4pc75swz"); // 	    sfile = agget(n, "shapefile");
UNSUPPORTED("6llro6gigojo2r8oo6c4k320o"); // 	    imagesize = gvusershape_size(agraphof(n), sfile);
UNSUPPORTED("3ngt3ika8ppq3m9vbgf2q5lu1"); // 	    if ((imagesize.x == -1) && (imagesize.y == -1)) {
UNSUPPORTED("5l8jenkv77ax02t47zzxyv1k0"); // 		agerr(AGWARN,
UNSUPPORTED("7hgyav5bbs1v4kts1oocozork"); // 		      "No or improper shapefile=\"%s\" for node \"%s\"\n",
UNSUPPORTED("34s5a4xy12nydt3idmis4np67"); // 		      (sfile ? sfile : "<nil>"), agnameof(n));
UNSUPPORTED("apwtbaz0akr5pg5p6uwcroaan"); // 		imagesize.x = imagesize.y = 0;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("4xiqgw4br039sl4r32gg5jv6k"); // 		(((Agraphinfo_t*)(((Agobj_t*)(agraphof(n)))->data))->has_images) = (!(0));
UNSUPPORTED("3nl4wsbxuqlad4tcg8vmg99rx"); // 		imagesize.x += 2;	/* some fixed padding */
UNSUPPORTED("da00oaavfp6fwqwgshhrodz2r"); // 		imagesize.y += 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    } else if ((sfile = agget(n, new CString("image")))!=null && (sfile.charAt(0) != '\0')) {
UNSUPPORTED("76t38i30025xohbzg8w9z4pn9"); // 	imagesize = gvusershape_size(agraphof(n), sfile);
UNSUPPORTED("b8spvmvtuxcciaejq8j1xhu5s"); // 	if ((imagesize.x == -1) && (imagesize.y == -1)) {
UNSUPPORTED("cw5grwj6gbj94jcztvnp2ooyj"); // 	    agerr(AGWARN,
UNSUPPORTED("68jdsrao22ymfpb8e1rdezyez"); // 		  "No or improper image=\"%s\" for node \"%s\"\n",
UNSUPPORTED("5lmxoq1rboqrxrchjf5ubg6w5"); // 		  (sfile ? sfile : "<nil>"), agnameof(n));
UNSUPPORTED("7wmzbnczyvj4oocepujtghrka"); // 	    imagesize.x = imagesize.y = 0;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("71mfu0uflnm85dbt8g2oxs9rd"); // 	    (((Agraphinfo_t*)(((Agobj_t*)(agraphof(n)))->data))->has_images) = (!(0));
UNSUPPORTED("286u48muwmjkomlzqufoqm5cw"); // 	    imagesize.x += 2;	/* some fixed padding */
UNSUPPORTED("1x57knvrmlciu7odfroo3paso"); // 	    imagesize.y += 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    /* initialize node bb to labelsize */
    ((ST_pointf) bb).x = ((dimen.x)>(imagesize.x)?(dimen.x):(imagesize.x));
    ((ST_pointf) bb).y = ((dimen.y)>(imagesize.y)?(dimen.y):(imagesize.y));
    /* I don't know how to distort or skew ellipses in postscript */
    /* Convert request to a polygon with a large number of sides */
    if ((sides <= 2) && ((distortion != 0.) || (skew != 0.))) {
	sides = 120;
    }
    /* extra sizing depends on if label is centered vertically */
    p = agget(n, new CString("labelloc"));
    if (p!=null && (p.charAt(0) == 't' || p.charAt(0) == 'b'))
    n.castTo_ST_Agobj_s().data.castTo_ST_Agnodeinfo_t().label.setInt("valign", p.charAt(0));
    else
    n.castTo_ST_Agobj_s().data.castTo_ST_Agnodeinfo_t().label.setInt("valign", 'c');
    isBox = (sides == 4 && (((orientation>=0)?(int)(orientation + .5):(int)(orientation - .5)) % 90) == 0
	     && distortion == 0. && skew == 0.);
    if (isBox) {
	/* for regular boxes the fit should be exact */
    } else if (((ST_Agnodeinfo_t)n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class)).shape.polygon.vertices!=null) {
UNSUPPORTED("4adqsyjwqwzs50ggjp57ok6u7"); // 	poly_desc_t* pd = (poly_desc_t*)(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->polygon->vertices;
UNSUPPORTED("1fjwgzo5xkijo98ycmzhal8yv"); // 	bb = pd->size_gen(bb);
    } else {
	/* for all other shapes, compute a smallest ellipse
	 * containing bb centered on the origin, and then pad for that.
	 * We assume the ellipse is defined by a scaling up of bb.
	 */
	temp = bb.y * 1.41421356237309504880;
	if (height > temp && (((ST_Agnodeinfo_t)n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class)).label.valign == 'c')) {
	    /* if there is height to spare
	     * and the label is centered vertically
	     * then just pad x in proportion to the spare height */
	    bb.setDouble("x", bb.x * sqrt(1. / (1. - ((bb.y / height) * (bb.y / height)))));
	} else {
	    bb.setDouble("x", bb.x * 1.41421356237309504880);
	    bb.setDouble("y", temp);
	}
	if (sides > 2) {
	    temp = cos(3.14159265358979323846 / sides);
	    bb.setDouble("x", bb.x / temp);
	    bb.setDouble("y", bb.y / temp);
	    /* FIXME - for odd-sided polygons, e.g. triangles, there
	       would be a better fit with some vertical adjustment of the shape */
	}
    }
    /* at this point, bb is the minimum size of node that can hold the label */
    min_bb.___(bb);
    /* increase node size to width/height if needed */
    fxd = late_string(n, Z.z().N_fixed, new CString("false"));
    if ((fxd.charAt(0) == 's') && (N(strcmp(fxd,new CString("shape"))))) {
	bb.setDouble("x", width);
	bb.setDouble("y", height);
	poly.setInt("option", poly.option | (1 << 11));
    } else if (mapbool(fxd)) {
	/* check only label, as images we can scale to fit */
	if ((width < (((ST_Agnodeinfo_t)n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class)).label.dimen.x) 
		|| (height < (((ST_Agnodeinfo_t)n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class)).label.dimen.y))))
	    System.err.println(
		  "node '%s', graph '%s' size too small for label\n");
		  //agnameof(n), agnameof(agraphof(n)));
	bb.setDouble("x",  width);
	bb.setDouble("y", height);
    } else {
	width = MAX(width, bb.x);
	((ST_pointf) bb).x = width;
	height = MAX(height, bb.y);
	((ST_pointf) bb).y = height;
    }
    /* If regular, make dimensions the same.
     * Need this to guarantee final node size is regular.
     */
    if (regular) {
    width = MAX(bb.x, bb.y);
    height = width;
    ((ST_pointf) bb).x = width;
    ((ST_pointf) bb).y = width;
    }
    /* Compute space available for label.  Provides the justification borders */
    if (N(mapbool(late_string(n, Z.z().N_nojustify, new CString("false"))))) {
	if (isBox) {
		((ST_pointf) ND_label(n).space).x = MAX(dimen.x,bb.x) - spacex;
	}
	else if (dimen.y < bb.y) {
	    temp = bb.x * sqrt(1.0 - SQR(dimen.y) / SQR(bb.y));
	    ND_label(n).space.setDouble("x", MAX(dimen.x,temp) - spacex);
        }
	else
	    ND_label(n).space.setDouble("x", dimen.x - spacex);
    } else {
	    ND_label(n).space.setDouble("x", dimen.x - spacex);
    }
    if ((poly.option & (1 << 11)) == 0) {
	temp = bb.y - min_bb.y;
	if (dimen.y < imagesize.y)
	    temp += imagesize.y - dimen.y;
	((ST_pointf) ND_label(n).space).y = dimen.y + temp;
    }
    outp = peripheries;
    if (peripheries < 1)
	outp = 1;
    if (sides < 3) {		/* ellipses */
	sides = 2;
	vertices = new ST_pointf.Array(outp * sides);
	P.setDouble("x", bb.x / 2.);
	P.setDouble("y", bb.y / 2.);
	vertices.setDouble("x", -P.x);
	vertices.setDouble("y", -P.y);
	vertices.plus(1).setDouble("x", P.x);
	vertices.plus(1).setDouble("y", P.y);
	if (peripheries > 1) {
UNSUPPORTED("4ofenmfgj7cgyf624qmugcx77"); // 	    for (j = 1, i = 2; j < peripheries; j++) {
UNSUPPORTED("458w3r6n3nidn2j2b154phpzt"); // 		P.x += 4;
UNSUPPORTED("24bcrwtjsfswpmtwxnadf0cn1"); // 		P.y += 4;
UNSUPPORTED("byrwdiqkace7e10l3pibk54wg"); // 		vertices[i].x = -P.x;
UNSUPPORTED("bicdgzzy9pdopb03hn9l48yns"); // 		vertices[i].y = -P.y;
UNSUPPORTED("en9fjm2thtauyxn9t7v4j2xgl"); // 		i++;
UNSUPPORTED("2pejlnamuvmi1m7339vzctpnb"); // 		vertices[i].x = P.x;
UNSUPPORTED("dy64x78vvm5nufbuxsn0bi8ng"); // 		vertices[i].y = P.y;
UNSUPPORTED("en9fjm2thtauyxn9t7v4j2xgl"); // 		i++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6g6b8zsanvqyc953c4jh1j7iy"); // 	    bb.x = 2. * P.x;
UNSUPPORTED("b5z0hw4dacenv33xsaex70g8d"); // 	    bb.y = 2. * P.y;
	}
    } else {
/*
 * FIXME - this code is wrong - it doesn't work for concave boundaries.
 *          (e.g. "folder"  or "promoter")
 *   I don't think it even needs sectorangle, or knowledge of skewed shapes.
 *   (Concepts that only work for convex regular (modulo skew/distort) polygons.)
 *
 *   I think it only needs to know inside v. outside (by always drawing
 *   boundaries clockwise, say),  and the two adjacent segments.
 *
 *   It needs to find the point where the two lines, parallel to
 *   the current segments, and outside by GAP distance, intersect.   
 */
	vertices = new ST_pointf.Array(outp * sides);
	if (((ST_polygon_t)ND_shape(n).polygon).vertices!=null) {
UNSUPPORTED("3ghle84ieryaenfnlbzrfv7bw"); // 	    poly_desc_t* pd = (poly_desc_t*)(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->polygon->vertices;
UNSUPPORTED("227lpcg9dt83m2bm8yshb4djf"); // 	    pd->vertex_gen (vertices, &bb);
UNSUPPORTED("cc3jvnwvbhjhro4adeet363yd"); // 	    xmax = bb.x/2;
UNSUPPORTED("39rdmp8vl9muqtv7xs1xwtrwk"); // 	    ymax = bb.y/2;
	} else {
	    sectorangle = 2. * M_PI / sides;
	    sidelength = sin(sectorangle / 2.);
	    skewdist = hypot(fabs(distortion) + fabs(skew), 1.);
	    gdistortion = distortion * SQRT2 / cos(sectorangle / 2.);
	    gskew = skew / 2.;
	    angle = (sectorangle - M_PI) / 2.;
	    sinx = sin(angle); cosx = cos(angle);
	    ((ST_pointf) R).x = .5 * cosx;
	    ((ST_pointf) R).y = .5 * sinx;
	    xmax = ymax = 0.;
	    angle += (M_PI - sectorangle) / 2.;
	    for (i = 0; i < sides; i++) {
	    /*next regular vertex */
		angle += sectorangle;
	    sinx = sin(angle); cosx = cos(angle);
	    ((ST_pointf) R).x = R.x + sidelength * cosx;
	    ((ST_pointf) R).y = R.y + sidelength * sinx;
	    /*distort and skew */
	    ((ST_pointf) P).x = R.x * (skewdist + R.y * gdistortion) + R.y * gskew;
	    ((ST_pointf) P).y = R.y;
	    /*orient P.x,P.y */
		alpha = RADIANS(orientation) + atan2(P.y, P.x);
	    sinx = sin(alpha); cosx = cos(alpha);
	    ((ST_pointf) P).y = hypot(P.x, P.y);
	    ((ST_pointf) P).x = P.y;
	    ((ST_pointf) P).x = P.x * cosx;
	    ((ST_pointf) P).y = P.y * sinx;
	    /*scale for label */
	    ((ST_pointf) P).x = P.x * bb.x;
	    ((ST_pointf) P).y = P.y * bb.y;
	    /*find max for bounding box */
		xmax = MAX(fabs(P.x), xmax);
		ymax = MAX(fabs(P.y), ymax);
	    /* store result in array of points */
		vertices.plus(i).setStruct(P);
		if (isBox) { /* enforce exact symmetry of box */
			vertices.get(1).x = -P.x;
			vertices.get(1).y = P.y;
			vertices.get(2).x = -P.x;
			vertices.get(2).y = -P.y;
			vertices.get(3).x = P.x;
			vertices.get(3).y = -P.y;
		    break;
		}
	    }
	}
	/* apply minimum dimensions */
	xmax *= 2.;
	ymax *= 2.;
	((ST_pointf) bb).x = MAX(width, xmax);
	((ST_pointf) bb).y = MAX(height, ymax);
	scalex = bb.x / xmax;
	scaley = bb.y / ymax;
	for (i = 0; i < sides; i++) {
	    P.____(vertices.plus(i));
	    ((ST_pointf) P).x = P.x * scalex;
	    ((ST_pointf) P).y = P.y * scaley;
	    vertices.plus(i).setStruct(P);
	}
	if (peripheries > 1) {
UNSUPPORTED("3x6t3unoi91ezbh3iz168cm2t"); // 	    Q = vertices[(sides - 1)];
UNSUPPORTED("8cm8js7jdmpakzujw3wo4h6jk"); // 	    R = vertices[0];
UNSUPPORTED("5zpv8twf25wr8n71ql3lh8ku2"); // 	    beta = atan2(R.y - Q.y, R.x - Q.x);
UNSUPPORTED("9mlrumbikcvketd18jx1ox7k7"); // 	    for (i = 0; i < sides; i++) {
UNSUPPORTED("gkm8nb6f6ispdzj0ausiv1fe"); // 		/*for each vertex find the bisector */
UNSUPPORTED("8tp61rvblb9bmqfwgyknlk906"); // 		P = Q;
UNSUPPORTED("1ls3xc8rwvn3763c32mx1wzsd"); // 		Q = R;
UNSUPPORTED("1zrelve2mvbnzah086dkomf6k"); // 		R = vertices[(i + 1) % sides];
UNSUPPORTED("bgx8ee996r89memnp0ea0b80m"); // 		alpha = beta;
UNSUPPORTED("5p9jzpcd51evtwqyugnwk50vf"); // 		beta = atan2(R.y - Q.y, R.x - Q.x);
UNSUPPORTED("dwskcoivmu9pc5kth75x0ersl"); // 		gamma = (alpha + 3.14159265358979323846 - beta) / 2.;
UNSUPPORTED("cmm5tvlcafe2aso9bkk3kl7of"); // 		/*find distance along bisector to */
UNSUPPORTED("bebwurfm1a1h1bywf9kf5ueug"); // 		/*intersection of next periphery */
UNSUPPORTED("1mtgr15b978d0tdunbpj2pkdp"); // 		temp = 4 / sin(gamma);
UNSUPPORTED("28mxt6c4230xruf63s6u415y9"); // 		/*convert this distance to x and y */
UNSUPPORTED("1fxw0fz2b6iq6p6qy58mx9mwu"); // 		*&sinx = sin((alpha - gamma)); *&cosx = cos((alpha - gamma));
UNSUPPORTED("8j7vx250v0icumolzos2p5qa8"); // 		sinx *= temp;
UNSUPPORTED("35ed1mrpnziq164g6cg4stt5w"); // 		cosx *= temp;
UNSUPPORTED("eu3ptwi3s2200v4253yk1x69t"); // 		/*save the vertices of all the */
UNSUPPORTED("7lc5jxgzj6z4lq7sd9y2b6vex"); // 		/*peripheries at this base vertex */
UNSUPPORTED("86nzalouete6viryy967d5g9u"); // 		for (j = 1; j < peripheries; j++) {
UNSUPPORTED("dautpj9jyj2qwa8jpujdh3436"); // 		    Q.x += cosx;
UNSUPPORTED("7p6tl6s20kdual1ysfoxl8wku"); // 		    Q.y += sinx;
UNSUPPORTED("1yw9xq85ss81cogn9jrg24ojc"); // 		    vertices[i + j * sides] = Q;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9mlrumbikcvketd18jx1ox7k7"); // 	    for (i = 0; i < sides; i++) {
UNSUPPORTED("aa5s79go4kwos2as72rcsdrxf"); // 		P = vertices[i + (peripheries - 1) * sides];
UNSUPPORTED("7ee9ageu4efyramsg9jn6klpb"); // 		bb.x = ((2. * fabs(P.x))>(bb.x)?(2. * fabs(P.x)):(bb.x));
UNSUPPORTED("lklvdmn7xiqbxhpgdeufcvjj"); // 		bb.y = ((2. * fabs(P.y))>(bb.y)?(2. * fabs(P.y)):(bb.y));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
	}
    }
    poly.setInt("regular", regular?1:0);
    poly.setInt("peripheries", peripheries);
    poly.setInt("sides", sides);
    poly.setDouble("orientation", orientation);
    poly.setDouble("skew", skew);
    poly.setDouble("distortion", distortion);
    poly.vertices = vertices;
    if ((poly.option & (1 << 11))!=0) {
	/* set width and height to reflect label and shape */
UNSUPPORTED("7kk8oru3b3copylmq3gssx6qx"); // 	(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->width) = ((((dimen.x)>(bb.x)?(dimen.x):(bb.x)))/(double)72);
UNSUPPORTED("8oouzms2x039fhfcfxm7yc4su"); // 	(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->height) = ((((dimen.y)>(bb.y)?(dimen.y):(bb.y)))/(double)72);
    } else {
    n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class).setDouble("width", ((bb.x)/(double)72));
    n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class).setDouble("height", ((bb.y)/(double)72));
    }
    n.castTo_ST_Agobj_s().data.castTo(ST_Agnodeinfo_t.class).setPtr("shape_info", poly);
} finally {
LEAVING("a11xv6duihbr3d6gkgo2ye2j5","poly_init");
}
}




//3 63sj12avbdw6e27zf3sedls1r
// static void poly_free(node_t * n) 
public static Object poly_free(Object... arg) {
UNSUPPORTED("cfl0ro4734avs9rtdlar7nbg8"); // static void poly_free(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5ha1ng0rlvg0kiui0qhgme4nb"); //     polygon_t *p = ND_shape_info(n);
UNSUPPORTED("3cvmixd2u1g2d9l03kuxyyxxw"); //     if (p) {
UNSUPPORTED("3cjbelr7499ch9kn6lbjaz7l7"); // 	free(p->vertices);
UNSUPPORTED("cy5x5dma0v4hiepir7lrfuo17"); // 	free(p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//static Agnode_s lastn;	/* last node argument */
//static polygon_t poly;
//static int last, outp, sides;
//static final ST_pointf O = new ST_pointf(); /* point (0,0) */
//static pointf vertex;
//static double xsize, ysize, scalex, scaley, box_URx, box_URy;
//3 570t4xovyyfqipaikkf63crmk
//static boolean poly_inside(inside_t * inside_context, pointf p) 
public static boolean poly_inside(ST_inside_t inside_context, ST_pointf p) {
// WARNING!! STRUCT
return poly_inside_w_(inside_context, p.copy());
}
private static boolean poly_inside_w_(ST_inside_t inside_context, final ST_pointf p) {
ENTERING("570t4xovyyfqipaikkf63crmk","poly_inside");
try {
    int i, i1, j;
    boolean s;
    final ST_pointf P = new ST_pointf(), Q = new ST_pointf(), R = new ST_pointf();
    ST_boxf bp = inside_context.s_bp;
    ST_Agnode_s n = inside_context.s_n;
    P.___(ccwrotatepf(p, 90 * GD_rankdir(agraphof(n))));
    /* Quick test if port rectangle is target */
    if (bp!=null) {
	final ST_boxf bbox = new ST_boxf();
	bbox.___(bp.getStruct());
	return INSIDE(P, bbox);
    }
    if (NEQ(n, Z.z().lastn)) {
	double n_width = 0, n_height = 0;
	Z.z().poly = (ST_polygon_t) ND_shape_info(n);
	Z.z().vertex = Z.z().poly.vertices;
	Z.z().sides = Z.z().poly.sides;
	if ((Z.z().poly.option & (1 << 11))!=0) {
UNSUPPORTED("18yw1scg4sol8bhyf1vedj9kn"); // 	   boxf bb = polyBB(poly); 
UNSUPPORTED("7rz7vxyxao0efec2nvd6g19m1"); // 	    n_width = bb.UR.x - bb.LL.x;
UNSUPPORTED("4h0k2wroz3xqx1ljokdbaqaad"); // 	    n_height = bb.UR.y - bb.LL.y;
UNSUPPORTED("dgykcjw02yoka8uz5b7jdc2ct"); // 	    /* get point and node size adjusted for rankdir=LR */
UNSUPPORTED("75jifr4aucrxp2hvnsrcfunej"); // 	    if (GD_flip(agraphof(n))) {
UNSUPPORTED("e53876tm7q1oasuu013njtgx"); // 		ysize = n_width;
UNSUPPORTED("7wnmmcv8dfzi1bdwml4vcxf0w"); // 		xsize = n_height;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("10ux82vu0kynxilmf6ak7x70q"); // 		xsize = n_width;
UNSUPPORTED("5xao1mdiugxzaq03na34mbl5w"); // 		ysize = n_height;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
	} else {
	    /* get point and node size adjusted for rankdir=LR */
	    if (GD_flip(agraphof(n))!=0) {
UNSUPPORTED("dapvd4c0ggliaqcj08jvao221"); // 		ysize = ND_lw(n) + ND_rw(n);
UNSUPPORTED("8t3g4d9acruono62leh5a8hxh"); // 		xsize = ND_ht(n);
	    } else {
		Z.z().xsize = ND_lw(n) + ND_rw(n);
		Z.z().ysize = ND_ht(n);
	    }
	    n_width = (ROUND((ND_width(n))*72));
	    n_height = (ROUND((ND_height(n))*72));
	}
	/* scale */
	if (Z.z().xsize == 0.0)
	    Z.z().xsize = 1.0;
	if (Z.z().ysize == 0.0)
	    Z.z().ysize = 1.0;
	Z.z().scalex = n_width / Z.z().xsize;
	Z.z().scaley = n_height / Z.z().ysize;
	Z.z().box_URx = n_width / 2.0;
	Z.z().box_URy = n_height / 2.0;
	/* index to outer-periphery */
	Z.z().outp = (Z.z().poly.peripheries - 1) * Z.z().sides;
	if (Z.z().outp < 0)
	    Z.z().outp = 0;
	Z.z().lastn = (ST_Agnode_s) n;
    }
    /* scale */
    P.setDouble("x", P.x * Z.z().scalex);
    P.setDouble("y", P.y * Z.z().scaley);
    /* inside bounding box? */
    if ((fabs(P.x) > Z.z().box_URx) || (fabs(P.y) > Z.z().box_URy))
	return false;
    /* ellipses */
    if (Z.z().sides <= 2)
	return (hypot(P.x / Z.z().box_URx, P.y / Z.z().box_URy) < 1.);
    /* use fast test in case we are converging on a segment */
    i = Z.z().last % Z.z().sides;		/* in case last left over from larger polygon */
    i1 = (i + 1) % Z.z().sides;
    Q.___(Z.z().vertex.plus(i + Z.z().outp).getStruct());
    R.___(Z.z().vertex.plus(i1 + Z.z().outp).getStruct());
    if (N(same_side(P, (ST_pointf)Z.z().O, Q, R)))   /* false if outside the segment's face */
	return false;
    /* else inside the segment face... */
    if ((s = same_side(P, Q, R, (ST_pointf)Z.z().O)) && (same_side(P, R, (ST_pointf)Z.z().O, Q))) /* true if between the segment's sides */
	return NOT(0);
    /* else maybe in another segment */
    for (j = 1; j < Z.z().sides; j++) { /* iterate over remaining segments */
	if (s) { /* clockwise */
	    i = i1;
	    i1 = (i + 1) % Z.z().sides;
	} else { /* counter clockwise */
	    i1 = i;
	    i = (i + Z.z().sides - 1) % Z.z().sides;
	}
	if (N(same_side(P, (ST_pointf)Z.z().O, (ST_pointf)Z.z().vertex.plus(i + Z.z().outp).getStruct(), (ST_pointf)Z.z().vertex.plus(i1 + Z.z().outp).getStruct()))) { /* false if outside any other segment's face */
	    Z.z().last = i;
	    return false;
	}
    }
    /* inside all segments' faces */
    Z.z().last = i;			/* in case next edge is to same side */
    return NOT(0);
} finally {
LEAVING("570t4xovyyfqipaikkf63crmk","poly_inside");
}
}




//3 5mmuhvq40xadw0g9mzlauyztq
// static int poly_path(node_t * n, port * p, int side, boxf rv[], int *kptr) 
public static int poly_path(ST_Agnode_s n, ST_port p, int side, Object rv, Object kptr) {
ENTERING("5mmuhvq40xadw0g9mzlauyztq","poly_path");
try {
    side = 0;
    if (ND_label(n).html && ND_has_port(n)) {
UNSUPPORTED("67g7bthntnw8syb6zd03ueg84"); // 	side = html_path(n, p, side, rv, kptr);
    }
    return side;
} finally {
LEAVING("5mmuhvq40xadw0g9mzlauyztq","poly_path");
}
}




//3 857i3hwbu9mbq4nwur2q7e7er
// static int invflip_side(int side, int rankdir) 
public static Object invflip_side(Object... arg) {
UNSUPPORTED("acksa1hz632l0v9ucpm5gmqzk"); // static int invflip_side(int side, int rankdir)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7gnjhunyvxphjgrfh8byey4ch"); //     switch (rankdir) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("o4wjkq58uh9dgs94m2vxettc"); // 	switch (side) {
UNSUPPORTED("a0zo28ne6fq7qm9hko3jwrsie"); // 	case (1<<2):
UNSUPPORTED("asl0z4i3qt99vpfphpr7hpk5"); // 	    side = (1<<0);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5uxczmgv9jelovrky9lyqmqxn"); // 	case (1<<0):
UNSUPPORTED("aj9jgzaslnfuc2iy41yo6577i"); // 	    side = (1<<2);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("o4wjkq58uh9dgs94m2vxettc"); // 	switch (side) {
UNSUPPORTED("a0zo28ne6fq7qm9hko3jwrsie"); // 	case (1<<2):
UNSUPPORTED("csyxlzh6yvg14dkwm5h0q8l4e"); // 	    side = (1<<1);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5uxczmgv9jelovrky9lyqmqxn"); // 	case (1<<0):
UNSUPPORTED("6ob9sb98jfamphtvv99f9nny7"); // 	    side = (1<<3);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3vvicpwbia6xzcxsn2qnkbzq8"); // 	case (1<<3):
UNSUPPORTED("aj9jgzaslnfuc2iy41yo6577i"); // 	    side = (1<<2);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("ad90yo3mu0ffjurb9egult4pi"); // 	case (1<<1):
UNSUPPORTED("asl0z4i3qt99vpfphpr7hpk5"); // 	    side = (1<<0);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("33l7a58zp8vj6fuliwdkk2nkn"); //     case 3:
UNSUPPORTED("o4wjkq58uh9dgs94m2vxettc"); // 	switch (side) {
UNSUPPORTED("a0zo28ne6fq7qm9hko3jwrsie"); // 	case (1<<2):
UNSUPPORTED("csyxlzh6yvg14dkwm5h0q8l4e"); // 	    side = (1<<1);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5uxczmgv9jelovrky9lyqmqxn"); // 	case (1<<0):
UNSUPPORTED("6ob9sb98jfamphtvv99f9nny7"); // 	    side = (1<<3);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3vvicpwbia6xzcxsn2qnkbzq8"); // 	case (1<<3):
UNSUPPORTED("asl0z4i3qt99vpfphpr7hpk5"); // 	    side = (1<<0);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("ad90yo3mu0ffjurb9egult4pi"); // 	case (1<<1):
UNSUPPORTED("aj9jgzaslnfuc2iy41yo6577i"); // 	    side = (1<<2);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2nk83e61yc1xqh0sxx13m5l1j"); //     return side;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 72pzdmirzds0yer4ks1ooxvic
// static double invflip_angle(double angle, int rankdir) 
public static Object invflip_angle(Object... arg) {
UNSUPPORTED("1klgft1h7fhevbm1j1guzv58"); // static double invflip_angle(double angle, int rankdir)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7gnjhunyvxphjgrfh8byey4ch"); //     switch (rankdir) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("e7qgsf2gzf7fv8r5lpdfqp2gp"); // 	angle *= -1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("b5wrpw5rvhjh7999v3sqqlbo3"); // 	angle -= M_PI * 0.5;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("33l7a58zp8vj6fuliwdkk2nkn"); //     case 3:
UNSUPPORTED("536aocvem6ko7h9t50pllxla0"); // 	if (angle == M_PI)
UNSUPPORTED("kxow9q31jmisg5yv60fj9z3g"); // 	    angle = -0.5 * M_PI;
UNSUPPORTED("3hy3z7oxc494l61va60rwh9k3"); // 	else if (angle == M_PI * 0.75)
UNSUPPORTED("76t0zkyxc3q2wnpcajih9mf65"); // 	    angle = -0.25 * M_PI;
UNSUPPORTED("bd02ns5pweyapa70g9ozio3m4"); // 	else if (angle == M_PI * 0.5)
UNSUPPORTED("a0pp5xd6lligtfp0riunw38t3"); // 	    angle = 0;
UNSUPPORTED("8cqf9j5edmb4u2xnd8lkahkht"); // /* clang complains about self assignment of double
UNSUPPORTED("205i7xisgiaz1vhn9p93tsw5a"); // 	else if (angle == M_PI * 0.25)
UNSUPPORTED("76g7hlyzy67q9n7p5l89y4gxw"); // 	    angle = angle;
UNSUPPORTED("e5xwyhh2l2jm6g9w2ofnktaf6"); //  */
UNSUPPORTED("8pqjflzypl5wbdev1h4r6ee0e"); // 	else if (angle == 0)
UNSUPPORTED("3uy8u4gjki2ksohuj3gn6ewkj"); // 	    angle = M_PI * 0.5;
UNSUPPORTED("bqlwd51jj33yedz7tuck5hukd"); // 	else if (angle == M_PI * -0.25)
UNSUPPORTED("3s431nqj2tfm95djdmjfjig6h"); // 	    angle = M_PI * 0.75;
UNSUPPORTED("tl121swu8uuow1dlzumo1pyi"); // 	else if (angle == M_PI * -0.5)
UNSUPPORTED("aa92obzwij392if7nnjch6dtz"); // 	    angle = M_PI;
UNSUPPORTED("8cqf9j5edmb4u2xnd8lkahkht"); // /* clang complains about self assignment of double
UNSUPPORTED("2waz5md3krpirny5m7gagynkc"); // 	else if (angle == M_PI * -0.75)
UNSUPPORTED("76g7hlyzy67q9n7p5l89y4gxw"); // 	    angle = angle;
UNSUPPORTED("e5xwyhh2l2jm6g9w2ofnktaf6"); //  */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("62ygf2gmqakbkjtv70bqh5q3i"); //     return angle;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 54t5x4hsq6ie4hn83dix0fi3g
// static pointf compassPoint(inside_t * ictxt, double y, double x) 
public static Object compassPoint(Object... arg) {
UNSUPPORTED("1owp098dshhw9x2d86x61ho3n"); // static pointf compassPoint(inside_t * ictxt, double y, double x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5jw267n0iigspndf3p51uuoyt"); //     pointf curve[4];		/* bezier control points for a straight line */
UNSUPPORTED("2ol68djy9gbphj8kdfml5q1ej"); //     node_t *n = ictxt->s.n;
UNSUPPORTED("f121hhzfkpb97hn84g46lhxdh"); //     graph_t* g = agraphof(n);
UNSUPPORTED("347leky6wh51yiydoij5od0h2"); //     int rd = GD_rankdir(g);
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("saqn1396zzjkeo01vp1tskia"); //     p.x = x;
UNSUPPORTED("5jdhcgi82gtmvn690v78zmkpe"); //     p.y = y;
UNSUPPORTED("2imvfuepadgxdlfwq3qmsatju"); //     if (rd)
UNSUPPORTED("8gcpvoawmbrjuiq80lglpl2bn"); // 	p = cwrotatepf(p, 90 * rd);
UNSUPPORTED("b4ktwkbs8awubvwfgfeqzhlx0"); //     curve[0].x = curve[0].y = 0;
UNSUPPORTED("dcqc3vt7dwuvg73lixbbwd3dj"); //     curve[1] = curve[0];
UNSUPPORTED("ahj7ruzql6g6cm5nvomizsgcz"); //     curve[3] = curve[2] = p;
UNSUPPORTED("6wkk7v0v7iyai22oyhq16dcno"); //     bezier_clip(ictxt, ND_shape(n)->fns->insidefn, curve, 1);
UNSUPPORTED("2imvfuepadgxdlfwq3qmsatju"); //     if (rd)
UNSUPPORTED("ip6d55dog3nmeksqauqb1fyo"); // 	curve[0] = ccwrotatepf(curve[0], 90 * rd);
UNSUPPORTED("7jlv4v811jdfr56u2h3wdxxbm"); //     return curve[0];
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4oj0c3dwqqjei7u5u2ik9yyw1
// static int compassPort(node_t * n, boxf * bp, port * pp, char *compass, int sides, 	    inside_t * ictxt) 
public static Object compassPort(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("axo7c40w8kmff5juyfulc507z"); // compassPort(node_t * n, boxf * bp, port * pp, char *compass, int sides,
UNSUPPORTED("8wsp6kb4b5k1nk99rjf8cd50s"); // 	    inside_t * ictxt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c57pq0f87j6dnbcvygu7v6k84"); //     boxf b;
UNSUPPORTED("4t6cefa2mlqz8cen3hr5w5o2"); //     pointf p, ctr;
UNSUPPORTED("en7ch189nkys76f42mlo1s5zz"); //     int rv = 0;
UNSUPPORTED("39lrh4yp65e7bz2yxmw4eykrf"); //     double theta = 0.0;
UNSUPPORTED("1whpo0vj6bc0o4y50ozcd6pib"); //     boolean constrain = 0;
UNSUPPORTED("39rwowaomcdh9hhpbv5vzxqtg"); //     boolean dyna = 0;
UNSUPPORTED("ekt4uw7awxhup7bp69cd0de32"); //     int side = 0;
UNSUPPORTED("26uc2ctav83esiworswb0yyqw"); //     boolean clip = NOT(0);
UNSUPPORTED("8p9hhcu69i2810srbetaxiscj"); //     boolean defined;
UNSUPPORTED("dw42q4y6we417p9qhmaei6d3f"); //     double maxv;  /* sufficiently large value outside of range of node */
UNSUPPORTED("8ix20ei8mhm5e1r57koylhxmw"); //     if (bp) {
UNSUPPORTED("ddqw44b5basd7iv78obd8twlq"); // 	b = *bp;
UNSUPPORTED("bdd8asje3pa2bu5sxtn1lp6vh"); // 	p = pointfof((b.LL.x + b.UR.x) / 2, (b.LL.y + b.UR.y) / 2);
UNSUPPORTED("ap5anvaqlfe3mcudjsohthesw"); // 	defined = NOT(0);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("dza301uiuu792cezaz0eoyzsr"); // 	p.x = p.y = 0.;
UNSUPPORTED("ek9a7u2yx8w4r9x5k7somxuup"); // 	if (GD_flip(agraphof(n))) {
UNSUPPORTED("e21k9f24vr25zdbgo37m5er48"); // 	    b.UR.x = ND_ht(n) / 2.;
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("7luuqd8n7bpffoa8v27jp7tn3"); // 	    b.UR.y = ND_lw(n);
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("dma0yc0pj8g9efl81ud62uh2x"); // 	    b.UR.y = ND_ht(n) / 2.;
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
UNSUPPORTED("59beisnsabbp6eavnuxrqch2d"); // 	    b.UR.x = ND_lw(n);
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1e1jhd4j2f4wri70ymrc2k0pg"); // 	defined = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("w2aiqm3p7bifxbzx4tx4mipl"); //     maxv = MAX(b.UR.x,b.UR.y);
UNSUPPORTED("b8awq55ll3vhs3fjdyqay8kms"); //     maxv *= 4.0;
UNSUPPORTED("cm85kba38i3sc3kntnc0a8070"); //     ctr = p;
UNSUPPORTED("e7z3qfzj5jj1f70il8lep0oyu"); //     if (compass && *compass) {
UNSUPPORTED("72tyr9kgl0hmhdft6u5nwswi3"); // 	switch (*compass++) {
UNSUPPORTED("2fzjr952o6hmcz3ad5arl2n8d"); // 	case 'e':
UNSUPPORTED("caapivv2sv3cdu0rt3fnciovd"); // 	    if (*compass)
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("3s6xpfbxpp44ou5loxsjfgzqp"); //                 if (ictxt)
UNSUPPORTED("8whok6jl4olniblvibxhrbbre"); //                     p = compassPoint(ictxt, ctr.y, maxv);
UNSUPPORTED("c0op0grmjt3kp22s10twqy66r"); //                 else
UNSUPPORTED("5f4jye7znkk6hbv6lv0l9l0hs"); // 		    p.x = b.UR.x;
UNSUPPORTED("8u0aqa3sxpk302o1oqxok6u47"); // 		theta = 0.0;
UNSUPPORTED("8cz8xetupjr38r0y1sfojc387"); // 		constrain = NOT(0);
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("9ni9ftlf9oep71byodeubp1sr"); // 		clip = 0;
UNSUPPORTED("4sdfaevi5d5qk596bdiw1t1t5"); // 		side = sides & (1<<1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("b4quboi30n0afnbpgm9chdttx"); // 	case 's':
UNSUPPORTED("c2y7pxvutf20cxm0z3nfgoc3k"); // 	    p.y = b.LL.y;
UNSUPPORTED("b4ce4svfzxg7z2fd1fquu4qmu"); // 	    constrain = NOT(0);
UNSUPPORTED("c3sati4oun39vfql5zx4ru6z"); // 	    clip = 0;
UNSUPPORTED("bzgzeuh7ihvw4c4eysc3dgpzd"); // 	    switch (*compass) {
UNSUPPORTED("6qkxsufvygbc44eq3d6xorgsx"); // 	    case '\0':
UNSUPPORTED("9ca4neaxnf1dd0hyuuubj4egq"); // 		theta = -M_PI * 0.5;
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("3s6xpfbxpp44ou5loxsjfgzqp"); //                 if (ictxt)
UNSUPPORTED("2iohu3tvlkzx2emq04ycxkhta"); //                     p = compassPoint(ictxt, -maxv, ctr.x);
UNSUPPORTED("c0op0grmjt3kp22s10twqy66r"); //                 else
UNSUPPORTED("3ly2ywet0m7qfki5yj2nz9t87"); //                     p.x = ctr.x;
UNSUPPORTED("yc8wdhrrn5mzbqwxidxp3i2y"); // 		side = sides & (1<<0);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("mnoev43mr2cyh3zj9yiupqzy"); // 	    case 'e':
UNSUPPORTED("avfplp4wadl774qo2yrqn2btg"); // 		theta = -M_PI * 0.25;
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("4qnqhz6577yhq6u9919ve4tjb"); // 		    p = compassPoint(ictxt, -maxv, maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("5f4jye7znkk6hbv6lv0l9l0hs"); // 		    p.x = b.UR.x;
UNSUPPORTED("b0weojc8y88qjfkoujifnu9ag"); // 		side = sides & ((1<<0) | (1<<1));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("xzkwmazk8r4ms7xbr46l9xyq"); // 	    case 'w':
UNSUPPORTED("a6j042vifpt4pgkwczny2dy24"); // 		theta = -M_PI * 0.75;
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("c0hdr34iyaygjxcr6a65hns2g"); // 		    p = compassPoint(ictxt, -maxv, -maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("e2vcgqbz5sfyjwfyadlmm3s7n"); // 		    p.x = b.LL.x;
UNSUPPORTED("9yg4wc52hqtj6s3orou0nnbq4"); // 		side = sides & ((1<<0) | (1<<3));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("bt2g0yhsy3c7keqyftf3c98ut"); // 	    default:
UNSUPPORTED("c8if0ggdrakzyxyn4fwlc8z2j"); // 		p.y = ctr.y;
UNSUPPORTED("30qndpdx39k6rmlgid0k16w53"); // 		constrain = 0;
UNSUPPORTED("2uxoapmd0p84jvg4utlai18nj"); // 		clip = NOT(0);
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("6f2jab2h8wmbwxojrav1mbs6l"); // 	case 'w':
UNSUPPORTED("caapivv2sv3cdu0rt3fnciovd"); // 	    if (*compass)
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("3s6xpfbxpp44ou5loxsjfgzqp"); //                 if (ictxt)
UNSUPPORTED("dkdxl90pni5x4m9rsi9l4fkml"); //                     p = compassPoint(ictxt, ctr.y, -maxv);
UNSUPPORTED("c0op0grmjt3kp22s10twqy66r"); //                 else
UNSUPPORTED("e2vcgqbz5sfyjwfyadlmm3s7n"); // 		    p.x = b.LL.x;
UNSUPPORTED("4dcpup1eqdwbtlebzv22j3izb"); // 		theta = M_PI;
UNSUPPORTED("8cz8xetupjr38r0y1sfojc387"); // 		constrain = NOT(0);
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("9ni9ftlf9oep71byodeubp1sr"); // 		clip = 0;
UNSUPPORTED("3vss3xpo7q52fe0nwev7hyiyj"); // 		side = sides & (1<<3);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f187wptsr73liavtlyoyfovp3"); // 	case 'n':
UNSUPPORTED("7gucyzlkpil4bfg1hpst17k3d"); // 	    p.y = b.UR.y;
UNSUPPORTED("b4ce4svfzxg7z2fd1fquu4qmu"); // 	    constrain = NOT(0);
UNSUPPORTED("c3sati4oun39vfql5zx4ru6z"); // 	    clip = 0;
UNSUPPORTED("bzgzeuh7ihvw4c4eysc3dgpzd"); // 	    switch (*compass) {
UNSUPPORTED("6qkxsufvygbc44eq3d6xorgsx"); // 	    case '\0':
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("5w6ijz1qm65stfcc659o09osm"); // 		theta = M_PI * 0.5;
UNSUPPORTED("3s6xpfbxpp44ou5loxsjfgzqp"); //                 if (ictxt)
UNSUPPORTED("6l60lhko2eg8jry5mf4wpknho"); //                     p = compassPoint(ictxt, maxv, ctr.x);
UNSUPPORTED("c0op0grmjt3kp22s10twqy66r"); //                 else
UNSUPPORTED("3ly2ywet0m7qfki5yj2nz9t87"); //                     p.x = ctr.x;
UNSUPPORTED("8cnew4tzzej0j68sucbir8orn"); // 		side = sides & (1<<2);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("mnoev43mr2cyh3zj9yiupqzy"); // 	    case 'e':
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("dpfvfzmxj8yxv0s9b2jrvy1dt"); // 		theta = M_PI * 0.25;
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("eaiok8sr9qt2m9t35bj1n33vk"); // 		    p = compassPoint(ictxt, maxv, maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("5f4jye7znkk6hbv6lv0l9l0hs"); // 		    p.x = b.UR.x;
UNSUPPORTED("7eo9yj1faco0zq3n56ljnckjl"); // 		side = sides & ((1<<2) | (1<<1));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("xzkwmazk8r4ms7xbr46l9xyq"); // 	    case 'w':
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("b4rydjq1y842ljagzj3esvilf"); // 		theta = M_PI * 0.75;
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("aftpsq12rdaiypy81n10uki6g"); // 		    p = compassPoint(ictxt, maxv, -maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("e2vcgqbz5sfyjwfyadlmm3s7n"); // 		    p.x = b.LL.x;
UNSUPPORTED("46gsms8looi57wty5vza2s5el"); // 		side = sides & ((1<<2) | (1<<3));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("bt2g0yhsy3c7keqyftf3c98ut"); // 	    default:
UNSUPPORTED("c8if0ggdrakzyxyn4fwlc8z2j"); // 		p.y = ctr.y;
UNSUPPORTED("30qndpdx39k6rmlgid0k16w53"); // 		constrain = 0;
UNSUPPORTED("2uxoapmd0p84jvg4utlai18nj"); // 		clip = NOT(0);
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8yhowesoplp2da595z8jyojxi"); // 	case '_':
UNSUPPORTED("f5fa3usseyo7pci5g560azmea"); // 	    dyna = NOT(0);
UNSUPPORTED("czfzlng8hat0mzzbxb59g5eix"); // 	    side = sides;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f3lyz2cejs6yn5fyckhn7ba1"); // 	case 'c':
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("eldwzm3uyfs9zu1roub5a2uqi"); // 	    rv = 1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3y0o9dwc6l3w452ce3a06atj4"); //     p = cwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("bg899fz3cixwumeyw39ytk9ky"); //     if (dyna)
UNSUPPORTED("c3yy1egy8x5xhv1n80wf30bsp"); // 	pp->side = side;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("fbhfdhcz7hf8d057jq31086q"); // 	pp->side = invflip_side(side, GD_rankdir(agraphof(n)));
UNSUPPORTED("cx3gmg51rc8so3a5bx6cltryq"); //     pp->bp = bp;
UNSUPPORTED("epsoufavu8hg3cbvo2ejmmrx0"); //     PF2P(p, pp->p);
UNSUPPORTED("ami97j8il2yf1ggrixwlm2sga"); //     pp->theta = invflip_angle(theta, GD_rankdir(agraphof(n)));
UNSUPPORTED("12w9ecxxght79o36ovi8zwdz5"); //     if ((p.x == 0) && (p.y == 0))
UNSUPPORTED("ckgevzxmtfz2xxsz34wg7zura"); // 	pp->order = 256 / 2;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("eq22ufb5r5rfpsjxc3gie6aon"); // 	/* compute angle with 0 at north pole, increasing CCW */
UNSUPPORTED("aqoi5i20orce7l827lbn5rpfs"); // 	double angle = atan2(p.y, p.x) + 1.5 * M_PI;
UNSUPPORTED("dfb9u6ghdbpwnspizdx8mrdz3"); // 	if (angle >= 2 * M_PI)
UNSUPPORTED("jxrmr2dbqvl6zf1pboxh2hbn"); // 	    angle -= 2 * M_PI;
UNSUPPORTED("7wk803jipbaan1qcm89gw97t0"); // 	pp->order = (int) ((256 * angle) / (2 * M_PI));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("956p2hx4hma7on3bk77kr5mjg"); //     pp->constrained = constrain;
UNSUPPORTED("docrlijfgalt2ubkbd5x1aj0g"); //     pp->defined = defined;
UNSUPPORTED("bmz5fuwc9jp1t2yghe0gppn11"); //     pp->clip = clip;
UNSUPPORTED("2vslebofw1l11qjpgy2bu0q0g"); //     pp->dyna = dyna;
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5k2b9gfpwm2tj3zmzniuz9azt
// static port poly_port(node_t * n, char *portname, char *compass) 
public static ST_port poly_port(ST_Agnode_s n, CString portname, CString compass) {
// WARNING!! STRUCT
return poly_port_w_(n, portname, compass).copy();
}
private static ST_port poly_port_w_(ST_Agnode_s n, CString portname, CString compass) {
ENTERING("5k2b9gfpwm2tj3zmzniuz9azt","poly_port");
try {
    final ST_port rv= new ST_port();
    ST_boxf bp;
    int sides;			/* bitmap of which sides the port lies along */
    if (portname.charAt(0) == '\0')
	return Z.z().Center.copy();
UNSUPPORTED("cm99rhftfe8nq2suzac5fwbgp"); //     if (compass == NULL)
UNSUPPORTED("238a13tlawcw3bixwliz859y5"); // 	compass = "_";
UNSUPPORTED("ci2ge3idao9rokpvacvcspaxl"); //     sides = (1<<0) | (1<<1) | (1<<2) | (1<<3);
UNSUPPORTED("p7u7ou2qrodeed98v2l4kt16"); //     if (((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->label)->html) && (bp = html_port(n, portname, &sides))) {
UNSUPPORTED("dl6n43wu7irkeiaxb6wed3388"); // 	if (compassPort(n, bp, &rv, compass, sides, NULL)) {
UNSUPPORTED("cw5grwj6gbj94jcztvnp2ooyj"); // 	    agerr(AGWARN,
UNSUPPORTED("en2xpqtprfng8gmc77dzq7klv"); // 		  "node %s, port %s, unrecognized compass point '%s' - ignored\n",
UNSUPPORTED("cmo03yl2q1wgn0c1r45y1ay5e"); // 		  agnameof(n), portname, compass);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("dthh3e4ncmo6w9ycaz15zau5"); // 	inside_t *ictxtp;
UNSUPPORTED("2txee0yz6un4eoxopikuvmodk"); // 	inside_t ictxt;
UNSUPPORTED("ffnmnsqi4jd54ewadswjs4c3"); // 	if (((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->polygon == &p_box))
UNSUPPORTED("caeppa7hx6uy3mtmkxxe509c9"); // 	    ictxtp = NULL;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("17pbmb7rfq2rdapm13ww6pefz"); // 	    ictxt.s.n = n;
UNSUPPORTED("etss3zom716xdeasxnytjb8db"); // 	    ictxt.s.bp = NULL;
UNSUPPORTED("89cj6b362bd80f627mp67yjh0"); // 	    ictxtp = &ictxt;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5dzg4u1k50dmwpfqatxykvula"); // 	if (compassPort(n, NULL, &rv, portname, sides, ictxtp))
UNSUPPORTED("9xmgpfnye0xzd72aptv8i0cgl"); // 	    unrecognized(n, portname);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("5k2b9gfpwm2tj3zmzniuz9azt","poly_port");
}
}




//3 1tks71z165dy9pzfshnjejpx3
// static void poly_gencode(GVJ_t * job, node_t * n) 
public static Object poly_gencode(Object... arg) {
UNSUPPORTED("p0x21cs921921juch0sv0bun"); // static void poly_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("7b0667dpeiekddi69gpywx92t"); //     polygon_t *poly;
UNSUPPORTED("behzd4x7hwrpj60ld9ydd6ldw"); //     double xsize, ysize;
UNSUPPORTED("avlmoeaaigyvssingomxrvja4"); //     int i, j, peripheries, sides, style;
UNSUPPORTED("1r39xvspssd187ru2ru0hw25i"); //     pointf P, *vertices;
UNSUPPORTED("behdcj4jfqh2lxeud7bvr9dxx"); //     static pointf *AF;
UNSUPPORTED("922k2c5xjbw7vuw4vfhavkll9"); //     static int A_size;
UNSUPPORTED("e26zsspincyfi747lhus7h41b"); //     boolean filled;
UNSUPPORTED("343gvjl2hbvjb2nrrtcqqetep"); //     boolean usershape_p;
UNSUPPORTED("55zxkmqgt42k3bgw1g1del41"); //     boolean pfilled;		/* true if fill not handled by user shape */
UNSUPPORTED("b80uijjl4g1zjdox5s5vdh8s5"); //     char *color, *name;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = (obj->url || obj->explicit_tooltip);
UNSUPPORTED("7421ua6zgvtho3nwdlh9ypytf"); //     char* fillcolor=NULL;
UNSUPPORTED("39txqf5jgyh1q10jekeaemag6"); //     char* pencolor=NULL;
UNSUPPORTED("bhtcyodd9jiazat6sqhp9pm4x"); //     char* clrs[2];
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("e8a863hfpkzgw2w09pemrprir"); //     poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("44eync2gzhkt36aljp0pdxlws"); //     vertices = poly->vertices;
UNSUPPORTED("bt0ymhl3qyi2wkx6awwozl8pm"); //     sides = poly->sides;
UNSUPPORTED("axi5xtmkixooa3vai8uysr8y1"); //     peripheries = poly->peripheries;
UNSUPPORTED("3yzb2exxpwntmjik61bia8qin"); //     if (A_size < sides) {
UNSUPPORTED("6czsf4ed6c2x6qn10dz9vvpc2"); // 	A_size = sides + 5;
UNSUPPORTED("4fxnv89xcha2g2jkqjznbfhtl"); // 	AF = ALLOC(A_size, AF, pointf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("65psnpx1lm1txgz684nsf5fy0"); //     /* nominal label position in the center of the node */
UNSUPPORTED("1bslo0pyyucx0zmdzt12sei6d"); //     ND_label(n)->pos = ND_coord(n);
UNSUPPORTED("8pq7sdzx1tcm5jiy7gk6k14ru"); //     xsize = (ND_lw(n) + ND_rw(n)) / (ROUND((ND_width(n))*72));
UNSUPPORTED("ebgzy2lbfiijt1acuci7zobbz"); //     ysize = ND_ht(n) / (ROUND((ND_height(n))*72));
UNSUPPORTED("6yjfupcwvts03fbmr493ea2ja"); //     style = stylenode(job, n);
UNSUPPORTED("92hvfvrwzs8dy1vdgk97mu8rm"); //     clrs[0] = NULL;
UNSUPPORTED("e5t9x8qxknm67g2irjuq09m0n"); //     if (ND_gui_state(n) & (1<<0)) {
UNSUPPORTED("bmfnw21ksvzdvbf1k6jhpy482"); // 	pencolor = late_nnstring(n, N_activepencolor, "#808080");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("3kou17p4mmlejrgnb4ubal4y0"); // 	    late_nnstring(n, N_activefillcolor, "#fcfcfc");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("9ihvjyvhnzzz36yb9vxt7ds0x"); //     } else if (ND_gui_state(n) & (1<<1)) {
UNSUPPORTED("aak3ib1vf3cr00erxujx1x1a2"); // 	pencolor =
UNSUPPORTED("1cimazkiwwo2m0abp23m3fnme"); // 	    late_nnstring(n, N_selectedpencolor, "#303030");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("28yl28qxl17kdj778ikor38xk"); // 	    late_nnstring(n, N_selectedfillcolor,
UNSUPPORTED("47h1lk49r1o0z3cv330dq6dx"); // 			  "#e8e8e8");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("1yfjih723r7l1aal6cgysntu9"); //     } else if (ND_gui_state(n) & (1<<3)) {
UNSUPPORTED("aak3ib1vf3cr00erxujx1x1a2"); // 	pencolor =
UNSUPPORTED("7ksdqin8o1wm9jzsj3vquwpn4"); // 	    late_nnstring(n, N_deletedpencolor, "#e0e0e0");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("bt3kkty4bxox77ydiwjgsxvdl"); // 	    late_nnstring(n, N_deletedfillcolor, "#f0f0f0");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("8zwfuofs5l5a6z3f4rvlihyw2"); //     } else if (ND_gui_state(n) & (1<<2)) {
UNSUPPORTED("aak3ib1vf3cr00erxujx1x1a2"); // 	pencolor =
UNSUPPORTED("ctvdbytqgb1rzge7ij5ocomx9"); // 	    late_nnstring(n, N_visitedpencolor, "#101010");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("2ffts5ygp2gvce89s4zmac21o"); // 	    late_nnstring(n, N_visitedfillcolor, "#f8f8f8");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("71lsnu3rvb8q4qjlg8ekkueb8"); // 	if (style & (1 << 0)) {
UNSUPPORTED("1ldzvmymblz8y4a6idvyxoj5t"); // 	    float frac;
UNSUPPORTED("e039lb3amkbtia1p5xid53g8f"); // 	    fillcolor = findFill (n);
UNSUPPORTED("5dnga3gh00f4sv4fk1n2iqdgu"); // 	    if (findStopColor (fillcolor, clrs, &frac)) {
UNSUPPORTED("12wjuz2zq45txyp39hhco78xu"); //         	gvrender_set_fillcolor(job, clrs[0]);
UNSUPPORTED("5o23oun5dlazsaicyjj530pp"); // 		if (clrs[1]) 
UNSUPPORTED("ct9w73vq2t9wsony60rgp0vuv"); // 		    gvrender_set_gradient_vals(job,clrs[1],late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("5v31mz0fdr0su096gqov41vyn"); // 		else 
UNSUPPORTED("5hcjieyymox6ih0mqxtesfkai"); // 		    gvrender_set_gradient_vals(job,"black",late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("cu80xxb02iidme5bgb4b9q03o"); // 		if (style & (1 << 1))
UNSUPPORTED("5jf506rwz9snq4d6ozpjvg3yg"); // 		    filled = 3;
UNSUPPORTED("7rknc7r0egcn3cw68mrvgow3v"); // 	 	else
UNSUPPORTED("7bikp52v1ey2yil3rybx6nris"); // 		    filled = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("es2lu1zhy5wdeml1v1kmrcix3"); //         	gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("6w06em6l23suofe15du0wq9hb"); // 		filled = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cbyq6e4yotsw91ihtsbpqk9n"); // 	else if (style & ((1 << 6)|(1 << 9)))  {
UNSUPPORTED("e039lb3amkbtia1p5xid53g8f"); // 	    fillcolor = findFill (n);
UNSUPPORTED("b39ijeotj91epdulx0zfawqg7"); //             /* gvrender_set_fillcolor(job, fillcolor); */
UNSUPPORTED("5op945vn3c1cyxwov5p8rj33t"); // 	    filled = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("6hyckgrxm2nsg8cw4hffomldu"); // 	    filled = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7v8vwyf8talmtwk6o9fv16cu7"); // 	pencolor = penColor(job, n);	/* emit pen color */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dzz4jp9gamcvlyn9e3vzfb9m5"); //     pfilled = !ND_shape(n)->usershape || (*(ND_shape(n)->name)==*("custom")&&!strcmp(ND_shape(n)->name,"custom"));
UNSUPPORTED("867znru6ot29tjqobp8dlbw6z"); //     /* if no boundary but filled, set boundary color to transparent */
UNSUPPORTED("42p7y58vqzgaceefog269961h"); //     if ((peripheries == 0) && filled && pfilled) {
UNSUPPORTED("15ha366z6aj0vmrwy4kws0mqd"); // 	peripheries = 1;
UNSUPPORTED("9h0jwzscq5xyee6v8y9a84z5z"); // 	gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b5y5lqlrrc44k9t418m98208o"); //     /* draw peripheries first */
UNSUPPORTED("3ldxhnwdjmonz5bmmr7t8i5v6"); //     for (j = 0; j < peripheries; j++) {
UNSUPPORTED("bnlcutimilujroygrsjpbamec"); // 	for (i = 0; i < sides; i++) {
UNSUPPORTED("6jkqzav2wqsdxuy5nalny0l8v"); // 	    P = vertices[i + j * sides];
UNSUPPORTED("7cdu1dtqyaubntomiasv9qnoj"); // 	    AF[i].x = P.x * xsize + ND_coord(n).x;
UNSUPPORTED("e40xvnbar4dmi82ewqw5laa59"); // 	    AF[i].y = P.y * ysize + ND_coord(n).y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9dpfyah7h8cjesbm1tagc3qr2"); // 	if (sides <= 2) {
UNSUPPORTED("4iafj5ab7zhphfv75axr98xpm"); // 	    if ((style & (1 << 9)) && (j == 0) && (strchr(fillcolor,':'))) {
UNSUPPORTED("brwfdh2hmhcwxahcpjocmax54"); // 		int rv = wedgedEllipse (job, AF, fillcolor);
UNSUPPORTED("4195dkkxygfup9x2hevx5t0kt"); // 		if (rv > 1)
UNSUPPORTED("6d80sdeoci13p59wizsvnilpd"); // 		    agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("3zx9cyeiqls2js359g1ja8px8"); // 		filled = 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dzwn73f4njl5hkp0qrnncl2ff"); // 	    gvrender_ellipse(job, AF, sides, filled);
UNSUPPORTED("chb5tdwhi8a8xmy8ftheo6824"); // 	    if (style & (1 << 3)) {
UNSUPPORTED("efwhq15vj62j7hdj6evx064cg"); // 		Mcircle_hack(job, n);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("aci5r7yyn8mzrv3exe7znstcn"); // 	} else if (style & (1 << 6)) {
UNSUPPORTED("dgwuupvm0kjmgthk4ugim8woz"); // 	    if (j == 0) {
UNSUPPORTED("3x4ndf7fx76diabv9nfllk0b5"); // 		int rv = stripedBox (job, AF, fillcolor, 1);
UNSUPPORTED("4195dkkxygfup9x2hevx5t0kt"); // 		if (rv > 1)
UNSUPPORTED("6d80sdeoci13p59wizsvnilpd"); // 		    agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("717qv74rlec63ys8natmmpak9"); // 	    gvrender_polygon(job, AF, sides, 0);
UNSUPPORTED("5ueys9z3ukkzz7o4fr6z8tuk0"); // 	} else if (style & (1 << 10)) {
UNSUPPORTED("8ozii45lu97yd30cta30grmf8"); // 	    gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("oe3tziy2rg7shg7dan61ilfq"); // 	    gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("9cgcmdbt8qdrnqnvs86u9cd53"); // 	    gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("fft8g5x7554aunjp9t27mqx6"); // 	    gvrender_polyline(job, AF+2, 2);
UNSUPPORTED("8t4w6b2lracu2ee6rqqm6r915"); // 	} else if (((style) & ((1 << 2) | (1 << 3) | (127 << 24)))) {
UNSUPPORTED("858fovk41ca06eamq91gjw7tm"); // 	    round_corners(job, AF, sides, style, filled);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("oe3tziy2rg7shg7dan61ilfq"); // 	    gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1peuavyjb0rqm2z4fzpf2afzm"); // 	/* fill innermost periphery only */
UNSUPPORTED("arpfq2ay8oyluwsz8s1wp6tp4"); // 	filled = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("76vwep8b6qm5azc0fy66d98rw"); //     usershape_p = 0;
UNSUPPORTED("8fhwzyrc8mh95ap0b1g7e9nbq"); //     if (ND_shape(n)->usershape) {
UNSUPPORTED("2v9mlb5rtcmwqpcth7w27clk5"); // 	name = ND_shape(n)->name;
UNSUPPORTED("ad1u0yih0rcookfy0x1lsev4d"); // 	if ((*(name)==*("custom")&&!strcmp(name,"custom"))) {
UNSUPPORTED("7eg6kesbmod5ryqil85qa0nhh"); // 	    if ((name = agget(n, "shapefile")) && name[0])
UNSUPPORTED("avdrph3m5jvu0m9cldtioxy3f"); // 		usershape_p = NOT(0);
UNSUPPORTED("6to1esmb8qfrhzgtr7jdqleja"); // 	} else
UNSUPPORTED("cmpu4v9yae7spgt5x9vvwycqu"); // 	    usershape_p = NOT(0);
UNSUPPORTED("5i5g01dslsnkth7in6u6rbi99"); //     } else if ((name = agget(n, "image")) && name[0]) {
UNSUPPORTED("e220s4b8iyyeqjgxmlg5pcdrj"); // 	usershape_p = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3btjgija7cfs4jgzrv91fjwpt"); //     if (usershape_p) {
UNSUPPORTED("9usktstdf8lawthhtrs6s58pm"); // 	/* get coords of innermost periphery */
UNSUPPORTED("bnlcutimilujroygrsjpbamec"); // 	for (i = 0; i < sides; i++) {
UNSUPPORTED("5dznk69haxedww8ugav5ykrld"); // 	    P = vertices[i];
UNSUPPORTED("7cdu1dtqyaubntomiasv9qnoj"); // 	    AF[i].x = P.x * xsize + ND_coord(n).x;
UNSUPPORTED("e40xvnbar4dmi82ewqw5laa59"); // 	    AF[i].y = P.y * ysize + ND_coord(n).y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1xu7you106p030clm45rzsrgc"); // 	/* lay down fill first */
UNSUPPORTED("3sznkjp2q6eryoqsuxyw523pa"); // 	if (filled && pfilled) {
UNSUPPORTED("dur5g2omz2d8j499p5rr99e0g"); // 	    if (sides <= 2) {
UNSUPPORTED("ezipi4mltlppyq0tetpgbb2rn"); // 		if ((style & (1 << 9)) && (j == 0) && (strchr(fillcolor,':'))) {
UNSUPPORTED("an9w62svq9d61trsclgublxs4"); // 		    int rv = wedgedEllipse (job, AF, fillcolor);
UNSUPPORTED("4njt8ngwdhm5t0qj38vd4vx26"); // 		    if (rv > 1)
UNSUPPORTED("dimjpscq5rjb3aaiz8l8gia45"); // 			agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("14k7t5gy5xvy3m6y4lllccbyz"); // 		    filled = 0;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("4ey5tpwqvnf3eih51z1ar6wgg"); // 		gvrender_ellipse(job, AF, sides, filled);
UNSUPPORTED("7eygavzyy3od5lurlb1kyvq4q"); // 		if (style & (1 << 3)) {
UNSUPPORTED("53tsr41edfe2tdmq1vs4qmoh6"); // 		    Mcircle_hack(job, n);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("79b2w9yvj1qj97vqjuf6ff9w0"); // 	    } else if (style & (1 << 6)) {
UNSUPPORTED("3x4ndf7fx76diabv9nfllk0b5"); // 		int rv = stripedBox (job, AF, fillcolor, 1);
UNSUPPORTED("4195dkkxygfup9x2hevx5t0kt"); // 		if (rv > 1)
UNSUPPORTED("6d80sdeoci13p59wizsvnilpd"); // 		    agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("ctx2lp124btfhy4z6030o2gs"); // 		gvrender_polygon(job, AF, sides, 0);
UNSUPPORTED("89clftmmkfws4k288i4jas2yb"); // 	    } else if (style & ((1 << 2) | (1 << 3))) {
UNSUPPORTED("dk9vlsyutilnikpal5kjamo5x"); // 		round_corners(job, AF, sides, style, filled);
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("azv3esl3n2c27ol5b9dgx7yrz"); // 		gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8bp2tc15gonvz3x38es3dcmqm"); // 	gvrender_usershape(job, name, AF, sides, filled,
UNSUPPORTED("4ob0y29flbn0mu1b6or1pikm"); // 			   late_string(n, N_imagescale, "false"));
UNSUPPORTED("cyozk4ozoaaqkwqvcr0wuavfb"); // 	filled = 0;		/* with user shapes, we have done the fill if needed */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("59de9ohjmjuxis5h2yvc2zjnx"); //     free (clrs[0]);
UNSUPPORTED("8r8t0lgzzpigm1odig9a9yg1c"); //     emit_label(job, EMIT_NLABEL, ND_label(n));
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4jw571mqhnyucuj7xrwi1h9o5
// static void point_init(node_t * n) 
public static Object point_init(Object... arg) {
UNSUPPORTED("9teib8zo1wrsipfc5j66cuyuy"); // static void point_init(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5r323abe0z0jo5p5exij6a98a"); //     polygon_t *poly = (polygon_t*)zmalloc(sizeof(polygon_t));
UNSUPPORTED("6s51isafyf01shjhyf1xybd0g"); //     int sides, outp, peripheries = ND_shape(n)->polygon->peripheries;
UNSUPPORTED("483po9irjcq4khru9r63df3hc"); //     double sz;
UNSUPPORTED("1r39xvspssd187ru2ru0hw25i"); //     pointf P, *vertices;
UNSUPPORTED("dzpsknrwv8qkqq20hjnjpjn68"); //     int i, j;
UNSUPPORTED("cldfniz1yu2rzamoi5a3tri2q"); //     double w, h;
UNSUPPORTED("2k44yvy3ib1xjsbz4aoziggm6"); //     /* set width and height, and make them equal
UNSUPPORTED("2cp3uyfw5ix1x35noly2mr8xl"); //      * if user has set weight or height, use it.
UNSUPPORTED("bnvp9gvvkfk14zw1xx1fol30y"); //      * if both are set, use smallest.
UNSUPPORTED("e7wh555e1kr9ygs6v77uuxuek"); //      * if neither, use default
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("shqz46obs8iqqm2ty42thbuz"); //     w = late_double(n, N_width, MAXDOUBLE, 0.0);
UNSUPPORTED("4j74z7hdszhifo9kh4hh1wofy"); //     h = late_double(n, N_height, MAXDOUBLE, 0.0);
UNSUPPORTED("8fn9glr373p3sq9l96u8rshi5"); //     w = MIN(w, h);
UNSUPPORTED("ebrgob1ni20nxqse89rw5hipv"); //     if ((w == MAXDOUBLE) && (h == MAXDOUBLE))	/* neither defined */
UNSUPPORTED("ax36ksj200y93pvt2rjxvnupc"); // 	ND_width(n) = ND_height(n) = 0.05;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("42uohvzx3qdjcdkup3wcj6s6x"); // 	w = MIN(w, h);
UNSUPPORTED("1km16exp8dsurkq6pd730gw3v"); // 	/* If w == 0, use it; otherwise, make w no less than MIN_POINT due
UNSUPPORTED("3ilg8ohbfo44z8h3fnuc0560h"); //          * to the restrictions mentioned above.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("7lyxf0b1aikm6fzbqhbr8k9oa"); // 	if (w > 0.0) 
UNSUPPORTED("dsnyzjlf33z6dnm3ptthdw0y3"); // 	    w = MAX(w,0.0003);
UNSUPPORTED("2fz0g7sw4drprgpggwdns197i"); // 	ND_width(n) = ND_height(n) = w;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("57ect78x8mvtomzg0tyodv7g"); //     sz = ND_width(n) * 72;
UNSUPPORTED("6s2rmv5dvc38plrfxn2iknbec"); //     peripheries = late_int(n, N_peripheries, peripheries, 0);
UNSUPPORTED("d8z7y5n3mm6modrbwu1y08k80"); //     if (peripheries < 1)
UNSUPPORTED("5j2roihkrnhpbzz7w0qxrt1tb"); // 	outp = 1;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("dt4js26tpr1dccw0n8attbsqv"); // 	outp = peripheries;
UNSUPPORTED("8o3qj0u55iaq7q7uz9bp2btmt"); //     sides = 2;
UNSUPPORTED("e6zmcz1u6vh44zg5bdfazji16"); //     vertices = (pointf*)zmalloc((outp * sides)*sizeof(pointf));
UNSUPPORTED("wog9mbijcqexxahwely1klv"); //     P.y = P.x = sz / 2.;
UNSUPPORTED("401agb4oue2c5jj69la268pcx"); //     vertices[0].x = -P.x;
UNSUPPORTED("877hteaqk6t4p1f5at5526w7s"); //     vertices[0].y = -P.y;
UNSUPPORTED("cjcbbgirujkoa3jg96de4tn4n"); //     vertices[1] = P;
UNSUPPORTED("e3yma9eluyvh0pqkjh60cdtds"); //     if (peripheries > 1) {
UNSUPPORTED("9opkd1w4nqz4bwppgnj4nh97x"); // 	for (j = 1, i = 2; j < peripheries; j++) {
UNSUPPORTED("460zn3qymugfa5w8867gevz9q"); // 	    P.x += 4;
UNSUPPORTED("esxff06psrm7h7ol9ohlnzcpi"); // 	    P.y += 4;
UNSUPPORTED("6iwi6enjzo7fz7swxeospzgor"); // 	    vertices[i].x = -P.x;
UNSUPPORTED("7s0s5pcvrfbi2igyjxkzrm6q3"); // 	    vertices[i].y = -P.y;
UNSUPPORTED("1lo0ackow66iudrq1gb15y3ry"); // 	    i++;
UNSUPPORTED("d1mf936qm1rffrj0h5t3x94ng"); // 	    vertices[i].x = P.x;
UNSUPPORTED("eagc6fu5xvv54q8ct60z25dnr"); // 	    vertices[i].y = P.y;
UNSUPPORTED("1lo0ackow66iudrq1gb15y3ry"); // 	    i++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2gqt5o1ww7zscvmqs2er3asq5"); // 	sz = 2. * P.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dcm4c8vrwq4xjdw6mnk7jh45a"); //     poly->regular = 1;
UNSUPPORTED("27nmn21035p035qyz0wzee98a"); //     poly->peripheries = peripheries;
UNSUPPORTED("auxb6kzl2meihrn96iopky8b2"); //     poly->sides = 2;
UNSUPPORTED("38u6c2cd5dc1o1vmpzv5kfhok"); //     poly->orientation = 0;
UNSUPPORTED("1rjh9t0o5cpm795hqnsjkz026"); //     poly->skew = 0;
UNSUPPORTED("2196taxexoskznznmkkvqvfws"); //     poly->distortion = 0;
UNSUPPORTED("5lucyy48dtz44fkdhcjrphkjz"); //     poly->vertices = vertices;
UNSUPPORTED("16k7ddcfff7zeto50vvf4pn7e"); //     ND_height(n) = ND_width(n) = ((sz)/(double)72);
UNSUPPORTED("43hsn9iilhppt0qo3od9c7rnq"); //     ND_shape_info(n) = (void *) poly;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 34yxvhscic4iigcqveaac58ue
// static boolean point_inside(inside_t * inside_context, pointf p) 
public static Object point_inside(Object... arg) {
UNSUPPORTED("96ogc5j6ek5rdyjpr6kp1yocz"); // static boolean point_inside(inside_t * inside_context, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ogz1m3q9xn7z7hiecjp98bmt"); //     static node_t *lastn;	/* last node argument */
UNSUPPORTED("5l35lijc0ciwjv4q6hr6uiiwb"); //     static double radius;
UNSUPPORTED("7lh87lvufqsd73q9difg0omei"); //     pointf P;
UNSUPPORTED("d8oppi8gt9b4eaonkdgb7a54l"); //     node_t *n = inside_context->s.n;
UNSUPPORTED("823iiqtx9pt0gijqrohrd3zx7"); //     P = ccwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("8rl2cn4oxr94675yld5eohkie"); //     if (n != lastn) {
UNSUPPORTED("b3vl4n360t74xga3kf8kpy77j"); // 	int outp;
UNSUPPORTED("ekuhffxeyunmrbugeqeo525ww"); // 	polygon_t *poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("d41xba93s17axh19qsbhg0x8a"); // 	/* index to outer-periphery */
UNSUPPORTED("8qv90di846fa1tngzoa71d029"); // 	outp = 2 * (poly->peripheries - 1);
UNSUPPORTED("47l17pa0edzmfnlr8ysqs0qh4"); // 	if (outp < 0)
UNSUPPORTED("jyf75douzxhfzxfyrq3kes6e"); // 	    outp = 0;
UNSUPPORTED("4dly7702wk6tpv9taxmfj31og"); // 	radius = poly->vertices[outp + 1].x;
UNSUPPORTED("dz5401vppes7iz7b0c6pzkge6"); // 	lastn = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c0dgd06mnotxt3zonxv8qfpsv"); //     /* inside bounding box? */
UNSUPPORTED("56idyrf96f39b4a9qfa4aaoar"); //     if ((fabs(P.x) > radius) || (fabs(P.y) > radius))
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("1nxamo9gj6oech5spo5v5a16q"); //     return (hypot(P.x, P.y) <= radius);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9nmafjnpqf5021bgj7xic6bcv
// static void point_gencode(GVJ_t * job, node_t * n) 
public static Object point_gencode(Object... arg) {
UNSUPPORTED("c8zl3q8hj4ggg44eavpqwpcz6"); // static void point_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("7b0667dpeiekddi69gpywx92t"); //     polygon_t *poly;
UNSUPPORTED("6smidc9cn5hmnzglv2o3ha87a"); //     int i, j, sides, peripheries, style;
UNSUPPORTED("1r39xvspssd187ru2ru0hw25i"); //     pointf P, *vertices;
UNSUPPORTED("behdcj4jfqh2lxeud7bvr9dxx"); //     static pointf *AF;
UNSUPPORTED("922k2c5xjbw7vuw4vfhavkll9"); //     static int A_size;
UNSUPPORTED("e26zsspincyfi747lhus7h41b"); //     boolean filled;
UNSUPPORTED("5zltq70xm6o2q24ddyqe6noyn"); //     char *color;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = (obj->url || obj->explicit_tooltip);
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("e8a863hfpkzgw2w09pemrprir"); //     poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("44eync2gzhkt36aljp0pdxlws"); //     vertices = poly->vertices;
UNSUPPORTED("bt0ymhl3qyi2wkx6awwozl8pm"); //     sides = poly->sides;
UNSUPPORTED("axi5xtmkixooa3vai8uysr8y1"); //     peripheries = poly->peripheries;
UNSUPPORTED("3yzb2exxpwntmjik61bia8qin"); //     if (A_size < sides) {
UNSUPPORTED("cbz0nkwec20m1ib1g01fqvu61"); // 	A_size = sides + 2;
UNSUPPORTED("4fxnv89xcha2g2jkqjznbfhtl"); // 	AF = ALLOC(A_size, AF, pointf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ba3rlm9loi447hvq9xargrfj5"); //     checkStyle(n, &style);
UNSUPPORTED("c9p9i9kp0tu95s7r5pe6r726t"); //     if (style & (1 << 5))
UNSUPPORTED("3t8beazmvyshgjao810hjqmie"); // 	gvrender_set_style(job, point_style);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("74zb1tnkmdx8ynmv7c1olqg7e"); // 	gvrender_set_style(job, &point_style[1]);
UNSUPPORTED("e5t9x8qxknm67g2irjuq09m0n"); //     if (ND_gui_state(n) & (1<<0)) {
UNSUPPORTED("494wbiv03tk48fokerl16r6f4"); // 	color = late_nnstring(n, N_activepencolor, "#808080");
UNSUPPORTED("9e08s11lexyxn6kite8m0sfty"); // 	gvrender_set_pencolor(job, color);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("3kou17p4mmlejrgnb4ubal4y0"); // 	    late_nnstring(n, N_activefillcolor, "#fcfcfc");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("9ihvjyvhnzzz36yb9vxt7ds0x"); //     } else if (ND_gui_state(n) & (1<<1)) {
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("1cimazkiwwo2m0abp23m3fnme"); // 	    late_nnstring(n, N_selectedpencolor, "#303030");
UNSUPPORTED("9e08s11lexyxn6kite8m0sfty"); // 	gvrender_set_pencolor(job, color);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("28yl28qxl17kdj778ikor38xk"); // 	    late_nnstring(n, N_selectedfillcolor,
UNSUPPORTED("47h1lk49r1o0z3cv330dq6dx"); // 			  "#e8e8e8");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("1yfjih723r7l1aal6cgysntu9"); //     } else if (ND_gui_state(n) & (1<<3)) {
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("7ksdqin8o1wm9jzsj3vquwpn4"); // 	    late_nnstring(n, N_deletedpencolor, "#e0e0e0");
UNSUPPORTED("9e08s11lexyxn6kite8m0sfty"); // 	gvrender_set_pencolor(job, color);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("bt3kkty4bxox77ydiwjgsxvdl"); // 	    late_nnstring(n, N_deletedfillcolor, "#f0f0f0");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("8zwfuofs5l5a6z3f4rvlihyw2"); //     } else if (ND_gui_state(n) & (1<<2)) {
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("ctvdbytqgb1rzge7ij5ocomx9"); // 	    late_nnstring(n, N_visitedpencolor, "#101010");
UNSUPPORTED("9e08s11lexyxn6kite8m0sfty"); // 	gvrender_set_pencolor(job, color);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("2ffts5ygp2gvce89s4zmac21o"); // 	    late_nnstring(n, N_visitedfillcolor, "#f8f8f8");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("cf8q4vce1jpdyg0g44am343kh"); // 	color = findFillDflt(n, "black");
UNSUPPORTED("552c4dm43883t0wtf5hl0yvwe"); // 	gvrender_set_fillcolor(job, color);	/* emit fill color */
UNSUPPORTED("ep2kgzcflmdfwwmtohgbczks7"); // 	penColor(job, n);	/* emit pen color */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2gwieuk9tzvsileijzb0m497g"); //     filled = NOT(0);
UNSUPPORTED("1pz5kftjxxzi9lv9u3y1o6rn0"); //     /* if no boundary but filled, set boundary color to fill color */
UNSUPPORTED("5mlpxufq3yx495mi8tzel72un"); //     if (peripheries == 0) {
UNSUPPORTED("15ha366z6aj0vmrwy4kws0mqd"); // 	peripheries = 1;
UNSUPPORTED("2kvd4jaqat0g550t2krwhucr2"); // 	if (color[0])
UNSUPPORTED("2v5mkqa2bpb5gkm53lbc3a8do"); // 	    gvrender_set_pencolor(job, color);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3ldxhnwdjmonz5bmmr7t8i5v6"); //     for (j = 0; j < peripheries; j++) {
UNSUPPORTED("bnlcutimilujroygrsjpbamec"); // 	for (i = 0; i < sides; i++) {
UNSUPPORTED("6jkqzav2wqsdxuy5nalny0l8v"); // 	    P = vertices[i + j * sides];
UNSUPPORTED("e8tnxali1kzd83o1vue986ipj"); // 	    AF[i].x = P.x + ND_coord(n).x;
UNSUPPORTED("6v07wdm0qkwc0lx20mi2w7w7h"); // 	    AF[i].y = P.y + ND_coord(n).y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("debstz8tichfsgcen5fgx33pj"); // 	gvrender_ellipse(job, AF, sides, filled);
UNSUPPORTED("1peuavyjb0rqm2z4fzpf2afzm"); // 	/* fill innermost periphery only */
UNSUPPORTED("arpfq2ay8oyluwsz8s1wp6tp4"); // 	filled = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 7tslf55o9g8v48j97pdsyich9
// static char *reclblp




//3 1dflsvfaih0mcg1gg4n23v1rg
// static void free_field(field_t * f) 
public static Object free_field(Object... arg) {
UNSUPPORTED("1w8vqjgpmm3wzxdg86sst9sna"); // static void free_field(field_t * f)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7zbyipqbl6t75m71to6vrvnmq"); //     for (i = 0; i < f->n_flds; i++) {
UNSUPPORTED("44t6o1rhsqwprcg98j3zgbzvz"); // 	free_field(f->fld[i]);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8mpeuez5fwrg7hufhlnvpzpk6"); //     free(f->id);
UNSUPPORTED("9mo450myxof5j4jin03aqpb9n"); //     free_label(f->lp);
UNSUPPORTED("6onriqqkoxktq7iqg9iiuw1zo"); //     free(f->fld);
UNSUPPORTED("a4v6veu7h0jl3a2wwlxwpdsuw"); //     free(f);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e9v6g9o3uouhdvy68a5uiisw9
// static field_t *parse_error(field_t * rv, char *port) 
public static Object parse_error(Object... arg) {
UNSUPPORTED("9bgvna5r61dzeu3tfs4uvakqi"); // static field_t *parse_error(field_t * rv, char *port)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4i2f9fbl6g816tgzjosnh9uxp"); //     free_field(rv);
UNSUPPORTED("d6w5xfrx6ivjrjr01evg54l4o"); //     if (port)
UNSUPPORTED("11a5y4khavkjgq5ubj4dyaa5p"); // 	free(port);
UNSUPPORTED("o68dp934ebg4cplebgc5hv4v"); //     return NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7zxlp1fmrq3zt4fprrtesdbg3
// static field_t *parse_reclbl(node_t * n, int LR, int flag, char *text) 
public static Object parse_reclbl(Object... arg) {
UNSUPPORTED("9v20mrghfq60f2qgsfdn1xxdw"); // static field_t *parse_reclbl(node_t * n, int LR, int flag, char *text)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ws2qs7cm9wlnskmpn5zahpwz"); //     field_t *fp, *rv = (field_t*)zmalloc(sizeof(field_t));
UNSUPPORTED("ebck8ts2eioqv6lvicwad9mj8"); //     char *tsp, *psp=NULL, *hstsp, *hspsp=NULL, *sp;
UNSUPPORTED("1tf7pz3w8sq5wk926zvu829ep"); //     char *tmpport = NULL;
UNSUPPORTED("e7a8l4oon3q1mu7hlgf4clu1b"); //     int maxf, cnt, mode, wflag, ishardspace, fi;
UNSUPPORTED("3jmp83p22dny4oi3sy4awqg1d"); //     textlabel_t *lbl = ND_label(n);
UNSUPPORTED("9igz1d93visyobl4po13vtvkx"); //     fp = NULL;
UNSUPPORTED("2amh1z5unfmpprayylcu5gr96"); //     for (maxf = 1, cnt = 0, sp = reclblp; *sp; sp++) {
UNSUPPORTED("awjvysmspiqruxxa3ttkp4hto"); // 	if (*sp == '\\') {
UNSUPPORTED("c6ujjd37nlex68cg5oii0ds8q"); // 	    sp++;
UNSUPPORTED("22vdzj6ye8zw196h8o51k5ol3"); // 	    if (*sp
UNSUPPORTED("84ppu6pbdmw18lsn0ryv4v2si"); // 		&& (*sp == '{' || *sp == '}' || *sp == '|' || *sp == '\\'))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dacur0fb8e1qtm4h9105hx3rs"); // 	if (*sp == '{')
UNSUPPORTED("7hl03wjg5yryhvbe4ar0i0b8g"); // 	    cnt++;
UNSUPPORTED("yro6chknoj2gs3h8b14wg99v"); // 	else if (*sp == '}')
UNSUPPORTED("4vzj6cjceqbghqhehc5ucl97m"); // 	    cnt--;
UNSUPPORTED("e69f7pkphb1u2seq6xkr4kaho"); // 	else if (*sp == '|' && cnt == 0)
UNSUPPORTED("8t45h7yg233z39lxb621fptcg"); // 	    maxf++;
UNSUPPORTED("2edcod5le4rwdf79wg0b9o14q"); // 	if (cnt < 0)
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4ris3hlhijr2gnuhvu238vyni"); //     rv->fld = (field_t **)zmalloc((maxf)*sizeof(field_t *));
UNSUPPORTED("avl5mval8jm4w6du3hw9sxc1e"); //     rv->LR = LR;
UNSUPPORTED("565ypt38g1szuouqlmu74ejq9"); //     mode = 0;
UNSUPPORTED("8ub4c1i56h9jmytputkewow9k"); //     fi = 0;
UNSUPPORTED("85ilec7k1i6sdrpb5u8ncfru7"); //     hstsp = tsp = text;
UNSUPPORTED("1x6ue04deldh5y980p3gm7er1"); //     wflag = NOT(0);
UNSUPPORTED("58z9686vengy8z9jzrj1jxa13"); //     ishardspace = 0;
UNSUPPORTED("4edx4759azcbmezyrc5h0tmw"); //     while (wflag) {
UNSUPPORTED("23je92fa88f4sesva9mh3fk5k"); // 	if ((*reclblp < ' ') && *reclblp) {    /* Ignore non-0 control characters */
UNSUPPORTED("8m8ph0munfeo3anrkr9n5c0o8"); // 	    reclblp++;
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cxp5sujfti7s3yzy5w1hqq6xz"); // 	switch (*reclblp) {
UNSUPPORTED("cfap8yeec19z0ucbudqen8t02"); // 	case '<':
UNSUPPORTED("bb8zo5knwust0d4cwoa87msmw"); // 	    if (mode & (4 | 2))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
UNSUPPORTED("2lft1znt6um5sewf4ta8eigdi"); // 	    if (lbl->html)
UNSUPPORTED("75bwqdnezjvhazmryfatc4819"); // 		goto dotext;
UNSUPPORTED("5z8248k3ca5miryedahc2wb60"); // 	    mode |= (2 | 16);
UNSUPPORTED("8m8ph0munfeo3anrkr9n5c0o8"); // 	    reclblp++;
UNSUPPORTED("d1omi22iukq2z2ih6p6w2zy5q"); // 	    hspsp = psp = text;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5suo7424ml3937pmao1lly7tj"); // 	case '>':
UNSUPPORTED("2lft1znt6um5sewf4ta8eigdi"); // 	    if (lbl->html)
UNSUPPORTED("75bwqdnezjvhazmryfatc4819"); // 		goto dotext;
UNSUPPORTED("e7t4hd1jo3rbmnlsnt7tbyso7"); // 	    if (!(mode & 16))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
UNSUPPORTED("5mkkaq04kcnssm3nv93a82w58"); // 	    if (psp > text + 1 && psp - 1 != hspsp && *(psp - 1) == ' ')
UNSUPPORTED("7v2hf4x5nsnlq1l025dplo0vo"); // 		psp--;
UNSUPPORTED("8qt2jpqdy5xvffbwdbwoz25od"); // 	    *psp = '\000';
UNSUPPORTED("jt6w0csqwc7g51zgiaulvv1y"); // 	    tmpport = strdup(text);
UNSUPPORTED("v0n557cij70vu46xrnalpnkf"); // 	    mode &= ~16;
UNSUPPORTED("8m8ph0munfeo3anrkr9n5c0o8"); // 	    reclblp++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("e1d1tlv81emdkqrj3h7vwega1"); // 	case '{':
UNSUPPORTED("8m8ph0munfeo3anrkr9n5c0o8"); // 	    reclblp++;
UNSUPPORTED("171fk0fom3xhcmehn269b68k0"); // 	    if (mode != 0 || !*reclblp)
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
UNSUPPORTED("4f6lpwau75tu63tu957laxmhg"); // 	    mode = 4;
UNSUPPORTED("4uchlyreqn6xrcbdv514yikv5"); // 	    if (!(rv->fld[fi++] = parse_reclbl(n, NOT(LR), 0, text)))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5yyz13ef8dtnlysqshj0o345v"); // 	case '}':
UNSUPPORTED("2miq185bwgdi03xsbuf2yo7jg"); // 	case '|':
UNSUPPORTED("dnmucnqmv1b1788zqbo1ra28z"); // 	case '\000':
UNSUPPORTED("9hqo6gl1ko0z6pxo2slbietya"); // 	    if ((!*reclblp && !flag) || (mode & 16))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
UNSUPPORTED("190gti87w167l9bzbbuqdaao4"); // 	    if (!(mode & 4))
UNSUPPORTED("l9mhl6lmaxqj7lr1vpp59qcx"); // 		fp = rv->fld[fi++] = (field_t*)zmalloc(sizeof(field_t));
UNSUPPORTED("6vtiuvpbccho5bvog9tbt6zt7"); // 	    if (tmpport) {
UNSUPPORTED("vx76i8n34hlrd473lur8wdmg"); // 		fp->id = tmpport;
UNSUPPORTED("f4uqf89q045ls6scq11ld6jzl"); // 		tmpport = NULL;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6ogkowbov04jvech8rbmf5h71"); // 	    if (!(mode & (1 | 4)))
UNSUPPORTED("eiatbbffa47bl558pxd0v9w1g"); // 		mode |= 1, *tsp++ = ' ';
UNSUPPORTED("3r0hwquezbtg597n0ud6pg0v2"); // 	    if (mode & 1) {
UNSUPPORTED("eciju51pa83rngg58rdfhhwlh"); // 		if (tsp > text + 1 &&
UNSUPPORTED("144ggmb5thn6bqdp5qsh5q542"); // 		    tsp - 1 != hstsp && *(tsp - 1) == ' ')
UNSUPPORTED("4rman9clm35ayjpnaekp2pa4c"); // 		    tsp--;
UNSUPPORTED("abhck6fmj383j2xczaz9n0hyc"); // 		*tsp = '\000';
UNSUPPORTED("b6zzmy9m0a71wiwkokxbmshtj"); // 		fp->lp =
UNSUPPORTED("281g7dyja9pt1j00mw40mvk8h"); // 		    make_label((void *) n, strdup(text),
UNSUPPORTED("dwtk6llg07gqdeogzt9phcutg"); // 			       (lbl->html ? (1 << 1) : (0 << 1)),
UNSUPPORTED("4c25jiasa72rqemdd9q0fkqfr"); // 			       lbl->fontsize, lbl->fontname,
UNSUPPORTED("esi7yfxq7b1t44gjijlfmefkx"); // 			       lbl->fontcolor);
UNSUPPORTED("13033k8vmyxsz6v6ehqkz1pjy"); // 		fp->LR = NOT(0);
UNSUPPORTED("3wxuyqcnsxu1lolpwdvh532ta"); // 		hstsp = tsp = text;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("49kjrnlxdycco3jlkx7a6uhfe"); // 	    if (*reclblp) {
UNSUPPORTED("bxo1ji3i5yxlcsgif616qyf9y"); // 		if (*reclblp == '}') {
UNSUPPORTED("dcc90zmv0256yuz6jtriktl8s"); // 		    reclblp++;
UNSUPPORTED("emd51x6hgxuinh9oga7wnkrqk"); // 		    rv->n_flds = fi;
UNSUPPORTED("7nukzdmlh4mklsedpm903o4cj"); // 		    return rv;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7dcvo4gc0ng3ctuwhrcjfrb41"); // 		mode = 0;
UNSUPPORTED("be6gxzbdtfezsd8u46xrj6xw4"); // 		reclblp++;
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("5iumhr0xqqhxv0zr03nxwhm7o"); // 		wflag = 0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("a4kmq96jzs4d007vvibjcu25v"); // 	case '\\':
UNSUPPORTED("3vnixbvvmty9ydvf0l1929gle"); // 	    if (*(reclblp + 1)) {
UNSUPPORTED("bjtxv6n9c9aqzdkik1c6cqbvy"); // 		if (((*(reclblp + 1)) == '{' || (*(reclblp + 1)) == '}' || (*(reclblp + 1)) == '|' || (*(reclblp + 1)) == '<' || (*(reclblp + 1)) == '>'))
UNSUPPORTED("dcc90zmv0256yuz6jtriktl8s"); // 		    reclblp++;
UNSUPPORTED("c8cxvsbs7ae3wdjeflwbk3z6u"); // 		else if ((*(reclblp + 1) == ' ') && !lbl->html)
UNSUPPORTED("djkriuw8khnsxfne1jal3yysz"); // 		    ishardspace = NOT(0), reclblp++;
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("2qwaphvt2yekkogtyqq0omhut"); // 		    *tsp++ = '\\';
UNSUPPORTED("63p7706g22u4h7m9yealimr3g"); // 		    mode |= (8 | 1);
UNSUPPORTED("dcc90zmv0256yuz6jtriktl8s"); // 		    reclblp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dg5yg97t3vto8m73vvwj8jnb2"); // 	    /* falling through ... */
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("2d4vmvpowhgj7h9539m0qrxsy"); // 	  dotext:
UNSUPPORTED("5jto4v3wquxhnfa1ubq7jhn9e"); // 	    if ((mode & 4) && *reclblp != ' ')
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
UNSUPPORTED("f5g7ycr0n6dxs6l70ws5qbyha"); // 	    if (!(mode & (8 | 16)) && *reclblp != ' ')
UNSUPPORTED("49k9f66mkv4qjn84gy5oo6mfz"); // 		mode |= (8 | 1);
UNSUPPORTED("4ehnscdbdyu6b96qrct40t547"); // 	    if (mode & 8) {
UNSUPPORTED("eoigvu52glsps928r04np5k12"); // 		if (!
UNSUPPORTED("9zalai2d852m4eoy15f6bpxgu"); // 		    (*reclblp == ' ' && !ishardspace && *(tsp - 1) == ' '
UNSUPPORTED("ahr3j94flu719g0gxu2i46zwn"); // 		     && !lbl->html))
UNSUPPORTED("c8qpd7i4393s9xlwk5waq8sk3"); // 		    *tsp++ = *reclblp;
UNSUPPORTED("c2bifaubj01ag8b39d60p9cbk"); // 		if (ishardspace)
UNSUPPORTED("atuokfvkmomvi6gvwvpbxggoz"); // 		    hstsp = tsp - 1;
UNSUPPORTED("blopjrsgbbips57lefdw6bg8b"); // 	    } else if (mode & 16) {
UNSUPPORTED("1eoe6a3jwmhnj7vdpgz9q6vsn"); // 		if (!(*reclblp == ' ' && !ishardspace &&
UNSUPPORTED("7i865uqx5v3rutzyppio4oztc"); // 		      (psp == text || *(psp - 1) == ' ')))
UNSUPPORTED("ccywxarqbhmcmnh8re3pv8j45"); // 		    *psp++ = *reclblp;
UNSUPPORTED("c2bifaubj01ag8b39d60p9cbk"); // 		if (ishardspace)
UNSUPPORTED("5u5h7cb6egued2g1q7w8yhb1n"); // 		    hspsp = psp - 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8m8ph0munfeo3anrkr9n5c0o8"); // 	    reclblp++;
UNSUPPORTED("eikj8pnam53jenwbu8enjrw4r"); // 	    while (*reclblp & 128)
UNSUPPORTED("86nc3qdu6nuyt7u67d0kblb9w"); // 		*tsp++ = *reclblp++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("csdg0jacrzxz21ls0053286t"); //     rv->n_flds = fi;
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dwk0rh74bwfd7mky5hg9t1epj
// static pointf size_reclbl(node_t * n, field_t * f) 
public static Object size_reclbl(Object... arg) {
UNSUPPORTED("406fn16lbi8yxgg92lsuaglae"); // static pointf size_reclbl(node_t * n, field_t * f)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("mt3ya9zh07mi1ttjb64zhd7m"); //     double marginx, marginy;
UNSUPPORTED("6ub135100vcy57x6o36uz14be"); //     pointf d, d0;
UNSUPPORTED("bgjjpl6jaaa122twwwd0vif6x"); //     pointf dimen;
UNSUPPORTED("cofpjibrqirnx1jqt3qoptoa8"); //     if (f->lp) {
UNSUPPORTED("96sfr72s46epnfb44t03vqqu2"); // 	dimen = f->lp->dimen;
UNSUPPORTED("bqi7vwb2jmnrrqcdzo1mntnvb"); // 	/* minimal whitespace around label */
UNSUPPORTED("dhtf2vwrnsx779x9mday279x5"); // 	if ((dimen.x > 0.0) || (dimen.y > 0.0)) {
UNSUPPORTED("2yzf5uec82v14ygrfjcq6ktxp"); // 	    /* padding */
UNSUPPORTED("b8i1qxc1gtg61p1c1szkj2osr"); // 	    if ((p = agget(n, "margin"))) {
UNSUPPORTED("bfyy3iw4z9ebf4m89x69tn1eb"); // 		i = sscanf(p, "%lf,%lf", &marginx, &marginy);
UNSUPPORTED("ebo7omz8ev8wu69ub10b4o890"); // 		if (i > 0) {
UNSUPPORTED("efcgckeemzkbxh32pc2qcdv0d"); // 		    dimen.x += 2 * (ROUND((marginx)*72));
UNSUPPORTED("c755n9x3n7022hjjg8hanklib"); // 		    if (i > 1)
UNSUPPORTED("2az12nq89f7txcsfmqdj8tly1"); // 			dimen.y += 2 * (ROUND((marginy)*72));
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("2az12nq89f7txcsfmqdj8tly1"); // 			dimen.y += 2 * (ROUND((marginy)*72));
UNSUPPORTED("738mi6h8ef0itznt34ngxe25o"); // 		} else
UNSUPPORTED("b12tl2a8tebl71ewuz3jms9jv"); // 		    {((dimen).x += 4*4); ((dimen).y += 2*4);};
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("87bdwkkwbzyswxnepdd9bj8mb"); // 		{((dimen).x += 4*4); ((dimen).y += 2*4);};
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2ig0j65v03nhlp8gta21y7upj"); // 	d = dimen;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("3h1vugg28z5an28yxqksy73a1"); // 	d.x = d.y = 0;
UNSUPPORTED("9cjjlq42o712cit9tby1b7l6a"); // 	for (i = 0; i < f->n_flds; i++) {
UNSUPPORTED("1dovx3mi47z2lap7bct0utmvv"); // 	    d0 = size_reclbl(n, f->fld[i]);
UNSUPPORTED("xwrnxfxcwmxuxvjpfw1sb7lt"); // 	    if (f->LR) {
UNSUPPORTED("2s4lr8xqoy2s4fcc3dy43z546"); // 		d.x += d0.x;
UNSUPPORTED("da9p29dgwktuisirb2i22mw4p"); // 		d.y = MAX(d.y, d0.y);
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("eey7x226uokoejjsvtccwizqn"); // 		d.y += d0.y;
UNSUPPORTED("cvznbvji1rgs4g8avqaznhmtl"); // 		d.x = MAX(d.x, d0.x);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("58mlt8qk1oe73v2mcc1mgl8k1"); //     f->size = d;
UNSUPPORTED("3r3o80n61nmy2jv0ezi9xg2xp"); //     return d;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 blo8etwhtlcsld8ox0vryznfw
// static void resize_reclbl(field_t * f, pointf sz, int nojustify_p) 
public static Object resize_reclbl(Object... arg) {
UNSUPPORTED("aye44rj1356dmxgwk9gx7pwh2"); // static void resize_reclbl(field_t * f, pointf sz, int nojustify_p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("71jvj7ehg7fq26a2s80qyw5pl"); //     int i, amt;
UNSUPPORTED("9rbb4p3tnymgkgm61ids5opx5"); //     double inc;
UNSUPPORTED("4vbedk7s9t1o5o9sll2mj3m4d"); //     pointf d;
UNSUPPORTED("azi9wyi9dsgwzm61ggr9st546"); //     pointf newsz;
UNSUPPORTED("1zy316mu1h3i1dwl59s1v5gtd"); //     field_t *sf;
UNSUPPORTED("4od0ewvxs3wubgnizxmaw95m6"); //     /* adjust field */
UNSUPPORTED("2sbunv882wuaeiivap21n2bqb"); //     d.x = sz.x - f->size.x;
UNSUPPORTED("57mh8iamjlos1vfnth2m4qmgb"); //     d.y = sz.y - f->size.y;
UNSUPPORTED("b6m22nmidg72njh0uo1z8sndv"); //     f->size = sz;
UNSUPPORTED("9agycbghiz3e1xpj2702hn30z"); //     /* adjust text area */
UNSUPPORTED("c5jen7s7dn54yuakfb7yncb47"); //     if (f->lp && !nojustify_p) {
UNSUPPORTED("65j1bcx86bkt4yhiu9enb30sv"); // 	f->lp->space.x += d.x;
UNSUPPORTED("2hh4ho34pe678h62y8yhzq6ek"); // 	f->lp->space.y += d.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("esogsiw5n6l1sk5umv7ky4yi"); //     /* adjust children */
UNSUPPORTED("9mofgi5b5ggg6ugo378omtt93"); //     if (f->n_flds) {
UNSUPPORTED("99h71dz46rnni7lfrxm1wzs87"); // 	if (f->LR)
UNSUPPORTED("3idlilzzcmmfphl3ogxoi0gz"); // 	    inc = d.x / f->n_flds;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("gsh4tsmu1upaadj6oebffxe4"); // 	    inc = d.y / f->n_flds;
UNSUPPORTED("9cjjlq42o712cit9tby1b7l6a"); // 	for (i = 0; i < f->n_flds; i++) {
UNSUPPORTED("abiqjigzcxzn37q0cla73adcd"); // 	    sf = f->fld[i];
UNSUPPORTED("cnehjao25jc5jjv5vfumb0qmk"); // 	    amt = ((int) ((i + 1) * inc)) - ((int) (i * inc));
UNSUPPORTED("e5eeg49by0lz43uois7f6e5xt"); // 	    if (f->LR)
UNSUPPORTED("bhtvr8j5ek5tt7xm6c9z7hu5r"); // 		newsz = pointfof(sf->size.x + amt, sz.y);
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("cajand0txpij2kirrkxtjn1ps"); // 		newsz = pointfof(sz.x, sf->size.y + amt);
UNSUPPORTED("aq6om414jdhfaf5204q5mmln4"); // 	    resize_reclbl(sf, newsz, nojustify_p);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ds4v2i9xw0hm4y53ggbt8z2yk
// static void pos_reclbl(field_t * f, pointf ul, int sides) 
public static Object pos_reclbl(Object... arg) {
UNSUPPORTED("57l93w5nat2i71wn6e82nj4x9"); // static void pos_reclbl(field_t * f, pointf ul, int sides)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2f3a6lf3ce1tznglhu95xoda0"); //     int i, last, mask;
UNSUPPORTED("74djpee6zz6hk95hdvulbbjkb"); //     f->sides = sides;
UNSUPPORTED("2mtubjx0wssvmgcmhx716lypo"); //     f->b.LL = pointfof(ul.x, ul.y - f->size.y);
UNSUPPORTED("26ciar9flz0wc6r1zk7krxrg1"); //     f->b.UR = pointfof(ul.x + f->size.x, ul.y);
UNSUPPORTED("6f9cc8k5z3tih73jcgvyqzdy5"); //     last = f->n_flds - 1;
UNSUPPORTED("d5c2y0xbpfqkrfiwsb6b2q6qr"); //     for (i = 0; i <= last; i++) {
UNSUPPORTED("6s6izyatmponzopy22eomuw6h"); // 	if (sides) {
UNSUPPORTED("xwrnxfxcwmxuxvjpfw1sb7lt"); // 	    if (f->LR) {
UNSUPPORTED("by48lavhlctvmymlkz9qkjvi0"); // 		if (i == 0) {
UNSUPPORTED("4r4jabt98z99ira0e4bpyyktj"); // 		    if (i == last)
UNSUPPORTED("rvq6ubzk0rezd88243ailv84"); // 			mask = (1<<2) | (1<<0) | (1<<1) | (1<<3);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("6tzvg9fxvr7v7qt2yh73xn94n"); // 			mask = (1<<2) | (1<<0) | (1<<3);
UNSUPPORTED("9xksdopde69ktgm9z90l55he9"); // 		} else if (i == last)
UNSUPPORTED("eas1815ent5z97kozcm2qwglp"); // 		    mask = (1<<2) | (1<<0) | (1<<1);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("evm1s2keksyeukfcgn64wt7k6"); // 		    mask = (1<<2) | (1<<0);
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("by48lavhlctvmymlkz9qkjvi0"); // 		if (i == 0) {
UNSUPPORTED("4r4jabt98z99ira0e4bpyyktj"); // 		    if (i == last)
UNSUPPORTED("rvq6ubzk0rezd88243ailv84"); // 			mask = (1<<2) | (1<<0) | (1<<1) | (1<<3);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("2wutifbakw4oqtj4lrjidlytt"); // 			mask = (1<<2) | (1<<1) | (1<<3);
UNSUPPORTED("9xksdopde69ktgm9z90l55he9"); // 		} else if (i == last)
UNSUPPORTED("84g4in93npbh6zty66dclzf8a"); // 		    mask = (1<<3) | (1<<0) | (1<<1);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("ojlsh8buk0hgqhg1p1o1ogxy"); // 		    mask = (1<<3) | (1<<1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6to1esmb8qfrhzgtr7jdqleja"); // 	} else
UNSUPPORTED("cbvih14pp0igaj8ytp83216z6"); // 	    mask = 0;
UNSUPPORTED("c310n6zcvshjtf3nn15m858xd"); // 	pos_reclbl(f->fld[i], ul, sides & mask);
UNSUPPORTED("99h71dz46rnni7lfrxm1wzs87"); // 	if (f->LR)
UNSUPPORTED("6o8kbyjjm2bi8zipsgi5lr8ww"); // 	    ul.x = ul.x + f->fld[i]->size.x;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("90vovw2s5lzb6up513xqh9u43"); // 	    ul.y = ul.y - f->fld[i]->size.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 h2lcuthzwljbcjwdeidw1jiv
// static void record_init(node_t * n) 
public static Object record_init(Object... arg) {
UNSUPPORTED("8bn5tg52zfoyvwdnvqbw04l70"); // static void record_init(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("86oi3nfc6x87w7aj799p0ypzn"); //     field_t *info;
UNSUPPORTED("6rttlsij6gno16sto3oqsqa40"); //     pointf ul, sz;
UNSUPPORTED("9fmah1vlmqynhowqu4v9q2zu0"); //     int flip, len;
UNSUPPORTED("4ly7xwlmpjgyokyzpns8fjvi0"); //     char *textbuf;		/* temp buffer for storing labels */
UNSUPPORTED("1klslcqyc6484ub7o51jznyqf"); //     int sides = (1<<0) | (1<<1) | (1<<2) | (1<<3);
UNSUPPORTED("6jo9tl9gx62fi9j5u1nn0dfzw"); //     /* Always use rankdir to determine how records are laid out */
UNSUPPORTED("ez37gg27m14oqygf9tny4whd9"); //     flip = NOT(GD_realflip(agraphof(n)));
UNSUPPORTED("831ssvyd2gs7q5d7r83p0tckc"); //     reclblp = ND_label(n)->text;
UNSUPPORTED("b61x6z42kkk6b66dyi8rykpvw"); //     len = strlen(reclblp);
UNSUPPORTED("17kft1nompfgymd2cpz9p06we"); //     /* For some forgotten reason, an empty label is parsed into a space, so
UNSUPPORTED("13t238hjobkason0chns2coag"); //      * we need at least two bytes in textbuf.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("6k99spqggna26l34pfzpjeotk"); //     len = MAX(len, 1);
UNSUPPORTED("ey612n2e72vl1gbnw3arjznb5"); //     textbuf = (char*)zmalloc((len + 1)*sizeof(char));
UNSUPPORTED("7uzna6j5b36j6wvueakqwtauo"); //     if (!(info = parse_reclbl(n, flip, NOT(0), textbuf))) {
UNSUPPORTED("7iezaksu9hyxhmv3r4cp4o529"); // 	agerr(AGERR, "bad label format %s\n", ND_label(n)->text);
UNSUPPORTED("8f1id7rqm71svssnxbjo0uwcu"); // 	reclblp = "\\N";
UNSUPPORTED("2wv3zfqhq53941rwk4vu9p9th"); // 	info = parse_reclbl(n, flip, NOT(0), textbuf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9upwq9jn3fup3nwwae3y0vv0q"); //     free(textbuf);
UNSUPPORTED("31sxwdtqr63gtnpp86jz8pdac"); //     size_reclbl(n, info);
UNSUPPORTED("434l8ab1hff80o0xz22d91u00"); //     sz.x = (ROUND((ND_width(n))*72));
UNSUPPORTED("7qr0k3gmk24mj6toqadcqky22"); //     sz.y = (ROUND((ND_height(n))*72));
UNSUPPORTED("6qf3p8rzhkupjmocuje9q4p2q"); //     if (mapbool(late_string(n, N_fixed, "false"))) {
UNSUPPORTED("8iu51xbtntpdf5sc00g91djym"); // 	if ((sz.x < info->size.x) || (sz.y < info->size.y)) {
UNSUPPORTED("4vs5u30jzsrn6fpjd327xjf7r"); // /* should check that the record really won't fit, e.g., there may be no text.
UNSUPPORTED("7k6yytek9nu1ihxix2880667g"); // 			agerr(AGWARN, "node '%s' size may be too small\n", agnameof(n));
UNSUPPORTED("bnetqzovnscxile7ao44kc0qd"); // */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("9vx9i9jopcbh8v928ih57vgj7"); // 	sz.x = MAX(info->size.x, sz.x);
UNSUPPORTED("evseq8gqlm6aqw269kwgi57xh"); // 	sz.y = MAX(info->size.y, sz.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c0urxnxgjlnro89b6209kl0zf"); //     resize_reclbl(info, sz, mapbool(late_string(n, N_nojustify, "false")));
UNSUPPORTED("2qhtglqv4czw1wzgy8h9w4o3v"); //     ul = pointfof(-sz.x / 2., sz.y / 2.);	/* FIXME - is this still true:    suspected to introduce ronding error - see Kluge below */
UNSUPPORTED("816kf840erjhpdg9bin63xyig"); //     pos_reclbl(info, ul, sides);
UNSUPPORTED("7rjoo40zh2fd13jllh0j2n1w1"); //     ND_width(n) = ((info->size.x)/(double)72);
UNSUPPORTED("x5gpmbn3zd3hac5yz2s7trtx"); //     ND_height(n) = ((info->size.y + 1)/(double)72);	/* Kluge!!  +1 to fix rounding diff between layout and rendering 
UNSUPPORTED("edky8r362p1bpruc2jcs2hyft"); // 						   otherwise we can get -1 coords in output */
UNSUPPORTED("40bol1suut41tvh0xudcyhgpi"); //     ND_shape_info(n) = (void *) info;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1lq2tksbz3nzqw9c3xqfs4ymf
// static void record_free(node_t * n) 
public static Object record_free(Object... arg) {
UNSUPPORTED("8anx9p03jsmcuhguyzf7q6qe3"); // static void record_free(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aiplewsp8j9h5b1bokpivfnqv"); //     field_t *p = ND_shape_info(n);
UNSUPPORTED("cn1q1h4lwj1gctn9nim9hdhpt"); //     free_field(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a005gfg4ujp2d29bpdrtowla0
// static field_t *map_rec_port(field_t * f, char *str) 
public static Object map_rec_port(Object... arg) {
UNSUPPORTED("7m0itp0yyimj2qrw4m7gkucwe"); // static field_t *map_rec_port(field_t * f, char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8vkza1jahjxmu3aujqqwejvh4"); //     field_t *rv;
UNSUPPORTED("bg1s70jnrhf3ei0qo60unqlly"); //     int sub;
UNSUPPORTED("2wb1wig5yijs1gwl6gsqsfypb"); //     if (f->id && ((*(f->id)==*(str)&&!strcmp(f->id,str))))
UNSUPPORTED("c6qw5ghrclqer15nnzk3dcf9o"); // 	rv = f;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("52b2tnzwipbuygdvyeyxg1lij"); // 	rv = NULL;
UNSUPPORTED("2z6jes522cxoquqcehqxkfezp"); // 	for (sub = 0; sub < f->n_flds; sub++)
UNSUPPORTED("2nwfsthm8r2bsdqcq3cs6mwi7"); // 	    if ((rv = map_rec_port(f->fld[sub], str)))
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 chsi0jlfodruvkjj5dlrv5ur3
// static port record_port(node_t * n, char *portname, char *compass) 
public static Object record_port(Object... arg) {
UNSUPPORTED("108iil8l4qbk7n5zy99yzuhna"); // static port record_port(node_t * n, char *portname, char *compass)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("30fmp9xlabtd67je318axlfiy"); //     field_t *f;
UNSUPPORTED("9nozntsyybg1oi5e3jpibarmk"); //     field_t *subf;
UNSUPPORTED("9ricxd1wv1am78xxjvd40ki5e"); //     port rv;
UNSUPPORTED("dezxih515uk47gnmutkm1zuno"); //     int sides;			/* bitmap of which sides the port lies along */
UNSUPPORTED("8531pl39rpqki88wilp72dh12"); //     if (portname[0] == '\0')
UNSUPPORTED("ct95magnbje1vlax6sewfa40f"); // 	return Center;
UNSUPPORTED("ci2ge3idao9rokpvacvcspaxl"); //     sides = (1<<0) | (1<<1) | (1<<2) | (1<<3);
UNSUPPORTED("cm99rhftfe8nq2suzac5fwbgp"); //     if (compass == NULL)
UNSUPPORTED("238a13tlawcw3bixwliz859y5"); // 	compass = "_";
UNSUPPORTED("9xovezi85vdgw8han4h0wr87s"); //     f = (field_t *) ND_shape_info(n);
UNSUPPORTED("9ihnypp6vu95to95ydtnntq0l"); //     if ((subf = map_rec_port(f, portname))) {
UNSUPPORTED("6fvm5el2zlrng001datkwt7cc"); // 	if (compassPort(n, &subf->b, &rv, compass, subf->sides, NULL)) {
UNSUPPORTED("cw5grwj6gbj94jcztvnp2ooyj"); // 	    agerr(AGWARN,
UNSUPPORTED("en2xpqtprfng8gmc77dzq7klv"); // 		  "node %s, port %s, unrecognized compass point '%s' - ignored\n",
UNSUPPORTED("cmo03yl2q1wgn0c1r45y1ay5e"); // 		  agnameof(n), portname, compass);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3pocnlftms62i7fn2yjeglilc"); //     } else if (compassPort(n, &f->b, &rv, portname, sides, NULL)) {
UNSUPPORTED("98h27uayj9wzp3psyqb5feymg"); // 	unrecognized(n, portname);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1f7b6eq3csywqv96raw75jqxr
// static boolean record_inside(inside_t * inside_context, pointf p) 
public static Object record_inside(Object... arg) {
UNSUPPORTED("86kzi0ldxu2wp8jrcz52g23br"); // static boolean record_inside(inside_t * inside_context, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("awt4go86uiz8hw73uxp1kk1pd"); //     field_t *fld0;
UNSUPPORTED("4rtja2mn137n7wcxryrmo12ko"); //     boxf *bp = inside_context->s.bp;
UNSUPPORTED("d8oppi8gt9b4eaonkdgb7a54l"); //     node_t *n = inside_context->s.n;
UNSUPPORTED("dhrqm3z9pldopj98epb4nfoi4"); //     boxf bbox;
UNSUPPORTED("5a3hmefcpaol32pvsn6wgwi4p"); //     /* convert point to node coordinate system */
UNSUPPORTED("7htr4npyn53khcc3o9gi9eew4"); //     p = ccwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("57ly3awriopiy2hf7v62cw7ny"); //     if (bp == NULL) {
UNSUPPORTED("6f802g721m0ev4mztwuama272"); // 	fld0 = (field_t *) ND_shape_info(n);
UNSUPPORTED("blfb7lv2fmwy1z0sax8wmxz17"); // 	bbox = fld0->b;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("2punkrny8rmrqvqgluved9xjm"); // 	bbox = *bp;
UNSUPPORTED("ca4t2zns3zehe95rbrig7evej"); //     return INSIDE(p, bbox);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3p54k8x2kyueort8kj41qvkty
// static int record_path(node_t * n, port * prt, int side, boxf rv[], 		       int *kptr) 
public static Object record_path(Object... arg) {
UNSUPPORTED("98cjokh407kmiyj3ne6z8tugr"); // static int record_path(node_t * n, port * prt, int side, boxf rv[],
UNSUPPORTED("5j2zss62xcus5zenfk516rmmp"); // 		       int *kptr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bxj1nynia6lfoufq42q6fbw81"); //     int i, ls, rs;
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("86oi3nfc6x87w7aj799p0ypzn"); //     field_t *info;
UNSUPPORTED("41x42ivkcydor03dmtntfcr2c"); //     if (!prt->defined)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("2q24y4tztoqnjrae3pyz4xos1"); //     p = prt->p;
UNSUPPORTED("qqnxp10edgu1p0nyl4uq7t2m"); //     info = (field_t *) ND_shape_info(n);
UNSUPPORTED("2ocjia9onk38jkynynvvsx6wy"); //     for (i = 0; i < info->n_flds; i++) {
UNSUPPORTED("6hin33h1bfc9sf9lszjmqgyev"); // 	if (!GD_flip(agraphof(n))) {
UNSUPPORTED("72j4jh7zoqcwbpm47gdgozled"); // 	    ls = info->fld[i]->b.LL.x;
UNSUPPORTED("86sxinhjfgtxnim78cjm8ptup"); // 	    rs = info->fld[i]->b.UR.x;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("dm9w81fxfdqc5bhtaimpbisvl"); // 	    ls = info->fld[i]->b.LL.y;
UNSUPPORTED("3sqtp996aa7m19wv9gwkrvav1"); // 	    rs = info->fld[i]->b.UR.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cc82ixosqyaz38wkysyxdbus1"); // 	if (BETWEEN(ls, p.x, rs)) {
UNSUPPORTED("cmdehzhkvreoa7ge2cf6l81ux"); // 	    /* FIXME: I don't understand this code */
UNSUPPORTED("75jifr4aucrxp2hvnsrcfunej"); // 	    if (GD_flip(agraphof(n))) {
UNSUPPORTED("8p9z8b0nypgkzi1b3k7sx0fyz"); // 		rv[0] = flip_rec_boxf(info->fld[i]->b, ND_coord(n));
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("b2c88nitm58fxzi8rh5vergvc"); // 		rv[0].LL.x = ND_coord(n).x + ls;
UNSUPPORTED("4ivcuhn3xc40qws7sdw4ai8s5"); // 		rv[0].LL.y = ND_coord(n).y - (ND_ht(n) / 2);
UNSUPPORTED("39yu1w8ymm77xe0plqbb1tlsu"); // 		rv[0].UR.x = ND_coord(n).x + rs;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("78963top1lc1j91gvr130yzlg"); // 	    rv[0].UR.y = ND_coord(n).y + (ND_ht(n) / 2);
UNSUPPORTED("9jd15eqfyfmga3uhuzmoiobm4"); // 	    *kptr = 1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2nk83e61yc1xqh0sxx13m5l1j"); //     return side;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 exqpf0bds3z9eae52fqnqdv4f
// static void gen_fields(GVJ_t * job, node_t * n, field_t * f) 
public static Object gen_fields(Object... arg) {
UNSUPPORTED("alxnwxfpkfjmexy2v4wj8txsk"); // static void gen_fields(GVJ_t * job, node_t * n, field_t * f)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("676ly5o6s7hfka2a7xkzfm02t"); //     pointf AF[2], coord;
UNSUPPORTED("cofpjibrqirnx1jqt3qoptoa8"); //     if (f->lp) {
UNSUPPORTED("ecjyk9xlxeub85r4qs7onmaab"); // 	f->lp->pos = add_pointf(mid_pointf(f->b.LL, f->b.UR), ND_coord(n));
UNSUPPORTED("ayphvetcwpb3thkz5bz5xrpdy"); // 	emit_label(job, EMIT_NLABEL, f->lp);
UNSUPPORTED("7wlqawgs50g2g4tdmjv67qq9a"); // 	penColor(job, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("80cw1alior8ng7fmhmcmpy9ql"); //     coord = ND_coord(n);
UNSUPPORTED("7zbyipqbl6t75m71to6vrvnmq"); //     for (i = 0; i < f->n_flds; i++) {
UNSUPPORTED("72y5kehzju60vd6wi6h7d6r0v"); // 	if (i > 0) {
UNSUPPORTED("xwrnxfxcwmxuxvjpfw1sb7lt"); // 	    if (f->LR) {
UNSUPPORTED("31wcrtlrn4wvlomxri9wdvmtj"); // 		AF[0] = f->fld[i]->b.LL;
UNSUPPORTED("27hrv59ztxl0a8b8gttmk2ikp"); // 		AF[1].x = AF[0].x;
UNSUPPORTED("1gqm7vpecmn340v1a2mskym8t"); // 		AF[1].y = f->fld[i]->b.UR.y;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("e1tqrqwt7bk5snpfcayjcbyzx"); // 		AF[1] = f->fld[i]->b.UR;
UNSUPPORTED("9r6qdvxkhroix64em71xgwldt"); // 		AF[0].x = f->fld[i]->b.LL.x;
UNSUPPORTED("vymaq435j4bsfggfjzzfiugk"); // 		AF[0].y = AF[1].y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9n3sn3rtnfiq76vd0067xvph3"); // 	    AF[0] = add_pointf(AF[0], coord);
UNSUPPORTED("1g9pox6kvg6c5nwlvug1i3dav"); // 	    AF[1] = add_pointf(AF[1], coord);
UNSUPPORTED("9nhrbbkzn6ygl91paz8e19asp"); // 	    gvrender_polyline(job, AF, 2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("56fhzogzd9i3iuh44c2jkxoaa"); // 	gen_fields(job, n, f->fld[i]);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3bum3y2gmowozskwp7e492wm7
// static void record_gencode(GVJ_t * job, node_t * n) 
public static Object record_gencode(Object... arg) {
UNSUPPORTED("cpq4ylwlb0lwi7ibim51gndor"); // static void record_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("bzz7vodjegzgwxp8jzgkq3uti"); //     boxf BF;
UNSUPPORTED("en6q26cyrg17g6yd6el73b3ns"); //     pointf AF[4];
UNSUPPORTED("b89hspuulkkzgmrj59tfy2fus"); //     int style;
UNSUPPORTED("30fmp9xlabtd67je318axlfiy"); //     field_t *f;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = (obj->url || obj->explicit_tooltip);
UNSUPPORTED("3ml0gugucwlbwt5mbcdlymm8b"); //     int filled;
UNSUPPORTED("bhtcyodd9jiazat6sqhp9pm4x"); //     char* clrs[2];
UNSUPPORTED("9xovezi85vdgw8han4h0wr87s"); //     f = (field_t *) ND_shape_info(n);
UNSUPPORTED("arohpr2hcj50a0nm6wiegz75n"); //     BF = f->b;
UNSUPPORTED("9dwww64wl2oaucxyyhoa2u5op"); //     BF.LL.x += ND_coord(n).x;
UNSUPPORTED("eqak8167f3whj617r6180val"); //     BF.LL.y += ND_coord(n).y;
UNSUPPORTED("3u5f15d4i1cs3igvot9majw8n"); //     BF.UR.x += ND_coord(n).x;
UNSUPPORTED("18gannqx4rafy1juoif3uog1p"); //     BF.UR.y += ND_coord(n).y;
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("6yjfupcwvts03fbmr493ea2ja"); //     style = stylenode(job, n);
UNSUPPORTED("5qxdje5wxqq1c9786htlyohkx"); //     penColor(job, n);
UNSUPPORTED("92hvfvrwzs8dy1vdgk97mu8rm"); //     clrs[0] = NULL;
UNSUPPORTED("a0xb2wsthoxt62j0aks4aht13"); //     if (style & (1 << 0)) {
UNSUPPORTED("64vz86w7mg90duu37ik1bcm8m"); // 	char* fillcolor = findFill (n);
UNSUPPORTED("4xv0cmpfa4sol0pqmfumr0rnm"); // 	float frac;
UNSUPPORTED("dily1m3rwbo5mniq7aneh3qhu"); // 	if (findStopColor (fillcolor, clrs, &frac)) {
UNSUPPORTED("5m1l4f0yk2x1r9n00p7xoarhk"); //             gvrender_set_fillcolor(job, clrs[0]);
UNSUPPORTED("850qgpdnne96gxnh244hf2rh2"); // 	    if (clrs[1]) 
UNSUPPORTED("m1ck996y4kjzra9yxa5gif68"); // 		gvrender_set_gradient_vals(job,clrs[1],late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("f3qa0cv737ikcre1vpqlkukio"); // 	    else 
UNSUPPORTED("72n9vguy2n416qggkz5tpz279"); // 		gvrender_set_gradient_vals(job,"black",late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("5dn7m0lqq174sxj9ezr6p8anp"); // 	    if (style & (1 << 1))
UNSUPPORTED("s4xfcz4il9k9jw0w0dh9lzpj"); // 		filled = 3;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("1ijl60mqfpjns1tss115yw4zp"); // 		filled = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("7ek7aftv8z293izx886r01oqm"); // 	    filled = 1;
UNSUPPORTED("pufcu1p86jfo891eaibok4yb"); //             gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("53gluhbrz2oi6qw7sff0fb0hj"); //     else filled = 0;
UNSUPPORTED("a80jadmfy336hxiquc1baf16m"); //     if ((*(ND_shape(n)->name)==*("Mrecord")&&!strcmp(ND_shape(n)->name,"Mrecord")))
UNSUPPORTED("6iazzglp38g7uxmnloiwk5ilq"); // 	style |= (1 << 2);
UNSUPPORTED("gn97uo130dzjs4b5bnhnvlsq"); //     if (((style) & ((1 << 2) | (1 << 3) | (127 << 24)))) {
UNSUPPORTED("5rrbml0v0bc8c6x2ddgjh75p1"); // 	AF[0] = BF.LL;
UNSUPPORTED("8ctty3poiybj8vyrg3fy6s4ju"); // 	AF[2] = BF.UR;
UNSUPPORTED("bqdx8e632ko1pofmr5b91xpmh"); // 	AF[1].x = AF[2].x;
UNSUPPORTED("7gb7yo735gfv67doxjnyl8av7"); // 	AF[1].y = AF[0].y;
UNSUPPORTED("7w69hwqpw5l9f1rsaolr1ytmx"); // 	AF[3].x = AF[0].x;
UNSUPPORTED("cg5ir4ssc1l9d4x56swq1rw0k"); // 	AF[3].y = AF[2].y;
UNSUPPORTED("7gm0bhmoegfvu3uf7hnwfae67"); // 	round_corners(job, AF, 4, style, filled);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("5sf771cxqfrvdu2vzl3t1687e"); // 	gvrender_box(job, BF, filled);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9rksrkk1y26l0lgodpusjgg6r"); //     gen_fields(job, n, f);
UNSUPPORTED("ovdkxg0m1si7d9k8lawdnq"); //     if (clrs[0]) free (clrs[0]);
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 7unoy39g6rhro6he8kci7oh4n
// static shape_desc **UserShape


//1 94927xsjiykujshql95ma97vb
// static int N_UserShape




//3 35sn43hohjmtc7uvkjrx6u7jt
// shape_desc *find_user_shape(const char *name) 
public static Object find_user_shape(Object... arg) {
UNSUPPORTED("dn82ttgu4gvl5nnzl8cu29o63"); // shape_desc *find_user_shape(const char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("757gomzjey403egq882hclnn0"); //     if (UserShape) {
UNSUPPORTED("30x6ygp0c6pjoq410g7sbl3lv"); // 	for (i = 0; i < N_UserShape; i++) {
UNSUPPORTED("3ka0imewegdrxvt7cdk37mqgj"); // 	    if ((*(UserShape[i]->name)==*(name)&&!strcmp(UserShape[i]->name,name)))
UNSUPPORTED("5eh2ibmiqg7qx9z5fvoxfnfyo"); // 		return UserShape[i];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("o68dp934ebg4cplebgc5hv4v"); //     return NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2eqlcx6fsiu46csoml6irvkib
// static shape_desc *user_shape(char *name) 
public static Object user_shape(Object... arg) {
UNSUPPORTED("7omo61zea17p3tb29vnkd0g5k"); // static shape_desc *user_shape(char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7vbdsnuzb5k7dx9t6iq98bqvu"); //     shape_desc *p;
UNSUPPORTED("1cmwlrf177nfgj5tr0chf53vd"); //     if ((p = find_user_shape(name)))
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
UNSUPPORTED("61jp6r0u1tz0ytbo0pkbtibih"); //     i = N_UserShape++;
UNSUPPORTED("8m5c0jxj3trkdqlzd49ch4393"); //     UserShape = ALLOC(N_UserShape, UserShape, shape_desc *);
UNSUPPORTED("2ho6cxrgsfg5i9qgyksouth00"); //     p = UserShape[i] = (shape_desc*)zmalloc(sizeof(shape_desc));
UNSUPPORTED("9mrvf7ijsvjzugxe3889n26wy"); //     *p = Shapes[0];
UNSUPPORTED("do2hwp4zw8tlp7jj180uisyrb"); //     p->name = strdup(name);
UNSUPPORTED("2x9tm5f2pfgyebmuac8jgc2j9"); //     if (Lib == NULL && !(*(name)==*("custom")&&!strcmp(name,"custom"))) {
UNSUPPORTED("dmfmug44jzfjuimu3j7e9meux"); // 	agerr(AGWARN, "using %s for unknown shape %s\n", Shapes[0].name,
UNSUPPORTED("54012lqhgsbg1obhzpdwdl35o"); // 	      p->name);
UNSUPPORTED("e7dpsukvk4pid9w6icote4v1b"); // 	p->usershape = 0;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("di2ydnxklvyghg80rycxyto6o"); // 	p->usershape = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bmt148wdf0a7gslm7hmp043jy
// shape_desc *bind_shape(char *name, node_t * np) 
public static ST_shape_desc bind_shape(CString name, ST_Agnode_s np) {
ENTERING("bmt148wdf0a7gslm7hmp043jy","bind_shape");
try {
	ST_shape_desc rv = null;
    CString str;
    str = safefile(agget(np, new CString("shapefile")));
    /* If shapefile is defined and not epsf, set shape = custom */
    if (str!=null && UNSUPPORTED("!(*(name)==*(\"epsf\")&&!strcmp(name,\"epsf\"))")!=null)
	name = new CString("custom");
    if (N(name.charAt(0)=='c' && N(strcmp(name,new CString("custom"))))) {
	for (ST_shape_desc ptr : Z.z().Shapes) {
	    if ((N(strcmp(ptr.name,name)))) {
		rv = ptr;
		break;
	    }
	}
    }
    if (rv == null)
UNSUPPORTED("7funuix8h6nhe6fqrjsec3kvk"); // 	rv = user_shape(name);
    return rv;
} finally {
LEAVING("bmt148wdf0a7gslm7hmp043jy","bind_shape");
}
}




//3 9n2zfdpzi6zgvnhcb3kz7nw1u
// static boolean epsf_inside(inside_t * inside_context, pointf p) 
public static Object epsf_inside(Object... arg) {
UNSUPPORTED("cq9kgtgzrb9sazy7y2fpt859x"); // static boolean epsf_inside(inside_t * inside_context, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7lh87lvufqsd73q9difg0omei"); //     pointf P;
UNSUPPORTED("9ikeydfq03qx7m09iencqsk36"); //     double x2;
UNSUPPORTED("d8oppi8gt9b4eaonkdgb7a54l"); //     node_t *n = inside_context->s.n;
UNSUPPORTED("823iiqtx9pt0gijqrohrd3zx7"); //     P = ccwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("6uktb6bwhvglg7v3nygillmqx"); //     x2 = ND_ht(n) / 2;
UNSUPPORTED("3gki5ta81e51de9h4b5nvmoij"); //     return ((P.y >= -x2) && (P.y <= x2) && (P.x >= -ND_lw(n))
UNSUPPORTED("3bzok6rkdjzamkk155dcqc8n2"); // 	    && (P.x <= ND_rw(n)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6xv85fky6n2v03mt0dbvpz05e
// static void epsf_gencode(GVJ_t * job, node_t * n) 
public static Object epsf_gencode(Object... arg) {
UNSUPPORTED("4mtkoc5bwv0wkraw1xv9ptjlo"); // static void epsf_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("31b47kcwg6z2ds4cugdfq5hft"); //     epsf_t *desc;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = (obj->url || obj->explicit_tooltip);
UNSUPPORTED("7wygkmvhwjn2l2fmpw5bj1o6g"); //     desc = (epsf_t *) (ND_shape_info(n));
UNSUPPORTED("c98tv4jn3ode5so0mefrwcut7"); //     if (!desc)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("4i1fd7rw5klkjsnyehf6v44a3"); //     if (desc)
UNSUPPORTED("8yueq6sa0qe98f00ykgedfrzl"); // 	fprintf(job->output_file,
UNSUPPORTED("aqf73hied952lsirjjyf0hfr4"); // 		"%.5g %.5g translate newpath user_shape_%d\n",
UNSUPPORTED("afxenk7cqa80e074cox3d04n5"); // 		ND_coord(n).x + desc->offset.x,
UNSUPPORTED("57mec07ttst0x3aspieywssni"); // 		ND_coord(n).y + desc->offset.y, desc->macro_id);
UNSUPPORTED("1bslo0pyyucx0zmdzt12sei6d"); //     ND_label(n)->pos = ND_coord(n);
UNSUPPORTED("8r8t0lgzzpigm1odig9a9yg1c"); //     emit_label(job, EMIT_NLABEL, ND_label(n));
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d0jsei60yky7c36q8bja8q58d
// static pointf star_size (pointf sz0) 
public static Object star_size(Object... arg) {
UNSUPPORTED("6bl2ntfn97yev6qvlwplor61o"); // static pointf star_size (pointf sz0)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3zlnn621zia9mss7z1ay24myc"); //     pointf sz;
UNSUPPORTED("5u7lf36burm76yokjuxgcd4tn"); //     double r0, r, rx, ry;
UNSUPPORTED("9ya7hg30u0pmebfvhy4ba5kfp"); //     rx = sz0.x/(2*cos((M_PI/10.0)));
UNSUPPORTED("y09869s34d94qtdcsuuz4mjy"); //     ry = sz0.y/(sin((M_PI/10.0)) + sin((3*(M_PI/10.0))));
UNSUPPORTED("1qn336ppz1ubj5d9vmolmwhfa"); //     r0 = MAX(rx,ry);
UNSUPPORTED("99spig8n4dowh045zi2u054cf"); //     r = (r0*sin((2*(2*(M_PI/10.0))))*cos((2*(M_PI/10.0))))/(cos((M_PI/10.0))*cos((2*(2*(M_PI/10.0)))));
UNSUPPORTED("3h9e5okkzg8gzypvpzok96ikc"); //     sz.x = 2*r*cos((M_PI/10.0));
UNSUPPORTED("19ba70prhdthsxh7ukqn07tw9"); //     sz.y = r*(1 + sin((3*(M_PI/10.0))));
UNSUPPORTED("ban3s2canux7qwxava1n2e4v2"); //     return sz;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a7r80ro5nb15ttgfpqwayycmf
// static void star_vertices (pointf* vertices, pointf* bb) 
public static Object star_vertices(Object... arg) {
UNSUPPORTED("8p40gvc5ocryzfeoybiuc0tzd"); // static void star_vertices (pointf* vertices, pointf* bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6ebg0h9irk3pcisrj710o7d79"); //     pointf sz = *bb;
UNSUPPORTED("daheewjo1ertfvnwnfdg2fcxr"); //     double offset, a, aspect = (1 + sin((3*(M_PI/10.0))))/(2*cos((M_PI/10.0)));
UNSUPPORTED("6ir3jujwrh6dpiqug6p2v3ttj"); //     double r, r0, theta = (M_PI/10.0);
UNSUPPORTED("3dcxsdbybxzvk7jsod9d2ubvm"); //     /* Scale up width or height to required aspect ratio */
UNSUPPORTED("o422759cptua4yuo9guk3367"); //     a = sz.y/sz.x;
UNSUPPORTED("4czf228z5owjh6ew3vh3ugvdv"); //     if (a > aspect) {
UNSUPPORTED("97gq966jokpf07dc3wv6hgk45"); // 	sz.x = sz.y/aspect;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7dnql7ghevwkt7vxe0s4wndha"); //     else if (a < aspect) {
UNSUPPORTED("aeoxa8vdht2x8kc1xojdwlz3j"); // 	sz.y = sz.x*aspect;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a8zuo1xnr4jnza3araqzl5q43"); //     /* for given sz, get radius */
UNSUPPORTED("5j3gmpqii05zcp2ncicxs2si"); //     r = sz.x/(2*cos((M_PI/10.0)));
UNSUPPORTED("214oro38cddbf9fk06d0m6duf"); //     r0 = (r*cos((M_PI/10.0))*cos((2*(2*(M_PI/10.0)))))/(sin((2*(2*(M_PI/10.0))))*cos((2*(M_PI/10.0))));
UNSUPPORTED("4rot7vm0whb5r2oo8ne4pn1j4"); //     /* offset is the y shift of circle center from bb center */
UNSUPPORTED("aa1u9d9ckbucmn1eyvyyijwkf"); //     offset = (r*(1 - sin((3*(M_PI/10.0)))))/2;
UNSUPPORTED("5zsqst1ddsdoai9yogpi1mnfl"); //     for (i = 0; i < 10; i += 2) {
UNSUPPORTED("dy5yk8kfoxfn3h4wby7vyciqz"); // 	vertices[i].x = r*cos(theta);
UNSUPPORTED("a3uapptgvfngiztwa4vm4pbuu"); // 	vertices[i].y = r*sin(theta) - offset;
UNSUPPORTED("7z0zntmu5ddcj6evxm9imjmv8"); // 	theta += (2*(M_PI/10.0));
UNSUPPORTED("da5vtvcsngi7wqtllzq8l190t"); // 	vertices[i+1].x = r0*cos(theta);
UNSUPPORTED("9zr96c70zwnim4wjqf6zn7p68"); // 	vertices[i+1].y = r0*sin(theta) - offset;
UNSUPPORTED("7z0zntmu5ddcj6evxm9imjmv8"); // 	theta += (2*(M_PI/10.0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("58zowxx0q5742vxn8iad1i1xe"); //     *bb = sz;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5sbhhjvptmhgl2v2zc12aemgv
// static boolean star_inside(inside_t * inside_context, pointf p) 
public static Object star_inside(Object... arg) {
UNSUPPORTED("2s46vczrfqrysl35qtk55j8dq"); // static boolean star_inside(inside_t * inside_context, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ogz1m3q9xn7z7hiecjp98bmt"); //     static node_t *lastn;	/* last node argument */
UNSUPPORTED("ehzu6nig1i3kg2wnd7f7k9j5n"); //     static polygon_t *poly;
UNSUPPORTED("cy02ifkuodmjjlsu0kxnyjpoh"); //     static int outp, sides;
UNSUPPORTED("53wr032f7cpvhrjze3ml553bu"); //     static pointf *vertex;
UNSUPPORTED("c173x9hgi0epjtbq9crz665t6"); //     static pointf O;		/* point (0,0) */
UNSUPPORTED("4rtja2mn137n7wcxryrmo12ko"); //     boxf *bp = inside_context->s.bp;
UNSUPPORTED("d8oppi8gt9b4eaonkdgb7a54l"); //     node_t *n = inside_context->s.n;
UNSUPPORTED("eu67sekaddiid7bjwclyd9lpq"); //     pointf P, Q, R;
UNSUPPORTED("dk1ablxthh1rqusv958glmv1k"); //     int i, outcnt;
UNSUPPORTED("823iiqtx9pt0gijqrohrd3zx7"); //     P = ccwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("9nc5qvx5xechvyre5wvhjqpjk"); //     /* Quick test if port rectangle is target */
UNSUPPORTED("8ix20ei8mhm5e1r57koylhxmw"); //     if (bp) {
UNSUPPORTED("48wucupbjgeu51wy1djengl4f"); // 	boxf bbox = *bp;
UNSUPPORTED("b87pzpk1bdd2rzscbmza3pxyu"); // 	return INSIDE(P, bbox);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8rl2cn4oxr94675yld5eohkie"); //     if (n != lastn) {
UNSUPPORTED("a7zf42vgzubszo05gyqjhr4bb"); // 	poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("2y1ov1roe3ma4wlkdj2w8r3sg"); // 	vertex = poly->vertices;
UNSUPPORTED("98ormfm5j66dmbja3sdsx38az"); // 	sides = poly->sides;
UNSUPPORTED("d41xba93s17axh19qsbhg0x8a"); // 	/* index to outer-periphery */
UNSUPPORTED("bmmroksk9aecg8ik0z1sxpzie"); // 	outp = (poly->peripheries - 1) * sides;
UNSUPPORTED("47l17pa0edzmfnlr8ysqs0qh4"); // 	if (outp < 0)
UNSUPPORTED("jyf75douzxhfzxfyrq3kes6e"); // 	    outp = 0;
UNSUPPORTED("dz5401vppes7iz7b0c6pzkge6"); // 	lastn = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4zcxdh0y0cit31t1myzksbyc"); //     outcnt = 0;
UNSUPPORTED("bs8ipj0v83bijiw6u6kpz14s1"); //     for (i = 0; i < sides; i += 2) {
UNSUPPORTED("cmwbnui44mpmy3kjz18pxp1cd"); // 	Q = vertex[i + outp];
UNSUPPORTED("4oudcajkxkcstsh2bvjaheadi"); // 	R = vertex[((i+4) % sides) + outp];
UNSUPPORTED("b4anc6i6r4xczgkhjcjudktb"); // 	if (!(same_side(P, O, Q, R))) {
UNSUPPORTED("b291xvw4hm8vcmlaoxcl8dj94"); // 	    outcnt++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3mpbcjrh8r4u6u2twxvyqx9v9"); // 	if (outcnt == 2) {
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 7nso0aprwwsa0je3az7h9nlue
// static char *side_port[] = 




//3 8hx6dn19tost35djnvvnzh92y
// static point cvtPt(pointf p, int rankdir) 
public static Object cvtPt(Object... arg) {
UNSUPPORTED("eid0v5e8f2edm1ehyq1d0zgow"); // static point cvtPt(pointf p, int rankdir)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6nl26joj7b7zxkfry5bt8z27d"); //     pointf q = { 0, 0 };
UNSUPPORTED("6em4lvgp5tqcu7z93czp75mqr"); //     point Q;
UNSUPPORTED("7gnjhunyvxphjgrfh8byey4ch"); //     switch (rankdir) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("a3bxar1i984lfbgqyiie54sqq"); // 	q = p;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("drh1t5heo8w8z199n0vydnon7"); // 	q.x = p.x;
UNSUPPORTED("1sp6xbp6wduyl3r6q3ki03lj"); // 	q.y = -p.y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("aqxuqmimmi2id7ukk2b64x1in"); // 	q.y = p.x;
UNSUPPORTED("djyedvti0u3rb22lyp3mp7i8n"); // 	q.x = -p.y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("33l7a58zp8vj6fuliwdkk2nkn"); //     case 3:
UNSUPPORTED("aqxuqmimmi2id7ukk2b64x1in"); // 	q.y = p.x;
UNSUPPORTED("7d33c84ojx2qc6awisfs88pf5"); // 	q.x = p.y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5da6w8o8qgq5rx9tk3asaefvi"); //     PF2P(q, Q);
UNSUPPORTED("11g52me0fwkpxm5crnv4uqfgr"); //     return Q;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cmt4wr13jgcd9ihg14t972aam
// static char *closestSide(node_t * n, node_t * other, port * oldport) 
public static Object closestSide(Object... arg) {
UNSUPPORTED("7nx8rmo64f0qpalnfdir13szh"); // static char *closestSide(node_t * n, node_t * other, port * oldport)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c57pq0f87j6dnbcvygu7v6k84"); //     boxf b;
UNSUPPORTED("6yudcsapgh1wsfc2gfszyhv9j"); //     int rkd = GD_rankdir(agraphof(n)->root);
UNSUPPORTED("7f1eg16y5dgh2o3bgeyia19n3"); //     point p = { 0, 0 };
UNSUPPORTED("dc41k2vc95rcs2d9fsi2z8wem"); //     point pt = cvtPt(ND_coord(n), rkd);
UNSUPPORTED("bk9nxen9ygr1a03ygu4cdx4rj"); //     point opt = cvtPt(ND_coord(other), rkd);
UNSUPPORTED("4oio8elmu3g3k7i9rcsme7msk"); //     int sides = oldport->side;
UNSUPPORTED("2lc32htm5f42afmf30kcuxspa"); //     char *rv = NULL;
UNSUPPORTED("5myu0a4ngtwkfxidkfff5h3ci"); //     int i, d, mind = 0;
UNSUPPORTED("42bedbxabt6b225h992k8rfpa"); //     if ((sides == 0) || (sides == ((1<<2) | (1<<0) | (1<<3) | (1<<1))))
UNSUPPORTED("85378rh1rr2g6iscv3ocyh4ky"); // 	return rv;		/* use center */
UNSUPPORTED("9qqot69wqtq8rkizbwz0f4vu"); //     if (oldport->bp) {
UNSUPPORTED("ckp5i3rvsjcc5f9xk025kpv81"); // 	b = *oldport->bp;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("ek9a7u2yx8w4r9x5k7somxuup"); // 	if (GD_flip(agraphof(n))) {
UNSUPPORTED("5m0qxjiybs5ei0xyt8rztghk5"); // 	    b.UR.x = ND_ht(n) / 2;
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("7luuqd8n7bpffoa8v27jp7tn3"); // 	    b.UR.y = ND_lw(n);
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("2kqd0a7y22hequs0ypjfw2ltw"); // 	    b.UR.y = ND_ht(n) / 2;
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
UNSUPPORTED("59beisnsabbp6eavnuxrqch2d"); // 	    b.UR.x = ND_lw(n);
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a2n8aqfq0cqpx8elstmfn9oq6"); //     for (i = 0; i < 4; i++) {
UNSUPPORTED("bs33qxg9liuq4cqnxgjcpo2pp"); // 	if ((sides & (1 << i)) == 0)
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("9bo1itj979wxduxtvlcn8uetb"); // 	switch (i) {
UNSUPPORTED("46lzlkypfilrge90rkaiveuyb"); // 	case 0:
UNSUPPORTED("c2y7pxvutf20cxm0z3nfgoc3k"); // 	    p.y = b.LL.y;
UNSUPPORTED("2vekwappcfvbfvrjvig9oqlmp"); // 	    p.x = (b.LL.x + b.UR.x) / 2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2o83im06dulx11wjpy469gkoa"); // 	case 1:
UNSUPPORTED("6ertkjhcjyaq08zsm3g294l4b"); // 	    p.x = b.UR.x;
UNSUPPORTED("3jxafko9fvkftv1vxjstl9gr5"); // 	    p.y = (b.LL.y + b.UR.y) / 2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("b8vgbvwzllfs4lrqmmqyr1spk"); // 	case 2:
UNSUPPORTED("7gucyzlkpil4bfg1hpst17k3d"); // 	    p.y = b.UR.y;
UNSUPPORTED("2vekwappcfvbfvrjvig9oqlmp"); // 	    p.x = (b.LL.x + b.UR.x) / 2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1640m8as34e90xhvvtl877cmo"); // 	case 3:
UNSUPPORTED("3hd28hpejkfur36krdelc2enk"); // 	    p.x = b.LL.x;
UNSUPPORTED("3jxafko9fvkftv1vxjstl9gr5"); // 	    p.y = (b.LL.y + b.UR.y) / 2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7lafbtu79v445pj4z8m4brkmm"); // 	p.x += pt.x;
UNSUPPORTED("ef9oyp0p6aqlpy8atlw8pjnj2"); // 	p.y += pt.y;
UNSUPPORTED("q27stk74ksja0hoiobb8mpfg"); // 	d = DIST2(p, opt);
UNSUPPORTED("77inwlax2kjikvjehyh67g2v1"); // 	if (!rv || (d < mind)) {
UNSUPPORTED("e4k01qtlw2t5jf3hpn1bmxivu"); // 	    mind = d;
UNSUPPORTED("cghq7r095a9g9ix9m1bfx0niw"); // 	    rv = side_port[i];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ckbg1dyu9jzx7g0c9dbriez7r
// port resolvePort(node_t * n, node_t * other, port * oldport) 
public static ST_port resolvePort(ST_Agnode_s n, ST_Agnode_s other, ST_port oldport) {
// WARNING!! STRUCT
return resolvePort_w_(n, other, oldport).copy();
}
private static ST_port resolvePort_w_(ST_Agnode_s n, ST_Agnode_s other, ST_port oldport) {
ENTERING("ckbg1dyu9jzx7g0c9dbriez7r","resolvePort");
try {
 UNSUPPORTED("1aa44pvk8su341rug2x5h5h9o"); // port resolvePort(node_t * n, node_t * other, port * oldport)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9ricxd1wv1am78xxjvd40ki5e"); //     port rv;
UNSUPPORTED("jdbaecwphvo5avabk6l4xiy3"); //     char *compass = closestSide(n, other, oldport);
UNSUPPORTED("afwu4bhodfj1avn0vvvmp2j5n"); //     /* transfer name pointer; all other necessary fields will be regenerated */
UNSUPPORTED("102hdormt85y9xfjr5sy321jr"); //     rv.name = oldport->name;
UNSUPPORTED("f4v5e5pyzu6udom6g40aku41y"); //     compassPort(n, oldport->bp, &rv, compass, oldport->side, NULL);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("ckbg1dyu9jzx7g0c9dbriez7r","resolvePort");
}
}




//3 9ttd9vkih0mogy1ps3khfjum6
// void resolvePorts(edge_t * e) 
public static Object resolvePorts(Object... arg) {
UNSUPPORTED("ceen1bdr2y10gl9g3stj9dq13"); // void resolvePorts(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1avf6c49h37pc64khn45b3zla"); //     if (ED_tail_port(e).dyna)
UNSUPPORTED("bjgkohc8n22pf9yf5anfmfjdl"); // 	ED_tail_port(e) =
UNSUPPORTED("c5phu7zavynmooq4ykt058d6t"); // 	    resolvePort(agtail(e), aghead(e), &ED_tail_port(e));
UNSUPPORTED("56ff4qr7o1xsl73k68f4kjmd1"); //     if (ED_head_port(e).dyna)
UNSUPPORTED("d4aylrk5xwagx7so633xn35ug"); // 	ED_head_port(e) =
UNSUPPORTED("ctvcevp7oejtitu1hunh3yj02"); // 	    resolvePort(aghead(e), agtail(e), &ED_head_port(e));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
