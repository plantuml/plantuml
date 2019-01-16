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
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agsubedge;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agcontains;
import static gen.lib.cgraph.obj__c.agdelete;
import static gen.lib.cgraph.subg__c.agfstsubg;
import static gen.lib.cgraph.subg__c.agnxtsubg;
import static gen.lib.common.input__c.do_graph_label;
import static gen.lib.common.ns__c.rank;
import static gen.lib.common.utils__c.UF_find;
import static gen.lib.common.utils__c.UF_singleton;
import static gen.lib.common.utils__c.UF_union;
import static gen.lib.common.utils__c.maptoken;
import static gen.lib.dotgen.acyclic__c.acyclic_;
import static gen.lib.dotgen.aspect__c.rank3;
import static gen.lib.dotgen.class1__c.class1_;
import static gen.lib.dotgen.decomp__c.decompose;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.strncmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_leader;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_maxset;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_minset;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_parent;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.GD_set_type;
import static smetana.core.Macro.MAXSHORT;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.ZALLOC_ST_Agraph_s;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_aspect_t;
import h.ST_elist;
import h.ST_point;
import h.ST_pointf;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;

public class rank__c {
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




//3 3f1re3nfkhxwjjb90kppwuupr
// static void  renewlist(elist * L) 
public static void renewlist(ST_elist L) {
ENTERING("3f1re3nfkhxwjjb90kppwuupr","renewlist");
try {
    int i;
    for (i = L.size; i >= 0; i--)
	L.list.set(i, null);
    L.size = 0;
} finally {
LEAVING("3f1re3nfkhxwjjb90kppwuupr","renewlist");
}
}




//3 1xov2qhuxj1f9nbzu3xsa6679
// static void  cleanup1(graph_t * g) 
public static void cleanup1(ST_Agraph_s g) {
ENTERING("1xov2qhuxj1f9nbzu3xsa6679","cleanup1");
try {
    ST_Agnode_s n;
    ST_Agedge_s e, f;
    int c;
    for (c = 0; c < GD_comp(g).size; c++) {
    	GD_nlist(g, GD_comp(g).getFromList(c));
	for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	    renewlist(ND_in(n));
	    renewlist(ND_out(n));
	    ND_mark(n, 0);
	}
    }
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
	    f = ED_to_virt(e);
	    /* Null out any other references to f to make sure we don't 
	     * handle it a second time. For example, parallel multiedges 
	     * share a virtual edge.
	     */
	    if (f!=null && (EQ(e, ED_to_orig(f)))) {
		ST_Agedge_s e1, f1;
		ST_Agnode_s n1;
		for (n1 = agfstnode(g); n1!=null; n1 = agnxtnode(g, n1)) {
		    for (e1 = agfstout(g, n1); e1!=null; e1 = agnxtout(g, e1)) {
			if (NEQ(e, e1)) {
			    f1 = ED_to_virt(e1);
			    if (f1!=null && EQ(f, f1)) {
				ED_to_virt(e1, null);
			    }
			}
		    }
		}
		Memory.free(f.base.data);
		Memory.free(f);
	    }
	    ED_to_virt(e, null);
	}
    }
    GD_comp(g).resetList();
    GD_comp(g).size = 0;
} finally {
LEAVING("1xov2qhuxj1f9nbzu3xsa6679","cleanup1");
}
}




//3 bxjf5g7g953ii1hfodl1j0y4u
// static void  edgelabel_ranks(graph_t * g) 
public static void edgelabel_ranks(ST_Agraph_s g) {
ENTERING("bxjf5g7g953ii1hfodl1j0y4u","edgelabel_ranks");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    if ((GD_has_labels(g) & (1 << 0))!=0) {
	for (n = agfstnode(g); n!=null; n = agnxtnode(g, n))
	    for (e = agfstout(g, n); e!=null; e = agnxtout(g, e))
		ED_minlen(e, ED_minlen(e) * 2);
	GD_ranksep(g, (GD_ranksep(g) + 1) / 2);
    }
} finally {
LEAVING("bxjf5g7g953ii1hfodl1j0y4u","edgelabel_ranks");
}
}




//3 9kjpoxcxoy3nhqd9rflwclo7c
// static void  collapse_rankset(graph_t * g, graph_t * subg, int kind) 
public static Object collapse_rankset(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("8hizp29cxh1rnp84yrlv4nl8x"); // collapse_rankset(graph_t * g, graph_t * subg, int kind)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5yrhx4blosxo5xnc1nh1kzhfs"); //     node_t *u, *v;
UNSUPPORTED("nbvmqthk0lqbm00ekylf0l0g"); //     u = v = agfstnode(subg);
UNSUPPORTED("5q27ub494lpst2s18bizunri0"); //     if (u) {
UNSUPPORTED("97vrl7utckj5ct78d81xyhhjl"); // 	ND_ranktype(u) = kind;
UNSUPPORTED("99ruvdyom1mcyir0v7i8zq8eh"); // 	while ((v = agnxtnode(subg, v))) {
UNSUPPORTED("7f9cf0wfrirgdoty4qy5pfuj9"); // 	    UF_union(u, v);
UNSUPPORTED("5jt25she9etuqjk6nrkrt3059"); // 	    ND_ranktype(v) = ND_ranktype(u);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9ty5l2g646lrkxz43tcnhcsk8"); // 	switch (kind) {
UNSUPPORTED("b8vgbvwzllfs4lrqmmqyr1spk"); // 	case 2:
UNSUPPORTED("1640m8as34e90xhvvtl877cmo"); // 	case 3:
UNSUPPORTED("2crlxhvtrgd5ohsriopqywv1m"); // 	    if (GD_minset(g) == NULL)
UNSUPPORTED("9py54j3v52y5qevrsi1omdoq7"); // 		GD_minset(g) = u;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("1cqwn2xb41g0dsm2oltj15dsd"); // 		GD_minset(g) = UF_union(GD_minset(g), u);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("495y0cnvj5vci19wsufg88rrq"); // 	case 4:
UNSUPPORTED("1wjv2f7dql1ddky1us3a7q5jq"); // 	case 5:
UNSUPPORTED("1myv9cwrp9n535g9xsalgmg7n"); // 	    if (GD_maxset(g) == NULL)
UNSUPPORTED("45gr04d25a1qxrh4hm1kiip5v"); // 		GD_maxset(g) = u;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("d5a4ohz8nh8xso8ovij23zsxi"); // 		GD_maxset(g) = UF_union(GD_maxset(g), u);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9ty5l2g646lrkxz43tcnhcsk8"); // 	switch (kind) {
UNSUPPORTED("1640m8as34e90xhvvtl877cmo"); // 	case 3:
UNSUPPORTED("j5ay8vao16zse2bq0etmlhua"); // 	    ND_ranktype(GD_minset(g)) = kind;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1wjv2f7dql1ddky1us3a7q5jq"); // 	case 5:
UNSUPPORTED("9dtrubjv4hiv1k3dq24skxdb8"); // 	    ND_ranktype(GD_maxset(g)) = kind;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 65qi5f0bxp6d6vahhlcolpk88
// static int  rank_set_class(graph_t * g) 
public static int rank_set_class(ST_Agraph_s g) {
ENTERING("65qi5f0bxp6d6vahhlcolpk88","rank_set_class");
try {
    CString name[] = new CString[] { new CString("same"), new CString("min"), new CString("source"), new CString("max"), new CString("sink"), null };
    int class_[] = new int[] { 1, 2, 3, 4, 5, 0 };
    int val;
    if (is_cluster(g))
	return 7;
    val = maptoken(agget(g, new CString("rank")), name, class_);
    GD_set_type(g, val);
    return val;
} finally {
LEAVING("65qi5f0bxp6d6vahhlcolpk88","rank_set_class");
}
}




//3 5189iviqj57iztftckz86y6jj
// static int  make_new_cluster(graph_t * g, graph_t * subg) 
public static int make_new_cluster(ST_Agraph_s g, ST_Agraph_s subg) {
ENTERING("5189iviqj57iztftckz86y6jj","make_new_cluster");
try {
    int cno;
    GD_n_cluster(g, GD_n_cluster(g)+1);
    cno = GD_n_cluster(g);
    GD_clust(g, ZALLOC_ST_Agraph_s((ST_Agraph_s.Array) GD_clust(g), cno + 1));
    GD_clust(g).plus(cno).setPtr(subg);
    do_graph_label(subg);
    return cno;
} finally {
LEAVING("5189iviqj57iztftckz86y6jj","make_new_cluster");
}
}




//3 9lvm2ufqjzl2bsbpo0zg9go58
// static void  node_induce(graph_t * par, graph_t * g) 
public static void node_induce(ST_Agraph_s par, ST_Agraph_s g) {
ENTERING("9lvm2ufqjzl2bsbpo0zg9go58","node_induce");
try {
    ST_Agnode_s n, nn;
    ST_Agedge_s e;
    int i;
    LOG2("node_induce");
    /* enforce that a node is in at most one cluster at this level */
    for (n = agfstnode(g); n!=null; n = nn) {
	nn = agnxtnode(g, n);
	if (ND_ranktype(n)!=0) {
	    agdelete(g, n);
	    continue;
	}
	for (i = 1; i < GD_n_cluster(par); i++)
	    if (agcontains((ST_Agraph_s) GD_clust(par).get(i).getPtr(), n))
		break;
	if (i < GD_n_cluster(par))
	    agdelete(g, n);
	ND_clust(n, null);
    }
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	for (e = agfstout(dot_root(g), n); e!=null; e = agnxtout(dot_root(g), e)) {
	    if (agcontains(g, aghead(e)))
		agsubedge(g,e,true);
	}
    }
} finally {
LEAVING("9lvm2ufqjzl2bsbpo0zg9go58","node_induce");
}
}




//3 650rxyqioihwhhqkex61prwfs
// void  dot_scan_ranks(graph_t * g) 
public static Object dot_scan_ranks(Object... arg) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("3qe2zolxii33gr1krcjkgygwm"); // dot_scan_ranks(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dkbxmqzr28yu8rswd5vubd3ha"); //     node_t *n, *leader = NULL;
UNSUPPORTED("65ovv0jsp2kemyp7179cotrqh"); //     GD_minrank(g) = MAXSHORT;
UNSUPPORTED("4rx9wnlw0uumqfzcjtyg9rpfl"); //     GD_maxrank(g) = -1;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("68vv0lam9vxeuk3fg60ad5w6g"); // 	if (GD_maxrank(g) < ND_rank(n))
UNSUPPORTED("2v65dy95gqvsnppoelwwl8ayh"); // 	    GD_maxrank(g) = ND_rank(n);
UNSUPPORTED("3ozq6tqfxcegom34qkyrrxnfg"); // 	if (GD_minrank(g) > ND_rank(n))
UNSUPPORTED("duyud2jy9uf1rbcis84c3lsj6"); // 	    GD_minrank(g) = ND_rank(n);
UNSUPPORTED("5j9c428shih0wjw2salkxw0qm"); // 	if (leader == NULL)
UNSUPPORTED("73jlqlf2bn3kx7i020menpfcw"); // 	    leader = n;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("bwqhlzu5ifbhdg1jral42ycdx"); // 	    if (ND_rank(n) < ND_rank(leader))
UNSUPPORTED("7h663p7xpn4qn3l6bs6vxze7i"); // 		leader = n;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5j1zsofmtglx1esxwbstti1un"); //     GD_leader(g) = leader;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2rbs5deyvlh5s7lkhv6zouqbe
// static void cluster_leader(graph_t * clust) 
public static void cluster_leader(ST_Agraph_s clust) {
ENTERING("2rbs5deyvlh5s7lkhv6zouqbe","cluster_leader");
try {
    ST_Agnode_s leader, n;
    int maxrank = 0;
    /* find number of ranks and select a leader */
    leader = null;
    for (n = GD_nlist(clust); n!=null; n = ND_next(n)) {
	if ((ND_rank(n) == 0) && (ND_node_type(n) == 0))
	    leader = n;
	if (maxrank < ND_rank(n))
	    maxrank = ND_rank(n);
    }
    assert(leader != null);
    GD_leader(clust, leader);
    for (n = agfstnode(clust); n!=null; n = agnxtnode(clust, n)) {
	//assert((ND_UF_size(n) <= 1) || (n == leader));
	UF_union(n, leader);
	ND_ranktype(n, 7);
    }
} finally {
LEAVING("2rbs5deyvlh5s7lkhv6zouqbe","cluster_leader");
}
}




//3 f3sl627dqmre3kru883bpdxc3
// static void  collapse_cluster(graph_t * g, graph_t * subg) 
public static void collapse_cluster(ST_Agraph_s g, ST_Agraph_s subg) {
ENTERING("f3sl627dqmre3kru883bpdxc3","collapse_cluster");
try {
    if (GD_parent(subg)!=null) {
	return;
    }
    GD_parent(subg, g);
    node_induce(g, subg);
    if (agfstnode(subg) == null)
	return;
    make_new_cluster(g, subg);
    if (Z.z().CL_type == 100) {
	dot1_rank(subg, null);
	cluster_leader(subg);
    } else
UNSUPPORTED("1os84mtyrb110i4sd8bdjrwk"); // 	dot_scan_ranks(subg);
} finally {
LEAVING("f3sl627dqmre3kru883bpdxc3","collapse_cluster");
}
}




//3 din4qnipewrwnelaimzvlplft
// static void  collapse_sets(graph_t *rg, graph_t *g) 
public static void collapse_sets(ST_Agraph_s rg, ST_Agraph_s g) {
ENTERING("din4qnipewrwnelaimzvlplft","collapse_sets");
try {
    int c;
    ST_Agraph_s  subg;
    for (subg = agfstsubg(g); subg!=null; subg = agnxtsubg(subg)) {
	c = rank_set_class(subg);
	if (c!=0) {
	    if ((c == 7) && Z.z().CL_type == 100)
		collapse_cluster(rg, subg);
	    else
		collapse_rankset(rg, subg, c);
	}
	else collapse_sets(rg, subg);
    }
} finally {
LEAVING("din4qnipewrwnelaimzvlplft","collapse_sets");
}
}




//3 5n9mgh7vlru5mb1j9oienvbvs
// static void  find_clusters(graph_t * g) 
public static Object find_clusters(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("cdsgmo50taekqgk95mfn25930"); // find_clusters(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8uujemixuhlf040icq3zsh7j8"); //     graph_t *subg;
UNSUPPORTED("39msf2samfrjyh2h1a0nh0bnq"); //     for (subg = agfstsubg(dot_root(g)); subg; subg = agnxtsubg(subg)) {
UNSUPPORTED("zmexivcsx1b4oppz6cjwhzd9"); // 	if (GD_set_type(subg) == 7)
UNSUPPORTED("xqwyd1xyo86onxfw4s7p8at4"); // 	    collapse_cluster(g, subg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 12fw0esv4unfin6waf9mknc1o
// static void  set_minmax(graph_t * g) 
public static void set_minmax(ST_Agraph_s g) {
ENTERING("12fw0esv4unfin6waf9mknc1o","set_minmax");
try {
    int c;
    GD_minrank(g, GD_minrank(g) + ND_rank(GD_leader(g)));
    GD_maxrank(g, GD_maxrank(g) + ND_rank(GD_leader(g)));
    for (c = 1; c <= GD_n_cluster(g); c++)
	set_minmax((ST_Agraph_s) GD_clust(g).get(c).getPtr());
} finally {
LEAVING("12fw0esv4unfin6waf9mknc1o","set_minmax");
}
}




//3 3bcr1748gqnu8ogb73jeja7ly
// static point  minmax_edges(graph_t * g) 
public static ST_point minmax_edges(ST_Agraph_s g) {
// WARNING!! STRUCT
return (ST_point) minmax_edges_w_(g).copy();
}
private static ST_point minmax_edges_w_(ST_Agraph_s g) {
ENTERING("3bcr1748gqnu8ogb73jeja7ly","minmax_edges");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    final ST_point slen = new ST_point();
    slen.setInt("x", 0);
    slen.setInt("y", 0);
    if ((GD_maxset(g) == null) && (GD_minset(g) == null))
	return slen;
UNSUPPORTED("d0tnzm7aw9504y1w1oqoesw64"); //     if ((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset) != NULL)
UNSUPPORTED("9esfh1bqntzgyk7zcq16k9f96"); // 	(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset) = UF_find((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset));
UNSUPPORTED("2szhe8u8hvuy7p23r4p4zcb83"); //     if ((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset) != NULL)
UNSUPPORTED("tufrhwafgfvg5vepfqo9dpwg"); // 	(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset) = UF_find((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset));
UNSUPPORTED("3num56yubfb33g0m56jntiy0x"); //     if ((n = (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset))) {
UNSUPPORTED("d60rrtpfeuylcbp2490sojfjq"); // 	slen.y = ((((Agnodeinfo_t*)(((Agobj_t*)((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset)))->data))->ranktype) == 5);
UNSUPPORTED("79ls52ss65f22xrsubkcofzz"); // 	while ((e = (((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->out).list[0])) {
UNSUPPORTED("chd9prkphze2z32e98mbxhqyd"); // 	    assert(((((((Agobj_t*)(e))->tag).objtype) == 2? (e): ((e)-1))->node) == UF_find(((((((Agobj_t*)(e))->tag).objtype) == 2? (e): ((e)-1))->node)));
UNSUPPORTED("829yx3b7rjn7ptz89mz4dj5yo"); // 	    reverse_edge(e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8us4psjv2ebkgcp54fvjbuhj8"); //     if ((n = (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset))) {
UNSUPPORTED("7uri9lp9wjgo20ram4gfo974w"); // 	slen.x = ((((Agnodeinfo_t*)(((Agobj_t*)((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset)))->data))->ranktype) == 3);
UNSUPPORTED("5up69q1rp9ts32jvunwg9hlrr"); // 	while ((e = (((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->in).list[0])) {
UNSUPPORTED("4t476gsg37fhfa2fdrokupx2c"); // 	    assert(((((((Agobj_t*)(e))->tag).objtype) == 3? (e): ((e)+1))->node) == UF_find(((((((Agobj_t*)(e))->tag).objtype) == 3? (e): ((e)+1))->node)));
UNSUPPORTED("829yx3b7rjn7ptz89mz4dj5yo"); // 	    reverse_edge(e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("erz19oifq072tdfzgz6dxa9i4"); //     return slen;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("3bcr1748gqnu8ogb73jeja7ly","minmax_edges");
}
}




//3 1rmlm1wo3t94wyet9rlwrmith
// static int  minmax_edges2(graph_t * g, point slen) 
public static boolean minmax_edges2(ST_Agraph_s g, final ST_point slen) {
// WARNING!! STRUCT
return minmax_edges2_w_(g, (ST_point) slen.copy());
}
private static boolean minmax_edges2_w_(ST_Agraph_s g, final ST_point slen) {
ENTERING("1rmlm1wo3t94wyet9rlwrmith","minmax_edges2");
try {
    ST_Agnode_s n;
    ST_Agedge_s e = null;
    if ((GD_maxset(g)!=null) || (GD_minset(g)!=null)) {
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("8y47p29z0c2f1xpkrsb8w8re8"); // 	    if (n != UF_find(n))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("49yt5gs5xlk2yzmiulvp7iqrd"); // 	    if (((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->out).size == 0) && (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset) && (n != (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset))) {
UNSUPPORTED("9ksut17itonzpk3hp57jn4d1s"); // 		e = virtual_edge(n, (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxset), NULL);
UNSUPPORTED("7dlot3nkpploeywkumjt3erop"); // 		(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->minlen) = slen.y;
UNSUPPORTED("5ddkb181unkbg63gxqjx85fzq"); // 		(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->weight) = 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5b66s1jsuwe7l2e8p6o1xpnab"); // 	    if (((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->in).size == 0) && (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset) && (n != (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset))) {
UNSUPPORTED("c00g90uqqonkk08nncvi45c8f"); // 		e = virtual_edge((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minset), n, NULL);
UNSUPPORTED("cxdsqlq2h35nyz65uc4eifchp"); // 		(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->minlen) = slen.x;
UNSUPPORTED("5ddkb181unkbg63gxqjx85fzq"); // 		(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->weight) = 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    return (e != null);
} finally {
LEAVING("1rmlm1wo3t94wyet9rlwrmith","minmax_edges2");
}
}




//3 3vpthwso788idvycelpnqijys
// void rank1(graph_t * g) 
public static void rank1(ST_Agraph_s g) {
ENTERING("3vpthwso788idvycelpnqijys","rank1");
try {
    int maxiter = Integer.MAX_VALUE;
    int c;
    CString s;
    if ((s = agget(g, new CString("nslimit1")))!=null)
UNSUPPORTED("9tp2zk1tsr4ce9rwsr0is9u3o"); // 	maxiter = atof(s) * agnnodes(g);
    for (c = 0; c < GD_comp(g).size; c++) {
    	//GD_nlist(g, GD_comp(g).list.get(c));
    	GD_nlist(g, GD_comp(g).getFromList(c));
	rank(g, (GD_n_cluster(g) == 0 ? 1 : 0), maxiter);	/* TB balance */
    }
} finally {
LEAVING("3vpthwso788idvycelpnqijys","rank1");
}
}




//3 cdh8wnb99v90dy6efpbzmrjix
// static void expand_ranksets(graph_t * g, aspect_t* asp) 
public static void expand_ranksets(ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("cdh8wnb99v90dy6efpbzmrjix","expand_ranksets");
try {
    int c;
    ST_Agnode_s n, leader;
    if ((n = agfstnode(g))!=null) {
	GD_minrank(g, MAXSHORT);
	GD_maxrank(g, -1);
	while (n!=null) {
	    leader = UF_find(n);
	    /* The following works because ND_rank(n) == 0 if n is not in a
	     * cluster, and ND_rank(n) = the local rank offset if n is in
	     * a cluster. */
	    if (NEQ(leader, n) && (N(asp) || (ND_rank(n) == 0)))
		ND_rank(n, ND_rank(n) + ND_rank(leader));
	    if (GD_maxrank(g) < ND_rank(n))
		GD_maxrank(g, ND_rank(n));
	    if (GD_minrank(g) > ND_rank(n))
		GD_minrank(g, ND_rank(n));
	    if (ND_ranktype(n)!=0 && (ND_ranktype(n) != 6))
		UF_singleton(n);
	    n = agnxtnode(g, n);
	}
	if (EQ(g, dot_root(g))) {
	    if (Z.z().CL_type == 100) {
		for (c = 1; c <= GD_n_cluster(g); c++)
		    set_minmax((ST_Agraph_s) GD_clust(g).get(c).getPtr());
	    } else {
		find_clusters(g);
	    }
	}
    } else {
	GD_maxrank(g, 0);
	GD_minrank(g, 0);
    }
} finally {
LEAVING("cdh8wnb99v90dy6efpbzmrjix","expand_ranksets");
}
}




//3 2o4rmb4o6f6zh46ak3se91rwr
// static void dot1_rank(graph_t * g, aspect_t* asp) 
public static void dot1_rank(ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("2o4rmb4o6f6zh46ak3se91rwr","dot1_rank");
try {
    final ST_point p = new ST_point();
    edgelabel_ranks(g);
    if (asp!=null) {
UNSUPPORTED("kh7e20nqwuserrnpf3zpvuyl"); // 	init_UF_size(g);
UNSUPPORTED("d88j5oswhz0d3yvd4wlvxohmu"); // 	initEdgeTypes(g);
    }
    collapse_sets(g,g);
    /*collapse_leaves(g); */
    class1_(g);
    p.___(minmax_edges(g));
    decompose(g, 0);
    if (asp!=null && ((GD_comp(g).size > 1)||(GD_n_cluster(g) > 0))) {
UNSUPPORTED("evcjt85irnaa02v8cam07i009"); // 	asp->badGraph = 1;
UNSUPPORTED("45nxv6kczal9hnytkfcyt2jk8"); // 	asp = NULL;
    }
    acyclic_(g);
    if (minmax_edges2(g, p))
UNSUPPORTED("800vpyk6y4hcx2txwyrr2boxu"); // 	decompose(g, 0);
    if (asp!=null)
	rank3(g, asp);
    else
	rank1(g);
    expand_ranksets(g, asp);
    cleanup1(g);
} finally {
LEAVING("2o4rmb4o6f6zh46ak3se91rwr","dot1_rank");
}
}




//3 asyfujgwqa407ffvqn5psbtsc
// void dot_rank(graph_t * g, aspect_t* asp) 
public static void dot_rank(ST_Agraph_s g, ST_aspect_t asp) {
ENTERING("asyfujgwqa407ffvqn5psbtsc","dot_rank");
try {
    if (agget (g, new CString("newrank"))!=null) {
	GD_flags(g, GD_flags(g) | (1 << 4));
	dot2_rank (g, asp);
    }
    else
	dot1_rank (g, asp);
    //if (Verbose)
	//fprintf (stderr, "Maxrank = %d, minrank = %d\n", (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->maxrank), (((Agraphinfo_t*)(((Agobj_t*)(g))->data))->minrank));
} finally {
LEAVING("asyfujgwqa407ffvqn5psbtsc","dot_rank");
}
}




//3 cdncou6d2ng5i48rd1mk2cpnw
// int is_cluster(graph_t * g) 
public static boolean is_cluster(ST_Agraph_s g) {
ENTERING("cdncou6d2ng5i48rd1mk2cpnw","is_cluster");
try {
    return (strncmp(agnameof(g), new CString("cluster"), 7) == 0);
} finally {
LEAVING("cdncou6d2ng5i48rd1mk2cpnw","is_cluster");
}
}




//3 29qzn29glqnhg14z5cwt9i8ds
// static void set_parent(graph_t* g, graph_t* p)  
public static Object set_parent(Object... arg) {
UNSUPPORTED("blcrnfwfdmb15y0sduplfd1kd"); // static void set_parent(graph_t* g, graph_t* p) 
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("761elco9es28q1el1h87c9q8w"); //     GD_parent(g) = p;
UNSUPPORTED("84dx27r05ns2c0pm7idum25td"); //     make_new_cluster(p, g);
UNSUPPORTED("65op19ofyxj4vywwg8hovy2yh"); //     node_induce(p, g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3qlca4afaxtwu0r2v22ccpfy9
// static int is_empty(graph_t * g) 
public static Object is_empty(Object... arg) {
UNSUPPORTED("1bkrdpgwmb75nr2g9ooqfc79r"); // static int is_empty(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8bgakhvkt8exvi70zpvdvwrt4"); //     return (!agfstnode(g));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 sxmrpf4e3wi4vzeiu486heyw
// static int is_a_strong_cluster(graph_t * g) 
public static Object is_a_strong_cluster(Object... arg) {
UNSUPPORTED("251zndmdq92kg3zmfr3akrrmn"); // static int is_a_strong_cluster(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("9l9qe4o3ak62d2r6cbq90e5g"); //     char *str = agget(g, "compact");
UNSUPPORTED("5zgcjj66562k7c4z26hmps9jr"); //     /* rv = mapBool((str), TRUE); */
UNSUPPORTED("7k6ls7carz5knwiocbejouapm"); //     rv = mapBool((str), 0);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 954bo7mrh993f96ujb8u3e8vt
// static int rankset_kind(graph_t * g) 
public static Object rankset_kind(Object... arg) {
UNSUPPORTED("5vyg261oak77cq7e9dmvw2omc"); // static int rankset_kind(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("70l71erfhd26e1u7dnnxpa6i"); //     char *str = agget(g, "rank");
UNSUPPORTED("4ml2m315k80oi1eww739kxwgi"); //     if (str && str[0]) {
UNSUPPORTED("7j0qc9ibr6niawo611kvm2swb"); // 	if (!strcmp(str, "min"))
UNSUPPORTED("avnuwo0ld6vqw4bdve4ku0kwr"); // 	    return 2;
UNSUPPORTED("8q0yyecb20k2nm2qnluagpkcv"); // 	if (!strcmp(str, "source"))
UNSUPPORTED("1xt8eg4imiwilo2bv2i5shg7g"); // 	    return 3;
UNSUPPORTED("7dkmgopul9nxw7arr5odhh641"); // 	if (!strcmp(str, "max"))
UNSUPPORTED("ajqfmduyeyu16131ii3itnjx7"); // 	    return 4;
UNSUPPORTED("4f7keu82iifj10rf0thee5c4s"); // 	if (!strcmp(str, "sink"))
UNSUPPORTED("f45x5jzpirzixrsp3utlgrddo"); // 	    return 5;
UNSUPPORTED("kjtw0fdz1jophwulunsrgzdb"); // 	if (!strcmp(str, "same"))
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ds4c38px5ikyzcv8pbtfcfg8h"); //     return 6;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ej4vtw2e6g22jzlypo03buuob
// static int is_nonconstraint(edge_t * e) 
public static Object is_nonconstraint(Object... arg) {
UNSUPPORTED("cdj60nbfp2uc9emgj9bb9tuq4"); // static int is_nonconstraint(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5bdi9u1cut03m2rbxpkmjg38w"); //     char *constr;
UNSUPPORTED("ipmkcv3n8rnco04rgte3qrlh"); //     if (E_constr && (constr = agxget(e, E_constr))) {
UNSUPPORTED("cnlkffmn4nysfcmoyktn3wvrx"); // 	if (constr[0] && mapbool(constr) == 0)
UNSUPPORTED("9qhn9m3123s8n6wwxjfo8awlm"); // 	    return NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8pb7znv8q3ikfulus8sprsrb8
// static node_t *find(node_t * n) 
public static Object find(Object... arg) {
UNSUPPORTED("420nqb6oiuhh8qfg7ick8eb5j"); // static node_t *find(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dxloerh79kcplsvqy37g3kwx1"); //     node_t *set;
UNSUPPORTED("ch7ucg4bhoyhb0yswbbtjy53"); //     if ((set = ND_set(n))) {
UNSUPPORTED("e20g6f3inc2t1acfqffb0ksqe"); // 	if (set != n)
UNSUPPORTED("3lalmwvgf54uou021ltj0hfqg"); // 	    set = ND_set(n) = find(set);
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("45wb5d7uba8g13ojkskeug5v2"); // 	set = ND_set(n) = n;
UNSUPPORTED("d5jffopzvq1b1jnhb3jadtkkb"); //     return set;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 tgndl91vjf2dvnepj15uye32
// static node_t *union_one(node_t * leader, node_t * n) 
public static Object union_one(Object... arg) {
UNSUPPORTED("bk1ys5rvc0fqcssw437mw03pk"); // static node_t *union_one(node_t * leader, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("vkwoakpy0gnbvzgy8gprpluu"); //     if (n)
UNSUPPORTED("879p5fay2s7f829lo6qaxs62y"); // 	return (ND_set(find(n)) = find(leader));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("o1psslvh23yuu288xx1nzbv6"); // 	return leader;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ku7zqljp4yk6j8pqxa19ko4u
// static node_t *union_all(graph_t * g) 
public static Object union_all(Object... arg) {
UNSUPPORTED("1bddf3dbho07f9di3ae5tdn2z"); // static node_t *union_all(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("d5wzfazsul6ns71kurzu097zq"); //     node_t *n, *leader;
UNSUPPORTED("42x5vqk9aw9a2ld3duvpmp7u9"); //     n = agfstnode(g);
UNSUPPORTED("b23lxt0h34yyivnau77kavofl"); //     if (!n)
UNSUPPORTED("bp96fem54xcxrw16cmnlpell9"); // 	return n;
UNSUPPORTED("c7r16vtue9uiodzk7n9ybdegz"); //     leader = find(n);
UNSUPPORTED("1azt0yldbuvzvyhftheine5bv"); //     while ((n = agnxtnode(g, n)))
UNSUPPORTED("aygpegbyw6isjunzjiq2efa8e"); // 	union_one(leader, n);
UNSUPPORTED("d2vvjehoyl5rcjodzjl8q0xne"); //     return leader;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dehfujiq2i24w0y9qcoq88gbd
// static void compile_samerank(graph_t * ug, graph_t * parent_clust) 
public static Object compile_samerank(Object... arg) {
UNSUPPORTED("52xc1gml0ynibrpu4r03l9cs"); // static void compile_samerank(graph_t * ug, graph_t * parent_clust)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5prtalowcftfpc3phfgtdwxe1"); //     graph_t *s;			/* subgraph being scanned */
UNSUPPORTED("6rya07j39ddlbnek0cawgbdxq"); //     graph_t *clust;		/* cluster that contains the rankset */
UNSUPPORTED("d5wzfazsul6ns71kurzu097zq"); //     node_t *n, *leader;
UNSUPPORTED("3j62rndf411nak99bxbvwogwg"); //     if (is_empty(ug))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("116sd790xqr0sm9prs44dhdi9"); //     if (is_a_cluster(ug)) {
UNSUPPORTED("anfjvlosf3592vmzm64wp9ukv"); // 	clust = ug;
UNSUPPORTED("a4nj79aw8gc4rsc6qh4h1j3ca"); // 	if (parent_clust) {
UNSUPPORTED("2cme4svj15jnccy2a5my2k0vk"); // 	    GD_level(ug) = GD_level(parent_clust) + 1;
UNSUPPORTED("92dkw1fscsyjwrbkjnllcmsiz"); // 	    set_parent(ug, parent_clust);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("1f3tsi2calw640ylv89zufnqg"); // 	    GD_level(ug) = 0;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("f2eqknbcu7dkt0mk3aa0nseps"); // 	clust = parent_clust;
UNSUPPORTED("2jowbcd45wf29ysq00oxpb9qh"); //     /* process subgraphs of this subgraph */
UNSUPPORTED("cpub3vrdy1soy50cezvowuqp8"); //     for (s = agfstsubg(ug); s; s = agnxtsubg(s))
UNSUPPORTED("6y2vizzx42iva8zf3ndgfxuoo"); // 	compile_samerank(s, clust);
UNSUPPORTED("4st45szka4kxyf5afpt9r2lr7"); //     /* process this subgraph as a cluster */
UNSUPPORTED("116sd790xqr0sm9prs44dhdi9"); //     if (is_a_cluster(ug)) {
UNSUPPORTED("4pm26v2jo8qeolmy8246h5zal"); // 	for (n = agfstnode(ug); n; n = agnxtnode(ug, n)) {
UNSUPPORTED("dpma84q2zbh96nqex4fp8bs5a"); // 	    if (ND_clust(n) == 0)
UNSUPPORTED("9ov8qga72kqj7lnxefrlneehb"); // 		ND_clust(n) = ug;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b6vj39735t21yl5ayiouei3ea"); //     /* process this subgraph as a rankset */
UNSUPPORTED("bt28cwea3vaqopki07c2rians"); //     switch (rankset_kind(ug)) {
UNSUPPORTED("33l7a58zp8vj6fuliwdkk2nkn"); //     case 3:
UNSUPPORTED("btl4oqew40wl0bqc9bhe1qshq"); // 	GD_has_sourcerank(clust) = NOT(0);	/* fall through */
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("9shvnosp4rfl2zcu1mk8mt6k"); // 	leader = union_all(ug);
UNSUPPORTED("6o42rb3i5w3jv1861oa077ahy"); // 	GD_minrep(clust) = union_one(leader, GD_minrep(clust));
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("94bjfk0blg9ugkinwopclju8t"); //     case 5:
UNSUPPORTED("b9tzw03kknh7ged56o6llxdh0"); // 	GD_has_sinkrank(clust) = NOT(0);	/* fall through */
UNSUPPORTED("7gwyze795m9aa2915n3bou49x"); //     case 4:
UNSUPPORTED("9shvnosp4rfl2zcu1mk8mt6k"); // 	leader = union_all(ug);
UNSUPPORTED("dumvmo0mkc9khfmooiadflzuz"); // 	GD_maxrep(clust) = union_one(leader, GD_maxrep(clust));
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("9shvnosp4rfl2zcu1mk8mt6k"); // 	leader = union_all(ug);
UNSUPPORTED("aukepn75qomcnwetlwyvziwx1"); // 	/* do we need to record these ranksets? */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4ak4rwp0nsvc9n89y3dnvoiy9"); //     case 6:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("b7b3qf9gm8yinsl7rq2tcvamg"); //     default:			/* unrecognized - warn and do nothing */
UNSUPPORTED("dsg015rva622f29whl6b5ner1"); // 	agerr(AGWARN, "%s has unrecognized rank=%s", agnameof(ug),
UNSUPPORTED("7gjdlew6rtq0dsdxc6uyc7v48"); // 	      agget(ug, "rank"));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8z2hxrvowojvpioiz41x8djhw"); //     /* a cluster may become degenerate */
UNSUPPORTED("bg6be1cr97nd4blhxwc8577cz"); //     if (is_a_cluster(ug) && GD_minrep(ug)) {
UNSUPPORTED("7z1ki4lb1ja4hai3qi68hlg72"); // 	if (GD_minrep(ug) == GD_maxrep(ug)) {
UNSUPPORTED("6d3xnrwrzibz1hajej296nhj4"); // 	    node_t *up = union_all(ug);
UNSUPPORTED("bisonxzfe0vx9x9xtsvv27rq9"); // 	    GD_minrep(ug) = up;
UNSUPPORTED("esz48qzeb3f3oomg8wb4drmrn"); // 	    GD_maxrep(ug) = up;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6mzsthkzz214du9ljfc7ijlf3
// static graph_t *dot_lca(graph_t * c0, graph_t * c1) 
public static Object dot_lca(Object... arg) {
UNSUPPORTED("egnw42d8jhfdyl5zqlzqffv17"); // static graph_t *dot_lca(graph_t * c0, graph_t * c1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8qmvk29gersospxrjeukh5zdn"); //     while (c0 != c1) {
UNSUPPORTED("9395225iwl078vpfdl5fz3q1i"); // 	if (GD_level(c0) >= GD_level(c1))
UNSUPPORTED("ud0xxtx626m6p44f5ybl28oz"); // 	    c0 = GD_parent(c0);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("1z7wsarh0nlwq4y04yizw1pbj"); // 	    c1 = GD_parent(c1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("aqd6v9mqxnbx6er4fkhkmhusr"); //     return c0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7fwe6cym6k60fw6f2gkbftvh8
// static int is_internal_to_cluster(edge_t * e) 
public static Object is_internal_to_cluster(Object... arg) {
UNSUPPORTED("esoedecj682cdsbz3i9026zfo"); // static int is_internal_to_cluster(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bf7f1i4wj7cvj82os2h0somln"); //     graph_t *par, *ct, *ch;
UNSUPPORTED("pzh3wmhacvb8baamh8d0x2j5"); //     ct = ND_clust(agtail(e));
UNSUPPORTED("1yxzewahgac4khyfwgegotstb"); //     ch = ND_clust(aghead(e));
UNSUPPORTED("c0hxl5pgz9bgngw9cw54a6y8z"); //     if (ct == ch)
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("b4gos5u06phmhqll71bg7znsl"); //     par = dot_lca(ct, ch);
UNSUPPORTED("32a491a5jyr0gaudx2odlyl1x"); //     /* if (par == agroot(par)) */
UNSUPPORTED("4ugp2o11w5qr2tr0vjcqlm5xu"); // 	/* return FALSE; */
UNSUPPORTED("1vb9z2lsj8ote2lis1nzqfq98"); //     if ((par == ct) || (par == ch))
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 ca279jzn1n2k40bv7mz55ccx7
// static node_t* Last_node
//private static Agnode_s Last_node;


//3 6odjfuoywf6x6xpuz14xn1w07
// static node_t* makeXnode (graph_t* G, char* name) 
public static Object makeXnode(Object... arg) {
UNSUPPORTED("bpc5db7ozsqpc73t2xl0qmpnr"); // static node_t* makeXnode (graph_t* G, char* name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("as7pt6rau89ac05ml4zp39xgj"); //     node_t *n = agnode(G, name, 1);
UNSUPPORTED("6b9ymcqm4d2w42w6dvlhb7hio"); //     alloc_elist(4, ND_in(n));
UNSUPPORTED("b0ocdkprm41g10emqffwvovpd"); //     alloc_elist(4, ND_out(n));
UNSUPPORTED("e32lwhe8aj8zbofa5hl91g94x"); //     if (Last_node) {
UNSUPPORTED("ctnbewkujd0gitkeptmdjrc5e"); // 	ND_prev(n) = Last_node;
UNSUPPORTED("efpj513jkepv1eb6ehj5cyuqy"); // 	ND_next(Last_node) = n;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("d73xo1uhf9960attqraecy18q"); // 	ND_prev(n) = NULL;
UNSUPPORTED("bd7fe5nmcvdfwgbbuzoxzk59n"); // 	GD_nlist(G) = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7gutgf5zbme88t9ueyzvir9yh"); //     Last_node = n;
UNSUPPORTED("9tl9ztdpfpeb900t5gagch4eg"); //     ND_next(n) = NULL;
UNSUPPORTED("69hc24ic55i66g8tf2ne42327"); //     return n;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cd3kauuz11z03xym1la7ze5e6
// static void compile_nodes(graph_t * g, graph_t * Xg) 
public static Object compile_nodes(Object... arg) {
UNSUPPORTED("12jh8m3jnppgzqcx642zkv20x"); // static void compile_nodes(graph_t * g, graph_t * Xg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dm6k18yfspisvs0pl8f3kp9j4"); //     /* build variables */
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("hibhvgkp511r6u6ips8yb0un"); //     Last_node = NULL;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("2f2poxq5fr9k7bgerylsq6dkm"); // 	if (find(n) == n)
UNSUPPORTED("9xe9wxas5cxrrzpmqtkfavbuj"); // 	    ND_rep(n) = makeXnode (Xg, agnameof(n));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("cfjmkdzc5nj1nbp2ckl0mejra"); // 	if (ND_rep(n) == 0)
UNSUPPORTED("2u4sgiv3zkgh3t4r1ksk76oyl"); // 	    ND_rep(n) = ND_rep(find(n));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e1guv5kmb9i30k71e66mdxo3y
// static void merge(edge_t * e, int minlen, int weight) 
public static Object merge(Object... arg) {
UNSUPPORTED("1npguevtdh47xfz698yahzrqb"); // static void merge(edge_t * e, int minlen, int weight)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("acr0hvlhebqp3iieup5bti0r6"); //     ED_minlen(e) = MAX(ED_minlen(e), minlen);
UNSUPPORTED("eq9oxzgg08304c8ph77144cpu"); //     ED_weight(e) += weight;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 59c6dqhr5u2wv75vxz5cgbkui
// static void strong(graph_t * g, node_t * t, node_t * h, edge_t * orig) 
public static Object strong(Object... arg) {
UNSUPPORTED("6vzggze9zva4h232s9hd64r27"); // static void strong(graph_t * g, node_t * t, node_t * h, edge_t * orig)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("bqayp53mx8tl3a9owcil2sfpy"); //     if ((e = (agedge(g,t,h,NULL,0))) ||
UNSUPPORTED("4bkprvzfmfr1nlqmvqcd1xpg"); // 	(e = (agedge(g,h,t,NULL,0))) || (e = agedge(g, t, h, 0, 1)))
UNSUPPORTED("9otcshlag9drzks2p4q4bz1z8"); // 	merge(e, ED_minlen(orig), ED_weight(orig));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("1qsfg1so4d9ee0f799ylbtkyp"); // 	agerr(AGERR, "ranking: failure to create strong constraint edge between nodes %s and %s\n", 
UNSUPPORTED("347820654uihcls8om0v8c3g1"); // 	    agnameof(t), agnameof(h));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a1kjm11iwgfl824pakzcm8kuu
// static void weak(graph_t * g, node_t * t, node_t * h, edge_t * orig) 
public static Object weak(Object... arg) {
UNSUPPORTED("e0f6tf4pkq822l7f10u2xromd"); // static void weak(graph_t * g, node_t * t, node_t * h, edge_t * orig)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aigogf44ojtcesuy4xs7inqbn"); //     node_t *v;
UNSUPPORTED("9b48a157azcrz2ihzqehhpsvs"); //     edge_t *e, *f;
UNSUPPORTED("3kkq8fnz3moxvyriighzr9v70"); //     static int id;
UNSUPPORTED("hbaoyuj2oi8c7iuzviuukgb7"); //     char buf[100];
UNSUPPORTED("5n9mmbpgpmin1li1s5wxfn0j7"); //     for (e = agfstin(g, t); e; e = agnxtin(g, e)) {
UNSUPPORTED("blrs1fuvxzbs20bdqz7btw4yt"); // 	/* merge with existing weak edge (e,f) */
UNSUPPORTED("cccrsacwb6lshsxm8g9vpqdvo"); // 	v = agtail(e);
UNSUPPORTED("657evjbfij6x36pli6d9g15lg"); // 	if ((f = agfstout(g, v)) && (aghead(f) == h)) {
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dhdmfarapbytpy4h24lgqslbi"); //     if (!e) {
UNSUPPORTED("blf0483ca5ufq6yh26qqww4wv"); // 	sprintf (buf, "_weak_%d", id++);
UNSUPPORTED("9gu1nn0ir6yx53fj7kawo6xtw"); // 	v = makeXnode(g, buf);
UNSUPPORTED("7us6c9ykrtln0besnd67v5cer"); // 	e = agedge(g, v, t, 0, 1);
UNSUPPORTED("atd2wygaazfzcta0bus6bs4uu"); // 	f = agedge(g, v, h, 0, 1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7nn4pn4dj4nuo71wfp1byijpx"); //     ED_minlen(e) = MAX(ED_minlen(e), 0);	/* effectively a nop */
UNSUPPORTED("drqgauf89xr1mpyydaxqfg694"); //     ED_weight(e) += ED_weight(orig) * 1000;
UNSUPPORTED("halhvp8h6olsu4tb4b5zb8ys"); //     ED_minlen(f) = MAX(ED_minlen(f), ED_minlen(orig));
UNSUPPORTED("dyyu1myirsx00xvfvqq97f6dt"); //     ED_weight(f) += ED_weight(orig);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 68dc7hsp2siu9in566grx5h8l
// static void compile_edges(graph_t * ug, graph_t * Xg) 
public static Object compile_edges(Object... arg) {
UNSUPPORTED("57s72j3dqfa0fdsi9zu6yqbxe"); // static void compile_edges(graph_t * ug, graph_t * Xg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("1dgz401xgus10wp90qpgfmm84"); //     node_t *Xt, *Xh;
UNSUPPORTED("c37x0xox8y9eba9wy6d00j14i"); //     graph_t *tc, *hc;
UNSUPPORTED("lp2eyq9typ14npscbcmds97c"); //     /* build edge constraints */
UNSUPPORTED("1rgbf8esuu21jsnjy0ozcv2nx"); //     for (n = agfstnode(ug); n; n = agnxtnode(ug, n)) {
UNSUPPORTED("36jgu4l53q07scrzxtbzu9ws1"); // 	Xt = ND_rep(n);
UNSUPPORTED("1huyj2j0jo6l60j1121l0cenn"); // 	for (e = agfstout(ug, n); e; e = agnxtout(ug, e)) {
UNSUPPORTED("eihgt2jen0ohii72jzayossof"); // 	    if (is_nonconstraint(e))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("21s68hvg9tcok9mc1yj0cxyo8"); // 	    Xh = ND_rep(find(aghead(e)));
UNSUPPORTED("5c9s9jmoqhby2u29zb0g9neqn"); // 	    if (Xt == Xh)
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("enlmoet02jwyseh462fspj7kr"); // 	    tc = ND_clust(agtail(e));
UNSUPPORTED("47ba8c5wv42664v4uk20dcxui"); // 	    hc = ND_clust(aghead(e));
UNSUPPORTED("7o1ut6pe3ws38101s6x9jv08k"); // 	    if (is_internal_to_cluster(e)) {
UNSUPPORTED("61gy4yt9v1l4586kvpqtacmur"); // 		/* determine if graph requires reversed edge */
UNSUPPORTED("cuivzwkiwyl1njm43k0v3zig3"); // 		if ((find(agtail(e)) == GD_maxrep(ND_clust(agtail(e))))
UNSUPPORTED("9xxo59da6q1v90ljd3h8apc3j"); // 		    || (find(aghead(e)) == GD_minrep(ND_clust(aghead(e))))) {
UNSUPPORTED("4mdqa1pib7sml5socnk5zj1jn"); // 		    node_t *temp = Xt;
UNSUPPORTED("edto6lxzuam8lv9gvigp2bnnh"); // 		    Xt = Xh;
UNSUPPORTED("edahfvolx6t8a93b1jzop0prz"); // 		    Xh = temp;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("eap1t3wskyfghhrwqv5fzgiye"); // 		strong(Xg, Xt, Xh, e);
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("73tu1coi87ooqzro2vtdezlut"); // 		if (is_a_strong_cluster(tc) || is_a_strong_cluster(hc))
UNSUPPORTED("1qlyasf525g29jx5adouyjjaf"); // 		    weak(Xg, Xt, Xh, e);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("dtnnxvw72r9ajw35rh6sh0twe"); // 		    strong(Xg, Xt, Xh, e);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1nzwhja8l48xyfliiyqjsvegu
// static void compile_clusters(graph_t* g, graph_t* Xg, node_t* top, node_t* bot) 
public static Object compile_clusters(Object... arg) {
UNSUPPORTED("4w43o1w7rtbr7a1ewf1ai3ynx"); // static void compile_clusters(graph_t* g, graph_t* Xg, node_t* top, node_t* bot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("tppzioyoeodu2sq7fsqife44"); //     node_t *rep;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("evmjaaqcnbnbnogx8aaw70ura"); //     graph_t *sub;
UNSUPPORTED("73bera1w406yjis65313aefau"); //     if (is_a_cluster(g) && is_a_strong_cluster(g)) {
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("cpkj2qkslx3blfoouey105509"); // 	    if (agfstin(g, n) == 0) {
UNSUPPORTED("eqsuud7jsawkoiswwrrru1r3a"); // 		rep = ND_rep(find(n));
UNSUPPORTED("4fbgaoqhx6tmezixpndizn8ee"); // 		if (!top) top = makeXnode(Xg,"\177top");
UNSUPPORTED("bp6fj85jbfmup51iezb1m0ceo"); // 		agedge(Xg, top, rep, 0, 1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("f5bjshz39kvzzthd7dqnbg81p"); // 	    if (agfstout(g, n) == 0) {
UNSUPPORTED("eqsuud7jsawkoiswwrrru1r3a"); // 		rep = ND_rep(find(n));
UNSUPPORTED("46t01tbbri501pur6cw0iwvs3"); // 		if (!bot)  bot = makeXnode(Xg,"\177bot");
UNSUPPORTED("5hobrjtzylmppobf8pdnq7rhk"); // 		agedge(Xg, rep, bot, 0, 1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6qtb53kac7myad1p5cy3wf89i"); // 	if (top && bot) {
UNSUPPORTED("2ydmagof3r394ooo3v0twq5us"); // 	    e = agedge(Xg, top, bot, 0, 1);
UNSUPPORTED("5s7xl2rvkq0dxxapwrubcmv9p"); // 	    merge(e, 0, 1000);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bkafc0cyfhu7g619r30g2vtmg"); //     for (sub = agfstsubg(g); sub; sub = agnxtsubg(sub))
UNSUPPORTED("br6bnlza0f68fwkd0sbsfznv5"); // 	compile_clusters(sub, Xg, top, bot);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 93ycqhupenif7m6n70yj2rptv
// static void reverse_edge2(graph_t * g, edge_t * e) 
public static Object reverse_edge2(Object... arg) {
UNSUPPORTED("aa57ihhjujmpk9d1fjnh7uhn2"); // static void reverse_edge2(graph_t * g, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("32j70piql6r327tbseq7o0k28"); //     edge_t *rev;
UNSUPPORTED("ankp6nch7e9a18lqb2heot7yf"); //     rev = (agedge(g,aghead(e),agtail(e),NULL,0));
UNSUPPORTED("8c5hijvd5kr4lynpc8zoa0b9l"); //     if (!rev)
UNSUPPORTED("3tf0rz74jsukbjti74suklmrr"); // 	rev = agedge(g, aghead(e), agtail(e), 0, 1);
UNSUPPORTED("a37ttwpda0n66ej1agvpbx9u"); //     merge(rev, ED_minlen(e), ED_weight(e));
UNSUPPORTED("ae7ymr0zymf63zpp3vzny1kw5"); //     agdelete(g, e);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 75994nd7ifrh2xjk0eei7kj04
// static void dfs(graph_t * g, node_t * v) 
public static Object dfs(Object... arg) {
UNSUPPORTED("e728nuv7n4wyffryp1y6ny8no"); // static void dfs(graph_t * g, node_t * v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9b48a157azcrz2ihzqehhpsvs"); //     edge_t *e, *f;
UNSUPPORTED("9cx417bj13sr8qn5zfj0zllwm"); //     node_t *w;
UNSUPPORTED("701du899u3x4bjilnoar3a2me"); //     if (ND_mark(v))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("3dxrqgsmycugkp3qprexbuawx"); //     ND_mark(v) = NOT(0);
UNSUPPORTED("9z9a3g70rwq874kmknu0re0w0"); //     ND_onstack(v) = NOT(0);
UNSUPPORTED("3twcq4i177ymcmm9hj6l996fr"); //     for (e = agfstout(g, v); e; e = f) {
UNSUPPORTED("9oaziib9dhmc3xyk6ku5rco5a"); // 	f = agnxtout(g, e);
UNSUPPORTED("bqi3fu38n0i7mblfl3ycwdjuo"); // 	w = aghead(e);
UNSUPPORTED("1pq8rawujka41xkc0ujz0c4jl"); // 	if (ND_onstack(w))
UNSUPPORTED("cxzlqoajcjsygpeg1218t5uh8"); // 	    reverse_edge2(g, e);
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("1w7s47988wvu89n176ee6lnii"); // 	    if (ND_mark(w) == 0)
UNSUPPORTED("d5xmvav6dfdviu7ikabr84w6g"); // 		dfs(g, w);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8o0qmsv6vkvey8j3nrtn0z3nd"); //     ND_onstack(v) = 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dt90swbhv55qox6i9anmtxctb
// static void break_cycles(graph_t * g) 
public static Object break_cycles(Object... arg) {
UNSUPPORTED("d7jjxsr59cimfe921b021ndni"); // static void break_cycles(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("3hbdxz1mh0xcb0h1e1gea41u4"); // 	ND_mark(n) = ND_onstack(n) = 0;
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("15bqemmbeo0l42s4hi394weuz"); // 	dfs(g, n);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8wnczo1mkpxdobt1qmszr6m9f
// static void setMinMax (graph_t* g, int doRoot) 
public static Object setMinMax(Object... arg) {
UNSUPPORTED("5hvfkvu5sx7btm870992ll8rq"); // static void setMinMax (graph_t* g, int doRoot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1cpavh3irbeilfgefcbzvnl04"); //     int c, v;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("eygm2jbvlhvevem9cgvzdsnoi"); //     node_t* leader;
UNSUPPORTED("8lepc7wxus3ce9jmpjg54nxtw"); //       /* Do child clusters */
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("8r4qwge4bz0777mdq5rt0ojei"); // 	    setMinMax(GD_clust(g)[c], 0);
UNSUPPORTED("3x63fs1jyz7xhzx9gygufh7wx"); //     if (!GD_parent(g) && !doRoot) // root graph
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("65ovv0jsp2kemyp7179cotrqh"); //     GD_minrank(g) = MAXSHORT;
UNSUPPORTED("4rx9wnlw0uumqfzcjtyg9rpfl"); //     GD_maxrank(g) = -1;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("5wsvwyjckf81tdlqonfb9nvhu"); // 	v = ND_rank(n);
UNSUPPORTED("bbcqt8euig2s31zz814pp52cw"); // 	if (GD_maxrank(g) < v)
UNSUPPORTED("3cfkrpa6pt2hni0tkp45ybvtz"); // 	    GD_maxrank(g) = v;
UNSUPPORTED("3vjkf11cl59z0q8i5mfju17se"); // 	if (GD_minrank(g) > v) {
UNSUPPORTED("cze1fj82fd8cmalgawkse3gy"); // 	    GD_minrank(g) = v;
UNSUPPORTED("73jlqlf2bn3kx7i020menpfcw"); // 	    leader = n;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5j1zsofmtglx1esxwbstti1un"); //     GD_leader(g) = leader;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1rsds60zu7vl5g6sqr3ielup6
// static void readout_levels(graph_t * g, graph_t * Xg, int ncc) 
public static Object readout_levels(Object... arg) {
UNSUPPORTED("5rkflwuoyx87w4zl80k9x22hy"); // static void readout_levels(graph_t * g, graph_t * Xg, int ncc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("889z71siph20icfcbycjqnicp"); //     node_t *xn;
UNSUPPORTED("a76n98ruj81c10y11ge1t5f71"); //     int* minrk = NULL;
UNSUPPORTED("8kdgedl9jvch9df0ltm68vfas"); //     int doRoot = 0;
UNSUPPORTED("65ovv0jsp2kemyp7179cotrqh"); //     GD_minrank(g) = MAXSHORT;
UNSUPPORTED("4rx9wnlw0uumqfzcjtyg9rpfl"); //     GD_maxrank(g) = -1;
UNSUPPORTED("almasgzi7eeks0c5qj1cne0ma"); //     if (ncc > 1) {
UNSUPPORTED("bbuxsg26kpzb2fl660hjri9l8"); // 	int i;
UNSUPPORTED("dl79ga1rb2mw8udgcp9s69msx"); // 	minrk = (int*)zmalloc((ncc+1)*sizeof(int));
UNSUPPORTED("2e6gemf3dl8erdtw782hcarc0"); // 	for (i = 1; i <= ncc; i++)
UNSUPPORTED("dd010jlvkjz1z7fowihz8s25o"); // 	    minrk[i] = MAXSHORT;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("dfofew00g6pxf2nxt4l881l0i"); // 	xn = ND_rep(find(n));
UNSUPPORTED("qp65iwqtnpliswz9lpihtuzc"); // 	ND_rank(n) = ND_rank(xn);
UNSUPPORTED("68vv0lam9vxeuk3fg60ad5w6g"); // 	if (GD_maxrank(g) < ND_rank(n))
UNSUPPORTED("2v65dy95gqvsnppoelwwl8ayh"); // 	    GD_maxrank(g) = ND_rank(n);
UNSUPPORTED("3ozq6tqfxcegom34qkyrrxnfg"); // 	if (GD_minrank(g) > ND_rank(n))
UNSUPPORTED("duyud2jy9uf1rbcis84c3lsj6"); // 	    GD_minrank(g) = ND_rank(n);
UNSUPPORTED("5hf6jaqj5exe9r41fzbjl28ys"); // 	if (minrk) {
UNSUPPORTED("2m02fq9wavpk0rfm07klg50p0"); // 	    ND_hops(n) = ND_hops(xn);
UNSUPPORTED("1kpcg7olwm9sb9fw0cw9xov8p"); // 	    minrk[ND_hops(n)] = MIN(minrk[ND_hops(n)],ND_rank(n));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6grm8jag5924kjntwhi98yt6v"); //     if (minrk) {
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("1xy7cyvcmd5jh134wfd4hkd73"); // 	    ND_rank(n) -= minrk[ND_hops(n)];
UNSUPPORTED("3h50jqntcutttxjjqk9e7qrp9"); // 	/* Non-uniform shifting, so recompute maxrank/minrank of root graph */
UNSUPPORTED("7cxmg4tvzh3us2wx1hpur4ify"); // 	doRoot = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("19s4z0qdne7jghr6qco9pewkb"); //     else if (GD_minrank(g) > 0) {  /* should never happen */
UNSUPPORTED("ekheljktcnka7fv7odg7w4w0b"); // 	int delta = GD_minrank(g);
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("6xuxsgiblao234kr6ifzzngj"); // 	    ND_rank(n) -= delta;
UNSUPPORTED("205yra8usg0d60ou7fdef8sb8"); // 	GD_minrank(g) -= delta;
UNSUPPORTED("1l0r03kgtm5wfa9w1tb2o6pst"); // 	GD_maxrank(g) -= delta;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bfa83r7gfvl4so45ea11h6hs8"); //     setMinMax(g, doRoot);
UNSUPPORTED("1oeld9qf035o8gav2314scxai"); //     /* release fastgraph memory from Xg */
UNSUPPORTED("4uue9kqy9xt1y7qinhilyp73p"); //     for (n = agfstnode(Xg); n; n = agnxtnode(Xg, n)) {
UNSUPPORTED("6uowedfybay0zqxujx4izx5eb"); // 	free_list(ND_in(n));
UNSUPPORTED("9ma0k3ktcua915egahxrgo1f"); // 	free_list(ND_out(n));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("13jrnqbzc7n4ujm1kc140syrh"); //     free(ND_alg(agfstnode(g)));
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("6acp2mmds4i0gjn21ospjdm1i"); // 	ND_alg(n) = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("xlaantr4lfd0cf3p6fhyfjii"); //     if (minrk)
UNSUPPORTED("2quf4kce4g997p8lptbe0s678"); // 	free (minrk);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6wyn9t7y4rni0tldyaap4zsg4
// static void dfscc(graph_t * g, node_t * n, int cc) 
public static Object dfscc(Object... arg) {
UNSUPPORTED("9rlus4sokq2q7mrelfhcyeq9t"); // static void dfscc(graph_t * g, node_t * n, int cc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("6xj79bn6cjnne89udj4eccjbq"); //     if (ND_hops(n) == 0) {
UNSUPPORTED("2ug68j3zfpl1hhipj43mlacw4"); // 	ND_hops(n) = cc;
UNSUPPORTED("8gbd6b2ssf51om8neirzzr6rw"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e))
UNSUPPORTED("1petrc5reriuz99j26l6kvk9g"); // 	    dfscc(g, aghead(e), cc);
UNSUPPORTED("c9yz11pm5cux1tvh62xx2kwle"); // 	for (e = agfstin(g, n); e; e = agnxtin(g, e))
UNSUPPORTED("4gm4tqnmznd5xu1jmel9vdwu0"); // 	    dfscc(g, agtail(e), cc);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4n4sn2k04eosjc6v3amau8l89
// static int connect_components(graph_t * g) 
public static Object connect_components(Object... arg) {
UNSUPPORTED("93546dpzyvd2lgopztw3kyszh"); // static int connect_components(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3zjt2iwedrvoc4tjrhcgrj2kp"); //     int cc = 0;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("5yepsj8ho954ywprktudr66m8"); // 	ND_hops(n) = 0;
UNSUPPORTED("16hw9gw0dz2w7mrtba0eoqrdi"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("12mke9q9ufte7dz3juk9xj03o"); // 	if (ND_hops(n) == 0)
UNSUPPORTED("1hzimiukrcikr5ja8r8jsy6mg"); // 	    dfscc(g, n, ++cc);
UNSUPPORTED("16hhes6efkztzuw8ooeyr7duq"); //     if (cc > 1) {
UNSUPPORTED("aen3uv1t10s8e76c30y8j0051"); // 	node_t *root = makeXnode(g, "\177root");
UNSUPPORTED("bzbr2vqual2twjcg2p2sffsd4"); // 	int ncc = 1;
UNSUPPORTED("attp4bsjqe99xnhi7lr7pszar"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("eci2j6e8io07039308zl0no88"); // 	    if (ND_hops(n) == ncc) {
UNSUPPORTED("7rj8qzz8d2u85i7gf6c4rs62m"); // 		(void) agedge(g, root, n, 0, 1);
UNSUPPORTED("cgm8ehtyoyn2ybwnrxvfx1cv8"); // 		ncc++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("95z6nz9mlol4p31l239u0zyz1"); //     return (cc);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9lj0r4q3ulap7ly9cvvqn3d0t
// static void add_fast_edges (graph_t * g) 
public static Object add_fast_edges(Object... arg) {
UNSUPPORTED("9od2j2a8s9ki669jghjqrkcym"); // static void add_fast_edges (graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("1xm961cuv38vb1rckbyfsk5a8"); // 	    elist_append(e, ND_out(n));
UNSUPPORTED("30covjl73nxramgd8lw1apbkm"); // 	    elist_append(e, ND_in(aghead(e)));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2xyhunzw903dytfpyy63oznhz
// static void my_init_graph(Agraph_t *g, Agobj_t *graph, void *arg) 
public static Object my_init_graph(Object... arg) {
UNSUPPORTED("3s5gmr0i7qaf8sa79h9ek2t42"); // static void my_init_graph(Agraph_t *g, Agobj_t *graph, void *arg)
UNSUPPORTED("12ubgsqsc8d2oeobshclx2e9m"); // { int *sz = arg; agbindrec(graph,"level graph rec",sz[0],NOT(0)); }

throw new UnsupportedOperationException();
}




//3 1vibj3ycvfkl07m1a2dzr3qf3
// static void my_init_node(Agraph_t *g, Agobj_t *node, void *arg) 
public static Object my_init_node(Object... arg) {
UNSUPPORTED("448m4h3oktm1kylrq3iss782r"); // static void my_init_node(Agraph_t *g, Agobj_t *node, void *arg)
UNSUPPORTED("3mooa3mwczxhc97acdygow0sx"); // { int *sz = arg; agbindrec(node,"level node rec",sz[1],NOT(0)); }

throw new UnsupportedOperationException();
}




//3 avor9syqevkn2jo4yf8whbg5f
// static void my_init_edge(Agraph_t *g, Agobj_t *edge, void *arg) 
public static Object my_init_edge(Object... arg) {
UNSUPPORTED("bo6poh3fcfvu02sod3dyzjhsi"); // static void my_init_edge(Agraph_t *g, Agobj_t *edge, void *arg)
UNSUPPORTED("4f0jw3wys20zimucjor8qrhzg"); // { int *sz = arg; agbindrec(edge,"level edge rec",sz[2],NOT(0)); }

throw new UnsupportedOperationException();
}


//1 46nx7przjp7fn42o28hqdaj9k
// static Agcbdisc_t mydisc = 


//1 9quqxw3oujl7u6bymimr5be7t
// int infosizes[] = 




//3 590k5zi3mrpwbc3lib0w3rmr2
// void dot2_rank(graph_t * g, aspect_t* asp) 
public static Object dot2_rank(Object... arg) {
UNSUPPORTED("d8gu9ua6rerpv9vz9ctco1ca2"); // void dot2_rank(graph_t * g, aspect_t* asp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("23k65agnd27tv4ix9teds9e2t"); //     int ssize;
UNSUPPORTED("dx1unsp79t4ji8dh8idt48jrc"); //     int ncc, maxiter = INT_MAX;
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("dxlxz9md3d6r12wog4x5sc7td"); //     graph_t *Xg;
UNSUPPORTED("hibhvgkp511r6u6ips8yb0un"); //     Last_node = NULL;
UNSUPPORTED("ey4p0fjtw4ac18jh9svmzjs23"); //     Xg = agopen("level assignment constraints", Agstrictdirected, 0);
UNSUPPORTED("e4j7z7nfe33svydzyn4w6abcy"); //     agbindrec(Xg,"level graph rec",sizeof(Agraphinfo_t),NOT(0));
UNSUPPORTED("4j4bkw2k5v7xlf7ycqcrz8qip"); //     agpushdisc(Xg,&mydisc,infosizes);
UNSUPPORTED("d4pjn5ef0ywzmhe2fshhm8bvn"); //     edgelabel_ranks(g);
UNSUPPORTED("e0rdg08m66a12fiixgkjnyrbj"); //     if ((s = agget(g, "nslimit1")))
UNSUPPORTED("9tp2zk1tsr4ce9rwsr0is9u3o"); // 	maxiter = atof(s) * agnnodes(g);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("dapt7hf1vwq593la2oydyrv27"); // 	maxiter = INT_MAX;
UNSUPPORTED("62k95fm4s1z2wzcyg28ir0x7u"); //     compile_samerank(g, 0);
UNSUPPORTED("bh9imh5owlj1c9ad7mime392x"); //     compile_nodes(g, Xg);
UNSUPPORTED("3kxtahvovojtzi6qqnrricpoo"); //     compile_edges(g, Xg);
UNSUPPORTED("9twf7u3r2hzeic9w0gmvh10bc"); //     compile_clusters(g, Xg, 0, 0);
UNSUPPORTED("cwrov5g30logh4g9omvkblonh"); //     break_cycles(Xg);
UNSUPPORTED("3ficrpbhiwichejg6n1hshz7k"); //     ncc = connect_components(Xg);
UNSUPPORTED("9x72se4xuqwfv27jlqpmivrwb"); //     add_fast_edges (Xg);
UNSUPPORTED("2yazmwrpb1ni51wuck3ruvi2j"); //     if (asp) {
UNSUPPORTED("8ow3lzc6gh107g9bcn4szm7hj"); // 	init_UF_size(Xg);
UNSUPPORTED("9503vlimf1i2zv76ua88ooepc"); // 	initEdgeTypes(Xg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b65fc1791mzxccp9zzxi8vk12"); //     if ((s = agget(g, "searchsize")))
UNSUPPORTED("aqd144wenl3zq15bwc41u9aha"); // 	ssize = atoi(s);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2n4z8w1w3il45lik0kraspkud"); // 	ssize = -1;
UNSUPPORTED("aotd35u0hficqt6hlkw8xof03"); //     rank2(Xg, 1, maxiter, ssize);
UNSUPPORTED("4x9mvgxbdou6xj4n98rwzucgi"); // /* fastgr(Xg); */
UNSUPPORTED("8un6x92pzddrzsnq8y95af4m6"); //     readout_levels(g, Xg, ncc);
UNSUPPORTED("6rs6sp7mefzzbf02kfmvycnaq"); //     agclose(Xg);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
