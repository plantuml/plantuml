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
package gen.lib.pack;
import static gen.lib.cgraph.attr__c.agget;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agraph_s;
import h.ST_pack_info;
import h.ST_pointf;
import smetana.core.CString;

public class pack__c {
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


//1 exy2l03ceq9zw4vf01bbd4d3o
// typedef unsigned int packval_t


//1 6ddu6mrp88g3kun2w1gg8ck8t
// typedef Dict_t PointSet


//1 6t1gwljnc5qkhgkp9oc8y7lhm
// typedef Dict_t PointMap




//3 bdbcgpx3oes6bcwdhfnfq0wjf
// static int computeStep(int ng, boxf* bbs, int margin) 
public static Object computeStep(Object... arg) {
UNSUPPORTED("8t29o17pwh1az79qvvoe5gzig"); // static int computeStep(int ng, boxf* bbs, int margin)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("55lbmpqutqoil2r1b1kshhf4"); //     double l1, l2;
UNSUPPORTED("a43rbn0mnlegy80nlm8zaa198"); //     double a, b, c, d, r;
UNSUPPORTED("ady3ytrxxbt2q1wph02epzm2x"); //     double W, H;		/* width and height of graph, with margin */
UNSUPPORTED("4wnirjl62mfwcoakp6lyep3p7"); //     int i, root;
UNSUPPORTED("xo2ha29kgc22utvzozfopc3t"); //     a = 100 * ng - 1;
UNSUPPORTED("10mngu08a3ilg9ogq1ucw6uro"); //     c = 0;
UNSUPPORTED("5bkjzlhrfwudnhdqvgcum89jk"); //     b = 0;
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("3u59zam8do4bsljnozwf0fx8s"); // 	boxf bb = bbs[i];
UNSUPPORTED("bzl9bfq6ylumj5u7zvlkdd1fy"); // 	W = bb.UR.x - bb.LL.x + 2 * margin;
UNSUPPORTED("6keu83u4vpu8uuctqbhsxm1f"); // 	H = bb.UR.y - bb.LL.y + 2 * margin;
UNSUPPORTED("66ehok2jts1k8egaj9o3d4mhd"); // 	b -= (W + H);
UNSUPPORTED("ecmu5b20wldgp7ltdenk6r6y3"); // 	c -= (W * H);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6eylx7urr9urjgcpq15mz9mbx"); //     d = b * b - 4.0 * a * c;
UNSUPPORTED("6gohrnij1pp8uigeqwldand78"); //     if (d < 0) {
UNSUPPORTED("aqx00bc7erj1s1idkis6k0xou"); // 	agerr(AGERR, "libpack: disc = %f ( < 0)\n", d);
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7w2mz82iiqh2etpb0eadr0ddt"); //     r = sqrt(d);
UNSUPPORTED("1iy2sivksdztrjf4h3k0algs0"); //     l1 = (-b + r) / (2 * a);
UNSUPPORTED("erlkg1mh0rfh16bj67xa68qmy"); //     l2 = (-b - r) / (2 * a);
UNSUPPORTED("cr6iwpvlne2bcqjtejlgpsa48"); //     root = (int) l1;
UNSUPPORTED("1gg2fab0bfmz9yl7zohktb3g9"); //     if (root == 0) root = 1;
UNSUPPORTED("3kxq91b30qey91r4hkc9jrb9n"); //     if (Verbose > 2) {
UNSUPPORTED("55e0qgjsydjz1tjye67igkeny"); // 	fprintf(stderr, "Packing: compute grid size\n");
UNSUPPORTED("eirgraz2ak76fhuf5dbajvv02"); // 	fprintf(stderr, "a %f b %f c %f d %f r %f\n", a, b, c, d, r);
UNSUPPORTED("eekm76wxww26ubtn5v9ypypiy"); // 	fprintf(stderr, "root %d (%f) %d (%f)\n", root, l1, (int) l2,
UNSUPPORTED("eilpovu9tr2mn82840g4gwduy"); // 		l2);
UNSUPPORTED("5pjri22kxtwvbb3hbf0n1qput"); // 	fprintf(stderr, " r1 %f r2 %f\n", a * l1 * l1 + b * l1 + c,
UNSUPPORTED("3h2jsnh1591w0kbq88cmk89ul"); // 		a * l2 * l2 + b * l2 + c);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4hzlvk8t0qcn15wg6988vwra9"); //     return root;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dob2o9m8sfsrsm1g93hfznsp
// static int cmpf(const void *X, const void *Y) 
public static Object cmpf(Object... arg) {
UNSUPPORTED("4nc6jpvian95rmtwnhxuaib4b"); // static int cmpf(const void *X, const void *Y)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2eux9vyonke173k6wx67nvmeu"); //     ginfo *x = *(ginfo **) X;
UNSUPPORTED("4rofn9loku05xrr4fp3slcs6r"); //     ginfo *y = *(ginfo **) Y;
UNSUPPORTED("95s1jm8x7y2tc9y914raaljrs"); //     /* flip order to get descending array */
UNSUPPORTED("46lhhdh1hfndiefr57nzqj6ib"); //     return (y->perim - x->perim);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4r8s4k6p01ynfz88kz50ttlr8
// void fillLine(pointf p, pointf q, PointSet * ps) 
public static Object fillLine(Object... arg) {
UNSUPPORTED("a5130f5j305uj46zjcopit703"); // void fillLine(pointf p, pointf q, PointSet * ps)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1duvjyh47lxf7d7ue5car0vsj"); //     int x1 = ROUND(p.x);
UNSUPPORTED("9pzzawvcmpw6i93hjb26rq63h"); //     int y1 = ROUND(p.y);
UNSUPPORTED("3057bhojttdigycfgf1lyio0f"); //     int x2 = ROUND(q.x);
UNSUPPORTED("5bgjp0mm0o564h53ba0zrnbhq"); //     int y2 = ROUND(q.y);
UNSUPPORTED("d8jccesapcqy22p9y3vtpm616"); //     int d, x, y, ax, ay, sx, sy, dx, dy;
UNSUPPORTED("6vdso33syc5f2j40e5bsuhb83"); //     dx = x2 - x1;
UNSUPPORTED("6zrp5a2timck4sou5qym3hsrd"); //     ax = ABS(dx) << 1;
UNSUPPORTED("ay754u242psxjbqm7n8azez52"); //     sx = SGN(dx);
UNSUPPORTED("cmvkhiqbscc6cgktpynqycfhn"); //     dy = y2 - y1;
UNSUPPORTED("eigqi5q47z9sk7utvxv72dif0"); //     ay = ABS(dy) << 1;
UNSUPPORTED("1ifl8r2gf1amwzvvvfohlx6uw"); //     sy = SGN(dy);
UNSUPPORTED("2x97iysx5j91wtf3wmgeo2pt"); // /* fprintf (stderr, "fillLine %d %d - %d %d\n", x1,y1,x2,y2); */
UNSUPPORTED("9y4duec6l30q5clw8qd85w784"); //     x = x1;
UNSUPPORTED("6rlb844uhus8twyid93q8kqbs"); //     y = y1;
UNSUPPORTED("ccle7h8pebi673oczx2fj5nxi"); //     if (ax > ay) {              /* x dominant */
UNSUPPORTED("7h60osio7amm1igvh0h7ipgx9"); //         d = ay - (ax >> 1);
UNSUPPORTED("2v2pue2y1u2d4ka6733iv5vk0"); //         for (;;) {
UNSUPPORTED("13fg1e71tu92h0bfrs9xka8tb"); // /* fprintf (stderr, "  addPS %d %d\n", x,y); */
UNSUPPORTED("62ytgdd26o3p2xgoq37xnoifl"); //             addPS(ps, x, y);
UNSUPPORTED("2lero25dpfd7f3wrrhmxhly2h"); //             if (x == x2)
UNSUPPORTED("6an8ocqq0sjru42k4aathe94m"); //                 return;
UNSUPPORTED("9erzrv938084t7vmam5bxw7mx"); //             if (d >= 0) {
UNSUPPORTED("9cka76ykusjv3k3y8fda4nfk0"); //                 y += sy;
UNSUPPORTED("bynlwjpgqdvo01z64gm1sf2ga"); //                 d -= ax;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("1vyuktjou7iccegm28w06drqv"); //             x += sx;
UNSUPPORTED("3a5mkjlhp2t10gtqh3qj69q2v"); //             d += ay;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("161urjc4hdupo7zswtjf376th"); //     } else {                    /* y dominant */
UNSUPPORTED("39keqb5njfkczwllbrur21irg"); //         d = ax - (ay >> 1);
UNSUPPORTED("2v2pue2y1u2d4ka6733iv5vk0"); //         for (;;) {
UNSUPPORTED("13fg1e71tu92h0bfrs9xka8tb"); // /* fprintf (stderr, "  addPS %d %d\n", x,y); */
UNSUPPORTED("62ytgdd26o3p2xgoq37xnoifl"); //             addPS(ps, x, y);
UNSUPPORTED("colj1ywjuo0sy4arrbdvl8jfd"); //             if (y == y2)
UNSUPPORTED("6an8ocqq0sjru42k4aathe94m"); //                 return;
UNSUPPORTED("9erzrv938084t7vmam5bxw7mx"); //             if (d >= 0) {
UNSUPPORTED("14xdaydif31mwi2l1z4k2oepc"); //                 x += sx;
UNSUPPORTED("jjd6xfq9lhh82zyxg0ikry3"); //                 d -= ay;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("58q6s3oua65hpto8xa7dz10sf"); //             y += sy;
UNSUPPORTED("d3ci5x842hrarrzjeputfalm"); //             d += ax;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6ewq3stx5of82qf0ol1xwjf0o
// static void fillEdge(Agedge_t * e, point p, PointSet * ps, int dx, int dy, 	 int ssize, int doS) 
public static Object fillEdge(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9b96jon8bxkz7znkmv20875w6"); // fillEdge(Agedge_t * e, point p, PointSet * ps, int dx, int dy,
UNSUPPORTED("8yrc5v6qafl4vg0xln2we5kn3"); // 	 int ssize, int doS)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9e6bnowy6jfhnib5uev3scpsu"); //     int j, k;
UNSUPPORTED("37thdceezsvepe7tlyfatrbcw"); //     bezier bz;
UNSUPPORTED("9j8f6779znpou18ci9dqadum3"); //     pointf pt, hpt;
UNSUPPORTED("15nfkonrgeitraj70893dwyoj"); //     Agnode_t *h;
UNSUPPORTED("5adeloig3qt9512e0wlnw0stj"); //     P2PF(p, pt);
UNSUPPORTED("9k8qdzbe2z8whvdrbl13aayb6"); //     /* If doS is false or the edge has not splines, use line segment */
UNSUPPORTED("bd6qvosxwfyw4x4l6o4442ous"); //     if (!doS || !ED_spl(e)) {
UNSUPPORTED("1058dpzesyxnmmn0hdydhfm1n"); // 	h = aghead(e);
UNSUPPORTED("dyy30pakjvyqg13qemnd5v50i"); // 	hpt = coord(h);
UNSUPPORTED("d3qru0pr047p8fj3evxw9gstq"); // 	((hpt).x += dx, (hpt).y += dy);
UNSUPPORTED("99rq045btzmqomibgmhvv8wce"); // 	((hpt).x = (((hpt).x) >= 0 ? ((hpt).x)/(ssize) : ((((hpt).x)+1)/(ssize))-1), (hpt).y = (((hpt).y) >= 0 ? ((hpt).y)/((ssize)) : ((((hpt).y)+1)/((ssize)))-1));
UNSUPPORTED("74hokpj1xz0ky4pomxsicfkvi"); // 	fillLine(pt, hpt, ps);
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e9vow5lzqvgcxabgjj99yokjr"); //     for (j = 0; j < ED_spl(e)->size; j++) {
UNSUPPORTED("8ni4zfvf78h93ucjuoqtuomjh"); // 	bz = ED_spl(e)->list[j];
UNSUPPORTED("2qc36pchob3dha7c8jedwalcn"); // 	if (bz.sflag) {
UNSUPPORTED("d5qnj0ut8jldhlvcj8dgvm1r5"); // 	    pt = bz.sp;
UNSUPPORTED("8h1rn4kivdousjdpfa1mmawwm"); // 	    hpt = bz.list[0];
UNSUPPORTED("7hf6867dr3c65w2pf4xq323mu"); // 	    k = 1;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("bf65xvm2dou3rh6h7knsa2us5"); // 	    pt = bz.list[0];
UNSUPPORTED("3z2k5orut98eo0vq0pyevi2ed"); // 	    hpt = bz.list[1];
UNSUPPORTED("dz2m8yuk8yfnc2srukp3nh4tc"); // 	    k = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cgn9befmvtd4d8yfqs126lbpq"); // 	((pt).x += dx, (pt).y += dy);
UNSUPPORTED("dpf9cre3x7edch35g0alvo167"); // 	((pt).x = (((pt).x) >= 0 ? ((pt).x)/(ssize) : ((((pt).x)+1)/(ssize))-1), (pt).y = (((pt).y) >= 0 ? ((pt).y)/((ssize)) : ((((pt).y)+1)/((ssize)))-1));
UNSUPPORTED("d3qru0pr047p8fj3evxw9gstq"); // 	((hpt).x += dx, (hpt).y += dy);
UNSUPPORTED("99rq045btzmqomibgmhvv8wce"); // 	((hpt).x = (((hpt).x) >= 0 ? ((hpt).x)/(ssize) : ((((hpt).x)+1)/(ssize))-1), (hpt).y = (((hpt).y) >= 0 ? ((hpt).y)/((ssize)) : ((((hpt).y)+1)/((ssize)))-1));
UNSUPPORTED("74hokpj1xz0ky4pomxsicfkvi"); // 	fillLine(pt, hpt, ps);
UNSUPPORTED("6gftmkq3mhknwzkm4an4llzet"); // 	for (; k < bz.size; k++) {
UNSUPPORTED("a9t723l1szseqti9o12357emd"); // 	    pt = hpt;
UNSUPPORTED("b960h12noe0vcvmcdqt04yz5r"); // 	    hpt = bz.list[k];
UNSUPPORTED("uxitedizhv3r59kwo9hloxga"); // 	    ((hpt).x += dx, (hpt).y += dy);
UNSUPPORTED("g2ys8m7lhj8ygyslxd1awd8c"); // 	    ((hpt).x = (((hpt).x) >= 0 ? ((hpt).x)/(ssize) : ((((hpt).x)+1)/(ssize))-1), (hpt).y = (((hpt).y) >= 0 ? ((hpt).y)/((ssize)) : ((((hpt).y)+1)/((ssize)))-1));
UNSUPPORTED("b6h1kxqo0bbiehrmk1374qmig"); // 	    fillLine(pt, hpt, ps);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("973cbk976xio10xxw9bgkyen2"); // 	if (bz.eflag) {
UNSUPPORTED("a9t723l1szseqti9o12357emd"); // 	    pt = hpt;
UNSUPPORTED("mppggrplo1gua39zwczw05dl"); // 	    hpt = bz.ep;
UNSUPPORTED("uxitedizhv3r59kwo9hloxga"); // 	    ((hpt).x += dx, (hpt).y += dy);
UNSUPPORTED("g2ys8m7lhj8ygyslxd1awd8c"); // 	    ((hpt).x = (((hpt).x) >= 0 ? ((hpt).x)/(ssize) : ((((hpt).x)+1)/(ssize))-1), (hpt).y = (((hpt).y) >= 0 ? ((hpt).y)/((ssize)) : ((((hpt).y)+1)/((ssize)))-1));
UNSUPPORTED("b6h1kxqo0bbiehrmk1374qmig"); // 	    fillLine(pt, hpt, ps);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1qqcfh1obd0wpw60zt3qb10o1
// static void genBox(boxf bb0, ginfo * info, int ssize, int margin, point center, char* s) 
public static Object genBox(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("dkh8x4gma6inyhel8ms5kdqyn"); // genBox(boxf bb0, ginfo * info, int ssize, int margin, point center, char* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aqkjgsdfo55oljkejalj18x5o"); //     PointSet *ps;
UNSUPPORTED("d9cafq96lxhglzixa87lnoi5j"); //     int W, H;
UNSUPPORTED("20jw6e25uf75jfj884w81pyn6"); //     point UR, LL;
UNSUPPORTED("dli0hhfxexiypybw00gp6530v"); //     box bb;
UNSUPPORTED("7ksbgqgqrh7y32ijnkxl8zaw0"); //     int x, y;
UNSUPPORTED("bmfwjlyug1vwjdhkwhf2qxh0a"); //     BF2B(bb0, bb);
UNSUPPORTED("6udv07xykmb0e2pdyqurnc9p4"); //     ps = newPS();
UNSUPPORTED("6xe56afm9k659fq07xtcn9147"); //     LL.x = center.x - margin;
UNSUPPORTED("dh3kj8xysu2kv9k3a983iqav3"); //     LL.y = center.y - margin;
UNSUPPORTED("585l7y7guge9z8fsbbxli30rj"); //     UR.x = center.x + bb.UR.x - bb.LL.x + margin;
UNSUPPORTED("e584696y40atkpz430l6plczr"); //     UR.y = center.y + bb.UR.y - bb.LL.y + margin;
UNSUPPORTED("9v2053h0uv8gyfep4zczhjyd"); //     ((LL).x = (((LL).x) >= 0 ? ((LL).x)/(ssize) : ((((LL).x)+1)/(ssize))-1), (LL).y = (((LL).y) >= 0 ? ((LL).y)/((ssize)) : ((((LL).y)+1)/((ssize)))-1));
UNSUPPORTED("de40ubd0t9d1rv08pcspaltry"); //     ((UR).x = (((UR).x) >= 0 ? ((UR).x)/(ssize) : ((((UR).x)+1)/(ssize))-1), (UR).y = (((UR).y) >= 0 ? ((UR).y)/((ssize)) : ((((UR).y)+1)/((ssize)))-1));
UNSUPPORTED("afbpk7wdnlzfrfjsfanv0nxp9"); //     for (x = LL.x; x <= UR.x; x++)
UNSUPPORTED("eqzaosdvrone8rhgu36tsk45v"); // 	for (y = LL.y; y <= UR.y; y++)
UNSUPPORTED("5jwcnu6k267vszq6le3zpltr2"); // 	    addPS(ps, x, y);
UNSUPPORTED("b6965k7cvxgazo4d1x1gpicax"); //     info->cells = pointsOf(ps);
UNSUPPORTED("79rp86itd9b91tc0db54220fr"); //     info->nc = sizeOf(ps);
UNSUPPORTED("agmsrp2u25tarnb04rrmz8szf"); //     W = ((int)ceil((bb0.UR.x - bb0.LL.x + 2 * margin)/(ssize)));
UNSUPPORTED("37qvodfrig2t4hcn9v3c7fita"); //     H = ((int)ceil((bb0.UR.y - bb0.LL.y + 2 * margin)/(ssize)));
UNSUPPORTED("2w04og11wkyoe6e59x489rdlp"); //     info->perim = W + H;
UNSUPPORTED("3kxq91b30qey91r4hkc9jrb9n"); //     if (Verbose > 2) {
UNSUPPORTED("bbuxsg26kpzb2fl660hjri9l8"); // 	int i;
UNSUPPORTED("f23g1eoeiffc45ucsecqxqkap"); // 	fprintf(stderr, "%s no. cells %d W %d H %d\n",
UNSUPPORTED("dbm7epl7o6mpeo72klw135uni"); // 		s, info->nc, W, H);
UNSUPPORTED("5raufl639beqv87bsfap22ar5"); // 	for (i = 0; i < info->nc; i++)
UNSUPPORTED("36wdram8nco5bacym97n1egc0"); // 	    fprintf(stderr, "  %d %d cell\n", info->cells[i].x,
UNSUPPORTED("606tqxapdsuzdhl2ssic0tz2v"); // 		    info->cells[i].y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dg5ijmmysdxc2qm5j58u0zr8i"); //     freePS(ps);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5un21g6597dr1wxwpjststcy2
// static int genPoly(Agraph_t * root, Agraph_t * g, ginfo * info, 	int ssize, pack_info * pinfo, point center) 
public static Object genPoly(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("29xjrg34ztih2ce44tcl0h5dl"); // genPoly(Agraph_t * root, Agraph_t * g, ginfo * info,
UNSUPPORTED("103ox5jcn94jkt4i92802xe30"); // 	int ssize, pack_info * pinfo, point center)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aqkjgsdfo55oljkejalj18x5o"); //     PointSet *ps;
UNSUPPORTED("d9cafq96lxhglzixa87lnoi5j"); //     int W, H;
UNSUPPORTED("9z1w1elfjsy9fmy4de64anci3"); //     point LL, UR;
UNSUPPORTED("du26lyeqo8abix4w0d22tb9t8"); //     point pt, s2;
UNSUPPORTED("bz0wmxtnzgka3zfovkbr7eozh"); //     pointf ptf;
UNSUPPORTED("y2e8aue8lq20s36dp5cktigw"); //     Agraph_t *eg;		/* graph containing edges */
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("36vshotvjkc5iodgg7nq6qa2r"); //     Agedge_t *e;
UNSUPPORTED("7ksbgqgqrh7y32ijnkxl8zaw0"); //     int x, y;
UNSUPPORTED("bftz89757hgmx7ivuna8lc4z6"); //     int dx, dy;
UNSUPPORTED("8uujemixuhlf040icq3zsh7j8"); //     graph_t *subg;
UNSUPPORTED("bgh1ykg5jxme381zrqlfzre56"); //     int margin = pinfo->margin;
UNSUPPORTED("7l72pjfqpydwx4ojgn4x8l6u7"); //     int doSplines = pinfo->doSplines;
UNSUPPORTED("dli0hhfxexiypybw00gp6530v"); //     box bb;
UNSUPPORTED("dqseu01ng2nnpewmpp95sm688"); //     if (root)
UNSUPPORTED("3nj280m96ug062kyr3cutmoqh"); // 	eg = root;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("bcwj9q77uro493bbvuwbpo5vy"); // 	eg = g;
UNSUPPORTED("6udv07xykmb0e2pdyqurnc9p4"); //     ps = newPS();
UNSUPPORTED("9i5jau3uugaz7oitfcmhousx1"); //     dx = center.x - ROUND(GD_bb(g).LL.x);
UNSUPPORTED("66eemj62brnu3mglipwc7cqii"); //     dy = center.y - ROUND(GD_bb(g).LL.y);
UNSUPPORTED("dj9kkvxpyum7mive0tpr3vfod"); //     if (pinfo->mode == l_clust) {
UNSUPPORTED("bbuxsg26kpzb2fl660hjri9l8"); // 	int i;
UNSUPPORTED("5tckx2f0jgn7lryi9m2e7btre"); // 	void **alg;
UNSUPPORTED("3sna9ll8elsitmdj8rs4m15cr"); // 	/* backup the alg data */
UNSUPPORTED("cbau5vo7rkixloiylj1lwlytg"); // 	alg = (void **)gmalloc((agnnodes(g))*sizeof(void *));
UNSUPPORTED("1rckxjxqzxb846omrtt9yuxw3"); // 	for (i = 0, n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("4xioornmodd5ydv19wigsz1lu"); // 	    alg[i++] = ND_alg(n);
UNSUPPORTED("bqtu555nwh760awky8m65eec2"); // 	    ND_alg(n) = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5s8wtxkay1fm0fn50m39tk6fj"); // 	/* do bbox of top clusters */
UNSUPPORTED("axxs2u0a8b8lf42oqy6xq0bx1"); // 	for (i = 1; i <= GD_n_cluster(g); i++) {
UNSUPPORTED("2vrnig406r4mku4kxy2kf648a"); // 	    subg = GD_clust(g)[i];
UNSUPPORTED("pfi3ma6w50rsu99wqswvhjgx"); // 	    BF2B(GD_bb(subg), bb);
UNSUPPORTED("btynl1nm8k1pr7vp22utdesdj"); // 	    if ((bb.UR.x > bb.LL.x) && (bb.UR.y > bb.LL.y)) {
UNSUPPORTED("ev14habqjuz1vhqxmavftlqlh"); // 		((bb.LL).x += dx, (bb.LL).y += dy);
UNSUPPORTED("bdzc32hvn2ybrea165mrd66rg"); // 		((bb.UR).x += dx, (bb.UR).y += dy);
UNSUPPORTED("55n48hmw8i7wktq78vfub4obw"); // 		bb.LL.x -= margin;
UNSUPPORTED("3756d2o8pocqpkunmmdj28zmf"); // 		bb.LL.y -= margin;
UNSUPPORTED("5urkhackfkfy3g74mu9xtpk5d"); // 		bb.UR.x += margin;
UNSUPPORTED("9pw4vnniswa2mnfe3olr8ciey"); // 		bb.UR.y += margin;
UNSUPPORTED("7thgtnyonec8a9sn8hizox5ig"); // 		((bb.LL).x = (((bb.LL).x) >= 0 ? ((bb.LL).x)/(ssize) : ((((bb.LL).x)+1)/(ssize))-1), (bb.LL).y = (((bb.LL).y) >= 0 ? ((bb.LL).y)/((ssize)) : ((((bb.LL).y)+1)/((ssize)))-1));
UNSUPPORTED("f33jy5tuo13bi4db8gwf2ilvy"); // 		((bb.UR).x = (((bb.UR).x) >= 0 ? ((bb.UR).x)/(ssize) : ((((bb.UR).x)+1)/(ssize))-1), (bb.UR).y = (((bb.UR).y) >= 0 ? ((bb.UR).y)/((ssize)) : ((((bb.UR).y)+1)/((ssize)))-1));
UNSUPPORTED("2fzfb48cw1tpgnx1fenamdt17"); // 		for (x = bb.LL.x; x <= bb.UR.x; x++)
UNSUPPORTED("dw8r9tjg3wg1jpgi9dg0ordai"); // 		    for (y = bb.LL.y; y <= bb.UR.y; y++)
UNSUPPORTED("9fsyndaeb3wa4vnzkwju02aa6"); // 			addPS(ps, x, y);
UNSUPPORTED("b9ww7g26bsulxmdli1uykqg48"); // 		/* note which nodes are in clusters */
UNSUPPORTED("78m4mkxem2uguubk1gpgsx6oc"); // 		for (n = agfstnode(subg); n; n = agnxtnode(subg, n))
UNSUPPORTED("8uh55vusbiw7a90rj8jzyf76o"); // 		    ND_clust(n) = subg;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ehh6v9228lmsahzxxt4e6a0f9"); // 	/* now do remaining nodes and edges */
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("6kr9j6zxr0o4iy7nnp40lvuvz"); // 	    ptf = coord(n);
UNSUPPORTED("75lvkqsq23pobdcg6bmqa6mt9"); // 	    PF2P(ptf, pt);
UNSUPPORTED("4rskihc5j4vz55z4tvwua5wpa"); // 	    ((pt).x += dx, (pt).y += dy);
UNSUPPORTED("1pf6jaggdcnmdld5nu4v22mh9"); // 	    if (!ND_clust(n)) {	/* n is not in a top-level cluster */
UNSUPPORTED("2hsx1r535mu25g5wpz8r66dbw"); // 		s2.x = margin + ND_xsize(n) / 2;
UNSUPPORTED("dpwfob5uc018fjpxki2nqa0si"); // 		s2.y = margin + ND_ysize(n) / 2;
UNSUPPORTED("d21twu3noz0h6l4k80bk03bfj"); // 		LL = sub_point(pt, s2);
UNSUPPORTED("pje1a01729779s85awuaqnan"); // 		UR = add_point(pt, s2);
UNSUPPORTED("60bthwtp8xgwr83vnyczt23xz"); // 		((LL).x = (((LL).x) >= 0 ? ((LL).x)/(ssize) : ((((LL).x)+1)/(ssize))-1), (LL).y = (((LL).y) >= 0 ? ((LL).y)/((ssize)) : ((((LL).y)+1)/((ssize)))-1));
UNSUPPORTED("einuv6rwbjqts3wnufwpslxlv"); // 		((UR).x = (((UR).x) >= 0 ? ((UR).x)/(ssize) : ((((UR).x)+1)/(ssize))-1), (UR).y = (((UR).y) >= 0 ? ((UR).y)/((ssize)) : ((((UR).y)+1)/((ssize)))-1));
UNSUPPORTED("83q0u30721zb0pgciso7ziyt8"); // 		for (x = LL.x; x <= UR.x; x++)
UNSUPPORTED("8c9342p5vvoot1zdjdtqlz2m9"); // 		    for (y = LL.y; y <= UR.y; y++)
UNSUPPORTED("9fsyndaeb3wa4vnzkwju02aa6"); // 			addPS(ps, x, y);
UNSUPPORTED("dxoo8z7t9s82hnv32b8inj5fl"); // 		((pt).x = (((pt).x) >= 0 ? ((pt).x)/(ssize) : ((((pt).x)+1)/(ssize))-1), (pt).y = (((pt).y) >= 0 ? ((pt).y)/((ssize)) : ((((pt).y)+1)/((ssize)))-1));
UNSUPPORTED("av9pal9kng6v9bj1n1r76p890"); // 		for (e = agfstout(eg, n); e; e = agnxtout(eg, e)) {
UNSUPPORTED("3244vgtt0iocuk7pgdpymweq8"); // 		    fillEdge(e, pt, ps, dx, dy, ssize, doSplines);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("dxoo8z7t9s82hnv32b8inj5fl"); // 		((pt).x = (((pt).x) >= 0 ? ((pt).x)/(ssize) : ((((pt).x)+1)/(ssize))-1), (pt).y = (((pt).y) >= 0 ? ((pt).y)/((ssize)) : ((((pt).y)+1)/((ssize)))-1));
UNSUPPORTED("av9pal9kng6v9bj1n1r76p890"); // 		for (e = agfstout(eg, n); e; e = agnxtout(eg, e)) {
UNSUPPORTED("ea4ls3030vg98zyoy7palapff"); // 		    if (ND_clust(n) == ND_clust(aghead(e)))
UNSUPPORTED("91ilj0e8yph90t7k4ijuq5rb0"); // 			continue;
UNSUPPORTED("3244vgtt0iocuk7pgdpymweq8"); // 		    fillEdge(e, pt, ps, dx, dy, ssize, doSplines);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("33gzyqd0dti267fbnuc393vqf"); // 	/* restore the alg data */
UNSUPPORTED("1rckxjxqzxb846omrtt9yuxw3"); // 	for (i = 0, n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("7d40zxbnht9hibe4reayjt3ab"); // 	    ND_alg(n)= alg[i++];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1avb9rstkf5v05swj5oavmbsx"); // 	free(alg);
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("6kr9j6zxr0o4iy7nnp40lvuvz"); // 	    ptf = coord(n);
UNSUPPORTED("75lvkqsq23pobdcg6bmqa6mt9"); // 	    PF2P(ptf, pt);
UNSUPPORTED("4rskihc5j4vz55z4tvwua5wpa"); // 	    ((pt).x += dx, (pt).y += dy);
UNSUPPORTED("32wulfgkqo1gdziiuynkzohma"); // 	    s2.x = margin + ND_xsize(n) / 2;
UNSUPPORTED("asa04lxhwek7h53q45xwt9eo3"); // 	    s2.y = margin + ND_ysize(n) / 2;
UNSUPPORTED("e4pwp7ppfey2ur2rzi0bnbarb"); // 	    LL = sub_point(pt, s2);
UNSUPPORTED("dtfkzedesg45sgmdx85bqajb"); // 	    UR = add_point(pt, s2);
UNSUPPORTED("2lukx7z9l672b9an4yd3kimpc"); // 	    ((LL).x = (((LL).x) >= 0 ? ((LL).x)/(ssize) : ((((LL).x)+1)/(ssize))-1), (LL).y = (((LL).y) >= 0 ? ((LL).y)/((ssize)) : ((((LL).y)+1)/((ssize)))-1));
UNSUPPORTED("1rt3uhmbxplvrk1o7obv4z3er"); // 	    ((UR).x = (((UR).x) >= 0 ? ((UR).x)/(ssize) : ((((UR).x)+1)/(ssize))-1), (UR).y = (((UR).y) >= 0 ? ((UR).y)/((ssize)) : ((((UR).y)+1)/((ssize)))-1));
UNSUPPORTED("dx18nl37frf8sg26xij552ejz"); // 	    for (x = LL.x; x <= UR.x; x++)
UNSUPPORTED("6a90v0hbdo93le7neim40no3u"); // 		for (y = LL.y; y <= UR.y; y++)
UNSUPPORTED("dn4dbpnefy79i1g03hfsakcno"); // 		    addPS(ps, x, y);
UNSUPPORTED("8hl3q69p8aqetblqadqf2uhkf"); // 	    ((pt).x = (((pt).x) >= 0 ? ((pt).x)/(ssize) : ((((pt).x)+1)/(ssize))-1), (pt).y = (((pt).y) >= 0 ? ((pt).y)/((ssize)) : ((((pt).y)+1)/((ssize)))-1));
UNSUPPORTED("7hqutkde2o0fy2g0xv1yitxjf"); // 	    for (e = agfstout(eg, n); e; e = agnxtout(eg, e)) {
UNSUPPORTED("6zc0jmxujx99ljtqkmiydrjzq"); // 		fillEdge(e, pt, ps, dx, dy, ssize, doSplines);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("b6965k7cvxgazo4d1x1gpicax"); //     info->cells = pointsOf(ps);
UNSUPPORTED("79rp86itd9b91tc0db54220fr"); //     info->nc = sizeOf(ps);
UNSUPPORTED("es3gh0zqwlw0iiolk6x45vege"); //     W = ((int)ceil((GD_bb(g).UR.x - GD_bb(g).LL.x + 2 * margin)/(ssize)));
UNSUPPORTED("bfr7ygy7lkfhss45a84thsa7s"); //     H = ((int)ceil((GD_bb(g).UR.y - GD_bb(g).LL.y + 2 * margin)/(ssize)));
UNSUPPORTED("2w04og11wkyoe6e59x489rdlp"); //     info->perim = W + H;
UNSUPPORTED("3kxq91b30qey91r4hkc9jrb9n"); //     if (Verbose > 2) {
UNSUPPORTED("bbuxsg26kpzb2fl660hjri9l8"); // 	int i;
UNSUPPORTED("f23g1eoeiffc45ucsecqxqkap"); // 	fprintf(stderr, "%s no. cells %d W %d H %d\n",
UNSUPPORTED("dhnvei7zsgifid5vebovzl15"); // 		agnameof(g), info->nc, W, H);
UNSUPPORTED("5raufl639beqv87bsfap22ar5"); // 	for (i = 0; i < info->nc; i++)
UNSUPPORTED("36wdram8nco5bacym97n1egc0"); // 	    fprintf(stderr, "  %d %d cell\n", info->cells[i].x,
UNSUPPORTED("606tqxapdsuzdhl2ssic0tz2v"); // 		    info->cells[i].y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dg5ijmmysdxc2qm5j58u0zr8i"); //     freePS(ps);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c9qfbamaiq4liwngpzcw4g15e
// static int fits(int x, int y, ginfo * info, PointSet * ps, point * place, int step, boxf* bbs) 
public static Object fits(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("1qf35tpyffbtwwrvw009al0jw"); // fits(int x, int y, ginfo * info, PointSet * ps, point * place, int step, boxf* bbs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7s7b72q8pqs2vwdmytpwqd8fw"); //     point *cells = info->cells;
UNSUPPORTED("dx19rog6lno8k3o89p0yw80de"); //     int n = info->nc;
UNSUPPORTED("8w8wc32jqtifc9dhh8tzlcnzx"); //     point cell;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("bs4odh29a6rnkbmhk59sanyar"); //     point LL;
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("ct2td16327xzczczmc9cxbxhr"); // 	cell = *cells;
UNSUPPORTED("8cjpqypsv5o9zwngain8itc5l"); // 	cell.x += x;
UNSUPPORTED("8yup3gihmsdumxy94c06s7514"); // 	cell.y += y;
UNSUPPORTED("qak41spsdwyya6gwwbgwkl53"); // 	if (inPS(ps, cell))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("cbdyrzw9tiul7z5wurhto17q"); // 	cells++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2bp5zif4pew12zbwc252fzewz"); //     PF2P(bbs[info->index].LL, LL);
UNSUPPORTED("d9jkxf05nyf0mf7eh3mig9p6"); //     place->x = step * x - LL.x;
UNSUPPORTED("ciocs0be2oq5sze7g0mgy1k3c"); //     place->y = step * y - LL.y;
UNSUPPORTED("1eqiy1kl714gqpoigzeqcvf5p"); //     cells = info->cells;
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("ct2td16327xzczczmc9cxbxhr"); // 	cell = *cells;
UNSUPPORTED("8cjpqypsv5o9zwngain8itc5l"); // 	cell.x += x;
UNSUPPORTED("8yup3gihmsdumxy94c06s7514"); // 	cell.y += y;
UNSUPPORTED("g4rk5u461kggq0l6ilauwlo4"); // 	insertPS(ps, cell);
UNSUPPORTED("cbdyrzw9tiul7z5wurhto17q"); // 	cells++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("41xjj6amd58fjieqo5xy29iwq"); //     if (Verbose >= 2)
UNSUPPORTED("69kux3f6h23w09bw3rza60lq8"); // 	fprintf(stderr, "cc (%d cells) at (%d,%d) (%d,%d)\n", n, x, y,
UNSUPPORTED("5aew9usp1puf2vjfyvkt8ki4u"); // 		place->x, place->y);
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3bievtf2w4iqqz5qhvvye4imw
// static void placeFixed(ginfo * info, PointSet * ps, point * place, point center) 
public static Object placeFixed(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("5u8hxdr88fztn78otn0zptwfo"); // placeFixed(ginfo * info, PointSet * ps, point * place, point center)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7s7b72q8pqs2vwdmytpwqd8fw"); //     point *cells = info->cells;
UNSUPPORTED("dx19rog6lno8k3o89p0yw80de"); //     int n = info->nc;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("dxwnlmthaq0wgr96pkmv2fk9e"); //     place->x = -center.x;
UNSUPPORTED("6btj6uln4as29swn0gnfjlfrb"); //     place->y = -center.y;
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("chepmmzu07rgo2s6lhxc1yust"); // 	insertPS(ps, *cells++);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("41xjj6amd58fjieqo5xy29iwq"); //     if (Verbose >= 2)
UNSUPPORTED("efy6uprafzlbzcuuea0iywmn"); // 	fprintf(stderr, "cc (%d cells) at (%d,%d)\n", n, place->x,
UNSUPPORTED("erkjyjoclu003wz9pn5id7r1n"); // 		place->y);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6ly0zu2bl9e2z9zb6tgiis6ji
// static void placeGraph(int i, ginfo * info, PointSet * ps, point * place, int step, 	   int margin, boxf* bbs) 
public static Object placeGraph(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("bomsrpn26g609cfz6rxujk3bu"); // placeGraph(int i, ginfo * info, PointSet * ps, point * place, int step,
UNSUPPORTED("bqojgt4ai0dr1qe25n6dgk8zp"); // 	   int margin, boxf* bbs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ksbgqgqrh7y32ijnkxl8zaw0"); //     int x, y;
UNSUPPORTED("d9cafq96lxhglzixa87lnoi5j"); //     int W, H;
UNSUPPORTED("90urniwcsawwori7426tta0kx"); //     int bnd;
UNSUPPORTED("3cvtov5t3e3nw7jf8qx7h8eeo"); //     boxf bb = bbs[info->index];
UNSUPPORTED("bpwdzzlwgsm5lqfqgzplsw92a"); //     if (i == 0) {
UNSUPPORTED("3zg9yd27ewzn95seu5sp3bxw7"); // 	W = ((int)ceil((bb.UR.x - bb.LL.x + 2 * margin)/(step)));
UNSUPPORTED("emdieik6twlh1vjo5ywsbcdaz"); // 	H = ((int)ceil((bb.UR.y - bb.LL.y + 2 * margin)/(step)));
UNSUPPORTED("65t43oobc63x9wvv9o6muhprh"); // 	if (fits(-W / 2, -H / 2, info, ps, place, step, bbs))
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("18spugm2e70i89b481k3i5w70"); //     if (fits(0, 0, info, ps, place, step, bbs))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("jx7bqatqf7k5p5w0ci42dfd8"); //     W = ceil(bb.UR.x - bb.LL.x);
UNSUPPORTED("1sc44fow0xifkubpnp87bjwp"); //     H = ceil(bb.UR.y - bb.LL.y);
UNSUPPORTED("8wngkvmkilwdbxat9rddy9qv6"); //     if (W >= H) {
UNSUPPORTED("1xe0f3svmp0ieofq7s2ss9z2n"); // 	for (bnd = 1;; bnd++) {
UNSUPPORTED("9i002a0gwbjlfbzmkcc26n16h"); // 	    x = 0;
UNSUPPORTED("bbyf4zi7pju6lcgspmut3qygt"); // 	    y = -bnd;
UNSUPPORTED("dhlxiqgb2o6a1bomoafek4b66"); // 	    for (; x < bnd; x++)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("eyxe9lc00uz4eqb1uii15fjc1"); // 	    for (; y < bnd; y++)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("9s5n7ukau4s2kpxp4szrf537q"); // 	    for (; x > -bnd; x--)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("2p7aqe0xpy2yshac8hs4h8boz"); // 	    for (; y > -bnd; y--)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("7zfcwps71uh29bw8vxk0wu5r6"); // 	    for (; x < 0; x++)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("1xe0f3svmp0ieofq7s2ss9z2n"); // 	for (bnd = 1;; bnd++) {
UNSUPPORTED("3suauipkdshia74hhot2qydd6"); // 	    y = 0;
UNSUPPORTED("13guhblt4ee02wp7skslgweux"); // 	    x = -bnd;
UNSUPPORTED("2p7aqe0xpy2yshac8hs4h8boz"); // 	    for (; y > -bnd; y--)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("dhlxiqgb2o6a1bomoafek4b66"); // 	    for (; x < bnd; x++)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("eyxe9lc00uz4eqb1uii15fjc1"); // 	    for (; y < bnd; y++)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("9s5n7ukau4s2kpxp4szrf537q"); // 	    for (; x > -bnd; x--)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("ejcax560bkqv16vszo54w4m5d"); // 	    for (; y > 0; y--)
UNSUPPORTED("c80k1vihrjb8lsuc6moaq5bh3"); // 		if (fits(x, y, info, ps, place, step, bbs))
UNSUPPORTED("46hqh1l8dmimisi2nr03ntomn"); // 		    return;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 cm57bp8kzzj7nhvnbzhzniya7
// static packval_t* userVals




//3 dy401uvesqi5waa3p7s48m4ab
// static int ucmpf(const void *X, const void *Y) 
public static Object ucmpf(Object... arg) {
UNSUPPORTED("137928stzn8zfxl3jxu2a3ai5"); // static int ucmpf(const void *X, const void *Y)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ku8n7iezmely5ex218gfmgnn"); //     ainfo* x = *(ainfo **) X;
UNSUPPORTED("c5kqjorns3t5qoc40cc435dzl"); //     ainfo* y = *(ainfo **) Y;
UNSUPPORTED("5tomfbuz1yassu35dwz1iq7vm"); //     int dX = userVals[x->index];
UNSUPPORTED("bpyepdnz0i98c1givec8sfzi8"); //     int dY = userVals[y->index];
UNSUPPORTED("esc8v2x5h3onwsqp6c6xno2wx"); //     if (dX > dY) return 1;
UNSUPPORTED("7qerkoonjr1hepr83sy6pv0tv"); //     else if (dX < dY) return -1;
UNSUPPORTED("895k5gbyc2kla939upm8m5u76"); //     else return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6x4xkvohgtocih0ggo5pnszne
// static int acmpf(const void *X, const void *Y) 
public static Object acmpf(Object... arg) {
UNSUPPORTED("423xzl59p1su4ak69hnavsj3y"); // static int acmpf(const void *X, const void *Y)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ku8n7iezmely5ex218gfmgnn"); //     ainfo* x = *(ainfo **) X;
UNSUPPORTED("c5kqjorns3t5qoc40cc435dzl"); //     ainfo* y = *(ainfo **) Y;
UNSUPPORTED("8gzrdc9r316hn5bgbdj9iwld"); //     double dX = x->height + x->width; 
UNSUPPORTED("acl6jabv3oppesl52kwesjisc"); //     double dY = y->height + y->width; 
UNSUPPORTED("3ydx317qb6esfml32gmrn8d2"); //     if (dX < dY) return 1;
UNSUPPORTED("6dxscssmk09iu34elvy825a17"); //     else if (dX > dY) return -1;
UNSUPPORTED("895k5gbyc2kla939upm8m5u76"); //     else return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ri9de5rw5soigxidz94knl59
// static point * arrayRects (int ng, boxf* gs, pack_info* pinfo) 
public static Object arrayRects(Object... arg) {
UNSUPPORTED("7mjw19ag4ln47wi0vuem3rdo7"); // static point *
UNSUPPORTED("7i2605a8yy8rvl2sjqdyq0zkr"); // arrayRects (int ng, boxf* gs, pack_info* pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("587clam8w31px41vh3zl1x5zz"); //     int nr = 0, nc;
UNSUPPORTED("3uj2h26d8pqkuu2k0mjvkeol9"); //     int r, c;
UNSUPPORTED("exshbiefqo5bvtelq35q1uihg"); //     ainfo *info;
UNSUPPORTED("wyaib9x4tjhrx2m1ezfsaukn"); //     ainfo *ip;
UNSUPPORTED("2s7rlgsp3tf6744uvlbowf61l"); //     ainfo **sinfo;
UNSUPPORTED("3dy5gn4dyvwvrrpcjcltwo1or"); //     double* widths;
UNSUPPORTED("c0nhg35w7lcgf18f246eg5tsw"); //     double* heights;
UNSUPPORTED("4d9aph59v9jbee7xholivf5zp"); //     double v, wd, ht;
UNSUPPORTED("7843lpndabgsr9cdbfkcywvvx"); //     point* places = (point*)zmalloc((ng)*sizeof(point));
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("7l9fl3dxxtjbfy8hgqxikcdlp"); //     int sz, rowMajor;
UNSUPPORTED("83ixjypz1k5iieg2apicsmpgn"); //     /* set up no. of rows and columns */
UNSUPPORTED("c1codecudzu0sc5eeyf0k3shl"); //     sz = pinfo->sz;
UNSUPPORTED("368ad1d7qg13ji33rpsspgt8n"); //     if (pinfo->flags & (1 << 0)) {
UNSUPPORTED("9nsjbn4fps3cxesy71lw4otlx"); // 	rowMajor = 0;
UNSUPPORTED("6r33xzmrfhkea13cb2uq9to3w"); // 	if (sz > 0) {
UNSUPPORTED("t30y3kxgl5xg1b0x1pvhso1i"); // 	    nr = sz;
UNSUPPORTED("6ifv906e9hfiqnj8h4zuwcj4y"); // 	    nc = (ng + (nr-1))/nr;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("cio9a5vmyx6df3sfnalfboiuf"); // 	    nr = ceil(sqrt(ng));
UNSUPPORTED("6ifv906e9hfiqnj8h4zuwcj4y"); // 	    nc = (ng + (nr-1))/nr;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("ctpc0t0nilg3t9gj01k4ywsj3"); // 	rowMajor = 1;
UNSUPPORTED("6r33xzmrfhkea13cb2uq9to3w"); // 	if (sz > 0) {
UNSUPPORTED("6errm4a85tsehl607eqelczds"); // 	    nc = sz;
UNSUPPORTED("1x339pl43qiukqwjve0wxt28e"); // 	    nr = (ng + (nc-1))/nc;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("364szoyjdpowhykehht3akq3e"); // 	    nc = ceil(sqrt(ng));
UNSUPPORTED("1x339pl43qiukqwjve0wxt28e"); // 	    nr = (ng + (nc-1))/nc;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("3pdy7z56ruqq4oyacr883mikq"); // 	fprintf (stderr, "array packing: %s %d rows %d columns\n", (rowMajor?"row major":"column major"), nr, nc);
UNSUPPORTED("6043fy5yx6lbdsxu5yn3brmal"); //     widths = (double*)zmalloc((nc+1)*sizeof(double));
UNSUPPORTED("a1b2xc5h9ne6kbzuy0nkuwvyo"); //     heights = (double*)zmalloc((nr+1)*sizeof(double));
UNSUPPORTED("9aw3rarlnxmlwhap0h7473k7u"); //     ip = info = (ainfo*)zmalloc((ng)*sizeof(ainfo));
UNSUPPORTED("5rngkhxlli0ca2dadcc2l90yc"); //     for (i = 0; i < ng; i++, ip++) {
UNSUPPORTED("ay7zkx9vy6thctuz1zc6uwgp"); // 	bb = gs[i];
UNSUPPORTED("6u6ndmrhwricwao2uxni5h1k0"); // 	ip->width = bb.UR.x - bb.LL.x + pinfo->margin;
UNSUPPORTED("e97e7wvrktg5tvh7jgix3rh4n"); // 	ip->height = bb.UR.y - bb.LL.y + pinfo->margin;
UNSUPPORTED("c1vy7jp6gj45js1vpscqrdow5"); // 	ip->index = i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cj72vx5oyj2o2q2m7645spujf"); //     sinfo = (ainfo**)zmalloc((ng)*sizeof(ainfo*));
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("9mn44smuwqqa7634ytys2no53"); // 	sinfo[i] = info + i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("12fyz4qnl5zp3xwitz3v50nei"); //     if (pinfo->vals) {
UNSUPPORTED("60usk332i3h20n9in7ul42urp"); // 	userVals = pinfo->vals;
UNSUPPORTED("zri192zm0qymulo1qj64bkk4"); // 	qsort(sinfo, ng, sizeof(ainfo *), ucmpf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("elre2mkk0b66ystun7ksceacd"); //     else if (!(pinfo->flags & (1 << 6))) {
UNSUPPORTED("46wm7t71y606fg0p1pmnrotxs"); // 	qsort(sinfo, ng, sizeof(ainfo *), acmpf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bh1a6itzd1t536vncxeg55kws"); //     /* compute column widths and row heights */
UNSUPPORTED("a29uwx3mv4g7tkw0a7nld91n6"); //     r = c = 0;
UNSUPPORTED("5rngkhxlli0ca2dadcc2l90yc"); //     for (i = 0; i < ng; i++, ip++) {
UNSUPPORTED("9pextrq1h4wbpvek3nn5hpdj1"); // 	ip = sinfo[i];
UNSUPPORTED("2m8hk2qls872rs7vknlc6s0lx"); // 	widths[c] = MAX(widths[c],ip->width);
UNSUPPORTED("e6t8bv0nuykk21ia3zd431xfa"); // 	heights[r] = MAX(heights[r],ip->height);
UNSUPPORTED("2x8y0mx5s4y94qc2l938923p3"); // 	if (rowMajor){ c++; if (c == nc) { c = 0; r++; } } else { r++; if (r == nr) { r = 0; c++; } };
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("35agl3q9fx549xwrbpsl2d4us"); //     /* convert widths and heights to positions */
UNSUPPORTED("2xhvo47ul8yuzrfw9qt8dw0ps"); //     wd = 0;
UNSUPPORTED("b19j342j5puv71h95o4ipftcy"); //     for (i = 0; i <= nc; i++) {
UNSUPPORTED("cn91vt3tkx1q0hk0v0efsy0u5"); // 	v = widths[i];
UNSUPPORTED("9rsjnuoiq6k71cnjchxzdi4b2"); // 	widths[i] = wd;
UNSUPPORTED("8uj5r8kz22am852pbxn5xbj2o"); // 	wd += v;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("28dk2342xmi4riersjpcgji1t"); //     ht = 0;
UNSUPPORTED("v2352s1mp3wons7njjjynrxt"); //     for (i = nr; 0 < i; i--) {
UNSUPPORTED("6ua786jl1gs84859xekg6b8fg"); // 	v = heights[i-1];
UNSUPPORTED("bazhlkhnh5k7eheis0ez0n9vb"); // 	heights[i] = ht;
UNSUPPORTED("bvofkv7v5wfgk9cj4wzfaib3y"); // 	ht += v;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9rbcr484ikmoafnug3xnaxs32"); //     heights[0] = ht;
UNSUPPORTED("ddygazuu5ir5m2y5nmnsrajqx"); //     /* position rects */
UNSUPPORTED("a29uwx3mv4g7tkw0a7nld91n6"); //     r = c = 0;
UNSUPPORTED("5rngkhxlli0ca2dadcc2l90yc"); //     for (i = 0; i < ng; i++, ip++) {
UNSUPPORTED("drn8u5l7wlzkbovfo92png2jy"); // 	int idx;
UNSUPPORTED("9pextrq1h4wbpvek3nn5hpdj1"); // 	ip = sinfo[i];
UNSUPPORTED("2ps143essu8ajs0kj531wnief"); // 	idx = ip->index;
UNSUPPORTED("3k2y6x5t2zzgaukk772pdwx6f"); // 	bb = gs[idx];
UNSUPPORTED("81ldqtnpthu2abqyry4z6ym8m"); // 	if (pinfo->flags & (1 << 2))
UNSUPPORTED("13f3a690c3l0f4v06nrwmimi4"); // 	    places[idx].x = widths[c];
UNSUPPORTED("8eg2gw7wqr3mql161981r68br"); // 	else if (pinfo->flags & (1 << 3))
UNSUPPORTED("e3iwxanyszyzq23zx6vtonrj8"); // 	    places[idx].x = widths[c+1] - (bb.UR.x - bb.LL.x);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("67vni7zjcybymkjnpjnavoflk"); // 	    places[idx].x = (widths[c] + widths[c+1] - bb.UR.x - bb.LL.x)/2.0;
UNSUPPORTED("8dcmf759vr0nvbyid59jtzauy"); // 	if (pinfo->flags & (1 << 4))
UNSUPPORTED("24esxiop1grjauhaq6g2839fx"); // 	    places[idx].y = heights[r] - (bb.UR.y - bb.LL.y);
UNSUPPORTED("2dch1dl8cgtov5959f5m06bwn"); // 	else if (pinfo->flags & (1 << 5))
UNSUPPORTED("4t5x1tjwncmgjvqii9uimq4b1"); // 	    places[idx].y = heights[r+1];
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("djge91fby18vnmw35p7pnh54e"); // 	    places[idx].y = (heights[r] + heights[r+1] - bb.UR.y - bb.LL.y)/2.0;
UNSUPPORTED("2x8y0mx5s4y94qc2l938923p3"); // 	if (rowMajor){ c++; if (c == nc) { c = 0; r++; } } else { r++; if (r == nr) { r = 0; c++; } };
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2t9jewjat2jsi4mczos1h2k63"); //     free (info);
UNSUPPORTED("6m428qwh53clq0b13nr60dt5t"); //     free (sinfo);
UNSUPPORTED("srea4nhmmob1m0yxi5gapwxa"); //     free (widths);
UNSUPPORTED("aej4g6wixnif5navqnu0z5d02"); //     free (heights);
UNSUPPORTED("4pw9nikwh26us27xquq8gxeom"); //     return places;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2irl4ooaj6ar2weyzs1jt6vnj
// static point* polyRects(int ng, boxf* gs, pack_info * pinfo) 
public static Object polyRects(Object... arg) {
UNSUPPORTED("ck2vnuu91etfn4os7sqednjdt"); // static point*
UNSUPPORTED("emdivhrzg1z379tgeonw1e2wn"); // polyRects(int ng, boxf* gs, pack_info * pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("824stmaoi8w8lahubzh2aqmnt"); //     int stepSize;
UNSUPPORTED("egeh7d2rftmojiwelzoch9qzs"); //     ginfo *info;
UNSUPPORTED("cgpy9yq4nom59s6xdcks21z34"); //     ginfo **sinfo;
UNSUPPORTED("d5zj0uiljdydjp99y2c8421tm"); //     point *places;
UNSUPPORTED("vela488x5cw8iukv3b2rxxfg"); //     Dict_t *ps;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("757l2o5nnleyj2fwon83lkwp4"); //     point center;
UNSUPPORTED("4xciuabrdcqf4qlyxzw20rgoq"); //     /* calculate grid size */
UNSUPPORTED("2eg1bg6rzeyuwox4ji13hfsn3"); //     stepSize = computeStep(ng, gs, pinfo->margin);
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("9jvi53wte1pa00ysz1tyoirwu"); // 	fprintf(stderr, "step size = %d\n", stepSize);
UNSUPPORTED("4uchvv7hqly12y7in635yrzk2"); //     if (stepSize <= 0)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("ai9xhwyay6jsf18f9an9wiqxh"); //     /* generate polyomino cover for the rectangles */
UNSUPPORTED("cc7f8jeq787ivlq7z8w36zsxr"); //     center.x = center.y = 0;
UNSUPPORTED("5ja8kdpayis0xlce0mars9kyq"); //     info = (ginfo*)zmalloc((ng)*sizeof(ginfo));
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("dotsh6x7f3q84jxx9kanz0zxs"); // 	info[i].index = i;
UNSUPPORTED("50f4hs6qh3bifktl7ofmdluau"); // 	genBox(gs[i], info + i, stepSize, pinfo->margin, center, "");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7hsq9ilfch3hi4eeqdzbo8hcz"); //     /* sort */
UNSUPPORTED("516yap1pdty58f0uvcm0ol21t"); //     sinfo = (ginfo **)zmalloc((ng)*sizeof(ginfo *));
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("9mn44smuwqqa7634ytys2no53"); // 	sinfo[i] = info + i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("aht3on8lvvmmuavandpu3mrqf"); //     qsort(sinfo, ng, sizeof(ginfo *), cmpf);
UNSUPPORTED("6udv07xykmb0e2pdyqurnc9p4"); //     ps = newPS();
UNSUPPORTED("1lrlt4okvwo1r2jn12curx4a4"); //     places = (point*)zmalloc((ng)*sizeof(point));
UNSUPPORTED("8ykrswirvj1kokawlp0qt20e"); //     for (i = 0; i < ng; i++)
UNSUPPORTED("amjrgk4ewbgnlny99ltipn4rw"); // 	placeGraph(i, sinfo[i], ps, places + (sinfo[i]->index),
UNSUPPORTED("cdibq48a1vztjqwrav8lplm2l"); // 		       stepSize, pinfo->margin, gs);
UNSUPPORTED("cz66ku8toq29n6o82kekmnlpy"); //     free(sinfo);
UNSUPPORTED("8ykrswirvj1kokawlp0qt20e"); //     for (i = 0; i < ng; i++)
UNSUPPORTED("87pbwy86umrgdjafr4sezm9n"); // 	free(info[i].cells);
UNSUPPORTED("3evfbda0xfi17zcdebhf34f0u"); //     free(info);
UNSUPPORTED("dg5ijmmysdxc2qm5j58u0zr8i"); //     freePS(ps);
UNSUPPORTED("9ez4vr7qufhjoyjfdx4btsya0"); //     if (Verbose > 1)
UNSUPPORTED("8zex7y0yhzzbk09jno4q16t99"); // 	for (i = 0; i < ng; i++)
UNSUPPORTED("d35w6hrucxuabl0h4ezjaa3uc"); // 	    fprintf(stderr, "pos[%d] %d %d\n", i, places[i].x,
UNSUPPORTED("a7cjk49c6ofhzhsgib8hilvhs"); // 		    places[i].y);
UNSUPPORTED("4pw9nikwh26us27xquq8gxeom"); //     return places;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1knslw9muqsgklyyji7ievnok
// static point* polyGraphs(int ng, Agraph_t ** gs, Agraph_t * root, pack_info * pinfo) 
public static Object polyGraphs(Object... arg) {
UNSUPPORTED("ck2vnuu91etfn4os7sqednjdt"); // static point*
UNSUPPORTED("b0bfi4ppj9z49du0yf8jw51g5"); // polyGraphs(int ng, Agraph_t ** gs, Agraph_t * root, pack_info * pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("824stmaoi8w8lahubzh2aqmnt"); //     int stepSize;
UNSUPPORTED("egeh7d2rftmojiwelzoch9qzs"); //     ginfo *info;
UNSUPPORTED("cgpy9yq4nom59s6xdcks21z34"); //     ginfo **sinfo;
UNSUPPORTED("d5zj0uiljdydjp99y2c8421tm"); //     point *places;
UNSUPPORTED("vela488x5cw8iukv3b2rxxfg"); //     Dict_t *ps;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("g39u5n77k6uyq207nn2icvox"); //     boolean *fixed = pinfo->fixed;
UNSUPPORTED("7o8gvve73xqzq084bw5qh7gcn"); //     int fixed_cnt = 0;
UNSUPPORTED("w52lhq0cd0isq9ayfz50inrq"); //     box bb, fixed_bb = { {0, 0}, {0, 0} };
UNSUPPORTED("757l2o5nnleyj2fwon83lkwp4"); //     point center;
UNSUPPORTED("7gi50jrfs9ke5a9wali0er15n"); //     boxf* bbs;
UNSUPPORTED("3mtqlb2sgomhp5qec8wdsugsi"); //     if (ng <= 0)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("djthf1vazs4za0so4hdnt5wj0"); //     /* update bounding box info for each graph */
UNSUPPORTED("5jm29bcxipf9ixrkoge4l6qm2"); //     /* If fixed, compute bbox of fixed graphs */
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("8u7vr203farotwj0fftrag209"); // 	Agraph_t *g = gs[i];
UNSUPPORTED("1pr9y3xbbgtpw0j2cdlmr5z50"); // 	compute_bb(g);
UNSUPPORTED("24swiauow6ik517gmj0sm5rfw"); // 	if (fixed && fixed[i]) {
UNSUPPORTED("97wxoa3jn3w53rreu435mpy74"); // 	    BF2B(GD_bb(g), bb);
UNSUPPORTED("5lpwic2iylf55w56n041ro1x"); // 	    if (fixed_cnt) {
UNSUPPORTED("9frrmjyww4wh93dyu5k98peb9"); // 		fixed_bb.LL.x = MIN(bb.LL.x, fixed_bb.LL.x);
UNSUPPORTED("e85tcd131giv33jrjja1olduw"); // 		fixed_bb.LL.y = MIN(bb.LL.y, fixed_bb.LL.y);
UNSUPPORTED("dj6e5qtw7vxwfyomq1k58hcsk"); // 		fixed_bb.UR.x = MAX(bb.UR.x, fixed_bb.UR.x);
UNSUPPORTED("b4wurhjcgswx703j06qz21n6l"); // 		fixed_bb.UR.y = MAX(bb.UR.y, fixed_bb.UR.y);
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("2e612eixx1ofzkwvzqn2kwd76"); // 		fixed_bb = bb;
UNSUPPORTED("ny0y2ibeyhhufmet5xl3j048"); // 	    fixed_cnt++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("29xfwsb3c8xg86f1k0dnyeo0w"); // 	if (Verbose > 2) {
UNSUPPORTED("cdzr4ncs6h15d0ih7ert3l5z3"); // 	    fprintf(stderr, "bb[%s] %.5g %.5g %.5g %.5g\n", agnameof(g),
UNSUPPORTED("759r58p1rwwxhyxaj7ofczgfy"); // 		GD_bb(g).LL.x, GD_bb(g).LL.y,
UNSUPPORTED("btv2fogged3wlas7cotr1a3my"); // 		GD_bb(g).UR.x, GD_bb(g).UR.y);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4xciuabrdcqf4qlyxzw20rgoq"); //     /* calculate grid size */
UNSUPPORTED("1zfqthv6gt9dvwrk425hk65xk"); //     bbs = (boxf*)gmalloc((ng)*sizeof(boxf));
UNSUPPORTED("8ykrswirvj1kokawlp0qt20e"); //     for (i = 0; i < ng; i++)
UNSUPPORTED("2bt80lu17wcugimh4ljj70zdp"); // 	bbs[i] = GD_bb(gs[i]);
UNSUPPORTED("5owrhafi21tpc0ktinz3o2f43"); //     stepSize = computeStep(ng, bbs, pinfo->margin);
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("9jvi53wte1pa00ysz1tyoirwu"); // 	fprintf(stderr, "step size = %d\n", stepSize);
UNSUPPORTED("4uchvv7hqly12y7in635yrzk2"); //     if (stepSize <= 0)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("1alk6avw0u6hfvqo3xmul0jg6"); //     /* generate polyomino cover for the graphs */
UNSUPPORTED("84yhbc8186y40vqfeqxzxfrz6"); //     if (fixed) {
UNSUPPORTED("5ueew992kcblg2bz1oerhmyqi"); // 	center.x = (fixed_bb.LL.x + fixed_bb.UR.x) / 2;
UNSUPPORTED("87nbiqt8xjmmkxfd2e4w4w7vr"); // 	center.y = (fixed_bb.LL.y + fixed_bb.UR.y) / 2;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("e83bosc1hfovot0zgf1yquw6p"); // 	center.x = center.y = 0;
UNSUPPORTED("5ja8kdpayis0xlce0mars9kyq"); //     info = (ginfo*)zmalloc((ng)*sizeof(ginfo));
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("8u7vr203farotwj0fftrag209"); // 	Agraph_t *g = gs[i];
UNSUPPORTED("dotsh6x7f3q84jxx9kanz0zxs"); // 	info[i].index = i;
UNSUPPORTED("khn1not6v8q3llxivwzrif1a"); // 	if (pinfo->mode == l_graph)
UNSUPPORTED("ef0j485ay1qbdf8azewxc0pwi"); // 	    genBox(GD_bb(g), info + i, stepSize, pinfo->margin, center, agnameof(g));
UNSUPPORTED("bwshrbmzbgqbses1jh55297bx"); // 	else if (genPoly(root, gs[i], info + i, stepSize, pinfo, center)) {
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7hsq9ilfch3hi4eeqdzbo8hcz"); //     /* sort */
UNSUPPORTED("516yap1pdty58f0uvcm0ol21t"); //     sinfo = (ginfo **)zmalloc((ng)*sizeof(ginfo *));
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("9mn44smuwqqa7634ytys2no53"); // 	sinfo[i] = info + i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("aht3on8lvvmmuavandpu3mrqf"); //     qsort(sinfo, ng, sizeof(ginfo *), cmpf);
UNSUPPORTED("6udv07xykmb0e2pdyqurnc9p4"); //     ps = newPS();
UNSUPPORTED("1lrlt4okvwo1r2jn12curx4a4"); //     places = (point*)zmalloc((ng)*sizeof(point));
UNSUPPORTED("84yhbc8186y40vqfeqxzxfrz6"); //     if (fixed) {
UNSUPPORTED("3e0deh4u5tol3atbp956xif23"); // 	for (i = 0; i < ng; i++) {
UNSUPPORTED("umfi6ok5pdkv9azybxeoslof"); // 	    if (fixed[i])
UNSUPPORTED("8wt6vr2q1holh0icpplq4zqec"); // 		placeFixed(sinfo[i], ps, places + (sinfo[i]->index),
UNSUPPORTED("deqjfvghqxur4bfoz3puk300n"); // 			   center);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3e0deh4u5tol3atbp956xif23"); // 	for (i = 0; i < ng; i++) {
UNSUPPORTED("74god7lqd61d25tmue88dqtbf"); // 	    if (!fixed[i])
UNSUPPORTED("czkvahaom618elf7saxx38r8p"); // 		placeGraph(i, sinfo[i], ps, places + (sinfo[i]->index),
UNSUPPORTED("i3cmxkksuk6am6ht9wvhbdgc"); // 			   stepSize, pinfo->margin, bbs);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("8zex7y0yhzzbk09jno4q16t99"); // 	for (i = 0; i < ng; i++)
UNSUPPORTED("a532n3o5yemks8slunbp5py2i"); // 	    placeGraph(i, sinfo[i], ps, places + (sinfo[i]->index),
UNSUPPORTED("dt17bemprdm748nimg2zjv0m0"); // 		       stepSize, pinfo->margin, bbs);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cz66ku8toq29n6o82kekmnlpy"); //     free(sinfo);
UNSUPPORTED("8ykrswirvj1kokawlp0qt20e"); //     for (i = 0; i < ng; i++)
UNSUPPORTED("87pbwy86umrgdjafr4sezm9n"); // 	free(info[i].cells);
UNSUPPORTED("3evfbda0xfi17zcdebhf34f0u"); //     free(info);
UNSUPPORTED("dg5ijmmysdxc2qm5j58u0zr8i"); //     freePS(ps);
UNSUPPORTED("a3ilp870e8stntakfa9mhpq3s"); //     free (bbs);
UNSUPPORTED("9ez4vr7qufhjoyjfdx4btsya0"); //     if (Verbose > 1)
UNSUPPORTED("8zex7y0yhzzbk09jno4q16t99"); // 	for (i = 0; i < ng; i++)
UNSUPPORTED("d35w6hrucxuabl0h4ezjaa3uc"); // 	    fprintf(stderr, "pos[%d] %d %d\n", i, places[i].x,
UNSUPPORTED("a7cjk49c6ofhzhsgib8hilvhs"); // 		    places[i].y);
UNSUPPORTED("4pw9nikwh26us27xquq8gxeom"); //     return places;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 al89yl8dh1jxwk08tyczwknn7
// point *putGraphs(int ng, Agraph_t ** gs, Agraph_t * root, 		 pack_info * pinfo) 
public static Object putGraphs(Object... arg) {
UNSUPPORTED("2bmiezotvi7u8eu53600a4id7"); // point *putGraphs(int ng, Agraph_t ** gs, Agraph_t * root,
UNSUPPORTED("dnotj3yx4zh4izfbxl101921l"); // 		 pack_info * pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b9q08fv1def3y3mbkfcttoygi"); //     int i, v;
UNSUPPORTED("7gi50jrfs9ke5a9wali0er15n"); //     boxf* bbs;
UNSUPPORTED("c9h1fo1uvas6uw1a8qaapnln6"); //     Agraph_t* g;
UNSUPPORTED("37y05h5xfo0t6oujuechff9od"); //     point* pts;
UNSUPPORTED("yiuh599p05f2mpu2e3pesu2o"); //     char* s;
UNSUPPORTED("10gh49wkm3r9433ve3ykoy8zd"); //     if (ng <= 0) return NULL;
UNSUPPORTED("74z1gk7owd6s8rog374fsoz77"); //     if (pinfo->mode <= l_graph)
UNSUPPORTED("3mqc3im4s572a4m1gq3luvraa"); // 	return polyGraphs (ng, gs, root, pinfo);
UNSUPPORTED("1zfqthv6gt9dvwrk425hk65xk"); //     bbs = (boxf*)gmalloc((ng)*sizeof(boxf));
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("1o35elvmsbg14ylv3li4k555e"); // 	g = gs[i];
UNSUPPORTED("1pr9y3xbbgtpw0j2cdlmr5z50"); // 	compute_bb(g);
UNSUPPORTED("38ehh0n0z7yr3ku3wax91eny9"); // 	bbs[i] = GD_bb(g);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8kmg7cdfeuqszcwxcz5y3c5td"); //     if (pinfo->mode == l_array) {
UNSUPPORTED("7nb1jaoblp1yvgvo27aaud6s6"); // 	if (pinfo->flags & (1 << 1)) {
UNSUPPORTED("dbollhgp3oflycwtdbdbh9i88"); // 	    pinfo->vals = (packval_t*)zmalloc((ng)*sizeof(packval_t));
UNSUPPORTED("58nh8251ikin1r1ikujawgmvi"); // 	    for (i = 0; i < ng; i++) {
UNSUPPORTED("f0c6o7jozps86mqlhvb9n9d6s"); // 		s = agget (gs[i], "sortv");
UNSUPPORTED("dor0p055w386zaz88qm97z87v"); // 		if (s && (sscanf (s, "%d", &v) > 0) && (v >= 0))
UNSUPPORTED("14rvncot97v829zy8a8tp0zwz"); // 		    pinfo->vals[i] = v;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1ptopw7nt3b3yr62pvaymgk9m"); // 	pts = arrayRects (ng, bbs, pinfo);
UNSUPPORTED("b93dvbn6t4y3hpjm8h8t94bxt"); // 	if (pinfo->flags & (1 << 1))
UNSUPPORTED("2y3e1eszpxr131wfwuq7wc18i"); // 	    free (pinfo->vals);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a3ilp870e8stntakfa9mhpq3s"); //     free (bbs);
UNSUPPORTED("b7gk8q1reftzri269holggnig"); //     return pts;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e73o8yamtvnffshrs9rm3lim0
// point * putRects(int ng, boxf* bbs, pack_info* pinfo) 
public static Object putRects(Object... arg) {
UNSUPPORTED("b2t3wt7lnrvb61t0hory0gh2h"); // point *
UNSUPPORTED("8fweyba26afb4b3vn251yp6u1"); // putRects(int ng, boxf* bbs, pack_info* pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("10gh49wkm3r9433ve3ykoy8zd"); //     if (ng <= 0) return NULL;
UNSUPPORTED("f2v1akrh6kotuwhh1shnn0cvk"); //     if ((pinfo->mode == l_node) || (pinfo->mode == l_clust)) return NULL;
UNSUPPORTED("bvxb92ntr7lr5208ycn2rmmpa"); //     if (pinfo->mode == l_graph)
UNSUPPORTED("790j7xshzs2a26qdpizm1dor3"); // 	return polyRects (ng, bbs, pinfo);
UNSUPPORTED("15zpqbwzhvrw6k4i293i6qm4o"); //     else if (pinfo->mode == l_array)
UNSUPPORTED("defqlfr5ltykxr36gzynktwnd"); // 	return arrayRects (ng, bbs, pinfo);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("11hwqop4xebvtcskop4uhpp01"); // 	return NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1snngl11fvf8fd7gvroyic9u6
// int  packRects(int ng, boxf* bbs, pack_info* pinfo) 
public static Object packRects(Object... arg) {
UNSUPPORTED("7zkpme13g8rxxwloxvpvvnbcw"); // int 
UNSUPPORTED("byuhbycc8aq8qmlje1kkikzes"); // packRects(int ng, boxf* bbs, pack_info* pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("ca6brvq3h4u316zi41fa1e7nl"); //     point *pp;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("41nrdsugvfgw23s93g0dhizkn"); //     point p;
UNSUPPORTED("4rnqckqjseeaveduaeddk5kly"); //     if (ng < 0) return -1;
UNSUPPORTED("62vqifnah2fjfuxwj69bq3p1z"); //     if (ng <= 1) return 0;
UNSUPPORTED("e86z2a3ujrvoqxb1e6rn7l4xh"); //     pp = putRects(ng, bbs, pinfo);
UNSUPPORTED("5e2m9zqmerz2m76uyivjvvjnz"); //     if (!pp)
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("1hemhly8ts9l7caj22as9b5nv"); //     for (i = 0; i < ng; i++) { 
UNSUPPORTED("cpgjovg5bttmcqygaaobc3a6l"); // 	bb = bbs[i];
UNSUPPORTED("4v8yuwm5xgienbqxhd69pz5cc"); // 	p = pp[i];
UNSUPPORTED("f3utitevgnfhjuotaq73baii9"); // 	bb.LL.x += p.x;
UNSUPPORTED("31znkfxiz3wa3ebxopvejt2eu"); // 	bb.UR.x += p.x;
UNSUPPORTED("c3ak9fudqw0ye4ls2y61ajfta"); // 	bb.LL.y += p.y;
UNSUPPORTED("djl8lmebqjuxhormrrnxryg2d"); // 	bb.UR.y += p.y;
UNSUPPORTED("ds6wyt4f0tketopteichqayg0"); // 	bbs[i] = bb;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7vph3omiu71ktck9w3uvcdp6q"); //     free(pp);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eyiqyefdjlfh0t51u26x2zh5x
// static void shiftEdge(Agedge_t * e, int dx, int dy) 
public static Object shiftEdge(Object... arg) {
UNSUPPORTED("9u5vr04c98yfoctnamlbuc2ag"); // static void shiftEdge(Agedge_t * e, int dx, int dy)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9e6bnowy6jfhnib5uev3scpsu"); //     int j, k;
UNSUPPORTED("37thdceezsvepe7tlyfatrbcw"); //     bezier bz;
UNSUPPORTED("88hfgnjbnnlyh1vmms1kwsrod"); //     if (ED_label(e))
UNSUPPORTED("4ktdw0hk8c5lnprxiqxxp8kpf"); // 	((ED_label(e)->pos).x += dx, (ED_label(e)->pos).y += dy);
UNSUPPORTED("37decwf4jqk863lp048x1gcwk"); //     if (ED_xlabel(e))
UNSUPPORTED("8xzkzyz7cb3te6mkfkt3zlvvv"); // 	((ED_xlabel(e)->pos).x += dx, (ED_xlabel(e)->pos).y += dy);
UNSUPPORTED("dc4h855lfazgpazdd3bsd3dle"); //     if (ED_head_label(e))
UNSUPPORTED("ekvchodhl5temnce8je23xtng"); // 	((ED_head_label(e)->pos).x += dx, (ED_head_label(e)->pos).y += dy);
UNSUPPORTED("2nan67aki9dkzb14alqq58pk6"); //     if (ED_tail_label(e))
UNSUPPORTED("4humrtjlk95veottt9p8slpt3"); // 	((ED_tail_label(e)->pos).x += dx, (ED_tail_label(e)->pos).y += dy);
UNSUPPORTED("tt8piuiel7igl12irv6488jd"); //     if (ED_spl(e) == NULL)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("e9vow5lzqvgcxabgjj99yokjr"); //     for (j = 0; j < ED_spl(e)->size; j++) {
UNSUPPORTED("8ni4zfvf78h93ucjuoqtuomjh"); // 	bz = ED_spl(e)->list[j];
UNSUPPORTED("7h0wreqeq69tmfuymjgf6y30p"); // 	for (k = 0; k < bz.size; k++)
UNSUPPORTED("8p04wyfp1epw24aj3ooutcm9f"); // 	    ((bz.list[k]).x += dx, (bz.list[k]).y += dy);
UNSUPPORTED("9u19ic8wst5bdydvzocx725dd"); // 	if (bz.sflag)
UNSUPPORTED("7drx2tftwhcon6o1unse2w8rs"); // 	    ((ED_spl(e)->list[j].sp).x += dx, (ED_spl(e)->list[j].sp).y += dy);
UNSUPPORTED("3ps9lzlxh4tqc8zn14yrvcl4n"); // 	if (bz.eflag)
UNSUPPORTED("did8o6w576i3j4f1c8nqnwjpl"); // 	    ((ED_spl(e)->list[j].ep).x += dx, (ED_spl(e)->list[j].ep).y += dy);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7xx1rik6wio4uaxsnuw7osy3u
// static void shiftGraph(Agraph_t * g, int dx, int dy) 
public static Object shiftGraph(Object... arg) {
UNSUPPORTED("3rx9mi6118cqiy2pcuxsl4mgc"); // static void shiftGraph(Agraph_t * g, int dx, int dy)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8uujemixuhlf040icq3zsh7j8"); //     graph_t *subg;
UNSUPPORTED("2eiur8hkm8tcazpq12w4ikbqo"); //     boxf bb = GD_bb(g);
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("3pb1rp6yt77fdctds9bv6t2q4"); //     bb = GD_bb(g);
UNSUPPORTED("8apkqrqdqqydv62uo6t2kih65"); //     bb.LL.x += dx;
UNSUPPORTED("cxx7s1j7oanjj7qcgww51qzv"); //     bb.UR.x += dx;
UNSUPPORTED("8i5b905t9fyewlfs3ptj5ts9i"); //     bb.LL.y += dy;
UNSUPPORTED("bat733mf4if1fc1cc75wf3vdh"); //     bb.UR.y += dy;
UNSUPPORTED("8pjanfm12ixxbeb7k86g3z5p4"); //     GD_bb(g) = bb;
UNSUPPORTED("aplr7sm051i57jygcfj6gigoh"); //     if (GD_label(g))
UNSUPPORTED("3y0fbtz3yao1jomzv4uolrp00"); // 	((GD_label(g)->pos).x += dx, (GD_label(g)->pos).y += dy);
UNSUPPORTED("7naa6f8pevjidfr7m41eli6xj"); //     for (i = 1; i <= GD_n_cluster(g); i++) {
UNSUPPORTED("cpbcovu6u9jpbhniuwko58sge"); // 	subg = GD_clust(g)[i];
UNSUPPORTED("2gsxiux9jv0jpf915foz63dgv"); // 	shiftGraph(subg, dx, dy);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e3319d41y02srtfpto31q37ty
// int shiftGraphs(int ng, Agraph_t ** gs, point * pp, Agraph_t * root, 	    int doSplines) 
public static Object shiftGraphs(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("e3b6ggtg7s8nlv7sbaoirghbw"); // shiftGraphs(int ng, Agraph_t ** gs, point * pp, Agraph_t * root,
UNSUPPORTED("9noyex54k0macq3k16r5ym7m7"); // 	    int doSplines)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("bftz89757hgmx7ivuna8lc4z6"); //     int dx, dy;
UNSUPPORTED("52jqyu9tdwfx6u9l7492mdvxw"); //     double fx, fy;
UNSUPPORTED("41nrdsugvfgw23s93g0dhizkn"); //     point p;
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("4fddvn6emf9gppkxys6y69el2"); //     Agraph_t *eg;
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("36vshotvjkc5iodgg7nq6qa2r"); //     Agedge_t *e;
UNSUPPORTED("3mtqlb2sgomhp5qec8wdsugsi"); //     if (ng <= 0)
UNSUPPORTED("bof722m66fo2c6qrziuzp1qrb"); // 	return abs(ng);
UNSUPPORTED("7zun8aoc3cgzj5s89wluw8xq4"); //     for (i = 0; i < ng; i++) {
UNSUPPORTED("1o35elvmsbg14ylv3li4k555e"); // 	g = gs[i];
UNSUPPORTED("651v5s5braiusljmonry4n2gv"); // 	if (root)
UNSUPPORTED("64a6av0hcmo7owy9zqh1m0rf2"); // 	    eg = root;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("9w2sfnbzooomlt0cy7lv4r651"); // 	    eg = g;
UNSUPPORTED("4v8yuwm5xgienbqxhd69pz5cc"); // 	p = pp[i];
UNSUPPORTED("ap6rzh2gz0cyv006wzje9vm64"); // 	dx = p.x;
UNSUPPORTED("ecigmad6hz1aki283absmrv4k"); // 	dy = p.y;
UNSUPPORTED("crjm7321nijj633qm2os1rffv"); // 	fx = ((dx)/(double)72);
UNSUPPORTED("9fof84axpoehtz8vm7e591gug"); // 	fy = ((dy)/(double)72);
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("emng3ci5q7cb4o56sokohkzxg"); // 	    ND_pos(n)[0] += fx;
UNSUPPORTED("7hccr00zs6a1djjd96lnn6aka"); // 	    ND_pos(n)[1] += fy;
UNSUPPORTED("9p2dkqvsj8qbvhoe67ls2u17p"); // 	    ((ND_coord(n)).x += dx, (ND_coord(n)).y += dy);
UNSUPPORTED("9i39rsg8gc22jjd87bzbvdziv"); // 	    if (ND_xlabel(n)) {
UNSUPPORTED("9hx48w0r43gelhjavye29okfv"); // 		((ND_xlabel(n)->pos).x += dx, (ND_xlabel(n)->pos).y += dy);
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("3dcleparqyzm2ewv674fws8u9"); // 	    if (doSplines) {
UNSUPPORTED("8menlqwqcb9c7ireb6sa8qo21"); // 		for (e = agfstout(eg, n); e; e = agnxtout(eg, e))
UNSUPPORTED("l0qmhpy5cagstbj5hrkzk8rr"); // 		    shiftEdge(e, dx, dy);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bk9jkveibpb3mrdqpv4po37sy"); // 	shiftGraph(g, dx, dy);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9db5t8nm89djo9ln21gm3yf8u
// int packGraphs(int ng, Agraph_t ** gs, Agraph_t * root, pack_info * info) 
public static Object packGraphs(Object... arg) {
UNSUPPORTED("56vi2bsj4me6of6ownlhvsvwz"); // int packGraphs(int ng, Agraph_t ** gs, Agraph_t * root, pack_info * info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7p7i4cv4qt10ow22vl9znw72j"); //     int ret;
UNSUPPORTED("56ocwkz3s6lgpumk8xidlqcc7"); //     point *pp = putGraphs(ng, gs, root, info);
UNSUPPORTED("5e2m9zqmerz2m76uyivjvvjnz"); //     if (!pp)
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("7qo1holf96smktjgs8l0gibpj"); //     ret = shiftGraphs(ng, gs, pp, root, info->doSplines);
UNSUPPORTED("7vph3omiu71ktck9w3uvcdp6q"); //     free(pp);
UNSUPPORTED("f3b7mj138albdr4lodyomke0z"); //     return ret;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eeedpohq9tu61htldhskjnrqo
// int packSubgraphs(int ng, Agraph_t ** gs, Agraph_t * root, pack_info * info) 
public static Object packSubgraphs(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("cu03rkfa1tn2levktk9f0kn72"); // packSubgraphs(int ng, Agraph_t ** gs, Agraph_t * root, pack_info * info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7p7i4cv4qt10ow22vl9znw72j"); //     int ret;
UNSUPPORTED("60qkq3hoxtw8jjw7osm6gbvh9"); //     ret = packGraphs(ng, gs, root, info);
UNSUPPORTED("4uiunytnyi2vmlytj49547oen"); //     if (ret == 0) {
UNSUPPORTED("cb6qes2nvjr9djerw1rcq8w3j"); // 	int i, j;
UNSUPPORTED("58vfsgm37n0q8bcfczvgvut8m"); // 	boxf bb;
UNSUPPORTED("ak2n425v85g82xo9282myas7y"); // 	graph_t* g;
UNSUPPORTED("6rvedme286x3a6z4sema6be33"); // 	compute_bb(root);
UNSUPPORTED("b6i8aku7mgq1oktewocn2b8au"); // 	bb = GD_bb(root);
UNSUPPORTED("3e0deh4u5tol3atbp956xif23"); // 	for (i = 0; i < ng; i++) {
UNSUPPORTED("8awv02csdisyrr0puo3g7x7zo"); // 	    g = gs[i];
UNSUPPORTED("3pxtqjoekga8rbxndmr9eyk8t"); // 	    for (j = 1; j <= GD_n_cluster(g); j++) {
UNSUPPORTED("15cmvttn1yf4brhtxlki7jqff"); // 		EXPANDBB(bb,GD_bb(GD_clust(g)[j]));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e9bwlicnysgjem9pm2owophm3"); // 	GD_bb(root) = bb;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f3b7mj138albdr4lodyomke0z"); //     return ret;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8rfd6spe19v1327cufpqvnqec
// int  pack_graph(int ng, Agraph_t** gs, Agraph_t* root, boolean* fixed) 
public static Object pack_graph(Object... arg) {
UNSUPPORTED("7zkpme13g8rxxwloxvpvvnbcw"); // int 
UNSUPPORTED("dgtmyy97t6j1t4kpcjat3nnl1"); // pack_graph(int ng, Agraph_t** gs, Agraph_t* root, boolean* fixed)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7p7i4cv4qt10ow22vl9znw72j"); //     int ret;
UNSUPPORTED("1uqyueaw3113nhl88qcfu7bzu"); //     pack_info info;
UNSUPPORTED("9pq8hgklb5oc6tdvk45bgja03"); //     getPackInfo(root, l_graph, 8, &info);
UNSUPPORTED("32h39ruk7pnxsjjwa8ptjg8re"); //     info.doSplines = 1;
UNSUPPORTED("1my115b5f8fzfk0oafnabmxfj"); //     info.fixed = fixed;
UNSUPPORTED("2bhwyiicw1zcnic4vjothzze3"); //     ret = packSubgraphs(ng, gs, root, &info);
UNSUPPORTED("5nd36gtfbsaee4fp6jvef26g8"); //     if (ret == 0) dotneato_postprocess (root);
UNSUPPORTED("f3b7mj138albdr4lodyomke0z"); //     return ret;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5u1pdxxmfiu2hry342a4x7kh6
// static char* chkFlags (char* p, pack_info* pinfo) 
public static Object chkFlags(Object... arg) {
UNSUPPORTED("1yranxmu2maol02ulzd1ka1re"); // static char*
UNSUPPORTED("8hwjd3nd6s2uo8zndhppm3kvo"); // chkFlags (char* p, pack_info* pinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b95pirj2cs9mytcz8lfgdzg8w"); //     int c, more;
UNSUPPORTED("2uon126vetx4frre9rfj5kwo3"); //     if (*p != '_') return p;
UNSUPPORTED("cdsfbjd0c8345i6xay73clz7w"); //     p++;
UNSUPPORTED("6ecxobdci26ildbdmnvdlxcaq"); //     more = 1;
UNSUPPORTED("69yg68y6v9aac3lfm1ph27y4p"); //     while (more && (c = *p)) {
UNSUPPORTED("7rk995hpmaqbbasmi40mqg0yw"); // 	switch (c) {
UNSUPPORTED("7uv0u7m8t8vyjwy3v82ow51w3"); // 	case 'c' :
UNSUPPORTED("zpuzt82wipz2nkqsosee5ak2"); // 	    pinfo->flags |= (1 << 0);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dcwafq80cu9cfgjlh6mde3jl5"); // 	case 'i' :
UNSUPPORTED("93wkkv5r9w97t1opi2g578qdx"); // 	    pinfo->flags |= (1 << 6);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8ef3noocjg099b2wm2at9fcfm"); // 	case 'u' :
UNSUPPORTED("5fat3lcknhnoy1piblquof76n"); // 	    pinfo->flags |= (1 << 1);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4gib6kzlsyq6li9hy7twvu1z2"); // 	case 't' :
UNSUPPORTED("3fkvofx77siau3tckaayyty9i"); // 	    pinfo->flags |= (1 << 4);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f2k4zkd8o9triyu1ym1kmlyq3"); // 	case 'b' :
UNSUPPORTED("bexhnqh3hnmo2exnreo65qrn9"); // 	    pinfo->flags |= (1 << 5);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3gnomrm4ne7tv5zoleucfclel"); // 	case 'l' :
UNSUPPORTED("50e0nwuj7xqbdvos1msb37ily"); // 	    pinfo->flags |= (1 << 2);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("aba8jswwne2ykziks3sfo201b"); // 	case 'r' :
UNSUPPORTED("izuljhdmym8vluo87hpk7kq8"); // 	    pinfo->flags |= (1 << 3);
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5ujjs4gho9mjjupbibyqyplxp"); // 	default :
UNSUPPORTED("ak3tvcq0zczj4iaz1osxh5ruq"); // 	    more = 0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 f2iaacl82y04a7oguahy9oo6
// static char* mode2Str (pack_mode m) 
public static Object mode2Str(Object... arg) {
UNSUPPORTED("1yranxmu2maol02ulzd1ka1re"); // static char*
UNSUPPORTED("cbahd10pirq1ryicw6cbj8454"); // mode2Str (pack_mode m)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("c4zsjnmgv48k6r8sbrvwztb6n"); //     switch (m) {
UNSUPPORTED("57a9puw02tf6kffzzki2yiejt"); //     case l_clust:
UNSUPPORTED("eriuzmc5hmkj22dwkc1jyxn0f"); // 	s = "cluster";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9dmynvx8joldqskjnpn5m77ze"); //     case l_node:
UNSUPPORTED("dm3a6gn1co2hl85txdr7kc7ux"); // 	s = "node";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dpfo62efj4ibm7vn73t9zwpcv"); //     case l_graph:
UNSUPPORTED("1leprh5ff6gk2iz2teqzgpmz9"); // 	s = "graph";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9qrcli4a5yhl31kzvzyvb5iho"); //     case l_array:
UNSUPPORTED("a8mkbpe7uwb8dmzrlu3hutdsm"); // 	s = "array";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8xvt26fzpofcs0qvk5cpwuynx"); //     case l_aspect:
UNSUPPORTED("15jl19ngssxkg73ykfeez8dpd"); // 	s = "aspect";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1jswhiu2nci8p4mklivv8912o"); //     case l_undef: 
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("aufikv57oho66mjvuxi8sjjes"); // 	s = "undefined";
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dyb1n3lhbi0wnr9kvmu6onux9
// pack_mode  parsePackModeInfo(char* p, pack_mode dflt, pack_info* pinfo) 
public static int parsePackModeInfo(CString p, int dflt, ST_pack_info pinfo) {
ENTERING("dyb1n3lhbi0wnr9kvmu6onux9","parsePackModeInfo");
try {
    float v;
    int i;
    //assert (pinfo);
    pinfo.setInt("flags", 0);
    pinfo.setInt("mode", dflt);
    pinfo.setInt("sz", 0);
    pinfo.setPtr("vals", null);
    if (p!=null && p.charAt(0)!='\0') {
UNSUPPORTED("2ybrtqq8efxouph8wjbrnowxz"); // 	switch (*p) {
UNSUPPORTED("2v5u3irq50r1n2ccuna0y09lk"); // 	case 'a':
UNSUPPORTED("4o68zbwd6291pxryyntjh2432"); // 	    if ((!strncmp(p,"array",(sizeof("array")/sizeof(char) - 1)))) {
UNSUPPORTED("3cxht114gc0fn4wl8xjy515bv"); // 		pinfo->mode = l_array;
UNSUPPORTED("edkaa80od81ojkn7d4h0q1zbu"); // 		p += (sizeof("array")/sizeof(char) - 1);
UNSUPPORTED("106zhcf9a6frvafctbbgrbws3"); // 		p = chkFlags (p, pinfo);
UNSUPPORTED("epac8gmlq3r1q3g6lh3fb9nzh"); // 		if ((sscanf (p, "%d", &i)>0) && (i > 0))
UNSUPPORTED("2e563kaxakuuou2t5kqvv33w5"); // 		    pinfo->sz = i;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("38dve3snq2grkwui6y1tpc6r1"); // 	    else if ((!strncmp(p,"aspect",(sizeof("aspect")/sizeof(char) - 1)))) {
UNSUPPORTED("ems4wqj0nbd0vi3sttf36tnir"); // 		pinfo->mode = l_aspect;
UNSUPPORTED("3ckdfsy55ba8os2xhymh002al"); // 		if ((sscanf (p + (sizeof("array")/sizeof(char) - 1), "%f", &v)>0) && (v > 0))
UNSUPPORTED("1i6xlg2sfqvjqjdu811xm5398"); // 		    pinfo->aspect = v;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("b3tzhasiwa1p83l47nhl6k5x8"); // 		    pinfo->aspect = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f3lyz2cejs6yn5fyckhn7ba1"); // 	case 'c':
UNSUPPORTED("d6yjom2gtqleeq1vf5l72eazl"); // 	    if ((*(p)==*("cluster")&&!strcmp(p,"cluster")))
UNSUPPORTED("2xe2y3s32q27l74dshw3831hu"); // 		pinfo->mode = l_clust;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("e00kor2k58i044hvvxytt9dhg"); // 	case 'g':
UNSUPPORTED("8vzhimdinzz48u15tcx34gzgs"); // 	    if ((*(p)==*("graph")&&!strcmp(p,"graph")))
UNSUPPORTED("coafx7pn3ah1ulp0o7a0z119r"); // 		pinfo->mode = l_graph;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f187wptsr73liavtlyoyfovp3"); // 	case 'n':
UNSUPPORTED("aqi1ed010xc3cj8xoye8vqkqe"); // 	    if ((*(p)==*("node")&&!strcmp(p,"node")))
UNSUPPORTED("epewq8yin5pr1meeofxvntjes"); // 		pinfo->mode = l_node;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    //if (Verbose) {
	//fprintf (stderr, "pack info:\n");
	//fprintf (stderr, "  mode   %s\n", mode2Str(pinfo->mode));
	//if (pinfo->mode == l_aspect)
	//    fprintf (stderr, "  aspect %f\n", pinfo->aspect);
	//fprintf (stderr, "  size   %d\n", pinfo->sz);
	//fprintf (stderr, "  flags  %d\n", pinfo->flags);
    //}
    return pinfo.mode;
} finally {
LEAVING("dyb1n3lhbi0wnr9kvmu6onux9","parsePackModeInfo");
}
}




//3 bnxqpsmvz6tyekstfjte4pzwj
// pack_mode  getPackModeInfo(Agraph_t * g, pack_mode dflt, pack_info* pinfo) 
public static int getPackModeInfo(ST_Agraph_s g, int dflt, ST_pack_info pinfo) {
ENTERING("bnxqpsmvz6tyekstfjte4pzwj","getPackModeInfo");
try {
    return parsePackModeInfo (agget(g, new CString("packmode")), dflt, pinfo);
} finally {
LEAVING("bnxqpsmvz6tyekstfjte4pzwj","getPackModeInfo");
}
}




//3 7drbmsd08zij375icggr3egvy
// pack_mode  getPackMode(Agraph_t * g, pack_mode dflt) 
public static Object getPackMode(Object... arg) {
UNSUPPORTED("c1prvosj3g3y8yq8z6btufyr1"); // pack_mode 
UNSUPPORTED("acjweauaygv7o5gppq7c33mp7"); // getPackMode(Agraph_t * g, pack_mode dflt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c0d79ghjhm6kl6qz811tqj0ip"); //   pack_info info;
UNSUPPORTED("3abevvtclwlzp8dcg3zhzsnll"); //   return getPackModeInfo (g, dflt, &info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ata97fmix5q1oikrmk5pezvrf
// int getPack(Agraph_t * g, int not_def, int dflt) 
public static int getPack(ST_Agraph_s g, int not_def, int dflt) {
ENTERING("ata97fmix5q1oikrmk5pezvrf","getPack");
try {
    CString p;
    int i;
    int v = not_def;
    if ((p = agget(g, new CString("pack")))!=null) {
UNSUPPORTED("enpqar7c7okvibe7xhe0vjtfn"); // 	if ((sscanf(p, "%d", &i) == 1) && (i >= 0))
UNSUPPORTED("3puzxwczcmpxvlw8cvmeyio74"); // 	    v = i;
UNSUPPORTED("65w8fxtw319slbg2c6nvtmow8"); // 	else if ((*p == 't') || (*p == 'T'))
UNSUPPORTED("5lifdir9mxnvpi8ur34qo0jej"); // 	    v = dflt;
    }
    return v;
} finally {
LEAVING("ata97fmix5q1oikrmk5pezvrf","getPack");
}
}




//3 ce4a70w8ddkj4l9efi74h61s6
// pack_mode  getPackInfo(Agraph_t * g, pack_mode dflt, int dfltMargin, pack_info* pinfo) 
public static int getPackInfo(ST_Agraph_s g, int dflt, int dfltMargin, ST_pack_info pinfo) {
ENTERING("ce4a70w8ddkj4l9efi74h61s6","getPackInfo");
try {
    //assert (pinfo);
    pinfo.setInt("margin", getPack(g, dfltMargin, dfltMargin));
    //if (Verbose) {
	//fprintf (stderr, "  margin %d\n", pinfo->margin);
    //}
    pinfo.setInt("doSplines", 0);
    pinfo.setInt("fixed", 0);
    getPackModeInfo(g, dflt, pinfo);
    return pinfo.mode;
} finally {
LEAVING("ce4a70w8ddkj4l9efi74h61s6","getPackInfo");
}
}


}
