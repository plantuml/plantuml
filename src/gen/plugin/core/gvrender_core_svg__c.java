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
package gen.plugin.core;
import static smetana.core.Macro.UNSUPPORTED;

public class gvrender_core_svg__c {
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


//1 bvcf56ia0p6vpyltaakksum8n
// static char *sdasharray = 


//1 32o9ll4py294p7pqt1k953d4w
// static char *sdotarray = 




//3 827ujd356a39nqzblmaenq91m
// static void svg_bzptarray(GVJ_t * job, pointf * A, int n) 
public static Object svg_bzptarray(Object... arg) {
UNSUPPORTED("cazk54t3k9gr91cuy8thjwn1l"); // static void svg_bzptarray(GVJ_t * job, pointf * A, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("wrvu9u7a8j6i6y6552zncxfk"); //     char c;
UNSUPPORTED("6828kr12yxx3rtky06j9ffxcx"); //     c = 'M';			/* first point */
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("e6mg6rzvpgtjw4cqzq98shznt"); // 	gvprintf(job, "%c%g,%g", c, A[i].x, -A[i].y);
UNSUPPORTED("82djk3ejfh3b927h8m60dmvut"); // 	if (i == 0)
UNSUPPORTED("96i2xuik7cifcs9jthuh45l7g"); // 	    c = 'C';		/* second point */
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("5xdp61gfqysw7d4xy1nav6fbf"); // 	    c = ' ';		/* remaining points */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ayourvjaaejk97eijt6e5i8v3
// static void svg_print_color(GVJ_t * job, gvcolor_t color) 
public static Object svg_print_color(Object... arg) {
UNSUPPORTED("2fy9dz4og1ebbt4qo1ewgqlj6"); // static void svg_print_color(GVJ_t * job, gvcolor_t color)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hn9tht7vq6fnuzbw3mgpwl6i"); //     switch (color.type) {
UNSUPPORTED("8d4tzwdd3f608e3jl4lzyugu0"); //     case COLOR_STRING:
UNSUPPORTED("13cycml8lfb1s4abkvnuhd8dh"); // 	gvputs(job, color.u.string);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1ke9k1couoeb4od2nwibgg0xu"); //     case RGBA_BYTE:
UNSUPPORTED("abau3frv1i9j8mksgb01245yj"); // 	if (color.u.rgba[3] == 0)	/* transparent */
UNSUPPORTED("azlrdflufy5e04p9xhwe83phw"); // 	    gvputs(job, "none");
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("7jq54scftd6wr5k5aefwuq5r4"); // 	    gvprintf(job, "#%02x%02x%02x",
UNSUPPORTED("625ktjnk20h4ovl2ahumf26wi"); // 		     color.u.rgba[0], color.u.rgba[1], color.u.rgba[2]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("3yz4iw4v7mm470if4vb6yaack"); // 	assert(0);		/* internal error */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ewal0ri6n9uxj2sbig9gocp29
// static void svg_grstyle(GVJ_t * job, int filled, int gid) 
public static Object svg_grstyle(Object... arg) {
UNSUPPORTED("7elkc6m9imb8varjnzb30xb02"); // static void svg_grstyle(GVJ_t * job, int filled, int gid)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("c2x5gho26hata2tuomem5yvki"); //     gvputs(job, " fill=\"");
UNSUPPORTED("6ix1vu2jcuffbc6bfez9ob1bm"); //     if (filled == 2) {
UNSUPPORTED("dvtv0kwd2akm1jigbiozoiqw5"); // 	gvprintf(job, "url(#l_%d)", gid);
UNSUPPORTED("efd7920d59k4ry70p4ywahhv7"); //     } else if (filled == 3) {
UNSUPPORTED("477jazwco6vx5txrrroe5v8z5"); // 	gvprintf(job, "url(#r_%d)", gid);
UNSUPPORTED("1vkwrzn8i2sf7fo7hsb8koiqs"); //     } else if (filled) {
UNSUPPORTED("4gxgoulqqxervkv6xlv14to0z"); // 	svg_print_color(job, obj->fillcolor);
UNSUPPORTED("7qngwi9a7zbb7rz4n2iflsxnb"); // 	if (obj->fillcolor.type == RGBA_BYTE
UNSUPPORTED("2pteb6b7jxu3tu27ixhdkxkwr"); // 	    && obj->fillcolor.u.rgba[3] > 0
UNSUPPORTED("chj56ql2g9vj2sr7r5vc0ot60"); // 	    && obj->fillcolor.u.rgba[3] < 255)
UNSUPPORTED("ckmt75f4ouzjccut0i5hbpjg1"); // 	    gvprintf(job, "\" fill-opacity=\"%f",
UNSUPPORTED("deszrkdt62tz6uhj3w9wa76jm"); // 		     ((float) obj->fillcolor.u.rgba[3] / 255.0));
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("30hha2b9y77pqqb6gvdbgcm43"); // 	gvputs(job, "none");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5e5xi4p9sv1ne0igfwfygfhru"); //     gvputs(job, "\" stroke=\"");
UNSUPPORTED("9gt78yg9a3ph8mbbfsrn902ib"); //     svg_print_color(job, obj->pencolor);
UNSUPPORTED("eeukvd8elh15jnqvrpkyb0m4q"); //     if (obj->penwidth != 1.)
UNSUPPORTED("4oq6l59kmwm1i8icb12lytqrk"); // 	gvprintf(job, "\" stroke-width=\"%g", obj->penwidth);
UNSUPPORTED("djo67r65tyuacmqc3z1ydjwqz"); //     if (obj->pen == PEN_DASHED) {
UNSUPPORTED("a4974y0ir8mq99xudu33ptkas"); // 	gvprintf(job, "\" stroke-dasharray=\"%s", sdasharray);
UNSUPPORTED("8lyt1l1t8621bzmtn7js5zd27"); //     } else if (obj->pen == PEN_DOTTED) {
UNSUPPORTED("427m3m7ogg0sx6ys62nnfa6dl"); // 	gvprintf(job, "\" stroke-dasharray=\"%s", sdotarray);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9iyzhe3jrf11fsglobdu8z73k"); //     if (obj->pencolor.type == RGBA_BYTE && obj->pencolor.u.rgba[3] > 0
UNSUPPORTED("77a9crd4hjv6i9wqpfa5vc90d"); // 	&& obj->pencolor.u.rgba[3] < 255)
UNSUPPORTED("mhthumv7lzyftxticeftd06x"); // 	gvprintf(job, "\" stroke-opacity=\"%f",
UNSUPPORTED("89yrujjhfpvunosus21e587nh"); // 		 ((float) obj->pencolor.u.rgba[3] / 255.0));
UNSUPPORTED("37rm205a74loh45pr3ipamp7f"); //     gvputs(job, "\"");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 29mng6on9ihwuvj7oo9qtysys
// static void svg_comment(GVJ_t * job, char *str) 
public static Object svg_comment(Object... arg) {
UNSUPPORTED("5l1vlrhw8bkx7tct31qeo6151"); // static void svg_comment(GVJ_t * job, char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6944t04aqfgp80pwqjz0owo0u"); //     gvputs(job, "<!-- ");
UNSUPPORTED("2lin9wqy8wibbp6ena4y1v99d"); //     gvputs(job, xml_string(str));
UNSUPPORTED("brgolh5cqbe2pwfd8jorg0hy5"); //     gvputs(job, " -->\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6mwdhf5su8dr537zquta04atq
// static void svg_begin_job(GVJ_t * job) 
public static Object svg_begin_job(Object... arg) {
UNSUPPORTED("4e7xpfs5qzccui3jexpu1pqoe"); // static void svg_begin_job(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("ayed4u3e3kuzbpmriet6gf3jg"); //     gvputs(job,
UNSUPPORTED("4gl5ch5bkikklz0xjqvf4kwku"); // 	   "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
UNSUPPORTED("dydthgf8fenq2b363w8o1seqw"); //     if ((s = agget(job->gvc->g, "stylesheet")) && s[0]) {
UNSUPPORTED("eucq5lq6nxhzzmxfzcixi5h4f"); // 	gvputs(job, "<?xml-stylesheet href=\"");
UNSUPPORTED("3cuw5zkbwgq5q6fre6xhaux9i"); // 	gvputs(job, s);
UNSUPPORTED("dhtfgzltn0mebjqta3k23h79u"); // 	gvputs(job, "\" type=\"text/css\"?>\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ctj0hnvfmv4c6uy6y5b9bel4o"); //     gvputs(job, "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n");
UNSUPPORTED("ayed4u3e3kuzbpmriet6gf3jg"); //     gvputs(job,
UNSUPPORTED("6lodqsqrqwzds9ywi2kmfdtj"); // 	   " \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
UNSUPPORTED("9e78axv4zzrr2igh04wi6f9t7"); //     gvputs(job, "<!-- Generated by ");
UNSUPPORTED("347xz1ktdhgxewjoclwojjaoy"); //     gvputs(job, xml_string(job->common->info[0]));
UNSUPPORTED("cx22tlhx29vhu1t7qu0qa4qq7"); //     gvputs(job, " version ");
UNSUPPORTED("8m3kmxpj612kak9br7vf4hj1c"); //     gvputs(job, xml_string(job->common->info[1]));
UNSUPPORTED("85rsmmnqjhjgun71czh20mlqf"); //     gvputs(job, " (");
UNSUPPORTED("5udbfg3gbonorzuwnnzgfcyfu"); //     gvputs(job, xml_string(job->common->info[2]));
UNSUPPORTED("2e9opn4z98non8389n3x7cvfj"); //     gvputs(job, ")\n");
UNSUPPORTED("brgolh5cqbe2pwfd8jorg0hy5"); //     gvputs(job, " -->\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6u4qt4f63eb32j74vgthrvm3k
// static void svg_begin_graph(GVJ_t * job) 
public static Object svg_begin_graph(Object... arg) {
UNSUPPORTED("etwt71rr36sz8xg8iwyo3lxpm"); // static void svg_begin_graph(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("86eziot3ia26vo3saxgg09478"); //     gvputs(job, "<!--");
UNSUPPORTED("a140rb3spyp62aewcsebcj6lz"); //     if (agnameof(obj->u.g)[0]) {
UNSUPPORTED("c4bysvuna8fctosov2vgh6i0k"); // 	gvputs(job, " Title: ");
UNSUPPORTED("56224uoiibb4gtpsqio4wdtya"); // 	gvputs(job, xml_string(agnameof(obj->u.g)));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1kqcp6kyya5y18d3gvqmrpeya"); //     gvprintf(job, " Pages: %d -->\n",
UNSUPPORTED("1ej7utiu4i2qo6za5fe4yt3mk"); // 	     job->pagesArraySize.x * job->pagesArraySize.y);
UNSUPPORTED("bvmajkx0qvgcj8bp8btgb0aat"); //     gvprintf(job, "<svg width=\"%dpt\" height=\"%dpt\"\n",
UNSUPPORTED("bcpqsycur1np13nncrbpucphy"); // 	     job->width, job->height);
UNSUPPORTED("6rttd8yqbzg3ge9sbgwv1rc7f"); //     gvprintf(job, " viewBox=\"%.2f %.2f %.2f %.2f\"",
UNSUPPORTED("1tgix2fnrgqsbtjao4h5qa8y8"); // 	job->canvasBox.LL.x,
UNSUPPORTED("clzvcjx7z45oixlhix29si3iq"); // 	job->canvasBox.LL.y,
UNSUPPORTED("4l8ealx1li6nrviboxdy946k5"); // 	job->canvasBox.UR.x,
UNSUPPORTED("s6k2ogvbwyc0koya5xkl6qz7"); // 	job->canvasBox.UR.y);
UNSUPPORTED("2bzmoimqon8kjq0pd5r46qm39"); //     /* namespace of svg */
UNSUPPORTED("4cefpshby7cvk6nh7o5k9dd1l"); //     gvputs(job, " xmlns=\"http://www.w3.org/2000/svg\"");
UNSUPPORTED("173phnczur0o8y4nl77tj7w4t"); //     /* namespace of xlink */
UNSUPPORTED("26qfcwfkirbk76zvu4fbvw4dg"); //     gvputs(job, " xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
UNSUPPORTED("8nnf9bugtewncr07cqxfrhd2f"); //     gvputs(job, ">\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2hipijo16x18tlxywsjp6iuco
// static void svg_end_graph(GVJ_t * job) 
public static Object svg_end_graph(Object... arg) {
UNSUPPORTED("4avti5hfwd5zukwiqponwsj4p"); // static void svg_end_graph(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("67oo7pytz2b3e3mgw1csct2it"); //     gvputs(job, "</svg>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4n6b17byr3am0te4kjrvg0out
// static void svg_begin_layer(GVJ_t * job, char *layername, int layerNum, 			    int numLayers) 
public static Object svg_begin_layer(Object... arg) {
UNSUPPORTED("1r9c8oevaa5ry2zdcz0aacjqk"); // static void svg_begin_layer(GVJ_t * job, char *layername, int layerNum,
UNSUPPORTED("19krtcvsu8mqw2qgui7bmaa55"); // 			    int numLayers)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a34kpoo0q6rr9pc4et7430a4v"); //     gvputs(job, "<g id=\"");
UNSUPPORTED("2t93t2mp7jlfucchywfl7iqgm"); //     gvputs(job, xml_string(layername));
UNSUPPORTED("baatl03568yj0ga379aoxrgyg"); //     gvputs(job, "\" class=\"layer\">\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7t2zqyq53yz5otut6qswu0dwl
// static void svg_end_layer(GVJ_t * job) 
public static Object svg_end_layer(Object... arg) {
UNSUPPORTED("944h0psiepf1zvjpjbct7pd04"); // static void svg_end_layer(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3hlnrvjmgpq1q1u2yiwyshlu7"); //     gvputs(job, "</g>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9xak74k7uwv1dmzpqrrjngk0d
// static void svg_begin_page(GVJ_t * job) 
public static Object svg_begin_page(Object... arg) {
UNSUPPORTED("603r99thfc1ydss48z9z4a4xq"); // static void svg_begin_page(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("8idc8bxfl4rmppt1hunj7lyc8"); //     /* its really just a page of the graph, but its still a graph,
UNSUPPORTED("4ftdfj0v4nvlad96sxezwsljv"); //      * and it is the entire graph if we're not currently paging */
UNSUPPORTED("a34kpoo0q6rr9pc4et7430a4v"); //     gvputs(job, "<g id=\"");
UNSUPPORTED("dgdxcjjx6rzf366dzj15iidcd"); //     gvputs(job, xml_string(obj->id));
UNSUPPORTED("5ad32c614mvtmwco7ib5x86r5"); //     gvputs(job, "\" class=\"graph\"");
UNSUPPORTED("a8yoal1dchk0nt5nro0jh2f29"); //     gvprintf(job,
UNSUPPORTED("2g7b7oapf1f994y3cbzhwtffs"); // 	     " transform=\"scale(%g %g) rotate(%d) translate(%g %g)\">\n",
UNSUPPORTED("5dcg2db7g2mpvt9u2skn5pvza"); // 	     job->scale.x, job->scale.y, -job->rotation,
UNSUPPORTED("8bf40j0648ock32z2279j44lw"); // 	     job->translation.x, -job->translation.y);
UNSUPPORTED("9ab0b7dzh2g3iqza181l2oauj"); //     /* default style */
UNSUPPORTED("a140rb3spyp62aewcsebcj6lz"); //     if (agnameof(obj->u.g)[0]) {
UNSUPPORTED("5kgm2a3rpk6s37lzlp8n3f1tc"); // 	gvputs(job, "<title>");
UNSUPPORTED("56224uoiibb4gtpsqio4wdtya"); // 	gvputs(job, xml_string(agnameof(obj->u.g)));
UNSUPPORTED("4rj6l5z1l0bf3ie9wy97io0dl"); // 	gvputs(job, "</title>\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3lrylfpzwboor5fazs5bzblqy
// static void svg_end_page(GVJ_t * job) 
public static Object svg_end_page(Object... arg) {
UNSUPPORTED("a63vtpfz17jfrg1v6k4ithnck"); // static void svg_end_page(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3hlnrvjmgpq1q1u2yiwyshlu7"); //     gvputs(job, "</g>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1i2x52qcsyl7b2n1uk3xo8bt2
// static void svg_begin_cluster(GVJ_t * job) 
public static Object svg_begin_cluster(Object... arg) {
UNSUPPORTED("5770ni8d11ktmtniojx88vupg"); // static void svg_begin_cluster(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("a34kpoo0q6rr9pc4et7430a4v"); //     gvputs(job, "<g id=\"");
UNSUPPORTED("dgdxcjjx6rzf366dzj15iidcd"); //     gvputs(job, xml_string(obj->id));
UNSUPPORTED("ak3v9uh54ekp5ra6l7niqmhir"); //     gvputs(job, "\" class=\"cluster\">");
UNSUPPORTED("2wuzvoqzbmnfp97tc2ns323h7"); //     gvputs(job, "<title>");
UNSUPPORTED("1y1fios3ue07ny9j6953yqqgl"); //     gvputs(job, xml_string(agnameof(obj->u.g)));
UNSUPPORTED("6c646bxlvxh07x9tspq8c6op6"); //     gvputs(job, "</title>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 12501qs5dpkc4yvizuoe3oszr
// static void svg_end_cluster(GVJ_t * job) 
public static Object svg_end_cluster(Object... arg) {
UNSUPPORTED("bf99mf7m7ypqllj8l88ybolew"); // static void svg_end_cluster(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3hlnrvjmgpq1q1u2yiwyshlu7"); //     gvputs(job, "</g>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 62sozvxvxh2xvvuz04updxsoj
// static void svg_begin_node(GVJ_t * job) 
public static Object svg_begin_node(Object... arg) {
UNSUPPORTED("6kr2t5s0pv6i3wkyf85csih71"); // static void svg_begin_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("a34kpoo0q6rr9pc4et7430a4v"); //     gvputs(job, "<g id=\"");
UNSUPPORTED("dgdxcjjx6rzf366dzj15iidcd"); //     gvputs(job, xml_string(obj->id));
UNSUPPORTED("3t9kr3ms5xoxx12pel4pthxrk"); //     if (job->layerNum > 1)
UNSUPPORTED("28x41kw3rxtipdm6pxbqd0m0x"); // 	gvprintf (job, "_%s", xml_string(job->gvc->layerIDs[job->layerNum]));
UNSUPPORTED("7brh7599jyc889z01a186atee"); //     gvputs(job, "\" class=\"node\">");
UNSUPPORTED("2wuzvoqzbmnfp97tc2ns323h7"); //     gvputs(job, "<title>");
UNSUPPORTED("enr2p4c7ke6vme870ssfffrvf"); //     gvputs(job, xml_string(agnameof(obj->u.n)));
UNSUPPORTED("6c646bxlvxh07x9tspq8c6op6"); //     gvputs(job, "</title>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2ssu7z7ncurc3v03vezyd70jd
// static void svg_end_node(GVJ_t * job) 
public static Object svg_end_node(Object... arg) {
UNSUPPORTED("e42ev4sec4fgoa1lxjoddv9vc"); // static void svg_end_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3hlnrvjmgpq1q1u2yiwyshlu7"); //     gvputs(job, "</g>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4lkaj8kdorw3nudimt40x1p0m
// static void svg_begin_edge(GVJ_t * job) 
public static Object svg_begin_edge(Object... arg) {
UNSUPPORTED("5wtieu2gn40jt7gdgind9bf4"); // static void svg_begin_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("6ujcykx18mkszn1x15d0ptd8m"); //     char *ename;
UNSUPPORTED("a34kpoo0q6rr9pc4et7430a4v"); //     gvputs(job, "<g id=\"");
UNSUPPORTED("dgdxcjjx6rzf366dzj15iidcd"); //     gvputs(job, xml_string(obj->id));
UNSUPPORTED("62xyzl6khtp7jteidlu55vcar"); //     gvputs(job, "\" class=\"edge\">");
UNSUPPORTED("2wuzvoqzbmnfp97tc2ns323h7"); //     gvputs(job, "<title>");
UNSUPPORTED("dakm0rgzt870aen1gs4yfkqvm"); //     ename = strdup_and_subst_obj("\\E", (void *) (obj->u.e));
UNSUPPORTED("4h38cdc7bvsawzk220q60efus"); //     gvputs(job, xml_string(ename));
UNSUPPORTED("1ptaw1megx8o1nxddb9cru8b1"); //     free(ename);
UNSUPPORTED("6c646bxlvxh07x9tspq8c6op6"); //     gvputs(job, "</title>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5jbl4ebzw11dwjfx2exyr6twj
// static void svg_end_edge(GVJ_t * job) 
public static Object svg_end_edge(Object... arg) {
UNSUPPORTED("9d2i3nxvvtquponlm7cw31rha"); // static void svg_end_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3hlnrvjmgpq1q1u2yiwyshlu7"); //     gvputs(job, "</g>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2ls1kg5xerh9e6l4ngof4km0p
// static void svg_begin_anchor(GVJ_t * job, char *href, char *tooltip, char *target, 		 char *id) 
public static Object svg_begin_anchor(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("4y9u0dzkdunolhcze5gpuysob"); // svg_begin_anchor(GVJ_t * job, char *href, char *tooltip, char *target,
UNSUPPORTED("6tix47kj9o1r14xs59rhq05qt"); // 		 char *id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("an2ri99x8tiwg2vb9fhwft10s"); //     gvputs(job, "<g");
UNSUPPORTED("8pqxqcleeeciqav1a5t9gvsve"); //     if (id) {
UNSUPPORTED("8w1reeeesybtngro76tt4bjon"); // 	gvputs(job, " id=\"a_");
UNSUPPORTED("5jktoqisch3gafbs6hrk6u6g4"); //         gvputs(job, xml_string(id));
UNSUPPORTED("bkiduymkqp04lhmsvl7ge0tkw"); //         gvputs(job, "\"");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b0wlwv4nycnh5og2scvone3i5"); //     gvputs(job, ">");
UNSUPPORTED("amqsywm4tf3cr4mn35bngqsho"); //     gvputs(job, "<a");
UNSUPPORTED("7nrpkvdw7jt6zu73cgb4nlpja"); //     if (href && href[0]) {
UNSUPPORTED("5tg8bvaati3lgq0381somsumq"); // 	gvputs(job, " xlink:href=\"");
UNSUPPORTED("sik5te2dbpsnb0nca0e9bimh"); // 	gvputs(job, href);
UNSUPPORTED("5dipay2vdinr7r0bjdg2vqthj"); // 	gvputs(job, "\"");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cbgv64izrpi5yclz55ewxzjgh"); //     if (tooltip && tooltip[0]) {
UNSUPPORTED("7zohbahnyu9ze1ip71rceikos"); // 	gvputs(job, " xlink:title=\"");
UNSUPPORTED("5ojsaz63da1o4m7k3t74iz86a"); // 	gvputs(job, xml_string(tooltip));
UNSUPPORTED("5dipay2vdinr7r0bjdg2vqthj"); // 	gvputs(job, "\"");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("45qrdhvnr0oph8gk1zzro3aqm"); //     if (target && target[0]) {
UNSUPPORTED("6dflw3q1nisark8k7vr6ygicz"); // 	gvputs(job, " target=\"");
UNSUPPORTED("d6xj22r3kcoobd3wdb15zc6ja"); // 	gvputs(job, xml_string(target));
UNSUPPORTED("5dipay2vdinr7r0bjdg2vqthj"); // 	gvputs(job, "\"");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8nnf9bugtewncr07cqxfrhd2f"); //     gvputs(job, ">\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4ddtxxh621w5w7uuokg15lfuk
// static void svg_end_anchor(GVJ_t * job) 
public static Object svg_end_anchor(Object... arg) {
UNSUPPORTED("96oojtu8n7eizsc1uma8aeq1f"); // static void svg_end_anchor(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("gdbgsnwooftm30av6d4pf8vp"); //     gvputs(job, "</a>\n");
UNSUPPORTED("3hlnrvjmgpq1q1u2yiwyshlu7"); //     gvputs(job, "</g>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 509rbvnnpffihwcds1q4o4el5
// static void svg_textspan(GVJ_t * job, pointf p, textspan_t * span) 
public static Object svg_textspan(Object... arg) {
UNSUPPORTED("cdh2myddciaij2jg2ygpu0f4d"); // static void svg_textspan(GVJ_t * job, pointf p, textspan_t * span)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("7pxhx3kruwsackat2315zxaxh"); //     PostscriptAlias *pA;
UNSUPPORTED("boqtrshd5fxyaq9o4qrp6ies"); //     char *family = NULL, *weight = NULL, *stretch = NULL, *style = NULL;
UNSUPPORTED("f0j3qhox7adw2oktxrx3z6n3b"); //     unsigned int flags;
UNSUPPORTED("6eq2wimnyond8hhyt7fj62jkq"); //     gvputs(job, "<text");
UNSUPPORTED("bpqxh9mig0sh1gasrlkg6hbph"); //     switch (span->just) {
UNSUPPORTED("15tf5rbprgr65ucp24e4bba9t"); //     case 'l':
UNSUPPORTED("256awp1qimuok9dpwcna489jx"); // 	gvputs(job, " text-anchor=\"start\"");
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8pu80wsk8me7q17ensqlviq86"); //     case 'r':
UNSUPPORTED("eyrb4ojjun5482g8v15qc73r3"); // 	gvputs(job, " text-anchor=\"end\"");
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("37fbny64zwo23oymypyreuldc"); //     case 'n':
UNSUPPORTED("3023vn5j4xt8ewwp1kfhet7lv"); // 	gvputs(job, " text-anchor=\"middle\"");
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c9qtg5nyqzscz54w8w9mfwv7v"); //     p.y += span->yoffset_centerline;
UNSUPPORTED("7l5wmvdydbzztglmyvtqj67ac"); //     gvprintf(job, " x=\"%g\" y=\"%g\"", p.x, -p.y);
UNSUPPORTED("5gvgwpzq8o9hz6bmkphkldr14"); //     pA = span->font->postscript_alias;
UNSUPPORTED("85v4mh1bambtpopgva0jmspn9"); //     if (pA) {
UNSUPPORTED("93b89673s4m4oux21ikja33by"); // 	switch (GD_fontnames(job->gvc->g)) {
UNSUPPORTED("7t6u02vcbbp8pgwlv0s475wy7"); // 	case PSFONTS:
UNSUPPORTED("1zo7hx1h667n22l58hefai6xu"); // 	    family = pA->name;
UNSUPPORTED("tzy3gy5xrkn2kylegnfq40gx"); // 	    weight = pA->weight;
UNSUPPORTED("4biccq2rftoyzak5v0rx6bbua"); // 	    style = pA->style;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("92e0ccltko27a70yl2qkogz66"); // 	case SVGFONTS:
UNSUPPORTED("d29jpipvsssvit4udg5m4rb9z"); // 	    family = pA->svg_font_family;
UNSUPPORTED("b7bano7beynz6cdjgdw5p10xh"); // 	    weight = pA->svg_font_weight;
UNSUPPORTED("9u0c8t1jl3ksdy4uqeau1ytm9"); // 	    style = pA->svg_font_style;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("8usgbe5tejhe3ynslgkg0pw79"); // 	case NATIVEFONTS:
UNSUPPORTED("e2iij4hncoq1z5mod74s7xb9l"); // 	    family = pA->family;
UNSUPPORTED("tzy3gy5xrkn2kylegnfq40gx"); // 	    weight = pA->weight;
UNSUPPORTED("4biccq2rftoyzak5v0rx6bbua"); // 	    style = pA->style;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("apakty8ulxpur721psab8qc25"); // 	stretch = pA->stretch;
UNSUPPORTED("f2ahbdlyhogvxic3wgumxlnx4"); // 	gvprintf(job, " font-family=\"%s", family);
UNSUPPORTED("dlvmrp3zu9by9thsalw30kor9"); // 	if (pA->svg_font_family)
UNSUPPORTED("32af7qmlteybi80xxc5ku3d6c"); // 	    gvprintf(job, ",%s", pA->svg_font_family);
UNSUPPORTED("5dipay2vdinr7r0bjdg2vqthj"); // 	gvputs(job, "\"");
UNSUPPORTED("1823tfo3ifqci6w34a3hp2jm3"); // 	if (weight)
UNSUPPORTED("erjhhd4wujeagj8jj8i553pyb"); // 	    gvprintf(job, " font-weight=\"%s\"", weight);
UNSUPPORTED("dddcuk8zw7ruwe8def3hcqhq1"); // 	if (stretch)
UNSUPPORTED("1gicoqs8k03tm9i7r2n7yvtaf"); // 	    gvprintf(job, " font-stretch=\"%s\"", stretch);
UNSUPPORTED("2k6etsm6v3ivdzk5e818fki9r"); // 	if (style)
UNSUPPORTED("dwa6mvk6ta3fyrds7hknr5e2l"); // 	    gvprintf(job, " font-style=\"%s\"", style);
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("9rrqjadroq7mnlchw9ylzerl5"); // 	gvprintf(job, " font-family=\"%s\"", span->font->name);
UNSUPPORTED("8pu27ywa7vnvu5xu1c6mfmdcb"); //     if ((span->font) && (flags = span->font->flags)) {
UNSUPPORTED("7ba191a6p5dly5azwbzrnq45c"); // 	if ((flags & (1 << 0)) && !weight)
UNSUPPORTED("4xpkdz7hajytfllktyx3iaoxg"); // 	    gvprintf(job, " font-weight=\"bold\"");
UNSUPPORTED("8hb411ksceuuz5uydsa7vfjfz"); // 	if ((flags & (1 << 1)) && !style)
UNSUPPORTED("ch4ehek8vqzttd65qrzb0wgm5"); // 	    gvprintf(job, " font-style=\"italic\"");
UNSUPPORTED("8lsjw9ngsxcsmcngqckbis5rr"); // 	if ((flags & ((1 << 2)|(1 << 5)|(1 << 6)))) {
UNSUPPORTED("5ceh1yy4z3eou7y6sb7daoij0"); // 	    int comma = 0;
UNSUPPORTED("4ol2se2oh595cmdhxyppj02k0"); // 	    gvprintf(job, " text-decoration=\"");
UNSUPPORTED("5rdoupdxxwd930xga1248jy1j"); // 	    if ((flags & (1 << 2))) {
UNSUPPORTED("5pt5lms126q1c044gxn7rma8m"); // 		gvprintf(job, "underline");
UNSUPPORTED("5cv6nleg9qer8wzue8yj3mhu2"); // 		comma = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4z5scs2etzrpry6bj0hqsph8v"); // 	    if ((flags & (1 << 6))) {
UNSUPPORTED("cipt1c28whuawjgtwpo5bgob3"); // 		gvprintf(job, "%soverline", (comma?",":""));
UNSUPPORTED("5cv6nleg9qer8wzue8yj3mhu2"); // 		comma = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("eqjv5jxaigje1i0o5dz9obtt5"); // 	    if ((flags & (1 << 5)))
UNSUPPORTED("29q3ccadncw279yksfy435h8g"); // 		gvprintf(job, "%sline-through", (comma?",":""));
UNSUPPORTED("8o2uvz97hn60l8j1okfocnsbl"); // 	    gvprintf(job, "\"");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9m7kf0pwe40hzlub06abvfeh9"); // 	if ((flags & (1 << 3)))
UNSUPPORTED("nr8krq8748jfkd0ispcblf9f"); // 	    gvprintf(job, " baseline-shift=\"super\"");
UNSUPPORTED("clacovmef99qjhl0yk3rl7piu"); // 	if ((flags & (1 << 4)))
UNSUPPORTED("1sfxrrei9occxyku20wyj1583"); // 	    gvprintf(job, " baseline-shift=\"sub\"");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5emn8p1yeoecdhojsizg5m36t"); //     gvprintf(job, " font-size=\"%.2f\"", span->font->size);
UNSUPPORTED("1ns59jz9xcw8otsxv6kgjgr5e"); //     switch (obj->pencolor.type) {
UNSUPPORTED("8d4tzwdd3f608e3jl4lzyugu0"); //     case COLOR_STRING:
UNSUPPORTED("b7fzwqzuy02m4jzy6m43xnpd1"); // 	if (strcasecmp(obj->pencolor.u.string, "black"))
UNSUPPORTED("f4bdj41dxfe9c90otbtpckefx"); // 	    gvprintf(job, " fill=\"%s\"", obj->pencolor.u.string);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1ke9k1couoeb4od2nwibgg0xu"); //     case RGBA_BYTE:
UNSUPPORTED("8ajndit7j1806751z61j7gfj9"); // 	gvprintf(job, " fill=\"#%02x%02x%02x\"",
UNSUPPORTED("5raiao60ivpnxzihbubbx93ls"); // 		 obj->pencolor.u.rgba[0], obj->pencolor.u.rgba[1],
UNSUPPORTED("1y0xlsu8sbuudl5682umhlese"); // 		 obj->pencolor.u.rgba[2]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("3yz4iw4v7mm470if4vb6yaack"); // 	assert(0);		/* internal error */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b0wlwv4nycnh5og2scvone3i5"); //     gvputs(job, ">");
UNSUPPORTED("b2d727ygdp49jzxj4mltky63u"); //     gvputs(job, xml_string0(span->str, NOT(0)));
UNSUPPORTED("dmq45ciur1vva2ppfymdb900x"); //     gvputs(job, "</text>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3gjy3fev22j4mgz0ihk6o8twf
// static int svg_gradstyle(GVJ_t * job, pointf * A, int n) 
public static Object svg_gradstyle(Object... arg) {
UNSUPPORTED("2bfo7hixlhvjvrfaafyyg88ar"); // static int svg_gradstyle(GVJ_t * job, pointf * A, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4umvnon5n2rousfurm6sgtb1j"); //     pointf G[2];
UNSUPPORTED("c7hjgducl4grlwhjwsinbtcee"); //     float angle;
UNSUPPORTED("egjk91955uvmmeb20amqowi0s"); //     static int gradId;
UNSUPPORTED("6fce7laz159a9nfec1weed9tk"); //     int id = gradId++;
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("3fni3vsly1h90xeevvu56dkuj"); //     angle = obj->gradient_angle * M_PI / 180;	//angle of gradient line
UNSUPPORTED("2qsw987g3l3zzvlx93ioi4gx2"); //     G[0].x = G[0].y = G[1].x = G[1].y = 0.;
UNSUPPORTED("3bsktxjgzrewl1tjtpyx52p3f"); //     get_gradient_points(A, G, n, angle, 0);	//get points on gradient line
UNSUPPORTED("a8yoal1dchk0nt5nro0jh2f29"); //     gvprintf(job,
UNSUPPORTED("aq4i0pf82yoa5atdyl1f1efy6"); // 	     "<defs>\n<linearGradient id=\"l_%d\" gradientUnits=\"userSpaceOnUse\" ", id);
UNSUPPORTED("2gw3pzkdg3dyzr6ps9goamqid"); //     gvprintf(job, "x1=\"%g\" y1=\"%g\" x2=\"%g\" y2=\"%g\" >\n", G[0].x,
UNSUPPORTED("biy1yz3z88ncz02663a4qs5fz"); // 	     G[0].y, G[1].x, G[1].y);
UNSUPPORTED("bzb8si8h6dzcj4rpku675ennh"); //     if (obj->gradient_frac > 0)
UNSUPPORTED("z96xvbayfas7iuiat2o7lacc"); // 	gvprintf(job, "<stop offset=\"%.03f\" style=\"stop-color:", obj->gradient_frac - 0.001);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("5j0brbmsc3uac75nrnbi7hbjg"); // 	gvputs(job, "<stop offset=\"0\" style=\"stop-color:");
UNSUPPORTED("atuzsdabcrol4e2016w782wyi"); //     svg_print_color(job, obj->fillcolor);
UNSUPPORTED("25yyjy09rw6wj1hem358rrtx5"); //     gvputs(job, ";stop-opacity:");
UNSUPPORTED("894falzn9ikb3ugxo7rf30v01"); //     if (obj->fillcolor.type == RGBA_BYTE && obj->fillcolor.u.rgba[3] > 0
UNSUPPORTED("7msi3z20gjxrpjeseoycqw22g"); // 	&& obj->fillcolor.u.rgba[3] < 255)
UNSUPPORTED("2dbabn5cv0kwtysa1rcpsrcq7"); // 	gvprintf(job, "%f", ((float) obj->fillcolor.u.rgba[3] / 255.0));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("czmoa1w0n08fmcmgutwcr3apz"); // 	gvputs(job, "1.");
UNSUPPORTED("dy96k8bewexhluplfg196pr0c"); //     gvputs(job, ";\"/>\n");
UNSUPPORTED("bzb8si8h6dzcj4rpku675ennh"); //     if (obj->gradient_frac > 0)
UNSUPPORTED("5vmqn1vuq3rb8jtl78xdov7zm"); // 	gvprintf(job, "<stop offset=\"%.03f\" style=\"stop-color:", obj->gradient_frac);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("3nhcni3eauuet4n0oul1y75zp"); // 	gvputs(job, "<stop offset=\"1\" style=\"stop-color:");
UNSUPPORTED("9xzivb2a3dwp100o6xzw78v20"); //     svg_print_color(job, obj->stopcolor);
UNSUPPORTED("25yyjy09rw6wj1hem358rrtx5"); //     gvputs(job, ";stop-opacity:");
UNSUPPORTED("69jkl15qlmjhgsrh61j09jxp9"); //     if (obj->stopcolor.type == RGBA_BYTE && obj->stopcolor.u.rgba[3] > 0
UNSUPPORTED("5yvxwp3rfgj1z2d6dri2q83cx"); // 	&& obj->stopcolor.u.rgba[3] < 255)
UNSUPPORTED("bivsghea127ronz4jnplynoy"); // 	gvprintf(job, "%f", ((float) obj->stopcolor.u.rgba[3] / 255.0));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("czmoa1w0n08fmcmgutwcr3apz"); // 	gvputs(job, "1.");
UNSUPPORTED("1iszsyeqv6zyj3zb7w2pfpu4i"); //     gvputs(job, ";\"/>\n</linearGradient>\n</defs>\n");
UNSUPPORTED("8jjtthatl5k37wbc9wfeogjct"); //     return id;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 67ww4214261qqk7os79dnxb91
// static int svg_rgradstyle(GVJ_t * job, pointf * A, int n) 
public static Object svg_rgradstyle(Object... arg) {
UNSUPPORTED("2qd44o0nwqzg9a83ie5f868kk"); // static int svg_rgradstyle(GVJ_t * job, pointf * A, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("anwmj45eahbja1x9uwxa4z67q"); //     /* pointf G[2]; */
UNSUPPORTED("c7hjgducl4grlwhjwsinbtcee"); //     float angle;
UNSUPPORTED("1gwihowmrjihhujhiqo20snqq"); //     int ifx, ify;
UNSUPPORTED("1v43j91cccrl5r4pl30a68se3"); //     static int rgradId;
UNSUPPORTED("bg4c10jkjriorbj66ijiyhvh0"); //     int id = rgradId++;
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("3fni3vsly1h90xeevvu56dkuj"); //     angle = obj->gradient_angle * M_PI / 180;	//angle of gradient line
UNSUPPORTED("a7sn2w4y0sbdrtnmup718yxk2"); //     /* G[0].x = G[0].y = G[1].x = G[1].y; */
UNSUPPORTED("ea160wg5wxr6lqtaddejmsqka"); //     /* get_gradient_points(A, G, n, 0, 1); */
UNSUPPORTED("ylvjn4yu1p8yxux37vu789na"); //     if (angle == 0.) {
UNSUPPORTED("2izgqowow6pmwmv27fiva3tvz"); // 	ifx = ify = 50;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("aum9vd35r7s7f9use8zf38z9k"); // 	ifx = 50 * (1 + cos(angle));
UNSUPPORTED("cm03wwy8l2b9wnla83wnp3msr"); // 	ify = 50 * (1 - sin(angle));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a8yoal1dchk0nt5nro0jh2f29"); //     gvprintf(job,
UNSUPPORTED("14gmgrq5aclflj13s6zyh43is"); // 	     "<defs>\n<radialGradient id=\"r_%d\" cx=\"50%%\" cy=\"50%%\" r=\"75%%\" fx=\"%d%%\" fy=\"%d%%\">\n",
UNSUPPORTED("l5wr5d3d8joaguf800asanct"); // 	     id, ifx, ify);
UNSUPPORTED("8ado4ojlrd7ipx162vau7fxvx"); //     gvputs(job, "<stop offset=\"0\" style=\"stop-color:");
UNSUPPORTED("atuzsdabcrol4e2016w782wyi"); //     svg_print_color(job, obj->fillcolor);
UNSUPPORTED("25yyjy09rw6wj1hem358rrtx5"); //     gvputs(job, ";stop-opacity:");
UNSUPPORTED("894falzn9ikb3ugxo7rf30v01"); //     if (obj->fillcolor.type == RGBA_BYTE && obj->fillcolor.u.rgba[3] > 0
UNSUPPORTED("7msi3z20gjxrpjeseoycqw22g"); // 	&& obj->fillcolor.u.rgba[3] < 255)
UNSUPPORTED("2dbabn5cv0kwtysa1rcpsrcq7"); // 	gvprintf(job, "%f", ((float) obj->fillcolor.u.rgba[3] / 255.0));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("czmoa1w0n08fmcmgutwcr3apz"); // 	gvputs(job, "1.");
UNSUPPORTED("dy96k8bewexhluplfg196pr0c"); //     gvputs(job, ";\"/>\n");
UNSUPPORTED("2ciprtix75p6n1rvrofzer09k"); //     gvputs(job, "<stop offset=\"1\" style=\"stop-color:");
UNSUPPORTED("9xzivb2a3dwp100o6xzw78v20"); //     svg_print_color(job, obj->stopcolor);
UNSUPPORTED("25yyjy09rw6wj1hem358rrtx5"); //     gvputs(job, ";stop-opacity:");
UNSUPPORTED("69jkl15qlmjhgsrh61j09jxp9"); //     if (obj->stopcolor.type == RGBA_BYTE && obj->stopcolor.u.rgba[3] > 0
UNSUPPORTED("5yvxwp3rfgj1z2d6dri2q83cx"); // 	&& obj->stopcolor.u.rgba[3] < 255)
UNSUPPORTED("bivsghea127ronz4jnplynoy"); // 	gvprintf(job, "%f", ((float) obj->stopcolor.u.rgba[3] / 255.0));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("czmoa1w0n08fmcmgutwcr3apz"); // 	gvputs(job, "1.");
UNSUPPORTED("82y1ud8efmpl12973l04ba0xk"); //     gvputs(job, ";\"/>\n</radialGradient>\n</defs>\n");
UNSUPPORTED("8jjtthatl5k37wbc9wfeogjct"); //     return id;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6179wcbuyu90qz6rpi6mgppxr
// static void svg_ellipse(GVJ_t * job, pointf * A, int filled) 
public static Object svg_ellipse(Object... arg) {
UNSUPPORTED("aeulny0gu30zb5sbdc2fb6i8q"); // static void svg_ellipse(GVJ_t * job, pointf * A, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5n5mg10ujef23pj2i3tfg98mb"); //     int gid = 0;
UNSUPPORTED("8tuvacxowvjyekfxkm6dgvp8f"); //     /* A[] contains 2 points: the center and corner. */
UNSUPPORTED("6ix1vu2jcuffbc6bfez9ob1bm"); //     if (filled == 2) {
UNSUPPORTED("408j2y19ppayrgj8rcylk5uej"); // 	gid = svg_gradstyle(job, A, 2);
UNSUPPORTED("1f3u2rbrimcdejw1fhkimfmtq"); //     } else if (filled == (3)) {
UNSUPPORTED("17dijdtidp3dm9zedxik5pte8"); // 	gid = svg_rgradstyle(job, A, 2);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("prza2073gtuthtmkfaeasaom"); //     gvputs(job, "<ellipse");
UNSUPPORTED("44lvhj72astmac0tsena4t4kl"); //     svg_grstyle(job, filled, gid);
UNSUPPORTED("489d499vl0saxr69uxc59e6gk"); //     gvprintf(job, " cx=\"%g\" cy=\"%g\"", A[0].x, -A[0].y);
UNSUPPORTED("5p4s338slnboz9u4hhlndj449"); //     gvprintf(job, " rx=\"%g\" ry=\"%g\"",
UNSUPPORTED("5m3fclgzymgaw2vbesy58fbmv"); // 	     A[1].x - A[0].x, A[1].y - A[0].y);
UNSUPPORTED("tkpriz9q4d4xzeglsnt7kh4c"); //     gvputs(job, "/>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4voh5pqlltjszwhlaqt8qc6h
// static void svg_bezier(GVJ_t * job, pointf * A, int n, int arrow_at_start, 	   int arrow_at_end, int filled) 
public static Object svg_bezier(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("4b2sf54o9xlmuusu6iujw6tzo"); // svg_bezier(GVJ_t * job, pointf * A, int n, int arrow_at_start,
UNSUPPORTED("dxw18xwwo4n9p0pz03h53njzw"); // 	   int arrow_at_end, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bva9x0qdlhvut574wqidcp51o"); //   int gid = 0;
UNSUPPORTED("6ix1vu2jcuffbc6bfez9ob1bm"); //     if (filled == 2) {
UNSUPPORTED("336pt1lxutdhk6cn1yrrnb31g"); // 	gid = svg_gradstyle(job, A, n);
UNSUPPORTED("1f3u2rbrimcdejw1fhkimfmtq"); //     } else if (filled == (3)) {
UNSUPPORTED("7ykd3tlli3mnnab2q0uiotvhd"); // 	gid = svg_rgradstyle(job, A, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2wytgj2mvbp924ox0n93mx0s2"); //     gvputs(job, "<path");
UNSUPPORTED("44lvhj72astmac0tsena4t4kl"); //     svg_grstyle(job, filled, gid);
UNSUPPORTED("crna7isti1xjrskicrwpceytv"); //     gvputs(job, " d=\"");
UNSUPPORTED("7z3cge42455yz8j4yhqat1ld4"); //     svg_bzptarray(job, A, n);
UNSUPPORTED("50f5fiyjauli4r1mevk4hrly"); //     gvputs(job, "\"/>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7ibkd0we1erd0sjwcq0769ynl
// static void svg_polygon(GVJ_t * job, pointf * A, int n, int filled) 
public static Object svg_polygon(Object... arg) {
UNSUPPORTED("bs95s3hzmbofgepmna889krqu"); // static void svg_polygon(GVJ_t * job, pointf * A, int n, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9rv93hlp64hdng1p33rot7lfv"); //     int i, gid = 0;
UNSUPPORTED("6ix1vu2jcuffbc6bfez9ob1bm"); //     if (filled == 2) {
UNSUPPORTED("336pt1lxutdhk6cn1yrrnb31g"); // 	gid = svg_gradstyle(job, A, n);
UNSUPPORTED("1f3u2rbrimcdejw1fhkimfmtq"); //     } else if (filled == (3)) {
UNSUPPORTED("7ykd3tlli3mnnab2q0uiotvhd"); // 	gid = svg_rgradstyle(job, A, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("78z961zddfij8pb5rmwl4n2tm"); //     gvputs(job, "<polygon");
UNSUPPORTED("44lvhj72astmac0tsena4t4kl"); //     svg_grstyle(job, filled, gid);
UNSUPPORTED("boajpqbbfvqq1ct9fgwpo8qjo"); //     gvputs(job, " points=\"");
UNSUPPORTED("e6c6vkuvc5wlnup26rm248nss"); //     for (i = 0; i < n; i++)
UNSUPPORTED("74wzmowwfq9ffhq8vhb4k24lm"); // 	gvprintf(job, "%g,%g ", A[i].x, -A[i].y);
UNSUPPORTED("by9yye93q4s121zub7keng9a7"); //     gvprintf(job, "%g,%g", A[0].x, -A[0].y);	/* because Adobe SVG is broken */
UNSUPPORTED("50f5fiyjauli4r1mevk4hrly"); //     gvputs(job, "\"/>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c8rn5juypta39ccw5pbh8pk49
// static void svg_polyline(GVJ_t * job, pointf * A, int n) 
public static Object svg_polyline(Object... arg) {
UNSUPPORTED("38urwxhvfthoaqtp3stv44nr7"); // static void svg_polyline(GVJ_t * job, pointf * A, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6g8w45vwmydzwnarkgxk5lr6g"); //     gvputs(job, "<polyline");
UNSUPPORTED("67tcb9we4il6fxoqbulgwetsc"); //     svg_grstyle(job, 0, 0);
UNSUPPORTED("boajpqbbfvqq1ct9fgwpo8qjo"); //     gvputs(job, " points=\"");
UNSUPPORTED("e6c6vkuvc5wlnup26rm248nss"); //     for (i = 0; i < n; i++)
UNSUPPORTED("74wzmowwfq9ffhq8vhb4k24lm"); // 	gvprintf(job, "%g,%g ", A[i].x, -A[i].y);
UNSUPPORTED("50f5fiyjauli4r1mevk4hrly"); //     gvputs(job, "\"/>\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 b9hjnmz91lrhcgjntnr3pxxg8
// static char *svg_knowncolors[] = 


//1 4e1dh88puzpsk9gnuuog4v6n
// gvrender_engine_t svg_engine = 


//1 eaglivfetg4iko7wbadzatuu1
// gvrender_features_t render_features_svg = 


//1 4n103oc5z61rm55i81psizksi
// gvdevice_features_t device_features_svg = 


//1 bd33dnvj2ff9ha2832sa7e0u8
// gvdevice_features_t device_features_svgz = 


//1 c8ccvawathznbo5pws5jwz6q0
// gvplugin_installed_t gvrender_svg_types[] = 


//1 3g6v0vqqku0vco0few4b26in8
// gvplugin_installed_t gvdevice_svg_types[] = 


}
