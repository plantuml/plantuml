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
import static gen.lib.cgraph.attr__c.agattr;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_pointf;
import smetana.core.CString;
import smetana.core.Z;

public class sameport__c {
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




//3 eu2yvovb9xx4rzic3gllij2bv
// void dot_sameports(graph_t * g) 
public static void dot_sameports(ST_Agraph_s g) {
ENTERING("eu2yvovb9xx4rzic3gllij2bv","dot_sameports");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    CString id;
    //same_t samehead[5];
    //same_t sametail[5];
    int n_samehead;		/* number of same_t groups on current node */
    int n_sametail;		/* number of same_t groups on current node */
    int i;
    Z.z().E_samehead = agattr(g, AGEDGE, new CString("samehead"),null);
    Z.z().E_sametail = agattr(g, AGEDGE, new CString("sametail"),null);
    if (N(Z.z().E_samehead!=null || Z.z().E_sametail!=null))
	return;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("4roxmr5lxkjz6gn1j9mndurq2"); // 	n_samehead = n_sametail = 0;
UNSUPPORTED("8oxob1qbbkbjh0jjcogk42jfl"); // 	for (e = agfstedge(g, n); e; e = agnxtedge(g, e, n)) {
UNSUPPORTED("4gy7rakqurxvound05crezka2"); // 	    if (aghead(e) == agtail(e)) continue;  /* Don't support same* for loops */
UNSUPPORTED("2r5fkddp1ey0fvpok2scgkk99"); // 	    if (aghead(e) == n && E_samehead &&
UNSUPPORTED("d38ofiemhq37ykyauh9wync84"); // 	        (id = agxget(e, E_samehead))[0])
UNSUPPORTED("18y7dy98psh7ultlx0jugsfu2"); // 		n_samehead = sameedge(samehead, n_samehead, n, e, id);
UNSUPPORTED("5snv0fee5roi91irdwv8x51xi"); // 	    else if (agtail(e) == n && E_sametail &&
UNSUPPORTED("8xvjbvzldkn1yksprzfexgsjs"); // 	        (id = agxget(e, E_sametail))[0])
UNSUPPORTED("cdn1fgq1pke9ekyar2b4r6e91"); // 		n_sametail = sameedge(sametail, n_sametail, n, e, id);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c96gtgvp5uw8ktp389l2s9l4u"); // 	for (i = 0; i < n_samehead; i++) {
UNSUPPORTED("b2s9feywib1q9pxw4h31yz6dk"); // 	    if (samehead[i].l.size > 1)
UNSUPPORTED("1xvsmwfz2hihjki8tsqiaa1g8"); // 		sameport(n, &samehead[i].l, samehead[i].arr_len);
UNSUPPORTED("cpzvkkchr60qet357b9gg1e5q"); // 	    free_list(samehead[i].l);
UNSUPPORTED("6cnsuklvjftdyhzat6za4qggi"); // 	    /* I sure hope I don't need to free the char* id */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9ktz8j6cseb5w7jkfhrj5r19t"); // 	for (i = 0; i < n_sametail; i++) {
UNSUPPORTED("5vcqm1irqrqcg4f6ldrqkikq8"); // 	    if (sametail[i].l.size > 1)
UNSUPPORTED("dcjzqns8bm1o766uqi8dy72qf"); // 		sameport(n, &sametail[i].l, sametail[i].arr_len);
UNSUPPORTED("13a7frulpyheo0h4ajbfja7ph"); // 	    free_list(sametail[i].l);
UNSUPPORTED("6cnsuklvjftdyhzat6za4qggi"); // 	    /* I sure hope I don't need to free the char* id */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("eu2yvovb9xx4rzic3gllij2bv","dot_sameports");
}
}




//3 e6phoefj4ujntgmpiclbnmliw
// static int sameedge(same_t * same, int n_same, node_t * n, edge_t * e, char *id) 
public static Object sameedge(Object... arg) {
UNSUPPORTED("4pviw1spiyvhdz8yvl4ho6qkx"); // static int sameedge(same_t * same, int n_same, node_t * n, edge_t * e, char *id)
UNSUPPORTED("bz7wamuuo5855unp564487v13"); // /* register E in the SAME structure of N under ID. Uses static int N_SAME */
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ddmbnep3k060w2rgslisky5vw"); //     int i, sflag, eflag, flag;
UNSUPPORTED("4yhtj0buzrukmv0lwrz77s5oa"); //     for (i = 0; i < n_same; i++)
UNSUPPORTED("6htdinsa3h8aamp1e6pvd6ig5"); // 	if ((*(same[i].id)==*(id)&&!strcmp(same[i].id,id))) {
UNSUPPORTED("bqrcn2qjykhnd6fuwneng85h"); // 	    elist_append(e, same[i].l);
UNSUPPORTED("g5anfkdhikcxt42clvstq6hm"); // 	    goto set_arrow;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8ac5s7v71vxuj2d2r4t989b1c"); //     if (++n_same > 5) {
UNSUPPORTED("c29filv0jivh50blpbuqjk5ib"); // 	n_same--;
UNSUPPORTED("3uf43hfdusyzm1vp3dq12qu29"); // 	agerr(AGERR, "too many (> %d) same{head,tail} groups for node %s\n",
UNSUPPORTED("ciwytw61x3j4anugbarxaa7m"); // 	      5, agnameof(n));
UNSUPPORTED("3lrnrroeosc48ilq0azgtzyna"); // 	return n_same;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("36d7os443e3k5gf7g7zvi3vfs"); //     alloc_elist(1, same[i].l);
UNSUPPORTED("3s0n702bprlxue4xtghqv3nad"); //     elist_fastapp(e, same[i].l);
UNSUPPORTED("cwxcw0cnsxpwkd7l94pmw1dfg"); //     same[i].id = id;
UNSUPPORTED("4y2b9aw2a57zud1ncpiw2fihn"); //     same[i].n_arr = 0;
UNSUPPORTED("2lemlhm6qdqemg83e6xwyzd01"); //     same[i].arr_len = 0;
UNSUPPORTED("3k68ve2sycumnr4ncfzymgyli"); //   set_arrow:
UNSUPPORTED("4028w60fcyzgcb6qh4kf8090j"); //     arrow_flags(e, &sflag, &eflag);
UNSUPPORTED("1rkfoax4tin2ccbtpc8w1tnpv"); //     if ((flag = aghead(e) == n ? eflag : sflag))
UNSUPPORTED("bhh3lvo0knve8fiod7js8o8oy"); // 	same[i].arr_len =
UNSUPPORTED("b7yh5042d5o8u6iso9bm39glf"); // 	    /* only consider arrows if there's exactly one arrow */
UNSUPPORTED("5r1jxkep2wvpcpjz1e6n0dxo7"); // 	    (++same[i].n_arr == 1) ? arrow_length(e, flag) : 0;
UNSUPPORTED("59a2z18u4tje6r407a6psdqy8"); //     return n_same;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2mnqx0mihpyo94rckzyvqd3ha
// static void sameport(node_t * u, elist * l, double arr_len) 
public static Object sameport(Object... arg) {
UNSUPPORTED("5h4o73bwixguohs97owrtr8ef"); // static void sameport(node_t * u, elist * l, double arr_len)
UNSUPPORTED("d5nznmmgnjszlfjdd61kps3jx"); // /* make all edges in L share the same port on U. The port is placed on the
UNSUPPORTED("bo9mbi1sf1ycc8zr8zucow3sz"); //    node boundary and the average angle between the edges. FIXME: this assumes
UNSUPPORTED("4jpc10q4iiglyfgqc4t0rvtce"); //    naively that the edges are straight lines, which is wrong if they are long.
UNSUPPORTED("wh2ex8xlk21eu1y33pacj3v2"); //    In that case something like concentration could be done.
UNSUPPORTED("4c9tqu2byiozzo2mzc1fbvda9"); //    An arr_port is also computed that's ARR_LEN away from the node boundary.
UNSUPPORTED("atdcc5pbds8mqzssaq6v0r6rw"); //    It's used for edges that don't themselves have an arrow.
UNSUPPORTED("bnetqzovnscxile7ao44kc0qd"); // */
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aigogf44ojtcesuy4xs7inqbn"); //     node_t *v;
UNSUPPORTED("9b48a157azcrz2ihzqehhpsvs"); //     edge_t *e, *f;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("e39yybz8f8dk5960upp4t7e85"); //     double x = 0, y = 0, x1, y1, x2, y2, r;
UNSUPPORTED("cmzlbdlia0ky3ulmkmhw731vo"); //     port prt;
UNSUPPORTED("lnwbe9uftcv8uw5w72tyaim6"); //     int sflag, eflag;
UNSUPPORTED("3s75qjxnr416bs4xwntk5wjey"); //     /* Compute the direction vector (x,y) of the average direction. We compute
UNSUPPORTED("168uvy6red5coj19cfwdg74w4"); //        with direction vectors instead of angles because else we have to first
UNSUPPORTED("byd7rdw7ogomu0h8xa3cngptx"); //        bring the angles within PI of each other. av(a,b)!=av(a,b+2*PI) */
UNSUPPORTED("7qdtxp882tja3q4pjpimsdpz2"); //     for (i = 0; i < l->size; i++) {
UNSUPPORTED("9a2w9ypg4zbrmt9mwxerl9ku9"); // 	e = l->list[i];
UNSUPPORTED("dmcddmsjj52vhbo0p72d20r7x"); // 	if (aghead(e) == u)
UNSUPPORTED("4gg5n60ynsciy3te5bmvsjdu0"); // 	    v = agtail(e);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("39bh91n8ucstjr8vmtx3ynvcd"); // 	    v = aghead(e);
UNSUPPORTED("3bzvithkkr721ghsi2ljswz41"); // 	x1 = ND_coord(v).x - ND_coord(u).x;
UNSUPPORTED("2b884aalfpupcphtkwlw7fh1p"); // 	y1 = ND_coord(v).y - ND_coord(u).y;
UNSUPPORTED("2bo8vt6om92qnyd8ajsur7839"); // 	r = hypot(x1, y1);
UNSUPPORTED("8ldeax6x9cgrmbfybuxbl5n3o"); // 	x += x1 / r;
UNSUPPORTED("lvaxvfnj6g12uz2apxvc32bt"); // 	y += y1 / r;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a1mnn3lz9vym0mycdnjtnqt50"); //     r = hypot(x, y);
UNSUPPORTED("en33l6y6hjb8cbouawh4qgny1"); //     x /= r;
UNSUPPORTED("7ogy0bdt0ghq60m7dyzb48vku"); //     y /= r;
UNSUPPORTED("1zwmby4z9dnptpyvedi7zqx1y"); //     /* (x1,y1),(x2,y2) is a segment that must cross the node boundary */
UNSUPPORTED("aezmu3kg51jsv6vuj8yl2vgx0"); //     x1 = ND_coord(u).x;
UNSUPPORTED("b9lds1fhngov0656kxk71v6o"); //     y1 = ND_coord(u).y;	/* center of node */
UNSUPPORTED("a47id67bq23txqyol6w89ohg8"); //     r = MAX(ND_lw(u) + ND_rw(u), ND_ht(u) + GD_ranksep(agraphof(u)));	/* far away */
UNSUPPORTED("632ifec281b8hg0vql6w66fd0"); //     x2 = x * r + ND_coord(u).x;
UNSUPPORTED("tpm4a8o4c87dctdvop70l3gg"); //     y2 = y * r + ND_coord(u).y;
UNSUPPORTED("9axcwk1yl7elzq5ch7zzsg2b"); //     {				/* now move (x1,y1) to the node boundary */
UNSUPPORTED("6qp76u7xiuyi81ocft3zna7rw"); // 	pointf curve[4];		/* bezier control points for a straight line */
UNSUPPORTED("d2banip9m2nhni4tcg0ub95sb"); // 	curve[0].x = x1;
UNSUPPORTED("jy2q28s2gzpbigtocfoo6cy6"); // 	curve[0].y = y1;
UNSUPPORTED("90jm80vushtpjetfmrlks5tr6"); // 	curve[1].x = (2 * x1 + x2) / 3;
UNSUPPORTED("ah6xwuv1rqq311cmwhnrs4p7g"); // 	curve[1].y = (2 * y1 + y2) / 3;
UNSUPPORTED("5nzdtvblt406q4j887woceaso"); // 	curve[2].x = (2 * x2 + x1) / 3;
UNSUPPORTED("8ccq8nqc6cwcgqyrls16i5n1x"); // 	curve[2].y = (2 * y2 + y1) / 3;
UNSUPPORTED("9e73i3zoi98jcem56ovsw95mw"); // 	curve[3].x = x2;
UNSUPPORTED("bt1idr0p5w37ehw0ca5qvh2d0"); // 	curve[3].y = y2;
UNSUPPORTED("4hvdnsqv9gvg89n449ivew89w"); // 	shape_clip(u, curve);
UNSUPPORTED("e8zqbvvz0hnmxynqrsi4tq6z9"); // 	x1 = curve[0].x - ND_coord(u).x;
UNSUPPORTED("10uhdsyhabakyucrrlkpxmy39"); // 	y1 = curve[0].y - ND_coord(u).y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ftxzqcd66xy9t3i1shc08phg"); //     /* compute PORT on the boundary */
UNSUPPORTED("egqrua11zla4ilqnv8fe2rqa9"); //     prt.p.x = ROUND(x1);
UNSUPPORTED("3j43h2ta90714rhr89dxd9bly"); //     prt.p.y = ROUND(y1);
UNSUPPORTED("72f3ncaut57fflmrrrd7g625i"); //     prt.bp = 0;
UNSUPPORTED("86dw0xu09q0vf963rwyhthsga"); //     prt.order =
UNSUPPORTED("bi7e99txixk5vn16uz1ewze60"); // 	(256 * (ND_lw(u) + prt.p.x)) / (ND_lw(u) + ND_rw(u));
UNSUPPORTED("2ssajop92yd2a8y22o22ea36z"); //     prt.constrained = 0;
UNSUPPORTED("6oy9nbi9ensh5cuoda16glfkk"); //     prt.defined = NOT(0);
UNSUPPORTED("9vfrhc8s3bav5vsioug70ec8a"); //     prt.clip = 0;
UNSUPPORTED("58u5npq8vw06285kpx39zg9us"); //     prt.dyna = 0;
UNSUPPORTED("26hzjlrn2f97g04rs41bxd6cp"); //     prt.theta = 0;
UNSUPPORTED("6r9ivoxmq49o3gv1yto4zyvi3"); //     prt.side = 0;
UNSUPPORTED("aujriwmmb49o2fxrmsjoedap6"); //     prt.name = NULL;
UNSUPPORTED("3ff91qop4adzc4f4h1lnljqyp"); //     /* assign one of the ports to every edge */
UNSUPPORTED("7qdtxp882tja3q4pjpimsdpz2"); //     for (i = 0; i < l->size; i++) {
UNSUPPORTED("9a2w9ypg4zbrmt9mwxerl9ku9"); // 	e = l->list[i];
UNSUPPORTED("84n057olv326z2v8k45silocb"); // 	arrow_flags(e, &sflag, &eflag);
UNSUPPORTED("2csv6mfvww0031xz126km61c5"); // 	for (; e; e = ED_to_virt(e)) {	/* assign to all virt edges of e */
UNSUPPORTED("9rrwjg2l48y4n9w7ymrr9udrs"); // 	    for (f = e; f;
UNSUPPORTED("cd9trmrmocvmp9gcd5yhpi8j2"); // 		 f = ED_edge_type(f) == 1 &&
UNSUPPORTED("c609sm2glgudtov2qxajroomq"); // 		 ND_node_type(aghead(f)) == 1 &&
UNSUPPORTED("ez0iln0wthfexsk2addlvf968"); // 		 ND_out(aghead(f)).size == 1 ?
UNSUPPORTED("3c7kng7zq5tuk7q0g4368f83a"); // 		 ND_out(aghead(f)).list[0] : NULL) {
UNSUPPORTED("au8chupq2urgaefasiz9q9p4"); // 		if (aghead(f) == u)
UNSUPPORTED("9n4w8egcjmjwmkgptavq9x8on"); // 		    ED_head_port(f) = prt;
UNSUPPORTED("ckzw343313p1917g5yol8mget"); // 		if (agtail(f) == u)
UNSUPPORTED("es5btobfx5kd4ziscx7ym4kf7"); // 		    ED_tail_port(f) = prt;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9rrwjg2l48y4n9w7ymrr9udrs"); // 	    for (f = e; f;
UNSUPPORTED("cd9trmrmocvmp9gcd5yhpi8j2"); // 		 f = ED_edge_type(f) == 1 &&
UNSUPPORTED("9po6nu7kgaeigs2afm08whv2o"); // 		 ND_node_type(agtail(f)) == 1 &&
UNSUPPORTED("49xjryc5nmnom413y2dy0v7gh"); // 		 ND_in(agtail(f)).size == 1 ?
UNSUPPORTED("91kaqt4bbz94s6de4uf9wept7"); // 		 ND_in(agtail(f)).list[0] : NULL) {
UNSUPPORTED("au8chupq2urgaefasiz9q9p4"); // 		if (aghead(f) == u)
UNSUPPORTED("9n4w8egcjmjwmkgptavq9x8on"); // 		    ED_head_port(f) = prt;
UNSUPPORTED("ckzw343313p1917g5yol8mget"); // 		if (agtail(f) == u)
UNSUPPORTED("es5btobfx5kd4ziscx7ym4kf7"); // 		    ED_tail_port(f) = prt;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d7reofoel6ngjj7zza32cdi0w"); //     ND_has_port(u) = NOT(0);	/* kinda pointless, because mincross is already done */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
