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
import static smetana.core.Macro.UNSUPPORTED;

public class colxlate__c {
//1 9jk9ukwctyqdsan5lm6845y0i
// static hsvrgbacolor_t color_lib[] = 


//1 48s1hew37k508i0ge3vcsxs69
// static char* colorscheme




//3 7m77ow39f9cx65se44asa35dr
// static void hsv2rgb(double h, double s, double v, 			double *r, double *g, double *b) 
public static Object hsv2rgb(Object... arg) {
UNSUPPORTED("bimj2bwogufgm8nwcyse0gmpq"); // static void hsv2rgb(double h, double s, double v,
UNSUPPORTED("95mlswcykd5as5wr8jbkfhz4f"); // 			double *r, double *g, double *b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("zxlsmu6cv48cbb2gz66otml0"); //     double f, p, q, t;
UNSUPPORTED("8c67ztx4rycxh4gu2wweri33c"); //     if (s <= 0.0) {		/* achromatic */
UNSUPPORTED("94xhdyetsqaie6jerahy5j021"); // 	*r = v;
UNSUPPORTED("9yacceicho3snok9td1kmsz9n"); // 	*g = v;
UNSUPPORTED("8ocel5g2qz8h3vz6bplc9bqe1"); // 	*b = v;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6yid2gjsrd4a5mnmskepuy6us"); // 	if (h >= 1.0)
UNSUPPORTED("6n63uw6udor92ej9zr5x3cno3"); // 	    h = 0.0;
UNSUPPORTED("3i7f8yorkl1thq29uhkr8b1uu"); // 	h = 6.0 * h;
UNSUPPORTED("12owlv8yrft4tfobrzyrgicux"); // 	i = (int) h;
UNSUPPORTED("5u8p6i1s1982n1h0xpp8k1pro"); // 	f = h - (double) i;
UNSUPPORTED("ajuk9rgsixt6bj32ar7rcc0y7"); // 	p = v * (1 - s);
UNSUPPORTED("707y9q1bx5bjn1lvry15sl54u"); // 	q = v * (1 - (s * f));
UNSUPPORTED("jfjlwylasccbjb8xviddgimf"); // 	t = v * (1 - (s * (1 - f)));
UNSUPPORTED("9bo1itj979wxduxtvlcn8uetb"); // 	switch (i) {
UNSUPPORTED("46lzlkypfilrge90rkaiveuyb"); // 	case 0:
UNSUPPORTED("3i88n1gcazf1gz2x4pkhnw9so"); // 	    *r = v;
UNSUPPORTED("bc8k4esl3g1mon6lhrh3ekj1w"); // 	    *g = t;
UNSUPPORTED("7yrzwjb1vtg9vfwd29ab2xh4d"); // 	    *b = p;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2o83im06dulx11wjpy469gkoa"); // 	case 1:
UNSUPPORTED("1487j2wuhsnecg3oa9zmz8ykw"); // 	    *r = q;
UNSUPPORTED("1ynaxhs8cckpg37mv7f9id75r"); // 	    *g = v;
UNSUPPORTED("7yrzwjb1vtg9vfwd29ab2xh4d"); // 	    *b = p;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("b8vgbvwzllfs4lrqmmqyr1spk"); // 	case 2:
UNSUPPORTED("251n6pj3iysy48k21h1sj93lb"); // 	    *r = p;
UNSUPPORTED("1ynaxhs8cckpg37mv7f9id75r"); // 	    *g = v;
UNSUPPORTED("bdpl9tlkwe79rgxap4jt30wsz"); // 	    *b = t;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1640m8as34e90xhvvtl877cmo"); // 	case 3:
UNSUPPORTED("251n6pj3iysy48k21h1sj93lb"); // 	    *r = p;
UNSUPPORTED("nsh415cf187nepn41pky0qp2"); // 	    *g = q;
UNSUPPORTED("6p2xjajb6ttv9mqfeunzd9eil"); // 	    *b = v;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("495y0cnvj5vci19wsufg88rrq"); // 	case 4:
UNSUPPORTED("9fwms7ny5s08rx7kz1uyk22di"); // 	    *r = t;
UNSUPPORTED("d9mdl8v0dmc06nxrlfveytbxc"); // 	    *g = p;
UNSUPPORTED("6p2xjajb6ttv9mqfeunzd9eil"); // 	    *b = v;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1wjv2f7dql1ddky1us3a7q5jq"); // 	case 5:
UNSUPPORTED("3i88n1gcazf1gz2x4pkhnw9so"); // 	    *r = v;
UNSUPPORTED("d9mdl8v0dmc06nxrlfveytbxc"); // 	    *g = p;
UNSUPPORTED("85qonu4zwiaxex0rz7lnk8erp"); // 	    *b = q;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dujzfvldmjwflku9wihupdxnv
// static void rgb2hsv(double r, double g, double b, 		double *h, double *s, double *v) 
public static Object rgb2hsv(Object... arg) {
UNSUPPORTED("c10593ciqiaa4rupftmpdu18r"); // static void rgb2hsv(double r, double g, double b,
UNSUPPORTED("e4aaxkxcwspiazmche1j75xdt"); // 		double *h, double *s, double *v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("440ugp6g625kjfyqr3kq3jrya"); //     double rgbmin, rgbmax;
UNSUPPORTED("4zaqiduai9l3ps6u0ild4xiok"); //     double rc, bc, gc;
UNSUPPORTED("cii4xmgmg0i6aidwdo4k8lvrg"); //     double ht = 0.0, st = 0.0;
UNSUPPORTED("29eodvfgvnx0ikz7xtmlydoyb"); //     rgbmin = MIN(r, MIN(g, b));
UNSUPPORTED("2q0rmom8c6ik02h6h0nuybjln"); //     rgbmax = MAX(r, MAX(g, b));
UNSUPPORTED("1a9pi00vrpfpynvcdzxg33yji"); //     if (rgbmax > 0.0)
UNSUPPORTED("38gq7n4g3llpkn0d8h2ncfn4a"); // 	st = (rgbmax - rgbmin) / rgbmax;
UNSUPPORTED("e92j0hzan753sveeu1kuwt73a"); //     if (st > 0.0) {
UNSUPPORTED("8yt1g8tezilw0yagtf68gd4qn"); // 	rc = (rgbmax - r) / (rgbmax - rgbmin);
UNSUPPORTED("8h7ff4ogst5zbmyxcuu263r3l"); // 	gc = (rgbmax - g) / (rgbmax - rgbmin);
UNSUPPORTED("5sstg8pw8w9jswa78mg2lp3o1"); // 	bc = (rgbmax - b) / (rgbmax - rgbmin);
UNSUPPORTED("7qtihe7rt5ub2gdmz6y337qie"); // 	if (r == rgbmax)
UNSUPPORTED("9f8cswgb2rpdygjlxjh2iulgq"); // 	    ht = bc - gc;
UNSUPPORTED("5m7yr50934hcucttwck4tl2o9"); // 	else if (g == rgbmax)
UNSUPPORTED("brfmtodakxaatp88cgebeetdl"); // 	    ht = 2 + rc - bc;
UNSUPPORTED("7x8fw9oqxkymp3jp9hvfvoh7o"); // 	else if (b == rgbmax)
UNSUPPORTED("5ev3v0kjv7x08a9yplztn6wa2"); // 	    ht = 4 + gc - rc;
UNSUPPORTED("aasi665vfkuijj147w3r3jb8b"); // 	ht = ht * 60.0;
UNSUPPORTED("1wreejtyubn67ne2tfig9anp0"); // 	if (ht < 0.0)
UNSUPPORTED("2cxp7z4dkecyj870xw6hya2rj"); // 	    ht += 360.0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7970jq9aylmwywthy9p42i5fy"); //     *h = ht / 360.0;
UNSUPPORTED("6couzbc17vserykjutgcv7bau"); //     *v = rgbmax;
UNSUPPORTED("218fn8tnzsp40gkh5om1ut3qy"); //     *s = st;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9dul807684jurxmxyyg1zyure
// static void rgb2cmyk(double r, double g, double b, double *c, double *m, 		     double *y, double *k) 
public static Object rgb2cmyk(Object... arg) {
UNSUPPORTED("352h951qbn47q4ctnzbrevu5m"); // static void rgb2cmyk(double r, double g, double b, double *c, double *m,
UNSUPPORTED("plznbj7ka6vfot5ko2iqa8gb"); // 		     double *y, double *k)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("mlwnha0hbx9pl1rufkohkksk"); //     *c = 1.0 - r;
UNSUPPORTED("40r81y2k11kyitt5p234zhr70"); //     *m = 1.0 - g;
UNSUPPORTED("ds3bnptxye9sd8n9a6gnfbrf4"); //     *y = 1.0 - b;
UNSUPPORTED("562ybodfaiqpazgnghrz8vu3h"); //     *k = *c < *m ? *c : *m;
UNSUPPORTED("4agh1yjrfmwb2sia0cnj5q0j1"); //     *k = *y < *k ? *y : *k;
UNSUPPORTED("9tw36bdsk4la16ly056swg45a"); //     *c -= *k;
UNSUPPORTED("63fae2kzb2jh1n6rs66cvwviz"); //     *m -= *k;
UNSUPPORTED("bdgp8lodki7wk4jv9ml43a0zd"); //     *y -= *k;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4yhpwysfqkk00mf4cyx28mzrl
// static int colorcmpf(const void *p0, const void *p1) 
public static Object colorcmpf(Object... arg) {
UNSUPPORTED("vnbeb7jqvgxfst4woiz27q00"); // static int colorcmpf(const void *p0, const void *p1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("anwjd7uy4hi0y3rmpq55acgx0"); //     return strcasecmp(((hsvrgbacolor_t *) p0)->name, ((hsvrgbacolor_t *) p1)->name);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ax75qqr4my2r9ct8dbjvvjtx6
// char *canontoken(char *str) 
public static Object canontoken(Object... arg) {
UNSUPPORTED("1629zjdfy374s0osuesihd243"); // char *canontoken(char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2v53bu6z6q950ej04e355vyf7"); //     static unsigned char *canon;
UNSUPPORTED("7bza0dswpz5oeunov6e43mgnz"); //     static int allocated;
UNSUPPORTED("8j8ixiwexrxoe86i1a876qdje"); //     unsigned char c, *p, *q;
UNSUPPORTED("dwe86466ugstemepdfk8yzphz"); //     int len;
UNSUPPORTED("1zhw3nw03w7084dt7cn5ig222"); //     p = (unsigned char *) str;
UNSUPPORTED("8vgrid9zcfh2t3gzsci2uvqde"); //     len = strlen(str);
UNSUPPORTED("6kgkyoeg9m2xhq784cr9r710f"); //     if (len >= allocated) {
UNSUPPORTED("choxsv8xq99cuoyy5rg3o3ufx"); // 	allocated = len + 1 + 10;
UNSUPPORTED("ccnla80pbvf8pmi4mw346hsq"); // 	canon = grealloc(canon, allocated);
UNSUPPORTED("evp8j7r652aqkdwzrhv80ufny"); // 	if (!canon)
UNSUPPORTED("7t3fvwp9cv90qu5bdjdglcgtk"); // 	    return NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("53qdbi352d1p2q9140cbko0cz"); //     q = (unsigned char *) canon;
UNSUPPORTED("5idssb31ihz3min8hoscutczg"); //     while ((c = *p++)) {
UNSUPPORTED("6oxkjdt12xal6ds7edudbei57"); // 	/* if (isalnum(c) == FALSE) */
UNSUPPORTED("9aswll10socwk83z1vt2t2vu5"); // 	    /* continue; */
UNSUPPORTED("4ne58tc0bo4i4oz5o4b4jme6v"); // 	if (isupper(c))
UNSUPPORTED("815fsuia4alqab5oi8bf1fe1u"); // 	    c = tolower(c);
UNSUPPORTED("6ibj8xkzlbep7pg079j68cmn5"); // 	*q++ = c;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5i5738mywatxkhk4v46m08ovk"); //     *q = '\0';
UNSUPPORTED("6nni1hupcgk2q8q167p2yra2t"); //     return (char*)canon;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5vgbyvdcctqnygkqbcvf8gqkn
// static char* fullColor (char* prefix, char* str) 
public static Object fullColor(Object... arg) {
UNSUPPORTED("6jzwle4t7n644wac4w3ptbzmk"); // static char* fullColor (char* prefix, char* str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("424m1v4nzzlsabaud5t50dky9"); //     static char *fulls;
UNSUPPORTED("7bza0dswpz5oeunov6e43mgnz"); //     static int allocated;
UNSUPPORTED("5tzyrmochxttq8ah54w2vyt0t"); //     int len = strlen (prefix) + strlen (str) + 3;
UNSUPPORTED("6kgkyoeg9m2xhq784cr9r710f"); //     if (len >= allocated) {
UNSUPPORTED("5nfq8ajs2tcl63yizz227j6hh"); // 	allocated = len + 10;
UNSUPPORTED("ahqyb9ue6f8my0dze9s8q9uqt"); // 	fulls = grealloc(fulls, allocated);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f07r4c1sjki0te2vto68bc7d5"); //     sprintf (fulls, "/%s/%s", prefix, str);
UNSUPPORTED("585wlsg0zojpqdlnosc7sdtor"); //     return fulls;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7fjoenpad8y11os28cr4ephqh
// static char* resolveColor (char* str) 
public static Object resolveColor(Object... arg) {
UNSUPPORTED("7m0ujhxnmed6v558yry5p4sic"); // static char* resolveColor (char* str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("yiuh599p05f2mpu2e3pesu2o"); //     char* s;
UNSUPPORTED("a7quapa33ocs19moe9pa284em"); //     char* ss;   /* second slash */
UNSUPPORTED("8rgzc2l3qs5gokf0lfbwmmhs3"); //     char* c2;   /* second char */
UNSUPPORTED("7ucbnwh0ljbustpvbj9f1fn8t"); //     if ((*str == 'b') || !strncmp(str+1,"lack",4)) return str;
UNSUPPORTED("a0xs3pzeccr79t7yxu1vm2r8u"); //     if ((*str == 'w') || !strncmp(str+1,"hite",4)) return str;
UNSUPPORTED("d8l6cky1r79llb12wx586fh0i"); //     if ((*str == 'l') || !strncmp(str+1,"ightgrey",8)) return str;
UNSUPPORTED("68g0vl4sfqxaix0eokby77miu"); //     if (*str == '/') {   /* if begins with '/' */
UNSUPPORTED("1thcre099akc701ud9cuvpzq9"); // 	c2 = str+1;
UNSUPPORTED("8u609q2j81o8ca2nq875feu9l"); //         if ((ss = strchr(c2, '/'))) {  /* if has second '/' */
UNSUPPORTED("96qq76vindtvvu8py6ok6rsr8"); // 	    if (*c2 == '/') {    /* if second '/' is second character */
UNSUPPORTED("6ob87wfl5ntacrexwpvwr2quj"); // 		    /* Do not compare against final '/' */
UNSUPPORTED("5o4dygmz7fi9rfd6gjdpg4y4o"); // 		if (((colorscheme) && *(colorscheme) && strncasecmp("X11/", colorscheme, ((sizeof("X11/")-1)/sizeof(char))-1)))
UNSUPPORTED("bwvwh0rl4f7dxumtom7tf2aa0"); // 		    s = fullColor (colorscheme, c2+1);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("6w0zpynuopuao5in4a5tgvx5f"); // 		    s = c2+1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("3q14jpw2hoz4iitdnw1u99dd1"); // 	    else if (strncasecmp("X11/", c2, ((sizeof("X11/")-1)/sizeof(char)))) s = str;
UNSUPPORTED("7dtq56iupgwvfj9k3shmy1wkp"); // 	    else s = ss + 1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1rqtbmq74rzsr87zc33fdw705"); // 	else s = c2;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("62rg8f7qxc5hu0156fliwf3ag"); //     else if (((colorscheme) && *(colorscheme) && strncasecmp("X11/", colorscheme, ((sizeof("X11/")-1)/sizeof(char))-1))) s = fullColor (colorscheme, str);
UNSUPPORTED("33x0otv2ficwuhmdfoii6co2s"); //     else s = str;
UNSUPPORTED("f133bzwue6od0ojktddh20n9o"); //     return canontoken(s);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2zuzgeyimjdnf5mv3x8t0ki77
// int colorxlate(char *str, gvcolor_t * color, color_type_t target_type) 
public static Object colorxlate(Object... arg) {
UNSUPPORTED("ehzhqf0ynhngejezvvyifodzu"); // int colorxlate(char *str, gvcolor_t * color, color_type_t target_type)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8dd4kyqg8a18xkqxxrnm0kpsx"); //     static hsvrgbacolor_t *last;
UNSUPPORTED("2v53bu6z6q950ej04e355vyf7"); //     static unsigned char *canon;
UNSUPPORTED("7bza0dswpz5oeunov6e43mgnz"); //     static int allocated;
UNSUPPORTED("31egkhuhw9vzm6yjeq3o989rg"); //     unsigned char *p, *q;
UNSUPPORTED("2pxbjw6strjq1x0l4vcvhnzrd"); //     hsvrgbacolor_t fake;
UNSUPPORTED("10sir32iwi5l2jyfgp65pihto"); //     unsigned char c;
UNSUPPORTED("f2swz3fpapldgb0ehjrn24ye6"); //     double H, S, V, A, R, G, B;
UNSUPPORTED("e82iuugluc7yqcpf1hzdj9t0b"); //     double C, M, Y, K;
UNSUPPORTED("5fmu1gaywyplm6yn24ng702iy"); //     unsigned int r, g, b, a;
UNSUPPORTED("2w4c2wk59z31rlianeffya4b7"); //     int len, rc;
UNSUPPORTED("3xampp1xmjli0ebd8k04oflkz"); //     color->type = target_type;
UNSUPPORTED("z00zvrgs8voqmzcegfqnd3mj"); //     rc = 0;
UNSUPPORTED("aa2xm983i87wm9yrret50wu7z"); //     for (; *str == ' '; str++);	/* skip over any leading whitespace */
UNSUPPORTED("1zhw3nw03w7084dt7cn5ig222"); //     p = (unsigned char *) str;
UNSUPPORTED("6ylglimpylokiuqholp7kc8f1"); //     /* test for rgb value such as: "#ff0000"
UNSUPPORTED("1hhw7mmcd7hy3tga5c2cy4uj1"); //        or rgba value such as "#ff000080" */
UNSUPPORTED("dlrbi3u8m2m8y76z33sps4wba"); //     a = 255;			/* default alpha channel value=opaque in case not supplied */
UNSUPPORTED("5z0a0j45mhkka5aqm9y0dmg2"); //     if ((*p == '#')
UNSUPPORTED("aycrro09nwns8jn23jakn4e31"); // 	&& (sscanf((char *) p, "#%2x%2x%2x%2x", &r, &g, &b, &a) >= 3)) {
UNSUPPORTED("cqoqa6pnbr8t180lly83arzbf"); // 	switch (target_type) {
UNSUPPORTED("8x2g67uo65ux7na4wn8yr2hoi"); // 	case HSVA_DOUBLE:
UNSUPPORTED("dpkd6jyfsackl5xx7qm7yc3ju"); // 	    R = (double) r / 255.0;
UNSUPPORTED("cepcltt67wcchzsr6s990ntkq"); // 	    G = (double) g / 255.0;
UNSUPPORTED("4ntwadqpbmdf0t4d0ua8tquxu"); // 	    B = (double) b / 255.0;
UNSUPPORTED("c5xrk3b0ql8xij7ug0lc3j511"); // 	    A = (double) a / 255.0;
UNSUPPORTED("7p8xgh8y8u9gfp2723esq8nhr"); // 	    rgb2hsv(R, G, B, &H, &S, &V);
UNSUPPORTED("3c6fimz05io2h7r45jqm5akmd"); // 	    color->u.HSVA[0] = H;
UNSUPPORTED("dvkaok4qjui5j7he8uyb7cjkk"); // 	    color->u.HSVA[1] = S;
UNSUPPORTED("esjtk662lb2dnrfl7t3r19b07"); // 	    color->u.HSVA[2] = V;
UNSUPPORTED("a8ptt1hl64brvfj57jiv380y2"); // 	    color->u.HSVA[3] = A;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4qyq5lb20pb6flw57yam37kot"); // 	case RGBA_BYTE:
UNSUPPORTED("9fiju79nf1q6i93juztjnip4z"); // 	    color->u.rgba[0] = r;
UNSUPPORTED("1tmbx7r5acrevkd0wcmtzec6i"); // 	    color->u.rgba[1] = g;
UNSUPPORTED("5tqv0dfpaz9wlvpf33sd44mnj"); // 	    color->u.rgba[2] = b;
UNSUPPORTED("4ubswipncx92gv5jqv7v4n7un"); // 	    color->u.rgba[3] = a;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("b18nw968wk9mdxeq4ho72d8xp"); // 	case CMYK_BYTE:
UNSUPPORTED("dpkd6jyfsackl5xx7qm7yc3ju"); // 	    R = (double) r / 255.0;
UNSUPPORTED("cepcltt67wcchzsr6s990ntkq"); // 	    G = (double) g / 255.0;
UNSUPPORTED("4ntwadqpbmdf0t4d0ua8tquxu"); // 	    B = (double) b / 255.0;
UNSUPPORTED("7tttv6awjz9muyxywh3cs8h1a"); // 	    rgb2cmyk(R, G, B, &C, &M, &Y, &K);
UNSUPPORTED("c0b76suwtky43nbsirv28wize"); // 	    color->u.cmyk[0] = (int) C *255;
UNSUPPORTED("5yg3z9bc07yyfs901cvlx7p6b"); // 	    color->u.cmyk[1] = (int) M *255;
UNSUPPORTED("5rh4naug8vgufalo3q2odxixd"); // 	    color->u.cmyk[2] = (int) Y *255;
UNSUPPORTED("2ijzabu50uo431fzaevemaq3p"); // 	    color->u.cmyk[3] = (int) K *255;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3hhhktkxv13skdg8qtbqfccvb"); // 	case RGBA_WORD:
UNSUPPORTED("bsssysiehopm4fcx00c71nb0u"); // 	    color->u.rrggbbaa[0] = r * 65535 / 255;
UNSUPPORTED("bc6z6ydxkwnxduq905k7ry0gj"); // 	    color->u.rrggbbaa[1] = g * 65535 / 255;
UNSUPPORTED("1u2tjaly9mqdx06awvx8fw8c4"); // 	    color->u.rrggbbaa[2] = b * 65535 / 255;
UNSUPPORTED("86f7yk4nr72y7ayyrc6e57135"); // 	    color->u.rrggbbaa[3] = a * 65535 / 255;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1ap6qfmpr0qmmroo04n4v2y6p"); // 	case RGBA_DOUBLE:
UNSUPPORTED("ammm9vmhuq93wsdacjqvvo546"); // 	    color->u.RGBA[0] = (double) r / 255.0;
UNSUPPORTED("5x5jz0u21i8uemni5rpvge5oa"); // 	    color->u.RGBA[1] = (double) g / 255.0;
UNSUPPORTED("9nfyug5ab9pqtaa0fxrdd2v8a"); // 	    color->u.RGBA[2] = (double) b / 255.0;
UNSUPPORTED("eyrh1tnmpdj4ybancy56dmmml"); // 	    color->u.RGBA[3] = (double) a / 255.0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3963t7kz3wj9jmr4b42bh0ikz"); // 	case COLOR_STRING:
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8efm2ybg3ifrezi2q4fb03p57"); // 	case COLOR_INDEX:
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eitnhp9lhv04qoq5fn35tp7p2"); // 	return rc;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e75g686bfy9mqu1k87in9vv1r"); //     /* test for hsv value such as: ".6,.5,.3" */
UNSUPPORTED("e2k96empw2ygjnzfrqjpvgffr"); //     if (((c = *p) == '.') || isdigit(c)) {
UNSUPPORTED("3zq9jhiq7l7htzl88zmc4a0b2"); // 	len = strlen((char*)p);
UNSUPPORTED("28up8t9arwfar8xkch24atnae"); // 	if (len >= allocated) {
UNSUPPORTED("ddmhu29n34mvpbug20e7gfrpp"); // 	    allocated = len + 1 + 10;
UNSUPPORTED("c8rgido7c9hwk057p1nwflu9h"); // 	    canon = grealloc(canon, allocated);
UNSUPPORTED("3soihimxufqqtgztcecr5lan"); // 	    if (! canon) {
UNSUPPORTED("47umle9mvhgi9vy7toebc2zvw"); // 		rc = -1;
UNSUPPORTED("cgwgihktqarc0n7hll268tdb4"); // 		return rc;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("d1cbfh7ze9z7h7rh3jxl96c8x"); // 	q = canon;
UNSUPPORTED("6uczw0au913vi88zvveu2h0tl"); // 	while ((c = *p++)) {
UNSUPPORTED("f0m7phmob63hrmzhuvohg5wet"); // 	    if (c == ',')
UNSUPPORTED("ael7ld07kdrxvpb8xj12l8vij"); // 		c = ' ';
UNSUPPORTED("a1naxqdqse0i1gltpcxpqtfp9"); // 	    *q++ = c;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cxlp7boqg1gqp4ii26w29519a"); // 	*q = '\0';
UNSUPPORTED("2ppiwy4oes1lkaol4e2nknf9c"); // 	if (sscanf((char *) canon, "%lf%lf%lf", &H, &S, &V) == 3) {
UNSUPPORTED("2c8rcf5yaysr8fqa5ap1asp9j"); // 	    /* clip to reasonable values */
UNSUPPORTED("4tzrqsjvx05il761enljel6n6"); // 	    H = MAX(MIN(H, 1.0), 0.0);
UNSUPPORTED("dqjpxih9a58hiwicuciw06vr4"); // 	    S = MAX(MIN(S, 1.0), 0.0);
UNSUPPORTED("2za1jxsv0x24f8jimgjfpvj15"); // 	    V = MAX(MIN(V, 1.0), 0.0);
UNSUPPORTED("7tcobltkij2n43sx05o9n6kug"); // 	    switch (target_type) {
UNSUPPORTED("3waht38mgt9ux3yz4zl4g1g4k"); // 	    case HSVA_DOUBLE:
UNSUPPORTED("nuwdoqw88otrz2ssylnbnhnh"); // 		color->u.HSVA[0] = H;
UNSUPPORTED("f2w3ia24h1oa09rc6ewthuc5i"); // 		color->u.HSVA[1] = S;
UNSUPPORTED("605q0z46ca4sxxsb3vb0rvj5o"); // 		color->u.HSVA[2] = V;
UNSUPPORTED("6zqfqqrvuik71peygm447hc76"); // 		color->u.HSVA[3] = 1.0; /* opaque */
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6ulscgeog9ank5opws5gczv7f"); // 	    case RGBA_BYTE:
UNSUPPORTED("99gjspcfmqpjeen6w12y4grb9"); // 		hsv2rgb(H, S, V, &R, &G, &B);
UNSUPPORTED("bwjh16cq6yjwoe2p22hgy0pum"); // 		color->u.rgba[0] = (int) (R * 255);
UNSUPPORTED("7pi61v8ugyyvumjdeyhcs1bzc"); // 		color->u.rgba[1] = (int) (G * 255);
UNSUPPORTED("5ydlbqv65443sqvn73lcaww78"); // 		color->u.rgba[2] = (int) (B * 255);
UNSUPPORTED("7thq3k4slxcy7ycv1gx28o9h0"); // 		color->u.rgba[3] = 255;	/* opaque */
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("1nx06r4czcqlfs4lqegi34ddz"); // 	    case CMYK_BYTE:
UNSUPPORTED("99gjspcfmqpjeen6w12y4grb9"); // 		hsv2rgb(H, S, V, &R, &G, &B);
UNSUPPORTED("2bxuxozonvzepjt6utzrrqfdu"); // 		rgb2cmyk(R, G, B, &C, &M, &Y, &K);
UNSUPPORTED("ehoij0dm9c7xnl5za53e9joub"); // 		color->u.cmyk[0] = (int) C *255;
UNSUPPORTED("3kk069yq96yvqh7cbytq1oe9p"); // 		color->u.cmyk[1] = (int) M *255;
UNSUPPORTED("pnh92bvbr8g0ovs0svdb0ety"); // 		color->u.cmyk[2] = (int) Y *255;
UNSUPPORTED("8j6drpob4qn1tzqv46ezm2qta"); // 		color->u.cmyk[3] = (int) K *255;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("40ebfdiui5ixqhgg7n6ycmk1d"); // 	    case RGBA_WORD:
UNSUPPORTED("99gjspcfmqpjeen6w12y4grb9"); // 		hsv2rgb(H, S, V, &R, &G, &B);
UNSUPPORTED("bqjm1u4dd0c7dgjivlp4lunfx"); // 		color->u.rrggbbaa[0] = (int) (R * 65535);
UNSUPPORTED("7qa6nwupc5p5tzjy2j3sbpw84"); // 		color->u.rrggbbaa[1] = (int) (G * 65535);
UNSUPPORTED("9upp88q1d7krwt03zdr7f2dp1"); // 		color->u.rrggbbaa[2] = (int) (B * 65535);
UNSUPPORTED("v5ww88r64fhyefive8jpwod1"); // 		color->u.rrggbbaa[3] = 65535;	/* opaque */
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("f20hyopi99mh64hgvbob3629i"); // 	    case RGBA_DOUBLE:
UNSUPPORTED("99gjspcfmqpjeen6w12y4grb9"); // 		hsv2rgb(H, S, V, &R, &G, &B);
UNSUPPORTED("6jkogp2xmtjsirkkt4gq0qk9g"); // 		color->u.RGBA[0] = R;
UNSUPPORTED("9k4pjgp3uz7nshtjln3fy9auu"); // 		color->u.RGBA[1] = G;
UNSUPPORTED("7y5ue1vo9dkn36t07st57ckmh"); // 		color->u.RGBA[2] = B;
UNSUPPORTED("afan1dakanjzco4n2c6rjrxw1"); // 		color->u.RGBA[3] = 1.0;	/* opaque */
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("yi2ftsdqvd62kvijtv49ciof"); // 	    case COLOR_STRING:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("bptas91kxl67utozz0z9ksslp"); // 	    case COLOR_INDEX:
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("boigxj79k0wl3vix6mrtksbik"); // 	    return rc;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("akwwtrxikvsg54ppqaouiraja"); //     /* test for known color name (generic, not renderer specific known names) */
UNSUPPORTED("bniidu3c7uw4j4gnlrw1sml7b"); //     fake.name = resolveColor(str);
UNSUPPORTED("a3ffcy8id0r4fealybty3ncv2"); //     if (!fake.name)
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("nlfkt4ol3t13u9upjdrx7dnl"); //     if ((last == NULL)
UNSUPPORTED("dcjahkpgs4yy496ikzaozniuw"); // 	|| (last->name[0] != fake.name[0])
UNSUPPORTED("4nyn87tawoh9ejder70ax0di4"); // 	|| (strcmp(last->name, fake.name))) {
UNSUPPORTED("c8e30ys418x2wrqsniggk3h2u"); // 	last = (hsvrgbacolor_t *) bsearch((void *) &fake,
UNSUPPORTED("13zn2zu4jxknhl31gd5np1hq9"); // 				      (void *) color_lib,
UNSUPPORTED("csk5c5fpsg633xn59o6ekp7kw"); // 				      sizeof(color_lib) /
UNSUPPORTED("326muvlkda3aeejlqnmab0unj"); // 				      sizeof(hsvrgbacolor_t), sizeof(fake),
UNSUPPORTED("92scws4jkdku0225nkuwyg3en"); // 				      colorcmpf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7wyv7w4rv25zss2ym7p8di9e"); //     if (last != NULL) {
UNSUPPORTED("cqoqa6pnbr8t180lly83arzbf"); // 	switch (target_type) {
UNSUPPORTED("8x2g67uo65ux7na4wn8yr2hoi"); // 	case HSVA_DOUBLE:
UNSUPPORTED("1p412ag7t6o00ebns760u1buq"); // 	    color->u.HSVA[0] = ((double) last->h) / 255.0;
UNSUPPORTED("3u2uzmxb21wo3n5ofhxvsos1j"); // 	    color->u.HSVA[1] = ((double) last->s) / 255.0;
UNSUPPORTED("1f7avk3ldrvt4iqyousfpakfa"); // 	    color->u.HSVA[2] = ((double) last->v) / 255.0;
UNSUPPORTED("9cutkzl8ytljfv3pdlcwwuo39"); // 	    color->u.HSVA[3] = ((double) last->a) / 255.0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4qyq5lb20pb6flw57yam37kot"); // 	case RGBA_BYTE:
UNSUPPORTED("euv8extiyg9ojwarxbdg173x0"); // 	    color->u.rgba[0] = last->r;
UNSUPPORTED("ew13r0uorvrr11af042y87wox"); // 	    color->u.rgba[1] = last->g;
UNSUPPORTED("ama3ynaxltjqwoy88w1z0aarf"); // 	    color->u.rgba[2] = last->b;
UNSUPPORTED("1chs0ktvl6et5zijblcp8qlnr"); // 	    color->u.rgba[3] = last->a;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("b18nw968wk9mdxeq4ho72d8xp"); // 	case CMYK_BYTE:
UNSUPPORTED("yiknpgciig2uwoai0h23rlk8"); // 	    R = (last->r) / 255.0;
UNSUPPORTED("xlpnj1maqomtvccdwcrsa06q"); // 	    G = (last->g) / 255.0;
UNSUPPORTED("956if091oxzp9zfflftahx9vj"); // 	    B = (last->b) / 255.0;
UNSUPPORTED("7tttv6awjz9muyxywh3cs8h1a"); // 	    rgb2cmyk(R, G, B, &C, &M, &Y, &K);
UNSUPPORTED("dq3d8fxtdbd8vmi1ctrbouqys"); // 	    color->u.cmyk[0] = (int) C * 255;
UNSUPPORTED("1v32c1lsmzvu5i9ciubbfzgj8"); // 	    color->u.cmyk[1] = (int) M * 255;
UNSUPPORTED("8ilbscdq894a1u0mxx9qehekv"); // 	    color->u.cmyk[2] = (int) Y * 255;
UNSUPPORTED("4p9d1nzs3tw96wxlefrt3dbum"); // 	    color->u.cmyk[3] = (int) K * 255;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3hhhktkxv13skdg8qtbqfccvb"); // 	case RGBA_WORD:
UNSUPPORTED("2chrmq478c0dkbx2vtuzkttt3"); // 	    color->u.rrggbbaa[0] = last->r * 65535 / 255;
UNSUPPORTED("elxida3hf2w5648hreja3h77h"); // 	    color->u.rrggbbaa[1] = last->g * 65535 / 255;
UNSUPPORTED("7vfbr6rrlg6254zzkf6ezthyz"); // 	    color->u.rrggbbaa[2] = last->b * 65535 / 255;
UNSUPPORTED("9d0ijf90chv5xiy3wyak6svth"); // 	    color->u.rrggbbaa[3] = last->a * 65535 / 255;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1ap6qfmpr0qmmroo04n4v2y6p"); // 	case RGBA_DOUBLE:
UNSUPPORTED("c8i3r57st2or1lb0p5zmpjbel"); // 	    color->u.RGBA[0] = last->r / 255.0;
UNSUPPORTED("bad4ojyrfigi4mmom4cehxisg"); // 	    color->u.RGBA[1] = last->g / 255.0;
UNSUPPORTED("e69nmcysppzvuafruou5z51pw"); // 	    color->u.RGBA[2] = last->b / 255.0;
UNSUPPORTED("5x4y6hddq6bkpbfumqlnf287x"); // 	    color->u.RGBA[3] = last->a / 255.0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3963t7kz3wj9jmr4b42bh0ikz"); // 	case COLOR_STRING:
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8efm2ybg3ifrezi2q4fb03p57"); // 	case COLOR_INDEX:
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eitnhp9lhv04qoq5fn35tp7p2"); // 	return rc;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c2rphnmj3qjo7esjrr8lrf9n8"); //     /* if we're still here then we failed to find a valid color spec */
UNSUPPORTED("7g304hflsebasgkwe7p0ogfcf"); //     rc = 1;
UNSUPPORTED("7g1g78rlfxhrdbth346b6va3v"); //     switch (target_type) {
UNSUPPORTED("3xh9pvlzl0se6kai61hpvztb0"); //     case HSVA_DOUBLE:
UNSUPPORTED("77m5r1fi548i2a3r9s6x0gsce"); // 	color->u.HSVA[0] = color->u.HSVA[1] = color->u.HSVA[2] = 0.0;
UNSUPPORTED("8r2m62v2wseqye77wjj1iuzx5"); // 	color->u.HSVA[3] = 1.0; /* opaque */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1ke9k1couoeb4od2nwibgg0xu"); //     case RGBA_BYTE:
UNSUPPORTED("3f06oe6h7bl42mxja847kojce"); // 	color->u.rgba[0] = color->u.rgba[1] = color->u.rgba[2] = 0;
UNSUPPORTED("epcd3n4y8gy400i35b2dx3dfz"); // 	color->u.rgba[3] = 255;	/* opaque */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3setnlgc8vsi8j9jzph3w3tgw"); //     case CMYK_BYTE:
UNSUPPORTED("8moqdtdmwf0umamz8fe6mppyd"); // 	color->u.cmyk[0] =
UNSUPPORTED("66cowglrsnm9ygf0ojc1i0tka"); // 	    color->u.cmyk[1] = color->u.cmyk[2] = color->u.cmyk[3] = 0;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1gc399p944szfhhd18p1c2zde"); //     case RGBA_WORD:
UNSUPPORTED("a7056wzw9fyolfdizijjxbrnr"); // 	color->u.rrggbbaa[0] = color->u.rrggbbaa[1] = color->u.rrggbbaa[2] = 0;
UNSUPPORTED("4e0n7dmb7rj5h0oatmxqc3lv2"); // 	color->u.rrggbbaa[3] = 65535;	/* opaque */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("ix5z8u4vldva0mpwk1hnmx3s"); //     case RGBA_DOUBLE:
UNSUPPORTED("a86aiakru49t2tk7tg5y7ym3h"); // 	color->u.RGBA[0] = color->u.RGBA[1] = color->u.RGBA[2] = 0.0;
UNSUPPORTED("ck9hrfq4njkslj58o9vzim5wf"); // 	color->u.RGBA[3] = 1.0;	/* opaque */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8d4tzwdd3f608e3jl4lzyugu0"); //     case COLOR_STRING:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("aa8lnx1ghmaq1qg4pxuhfpz9d"); //     case COLOR_INDEX:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5bc9k4vsl6g7wejc5xefc5964"); //     return rc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6xt07sam57leaegn555zem71x
// static void rgba_wordToByte (int* rrggbbaa, unsigned char* rgba) 
public static Object rgba_wordToByte(Object... arg) {
UNSUPPORTED("bao9ostouhscklw6g4sr9d1ca"); // static void rgba_wordToByte (int* rrggbbaa, unsigned char* rgba)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("a2n8aqfq0cqpx8elstmfn9oq6"); //     for (i = 0; i < 4; i++) {
UNSUPPORTED("56z2bopl8zqqjgty1iei0mdz8"); // 	rgba[i] = rrggbbaa[i] * 255 / 65535;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6yolsjak65tidlbndbkdir4zi
// static void rgba_dblToByte (double* RGBA, unsigned char* rgba) 
public static Object rgba_dblToByte(Object... arg) {
UNSUPPORTED("44ty1s8y3t609jxlz3dhwo8up"); // static void rgba_dblToByte (double* RGBA, unsigned char* rgba)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("a2n8aqfq0cqpx8elstmfn9oq6"); //     for (i = 0; i < 4; i++) {
UNSUPPORTED("7top6cd4psdex6hfkw4d92a5z"); // 	rgba[i] = (unsigned char)(RGBA[i] * 255);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4u0xx0imm7gw6d27a58wt7uhd
// int colorCvt(gvcolor_t *ocolor, gvcolor_t *ncolor) 
public static Object colorCvt(Object... arg) {
UNSUPPORTED("crf94filr3nvamikn2fv9l283"); // int colorCvt(gvcolor_t *ocolor, gvcolor_t *ncolor)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("cf8w5z35zxww7q6yjin9lc9op"); //     char buf[BUFSIZ];
UNSUPPORTED("yiuh599p05f2mpu2e3pesu2o"); //     char* s;
UNSUPPORTED("7n0bnxi86o1eioqyouz3afqm6"); //     unsigned char rgba[4];
UNSUPPORTED("4rev51u4r7578qsqtboqis36t"); //     if (ocolor->type == ncolor->type) {
UNSUPPORTED("b5cexskqcmrvkru1pwloy29ap"); // 	memcpy (&ncolor->u, &ocolor->u, sizeof(ocolor->u));    
UNSUPPORTED("awjmnc3xe191yrjoav96x4t6i"); // 	return 0; 
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("29yartnlkg78jtqss7wvr2ukx"); //     s = buf;
UNSUPPORTED("bprjantrlt6985c2t92ljx17q"); //     switch (ocolor->type) {
UNSUPPORTED("85pg3jiffmuxf208m722qskve"); //     case HSVA_DOUBLE :
UNSUPPORTED("b61kxubde7agaa67lf3047gw9"); // 	sprintf (buf, "%.03f %.03f %.03f %.03f", 
UNSUPPORTED("ecz167vvi43rqv3i1be6y9d44"); // 	    ocolor->u.HSVA[0], ocolor->u.HSVA[1], ocolor->u.HSVA[2], ocolor->u.HSVA[3]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dxegdbspzy9czkr6nd5x8poe1"); //     case RGBA_BYTE :
UNSUPPORTED("bdfv0m39061r7mieslqyunk37"); // 	sprintf (buf, "#%02x%02x%02x%02x", 
UNSUPPORTED("eavvghusqadvoi5m9t899gywv"); // 	    ocolor->u.rgba[0], ocolor->u.rgba[1], ocolor->u.rgba[2], ocolor->u.rgba[3]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1gc399p944szfhhd18p1c2zde"); //     case RGBA_WORD:
UNSUPPORTED("ahox2to99gp3ku632fit5yh2w"); // 	rgba_wordToByte (ocolor->u.rrggbbaa, rgba);
UNSUPPORTED("dvj2lr88uw99nw4a2fsu74quo"); // 	sprintf (buf, "#%02x%02x%02x%02x", rgba[0], rgba[1], rgba[2], rgba[3]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("ix5z8u4vldva0mpwk1hnmx3s"); //     case RGBA_DOUBLE:
UNSUPPORTED("a04b7du8qje7yf4tbvp5yadym"); // 	rgba_dblToByte (ocolor->u.RGBA, rgba);
UNSUPPORTED("dvj2lr88uw99nw4a2fsu74quo"); // 	sprintf (buf, "#%02x%02x%02x%02x", rgba[0], rgba[1], rgba[2], rgba[3]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8d4tzwdd3f608e3jl4lzyugu0"); //     case COLOR_STRING:
UNSUPPORTED("e7srlpjuk0ycck3ei9y4xotvg"); // 	s = ocolor->u.string;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8p4y43r1r9vccmnk4h3h083v9"); //     case CMYK_BYTE :
UNSUPPORTED("1z040zrivaz8vtuh8w8a4mqsn"); // 	/* agerr (AGWARN, "Input color type 'CMYK_BYTE' not supported for conversion\n"); */
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("aa8lnx1ghmaq1qg4pxuhfpz9d"); //     case COLOR_INDEX:
UNSUPPORTED("99jz8sfo7x4ocdi773b6ja0za"); // 	/* agerr (AGWARN, "Input color type 'COLOR_INDEX' not supported for conversion\n"); */
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("dxk2qiq2wg8ckisq3kx3k8wd"); // 	/* agerr (AGWARN, "Unknown input color type value '%u'\n", ncolor->type); */
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("eq82slwrwm0pcpqius7pgtlrn"); //     rc = colorxlate (s, ncolor, ncolor->type);
UNSUPPORTED("5bc9k4vsl6g7wejc5xefc5964"); //     return rc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 idov6ir1ql05vewhvosl6zmm
// void setColorScheme (char* s) 
public static Object setColorScheme(Object... arg) {
UNSUPPORTED("aae50vjirjpwewp57f09nxgqi"); // void setColorScheme (char* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9d6vms1yygd86dcdp61pb75o5"); //     colorscheme = s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
