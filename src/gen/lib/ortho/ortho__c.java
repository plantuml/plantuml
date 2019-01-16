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
package gen.lib.ortho;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_pointf;
import smetana.core.jmp_buf;

public class ortho__c {
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


//1 7uu4dvqjnvvhc0gokmgcs954k
// extern int odb_flags




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


//1 6ddu6mrp88g3kun2w1gg8ck8t
// typedef Dict_t PointSet


//1 6t1gwljnc5qkhgkp9oc8y7lhm
// typedef Dict_t PointMap


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();

//1 4i7exv80uvzrbbvqtmcwqgujj
// int odb_flags




//3 1fgp1w41g6nj3rcw29phstsaj
// static cell* cellOf (snode* p, snode* q) 
public static Object cellOf(Object... arg) {
UNSUPPORTED("earoc9gw4gdp2iwqsdlz5babo"); // static cell*
UNSUPPORTED("a81f9eexacyqhjwx31312aaij"); // cellOf (snode* p, snode* q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2p9afojds64j2415r77pe11ot"); //     cell* cp = p->cells[0];
UNSUPPORTED("6s9p9cp0t5f776vclqnqj9dps"); //     if ((cp == q->cells[0]) || (cp == q->cells[1])) return cp; 
UNSUPPORTED("3x1wxbq1zv1fxtxbt8cqyf2zm"); //     else return p->cells[1];
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d9bq37gvmpmw51z5hep2ibevp
// static pointf midPt (cell* cp) 
public static Object midPt(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("5m9iv8q1cwqhqjdweoca9fxwp"); // midPt (cell* cp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("6wfic6l7qkikzq3uxy7ra5rk8"); //     p.x = (((cp->bb.LL.x)+(cp->bb.UR.x))/2.0);
UNSUPPORTED("270uxtsi045j2nyb091mgys86"); //     p.y = (((cp->bb.LL.y)+(cp->bb.UR.y))/2.0);
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3fs08z3b3bgjahvaa5l6b4zo0
// static pointf sidePt (snode* ptr, cell* cp) 
public static Object sidePt(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("a38yr0nzj0vkqomj9uks960c1"); // sidePt (snode* ptr, cell* cp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2e0v7u9pfngzg11mt4kn3oudg"); //     pointf pt;
UNSUPPORTED("67cqw53uchqjz2cyglbs25v99"); //     if (cp == ptr->cells[1]) {
UNSUPPORTED("7hlq358idsgr518ei0otfvvzn"); // 	if (ptr->isVert) {
UNSUPPORTED("dxvnot4cde33jxs0bsb7nz382"); // 	    pt.x = cp->bb.LL.x;
UNSUPPORTED("64l4d6adxd1dwebr9az75w0bq"); // 	    pt.y = (((cp->bb.LL.y)+(cp->bb.UR.y))/2.0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("b55gnqjtnj37pizchlqw7tuat"); // 	    pt.x = (((cp->bb.LL.x)+(cp->bb.UR.x))/2.0);
UNSUPPORTED("d899yxmcyrxgknu569q83j56y"); // 	    pt.y = cp->bb.LL.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("7hlq358idsgr518ei0otfvvzn"); // 	if (ptr->isVert) {
UNSUPPORTED("5jm345o0dxsgpqu2h67gvkukd"); // 	    pt.x = cp->bb.UR.x;
UNSUPPORTED("64l4d6adxd1dwebr9az75w0bq"); // 	    pt.y = (((cp->bb.LL.y)+(cp->bb.UR.y))/2.0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("b55gnqjtnj37pizchlqw7tuat"); // 	    pt.x = (((cp->bb.LL.x)+(cp->bb.UR.x))/2.0);
UNSUPPORTED("3p9qsubmglgugaw1d40s99klt"); // 	    pt.y = cp->bb.UR.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ak6zrr3ug1q6nedtkthjyg33a"); //     return pt;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7emnes5443mx9i54qiv085z3c
// static void setSeg (segment* sp, int dir, double fix, double b1, double b2, int l1, int l2) 
public static Object setSeg(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("czy1oo8vy1jomzgzissjz1pa8"); // setSeg (segment* sp, int dir, double fix, double b1, double b2, int l1, int l2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2lzx2lnk6n6qiepwpcof58amv"); //     sp->isVert = dir;
UNSUPPORTED("egu7wy8np7s0sfcam1ccn50l8"); //     sp->comm_coord = fix;
UNSUPPORTED("4k7kksif599bd6vj4gbjlgdvu"); //     if (b1 < b2) {
UNSUPPORTED("2kbc7nvci81onspyljnbbm2yy"); // 	sp->p.p1 = b1;
UNSUPPORTED("69ntpjs4vuyb4c55h77zuda23"); // 	sp->p.p2 = b2;
UNSUPPORTED("a5cq2nxrju7radz9bujgt4rhr"); // 	sp->l1 = l1;
UNSUPPORTED("9tatc5wgmjq70eejnp326bw0s"); // 	sp->l2 = l2;
UNSUPPORTED("20uja4lokugnba2pfzz5gomz4"); // 	sp->flipped = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("dmrpx7gtvzbduny1jg7iwe9m4"); // 	sp->p.p2 = b1;
UNSUPPORTED("1fydpfnyspbu5e91gaqjd462s"); // 	sp->p.p1 = b2;
UNSUPPORTED("1q9i62p8y6lpofiaqtwag6ytb"); // 	sp->l2 = l1;
UNSUPPORTED("askwqfrybpj6e7cjozg2h65p2"); // 	sp->l1 = l2;
UNSUPPORTED("etuylyul4aqlllu3yotsuuzp8"); // 	sp->flipped = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ejdbblkeiy6k5qhpj8qsj0z8y
// static route convertSPtoRoute (sgraph* g, snode* fst, snode* lst) 
public static Object convertSPtoRoute(Object... arg) {
UNSUPPORTED("8fb3yibi0votqqanq3fr6x8pj"); // static route
UNSUPPORTED("wyj8sug7d8lkktfaafr8tnj7"); // convertSPtoRoute (sgraph* g, snode* fst, snode* lst)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("76tjoqkn7jsh2pn0trcuu3nvs"); //     route rte;
UNSUPPORTED("au5gm9chihcao74rl90b6ryps"); //     snode* ptr;
UNSUPPORTED("cywzrf8tem3eh04r3uc8ah1di"); //     snode* next;
UNSUPPORTED("dvl617t5wak51pbm6b2wwpq7a"); //     snode* prev;  /* node in shortest path just previous to next */
UNSUPPORTED("ccl0rgdbwuva223xhat8drba8"); //     int i, sz = 0;
UNSUPPORTED("dyevh82lgyyzdp6weja1ovxir"); //     cell* cp;
UNSUPPORTED("a5qjw7t9yw4fy2yn8wybaubwv"); //     cell* ncp;
UNSUPPORTED("8tfdncboqzrme5mtb8miqamt8"); //     segment seg;
UNSUPPORTED("e8ttz1kqzt1p2bbcbynxzgxwr"); //     double fix, b1, b2;
UNSUPPORTED("1znk1bh9hsbr0uenjzfdyl00x"); //     int l1, l2;
UNSUPPORTED("af5fm46r9aa1jyef1lcxmilbj"); //     pointf bp1, bp2, prevbp;  /* bend points */
UNSUPPORTED("b6zcm30rwu0uuagbtdyocmn03"); // 	/* count no. of nodes in shortest path */
UNSUPPORTED("2wn8ggohh5i2zmr4zmswe82yn"); //     for (ptr = fst; ptr; ptr = (ptr)->n_dad) sz++;
UNSUPPORTED("32gg7r7g3ge2w1h3so5nleqkn"); //     rte.n = 0;
UNSUPPORTED("c8dglxlwc6735e241xb2kleps"); //     rte.segs = (segment*)zmalloc((sz-2)*sizeof(segment));  /* at most sz-2 segments */
UNSUPPORTED("anlmm69dbu1fyet3im0yytjdv"); //     seg.prev = seg.next = 0;
UNSUPPORTED("99cys70aea9mlxqhd6l7nznh8"); //     ptr = prev = (fst)->n_dad;
UNSUPPORTED("adcxm0zct91fjkt21xpk7t2oc"); //     next = (ptr)->n_dad;
UNSUPPORTED("cw4hqow3w9qpnqan38q4lzr80"); //     if ((ptr->cells[0]->flags & 1))
UNSUPPORTED("f3ouda0gkwbhpq1ghgnvzs28s"); // 	cp = ptr->cells[1];
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("bwecpy85k4r89mdewgkqnxmqs"); // 	cp = ptr->cells[0];
UNSUPPORTED("8hu317tt07i24q0t1njlxgaaj"); //     bp1 = sidePt (ptr, cp);
UNSUPPORTED("fojny6cnx3nf3wyctl8jt3m1"); //     while ((next)->n_dad!=NULL) {
UNSUPPORTED("4685y9um6houwpnmq3o3xdnbl"); // 	ncp = cellOf (prev, next);
UNSUPPORTED("8avl1rf7puyncfn3xpiz6jmyq"); // 	updateWts (g, ncp, (ptr)->n_edge);
UNSUPPORTED("e9wkqtlau9mlc4plmvuuh9n7t"); //         /* add seg if path bends or at end */
UNSUPPORTED("f4tdpv7sv5hrh14s0m0qn6ijr"); // 	if ((ptr->isVert != next->isVert) || ((next)->n_dad == lst)) {
UNSUPPORTED("c536jildnbvxtl2yss5a5uez"); // 	    if (ptr->isVert != next->isVert)
UNSUPPORTED("8hpdisbshs8wjwypt17ftfmfg"); // 		bp2 = midPt (ncp);
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("c06vwwj01jup12zkp64pdhnl5"); // 		bp2 = sidePt(next, ncp);
UNSUPPORTED("1yychnglbjeaemvsoqr67uc24"); // 	    if (ptr->isVert) {   /* horizontal segment */
UNSUPPORTED("dvn1bwxcs69692i2rl1pm8ks9"); // 		if (ptr == (fst)->n_dad) l1 = B_NODE;
UNSUPPORTED("7xkaeei1db8jok1w7cm6ipvij"); // 		else if (prevbp.y > bp1.y) l1 = B_UP;
UNSUPPORTED("eudlpvdzus72tg534zqj1oyt2"); // 		else l1 = B_DOWN; 
UNSUPPORTED("2u5nit0ixq58ppnkni37zkx0p"); // 		if (ptr->isVert != next->isVert) {
UNSUPPORTED("ho3qdbjkg3vo1gms22e3owtc"); // 		    if (next->cells[0] == ncp) l2 = B_UP;
UNSUPPORTED("yfq0lkdewjlujiizjym1ixwn"); // 		    else l2 = B_DOWN;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("wfw1pwgc72zyl8jhmc2mx9q1"); // 		else l2 = B_NODE;
UNSUPPORTED("1kjvrvanocnesq3lr4m4rkpql"); // 		fix = cp->bb.LL.y;
UNSUPPORTED("3l1nsh8rq7tmjt0aaypvrjafd"); // 		b1 = cp->bb.LL.x;
UNSUPPORTED("bdk1ammkqt91fkcw3tko38c4f"); // 		b2 = ncp->bb.LL.x;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("3jhmgphu961hd77sesd05sgwa"); // 	    else {   /* vertical segment */
UNSUPPORTED("dvn1bwxcs69692i2rl1pm8ks9"); // 		if (ptr == (fst)->n_dad) l1 = B_NODE;
UNSUPPORTED("5omok2oqvkufnr17ynsa5l31u"); // 		else if (prevbp.x > bp1.x) l1 = B_RIGHT;
UNSUPPORTED("5u8ur9yzca1q5snap89wagubu"); // 		else l1 = B_LEFT; 
UNSUPPORTED("2u5nit0ixq58ppnkni37zkx0p"); // 		if (ptr->isVert != next->isVert) {
UNSUPPORTED("70wuwqcivcyzgs1tpuyker0tw"); // 		    if (next->cells[0] == ncp) l2 = B_RIGHT;
UNSUPPORTED("b1h57bzme4p9um8dgwvjorz6d"); // 		    else l2 = B_LEFT;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("wfw1pwgc72zyl8jhmc2mx9q1"); // 		else l2 = B_NODE;
UNSUPPORTED("a6wqml6r0ya7a0ukfjl1akcof"); // 		fix = cp->bb.LL.x;
UNSUPPORTED("c5y1a76ll49xwb6f525w31cxw"); // 		b1 = cp->bb.LL.y;
UNSUPPORTED("5y9rsak5s8cwzp9r7hqzjtdn2"); // 		b2 = ncp->bb.LL.y;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5ytq0i1egd8zgjegz2alyi2l"); // 	    setSeg (&seg, !ptr->isVert, fix, b1, b2, l1, l2);
UNSUPPORTED("3d5wnmejfwzawn8av2oedjr6w"); // 	    rte.segs[rte.n++] = seg;
UNSUPPORTED("4butkpw7q0mfe8ci5ktgzfx7h"); // 	    cp = ncp;
UNSUPPORTED("6euarq5hpmo6fnhw9236tny10"); // 	    prevbp = bp1;
UNSUPPORTED("9ak3tu9gfug7h6n5oe3atds63"); // 	    bp1 = bp2;
UNSUPPORTED("6upeg6isij4ds1zu1jxrpacvy"); // 	    if ((ptr->isVert != next->isVert) && ((next)->n_dad == lst)) {
UNSUPPORTED("c06vwwj01jup12zkp64pdhnl5"); // 		bp2 = sidePt(next, ncp);
UNSUPPORTED("1ud8fuhorhhfedc95moinbg3n"); // 		l2 = B_NODE;
UNSUPPORTED("7i3it08m376phdxh0p1uzqai5"); // 		if (next->isVert) {   /* horizontal segment */
UNSUPPORTED("6i41faageoastaoondsec5yp4"); // 		    if (prevbp.y > bp1.y) l1 = B_UP;
UNSUPPORTED("5g30gkn2ryx49ev1n4e2smcx9"); // 		    else l1 = B_DOWN; 
UNSUPPORTED("8fxukh2rk7ex4092kgv805ite"); // 		    fix = cp->bb.LL.y;
UNSUPPORTED("cuz1j4nsn7d1nc8gyk6pwxms4"); // 		    b1 = cp->bb.LL.x;
UNSUPPORTED("6bi6zqtgdmd0jnfszi2huxntb"); // 		    b2 = ncp->bb.LL.x;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("qmgaeq8ywmhhu9os2y3ykl5h"); // 		    if (prevbp.x > bp1.x) l1 = B_RIGHT;
UNSUPPORTED("elww0yfcft9fn920zdzqjqag4"); // 		    else l1 = B_LEFT; 
UNSUPPORTED("e942krdqcom8eneazkyc1gf5i"); // 		    fix = cp->bb.LL.x;
UNSUPPORTED("225z3yt58cl9ma5i7jb3b6zj1"); // 		    b1 = cp->bb.LL.y;
UNSUPPORTED("2grsi5yyliiziqp3ehv7saoqg"); // 		    b2 = ncp->bb.LL.y;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("atcm0274c8dmqoq2enw1723k1"); // 		setSeg (&seg, !next->isVert, fix, b1, b2, l1, l2);
UNSUPPORTED("6eajjgh7sernfrp1c4gqwdqva"); // 		rte.segs[rte.n++] = seg;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5oraagbeabq0cwuspqij33hro"); // 	    ptr = next;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1qyv8z4yk5u9fxyioeycj4pg4"); // 	prev = next;
UNSUPPORTED("dtddoorswdnxqgywntqy6r3rj"); // 	next = (next)->n_dad;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6wc8b07bbvef9c5f0xaq0a7w7"); //     rte.segs = realloc (rte.segs, rte.n*sizeof(segment));
UNSUPPORTED("9t92jm5uuo94z4984kskv1zs1"); //     for (i=0; i<rte.n; i++) {
UNSUPPORTED("5lyp63c6dqwdpk6sdzho7f12g"); // 	if (i > 0)
UNSUPPORTED("6ocqv4us3offjwlpkz1trldjf"); // 	    rte.segs[i].prev = rte.segs + (i-1);
UNSUPPORTED("140nkijwoi101c4avzme9y00x"); // 	if (i < rte.n-1)
UNSUPPORTED("2zgm1op1kek2062uh1ir84v1p"); // 	    rte.segs[i].next = rte.segs + (i+1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6rsvido8aoxm4xz31egwihvie"); //     return rte;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aqw2pcwz2wbv0f1rw9usmyqgf
// static void freeChannel (Dt_t* d, channel* cp, Dtdisc_t* disc) 
public static Object freeChannel(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("c9euy7p3sclc7ckkp1bzs1jx"); // freeChannel (Dt_t* d, channel* cp, Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b9qijws3odg8l7q5fou8uuguj"); //     free_graph (cp->G);
UNSUPPORTED("q5fdp3fynm4ytf68bfq4eyi1"); //     free (cp->seg_list);
UNSUPPORTED("15g2jqm1fxtvztnindiv43hg9"); //     free (cp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 czg5smuwmyt5ciilshur9hm5e
// static void freeChanItem (Dt_t* d, chanItem* cp, Dtdisc_t* disc) 
public static Object freeChanItem(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3shy48lfkzdtnmko723ebxmhv"); // freeChanItem (Dt_t* d, chanItem* cp, Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2j623061b9jjl1jqz6i2eartz"); //     dtclose (cp->chans);
UNSUPPORTED("15g2jqm1fxtvztnindiv43hg9"); //     free (cp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cbapcnadrscvk8m1ip2223p3e
// static int chancmpid(Dt_t* d, paird* key1, paird* key2, Dtdisc_t* disc) 
public static Object chancmpid(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("c3dmj421aezge5a3ggj2xi34t"); // chancmpid(Dt_t* d, paird* key1, paird* key2, Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jh3acz833d3cdotdb2s5kr3v"); //   if (key1->p1 > key2->p1) {
UNSUPPORTED("p297m3b0zcsixgurzbmjqkqr"); //     if (key1->p2 <= key2->p2) return 0;
UNSUPPORTED("ayizfb4et318igswkluxa6rgf"); //     else return 1;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("awnfnp6rvvw6qa9g0i6vjio9k"); //   else if (key1->p1 < key2->p1) {
UNSUPPORTED("6hzvi4g07ent2az0wn1att4zj"); //     if (key1->p2 >= key2->p2) return 0;
UNSUPPORTED("c20igl46iqplts26214lc7ebm"); //     else return -1;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("7lrkjjj5lce2uf86c1y9o9yoa"); //   else return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 659jucoqq7j1ud5wehj42ik2d
// static int dcmpid(Dt_t* d, double* key1, double* key2, Dtdisc_t* disc) 
public static Object dcmpid(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("6vxm1h1q3grwa81zaivfch1re"); // dcmpid(Dt_t* d, double* key1, double* key2, Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1cu94gjng90rrt7xtp42ifr1"); //   if (*key1 > *key2) return 1;
UNSUPPORTED("3h1tdtwyfqaqhne2o7pz8yq9h"); //   else if (*key1 < *key2) return -1;
UNSUPPORTED("7lrkjjj5lce2uf86c1y9o9yoa"); //   else return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 ethokur0ooay9stl4lw8geq1r
// static Dtdisc_t chanDisc = 


//1 db0w2llnzryezehbaes3knwwv
// static Dtdisc_t chanItemDisc = 




//3 1yykle604b3x2mutto2rk7k0q
// static void addChan (Dt_t* chdict, channel* cp, double j) 
public static Object addChan(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("5y1j3qnhsbg3b86o7hpanej1s"); // addChan (Dt_t* chdict, channel* cp, double j)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("67yxlxmzwjczlfm96jhe7luye"); //     chanItem* subd = (*(((Dt_t*)(chdict))->searchf))((chdict),(void*)(&j),0001000);
UNSUPPORTED("bl97x4t04cnuvu98zcsh3u3r1"); //     if (!subd) {
UNSUPPORTED("92ya0nhv9b16cc5o2095t5rp2"); // 	subd = (chanItem*)zmalloc(sizeof(chanItem));
UNSUPPORTED("6kkqd46tx54vo55rc8xyc93dg"); // 	subd->v = j;
UNSUPPORTED("e4cijg0myh4jmsjklqxc3ia0r"); // 	subd->chans = dtopen (&chanDisc, Dtoset);
UNSUPPORTED("anh61bjg3nlucvypx7x43xfk7"); // 	(*(((Dt_t*)(chdict))->searchf))((chdict),(void*)(subd),0000001);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("344a2yp8gxqtygac1hl0bxduf"); //     (*(((Dt_t*)(subd->chans))->searchf))((subd->chans),(void*)(cp),0000001);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 870j0jbluhoffx3ke1eog9a0e
// static Dt_t* extractHChans (maze* mp) 
public static Object extractHChans(Object... arg) {
UNSUPPORTED("2xt7ub3z01qt6i7l0luh5zpbd"); // static Dt_t*
UNSUPPORTED("6h6avfk40i1d436sls3sjaic3"); // extractHChans (maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("bhhrlzv5tqbx6ctv8b8hya7ll"); //     snode* np;
UNSUPPORTED("2pyx8we0m19pr0whdh4wa2df9"); //     Dt_t* hchans = dtopen (&chanItemDisc, Dtoset);
UNSUPPORTED("cc6zk9wsty15bcay61nb68jos"); //     for (i = 0; i < mp->ncells; i++) {
UNSUPPORTED("6mflke7nml2pfj18uu5t0itb2"); // 	channel* chp;
UNSUPPORTED("2noex6z427j2b0xavovcqkxja"); // 	cell* cp = mp->cells+i;
UNSUPPORTED("4id7mgutk26h361i0yn0dr0c9"); // 	cell* nextcp;
UNSUPPORTED("lrx70e5v1i9g2x8dyh0781af"); // 	if ((cp->flags & 4)) continue;
UNSUPPORTED("4fw3zqbj9rdk41p6d04n8l4p9"); // 	/* move left */
UNSUPPORTED("4hw0byd0u8kw6qzgqn54tsvsm"); // 	while ((np = cp->sides[M_LEFT]) && (nextcp = np->cells[0]) &&
UNSUPPORTED("rfl3ehaprpjsjs96qg8xsipw"); // 	    !(nextcp->flags & 1)) {
UNSUPPORTED("292smg9hw43fsfevnum565skt"); // 	    cp = nextcp;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cor1kngy665478bu9tlxw55y0"); // 	chp = (channel*)zmalloc(sizeof(channel));
UNSUPPORTED("3tv0uje2s5tbgyq4tltlazjfp"); // 	chp->cp = cp;
UNSUPPORTED("241dtcbm0olj1vf27gmksfshg"); // 	chp->p.p1 = cp->bb.LL.x;
UNSUPPORTED("67k0t4al7ygki756mk386ga5v"); // 	/* move right */
UNSUPPORTED("c89349lhw5ip8nh8afxcjd01t"); // 	cp->flags |= 4;
UNSUPPORTED("6ky3mzdgm24nohd7cwj4ljp2k"); // 	while ((np = cp->sides[M_RIGHT]) && (nextcp = np->cells[1]) &&
UNSUPPORTED("rfl3ehaprpjsjs96qg8xsipw"); // 	    !(nextcp->flags & 1)) {
UNSUPPORTED("292smg9hw43fsfevnum565skt"); // 	    cp = nextcp;
UNSUPPORTED("1en3ueviartdiagf21oyb422w"); // 	    cp->flags |= 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("78071fo42hy6imylzbfy3bf8i"); //         chp->p.p2 = cp->bb.UR.x;
UNSUPPORTED("e8p02vlsess6lxodmtmo1c1ms"); // 	addChan (hchans, chp, chp->cp->bb.LL.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bq8ebjnwnc14ngjegh07xcdh7"); //     return hchans;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7s43hm0mz2wo1mdduca26ojr5
// static Dt_t* extractVChans (maze* mp) 
public static Object extractVChans(Object... arg) {
UNSUPPORTED("2xt7ub3z01qt6i7l0luh5zpbd"); // static Dt_t*
UNSUPPORTED("c36zuoh2qlc3lrap5d8f3y8lk"); // extractVChans (maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("bhhrlzv5tqbx6ctv8b8hya7ll"); //     snode* np;
UNSUPPORTED("dnn2hmk22k5leb4unfdv23ay"); //     Dt_t* vchans = dtopen (&chanItemDisc, Dtoset);
UNSUPPORTED("cc6zk9wsty15bcay61nb68jos"); //     for (i = 0; i < mp->ncells; i++) {
UNSUPPORTED("6mflke7nml2pfj18uu5t0itb2"); // 	channel* chp;
UNSUPPORTED("2noex6z427j2b0xavovcqkxja"); // 	cell* cp = mp->cells+i;
UNSUPPORTED("4id7mgutk26h361i0yn0dr0c9"); // 	cell* nextcp;
UNSUPPORTED("4oetph2txtwv1olvghotnhpps"); // 	if ((cp->flags & 2)) continue;
UNSUPPORTED("c6rkuey5ihzorfjp9881pbmej"); // 	/* move down */
UNSUPPORTED("a9307fsrcb38pq8una61um0oe"); // 	while ((np = cp->sides[M_BOTTOM]) && (nextcp = np->cells[0]) &&
UNSUPPORTED("rfl3ehaprpjsjs96qg8xsipw"); // 	    !(nextcp->flags & 1)) {
UNSUPPORTED("292smg9hw43fsfevnum565skt"); // 	    cp = nextcp;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cor1kngy665478bu9tlxw55y0"); // 	chp = (channel*)zmalloc(sizeof(channel));
UNSUPPORTED("3tv0uje2s5tbgyq4tltlazjfp"); // 	chp->cp = cp;
UNSUPPORTED("1yizwyrbv5k2pljd9srrt507t"); // 	chp->p.p1 = cp->bb.LL.y;
UNSUPPORTED("9l69ytmot9sii7jf1nezunp0j"); // 	/* move up */
UNSUPPORTED("ekzngugyx3nx0hy0tcenivo3e"); // 	cp->flags |= 2;
UNSUPPORTED("eaga01f5n4bcc04jzd7koe2zn"); // 	while ((np = cp->sides[M_TOP]) && (nextcp = np->cells[1]) &&
UNSUPPORTED("rfl3ehaprpjsjs96qg8xsipw"); // 	    !(nextcp->flags & 1)) {
UNSUPPORTED("292smg9hw43fsfevnum565skt"); // 	    cp = nextcp;
UNSUPPORTED("2z9bt6rzic3h5f2bswrctlkm5"); // 	    cp->flags |= 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4t6wc1803m511jzzii1e5refw"); //         chp->p.p2 = cp->bb.UR.y;
UNSUPPORTED("117s1riz7scnahvfdhjad7oo7"); // 	addChan (vchans, chp, chp->cp->bb.LL.x);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("mc3k9myynjaxs0u8wam7xntw"); //     return vchans;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3fx0dtmoubptoy863qaohsyiv
// static void insertChan (channel* chan, segment* seg) 
public static Object insertChan(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9wvfovtdcnhpa9y21q29xb2xs"); // insertChan (channel* chan, segment* seg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8rut445afekmag201py7zerog"); //     seg->ind_no = chan->cnt++;
UNSUPPORTED("1fpx5uvyy1jw7h6nzfgxqij4q"); //     chan->seg_list = ALLOC(chan->cnt, chan->seg_list, segment*);
UNSUPPORTED("dtkgbijbhv6zio2otpjus55me"); //     chan->seg_list[chan->cnt-1] = seg;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2k7dmuexcawtry3q8ifzylkb9
// static channel* chanSearch (Dt_t* chans, segment* seg) 
public static Object chanSearch(Object... arg) {
UNSUPPORTED("9qh7gtx6joh26yrtekarlkx5k"); // static channel*
UNSUPPORTED("6iggv78oc0ydb9b7w5c37fcdb"); // chanSearch (Dt_t* chans, segment* seg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6ysgprb9sb91hot466r1pbjvf"); //   channel* cp;
UNSUPPORTED("3eoemzrlaus9ecq2mmuirfx2e"); //   chanItem* chani = (*(((Dt_t*)(chans))->searchf))((chans),(void*)(&seg->comm_coord),0001000);
UNSUPPORTED("xbnh2djh0amu19ivzrfv1zrb"); //   assert (chani);
UNSUPPORTED("a5sgr1i6e2c5nbli6kjtt8w0o"); //   cp = (*(((Dt_t*)(chani->chans))->searchf))((chani->chans),(void*)(&seg->p),0001000);
UNSUPPORTED("1e4wppypayzgri783mhjwfvr3"); //   assert (cp);
UNSUPPORTED("4jjmko29xep9s4rocrlxrqsof"); //   return cp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e8ecrel25ykgpfve8c2pg90bt
// static void assignSegs (int nrtes, route* route_list, maze* mp) 
public static Object assignSegs(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9gbzlbbthoblu6f8211e9rp46"); // assignSegs (int nrtes, route* route_list, maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7pmetyg241ybw8hq3j61i271y"); //     channel* chan;
UNSUPPORTED("dzpsknrwv8qkqq20hjnjpjn68"); //     int i, j;
UNSUPPORTED("arcr89i9kld6j0uwf2fd919vf"); //     for (i=0;i<nrtes;i++) {
UNSUPPORTED("8nmg5kva9un1iagmu3w2ewcpz"); // 	route rte = route_list[i];
UNSUPPORTED("9ken3gc0llddmodnj5r6bw80u"); // 	for (j=0;j<rte.n;j++) {
UNSUPPORTED("x3l60c48rutxrsfwa82yiy0b"); // 	    segment* seg = rte.segs+j;
UNSUPPORTED("b131wmg0a9ey6t0bzxmyjvujo"); // 	    if (seg->isVert)
UNSUPPORTED("bdeg9z0esjwizlyn8av1pup0v"); // 		chan = chanSearch(mp->vchans, seg);
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("52uzmq8ikrhropj8svxedldsn"); // 		chan = chanSearch(mp->hchans, seg);
UNSUPPORTED("73ufkqlb3bea3t1t87hm2wgdl"); // 	    insertChan (chan, seg);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4648ghx1zvrhmfvw1szjx5en
// static void addLoop (sgraph* sg, cell* cp, snode* dp, snode* sp) 
public static Object addLoop(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("a3tsmcxhg7iyjoceb1tpll3mb"); // addLoop (sgraph* sg, cell* cp, snode* dp, snode* sp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("a5bapcojbf1pon4upj40dgnit"); //     int onTop;
UNSUPPORTED("2mmprwecm43sdonztnzfjh86u"); //     pointf midp = midPt (cp);
UNSUPPORTED("2zty3wxdquu2fbdk9ag3eibug"); //     for (i = 0; i < cp->nsides; i++) {
UNSUPPORTED("6aknf99z3dbb067he348v23c2"); // 	cell* ocp;
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("97a26i8tw0qmzg5i2dlia94tf"); // 	double wt;
UNSUPPORTED("2o74t2kk9zszzjmhl2o58radp"); // 	snode* onp = cp->sides[i];
UNSUPPORTED("2bh0qvoskfpyikivdli33p1t7"); // 	if (onp->isVert) continue;
UNSUPPORTED("3r96db0og4bylr45sqo2u7ire"); // 	if (onp->cells[0] == cp) {
UNSUPPORTED("5ld3bwx3i8kedyixr9hiunybw"); // 	    onTop = 1;
UNSUPPORTED("7u4yqyzc7gp7vvs8j9a8o1vz3"); // 	    ocp = onp->cells[1];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("4nrjdhrb9ccxue57dab1e82ec"); // 	    onTop = 0;
UNSUPPORTED("5t2fdtivke5zplmmsz3noc2h2"); // 	    ocp = onp->cells[0];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3o5hqzfql7utdq40v2mp489na"); // 	p = sidePt (onp, ocp);
UNSUPPORTED("2pqzno9l1yhihvq61yikfha7x"); // 	wt = abs(p.x - midp.x) +  abs(p.y - midp.y);
UNSUPPORTED("eeypctdtd8leg1t71p1ef13ns"); // 	if (onTop)
UNSUPPORTED("ay6bley4n4nuqyi003sc8kb3d"); // 	    createSEdge (sg, sp, onp, 0);  /* FIX weight */
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("bhgjjjfikli3ur37hc7x7pvem"); // 	    createSEdge (sg, dp, onp, 0);  /* FIX weight */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3o1amzqcqc6xflt5rclqgaoop"); //     sg->nnodes += 2;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 89iyhk6f9rhg0e8rz0hlwkesx
// static void addNodeEdges (sgraph* sg, cell* cp, snode* np) 
public static Object addNodeEdges(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3s08k325s6c0dzl8xxm0e71pf"); // addNodeEdges (sgraph* sg, cell* cp, snode* np)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("2mmprwecm43sdonztnzfjh86u"); //     pointf midp = midPt (cp);
UNSUPPORTED("2zty3wxdquu2fbdk9ag3eibug"); //     for (i = 0; i < cp->nsides; i++) {
UNSUPPORTED("2o74t2kk9zszzjmhl2o58radp"); // 	snode* onp = cp->sides[i];
UNSUPPORTED("6aknf99z3dbb067he348v23c2"); // 	cell* ocp;
UNSUPPORTED("9wdrv4uc4c7ssn0qpmxgz5eu1"); // 	pointf p;
UNSUPPORTED("97a26i8tw0qmzg5i2dlia94tf"); // 	double wt;
UNSUPPORTED("5tmebwuyw7chpq5iitcyuvnod"); // 	if (onp->cells[0] == cp)
UNSUPPORTED("7u4yqyzc7gp7vvs8j9a8o1vz3"); // 	    ocp = onp->cells[1];
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("5t2fdtivke5zplmmsz3noc2h2"); // 	    ocp = onp->cells[0];
UNSUPPORTED("3o5hqzfql7utdq40v2mp489na"); // 	p = sidePt (onp, ocp);
UNSUPPORTED("2pqzno9l1yhihvq61yikfha7x"); // 	wt = abs(p.x - midp.x) +  abs(p.y - midp.y);
UNSUPPORTED("d513bewr1npimr2it2ia2k56r"); // 	createSEdge (sg, np, onp, 0);  /* FIX weight */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("580kqdl0txe4fd1g3jdsx7oj8"); //     sg->nnodes++;
UNSUPPORTED("8dkxzvuy1kheqf9w0lmvljni5"); //     np->cells[0] = np->cells[1] = cp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7yrubx0zxon9c2uglsfqdw6wb
// static char* bendToStr (bend b) 
public static Object bendToStr(Object... arg) {
UNSUPPORTED("aso9sesbnmywhnboxv863whri"); // static char* bendToStr (bend b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1nqf38mopzr5h6gz1w5jgibcq"); //   char* s;
UNSUPPORTED("6ep6naubtnkle2baax49za5kt"); //   switch (b) {
UNSUPPORTED("468ky1b2wfpa4muv0a4vayt73"); //   case B_NODE :
UNSUPPORTED("7asao4q2agscqkw1vxzbiovd"); //     s = "B_NODE";
UNSUPPORTED("8ofzhd1f1qc2gz9hfk7vzz6ox"); //     break;
UNSUPPORTED("9p3ucw6vl509wdnlc9fo3qviu"); //   case B_UP :
UNSUPPORTED("eabybll1yskskaq7a8kxvc8tp"); //     s = "B_UP";
UNSUPPORTED("8ofzhd1f1qc2gz9hfk7vzz6ox"); //     break;
UNSUPPORTED("6y3zt8d6u1qyjwi9sxz0vhxov"); //   case B_LEFT :
UNSUPPORTED("83j5r4h8qfyqcjko2filk6pf6"); //     s = "B_LEFT";
UNSUPPORTED("8ofzhd1f1qc2gz9hfk7vzz6ox"); //     break;
UNSUPPORTED("9twa5d2kxeox6wxq7il75kpvb"); //   case B_DOWN :
UNSUPPORTED("2lrnwyogg8ylvavh64j9fsc4v"); //     s = "B_DOWN";
UNSUPPORTED("8ofzhd1f1qc2gz9hfk7vzz6ox"); //     break;
UNSUPPORTED("e3th0xydbkfhgz8nlphys6knw"); //   case B_RIGHT :
UNSUPPORTED("4z5jeqjq3629k9tjxww0ixjqv"); //     s = "B_RIGHT";
UNSUPPORTED("8ofzhd1f1qc2gz9hfk7vzz6ox"); //     break;
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("cw8hinum739bvjkye9axxkjp3"); //   return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 90xcxnm75ks9geu9fenh3dm1y
// static void putSeg (FILE* fp, segment* seg) 
public static Object putSeg(Object... arg) {
UNSUPPORTED("ezdh139xemkeu6ueshufvm7w2"); // static void putSeg (FILE* fp, segment* seg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("70c5pnzzsu7czszgxj1512qab"); //   if (seg->isVert)
UNSUPPORTED("ah8jrf48288m893sn0xhnsoyl"); //     fprintf (fp, "((%f,%f),(%f,%f)) %s %s", seg->comm_coord, seg->p.p1,
UNSUPPORTED("1m3ectywf3939xa468uztvqoq"); //       seg->comm_coord, seg->p.p2, bendToStr (seg->l1), bendToStr (seg->l2));
UNSUPPORTED("8983svt6g1kt3l45bd6ju9mw6"); //   else
UNSUPPORTED("c1qsbthiirdz2q4klv9p53rth"); //     fprintf (fp, "((%f,%f),(%f,%f)) %s %s", seg->p.p1,seg->comm_coord, 
UNSUPPORTED("bxjfdk1v4pdjllz1clm0jzi7c"); //       seg->p.p2, seg->comm_coord, bendToStr (seg->l1), bendToStr (seg->l2));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2fdwckbttldonqwo3v1bwb0tx
// static void dumpChanG (channel* cp, int v) 
public static Object dumpChanG(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("1jiwdzplvv49bv58z35y6cdxe"); // dumpChanG (channel* cp, int v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8ebsbykup813mmiz374hgxn60"); //   int k;
UNSUPPORTED("9sznn1kro0jd847rh8vo0hwbc"); //   intitem* ip;
UNSUPPORTED("1e46umbsk6iochvxe1wvpa4hm"); //   Dt_t* adj;
UNSUPPORTED("6oro5r2fdy2jr6tual6x7sagh"); //   if (cp->cnt < 2) return;
UNSUPPORTED("cz9tbd9xi7o70lfrwgwnls8ry"); //   fprintf (stderr, "channel %d (%f,%f)\n", v, cp->p.p1, cp->p.p2);
UNSUPPORTED("2e81m6ga4aivsowi1d0mro7qz"); //   for (k=0;k<cp->cnt;k++) {
UNSUPPORTED("esbukm9onpnxbeewu9ks5cx7q"); //     adj = cp->G->vertices[k].adj_list;
UNSUPPORTED("7d5y6bg0ngegbqsnsn7gu87bt"); //     if (dtsize(adj) == 0) continue;
UNSUPPORTED("7wdg4xscc6apj5ksye6gvz131"); //     putSeg (stderr, cp->seg_list[k]);
UNSUPPORTED("d811shlhb0ykm0lp0t2naamkw"); //     fputs (" ->\n", stderr);
UNSUPPORTED("4lca7ud2mcwkyvh8ynb1pjo61"); //     for (ip = (intitem*)(*(((Dt_t*)(adj))->searchf))((adj),(void*)(0),0000200); ip; ip = (intitem*)(*(((Dt_t*)(adj))->searchf))((adj),(void*)(ip),0000010)) {
UNSUPPORTED("drhixa423prmpgu5j6qe8l3je"); //       fputs ("     ", stderr);
UNSUPPORTED("8kybw0d4hrww4xqhdna4mlsgc"); //       putSeg (stderr, cp->seg_list[ip->id]);
UNSUPPORTED("24p9gcmr0oxvz0sli52c722vt"); //       fputs ("\n", stderr);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7ijd6pszsxnoopppf6xwo8wdl"); //   }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d0tqkl09dlk95z7uv7a0esz2h
// static void assignTrackNo (Dt_t* chans) 
public static Object assignTrackNo(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("32zdrf69xvfxhlj0l5jffzppy"); // assignTrackNo (Dt_t* chans)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a4p8766eh6ra21i7u5o9nf6rm"); //     Dt_t* lp;
UNSUPPORTED("1mqjeo37v4tmo6kht0rep1tht"); //     Dtlink_t* l1;
UNSUPPORTED("eiqtkkndttzmunuxtnm99mkpy"); //     Dtlink_t* l2;
UNSUPPORTED("dbd08d8bpiw762wer960cicgj"); //     channel* cp;
UNSUPPORTED("b0kisc5bimb4jnz3z1g2yhbqv"); //     int k;
UNSUPPORTED("cp4kv1fg74up3ubek2qyx8d25"); //     for (l1 = dtflatten (chans); l1; l1 = (((Dtlink_t*)(l1))->right)) {
UNSUPPORTED("950o75hi1nj794oxa101pt5t0"); // 	lp = ((chanItem*)l1)->chans;
UNSUPPORTED("71dqer4eeiqshvsh6rbj0kis5"); // 	for (l2 = dtflatten (lp); l2; l2 = (((Dtlink_t*)(l2))->right)) {
UNSUPPORTED("135wr0zp93ul7lqzg4608t7cy"); // 	    cp = (channel*)l2;
UNSUPPORTED("jmhm2rezhg6jgnyq2vvyf1xt"); // 	    if (cp->cnt) {
UNSUPPORTED("oz7u32hmswbxtvcx0h9iwqbc"); //     if (odb_flags & 8) dumpChanG (cp, ((chanItem*)l1)->v);
UNSUPPORTED("4g04bc8ywv1z7gpxqhucbcys2"); // 		top_sort (cp->G);
UNSUPPORTED("9u4bikrj4ida8i29gym2jn5b9"); // 		for (k=0;k<cp->cnt;k++)
UNSUPPORTED("8o41cm37dmlh7y14o8j7byzbn"); // 		    cp->seg_list[k]->track_no = cp->G->vertices[k].topsort_order+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9pqd0punhe3g1up9qd8xxo652"); //    	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4cvz30tum1j5kh2mr4tikagu7
// static void create_graphs(Dt_t* chans) 
public static Object create_graphs(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("bhabfg4ovu9hg3f74xotdq8w2"); // create_graphs(Dt_t* chans)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a4p8766eh6ra21i7u5o9nf6rm"); //     Dt_t* lp;
UNSUPPORTED("1mqjeo37v4tmo6kht0rep1tht"); //     Dtlink_t* l1;
UNSUPPORTED("eiqtkkndttzmunuxtnm99mkpy"); //     Dtlink_t* l2;
UNSUPPORTED("dbd08d8bpiw762wer960cicgj"); //     channel* cp;
UNSUPPORTED("cp4kv1fg74up3ubek2qyx8d25"); //     for (l1 = dtflatten (chans); l1; l1 = (((Dtlink_t*)(l1))->right)) {
UNSUPPORTED("950o75hi1nj794oxa101pt5t0"); // 	lp = ((chanItem*)l1)->chans;
UNSUPPORTED("71dqer4eeiqshvsh6rbj0kis5"); // 	for (l2 = dtflatten (lp); l2; l2 = (((Dtlink_t*)(l2))->right)) {
UNSUPPORTED("135wr0zp93ul7lqzg4608t7cy"); // 	    cp = (channel*)l2;
UNSUPPORTED("3cr2g51ro0c0oi5l2n756dpbt"); // 	    cp->G = make_graph (cp->cnt);
UNSUPPORTED("9pqd0punhe3g1up9qd8xxo652"); //    	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bzybz2vhse04djha8f0fquflw
// static int eqEndSeg (bend S1l2, bend S2l2, bend T1, bend T2) 
public static Object eqEndSeg(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("85zkrfkxr894bmai11bwpink8"); // eqEndSeg (bend S1l2, bend S2l2, bend T1, bend T2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7gxac6sgui9mlexwzhcjde73q"); //     if (((S1l2==T2)&&(S2l2=!T2))
UNSUPPORTED("60oyzebkxpkavx2feau5rt7kl"); //      || ((S1l2==B_NODE)&&(S2l2==T1)))
UNSUPPORTED("dcqs008t3a3ccoe71d0lqe7yd"); // 	return(0);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("1b9bshcz3yhp7bwyu248z4k9t"); // 	return(-1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eb1uhz6fkbngny6t6auaxxt9b
// static int overlapSeg (segment* S1, segment* S2, bend T1, bend T2) 
public static Object overlapSeg(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("ezv23yz3jqikw7nzsie354dxc"); // overlapSeg (segment* S1, segment* S2, bend T1, bend T2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a78h8j082nhcdjv739yn4qppz"); // 	if(S1->p.p2<S2->p.p2) {
UNSUPPORTED("4rsshkwyw60o88gdb3dn0ovf4"); // 		if(S1->l2==T1&&S2->l1==T2) return(-1);
UNSUPPORTED("2mabne1z2rhv79s1slqsz0khx"); // 		else if(S1->l2==T2&&S2->l1==T1) return(1);
UNSUPPORTED("2vzk76estb6plbulns7pz9fh4"); // 		else return(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("f2sgqrqg5ty4z7q2umaxpejio"); // 	else if(S1->p.p2==S2->p.p2) {
UNSUPPORTED("ct6utyz0mi3m48f5wk8rnqoc3"); // 		if(S2->l1==T2) return eqEndSeg (S1->l2, S2->l2, T1, T2);
UNSUPPORTED("5ot15cxdtf6ibi79p9ahnuiyy"); // 		else return -1*eqEndSeg (S2->l2, S1->l2, T1, T2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7aq9p42tl9bfo9gar2o9pyo6"); // 	else { /* S1->p.p2>S2->p.p2 */
UNSUPPORTED("89qwmoj26o4fkmkwqkugfzwjf"); // 		if(S2->l1==T2&&S2->l2==T2) return(-1);
UNSUPPORTED("adhycem4k08o2rbxo59z3jkhz"); // 		else if (S2->l1==T1&&S2->l2==T1) return(1);
UNSUPPORTED("2vzk76estb6plbulns7pz9fh4"); // 		else return(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 916xnvutvt73y05azmf2m6y6t
// static int ellSeg (bend S1l1, bend S1l2, bend T) 
public static Object ellSeg(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("akzolart54jjjeeqmyxqrnmuc"); // ellSeg (bend S1l1, bend S1l2, bend T)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("be5bxc372qf9z4u0bn8p5f3a2"); //     if (S1l1 == T) {
UNSUPPORTED("19rq2setuug0dsuwd18u6bp16"); // 	if (S1l2== T) return -1;
UNSUPPORTED("3x8iplz4npgo85ip5rp23jmcu"); // 	else return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ayizfb4et318igswkluxa6rgf"); //     else return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3set1dgc7lhl9wfckj4voync3
// static int segCmp (segment* S1, segment* S2, bend T1, bend T2) 
public static Object segCmp(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("5so0u4o583yem7g066n6h5h8v"); // segCmp (segment* S1, segment* S2, bend T1, bend T2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cgb5j5ouyqocl1eu5sq5ul1m1"); // 	/* no overlap */
UNSUPPORTED("1m7w9c5br675ne9d38ak6hu53"); //     if((S1->p.p2<S2->p.p1)||(S1->p.p1>S2->p.p2)) return(0);
UNSUPPORTED("edg1u6oyftrvreqlbjqxcznnc"); // 	/* left endpoint of S2 inside S1 */
UNSUPPORTED("1x0gxclmpt2dwwdqev2ara450"); //     if(S1->p.p1<S2->p.p1&&S2->p.p1<S1->p.p2)
UNSUPPORTED("am1178qaqllybktyjo3nv97xc"); // 	return overlapSeg (S1, S2, T1, T2);
UNSUPPORTED("bvhn7dxguoamigzpbv1635rse"); // 	/* left endpoint of S1 inside S2 */
UNSUPPORTED("1x5d71ow7dh9gcypm1uex4adi"); //     else if(S2->p.p1<S1->p.p1&&S1->p.p1<S2->p.p2)
UNSUPPORTED("8jh6cjdtp573oq8qqbvo18fjx"); // 	return -1*overlapSeg (S2, S1, T1, T2);
UNSUPPORTED("cjqc4716c7f0ty1biibdcbi3b"); //     else if(S1->p.p1==S2->p.p1) {
UNSUPPORTED("1a96hc1yp8pa6nhuky2f3kpov"); // 	if(S1->p.p2==S2->p.p2) {
UNSUPPORTED("dakz4gu568b47ilof0mjlkp3g"); // 	    if((S1->l1==S2->l1)&&(S1->l2==S2->l2))
UNSUPPORTED("dtxrebylagy9i05p1v1fhxnuz"); // 		return(0);
UNSUPPORTED("57vde3e47agnudbdc9bacshif"); // 	    else if (S2->l1==S2->l2) {
UNSUPPORTED("67o69o09wpy0v0ur5f1l1c5s5"); // 		if(S2->l1==T1) return(1);
UNSUPPORTED("b6r32yfv7dexk3ru7g6nqwg1g"); // 		else if(S2->l1==T2) return(-1);
UNSUPPORTED("ek9n5w1l5tq5so113ne9728y8"); // 		else if ((S1->l1!=T1)&&(S1->l2!=T1)) return (1);
UNSUPPORTED("3877rrvm4cl9vsly8mophiluw"); // 		else if ((S1->l1!=T2)&&(S1->l2!=T2)) return (-1);
UNSUPPORTED("7ke2h1gpwvqi6ougdwfz82wmh"); // 		else return 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("3lz4dhijiqr5ht941jdjwgi7z"); // 	    else if ((S2->l1==T1)&&(S2->l2==T2)) {
UNSUPPORTED("6679v73zr10343sabv0545ajx"); // 		if ((S1->l1!=T1)&&(S1->l2==T2)) return 1;
UNSUPPORTED("5c357ofc3tlta7b6ntliniwd9"); // 		else if ((S1->l1==T1)&&(S1->l2!=T2)) return -1;
UNSUPPORTED("7ke2h1gpwvqi6ougdwfz82wmh"); // 		else return 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("evz657j0gnvx74823jqra523c"); // 	    else if ((S2->l2==T1)&&(S2->l1==T2)) {
UNSUPPORTED("aitlrwojh186x6j2ty4o4gocm"); // 		if ((S1->l2!=T1)&&(S1->l1==T2)) return 1;
UNSUPPORTED("er78pkwc9gkhcxvyjq62zor6c"); // 		else if ((S1->l2==T1)&&(S1->l1!=T2)) return -1;
UNSUPPORTED("7ke2h1gpwvqi6ougdwfz82wmh"); // 		else return 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("1ny3yv8c13bp4zcfp8dvffoa8"); // 	    else if ((S2->l1==B_NODE)&&(S2->l2==T1)) {
UNSUPPORTED("4mlaa90t6h9d80djun5rflzkq"); // 		return ellSeg (S1->l1, S1->l2, T1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("3dq7kw6w7e1wh4etca0rl6d0v"); // 	    else if ((S2->l1==B_NODE)&&(S2->l2==T2)) {
UNSUPPORTED("4i2pfdj4zt2umblnmyoloiduk"); // 		return -1*ellSeg (S1->l1, S1->l2, T2);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("2v5dfnazye6510cbha31l8i7w"); // 	    else if ((S2->l1==T1)&&(S2->l2==B_NODE)) {
UNSUPPORTED("4rq25g617jse4401t7v75md57"); // 		return ellSeg (S1->l2, S1->l1, T1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("96b656c5ejlopals3g50cqegi"); // 	    else { /* ((S2->l1==T2)&&(S2->l2==B_NODE)) */
UNSUPPORTED("bcifb3xs85visnlkzrg2nt70g"); // 		return -1*ellSeg (S1->l2, S1->l1, T2);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bmcaw8gehzc5d62dx0w3skyig"); // 	else if(S1->p.p2<S2->p.p2) {
UNSUPPORTED("1u3o14xi8e1cxvb445iu3kct6"); // 	    if(S1->l2==T1)
UNSUPPORTED("1qnx7k0mdbkhofu0mdzisxijd"); // 		return eqEndSeg (S2->l1, S1->l1, T1, T2);
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("d9rodbu7eiqzjvik47smclsdl"); // 		return -1*eqEndSeg (S2->l1, S1->l1, T1, T2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7aq9p42tl9bfo9gar2o9pyo6"); // 	else { /* S1->p.p2>S2->p.p2 */
UNSUPPORTED("51m1xa8u2znzxazdwantwyuyv"); // 	    if(S2->l2==T2)
UNSUPPORTED("4f3mewxm7upg06y9z4bjrn4vf"); // 		return eqEndSeg (S1->l1, S2->l1, T1, T2);
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("b7xkdbwtc22b1j95kr0hty8sp"); // 		return -1*eqEndSeg (S1->l1, S2->l1, T1, T2);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1z3rgo8s9sfovtqdrlci7ydng"); //     else if(S1->p.p2==S2->p.p1) {
UNSUPPORTED("dryuqkmwg4utuh4fz56nec4iu"); // 	if(S1->l2==S2->l1) return(0);
UNSUPPORTED("92dwbh0qiegdwle729knbwdao"); // 	else if(S1->l2==T2) return(1);
UNSUPPORTED("2yl5dq1it450fqpkn8wjdxab4"); // 	else return(-1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ewjrcchl8l1d8fp4y1r6qfbp3"); //     else { /* S1->p.p1==S2->p.p2 */
UNSUPPORTED("1h7f8h4o89nvu26mb04i68h34"); // 	if(S1->l1==S2->l2) return(0);
UNSUPPORTED("e97u2o9q8t0rv2icxkb8fsowu"); // 	else if(S1->l1==T2) return(1);
UNSUPPORTED("2yl5dq1it450fqpkn8wjdxab4"); // 	else return(-1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6ufddz487491174h94zezajk8"); //     assert(0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2why0kxj0qrlrn435c0twfblb
// static int seg_cmp(segment* S1, segment* S2)		 
public static Object seg_cmp(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("5ucj9rj7ohyk0eh1h5jzk9qhh"); // seg_cmp(segment* S1, segment* S2)		
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ef82t09fd18di0isi59csjlwu"); //     if(S1->isVert!=S2->isVert||S1->comm_coord!=S2->comm_coord) {
UNSUPPORTED("2a6h1eg3z0m4zzhdw7vtdz33i"); // 	agerr (AGERR, "incomparable segments !! -- Aborting\n");
UNSUPPORTED("1ghgraebed3srgimi1gdqx5nj"); // 	longjmp(jbuf, 1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("88n6v57rjcx5w9zz51wjd80mn"); //     if(S1->isVert)
UNSUPPORTED("10jcxkwtbmjakpk36jpzzhsnm"); // 	return segCmp (S1, S2, B_RIGHT, B_LEFT);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("1ealfunb0xbji4f46jf9balu7"); // 	return segCmp (S1, S2, B_DOWN, B_UP);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7ycppm4b8xqpa85ov0gpp4r1w
// static void  add_edges_in_G(channel* cp) 
public static Object add_edges_in_G(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("e7prlxhst1riecg8sy2dq7ho3"); // add_edges_in_G(channel* cp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2b5x6xxv7d1fwjdnit9o6fac4"); //     int x,y;
UNSUPPORTED("462fowlguwrqt49rjdjc35vdl"); //     segment** seg_list = cp->seg_list;
UNSUPPORTED("9zouge4neqmqsg96284lxensf"); //     int size = cp->cnt;
UNSUPPORTED("dmbfnx3582vhxsi4o65ruesyn"); //     rawgraph* G = cp->G;
UNSUPPORTED("8uyd6tjjadhk8ovw1osaocc0g"); //     for(x=0;x+1<size;x++) {
UNSUPPORTED("d6rcr7i0zif84j0etj6znobof"); // 	for(y=x+1;y<size;y++) {
UNSUPPORTED("13nwmc2b6etct75wmtn4vn94z"); // 	    switch (seg_cmp(seg_list[x],seg_list[y])) {
UNSUPPORTED("5224921bwtkpbe3zt8fvgs1bu"); // 	    case 1:
UNSUPPORTED("5e6oqdqibspr5jq2bc2n7dkk2"); // 		insert_edge(G,x,y);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("7v6ydzgso87upf4u6dz4wpwrk"); // 	    case 0:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6d61fnn1tbjq1dmeag53gout6"); // 	    case -1:
UNSUPPORTED("pcg0zn469jrk5lvqquyyj7oi"); // 		insert_edge(G,y,x);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d5ycixr4ake5beje9bmkeg5p0
// static void add_np_edges (Dt_t* chans) 
public static Object add_np_edges(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("4pvanrmfcrnmuei62le54kcbt"); // add_np_edges (Dt_t* chans)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a4p8766eh6ra21i7u5o9nf6rm"); //     Dt_t* lp;
UNSUPPORTED("1mqjeo37v4tmo6kht0rep1tht"); //     Dtlink_t* l1;
UNSUPPORTED("eiqtkkndttzmunuxtnm99mkpy"); //     Dtlink_t* l2;
UNSUPPORTED("dbd08d8bpiw762wer960cicgj"); //     channel* cp;
UNSUPPORTED("cp4kv1fg74up3ubek2qyx8d25"); //     for (l1 = dtflatten (chans); l1; l1 = (((Dtlink_t*)(l1))->right)) {
UNSUPPORTED("950o75hi1nj794oxa101pt5t0"); // 	lp = ((chanItem*)l1)->chans;
UNSUPPORTED("71dqer4eeiqshvsh6rbj0kis5"); // 	for (l2 = dtflatten (lp); l2; l2 = (((Dtlink_t*)(l2))->right)) {
UNSUPPORTED("135wr0zp93ul7lqzg4608t7cy"); // 	    cp = (channel*)l2;
UNSUPPORTED("b9bbgy74zwpeh6zk1wsld8tx2"); // 	    if (cp->cnt)
UNSUPPORTED("37n1ytiss9w7m68eh2a5ahf44"); // 		add_edges_in_G(cp);
UNSUPPORTED("9pqd0punhe3g1up9qd8xxo652"); //    	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 emn5p2pij0zdp2hl3dyjefe36
// static segment* next_seg(segment* seg, int dir) 
public static Object next_seg(Object... arg) {
UNSUPPORTED("5hl6i4lkcopw8a7eywmfv5qyv"); // static segment*
UNSUPPORTED("6ov8c8zpwswdr0pj1gzchtfwx"); // next_seg(segment* seg, int dir)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6ri8jco2tikytst7ste1069yq"); //     assert(seg);
UNSUPPORTED("9yxubvqbrubzc3jcqnh9rpnmf"); //     if (!dir)
UNSUPPORTED("8dd4jx4ni1rdife7uof1nuwi9"); //         return(seg->prev);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("5c5he207xx8av0i912x9hawr2"); //         return(seg->next);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cs3u9dks2e2tzv2xu98p77w90
// static int propagate_prec(segment* seg, int prec, int hops, int dir) 
public static Object propagate_prec(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("tr6cv9djjf64a8yuh6q95jsp"); // propagate_prec(segment* seg, int prec, int hops, int dir)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dpr58lv2eeka28wequlp2xrzl"); //     int x;
UNSUPPORTED("ay64podpnebhkrkm9jq6r391s"); //     int ans=prec;
UNSUPPORTED("5ih4ipri3bjcgm61xdrc8ud4y"); //     segment* next;
UNSUPPORTED("cbeluzwuxiponx0az49dgkui4"); //     segment* current;
UNSUPPORTED("o8icbmalf0qgkleaf5kmc0ui"); //     current = seg;
UNSUPPORTED("yvzhy5oaj4saluubblynzmhv"); //     for(x=1;x<=hops;x++) {
UNSUPPORTED("9h1ebsbxexcfuink8l7o97r07"); // 	next = next_seg(current, dir);
UNSUPPORTED("2gosr1fjv173ysmvks0ybbgvt"); // 	if(!current->isVert) {
UNSUPPORTED("4v64kcbbxxsk315fnsi8ya930"); // 	    if(next->comm_coord==current->p.p1) {
UNSUPPORTED("6gkb69knhwko2b5weglnqbil9"); // 		if(current->l1==B_UP) ans *= -1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2o8y0vj6uxky1dqkbntiyges"); // 		if(current->l2==B_DOWN) ans *= -1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("4v64kcbbxxsk315fnsi8ya930"); // 	    if(next->comm_coord==current->p.p1) {
UNSUPPORTED("bzsdu04we48ifrljpe0az0g4j"); // 		if(current->l1==B_RIGHT) ans *= -1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("2q2w4w0cp24ko6jdyfchiodyq"); // 		if(current->l2==B_LEFT) ans *= -1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("10sew6kuf3dcpt2wqk10ptksi"); // 	current = next;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dhivmxtyss2i31bgun146pdvx"); //     return(ans);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bp53nx2ri4vm9anrojt7u7c2u
// static int is_parallel(segment* s1, segment* s2) 
public static Object is_parallel(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("ddygyr541ybi2kvxacd9n2m9j"); // is_parallel(segment* s1, segment* s2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("51or2j2w3vw4tt3ek3h4o7r2t"); //     assert (s1->comm_coord==s2->comm_coord);
UNSUPPORTED("9gufgbv51f5we43a6jx1uyqh9"); //     return ((s1->p.p1==s2->p.p1)&&
UNSUPPORTED("4cxvzf3a2g1ubcxf02lgmj2xh"); //             (s1->p.p2==s2->p.p2)&&
UNSUPPORTED("bzty8iew02v127lmuh0uuecmi"); //             (s1->l1==s2->l1)&&
UNSUPPORTED("8ilakshy93x5k5my1hijj1fpw"); //             (s1->l2==s2->l2));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5cy5xm5pageww1ken6y8teea4
// static pair  decide_point(segment* si, segment* sj, int dir1, int dir2) 
public static Object decide_point(Object... arg) {
UNSUPPORTED("f1byl3savsjxfld9h2fobwfo3"); // static pair 
UNSUPPORTED("7l2qer0l7u4rucnbo5kqj4vgc"); // decide_point(segment* si, segment* sj, int dir1, int dir2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dfk7h3txri9a3b4e7wkrzosju"); //     int prec, ans = 0, temp;
UNSUPPORTED("axbwlxs3xs2c5t2is2ke1ham5"); //     pair ret;
UNSUPPORTED("36dbtz3qjw44q55zoxfkmkykb"); //     segment* np1;
UNSUPPORTED("b95suv5ftmjl1s7ra4hrpi2db"); //     segment* np2;
UNSUPPORTED("afc9pg3q0h9ku0fioayd8nokq"); //     while ((np1 = next_seg(si,dir1)) && (np2 = next_seg(sj,dir2)) &&
UNSUPPORTED("a0ti049y8u1wrflxigii3nmuq"); // 	is_parallel(np1, np2)) {
UNSUPPORTED("aunxscflrorekqz8lv1sco0h1"); // 	ans++;
UNSUPPORTED("ok657kaukdbv5j6b55n9lu6c"); // 	si = np1;
UNSUPPORTED("7e7drakmuxwngzq18f1lbsn1g"); // 	sj = np2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4z9en58svufdyqqt3fyi3z80j"); //     if (!np1)
UNSUPPORTED("98h93apvsb643md8j6fnbun83"); // 	prec = 0;
UNSUPPORTED("8p3l7vg6fa42z5f9qr4kcwdbk"); //     else if (!np2)
UNSUPPORTED("9o9tshvh3903w7oznf4dx4ksw"); // 	assert(0); /* FIXME */
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("9sednl43da21gajvwpmk1pyle"); // 	temp = seg_cmp(np1, np2);
UNSUPPORTED("bojwazeev7k8w4xnz842s9dc6"); // 	prec = propagate_prec(np1, temp, ans+1, 1-dir1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c46mwi9n2qk3nz5az4iu04e2s"); //     ret.a = ans;
UNSUPPORTED("5vibf15888faapk0uzvvophg9"); //     ret.b = prec;
UNSUPPORTED("35t727xxibdqfcn6xwikpae5w"); //     return(ret);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9lqaqtqdkw9rqhmyltdrzs6xy
// static void set_parallel_edges (segment* seg1, segment* seg2, int dir1, int dir2, int hops,     maze* mp) 
public static Object set_parallel_edges(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3p9s2fmb4u10tilwejs0l9pp1"); // set_parallel_edges (segment* seg1, segment* seg2, int dir1, int dir2, int hops,
UNSUPPORTED("1rikd6gjoe3cvxggpkhj7z34d"); //     maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dpr58lv2eeka28wequlp2xrzl"); //     int x;
UNSUPPORTED("7pmetyg241ybw8hq3j61i271y"); //     channel* chan;
UNSUPPORTED("81v3bzorfnk43chwizhwmbb1n"); //     channel* nchan;
UNSUPPORTED("2qv0nvdra5zvo2b2ewep1ala4"); //     segment* prev1;
UNSUPPORTED("6s95g7xlatuyfm26klez18no0"); //     segment* prev2;
UNSUPPORTED("430rnwqjk85b89sh9kt07e6a0"); //     if (seg1->isVert)
UNSUPPORTED("4m8jh0bz7h9xwt55zl1stxnm8"); // 	chan = chanSearch(mp->vchans, seg1);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("4sukugpamblizlwievszn8nl3"); // 	chan = chanSearch(mp->hchans, seg1);
UNSUPPORTED("1elt521vxb74ewc1nqabq3gby"); //     insert_edge(chan->G, seg1->ind_no, seg2->ind_no);
UNSUPPORTED("2n7xloj5t3tkvc8h58imqk8td"); //     for (x=1;x<=hops;x++) {
UNSUPPORTED("iytowix4us30gxbxx69ke9rn"); // 	prev1 = next_seg(seg1, dir1);
UNSUPPORTED("9v39bw7og01cgrd9fnjubn0pi"); // 	prev2 = next_seg(seg2, dir2);
UNSUPPORTED("7q2x5kqynrhvcsduekv09bdn4"); // 	if(!seg1->isVert) {
UNSUPPORTED("7hs0lytek5umsxrj0a6jc8t5f"); // 	    nchan = chanSearch(mp->vchans, prev1);
UNSUPPORTED("7lpjkbjai262xfygabcg0nwnu"); // 	    if(prev1->comm_coord==seg1->p.p1) {
UNSUPPORTED("13ucbaachj06c0mvgupkiswfl"); // 		if(seg1->l1==B_UP) {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8vywd7o1vdaaij4z9jtx8pi63"); // 		if(seg1->l2==B_UP) {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("173hlqu5ukdae2k6wt7ch1x8p"); // 			insert_edge(nchan->G,prev1->ind_no, prev2->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("7lby5m10dvjryzfv32elha7cq"); // 			insert_edge(nchan->G,prev2->ind_no, prev1->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("9tap010f91m7yxd7a77xcc6gy"); // 	    nchan = chanSearch(mp->hchans, prev1);
UNSUPPORTED("7lpjkbjai262xfygabcg0nwnu"); // 	    if(prev1->comm_coord==seg1->p.p1) {
UNSUPPORTED("bv0pfzaflx2zmsfq7fl4fumox"); // 		if(seg1->l1==B_LEFT) {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("nvpd9xg7sp7w29u53zc5n7om"); // 		if(seg1->l2==B_LEFT) {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("e14484zac1ca6tr55nq056y2s"); // 		    if(edge_exists(chan->G, seg1->ind_no, seg2->ind_no))
UNSUPPORTED("9jmkjj0x67qxhr4shs6ibvgk3"); // 			insert_edge(nchan->G, prev1->ind_no, prev2->ind_no);
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("850me5iwi1gvweet5jzd7lqwy"); // 			insert_edge(nchan->G, prev2->ind_no, prev1->ind_no);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3d9r09rxjpct04hoj66705cp1"); // 	chan = nchan;
UNSUPPORTED("2c2yvnaxh16x89y9hq4w5yk1m"); // 	seg1 = prev1;
UNSUPPORTED("11ckhoc2b2u2dsurx9qg07st6"); // 	seg2 = prev2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 38cpeb1o8uvzyjd93p234ds9f
// static void removeEdge(segment* seg1, segment* seg2, int dir, maze* mp) 
public static Object removeEdge(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("6sgltkpz58p9krap0y1sbvj6k"); // removeEdge(segment* seg1, segment* seg2, int dir, maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("szrq58jnhhhevz1yqgbr4ck2"); //     segment* ptr1;
UNSUPPORTED("31dak66p4n83b984pfixwotla"); //     segment* ptr2;
UNSUPPORTED("7pmetyg241ybw8hq3j61i271y"); //     channel* chan;
UNSUPPORTED("3f6utfpnejoda18juym1fb1ko"); //     ptr1 = seg1;
UNSUPPORTED("4zdcf2h90axz04i03w6ttg5k4"); //     ptr2 = seg2;
UNSUPPORTED("bnoomvnxrkeudsz6vyirx44uc"); //     while(is_parallel(ptr1, ptr2)) {
UNSUPPORTED("bgoj9z4ow368rwa0wiqpvom0o"); // 	ptr1 = next_seg(ptr1, 1);
UNSUPPORTED("6k9a6lk6tlg00as6qklki8xiz"); // 	ptr2 = next_seg(ptr2, dir);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ai7oa06ous30ke73ob1k1h6go"); //     if(ptr1->isVert)
UNSUPPORTED("7qlxjn3a1d6nys3x5s0ngrs5q"); // 	chan = chanSearch(mp->vchans, ptr1);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("79qdq8et7m2rlyh1z4wss3y1n"); // 	chan = chanSearch(mp->hchans, ptr1);
UNSUPPORTED("1f4mrzxo4q83k5yazad1hezbb"); //     remove_redge (chan->G, ptr1->ind_no, ptr2->ind_no);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eiig50kmclab4ei88pq4soo34
// static void addPEdges (channel* cp, maze* mp) 
public static Object addPEdges(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("68fs5heyk95e6jn74c6cgq01t"); // addPEdges (channel* cp, maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8nbbb2xvtigkcv51627tsphb7"); //     int i,j;
UNSUPPORTED("691z894ert27013tlk15hh2qh"); //     /* dir[1,2] are used to figure out whether we should use prev 
UNSUPPORTED("d52bbwvlo6xaftcxmyc07f9w5"); //      * pointers or next pointers -- 0 : decrease, 1 : increase
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("2ihd1217lj7h5zt9n0d1q5cna"); //     int dir;
UNSUPPORTED("2ur22e7h79m2ohnopwac8a76b"); //     /* number of hops along the route to get to the deciding points */
UNSUPPORTED("8czu5hbarwrdzcuxp2djvv4rp"); //     pair hops;
UNSUPPORTED("3cz2vt0y2kw2ogxww3u2j1kl6"); //     /* precedences of the deciding points : same convention as 
UNSUPPORTED("1tpdmm5wve9rduvay2ahgvle9"); //      * seg_cmp function 
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("522qtrew5eqvwpn5wxnu2k9dn"); //     int prec1, prec2;
UNSUPPORTED("1fnwnz6zqrixdpfmd5wf4hbj"); //     pair p;
UNSUPPORTED("dmbfnx3582vhxsi4o65ruesyn"); //     rawgraph* G = cp->G;
UNSUPPORTED("7hb8fcc74usveupz7kgy2qol3"); //     segment** segs = cp->seg_list;
UNSUPPORTED("39nnkurg9t4qq74t5jz3zkjrl"); //     for(i=0;i+1<cp->cnt;i++) {
UNSUPPORTED("6a9jbb5aterxr0g0hiu2pyhld"); // 	for(j=i+1;j<cp->cnt;j++) {
UNSUPPORTED("3dvaxn4ireem858ssmcsr58hn"); // 	    if (!edge_exists(G,i,j) && !edge_exists(G,j,i)) {
UNSUPPORTED("dpy9omkq97zyoo9w1inx3pbm9"); // 		if (is_parallel(segs[i], segs[j])) {
UNSUPPORTED("6ig9p93cmed2v70a0npblnlxp"); // 		/* get_directions */
UNSUPPORTED("amvy33u98h6r9qq7ugnuf2nlz"); // 		    if(segs[i]->prev==0) {
UNSUPPORTED("cc25alanei7n0arh5bqailcv4"); // 			if(segs[j]->prev==0)
UNSUPPORTED("27dopm02s7e623flmf0oyucf6"); // 			    dir = 0;
UNSUPPORTED("cqgi8f4d37bqva8z6bx5rvn7w"); // 			else
UNSUPPORTED("afmb9itetac5509pz0vzquffn"); // 			    dir = 1;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6tva5tknk59csaydjoaehlxqy"); // 		    else if(segs[j]->prev==0) {
UNSUPPORTED("bf3scu300t9b1jl5wrxkahki9"); // 			dir = 1;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("cphaexi33y32dnefwtu3jsom4"); // 		    else {
UNSUPPORTED("ay1tigliz8fu739c3s4lnp4c8"); // 			if(segs[i]->prev->comm_coord==segs[j]->prev->comm_coord)
UNSUPPORTED("27dopm02s7e623flmf0oyucf6"); // 			    dir = 0;
UNSUPPORTED("cqgi8f4d37bqva8z6bx5rvn7w"); // 			else
UNSUPPORTED("afmb9itetac5509pz0vzquffn"); // 			    dir = 1;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("2h9ts7wzrya1rr66lklhh0rda"); // 		    p = decide_point(segs[i], segs[j], 0, dir);
UNSUPPORTED("y15vvy2yhgh75hmjibm6d0tv"); // 		    hops.a = p.a;
UNSUPPORTED("6y60dh26moscelbp5xfex7fy7"); // 		    prec1 = p.b;
UNSUPPORTED("1fwna4qrlam89qjgrsjnzwkuc"); // 		    p = decide_point(segs[i], segs[j], 1, 1-dir);
UNSUPPORTED("7ck6vr1ic310ui6v7y8lcpwbh"); // 		    hops.b = p.a;
UNSUPPORTED("564q1rsht56yza954y2x1jtm"); // 		    prec2 = p.b;
UNSUPPORTED("dtcs9mr4kwcta690gbrwlt910"); // 		    switch(prec1) {
UNSUPPORTED("7xhopigym72b3wkri4tfjy1dy"); // 		    case -1 :
UNSUPPORTED("enqa6da4597ij1v8v5x3f251q"); // 			set_parallel_edges (segs[j], segs[i], dir, 0, hops.a, mp);
UNSUPPORTED("6diyb5ikl44b7ym0eennr0yq6"); // 			set_parallel_edges (segs[j], segs[i], 1-dir, 1, hops.b, mp);
UNSUPPORTED("5r2jji4zmdfrbfwx99e4tv69i"); // 			if(prec2==1)
UNSUPPORTED("8x6usd0clpqn19g8onqqgzjn4"); // 			    removeEdge (segs[i], segs[j], 1-dir, mp);
UNSUPPORTED("a5064jph9xpw67dcun2my4mcm"); // 			break;
UNSUPPORTED("22xfu28lqt3r284k47pbuy7cz"); // 		    case 0 :
UNSUPPORTED("4dfzzbp1vuxk64hoh65xa34yc"); // 			switch(prec2) {
UNSUPPORTED("4gsejoo0eevrt7un6e6qn989l"); // 			case -1:
UNSUPPORTED("emhqawmwj90tfh02qk8r1pk0v"); // 			    set_parallel_edges (segs[j], segs[i], dir, 0, hops.a, mp);
UNSUPPORTED("9n3o6zltbzimqd4msu9ngqtyd"); // 			    set_parallel_edges (segs[j], segs[i], 1-dir, 1, hops.b, mp);
UNSUPPORTED("a4shncx2bmdaa04gvja9v2blz"); // 			    break;
UNSUPPORTED("9bar4olfqd3r7389ggqw5djvq"); // 			case 0 :
UNSUPPORTED("2s64t77sytwec1d6n2jbf74wc"); // 			    set_parallel_edges (segs[i], segs[j], 0, dir, hops.a, mp);
UNSUPPORTED("6r0468haso97c6g4zkdfg9kt2"); // 			    set_parallel_edges (segs[i], segs[j], 1, 1-dir, hops.b, mp);
UNSUPPORTED("a4shncx2bmdaa04gvja9v2blz"); // 			    break;
UNSUPPORTED("4yd30eh9icuxnqotseihb7l4w"); // 			case 1:
UNSUPPORTED("2s64t77sytwec1d6n2jbf74wc"); // 			    set_parallel_edges (segs[i], segs[j], 0, dir, hops.a, mp);
UNSUPPORTED("6r0468haso97c6g4zkdfg9kt2"); // 			    set_parallel_edges (segs[i], segs[j], 1, 1-dir, hops.b, mp);
UNSUPPORTED("a4shncx2bmdaa04gvja9v2blz"); // 			    break;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("a5064jph9xpw67dcun2my4mcm"); // 			break;
UNSUPPORTED("7725wrsrh4ancb1bh89l3x12r"); // 		    case 1 :
UNSUPPORTED("3ubmnbt9kypi1ptpye7w37vkl"); // 			set_parallel_edges (segs[i], segs[j], 0, dir, hops.a, mp);
UNSUPPORTED("4s97q82e6wclxf2y8jhzfh14k"); // 			set_parallel_edges (segs[i], segs[j], 1, 1-dir, hops.b, mp);
UNSUPPORTED("5zh7u5zg9v4zshpcfsj3brwfk"); // 			if(prec2==-1)
UNSUPPORTED("8x6usd0clpqn19g8onqqgzjn4"); // 			    removeEdge (segs[i], segs[j], 1-dir, mp);
UNSUPPORTED("a5064jph9xpw67dcun2my4mcm"); // 			break;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 98d1c54yaxchy0ms0e99lst7s
// static void  add_p_edges (Dt_t* chans, maze* mp) 
public static Object add_p_edges(Object... arg) {
UNSUPPORTED("59dl3yc4jbcy2pb7j1njhlybi"); // static void 
UNSUPPORTED("5ebboq5zrhtd0rfaeawkl5s2f"); // add_p_edges (Dt_t* chans, maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a4p8766eh6ra21i7u5o9nf6rm"); //     Dt_t* lp;
UNSUPPORTED("1mqjeo37v4tmo6kht0rep1tht"); //     Dtlink_t* l1;
UNSUPPORTED("eiqtkkndttzmunuxtnm99mkpy"); //     Dtlink_t* l2;
UNSUPPORTED("cp4kv1fg74up3ubek2qyx8d25"); //     for (l1 = dtflatten (chans); l1; l1 = (((Dtlink_t*)(l1))->right)) {
UNSUPPORTED("950o75hi1nj794oxa101pt5t0"); // 	lp = ((chanItem*)l1)->chans;
UNSUPPORTED("71dqer4eeiqshvsh6rbj0kis5"); // 	for (l2 = dtflatten (lp); l2; l2 = (((Dtlink_t*)(l2))->right)) {
UNSUPPORTED("di14tjuua1p9n96grrfboue6o"); // 	    addPEdges ((channel*)l2, mp);
UNSUPPORTED("9pqd0punhe3g1up9qd8xxo652"); //    	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bxsh28469kd6r9fen9l7v8f2w
// static void assignTracks (int nrtes, route* route_list, maze* mp) 
public static Object assignTracks(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("e80kmdu0n8gwftxrcchppqznf"); // assignTracks (int nrtes, route* route_list, maze* mp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("26qmbe7syf8caazopicbw1o73"); //     /* Create the graphs for each channel */
UNSUPPORTED("5kswlmxeu7z6znausks9omj52"); //     create_graphs(mp->hchans);
UNSUPPORTED("1qo05yt4o0eiyn1axxxfiecee"); //     create_graphs(mp->vchans);
UNSUPPORTED("9k9f44tgb14d6rvcxauyk4kg3"); //     /* add edges between non-parallel segments */
UNSUPPORTED("2bpipgzzkyey9l1jwcx2ytvc0"); //     add_np_edges(mp->hchans);
UNSUPPORTED("7fju2e1xyweu6benhq0akolab"); //     add_np_edges(mp->vchans);
UNSUPPORTED("a2akrqgy2t88akl274eg0somv"); //     /* add edges between parallel segments + remove appropriate edges */
UNSUPPORTED("5zdimrk15jv0lju7twpsqz29m"); //     add_p_edges(mp->hchans, mp);
UNSUPPORTED("9bqggms2mbxxvkbh0u4jidv0l"); //     add_p_edges(mp->vchans, mp);
UNSUPPORTED("9envbpdpd498dstkls2pqtswo"); //     /* Assign the tracks after a top sort */
UNSUPPORTED("10z0c5n6u2v6o7td59aig5lfd"); //     assignTrackNo (mp->hchans);
UNSUPPORTED("t8kw88xoabf7nbvqv8ds7www"); //     assignTrackNo (mp->vchans);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 udi38qq74cw0pvrqn6g1yle5
// static double vtrack (segment* seg, maze* m) 
public static Object vtrack(Object... arg) {
UNSUPPORTED("lt6cippjix5bbvyhkcpl8g7g"); // static double
UNSUPPORTED("19x5ivyvkfhintu5d2rpflcvo"); // vtrack (segment* seg, maze* m)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1xvv17gm5rfjinvy3gmnsc2ke"); //   channel* chp = chanSearch(m->vchans, seg);
UNSUPPORTED("76k3vszrhcky681kek2r7d5h9"); //   double f = ((double)seg->track_no)/(chp->cnt+1); 
UNSUPPORTED("2ug8vx71z1uw4iw38ehgqdc7g"); //   double left = chp->cp->bb.LL.x;
UNSUPPORTED("f2ozrx5u8liyshw7972fqzlry"); //   double right = chp->cp->bb.UR.x;
UNSUPPORTED("2gju483ltduiu0c964t9ir9wa"); //   return left + f*(right-left);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3cqaxjb6pietkwiyugn6h26kd
// static int htrack (segment* seg, maze* m) 
public static Object htrack(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("a2hs7lm69vuj9mt0513f8z4oz"); // htrack (segment* seg, maze* m)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("40ntn3e9uwr1zuuu2sbtdsa6w"); //   channel* chp = chanSearch(m->hchans, seg);
UNSUPPORTED("erszzli51plsbzcgsn48c0180"); //   double f = 1.0 - ((double)seg->track_no)/(chp->cnt+1); 
UNSUPPORTED("4i67gabp0r3u9scclorhs3ktm"); //   double lo = chp->cp->bb.LL.y;
UNSUPPORTED("4p8tiujx3t2fm7is5dxnoqxhe"); //   double hi = chp->cp->bb.UR.y;
UNSUPPORTED("83od9je177vrwp4ps0kbd4szw"); //   return lo + f*(hi-lo);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 83e5ejcqta67f2p2asgbtfaoi
// static pointf addPoints(pointf p0, pointf p1) 
public static Object addPoints(Object... arg) {
UNSUPPORTED("2zzd7mrm2u540dwuyzehozffj"); // static pointf
UNSUPPORTED("682qkd4mibtnxpxyv5orrsjtx"); // addPoints(pointf p0, pointf p1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f5hox117bxx1r58lcs3udisoe"); //     p0.x += p1.x;
UNSUPPORTED("ujsxixrtu1t8f32elbvw8dwa"); //     p0.y += p1.y;
UNSUPPORTED("3eojsvh2rm970fcai0twfpvnl"); //     return p0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a0iuxsb2zu9l7k8a3v4ixv00e
// static void attachOrthoEdges (Agraph_t* g, maze* mp, int n_edges, route* route_list, splineInfo *sinfo, epair_t es[], int doLbls) 
public static Object attachOrthoEdges(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("cspgusupjxy5utjicgxrmrq7w"); // attachOrthoEdges (Agraph_t* g, maze* mp, int n_edges, route* route_list, splineInfo *sinfo, epair_t es[], int doLbls)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bzsk5t9x5gtbz8fnomb2f9s4v"); //     int irte = 0;
UNSUPPORTED("9guu55302lj09t48j16nqyxbq"); //     int i, ipt, npts;
UNSUPPORTED("5b40dnpfc88et1cia7fuj40yb"); //     pointf* ispline = 0;
UNSUPPORTED("dprplco8u5mu9i11jo6go6bcy"); //     int splsz = 0;
UNSUPPORTED("2asgcu0nywvhoazecb6ofcubs"); //     pointf p, p1, q1;
UNSUPPORTED("76tjoqkn7jsh2pn0trcuu3nvs"); //     route rte;
UNSUPPORTED("9ixgpcejsl25oetqsy1ewnfnu"); //     segment* seg;
UNSUPPORTED("6yramhpyls8c6kexupyqip8oq"); //     Agedge_t* e;
UNSUPPORTED("ef044lvbrsjiucn4ouhr6x2qb"); //     textlabel_t* lbl;
UNSUPPORTED("77xmhk7dbpg0c00j8666pclyi"); //     for (; irte < n_edges; irte++) {
UNSUPPORTED("bv52iygkt2gg4gzoyp4c9wk6b"); // 	e = es[irte].e;
UNSUPPORTED("ay9p5i1iubyhzon5z6a3jrw9n"); // 	p1 = addPoints(ND_coord(agtail(e)), ED_tail_port(e).p);
UNSUPPORTED("4z87m5xmyxfqf4gbe4pskna9n"); // 	q1 = addPoints(ND_coord(aghead(e)), ED_head_port(e).p);
UNSUPPORTED("8wciy7quxxigt8dwxr9ctgk2d"); // 	rte = route_list[irte];
UNSUPPORTED("6ed9g47vzboko6g0h5y17e1ou"); // 	npts = 1 + 3*rte.n;
UNSUPPORTED("efs0cd713nnm7zxrgb0l11wz"); // 	if (npts > splsz) {
UNSUPPORTED("5d7b0ws1zxf4b536zojp5j1gy"); // 		if (ispline) free (ispline);
UNSUPPORTED("dqxwk098ekx7gst5gdhr310bc"); // 		ispline = (pointf*)gmalloc((npts)*sizeof(pointf));
UNSUPPORTED("e4i1tff7gmkjg8kevj7hw8gs1"); // 		splsz = npts;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1ry9c35mgaxl2ivjx1h2v5cr0"); // 	seg = rte.segs;
UNSUPPORTED("6pz2d4cdoe2l7351iysy6x98p"); // 	if (seg->isVert) {
UNSUPPORTED("91n0eh3r7wj0n97kdj9ld31kj"); // 		p.x = vtrack(seg, mp);
UNSUPPORTED("45fe06ci8qec4u7eii52becqh"); // 		p.y = p1.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("9t5iimum2tu1jo5tlcx6m7fhq"); // 		p.y = htrack(seg, mp);
UNSUPPORTED("8je4vuu6jnq5fu4znanftosq8"); // 		p.x = p1.x;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("76sz3tzo9psbgiwiel6nlkhyd"); // 	ispline[0] = ispline[1] = p;
UNSUPPORTED("2xvzqz1y0roznr4j0qz8gdn97"); // 	ipt = 2;
UNSUPPORTED("ehl4i682tsgw4ev386bxd9bvk"); // 	for (i = 1;i<rte.n;i++) {
UNSUPPORTED("dqq6tgen8yf0v8lzn6zc3vwpi"); // 		seg = rte.segs+i;
UNSUPPORTED("oscgdxl7tuvnm27y3ajyf90a"); // 		if (seg->isVert)
UNSUPPORTED("5ordk0g9tt85vst6tn9y7yw5e"); // 		    p.x = vtrack(seg, mp);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("2e3jl8piax6scxsrx2jl4mn0m"); // 		    p.y = htrack(seg, mp);
UNSUPPORTED("5zlu7lc3edxwcalx4u2toh91o"); // 		ispline[ipt+2] = ispline[ipt+1] = ispline[ipt] = p;
UNSUPPORTED("1h4km88eju8ubv62x2a9mwdzm"); // 		ipt += 3;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6pz2d4cdoe2l7351iysy6x98p"); // 	if (seg->isVert) {
UNSUPPORTED("91n0eh3r7wj0n97kdj9ld31kj"); // 		p.x = vtrack(seg, mp);
UNSUPPORTED("obj8bne89a63k09t0vg3ksax"); // 		p.y = q1.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("9t5iimum2tu1jo5tlcx6m7fhq"); // 		p.y = htrack(seg, mp);
UNSUPPORTED("cwlvttt636ou35je24a6zn8fw"); // 		p.x = q1.x;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6883d1dn4elcixp0dy2yzu4ck"); // 	ispline[ipt] = ispline[ipt+1] = p;
UNSUPPORTED("e7wcbocwpb0ubwm19n3qdsfdu"); // 	if (Verbose > 1)
UNSUPPORTED("6243buyivvpmixvapfka7f2n3"); // 	    fprintf(stderr, "ortho %s %s\n", agnameof(agtail(e)),agnameof(aghead(e)));
UNSUPPORTED("aiznf16jsmgfywhbln59n8weh"); // 	clip_and_install(e, aghead(e), ispline, npts, sinfo);
UNSUPPORTED("5odapjhwjcy61acm5m79qw8fs"); // 	if (doLbls && (lbl = ED_label(e)) && !lbl->set)
UNSUPPORTED("2bpsdc28nv5l876oe7rqtanlx"); // 	    addEdgeLabels(g, e, p1, q1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c0bfcb8svwdkq95cokqra1pi2"); //     free(ispline);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e71zkztxzapvl1mgiq8p5gzzs
// static int edgeLen (Agedge_t* e) 
public static Object edgeLen(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("4eh66xikxzh6l4lhjcmvtv8cn"); // edgeLen (Agedge_t* e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1vcqzavbye1orax38cfndljrr"); //     pointf p = ND_coord(agtail(e));
UNSUPPORTED("d1qthvuzf3w3d5qujx199xm23"); //     pointf q = ND_coord(aghead(e));
UNSUPPORTED("7b42di3iyhbsuhhw6yaw6539m"); //     return (int)DIST2(p,q);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7afj2qnluiytxt8hvcnhreb2
// static int edgecmp(epair_t* e0, epair_t* e1) 
public static Object edgecmp(Object... arg) {
UNSUPPORTED("4biy4n19ul16vp6vcv9xmui1i"); // static int edgecmp(epair_t* e0, epair_t* e1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dlcw570tkt0usu32yvwm8cd3q"); //     return (e0->d - e1->d);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 v4bpabyggw2gk3qw5qiapig9
// static boolean spline_merge(node_t * n) 
public static Object spline_merge(Object... arg) {
UNSUPPORTED("676qpx3n8ouo1wjyumn6zmyxp"); // static boolean spline_merge(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 90yjqn8lf9ig968uma0q5sh34
// static boolean swap_ends_p(edge_t * e) 
public static Object swap_ends_p(Object... arg) {
UNSUPPORTED("3fjj0uj2p25pitmluszw94p02"); // static boolean swap_ends_p(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 9nmy74adtwr7rthq8xzoptzrf
// static splineInfo sinfo = 




//3 9rb5k3o906weqm31n2i8lv5so
// void orthoEdges (Agraph_t* g, int doLbls) 
public static Object orthoEdges(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("9ttq9irkfbqsx0q7wcnetzy74"); // orthoEdges (Agraph_t* g, int doLbls)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2omyui5qg1nq6by9uehikdjsy"); //     sgraph* sg;
UNSUPPORTED("7cg9ke1jhysixb020x5an69jv"); //     maze* mp;
UNSUPPORTED("cu6yxi1acnq5g0btge7zjdkm0"); //     int n_edges;
UNSUPPORTED("9ro0xuxbh1xd61xqzcaaadikl"); //     route* route_list;
UNSUPPORTED("bl7f59ax29638dd6kka6eopr"); //     int i, gstart;
UNSUPPORTED("ci2zh69w6nhi0q816i1ixuy9k"); //     Agnode_t* n;
UNSUPPORTED("6yramhpyls8c6kexupyqip8oq"); //     Agedge_t* e;
UNSUPPORTED("7kt5z7l3gj30fc2g11brz7hn3"); //     snode* sn;
UNSUPPORTED("4lm0qpz2knefonz0ik550n9qi"); //     snode* dn;
UNSUPPORTED("f0kzqokjxzhga3oyrmev903hw"); //     epair_t* es = (epair_t*)gmalloc((agnedges(g))*sizeof(epair_t));
UNSUPPORTED("ih7y3b036cc7gqpd8tqtpr9q"); //     cell* start;
UNSUPPORTED("1jv0h6v7ggk7dfm6oqzs4u3lj"); //     cell* dest;
UNSUPPORTED("4vt2v1dwhho9ajlyh6nnd1d8h"); //     PointSet* ps;
UNSUPPORTED("ef044lvbrsjiucn4ouhr6x2qb"); //     textlabel_t* lbl;
UNSUPPORTED("e800kdbiqq8i4me0u6fwkd370"); //     if (Concentrate) 
UNSUPPORTED("7r91fjc8u70111hbxlvqd7ktw"); // 	ps = newPS();
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("9wn8pvvih4vrawe5alvfhm9yn"); // 	char* s = agget(g, "odb");
UNSUPPORTED("b8c7bwegidsegu11m5xttb11j"); //         char c;
UNSUPPORTED("1jro8v0ct38i7wcl91bk3ic3e"); // 	odb_flags = 0;
UNSUPPORTED("3jmzbbt2cpizqt2dufx87w3al"); // 	if (s && (*s != '\0')) {
UNSUPPORTED("7ns3g2gy7v368e78t92w9xfwe"); // 	    while ((c = *s++)) {
UNSUPPORTED("2mo7busikqaj4ukkdtf5zm8ik"); // 		switch (c) {
UNSUPPORTED("ahx4iaogjvo14pciwab02xzsk"); // 		case 'c' :
UNSUPPORTED("eai7hbieqphsrvjgzprem79fi"); // 		    odb_flags |= 8;     // emit channel graph 
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("3dcblef3ry3y3dukenp1w4dfg"); // 		case 'i' :
UNSUPPORTED("8f1c36fjkua8s8jgm0zigqvgv"); // 		    odb_flags |= (2|16);  // emit search graphs
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("a5nt85f0rq0jcipae19734270"); // 		case 'm' :
UNSUPPORTED("6jh77aqljluu3rx42j6uz2wog"); // 		    odb_flags |= 1;      // emit maze
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("ci305ii9fygih5qjkc9cb43gn"); // 		case 'r' :
UNSUPPORTED("db7u5t19gdpwvuygsvb2vjc3d"); // 		    odb_flags |= 4;     // emit routes in maze
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("bt1b5biu9zb9zccgj0cr658xz"); // 		case 's' :
UNSUPPORTED("3uqs3qb95942pbzmg53uqc42a"); // 		    odb_flags |= 2;    // emit search graph 
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("71u00vuawtzqg4gz0lsai1m4a"); //     if (doLbls) {
UNSUPPORTED("10xzs4g55w92883ej7ty5glms"); // 	agerr(AGWARN, "Orthogonal edges do not currently handle edge labels. Try using xlabels.\n");
UNSUPPORTED("ditf90dyqp9mua87s8pumvi32"); // 	doLbls = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("etv0iiw2ala4rlayc040wzggy"); //     mp = mkMaze (g, doLbls);
UNSUPPORTED("4mmgpe06gcmxv31p3y6ipyf5s"); //     sg = mp->sg;
UNSUPPORTED("emjkv8wxfnbb355266hsa90ni"); //     if (odb_flags & 2) emitSearchGraph (stderr, sg);
UNSUPPORTED("6kfj8qzl4paerer9gefcgsf7h"); //     /* store edges to be routed in es, along with their lengths */
UNSUPPORTED("c8047kmsz3vp9lv0w3cw7yas8"); //     n_edges = 0;
UNSUPPORTED("ef1ej9licmntta3ibshtov76v"); //     for (n = agfstnode (g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("bojcrt9w5b2e0csoijdzfkvq9"); //         for (e = agfstout(g, n); e; e = agnxtout(g,e)) {
UNSUPPORTED("2xpa4q3kqyqhcc2z6j7asagqt"); // 	    if ((Nop == 2) && ED_spl(e)) continue;
UNSUPPORTED("41rwk0z8paqjc2mcm2a1byosa"); // 	    if (Concentrate) {
UNSUPPORTED("9luudm8wqdv4jx4s0ustrk6br"); // 		int ti = AGSEQ(agtail(e));
UNSUPPORTED("c976iy0d8sqzcrczrfzsoevxv"); // 		int hi = AGSEQ(aghead(e));
UNSUPPORTED("48wqpeunj9fn8sno227cy07cn"); // 		if (ti <= hi) {
UNSUPPORTED("4xk5rpi8h5diyml5ko1c9pb4o"); // 		    if (isInPS (ps,ti,hi)) continue;
UNSUPPORTED("eufspanc57875qmod8zz8vvj"); // 		    else addPS (ps,ti,hi);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("9g3uik8w4uis1skxgzm9wfjkr"); // 		    if (isInPS (ps,hi,ti)) continue;
UNSUPPORTED("ckw8orokucnmrq9rly0vp6483"); // 		    else addPS (ps,hi,ti);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("disvm7blt6qwq36jp0pcx05p8"); // 	    es[n_edges].e = e;
UNSUPPORTED("8treoc9xy0md6z4ggqb44auyg"); // 	    es[n_edges].d = edgeLen (e);
UNSUPPORTED("e9hecjb6c3qp2o2tya683ai62"); // 	    n_edges++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("191cpw36uzdm3gqmxfphm1xbu"); //     route_list = (route*)zmalloc((n_edges)*sizeof(route));
UNSUPPORTED("2gq02agovwtyskbeh5vefmslh"); //     qsort((char *)es, n_edges, sizeof(epair_t), (qsort_cmpf) edgecmp);
UNSUPPORTED("brl796jea4mql81k1b7ershdc"); //     gstart = sg->nnodes;
UNSUPPORTED("cs9x91hl58n3ub79e9ugvdn4x"); //     PQgen (sg->nnodes+2);
UNSUPPORTED("5d43q0fg31x5nwtqm0ata3zjz"); //     sn = &sg->nodes[gstart];
UNSUPPORTED("66qkjbw0jrnak7d2s42h0jg70"); //     dn = &sg->nodes[gstart+1];
UNSUPPORTED("6gdenpywxn3p3xidvp7z66cf1"); //     for (i = 0; i < n_edges; i++) {
UNSUPPORTED("49gwq06gbm2qhfpy2s6ar6gje"); // 	if ((i > 0) && (odb_flags & 16)) emitSearchGraph (stderr, sg);
UNSUPPORTED("a80m8bfu7lu477kat2scc0z01"); // 	e = es[i].e;
UNSUPPORTED("cod1w9an9gcxijan671a4atdt"); //         start = ((cell*)ND_alg(agtail(e)));
UNSUPPORTED("emi0a8wy2fz98d3tyq50nu13j"); //         dest = ((cell*)ND_alg(aghead(e)));
UNSUPPORTED("3n4xox18pqitgomsjx10rjonm"); // 	if (doLbls && (lbl = ED_label(e)) && lbl->set) {
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("596hk3sqi3zgv5ypngx0bx7mq"); // 	    if (start == dest)
UNSUPPORTED("d0zcyw4sme2i3mtmhe3hlbrmc"); // 		addLoop (sg, start, dn, sn);
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("3uirdblzfj10bp724w2p35bup"); //        		addNodeEdges (sg, dest, dn);
UNSUPPORTED("52k51th88uehvgef7fd4gfbu6"); // 		addNodeEdges (sg, start, sn);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dd3ztkz69q7dffhj4ihaizk6s"); //        	    if (shortPath (sg, dn, sn)) goto orthofinish;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("nl2nzce1mtx118xuy1w9uq6v"); //        	route_list[i] = convertSPtoRoute(sg, sn, dn);
UNSUPPORTED("bt25p7tmlvva5imw8gh97toy4"); //        	reset (sg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9br7yf8ixw2vq0ih496kr0a3p"); //     PQfree ();
UNSUPPORTED("1h1p1wlchwso3mavejmc8cyxv"); //     mp->hchans = extractHChans (mp);
UNSUPPORTED("dbw3nbw93ongdgdhdtscvuxb8"); //     mp->vchans = extractVChans (mp);
UNSUPPORTED("9r77bxpkuu3nsxa0nttqjh0hl"); //     assignSegs (n_edges, route_list, mp);
UNSUPPORTED("bbjzso9cox08g0ax9rs132q90"); //     if (setjmp(jbuf))
UNSUPPORTED("uycdh1qmy4ty2pb0zgz2v78a"); // 	goto orthofinish;
UNSUPPORTED("41rz8iqv9lm60deshcar8hjxa"); //     assignTracks (n_edges, route_list, mp);
UNSUPPORTED("as0ctk4holgq03a3hl4doch8d"); //     if (odb_flags & 4) emitGraph (stderr, mp, n_edges, route_list, es);
UNSUPPORTED("c1p0r114sfd1lvyamywzbro10"); //     attachOrthoEdges (g, mp, n_edges, route_list, &sinfo, es, doLbls);
UNSUPPORTED("7e5qeaxbygknkdgnv53oq2u1e"); // orthofinish:
UNSUPPORTED("2o1inibord55o8k32yn7kfrwl"); //     if (Concentrate)
UNSUPPORTED("ghiimsk3vbhjzh67h8pwbk95"); // 	freePS (ps);
UNSUPPORTED("an57w0rx9dpimg8nf764q8n97"); //     for (i=0; i < n_edges; i++)
UNSUPPORTED("3fz1dnh6u4wislz0u5bxb5ugh"); // 	free (route_list[i].segs);
UNSUPPORTED("570o41p0l7jgclg83j647v910"); //     free (route_list);
UNSUPPORTED("3pnwqn95p3wz9ni8ys8j0u5hv"); //     freeMaze (mp);
UNSUPPORTED("d507lsxnfvq57ccke7bica6zl"); //     free (es);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 d9p47q2g7x3b0asfu3jvceq3l
// static char* prolog2 = 


//1 475m5xvb3loybtd1l2z8rftoq
// static char* epilog2 = 




//3 dx1s3vj94skvk7pmrz4s875l9
// static point coordOf (cell* cp, snode* np) 
public static Object coordOf(Object... arg) {
UNSUPPORTED("5ji3k3tukj2uxmd2ympfpwtml"); // static point
UNSUPPORTED("dd9sjhv4zfl34767d8g79k27"); // coordOf (cell* cp, snode* np)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("41nrdsugvfgw23s93g0dhizkn"); //     point p;
UNSUPPORTED("703pcgiucdy0i6g6mphde8dno"); //     if (cp->sides[M_TOP] == np) {
UNSUPPORTED("68easn97lilb2aqdb8frrsss1"); // 	p.x = (cp->bb.LL.x + cp->bb.UR.x)/2;
UNSUPPORTED("6aab1cx7aql0barme21mg8zqj"); // 	p.y = cp->bb.UR.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("835f42b2b380czfp1110k8i9k"); //     else if (cp->sides[M_BOTTOM] == np) {
UNSUPPORTED("68easn97lilb2aqdb8frrsss1"); // 	p.x = (cp->bb.LL.x + cp->bb.UR.x)/2;
UNSUPPORTED("2hak744886lhw45wamafgo56x"); // 	p.y = cp->bb.LL.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5ik2pzxq0pwcq7l7og6ti9ee3"); //     else if (cp->sides[M_LEFT] == np) {
UNSUPPORTED("1t69shrxanujd9qd85gq4qeng"); // 	p.y = (cp->bb.LL.y + cp->bb.UR.y)/2;
UNSUPPORTED("4k1o0atpyqo35gcbuse4az3rg"); // 	p.x = cp->bb.LL.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4u9elo3yvf0zruskl3zjt2b2f"); //     else if (cp->sides[M_RIGHT] == np) {
UNSUPPORTED("1t69shrxanujd9qd85gq4qeng"); // 	p.y = (cp->bb.LL.y + cp->bb.UR.y)/2;
UNSUPPORTED("ehaxid5d4p09fg8swm5oivbh9"); // 	p.x = cp->bb.UR.x;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 88q5zadn0aehrwxgxwm2qrbrk
// static boxf emitEdge (FILE* fp, Agedge_t* e, route rte, maze* m, int ix, boxf bb) 
public static Object emitEdge(Object... arg) {
UNSUPPORTED("d5qt6s97burjfu5qe0oxyyrmr"); // static boxf
UNSUPPORTED("dnxab88k0n0c2bl3z0wgvml6i"); // emitEdge (FILE* fp, Agedge_t* e, route rte, maze* m, int ix, boxf bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eusea1bk6jn8fd1b9chikta59"); //     int i, x, y;
UNSUPPORTED("orqp5l9owubc5xfs0c6zc4fu"); //     boxf n = ((cell*)ND_alg(agtail(e)))->bb;
UNSUPPORTED("bv14gu5qoc27okyaav2vdl3fi"); //     segment* seg = rte.segs;
UNSUPPORTED("9z20n0kimree8b0ala1dzcz3f"); //     if (seg->isVert) {
UNSUPPORTED("d35lpqdphvgn7a4elfttfttf5"); // 	x = vtrack(seg, m);
UNSUPPORTED("7d8m8clw270qs6crbw28fvj90"); // 	y = (n.UR.y + n.LL.y)/2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("cl661znjs3x3fj8d1y9cg24sj"); // 	y = htrack(seg, m);
UNSUPPORTED("c1hxt4wou5pqdatrc4gw3y5yt"); // 	x = (n.UR.x + n.LL.x)/2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("xhluynvutbzbfvcxx2cjyytz"); //     bb.LL.x = MIN(bb.LL.x, 1*x);
UNSUPPORTED("33wk87zqb5r4f23468khg93pg"); //     bb.LL.y = MIN(bb.LL.y, 1*y);
UNSUPPORTED("6mk0mwcxlhcgch8smmk49m01g"); //     bb.UR.x = MAX(bb.UR.x, 1*x);
UNSUPPORTED("4z54bwwdfw0evollgabbhoyfh"); //     bb.UR.y = MAX(bb.UR.y, 1*y);
UNSUPPORTED("bd57sg9ngr5l29b4xy3hasneh"); //     fprintf (fp, "newpath %d %d moveto\n", 1*x, 1*y);
UNSUPPORTED("rfx8uba9j7zxsbk2dfcyo2j8"); //     for (i = 1;i<rte.n;i++) {
UNSUPPORTED("90i6hsbw4ncxgqnor5ejql4qy"); // 	seg = rte.segs+i;
UNSUPPORTED("6pz2d4cdoe2l7351iysy6x98p"); // 	if (seg->isVert) {
UNSUPPORTED("1b5sxos9qcnmmvphnk5nns37s"); // 	    x = vtrack(seg, m);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("19kkar9zhosg7ryut8il82rx7"); // 	    y = htrack(seg, m);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2d7turuf2z2kzsjsi5ajmg6np"); // 	bb.LL.x = MIN(bb.LL.x, 1*x);
UNSUPPORTED("c2qeisl94yrotinrfx39xxqo3"); // 	bb.LL.y = MIN(bb.LL.y, 1*y);
UNSUPPORTED("2y9tfuxsj7noszyfrt0ahp2lk"); // 	bb.UR.x = MAX(bb.UR.x, 1*x);
UNSUPPORTED("ckczjxes8uggdg6ltkg2gqw5x"); // 	bb.UR.y = MAX(bb.UR.y, 1*y);
UNSUPPORTED("dwd2wbhfazyd038t2qn0hc7bv"); // 	fprintf (fp, "%d %d lineto\n", 1*x, 1*y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b0b65d8qp73v6bg7hjzivvqz9"); //     n = ((cell*)ND_alg(aghead(e)))->bb;
UNSUPPORTED("9z20n0kimree8b0ala1dzcz3f"); //     if (seg->isVert) {
UNSUPPORTED("d35lpqdphvgn7a4elfttfttf5"); // 	x = vtrack(seg, m);
UNSUPPORTED("7d8m8clw270qs6crbw28fvj90"); // 	y = (n.UR.y + n.LL.y)/2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("cl661znjs3x3fj8d1y9cg24sj"); // 	y = htrack(seg, m);
UNSUPPORTED("d0efs14iyqyu5lops51368trn"); // 	x = (n.LL.x + n.UR.x)/2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("xhluynvutbzbfvcxx2cjyytz"); //     bb.LL.x = MIN(bb.LL.x, 1*x);
UNSUPPORTED("33wk87zqb5r4f23468khg93pg"); //     bb.LL.y = MIN(bb.LL.y, 1*y);
UNSUPPORTED("6mk0mwcxlhcgch8smmk49m01g"); //     bb.UR.x = MAX(bb.UR.x, 1*x);
UNSUPPORTED("4z54bwwdfw0evollgabbhoyfh"); //     bb.UR.y = MAX(bb.UR.y, 1*y);
UNSUPPORTED("4lv2wvln641rjodmppi1ea8ow"); //     fprintf (fp, "%d %d lineto stroke\n", 1*x, 1*y);
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 26m9b9nmqeqz2egz17qjrsmi6
// static void emitSearchGraph (FILE* fp, sgraph* sg) 
public static Object emitSearchGraph(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3afa3kd81chzz47ecdq46zrer"); // emitSearchGraph (FILE* fp, sgraph* sg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dyevh82lgyyzdp6weja1ovxir"); //     cell* cp;
UNSUPPORTED("bhhrlzv5tqbx6ctv8b8hya7ll"); //     snode* np;
UNSUPPORTED("2360u3fv51vz7azzvwtpxz8ys"); //     sedge* ep;
UNSUPPORTED("41nrdsugvfgw23s93g0dhizkn"); //     point p;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6gvhztp2b7m89nv2btikaiyz4"); //     fputs ("graph G {\n", fp);
UNSUPPORTED("bmtp7y0sxxjdh22j8p4pvgssu"); //     fputs (" node[shape=point]\n", fp);
UNSUPPORTED("89usm1dk3yo4ttycgh9mcqv0f"); //     for (i = 0; i < sg->nnodes; i++) {
UNSUPPORTED("3kjyiosnhbp02weff5w4htinu"); // 	np = sg->nodes+i;
UNSUPPORTED("4k5we2dw3u45kv7a09gffw8tf"); // 	cp = np->cells[0];
UNSUPPORTED("1kn01nkzc34bp86c9bvl7a74b"); // 	if (cp == np->cells[1]) {
UNSUPPORTED("e78riujsgaj8ogp31bihqa6rr"); // 	    pointf pf = midPt (cp);
UNSUPPORTED("1hdg4vfvme7qnviehisvcj5jl"); // 	    p.x = pf.x;
UNSUPPORTED("7o9naunu16bztru6ho4g6yp5s"); // 	    p.y = pf.y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("7ks8s38h2f1xoqnd6vgdegepn"); // 	    if ((cp->flags & 1)) cp = np->cells[1];
UNSUPPORTED("995g67hltj0paxtxlkedyryh1"); // 	    p = coordOf (cp, np);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7lalws1y1951xlz9ffakcbom8"); // 	fprintf (fp, "  %d [pos=\"%d,%d\"]\n", i, p.x, p.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2ktcncbuekdxpzstdfs5xnwyu"); //     for (i = 0; i < sg->nedges; i++) {
UNSUPPORTED("6hjfrnsaxev4hsgcyeka41z77"); // 	ep = sg->edges+i;
UNSUPPORTED("a7b1ejc81bv3o5ahc8hoxa98u"); // 	fprintf (fp, "  %d -- %d[len=\"%f\"]\n", ep->v1, ep->v2, ep->weight);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("69smfbpzurcjvgjvm90vhl35k"); //     fputs ("}\n", fp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7qavznbkl6bvpmm9axulwfdbe
// static void emitGraph (FILE* fp, maze* mp, int n_edges, route* route_list, epair_t es[]) 
public static Object emitGraph(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("bkcsiggpetlvefiz01q93uej4"); // emitGraph (FILE* fp, maze* mp, int n_edges, route* route_list, epair_t es[])
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7546bgor76e4n44ldtchty3d4"); //     boxf bb, absbb;
UNSUPPORTED("emg8ro778e56pul9iomm1odwo"); //     box bbox;
UNSUPPORTED("c1kybqd5l3qg3n9a305wjynks"); //     absbb.LL.x = absbb.LL.y = MAXDOUBLE;
UNSUPPORTED("8k7etzgpxlrl6uupdtjg0hodh"); //     absbb.UR.x = absbb.UR.y = -MAXDOUBLE;
UNSUPPORTED("118oh6o7l5ozdkayqepqyzeq1"); //     fprintf (fp, "%s", prolog2);
UNSUPPORTED("1m4pf51j8j5r5hwafqhf1gpxp"); //     fprintf (fp, "%d %d translate\n", 10, 10);
UNSUPPORTED("1l4k0raj069p4juux59nmeddm"); //     fputs ("0 0 1 setrgbcolor\n", fp);
UNSUPPORTED("eabxb5i0ddkbddnag9j34xjbc"); //     for (i = 0; i < mp->ngcells; i++) {
UNSUPPORTED("8we0nz55uqrc136qh9ei4wi1r"); //       bb = mp->gcells[i].bb;
UNSUPPORTED("eaawrfhkil1c35bbtxc96ifj7"); //       fprintf (fp, "%f %f %f %f node\n", bb.LL.x, bb.LL.y, bb.UR.x, bb.UR.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6gdenpywxn3p3xidvp7z66cf1"); //     for (i = 0; i < n_edges; i++) {
UNSUPPORTED("c0bzh5j9t4e77lu0tb21a67bz"); // 	absbb = emitEdge (fp, es[i].e, route_list[i], mp, i, absbb);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("86kkucwmapf5ozkph3236xcgb"); //     fputs ("0.8 0.8 0.8 setrgbcolor\n", fp);
UNSUPPORTED("cc6zk9wsty15bcay61nb68jos"); //     for (i = 0; i < mp->ncells; i++) {
UNSUPPORTED("323ihqshamlg2xo726pk3b3vj"); //       bb = mp->cells[i].bb;
UNSUPPORTED("e86ar5rrwd873yux3qqdkktes"); //       fprintf (fp, "%f %f %f %f cell\n", bb.LL.x, bb.LL.y, bb.UR.x, bb.UR.y);
UNSUPPORTED("1739n1u2wlfk0ha07u0i2kqns"); //       absbb.LL.x = MIN(absbb.LL.x, bb.LL.x);
UNSUPPORTED("2k0qxt9afi97ol188k2rxoiot"); //       absbb.LL.y = MIN(absbb.LL.y, bb.LL.y);
UNSUPPORTED("cgmau9yngryx709jc2th5sk7o"); //       absbb.UR.x = MAX(absbb.UR.x, bb.UR.x);
UNSUPPORTED("b08g38fj835q33y3jhlu4r7or"); //       absbb.UR.y = MAX(absbb.UR.y, bb.UR.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("66qdohztsd8obgtrz7n7aneyq"); //     bbox.LL.x = absbb.LL.x + 10;
UNSUPPORTED("eanpui2i9474f3m8ugpr9isad"); //     bbox.LL.y = absbb.LL.y + 10;
UNSUPPORTED("8azdlhxair6id14ox480nrq72"); //     bbox.UR.x = absbb.UR.x + 10;
UNSUPPORTED("3pbdldqi9gmkwgneq7kyfowun"); //     bbox.UR.y = absbb.UR.y + 10;
UNSUPPORTED("e2528fy8l69smovwey76qcix3"); //     fprintf (fp, epilog2, bbox.LL.x, bbox.LL.y,  bbox.UR.x, bbox.UR.y);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
