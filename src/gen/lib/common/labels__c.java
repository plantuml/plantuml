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
import static gen.lib.cgraph.graph__c.agisdirected;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.obj__c.agobjkind;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.htmltable__c.make_html_label;
import static gen.lib.common.memory__c.gmalloc;
import static gen.lib.common.utils__c.htmlEntityUTF8;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.strlen;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.ZALLOC_ST_textspan_t;
import static smetana.core.Macro.agtail;
import static smetana.core.Macro.hackInitDimensionFromLabel;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agrec_s;
import h.ST_GVC_s;
import h.ST_pointf;
import h.ST_port;
import h.ST_textlabel_t;
import h.ST_textspan_t;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class labels__c {
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




//3 4wkeqik2dt7ecr64ej6ltbnvb
// static void storeline(GVC_t *gvc, textlabel_t *lp, char *line, char terminator) 
//private static __struct__<textfont_t> tf = JUtils.from(textfont_t.class);
public static void storeline(ST_GVC_s gvc, ST_textlabel_t lp, CString line, char terminator) {
ENTERING("4wkeqik2dt7ecr64ej6ltbnvb","storeline");
try {
    final ST_pointf size = new ST_pointf();
    ST_textspan_t span = null;
    int oldsz = lp.nspans + 1;
    lp.span = ZALLOC_ST_textspan_t((ST_textspan_t.Array) lp.span, oldsz + 1);
    span = lp.span.get(lp.nspans);
    span.str = line;
    span.just = terminator;
    if (line!=null && line.charAt(0)!='\0') {
	Z.z().tf.name = lp.fontname;
	Z.z().tf.size = lp.fontsize;
	// WE CHEAT
	size.x = 0.0;
	size.y = (int)(lp.fontsize * 1.20);
	hackInitDimensionFromLabel(size, line.getContent());
	span.size.y = (int)size.y;
    }
    else {
	System.err.println("YOU SHOULD NOT SEE THAT");
	size.x = 0.0;
	size.y = (int)(lp.fontsize * 1.20);
	span.size.y = (int)(lp.fontsize * 1.20);
    }
    lp.nspans = lp.nspans + 1;
    /* width = max line width */
    lp.dimen.x = MAX(lp.dimen.x, size.x);
    /* accumulate height */
    lp.dimen.y = lp.dimen.y + size.y;
} finally {
LEAVING("4wkeqik2dt7ecr64ej6ltbnvb","storeline");
}
}




//3 22ar72ye93a8ntj8pagnt5b5k
// void make_simple_label(GVC_t * gvc, textlabel_t * lp) 
public static void make_simple_label(ST_GVC_s gvc, ST_textlabel_t lp) {
ENTERING("22ar72ye93a8ntj8pagnt5b5k","make_simple_label");
try {
    char c;
    CString p, line, lineptr, str = lp.text;
    char bytee = 0x00;
    ((ST_pointf) lp.dimen).x = 0.0;
    ((ST_pointf) lp.dimen).y = 0.0;
    if (str.charAt(0) == '\0')
	return;
    line = lineptr = null;
    p = str;
    line = lineptr = gmalloc((strlen(p) + 1));
    line.setCharAt(0, '\0');
    while ((c = p.charAt(0))!='\0') {
    p = p.plus(1);
	bytee = c;
	/* wingraphviz allows a combination of ascii and big-5. The latter
         * is a two-byte encoding, with the first byte in 0xA1-0xFE, and
         * the second in 0x40-0x7e or 0xa1-0xfe. We assume that the input
         * is well-formed, but check that we don't go past the ending '\0'.
         */
	if ((lp.charset == 2) && 0xA1 <= bytee && bytee <= 0xFE) {
UNSUPPORTED("6la63t1mnqv30shyyp3yfroxb"); // 	    *lineptr++ = c;
UNSUPPORTED("ebmmarxykvf76hmfmjuk0ssjz"); // 	    c = *p++;
UNSUPPORTED("6la63t1mnqv30shyyp3yfroxb"); // 	    *lineptr++ = c;
UNSUPPORTED("1kri3b36twfj4t7bvjbrt6dhs"); // 	    if (!c) /* NB. Protect against unexpected string end here */
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	} else {
	    if (c == '\\') {
		switch (p.charAt(0)) {
		case 'n':
		case 'l':
		case 'r':
		    lineptr.setCharAt(0, '\0');
		    lineptr = lineptr.plus(1);
		    storeline(gvc, lp, line, p.charAt(0));
		    line = lineptr;
		    break;
		default:
		    lineptr.setCharAt(0, p.charAt(0));
		    lineptr = lineptr.plus(1);
		}
		if (p.charAt(0)!='\0')
		    p = p.plus(1);
		/* tcldot can enter real linend characters */
	    } else if (c == '\n') {
		    lineptr.setCharAt(0, '\0');
		    lineptr = lineptr.plus(1);
		storeline(gvc, lp, line, 'n');
		line = lineptr;
	    } else {
		    lineptr.setCharAt(0, c);
		    lineptr = lineptr.plus(1);
	    }
	}
    }
    if (NEQ(line, lineptr)) {
	lineptr.setCharAt(0, '\0');
	lineptr = lineptr.plus(1);
	storeline(gvc, lp, line, 'n');
    }
    lp.setStruct("space", lp.dimen);
} finally {
LEAVING("22ar72ye93a8ntj8pagnt5b5k","make_simple_label");
}
}




//3 ecq5lydlrjrlaz8o6vm6svc8i
// textlabel_t *make_label(void *obj, char *str, int kind, double fontsize, char *fontname, char *fontcolor) 
public static ST_textlabel_t make_label(__ptr__ obj, CString str, int kind, double fontsize, CString fontname, CString fontcolor) {
ENTERING("ecq5lydlrjrlaz8o6vm6svc8i","make_label");
try {
	ST_textlabel_t rv = new ST_textlabel_t();
    ST_Agraph_s g = null, sg = null;
    ST_Agnode_s n = null;
    ST_Agedge_s e = null;
        CString s = null;
    switch (agobjkind(obj)) {
    case AGRAPH:
        sg = (ST_Agraph_s)obj;
	g = (ST_Agraph_s) sg.root;
	break;
    case AGNODE:
        n = (ST_Agnode_s)obj.castTo(ST_Agnode_s.class);
	g = agroot(agraphof(n));
	break;
    case AGEDGE:
        e = (ST_Agedge_s)obj.castTo(ST_Agedge_s.class);
	g = agroot(agraphof(aghead(e)));
	break;
    }
    rv.setPtr("fontname", fontname);
    rv.setPtr("fontcolor", fontcolor);
    rv.setDouble("fontsize", fontsize);
    rv.charset = ((ST_Agraphinfo_t)g.castTo_ST_Agobj_s().data.castTo(ST_Agraphinfo_t.class)).charset;
    if ((kind & (2 << 1))!=0) {
	rv.setPtr("text", str.strdup());
        if ((kind & (1 << 1))!=0) {
	    rv.html = (N(0));
	}
    }
    else if (kind == (1 << 1)) {
	rv.setPtr("text", str.strdup());
	rv.html = N(0);
	if (make_html_label(obj, rv)!=0) {
	    switch (agobjkind(obj)) {
    case AGRAPH:
	        UNSUPPORTED("agerr(AGPREV, in label of graph %s\n,agnameof(sg));");
		break;
    case AGNODE:
	        UNSUPPORTED("agerr(AGPREV, in label of node %s\n, agnameof(n));");
		break;
    case AGEDGE:
		UNSUPPORTED("agerr(AGPREV, in label of edge %s %s %s\n,");
//		        agnameof(((((((Agobj_t*)(e))->tag).objtype) == 3? (e): ((e)+1))->node)), agisdirected(g)?"->":"--", agnameof(((((((Agobj_t*)(e))->tag).objtype) == 2? (e): ((e)-1))->node)));
		break;
	    }
	}
    }
    else {
        //assert(kind == (0 << 1));
	/* This call just processes the graph object based escape sequences. The formatting escape
         * sequences (\n, \l, \r) are processed in make_simple_label. That call also replaces \\ with \.
         */
	rv.setPtr("text", strdup_and_subst_obj0(str, obj, 0));
        switch (rv.charset) {
    case 1:
	    UNSUPPORTED("s = latin1ToUTF8(rv->text);");
	    break;
	default: /* UTF8 */
	    s = htmlEntityUTF8(rv.text, g);
	    break;
	}
        Memory.free(rv.text);
        rv.setPtr("text", s);
	make_simple_label(g.castTo_ST_Agobj_s().data.castTo_ST_Agraphinfo_t().gvc, rv);
    }
    return rv;
} finally {
LEAVING("ecq5lydlrjrlaz8o6vm6svc8i","make_label");
}
}




//3 1qv9kl0wi0snf1xtrb6vo2yiu
// void free_textspan(textspan_t * tl, int cnt) 
public static Object free_textspan(Object... arg) {
UNSUPPORTED("eh9j8hhtolluyaegv937nnpys"); // void free_textspan(textspan_t * tl, int cnt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("4iuvdwfqjfqyk7dpy1s8mgw2o"); //     textspan_t* tlp = tl;
UNSUPPORTED("1f70128zxfm2jtz3ebwavc3nc"); //     if (!tl) return;
UNSUPPORTED("3r70mdmcurt4csiwolv9n6mmh"); //     for (i = 0; i < cnt; i++) { 
UNSUPPORTED("anxpl4mk65alvdjja9pn97kro"); // 	if ((i == 0) && tlp->str)
UNSUPPORTED("1fhpzk3z0aliuuzuovdmsmp7d"); // 	    free(tlp->str);
UNSUPPORTED("b0d4wly929yfs331ilovdcqjo"); // 	if (tlp->layout && tlp->free_layout)
UNSUPPORTED("40xyvfopujwdcztwx5vs27p5d"); // 	    tlp->free_layout (tlp->layout);
UNSUPPORTED("72ldhje939sa8dg5ttywkmwa6"); // 	tlp++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4pi9nz7hlvy0eo98r6tzd0oy"); //     free(tl);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2palqnlr5u0ssnag1bp5tq510
// void free_label(textlabel_t * p) 
public static Object free_label(Object... arg) {
UNSUPPORTED("7fgqv20ow9mktpealn2ueqf9i"); // void free_label(textlabel_t * p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3cvmixd2u1g2d9l03kuxyyxxw"); //     if (p) {
UNSUPPORTED("26ktwrszmdk4inac0pxe7mudg"); // 	free(p->text);
UNSUPPORTED("6sxnt2ovvq1ihlz4odqepnnln"); // 	if (p->html) {
UNSUPPORTED("8p452npbufapts0rm8ff82xfu"); // 	    if (p->u.html) free_html_label(p->u.html, 1);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("ghxtimeg851hu16krg2dnb14"); // 	    free_textspan(p->u.txt.span, p->u.txt.nspans);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cy5x5dma0v4hiepir7lrfuo17"); // 	free(p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a0gse5mgpw0xhsmu4lwellfdq
// void emit_label(GVJ_t * job, emit_state_t emit_state, textlabel_t * lp) 
public static Object emit_label(Object... arg) {
UNSUPPORTED("2bspalmq4q86q66b0sehcnui"); // void emit_label(GVJ_t * job, emit_state_t emit_state, textlabel_t * lp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("ecr1y7qy0ikxkidkdfvwv88ir"); //     emit_state_t old_emit_state;
UNSUPPORTED("c3lqudp40feg72zp97ngqkww9"); //     old_emit_state = obj->emit_state;
UNSUPPORTED("3ook7gsw0rr7b6uwm9f5a5dtx"); //     obj->emit_state = emit_state;
UNSUPPORTED("17ejiipjo2ljqtqe4rkpx6b15"); //     if (lp->html) {
UNSUPPORTED("8jyhluz8a51w5y5qalhlrpgnp"); // 	emit_html_label(job, lp->u.html, lp);
UNSUPPORTED("2rghcn1n5g9f29wxd8nqhub0p"); // 	obj->emit_state = old_emit_state;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("71u0rnrd0pn6urf5ttv39ec5d"); //     /* make sure that there is something to do */
UNSUPPORTED("c4lzcn6qndbrx4u9kybwfk11l"); //     if (lp->u.txt.nspans < 1)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("2qj4sjf5amdyhvqsi47mei07v"); //     gvrender_begin_label(job, LABEL_PLAIN);
UNSUPPORTED("bl5mm7hn2il0n2e623rbq8q5w"); //     gvrender_set_pencolor(job, lp->fontcolor);
UNSUPPORTED("8a1fbspdbpecd6k8uj7bse1cx"); //     /* position for first span */
UNSUPPORTED("f433l06cgmbaiibsv9a9tjun8"); //     switch (lp->valign) {
UNSUPPORTED("c66d69vmpko3goomc3npvjhy3"); // 	case 't':
UNSUPPORTED("63ukdafw48pf9seqbe8vedft9"); //     	    p.y = lp->pos.y + lp->space.y / 2.0 - lp->fontsize;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5coaag9ev3fzc0jtpkjkfvhd2"); // 	case 'b':
UNSUPPORTED("eodoiejpigubj8in93lnnc05d"); //     	    p.y = lp->pos.y - lp->space.y / 2.0 + lp->dimen.y - lp->fontsize;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f3lyz2cejs6yn5fyckhn7ba1"); // 	case 'c':
UNSUPPORTED("8jmef3sfg06sme4q6t23ms8i7"); // 	default:	
UNSUPPORTED("bicy4u16zheftiukscavoqq3c"); //     	    p.y = lp->pos.y + lp->dimen.y / 2.0 - lp->fontsize;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8mg9hiit60q1ve98yormydem5"); //     for (i = 0; i < lp->u.txt.nspans; i++) {
UNSUPPORTED("aq0sxux77sg7whxt7hslkske3"); // 	switch (lp->u.txt.span[i].just) {
UNSUPPORTED("bwy7mh2nb7lz950r20rfilwa4"); // 	case 'l':
UNSUPPORTED("ds60aoxxlta1y3r5wuo09mzzp"); // 	    p.x = lp->pos.x - lp->space.x / 2.0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("43ds1cyu29ex66kcjszrcu3mp"); // 	case 'r':
UNSUPPORTED("1103h1zfvqia1xelrw919hw2p"); // 	    p.x = lp->pos.x + lp->space.x / 2.0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("f187wptsr73liavtlyoyfovp3"); // 	case 'n':
UNSUPPORTED("7tkhws043t1k3ra1n1191ld2m"); // 	    p.x = lp->pos.x;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("a00q6gh3dlg3gdt95e6rt129n"); // 	gvrender_textspan(job, p, &(lp->u.txt.span[i]));
UNSUPPORTED("dczp8jeltzgwb21pyxgxyrasy"); // 	/* UL position for next span */
UNSUPPORTED("33byiwlc3a1j2oeyyqf04jpoo"); // 	p.y -= lp->u.txt.span[i].size.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8bm7lzosnhuf0od7i4hkgzovo"); //     gvrender_end_label(job);
UNSUPPORTED("b1bkq4eyrmepbxyb3qiuhi8b8"); //     obj->emit_state = old_emit_state;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ajohywvjbrvkc7zca2uew6ghm
// static char *strdup_and_subst_obj0 (char *str, void *obj, int escBackslash) 
public static CString strdup_and_subst_obj0(CString str, __ptr__ obj, int escBackslash) {
ENTERING("ajohywvjbrvkc7zca2uew6ghm","strdup_and_subst_obj0");
try {
    char c; CString s, p, t, newstr;
    CString tp_str = new CString(""), hp_str = new CString("");
    CString g_str = new CString("\\G"), n_str = new CString("\\N"), e_str = new CString("\\E"),
	h_str = new CString("\\H"), t_str = new CString("\\T"), l_str = new CString("\\L");
    int g_len = 2, n_len = 2, e_len = 2,
	h_len = 2, t_len = 2, l_len = 2,
	tp_len = 0, hp_len = 0;
    int newlen = 0;
    int isEdge = 0;
    ST_textlabel_t tl;
    final ST_port pt = new ST_port();
    /* prepare substitution strings */
    switch (agobjkind(obj)) {
	case AGRAPH:
	    g_str = agnameof((ST_Agraph_s)obj);
	    g_len = strlen(g_str);
	    tl = GD_label((ST_Agraph_s)obj);
	    if (tl!=null) {
		l_str = tl.text;
	    	if (str!=null) l_len = strlen(l_str);
	    }
	    break;
	case AGNODE:
	    g_str = agnameof(agraphof(obj.castTo(ST_Agnode_s.class)));
	    g_len = strlen(g_str);
	    n_str = agnameof(obj.castTo(ST_Agnode_s.class));
	    n_len = strlen(n_str);
	    tl =  (ST_textlabel_t) ((ST_Agnode_s)obj.castTo(ST_Agnode_s.class)).castTo_ST_Agobj_s().data.castTo_ST_Agnodeinfo_t().label;
	    if (tl!=null) {
		l_str = tl.text;
	    	if (str!=null) l_len = strlen(l_str);
	    }
	    break;
	case AGEDGE:
	    isEdge = 1;
	    g_str = agnameof(agroot(agraphof(agtail((obj.castTo(ST_Agedge_s.class))))));
	    g_len = strlen(g_str);
	    t_str = agnameof(agtail((obj.castTo(ST_Agedge_s.class))));
	    t_len = strlen(t_str);
	    pt.___(ED_tail_port(obj.castTo(ST_Agedge_s.class)));
	    if ((tp_str = pt.name)!=null)
	        tp_len = strlen(tp_str);
	    h_str = agnameof(aghead((obj.castTo(ST_Agedge_s.class))));
	    h_len = strlen(h_str);
	    pt.___(ED_head_port(obj.castTo(ST_Agedge_s.class)));
	    if ((hp_str = pt.name)!=null)
		hp_len = strlen(hp_str);
	    h_len = strlen(h_str);
	    tl = ED_label(obj.castTo(ST_Agedge_s.class));
	    if (tl!=null) {
	    	l_str = tl.text;
	    	if (str!=null) l_len = strlen(l_str);
	    }
	    if (agisdirected(agroot(agraphof(agtail((obj.castTo(ST_Agedge_s.class)))))))
		e_str = new CString("->");
	    else
		e_str = new CString("--");
	    e_len = t_len + (tp_len!=0?tp_len+1:0) + 2 + h_len + (hp_len!=0?hp_len+1:0);
	    break;
    }
    /* two passes over str.
     *
     * first pass prepares substitution strings and computes 
     * total length for newstring required from malloc.
     */
    for (s = str; ;) {
    c = s.charAt(0);
    s = s.plus(1);
    if (c=='\0') break;
	if (c == '\\') {
	  c = s.charAt(0);
	  s = s.plus(1);
	    switch (c) {
	    case 'G':
		newlen += g_len;
		break;
	    case 'N':
		newlen += n_len;
		break;
	    case 'E':
		newlen += e_len;
		break;
	    case 'H':
		newlen += h_len;
		break;
	    case 'T':
		newlen += t_len;
		break; 
	    case 'L':
		newlen += l_len;
		break; 
	    case '\\':
		if (escBackslash!=0) {
		    newlen += 1;
		    break; 
		}
		/* Fall through */
	    default:  /* leave other escape sequences unmodified, e.g. \n \l \r */
		newlen += 2;
	    }
	} else {
	    newlen++;
	}
    }
    /* allocate new string */
    newstr = gmalloc(newlen + 1);
    /* second pass over str assembles new string */
    p = newstr;
    for (s = str; ;) {
    c = s.charAt(0);
    s = s.plus(1);
    if (c=='\0') break;
	if (c == '\\') {
	  c = s.charAt(0);
	  s = s.plus(1);
	    switch (c) {
	    case 'G':
		UNSUPPORTED("for (t = g_str; (*p = *t++); p++);");
		break;
	    case 'N':
		for (t = n_str; ; ) {
		p.setCharAt(0, t.charAt(0));
		t = t.plus(1);
		if (p.charAt(0)=='\0') break;
		p = p.plus(1);
		}
		break;
	    case 'E':
		UNSUPPORTED("if (isEdge) {");
/*		    for (t = t_str; (*p = *t++); p++);
		    if (tp_len) {
			*p++ = ':';
			for (t = tp_str; (*p = *t++); p++);
		    }
		    for (t = e_str; (*p = *t++); p++);
		    for (t = h_str; (*p = *t++); p++);
		    if (hp_len) {
			*p++ = ':';
			for (t = hp_str; (*p = *t++); p++);
		    }
		}*/
		break;
	    case 'T':
		UNSUPPORTED("for (t = t_str; (*p = *t++); p++);");
		break;
	    case 'H':
		UNSUPPORTED("for (t = h_str; (*p = *t++); p++);");
		break;
	    case 'L':
		UNSUPPORTED("for (t = l_str; (*p = *t++); p++);");
		break;
	    case '\\':
		UNSUPPORTED("if (escBackslash) {");
/*		    *p++ = '\\';
		    break; 
		}*/
		/* Fall through */
	    default:  /* leave other escape sequences unmodified, e.g. \n \l \r */
	    p.setCharAt(0, '\\');
	    p = p.plus(1);
	    p.setCharAt(0, c);
	    p = p.plus(1);
		break;
	    }
	} else {
	    p.setCharAt(0, c);
	    p = p.plus(1);
	}
    }
	    p.setCharAt(0, '\0');
	    p = p.plus(1);
    return newstr;
} finally {
LEAVING("ajohywvjbrvkc7zca2uew6ghm","strdup_and_subst_obj0");
}
}




//3 af2a0cdl8ld7bbq0qu0rt1d8z
// char *strdup_and_subst_obj(char *str, void *obj) 
public static CString strdup_and_subst_obj(CString str, __ptr__ obj) {
ENTERING("af2a0cdl8ld7bbq0qu0rt1d8z","strdup_and_subst_obj");
try {
 UNSUPPORTED("7eeocxzl6qhtvcv7gnh73o7d1"); // char *strdup_and_subst_obj(char *str, void *obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("67419rdrhawe7vudn882sohkd"); //     return strdup_and_subst_obj0 (str, obj, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("af2a0cdl8ld7bbq0qu0rt1d8z","strdup_and_subst_obj");
}
}




//3 bevzgi4opmmgxwksl1lssepxc
// static int xml_isentity(char *s) 
public static Object xml_isentity(Object... arg) {
UNSUPPORTED("ddxpdim5n11qvep9b61kpijae"); // static int xml_isentity(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7309b02hjekdyss2l1jlfa3o5"); //     s++;			/* already known to be '&' */
UNSUPPORTED("ee1xg58k0yj6mgak36f7bqx4z"); //     if (*s == '#') {
UNSUPPORTED("1fe0kohehgdxhenrepo7ymdcw"); // 	s++;
UNSUPPORTED("autyof60ysqv0zxvcovs8ol1o"); // 	if (*s == 'x' || *s == 'X') {
UNSUPPORTED("8dqpp2f04tbhgnmk4gjjb8dga"); // 	    s++;
UNSUPPORTED("7z8kjrmcms9gfhq10a65dbjog"); // 	    while ((*s >= '0' && *s <= '9')
UNSUPPORTED("cvyao8709l4lhttkvu9hcnvne"); // 		   || (*s >= 'a' && *s <= 'f')
UNSUPPORTED("5q6n6p2xf3gohuffw81ur8vhb"); // 		   || (*s >= 'A' && *s <= 'F'))
UNSUPPORTED("zybbca41b450wccgr0kkur00"); // 		s++;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("1hg41j8n8b0uz0lmqlqcy30so"); // 	    while (*s >= '0' && *s <= '9')
UNSUPPORTED("zybbca41b450wccgr0kkur00"); // 		s++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("8h0cru3h3jveaqc5sgyq4rk71"); // 	while ((*s >= 'a' && *s <= 'z')
UNSUPPORTED("31bir978ftxj7zjleb3mr1bei"); // 	       || (*s >= 'A' && *s <= 'Z'))
UNSUPPORTED("8dqpp2f04tbhgnmk4gjjb8dga"); // 	    s++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9hcbv4ydnt8trlfaz870sjsyh"); //     if (*s == ';')
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 57faqt3qjliznr0tkqn1ebjgk
// char *xml_string(char *s) 
public static Object xml_string(Object... arg) {
UNSUPPORTED("bnxc0qd7mqxuf0ayp7tmgh9i0"); // char *xml_string(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ufug0n20nuue5zidl5g8283p"); //     return xml_string0 (s, 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2ukbjx5sti6tulvxdrftanx4p
// char *xml_string0(char *s, boolean raw) 
public static Object xml_string0(Object... arg) {
UNSUPPORTED("c15krv3c4yexqimy1thaiy4zm"); // char *xml_string0(char *s, boolean raw)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("210phh7av8ei8ajjmpptx96m6"); //     static char *buf = NULL;
UNSUPPORTED("6fpm1wk8mz0hofn15361g6nzn"); //     static int bufsize = 0;
UNSUPPORTED("1q9p9mppsiolvz4xyp1xfe8s2"); //     char *p, *sub, *prev = NULL;
UNSUPPORTED("9ee5mdh9hsw491mz8n88ey33d"); //     int len, pos = 0;
UNSUPPORTED("cjyiejw67ffjjsk60tgeebvif"); //     if (!buf) {
UNSUPPORTED("4xkxdnucjd4ldoikgelnsa7c9"); // 	bufsize = 64;
UNSUPPORTED("9pvgystp8nwvblycum3rps3tz"); // 	buf = gmalloc(bufsize);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dpfzdhkfq6u86uv5f9lnqfbpq"); //     p = buf;
UNSUPPORTED("e8ng575h9ts71v7d04mtpxyr5"); //     while (s && *s) {
UNSUPPORTED("85svsyrkaau2681m7ya7tygx6"); // 	if (pos > (bufsize - 8)) {
UNSUPPORTED("aa09fjtytqkag416h6457rmkr"); // 	    bufsize *= 2;
UNSUPPORTED("3vu2n3g10rrkdmia0oyndfj4s"); // 	    buf = grealloc(buf, bufsize);
UNSUPPORTED("cz6j3s7zlvrbs83g3e42o83z7"); // 	    p = buf + pos;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("97iud4axotxlk7ywlijb75m3d"); // 	/* escape '&' only if not part of a legal entity sequence */
UNSUPPORTED("4x9tn37kkejgziq8i1mkuzm2l"); // 	if (*s == '&' && (raw || !(xml_isentity(s)))) {
UNSUPPORTED("cac1jzftgtiz1fvztpxrb5pzr"); // 	    sub = "&amp;";
UNSUPPORTED("17g0bn2jwfuvdxn93z9lsronr"); // 	    len = 5;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7e1vaedh6l0yfegrgw9donb9e"); // 	/* '<' '>' are safe to substitute even if string is already UTF-8 coded
UNSUPPORTED("2hspzajrej6p5fmxtp3xo33mw"); // 	 * since UTF-8 strings won't contain '<' or '>' */
UNSUPPORTED("3xg4wo6utmvnlgyvg8ciz4tey"); // 	else if (*s == '<') {
UNSUPPORTED("13qv1mf2mabbasi5mgrjsqs3n"); // 	    sub = "&lt;";
UNSUPPORTED("cn6mu8yh8covbdy1zkhq7256"); // 	    len = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("art2agkl4px2ncm5gmygaoi4v"); // 	else if (*s == '>') {
UNSUPPORTED("dbkjqbduusn4tza1p3ng8iuyi"); // 	    sub = "&gt;";
UNSUPPORTED("cn6mu8yh8covbdy1zkhq7256"); // 	    len = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("aundz2f5ze9pcxo1dksgg0zxi"); // 	else if (*s == '-') {	/* can't be used in xml comment strings */
UNSUPPORTED("37g3n162xpdox7os6l6tphpwc"); // 	    sub = "&#45;";
UNSUPPORTED("17g0bn2jwfuvdxn93z9lsronr"); // 	    len = 5;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1lvkv9camdmveih40gwylsnw6"); // 	else if (*s == ' ' && prev && *prev == ' ') {
UNSUPPORTED("b0atdb6d1uzal5s04csjvyq30"); // 	    /* substitute 2nd and subsequent spaces with required_spaces */
UNSUPPORTED("72g8c8ott8fvqjni8tir0plf7"); // 	    sub = "&#160;";  /* inkscape doesn't recognise &nbsp; */
UNSUPPORTED("axaethtisavqkan2dx7laigpw"); // 	    len = 6;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9e32cag083iuoabhlvpcldsa8"); // 	else if (*s == '"') {
UNSUPPORTED("4w7kby1vesehrcs7zpowfb0q6"); // 	    sub = "&quot;";
UNSUPPORTED("axaethtisavqkan2dx7laigpw"); // 	    len = 6;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("akz9vs5rt6crj61e0batgdi09"); // 	else if (*s == '\'') {
UNSUPPORTED("6ohptq6v6vjhjivmjf7d5auoz"); // 	    sub = "&#39;";
UNSUPPORTED("17g0bn2jwfuvdxn93z9lsronr"); // 	    len = 5;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("adxr80w3fzxvf4gxvbuo78rm5"); // 	    sub = s;
UNSUPPORTED("ct414lk8my1pywj7ypk05e6d"); // 	    len = 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("25ryz7f7izh8r6792860c1d0y"); // 	while (len--) {
UNSUPPORTED("byu93mjyl867v3xyy1fb9pw9"); // 	    *p++ = *sub++;
UNSUPPORTED("27349aw1zezm0lpez1jn3pcw4"); // 	    pos++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5j7yubj14qnrajv5mpkfylpk3"); // 	prev = s;
UNSUPPORTED("1fe0kohehgdxhenrepo7ymdcw"); // 	s++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2t1d66kwn86kqh0i665hqw6cl"); //     *p = '\0';
UNSUPPORTED("5jfpogdyby101eyuw2dhtb5cg"); //     return buf;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d3mybo5t72cpoks3gfdfynkjw
// char *xml_url_string(char *s) 
public static Object xml_url_string(Object... arg) {
UNSUPPORTED("9u7qpiphyo8n8zc8n1071b67e"); // char *xml_url_string(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("210phh7av8ei8ajjmpptx96m6"); //     static char *buf = NULL;
UNSUPPORTED("6fpm1wk8mz0hofn15361g6nzn"); //     static int bufsize = 0;
UNSUPPORTED("47g093jarn346ptid7u4d7e7m"); //     char *p, *sub;
UNSUPPORTED("9ee5mdh9hsw491mz8n88ey33d"); //     int len, pos = 0;
UNSUPPORTED("cjyiejw67ffjjsk60tgeebvif"); //     if (!buf) {
UNSUPPORTED("4xkxdnucjd4ldoikgelnsa7c9"); // 	bufsize = 64;
UNSUPPORTED("9pvgystp8nwvblycum3rps3tz"); // 	buf = gmalloc(bufsize);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dpfzdhkfq6u86uv5f9lnqfbpq"); //     p = buf;
UNSUPPORTED("e8ng575h9ts71v7d04mtpxyr5"); //     while (s && *s) {
UNSUPPORTED("85svsyrkaau2681m7ya7tygx6"); // 	if (pos > (bufsize - 8)) {
UNSUPPORTED("aa09fjtytqkag416h6457rmkr"); // 	    bufsize *= 2;
UNSUPPORTED("3vu2n3g10rrkdmia0oyndfj4s"); // 	    buf = grealloc(buf, bufsize);
UNSUPPORTED("cz6j3s7zlvrbs83g3e42o83z7"); // 	    p = buf + pos;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("97iud4axotxlk7ywlijb75m3d"); // 	/* escape '&' only if not part of a legal entity sequence */
UNSUPPORTED("2ulqzjhkp81o1cpur4y62jck9"); // 	if (*s == '&' && !(xml_isentity(s))) {
UNSUPPORTED("cac1jzftgtiz1fvztpxrb5pzr"); // 	    sub = "&amp;";
UNSUPPORTED("17g0bn2jwfuvdxn93z9lsronr"); // 	    len = 5;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7e1vaedh6l0yfegrgw9donb9e"); // 	/* '<' '>' are safe to substitute even if string is already UTF-8 coded
UNSUPPORTED("2hspzajrej6p5fmxtp3xo33mw"); // 	 * since UTF-8 strings won't contain '<' or '>' */
UNSUPPORTED("3xg4wo6utmvnlgyvg8ciz4tey"); // 	else if (*s == '<') {
UNSUPPORTED("13qv1mf2mabbasi5mgrjsqs3n"); // 	    sub = "&lt;";
UNSUPPORTED("cn6mu8yh8covbdy1zkhq7256"); // 	    len = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("art2agkl4px2ncm5gmygaoi4v"); // 	else if (*s == '>') {
UNSUPPORTED("dbkjqbduusn4tza1p3ng8iuyi"); // 	    sub = "&gt;";
UNSUPPORTED("cn6mu8yh8covbdy1zkhq7256"); // 	    len = 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9e32cag083iuoabhlvpcldsa8"); // 	else if (*s == '"') {
UNSUPPORTED("4w7kby1vesehrcs7zpowfb0q6"); // 	    sub = "&quot;";
UNSUPPORTED("axaethtisavqkan2dx7laigpw"); // 	    len = 6;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("akz9vs5rt6crj61e0batgdi09"); // 	else if (*s == '\'') {
UNSUPPORTED("6ohptq6v6vjhjivmjf7d5auoz"); // 	    sub = "&#39;";
UNSUPPORTED("17g0bn2jwfuvdxn93z9lsronr"); // 	    len = 5;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("adxr80w3fzxvf4gxvbuo78rm5"); // 	    sub = s;
UNSUPPORTED("ct414lk8my1pywj7ypk05e6d"); // 	    len = 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("25ryz7f7izh8r6792860c1d0y"); // 	while (len--) {
UNSUPPORTED("byu93mjyl867v3xyy1fb9pw9"); // 	    *p++ = *sub++;
UNSUPPORTED("27349aw1zezm0lpez1jn3pcw4"); // 	    pos++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1fe0kohehgdxhenrepo7ymdcw"); // 	s++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2t1d66kwn86kqh0i665hqw6cl"); //     *p = '\0';
UNSUPPORTED("5jfpogdyby101eyuw2dhtb5cg"); //     return buf;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
