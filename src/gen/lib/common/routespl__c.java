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
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.pathplan.route__c.Proutespline;
import static gen.lib.pathplan.shortest__c.Pshortestpath;
import static gen.lib.pathplan.util__c.make_polyline;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.cos;
import static smetana.core.JUtils.sin;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ABS;
import static smetana.core.Macro.ALLOC_allocated_ST_Pedge_t;
import static smetana.core.Macro.ALLOC_allocated_ST_pointf;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.INT_MIN;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Ppoly_t;
import h.ST_boxf;
import h.ST_path;
import h.ST_pointf;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class routespl__c {
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


//1 6fn146yunkq3g30gummlety1l
// static int nedges, nboxes
//private static int nedges, nboxes;

//1 651u86vwr4psbjmrzsbg6zgi5
// static int routeinit
//private static int routeinit;

//1 bitbif2zv0lxox45wigfzbqcp
// static pointf *ps
//private static __ptr__ ps;

//1 5m31ig8bxuefkdin279ih13kc
// static int maxpn
//private static int maxpn;

//1 4u04uog28vlnmx0azm4y9qvj1
// static Ppoint_t *polypoints
//private static __ptr__ polypoints;

//1 eoo5tej8soeqa10cqz02mpetw
// static int polypointn
//private static int polypointn;

//1 dr2m01yvkuh5brtywxzvn5tal
// static Pedge_t *edges
//private static __ptr__ edges;

//1 cxjqaauevyqicged5zki42yg7
// static int edgen
//private static int edgen;



//3 7ebl6qohcfpf1b9ucih5r9qgp
// pointf* simpleSplineRoute (pointf tp, pointf hp, Ppoly_t poly, int* n_spl_pts,     int polyline) 
public static ST_pointf.Array simpleSplineRoute(final ST_pointf tp, final ST_pointf hp, final ST_Ppoly_t poly, int[] n_spl_pts, boolean polyline) {
// WARNING!! STRUCT
return simpleSplineRoute_w_(tp.copy(), hp.copy(), (ST_Ppoly_t) poly.copy(), n_spl_pts, polyline);
}
private static ST_pointf.Array simpleSplineRoute_w_(final ST_pointf tp, final ST_pointf hp, final ST_Ppoly_t poly, int[] n_spl_pts, boolean polyline) {
ENTERING("7ebl6qohcfpf1b9ucih5r9qgp","simpleSplineRoute");
try {
    final ST_Ppoly_t pl = new ST_Ppoly_t(), spl = new ST_Ppoly_t();
    final ST_pointf.Array eps = new ST_pointf.Array( 2);
    final ST_pointf.Array evs = new ST_pointf.Array( 2);
    int i;
    eps.plus(0).setDouble("x", tp.x);
    eps.plus(0).setDouble("y", tp.y);
    eps.plus(1).setDouble("x", hp.x);
    eps.plus(1).setDouble("y", hp.y);
    if (Pshortestpath(poly, eps, pl) < 0)
        return null;
    if (polyline)
	make_polyline (pl, spl);
    else {
	if (poly.pn > Z.z().edgen) {
	    Z.z().edges = ALLOC_allocated_ST_Pedge_t(Z.z().edges, poly.pn);
	    Z.z().edgen = poly.pn;
	}
	for (i = 0; i < poly.pn; i++) {
	    Z.z().edges.plus(i).setStruct("a", poly.ps.plus(i).getStruct());
	    Z.z().edges.plus(i).setStruct("b", poly.ps.plus((i + 1) % poly.pn).getStruct());
	}
	    evs.plus(0).setDouble("x", 0);
	    evs.plus(0).setDouble("y", 0);
	    evs.plus(1).setDouble("x", 0);
	    evs.plus(1).setDouble("y", 0);
	if (Proutespline(Z.z().edges, poly.pn, pl, evs.asPtr(), spl) < 0)
            return null;
    }
    if (mkspacep(spl.pn))
	return null;
    for (i = 0; i < spl.pn; i++) {
        Z.z().ps.plus(i).setStruct(spl.ps.plus(i).getStruct());
    }
    n_spl_pts[0] = spl.pn;
    return Z.z().ps;
} finally {
LEAVING("7ebl6qohcfpf1b9ucih5r9qgp","simpleSplineRoute");
}
}




//3 bfsrazjf3vkf12stnke48vc8t
// int routesplinesinit() 
public static int routesplinesinit() {
ENTERING("bfsrazjf3vkf12stnke48vc8t","routesplinesinit");
try {
    if (++Z.z().routeinit > 1) return 0;
    if (N(Z.z().ps = new ST_pointf.Array(300))) {
UNSUPPORTED("2qoo3na2ur9oh7hmvt6xv1txd"); // 	agerr(AGERR, "routesplinesinit: cannot allocate ps\n");
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
    }
    Z.z().maxpn = 300;
    Z.z().nedges = 0;
    Z.z().nboxes = 0;
    /*if (Verbose)
	start_timer();*/
    return 0;
} finally {
LEAVING("bfsrazjf3vkf12stnke48vc8t","routesplinesinit");
}
}




//3 55j3tny5cxemrsvrt3m21jxg8
// void routesplinesterm() 
public static void routesplinesterm() {
ENTERING("55j3tny5cxemrsvrt3m21jxg8","routesplinesterm");
try {
    if (--Z.z().routeinit > 0) return;
    Memory.free(Z.z().ps);
    /*if (Verbose)
	fprintf(stderr,
		"routesplines: %d edges, %d boxes %.2f sec\n",
		nedges, nboxes, elapsed_sec());*/
} finally {
LEAVING("55j3tny5cxemrsvrt3m21jxg8","routesplinesterm");
}
}




//3 cu8ssjizw7ileqe9u7tcclq7k
// static void limitBoxes (boxf* boxes, int boxn, pointf *pps, int pn, int delta) 
public static void limitBoxes(ST_boxf boxes[], int boxn, __ptr__ pps, int pn, int delta) {
ENTERING("cu8ssjizw7ileqe9u7tcclq7k","limitBoxes");
try {
    int bi, si, splinepi;
    double t;
    final ST_pointf.Array sp = new ST_pointf.Array( 4);
    int num_div = delta * boxn;
    for (splinepi = 0; splinepi + 3 < pn; splinepi += 3) {
	for (si = 0; si <= num_div; si++) {
	    t = si / (double)num_div;
	    sp.plus(0).getStruct().___(pps.plus(splinepi).getStruct());
	    sp.plus(1).getStruct().___(pps.plus(splinepi+1).getStruct());
	    sp.plus(2).getStruct().___(pps.plus(splinepi+2).getStruct());
	    sp.plus(3).getStruct().___(pps.plus(splinepi+3).getStruct());
	    sp.plus(0).setDouble("x", sp.get(0).x + t * (sp.get(1).x - sp.get(0).x));
	    sp.plus(0).setDouble("y", sp.get(0).y + t * (sp.get(1).y - sp.get(0).y));
	    sp.plus(1).setDouble("x", sp.get(1).x + t * (sp.get(2).x - sp.get(1).x));
	    sp.plus(1).setDouble("y", sp.get(1).y + t * (sp.get(2).y - sp.get(1).y));
	    sp.plus(2).setDouble("x", sp.get(2).x + t * (sp.get(3).x - sp.get(2).x));
	    sp.plus(2).setDouble("y", sp.get(2).y + t * (sp.get(3).y - sp.get(2).y));
 	    sp.plus(0).setDouble("x", sp.get(0).x + t * (sp.get(1).x - sp.get(0).x));
	    sp.plus(0).setDouble("y", sp.get(0).y + t * (sp.get(1).y - sp.get(0).y));
	    sp.plus(1).setDouble("x", sp.get(1).x + t * (sp.get(2).x - sp.get(1).x));
	    sp.plus(1).setDouble("y", sp.get(1).y + t * (sp.get(2).y - sp.get(1).y));
	    sp.plus(0).setDouble("x", sp.get(0).x + t * (sp.get(1).x - sp.get(0).x));
	    sp.plus(0).setDouble("y", sp.get(0).y + t * (sp.get(1).y - sp.get(0).y));
	    for (bi = 0; bi < boxn; bi++) {
/* this tested ok on 64bit machines, but on 32bit we need this FUDGE
 *     or graphs/directed/records.gv fails */
		if (sp.get(0).y <= boxes[bi].UR.y+.0001 && sp.get(0).y >= boxes[bi].LL.y-.0001) {
		    if (boxes[bi].LL.x > sp.get(0).x)
			boxes[bi].LL.setDouble("x", sp.get(0).x);
		    if (boxes[bi].UR.x < sp.get(0).x)
			boxes[bi].UR.setDouble("x", sp.get(0).x);
		}
	    }
	}
    }
} finally {
LEAVING("cu8ssjizw7ileqe9u7tcclq7k","limitBoxes");
}
}




//3 3mcnemqisisnqtd4mr72ej76y
// static pointf *_routesplines(path * pp, int *npoints, int polyline) 
public static __ptr__ _routesplines(ST_path pp, int npoints[], int polyline) {
ENTERING("3mcnemqisisnqtd4mr72ej76y","_routesplines");
try {
    final ST_Ppoly_t poly = new ST_Ppoly_t();
    final ST_Ppoly_t pl  = new ST_Ppoly_t(), spl = new ST_Ppoly_t();
    int splinepi;
    final ST_pointf.Array eps = new ST_pointf.Array( 2);
    final ST_pointf.Array evs = new ST_pointf.Array( 2);
    int edgei, prev, next;
    int pi=0, bi;
    ST_boxf[] boxes;
    int boxn;
    ST_Agedge_s realedge;
    int flip;
    int loopcnt, delta = 10;
    boolean unbounded;
    Z.z().nedges++;
    Z.z().nboxes += pp.nbox;
    for (realedge = (ST_Agedge_s) pp.data.castTo(ST_Agedge_s.class);
	 realedge!=null && ED_edge_type(realedge) != 0;
	 realedge = ED_to_orig(realedge));
    if (N(realedge)) {
	UNSUPPORTED("agerr(AGERR, _in routesplines, cannot find NORMAL edge");
	return null;
    }
    boxes = pp.boxes;
    boxn = pp.nbox;
    if (checkpath(boxn, boxes, pp)!=0)
	return null;
    if (boxn * 8 > Z.z().polypointn) {
	Z.z().polypoints = ALLOC_allocated_ST_pointf(Z.z().polypoints, boxn * 8);
	Z.z().polypointn = boxn * 8;
    }
    if ((boxn > 1) && (((ST_boxf)boxes[0]).LL.y > ((ST_boxf)boxes[1]).LL.y)) {
        flip = 1;
	for (bi = 0; bi < boxn; bi++) {
	    double v = ((ST_boxf)boxes[bi]).UR.y;
	    ((ST_boxf)boxes[bi]).UR.y= -1*((ST_boxf)boxes[bi]).LL.y;
	    ((ST_boxf)boxes[bi]).LL.y = -v;
	}
    }
    else flip = 0;
    if (NEQ(agtail(realedge), aghead(realedge))) {
	/* I assume that the path goes either down only or
	   up - right - down */
	for (bi = 0, pi = 0; bi < boxn; bi++) {
	    next = prev = 0;
	    if (bi > 0)
		prev = (((ST_boxf)boxes[bi]).LL.y > ((ST_boxf)boxes[bi-1]).LL.y) ? -1 : 1;
	    if (bi < boxn - 1)
		next = (((ST_boxf)boxes[bi+1]).LL.y > ((ST_boxf)boxes[bi]).LL.y) ? 1 : -1;
	    if (prev != next) {
		if (next == -1 || prev == 1) {
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).LL.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).UR.y);
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).LL.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).LL.y);
		} else {
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).UR.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).LL.y);
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).UR.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).UR.y);
		}
	    }
	    else if (prev == 0) { /* single box */
UNSUPPORTED("2bfai79qe7cec0rljrn56jg2f"); // 		polypoints[pi].x = boxes[bi].LL.x;
UNSUPPORTED("cjppvcr7k9pknjrjugccsky56"); // 		polypoints[pi++].y = boxes[bi].UR.y;
UNSUPPORTED("2bfai79qe7cec0rljrn56jg2f"); // 		polypoints[pi].x = boxes[bi].LL.x;
UNSUPPORTED("99xeozpks5v0iza4sv2occuuq"); // 		polypoints[pi++].y = boxes[bi].LL.y;
	    } 
	    else {
 		if (N(prev == -1 && next == -1)) {
UNSUPPORTED("cgpvvfb9phbipyhij0cjh1nmi"); // 		    agerr(AGERR, "in routesplines, illegal values of prev %d and next %d, line %d\n", prev, next, 444);
UNSUPPORTED("9idk92zg2ysz316lfwzvvvde6"); // 		    return NULL;
 		}
	    }
	}
	for (bi = boxn - 1; bi >= 0; bi--) {
	    next = prev = 0;
	    if (bi < boxn - 1)
		prev = (((ST_boxf)boxes[bi]).LL.y > ((ST_boxf)boxes[bi+1]).LL.y) ? -1 : 1;
	    if (bi > 0)
		next = (((ST_boxf)boxes[bi-1]).LL.y > ((ST_boxf)boxes[bi]).LL.y) ? 1 : -1;
	    if (prev != next) {
		if (next == -1 || prev == 1 ) {
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).LL.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).UR.y);
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).LL.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).LL.y);
		} else {
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).UR.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).LL.y);
		    Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).UR.x);
		    Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).UR.y);
		}
	    } 
	    else if (prev == 0) { /* single box */
UNSUPPORTED("ya84m81ogarx28l99om39lba"); // 		polypoints[pi].x = boxes[bi].UR.x;
UNSUPPORTED("99xeozpks5v0iza4sv2occuuq"); // 		polypoints[pi++].y = boxes[bi].LL.y;
UNSUPPORTED("ya84m81ogarx28l99om39lba"); // 		polypoints[pi].x = boxes[bi].UR.x;
UNSUPPORTED("cjppvcr7k9pknjrjugccsky56"); // 		polypoints[pi++].y = boxes[bi].UR.y;
	    }
	    else {
		if (N(prev == -1 && next == -1)) {
UNSUPPORTED("87y5d0ts6xdjyx905bha50f3s"); // 		    /* it went badly, e.g. degenerate box in boxlist */
UNSUPPORTED("1qt7hixteu3pt64wk1sqw352a"); // 		    agerr(AGERR, "in routesplines, illegal values of prev %d and next %d, line %d\n", prev, next, 476);
UNSUPPORTED("35untdbpd42pt4c74gjbxqx7q"); // 		    return NULL; /* for correctness sake, it's best to just stop */
		}
		Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).UR.x);
		Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).LL.y);
		Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).UR.x);
		Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).UR.y);
		Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).LL.x);
		Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).UR.y);
		Z.z().polypoints.plus(pi).setDouble("x", ((ST_boxf)boxes[bi]).LL.x);
		Z.z().polypoints.plus(pi++).setDouble("y", ((ST_boxf)boxes[bi]).LL.y);
	    }
	}
    }
    else {
UNSUPPORTED("1izvmtfwbnl5xq4u2x5fdraxp"); // 	agerr(AGERR, "in routesplines, edge is a loop at %s\n", agnameof(aghead(realedge)));
UNSUPPORTED("11hwqop4xebvtcskop4uhpp01"); // 	return NULL;
    }
    if (flip!=0) {
	int i;
	for (bi = 0; bi < boxn; bi++) {
	    int v = (int) ((ST_boxf)boxes[bi]).UR.y;
	    ((ST_boxf)boxes[bi]).UR.y = -1*((ST_boxf)boxes[bi]).LL.y;
	    ((ST_boxf)boxes[bi]).LL.y = -v;
	}
	for (i = 0; i < pi; i++)
	    Z.z().polypoints.plus(i).setDouble("y", -1 * Z.z().polypoints.get(i).y);
    }
    for (bi = 0; bi < boxn; bi++) {
	((ST_boxf)boxes[bi]).LL.x = INT_MAX;
	((ST_boxf)boxes[bi]).UR.x = INT_MIN;
	}
    poly.ps = Z.z().polypoints;
    poly.pn = pi;
    eps.plus(0).getStruct().setDouble("x", pp.start.p.x);
    eps.plus(0).getStruct().setDouble("y", pp.start.p.y);
    eps.plus(1).getStruct().setDouble("x", pp.end.p.x);
    eps.plus(1).getStruct().setDouble("y", pp.end.p.y);
    if (Pshortestpath(poly, eps, pl) < 0) {
		System.err.println("in routesplines, Pshortestpath failed\n");
		return null;
    }
    if (polyline!=0) {
UNSUPPORTED("48veztc3k9dfw8tqolu7jsktk"); // 	make_polyline (pl, &spl);
    }
    else {
	if (poly.pn > Z.z().edgen) {
	    Z.z().edges = ALLOC_allocated_ST_Pedge_t(Z.z().edges, poly.pn);
	    Z.z().edgen = poly.pn;
	}
	for (edgei = 0; edgei < poly.pn; edgei++) {
	    Z.z().edges.plus(edgei).setStruct("a", Z.z().polypoints.plus(edgei).getStruct());
	    Z.z().edges.plus(edgei).setStruct("b", Z.z().polypoints.plus((edgei + 1) % poly.pn).getStruct());
	}
	if (pp.start.constrained!=0) {
 	    evs.plus(0).getStruct().setDouble("x", cos(pp.start.theta));
 	    evs.plus(0).getStruct().setDouble("y", sin(pp.start.theta));
	} else
	{
	    evs.plus(0).getStruct().setDouble("x", 0);
	    evs.plus(0).getStruct().setDouble("y", 0);
    }
	if (pp.end.constrained!=0) {
 	    evs.plus(1).getStruct().setDouble("x", -cos(pp.end.theta));
 	    evs.plus(1).getStruct().setDouble("y", -sin(pp.end.theta));
	} else
	{
	    evs.plus(1).getStruct().setDouble("x", 0);
	    evs.plus(1).getStruct().setDouble("y", 0);
	}
	if (Proutespline(Z.z().edges, poly.pn, pl, evs.asPtr(), spl) < 0) {
UNSUPPORTED("elkeyywrfd4hq75w7toc94rzs"); // 	    agerr(AGERR, "in routesplines, Proutespline failed\n");
UNSUPPORTED("7t3fvwp9cv90qu5bdjdglcgtk"); // 	    return NULL;
	}
    }
    if (mkspacep(spl.pn))
UNSUPPORTED("7x5kpcbvg4va887hky7ufm45y"); // 	return NULL;  /* Bailout if no memory left */
    for (bi = 0; bi < boxn; bi++) {
    	boxes[bi].LL.setDouble("x", INT_MAX);
    	boxes[bi].UR.setDouble("x", INT_MIN);
    }
    unbounded = NOT(false);
    for (splinepi = 0; splinepi < spl.pn; splinepi++) {
	Z.z().ps.plus(splinepi).setStruct(spl.ps.plus(splinepi).getStruct());
    }
    for (loopcnt = 0; unbounded && (loopcnt < 15); loopcnt++) {
	limitBoxes (boxes, boxn, Z.z().ps, spl.pn, delta);
    /* The following check is necessary because if a box is not very 
     * high, it is possible that the sampling above might miss it.
     * Therefore, we make the sample finer until all boxes have
     * valid values. cf. bug 456. Would making sp[] pointfs help?
     */
	for (bi = 0; bi < boxn; bi++) {
	/* these fp equality tests are used only to detect if the
	 * values have been changed since initialization - ok */
	    if ((boxes[bi].LL.x == INT_MAX) || (boxes[bi].UR.x == INT_MIN)) {
		delta *= 2; /* try again with a finer interval */
		if (delta > INT_MAX/boxn) /* in limitBoxes, boxn*delta must fit in an int, so give up */
		    loopcnt = 15;
		break;
	    }
	}
	if (bi == boxn)
	    unbounded = false;
    }
    if (unbounded) {  
	/* Either an extremely short, even degenerate, box, or some failure with the path
         * planner causing the spline to miss some boxes. In any case, use the shortest path 
	 * to bound the boxes. This will probably mean a bad edge, but we avoid an infinite
	 * loop and we can see the bad edge, and even use the showboxes scaffolding.
	 */
	final ST_Ppoly_t polyspl = new ST_Ppoly_t();
	System.err.println("Unable to reclaim box space in spline routing for edge \"%s\" -> \"%s\". Something is probably seriously wrong.\n");
	make_polyline (pl, polyspl);
	limitBoxes (boxes, boxn, polyspl.ps, polyspl.pn, 10);
	Memory.free (polyspl.ps);
    }
    npoints[0] = spl.pn;
    return Z.z().ps;
} finally {
LEAVING("3mcnemqisisnqtd4mr72ej76y","_routesplines");
}
}




//3 axqoytp2rpr8crajhkuvns6q9
// pointf *routesplines(path * pp, int *npoints) 
public static ST_pointf.Array routesplines(ST_path pp, int npoints[]) {
ENTERING("axqoytp2rpr8crajhkuvns6q9","routesplines");
try {
    return (ST_pointf.Array) _routesplines (pp, npoints, 0);
} finally {
LEAVING("axqoytp2rpr8crajhkuvns6q9","routesplines");
}
}




//3 2v22s41xitwnnsljk9n01nrcy
// pointf *routepolylines(path * pp, int *npoints) 
public static ST_pointf.Array routepolylines(ST_path pp, int npoints[]) {
ENTERING("2v22s41xitwnnsljk9n01nrcy","routepolylines");
try {
 UNSUPPORTED("1kjkc6bl8zpf8zjcwgbyni9p3"); // pointf *routepolylines(path * pp, int *npoints)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6wf7p4npeom96y0l1v39essmx"); //     return _routesplines (pp, npoints, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("2v22s41xitwnnsljk9n01nrcy","routepolylines");
}
}




//3 65qv6x7ghwyt6hey5qd8cgizn
// static int overlap(int i0, int i1, int j0, int j1) 
public static int overlap(double i0, double i1, double j0, double j1) {return overlap((int) i0, (int) i1, (int) j0, (int) j1);}
public static int overlap(int i0, int i1, int j0, int j1) {
ENTERING("65qv6x7ghwyt6hey5qd8cgizn","overlap");
try {
    /* i'll bet there's an elegant way to do this */
    if (i1 <= j0)
	return 0;
    if (i0 >= j1)
	return 0;
    if ((j0 <= i0) && (i0 <= j1))
	return (j1 - i0);
    if ((j0 <= i1) && (i1 <= j1))
	return (i1 - j0);
    return MIN(i1 - i0, j1 - j0);
} finally {
LEAVING("65qv6x7ghwyt6hey5qd8cgizn","overlap");
}
}




//3 dxqjhiid5f58b9gjxp0v3j97b
// static int checkpath(int boxn, boxf* boxes, path* thepath) 
public static int checkpath(int boxn, ST_boxf[] boxes, ST_path thepath) {
ENTERING("dxqjhiid5f58b9gjxp0v3j97b","checkpath");
try {
	ST_boxf ba, bb;
    int bi, i, errs, l, r, d, u;
    int xoverlap, yoverlap;
    /* remove degenerate boxes. */
    i = 0;
    for (bi = 0; bi < boxn; bi++) {
	if (ABS(((ST_boxf)boxes[bi]).LL.y - ((ST_boxf)boxes[bi]).UR.y) < .01)
	    continue;
	if (ABS(((ST_boxf)boxes[bi]).LL.x - ((ST_boxf)boxes[bi]).UR.x) < .01)
	    continue;
	if (i != bi)
	    boxes[i].setStruct(boxes[bi]);
	i++;
    }
    boxn = i;
    ba = (ST_boxf) boxes[0];
    if (ba.LL.x > ba.UR.x || ba.LL.y > ba.UR.y) {
UNSUPPORTED("39tznwvf6k5lgj78jp32p0kfl"); // 	agerr(AGERR, "in checkpath, box 0 has LL coord > UR coord\n");
UNSUPPORTED("evdvb9esh16y8zeoczxhcz7xm"); // 	printpath(thepath);
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
    }
    for (bi = 0; bi < boxn - 1; bi++) {
	ba = (ST_boxf) boxes[bi];
	bb = (ST_boxf) boxes[bi + 1];
	if (bb.LL.x > bb.UR.x || bb.LL.y > bb.UR.y) {
UNSUPPORTED("c8oodo0ge4n4dglb28fvf610v"); // 	    agerr(AGERR, "in checkpath, box %d has LL coord > UR coord\n",
UNSUPPORTED("929pkk2ob1lh7hfe4scuoo5pn"); // 		  bi + 1);
UNSUPPORTED("2m9o6g4nneiul4gt8xb9yb9zi"); // 	    printpath(thepath);
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
	}
	l = (ba.UR.x < bb.LL.x) ? 1 : 0;
	r = (ba.LL.x > bb.UR.x) ? 1 : 0;
	d = (ba.UR.y < bb.LL.y) ? 1 : 0;
	u = (ba.LL.y > bb.UR.y) ? 1 : 0;
	errs = l + r + d + u;
	/*if (errs > 0 && Verbose) {
	    fprintf(stderr, "in checkpath, boxes %d and %d don't touch\n",
		    bi, bi + 1);
	    printpath(thepath);
	}*/
	if (errs > 0) {
	    int xy;
	    if (l == 1)
	    {
		xy = (int) ba.UR.x;
		ba.UR.setDouble("x", bb.LL.x);
		bb.LL.setDouble("x", xy);
		l = 0;
		}
	    else if (r == 1) {
	    	xy = (int)(ba.LL.x);
	    	ba.LL.setDouble("x", bb.UR.x);
	    	bb.UR.setDouble("x", xy);
	    	r = 0;
	    }
	    else if (d == 1) {
	    	xy = (int)(ba.UR.y);
	    	ba.UR.setDouble("y", bb.LL.y);
	    	bb.LL.setDouble("y", xy);
	    	d = 0;
	    }
	    else if (u == 1)
UNSUPPORTED("5kcd52bwvbxxs0md0enfs100u"); // 		xy = ba.LL.y, ba.LL.y = bb.UR.y, bb.UR.y = xy, u = 0;
	    for (i = 0; i < errs - 1; i++) {
UNSUPPORTED("as3p2ldwbg3rbgy64oxx5phar"); // 		if (l == 1)
UNSUPPORTED("efz1z5cfywki1k6q6avldku9z"); // 		    xy = (ba.UR.x + bb.LL.x) / 2.0 + 0.5, ba.UR.x =
UNSUPPORTED("6dfh7cf1xptapqd1mcqtxjrxa"); // 			bb.LL.x = xy, l = 0;
UNSUPPORTED("ang3qytu77fd5owijwbnmkdav"); // 		else if (r == 1)
UNSUPPORTED("67ehof0qqlk339zgl0sqwfu5r"); // 		    xy = (ba.LL.x + bb.UR.x) / 2.0 + 0.5, ba.LL.x =
UNSUPPORTED("llmwvndoq1ne9c62ohtstkwa"); // 			bb.UR.x = xy, r = 0;
UNSUPPORTED("3ce9i9asrqbuog7v1tdurqo6e"); // 		else if (d == 1)
UNSUPPORTED("3mibjrb2jtfextkg9ac5k9spl"); // 		    xy = (ba.UR.y + bb.LL.y) / 2.0 + 0.5, ba.UR.y =
UNSUPPORTED("bccpbv2n38c5utkfh7msoc2y"); // 			bb.LL.y = xy, d = 0;
UNSUPPORTED("7302rnmwdji9n7txquk8k36to"); // 		else if (u == 1)
UNSUPPORTED("9oqpoodvpheztihe63p40guof"); // 		    xy = (ba.LL.y + bb.UR.y) / 2.0 + 0.5, ba.LL.y =
UNSUPPORTED("2cnb1bdjh6y26f98vonla73qa"); // 			bb.UR.y = xy, u = 0;
	    }
	}
	/* check for overlapping boxes */
	xoverlap = overlap(ba.LL.x, ba.UR.x, bb.LL.x, bb.UR.x);
	yoverlap = overlap(ba.LL.y, ba.UR.y, bb.LL.y, bb.UR.y);
	if (xoverlap!=0 && yoverlap!=0) {
 	    if (xoverlap < yoverlap) {
 	    	if (ba.UR.x - ba.LL.x > bb.UR.x - bb.LL.x) {
 	    		/* take space from ba */
UNSUPPORTED("5dqxf3gq05pjtobtnru1g2tuj"); // 		    if (ba.UR.x < bb.UR.x)
UNSUPPORTED("8gz6k803qp9zyw9s459cpp039"); // 			ba.UR.x = bb.LL.x;
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("5r6ck8hfb1cxywn9go61se9kx"); // 			ba.LL.x = bb.UR.x;
 	    	} else {
 	    		/* take space from bb */
 	    		if (ba.UR.x < bb.UR.x)
 	    			bb.LL.setDouble("x", ba.UR.x);
 	    		else
 	    			bb.UR.setDouble("x", ba.LL.x);
 	    	}
 	    } else {		/* symmetric for y coords */
 	    	if (ba.UR.y - ba.LL.y > bb.UR.y - bb.LL.y) {
 	    		/* take space from ba */
 	    		if (ba.UR.y < bb.UR.y)
 	    			ba.UR.setDouble("y", bb.LL.y);
 	    		else
 	    			ba.LL.setDouble("y", bb.UR.y);
 	    	} else {
 	    		/* take space from bb */
 	    		if (ba.UR.y < bb.UR.y)
 	    			bb.LL.setDouble("y", ba.UR.y);
 	    		else
 	    			bb.UR.setDouble("y", ba.LL.y);
 	    	}
	    }
	}
    }
    if (thepath.start.p.x < ((ST_boxf)boxes[0]).LL.x
	|| thepath.start.p.x > ((ST_boxf)boxes[0]).UR.x
	|| thepath.start.p.y < ((ST_boxf)boxes[0]).LL.y
	|| thepath.start.p.y > ((ST_boxf)boxes[0]).UR.y) {
	/*if (Verbose) {
	    fprintf(stderr, "in checkpath, start port not in first box\n");
	    printpath(thepath);
	}*/
	if (thepath.start.p.x < ((ST_boxf)boxes[0]).LL.x)
	    thepath.start.p.setDouble("x", ((ST_boxf)boxes[0]).LL.x);
	if (thepath.start.p.x > ((ST_boxf)boxes[0]).UR.x)
	    thepath.start.p.setDouble("x", ((ST_boxf)boxes[0]).UR.x);
	if (thepath.start.p.y < ((ST_boxf)boxes[0]).LL.y)
	    thepath.start.p.setDouble("y", ((ST_boxf)boxes[0]).LL.y);
	if (thepath.start.p.y > ((ST_boxf)boxes[0]).UR.y)
	    thepath.start.p.setDouble("y", ((ST_boxf)boxes[0]).UR.y);
    }
    if (thepath.end.p.x < ((ST_boxf)boxes[boxn - 1]).LL.x
	|| thepath.end.p.x > ((ST_boxf)boxes[boxn - 1]).UR.x
	|| thepath.end.p.y < ((ST_boxf)boxes[boxn - 1]).LL.y
	|| thepath.end.p.y > ((ST_boxf)boxes[boxn - 1]).UR.y) {
	/*if (Verbose) {
	    fprintf(stderr, "in checkpath, end port not in last box\n");
	    printpath(thepath);
	}*/
	if (thepath.end.p.x < ((ST_boxf)boxes[boxn - 1]).LL.x)
	    thepath.end.p.setDouble("x", ((ST_boxf)boxes[boxn - 1]).LL.x);
	if (thepath.end.p.x > ((ST_boxf)boxes[boxn - 1]).UR.x)
	    thepath.end.p.setDouble("x", ((ST_boxf)boxes[boxn - 1]).UR.x);
	if (thepath.end.p.y < ((ST_boxf)boxes[boxn - 1]).LL.y)
	    thepath.end.p.setDouble("y", ((ST_boxf)boxes[boxn - 1]).LL.y);
	if (thepath.end.p.y > ((ST_boxf)boxes[boxn - 1]).UR.y)
	    thepath.end.p.setDouble("y", ((ST_boxf)boxes[boxn - 1]).UR.y);
    }
    return 0;
} finally {
LEAVING("dxqjhiid5f58b9gjxp0v3j97b","checkpath");
}
}




//3 de6jvvw786rx88318tuuqywgq
// static int mkspacep(int size) 
public static boolean mkspacep(int size) {
ENTERING("de6jvvw786rx88318tuuqywgq","mkspacep");
try {
    if (size > Z.z().maxpn) {
	int newmax = Z.z().maxpn + (size / 300 + 1) * 300;
	Z.z().ps = Z.z().ps.reallocJ(newmax);
	if (N(Z.z().ps)) {
UNSUPPORTED("ds2v91aohji00tc7zmjuc3v6q"); // 	    agerr(AGERR, "cannot re-allocate ps\n");
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
	}
	Z.z().maxpn = newmax;
    }
    return false;
} finally {
LEAVING("de6jvvw786rx88318tuuqywgq","mkspacep");
}
}




//3 eoba7h2vv4nbevb7t3uj8xdxd
// static void printpath(path * pp) 
public static Object printpath(Object... arg) {
UNSUPPORTED("baqhg7gu97aw2vlb1lexu7ocs"); // static void printpath(path * pp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8k9loe0uz6grwq8fq5iqfih2c"); //     int bi;
UNSUPPORTED("hop7xuggqoec7e0p0gpzb5ys"); //     fprintf(stderr, "%d boxes:\n", pp->nbox);
UNSUPPORTED("fqxwgt6brxm5novm8sey6vp3"); //     for (bi = 0; bi < pp->nbox; bi++)
UNSUPPORTED("2n85v7ex13a1rhnx63lam2yth"); // 	fprintf(stderr, "%d (%.5g, %.5g), (%.5g, %.5g)\n", bi,
UNSUPPORTED("86zhcjpiy229kbbomcqmj1bvn"); // 		pp->boxes[bi].LL.x, pp->boxes[bi].LL.y,
UNSUPPORTED("dbddokl6i6319f232oqnbecmv"); // 	       	pp->boxes[bi].UR.x, pp->boxes[bi].UR.y);
UNSUPPORTED("867y6166h7x6yibezm0cb0cf1"); //     fprintf(stderr, "start port: (%.5g, %.5g), tangent angle: %.5g, %s\n",
UNSUPPORTED("8nze219nhghn1a0uewjfq30xr"); // 	    pp->start.p.x, pp->start.p.y, pp->start.theta,
UNSUPPORTED("1apwqgz53812wo2ejjdn8zz3h"); // 	    pp->start.constrained ? "constrained" : "not constrained");
UNSUPPORTED("4i8p7ha6i9bkydbg3i04ssmqj"); //     fprintf(stderr, "end port: (%.5g, %.5g), tangent angle: %.5g, %s\n",
UNSUPPORTED("32nx4386ouvodf09v0u4emfkv"); // 	    pp->end.p.x, pp->end.p.y, pp->end.theta,
UNSUPPORTED("d7cxrqy42haay280fzr1e81ts"); // 	    pp->end.constrained ? "constrained" : "not constrained");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6u7mpvz8gz08jacc16azxm31t
// static pointf get_centroid(Agraph_t *g) 
public static Object get_centroid(Object... arg) {
UNSUPPORTED("2zo1gwkn1bj1agy4g8dxcfmh"); // static pointf get_centroid(Agraph_t *g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6reqgncjwgiuo3fb8rkhmtjv1"); //     int     cnt = 0;
UNSUPPORTED("e1s7ys0dbvp12r1vo9cdl7sp0"); //     static pointf   sum = {0.0, 0.0};
UNSUPPORTED("4nx1l2ustbo0ptupk2ja9ekds"); //     static Agraph_t *save;
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("f17csexi07fwmif5pkxj2wgr1"); //     sum.x = (GD_bb(g).LL.x + GD_bb(g).UR.x) / 2.0;
UNSUPPORTED("8hoglhn0m8txbvp8w9fvo5cwt"); //     sum.y = (GD_bb(g).LL.y + GD_bb(g).UR.y) / 2.0;
UNSUPPORTED("e8d6re0f7zq6d14zby6lxtm85"); //     return sum;
UNSUPPORTED("5xw9py1u6p24f7e8fju2hsmw0"); //     if (save == g) return sum;
UNSUPPORTED("a1cvki7btccn0pv2gpusya4pt"); //     save = g;
UNSUPPORTED("7wq24g054kmx3aw25vk5ksj4"); //     for (n = agfstnode(g); n; n = agnxtnode(g,n)) {
UNSUPPORTED("7mgmah70c4cze5ed5zlcjfnpj"); //         sum.x += ND_pos(n)[0];
UNSUPPORTED("3pk32i8trbg5r4tjfzi0xepsa"); //         sum.y += ND_pos(n)[1];
UNSUPPORTED("77mslev6fngkh87g2jkd6j87j"); //         cnt++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ci5ydgm2xpqvgw8qn5sz0317w"); //     sum.x = sum.x / cnt;
UNSUPPORTED("f1ofwhoh52tmotz2jx74ji4h5"); //     sum.y = sum.y / cnt;
UNSUPPORTED("e8d6re0f7zq6d14zby6lxtm85"); //     return sum;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7l11iqopuq2ovl93rxmwkobas
// static void bend(pointf spl[4], pointf centroid) 
public static Object bend(Object... arg) {
UNSUPPORTED("4ftn2rr6n4c36dbi9as8t8g5h"); // static void bend(pointf spl[4], pointf centroid)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1xm6245yjmk93iuvvm93e1rci"); //     pointf  midpt,a;
UNSUPPORTED("6zgogm7cuuy7co8hhompfoo5"); //     double  r;
UNSUPPORTED("9lxjpyaeeeq2agdn7fik6kzhs"); //     double  dist,dx,dy;
UNSUPPORTED("5bkk1q959a76y20xt0228j9d7"); //     midpt.x = (spl[0].x + spl[3].x)/2.0;
UNSUPPORTED("bfx7hrazv0khewy4e1ejp2kyb"); //     midpt.y = (spl[0].y + spl[3].y)/2.0;
UNSUPPORTED("9ydjah76t018eukiay40izzko"); //     dx = (spl[3].x - spl[0].x);
UNSUPPORTED("ap0c81vlb8rsjnpumymlnvzrl"); //     dy = (spl[3].y - spl[0].y);
UNSUPPORTED("dfs4ufukqaz9pf7ey54fbuaco"); //     dist = sqrt(dx*dx + dy*dy);
UNSUPPORTED("97su53ievky8nzohnw0en0a6e"); //     r = dist/5.0;
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("cltgduzxyw56ktxuoeoqt4bkm"); //         double vX = centroid.x - midpt.x;
UNSUPPORTED("h4thwscp36mqboiugjkim5um"); //         double vY = centroid.y - midpt.y;
UNSUPPORTED("5q6wpyh72kf9xggy2oc929uc3"); //         double magV = sqrt(vX*vX + vY*vY);
UNSUPPORTED("1w5d961mnlte15ap13tdi5sv2"); //         a.x = midpt.x - vX / magV * r;      /* + would be closest point */
UNSUPPORTED("2oxj7hzixzdciej9f8u7xe49o"); //         a.y = midpt.y - vY / magV * r;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("645kbqr5zpbe6vj9z3o2x5yrb"); //     /* this can be improved */
UNSUPPORTED("72i64plp6g9odwle7ly7wgn4a"); //     spl[1].x = spl[2].x = a.x;
UNSUPPORTED("at1u1jcu4ulhd191p0rfc9e8j"); //     spl[1].y = spl[2].y = a.y;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2erpr561ggowmq2m9465p5kqd
// void  makeStraightEdge(graph_t * g, edge_t * e, int et, splineInfo* sinfo) 
public static Object makeStraightEdge(Object... arg) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("89t1p0jilkexnvk5i1vv1arc0"); // makeStraightEdge(graph_t * g, edge_t * e, int et, splineInfo* sinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7pmvaj5a5w3pee9zkh22s4jt5"); //     edge_t *e0;
UNSUPPORTED("821nfzu5iwd6ayryeggj37hj2"); //     edge_t** edges;
UNSUPPORTED("19s88cb2xh6yjpqg0ip3ovi08"); //     edge_t* elist[20];
UNSUPPORTED("5dd4r3fk1fwybt8tvqt4z4ta3"); //     int i, e_cnt;
UNSUPPORTED("310og4kvqsrlm4vs26zqw8c8p"); //     e_cnt = 1;
UNSUPPORTED("63kfcznl8sc62h1pj6k83cjn3"); //     e0 = e;
UNSUPPORTED("1451mr2njzp5m6mj64k6m36tf"); //     while ((e0 = ED_to_virt(e0))) e_cnt++;
UNSUPPORTED("1qovtxs3b3fa6ztj4wx1ahkf2"); //     if (e_cnt <= 20)
UNSUPPORTED("bp3rco5i77vkqs7wua0k7dbfo"); // 	edges = elist;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("8zwb4z35iwpyfy9d9n4b51yvh"); // 	edges = (edge_t**)zmalloc((e_cnt)*sizeof(edge_t*));
UNSUPPORTED("63kfcznl8sc62h1pj6k83cjn3"); //     e0 = e;
UNSUPPORTED("tf4qi3e2hsjxi603z57w6hx6"); //     for (i = 0; i < e_cnt; i++) {
UNSUPPORTED("106t1hs57atf24mgepcp9wwjw"); // 	edges[i] = e0;
UNSUPPORTED("dfdtts0ddwzo6ffy5m1pso8t6"); // 	e0 = ED_to_virt(e0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("db2lohkib2plgiw7i90nxgkjk"); //     makeStraightEdges (g, edges, e_cnt, et, sinfo);
UNSUPPORTED("b1xlizpp11lvumjyajfk6mxky"); //     if (e_cnt > 20) free (edges);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5ztzq4z6l8uj7cn0d1ke7n87p
// void  makeStraightEdges(graph_t * g, edge_t** edges, int e_cnt, int et, splineInfo* sinfo) 
public static Object makeStraightEdges(Object... arg) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("ayvyrhydt6lt2z6uvpxyh2nxn"); // makeStraightEdges(graph_t * g, edge_t** edges, int e_cnt, int et, splineInfo* sinfo)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5u4oz0rrd9yradyuzr6qniald"); //     pointf dumb[4];
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("8i0kwb8r6a2sj6nuvcy3bg9rx"); //     node_t *head;
UNSUPPORTED("b6jt41a0u7si76m5i0lookv4x"); //     int curved = (et == (2 << 1));
UNSUPPORTED("2qrm9sibdk45g8miaadnlmf0z"); //     pointf perp;
UNSUPPORTED("e88o7l9ftcoq7m3q7njvm0hjb"); //     pointf del;
UNSUPPORTED("7pmvaj5a5w3pee9zkh22s4jt5"); //     edge_t *e0;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("39f8qp0sf99n5kz67fe1sjnao"); //     int i, j, xstep, dx;
UNSUPPORTED("5qf9bfxinlwybt6vckmul8n5c"); //     double l_perp;
UNSUPPORTED("biqd62eqps6szrtk8p8sci92q"); //     pointf dumber[4];
UNSUPPORTED("3ircl7kohy2qt6yrj3lysuhor"); //     pointf p, q;
UNSUPPORTED("b0ek9vxrdn6l0aponkc0ezxof"); //     e = edges[0];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("9vcgvjkna4elv9mw682bcl1ry"); //     head = aghead(e);
UNSUPPORTED("b7iaxy7swt1knhamnyh2kvzlj"); //     p = dumb[1] = dumb[0] = add_pointf(ND_coord(n), ED_tail_port(e).p);
UNSUPPORTED("di45fpacl09sotabtbtbrk4bw"); //     q = dumb[2] = dumb[3] = add_pointf(ND_coord(head), ED_head_port(e).p);
UNSUPPORTED("9yh32i6luwgciwhvrfvt8aw1a"); //     if ((e_cnt == 1) || Concentrate) {
UNSUPPORTED("coqqvdvknesi3rdbg68qcc6vn"); // 	if (curved) bend(dumb,get_centroid(g));
UNSUPPORTED("ejd1q887zolcoupzctycvs5cv"); // 	clip_and_install(e, aghead(e), dumb, 4, sinfo);
UNSUPPORTED("11s5bteac4942mtfiiy4qbq5t"); // 	addEdgeLabels(g, e, p, q);
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("63kfcznl8sc62h1pj6k83cjn3"); //     e0 = e;
UNSUPPORTED("6qz2idcg1avhf5caqig9ewcxv"); //     if (APPROXEQPT(dumb[0], dumb[3], MILLIPOINT)) {
UNSUPPORTED("738uip25tpsxuc3qvsye90wm7"); // 	/* degenerate case */
UNSUPPORTED("bq580fqcdybbomvpm4qmh5jtq"); // 	dumb[1] = dumb[0];
UNSUPPORTED("8u36zhoh7cpf1dv8ky0ngt667"); // 	dumb[2] = dumb[3];
UNSUPPORTED("bgjd2ldhnogj885m52k0nxe0d"); // 	del.x = 0;
UNSUPPORTED("b1x914d40c33xvbmrjx82p563"); // 	del.y = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("3ksfb9uz6ssvxevjyvfdhcx15"); //         perp.x = dumb[0].y - dumb[3].y;
UNSUPPORTED("5qy02hcm7w3srhbws1saebqfk"); //         perp.y = dumb[3].x - dumb[0].x;
UNSUPPORTED("dkis85cdygy51zdkxp4m6zrfn"); // 	l_perp = LEN(perp.x, perp.y);
UNSUPPORTED("60ix8gm4odvpl4njysex8efr4"); // 	xstep = GD_nodesep(g->root);
UNSUPPORTED("9lo8r2t8xlvkucmcxj6r7zcjc"); // 	dx = xstep * (e_cnt - 1) / 2;
UNSUPPORTED("29kynt0cc7vrimccs1typen4c"); // 	dumb[1].x = dumb[0].x + (dx * perp.x) / l_perp;
UNSUPPORTED("8ffy5d6eokrgqxbbzlr3oeo7w"); // 	dumb[1].y = dumb[0].y + (dx * perp.y) / l_perp;
UNSUPPORTED("5ss1wkhlrlbzhjmgex5g5td0j"); // 	dumb[2].x = dumb[3].x + (dx * perp.x) / l_perp;
UNSUPPORTED("78hu4vl7uqaa2xjxh966lsl2z"); // 	dumb[2].y = dumb[3].y + (dx * perp.y) / l_perp;
UNSUPPORTED("czezd6edgg6prqa40qn4cjk99"); // 	del.x = -xstep * perp.x / l_perp;
UNSUPPORTED("brlhwaq7o7jlirlmbgsluegqc"); // 	del.y = -xstep * perp.y / l_perp;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("tf4qi3e2hsjxi603z57w6hx6"); //     for (i = 0; i < e_cnt; i++) {
UNSUPPORTED("ezgc9yof65sv59h9bc3xaarqc"); // 	e0 = edges[i];
UNSUPPORTED("cdinc4vpag193dvamw97pjop"); // 	if (aghead(e0) == head) {
UNSUPPORTED("70bzwaan8xb1eymztyzg8db4z"); // 	    p = dumb[0];
UNSUPPORTED("7s7bzu6vkika1vowxe90pn8jl"); // 	    q = dumb[3];
UNSUPPORTED("xt68cvhca5ecivsf28r6480k"); // 	    for (j = 0; j < 4; j++) {
UNSUPPORTED("euip77qbbydsz8d5amnoqz2wp"); // 		dumber[j] = dumb[j];
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("65qac2gqwej8ehvl0kr0azgoc"); // 	    p = dumb[3];
UNSUPPORTED("1p3ys1kne72eu96ymhhd08ki9"); // 	    q = dumb[0];
UNSUPPORTED("xt68cvhca5ecivsf28r6480k"); // 	    for (j = 0; j < 4; j++) {
UNSUPPORTED("cl6iic4z87ms6sxw112uwa8iq"); // 		dumber[3 - j] = dumb[j];
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("duxo2x55p2xtgyt3bec5dikrr"); // 	if (et == (3 << 1)) {
UNSUPPORTED("3cuaxa5hpxf783bmxnedboxcr"); // 	    Ppoint_t pts[4];
UNSUPPORTED("ey5aedx85s5fe7o31ioqdfot3"); // 	    Ppolyline_t spl, line;
UNSUPPORTED("8h359s121i98xp9zlr35vrgo9"); // 	    line.pn = 4;
UNSUPPORTED("57e37c2h1b5chgu6si6xi0cp4"); // 	    line.ps = pts;
UNSUPPORTED("phcktpjx2242lnn14blec2nu"); // 	    for (j=0; j < 4; j++) {
UNSUPPORTED("5tjkl4ul3rus6dd6xt7wfhv9a"); // 		pts[j] = dumber[j];
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5uszm2xlyusuekyfgopt358b8"); // 	    make_polyline (line, &spl);
UNSUPPORTED("4g67nliqr55eodkgwfdxbw87k"); // 	    clip_and_install(e0, aghead(e0), spl.ps, spl.pn, sinfo);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("cs4gj80s0cjsf32zlavo9gg0q"); // 	    clip_and_install(e0, aghead(e0), dumber, 4, sinfo);
UNSUPPORTED("9g4169ket42x6bl41jrpu9vvm"); // 	addEdgeLabels(g, e0, p, q);
UNSUPPORTED("8yg135oq3whkc9jbibbmn6v6a"); // 	dumb[1].x += del.x;
UNSUPPORTED("9sw7189ksprjtdn4dlniux8z4"); // 	dumb[1].y += del.y;
UNSUPPORTED("81xmo1my3dst3f72w8v2o3xb6"); // 	dumb[2].x += del.x;
UNSUPPORTED("bba6iln00idzhk4wan9c8n5ja"); // 	dumb[2].y += del.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
