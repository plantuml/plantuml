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
package gen.lib.gvc;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_pointf;

public class gvrender__c {
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


//1 4cynie80t6jls6vhgs3jl2fit
// static pointf *AF


//1 9o8a3mexi9w7oq3wr7jtjcihk
// static int sizeAF




//3 9fduxdho8czelodj9h6y89ino
// int gvrender_select(GVJ_t * job, const char *str) 
public static Object gvrender_select(Object... arg) {
UNSUPPORTED("8q2hci07ap2ph732vslggau4t"); // int gvrender_select(GVJ_t * job, const char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("5ssvxydvbzuxmb74t0hvdbmws"); //     gvplugin_available_t *plugin;
UNSUPPORTED("9q2rkohjybr7oxouto8gnuzsb"); //     gvplugin_installed_t *typeptr;
UNSUPPORTED("5s8ygazjg27yo5uwaw66w15r1"); //     gvplugin_load(gvc, API_device, str);
UNSUPPORTED("ek4fyjg4ptxc2el4evmh805o1"); //     /* When job is created, it is zeroed out.
UNSUPPORTED("3kefg2aeqsvm2d6dncfwpxsr8"); //      * Some flags, such as OUTPUT_NOT_REQUIRED, may already be set,
UNSUPPORTED("9ez2btubxikj170dwe5jqvnof"); //      * so don't reset.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("4tlui225yloqahoq9la8pzwy1"); //     /* job->flags = 0; */
UNSUPPORTED("76gynnjrs6xvbeb7wfpi1ev1l"); //     plugin = gvc->api[API_device];
UNSUPPORTED("3qzhu1d1ev2sbxdqyn2rujv5y"); //     if (plugin) {
UNSUPPORTED("8cnmkxanogd09zc24faarugvo"); // 	typeptr = plugin->typeptr;
UNSUPPORTED("cx8hqrenucgiq5zy97oaqusmc"); // 	job->device.engine = (gvdevice_engine_t *) (typeptr->engine);
UNSUPPORTED("bm8s8c7aquf0i3erdxldrern4"); // 	job->device.features = (gvdevice_features_t *) (typeptr->features);
UNSUPPORTED("7tiv1ctzor8f6a193o9c3dc4"); // 	job->device.id = typeptr->id;
UNSUPPORTED("b3fa1nmh14yrlxh7u11av178h"); // 	job->device.type = plugin->typestr;
UNSUPPORTED("98gdwzzklfnnvl4rcrxln6ax5"); // 	job->flags |= job->device.features->flags;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("7kkm265fhqxbcvyb1tfbvlbg"); // 	return 999;	/* FIXME - should differentiate problem */
UNSUPPORTED("7f2ichmjy32bcdpzo6eam8v7t"); //     /* The device plugin has a dependency on a render plugin,
UNSUPPORTED("cfcjkcl1s1689uqm7fm9yszr4"); //      * so the render plugin should be available as well now */
UNSUPPORTED("bu439zn5xm8jaz1bs1pxzp0dp"); //     plugin = gvc->api[API_render];
UNSUPPORTED("3qzhu1d1ev2sbxdqyn2rujv5y"); //     if (plugin) {
UNSUPPORTED("8cnmkxanogd09zc24faarugvo"); // 	typeptr = plugin->typeptr;
UNSUPPORTED("7tt4s00k0osfiqwahwoff1agx"); // 	job->render.engine = (gvrender_engine_t *) (typeptr->engine);
UNSUPPORTED("669i3eyhmswim1vqxlgkovfrk"); // 	job->render.features = (gvrender_features_t *) (typeptr->features);
UNSUPPORTED("11cw3vs43482kobe5ny07nu35"); // 	job->render.type = plugin->typestr;
UNSUPPORTED("gnm8nzbq1sv2lxsg2d1aerng"); // 	job->flags |= job->render.features->flags;
UNSUPPORTED("243x5gphnardgbfaphg9njktu"); // 	if (job->device.engine)
UNSUPPORTED("5ptnbs646e5n7ko2ca57lk4x"); // 	    job->render.id = typeptr->id;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("5sdmyjmm37n90aaenqcx0oknb"); // 	    /* A null device engine indicates that the device id is also the renderer id
UNSUPPORTED("p1e1xenrya70i1wee5fqsrql"); // 	     * and that the renderer doesn't need "device" functions.
UNSUPPORTED("787dp7rjt82811tj8dlkpb18e"); // 	     * Device "features" settings are still available */
UNSUPPORTED("e580tqo2v5qy1ecm6k2ep5hy9"); // 	    job->render.id = job->device.id;
UNSUPPORTED("e2xl4ygmp65bn739zdpqbu0jq"); // 	return 300;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5868n9w3txgvuqjat3ibyfjdn"); //     job->render.engine = NULL;
UNSUPPORTED("dnojigjj91bs3obtgs440c1pu"); //     return 999;		/* FIXME - should differentiate problem */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8r860ajsh90bfs4d0opfouak
// int gvrender_features(GVJ_t * job) 
public static Object gvrender_features(Object... arg) {
UNSUPPORTED("1szoxv2gwujscwb7rnlygg91m"); // int gvrender_features(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("1vuliw37h78m972417wd4axpz"); //     int features = 0;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("e5v22h9rlc1j09d58ivcttn9r"); // 	features = job->render.features->flags;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6dg3yx86pwrpwg0z9d4frxk3q"); //     return features;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4h2luwpil87bs6pkf4f7qo2k9
// int gvrender_begin_job(GVJ_t * job) 
public static Object gvrender_begin_job(Object... arg) {
UNSUPPORTED("6ev0o0og4bywnq3aw32oauko3"); // int gvrender_begin_job(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("q4eshv98z33fert8lpher2vz"); //     if (gvdevice_initialize(job))
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("9890blf35qwu47a330c2ge4nc"); // 	if (gvre->begin_job)
UNSUPPORTED("aep2cwbk720jnf13nui5zc4gn"); // 	    gvre->begin_job(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3qmxnj1638ltgmhz0csgcpfjk
// void gvrender_end_job(GVJ_t * job) 
public static Object gvrender_end_job(Object... arg) {
UNSUPPORTED("c841pljir2lr5bp3us608ghiq"); // void gvrender_end_job(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("52mw58ugr0a7omsyevqcshnls"); // 	if (gvre->end_job)
UNSUPPORTED("a8sk8cfwcv2mpv7g84p1g2zt4"); // 	    gvre->end_job(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2dnat2dhr60k7lx6x7j868ccu"); //     job->gvc->common.lib = NULL;	/* FIXME - minimally this doesn't belong here */
UNSUPPORTED("dql0bth0nzsrpiu9vnffonrhf"); //     gvdevice_finalize(job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 51309lo7u8y4alv899e4yqadk
// pointf gvrender_ptf(GVJ_t * job, pointf p) 
public static Object gvrender_ptf(Object... arg) {
UNSUPPORTED("bvtfrro3td44g1j12ar1rdjpx"); // pointf gvrender_ptf(GVJ_t * job, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bkj6y2vx6eazaf4xct5yhf4t"); //     pointf rv, translation, scale;
UNSUPPORTED("ayk9sbv0i0esapa28j5940syn"); //     translation = job->translation;
UNSUPPORTED("5cwqynigwx0y1vbchhhtgvhwf"); //     scale.x = job->zoom * job->devscale.x;
UNSUPPORTED("aencyok7qan341gowih0ppjxf"); //     scale.y = job->zoom * job->devscale.y;
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("7etz5b9vm8n98pkfmmehwqbnc"); // 	rv.x = -(p.y + translation.y) * scale.x;
UNSUPPORTED("3q08n8dxktpivul7bbze3tow2"); // 	rv.y = (p.x + translation.x) * scale.y;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("bxbzg3yd7725e95lwwjizgtmd"); // 	rv.x = (p.x + translation.x) * scale.x;
UNSUPPORTED("a7bvzlhyi51usu7oaeku8csk6"); // 	rv.y = (p.y + translation.y) * scale.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 crcyqn21mwz4omhozkd2cp4jc
// pointf *gvrender_ptf_A(GVJ_t * job, pointf * af, pointf * AF, int n) 
public static Object gvrender_ptf_A(Object... arg) {
UNSUPPORTED("6s7dm40y4hhxw9cl6698x6qld"); // pointf *gvrender_ptf_A(GVJ_t * job, pointf * af, pointf * AF, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6wfj8onzmd4ihhh62dqfmqpbx"); //     double t;
UNSUPPORTED("djnewvgctvkpvlggbpw7lj1sc"); //     pointf translation, scale;
UNSUPPORTED("ayk9sbv0i0esapa28j5940syn"); //     translation = job->translation;
UNSUPPORTED("5cwqynigwx0y1vbchhhtgvhwf"); //     scale.x = job->zoom * job->devscale.x;
UNSUPPORTED("aencyok7qan341gowih0ppjxf"); //     scale.y = job->zoom * job->devscale.y;
UNSUPPORTED("3h1yo631e2fq69mxwoggya716"); //     if (job->rotation) {
UNSUPPORTED("7lppn4o65696k131iftk9aihq"); // 	for (i = 0; i < n; i++) {
UNSUPPORTED("8inojmsrqrgmcgv4lgjntmk1p"); // 	    t = -(af[i].y + translation.y) * scale.x;
UNSUPPORTED("1tkzeyviyufhxywvbfka8jbq7"); // 	    AF[i].y = (af[i].x + translation.x) * scale.y;
UNSUPPORTED("d7d03owzn12m66ymdf5730kj0"); // 	    AF[i].x = t;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("7lppn4o65696k131iftk9aihq"); // 	for (i = 0; i < n; i++) {
UNSUPPORTED("6tyixk0bh3dd1yv33d4y93jjz"); // 	    AF[i].x = (af[i].x + translation.x) * scale.x;
UNSUPPORTED("cs1v590y88hqekl0xoh3y093u"); // 	    AF[i].y = (af[i].y + translation.y) * scale.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1mnc0qamisdd62ud5dfotjxss"); //     return AF;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d0k7jgvawtauln88of22iuy40
// static int gvrender_comparestr(const void *s1, const void *s2) 
public static Object gvrender_comparestr(Object... arg) {
UNSUPPORTED("b9vqnpfss55985u1jx9mhx1ss"); // static int gvrender_comparestr(const void *s1, const void *s2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1d9rfce1ndx9322h0frb0z98p"); //     return strcmp(*(char **) s1, *(char **) s2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 93cbs4byyecjhyq5vu6p19p57
// static void gvrender_resolve_color(gvrender_features_t * features, 				   char *name, gvcolor_t * color) 
public static Object gvrender_resolve_color(Object... arg) {
UNSUPPORTED("3knci4gywmc6hb9ujch5j3e9a"); // static void gvrender_resolve_color(gvrender_features_t * features,
UNSUPPORTED("aiwlhbymjioyoqr6cp2hw7ukv"); // 				   char *name, gvcolor_t * color)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eqt7m58ywry2anaa5wpii01ri"); //     char *tok;
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("ety8bdkhij0jyjwrsxks5sz02"); //     color->u.string = name;
UNSUPPORTED("30asoip27omoa1yx0rv9iuutk"); //     color->type = COLOR_STRING;
UNSUPPORTED("dxlu1039n9c20pueltjind8cy"); //     tok = canontoken(name);
UNSUPPORTED("dbrv2omaxn2z4bi8k1tjkgbyl"); //     if (!features->knowncolors
UNSUPPORTED("8s9kvd9phxubwfikx5wl2o4eb"); // 	||
UNSUPPORTED("ewkhu0md9r3wkfof3k37k1a4e"); // 	(bsearch
UNSUPPORTED("7nf6yu4m9s7f9lckxs7vo9v1f"); // 	 (&tok, features->knowncolors, features->sz_knowncolors,
UNSUPPORTED("gms28vfgml0rul9gi006ety8"); // 	  sizeof(char *), gvrender_comparestr)) == NULL) {
UNSUPPORTED("9nqwxnnz5dxy6h3ij9eovzt8g"); // 	/* if tok was not found in known_colors */
UNSUPPORTED("dtlh659rjk09rfjgy14jyed67"); // 	rc = colorxlate(name, color, features->color_type);
UNSUPPORTED("ext4y0w1v77cxnsn9odfd3hh0"); // 	if (rc != 0) {
UNSUPPORTED("2wu9auf85lz824gtt1h6w6ev0"); // 	    if (rc == 1) {
UNSUPPORTED("avvvne4m4cq0vl2trvdvdg2x6"); // 		char *missedcolor = gmalloc(strlen(name) + 16);
UNSUPPORTED("22z3pxv0m8em81soozhhrokv0"); // 		sprintf(missedcolor, "color %s", name);
UNSUPPORTED("cvqbzc6txkpe2jyy2mhmrh12i"); // 		if (emit_once(missedcolor))
UNSUPPORTED("6mde88389r3jhseiz163208n6"); // 		    agerr(AGWARN, "%s is not a known color.\n", name);
UNSUPPORTED("9e5cs6q89hrxxe32ywke5xd0i"); // 		free(missedcolor);
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("627apvfea14p6eh91bth30ke"); // 		agerr(AGERR, "error in colxlate()\n");
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8bz32mh1b67dm7onb98cukhr
// void gvrender_begin_graph(GVJ_t * job, graph_t * g) 
public static Object gvrender_begin_graph(Object... arg) {
UNSUPPORTED("8niigqov4bujqwwvqrq6qkfp"); // void gvrender_begin_graph(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1afe6q2gmnv35uj22aogbg2gh"); //     /* GVC_t *gvc = job->gvc; */
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("62zv37oupg2zeqb8cv4j3mqf0"); //     /* char *s; */
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("3u2mk914st9v3rc5z2iyvnj9k"); // 	/* render specific init */
UNSUPPORTED("12nyq1yot2zd8pg8a198tiw9c"); // 	if (gvre->begin_graph)
UNSUPPORTED("ccy204j05mtzfr44lugx3pslo"); // 	    gvre->begin_graph(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 65xsy2npcrn2bagmq96hoplmc
// void gvrender_end_graph(GVJ_t * job) 
public static Object gvrender_end_graph(Object... arg) {
UNSUPPORTED("e845mtmabap40x91drpi98rgb"); // void gvrender_end_graph(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("47tlf516sc8wywg2atz9mut2v"); // 	if (gvre->end_graph)
UNSUPPORTED("7gu26oojajmet9zp3zbbf8xbl"); // 	    gvre->end_graph(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3z3ed5jhwlihctmb5fr24wahu"); //     gvdevice_format(job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4ptkpos0d265xcx36yp5olziv
// void gvrender_begin_page(GVJ_t * job) 
public static Object gvrender_begin_page(Object... arg) {
UNSUPPORTED("dk9dqgxupw4tns0blo563bw3"); // void gvrender_begin_page(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("3ojiwmme99huqifioqypnkdu"); // 	if (gvre->begin_page)
UNSUPPORTED("4rds4n8ynzo8bstdc56wcaq67"); // 	    gvre->begin_page(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2neje7ddfin73he2004jok2oq
// void gvrender_end_page(GVJ_t * job) 
public static Object gvrender_end_page(Object... arg) {
UNSUPPORTED("60qwktj4d3u2lx6w9ya7x2ll5"); // void gvrender_end_page(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("bbvge1xpf3csvsgdquklh64wz"); // 	if (gvre->end_page)
UNSUPPORTED("6epuiije9k0po6zbf1wuiv0m"); // 	    gvre->end_page(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bjs8uuw252trlu14ei8bzbsvr
// void gvrender_begin_layer(GVJ_t * job) 
public static Object gvrender_begin_layer(Object... arg) {
UNSUPPORTED("73q54jv3dls7oji2nnpvn4jkf"); // void gvrender_begin_layer(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("i2jio5waiow93ulk6mqogx07"); // 	if (gvre->begin_layer)
UNSUPPORTED("979u6s95uq3clnd96s3uo2807"); // 	    gvre->begin_layer(job, job->gvc->layerIDs[job->layerNum],
UNSUPPORTED("bw05jjansbpeswe4ktw01i6j6"); // 			      job->layerNum, job->numLayers);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 401dx8s3fwrxydg3x6alimmby
// void gvrender_end_layer(GVJ_t * job) 
public static Object gvrender_end_layer(Object... arg) {
UNSUPPORTED("9ae3jsve0v8yslvoyx3pnlacr"); // void gvrender_end_layer(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("4vk85i8m93l7fxj6a38e1fcqx"); // 	if (gvre->end_layer)
UNSUPPORTED("4susivxkhmdvvydg2r87cakmo"); // 	    gvre->end_layer(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 27poq9moik2l4d2chmxas8rm1
// void gvrender_begin_cluster(GVJ_t * job, graph_t * sg) 
public static Object gvrender_begin_cluster(Object... arg) {
UNSUPPORTED("r4znhjms3ot7kcrhcixet65f"); // void gvrender_begin_cluster(GVJ_t * job, graph_t * sg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("cj1dczwhpa0r63wls9ra640q7"); // 	if (gvre->begin_cluster)
UNSUPPORTED("5lz5n1eutzyea7yee250561oi"); // 	    gvre->begin_cluster(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1eapbz6ecf0g6kdtqu28czes2
// void gvrender_end_cluster(GVJ_t * job, graph_t * g) 
public static Object gvrender_end_cluster(Object... arg) {
UNSUPPORTED("dd5abixfui8mnp71zqvuj59qz"); // void gvrender_end_cluster(GVJ_t * job, graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("25ikbftyuamzhvmocbqm27sir"); // 	if (gvre->end_cluster)
UNSUPPORTED("b3jrm6ybfvymn6zj4mdz97vu"); // 	    gvre->end_cluster(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c19ss30f97hmgqtzuxhn0qwys
// void gvrender_begin_nodes(GVJ_t * job) 
public static Object gvrender_begin_nodes(Object... arg) {
UNSUPPORTED("97cjt43wnlxgia92a6tk59ud0"); // void gvrender_begin_nodes(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("vylx62m32dzueggujf0nyjtx"); // 	if (gvre->begin_nodes)
UNSUPPORTED("au32m8j1o2erzrxm0k27uf4mx"); // 	    gvre->begin_nodes(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eyo9qxxkb76cgbq7a8ojrxnic
// void gvrender_end_nodes(GVJ_t * job) 
public static Object gvrender_end_nodes(Object... arg) {
UNSUPPORTED("9wdm2tj0803fr2xaj6p5xftvn"); // void gvrender_end_nodes(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("4zv75v2c0kmbs38j0c97u4dwl"); // 	if (gvre->end_nodes)
UNSUPPORTED("5cuhpzbv9lqn4aq0gwyo2ye82"); // 	    gvre->end_nodes(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bevetts9ovmaou0v5l9i1aq5
// void gvrender_begin_edges(GVJ_t * job) 
public static Object gvrender_begin_edges(Object... arg) {
UNSUPPORTED("1alfkafh1xx3y2k9ujca26ii1"); // void gvrender_begin_edges(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("lnm2jgz2nptqnahuln8bs898"); // 	if (gvre->begin_edges)
UNSUPPORTED("7kjkdeb3sqzw19x4k66d6hjck"); // 	    gvre->begin_edges(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 35426mu8gpbqgoc6qh9q4c1xo
// void gvrender_end_edges(GVJ_t * job) 
public static Object gvrender_end_edges(Object... arg) {
UNSUPPORTED("2z8hgdz7ckm5uvm5dk09ytuix"); // void gvrender_end_edges(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("1pwokcd6d4pph0uv4ob0v2ayz"); // 	if (gvre->end_edges)
UNSUPPORTED("45wdv8q1q1h4ql4iu7j2zfw3"); // 	    gvre->end_edges(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d5gj3gg608fp9k43jzb4ts4xb
// void gvrender_begin_node(GVJ_t * job, node_t * n) 
public static Object gvrender_begin_node(Object... arg) {
UNSUPPORTED("cqhiq3ed9f6eu8n3045t48fno"); // void gvrender_begin_node(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("9hw2jrhsnzuh99o0dwa7w2t46"); // 	if (gvre->begin_node)
UNSUPPORTED("2tnqnm5tlwv327095ukix5mkz"); // 	    gvre->begin_node(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 avfb2baedlecrkfbow9cxrpj8
// void gvrender_end_node(GVJ_t * job) 
public static Object gvrender_end_node(Object... arg) {
UNSUPPORTED("cx7wx0qonl1fg8sktpbann4v7"); // void gvrender_end_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("bmg00d8fks4epb7d1z151ujes"); // 	if (gvre->end_node)
UNSUPPORTED("7yw45ob7vq7hv1816oawtvnaw"); // 	    gvre->end_node(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e2uj2wkgme6qb2iw67xtnj76
// void gvrender_begin_edge(GVJ_t * job, edge_t * e) 
public static Object gvrender_begin_edge(Object... arg) {
UNSUPPORTED("92wxdxnrq7ym6oxa8ncnte8yx"); // void gvrender_begin_edge(GVJ_t * job, edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("bpy6xdkxvnktpvt4ng837tqpv"); // 	if (gvre->begin_edge)
UNSUPPORTED("zj6g2pof9uhapm6bxydrx8fq"); // 	    gvre->begin_edge(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dmjwy0x5f2ylehrl4asephmu
// void gvrender_end_edge(GVJ_t * job) 
public static Object gvrender_end_edge(Object... arg) {
UNSUPPORTED("503mqi542lcqpbzcar48yjzca"); // void gvrender_end_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("1esg5a3gkxrmgs5zvdu795q7x"); // 	if (gvre->end_edge)
UNSUPPORTED("eu84oc9h3jhmhc6v1kbvxatlo"); // 	    gvre->end_edge(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a3oxjcx4pit0tlwvqrj5pjgla
// void gvrender_begin_anchor(GVJ_t * job, char *href, char *tooltip, 			   char *target, char *id) 
public static Object gvrender_begin_anchor(Object... arg) {
UNSUPPORTED("t7nw5oeaewb5af8soqhimjf8"); // void gvrender_begin_anchor(GVJ_t * job, char *href, char *tooltip,
UNSUPPORTED("ca2v6kkbpi770n597h24tagaa"); // 			   char *target, char *id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("auez010xhho0xcap2ycz9lju2"); // 	if (gvre->begin_anchor)
UNSUPPORTED("9272f1ryutzdz60cexops6mrp"); // 	    gvre->begin_anchor(job, href, tooltip, target, id);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 939xwaginerexczl0cmv7ihe2
// void gvrender_end_anchor(GVJ_t * job) 
public static Object gvrender_end_anchor(Object... arg) {
UNSUPPORTED("bcd3uehiopd66fbezppgae2dt"); // void gvrender_end_anchor(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("a91isqnl1vm4eci36n2n4cbaj"); // 	if (gvre->end_anchor)
UNSUPPORTED("5qppuiae51yhbsfdv0frgywun"); // 	    gvre->end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8hno79twchywwcg8nsufipda9
// void gvrender_begin_label(GVJ_t * job, label_type type) 
public static Object gvrender_begin_label(Object... arg) {
UNSUPPORTED("4flxiegpywb83nvqdngss5inv"); // void gvrender_begin_label(GVJ_t * job, label_type type)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("1jmpi9g9tuv3h494cpncqbeby"); // 	if (gvre->begin_label)
UNSUPPORTED("dnncmlhww1v0s5m19zpp1ayrx"); // 	    gvre->begin_label(job, type);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 33lmy7dfx78r3jdoiq6vipmbf
// void gvrender_end_label(GVJ_t * job) 
public static Object gvrender_end_label(Object... arg) {
UNSUPPORTED("962k3bif0rr2lxaitbmcqqgap"); // void gvrender_end_label(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("ywfznc4r5iq2ztfi831qudip"); // 	if (gvre->end_label)
UNSUPPORTED("dpb1dlhljjzdlysacyo5qge5w"); // 	    gvre->end_label(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2n07hv6i7ifhafigqmh9hjs41
// void gvrender_textspan(GVJ_t * job, pointf p, textspan_t * span) 
public static Object gvrender_textspan(Object... arg) {
UNSUPPORTED("c11fz6lbqdebdh90bo51ypv2b"); // void gvrender_textspan(GVJ_t * job, pointf p, textspan_t * span)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("7dzn9x60phgi2716c3utq7okm"); //     pointf PF;
UNSUPPORTED("76pnbk0msbwxf3hexhq42v5l3"); //     if (span->str && span->str[0]
UNSUPPORTED("avci39h8hze5k7sgd2z1e5ut6"); // 	&& (!job->obj		/* because of xdgen non-conformity */
UNSUPPORTED("95huo314k0dtj3gzujngpxav9"); // 	    || job->obj->pen != PEN_NONE)) {
UNSUPPORTED("dbbj4xbqmojh658txgt6nut8a"); // 	if (job->flags & (1<<13))
UNSUPPORTED("1lpccyww1zgpiec5mn5cvykxo"); // 	    PF = p;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("7gbtmnftt2mwo238ttx5dn8tk"); // 	    PF = gvrender_ptf(job, p);
UNSUPPORTED("43ewa06mslfnjci1b1imd77l"); // 	if (gvre) {
UNSUPPORTED("e62o0l6l0i86ux0dps9h9e1p5"); // 	    if (gvre->textspan)
UNSUPPORTED("bffevstxgixicve9h9ysw8iox"); // 		gvre->textspan(job, PF, span);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3ip6dcdve8gi3behn0lnxf9ii
// void gvrender_set_pencolor(GVJ_t * job, char *name) 
public static Object gvrender_set_pencolor(Object... arg) {
UNSUPPORTED("4ke4p18hmuf4dn24qbbac7tsw"); // void gvrender_set_pencolor(GVJ_t * job, char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dkx0mdb9is3arqebifuxxgail"); //     gvcolor_t *color = &(job->obj->pencolor);
UNSUPPORTED("7qm0rh822k1sczzlqrrqvbs2x"); //     char *cp = NULL;
UNSUPPORTED("9of4r30ug3z0b20d8j12e3pnk"); //     if ((cp = strstr(name, ":")))	/* if its a color list, then use only first */
UNSUPPORTED("cbuf05ko7kaxq2n9zw35l5v2h"); // 	*cp = '\0';
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("71y1xk8zhlqbr7ljk4xi6j2x"); // 	gvrender_resolve_color(job->render.features, name, color);
UNSUPPORTED("80xcpwmaq0yeai2wwkv3ipjd8"); // 	if (gvre->resolve_color)
UNSUPPORTED("5skt8kbpasgr6oei3cw53lrw3"); // 	    gvre->resolve_color(job, color);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3h7ovb03mv53jkj4z79mdiv0m"); //     if (cp)			/* restore color list */
UNSUPPORTED("971i954brvgqb35cftazlqhon"); // 	*cp = ':';
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d1se1bjsc3m7jbhugf98bvdsy
// void gvrender_set_fillcolor(GVJ_t * job, char *name) 
public static Object gvrender_set_fillcolor(Object... arg) {
UNSUPPORTED("5r7oj4opkrxunyrvpxbz9eucg"); // void gvrender_set_fillcolor(GVJ_t * job, char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("eh9bysmovke6e9iezr0onfsif"); //     gvcolor_t *color = &(job->obj->fillcolor);
UNSUPPORTED("7qm0rh822k1sczzlqrrqvbs2x"); //     char *cp = NULL;
UNSUPPORTED("9of4r30ug3z0b20d8j12e3pnk"); //     if ((cp = strstr(name, ":")))	/* if its a color list, then use only first */
UNSUPPORTED("cbuf05ko7kaxq2n9zw35l5v2h"); // 	*cp = '\0';
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("71y1xk8zhlqbr7ljk4xi6j2x"); // 	gvrender_resolve_color(job->render.features, name, color);
UNSUPPORTED("80xcpwmaq0yeai2wwkv3ipjd8"); // 	if (gvre->resolve_color)
UNSUPPORTED("5skt8kbpasgr6oei3cw53lrw3"); // 	    gvre->resolve_color(job, color);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5nbgkqw34assnwmypil321sqd"); //     if (cp)
UNSUPPORTED("971i954brvgqb35cftazlqhon"); // 	*cp = ':';
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bq4b7phujqc961e7ziqipopma
// void gvrender_set_gradient_vals (GVJ_t * job, char *stopcolor, int angle, float frac) 
public static Object gvrender_set_gradient_vals(Object... arg) {
UNSUPPORTED("3y1lob8nc9d6kylff8slmrvt6"); // void gvrender_set_gradient_vals (GVJ_t * job, char *stopcolor, int angle, float frac)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("4wb9uleub62gq1lleh0uzvdvk"); //     gvcolor_t *color = &(job->obj->stopcolor);
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("ba71q0eh93yuaf62z4gj4v16l"); // 	gvrender_resolve_color(job->render.features, stopcolor, color);
UNSUPPORTED("80xcpwmaq0yeai2wwkv3ipjd8"); // 	if (gvre->resolve_color)
UNSUPPORTED("5skt8kbpasgr6oei3cw53lrw3"); // 	    gvre->resolve_color(job, color);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4g6prg2786ftm2i5xccm96e6h"); //     job->obj->gradient_angle = angle;
UNSUPPORTED("6l998scnzlh9f2me8u596k5zd"); //     job->obj->gradient_frac = frac;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2lnvbc2ahvd0talu9x7r9dqgb
// void gvrender_set_style(GVJ_t * job, char **s) 
public static Object gvrender_set_style(Object... arg) {
UNSUPPORTED("ed6tqrjoo4liy9duowya74n8d"); // void gvrender_set_style(GVJ_t * job, char **s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("cc0bdchuthy63n23oh854p3ir"); //     char *line, *p;
UNSUPPORTED("8hqsf9nh2ybldgbnnk3bcmu"); //     obj->rawstyle = s;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("equxj86ju7dkcxy7wefz8l6u5"); // 	if (s)
UNSUPPORTED("bnw5es8qe45nr5yp9tz6z7a3e"); // 	    while ((p = line = *s++)) {
UNSUPPORTED("7qpijbh4ybhl6fxaftd0sfork"); // 		if ((*(line)==*("solid")&&!strcmp(line,"solid")))
UNSUPPORTED("6hud8gwtkg3zahe2ua4hq83ij"); // 		    obj->pen = PEN_SOLID;
UNSUPPORTED("6bwanlgbxst8bc2cv0xzbbsud"); // 		else if ((*(line)==*("dashed")&&!strcmp(line,"dashed")))
UNSUPPORTED("95srmeof6k7hfybaw6p7dyc3n"); // 		    obj->pen = PEN_DASHED;
UNSUPPORTED("8v6hgllyohdu38fmfn5gwk1gs"); // 		else if ((*(line)==*("dotted")&&!strcmp(line,"dotted")))
UNSUPPORTED("3kl1lutns4cz2ild0sz1xh16q"); // 		    obj->pen = PEN_DOTTED;
UNSUPPORTED("4l94fr7n2phccurat8ysgwqio"); // 		else if ((*(line)==*("invis")&&!strcmp(line,"invis")) || (*(line)==*("invisible")&&!strcmp(line,"invisible")))
UNSUPPORTED("ac76fwv7t6jv1nljbublzljmb"); // 		    obj->pen = PEN_NONE;
UNSUPPORTED("aqx8ac8quxag6xil6wuavvi5a"); // 		else if ((*(line)==*("bold")&&!strcmp(line,"bold")))
UNSUPPORTED("acx11t55moi09ri1211mh10m8"); // 		    obj->penwidth = 2.;
UNSUPPORTED("emw5dlagymi75sor2qr0cg7vd"); // 		else if ((*(line)==*("setlinewidth")&&!strcmp(line,"setlinewidth"))) {
UNSUPPORTED("8on9zstcjf8x7vnxo0ruhe15f"); // 		    while (*p)
UNSUPPORTED("kqo5b69i4k26e6iv18175dju"); // 			p++;
UNSUPPORTED("6vkekzy7fxtvws16bdbx2yyp0"); // 		    p++;
UNSUPPORTED("b78whm1hkisnpr9ue88h9s6mp"); // 		    obj->penwidth = atof(p);
UNSUPPORTED("901ar3u82oxx2edwzx67af916"); // 		} else if ((*(line)==*("filled")&&!strcmp(line,"filled")))
UNSUPPORTED("al7f421nliq8cp8umch5g84id"); // 		    obj->fill = FILL_SOLID;
UNSUPPORTED("2c0o9j23cvx64elo52vn5fa55"); // 		else if ((*(line)==*("unfilled")&&!strcmp(line,"unfilled")))
UNSUPPORTED("4rogmkrrzxs6f7jpqzituon7h"); // 		    obj->fill = FILL_NONE;
UNSUPPORTED("afqdk2t3k49ax3yr996td7xij"); // 		else if ((*(line)==*("tapered")&&!strcmp(line,"tapered")));
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("3f0u3cjz549lwgnkj0rlfv6rv"); // 		    agerr(AGWARN,
UNSUPPORTED("2qw622u36zt4xp5de28umz71p"); // 			  "gvrender_set_style: unsupported style %s - ignoring\n",
UNSUPPORTED("c417iw9xyayechno2h2gtkidk"); // 			  line);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eyxsx2an9nibgyxoozqxipc50
// void gvrender_ellipse(GVJ_t * job, pointf * pf, int n, int filled) 
public static Object gvrender_ellipse(Object... arg) {
UNSUPPORTED("7id4bcj5ex7dkq7nf2sjxqkxy"); // void gvrender_ellipse(GVJ_t * job, pointf * pf, int n, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("d33a6zjcnxa56avpwpfci03w0"); // 	if (gvre->ellipse && job->obj->pen != PEN_NONE) {
UNSUPPORTED("5ee29opjv6aldntaubhxveste"); // 	    pointf af[2];
UNSUPPORTED("ezddcbil3k2vpgi3937t190vg"); // 	    /* center */
UNSUPPORTED("eqotgr25by5valy10q8yrlto3"); // 	    af[0].x = (pf[0].x + pf[1].x) / 2.;
UNSUPPORTED("7gk0lclx0jgwhwg64fctmcyuw"); // 	    af[0].y = (pf[0].y + pf[1].y) / 2.;
UNSUPPORTED("1x05c1xvw0f812ervsc5c1db7"); // 	    /* corner */
UNSUPPORTED("9lxv0dip7ojewgkwaldenpvat"); // 	    af[1] = pf[1];
UNSUPPORTED("4hpouh4g99lqypvfaulobxe1q"); // 	    if (!(job->flags & (1<<13)))
UNSUPPORTED("b0ame3q1wj0xu1ddafxmv61r6"); // 		gvrender_ptf_A(job, af, af, 2);
UNSUPPORTED("ecg6t40fcec9tuaidvse5o9k7"); // 	    gvre->ellipse(job, af, filled);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 yp8iozq6mdutn12n5clinpsw
// void gvrender_polygon(GVJ_t * job, pointf * af, int n, int filled) 
public static Object gvrender_polygon(Object... arg) {
UNSUPPORTED("bv02ljcfrzola6maqr6v7beev"); // void gvrender_polygon(GVJ_t * job, pointf * af, int n, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7etlb9y79uddlra5j9bq7hjy0"); //     int noPoly = 0;
UNSUPPORTED("6wlq83bksy1dlfrxo5oqh3qfg"); //     gvcolor_t save_pencolor;
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("4dvbzlf9ofw1sbr0kinbiopjy"); // 	if (gvre->polygon && job->obj->pen != PEN_NONE) {
UNSUPPORTED("3c069bec725soy1ddur2lmx88"); // 	    if (filled & 4) {
UNSUPPORTED("drtmij0csvtkvgrbk1yqdr04x"); // 		noPoly = 1;
UNSUPPORTED("8fyh7hf01mlybwcrx096jx0g9"); // 		filled &= ~4;
UNSUPPORTED("54l2vwy7bed4cbytx05i897io"); // 		save_pencolor = job->obj->pencolor;
UNSUPPORTED("3sjiewne1u0xvr0lijo4fsj4r"); // 		job->obj->pencolor = job->obj->fillcolor;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("axkfudcbukjknssqlc19up4oj"); // 	    if (job->flags & (1<<13))
UNSUPPORTED("dtcn338f4dw09aoxz62631sqw"); // 		gvre->polygon(job, af, n, filled);
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("4bjezvz1si20bgioa75ow050b"); // 		if (sizeAF < n) {
UNSUPPORTED("6uvcrsm6kp84s8ocym7dub4f"); // 		    sizeAF = n + 10;
UNSUPPORTED("bq7cfbgsgn87xeot78xdy9ui5"); // 		    AF = grealloc(AF, sizeAF * sizeof(pointf));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("8uevq5szpmt24ikijexgharm4"); // 		gvrender_ptf_A(job, af, AF, n);
UNSUPPORTED("6lcl4fwte735kt1go13ybqeh"); // 		gvre->polygon(job, AF, n, filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("w9ool8zsr5xhadzpe15wbg2s"); // 	    if (noPoly)
UNSUPPORTED("4n90uzffapw0hn5othv0zrbi3"); // 		job->obj->pencolor = save_pencolor;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 887zxc7xkpuux75pyddc0t2sm
// void gvrender_box(GVJ_t * job, boxf B, int filled) 
public static Object gvrender_box(Object... arg) {
UNSUPPORTED("e0bwh5wg4tq95uckdyef5xzub"); // void gvrender_box(GVJ_t * job, boxf B, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ctvmenh2xwttzvzv2mrj2pizl"); //     pointf A[4];
UNSUPPORTED("ayegu4zx75rk1lklks1prb14u"); //     A[0] = B.LL;
UNSUPPORTED("blhtdvdu48mofhgnuwowogpyp"); //     A[2] = B.UR;
UNSUPPORTED("ef7fxzoqydqv1nwffgjbuf1sd"); //     A[1].x = A[0].x;
UNSUPPORTED("eszwoyo2gnla64vngjy3z1q3v"); //     A[1].y = A[2].y;
UNSUPPORTED("egya9mjzieml8yqx2caonmq6e"); //     A[3].x = A[2].x;
UNSUPPORTED("2oe4fg2nwhsb5dalpz4mg5nbn"); //     A[3].y = A[0].y;
UNSUPPORTED("3zlnq8g9z7psgb93ljeinqwcs"); //     gvrender_polygon(job, A, 4, filled);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3ob8x7840dyx76jqhumocvdr6
// void gvrender_beziercurve(GVJ_t * job, pointf * af, int n, 			  int arrow_at_start, int arrow_at_end, 			  boolean filled) 
public static Object gvrender_beziercurve(Object... arg) {
UNSUPPORTED("9vork9t9rxekftwoe2b4co51v"); // void gvrender_beziercurve(GVJ_t * job, pointf * af, int n,
UNSUPPORTED("3xhx7uex1gp1o3nnsoocvsyif"); // 			  int arrow_at_start, int arrow_at_end,
UNSUPPORTED("275qles1p1xn7ecqbi8gds0jt"); // 			  boolean filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("54lpmghvciyi7cdmsm6wqq3yc"); // 	if (gvre->beziercurve && job->obj->pen != PEN_NONE) {
UNSUPPORTED("axkfudcbukjknssqlc19up4oj"); // 	    if (job->flags & (1<<13))
UNSUPPORTED("52u47z983op39vd0d6yprfpi7"); // 		gvre->beziercurve(job, af, n, arrow_at_start, arrow_at_end,
UNSUPPORTED("4oaa7hd9zzxozaff0cbix2kmp"); // 				  filled);
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("4bjezvz1si20bgioa75ow050b"); // 		if (sizeAF < n) {
UNSUPPORTED("6uvcrsm6kp84s8ocym7dub4f"); // 		    sizeAF = n + 10;
UNSUPPORTED("bq7cfbgsgn87xeot78xdy9ui5"); // 		    AF = grealloc(AF, sizeAF * sizeof(pointf));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("8uevq5szpmt24ikijexgharm4"); // 		gvrender_ptf_A(job, af, AF, n);
UNSUPPORTED("ai7qtqrnw5vcq4jpskv8mtnvw"); // 		gvre->beziercurve(job, AF, n, arrow_at_start, arrow_at_end,
UNSUPPORTED("4oaa7hd9zzxozaff0cbix2kmp"); // 				  filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3b7i3jvz34m0h6yog8k2fvyfd
// void gvrender_polyline(GVJ_t * job, pointf * af, int n) 
public static Object gvrender_polyline(Object... arg) {
UNSUPPORTED("aeof52u6j3ibwxck6m4wrrivp"); // void gvrender_polyline(GVJ_t * job, pointf * af, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("bn4y0nlsh3umgedtpmscc5c9f"); // 	if (gvre->polyline && job->obj->pen != PEN_NONE) {
UNSUPPORTED("axkfudcbukjknssqlc19up4oj"); // 	    if (job->flags & (1<<13))
UNSUPPORTED("2vlnksg14jlxy4qyvzmm0zim1"); // 		gvre->polyline(job, af, n);
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("4bjezvz1si20bgioa75ow050b"); // 		if (sizeAF < n) {
UNSUPPORTED("6uvcrsm6kp84s8ocym7dub4f"); // 		    sizeAF = n + 10;
UNSUPPORTED("bq7cfbgsgn87xeot78xdy9ui5"); // 		    AF = grealloc(AF, sizeAF * sizeof(pointf));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("8uevq5szpmt24ikijexgharm4"); // 		gvrender_ptf_A(job, af, AF, n);
UNSUPPORTED("1gvnhkj77n4ypjiwq1fqxtgp8"); // 		gvre->polyline(job, AF, n);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 blnfx50nrebk4gjfqqnn4rm7f
// void gvrender_comment(GVJ_t * job, char *str) 
public static Object gvrender_comment(Object... arg) {
UNSUPPORTED("b17bq37munzsdb5wh62le6aje"); // void gvrender_comment(GVJ_t * job, char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("5r4zhowru1r7j2yc1tquc5n8c"); //     if (!str || !str[0])
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("2alwhjzc47rg93cqdi01wfpgm"); // 	if (gvre->comment)
UNSUPPORTED("et94lf5x9gl8pdspb3jo8s9mg"); // 	    gvre->comment(job, str);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 93prcj5sag4lfc8a5sb2wmsd
// static imagescale_t get_imagescale(char *s) 
public static Object get_imagescale(Object... arg) {
UNSUPPORTED("1txzuoketj9nchn3z70fng5v3"); // static imagescale_t get_imagescale(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ajfjfii8vpjbquxmivdtxeg2b"); //     if (*s == '\0')
UNSUPPORTED("2coe29ec3wsegxx4gdv5m2kkd"); // 	return IMAGESCALE_FALSE;
UNSUPPORTED("5yk4xjth2k279fyki9bkfr66x"); //     if (!strcasecmp(s, "width"))
UNSUPPORTED("3lu8ftn55h0wldfqo68xgloi7"); // 	return IMAGESCALE_WIDTH;
UNSUPPORTED("92jzx9kaxuoztwbek5hyxzzuz"); //     if (!strcasecmp(s, "height"))
UNSUPPORTED("bu03yyk0qldvbb0ods2x4yawd"); // 	return IMAGESCALE_HEIGHT;
UNSUPPORTED("28h8fykjn40nqnrw5vahnfaq4"); //     if (!strcasecmp(s, "both"))
UNSUPPORTED("kkh6e6r11wcdirsr594y1di"); // 	return IMAGESCALE_BOTH;
UNSUPPORTED("8gusbxecihttgbvw2nehw572g"); //     if (mapbool(s))
UNSUPPORTED("bikcgjnvqb6lez1cpnd544oim"); // 	return IMAGESCALE_TRUE;
UNSUPPORTED("42n71z3o7xthvr2pah8c8u3lw"); //     return IMAGESCALE_FALSE;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 pi86b4slwqam0a1xxdq0xdf0
// void gvrender_usershape(GVJ_t * job, char *name, pointf * a, int n, 			boolean filled, char *imagescale) 
public static Object gvrender_usershape(Object... arg) {
UNSUPPORTED("6rpcjgqcfwaiwqvb418x8kokh"); // void gvrender_usershape(GVJ_t * job, char *name, pointf * a, int n,
UNSUPPORTED("8jff0dyonsdk8syp7l4ev2bet"); // 			boolean filled, char *imagescale)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("exneoozy5g8al0a8y6fxb88zv"); //     usershape_t *us;
UNSUPPORTED("55qv67frd38p9fzngao3h7pz3"); //     double iw, ih, pw, ph;
UNSUPPORTED("cz8micuten0mlwt1eebcv7uki"); //     double scalex, scaley;	/* scale factors */
UNSUPPORTED("crqj2odd9fbsbeuzanl23jpv5"); //     boxf b;			/* target box */
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6l51ed47a0ut0f1nmi3st4xsd"); //     point isz;
UNSUPPORTED("1nk5slg1h855ktp6d9uxli7u3"); //     assert(job);
UNSUPPORTED("265kxn69043hh3vmr1ma8pbpg"); //     assert(name);
UNSUPPORTED("1av8we70pcc0ni7489zk2ttcg"); //     assert(name[0]);
UNSUPPORTED("dztu0qy4iohllldygpjrgx0gv"); //     if (!(us = gvusershape_find(name))) {
UNSUPPORTED("e4iagmogyeon44yhelfxakcrj"); // 	if (find_user_shape(name)) {
UNSUPPORTED("c7obssvvot4eby517jwrv86al"); // 	    if (gvre && gvre->library_shape)
UNSUPPORTED("9hgqs8vju366mem50j32o9147"); // 		gvre->library_shape(job, name, a, n, filled);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8mq2y4jjt82ryu31wh7v1hyd4"); //     isz = gvusershape_size_dpi(us, job->dpi);
UNSUPPORTED("99au5wlhecqbfygzkzv8981mi"); //     if ((isz.x <= 0) && (isz.y <= 0))
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("esl5shk1uch8lb65tqqq1m71g"); //     /* compute bb of polygon */
UNSUPPORTED("9iktk3ta9uy01wdkx10u1v1i0"); //     b.LL = b.UR = a[0];
UNSUPPORTED("1zy7dp66aw6aapybjihdnqlz7"); //     for (i = 1; i < n; i++) {
UNSUPPORTED("1y3lpr6pjid8ob5onc2hncben"); // 	EXPANDBP(b, a[i]);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5l547bajz46b3nak1gcn1xgbx"); //     pw = b.UR.x - b.LL.x;
UNSUPPORTED("370p3pzdb3r4j80cqbinj84ig"); //     ph = b.UR.y - b.LL.y;
UNSUPPORTED("20jqgqhi8nnb31ugjlxv5mfgd"); //     ih = (double) isz.y;
UNSUPPORTED("eajz69qazr70u5u4virjxef9c"); //     iw = (double) isz.x;
UNSUPPORTED("3jv1mdnld1gfgqyowoe3iai46"); //     scalex = pw / iw;
UNSUPPORTED("19lmg6layyecz6vq3tzk6iwqc"); //     scaley = ph / ih;
UNSUPPORTED("crt8ua8sg8nrxfn965bl378nm"); //     switch (get_imagescale(imagescale)) {
UNSUPPORTED("9x5d1p2vwawhnebkxvik4ler0"); //     case IMAGESCALE_TRUE:
UNSUPPORTED("13lnmnrrtauczxh7m0yh16omx"); // 	/* keep aspect ratio fixed by just using the smaller scale */
UNSUPPORTED("35ptrfp241fjgv3d65rhgyvfz"); // 	if (scalex < scaley) {
UNSUPPORTED("evffc82eplte7tbacg5igm7t1"); // 	    iw *= scalex;
UNSUPPORTED("4c103f16tmlh4niqoc6sqgngw"); // 	    ih *= scalex;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("2ia00rm1213j433ep221netai"); // 	    iw *= scaley;
UNSUPPORTED("e73g6zisi7ulw9whkaan7y5go"); // 	    ih *= scaley;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("bg4352ii1wtvlgazxbs7q8h21"); //     case IMAGESCALE_WIDTH:
UNSUPPORTED("98n9ml3m71mrha04tx2qjd2ug"); // 	iw *= scalex;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("ecv5vta16tbc18yxmxjzpkl03"); //     case IMAGESCALE_HEIGHT:
UNSUPPORTED("bthar9ttyrew0nvghtr3jbrlv"); // 	ih *= scaley;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("52xjldytepdnjwbuuftuni25s"); //     case IMAGESCALE_BOTH:
UNSUPPORTED("98n9ml3m71mrha04tx2qjd2ug"); // 	iw *= scalex;
UNSUPPORTED("bthar9ttyrew0nvghtr3jbrlv"); // 	ih *= scaley;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4dtyjnaw3zlyc8ymxnftwyvm6"); //     case IMAGESCALE_FALSE:
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("86tahm66tt20ykc8ny2tzk3ze"); //     /* if image is smaller than target area then center it */
UNSUPPORTED("5h0ev3sjr0rjuc1rvqvcxdfvq"); //     if (iw < pw) {
UNSUPPORTED("cbvvk9amzw6bdegl0ecjking7"); // 	b.LL.x += (pw - iw) / 2.0;
UNSUPPORTED("5hysx98tyafdlh0uqucunh4q8"); // 	b.UR.x -= (pw - iw) / 2.0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7961jlqlc9zg8cezqa8kciiwy"); //     if (ih < ph) {
UNSUPPORTED("cc1xxa7ttu5nlyd4m0qkif9w7"); // 	b.LL.y += (ph - ih) / 2.0;
UNSUPPORTED("6oh6e3zjxnin6hxzzbd6ljfxp"); // 	b.UR.y -= (ph - ih) / 2.0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("69ziz4onms29gp005lfbsxw08"); //     /* convert from graph to device coordinates */
UNSUPPORTED("5ns2lzxxg3bbgizp0jtbmieye"); //     if (!(job->flags & (1<<13))) {
UNSUPPORTED("buzq3sk9f7ll29s59hivg2fni"); // 	b.LL = gvrender_ptf(job, b.LL);
UNSUPPORTED("dl8zgtciz8uf4yloys650b6dc"); // 	b.UR = gvrender_ptf(job, b.UR);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b6p3r12lnoxocu3l1hnza3ybx"); //     if (b.LL.x > b.UR.x) {
UNSUPPORTED("34o3euq4mb31azwc69v72tqa1"); // 	double d = b.LL.x;
UNSUPPORTED("5uptom4thtt0qanfm9axd15zo"); // 	b.LL.x = b.UR.x;
UNSUPPORTED("6pdd6mw8v1li3v7jz1g4q8t8d"); // 	b.UR.x = d;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ui9nw26f8tqmqg8nl2ppbzxs"); //     if (b.LL.y > b.UR.y) {
UNSUPPORTED("5n2cnyobffi7mbdk1qslollkp"); // 	double d = b.LL.y;
UNSUPPORTED("783ve2lazsyx422w165c4syrf"); // 	b.LL.y = b.UR.y;
UNSUPPORTED("5x5nvpjy2r74y4ljpnu4u08i"); // 	b.UR.y = d;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("9mmza3nlarjca7p78m5h9xos4"); // 	gvloadimage(job, us, b, filled, job->render.type);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4u2p88wr82whlushkt6jc2qjm
// void gvrender_set_penwidth(GVJ_t * job, double penwidth) 
public static Object gvrender_set_penwidth(Object... arg) {
UNSUPPORTED("4sxn3kk22l21zki26l63tudc1"); // void gvrender_set_penwidth(GVJ_t * job, double penwidth)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f1y2y1c3z2f172ut1ewmzfnmn"); //     gvrender_engine_t *gvre = job->render.engine;
UNSUPPORTED("dg636tu66pifgwut68sbx2cvx"); //     if (gvre) {
UNSUPPORTED("3zhz40tekxxllcduntmn94hvv"); // 	job->obj->penwidth = penwidth;
UNSUPPORTED("3c3xrh3ygj2k3vrtdokz98wd7"); // 	/*if (gvre->set_penwidth) gvre->set_penwidth(job, penwidth); */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
