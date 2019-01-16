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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agraph_s;
import h.ST_aspect_t;
import h.ST_pointf;
import smetana.core.CString;

public class aspect__c {
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


//1 buo6aw1j4uziggaugy984eotj
// static nodeGroup_t *nodeGroups


//1 8pcclsxg8w461gulo0pvihkjp
// static int nNodeGroups = 0




//3 e1ii1fk8ce43zhjwticczdewi
// static void computeNodeGroups(graph_t * g) 
public static Object computeNodeGroups(Object... arg) {
UNSUPPORTED("85tw6ni78b2fdpbmcvfkfjj2g"); // static void computeNodeGroups(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("41zdayra08bo7qofh0t0g44tj"); //     nodeGroups = (nodeGroup_t*)gmalloc((agnnodes(g))*sizeof(nodeGroup_t));
UNSUPPORTED("751hgbeopd6kx0brs46ydl3nk"); //     nNodeGroups = 0;
UNSUPPORTED("bhhgxkf7jt2bkoqqybblectmc"); //     /* initialize node ids. Id of a node is used as an index to the group. */
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("c29pxxzew8wpbooylabmstb6k"); // 	ND_id(n) = -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("856fgjw3c9mfzqbqcelccqz6i"); // 	if (ND_UF_size(n) == 0) {	/* no same ranking constraint */
UNSUPPORTED("995nygsph6blhs7u3roc320vt"); // 	    nodeGroups[nNodeGroups].nodes = (node_t **)zmalloc(sizeof(node_t *));
UNSUPPORTED("yecg6kqqsu0dce5l7yl9nz0v"); // 	    nodeGroups[nNodeGroups].nodes[0] = n;
UNSUPPORTED("48boktg0q0ftjks7g2ugmcgr0"); // 	    nodeGroups[nNodeGroups].nNodes = 1;
UNSUPPORTED("asru03zwy8elrmkb54ceapyf5"); // 	    nodeGroups[nNodeGroups].width = ND_width(n);
UNSUPPORTED("63tq5olg8fmmqnngdsd2m0p7y"); // 	    nodeGroups[nNodeGroups].height = ND_height(n);
UNSUPPORTED("2jtn1zrlba2gjw9o8ijqfjd8l"); // 	    ND_id(n) = nNodeGroups;
UNSUPPORTED("26stq7kxzhcw4bdbet988ue9u"); // 	    nNodeGroups++;
UNSUPPORTED("ek3ylp6qgnzrlvktaorzarkl6"); // 	} else			/* group same ranked nodes */
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("2xfdbwqwkydzc27ndexkdqpk3"); // 	    node_t *l = UF_find(n);
UNSUPPORTED("9k1apjnlyvc0imkojboqq56t"); // 	    if (ND_id(l) > -1)	/* leader is already grouped */
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("1dxpo17pe23wsyc88ervx5x7n"); // 		int index = ND_id(l);
UNSUPPORTED("a3rj9avq06zyyeiex95z1m5qs"); // 		nodeGroups[index].nodes[nodeGroups[index].nNodes++] = n;
UNSUPPORTED("4qloaqbsyg2krphzbyu8l502n"); // 		nodeGroups[index].width += ND_width(n);
UNSUPPORTED("7oxuv0m39o7l9yf15yjhvw804"); // 		nodeGroups[index].height =
UNSUPPORTED("f301cq3rurukx2hdrzu2rvioq"); // 		    (nodeGroups[index].height <
UNSUPPORTED("8oi4o7ku7aqcurrfujz4p5qkb"); // 		     ND_height(n)) ? ND_height(n) : nodeGroups[index].
UNSUPPORTED("bkhrhsn74c58qx4h0val1pmjp"); // 		    height;
UNSUPPORTED("7me4qfeob3i8uvnfzwktfincy"); // 		ND_id(n) = index;
UNSUPPORTED("d6w1jxu5j3ja7tpl4hcag7fv0"); // 	    } else		/* create a new group */
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("2l80so4pkfhpik5o2c4xb7cff"); // 		nodeGroups[nNodeGroups].nodes =
UNSUPPORTED("3ey9wbuwiu5f6tykjlpo26lhj"); // 		    (node_t **)zmalloc((ND_UF_size(l))*sizeof(node_t *));
UNSUPPORTED("53ell10epajj2omj9xiynycta"); // 		if (l == n)	/* node n is the leader */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("7m6pmc4ajzo9gva5pgvtljwf3"); // 		    nodeGroups[nNodeGroups].nodes[0] = l;
UNSUPPORTED("1n4ihvxa7g9rlwmex1up2728t"); // 		    nodeGroups[nNodeGroups].nNodes = 1;
UNSUPPORTED("a8p3badahan60lefgn5iq9a8c"); // 		    nodeGroups[nNodeGroups].width = ND_width(l);
UNSUPPORTED("dcwvaft8b2juhy42freidgc8m"); // 		    nodeGroups[nNodeGroups].height = ND_height(l);
UNSUPPORTED("a47jqpic91ky93e1ohxv590l5"); // 		} else {
UNSUPPORTED("7m6pmc4ajzo9gva5pgvtljwf3"); // 		    nodeGroups[nNodeGroups].nodes[0] = l;
UNSUPPORTED("2aqign2uu1g30cyam63ohxptd"); // 		    nodeGroups[nNodeGroups].nodes[1] = n;
UNSUPPORTED("el4pqstmtl01oio7cmzrq6qtb"); // 		    nodeGroups[nNodeGroups].nNodes = 2;
UNSUPPORTED("c1rjyfjlfo2jj62bcy025ukg1"); // 		    nodeGroups[nNodeGroups].width =
UNSUPPORTED("a2sytyotqqfithwfaykqmsycp"); // 			ND_width(l) + ND_width(n);
UNSUPPORTED("eiifp86c59tpvht7teuygr2hw"); // 		    nodeGroups[nNodeGroups].height =
UNSUPPORTED("7zt5ahh08kx3u1cw8m39031bh"); // 			(ND_height(l) <
UNSUPPORTED("3s43dn8agepaf1zik284rw4iw"); // 			 ND_height(n)) ? ND_height(n) : ND_height(l);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("1z3t7m0t6n4kv8tcm579ais2e"); // 		ND_id(l) = nNodeGroups;
UNSUPPORTED("1r38ircnao62y8uafy4au9p8c"); // 		ND_id(n) = nNodeGroups;
UNSUPPORTED("7yf3wfxeea0vbj0tkmh1sv74w"); // 		nNodeGroups++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5vqsnuao6a6zpx51lrbvsra7t
// int countDummyNodes(graph_t * g) 
public static Object countDummyNodes(Object... arg) {
UNSUPPORTED("51gqqbcx9xe38hsqk7vtw2h74"); // int countDummyNodes(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("551funk1bxiemly3silcqtngw"); //     int count = 0;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("33mzsqu0xadl81jos28k8lc70"); //     /* Count dummy nodes */
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("bzrm12ll46b0ey13f7049ny17"); // 		/* flat edges do not have dummy nodes */
UNSUPPORTED("cyx78ekjs6rwln3461fbzwf7o"); // 	    if (ND_rank(aghead(e)) != ND_rank(agtail(e)))	
UNSUPPORTED("cb5zuqyn7ijkx5rk10yc6tfrv"); // 		count += abs(ND_rank(aghead(e)) - ND_rank(agtail(e))) - 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7rr0cazy4menu64k2rf61h98y"); //     return count;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 by0sucsgzrupx1vrq0sc82zrb
// static layerWidthInfo_t *layerWidthInfo = NULL


//1 2g8u71llxsq207y4p1j8kefuc
// static int *sortedLayerIndex


//1 28et5dpanlo1pz361e1ey0l13
// static int nLayers = 0




//3 ecn38irnr39x1ci0qrksvt2vb
// static void computeLayerWidths(graph_t * g) 
public static Object computeLayerWidths(Object... arg) {
UNSUPPORTED("f0yxrj5hl2h30nj0ykado8683"); // static void computeLayerWidths(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("aigogf44ojtcesuy4xs7inqbn"); //     node_t *v;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("8o5u7blacrv03sbkd57tsy3ff"); //     nLayers = 0;
UNSUPPORTED("dojzfz9p7nqclvdb5tee3yl8g"); //     /* free previously allocated memory */
UNSUPPORTED("74k79fcsj62zr7jlyzubwv7ld"); //     if (layerWidthInfo) {
UNSUPPORTED("7c1hsgkci660fl7ovcywsfy7h"); // 	for (i = 0; i < nNodeGroups; i++) {
UNSUPPORTED("4fm2a017gr8w7plpnzbbgdte6"); // 	    if (layerWidthInfo[i].nodeGroupsInLayer) {
UNSUPPORTED("1p0hpvyuh05m9zj7iv2byacuu"); // 		int j;
UNSUPPORTED("torjjxsqraaddaop50hfluyq"); // 		for (j = 0; j < layerWidthInfo[i].nNodeGroupsInLayer; j++) {
UNSUPPORTED("5rh2ziady9vvl3275k6sxuawd"); // 		    //if (layerWidthInfo[i].nodeGroupsInLayer[j])
UNSUPPORTED("qfkvchcmb0h3vrvsnu17fj29"); // 		    //free(layerWidthInfo[i].nodeGroupsInLayer[j]);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9jguo0kg597gyce5y5nj8kf6h"); // 		free(layerWidthInfo[i].nodeGroupsInLayer);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4vnpv79q6n6p1kg9ndbsmzfds"); // 	    if (layerWidthInfo[i].removed)
UNSUPPORTED("2ypdtktf36wfecowcdjfjxbfn"); // 		free(layerWidthInfo[i].removed);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("aw89cecwa5xnmsohanlvbst8f"); // 	free(layerWidthInfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5xa04vocq8qrzxrvg4if75lnp"); //     /* allocate memory
UNSUPPORTED("ge7jqooagslmrp81viou5iok"); //      * the number of layers can go up to the number of node groups
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("2e016wvwaqoa74u0xllotojs0"); //     layerWidthInfo = (layerWidthInfo_t*)zmalloc((nNodeGroups)*sizeof(layerWidthInfo_t));
UNSUPPORTED("7vfjpab2mkl8qvm3ag5psj6hf"); //     for (i = 0; i < nNodeGroups; i++) {
UNSUPPORTED("1ha5uzjeh0q6s99y9ngi159c"); // 	layerWidthInfo[i].nodeGroupsInLayer =
UNSUPPORTED("1bj8nm95udr98gzajp4k7nmuu"); // 	    (nodeGroup_t **)zmalloc((nNodeGroups)*sizeof(nodeGroup_t *));
UNSUPPORTED("6loyginnjye85c38h5sul660e"); // 	layerWidthInfo[i].removed = (int*)zmalloc((nNodeGroups)*sizeof(int));
UNSUPPORTED("a16qoyl314d7d9chwymtn019s"); // 	layerWidthInfo[i].layerNumber = i;
UNSUPPORTED("cg2cd89vdfegcllk4lqfvn5h2"); // 	layerWidthInfo[i].nNodeGroupsInLayer = 0;
UNSUPPORTED("5qbgpr6w09rjfbzyrmcge4tt2"); // 	layerWidthInfo[i].nDummyNodes = 0;
UNSUPPORTED("6cn4a2kaj95meb5bwxgi6t0qr"); // 	layerWidthInfo[i].width = 0.0;
UNSUPPORTED("nklu8lmzpq6zswxqna7hbf2"); // 	layerWidthInfo[i].height = 0.0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("68bkdm53hpxme9qp9j1zxh4if"); //     /* Count dummy nodes in the layer */
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("bgir6fgzucxltcpf123wxwa16"); // 	    int k;
UNSUPPORTED("9l0n1phb69gtf3xurnpundse6"); // 	    /* FIX: This loop maybe unnecessary, but removing it and using 
UNSUPPORTED("7qmve7ja4rala0jzz6b2bf7ee"); //              * the commented codes next, gives a segmentation fault. I 
UNSUPPORTED("6tsb25jdjgeyd8kt16ms1k9qc"); //              * forgot the reason why.
UNSUPPORTED("29edlge3wqyp1gs0vymtr5700"); //              */
UNSUPPORTED("40f532kzdw6xhs33zio76rmo2"); // 	    for (k = ND_rank(agtail(e)) + 1; k < ND_rank(aghead(e)); k++) {
UNSUPPORTED("chgb216iqv20trqdzrg86mdmx"); // 		layerWidthInfo[k].nDummyNodes++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("69qgvs0kkl50uqq9a8psjvv9h"); //     /* gather the layer information */
UNSUPPORTED("7vfjpab2mkl8qvm3ag5psj6hf"); //     for (i = 0; i < nNodeGroups; i++) {
UNSUPPORTED("dh83pjg3c5msf1h1kf0axqdsx"); // 	v = nodeGroups[i].nodes[0];
UNSUPPORTED("9bi1i3v5pz27x2f1y1abwhdrm"); // 	if (ND_rank(v) + 1 > nLayers)	/* update the number of layers */
UNSUPPORTED("ezub0uc17r32oo1qlkf2gdjds"); // 	    nLayers = ND_rank(v) + 1;
UNSUPPORTED("aksirw784lry2qxk8yppa0p4e"); // 	layerWidthInfo[ND_rank(v)].width +=
UNSUPPORTED("3szfevpiz60jx1spm5qxs6dd"); // 	    nodeGroups[i].width * 72 + (layerWidthInfo[ND_rank(v)].width >
UNSUPPORTED("amrnb3ao1kyyugopgztdru2yy"); // 					 0) * GD_nodesep(g);
UNSUPPORTED("9jz8yn32phtkki2y93ymg6cmh"); // 	if (layerWidthInfo[ND_rank(v)].height < nodeGroups[i].height * 72)
UNSUPPORTED("41gme6wvrco68e7sq80ey8oal"); // 	    layerWidthInfo[ND_rank(v)].height = nodeGroups[i].height * 72;
UNSUPPORTED("eglala2xwc9bmolhriirfo56e"); // 	layerWidthInfo[ND_rank(v)].
UNSUPPORTED("arh0xb14ftp0yfs8ejxzmhjzk"); // 	    nodeGroupsInLayer[layerWidthInfo[ND_rank(v)].
UNSUPPORTED("mdsrd2ju7gexbgpwyclxucy0"); // 			      nNodeGroupsInLayer] = &nodeGroups[i];
UNSUPPORTED("8dv04kyx67hibudzpw8i562vh"); // 	layerWidthInfo[ND_rank(v)].nNodeGroupsInLayer++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 62on7vg7kkyolc539do3mec4h
// static int compFunction(const void *a, const void *b) 
public static Object compFunction(Object... arg) {
UNSUPPORTED("4ocl61j8fqxzk98dh43ldqo7n"); // static int compFunction(const void *a, const void *b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("i2zwxk0ezuvec7grppkqqj67"); //     int *ind1 = (int *) a;
UNSUPPORTED("2obagarn6xlc7uq4q8fg8pw05"); //     int *ind2 = (int *) b;
UNSUPPORTED("cy4oapfo680baj6mqy6v64o0d"); //     return (layerWidthInfo[*ind2].width >
UNSUPPORTED("44d4z7bklr6jymot3f82e2qk"); // 	    layerWidthInfo[*ind1].width) - (layerWidthInfo[*ind2].width <
UNSUPPORTED("dol0wgt1zxwmpxius6rn6lf36"); // 					    layerWidthInfo[*ind1].width);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ehhhz69emnjuunlpp23vqc6px
// static void sortLayers(graph_t * g) 
public static Object sortLayers(Object... arg) {
UNSUPPORTED("30da20fhd5pcu6ofmw4vzj6xt"); // static void sortLayers(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1djuhyco70xz45va6y1yesa70"); //     qsort(sortedLayerIndex, agnnodes(g), sizeof(int), compFunction);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6evdukaqstizjshbr4oanxa73
// static int getOutDegree(nodeGroup_t * ng) 
public static Object getOutDegree(Object... arg) {
UNSUPPORTED("2h5zq3e2klttmcfrs4pg0cst6"); // static int getOutDegree(nodeGroup_t * ng)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b5a93beg34iiqtnufv9wanh2r"); //     int i, cnt = 0;
UNSUPPORTED("7ur993neb8tctm0gbe60vhpva"); //     for (i = 0; i < ng->nNodes; i++) {
UNSUPPORTED("3ubjqvl7psu25r3c10ddw54v5"); // 	node_t *n = ng->nodes[i];
UNSUPPORTED("ak952thd788dbr90m2fvaklyu"); // 	edge_t *e;
UNSUPPORTED("2z8g63ud3byunrtnllsx573mk"); // 	graph_t *g = agraphof(n);
UNSUPPORTED("dar3s057kgujhrxf7no64ofdb"); // 	/* count outdegree. This loop might be unnecessary. */
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("7hl03wjg5yryhvbe4ar0i0b8g"); // 	    cnt++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3zkt5kbq3sockq663o3s3ync7"); //     return cnt;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 advd4q4a55y9bkee9lm2jlb0b
// static int compFunction2(const void *a, const void *b) 
public static Object compFunction2(Object... arg) {
UNSUPPORTED("3ozmsdf5a2dqxtwspwzu5w8yl"); // static int compFunction2(const void *a, const void *b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("751goeklcv2nazn2zynh989rh"); //     nodeGroup_t **ind1 = (nodeGroup_t **) a, **ind2 = (nodeGroup_t **) b;
UNSUPPORTED("d5t70ah6319tmfn70ddvkq7wr"); //     int cnt1 = getOutDegree(*ind1);
UNSUPPORTED("8wbvk61eveeuzv86g34s2xbd4"); //     int cnt2 = getOutDegree(*ind2);
UNSUPPORTED("54loo8ag5vsb7zdxx83v3wegy"); //     return (cnt2 < cnt1) - (cnt2 > cnt1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aumg85jl3ptocfpw3ix2pech9
// static void reduceMaxWidth2(graph_t * g) 
public static Object reduceMaxWidth2(Object... arg) {
UNSUPPORTED("6kkeddpljjg8jce0vm3z2zxdy"); // static void reduceMaxWidth2(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("809hvj8mnkno5tj5jlitlzwba"); //     int maxLayerIndex;
UNSUPPORTED("7krkyrq7vtxesiaw3i63dji4m"); //     double nextMaxWidth;
UNSUPPORTED("c9w9u59u9ncaoyp4lxxvw7ys3"); //     double w = 0;
UNSUPPORTED("9x23dbda4cbxrfoy5h5q89sef"); //     double targetWidth;
UNSUPPORTED("3d8p59v1x246rys9xsoqpko98"); //     int fst;
UNSUPPORTED("4zuos9b188cv83meg3dw0orp3"); //     nodeGroup_t *fstNdGrp;
UNSUPPORTED("beehmu1l85sgql2h4wysgoce4"); //     int ndem;
UNSUPPORTED("884zwztabkph7x61nviw8sjef"); //     int p, q;
UNSUPPORTED("8t1tccvz58ueg1sf9fyado1vu"); //     int limit;
UNSUPPORTED("di25ovytf5y19mju57f42pogp"); //     int rem;
UNSUPPORTED("dcmflthwszqqq5jsozmt9oyfi"); //     int rem2;
UNSUPPORTED("81h1x4spv5vyw9fipnyfpgcmm"); //     /* Find the widest layer. it must have at least 2 nodes. */
UNSUPPORTED("5ad5whcn26kk2tafa6g5hmjgm"); //     for (i = 0; i < nLayers; i++) {
UNSUPPORTED("367ttm0jqivj6ascugpsq2ihs"); // 	if (layerWidthInfo[sortedLayerIndex[i]].nNodeGroupsInLayer <= 1)
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("1qngg4wjsrt1uwmffwse8e1fm"); // 	    maxLayerIndex = sortedLayerIndex[i];
UNSUPPORTED("mrjkzaja4hn0djdm4njyzwgn"); // 	    /* get the width of the next widest layer */
UNSUPPORTED("bekefegm5r42jf82uhdsw60k2"); // 	    nextMaxWidth =
UNSUPPORTED("1s96rlqp02to8be86ahpks4qp"); // 		(nLayers >
UNSUPPORTED("4qkxf4ozs5338fev3ejswyucm"); // 		 i + 1) ? layerWidthInfo[sortedLayerIndex[i +
UNSUPPORTED("90jb1nqwt32r76t90h3kukxls"); // 							  1]].width : 0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("atqfhmyhmoiwees87nqlh9bs2"); //     if (i == nLayers)
UNSUPPORTED("4wofltgoxyf0kq1ucrvv2sbq9"); // 	return;			/* reduction of layerwidth is not possible. */
UNSUPPORTED("5nqb1rqm56ozkw6w0ysotyy06"); //     /* sort the node groups in maxLayerIndex layer by height and 
UNSUPPORTED("e9gqf91z12tzdilu3jefz85op"); //      * then width, nonincreasing 
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("3tbh2oua8twhyt14bkhup0v5n"); //     qsort(layerWidthInfo[maxLayerIndex].nodeGroupsInLayer,
UNSUPPORTED("80ucuxmaxd96o7ozxxno7y18c"); // 	  layerWidthInfo[maxLayerIndex].nNodeGroupsInLayer,
UNSUPPORTED("f0wxqmd02xh4xxnox7semmqup"); // 	  sizeof(nodeGroup_t *), compFunction2);
UNSUPPORTED("bukry94lbr7k56elmyf5nyna1"); //     if (nextMaxWidth <= layerWidthInfo[maxLayerIndex].width / 4
UNSUPPORTED("1dy07qz8nxz4ovvsonpv95h8x"); // 	|| nextMaxWidth >= layerWidthInfo[maxLayerIndex].width * 3 / 4)
UNSUPPORTED("4xw4q9anml67xuotpbea3yy1x"); // 	nextMaxWidth = layerWidthInfo[maxLayerIndex].width / 2;
UNSUPPORTED("4vcsswlnmvhk990gonx0dbl53"); //     targetWidth = nextMaxWidth;	/* layerWidthInfo[maxLayerIndex].width/2; */
UNSUPPORTED("8xl0athl77hag9patwu9qhehu"); //     /* now partition the current layer into two or more 
UNSUPPORTED("c49o3kf9bot8xo1pu9vb9qcsy"); //      * layers (determined by the ranking algorithm)
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("3ku48i8nky2n7oji9472v747k"); //     fst = 0;
UNSUPPORTED("cspv6bcy4ird3pgtk05x2v9x6"); //     ndem = 0;
UNSUPPORTED("1b8yssh8ruoe077cgcki6zdrt"); //     limit = layerWidthInfo[maxLayerIndex].nNodeGroupsInLayer;
UNSUPPORTED("2rbxfdjhm2zringpkeerct448"); //     rem = 0;
UNSUPPORTED("apzuij26xnj5ma4m18z4ai50w"); //     rem2 = 0;
UNSUPPORTED("dap1pwebtq8maf5s2545nbrp1"); //     /* initialize w, the width of the widest layer after partitioning */
UNSUPPORTED("7l2ucqsu9ruv5iie2klh90ahd"); //     w = 0;
UNSUPPORTED("a7g80jjjbl6srwowqdzxcoqs0"); //     for (i = 0; i < limit + rem; i++) {
UNSUPPORTED("1ahjq48zq780a0cwvpseutdkn"); // 	if (layerWidthInfo[maxLayerIndex].removed[i]) {
UNSUPPORTED("48so496p8g7nm344prgfhv3r5"); // 	    rem++;
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cg4gj8hzn83y6snfckfbmfo6e"); // 	if ((w +
UNSUPPORTED("dv06i6wtz2bw1qsahmuqs98hl"); // 	     layerWidthInfo[maxLayerIndex].nodeGroupsInLayer[i]->width *
UNSUPPORTED("pf67y1fh8hdiylyu2431xmxa"); // 	     72 + (w > 0) * GD_nodesep(g) <= targetWidth)
UNSUPPORTED("bq1bstz8voswqa4fjibespqab"); // 	    || !fst) {
UNSUPPORTED("5qq0d479u9d6d2ye5xl80ax0d"); // 	    w += (layerWidthInfo[maxLayerIndex].nodeGroupsInLayer[i])->
UNSUPPORTED("bq93mxk1jp4ou77b72s93zr85"); // 		width * 72 + (w > 0) * GD_nodesep(g);
UNSUPPORTED("366y982g736adlsryzw0c3oek"); // 	    if (!fst) {
UNSUPPORTED("9plnm3zvs0636dt0kmoha46l"); // 		fstNdGrp =
UNSUPPORTED("5xiakyfs1gp0y9aasprrrk5mn"); // 		    layerWidthInfo[maxLayerIndex].nodeGroupsInLayer[i];
UNSUPPORTED("bf0q1igp1253yxfg3f1tfel24"); // 		fst = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("3wwlt6ahhurc0ijlwm0bxslur"); // 	    nodeGroup_t *ng =
UNSUPPORTED("43fttvx26kc9s70wgpnm3ws2q"); // 		layerWidthInfo[maxLayerIndex].nodeGroupsInLayer[i];
UNSUPPORTED("kq0gpmkxpiwbektqvym913vy"); // 	    for (p = 0; p < fstNdGrp->nNodes; p++)
UNSUPPORTED("7chqat1hgor0xzck89881kmmz"); // 		for (q = 0; q < ng->nNodes; q++) {
UNSUPPORTED("3egbmst2r8790sta0upbaq3kj"); // 		    //printf("Trying to add virtual edge: %s -> %s\n",
UNSUPPORTED("dwbnct46v3we663g5l2tmptui"); // 		    //    agnameof(fstNdGrp->nodes[p]), agnameof(ng->nodes[q]));
UNSUPPORTED("cuqv9sy98rmhdbjpzeex3rg4v"); // 		    /* The following code is for deletion of long virtual edges.
UNSUPPORTED("1r2z18tbg612ek9sfb489a6xc"); // 		     * It's no longer used.
UNSUPPORTED("e37um01phgd5qy4hrex7cu8bi"); // 		     */
UNSUPPORTED("58a2mmk9lsz9t5yzb8b9f8bkp"); // 		    /* add a new virtual edge */
UNSUPPORTED("6ljuxngn9wk3ftcodzf054yeu"); // 		    edge_t *newVEdge =
UNSUPPORTED("844w3zq8p902r572neb30unb6"); // 			virtual_edge(fstNdGrp->nodes[p], ng->nodes[q],
UNSUPPORTED("b955fhzlbikknivthxtzpnugj"); // 				     NULL);
UNSUPPORTED("qt1p5nrwd57te4eh2ohjmmta"); // 		    ED_edge_type(newVEdge) = 1;
UNSUPPORTED("dsgv48crsux69djskyfkvpq8a"); // 		    ndem++;	/* increase number of node demotions */
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7vqv39l80iv7x6hfdcecytlgy"); // 	    /* the following code updates the layer width information. The 
UNSUPPORTED("7j2qzieax8k3ao9i9so65vkc1"); // 	     * update is not useful in the current version of the heuristic.
UNSUPPORTED("20m1lc1moer8x00tx9ceto0iw"); // 	     */
UNSUPPORTED("8vij5gjt5kh41fekao3vc79ck"); // 	    layerWidthInfo[maxLayerIndex].removed[i] = 1;
UNSUPPORTED("czzy9dbc0rmsq8m3o0g5tuamg"); // 	    rem2++;
UNSUPPORTED("96jz56riot2d1ri2fufximy6f"); // 	    layerWidthInfo[maxLayerIndex].nNodeGroupsInLayer--;
UNSUPPORTED("ahih3boi0hmv6cjfgurjbltxl"); // 	    /* SHOULD BE INCREASED BY THE SUM OF INDEG OF ALL NODES IN GROUP */
UNSUPPORTED("7wh7bhbziqs0nzma76j37hbiz"); // 	    layerWidthInfo[maxLayerIndex].nDummyNodes++;
UNSUPPORTED("4zs60km4pkprzvbgtwsdhp5ad"); // 	    layerWidthInfo[maxLayerIndex].width -=
UNSUPPORTED("atkdij9n6sv7amu7j5iiofmpb"); // 		(ng->width * 72 + GD_nodesep(g));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9m17vsomqcrp49v7i892k9wdz
// static void applyPacking2(graph_t * g) 
public static Object applyPacking2(Object... arg) {
UNSUPPORTED("a65t347rhgkl70wordiofv3b"); // static void applyPacking2(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("5z9j46ysd1pb5bnq6c90enmrq"); //     sortedLayerIndex = (int*)zmalloc((agnnodes(g))*sizeof(int));
UNSUPPORTED("9bb75wlkcadi5zu3bgx60fel0"); //     for (i = 0; i < agnnodes(g); i++) {
UNSUPPORTED("e9lhr9mahq6qolp5r06dl6rfy"); // 	sortedLayerIndex[i] = i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ekdavoqh7w4imu35z1hk4cr27"); //     computeLayerWidths(g);
UNSUPPORTED("6541sj456n87xc6ssjlvin6lv"); //     sortLayers(g);
UNSUPPORTED("22r0c4vewes195tok6uvfzjjk"); //     reduceMaxWidth2(g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b9x4re3dvjn32qyn2twehamcd
// void initEdgeTypes(graph_t * g) 
public static Object initEdgeTypes(Object... arg) {
UNSUPPORTED("2syp4meyieisq746m5m1ff921"); // void initEdgeTypes(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("7k5t9s9q7nhmwr9qz8jg12ams"); //     int lc;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("z39vglndoyxj54j2z9v64r6q"); // 	for (lc = 0; lc < ND_in(n).size; lc++) {
UNSUPPORTED("agilsevb8bk3ymoczc2ptb24y"); // 	    e = ND_in(n).list[lc];
UNSUPPORTED("21bvtwcu90i27r8cbu2ngifk"); // 	    ED_edge_type(e) = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9b4z7q5fnvgvi1cs36mimkrqs
// static double computeCombiAR(graph_t * g) 
public static Object computeCombiAR(Object... arg) {
UNSUPPORTED("8y99bebjn0nlslisg0u06o7ef"); // static double computeCombiAR(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("gmffj6qikpm7gyjl7ppj2avw"); //     int i, maxLayerIndex;
UNSUPPORTED("bkqjq8sflq6i69h1q5wtrqdyz"); //     double maxW = 0;
UNSUPPORTED("d2sh953raugyt16qa6wimemaq"); //     double maxH;
UNSUPPORTED("3dg5tppzkfs857vo6ykmcnnah"); //     double ratio;
UNSUPPORTED("ekdavoqh7w4imu35z1hk4cr27"); //     computeLayerWidths(g);
UNSUPPORTED("btqcp1jn7vzx1obrdwlpao8ei"); //     maxH = (nLayers - 1) * GD_ranksep(g);
UNSUPPORTED("5ad5whcn26kk2tafa6g5hmjgm"); //     for (i = 0; i < nLayers; i++) {
UNSUPPORTED("6nkzs7fajji0961pjb259v0fk"); // 	if (maxW <
UNSUPPORTED("5k2g68qxz0ue7tb2a4pdbhu2j"); // 	    layerWidthInfo[i].width +
UNSUPPORTED("eoi6tblklq5p0ap1ck08iiqj8"); // 	    layerWidthInfo[i].nDummyNodes * GD_nodesep(g)) {
UNSUPPORTED("93grfh5yi1ydy8iyt353uh909"); // 	    maxW =
UNSUPPORTED("279i4wi2e4voircoomc1ry8ua"); // 		layerWidthInfo[i].width +
UNSUPPORTED("6xb1rhy6cdjxo2vbpz8nhuvxe"); // 		layerWidthInfo[i].nDummyNodes * GD_nodesep(g);
UNSUPPORTED("92zn37cakfkafc7ginje53mb6"); // 	    maxLayerIndex = i;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cn95qhc4xh1ri1c41fnpwce48"); // 	maxH += layerWidthInfo[i].height;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9p670kmiu3qqp1vgbn22mq4w9"); //     ratio = maxW / maxH;
UNSUPPORTED("beyrasxgt5kbbdyzy9nvsaf3i"); //     return ratio;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 w58r6lvyglcqidl4qnxr3cm5
// static void zapLayers(graph_t * g) 
public static Object zapLayers(Object... arg) {
UNSUPPORTED("dsto0e5yga5gs3tgtzp4pbffj"); // static void zapLayers(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dzpsknrwv8qkqq20hjnjpjn68"); //     int i, j;
UNSUPPORTED("7cl1kmap8x457uux62gb4277b"); //     int start = 0;
UNSUPPORTED("551funk1bxiemly3silcqtngw"); //     int count = 0;
UNSUPPORTED("90et4gwa94l7wrduohmumf7q6"); //     /* the layers are sorted by the layer number.  now zap the empty layers */
UNSUPPORTED("5ad5whcn26kk2tafa6g5hmjgm"); //     for (i = 0; i < nLayers; i++) {
UNSUPPORTED("8oo7ghtfr3xrf5gukymtnn4jk"); // 	if (layerWidthInfo[i].nNodeGroupsInLayer == 0) {
UNSUPPORTED("dgv3r9g8m1rprnlfpxqhfe1z9"); // 	    if (count == 0)
UNSUPPORTED("2ksocundgfwgz5agi901h1ng6"); // 		start = layerWidthInfo[i].layerNumber;
UNSUPPORTED("fn6rrzmrb2yold6j6aofg94n"); // 	    count++;
UNSUPPORTED("ern96acbmw0trnet16xgt1chp"); // 	} else if (count && layerWidthInfo[i].layerNumber > start) {
UNSUPPORTED("4f4sjt4zqxe5b75pgq7zd079z"); // 	    for (j = 0; j < layerWidthInfo[i].nNodeGroupsInLayer; j++) {
UNSUPPORTED("euaoxl3q399fzxn78jpuxbz0m"); // 		int q;
UNSUPPORTED("4y0bw1xrvij26xm6lflig8hph"); // 		nodeGroup_t *ng = layerWidthInfo[i].nodeGroupsInLayer[j];
UNSUPPORTED("7chqat1hgor0xzck89881kmmz"); // 		for (q = 0; q < ng->nNodes; q++) {
UNSUPPORTED("ez35ky0s0p5r57be9io2lrr10"); // 		    node_t *nd = ng->nodes[q];
UNSUPPORTED("bcfxc7h41rzopvk27c7kreog"); // 		    ND_rank(nd) -= count;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 25p8yyx732fa1dwxany5vkmqv
// void rank3(graph_t * g, aspect_t * asp) 
public static Object rank3(Object... arg) {
UNSUPPORTED("ey9g3c61eb7j5dnz3gbhvflt3"); // void rank3(graph_t * g, aspect_t * asp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("752slid56zrpl98vchhyeqpj"); //     int iterations = asp->nextIter;
UNSUPPORTED("6wfeyz2bw1fm15jb5qg3a79a2"); //     double lastAR = MAXDOUBLE;
UNSUPPORTED("5bvy7rsy7sf1krbn35gmd888x"); //     computeNodeGroups(g);	/* groups of UF DS nodes */
UNSUPPORTED("cn5dqgh2amp70r1jzq8d7v75x"); //     for (i = 0; (i < iterations) || (iterations == -1); i++) {
UNSUPPORTED("42uwgh3dkewf6zkd83xt1wzjd"); // 	/* initialize all ranks to be 0 */
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("f429zn3r84ceckfatzshenxzo"); // 	    ND_rank(n) = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("d7qgdx3iixit5pdzj1yfw5vc8"); // 	/* need to compute ranking first--- by Network flow */
UNSUPPORTED("1um989w8mqkye2u0q3wbc8dgb"); // 	rank1(g);
UNSUPPORTED("cou59yxqf69o7kpjwdkc0vdth"); // 	asp->combiAR = computeCombiAR(g);
UNSUPPORTED("a0xpyoq74njvdwc9lcvgyfc0p"); // 	if (Verbose)
UNSUPPORTED("5ndbkluuhatxcz2bwngcywbhc"); // 	    fprintf(stderr, "combiAR = %lf\n", asp->combiAR);
UNSUPPORTED("53w2avskexaf6hjssoeplgz0o"); // 	/* Uncomment the following codes, for working with narrow graphs */
UNSUPPORTED("am6dg6wfejg71244n1zzvaed6"); //         /* Success or if no improvement */
UNSUPPORTED("7qroolj8ezwbbkwrss9k1q5mc"); // 	if ((asp->combiAR <= asp->targetAR) || ((iterations == -1) && (lastAR <= asp->combiAR))) {
UNSUPPORTED("11dozd7gvruvmd3x164caqmgz"); // 	    asp->prevIterations = asp->curIterations;
UNSUPPORTED("bm7zpotfxeku2ydqqsd6twzm6"); // 	    asp->curIterations = i;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7r2hn8bkthpfcf03lu4vnje0f"); // 	lastAR = asp->combiAR;
UNSUPPORTED("9tsz1ho0itczonzjb308rql0m"); // 	/* Apply the FFDH algorithm to reduce the aspect ratio; */
UNSUPPORTED("7jlj2ab1tf2kzdmzf0f14epg4"); // 	applyPacking2(g);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9zv64kjrcw7u09t5lm2qsg4fw"); //     /* do network flow once again... incorporating the added edges */
UNSUPPORTED("393qo3g8b50gvp5o0tpqh29v1"); //     rank1(g);
UNSUPPORTED("ekdavoqh7w4imu35z1hk4cr27"); //     computeLayerWidths(g);
UNSUPPORTED("b1ukbsj4nhxbvn64fx7zbx7sn"); //     zapLayers(g);
UNSUPPORTED("3b5copquo4t13fbkmtbbkvo8j"); //     asp->combiAR = computeCombiAR(g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7bn6xhopjjdriiwvcpg9wqy1y
// void init_UF_size(graph_t * g) 
public static Object init_UF_size(Object... arg) {
UNSUPPORTED("cqp1rfchvfwon31gknapperjh"); // void init_UF_size(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("2jzig41vokvwcy6z0o5dgo7sb"); // 	ND_UF_size(n) = 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5srsfxqlego6qiyj5mm8m4ql2
// aspect_t* setAspect (Agraph_t * g, aspect_t* adata) 
public static ST_aspect_t setAspect(ST_Agraph_s g, ST_aspect_t adata) {
ENTERING("5srsfxqlego6qiyj5mm8m4ql2","setAspect");
try {
    double rv;
    CString p;
    int r, passes = 5;
    p = agget (g, new CString("aspect"));
    if (N(p) || UNSUPPORTED("(r = sscanf (p, %lf,%d, &rv, &passes)) <= 0)")==null) {
	adata.setInt("nextIter", 0);
	adata.setInt("badGraph", 0);
	return null;
    }
UNSUPPORTED("bq1l9wqiw8bcls8ptb8o9rhqi"); //     agerr (AGWARN, "the aspect attribute has been disabled due to implementation flaws - attribute ignored.\n");
UNSUPPORTED("1hvtnh8r9fb47ewad86tcf0n0"); //     adata->nextIter = 0;
UNSUPPORTED("xx058mix2zyckbi0esnoc56v"); //     adata->badGraph = 0;
UNSUPPORTED("o68dp934ebg4cplebgc5hv4v"); //     return NULL;
UNSUPPORTED("ikxv7u2qba6riujs2unqsitx"); //     if (rv < 1.0) rv = 1.0;
UNSUPPORTED("acmmvkcaf8bc8wb6kdx9s2d3a"); //     else if (rv > 20.0) rv = 20.0;
UNSUPPORTED("18wyf3ih8fg4x4s3c006nx8zx"); //     adata->targetAR = rv;
UNSUPPORTED("2875r01nyy2cpm1c439zwu6tf"); //     adata->nextIter = -1;
UNSUPPORTED("9ssjc7jsfl2jgw01gwnr3ftal"); //     adata->nPasses = passes;
UNSUPPORTED("xx058mix2zyckbi0esnoc56v"); //     adata->badGraph = 0;
UNSUPPORTED("620q0efwdrg8vw3ggwpmxqiwv"); //     if (Verbose) 
UNSUPPORTED("a7heh0decro63skjh8xq4buo7"); //         fprintf(stderr, "Target AR = %g\n", adata->targetAR);
UNSUPPORTED("b1cn763x4de1h91cdv3d4o3lb"); //     return adata;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("5srsfxqlego6qiyj5mm8m4ql2","setAspect");
}
}


}
