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
import static gen.lib.cgraph.attr__c.agattr;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.refstr__c.aghtmlstr;
import static gen.lib.common.emit__c.init_xdot;
import static gen.lib.common.labels__c.make_label;
import static gen.lib.common.labels__c.strdup_and_subst_obj;
import static gen.lib.common.utils__c.late_double;
import static gen.lib.common.utils__c.late_int;
import static gen.lib.common.utils__c.late_nnstring;
import static gen.lib.common.utils__c.late_string;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.maptoken;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.atof;
import static smetana.core.JUtils.atoi;
import static smetana.core.JUtils.enumAsInt;
import static smetana.core.JUtils.getenv;
import static smetana.core.JUtils.strstr;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.GD_border;
import static smetana.core.Macro.GD_charset;
import static smetana.core.Macro.GD_drawing;
import static smetana.core.Macro.GD_exact_ranksep;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_fontnames;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.GD_label_pos;
import static smetana.core.Macro.GD_nodesep;
import static smetana.core.Macro.GD_rankdir2;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.GD_showboxes;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ROUND;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agraph_s;
import h.ST_layout_t;
import h.ST_pointf;
import h.fontname_kind;
import smetana.core.CString;
import smetana.core.Z;

public class input__c {
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


//1 1fi3wib3hc7ibek0vfrpx9k3i
// static char *usageFmt =     


//1 cpzagrot2j4620xbm08g3qbaz
// static char *genericItems = 


//1 ej8f5pc6itbjzywbvv9r7pgog
// static char *neatoFlags =     


//1 6zygu4f39vz4q5m4oiz64om5v
// static char *neatoItems = 


//1 a5i7jzdqfacw4bequdriv6cb9
// static char *fdpFlags =     


//1 9hrf5y45qp9kii44glcd4nx6e
// static char *fdpItems = 


//1 bw7swzrd97c859k69vhbo6xui
// static char *memtestFlags = 


//1 dlf2hcbhlyk0xi7y4hhyxdjlg
// static char *memtestItems = 


//1 bfkjkg4j8ncjq3fbcfon7ce1a
// static char *configFlags = 


//1 cwsgle0ax1dh0i4rb6c4n90s8
// static char *configItems = 




//3 18dk9rr2jwvw2k0pwd01u1rp
// int dotneato_usage(int exval) 
public static Object dotneato_usage(Object... arg) {
UNSUPPORTED("cjfxortgnqo3ho8cb4mse3bjk"); // int dotneato_usage(int exval)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9qo38fqtykhj9o5wf9n2mmvf9"); //     FILE *outs;
UNSUPPORTED("6p2t5f6k16pthcnlxnvr8fxp2"); //     if (exval > 0)
UNSUPPORTED("ajsyw6vt4yc7jws9my3dfqw55"); // 	outs = stderr;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("9ymsocy1jyvql8lvl7z9v3x1d"); // 	outs = stdout;
UNSUPPORTED("eo2ztyy17mz06ptqvcj5azpza"); //     fprintf(outs, usageFmt, CmdName);
UNSUPPORTED("1nhpls9sffy8jo9sa7638u515"); //     fputs(neatoFlags, outs);
UNSUPPORTED("578fe6racfp402cmjp3xuomor"); //     fputs(fdpFlags, outs);
UNSUPPORTED("a3xdyyuyrv70igk8e8z4415gn"); //     fputs(memtestFlags, outs);
UNSUPPORTED("59cqs6545cogaa8zbv9x1fep0"); //     fputs(configFlags, outs);
UNSUPPORTED("9qmx2r5uawon9q2snigjcita"); //     fputs(genericItems, outs);
UNSUPPORTED("18sodiqes6jpcc8fj1vlkj6bd"); //     fputs(neatoItems, outs);
UNSUPPORTED("boxkj32094gcugdk6u9p1hppc"); //     fputs(fdpItems, outs);
UNSUPPORTED("4yygtzneqsdphtbnhfta2lge0"); //     fputs(memtestItems, outs);
UNSUPPORTED("8ywutcqn5x3zpugo58b72ualq"); //     fputs(configItems, outs);
UNSUPPORTED("3rabv7gfkqu0ag8x2rjiyrjbr"); //     if (GvExitOnUsage && (exval >= 0))
UNSUPPORTED("1swto2i8s568mypddkno5wez1"); // 	exit(exval);
UNSUPPORTED("3jphahsl7jw3c1c1u71vs8dj3"); //     return (exval+1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9s68av3h3ph5gjla9e2d3220t
// static char *getFlagOpt(int argc, char **argv, int *idx) 
public static Object getFlagOpt(Object... arg) {
UNSUPPORTED("7i2co2mk6i4v2e5zed6cohfi0"); // static char *getFlagOpt(int argc, char **argv, int *idx)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0vo8zzyjurgsxynujp3wbwn3"); //     int i = *idx;
UNSUPPORTED("9ldayvulqiau72gm4iigedbe"); //     char *arg = argv[i];
UNSUPPORTED("1ii197c2ypmbtq6b4c6xrmqre"); //     if (arg[2])
UNSUPPORTED("85bk7kest90gpgv6qhqqam6od"); // 	return arg + 2;
UNSUPPORTED("62vtkmbmceearvwa1gge24udl"); //     if (i < argc - 1) {
UNSUPPORTED("chd2f5z6rt19lbaye25ej7q6j"); // 	i++;
UNSUPPORTED("dbe1l1xge33op9cemtc13bsld"); // 	arg = argv[i];
UNSUPPORTED("e7t6j4nall86kdxxvxopr6hl7"); // 	if (*arg && (*arg != '-')) {
UNSUPPORTED("a1qi3k2o6tudikh6zg6qxb32v"); // 	    *idx = i;
UNSUPPORTED("aegbvj6xoqbt16mud86st36ex"); // 	    return arg;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2dx6lb6fkeqxispmv7w0bgsat
// static char* dotneato_basename (char* path) 
public static Object dotneato_basename(Object... arg) {
UNSUPPORTED("58z62a4pwz8fb1fqzgemmk2v"); // static char* dotneato_basename (char* path)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("60anbhq8j280g1jvxqcu76t4v"); //     char* ret;
UNSUPPORTED("cmcyg2bmd7exlb7oegpilnua8"); //     char* s = path;
UNSUPPORTED("7oz55r1w75doc5wm9wdr5ud7c"); //     if (*s == '\0') return path; /* empty string */
UNSUPPORTED("48at50ffoqbw40aae7qlp0vus"); //     while (*s) s++; s--;
UNSUPPORTED("tbim4ak38lvnw1gb72gj4hnh"); //     /* skip over trailing slashes, nulling out as we go */
UNSUPPORTED("clj6wpwuuq0wl5g7f67hqvvfc"); //     while ((s > path) && ((*s == '/') || (*s == '\\')))
UNSUPPORTED("f59muao0hgreza561qmmnlzum"); // 	*s-- = '\0';
UNSUPPORTED("18c1lv0flxz0ts64xlwuviv33"); //     if (s == path) ret = path;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("4x9t5rl1kdp5nac9tewdf9x2n"); // 	while ((s > path) && ((*s != '/') && (*s != '\\'))) s--;
UNSUPPORTED("953u2wmr3tzfpiq8m06fdvhn5"); // 	if ((*s == '/') || (*s == '\\')) ret = s+1;
UNSUPPORTED("5dwayhic40dcurqedqxv1q7mj"); // 	else ret = path;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f3b7mj138albdr4lodyomke0z"); //     return ret;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ez0qfar6yuf01ivvqrnev06fv
// static void use_library(GVC_t *gvc, const char *name) 
public static Object use_library(Object... arg) {
UNSUPPORTED("cjicty7s03euuxnpum74nrt6f"); // static void use_library(GVC_t *gvc, const char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8h89r8rhn7udjmeo8y259899y"); //     static int cnt = 0;
UNSUPPORTED("30nxp5k7c6mdth5ymcpz1oxob"); //     if (name) {
UNSUPPORTED("dwg0l3nktjnwky7m5lipngiot"); // 	Lib = ALLOC(cnt + 2, Lib, const char *);
UNSUPPORTED("axgfffz8lebk44oe1y1djiu6p"); // 	Lib[cnt++] = name;
UNSUPPORTED("3c388gk5lojcaen61m94i3x0w"); // 	Lib[cnt] = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("comriuhmiu8kq7sayutlxoqbq"); //     gvc->common.lib = Lib;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dlfidqx2agrk43ikmxgzw9kgp
// static void global_def(agxbuf* xb, char *dcl, int kind,          attrsym_t * ((*dclfun) (Agraph_t *, int kind, char *, char *)) ) 
public static Object global_def(Object... arg) {
UNSUPPORTED("zydu58d3g8obsevu9l8zo05i"); // static void global_def(agxbuf* xb, char *dcl, int kind,
UNSUPPORTED("zj9p9fdfpp3hwme7atl3cug3"); //          attrsym_t * ((*dclfun) (Agraph_t *, int kind, char *, char *)) )
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("c6ykztqlvb01grrqat3q7f8hg"); //     char *rhs = "true";
UNSUPPORTED("7c3pfnvbbbnijw9cg9xkyyatm"); //     attrsym_t *sym;
UNSUPPORTED("1qmhad0yyiddc207b8z5rm70x"); //     if ((p = strchr(dcl, '='))) {
UNSUPPORTED("5s96z976xk7iglr5vvuad1dsb"); // 	agxbput_n (xb, dcl, p-dcl);
UNSUPPORTED("dbw9pn8xmpdqi11uffv4r6gxq"); //         rhs = p+1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("dv0ywo1nopy8xc9d9kfbn0hgz"); // 	agxbput (xb, dcl);
UNSUPPORTED("dhedzhv3dnzrq7ytgiqff11ku"); //     sym = dclfun(NULL, kind, (((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'\0'))),(char*)((xb)->ptr = (xb)->buf)), rhs);
UNSUPPORTED("6o4s3a3c3rae5ltba8nvab5px"); //     sym->fixed = 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5qvhl3c476hpwnik5r2ee5pin
// static int gvg_init(GVC_t *gvc, graph_t *g, char *fn, int gidx) 
public static Object gvg_init(Object... arg) {
UNSUPPORTED("69zdfufo90wdjvfvsw59lz5n3"); // static int gvg_init(GVC_t *gvc, graph_t *g, char *fn, int gidx)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c55ofvf49idlhjsnnxfqjpi9s"); //     GVG_t *gvg;
UNSUPPORTED("1nqr81udw639pz7enx2hfhtn5"); //     gvg = zmalloc(sizeof(GVG_t));
UNSUPPORTED("wpylwsmjyiuxs9f8x3srqmfs"); //     if (!gvc->gvgs) 
UNSUPPORTED("9y22l2dxq6artoaqqeeczdq1x"); // 	gvc->gvgs = gvg;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2nndq73tw0aaltr2i1ajvsspn"); // 	gvc->gvg->next = gvg;
UNSUPPORTED("e9w6optlcophkwjmfin7kyi1i"); //     gvc->gvg = gvg;
UNSUPPORTED("eish9dbcdxs6v4dh4sgg6uzjj"); //     gvg->gvc = gvc;
UNSUPPORTED("27sxrps4axrp5fbl0qnenmif3"); //     gvg->g = g;
UNSUPPORTED("5q4sypoeu8fbwv3a2p6qsnq73"); //     gvg->input_filename = fn;
UNSUPPORTED("1wh5jhwi3fb70nrl37aoz6lhj"); //     gvg->graph_index = gidx;
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 6k1gxkaeteh3v4108asx0nu9q
// static graph_t *P_graph




//3 2zkpt5r5hmvqy31vbxai8aoww
// graph_t *gvPluginsGraph(GVC_t *gvc) 
public static Object gvPluginsGraph(Object... arg) {
UNSUPPORTED("aq8xsrhhkbt250zdmff189jej"); // graph_t *gvPluginsGraph(GVC_t *gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eoghsuji192if07hz2zmt1geg"); //     gvg_init(gvc, P_graph, "<internal>", 0);
UNSUPPORTED("5qryvsjfdmb52s891tbejpwi3"); //     return P_graph;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a4vyp310q1ezn1wiiqbhjazfi
// int dotneato_args_initialize(GVC_t * gvc, int argc, char **argv) 
public static Object dotneato_args_initialize(Object... arg) {
UNSUPPORTED("3an9kpb8l897hglulndwlyhmk"); // int dotneato_args_initialize(GVC_t * gvc, int argc, char **argv)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("exs7yxl18noutslfdhd65grcd"); //     char c, *rest, *layout;
UNSUPPORTED("d28hrwkttitp7p3zkyur6josm"); //     const char *val;
UNSUPPORTED("e4nq5nxc3t4f7sn9hms693ro5"); //     int i, v, nfiles;
UNSUPPORTED("h0or3v13348vfl22jqz895yc"); //     unsigned char buf[128];
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("djkz3f3ke85c3ihtck61wzehd"); //     int Kflag = 0;
UNSUPPORTED("e36z5l2h47e3sm6az444bpmte"); //     /* establish if we are running in a CGI environment */
UNSUPPORTED("39kpbo7t3xw42psbqxwyosbtg"); //     HTTPServerEnVar = getenv("SERVER_NAME");
UNSUPPORTED("bjgrdu955j26h6boths39zysy"); //     /* establish Gvfilepath, if any */
UNSUPPORTED("9u1u08bh9yk3m8qjesa9h35o3"); //     Gvfilepath = getenv("GV_FILE_PATH");
UNSUPPORTED("byzhjcmd87bu2q2ifs8d2zqmx"); //     gvc->common.cmdname = dotneato_basename(argv[0]);
UNSUPPORTED("6t7yoiijwsc45jhh2ycc1zvqn"); //     if (gvc->common.verbose) {
UNSUPPORTED("5jlgk53d79be5z8yrpqk31i41"); //         fprintf(stderr, "%s - %s version %s (%s)\n",
UNSUPPORTED("a3fdnva5eaynygwl01w4i14vu"); // 	    gvc->common.cmdname, gvc->common.info[0],
UNSUPPORTED("4cr6o6cpwligpzuiy9go86dtk"); // 	    gvc->common.info[1], gvc->common.info[2]);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ebjtmwuwx6vwhxaswhb2j4mfm"); //     /* configure for available plugins */
UNSUPPORTED("7y1a5ferpdpuzp8lj2nreef7e"); //     /* needs to know if "dot -c" is set (gvc->common.config) */
UNSUPPORTED("1915n665xv0fno6lfzaikw5ml"); //     /* must happen before trying to select any plugins */
UNSUPPORTED("b2umkw2rzz1ig1cngfiht4fmx"); //     if (gvc->common.config) {
UNSUPPORTED("d3di2hukfdei22j9nlhb4lr4i"); //         gvconfig(gvc, gvc->common.config);
UNSUPPORTED("ew35v5jfro4z9mn5cwzl5e0ha"); // 	exit (0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9gg8gbp3jei0upfnr0b5i6fur"); //     /* feed the globals */
UNSUPPORTED("36hh3435f786qyybpu0o87zjv"); //     Verbose = gvc->common.verbose;
UNSUPPORTED("es2j9l0phjktwgdz1y2435lnm"); //     CmdName = gvc->common.cmdname;
UNSUPPORTED("dhvbz69j0rmligcrm9974041x"); //     nfiles = 0;
UNSUPPORTED("d1jh4myxvrwmm9xcl79yh24g1"); //     for (i = 1; i < argc; i++)
UNSUPPORTED("32x1kvhg66oubz0hakj6dvxg1"); // 	if (argv[i] && argv[i][0] != '-')
UNSUPPORTED("5pk2vvpyoy1qbkfwm0d3cqpip"); // 	    nfiles++;
UNSUPPORTED("d4gb5xxnp2a9dqmzsisy3d2x5"); //     gvc->input_filenames = (char **)zmalloc((nfiles + 1)*sizeof(char *));
UNSUPPORTED("dhvbz69j0rmligcrm9974041x"); //     nfiles = 0;
UNSUPPORTED("ci65k77x1b3nq6luu69s87oup"); //     agxbinit(&xb, 128, buf);
UNSUPPORTED("9fp588sbdt939tsh4lldsi78p"); //     for (i = 1; i < argc; i++) {
UNSUPPORTED("71ydjmz8tdkhga4y130hpfzd7"); // 	if (argv[i] && argv[i][0] == '-') {
UNSUPPORTED("a2i31gh8f8d1uzwvazthtdjhl"); // 	    rest = &(argv[i][2]);
UNSUPPORTED("akiijvdhiis6rte3uan48lkio"); // 	    switch (c = argv[i][1]) {
UNSUPPORTED("cnw3cn0y6fyfmhrj9i6zrj7yt"); // 	    case 'G':
UNSUPPORTED("cyapeoqsbt759mwufn37a0j3w"); // 		if (*rest)
UNSUPPORTED("37iemzdcou8tf7mb850gmys6k"); // 		    global_def(&xb, rest, AGRAPH, agattr);
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("d10434bczuxvbju6r580xu4i3"); // 		    fprintf(stderr, "Missing argument for -G flag\n");
UNSUPPORTED("3j6l9hq73a342kljq6expow6m"); // 		    return (dotneato_usage(1));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("3za1kdrr0abcgx59eek9sst25"); // 	    case 'N':
UNSUPPORTED("cyapeoqsbt759mwufn37a0j3w"); // 		if (*rest)
UNSUPPORTED("22su7vu663f22bni5gx0jkxq9"); // 		    global_def(&xb, rest, AGNODE,agattr);
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("14g4xc09ropngbhfr31tffeii"); // 		    fprintf(stderr, "Missing argument for -N flag\n");
UNSUPPORTED("3j6l9hq73a342kljq6expow6m"); // 		    return (dotneato_usage(1));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("30903uov1ouylqet6qkn3k0rw"); // 	    case 'E':
UNSUPPORTED("cyapeoqsbt759mwufn37a0j3w"); // 		if (*rest)
UNSUPPORTED("b73i9nd8mv1m5tjqoqs0xawyw"); // 		    global_def(&xb, rest, AGEDGE,agattr);
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("6utrckluwkoaluhpksl5aa52s"); // 		    fprintf(stderr, "Missing argument for -E flag\n");
UNSUPPORTED("3j6l9hq73a342kljq6expow6m"); // 		    return (dotneato_usage(1));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("1ve8bjgk9dfpf0eremg7h6xzz"); // 	    case 'T':
UNSUPPORTED("8xdr8a6r0v0ddt027euqcwvue"); // 		val = getFlagOpt(argc, argv, &i);
UNSUPPORTED("3w8hn108291bjaa11z3v4j97d"); // 		if (!val) {
UNSUPPORTED("cd45xgksaxjl5u63gikj5qcyu"); // 		    fprintf(stderr, "Missing argument for -T flag\n");
UNSUPPORTED("3j6l9hq73a342kljq6expow6m"); // 		    return (dotneato_usage(1));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("72i7z40rqqngolv7pgnr45kl3"); // 		v = gvjobs_output_langname(gvc, val);
UNSUPPORTED("9cs6zbfun0bg9dhunbu1dwnox"); // 		if (!v) {
UNSUPPORTED("7ohbl1a39cg7xkg3hactpw7w3"); // 		    fprintf(stderr, "Format: \"%s\" not recognized. Use one of:%s\n",
UNSUPPORTED("f2p3vxh49izcvsl9jvtkf6q3o"); // 			val, gvplugin_list(gvc, API_device, val));
UNSUPPORTED("910dtu59610pevhvj5yhrqcm4"); // 		    if (GvExitOnUsage) exit(1);
UNSUPPORTED("7uqiarbyt9mx4hwdla4nbhj8p"); // 		    return(2);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("8e0kz1b9axy6hx29fg32k1asu"); // 	    case 'K':
UNSUPPORTED("8xdr8a6r0v0ddt027euqcwvue"); // 		val = getFlagOpt(argc, argv, &i);
UNSUPPORTED("3w8hn108291bjaa11z3v4j97d"); // 		if (!val) {
UNSUPPORTED("4387cb0sfakxesew55rctdheb"); //                     fprintf(stderr, "Missing argument for -K flag\n");
UNSUPPORTED("2ns70sizijh2h7z83rt81fqfk"); //                     return (dotneato_usage(1));
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("13d5md8v926ivibrbmgaktksx"); //                 v = gvlayout_select(gvc, val);
UNSUPPORTED("4pu52xhc37cufgh16nc8pjoa2"); //                 if (v == 999) {
UNSUPPORTED("8uoslbuyiw8828cnsd28ys8oh"); // 	            fprintf(stderr, "There is no layout engine support for \"%s\"\n", val);
UNSUPPORTED("em4qxiev3phf1bnbh6vx4zjp9"); //                     if ((*(val)==*("dot")&&!strcmp(val,"dot"))) {
UNSUPPORTED("e46yvd7c19nfgratz9j0sg9d0"); //                         fprintf(stderr, "Perhaps \"dot -c\" needs to be run (with installer's privileges) to register the plugins?\n");
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("cphaexi33y32dnefwtu3jsom4"); // 		    else {
UNSUPPORTED("d3cg95zim5q97685u5v0sxrhv"); //                         fprintf(stderr, "Use one of:%s\n",
UNSUPPORTED("7ced84fhzz8sv21ptj4yf5b3p"); // 				gvplugin_list(gvc, API_layout, val));
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("910dtu59610pevhvj5yhrqcm4"); // 		    if (GvExitOnUsage) exit(1);
UNSUPPORTED("7uqiarbyt9mx4hwdla4nbhj8p"); // 		    return(2);
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("8c0wmxuda35p1as4i2fh9yoti"); // 		Kflag = 1;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("5gsxsxc1w5fdmgnphelmjuqql"); // 	    case 'P':
UNSUPPORTED("91ohbqvqagns01k8geznhjm7k"); // 		P_graph = gvplugin_graph(gvc);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("cxe7ytf67rip7dewog9rnbqqq"); // 	    case 'V':
UNSUPPORTED("61p8yjtqxyg8jhsx9xyqa39my"); // 		fprintf(stderr, "%s - %s version %s (%s)\n",
UNSUPPORTED("chg3zu0nmmc2hpkc8a0cx08er"); // 			gvc->common.cmdname, gvc->common.info[0], 
UNSUPPORTED("b9v3iookta64ex67ies4j4zva"); // 			gvc->common.info[1], gvc->common.info[2]);
UNSUPPORTED("2hk3eyce9u1ys3e3ycfmrtq9n"); // 		if (GvExitOnUsage) exit(0);
UNSUPPORTED("b9uibzxx0tu796r6pqyspuc8u"); // 		return (1);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("8et213nsqt44k6e0d06mh32mg"); // 	    case 'l':
UNSUPPORTED("8xdr8a6r0v0ddt027euqcwvue"); // 		val = getFlagOpt(argc, argv, &i);
UNSUPPORTED("3w8hn108291bjaa11z3v4j97d"); // 		if (!val) {
UNSUPPORTED("3l9adyncbqlq4cr0dn291j8ms"); // 		    fprintf(stderr, "Missing argument for -l flag\n");
UNSUPPORTED("3j6l9hq73a342kljq6expow6m"); // 		    return (dotneato_usage(1));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("a3ei53c2mnxhfpt33rezp6ll1"); // 		use_library(gvc, val);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t4c4wqag0c9inoine0vc6rzh"); // 	    case 'o':
UNSUPPORTED("8xdr8a6r0v0ddt027euqcwvue"); // 		val = getFlagOpt(argc, argv, &i);
UNSUPPORTED("cgclbrsy2pcq9nt94cnmi4l1n"); // 		if (! gvc->common.auto_outfile_names)
UNSUPPORTED("6l1o8s3lihedxdhlhkt8bacw5"); // 		    gvjobs_output_filename(gvc, val);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("3gzpswryl53n5xaxcbut8piyh"); // 	    case 'q':
UNSUPPORTED("55tn4eqemjloic8o06vd4n3nc"); // 		if (*rest) {
UNSUPPORTED("5j8v456fg3eazoh4x59s440ph"); // 		    v = atoi(rest);
UNSUPPORTED("ee277mlx9bo22lecmdsnie12n"); // 		    if (v <= 0) {
UNSUPPORTED("3iwc3dzplzj2jkbze5cd6zfh9"); // 			fprintf(stderr,
UNSUPPORTED("6w3cyan5p5sb01pzz7n8i45h6"); // 				"Invalid parameter \"%s\" for -q flag - ignored\n",
UNSUPPORTED("77gwizewn0zj87535pi2g735m"); // 				rest);
UNSUPPORTED("2ndpjzfiv49aqobcgbi5tftoi"); // 		    } else if (v == 1)
UNSUPPORTED("b4xb9n0clcaf5h0njzxmd6t8u"); // 			agseterr(AGERR);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("eb2xug8syn6gd6cd1ms784rt0"); // 			agseterr(AGMAX);
UNSUPPORTED("738mi6h8ef0itznt34ngxe25o"); // 		} else
UNSUPPORTED("cyu314astki71lyhi8jonkon1"); // 		    agseterr(AGERR);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("9laca56e8dr2klwt5asm5s92v"); // 	    case 's':
UNSUPPORTED("55tn4eqemjloic8o06vd4n3nc"); // 		if (*rest) {
UNSUPPORTED("7p5xijseyywlgq947on87fbfy"); // 		    PSinputscale = atof(rest);
UNSUPPORTED("te6xrfczv0b2rmmfw7n419bj"); // 		    if (PSinputscale < 0) {
UNSUPPORTED("3iwc3dzplzj2jkbze5cd6zfh9"); // 			fprintf(stderr,
UNSUPPORTED("298zr2x6bn7osz168zt1qsgbn"); // 				"Invalid parameter \"%s\" for -s flag\n",
UNSUPPORTED("77gwizewn0zj87535pi2g735m"); // 				rest);
UNSUPPORTED("788fqd2nm2s7cyhjye34lwaho"); // 			return (dotneato_usage(1));
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("e99bugzc8p62vi8asjsx3jnat"); // 		    else if (PSinputscale == 0)
UNSUPPORTED("ca5magegib4z3wn2wbj91xdz5"); // 			PSinputscale = 72;
UNSUPPORTED("738mi6h8ef0itznt34ngxe25o"); // 		} else
UNSUPPORTED("cdz7sxlinpy8lsv4kjrrzvmlb"); // 		    PSinputscale = 72;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("551eo7ey20lfrotadzc8xx636"); // 	    case 'x':
UNSUPPORTED("ciou2ugu3ekwr7d8dtcmo8bqd"); // 		Reduce = NOT(0);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6hwwmvxwrrrsv7qs0y53et76n"); // 	    case 'y':
UNSUPPORTED("71s7bg2w58aqtjmpwed4525kz"); // 		Y_invert = NOT(0);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("eqbveikc3czxh4drpev1uzhla"); // 	    case '?':
UNSUPPORTED("5tdq5fsx232bmgvcnkjmwox6c"); // 		return (dotneato_usage(0));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("bt2g0yhsy3c7keqyftf3c98ut"); // 	    default:
UNSUPPORTED("ex8ddsq0de4n302ieh93s4nrw"); // 		agerr(AGERR, "%s: option -%c unrecognized\n\n", gvc->common.cmdname,
UNSUPPORTED("4fgwtijdvmyysu4tcsnigf36q"); // 			c);
UNSUPPORTED("5mxnk6d8u3qj69z7yzqkphjgw"); // 		return (dotneato_usage(1));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ezl09f02n0cfigsaeyqsejcm0"); // 	} else if (argv[i])
UNSUPPORTED("ez69zldbihwem8y9yr9rmi9gt"); // 	    gvc->input_filenames[nfiles++] = argv[i];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9ocnzhe59r19odwgtedwnydm"); //     agxbfree (&xb);
UNSUPPORTED("56kll2bx8jbmqba2kk0pxvefe"); //     /* if no -K, use cmd name to set layout type */
UNSUPPORTED("dy5okspyylmag8l3ke6of2fps"); //     if (!Kflag) {
UNSUPPORTED("18vaoqlkvxjkxccqkm1cxljuo"); // 	layout = gvc->common.cmdname;
UNSUPPORTED("bshj1mtaoepm94oi9afocf8ou"); // 	if ((*(layout)==*("dot_static")&&!strcmp(layout,"dot_static"))
UNSUPPORTED("64dtzt25t15e7uoo50r0rsefx"); // 	    || (*(layout)==*("dot_builtins")&&!strcmp(layout,"dot_builtins"))
UNSUPPORTED("klg5jjw0m71w4m5shlxzhfjy"); // 	    || (*(layout)==*("lt-dot")&&!strcmp(layout,"lt-dot"))
UNSUPPORTED("42tigt1aywc44r9j37x5jq0ib"); // 	    || (*(layout)==*("lt-dot_builtins")&&!strcmp(layout,"lt-dot_builtins"))
UNSUPPORTED("56pxlfwd1wodkyuswmf36lmwr"); // 	    || (*(layout)==*("")&&!strcmp(layout,""))   /* when run as a process from Gvedit on Windows */
UNSUPPORTED("awdmf39ch8hkgicc7jwv9s67r"); // 	)
UNSUPPORTED("80rf3qgk59flt06kvnzepp9kt"); //             layout = "dot";
UNSUPPORTED("e3pxmvk611turzkqpddzqql3e"); // 	i = gvlayout_select(gvc, layout);
UNSUPPORTED("cbslslfvt4zqfxukzdqeu902c"); // 	if (i == 999) {
UNSUPPORTED("2h041d156jcuzdn0h3t1kxz6b"); // 	    fprintf(stderr, "There is no layout engine support for \"%s\"\n", layout);
UNSUPPORTED("6xr0y24n28bl6fmb7hwi2d6yh"); //             if ((*(layout)==*("dot")&&!strcmp(layout,"dot")))
UNSUPPORTED("a9b0u4vno2ovyayhgdz2qi2l0"); // 		fprintf(stderr, "Perhaps \"dot -c\" needs to be run (with installer's privileges) to register the plugins?\n");
UNSUPPORTED("f3qa0cv737ikcre1vpqlkukio"); // 	    else 
UNSUPPORTED("3oqrxaejbit2ag4yv1f8std7v"); // 		fprintf(stderr, "Use one of:%s\n", gvplugin_list(gvc, API_layout, ""));
UNSUPPORTED("cziimyez7l7opmyxtz7i258x"); // 	    if (GvExitOnUsage) exit(1);
UNSUPPORTED("8u2416o82oso1w72bexmapn9v"); // 	    return(2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e8n0c84rizzjhmsff49m2fynz"); //     /* if no -Txxx, then set default format */
UNSUPPORTED("5atdq1nn8pklea2e61l8ekie"); //     if (!gvc->jobs || !gvc->jobs->output_langname) {
UNSUPPORTED("ejs6fyeynpj73y8zhc2xjcvrc"); // 	v = gvjobs_output_langname(gvc, "dot");
UNSUPPORTED("6lpp7llfms4w364wz03qdlrnl"); // 	if (!v) {
UNSUPPORTED("5di5qeuntrt4eii2azt25l076"); // //	assert(v);  /* "dot" should always be available as an output format */
UNSUPPORTED("18zn34qcs4vsdhhh831gn9vc9"); // 		fprintf(stderr,
UNSUPPORTED("5rhyltg4walgso272exe4gdqz"); // 			"Unable to find even the default \"-Tdot\" renderer.  Has the config\nfile been generated by running \"dot -c\" with installer's priviledges?\n");
UNSUPPORTED("3r5dyo5vxrzten0rhlmlmhe8v"); // 			return(2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1i3ejmrslphirq6u7onu2i3cr"); //     /* set persistent attributes here (if not already set from command line options) */
UNSUPPORTED("et68lvyh6row6cmvnxmw4nuvj"); //     if (!agattr(NULL, AGNODE, "label", 0))
UNSUPPORTED("74v5uwoisv6m2lnnjv33219om"); // 	agattr(NULL, AGNODE, "label", "\\N");
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 72no6ayfvjinlnupyn5jlmayg
// static boolean getdoubles2ptf(graph_t * g, char *name, pointf * result) 
public static boolean getdoubles2ptf(ST_Agraph_s g, CString name, ST_pointf result) {
ENTERING("72no6ayfvjinlnupyn5jlmayg","getdoubles2ptf");
try {
    CString p;
    int i;
    double xf, yf;
    char c = '\0';
    boolean rv = false;
    if ((p = agget(g, name))!=null) {
UNSUPPORTED("21b2kes0vrizyai71yj9e2os3"); // 	i = sscanf(p, "%lf,%lf%c", &xf, &yf, &c);
UNSUPPORTED("9wua6uiybfvqd70huuo0yatcf"); // 	if ((i > 1) && (xf > 0) && (yf > 0)) {
UNSUPPORTED("8z2huopqt4m1rvfcd7vqatka4"); // 	    result->x = ((((xf)*72>=0)?(int)((xf)*72 + .5):(int)((xf)*72 - .5)));
UNSUPPORTED("cil4j0n3iq35gr2pfewi2qawz"); // 	    result->y = ((((yf)*72>=0)?(int)((yf)*72 + .5):(int)((yf)*72 - .5)));
UNSUPPORTED("9qnr8qmbz7pf3mmpebux0p08m"); // 	    if (c == '!')
UNSUPPORTED("dqyb6drzg8ig5ecb31fq5c1d4"); // 		rv = (!(0));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("8wtaqjit9awt7xd08vuifknry"); // 	    c = '\0';
UNSUPPORTED("705372l4htjtcvnq97l7i54g8"); // 	    i = sscanf(p, "%lf%c", &xf, &c);
UNSUPPORTED("4n9k1twwfmxyet8tokr7xnktj"); // 	    if ((i > 0) && (xf > 0)) {
UNSUPPORTED("8ui53rmpq7ao1p4yin0xqzszj"); // 		result->y = result->x = ((((xf)*72>=0)?(int)((xf)*72 + .5):(int)((xf)*72 - .5)));
UNSUPPORTED("1rflva1x66uhyqxr5zbpcsgnh"); // 		if (c == '!') rv = (!(0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    return rv;
} finally {
LEAVING("72no6ayfvjinlnupyn5jlmayg","getdoubles2ptf");
}
}




//3 1xg46gdvtsko1yrtm6mg4tsxy
// void getdouble(graph_t * g, char *name, double *result) 
public static Object getdouble(Object... arg) {
UNSUPPORTED("5gfb0pnjet6us7l51d48x25aq"); // void getdouble(graph_t * g, char *name, double *result)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("jnku6gn089m43hq5hndzrxzn"); //     double f;
UNSUPPORTED("bifb8kht3vkytb74qbof9vpob"); //     if ((p = agget(g, name))) {
UNSUPPORTED("4r30fz6hpqhfj44lip5cndh1m"); // 	if (sscanf(p, "%lf", &f) >= 1)
UNSUPPORTED("jepdvpsjq4757gzwaplghh4j"); // 	    *result = f;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7c1tanyo6vwej9cqo0rkiv6sv
// graph_t *gvNextInputGraph(GVC_t *gvc) 
public static Object gvNextInputGraph(Object... arg) {
UNSUPPORTED("a6jdteesa5ifdtthxxsohrlh2"); // graph_t *gvNextInputGraph(GVC_t *gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ccvkc7reh332l10k91bjvksnm"); //     graph_t *g = NULL;
UNSUPPORTED("5dpauyujvamkm0ay3pfh999y3"); //     static char *fn;
UNSUPPORTED("46orciiuryyogkvndndbawo06"); //     static FILE *fp;
UNSUPPORTED("82yfc13etao3sz5hqypnt56oq"); //     static FILE *oldfp;
UNSUPPORTED("1c51f3lle32l3xcfnkzig5ett"); //     static int fidx, gidx;
UNSUPPORTED("6i509d0s1nqxjr873r5dz7gv5"); //     while (!g) {
UNSUPPORTED("56tws2uz7mqhxwswpbpf94b5c"); // 	if (!fp) {
UNSUPPORTED("6d4ms2m7wzcyf2eofwsoz7jzu"); //     	    if (!(fn = gvc->input_filenames[0])) {
UNSUPPORTED("eec7y1e55sjjkrx06jmtoyrz1"); // 		if (fidx++ == 0)
UNSUPPORTED("sln8j5e1981v4p6fvxyy4jjq"); // 		    fp = stdin;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("btttznywgnyh5niqc16ebuucw"); // 		while ((fn = gvc->input_filenames[fidx++]) && !(fp = fopen(fn, "r")))  {
UNSUPPORTED("4futxtc5kgl4i6bw6j1xhws4s"); // 		    agerr(AGERR, "%s: can't open %s\n", gvc->common.cmdname, fn);
UNSUPPORTED("o7u2b38bnefhf1l58zkel4i3"); // 		    graphviz_errors++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("74qpksqxqa1hxoxfw5ugamyww"); // 	if (fp == NULL)
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2euu5u83dzpauthvjfy4vlcxg"); // 	if (oldfp != fp) {
UNSUPPORTED("cdwz1axrp68a13bwv1la3a736"); // 	    agsetfile(fn ? fn : "<stdin>");
UNSUPPORTED("36hhlg0nbd0exjvtbe0fc5gj6"); // 	    oldfp = fp;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("b1s6sspge1n2c2b0yukawa8jw"); // 	g = agread(fp,((Agdisc_t*)0));
UNSUPPORTED("wx1q1tyb5r9oziojtpc4vd1n"); // 	if (g) {
UNSUPPORTED("8r806yndx1ticudcknc3r1sp2"); // 	    gvg_init(gvc, g, fn, gidx++);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7oaqgqiffegej6sz73ow4cwtw"); // 	if (fp != stdin)
UNSUPPORTED("caiflnlhuyqft76qr8gx91bf3"); // 	    fclose (fp);
UNSUPPORTED("7y7knbs9950t3udidyrln8lmp"); // 	fp = NULL;
UNSUPPORTED("ecnsdkjxzhqh68kkz6fpbez04"); // 	gidx = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2syri7q5tc0jyvwq8ecyfo3vr"); //     return g;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9t08dr2ks9qz1pyfz99awla6x
// static int findCharset (graph_t * g) 
public static int findCharset(ST_Agraph_s g) {
ENTERING("9t08dr2ks9qz1pyfz99awla6x","findCharset");
try {
	return 0;
} finally {
LEAVING("9t08dr2ks9qz1pyfz99awla6x","findCharset");
}
}




//3 3bnmjpvynh1j9oh2p2vi0vh2m
// static void setRatio(graph_t * g) 
public static void setRatio(ST_Agraph_s g) {
ENTERING("3bnmjpvynh1j9oh2p2vi0vh2m","setRatio");
try {
    CString p;
    char c;
    double ratio;
    if ((p = agget(g, new CString("ratio")))!=null && ((c = p.charAt(0))!='\0')) {
UNSUPPORTED("7rk995hpmaqbbasmi40mqg0yw"); // 	switch (c) {
UNSUPPORTED("2v5u3irq50r1n2ccuna0y09lk"); // 	case 'a':
UNSUPPORTED("3jv8xrrloj92axkpkgolzwgo6"); // 	    if ((*(p)==*("auto")&&!strcmp(p,"auto")))
UNSUPPORTED("8bdbsrt9sk4hnj3wm6z100qm"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_AUTO;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f3lyz2cejs6yn5fyckhn7ba1"); // 	case 'c':
UNSUPPORTED("1v3jyjziibgnha1glbymorwg1"); // 	    if ((*(p)==*("compress")&&!strcmp(p,"compress")))
UNSUPPORTED("coprfqf41n6byzz3nfneke6a"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_COMPRESS;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2fzjr952o6hmcz3ad5arl2n8d"); // 	case 'e':
UNSUPPORTED("5s06nikh994hgncpwni2p4rwq"); // 	    if ((*(p)==*("expand")&&!strcmp(p,"expand")))
UNSUPPORTED("eanijnkdjj1f6q7su4gmmijpj"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_EXPAND;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8jntw084f69528np3kisw5ioc"); // 	case 'f':
UNSUPPORTED("105p0jwfnsptmrweig5mhpkn9"); // 	    if ((*(p)==*("fill")&&!strcmp(p,"fill")))
UNSUPPORTED("eknfh3axjhorf2rfb914hdgbd"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_FILL;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("e4fr8djxwn615yr0rj46vtdbd"); // 	    ratio = atof(p);
UNSUPPORTED("43a0ik2dkpg3y58orisgkn32q"); // 	    if (ratio > 0.0) {
UNSUPPORTED("azv56xi8njootl2n9l5bm1udc"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_VALUE;
UNSUPPORTED("ch5o67mezsw0v6iwxylb98myn"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio = ratio;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
    }
} finally {
LEAVING("3bnmjpvynh1j9oh2p2vi0vh2m","setRatio");
}
}




static CString rankname[] = new CString[] { new CString("local"), new CString("global"), new CString("none"), null };
static int rankcode[] = { 100, 101, 102, 100 };
static CString fontnamenames[] = new CString[] {new CString("gd"),new CString("ps"),new CString("svg"), null};
static int fontnamecodes[] = {enumAsInt(fontname_kind.class, "NATIVEFONTS"),enumAsInt(fontname_kind.class, "PSFONTS"),
enumAsInt(fontname_kind.class, "SVGFONTS"),-1};
//3 8gzdr3oil2d0e2o7m84wsszfg
//void graph_init(graph_t * g, boolean use_rankdir) 
public static void graph_init(ST_Agraph_s g, boolean use_rankdir) {
ENTERING("8gzdr3oil2d0e2o7m84wsszfg","graph_init");
try {
    CString p;
    double xf;
    int rankdir;
    GD_drawing(g, new ST_layout_t());
    /* set this up fairly early in case any string sizes are needed */
    if ((p = agget(g, new CString("fontpath")))!=null || (p = getenv(new CString("DOTFONTPATH")))!=null) {
UNSUPPORTED("81bz3jcukzyotxiqgrlhn9cbq"); // 	/* overide GDFONTPATH in local environment if dot
UNSUPPORTED("6jgl7atk1m9yeam4auh127azw"); // 	 * wants its own */
UNSUPPORTED("dyk0vc64gdzy1uwvsc2jqnjdw"); // 	static char *buf = 0;
UNSUPPORTED("8dywgree8jdjmj2ll2whbekhe"); // 	buf = grealloc(buf, strlen("GDFONTPATH=") + strlen(p) + 1);
UNSUPPORTED("d9ej6bo2s49vpstu3pql6tkrx"); // 	strcpy(buf, "GDFONTPATH=");
UNSUPPORTED("1s2jcd2h3eok7j6pclv20gyi2"); // 	strcat(buf, p);
UNSUPPORTED("abkxekvux4nramryfw2e8vcru"); // 	putenv(buf);
    }
    GD_charset(g, findCharset (g));
    /*if (!HTTPServerEnVar) {
	Gvimagepath = agget (g, "imagepath");
	if (!Gvimagepath)
	    Gvimagepath = Gvfilepath;
    }*/
    GD_drawing(g).setDouble("quantum",
 	late_double(g, (agattr(g,AGRAPH,new CString("quantum"),null)), 0.0, 0.0));
    /* setting rankdir=LR is only defined in dot,
     * but having it set causes shape code and others to use it. 
     * The result is confused output, so we turn it off unless requested.
     * This effective rankdir is stored in the bottom 2 bits of g->u.rankdir.
     * Sometimes, the code really needs the graph's rankdir, e.g., neato -n
     * with record shapes, so we store the real rankdir in the next 2 bits.
     */
    rankdir = 0;
    if ((p = agget(g, new CString("rankdir")))!=null) {
UNSUPPORTED("sp7zcza7w0dn7t66aj8rf4wn"); // 	if ((*(p)==*("LR")&&!strcmp(p,"LR")))
UNSUPPORTED("bjd2vk1jssqehllmgnqv601qd"); // 	    rankdir = 1;
UNSUPPORTED("ry8itlrmblmuegdwk1iu1t0x"); // 	else if ((*(p)==*("BT")&&!strcmp(p,"BT")))
UNSUPPORTED("5hno0xn18yt443qg815w3c2s2"); // 	    rankdir = 2;
UNSUPPORTED("aal39mi047mhafrsrxoutcffk"); // 	else if ((*(p)==*("RL")&&!strcmp(p,"RL")))
UNSUPPORTED("7vlda224wrgcdhr0ts3mndh5q"); // 	    rankdir = 3;
    }
    if (use_rankdir)
	GD_rankdir2(g, (rankdir << 2) | rankdir);
    else
	GD_rankdir2(g, (rankdir << 2));
    xf = late_double(g, (agattr(g,AGRAPH,new CString("nodesep"),null)),
		0.25, 0.02);
    GD_nodesep(g, (ROUND((xf)*72)));
    p = late_string(g, (agattr(g,AGRAPH,new CString("ranksep"),null)), null);
    if (p!=null) {
UNSUPPORTED("c3p25g4289dxlei062z4eflss"); // 	if (sscanf(p, "%lf", &xf) == 0)
UNSUPPORTED("570vljex12zx5dkwi7mqa9knw"); // 	    xf = 0.5;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("p882lodfwy5v48rwbxvg5s9i"); // 	    if (xf < 0.02)
UNSUPPORTED("dhhbmqv6n01j1eeyy7fpus1xw"); // 		xf = 0.02;
	if (strstr(p, new CString("equally"))!=null)
	    GD_exact_ranksep(g, 1);
    } else
	xf = 0.5;
    GD_ranksep(g, (ROUND((xf)*72)));
    GD_showboxes(g, late_int(g, (agattr(g,AGRAPH,new CString("showboxes"),null)), 0, 0));
    p = late_string(g, (agattr(g,AGRAPH,new CString("fontnames"),null)), null);
    GD_fontnames(g, maptoken(p, fontnamenames, fontnamecodes));
    setRatio(g);
    GD_drawing(g).filled = 
	getdoubles2ptf(g, new CString("size"), (ST_pointf) GD_drawing(g).size);
    getdoubles2ptf(g, new CString("page"), (ST_pointf) GD_drawing(g).page);
    GD_drawing(g).centered = mapbool(agget(g, new CString("center")));
    if ((p = agget(g, new CString("rotate")))!=null)
	GD_drawing(g).landscape= (atoi(p) == 90);
    else if ((p = agget(g, new CString("orientation")))!=null)
	GD_drawing(g).landscape= ((p.charAt(0) == 'l') || (p.charAt(0) == 'L'));
    else if ((p = agget(g, new CString("landscape")))!=null)
	GD_drawing(g).landscape= mapbool(p);
    p = agget(g, new CString("clusterrank"));
    Z.z().CL_type = maptoken(p, rankname, rankcode);
    p = agget(g, new CString("concentrate"));
    Z.z().Concentrate = mapbool(p);
    Z.z().State = 0;
    Z.z().EdgeLabelsDone = 0;
    GD_drawing(g).setDouble("dpi", 0.0);
    if (((p = agget(g, new CString("dpi")))!=null && p.charAt(0)!='\0')
	|| ((p = agget(g, new CString("resolution")))!=null && p.charAt(0)!='\0'))
	GD_drawing(g).setDouble("dpi", atof(p));
    do_graph_label(g);
    Z.z().Initial_dist = (1.0e+37);
    Z.z().G_ordering = (agattr(g,AGRAPH,new CString("ordering"),null));
    Z.z().G_gradientangle = (agattr(g,AGRAPH,new CString("gradientangle"),null));
    Z.z().G_margin = (agattr(g,AGRAPH,new CString("margin"),null));
    /* initialize nodes */
    Z.z().N_height = (agattr(g,AGNODE,new CString("height"),null));
    Z.z().N_width = (agattr(g,AGNODE,new CString("width"),null));
    Z.z().N_shape = (agattr(g,AGNODE,new CString("shape"),null));
    Z.z().N_color = (agattr(g,AGNODE,new CString("color"),null));
    Z.z().N_fillcolor = (agattr(g,AGNODE,new CString("fillcolor"),null));
    Z.z().N_style = (agattr(g,AGNODE,new CString("style"),null));
    Z.z().N_fontsize = (agattr(g,AGNODE,new CString("fontsize"),null));
    Z.z().N_fontname = (agattr(g,AGNODE,new CString("fontname"),null));
    Z.z().N_fontcolor = (agattr(g,AGNODE,new CString("fontcolor"),null));
    Z.z().N_label = (agattr(g,AGNODE,new CString("label"),null));
    if (N(Z.z().N_label))
	Z.z().N_label = agattr(g, AGNODE, new CString("label"), new CString("\\N"));
    Z.z().N_xlabel = (agattr(g,AGNODE,new CString("xlabel"),null));
    Z.z().N_showboxes = (agattr(g,AGNODE,new CString("showboxes"),null));
    Z.z().N_penwidth = (agattr(g,AGNODE,new CString("penwidth"),null));
    Z.z().N_ordering = (agattr(g,AGNODE,new CString("ordering"),null));
    Z.z().N_margin = (agattr(g,AGNODE,new CString("margin"),null));
    /* attribs for polygon shapes */
    Z.z().N_sides = (agattr(g,AGNODE,new CString("sides"),null));
    Z.z().N_peripheries = (agattr(g,AGNODE,new CString("peripheries"),null));
    Z.z().N_skew = (agattr(g,AGNODE,new CString("skew"),null));
    Z.z().N_orientation = (agattr(g,AGNODE,new CString("orientation"),null));
    Z.z().N_distortion = (agattr(g,AGNODE,new CString("distortion"),null));
    Z.z().N_fixed = (agattr(g,AGNODE,new CString("fixedsize"),null));
    Z.z().N_imagescale = (agattr(g,AGNODE,new CString("imagescale"),null));
    Z.z().N_nojustify = (agattr(g,AGNODE,new CString("nojustify"),null));
    Z.z().N_layer = (agattr(g,AGNODE,new CString("layer"),null));
    Z.z().N_group = (agattr(g,AGNODE,new CString("group"),null));
    Z.z().N_comment = (agattr(g,AGNODE,new CString("comment"),null));
    Z.z().N_vertices = (agattr(g,AGNODE,new CString("vertices"),null));
    Z.z().N_z = (agattr(g,AGNODE,new CString("z"),null));
    Z.z().N_gradientangle = (agattr(g,AGNODE,new CString("gradientangle"),null));
    /* initialize edges */
    Z.z().E_weight = (agattr(g,AGEDGE,new CString("weight"),null));
    Z.z().E_color = (agattr(g,AGEDGE,new CString("color"),null));
    Z.z().E_fillcolor = (agattr(g,AGEDGE,new CString("fillcolor"),null));
    Z.z().E_fontsize = (agattr(g,AGEDGE,new CString("fontsize"),null));
    Z.z().E_fontname = (agattr(g,AGEDGE,new CString("fontname"),null));
    Z.z().E_fontcolor = (agattr(g,AGEDGE,new CString("fontcolor"),null));
    Z.z().E_label = (agattr(g,AGEDGE,new CString("label"),null));
    Z.z().E_xlabel = (agattr(g,AGEDGE,new CString("xlabel"),null));
    Z.z().E_label_float = (agattr(g,AGEDGE,new CString("labelfloat"),null));
    /* vladimir */
    Z.z().E_dir = (agattr(g,AGEDGE,new CString("dir"),null));
    Z.z().E_arrowhead = (agattr(g,AGEDGE,new CString("arrowhead"),null));
    Z.z().E_arrowtail = (agattr(g,AGEDGE,new CString("arrowtail"),null));
    Z.z().E_headlabel = (agattr(g,AGEDGE,new CString("headlabel"),null));
    Z.z().E_taillabel = (agattr(g,AGEDGE,new CString("taillabel"),null));
    Z.z().E_labelfontsize = (agattr(g,AGEDGE,new CString("labelfontsize"),null));
    Z.z().E_labelfontname = (agattr(g,AGEDGE,new CString("labelfontname"),null));
    Z.z().E_labelfontcolor = (agattr(g,AGEDGE,new CString("labelfontcolor"),null));
    Z.z().E_labeldistance = (agattr(g,AGEDGE,new CString("labeldistance"),null));
    Z.z().E_labelangle = (agattr(g,AGEDGE,new CString("labelangle"),null));
    /* end vladimir */
    Z.z().E_minlen = (agattr(g,AGEDGE,new CString("minlen"),null));
    Z.z().E_showboxes = (agattr(g,AGEDGE,new CString("showboxes"),null));
    Z.z().E_style = (agattr(g,AGEDGE,new CString("style"),null));
    Z.z().E_decorate = (agattr(g,AGEDGE,new CString("decorate"),null));
    Z.z().E_arrowsz = (agattr(g,AGEDGE,new CString("arrowsize"),null));
    Z.z().E_constr = (agattr(g,AGEDGE,new CString("constraint"),null));
    Z.z().E_layer = (agattr(g,AGEDGE,new CString("layer"),null));
    Z.z().E_comment = (agattr(g,AGEDGE,new CString("comment"),null));
    Z.z().E_tailclip = (agattr(g,AGEDGE,new CString("tailclip"),null));
    Z.z().E_headclip = (agattr(g,AGEDGE,new CString("headclip"),null));
    Z.z().E_penwidth = (agattr(g,AGEDGE,new CString("penwidth"),null));
    /* background */
    GD_drawing(g).setPtr("xdots", init_xdot (g));
    /* initialize id, if any */
    if ((p = agget(g, new CString("id")))!=null && p.charAt(0)!='\0')
	GD_drawing(g).setPtr("id", strdup_and_subst_obj(p, g));
} finally {
LEAVING("8gzdr3oil2d0e2o7m84wsszfg","graph_init");
}
}




//3 46ypwxxdurpwoq7ee0nagnyuw
// void graph_cleanup(graph_t *g) 
public static Object graph_cleanup(Object... arg) {
UNSUPPORTED("30nwbe5cpmxhh80h8xa9akr9z"); // void graph_cleanup(graph_t *g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dnom8brm7mdyz49mlyew1yfx4"); //     if (GD_drawing(g) && GD_drawing(g)->xdots)
UNSUPPORTED("cbn0kehrijve4p68esddyi4cm"); // 	freeXDot ((xdot*)GD_drawing(g)->xdots);
UNSUPPORTED("4wfyhel6dchugc6m03gzcaqqx"); //     if (GD_drawing(g) && GD_drawing(g)->id)
UNSUPPORTED("3uhbrv39ml1lee2b5i24tnmp2"); // 	free (GD_drawing(g)->id);
UNSUPPORTED("vcg73wzydblsuguqzall9cv4"); //     free(GD_drawing(g));
UNSUPPORTED("1ia1a125sivdblphtrgblo6nr"); //     GD_drawing(g) = NULL;
UNSUPPORTED("amdwcc4txs1rjdj436t6qt2k4"); //     free_label(GD_label(g));
UNSUPPORTED("8jf0pz51pmyvkml9d1jqhncju"); //     //FIX HERE , STILL SHALLOW
UNSUPPORTED("32tijapsyiumwfmjqrf8j6d41"); //     //memset(&(g->u), 0, sizeof(Agraphinfo_t));
UNSUPPORTED("7e4eo7ldxaf48s2v3paft8j2c"); //     agclean(g, AGRAPH,"Agraphinfo_t");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7rzv30lub416sffko0du3o6sx
// char* charsetToStr (int c) 
public static Object charsetToStr(Object... arg) {
UNSUPPORTED("cqm25rponse4rsi686sbn1lo0"); // char*
UNSUPPORTED("b1ttom615vlztws5drinv8k4i"); // charsetToStr (int c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cypok90bpbt6z74ak3nu63g1m"); //    char* s;
UNSUPPORTED("239qe3atroys6jen2eufic7ex"); //    switch (c) {
UNSUPPORTED("1nhgtydm95uz0oftevo3oly8e"); //    case 0 :
UNSUPPORTED("2nidjssyf3n7w7cygka1k20t7"); // 	s = "UTF-8";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("6152devym3begeqtwle6okwtn"); //    case 1 :
UNSUPPORTED("ct1k13idag6941hvbi9y2bzt3"); // 	s = "ISO-8859-1";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("brmutjgcyjq57ggmjk11na8lu"); //    case 2 :
UNSUPPORTED("5irze7y061rfoysvsbc01net8"); // 	s = "BIG-5";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("cjimoqzt0qz3wos8m9h7g3hmh"); //    default :
UNSUPPORTED("816pcwbgdg9rau7jfcj6xpoel"); // 	agerr(AGERR, "Unsupported charset value %d\n", c);
UNSUPPORTED("2nidjssyf3n7w7cygka1k20t7"); // 	s = "UTF-8";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("67k63k77j3kjabivb0i8hxrwd"); //    }
UNSUPPORTED("dyq366cow9q7c8bh5jns3dlqo"); //    return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5vks1zdadu5vjinaivs0j2bkb
// void do_graph_label(graph_t * sg) 
public static void do_graph_label(ST_Agraph_s  sg) {
ENTERING("5vks1zdadu5vjinaivs0j2bkb","do_graph_label");
try {
    CString str, pos, just;
    int pos_ix;
    /* it would be nice to allow multiple graph labels in the future */
    if ((str = agget(sg, new CString("label")))!=null && (str.charAt(0) != '\0')) {
	char pos_flag=0;
	final ST_pointf dimen = new ST_pointf();
	GD_has_labels(sg.root, GD_has_labels(sg.root) | (1 << 3));
	GD_label(sg, make_label(sg, str, (aghtmlstr(str)!=0 ? (1 << 1) : (0 << 1)),
	    late_double(sg, (agattr(sg,AGRAPH,new CString("fontsize"),null)),
			14.0, 1.0),
	    late_nnstring(sg, (agattr(sg,AGRAPH,new CString("fontname"),null)),
			new CString("Times-Roman")),
	    late_nnstring(sg, (agattr(sg,AGRAPH,new CString("fontcolor"),null)),
			new CString("black"))));
	/* set label position */
	pos = agget(sg, new CString("labelloc"));
	if (NEQ(sg, agroot(sg))) {
	    if (pos!=null && (pos.charAt(0) == 'b'))
		pos_flag = 0;
	    else
		pos_flag = 1;
	} else {
UNSUPPORTED("601b6yrqr391vnfpa74d7fec7"); // 	    if (pos && (pos[0] == 't'))
UNSUPPORTED("bxai2kktsidvda3696ctyk63c"); // 		pos_flag = 1;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("6m5sy5ew8izdy8i10zb5o2dvu"); // 		pos_flag = 0;
	}
	just = agget(sg, new CString("labeljust"));
	if (just!=null) {
UNSUPPORTED("3gxohpfqzahytaf7f9apn58az"); // 	    if (just[0] == 'l')
UNSUPPORTED("ch7sydr4cg29o8ky9fbk5vnlg"); // 		pos_flag |= 2;
UNSUPPORTED("336to8kpmovx00pexhhenz74b"); // 	    else if (just[0] == 'r')
UNSUPPORTED("evu9w6pw3kkh7z8w7t4rx4qxc"); // 		pos_flag |= 4;
	}
	GD_label_pos(sg, pos_flag);
	if (EQ(sg, agroot(sg)))
	    return;
	/* Set border information for cluster labels to allow space
	 */
	dimen.___(GD_label(sg).dimen);
	dimen.setDouble("x", dimen.x + 4*4);
	dimen.setDouble("y", dimen.y + 2*4);
	if (N(GD_flip(agroot(sg)))) {
	    if ((GD_label_pos(sg) & 1)!=0)
		pos_ix = 2;
	    else
		pos_ix = 0;
	    GD_border(sg)[pos_ix].___(dimen);
	} else {
	    /* when rotated, the labels will be restored to TOP or BOTTOM  */
UNSUPPORTED("cabz6xbjdvz5vmjulzrhlxh48"); // 	    if ((((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->label_pos) & 1)
UNSUPPORTED("dx7v6663o9o0x1j5r8z4wumxb"); // 		pos_ix = 1;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("97dtv6k7yw1qvfzgs65cj2v0l"); // 		pos_ix = 3;
UNSUPPORTED("21iuie8b11x65je8vampstgt6"); // 	    (((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->border)[pos_ix].x = dimen.y;
UNSUPPORTED("8cawl3kik853hkvgm39y34urs"); // 	    (((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->border)[pos_ix].y = dimen.x;
	}
    }
} finally {
LEAVING("5vks1zdadu5vjinaivs0j2bkb","do_graph_label");
}
}


}
