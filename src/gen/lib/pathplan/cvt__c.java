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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_pointf;

public class cvt__c {
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD


//1 e75el5ykqd72ikokwkl7j2epc
// typedef COORD **array2


//1 ej0x6pmbhu30xkhld8bcz4gwv
// typedef Ppoint_t ilcoord_t




//3 1xzun807liyzhh33wecbjr36t
// static void *mymalloc(size_t newsize) 
public static Object mymalloc(Object... arg) {
UNSUPPORTED("4sbfatyg3fhc2c20h2xhk3mx9"); // static void *mymalloc(size_t newsize)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5ccnu5m92hidffpixzo964tna"); //     void *rv;
UNSUPPORTED("bl8qbtd4wj1z7r9hhretdafhi"); //     if (newsize > 0)
UNSUPPORTED("911ost0j5419vf2t0dcv9syl0"); // 	rv = malloc(newsize);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c4gs555ukbqlir0vf7test5fk"); // 	rv = (void *) 0;
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4pfhgk8c796mhpyfqs8fd3fy9
// vconfig_t *Pobsopen(Ppoly_t ** obs, int n_obs) 
public static Object Pobsopen(Object... arg) {
UNSUPPORTED("dz610tzpr6mx5ioen71yt9lw2"); // vconfig_t *Pobsopen(Ppoly_t ** obs, int n_obs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6h763iz1lhdcesjffuohit038"); //     vconfig_t *rv;
UNSUPPORTED("6fyedst0dusa16an4rkc8wowr"); //     int poly_i, pt_i, i, n;
UNSUPPORTED("aa6b5n017in5orglen9ktgs9a"); //     int start, end;
UNSUPPORTED("2izagha9qm9hfmw2zjtq0yyqy"); //     rv = malloc(sizeof(vconfig_t));
UNSUPPORTED("3932k5yue8e2mb5g02yh32q3u"); //     if (!rv) {
UNSUPPORTED("11hwqop4xebvtcskop4uhpp01"); // 	return NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("537ma80b4p1qif18ogmioua6f"); //     /* get storage */
UNSUPPORTED("5479ingjjk6qn2seps3k0btka"); //     n = 0;
UNSUPPORTED("6pryh49akl0qqyof6t9enjse3"); //     for (poly_i = 0; poly_i < n_obs; poly_i++)
UNSUPPORTED("7mn5akiq7xlxj3bhbg2hjyw43"); // 	n = n + obs[poly_i]->pn;
UNSUPPORTED("4w0j0xkeflf96a4msrd1djw60"); //     rv->P = mymalloc(n * sizeof(Ppoint_t));
UNSUPPORTED("d9e6s9bw2zqrvvmjjx99t8om5"); //     rv->start = mymalloc((n_obs + 1) * sizeof(int));
UNSUPPORTED("cxuszkdst7q9ivsqe8ioe2yrw"); //     rv->next = mymalloc(n * sizeof(int));
UNSUPPORTED("burxtua12xjppcbmval0ggvxg"); //     rv->prev = mymalloc(n * sizeof(int));
UNSUPPORTED("b6qs2ihtcpwqt5curgpzssi0t"); //     rv->N = n;
UNSUPPORTED("y8pzts8dvguxzsn2xoi6o7xr"); //     rv->Npoly = n_obs;
UNSUPPORTED("85r5n7qhz9w2j71ctswryvhs9"); //     /* build arrays */
UNSUPPORTED("9z3er49pc4h2rxja5r9grdo0h"); //     i = 0;
UNSUPPORTED("a9lqcyerwxbnkjv25bsdl8zm4"); //     for (poly_i = 0; poly_i < n_obs; poly_i++) {
UNSUPPORTED("c9man7k6dcktdn55nx8sc8hu2"); // 	start = i;
UNSUPPORTED("l4rzwhaigo1dmtzc7olyk6y2"); // 	rv->start[poly_i] = start;
UNSUPPORTED("do78yh8gawc61ywbjurslrmc7"); // 	end = start + obs[poly_i]->pn - 1;
UNSUPPORTED("ef97u4pbd8y1vhy459q7r4jlr"); // 	for (pt_i = 0; pt_i < obs[poly_i]->pn; pt_i++) {
UNSUPPORTED("duznt0bepum3jc0h5n1g0j6r1"); // 	    rv->P[i] = obs[poly_i]->ps[pt_i];
UNSUPPORTED("cbqnpyguo28y5hi6788clitol"); // 	    rv->next[i] = i + 1;
UNSUPPORTED("75iqn38g2hugox3zmrro1eruh"); // 	    rv->prev[i] = i - 1;
UNSUPPORTED("1lo0ackow66iudrq1gb15y3ry"); // 	    i++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c62cdvr5s10dskbn3g3nv130u"); // 	rv->next[end] = start;
UNSUPPORTED("b77mp3jilxwb6gjveuwuldrvq"); // 	rv->prev[start] = end;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1u0umi0jiwt4fdh8n0nr1j8ns"); //     rv->start[poly_i] = i;
UNSUPPORTED("26uynreqxoitqc5md315okjca"); //     visibility(rv);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e4migs6coi22m83vhdmasdpq2
// void Pobsclose(vconfig_t * config) 
public static Object Pobsclose(Object... arg) {
UNSUPPORTED("54tyi1oh5fd4a8lh1107ysppi"); // void Pobsclose(vconfig_t * config)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9i5pewoqi7dxp9hvkbh6p1kcd"); //     free(config->P);
UNSUPPORTED("dyvruo3b8nwce9o0q2bcycvb6"); //     free(config->start);
UNSUPPORTED("7at6jri0rjj24yyxd75duo75z"); //     free(config->next);
UNSUPPORTED("akdd6tfoylim5pdwp1w2o0j20"); //     free(config->prev);
UNSUPPORTED("e5hnzu51syeuaqx4dgswvpyje"); //     if (config->vis) {
UNSUPPORTED("8rwcdtu45rfzpyn5z46pkkhl6"); // 	free(config->vis[0]);
UNSUPPORTED("i31zzb5907stuz3hbwbdcbhp"); // 	free(config->vis);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("61miap7xqs0is8dzf039j57nq"); //     free(config);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8doijz2zl7icgvbs59qtqw2e6
// int Pobspath(vconfig_t * config, Ppoint_t p0, int poly0, Ppoint_t p1, 	     int poly1, Ppolyline_t * output_route) 
public static Object Pobspath(Object... arg) {
UNSUPPORTED("es9wttiuk2pkcqinglvdetmxh"); // int Pobspath(vconfig_t * config, Ppoint_t p0, int poly0, Ppoint_t p1,
UNSUPPORTED("8waigfzvwmpsy4nk2ybswloiu"); // 	     int poly1, Ppolyline_t * output_route)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e2p3zngy6gppxlaxnza2atj67"); //     int i, j, *dad;
UNSUPPORTED("e62ya6atrka5y0vefbxn9gvsd"); //     int opn;
UNSUPPORTED("eio9tluzd00a9allc8rpk3sn3"); //     Ppoint_t *ops;
UNSUPPORTED("ekriswamubxah5xbwpyub2glk"); //     COORD *ptvis0, *ptvis1;
UNSUPPORTED("f3ay7pz98tp6wbnj91hh3kj"); //     ptvis0 = ptVis(config, poly0, p0);
UNSUPPORTED("9d1jc97ql0y8dloqkw50mmzdj"); //     ptvis1 = ptVis(config, poly1, p1);
UNSUPPORTED("7qs63rfsr6g17tfm9o33b3su6"); //     dad = makePath(p0, poly0, ptvis0, p1, poly1, ptvis1, config);
UNSUPPORTED("b26wav438av32fij4hegkkgu6"); //     opn = 1;
UNSUPPORTED("2vrbfn91t93xoyd85qnu32sy2"); //     for (i = dad[config->N]; i != config->N + 1; i = dad[i])
UNSUPPORTED("3qey9tefagfie3qnkiki4nf6l"); // 	opn++;
UNSUPPORTED("4196zy1bbqiysbv1glm6f6rs5"); //     opn++;
UNSUPPORTED("3snkzyfbaaeiotrx64sx9tpni"); //     ops = malloc(opn * sizeof(Ppoint_t));
UNSUPPORTED("63lio5qjaoeboqhgiam7y7q0q"); //     j = opn - 1;
UNSUPPORTED("ebor0877uiofxllu68qt0ni85"); //     ops[j--] = p1;
UNSUPPORTED("2vrbfn91t93xoyd85qnu32sy2"); //     for (i = dad[config->N]; i != config->N + 1; i = dad[i])
UNSUPPORTED("53win5ovenp62h9g9qdgiu4wq"); // 	ops[j--] = config->P[i];
UNSUPPORTED("2edzhlekn1dh9s7bk2yb2b4sm"); //     ops[j] = p0;
UNSUPPORTED("7i8zqi2jg8upn1wjzqf5il16o"); //     assert(j == 0);
UNSUPPORTED("4sudedq08kppbbadyqc21is7x"); //     if (ptvis0)
UNSUPPORTED("6zdjevsy6rl1dvkniswano9ae"); // 	free(ptvis0);
UNSUPPORTED("1rp1r4ghfa31f1v07hmkoglxl"); //     if (ptvis1)
UNSUPPORTED("au3lcfls1tf7t5z3skjuvfuv8"); // 	free(ptvis1);
UNSUPPORTED("70yf545lz9a6vp2qw2i0q9mr1"); //     output_route->pn = opn;
UNSUPPORTED("e9qedm1c6q4ehjjxi6v3u68ms"); //     output_route->ps = ops;
UNSUPPORTED("6qqkuw5cd4ebshxoji365xfkz"); //     free(dad);
UNSUPPORTED("2mmsh4mer8e3bkt2jk4gf4cyq"); //     return ((!(0)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dya1texcjg4i2qyusjnmin7d1
// int Pobsbarriers(vconfig_t * config, Pedge_t ** barriers, int *n_barriers) 
public static Object Pobsbarriers(Object... arg) {
UNSUPPORTED("bbaf03fwbv3733qfrsvd4eoom"); // int Pobsbarriers(vconfig_t * config, Pedge_t ** barriers, int *n_barriers)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dzpsknrwv8qkqq20hjnjpjn68"); //     int i, j;
UNSUPPORTED("cjg450i3kmtjepexj18lku2oz"); //     *barriers = malloc(config->N * sizeof(Pedge_t));
UNSUPPORTED("b77jnv231p5441j8pofch6qjp"); //     *n_barriers = config->N;
UNSUPPORTED("bt2nq7y73jm09gh8xqv1scjog"); //     for (i = 0; i < config->N; i++) {
UNSUPPORTED("5xul9j0873agp8fm22e0halsf"); // 	barriers[i]->a.x = config->P[i].x;
UNSUPPORTED("ca43a9xp0edjts9sme3eysq0v"); // 	barriers[i]->a.y = config->P[i].y;
UNSUPPORTED("blqt6zbpyfb8cpsh8az3u154y"); // 	j = config->next[i];
UNSUPPORTED("4iqggxhzor4r40emialldaqms"); // 	barriers[i]->b.x = config->P[j].x;
UNSUPPORTED("68b84cgnmyt50bomnqfrw8nau"); // 	barriers[i]->b.y = config->P[j].y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 7jm8pu31yvt3fi0tc6t6go9tr
// static Ppoint_t Bezpt[1000]


//1 7h8jm5n1tlklgw1yaxx7dlvsx
// static int Bezctr




//3 oneie60d998qei37slgf3ce4
// static void addpt(Ppoint_t p) 
public static Object addpt(Object... arg) {
UNSUPPORTED("3r6t7ceh9f1lplt1ewgna2ynh"); // static void addpt(Ppoint_t p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ad7t5k9prp8sma4kc9pwszul3"); //     if ((Bezctr == 0) ||
UNSUPPORTED("eam5jo5658c75g0tppvkhcu31"); // 	(Bezpt[Bezctr - 1].x != p.x) || (Bezpt[Bezctr - 1].y != p.y))
UNSUPPORTED("a0616m6xy91u5reig6r0gz4pg"); // 	Bezpt[Bezctr++] = p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3xaxtxslceffm0wx9zcpiqjgb
// static ilcoord_t Bezier(ilcoord_t * V, int degree, double t, 			ilcoord_t * Left, ilcoord_t * Right) 
public static ST_pointf Bezier(ST_pointf V, int degree, double t, ST_pointf Left, ST_pointf Right) {
// WARNING!! STRUCT
return Bezier_w_(V, degree, t, Left, Right).copy();
}
private static ST_pointf Bezier_w_(ST_pointf V, int degree, double t, ST_pointf Left, ST_pointf Right) {
ENTERING("3xaxtxslceffm0wx9zcpiqjgb","Bezier");
try {
    int i, j;			/* Index variables  */
UNSUPPORTED("3a1zgxysa3t54vyq7k6z62wlj"); //     ilcoord_t Vtemp[5 + 1][5 + 1];
UNSUPPORTED("byow1e8wyl29b607ktk3oabre"); //     /* Copy control points  */
UNSUPPORTED("88y00f5lc64b0ryy0gup9n4sb"); //     for (j = 0; j <= degree; j++) {
UNSUPPORTED("41awql5ne79yczmc4ilerplz3"); // 	Vtemp[0][j] = V[j];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("djq9b7i3h3uq77dr40ha5o1kr"); //     /* Triangle computation */
UNSUPPORTED("227fs1hi4i0vh0vutu3yqxarc"); //     for (i = 1; i <= degree; i++) {
UNSUPPORTED("85sv74izli9gqstg0gqk2oznr"); // 	for (j = 0; j <= degree - i; j++) {
UNSUPPORTED("6iowld1ly15t67xtpbyotvond"); // 	    Vtemp[i][j].x =
UNSUPPORTED("c9tdp4smiiyeg0r8t5udg667x"); // 		(1.0 - t) * Vtemp[i - 1][j].x + t * Vtemp[i - 1][j + 1].x;
UNSUPPORTED("d7sb593gdk2getmc8zbflz6wq"); // 	    Vtemp[i][j].y =
UNSUPPORTED("d3h6zzsk3tsvl81ds9isu6lrl"); // 		(1.0 - t) * Vtemp[i - 1][j].y + t * Vtemp[i - 1][j + 1].y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7eb3j7c6ls3zjqm1bteyyg8dd"); //     if (Left != ((ilcoord_t *)0))
UNSUPPORTED("dzkatwg4oc51psd4chd1yao6"); // 	for (j = 0; j <= degree; j++)
UNSUPPORTED("7zwjg663emkibah35euakizmj"); // 	    Left[j] = Vtemp[j][0];
UNSUPPORTED("6h28oxt02m95ar9k62xgzs2"); //     if (Right != ((ilcoord_t *)0))
UNSUPPORTED("dzkatwg4oc51psd4chd1yao6"); // 	for (j = 0; j <= degree; j++)
UNSUPPORTED("1rki0omqbix35s4wfx442v4ty"); // 	    Right[j] = Vtemp[degree - j][j];
UNSUPPORTED("b23mms6jm55li1q74etrttq6m"); //     return (Vtemp[degree][0]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("3xaxtxslceffm0wx9zcpiqjgb","Bezier");
}
}




//3 2e1dbsqqgp7k8i5doa7prt6jc
// static void append_bezier(Ppoint_t * bezier) 
public static Object append_bezier(Object... arg) {
UNSUPPORTED("5zm80zw3vn3gsj6zre2ynedck"); // static void append_bezier(Ppoint_t * bezier)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("544oxt8zbzz0u06hmg8ivxp3p"); //     double a;
UNSUPPORTED("2lyetpb097xdkox7xgnw605xd"); //     ilcoord_t left[4], right[4];
UNSUPPORTED("4c5enrmeixpvwutnq2tbdb7ck"); //     a = fabs(area2(bezier[0], bezier[1], bezier[2]))
UNSUPPORTED("4fcyo20wfetvf2l99r0pchq0l"); // 	+ fabs(area2(bezier[2], bezier[3], bezier[0]));
UNSUPPORTED("3y36bo7zjs0dx2q4g6sl5awol"); //     if (a < .5) {
UNSUPPORTED("ar7l14fqklgvayi8jwebecz25"); // 	addpt(bezier[0]);
UNSUPPORTED("268jkzt57hijh09gst1b1yl1e"); // 	addpt(bezier[3]);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("4k2sg523wku7lp7tcc9l6ggoj"); // 	(void) Bezier(bezier, 3, .5, left, right);
UNSUPPORTED("bct90i183hbs4qhx4mmpnep1i"); // 	append_bezier(left);
UNSUPPORTED("5dcnwocv9fm5psu807c2hpft6"); // 	append_bezier(right);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
