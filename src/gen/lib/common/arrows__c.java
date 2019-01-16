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
import static gen.lib.cgraph.attr__c.agxget;
import static gen.lib.cgraph.graph__c.agisdirected;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.splines__c.bezier_clip;
import static gen.lib.common.utils__c.late_double;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.function;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.DIST2;
import static smetana.core.Macro.ED_conc_opp_flag;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_arrowdir_t;
import h.ST_arrowname_t;
import h.ST_bezier;
import h.ST_inside_t;
import h.ST_pointf;
import smetana.core.CString;
import smetana.core.MutableDouble;
import smetana.core.Z;

public class arrows__c {
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


//1 8g6b12c9lhliz7wt1b4g6ol4q
// static arrowdir_t Arrowdirs[] = 


//1 bh7utclfhqwp6bpy5c8g693wa
// static arrowname_t Arrowsynonyms[] = 
/*private static final __array_of_struct__ Arrowsynonyms = __array_of_struct__.malloc(arrowname_t.class, 1);
static {
	Arrowsynonyms.plus(0).setStruct(create_arrowname_t(null, 0));
}*/

//1 5yb25qm8n3hiqb86lalpa6q5n
// static arrowname_t Arrowmods[] = 
/*private static final __array_of_struct__ Arrowmods = __array_of_struct__.malloc(arrowname_t.class, 1);
static {
	Arrowmods.plus(0).setStruct(create_arrowname_t(null, 0));
}*/

//1 e8dhqbfo267go86knqiqq0dnv
// static arrowname_t Arrownames[] = 
/*private static final __array_of_struct__ Arrownames = __array_of_struct__.malloc(arrowname_t.class, 3);
static {
	Arrownames.plus(0).setStruct(create_arrowname_t("normal", 1));
	Arrownames.plus(1).setStruct(create_arrowname_t("none", 8));
	Arrownames.plus(2).setStruct(create_arrowname_t(null, 0));
}
private final static __struct__ create_arrowname_t(String name, int type) {
	final __struct__<arrowname_t> result = JUtils.from(arrowname_t.class);
	result.setCString("name", name==null?null:new CString(name));
	result.setInt("type", type);
	return result;
}*/
//1 dnirq5m2r8c2mep5o1m3cdn6d
// static arrowtype_t Arrowtypes[] = 
/*private static final __array_of_struct__ Arrowtypes = __array_of_struct__.malloc(arrowtype_t.class, 9);
static {
	Arrowtypes.plus(0).setStruct(createArrowtypes(1, 1.0, function(arrows__c.class, "arrow_type_normal")));
	Arrowtypes.plus(1).setStruct(createArrowtypes(2, 1.0, function(arrows__c.class, "arrow_type_crow")));
	Arrowtypes.plus(2).setStruct(createArrowtypes(3, 0.5, function(arrows__c.class, "arrow_type_tee")));
	Arrowtypes.plus(3).setStruct(createArrowtypes(4, 1.0, function(arrows__c.class, "arrow_type_box")));
	Arrowtypes.plus(4).setStruct(createArrowtypes(5, 1.2, function(arrows__c.class, "arrow_type_diamond")));
	Arrowtypes.plus(5).setStruct(createArrowtypes(6, 0.8, function(arrows__c.class, "arrow_type_dot")));
	Arrowtypes.plus(6).setStruct(createArrowtypes(7, 1.0, function(arrows__c.class, "arrow_type_curve")));
	Arrowtypes.plus(7).setStruct(createArrowtypes(8, 0.5, function(arrows__c.class, "arrow_type_gap")));
	Arrowtypes.plus(8).setStruct(createArrowtypes(0, 0.0, null));
}
private final static __struct__ createArrowtypes(int type, double lenfact, CFunction function) {
	final __struct__<arrowtype_t> result = JUtils.from(arrowtype_t.class);
	result.setInt("type", type);
	result.setDouble("lenfact", lenfact);
	result.setPtr("gen", function);
	return result;
}*/


//3 3apnay8wumntfkvud64ov7fcf
// static char *arrow_match_name_frag(char *name, arrowname_t * arrownames, int *flag) 
public static CString arrow_match_name_frag(CString name, ST_arrowname_t[] arrowsynonyms, int flag[]) {
ENTERING("3apnay8wumntfkvud64ov7fcf","arrow_match_name_frag");
try {
 UNSUPPORTED("cw8t22aa6zs16jqowqjjkzywg"); // static char *arrow_match_name_frag(char *name, arrowname_t * arrownames, int *flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ij5y28mkncrjeiqcpshx0eb6"); //     arrowname_t *arrowname;
UNSUPPORTED("1tn1krtup6qe1swed3vb7rsyl"); //     int namelen = 0;
UNSUPPORTED("c0vrdgjia18jvvw01f49sovz5"); //     char *rest = name;
UNSUPPORTED("ecwk85uixpdt1xvlwr1rw58a4"); //     for (arrowname = arrownames; arrowname->name; arrowname++) {
UNSUPPORTED("9h58czuqvp8q45izpqt7dzgi6"); // 	namelen = strlen(arrowname->name);
UNSUPPORTED("9fd8hjdir8m00yuhi9anrrnos"); // 	if (strncmp(name, arrowname->name, namelen) == 0) {
UNSUPPORTED("ag3b2jixanemgvefu1c01mp6u"); // 	    *flag |= arrowname->type;
UNSUPPORTED("1h43j4ja8m8uxuvay0jg33ukm"); // 	    rest += namelen;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bbweh79ihpurvsz097xab3u5k"); //     return rest;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("3apnay8wumntfkvud64ov7fcf","arrow_match_name_frag");
}
}




//3 b669zec8aznq4obnil98j5lby
// static char *arrow_match_shape(char *name, int *flag) 
public static CString arrow_match_shape(CString name, int flag[]) {
ENTERING("b669zec8aznq4obnil98j5lby","arrow_match_shape");
try {
    CString next, rest;
    int f[] = new int[] {0};
    rest = arrow_match_name_frag(name, Z.z().Arrowsynonyms, f);
UNSUPPORTED("304yfmlt3qwn4zydpx1hgmf5o"); //     if (rest == name) {
UNSUPPORTED("8vxyvy38lzpbd83cu26nejaan"); // 	do {
UNSUPPORTED("do0zgfzipmk0sgv0q0u14es1c"); // 	    next = rest;
UNSUPPORTED("csdu3mgjv4ya6xqk2bisko4gp"); // 	    rest = arrow_match_name_frag(next, Arrowmods, &f);
UNSUPPORTED("17cxbrtqid90xrrl75cvyvhs5"); // 	} while (next != rest);
UNSUPPORTED("9u4q5zwdkpdava55p9xyg8xph"); // 	rest = arrow_match_name_frag(rest, Arrownames, &f);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("98pvjlaju0ufz56v1jcwyelw6"); //     if (f && !(f & ((1 << 4) - 1)))
UNSUPPORTED("2mly07gipiope02mgflzcie3e"); // 	f |= 1;
UNSUPPORTED("48w47t8z0k3lb7rxdlbd6n7p9"); //     *flag |= f;
UNSUPPORTED("bbweh79ihpurvsz097xab3u5k"); //     return rest;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("b669zec8aznq4obnil98j5lby","arrow_match_shape");
}
}




//3 2pveqb5qcgfxcqp410ub942eg
// static void arrow_match_name(char *name, int *flag) 
public static void arrow_match_name(CString name, int flag[]) {
ENTERING("2pveqb5qcgfxcqp410ub942eg","arrow_match_name");
try {
    CString rest = name;
    CString next;
    int i, f;
    flag[0] = 0;
    LOG2("Skipping arrow_match_name");
} finally {
LEAVING("2pveqb5qcgfxcqp410ub942eg","arrow_match_name");
}
}




//3 2szgwtfieaw58pea2ohjyu8ea
// void arrow_flags(Agedge_t * e, int *sflag, int *eflag) 
public static void arrow_flags(ST_Agedge_s e, int sflag[], int eflag[]) {
ENTERING("2szgwtfieaw58pea2ohjyu8ea","arrow_flags");
try {
    CString attr;
    ST_arrowdir_t arrowdir;
    sflag[0] = (0);
    eflag[0] = agisdirected(agraphof(e)) ? 1 : (0);
    if (Z.z().E_dir!=null && ((attr = agxget(e, Z.z().E_dir))).charAt(0)!='\0') {
UNSUPPORTED("em7x45v09orjeey5u06gf9b4s"); // 	for (arrowdir = Arrowdirs; arrowdir->dir; arrowdir++) {
UNSUPPORTED("dhaookuw0a1xqmh07lldcvlgi"); // 	    if ((*(attr)==*(arrowdir->dir)&&!strcmp(attr,arrowdir->dir))) {
UNSUPPORTED("1d32qbc447n7nmmvedj3bnhr4"); // 		*sflag = arrowdir->sflag;
UNSUPPORTED("4bwlkonvn34iwi5ea1o8zov3o"); // 		*eflag = arrowdir->eflag;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    if (Z.z().E_arrowhead!=null && (eflag[0] == 1) && ((attr = agxget(e,Z.z(). E_arrowhead))).charAt(0)!='\0')
	arrow_match_name(attr, eflag);
    if (Z.z().E_arrowtail!=null && (sflag[0] == 1) && ((attr = agxget(e, Z.z().E_arrowtail))).charAt(0)!='\0')
	arrow_match_name(attr, sflag);
    if (ED_conc_opp_flag(e)) {
UNSUPPORTED("1p2usipxeqlorwroqo37t3yfy"); // 	edge_t *f;
UNSUPPORTED("6ne3pu2bnhx6tyx81t4td4up6"); // 	int s0, e0;
UNSUPPORTED("8vccx8sm1c228dqm7l1jm2hfs"); // 	/* pick up arrowhead of opposing edge */
UNSUPPORTED("cu21qrxyz93ly7l96af2gcsle"); // 	f = (agedge(agraphof(aghead(e)),aghead(e),agtail(e),NULL,0));
UNSUPPORTED("9u6scg3h7baww90tcykvjhajo"); // 	arrow_flags(f, &s0, &e0);
UNSUPPORTED("157il4mnbenpon7knxfdb4fwb"); // 	*eflag = *eflag | s0;
UNSUPPORTED("7wucod5xwp24vblpcbjbmmcq1"); // 	*sflag = *sflag | e0;
    }
} finally {
LEAVING("2szgwtfieaw58pea2ohjyu8ea","arrow_flags");
}
}




//3 1yk5wl46i7rlzcern0tefd24s
// double arrow_length(edge_t * e, int flag) 
public static double arrow_length(ST_Agedge_s e, int flag) {
ENTERING("1yk5wl46i7rlzcern0tefd24s","arrow_length");
try {
    double lenfact = 0.0;
    int f, i;
    for (i = 0; i < 4; i++) {
        /* we don't simply index with flag because arrowtypes are not necessarily sorted */
        f = (flag >> (i * 8)) & ((1 << 4) - 1);
        for (int arrowtype = 0; Z.z().Arrowtypes[arrowtype].gen!=null; arrowtype++) {
	    if (f == Z.z().Arrowtypes[arrowtype].type) {
	        lenfact += Z.z().Arrowtypes[arrowtype].lenfact;
	        break;
	    }
        }
    }
    /* The original was missing the factor E_arrowsz, but I believe it
       should be here for correct arrow clipping */
    return 10. * lenfact * late_double(e, Z.z().E_arrowsz, 1.0, 0.0);
} finally {
LEAVING("1yk5wl46i7rlzcern0tefd24s","arrow_length");
}
}




//3 7ymcsnwqkr1crisrga0kezh1f
// static boolean inside(inside_t * inside_context, pointf p) 
public static boolean inside(ST_inside_t inside_context, final ST_pointf p) {
// WARNING!! STRUCT
return inside_w_(inside_context, p.copy());
}
private static boolean inside_w_(ST_inside_t inside_context, final ST_pointf p) {
ENTERING("7ymcsnwqkr1crisrga0kezh1f","inside");
try {
    return DIST2(p, inside_context.a_p.get(0)) <= inside_context.a_r.getDouble();
} finally {
LEAVING("7ymcsnwqkr1crisrga0kezh1f","inside");
}
}




//3 9eellwhg4gsa2pdszpeqihs2d
// int arrowEndClip(edge_t* e, pointf * ps, int startp, 		 int endp, bezier * spl, int eflag) 
public static int arrowEndClip(ST_Agedge_s e, ST_pointf.Array ps, int startp, int endp, ST_bezier spl, int eflag) {
ENTERING("9eellwhg4gsa2pdszpeqihs2d","arrowEndClip");
try {
    final ST_inside_t inside_context = new ST_inside_t();
    final ST_pointf.Array sp = new ST_pointf.Array( 4);
    double elen;
    MutableDouble elen2 = new MutableDouble(0);
    elen = arrow_length(e, eflag);
    elen2.setValue(elen * elen);
    spl.setInt("eflag", eflag);
    spl.setStruct("ep", ps.plus(endp + 3).getStruct());
    if (endp > startp && DIST2(ps.get(endp), ps.get(endp + 3)) < elen2.getValue()) {
	endp -= 3;
    }
    sp.plus(3).setStruct(ps.plus(endp).getStruct());
    sp.plus(2).setStruct(ps.plus(endp+1).getStruct());
    sp.plus(1).setStruct(ps.plus(endp+2).getStruct());
    sp.plus(0).setStruct(spl.ep);
    /* ensure endpoint starts inside */
    inside_context.setPtr("a.p", sp.plus(0).asPtr());
    inside_context.setPtr("a.r", elen2.amp());
    bezier_clip(inside_context, function(arrows__c.class, "inside"), sp, NOT(false));
    ps.plus(endp).setStruct(sp.plus(3).getStruct());
    ps.plus(endp+1).setStruct(sp.plus(2).getStruct());
    ps.plus(endp+2).setStruct(sp.plus(1).getStruct());
    ps.plus(endp+3).setStruct(sp.plus(0).getStruct());
    return endp;
} finally {
LEAVING("9eellwhg4gsa2pdszpeqihs2d","arrowEndClip");
}
}




//3 q7y4oxn0paexbgynmtg2zmiv
// int arrowStartClip(edge_t* e, pointf * ps, int startp, 		   int endp, bezier * spl, int sflag) 
public static int arrowStartClip(ST_Agedge_s e, ST_pointf.Array ps, int startp, int endp, ST_bezier spl, int sflag) {
ENTERING("q7y4oxn0paexbgynmtg2zmiv","arrowStartClip");
try {
    final ST_inside_t inside_context = new ST_inside_t();
    final ST_pointf.Array sp = new ST_pointf.Array( 4);
    double slen;
    MutableDouble slen2 = new MutableDouble(0);
    slen = arrow_length(e, sflag);
    slen2.setValue(slen * slen);
    spl.setInt("sflag", sflag);
    spl.setStruct("sp", ps.plus(startp).getStruct());
    if (endp > startp && DIST2(ps.get(startp), ps.get(startp + 3)) < slen2.getValue()) {
    	startp += 3;
    }
    sp.plus(0).setStruct(ps.plus(startp+3).getStruct());
    sp.plus(1).setStruct(ps.plus(startp+2).getStruct());
    sp.plus(2).setStruct(ps.plus(startp+1).getStruct());
    sp.plus(3).setStruct(spl.sp);
    /* ensure endpoint starts inside */
    inside_context.setPtr("a.p", sp.plus(3).asPtr());
    inside_context.setPtr("a.r", slen2.amp());
    bezier_clip(inside_context, function(arrows__c.class, "inside"), sp, false);
    ps.plus(startp).setStruct(sp.plus(3).getStruct());
    ps.plus(startp+1).setStruct(sp.plus(2).getStruct());
    ps.plus(startp+2).setStruct(sp.plus(1).getStruct());
    ps.plus(startp+3).setStruct(sp.plus(0).getStruct());
    return startp;
} finally {
LEAVING("9eellwhg4gsa2pdszpeqihs2d","arrowEndClip");
}
}




//3 5i0vg914q5v5dzz5vo7rg9omc
// void arrowOrthoClip(edge_t* e, pointf* ps, int startp, int endp, bezier* spl, int sflag, int eflag) 
public static Object arrowOrthoClip(Object... arg) {
UNSUPPORTED("5cmga0193q90gs5y2r0l9ekgq"); // void arrowOrthoClip(edge_t* e, pointf* ps, int startp, int endp, bezier* spl, int sflag, int eflag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("y7yudkjc31udfiam6z6lkpyz"); //     pointf p, q, r, s, t;
UNSUPPORTED("3kkc3p6yj8romhqyooa86wcf7"); //     double d, tlen, hlen, maxd;
UNSUPPORTED("c69aoxg5blb5c27rwb7uvguna"); //     if (sflag && eflag && (endp == startp)) { /* handle special case of two arrows on a single segment */
UNSUPPORTED("eb6qp4f6c1liqz5gv8yr4nt2u"); // 	p = ps[endp];
UNSUPPORTED("ecphms6syi9sh7jtisdvhb8hr"); // 	q = ps[endp+3];
UNSUPPORTED("2pzsi9r63yv2o8qeounzv6cny"); // 	tlen = arrow_length (e, sflag);
UNSUPPORTED("f4d86okjchj0qyg2roq13hufh"); // 	hlen = arrow_length (e, eflag);
UNSUPPORTED("3sbhjktcu1u1avngc5ej62mw4"); //         d = DIST(p, q);
UNSUPPORTED("bsdcbs5e8tkm1802lidu0jtw8"); // 	if (hlen + tlen >= d) {
UNSUPPORTED("8gpoj60hh2teibwc83s0ii79w"); // 	    hlen = tlen = d/3.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6xtkcedj7la7fqplc0unqj0wx"); // 	if (p.y == q.y) { /* horz segment */
UNSUPPORTED("9n0q5j1nqa19z0zoz3mpmwpdv"); // 	    s.y = t.y = p.y;
UNSUPPORTED("c2tle7mztwggexoad4drqjw0a"); // 	    if (p.x < q.x) {
UNSUPPORTED("183kgzstrmgynznfkfj0jl3df"); // 		t.x = q.x - hlen;
UNSUPPORTED("7cugpgpm4lyr66kkhauqj5qvy"); // 		s.x = p.x + tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("a8lrkw50xbjo3ntsv0r1mz5i9"); // 		t.x = q.x + hlen;
UNSUPPORTED("37zp6lexzsbm2vomf22x7i5r"); // 		s.x = p.x - tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4l6yfu81669thfh19tmcn44pu"); // 	else {            /* vert segment */
UNSUPPORTED("bc0n1oxhmb3wgphgm1w4n9dz1"); // 	    s.x = t.x = p.x;
UNSUPPORTED("d2pzq44lkkxam6rx01xnozquf"); // 	    if (p.y < q.y) {
UNSUPPORTED("5k5qyffqi7gacnu4jwl6efngx"); // 		t.y = q.y - hlen;
UNSUPPORTED("7ppaznbfc8awmm6e9d9qzw4ms"); // 		s.y = p.y + tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8ohhvcqa5v7oor1gbpznb6faq"); // 		t.y = q.y + hlen;
UNSUPPORTED("4j6guu6e5ddqobe77kt7sbmjq"); // 		s.y = p.y - tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("a4d4fgiq4l3sbeb9ud8dowkby"); // 	ps[endp] = ps[endp + 1] = s;
UNSUPPORTED("db740uoo9pfyknnmi2yx0glgb"); // 	ps[endp + 2] = ps[endp + 3] = t;
UNSUPPORTED("ewajj4utlr95mfmaswtc9yeiv"); // 	spl->eflag = eflag, spl->ep = p;
UNSUPPORTED("9bgf1pn9yx1vlolgcjos2emsl"); // 	spl->sflag = sflag, spl->sp = q;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5zse8mf1iyqtzzlq2txqcym6x"); //     if (eflag) {
UNSUPPORTED("bpf9snlwegftq8d78l9hsz76b"); // 	hlen = arrow_length(e, eflag);
UNSUPPORTED("eb6qp4f6c1liqz5gv8yr4nt2u"); // 	p = ps[endp];
UNSUPPORTED("ecphms6syi9sh7jtisdvhb8hr"); // 	q = ps[endp+3];
UNSUPPORTED("3sbhjktcu1u1avngc5ej62mw4"); //         d = DIST(p, q);
UNSUPPORTED("9b0ae4jocdkvqt8r3iw39yf5d"); // 	maxd = 0.9*d;
UNSUPPORTED("bwzkrhk431iwhs6c467tb0yh9"); // 	if (hlen >= maxd) {   /* arrow too long */
UNSUPPORTED("23uwvl5a8msik1u1crb262nqj"); // 	    hlen = maxd;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6xtkcedj7la7fqplc0unqj0wx"); // 	if (p.y == q.y) { /* horz segment */
UNSUPPORTED("a851ewci39wssny4nn99f4nmr"); // 	    r.y = p.y;
UNSUPPORTED("a2dyb0em7hwd4qdx1u0tuc8pl"); // 	    if (p.x < q.x) r.x = q.x - hlen;
UNSUPPORTED("90ksto8lyojedi0p77l4zm7x"); // 	    else r.x = q.x + hlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4l6yfu81669thfh19tmcn44pu"); // 	else {            /* vert segment */
UNSUPPORTED("4rh9lai2dcsutwg48bb2qljyg"); // 	    r.x = p.x;
UNSUPPORTED("6gnp9tso58zn1rn4j7jv3i1y0"); // 	    if (p.y < q.y) r.y = q.y - hlen;
UNSUPPORTED("3wd6fw8km4tp6a1p9ijk343ih"); // 	    else r.y = q.y + hlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bxxlk6noh1kzyi93fptcz29j4"); // 	ps[endp + 1] = p;
UNSUPPORTED("d3kionq4ycqr87orc5vkdnse0"); // 	ps[endp + 2] = ps[endp + 3] = r;
UNSUPPORTED("4uwxjmxybnuriwua5xoo17bfa"); // 	spl->eflag = eflag;
UNSUPPORTED("25oo9o1uy5fisoodt43sio6zx"); // 	spl->ep = q;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3297fx8lk8bjyg998l9ujeeph"); //     if (sflag) {
UNSUPPORTED("5slqgq5wsgplyy9uj9mg5pkrc"); // 	tlen = arrow_length(e, sflag);
UNSUPPORTED("ayxhimnpo6p08kshlux75qpcu"); // 	p = ps[startp];
UNSUPPORTED("2ydx1urmjnn1tgx6ffzsvwimx"); // 	q = ps[startp+3];
UNSUPPORTED("3sbhjktcu1u1avngc5ej62mw4"); //         d = DIST(p, q);
UNSUPPORTED("9b0ae4jocdkvqt8r3iw39yf5d"); // 	maxd = 0.9*d;
UNSUPPORTED("1uya1cfbkj8b6j38zbvdxmgrq"); // 	if (tlen >= maxd) {   /* arrow too long */
UNSUPPORTED("3ydle9u127f7saxiibosc2lxs"); // 	    tlen = maxd;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6xtkcedj7la7fqplc0unqj0wx"); // 	if (p.y == q.y) { /* horz segment */
UNSUPPORTED("a851ewci39wssny4nn99f4nmr"); // 	    r.y = p.y;
UNSUPPORTED("7xq2f46jfu6rsd83fqyr71z26"); // 	    if (p.x < q.x) r.x = p.x + tlen;
UNSUPPORTED("8gtrjqabiq8x8jl0j2eveiugg"); // 	    else r.x = p.x - tlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4l6yfu81669thfh19tmcn44pu"); // 	else {            /* vert segment */
UNSUPPORTED("4rh9lai2dcsutwg48bb2qljyg"); // 	    r.x = p.x;
UNSUPPORTED("es4i2rg7sahthpreieu5hcwl7"); // 	    if (p.y < q.y) r.y = p.y + tlen;
UNSUPPORTED("26o5nwhklplaxveikjpxzxoom"); // 	    else r.y = p.y - tlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5pnx91gtz1gahdprf0syc4yk5"); // 	ps[startp] = ps[startp + 1] = r;
UNSUPPORTED("3e3iux8uecaf6eu2s9q46clr5"); // 	ps[startp + 2] = q;
UNSUPPORTED("bmeeipd0o72kslox40628z9gj"); // 	spl->sflag = sflag;
UNSUPPORTED("dwq656v3u8zmbqdetlo0wmyeb"); // 	spl->sp = p;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b7nm38od2nxotpyzxg0ychqdb
// static void arrow_type_normal(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_normal(Object... arg) {
UNSUPPORTED("bk3aihjbdtkitpdvvtmzbt2zu"); // static void arrow_type_normal(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4z3ehq6q8ofvyjy4r4mrg86cl"); //     pointf q, v, a[5];
UNSUPPORTED("5qr741c2e1pdivv0bbjcr6cik"); //     double arrowwidth;
UNSUPPORTED("9gahmv6psjnccqesq7xt6q6hx"); //     arrowwidth = 0.35;
UNSUPPORTED("bih7b1ef9lfxy4uy4o07ae3bk"); //     if (penwidth > 4)
UNSUPPORTED("o3yn0730zlzconddqarivq05"); //         arrowwidth *= penwidth / 4;
UNSUPPORTED("949z3586m713okvlu1ymyw1us"); //     v.x = -u.y * arrowwidth;
UNSUPPORTED("c8xqr3hfd006yf1uuh47ndi71"); //     v.y = u.x * arrowwidth;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("eh6fh3kco3kvywuta8d8yb5v0"); //     if (flag & (1<<(4+1))) {
UNSUPPORTED("3by9fwhdnq30ll5nt8qbfwx0p"); // 	a[0] = a[4] = p;
UNSUPPORTED("4ut2bmvsgdsemxiv8urham7m3"); // 	a[1].x = p.x - v.x;
UNSUPPORTED("asa1ffrrxd6dkzm1sdamgofuu"); // 	a[1].y = p.y - v.y;
UNSUPPORTED("60oc061ln68pvrg7zp8s3ncog"); // 	a[2] = q;
UNSUPPORTED("591i8kbz6r8bskar4gy0vpsus"); // 	a[3].x = p.x + v.x;
UNSUPPORTED("7i9sj4qz5f52w1wiz47bo6dv2"); // 	a[3].y = p.y + v.y;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6ofxgqmmh2ikk8818bf8aw2mw"); // 	a[0] = a[4] = q;
UNSUPPORTED("4l3g4pagkn0dto3bwi2e0bukd"); // 	a[1].x = q.x - v.x;
UNSUPPORTED("9wv7w8vdiedhkfehzqwyyv897"); // 	a[1].y = q.y - v.y;
UNSUPPORTED("d0mui3zxt1cx6mx5wfax35iah"); // 	a[2] = p;
UNSUPPORTED("b5arwmpck5jtms8g1zgnojj5o"); // 	a[3].x = q.x + v.x;
UNSUPPORTED("3viyneb6qkp0alwghd7mo06cc"); // 	a[3].y = q.y + v.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("52punwd4fhhq1arhez8cuwvam"); // 	gvrender_polygon(job, a, 3, !(flag & (1<<(4+0))));
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("gu9pg6c0d0uzlztdiq2o96zh"); // 	gvrender_polygon(job, &a[2], 3, !(flag & (1<<(4+0))));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("7c6e70h4efa2wpa155lfy3er5"); // 	gvrender_polygon(job, &a[1], 3, !(flag & (1<<(4+0))));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b6y46a44yguy7zuhgxukxnq79
// static void arrow_type_crow(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_crow(Object... arg) {
UNSUPPORTED("6rtaogz992ixfhc4qfzpl9pw8"); // static void arrow_type_crow(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hutab8l2bf18ywdh16qls7ix"); //     pointf m, q, v, w, a[9];
UNSUPPORTED("9riilhm03el22oazbpvsntuyd"); //     double arrowwidth, shaftwidth;
UNSUPPORTED("3pjnxs55moxekjdkzcgmes60h"); //     arrowwidth = 0.45;
UNSUPPORTED("7k8ulzafyheqq2wcj2ued1mll"); //     if (penwidth > (4 * arrowsize) && (flag & (1<<(4+1))))
UNSUPPORTED("85aamjzbblwnk2bp9jvnetpmf"); //         arrowwidth *= penwidth / (4 * arrowsize);
UNSUPPORTED("ykk0490b4cs8a1v9gpqx95we"); //     shaftwidth = 0;
UNSUPPORTED("61i60vknj2akb0ojgfdcnjjyh"); //     if (penwidth > 1 && (flag & (1<<(4+1))))
UNSUPPORTED("ak0a6438e5b0b2nu2wl572f6d"); // 	shaftwidth = 0.05 * (penwidth - 1) / arrowsize;   /* arrowsize to cancel the arrowsize term already in u */
UNSUPPORTED("949z3586m713okvlu1ymyw1us"); //     v.x = -u.y * arrowwidth;
UNSUPPORTED("c8xqr3hfd006yf1uuh47ndi71"); //     v.y = u.x * arrowwidth;
UNSUPPORTED("56jq7ic46net8ort3ve4st2tw"); //     w.x = -u.y * shaftwidth;
UNSUPPORTED("ampfe31k0mn8vsflzy5cx4lgv"); //     w.y = u.x * shaftwidth;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("cfk5fnc73y4ey68iwqxlsr7u9"); //     m.x = p.x + u.x * 0.5;
UNSUPPORTED("2mtpxqz0h7jsb7bmao9g1vkar"); //     m.y = p.y + u.y * 0.5;
UNSUPPORTED("5rymactsr9099qy69qf1bkwdr"); //     if (flag & (1<<(4+1))) {  /* vee */
UNSUPPORTED("4s8pxkc9kbvhabi2gbpxic5sm"); // 	a[0] = a[8] = p;
UNSUPPORTED("4l3g4pagkn0dto3bwi2e0bukd"); // 	a[1].x = q.x - v.x;
UNSUPPORTED("9wv7w8vdiedhkfehzqwyyv897"); // 	a[1].y = q.y - v.y;
UNSUPPORTED("6rqmfaf4g98cg0t1qaax08e69"); // 	a[2].x = m.x - w.x;
UNSUPPORTED("1nwyu905ao88kvq8pgnfwerf5"); // 	a[2].y = m.y - w.y;
UNSUPPORTED("9z1frd2w5h9zvfo4pszqlzp30"); // 	a[3].x = q.x - w.x;
UNSUPPORTED("1s86b9xef6phy3gt70ojgn6ip"); // 	a[3].y = q.y - w.y;
UNSUPPORTED("4zd6xleeq5n1l2zznquvnlw7v"); // 	a[4] = q;
UNSUPPORTED("2wb1104b1x08j7tecfcwk25uj"); // 	a[5].x = q.x + w.x;
UNSUPPORTED("6tq7rnp0h0p9xtxibo13g8v3t"); // 	a[5].y = q.y + w.y;
UNSUPPORTED("7byonl28yipw4lk1syuuj51it"); // 	a[6].x = m.x + w.x;
UNSUPPORTED("lwb6vnlr2dq5ysijij21wbgl"); // 	a[6].y = m.y + w.y;
UNSUPPORTED("eh4rkk1h3ciybh0u0hgehkdxx"); // 	a[7].x = q.x + v.x;
UNSUPPORTED("bxhyoes561jf42tw73tjj33sj"); // 	a[7].y = q.y + v.y;
UNSUPPORTED("54abbljqrd361peswxjtohjg0"); //     } else {                     /* crow */
UNSUPPORTED("5i9r1mehhwkkn8ojo8csm0piw"); // 	a[0] = a[8] = q;
UNSUPPORTED("4ut2bmvsgdsemxiv8urham7m3"); // 	a[1].x = p.x - v.x;
UNSUPPORTED("asa1ffrrxd6dkzm1sdamgofuu"); // 	a[1].y = p.y - v.y;
UNSUPPORTED("6rqmfaf4g98cg0t1qaax08e69"); // 	a[2].x = m.x - w.x;
UNSUPPORTED("1nwyu905ao88kvq8pgnfwerf5"); // 	a[2].y = m.y - w.y;
UNSUPPORTED("5yhpyznqsxb2ga5si6phvakqg"); // 	a[3].x = p.x;
UNSUPPORTED("3eko75yr046fkm2yulawhw236"); // 	a[3].y = p.y;
UNSUPPORTED("7nofpsbtwg78gooeo8makz5bb"); // 	a[4] = p;
UNSUPPORTED("dl1jq9xglce6cfpe03mzu3p22"); // 	a[5].x = p.x;
UNSUPPORTED("8kqrguezr1d2awpazfz88cw5"); // 	a[5].y = p.y;
UNSUPPORTED("7byonl28yipw4lk1syuuj51it"); // 	a[6].x = m.x + w.x;
UNSUPPORTED("lwb6vnlr2dq5ysijij21wbgl"); // 	a[6].y = m.y + w.y;
UNSUPPORTED("egqlf021ldci9s31jrpk2m1pk"); // 	a[7].x = p.x + v.x;
UNSUPPORTED("60b7k5jzqw1ndaxnese0cnx2t"); // 	a[7].y = p.y + v.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("8vyuq79k664wims1n1ltnudbt"); // 	gvrender_polygon(job, a, 6, 1);
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("ap1vbm2kxsjtcp3pezwq2jwln"); // 	gvrender_polygon(job, &a[3], 6, 1);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("d0npn4cmn6nd0ytaww7u7ghrw"); // 	gvrender_polygon(job, a, 9, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e8w54seijyii7km6zl3sivjpu
// static void arrow_type_gap(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_gap(Object... arg) {
UNSUPPORTED("anlswsxb36i1znu2805bu47t2"); // static void arrow_type_gap(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("brf7jajkh244o41ekaw2tyora"); //     pointf q, a[2];
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("dhfgavaa2js7qt2ciwujmmrpv"); //     a[0] = p;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eg7sgk8umcqfthbo1t0plohbt
// static void arrow_type_tee(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_tee(Object... arg) {
UNSUPPORTED("9u6pwrzl9t5i0kfvnwn7uufrp"); // static void arrow_type_tee(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hwyzphi1ihi2lx3engtta1qa"); //     pointf m, n, q, v, a[4];
UNSUPPORTED("e8sltpxlu9ln6k3zn49u46zau"); //     v.x = -u.y;
UNSUPPORTED("1wxxf464go09wrecpyo3y2k25"); //     v.y = u.x;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("61cj5vg96f8j145swk5v2nz5a"); //     m.x = p.x + u.x * 0.2;
UNSUPPORTED("25zmgst5hb9ya664up64k51tt"); //     m.y = p.y + u.y * 0.2;
UNSUPPORTED("9wrt18toae33nd3tmifyahyt8"); //     n.x = p.x + u.x * 0.6;
UNSUPPORTED("1151hzer2rx55qz715on0gexs"); //     n.y = p.y + u.y * 0.6;
UNSUPPORTED("4ihm8x9khys2bcoivyqzf4dth"); //     a[0].x = m.x + v.x;
UNSUPPORTED("941h30wfi9u1c17vhabko438l"); //     a[0].y = m.y + v.y;
UNSUPPORTED("7qtkiyjyg6pzhhoyslv8pmp1q"); //     a[1].x = m.x - v.x;
UNSUPPORTED("24iwwf6paxfgux41w93obhwp4"); //     a[1].y = m.y - v.y;
UNSUPPORTED("314g2i9mvbz4um5y7oiyuldvw"); //     a[2].x = n.x - v.x;
UNSUPPORTED("7ebhg7671hxu0useo7ewi26kn"); //     a[2].y = n.y - v.y;
UNSUPPORTED("8mc60oc3vsykq69a5zb6h72u4"); //     a[3].x = n.x + v.x;
UNSUPPORTED("6q0rldgbg1rfr4skqzq0v099f"); //     a[3].y = n.y + v.y;
UNSUPPORTED("9u73bl75ej5xy9pe46nac6ih5"); //     if (flag & (1<<(4+2))) {
UNSUPPORTED("6omh7vjmab159riw0fejjpwk0"); // 	a[0] = m;
UNSUPPORTED("3w139dwzvrzrghf5w8hox1qen"); // 	a[3] = n;
UNSUPPORTED("2u9qb4zvcio06wzd2nb3bjrs4"); //     } else if (flag & (1<<(4+3))) {
UNSUPPORTED("7g1lyxw6yo0ycv1l688mehedr"); // 	a[1] = m;
UNSUPPORTED("72r460jtb2id4k8ri9sdwuqy4"); // 	a[2] = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("15b4dosag4vcx31fnicxczw81"); //     gvrender_polygon(job, a, 4, 1);
UNSUPPORTED("dhfgavaa2js7qt2ciwujmmrpv"); //     a[0] = p;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3hdgy0baje1akb7fjw9yovjwz
// static void arrow_type_box(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_box(Object... arg) {
UNSUPPORTED("4u7yj9rhqxdonlyd5taprxs28"); // static void arrow_type_box(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5lmvyh791uemv9zn9qfakp4qb"); //     pointf m, q, v, a[4];
UNSUPPORTED("ep2toe83b4kkoeb00wqwru73t"); //     v.x = -u.y * 0.4;
UNSUPPORTED("8c686kltb5ewlt4qkf7ljmmc8"); //     v.y = u.x * 0.4;
UNSUPPORTED("66oe3r3caie8ddr9mgkqkk0r3"); //     m.x = p.x + u.x * 0.8;
UNSUPPORTED("1mowxzv768r12lzc4blbkl873"); //     m.y = p.y + u.y * 0.8;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("apz7b1n6lnsr7u47d661qkf7r"); //     a[0].x = p.x + v.x;
UNSUPPORTED("eqdvtg0j9avxbrpfozktn2358"); //     a[0].y = p.y + v.y;
UNSUPPORTED("f0vjjdkq6txr0nrc7xjvcxpa9"); //     a[1].x = p.x - v.x;
UNSUPPORTED("4e5dina8t36xvh3bfr8y98a9m"); //     a[1].y = p.y - v.y;
UNSUPPORTED("4npxatuz83si51hcyaeshl3x3"); //     a[2].x = m.x - v.x;
UNSUPPORTED("br2saagm87ysykkosh9e0xjab"); //     a[2].y = m.y - v.y;
UNSUPPORTED("8kb1ee4fjdywi21l0xydfefb4"); //     a[3].x = m.x + v.x;
UNSUPPORTED("coalhyxqy4kj07zi50yoc48fy"); //     a[3].y = m.y + v.y;
UNSUPPORTED("9u73bl75ej5xy9pe46nac6ih5"); //     if (flag & (1<<(4+2))) {
UNSUPPORTED("5fbtaluh9dcnwehl25ff3obkc"); // 	a[0] = p;
UNSUPPORTED("9bjq8dynp7r5d7sbwtodjucxc"); // 	a[3] = m;
UNSUPPORTED("2u9qb4zvcio06wzd2nb3bjrs4"); //     } else if (flag & (1<<(4+3))) {
UNSUPPORTED("e2cqpiig8ac96q5ovh3nyr7t1"); // 	a[1] = p;
UNSUPPORTED("4uekqgdzqn4bhxlllod39w27b"); // 	a[2] = m;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7y7z0oxvzng5clk605r3p4zz8"); //     gvrender_polygon(job, a, 4, !(flag & (1<<(4+0))));
UNSUPPORTED("1p9lzfwbik1778u5gdeqpxm31"); //     a[0] = m;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 equc1q4r6wcoe2pwwnk2u01og
// static void arrow_type_diamond(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_diamond(Object... arg) {
UNSUPPORTED("4wg2b1eyit9ve72uqrds41jk2"); // static void arrow_type_diamond(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ww1c4ncz55av20zgtif7choz"); //     pointf q, r, v, a[5];
UNSUPPORTED("1xsjvcsxdwie2v666ry6ihghq"); //     v.x = -u.y / 3.;
UNSUPPORTED("9xea2lahdijfjgz0skow4ps5m"); //     v.y = u.x / 3.;
UNSUPPORTED("4p3vbsracdkwmi6dly3odymvb"); //     r.x = p.x + u.x / 2.;
UNSUPPORTED("7y5j7ecroy4fci5jzh6mk2sl9"); //     r.y = p.y + u.y / 2.;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("8n36qnwzpe2hugs30s3am1zsg"); //     a[0] = a[4] = q;
UNSUPPORTED("e97x8nztsokegzu9u2tgwssgi"); //     a[1].x = r.x + v.x;
UNSUPPORTED("3yv64xfvqgiyturtkz9pnkbtd"); //     a[1].y = r.y + v.y;
UNSUPPORTED("3isw1pofpfafh53xdqo1gmbrr"); //     a[2] = p;
UNSUPPORTED("655732redrvi9o3a3dyjh90af"); //     a[3].x = r.x - v.x;
UNSUPPORTED("55u2biopicz70n81k5ml5lzjp"); //     a[3].y = r.y - v.y;
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("gu9pg6c0d0uzlztdiq2o96zh"); // 	gvrender_polygon(job, &a[2], 3, !(flag & (1<<(4+0))));
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("52punwd4fhhq1arhez8cuwvam"); // 	gvrender_polygon(job, a, 3, !(flag & (1<<(4+0))));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6qac6zmwtg6rwpmn73fq6poje"); // 	gvrender_polygon(job, a, 4, !(flag & (1<<(4+0))));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dxl50r7ooipvtkyjb0sleittd
// static void arrow_type_dot(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_dot(Object... arg) {
UNSUPPORTED("bsrxktb5cvoy4qewxrb3z3ht"); // static void arrow_type_dot(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e077smjlsfuq1ptdwqpn7lcf0"); //     double r;
UNSUPPORTED("c1555k4n8zggr5m5nozuhequ8"); //     pointf AF[2];
UNSUPPORTED("9nadrv289g5ravobyhu05u9or"); //     r = sqrt(u.x * u.x + u.y * u.y) / 2.;
UNSUPPORTED("bxxvjt8g03vytuyx531n55b1g"); //     AF[0].x = p.x + u.x / 2. - r;
UNSUPPORTED("8lbe9l6h0hqnth5j5skwrhxx1"); //     AF[0].y = p.y + u.y / 2. - r;
UNSUPPORTED("1krebba2swwkp12jt15xqkjqd"); //     AF[1].x = p.x + u.x / 2. + r;
UNSUPPORTED("btubzlppo1x1284g7zu99lk03"); //     AF[1].y = p.y + u.y / 2. + r;
UNSUPPORTED("7mu57g14vmt1bdc523s4wiy22"); //     gvrender_ellipse(job, AF, 2, !(flag & (1<<(4+0))));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5oioemwdl3g1maj3ikzleo0nm
// static void arrow_type_curve(GVJ_t* job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_type_curve(Object... arg) {
UNSUPPORTED("2rt93fe18qb092yomrw5l6mko"); // static void arrow_type_curve(GVJ_t* job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7127v6bnaxe4g216ue7fn3fyp"); //     double arrowwidth = penwidth > 4 ? 0.5 * penwidth / 4 : 0.5;
UNSUPPORTED("euvu8ayo5swdxux8kyjmrywr5"); //     pointf q, v, w;
UNSUPPORTED("9jc1l9d43c00gutvyto6snss3"); //     pointf AF[4], a[2];
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("4qmtmdrrceo9h0mka0fkj5xci"); //     q.y = p.y + u.y; 
UNSUPPORTED("bvibgke8hnjr9rlyzfdumk5w"); //     v.x = -u.y * arrowwidth; 
UNSUPPORTED("c8xqr3hfd006yf1uuh47ndi71"); //     v.y = u.x * arrowwidth;
UNSUPPORTED("duqsnax25spvj38rnd9v32b4d"); //     w.x = v.y; // same direction as u, same magnitude as v.
UNSUPPORTED("4srsifkr8qc1viu2xijhnt66s"); //     w.y = -v.x;
UNSUPPORTED("dhfgavaa2js7qt2ciwujmmrpv"); //     a[0] = p;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("11fvrbnzwbuar9l5gc9wurbor"); //     AF[0].x = p.x + v.x + w.x;
UNSUPPORTED("34xjxb9aaiworzwsfodegww0g"); //     AF[0].y = p.y + v.y + w.y;
UNSUPPORTED("73ax76wsls8wr2c86mm6umxkl"); //     AF[3].x = p.x - v.x + w.x;
UNSUPPORTED("7q0ly8njmscobyx3v5u7xb59c"); //     AF[3].y = p.y - v.y + w.y;
UNSUPPORTED("9qmj4a7f67dltmu1pte8pzqox"); //     AF[1].x = p.x + 0.95 * v.x + w.x - w.x * 4.0 / 3.0;
UNSUPPORTED("5spg9wtj9dwqh43yhk0dqfija"); //     AF[1].y = AF[0].y - w.y * 4.0 / 3.0;
UNSUPPORTED("ab3yikrvtlncw10ivdxvr4a52"); //     AF[2].x = p.x - 0.95 * v.x + w.x - w.x * 4.0 / 3.0;
UNSUPPORTED("45ok1xa7ia1ugs2o8ediwmd5p"); //     AF[2].y = AF[3].y - w.y * 4.0 / 3.0;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("58oum3y30wqa0bofgze20unn7"); // 	Bezier(AF, 3, 0.5, NULL, AF);
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("8dngt8wih972oouawx4wska6k"); // 	Bezier(AF, 3, 0.5, AF, NULL);
UNSUPPORTED("78tsqk66ihei67oy2ptr6t6f3"); //     gvrender_beziercurve(job, AF, sizeof(AF) / sizeof(pointf), 0, 0, 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ruebmb0rzoin79tmkp4o357x
// static pointf arrow_gen_type(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_gen_type(Object... arg) {
UNSUPPORTED("6eekmrou08qiz0zielzyhyn4g"); // static pointf arrow_gen_type(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9rrbht5x8qg377l1khejt2as2"); //     int f;
UNSUPPORTED("aed0rb6bb02eluj3o0ugcfqv9"); //     arrowtype_t *arrowtype;
UNSUPPORTED("ml2ttdehp7agi83yijbgk49r"); //     f = flag & ((1 << 4) - 1);
UNSUPPORTED("f036frj7aawxz98ctbodsj666"); //     for (arrowtype = Arrowtypes; arrowtype->type; arrowtype++) {
UNSUPPORTED("6qf8zxk5crelbhxfi42gd00l3"); // 	if (f == arrowtype->type) {
UNSUPPORTED("epoo24e6zcp2uaje5ukce1yvh"); // 	    u.x *= arrowtype->lenfact * arrowsize;
UNSUPPORTED("bcfjvd5s3jub6wo9roe0xmn0g"); // 	    u.y *= arrowtype->lenfact * arrowsize;
UNSUPPORTED("5wc1a7bb8k1d528kxw2uchm7c"); // 	    (arrowtype->gen) (job, p, u, arrowsize, penwidth, flag);
UNSUPPORTED("3wwns14fz356e6p4s8byp3d6i"); // 	    p.x = p.x + u.x;
UNSUPPORTED("3rzld1v7nkscibpukz3bdox3v"); // 	    p.y = p.y + u.y;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2u4vcl57jl62dmf8fy80ioppm
// boxf arrow_bb(pointf p, pointf u, double arrowsize, int flag) 
public static Object arrow_bb(Object... arg) {
UNSUPPORTED("67tfc7x1j056na7s6itymoeol"); // boxf arrow_bb(pointf p, pointf u, double arrowsize, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2fvgbj82ham8m0frx5hn9dyi"); //     double s;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("94ds3z1i0vt5rbv13ja90fdfp"); //     double ax,ay,bx,by,cx,cy,dx,dy;
UNSUPPORTED("6r1gp4hfea5imwnuiyfuxzh6k"); //     double ux2, uy2;
UNSUPPORTED("d5vh8if7unojun6hmulj4il7u"); //     /* generate arrowhead vector */
UNSUPPORTED("5yc3jb0utnnay4x88h644puhz"); //     u.x -= p.x;
UNSUPPORTED("egh8lzpdfrza6k11lopupxykp"); //     u.y -= p.y;
UNSUPPORTED("bh7ueu6dokefdmej3xz79c7ty"); //     /* the EPSILONs are to keep this stable as length of u approaches 0.0 */
UNSUPPORTED("3oao4fejxee2cop1fhd4m8tae"); //     s = 10. * arrowsize / (sqrt(u.x * u.x + u.y * u.y) + .0001);
UNSUPPORTED("8qxmhdlg9d49yg9gxkjw043"); //     u.x += (u.x >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("4vxtvwh3x5b3i33sdyppe3trq"); //     u.y += (u.y >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("bwi3f8xk8t2nbzy5tjtgeewjl"); //     u.x *= s;
UNSUPPORTED("do56zsbrbn95ovnoqu6zzjjmw"); //     u.y *= s;
UNSUPPORTED("alix1e6k9ywov3xxcwxcgo1hh"); //     /* compute all 4 corners of rotated arrowhead bounding box */
UNSUPPORTED("9bdmzamsx8jasbcfy2mk0v7yt"); //     ux2 = u.x / 2.;
UNSUPPORTED("3k8htwk7cas9gfe4j797zk3b"); //     uy2 = u.y / 2.;
UNSUPPORTED("ar2s2pmmxun5v6p0d4mlij1ro"); //     ax = p.x - uy2;
UNSUPPORTED("d9cpq1pbscjxjhkyi57s76o4r"); //     ay = p.y - ux2;
UNSUPPORTED("7m3bdjur8btdn3q1dzd4o751s"); //     bx = p.x + uy2;
UNSUPPORTED("bhn3rg0stek17iytsy7bgbwqw"); //     by = p.y + ux2;
UNSUPPORTED("ai8hjx4uwhzow4nolep1478xn"); //     cx = ax + u.x;
UNSUPPORTED("15l0cqg7njm4ebimncczi9uho"); //     cy = ay + u.y;
UNSUPPORTED("29117dcz6pcm4ibiebo4cemeh"); //     dx = bx + u.x;
UNSUPPORTED("7s3y5imd0u3woy1d0q58g1wlh"); //     dy = by + u.y;
UNSUPPORTED("7lzozmdnkd5c06cyxy2skrar5"); //     /* compute a right bb */
UNSUPPORTED("4shnxc3z5z4wj3l0pl7tml625"); //     bb.UR.x = MAX(ax, MAX(bx, MAX(cx, dx)));
UNSUPPORTED("2igw3asrvk13qlfbw4sgn7vxt"); //     bb.UR.y = MAX(ay, MAX(by, MAX(cy, dy)));
UNSUPPORTED("7fz9fiabx9i87t8t6bgjeso5a"); //     bb.LL.x = MIN(ax, MIN(bx, MIN(cx, dx)));
UNSUPPORTED("c6v20rdx0lfdvypx8l4tomnri"); //     bb.LL.y = MIN(ay, MIN(by, MIN(cy, dy)));
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8ss8m9a0p5v0yx2oqggh0rx57
// void arrow_gen(GVJ_t * job, emit_state_t emit_state, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
public static Object arrow_gen(Object... arg) {
UNSUPPORTED("ag73i6wbc5lb0d46ul40euyur"); // void arrow_gen(GVJ_t * job, emit_state_t emit_state, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("2fvgbj82ham8m0frx5hn9dyi"); //     double s;
UNSUPPORTED("66oipfshtfj4imr4j2x2drib0"); //     int i, f;
UNSUPPORTED("ecr1y7qy0ikxkidkdfvwv88ir"); //     emit_state_t old_emit_state;
UNSUPPORTED("c3lqudp40feg72zp97ngqkww9"); //     old_emit_state = obj->emit_state;
UNSUPPORTED("3ook7gsw0rr7b6uwm9f5a5dtx"); //     obj->emit_state = emit_state;
UNSUPPORTED("exvy7jlggpvu1zhz08fo1jbvi"); //     /* Dotted and dashed styles on the arrowhead are ugly (dds) */
UNSUPPORTED("em34eidklzv0dobtybvgz9gwu"); //     /* linewidth needs to be reset */
UNSUPPORTED("4g8oyutwebzej18aaiz74zb9k"); //     gvrender_set_style(job, job->gvc->defaultlinestyle);
UNSUPPORTED("eertb1vvqryb308a1uuff8s0"); //     gvrender_set_penwidth(job, penwidth);
UNSUPPORTED("d5vh8if7unojun6hmulj4il7u"); //     /* generate arrowhead vector */
UNSUPPORTED("5yc3jb0utnnay4x88h644puhz"); //     u.x -= p.x;
UNSUPPORTED("egh8lzpdfrza6k11lopupxykp"); //     u.y -= p.y;
UNSUPPORTED("bh7ueu6dokefdmej3xz79c7ty"); //     /* the EPSILONs are to keep this stable as length of u approaches 0.0 */
UNSUPPORTED("9s182w6wdwxo0pwu9hljlyofe"); //     s = 10. / (sqrt(u.x * u.x + u.y * u.y) + .0001);
UNSUPPORTED("8qxmhdlg9d49yg9gxkjw043"); //     u.x += (u.x >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("4vxtvwh3x5b3i33sdyppe3trq"); //     u.y += (u.y >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("bwi3f8xk8t2nbzy5tjtgeewjl"); //     u.x *= s;
UNSUPPORTED("do56zsbrbn95ovnoqu6zzjjmw"); //     u.y *= s;
UNSUPPORTED("3zei0bi63grn37qiuxn09n7hz"); //     /* the first arrow head - closest to node */
UNSUPPORTED("a2n8aqfq0cqpx8elstmfn9oq6"); //     for (i = 0; i < 4; i++) {
UNSUPPORTED("8sgyt5ym5jt73oknb4tdj2zpl"); //         f = (flag >> (i * 8)) & ((1 << 8) - 1);
UNSUPPORTED("5vg3retgvi5ekir9xbw8j4zoq"); // 	if (f == (0))
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("biq7xz2uj7ksjrqn6tqr9glzj"); //         p = arrow_gen_type(job, p, u, arrowsize, penwidth, f);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b1bkq4eyrmepbxyb3qiuhi8b8"); //     obj->emit_state = old_emit_state;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
