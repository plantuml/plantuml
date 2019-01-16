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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_pointf;

public class geom__c {


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




//3 an3osr8wbab08w759hwbbqhm9
// box mkbox(point p, point q) 
public static Object mkbox(Object... arg) {
UNSUPPORTED("ehmvylmllzguxcdpk8dz6hwdr"); // box mkbox(point p, point q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dmq8mehh6aeppnudj702vldy4"); //     box r;
UNSUPPORTED("56irj1ii7mntnpbge097ptnng"); //     if (p.x < q.x) {
UNSUPPORTED("b8tp7wn90qjwecr4yhm0dif1i"); // 	r.LL.x = p.x;
UNSUPPORTED("2tgz3b5tfwtlyh39flhnyaipw"); // 	r.UR.x = q.x;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6jbrv8d8fjois445uwa6y61uz"); // 	r.LL.x = q.x;
UNSUPPORTED("crijm4pj4imiocb7jzikn2xj5"); // 	r.UR.x = p.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("92wx79b4ryvutf9j0365jonq"); //     if (p.y < q.y) {
UNSUPPORTED("9oisw44o8np9dsov1namsahq2"); // 	r.LL.y = p.y;
UNSUPPORTED("dq93klswmrh70fos28v81um3n"); // 	r.UR.y = q.y;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("aveby33ojiwj4tjoar3jn215s"); // 	r.LL.y = q.y;
UNSUPPORTED("4sjpslco78l4c28t6hrci8g8v"); // 	r.UR.y = p.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2c7s1wgyibdpn3cqrdci4mqwj
// boxf mkboxf(pointf p, pointf q) 
public static Object mkboxf(Object... arg) {
UNSUPPORTED("6axbrwdhhyfo8coxq5s27j6ph"); // boxf mkboxf(pointf p, pointf q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c8ehlu3p9fgdawb34kesn3k9m"); //     boxf r;
UNSUPPORTED("56irj1ii7mntnpbge097ptnng"); //     if (p.x < q.x) {
UNSUPPORTED("b8tp7wn90qjwecr4yhm0dif1i"); // 	r.LL.x = p.x;
UNSUPPORTED("2tgz3b5tfwtlyh39flhnyaipw"); // 	r.UR.x = q.x;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6jbrv8d8fjois445uwa6y61uz"); // 	r.LL.x = q.x;
UNSUPPORTED("crijm4pj4imiocb7jzikn2xj5"); // 	r.UR.x = p.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("92wx79b4ryvutf9j0365jonq"); //     if (p.y < q.y) {
UNSUPPORTED("9oisw44o8np9dsov1namsahq2"); // 	r.LL.y = p.y;
UNSUPPORTED("dq93klswmrh70fos28v81um3n"); // 	r.UR.y = q.y;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("aveby33ojiwj4tjoar3jn215s"); // 	r.LL.y = q.y;
UNSUPPORTED("4sjpslco78l4c28t6hrci8g8v"); // 	r.UR.y = p.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 29pnbvw6n2yoezie6xudgnrrc
// int lineToBox(pointf p, pointf q, boxf b) 
public static Object lineToBox(Object... arg) {
UNSUPPORTED("4yjnf6y95sbc4ugjerul6vk9m"); // int lineToBox(pointf p, pointf q, boxf b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("48cwacl32769puml63lq5tx4b"); //     int inside1, inside2;
UNSUPPORTED("9gsgfs2guis9c3c3oi57mxpq2"); //     /*
UNSUPPORTED("b5pw9v7zbg2sz0vvg6onzk3hw"); //      * First check the two points individually to see whether they
UNSUPPORTED("caais2dj0scdtrf20ixk47dfa"); //      * are inside the rectangle or not.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("cq0b826r85o6zz8ne0szl8yyk"); //     inside1 = (p.x >= b.LL.x) && (p.x <= b.UR.x)
UNSUPPORTED("1gflq8j7hbpz9170ekhci7yn8"); //             && (p.y >= b.LL.y) && (p.y <= b.UR.y);
UNSUPPORTED("bhzozbp5l936vg1gyqwuk4dx6"); //     inside2 = (q.x >= b.LL.x) && (q.x <= b.UR.x)
UNSUPPORTED("ct5vd0ytj5rjcrp2gpt8pkonc"); //             && (q.y >= b.LL.y) && (q.y <= b.UR.y);
UNSUPPORTED("ehkoqnyo9hihgs7jeo4qoysve"); //     if (inside1 != inside2) {
UNSUPPORTED("egywkvzo2t847qnathqnanvcj"); //         return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bmhand7n0jwpq7eue899h1p1f"); //     if (inside1 & inside2) {
UNSUPPORTED("3ywpya2w1bf4n909xcgre9wyy"); //         return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9gsgfs2guis9c3c3oi57mxpq2"); //     /*
UNSUPPORTED("4nkuw4roujcsqvbd2p9xdxitb"); //      * Both points are outside the rectangle, but still need to check
UNSUPPORTED("aminyyg03u6tufsuefljc9ckh"); //      * for intersections between the line and the rectangle.  Horizontal
UNSUPPORTED("6l0cqsf2f98o3thcqrs8isnnk"); //      * and vertical lines are particularly easy, so handle them
UNSUPPORTED("7q484vtovilvh9k2g9clt86ne"); //      * separately.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("2hgj2jk4zufho793ru5xo96qn"); //     if (p.x == q.x) {
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("aw25rbwlb76hcmi7i0a4hkroq"); //          * Vertical line.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("5x1j3sw3kiwvrlq7iovu7p1zg"); //         if (((p.y >= b.LL.y) ^ (q.y >= b.LL.y))
UNSUPPORTED("8z5rdz2i2k4vnxh41xau7lqox"); //                 && (p.x >= b.LL.x)
UNSUPPORTED("8sa26clzml2fktszuyohhq96s"); //                 && (p.x <= b.UR.x)) {
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("aypua15gtzlgyfgij2zgi2fy3"); //     } else if (p.y == q.y) {
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("cuvbkbznqopg3ock0vwua52e"); //          * Horizontal line.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("cr98tmonigesw5kgv445eaagx"); //         if (((p.x >= b.LL.x) ^ (q.x >= b.LL.x))
UNSUPPORTED("7moqrnwj9gzn15xyb4n1h71nz"); //                 && (p.y >= b.LL.y)
UNSUPPORTED("adozfrtcv9rz4ut26t65aqjmp"); //                 && (p.y <= b.UR.y)) {
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("3dx5qy44ca9k65rrlunknc4jr"); //         double m, x, y, low, high;
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("17rosazxzae3vpa7evezmoc18"); //          * Diagonal line.  Compute slope of line and use
UNSUPPORTED("38f61i9iz6jnzq3tqj3l15io"); //          * for intersection checks against each of the
UNSUPPORTED("24s7erxhfmz9equ6bz65nd452"); //          * sides of the rectangle: left, right, bottom, top.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("30gqvlvlxk2fyms9obiav5eld"); //         m = (q.y - p.y)/(q.x - p.x);
UNSUPPORTED("bydrxw4l4bbi1q3tpzpouyb05"); //         if (p.x < q.x) {
UNSUPPORTED("3vk3thjtlk0eg13l7tpn464hf"); //             low = p.x;  high = q.x;
UNSUPPORTED("7g575y36b78djy0o00izqx7eq"); //         } else {
UNSUPPORTED("9kxzbc22yz2m5undmp2d8iv7i"); //             low = q.x; high = p.x;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("8iz5exp7hmnagqdgln48v9h3c"); //          * Left edge.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("1kvmnzix9327sc1lr8oldfwfa"); //         y = p.y + (b.LL.x - p.x)*m;
UNSUPPORTED("6mt68el60h3098f84p9qof9cp"); //         if ((b.LL.x >= low) && (b.LL.x <= high)
UNSUPPORTED("7fuiowax7i86a2628diixw0kg"); //                 && (y >= b.LL.y) && (y <= b.UR.y)) {
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("cqsjfoaczly4orr3aoyk6a3kn"); //          * Right edge.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("8ix32dk9v0hahcbsbusuf1rvs"); //         y += (b.UR.x - b.LL.x)*m;
UNSUPPORTED("brui2iilj0qz4n9w80cckznor"); //         if ((y >= b.LL.y) && (y <= b.UR.y)
UNSUPPORTED("9lghx6zht8yyj8oo7vd3urxb0"); //                 && (b.UR.x >= low) && (b.UR.x <= high)) {
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("9k6j1wl5s3bvhlivy6qc3xt6b"); //          * Bottom edge.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("32eo9bh3jk75sopl737la1xiu"); //         if (p.y < q.y) {
UNSUPPORTED("dpom6twg5z6ngi160zl4sbdfy"); //             low = p.y;  high = q.y;
UNSUPPORTED("7g575y36b78djy0o00izqx7eq"); //         } else {
UNSUPPORTED("9n0a69t2iof5xbu1u5knw8v5e"); //             low = q.y; high = p.y;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("balzrwlwsv6rnon3m0ey2ftly"); //         x = p.x + (b.LL.y - p.y)/m;
UNSUPPORTED("d93289lj2vrieguknwyq7vizz"); //         if ((x >= b.LL.x) && (x <= b.UR.x)
UNSUPPORTED("2pou8in6puryywhwltwlqmbvw"); //                 && (b.LL.y >= low) && (b.LL.y <= high)) {
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("ap75hs54mf8uwlaksz3dq3q89"); //         /*
UNSUPPORTED("95agizrp9b5p1dhdv0pxg6eh7"); //          * Top edge.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("725knfzo8czrv30p686pkacn"); //         x += (b.UR.y - b.LL.y)/m;
UNSUPPORTED("d93289lj2vrieguknwyq7vizz"); //         if ((x >= b.LL.x) && (x <= b.UR.x)
UNSUPPORTED("6olkmrl6prxmetzy935itn4zw"); //                 && (b.UR.y >= low) && (b.UR.y <= high)) {
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8azkpi8o0wzdufa90lw8hpt6q"); //     return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1r7uvomrrc3o0z8d9ompm1ig4
// void rect2poly(pointf *p) 
public static Object rect2poly(Object... arg) {
UNSUPPORTED("4upujvzyed550abavjj8vlza1"); // void rect2poly(pointf *p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("jjuy9fch36k2y85yrqsb31r0"); //     p[3].x = p[2].x = p[1].x;
UNSUPPORTED("9meelngzsr1xhvzldh1ry9iww"); //     p[2].y = p[1].y;
UNSUPPORTED("adltawlh6me0hjx77x5jo35mr"); //     p[3].y = p[0].y;
UNSUPPORTED("3ben3eqfn8zv3650vloyu3dk"); //     p[1].x = p[0].x;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3aiyj7urv33rvps5ds204tciu
// static pointf rotatepf(pointf p, int cwrot) 
public static ST_pointf rotatepf(final ST_pointf p, int cwrot) {
// WARNING!! STRUCT
return rotatepf_w_(p.copy(), cwrot).copy();
}
private static ST_pointf rotatepf_w_(final ST_pointf p, int cwrot) {
ENTERING("3aiyj7urv33rvps5ds204tciu","rotatepf");
try {
 UNSUPPORTED("adzi0wztceimu4ni3aonznmq7"); // static pointf rotatepf(pointf p, int cwrot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bvmbf4zjo22hbkaarrfpdlocf"); //     static double sina, cosa;
UNSUPPORTED("2q61ok3mvkrnszcasq86sa47u"); //     static int last_cwrot;
UNSUPPORTED("7lh87lvufqsd73q9difg0omei"); //     pointf P;
UNSUPPORTED("apr20mshcgdjbln509cnpuysv"); //     /* cosa is initially wrong for a cwrot of 0
UNSUPPORTED("7chgrmqliof6d9xytud69tz1u"); //      * this caching only works because we are never called for 0 rotations */
UNSUPPORTED("bbm4jlwljjo7wmvr5ma5c3ybf"); //     if (cwrot != last_cwrot) {
UNSUPPORTED("djdw08yi87cxa9gld79itcxte"); // 	sincos(cwrot / (2 * M_PI), &sina, &cosa);
UNSUPPORTED("1p92a46pieij11gut3g3w5c8a"); // 	last_cwrot = cwrot;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5ebfpnhj3mdplyf7cdm05fvqt"); //     P.x = p.x * cosa - p.y * sina;
UNSUPPORTED("87v4w9w5q8h1qv8g0mktgna71"); //     P.y = p.y * cosa + p.x * sina;
UNSUPPORTED("57gdhsck3pq8wsbtv00wvc7ca"); //     return P;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("3aiyj7urv33rvps5ds204tciu","rotatepf");
}
}




//3 tytryqs1gqpghjdmwwvf1klb
// static point rotatep(point p, int cwrot) 
public static Object rotatep(Object... arg) {
UNSUPPORTED("5e2i6bk41qflr7y85q1osu5ts"); // static point rotatep(point p, int cwrot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("14au2rwic77e6h8riridc0ukd"); //     pointf pf;
UNSUPPORTED("5ec0bowt1l7dbrl26kdbij7m1"); //     P2PF(p, pf);
UNSUPPORTED("37ova7qixpohr8qiao9sckb10"); //     pf = rotatepf(pf, cwrot);
UNSUPPORTED("4z58o606xwke1d8wfsxob59px"); //     PF2P(pf, p);
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 66bs8dyk14129mi4jhjnm40yf
// point cwrotatep(point p, int cwrot) 
public static Object cwrotatep(Object... arg) {
UNSUPPORTED("bjpc8zmw5o75ij41axaonr91n"); // point cwrotatep(point p, int cwrot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("do61zekb7jneykkag5k2ihoru"); //     int x = p.x, y = p.y;
UNSUPPORTED("2dtbcjay37z9xis3sikr2uqvd"); //     switch (cwrot) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1q74ra1plwbzbg77dmxva7rl5"); //     case 90:
UNSUPPORTED("volj3587rzrhhe2x9l5tcqoe"); // 	p.x = y;
UNSUPPORTED("duim642efpe7clezuqpyzpw5y"); // 	p.y = -x;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("17fu4k5xy8d72f5mch8cg6aga"); //     case 180:
UNSUPPORTED("20gmg6188e8c58dfpcwshs68q"); // 	p.x = x;
UNSUPPORTED("c7p7spy43wsvu86ax82p9jeni"); // 	p.y = -y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("cltc0ksnwf3uugjgybaifa7r7"); //     case 270:
UNSUPPORTED("volj3587rzrhhe2x9l5tcqoe"); // 	p.x = y;
UNSUPPORTED("enc0wozuspuqe0erlieadffeb"); // 	p.y = x;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("o9oafab685xqeq9zr7gudl0e"); // 	if (cwrot < 0)
UNSUPPORTED("aaqg3ryvg9knt1q47x4gwtt5x"); // 	    return ccwrotatep(p, -cwrot);
UNSUPPORTED("1ehwxt44pkimc4wg1udnhmlw6"); //         if (cwrot > 360)
UNSUPPORTED("aq1nthhp1dp65k4mktxyqz4bq"); // 	    return cwrotatep(p, cwrot%360);
UNSUPPORTED("56r9i8o5t3hhzow2yhfx4zilm"); // 	return rotatep(p, cwrot);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5q8h2tm3jifiasn423wrm0y60
// pointf cwrotatepf(pointf p, int cwrot) 
public static ST_pointf cwrotatepf(final ST_pointf p, int cwrot) {
// WARNING!! STRUCT
return cwrotatepf_w_(p.copy(), cwrot).copy();
}
private static ST_pointf cwrotatepf_w_(final ST_pointf p, int cwrot) {
ENTERING("5q8h2tm3jifiasn423wrm0y60","cwrotatepf");
try {
    double x = p.x, y = p.y;
    switch (cwrot) {
    case 0:
	break;
    case 90:
	p.setDouble("x", y);
	p.setDouble("y", -x);
	break;
    case 180:
	p.setDouble("x", x);
	p.setDouble("y", -y);
	break;
    case 270:
	p.setDouble("x", y);
	p.setDouble("y", x);
	break;
    default:
	if (cwrot < 0)
	    return ccwrotatepf(p, -cwrot);
        if (cwrot > 360)
	    return cwrotatepf(p, cwrot%360);
	return rotatepf(p, cwrot);
    }
    return p;
} finally {
LEAVING("5q8h2tm3jifiasn423wrm0y60","cwrotatepf");
}
}




//3 8v4jp36jzoo7itgk1f2139cvz
// point ccwrotatep(point p, int ccwrot) 
public static Object ccwrotatep(Object... arg) {
UNSUPPORTED("4netvfi1o450fu9936ptrychj"); // point ccwrotatep(point p, int ccwrot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("do61zekb7jneykkag5k2ihoru"); //     int x = p.x, y = p.y;
UNSUPPORTED("9hd3h63ctysf7ploj23tblup2"); //     switch (ccwrot) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1q74ra1plwbzbg77dmxva7rl5"); //     case 90:
UNSUPPORTED("avktqxvqjaqt2xlnq4ykbvlca"); // 	p.x = -y;
UNSUPPORTED("enc0wozuspuqe0erlieadffeb"); // 	p.y = x;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("17fu4k5xy8d72f5mch8cg6aga"); //     case 180:
UNSUPPORTED("20gmg6188e8c58dfpcwshs68q"); // 	p.x = x;
UNSUPPORTED("c7p7spy43wsvu86ax82p9jeni"); // 	p.y = -y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("cltc0ksnwf3uugjgybaifa7r7"); //     case 270:
UNSUPPORTED("volj3587rzrhhe2x9l5tcqoe"); // 	p.x = y;
UNSUPPORTED("enc0wozuspuqe0erlieadffeb"); // 	p.y = x;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("225zfbgifuf05a2ndoo6eqxeu"); // 	if (ccwrot < 0)
UNSUPPORTED("bdxv7ie6ag4u5y8py9bg2v6s2"); // 	    return cwrotatep(p, -ccwrot);
UNSUPPORTED("2po0nwnam6kgn9hmqm0ls262w"); //         if (ccwrot > 360)
UNSUPPORTED("87ycp7fzgeye35jy3hylox5it"); // 	    return ccwrotatep(p, ccwrot%360);
UNSUPPORTED("9umyw4cv019twbecwlsnwom3k"); // 	return rotatep(p, 360-ccwrot);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6np74e9pfmv8uek8irqru2tma
// pointf ccwrotatepf(pointf p, int ccwrot) 
public static ST_pointf ccwrotatepf(final ST_pointf p, int ccwrot) {
// WARNING!! STRUCT
return ccwrotatepf_w_(p.copy(), ccwrot).copy();
}
private static ST_pointf ccwrotatepf_w_(final ST_pointf p, int ccwrot) {
ENTERING("6np74e9pfmv8uek8irqru2tma","ccwrotatepf");
try {
    double x = p.x, y = p.y;
    switch (ccwrot) {
    case 0:
	break;
    case 90:
	p.setDouble("x", -y);
	p.setDouble("y", x);
	break;
    case 180:
	p.setDouble("x", x);
	p.setDouble("y", -y);
	break;
    case 270:
	p.setDouble("x", y);
	p.setDouble("y", x);
	break;
    default:
	if (ccwrot < 0)
	    return cwrotatepf(p, -ccwrot);
        if (ccwrot > 360)
	    return ccwrotatepf(p, ccwrot%360);
	return rotatepf(p, 360-ccwrot);
    }
    return p;
} finally {
LEAVING("6np74e9pfmv8uek8irqru2tma","ccwrotatepf");
}
}




//3 5vf3yrj3pdre7b1b7c8sq4vnr
// inline box flip_rec_box(box b, point p) 
public static Object flip_rec_box(Object... arg) {
UNSUPPORTED("azqv2nylg3fv81xe2h7obme6h"); // inline box flip_rec_box(box b, point p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dmq8mehh6aeppnudj702vldy4"); //     box r;
UNSUPPORTED("byo3wh0htpthg3nhm3o3s8zw"); //     /* flip box */
UNSUPPORTED("d66qgllk8bjsehblbq26efncl"); //     r.UR.x = b.UR.y;
UNSUPPORTED("cd003wygnwu6zjz9ybxkxzek4"); //     r.UR.y = b.UR.x;
UNSUPPORTED("2ervqmsvgmknymzl5zimfcpf9"); //     r.LL.x = b.LL.y;
UNSUPPORTED("671k71gzkmwyp06lvvfh3boi"); //     r.LL.y = b.LL.x;
UNSUPPORTED("39nfxvqw1vl7uhpu80ul20w83"); //     /* move box */
UNSUPPORTED("ck4g4elw7icvjii70m8950473"); //     r.LL.x += p.x;
UNSUPPORTED("dy93dfzolo2ajmmszxsj5ywgb"); //     r.LL.y += p.y;
UNSUPPORTED("e5m7ftc0fuuqjs4x8ubrmrw2r"); //     r.UR.x += p.x;
UNSUPPORTED("7jezgbyhc86h5l890v0f8i1l9"); //     r.UR.y += p.y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 10berg5l071upv3r6ei5ri6h6
// boxf flip_rec_boxf(boxf b, pointf p) 
public static Object flip_rec_boxf(Object... arg) {
UNSUPPORTED("7lxs02hyh7jlorkakwwex7np1"); // boxf flip_rec_boxf(boxf b, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c8ehlu3p9fgdawb34kesn3k9m"); //     boxf r;
UNSUPPORTED("byo3wh0htpthg3nhm3o3s8zw"); //     /* flip box */
UNSUPPORTED("d66qgllk8bjsehblbq26efncl"); //     r.UR.x = b.UR.y;
UNSUPPORTED("cd003wygnwu6zjz9ybxkxzek4"); //     r.UR.y = b.UR.x;
UNSUPPORTED("2ervqmsvgmknymzl5zimfcpf9"); //     r.LL.x = b.LL.y;
UNSUPPORTED("671k71gzkmwyp06lvvfh3boi"); //     r.LL.y = b.LL.x;
UNSUPPORTED("39nfxvqw1vl7uhpu80ul20w83"); //     /* move box */
UNSUPPORTED("ck4g4elw7icvjii70m8950473"); //     r.LL.x += p.x;
UNSUPPORTED("dy93dfzolo2ajmmszxsj5ywgb"); //     r.LL.y += p.y;
UNSUPPORTED("e5m7ftc0fuuqjs4x8ubrmrw2r"); //     r.UR.x += p.x;
UNSUPPORTED("7jezgbyhc86h5l890v0f8i1l9"); //     r.UR.y += p.y;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 90k9l4o3khu3dw0fzkrcd97nk
// double ptToLine2 (pointf a, pointf b, pointf p) 
public static double ptToLine2(final ST_pointf a, final ST_pointf b, final ST_pointf p) {
// WARNING!! STRUCT
return ptToLine2_w_(a.copy(), b.copy(), p.copy());
}
private static double ptToLine2_w_(final ST_pointf a, final ST_pointf b, final ST_pointf p) {
ENTERING("90k9l4o3khu3dw0fzkrcd97nk","ptToLine2");
try {
  double dx = b.x-a.x;
  double dy = b.y-a.y;
  double a2 = (p.y-a.y)*dx - (p.x-a.x)*dy;
  a2 *= a2;   /* square - ensures that it is positive */
  if (a2 < 0.0000000001) return 0.;  /* avoid 0/0 problems */
  return a2 / (dx*dx + dy*dy);
} finally {
LEAVING("90k9l4o3khu3dw0fzkrcd97nk","ptToLine2");
}
}




//3 7a3ftw4ubky8oz0e6sd4diag1
// int line_intersect (pointf a, pointf b, pointf c, pointf d, pointf* p) 
public static Object line_intersect(Object... arg) {
UNSUPPORTED("4owj0m9g05vi1yxcownz0rtry"); // int line_intersect (pointf a, pointf b, pointf c, pointf d, pointf* p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5kh3p8pi4ouhb71qxt1wc19p4"); //     pointf mv = sub_pointf(b,a);
UNSUPPORTED("4qjn6j13besul6kp2zbryihnw"); //     pointf lv = sub_pointf(d,c);
UNSUPPORTED("cp5xvl6z479t3k1iqlafo0tu5"); //     pointf ln = perp (lv);
UNSUPPORTED("e6xr6qn23vmjveolqf2by1ynk"); //     double lc = -(ln.x*c.x+ln.y*c.y);
UNSUPPORTED("ez3o3rbsr12u6yt8icumd4ct"); //     double dt = (ln.x*mv.x+ln.y*mv.y);
UNSUPPORTED("c13p9pc06v96fokdr0tm36rec"); //     if (fabs(dt) < 0.0000000001) return 0;
UNSUPPORTED("4rkov8dzjqgoc0f65iwx9dri2"); //     *p = sub_pointf(a,scale(((ln.x*a.x+ln.y*a.y)+lc)/dt,mv));
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
