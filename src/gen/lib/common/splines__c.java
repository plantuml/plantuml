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
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.arrows__c.arrowEndClip;
import static gen.lib.common.arrows__c.arrowStartClip;
import static gen.lib.common.arrows__c.arrow_flags;
import static gen.lib.common.emit__c.update_bb_bz;
import static gen.lib.common.shapes__c.resolvePort;
import static gen.lib.common.utils__c.Bezier;
import static gen.lib.common.utils__c.dotneato_closest;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ABS;
import static smetana.core.Macro.ALLOC_ST_bezier;
import static smetana.core.Macro.APPROXEQPT;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MILLIPOINT;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_shape;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.NOTI;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_inside_t;
import h.ST_path;
import h.ST_pathend_t;
import h.ST_pointf;
import h.ST_splineInfo;
import h.ST_splines;
import h.ST_textlabel_t;
import smetana.core.CFunction;
import smetana.core.MutableDouble;
import smetana.core.__ptr__;

public class splines__c {
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




//3 6izm0fbkejw7odmiw4zaw1ycp
// static void arrow_clip(edge_t * fe, node_t * hn, 	   pointf * ps, int *startp, int *endp, 	   bezier * spl, splineInfo * info) 
public static void arrow_clip(ST_Agedge_s fe, ST_Agnode_s hn, ST_pointf.Array ps, int startp[], int endp[], ST_bezier spl, ST_splineInfo info) {
ENTERING("6izm0fbkejw7odmiw4zaw1ycp","arrow_clip");
try {
    ST_Agedge_s e;
    int i;
    boolean j;
    int sflag[] = new int[]{0};
    int eflag[] = new int[]{0};
    for (e = fe; ED_to_orig(e)!=null; e = ED_to_orig(e));
    if (info.ignoreSwap)
	j = false;
    else
	j = (Boolean) info.swapEnds.exe(e);
    arrow_flags(e, sflag, eflag);
    if ((Boolean) info.splineMerge.exe(hn))
	eflag[0] = 0;
    if ((Boolean) info.splineMerge.exe(agtail(fe)))
	sflag[0] = 0;
    /* swap the two ends */
    if (j) {
	i = sflag[0];
	sflag[0] = eflag[0];
	eflag[0] = i;
    }
    if (info.isOrtho) {
UNSUPPORTED("7a3lmojyfh13d6shkviuogx2c"); // 	if (eflag || sflag)
UNSUPPORTED("dzbrwr2ulubtjkbd8j2o4yyov"); // 	    arrowOrthoClip(e, ps, *startp, *endp, spl, sflag, eflag);
    }
    else {
	if (sflag[0]!=0)
		startp[0] =
		arrowStartClip(e, ps, startp[0], endp[0], spl, sflag[0]);
	if (eflag[0]!=0)
	    endp[0] =
		arrowEndClip(e, ps, startp[0], endp[0], spl, eflag[0]);
    }
} finally {
LEAVING("6izm0fbkejw7odmiw4zaw1ycp","arrow_clip");
}
}




//3 q4t1ywnk3wm1vyh5seoj7xye
// void bezier_clip(inside_t * inside_context, 		 boolean(*inside) (inside_t * inside_context, pointf p), 		 pointf * sp, boolean left_inside) 
public static void bezier_clip(__ptr__ inside_context, __ptr__ inside, ST_pointf.Array sp, boolean left_inside) {
ENTERING("q4t1ywnk3wm1vyh5seoj7xye","bezier_clip");
try {
    final ST_pointf.Array seg = new ST_pointf.Array( 4);
    final ST_pointf.Array best = new ST_pointf.Array( 4);
    final ST_pointf pt = new ST_pointf(), opt = new ST_pointf();
    __ptr__ left, right;
    final MutableDouble low = new MutableDouble(0), high = new MutableDouble(0);
    double t;
    MutableDouble idir, odir;
    boolean found;
    int i;
    if (left_inside) {
	left = null;
	right = seg.asPtr();
	pt.___(sp.plus(0).getStruct());
	idir = low;
	odir = high;
    } else {
	left = seg.asPtr();
	right = null;
	pt.___(sp.plus(3).getStruct());
	idir = high;
	odir = low;
    }
    found = false;
    low.setValue(0.0);
    high.setValue(1.0);
    do {
	opt.___(pt);
	t = (high.getValue() + low.getValue()) / 2.0;
	pt.___(Bezier(sp, 3, t, left, right));
	if ((Boolean) ((CFunction)inside).exe(inside_context, pt)) {
	    idir.setValue(t);
	} else {
	    for (i = 0; i < 4; i++)
		best.plus(i).setStruct(seg.plus(i).getStruct());
	    found = NOT(false);
	    odir.setValue(t);
	}
    } while (ABS(opt.x - pt.x) > .5 || ABS(opt.y - pt.y) > .5);
    if (found)
	for (i = 0; i < 4; i++)
	    sp.plus(i).setStruct(best.plus(i).getStruct());
    else
	for (i = 0; i < 4; i++)
	    sp.plus(i).setStruct(seg.plus(i).getStruct());
} finally {
LEAVING("q4t1ywnk3wm1vyh5seoj7xye","bezier_clip");
}
}




//3 1fjkj1ydhtlf13pqj5r041orq
// static void shape_clip0(inside_t * inside_context, node_t * n, pointf curve[4], 	    boolean left_inside) 
public static void shape_clip0(__ptr__ inside_context, ST_Agnode_s n, ST_pointf.Array curve, boolean left_inside) {
ENTERING("1fjkj1ydhtlf13pqj5r041orq","shape_clip0");
try {
    int i;
    double save_real_size;
    final ST_pointf.Array c = new ST_pointf.Array( 4);
    save_real_size = ND_rw(n);
    for (i = 0; i < 4; i++) {
	c.plus(i).setDouble("x", curve.get(i).x - ND_coord(n).x);
	c.plus(i).setDouble("y", curve.get(i).y - ND_coord(n).y);
    }
    bezier_clip(inside_context, ND_shape(n).fns.insidefn, c,
		left_inside);
    for (i = 0; i < 4; i++) {
	curve.plus(i).setDouble("x", c.get(i).x + ND_coord(n).x);
	curve.plus(i).setDouble("y", c.get(i).y + ND_coord(n).y);
    }
    ND_rw(n, save_real_size);
} finally {
LEAVING("1fjkj1ydhtlf13pqj5r041orq","shape_clip0");
}
}




//3 5m4auahepda0ug3e1o05gm0kf
// void shape_clip(node_t * n, pointf curve[4]) 
public static Object shape_clip(Object... arg) {
UNSUPPORTED("14nvh1wyusdizrbkaqswyz6a3"); // void shape_clip(node_t * n, pointf curve[4])
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9htbadryixdqglsfxbj66m3na"); //     double save_real_size;
UNSUPPORTED("9hp0vh92flvlgoaxmzp4h6uaq"); //     boolean left_inside;
UNSUPPORTED("a3jcleu2wok75rpalgfjt6tte"); //     pointf c;
UNSUPPORTED("114lbsab8twbq15luo36j31q2"); //     inside_t inside_context;
UNSUPPORTED("eua5l3utehs8yu615w4iusufv"); //     if (ND_shape(n) == NULL || ND_shape(n)->fns->insidefn == NULL)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("a8yyudcgedtaoi8ey9sje90ph"); //     inside_context.s.n = n;
UNSUPPORTED("3y9eccm2tmdyx34ew24hitfqq"); //     inside_context.s.bp = NULL;
UNSUPPORTED("27aelctxo5teujbhkeu9x73hp"); //     save_real_size = ND_rw(n);
UNSUPPORTED("21zlezol1pqversb7b7rso1hl"); //     c.x = curve[0].x - ND_coord(n).x;
UNSUPPORTED("dxn2z9gn2x96x83fwcba9bdme"); //     c.y = curve[0].y - ND_coord(n).y;
UNSUPPORTED("5o23jq82y9dhpry5f9u9umk5d"); //     left_inside = ND_shape(n)->fns->insidefn(&inside_context, c);
UNSUPPORTED("9q38fqg00ynddr6952dvaz6n3"); //     ND_rw(n) = save_real_size;
UNSUPPORTED("w5x58kft1tdd1d2ae83yfrej"); //     shape_clip0(&inside_context, n, curve, left_inside);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bdirexg1qdtophlh0ofjvsmj7
// bezier *new_spline(edge_t * e, int sz) 
public static ST_bezier new_spline(ST_Agedge_s e, int sz) {
ENTERING("bdirexg1qdtophlh0ofjvsmj7","new_spline");
try {
    ST_bezier rv;
    while (ED_edge_type(e) != 0)
	e = ED_to_orig(e);
    if (ED_spl(e) == null)
	ED_spl(e, new ST_splines());
    ED_spl(e).list = ALLOC_ST_bezier(ED_spl(e).size + 1, ED_spl(e).list);
    rv = (ST_bezier) ED_spl(e).list.plus(ED_spl(e).size).getPtr();
    ED_spl(e).setInt("size", ED_spl(e).size+1);
    rv.list = new ST_pointf.Array(sz);
    rv.setInt("size", sz);
    rv.setInt("sflag", 0);
    rv.setInt("eflag", 0);
    rv.sp.setDouble("x", 0);
    rv.sp.setDouble("y", 0);
    rv.ep.setDouble("x", 0);
    rv.ep.setDouble("y", 0);
    return rv;
} finally {
LEAVING("bdirexg1qdtophlh0ofjvsmj7","new_spline");
}
}




//3 duednxyuvf6xrff752uuv620f
// void clip_and_install(edge_t * fe, node_t * hn, pointf * ps, int pn, 		 splineInfo * info) 
public static void clip_and_install(ST_Agedge_s fe, ST_Agnode_s hn, ST_pointf.Array ps, int pn, ST_splineInfo info) {
ENTERING("duednxyuvf6xrff752uuv620f","clip_and_install");
try {
    final ST_pointf p2 = new ST_pointf();
    ST_bezier newspl;
    ST_Agnode_s tn;
    int start[] = new int[] {0};
    int end[] = new int[] {0};
    int i, clipTail=0, clipHead=0;
    ST_Agraph_s g;
    ST_Agedge_s orig;
    ST_boxf tbox=null, hbox=null;
    final ST_inside_t inside_context = new ST_inside_t();
    tn = agtail(fe);
    g = agraphof(tn);
    newspl = new_spline(fe, pn);
    for (orig = fe; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
    /* may be a reversed flat edge */
    if (N(info.ignoreSwap) && (ND_rank(tn) == ND_rank(hn)) && (ND_order(tn) > ND_order(hn))) {
	ST_Agnode_s tmp;
	tmp = hn;
	hn = tn;
	tn = tmp;
    }
    if (EQ(tn, agtail(orig))) {
	clipTail = ED_tail_port(orig).clip;
	clipHead = ED_head_port(orig).clip;
	tbox = (ST_boxf) ED_tail_port(orig).bp;
	hbox = (ST_boxf) ED_head_port(orig).bp;
    }
    else { /* fe and orig are reversed */
 	clipTail = ED_head_port(orig).clip;
 	clipHead = ED_tail_port(orig).clip;
 	hbox = (ST_boxf) ED_tail_port(orig).bp;
 	tbox = (ST_boxf) ED_head_port(orig).bp;
    }
    /* spline may be interior to node */
    if(clipHead!=0 && ND_shape(tn)!=null && ND_shape(tn).fns.insidefn!=null) {
	inside_context.setPtr("s.n", tn);
	inside_context.setPtr("s.bp", tbox);
	for (start[0] = 0; start[0] < pn - 4; start[0] += 3) {
	    p2.setDouble("x", ps.get(start[0] + 3).x - ND_coord(tn).x);
	    p2.setDouble("y", ps.get(start[0] + 3).y - ND_coord(tn).y);
	    if (((Boolean)ND_shape(tn).fns.insidefn.exe(inside_context, p2)) == false)
		break;
	}
	shape_clip0(inside_context, tn, ps.plus(start[0]), NOT(false));
    } else
	start[0] = 0;
    if(clipHead!=0 && ND_shape(hn)!=null && ND_shape(hn).fns.insidefn!=null) {
	inside_context.setPtr("s.n", hn);
	inside_context.setPtr("s.bp", hbox);
	for (end[0] = pn - 4; end[0] > 0; end[0] -= 3) {
	    p2.setDouble("x", ps.get(end[0]).x - ND_coord(hn).x);
	    p2.setDouble("y", ps.get(end[0]).y - ND_coord(hn).y);
	    if (((Boolean)ND_shape(hn).fns.insidefn.exe(inside_context, p2)) == false)
		break;
	}
	shape_clip0(inside_context, hn, ps.plus(end[0]), false);
    } else
	end[0] = pn - 4;
    for (; start[0] < pn - 4; start[0] += 3) 
	if (N(APPROXEQPT(ps.plus(start[0]).getPtr(), ps.plus(start[0] + 3).getPtr(), MILLIPOINT)))
	    break;
    for (; end[0] > 0; end[0] -= 3)
	if (N(APPROXEQPT(ps.plus(end[0]).getPtr(), ps.plus(end[0] + 3).getPtr(), MILLIPOINT)))
	    break;
   arrow_clip(fe, hn, ps, start, end, newspl, info);
    for (i = start[0]; i < end[0] + 4; ) {
	final ST_pointf.Array cp = new ST_pointf.Array( 4);
	newspl.list.get(i - start[0]).setStruct(ps.plus(i).getStruct());
	cp.plus(0).setStruct(ps.plus(i).getStruct());
	i++;
	if ( i >= end[0] + 4)
	    break;
	newspl.list.get(i - start[0]).setStruct(ps.plus(i).getStruct());
	cp.plus(1).setStruct(ps.plus(i).getStruct());
	i++;
	newspl.list.get(i - start[0]).setStruct(ps.plus(i).getStruct());
	cp.plus(2).setStruct(ps.plus(i).getStruct());
	i++;
	cp.plus(3).setStruct(ps.plus(i).getStruct());
	update_bb_bz(GD_bb(g), cp);
    }
    newspl.setInt("size", end[0] - start[0] + 4);
} finally {
LEAVING("duednxyuvf6xrff752uuv620f","clip_and_install");
}
}




//3 25ndy15kghfrogsv0b0o0xkgv
// static double  conc_slope(node_t* n) 
public static double conc_slope(ST_Agnode_s n) {
ENTERING("25ndy15kghfrogsv0b0o0xkgv","conc_slope");
try {
 UNSUPPORTED("e388y3vtrp8f6spgh9q4wx37w"); // static double 
UNSUPPORTED("4yxpid2dxvb387487trn1umlw"); // conc_slope(node_t* n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9669xuley9dxylr00ex9kbzg7"); //     double s_in, s_out, m_in, m_out;
UNSUPPORTED("wfd0ht8utdwwqctf47l4dtrz"); //     int cnt_in, cnt_out;
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("apjf2mf9d7qj0eo9o2x5yli2e"); //     s_in = s_out = 0.0;
UNSUPPORTED("7mc6shwmvz25mz9inwj97lqk6"); //     for (cnt_in = 0; (e = ND_in(n).list[cnt_in]); cnt_in++)
UNSUPPORTED("cb1h5cx7oxhtdkm5l0k6qrx2z"); // 	s_in += ND_coord(agtail(e)).x;
UNSUPPORTED("hjfqfqmtdqdrp9z80ebrpthm"); //     for (cnt_out = 0; (e = ND_out(n).list[cnt_out]); cnt_out++)
UNSUPPORTED("2iidrr9ljv8ap9s2g6gj3q1o3"); // 	s_out += ND_coord(aghead(e)).x;
UNSUPPORTED("2yeio9xc9oorju7qqnhilwujx"); //     p.x = ND_coord(n).x - (s_in / cnt_in);
UNSUPPORTED("87jzl9isj7w9kgyr05inw33s5"); //     p.y = ND_coord(n).y - ND_coord(agtail(ND_in(n).list[0])).y;
UNSUPPORTED("6y2pc9af2xxdqajbpykvca9eg"); //     m_in = atan2(p.y, p.x);
UNSUPPORTED("ruwz5svpk33ucfgs4wx0xolm"); //     p.x = (s_out / cnt_out) - ND_coord(n).x;
UNSUPPORTED("8vif8c37lbo7ww4vwfrcxgpmr"); //     p.y = ND_coord(aghead(ND_out(n).list[0])).y - ND_coord(n).y;
UNSUPPORTED("ez8z3gbteryfhktbqkwmzhhzs"); //     m_out = atan2(p.y, p.x);
UNSUPPORTED("ej1ftaglexa47x955elb88yh2"); //     return ((m_in + m_out) / 2.0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("25ndy15kghfrogsv0b0o0xkgv","conc_slope");
}
}




//3 egq4f4tmy1dhyj6jpj92r7xhu
// void add_box(path * P, boxf b) 
public static void add_box(ST_path P, final ST_boxf b) {
// WARNING!! STRUCT
add_box_w_(P, b.copy());
}
private static void add_box_w_(ST_path P, final ST_boxf b) {
ENTERING("egq4f4tmy1dhyj6jpj92r7xhu","add_box");
try {
    if (b.LL.x < b.UR.x && b.LL.y < b.UR.y)
    {
	P.boxes[P.nbox].setStruct(b);
	P.nbox = P.nbox+1;
	}
} finally {
LEAVING("egq4f4tmy1dhyj6jpj92r7xhu","add_box");
}
}




//3 7pc43ifcw5g56449d101qf590
// void beginpath(path * P, edge_t * e, int et, pathend_t * endp, boolean merge) 
public static void beginpath(ST_path P, ST_Agedge_s e, int et, ST_pathend_t endp, boolean merge) {
ENTERING("7pc43ifcw5g56449d101qf590","beginpath");
try {
    int side, mask;
    ST_Agnode_s n;
    CFunction pboxfn;
    n = agtail(e);
    if (ED_tail_port(e).dyna!=0)
	ED_tail_port(e, resolvePort(agtail(e), aghead(e), ED_tail_port(e)));
    if (ND_shape(n)!=null)
	pboxfn = (CFunction) ND_shape(n).fns.pboxfn;
    else
	pboxfn = null;
    P.start.setStruct("p", add_pointf(ND_coord(n), (ST_pointf) ED_tail_port(e).p));
    if (merge) {
	/*P->start.theta = - M_PI / 2; */
	P.start.setDouble("theta", conc_slope(agtail(e)));
	P.start.constrained= NOTI(false);
    } else {
	if (ED_tail_port(e).constrained!=0) {
	    P.start.setDouble("theta", ED_tail_port(e).theta);
	    P.start.constrained= NOTI(false);
	} else
	    P.start.constrained= 0;
    }
    P.nbox = 0;
    P.setPtr("data", e);
    endp.setStruct("np", P.start.p);
    if ((et == 1) && (ND_node_type(n) == 0) && ((side = ED_tail_port(e).side)!=0)) {
UNSUPPORTED("a7lrhlfwr0y475aqjk6abhb3b"); // 	edge_t* orig;
UNSUPPORTED("ew7nyfe712nsiphifeztwxfop"); // 	boxf b0, b = endp->nb;
UNSUPPORTED("ait3wtnnvt134z2k87lvhq4ek"); // 	if (side & (1<<2)) {
UNSUPPORTED("1r4lctdj9z1ivlz3uqpcj1yzf"); // 	    endp->sidemask = (1<<2);
UNSUPPORTED("arq09sf82lsjuxwfkesprcrcv"); // 	    if (P->start.p.x < ND_coord(n).x) { /* go left */
UNSUPPORTED("bj4z8gwgs6j5fax8k6l3u6mv3"); // 		b0.LL.x = b.LL.x - 1;
UNSUPPORTED("54rmdm0xwy361tjs4aj6cv401"); // 		/* b0.LL.y = ND_coord(n).y + HT2(n); */
UNSUPPORTED("11ax5pxz4q2uh0nzsrs1qs7ck"); // 		b0.LL.y = P->start.p.y;
UNSUPPORTED("5xsapgq04e1hslq2835500q6k"); // 		b0.UR.x = b.UR.x;
UNSUPPORTED("9ro8mx52kgsoogvlgfubgn4p0"); // 		b0.UR.y = ND_coord(n).y + (ND_ht(n)/2) + GD_ranksep(agraphof(n))/2;
UNSUPPORTED("6p2nw1nh0qwn5ro3dltmd6w6c"); // 		b.UR.x = ND_coord(n).x - ND_lw(n) - (2-2);
UNSUPPORTED("czvxm3loj0won7ye2b3xrfbv4"); // 		b.UR.y = b0.LL.y;
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("w9wsmby4dawn9npux1jrd9gl"); // 		b.LL.x -= 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("7sk0msbospwpwupzwpu8v87qt"); // 		b0.LL.x = b.LL.x;
UNSUPPORTED("11ax5pxz4q2uh0nzsrs1qs7ck"); // 		b0.LL.y = P->start.p.y;
UNSUPPORTED("54rmdm0xwy361tjs4aj6cv401"); // 		/* b0.LL.y = ND_coord(n).y + HT2(n); */
UNSUPPORTED("4e5ydpfmxn1wuhnp78arn3f9x"); // 		b0.UR.x = b.UR.x+1;
UNSUPPORTED("9ro8mx52kgsoogvlgfubgn4p0"); // 		b0.UR.y = ND_coord(n).y + (ND_ht(n)/2) + GD_ranksep(agraphof(n))/2;
UNSUPPORTED("3f26r03ydc7aq52vcqpgxawgy"); // 		b.LL.x = ND_coord(n).x + ND_rw(n) + (2-2);
UNSUPPORTED("czvxm3loj0won7ye2b3xrfbv4"); // 		b.UR.y = b0.LL.y;
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("bqk56pohk8hpgn91lv4m2zkv0"); // 		b.UR.x += 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("196ta4n5nsqizd83y6oo7z8a2"); // 	    } 
UNSUPPORTED("b7lioq6g7moe5otds46c8hrc"); // 	    P->start.p.y += 1;
UNSUPPORTED("4v7mmisc358r5tpq14qp4dx0f"); // 	    endp->boxn = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3s4re3z7asydnnotdylt94t1d"); // 	else if (side & (1<<0)) {
UNSUPPORTED("auefgwb39x5hzqqc9b1zgl239"); // 	    endp->sidemask = (1<<0);
UNSUPPORTED("ax7mx0s11g0pgcgb8iopcu82a"); // 	    b.UR.y = MAX(b.UR.y,P->start.p.y);
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("8pyl2559euuaxrntsyzj1ve8w"); // 	    P->start.p.y -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bn3pwhefgp4zdatx3g60lj0ou"); // 	else if (side & (1<<3)) {
UNSUPPORTED("2lmjkw07sr4x9a3xxrcb3yj07"); // 	    endp->sidemask = (1<<3);
UNSUPPORTED("bmdw7h7pzwkfbuzhxnsh4vbsm"); // 	    b.UR.x = P->start.p.x;
UNSUPPORTED("c86scga1j3ar95pgqvemnzrui"); // 	    b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("cdhdxsyg42tregieb2l7kz8n"); // 	    b.UR.y = P->start.p.y;
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("celmm9njwdxhpvd56zon98hrr"); // 	    P->start.p.x -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("8kgb5ztvt4yv5h0nezr0q6n3z"); // 	    endp->sidemask = (1<<1);
UNSUPPORTED("cysdxceleujmu3rckrhibxaqd"); // 	    b.LL.x = P->start.p.x;
UNSUPPORTED("c86scga1j3ar95pgqvemnzrui"); // 	    b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("cdhdxsyg42tregieb2l7kz8n"); // 	    b.UR.y = P->start.p.y;
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("1n8o29xgguq4cce4rf04o5rke"); // 	    P->start.p.x += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4gatpb3u0rq9nikm6rjtejp85"); // 	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
UNSUPPORTED("askl6l2rq6b2bznfxj7ydvz5t"); // 	if (n == agtail(orig))
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    if ((et == 2) && ((side = ED_tail_port(e).side)!=0)) {
UNSUPPORTED("ew7nyfe712nsiphifeztwxfop"); // 	boxf b0, b = endp->nb;
UNSUPPORTED("a7lrhlfwr0y475aqjk6abhb3b"); // 	edge_t* orig;
UNSUPPORTED("ait3wtnnvt134z2k87lvhq4ek"); // 	if (side & (1<<2)) {
UNSUPPORTED("d7fd91oymbo1kkxfqhtbe2jky"); // 	    b.LL.y = MIN(b.LL.y,P->start.p.y);
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("b7lioq6g7moe5otds46c8hrc"); // 	    P->start.p.y += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3s4re3z7asydnnotdylt94t1d"); // 	else if (side & (1<<0)) {
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("7vjialx9rln6cj2y0ni5nc2gi"); // 		b0.UR.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("4e5ydpfmxn1wuhnp78arn3f9x"); // 		b0.UR.x = b.UR.x+1;
UNSUPPORTED("1zpea73m3d4hdldoc5sypz1ag"); // 		b0.LL.x = P->start.p.x;
UNSUPPORTED("esamvv08qn005uqko6caft2u"); // 		b0.LL.y = b0.UR.y - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("3f26r03ydc7aq52vcqpgxawgy"); // 		b.LL.x = ND_coord(n).x + ND_rw(n) + (2-2);
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("bqk56pohk8hpgn91lv4m2zkv0"); // 		b.UR.x += 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("93chrd1duv0atudbvr439u7t4"); // 		endp->boxn = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8yftboq798vpnzuxkx6yuea18"); // 		b.UR.y = MAX(b.UR.y,P->start.p.y);
UNSUPPORTED("at4jfrag6jtwm7rxu8p4p8d46"); // 		endp->boxes[0] = b;
UNSUPPORTED("ev1muhahxwb1cntbhsb3c9aid"); // 		endp->boxn = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8pyl2559euuaxrntsyzj1ve8w"); // 	    P->start.p.y -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bn3pwhefgp4zdatx3g60lj0ou"); // 	else if (side & (1<<3)) {
UNSUPPORTED("bihp3ojpe2nsmh297nosihedn"); // 	    b.UR.x = P->start.p.x+1;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("afqhibyplfg1fftlkny8jq78t"); // 		b.LL.y = P->start.p.y-1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("4no3qn8v4vx6rk2in60hgr8w6"); // 		b.UR.y = P->start.p.y+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("celmm9njwdxhpvd56zon98hrr"); // 	    P->start.p.x -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("cysdxceleujmu3rckrhibxaqd"); // 	    b.LL.x = P->start.p.x;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("5oh26jb6vz012qke7865hz5h7"); // 		b.LL.y = P->start.p.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("4no3qn8v4vx6rk2in60hgr8w6"); // 		b.UR.y = P->start.p.y+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("1n8o29xgguq4cce4rf04o5rke"); // 	    P->start.p.x += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4gatpb3u0rq9nikm6rjtejp85"); // 	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
UNSUPPORTED("askl6l2rq6b2bznfxj7ydvz5t"); // 	if (n == agtail(orig))
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
UNSUPPORTED("8jqn3kj2hrrlcifbw3x9sf6qu"); // 	endp->sidemask = side;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    if (et == 1) side = (1<<0);
    else side = endp.sidemask;  /* for flat edges */
    if (pboxfn!=null
	&& (mask = (Integer) pboxfn.exe(n, ED_tail_port(e), side, endp.boxes[0], endp.boxn))!=0)
UNSUPPORTED("ex9kjvshm19zbu9vqonk1avd8"); // 	endp->sidemask = mask;
    else {
    endp.boxes[0].setStruct(endp.nb);
	endp.setInt("boxn", 1);
	switch (et) {
	case 8:
	/* moving the box UR.y by + 1 avoids colinearity between
	   port point and box that confuses Proutespline().  it's
	   a bug in Proutespline() but this is the easiest fix. */
UNSUPPORTED("9rnob8jdqqdjwzanv53yxc47u"); // 	    assert(0);  /* at present, we don't use beginpath for selfedges */
UNSUPPORTED("46vb5zg9vm9n0q21g53nj66v3"); // 	    endp->boxes[0].UR.y = P->start.p.y - 1;
UNSUPPORTED("auefgwb39x5hzqqc9b1zgl239"); // 	    endp->sidemask = (1<<0);
	    break;
	case 2:
	    if (endp.sidemask == (1<<2))
		((ST_boxf)endp.boxes[0]).LL.y = P.start.p.y;
	    else
	    	((ST_boxf)endp.boxes[0]).UR.y = P.start.p.y;
	    break;
	case 1:
	    ((ST_boxf)(endp).boxes[0]).UR.y = P.start.p.y;
	    endp.setInt("sidemask", (1<<0));
	    P.start.p.setDouble("y", P.start.p.y - 1);
	    break;
	}    
    }    
} finally {
LEAVING("7pc43ifcw5g56449d101qf590","beginpath");
}
}




//3 79dr5om55xs3n5lgai1sf58vu
// void endpath(path * P, edge_t * e, int et, pathend_t * endp, boolean merge) 
public static void endpath(ST_path P, ST_Agedge_s e, int et, ST_pathend_t endp, boolean merge) {
ENTERING("79dr5om55xs3n5lgai1sf58vu","endpath");
try {
    int side, mask;
    ST_Agnode_s n;
    CFunction pboxfn;
    n = aghead(e);
    if (ED_head_port(e).dyna!=0) 
UNSUPPORTED("9brhx94sjudx3jtzrnwa60x8"); // 	ED_head_port(e) = resolvePort(aghead(e), agtail(e), &ED_head_port(e));
    if (ND_shape(n)!=null)
	pboxfn = (CFunction) ND_shape(n).fns.pboxfn;
    else
	pboxfn = null;
    P.end.setStruct("p", add_pointf(ND_coord(n), (ST_pointf) ED_head_port(e).p));
    if (merge) {
UNSUPPORTED("cproejwusj67kuugolh6tbkwz"); // 	/*P->end.theta = M_PI / 2; */
UNSUPPORTED("65vhfvz1d1tub3f85tdsgg2g5"); // 	P->end.theta = conc_slope(aghead(e)) + M_PI;
UNSUPPORTED("du4hwt6pjf3bmkvowssm7b0uo"); // 	assert(P->end.theta < 2 * M_PI);
UNSUPPORTED("2w0c22i5xgcch77xd9jg104nw"); // 	P->end.constrained = NOT(0);
    } else {
	if (ED_head_port(e).constrained!=0) {
	    P.end.setDouble("theta", ED_head_port(e).theta);
	    P.end.setInt("constrained", 1);
	} else
	    P.end.setInt("constrained", 0);
    }
    endp.setStruct("np", P.end.p);
    if ((et == 1) && (ND_node_type(n) == 0) && ((side = ED_head_port(e).side)!=0)) {
UNSUPPORTED("a7lrhlfwr0y475aqjk6abhb3b"); // 	edge_t* orig;
UNSUPPORTED("ew7nyfe712nsiphifeztwxfop"); // 	boxf b0, b = endp->nb;
UNSUPPORTED("ait3wtnnvt134z2k87lvhq4ek"); // 	if (side & (1<<2)) {
UNSUPPORTED("1r4lctdj9z1ivlz3uqpcj1yzf"); // 	    endp->sidemask = (1<<2);
UNSUPPORTED("cropv6s2edu614uzt364nepfo"); // 	    b.LL.y = MIN(b.LL.y,P->end.p.y);
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("c91rvfjkunah0qffpuo47eshu"); // 	    P->end.p.y += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3s4re3z7asydnnotdylt94t1d"); // 	else if (side & (1<<0)) {
UNSUPPORTED("auefgwb39x5hzqqc9b1zgl239"); // 	    endp->sidemask = (1<<0);
UNSUPPORTED("4tlqpclu7x0szo1rszndqau0d"); // 	    if (P->end.p.x < ND_coord(n).x) { /* go left */
UNSUPPORTED("80ypgtfgfrgq8j7whkaueouh5"); // 		b0.LL.x = b.LL.x-1;
UNSUPPORTED("4ikkdf5k4ubwp4ou51rth0q41"); // 		/* b0.UR.y = ND_coord(n).y - HT2(n); */
UNSUPPORTED("baysgwgvs09ywaufn74gq6m0a"); // 		b0.UR.y = P->end.p.y;
UNSUPPORTED("5xsapgq04e1hslq2835500q6k"); // 		b0.UR.x = b.UR.x;
UNSUPPORTED("7ut9yqcephghob5a3yo8af293"); // 		b0.LL.y = ND_coord(n).y - (ND_ht(n)/2) - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("6p2nw1nh0qwn5ro3dltmd6w6c"); // 		b.UR.x = ND_coord(n).x - ND_lw(n) - (2-2);
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("w9wsmby4dawn9npux1jrd9gl"); // 		b.LL.x -= 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("7sk0msbospwpwupzwpu8v87qt"); // 		b0.LL.x = b.LL.x;
UNSUPPORTED("baysgwgvs09ywaufn74gq6m0a"); // 		b0.UR.y = P->end.p.y;
UNSUPPORTED("4ikkdf5k4ubwp4ou51rth0q41"); // 		/* b0.UR.y = ND_coord(n).y - HT2(n); */
UNSUPPORTED("4e5ydpfmxn1wuhnp78arn3f9x"); // 		b0.UR.x = b.UR.x+1;
UNSUPPORTED("7ut9yqcephghob5a3yo8af293"); // 		b0.LL.y = ND_coord(n).y - (ND_ht(n)/2) - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("3f26r03ydc7aq52vcqpgxawgy"); // 		b.LL.x = ND_coord(n).x + ND_rw(n) + (2-2);
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("bqk56pohk8hpgn91lv4m2zkv0"); // 		b.UR.x += 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("196ta4n5nsqizd83y6oo7z8a2"); // 	    } 
UNSUPPORTED("4v7mmisc358r5tpq14qp4dx0f"); // 	    endp->boxn = 2;
UNSUPPORTED("6kjd8mut2dn2xv1k1zr63qp0s"); // 	    P->end.p.y -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bn3pwhefgp4zdatx3g60lj0ou"); // 	else if (side & (1<<3)) {
UNSUPPORTED("2lmjkw07sr4x9a3xxrcb3yj07"); // 	    endp->sidemask = (1<<3);
UNSUPPORTED("4e2bsroer72trfy5dl5k8f5s8"); // 	    b.UR.x = P->end.p.x;
UNSUPPORTED("3rsswd4vcybmrbhoqt0aldqds"); // 	    b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("7m86tfoixpamdnl1ywyaz9uzy"); // 	    b.LL.y = P->end.p.y;
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("5j92wv3nt0b7hnlf3ktengoom"); // 	    P->end.p.x -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("8kgb5ztvt4yv5h0nezr0q6n3z"); // 	    endp->sidemask = (1<<1);
UNSUPPORTED("2upa323l3o3equsdn1v13nj0q"); // 	    b.LL.x = P->end.p.x;
UNSUPPORTED("3rsswd4vcybmrbhoqt0aldqds"); // 	    b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("7m86tfoixpamdnl1ywyaz9uzy"); // 	    b.LL.y = P->end.p.y;
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("44vy3z49e2oo6613r15tcgn8h"); // 	    P->end.p.x += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4gatpb3u0rq9nikm6rjtejp85"); // 	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
UNSUPPORTED("e8cujr3gqet8mj2n5h5jfogm1"); // 	if (n == aghead(orig))
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
UNSUPPORTED("8jqn3kj2hrrlcifbw3x9sf6qu"); // 	endp->sidemask = side;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    if ((et == 2) && ((side = ED_head_port(e).side)!=0)) {
UNSUPPORTED("ew7nyfe712nsiphifeztwxfop"); // 	boxf b0, b = endp->nb;
UNSUPPORTED("a7lrhlfwr0y475aqjk6abhb3b"); // 	edge_t* orig;
UNSUPPORTED("ait3wtnnvt134z2k87lvhq4ek"); // 	if (side & (1<<2)) {
UNSUPPORTED("cropv6s2edu614uzt364nepfo"); // 	    b.LL.y = MIN(b.LL.y,P->end.p.y);
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("c91rvfjkunah0qffpuo47eshu"); // 	    P->end.p.y += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3s4re3z7asydnnotdylt94t1d"); // 	else if (side & (1<<0)) {
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("80ypgtfgfrgq8j7whkaueouh5"); // 		b0.LL.x = b.LL.x-1;
UNSUPPORTED("7vjialx9rln6cj2y0ni5nc2gi"); // 		b0.UR.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("e403abqgqxgss6h01127ebeil"); // 		b0.UR.x = P->end.p.x;
UNSUPPORTED("esamvv08qn005uqko6caft2u"); // 		b0.LL.y = b0.UR.y - GD_ranksep(agraphof(n))/2;
UNSUPPORTED("29fp8dba1xqbt5ire1m3oad6c"); // 		b.UR.x = ND_coord(n).x - ND_lw(n) - 2;
UNSUPPORTED("74mnpbjmyubjppjur4ngy4t5u"); // 		b.LL.y = b0.UR.y;
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("w9wsmby4dawn9npux1jrd9gl"); // 		b.LL.x -= 1;
UNSUPPORTED("2f8usay82b128dq0sk4aqzw3h"); // 		endp->boxes[0] = b0;
UNSUPPORTED("2diqdwueoy5oizl5kmbz6uyi8"); // 		endp->boxes[1] = b;
UNSUPPORTED("93chrd1duv0atudbvr439u7t4"); // 		endp->boxn = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8yftboq798vpnzuxkx6yuea18"); // 		b.UR.y = MAX(b.UR.y,P->start.p.y);
UNSUPPORTED("at4jfrag6jtwm7rxu8p4p8d46"); // 		endp->boxes[0] = b;
UNSUPPORTED("ev1muhahxwb1cntbhsb3c9aid"); // 		endp->boxn = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6kjd8mut2dn2xv1k1zr63qp0s"); // 	    P->end.p.y -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bn3pwhefgp4zdatx3g60lj0ou"); // 	else if (side & (1<<3)) {
UNSUPPORTED("46ayak01kn7y7w3yaoreb6w1l"); // 	    b.UR.x = P->end.p.x+1;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("a3bb90cu4chg4dv4xfsx8r8ek"); // 		b.LL.y = P->end.p.y-1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("20q189zumqwpltcod94td3f"); // 		b.UR.y = P->end.p.y+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("5j92wv3nt0b7hnlf3ktengoom"); // 	    P->end.p.x -= 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("9tx1p6meq5zi4ce5essw11ikg"); // 	    b.LL.x = P->end.p.x-1;
UNSUPPORTED("6h0f9z7wklonn021j8ijd3b8m"); // 	    if (endp->sidemask == (1<<2)) {
UNSUPPORTED("a6wnwn2mc878a2wacqkmdefx7"); // 		b.UR.y = ND_coord(n).y + (ND_ht(n)/2);
UNSUPPORTED("a3bb90cu4chg4dv4xfsx8r8ek"); // 		b.LL.y = P->end.p.y-1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("1f4u492auf4ku7ik170e86iy3"); // 		b.LL.y = ND_coord(n).y - (ND_ht(n)/2);
UNSUPPORTED("181rv2y41gamwqbbccj0rnb57"); // 		b.UR.y = P->end.p.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("esv3oinoscr6zht0kce49o450"); // 	    endp->boxes[0] = b;
UNSUPPORTED("3hptqfzzuz4dlsc8ejk1ynxt9"); // 	    endp->boxn = 1;
UNSUPPORTED("44vy3z49e2oo6613r15tcgn8h"); // 	    P->end.p.x += 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4gatpb3u0rq9nikm6rjtejp85"); // 	for (orig = e; ED_edge_type(orig) != 0; orig = ED_to_orig(orig));
UNSUPPORTED("e8cujr3gqet8mj2n5h5jfogm1"); // 	if (n == aghead(orig))
UNSUPPORTED("2tw6ymudedo6qij3ux424ydsi"); // 	    ED_head_port(orig).clip = 0;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("dk49xvmby8949ngdmft4sgrox"); // 	    ED_tail_port(orig).clip = 0;
UNSUPPORTED("8jqn3kj2hrrlcifbw3x9sf6qu"); // 	endp->sidemask = side;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    if (et == 1) side = (1<<2);
    else side = endp.sidemask;  /* for flat edges */
    if (pboxfn!=null
	&& (mask = (Integer) pboxfn.exe(n, ED_head_port(e), side, endp.boxes[0], endp.boxn))!=0)
	endp.setInt("sidemask", mask);
    else {
    	endp.boxes[0].setStruct(endp.nb);
	endp.setInt("boxn", 1);
	switch (et) {
	case 8:
	    /* offset of -1 is symmetric w.r.t. beginpath() 
	     * FIXME: is any of this right?  what if self-edge 
	     * doesn't connect from BOTTOM to TOP??? */
UNSUPPORTED("bhkhf4i9pvxtxyka4sobszg33"); // 	    assert(0);  /* at present, we don't use endpath for selfedges */
UNSUPPORTED("db6vmvnse8bawy8qwct7l24u8"); // 	    endp->boxes[0].LL.y = P->end.p.y + 1;
UNSUPPORTED("1r4lctdj9z1ivlz3uqpcj1yzf"); // 	    endp->sidemask = (1<<2);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
	case 2:
	    if (endp.sidemask == (1<<2))
	    	endp.boxes[0].LL.y = P.end.p.y;
	    else
	    	endp.boxes[0].UR.y = P.end.p.y;
	    break;
	case 1:
		endp.boxes[0].LL.y = P.end.p.y;
	    endp.setInt("sidemask", (1<<2));
	    P.end.p.setDouble("y", P.end.p.y +1);
	    break;
	}
    }
} finally {
LEAVING("79dr5om55xs3n5lgai1sf58vu","endpath");
}
}




//3 3g7alj6eirl5b2hlhluiqvaax
// static int convert_sides_to_points(int tail_side, int head_side) 
public static int convert_sides_to_points(int tail_side, int head_side) {
ENTERING("3g7alj6eirl5b2hlhluiqvaax","convert_sides_to_points");
int vertices[] = new int[] {12,4,6,2,3,1,9,8};  //the cumulative side value of each node point
int i, tail_i, head_i;
int pair_a[][] = new int[][] {	    //array of possible node point pairs
{11,12,13,14,15,16,17,18},
{21,22,23,24,25,26,27,28},
{31,32,33,34,35,36,37,38},
{41,42,43,44,45,46,47,48},
{51,52,53,54,55,56,57,58},
{61,62,63,64,65,66,67,68},
{71,72,73,74,75,76,77,78},
{81,82,83,84,85,86,87,88}
};
try {
 tail_i = head_i = -1;
	for(i=0;i< 8; i++){
		if(head_side == vertices[i]){
			head_i = i;
			break;
		}
	}
	for(i=0;i< 8; i++){
		if(tail_side == vertices[i]){
			tail_i = i;
			break;
		}
	}
if( tail_i < 0 || head_i < 0)
  return 0;
else
  return pair_a[tail_i][head_i];
} finally {
LEAVING("3g7alj6eirl5b2hlhluiqvaax","convert_sides_to_points");
}
}




//3 7l37y1w97mt6n5pd9x5dzgwud
// static void selfBottom (edge_t* edges[], int ind, int cnt, 	double sizex, double stepy, splineInfo* sinfo)  
public static Object selfBottom(Object... arg) {
UNSUPPORTED("5mldqfen59kshqgaknayjc5ox"); // static void selfBottom (edge_t* edges[], int ind, int cnt,
UNSUPPORTED("e0472i5ngodtv68y0hdhq1azu"); // 	double sizex, double stepy, splineInfo* sinfo) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7a2vzpy4tpc2fpmuf12nhtfca"); //     pointf tp, hp, np;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("2131r3ibxv7drmcz6f2j5d9c2"); //     int i, sgn, point_pair;
UNSUPPORTED("de1bz9yfc9w49kc4vy1ge2ltd"); //     double hy, ty, stepx, dx, dy, width, height; 
UNSUPPORTED("cutkizwxyuykhmayeb60m22av"); //     pointf points[1000];
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("e3wy3x07xdsusfbgecfcqg5lj"); //     e = edges[ind];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("43yzlf5354g6qlugyzpmr745t"); //     stepx = (sizex / 2.) / cnt;
UNSUPPORTED("brakcbw9hvzlljogqwzlhgb0v"); //     stepx = MAX(stepx,2.);
UNSUPPORTED("dko3xt785e372nj0fiocjfas"); //     pointn = 0;
UNSUPPORTED("dqazhjgevh1spyg3xzwb3bcks"); //     np = ND_coord(n);
UNSUPPORTED("ehf9o80lfi02no07wz207kyp6"); //     tp = ED_tail_port(e).p;
UNSUPPORTED("f18822xrptoagri7001gamxwh"); //     tp.x += np.x;
UNSUPPORTED("pcmp8bdd8677mjvvef7kfh5y"); //     tp.y += np.y;
UNSUPPORTED("b4mfdkjjk3n78ssy4h80g5lc6"); //     hp = ED_head_port(e).p;
UNSUPPORTED("e7rhhgc42h5z6kvvnkz6wfn0r"); //     hp.x += np.x;
UNSUPPORTED("bisu3qji6rw3wu3srdv8vhrxb"); //     hp.y += np.y;
UNSUPPORTED("2c8kmvidaqx92wd2mq1ys6753"); //     if (tp.x >= hp.x) sgn = 1;
UNSUPPORTED("cvln1r5ffbp1z1sq0y6ago4og"); //     else sgn = -1;
UNSUPPORTED("7squuk10wt6xrbp24obpx41bw"); //     dy = ND_ht(n)/2., dx = 0.;
UNSUPPORTED("7sojr831wk2u8c86xerkjyojd"); //     // certain adjustments are required for some point_pairs in order to improve the 
UNSUPPORTED("byuachd2fjte06s7xwnbmxlcx"); //     // display of the edge path between them
UNSUPPORTED("eje36stfd9p7ulgo4qk6gjwvx"); //     point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
UNSUPPORTED("2qmvjd6iwnaqwop679caoaxnn"); //     switch(point_pair){
UNSUPPORTED("8c31t4u50f9yjnlb8ii84ts3w"); //       case 67:  sgn = -sgn;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("5vhsnixpf0pg2oz10ps2valyn"); //       default:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5n29oadzd6emvd2fwjisx6ovk"); //     ty = MIN(dy, 3*(tp.y + dy - np.y));
UNSUPPORTED("dly5hufg66dgb6zn5lqcerae1"); //     hy = MIN(dy, 3*(hp.y + dy - np.y));
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("a0u9ggni4r8gikqyyxf6wgaa5"); //         e = edges[ind++];
UNSUPPORTED("bgymnp4yekw8tzr70cnzzn9ez"); //         dy += stepy, ty += stepy, hy += stepy, dx += sgn*stepx;
UNSUPPORTED("8tkxpvgpxpilkes33cj73nr8o"); //         pointn = 0;
UNSUPPORTED("2j93ajzz3i9adm0syj177su98"); //         points[pointn++] = tp;
UNSUPPORTED("15uyub8ah85dmbdmc0lqgjqb"); //         points[pointn++] = pointfof(tp.x + dx, tp.y - ty / 3);
UNSUPPORTED("bh0lpazk6gpagl57bydccqkv4"); //         points[pointn++] = pointfof(tp.x + dx, np.y - dy);
UNSUPPORTED("381vppahpairjja0hahm7lktb"); //         points[pointn++] = pointfof((tp.x+hp.x)/2, np.y - dy);
UNSUPPORTED("n63wd0j09ndu0hiaxhwx7izb"); //         points[pointn++] = pointfof(hp.x - dx, np.y - dy);
UNSUPPORTED("dzdgwa3zfedg3kys9pd8mp5qm"); //         points[pointn++] = pointfof(hp.x - dx, hp.y - hy / 3);
UNSUPPORTED("6t0sueo9zyoccfzqit4c7pvcy"); //         points[pointn++] = hp;
UNSUPPORTED("6nhnbriaxn7zi0ab1z8bkbzd"); //         if (ED_label(e)) {
UNSUPPORTED("a7ea1ybpt7lv8fk1pc1outbs5"); // 	if (GD_flip(agraphof(agtail(e)))) {
UNSUPPORTED("7d83ym7h1stime4wbmifcx809"); //     	    width = ED_label(e)->dimen.y;
UNSUPPORTED("44m5sni7g3n6fnk6ca57u9dc2"); //     	    height = ED_label(e)->dimen.x;
UNSUPPORTED("s8koz5x85ytpnff1o94rlxqy"); //     	} else {
UNSUPPORTED("66vu2joy64r1yrkvp3oolz1ws"); //     	    width = ED_label(e)->dimen.x;
UNSUPPORTED("d6bobo1f6gxkxa2fffvmn41g0"); //     	    height = ED_label(e)->dimen.y;
UNSUPPORTED("klxoy56t7b20wxnwqm0qoofz"); //     	}
UNSUPPORTED("cot4bdvsbrav4yex2yesffgd9"); //     	ED_label(e)->pos.y = ND_coord(n).y - dy - height / 2.0;
UNSUPPORTED("9wg1yftg90g8jld2m2p5m31ro"); //     	ED_label(e)->pos.x = ND_coord(n).x;
UNSUPPORTED("7efx4yevu8176mmuqjtk4bfss"); //     	ED_label(e)->set = NOT(0);
UNSUPPORTED("13o3f1bpjm731ee8hpa8d3f5y"); //     	if (height > stepy)
UNSUPPORTED("alt1jvhdhimr8iltoxg7dycq1"); //     	    dy += height - stepy;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5fvid2bi7fy5jv5dyttfprpzj"); //         clip_and_install(e, aghead(e), points, pointn, sinfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2thwh4ase1jdq8ghhf0oqyql5
// static void selfTop (edge_t* edges[], int ind, int cnt, double sizex, double stepy,            splineInfo* sinfo)  
public static Object selfTop(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("32kq3vfpd1msv3v0nv0uqavzh"); // selfTop (edge_t* edges[], int ind, int cnt, double sizex, double stepy,
UNSUPPORTED("2t4o7k97lw32u08cs5j96r7if"); //            splineInfo* sinfo) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2131r3ibxv7drmcz6f2j5d9c2"); //     int i, sgn, point_pair;
UNSUPPORTED("1suoh1r8nnndqo9txafuch8az"); //     double hy, ty,  stepx, dx, dy, width, height; 
UNSUPPORTED("7a2vzpy4tpc2fpmuf12nhtfca"); //     pointf tp, hp, np;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("cutkizwxyuykhmayeb60m22av"); //     pointf points[1000];
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("e3wy3x07xdsusfbgecfcqg5lj"); //     e = edges[ind];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("43yzlf5354g6qlugyzpmr745t"); //     stepx = (sizex / 2.) / cnt;
UNSUPPORTED("7199vb689fs8rdn6j40wpw2py"); //     stepx = MAX(stepx, 2.);
UNSUPPORTED("dko3xt785e372nj0fiocjfas"); //     pointn = 0;
UNSUPPORTED("dqazhjgevh1spyg3xzwb3bcks"); //     np = ND_coord(n);
UNSUPPORTED("ehf9o80lfi02no07wz207kyp6"); //     tp = ED_tail_port(e).p;
UNSUPPORTED("f18822xrptoagri7001gamxwh"); //     tp.x += np.x;
UNSUPPORTED("pcmp8bdd8677mjvvef7kfh5y"); //     tp.y += np.y;
UNSUPPORTED("b4mfdkjjk3n78ssy4h80g5lc6"); //     hp = ED_head_port(e).p;
UNSUPPORTED("e7rhhgc42h5z6kvvnkz6wfn0r"); //     hp.x += np.x;
UNSUPPORTED("bisu3qji6rw3wu3srdv8vhrxb"); //     hp.y += np.y;
UNSUPPORTED("2c8kmvidaqx92wd2mq1ys6753"); //     if (tp.x >= hp.x) sgn = 1;
UNSUPPORTED("cvln1r5ffbp1z1sq0y6ago4og"); //     else sgn = -1;
UNSUPPORTED("7squuk10wt6xrbp24obpx41bw"); //     dy = ND_ht(n)/2., dx = 0.;
UNSUPPORTED("7sojr831wk2u8c86xerkjyojd"); //     // certain adjustments are required for some point_pairs in order to improve the 
UNSUPPORTED("byuachd2fjte06s7xwnbmxlcx"); //     // display of the edge path between them
UNSUPPORTED("eje36stfd9p7ulgo4qk6gjwvx"); //     point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
UNSUPPORTED("2qmvjd6iwnaqwop679caoaxnn"); //     switch(point_pair){
UNSUPPORTED("6mjalqxwnjw8e27c2ioujowul"); // 	case 15:	
UNSUPPORTED("5vvzajt4nlp9tr9qagb46uzw0"); // 		dx = sgn*(ND_rw(n) - (hp.x-np.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("av3gl91zikst7e3hby657df3z"); // 	case 38:
UNSUPPORTED("d2wzrbnbuinus07v39wtrzg6k"); // 		dx = sgn*(ND_lw(n)-(np.x-hp.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dk2te1ff65z24g7yge6td5w1h"); // 	case 41:
UNSUPPORTED("54zp7hq4t1477ra0toi6nfc3s"); // 		dx = sgn*(ND_rw(n)-(tp.x-np.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("eyz8046vmrhfd05uo35ud2o26"); // 	case 48:
UNSUPPORTED("54zp7hq4t1477ra0toi6nfc3s"); // 		dx = sgn*(ND_rw(n)-(tp.x-np.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("90hjo1ph35lg8jy4yywzro3nf"); // 	case 14:
UNSUPPORTED("23d0sltghssogk5wk9024lh41"); // 	case 37:
UNSUPPORTED("3dvppfwsy4t6h54uecu5i9hry"); // 	case 47:
UNSUPPORTED("95n009mwo78h9zg1mx5yc3j7l"); // 	case 51:
UNSUPPORTED("8ytmvd73zq9qu5c4ku4jcap4a"); // 	case 57:
UNSUPPORTED("1tbpkq9m2taj7n3fj63cocjyn"); // 	case 58:
UNSUPPORTED("u36w11cbjvnwnr2a9aukmfop"); // 		dx = sgn*((((ND_lw(n)-(np.x-tp.x)) + (ND_rw(n)-(hp.x-np.x)))/3.));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("a92kp8x7ej800lliiwzfuobem"); // 	case 73:
UNSUPPORTED("c22dsvqh8h2a9v76t3u9dzyi4"); //  		dx = sgn*(ND_lw(n)-(np.x-tp.x) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("3ijo2dao8lyum56ai3jujbmap"); // 	case 83:
UNSUPPORTED("bjovxk89tmb4rsuvw09nszp4c"); // 		dx = sgn*(ND_lw(n)-(np.x-tp.x));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("f4rvirpst5ft3uksqp3okyjcf"); // 	case 84:
UNSUPPORTED("1i3gur9btuj2u5s8ybbgmd87y"); // 		dx = sgn*((((ND_lw(n)-(np.x-tp.x)) + (ND_rw(n)-(hp.x-np.x)))/2.) + stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("72fiv7451m2qnkzbfjcwv7pgx"); // 	case 74:
UNSUPPORTED("1yel8f4unntut6w7bgdhgsvq1"); // 	case 75:
UNSUPPORTED("93kdallci9a743giye6pd0y0f"); // 	case 85:
UNSUPPORTED("919n1grj4s92nolxlmoqx4qqw"); // 		dx = sgn*((((ND_lw(n)-(np.x-tp.x)) + (ND_rw(n)-(hp.x-np.x)))/2.) + 2*stepx);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7jtwt2insvwb55tafkqx3boot"); //     ty = MIN(dy, 3*(np.y + dy - tp.y));
UNSUPPORTED("7vwwj5yxukw3e1k0twakhhgg3"); //     hy = MIN(dy, 3*(np.y + dy - hp.y));
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("a0u9ggni4r8gikqyyxf6wgaa5"); //         e = edges[ind++];
UNSUPPORTED("bgymnp4yekw8tzr70cnzzn9ez"); //         dy += stepy, ty += stepy, hy += stepy, dx += sgn*stepx;
UNSUPPORTED("8tkxpvgpxpilkes33cj73nr8o"); //         pointn = 0;
UNSUPPORTED("2j93ajzz3i9adm0syj177su98"); //         points[pointn++] = tp;
UNSUPPORTED("810s5qsu6it4vef0j2l5blqdm"); //         points[pointn++] = pointfof(tp.x + dx, tp.y + ty / 3);
UNSUPPORTED("r9y9vrfhtcn0ly9mxyipodbo"); //         points[pointn++] = pointfof(tp.x + dx, np.y + dy);
UNSUPPORTED("576fgxddv6rfxjwqc4ziex02m"); //         points[pointn++] = pointfof((tp.x+hp.x)/2, np.y + dy);
UNSUPPORTED("7wozarouo08hg5qnrcqmlrzv1"); //         points[pointn++] = pointfof(hp.x - dx, np.y + dy);
UNSUPPORTED("6z0fdvc1cxk34nwjps2o0vy9e"); //         points[pointn++] = pointfof(hp.x - dx, hp.y + hy / 3);
UNSUPPORTED("6t0sueo9zyoccfzqit4c7pvcy"); //         points[pointn++] = hp;
UNSUPPORTED("6nhnbriaxn7zi0ab1z8bkbzd"); //         if (ED_label(e)) {
UNSUPPORTED("95cz173vhlho6qxwqiafjznd6"); // 	    if (GD_flip(agraphof(agtail(e)))) {
UNSUPPORTED("5tq797micincut6x05g6eokxk"); // 		width = ED_label(e)->dimen.y;
UNSUPPORTED("2wpl3ja2mlxynjamnyblux5j"); // 		height = ED_label(e)->dimen.x;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("5oxmxe34kl5iq4p27e8r7k11y"); // 		width = ED_label(e)->dimen.x;
UNSUPPORTED("4eunm5kqgzuzko60febalr1gg"); // 		height = ED_label(e)->dimen.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8ygvy3gas9hrwh99z44j4vw14"); // 	    ED_label(e)->pos.y = ND_coord(n).y + dy + height / 2.0;
UNSUPPORTED("89l2ovblsu6gnx97clo8ev1yk"); // 	    ED_label(e)->pos.x = ND_coord(n).x;
UNSUPPORTED("3tkba5lhpnujfu8lcz8lewsyn"); // 	    ED_label(e)->set = NOT(0);
UNSUPPORTED("df1lpvk1x9s2nna4dimpv5ixv"); // 	    if (height > stepy)
UNSUPPORTED("anykz2jqihvnza16edujzsmnm"); // 		dy += height - stepy;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("iyaed8bkc8xb16vcnxvc7d6s"); //        clip_and_install(e, aghead(e), points, pointn, sinfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b9185t6i77ez1ac587ul8ndnc"); //     return;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3sr8gvj4141qql0v12lb89cyt
// static void selfRight (edge_t* edges[], int ind, int cnt, double stepx, double sizey,            splineInfo* sinfo)  
public static void selfRight(ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, double stepx, double sizey, ST_splineInfo sinfo) {
ENTERING("3sr8gvj4141qql0v12lb89cyt","selfRight");
try {
    int i, sgn, point_pair;
    double hx, tx, stepy, dx, dy, width, height; 
    final ST_pointf tp = new ST_pointf(), hp = new ST_pointf(), np = new ST_pointf();
    ST_Agnode_s n;
    ST_Agedge_s e;
    final ST_pointf.Array points = new ST_pointf.Array( 1000);
    int pointn;
    e = (ST_Agedge_s) edges.get(ind);
    n = agtail(e);
    stepy = (sizey / 2.) / cnt;
    stepy = MAX(stepy, 2.);
    pointn = 0;
    np.___(ND_coord(n));
    tp.___(ED_tail_port(e).p);
    tp.setDouble("x", tp.x + np.x);
    tp.setDouble("y", tp.y + np.y);
    hp.___(ED_head_port(e).p);
    hp.setDouble("x", hp.x + np.x);
    hp.setDouble("y", hp.y + np.y);
    if (tp.y >= hp.y) sgn = 1;
    else sgn = -1;
    dx = ND_rw(n);
    dy = 0;
    // certain adjustments are required for some point_pairs in order to improve the 
    // display of the edge path between them
    point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
    switch(point_pair){
      case 32: 
      case 65:	if(tp.y == hp.y)
		  sgn = -sgn;
		break;
      default:
		break;
    }
    tx = MIN(dx, 3*(np.x + dx - tp.x));
    hx = MIN(dx, 3*(np.x + dx - hp.x));
    for (i = 0; i < cnt; i++) {
        e = (ST_Agedge_s) edges.plus(ind++).getPtr();
        dx += stepx; tx += stepx; hx += stepx; dy += sgn*stepy;
        pointn = 0;
        points.plus(pointn++).setStruct(tp);
        points.plus(pointn++).setStruct(pointfof(tp.x + tx / 3, tp.y + dy));
        points.plus(pointn++).setStruct(pointfof(np.x + dx, tp.y + dy));
        points.plus(pointn++).setStruct(pointfof(np.x + dx, (tp.y+hp.y)/2));
        points.plus(pointn++).setStruct(pointfof(np.x + dx, hp.y - dy));
        points.plus(pointn++).setStruct(pointfof(hp.x + hx / 3, hp.y - dy));
        points.plus(pointn++).setStruct(hp);
        if (ED_label(e)!=null) {
	    if (GD_flip(agraphof(agtail(e)))!=0) {
		width = ED_label(e).dimen.y;
		height = ED_label(e).dimen.x;
	    } else {
		width = ED_label(e).dimen.x;
		height = ED_label(e).dimen.y;
	    }
	    ED_label(e).pos.setDouble("x", ND_coord(n).x + dx + width / 2.0);
	    ED_label(e).pos.setDouble("y", ND_coord(n).y);
	    ED_label(e).set= NOTI(false);
	    if (width > stepx)
		dx += width - stepx;
        }
	clip_and_install(e, aghead(e), points.asPtr(), pointn, sinfo);
    }
    return;
} finally {
LEAVING("3sr8gvj4141qql0v12lb89cyt","selfRight");
}
}




//3 pb3pqqgfs6pzscxz9g4ip66b
// static void selfLeft (edge_t* edges[], int ind, int cnt, double stepx, double sizey,           splineInfo* sinfo)  
public static Object selfLeft(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("e1xon7wncs6szxkut7r3ylg8a"); // selfLeft (edge_t* edges[], int ind, int cnt, double stepx, double sizey,
UNSUPPORTED("304grcrgelbk1tnep5avkaylv"); //           splineInfo* sinfo) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5kzmtbtnct9t7sugzyg0t1505"); //     int i, sgn,point_pair;
UNSUPPORTED("9oe5un4g42lfm6s5sruhajt5n"); //     double hx, tx, stepy, dx, dy, width, height; 
UNSUPPORTED("7a2vzpy4tpc2fpmuf12nhtfca"); //     pointf tp, hp, np;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("cutkizwxyuykhmayeb60m22av"); //     pointf points[1000];
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("e3wy3x07xdsusfbgecfcqg5lj"); //     e = edges[ind];
UNSUPPORTED("dul1axf6kjslblufm4omk5k32"); //     n = agtail(e);
UNSUPPORTED("2biq5cfn3eflyc9vcakp8z40j"); //     stepy = (sizey / 2.) / cnt;
UNSUPPORTED("ag6m3hxmkt2fwxfbd09gtse84"); //     stepy = MAX(stepy,2.);
UNSUPPORTED("dko3xt785e372nj0fiocjfas"); //     pointn = 0;
UNSUPPORTED("dqazhjgevh1spyg3xzwb3bcks"); //     np = ND_coord(n);
UNSUPPORTED("ehf9o80lfi02no07wz207kyp6"); //     tp = ED_tail_port(e).p;
UNSUPPORTED("f18822xrptoagri7001gamxwh"); //     tp.x += np.x;
UNSUPPORTED("pcmp8bdd8677mjvvef7kfh5y"); //     tp.y += np.y;
UNSUPPORTED("b4mfdkjjk3n78ssy4h80g5lc6"); //     hp = ED_head_port(e).p;
UNSUPPORTED("e7rhhgc42h5z6kvvnkz6wfn0r"); //     hp.x += np.x;
UNSUPPORTED("bisu3qji6rw3wu3srdv8vhrxb"); //     hp.y += np.y;
UNSUPPORTED("9pq7cc11wf5inm1gtl9nubola"); //     if (tp.y >= hp.y) sgn = 1;
UNSUPPORTED("cvln1r5ffbp1z1sq0y6ago4og"); //     else sgn = -1;
UNSUPPORTED("5t4m5gzysfvdd5gfy1snezlv1"); //     dx = ND_lw(n), dy = 0.;
UNSUPPORTED("7sojr831wk2u8c86xerkjyojd"); //     // certain adjustments are required for some point_pairs in order to improve the 
UNSUPPORTED("byuachd2fjte06s7xwnbmxlcx"); //     // display of the edge path between them
UNSUPPORTED("eje36stfd9p7ulgo4qk6gjwvx"); //     point_pair = convert_sides_to_points(ED_tail_port(e).side,ED_head_port(e).side);
UNSUPPORTED("2qmvjd6iwnaqwop679caoaxnn"); //     switch(point_pair){
UNSUPPORTED("1ztn6qfhzw55cdorxgbs8mvaw"); //       case 12:
UNSUPPORTED("5nakmzm2t38aw7gowxf3597ny"); //       case 67:
UNSUPPORTED("bvy8vwcvwtkz9nqaq8173x6bh"); // 		if(tp.y == hp.y)
UNSUPPORTED("cffqbosum7o1l5iposy2evrfl"); // 		  sgn = -sgn;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("5vhsnixpf0pg2oz10ps2valyn"); //       default:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("p2znjsd0rhcqyou2d4tcp4m3"); //     tx = MIN(dx, 3*(tp.x + dx - np.x));
UNSUPPORTED("9fc36i8mydvpx5fb9s7oomgg9"); //     hx = MIN(dx, 3*(hp.x + dx - np.x));
UNSUPPORTED("1psokm6w9e7qw7fm2g1cayuk7"); //     for (i = 0; i < cnt; i++) {
UNSUPPORTED("a0u9ggni4r8gikqyyxf6wgaa5"); //         e = edges[ind++];
UNSUPPORTED("corxl7j4p1epemy3mlhnxuh7f"); //         dx += stepx, tx += stepx, hx += stepx, dy += sgn*stepy;
UNSUPPORTED("8tkxpvgpxpilkes33cj73nr8o"); //         pointn = 0;
UNSUPPORTED("2j93ajzz3i9adm0syj177su98"); //         points[pointn++] = tp;
UNSUPPORTED("1d3rn5phdxf8hhlmh3b3wp7lh"); //         points[pointn++] = pointfof(tp.x - tx / 3, tp.y + dy);
UNSUPPORTED("2wxmjkn0pmrslgogz96iftqs0"); //         points[pointn++] = pointfof(np.x - dx, tp.y + dy);
UNSUPPORTED("clocavnhfvokhhthg9cujkqa0"); //         points[pointn++] = pointfof(np.x - dx, (tp.y+hp.y)/2);
UNSUPPORTED("6tz9mqs3ff68mo5r1xmq2zyc4"); //         points[pointn++] = pointfof(np.x - dx, hp.y - dy);
UNSUPPORTED("a6oh2uv36d620c50ery1vvmd7"); //         points[pointn++] = pointfof(hp.x - hx / 3, hp.y - dy);
UNSUPPORTED("6t0sueo9zyoccfzqit4c7pvcy"); //         points[pointn++] = hp;
UNSUPPORTED("6nhnbriaxn7zi0ab1z8bkbzd"); //         if (ED_label(e)) {
UNSUPPORTED("7ewy2tc2zfli5k6dghdnao8tw"); //     	if (GD_flip(agraphof(agtail(e)))) {
UNSUPPORTED("7d83ym7h1stime4wbmifcx809"); //     	    width = ED_label(e)->dimen.y;
UNSUPPORTED("44m5sni7g3n6fnk6ca57u9dc2"); //     	    height = ED_label(e)->dimen.x;
UNSUPPORTED("s8koz5x85ytpnff1o94rlxqy"); //     	} else {
UNSUPPORTED("66vu2joy64r1yrkvp3oolz1ws"); //     	    width = ED_label(e)->dimen.x;
UNSUPPORTED("d6bobo1f6gxkxa2fffvmn41g0"); //     	    height = ED_label(e)->dimen.y;
UNSUPPORTED("klxoy56t7b20wxnwqm0qoofz"); //     	}
UNSUPPORTED("e7au5qlazz8i26lvbl9c5k657"); //     	ED_label(e)->pos.x = ND_coord(n).x - dx - width / 2.0;
UNSUPPORTED("dfo4prcp2cafipoufh8bql0id"); //     	ED_label(e)->pos.y = ND_coord(n).y;
UNSUPPORTED("7efx4yevu8176mmuqjtk4bfss"); //     	ED_label(e)->set = NOT(0);
UNSUPPORTED("8ivpntotxg2rgw585hkdgsixh"); //     	if (width > stepx)
UNSUPPORTED("34ujzn0u4l056cgabsn09ncw8"); //     	    dx += width - stepx;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5fvid2bi7fy5jv5dyttfprpzj"); //         clip_and_install(e, aghead(e), points, pointn, sinfo);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 678whq05s481ertx02jloteu3
// int selfRightSpace (edge_t* e) 
public static int selfRightSpace(ST_Agedge_s e) {
ENTERING("678whq05s481ertx02jloteu3","selfRightSpace");
try {
    int sw=0;
    double label_width;
    ST_textlabel_t l = ED_label(e);
    if ((N(ED_tail_port(e).defined) && N(ED_head_port(e).defined)) ||
        (
		N(ED_tail_port(e).side & (1<<3)) && 
         N(ED_head_port(e).side & (1<<3)) &&
          ((ED_tail_port(e).side != ED_head_port(e).side) || 
          (N(ED_tail_port(e).side & ((1<<2)|(1<<0)))))
		  )) {
	sw = 18;
	if (l!=null) {
	    label_width = GD_flip(agraphof(aghead(e)))!=0 ? l.dimen.y : l.dimen.x;
	    sw += label_width;
    }
    }
    else sw = 0;
    return sw;
} finally {
LEAVING("678whq05s481ertx02jloteu3","selfRightSpace");
}
}




//3 bt3fwgprixbc5rceeewozdqr9
// void makeSelfEdge(path * P, edge_t * edges[], int ind, int cnt, double sizex, 	     double sizey, splineInfo * sinfo) 
public static void makeSelfEdge(ST_path P, ST_Agedge_s.ArrayOfStar edges, int ind, int cnt, double sizex, double sizey, ST_splineInfo sinfo) {
ENTERING("bt3fwgprixbc5rceeewozdqr9","makeSelfEdge");
try {
    ST_Agedge_s e;
    e = (ST_Agedge_s) edges.get(ind);
    /* self edge without ports or
     * self edge with all ports inside, on the right, or at most 1 on top 
     * and at most 1 on bottom 
     */
    if ((N(ED_tail_port(e).defined) && N(ED_head_port(e).defined)) ||
        (
		N(ED_tail_port(e).side & (1<<3)) && 
         N(ED_head_port(e).side & (1<<3)) &&
          ((ED_tail_port(e).side != ED_head_port(e).side) || 
          (N(ED_tail_port(e).side & ((1<<2)|(1<<0))))))) {
	selfRight(edges, ind, cnt, sizex, sizey, sinfo);
    }
    /* self edge with port on left side */
    else if ((ED_tail_port(e).side & (1<<3))!=0 || (ED_head_port(e).side & (1<<3))!=0) {
	/* handle L-R specially */
	if ((ED_tail_port(e).side & (1<<1))!=0 || (ED_head_port(e).side & (1<<1))!=0) {
	    selfTop(edges, ind, cnt, sizex, sizey, sinfo);
	}
	else {
	    selfLeft(edges, ind, cnt, sizex, sizey, sinfo);
	}
    }
    /* self edge with both ports on top side */
    else if ((ED_tail_port(e).side & (1<<2))!=0) {
	selfTop(edges, ind, cnt, sizex, sizey, sinfo);
    }
    else if ((ED_tail_port(e).side & (1<<0))!=0) {
	selfBottom(edges, ind, cnt, sizex, sizey, sinfo);
    }
    else assert(false);
} finally {
LEAVING("bt3fwgprixbc5rceeewozdqr9","makeSelfEdge");
}
}




//3 9br31owvzkmo6hpgi5o3yqs6u
// void makePortLabels(edge_t * e) 
public static Object makePortLabels(Object... arg) {
UNSUPPORTED("7h8uwyqfmkrxlwe199ha5685e"); // void makePortLabels(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8592v0z3b9lcnaz6kmep9i7fb"); //     /* Only use this if labelangle or labeldistance is set for the edge;
UNSUPPORTED("60d7j1whu0efnsich22dgcgct"); //      * otherwise, handle with external labels.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("exadv83959uk532g2sef35k8d"); //     if (!E_labelangle && !E_labeldistance) return;
UNSUPPORTED("ex1c7eojs5nx6t10t191xuzvj"); //     if (ED_head_label(e) && !ED_head_label(e)->set) {
UNSUPPORTED("1hjdl17xghg2aygn0psu0p2oq"); // 	if (place_portlabel(e, NOT(0)))
UNSUPPORTED("3rgc9w83agr2ba9s9muyubzqz"); // 	    updateBB(agraphof(agtail(e)), ED_head_label(e));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("twsg91c027nxls3s64jxn4cy"); //     if (ED_tail_label(e) && !ED_tail_label(e)->set) {
UNSUPPORTED("bu5i9ibw82s14f8vgd3nz2s46"); // 	if (place_portlabel(e, 0))
UNSUPPORTED("80z5y1fe03judv18cnzdcbyww"); // 	    updateBB(agraphof(agtail(e)), ED_tail_label(e));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7wyn51o9k6u7joaq9k18boffh
// static void endPoints(splines * spl, pointf * p, pointf * q) 
public static void endPoints(ST_splines spl, ST_pointf p, ST_pointf q) {
ENTERING("7wyn51o9k6u7joaq9k18boffh","endPoints");
try {
     final ST_bezier bz = new ST_bezier();
     bz.____(spl.list.get(0));
     if (bz.sflag!=0) {
UNSUPPORTED("4wazlko0bxmzxoobqacij1btk"); // 	*p = bz.sp;
     }
     else {
    	p.____(bz.list.get(0));
     }
     bz.____(spl.list.plus(spl.size-1).getPtr());
     if (bz.eflag!=0) {
UNSUPPORTED("78u9nvs8u7rxturidz5nf8hn4"); // 	*q = bz.ep;
     }
     else {
		q.____(bz.list.get(bz.size-1));
     }
} finally {
LEAVING("7wyn51o9k6u7joaq9k18boffh","endPoints");
}
}




//3 4v696uonfsqf7e6iafx99ovoj
// static pointf polylineMidpoint (splines* spl, pointf* pp, pointf* pq) 
public static Object polylineMidpoint(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("2h8atayvi2acugdlg0pai43ib"); // polylineMidpoint (splines* spl, pointf* pp, pointf* pq)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("37thdceezsvepe7tlyfatrbcw"); //     bezier bz;
UNSUPPORTED("88fnnm6x9xzbtbojfr24qfqgy"); //     int i, j, k;
UNSUPPORTED("ejxa966mpm7v3o59230r3vwjz"); //     double d, dist = 0;
UNSUPPORTED("8nmqlo5bwer36kl8mp1gtz5bf"); //     pointf pf, qf, mf;
UNSUPPORTED("4z4l1zu33m72iirxmuhdg36iw"); //     for (i = 0; i < spl->size; i++) {
UNSUPPORTED("ewoeh3d6otc2rgpaxuzwm38x7"); // 	bz = spl->list[i];
UNSUPPORTED("99as77xwpn23hvyqfywj6oi2g"); // 	for (j = 0, k=3; k < bz.size; j+=3,k+=3) {
UNSUPPORTED("2gecyey0ki14xgfwd8lmdmexb"); // 	    pf = bz.list[j];
UNSUPPORTED("apnzdgopwxw4q1fw7qtnifypo"); // 	    qf = bz.list[k];
UNSUPPORTED("829tfml4q5a7brr6x4nun2cuc"); // 	    dist += DIST(pf, qf);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("em4an03o2ddsawe30b5l50d8k"); //     dist /= 2;
UNSUPPORTED("4z4l1zu33m72iirxmuhdg36iw"); //     for (i = 0; i < spl->size; i++) {
UNSUPPORTED("ewoeh3d6otc2rgpaxuzwm38x7"); // 	bz = spl->list[i];
UNSUPPORTED("99as77xwpn23hvyqfywj6oi2g"); // 	for (j = 0, k=3; k < bz.size; j+=3,k+=3) {
UNSUPPORTED("2gecyey0ki14xgfwd8lmdmexb"); // 	    pf = bz.list[j];
UNSUPPORTED("apnzdgopwxw4q1fw7qtnifypo"); // 	    qf = bz.list[k];
UNSUPPORTED("9efld0l6jtgzu237fqme6vlih"); // 	    d = DIST(pf,qf);
UNSUPPORTED("uolmj8tx8ul9z4zxcs6xt03h"); // 	    if (d >= dist) {
UNSUPPORTED("5pequ7xrb4otn4nmoo4xzbppn"); // 		*pp = pf;
UNSUPPORTED("6y0c82r8tb4tcxzx62aes05hf"); // 		*pq = qf;
UNSUPPORTED("43wwwo6k1nlge8ou0cyuxjsws"); // 		mf.x = ((qf.x*dist) + (pf.x*(d-dist)))/d; 
UNSUPPORTED("7rndnud8ft5vs2kx1fjwhymtm"); // 		mf.y = ((qf.y*dist) + (pf.y*(d-dist)))/d; 
UNSUPPORTED("9t6wng08fgzru4uhripezjnkg"); // 		return mf;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("9pooqnj2v1895ue6jlimhrxgr"); // 		dist -= d;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8kllckmfkh8837qtc8hwoh74j"); //     assert (0);   /* should never get here */
UNSUPPORTED("cs6egh6pje9pqxb748vbj2q51"); //     return mf;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8hpmwzlqbj1nii32zubbe9hru
// pointf edgeMidpoint (graph_t* g, edge_t * e) 
public static ST_pointf edgeMidpoint(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("8hpmwzlqbj1nii32zubbe9hru","edgeMidpoint");
try {
	return edgeMidpoint_(g, e).copy();
} finally {
LEAVING("8hpmwzlqbj1nii32zubbe9hru","edgeMidpoint");
}
}
	
	
private static ST_pointf edgeMidpoint_(ST_Agraph_s g, ST_Agedge_s e) {
     int et = (GD_flags(g) & (7 << 1));
     final ST_pointf d = new ST_pointf();
     final ST_pointf spf = new ST_pointf();
     final ST_pointf p = new ST_pointf();
     final ST_pointf q = new ST_pointf();
     endPoints((ST_splines) ED_spl(e), p, q);
     if (APPROXEQPT(p, q, MILLIPOINT)) { /* degenerate spline */
UNSUPPORTED("7i8m5mpfnv7m9uqxh015zfdaj"); // 	spf = p;
     }
     else if ((et == (5 << 1)) || (et == (2 << 1))) {
 	d.x = (q.x + p.x) / 2.;
 	d.y = (p.y + q.y) / 2.;
 	spf.___(dotneato_closest((ST_splines)ED_spl(e), d));
     }
     else {   /* ET_PLINE, ET_ORTHO or ET_LINE */
UNSUPPORTED("6he3hi05vusuthrchn4enk7o6"); // 	spf = polylineMidpoint (ED_spl(e), &p, &q);
     }
     return spf;
}




//3 3msxu7tuq8q3m0sqtthq29flm
// void addEdgeLabels(graph_t* g, edge_t * e, pointf rp, pointf rq) 
public static Object addEdgeLabels(Object... arg) {
UNSUPPORTED("ldo41gaevp1jys68pnbguk3z"); // void addEdgeLabels(graph_t* g, edge_t * e, pointf rp, pointf rq)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("as6htoa1c0fv7e0v9a7h98p5h"); //     makePortLabels(e);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 54bdrq95zwpwmxf86ln5eom99
// int place_portlabel(edge_t * e, boolean head_p) 
public static Object place_portlabel(Object... arg) {
UNSUPPORTED("bnu1zhg128g969c502d6btzbs"); // int place_portlabel(edge_t * e, boolean head_p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2l3ombz9fng1wmxwdgkjokemn"); //     textlabel_t *l;
UNSUPPORTED("6tl9mepc2bett364jduh2q4mf"); //     splines *spl;
UNSUPPORTED("3hs99atzl1l857khumt6ycmbh"); //     bezier *bez;
UNSUPPORTED("7dsz4anx5o7u9qq3lzzknay2f"); //     double dist, angle;
UNSUPPORTED("65fz6d9e07e4c95zyum8bt633"); //     pointf c[4], pe, pf;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("8l8zbwbivibv6xgq0pxgwvq9n"); //     char* la;
UNSUPPORTED("e5b3d0nivpt5k8dkz65otlbpz"); //     char* ld;
UNSUPPORTED("2kp0srstg11z7hyemmxoo4e52"); //     if (ED_edge_type(e) == 6)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("chxgak0231km4auc7310y9pk8"); //     /* add label here only if labelangle or labeldistance is defined; else, use external label */
UNSUPPORTED("t397yg339z571sgse05vtq1w"); //     if ((!E_labelangle || (*(la = agxget(e,E_labelangle)) == '\0')) &&
UNSUPPORTED("8uvkv1d0qbznkf3xuozvc7qzf"); // 	(!E_labeldistance || (*(ld = agxget(e,E_labeldistance)) == '\0'))) {
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("tm1vvm5oplen4vv9y7lqo6xf"); //     l = head_p ? ED_head_label(e) : ED_tail_label(e);
UNSUPPORTED("3gtifp1yui3al397n1i9akefw"); //     if ((spl = getsplinepoints(e)) == NULL) return 0;
UNSUPPORTED("2ia4wsp0i4dpjelpvoiytkbi7"); //     if (!head_p) {
UNSUPPORTED("7ahdd5aq924y6mwot89trjyra"); // 	bez = &spl->list[0];
UNSUPPORTED("efd82pfo3nyqu51w9264hj3kl"); // 	if (bez->sflag) {
UNSUPPORTED("47udgszrx6pdd38kn2sweuhwh"); // 	    pe = bez->sp;
UNSUPPORTED("e5nb3zmvz6fp6xqkor9i4yf0"); // 	    pf = bez->list[0];
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("61yxghl3kwjuso7s1p0n7t1n2"); // 	    pe = bez->list[0];
UNSUPPORTED("ced0y25bh0hjo9cnppppf7h2x"); // 	    for (i = 0; i < 4; i++)
UNSUPPORTED("1y5d5t4i694a5f0j7ee6a7z2y"); // 		c[i] = bez->list[i];
UNSUPPORTED("92m7eg16654p0n58cwr883aqa"); // 	    pf = Bezier(c, 3, 0.1, NULL, NULL);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("4s7tr233odbsuhmyc1ksdmnjs"); // 	bez = &spl->list[spl->size - 1];
UNSUPPORTED("5d2ntg5cm9vsqeat2p88bel1l"); // 	if (bez->eflag) {
UNSUPPORTED("er4lbg8eptuapclxc4o7sqvam"); // 	    pe = bez->ep;
UNSUPPORTED("91krm055d5o8geih7ot17sp97"); // 	    pf = bez->list[bez->size - 1];
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("70e7dqxnvd7agtj92eve28u4r"); // 	    pe = bez->list[bez->size - 1];
UNSUPPORTED("ced0y25bh0hjo9cnppppf7h2x"); // 	    for (i = 0; i < 4; i++)
UNSUPPORTED("7v8xa8y04nup6v5cwzsdkqs2z"); // 		c[i] = bez->list[bez->size - 4 + i];
UNSUPPORTED("8pbi2gjbe0mg3ghpfkt7kwpm9"); // 	    pf = Bezier(c, 3, 0.9, NULL, NULL);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("14iusq03pj0vtm5mkr4ny0o1y"); //     angle = atan2(pf.y - pe.y, pf.x - pe.x) +
UNSUPPORTED("2jyhji8yxpqj21l189dgr1wcz"); // 	RADIANS(late_double(e, E_labelangle, -25, -180.0));
UNSUPPORTED("5ibf647cm3agxlbdzq27a2sed"); //     dist = 10 * late_double(e, E_labeldistance, 1.0, 0.0);
UNSUPPORTED("etyjtm4uw7xa47lhonum31o4r"); //     l->pos.x = pe.x + dist * cos(angle);
UNSUPPORTED("b28y7lmoordi3n69md9mfl7k9"); //     l->pos.y = pe.y + dist * sin(angle);
UNSUPPORTED("5nziiydj5nf07y5wrg22fpu86"); //     l->set = NOT(0);
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2tbz9tbkzx8os72qiyhgnby67
// splines *getsplinepoints(edge_t * e) 
public static ST_splines getsplinepoints(ST_Agedge_s e) {
ENTERING("2tbz9tbkzx8os72qiyhgnby67","getsplinepoints");
try {
    ST_Agedge_s le;
    ST_splines sp;
    for (le = e; N(sp = ED_spl(le)) && ED_edge_type(le) != 0;
	 le = ED_to_orig(le));
    if (sp == null) 
UNSUPPORTED("8oq6gemxrb07hmmw0gtux7os5"); // 	agerr (AGERR, "getsplinepoints: no spline points available for edge (%s,%s)\n",
// UNSUPPORTED("bw49w8tpkv5eblsevof4kelef"); // 	    agnameof(agtail(e)), agnameof(aghead(e)));
    return sp;
} finally {
LEAVING("2tbz9tbkzx8os72qiyhgnby67","getsplinepoints");
}
}


}
