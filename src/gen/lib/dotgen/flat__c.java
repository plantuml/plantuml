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
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static gen.lib.dotgen.fastgr__c.virtual_node;
import static gen.lib.dotgen.mincross__c.rec_reset_vlists;
import static gen.lib.dotgen.mincross__c.rec_save_vlists;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ALLOC_Agnode_s;
import static smetana.core.Macro.ALLOC_ST_rank_t;
import static smetana.core.Macro.ED_adjacent;
import static smetana.core.Macro.ED_dist;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_alg;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_pointf;
import h.ST_rank_t;
import smetana.core.__ptr__;

public class flat__c {
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




//3 e0gtvsxlvztmwu8yy44wfvf97
// static node_t *make_vn_slot(graph_t * g, int r, int pos) 
public static ST_Agnode_s make_vn_slot(ST_Agraph_s g, int r, int pos) {
ENTERING("e0gtvsxlvztmwu8yy44wfvf97","make_vn_slot");
try {
    int i;
    ST_Agnode_s.ArrayOfStar v;
    ST_Agnode_s n;
	v = ALLOC_Agnode_s(GD_rank(g).get(r).n + 2, (ST_Agnode_s.ArrayOfStar) GD_rank(g).get(r).v);
    GD_rank(g).plus(r).setPtr("v", v);
    for (i = GD_rank(g).get(r).n; i > pos; i--) {
	v.plus(i).setPtr(v.plus(i - 1).getPtr());
	ND_order(v.get(i), ND_order(v.get(i))+1);
    }
    n = virtual_node(g);
    v.plus(pos).setPtr(n);
    ND_order(n, pos);
    ND_rank(n, r);
    GD_rank(g).plus(r).setInt("n", GD_rank(g).get(r).n+1);
    v.plus(GD_rank(g).get(r).n).setPtr(null);
    return (ST_Agnode_s) v.get(pos);
} finally {
LEAVING("e0gtvsxlvztmwu8yy44wfvf97","make_vn_slot");
}
}




//3 d64wt9oqphauv3hp4axbg2ep3
// static void findlr(node_t * u, node_t * v, int *lp, int *rp) 
public static void findlr(ST_Agnode_s u, ST_Agnode_s v, int lp[], int rp[]) {
ENTERING("d64wt9oqphauv3hp4axbg2ep3","findlr");
try {
    int l, r;
    l = ND_order(u);
    r = ND_order(v);
    if (l > r) {
	int t = l;
	l = r;
	r = t;
    }
    lp[0] = l;
    rp[0] = r;
} finally {
LEAVING("d64wt9oqphauv3hp4axbg2ep3","findlr");
}
}




//3 bwjjmaydx5a2fnpeoligkha0r
// static void setbounds(node_t * v, int *bounds, int lpos, int rpos) 
public static void setbounds(ST_Agnode_s v, int bounds[], int lpos[], int rpos[]) {
ENTERING("bwjjmaydx5a2fnpeoligkha0r","setbounds");
try {
    int i, ord;
    int[] l = new int[1], r = new int[1];
    ST_Agedge_s f;
    if (ND_node_type(v) == 1) {
	ord = ND_order(v);
	if (ND_in(v).size == 0) {	/* flat */
	    assert(ND_out(v).size == 2);
	    findlr((ST_Agnode_s) aghead(ND_out(v).getFromList(0)), (ST_Agnode_s) aghead(ND_out(v).getFromList(1)), l,
		   r);
	    /* the other flat edge could be to the left or right */
	    if (r[0] <= lpos[0])
		bounds[2] = bounds[0] = ord;
	    else if (l[0] >= rpos[0])
		bounds[3] = bounds[1] = ord;
	    /* could be spanning this one */
	    else if ((l[0] < lpos[0]) && (r[0] > rpos[0]));	/* ignore */
	    /* must have intersecting ranges */
	    else {
		if ((l[0] < lpos[0]) || ((l[0] == lpos[0]) && (r[0] < rpos[0])))
		    bounds[2] = ord;
		if ((r[0] > rpos[0]) || ((r[0] == rpos[0]) && (l[0] > lpos[0])))
		    bounds[3] = ord;
	    }
	} else {		/* forward */
	    boolean onleft, onright;
	    onleft = onright = false;
	    for (i = 0; (f = (ST_Agedge_s) ND_out(v).getFromList(i))!=null; i++) {
		if (ND_order(aghead(f)) <= lpos[0]) {
		    onleft = NOT(false);
		    continue;
		}
		if (ND_order(aghead(f)) >= rpos[0]) {
		    onright = NOT(false);
		    continue;
		}
	    }
	    if (onleft && (onright == false))
		bounds[0] = ord + 1;
	    if (onright && (onleft == false))
		bounds[1] = ord - 1;
	}
    }
} finally {
LEAVING("bwjjmaydx5a2fnpeoligkha0r","setbounds");
}
}




//3 3bc4otcsxj1dujj49ydbb19oa
// static int flat_limits(graph_t * g, edge_t * e) 
public static int flat_limits(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("3bc4otcsxj1dujj49ydbb19oa","flat_limits");
try {
    int lnode, rnode, r, pos;
    int[] lpos = new int[1], rpos = new int[1];
    int bounds[] = new int[4];
    ST_Agnode_s.ArrayOfStar rank;
    r = ND_rank(agtail(e)) - 1;
    rank = GD_rank(g).get(r).v;
    lnode = 0;
    rnode = GD_rank(g).get(r).n - 1;
    bounds[0] = bounds[2] = lnode - 1;
    bounds[1] = bounds[3] = rnode + 1;
    findlr(agtail(e), aghead(e), lpos, rpos);
    while (lnode <= rnode) {
	setbounds((ST_Agnode_s)rank.get(lnode), bounds, lpos, rpos);
	if (lnode != rnode)
	    setbounds((ST_Agnode_s)rank.get(rnode), bounds, lpos, rpos);
	lnode++;
	rnode--;
	if (bounds[1] - bounds[0] <= 1)
	    break;
    }
    if (bounds[0] <= bounds[1])
	pos = (bounds[0] + bounds[1] + 1) / 2;
    else
	pos = (bounds[2] + bounds[3] + 1) / 2;
    return pos;
} finally {
LEAVING("3bc4otcsxj1dujj49ydbb19oa","flat_limits");
}
}




//3 4cw9yo9ap8ze1r873v6jat4yc
// static void  flat_node(edge_t * e) 
public static void flat_node(ST_Agedge_s e) {
ENTERING("4cw9yo9ap8ze1r873v6jat4yc","flat_node");
try {
    int r, place, ypos, h2;
    ST_Agraph_s g;
    ST_Agnode_s n, vn;
    ST_Agedge_s ve;
    final ST_pointf dimen = new ST_pointf();
    if (ED_label(e) == null)
	return;
    g = dot_root(agtail(e));
    r = ND_rank(agtail(e));
    place = flat_limits(g, e);
    /* grab ypos = LL.y of label box before make_vn_slot() */
    if ((n = (ST_Agnode_s) GD_rank(g).get(r - 1).v.get(0))!=null)
	ypos = (int)(ND_coord(n).y - GD_rank(g).get(r - 1).ht1);
    else {
	n = (ST_Agnode_s) GD_rank(g).get(r).v.get(0);
	ypos = (int)(ND_coord(n).y + GD_rank(g).get(r).ht2 + GD_ranksep(g));
    }
    vn = make_vn_slot(g, r - 1, place);
    dimen.___(ED_label(e).dimen);
    if (GD_flip(g)!=0) {
	double f = dimen.x;
	dimen.setDouble("x", dimen.y);
	dimen.setDouble("y", f);
    }
    ND_ht(vn, dimen.y);
    h2 = (int)(ND_ht(vn) / 2);
    ND_rw(vn, dimen.x / 2);
    ND_lw(vn, ND_rw(vn));
    ND_label(vn, ED_label(e));
    ND_coord(vn).setDouble("y", ypos + h2);
    ve = virtual_edge(vn, agtail(e), e);	/* was NULL? */
    ED_tail_port(ve).p.setDouble("x", -ND_lw(vn));
    ED_head_port(ve).p.setDouble("x", ND_rw(agtail(e)));
    ED_edge_type(ve, 4);
    ve = virtual_edge(vn, aghead(e), e);
    ED_tail_port(ve).p.setDouble("x", ND_rw(vn));
    ED_head_port(ve).p.setDouble("x", ND_lw(aghead(e)));
    ED_edge_type(ve, 4);
    /* another assumed symmetry of ht1/ht2 of a label node */
    if (GD_rank(g).get(r - 1).ht1 < h2)
	GD_rank(g).plus(r - 1).setDouble("ht1", h2);
    if (GD_rank(g).get(r - 1).ht2 < h2)
	GD_rank(g).plus(r - 1).setDouble("ht2", h2);
    ND_alg(vn, e);
} finally {
LEAVING("4cw9yo9ap8ze1r873v6jat4yc","flat_node");
}
}




//3 1lopavodoru6ee52snd5l6swd
// static void abomination(graph_t * g) 
public static void abomination(ST_Agraph_s g) {
ENTERING("1lopavodoru6ee52snd5l6swd","abomination");
try {
    int r;
    ST_rank_t.Array2 rptr;
    assert(GD_minrank(g) == 0);
    /* 3 = one for new rank, one for sentinel, one for off-by-one */
    r = GD_maxrank(g) + 3;
    rptr = ALLOC_ST_rank_t(r, (ST_rank_t.Array2) GD_rank(g));
    GD_rank(g, rptr.plus(1));
    for (r = GD_maxrank(g); r >= 0; r--)
	GD_rank(g).get(r).setStruct(GD_rank(g).get(r - 1).getStruct());
    GD_rank(g).plus(r).setInt("n", 0);
    GD_rank(g).plus(r).setInt("an", 0);
    GD_rank(g).plus(r).setPtr("v", new ST_Agnode_s.ArrayOfStar(2));
    GD_rank(g).plus(r).setPtr("av", GD_rank(g).get(r).v);
    GD_rank(g).plus(r).setPtr("flat", null);
    GD_rank(g).plus(r).setDouble("ht1", 1);
    GD_rank(g).plus(r).setDouble("ht2", 1);
    GD_rank(g).plus(r).setDouble("pht1", 1);
    GD_rank(g).plus(r).setDouble("pht2", 1);
    GD_minrank(g, GD_minrank(g)-1);
} finally {
LEAVING("1lopavodoru6ee52snd5l6swd","abomination");
}
}




//3 ctujx6e8k3rzv08h6gswdcaqs
// static void checkFlatAdjacent (edge_t* e) 
public static void checkFlatAdjacent(ST_Agedge_s e) {
ENTERING("ctujx6e8k3rzv08h6gswdcaqs","checkFlatAdjacent");
try {
    ST_Agnode_s tn = agtail(e);
    ST_Agnode_s hn = aghead(e);
    int i, lo, hi;
    ST_Agnode_s n;
    ST_rank_t.Array2 rank;
    if (ND_order(tn) < ND_order(hn)) {
	lo = ND_order(tn);
	hi = ND_order(hn);
    }
    else {
	lo = ND_order(hn);
	hi = ND_order(tn);
    }
    rank = GD_rank(dot_root(tn)).plus(ND_rank(tn));
    for (i = lo + 1; i < hi; i++) {
	n = (ST_Agnode_s) rank.getPtr().v.get(i);
	if ((ND_node_type(n) == 1 && ND_label(n)!=null) || 
             ND_node_type(n) == 0)
	    break;
    }
    if (i == hi) {  /* adjacent edge */
	do {
	    ED_adjacent(e, 1);
	    e = ED_to_virt(e);
	} while (e!=null); 
    }
} finally {
LEAVING("ctujx6e8k3rzv08h6gswdcaqs","checkFlatAdjacent");
}
}




//3 bjwwj6ftkm0gv04cf1edqeaw6
// int  flat_edges(graph_t * g) 
public static int flat_edges(ST_Agraph_s g) {
ENTERING("bjwwj6ftkm0gv04cf1edqeaw6","flat_edges");
try {
    int i, j, reset = 0;
    ST_Agnode_s n;
    ST_Agedge_s e;
    int found = 0;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	if (ND_flat_out(n).listNotNull()) {
	    for (j = 0; (e = (ST_Agedge_s) ND_flat_out(n).getFromList(j))!=null; j++) {
		checkFlatAdjacent (e);
	    }
	}
	for (j = 0; j < ND_other(n).size; j++) {
	    e = (ST_Agedge_s) ND_other(n).getFromList(j);
	    if (ND_rank(aghead(e)) == ND_rank(agtail(e)))
		checkFlatAdjacent (e);
	}
    }
    if ((GD_rank(g).get(0).flat!=null) || (GD_n_cluster(g) > 0)) {
	for (i = 0; (n = (ST_Agnode_s) GD_rank(g).get(0).v.get(i))!=null; i++) {
	    for (j = 0; (e = (ST_Agedge_s) ND_flat_in(n).getFromList(j))!=null; j++) {
		if ((ED_label(e)!=null) && N(ED_adjacent(e))) {
		    abomination(g);
		    found = 1;
		    break;
		}
	    }
	    if (found!=0)
		break;
	}
    }
    rec_save_vlists(g);
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
          /* if n is the tail of any flat edge, one will be in flat_out */
	if (ND_flat_out(n).listNotNull()) {
	    for (i = 0; (e = (ST_Agedge_s) ND_flat_out(n).getFromList(i))!=null; i++) {
		if (ED_label(e)!=null) {
		    if (ED_adjacent(e)!=0) {
			if (GD_flip(g)!=0) ED_dist(e, ED_label(e).dimen.y);
			else ED_dist(e, ED_label(e).dimen.x); 
		    }
		    else {
			reset = 1;
			flat_node(e);
		    }
		}
	    }
		/* look for other flat edges with labels */
	    for (j = 0; j < ND_other(n).size; j++) {
		ST_Agedge_s le;
		e = (ST_Agedge_s) ND_other(n).getFromList(j);
		if (ND_rank(agtail(e)) != ND_rank(aghead(e))) continue;
		if (EQ(agtail(e), aghead(e))) continue; /* skip loops */
		le = e;
		while (ED_to_virt(le)!=null) le = ED_to_virt(le);
		ED_adjacent(e, ED_adjacent(le)); 
		if (ED_label(e)!=null) {
		    if (ED_adjacent(e)!=0) {
			double lw;
			if (GD_flip(g)!=0) lw = ED_label(e).dimen.y;
			else lw = ED_label(e).dimen.x; 
			ED_dist(le, MAX(lw,ED_dist(le)));
		    }
		    else {
			reset = 1;
			flat_node(e);
		    }
		}
	    }
	}
    }
    if (reset!=0)
	rec_reset_vlists(g);
    return reset;
} finally {
LEAVING("bjwwj6ftkm0gv04cf1edqeaw6","flat_edges");
}
}


}
