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
import static gen.lib.cgraph.graph__c.agnedges;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agcontains;
import static gen.lib.common.utils__c.dequeue;
import static gen.lib.common.utils__c.enqueue;
import static gen.lib.common.utils__c.free_queue;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.new_queue;
import static gen.lib.dotgen.class2__c.class2;
import static gen.lib.dotgen.cluster__c.expand_cluster;
import static gen.lib.dotgen.cluster__c.install_cluster;
import static gen.lib.dotgen.cluster__c.mark_lowclusters;
import static gen.lib.dotgen.decomp__c.decompose;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static gen.lib.dotgen.fastgr__c.delete_flat_edge;
import static gen.lib.dotgen.fastgr__c.flat_edge;
import static gen.lib.dotgen.fastgr__c.merge_oneway;
import static gen.lib.dotgen.fastgr__c.new_virtual_edge;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.atof;
import static smetana.core.JUtils.function;
import static smetana.core.JUtils.qsort1;
import static smetana.core.JUtils.qsort2;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ALLOC_INT;
import static smetana.core.Macro.CLUSTER;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.FLATORDER;
import static smetana.core.Macro.GD_clust;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_flat_edges;
import static smetana.core.Macro.GD_maxrank;
import static smetana.core.Macro.GD_minrank;
import static smetana.core.Macro.GD_n_cluster;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rank;
import static smetana.core.Macro.GD_rankleader;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_has_port;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_low;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_mval;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_onstack;
import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_prev;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_weight_class;
import static smetana.core.Macro.NEW_RANK;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.VIRTUAL;
import static smetana.core.Macro.elist_append;
import static smetana.core.Macro.flatindex;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_adjmatrix_t;
import h.ST_elist;
import h.ST_nodequeue;
import h.ST_rank_t;
import smetana.core.CStar;
import smetana.core.CStarStar;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

/* 
 * dot_mincross(g) takes a ranked graphs, and finds an ordering
 * that avoids edge crossings.  clusters are expanded.
 * N.B. the rank structure is global (not allocated per cluster)
 * because mincross may compare nodes in different clusters.
 */
public class mincross__c {

//3 ciez0pfggxdljedzsbklq49f0
// static inline point pointof(int x, int y) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="pointof", key="ciez0pfggxdljedzsbklq49f0", definition="static inline point pointof(int x, int y)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="boxof", key="7cufnfitrh935ew093mw0i4b7", definition="static inline box boxof(int llx, int lly, int urx, int ury)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="add_point", key="1n5xl70wxuabyf97mclvilsm6", definition="static inline point add_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="sub_point", key="ai2dprak5y6obdsflguh5qbd7", definition="static inline point sub_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="sub_pointf", key="16f6pyogcv3j7n2p0n8giqqgh", definition="static inline pointf sub_pointf(pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mid_point", key="9k50jgrhc4f9824vf8ony74rw", definition="static inline point mid_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mid_pointf", key="59c4f7im0ftyowhnzzq2v9o1x", definition="static inline pointf mid_pointf(pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="interpolate_pointf", key="5r18p38gisvcx3zsvbb9saixx", definition="static inline pointf interpolate_pointf(double t, pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="exch_xy", key="bxzrv2ghq04qk5cbyy68s4mol", definition="static inline point exch_xy(point p)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="exch_xyf", key="9lt3e03tac6h6sydljrcws8fd", definition="static inline pointf exch_xyf(pointf p)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="box_bb", key="8l9qhieokthntzdorlu5zn29b", definition="static inline box box_bb(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="boxf_bb", key="clws9h3bbjm0lw3hexf8nl4c4", definition="static inline boxf boxf_bb(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="box_intersect", key="bit6ycxo1iqd2al92y8gkzlvb", definition="static inline box box_intersect(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="boxf_intersect", key="8gfybie7k6pgb3o1a6llgpwng", definition="static inline boxf boxf_intersect(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="box_overlap", key="7z8j2quq65govaaejrz7b4cvb", definition="static inline int box_overlap(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="boxf_overlap", key="4z0suuut2acsay5m8mg9dqjdu", definition="static inline int boxf_overlap(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="box_contains", key="dd34swz5rmdgu3a2np2a4h1dy", definition="static inline int box_contains(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="boxf_contains", key="8laj1bspbu2i1cjd9upr7xt32", definition="static inline int boxf_contains(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="perp", key="4wf5swkz24xx51ja2dynbycu1", definition="static inline pointf perp (pointf p)")
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
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="scale", key="6dtlpzv4mvgzb9o0b252yweuv", definition="static inline pointf scale (double c, pointf p)")
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




//3 7ye0tsa8kll1jntdcqzsgjp0q
// static int gd_minrank(Agraph_t *g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="gd_minrank", key="7ye0tsa8kll1jntdcqzsgjp0q", definition="static int gd_minrank(Agraph_t *g)")
public static Object gd_minrank(Object... arg) {
UNSUPPORTED("7wnr5wvv8mw2d3mfdyvdz5kz9"); // static int gd_minrank(Agraph_t *g) {return GD_minrank(g);}

throw new UnsupportedOperationException();
}




//3 c7dhattawtph3qv1eeoafltk0
// static int gd_maxrank(Agraph_t *g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="gd_maxrank", key="c7dhattawtph3qv1eeoafltk0", definition="static int gd_maxrank(Agraph_t *g)")
public static Object gd_maxrank(Object... arg) {
UNSUPPORTED("b89n5iyqsgra9z559023bf23e"); // static int gd_maxrank(Agraph_t *g) {return GD_maxrank(g);}

throw new UnsupportedOperationException();
}




//3 8g2can1ih1668s30s6d7wny5b
// static rank_t *gd_rank(Agraph_t *g, int r) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="8g2can1ih1668s30s6d7wny5b", definition="static rank_t *gd_rank(Agraph_t *g, int r)")
public static Object gd_rank(Object... arg) {
UNSUPPORTED("bxd4rm18v4y2x6fo30vh9pauo"); // static rank_t *gd_rank(Agraph_t *g, int r) {return &GD_rank(g)[r];}

throw new UnsupportedOperationException();
}




//3 8pjrnrvidhpqcubvkk5lsfp6
// static int nd_order(Agnode_t *v) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="nd_order", key="8pjrnrvidhpqcubvkk5lsfp6", definition="static int nd_order(Agnode_t *v)")
public static Object nd_order(Object... arg) {
UNSUPPORTED("f4zvlmxyp6gei8iiu33hpz4oe"); // static int nd_order(Agnode_t *v) { return ND_order(v); }

throw new UnsupportedOperationException();
}


/* dot_mincross:
 * Minimize edge crossings
 * Note that nodes are not placed into GD_rank(g) until mincross()
 * is called.
 */
@Unused
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="dot_mincross", key="e876vp4hgkzshluz6qk77cjwk", definition="void dot_mincross(graph_t * g, int doBalance)")
public static void dot_mincross(ST_Agraph_s g, boolean doBalance) {
ENTERING("e876vp4hgkzshluz6qk77cjwk","dot_mincross");
try {
    int c, nc;
    CString s;
    
    init_mincross(g);
    
    for (nc = c = 0; c < GD_comp(g).size; c++) {
	init_mccomp(g, c);
	nc += mincross_(g, 0, 2, doBalance);
    }
    
    merge2(g);
    
    /* run mincross on contents of each cluster */
    for (c = 1; c <= GD_n_cluster(g); c++) {
	nc += mincross_clust(g, GD_clust(g).get_(c), doBalance);
    }
    
    
    
    if ((GD_n_cluster(g) > 0)
	&& (N(s = agget(g, new CString("remincross"))) || (mapbool(s)))) {
	mark_lowclusters(g);
	Z.z().ReMincross = true;
	nc = mincross_(g, 2, 2, doBalance);
    }
    cleanup2(g, nc);
} finally {
LEAVING("e876vp4hgkzshluz6qk77cjwk","dot_mincross");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="756bre1tpxb1tq68p7xhkrxkc", definition="static adjmatrix_t *new_matrix(int i, int j)")
public static ST_adjmatrix_t new_matrix(int i, int j) {
ENTERING("756bre1tpxb1tq68p7xhkrxkc","new_matrix");
try {
	ST_adjmatrix_t rv = new ST_adjmatrix_t();
    rv.nrows = i;
    rv.ncols = j;
    rv.data = new int[i][j]; // Or maybe new int[j][i] ?
    return rv;
} finally {
LEAVING("756bre1tpxb1tq68p7xhkrxkc","new_matrix");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="free_matrix", key="1n1e0k0wtlbugwm3cb4na6av6", definition="static void free_matrix(adjmatrix_t * p)")
public static void free_matrix(ST_adjmatrix_t p) {
ENTERING("1n1e0k0wtlbugwm3cb4na6av6","free_matrix");
try {
    if (p!=null) {
	Memory.free(p.data);
	Memory.free(p);
    }
} finally {
LEAVING("1n1e0k0wtlbugwm3cb4na6av6","free_matrix");
}
}




@Difficult
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="init_mccomp", key="49vw7fkn99wbojtfksugvuruh", definition="static void init_mccomp(graph_t * g, int c)")
public static void init_mccomp(ST_Agraph_s g, int c) {
ENTERING("49vw7fkn99wbojtfksugvuruh","init_mccomp");
try {
    int r;
    
    GD_nlist(g, GD_comp(g).list.get_(c));
    if (c > 0) {
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    GD_rank(g).get__(r).v = GD_rank(g).get__(r).v.plus_(GD_rank(g).get__(r).n);
	    GD_rank(g).get__(r).n = 0;
	}
    }
} finally {
LEAVING("49vw7fkn99wbojtfksugvuruh","init_mccomp");
}
}




//3 72v5zs502m1of3vsofyfo15ap
// static int betweenclust(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="betweenclust", key="72v5zs502m1of3vsofyfo15ap", definition="static int betweenclust(edge_t * e)")
public static Object betweenclust(Object... arg) {
UNSUPPORTED("6k6r73j7gctt4ntepjubrlfze"); // static int betweenclust(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1z9f8ptfggp4qlwv5cdd55i3j"); //     while (ED_to_orig(e))
UNSUPPORTED("bdmai1d040qmubf08ds339v9x"); // 	e = ED_to_orig(e);
UNSUPPORTED("eq2o4v9qo3xx42in97ssadek"); //     return (ND_clust(agtail(e)) != ND_clust(aghead(e)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 760bwoz4twwy2jr1b32r6zefi
// static void do_ordering_node (graph_t * g, node_t* n, int outflag) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="do_ordering_node", key="760bwoz4twwy2jr1b32r6zefi", definition="static void do_ordering_node (graph_t * g, node_t* n, int outflag)")
public static Object do_ordering_node(Object... arg) {
UNSUPPORTED("ajn55zvrzf8njqkshll3kaz88"); // static void do_ordering_node (graph_t * g, node_t* n, int outflag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bp5tdulayknsvyv8a2j2tinh"); //     int i, ne;
UNSUPPORTED("5yrhx4blosxo5xnc1nh1kzhfs"); //     node_t *u, *v;
UNSUPPORTED("9cbk3ly04nbzqoxmmjwptqq9a"); //     edge_t *e, *f, *fe;
UNSUPPORTED("5ol5beitdgh7axwpk35pc9trm"); //     edge_t **sortlist = TE_list;
UNSUPPORTED("aeria2or5qpnnrr9wm15k588l"); //     if (ND_clust(n))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("442hvc4h32o6e3cvzm5y5y3oq"); //     if (outflag) {
UNSUPPORTED("7xi4jdz18dyv740agfh5uwtxv"); // 	for (i = ne = 0; (e = ND_out(n).list[i]); i++)
UNSUPPORTED("24smn55xhvkklh2do2cfcmy98"); // 	    if (!betweenclust(e))
UNSUPPORTED("ouas2w3lk54dwigzgglm3w7r"); // 		sortlist[ne++] = e;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6dgl0skwzmsi69ugcewc5vwaf"); // 	for (i = ne = 0; (e = ND_in(n).list[i]); i++)
UNSUPPORTED("24smn55xhvkklh2do2cfcmy98"); // 	    if (!betweenclust(e))
UNSUPPORTED("ouas2w3lk54dwigzgglm3w7r"); // 		sortlist[ne++] = e;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("hdnqvw2crdbzt2uenqso4l7k"); //     if (ne <= 1)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("4zkjvsomlyhcl06ummf4gz0d2"); //     /* write null terminator at end of list.
UNSUPPORTED("2zx16rdu8hp86z3z36natgy8t"); //        requires +1 in TE_list alloccation */
UNSUPPORTED("6dp57feioscwstrfmhqhrz1jx"); //     sortlist[ne] = 0;
UNSUPPORTED("4wqnlh6z9lr2xvmjp95nozn84"); //     qsort(sortlist, ne, sizeof(sortlist[0]), (qsort_cmpf) edgeidcmpf);
UNSUPPORTED("d6aia9q03brt8otnyu5m3tlxj"); //     for (ne = 1; (f = sortlist[ne]); ne++) {
UNSUPPORTED("1b9ylnvr8zscovbyrxoe9ypax"); // 	e = sortlist[ne - 1];
UNSUPPORTED("4nbrepm0pmohxxml0csb9j5xc"); // 	if (outflag) {
UNSUPPORTED("2rcn453iomwvou108kjr2rboc"); // 	    u = aghead(e);
UNSUPPORTED("96i3kysq5lfoyukhe65vpiah2"); // 	    v = aghead(f);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("96jttvtco2oqt3y3qr5fbhu1j"); // 	    u = agtail(e);
UNSUPPORTED("8g5mzwux62x43bjzeslylyi0g"); // 	    v = agtail(f);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("a7mygw39shaa9w4l8fdlgqr0i"); // 	if (find_flat_edge(u, v))
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("aoa2embk8eaij29q6nbl4e5tk"); // 	fe = new_virtual_edge(u, v, NULL);
UNSUPPORTED("67t5g8eu9tdqbnwj4s4g6ah6u"); // 	ED_edge_type(fe) = 4;
UNSUPPORTED("b32n066t1nkgj646wvo7vuwlq"); // 	flat_edge(g, fe);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 815nmj2gcomtex4yfclqde12j
// static void do_ordering(graph_t * g, int outflag) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="do_ordering", key="815nmj2gcomtex4yfclqde12j", definition="static void do_ordering(graph_t * g, int outflag)")
public static Object do_ordering(Object... arg) {
UNSUPPORTED("9v68euuxlii02rb5mhlyd8alp"); // static void do_ordering(graph_t * g, int outflag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5y5mm5obhrqt745c4fx4o0r56"); //     /* Order all nodes in graph */
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("bicm0b87pp2rusaezmk6qigjq"); // 	do_ordering_node (g, n, outflag);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 svq3i3c6yw3cbfwaeip5dfuy
// static void do_ordering_for_nodes(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="do_ordering_for_nodes", key="svq3i3c6yw3cbfwaeip5dfuy", definition="static void do_ordering_for_nodes(graph_t * g)")
public static Object do_ordering_for_nodes(Object... arg) {
UNSUPPORTED("7yd9vingwgdcdtrx0p38jti00"); // static void do_ordering_for_nodes(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("887aairlqk73reuayo3hz67v3"); //     /* Order nodes which have the "ordered" attribute */
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("cbbfhy5qk3dailijtedj1czut"); //     const char *ordering;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("ekvbu44bbrdwkw71yd21dbns9"); // 	if ((ordering = late_string(n, N_ordering, NULL))) {
UNSUPPORTED("7jueg5punlemj6mfbpgltb5u8"); // 	    if ((*(ordering)==*("out")&&!strcmp(ordering,"out")))
UNSUPPORTED("302oz5j9wva90otn6jq513bme"); // 		do_ordering_node(g, n, NOT(0));
UNSUPPORTED("1dt1hft3eak5ytycs6dh7yk0"); // 	    else if ((*(ordering)==*("in")&&!strcmp(ordering,"in")))
UNSUPPORTED("dnig45so0x904azp9fhxymqjn"); // 		do_ordering_node(g, n, 0);
UNSUPPORTED("2rxgmu8cktngvqtsex2rlbjed"); // 	    else if (ordering[0])
UNSUPPORTED("axk3fxl1bh09n475kmcx42kdu"); // 		agerr(AGERR, "ordering '%s' not recognized for node '%s'.\n", ordering, agnameof(n));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




/* ordered_edges:
 * handle case where graph specifies edge ordering
 * If the graph does not have an ordering attribute, we then
 * check for nodes having the attribute.
 * Note that, in this implementation, the value of G_ordering
 * dominates the value of N_ordering.
 */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="ordered_edges", key="hzoz2czb672i0nbjvjhbc3na", definition="static void ordered_edges(graph_t * g)")
public static void ordered_edges(ST_Agraph_s g) {
ENTERING("hzoz2czb672i0nbjvjhbc3na","ordered_edges");
try {
    CString ordering;
    if (N(Z.z().G_ordering) && N(Z.z().N_ordering))
	return;
UNSUPPORTED("98iqppixxkdndoz210i5ejppy"); //     if ((ordering = late_string(g, G_ordering, NULL))) {
UNSUPPORTED("lhhw62wj3on36enoy6ug6u1p"); // 	if ((*(ordering)==*("out")&&!strcmp(ordering,"out")))
UNSUPPORTED("ctbadiyd3jjtlt2f5t9p94au7"); // 	    do_ordering(g, NOT(0));
UNSUPPORTED("259y9uhqwc8w6jhrpm1eaa7hf"); // 	else if ((*(ordering)==*("in")&&!strcmp(ordering,"in")))
UNSUPPORTED("312ij4z5d42rye9s30l9io097"); // 	    do_ordering(g, 0);
UNSUPPORTED("25yw5chxmy54pmo88tre1w2ir"); // 	else if (ordering[0])
UNSUPPORTED("5ugg9zy8378pijqru97mxmen5"); // 	    agerr(AGERR, "ordering '%s' not recognized.\n", ordering);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("50itbchatjp61haxvg3xnhvoo"); // 	graph_t *subg;
UNSUPPORTED("evzjoqkm2xukapjvpccu2f23e"); // 	for (subg = agfstsubg(g); subg; subg = agnxtsubg(subg)) {
UNSUPPORTED("1016hqxaexp4j33vmkzvdpq80"); // 	    /* clusters are processed by separate calls to ordered_edges */
UNSUPPORTED("7ah0zlu96u6g6cquxebenj4z3"); // 	    if (!is_cluster(subg))
UNSUPPORTED("7sqzaehh40dvpm11hlxhef6cw"); // 		ordered_edges(subg);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("h2b3ao18r091ie7yk9v20pqq"); // 	if (N_ordering) do_ordering_for_nodes (g);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("hzoz2czb672i0nbjvjhbc3na","ordered_edges");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross_clust", key="crwc5qe7fmrpgcqh1a80toyvo", definition="static int mincross_clust(graph_t * par, graph_t * g, int doBalance)")
public static int mincross_clust(ST_Agraph_s par, ST_Agraph_s g, boolean doBalance) {
ENTERING("crwc5qe7fmrpgcqh1a80toyvo","mincross_clust");
try {
    int c, nc;
    
    expand_cluster(g);
    ordered_edges(g);
    flat_breakcycles(g);
    flat_reorder(g);
    nc = mincross_(g, 2, 2, doBalance);
    
    for (c = 1; c <= GD_n_cluster(g); c++)
	nc += mincross_clust(g, GD_clust(g).get_(c), doBalance);
    
    save_vlist(g);
    return nc;
} finally {
LEAVING("crwc5qe7fmrpgcqh1a80toyvo","mincross_clust");
}
}




//3 657v773m21j5w3g3v94o7464t
// static int left2right(graph_t * g, node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="left2right", key="657v773m21j5w3g3v94o7464t", definition="static int left2right(graph_t * g, node_t * v, node_t * w)")
public static boolean left2right(ST_Agraph_s g, ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("657v773m21j5w3g3v94o7464t","left2right");
try {
	ST_adjmatrix_t M;
    boolean rv=false;
    /* CLUSTER indicates orig nodes of clusters, and vnodes of skeletons */
    if (Z.z().ReMincross == false) {
	if (NEQ(ND_clust(v), ND_clust(w)) && (ND_clust(v)!=null) && (ND_clust(w)!=null)) {
	    /* the following allows cluster skeletons to be swapped */
	    if ((ND_ranktype(v) == 7)
		&& (ND_node_type(v) == 1))
		return false;
	    if ((ND_ranktype(w) == 7)
		&& (ND_node_type(w) == 1))
		return false;
	    return true;
	    /*return ((ND_ranktype(v) != CLUSTER) && (ND_ranktype(w) != CLUSTER)); */
	}
    } else {
 	if (NEQ(ND_clust(v), ND_clust(w)))
 	    return NOT(0);
    }
    M = GD_rank(g).get__(ND_rank(v)).flat;
    if (M == null)
	rv = false;
    else {
	if (GD_flip(g)) {
	    ST_Agnode_s t = v;
	    v = w;
	    w = t;
	}
	rv = M.data[ND_low(v)][ND_low(w)]!=0;
    }
    return rv;
} finally {
LEAVING("657v773m21j5w3g3v94o7464t","left2right");
}
}




//3 daknncpjy7g5peiicolbmh55i
// static int in_cross(node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="in_cross", key="daknncpjy7g5peiicolbmh55i", definition="static int in_cross(node_t * v, node_t * w)")
public static int in_cross(ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("daknncpjy7g5peiicolbmh55i","in_cross");
try {
    CStarStar<ST_Agedge_s> e2_ = ND_in(w).list;
    int inv, cross = 0, t;
    for (int ie2 = 0; e2_.get_(ie2)!=null; ie2++) {
	int cnt = ED_xpenalty(e2_.get_(ie2));		
	inv = ND_order((agtail(e2_.get_(ie2))));
    CStarStar<ST_Agedge_s> e1_ = ND_in(v).list;
	for (int ie1 = 0; e1_.get_(ie1)!=null; ie1++) {
	    t = ND_order(agtail(e1_.get_(ie1))) - inv;
	    if ((t > 0)
		|| ((t == 0)
		    && (  ED_tail_port(e1_.get_(ie1)).p.x > ED_tail_port(e2_.get_(ie2)).p.x)))
		cross += ED_xpenalty(e1_.get_(ie1)) * cnt;
	}
    }
    return cross;
} finally {
LEAVING("daknncpjy7g5peiicolbmh55i","in_cross");
}
}




//3 b7mf74np8ewrgzwd5u0o8fqod
// static int out_cross(node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="out_cross", key="b7mf74np8ewrgzwd5u0o8fqod", definition="static int out_cross(node_t * v, node_t * w)")
public static int out_cross(ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("b7mf74np8ewrgzwd5u0o8fqod","out_cross");
try {
 	CStarStar<ST_Agedge_s> e2_ = ND_out(w).list;
    int inv, cross = 0, t;
    for (int ie2 = 0; e2_.get_(ie2)!=null; ie2++) {
	int cnt = ED_xpenalty(e2_.get_(ie2));
	inv = ND_order(aghead(e2_.get_(ie2)));
    CStarStar<ST_Agedge_s> e1_ = ND_out(v).list;
	for (int ie1 = 0; e1_.get_(ie1)!=null; ie1++) {
	    t = ND_order(aghead(e1_.get_(ie1))) - inv;
	    if ((t > 0)
		|| ((t == 0)
		    && ((ED_head_port(e1_.get_(ie1))).p.x) > (ED_head_port(e2_.get_(ie2))).p.x))
		cross += ((ED_xpenalty(e1_.get_(ie1))) * cnt);
	}
    }
    return cross;
} finally {
LEAVING("b7mf74np8ewrgzwd5u0o8fqod","out_cross");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="exchange", key="ba4tbr57wips1dzpgxzx3b6ja", definition="static void exchange(node_t * v, node_t * w)")
public static void exchange(ST_Agnode_s v, ST_Agnode_s w) {
ENTERING("ba4tbr57wips1dzpgxzx3b6ja","exchange");
try {
    int vi, wi, r;
    
    r = ND_rank(v);
    vi = ND_order(v);
    wi = ND_order(w);
    ND_order(v, wi);
    GD_rank(Z.z().Root).get__(r).v.set_(wi, v);
    ND_order(w, vi);
    GD_rank(Z.z().Root).get__(r).v.set_(vi, w);
} finally {
LEAVING("ba4tbr57wips1dzpgxzx3b6ja","exchange");
}
}




//3 dzkv88lq5zv0g3yo7bf90qgr7
// static void balanceNodes(graph_t * g, int r, node_t * v, node_t * w) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="balanceNodes", key="dzkv88lq5zv0g3yo7bf90qgr7", definition="static void balanceNodes(graph_t * g, int r, node_t * v, node_t * w)")
public static Object balanceNodes(Object... arg) {
UNSUPPORTED("504qsj6detmzllj50pno6sdx4"); // static void balanceNodes(graph_t * g, int r, node_t * v, node_t * w)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dor7xumwnqvymg7xdesa6pxqn"); //     node_t *s;			/* separator node */
UNSUPPORTED("c6s55weiu8hc4voeunxf2utru"); //     int sepIndex;
UNSUPPORTED("6gqdkaiir2naiawry8cnlpf4a"); //     int nullType;		/* type of null nodes */
UNSUPPORTED("2dqxr1ujg381s9ugf0367iici"); //     int cntDummy = 0, cntOri = 0;
UNSUPPORTED("7yj94w2zpwdubyzymyljw490a"); //     int k = 0, m = 0, k1 = 0, m1 = 0, i = 0;
UNSUPPORTED("6mq7c2z232o8kvpp371lfmewh"); //     /* we only consider v and w of different types */
UNSUPPORTED("53kjkk6c0h35zpbikj9y2q6b4"); //     if (ND_node_type(v) == ND_node_type(w))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("5ylcsv8rwps97qz7sdftpwrka"); //     /* count the number of dummy and original nodes */
UNSUPPORTED("5025o4uc5dkbxf3smi3kmfoit"); //     for (i = 0; i < GD_rank(g)[r].n; i++) {
UNSUPPORTED("d87qjwxc2qjvh6hb9fmi9y9vq"); // 	if (ND_node_type(GD_rank(g)[r].v[i]) == 0)
UNSUPPORTED("dcny4syjny1sh20jyjjl7jj62"); // 	    cntOri++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("bbpsgrsgquajs3xxqzeqvgv6n"); // 	    cntDummy++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2qx27p3loyln3i09ycwf5p1dh"); //     if (cntOri < cntDummy) {
UNSUPPORTED("ex9drrxqg8zalagzrnvrulrlb"); // 	if (ND_node_type(v) == 0)
UNSUPPORTED("jtm789r1sqidxc39d09n9det"); // 	    s = v;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("c31t0vnbzvj9zml7p91syjzep"); // 	    s = w;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("ex9drrxqg8zalagzrnvrulrlb"); // 	if (ND_node_type(v) == 0)
UNSUPPORTED("c31t0vnbzvj9zml7p91syjzep"); // 	    s = w;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("jtm789r1sqidxc39d09n9det"); // 	    s = v;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ejbeaepbxwawm7mvtusyi2x57"); //     /* get the separator node index */
UNSUPPORTED("5025o4uc5dkbxf3smi3kmfoit"); //     for (i = 0; i < GD_rank(g)[r].n; i++) {
UNSUPPORTED("afetrkwabtn6v07evr5japsk5"); // 	if (GD_rank(g)[r].v[i] == s)
UNSUPPORTED("bvwqg08b9hn6rfdx11sjq0hgc"); // 	    sepIndex = i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ecis6irl9vire6ejfrshttjk4"); //     nullType = (ND_node_type(s) == 0) ? 1 : 0;
UNSUPPORTED("1i78b3w9omzkba0pmafwfunbj"); //     /* count the number of null nodes to the left and 
UNSUPPORTED("1f2jn45p7wv1ud964k43lgdss"); //      * right of the separator node 
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("5l8w7t0ta6p6hnfp72l7lbqey"); //     for (i = sepIndex - 1; i >= 0; i--) {
UNSUPPORTED("49j09cv8f8ien7kqoguzywjmw"); // 	if (ND_node_type(GD_rank(g)[r].v[i]) == nullType)
UNSUPPORTED("borg3y7rgdooetc6ckrsrg9fc"); // 	    k++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cuj70hyout6yv30drqkjiwllo"); //     for (i = sepIndex + 1; i < GD_rank(g)[r].n; i++) {
UNSUPPORTED("49j09cv8f8ien7kqoguzywjmw"); // 	if (ND_node_type(GD_rank(g)[r].v[i]) == nullType)
UNSUPPORTED("d35zlcj7podjjns9eg2ttocr7"); // 	    m++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9r0b42aqwdlx2cg7aqsxgle1o"); //     /* now exchange v,w and calculate the same counts */
UNSUPPORTED("cvktng9nwm6futhlv28bqbqy"); //     exchange(v, w);
UNSUPPORTED("ejbeaepbxwawm7mvtusyi2x57"); //     /* get the separator node index */
UNSUPPORTED("5025o4uc5dkbxf3smi3kmfoit"); //     for (i = 0; i < GD_rank(g)[r].n; i++) {
UNSUPPORTED("afetrkwabtn6v07evr5japsk5"); // 	if (GD_rank(g)[r].v[i] == s)
UNSUPPORTED("bvwqg08b9hn6rfdx11sjq0hgc"); // 	    sepIndex = i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1i78b3w9omzkba0pmafwfunbj"); //     /* count the number of null nodes to the left and 
UNSUPPORTED("1f2jn45p7wv1ud964k43lgdss"); //      * right of the separator node 
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("5l8w7t0ta6p6hnfp72l7lbqey"); //     for (i = sepIndex - 1; i >= 0; i--) {
UNSUPPORTED("49j09cv8f8ien7kqoguzywjmw"); // 	if (ND_node_type(GD_rank(g)[r].v[i]) == nullType)
UNSUPPORTED("eer25fyjj3njh33v709xj07lk"); // 	    k1++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cuj70hyout6yv30drqkjiwllo"); //     for (i = sepIndex + 1; i < GD_rank(g)[r].n; i++) {
UNSUPPORTED("49j09cv8f8ien7kqoguzywjmw"); // 	if (ND_node_type(GD_rank(g)[r].v[i]) == nullType)
UNSUPPORTED("80ptwhmp1gyks2xfkcjub24o9"); // 	    m1++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6yjcl5tugkib89c3p3beoi0cs"); //     if (abs(k1 - m1) > abs(k - m)) {
UNSUPPORTED("4hhzcihijbcjb2mo6eop9g855"); // 	exchange(v, w);		//revert to the original ordering
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 72rj5xs4qh00oh2yi1h5qaadu
// static int balance(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="balance", key="72rj5xs4qh00oh2yi1h5qaadu", definition="static int balance(graph_t * g)")
public static Object balance(Object... arg) {
UNSUPPORTED("4223t6rekw3qd8eksvz9kjcqh"); // static int balance(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("970giwpms1ljmt0px5djlqs0z"); //     int i, c0, c1, rv;
UNSUPPORTED("8xqnhjfdqnlwlyld1ep6hpnmb"); //     node_t *v, *w;
UNSUPPORTED("p6jnh7nvcpnl3zbz636pskbs"); //     int r;
UNSUPPORTED("yy0fwjrmxw3jbi85mvwrka7f"); //     rv = 0;
UNSUPPORTED("9ey19t6tw2srzaedeng4sq96z"); //     for (r = GD_maxrank(g); r >= GD_minrank(g); r--) {
UNSUPPORTED("2nlemgjbnd1ygry42bcbuwo9y"); // 	GD_rank(g)[r].candidate = 0;
UNSUPPORTED("91gzxbmcoy5xbvf8nlhmlj4eh"); // 	for (i = 0; i < GD_rank(g)[r].n - 1; i++) {
UNSUPPORTED("5ekdr3i8niy7x2ohi7ftgmjke"); // 	    v = GD_rank(g)[r].v[i];
UNSUPPORTED("efwyjm6j3e7byt6z83lrhgd1r"); // 	    w = GD_rank(g)[r].v[i + 1];
UNSUPPORTED("8ey6umhtefcrb61ncgis82hw6"); // 	    assert(ND_order(v) < ND_order(w));
UNSUPPORTED("ddkyq74d5rjfcofczt4hmkxsj"); // 	    if (left2right(g, v, w))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("cdpfb5fmlhe0831jf96pjp9s0"); // 	    c0 = c1 = 0;
UNSUPPORTED("3l676z6ajeugxvie4r957jap3"); // 	    if (r > 0) {
UNSUPPORTED("ennnd5cl9xhh55j2bni58iuxv"); // 		c0 += in_cross(v, w);
UNSUPPORTED("bk4dl2r7nw7j8xvkpg7g7x0z9"); // 		c1 += in_cross(w, v);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("f0ordylgf44hx2l90eof067yo"); // 	    if (GD_rank(g)[r + 1].n > 0) {
UNSUPPORTED("d945airzpo5xbu0fz68ylmgql"); // 		c0 += out_cross(v, w);
UNSUPPORTED("e6ez0uwca50uwo7i7i4vak4f2"); // 		c1 += out_cross(w, v);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("884yfwkgyw57w5ivpfrggmq15"); // 	    if (c1 <= c0) {
UNSUPPORTED("c2sevxb1gtyc2olq8akmvs40u"); // 		balanceNodes(g, r, v, w);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bxwzx4m9ejausu58u7abr6fm0
// static int transpose_step(graph_t * g, int r, int reverse) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="transpose_step", key="bxwzx4m9ejausu58u7abr6fm0", definition="static int transpose_step(graph_t * g, int r, int reverse)")
public static int transpose_step(ST_Agraph_s g, int r, boolean reverse) {
ENTERING("bxwzx4m9ejausu58u7abr6fm0","transpose_step");
try {
    int i, c0, c1, rv;
    ST_Agnode_s v, w;
    rv = 0;
    GD_rank(g).get__(r).candidate= false;
    for (i = 0; i < GD_rank(g).get__(r).n - 1; i++) {
	v = GD_rank(g).get__(r).v.get_(i);
	w = GD_rank(g).get__(r).v.get_(i + 1);
	//assert(ND_order(v) < ND_order(w));
	if (left2right(g, v, w))
	    continue;
	c0 = c1 = 0;
	if (r > 0) {
	    c0 += in_cross(v, w);
	    c1 += in_cross(w, v);
	}
	if (GD_rank(g).get__(r + 1).n > 0) {
	    c0 += out_cross(v, w);
	    c1 += out_cross(w, v);
	}
	if ((c1 < c0) || ((c0 > 0) && reverse && (c1 == c0))) {
	    exchange(v, w);
	    rv += (c0 - c1);
	    GD_rank(Z.z().Root).get__(r).valid= 0;
	    GD_rank(g).get__(r).candidate= true;
	    if (r > GD_minrank(g)) {
		GD_rank(Z.z().Root).get__(r - 1).valid= 0;
		GD_rank(g).get__(r - 1).candidate= true;
	    }
	    if (r < GD_maxrank(g)) {
		GD_rank(Z.z().Root).get__(r + 1).valid= 0;
		GD_rank(g).get__(r + 1).candidate= true;
	    }
	}
    }
    return rv;
} finally {
LEAVING("bxwzx4m9ejausu58u7abr6fm0","transpose_step");
}
}




//3 2i22bxgg5y7v5c5d40k5zppky
// static void transpose(graph_t * g, int reverse) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="transpose", key="2i22bxgg5y7v5c5d40k5zppky", definition="static void transpose(graph_t * g, int reverse)")
public static void transpose(ST_Agraph_s g, boolean reverse) {
ENTERING("2i22bxgg5y7v5c5d40k5zppky","transpose");
try {
    int r, delta;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++)
	GD_rank(g).get__(r).candidate= true;
    do {
	delta = 0;
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    if (GD_rank(g).get__(r).candidate) {
		delta += transpose_step(g, r, reverse);
	    }
	}
	/*} while (delta > ncross(g)*(1.0 - Convergence)); */
    } while (delta >= 1);
} finally {
LEAVING("2i22bxgg5y7v5c5d40k5zppky","transpose");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross", key="7lrk2rxqnwwdau8cx85oqkpmv", definition="static int mincross(graph_t * g, int startpass, int endpass, int doBalance)")
public static int mincross_(ST_Agraph_s g, int startpass, int endpass, boolean doBalance) {
ENTERING("7lrk2rxqnwwdau8cx85oqkpmv","mincross_");
try {
    int maxthispass=0, iter, trying, pass;
    int cur_cross, best_cross;
    
    if (startpass > 1) {
	cur_cross = best_cross = ncross(g);
	save_best(g);
    } else
	cur_cross = best_cross = INT_MAX;
    for (pass = startpass; pass <= endpass; pass++) {
	if (pass <= 1) {
	    maxthispass = MIN(4, Z.z().MaxIter);
	    if (EQ(g, dot_root(g)))
		build_ranks(g, pass);
	    if (pass == 0)
		flat_breakcycles(g);
	    flat_reorder(g);
	    
	    if ((cur_cross = ncross(g)) <= best_cross) {
		save_best(g);
		best_cross = cur_cross;
	    }
	    trying = 0;
	} else {
	    maxthispass = Z.z().MaxIter;
	    if (cur_cross > best_cross)
		restore_best(g);
	    cur_cross = best_cross;
	}
	trying = 0;
	for (iter = 0; iter < maxthispass; iter++) {
	    /*if (Verbose)
		fprintf(stderr,
			"mincross: pass %d iter %d trying %d cur_cross %d best_cross %d\n",
			pass, iter, trying, cur_cross, best_cross);*/
	    if (trying++ >= Z.z().MinQuit)
		break;
	    if (cur_cross == 0)
		break;
	    mincross_step(g, iter);
	    if ((cur_cross = ncross(g)) <= best_cross) {
		save_best(g);
		if (cur_cross < Z.z().Convergence * best_cross)
		    trying = 0;
		best_cross = cur_cross;
	    }
	}
	if (cur_cross == 0)
	    break;
    }
    if (cur_cross > best_cross)
	restore_best(g);
    if (best_cross > 0) {
	transpose(g, false);
	best_cross = ncross(g);
    }
    if (doBalance) {
	for (iter = 0; iter < maxthispass; iter++)
	    balance(g);
    }
    
    return best_cross;
} finally {
LEAVING("7lrk2rxqnwwdau8cx85oqkpmv","mincross_");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="restore_best", key="520049zkz9mafaeklgvm6s8e5", definition="static void restore_best(graph_t * g)")
public static void restore_best(ST_Agraph_s g) {
ENTERING("520049zkz9mafaeklgvm6s8e5","restore_best");
try {
    ST_Agnode_s n;
    int r;
    
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
	ND_order(n, (int)ND_coord(n).x);
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	GD_rank(Z.z().Root).get__(r).valid= 0;
    qsort1(GD_rank(g).get__(r).v,
    	    GD_rank(g).get__(r).n,
    	    function(mincross__c.class, "nodeposcmpf"));
    }
} finally {
LEAVING("520049zkz9mafaeklgvm6s8e5","restore_best");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="save_best", key="8uyqc48j0oul206l3np85wj9p", definition="static void save_best(graph_t * g)")
public static void save_best(ST_Agraph_s g) {
ENTERING("8uyqc48j0oul206l3np85wj9p","save_best");
try {
    ST_Agnode_s n;
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
    (ND_coord(n)).x = ND_order(n);
} finally {
LEAVING("8uyqc48j0oul206l3np85wj9p","save_best");
}
}




/* merges the connected components of g */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="merge_components", key="6d08fwi4dsk6ikk5d0gy6rq2h", definition="static void merge_components(graph_t * g)")
public static void merge_components(ST_Agraph_s g) {
ENTERING("6d08fwi4dsk6ikk5d0gy6rq2h","merge_components");
try {
    int c;
    ST_Agnode_s u, v;
    
    if (GD_comp(g).size <= 1)
	return;
    u = null;
    for (c = 0; c < GD_comp(g).size; c++) {
    v = (ST_Agnode_s) GD_comp(g).list.get_(c);
	if (u!=null)
	    ND_next(u, v);
	ND_prev(v, u);
	while (ND_next(v)!=null) {
	    v = ND_next(v);
	}
	u = v;
    }
    GD_comp(g).size = 1;
    GD_nlist(g, GD_comp(g).list.get_(0));
    GD_minrank(g, Z.z().GlobalMinRank);
    GD_maxrank(g, Z.z().GlobalMaxRank);
} finally {
LEAVING("6d08fwi4dsk6ikk5d0gy6rq2h","merge_components");
}
}




/* merge connected components, create globally consistent rank lists */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="merge2", key="91vebcdl3q3y0uyxef0iw71n9", definition="static void merge2(graph_t * g)")
public static void merge2(ST_Agraph_s g) {
ENTERING("91vebcdl3q3y0uyxef0iw71n9","merge2");
try {
    int i, r;
    ST_Agnode_s v;
    
    /* merge the components and rank limits */
    merge_components(g);
    
    /* install complete ranks */
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	GD_rank(g).get__(r).n = GD_rank(g).get__(r).an;
	GD_rank(g).get__(r).v = GD_rank(g).get__(r).av;
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    v = GD_rank(g).get__(r).v.get_(i);
	    if (v == null) {
		/*if (Verbose)
		    fprintf(stderr,
			    "merge2: graph %s, rank %d has only %d < %d nodes\n",
			    agnameof(g), r, i, GD_rank(g)[r].n);*/
		GD_rank(g).get__(r).n = i;
		break;
	    }
	    ND_order(v, i);
	}
    }
} finally {
LEAVING("91vebcdl3q3y0uyxef0iw71n9","merge2");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="cleanup2", key="3cwiyyk1d1jkoo9iqwb5bge4x", definition="static void cleanup2(graph_t * g, int nc)")
public static void cleanup2(ST_Agraph_s g, int nc) {
ENTERING("3cwiyyk1d1jkoo9iqwb5bge4x","cleanup2");
try {
    int i, j, r, c;
    ST_Agnode_s v;
    ST_Agedge_s e;
    
    if (Z.z().TI_list!=null) {
	Memory.free(Z.z().TI_list);
	Z.z().TI_list = null;
    }
    if (Z.z().TE_list!=null) {
	Memory.free(Z.z().TE_list);
	Z.z().TE_list = null;
    }
    /* fix vlists of clusters */
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_reset_vlists(GD_clust(g).get_(c));
    
    /* remove node temporary edges for ordering nodes */
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    v = GD_rank(g).get__(r).v.get_(i);
	    ND_order(v, i);
	    if (ND_flat_out(v).list!=null) {
		for (j = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(j))!=null; j++)
		    if (ED_edge_type(e) == FLATORDER) {
			delete_flat_edge(e);
			Memory.free(e.base.data);
			Memory.free(e);
			j--;
		    }
	    }
	}
	free_matrix(GD_rank(g).get__(r).flat);
    }
    /*if (Verbose)
	fprintf(stderr, "mincross %s: %d crossings, %.2f secs.\n",
		agnameof(g), nc, elapsed_sec());*/
} finally {
LEAVING("3cwiyyk1d1jkoo9iqwb5bge4x","cleanup2");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="arax68kzcf86dr2xu0gp962gq", definition="static node_t *neighbor(node_t * v, int dir)")
public static ST_Agnode_s neighbor(ST_Agnode_s v, int dir) {
ENTERING("arax68kzcf86dr2xu0gp962gq","neighbor");
try {
    ST_Agnode_s rv;
    
    rv = null;
assert(v!=null);
    if (dir < 0) {
	if (ND_order(v) > 0)
	    rv = GD_rank(Z.z().Root).get__(ND_rank(v)).v.get_(ND_order(v) - 1);
    } else
	rv = GD_rank(Z.z().Root).get__(ND_rank(v)).v.get_(ND_order(v) + 1);
assert((rv == null) || (ND_order(rv)-ND_order(v))*dir > 0);
    return rv;
} finally {
LEAVING("arax68kzcf86dr2xu0gp962gq","neighbor");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="is_a_normal_node_of", key="1waqm8z71hi389dt1wqh0bmhr", definition="static int is_a_normal_node_of(graph_t * g, node_t * v)")
public static boolean is_a_normal_node_of(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("1waqm8z71hi389dt1wqh0bmhr","is_a_normal_node_of");
try {
    return ((ND_node_type(v) == NORMAL) && agcontains(g, v));
} finally {
LEAVING("1waqm8z71hi389dt1wqh0bmhr","is_a_normal_node_of");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="is_a_vnode_of_an_edge_of", key="9f8atyi1unmleplge3rijdt4s", definition="static int is_a_vnode_of_an_edge_of(graph_t * g, node_t * v)")
public static boolean is_a_vnode_of_an_edge_of(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("9f8atyi1unmleplge3rijdt4s","is_a_vnode_of_an_edge_of");
try {
    if ((ND_node_type(v) == VIRTUAL)
	&& (ND_in(v).size == 1) && (ND_out(v).size == 1)) {
	ST_Agedge_s e = (ST_Agedge_s) ND_out(v).list.get_(0);
	while (ED_edge_type(e) != NORMAL)
	    e = ED_to_orig(e);
	if (agcontains(g, e))
	    return true;
    }
    return false;
} finally {
LEAVING("9f8atyi1unmleplge3rijdt4s","is_a_vnode_of_an_edge_of");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="inside_cluster", key="eo7ulc8vwmoaig0j479yapve2", definition="static int inside_cluster(graph_t * g, node_t * v)")
public static boolean inside_cluster(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("eo7ulc8vwmoaig0j479yapve2","inside_cluster");
try {
    return (is_a_normal_node_of(g, v) | is_a_vnode_of_an_edge_of(g, v));
} finally {
LEAVING("eo7ulc8vwmoaig0j479yapve2","inside_cluster");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="", key="8xkmkt4r6gfqj8gk0mokszoz0", definition="static node_t *furthestnode(graph_t * g, node_t * v, int dir)")
public static ST_Agnode_s furthestnode(ST_Agraph_s g, ST_Agnode_s v, int dir) {
ENTERING("8xkmkt4r6gfqj8gk0mokszoz0","furthestnode");
try {
    ST_Agnode_s u, rv;
    
    rv = u = v;
    while ((u = neighbor(u, dir))!=null) {
	if (is_a_normal_node_of(g, u))
	    rv = u;
	else if (is_a_vnode_of_an_edge_of(g, u))
	    rv = u;
    }
    return rv;
} finally {
LEAVING("8xkmkt4r6gfqj8gk0mokszoz0","furthestnode");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="save_vlist", key="bwmu2hkwud40601oq5vgo2f1h", definition="void save_vlist(graph_t * g)")
public static void save_vlist(ST_Agraph_s g) {
ENTERING("bwmu2hkwud40601oq5vgo2f1h","save_vlist");
try {
    int r;
    
    if (GD_rankleader(g)!=null)
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    GD_rankleader(g).set_(r, GD_rank(g).get__(r).v.get_(0));
	}
} finally {
LEAVING("bwmu2hkwud40601oq5vgo2f1h","save_vlist");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="rec_save_vlists", key="hwdxg97sefkuyd25x2q4pgzg", definition="void rec_save_vlists(graph_t * g)")
public static void rec_save_vlists(ST_Agraph_s g) {
ENTERING("hwdxg97sefkuyd25x2q4pgzg","rec_save_vlists");
try {
    int c;
    
    save_vlist(g);
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_save_vlists(GD_clust(g).get_(c));
} finally {
LEAVING("hwdxg97sefkuyd25x2q4pgzg","rec_save_vlists");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="rec_reset_vlists", key="f3b4wat4uxn5oil720i5mwq4v", definition="void rec_reset_vlists(graph_t * g)")
public static void rec_reset_vlists(ST_Agraph_s g) {
ENTERING("f3b4wat4uxn5oil720i5mwq4v","rec_reset_vlists");
try {
    int r, c;
    ST_Agnode_s u, v, w;
    
    /* fix vlists of sub-clusters */
    for (c = 1; c <= GD_n_cluster(g); c++)
	rec_reset_vlists(GD_clust(g).get_(c));
    
    if (GD_rankleader(g)!=null)
	for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	    v = GD_rankleader(g).get_(r);
	    u = furthestnode(g, v, -1);
	    w = furthestnode(g, v, 1);
	    GD_rankleader(g).set_(r, u);
	    GD_rank(g).get__(r).v = GD_rank(dot_root(g)).get__(r).v.plus_(ND_order(u));
	    GD_rank(g).get__(r).n = ND_order(w) - ND_order(u) + 1;
	}
} finally {
LEAVING("f3b4wat4uxn5oil720i5mwq4v","rec_reset_vlists");
}
}




//3 pv0rbbdopo4hmkbl5916qys1
// static Agraph_t* realFillRanks (Agraph_t* g, int rnks[], int rnks_sz, Agraph_t* sg) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="realFillRanks", key="pv0rbbdopo4hmkbl5916qys1", definition="static Agraph_t* realFillRanks (Agraph_t* g, int rnks[], int rnks_sz, Agraph_t* sg)")
public static ST_Agraph_s realFillRanks(ST_Agraph_s g, int[] rnks, int rnks_sz, ST_Agraph_s sg) {
ENTERING("pv0rbbdopo4hmkbl5916qys1","realFillRanks");
try {
 UNSUPPORTED("2o2sf6xi2aumo5k0vglp4ik2y"); // static Agraph_t*
UNSUPPORTED("3pmsfb0uhqmy5u141c932dtn1"); // realFillRanks (Agraph_t* g, int rnks[], int rnks_sz, Agraph_t* sg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("apiu3v3f9zs9yrq8e25nd2rti"); //     int i, c;
UNSUPPORTED("6yramhpyls8c6kexupyqip8oq"); //     Agedge_t* e;
UNSUPPORTED("ci2zh69w6nhi0q816i1ixuy9k"); //     Agnode_t* n;
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("wqxqmv4he79njsw6r7sludv"); // 	sg = realFillRanks (GD_clust(g)[c], rnks, rnks_sz, sg);
UNSUPPORTED("8htor72y55gsejjdeh0e8gdrs"); //     if (dot_root(g) == g)
UNSUPPORTED("bjae04ek2s3o50399zti5a1jh"); // 	return sg;
UNSUPPORTED("bqfhaopbxlwv8h4zfae1lhx6s"); //     memset (rnks, 0, sizeof(int)*rnks_sz);
UNSUPPORTED("7wq24g054kmx3aw25vk5ksj4"); //     for (n = agfstnode(g); n; n = agnxtnode(g,n)) {
UNSUPPORTED("do7v5atqor4gnpwgo18ibgjha"); // 	rnks[ND_rank(n)] = 1;
UNSUPPORTED("9fo44dng5uojukw2kj0z8vvdu"); // 	for (e = agfstout(g,n); e; e = agnxtout(g,e)) {
UNSUPPORTED("emlgvc6yzuboae72y6vxpn8z7"); // 	    for (i = ND_rank(n)+1; i <= ND_rank(aghead(e)); i++) 
UNSUPPORTED("d3fd9x50ix980jjw3old2jrcr"); // 		rnks[i] = 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7gfo0rr0un3w2026kt6ipknty"); //     for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
UNSUPPORTED("6mjvdb94lywg8d7ui6873f18p"); // 	if (rnks[i] == 0) {
UNSUPPORTED("8g0zxrf8w2zly4wln02j3lbnb"); // 	    if (!sg) {
UNSUPPORTED("eqx93butvb7swnxbz9j29winp"); // 		sg = agsubg (dot_root(g), "_new_rank", 1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9h7suegjozc071a939rh9apw1"); // 	    n = agnode (sg, NULL, 1);
UNSUPPORTED("8kclviftszfxkowsmlqq31u8"); // 	    agbindrec(n, "Agnodeinfo_t", sizeof(Agnodeinfo_t), NOT(0));
UNSUPPORTED("8wiry1vcdwy6bzdp98nmxss7c"); // 	    ND_rank(n) = i;
UNSUPPORTED("dy16sxtk3jj6127wavfcdx4yw"); // 	    ND_lw(n) = ND_rw(n) = 0.5;
UNSUPPORTED("6jr3li9af4sp0uxnpnoe8dqh8"); // 	    ND_ht(n) = 1;
UNSUPPORTED("c47fetnb62mx46qzuk9ag3qmu"); // 	    ND_UF_size(n) = 1;
UNSUPPORTED("899cyghezv8oxs0iomu58zncj"); // 	    alloc_elist(4, ND_in(n));
UNSUPPORTED("btfqtg58rqwdh7o4cc0gbaaai"); // 	    alloc_elist(4, ND_out(n));
UNSUPPORTED("9zap4z819hsiogrgmekh15f00"); // 	    agsubnode (g, n, 1);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1jw141gg4omv8r72xcbl5ln0o"); //     return sg;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("pv0rbbdopo4hmkbl5916qys1","realFillRanks");
}
}




//3 1qy9bupreg1pax62owznr98k
// static void fillRanks (Agraph_t* g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="fillRanks", key="1qy9bupreg1pax62owznr98k", definition="static void fillRanks (Agraph_t* g)")
public static void fillRanks(ST_Agraph_s g) {
ENTERING("1qy9bupreg1pax62owznr98k","fillRanks");
try {
    ST_Agraph_s sg;
    int rnks_sz = GD_maxrank(g) + 2;
    int[] rnks = new int[rnks_sz];
    sg = realFillRanks (g, rnks, rnks_sz, null);
    Memory.free (rnks);
} finally {
LEAVING("1qy9bupreg1pax62owznr98k","fillRanks");
}
}




@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="init_mincross", key="7fy4chyk12o7bgp1rv3h27yl3", definition="static void init_mincross(graph_t * g)")
public static void init_mincross(ST_Agraph_s g) {
ENTERING("7fy4chyk12o7bgp1rv3h27yl3","init_mincross");
try {
    int size;
    //if (Verbose)
	//start_timer();
    
    Z.z().ReMincross = false;
    Z.z().Root = g;
    /* alloc +1 for the null terminator usage in do_ordering() */
    /* also, the +1 avoids attempts to alloc 0 sizes, something
       that efence complains about */
    size = agnedges(dot_root(g)) + 1;
    Z.z().TE_list = CStarStar.<ST_Agedge_s>ALLOC(size, ST_Agedge_s.class);
    Z.z().TI_list = new int[size];
    mincross_options(g);
    if ((GD_flags(g) & NEW_RANK)!=0)
	fillRanks (g);
    class2(g);
    decompose(g, 1);
    allocate_ranks(g);
    ordered_edges(g);
    Z.z().GlobalMinRank = GD_minrank(g);
    Z.z().GlobalMaxRank = GD_maxrank(g);
} finally {
LEAVING("7fy4chyk12o7bgp1rv3h27yl3","init_mincross");
}
}




//3 6fprrp93vmz0jn3l4ro0iropp
// void flat_rev(Agraph_t * g, Agedge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_rev", key="6fprrp93vmz0jn3l4ro0iropp", definition="void flat_rev(Agraph_t * g, Agedge_t * e)")
public static void flat_rev(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("6fprrp93vmz0jn3l4ro0iropp","flat_rev");
try {
    int j;
    ST_Agedge_s rev;
    if (N(ND_flat_out(aghead(e)).list))
    // if (N(ND_flat_out(aghead(e)).list))
	rev = null;
    else
	for (j = 0; (rev = (ST_Agedge_s) ND_flat_out(aghead(e)).list.get_(j))!=null; j++)
	    if (EQ(aghead(rev), agtail(e)))
		break;
    if (rev!=null) {
	merge_oneway(e, rev);
	if (ED_to_virt(e) == null)
	    ED_to_virt(e, rev);
	if ((ED_edge_type(rev) == 4)
	    && (ED_to_orig(rev) == null))
	    ED_to_orig(rev, e);
	elist_append(e, ND_other(agtail(e)));
    } else {
	rev = new_virtual_edge(aghead(e), agtail(e), e);
	if (ED_edge_type(e) == 4)
	    ED_edge_type(rev, 4);
	else
	    ED_edge_type(rev, 3);
	ED_label(rev, ED_label(e));
	flat_edge(g, rev);
    }
} finally {
LEAVING("6fprrp93vmz0jn3l4ro0iropp","flat_rev");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_search", key="63ol0ch6cgln1nvl5oiz6n1v0", definition="static void flat_search(graph_t * g, node_t * v)")
public static void flat_search(ST_Agraph_s g, ST_Agnode_s v) {
ENTERING("63ol0ch6cgln1nvl5oiz6n1v0","flat_search");
try {
    int i;
    boolean hascl;
    ST_Agedge_s e;
    ST_adjmatrix_t M = GD_rank(g).get__(ND_rank(v)).flat;
    
    ND_mark(v, true);
    ND_onstack(v, true);
    hascl = (GD_n_cluster(dot_root(g)) > 0);
    if (ND_flat_out(v).list!=null)
	for (i = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(i))!=null; i++) {
	    if (hascl
		&& NOT(agcontains(g, agtail(e)) && agcontains(g, aghead(e))))
		continue;
	    if (ED_weight(e) == 0)
		continue;
	    if (ND_onstack(aghead(e)) == true) {
		assert(ND_low(aghead(e)) < M.nrows);
		assert(ND_low(agtail(e)) < M.ncols);
		M.data[ND_low(aghead(e))][ND_low(agtail(e))]=1;
		delete_flat_edge(e);
		i--;
		if (ED_edge_type(e) == FLATORDER)
		    continue;
		flat_rev(g, e);
	    } else {
		assert(ND_low(aghead(e)) < M.nrows);
		assert(ND_low(agtail(e)) < M.ncols);
		M.data[ND_low(agtail(e))][ND_low(aghead(e))]=1;
		if (ND_mark(aghead(e)) == 0)
		    flat_search(g, aghead(e));
	    }
	}
    ND_onstack(v, 0);
} finally {
LEAVING("63ol0ch6cgln1nvl5oiz6n1v0","flat_search");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_breakcycles", key="3v5h7z4vqivibvpt913lg8at0", definition="static void flat_breakcycles(graph_t * g)")
public static void flat_breakcycles(ST_Agraph_s g) {
ENTERING("3v5h7z4vqivibvpt913lg8at0","flat_breakcycles");
try {
    int i, r, flat;
    ST_Agnode_s v;
    
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	flat = 0;
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    v = GD_rank(g).get__(r).v.get_(i);
	    ND_mark(v, 0); ND_onstack(v, 0);
	    flatindex(v, i);
	    if ((ND_flat_out(v).size > 0) && (flat == 0)) {
		GD_rank(g).get__(r).flat =
		    new_matrix(GD_rank(g).get__(r).n, GD_rank(g).get__(r).n);
		flat = 1;
	    }
	}
	if (flat!=0) {
	    for (i = 0; i < GD_rank(g).get__(r).n; i++) {
		v = GD_rank(g).get__(r).v.get_(i);
		if (ND_mark(v) == 0)
		    flat_search(g, v);
	    }
	}
    }
} finally {
LEAVING("3v5h7z4vqivibvpt913lg8at0","flat_breakcycles");
}
}



/* allocate_ranks:
 * Allocate rank structure, determining number of nodes per rank.
 * Note that no nodes are put into the structure yet.
 */
@Difficult
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="allocate_ranks", key="d5vb6jiw8mhkaa8gjwn4eqfyn", definition="void allocate_ranks(graph_t * g)")
public static void allocate_ranks(ST_Agraph_s g) {
ENTERING("d5vb6jiw8mhkaa8gjwn4eqfyn","allocate_ranks");
try {
    int r, low, high;
    int cn[];
    ST_Agnode_s n;
    ST_Agedge_s e;
    
	cn = new int[GD_maxrank(g) + 2];
	/* must be 0 based, not GD_minrank */
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
   	cn[ND_rank(n)]++;
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
	    low = ND_rank(agtail(e));
	    high = ND_rank(aghead(e));
	    if (low > high) {
		int t = low;
		low = high;
		high = t;
	    }
	    for (r = low + 1; r < high; r++)
			cn[r]++;
	}
    }
    GD_rank(g, CStar.<ST_rank_t>ALLOC__(GD_maxrank(g) + 2, ST_rank_t.class));
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	GD_rank(g).get__(r).n = cn[r];
	GD_rank(g).get__(r).an = cn[r];
	CStarStar<ST_Agnode_s> tmp = CStarStar.<ST_Agnode_s>ALLOC(cn[r] + 1, ST_Agnode_s.class);
	GD_rank(g).get__(r).v = tmp;
	GD_rank(g).get__(r).av = tmp;
    }
    Memory.free(cn);
} finally {
LEAVING("d5vb6jiw8mhkaa8gjwn4eqfyn","allocate_ranks");
}
}




/* install a node at the current right end of its rank */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="install_in_rank", key="3lxoqxhiri9fgt20zc5jz3aa5", definition="void install_in_rank(graph_t * g, node_t * n)")
public static void install_in_rank(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("3lxoqxhiri9fgt20zc5jz3aa5","install_in_rank");
try {
    int i, r;
    
    r = ND_rank(n);
    i = GD_rank(g).get__(r).n;
    if (GD_rank(g).get__(r).an <= 0) {
	UNSUPPORTED("8qk1xhvvb994zhv9aq10k4v12"); // 	agerr(AGERR, "install_in_rank, line %d: %s %s rank %d i = %d an = 0\n",
	UNSUPPORTED("53h8d82ax23hys2k21hjswp72"); // 	      1034, agnameof(g), agnameof(n), r, i);
	return;
    }
    
    GD_rank(g).get__(r).v.set_(i, n);
    ND_order(n, i);
    GD_rank(g).get__(r).n++;
    assert(GD_rank(g).get__(r).n <= GD_rank(g).get__(r).an);
    if (ND_order(n) > GD_rank(Z.z().Root).get__(r).an) {
	UNSUPPORTED("399szcw1txekt1xssyw7s2x07"); // 	agerr(AGERR, "install_in_rank, line %d: ND_order(%s) [%d] > GD_rank(Root)[%d].an [%d]\n",
	UNSUPPORTED("9puojrmsk6vb1qc0jtr8ge4g8"); // 	      1052, agnameof(n), ND_order(n), r, GD_rank(Root)[r].an);
	return;
    }
    if ((r < GD_minrank(g)) || (r > GD_maxrank(g))) {
	UNSUPPORTED("7o1thnqda767wqpe2lh9mj03t"); // 	agerr(AGERR, "install_in_rank, line %d: rank %d not in rank range [%d,%d]\n",
	UNSUPPORTED("d2ugluzf7bmj7osicgitgy3sr"); // 	      1057, r, GD_minrank(g), GD_maxrank(g));
	return;
    }
    if (GD_rank(g).get__(r).v.plus_(ND_order(n)).comparePointer_(
	GD_rank(g).get__(r).av.plus_(GD_rank(Z.z().Root).get__(r).an))>0) {
	UNSUPPORTED("3eb32nc5czs5auwzz5p5mtl04"); // 	agerr(AGERR, "install_in_rank, line %d: GD_rank(g)[%d].v + ND_order(%s) [%d] > GD_rank(g)[%d].av + GD_rank(Root)[%d].an [%d]\n",
	UNSUPPORTED("3qe3qpw5h6vse39xs1ca9sjmo"); // 	      1062, r, agnameof(n),GD_rank(g)[r].v + ND_order(n), r, r, GD_rank(g)[r].av+GD_rank(Root)[r].an);
	return;
    }
} finally {
LEAVING("3lxoqxhiri9fgt20zc5jz3aa5","install_in_rank");
}
}




/*	install nodes in ranks. the initial ordering ensure that series-parallel
 *	graphs such as trees are drawn with no crossings.  it tries searching
 *	in- and out-edges and takes the better of the two initial orderings.
 */
@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="build_ranks", key="7t49bz6lfcbd9v63ds2x3518z", definition="void build_ranks(graph_t * g, int pass)")
public static void build_ranks(ST_Agraph_s g, int pass) {
ENTERING("7t49bz6lfcbd9v63ds2x3518z","build_ranks");
try {
    int i, j;
    ST_Agnode_s n, n0 = null;
    CStarStar<ST_Agedge_s> otheredges;
    ST_nodequeue q;
    
    q = new_queue(GD_n_nodes(g));
    for (n = GD_nlist(g); n!=null; n = ND_next(n))
	ND_mark(n, 0);
    
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++)
	GD_rank(g).get__(i).n = 0;
    
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	otheredges = ((pass == 0) ? ND_in(n).list : ND_out(n).list);
	if (otheredges.get_(0)!= null)
	    continue;
	if (ND_mark(n) == 0) {
	    ND_mark(n,  1);
	    enqueue(q, n);
	    while ((n0 = dequeue(q))!=null) {
		if (ND_ranktype(n0) != CLUSTER) {
		    install_in_rank(g, n0);
		    enqueue_neighbors(q, n0, pass);
		} else {
		    install_cluster(g, n0, pass, q);
		}
	    }
	}
    }
    if (dequeue(q)!=null)
    UNSUPPORTED("1b3hbd5artrq77i58q2o9kgz3"); // 	agerr(AGERR, "surprise\n");
    for (i = GD_minrank(g); i <= GD_maxrank(g); i++) {
	GD_rank(Z.z().Root).get__(i).valid = 0;
	if (GD_flip(g) && (GD_rank(g).get__(i).n > 0)) {
	    int nn, ndiv2;
	    CStarStar<ST_Agnode_s> vlist = GD_rank(g).get__(i).v;
	    nn = GD_rank(g).get__(i).n - 1;
	    ndiv2 = nn / 2;
	    for (j = 0; j <= ndiv2; j++)
		exchange(vlist.get_(j), vlist.get_(nn - j));
	}
    }
    
    
    if (EQ(g, dot_root(g)) && ncross(g) > 0)
	transpose(g, false);
    free_queue(q);
} finally {
LEAVING("7t49bz6lfcbd9v63ds2x3518z","build_ranks");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="enqueue_neighbors", key="bmjlneqxex6a9silzkkidkx6s", definition="void enqueue_neighbors(nodequeue * q, node_t * n0, int pass)")
public static void enqueue_neighbors(ST_nodequeue q, ST_Agnode_s n0, int pass) {
ENTERING("bmjlneqxex6a9silzkkidkx6s","enqueue_neighbors");
try {
    int i;
    ST_Agedge_s e;
    
    if (pass == 0) {
	for (i = 0; i < ND_out(n0).size; i++) {
	    e = (ST_Agedge_s) ND_out(n0).list.get_(i);
	    if ((ND_mark(aghead(e))) == 0) {
		ND_mark(aghead(e), 1);
		enqueue(q, aghead(e));
	    }
	}
    } else {
	for (i = 0; i < ND_in(n0).size; i++) {
	    e = (ST_Agedge_s) ND_in(n0).list.get_(i);
	    if (((ND_mark(agtail(e)))) == 0) {
		ND_mark(agtail(e), 1);
		enqueue(q, agtail(e));
	    }
	}
    }
} finally {
LEAVING("bmjlneqxex6a9silzkkidkx6s","enqueue_neighbors");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="constraining_flat_edge", key="c8nqj0v20api63pikohsbx92u", definition="static int constraining_flat_edge(Agraph_t *g, Agnode_t *v, Agedge_t *e)")
public static boolean constraining_flat_edge(ST_Agraph_s g, ST_Agnode_s v, ST_Agedge_s e) {
ENTERING("c8nqj0v20api63pikohsbx92u","constraining_flat_edge");
try {
	if (ED_weight(e) == 0) return false;
	if (N(inside_cluster(g,agtail(e)))) return false;
	if (N(inside_cluster(g,aghead(e)))) return false;
	return true;
} finally {
LEAVING("c8nqj0v20api63pikohsbx92u","constraining_flat_edge");
}
}




/* construct nodes reachable from 'here' in post-order.
* This is the same as doing a topological sort in reverse order.
*/
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="postorder", key="46to0pkk188af2dlkik2ab7e3", definition="static int postorder(graph_t * g, node_t * v, node_t ** list, int r)")
public static int postorder(ST_Agraph_s g, ST_Agnode_s v, CStarStar<ST_Agnode_s> list, int r) {
ENTERING("46to0pkk188af2dlkik2ab7e3","postorder");
try {
    ST_Agedge_s e;
    int i, cnt = 0;
    
    ND_mark(v, true);
    if (ND_flat_out(v).size > 0) {
	for (i = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(i))!=null; i++) {
	    if (N(constraining_flat_edge(g,v,e))) continue;
	    if ((ND_mark(aghead(e))) == 0)
		cnt += postorder(g, aghead(e), list.plus_(cnt), r);
	}
    }
    assert(ND_rank(v) == r);
    list.set_(cnt++, v);
    return cnt;
} finally {
LEAVING("46to0pkk188af2dlkik2ab7e3","postorder");
}
}




@Reviewed(when = "15/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_reorder", key="zuxoswju917kyl08a5f0gtp6", definition="static void flat_reorder(graph_t * g)")
public static void flat_reorder(ST_Agraph_s g) {
ENTERING("zuxoswju917kyl08a5f0gtp6","flat_reorder");
try {
    int i, j, r, pos, n_search, local_in_cnt, local_out_cnt, base_order;
    ST_Agnode_s v, t;
    CStarStar<ST_Agnode_s> left, right;
    CStarStar<ST_Agnode_s> temprank = null;
    ST_Agedge_s flat_e, e;
    
    if (GD_has_flat_edges(g) == 0)
	return;
    for (r = GD_minrank(g); r <= GD_maxrank(g); r++) {
	if (GD_rank(g).get__(r).n == 0) continue;
	base_order = ND_order(GD_rank(g).get__(r).v.get_(0));
	for (i = 0; i < GD_rank(g).get__(r).n; i++)
	    ND_mark(GD_rank(g).get__(r).v.get_(i), 0);
	temprank = CStarStar.<ST_Agnode_s>REALLOC(i + 1, temprank, ST_Agnode_s.class);
	pos = 0;
	
	/* construct reverse topological sort order in temprank */
	for (i = 0; i < GD_rank(g).get__(r).n; i++) {
	    if (GD_flip(g)) v = (ST_Agnode_s) GD_rank(g).get__(r).v.get_(i);
	    else v = GD_rank(g).get__(r).v.get_(GD_rank(g).get__(r).n - i - 1);
	    
	    local_in_cnt = local_out_cnt = 0;
	    for (j = 0; j < ND_flat_in(v).size; j++) {
		flat_e = (ST_Agedge_s) ND_flat_in(v).list.get_(j);
		if (constraining_flat_edge(g,v,flat_e)) local_in_cnt++;
	    }
	    for (j = 0; j < ND_flat_out(v).size; j++) {
		flat_e = (ST_Agedge_s) ND_flat_out(v).list.get_(j);
		if (constraining_flat_edge(g,v,flat_e)) local_out_cnt++;
	    }
	    if ((local_in_cnt == 0) && (local_out_cnt == 0))
		temprank.set_(pos++, v);
	    else {
		if ((ND_mark(v) == 0) && (local_in_cnt == 0)) {
		    left = temprank.plus_(pos);
		    n_search = postorder(g, v, left, r);
		    pos += n_search;
		}
	    }
	}
	
	if (pos!=0) {
	    if (GD_flip(g) == false) {
		left = temprank;
		right = temprank.plus_(pos - 1);
		while (left.comparePointer_(right) < 0) {
		    t = left.get_(0);
		    left.set_(0, right.get_(0));
		    right.set_(0, t);
		    left = left.plus_(1);
		    right = right.plus_(-1);
		}
	    }
	    for (i = 0; i < GD_rank(g).get__(r).n; i++) {
		v = temprank.get_(i);
		GD_rank(g).get__(r).v.set_(i, v);
		ND_order(v, i + base_order);
	    }
	    
	    /* nonconstraint flat edges must be made LR */
	    for (i = 0; i < GD_rank(g).get__(r).n; i++) {
		v = GD_rank(g).get__(r).v.get_(i);
		if (ND_flat_out(v).list!=null) {
		    for (j = 0; (e = (ST_Agedge_s) ND_flat_out(v).list.get_(j))!=null; j++) {
			if ( ((GD_flip(g) == false) && (ND_order(aghead(e)) < ND_order(agtail(e)))) ||
				 ( (GD_flip(g)) && (ND_order(aghead(e)) > ND_order(agtail(e)) ))) {
			    assert(constraining_flat_edge(g,v,e) == false);
			    delete_flat_edge(e);
			    j--;
			    flat_rev(g, e);
			}
		    }
		}
	    }
	    /* postprocess to restore intended order */
	}
	/* else do no harm! */
	GD_rank(Z.z().Root).get__(r).valid = 0;
    }
    if (temprank!=null)
	Memory.free(temprank);
} finally {
LEAVING("zuxoswju917kyl08a5f0gtp6","flat_reorder");
}
}




@Reviewed(when = "16/11/2020²")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="reorder", key="inv6wazjcnh4xkzzphsdcmg4", definition="static void reorder(graph_t * g, int r, int reverse, int hasfixed)")
public static void reorder(ST_Agraph_s g, int r, boolean reverse, boolean hasfixed) {
ENTERING("inv6wazjcnh4xkzzphsdcmg4","reorder");
try {
    boolean changed = false;
    int nelt;
    boolean muststay, sawclust;
    CStarStar<ST_Agnode_s> vlist = GD_rank(g).get__(r).v;
    CStarStar<ST_Agnode_s> lp, rp=null, ep = vlist.plus_(GD_rank(g).get__(r).n);
    
    for (nelt = GD_rank(g).get__(r).n - 1; nelt >= 0; nelt--) {
	lp = vlist;
	while (lp.comparePointer_(ep)<0) {
	    /* find leftmost node that can be compared */
	    while ((lp.comparePointer_(ep) < 0) && (ND_mval(lp.get_(0)) < 0))
		lp = lp.plus_(1);
	    if (lp.comparePointer_(ep) >= 0)
		break;
	    /* find the node that can be compared */
	    sawclust = muststay = false;
	    for (rp = lp.plus_(1); rp.comparePointer_(ep) < 0; rp=rp.plus_(1)) {
		if (sawclust && ND_clust(rp.get_(0))!=null)
		    continue;	/* ### */
		if (left2right(g, lp.get_(0), rp.get_(0))) {
		    muststay = true;
		    break;
		}
		if (ND_mval(rp.get_(0)) >= 0)
		    break;
		if (ND_clust(rp.get_(0))!=null)
		    sawclust = true;	/* ### */
	    }
	    if (rp.comparePointer_(ep) >= 0)
		break;
	    if (muststay == false) {
		int p1 = (int) (ND_mval(lp.get_(0)));
		int p2 = (int) (ND_mval(rp.get_(0)));
		if ((p1 > p2) || ((p1 == p2) && (reverse))) {
		    exchange(lp.get_(0), rp.get_(0));
		    changed=true;
		}
	    }
	    lp = rp;
	}
	if ((hasfixed == false) && (reverse == false))
	    ep = ep.plus_(-1);
    }
    if (changed) {
	GD_rank(Z.z().Root).get__(r).valid= 0;
	if (r > 0)
	    GD_rank(Z.z().Root).get__(r - 1).valid= 0;
    }
} finally {
LEAVING("inv6wazjcnh4xkzzphsdcmg4","reorder");
}
}




//3 14t80owwvm7io4ou6czb9ba9
// static void mincross_step(graph_t * g, int pass) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross_step", key="14t80owwvm7io4ou6czb9ba9", definition="static void mincross_step(graph_t * g, int pass)")
public static void mincross_step(ST_Agraph_s g, int pass) {
ENTERING("14t80owwvm7io4ou6czb9ba9","mincross_step");
try {
    int r, other, first, last, dir;
    boolean hasfixed, reverse;
    if ((pass % 4) < 2)
	reverse = true;
    else
	reverse = false;
    if ((pass % 2)!=0) {
	r = GD_maxrank(g) - 1;
	dir = -1;
    } /* up pass */
    else {
	r = 1;
	dir = 1;
    }				/* down pass */
    if (pass % 2 == 0) {	/* down pass */
	first = GD_minrank(g) + 1;
	if (GD_minrank(g) > GD_minrank(Z.z().Root))
	    first--;
	last = GD_maxrank(g);
	dir = 1;
    } else {			/* up pass */
	first = GD_maxrank(g) - 1;
	last = GD_minrank(g);
	if (GD_maxrank(g) < GD_maxrank(Z.z().Root))
	    first++;
	dir = -1;
    }
    for (r = first; r != last + dir; r += dir) {
	other = r - dir;
	hasfixed = medians(g, r, other);
	reorder(g, r, reverse, hasfixed);
    }
    transpose(g, NOT(reverse));
} finally {
LEAVING("14t80owwvm7io4ou6czb9ba9","mincross_step");
}
}




//3 aq18oa4k4grixvfjx7r2qnl6r
// static int local_cross(elist l, int dir) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="local_cross", key="aq18oa4k4grixvfjx7r2qnl6r", definition="static int local_cross(elist l, int dir)")
public static int local_cross(final ST_elist l, int dir) {
// WARNING!! STRUCT
return local_cross_w_(l.copy(), dir);
}
private static int local_cross_w_(final ST_elist l, int dir) {
ENTERING("aq18oa4k4grixvfjx7r2qnl6r","local_cross");
try {
    int i, j;
    boolean is_out = false;
    int cross = 0;
    ST_Agedge_s e, f;
    if (dir > 0)
	is_out = NOT(0);
    else
	is_out = false;
    for (i = 0; (e = l.list.get_(i))!=null; i++) {
	if (is_out)
	    for (j = i + 1; (f = l.list.get_(j))!=null; j++) {
		if ((ND_order(aghead(f)) - ND_order(aghead(e)))
			 * (ED_tail_port(f).p.x - ED_tail_port(e).p.x) < 0)
UNSUPPORTED("bw8rwv11yqzss88pad7ljil8a"); // 		    cross += ED_xpenalty(e) * ED_xpenalty(f);
	} else
	    for (j = i + 1; (f = l.list.get_(j))!=null; j++) {
UNSUPPORTED("bza83c6rmihrkzyllwf0jm4tn"); // 		if ((ND_order(agtail(f)) - ND_order(agtail(e)))
UNSUPPORTED("csrxg0y1azmvde7t833lm13sp"); // 			* (ED_head_port(f).p.x - ED_head_port(e).p.x) < 0)
UNSUPPORTED("bw8rwv11yqzss88pad7ljil8a"); // 		    cross += ED_xpenalty(e) * ED_xpenalty(f);
	    }
    }
    return cross;
} finally {
LEAVING("aq18oa4k4grixvfjx7r2qnl6r","local_cross");
}
}




//static __ptr__ Count;
//static int C;
//3 bk5nklhfqgg0uwkv7tv6dn8r2
// static int rcross(graph_t * g, int r) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="rcross", key="bk5nklhfqgg0uwkv7tv6dn8r2", definition="static int rcross(graph_t * g, int r)")
public static int rcross(ST_Agraph_s g, int r) {
ENTERING("bk5nklhfqgg0uwkv7tv6dn8r2","rcross");
try {
    int top, bot, cross, max, i, k;
    ST_Agnode_s v;
    CStarStar<ST_Agnode_s> rtop;
    
    cross = 0;
    max = 0;
    rtop = GD_rank(g).get__(r).v;
    
    if (Z.z().C <= GD_rank(Z.z().Root).get__(r + 1).n) {
	Z.z().C = GD_rank(Z.z().Root).get__(r + 1).n + 1;
	Z.z().Count = ALLOC_INT(Z.z().C, Z.z().Count);
    }
    
    for (i = 0; i < GD_rank(g).get__(r + 1).n; i++)
    	Z.z().Count[i] = 0;
    
    for (top = 0; top < GD_rank(g).get__(r).n; top++) {
	ST_Agedge_s e;
	if (max > 0) {
	    for (i = 0; (e = (ST_Agedge_s) ND_out(rtop.get_(top)).list.get_(i))!=null; i++) {
		for (k = ND_order(aghead(e)) + 1; k <= max; k++)
		    cross += Z.z().Count[k] * ED_xpenalty(e);
	    }
	}
	for (i = 0; (e = (ST_Agedge_s) ND_out(rtop.get_(top)).list.get_(i))!=null; i++) {
	    int inv = ND_order(aghead(e));
	    if (inv > max)
		max = inv;
	    Z.z().Count[inv] += ED_xpenalty(e);
	}
    }
    for (top = 0; top < GD_rank(g).get__(r).n; top++) {
	v = (ST_Agnode_s) GD_rank(g).get__(r).v.get_(top);
	if (ND_has_port(v))
	    cross += local_cross(ND_out(v), 1);
    }
    for (bot = 0; bot < GD_rank(g).get__(r + 1).n; bot++) {
	v = (ST_Agnode_s) GD_rank(g).get__(r + 1).v.get_(bot);
	if (ND_has_port(v))
	    cross += local_cross(ND_in(v), -1);
    }
    return cross;
} finally {
LEAVING("bk5nklhfqgg0uwkv7tv6dn8r2","rcross");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="ncross", key="dbjmz2tnii2pn9sxg26ap6w5r", definition="int ncross(graph_t * g)")
public static int ncross(ST_Agraph_s g) {
ENTERING("dbjmz2tnii2pn9sxg26ap6w5r","ncross");
try {
    int r, count, nc;
    
    g = Z.z().Root;
    count = 0;
    for (r = GD_minrank(g); r < GD_maxrank(g); r++) {
	if (GD_rank(g).get__(r).valid!=0)
	    count += GD_rank(g).get__(r).cache_nc;
	else {
	    nc = rcross(g, r);
	    GD_rank(g).get__(r).cache_nc = nc;
	    count += nc;
	    GD_rank(g).get__(r).valid = 1;
	}
    }
    return count;
} finally {
LEAVING("dbjmz2tnii2pn9sxg26ap6w5r","ncross");
}
}




//3 8wrsq8a2vooekcm3cdtv5x3ke
// static int ordercmpf(int *i0, int *i1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="ordercmpf", key="8wrsq8a2vooekcm3cdtv5x3ke", definition="static int ordercmpf(int *i0, int *i1)")
public static int ordercmpf(Integer i0, Integer i1) {
ENTERING("8wrsq8a2vooekcm3cdtv5x3ke","ordercmpf");
try {
    return (i0) - (i1);
} finally {
LEAVING("8wrsq8a2vooekcm3cdtv5x3ke","ordercmpf");
}
}




//3 7397kynkpqf2m1jkpmi8pgf0n
// static int flat_mval(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="flat_mval", key="7397kynkpqf2m1jkpmi8pgf0n", definition="static int flat_mval(node_t * n)")
public static boolean flat_mval(ST_Agnode_s n) {
ENTERING("7397kynkpqf2m1jkpmi8pgf0n","flat_mval");
try {
    int i;
    ST_Agedge_s e;
    CStarStar<ST_Agedge_s> fl;
    ST_Agnode_s nn;
    if (ND_flat_in(n).size > 0) {
	fl = ND_flat_in(n).list;
	nn = agtail(fl.get_(0));
	for (i = 1; (e = fl.get_(i))!=null; i++)
	    if (ND_order(agtail(e)) > ND_order(nn))
		nn = agtail(e);
	if (ND_mval(nn) >= 0) {
	    ND_mval(n, ND_mval(nn) + 1);
	    return false;
	}
    } else if (ND_flat_out(n).size > 0) {
	fl = ND_flat_out(n).list;
	nn = aghead(fl.get_(0));
	for (i = 1; (e = fl.get_(i))!=null; i++)
	    if (ND_order(aghead(e)) < ND_order(nn))
		nn = aghead(e);
	if (ND_mval(nn) > 0) {
	    ND_mval(n, ND_mval(nn) - 1);
	    return false;
	}
    }
    return true;
} finally {
LEAVING("7397kynkpqf2m1jkpmi8pgf0n","flat_mval");
}
}




//3 azvdpixwwxspl31wp7f4k4fmh
// static boolean medians(graph_t * g, int r0, int r1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="medians", key="azvdpixwwxspl31wp7f4k4fmh", definition="static boolean medians(graph_t * g, int r0, int r1)")
public static boolean medians(ST_Agraph_s g, int r0, int r1) {
ENTERING("azvdpixwwxspl31wp7f4k4fmh","medians");
try {
    int i, j, j0, lm, rm, lspan, rspan;
    int[] list;
    ST_Agnode_s n;
    CStarStar<ST_Agnode_s> v;
    ST_Agedge_s e;
    boolean hasfixed = false;
    list = Z.z().TI_list;
    v = GD_rank(g).get__(r0).v;
    for (i = 0; i < GD_rank(g).get__(r0).n; i++) {
	n = v.get_(i);
	j = 0;
	if (r1 > r0)
	    for (j0 = 0; (e = ND_out(n).list.get_(j0))!=null; j0++) {
		if (ED_xpenalty(e) > 0)
			list[j++] = ((256 * ND_order(aghead(e)) + (ED_head_port(e)).order));
	} else
	    for (j0 = 0; (e = ND_in(n).list.get_(j0))!=null; j0++) {
		if (ED_xpenalty(e) > 0)
			list[j++] = ((256 * ND_order(agtail(e)) + (ED_tail_port(e)).order));
	    }
	switch (j) {
	case 0:
	    ND_mval(n, -1);
	    break;
	case 1:
	    ND_mval(n, list[0]);
	    break;
	case 2:
	    ND_mval(n, (list[0] + list[1]) / 2);
	    break;
	default:
	    qsort2(list,
	    	    j,
	    	    function(mincross__c.class, "ordercmpf"));
	    if (j % 2!=0)
		ND_mval(n, list[j / 2]);
	    else {
		/* weighted median */
		rm = j / 2;
		lm = rm - 1;
		rspan = list[j - 1] - list[rm];
		lspan = list[lm] - list[0];
		if (lspan == rspan)
		    ND_mval(n, (list[lm] + list[rm]) / 2);
		else {
		    int w = list[lm] * rspan + list[rm] * lspan;
		    ND_mval(n, w / (lspan + rspan));
		}
	    }
	}
    }
    for (i = 0; i < GD_rank(g).get__(r0).n; i++) {
	n = (ST_Agnode_s) v.get_(i);
	if ((ND_out(n).size == 0) && (ND_in(n).size == 0))
	    hasfixed |= flat_mval(n);
    }
    return hasfixed;
} finally {
LEAVING("azvdpixwwxspl31wp7f4k4fmh","medians");
}
}




//3 2vdhpcykq508ma83aif8sxcbd
// static int nodeposcmpf(node_t ** n0, node_t ** n1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="nodeposcmpf", key="2vdhpcykq508ma83aif8sxcbd", definition="static int nodeposcmpf(node_t ** n0, node_t ** n1)")
public static Object nodeposcmpf(CStarStar<ST_Agnode_s> n0, CStarStar<ST_Agnode_s> n1) {
ENTERING("2vdhpcykq508ma83aif8sxcbd","nodeposcmpf");
try {
    return (ND_order(n0.get_(0)) - ND_order(n1.get_(0)));
} finally {
LEAVING("2vdhpcykq508ma83aif8sxcbd","nodeposcmpf");
}
}




//3 87c98ld9c4hv87ekcxdyojx8l
// static int edgeidcmpf(edge_t ** e0, edge_t ** e1) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="edgeidcmpf", key="87c98ld9c4hv87ekcxdyojx8l", definition="static int edgeidcmpf(edge_t ** e0, edge_t ** e1)")
public static Object edgeidcmpf(Object... arg) {
UNSUPPORTED("apvyql25ya1p7w7i7396gucdg"); // static int edgeidcmpf(edge_t ** e0, edge_t ** e1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9gjc2rit3ezq4il0t5aymk1z"); //     return (AGSEQ(*e0) - AGSEQ(*e1));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 40as9opn4mzq4gp4nkmp4dj8w
// static int table[3][3] = 
private static int table[][] = new int[][]{
    /* ordinary */ {1, 1, 1},
    /* singleton */ {1, 2, 2},
    /* virtual */ {1, 2, 4}
};



//3 7j638prioxd97f74v1v4adbsf
// static int endpoint_class(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="endpoint_class", key="7j638prioxd97f74v1v4adbsf", definition="static int endpoint_class(node_t * n)")
public static int endpoint_class(ST_Agnode_s n) {
ENTERING("7j638prioxd97f74v1v4adbsf","endpoint_class");
try {
    if (ND_node_type(n) == 1)
	return 2;
    if (ND_weight_class(n) <= 1)
	return 1;
    return 0;
} finally {
LEAVING("7j638prioxd97f74v1v4adbsf","endpoint_class");
}
}




//3 es57bn7ga4wc9tqtcixpn0451
// void virtual_weight(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="virtual_weight", key="es57bn7ga4wc9tqtcixpn0451", definition="void virtual_weight(edge_t * e)")
public static void virtual_weight(ST_Agedge_s e) {
ENTERING("es57bn7ga4wc9tqtcixpn0451","virtual_weight");
try {
    int t;
    t = table[endpoint_class(agtail(e))][endpoint_class(aghead(e))];
    ED_weight(e, ED_weight(e) * t);
} finally {
LEAVING("es57bn7ga4wc9tqtcixpn0451","virtual_weight");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/mincross.c", name="mincross_options", key="7ru09oqbudpeofsthzveig2m2", definition="static void mincross_options(graph_t * g)")
public static void mincross_options(ST_Agraph_s g) {
ENTERING("7ru09oqbudpeofsthzveig2m2","mincross_options");
try {
    CString p;
    double f;
    
    /* set default values */
    Z.z().MinQuit = 8;
    Z.z().MaxIter = 24;
    Z.z().Convergence = .995;
    
    p = agget(g, new CString("mclimit"));
    if (p!=null && ((f = atof(p)) > 0.0)) {
UNSUPPORTED("4iu53eiz077u6joqgwawca8ya"); // 	MinQuit = ((1)>(MinQuit * f)?(1):(MinQuit * f));
UNSUPPORTED("38po81l36cibw6jc3qlsscpcu"); // 	MaxIter = ((1)>(MaxIter * f)?(1):(MaxIter * f));
    }   
} finally {
LEAVING("7ru09oqbudpeofsthzveig2m2","mincross_options");
}
}


}
