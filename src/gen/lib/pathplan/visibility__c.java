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
package gen.lib.pathplan;
import static smetana.core.Macro.UNSUPPORTED;

public class visibility__c {
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD


//1 e75el5ykqd72ikokwkl7j2epc
// typedef COORD **array2




//3 bsczl8sh39sr9x8mdrdw934gc
// static array2 allocArray(int V, int extra) 
public static Object allocArray(Object... arg) {
UNSUPPORTED("2ep0t1ji7dax5248t02u6t7r6"); // static array2 allocArray(int V, int extra)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6advqmqfqum2lcm4ln3micrah"); //     array2 arr;
UNSUPPORTED("37gilvazosxkc28mj73rooar1"); //     COORD *p;
UNSUPPORTED("8o8ej4mv7ezjz8fiws88yzo9q"); //     arr = (COORD **) malloc((V + extra) * sizeof(COORD *));
UNSUPPORTED("e510sd0tpqggpavb3qj1c1qxt"); //     p = (COORD *) calloc(V * V, sizeof(COORD));
UNSUPPORTED("95d7bboanqosxp7ljja94xzf3"); //     for (i = 0; i < V; i++) {
UNSUPPORTED("cpb0ooq9n5nzvgd12sa7ws9h"); // 	arr[i] = p;
UNSUPPORTED("54buizub47gj1g6rqqp2nuxm"); // 	p += V;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dbbu9u15v89j269ghj0n14aj4"); //     for (i = V; i < V + extra; i++)
UNSUPPORTED("w0dpyxz5clv9ki0kuw7rzfn7"); // 	arr[i] = (COORD *) 0;
UNSUPPORTED("ccsfhshi4ig5z5la1ju1x9cnc"); //     return arr;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dlr6qgzk1zg621iotphi6ibd8
// COORD area2(Ppoint_t a, Ppoint_t b, Ppoint_t c) 
public static Object area2(Object... arg) {
UNSUPPORTED("955niwvmnzworzojvwdpapmof"); // COORD area2(Ppoint_t a, Ppoint_t b, Ppoint_t c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("57vegsifiuko417qaa6szlm9n"); //     return ((a.y - b.y) * (c.x - b.x) - (c.y - b.y) * (a.x - b.x));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 vt0jebecnhaqax0chaict0yw
// int wind(Ppoint_t a, Ppoint_t b, Ppoint_t c) 
public static Object wind(Object... arg) {
UNSUPPORTED("7zwv4pv8g3dirkozmn0fe34z8"); // int wind(Ppoint_t a, Ppoint_t b, Ppoint_t c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ecssyhacl2ayvt0ote07teaw"); //     COORD w;
UNSUPPORTED("5wu3j9ks9mimt6j7joxxnq9ia"); //     w = ((a.y - b.y) * (c.x - b.x) - (c.y - b.y) * (a.x - b.x));
UNSUPPORTED("es8qrkgjbswwyq90zllvcxwnh"); //     /* need to allow for small math errors.  seen with "gcc -O2 -mcpu=i686 -ffast-math" */
UNSUPPORTED("dzqusvkt2ejo97ycithiv2ucf"); //     return (w > .0001) ? 1 : ((w < -.0001) ? -1 : 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3zfwfpss5ea581xa4wtms7rxv
// int inBetween(Ppoint_t a, Ppoint_t b, Ppoint_t c) 
public static Object inBetween(Object... arg) {
UNSUPPORTED("dnvtojgefs45udqxtt80ifsxm"); // int inBetween(Ppoint_t a, Ppoint_t b, Ppoint_t c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9retom1yv4wpdu31f7jsiae8d"); //     if (a.x != b.x)		/* not vertical */
UNSUPPORTED("7bptnkdfcpj5jr3mniv8eq3tl"); // 	return (((a.x < c.x) && (c.x < b.x))
UNSUPPORTED("1v7hun84nlrv6wqpwv4dlqlxu"); // 		|| ((b.x < c.x) && (c.x < a.x)));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("d2izj3dk2mx1gese9uwrde73a"); // 	return (((a.y < c.y) && (c.y < b.y))
UNSUPPORTED("ey5cvegw2q4qz6328omtt2frt"); // 		|| ((b.y < c.y) && (c.y < a.y)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 79f7jjxrrdgo9l858v24xof7v
// int intersect(Ppoint_t a, Ppoint_t b, Ppoint_t c, Ppoint_t d) 
public static Object intersect(Object... arg) {
UNSUPPORTED("roqgz6tihjg6s1lbmr0j6phx"); // int intersect(Ppoint_t a, Ppoint_t b, Ppoint_t c, Ppoint_t d)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5ak23ep8c08cwpy4ro73kg6a9"); //     int a_abc;
UNSUPPORTED("pkbc2911eqdxi3q3oig9iggz"); //     int a_abd;
UNSUPPORTED("3x6q0p7p8vp6n3xuz68aqyi2z"); //     int a_cda;
UNSUPPORTED("2nckaq0hwjy642bs8wytfrvzc"); //     int a_cdb;
UNSUPPORTED("dh0rdktbccsux65trza0p1ex0"); //     a_abc = wind(a, b, c);
UNSUPPORTED("8rg4ptpwwdazrael76ersibnj"); //     if ((a_abc == 0) && inBetween(a, b, c)) {
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("65kyqogr1qwpdj4vzqhqdovjx"); //     a_abd = wind(a, b, d);
UNSUPPORTED("f2bfktdr6qjmqrr2o4ngzp8hs"); //     if ((a_abd == 0) && inBetween(a, b, d)) {
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("54toimzvo18mzs7vfnqvnumln"); //     a_cda = wind(c, d, a);
UNSUPPORTED("7qvzemgdibqwxezutu4es8otb"); //     a_cdb = wind(c, d, b);
UNSUPPORTED("4cp5ufnxuyvb9bthje52yijlm"); //     /* True if c and d are on opposite sides of ab,
UNSUPPORTED("4k1c3jig81qg0as8woji48ew9"); //      * and a and b are on opposite sides of cd.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("d581gmneeqsqn2uql7i5toff3"); //     return (((a_abc * a_abd) < 0) && ((a_cda * a_cdb) < 0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 897fne6m7yp6g1skatrohstqd
// static int in_cone(Ppoint_t a0, Ppoint_t a1, Ppoint_t a2, Ppoint_t b) 
public static Object in_cone(Object... arg) {
UNSUPPORTED("6k9z11o3eut4ihkvof27ionw4"); // static int in_cone(Ppoint_t a0, Ppoint_t a1, Ppoint_t a2, Ppoint_t b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("azst6m11c18syon6lwf1d36gq"); //     int m = wind(b, a0, a1);
UNSUPPORTED("edizv9dcv0vkkp4l4vuw7k97r"); //     int p = wind(b, a1, a2);
UNSUPPORTED("6b5qii4lexve7f52xqb76yfuj"); //     if (wind(a0, a1, a2) > 0)
UNSUPPORTED("eav3el0uo07672rpa2kyd0uge"); // 	return (m >= 0 && p >= 0);	/* convex at a */
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("21ufwcoiy0fe6t0dog8u1mgyb"); // 	return (m >= 0 || p >= 0);	/* reflex at a */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bo8ti3cpgvbanj1uzsm9c1bhe
// COORD dist2(Ppoint_t a, Ppoint_t b) 
public static Object dist2(Object... arg) {
UNSUPPORTED("blrxs1tia575ecxgyj1t3xyzr"); // COORD dist2(Ppoint_t a, Ppoint_t b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("p0k98xc3wk6x7in7sehd5dyh"); //     COORD delx = a.x - b.x;
UNSUPPORTED("bw95b7u6pyi3v7wyz9gln4uus"); //     COORD dely = a.y - b.y;
UNSUPPORTED("c07ue20s75ggd0htt7g0ow5sh"); //     return (delx * delx + dely * dely);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e6i8sjhx5klba13ifnde18uay
// static COORD dist(Ppoint_t a, Ppoint_t b) 
public static Object dist(Object... arg) {
UNSUPPORTED("f3414cvfyz69wnzb7u82byqr6"); // static COORD dist(Ppoint_t a, Ppoint_t b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c5c8q1zhigd7yv5pwxe30rbc4"); //     return sqrt(dist2(a, b));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bbuqqywhkghz3a4pmrzvnwat3
// static int inCone(int i, int j, Ppoint_t pts[], int nextPt[], int prevPt[]) 
public static Object inCone(Object... arg) {
UNSUPPORTED("2m7xwhnvt83bmhre90ibjzp22"); // static int inCone(int i, int j, Ppoint_t pts[], int nextPt[], int prevPt[])
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3nsyz336uzlzhdtn1ifpdpdbw"); //     return in_cone(pts[prevPt[i]], pts[i], pts[nextPt[i]], pts[j]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9k5g381ss7ta6nb7v37y3oo4x
// static int clear(Ppoint_t pti, Ppoint_t ptj, 		 int start, int end, 		 int V, Ppoint_t pts[], int nextPt[], int prevPt[]) 
public static Object clear(Object... arg) {
UNSUPPORTED("68m9293lem1wya8xmo8rg6biv"); // static int clear(Ppoint_t pti, Ppoint_t ptj,
UNSUPPORTED("8lrao52hd6uwbjl78v0f7a7ec"); // 		 int start, int end,
UNSUPPORTED("a8lvklwic5zz27yulrg7ododk"); // 		 int V, Ppoint_t pts[], int nextPt[], int prevPt[])
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b0kisc5bimb4jnz3z1g2yhbqv"); //     int k;
UNSUPPORTED("9phanu31yb7616q5xjn3fva6x"); //     for (k = 0; k < start; k++) {
UNSUPPORTED("ye8byugyetsemml5w5sus11k"); // 	if (intersect((pti),(ptj),(pts[k]),(pts[nextPt[k]])))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("aey08ei9e4arvxt8hzy83qb9c"); //     for (k = end; k < V; k++) {
UNSUPPORTED("ye8byugyetsemml5w5sus11k"); // 	if (intersect((pti),(ptj),(pts[k]),(pts[nextPt[k]])))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a5f84g6uyajcrmmt06a61ulyi
// static void compVis(vconfig_t * conf, int start) 
public static Object compVis(Object... arg) {
UNSUPPORTED("izn0i9pb2n1dmy8hrpd016j1"); // static void compVis(vconfig_t * conf, int start)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9e683sch2jealt2ckz48v9f0j"); //     int V = conf->N;
UNSUPPORTED("7d9bin19ief7l7y1pkikvuwiu"); //     Ppoint_t *pts = conf->P;
UNSUPPORTED("9rljos3iubpdnzm02l6bd6pga"); //     int *nextPt = conf->next;
UNSUPPORTED("4n11bnl3onnk0hldumoxfi7bx"); //     int *prevPt = conf->prev;
UNSUPPORTED("e0225hhlmgxnr7acr4j9fr4rj"); //     array2 wadj = conf->vis;
UNSUPPORTED("edgqyxis5vrh7zvky36d4e9a0"); //     int j, i, previ;
UNSUPPORTED("bjc6w95h0ns9cjy8h8j9niggy"); //     COORD d;
UNSUPPORTED("2xpkyvrmw8kb8hkc5g5820ed7"); //     for (i = start; i < V; i++) {
UNSUPPORTED("4woc2r6ql8s9ni8zc78gafifx"); // 	/* add edge between i and previ.
UNSUPPORTED("aoxujkcbwbq2misfrn7k3d7x8"); // 	 * Note that this works for the cases of polygons of 1 and 2
UNSUPPORTED("1uyr4bh9q9mhcn76g13u98kh0"); // 	 * vertices, though needless work is done.
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("31w2a4xrdim19v0h965b1cr2c"); // 	previ = prevPt[i];
UNSUPPORTED("4bd7y0t2a1w6hs3ohpem3ysgu"); // 	d = dist(pts[i], pts[previ]);
UNSUPPORTED("4otd40ic0u4tos8oseiipjzc9"); // 	wadj[i][previ] = d;
UNSUPPORTED("bib5b7wm3wqhsons82qecl"); // 	wadj[previ][i] = d;
UNSUPPORTED("e85r4voxpd10f2vhaakw43f9m"); // 	/* Check remaining, earlier vertices */
UNSUPPORTED("34wvs6wxg9wroez0cob4eq0yt"); // 	if (previ == i - 1)
UNSUPPORTED("52l667dp9oah6rslc8wjoujp1"); // 	    j = i - 2;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("9k7vxyf71hfmr8jfddpfd90fg"); // 	    j = i - 1;
UNSUPPORTED("86i8ouw8og1peosqts789zl36"); // 	for (; j >= 0; j--) {
UNSUPPORTED("axb1scf1zch83hijq76uhgavw"); // 	    if (inCone(i, j, pts, nextPt, prevPt) &&
UNSUPPORTED("f53u8xtqvo7t5kx42e92w686r"); // 		inCone(j, i, pts, nextPt, prevPt) &&
UNSUPPORTED("1g3bn9manlh9eb34pxq0uzz8t"); // 		clear(pts[i], pts[j], V, V, V, pts, nextPt, prevPt)) {
UNSUPPORTED("5qsnuuk4fv4fooabczbbd9ahx"); // 		/* if i and j see each other, add edge */
UNSUPPORTED("9fadh88q86g90beilv0kqxmaa"); // 		d = dist(pts[i], pts[j]);
UNSUPPORTED("6ut8yosujth8pmmzsvs0rbn4r"); // 		wadj[i][j] = d;
UNSUPPORTED("dm7renktz4js1qpo4fxfpjv96"); // 		wadj[j][i] = d;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 afx2tbncgy8wtrk8wrpvt8c7v
// void visibility(vconfig_t * conf) 
public static Object visibility(Object... arg) {
UNSUPPORTED("25e0cmz7nmahqmmpcvbvi3kbv"); // void visibility(vconfig_t * conf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("80p1p77b7wohnw2r3v9cilgja"); //     conf->vis = allocArray(conf->N, 2);
UNSUPPORTED("cocdypij6tuxqzgtmgw0ffdyi"); //     compVis(conf, 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 f53btxccmpw07qdajkgza16dq
// static int polyhit(vconfig_t * conf, Ppoint_t p) 
public static Object polyhit(Object... arg) {
UNSUPPORTED("3jhqkjkjkqnmtf4w0yu1vcgkm"); // static int polyhit(vconfig_t * conf, Ppoint_t p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("4a012clawvd7u5m72juylqzoa"); //     Ppoly_t poly;
UNSUPPORTED("bdsnknxap30wvari7ivffhs3m"); //     for (i = 0; i < conf->Npoly; i++) {
UNSUPPORTED("ey7hd6wdy31mr7tdmlijqjq0n"); // 	poly.ps = &(conf->P[conf->start[i]]);
UNSUPPORTED("3qtnbizhz0j64zuzkh3tthe1h"); // 	poly.pn = conf->start[i + 1] - conf->start[i];
UNSUPPORTED("5mttoot38d7jpevufudmwxewd"); // 	if (in_poly(poly, p))
UNSUPPORTED("8wtxouhhluawfic81s58qdk0e"); // 	    return i;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b8wqo20wej72k4fwead61jun4"); //     return -1111;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2yvo05bz8qq8rrhli2rlonyix
// COORD *ptVis(vconfig_t * conf, int pp, Ppoint_t p) 
public static Object ptVis(Object... arg) {
UNSUPPORTED("d58fb5rw4i15qcu5i6zi8562u"); // COORD *ptVis(vconfig_t * conf, int pp, Ppoint_t p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9e683sch2jealt2ckz48v9f0j"); //     int V = conf->N;
UNSUPPORTED("7d9bin19ief7l7y1pkikvuwiu"); //     Ppoint_t *pts = conf->P;
UNSUPPORTED("9rljos3iubpdnzm02l6bd6pga"); //     int *nextPt = conf->next;
UNSUPPORTED("4n11bnl3onnk0hldumoxfi7bx"); //     int *prevPt = conf->prev;
UNSUPPORTED("b0kisc5bimb4jnz3z1g2yhbqv"); //     int k;
UNSUPPORTED("aa6b5n017in5orglen9ktgs9a"); //     int start, end;
UNSUPPORTED("ib3s5dce6s963vo6b8ltvstb"); //     COORD *vadj;
UNSUPPORTED("99q4ws63ls29yvcb513my0kp3"); //     Ppoint_t pk;
UNSUPPORTED("bjc6w95h0ns9cjy8h8j9niggy"); //     COORD d;
UNSUPPORTED("amrnq8h2txtlsnydbwpxx8p9e"); //     vadj = (COORD *) malloc((V + 2) * sizeof(COORD));
UNSUPPORTED("1xmg38dmhrjwt80u0hb882akq"); //     if (pp == -2222)
UNSUPPORTED("67gimzlcd9mmh55z0uwgo1svm"); // 	pp = polyhit(conf, p);
UNSUPPORTED("fcadmwema5f9zw0pun57mr61"); //     if (pp >= 0) {
UNSUPPORTED("6rdfhzubd1dzowvcgtvu07skw"); // 	start = conf->start[pp];
UNSUPPORTED("qr28k5sqgz293v061p9lh7mw"); // 	end = conf->start[pp + 1];
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("4k2lbbhod6d50bie3p0ovngng"); // 	start = V;
UNSUPPORTED("6p0gh50lvtsw3epczfbncrrzh"); // 	end = V;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9phanu31yb7616q5xjn3fva6x"); //     for (k = 0; k < start; k++) {
UNSUPPORTED("685xi4ztalqspw63iho1ijb2a"); // 	pk = pts[k];
UNSUPPORTED("ej65d6v3kqqc41pf2amjx9u8o"); // 	if (in_cone(pts[prevPt[k]], pk, pts[nextPt[k]], p) &&
UNSUPPORTED("45whycl2s4k3gm8ltb52iawfc"); // 	    clear(p, pk, start, end, V, pts, nextPt, prevPt)) {
UNSUPPORTED("204lwnssk9azp929idzzmu2oh"); // 	    /* if p and pk see each other, add edge */
UNSUPPORTED("2y5618jurnxt2bnwydutfzwr4"); // 	    d = dist(p, pk);
UNSUPPORTED("d97qa8dblreww61gxggrpddq9"); // 	    vadj[k] = d;
UNSUPPORTED("6to1esmb8qfrhzgtr7jdqleja"); // 	} else
UNSUPPORTED("w27fuo0npg13jbsblev28cke"); // 	    vadj[k] = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3aiu3msz4kw0sitr3eu403460"); //     for (k = start; k < end; k++)
UNSUPPORTED("ecdly7fjptzn9hbgscfv42v5f"); // 	vadj[k] = 0;
UNSUPPORTED("aey08ei9e4arvxt8hzy83qb9c"); //     for (k = end; k < V; k++) {
UNSUPPORTED("685xi4ztalqspw63iho1ijb2a"); // 	pk = pts[k];
UNSUPPORTED("ej65d6v3kqqc41pf2amjx9u8o"); // 	if (in_cone(pts[prevPt[k]], pk, pts[nextPt[k]], p) &&
UNSUPPORTED("45whycl2s4k3gm8ltb52iawfc"); // 	    clear(p, pk, start, end, V, pts, nextPt, prevPt)) {
UNSUPPORTED("204lwnssk9azp929idzzmu2oh"); // 	    /* if p and pk see each other, add edge */
UNSUPPORTED("2y5618jurnxt2bnwydutfzwr4"); // 	    d = dist(p, pk);
UNSUPPORTED("d97qa8dblreww61gxggrpddq9"); // 	    vadj[k] = d;
UNSUPPORTED("6to1esmb8qfrhzgtr7jdqleja"); // 	} else
UNSUPPORTED("w27fuo0npg13jbsblev28cke"); // 	    vadj[k] = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6udsra1z6g208yb80gqshsz7i"); //     vadj[V] = 0;
UNSUPPORTED("6cyoyzpz6109ngfp8zw4x3ign"); //     vadj[V + 1] = 0;
UNSUPPORTED("euzt74fd0hlgexbbuks72111"); //     return vadj;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 euo3asq18kkzr6xpldh1whj5j
// int directVis(Ppoint_t p, int pp, Ppoint_t q, int qp, vconfig_t * conf) 
public static Object directVis(Object... arg) {
UNSUPPORTED("6vkegkahwe6v9zxmpo9y849de"); // int directVis(Ppoint_t p, int pp, Ppoint_t q, int qp, vconfig_t * conf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9e683sch2jealt2ckz48v9f0j"); //     int V = conf->N;
UNSUPPORTED("7d9bin19ief7l7y1pkikvuwiu"); //     Ppoint_t *pts = conf->P;
UNSUPPORTED("9rljos3iubpdnzm02l6bd6pga"); //     int *nextPt = conf->next;
UNSUPPORTED("cn01t5vyidv9t9jp5k1ebvqnf"); //     /* int*   prevPt = conf->prev; */
UNSUPPORTED("b0kisc5bimb4jnz3z1g2yhbqv"); //     int k;
UNSUPPORTED("5jthc5ipz7t03l29ayjuyuwss"); //     int s1, e1;
UNSUPPORTED("ewn7imvjy270ybgfwapp0tq42"); //     int s2, e2;
UNSUPPORTED("7s1xhwbtcl4cfff6w7bovcx4p"); //     if (pp < 0) {
UNSUPPORTED("8kwihfze04zickxvmduqyz9oz"); // 	s1 = 0;
UNSUPPORTED("d9lzfcaqs050pvd6f6sn4ybfd"); // 	e1 = 0;
UNSUPPORTED("bx8ug7ayoxr9yyklnxyk820fn"); // 	if (qp < 0) {
UNSUPPORTED("bz1de6hpp200ljwdvpkd5u3gn"); // 	    s2 = 0;
UNSUPPORTED("at1mpgp51u974mlhndrczv2lo"); // 	    e2 = 0;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("1w3zcz5sbss50hne5vn9c54uf"); // 	    s2 = conf->start[qp];
UNSUPPORTED("5aiw8tbtx5x4c6d90lqekac0k"); // 	    e2 = conf->start[qp + 1];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("91fgea3cg2flt7f19hu4m9f3k"); //     } else if (qp < 0) {
UNSUPPORTED("8kwihfze04zickxvmduqyz9oz"); // 	s1 = 0;
UNSUPPORTED("d9lzfcaqs050pvd6f6sn4ybfd"); // 	e1 = 0;
UNSUPPORTED("c7jjeiyrdte7kaqrcqi78tb6j"); // 	s2 = conf->start[pp];
UNSUPPORTED("181206xgdu45tknm5eoc7vy7p"); // 	e2 = conf->start[pp + 1];
UNSUPPORTED("1vsb0sn9g2id7jgf818wp2h61"); //     } else if (pp <= qp) {
UNSUPPORTED("2jpazbnst1z69liefzlp7x6k5"); // 	s1 = conf->start[pp];
UNSUPPORTED("birxkvbxsgsetxfbpuugteqbx"); // 	e1 = conf->start[pp + 1];
UNSUPPORTED("2lmk984g1rwi6bvyc4aqlzovj"); // 	s2 = conf->start[qp];
UNSUPPORTED("9sx0re3e6x3m54pfxrbuk571d"); // 	e2 = conf->start[qp + 1];
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("dun18brbfd255gv2nfo9sog80"); // 	s1 = conf->start[qp];
UNSUPPORTED("c1zf66qllk0cm9c4q3cjv9f3r"); // 	e1 = conf->start[qp + 1];
UNSUPPORTED("c7jjeiyrdte7kaqrcqi78tb6j"); // 	s2 = conf->start[pp];
UNSUPPORTED("181206xgdu45tknm5eoc7vy7p"); // 	e2 = conf->start[pp + 1];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f28p3ogo8j178cx69vueppwjb"); //     for (k = 0; k < s1; k++) {
UNSUPPORTED("an3a9zyfv6sgx5bn13egxyjbb"); // 	if (intersect((p),(q),(pts[k]),(pts[nextPt[k]])))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ecib0ib1qhu5qbad78mhypnbs"); //     for (k = e1; k < s2; k++) {
UNSUPPORTED("an3a9zyfv6sgx5bn13egxyjbb"); // 	if (intersect((p),(q),(pts[k]),(pts[nextPt[k]])))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dawiv8sbk7c60uo6bv5kesrag"); //     for (k = e2; k < V; k++) {
UNSUPPORTED("an3a9zyfv6sgx5bn13egxyjbb"); // 	if (intersect((p),(q),(pts[k]),(pts[nextPt[k]])))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
