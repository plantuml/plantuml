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
package gen.lib.common;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.common.utils__c.dequeue;
import static gen.lib.common.utils__c.enqueue;
import static gen.lib.common.utils__c.free_queue;
import static gen.lib.common.utils__c.new_queue;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.atoi;
import static smetana.core.JUtils.setjmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.ED_cutvalue;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_tree_index;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.INT_MAX;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.MIN;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_lim;
import static smetana.core.Macro.ND_low;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_par;
import static smetana.core.Macro.ND_priority;
import static smetana.core.Macro.ND_rank;
import static smetana.core.Macro.ND_tree_in;
import static smetana.core.Macro.ND_tree_out;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.SEARCHSIZE;
import static smetana.core.Macro.SEQ;
import static smetana.core.Macro.SLACK;
import static smetana.core.Macro.TREE_EDGE;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.free_list;

import gen.annotation.Difficult;
import gen.annotation.HasND_Rank;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_nodequeue;
import smetana.core.CStarStar;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.jmp_buf;


/* 
 * Network Simplex Algorithm for Ranking Nodes of a DAG
 */
public class ns__c {


//3 ciez0pfggxdljedzsbklq49f0
// static inline point pointof(int x, int y) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="pointof", key="ciez0pfggxdljedzsbklq49f0", definition="static inline point pointof(int x, int y)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="boxof", key="7cufnfitrh935ew093mw0i4b7", definition="static inline box boxof(int llx, int lly, int urx, int ury)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="add_point", key="1n5xl70wxuabyf97mclvilsm6", definition="static inline point add_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="sub_point", key="ai2dprak5y6obdsflguh5qbd7", definition="static inline point sub_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="sub_pointf", key="16f6pyogcv3j7n2p0n8giqqgh", definition="static inline pointf sub_pointf(pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="mid_point", key="9k50jgrhc4f9824vf8ony74rw", definition="static inline point mid_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="mid_pointf", key="59c4f7im0ftyowhnzzq2v9o1x", definition="static inline pointf mid_pointf(pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="interpolate_pointf", key="5r18p38gisvcx3zsvbb9saixx", definition="static inline pointf interpolate_pointf(double t, pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="exch_xy", key="bxzrv2ghq04qk5cbyy68s4mol", definition="static inline point exch_xy(point p)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="exch_xyf", key="9lt3e03tac6h6sydljrcws8fd", definition="static inline pointf exch_xyf(pointf p)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="box_bb", key="8l9qhieokthntzdorlu5zn29b", definition="static inline box box_bb(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="boxf_bb", key="clws9h3bbjm0lw3hexf8nl4c4", definition="static inline boxf boxf_bb(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="box_intersect", key="bit6ycxo1iqd2al92y8gkzlvb", definition="static inline box box_intersect(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="boxf_intersect", key="8gfybie7k6pgb3o1a6llgpwng", definition="static inline boxf boxf_intersect(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="box_overlap", key="7z8j2quq65govaaejrz7b4cvb", definition="static inline int box_overlap(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="boxf_overlap", key="4z0suuut2acsay5m8mg9dqjdu", definition="static inline int boxf_overlap(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="box_contains", key="dd34swz5rmdgu3a2np2a4h1dy", definition="static inline int box_contains(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="boxf_contains", key="8laj1bspbu2i1cjd9upr7xt32", definition="static inline int boxf_contains(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="perp", key="4wf5swkz24xx51ja2dynbycu1", definition="static inline pointf perp (pointf p)")
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
@Original(version="2.38.0", path="lib/common/ns.c", name="scale", key="6dtlpzv4mvgzb9o0b252yweuv", definition="static inline pointf scale (double c, pointf p)")
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


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();


@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/common/ns.c", name="add_tree_edge", key="6au5htcaxhw0blmx5c48v03u0", definition="static void add_tree_edge(edge_t * e)")
public static void add_tree_edge(ST_Agedge_s e) {
ENTERING("6au5htcaxhw0blmx5c48v03u0","add_tree_edge");
try {
    ST_Agnode_s n;
    if (TREE_EDGE(e)) {
	UNSUPPORTED("cq4nqjjxvb0dtdfy4c7pwpqai"); // 	agerr(AGERR, "add_tree_edge: missing tree edge\n");
	UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
    ED_tree_index(e, Z.z().Tree_edge.size);
    
    Z.z().Tree_edge.list.set_(Z.z().Tree_edge.size++, e);
        
    if (ND_mark(agtail(e)) == 0)
    Z.z().Tree_node.list.set_(Z.z().Tree_node.size++, agtail(e));
    if (ND_mark(aghead(e)) == 0)
	Z.z().Tree_node.list.set_(Z.z().Tree_node.size++, aghead(e));
    n = agtail(e);
    ND_mark(n, 1);
    ND_tree_out(n).list.set_(ND_tree_out(n).size++, e);
    ND_tree_out(n).list.set_(ND_tree_out(n).size, null);
    
    if (ND_out(n).list.get_(ND_tree_out(n).size - 1) == null) {
UNSUPPORTED("9src34zgmgy8yvdzfs1ozlh0w"); // 	agerr(AGERR, "add_tree_edge: empty outedge list\n");
UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
    n = aghead(e);
    ND_mark(n, 1);
    
    ND_tree_in(n).list.set_(ND_tree_in(n).size++, e);
    ND_tree_in(n).list.set_(ND_tree_in(n).size, null);

    if (ND_in(n).list.get_(ND_tree_in(n).size - 1) == null) {
UNSUPPORTED("f0uri98pv606g2qjpy9k385cy"); // 	agerr(AGERR, "add_tree_edge: empty inedge list\n");
UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
} finally {
LEAVING("6au5htcaxhw0blmx5c48v03u0","add_tree_edge");
}
}




//3 9b7b78pmafynmvffztrqnlxtn
// static void exchange_tree_edges(edge_t * e, edge_t * f) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="exchange_tree_edges", key="9b7b78pmafynmvffztrqnlxtn", definition="static void exchange_tree_edges(edge_t * e, edge_t * f)")
public static void exchange_tree_edges(ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("9b7b78pmafynmvffztrqnlxtn","exchange_tree_edges");
try {
    int i, j;
    ST_Agnode_s n;
    ED_tree_index(f, ED_tree_index(e));
    Z.z().Tree_edge.list.set_(ED_tree_index(e), f);
    ED_tree_index(e, -1);
    n = agtail(e);
    ND_tree_out(n).size = ND_tree_out(n).size -1;
    i = ND_tree_out(n).size;
    for (j = 0; j <= i; j++)
	if (EQ(ND_tree_out(n).list.get_(j), e))
	    break;
    ND_tree_out(n).list.set_(j, ND_tree_out(n).list.get_(i));
    ND_tree_out(n).list.set_(i, null);
    n = aghead(e);
    ND_tree_in(n).size = ND_tree_in(n).size -1;
    i = ND_tree_in(n).size;
    for (j = 0; j <= i; j++)
	if (EQ(ND_tree_in(n).list.get_(j), e))
	    break;
    ND_tree_in(n).list.set_(j, ND_tree_in(n).list.get_(i));
    ND_tree_in(n).list.set_(i, null);
    n = agtail(f);
    ND_tree_out(n).list.set_(ND_tree_out(n).size, f);
    ND_tree_out(n).size = ND_tree_out(n).size +1;
    ND_tree_out(n).list.set_(ND_tree_out(n).size, null);
    n = aghead(f);
    ND_tree_in(n).list.set_(ND_tree_in(n).size, f);
    ND_tree_in(n).size = ND_tree_in(n).size +1;
    ND_tree_in(n).list.set_(ND_tree_in(n).size, null);
} finally {
LEAVING("9b7b78pmafynmvffztrqnlxtn","exchange_tree_edges");
}
}




//3 dbxco6m0mabzhsqfo3pb8nctk
// static void init_rank(void) 
@Unused
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="init_rank", key="dbxco6m0mabzhsqfo3pb8nctk", definition="static void init_rank(void)")
public static void init_rank() {
ENTERING("dbxco6m0mabzhsqfo3pb8nctk","init_rank");
try {
    int i, ctr;
    ST_nodequeue Q;
    ST_Agnode_s v;
    ST_Agedge_s e;
    Q = new_queue(Z.z().N_nodes);
    ctr = 0;
    for (v = GD_nlist(Z.z().G_ns); v!=null; v = ND_next(v)) {
	if (ND_priority(v) == 0)
	    enqueue(Q, v);
    }
    while ((v = dequeue(Q))!=null) {
	ND_rank(v, 0);
	ctr++;
	for (i = 0; (e = ND_in(v).list.get_(i))!=null; i++)
	    ND_rank(v, MAX(ND_rank(v), ND_rank(agtail(e)) + ED_minlen(e)));
	for (i = 0; (e = ND_out(v).list.get_(i))!=null; i++) {
	    ND_priority(aghead(e), ND_priority(aghead(e)) -1 );
	    if ((ND_priority(aghead(e))) <= 0)
		enqueue(Q, aghead(e));
	}
    }
    if (ctr != Z.z().N_nodes) {
UNSUPPORTED("7sgp99x1l3hzfks5wykxa87gf"); // 	agerr(AGERR, "trouble in init_rank\n");
UNSUPPORTED("bwwunxmw4kgz6qntbn6xp0cur"); // 	for (v = (((Agraphinfo_t*)(((Agobj_t*)(G))->data))->nlist); v; v = (((Agnodeinfo_t*)(((Agobj_t*)(v))->data))->next))
UNSUPPORTED("3dk132mz1u2pf0tla64kl6hv0"); // 	    if ((((Agnodeinfo_t*)(((Agobj_t*)(v))->data))->priority))
UNSUPPORTED("916bi45h6sjvte1rgig12b1v2"); // 		agerr(AGPREV, "\t%s %d\n", agnameof(v), (((Agnodeinfo_t*)(((Agobj_t*)(v))->data))->priority));
    }
    free_queue(Q);
} finally {
LEAVING("dbxco6m0mabzhsqfo3pb8nctk","init_rank");
}
}




//3 bj7ux5kz8ls2lnfh0ix6i00b9
// static node_t *incident(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="bj7ux5kz8ls2lnfh0ix6i00b9", definition="static node_t *incident(edge_t * e)")
public static ST_Agnode_s incident(ST_Agedge_s e) {
ENTERING("bj7ux5kz8ls2lnfh0ix6i00b9","incident");
try {
    if (ND_mark(agtail(e))!=0) {
	if (ND_mark(aghead(e)) == 0)
	    return agtail(e);
    } else {
	if (ND_mark(aghead(e))!=0)
	    return aghead(e);
    }
    return null;
} finally {
LEAVING("bj7ux5kz8ls2lnfh0ix6i00b9","incident");
}
}





@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="4i9tcvid2iql874c6k70s9aqm", definition="static edge_t *leave_edge(void)")
public static ST_Agedge_s leave_edge() {
ENTERING("4i9tcvid2iql874c6k70s9aqm","leave_edge");
try {
    ST_Agedge_s f, rv = null;
    int j, cnt = 0;
    
    j = Z.z().S_i;
    while (Z.z().S_i < Z.z().Tree_edge.size) {
	if (ED_cutvalue(f = (ST_Agedge_s) Z.z().Tree_edge.list.get_(Z.z().S_i)) < 0) {
	    if (rv!=null) {
		if (ED_cutvalue(rv) > ED_cutvalue(f))
		    rv = f;
	    } else
		rv = (ST_Agedge_s) Z.z().Tree_edge.list.get_(Z.z().S_i);
	    if (++cnt >= Z.z().Search_size)
		return rv;
	}
	Z.z().S_i++;
    }
    if (j > 0) {
	Z.z().S_i = 0;
	while (Z.z().S_i < j) {
	    if (ED_cutvalue(f = (ST_Agedge_s) Z.z().Tree_edge.list.get_(Z.z().S_i)) < 0) {
		if (rv!=null) {
		    if (ED_cutvalue(rv) > ED_cutvalue(f))
			rv = f;
		} else
		    rv = (ST_Agedge_s) Z.z().Tree_edge.list.get_(Z.z().S_i);
		if (++cnt >= Z.z().Search_size)
		    return rv;
	    }
	    Z.z().S_i++;
	}
    }
    return rv;
} finally {
LEAVING("4i9tcvid2iql874c6k70s9aqm","leave_edge");
}
}


//1 3wm7ej298st1xk7rbhbtnbk64
// static edge_t *Enter
//private static Agedge_s Enter;

//1 dx9f0e947f5kjhc2eftn43t90
// static int Low, Lim, Slack
//private static int Low, Lim, Slack;



//3 10lkpr4y40luvy2idlozfiva3
// static void dfs_enter_outedge(node_t * v) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_enter_outedge", key="10lkpr4y40luvy2idlozfiva3", definition="static void dfs_enter_outedge(node_t * v)")
public static void dfs_enter_outedge(ST_Agnode_s v) {
ENTERING("10lkpr4y40luvy2idlozfiva3","dfs_enter_outedge");
try {
    int i, slack;
    ST_Agedge_s e;
    for (i = 0; (e = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++) {
	if (TREE_EDGE(e) == false) {
	    if (N(SEQ(Z.z().Low, ND_lim(aghead(e)), Z.z().Lim))) {
		slack = SLACK(e);
		if ((slack < Z.z().Slack) || (Z.z().Enter == null)) {
		    Z.z().Enter = e;
		    Z.z().Slack = slack;
		}
	    }
	} else if (ND_lim(aghead(e)) < ND_lim(v))
	    dfs_enter_outedge(aghead(e));
    }
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null && (Z.z().Slack > 0); i++)
	if (ND_lim(agtail(e)) < ND_lim(v))
	    dfs_enter_outedge(agtail(e));
} finally {
LEAVING("10lkpr4y40luvy2idlozfiva3","dfs_enter_outedge");
}
}




//3 2z9nii6380p8qlql8nznzgvof
// static void dfs_enter_inedge(node_t * v) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_enter_inedge", key="2z9nii6380p8qlql8nznzgvof", definition="static void dfs_enter_inedge(node_t * v)")
public static void dfs_enter_inedge(ST_Agnode_s v) {
ENTERING("2z9nii6380p8qlql8nznzgvof","dfs_enter_inedge");
try {
    int i, slack;
    ST_Agedge_s e;
    for (i = 0; (e = (ST_Agedge_s) ND_in(v).list.get_(i))!=null; i++) {
	if (TREE_EDGE(e) == false) {
	    if (N(SEQ(Z.z().Low, ND_lim(agtail(e)), Z.z().Lim))) {
		slack = SLACK(e);
		if ((slack < Z.z().Slack) || (Z.z().Enter == null)) {
		    Z.z().Enter = e;
		    Z.z().Slack = slack;
		}
	    }
	} else if (ND_lim(agtail(e)) < ND_lim(v))
	    dfs_enter_inedge(agtail(e));
    }
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null && (Z.z().Slack > 0); i++)
	if (ND_lim(aghead(e)) < ND_lim(v))
	    dfs_enter_inedge(aghead(e));
} finally {
LEAVING("2z9nii6380p8qlql8nznzgvof","dfs_enter_inedge");
}
}




//3 aeu2po1o1rvibmafk0k8dw0fh
// static edge_t *enter_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="aeu2po1o1rvibmafk0k8dw0fh", definition="static edge_t *enter_edge(edge_t * e)")
public static ST_Agedge_s enter_edge(ST_Agedge_s e) {
ENTERING("aeu2po1o1rvibmafk0k8dw0fh","enter_edge");
try {
    ST_Agnode_s v;
    int outsearch;
    /* v is the down node */
    if (ND_lim(agtail(e)) < ND_lim(aghead(e))) {
	v = agtail(e);
	outsearch = 0;
    } else {
	v = aghead(e);
	outsearch = 1;
    }
    Z.z().Enter = null;
    Z.z().Slack = INT_MAX;
    Z.z().Low = ND_low(v);
    Z.z().Lim = ND_lim(v);
    if (outsearch!=0)
	dfs_enter_outedge(v);
    else
	dfs_enter_inedge(v);
    return Z.z().Enter;
} finally {
LEAVING("aeu2po1o1rvibmafk0k8dw0fh","enter_edge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="treesearch", key="1gvyafmercq92v3lg6gb33cbt", definition="static int treesearch(node_t * v)")
public static boolean treesearch(ST_Agnode_s v) {
ENTERING("1gvyafmercq92v3lg6gb33cbt","treesearch");
try {
    int i;
    ST_Agedge_s e;
    
    for (i = 0; (e = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++) {
	if ((ND_mark(aghead(e)) == 0) && (SLACK(e) == 0)) {
	    add_tree_edge(e);
	    if ((Z.z().Tree_edge.size == Z.z().N_nodes - 1) || treesearch(aghead(e)))
		return true;
	}
    }
    for (i = 0; (e = (ST_Agedge_s) ND_in(v).list.get_(i))!=null; i++) {
	if ((ND_mark(agtail(e)) == 0) && (SLACK(e) == 0)) {
	    add_tree_edge(e);
	    if ((Z.z().Tree_edge.size == Z.z().N_nodes - 1) || treesearch(agtail(e)))
		return true;
	}
    }
    return false;
} finally {
LEAVING("1gvyafmercq92v3lg6gb33cbt","treesearch");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="tight_tree", key="c98bj1u8j43cdezeczn33mec0", definition="static int tight_tree(void)")
public static int tight_tree() {
ENTERING("c98bj1u8j43cdezeczn33mec0","tight_tree");
try {
    int i;
    ST_Agnode_s n;
    
    for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n)) {
	ND_mark(n, 0);
	ND_tree_in(n).list.set_(0, null);
	ND_tree_out(n).list.set_(0, null);
	ND_tree_in(n).size = ND_tree_out(n).size = 0;
    }
    for (i = 0; i < Z.z().Tree_edge.size; i++)
	ED_tree_index(Z.z().Tree_edge.list.get_(i), -1);
    
    Z.z().Tree_node.size = Z.z().Tree_edge.size = 0;
    for (n = GD_nlist(Z.z().G_ns); n!=null && (Z.z().Tree_edge.size == 0); n = ND_next(n))
	treesearch(n);
    return Z.z().Tree_node.size;
} finally {
LEAVING("c98bj1u8j43cdezeczn33mec0","tight_tree");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="init_cutvalues", key="10o7oe8d097fx7swmpqd4tf0h", definition="static void init_cutvalues(void)")
public static void init_cutvalues() {
ENTERING("10o7oe8d097fx7swmpqd4tf0h","init_cutvalues");
try {
    dfs_range(GD_nlist(Z.z().G_ns), null, 1);
    dfs_cutval(GD_nlist(Z.z().G_ns), null);
} finally {
LEAVING("10o7oe8d097fx7swmpqd4tf0h","init_cutvalues");
}
}




@Reviewed(when = "14/11/2020")
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="feasible_tree", key="756raqohoxdeiddqbyr37h7ig", definition="static int feasible_tree(void)")
public static int feasible_tree() {
ENTERING("756raqohoxdeiddqbyr37h7ig","feasible_tree");
try {
    int i, delta;
    ST_Agnode_s n;
    ST_Agedge_s e, f;
    
    if (Z.z().N_nodes <= 1)
	return 0;
    while (tight_tree() < Z.z().N_nodes) {
	e = null;
	for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n)) {
	    for (i = 0; (f = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++) {
		if ((TREE_EDGE(f) == false) && incident(f)!=null && ((e == null)
							       || (SLACK(f)
								   <
								   SLACK
								   (e))))
		    e = f;
	    }
	}
	
	
	if (e!=null) {
	    delta = SLACK(e);
	    if (delta!=0) {
		if (EQ(incident(e), aghead(e)))
		    delta = -delta;
		for (i = 0; i < Z.z().Tree_node.size; i++)
		    ND_rank(Z.z().Tree_node.list.get_(i), ND_rank(Z.z().Tree_node.list.get_(i)) + delta);
	    }
	} else {
	    return 1;
	}
    }
    init_cutvalues();
    return 0;
} finally {
LEAVING("756raqohoxdeiddqbyr37h7ig","feasible_tree");
}
}




//3 49un8m43odrf89cedvin3wz3r
// static node_t *treeupdate(node_t * v, node_t * w, int cutvalue, int dir) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="", key="49un8m43odrf89cedvin3wz3r", definition="static node_t *treeupdate(node_t * v, node_t * w, int cutvalue, int dir)")
public static ST_Agnode_s treeupdate(ST_Agnode_s v, ST_Agnode_s w, int cutvalue, boolean dir) {
ENTERING("49un8m43odrf89cedvin3wz3r","treeupdate");
try {
    ST_Agedge_s e;
    boolean d;
    while (N(SEQ(ND_low(v), ND_lim(w), ND_lim(v)))) {
	e = (ST_Agedge_s) ND_par(v);
	if (EQ(v, agtail(e)))
	    d = dir;
	else
	    d = NOT(dir);
	if (d)
	    ED_cutvalue(e, ED_cutvalue(e) + cutvalue);
	else
	    ED_cutvalue(e, ED_cutvalue(e) - cutvalue);
	if (ND_lim(agtail(e)) > ND_lim(aghead(e)))
	    v = agtail(e);
	else
	    v = aghead(e);
    }
    return v;
} finally {
LEAVING("49un8m43odrf89cedvin3wz3r","treeupdate");
}
}




//3 e66n8gern1fejjsn8nefypo0g
// static void rerank(node_t * v, int delta) 
@Unused
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="rerank", key="e66n8gern1fejjsn8nefypo0g", definition="static void rerank(node_t * v, int delta)")
public static void rerank(ST_Agnode_s v, int delta) {
ENTERING("e66n8gern1fejjsn8nefypo0g","rerank");
try {
    int i;
    ST_Agedge_s e;
    ND_rank(v, ND_rank(v) - delta);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null; i++)
	if (NEQ(e, ND_par(v)))
	    rerank(aghead(e), delta);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null; i++)
	if (NEQ(e, ND_par(v)))
	    rerank(agtail(e), delta);
} finally {
LEAVING("e66n8gern1fejjsn8nefypo0g","rerank");
}
}




//3 xww1p8bentf1qk7mgfhi1q6m
// static void  update(edge_t * e, edge_t * f) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="update", key="xww1p8bentf1qk7mgfhi1q6m", definition="static void  update(edge_t * e, edge_t * f)")
public static void update(ST_Agedge_s e, ST_Agedge_s f) {
ENTERING("xww1p8bentf1qk7mgfhi1q6m","update");
try {
    int cutvalue, delta;
    ST_Agnode_s lca;
    delta = SLACK(f);
    /* "for (v = in nodes in tail side of e) do ND_rank(v) -= delta;" */
    if (delta > 0) {
	int s;
	s = ND_tree_in(agtail(e)).size + ND_tree_out(agtail(e)).size;
	if (s == 1)
	    rerank(agtail(e), delta);
	else {
	    s = ND_tree_in(aghead(e)).size + ND_tree_out(aghead(e)).size;
	    if (s == 1)
		rerank(aghead(e), -delta);
	    else {
		if (ND_lim(agtail(e)) < ND_lim(aghead(e)))
		    rerank(agtail(e), delta);
		else
		    rerank(aghead(e), -delta);
	    }
	}
    }
    cutvalue = ED_cutvalue(e);
    lca = treeupdate(agtail(f), aghead(f), cutvalue, true);
    if (NEQ(treeupdate(aghead(f), agtail(f), cutvalue, false), lca)) {
UNSUPPORTED("f2l4c6yhnwnfer3vrasf55fio"); // 	agerr(AGERR, "update: mismatched lca in treeupdates\n");
UNSUPPORTED("6fzmgjpkhmnx0a2cnt0q0rceg"); // 	longjmp (jbuf, 1);
    }
    ED_cutvalue(f, -cutvalue);
    ED_cutvalue(e, 0);
    exchange_tree_edges(e, f);
    dfs_range(lca, ND_par(lca), ND_low(lca));
} finally {
LEAVING("xww1p8bentf1qk7mgfhi1q6m","update");
}
}




@Reviewed(when = "14/11/2020")
@HasND_Rank
@Original(version="2.38.0", path="lib/common/ns.c", name="scan_and_normalize", key="3yw7w42hz7af67d6qse3b2172", definition="static void scan_and_normalize(void)")
public static void scan_and_normalize() {
ENTERING("3yw7w42hz7af67d6qse3b2172","scan_and_normalize");
try {
    ST_Agnode_s n;
    
    Z.z().Minrank = Integer.MAX_VALUE;
    Z.z().Maxrank = -Integer.MAX_VALUE;
    for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n)) {
	if (ND_node_type(n) == NORMAL) {
	    Z.z().Minrank = MIN(Z.z().Minrank, ND_rank(n));
	    Z.z().Maxrank = MAX(Z.z().Maxrank, ND_rank(n));
	}
    }
    if (Z.z().Minrank != 0) {
	for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n))
	    ND_rank(n, ND_rank(n) - Z.z().Minrank);
	Z.z().Maxrank -= Z.z().Minrank;
	Z.z().Minrank = 0;
    }
} finally {
LEAVING("3yw7w42hz7af67d6qse3b2172","scan_and_normalize");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="freeTreeList", key="7eg6zti36nbg4tqyo8yunh86r", definition="static void freeTreeList (graph_t* g)")
public static void freeTreeList(ST_Agraph_s g) {
ENTERING("7eg6zti36nbg4tqyo8yunh86r","freeTreeList");
try {
    ST_Agnode_s n;
    for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n)) {
	free_list(ND_tree_in(n));
	free_list(ND_tree_out(n));
	ND_mark(n, 0);
    }
} finally {
LEAVING("7eg6zti36nbg4tqyo8yunh86r","freeTreeList");
}
}




//3 9gx8p7md3v3mzp640xdjj814a
// static void LR_balance(void) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="LR_balance", key="9gx8p7md3v3mzp640xdjj814a", definition="static void LR_balance(void)")
public static void LR_balance() {
ENTERING("9gx8p7md3v3mzp640xdjj814a","LR_balance");
try {
    int i, delta;
    ST_Agedge_s e, f;
    for (i = 0; i < Z.z().Tree_edge.size; i++) {
	e = (ST_Agedge_s) Z.z().Tree_edge.list.get_(i);
	if (ED_cutvalue(e) == 0) {
	    f = enter_edge(e);
	    if (f == null)
		continue;
	    delta = SLACK(f);
	    if (delta <= 1)
		continue;
	    if (ND_lim(agtail(e)) < ND_lim(aghead(e)))
		rerank(agtail(e), delta / 2);
	    else
		rerank(aghead(e), -delta / 2);
	}
    }
    freeTreeList (Z.z().G_ns);
} finally {
LEAVING("9gx8p7md3v3mzp640xdjj814a","LR_balance");
}
}



@Difficult
@HasND_Rank
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="TB_balance", key="5c01jnao2ubmy4l0vi5jol0jz", definition="static void TB_balance(void)")
public static void TB_balance() {
ENTERING("5c01jnao2ubmy4l0vi5jol0jz","TB_balance");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    int i, low, high, choice;
    int[] nrank;
    int inweight, outweight;
    
    scan_and_normalize();
    
    /* find nodes that are not tight and move to less populated ranks */
    nrank = new int[Z.z().Maxrank + 1];
    for (i = 0; i <= Z.z().Maxrank; i++)
    nrank[i] = 0;
    for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n))
	if (ND_node_type(n) == NORMAL)
		nrank[ND_rank(n)]++;
    for (n = GD_nlist(Z.z().G_ns); n!=null; n = ND_next(n)) {
	if (ND_node_type(n) != NORMAL)
	    continue;
	inweight = outweight = 0;
	low = 0;
	high = Z.z().Maxrank;
	for (i = 0; (e = (ST_Agedge_s) ND_in(n).list.get_(i))!=null; i++) {
	    inweight += ED_weight(e);
	    low = MAX(low, ND_rank(agtail(e)) + ED_minlen(e));
	}
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++) {
	    outweight += ED_weight(e);
	    high = MIN(high, ND_rank(aghead(e)) - ED_minlen(e));
	}
	if (low < 0)
	    low = 0;		/* vnodes can have ranks < 0 */
	if (inweight == outweight) {
	    choice = low;
	    for (i = low + 1; i <= high; i++)
		if (nrank[i] < nrank[choice])
		    choice = i;
	    nrank[ND_rank(n)]--;
	    nrank[choice]++;
	    ND_rank(n, choice);
	}
	free_list(ND_tree_in(n));
	free_list(ND_tree_out(n));
	ND_mark(n, 0);
    }
    Memory.free(nrank);
} finally {
LEAVING("5c01jnao2ubmy4l0vi5jol0jz","TB_balance");
}
}







@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/common/ns.c", name="init_graph", key="37hg5w7ywmyljdiebgp5ltl22", definition="static int init_graph(graph_t * g)")
public static int init_graph(ST_Agraph_s g) {
ENTERING("37hg5w7ywmyljdiebgp5ltl22","init_graph");
try {
    int i, feasible;
    ST_Agnode_s n;
    ST_Agedge_s e;
    
    
    Z.z().G_ns = g;
    Z.z().N_nodes = Z.z().N_edges = Z.z().S_i = 0;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	ND_mark(n, 0);
	Z.z().N_nodes++;
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++)
	    Z.z().N_edges++;
    }
    
    
    Z.z().Tree_node.list = CStarStar.<ST_Agnode_s>REALLOC(Z.z().N_nodes, Z.z().Tree_node.list, ST_Agnode_s.class); 
    Z.z().Tree_node.size =  0;
    Z.z().Tree_edge.list = CStarStar.<ST_Agedge_s>REALLOC(Z.z().N_nodes, Z.z().Tree_edge.list, ST_Agedge_s.class);
    Z.z().Tree_edge.size = 0;
    
    
    feasible = 1;
    for (n = GD_nlist(g); n!=null; n = ND_next(n)) {
	ND_priority(n, 0);
	for (i = 0; (e = (ST_Agedge_s) ND_in(n).list.get_(i))!=null; i++) {
	    ND_priority(n, ND_priority(n)+1);
	    ED_cutvalue(e, 0);
	    ED_tree_index(e, -1);
	    if (feasible!=0
		&& (ND_rank(aghead(e)) - ND_rank(agtail(e)) < ED_minlen(e)))
		feasible = 0;
	}
	ND_tree_in(n).list = CStarStar.<ST_Agedge_s> ALLOC(i + 1, ST_Agedge_s.class);
	ND_tree_in(n).size = 0;
	for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++);
	ND_tree_out(n).list = CStarStar.<ST_Agedge_s> ALLOC(i + 1, ST_Agedge_s.class);
	ND_tree_out(n).size = 0;
    }
    return feasible;
} finally {
LEAVING("37hg5w7ywmyljdiebgp5ltl22","init_graph");
}
}




//3 4jv545ixndzfz0hmy2ck1kvre
// static void graphSize (graph_t * g, int* nn, int* ne) 
@Unused
@Original(version="2.38.0", path="lib/common/ns.c", name="graphSize", key="4jv545ixndzfz0hmy2ck1kvre", definition="static void graphSize (graph_t * g, int* nn, int* ne)")
public static Object graphSize(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("d95292hibn08mh6rbguy3zeib"); // graphSize (graph_t * g, int* nn, int* ne)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4i46dhl8eqbh29vq1vn3ipo70"); //     int i, nnodes, nedges;
UNSUPPORTED("cjx5v6hayed3q8eeub1cggqca"); //     node_t *n;
UNSUPPORTED("5gypxs09iuryx5a2eho9lgdcp"); //     edge_t *e;
UNSUPPORTED("359m8vuzzte7zl5lru7vf4bga"); //     nnodes = nedges = 0;
UNSUPPORTED("8g62mxpap4eeua2lkn9a1iosi"); //     for (n = GD_nlist(g); n; n = ND_next(n)) {
UNSUPPORTED("a5hcy2vbfyq09svspfdvsreu1"); // 	nnodes++;
UNSUPPORTED("az80xiplaqv1i8gmq2feyyrea"); // 	for (i = 0; (e = ND_out(n).list[i]); i++) {
UNSUPPORTED("1sy3rnb8qjw3nmyka290izgi2"); // 	    nedges++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("h7rj9k6bm1iatjqeqmhlinoi"); //     *nn = nnodes;
UNSUPPORTED("7whkfg1zlimqk163vbj7cgxmt"); //     *ne = nedges;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}



/* rank:
 * Apply network simplex to rank the nodes in a graph.
 * Uses ED_minlen as the internode constraint: if a->b with minlen=ml,
 * rank b - rank a >= ml.
 * Assumes the graph has the following additional structure:
 *   A list of all nodes, starting at GD_nlist, and linked using ND_next.
 *   Out and in edges lists stored in ND_out and ND_in, even if the node
 *  doesn't have any out or in edges.
 * The node rank values are stored in ND_rank.
 * Returns 0 if successful; returns 1 if `he graph was not connected;
 * returns 2 if something seriously wrong;
 */
@Difficult
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="rank2", key="5n0ipwzhr8urlx0fsdzr02gwq", definition="int rank2(graph_t * g, int balance, int maxiter, int search_size)")
public static int rank2(ST_Agraph_s g, int balance, int maxiter, int search_size) {
ENTERING("5n0ipwzhr8urlx0fsdzr02gwq","rank2");
try {
    int iter = 0, feasible;
    CString ns = new CString("network simplex: ");
    ST_Agedge_s e, f;
    
    
    /*if (Verbose) {
	int nn, ne;
	graphSize (g, &nn, &ne);
	fprintf(stderr, "%s %d nodes %d edges maxiter=%d balance=%d\n", ns,
	    nn, ne, maxiter, balance);
	start_timer();
    }*/
    feasible = init_graph(g);
    if (N(feasible))
	init_rank();
    if (maxiter <= 0) {
	freeTreeList (g);
	return 0;
    }
    
    
    if (search_size >= 0)
	Z.z().Search_size = search_size;
    else
	Z.z().Search_size = SEARCHSIZE;
    
    
    if (setjmp (jbuf)!=0) {
	return 2;
    }
    
    
    if (feasible_tree()!=0) {
	freeTreeList (g);
	return 1;
    }
    while ((e = leave_edge())!=null) {
	f = enter_edge(e);
	update(e, f);
	iter++;
	/*if (Verbose && (iter % 100 == 0)) {
	    if (iter % 1000 == 100)
		fputs(ns, stderr);
	    fprintf(stderr, "%d ", iter);
	    if (iter % 1000 == 0)
		fputc('\n', stderr);
	}*/
	if (iter >= maxiter)
	    break;
    }
    switch (balance) {
    case 1:
	TB_balance();
	break;
    case 2:
	LR_balance();
	break;
    default:
	scan_and_normalize();
	freeTreeList (Z.z().G_ns);
	break;
    }
    /*if (Verbose) {
	if (iter >= 100)
	    fputc('\n', stderr);
	fprintf(stderr, "%s%d nodes %d edges %d iter %.2f sec\n",
		ns, N_nodes, N_edges, iter, elapsed_sec());
    }*/
    return 0;
} finally {
LEAVING("5n0ipwzhr8urlx0fsdzr02gwq","rank2");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="rank", key="aqly8eniwjr5bmh4hzwc7ftdr", definition="int rank(graph_t * g, int balance, int maxiter)")
public static int rank(ST_Agraph_s g, int balance, int maxiter) {
ENTERING("aqly8eniwjr5bmh4hzwc7ftdr","rank");
try {
    CString s;
    int search_size;
    
    if ((s = agget(g, new CString("searchsize")))!=null)
	search_size = atoi(s);
    else
	search_size = SEARCHSIZE;
    
    return rank2 (g, balance, maxiter, search_size);
} finally {
LEAVING("aqly8eniwjr5bmh4hzwc7ftdr","rank");
}
}




/* set cut value of f, assuming values of edges on one side were already set */
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="x_cutval", key="2q59mz8qtn0biifbezb8uxz17", definition="static void x_cutval(edge_t * f)")
public static void x_cutval(ST_Agedge_s f) {
ENTERING("2q59mz8qtn0biifbezb8uxz17","x_cutval");
try {
    ST_Agnode_s v=null;
    ST_Agedge_s e;
    int i, sum, dir=0;
    
    /* set v to the node on the side of the edge already searched */
    if (EQ(ND_par(agtail(f)), f)) {
	v = agtail(f);
	dir = 1;
    } else {
	v = aghead(f);
	dir = -1;
    }
    
    sum = 0;
    for (i = 0; (e = (ST_Agedge_s) ND_out(v).list.get_(i))!=null; i++)
	sum += x_val(e, v, dir);
    for (i = 0; (e = (ST_Agedge_s) ND_in(v).list.get_(i))!=null; i++)
	sum += x_val(e, v, dir);
    ED_cutvalue(f, sum);
} finally {
LEAVING("2q59mz8qtn0biifbezb8uxz17","x_cutval");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="x_val", key="bfeafmsmmnblgizs37qj03dy4", definition="static int x_val(edge_t * e, node_t * v, int dir)")
public static int x_val(ST_Agedge_s e, ST_Agnode_s v, int dir) {
ENTERING("bfeafmsmmnblgizs37qj03dy4","x_val");
try {
    ST_Agnode_s other;
    int d=0, rv=0, f=0;
    
    if (EQ(agtail(e), v))
	other = aghead(e);
    else
	other = agtail(e);
    if (N(SEQ(ND_low(v), ND_lim(other), ND_lim(v)))) {
	f = 1;
	rv = ED_weight(e);
    } else {
	f = 0;
	if (TREE_EDGE(e))
	    rv = ED_cutvalue(e);
	else
	    rv = 0;
	rv -= ED_weight(e);
    }
    if (dir > 0) {
	if (EQ(aghead(e), v))
	    d = 1;
	else
	    d = -1;
    } else {
	if (EQ(agtail(e), v))
	    d = 1;
	else
	    d = -1;
    }
    if (f!=0)
	d = -d;
    if (d < 0)
	rv = -rv;
    return rv;
} finally {
LEAVING("bfeafmsmmnblgizs37qj03dy4","x_val");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_cutval", key="ah65iqmwa5j0qwotm6amhijlg", definition="static void dfs_cutval(node_t * v, edge_t * par)")
public static void dfs_cutval(ST_Agnode_s v, ST_Agedge_s par) {
ENTERING("ah65iqmwa5j0qwotm6amhijlg","dfs_cutval");
try {
    int i;
    ST_Agedge_s e;
    
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null; i++)
	if (NEQ(e, par))
	    dfs_cutval(aghead(e), e);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null; i++)
	if (NEQ(e, par))
	    dfs_cutval(agtail(e), e);
    if (par!=null)
	x_cutval(par);
} finally {
LEAVING("ah65iqmwa5j0qwotm6amhijlg","dfs_cutval");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/ns.c", name="dfs_range", key="cgqr48qol9p8bsqjnryo5z5x9", definition="static int dfs_range(node_t * v, edge_t * par, int low)")
public static int dfs_range(ST_Agnode_s v, ST_Agedge_s par, int low) {
ENTERING("cgqr48qol9p8bsqjnryo5z5x9","dfs_range");
try {
    ST_Agedge_s e;
    int i, lim;
    
    lim = low;
    ND_par(v, par);
    ND_low(v, low);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_out(v).list.get_(i))!=null; i++)
	if (NEQ(e, par))
	    lim = dfs_range(aghead(e), e, lim);
    for (i = 0; (e = (ST_Agedge_s) ND_tree_in(v).list.get_(i))!=null; i++)
	if (NEQ(e, par))
	    lim = dfs_range(agtail(e), e, lim);
    ND_lim(v, lim);
    return lim + 1;
} finally {
LEAVING("cgqr48qol9p8bsqjnryo5z5x9","dfs_range");
}
}


}
