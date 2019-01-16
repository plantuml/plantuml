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
import static gen.lib.common.geom__c.ptToLine2;
import static gen.lib.common.utils__c.Bezier;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agraph_s;
import h.ST_boxf;
import h.ST_pointf;
import h.xdot;
import smetana.core.CString;
import smetana.core.__ptr__;

public class emit__c {
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




//3 7udip7yo3ddkp9ocjftokpm9y
// void* init_xdot (Agraph_t* g) 
public static __ptr__ init_xdot(ST_Agraph_s g) {
ENTERING("7udip7yo3ddkp9ocjftokpm9y","init_xdot");
try {
    CString p;
    xdot xd = null;
    if (N((p = agget(g, new CString("_background")))!=null && p.charAt(0)!='\0')) {
	if (N((p = agget(g, new CString("_draw_")))!=null  && p.charAt(0)!='\0')) {
	    return null;
	}
    }
UNSUPPORTED("16fu50ud9qppkwxzdy0nde3lm"); //     xd = parseXDotF (p, NULL, sizeof (exdot_op));
UNSUPPORTED("1x2xrqe9on9i2dlb07gj02n65"); //     if (!xd) {
UNSUPPORTED("b4emzm37tsv7edlai0fhwoul1"); // 	agerr(AGWARN, "Could not parse \"_background\" attribute in graph %s\n", agnameof(g));
UNSUPPORTED("72v3r8ey2hvh1o9qskrji8im4"); // 	agerr(AGPREV, "  \"%s\"\n", p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("jujeh27uxxeyas8n09tnlnbu"); //     return xd;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("7udip7yo3ddkp9ocjftokpm9y","init_xdot");
}
}


//1 2mlc0h4j6tfwqvwqa6hyvz4o5
// static char *defaultlinestyle[3] = 




//3 7d00uua41zvsvxe77gttfnmy
// obj_state_t* push_obj_state(GVJ_t *job) 
public static Object push_obj_state(Object... arg) {
UNSUPPORTED("epyu2q1m9hozws9k2sjri40sa"); // obj_state_t* push_obj_state(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e468kl46d9gv50x67guzl3gvx"); //     obj_state_t *obj, *parent;
UNSUPPORTED("4iwsnw8sdl14e952mea0vbirj"); //     if (! (obj = zmalloc(sizeof(obj_state_t))))
UNSUPPORTED("ehd3dhcaq9t6u9bppvv8uk9pq"); //         agerr(AGERR, "no memory from zmalloc()\n");
UNSUPPORTED("4sj5l7lrob40gejr8efmsh7jd"); //     parent = obj->parent = job->obj;
UNSUPPORTED("cymxgixtrn3cty2dfm0f5n993"); //     job->obj = obj;
UNSUPPORTED("2yxuvhwao8vnwsdql2towwn8a"); //     if (parent) {
UNSUPPORTED("at60efb0o2t6r5j89rkvnvmmz"); //         obj->pencolor = parent->pencolor;        /* default styles to parent's style */
UNSUPPORTED("2i4ng2pvtum10vrqr1k5jmdhy"); //         obj->fillcolor = parent->fillcolor;
UNSUPPORTED("er3gsvbhtbl0f2njgt05z1ula"); //         obj->pen = parent->pen;
UNSUPPORTED("e20x3563jh67vt1lll5yvztoa"); //         obj->fill = parent->fill;
UNSUPPORTED("1vf36pbannt87rqhdedfp9tzg"); //         obj->penwidth = parent->penwidth;
UNSUPPORTED("bm8svjgexuo58jrlla94c7lmf"); // 	obj->gradient_angle = parent->gradient_angle;
UNSUPPORTED("96a2xfansfphgn02ik2pzbgsk"); // 	obj->stopcolor = parent->stopcolor;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("2hztnghjdrq4iblkkiw28e6lp"); // 	/* obj->pencolor = NULL */
UNSUPPORTED("f1vt7k6jpnvkr3l5hnrufbo7n"); // 	/* obj->fillcolor = NULL */
UNSUPPORTED("1d8vwhh78wsjnjv3b66v7k71f"); // 	obj->pen = PEN_SOLID;
UNSUPPORTED("327rx5pyklem7b31hdikywlqb"); // 	obj->fill = FILL_NONE;
UNSUPPORTED("8jey9s92w1zm7gze74a8q4dp6"); // 	obj->penwidth = 1.;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5ps6tm8zierqwp3ptuy9ntbwr"); //     return obj;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 fky2di67il17v6esshx73u77
// void pop_obj_state(GVJ_t *job) 
public static Object pop_obj_state(Object... arg) {
UNSUPPORTED("b5pcmjz1yx45rkkn6ihfese3y"); // void pop_obj_state(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("cvrjqjl5r3cwa1izxxgzgzrap"); //     assert(obj);
UNSUPPORTED("1ngdn0iitk41tdtce8t4ip3nx"); //     free(obj->id);
UNSUPPORTED("4di6e9e0vvr37qbzgje4ludyv"); //     free(obj->url);
UNSUPPORTED("11bs83ywfvb5bc5pmq26xjbe1"); //     free(obj->labelurl);
UNSUPPORTED("4iclkuumd2z23hcz3vtlmij9v"); //     free(obj->tailurl);
UNSUPPORTED("7w1xqpnr4u8nds6opogcbvnya"); //     free(obj->headurl);
UNSUPPORTED("b8dl0ih21bsnryi1pxe5tgqjp"); //     free(obj->tooltip);
UNSUPPORTED("9l3frodyf6j4nlcj24te5nrwp"); //     free(obj->labeltooltip);
UNSUPPORTED("tl0hkuk7y0kr8oegohd0xg83"); //     free(obj->tailtooltip);
UNSUPPORTED("8td9te5re4sfu60jumgh7qvpd"); //     free(obj->headtooltip);
UNSUPPORTED("azi0jshcues9wq2154bdnsfdu"); //     free(obj->target);
UNSUPPORTED("1bv7lwtfb303gq0pr61b5ie5e"); //     free(obj->labeltarget);
UNSUPPORTED("36xt1x2kwfa0fj8scj17o65hk"); //     free(obj->tailtarget);
UNSUPPORTED("eikpmw4wrmt4519cz2ixxv4hn"); //     free(obj->headtarget);
UNSUPPORTED("5g6xu70y47jbf9u1izz9d7lqm"); //     free(obj->url_map_p);
UNSUPPORTED("9d3byqiz2l6m12o73jh9el0av"); //     free(obj->url_bsplinemap_p);
UNSUPPORTED("e2337piojwep89s9v6hvret9f"); //     free(obj->url_bsplinemap_n);
UNSUPPORTED("36q9rct12zy148w6hckc97kcj"); //     job->obj = obj->parent;
UNSUPPORTED("8are8jqzwfj87yj4tkaohi3tf"); //     free(obj);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 z4e7tnt40d3c4kv5u7cyhl9x
// int initMapData (GVJ_t* job, char* lbl, char* url, char* tooltip, char* target, char *id,   void* gobj) 
public static Object initMapData(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("1r7179s3ntgv42pdh8lf7ri6p"); // initMapData (GVJ_t* job, char* lbl, char* url, char* tooltip, char* target, char *id,
UNSUPPORTED("us8je5vaodb65uh2al1w1c3h"); //   void* gobj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("5x4jq493dtdcx7z85qo0l2vwm"); //     int assigned = 0;
UNSUPPORTED("9c0gci8o1tk34epj1ssmue4dx"); //     if ((flags & (1<<15)) && lbl)
UNSUPPORTED("6hfoiuvpq4uvwmw4pxqqcjro6"); //         obj->label = lbl;
UNSUPPORTED("ekhzvq8l2u2frs2tl01cuf71s"); //     if (flags & (1<<16)) {
UNSUPPORTED("5u6uuo67mo3c4f2ojvfudltt5"); //         obj->id = strdup_and_subst_obj(id, gobj);
UNSUPPORTED("rss1781s9o1r7w5npkmc0bmg"); // 	if (url && url[0]) {
UNSUPPORTED("1sruesngpnjzqoec5jwxwxwj8"); //             obj->url = strdup_and_subst_obj(url, gobj);
UNSUPPORTED("e9av6ob52m06dxds7d0uontab"); // 	    assigned = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ep9t9tvrtouwjd7pulp378fzi"); //     if (flags & (1<<22)) {
UNSUPPORTED("ccrueeib4dlvmyrlpzxl572jv"); //         if (tooltip && tooltip[0]) {
UNSUPPORTED("96zad8kmhopk7wo5xdyxacpl0"); //             obj->tooltip = strdup_and_subst_obj(tooltip, gobj);
UNSUPPORTED("3fiobhu1ragr2f099ghsonx1h"); //             obj->explicit_tooltip = NOT(0);
UNSUPPORTED("e9av6ob52m06dxds7d0uontab"); // 	    assigned = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("a3azuqia2enebzovqhpnhquvj"); //         else if (obj->label) {
UNSUPPORTED("2l7dzbw9rqphv63paqq8wvbrc"); //             obj->tooltip = strdup(obj->label);
UNSUPPORTED("e9av6ob52m06dxds7d0uontab"); // 	    assigned = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4lne2bv62nrpmlhj27vxjy1f6"); //     if ((flags & (1<<23)) && target && target[0]) {
UNSUPPORTED("ebgucy53hwmcaz1m04q7dolf4"); //         obj->target = strdup_and_subst_obj(target, gobj);
UNSUPPORTED("3wreyz9de84yyv079xk4ujkv3"); // 	assigned = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2zpk6us1pyyltxkpge8hnuyn2"); //     return assigned;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d74beympg4wulmk8p6lh8daql
// static void layerPagePrefix (GVJ_t* job, agxbuf* xb) 
public static Object layerPagePrefix(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("6djudoziuh8bcxd8oxm1qch58"); // layerPagePrefix (GVJ_t* job, agxbuf* xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8gm1i2tpkqevee6guh43vbzjg"); //     char buf[128]; /* large enough for 2 decimal 64-bit ints and "page_," */
UNSUPPORTED("cxr89wmz3j80aqc8410masvdn"); //     if (job->layerNum > 1 && (job->flags & (1<<6))) {
UNSUPPORTED("bmg9rcy7b1x6gh95ojpwla6i8"); // 	agxbput (xb, job->gvc->layerIDs[job->layerNum]);
UNSUPPORTED("7eax6z8h47h2x1xxyqr1tkluc"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'_')));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("age7jtxi8adhl9jz4ne7xdp80"); //     if ((job->pagesArrayElem.x > 0) || (job->pagesArrayElem.x > 0)) {
UNSUPPORTED("cnwkbrftohdks7u707na56420"); // 	sprintf (buf, "page%d,%d_", job->pagesArrayElem.x, job->pagesArrayElem.y);
UNSUPPORTED("2i6wv75y4aki5i8plhbvy276v"); // 	agxbput (xb, buf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3uk5ht4qhgq91m46lw7xtdx8q
// char* getObjId (GVJ_t* job, void* obj, agxbuf* xb) 
public static Object getObjId(Object... arg) {
UNSUPPORTED("cqm25rponse4rsi686sbn1lo0"); // char*
UNSUPPORTED("cxy7pg7aplrpxxilr17uxmir1"); // getObjId (GVJ_t* job, void* obj, agxbuf* xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e7icadj7xe1r3gypdnhaaqh0z"); //     char* id;
UNSUPPORTED("c34mxcvhpn9flzo7mx3jta9v6"); //     graph_t* root = job->gvc->g;
UNSUPPORTED("4vz75ugb6dvgui5r4b3vha9w5"); //     char* gid = GD_drawing(root)->id;
UNSUPPORTED("b6fudxfj7yzgmij9ar0axs3cs"); //     long idnum;
UNSUPPORTED("1y49dkdztswfqp8gc2fsgsfjt"); //     char* pfx;
UNSUPPORTED("42dnqesv46qi3oy4hhv4xtngt"); //     char buf[64]; /* large enough for a decimal 64-bit int */
UNSUPPORTED("eyzyn1uwtp0eb367awnyqsnu9"); //     layerPagePrefix (job, xb);
UNSUPPORTED("4ivnihqhlqxt5urta85bj8cvo"); //     id = agget(obj, "id");
UNSUPPORTED("b5lnfc5x2s74jwjfi17neykdl"); //     if (id && (*id != '\0')) {
UNSUPPORTED("h8d3b8qgeb1asgpckee022dh"); // 	agxbput (xb, id);
UNSUPPORTED("7q3mbxgjfr96cttjsdev54twj"); // 	return (((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'\0'))),(char*)((xb)->ptr = (xb)->buf));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("442w4uruys9gula2urpooqejy"); //     if ((obj != root) && gid) {
UNSUPPORTED("b58nurhwbw66quisr070n2wi8"); // 	agxbput (xb, gid);
UNSUPPORTED("7eax6z8h47h2x1xxyqr1tkluc"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'_')));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91broiy562bqiwualvjbh1l0s"); //     switch (agobjkind(obj)) {
UNSUPPORTED("eyna33dobiebmtd0nihpgura4"); //     case AGRAPH:
UNSUPPORTED("250ofmvxldcwzahtn4zl83wcq"); // 	idnum = AGSEQ(obj);
UNSUPPORTED("7ivrapxz56e3axqq0wlp2c84o"); // 	if (root == obj)
UNSUPPORTED("8i5eqmk1n84h3oisk8qmr80fk"); // 	    pfx = "graph";
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("eghyuptvw5kbqlkky78xkyqch"); // 	    pfx = "clust";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("6x7ztvlgv763oeop84udp1egg"); //     case AGNODE:
UNSUPPORTED("dioszphfdf02j4mzkk1orixil"); //         idnum = AGSEQ((Agnode_t*)obj);
UNSUPPORTED("24c6tv0vcpu7u1sf37b5vd6bn"); // 	pfx = "node";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("2dqikmmb6ag0anvbzo5tybf1r"); //     case AGEDGE:
UNSUPPORTED("88j50o42ltjx7x5kljmhtaimc"); //         idnum = AGSEQ((Agedge_t*)obj);
UNSUPPORTED("8dj6qoxln9rok8xeaycmhigg3"); // 	pfx = "edge";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("doz63noywyk8negzqyp5wtn7y"); //     agxbput (xb, pfx);
UNSUPPORTED("9arliqoqxm070wy9ayr807ydh"); //     sprintf (buf, "%ld", idnum);
UNSUPPORTED("4nzvose05k5ompo6nn67jyf82"); //     agxbput (xb, buf);
UNSUPPORTED("ersrvcvo8qkfouomz222hiih7"); //     return (((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'\0'))),(char*)((xb)->ptr = (xb)->buf));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dcom8r4kkrsi0tbps20sjm7rl
// static void initObjMapData (GVJ_t* job, textlabel_t *lab, void* gobj) 
public static Object initObjMapData(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("ezkgjtfltwmwf154o793aps7z"); // initObjMapData (GVJ_t* job, textlabel_t *lab, void* gobj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4m1ejpxv77loq6ojxpmbi77gc"); //     char* lbl;
UNSUPPORTED("dr7hyuwqwq1d5pbc066tdwb5g"); //     char* url = agget(gobj, "href");
UNSUPPORTED("9eonbfqft5myxd7s5xqwvv974"); //     char* tooltip = agget(gobj, "tooltip");
UNSUPPORTED("5tk1r0qbghssa155we7canz54"); //     char* target = agget(gobj, "target");
UNSUPPORTED("e7icadj7xe1r3gypdnhaaqh0z"); //     char* id;
UNSUPPORTED("h0or3v13348vfl22jqz895yc"); //     unsigned char buf[128];
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("ci65k77x1b3nq6luu69s87oup"); //     agxbinit(&xb, 128, buf);
UNSUPPORTED("e1z56ssmce7u8j50hn74nzs06"); //     if (lab) lbl = lab->text;
UNSUPPORTED("bw2gbd060avck8ivr43eed32t"); //     else lbl = NULL;
UNSUPPORTED("9kl6bihhj6lqk5907u9vx0svh"); //     if (!url || !*url)  /* try URL as an alias for href */
UNSUPPORTED("5x0bbp126wunx54k3b5z6kqiv"); // 	url = agget(gobj, "URL");
UNSUPPORTED("5n6h8udz66glqsnvzl6q0ax4d"); //     id = getObjId (job, gobj, &xb);
UNSUPPORTED("5eb4m9ppzio68im6rq3z9owdi"); //     initMapData (job, lbl, url, tooltip, target, id, gobj);
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 hjkaeebrodvjebhg1hpg9z5e
// static void map_point(GVJ_t *job, pointf pf) 
public static Object map_point(Object... arg) {
UNSUPPORTED("8blzxhihaqxozyztu9fj7nshg"); // static void map_point(GVJ_t *job, pointf pf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("aukx8c3dz83p6cpnh0fhhnqo8"); //     pointf *p;
UNSUPPORTED("6ic0ku7wr32jsf5j0pwkrzq8j"); //     if (flags & ((1<<16) | (1<<22))) {
UNSUPPORTED("68o2j7ic39aasjk49deprnnhh"); // 	if (flags & (1<<17)) {
UNSUPPORTED("69ud33lfpzxun6ls6bpequzo8"); // 	    obj->url_map_shape = MAP_RECTANGLE;
UNSUPPORTED("d7uo4fiq8is4wmoeklxb9lrhm"); // 	    obj->url_map_n = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("5k8t4lz63jq26u2xqeoskhen7"); // 	    obj->url_map_shape = MAP_POLYGON;
UNSUPPORTED("ah8hfbgy2ofsubklkazu04w4d"); // 	    obj->url_map_n = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9wnyoic97gd28arr56l0i8iy3"); // 	free(obj->url_map_p);
UNSUPPORTED("ec0oubjt8fhapkogfpg8s7y13"); // 	obj->url_map_p = p = (pointf*)zmalloc((obj->url_map_n)*sizeof(pointf));
UNSUPPORTED("d7tqa8sc5yn0805hga1a0lhy2"); // 	(p[0].x = pf.x - 3, p[0].y = pf.y - 3, p[1].x = pf.x + 3, p[1].y = pf.y + 3);
UNSUPPORTED("cnadzqqicrskcdzko360llcg5"); // 	if (! (flags & (1<<13)))
UNSUPPORTED("9626u9a6bmyb4pqydfy9oe48w"); // 	    gvrender_ptf_A(job, p, p, 2);
UNSUPPORTED("3qt79u8muuulxatmo48vjk6r3"); // 	if (! (flags & (1<<17)))
UNSUPPORTED("e0f10b7obog3b7z2tra14ajtx"); // 	    rect2poly(p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dlyzfsrfmu4fl7l50uzdvu3mc
// static char **checkClusterStyle(graph_t* sg, int *flagp) 
public static Object checkClusterStyle(Object... arg) {
UNSUPPORTED("4fe1xtiblsit7upcg8o8vn11n"); // static char **checkClusterStyle(graph_t* sg, int *flagp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("byo308l1rmve5rmx8wt32juux"); //     char *style;
UNSUPPORTED("1m69eum6fcxl62kbxksis88or"); //     char **pstyle = 0;
UNSUPPORTED("1y4qclfm9xrlqe6oi5k512dih"); //     int istyle = 0;
UNSUPPORTED("c77cb1sgh5mlnuscrdp9pxwm8"); //     if (((style = agget(sg, "style")) != 0) && style[0]) {
UNSUPPORTED("h7xdp8l98vgmu8dkibee5kcm"); // 	char **pp;
UNSUPPORTED("9uleufnyvtzwmq2wbcnmeyp3a"); // 	char **qp;
UNSUPPORTED("1ys4x1uj0hoyf2yuvrmddwh9i"); // 	char *p;
UNSUPPORTED("6akvfrhnmmgier60pl8bkt42r"); // 	pp = pstyle = parse_style(style);
UNSUPPORTED("9vj0v0tutidnb2hxprdosjm39"); // 	while ((p = *pp)) {
UNSUPPORTED("e3mpm8potxtvjdd42cnwhluxl"); // 	    if (strcmp(p, "filled") == 0) {
UNSUPPORTED("5uu1d9grveb0bmsov10mqrtj3"); // 		istyle |= (1 << 0);
UNSUPPORTED("5lcmi10wuwp3fyn36cloc29y3"); // 		pp++;
UNSUPPORTED("5fxeuc5zc3643ly5dyson06s5"); //  	    }else if (strcmp(p, "radial") == 0) {
UNSUPPORTED("a3fnxu8lgm7c35op5nmoe8xj6"); //  		istyle |= ((1 << 0) | (1 << 1));
UNSUPPORTED("9bo1rem63xdcqaveddsqb89y"); // 		qp = pp; /* remove rounded from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("7jb3shp3mthw99uh3iir44z47"); // 		    *(qp-1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("9wcwovvjpprmy66u5cc2hpy3i"); //  	    }else if (strcmp(p, "striped") == 0) {
UNSUPPORTED("6tcwivyh9w6pmh1ys8hf9m48o"); //  		istyle |= (1 << 6);
UNSUPPORTED("9bo1rem63xdcqaveddsqb89y"); // 		qp = pp; /* remove rounded from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("7jb3shp3mthw99uh3iir44z47"); // 		    *(qp-1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("agd1qpujy8ioolsnjhmz0zapr"); // 	    }else if (strcmp(p, "rounded") == 0) {
UNSUPPORTED("dtj2r1tvn8fuoefvhmbg7k8es"); // 		istyle |= (1 << 2);
UNSUPPORTED("9bo1rem63xdcqaveddsqb89y"); // 		qp = pp; /* remove rounded from list passed to renderer */
UNSUPPORTED("21y8hlxutle9votbe86f2hfpl"); // 		do {
UNSUPPORTED("7ca407ksqxmxkuny8m7gr1rzj"); // 		    qp++;
UNSUPPORTED("7jb3shp3mthw99uh3iir44z47"); // 		    *(qp-1) = *qp;
UNSUPPORTED("1ujv3j8mb7i0c6nzdfgy27w4s"); // 		} while (*qp);
UNSUPPORTED("7z03qzrkm0iobzqhlwp87ljr2"); // 	    } else pp++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8wc6gkmat3lo0h81wd7xv879k"); //     *flagp = istyle;
UNSUPPORTED("iuoxnbeo1cwfgz97k92ylegx"); //     return pstyle;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9iqjkcr5mukm81hrbwh0ea6fl
// static void freeSegs (colorsegs_t* segs) 
public static Object freeSegs(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("464xlb3ygxv6p9mg54yjog4xx"); // freeSegs (colorsegs_t* segs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("byogskcr9egmgq0x5kmqvwi4v"); //     free (segs->base);
UNSUPPORTED("70o1qrp2ik2rtbnfye965jlti"); //     free (segs->segs);
UNSUPPORTED("864f5ylvxgoy5rwmwl4pstg2p"); //     free (segs);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 97btw75unjbstma95wtq0zatf
// static double getSegLen (char* s) 
public static Object getSegLen(Object... arg) {
UNSUPPORTED("9xupxgb2zpj09jpcf9avjwewg"); // static double getSegLen (char* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2xm53iv3ul2vyq7751jovt7yv"); //     char* p = strchr (s, ';');
UNSUPPORTED("bkz9mqmemp1ljxdwdbu8xv3e9"); //     char* endp;
UNSUPPORTED("9gol5nm38e942ve1ebl1cjtlv"); //     double v;
UNSUPPORTED("e5uuzflrromc49aac8g763pcj"); //     if (!p) {
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ad4vrj0twr5jijvunb1bz9koj"); //     *p++ = '\0';
UNSUPPORTED("8hxu46s395rpsel8pqxzl9ana"); //     v = strtod (p, &endp);
UNSUPPORTED("bog16vtrt7asrtd5qswrdityh"); //     if (endp != p) {  /* scanned something */
UNSUPPORTED("rqt6x5hv72afst4yu8og4eh1"); // 	if (v >= 0)
UNSUPPORTED("b9t6ne0njxrwqqcdw8t4lt7kt"); // 	    return v;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8azkpi8o0wzdufa90lw8hpt6q"); //     return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8zfj6ri8h66qg7vjb6r0gtwl5
// static int parseSegs (char* clrs, int nseg, colorsegs_t** psegs) 
public static Object parseSegs(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("apdtd2ytdipe6mbcrtuzg8me3"); // parseSegs (char* clrs, int nseg, colorsegs_t** psegs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b2p5bomt1ebfvkood2varaauq"); //     colorsegs_t* segs = (colorsegs_t*)zmalloc(sizeof(colorsegs_t));
UNSUPPORTED("8zv1ffgik05z2fcc41di32sn8"); //     colorseg_t* s;
UNSUPPORTED("elj0n3kcg90w49mdluubv8bns"); //     char* colors = strdup (clrs);
UNSUPPORTED("3tsetuywdzi8crkdp7yfkzgs0"); //     char* color;
UNSUPPORTED("c4mq9v0j066932j65m2kds553"); //     int cnum = 0;
UNSUPPORTED("5wbs6wytrjo8k4jbvke5n0u42"); //     double v, left = 1;
UNSUPPORTED("4tnoq3emby6jh6mtizghjicok"); //     static int doWarn = 1;
UNSUPPORTED("a8vxj66zhdcnifeyd5g50smwk"); //     int i, rval = 0;
UNSUPPORTED("a4px33i4moqe8ybwatz0g8k6"); //     char* p;
UNSUPPORTED("axv64lmdgc8l7wrse4f2uje2c"); //     if (nseg == 0) {
UNSUPPORTED("9kkvl2096j2mcfgl88zxrg3mt"); // 	nseg = 1;
UNSUPPORTED("1p0odq4xbiiyk9zulrtgh78p7"); // 	/* need to know how many colors separated by ':' */
UNSUPPORTED("8lwg5726u0argoi40r0o86mvp"); // 	for (p = colors; *p; p++) {
UNSUPPORTED("1rfsopgavnlzprt3drtcsj0x4"); // 	    if (*p == ':') nseg++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9bp5sal0vgfug2x1wf511qs7f"); //     segs->base = colors;
UNSUPPORTED("2ivkw78cp3krrv7689cye5s78"); //     segs->segs = s = (colorseg_t*)zmalloc((nseg+1)*sizeof(colorseg_t));
UNSUPPORTED("4szmily3ea0fmu6uuro6gu156"); //     for (color = strtok(colors, ":"); color; color = strtok(0, ":")) {
UNSUPPORTED("4004f4k7a6sfsgb610fgbmu5w"); // 	if ((v = getSegLen (color)) >= 0) {
UNSUPPORTED("azbip4rt1t97r5xu2s3fylz3y"); // 	    double del = v - left;
UNSUPPORTED("8h66ar8vsn5btqcntrimnkktm"); // 	    if (del > 0) {
UNSUPPORTED("ed89ssfw3aoze3hsxopkgecse"); // 		if (doWarn && !(((del) < 1E-5) && ((del) > -1E-5))) {
UNSUPPORTED("bteqpt6c6yz7o7d5dbmof10wj"); // 		    agerr (AGWARN, "Total size > 1 in \"%s\" color spec ", clrs);
UNSUPPORTED("24grm1ux24glgzb3vunz0hgk2"); // 		    doWarn = 0;
UNSUPPORTED("7uujii1ibtc8h2vtmdczkb97f"); // 		    rval = 3;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("c5j07yi4nl2nzq8fjmkv7tsby"); // 		v = left;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("733sj1rtrkdhnykyrn3z1muzd"); // 	    left -= v;
UNSUPPORTED("7dvqls8r4achyytx9jb4ce5nd"); // 	    if (v > 0) s[cnum].hasFraction = NOT(0);
UNSUPPORTED("3jt663rt945dkhqkl1rv5w95u"); // 	    if (*color) s[cnum].color = color;
UNSUPPORTED("c1mbrfego3c40gci714lbozdu"); // 	    s[cnum++].t = v;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("7qqjev9ey61berllhtqdtio5j"); // 	    if (doWarn) {
UNSUPPORTED("1uclwqp3vls9evizalhsqup1f"); // 		agerr (AGERR, "Illegal length value in \"%s\" color attribute ", clrs);
UNSUPPORTED("ccs8p4eo4ehywnii033x2xz3k"); // 		doWarn = 0;
UNSUPPORTED("4o9eycynbr1xrbs7xu7wqkoqo"); // 		rval = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5spew51c4hokx3tvptq38hoif"); // 	    else rval = 1;
UNSUPPORTED("723lxfi188696x4nsqtjqibuc"); // 	    freeSegs (segs);
UNSUPPORTED("4sm28tt9yujgmryycwcj3ozcs"); // 	    return rval;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dcahiv0k45nuszddfmx52qvsl"); // 	if ((((left) < 1E-5) && ((left) > -1E-5))) {
UNSUPPORTED("546rex5xh4grberu1heou0wg1"); // 	    left = 0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8wynr3ctvgfgo13bcha3yofvl"); //     /* distribute remaining into slot with t == 0; if none, add to last */
UNSUPPORTED("az8pyhrjnf2hzlv8tkhg72zw6"); //     if (left > 0) {
UNSUPPORTED("dutfiykx9omnitayjorbg1vx2"); // 	/* count zero segments */
UNSUPPORTED("49ujuxia41bf3tqa9jq7skubi"); // 	nseg = 0;
UNSUPPORTED("7wc6473fldnm8onr6x54aoknb"); // 	for (i = 0; i < cnum; i++) {
UNSUPPORTED("hw7c0mgetn36f6qtikru7dxh"); // 	    if (s[i].t == 0) nseg++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e5xpvo7h1n57u6ajux2emdsjl"); // 	if (nseg > 0) {
UNSUPPORTED("b9220xzm85t055qqrbe3x2o91"); // 	    double delta = left/nseg;
UNSUPPORTED("2tpczuw50n6atxp3ugll97g2l"); // 	    for (i = 0; i < cnum; i++) {
UNSUPPORTED("datujg26m1qe4v9viophnfqmk"); // 		if (s[i].t == 0) s[i].t = delta;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("cpvqe6ppslph7n78gdtd33ll2"); // 	    s[cnum-1].t += left;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bzphdbk9lv18c2kha9yj9p9bq"); //     /* Make sure last positive segment is followed by a sentinel. */
UNSUPPORTED("dnru0y416hypltckpasyalvi1"); //     nseg = 0;
UNSUPPORTED("8es1vpe9qvsc5j4mi98x0h6ml"); //     for (i = cnum-1; i >= 0; i--) {
UNSUPPORTED("8eg8vanptayonxgtj46f8qo4h"); // 	if (s[i].t > 0) break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("63bgrye6x8o4p2w5u6ix8cfnh"); //     s[i+1].color = NULL;
UNSUPPORTED("cg9otruqfz0hiw1to7o5dbvz2"); //     segs->numc = i+1;
UNSUPPORTED("9evut88r5b01zyynad10pnqj6"); //     *psegs = segs;
UNSUPPORTED("bhpm3hntm4gkn66ozolahaqhb"); //     return rval;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5mu8biu5ww33wd5ieraclh88z
// int  wedgedEllipse (GVJ_t* job, pointf * pf, char* clrs) 
public static Object wedgedEllipse(Object... arg) {
UNSUPPORTED("7zkpme13g8rxxwloxvpvvnbcw"); // int 
UNSUPPORTED("aa0ojv015bm44zwcwqav8hg79"); // wedgedEllipse (GVJ_t* job, pointf * pf, char* clrs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8z3a2qjysx0m326m5qjy91tnu"); //     colorsegs_t* segs;
UNSUPPORTED("8zv1ffgik05z2fcc41di32sn8"); //     colorseg_t* s;
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("8vpvinya0zfrnowhlphkyccd5"); //     double save_penwidth = job->obj->penwidth;
UNSUPPORTED("1nq1fbyjqq72b0sc2bulz00rw"); //     pointf ctr, semi;
UNSUPPORTED("qw8wtu53e7k1aubdv962neuz"); //     Ppolyline_t* pp;
UNSUPPORTED("ecf4tlsg5qocdf5cl3xt0usir"); //     double angle0, angle1;
UNSUPPORTED("7w32bzfgk50pyvzfs7qaxoi1e"); //     rv = parseSegs (clrs, 0, &segs);
UNSUPPORTED("45tn760kszitmijgc00zjux63"); //     if ((rv == 1) || (rv == 2)) return rv;
UNSUPPORTED("5a05hfj5quc4ubw4bd0bbtjoq"); //     ctr.x = (pf[0].x + pf[1].x) / 2.;
UNSUPPORTED("65mz1z9ktoe9whfa6lwcmvscf"); //     ctr.y = (pf[0].y + pf[1].y) / 2.;
UNSUPPORTED("8ji2e4xqar7owxkedpin2m8ez"); //     semi.x = pf[1].x - ctr.x;
UNSUPPORTED("92ipp30q5f40j3d2wqk9xu4od"); //     semi.y = pf[1].y - ctr.y;
UNSUPPORTED("2qlp0rgdgbozdtvtuwqp37pa4"); //     if (save_penwidth > 0.5)
UNSUPPORTED("hmdk2acbhhzqbukut4pqgz2q"); // 	gvrender_set_penwidth(job, 0.5);
UNSUPPORTED("dddsxf9cjjlgn8v2snkxl6e1z"); //     angle0 = 0;
UNSUPPORTED("9lw73gbrcdxipj1j1b1kpyvfc"); //     for (s = segs->segs; s->color; s++) {
UNSUPPORTED("8wmjgptzcrcqkza35tcf7btrq"); // 	if (s->t == 0) continue;
UNSUPPORTED("40pn9i6e6b8aw6gvfysg075ae"); // 	gvrender_set_fillcolor (job, (s->color?s->color:"black"));
UNSUPPORTED("4vzp246p162a5yt5k0d62gcz6"); // 	if (s[1].color == NULL) 
UNSUPPORTED("9647s4q9zvdoqktuh274a9y3e"); // 	    angle1 = 2*M_PI;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("caiwb8g1acalpp0esm3dy0be2"); // 	    angle1 = angle0 + 2*M_PI*(s->t);
UNSUPPORTED("9ll9d8jthp2hl8sh52a8h2dgp"); // 	pp = ellipticWedge (ctr, semi.x, semi.y, angle0, angle1);
UNSUPPORTED("7arx1xvmroi48xi8occ15h0qj"); // 	gvrender_beziercurve(job, pp->ps, pp->pn, 0, 0, 1);
UNSUPPORTED("dtpeo119iz3e8pf4sty2eypec"); // 	angle0 = angle1;
UNSUPPORTED("bku6jthxqindqm1lycet3lgq1"); // 	freePath (pp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2qlp0rgdgbozdtvtuwqp37pa4"); //     if (save_penwidth > 0.5)
UNSUPPORTED("efgc8uu9zhn5gek92wvnw2cqt"); // 	gvrender_set_penwidth(job, save_penwidth);
UNSUPPORTED("dwbftn5fib1yir7gshhy14cup"); //     freeSegs (segs);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1lx9cef1wqmf2k66twye6mf9g
// int stripedBox (GVJ_t * job, pointf* AF, char* clrs, int rotate) 
public static Object stripedBox(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("ekv2bika3f3ammx903i7szf5x"); // stripedBox (GVJ_t * job, pointf* AF, char* clrs, int rotate)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8z3a2qjysx0m326m5qjy91tnu"); //     colorsegs_t* segs;
UNSUPPORTED("8zv1ffgik05z2fcc41di32sn8"); //     colorseg_t* s;
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("cvn5016us8gcyjc73vgd06clq"); //     double xdelta;
UNSUPPORTED("d8sp6cfkpfyndzmid92shiq50"); //     pointf pts[4];
UNSUPPORTED("aqsj4hnm1mckt6pv4pqok37cv"); //     double lastx;
UNSUPPORTED("8vpvinya0zfrnowhlphkyccd5"); //     double save_penwidth = job->obj->penwidth;
UNSUPPORTED("7w32bzfgk50pyvzfs7qaxoi1e"); //     rv = parseSegs (clrs, 0, &segs);
UNSUPPORTED("45tn760kszitmijgc00zjux63"); //     if ((rv == 1) || (rv == 2)) return rv;
UNSUPPORTED("csirqgn1exx47m56awd7suhlq"); //     if (rotate) {
UNSUPPORTED("6yvxg6bp1ape4nmefxjyjjhti"); // 	pts[0] = AF[2];
UNSUPPORTED("7x2bhgf39q42g2fhxk0vfkxxk"); // 	pts[1] = AF[3];
UNSUPPORTED("f179v6qur6kketo9cy3lotml7"); // 	pts[2] = AF[0];
UNSUPPORTED("b1kk11zisn7fvypwmt593hdwp"); // 	pts[3] = AF[1];
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("eolhyp22bac440ap3o96c3mqz"); // 	pts[0] = AF[0];
UNSUPPORTED("7bgongoew7lrxnvqed64y1aq5"); // 	pts[1] = AF[1];
UNSUPPORTED("dsj2jtc38wd64ffe18qloaybe"); // 	pts[2] = AF[2];
UNSUPPORTED("w8lcp07u15ugb9pbyz628ux6"); // 	pts[3] = AF[3];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8cka942q7qvwsisxeamy9xfgs"); //     lastx = pts[1].x;
UNSUPPORTED("el6wjonhvfaekmhry0o1xncfi"); //     xdelta = (pts[1].x - pts[0].x);
UNSUPPORTED("m16l42bmexhmji8xy54ow3ac"); //     pts[1].x = pts[2].x = pts[0].x;
UNSUPPORTED("2qlp0rgdgbozdtvtuwqp37pa4"); //     if (save_penwidth > 0.5)
UNSUPPORTED("hmdk2acbhhzqbukut4pqgz2q"); // 	gvrender_set_penwidth(job, 0.5);
UNSUPPORTED("9lw73gbrcdxipj1j1b1kpyvfc"); //     for (s = segs->segs; s->color; s++) {
UNSUPPORTED("8wmjgptzcrcqkza35tcf7btrq"); // 	if (s->t == 0) continue;
UNSUPPORTED("40pn9i6e6b8aw6gvfysg075ae"); // 	gvrender_set_fillcolor (job, (s->color?s->color:"black"));
UNSUPPORTED("3r3zv42eo8ubdity1juxhmhh4"); // 	/* gvrender_polygon(job, pts, 4, FILL | NO_POLY); */
UNSUPPORTED("4vzp246p162a5yt5k0d62gcz6"); // 	if (s[1].color == NULL) 
UNSUPPORTED("9zsrpebctk9utmb3xexrrnn5n"); // 	    pts[1].x = pts[2].x = lastx;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("cnyxri62od5y8ghnijv4z9wsb"); // 	    pts[1].x = pts[2].x = pts[0].x + xdelta*(s->t);
UNSUPPORTED("7s4cl2nfmsmuphbzuu24n43gk"); // 	gvrender_polygon(job, pts, 4, 1);
UNSUPPORTED("8g6jo6gn7rt47zmjtrbh705qi"); // 	pts[0].x = pts[3].x = pts[1].x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2qlp0rgdgbozdtvtuwqp37pa4"); //     if (save_penwidth > 0.5)
UNSUPPORTED("efgc8uu9zhn5gek92wvnw2cqt"); // 	gvrender_set_penwidth(job, save_penwidth);
UNSUPPORTED("dwbftn5fib1yir7gshhy14cup"); //     freeSegs (segs);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a4ze3nkth5n070iajraile73b
// void emit_map_rect(GVJ_t *job, boxf b) 
public static Object emit_map_rect(Object... arg) {
UNSUPPORTED("co9kbfer4pfzrsg32xzdxnu2u"); // void emit_map_rect(GVJ_t *job, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("aukx8c3dz83p6cpnh0fhhnqo8"); //     pointf *p;
UNSUPPORTED("6ic0ku7wr32jsf5j0pwkrzq8j"); //     if (flags & ((1<<16) | (1<<22))) {
UNSUPPORTED("68o2j7ic39aasjk49deprnnhh"); // 	if (flags & (1<<17)) {
UNSUPPORTED("69ud33lfpzxun6ls6bpequzo8"); // 	    obj->url_map_shape = MAP_RECTANGLE;
UNSUPPORTED("d7uo4fiq8is4wmoeklxb9lrhm"); // 	    obj->url_map_n = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("5k8t4lz63jq26u2xqeoskhen7"); // 	    obj->url_map_shape = MAP_POLYGON;
UNSUPPORTED("ah8hfbgy2ofsubklkazu04w4d"); // 	    obj->url_map_n = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9wnyoic97gd28arr56l0i8iy3"); // 	free(obj->url_map_p);
UNSUPPORTED("ec0oubjt8fhapkogfpg8s7y13"); // 	obj->url_map_p = p = (pointf*)zmalloc((obj->url_map_n)*sizeof(pointf));
UNSUPPORTED("67z1fedypeeyro29m0t5h6cii"); // 	p[0] = b.LL;
UNSUPPORTED("dskfal9yhsof4338w6yry9mve"); // 	p[1] = b.UR;
UNSUPPORTED("cnadzqqicrskcdzko360llcg5"); // 	if (! (flags & (1<<13)))
UNSUPPORTED("9626u9a6bmyb4pqydfy9oe48w"); // 	    gvrender_ptf_A(job, p, p, 2);
UNSUPPORTED("3qt79u8muuulxatmo48vjk6r3"); // 	if (! (flags & (1<<17)))
UNSUPPORTED("e0f10b7obog3b7z2tra14ajtx"); // 	    rect2poly(p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3ai5lu5hhd5fuikmo022v1jm0
// static void map_label(GVJ_t *job, textlabel_t *lab) 
public static Object map_label(Object... arg) {
UNSUPPORTED("c4rnzzthoq9f8agcrqudi4mz"); // static void map_label(GVJ_t *job, textlabel_t *lab)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("aukx8c3dz83p6cpnh0fhhnqo8"); //     pointf *p;
UNSUPPORTED("6ic0ku7wr32jsf5j0pwkrzq8j"); //     if (flags & ((1<<16) | (1<<22))) {
UNSUPPORTED("68o2j7ic39aasjk49deprnnhh"); // 	if (flags & (1<<17)) {
UNSUPPORTED("69ud33lfpzxun6ls6bpequzo8"); // 	    obj->url_map_shape = MAP_RECTANGLE;
UNSUPPORTED("d7uo4fiq8is4wmoeklxb9lrhm"); // 	    obj->url_map_n = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("5k8t4lz63jq26u2xqeoskhen7"); // 	    obj->url_map_shape = MAP_POLYGON;
UNSUPPORTED("ah8hfbgy2ofsubklkazu04w4d"); // 	    obj->url_map_n = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9wnyoic97gd28arr56l0i8iy3"); // 	free(obj->url_map_p);
UNSUPPORTED("ec0oubjt8fhapkogfpg8s7y13"); // 	obj->url_map_p = p = (pointf*)zmalloc((obj->url_map_n)*sizeof(pointf));
UNSUPPORTED("pv2pug94o7tyi304mtm18jaw"); // 	(p[0].x = lab->pos.x - lab->dimen.x / 2., p[0].y = lab->pos.y - lab->dimen.y / 2., p[1].x = lab->pos.x + lab->dimen.x / 2., p[1].y = lab->pos.y + lab->dimen.y / 2.);
UNSUPPORTED("cnadzqqicrskcdzko360llcg5"); // 	if (! (flags & (1<<13)))
UNSUPPORTED("9626u9a6bmyb4pqydfy9oe48w"); // 	    gvrender_ptf_A(job, p, p, 2);
UNSUPPORTED("3qt79u8muuulxatmo48vjk6r3"); // 	if (! (flags & (1<<17)))
UNSUPPORTED("e0f10b7obog3b7z2tra14ajtx"); // 	    rect2poly(p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9hfoy16yb9l36g778lx6o4loa
// static boolean isRect(polygon_t * p) 
public static Object isRect(Object... arg) {
UNSUPPORTED("9boj1p1495tsl7kbb9os62euc"); // static boolean isRect(polygon_t * p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("jq836u6s0o7a40yevy0sjlhv"); //     return (p->sides == 4 && (ROUND(p->orientation) % 90) == 0
UNSUPPORTED("2pp49cjtsontnjugs7vlomdvi"); //             && p->distortion == 0.0 && p->skew == 0.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 drf7d6idpinqx579kloyv3tjf
// static int ifFilled(node_t * n) 
public static Object ifFilled(Object... arg) {
UNSUPPORTED("576n3ll5ycnzfrne5a0vg4kmb"); // static int ifFilled(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ezo17twsxmg7baw2hkcmffhbr"); //     char *style, *p, **pp;
UNSUPPORTED("9lfwryf4m8bo4travw1edcxtr"); //     int r = 0;
UNSUPPORTED("8pjk3xuss8ew2p7e7t0djo8qp"); //     style = late_nnstring(n, N_style, "");
UNSUPPORTED("4ouo3ttcnk1yyzsz5wrt88zw"); //     if (style[0]) {
UNSUPPORTED("a7llauzxp1up7djc1wq1dnfco"); //         pp = parse_style(style);
UNSUPPORTED("307dibrb4o6rniseaep5vofol"); //         while ((p = *pp)) {
UNSUPPORTED("36yyrnflvlp1fqax3117d8a7o"); //             if (strcmp(p, "filled") == 0)
UNSUPPORTED("dxmd648tdx5y1devieu0sclw"); //                 r = 1;
UNSUPPORTED("8dkwt30u4binxrx5y6sqggg8i"); //             pp++;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3sh9756i5zm4vgebijiohr21q
// static pointf *pEllipse(double a, double b, int np) 
public static Object pEllipse(Object... arg) {
UNSUPPORTED("81ja3n1hoznf26wc2vhvhl9fj"); // static pointf *pEllipse(double a, double b, int np)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("39lrh4yp65e7bz2yxmw4eykrf"); //     double theta = 0.0;
UNSUPPORTED("eyglfufrpugwrnbykbifud876"); //     double deltheta = 2 * M_PI / np;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("2rkzhui0essisp5zlw44vx4j9"); //     pointf *ps;
UNSUPPORTED("9plpwtwefveugoac2qi9x6chi"); //     ps = (pointf*)zmalloc((np)*sizeof(pointf));
UNSUPPORTED("8sd8edat3oeyfbnetgt6bwa6l"); //     for (i = 0; i < np; i++) {
UNSUPPORTED("78zadymnp5kv296ust519rwwy"); //         ps[i].x = a * cos(theta);
UNSUPPORTED("7aqmygicj0weq2ez2gpdasxzj"); //         ps[i].y = b * sin(theta);
UNSUPPORTED("4tksxfn79hejj7dk9u7587tdx"); //         theta += deltheta;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b0dfwpxhogdrp9mwkzc8oa9vt"); //     return ps;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7nqmdkcnal35ollpstkk707t8
// static int check_control_points(pointf *cp) 
public static boolean check_control_points(__ptr__ cp) {
ENTERING("7nqmdkcnal35ollpstkk707t8","check_control_points");
try {
    double dis1 = ptToLine2 ((ST_pointf)cp.plus(0).getStruct(), (ST_pointf)cp.plus(3).getStruct(), (ST_pointf)cp.plus(1).getStruct());
    double dis2 = ptToLine2 ((ST_pointf)cp.plus(0).getStruct(), (ST_pointf)cp.plus(3).getStruct(), (ST_pointf)cp.plus(2).getStruct());
    if (dis1 < 2.0*2.0 && dis2 < 2.0*2.0)
        return true;
    else
        return false;
} finally {
LEAVING("7nqmdkcnal35ollpstkk707t8","check_control_points");
}
}




//3 5wldemr88fdxl6101ugewclw9
// void update_bb_bz(boxf *bb, pointf *cp) 
public static void update_bb_bz(ST_boxf bb, ST_pointf.Array cp) {
ENTERING("5wldemr88fdxl6101ugewclw9","update_bb_bz");
try {
    /* if any control point of the segment is outside the bounding box */
    if (cp.get(0).x > bb.UR.x || cp.get(0).x < bb.LL.x ||
        cp.get(0).y > bb.UR.y || cp.get(0).y < bb.LL.y ||
        cp.get(1).x > bb.UR.x || cp.get(1).x < bb.LL.x ||
        cp.get(1).y > bb.UR.y || cp.get(1).y < bb.LL.y ||
        cp.get(2).x > bb.UR.x || cp.get(2).x < bb.LL.x ||
        cp.get(2).y > bb.UR.y || cp.get(2).y < bb.LL.y ||
        cp.get(3).x > bb.UR.x || cp.get(3).x < bb.LL.x ||
        cp.get(3).y > bb.UR.y || cp.get(3).y < bb.LL.y) {
        /* if the segment is sufficiently refined */
        if (check_control_points(cp.asPtr())) {        
            int i;
            /* expand the bounding box */
            for (i = 0; i < 4; i++) {
                if (cp.get(i).x > bb.UR.x)
                    bb.UR.setDouble("x", cp.get(i).x);
                else if (cp.get(i).x < bb.LL.x)
                    bb.LL.setDouble("x", cp.get(i).x);
                if (cp.get(i).y > bb.UR.y)
                    bb.UR.setDouble("y", cp.get(i).y);
                else if (cp.get(i).y < bb.LL.y)
                    bb.LL.setDouble("y", cp.get(i).y);
            }
        }
        else { /* else refine the segment */
		    final ST_pointf.Array left = new ST_pointf.Array( 4);
		    final ST_pointf.Array right = new ST_pointf.Array( 4);
            Bezier (cp, 3, 0.5, left.asPtr(), right.asPtr());
            update_bb_bz(bb, left);
            update_bb_bz(bb, right);
        }
    }
} finally {
LEAVING("5wldemr88fdxl6101ugewclw9","update_bb_bz");
}
}




//3 5inp24tkswwr4gef832cfsh04
// static segitem_t* appendSeg (pointf p, segitem_t* lp) 
public static Object appendSeg(Object... arg) {
UNSUPPORTED("9ox1lgfkkrjj3l9g1jpe0gs04"); // static segitem_t* appendSeg (pointf p, segitem_t* lp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bk3628thwysou5cfzrxarcqte"); //     segitem_t* s = (segitem_t*)gmalloc(sizeof(segitem_t));
UNSUPPORTED("3tnumckwljivhxj7b6mdf24d6"); //     {(s)->next = 0; (s)->p = p;};
UNSUPPORTED("46ylfb4pi0znpxitzsije2mhw"); //     lp->next = s;
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4n0kvrpdwpl0e1imvz963aa1o
// static void map_bspline_poly(pointf **pbs_p, int **pbs_n, int *pbs_poly_n, int n, pointf* p1, pointf* p2) 
public static Object map_bspline_poly(Object... arg) {
UNSUPPORTED("1r3l20vlipm0x1r2dprvygc9i"); // static void map_bspline_poly(pointf **pbs_p, int **pbs_n, int *pbs_poly_n, int n, pointf* p1, pointf* p2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("30xnf4zdgb1ykf89oqbuo4l2a"); //     int i = 0, nump = 0, last = 2*n-1;
UNSUPPORTED("f06mx73hq0m616fggpl7pm6l0"); //     for ( ; i < *pbs_poly_n; i++)
UNSUPPORTED("9bq6z8tgn463c2b2b1ujzzu64"); //         nump += (*pbs_n)[i];
UNSUPPORTED("6xsx7pggjnc2umqgysrjf2o7f"); //     (*pbs_poly_n)++;
UNSUPPORTED("cavf92uaf58wsqimp1tktt9u5"); //     *pbs_n = grealloc(*pbs_n, (*pbs_poly_n) * sizeof(int));
UNSUPPORTED("9v28ohj2y5y341vn7mh44qo8"); //     (*pbs_n)[i] = 2*n;
UNSUPPORTED("8uvz87etb5208kleb7ehkzyfc"); //     *pbs_p = grealloc(*pbs_p, (nump + 2*n) * sizeof(pointf));
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("6hab7v9b2nr1fy1es1y7q8vhn"); //         (*pbs_p)[nump+i] = p1[i];
UNSUPPORTED("2qtwscm7pv5eahjzljn3gwez3"); //         (*pbs_p)[nump+last-i] = p2[i];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7vktvjafa2lmwlaxsy0nbulut
// static segitem_t* approx_bezier (pointf *cp, segitem_t* lp) 
public static Object approx_bezier(Object... arg) {
UNSUPPORTED("al673209lz1hskkt7drx6qaib"); // static segitem_t* approx_bezier (pointf *cp, segitem_t* lp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9460wahmu30hhy9lbmjxls3ju"); //     pointf left[4], right[4];
UNSUPPORTED("eomhceyowlehxg33yuxfclcdz"); //     if (check_control_points(cp)) {
UNSUPPORTED("czol56efilyo93ev9cxz3ytyo"); //         if (((lp)->next == (segitem_t*)1)) {(lp)->next = 0; (lp)->p = cp[0];};
UNSUPPORTED("1358cb4a22h5h06oy1a6j08sq"); //         lp = appendSeg (cp[3], lp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("9czjzt923nh9t1r0ofwg1uiqi"); //         Bezier (cp, 3, 0.5, left, right);
UNSUPPORTED("b0liuqott07bzgsycyywz3wgi"); //         lp = approx_bezier (left, lp);
UNSUPPORTED("3fohjex9x5u6w5e9ivere17uz"); //         lp = approx_bezier (right, lp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("22f0z0bsq8tczqmt8rdxhmye9"); //     return lp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3nlf0dmvl0qwc88vhsi69eo85
// static double bisect (pointf pp, pointf cp, pointf np) 
public static Object bisect(Object... arg) {
UNSUPPORTED("8h36arb302hy4bmfyuq5lvq4d"); // static double bisect (pointf pp, pointf cp, pointf np)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8kh11h44zj580voonfm37tko9"); //   double ang, theta, phi;
UNSUPPORTED("85xsvegtkipyc8fvrmffpt7on"); //   theta = atan2(np.y - cp.y,np.x - cp.x);
UNSUPPORTED("c3wm3ceipv4mlnttlnfmsnx9c"); //   phi = atan2(pp.y - cp.y,pp.x - cp.x);
UNSUPPORTED("b0ntn6nfptzm2qbjo2ls49i1c"); //   ang = theta - phi;
UNSUPPORTED("4li40ysz6584mc8iejchar0ej"); //   if (ang > 0) ang -= 2*M_PI;
UNSUPPORTED("col2agnktr81l4ybzpnuvni0b"); //   return (phi + ang/2.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 45zvw50w2tfskogjqa2wzlmhg
// static void mkSegPts (segitem_t* prv, segitem_t* cur, segitem_t* nxt,         pointf* p1, pointf* p2, double w2) 
public static Object mkSegPts(Object... arg) {
UNSUPPORTED("5hv5nomfe90vtds8hxhox7yej"); // static void mkSegPts (segitem_t* prv, segitem_t* cur, segitem_t* nxt,
UNSUPPORTED("78rchge0ctod4mkpq2fslrh0s"); //         pointf* p1, pointf* p2, double w2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("egh0kbv4tt7cyg4y0jqpw892u"); //     pointf cp, pp, np;
UNSUPPORTED("biw1k2scxl82ky4h2e5gzzz3w"); //     double theta, delx, dely;
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("1uw3lcxgjre9mhnye9n5mc3nq"); //     cp = cur->p;
UNSUPPORTED("8oyy7q664cu8y31s5gm98jaok"); //     /* if prv or nxt are NULL, use the one given to create a collinear
UNSUPPORTED("7cujcl5h2n20wpsmy96ce8y1t"); //      * prv or nxt. This could be more efficiently done with special case code, 
UNSUPPORTED("90m6pb4mxn7bdssm66d6ex652"); //      * but this way is more uniform.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("4m73973c5to6aicgd30f6yzaf"); //     if (prv) {
UNSUPPORTED("c1rauhosdsgiv6485akroiljy"); //         pp = prv->p;
UNSUPPORTED("ethb044vg7vhe5m2lhq81kab3"); //         if (nxt)
UNSUPPORTED("5svwwfzkdun5jxqdn4mouh5n6"); //             np = nxt->p;
UNSUPPORTED("3jir07ymknf0hmb9pv9x4dr3o"); //         else {
UNSUPPORTED("b6z52jimoq1wmtvxf8qjyx889"); //             np.x = 2*cp.x - pp.x;
UNSUPPORTED("7plg4dt2urchak7x50b0qhyz2"); //             np.y = 2*cp.y - pp.y;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("8hw6aqovl83yx8u0xlbnkcbw2"); //         np = nxt->p;
UNSUPPORTED("a6vni7nsbr4l53ceg9tyv09i8"); //         pp.x = 2*cp.x - np.x;
UNSUPPORTED("9r62maaemrr57dm3tdh7fuva6"); //         pp.y = 2*cp.y - np.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3ktptkz1bjdklvgym7woyu60x"); //     theta = bisect(pp,cp,np);
UNSUPPORTED("a4adtr9lu9kofe8mqolr4fg4r"); //     delx = w2*cos(theta);
UNSUPPORTED("4uljbij5lrj26jqovnqnbkzxc"); //     dely = w2*sin(theta);
UNSUPPORTED("6r23zes3ngn7ela6z0elabnsq"); //     p.x = cp.x + delx;
UNSUPPORTED("4uzttmp6bugdo2qudn1xrr8fj"); //     p.y = cp.y + dely;
UNSUPPORTED("bntn382sm9mnloab7gci08f64"); //     *p1 = p;
UNSUPPORTED("92rv8dekgpna6e0qh7la8kyg0"); //     p.x = cp.x - delx;
UNSUPPORTED("2czgguvp48sudmjwiuoanrvbq"); //     p.y = cp.y - dely;
UNSUPPORTED("823jqic6za2w9bn6bxd5sfovp"); //     *p2 = p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3xmhrtq5g7trpp4u40xr0b8y2
// static void map_output_bspline (pointf **pbs, int **pbs_n, int *pbs_poly_n, bezier* bp, double w2) 
public static Object map_output_bspline(Object... arg) {
UNSUPPORTED("6lvrwl802yfwlo7kfixgg5nez"); // static void map_output_bspline (pointf **pbs, int **pbs_n, int *pbs_poly_n, bezier* bp, double w2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("460a9s2we7gle00rxon2fthvv"); //     segitem_t* segl = (segitem_t*)gmalloc(sizeof(segitem_t));
UNSUPPORTED("7jg00n3u4jk162hmdo63f64lo"); //     segitem_t* segp = segl;
UNSUPPORTED("b8pna82nkq66ot9l709idym2o"); //     segitem_t* segprev;
UNSUPPORTED("agsgwjn5iwlfedcqbv8ckm57"); //     segitem_t* segnext;
UNSUPPORTED("8kw0juwrrjw2z97umyrg0g1qd"); //     int nc, j, k, cnt;
UNSUPPORTED("8d1z0mjgb910nvnha97hjtek8"); //     pointf pts[4], pt1[50], pt2[50];
UNSUPPORTED("asm74enhle0g30mwnz8t1buui"); //     ((segl)->next = (segitem_t*)1);
UNSUPPORTED("2adno2mo8kzsvrdtc21rhj4v5"); //     nc = (bp->size - 1)/3; /* nc is number of bezier curves */
UNSUPPORTED("7swib3bakj4z6ubjcayxfitj6"); //     for (j = 0; j < nc; j++) {
UNSUPPORTED("95y530ux2hdmxrhr3nyzp3g9t"); //         for (k = 0; k < 4; k++) {
UNSUPPORTED("c9uvgse79w32172gjma953aq2"); //             pts[k] = bp->list[3*j + k];
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5sy2c7piyke76572zgaizewud"); //         segp = approx_bezier (pts, segp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6ivsqczanxvoll90ol5rf9nwj"); //     segp = segl;
UNSUPPORTED("7934mrgiu0ms3qybvycy3cgay"); //     segprev = 0;
UNSUPPORTED("3wtn792c3ql5yhn77alu6r5d8"); //     cnt = 0;
UNSUPPORTED("2aqvc301awlepsejrafp4yyc6"); //     while (segp) {
UNSUPPORTED("3aw5thxio3mh4yvrwep3op3cb"); //         segnext = segp->next;
UNSUPPORTED("4ov54jbry4z74fejjwuepxe84"); //         mkSegPts (segprev, segp, segnext, pt1+cnt, pt2+cnt, w2);
UNSUPPORTED("77mslev6fngkh87g2jkd6j87j"); //         cnt++;
UNSUPPORTED("2r5ivfdgqpq5a6nyot9hijti3"); //         if ((segnext == NULL) || (cnt == 50)) {
UNSUPPORTED("8upks3wb9w2i7a6fib6tg86so"); //             map_bspline_poly (pbs, pbs_n, pbs_poly_n, cnt, pt1, pt2);
UNSUPPORTED("br4bzacgnowkbgloqwj6hi6xi"); //             pt1[0] = pt1[cnt-1];
UNSUPPORTED("1a82ke2ad8rgjec4c9c58r8nz"); //             pt2[0] = pt2[cnt-1];
UNSUPPORTED("5n5phg49hlzthwyjtssepk4pr"); //             cnt = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("3m3komtzmujd239y2dgskiyj6"); //         segprev = segp;
UNSUPPORTED("7gox3cmouztk9pgbp4kyi21n3"); //         segp = segnext;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f3da2ac9kdmfwv0pfno2zrouc"); //     /* free segl */
UNSUPPORTED("5h71fr0i2yoyjffmruxsjp21z"); //     while (segl) {
UNSUPPORTED("diwidyhfydftnczhbvpa03d1"); //         segp = segl->next;
UNSUPPORTED("n2x8n88pew5ot3r4s1zgnqw9"); //         free (segl);
UNSUPPORTED("9facw1k96gpqu3xnm2hjb1d2c"); //         segl = segp;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9xju3of8zy5f2mk0f6vd6kxoi
// static boolean is_natural_number(char *sstr) 
public static Object is_natural_number(Object... arg) {
UNSUPPORTED("9rhheqjcnai59q1k0d8y9jynt"); // static boolean is_natural_number(char *sstr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dlob9vbxvn4cj6g0rosu94jd"); //     unsigned char *str = (unsigned char *) sstr;
UNSUPPORTED("alw7hro2r95xcs6yzutcluxtm"); //     while (*str)
UNSUPPORTED("9m3itcf8i2mq6xorvv3zdwhh"); // 	if (NOT(isdigit(*str++)))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4udru8pf61208dtuf22zzmmqd
// static int layer_index(GVC_t *gvc, char *str, int all) 
public static Object layer_index(Object... arg) {
UNSUPPORTED("3lxm6mluhtqpfsa9nzkd3apqi"); // static int layer_index(GVC_t *gvc, char *str, int all)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3ak6myanfllb9z1xz4e6c0nhf"); //     /* GVJ_t *job = gvc->job; */
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("ddtbvyocaoyzuds5nmnsodhqb"); //     if ((*(str)==*("all")&&!strcmp(str,"all")))
UNSUPPORTED("8duf2go7r0jiqw3wnkiqbol4r"); // 	return all;
UNSUPPORTED("8ztkkwz0e702fr9np4y6o4kuu"); //     if (is_natural_number(str))
UNSUPPORTED("34uc4omjjar6x7rskk72va3j6"); // 	return atoi(str);
UNSUPPORTED("1s842n3ke9usoirca1dwsrrzx"); //     if (gvc->layerIDs)
UNSUPPORTED("cpogmg64n6exvblx87d1pmwfo"); // 	for (i = 1; i <= gvc->numLayers; i++)
UNSUPPORTED("939rzzi8dp0sws71xjv2n1d66"); // 	    if ((*(str)==*(gvc->layerIDs[i])&&!strcmp(str,gvc->layerIDs[i])))
UNSUPPORTED("b9e4nv60rh6o1ai85uu0ougv"); // 		return i;
UNSUPPORTED("8azkpi8o0wzdufa90lw8hpt6q"); //     return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a8rdk6dogn52h5grbbxfoq34z
// static boolean selectedLayer(GVC_t *gvc, int layerNum, int numLayers, char *spec) 
public static Object selectedLayer(Object... arg) {
UNSUPPORTED("eri18s9ivcrynir2bpx4jnw8g"); // static boolean selectedLayer(GVC_t *gvc, int layerNum, int numLayers, char *spec)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("15uc40a82mi1mztghnyvoy460"); //     int n0, n1;
UNSUPPORTED("h0or3v13348vfl22jqz895yc"); //     unsigned char buf[128];
UNSUPPORTED("2zdui2eukxklqn2ig8nh0bw1t"); //     char *w0, *w1;
UNSUPPORTED("ccg75yolmilezu48nio7ksbf9"); //     char *buf_part_p = NULL, *buf_p = NULL, *cur, *part_in_p;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("9h6icoq4cc2e2ibff10l3xrob"); //     boolean rval = 0;
UNSUPPORTED("ci65k77x1b3nq6luu69s87oup"); //     agxbinit(&xb, 128, buf);
UNSUPPORTED("bhiyk7f25o6jysgsxycaf5vbo"); //     agxbput(&xb, spec);
UNSUPPORTED("aww221dmkj4h9x0qmlvai6bun"); //     part_in_p = (((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf));
UNSUPPORTED("8o0g4i9g8jc2qoljztsf5l69"); //     /* Thanks to Matteo Nastasi for this extended code. */
UNSUPPORTED("5wc9kd553mqdmeqrzv5u3kuyv"); //     while ((rval == 0) && (cur = strtok_r(part_in_p, gvc->layerListDelims, &buf_part_p))) {
UNSUPPORTED("5mvzc9us20jr0bcopdypz0mfo"); // 	w1 = w0 = strtok_r (cur, gvc->layerDelims, &buf_p);
UNSUPPORTED("snrpgbkmqz2ba186d24cmfnx"); // 	if (w0)
UNSUPPORTED("bx1n828umga5916irui1tb7cx"); // 	    w1 = strtok_r (NULL, gvc->layerDelims, &buf_p);
UNSUPPORTED("crroc3un37wt9d0omuvaq8gca"); // 	switch ((w0 != NULL) + (w1 != NULL)) {
UNSUPPORTED("46lzlkypfilrge90rkaiveuyb"); // 	case 0:
UNSUPPORTED("1y7jah5hullcu9snkfa1oy1f2"); // 	    rval = 0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2o83im06dulx11wjpy469gkoa"); // 	case 1:
UNSUPPORTED("1jygz6h6idqhtvh7bqd78fhy2"); // 	    n0 = layer_index(gvc, w0, layerNum);
UNSUPPORTED("c8zzp13vx1yth31xuqj1661c8"); // 	    rval = (n0 == layerNum);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("b8vgbvwzllfs4lrqmmqyr1spk"); // 	case 2:
UNSUPPORTED("9y7cribaclzkutrf9lusdgb73"); // 	    n0 = layer_index(gvc, w0, 0);
UNSUPPORTED("45uxr9q1d74dbhay2ugv205w0"); // 	    n1 = layer_index(gvc, w1, numLayers);
UNSUPPORTED("2gcf1ylvtukspepmlgp37irq5"); // 	    if ((n0 >= 0) || (n1 >= 0)) {
UNSUPPORTED("ad3pm1secuibs7iqrobtxw2is"); // 		if (n0 > n1) {
UNSUPPORTED("en01k6v1n32c2swyx0elf6pcg"); // 		    int t = n0;
UNSUPPORTED("2bjyjz9cvls6hyytpcmbkgmcs"); // 		    n0 = n1;
UNSUPPORTED("bpseot9l807jidv28yid809wm"); // 		    n1 = t;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("cer1dt8c42dt0eigzkalgg0u4"); // 		rval = BETWEEN(n0, layerNum, n1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("59btplbumo93hp7myb8mvi5ee"); // 	part_in_p = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("bhpm3hntm4gkn66ozolahaqhb"); //     return rval;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2zlzcril6uiesf8z9zo8bvfyz
// static boolean selectedlayer(GVJ_t *job, char *spec) 
public static Object selectedlayer(Object... arg) {
UNSUPPORTED("3nzphmy08w2y4a3yntr24xw0p"); // static boolean selectedlayer(GVJ_t *job, char *spec)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("any52sc1hzc52gsl6k37qteok"); //     return selectedLayer (job->gvc, job->layerNum, job->numLayers, spec);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8elt2ktgbiurz9gdlvgjpr8mp
// static int* parse_layerselect(GVC_t *gvc, graph_t * g, char *p) 
public static Object parse_layerselect(Object... arg) {
UNSUPPORTED("6lvivuwtc6e4wjgguepfq8nud"); // static int* parse_layerselect(GVC_t *gvc, graph_t * g, char *p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ezxtli9973z4een4uoj2cfsho"); //     int* laylist = (int*)gmalloc((gvc->numLayers+2)*sizeof(int));
UNSUPPORTED("b5a93beg34iiqtnufv9wanh2r"); //     int i, cnt = 0;
UNSUPPORTED("c2uuo0qyuykgxql7fsz1al9h4"); //     for (i = 1; i <=gvc->numLayers; i++) {
UNSUPPORTED("7u51wobeze34in0qy7cnjmqv5"); // 	if (selectedLayer (gvc, i, gvc->numLayers, p)) {
UNSUPPORTED("b5ymvvr45fvj1wbcdyoe57nnd"); // 	    laylist[++cnt] = i;
UNSUPPORTED("8nzcpbtoi924xzu8ze3z6dbft"); // 	} 
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("77njmf3w9m7xk714hk5o8hjv5"); //     if (cnt) {
UNSUPPORTED("f3878e9jng3sa7ipt8cmlofqs"); // 	laylist[0] = cnt;
UNSUPPORTED("a3k7ll5ie5r0j4h3t4tefex3b"); // 	laylist[cnt+1] = gvc->numLayers+1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("c0ejg4cn6ao9jn7vtsuqug4gl"); // 	agerr(AGWARN, "The layerselect attribute \"%s\" does not match any layer specifed by the layers attribute - ignored.\n", p);
UNSUPPORTED("f3878e9jng3sa7ipt8cmlofqs"); // 	laylist[0] = cnt;
UNSUPPORTED("8skx11npk6vn3hbxvcuxklf6v"); // 	free (laylist);
UNSUPPORTED("45iwrgdr374i74cf8em6uwfmx"); // 	laylist = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("30gwy84osotlzmeicu7kfluyp"); //     return laylist;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4kiahfclenuf921uyxg4lzlbs
// static int parse_layers(GVC_t *gvc, graph_t * g, char *p) 
public static Object parse_layers(Object... arg) {
UNSUPPORTED("1spuppzfx1me4g279wfftgu5n"); // static int parse_layers(GVC_t *gvc, graph_t * g, char *p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7v0207mxa8hqzexz49lscukl8"); //     int ntok;
UNSUPPORTED("eqt7m58ywry2anaa5wpii01ri"); //     char *tok;
UNSUPPORTED("bpt627vzs0r1tzo7jgpyjdssp"); //     int sz;
UNSUPPORTED("2v5nye0hqxoem0cn4n22cgcov"); //     gvc->layerDelims = agget(g, "layersep");
UNSUPPORTED("2gnxlhqmvfrjzjd75hm5mx6em"); //     if (!gvc->layerDelims)
UNSUPPORTED("aia1lj9r0sseuxbuefdo14bao"); //         gvc->layerDelims = ":\t ";
UNSUPPORTED("6b9c2dn4yffcjru8dxyzsu37e"); //     gvc->layerListDelims = agget(g, "layerlistsep");
UNSUPPORTED("7wf2w4t3x9ak3skyqq8y1okhb"); //     if (!gvc->layerListDelims)
UNSUPPORTED("563hs3cgmcdcqb6l4rrpv75ek"); //         gvc->layerListDelims = ",";
UNSUPPORTED("8r1h2mlb2u87klqp21y9ilhie"); //     if ((tok = strpbrk (gvc->layerDelims, gvc->layerListDelims))) { /* conflict in delimiter strings */
UNSUPPORTED("q0y5qqfvuie4x5nbuh4ltwml"); // 	agerr(AGWARN, "The character \'%c\' appears in both the layersep and layerlistsep attributes - layerlistsep ignored.\n", *tok);
UNSUPPORTED("5yy5l7k4jffs33fkk86gnxx1y"); //         gvc->layerListDelims = "";
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c4wye11e21cuxetha8fjaezcj"); //     ntok = 0;
UNSUPPORTED("1gxcbnw3tixp11pk9jkijirao"); //     sz = 0;
UNSUPPORTED("2ejr8g3scrhv7fxwj1tno13zd"); //     gvc->layers = strdup(p);
UNSUPPORTED("6q7eqo62kv90x2ch8rce2nhwq"); //     for (tok = strtok(gvc->layers, gvc->layerDelims); tok;
UNSUPPORTED("577hr51xn1w20008653y51w8j"); //          tok = strtok(NULL, gvc->layerDelims)) {
UNSUPPORTED("7gwdsxgme4t8qinotj0dxsvcw"); //         ntok++;
UNSUPPORTED("2ffbgwipxpwoyr4fd0k4ha0nq"); //         if (ntok > sz) {
UNSUPPORTED("d3yzx9720tr2kq7hefgjdkrp6"); //             sz += 128;
UNSUPPORTED("32u0qx8yeqxu4r0bk3enhqi0n"); //             gvc->layerIDs = ALLOC(sz, gvc->layerIDs, char *);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("3q96x283efzk6afscurxeve1h"); //         gvc->layerIDs[ntok] = tok;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b0n1l1dvbn6aov6g3xvi2i0ur"); //     if (ntok) {
UNSUPPORTED("epu81n9xme9sxje3laz6szujx"); //         gvc->layerIDs = RALLOC(ntok + 2, gvc->layerIDs, char *);        /* shrink to minimum size */
UNSUPPORTED("yd1fq95zbahnl8ztvhpjb71k"); //         gvc->layerIDs[0] = NULL;
UNSUPPORTED("557694uxqmnbvy9qb7t1armld"); //         gvc->layerIDs[ntok + 1] = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c4f8xkcsh8jpj7admwanbez66"); //     return ntok;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4gyjzihgl8che7plolivfm89h
// static int chkOrder(graph_t * g) 
public static Object chkOrder(Object... arg) {
UNSUPPORTED("9jzoupo86al5szoppdb9ug4jm"); // static int chkOrder(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7g8u2dbeh9st5st5kp6oshdu5"); //     char *p = agget(g, "outputorder");
UNSUPPORTED("3cvmixd2u1g2d9l03kuxyyxxw"); //     if (p) {
UNSUPPORTED("a88zp39raphu4mpw0pi8oq1hi"); //         char c = *p;
UNSUPPORTED("a11i71rfjtl0mxznvr2rsxg4r"); //         if ((c == 'n') && !strcmp(p + 1, "odesfirst"))
UNSUPPORTED("d6qsecgbj60rq8xaef9umqge9"); //             return (1<<0);
UNSUPPORTED("a5f5xut0kypunfkizx4yu77ae"); //         if ((c == 'e') && !strcmp(p + 1, "dgesfirst"))
UNSUPPORTED("9boy87u68njstsw43wxf5urh9"); //             return (1<<4);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 33hjwrwudmjobd5yhbvu86jxz
// static void init_layering(GVC_t * gvc, graph_t * g) 
public static Object init_layering(Object... arg) {
UNSUPPORTED("7iqktnxyjnxn51eeuuy0oh1uz"); // static void init_layering(GVC_t * gvc, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("76nok3eiyr33qf4ecv69ujxn6"); //     char *str;
UNSUPPORTED("avab4390fpb68yais5eijodkt"); //     /* free layer strings and pointers from previous graph */
UNSUPPORTED("8zsh0fgdj4ri6w73fr47391cy"); //     if (gvc->layers) {
UNSUPPORTED("6zw20p1kbgu9ycram23hk8ece"); // 	free(gvc->layers);
UNSUPPORTED("a012u6bc7d55ctuf92taryny"); // 	gvc->layers = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("42i2j3ry654uexmr231uhaqw8"); //     if (gvc->layerIDs) {
UNSUPPORTED("5x7zl391qtijlzmv7wg3my1uh"); // 	free(gvc->layerIDs);
UNSUPPORTED("b4z8eoj6aem9v33o3snekjt37"); // 	gvc->layerIDs = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1zd3la1zp34vbtw6eiyojzrxi"); //     if (gvc->layerlist) {
UNSUPPORTED("c2onv8y2o0rubycnv6cvmfgxx"); // 	free(gvc->layerlist);
UNSUPPORTED("1ovwh9w5ny7xovqo71y582jpw"); // 	gvc->layerlist = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("efigtm6w6713gs6f8j1kdon3p"); //     if ((str = agget(g, "layers")) != 0) {
UNSUPPORTED("vdhfuyl28ln9lsrhizcu26ri"); // 	gvc->numLayers = parse_layers(gvc, g, str);
UNSUPPORTED("2zl1bqc97q5o2qjpkdo7svkrj"); //  	if (((str = agget(g, "layerselect")) != 0) && *str) {
UNSUPPORTED("428c4zbkpplv4awwcg3ne3q2i"); // 	    gvc->layerlist = parse_layerselect(gvc, g, str);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("b4z8eoj6aem9v33o3snekjt37"); // 	gvc->layerIDs = NULL;
UNSUPPORTED("d1k0kld2zrm4eln7qp4kx6yaz"); // 	gvc->numLayers = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3edoapk4bnc3rsmt3huwoqf9g
// static int numPhysicalLayers (GVJ_t *job) 
public static Object numPhysicalLayers(Object... arg) {
UNSUPPORTED("5zoarmqyf1m3t67nl5xahrslu"); // static int numPhysicalLayers (GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bzx9zh3zx8kyugkbdhlk4qzte"); //     if (job->gvc->layerlist) {
UNSUPPORTED("60ik4am7yzmctb72j26n5lr2q"); // 	return job->gvc->layerlist[0];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("1aeayu3ipqii7ienwaw2fclmc"); // 	return job->numLayers;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cpiumi39pczm8e5154f9udcco
// static void firstlayer(GVJ_t *job, int** listp) 
public static Object firstlayer(Object... arg) {
UNSUPPORTED("5e25vrbqvnj3q41zkb05shjgt"); // static void firstlayer(GVJ_t *job, int** listp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("90n549z2ovp2q6sbcp8ovmgl5"); //     job->numLayers = job->gvc->numLayers;
UNSUPPORTED("bzx9zh3zx8kyugkbdhlk4qzte"); //     if (job->gvc->layerlist) {
UNSUPPORTED("2fuv307u38d5mdk8l04r6u4wq"); // 	int *list = job->gvc->layerlist;
UNSUPPORTED("y80sygu21ondz3a4lzihqypj"); // 	int cnt = *list++;
UNSUPPORTED("5pz4cc2cq395z3znk5opultm2"); // 	if ((cnt > 1) && (! (job->flags & (1<<6)))) {
UNSUPPORTED("4gruo7b3gf61r0fw9j6awgase"); // 	    agerr(AGWARN, "layers not supported in %s output\n",
UNSUPPORTED("6aw3ie76clejxz6q2120b63i8"); // 		job->output_langname);
UNSUPPORTED("edlmftq4sdx2y0m6ghban11vo"); // 	    list[1] = job->numLayers + 1; /* only one layer printed */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("397jrexm8oa7doqg2bmttotgy"); // 	job->layerNum = *list++;
UNSUPPORTED("74506hoopc6w4pd2tieyyk052"); // 	*listp = list;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("9njkqrmknfdjv143rey9fn21b"); // 	if ((job->numLayers > 1)
UNSUPPORTED("b6082dq1axeyie3r2u3gmt90y"); // 		&& (! (job->flags & (1<<6)))) {
UNSUPPORTED("4gruo7b3gf61r0fw9j6awgase"); // 	    agerr(AGWARN, "layers not supported in %s output\n",
UNSUPPORTED("6aw3ie76clejxz6q2120b63i8"); // 		job->output_langname);
UNSUPPORTED("7aldo2ccuxu5nz7wrjpi2kgbk"); // 	    job->numLayers = 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8fsuke0q52rma3v3dzl4r7nxl"); // 	job->layerNum = 1;
UNSUPPORTED("8by4zer0vztykds6f7wo09dxl"); // 	*listp = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b445mlh9alsqmydjwlh3lzsnz
// static boolean validlayer(GVJ_t *job) 
public static Object validlayer(Object... arg) {
UNSUPPORTED("bfi9hniz5k2qe0dadeqq4ki5b"); // static boolean validlayer(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("45m14336nzkr3zwmroo7wwmop"); //     return (job->layerNum <= job->numLayers);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3y21gcvzhj6vn9gl9g6gag119
// static void nextlayer(GVJ_t *job, int** listp) 
public static Object nextlayer(Object... arg) {
UNSUPPORTED("28yugcvd6qow68yasmq967pbn"); // static void nextlayer(GVJ_t *job, int** listp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("20yv9az4kpafd26nxc9kj1ggt"); //     int *list = *listp;
UNSUPPORTED("essmww0ueu4ulfn9wjtq7m4vl"); //     if (list) {
UNSUPPORTED("397jrexm8oa7doqg2bmttotgy"); // 	job->layerNum = *list++;
UNSUPPORTED("74506hoopc6w4pd2tieyyk052"); // 	*listp = list;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2xz0u3jthtaln82mr50e5afw8"); // 	job->layerNum++;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dpqjdl72ma03lvh522bej00pf
// static point pagecode(GVJ_t *job, char c) 
public static Object pagecode(Object... arg) {
UNSUPPORTED("858jif62sg7t8bfj0q5dgi77b"); // static point pagecode(GVJ_t *job, char c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e5r3mj8btrkw973m7l0rritko"); //     point rv;
UNSUPPORTED("b2ritu2u2kl5u66e81tvv21f8"); //     rv.x = rv.y = 0;
UNSUPPORTED("8amt8fmqdipygnxirowfbawox"); //     switch (c) {
UNSUPPORTED("2s6hwvc7utwke7l45suhmumhk"); //     case 'T':
UNSUPPORTED("13gd95p7284sp2kf1rup3gssg"); // 	job->pagesArrayFirst.y = job->pagesArraySize.y - 1;
UNSUPPORTED("6nseewwep98wlkw901fwmkkr4"); // 	rv.y = -1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9nbe45sgjnq9pebqfddm57j34"); //     case 'B':
UNSUPPORTED("8wyp8gbxgnri6agdl2zv0nzbv"); // 	rv.y = 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("92wd4259ggzetbnn56kh75cap"); //     case 'L':
UNSUPPORTED("9zlyi4rkx1oa6app1mk6zdqs1"); // 	rv.x = 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9rqaici2t4gmxgbc4jjbi5147"); //     case 'R':
UNSUPPORTED("d4nni7llszxdo8f1w0qosk8n2"); // 	job->pagesArrayFirst.x = job->pagesArraySize.x - 1;
UNSUPPORTED("92r9rc8z07ot90pz7kfoilce8"); // 	rv.x = -1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4wn2yfi2l0bfzlw5fyeeunug7
// static void init_job_pagination(GVJ_t * job, graph_t *g) 
public static Object init_job_pagination(Object... arg) {
UNSUPPORTED("3i2vk6s34y6kty29dnciiqr2i"); // static void init_job_pagination(GVJ_t * job, graph_t *g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("1p9i7j8ykvafmept9v326ki38"); //     pointf pageSize;	/* page size for the graph - points*/
UNSUPPORTED("cbreumvyopnc8jb823f3fg490"); //     pointf imageSize;	/* image size on one page of the graph - points */
UNSUPPORTED("bvl8rq7rhjfofyjexcv6ath9j"); //     pointf margin;	/* margin for a page of the graph - points */
UNSUPPORTED("dlxve5gfi5a0trnjntul0l7vt"); //     pointf centering = {0.0, 0.0}; /* centering offset - points */
UNSUPPORTED("7wipi75cu71j2ubfdid86r3mz"); //     /* unpaginated image size - in points - in graph orientation */
UNSUPPORTED("4hd3j07l34jpp34kggybuv7g7"); //     imageSize = job->view;
UNSUPPORTED("euhifkui6pusftx1ts79yvw64"); //     /* rotate imageSize to page orientation */
UNSUPPORTED("eoj2n5u9aqzo40fe4thgnio4v"); //     if (job->rotation)
UNSUPPORTED("5bktmmmrdanogwscr9akvqvgp"); // 	imageSize = exch_xyf(imageSize);
UNSUPPORTED("cj9bqu9dsr79jfgtfgxyw1bt7"); //     /* margin - in points - in page orientation */
UNSUPPORTED("2prr8ugshi1wh3b0uqq1rytbq"); //     margin = job->margin;
UNSUPPORTED("e0syewo5shkbuvckfgxahgawk"); //     /* determine pagination */
UNSUPPORTED("3ben86oxo4b9mvbfear0uaa5k"); //     if (gvc->graph_sets_pageSize && (job->flags & (1<<5))) {
UNSUPPORTED("e9k3pfk8bwgj68mf5plbws00a"); // 	/* page was set by user */
UNSUPPORTED("26ouotfw5qjqy45epxvh85q2a"); //         /* determine size of page for image */
UNSUPPORTED("cmeyghelvujbqypzncob2k347"); // 	pageSize.x = gvc->pageSize.x - 2 * margin.x;
UNSUPPORTED("4hhbnhebhvdvguv07gl1exqzr"); // 	pageSize.y = gvc->pageSize.y - 2 * margin.y;
UNSUPPORTED("7dzf9uvb5ahp4ja7cchy16e9q"); // 	if (pageSize.x < .0001)
UNSUPPORTED("8qfk6mlrxuxel1fxryn3xxsw7"); // 	    job->pagesArraySize.x = 1;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("39zkkuuxu0d1bo4xiaa6rvio1"); // 	    job->pagesArraySize.x = (int)(imageSize.x / pageSize.x);
UNSUPPORTED("crpbptuooqpagkwwoovfr87ze"); // 	    if ((imageSize.x - (job->pagesArraySize.x * pageSize.x)) > .0001)
UNSUPPORTED("fvkmj6z0flmool752kj3ezui"); // 		job->pagesArraySize.x++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("aeqvjc584z1k3o3btf1z27yi"); // 	if (pageSize.y < .0001)
UNSUPPORTED("9i2hzfup3uawsgs8xz84cvm3u"); // 	    job->pagesArraySize.y = 1;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("eu05pk9cbv4r7teo48863rut5"); // 	    job->pagesArraySize.y = (int)(imageSize.y / pageSize.y);
UNSUPPORTED("9ec6tcamvhf35mwg760yp31tj"); // 	    if ((imageSize.y - (job->pagesArraySize.y * pageSize.y)) > .0001)
UNSUPPORTED("2ptmkss0hqmoiaitn5hc03ccy"); // 		job->pagesArraySize.y++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dz4mq3t7i81irfy7jpgoo8slq"); // 	job->numPages = job->pagesArraySize.x * job->pagesArraySize.y;
UNSUPPORTED("8umi1sn4x93betwy7p6c2ceqk"); // 	/* find the drawable size in points */
UNSUPPORTED("eei96a8vzvruyjbss0f6i4qe9"); // 	imageSize.x = MIN(imageSize.x, pageSize.x);
UNSUPPORTED("a7bw5k7czo7ubsb5x8785vxd7"); // 	imageSize.y = MIN(imageSize.y, pageSize.y);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("226xpnvf8rs1n0r4ei8klybwz"); // 	/* page not set by user, use default from renderer */
UNSUPPORTED("916om7kp0deyvh2ghmt7tmt47"); // 	if (job->render.features) {
UNSUPPORTED("21jloev0l8fed4itydbq5py3k"); // 	    pageSize.x = job->device.features->default_pagesize.x - 2*margin.x;
UNSUPPORTED("6c84lafv8rataik7pt8umib2"); // 	    if (pageSize.x < 0.)
UNSUPPORTED("86x0hskgajl1rvbco28b3ftih"); // 		pageSize.x = 0.;
UNSUPPORTED("bvs3ijknux4wq8q8oh2adxyyp"); // 	    pageSize.y = job->device.features->default_pagesize.y - 2*margin.y;
UNSUPPORTED("5ff2kpbbm9iear9jk4p33l1eu"); // 	    if (pageSize.y < 0.)
UNSUPPORTED("nnszo3g5yfwbcgr1rfyh9htr"); // 		pageSize.y = 0.;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("7flj6fib45vcdli5dtp5hzydu"); // 	    pageSize.x = pageSize.y = 0.;
UNSUPPORTED("563r9ecafak4g1eplsnjkqdi3"); // 	job->pagesArraySize.x = job->pagesArraySize.y = job->numPages = 1;
UNSUPPORTED("2ey4w19bytoq2sls78pheoku7"); //         if (pageSize.x < imageSize.x)
UNSUPPORTED("15499m2w5qjd6yd4xa4ixhvjr"); // 	    pageSize.x = imageSize.x;
UNSUPPORTED("austii7xnr5g8y2fjg8l6gl3"); //         if (pageSize.y < imageSize.y)
UNSUPPORTED("bblz8gxp9uoak4lx31ok0b8b8"); // 	    pageSize.y = imageSize.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bbafhwu8tnhlc6acwajgx43vy"); //     /* initial window size */
UNSUPPORTED("8362vxhywm5vjg5yy2rukot0l"); // //fprintf(stderr,"page=%g,%g dpi=%g,%g zoom=%g\n", pageSize.x, pageSize.y, job->dpi.x, job->dpi.y, job->zoom);
UNSUPPORTED("9ov3mqr5woomu5pa3ijim18bp"); //     job->width = ROUND((pageSize.x + 2*margin.x) * job->dpi.x / 72);
UNSUPPORTED("dtc0guw406xkcekddmamm5rae"); //     job->height = ROUND((pageSize.y + 2*margin.y) * job->dpi.y / 72);
UNSUPPORTED("7osvdh761zt2s8mi855ch8az8"); //     /* set up pagedir */
UNSUPPORTED("1cf5m3zu5ms9hd5uutq68nzfr"); //     job->pagesArrayMajor.x = job->pagesArrayMajor.y 
UNSUPPORTED("cuhzmvcf28hg8mxfw6jzahyz1"); // 		= job->pagesArrayMinor.x = job->pagesArrayMinor.y = 0;
UNSUPPORTED("910btb676tbzhf5zag3qt6p6y"); //     job->pagesArrayFirst.x = job->pagesArrayFirst.y = 0;
UNSUPPORTED("58i27py4gadbh4uynztkillbo"); //     job->pagesArrayMajor = pagecode(job, gvc->pagedir[0]);
UNSUPPORTED("4g3yxj64kn7kau45efo78icdp"); //     job->pagesArrayMinor = pagecode(job, gvc->pagedir[1]);
UNSUPPORTED("crjkqjf6ujmxl2713vrq1mowh"); //     if ((abs(job->pagesArrayMajor.x + job->pagesArrayMinor.x) != 1)
UNSUPPORTED("8xuyb2svr5wn3bsnke50x9egf"); //      || (abs(job->pagesArrayMajor.y + job->pagesArrayMinor.y) != 1)) {
UNSUPPORTED("rupytferkko4y32b1y4nvfda"); // 	job->pagesArrayMajor = pagecode(job, 'B');
UNSUPPORTED("3oh81o2dhshk0cwkh0se96nqd"); // 	job->pagesArrayMinor = pagecode(job, 'L');
UNSUPPORTED("6fm493t81wrciw7a1qwrwwv2v"); // 	agerr(AGWARN, "pagedir=%s ignored\n", gvc->pagedir);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("csilklooo31ve0sxcvlk6sz50"); //     /* determine page box including centering */
UNSUPPORTED("alu1x0je9svo1m9vsro4jt661"); //     if (GD_drawing(g)->centered) {
UNSUPPORTED("bd1di5q520ro9gdu3luri6uqw"); // 	if (pageSize.x > imageSize.x)
UNSUPPORTED("by6kyuqhzuke68yh4d1zsgjl8"); // 	    centering.x = (pageSize.x - imageSize.x) / 2;
UNSUPPORTED("245d5xe3ls4raq0ru2opv9bjk"); // 	if (pageSize.y > imageSize.y)
UNSUPPORTED("entb38itr4f1eoghh2si1k2yc"); // 	    centering.y = (pageSize.y - imageSize.y) / 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2jawgno03mhhf060l1l58y85o"); //     /* rotate back into graph orientation */
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("5bktmmmrdanogwscr9akvqvgp"); // 	imageSize = exch_xyf(imageSize);
UNSUPPORTED("cdvkc3n17wltx1ckd9ctxu40"); // 	pageSize = exch_xyf(pageSize);
UNSUPPORTED("aoommvrw3wcdqhpe1d9z2nrbr"); // 	margin = exch_xyf(margin);
UNSUPPORTED("am7xuokdmqu2xn6gmw3imq4ys"); // 	centering = exch_xyf(centering);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("452t79mqvs76inxtds258lmwc"); //     /* canvas area, centered if necessary */
UNSUPPORTED("egq8epmn2ww7uiikth9xfqyg2"); //     job->canvasBox.LL.x = margin.x + centering.x;
UNSUPPORTED("317995ucd58ato9cs7ssy4kw5"); //     job->canvasBox.LL.y = margin.y + centering.y;
UNSUPPORTED("9dlme2kqph7etv96uch6et43x"); //     job->canvasBox.UR.x = margin.x + centering.x + imageSize.x;
UNSUPPORTED("5eznso0zqq6thx7ifejpl7egg"); //     job->canvasBox.UR.y = margin.y + centering.y + imageSize.y;
UNSUPPORTED("ejrxa302gsbb1mtk617yev4ma"); //     /* size of one page in graph units */
UNSUPPORTED("2ktn14lo6mijabcu413qkefxe"); //     job->pageSize.x = imageSize.x / job->zoom;
UNSUPPORTED("mo56q7xk3qwgskgf05s41bku"); //     job->pageSize.y = imageSize.y / job->zoom;
UNSUPPORTED("b5mgup3twbezb6h3b77frrr32"); //     /* pageBoundingBox in device units and page orientation */
UNSUPPORTED("wgrjodb4gag4o6bbqdxsct4f"); //     job->pageBoundingBox.LL.x = ROUND(job->canvasBox.LL.x * job->dpi.x / 72);
UNSUPPORTED("4ibgme5rpasxyxnxfo9nx0uej"); //     job->pageBoundingBox.LL.y = ROUND(job->canvasBox.LL.y * job->dpi.y / 72);
UNSUPPORTED("23f72iko8ym45z6o2v06lmrf8"); //     job->pageBoundingBox.UR.x = ROUND(job->canvasBox.UR.x * job->dpi.x / 72);
UNSUPPORTED("cazmki1n27pnpo1rf83qc35cv"); //     job->pageBoundingBox.UR.y = ROUND(job->canvasBox.UR.y * job->dpi.y / 72);
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("effd896sgn6sehp6jdoqfo5bu"); // 	job->pageBoundingBox.LL = exch_xy(job->pageBoundingBox.LL);
UNSUPPORTED("e6umepvfyiglcz7y9070vqcw1"); // 	job->pageBoundingBox.UR = exch_xy(job->pageBoundingBox.UR);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 18839plafofp0mij32iifl4kw
// static void firstpage(GVJ_t *job) 
public static Object firstpage(Object... arg) {
UNSUPPORTED("54ikst765kaurqs5qbxgw62pi"); // static void firstpage(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("d5tvlsaovhs5xq1fsq2ff13ea"); //     job->pagesArrayElem = job->pagesArrayFirst;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1oosdfui4w6wrmjyrea5q0q5j
// static boolean validpage(GVJ_t *job) 
public static Object validpage(Object... arg) {
UNSUPPORTED("5hdqj8s4dsinb9hoj6ra36f9w"); // static boolean validpage(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6s658komj0oc3gl02m6w1a5cl"); //     return ((job->pagesArrayElem.x >= 0)
UNSUPPORTED("i8aw94fegecco4s00otz3bb0"); // 	 && (job->pagesArrayElem.x < job->pagesArraySize.x)
UNSUPPORTED("61clso3i8seexlw62qjygdpkc"); // 	 && (job->pagesArrayElem.y >= 0)
UNSUPPORTED("aude4er9wxhdfvjg0cm8qe53l"); // 	 && (job->pagesArrayElem.y < job->pagesArraySize.y));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9xha5eg5k3mbziq2ptbw1gyyk
// static void nextpage(GVJ_t *job) 
public static Object nextpage(Object... arg) {
UNSUPPORTED("3zzzr5yvj7mho7ylmluju8zzv"); // static void nextpage(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("90v54lwwxx1bsav5znrj48tkj"); //     job->pagesArrayElem = add_point(job->pagesArrayElem, job->pagesArrayMinor);
UNSUPPORTED("9lqhnwlrk62cxcln9uq0024y0"); //     if (validpage(job) == 0) {
UNSUPPORTED("2mcnl5q2zavzvatv4bugp5g6v"); // 	if (job->pagesArrayMajor.y)
UNSUPPORTED("53fqbcbkex674c100tfx744f6"); // 	    job->pagesArrayElem.x = job->pagesArrayFirst.x;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("503luvfr4rh4kt3ktfo48tii4"); // 	    job->pagesArrayElem.y = job->pagesArrayFirst.y;
UNSUPPORTED("a179fxb17hjyc52yjgeshme15"); // 	job->pagesArrayElem = add_point(job->pagesArrayElem, job->pagesArrayMajor);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a7du41byqeco4ri9ytgrmrv91
// static boolean write_edge_test(Agraph_t * g, Agedge_t * e) 
public static Object write_edge_test(Object... arg) {
UNSUPPORTED("9rfens9tggchdklhfwa4kaqlu"); // static boolean write_edge_test(Agraph_t * g, Agedge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5qsdev7ikmpmm9zepv8ye0zy6"); //     Agraph_t *sg;
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("99d9j6m0161wdv2tu4wbf3ifi"); //     for (c = 1; c <= GD_n_cluster(g); c++) {
UNSUPPORTED("cuf43q4kl3kqgyuuxdqve1mqt"); // 	sg = GD_clust(g)[c];
UNSUPPORTED("72iyzot8hjs1ni2lzvvc2ibn2"); // 	if (agcontains(sg, e))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 blr202hbxs2lw3k393qohaw2i
// static boolean write_node_test(Agraph_t * g, Agnode_t * n) 
public static Object write_node_test(Object... arg) {
UNSUPPORTED("9xuxw9sbimnioecqbuwnav0qp"); // static boolean write_node_test(Agraph_t * g, Agnode_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5qsdev7ikmpmm9zepv8ye0zy6"); //     Agraph_t *sg;
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("99d9j6m0161wdv2tu4wbf3ifi"); //     for (c = 1; c <= GD_n_cluster(g); c++) {
UNSUPPORTED("cuf43q4kl3kqgyuuxdqve1mqt"); // 	sg = GD_clust(g)[c];
UNSUPPORTED("4mgclp1plbdi4oudwrp9t4oh2"); // 	if (agcontains(sg, n))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3w1wzp6uh4yl5o7brxcuf9ip6
// static pointf* copyPts (pointf* pts, int* ptsize, xdot_point* inpts, int numpts) 
public static Object copyPts(Object... arg) {
UNSUPPORTED("1wmmoe7m5528rl69n3zrnxhrg"); // static pointf*
UNSUPPORTED("7bm15tetrusr4tjv45g1l12mm"); // copyPts (pointf* pts, int* ptsize, xdot_point* inpts, int numpts)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9a0g5x0xovxojrvkasvwowcw5"); //     int i, sz = *ptsize;
UNSUPPORTED("a9llhmmnr8bimltlwtihii1cr"); //     if (numpts > sz) {
UNSUPPORTED("3z4f2367ql9ea8y2br7xxotsn"); // 	sz = MAX(2*sz, numpts);
UNSUPPORTED("20m3u8zv7b0bef39rhk9o4ft5"); // 	pts = RALLOC(sz, pts, pointf);
UNSUPPORTED("klakg17zs75ckw23eh65hsxj"); // 	*ptsize = sz;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5alxr1b92wrqvkbrhhkibw4n0"); //     for (i = 0; i < numpts; i++) {
UNSUPPORTED("23z0p3o7uw6iisz8rz93fmju8"); // 	pts[i].x = inpts[i].x;
UNSUPPORTED("1otoj1bflw7ets1aqhi7x3gv7"); // 	pts[i].y = inpts[i].y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b7gk8q1reftzri269holggnig"); //     return pts;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2ho96kyoz7afiqvpkgzje2q17
// static void emit_xdot (GVJ_t * job, xdot* xd) 
public static Object emit_xdot(Object... arg) {
UNSUPPORTED("dx3xx20717wqwhmrxmr2vve4w"); // static void emit_xdot (GVJ_t * job, xdot* xd)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2p6ji7mn6c9yb2wjrxpfdi2a7"); //     int image_warn = 1;
UNSUPPORTED("4fdllwmvxpz1l1wbhs4x0nd8h"); //     int ptsize = 1000;
UNSUPPORTED("14sqwaw2nua8zxlzi03vrp99n"); //     pointf* pts = (pointf*)gmalloc((1000)*sizeof(pointf));
UNSUPPORTED("6h54fnrtmztsxuhsa6imjz1a7"); //     exdot_op* op;
UNSUPPORTED("fliif79u3s8wl3il1sr4xy7m"); //     int i, angle;
UNSUPPORTED("a0vsnu7j685g9wawdylnp9ack"); //     char** styles = 0;
UNSUPPORTED("610stn97kmwddhnpeizvxmri2"); //     int filled = 1;
UNSUPPORTED("9go5curik6dnbejv5o2pj8ve2"); //     op = (exdot_op*)(xd->ops);
UNSUPPORTED("dvwaxn9xbj2jw2hjwvvs0tj3z"); //     for (i = 0; i < xd->cnt; i++) {
UNSUPPORTED("ab1ajnetstzss8ksa66rwdtze"); // 	switch (op->op.kind) {
UNSUPPORTED("1texeok1es39lsgo5wdppbco2"); // 	case xd_filled_ellipse :
UNSUPPORTED("effcskuftros0sla2ltem13mh"); // 	case xd_unfilled_ellipse :
UNSUPPORTED("9oxng5yti9jzjlhlee8ivkduh"); //     	    if (boxf_overlap(op->bb, job->clip)) {
UNSUPPORTED("eh7zpmojvvc2ava7e01p17wy3"); // 		pts[0].x = op->op.u.ellipse.x - op->op.u.ellipse.w;
UNSUPPORTED("6buxv3e1hyjli0lwoxxp24v0a"); // 		pts[0].y = op->op.u.ellipse.y - op->op.u.ellipse.h;
UNSUPPORTED("7zuf0gmhnqw8pehovb01ajiff"); // 		pts[1].x = op->op.u.ellipse.w;
UNSUPPORTED("8jfm3tgnfah6oxs36u6auntxy"); // 		pts[1].y = op->op.u.ellipse.h;
UNSUPPORTED("9qjmukp231ben11immdj4r1tz"); // 		gvrender_ellipse(job, pts, 2, (op->op.kind == xd_filled_ellipse?filled:0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("40fjmoej0qk9hwneja77jlo2y"); // 	case xd_filled_polygon :
UNSUPPORTED("6uw61r6bh4tiicbj46yxr9t4k"); // 	case xd_unfilled_polygon :
UNSUPPORTED("9oxng5yti9jzjlhlee8ivkduh"); //     	    if (boxf_overlap(op->bb, job->clip)) {
UNSUPPORTED("e8xah2ix2nhclr9bifa3tcc8x"); // 		pts = copyPts (pts, &ptsize, op->op.u.polygon.pts, op->op.u.polygon.cnt);
UNSUPPORTED("40c49lgnwfti09qhawe9qm3f7"); // 		gvrender_polygon(job, pts, op->op.u.polygon.cnt, (op->op.kind == xd_filled_polygon?filled:0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4eu4a2drbgdgq6xb3lnl3hn9y"); // 	case xd_filled_bezier :
UNSUPPORTED("bn514k7swi4s6uclqqke583n8"); // 	case xd_unfilled_bezier :
UNSUPPORTED("9oxng5yti9jzjlhlee8ivkduh"); //     	    if (boxf_overlap(op->bb, job->clip)) {
UNSUPPORTED("2g406s0cpa1h7r9vudzsxwz3b"); // 		pts = copyPts (pts, &ptsize, op->op.u.bezier.pts, op->op.u.bezier.cnt);
UNSUPPORTED("3kzwg9cpwabfnvuyfvvt0e4c9"); // 		gvrender_beziercurve(job, pts, op->op.u.bezier.cnt, 0, 0, (op->op.kind == xd_filled_bezier?filled:0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1l7ha9akoh6yew2v54hvu3del"); // 	case xd_polyline :
UNSUPPORTED("9oxng5yti9jzjlhlee8ivkduh"); //     	    if (boxf_overlap(op->bb, job->clip)) {
UNSUPPORTED("75vwkjetu3vs2efqgkquomhey"); // 		pts = copyPts (pts, &ptsize, op->op.u.polyline.pts, op->op.u.polyline.cnt);
UNSUPPORTED("2kirl6fra7pbqwmtc9b9rav42"); // 		gvrender_polyline(job, pts, op->op.u.polyline.cnt);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("60mg8gze602593bf2vddicza0"); // 	case xd_text :
UNSUPPORTED("9oxng5yti9jzjlhlee8ivkduh"); //     	    if (boxf_overlap(op->bb, job->clip)) {
UNSUPPORTED("djw3lr9bvuzdg88p1b61ibm23"); // 		pts[0].x = op->op.u.text.x;
UNSUPPORTED("405lw9ptftz28hgpp0qcf6ggg"); // 		pts[0].y = op->op.u.text.y;
UNSUPPORTED("8uwiw0r7tra0g01788ktwkt8t"); // 		gvrender_textspan(job, pts[0], op->span);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("298qj2a6o0kxeatu0jgvoi784"); // 	case xd_fill_color :
UNSUPPORTED("3zv13wowl159snnsunkxkjtug"); //             gvrender_set_fillcolor(job, op->op.u.color);
UNSUPPORTED("7ek7aftv8z293izx886r01oqm"); // 	    filled = 1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("qwcu5b23c4g1dkhz5kg9foza"); // 	case xd_pen_color :
UNSUPPORTED("1zlhprkxd9efydpc2r24kd7fa"); //             gvrender_set_pencolor(job, op->op.u.color);
UNSUPPORTED("7ek7aftv8z293izx886r01oqm"); // 	    filled = 1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8yhndo0ghuwhbavc33g7v21ni"); // 	case xd_grad_fill_color : 
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("briow60sq6ay4k0slv0k1e0ai"); // 		char* clr0;
UNSUPPORTED("1d5jtx9oz042hr4a21vm9wd8s"); // 		char* clr1;
UNSUPPORTED("9tmq69gqblhx0uj3ylye71gdm"); // 		float frac;
UNSUPPORTED("3qtmddjw3ecnb2jfielq996dh"); // 		if (op->op.u.grad_color.type == xd_radial) {
UNSUPPORTED("7wjddh54icciyrvc03tmou0k6"); // 		    xdot_radial_grad* p = &op->op.u.grad_color.u.ring;
UNSUPPORTED("82vevzieh8c8a5vw0c3537ysr"); // 		    clr0 = p->stops[0].color;
UNSUPPORTED("25x3dhc524e69l7m63fm0botd"); // 		    clr1 = p->stops[1].color;
UNSUPPORTED("2yubqrr4rmb6wyok8zz3d4l4b"); // 		    frac = p->stops[1].frac;
UNSUPPORTED("c7ot1hnnumwoi4dv6bz51rr86"); // 		    if ((p->x1 == p->x0) && (p->y1 == p->y0))
UNSUPPORTED("2wcz57lhb7pd9xqu6a16n149a"); // 			angle = 0;
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("o9ps6kmfn10hdy083p2fd3as"); // 			angle = (int)(180.0*acos((p->x0 - p->x1)/p->r0)/M_PI);
UNSUPPORTED("34ytjoiyf9fnxnng5a51xtnj9"); //         	    gvrender_set_fillcolor(job, clr0);
UNSUPPORTED("7ynfnu592qnwkfm90qrka6a4n"); // 		    gvrender_set_gradient_vals(job, clr1, angle, frac);
UNSUPPORTED("5jf506rwz9snq4d6ozpjvg3yg"); // 		    filled = 3;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("828t3nqv5ns5qvt2e0glyg2ju"); // 		    xdot_linear_grad* p = &op->op.u.grad_color.u.ling;
UNSUPPORTED("82vevzieh8c8a5vw0c3537ysr"); // 		    clr0 = p->stops[0].color;
UNSUPPORTED("25x3dhc524e69l7m63fm0botd"); // 		    clr1 = p->stops[1].color;
UNSUPPORTED("2yubqrr4rmb6wyok8zz3d4l4b"); // 		    frac = p->stops[1].frac;
UNSUPPORTED("3w9y8g31d63rmvl7ccv0624ye"); // 		    angle = (int)(180.0*atan2(p->y1-p->y0,p->x1-p->x0)/M_PI);
UNSUPPORTED("34ytjoiyf9fnxnng5a51xtnj9"); //         	    gvrender_set_fillcolor(job, clr0);
UNSUPPORTED("7ynfnu592qnwkfm90qrka6a4n"); // 		    gvrender_set_gradient_vals(job, clr1, angle, frac);
UNSUPPORTED("7bikp52v1ey2yil3rybx6nris"); // 		    filled = 2;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("6qu5ytxcjfat0eg64bep0kksy"); // 	case xd_grad_pen_color :
UNSUPPORTED("as7jx69a7p4gq2bo9ij43797s"); // 	    agerr (AGWARN, "gradient pen colors not yet supported.\n");
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("56n5nfs2z21zo7ac9z2ehf12l"); // 	case xd_font :
UNSUPPORTED("94uhdsl49d2gyeyqzafatdww3"); // 	    /* fontsize and fontname already encoded via xdotBB */
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("60cspx7wpewmlz0ykd29ul27c"); // 	case xd_style :
UNSUPPORTED("2du13zy7ki13r60g7zvb6kuz1"); // 	    styles = parse_style (op->op.u.style);
UNSUPPORTED("a5m3xgb63z0xz7nf5eez363f2"); //             gvrender_set_style (job, styles);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2kiqf0asbojt8x24lr5eykca1"); // 	case xd_fontchar :
UNSUPPORTED("37zacnm7f82nfay9tbqkaay4d"); // 	    /* font characteristics already encoded via xdotBB */
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("91w60vqrbkqvk3q82pdoxz0y"); // 	case xd_image :
UNSUPPORTED("6f7cw2kz0kg3lxhd6cgr661hm"); // 	    if (image_warn) {
UNSUPPORTED("euk5nzqu0sjv93t5ys3lhhx1q"); // 	        agerr(AGWARN, "Images unsupported in \"background\" attribute\n");
UNSUPPORTED("6omlto7p0ejzb4vi9rt3vyuck"); // 	        image_warn = 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("42f106i435avz0oneb0fhe81f"); // 	op++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7jkrbr4tf42xe3tv9gb8ao7iu"); //     if (styles)
UNSUPPORTED("aw7zhlkbn1bg9h9hrlr9by8ty"); // 	gvrender_set_style(job, job->gvc->defaultlinestyle);
UNSUPPORTED("1yomvtf3njcprecdmp7eq33o7"); //     free (pts);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3adhe426gweys0vj89xk7l5qs
// static void emit_background(GVJ_t * job, graph_t *g) 
public static Object emit_background(Object... arg) {
UNSUPPORTED("4gpum3t892n8i9y337zhppgqh"); // static void emit_background(GVJ_t * job, graph_t *g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5f36lqh4hk3q6qlni9dobv8ye"); //     xdot* xd;
UNSUPPORTED("76nok3eiyr33qf4ecv69ujxn6"); //     char *str;
UNSUPPORTED("4qfeabztp0a8uifsz5h2qj0xm"); //     int dfltColor;
UNSUPPORTED("5c5y7z63yavk2nbvb55um8ob0"); //     /* if no bgcolor specified - first assume default of "white" */
UNSUPPORTED("eupo0q6569myn401db6vre9x9"); //     if (! ((str = agget(g, "bgcolor")) && str[0])) {
UNSUPPORTED("aje4jzb3skm6bryubgu0u8aoo"); // 	str = "white";
UNSUPPORTED("3myfxeslr7cfs3cawa74sviw2"); // 	dfltColor = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c3z9eq4yq4ibi29ne1aux9u1a"); // 	dfltColor = 0;
UNSUPPORTED("72j9cwumrsza4qx1rwsxe0rxg"); //     /* if device has no truecolor support, change "transparent" to "white" */
UNSUPPORTED("ati9yt7rkd3v77g6nvlpbc3wa"); //     if (! (job->flags & (1<<8)) && ((*(str)==*("transparent")&&!strcmp(str,"transparent")))) {
UNSUPPORTED("aje4jzb3skm6bryubgu0u8aoo"); // 	str = "white";
UNSUPPORTED("3myfxeslr7cfs3cawa74sviw2"); // 	dfltColor = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bvjta4pe1bd0an3zb3gm4m9ug"); //     /* except for "transparent" on truecolor, or default "white" on (assumed) white paper, paint background */
UNSUPPORTED("aj6etszuv7t3orniarovw9oui"); //     if (!(   ((job->flags & (1<<8)) && (*(str)==*("transparent")&&!strcmp(str,"transparent")))
UNSUPPORTED("ysgvhk2xw5my7hpp0pyhsyhv"); //           || ((job->flags & (1<<25)) && dfltColor))) {
UNSUPPORTED("qsl046aw7731cpj2cdrs5l2u"); // 	char* clrs[2];
UNSUPPORTED("4xv0cmpfa4sol0pqmfumr0rnm"); // 	float frac;
UNSUPPORTED("3oqdtywao9635lj8pbh00gww9"); // 	if ((findStopColor (str, clrs, &frac))) {
UNSUPPORTED("3vfwc36a7qaby9xcub7abdnrw"); // 	    int filled, istyle = 0;
UNSUPPORTED("5m1l4f0yk2x1r9n00p7xoarhk"); //             gvrender_set_fillcolor(job, clrs[0]);
UNSUPPORTED("ca8n6otn7zx4kgpo3p43opznn"); //             gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("4xxqxkr63rp33ynfp8093ce5j"); // 	    checkClusterStyle(g, &istyle);
UNSUPPORTED("850qgpdnne96gxnh244hf2rh2"); // 	    if (clrs[1]) 
UNSUPPORTED("2i2pjytalbtpm2nu5e4l391tt"); // 		gvrender_set_gradient_vals(job,clrs[1],late_int(g,G_gradientangle,0,0), frac);
UNSUPPORTED("f3qa0cv737ikcre1vpqlkukio"); // 	    else 
UNSUPPORTED("cqldfd2kxstrxcsu8vov72204"); // 		gvrender_set_gradient_vals(job,"black",late_int(g,G_gradientangle,0,0), frac);
UNSUPPORTED("4z7nxcqujj30hxs47niv7ng5z"); // 	    if (istyle & (1 << 1))
UNSUPPORTED("s4xfcz4il9k9jw0w0dh9lzpj"); // 		filled = 3;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("1ijl60mqfpjns1tss115yw4zp"); // 		filled = 2;
UNSUPPORTED("shtvpixd4zg9junin51ss0n6"); // 	    gvrender_box(job, job->clip, filled);
UNSUPPORTED("tddizbp6oh1hmfmmbyetc39q"); // 	    free (clrs[0]);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("75elz449frogpglpfecpb7n35"); //             gvrender_set_fillcolor(job, str);
UNSUPPORTED("aa64pbbtgdbmq07oeeb6b6xd6"); //     	    gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("em1ymq2htxwlplauvapkn96jg"); // 	    gvrender_box(job, job->clip, 1);	/* filled */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b3w7cbaupdjkjv8mlqprw6bgy"); //     if ((xd = (xdot*)GD_drawing(g)->xdots))
UNSUPPORTED("91v0ozby5hp08jwxac38nv9eq"); // 	emit_xdot (job, xd);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 500xb8otehz86mqouvbmd0ri4
// static void setup_page(GVJ_t * job, graph_t * g) 
public static Object setup_page(Object... arg) {
UNSUPPORTED("eie6fiea6odhb16j5p69i17ps"); // static void setup_page(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e8udyft1fkl49dzp8kuvsscja"); //     point pagesArrayElem = job->pagesArrayElem, pagesArraySize = job->pagesArraySize;
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("4m3552gi4z6716erhvg0mybdi"); // 	pagesArrayElem = exch_xy(pagesArrayElem);
UNSUPPORTED("5n79xja43o2fzgv2jpwlowx9e"); // 	pagesArraySize = exch_xy(pagesArraySize);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3ezjo786mtpn6j3zv1sixv5yx"); //     /* establish current box in graph units */
UNSUPPORTED("2wdi0jzr6koh59dq2o5wm7rdk"); //     job->pageBox.LL.x = pagesArrayElem.x * job->pageSize.x - job->pad.x;
UNSUPPORTED("8tjzkjxgopydb6bnmjxzwrdza"); //     job->pageBox.LL.y = pagesArrayElem.y * job->pageSize.y - job->pad.y;
UNSUPPORTED("su2b0thzhhuzkggkmn3o5olo"); //     job->pageBox.UR.x = job->pageBox.LL.x + job->pageSize.x;
UNSUPPORTED("3lljoqtt3qxcnasqtqf3ibj8v"); //     job->pageBox.UR.y = job->pageBox.LL.y + job->pageSize.y;
UNSUPPORTED("6i1o1mvszl64iabuc4wxhwwsz"); //     /* maximum boundingBox in device units and page orientation */
UNSUPPORTED("51aihik1hbq97o8c6qesdchi2"); //     if (job->common->viewNum == 0)
UNSUPPORTED("1hl3w4ojiqaffaut1cbitfui3"); //         job->boundingBox = job->pageBoundingBox;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("67qpim1lm8zwmdptcomxyx9zh"); //         EXPANDBB(job->boundingBox, job->pageBoundingBox);
UNSUPPORTED("dutcpy0okvnjgc0dspbfidfls"); //     if (job->flags & (1<<7)) {
UNSUPPORTED("2xekg55ov733mt60q1dwo2uau"); //         job->clip.LL.x = job->focus.x - job->view.x / 2.;
UNSUPPORTED("elawyto1cl29eu55o90i97mwe"); //         job->clip.LL.y = job->focus.y - job->view.y / 2.;
UNSUPPORTED("cvsh3xbateu7iuus29faim1ib"); //         job->clip.UR.x = job->focus.x + job->view.x / 2.;
UNSUPPORTED("2dms1ogbgw3r7bq8cz5sikfnm"); //         job->clip.UR.y = job->focus.y + job->view.y / 2.;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("axdbapte9nha7r33skc7m9qp8"); //         job->clip.LL.x = job->focus.x + job->pageSize.x * (pagesArrayElem.x - pagesArraySize.x / 2.);
UNSUPPORTED("9y9ky161dftrmlq4zybikd3wo"); //         job->clip.LL.y = job->focus.y + job->pageSize.y * (pagesArrayElem.y - pagesArraySize.y / 2.);
UNSUPPORTED("28rqxotpw4u5xyb8k5ntx5sv3"); //         job->clip.UR.x = job->clip.LL.x + job->pageSize.x;
UNSUPPORTED("1yvnjmifai53mslfxbm113t6z"); //         job->clip.UR.y = job->clip.LL.y + job->pageSize.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6dd6oqrwhbhk7mqj8blieol8a"); //     /* CAUTION - job->translation was difficult to get right. */
UNSUPPORTED("ati7mqxwmn8u4o5lrx82875b7"); //     /* Test with and without assymetric margins, e.g: -Gmargin="1,0" */
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("64qd8daar2r794nip6jt8u6r1"); // 	job->translation.y = - job->clip.UR.y - job->canvasBox.LL.y / job->zoom;
UNSUPPORTED("4356s4xto6awx7cij60pg6lvm"); //         if ((job->flags & (1<<12)) || (Y_invert))
UNSUPPORTED("9rso607ag8rokfb0keaj3dw8i"); //             job->translation.x = - job->clip.UR.x - job->canvasBox.LL.x / job->zoom;
UNSUPPORTED("35nw1pbiz2p3s6qwlam5eoo3m"); //         else
UNSUPPORTED("6nzlh7e28djtn3tuh7wyazl0f"); //             job->translation.x = - job->clip.LL.x + job->canvasBox.LL.x / job->zoom;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("9ndd3yrf6pi0yzyq6qhvwvkmb"); // 	/* pre unscale margins to keep them constant under scaling */
UNSUPPORTED("71450l3ur3gaikd8hf09vaisy"); //         job->translation.x = - job->clip.LL.x + job->canvasBox.LL.x / job->zoom;
UNSUPPORTED("4356s4xto6awx7cij60pg6lvm"); //         if ((job->flags & (1<<12)) || (Y_invert))
UNSUPPORTED("e3i6pyd8cgpyxqdji6ikxfolc"); //             job->translation.y = - job->clip.UR.y - job->canvasBox.LL.y / job->zoom;
UNSUPPORTED("35nw1pbiz2p3s6qwlam5eoo3m"); //         else
UNSUPPORTED("8bfteitivddvt9tazhk9smelv"); //             job->translation.y = - job->clip.LL.y + job->canvasBox.LL.y / job->zoom;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 as589o2efx45izm3wp7enyaht
// static boolean node_in_layer(GVJ_t *job, graph_t * g, node_t * n) 
public static Object node_in_layer(Object... arg) {
UNSUPPORTED("9qfpqdz7l9i14k8tt5niso9hr"); // static boolean node_in_layer(GVJ_t *job, graph_t * g, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4np0l1afjcabfu4lj2slo7u1h"); //     char *pn, *pe;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("br25a8xf14d9pszeecj99a422"); //     if (job->numLayers <= 1)
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("6b110nazp4kekcjo0cggixji8"); //     pn = late_string(n, N_layer, "");
UNSUPPORTED("3jdetxpc61my2m5ouwwlafmya"); //     if (selectedlayer(job, pn))
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("1472k1aiajh8fdwws76p0peui"); //     if (pn[0])
UNSUPPORTED("974jvy6bjjit5aunmg1ofk323"); // 	return 0;		/* Only check edges if pn = "" */
UNSUPPORTED("6iiceie8alt1qdir25ryu1f6k"); //     if ((e = agfstedge(g, n)) == NULL)
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("dcuhjbsiioawp6zspvonwd4px"); //     for (e = agfstedge(g, n); e; e = agnxtedge(g, e, n)) {
UNSUPPORTED("82x79oibcq3pjyv631qc1tlg0"); // 	pe = late_string(e, E_layer, "");
UNSUPPORTED("820bc3hf82s7cw6chnw5c4nbq"); // 	if ((pe[0] == '\0') || selectedlayer(job, pe))
UNSUPPORTED("9qhn9m3123s8n6wwxjfo8awlm"); // 	    return NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1u0s2ppd3ul08n4e2gdaryyy8
// static boolean edge_in_layer(GVJ_t *job, graph_t * g, edge_t * e) 
public static Object edge_in_layer(Object... arg) {
UNSUPPORTED("313onxwqkhkgi36hzjkauyb42"); // static boolean edge_in_layer(GVJ_t *job, graph_t * g, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("utbq83gb14i5eml17vr8pcid"); //     char *pe, *pn;
UNSUPPORTED("6iv4xusq3ncisurzdtlt8lhe9"); //     int cnt;
UNSUPPORTED("br25a8xf14d9pszeecj99a422"); //     if (job->numLayers <= 1)
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("6x2v74zlvitlrgkn17942vcgh"); //     pe = late_string(e, E_layer, "");
UNSUPPORTED("3ywt8di993vozrwwnup6dpwyy"); //     if (selectedlayer(job, pe))
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("1qtr20a3naoc1fyck8og7mmrh"); //     if (pe[0])
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("6b357rcrnr0ds1kjupqbpaa3e"); //     for (cnt = 0; cnt < 2; cnt++) {
UNSUPPORTED("b8bas27843d09g6jaaawthl8w"); // 	pn = late_string(cnt < 1 ? agtail(e) : aghead(e), N_layer, "");
UNSUPPORTED("jsx6qbuduuwph63wgsoiqgvq"); // 	if ((pn[0] == '\0') || selectedlayer(job, pn))
UNSUPPORTED("9qhn9m3123s8n6wwxjfo8awlm"); // 	    return NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dyo8zdve7djszp87cuf0xuoet
// static boolean clust_in_layer(GVJ_t *job, graph_t * sg) 
public static Object clust_in_layer(Object... arg) {
UNSUPPORTED("4e36i94sm4m9tfa4ajm22cim8"); // static boolean clust_in_layer(GVJ_t *job, graph_t * sg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b9rg1zyq2dr4mvb3afpkekhnt"); //     char *pg;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("br25a8xf14d9pszeecj99a422"); //     if (job->numLayers <= 1)
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("20qd9xj97lsoon2qf56l64qxd"); //     pg = late_string(sg, agattr(sg, AGRAPH, "layer", 0), "");
UNSUPPORTED("658zavk95iaxd3u1jryh7rbqc"); //     if (selectedlayer(job, pg))
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("82stl7dpo6yrg2w5a6umx8q9c"); //     if (pg[0])
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("bjjx5k3xelkwyvbcb4gwnqq7s"); //     for (n = agfstnode(sg); n; n = agnxtnode(sg, n))
UNSUPPORTED("dl5e8i6kxe0n05x40z42vjo3g"); // 	if (node_in_layer(job, sg, n))
UNSUPPORTED("9qhn9m3123s8n6wwxjfo8awlm"); // 	    return NOT(0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 75k6tn88w31u4ppw3q70g1jsk
// static boolean node_in_box(node_t *n, boxf b) 
public static Object node_in_box(Object... arg) {
UNSUPPORTED("211j77g7ql3frkbpciwlnn9zy"); // static boolean node_in_box(node_t *n, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("46tealt6sxovbrn80ssq4sa8g"); //     return boxf_overlap(ND_bb(n), b);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7j4u56kqr6m677iq36g2aih28
// static void emit_begin_node(GVJ_t * job, node_t * n) 
public static Object emit_begin_node(Object... arg) {
UNSUPPORTED("6oh0hqc0y111peee2y6h3dyp0"); // static void emit_begin_node(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8lamppsqs7nhiu5v8k1f5jfh2"); //     obj_state_t *obj;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("ey6s6opybnefos5fozgk763tu"); //     int sides, peripheries, i, j, filled = 0, rect = 0, shape, nump = 0;
UNSUPPORTED("cc2tjyw4ovc2cfzpfqu5s7xnr"); //     polygon_t *poly = NULL;
UNSUPPORTED("1rh8tfsy1jwdlk7jeq8xlypar"); //     pointf *vertices, *p = NULL;
UNSUPPORTED("avrrxyqlc98q6r2lvs1ku6ptd"); //     pointf coord;
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("1iexddadjo0w6fdgddatfx40s"); //     obj = push_obj_state(job);
UNSUPPORTED("7o8x3n4tonxrhr3uxintmcd0j"); //     obj->type = NODE_OBJTYPE;
UNSUPPORTED("otsh0jqjlx2ylharf6sknjc7"); //     obj->u.n = n;
UNSUPPORTED("heflz2y0siuc5a4qsd7xdy0i"); //     obj->emit_state = EMIT_NDRAW;
UNSUPPORTED("984vi925q5gu9ti0dn4c6h1pu"); //     if (flags & (1<<24)) {
UNSUPPORTED("8hn4rzdhnzbbm4vcj7b1o2h8s"); //         /* obj->z = late_double(n, N_z, 0.0, -MAXFLOAT); */
UNSUPPORTED("7xl25s8bmvdzfgqceezs4vbv7"); // 	if (GD_odim(agraphof(n)) >=3)
UNSUPPORTED("e9bowj0fth92lpwok5jw7ygmv"); //             obj->z = (ROUND((ND_pos(n)[2])*72));
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("7ss0158fo4kqsce5naq3vb9di"); //             obj->z = 0.0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("de51o3rsacfia37u3ekmuuyep"); //     initObjMapData (job, ND_label(n), n);
UNSUPPORTED("8k7npuk2pg2np42cu63aachgc"); //     if ((flags & ((1<<16) | (1<<22)))
UNSUPPORTED("ap5s8ebp9t1efc4rdd66xi8h2"); //            && (obj->url || obj->explicit_tooltip)) {
UNSUPPORTED("752pv46veugd2etmozcnfiypa"); //         /* checking shape of node */
UNSUPPORTED("5zbsi1lwuz5ursdo00msuv88d"); //         shape = shapeOf(n);
UNSUPPORTED("7s077dx2lcy2m2jqo0bd1qwbj"); //         /* node coordinate */
UNSUPPORTED("2qpk1rnkjskc4vssp5kqy1hob"); //         coord = ND_coord(n);
UNSUPPORTED("4htxfyxz1smr3utoocizilxu4"); //         /* checking if filled style has been set for node */
UNSUPPORTED("5kcgc99yv9rp8w1dcte4rnf51"); //         filled = ifFilled(n);
UNSUPPORTED("600lgxny2fkn72axw3r34cgqb"); //         if (shape == SH_POLY || shape == SH_POINT) {
UNSUPPORTED("d5nksf3ih1yt0kg59sfkewlsj"); //             poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("456i8yij35bvszszfonbqz35a"); //             /* checking if polygon is regular rectangle */
UNSUPPORTED("4vyy0i6dsllcradhvl2oe51ir"); //             if (isRect(poly) && (poly->peripheries || filled))
UNSUPPORTED("3yn5fb3yxthk0g149yritkp9"); //                 rect = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5t5nn5iltsyof8cyz3b2gwvdf"); //         /* When node has polygon shape and requested output supports polygons
UNSUPPORTED("rtnlliq5iwd307fmm56s27kf"); //          * we use a polygon to map the clickable region that is a:
UNSUPPORTED("awa42ejtbglkhruoiebsfh83c"); //          * circle, ellipse, polygon with n side, or point.
UNSUPPORTED("4w3ojigi9jyced2btkx1r494k"); //          * For regular rectangular shape we have use node's bounding box to map clickable region
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("1qgrvitr94qu2hi2by8s1ylm1"); //         if (poly && !rect && (flags & (1<<19))) {
UNSUPPORTED("91fvk4kb7gg5t610zxi308wws"); //             if (poly->sides < 3)
UNSUPPORTED("f4hqnp8zargnp3vhkbcl094mr"); //                 sides = 1;
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("csupw9r57m2dwx6i5f2tu4d86"); //                 sides = poly->sides;
UNSUPPORTED("5q6n2srifirg3tkf0bas934m1"); //             if (poly->peripheries < 2)
UNSUPPORTED("6panx1vemgvl6wfskrd3m1mww"); //                 peripheries = 1;
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("81q50lt0mna1wn04hztqusptn"); //                 peripheries = poly->peripheries;
UNSUPPORTED("2uakxkguw0fhjjx37xg11qtpp"); //             vertices = poly->vertices;
UNSUPPORTED("dp132vof8gu0b6vv6svot7dp4"); //             if ((s = agget(n, "samplepoints")))
UNSUPPORTED("3ditmtyp8gocdq6i8g23oqfk3"); //                 nump = atoi(s);
UNSUPPORTED("9rcwqkr45gajonyf3mylgs54d"); //             /* We want at least 4 points. For server-side maps, at most 100
UNSUPPORTED("80gxq7mljih09qbykr2a77hji"); //              * points are allowed. To simplify things to fit with the 120 points
UNSUPPORTED("95v6tfii1cmgyczmw9snifrz2"); //              * used for skewed ellipses, we set the bound at 60.
UNSUPPORTED("29edlge3wqyp1gs0vymtr5700"); //              */
UNSUPPORTED("24rjq7ktpbelyle5ccux41fbw"); //             if ((nump < 4) || (nump > 60))
UNSUPPORTED("8f615e20vka2xve8ihx3czfgz"); //                 nump = 20;
UNSUPPORTED("7o7i7thssuva8pvhyrohuym9s"); //             /* use bounding box of text label or node image for mapping
UNSUPPORTED("2bw9y6a24gfxnxsqoc4pzu34f"); //              * when polygon has no peripheries and node is not filled
UNSUPPORTED("29edlge3wqyp1gs0vymtr5700"); //              */
UNSUPPORTED("a7oq5da9ov5iuj0nr5evgcaey"); //             if (poly->peripheries == 0 && !filled) {
UNSUPPORTED("5hkjrhdwgew8q4bjy8hi39y0l"); //                 obj->url_map_shape = MAP_RECTANGLE;
UNSUPPORTED("6z27gxa0h15ax0y7jhl4jqw7u"); //                 nump = 2;
UNSUPPORTED("6y6ve2v6irttd8deh2u0tidty"); //                 p = (pointf*)zmalloc((nump)*sizeof(pointf));
UNSUPPORTED("586tid4nqbu8z1d3usgwdw8jp"); //                 (p[0].x = coord.x - ND_lw(n), p[0].y = coord.y - ND_ht(n) / 2.0, p[1].x = coord.x + ND_lw(n), p[1].y = coord.y + ND_ht(n) / 2.0);
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("d4glybpyvbpo9bu6saa4l6bde"); //             /* circle or ellipse */
UNSUPPORTED("9d4mrdegox0b1pm5xbfep5uko"); //             else if (poly->sides < 3 && poly->skew == 0.0 && poly->distortion == 0.0) {
UNSUPPORTED("cdqbx7sifcci71ypvwe0wz7yv"); //                 if (poly->regular) {
UNSUPPORTED("7dlfvykcyhmfeqqtnxuxpu83y"); //                     obj->url_map_shape = MAP_CIRCLE;
UNSUPPORTED("5um56x6io8un90j92meh20cjv"); //                     nump = 2;              /* center of circle and top right corner of bb */
UNSUPPORTED("yoyr2t09q9xi2y3drrvf3xad"); //                     p = (pointf*)zmalloc((nump)*sizeof(pointf));
UNSUPPORTED("7p1zauucnrwblaitis0khiz1r"); //                     p[0].x = coord.x;
UNSUPPORTED("e6o8x1sj9om9kkeixtn7zklcs"); //                     p[0].y = coord.y;
UNSUPPORTED("60m7eqldm742a0adfcs7kfqcg"); // 		    /* even vertices contain LL corner of bb */
UNSUPPORTED("9758i1qof39xb2yzi57in7oxt"); // 		    /* odd vertices contain UR corner of bb */
UNSUPPORTED("854obam1okv2gd6w4k2r6m9ot"); //                     p[1].x = coord.x + vertices[2*peripheries - 1].x;
UNSUPPORTED("2f7rjskpdx9cpkmm1a048d0oe"); //                     p[1].y = coord.y + vertices[2*peripheries - 1].y;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("9amplb4x49w5htb1q19aqmixx"); //                 else { /* ellipse is treated as polygon */
UNSUPPORTED("7cvkihpbuxq2fiho8en5lxlk5"); //                     obj->url_map_shape= MAP_POLYGON;
UNSUPPORTED("5n0o7h3635rfk8v1ofc24e5vh"); //                     p = pEllipse((double)(vertices[2*peripheries - 1].x),
UNSUPPORTED("68gocmgm77xvuicc7u8i05814"); //                                  (double)(vertices[2*peripheries - 1].y), nump);
UNSUPPORTED("34bytcpuln6qgd13en0tbt49q"); //                     for (i = 0; i < nump; i++) {
UNSUPPORTED("7d7wspnuodlkz25ma45nsi6ag"); //                         p[i].x += coord.x;
UNSUPPORTED("75mj4vd1u3ik8porqjoty5tt1"); //                         p[i].y += coord.y;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("1b3uzl88u778jb7f869wx7xpc"); //             /* all other polygonal shape */
UNSUPPORTED("89xddhb2xugz6vr9s2y1n4iko"); //             else {
UNSUPPORTED("7n9hod7wrkfif1s19ajej7dxw"); //                 int offset = (peripheries - 1)*(poly->sides);
UNSUPPORTED("8mmxiwtk0jn6wb1tdqa1b3w10"); //                 obj->url_map_shape = MAP_POLYGON;
UNSUPPORTED("9f3mabmc7lq7y42eheer04fzl"); //                 /* distorted or skewed ellipses and circles are polygons with 120
UNSUPPORTED("5xz548f5h9x5kyiwplsm69qf5"); //                  * sides. For mapping we convert them into polygon with sample sides
UNSUPPORTED("316yclqq5365l289qzfq39l6w"); //                  */
UNSUPPORTED("6tjxq9hxjynudn5fd5eb09jyb"); //                 if (poly->sides >= nump) {
UNSUPPORTED("bdbaovdssj5kkgpsxokx4trlm"); //                     int delta = poly->sides / nump;
UNSUPPORTED("yoyr2t09q9xi2y3drrvf3xad"); //                     p = (pointf*)zmalloc((nump)*sizeof(pointf));
UNSUPPORTED("c046rwe9l0lp00lemr5p5fxa"); //                     for (i = 0, j = 0; j < nump; i += delta, j++) {
UNSUPPORTED("c1dhcvofcsymhvnczsxmmhr41"); //                         p[j].x = coord.x + vertices[i + offset].x;
UNSUPPORTED("ei7ntnpjxjte5mvwta7em30ll"); //                         p[j].y = coord.y + vertices[i + offset].y;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("esb1hqr9y2ldyt0mt0w98tg6k"); //                 } else {
UNSUPPORTED("ofg0e43bcmuyf7t2nedsgevg"); //                     nump = sides;
UNSUPPORTED("yoyr2t09q9xi2y3drrvf3xad"); //                     p = (pointf*)zmalloc((nump)*sizeof(pointf));
UNSUPPORTED("34bytcpuln6qgd13en0tbt49q"); //                     for (i = 0; i < nump; i++) {
UNSUPPORTED("dnojnlwgkx6ddefag0zwo6ds2"); //                         p[i].x = coord.x + vertices[i + offset].x;
UNSUPPORTED("4dblb5sdbisslvfdhwsybgeh"); //                         p[i].y = coord.y + vertices[i + offset].y;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("3jir07ymknf0hmb9pv9x4dr3o"); //         else {
UNSUPPORTED("98n8obslcmy8y3s7e6jf9esg2"); //             /* we have to use the node's bounding box to map clickable region
UNSUPPORTED("50m9f9e7fwiwwh75wjf4clqoj"); //              * when requested output format is not capable of polygons.
UNSUPPORTED("29edlge3wqyp1gs0vymtr5700"); //              */
UNSUPPORTED("dtpqdcuqa1r9h12j3i5ycrvw1"); //             obj->url_map_shape = MAP_RECTANGLE;
UNSUPPORTED("64g4nd2mma198mwj1rn8lub1x"); //             nump = 2;
UNSUPPORTED("cfwfzr78yzuxsg39clh917i7v"); //             p = (pointf*)zmalloc((nump)*sizeof(pointf));
UNSUPPORTED("odbnll58vskylwajptbzx1ep"); //             p[0].x = coord.x - ND_lw(n);
UNSUPPORTED("nz1xqz7manrsw4hebsgnykie"); //             p[0].y = coord.y - (ND_ht(n) / 2);
UNSUPPORTED("40l7iiv57pa37xyblc80nhqjv"); //             p[1].x = coord.x + ND_rw(n);
UNSUPPORTED("bvo8cz7zx62rjde71khxdb3li"); //             p[1].y = coord.y + (ND_ht(n) / 2);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("4lzx178ws3xixysf6d4x8jxs2"); //         if (! (flags & (1<<13)))
UNSUPPORTED("6vlhqkskkzyjn8cw33u0m8dkp"); //             gvrender_ptf_A(job, p, p, nump);
UNSUPPORTED("d6dtek5hzc7rtaly67ykeq98d"); //         obj->url_map_p = p;
UNSUPPORTED("c1ycajl1r4i9caqbpdh9zmyx0"); //         obj->url_map_n = nump;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("aemq91z7mpchiy1jqeg9gcaix"); //     setColorScheme (agget (n, "colorscheme"));
UNSUPPORTED("65nxjiymni4pwghzckh64eskn"); //     gvrender_begin_node(job, n);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d9mzxl9say77sxvq7p8x64arq
// static void emit_end_node(GVJ_t * job) 
public static Object emit_end_node(Object... arg) {
UNSUPPORTED("2l9v7jqkrub7vxobkquy7asiq"); // static void emit_end_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ibtlropykj9c7r9ibxwqb2mq"); //     gvrender_end_node(job);
UNSUPPORTED("39iamwq9cd9iv3d2iyiaq8gz9"); //     pop_obj_state(job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 do63wzhzr0kzmjcdc6gjwjnj1
// static void emit_node(GVJ_t * job, node_t * n) 
public static Object emit_node(Object... arg) {
UNSUPPORTED("a610z8g79rvtm3401gis6ckc8"); // static void emit_node(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("byo308l1rmve5rmx8wt32juux"); //     char *style;
UNSUPPORTED("getfykrvugvlv3wxt5qm5ghl"); //     char **styles = 0;
UNSUPPORTED("9q7vvjxznd6x0u1t6fgd82byj"); //     char **sp;
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("1ox2ru6l44s8f2m2tqkkpb60"); //     if (ND_shape(n) 				     /* node has a shape */
UNSUPPORTED("2pbaf4f2fx6en7dizu6y1x508"); // 	    && node_in_layer(job, agraphof(n), n)    /* and is in layer */
UNSUPPORTED("bfz19cj6ftdbt8an3chivuz6r"); // 	    && node_in_box(n, job->clip)             /* and is in page/view */
UNSUPPORTED("7beo9zmd6okbo2j2mrhh57pg5"); // 	    && (ND_state(n) != gvc->common.viewNum)) /* and not already drawn */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("5xnzubm059ragnfr4w9qppbcd"); // 	ND_state(n) = gvc->common.viewNum; 	     /* mark node as drawn */
UNSUPPORTED("d06edad4blz58hs97kcth9q6u"); //         gvrender_comment(job, agnameof(n));
UNSUPPORTED("cagpugtwz1xuxol6c2w44i117"); // 	s = late_string(n, N_comment, "");
UNSUPPORTED("7oyyy6d3itm6qqhr3p5zu6ded"); // 	if (s[0])
UNSUPPORTED("8gbf99sx0atz9ku0tfjv4m4o3"); // 	    gvrender_comment(job, s);
UNSUPPORTED("9ihgp28ukdi3iied1az7rndhv"); // 	style = late_string(n, N_style, "");
UNSUPPORTED("12ihr78gv09dxppuorymkgt75"); // 	if (style[0]) {
UNSUPPORTED("32d3t9w0x6lbw2olmibm97mqd"); // 	    styles = parse_style(style);
UNSUPPORTED("ai2h90fa4b5ss40yyc0ehvrzd"); // 	    sp = styles;
UNSUPPORTED("9v2c8p99l7pj3zqwvtb298adq"); // 	    while ((p = *sp++)) {
UNSUPPORTED("xtuts27rjtqvzh4gjkw96ime"); // 		if ((*(p)==*("invis")&&!strcmp(p,"invis"))) return;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("58q1665qa9481hld1ioe4zwoj"); // 	emit_begin_node(job, n);
UNSUPPORTED("mfw9xokftdmhodm2jpp901c5"); // 	ND_shape(n)->fns->codefn(job, n);
UNSUPPORTED("cncg3snxi0wuz6b1dkj8gz85t"); // 	if (ND_xlabel(n) && ND_xlabel(n)->set)
UNSUPPORTED("67z3k2l4flib7gfhwxaekdsam"); // 	    emit_label(job, EMIT_NLABEL, ND_xlabel(n));
UNSUPPORTED("2jcokqlllmekcinbvegkojl7u"); // 	emit_end_node(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1690phewavvug1dfq83cwep5d
// static pointf computeoffset_p(pointf p, pointf q, double d) 
public static Object computeoffset_p(Object... arg) {
UNSUPPORTED("63wh9kozu453znl0upnphf9mi"); // static pointf computeoffset_p(pointf p, pointf q, double d)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5uemny2bjcvw6dq2uqo1t1xl0"); //     pointf res;
UNSUPPORTED("cj9v3vgjktw1o09qn9i35u6gl"); //     double x = p.x - q.x, y = p.y - q.y;
UNSUPPORTED("c619zohl4d6wz9g3kekvpg7d1"); //     /* keep d finite as line length approaches 0 */
UNSUPPORTED("5c6u3xrusgxwuo66z5mvimg9m"); //     d /= sqrt(x * x + y * y + .0001);
UNSUPPORTED("4lo34cbbs51klu8okkvjbbg1u"); //     res.x = y * d;
UNSUPPORTED("cdsev61yjq1ezm8vyeqy9bskw"); //     res.y = -x * d;
UNSUPPORTED("cezffb75elgmxmthji4vj0se9"); //     return res;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dly15js1s0j0q9xlzznflvp5o
// static pointf computeoffset_qr(pointf p, pointf q, pointf r, pointf s, 			       double d) 
public static Object computeoffset_qr(Object... arg) {
UNSUPPORTED("8sdovg67zcg8hbc8uxomazvo7"); // static pointf computeoffset_qr(pointf p, pointf q, pointf r, pointf s,
UNSUPPORTED("cw59rrix8ii9c4gpgc087wzod"); // 			       double d)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5uemny2bjcvw6dq2uqo1t1xl0"); //     pointf res;
UNSUPPORTED("83zpklqoz4hkcpabg6bg1meo4"); //     double len;
UNSUPPORTED("sk1w1xfer0nznuf4hm21fm98"); //     double x = q.x - r.x, y = q.y - r.y;
UNSUPPORTED("2c5il9v49cdl4tr8gyt83did1"); //     len = sqrt(x * x + y * y);
UNSUPPORTED("2ach8qtgjeze9t2249wcwdkat"); //     if (len < .0001) {
UNSUPPORTED("6lej7y0livho67yeaclv11eqn"); // 	/* control points are on top of each other
UNSUPPORTED("c2tbggffhpyqvaphzbz85js2"); // 	   use slope between endpoints instead */
UNSUPPORTED("ld3z8wtan3gkvuzdn53c58vc"); // 	x = p.x - s.x, y = p.y - s.y;
UNSUPPORTED("7htvz9tvqvjgwskwcyc2urf53"); // 	/* keep d finite as line length approaches 0 */
UNSUPPORTED("ealmen1ps4qroua1hv1qypt8o"); // 	len = sqrt(x * x + y * y + .0001);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("p5a5c43qtb7svsztnqiqbvut"); //     d /= len;
UNSUPPORTED("4lo34cbbs51klu8okkvjbbg1u"); //     res.x = y * d;
UNSUPPORTED("cdsev61yjq1ezm8vyeqy9bskw"); //     res.y = -x * d;
UNSUPPORTED("cezffb75elgmxmthji4vj0se9"); //     return res;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9jbox5ffqmnuiqxjmiewkjd7v
// static void emit_attachment(GVJ_t * job, textlabel_t * lp, splines * spl) 
public static Object emit_attachment(Object... arg) {
UNSUPPORTED("cqsl6q4bc0by6b7m5fjbaz0ff"); // static void emit_attachment(GVJ_t * job, textlabel_t * lp, splines * spl)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("69bvnbgbqrcytrd76lwcg0vka"); //     pointf sz, AF[3];
UNSUPPORTED("7f6xhv1pxs09lj5ogjqiqd0jc"); //     unsigned char *s;
UNSUPPORTED("3asm1mshny9o50slevb5zf6uy"); //     for (s = (unsigned char *) (lp->text); *s; s++) {
UNSUPPORTED("ndpe342kdaool85s57l4ih7r"); // 	if (isspace(*s) == 0)
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7w2f0c8cdlaczfa4mtfux75ou"); //     if (*s == 0)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("adxqzkeq9mg15dgugx9z222go"); //     sz = lp->dimen;
UNSUPPORTED("8n9kjtyakr4mqv1td5vrgkb96"); //     AF[0] = pointfof(lp->pos.x + sz.x / 2., lp->pos.y - sz.y / 2.);
UNSUPPORTED("6kkjo8ld87lpzum3u7wv6yv4b"); //     AF[1] = pointfof(AF[0].x - sz.x, AF[0].y);
UNSUPPORTED("6ezlaa100st3k35o4mzoczre2"); //     AF[2] = dotneato_closest(spl, lp->pos);
UNSUPPORTED("aigctuc4kajr8onbk5fmrvh00"); //     /* Don't use edge style to draw attachment */
UNSUPPORTED("4g8oyutwebzej18aaiz74zb9k"); //     gvrender_set_style(job, job->gvc->defaultlinestyle);
UNSUPPORTED("6farm4z5p1r4wzjnb5f1kioin"); //     /* Use font color to draw attachment
UNSUPPORTED("25anit6qhtt5ef1ewc1h0q577"); //        - need something unambiguous in case of multicolored parallel edges
UNSUPPORTED("f115hnh0a1ezbinf75vc9r0lg"); //        - defaults to black for html-like labels
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("bl5mm7hn2il0n2e623rbq8q5w"); //     gvrender_set_pencolor(job, lp->fontcolor);
UNSUPPORTED("35prexehk0x6n14ojx7sihunn"); //     gvrender_polyline(job, AF, 3);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4g1n8pxbh06t81obycv6rqtxv
// static char* default_pencolor(char *pencolor, char *deflt) 
public static Object default_pencolor(Object... arg) {
UNSUPPORTED("4wukcoxynojzor25ps1rby9yw"); // static char* default_pencolor(char *pencolor, char *deflt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8kywmm7f51dmrjngxwvnpsd4o"); //     static char *buf;
UNSUPPORTED("22dcr6aukx12do9pe4dib69c4"); //     static int bufsz;
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("a005u14z35hf8yjnafoaaeqg"); //     int len, ncol;
UNSUPPORTED("e6y6l26pcnusy834r3jfxyd52"); //     ncol = 1;
UNSUPPORTED("6zcf9gon91dymhtem9dwap4mh"); //     for (p = pencolor; *p; p++) {
UNSUPPORTED("2s6gq2wmmin2ab5zt90m5xjep"); // 	if (*p == ':')
UNSUPPORTED("9f19jjr21zaezdxswj6tevd9n"); // 	    ncol++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2134j62fnfo06vfq9fo45gceu"); //     len = ncol * (strlen(deflt) + 1);
UNSUPPORTED("55qowwh2rcv2v2uu70hde9ct4"); //     if (bufsz < len) {
UNSUPPORTED("19odu3570teiwv839flm3iezt"); // 	bufsz = len + 10;
UNSUPPORTED("e15ktgr11acljfi5jubpq5sba"); // 	buf = realloc(buf, bufsz);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3nutbraj7qprn5hvmj8fqtl63"); //     strcpy(buf, deflt);
UNSUPPORTED("22y7o7tq5wsmfgkczgy0hqxmx"); //     while(--ncol) {
UNSUPPORTED("9se30dn1tgkm9kisqzl2ojhic"); // 	strcat(buf, ":");
UNSUPPORTED("68ovea8yallwok9w5wpk46e5m"); // 	strcat(buf, deflt);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5jfpogdyby101eyuw2dhtb5cg"); //     return buf;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4idfslavgy1wy953xlflg5ws6
// static double approxLen (pointf* pts) 
public static Object approxLen(Object... arg) {
UNSUPPORTED("6tz6ed7moergtlszt1ajl0p9v"); // static double approxLen (pointf* pts)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3s4rz758a6plk3e82llqp2gg9"); //     double d = DIST(pts[0],pts[1]);
UNSUPPORTED("9v02me6trkr3y3f94cm9fyk9a"); //     d += DIST(pts[1],pts[2]);
UNSUPPORTED("8r2j0wkc637k7tyzg3r6uh9iv"); //     d += DIST(pts[2],pts[3]);
UNSUPPORTED("3r3o80n61nmy2jv0ezi9xg2xp"); //     return d;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c24egok0eodx1gogrwwnimllw
// static void splitBSpline (bezier* bz, float t, bezier* left, bezier* right) 
public static Object splitBSpline(Object... arg) {
UNSUPPORTED("bzosun8763baoenilgqapqo0v"); // static void splitBSpline (bezier* bz, float t, bezier* left, bezier* right)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dakf3h64dqiulqbamu0w9twdu"); //     int i, j, k, cnt = (bz->size - 1)/3;
UNSUPPORTED("7lwfrj61cydg2xwok9ruezq5q"); //     double* lens;
UNSUPPORTED("6xpvacdzhffp9veax0hnyrxsk"); //     double last, len, sum;
UNSUPPORTED("48n1zwofayobr58hhiz0y5wfp"); //     pointf* pts;
UNSUPPORTED("89zep3p5j2e5egc7g57ipff0g"); //     float r;
UNSUPPORTED("6556o7pk7swku5m4ocatctrjt"); //     if (cnt == 1) {
UNSUPPORTED("e6f8rl0x4aqawbb1f9o0iw15m"); // 	left->size = 4;
UNSUPPORTED("twafm7lir3aq9n2mil73iowf"); // 	left->list = (pointf*)zmalloc((4)*sizeof(pointf));
UNSUPPORTED("1baicin4fqf7m9f8aohq6veca"); // 	right->size = 4;
UNSUPPORTED("4iwp6vrwtjet80pf2dloisel9"); // 	right->list = (pointf*)zmalloc((4)*sizeof(pointf));
UNSUPPORTED("cewi4etd9db03yftetga0xkdh"); // 	Bezier (bz->list, 3, t, left->list, right->list);
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dd6fkzw1fpmv18pgue4xs5bqi"); //     lens = (double*)zmalloc((cnt)*sizeof(double));
UNSUPPORTED("a6vxbyim8v2mmrdcey3t2n4r0"); //     sum = 0;
UNSUPPORTED("6vpzf70qotrvlbnwdne2riv0x"); //     pts = bz->list;
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("8r4arv0b9go700qz2tm3mm221"); // 	lens[i] = approxLen (pts);
UNSUPPORTED("eoo69513qfc7r4hl1k5c2eiwv"); // 	sum += lens[i];
UNSUPPORTED("e4542wxbu4szwpfa8e0ra68f2"); // 	pts += 3;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4ajaxhjxm9owoq0tyl6901zpb"); //     len = t*sum;
UNSUPPORTED("a6vxbyim8v2mmrdcey3t2n4r0"); //     sum = 0;
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("eoo69513qfc7r4hl1k5c2eiwv"); // 	sum += lens[i];
UNSUPPORTED("5boo0t1khz74gcsiopw0n3w2z"); // 	if (sum >= len)
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("75pszhfxvdcbo780wsjt6bh92"); //     left->size = 3*(i+1) + 1;
UNSUPPORTED("6dip2t5t8rbleilxa8lj81obn"); //     left->list = (pointf*)zmalloc((left->size)*sizeof(pointf));
UNSUPPORTED("5p4l0m9e5xsnuegq2yhv5c3d6"); //     right->size = 3*(cnt-i) + 1;
UNSUPPORTED("26r2b2ifak9lziwwpho1xrj5x"); //     right->list = (pointf*)zmalloc((right->size)*sizeof(pointf));
UNSUPPORTED("3wleiy529fpgcsz1ikho2emvm"); //     for (j = 0; j < left->size; j++)
UNSUPPORTED("ba9gueiwlmayc8ysz2l1cwcxj"); // 	left->list[j] = bz->list[j];
UNSUPPORTED("ep0odw2vpvxzya4y8rennmr9k"); //     k = j - 4;
UNSUPPORTED("e6f27aong4yxk1ijo8d8w0888"); //     for (j = 0; j < right->size; j++)
UNSUPPORTED("9wqub3rjwa4mbj3s7e2szhas0"); // 	right->list[j] = bz->list[k++];
UNSUPPORTED("7a9c792hfdlnnk7sj8q4whllc"); //     last = lens[i];
UNSUPPORTED("8sjlbix5lxv8ks4no35ryh1ky"); //     r = (len - (sum - last))/last;
UNSUPPORTED("d2rexqliaoccgx5aaon5n8xad"); //     Bezier (bz->list + 3*i, 3, r, left->list + 3*i, right->list);
UNSUPPORTED("abtwh44up8wmpmdx48y0s2mc9"); //     free (lens);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 emsbbp4kdnhrbf2gca7ikthj5
// static int multicolor (GVJ_t * job, edge_t * e, char** styles, char* colors, int num, double arrowsize, double penwidth) 
public static Object multicolor(Object... arg) {
UNSUPPORTED("dvqr9ynn0c9bn4xfcpie8zyez"); // static int multicolor (GVJ_t * job, edge_t * e, char** styles, char* colors, int num, double arrowsize, double penwidth)
UNSUPPORTED("yo7buicdiu29rv5vxhas0v3s"); // { 
UNSUPPORTED("37thdceezsvepe7tlyfatrbcw"); //     bezier bz;
UNSUPPORTED("2g6n25q0mmfzs7lbcmb5xqnsw"); //     bezier bz0, bz_l, bz_r;
UNSUPPORTED("ec8yrss5nd4ir33xtxncbfbx1"); //     int i, rv;
UNSUPPORTED("8z3a2qjysx0m326m5qjy91tnu"); //     colorsegs_t* segs;
UNSUPPORTED("8zv1ffgik05z2fcc41di32sn8"); //     colorseg_t* s;
UNSUPPORTED("491d6vy43b2c5s364tuuj531z"); //     char* endcolor;
UNSUPPORTED("23904qu3vgmgg4o78f0lbtk03"); //     double left;
UNSUPPORTED("3wtewf1w0788agxoxikqp6n0a"); //     int first;  /* first segment with t > 0 */
UNSUPPORTED("5vkb2ckjwqesd3277mqs5of2i"); //     rv = parseSegs (colors, num, &segs);
UNSUPPORTED("2m7c7iiy39zf5k6eva7052n4u"); //     if (rv > 1) {
UNSUPPORTED("cfaq63es7s358djxe08cx7kjx"); // 	Agraph_t* g = agraphof(agtail(e));
UNSUPPORTED("82aqfdoa44vt6zjffu4h6i7tz"); // 	agerr (AGPREV, "in edge %s%s%s\n", agnameof(agtail(e)), (agisdirected(g)?" -> ":" -- "), agnameof(aghead(e)));
UNSUPPORTED("bqes0nkjkdq91khi1ouux7zfv"); // 	if (rv == 2)
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("tqrvg9la1h2jgnwa0zql3ck5"); //     else if (rv == 1)
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("egp4hta3woqfkcxs9fcq2yvuy"); //     for (i = 0; i < ED_spl(e)->size; i++) {
UNSUPPORTED("cgwd7hfmn0br1bm5fi4gr97b3"); // 	left = 1;
UNSUPPORTED("5ngoqqxxd5huppctrruhxjota"); // 	bz = ED_spl(e)->list[i];
UNSUPPORTED("3vet6pylmf84jbf0ssb1sifb7"); // 	first = 1;
UNSUPPORTED("d2ic66tp4jlglvga1xy32sk6j"); // 	for (s = segs->segs; s->color; s++) {
UNSUPPORTED("78q8ibx2yffz6m8wihta0ii64"); // 	    if ((((s->t) < 1E-5) && ((s->t) > -1E-5))) continue;
UNSUPPORTED("xa07dy2pw68wlizyfp8oymag"); //     	    gvrender_set_pencolor(job, s->color);
UNSUPPORTED("56ofcgemtdmz0pup4itjvidtj"); // 	    left -= s->t;
UNSUPPORTED("chr2o19f7c3h4ymbaatmy5jae"); // 	    endcolor = s->color;
UNSUPPORTED("ay38rk7fywnv41q0742j6585x"); // 	    if (first) {
UNSUPPORTED("7ttvwgvz7wpyo71aggs2auiau"); // 		first = 0;
UNSUPPORTED("9rs2pz8j0ynilgapygagbgd89"); // 		splitBSpline (&bz, s->t, &bz_l, &bz_r);
UNSUPPORTED("99cbkfbl5fivdra5eaj0kf8n5"); // 		gvrender_beziercurve(job, bz_l.list, bz_l.size, 0, 0, 0);
UNSUPPORTED("vrjjl72p0ams86w0e3fsnith"); // 		free (bz_l.list);
UNSUPPORTED("48xtw1gzugr4tgwurr50x6d8e"); // 		if ((((left) < 1E-5) && ((left) > -1E-5))) {
UNSUPPORTED("bkk1vblwk5js8005v0nk0jk15"); // 		    free (bz_r.list);
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4mbggres28g1urrdviyjscdh0"); // 	    else if ((((left) < 1E-5) && ((left) > -1E-5))) {
UNSUPPORTED("16m5vvsclnhu00mxtqc62jc5q"); // 		gvrender_beziercurve(job, bz_r.list, bz_r.size, 0, 0, 0);
UNSUPPORTED("dukn62ogiycorcn8c4esuwmu1"); // 		free (bz_r.list);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2ej2iwx8roed79sbv44tte9sg"); // 		bz0 = bz_r;
UNSUPPORTED("82oq6xp2v59bcqu7z1dbvilyn"); // 		splitBSpline (&bz0, (s->t)/(left+s->t), &bz_l, &bz_r);
UNSUPPORTED("btj36obnij7vfhl4k6li7ixlh"); // 		free (bz0.list);
UNSUPPORTED("99cbkfbl5fivdra5eaj0kf8n5"); // 		gvrender_beziercurve(job, bz_l.list, bz_l.size, 0, 0, 0);
UNSUPPORTED("vrjjl72p0ams86w0e3fsnith"); // 		free (bz_l.list);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bjb95001z7szmwgcquyyal5ta"); //                 /* arrow_gen resets the job style  (How?  FIXME)
UNSUPPORTED("eevkw6z2fbi2d7kpx3zp0uuvc"); //                  * If we have more splines to do, restore the old one.
UNSUPPORTED("500s7rroyq30zj2qxhdp4jluo"); //                  * Use local copy of penwidth to work around reset.
UNSUPPORTED("316yclqq5365l289qzfq39l6w"); //                  */
UNSUPPORTED("2qc36pchob3dha7c8jedwalcn"); // 	if (bz.sflag) {
UNSUPPORTED("b6y3ukuisz1mssdko3l2oa179"); //     	    gvrender_set_pencolor(job, segs->segs->color);
UNSUPPORTED("b33lxxzdyehr8v8uwpkcgabuh"); //     	    gvrender_set_fillcolor(job, segs->segs->color);
UNSUPPORTED("7l0hrlftphvjrn1f7whgtaofw"); // 	    arrow_gen(job, EMIT_TDRAW, bz.sp, bz.list[0], arrowsize, penwidth, bz.sflag);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("973cbk976xio10xxw9bgkyen2"); // 	if (bz.eflag) {
UNSUPPORTED("7mplmupdwkpendc0q8ocljk18"); //     	    gvrender_set_pencolor(job, endcolor);
UNSUPPORTED("ahacu4j8j9b29k2c80wyfedus"); //     	    gvrender_set_fillcolor(job, endcolor);
UNSUPPORTED("d89bwaeqinnrhngamz9mrl01r"); // 	    arrow_gen(job, EMIT_HDRAW, bz.ep, bz.list[bz.size - 1], arrowsize, penwidth, bz.eflag);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9zc0fxovb3sw9wibqdnoqamso"); // 	if ((ED_spl(e)->size>1) && (bz.sflag||bz.eflag) && styles) 
UNSUPPORTED("1sadq9tjru1s6bzpcgl677k6i"); // 	    gvrender_set_style(job, styles);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("864f5ylvxgoy5rwmwl4pstg2p"); //     free (segs);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 13jatehy3n1t03488fq3ek8st
// static void free_stroke (stroke_t* sp) 
public static Object free_stroke(Object... arg) {
UNSUPPORTED("ahnzha1pnhrfmdhm9uwi8iuao"); // static void free_stroke (stroke_t* sp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3h9wjmlt6pp3ti25zl9lhmigy"); //     if (sp) {
UNSUPPORTED("c8uned45gp04il3dvzfnj6g5k"); // 	free (sp->vertices);
UNSUPPORTED("6tcxn9cesyebua29d6q299172"); // 	free (sp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5b9ltl2mduk3ua0x8235o922t
// static double forfunc (double curlen, double totallen, double initwid) 
public static Object forfunc(Object... arg) {
UNSUPPORTED("3t80v9xj410ss5j0pqnc1zrjp"); // static double forfunc (double curlen, double totallen, double initwid)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7w1a8rqs29lelmnia96oa58f9"); //     return ((1 - (curlen/totallen))*initwid/2.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dsgrrv9hsyak20n1h9x5jvp3f
// static double revfunc (double curlen, double totallen, double initwid) 
public static Object revfunc(Object... arg) {
UNSUPPORTED("bld5nqyhkdbuxeay2ll473qr6"); // static double revfunc (double curlen, double totallen, double initwid)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("blt0wpagxakbwm5voqwfqir05"); //     return (((curlen/totallen))*initwid/2.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3evv2ec1hr5uijikij2h26w3t
// static double nonefunc (double curlen, double totallen, double initwid) 
public static Object nonefunc(Object... arg) {
UNSUPPORTED("e8e1fgr1nlozsj7m2ijaws2gd"); // static double nonefunc (double curlen, double totallen, double initwid)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2zcusggl2dike7m7cwbckjltr"); //     return (initwid/2.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 333yk8u7vrjjv2pfad3z273ra
// static double bothfunc (double curlen, double totallen, double initwid) 
public static Object bothfunc(Object... arg) {
UNSUPPORTED("au7dp9qva7eo751dhmuu29nq9"); // static double bothfunc (double curlen, double totallen, double initwid)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7hpykssamp5naz4fl0qzgklsq"); //     double fr = curlen/totallen;
UNSUPPORTED("bixgbzdc1rbkvbqmjxn5drr4k"); //     if (fr <= 0.5) return (fr*initwid);
UNSUPPORTED("1b7ckst75n0s5njx7vvi7w3jw"); //     else return ((1-fr)*initwid);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 256panf42dptclohlg3aolfqv
// static radfunc_t  taperfun (edge_t* e) 
public static Object taperfun(Object... arg) {
UNSUPPORTED("ygkpgli37inuq18f0rtbwcs1"); // static radfunc_t 
UNSUPPORTED("6r0vilajj0np6wokfkwjl7ykm"); // taperfun (edge_t* e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6ms33tgqoacyvkev0g6t87x2n"); //     char* attr;
UNSUPPORTED("1un0u4nwlweclzc8w1ydozt5q"); //     if (E_dir && ((attr = agxget(e, E_dir)))[0]) {
UNSUPPORTED("bslhjzz27pwavlahlu4z4vaei"); // 	if ((*(attr)==*("forward")&&!strcmp(attr,"forward"))) return forfunc;
UNSUPPORTED("eulgmrvzxxnnylptzfoj4ebix"); // 	if ((*(attr)==*("back")&&!strcmp(attr,"back"))) return revfunc;
UNSUPPORTED("7jy0p75av38y41nrehkh8eh5r"); // 	if ((*(attr)==*("both")&&!strcmp(attr,"both"))) return bothfunc;
UNSUPPORTED("15n6mwfm4pg2fpvr7pkllvmcs"); // 	if ((*(attr)==*("none")&&!strcmp(attr,"none"))) return nonefunc;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bsel6fx8wjsph743urtdg1qfv"); //     return (agisdirected(agraphof(aghead(e))) ? forfunc : nonefunc);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7u6cnonl0chpkjro9quo09fyr
// static void emit_edge_graphics(GVJ_t * job, edge_t * e, char** styles) 
public static Object emit_edge_graphics(Object... arg) {
UNSUPPORTED("44vrfcbqr2d1szhfncg37jn9c"); // static void emit_edge_graphics(GVJ_t * job, edge_t * e, char** styles)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dyh4es8zja669xh6w704698u1"); //     int i, j, cnum, numc = 0, numsemi = 0;
UNSUPPORTED("4fd30tht9vca24rbn5qkxxm2h"); //     char *color, *pencolor, *fillcolor;
UNSUPPORTED("dou14upt5u5f8go87xjj5jdtc"); //     char *headcolor, *tailcolor, *lastcolor;
UNSUPPORTED("da94242ir0732sz1wjfrstzam"); //     char *colors = NULL;
UNSUPPORTED("37thdceezsvepe7tlyfatrbcw"); //     bezier bz;
UNSUPPORTED("3l9st5qdrh657gcmhelkkjwk3"); //     splines offspl, tmpspl;
UNSUPPORTED("9sg7pim3sh38o6cyrrysxdihv"); //     pointf pf0, pf1, pf2 = { 0, 0 }, pf3, *offlist, *tmplist;
UNSUPPORTED("20vx89srcsfcjhjnu7dhv2ha0"); //     double arrowsize, numc2, penwidth=job->obj->penwidth;
UNSUPPORTED("a4px33i4moqe8ybwatz0g8k6"); //     char* p;
UNSUPPORTED("d7s9yrspa1yd9tdzj22df8zfx"); //     boolean tapered = 0;
UNSUPPORTED("d7dk8nv783iyi7g16fbyvoswy"); //     setColorScheme (agget (e, "colorscheme"));
UNSUPPORTED("4pdkymy0wuxj1yn9xqqzp2x4h"); //     if (ED_spl(e)) {
UNSUPPORTED("kp36oq4bt444kf967o82pd23"); // 	arrowsize = late_double(e, E_arrowsz, 1.0, 0.0);
UNSUPPORTED("47l2wt9sohpfh3winazhczpb4"); // 	color = late_string(e, E_color, "");
UNSUPPORTED("d2n6qh5kiavdnxsfrc0fqqv4e"); // 	if (styles) {
UNSUPPORTED("54isti54aox6yj0vuk6j86qhs"); // 	    char** sp = styles;
UNSUPPORTED("9v2c8p99l7pj3zqwvtb298adq"); // 	    while ((p = *sp++)) {
UNSUPPORTED("2ku2emqwvspqz2c6481orcixb"); // 		if ((*(p)==*("tapered")&&!strcmp(p,"tapered"))) {
UNSUPPORTED("adyiyvvw6sowssjqqu2wojmtv"); // 		    tapered = 1;
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1p0odq4xbiiyk9zulrtgh78p7"); // 	/* need to know how many colors separated by ':' */
UNSUPPORTED("epbhl3zzqty7zxgexbqsijz3t"); // 	for (p = color; *p; p++) {
UNSUPPORTED("1zdp9y9tqsxdxtj8gfy3leixo"); // 	    if (*p == ':')
UNSUPPORTED("5iqqlev08y8ukmtx3vha7s7kc"); // 		numc++;
UNSUPPORTED("88xgi4gcg06c70hzvrxc2pij2"); // 	    else if (*p == ';')
UNSUPPORTED("ajossrlhthg39idzi4plrieni"); // 		numsemi++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("f1kfj1lvizljipxfa8m49o34e"); // 	if (numsemi && numc) {
UNSUPPORTED("9vi0u3wh25rzf9qx22ub742q9"); // 	    if (multicolor (job, e, styles, color, numc+1, arrowsize, penwidth)) {
UNSUPPORTED("2hm4ng34o2djs8z7qg2ac3c1x"); // 		color = "black";
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1adkfqmo3ykccvljbvh44veke"); // 	fillcolor = pencolor = color;
UNSUPPORTED("5zjv2jq8jwy0xnngx03rvezxj"); // 	if (ED_gui_state(e) & (1<<0)) {
UNSUPPORTED("e1znbw6oeo9yq3u94bd736qh0"); // 	    pencolor = late_nnstring(e, E_activepencolor,
UNSUPPORTED("1g9t8pkrtaf7lucfw4wjhwq1b"); // 			default_pencolor(pencolor, "#808080"));
UNSUPPORTED("2jqwe3bwxzh4ldnv9yft80lr9"); // 	    fillcolor = late_nnstring(e, E_activefillcolor, "#fcfcfc");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("d3olcb516sc9gnsf00xgo98dd"); // 	else if (ED_gui_state(e) & (1<<1)) {
UNSUPPORTED("6n83olgg54bds3atz6ocdxcgm"); // 	    pencolor = late_nnstring(e, E_selectedpencolor,
UNSUPPORTED("bjgr6yow0b74j4um7a2sekd1t"); // 			default_pencolor(pencolor, "#303030"));
UNSUPPORTED("cm8kancv5dyuwquvezp6bfx54"); // 	    fillcolor = late_nnstring(e, E_selectedfillcolor, "#e8e8e8");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3szmncx89hltlxv3fqrn7abjs"); // 	else if (ED_gui_state(e) & (1<<3)) {
UNSUPPORTED("9pzm0fj2ru19falkosarggftj"); // 	    pencolor = late_nnstring(e, E_deletedpencolor,
UNSUPPORTED("1ivzzes2dmdgnv86yk0ffaf2u"); // 			default_pencolor(pencolor, "#e0e0e0"));
UNSUPPORTED("8o3b6zumizq20edhwhmjjtvjf"); // 	    fillcolor = late_nnstring(e, E_deletedfillcolor, "#f0f0f0");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("59tkofvq26pokzebj54fxmvn"); // 	else if (ED_gui_state(e) & (1<<2)) {
UNSUPPORTED("9ur79rphks1cca1bf289echp6"); // 	    pencolor = late_nnstring(e, E_visitedpencolor,
UNSUPPORTED("5c0jkjan0v1lp9rthvuo36q5e"); // 			default_pencolor(pencolor, "#101010"));
UNSUPPORTED("73uog35zh3g1t58z0zbwtmoxh"); // 	    fillcolor = late_nnstring(e, E_visitedfillcolor, "#f8f8f8");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("70e3lpuncfyeq8ow3o0twrknb"); // 	    fillcolor = late_nnstring(e, E_fillcolor, color);
UNSUPPORTED("besdb3lw2v7ps66w9w7tit6bj"); // 	if (pencolor != color)
UNSUPPORTED("4s4clbt0ohbem378iomo2d67o"); //     	    gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("ajy8xtlpgl5nu72sbovqnt6fl"); // 	if (fillcolor != color)
UNSUPPORTED("b156qgcfm85r551m3jmzhysy7"); // 	    gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("eso6mj6u37z0eyzv9bnyclws9"); // 	color = pencolor;
UNSUPPORTED("75fa2ioc5gvzkhqk5qlljrc5k"); // 	if (tapered) {
UNSUPPORTED("d2fgbjxcola63obus1hikp8pg"); // 	    stroke_t* stp;
UNSUPPORTED("4fan4tn82jocc40ifj8ouifx4"); // 	    if (*color == '\0') color = "black";
UNSUPPORTED("287ivnme7uzrjofjffd2b728s"); // 	    if (*fillcolor == '\0') fillcolor = "black";
UNSUPPORTED("aa64pbbtgdbmq07oeeb6b6xd6"); //     	    gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("b4ftxmadx3875jpvifjzhsz4d"); // 	    gvrender_set_fillcolor(job, color);
UNSUPPORTED("3cdfvg0cegvnb5z8rbltyba5w"); // 	    bz = ED_spl(e)->list[0];
UNSUPPORTED("bstcwq3pyy3ut7b2lsr32fr81"); // 	    stp = taper (&bz, taperfun (e), penwidth, 0, 0);
UNSUPPORTED("4b4v79dvjxu6pk3cfm0u6f6ec"); // 	    gvrender_polygon(job, stp->vertices, stp->nvertices, NOT(0));
UNSUPPORTED("chpwwmn3mqyi2bzsp7whr9nd9"); // 	    free_stroke (stp);
UNSUPPORTED("2dyaq1mbp4llru6ov9chn9lky"); //     	    gvrender_set_pencolor(job, color);
UNSUPPORTED("7tv6l7sws5ownkmdcnfh1bd70"); // 	    if (fillcolor != color)
UNSUPPORTED("c8g4zrr9fhcg9l45gm7ut3wkl"); // 		gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("4ulz6ze3ok8dfrvqvzfe163do"); // 	    if (bz.sflag) {
UNSUPPORTED("3bsc7v0uj4ukdu3q4yku6pyc5"); // 		arrow_gen(job, EMIT_TDRAW, bz.sp, bz.list[0], arrowsize, penwidth, bz.sflag);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5t5bh9txkzxp6h1lozsf250ww"); // 	    if (bz.eflag) {
UNSUPPORTED("dosyp6r5kbys1grl265mqm6d6"); // 		arrow_gen(job, EMIT_HDRAW, bz.ep, bz.list[bz.size - 1], arrowsize, penwidth, bz.eflag);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3rk5e1uiwm7hzn0bhxpzi5dey"); // 	/* if more than one color - then generate parallel beziers, one per color */
UNSUPPORTED("arvi3zjcegh42bgtwaulg8wkn"); // 	else if (numc) {
UNSUPPORTED("8eaaczlv3zth4i3rc8kryne45"); // 	    /* calculate and save offset vector spline and initialize first offset spline */
UNSUPPORTED("8a120p6zsfs8ycbql82yqol7p"); // 	    tmpspl.size = offspl.size = ED_spl(e)->size;
UNSUPPORTED("e3xuwliq0lpq61lr4e3efh174"); // 	    offspl.list = malloc(sizeof(bezier) * offspl.size);
UNSUPPORTED("41o3n44hn71jtdapbdfe0y9si"); // 	    tmpspl.list = malloc(sizeof(bezier) * tmpspl.size);
UNSUPPORTED("8pvfophsjdyrye9m6oieixnvf"); // 	    numc2 = (2 + numc) / 2.0;
UNSUPPORTED("7wvoxnl3dqatxwcvgzbrvp8lz"); // 	    for (i = 0; i < offspl.size; i++) {
UNSUPPORTED("6uzqmcfjwogzxlwn3gsd5jdj"); // 		bz = ED_spl(e)->list[i];
UNSUPPORTED("4lo2g191z1b9knaq7l6mnn6bh"); // 		tmpspl.list[i].size = offspl.list[i].size = bz.size;
UNSUPPORTED("cc4js3gwy5jel80gaqbo8y129"); // 		offlist = offspl.list[i].list = malloc(sizeof(pointf) * bz.size);
UNSUPPORTED("vf689dmffmji3k8sgjrc525g"); // 		tmplist = tmpspl.list[i].list = malloc(sizeof(pointf) * bz.size);
UNSUPPORTED("6g7ruy3ae9horpmcz5nfjwyvm"); // 		pf3 = bz.list[0];
UNSUPPORTED("7foyxeolsc28jypsxdwryyo6j"); // 		for (j = 0; j < bz.size - 1; j += 3) {
UNSUPPORTED("du9m49x179tlyjv3kpi4ajyxu"); // 		    pf0 = pf3;
UNSUPPORTED("4kiwxmoozlkcz1l62hop75o9g"); // 		    pf1 = bz.list[j + 1];
UNSUPPORTED("euzly3uj0433xtqrcskytd7pi"); // 		    /* calculate perpendicular vectors for each bezier point */
UNSUPPORTED("8dvury325tlnhct9axnqkloml"); // 		    if (j == 0)	/* first segment, no previous pf2 */
UNSUPPORTED("eqrlk7aad4crtsnmovu8rx5e6"); // 			offlist[j] = computeoffset_p(pf0, pf1, 2.0);
UNSUPPORTED("ybsy5tjwpggeq6c9pm1r0mvg"); // 		    else	/* i.e. pf2 is available from previous segment */
UNSUPPORTED("1x7zjqoo6zzgcjg1fmrin53gg"); // 			offlist[j] = computeoffset_p(pf2, pf1, 2.0);
UNSUPPORTED("31nofnynnq04ixi0v38p12n9p"); // 		    pf2 = bz.list[j + 2];
UNSUPPORTED("w4fsbg1khv9rq1z4zqjr6buj"); // 		    pf3 = bz.list[j + 3];
UNSUPPORTED("5pp5m5z0j3ifb4261labxynia"); // 		    offlist[j + 1] = offlist[j + 2] =
UNSUPPORTED("az662ecxsluy2yowrt3q6ek2p"); // 			computeoffset_qr(pf0, pf1, pf2, pf3, 2.0);
UNSUPPORTED("cbwbz3whkfb01jvescyn9k0sv"); // 		    /* initialize tmpspl to outermost position */
UNSUPPORTED("8wlvgs6gmjfrxnf217l61k9gs"); // 		    tmplist[j].x = pf0.x - numc2 * offlist[j].x;
UNSUPPORTED("88jzucdv8idpx03j2fewuvg1x"); // 		    tmplist[j].y = pf0.y - numc2 * offlist[j].y;
UNSUPPORTED("9xhxpretw3vq1bse37lv4cu0y"); // 		    tmplist[j + 1].x = pf1.x - numc2 * offlist[j + 1].x;
UNSUPPORTED("e3usjnf19ii4a29t9rtdtmfqy"); // 		    tmplist[j + 1].y = pf1.y - numc2 * offlist[j + 1].y;
UNSUPPORTED("a68d7jpzubul7pz15p6d036t0"); // 		    tmplist[j + 2].x = pf2.x - numc2 * offlist[j + 2].x;
UNSUPPORTED("9arrexwrbyqpj4ypwfyhjrtzg"); // 		    tmplist[j + 2].y = pf2.y - numc2 * offlist[j + 2].y;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("ed04mnw0pqbnqfyg4xzrkfzkt"); // 		/* last segment, no next pf1 */
UNSUPPORTED("gwh8gl1f8r7993j5qtnjb5du"); // 		offlist[j] = computeoffset_p(pf2, pf3, 2.0);
UNSUPPORTED("80gkd0c5b7k80ahlygpim1gmb"); // 		tmplist[j].x = pf3.x - numc2 * offlist[j].x;
UNSUPPORTED("3z599nt08uc3h1yma07b04aj3"); // 		tmplist[j].y = pf3.y - numc2 * offlist[j].y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4tu18v3pp0xuim6h8mg6d5acj"); // 	    lastcolor = headcolor = tailcolor = color;
UNSUPPORTED("e898dxfr4zmw4omwcbes9a6bp"); // 	    colors = strdup(color);
UNSUPPORTED("9c7ud1ls5bwt4gauc6v44g982"); // 	    for (cnum = 0, color = strtok(colors, ":"); color;
UNSUPPORTED("9ekqe3cr3rsir3piy9j737nk2"); // 		cnum++, color = strtok(0, ":")) {
UNSUPPORTED("anr2fzsu68eq5ho25s3n57ucq"); // 		if (!color[0])
UNSUPPORTED("axx7q4yy90hd2qya7l3hppd5c"); // 		    color = "black";
UNSUPPORTED("a2ducrn6b7qjjhh7f81totdqw"); // 		if (color != lastcolor) {
UNSUPPORTED("aabfth40rjblehad0r9zya7ci"); // 	            if (! (ED_gui_state(e) & ((1<<0) | (1<<1)))) {
UNSUPPORTED("aa6xnio2sxgn1omogw6y8xyri"); // 		        gvrender_set_pencolor(job, color);
UNSUPPORTED("cjpgya31i8in4mlytxcux25xk"); // 		        gvrender_set_fillcolor(job, color);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("e9aurhv58krtw8fa2qx3bd70y"); // 		    lastcolor = color;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("1ngzlxjzccbsg6ucsln5rvfy0"); // 		if (cnum == 0)
UNSUPPORTED("7hmj22ga66vzbca8w3syrk9f7"); // 		    headcolor = tailcolor = color;
UNSUPPORTED("4wn5san08kv94kkd7i7afxjhl"); // 		if (cnum == 1)
UNSUPPORTED("7oxd4vlfp5a45womyvouxm6d6"); // 		    tailcolor = color;
UNSUPPORTED("d7gdoq0jhg8b4ac0gf5kv9ivo"); // 		for (i = 0; i < tmpspl.size; i++) {
UNSUPPORTED("46p8d4gs7irf8q8np6w0dmvi2"); // 		    tmplist = tmpspl.list[i].list;
UNSUPPORTED("ew31zut96cgfu6j038wbwoeuh"); // 		    offlist = offspl.list[i].list;
UNSUPPORTED("5aahyg8gv7pvkfk4zigpruzu6"); // 		    for (j = 0; j < tmpspl.list[i].size; j++) {
UNSUPPORTED("428mqlpmorpkmzs7314jtfgo1"); // 			tmplist[j].x += offlist[j].x;
UNSUPPORTED("870cggpxew62biyq2myzh1be"); // 			tmplist[j].y += offlist[j].y;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("1n3ez6dffthnb7zqgi0dc571b"); // 		    gvrender_beziercurve(job, tmplist, tmpspl.list[i].size,
UNSUPPORTED("3tvyao2azw8uqezjar4gg12rj"); // 					 0, 0, 0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4ulz6ze3ok8dfrvqvzfe163do"); // 	    if (bz.sflag) {
UNSUPPORTED("cnl2vgj89lj652fz58jzw5fgr"); // 		if (color != tailcolor) {
UNSUPPORTED("61755uq447ue5aaxqvfbryhfo"); // 		    color = tailcolor;
UNSUPPORTED("aabfth40rjblehad0r9zya7ci"); // 	            if (! (ED_gui_state(e) & ((1<<0) | (1<<1)))) {
UNSUPPORTED("aa6xnio2sxgn1omogw6y8xyri"); // 		        gvrender_set_pencolor(job, color);
UNSUPPORTED("cjpgya31i8in4mlytxcux25xk"); // 		        gvrender_set_fillcolor(job, color);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("ankm9epmn1d51qf5040r6fpo3"); // 		arrow_gen(job, EMIT_TDRAW, bz.sp, bz.list[0],
UNSUPPORTED("4o0fkltlgsxx45rvoau5pvp16"); // 			arrowsize, penwidth, bz.sflag);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5t5bh9txkzxp6h1lozsf250ww"); // 	    if (bz.eflag) {
UNSUPPORTED("1bf269kx0rl5v6qpt9cbabezx"); // 		if (color != headcolor) {
UNSUPPORTED("91s08h9fw7wrj8oyt31cgu4or"); // 		    color = headcolor;
UNSUPPORTED("aabfth40rjblehad0r9zya7ci"); // 	            if (! (ED_gui_state(e) & ((1<<0) | (1<<1)))) {
UNSUPPORTED("aa6xnio2sxgn1omogw6y8xyri"); // 		        gvrender_set_pencolor(job, color);
UNSUPPORTED("cjpgya31i8in4mlytxcux25xk"); // 		        gvrender_set_fillcolor(job, color);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("77fdpd135fqcv7jprr2ac2k7b"); // 		arrow_gen(job, EMIT_HDRAW, bz.ep, bz.list[bz.size - 1],
UNSUPPORTED("3umh63h5q3be2s7ix5nvchvxa"); // 			arrowsize, penwidth, bz.eflag);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ein30zj4hern2nnkuo8pqpwqy"); // 	    free(colors);
UNSUPPORTED("7wvoxnl3dqatxwcvgzbrvp8lz"); // 	    for (i = 0; i < offspl.size; i++) {
UNSUPPORTED("1wjuw35ysxsboo3rdxm79nmfy"); // 		free(offspl.list[i].list);
UNSUPPORTED("60zkuby9v464su4bc563u74ts"); // 		free(tmpspl.list[i].list);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ewlz06ozbl7bfb9epkz1ox6mt"); // 	    free(offspl.list);
UNSUPPORTED("14byj4mkdb5aaeyd48wnrxku6"); // 	    free(tmpspl.list);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("8gqszi1k0mq14icjh1ot8vg53"); // 	    if (! (ED_gui_state(e) & ((1<<0) | (1<<1)))) {
UNSUPPORTED("5czy66vom4jkjwpkobz0tn2kv"); // 	        if (color[0]) {
UNSUPPORTED("bd0syu44e3lbokb2mhifc6xzx"); // 		    gvrender_set_pencolor(job, color);
UNSUPPORTED("sj6fn2hkr0i5p6gyntb8nbz7"); // 		    gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("3pv9j4qzifaa1yqzdsrv6crqk"); // 	        } else {
UNSUPPORTED("38cb36y8b8k7nyp8ou2ci224"); // 		    gvrender_set_pencolor(job, "black");
UNSUPPORTED("53s7vea1n8kkq2j04kqwf7f57"); // 		    if (fillcolor[0])
UNSUPPORTED("b3wipvue5h9yn4mwq4jdrvbqj"); // 			gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("3gfs0bkmty8cyqpr1hvhleit"); // 			gvrender_set_fillcolor(job, "black");
UNSUPPORTED("6o67xwzi6pf81mieipn703sxl"); // 	        }
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ah9ldbdg46psh3ic9qv1v1w1h"); // 	    for (i = 0; i < ED_spl(e)->size; i++) {
UNSUPPORTED("6uzqmcfjwogzxlwn3gsd5jdj"); // 		bz = ED_spl(e)->list[i];
UNSUPPORTED("4vmmlrhormhci854vjzy3xfp5"); // 		if (job->flags & (1<<14)) {
UNSUPPORTED("3fq5qfvm5itvhminu91gbm0xl"); // 		    gvrender_beziercurve(job, bz.list, bz.size, bz.sflag, bz.eflag, 0);
UNSUPPORTED("a47jqpic91ky93e1ohxv590l5"); // 		} else {
UNSUPPORTED("dpf7yejadgyhte1g6i690hdvh"); // 		    gvrender_beziercurve(job, bz.list, bz.size, 0, 0, 0);
UNSUPPORTED("1varqft14sst3gy9wlbwfxs2h"); // 		    if (bz.sflag) {
UNSUPPORTED("4kjbcxhhyztcuvz6tljmieqnn"); // 			arrow_gen(job, EMIT_TDRAW, bz.sp, bz.list[0],
UNSUPPORTED("4on3hu0dhyuvi03lazibxt3cb"); // 				arrowsize, penwidth, bz.sflag);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("1rw0tdjfpvfjrqqs2goiovglj"); // 		    if (bz.eflag) {
UNSUPPORTED("308rgxf2j9o0sn6ergiflga31"); // 			arrow_gen(job, EMIT_HDRAW, bz.ep, bz.list[bz.size - 1],
UNSUPPORTED("dbyip7sr13mvy3tsoe4o9i19t"); // 				arrowsize, penwidth, bz.eflag);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6k0z6eroe598hkmkh1ynpnanu"); // 		    if ((ED_spl(e)->size>1) && (bz.sflag||bz.eflag) && styles) 
UNSUPPORTED("44yezt0mg1ptjxgfbrz8rnq3d"); // 			gvrender_set_style(job, styles);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8clgbd8hcgw564ispajc9fzmg
// static boolean edge_in_box(edge_t *e, boxf b) 
public static Object edge_in_box(Object... arg) {
UNSUPPORTED("4ocf4su3fqdqvh06u5f0hltsn"); // static boolean edge_in_box(edge_t *e, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6tl9mepc2bett364jduh2q4mf"); //     splines *spl;
UNSUPPORTED("cqwl7s9yvzr8n5v8svuuv1a1q"); //     textlabel_t *lp;
UNSUPPORTED("2c3cg84bl0xam4mk6g5f31jj0"); //     spl = ED_spl(e);
UNSUPPORTED("7hvxias4hzevaqzopdofxqtf7"); //     if (spl && boxf_overlap(spl->bb, b))
UNSUPPORTED("anqbm3khxs8r22akkx8kcgbbx"); //         return NOT(0);
UNSUPPORTED("n9pj1f2ecz41q7q378oisbjt"); //     lp = ED_label(e);
UNSUPPORTED("26tbjilm0lmi9lsxtc9qh7qr"); //     if (lp && overlap_label(lp, b))
UNSUPPORTED("anqbm3khxs8r22akkx8kcgbbx"); //         return NOT(0);
UNSUPPORTED("16wq7hm2hmp8t60ogm2usvvsu"); //     lp = ED_xlabel(e);
UNSUPPORTED("erykqbn6tqjiaxzh5ob60z1f1"); //     if (lp && lp->set && overlap_label(lp, b))
UNSUPPORTED("anqbm3khxs8r22akkx8kcgbbx"); //         return NOT(0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cbl8pryorx02zjsmvgy70ve3p
// static void emit_begin_edge(GVJ_t * job, edge_t * e, char** styles) 
public static Object emit_begin_edge(Object... arg) {
UNSUPPORTED("2ig8s73qzrztk38xr6ky7zb11"); // static void emit_begin_edge(GVJ_t * job, edge_t * e, char** styles)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8lamppsqs7nhiu5v8k1f5jfh2"); //     obj_state_t *obj;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("161c0dnzyd7i9yp8msmk5m4uk"); //     textlabel_t *lab = NULL, *tlab = NULL, *hlab = NULL;
UNSUPPORTED("5nywj0gapcfc2dmrad685bp6p"); //     pointf *pbs = NULL;
UNSUPPORTED("cl4bevd50vcwmjkrgq0elpafi"); //     int	i, nump, *pbs_n = NULL, pbs_poly_n = 0;
UNSUPPORTED("3jo16cd9vt1kjc2upgxm9v4ro"); //     char* dflt_url = NULL;
UNSUPPORTED("c1xlgxbqyzghwddjio1795bot"); //     char* dflt_target = NULL;
UNSUPPORTED("75w3zx2oz7s1yf7arcxf48heo"); //     double penwidth;
UNSUPPORTED("1iexddadjo0w6fdgddatfx40s"); //     obj = push_obj_state(job);
UNSUPPORTED("b89ouigo5gftk9ug9cj99l2ev"); //     obj->type = EDGE_OBJTYPE;
UNSUPPORTED("8uahr3ivszgqh5671v8617mjq"); //     obj->u.e = e;
UNSUPPORTED("atedmjmxm7qoeolyi1wi7k1j4"); //     obj->emit_state = EMIT_EDRAW;
UNSUPPORTED("1db1hibhp0ubguecujfaj8wix"); //     /* We handle the edge style and penwidth here because the width
UNSUPPORTED("69ewknm6ydrnvfky1fo5ty9my"); //      * is needed below for calculating polygonal image maps
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("rrp11kv0zwuznrwfe4ka8ko0"); //     if (styles && ED_spl(e)) gvrender_set_style(job, styles);
UNSUPPORTED("6vy4pkkhruh7w9iykz7tfgi9g"); //     if (E_penwidth && ((s=agxget(e,E_penwidth)) && s[0])) {
UNSUPPORTED("bei9v8b4qq66l2v8kmz3bnuwb"); // 	penwidth = late_double(e, E_penwidth, 1.0, 0.0);
UNSUPPORTED("a85jezrt5nu63vkla8bslhzcx"); // 	gvrender_set_penwidth(job, penwidth);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("984vi925q5gu9ti0dn4c6h1pu"); //     if (flags & (1<<24)) {
UNSUPPORTED("aaozphuch8tw889utqbq1vdsl"); //         /* obj->tail_z = late_double(agtail(e), N_z, 0.0, -1000.0); */
UNSUPPORTED("1qb7gbxecops5b2y4ze6kkb3m"); //         /* obj->head_z = late_double(aghead(e), N_z, 0.0, -MAXFLOAT); */
UNSUPPORTED("d9qmjxnu6if6ms7ptj8jv1sou"); // 	if (GD_odim(agraphof(agtail(e))) >=3) {
UNSUPPORTED("apupbk890ov955gpnt1ywdg8x"); //             obj->tail_z = (ROUND((ND_pos(agtail(e))[2])*72));
UNSUPPORTED("7l2k0b1xoqey4kaneraonwuox"); //             obj->head_z = (ROUND((ND_pos(aghead(e))[2])*72));
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("e5r0sn9g2mp9ns4hrphra5h51"); //             obj->tail_z = obj->head_z = 0.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("23wil4rzu7y2cg0oxiahdthfo"); //     if (flags & (1<<15)) {
UNSUPPORTED("1vaujzmkgkvzg9r3ws72hd0sn"); // 	if ((lab = ED_label(e)))
UNSUPPORTED("10gcpuuqg5ziec0pp9kyhlbqb"); // 	    obj->label = lab->text;
UNSUPPORTED("18olw94kq4sb382d1xowaqmex"); // 	obj->taillabel = obj->headlabel = obj->xlabel = obj->label;
UNSUPPORTED("2hqbgo1ml83utbsvik1j8i2us"); // 	if ((tlab = ED_xlabel(e)))
UNSUPPORTED("4lnx8sli5qlwwkc6jf7fct4rl"); // 	    obj->xlabel = tlab->text;
UNSUPPORTED("ach6hnpzids5tf6isfd5kmv6k"); // 	if ((tlab = ED_tail_label(e)))
UNSUPPORTED("e7gzimwa7y8dtdogbme4beiz1"); // 	    obj->taillabel = tlab->text;
UNSUPPORTED("9i7k1tgfaw9tmwoxsq3mqygz7"); // 	if ((hlab = ED_head_label(e)))
UNSUPPORTED("dp7pv3xtw76x8tv19cch23xnp"); // 	    obj->headlabel = hlab->text;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ekhzvq8l2u2frs2tl01cuf71s"); //     if (flags & (1<<16)) {
UNSUPPORTED("8moktqylyfg7421xon31c77cc"); // 	agxbuf xb;
UNSUPPORTED("2m3h7h7zkzx4aix1gvfd54itd"); // 	unsigned char xbuf[128];
UNSUPPORTED("6dfmabcsvvsbizc4h30vlmcv0"); // 	agxbinit(&xb, 128, xbuf);
UNSUPPORTED("cxt18v48tfjjupb58w42yijkl"); // 	s = getObjId (job, e, &xb);
UNSUPPORTED("20n0stvu1ku0ueoyhntfblzwk"); // 	obj->id = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("52frst03mcbsuu6bmnqs962qm"); // 	agxbfree(&xb);
UNSUPPORTED("1671vg2ii28nl1884z9by98oj"); //         if (((s = agget(e, "href")) && s[0]) || ((s = agget(e, "URL")) && s[0]))
UNSUPPORTED("d91p689ronm5sfp1t1zp8a9lg"); //             dflt_url = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("14h7yzdms65e8u39cm8u9j09d"); // 	if (((s = agget(e, "edgehref")) && s[0]) || ((s = agget(e, "edgeURL")) && s[0]))
UNSUPPORTED("7tbdqrc1yfgnnqv43758e83ox"); //             obj->url = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("95kpmzfgantzkuuqda3257e1w"); // 	else if (dflt_url)
UNSUPPORTED("bpb2jomy0ylc3sny2qpqs5ow0"); // 	    obj->url = strdup(dflt_url);
UNSUPPORTED("4px9z3013lzzxp1y25an6kq60"); // 	if (((s = agget(e, "labelhref")) && s[0]) || ((s = agget(e, "labelURL")) && s[0]))
UNSUPPORTED("at75pf09xegtmpfpxuho1ez3a"); //             obj->labelurl = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("95kpmzfgantzkuuqda3257e1w"); // 	else if (dflt_url)
UNSUPPORTED("97vequ01i369l44p14efuqvhm"); // 	    obj->labelurl = strdup(dflt_url);
UNSUPPORTED("9iu3uo079ib1p40uh1yasbvqm"); // 	if (((s = agget(e, "tailhref")) && s[0]) || ((s = agget(e, "tailURL")) && s[0])) {
UNSUPPORTED("ytymloen95ie2kurds6mbjqg"); //             obj->tailurl = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("7hm7tuu8dsjj1pui6g0tc1k6f"); //             obj->explicit_tailurl = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("95kpmzfgantzkuuqda3257e1w"); // 	else if (dflt_url)
UNSUPPORTED("47aatd2rv5gwkpo89wjbtk0h1"); // 	    obj->tailurl = strdup(dflt_url);
UNSUPPORTED("7nxel42t6oq08r8gct4rsxf7v"); // 	if (((s = agget(e, "headhref")) && s[0]) || ((s = agget(e, "headURL")) && s[0])) {
UNSUPPORTED("7gtgsooxqvcu6l1puh1h4eo7b"); //             obj->headurl = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("asli1axdl9etovuah6pxmmee5"); //             obj->explicit_headurl = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("95kpmzfgantzkuuqda3257e1w"); // 	else if (dflt_url)
UNSUPPORTED("4pmt43kqcvoumkzh5z4w2t1vf"); // 	    obj->headurl = strdup(dflt_url);
UNSUPPORTED("3xbu5lb3fe5zgccq9vbjk3kln"); //     } 
UNSUPPORTED("akp8i12zqhzyti5072dctwx4z"); //     if (flags & (1<<23)) {
UNSUPPORTED("6863y1q9ojbbbx8f4e3vwlen2"); //         if ((s = agget(e, "target")) && s[0])
UNSUPPORTED("6e7ucuyioknvvz9i86kctjo8f"); //             dflt_target = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("9ybfu42vnta17eg18hledoxk7"); //         if ((s = agget(e, "edgetarget")) && s[0]) {
UNSUPPORTED("doh5a8birowwtmsp8f8982lle"); // 	    obj->explicit_edgetarget = NOT(0);
UNSUPPORTED("4z44bpd7qea5mlut410evqsif"); //             obj->target = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ausveuroy93s2sdgviv4p0lcv"); // 	else if (dflt_target)
UNSUPPORTED("a1ke8xqvu9o082268ksexzgny"); // 	    obj->target = strdup(dflt_target);
UNSUPPORTED("bpe0irgpt9end7gtltjdokxpy"); //         if ((s = agget(e, "labeltarget")) && s[0])
UNSUPPORTED("bzn9htm8cci6c7dde9k91l83s"); //             obj->labeltarget = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("ausveuroy93s2sdgviv4p0lcv"); // 	else if (dflt_target)
UNSUPPORTED("ar607cwjyspcv574196uxzkjc"); // 	    obj->labeltarget = strdup(dflt_target);
UNSUPPORTED("5rdzxxesadfpf6txv5t01ozuj"); //         if ((s = agget(e, "tailtarget")) && s[0]) {
UNSUPPORTED("4w54a911j5thxlajsmya0lxqq"); //             obj->tailtarget = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("85s8gvyt4vepi64xcje31iek8"); // 	    obj->explicit_tailtarget = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ausveuroy93s2sdgviv4p0lcv"); // 	else if (dflt_target)
UNSUPPORTED("37o9c6wmbsdgi78jeu7kisvbu"); // 	    obj->tailtarget = strdup(dflt_target);
UNSUPPORTED("9h2r9gbx0nfsnhwvo6rkbrx9i"); //         if ((s = agget(e, "headtarget")) && s[0]) {
UNSUPPORTED("4zxyzeqp8h430tyb6zjdn4sd6"); // 	    obj->explicit_headtarget = NOT(0);
UNSUPPORTED("7r8xkvzh66pda9mg5skownrld"); //             obj->headtarget = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ausveuroy93s2sdgviv4p0lcv"); // 	else if (dflt_target)
UNSUPPORTED("dr88cbxj5srk1ojowv9swn9pr"); // 	    obj->headtarget = strdup(dflt_target);
UNSUPPORTED("3xbu5lb3fe5zgccq9vbjk3kln"); //     } 
UNSUPPORTED("ep9t9tvrtouwjd7pulp378fzi"); //     if (flags & (1<<22)) {
UNSUPPORTED("8b522c1xkaxbe7orhmgdpw7wo"); //         if (((s = agget(e, "tooltip")) && s[0]) ||
UNSUPPORTED("epyuvpbf4py1ueht2htfx9wrg"); //             ((s = agget(e, "edgetooltip")) && s[0])) {
UNSUPPORTED("8mqt0zsk0c9lp05pgm5t2c6z7"); //             obj->tooltip = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("diwiuhgudo706youzbhxsmajm"); // 	    obj->explicit_tooltip = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("aqmnkkhtx4an4uhckaewhcgmp"); // 	else if (obj->label)
UNSUPPORTED("4s32lfe96j6y84uateu8as1g0"); // 	    obj->tooltip = strdup(obj->label);
UNSUPPORTED("c5kcbti582hfnwvehr32n2va7"); //         if ((s = agget(e, "labeltooltip")) && s[0]) {
UNSUPPORTED("bhdlgf7cc5ykmujysafzspf2m"); //             obj->labeltooltip = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("npeafaiazv07hkrit692subv"); // 	    obj->explicit_labeltooltip = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("aqmnkkhtx4an4uhckaewhcgmp"); // 	else if (obj->label)
UNSUPPORTED("2hl1g7eri9qv82oy9sfyzt81j"); // 	    obj->labeltooltip = strdup(obj->label);
UNSUPPORTED("6voynh2r7ssu3ixda4lxxkair"); //         if ((s = agget(e, "tailtooltip")) && s[0]) {
UNSUPPORTED("du6ndrua8ofzqrdrl93za73fy"); //             obj->tailtooltip = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("c8tog5rk6xyjayycyb3lrvv1z"); // 	    obj->explicit_tailtooltip = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2ztxqs1s2cxhj4vgiol21jz63"); // 	else if (obj->taillabel)
UNSUPPORTED("8oa1ltfyq9fydfsizol8ewimo"); // 	    obj->tailtooltip = strdup(obj->taillabel);
UNSUPPORTED("cl0wrp7bp2cgxks07e64qiuqf"); //         if ((s = agget(e, "headtooltip")) && s[0]) {
UNSUPPORTED("5phljruko4leqqbs5bppxgsj"); //             obj->headtooltip = strdup_and_subst_obj(s, (void*)e);
UNSUPPORTED("eq1aukj58yixydy8kpfo7z1yi"); // 	    obj->explicit_headtooltip = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9roz9l9toa612u73rurlji9l4"); // 	else if (obj->headlabel)
UNSUPPORTED("24l5dbncepggtecq9rngaod0d"); // 	    obj->headtooltip = strdup(obj->headlabel);
UNSUPPORTED("3xbu5lb3fe5zgccq9vbjk3kln"); //     } 
UNSUPPORTED("2mg64zxg17fewxae9xmwwaa0m"); //     free (dflt_url);
UNSUPPORTED("6w5fxao8mlryxwk1mz7ksvbce"); //     free (dflt_target);
UNSUPPORTED("6ic0ku7wr32jsf5j0pwkrzq8j"); //     if (flags & ((1<<16) | (1<<22))) {
UNSUPPORTED("5ndyf9os0xxk8bsbjd2c2scjp"); // 	if (ED_spl(e) && (obj->url || obj->tooltip) && (flags & (1<<19))) {
UNSUPPORTED("gq85b3oml4t5u52mslbiohm4"); // 	    int ns;
UNSUPPORTED("bapn9032dhdk62b51i2ut3i7t"); // 	    splines *spl;
UNSUPPORTED("3wvt6ajtp2z5ux7yhf3i1uu51"); // 	    double w2 = MAX(job->obj->penwidth/2.0,2.0);
UNSUPPORTED("74rl229xn6mlrx2uv5gby9xoi"); // 	    spl = ED_spl(e);
UNSUPPORTED("borrlfudm59nvnwsmvvccimug"); // 	    ns = spl->size; /* number of splines */
UNSUPPORTED("bwf639msooalzn11a63mrvdh0"); // 	    for (i = 0; i < ns; i++)
UNSUPPORTED("9ta5hkbutugjx9htzj3lq1pav"); // 		map_output_bspline (&pbs, &pbs_n, &pbs_poly_n, spl->list+i, w2);
UNSUPPORTED("3l39oh2gmkeoyrfm0yzl8m4d4"); // 	    obj->url_bsplinemap_poly_n = pbs_poly_n;
UNSUPPORTED("co64ewdmi3b7x6t2h8cp9hkvg"); // 	    obj->url_bsplinemap_n = pbs_n;
UNSUPPORTED("6pz9l8cp367kjl3asc6hiwnpz"); // 	    if (! (flags & (1<<13))) {
UNSUPPORTED("9zfwixrcbys3l8gx4ol8980op"); //     		for ( nump = 0, i = 0; i < pbs_poly_n; i++)
UNSUPPORTED("a4l7ie3p7q35rrj0oglmisn5t"); //         	    nump += pbs_n[i];
UNSUPPORTED("7v0e42759wdkgsyz70af06mms"); // 		gvrender_ptf_A(job, pbs, pbs, nump);		
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4o2ao6pcwmplab6n21deqwps9"); // 	    obj->url_bsplinemap_p = pbs;
UNSUPPORTED("5k8t4lz63jq26u2xqeoskhen7"); // 	    obj->url_map_shape = MAP_POLYGON;
UNSUPPORTED("1mpert8m5p683pcg0qyjgzj1"); // 	    obj->url_map_p = pbs;
UNSUPPORTED("84gohar4j2iw17h4sy0oehdhy"); // 	    obj->url_map_n = pbs_n[0];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("22f78nyasgzgcunmxhflea4ug"); //     gvrender_begin_edge(job, e);
UNSUPPORTED("spdml5d3q3jza61kjh4zxw31"); //     if (obj->url || obj->explicit_tooltip)
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("av1tl9edbbm94up94c6rfx2tc"); // 		obj->url, obj->tooltip, obj->target, obj->id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 29djyajtv7sgp9x7yjc7qzabo
// static void emit_edge_label(GVJ_t* job, textlabel_t* lbl, emit_state_t lkind, int explicit,     char* url, char* tooltip, char* target, char *id, splines* spl) 
public static Object emit_edge_label(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("35xyp233i430kqmc9ujvqett4"); // emit_edge_label(GVJ_t* job, textlabel_t* lbl, emit_state_t lkind, int explicit,
UNSUPPORTED("en7wsvah9njefedha3fj9icwa"); //     char* url, char* tooltip, char* target, char *id, splines* spl)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("ecr1y7qy0ikxkidkdfvwv88ir"); //     emit_state_t old_emit_state;
UNSUPPORTED("6w3ygcd4u5ovgqghawjtzd4l8"); //     char* newid;
UNSUPPORTED("98v0vg88ycqpkpyg1kritbah0"); //     char* type;
UNSUPPORTED("3sg0pzcyo2xi4xr9utypik0di"); //     if ((lbl == NULL) || !(lbl->set)) return;
UNSUPPORTED("bt2nnyzwpzxnzhhrsjv03du9j"); //     if (id) { /* non-NULL if needed */
UNSUPPORTED("6o6r1v1dpl86m94m7l5omr00u"); // 	newid = (char*)zmalloc((strlen(id) + sizeof("-headlabel"))*sizeof(char));
UNSUPPORTED("9queigxdufzgkqvtlnulkij8z"); // 	switch (lkind) {
UNSUPPORTED("4dur34ux7rmaxikyxa4xy6mt3"); // 	case EMIT_ELABEL :
UNSUPPORTED("5yejllfmasus697zyh9icg23m"); // 	    type = "label";
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("ckacb8gd8ihcl655tak5p48xx"); // 	case EMIT_HLABEL :
UNSUPPORTED("60jcdu5mykv7nj9c62dt74vbg"); // 	    type = "headlabel";
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8j00sch2b081jzzzib4soebcu"); // 	case EMIT_TLABEL :
UNSUPPORTED("erdib8kle1lcl2if7uf5rx8xs"); // 	    type = "taillabel";
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5ujjs4gho9mjjupbibyqyplxp"); // 	default :
UNSUPPORTED("9rml26jwxfvc1i0eeqsucqrl7"); // 	    assert (0);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("etlt5rbiatgk1pin7kk30ll2x"); // 	sprintf (newid, "%s-%s", id, type);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("5qoplxw5vuo9t9b18i6ee7ime"); // 	newid = NULL;
UNSUPPORTED("ekjwoxwz3y39zx2i2q6310l1u"); //     old_emit_state = job->obj->emit_state;
UNSUPPORTED("d5oaybfzozrw1bnvrdisv5yxc"); //     job->obj->emit_state = lkind;
UNSUPPORTED("c2j2m5w18gb9vht8elcpe4ce1"); //     if ((url || explicit) && !(flags & (1<<2))) {
UNSUPPORTED("8as81xybsx2lbhatoq5dfka46"); // 	map_label(job, lbl);
UNSUPPORTED("b1r5s44l517bwmgj4kvnf5pth"); // 	gvrender_begin_anchor(job, url, tooltip, target, newid);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6n40fbgjky821o8xqbk4ke6js"); //     emit_label(job, lkind, lbl);
UNSUPPORTED("as20j8imvr5tdwziovn1yy9w9"); //     if (spl) emit_attachment(job, lbl, spl);
UNSUPPORTED("buoc16exkpqacfj0vige0c8cs"); //     if (url || explicit) {
UNSUPPORTED("8z7a7lmqaytt0za0fb8kavivm"); // 	if (flags & (1<<2)) {
UNSUPPORTED("7jrzhi6w0txc5ge27adx7n1fg"); // 	    map_label(job, lbl);
UNSUPPORTED("5ygb95yq4xmsdotqmdr3vj3v5"); // 	    gvrender_begin_anchor(job, url, tooltip, target, newid);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4ml3jp9auhgd67u12ys1dysmz"); //     if (newid) free (newid);
UNSUPPORTED("e0dze8hjyg2dby274irdlx48q"); //     job->obj->emit_state = old_emit_state;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 awxicbypnp5lexym1y3ymowey
// static void nodeIntersect (GVJ_t * job, pointf p,      boolean explicit_iurl, char* iurl,     boolean explicit_itooltip, char* itooltip,     boolean explicit_itarget, char* itarget) 
public static Object nodeIntersect(Object... arg) {
UNSUPPORTED("cmhcypje3hntq13rzpbnk7ors"); // static void nodeIntersect (GVJ_t * job, pointf p, 
UNSUPPORTED("3oh5ubi4nhyilnxnj6o43m9jg"); //     boolean explicit_iurl, char* iurl,
UNSUPPORTED("1l6n135od7wjrab69cp2rq4as"); //     boolean explicit_itooltip, char* itooltip,
UNSUPPORTED("a9o31jy22twcezb78yp27lex3"); //     boolean explicit_itarget, char* itarget)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("a5d87vmbdhktxeng57v2hzdid"); //     char* url;
UNSUPPORTED("836sn3dd0sjadnx46ehd0ojan"); //     char* tooltip;
UNSUPPORTED("9zm2viiigdykrikvq2wxotz4t"); //     char* target;
UNSUPPORTED("5mdh3lx400yozcu82r1yqq8fs"); //     boolean explicit;
UNSUPPORTED("70p390q4p8ly00fs0tu1ou5lc"); //     if (explicit_iurl) url = iurl;
UNSUPPORTED("2qo54smwvn3zpz62uwalydr4e"); //     else url = obj->url;
UNSUPPORTED("elyxsmzru5je5et9mzf7xln9e"); //     if (explicit_itooltip) {
UNSUPPORTED("bih7mdnro80d1ycl5mduylzjv"); // 	tooltip = itooltip;
UNSUPPORTED("b8xy4j8mzbef8wy4u4f0e928z"); // 	explicit = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3njq6nu65dp353iz31nlymjhr"); //     else if (obj->explicit_tooltip) {
UNSUPPORTED("bqwxu32cunjm0lxj3oi5zlr4s"); // 	tooltip = obj->tooltip;
UNSUPPORTED("b8xy4j8mzbef8wy4u4f0e928z"); // 	explicit = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("bj1n4exyidlxkszug92q5e8pm"); // 	explicit = 0;
UNSUPPORTED("bih7mdnro80d1ycl5mduylzjv"); // 	tooltip = itooltip;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("68e6sqdkexks56m61b36z4jam"); //     if (explicit_itarget)
UNSUPPORTED("ezrw5qg1aplvs9qzwam68j5uq"); // 	target = itarget;
UNSUPPORTED("16b0s2q3bcb5cqp3fd3ga7wor"); //     else if (obj->explicit_edgetarget)
UNSUPPORTED("e9a53dgfxmu20y35wg7ben6gm"); // 	target = obj->target;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("ezrw5qg1aplvs9qzwam68j5uq"); // 	target = itarget;
UNSUPPORTED("buoc16exkpqacfj0vige0c8cs"); //     if (url || explicit) {
UNSUPPORTED("4hpmlshabmpme6e8axio0u6tm"); // 	map_point(job, p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ywemz3ws8gm7t2jkezm9zl44
// static void emit_end_edge(GVJ_t * job) 
public static Object emit_end_edge(Object... arg) {
UNSUPPORTED("1dduzu2v1v5eoimgn6wl3pq1s"); // static void emit_end_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("4qb9sxge2bkx34r71ezj0knk2"); //     edge_t *e = obj->u.e;
UNSUPPORTED("avhbw4mqc8i7mhe1mi06nj1fv"); //     int i, nump;
UNSUPPORTED("ebrzd2bubs5trqandzsfjpugj"); //     if (obj->url || obj->explicit_tooltip) {
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dmr093y6wa3ax73y54vvo29lj"); // 	if (obj->url_bsplinemap_poly_n) {
UNSUPPORTED("8j5y8urq29one6yb4kfmzna3f"); // 	    for ( nump = obj->url_bsplinemap_n[0], i = 1; i < obj->url_bsplinemap_poly_n; i++) {
UNSUPPORTED("9xsziog2rrr4vw3x27e2ndqjp"); // 		/* additional polygon maps around remaining bezier pieces */
UNSUPPORTED("5w8ld66x8j20bfg4bhnrzgnno"); // 		obj->url_map_n = obj->url_bsplinemap_n[i];
UNSUPPORTED("9ka5hm7zu6chhinb1q0vf2z4v"); // 		obj->url_map_p = &(obj->url_bsplinemap_p[nump]);
UNSUPPORTED("5xf0jq48hur62ull7qfz6hvx9"); // 		gvrender_begin_anchor(job,
UNSUPPORTED("blnuziy4xllrn7u5trd1fnls8"); // 			obj->url, obj->tooltip, obj->target, obj->id);
UNSUPPORTED("9komgvtfublw0fuwcn56n60nb"); // 		gvrender_end_anchor(job);
UNSUPPORTED("c9dwtox5h6ixy1w83w397g2kj"); // 		nump += obj->url_bsplinemap_n[i];
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9pmuzaizzoydjgbkmuor040f6"); //     obj->url_map_n = 0;       /* null out copy so that it doesn't get freed twice */
UNSUPPORTED("8cqdsgr453qauw78nltd5c4tx"); //     obj->url_map_p = NULL;
UNSUPPORTED("4pdkymy0wuxj1yn9xqqzp2x4h"); //     if (ED_spl(e)) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("7jumvon0fvx7rozmib9zg46pn"); // 	bezier bz;
UNSUPPORTED("66ycb012574x3aksn28ibhxd5"); // 	/* process intersection with tail node */
UNSUPPORTED("7waxmjg8e70tet8to37przecd"); // 	bz = ED_spl(e)->list[0];
UNSUPPORTED("jksehajhc4wys677xg3wbbqp"); // 	if (bz.sflag) /* Arrow at start of splines */
UNSUPPORTED("kxn3waz2mvbtl0y7qgit84qy"); // 	    p = bz.sp;
UNSUPPORTED("7162vc0qtrox86ru0t0edndpq"); // 	else /* No arrow at start of splines */
UNSUPPORTED("6vhfnfcnxq7pk1ylvgqsbhisf"); // 	    p = bz.list[0];
UNSUPPORTED("3he9xp2xirwwauclzoba24j8h"); // 	nodeIntersect (job, p, obj->explicit_tailurl, obj->tailurl,
UNSUPPORTED("digi7elarmtdncyct8hntl9k6"); // 	    obj->explicit_tailtooltip, obj->tailtooltip, 
UNSUPPORTED("bjqpxn7ux8x0z0psos6lg97at"); // 	    obj->explicit_tailtarget, obj->tailtarget); 
UNSUPPORTED("1eo0iyrg7y6hw7gaa6t4rckhw"); // 	/* process intersection with head node */
UNSUPPORTED("1z1ljfme78h5hs2nhpnh0c1fi"); // 	bz = ED_spl(e)->list[ED_spl(e)->size - 1];
UNSUPPORTED("eg9okxorf20k4z98ukgoxo3n7"); // 	if (bz.eflag) /* Arrow at end of splines */
UNSUPPORTED("ehclpn3uuogubck9azlh1vpas"); // 	    p = bz.ep;
UNSUPPORTED("9vqsg7ptbtw9uq9csktycrpd1"); // 	else /* No arrow at end of splines */
UNSUPPORTED("a3eqfcdajd48yebxdzxtmmfoi"); // 	    p = bz.list[bz.size - 1];
UNSUPPORTED("90oe5mif8a78ifcvx4ihiqnap"); // 	nodeIntersect (job, p, obj->explicit_headurl, obj->headurl,
UNSUPPORTED("47gawnzk5qaei40hp6wyzmccl"); // 	    obj->explicit_headtooltip, obj->headtooltip, 
UNSUPPORTED("eovhrgd0p19e5n9en1fyyikgc"); // 	    obj->explicit_headtarget, obj->headtarget); 
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3myme3h0w55rj1gxrei3sza4u"); //     emit_edge_label(job, ED_label(e), EMIT_ELABEL,
UNSUPPORTED("3plv0n79k76me9i1hjgw53uf"); // 	obj->explicit_labeltooltip, 
UNSUPPORTED("aaks2xubvv971c3n3lkewm11l"); // 	obj->labelurl, obj->labeltooltip, obj->labeltarget, obj->id, 
UNSUPPORTED("agvl7gmqjq08dcsq6in3idrlh"); // 	((mapbool(late_string(e, E_decorate, "false")) && ED_spl(e)) ? ED_spl(e) : 0));
UNSUPPORTED("5s1nsti98iov2qpm95devpckw"); //     emit_edge_label(job, ED_xlabel(e), EMIT_ELABEL,
UNSUPPORTED("3plv0n79k76me9i1hjgw53uf"); // 	obj->explicit_labeltooltip, 
UNSUPPORTED("aaks2xubvv971c3n3lkewm11l"); // 	obj->labelurl, obj->labeltooltip, obj->labeltarget, obj->id, 
UNSUPPORTED("agvl7gmqjq08dcsq6in3idrlh"); // 	((mapbool(late_string(e, E_decorate, "false")) && ED_spl(e)) ? ED_spl(e) : 0));
UNSUPPORTED("du836h1rt0jh9ud6q2syf0ym8"); //     emit_edge_label(job, ED_head_label(e), EMIT_HLABEL, 
UNSUPPORTED("1pdnwlknuz9w3gkir3l8o1yro"); // 	obj->explicit_headtooltip,
UNSUPPORTED("8bd82h05ko2pwessxq68kaxai"); // 	obj->headurl, obj->headtooltip, obj->headtarget, obj->id,
UNSUPPORTED("ai3ojmcrmb5sj7kn23byccggm"); // 	0);
UNSUPPORTED("bh63wcy1dti8ji145r5ghesje"); //     emit_edge_label(job, ED_tail_label(e), EMIT_TLABEL, 
UNSUPPORTED("9y21fl5ek5f4g5ebjoixzr4gv"); // 	obj->explicit_tailtooltip,
UNSUPPORTED("b1470sa0rpd26d4t30nidwv8"); // 	obj->tailurl, obj->tailtooltip, obj->tailtarget, obj->id,
UNSUPPORTED("ai3ojmcrmb5sj7kn23byccggm"); // 	0);
UNSUPPORTED("9sm494bjst3m8frvi02esqjkm"); //     gvrender_end_edge(job);
UNSUPPORTED("39iamwq9cd9iv3d2iyiaq8gz9"); //     pop_obj_state(job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e552br6jk0jigqmq1c9d2x0fy
// static void emit_edge(GVJ_t * job, edge_t * e) 
public static Object emit_edge(Object... arg) {
UNSUPPORTED("701wlpnv3kz0k3hxn7zqrhbqx"); // static void emit_edge(GVJ_t * job, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("byo308l1rmve5rmx8wt32juux"); //     char *style;
UNSUPPORTED("getfykrvugvlv3wxt5qm5ghl"); //     char **styles = 0;
UNSUPPORTED("9q7vvjxznd6x0u1t6fgd82byj"); //     char **sp;
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("6govt2ekuiy0vzlv3yv2uu723"); //     if (edge_in_box(e, job->clip) && edge_in_layer(job, agraphof(aghead(e)), e) ) {
UNSUPPORTED("40dvidospika9vlp9ti3h4kbk"); // 	s = malloc(strlen(agnameof(agtail(e))) + 2 + strlen(agnameof(aghead(e))) + 1);
UNSUPPORTED("aanr456304klecsfh7bdhjuqs"); // 	strcpy(s,agnameof(agtail(e)));
UNSUPPORTED("90k8dynebdy5ezb8ysfakj1bq"); // 	if (agisdirected(agraphof(aghead(e))))
UNSUPPORTED("6t25g7w3win3lim8fgtsen7d0"); // 	    strcat(s,"->");
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("8kwffycoj7ji16shumcvo0z2w"); // 	    strcat(s,"--");
UNSUPPORTED("9qi68sa2442hrlpp7zizf86w2"); // 	strcat(s,agnameof(aghead(e)));
UNSUPPORTED("9tyf9lnhx38ny94z7gx5gkioi"); // 	gvrender_comment(job, s);
UNSUPPORTED("d285xdylp7oj81aar06ulc7kh"); // 	free(s);
UNSUPPORTED("dzzea54yfhrfp3ko6rqi4h8na"); // 	s = late_string(e, E_comment, "");
UNSUPPORTED("7oyyy6d3itm6qqhr3p5zu6ded"); // 	if (s[0])
UNSUPPORTED("8gbf99sx0atz9ku0tfjv4m4o3"); // 	    gvrender_comment(job, s);
UNSUPPORTED("9ue98xnv5lygucjpcp6ugmkjr"); // 	style = late_string(e, E_style, "");
UNSUPPORTED("c6nyyirbpagqf6tsr8p6b3xw9"); // 	/* We shortcircuit drawing an invisible edge because the arrowhead
UNSUPPORTED("4oynhdibr581vvritt0c2m0zi"); // 	 * code resets the style to solid, and most of the code generators
UNSUPPORTED("b151tzgt2xkvtkf487juwpl32"); // 	 * (except PostScript) won't honor a previous style of invis.
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("12ihr78gv09dxppuorymkgt75"); // 	if (style[0]) {
UNSUPPORTED("32d3t9w0x6lbw2olmibm97mqd"); // 	    styles = parse_style(style);
UNSUPPORTED("ai2h90fa4b5ss40yyc0ehvrzd"); // 	    sp = styles;
UNSUPPORTED("9v2c8p99l7pj3zqwvtb298adq"); // 	    while ((p = *sp++)) {
UNSUPPORTED("xtuts27rjtqvzh4gjkw96ime"); // 		if ((*(p)==*("invis")&&!strcmp(p,"invis"))) return;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9hsqvthyp1sj37t7lu10n1luq"); // 	emit_begin_edge(job, e, styles);
UNSUPPORTED("cchqw8nozlblfh2ekimz3q7ye"); // 	emit_edge_graphics (job, e, styles);
UNSUPPORTED("6fu8546pxs99njakbk5cl5q31"); // 	emit_end_edge(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 dacilfxr6a3uba0ks0btnjoms
// static char adjust[] = 




//3 crlphx0gbhlhrn2w2jq92mgxq
// static void expandBB (boxf* bb, pointf p) 
public static Object expandBB(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3lpbo3h0n97pkcx19mleg4zy3"); // expandBB (boxf* bb, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("82qwewr47nk4i4uoe835qwx7m"); //     if (p.x > bb->UR.x)
UNSUPPORTED("6g9fswlylro0l373mk5r6hrf2"); // 	bb->UR.x = p.x;
UNSUPPORTED("42h6ln67gjb305nianf5cqtwb"); //     if (p.x < bb->LL.x)
UNSUPPORTED("aecuyren82eu4q6khc57o37fm"); // 	bb->LL.x = p.x;
UNSUPPORTED("5sb5u1ns40tj304k0u2ab1at7"); //     if (p.y > bb->UR.y)
UNSUPPORTED("759apld02jf160x6shlbl6vh0"); // 	bb->UR.y = p.y;
UNSUPPORTED("6kqd1pni8dvsj92cyx40pg7yq"); //     if (p.y < bb->LL.y)
UNSUPPORTED("196dcdn12h9rrk3ut2ar5byra"); // 	bb->LL.y = p.y;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9fdfpbfftpegojteq4f4zkh3t
// static boxf ptsBB (xdot_point* inpts, int numpts, boxf* bb) 
public static Object ptsBB(Object... arg) {
UNSUPPORTED("d5qt6s97burjfu5qe0oxyyrmr"); // static boxf
UNSUPPORTED("6t79m8rz3g0dlgf125e80z567"); // ptsBB (xdot_point* inpts, int numpts, boxf* bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1sc1s00b2mxqj9p5pa0w4t375"); //     boxf opbb;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("9io5mvwev5td74rtkwnm8o3s2"); //     opbb.LL.x = opbb.UR.x = inpts->x;
UNSUPPORTED("4cwi4xd54tfe64lw7iegazb8u"); //     opbb.LL.y = opbb.UR.y = inpts->y;
UNSUPPORTED("79on93mttov53oge4afx7vsuc"); //     for (i = 1; i < numpts; i++) {
UNSUPPORTED("3ft3l1b2jpm88evrrzvlmn8e1"); // 	inpts++;
UNSUPPORTED("1jkw7uv3v8c3h4n5wz0vz7ug7"); // 	if (inpts->x < opbb.LL.x)
UNSUPPORTED("8heqoomogx0vbm9c6b353nauh"); // 	    opbb.LL.x = inpts->x;
UNSUPPORTED("ctlx14otfvxc1g2po12swqr5"); // 	else if (inpts->x > opbb.UR.x)
UNSUPPORTED("cm99tuzk0ha0i27620s0ug6qm"); // 	    opbb.UR.x = inpts->x;
UNSUPPORTED("cbdkr3fvabha45e8zix3wm0ra"); // 	if (inpts->y < opbb.LL.y)
UNSUPPORTED("cfi2hy5ra04w2nvwbbeif8iwu"); // 	    opbb.LL.y = inpts->y;
UNSUPPORTED("7i89xmuxwx6aon3vbdt6v5whc"); // 	else if (inpts->y > opbb.UR.y)
UNSUPPORTED("7ssr5coatcfnn57zucbgjlgfc"); // 	    opbb.UR.y = inpts->y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("vnhtdhcqg8xtzofq2hgzmd74"); //     expandBB (bb, opbb.LL);
UNSUPPORTED("17t235kr79n3w5l1c3anpgwt5"); //     expandBB (bb, opbb.UR);
UNSUPPORTED("cbril871fhn70vo8av8mkgt6q"); //     return opbb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9pymyll8xo6gubyh1lffjkfam
// static boxf textBB (double x, double y, textspan_t* span) 
public static Object textBB(Object... arg) {
UNSUPPORTED("d5qt6s97burjfu5qe0oxyyrmr"); // static boxf
UNSUPPORTED("ay5ce8utx9ld287zwnwbctqjj"); // textBB (double x, double y, textspan_t* span)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("9uvgf08lwlxzhybxua7r75tev"); //     pointf sz = span->size;
UNSUPPORTED("bpqxh9mig0sh1gasrlkg6hbph"); //     switch (span->just) {
UNSUPPORTED("15tf5rbprgr65ucp24e4bba9t"); //     case 'l':
UNSUPPORTED("3ehk6wetysrhmdleh0z9s6zjz"); // 	bb.LL.x = x;
UNSUPPORTED("c1lsp9mt50b64uby7n2an805t"); // 	bb.UR.x = bb.LL.x + sz.x;
UNSUPPORTED("6qvptoliwn65n1ln7eoo4j8wd"); // 	break; 
UNSUPPORTED("37fbny64zwo23oymypyreuldc"); //     case 'n':
UNSUPPORTED("690ouuxs02oivip5twcvs5b11"); // 	bb.LL.x = x - sz.x / 2.0; 
UNSUPPORTED("2btku6pj5fxafqdz3xa85sd93"); // 	bb.UR.x = x + sz.x / 2.0; 
UNSUPPORTED("6qvptoliwn65n1ln7eoo4j8wd"); // 	break; 
UNSUPPORTED("8pu80wsk8me7q17ensqlviq86"); //     case 'r':
UNSUPPORTED("9hg1o01u12gavwtqe9xofe38z"); // 	bb.UR.x = x; 
UNSUPPORTED("bzx0k97ggr7q3araimvm6f1v7"); // 	bb.LL.x = bb.UR.x - sz.x;
UNSUPPORTED("6qvptoliwn65n1ln7eoo4j8wd"); // 	break; 
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("197tcjynsl2sn03dwf3g9y12c"); //     bb.UR.y = y + span->yoffset_layout;
UNSUPPORTED("f2dg8t4khzpqdoaarum9tyuth"); //     bb.LL.y = bb.UR.y - sz.y;
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 54ihxujpi40cvxm21zvc4yygl
// static void freePara (exdot_op* op) 
public static Object freePara(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("6j7qjd9yj32q85k9t0i4xidev"); // freePara (exdot_op* op)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dexpmz01x7t5bwrxs2ciihvod"); //     if (op->op.kind == xd_text)
UNSUPPORTED("8pgl92d45zuyooxxmo53bafei"); // 	free_textspan (op->span, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bna6lkyh9mx61k76gw3t0uflo
// boxf xdotBB (Agraph_t* g) 
public static Object xdotBB(Object... arg) {
UNSUPPORTED("8wvskez9r3noz1urymuwc4hvt"); // boxf xdotBB (Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("de8em3aa6r4le6z9745lpxlet"); //     GVC_t *gvc = GD_gvc(g);
UNSUPPORTED("6h54fnrtmztsxuhsa6imjz1a7"); //     exdot_op* op;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("c5uzlkl44upygp10do07uxm0g"); //     double fontsize = 0.0;
UNSUPPORTED("casv8ugezf3t2g5a7mvcr6q7i"); //     char* fontname = NULL;
UNSUPPORTED("4vfngh2uewkmjri7btdwl43bs"); //     pointf pts[2];
UNSUPPORTED("3zlnn621zia9mss7z1ay24myc"); //     pointf sz;
UNSUPPORTED("226wftslr130qjwxv1q4x2p4f"); //     boxf bb0;
UNSUPPORTED("2eiur8hkm8tcazpq12w4ikbqo"); //     boxf bb = GD_bb(g);
UNSUPPORTED("arw8znn5jqfqq2ukyt2s9kcbv"); //     xdot* xd = (xdot*)GD_drawing(g)->xdots;
UNSUPPORTED("aarb87wzm7tqwo5n1l925d21x"); //     textfont_t tf, null_tf = {NULL,NULL,NULL,0.0,0,0};
UNSUPPORTED("8wytjfmy8k9op5hj7s6yf9n3z"); //     int fontflags;
UNSUPPORTED("eeh0uom1nsj9lpceinvwt6gi3"); //     if (!xd) return bb;
UNSUPPORTED("46up7fyvuyhtxo8wqaw9zdj3h"); //     if ((bb.LL.x == bb.UR.x) && (bb.LL.y == bb.UR.y)) {
UNSUPPORTED("5jqc951ribmsybqkbienxqmso"); // 	bb.LL.x = bb.LL.y = MAXDOUBLE;
UNSUPPORTED("71c2mgloraxvejp5ao9v3simg"); // 	bb.UR.x = bb.UR.y = -MAXDOUBLE;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9go5curik6dnbejv5o2pj8ve2"); //     op = (exdot_op*)(xd->ops);
UNSUPPORTED("dvwaxn9xbj2jw2hjwvvs0tj3z"); //     for (i = 0; i < xd->cnt; i++) {
UNSUPPORTED("64ugcfa5jpvzxyig6y6rolvzb"); // 	tf = null_tf;
UNSUPPORTED("ab1ajnetstzss8ksa66rwdtze"); // 	switch (op->op.kind) {
UNSUPPORTED("1texeok1es39lsgo5wdppbco2"); // 	case xd_filled_ellipse :
UNSUPPORTED("effcskuftros0sla2ltem13mh"); // 	case xd_unfilled_ellipse :
UNSUPPORTED("2njikvu7tjbux5fxkdd45103i"); // 	    pts[0].x = op->op.u.ellipse.x - op->op.u.ellipse.w;
UNSUPPORTED("9mz7byklb9m0gvc8x9d7ksnr0"); // 	    pts[0].y = op->op.u.ellipse.y - op->op.u.ellipse.h;
UNSUPPORTED("90ewlrvkws79qrau49gy8es9l"); // 	    pts[1].x = op->op.u.ellipse.x + op->op.u.ellipse.w;
UNSUPPORTED("55l7hlnc89dkhu16ft92xg16q"); // 	    pts[1].y = op->op.u.ellipse.y + op->op.u.ellipse.h;
UNSUPPORTED("bneotzphmkyeas6debvmmon8u"); // 	    op->bb.LL = pts[0];
UNSUPPORTED("956syiyurqle8isi3w4ljs7wn"); // 	    op->bb.UR = pts[1];
UNSUPPORTED("7v3sk5uea7pu17t9pnjza0xk7"); // 	    expandBB (&bb, pts[0]);
UNSUPPORTED("dqzu6l172pawxz0l88wjbsq5x"); // 	    expandBB (&bb, pts[1]);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("40fjmoej0qk9hwneja77jlo2y"); // 	case xd_filled_polygon :
UNSUPPORTED("6uw61r6bh4tiicbj46yxr9t4k"); // 	case xd_unfilled_polygon :
UNSUPPORTED("9962qomo41y1omsipfqjr24y8"); // 	    op->bb = ptsBB (op->op.u.polygon.pts, op->op.u.polygon.cnt, &bb);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4eu4a2drbgdgq6xb3lnl3hn9y"); // 	case xd_filled_bezier :
UNSUPPORTED("bn514k7swi4s6uclqqke583n8"); // 	case xd_unfilled_bezier :
UNSUPPORTED("9962qomo41y1omsipfqjr24y8"); // 	    op->bb = ptsBB (op->op.u.polygon.pts, op->op.u.polygon.cnt, &bb);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1l7ha9akoh6yew2v54hvu3del"); // 	case xd_polyline :
UNSUPPORTED("9962qomo41y1omsipfqjr24y8"); // 	    op->bb = ptsBB (op->op.u.polygon.pts, op->op.u.polygon.cnt, &bb);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("60mg8gze602593bf2vddicza0"); // 	case xd_text :
UNSUPPORTED("50t9ohqv5008mknnbuwru8tw9"); // 	    op->span = (textspan_t*)zmalloc(sizeof(textspan_t));
UNSUPPORTED("ctpfroyvq8upi1rvu07czeffu"); // 	    op->span->str = strdup (op->op.u.text.text);
UNSUPPORTED("clfgmfms6909nfbpqqf7rxx5f"); // 	    op->span->just = adjust [op->op.u.text.align];
UNSUPPORTED("e2rt0ufps1ruahn7aijlrciss"); // 	    tf.name = fontname;
UNSUPPORTED("dh77fvkpvbx40iely5t866d6h"); // 	    tf.size = fontsize;
UNSUPPORTED("c1ted8bjs12be6jywuonnv0d2"); // 	    tf.flags = fontflags;
UNSUPPORTED("73o7k5irrg046co7k0hyc8aay"); //             op->span->font = (*(((Dt_t*)(gvc->textfont_dt))->searchf))((gvc->textfont_dt),(void*)(&tf),0000001);
UNSUPPORTED("6wqbzii0m490jqa70vqge95ss"); // 	    sz = textspan_size (gvc, op->span);
UNSUPPORTED("5fi8b3ht0cjvlehytid1q9v0c"); // 	    bb0 = textBB (op->op.u.text.x, op->op.u.text.y, op->span);
UNSUPPORTED("dzc3pvcqhus44n3qpjnche2n1"); // 	    op->bb = bb0;
UNSUPPORTED("dqaec6oh9n34envsvaj2cws50"); // 	    expandBB (&bb, bb0.LL);
UNSUPPORTED("4lyco5r149007xof0pltbc6yd"); // 	    expandBB (&bb, bb0.UR);
UNSUPPORTED("4ancwi87ybxfg7nqplm66fmcb"); // 	    if (!xd->freefunc)
UNSUPPORTED("euom952odxbs14o7zgilkcyjm"); // 		xd->freefunc = (freefunc_t)freePara;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("56n5nfs2z21zo7ac9z2ehf12l"); // 	case xd_font :
UNSUPPORTED("758yjuz5lxvy105kkk178ecqc"); // 	    fontsize = op->op.u.font.size;
UNSUPPORTED("b47tnf0bsigh1pwzg0zn6hwmr"); // 	    fontname = op->op.u.font.name;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2kiqf0asbojt8x24lr5eykca1"); // 	case xd_fontchar :
UNSUPPORTED("c1y0e85yacutsp89zjxoa7i3"); // 	    fontflags = op->op.u.fontchar;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5ujjs4gho9mjjupbibyqyplxp"); // 	default :
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("42f106i435avz0oneb0fhe81f"); // 	op++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 85uk85yiir9od6zij92agheo3
// static void init_gvc(GVC_t * gvc, graph_t * g) 
public static Object init_gvc(Object... arg) {
UNSUPPORTED("67101m7ilw6rq5kq50kqyv4jn"); // static void init_gvc(GVC_t * gvc, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1fbxoul2f22tyxwq8g4cj6bo6"); //     double xf, yf;
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("8zmfa30bj4y52yk4nu6fzjg4a"); //     gvc->g = g;
UNSUPPORTED("ax64rv1jwwzs47pikcnl1j6vv"); //     /* margins */
UNSUPPORTED("1b600jcjwmocj6dp4hx8dcbyt"); //     gvc->graph_sets_margin = 0;
UNSUPPORTED("1w3l4667jn9pi9aaxvt639rye"); //     if ((p = agget(g, "margin"))) {
UNSUPPORTED("8ii1lpvqnmj77k0q2xjan2dan"); //         i = sscanf(p, "%lf,%lf", &xf, &yf);
UNSUPPORTED("1e2qc8e42f9ohrllk7q5kbqh9"); //         if (i > 0) {
UNSUPPORTED("qbpuuttxxcxlbp802q1a4fsu"); //             gvc->margin.x = gvc->margin.y = xf * 72;
UNSUPPORTED("afqdselz7uxi22z0zlydrzlo1"); //             if (i > 1)
UNSUPPORTED("7col7tgqabkqwnzsc1m4i7zz3"); //                 gvc->margin.y = yf * 72;
UNSUPPORTED("c3921i2dlk22b1p51jy3q34df"); //             gvc->graph_sets_margin = NOT(0);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("733qr7bihsz4jlxdzv0x4v1n3"); //     /* pad */
UNSUPPORTED("c4y9dph55ilj94mj5yjbabgqu"); //     gvc->graph_sets_pad = 0;
UNSUPPORTED("8vka83wlbomztn5m1dom8trfq"); //     if ((p = agget(g, "pad"))) {
UNSUPPORTED("8ii1lpvqnmj77k0q2xjan2dan"); //         i = sscanf(p, "%lf,%lf", &xf, &yf);
UNSUPPORTED("1e2qc8e42f9ohrllk7q5kbqh9"); //         if (i > 0) {
UNSUPPORTED("8vm798xe38aiaugd3y1g2aq1t"); //             gvc->pad.x = gvc->pad.y = xf * 72;
UNSUPPORTED("afqdselz7uxi22z0zlydrzlo1"); //             if (i > 1)
UNSUPPORTED("5nz9wezr9fh7hfzwoqgk11zm1"); //                 gvc->pad.y = yf * 72;
UNSUPPORTED("d1m7j39edzq6lp1g0a8lnxyhf"); //             gvc->graph_sets_pad = NOT(0);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("96riek1d0o8vwl70xmegernpv"); //     /* pagesize */
UNSUPPORTED("298vt0zyw2i76g01fnol84jux"); //     gvc->graph_sets_pageSize = 0;
UNSUPPORTED("2shc43elsrp70a4k2u220ehox"); //     gvc->pageSize = GD_drawing(g)->page;
UNSUPPORTED("6evkjzqolfcsrv7tpqsm58o3h"); //     if ((GD_drawing(g)->page.x > 0.001) && (GD_drawing(g)->page.y > 0.001))
UNSUPPORTED("fgxbok14dgt3z4b3nscl0p7n"); //         gvc->graph_sets_pageSize = NOT(0);
UNSUPPORTED("ecjhhm3qqdov34ahw0hdmnb1g"); //     /* rotation */
UNSUPPORTED("2cxu41gtx0x2822685tf09ctd"); //     if (GD_drawing(g)->landscape)
UNSUPPORTED("cva8oucw05hmnqf4l4bk4dgfh"); // 	gvc->rotation = 90;
UNSUPPORTED("4lti1w2qslxj3ihoatmbavsfr"); //     else 
UNSUPPORTED("b4f4qufgjft6e4a4p7mkitiqv"); // 	gvc->rotation = 0;
UNSUPPORTED("e5gxr3o07ghgvc4ucdwpjflcj"); //     /* pagedir */
UNSUPPORTED("bb3h91bcfx7by4d3fyhmzdy2b"); //     gvc->pagedir = "BL";
UNSUPPORTED("bpcvlnl6cfm6bjjupn4octi5k"); //     if ((p = agget(g, "pagedir")) && p[0])
UNSUPPORTED("ajams0xdty7a7uuekgeid69r1"); //             gvc->pagedir = p;
UNSUPPORTED("e2g5l4w3c1x1gfmugldesjjqd"); //     /* bounding box */
UNSUPPORTED("7tzs0wxbqa2wkozgqx08w4bau"); //     gvc->bb = GD_bb(g);
UNSUPPORTED("5hczsy1j61vmnr84wyz5bikya"); //     /* clusters have peripheries */
UNSUPPORTED("clrndk7c262q4i7auu2yxaxbe"); //     G_peripheries = (agattr(g,AGRAPH,"peripheries",NULL));
UNSUPPORTED("2kfixvhj3cpd8ixik3psbcfpe"); //     G_penwidth = (agattr(g,AGRAPH,"penwidth",NULL));
UNSUPPORTED("2cwp5x6rqs985rs765vqqygae"); //     /* default font */
UNSUPPORTED("e7ws06cub67fioowd2qb57bdq"); //     gvc->defaultfontname = late_nnstring(NULL,
UNSUPPORTED("7bozarsmoudzlhh3sw6pkg1h5"); //                 N_fontname, "Times-Roman");
UNSUPPORTED("58t4peku7cnyvnai2a20f5879"); //     gvc->defaultfontsize = late_double(NULL,
UNSUPPORTED("bbm0e0fv8warl1ji3fdc6yt6b"); //                 N_fontsize, 14.0, 1.0);
UNSUPPORTED("5w09u9j3f1qfayfhrgj57ecba"); //     /* default line style */
UNSUPPORTED("7qer3idmrdf9t80kv56o3uuss"); //     gvc->defaultlinestyle = defaultlinestyle;
UNSUPPORTED("4bhwcitlq1486x53gcbhloizr"); //     gvc->graphname = agnameof(g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9pyz8u3udm2aoyugcsto0kix5
// static void init_job_pad(GVJ_t *job) 
public static Object init_job_pad(Object... arg) {
UNSUPPORTED("agxhh3hamzm2l90mq5ojrmfkv"); // static void init_job_pad(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("de4elc7zm79xniuphcxgyvvk9"); //     if (gvc->graph_sets_pad) {
UNSUPPORTED("aptzesoqv5uqgvgg0mc6kao5e"); // 	job->pad = gvc->pad;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("aj6nzz9d7a58djho9n52jx8yw"); // 	switch (job->output_lang) {
UNSUPPORTED("b72lffvceole2ir7mpo7p336e"); // 	case 300:
UNSUPPORTED("2j1zpsvat3akpjxygl2b8ttbg"); // 	    job->pad.x = job->pad.y = job->render.features->default_pad;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("3w0qgk20xo9z7unk42folarn1"); // 	    job->pad.x = job->pad.y = 4;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 f2syfb4rj7trco59aj1v5jm7b
// static void init_job_margin(GVJ_t *job) 
public static Object init_job_margin(Object... arg) {
UNSUPPORTED("a2o4lfoc2z821pg69j2xraq5h"); // static void init_job_margin(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("7jaj0gwqx6io2udyixmrzcc1z"); //     if (gvc->graph_sets_margin) {
UNSUPPORTED("4snswj683owzbho4jjotw970l"); // 	job->margin = gvc->margin;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("za5jq2evnfd3fbn96g250vy9"); //         /* set default margins depending on format */
UNSUPPORTED("ampzlf94vv6frsr0uc6d017f6"); //         switch (job->output_lang) {
UNSUPPORTED("aul67kwcbtvgyy4b36yxw76ou"); //         case 300:
UNSUPPORTED("e4ovq7tj6z354n1fhbnxfzufe"); //             job->margin = job->device.features->default_margin;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("2jhp1005lxz99z1pc9syee424"); //         case 2: case 3: case 4: case 22: case 21: case 30:
UNSUPPORTED("9mg8oo60qykyf8i35w2raa26p"); //             job->margin.x = job->margin.y = 36;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("p0mt8wznalavjdm44ot4ykl7"); //         default:
UNSUPPORTED("5l3hvgkkpuhcf4cohs28abhtz"); //             job->margin.x = job->margin.y = 0;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8g64hepne35gexv6ju81tbqc3
// static void init_job_dpi(GVJ_t *job, graph_t *g) 
public static Object init_job_dpi(Object... arg) {
UNSUPPORTED("2p6nt5vob0ei8v7773p5tdecd"); // static void init_job_dpi(GVJ_t *job, graph_t *g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8m7r6h2rw1gx6dc2436egqiay"); //     GVJ_t *firstjob = job->gvc->active_jobs;
UNSUPPORTED("brb2vqrp3wxbej2f8h8oo3dcq"); //     if (GD_drawing(g)->dpi != 0) {
UNSUPPORTED("dt9qdoazpes90xzueapuxxspa"); //         job->dpi.x = job->dpi.y = (double)(GD_drawing(g)->dpi);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6upj4lbia39c7c7hd9vsvv8b8"); //     else if (firstjob && firstjob->device_sets_dpi) {
UNSUPPORTED("7mtrejna32wqb64ec8s1hvrgt"); //         job->dpi = firstjob->device_dpi;   /* some devices set dpi in initialize() */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("za5jq2evnfd3fbn96g250vy9"); //         /* set default margins depending on format */
UNSUPPORTED("ampzlf94vv6frsr0uc6d017f6"); //         switch (job->output_lang) {
UNSUPPORTED("aul67kwcbtvgyy4b36yxw76ou"); //         case 300:
UNSUPPORTED("azl3hjq4ji3la2pi9cndx1k3e"); //             job->dpi = job->device.features->default_dpi;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("p0mt8wznalavjdm44ot4ykl7"); //         default:
UNSUPPORTED("7g69tkl1he21l042jkugrqp6o"); //             job->dpi.x = job->dpi.y = (double)(DEFAULT_DPI);
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4q75k0eh9opws0n4g4hucx21k
// static void init_job_viewport(GVJ_t * job, graph_t * g) 
public static Object init_job_viewport(Object... arg) {
UNSUPPORTED("624efhjg4wk3vw59b77itfyvo"); // static void init_job_viewport(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("6i2nacyhq49omlq45m07qui2g"); //     pointf LL, UR, size, sz;
UNSUPPORTED("du235xtc4birmdt1yr7ce3jpv"); //     double X, Y, Z, x, y;
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("adc06gp2568j6zfk5jqvbfo2y"); //     char *str, *nodename = NULL, *junk = NULL;
UNSUPPORTED("cibtljruio32e5gvrl87lh88l"); //     UR = gvc->bb.UR;
UNSUPPORTED("5vjounfbnwwcy8tbj300r79r2"); //     LL = gvc->bb.LL;
UNSUPPORTED("50ygg0dkfzxllsg7k6fe56huy"); //     job->bb.LL.x = LL.x - job->pad.x;           /* job->bb is bb of graph and padding - graph units */
UNSUPPORTED("49aqo5a8zzjcw7blnfu1d31q4"); //     job->bb.LL.y = LL.y - job->pad.y;
UNSUPPORTED("b3wmaft4mjah5tibmxgrds6eg"); //     job->bb.UR.x = UR.x + job->pad.x;
UNSUPPORTED("80w280ro0p0cugy34wuqzbsh9"); //     job->bb.UR.y = UR.y + job->pad.y;
UNSUPPORTED("6w6xt2yb2jn5st0s2quady6io"); //     sz.x = job->bb.UR.x - job->bb.LL.x;   /* size, including padding - graph units */
UNSUPPORTED("99nzl03ckbymw5ajsxg2lxf5v"); //     sz.y = job->bb.UR.y - job->bb.LL.y;
UNSUPPORTED("2mgynwyvee3ie46hpl26kqtf2"); //     /* determine final drawing size and scale to apply. */
UNSUPPORTED("753yoijk60817tp79pq00vr0m"); //     /* N.B. size given by user is not rotated by landscape mode */
UNSUPPORTED("ag04g63wularfd1fbljawhkqj"); //     /* start with "natural" size of layout */
UNSUPPORTED("637m663yod0dkbreevp6qk44p"); //     Z = 1.0;
UNSUPPORTED("c8oufh828idctovprrgjdm0os"); //     if (GD_drawing(g)->size.x > 0.001 && GD_drawing(g)->size.y > 0.001) { /* graph size was given by user... */
UNSUPPORTED("bmz1dbirkeldxm62ak0co2qrb"); // 	size = GD_drawing(g)->size;
UNSUPPORTED("6qb7wsi1h19gy3dx25dsrf542"); // 	if (sz.x == 0) sz.x = size.x;
UNSUPPORTED("3o8bk5rxvc9z3ttlp9sr2qn4s"); // 	if (sz.y == 0) sz.y = size.y;
UNSUPPORTED("46ob9e3vmgm08hjtxp6tfvhoz"); // 	if ((size.x < sz.x) || (size.y < sz.y) /* drawing is too big (in either axis) ... */
UNSUPPORTED("5auolwo9gg1phxl3qsyuubbu1"); // 	    || ((GD_drawing(g)->filled) /* or ratio=filled requested and ... */
UNSUPPORTED("9x8rozkks9tk12it75o6ejh9"); // 		&& (size.x > sz.x) && (size.y > sz.y))) /* drawing is too small (in both axes) ... */
UNSUPPORTED("745ba2myf8zykpw51eip22lxr"); // 	    Z = MIN(size.x/sz.x, size.y/sz.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("62gog7hki8holy1wdxok8ii3s"); //     /* default focus, in graph units = center of bb */
UNSUPPORTED("eh4upvvzpvb3b2hlopix62qvx"); //     x = (LL.x + UR.x) / 2.;
UNSUPPORTED("8mdsmfdv26en9t0wke5lj3yoe"); //     y = (LL.y + UR.y) / 2.;
UNSUPPORTED("cb0lygghq98c5tsuyl02ecakn"); //     /* rotate and scale bb to give default absolute size in points*/
UNSUPPORTED("7y4ysg5hih6aou07wc3jxhb35"); //     job->rotation = job->gvc->rotation;
UNSUPPORTED("1tcvgfcsior9vxwosbtysfoyl"); //     X = sz.x * Z;
UNSUPPORTED("7rvjwv9lldh8cdbpwf2e5b4em"); //     Y = sz.y * Z;
UNSUPPORTED("7mjigzchm03dvse5yty9697ik"); //     /* user can override */
UNSUPPORTED("bbb4aunqlxn7ymn473u9tfjh2"); //     if ((str = agget(g, "viewport"))) {
UNSUPPORTED("70923c97qwh5i4mk8teckxbg"); //         nodename = malloc(strlen(str)+1);
UNSUPPORTED("axxbey14epctt08bqmg4e4ljx"); //         junk = malloc(strlen(str)+1);
UNSUPPORTED("4nbqjt16sk1g61qjzmyth3m3y"); // 	rv = sscanf(str, "%lf,%lf,%lf,\'%[^\']\'", &X, &Y, &Z, nodename);
UNSUPPORTED("8azqq8b6xz6pe75pwuu92bkis"); // 	if (rv == 4) {
UNSUPPORTED("6vgu6qnrol7hlgg01x8iopat1"); // 	    n = (agnode(g->root,nodename,0));
UNSUPPORTED("5m395o1mab9my7pgd1wtfpkt4"); // 	    if (n) {
UNSUPPORTED("54io2bm3l9lajfo4g3z58eg1t"); // 		x = ND_coord(n).x;
UNSUPPORTED("9woodb3r1ali6shtfkh2rz8h2"); // 		y = ND_coord(n).y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("s1rt11h530ot4w6oo53nl5ui"); // 	    rv = sscanf(str, "%lf,%lf,%lf,%[^,]%s", &X, &Y, &Z, nodename, junk);
UNSUPPORTED("4kcpjq4bw60ex3z1ewxgr1mjw"); // 	    if (rv == 4) {
UNSUPPORTED("874kb6l4r88vgo7isombaflpy"); //                 n = (agnode(g->root,nodename,0));
UNSUPPORTED("a2kqxh7ugzx0wm2vq2kbx20y7"); //                 if (n) {
UNSUPPORTED("9qhwk8hw3vgr1m1rzg5atx1zk"); //                     x = ND_coord(n).x;
UNSUPPORTED("e4zoymn2c1bi9un0aj7x2plqm"); //                     y = ND_coord(n).y;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2s114kzoj9bs4dyo9fxw68w0t"); // 	        rv = sscanf(str, "%lf,%lf,%lf,%lf,%lf", &X, &Y, &Z, &x, &y);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("blr8vtykke5kreaxyos1zlb97"); // 	free (nodename);
UNSUPPORTED("2pd0mx508kg0shcwmffjkyxc4"); // 	free (junk);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7bmnorp8hdilbzb9xmgr16rs5"); //     /* rv is ignored since args retain previous values if not scanned */
UNSUPPORTED("c22q6sbjdy7erjlxyrdanr1x3"); //     /* job->view gives port size in graph units, unscaled or rotated
UNSUPPORTED("eb5vhw8ufy0hgbridh6g2vao9"); //      * job->zoom gives scaling factor.
UNSUPPORTED("f4zt60wq4jiuahkpno3xurcl7"); //      * job->focus gives the position in the graph of the center of the port
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("asnqk2ep45wun0a0przl141qw"); //     job->view.x = X;
UNSUPPORTED("98ffy6ki313rluvwb97rnvig9"); //     job->view.y = Y;
UNSUPPORTED("2lcv08icuvjg8b9dwsdpqqold"); //     job->zoom = Z;              /* scaling factor */
UNSUPPORTED("3pf2n1wcp8dwhgr0dom3f5c5y"); //     job->focus.x = x;
UNSUPPORTED("d40e8u3927lyzr1i1eodxb5ip"); //     job->focus.y = y;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cixyhqk69srhxgi3264xh8v68
// static void emit_cluster_colors(GVJ_t * job, graph_t * g) 
public static Object emit_cluster_colors(Object... arg) {
UNSUPPORTED("4do3kncw83okg157mdqb1rtct"); // static void emit_cluster_colors(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5wyi0wasd5kecf6rhsqdjk7v2"); //     graph_t *sg;
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("76nok3eiyr33qf4ecv69ujxn6"); //     char *str;
UNSUPPORTED("99d9j6m0161wdv2tu4wbf3ifi"); //     for (c = 1; c <= GD_n_cluster(g); c++) {
UNSUPPORTED("cuf43q4kl3kqgyuuxdqve1mqt"); // 	sg = GD_clust(g)[c];
UNSUPPORTED("eior21tu8n3jvwp0uvojvs0f9"); // 	emit_cluster_colors(job, sg);
UNSUPPORTED("9obyty3pjddtipkl1momqv3h5"); // 	if (((str = agget(sg, "color")) != 0) && str[0])
UNSUPPORTED("6095vphn778u0cj7bwv5weas7"); // 	    gvrender_set_pencolor(job, str);
UNSUPPORTED("497nfxb4jva1q66gyzxn4skpv"); // 	if (((str = agget(sg, "pencolor")) != 0) && str[0])
UNSUPPORTED("6095vphn778u0cj7bwv5weas7"); // 	    gvrender_set_pencolor(job, str);
UNSUPPORTED("70ojemxfr6iv12m0fxcxdg0bp"); // 	if (((str = agget(sg, "bgcolor")) != 0) && str[0])
UNSUPPORTED("6095vphn778u0cj7bwv5weas7"); // 	    gvrender_set_pencolor(job, str);
UNSUPPORTED("cdl50xso69xffobr8d9xfkxez"); // 	if (((str = agget(sg, "fillcolor")) != 0) && str[0])
UNSUPPORTED("1a63r7ud39pu64tbombwke5j6"); // 	    gvrender_set_fillcolor(job, str);
UNSUPPORTED("a5y2a7fa23tghirfggvztwunc"); // 	if (((str = agget(sg, "fontcolor")) != 0) && str[0])
UNSUPPORTED("6095vphn778u0cj7bwv5weas7"); // 	    gvrender_set_pencolor(job, str);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6z2ta8nxn4szbpjidikrdzjd2
// static void emit_colors(GVJ_t * job, graph_t * g) 
public static Object emit_colors(Object... arg) {
UNSUPPORTED("60y3484rmpjm7xlt67j7mdl4"); // static void emit_colors(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("85i6qyzcmdaf9l7uvotaz8nlu"); //     char *str, *colors;
UNSUPPORTED("c7ds3utacpnq14lyij0jurwb4"); //     gvrender_set_fillcolor(job, "lightgrey");
UNSUPPORTED("1lp6a62t425k9v01z810wrp7b"); //     if (((str = agget(g, "bgcolor")) != 0) && str[0])
UNSUPPORTED("34u8f5cr333f4sprskexyebpp"); // 	gvrender_set_fillcolor(job, str);
UNSUPPORTED("56twc8sam9h8hr3clpxijei6p"); //     if (((str = agget(g, "fontcolor")) != 0) && str[0])
UNSUPPORTED("6uvs8it1wts6hypu32nx4owck"); // 	gvrender_set_pencolor(job, str);
UNSUPPORTED("bikc4jaduha3tdbl9ci5qcrq5"); //     emit_cluster_colors(job, g);
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("dnan5xu2dkpu7sl1b03ahndx"); // 	if (((str = agget(n, "color")) != 0) && str[0])
UNSUPPORTED("6095vphn778u0cj7bwv5weas7"); // 	    gvrender_set_pencolor(job, str);
UNSUPPORTED("7zwodk0er4swr49op7lbvckmu"); // 	if (((str = agget(n, "pencolor")) != 0) && str[0])
UNSUPPORTED("1a63r7ud39pu64tbombwke5j6"); // 	    gvrender_set_fillcolor(job, str);
UNSUPPORTED("7jynqpniz3o3a1uxq7wr68b3q"); // 	if (((str = agget(n, "fillcolor")) != 0) && str[0]) {
UNSUPPORTED("2zkeug16gnaam15be1glq0d41"); // 	    if (strchr(str, ':')) {
UNSUPPORTED("zgs1bnae9v7jn6nvqwuf2unh"); // 		colors = strdup(str);
UNSUPPORTED("65cuya3pzizp1x4mn7jdufwrd"); // 		for (str = strtok(colors, ":"); str;
UNSUPPORTED("3pjrrvuvl7stxcg84h491n5qg"); // 		    str = strtok(0, ":")) {
UNSUPPORTED("bxlc2ohnhd7goi002fh9vwmuf"); // 		    if (str[0])
UNSUPPORTED("a83f0af7up943bqeqb4ktss9t"); // 			gvrender_set_pencolor(job, str);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("1gvispqysaotaxpswiriasys2"); // 		free(colors);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("97osvej5vqjmf4a6du709am4f"); // 		gvrender_set_pencolor(job, str);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("gmm6wp0dtyc0tsmtzu5if62t"); // 	if (((str = agget(n, "fontcolor")) != 0) && str[0])
UNSUPPORTED("6095vphn778u0cj7bwv5weas7"); // 	    gvrender_set_pencolor(job, str);
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("bcwa1u39hsu389fzlp0d4l21f"); // 	    if (((str = agget(e, "color")) != 0) && str[0]) {
UNSUPPORTED("4efctxbb4wq0ep3hzlpkaz7ue"); // 		if (strchr(str, ':')) {
UNSUPPORTED("5vhkpar2tw6xtzhthkurydd1f"); // 		    colors = strdup(str);
UNSUPPORTED("aiy80oj5zipifrkb47xkmftsr"); // 		    for (str = strtok(colors, ":"); str;
UNSUPPORTED("6krlp42os46gl8wai0xgvb6kw"); // 			str = strtok(0, ":")) {
UNSUPPORTED("cnbqptqtolqfv8ldin52nlvx0"); // 			if (str[0])
UNSUPPORTED("cogpzraxmtn5x26pbzrmt64ow"); // 			    gvrender_set_pencolor(job, str);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("9eduqn5owyzon5mdfrfajror6"); // 		    free(colors);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("13h4fpjj7vkimtrsq5l5siic1"); // 		    gvrender_set_pencolor(job, str);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("33tragnfqd9eqgqdst6rty8gp"); // 	    if (((str = agget(e, "fontcolor")) != 0) && str[0])
UNSUPPORTED("97osvej5vqjmf4a6du709am4f"); // 		gvrender_set_pencolor(job, str);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dlue7nzn2pfskq2p54b34b488
// static void emit_view(GVJ_t * job, graph_t * g, int flags) 
public static Object emit_view(Object... arg) {
UNSUPPORTED("5imanth4uju8cetckpnvqvzub"); // static void emit_view(GVJ_t * job, graph_t * g, int flags)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c00osdfe9g1arquj6clfdrh3e"); //     GVC_t * gvc = job->gvc;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("csndbis67mjcboadb9c3vccda"); //     gvc->common.viewNum++;
UNSUPPORTED("586npexnnd5vijp8l2yb7kv6i"); //     /* when drawing, lay clusters down before nodes and edges */
UNSUPPORTED("8rgrpr4idg5l73bbih7wy6xuh"); //     if (!(flags & (1<<2)))
UNSUPPORTED("5gnlxmhx15pwjdqb1bv6lj9q1"); // 	emit_clusters(job, g, flags);
UNSUPPORTED("8odxjg9o1tr8v6pqudnrplweo"); //     if (flags & (1<<0)) {
UNSUPPORTED("2xnyatu1c43zijt16ke1g4gg9"); // 	/* output all nodes, then all edges */
UNSUPPORTED("24iil9cfhuxvvzbkepuax0fq8"); // 	gvrender_begin_nodes(job);
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("21cc1ilu48o079xze4a4zj021"); // 	    emit_node(job, n);
UNSUPPORTED("bwt97v7vxmudd2nrehidk4f20"); // 	gvrender_end_nodes(job);
UNSUPPORTED("3vzktz5re4ifmdiynmy5l85h9"); // 	gvrender_begin_edges(job);
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("8wpryg13apwpccvklympyeyqu"); // 	    for (e = agfstout(g, n); e; e = agnxtout(g, e))
UNSUPPORTED("egbuqrd3oowt73ihe2c07hylx"); // 		emit_edge(job, e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7pirwyfpglg7wt4glc4zlfvz"); // 	gvrender_end_edges(job);
UNSUPPORTED("aj09npfwn78tl31m397v0s50r"); //     } else if (flags & (1<<4)) {
UNSUPPORTED("clvp13zubjxry8l8nesv79xjl"); // 	/* output all edges, then all nodes */
UNSUPPORTED("3vzktz5re4ifmdiynmy5l85h9"); // 	gvrender_begin_edges(job);
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("8wpryg13apwpccvklympyeyqu"); // 	    for (e = agfstout(g, n); e; e = agnxtout(g, e))
UNSUPPORTED("egbuqrd3oowt73ihe2c07hylx"); // 		emit_edge(job, e);
UNSUPPORTED("7pirwyfpglg7wt4glc4zlfvz"); // 	gvrender_end_edges(job);
UNSUPPORTED("24iil9cfhuxvvzbkepuax0fq8"); // 	gvrender_begin_nodes(job);
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("21cc1ilu48o079xze4a4zj021"); // 	    emit_node(job, n);
UNSUPPORTED("bwt97v7vxmudd2nrehidk4f20"); // 	gvrender_end_nodes(job);
UNSUPPORTED("3v872xkvak5nthxntrmy679dt"); //     } else if (flags & (1<<3)) {
UNSUPPORTED("24iil9cfhuxvvzbkepuax0fq8"); // 	gvrender_begin_nodes(job);
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("enqvgohrxvf10ccw4y4r96nkf"); // 	    if (write_node_test(g, n))
UNSUPPORTED("1bzj2os22s6b3tf899bpkde6t"); // 		emit_node(job, n);
UNSUPPORTED("bwt97v7vxmudd2nrehidk4f20"); // 	gvrender_end_nodes(job);
UNSUPPORTED("3vzktz5re4ifmdiynmy5l85h9"); // 	gvrender_begin_edges(job);
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("7yvyv13me3s32qvq3gfbyt283"); // 	    for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("2fgkqy3hjpj9eo76nnctdhelj"); // 		if (write_edge_test(g, e))
UNSUPPORTED("auj2mo2tz3uf10zjkza14e82k"); // 		    emit_edge(job, e);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7pirwyfpglg7wt4glc4zlfvz"); // 	gvrender_end_edges(job);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("2638u64adcw68h4iekta3woqj"); // 	/* output in breadth first graph walk order */
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("21cc1ilu48o079xze4a4zj021"); // 	    emit_node(job, n);
UNSUPPORTED("7yvyv13me3s32qvq3gfbyt283"); // 	    for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("6a94yfoszisanhlfhbgaagm7b"); // 		emit_node(job, aghead(e));
UNSUPPORTED("egbuqrd3oowt73ihe2c07hylx"); // 		emit_edge(job, e);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1rit427sa8udvzo3k42t9iha0"); //     /* when mapping, detect events on clusters after nodes and edges */
UNSUPPORTED("1nky1gj4525b4130zqg4jg6ah"); //     if (flags & (1<<2))
UNSUPPORTED("5gnlxmhx15pwjdqb1bv6lj9q1"); // 	emit_clusters(job, g, flags);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dk8gj6u142q8vgup6ar9ruysc
// static void emit_begin_graph(GVJ_t * job, graph_t * g) 
public static Object emit_begin_graph(Object... arg) {
UNSUPPORTED("5njsnfg1w96wtvkzxnnx79oze"); // static void emit_begin_graph(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8lamppsqs7nhiu5v8k1f5jfh2"); //     obj_state_t *obj;
UNSUPPORTED("1iexddadjo0w6fdgddatfx40s"); //     obj = push_obj_state(job);
UNSUPPORTED("7ihakbkozgh3iuzyjmmhyh6k3"); //     obj->type = ROOTGRAPH_OBJTYPE;
UNSUPPORTED("9ud9itr2e77p4owess7q8718d"); //     obj->u.g = g;
UNSUPPORTED("5pf0fco1plw3oedj6uzq7uimr"); //     obj->emit_state = EMIT_GDRAW;
UNSUPPORTED("bg4pso3sflig43odlnhxv7hak"); //     initObjMapData (job, GD_label(g), g);
UNSUPPORTED("e44aso60solubotswg1yr8un"); //     gvrender_begin_graph(job, g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3nhn5596nwxutoyrm6tsxwvbd
// static void emit_end_graph(GVJ_t * job, graph_t * g) 
public static Object emit_end_graph(Object... arg) {
UNSUPPORTED("e5oluojkvyfuqdu97u95cqg3w"); // static void emit_end_graph(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1n9wfd0yho4z5950kfzq1f6y5"); //     gvrender_end_graph(job);
UNSUPPORTED("39iamwq9cd9iv3d2iyiaq8gz9"); //     pop_obj_state(job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9to165vrtas68afpbjdprx2vd
// static void emit_page(GVJ_t * job, graph_t * g) 
public static Object emit_page(Object... arg) {
UNSUPPORTED("di1dvfa15xs9rx182756djgqg"); // static void emit_page(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("6662g2itnb2i3tqvhvnkpfe2f"); //     int nump = 0, flags = job->flags;
UNSUPPORTED("1ikkkd5pukqwffmu5mtak47js"); //     textlabel_t *lab;
UNSUPPORTED("wv6e6kkgh4jw93f4dlmoozok"); //     pointf *p = NULL;
UNSUPPORTED("6xn8wrm21858wn8m5cu76e7k6"); //     char* saveid;
UNSUPPORTED("h0or3v13348vfl22jqz895yc"); //     unsigned char buf[128];
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("c4vh2q9zpipty999dty5nvmpm"); //     /* For the first page, we can use the values generated in emit_begin_graph. 
UNSUPPORTED("2rctyueh35ne2pj7qe3nbgj64"); //      * For multiple pages, we need to generate a new id.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("1p2sfiuz204i105cwxgsww1g7"); //     if ((((job)->layerNum>1)||((job)->pagesArrayElem.x > 0)||((job)->pagesArrayElem.x > 0))) {
UNSUPPORTED("el1z2krv8hoorc5c7vjsxw7yn"); // 	agxbinit(&xb, 128, buf);
UNSUPPORTED("288ufw5rels1yxspvgtsyk3ve"); // 	saveid = obj->id;
UNSUPPORTED("dly1rpg3t74a0cgwq5m4vdfir"); // 	layerPagePrefix (job, &xb);
UNSUPPORTED("9vff0t827i9c1drbwcuxv66vl"); // 	agxbput (&xb, saveid);
UNSUPPORTED("7fsfc9wkigjp8e60ca77krp4q"); // 	obj->id = (((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("5cjdy9xrlft2fd3m8c4wfxvn0"); // 	saveid = NULL;
UNSUPPORTED("3glknrzusaw84wts2i8rbn2cq"); //     setColorScheme (agget (g, "colorscheme"));
UNSUPPORTED("7n40v0iw6f6evriibhioqu7ts"); //     setup_page(job, g);
UNSUPPORTED("8xm4n85i531jwobi44phtf4ir"); //     gvrender_begin_page(job);
UNSUPPORTED("e7x5ptqcyrjeve73rf3hjhuhn"); //     gvrender_set_pencolor(job, "black");
UNSUPPORTED("c7ds3utacpnq14lyij0jurwb4"); //     gvrender_set_fillcolor(job, "lightgrey");
UNSUPPORTED("8k7npuk2pg2np42cu63aachgc"); //     if ((flags & ((1<<16) | (1<<22)))
UNSUPPORTED("cjxb7soy84ukfc9btgpgb20qv"); // 	    && (obj->url || obj->explicit_tooltip)) {
UNSUPPORTED("bdk6hu2yhmni45qx7umzxcygk"); // 	if (flags & ((1<<17) | (1<<19))) {
UNSUPPORTED("9cwsuo4ejz94kt28xa30z24o4"); // 	    if (flags & (1<<17)) {
UNSUPPORTED("4rlzy2etw4nk635bj5t9ru8p8"); // 		obj->url_map_shape = MAP_RECTANGLE;
UNSUPPORTED("c4ypp4cr8076ye8m20un6b1r0"); // 		nump = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("ei9dbrwfxqj4zzq5goyjrp35m"); // 		obj->url_map_shape = MAP_POLYGON;
UNSUPPORTED("bc2bgfqrx6wr17fw3ipheaxt3"); // 		nump = 4;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("1ubuo1fytrqcvpt7d5d82uhus"); // 	    p = (pointf*)zmalloc((nump)*sizeof(pointf));
UNSUPPORTED("7mbkf7uj9kru8y0p4vqwvqcuz"); // 	    p[0] = job->pageBox.LL;
UNSUPPORTED("6r0id695rza6fkyms76whk4iv"); // 	    p[1] = job->pageBox.UR;
UNSUPPORTED("bt5khrnbcnl2rast8s6f5hsb3"); // 	    if (! (flags & ((1<<17))))
UNSUPPORTED("32e1sy1ya6lb3qmnvfchmljx6"); // 		rect2poly(p);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cnadzqqicrskcdzko360llcg5"); // 	if (! (flags & (1<<13)))
UNSUPPORTED("8bpnq0a8qri3cve2qymb5tdyk"); // 	    gvrender_ptf_A(job, p, p, nump);
UNSUPPORTED("e4cpy14h8m2l16mjqadzsk89g"); // 	obj->url_map_p = p;
UNSUPPORTED("evxoqe3dfgjf7bsm11ipkr8uc"); // 	obj->url_map_n = nump;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6le9aolkoakj39i518xz8m4mp"); //     if ((flags & (1<<15)) && ((lab = GD_label(g))))
UNSUPPORTED("ear31tm0c4wpfnpcaoecqkfs3"); // 	/* do graph label on every page and rely on clipping to show it on the right one(s) */
UNSUPPORTED("1sy2sk1asrfkgl0880y6kmloo"); // 	obj->label = lab->text;
UNSUPPORTED("5r6tugfy15ojg2zla1xfnbmfc"); // 	/* If EMIT_CLUSTERS_LAST is set, we assume any URL or tooltip
UNSUPPORTED("33ajxgdgcy9ya7o2qo3545kjz"); // 	 * attached to the root graph is emitted either in begin_page
UNSUPPORTED("8oq67ru65jsrltrbkpicoq60r"); // 	 * or end_page of renderer.
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("b465fu7r28ccnhoa0uvk6txhm"); //     if (!(flags & (1<<2)) && (obj->url || obj->explicit_tooltip)) {
UNSUPPORTED("874x9mgt9apdota54iarnz9v1"); // 	emit_map_rect(job, job->clip);
UNSUPPORTED("5dgsg5nmc05yy1ape6ses927z"); // 	gvrender_begin_anchor(job, obj->url, obj->tooltip, obj->target, obj->id);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4h1kjxw1u80nnonna7i2zuoec"); //     /* if (numPhysicalLayers(job) == 1) */
UNSUPPORTED("2vh3blbhjrzadv1wr6ru210tj"); // 	emit_background(job, g);
UNSUPPORTED("aplr7sm051i57jygcfj6gigoh"); //     if (GD_label(g))
UNSUPPORTED("ett9sr8mbs57jfjk6eqf66ood"); // 	emit_label(job, EMIT_GLABEL, GD_label(g));
UNSUPPORTED("abgvf32lk9jxduwyafzp9fn6r"); //     if (!(flags & (1<<2)) && (obj->url || obj->explicit_tooltip))
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("ckjdhy2wwekw6raq99m9r1wwe"); //     emit_view(job,g,flags);
UNSUPPORTED("90q0ssftdvmcofopllyikqw70"); //     gvrender_end_page(job);
UNSUPPORTED("ba2uwvbdk3mubgsfak0882ay6"); //     if (saveid) {
UNSUPPORTED("52frst03mcbsuu6bmnqs962qm"); // 	agxbfree(&xb);
UNSUPPORTED("9z28bgnq8rla75dmcugnyi4ql"); // 	obj->id = saveid;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 87nutd480deekxln7o14or4vo
// void emit_graph(GVJ_t * job, graph_t * g) 
public static Object emit_graph(Object... arg) {
UNSUPPORTED("3bmd2hvgnq1fcmgpxtam9hsx0"); // void emit_graph(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("12bpksga44s9sfl7x8xn2rt2k"); //     int flags = job->flags;
UNSUPPORTED("9q16e0ya19gm18zkyq7str796"); //     int* lp;
UNSUPPORTED("3aho54lnlpjyvy4ud87k3xnt1"); //     /* device dpi is now known */
UNSUPPORTED("drdz8zf1napfd9649xa2vvoui"); //     job->scale.x = job->zoom * job->dpi.x / 72;
UNSUPPORTED("esbjlmqb7gaaf3pfsa89lgev"); //     job->scale.y = job->zoom * job->dpi.y / 72;
UNSUPPORTED("ag4tx55kpqxgiqh97rx32q22l"); //     job->devscale.x = job->dpi.x / 72;
UNSUPPORTED("7t3liq77ckb4jbxzzkuvpenqu"); //     job->devscale.y = job->dpi.y / 72;
UNSUPPORTED("d3hdv8k8yieahljfu3qt853ud"); //     if ((job->flags & (1<<12)) || (Y_invert))
UNSUPPORTED("dhkndrjvudfzh9rih1bg17gz3"); // 	job->devscale.y *= -1;
UNSUPPORTED("7q82xx3mn6mih80ewhar8lg0g"); //     /* compute current view in graph units */
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("b9r25eu40lq0rhgqpis89rji8"); // 	job->view.y = job->width / job->scale.y;
UNSUPPORTED("6a6udj0vxgy2ylquj5qgi0v5y"); // 	job->view.x = job->height / job->scale.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("6v8ftcsv837e27gwxszngvin2"); // 	job->view.x = job->width / job->scale.x;
UNSUPPORTED("49fsot485en3t7beaufx5qicy"); // 	job->view.y = job->height / job->scale.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("du7kz5k6s1p26hnk6fqj746mu"); //     s = late_string(g, agattr(g, AGRAPH, "comment", 0), "");
UNSUPPORTED("5td7tli30j1zl1fgvmy30blpa"); //     gvrender_comment(job, s);
UNSUPPORTED("b6bxtnfqf846y3t3131e0p4d0"); //     job->layerNum = 0;
UNSUPPORTED("7ky2s7fpgwjuqrftlhyowbq7q"); //     emit_begin_graph(job, g);
UNSUPPORTED("9saz20f1su2m7mwo82xf6hqc7"); //     if (flags & (1<<1))
UNSUPPORTED("5ay1uey9mpe77501hacs5cu3c"); // 	emit_colors(job,g);
UNSUPPORTED("f36txx1tdgu50b4nkh20skis"); //     /* reset node state */
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("9jg6tdwrn007w9odzq2qc6zdy"); // 	ND_state(n) = 0;
UNSUPPORTED("1xhln25szyh2h4t7ccsprle9"); //     /* iterate layers */
UNSUPPORTED("4nvgbfw6y1185pj4e8h7daw79"); //     for (firstlayer(job,&lp); validlayer(job); nextlayer(job,&lp)) {
UNSUPPORTED("bn0m6u9dix50gpbajrfezpgua"); // 	if (numPhysicalLayers (job) > 1)
UNSUPPORTED("35ib30gs09z3uwjvjoju5mpm2"); // 	    gvrender_begin_layer(job);
UNSUPPORTED("3b9ro4joid217f74xv0vx23ox"); // 	/* iterate pages */
UNSUPPORTED("5h2vy2qmsot0bw099k6zh72gd"); // 	for (firstpage(job); validpage(job); nextpage(job))
UNSUPPORTED("f0wsoynbdz170hyw1cg27wfte"); // 	    emit_page(job, g);
UNSUPPORTED("bn0m6u9dix50gpbajrfezpgua"); // 	if (numPhysicalLayers (job) > 1)
UNSUPPORTED("2v3s6ab0rugrq7jsmzwh0cop0"); // 	    gvrender_end_layer(job);
UNSUPPORTED("3xbu5lb3fe5zgccq9vbjk3kln"); //     } 
UNSUPPORTED("580k35rndifpvngdh5ih6za8j"); //     emit_end_graph(job, g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8drr2f8zs3soqc2sosnio8pzh
// static void free_string_entry(Dict_t * dict, char *key, Dtdisc_t * disc) 
public static Object free_string_entry(Object... arg) {
UNSUPPORTED("5fw7dopfefym7l730mi9nupmb"); // static void free_string_entry(Dict_t * dict, char *key, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3a76j29jy9as91ppatk4njqfj"); //     free(key);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 bh276mpp135v0h4xcw5xq7dw0
// static Dict_t *strings


//1 b145hfewfupd0yod069hevlar
// static Dtdisc_t stringdict = 




//3 2akcqhxfjsryfaxqftz8ogp65
// int emit_once(char *str) 
public static Object emit_once(Object... arg) {
UNSUPPORTED("av9vowc3noulfodcy4y75cigq"); // int emit_once(char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9o1ba2pupi4m7jhoaypqirzvz"); //     if (strings == 0)
UNSUPPORTED("oaii1lps6xe7x781clc5iody"); // 	strings = dtopen(&stringdict, Dtoset);
UNSUPPORTED("a42qo364e33vq6xbaw4vwmfzy"); //     if (!(*(((Dt_t*)(strings))->searchf))((strings),(void*)(str),0000004)) {
UNSUPPORTED("a012inr2a6x936i3bmmkqbemf"); // 	(*(((Dt_t*)(strings))->searchf))((strings),(void*)(strdup(str)),0000001);
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 13d7ot7fdy4wdgkjwthvwzm4e
// void emit_once_reset(void) 
public static Object emit_once_reset(Object... arg) {
UNSUPPORTED("7ssgqzgkp5093mqxhkhuxum69"); // void emit_once_reset(void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("92fsshcviqb93ohg6shu769vw"); //     if (strings) {
UNSUPPORTED("czsk7hllpj8z00253zg9zwhd7"); // 	dtclose(strings);
UNSUPPORTED("eolwadtu14y3m949xmkvfm351"); // 	strings = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 elzykz6y67nj4wkw8ujszlav6
// static void emit_begin_cluster(GVJ_t * job, Agraph_t * sg) 
public static Object emit_begin_cluster(Object... arg) {
UNSUPPORTED("5hucwlu4giv8cmas625we7f9h"); // static void emit_begin_cluster(GVJ_t * job, Agraph_t * sg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8lamppsqs7nhiu5v8k1f5jfh2"); //     obj_state_t *obj;
UNSUPPORTED("1iexddadjo0w6fdgddatfx40s"); //     obj = push_obj_state(job);
UNSUPPORTED("n4boka8crqg5xqjpjbgcxln8"); //     obj->type = CLUSTER_OBJTYPE;
UNSUPPORTED("4xukb2axnj1vamt817cyruv0g"); //     obj->u.sg = sg;
UNSUPPORTED("7by2ra75cr7w394dpq9nmc5ft"); //     obj->emit_state = EMIT_CDRAW;
UNSUPPORTED("bl6tgzo7jj7jcpgtgg48l2agv"); //     initObjMapData (job, GD_label(sg), sg);
UNSUPPORTED("85xekl5v59s8nwopb1oyrehis"); //     gvrender_begin_cluster(job, sg);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 djiumpyzkrvkc510ygwc0qw82
// static void emit_end_cluster(GVJ_t * job, Agraph_t * g) 
public static Object emit_end_cluster(Object... arg) {
UNSUPPORTED("9dxd1b11ibgf7uvh7mf5mvzye"); // static void emit_end_cluster(GVJ_t * job, Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9k80dolt6qs55o0oinc7mvk7k"); //     gvrender_end_cluster(job, g);
UNSUPPORTED("39iamwq9cd9iv3d2iyiaq8gz9"); //     pop_obj_state(job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 560z6epwa1xr2jys0av7hf7e5
// void emit_clusters(GVJ_t * job, Agraph_t * g, int flags) 
public static Object emit_clusters(Object... arg) {
UNSUPPORTED("9xh1f0u1yb9m8ypftvkh8ix4c"); // void emit_clusters(GVJ_t * job, Agraph_t * g, int flags)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("18uvc5sri0zzjmzue0wwo1z3j"); //     int doPerim, c, istyle, filled;
UNSUPPORTED("en6q26cyrg17g6yd6el73b3ns"); //     pointf AF[4];
UNSUPPORTED("w9bbn26rmq9dc8ihryhse3pi"); //     char *color, *fillcolor, *pencolor, **style, *s;
UNSUPPORTED("5wyi0wasd5kecf6rhsqdjk7v2"); //     graph_t *sg;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("8lamppsqs7nhiu5v8k1f5jfh2"); //     obj_state_t *obj;
UNSUPPORTED("1ikkkd5pukqwffmu5mtak47js"); //     textlabel_t *lab;
UNSUPPORTED("eqepzr79q5src0e30wgplnm07"); //     int doAnchor;
UNSUPPORTED("75w3zx2oz7s1yf7arcxf48heo"); //     double penwidth;
UNSUPPORTED("bhtcyodd9jiazat6sqhp9pm4x"); //     char* clrs[2];
UNSUPPORTED("99d9j6m0161wdv2tu4wbf3ifi"); //     for (c = 1; c <= GD_n_cluster(g); c++) {
UNSUPPORTED("cuf43q4kl3kqgyuuxdqve1mqt"); // 	sg = GD_clust(g)[c];
UNSUPPORTED("75ua4s360ejy0ad5556y7rxc6"); // 	if (clust_in_layer(job, sg) == 0)
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("cf5aumm9peswnfjdghtniv57i"); // 	/* when mapping, detect events on clusters after sub_clusters */
UNSUPPORTED("7pfc87mgoy8xnsdd8w3r44gt4"); // 	if (flags & (1<<2))
UNSUPPORTED("8zezqxx7s7218o1z84t9283i1"); // 	    emit_clusters(job, sg, flags);
UNSUPPORTED("4griuzh66dk49to2z4vg4n58t"); // 	emit_begin_cluster(job, sg);
UNSUPPORTED("4afw3f56zovkl91einsaznlr1"); // 	obj = job->obj;
UNSUPPORTED("8njvdvq4f4flv48y83eoo9guh"); // 	doAnchor = (obj->url || obj->explicit_tooltip);
UNSUPPORTED("88mnm1u4w0ntiz5kynm02mfri"); // 	setColorScheme (agget (sg, "colorscheme"));
UNSUPPORTED("eazs637wvmi4mg6rzyohl77bn"); // 	if (doAnchor && !(flags & (1<<2))) {
UNSUPPORTED("4brof5tmiwuq5x63jh8whfeo0"); // 	    emit_map_rect(job, GD_bb(sg));
UNSUPPORTED("96wz4onwo8hv1dre15npyoncl"); // 	    gvrender_begin_anchor(job, obj->url, obj->tooltip, obj->target, obj->id);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("arpfq2ay8oyluwsz8s1wp6tp4"); // 	filled = 0;
UNSUPPORTED("7z8t2n0ov011yh7grn6tj8xl2"); // 	istyle = 0;
UNSUPPORTED("e1llbio24o8st183k6ivn3x0p"); // 	if ((style = checkClusterStyle(sg, &istyle))) {
UNSUPPORTED("5ijzig29e4ve6o6tmpypjijfc"); // 	    gvrender_set_style(job, style);
UNSUPPORTED("1jumnpbkw6xmpcroil8k5o5m8"); // 	    if (istyle & (1 << 0))
UNSUPPORTED("6w06em6l23suofe15du0wq9hb"); // 		filled = 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("17irg4x9jsg1ae0ueg2w7tyws"); // 	fillcolor = pencolor = 0;
UNSUPPORTED("5erty8ili3cu3xxgovmr7jyzp"); // 	if (GD_gui_state(sg) & (1<<0)) {
UNSUPPORTED("5ikpu0qi6d121h4pgoxr837nf"); // 	    pencolor = late_nnstring(sg, G_activepencolor, "#808080");
UNSUPPORTED("4k5huj86rwz99dqspibchklqc"); // 	    fillcolor = late_nnstring(sg, G_activefillcolor, "#fcfcfc");
UNSUPPORTED("5op945vn3c1cyxwov5p8rj33t"); // 	    filled = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("76bnhukou8byzgwwws7ab8b0k"); // 	else if (GD_gui_state(sg) & (1<<1)) {
UNSUPPORTED("3twy3v6vi0eeadoc4u1zlxb45"); // 	    pencolor = late_nnstring(sg, G_activepencolor, "#303030");
UNSUPPORTED("1osuzkag4yl1jf1yqr8to7q1x"); // 	    fillcolor = late_nnstring(sg, G_activefillcolor, "#e8e8e8");
UNSUPPORTED("5op945vn3c1cyxwov5p8rj33t"); // 	    filled = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("er04c856pdifd7e1lw1c2jtba"); // 	else if (GD_gui_state(sg) & (1<<3)) {
UNSUPPORTED("8kgp6s9d1pkgammp2cqy0lgz4"); // 	    pencolor = late_nnstring(sg, G_deletedpencolor, "#e0e0e0");
UNSUPPORTED("50zeczr79uvycx2cumnhd1tjx"); // 	    fillcolor = late_nnstring(sg, G_deletedfillcolor, "#f0f0f0");
UNSUPPORTED("5op945vn3c1cyxwov5p8rj33t"); // 	    filled = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6ziqrkwt111lwfmj2h8pp9244"); // 	else if (GD_gui_state(sg) & (1<<2)) {
UNSUPPORTED("5zrylqp2iq3cuxz4lzc0545c3"); // 	    pencolor = late_nnstring(sg, G_visitedpencolor, "#101010");
UNSUPPORTED("2ehq3py2ixs9yy4hteia4zne7"); // 	    fillcolor = late_nnstring(sg, G_visitedfillcolor, "#f8f8f8");
UNSUPPORTED("5op945vn3c1cyxwov5p8rj33t"); // 	    filled = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("8ame1huznbi4b3mfey57xdith"); // 	    if (((color = agget(sg, "color")) != 0) && color[0])
UNSUPPORTED("122n5atq0kvxnd5tgprr5fu8m"); // 		fillcolor = pencolor = color;
UNSUPPORTED("1zymwwt3solqac47a89h9z0im"); // 	    if (((color = agget(sg, "pencolor")) != 0) && color[0])
UNSUPPORTED("17hxm349yg7jla8miv14hwpgd"); // 		pencolor = color;
UNSUPPORTED("dlm2m16no4yo0v65zm3r6q273"); // 	    if (((color = agget(sg, "fillcolor")) != 0) && color[0])
UNSUPPORTED("4mfyuvfftu02jnxjtms8u89df"); // 		fillcolor = color;
UNSUPPORTED("ee02x4g8qa44xw1iktx466luf"); // 	    /* bgcolor is supported for backward compatability 
UNSUPPORTED("921qzbfipmjh6nvtnq08xm284"); // 	       if fill is set, fillcolor trumps bgcolor, so
UNSUPPORTED("tzyfjffroigby20366m64xps"); //                don't bother checking.
UNSUPPORTED("3wmft8410e1kee6p60k7wx3aa"); //                if gradient is set fillcolor trumps bgcolor
UNSUPPORTED("29edlge3wqyp1gs0vymtr5700"); //              */
UNSUPPORTED("dzc0yotcol1cuthw3y39cexc6"); // 	    if ((!filled || !fillcolor) && ((color = agget(sg, "bgcolor")) != 0) && color[0]) {
UNSUPPORTED("4mfyuvfftu02jnxjtms8u89df"); // 		fillcolor = color;
UNSUPPORTED("1obncd6x9bei9fl89du99yv6z"); // 	        filled = 1;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4onosby3tz5zsis6f7400lgv0"); // 	if (!pencolor) pencolor = "black";
UNSUPPORTED("5eg5vn1y14wludgfck0xaq3b9"); // 	if (!fillcolor) fillcolor = "lightgrey";
UNSUPPORTED("1xf5m2okbj7owt77vc19vmf8r"); // 	clrs[0] = NULL;
UNSUPPORTED("2atgu691bmn6h9jvk8lve5qzc"); // 	if (filled) {
UNSUPPORTED("1ldzvmymblz8y4a6idvyxoj5t"); // 	    float frac;
UNSUPPORTED("5dnga3gh00f4sv4fk1n2iqdgu"); // 	    if (findStopColor (fillcolor, clrs, &frac)) {
UNSUPPORTED("12wjuz2zq45txyp39hhco78xu"); //         	gvrender_set_fillcolor(job, clrs[0]);
UNSUPPORTED("5o23oun5dlazsaicyjj530pp"); // 		if (clrs[1]) 
UNSUPPORTED("a7gdknkeqzyql1xn5aou1tu3u"); // 		    gvrender_set_gradient_vals(job,clrs[1],late_int(sg,G_gradientangle,0,0), frac);
UNSUPPORTED("5v31mz0fdr0su096gqov41vyn"); // 		else 
UNSUPPORTED("bs5b6w27pwn1xz2vkqcvhvw13"); // 		    gvrender_set_gradient_vals(job,"black",late_int(sg,G_gradientangle,0,0), frac);
UNSUPPORTED("7m5m0tkcf8zs343oe1dddsxz6"); // 		if (istyle & (1 << 1))
UNSUPPORTED("5jf506rwz9snq4d6ozpjvg3yg"); // 		    filled = 3;
UNSUPPORTED("7rknc7r0egcn3cw68mrvgow3v"); // 	 	else
UNSUPPORTED("7bikp52v1ey2yil3rybx6nris"); // 		    filled = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("es2lu1zhy5wdeml1v1kmrcix3"); //         	gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6j0eo47d00ez70necc0iot40d"); // 	if (G_penwidth && ((s=agxget(sg,G_penwidth)) && s[0])) {
UNSUPPORTED("6wycyviq3z90tulx4rjwg7sw"); // 	    penwidth = late_double(sg, G_penwidth, 1.0, 0.0);
UNSUPPORTED("7mn8zfxwz2kendg9ewcomj2tv"); //             gvrender_set_penwidth(job, penwidth);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4z3pjxp0yl5bmzgwdor3xvrv7"); // 	if (istyle & (1 << 2)) {
UNSUPPORTED("mp3otb18t8y3o4bchg5z2idp"); // 	    if ((doPerim = late_int(sg, G_peripheries, 1, 0)) || filled) {
UNSUPPORTED("8si227ium48spncryy7frwynh"); // 		AF[0] = GD_bb(sg).LL;
UNSUPPORTED("d45bxs36if3jklht2eq00pcr3"); // 		AF[2] = GD_bb(sg).UR;
UNSUPPORTED("7pt3ynvpv9wcldm9uyl4hs9ud"); // 		AF[1].x = AF[2].x;
UNSUPPORTED("busa4gdiebsow4m6i91pei9dk"); // 		AF[1].y = AF[0].y;
UNSUPPORTED("6zog1wdj92i5gy18x1tgo7l8k"); // 		AF[3].x = AF[0].x;
UNSUPPORTED("56s58onhzgnly5ggdvt1nlqnr"); // 		AF[3].y = AF[2].y;
UNSUPPORTED("4jwde8ij6mapmpzufmz71uw3e"); // 		if (doPerim)
UNSUPPORTED("7xogzuqvqci68vd0h8mw3g3hc"); //     		    gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("3eca1kfk7d0usysqe2g25s9dt"); //         	    gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("ayr41ncx6835i1t6cvbbocmjt"); // 		round_corners(job, AF, 4, istyle, filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9yi0sfy5jinx301z6euefq1gx"); // 	else if (istyle & (1 << 6)) {
UNSUPPORTED("ax8y1utessq4dsj8ao6g7j2gw"); // 	    int rv;
UNSUPPORTED("4hludau9a2am1tjk0xhlnxfwy"); // 	    AF[0] = GD_bb(sg).LL;
UNSUPPORTED("4k77pmee44hsr6plcoi20dqdh"); // 	    AF[2] = GD_bb(sg).UR;
UNSUPPORTED("94xw5t69k3x4bncfo418rzk41"); // 	    AF[1].x = AF[2].x;
UNSUPPORTED("2wgbau6oi3x84fa5vbjpl4tys"); // 	    AF[1].y = AF[0].y;
UNSUPPORTED("drztvrhevx0zqyqw37lqbywwv"); // 	    AF[3].x = AF[0].x;
UNSUPPORTED("47fk1ddlzlenv8temw9o74dkm"); // 	    AF[3].y = AF[2].y;
UNSUPPORTED("d53rklwtw1a8c0ucfyief5w1w"); // 	    if (late_int(sg, G_peripheries, 1, 0) == 0)
UNSUPPORTED("63hseiu22ctgv9gdekam65t07"); //         	gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("1wbjeln7h70lig2gl98kt8rf5"); //     		gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("97vrs1f2jac8rfh3rnv1du0r7"); // 	    rv = stripedBox (job, AF, fillcolor, 0);
UNSUPPORTED("26uxy1rkzvmsisagoc5g7f9qc"); // 	    if (rv > 1)
UNSUPPORTED("8b71lckf2t9xe9fslu15tp484"); // 		agerr (AGPREV, "in cluster %s\n", agnameof(sg));
UNSUPPORTED("cahw6lsgg8xmfslugv8wqwbhc"); // 	    gvrender_box(job, GD_bb(sg), 0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("dgmim3t3r6y21whczevrqvhn1"); // 	    if (late_int(sg, G_peripheries, 1, 0)) {
UNSUPPORTED("1wbjeln7h70lig2gl98kt8rf5"); //     		gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("8lraxifxzs9ffrnk8a0ckeesw"); // 		gvrender_box(job, GD_bb(sg), filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7a977zitdpktoxwn8rbjif9ux"); // 	    else if (filled) {
UNSUPPORTED("63hseiu22ctgv9gdekam65t07"); //         	gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("8lraxifxzs9ffrnk8a0ckeesw"); // 		gvrender_box(job, GD_bb(sg), filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1j4mx2l2nl1oo3makbr3eyoxc"); // 	free (clrs[0]);
UNSUPPORTED("ecowjd4zwo4ew6jeu9evjiij9"); // 	if ((lab = GD_label(sg)))
UNSUPPORTED("2rvfkljzair3c2v8un739u8j"); // 	    emit_label(job, EMIT_CLABEL, lab);
UNSUPPORTED("7lbjljjzqr08zoe4tepd81ci0"); // 	if (doAnchor) {
UNSUPPORTED("33sr8o6eyia8xv39c3ax3i9lq"); // 	    if (flags & (1<<2)) {
UNSUPPORTED("7fheoklw2kecmkg0cc6gap5f4"); // 		emit_map_rect(job, GD_bb(sg));
UNSUPPORTED("b2xca1s1bd7tkr1vo1weazvq1"); // 		gvrender_begin_anchor(job, obj->url, obj->tooltip, obj->target, obj->id);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dtwdqlhxsk4vpro8m02pot0co"); // 	    gvrender_end_anchor(job);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("aayivxn33okuwnvn0iqa4066x"); // 	if (flags & (1<<3)) {
UNSUPPORTED("7pdiogu941nic4o67h7upbhm4"); // 	    for (n = agfstnode(sg); n; n = agnxtnode(sg, n)) {
UNSUPPORTED("1bzj2os22s6b3tf899bpkde6t"); // 		emit_node(job, n);
UNSUPPORTED("cvwdbwresv9dldcthcbnyp9kk"); // 		for (e = agfstout(sg, n); e; e = agnxtout(sg, e))
UNSUPPORTED("auj2mo2tz3uf10zjkza14e82k"); // 		    emit_edge(job, e);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cv3hy45odqvlvwcjdyr6spm3h"); // 	emit_end_cluster(job, g);
UNSUPPORTED("40fwf06hlkuo7isg6mip8g7el"); // 	/* when drawing, lay down clusters before sub_clusters */
UNSUPPORTED("3i5ezqfabmzliv96rwzf0rp37"); // 	if (!(flags & (1<<2)))
UNSUPPORTED("8zezqxx7s7218o1z84t9283i1"); // 	    emit_clusters(job, sg, flags);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c4mtnyvuir2cais09r4vk4xwa
// static boolean is_style_delim(int c) 
public static Object is_style_delim(Object... arg) {
UNSUPPORTED("8mez49lbay1n6ghwk9q96arp5"); // static boolean is_style_delim(int c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8amt8fmqdipygnxirowfbawox"); //     switch (c) {
UNSUPPORTED("71si918orgtw88hf3ef5zmsur"); //     case '(':
UNSUPPORTED("6w345tkzwww6jqwaqdi7yww9s"); //     case ')':
UNSUPPORTED("d7tc6f9496ufckp546jd19usk"); //     case ',':
UNSUPPORTED("1luutzji7osg8esr8b5j2cmsr"); //     case '\0':
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ciz01t6xlpxu9dny1f4zwb45n
// static int style_token(char **s, agxbuf * xb) 
public static Object style_token(Object... arg) {
UNSUPPORTED("28oqkocgew358xzz1ws4vsuz4"); // static int style_token(char **s, agxbuf * xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8m3k758dcwwz4y8dqzc62remf"); //     char *p = *s;
UNSUPPORTED("eyxuxcumfs3lh2yyd55uy6v7w"); //     int token, rc;
UNSUPPORTED("wrvu9u7a8j6i6y6552zncxfk"); //     char c;
UNSUPPORTED("e5ufot6klnpqffix37q98elea"); //     while (*p && (isspace(*p) || (*p == ',')))
UNSUPPORTED("2wdgcrx402aszs54rq2kh9txd"); // 	p++;
UNSUPPORTED("ajkn8nsofzadrqa5lz0j3dil2"); //     switch (*p) {
UNSUPPORTED("1luutzji7osg8esr8b5j2cmsr"); //     case '\0':
UNSUPPORTED("dmnwmwojsxusa7q5qt23h5zmg"); // 	token = 0;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("71si918orgtw88hf3ef5zmsur"); //     case '(':
UNSUPPORTED("6w345tkzwww6jqwaqdi7yww9s"); //     case ')':
UNSUPPORTED("eksinwup1v6a3d98byb38snby"); // 	token = *p++;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("4cdl2v54v6i2dni2p09haa90i"); // 	token = 1;
UNSUPPORTED("chgfiktm89qx6plbkcedgxntv"); // 	while (!is_style_delim(c = *p)) {
UNSUPPORTED("44fjmben1pin1nau1r4s4b1jc"); // 	    rc = ((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3kblfzc6j01qhc67wzeuk2oam"); //     *s = p;
UNSUPPORTED("dzxpp1eh4dn7ckzehm8pbp5r5"); //     return token;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 5rjzz2a1wogd5wh9oa5o1fofa
// static unsigned char outbuf[128]


//1 adi2polpq88o43a9ixp81gazu
// static agxbuf ps_xb




//3 31nqh76a91ewgfgc421tjgnxd
// char **parse_style(char *s) 
public static Object parse_style(Object... arg) {
UNSUPPORTED("e5cy7r0xn364flrgake76xt8i"); // char **parse_style(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3vpphojcmf2gtvenbux84m8z9"); //     static char *parse[64];
UNSUPPORTED("3gkp456k9uixnbt0hgaw7oeep"); //     static boolean is_first = NOT(0);
UNSUPPORTED("ap64a3u5ezb8lo8thseajc887"); //     int fun = 0;
UNSUPPORTED("ci8semxrw11syu6su3skyfkoa"); //     boolean in_parens = 0;
UNSUPPORTED("h0or3v13348vfl22jqz895yc"); //     unsigned char buf[128];
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("ew6jw9dmd6hait9mgj7ouhmmr"); //     if (is_first) {
UNSUPPORTED("4m5jd5elxj32gsmed58fjnfcr"); // 	agxbinit(&ps_xb, 128, outbuf);
UNSUPPORTED("43wu8jjb66y01a3hlgjrjryh1"); // 	is_first = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ci65k77x1b3nq6luu69s87oup"); //     agxbinit(&xb, 128, buf);
UNSUPPORTED("7qb9c0pzh9hqyptqv00luagac"); //     p = s;
UNSUPPORTED("f242flcqmvdk48iozm6utnd21"); //     while ((c = style_token(&p, &xb)) != 0) {
UNSUPPORTED("7rk995hpmaqbbasmi40mqg0yw"); // 	switch (c) {
UNSUPPORTED("ej3xfo9vi5946bx4zdvzne2rq"); // 	case '(':
UNSUPPORTED("apbqdu2kbx5fo2pqipxe7a1dq"); // 	    if (in_parens) {
UNSUPPORTED("5gru9bi76isvm0xludlszwnh1"); // 		agerr(AGERR, "nesting not allowed in style: %s\n", s);
UNSUPPORTED("2zjdsh2kepmo5yfkli65ofxe1"); // 		parse[0] = (char *) 0;
UNSUPPORTED("97nzy67m5eyfd9a6d1montbam"); // 		agxbfree(&xb);
UNSUPPORTED("6c4u745gvkt06ky00z0po4obm"); // 		return parse;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("iod77t2iafvqv6ivwpk389b4"); // 	    in_parens = NOT(0);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("6740d5ahrznermt1b0z9i00yc"); // 	case ')':
UNSUPPORTED("83ahjzhawu24k7hgmo221pz3k"); // 	    if (in_parens == 0) {
UNSUPPORTED("9ymlk0ikhvtvhj6ke4b2tooxg"); // 		agerr(AGERR, "unmatched ')' in style: %s\n", s);
UNSUPPORTED("2zjdsh2kepmo5yfkli65ofxe1"); // 		parse[0] = (char *) 0;
UNSUPPORTED("97nzy67m5eyfd9a6d1montbam"); // 		agxbfree(&xb);
UNSUPPORTED("6c4u745gvkt06ky00z0po4obm"); // 		return parse;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9yik55atrk3v32zihswkxoepa"); // 	    in_parens = 0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("83ahjzhawu24k7hgmo221pz3k"); // 	    if (in_parens == 0) {
UNSUPPORTED("197h1mowju7n7bgy7wx0jpyyp"); // 		if (fun == 64 - 1) {
UNSUPPORTED("47cw8s3z99bcrten2c3js5q7u"); // 		    agerr(AGWARN, "truncating style '%s'\n", s);
UNSUPPORTED("2pxlo0tqwhg51d3v8r33x5vlm"); // 		    parse[fun] = (char *) 0;
UNSUPPORTED("pjkyn5638fsqi3xazu3sd2ce"); // 		    agxbfree(&xb);
UNSUPPORTED("dedftzivffm91rj6yyibb1jrh"); // 		    return parse;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("4h5li2e1ckwp4ztkl6ur4hjvr"); // 		((((&ps_xb)->ptr >= (&ps_xb)->eptr) ? agxbmore(&ps_xb,1) : 0), (int)(*(&ps_xb)->ptr++ = ((unsigned char)'\0')));	/* terminate previous */
UNSUPPORTED("czp2gx1ewvuil58eqg7muqbp3"); // 		parse[fun++] = ((char*)((&ps_xb)->ptr));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ex1k1y0wj1bw5fju2ynw8wztq"); // 	    agxbput(&ps_xb, (((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)));
UNSUPPORTED("8rvrgugqqjus18zwf0flkmyg5"); // 	    ((((&ps_xb)->ptr >= (&ps_xb)->eptr) ? agxbmore(&ps_xb,1) : 0), (int)(*(&ps_xb)->ptr++ = ((unsigned char)'\0')));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("eh4399dy51qz9v5k46j7u7f0b"); //     if (in_parens) {
UNSUPPORTED("a9afc2y8uauucn25hpjrq0oxx"); // 	agerr(AGERR, "unmatched '(' in style: %s\n", s);
UNSUPPORTED("dtv4q4ijbxgbp2d77nklhf35w"); // 	parse[0] = (char *) 0;
UNSUPPORTED("52frst03mcbsuu6bmnqs962qm"); // 	agxbfree(&xb);
UNSUPPORTED("415n1ugoxgttbkl3ewfv6oqts"); // 	return parse;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ajac1de3cpgvymee2hqm7cqi2"); //     parse[fun] = (char *) 0;
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("9tjoh4elqwg3obcqij1u484gf"); //     (void)(((((&ps_xb)->ptr >= (&ps_xb)->eptr) ? agxbmore(&ps_xb,1) : 0), (int)(*(&ps_xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&ps_xb)->ptr = (&ps_xb)->buf));		/* adds final '\0' to buffer */
UNSUPPORTED("be22l01ejxdw3x933qiz2eaon"); //     return parse;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ck3omsgy3q0zlazix8r2dmufz
// static boxf bezier_bb(bezier bz) 
public static Object bezier_bb(Object... arg) {
UNSUPPORTED("4x8to7ipsnszk7hvp8b6us95m"); // static boxf bezier_bb(bezier bz)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("b4p0empvxghe2amctuezw2fj3"); //     pointf p, p1, p2;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("du76etbklizf0ujj6epooc4u3"); //     assert(bz.size > 0);
UNSUPPORTED("ch7u0adkg05jhr4w0rc7h1fhk"); //     assert(bz.size % 3 == 1);
UNSUPPORTED("1ecqftepbszmmeix7g8yerzho"); //     bb.LL = bb.UR = bz.list[0];
UNSUPPORTED("67q9vbylk5li7nl6u0g87ew09"); //     for (i = 1; i < bz.size;) {
UNSUPPORTED("6lezptnqjziit71m8utngn9ec"); // 	/* take mid-point between two control points for bb calculation */
UNSUPPORTED("bcrmrqurd18pu7bb549lkj2zm"); // 	p1=bz.list[i];
UNSUPPORTED("chd2f5z6rt19lbaye25ej7q6j"); // 	i++;
UNSUPPORTED("f1w8uf97bnvwem92r7cka4vh2"); // 	p2=bz.list[i];
UNSUPPORTED("chd2f5z6rt19lbaye25ej7q6j"); // 	i++;
UNSUPPORTED("djwxlpeud1dn44xcrj6ndqzur"); // 	p.x = ( p1.x + p2.x ) / 2;
UNSUPPORTED("626yu2abpm7sgxs2a6ojagloa"); // 	p.y = ( p1.y + p2.y ) / 2;
UNSUPPORTED("a7j5rlajnoe7gj2d4vp29dlq3"); //         EXPANDBP(bb,p);
UNSUPPORTED("bg1txe7hn8ps3rf82tirvfik0"); // 	p=bz.list[i];
UNSUPPORTED("a7j5rlajnoe7gj2d4vp29dlq3"); //         EXPANDBP(bb,p);
UNSUPPORTED("chd2f5z6rt19lbaye25ej7q6j"); // 	i++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 mmp96ytye8ub6txmw93m4vel
// static void init_splines_bb(splines *spl) 
public static Object init_splines_bb(Object... arg) {
UNSUPPORTED("c3y3jo1ro3rw4jxe8wllr42wd"); // static void init_splines_bb(splines *spl)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("37thdceezsvepe7tlyfatrbcw"); //     bezier bz;
UNSUPPORTED("dljqqka7giae8b01dcwrse8yc"); //     boxf bb, b;
UNSUPPORTED("e0qqavuetiwg28hno3n4emxw0"); //     assert(spl->size > 0);
UNSUPPORTED("2hacb9fdt2n4mkiveluxkiu2e"); //     bz = spl->list[0];
UNSUPPORTED("3dpg7yydfzt8n9mt7n873m4bx"); //     bb = bezier_bb(bz);
UNSUPPORTED("4z4l1zu33m72iirxmuhdg36iw"); //     for (i = 0; i < spl->size; i++) {
UNSUPPORTED("1e2qc8e42f9ohrllk7q5kbqh9"); //         if (i > 0) {
UNSUPPORTED("75bvkqcdmtm8cgnu9vvehmsjq"); //             bz = spl->list[i];
UNSUPPORTED("7r4akm44y65pzpvui9670oihh"); //             b = bezier_bb(bz);
UNSUPPORTED("3vlk6ne4cz4fhhm5a6o0buaiy"); //             EXPANDBB(bb, b);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5uvyqkv0zlq1pvknbc0xbocwu"); //         if (bz.sflag) {
UNSUPPORTED("8ee2v0q9etbnezmd0792omuyg"); //             b = arrow_bb(bz.sp, bz.list[0], 1, bz.sflag);
UNSUPPORTED("3vlk6ne4cz4fhhm5a6o0buaiy"); //             EXPANDBB(bb, b);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("d2m398qqli1zbrdk57sgrhwjf"); //         if (bz.eflag) {
UNSUPPORTED("w3y4iuwxuffqu27zjsr7v6zs"); //             b = arrow_bb(bz.ep, bz.list[bz.size - 1], 1, bz.eflag);
UNSUPPORTED("3vlk6ne4cz4fhhm5a6o0buaiy"); //             EXPANDBB(bb, b);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2mlym7h1a7wx8a5v8uwfgt2ve"); //     spl->bb = bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 56w9yqsde88lk1salsdo9zhek
// static void init_bb_edge(edge_t *e) 
public static Object init_bb_edge(Object... arg) {
UNSUPPORTED("czoe4mrvfnlkgkf9dd5gswdun"); // static void init_bb_edge(edge_t *e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6tl9mepc2bett364jduh2q4mf"); //     splines *spl;
UNSUPPORTED("2c3cg84bl0xam4mk6g5f31jj0"); //     spl = ED_spl(e);
UNSUPPORTED("cnmg8m65mfhsyyzyck7jhyf0v"); //     if (spl)
UNSUPPORTED("5vuo8sxknwrhunmoccygtuuvr"); //         init_splines_bb(spl);
UNSUPPORTED("2o9ao5unxwbngk1qd0p8whpeg"); // //    lp = ED_label(e);
UNSUPPORTED("9g4o5rsb2iu810alwb470phyr"); // //    if (lp)
UNSUPPORTED("3h79hsuz9ndp04mum4ile2v9k"); // //        {}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ciznhkb6ca1q6pevo943ekme0
// static void init_bb_node(graph_t *g, node_t *n) 
public static Object init_bb_node(Object... arg) {
UNSUPPORTED("9eic9chg3e24i3qt1eqh1kglr"); // static void init_bb_node(graph_t *g, node_t *n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("a20p0a7nmewnhy041mtj2osk3"); //     ND_bb(n).LL.x = ND_coord(n).x - ND_lw(n);
UNSUPPORTED("c2n18y1ykeeclakw3paasxuvw"); //     ND_bb(n).LL.y = ND_coord(n).y - ND_ht(n) / 2.;
UNSUPPORTED("cd1j2q3oehrg8vdh5mc2y15n3"); //     ND_bb(n).UR.x = ND_coord(n).x + ND_rw(n);
UNSUPPORTED("dtlwzpd98hya2ssejmz7kfy75"); //     ND_bb(n).UR.y = ND_coord(n).y + ND_ht(n) / 2.;
UNSUPPORTED("azojsmj2w5kkwset562797bbz"); //     for (e = agfstout(g, n); e; e = agnxtout(g, e))
UNSUPPORTED("7ubpqzgmjcyyknlapquyjzh46"); //         init_bb_edge(e);
UNSUPPORTED("a9q4nnyheflcgogy9pvb7zhe9"); //     /* IDEA - could also save in the node the bb of the node and
UNSUPPORTED("ap39xkzymz5jtyyjrlh4oh7jz"); //     all of its outedges, then the scan time would be proportional
UNSUPPORTED("p6lfyicqm841ng07jzfz6erf"); //     to just the number of nodes for many graphs.
UNSUPPORTED("7is89igk57j6xxcbdvbmswq98"); //     Wouldn't work so well if the edges are sprawling all over the place
UNSUPPORTED("2x1b3m9zalxzp45ojidztaq43"); //     because then the boxes would overlap a lot and require more tests,
UNSUPPORTED("18qw5xpzps91vgn0jsv7bjzi0"); //     but perhaps that wouldn't add much to the cost before trying individual
UNSUPPORTED("43p4cwx9g5a9grh509zne19l"); //     nodes and edges. */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 rpxf4np5e0zbdxhp8wqxwjru
// static void init_bb(graph_t *g) 
public static Object init_bb(Object... arg) {
UNSUPPORTED("4c9b9a1jd95zvo8rew7l4rdw2"); // static void init_bb(graph_t *g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("85uc0svzwbkhdrn713tjitqh5"); //         init_bb_node(g, n);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 5cwzfc0p4amr0ryx8p6dllo42
// extern gvevent_key_binding_t gvevent_key_binding[]


//1 d1twtwndjbh6p8fkxukwz6gh8
// extern int gvevent_key_binding_size


//1 7w0woib3eaxzhaeksz12t3814
// extern gvdevice_callbacks_t gvdevice_callbacks




//3 31vgctm6ydke1b6e0s06x85og
// void gv_fixLocale (int set) 
public static void gv_fixLocale(int set) {
ENTERING("31vgctm6ydke1b6e0s06x85og","gv_fixLocale");
try {
	// System.out.println("SKIPPING gv_fixLocale");
} finally {
LEAVING("31vgctm6ydke1b6e0s06x85og","gv_fixLocale");
}
}




//3 9elgfx2mwaal8mp8yu4i8x55g
// int gvRenderJobs (GVC_t * gvc, graph_t * g) 
public static Object gvRenderJobs(Object... arg) {
UNSUPPORTED("al6r85bl2757m64h0be99kb2v"); // int gvRenderJobs (GVC_t * gvc, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8hzx29jh0v2eh3jdrvrguy5mn"); //     static GVJ_t *prevjob;
UNSUPPORTED("88ctjz58veqptrabz1ddnfvaf"); //     GVJ_t *job, *firstjob;
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("aaarjibm8djv3koq2y6u4m044"); // 	start_timer();
UNSUPPORTED("c2dyme9nsswgb9qb96oh2m08s"); //     if (!(agbindrec(g, "Agraphinfo_t", 0, NOT(0)) && GD_drawing(g))) {
UNSUPPORTED("aix30bpzzydybozpwmqe2bf25"); //         agerr (AGERR, "Layout was not done.  Missing layout plugins? \n");
UNSUPPORTED("881fnyqz9k0o6nu5p5pf6n7ka"); // 	if (Verbose) fprintf(stderr,"gvRenderJobs %s: %.2f secs.\n", agnameof(g), elapsed_sec());
UNSUPPORTED("f3a98gxettwtewduvje9y3524"); //         return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8tzaifmwoislyhju3smc3zukv"); //     init_bb(g);
UNSUPPORTED("83mub13eiupttlu3d4j5lvj5p"); //     init_gvc(gvc, g);
UNSUPPORTED("1qo78wc1zt5cptfpywfwemtjj"); //     init_layering(gvc, g);
UNSUPPORTED("98b2uqnwq3rgamma244bsbrt1"); //     gv_fixLocale (1);
UNSUPPORTED("52t975anlz3qwnjcfxc5ok1at"); //     for (job = gvjobs_first(gvc); job; job = gvjobs_next(gvc)) {
UNSUPPORTED("6hzx763hhvpbtsvjf8aud3axf"); // 	if (gvc->gvg) {
UNSUPPORTED("ckzfglt7a4q9ca1ek9xm04vf5"); // 	    job->input_filename = gvc->gvg->input_filename;
UNSUPPORTED("5gsifqa9895ltjbs6fiwkoqup"); // 	    job->graph_index = gvc->gvg->graph_index;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("bv62u1zxiszyu0z7y97u4p0sq"); // 	    job->input_filename = NULL;
UNSUPPORTED("84w8f8lfynhieu4ce6k6q0f7o"); // 	    job->graph_index = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("752jskfqikhifji0h9luxjt5p"); // 	job->common = &(gvc->common);
UNSUPPORTED("2k3vzw6p3k1ruqmf5ckjkq8ss"); // 	job->layout_type = gvc->layout.type;
UNSUPPORTED("5j4wuo1sqs572ms6xud4w1vnj"); // 	job->keybindings = gvevent_key_binding;
UNSUPPORTED("3ev58alw9vec8e3ekuvfalsiw"); // 	job->numkeys = gvevent_key_binding_size;
UNSUPPORTED("5e56auki36a9c8a9jxaig2dtn"); // 	if (!GD_drawing(g)) {
UNSUPPORTED("7p3ojgx61zomxbqv96sv65491"); // 	    agerr (AGERR, "layout was not done\n");
UNSUPPORTED("1li5h7sm29md39a6rbj983auz"); // 	    gv_fixLocale (0);
UNSUPPORTED("1i6w37q3xx7w76ft9lu49wl53"); // 	    if (Verbose) fprintf(stderr,"gvRenderJobs %s: %.2f secs.\n", agnameof(g), elapsed_sec());
UNSUPPORTED("aivfd7ajlfz8o8oi68d4u5s5z"); // 	    return -1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("xa3la9zayp2j503lislwf8bw"); //         job->output_lang = gvrender_select(job, job->output_langname);
UNSUPPORTED("8mhtshj21zmdehr49crz92ubk"); //         if (job->output_lang == 999) {
UNSUPPORTED("74iiov8kfwdwwftrvbuhx0uar"); //             agerr (AGERR, "renderer for %s is unavailable\n", job->output_langname);
UNSUPPORTED("1li5h7sm29md39a6rbj983auz"); // 	    gv_fixLocale (0);
UNSUPPORTED("1i6w37q3xx7w76ft9lu49wl53"); // 	    if (Verbose) fprintf(stderr,"gvRenderJobs %s: %.2f secs.\n", agnameof(g), elapsed_sec());
UNSUPPORTED("62ko03w39aomt1h9y758mag0k"); //             return -1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("ampzlf94vv6frsr0uc6d017f6"); //         switch (job->output_lang) {
UNSUPPORTED("14mysa63mq9rkffdt98zlyxsl"); //         case 21:
UNSUPPORTED("apkae0nhyzfihcum8kdea13xc"); //             /* output sorted, i.e. all nodes then all edges */
UNSUPPORTED("ci29ynt7cueaf8jqpqstm7gm7"); //             job->flags |= (1<<0);
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("ph1r5pdbnaj90pytg37mdsoy"); //         case 24:
UNSUPPORTED("70rmrtuyb914znb7mrak6xhuk"); //             /* output in preorder traversal of the graph */
UNSUPPORTED("cc3065soln8us3k43ovq935gw"); //             job->flags |= (1<<3)
UNSUPPORTED("c5oeo2n9siifh7xp8im8oy9i6"); // 		       | (1<<9);
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("p0mt8wznalavjdm44ot4ykl7"); //         default:
UNSUPPORTED("d8cu5xolc31bwzqz3dmt12i74"); //             job->flags |= chkOrder(g);
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("3yuatt6c28xj5dvgsxflopn9c"); // 	/* if we already have an active job list and the device doesn't support mutiple output files, or we are about to write to a different output device */
UNSUPPORTED("b6or2ku9xlp5adhzyfj6ggg74"); //         firstjob = gvc->active_jobs;
UNSUPPORTED("kkqmd3s54yqyxuy9ujfd6bp8"); //         if (firstjob) {
UNSUPPORTED("bkayjk7mhfk0z1prvegopn9f2"); // 	    if (! (firstjob->flags & (1<<5))
UNSUPPORTED("7rq6o3m7v5g2jxfc61r9529oz"); // 	      || (strcmp(job->output_langname,firstjob->output_langname))) {
UNSUPPORTED("9ciig60i0ebtgpx2rjem515x2"); // 	        gvrender_end_job(firstjob);
UNSUPPORTED("6qee6pctu0pao210s569zuhhx"); //             	gvc->active_jobs = NULL; /* clear active list */
UNSUPPORTED("6rrb76y5gxl4tyyy39ju8vsqh"); // 	    	gvc->common.viewNum = 0;
UNSUPPORTED("237aavazed82p3dahdkyqne7v"); // 	    	prevjob = NULL;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("3jir07ymknf0hmb9pv9x4dr3o"); //         else {
UNSUPPORTED("21ah75bxrqgvsk8az3ejvz9xm"); // 	    prevjob = NULL;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("eq1quwlwab222bf12rqzylabz"); // 	if (prevjob) {
UNSUPPORTED("7lbeby5mwfpebpi6oat7oziry"); //             prevjob->next_active = job;  /* insert job in active list */
UNSUPPORTED("92fdsdu2rxr0mo9vqr7xw6ivn"); // 	    job->output_file = prevjob->output_file;  /* FIXME - this is dumb ! */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("a3ph0rnir8dxbkuydjfk32hyf"); // 	    if (gvrender_begin_job(job))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("dhzmccdky20j8px8fj86coz1s"); // 	    gvc->active_jobs = job;   /* first job of new list */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ec054g5x4n09tjvekoarr5lj2"); // 	job->next_active = NULL;      /* terminate active list */
UNSUPPORTED("a3g0lqkbu5x3tp31mzdb9z1a1"); // 	job->callbacks = &gvdevice_callbacks;
UNSUPPORTED("91r9tszzf49cistgb3dx6c5cv"); // 	init_job_pad(job);
UNSUPPORTED("1b3piu56i8aqmewokmp76n4ov"); // 	init_job_margin(job);
UNSUPPORTED("c30lbf0gqt0nfgjngfkbrkh1u"); // 	init_job_dpi(job, g);
UNSUPPORTED("6719w8ckdbr5o57x6eoomwwqf"); // 	init_job_viewport(job, g);
UNSUPPORTED("12oknjh0if0kwauc10p58ooj6"); // 	init_job_pagination(job, g);
UNSUPPORTED("dhiros11nue53w4sfjg764f66"); // 	if (! (job->flags & (1<<7))) {
UNSUPPORTED("au82zdbwbf11c4dhp3j9goiuj"); // 	    emit_graph(job, g);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7wrg45h3f9gl77k72io059dov"); //         /* the last job, after all input graphs are processed,
UNSUPPORTED("b2u8zjdvgah99yawh4ppdgfxk"); //          *      is finalized from gvFinalize()
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("9g4mahhmb798zgagmhs69g7o5"); // 	prevjob = job;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bkwnwkliqxeb0zv9leq3mcdrx"); //     gv_fixLocale (0);
UNSUPPORTED("et6g7m7r8rj6f8qhvpz93lqxs"); //     if (Verbose) fprintf(stderr,"gvRenderJobs %s: %.2f secs.\n", agnameof(g), elapsed_sec());
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a3vxhyyu69l0qyuv5lmbs3mhh
// boolean findStopColor (char* colorlist, char* clrs[2], float* frac) 
public static Object findStopColor(Object... arg) {
UNSUPPORTED("4an2je0tp9mryy4qaly9pg51w"); // boolean findStopColor (char* colorlist, char* clrs[2], float* frac)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8z3a2qjysx0m326m5qjy91tnu"); //     colorsegs_t* segs;
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("5h68n5d5dxbzm77fsc9jacogv"); //     rv = parseSegs (colorlist, 0, &segs);
UNSUPPORTED("dtjj3ng1i6w3ypa53dix5k2bp"); //     if (rv || (segs->numc < 2) || (segs->segs[0].color == NULL)) {
UNSUPPORTED("1xf5m2okbj7owt77vc19vmf8r"); // 	clrs[0] = NULL;
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2y5g21fon514qpa8eaqvqil0b"); //     if (segs->numc > 2)
UNSUPPORTED("8r2xfo7zhrx0y02i0g0nmmb8v"); // 	agerr (AGWARN, "More than 2 colors specified for a gradient - ignoring remaining\n");
UNSUPPORTED("9qa66u5mlsn5qyltb3mz8tv6x"); //     clrs[0] = (char*)gmalloc((strlen(colorlist)+1)*sizeof(char)); 
UNSUPPORTED("6wkfmz13gix78yhxfl4wu387t"); //     strcpy (clrs[0], segs->segs[0].color);
UNSUPPORTED("346qdfuvl40mzq9b0c1jmxrph"); //     if (segs->segs[1].color) {
UNSUPPORTED("17tzhs6hjalzhgf9x6bjt9a21"); // 	clrs[1] = clrs[0] + (strlen(clrs[0])+1);
UNSUPPORTED("78iakjpvgfjfvohpfjme8wm3p"); // 	strcpy (clrs[1], segs->segs[1].color);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("8adbeboqco76ee1xfw2yotsrc"); // 	clrs[1] = NULL;
UNSUPPORTED("6srhlc05d53hy25ewfxz9k5zg"); //     if (segs->segs[0].hasFraction)
UNSUPPORTED("6b4phzvf6fhvg9dib574qa5zl"); // 	*frac = segs->segs[0].t;
UNSUPPORTED("95s3rw3vuxb43fouesg4994w9"); //     else if (segs->segs[1].hasFraction)
UNSUPPORTED("9vkv3aohcusc7x3qflm8ode9m"); // 	*frac = 1 - segs->segs[1].t;
UNSUPPORTED("4lti1w2qslxj3ihoatmbavsfr"); //     else 
UNSUPPORTED("dgf6ly3jfdezgto7fetu7v2nj"); // 	*frac = 0;
UNSUPPORTED("dwbftn5fib1yir7gshhy14cup"); //     freeSegs (segs);
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
