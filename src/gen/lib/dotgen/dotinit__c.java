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
 * (C) Copyright 2009-2022, Arnaud Roques
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
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.graph__c.agnnodes;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.rec__c.agbindrec;
import static gen.lib.cgraph.subg__c.agfstsubg;
import static gen.lib.cgraph.subg__c.agnxtsubg;
import static gen.lib.common.postproc__c.dotneato_postprocess;
import static gen.lib.common.utils__c.common_init_edge;
import static gen.lib.common.utils__c.common_init_node;
import static gen.lib.common.utils__c.gv_nodesize;
import static gen.lib.common.utils__c.late_int;
import static gen.lib.common.utils__c.late_string;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.setEdgeType;
import static gen.lib.dotgen.aspect__c.setAspect;
import static gen.lib.dotgen.class1__c.nonconstraint_edge;
import static gen.lib.dotgen.compound__c.dot_compoundEdges;
import static gen.lib.dotgen.dotsplines__c.dot_splines;
import static gen.lib.dotgen.mincross__c.dot_mincross;
import static gen.lib.dotgen.position__c.dot_position;
import static gen.lib.dotgen.rank__c.dot_rank;
import static gen.lib.dotgen.sameport__c.dot_sameports;
import static gen.lib.pack.pack__c.getPack;
import static gen.lib.pack.pack__c.getPackInfo;
import static gen.lib.pack.pack__c.getPackModeInfo;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.enumAsInt;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.CL_OFFSET;
import static smetana.core.Macro.ED_count;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_showboxes;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.ET_SPLINE;
import static smetana.core.Macro.GD_dotroot;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.NEW_RANK;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.agfindgraphattr;
import static smetana.core.Macro.alloc_elist;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_aspect_t;
import h.ST_pack_info;
import h.ST_pointf;
import h.pack_mode;
import smetana.core.CString;
import smetana.core.Z;
import smetana.core.__ptr__;

public class dotinit__c {




//3 ciez0pfggxdljedzsbklq49f0
// static inline point pointof(int x, int y) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="pointof", key="ciez0pfggxdljedzsbklq49f0", definition="static inline point pointof(int x, int y)")
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






//3 7cufnfitrh935ew093mw0i4b7
// static inline box boxof(int llx, int lly, int urx, int ury) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="boxof", key="7cufnfitrh935ew093mw0i4b7", definition="static inline box boxof(int llx, int lly, int urx, int ury)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="add_point", key="1n5xl70wxuabyf97mclvilsm6", definition="static inline point add_point(point p, point q)")
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




//3 ai2dprak5y6obdsflguh5qbd7
// static inline point sub_point(point p, point q) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="sub_point", key="ai2dprak5y6obdsflguh5qbd7", definition="static inline point sub_point(point p, point q)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="sub_pointf", key="16f6pyogcv3j7n2p0n8giqqgh", definition="static inline pointf sub_pointf(pointf p, pointf q)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="mid_point", key="9k50jgrhc4f9824vf8ony74rw", definition="static inline point mid_point(point p, point q)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="mid_pointf", key="59c4f7im0ftyowhnzzq2v9o1x", definition="static inline pointf mid_pointf(pointf p, pointf q)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="interpolate_pointf", key="5r18p38gisvcx3zsvbb9saixx", definition="static inline pointf interpolate_pointf(double t, pointf p, pointf q)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="exch_xy", key="bxzrv2ghq04qk5cbyy68s4mol", definition="static inline point exch_xy(point p)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="exch_xyf", key="9lt3e03tac6h6sydljrcws8fd", definition="static inline pointf exch_xyf(pointf p)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="box_bb", key="8l9qhieokthntzdorlu5zn29b", definition="static inline box box_bb(box b0, box b1)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="boxf_bb", key="clws9h3bbjm0lw3hexf8nl4c4", definition="static inline boxf boxf_bb(boxf b0, boxf b1)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="box_intersect", key="bit6ycxo1iqd2al92y8gkzlvb", definition="static inline box box_intersect(box b0, box b1)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="boxf_intersect", key="8gfybie7k6pgb3o1a6llgpwng", definition="static inline boxf boxf_intersect(boxf b0, boxf b1)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="box_overlap", key="7z8j2quq65govaaejrz7b4cvb", definition="static inline int box_overlap(box b0, box b1)")
public static Object box_overlap(Object... arg) {
UNSUPPORTED("1e9k599x7ygct7r4cfdxlk9u9"); // static inline int box_overlap(box b0, box b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7a9wwpu7dhdphd08y1ecw54w5"); //     return OVERLAP(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4z0suuut2acsay5m8mg9dqjdu
// static inline int boxf_overlap(boxf b0, boxf b1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="boxf_overlap", key="4z0suuut2acsay5m8mg9dqjdu", definition="static inline int boxf_overlap(boxf b0, boxf b1)")
public static Object boxf_overlap(Object... arg) {
UNSUPPORTED("905nejsewihwhhc3bhnrz9nwo"); // static inline int boxf_overlap(boxf b0, boxf b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7a9wwpu7dhdphd08y1ecw54w5"); //     return OVERLAP(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dd34swz5rmdgu3a2np2a4h1dy
// static inline int box_contains(box b0, box b1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="box_contains", key="dd34swz5rmdgu3a2np2a4h1dy", definition="static inline int box_contains(box b0, box b1)")
public static Object box_contains(Object... arg) {
UNSUPPORTED("aputfc30fjkvy6jx4otljaczq"); // static inline int box_contains(box b0, box b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("87ap80vrh2a4gpprbxr33lrg3"); //     return CONTAINS(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8laj1bspbu2i1cjd9upr7xt32
// static inline int boxf_contains(boxf b0, boxf b1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="boxf_contains", key="8laj1bspbu2i1cjd9upr7xt32", definition="static inline int boxf_contains(boxf b0, boxf b1)")
public static Object boxf_contains(Object... arg) {
UNSUPPORTED("7ccnttkiwt834yfyw0evcm18v"); // static inline int boxf_contains(boxf b0, boxf b1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("87ap80vrh2a4gpprbxr33lrg3"); //     return CONTAINS(b0, b1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4wf5swkz24xx51ja2dynbycu1
// static inline pointf perp (pointf p) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="perp", key="4wf5swkz24xx51ja2dynbycu1", definition="static inline pointf perp (pointf p)")
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
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="scale", key="6dtlpzv4mvgzb9o0b252yweuv", definition="static inline pointf scale (double c, pointf p)")
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




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_init_subg", key="cmr94z4p2bdeiply1d4wrqwes", definition="static void dot_init_subg(graph_t * g, graph_t* droot)")
public static void dot_init_subg(ST_Agraph_s g, ST_Agraph_s droot) {
ENTERING("cmr94z4p2bdeiply1d4wrqwes","dot_init_subg");
try {
    ST_Agraph_s subg;
    
    if (NEQ(g, agroot(g)))
	agbindrec(g, new CString("Agraphinfo_t"), sizeof(ST_Agraphinfo_t.class), true);
    if (EQ(g, droot))
	GD_dotroot(agroot(g), droot);
    
    for (subg = agfstsubg(g); subg!=null; subg = agnxtsubg(subg)) {
	dot_init_subg(subg, droot);
    }
} finally {
LEAVING("cmr94z4p2bdeiply1d4wrqwes","dot_init_subg");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_init_node", key="3hk92jbrfjmn6no3svn9jvje9", definition="static void  dot_init_node(node_t * n)")
public static void dot_init_node(ST_Agnode_s n) {
ENTERING("3hk92jbrfjmn6no3svn9jvje9","dot_init_node");
try {
    agbindrec(n, new CString("Agnodeinfo_t"), sizeof(ST_Agnodeinfo_t.class), true);	//graph custom data
    common_init_node(n);
    gv_nodesize(n, GD_flip(agraphof(n)));
    alloc_elist(4, ND_in(n));
    alloc_elist(4, ND_out(n));
    alloc_elist(2, ND_flat_in(n));
    alloc_elist(2, ND_flat_out(n));
    alloc_elist(2, ND_other(n));
    ND_UF_size(n, 1);
} finally {
LEAVING("3hk92jbrfjmn6no3svn9jvje9","dot_init_node");
}
}



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_init_edge", key="zbvhnhd78bppq8wb872847bj", definition="static void  dot_init_edge(edge_t * e)")
public static void dot_init_edge(ST_Agedge_s e) {
ENTERING("zbvhnhd78bppq8wb872847bj","dot_init_edge");
try {
    CString tailgroup, headgroup;
    agbindrec(e, new CString("Agedgeinfo_t"), sizeof(ST_Agedgeinfo_t.class), true);	//graph custom data
    common_init_edge(e);
    
    
    ED_weight(e, late_int(e, Z.z().E_weight, 1, 0));
    tailgroup = late_string(agtail(e), Z.z().N_group, new CString(""));
    headgroup = late_string(aghead(e), Z.z().N_group, new CString(""));
    ED_count(e, 1);
    ED_xpenalty(e, 1);
    if (tailgroup.charAt(0)!='\0' && (tailgroup.isSame(headgroup))) {
	UNSUPPORTED("atjraranegsdjegclykesn5gx"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->xpenalty) = 1000;
	UNSUPPORTED("5y0yunmvmngg67c9exlbn6jbk"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->weight) *= 100;
    }
    if (nonconstraint_edge(e)) {
	UNSUPPORTED("54niz21n2omf1i9v67brdid9w"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->xpenalty) = 0;
	UNSUPPORTED("2v5u5jdguhhn7vjihniotrml0"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->weight) = 0;
    }
    
    
    ED_showboxes(e, late_int(e, Z.z().E_showboxes, 0, 0));
    ED_minlen(e, late_int(e, Z.z().E_minlen, 1, 0));
} finally {
LEAVING("zbvhnhd78bppq8wb872847bj","dot_init_edge");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_init_node_edge", key="2ylyhz7macit0ts1hap2tg3wy", definition="void  dot_init_node_edge(graph_t * g)")
public static void dot_init_node_edge(ST_Agraph_s g) {
ENTERING("2ylyhz7macit0ts1hap2tg3wy","dot_init_node_edge");
try {
	ST_Agnode_s n;
	ST_Agedge_s e;
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n))
	dot_init_node(n);
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e))
	    dot_init_edge(e);
    }
} finally {
LEAVING("2ylyhz7macit0ts1hap2tg3wy","dot_init_node_edge");
}
}




//3 5z1h7gr0tgapjvuc2z9st9xjr
// static void  dot_cleanup_node(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_cleanup_node", key="5z1h7gr0tgapjvuc2z9st9xjr", definition="static void  dot_cleanup_node(node_t * n)")
public static Object dot_cleanup_node(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("3wd33alc21mdt92sw3x9gl65j"); // dot_cleanup_node(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bft238x0kn16j4rew2s49lf8p"); //     free_list(ND_in(n));
UNSUPPORTED("9k0f5njfz83ho3jgj8a6l97ia"); //     free_list(ND_out(n));
UNSUPPORTED("9hf5u4ow9x2bxsh5roh3klohe"); //     free_list(ND_flat_out(n));
UNSUPPORTED("d5cejyb0j1nhixps76lgii0fr"); //     free_list(ND_flat_in(n));
UNSUPPORTED("er7qgmylfnw39xn97jixe3cqp"); //     free_list(ND_other(n));
UNSUPPORTED("dv63sk5dujcwfkf99o6ponzqm"); //     free_label(ND_label(n));
UNSUPPORTED("5or6zu6ycx4zage9ggy1o9it4"); //     free_label(ND_xlabel(n));
UNSUPPORTED("4wkmsp7365vb7u5fqtm2buotu"); //     if (ND_shape(n))
UNSUPPORTED("2yujce4phniaiwg4fq8up6xu7"); // 	ND_shape(n)->fns->freefn(n);
UNSUPPORTED("4zk1aro1ispxkab9ee4dd0tf"); //     agdelrec(n, "Agnodeinfo_t");	
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5x42drpgjuyzb9oapi7osqxb6
// static void free_virtual_edge_list(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="free_virtual_edge_list", key="5x42drpgjuyzb9oapi7osqxb6", definition="static void free_virtual_edge_list(node_t * n)")
public static Object free_virtual_edge_list(Object... arg) {
UNSUPPORTED("2kwoaiz0pcpatrbztzebypz24"); // static void free_virtual_edge_list(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("4johvk6fhfn8nimnwv1bayucm"); //     for (i = ND_in(n).size - 1; i >= 0; i--) {
UNSUPPORTED("dkwp8r3x5yxwt373copif99qc"); // 	e = ND_in(n).list[i];
UNSUPPORTED("d0cytt2j3orasq0977h5sdypp"); // 	delete_fast_edge(e);
UNSUPPORTED("3ersod3eshpdnzwy1yb73olpy"); // 	free(e->base.data);
UNSUPPORTED("7eb6ytlppce1o1ihl2yf1mb6w"); // 	free(e);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dyyfpe9giifeythxw1ecgc9gw"); //     for (i = ND_out(n).size - 1; i >= 0; i--) {
UNSUPPORTED("d0wu190s7vunqmaadd9r01j7m"); // 	e = ND_out(n).list[i];
UNSUPPORTED("d0cytt2j3orasq0977h5sdypp"); // 	delete_fast_edge(e);
UNSUPPORTED("3ersod3eshpdnzwy1yb73olpy"); // 	free(e->base.data);
UNSUPPORTED("7eb6ytlppce1o1ihl2yf1mb6w"); // 	free(e);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bzy6cpyjjzfeesjlq49c5sfmo
// static void free_virtual_node_list(node_t * vn) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="free_virtual_node_list", key="bzy6cpyjjzfeesjlq49c5sfmo", definition="static void free_virtual_node_list(node_t * vn)")
public static Object free_virtual_node_list(Object... arg) {
UNSUPPORTED("b8mii2q777xhhq7ugpswc15so"); // static void free_virtual_node_list(node_t * vn)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3lk7715piw6ktz28izcuw3l10"); //     node_t *next_vn;
UNSUPPORTED("cs50uo5tdqm5ri532weezud0q"); //     while (vn) {
UNSUPPORTED("1zc0twfsq0pvs72obqpqn3jt7"); // 	next_vn = ND_next(vn);
UNSUPPORTED("6v1txca0lc0m7vffh6cxe7s0v"); // 	free_virtual_edge_list(vn);
UNSUPPORTED("ar8qtrvshtnclyd1lwsud1fe1"); // 	if (ND_node_type(vn) == 1) {
UNSUPPORTED("52z735rh4jv9he0idjly61jel"); // 	    free_list(ND_out(vn));
UNSUPPORTED("2cx91nqjcw6sdd1j219nb29ty"); // 	    free_list(ND_in(vn));
UNSUPPORTED("f2pq0qi224q8wspagt6qwoxdp"); // 	    free(vn->base.data);
UNSUPPORTED("3lo6egckclz8tk579663nqark"); // 	    free(vn);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1jra8cgf1nhojwmj7gyucvoni"); // 	vn = next_vn;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 colq6urkwrm9hh0zggl873dla
// static void  dot_cleanup_graph(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_cleanup_graph", key="colq6urkwrm9hh0zggl873dla", definition="static void  dot_cleanup_graph(graph_t * g)")
public static Object dot_cleanup_graph(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("84r7i6e9lays55x1yan1d0nlp"); // dot_cleanup_graph(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("8uujemixuhlf040icq3zsh7j8"); //     graph_t *subg;
UNSUPPORTED("92vjk6rjnnnji7bcczz51lwfx"); //     for (subg = agfstsubg(g); subg; subg = agnxtsubg(subg)) {
UNSUPPORTED("ykczo4avgse9arffpd1plmme"); // 	dot_cleanup_graph(subg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("12nbb17pw6fg2wwg5c7i800xv"); //     if (GD_clust(g)) free (GD_clust(g));
UNSUPPORTED("cw69q4tfzxmrd72fi1vz2be1g"); //     if (GD_rankleader(g)) free (GD_rankleader(g));
UNSUPPORTED("cnrd9322z9c4eqvr450ughbzh"); //     free_list(GD_comp(g));
UNSUPPORTED("djox731ykith2qz2trtp7zgzf"); //     if (GD_rank(g)) {
UNSUPPORTED("1vkvarx8mfy1iq12oyq5rtzpa"); // 	for (i = GD_minrank(g); i <= GD_maxrank(g); i++)
UNSUPPORTED("32lqmila03wshgx9x3686apbo"); // 	    free(GD_rank(g)[i].av);
UNSUPPORTED("dnv6plmiyr74l3u1aqc16qmiz"); // 	if (GD_minrank(g) == -1)
UNSUPPORTED("b0hpil9jwq1zov1klvpd6vbnf"); // 	    free(GD_rank(g)-1);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("eqir7eaetsm1anqchd3kb8q8t"); // 	    free(GD_rank(g));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("58o9b8zmlqcq96deeorllidce"); //     if (g != agroot(g)) 
UNSUPPORTED("f5hqyohefj8o12bvcv17xpswc"); // 	agdelrec(g,"Agraphinfo_t");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2uv40x0vx6nmbvld01reukyly
// void dot_cleanup(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_cleanup", key="2uv40x0vx6nmbvld01reukyly", definition="void dot_cleanup(graph_t * g)")
public static Object dot_cleanup(Object... arg) {
UNSUPPORTED("a2tqiktifcbodk46i6x0ux9j1"); // void dot_cleanup(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("v8twirr9ihzxnb06d6x6nxrd"); //     free_virtual_node_list(GD_nlist(g));
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("e20lm4qtccvgsfq5fzjv6sjyl"); // 	for (e = agfstout(g, n); e; e = agnxtout(g, e)) {
UNSUPPORTED("5otoqd7o1zz8ni50urywr7yjy"); // 	    gv_cleanup_edge(e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("yis08tp9td0x00hxoxqopt6b"); // 	dot_cleanup_node(n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2w88m44roqcqg67kuhg5k4agu"); //     dot_cleanup_graph(g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4jt1p8taqjuk4atqpwbxkru3g
// static void remove_from_rank (Agraph_t * g, Agnode_t* n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="remove_from_rank", key="4jt1p8taqjuk4atqpwbxkru3g", definition="static void remove_from_rank (Agraph_t * g, Agnode_t* n)")
public static Object remove_from_rank(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9axg5z61bd5rkf0cbubkto6eb"); // remove_from_rank (Agraph_t * g, Agnode_t* n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5v9scbkty9fkcyo8yw7jmkokj"); //     Agnode_t* v = NULL;
UNSUPPORTED("86gmm9qxyhkzst815hr54r1sp"); //     int j, rk = ND_rank(n);
UNSUPPORTED("7bstlpzg93apc08cfgp386p7m"); //     for (j = 0; j < GD_rank(g)[rk].n; j++) {
UNSUPPORTED("5oq2646mpw8p4gen80z8g8baz"); // 	v = GD_rank(g)[rk].v[j];
UNSUPPORTED("aby82dr89m0xbcj8mya7f9fvx"); // 	if (v == n) {
UNSUPPORTED("5494ozi7uqin7n7xlbkqrsahd"); // 	    for (j++; j < GD_rank(g)[rk].n; j++) {
UNSUPPORTED("a4l7560unq1yw14pck0v0yo6x"); // 		GD_rank(g)[rk].v[j-1] = GD_rank(g)[rk].v[j];
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("8pdkxsqmvoce243jar5hwxl3u"); // 	    GD_rank(g)[rk].n--;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cytanc5i1hmu0ag0vz4awfpi"); //     assert (v == n);  /* if found */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 xgvyh3kinj6cbnknb5oo9qfr
// static void removeFill (Agraph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="removeFill", key="xgvyh3kinj6cbnknb5oo9qfr", definition="static void removeFill (Agraph_t * g)")
public static Object removeFill(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("8asq16rjvm285smlce6dul5nq"); // removeFill (Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ci2zh69w6nhi0q816i1ixuy9k"); //     Agnode_t* n;
UNSUPPORTED("167at4ws3rrdv3znbvpsnb83o"); //     Agnode_t* nxt;
UNSUPPORTED("22j86kdb5hbziiedfsspj3ipn"); //     Agraph_t* sg = agsubg (g, "_new_rank", 0);
UNSUPPORTED("1pjx7ke9o4h5tgq0cs9jfui7j"); //     if (!sg) return;
UNSUPPORTED("5sms8ok354044bziyaq1kfv93"); //     for (n = agfstnode(sg); n; n = nxt) {
UNSUPPORTED("18crodqmo1f1apt7bwq0mv72m"); // 	nxt = agnxtnode(sg, n);
UNSUPPORTED("227ays2g6u04e210i8wp3jntu"); // 	delete_fast_node (g, n);
UNSUPPORTED("2tp9t78wzq032fbm3msk2gpfx"); // 	remove_from_rank (g, n);
UNSUPPORTED("cyyixtb6lqfain86edffcd92b"); // 	dot_cleanup_node (n);
UNSUPPORTED("3xjgsp211uvaug1aa3mvpdlnc"); // 	agdelnode(g, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9ar3yxaq5s0lagg5bw59suv8u"); //     agdelsubg (g, sg);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 63o8ieaetc5apjocrd74fobt0
// static void attach_phase_attrs (Agraph_t * g, int maxphase) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="attach_phase_attrs", key="63o8ieaetc5apjocrd74fobt0", definition="static void attach_phase_attrs (Agraph_t * g, int maxphase)")
public static Object attach_phase_attrs(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("adogsvx2s8kkwixbfl53zj175"); // attach_phase_attrs (Agraph_t * g, int maxphase)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("28snkvv05tay2b94o220j0rkx"); //     Agsym_t* rk = agattr(g,AGNODE,"rank","");
UNSUPPORTED("2mf6hpet62ccb0nfggxptz459"); //     Agsym_t* order = agattr(g,AGNODE,"order","");
UNSUPPORTED("ci2zh69w6nhi0q816i1ixuy9k"); //     Agnode_t* n;
UNSUPPORTED("cf8w5z35zxww7q6yjin9lc9op"); //     char buf[BUFSIZ];
UNSUPPORTED("7wq24g054kmx3aw25vk5ksj4"); //     for (n = agfstnode(g); n; n = agnxtnode(g,n)) {
UNSUPPORTED("16p5t5iyomfedg6du3xoszhzq"); // 	if (maxphase >= 1) {
UNSUPPORTED("bwgz5xhm64nl5wtnj9vfs1rx7"); // 	    sprintf(buf, "%d", ND_rank(n));
UNSUPPORTED("42xwkqdgcponmubqyxg876oo6"); // 	    agxset(n,rk,buf);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("93x84236b3xx5glpnx5xseeg0"); // 	if (maxphase >= 2) {
UNSUPPORTED("dt1ra0nrgwwdbm8fmxvgcz16x"); // 	    sprintf(buf, "%d", ND_order(n));
UNSUPPORTED("5e6vrutroh3qimffd0rioyhnz"); // 	    agxset(n,order,buf);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dotLayout", key="7t18nggek2s9vvb5opwqa8rwr", definition="static void dotLayout(Agraph_t * g)")
public static void dotLayout(ST_Agraph_s g) {
ENTERING("7t18nggek2s9vvb5opwqa8rwr","dotLayout");
try {
	ST_aspect_t aspect = new ST_aspect_t();
	ST_aspect_t asp;
    int maxphase = late_int(g, agfindgraphattr(g,"phase"), -1, 1);
    
    setEdgeType (g, ET_SPLINE);
    asp = setAspect (g, aspect);
    
    dot_init_subg(g,g);
    dot_init_node_edge(g);
    
    do {
        dot_rank(g, asp);
	if (maxphase == 1) {
	    attach_phase_attrs (g, 1);
	    return;
	}
	if (aspect.badGraph!=0) {
UNSUPPORTED("1yu5j8tk43i6jlmu8wk9jks15"); // 	    agerr(AGWARN, "dot does not support the aspect attribute for disconnected graphs or graphs with clusters\n");
UNSUPPORTED("5uwp9z6jkv5uc30iyfszyg6dw"); // 	    asp = NULL;
UNSUPPORTED("28kbszyxsjoj03gb134ov4hag"); // 	    aspect.nextIter = 0;
	}
        dot_mincross(g, (asp != null));
	if (maxphase == 2) {
	    attach_phase_attrs (g, 2);
	    return;
	}
        dot_position(g, asp);
	if (maxphase == 3) {
	    attach_phase_attrs (g, 2);  /* positions will be attached on output */
	    return;
	}
	aspect.nPasses--;
    } while (aspect.nextIter!=0 && aspect.nPasses!=0);
    if ((GD_flags(g) & NEW_RANK)!=0)
	removeFill (g);
    dot_sameports(g);
    dot_splines(g);
    if (mapbool(agget(g, new CString("compound"))))
	dot_compoundEdges(g);
} finally {
LEAVING("7t18nggek2s9vvb5opwqa8rwr","dotLayout");
}
}




//3 j18uqz16uc5895o8w92sc8us
// static void initSubg (Agraph_t* sg, Agraph_t* g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="initSubg", key="j18uqz16uc5895o8w92sc8us", definition="static void initSubg (Agraph_t* sg, Agraph_t* g)")
public static Object initSubg(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("dxv1kymai2xe3m4wblwapgffu"); // initSubg (Agraph_t* sg, Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3oolj8gqkndhoy95ebyjmjt0w"); //     agbindrec(sg, "Agraphinfo_t", sizeof(Agraphinfo_t), NOT(0));
UNSUPPORTED("2um641xaifqq107czm0htblc3"); //     GD_drawing(sg) = (layout_t*)zmalloc(sizeof(layout_t));
UNSUPPORTED("bfocldqmll6ddnmq3h3ci6njt"); //     GD_drawing(sg)->quantum = GD_drawing(g)->quantum; 
UNSUPPORTED("7cnwfvm4lswn7kvucss74krd3"); //     GD_drawing(sg)->dpi = GD_drawing(g)->dpi;
UNSUPPORTED("7kbbs52ycpyuytqi61m5vme4c"); //     GD_gvc(sg) = GD_gvc (g);
UNSUPPORTED("8flpf5avt4eg89sc16bgp9v13"); //     GD_charset(sg) = GD_charset (g);
UNSUPPORTED("d9o7bxs9hl0r5ma0lt9oejmqv"); //     GD_rankdir2(sg) = GD_rankdir2 (g);
UNSUPPORTED("74762qecxnstp743fwt31hk0p"); //     GD_nodesep(sg) = GD_nodesep(g);
UNSUPPORTED("b29jhl6zjh8f8jqfb6okoixby"); //     GD_ranksep(sg) = GD_ranksep(g);
UNSUPPORTED("bctcse1zj9vx2p0in9p4jhso3"); //     GD_fontnames(sg) = GD_fontnames(g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9pe0e6i01ks2zju1xjzgg39v1
// static void attachPos (Agraph_t* g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="attachPos", key="9pe0e6i01ks2zju1xjzgg39v1", definition="static void attachPos (Agraph_t* g)")
public static Object attachPos(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("d606brtp0hf4k8bamafqhu08b"); // attachPos (Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8fmu9hadea4uw8fwzoexoxab"); //     node_t* np;
UNSUPPORTED("5cl2cj5wj7w1zr7qjww0s4cdf"); //     double* ps = (double*)zmalloc((2*agnnodes(g))*sizeof(double));
UNSUPPORTED("73rfwkv8ws65l7qgnz7jw27gf"); //     for (np = agfstnode(g); np; np = agnxtnode(g, np)) {
UNSUPPORTED("7tbl6es7oks3vntqoetg24xa4"); // 	ND_pos(np) = ps;
UNSUPPORTED("9qwq8ll4gb28hun94xos9i4ud"); // 	ps[0] = ((ND_coord(np).x)/(double)72);
UNSUPPORTED("5z9ztec30pze3mzecneza61qq"); // 	ps[1] = ((ND_coord(np).y)/(double)72);
UNSUPPORTED("aeuy5zibx9et1ncwcqwlr3s3m"); // 	ps += 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eufhyu2dccn1mleg8a43s9k2h
// static void resetCoord (Agraph_t* g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="resetCoord", key="eufhyu2dccn1mleg8a43s9k2h", definition="static void resetCoord (Agraph_t* g)")
public static Object resetCoord(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("caofcucw710wwaiwfz4650zsz"); // resetCoord (Agraph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ds7ppzwt5kjihc3hp8q0ncyl"); //     node_t* np = agfstnode(g);
UNSUPPORTED("5s8nx20pwknmsow15z16muyab"); //     double* sp = ND_pos(np);
UNSUPPORTED("5u1fte9td0znzxeayi62ozq8a"); //     double* ps = sp;
UNSUPPORTED("73rfwkv8ws65l7qgnz7jw27gf"); //     for (np = agfstnode(g); np; np = agnxtnode(g, np)) {
UNSUPPORTED("8p3hwwbq7seyn0wpf7llgw1xd"); // 	ND_pos(np) = 0;
UNSUPPORTED("a33fx4q39yal0dnv915x71zia"); // 	ND_coord(np).x = ((ps[0])*(double)72);
UNSUPPORTED("avms8890ml5c9jclp66fpto3x"); // 	ND_coord(np).y = ((ps[1])*(double)72);
UNSUPPORTED("aeuy5zibx9et1ncwcqwlr3s3m"); // 	ps += 2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("btsriptlygrzlk6ojhvss234j"); //     free (sp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




/* doDot:
 * Assume g has nodes.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="doDot", key="nedairhdof6qkmjjoh4h68zy", definition="static void doDot (Agraph_t* g)")
public static void doDot(ST_Agraph_s g) {
ENTERING("nedairhdof6qkmjjoh4h68zy","doDot");
try {
    ST_Agraph_s ccs[];
    ST_Agraph_s sg;
    int ncc;
    int i;
    final ST_pack_info pinfo = new ST_pack_info();
    int Pack = getPack(g, -1, CL_OFFSET);
    int mode = getPackModeInfo (g, enumAsInt(pack_mode.class, "l_undef"), pinfo);
    getPackInfo(g, enumAsInt(pack_mode.class, "l_node"), 8, pinfo);
    if ((mode == enumAsInt(pack_mode.class, "l_undef")) && (Pack < 0)) {
	/* No pack information; use old dot with components
         * handled during layout
         */
	dotLayout(g);
    } else {
UNSUPPORTED("952usp51fee2pbidl2frwpq2x"); // 	/* fill in default values */
UNSUPPORTED("7d8flcn5zht92nop46f168hf9"); // 	if (mode == l_undef) 
UNSUPPORTED("bf04f2a6do2dovxgq57ta3qz5"); // 	    pinfo.mode = l_node;
UNSUPPORTED("7l7lph3yd7o3jelejupsiytnc"); // 	else if (Pack < 0)
UNSUPPORTED("7odp6lwv29g4nwpfvqt8hkmve"); // 	    Pack = 8;
UNSUPPORTED("au4kpvl0egq3dt8hlmqnad61o"); // 	pinfo.margin = Pack;
UNSUPPORTED("6o6lvrnsp1zkqdkduwrfetrrm"); // 	pinfo.fixed = 0;
UNSUPPORTED("7ijhbsssflu2oty986iwhi5u7"); //           /* components using clusters */
UNSUPPORTED("axesx20t6oyprzdzjz5cwneq4"); // 	ccs = cccomps(g, &ncc, 0);
UNSUPPORTED("edukq9g8egufczs6ja3h99k6a"); // 	if (ncc == 1) {
UNSUPPORTED("aj3wrqm1ouyaocsn56geghko0"); // 	    dotLayout(g);
UNSUPPORTED("2pkescmk4qojc8cqt3wzo8jkp"); // 	} else if ((((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind == R_NONE) {
UNSUPPORTED("295yqzsqueggcx71mcuv1zy7t"); // 	    pinfo.doSplines = 1;
UNSUPPORTED("3rfcnwowz3h58n92j7igzwap0"); // 	    for (i = 0; i < ncc; i++) {
UNSUPPORTED("6n1wgj9rnpi5u0c1f6mo7hvdr"); // 		sg = ccs[i];
UNSUPPORTED("9f5l1z5fur3bn6wlzykztsrr5"); // 		initSubg (sg, g);
UNSUPPORTED("boery4d5nv4jssiy0y3mf2yf7"); // 		dotLayout (sg);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ahr97rgrh96az7iroebb6p14f"); // 	    attachPos (g);
UNSUPPORTED("1eeqkm1855z53jwep9ukxptxa"); // 	    packSubgraphs(ncc, ccs, g, &pinfo);
UNSUPPORTED("5jevnxwr3ejh3o6i1n3b15oyz"); // 	    resetCoord (g);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("3dnz4esjw60s265perisqld8n"); // 	    /* Not sure what semantics should be for non-trivial ratio
UNSUPPORTED("9752lfn24j9vhkz6y8fvyvpop"); //              * attribute with multiple components.
UNSUPPORTED("cxv608avcmjij1ltc81ac9zp0"); //              * One possibility is to layout nodes, pack, then apply the ratio
UNSUPPORTED("71ykqx29qwldoh9wf4e85xh7u"); //              * adjustment. We would then have to re-adjust all positions.
UNSUPPORTED("29edlge3wqyp1gs0vymtr5700"); //              */
UNSUPPORTED("aj3wrqm1ouyaocsn56geghko0"); // 	    dotLayout(g);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cjjlbx1oxw3twss3vlotij507"); // 	for (i = 0; i < ncc; i++) {
UNSUPPORTED("dc0uduk2687c8n6qn7y3y0aph"); // 	    free ((((Agraphinfo_t*)(((Agobj_t*)(ccs[i]))->data))->drawing));
UNSUPPORTED("6lefivq8plsiwa47ucvy1ze1a"); // 	    agdelete(g, ccs[i]);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5eru5cerydhchm7ojmgdlkv9y"); // 	free(ccs);
    }
} finally {
LEAVING("nedairhdof6qkmjjoh4h68zy","doDot");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_layout", key="euvc3uoksq3e24augkwebfkcv", definition="void dot_layout(Agraph_t * g)")
public static void dot_layout(ST_Agraph_s g) {
ENTERING("euvc3uoksq3e24augkwebfkcv","dot_layout");
try {
    if (agnnodes(g)!=0) doDot (g);
    dotneato_postprocess(g);
} finally {
LEAVING("euvc3uoksq3e24augkwebfkcv","dot_layout");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/dotinit.c", name="dot_root", key="ca52dadcp7m8x0bqhaw4tvtaw", definition="Agraph_t * dot_root (void* p)")
public static ST_Agraph_s dot_root(__ptr__ p) {
ENTERING("ca52dadcp7m8x0bqhaw4tvtaw","dot_root");
try {
    return GD_dotroot(agroot(p));
} finally {
LEAVING("ca52dadcp7m8x0bqhaw4tvtaw","dot_root");
}
}


}
