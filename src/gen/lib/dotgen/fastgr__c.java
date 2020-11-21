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
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.dotgen.dotinit__c.dot_root;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ED_count;
import static smetana.core.Macro.ED_edge_type;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.ED_xpenalty;
import static smetana.core.Macro.GD_has_flat_edges;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_other;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_prev;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.UNSURE_ABOUT;
import static smetana.core.Macro.VIRTUAL;
import static smetana.core.Macro.aghead;
import static smetana.core.Macro.agtail;
import static smetana.core.Macro.alloc_elist;
import static smetana.core.Macro.elist_append;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Todo;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agedgepair_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_Agrec_s;
import h.ST_elist;
import h.ST_pointf;

public class fastgr__c {

//3 ciez0pfggxdljedzsbklq49f0
// static inline point pointof(int x, int y) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="pointof", key="ciez0pfggxdljedzsbklq49f0", definition="static inline point pointof(int x, int y)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="boxof", key="7cufnfitrh935ew093mw0i4b7", definition="static inline box boxof(int llx, int lly, int urx, int ury)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="add_point", key="1n5xl70wxuabyf97mclvilsm6", definition="static inline point add_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="sub_point", key="ai2dprak5y6obdsflguh5qbd7", definition="static inline point sub_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="sub_pointf", key="16f6pyogcv3j7n2p0n8giqqgh", definition="static inline pointf sub_pointf(pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="mid_point", key="9k50jgrhc4f9824vf8ony74rw", definition="static inline point mid_point(point p, point q)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="mid_pointf", key="59c4f7im0ftyowhnzzq2v9o1x", definition="static inline pointf mid_pointf(pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="interpolate_pointf", key="5r18p38gisvcx3zsvbb9saixx", definition="static inline pointf interpolate_pointf(double t, pointf p, pointf q)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="exch_xy", key="bxzrv2ghq04qk5cbyy68s4mol", definition="static inline point exch_xy(point p)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="exch_xyf", key="9lt3e03tac6h6sydljrcws8fd", definition="static inline pointf exch_xyf(pointf p)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="box_bb", key="8l9qhieokthntzdorlu5zn29b", definition="static inline box box_bb(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="boxf_bb", key="clws9h3bbjm0lw3hexf8nl4c4", definition="static inline boxf boxf_bb(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="box_intersect", key="bit6ycxo1iqd2al92y8gkzlvb", definition="static inline box box_intersect(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="boxf_intersect", key="8gfybie7k6pgb3o1a6llgpwng", definition="static inline boxf boxf_intersect(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="box_overlap", key="7z8j2quq65govaaejrz7b4cvb", definition="static inline int box_overlap(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="boxf_overlap", key="4z0suuut2acsay5m8mg9dqjdu", definition="static inline int boxf_overlap(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="box_contains", key="dd34swz5rmdgu3a2np2a4h1dy", definition="static inline int box_contains(box b0, box b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="boxf_contains", key="8laj1bspbu2i1cjd9upr7xt32", definition="static inline int boxf_contains(boxf b0, boxf b1)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="perp", key="4wf5swkz24xx51ja2dynbycu1", definition="static inline pointf perp (pointf p)")
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
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="scale", key="6dtlpzv4mvgzb9o0b252yweuv", definition="static inline pointf scale (double c, pointf p)")
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

/*
 * operations on the fast internal graph.
 */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="econbrl314rr46qnvvw5e32j7", definition="static edge_t *ffe(node_t * u, elist uL, node_t * v, elist vL)")
public static ST_Agedge_s ffe(ST_Agnode_s u, final ST_elist uL, ST_Agnode_s v, final ST_elist vL) {
// WARNING!! STRUCT
return ffe_w_(u, uL.copy(), v, vL.copy());
}
private static ST_Agedge_s ffe_w_(ST_Agnode_s u, final ST_elist uL, ST_Agnode_s v, final ST_elist vL) {
ENTERING("econbrl314rr46qnvvw5e32j7","ffe");
try {
    int i;
    ST_Agedge_s e = null;
    
    if ((uL.size > 0) && (vL.size > 0)) {
	if (uL.size < vL.size) {
	    for (i = 0; (e = (ST_Agedge_s) uL.list.get_(i))!=null; i++)
		if (EQ(aghead(e), v))
		    break;
	} else {
	    for (i = 0; (e = (ST_Agedge_s) vL.list.get_(i))!=null; i++)
		if (EQ(agtail(e), u))
		    break;
	}
    } else
	e = null;
    return e;
} finally {
LEAVING("econbrl314rr46qnvvw5e32j7","ffe");
}
}



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="1uygfrgur73lfy9vsjozwwupm", definition="edge_t *find_fast_edge(node_t * u, node_t * v)")
public static ST_Agedge_s find_fast_edge(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("1uygfrgur73lfy9vsjozwwupm","find_fast_edge");
try {
    return ffe(u, ND_out(u), v, ND_in(v));
} finally {
LEAVING("1uygfrgur73lfy9vsjozwwupm","find_fast_edge");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="find_fast_node", key="1yw7ahdnxnexnicj552zqyyej", definition="static node_t* find_fast_node(graph_t * g, node_t * n)")
public static ST_Agnode_s find_fast_node(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("1yw7ahdnxnexnicj552zqyyej","find_fast_node");
try {
    ST_Agnode_s v;
    for (v = GD_nlist(g); v!=null; v = ND_next(v))
	if (EQ(v, n))
	    break;
    return v;
} finally {
LEAVING("1yw7ahdnxnexnicj552zqyyej","find_fast_node");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="bf1j97keudu416avridkj9fpb", definition="edge_t *find_flat_edge(node_t * u, node_t * v)")
public static ST_Agedge_s find_flat_edge(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("bf1j97keudu416avridkj9fpb","find_flat_edge");
try {
    return ffe(u, ND_flat_out(u), v, ND_flat_in(v));
} finally {
LEAVING("bf1j97keudu416avridkj9fpb","find_flat_edge");
}
}



@Todo(what = "Strange that elist_append(e, (*L)) is never called")
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="safe_list_append", key="cttswsffgmw1g710jzvdd3wzn", definition="static void  safe_list_append(edge_t * e, elist * L)")
public static void safe_list_append(ST_Agedge_s e, ST_elist L) {
ENTERING("cttswsffgmw1g710jzvdd3wzn","safe_list_append");
try {
     int i;
     
     for (i = 0; i < L.size; i++)
     if (EQ(e, L.list.get_(i)))
 	 	return;
     UNSUPPORTED("cslejjtgepjdwlcykfas4fmvz"); //     elist_append(e, (*L));
} finally {
LEAVING("cttswsffgmw1g710jzvdd3wzn","safe_list_append");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="8t6gpubo908pz1pqnt1s88lnt", definition="edge_t *fast_edge(edge_t * e)")
public static ST_Agedge_s fast_edge(ST_Agedge_s e) {
ENTERING("8t6gpubo908pz1pqnt1s88lnt","fast_edge");
try {
    elist_append(e, ND_out(agtail(e)));
    elist_append(e, ND_in(aghead(e)));
    return e;
} finally {
LEAVING("8t6gpubo908pz1pqnt1s88lnt","fast_edge");
}
}




//3 dxb0q8ajb7iv25aj6zdqnbwh5
// void zapinlist(elist * L, edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="zapinlist", key="dxb0q8ajb7iv25aj6zdqnbwh5", definition="void zapinlist(elist * L, edge_t * e)")
public static void zapinlist(ST_elist L, ST_Agedge_s e) {
ENTERING("dxb0q8ajb7iv25aj6zdqnbwh5","zapinlist");
try {
    int i;
    for (i = 0; i < L.size; i++) {
	if (EQ(L.list.get_(i), e)) {
	    L.size = L.size-1;
	    L.list.set_(i, L.list.get_(L.size));
	    L.list.set_(L.size, null);
	    break;
	}
    }
} finally {
LEAVING("dxb0q8ajb7iv25aj6zdqnbwh5","zapinlist");
}
}




//3 dkv97rr4ytpehp291etaxe9gc
// void delete_fast_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="delete_fast_edge", key="dkv97rr4ytpehp291etaxe9gc", definition="void delete_fast_edge(edge_t * e)")
public static void delete_fast_edge(ST_Agedge_s e) {
ENTERING("dkv97rr4ytpehp291etaxe9gc","delete_fast_edge");
try {
    //assert(e != NULL);
    zapinlist((ND_out(agtail(e))), e);
    zapinlist((ND_in(aghead(e))), e);
} finally {
LEAVING("dkv97rr4ytpehp291etaxe9gc","delete_fast_edge");
}
}




//3 b8a9hlxts1y43x7r4f31vwee6
// static void  safe_delete_fast_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="safe_delete_fast_edge", key="b8a9hlxts1y43x7r4f31vwee6", definition="static void  safe_delete_fast_edge(edge_t * e)")
public static Object safe_delete_fast_edge(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("bw8hdfe3bql5qxhdyxjh12iaf"); // safe_delete_fast_edge(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6ag74vz3kjmohe4bp89fktey4"); //     edge_t *f;
UNSUPPORTED("axei6r4pdvrumkaqc9p82yzjh"); //     assert(e != NULL);
UNSUPPORTED("f10apexer8zzjd53hdc3kc6l5"); //     for (i = 0; (f = ND_out(agtail(e)).list[i]); i++)
UNSUPPORTED("dibajtvzac9fny0wd51x9jto8"); // 	if (f == e)
UNSUPPORTED("8jj8ar5kwgfuzsk5nd6eyfcmc"); // 	    zapinlist(&(ND_out(agtail(e))), e);
UNSUPPORTED("7h2ym5qzjdgt4gg6uscho1z1e"); //     for (i = 0; (f = ND_in(aghead(e)).list[i]); i++)
UNSUPPORTED("dibajtvzac9fny0wd51x9jto8"); // 	if (f == e)
UNSUPPORTED("dyemtglp4tpava0oysdm66am8"); // 	    zapinlist(&(ND_in(aghead(e))), e);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="other_edge", key="73oebfcfiescklohgt8mddswc", definition="void other_edge(edge_t * e)")
public static void other_edge(ST_Agedge_s e) {
ENTERING("73oebfcfiescklohgt8mddswc","other_edge");
try {
    elist_append(e, ND_other(agtail(e)));
} finally {
LEAVING("73oebfcfiescklohgt8mddswc","other_edge");
}
}




@Difficult
@Reviewed(when = "15/11/2020")
@Todo(what = "Review &(ND_other(agtail(e))")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="safe_other_edge", key="4zg1fp1b7bhnx2tbeaij8yeel", definition="void safe_other_edge(edge_t * e)")
public static void safe_other_edge(ST_Agedge_s e) {
ENTERING("4zg1fp1b7bhnx2tbeaij8yeel","safe_other_edge");
try {
	UNSURE_ABOUT("safe_list_append(e, &(ND_other(agtail(e))));");
	// Review &(ND_other(agtail(e))
    safe_list_append(e, ND_other(agtail(e)));	
} finally {
LEAVING("4zg1fp1b7bhnx2tbeaij8yeel","safe_other_edge");
}
}



/* new_virtual_edge:
 * Create and return a new virtual edge e attached to orig.
 * ED_to_orig(e) = orig
 * ED_to_virt(orig) = e if e is the first virtual edge attached.
 * orig might be an input edge, reverse of an input edge, or virtual edge
 */
@Reviewed(when = "14/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="4gd9tmpq70q0rij5otj0k6sn2", definition="edge_t *new_virtual_edge(node_t * u, node_t * v, edge_t * orig)")
public static ST_Agedge_s new_virtual_edge(ST_Agnode_s u, ST_Agnode_s v, ST_Agedge_s orig) {
ENTERING("4gd9tmpq70q0rij5otj0k6sn2","new_virtual_edge");
try {
    ST_Agedge_s e;
    
    ST_Agedgepair_s e2 = new ST_Agedgepair_s();
    AGTYPE(e2.in, AGINEDGE);
    AGTYPE(e2.out, AGOUTEDGE);
    e2.out.base.data = (ST_Agrec_s) new ST_Agedgeinfo_t().castTo(ST_Agrec_s.class);
    e = e2.out;
    agtail(e, u);
    aghead(e, v);
    ED_edge_type(e, VIRTUAL);
    
    
    if (orig!=null) {
	AGSEQ(e, AGSEQ(orig));
	AGSEQ(e2.in, AGSEQ(orig));
	ED_count(e, ED_count(orig));
	ED_xpenalty(e, ED_xpenalty(orig));
	ED_weight(e, ED_weight(orig));
	ED_minlen(e, ED_minlen(orig));
	if (EQ(agtail(e), agtail(orig)))
	    ED_tail_port(e, ED_tail_port(orig));
	else if (EQ(agtail(e), aghead(orig)))
	    ED_tail_port(e, ED_head_port(orig));
	if (EQ(aghead(e), aghead(orig)))
	    ED_head_port(e, ED_head_port(orig));
	else if (EQ(aghead(e), agtail(orig)))
	    ED_head_port(e, ED_tail_port(orig));
	
	
	if (ED_to_virt(orig) == null)
	    ED_to_virt(orig, e);
	ED_to_orig(e, orig);
    } else {
	ED_minlen(e, 1);
	ED_count(e, 1);
	ED_xpenalty(e, 1);
	ED_weight(e, 1);
	}
    return e;
} finally {
LEAVING("4gd9tmpq70q0rij5otj0k6sn2","new_virtual_edge");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="9obdfflzw4cs2z9r0dng26mvw", definition="edge_t *virtual_edge(node_t * u, node_t * v, edge_t * orig)")
public static ST_Agedge_s virtual_edge(ST_Agnode_s u, ST_Agnode_s v, ST_Agedge_s orig) {
ENTERING("9obdfflzw4cs2z9r0dng26mvw","virtual_edge");
try {
    return fast_edge(new_virtual_edge(u, v, orig));
} finally {
LEAVING("9obdfflzw4cs2z9r0dng26mvw","virtual_edge");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="fast_node", key="98hkec8t6fjk10bjpstumw0ey", definition="void fast_node(graph_t * g, Agnode_t * n)")
public static void fast_node(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("98hkec8t6fjk10bjpstumw0ey","fast_node");
try {
    ND_next(n, GD_nlist(g));
    if (ND_next(n)!=null)
	ND_prev(ND_next(n), n);
    GD_nlist(g, n);
    ND_prev(n, null);
    assert(NEQ(n, ND_next(n)));
} finally {
LEAVING("98hkec8t6fjk10bjpstumw0ey","fast_node");
}
}




//3 66jdzhjfa6kx3ntfyl5t7cehm
// void fast_nodeapp(node_t * u, node_t * v) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="fast_nodeapp", key="66jdzhjfa6kx3ntfyl5t7cehm", definition="void fast_nodeapp(node_t * u, node_t * v)")
public static Object fast_nodeapp(Object... arg) {
UNSUPPORTED("24rf80znlmwn6xx6m03vqyykr"); // void fast_nodeapp(node_t * u, node_t * v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8g450a5aruflexr2yn977dnjz"); //     assert(u != v);
UNSUPPORTED("dw67xh0kd304grgqkgiffmejh"); //     assert(ND_next(v) == NULL);
UNSUPPORTED("1tm9zh31fbm0sufjyg78ke83i"); //     ND_next(v) = ND_next(u);
UNSUPPORTED("cswodtimhknr7wxlbmhxfzutw"); //     if (ND_next(u))
UNSUPPORTED("7aukr7lwtj4wnis5h9ciserpd"); // 	ND_prev(ND_next(u)) = v;
UNSUPPORTED("6oaowqmk3llh0vp3h096bo6nq"); //     ND_prev(v) = u;
UNSUPPORTED("5ymiaen2fr7qzsn4uotyf2eqy"); //     ND_next(u) = v;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="delete_fast_node", key="emsq7b6s5100lscckzy3ileqd", definition="void delete_fast_node(graph_t * g, node_t * n)")
public static void delete_fast_node(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("emsq7b6s5100lscckzy3ileqd","delete_fast_node");
try {
    assert(find_fast_node(g, n)!=null);
    if (ND_next(n)!=null)
	ND_prev(ND_next(n), ND_prev(n));
    if (ND_prev(n)!=null)
	ND_next(ND_prev(n), ND_next(n));
    else
	GD_nlist(g, ND_next(n));
} finally {
LEAVING("emsq7b6s5100lscckzy3ileqd","delete_fast_node");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="", key="eg08ajzojsm0224btmfi7kdxt", definition="node_t *virtual_node(graph_t * g)")
public static ST_Agnode_s virtual_node(ST_Agraph_s g) {
ENTERING("eg08ajzojsm0224btmfi7kdxt","virtual_node");
try {
	ST_Agnode_s n;
	
    n = new ST_Agnode_s();
//  agnameof(n) = "virtual";
    AGTYPE(n, AGNODE);
    n.base.data = (ST_Agrec_s) new ST_Agnodeinfo_t().castTo(ST_Agrec_s.class);
    n.root = agroot(g);
    ND_node_type(n, VIRTUAL);
    ND_rw(n, 1); ND_lw(n, 1);
    ND_ht(n, 1);
    ND_UF_size(n, 1);
    alloc_elist(4, ND_in(n));
    alloc_elist(4, ND_out(n));
    fast_node(g, n);
    GD_n_nodes(g, GD_n_nodes(g)+1);
    return n;
} finally {
LEAVING("eg08ajzojsm0224btmfi7kdxt","virtual_node");
}
}




@Difficult
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="flat_edge", key="8dvukicq96g5t3xgdl0ue35mj", definition="void flat_edge(graph_t * g, edge_t * e)")
public static void flat_edge(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("8dvukicq96g5t3xgdl0ue35mj","flat_edge");
try {
    elist_append(e, ND_flat_out(agtail(e)));
    elist_append(e, ND_flat_in(aghead(e)));
    GD_has_flat_edges(g, true);
    GD_has_flat_edges(dot_root(g), true);
} finally {
LEAVING("8dvukicq96g5t3xgdl0ue35mj","flat_edge");
}
}




//3 clspalhiuedfnk9g9rlvfqpg7
// void delete_flat_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="delete_flat_edge", key="clspalhiuedfnk9g9rlvfqpg7", definition="void delete_flat_edge(edge_t * e)")
public static void delete_flat_edge(ST_Agedge_s e) {
ENTERING("clspalhiuedfnk9g9rlvfqpg7","delete_flat_edge");
try {
    assert(e != null);
    if (ED_to_orig(e)!=null && EQ(ED_to_virt(ED_to_orig(e)), e))
	ED_to_virt(ED_to_orig(e), null);
    zapinlist((ND_flat_out(agtail(e))), e);
    zapinlist((ND_flat_in(aghead(e))), e);
} finally {
LEAVING("clspalhiuedfnk9g9rlvfqpg7","delete_flat_edge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="basic_merge", key="dcfpol11cvlt6aaa6phqbp6fo", definition="static void  basic_merge(edge_t * e, edge_t * rep)")
public static void basic_merge(ST_Agedge_s e, ST_Agedge_s rep) {
ENTERING("dcfpol11cvlt6aaa6phqbp6fo","basic_merge");
try {
    if (ED_minlen(rep) < ED_minlen(e))
	ED_minlen(rep, ED_minlen(e));
    while (rep!=null) {
	ED_count(rep, ED_count(rep) + ED_count(e));
	ED_xpenalty(rep, ED_xpenalty(rep) + ED_xpenalty(e));
	ED_weight(rep, ED_weight(rep) + ED_weight(e));
	rep = ED_to_virt(rep);
    }
} finally {
LEAVING("dcfpol11cvlt6aaa6phqbp6fo","basic_merge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="merge_oneway", key="6dxgtoii76tmonlnvz4rmiytd", definition="void  merge_oneway(edge_t * e, edge_t * rep)")
public static void merge_oneway(ST_Agedge_s e, ST_Agedge_s rep) {
ENTERING("6dxgtoii76tmonlnvz4rmiytd","merge_oneway");
try {
    if (EQ(rep, ED_to_virt(e))) {
UNSUPPORTED("84xxsh1cgsif42hgizyxw36ul"); // 	agerr(AGWARN, "merge_oneway glitch\n");
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
    }
    //assert(ED_to_virt(e) == NULL);
    ED_to_virt(e, rep);
    basic_merge(e, rep);
} finally {
LEAVING("6dxgtoii76tmonlnvz4rmiytd","merge_oneway");
}
}




//3 656h1u18x3gommk50i4bak8wi
// static void  unrep(edge_t * rep, edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="unrep", key="656h1u18x3gommk50i4bak8wi", definition="static void  unrep(edge_t * rep, edge_t * e)")
public static Object unrep(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("1cysdqgx90krtmp6pc8358byz"); // unrep(edge_t * rep, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("az4p9xxo3dhparzmtf3co61n"); //     ED_count(rep) -= ED_count(e);
UNSUPPORTED("bf8f3cgvxc5n9ixbj4d1z94r0"); //     ED_xpenalty(rep) -= ED_xpenalty(e);
UNSUPPORTED("5m9h7kqjxjgizf3o1i6udm3c5"); //     ED_weight(rep) -= ED_weight(e);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 62io7qyqg9kqthfkbotnjdq49
// void unmerge_oneway(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/fastgr.c", name="unmerge_oneway", key="62io7qyqg9kqthfkbotnjdq49", definition="void unmerge_oneway(edge_t * e)")
public static Object unmerge_oneway(Object... arg) {
UNSUPPORTED("3rlflkkd29cm53ssikvrqx06v"); // void unmerge_oneway(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3we8ga4x5k2sj4s20xlwumjq5"); //     edge_t *rep, *nextrep;
UNSUPPORTED("e23vpxy61ysfsjvp3u2boafhb"); //     for (rep = ED_to_virt(e); rep; rep = nextrep) {
UNSUPPORTED("bifyf7jydjlvj8u8hc9tqwutm"); // 	unrep(rep, e);
UNSUPPORTED("eyjz7xjwxabdlqhyvuzfkks49"); // 	nextrep = ED_to_virt(rep);
UNSUPPORTED("4h71tbcobl3o6put6h58thpgu"); // 	if (ED_count(rep) == 0)
UNSUPPORTED("c87nxr35m1again4xqc8x4rhh"); // 	    safe_delete_fast_edge(rep);	/* free(rep)? */
UNSUPPORTED("9np2sz8r74ucvij23elgzxkr4"); // 	/* unmerge from a virtual edge chain */
UNSUPPORTED("5wohy8hhosadqtj6712ufa10t"); // 	while ((ED_edge_type(rep) == 1)
UNSUPPORTED("9nortwuccpr0jx3z027d2jcyh"); // 	       && (ND_node_type(aghead(rep)) == 1)
UNSUPPORTED("drowv2sbxmbe2b6632mn3luww"); // 	       && (ND_out(aghead(rep)).size == 1)) {
UNSUPPORTED("6f9b53obe00vhcbxj865xc57u"); // 	    rep = ND_out(aghead(rep)).list[0];
UNSUPPORTED("11fxzls2ru6go1mcuza6i8zhv"); // 	    unrep(rep, e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bm31ww0y2fu2w9n5nyi04ad76"); //     ED_to_virt(e) = NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
