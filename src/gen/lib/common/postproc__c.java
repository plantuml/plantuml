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
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.graph__c.agnnodes;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.geom__c.ccwrotatepf;
import static gen.lib.common.splines__c.edgeMidpoint;
import static gen.lib.common.splines__c.getsplinepoints;
import static gen.lib.common.utils__c.late_bool;
import static gen.lib.common.utils__c.updateBB;
import static gen.lib.label.xlabels__c.placeLabels;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_label;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_spl;
import static smetana.core.Macro.ED_tail_label;
import static smetana.core.Macro.ED_xlabel;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_border;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.GD_label_pos;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_rankdir;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_height;
import static smetana.core.Macro.ND_width;
import static smetana.core.Macro.ND_xlabel;
import static smetana.core.Macro.NOTI;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsym_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_cinfo_t;
import h.ST_label_params_t;
import h.ST_object_t;
import h.ST_pointf;
import h.ST_splines;
import h.ST_textlabel_t;
import h.ST_xlabel_t;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;

public class postproc__c {
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


//1 1b0s7t28jl5ftrxccb8fsopp4
// static int Rankdir
//private static int Rankdir;

//1 3oo1lt5jtd6kuqjr6qqlnkutt
// static boolean Flip
//private static boolean Flip;

//1 ejooa1m5uoq0ue852wtuerpy
// static pointf Offset
//private final static ST_pointf Offset = new ST_pointf();



//3 dajapw16wus3rwimkrk5ihi2b
// static pointf map_point(pointf p) 
public static ST_pointf map_point(final ST_pointf p) {
// WARNING!! STRUCT
return map_point_w_(p.copy()).copy();
}
private static ST_pointf map_point_w_(final ST_pointf p) {
ENTERING("dajapw16wus3rwimkrk5ihi2b","map_point");
try {
    p.___(ccwrotatepf(p, Z.z().Rankdir * 90));
    p.setDouble("x", p.x - Z.z().Offset.x);
    p.setDouble("y", p.y - Z.z().Offset.y);
    return p;
} finally {
LEAVING("dajapw16wus3rwimkrk5ihi2b","map_point");
}
}




//3 bvq3vvonvotn47mfe5zsvchie
// static void map_edge(edge_t * e) 
public static void map_edge(ST_Agedge_s e) {
ENTERING("bvq3vvonvotn47mfe5zsvchie","map_edge");
try {
    int j, k;
    final ST_bezier bz = new ST_bezier();
    if (ED_spl(e) == null) {
	if ((Z.z().Concentrate == false) && (ED_edge_type(e) != 6))
	    System.err.println("lost %s %s edge\n"+ agnameof(agtail(e))+
		  agnameof(aghead(e)));
	return;
    }
    for (j = 0; j < ED_spl(e).size; j++) {
	bz.___(ED_spl(e).list.plus(j).getStruct());
	for (k = 0; k < bz.size; k++) {
	    bz.list.get(k).setStruct(map_point((ST_pointf) bz.list.get(k).getStruct()));
	}
	if (bz.sflag!=0)
UNSUPPORTED("7894dgzvk2um2w1a5ph2r0bcc"); // 	    ED_spl(e)->list[j].sp = map_point(ED_spl(e)->list[j].sp);
	if (bz.eflag!=0) {
	    ED_spl(e).list.get(j).ep.___(map_point((ST_pointf) ED_spl(e).list.get(j).ep));
    }
    }
    if (ED_label(e)!=null)
	ED_label(e).pos.___(map_point((ST_pointf) ED_label(e).pos));
    if (ED_xlabel(e)!=null)
UNSUPPORTED("al3tnq9zjjqeq1ll7qdxyu3ja"); // 	ED_xlabel(e)->pos = map_point(ED_xlabel(e)->pos);
    /* vladimir */
    if (ED_head_label(e)!=null)
    	ED_head_label(e).setStruct("pos", map_point((ST_pointf) ED_head_label(e).pos));
    if (ED_tail_label(e)!=null)
    	ED_tail_label(e).setStruct("pos", map_point((ST_pointf) ED_tail_label(e).pos));
} finally {
LEAVING("bvq3vvonvotn47mfe5zsvchie","map_edge");
}
}




//3 a3hf82rxsojxbunj6p8a6bkse
// void translate_bb(graph_t * g, int rankdir) 
public static void translate_bb(ST_Agraph_s g, int rankdir) {
ENTERING("a3hf82rxsojxbunj6p8a6bkse","translate_bb");
try {
    int c;
    final ST_boxf bb = new ST_boxf(), new_bb = new ST_boxf();
    bb.___(GD_bb(g));
    if (rankdir == 1 || rankdir == 2) {
UNSUPPORTED("d4wrtj0h7lkb0e0vernd9czq9"); // 	new_bb.LL = map_point(pointfof(bb.LL.x, bb.UR.y));
UNSUPPORTED("crysiae5zxc69cj3v2ygfs8xn"); // 	new_bb.UR = map_point(pointfof(bb.UR.x, bb.LL.y));
    } else {
	new_bb.LL.___(map_point(pointfof(bb.LL.x, bb.LL.y)));
	new_bb.UR.___(map_point(pointfof(bb.UR.x, bb.UR.y)));
    }
    GD_bb(g).___(new_bb);
    if (GD_label(g)!=null) {
	GD_label(g).setStruct("pos", map_point((ST_pointf) GD_label(g).pos));
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	translate_bb((ST_Agraph_s) GD_clust(g).get(c).getPtr(), rankdir);
} finally {
LEAVING("a3hf82rxsojxbunj6p8a6bkse","translate_bb");
}
}




//3 h4i5qxnd7hlrew919abswd13
// static void translate_drawing(graph_t * g) 
public static void translate_drawing(ST_Agraph_s g) {
ENTERING("h4i5qxnd7hlrew919abswd13","translate_drawing");
try {
    ST_Agnode_s v;
    ST_Agedge_s e;
    boolean shift = (Z.z().Offset.x!=0.0 || Z.z().Offset.y!=0.0);
    if (N(shift) && N(Z.z().Rankdir))
	return;
    for (v = agfstnode(g); v!=null; v = agnxtnode(g, v)) {
	if (Z.z().Rankdir!=0)
UNSUPPORTED("e0j848r4j1j7sojfht5gwikvi"); // 	    gv_nodesize(v, 0);
	ND_coord(v).___(map_point(ND_coord(v)));
	if (ND_xlabel(v)!=null)
UNSUPPORTED("3fy0l7w2v24hzrvlpstpknwl7"); // 	    ND_xlabel(v)->pos = map_point(ND_xlabel(v)->pos);
	if (Z.z().State == 1)
	    for (e = agfstout(g, v); e!=null; e = agnxtout(g, e))
		map_edge(e);
    }
    translate_bb(g, GD_rankdir(g));
} finally {
LEAVING("h4i5qxnd7hlrew919abswd13","translate_drawing");
}
}




//3 52s5qfdn5zxeyuodfq8tgxwaa
// static void place_root_label(graph_t * g, pointf d) 
public static Object place_root_label(Object... arg) {
UNSUPPORTED("8m0j9ogp2h20aphxjuxn7copg"); // static void place_root_label(graph_t * g, pointf d)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("2gqofhemdqlul2pxjvuyly8gr"); //     if (GD_label_pos(g) & 4) {
UNSUPPORTED("5y20n2wgxe7hnzxnkg9144bhl"); // 	p.x = GD_bb(g).UR.x - d.x / 2;
UNSUPPORTED("2892hrfudeikl7o70w4nofhny"); //     } else if (GD_label_pos(g) & 2) {
UNSUPPORTED("2wafl9f5ytdr13m270aknhkub"); // 	p.x = GD_bb(g).LL.x + d.x / 2;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("aktxwmc3uefegw0cs334i9dw8"); // 	p.x = (GD_bb(g).LL.x + GD_bb(g).UR.x) / 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dpev8t1rdycpdnwjscmowsvaf"); //     if (GD_label_pos(g) & 1) {
UNSUPPORTED("1d6n5uhjoy1y3mfj3ddrdt6rc"); // 	p.y = GD_bb(g).UR.y - d.y / 2;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("d6z40q4ju0vq45te2z2prcsua"); // 	p.y = GD_bb(g).LL.y + d.y / 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dgs75zvkk4komxfg61cia1t82"); //     GD_label(g)->pos = p;
UNSUPPORTED("8dkewv20cdne73egfv2tn37lb"); //     GD_label(g)->set = NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2i713kmewjct2igf3lwm80462
// static pointf centerPt (xlabel_t* xlp) 
public static ST_pointf centerPt(ST_xlabel_t.Array xlp) {
ENTERING("2i713kmewjct2igf3lwm80462","centerPt");
try {
   final ST_pointf p = new ST_pointf();
   p.___(xlp.getStruct().pos);
   p.setDouble("x", p.x + xlp.getStruct().sz.x/2);
   p.setDouble("y", p.y + xlp.getStruct().sz.y/2);
   return p;
} finally {
LEAVING("2i713kmewjct2igf3lwm80462","centerPt");
}
}




//3 2ozwtuocg4cz5ghqemmche3vt
// static int printData (object_t* objs, int n_objs, xlabel_t* lbls, int n_lbls, 	   label_params_t* params) 
public static Object printData(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("ckiu0galpa1852h3cty8ba6r7"); // printData (object_t* objs, int n_objs, xlabel_t* lbls, int n_lbls,
UNSUPPORTED("dzs60ka6anviw625w47fdcq2z"); // 	   label_params_t* params) {
UNSUPPORTED("327a0i7b6rqjqj8wdl0h29mnc"); //   int i;
UNSUPPORTED("2v2z0ng0lije7nd5xnnwpvhsk"); //   xlabel_t* xp;
UNSUPPORTED("8b4vu28i0jzto8xj8y5xidmfi"); //   fprintf (stderr, "%d objs %d xlabels force=%d bb=(%.02f,%.02f) (%.02f,%.02f)\n",
UNSUPPORTED("4brfvoc74eb0wrjbv0jim84br"); // 	   n_objs, n_lbls, params->force, params->bb.LL.x, params->bb.LL.y,
UNSUPPORTED("bg9iybxqjpzhrq7ake4qd022a"); // 	   params->bb.UR.x, params->bb.UR.y);
UNSUPPORTED("cqr6z8w761iogdatyx6tujxrj"); //   if (Verbose < 2) return 0;
UNSUPPORTED("3k661zowi8f52at88thx4gq3c"); //   fprintf(stderr, "objects\n");
UNSUPPORTED("a0du2scf7lcyviw12x1mpntj5"); //   for (i = 0; i < n_objs; i++) {
UNSUPPORTED("7ljpm4lk7dhgz8jneyiurqcqj"); //     xp = objs->lbl;
UNSUPPORTED("6synbitl0b8qre39c8hfx9ji7"); //     fprintf (stderr, " [%d] (%.02f,%.02f) (%.02f,%.02f) %p \"%s\"\n",
UNSUPPORTED("5fncs0x8f05ep5nr4lds4lyfi"); // 	    i, objs->pos.x,objs->pos.y,objs->sz.x,objs->sz.y, objs->lbl, 
UNSUPPORTED("csy4r7zdyqrnbp3yvfpm66w59"); // 	    (xp?((textlabel_t*)(xp->lbl))->text:""));
UNSUPPORTED("bd3gftycfqozopsoe1ptqueov"); //     objs++;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("2kq3xkqpgi5kr4141ab3bukkd"); //   fprintf(stderr, "xlabels\n");
UNSUPPORTED("9dzxzlqrz9th6wohjjvd93rtd"); //   for (i = 0; i < n_lbls; i++) {
UNSUPPORTED("6e093u52b50denwzlx0m7a36a"); //     fprintf (stderr, " [%d] %p set %d (%.02f,%.02f) (%.02f,%.02f) %s\n",
UNSUPPORTED("bb908gbwccyotc72hfeopef2e"); // 	     i, lbls,  lbls->set, lbls->pos.x,lbls->pos.y, lbls->sz.x,lbls->sz.y, ((textlabel_t*)lbls->lbl)->text);  
UNSUPPORTED("30hn2ulrrh2sxlt3rd89e1nja"); //     lbls++;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("bid671dovx1rdiquw5vm3fttj"); //   return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 95pnpuiq4khinrz2bqkci9nfg
// static pointf edgeTailpoint (Agedge_t* e) 
public static ST_pointf edgeTailpoint(ST_Agedge_s e) {
ENTERING("95pnpuiq4khinrz2bqkci9nfg","edgeTailpoint");
try {
	return edgeTailpoint_(e).copy();
} finally {
LEAVING("95pnpuiq4khinrz2bqkci9nfg","edgeTailpoint");
}
}
private static ST_pointf edgeTailpoint_(ST_Agedge_s e) {
	ST_splines spl;
	ST_bezier bez;
	spl = getsplinepoints(e);
	if (spl == null) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("ezy0ey6dn5uqp6peuorn615x6"); // 	p.x = p.y = 0;
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
    }
	bez = spl.list.get(0);
    //     bez = &spl->list[0];
if (bez.sflag!=0) {
	return (ST_pointf) bez.sp;
} else {
	return (ST_pointf) (bez.getPtr()).list.get(0);
   // 	return bez->list[0];
 }

}




//3 av67wf2xi70ncgl90j1ttrjjs
// static pointf edgeHeadpoint (Agedge_t* e) 
public static ST_pointf edgeHeadpoint(ST_Agedge_s e) {
ENTERING("av67wf2xi70ncgl90j1ttrjjs","edgeHeadpoint");
try {
	return edgeHeadpoint_(e).copy();
} finally {
LEAVING("av67wf2xi70ncgl90j1ttrjjs","edgeHeadpoint");
}
}
private static ST_pointf edgeHeadpoint_(ST_Agedge_s e) {
	ST_splines spl;
	ST_bezier bez;
	spl = getsplinepoints(e);
if (spl == null) {
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("ezy0ey6dn5uqp6peuorn615x6"); // 	p.x = p.y = 0;
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
}
bez = spl.list.plus(spl.size - 1).getPtr();
//     bez = &spl->list[spl->size - 1];
if (bez.eflag!=0) {
	return (ST_pointf) bez.ep;
} else {
	return (ST_pointf) ((ST_bezier)bez.getPtr()).list.plus(bez.size - 1).getStruct();
// 	return bez->list[bez->size - 1];
}
}




//3 1ca6fh8ns5bgzfzcz8al4eh4k
// static boxf adjustBB (object_t* objp, boxf bb) 
public static ST_boxf adjustBB(ST_object_t.Array objp, ST_boxf bb) {
ENTERING("1ca6fh8ns5bgzfzcz8al4eh4k","adjustBB");
try {
	return adjustBB_(objp, bb.copy()).copy();
} finally {
LEAVING("1ca6fh8ns5bgzfzcz8al4eh4k","adjustBB");
}
}
private static ST_boxf adjustBB_(ST_object_t.Array objp, ST_boxf bb) {
	final ST_pointf ur = new ST_pointf();
	/* Adjust bounding box */
	bb.LL.setDouble("x", MIN(bb.LL.x, objp.getStruct().pos.x));
	bb.LL.setDouble("y", MIN(bb.LL.y, objp.getStruct().pos.y));
	ur.setDouble("x", objp.getStruct().pos.x + objp.getStruct().sz.x);
	ur.setDouble("y", objp.getStruct().pos.y + objp.getStruct().sz.y);
	bb.UR.setDouble("x", MAX(bb.UR.x, ur.x));
	bb.UR.setDouble("y", MAX(bb.UR.y, ur.y));
	return bb;
}




//3 3mefe722uemyoa0czmkkw6hjb
// static void addXLabel (textlabel_t* lp, object_t* objp, xlabel_t* xlp, int initObj, pointf pos) 
public static void addXLabel(ST_textlabel_t lp, ST_object_t.Array objp, ST_xlabel_t.Array xlp, int initObj, ST_pointf pos) {
ENTERING("3mefe722uemyoa0czmkkw6hjb","addXLabel");
try {
	addXLabel_(lp, objp, xlp, initObj, pos.copy());
} finally {
LEAVING("3mefe722uemyoa0czmkkw6hjb","addXLabel");
}
}
private static void addXLabel_(ST_textlabel_t lp, ST_object_t.Array objp, ST_xlabel_t.Array xlp, int initObj, ST_pointf pos) {
if (initObj!=0) {
	objp.getStruct().sz.setDouble("x", 0);
	objp.getStruct().sz.setDouble("y", 0);
	objp.setStruct("pos", pos);
}
if (Z.z().Flip) {
UNSUPPORTED("99tzt7erbvtfsbo0jbdz0lc8m"); // 	xlp->sz.x = lp->dimen.y;
UNSUPPORTED("6v5t3ysaisj27bwc0r9zg3rpd"); // 	xlp->sz.y = lp->dimen.x;
}
else {
	xlp.get(0).sz.___(lp.dimen);
}
xlp.get(0).lbl = lp;
xlp.get(0).set = 0;
objp.setPtr("lbl", xlp);
}




//3 dwxd5kvlanbcxqfuncjg0ea54
// static boxf addLabelObj (textlabel_t* lp, object_t* objp, boxf bb) 
public static ST_boxf addLabelObj(ST_textlabel_t lp, ST_object_t.Array objp, final ST_boxf bb) {
	// WARNING!! STRUCT
	return addLabelObj_(lp, objp, bb.copy()).copy();
}
private static ST_boxf addLabelObj_(ST_textlabel_t lp, ST_object_t.Array objp, final ST_boxf bb) {
	ENTERING("dwxd5kvlanbcxqfuncjg0ea54","addLabelObj");
	try {
		if (Z.z().Flip) {
UNSUPPORTED("6z2yrwq81gtsk3q9c5pofow1x"); // 	objp->sz.x = lp->dimen.y; 
UNSUPPORTED("8xsm9kavrekjrsydqe1wh1pu"); // 	objp->sz.y = lp->dimen.x;
		}
		else {
			objp.getStruct().sz.setDouble("x", lp.dimen.x);
			objp.getStruct().sz.setDouble("y", lp.dimen.y);
		}
	objp.setStruct("pos", lp.pos);
	objp.getStruct().pos.setDouble("x", objp.getStruct().pos.x - (objp.getStruct().sz.x / 2.0 )); 
	objp.getStruct().pos.setDouble("y", objp.getStruct().pos.y - (objp.getStruct().sz.y / 2.0 )); 
	return adjustBB(objp, bb);
		} finally {
			LEAVING("dwxd5kvlanbcxqfuncjg0ea54","addLabelObj");
		}
}




//3 b8tjygxnwny5qoiir1mha1d62
// static boxf addNodeObj (node_t* np, object_t* objp, boxf bb) 
public static ST_boxf addNodeObj(ST_Agnode_s np, ST_object_t.Array objp, final ST_boxf bb) {
	// WARNING!! STRUCT
	return addNodeObj_(np, objp, bb.copy()).copy();
}
public static ST_boxf addNodeObj_(ST_Agnode_s np, ST_object_t.Array objp, final ST_boxf bb) {
ENTERING("b8tjygxnwny5qoiir1mha1d62","map_point");
try {
	if (Z.z().Flip) {
	UNSUPPORTED("1ri5uimcd1z58iix8tp528l1m"); // 	objp->sz.x = ((ND_height(np))*(double)72);
	UNSUPPORTED("6r5gwwhz3sjxrssh8yo3v5c3v"); // 	objp->sz.y = ((ND_width(np))*(double)72);
	}
	else {
		  objp.getStruct().sz.setDouble("x", ((ND_width(np))*(double)72));
		  objp.getStruct().sz.setDouble("y", ((ND_height(np))*(double)72));
	}
	objp.setPtr("pos", ND_coord(np));
	objp.getStruct().pos.setDouble("x", objp.getStruct().pos.x - objp.getStruct().sz.x / 2.0);
	objp.getStruct().pos.setDouble("y", objp.getStruct().pos.y - objp.getStruct().sz.y / 2.0);
	return adjustBB(objp, bb);
} finally {
	LEAVING("dajapw16wus3rwimkrk5ihi2b","map_point");
}
}




//3 6kx3lin2ig9o2otk2bqzdvd4t
// static cinfo_t addClusterObj (Agraph_t* g, cinfo_t info) 
public static ST_cinfo_t addClusterObj(ST_Agraph_s g, ST_cinfo_t info) {
ENTERING("6kx3lin2ig9o2otk2bqzdvd4t","addClusterObj");
try {
	return addClusterObj_(g, info.copy()).copy();
} finally {
LEAVING("6kx3lin2ig9o2otk2bqzdvd4t","addClusterObj");
}
}
private static ST_cinfo_t addClusterObj_(ST_Agraph_s g, ST_cinfo_t info) {
     int c;
     for (c = 1; c <= GD_n_cluster(g); c++)
 	info.___(addClusterObj ((ST_Agraph_s)GD_clust(g).get(c).castTo(ST_Agraph_s.class), info));
     if (NEQ(g, agroot(g)) && (GD_label(g)!=null) && GD_label(g).set!=0) {
    	 ST_object_t.Array objp = info.objp;
    	 info.setStruct("bb", addLabelObj (GD_label(g), objp, (ST_boxf) info.bb));
    	 info.objp = info.objp.plus(1);
//UNSUPPORTED("dcgq2zlh4t0m1gno12t6h7ouy"); // 	object_t* objp = info.objp;
//UNSUPPORTED("ddz79zm5235krd6smukq1gza0"); // 	info.bb = addLabelObj (GD_label(g), objp, info.bb);
//UNSUPPORTED("be25qc3x3muxo4l87ji01t3kd"); // 	info.objp++;
     }
     return info;
}




//3 2tdbzvdtkwxp75kj0iufsynm5
// static int countClusterLabels (Agraph_t* g) 
public static int countClusterLabels(ST_Agraph_s g) {
ENTERING("2tdbzvdtkwxp75kj0iufsynm5","countClusterLabels");
try {
     int c, i = 0;
     if (NEQ(g, agroot(g)) && GD_label(g)!=null && GD_label(g).set!=0)
    	 i++;
     for (c = 1; c <= GD_n_cluster(g); c++)
			i += countClusterLabels((ST_Agraph_s) GD_clust(g).plus(c).castTo(ST_Agraph_s.class));
     return i;
} finally {
LEAVING("2tdbzvdtkwxp75kj0iufsynm5","countClusterLabels");
}

}




//3 d4215jd9wukfn6t0iknwzjcof
// static void addXLabels(Agraph_t * gp) 
public static void addXLabels(ST_Agraph_s gp) {
ENTERING("d4215jd9wukfn6t0iknwzjcof","addXLabels");
try {
    ST_Agnode_s np;
    ST_Agedge_s ep;
    int cnt, i, n_objs, n_lbls;
    int n_nlbls = 0;		/* # of unset node xlabels */
    int n_elbls = 0;		/* # of unset edge labels or xlabels */
    int n_set_lbls = 0;		/* # of set xlabels and edge labels */
    int n_clbls = 0;		/* # of set cluster labels */
    final ST_boxf bb = new ST_boxf();
    final ST_pointf ur = new ST_pointf();
    ST_textlabel_t lp;
    final ST_label_params_t params = new ST_label_params_t();
    ST_object_t.Array objs;
    ST_xlabel_t.Array lbls;
    ST_object_t.Array objp;
    ST_xlabel_t.Array xlp;
    ST_Agsym_s force;
    int et = (GD_flags(gp) & (7 << 1));
    if (N(GD_has_labels(gp) & (1 << 4)) &&
	N(GD_has_labels(gp) & (1 << 5)) &&
	N(GD_has_labels(gp) & (1 << 2)) &&
	N(GD_has_labels(gp) & (1 << 1)) &&
	(N(GD_has_labels(gp) & (1 << 0)) || Z.z().EdgeLabelsDone!=0))
	return;
	for (np = agfstnode(gp); np!=null; np = agnxtnode(gp, np)) {
	if (ND_xlabel(np)!=null) {
UNSUPPORTED("6oje33bnpp4jv5mclsrrhl005"); // 	    if (ND_xlabel(np)->set)
UNSUPPORTED("cfkrw6t4lrs7dfgx86sgrz26"); // 		n_set_lbls++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("26eewzzknvqt2nbcrqds5fmti"); // 		n_nlbls++;
	}
	for (ep = agfstout(gp, np); ep!=null; ep = agnxtout(gp, ep)) {
		if (ED_xlabel(ep)!=null) {
UNSUPPORTED("appkettxihy2o612jk6fahbnh"); // 		if (ED_xlabel(ep)->set)
UNSUPPORTED("8k2rclvg6eaoph9r2pz4620xq"); // 		    n_set_lbls++;
UNSUPPORTED("14y6caappoxe17mogr979qf75"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL)))
UNSUPPORTED("q3t8uxncrxc4n8rtuabtzxya"); // 		    n_elbls++;
		}
		if (ED_head_label(ep)!=null) {
			if (ED_head_label(ep).set!=0)
				n_set_lbls++;
			else if (((et != (0 << 1)) && (ED_spl(ep) != null)))
				n_elbls++;
		}
		if (ED_tail_label(ep)!=null) {
			if (ED_tail_label(ep).set!=0)
				n_set_lbls++;
			else if (((et != (0 << 1)) && (ED_spl(ep) != null)))
				n_elbls++;
		}
		if (ED_label(ep)!=null) {
			if (ED_label(ep).set!=0)
				n_set_lbls++;
			else if (((et != (0 << 1)) && (ED_spl(ep) != null)))
				n_elbls++;
		}
		}
	}
	if ((GD_has_labels(gp) & (1 << 3))!=0)
		n_clbls = countClusterLabels (gp);
	/* A label for each unpositioned external label */
	n_lbls = n_nlbls + n_elbls;
	if (n_lbls == 0) return;
	/* An object for each node, each positioned external label, any cluster label, 
	 * and all unset edge labels and xlabels.
	 */
	n_objs = agnnodes(gp) + n_set_lbls + n_clbls + n_elbls;
	objs = new ST_object_t.Array(n_objs);
	objp = objs;
	lbls = new ST_xlabel_t.Array(n_lbls);
	xlp = lbls;
	bb.setStruct("LL", pointfof(INT_MAX, INT_MAX));
	bb.setStruct("UR", pointfof(-INT_MAX, -INT_MAX));
	for (np = agfstnode(gp); np!=null; np = agnxtnode(gp, np)) {
		bb.___(addNodeObj (np, objp, bb));
		lp = ND_xlabel(np);
		if (lp != null) {
UNSUPPORTED("d5pjy3dwui27jfdz550cy0cln"); // 	    if (lp->set) {
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
UNSUPPORTED("3zy3jhlqyioeyh9mlrspjjgc6"); // 		bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2msn58w2dse7pbq2esv7awk4r"); // 		addXLabel (lp, objp, xlp, 0, ur); 
UNSUPPORTED("1zpq9rd3nn9kjrmun8ivs9zx5"); // 		xlp++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
    }
	objp = objp.plus(1);
    for (ep = agfstout(gp, np); ep!=null; ep = agnxtout(gp, ep)) {
    	lp = ED_label(ep);
    	if (lp != null) {
    		if (lp.set!=0) {
    			bb.___(addLabelObj (lp, objp, bb));
    	    }
    		else if (((et != (0 << 1)) && (ED_spl(ep) != null))) {
 		    addXLabel (lp, objp, xlp, 1, edgeMidpoint(gp, ep)); 
 		    xlp = xlp.plus(1);
}
else {
UNSUPPORTED("3ia66n3hqrwmh3hybkoh6f8wa"); // 		    agerr(AGWARN, "no position for edge with label %s",
UNSUPPORTED("9npeksy1st7v005znerttzzzv"); // 			    ED_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
}
objp = objp.plus(1);
    	}
    	lp = ED_tail_label(ep);
    	if (lp != null) {
if (lp.set!=0) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
}
 		else if (((et != (0 << 1)) && (ED_spl(ep) != null))) {
 		    addXLabel (lp, objp, xlp, 1, edgeTailpoint(ep)); 
 		    xlp = xlp.plus(1);
 		}
 		else {
UNSUPPORTED("5ixexxcbcix5hrfl43td7pj4s"); // 		    agerr(AGWARN, "no position for edge with tail label %s",
UNSUPPORTED("cf9qaysecgkvv4165la4uu6cb"); // 			    ED_tail_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
 		}
			objp = objp.plus(1);
    	}
    	lp = ED_head_label(ep);
    	if (lp != null) {
if (lp.set!=0) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
}
else if (((et != (0 << 1)) && (ED_spl(ep) != null))) {
addXLabel (lp, objp, xlp, 1, edgeHeadpoint(ep));
xlp = xlp.plus(1);
}
else {
UNSUPPORTED("8nrkavpg9ifts9yylhfijn9rp"); // 		    agerr(AGWARN, "no position for edge with head label %s",
UNSUPPORTED("a5omwtwd411hsfrc37d8t6m8b"); // 			    ED_head_label(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
}
		objp = objp.plus(1);
    }
    lp = ED_xlabel(ep);
    if (lp != null) {
UNSUPPORTED("5dapykbxjvnhw0dpi7jfpcazk"); // 		if (lp->set) {
UNSUPPORTED("7rwrlod7lkgin3rnnzy3iw2rw"); // 		    bb = addLabelObj (lp, objp, bb);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dfnmpe0hri6ksye0gnxssi4zz"); // 		else if (((et != (0 << 1)) && (ED_spl(ep) != NULL))) {
UNSUPPORTED("9ffmrymv8cg4h4b3ea97t9qbp"); // 		    addXLabel (lp, objp, xlp, 1, edgeMidpoint(gp, ep)); 
UNSUPPORTED("808184nt3k6cxj5dsg27yvpvg"); // 		    xlp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("dbskad3xgu5oqqhwl1cr9f88g"); // 		    agerr(AGWARN, "no position for edge with xlabel %s",
UNSUPPORTED("dtpynjioyrbt2xfca2o46eb0j"); // 			    ED_xlabel(ep)->text);
UNSUPPORTED("2yi9az7ibt7j9bwztjilyo0v2"); // 		    continue;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("cls7z8l7wi371a4wrec0viqil"); // 		objp++;
    	}
    }
}
if (n_clbls!=0) {
    final ST_cinfo_t info = new ST_cinfo_t();
    info.setStruct("bb", bb);
    info.objp = objp;
    info.___(addClusterObj (gp, info));
    bb.___(info.bb);
}
force = (agattr(gp,AGRAPH,new CString("forcelabels"),null));
params.force = late_bool(gp, force, 1);
params.setStruct("bb", bb);
placeLabels(objs, n_objs, lbls, n_lbls, params);
//     if (Verbose)
// 	printData(objs, n_objs, lbls, n_lbls, &params);
     xlp = lbls;
     cnt = 0;
     for (i = 0; i < n_lbls; i++) {
 	if (xlp.get(0).set!=0) {
 	    cnt++;
 	    lp = xlp.getStruct().lbl;
 	    lp.setInt("set", 1);
 	    lp.setStruct("pos", centerPt(xlp));
 	    updateBB (gp, lp);
 	}
 	xlp = xlp.plus(1);
     }
//     if (Verbose)
// 	fprintf (stderr, "%d out of %d labels positioned.\n", cnt, n_lbls);
if (cnt != n_lbls)
UNSUPPORTED("9hqu9h8q1a2xl4ty48ct0fdyp"); // 	agerr(AGWARN, "%d out of %d exterior labels positioned.\n", cnt, n_lbls);
	 Memory.free(objs);
     Memory.free(lbls);
} finally {
LEAVING("d4215jd9wukfn6t0iknwzjcof","addXLabels");
}
}




//3 8fc0zxg8y7hec3n4evx3jw6cq
// void gv_postprocess(Agraph_t * g, int allowTranslation) 
public static void gv_postprocess(ST_Agraph_s g, int allowTranslation) {
ENTERING("8fc0zxg8y7hec3n4evx3jw6cq","gv_postprocess");
try {
    double diff;
    final ST_pointf dimen = new ST_pointf();
    Z.z().Rankdir = GD_rankdir(g);
    Z.z().Flip = GD_flip(g)!=0;
    /* Handle cluster labels */
    if (Z.z().Flip)
UNSUPPORTED("4hxky2sp978rmy6018sfmts6m"); // 	place_flip_graph_label(g);
    else
	place_graph_label(g);
    /* Everything has been placed except the root graph label, if any.
     * The graph positions have not yet been rotated back if necessary.
     */
    addXLabels(g);
    /* Add space for graph label if necessary */
    if (GD_label(g)!=null && N(GD_label(g).set)) {
UNSUPPORTED("crj0py2wme4b5l8apvbxqcmqa"); // 	dimen = GD_label(g)->dimen;
UNSUPPORTED("22jhn709g4c5wh0gb6v40rh19"); // 	{((dimen).x += 4*4); ((dimen).y += 2*4);};
UNSUPPORTED("9k69y89jybam5elefg45va3ey"); // 	if (Flip) {
UNSUPPORTED("andsvpqa42ef9h5dkn3uyv6tj"); // 	    if (GD_label_pos(g) & 1) {
UNSUPPORTED("65ggem18g4zgz2yx552vi2n4v"); // 		GD_bb(g).UR.x += dimen.y;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("9dm14vohn1tuwqrwprpdywylr"); // 		GD_bb(g).LL.x -= dimen.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("94nnj9ien92542qanqtyo8qzq"); // 	    if (dimen.x > (GD_bb(g).UR.y - GD_bb(g).LL.y)) {
UNSUPPORTED("awekuk9gokwsbb49j41hvhqt4"); // 		diff = dimen.x - (GD_bb(g).UR.y - GD_bb(g).LL.y);
UNSUPPORTED("5856jxlve8fb2pennnazjjkij"); // 		diff = diff / 2.;
UNSUPPORTED("3t8m6fustsc50cpggxiadcjax"); // 		GD_bb(g).LL.y -= diff;
UNSUPPORTED("7c25kl7mn9jd5x5x2uflcql86"); // 		GD_bb(g).UR.y += diff;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("andsvpqa42ef9h5dkn3uyv6tj"); // 	    if (GD_label_pos(g) & 1) {
UNSUPPORTED("7x8evhhttjy9mwgcpfpb3l7lm"); // 		if (Rankdir == 0)
UNSUPPORTED("45a42yl5qj83sj5mzdd6k6wcj"); // 		    GD_bb(g).UR.y += dimen.y;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("6i55hrio04eg5ilg5i01jw8vv"); // 		    GD_bb(g).LL.y -= dimen.y;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("7x8evhhttjy9mwgcpfpb3l7lm"); // 		if (Rankdir == 0)
UNSUPPORTED("6i55hrio04eg5ilg5i01jw8vv"); // 		    GD_bb(g).LL.y -= dimen.y;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("45a42yl5qj83sj5mzdd6k6wcj"); // 		    GD_bb(g).UR.y += dimen.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("p1mrpzvl0adzwcw3lialn8v3"); // 	    if (dimen.x > (GD_bb(g).UR.x - GD_bb(g).LL.x)) {
UNSUPPORTED("3ie0x59qavcqpnvy7kci31lgc"); // 		diff = dimen.x - (GD_bb(g).UR.x - GD_bb(g).LL.x);
UNSUPPORTED("5856jxlve8fb2pennnazjjkij"); // 		diff = diff / 2.;
UNSUPPORTED("anqdsrkl2qs1pqbuivrdz6fnt"); // 		GD_bb(g).LL.x -= diff;
UNSUPPORTED("c0ah0pvnkczqdg5jt0u955wns"); // 		GD_bb(g).UR.x += diff;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    if (allowTranslation!=0) {
	switch (Z.z().Rankdir) {
	case 0:
	    Z.z().Offset.___(GD_bb(g).LL);
	    break;
	case 1:
UNSUPPORTED("5t3j9lrc86kd5ouaqgzvz3qcm"); // 	    Offset = pointfof(-GD_bb(g).UR.y, GD_bb(g).LL.x);
	    break;
	case 2:
UNSUPPORTED("96ajwnh79ja9g57xmut6dmh9d"); // 	    Offset = pointfof(GD_bb(g).LL.x, -GD_bb(g).UR.y);
	    break;
	case 3:
UNSUPPORTED("3xtu7zkpqq7nsx9oe68oi6ebt"); // 	    Offset = pointfof(GD_bb(g).LL.y, GD_bb(g).LL.x);
	    break;
	}
	translate_drawing(g);
    }
    if (GD_label(g)!=null && N(GD_label(g).set))
UNSUPPORTED("6dds0zsvqw48u510zcy954fh1"); // 	place_root_label(g, dimen);
    if (Z.z().Show_boxes!=null) {
UNSUPPORTED("8c7x8di5w36ib05qan9z4sl9"); // 	char buf[BUFSIZ];
UNSUPPORTED("83qqprhiseoxlwtwi991aag0c"); // 	if (Flip)
UNSUPPORTED("86tova7pv19alt02nlk0d17oj"); // 	    sprintf(buf, "/pathbox {\n    /X exch neg %.5g sub def\n    /Y exch %.5g sub def\n    /x exch neg %.5g sub def\n    /y exch %.5g sub def\n    newpath x y moveto\n    X y lineto\n    X Y lineto\n    x Y lineto\n    closepath stroke\n} def\n", Offset.x, Offset.y, Offset.x, Offset.y);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("79tu9xkxv4v48uko4cxz7v04t"); // 	    sprintf(buf, "/pathbox {\n    /Y exch %.5g sub def\n    /X exch %.5g sub def\n    /y exch %.5g sub def\n    /x exch %.5g sub def\n    newpath x y moveto\n    X y lineto\n    X Y lineto\n    x Y lineto\n    closepath stroke\n } def\n/dbgstart { gsave %.5g %.5g translate } def\n/arrowlength 10 def\n/arrowwidth arrowlength 2 div def\n/arrowhead {\n    gsave\n    rotate\n    currentpoint\n    newpath\n    moveto\n    arrowlength arrowwidth 2 div rlineto\n    0 arrowwidth neg rlineto\n    closepath fill\n    grestore\n} bind def\n/makearrow {\n    currentpoint exch pop sub exch currentpoint pop sub atan\n    arrowhead\n} bind def\n/point {    newpath    2 0 360 arc fill} def/makevec {\n    /Y exch def\n    /X exch def\n    /y exch def\n    /x exch def\n    newpath x y moveto\n    X Y lineto stroke\n    X Y moveto\n    x y makearrow\n} def\n", Offset.y, Offset.x, Offset.y, Offset.x,
UNSUPPORTED("aow79vde4xjqtwexymr5ocjl6"); // 		    -Offset.x, -Offset.y);
UNSUPPORTED("6g3g36v7l0tyfootyy8mzv3t8"); // 	Show_boxes[0] = strdup(buf);
    }
} finally {
LEAVING("8fc0zxg8y7hec3n4evx3jw6cq","gv_postprocess");
}
}




//3 3qbbvlnq1b06ylgr0yj2slbhm
// void dotneato_postprocess(Agraph_t * g) 
public static void dotneato_postprocess(ST_Agraph_s g) {
ENTERING("3qbbvlnq1b06ylgr0yj2slbhm","dotneato_postprocess");
try {
    gv_postprocess(g, 1);
} finally {
LEAVING("3qbbvlnq1b06ylgr0yj2slbhm","dotneato_postprocess");
}
}




//3 ehe7n8wkl4thn86tisjzdotpq
// static void place_flip_graph_label(graph_t * g) 
public static Object place_flip_graph_label(Object... arg) {
UNSUPPORTED("3zsjtcmcfxhkmaagi0on4dy20"); // static void place_flip_graph_label(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("1wiyv8zjadpaaw6l7rs3o7g1w"); //     pointf p, d;
UNSUPPORTED("3aye33sd5gwxvun5g9nvgb2py"); //     if ((g != agroot(g)) && (GD_label(g)) && !GD_label(g)->set) {
UNSUPPORTED("bb9kbz7bijh4xjt97fdn2q90k"); // 	if (GD_label_pos(g) & 1) {
UNSUPPORTED("9fqnrxwwa66oa8qe31y1gf37u"); // 	    d = GD_border(g)[1];
UNSUPPORTED("cgv3bcg9c274cdwxi1y0sja3p"); // 	    p.x = GD_bb(g).UR.x - d.x / 2;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("16roor3488xb2g2wr6oh8dqpx"); // 	    d = GD_border(g)[3];
UNSUPPORTED("7ictv9eqmjvxjii5lqlyw8nu"); // 	    p.x = GD_bb(g).LL.x + d.x / 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2qpji9cqj2p2czgcug3wvnqpl"); // 	if (GD_label_pos(g) & 4) {
UNSUPPORTED("2xa4n9ca16xpf1kahaycmkl4r"); // 	    p.y = GD_bb(g).LL.y + d.y / 2;
UNSUPPORTED("blrmgi2c43f98h1nso1k757hi"); // 	} else if (GD_label_pos(g) & 2) {
UNSUPPORTED("a7anlx7s8s2pqd73q59ep0kpf"); // 	    p.y = GD_bb(g).UR.y - d.y / 2;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("378pj84d79yuezjebpqcchyut"); // 	    p.y = (GD_bb(g).LL.y + GD_bb(g).UR.y) / 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ptjqgn3loi94u957cup0fi1"); // 	GD_label(g)->pos = p;
UNSUPPORTED("5ezl5j9dxa3ewoj8hxw72wn4n"); // 	GD_label(g)->set = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("d388zyk4c9le0jg4fcfx0b2"); // 	place_flip_graph_label(GD_clust(g)[c]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 72zw1alhd5vd0g6mhum507rvx
// void place_graph_label(graph_t * g) 
public static void place_graph_label(ST_Agraph_s g) {
ENTERING("72zw1alhd5vd0g6mhum507rvx","place_graph_label");
try {
    int c;
    final ST_pointf p = new ST_pointf(), d = new ST_pointf();
    if (NEQ(g, agroot(g)) && (GD_label(g)!=null) && N(GD_label(g).set)) {
	if ((GD_label_pos(g) & 1)!=0) {
	    d.___(GD_border(g)[2].getStruct());
	    p.setDouble("y", GD_bb(g).UR.y - d.y / 2);
	} else {
UNSUPPORTED("1w38no4welthbwa0i10hei16b"); // 	    d = GD_border(g)[0];
UNSUPPORTED("2xa4n9ca16xpf1kahaycmkl4r"); // 	    p.y = GD_bb(g).LL.y + d.y / 2;
	}
	if ((GD_label_pos(g) & 4)!=0) {
UNSUPPORTED("cgv3bcg9c274cdwxi1y0sja3p"); // 	    p.x = GD_bb(g).UR.x - d.x / 2;
	} else if ((GD_label_pos(g) & 2)!=0) {
UNSUPPORTED("7ictv9eqmjvxjii5lqlyw8nu"); // 	    p.x = GD_bb(g).LL.x + d.x / 2;
	} else {
	    p.setDouble("x", (GD_bb(g).LL.x + GD_bb(g).UR.x) / 2);
	}
	GD_label(g).setStruct("pos", p);
	GD_label(g).set= NOTI(false);
    }
    for (c = 1; c <= GD_n_cluster(g); c++)
	place_graph_label((ST_Agraph_s) GD_clust(g).get(c).getPtr());
} finally {
LEAVING("72zw1alhd5vd0g6mhum507rvx","place_graph_label");
}
}


}
