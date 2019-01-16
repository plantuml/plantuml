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

public class gvrender_core_ps__c {
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


//1 1qtflsog3urfbtfk7yequcb6l
// static const char *ps_txt[] = 


//1 7fq6ozjgi243ox7yrdb2ep5lx
// static int isLatin1


//1 u3p8me9bh8fnyshn034rzpzo
// static char setupLatin1




//3 4jsbd8kmwbzb2dkxcq0akopqt
// static void psgen_begin_job(GVJ_t * job) 
public static Object psgen_begin_job(Object... arg) {
UNSUPPORTED("9orw8eslfn5kltlv212oz9m4t"); // static void psgen_begin_job(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2gqkshnzesys4oyr4v83wtm7r"); //     gvputs(job, "%!PS-Adobe-3.0");
UNSUPPORTED("10cxufp7bdbg3focj7i2ji75x"); //     if (job->render.id == FORMAT_EPS)
UNSUPPORTED("d7jryk3bdwpt507jq18ibacn5"); // 	gvputs(job, " EPSF-3.0\n");
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("ab9vno2hfka5wkcaq86o3njmq"); // 	gvputs(job, "\n");
UNSUPPORTED("dv3jmvt7myav44xtzcqkbrvpa"); //     gvprintf(job, "%%%%Creator: %s version %s (%s)\n",
UNSUPPORTED("4avcb2reh8m3qem3f2716wav3"); // 	    job->common->info[0], job->common->info[1], job->common->info[2]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3vsgq26dsfvpzsck4p2l68jv8
// static void psgen_end_job(GVJ_t * job) 
public static Object psgen_end_job(Object... arg) {
UNSUPPORTED("829ltuhwlzr8hozer6oad9sz5"); // static void psgen_end_job(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("djedkpe7zjd45r39k271iaxcu"); //     gvputs(job, "%%Trailer\n");
UNSUPPORTED("b3iu4ks8uomai9twz4bxnbipx"); //     if (job->render.id != FORMAT_EPS)
UNSUPPORTED("36ngacoh31azhdowttvvpjjk2"); //         gvprintf(job, "%%%%Pages: %d\n", job->common->viewNum);
UNSUPPORTED("dgmf05k9an1qfxjxd97hq1buj"); //     if (job->common->show_boxes == NULL)
UNSUPPORTED("6h3w6ikmuw9ywilwm2qpevp3l"); //         if (job->render.id != FORMAT_EPS)
UNSUPPORTED("18j4zj26y29rtjxd0cekzpfee"); // 	    gvprintf(job, "%%%%BoundingBox: %d %d %d %d\n",
UNSUPPORTED("dxbze4i780qyz19i2rh8y5jht"); // 	        job->boundingBox.LL.x, job->boundingBox.LL.y,
UNSUPPORTED("60out7dqilui46fgktwm5ej8r"); // 	        job->boundingBox.UR.x, job->boundingBox.UR.y);
UNSUPPORTED("c1p513mbeu7uv14dlqfw6abkg"); //     gvputs(job, "end\nrestore\n");
UNSUPPORTED("4uqbd6po1wgmrs3zrxz4ik9mw"); //     gvputs(job, "%%EOF\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3gb6ikypoueyb2y6i764cxbhx
// static void psgen_begin_graph(GVJ_t * job) 
public static Object psgen_begin_graph(Object... arg) {
UNSUPPORTED("apgmzjzeua7qxnlanxaxe3hua"); // static void psgen_begin_graph(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("bhf3m2sbf59gs5cg8rpaiywew"); //     setupLatin1 = 0;
UNSUPPORTED("8bju5ndiwsdb8q2ly29dv84wm"); //     if (job->common->viewNum == 0) {
UNSUPPORTED("7q41bmyw0rrgi4x6negigle3m"); //         gvprintf(job, "%%%%Title: %s\n", agnameof(obj->u.g));
UNSUPPORTED("5n912p3phcw0i5akike49jiqc"); //     	if (job->render.id != FORMAT_EPS)
UNSUPPORTED("8aqdygs4r7d8g3i6xp7mj8e50"); //             gvputs(job, "%%Pages: (atend)\n");
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("4vsc5buyrhwh1ovu2agkm6zjy"); // 	    gvputs(job, "%%Pages: 1\n");
UNSUPPORTED("d56ndgp8enkjfui03ixmhufw3"); //         if (job->common->show_boxes == NULL) {
UNSUPPORTED("w7ueydz0ch59r5x7jz39zgtp"); //     	    if (job->render.id != FORMAT_EPS)
UNSUPPORTED("bxgfbxaraautik8cspg1wmptv"); //                 gvputs(job, "%%BoundingBox: (atend)\n");
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("6wpp34s0c7tqg0wv4g8ltebzm"); // 	        gvprintf(job, "%%%%BoundingBox: %d %d %d %d\n",
UNSUPPORTED("1smwnfuueu6rmd1bgwt1zhvtx"); // 	            job->pageBoundingBox.LL.x, job->pageBoundingBox.LL.y,
UNSUPPORTED("6lr43m798s3534swmbhen5m0q"); // 	            job->pageBoundingBox.UR.x, job->pageBoundingBox.UR.y);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6q85d3glhzmompt5xmvj42zoa"); //         gvputs(job, "%%EndComments\nsave\n");
UNSUPPORTED("c9ohr5sqm5pydsv4h8o12ku9h"); //         /* include shape library */
UNSUPPORTED("sk0bwxtidaigb1oi4xkk10up"); //         cat_libfile(job, job->common->lib, ps_txt);
UNSUPPORTED("clqt6n30kglwpmnxf6ayp06x9"); // 	/* include epsf */
UNSUPPORTED("61opdwqs031lj4tzzg2sgcci2"); //         epsf_define(job);
UNSUPPORTED("6vg9px4q8r7fdndbyt64w62lt"); //         if (job->common->show_boxes) {
UNSUPPORTED("edalkp7jyj28icbidbcmfsz4y"); //             const char* args[2];
UNSUPPORTED("912tsmkymhqfhqp25qexxz6m5"); //             args[0] = job->common->show_boxes[0];
UNSUPPORTED("83o9dwn4xs47t734kcj652p5p"); //             args[1] = NULL;
UNSUPPORTED("7miirzhxs42qrp2ovadq92khu"); //             cat_libfile(job, NULL, args);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("33xxzmz1g6bggeapvxm1zx9yr"); //     isLatin1 = (GD_charset(obj->u.g) == 1 ? 1 : -1);
UNSUPPORTED("1xv4b98ox7mq8hhp97i19zfor"); //     /* We always setup Latin1. The charset info is always output,
UNSUPPORTED("1d9yupk35hp3i9e12yycbevaz"); //      * and installing it is cheap. With it installed, we can then
UNSUPPORTED("m5mzxzrnlpf7h3hy7np9zmd0"); //      * rely on ps_string to convert UTF-8 characters whose encoding
UNSUPPORTED("cajcmda1cmsbpwhy0mvm56dao"); //      * is in the range of Latin-1 into the Latin-1 equivalent and
UNSUPPORTED("eko1rvgwxxxitm2caqgogrvhq"); //      * get the expected PostScript output.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("a6gal7mia1c9lnenwqr2je5bh"); //     if (!setupLatin1) {
UNSUPPORTED("erhruwqa7hy0mxl27hsdhwjvq"); // 	gvputs(job, "setupLatin1\n");	/* as defined in ps header */
UNSUPPORTED("4yyacs2oqpdxm2x0r5pdup66w"); // 	setupLatin1 = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6szrbi81oehk1yhwceaheeuev"); //     /*  Set base URL for relative links (for Distiller >= 3.0)  */
UNSUPPORTED("4jozt8n5f1lyy86sk34jfrvgg"); //     if (obj->url)
UNSUPPORTED("36rstk5tbud6jtt4g6z61q808"); // 	gvprintf(job, "[ {Catalog} << /URI << /Base %s >> >>\n"
UNSUPPORTED("d3gnahi0ckutslapcmkiqsd7p"); // 		"/PUT pdfmark\n", ps_string(obj->url,isLatin1));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ar4xx7dz90q15xh2y4klc4num
// static void psgen_begin_layer(GVJ_t * job, char *layername, int layerNum, int numLayers) 
public static Object psgen_begin_layer(Object... arg) {
UNSUPPORTED("agsvap2lizd7txyml3u0vrp85"); // static void psgen_begin_layer(GVJ_t * job, char *layername, int layerNum, int numLayers)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("61nwew8w1g6gyi8sfcje734f0"); //     gvprintf(job, "%d %d setlayer\n", layerNum, numLayers);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7m0sjz6ewwd4y7tot8bmcfcei
// static void psgen_begin_page(GVJ_t * job) 
public static Object psgen_begin_page(Object... arg) {
UNSUPPORTED("cogyrlqcthcac2sfe3ms3b7v8"); // static void psgen_begin_page(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("76g96x1rehn6s0a72jz2no0bi"); //     box pbr = job->pageBoundingBox;
UNSUPPORTED("br2sfjk7c29me4e6ggop0ljk3"); //     gvprintf(job, "%%%%Page: %d %d\n",
UNSUPPORTED("17n0zsnmgi4zptncq1uvxywuu"); // 	    job->common->viewNum + 1, job->common->viewNum + 1);
UNSUPPORTED("dgmf05k9an1qfxjxd97hq1buj"); //     if (job->common->show_boxes == NULL)
UNSUPPORTED("68yf7k1sgpmnim51dqkg7iijt"); //         gvprintf(job, "%%%%PageBoundingBox: %d %d %d %d\n",
UNSUPPORTED("21d5hfu7mone3g6dbuen2sgg3"); // 	    pbr.LL.x, pbr.LL.y, pbr.UR.x, pbr.UR.y);
UNSUPPORTED("3x0ssk90v3lg33lsr6b4ula1p"); //     gvprintf(job, "%%%%PageOrientation: %s\n",
UNSUPPORTED("78a69y96uj20rf1bjroairawi"); // 	    (job->rotation ? "Landscape" : "Portrait"));
UNSUPPORTED("2ztdn9tzegcfj0564qx2gwyq0"); //     if (job->render.id == FORMAT_PS2)
UNSUPPORTED("3uk2omkh4h2oh9x6p6gdfgezn"); //         gvprintf(job, "<< /PageSize [%d %d] >> setpagedevice\n",
UNSUPPORTED("alr7wses0g3ewh8vkfvlk58fc"); //             pbr.UR.x, pbr.UR.y);
UNSUPPORTED("4h7sybrqpqww3pcpixnfe21z6"); //     gvprintf(job, "%d %d %d beginpage\n",
UNSUPPORTED("96jucytj8f7c6fy6z1gy6r1ze"); // 	    job->pagesArrayElem.x, job->pagesArrayElem.y, job->numPages);
UNSUPPORTED("dgmf05k9an1qfxjxd97hq1buj"); //     if (job->common->show_boxes == NULL)
UNSUPPORTED("5koijcwi38ci3a4qx13pvfwlw"); //         gvprintf(job, "gsave\n%d %d %d %d boxprim clip newpath\n",
UNSUPPORTED("9clbdsb3beptgdszw41ynvu0p"); // 	    pbr.LL.x, pbr.LL.y, pbr.UR.x-pbr.LL.x, pbr.UR.y-pbr.LL.y);
UNSUPPORTED("4a7e5k6q3rlacd537rx9ub20c"); //     gvprintf(job, "%g %g set_scale %d rotate %g %g translate\n",
UNSUPPORTED("10afwn16gj676dpglziigsm50"); // 	    job->scale.x, job->scale.y,
UNSUPPORTED("a1cdlhacjn1xlkc1yude0wrql"); // 	    job->rotation,
UNSUPPORTED("c7ifb58gy4lomkldshbldytbo"); // 	    job->translation.x, job->translation.y);
UNSUPPORTED("1z8ea74p9uxtwtwscul3o3jz7"); //     /*  Define the size of the PS canvas  */
UNSUPPORTED("4sr6w5mogwrv0l5izwjeu7ryc"); //     if (job->render.id == FORMAT_PS2) {
UNSUPPORTED("co3q2mi6gjxi3gchpln4w4567"); // 	if (pbr.UR.x >= 14400 || pbr.UR.y >= 14400)
UNSUPPORTED("31162iy3wjwmhqlutqv8nfc7d"); // 	    job->common->errorfn("canvas size (%d,%d) exceeds PDF limit (%d)\n"
UNSUPPORTED("an7h4u4y334blwkn06120ydt5"); // 		  "\t(suggest setting a bounding box size, see dot(1))\n",
UNSUPPORTED("c56zgl5pu3nxenerpmp3f3o0z"); // 		  pbr.UR.x, pbr.UR.y, 14400);
UNSUPPORTED("dxf3rvnawk3z9x2n179fzbxt9"); // 	gvprintf(job, "[ /CropBox [%d %d %d %d] /PAGES pdfmark\n",
UNSUPPORTED("ddbug0c0aj9lbqu8f829kzjvm"); // 		pbr.LL.x, pbr.LL.y, pbr.UR.x, pbr.UR.y);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7jxfd86j0csce75s68ekegiq6
// static void psgen_end_page(GVJ_t * job) 
public static Object psgen_end_page(Object... arg) {
UNSUPPORTED("5uicvg0h4t78pg6xobhau861e"); // static void psgen_end_page(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3u573a16zqopxfboi2w0zqqzz"); //     if (job->common->show_boxes) {
UNSUPPORTED("8uc8ww599n2y8x603qf4a4b06"); // 	gvputs(job, "0 0 0 edgecolor\n");
UNSUPPORTED("c67xlny46u43tnb3m46iy32o4"); // 	cat_libfile(job, NULL, job->common->show_boxes + 1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5flwz55ptjslg8li9ttksuap5"); //     /* the showpage is really a no-op, but at least one PS processor
UNSUPPORTED("9eh3687p6imh9z6upt3sok5ka"); //      * out there needs to see this literal token.  endpage does the real work.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("2jczhqdkf2v0tvqskulv69qq3"); //     gvputs(job, "endpage\nshowpage\ngrestore\n");
UNSUPPORTED("eof3ypspah8sg4phxnbptv9l"); //     gvputs(job, "%%PageTrailer\n");
UNSUPPORTED("48g7wddoi0h6l2ma38gg97vcs"); //     gvprintf(job, "%%%%EndPage: %d\n", job->common->viewNum);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 64yihbmjh014pmpusuctsl3sl
// static void psgen_begin_cluster(GVJ_t * job) 
public static Object psgen_begin_cluster(Object... arg) {
UNSUPPORTED("9m23jcvafreyz22sgraim1dr6"); // static void psgen_begin_cluster(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("e2btf3o0oasmpyurrr5iedyei"); //     gvprintf(job, "%% %s\n", agnameof(obj->u.g));
UNSUPPORTED("br61vl70v0xgjsjgnfhks64fr"); //     gvputs(job, "gsave\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3yzoaq9nyxn75ghsk94xknu7c
// static void psgen_end_cluster(GVJ_t * job) 
public static Object psgen_end_cluster(Object... arg) {
UNSUPPORTED("x7ci6jzq6e6cbwdkt9cbd1az"); // static void psgen_end_cluster(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8efwipnmlx4cesl8wwb0fxsi1"); //     gvputs(job, "grestore\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4gsfnvgb2qcux6uz1kdumc5vr
// static void psgen_begin_node(GVJ_t * job) 
public static Object psgen_begin_node(Object... arg) {
UNSUPPORTED("4ngrphh4a1xs5qfo6vgfzqgh0"); // static void psgen_begin_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("br61vl70v0xgjsjgnfhks64fr"); //     gvputs(job, "gsave\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 f0az3wc3ll61cwwyyr8dsyphs
// static void psgen_end_node(GVJ_t * job) 
public static Object psgen_end_node(Object... arg) {
UNSUPPORTED("ecuij4nzrh3ztsf6nmjoubsqu"); // static void psgen_end_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8efwipnmlx4cesl8wwb0fxsi1"); //     gvputs(job, "grestore\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6d5xpnziymgn6d0min85jfjvv
// static void psgen_begin_edge(GVJ_t * job) 
public static Object psgen_begin_edge(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("eni14d6lyslf5a4etuic39aa8"); // psgen_begin_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("br61vl70v0xgjsjgnfhks64fr"); //     gvputs(job, "gsave\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1smwxyj2mbjasopcdnl2snrpe
// static void psgen_end_edge(GVJ_t * job) 
public static Object psgen_end_edge(Object... arg) {
UNSUPPORTED("f2g7y2r104tklwtr280jfpf5h"); // static void psgen_end_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8efwipnmlx4cesl8wwb0fxsi1"); //     gvputs(job, "grestore\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b0pad7wyg5jnhkvyvxxfex8ij
// static void psgen_begin_anchor(GVJ_t *job, char *url, char *tooltip, char *target, char *id) 
public static Object psgen_begin_anchor(Object... arg) {
UNSUPPORTED("51xu7flm3qjo0cjkkrc57auwy"); // static void psgen_begin_anchor(GVJ_t *job, char *url, char *tooltip, char *target, char *id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("9cok0gxbmxzogxgwng4xmi787"); //     if (url && obj->url_map_p) {
UNSUPPORTED("18v6n3l39cjzkzv2qek8f5afv"); //         gvputs(job, "[ /Rect [ ");
UNSUPPORTED("chip985p4cjoasqpkgaiuru5f"); // 	gvprintpointflist(job, obj->url_map_p, 2);
UNSUPPORTED("672y6r8zwkhwb44q91feajtwe"); //         gvputs(job, " ]\n");
UNSUPPORTED("15sn5jwyf74feyieb8amxyl2n"); //         gvprintf(job, "  /Border [ 0 0 0 ]\n"
UNSUPPORTED("efeqhwnyncozgnojuosrolj8l"); // 		"  /Action << /Subtype /URI /URI %s >>\n"
UNSUPPORTED("5zen76z1f3ruolu46nam3b8sm"); // 		"  /Subtype /Link\n"
UNSUPPORTED("4xsvt507xerg3xtxu3l0co4zn"); // 		"/ANN pdfmark\n",
UNSUPPORTED("2xp015vqrvd5x2cu42yttmbvv"); // 		ps_string(url, isLatin1));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 azkjcny2ozhl5ngrex3xy1920
// static void ps_set_pen_style(GVJ_t *job) 
public static Object ps_set_pen_style(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("7tbc3p5jgba58vc3atp666q9o"); // ps_set_pen_style(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3jhg4yacyumgqbahinlar0ugn"); //     double penwidth = job->obj->penwidth;
UNSUPPORTED("9nzbcv0dhv0ufp0gxl6imgp7c"); //     char *p, *line, **s = job->obj->rawstyle;
UNSUPPORTED("a54kc8hkuqkvqgh8t0g3edgg6"); //     gvprintdouble(job, penwidth);
UNSUPPORTED("193mrkgt2kaa19s0ahu3qlpvn"); //     gvputs(job," setlinewidth\n");
UNSUPPORTED("exk7aqfswofs5s40p0nulkkbq"); //     while (s && (p = line = *s++)) {
UNSUPPORTED("996bsna1tw4r0r1j0ontx6960"); // 	if (strcmp(line, "setlinewidth") == 0)
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("yxnvzyld8ixj1ioqsioffpte"); // 	while (*p)
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("2wdgcrx402aszs54rq2kh9txd"); // 	p++;
UNSUPPORTED("1bnbwdqgkfyku19nvuttx2w2d"); // 	while (*p) {
UNSUPPORTED("32m73n62g8mlcoc4opqo15aug"); //             gvprintf(job,"%s ", p);
UNSUPPORTED("48rrlpibv3umwc6ofz2eg3007"); // 	    while (*p)
UNSUPPORTED("f0iluvyv5shsjqi6qh07pclgc"); // 		p++;
UNSUPPORTED("847zwwso12sey42b59zepembc"); // 	    p++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3uz4rodnpqcb2rgbcjlk95s2c"); // 	if (strcmp(line, "invis") == 0)
UNSUPPORTED("1tn42ug84sznim8sne8iif91k"); // 	    job->obj->penwidth = 0;
UNSUPPORTED("f3bcgmltuhfwlwib8wdxiq8vz"); // 	gvprintf(job, "%s\n", line);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ewmcmbew48t6xoasz1f2dg37q
// static void ps_set_color(GVJ_t *job, gvcolor_t *color) 
public static Object ps_set_color(Object... arg) {
UNSUPPORTED("97ps1rl0dp2n5licoxgmtrh4d"); // static void ps_set_color(GVJ_t *job, gvcolor_t *color)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aqb0odbaeg51ab9k2c5qo3x9x"); //     char *objtype;
UNSUPPORTED("aut7kl4l7gn4g3hxqkru7ilg4"); //     if (color) {
UNSUPPORTED("5loilaiyk3wcqxx79q7zkv8pe"); // 	switch (job->obj->type) {
UNSUPPORTED("4twjeggzt94nyvkfk4ngj7uqp"); // 	    case ROOTGRAPH_OBJTYPE:
UNSUPPORTED("bs92udgg6yjwdh38r8entr4c"); // 	    case CLUSTER_OBJTYPE:
UNSUPPORTED("4nfiirl3mvtumxrrh21gbk154"); // 		objtype = "graph";
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("c7xs8ak1x3jqu2f4z7haikpgw"); // 	    case NODE_OBJTYPE:
UNSUPPORTED("31u8h2s30zfu9ub2557w6y2n1"); // 		objtype = "node";
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("5cvje3cnq8nvl851sut6sdhs9"); // 	    case EDGE_OBJTYPE:
UNSUPPORTED("4aw3ehtuh69fgbi7j764udybx"); // 		objtype = "edge";
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("bt2g0yhsy3c7keqyftf3c98ut"); // 	    default:
UNSUPPORTED("6o2vs9xfrblfvi3oi2nzlvsaa"); // 		objtype = "sethsb";
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3xu9fluch6hl1rf5xj3p7bmwh"); // 	gvprintf(job, "%.5g %.5g %.5g %scolor\n",
UNSUPPORTED("bfjslqaz3l8lvo8opshvafth"); // 	    color->u.HSVA[0], color->u.HSVA[1], color->u.HSVA[2], objtype);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ay05ube2905przfamsvtiz6l6
// static void psgen_textspan(GVJ_t * job, pointf p, textspan_t * span) 
public static Object psgen_textspan(Object... arg) {
UNSUPPORTED("d3apfsoqjlwvrkzxkew5ymyrd"); // static void psgen_textspan(GVJ_t * job, pointf p, textspan_t * span)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("76nok3eiyr33qf4ecv69ujxn6"); //     char *str;
UNSUPPORTED("27583bfrnkeiouhqlzq21bpee"); //     if (job->obj->pencolor.u.HSVA[3] < .5)
UNSUPPORTED("6p588rupwy8yjjk196zz8qcvj"); // 	return;  /* skip transparent text */
UNSUPPORTED("78h44lidscrr82izbmmtwxpvy"); //     ps_set_color(job, &(job->obj->pencolor));
UNSUPPORTED("33ezxcuazz0b3dqe48eqxi81p"); //     gvprintdouble(job, span->font->size);
UNSUPPORTED("29wnsn5uix8ajohqm9brgm9gr"); //     gvprintf(job, " /%s set_font\n", span->font->name);
UNSUPPORTED("9mljtbtg6rnqp0ppoda72glkp"); //     str = ps_string(span->str,isLatin1);
UNSUPPORTED("bpqxh9mig0sh1gasrlkg6hbph"); //     switch (span->just) {
UNSUPPORTED("8pu80wsk8me7q17ensqlviq86"); //     case 'r':
UNSUPPORTED("lzwxwwf5wl644m2hvx2zioue"); //         p.x -= span->size.x;
UNSUPPORTED("bzxyddcf9jharsko2rb8asyik"); //         break;
UNSUPPORTED("15tf5rbprgr65ucp24e4bba9t"); //     case 'l':
UNSUPPORTED("5u1yeddr7gxqz7isver6zd56p"); //         p.x -= 0.0;
UNSUPPORTED("bzxyddcf9jharsko2rb8asyik"); //         break;
UNSUPPORTED("37fbny64zwo23oymypyreuldc"); //     case 'n':
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("e3odj4lftxsik5ovvxgbu6ddv"); //         p.x -= span->size.x / 2.0;
UNSUPPORTED("bzxyddcf9jharsko2rb8asyik"); //         break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c9qtg5nyqzscz54w8w9mfwv7v"); //     p.y += span->yoffset_centerline;
UNSUPPORTED("i7zx9lpif6lu4yrqqzsw2a4o"); //     gvprintpointf(job, p);
UNSUPPORTED("bj02ifjyeidil7b0nmgp8v2p3"); //     gvputs(job, " moveto ");
UNSUPPORTED("ckz29c0pawvdlkc5buih60dss"); //     gvprintdouble(job, span->size.x);
UNSUPPORTED("8lffoflzrv4a2q19ehsyhtrpb"); //     gvprintf(job, " %s alignedtext\n", str);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1g8u7srxnvq1rd50aunp0in6i
// static void psgen_ellipse(GVJ_t * job, pointf * A, int filled) 
public static Object psgen_ellipse(Object... arg) {
UNSUPPORTED("2jfirodbjd67iil3z6l362as"); // static void psgen_ellipse(GVJ_t * job, pointf * A, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8tuvacxowvjyekfxkm6dgvp8f"); //     /* A[] contains 2 points: the center and corner. */
UNSUPPORTED("6c99ntmslfdjq7lacoxq90io4"); //     pointf AA[2];
UNSUPPORTED("9m1ds5xbx68961mur5f3hmoa0"); //     AA[0] = A[0];
UNSUPPORTED("ad7su3a8dtyp8cqjizxb1fa0w"); //     AA[1].x = A[1].x - A[0].x;
UNSUPPORTED("2k9sojs8nidphiuatou9i7dmw"); //     AA[1].y = A[1].y - A[0].y;
UNSUPPORTED("2ream5c4u1f89xiwzlijg1ydk"); //     if (filled && job->obj->fillcolor.u.HSVA[3] > .5) {
UNSUPPORTED("ba0yi3he6a0tf9iisvs2r7lud"); // 	ps_set_color(job, &(job->obj->fillcolor));
UNSUPPORTED("c9ias5z64yrffrtfmr2jmrp6o"); // 	gvprintpointflist(job, AA, 2);
UNSUPPORTED("7gxao5sidmj76sv5nozugni5x"); // 	gvputs(job, " ellipse_path fill\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5745blic5ogwm4kppw7oweo2d"); //     if (job->obj->pencolor.u.HSVA[3] > .5) {
UNSUPPORTED("21kugn3mt0fvrcqbct2bxlb7t"); //         ps_set_pen_style(job);
UNSUPPORTED("axvgzd7tszpjn2lhenmufiumy"); //         ps_set_color(job, &(job->obj->pencolor));
UNSUPPORTED("c9ias5z64yrffrtfmr2jmrp6o"); // 	gvprintpointflist(job, AA, 2);
UNSUPPORTED("5zt3wwgax2r40r61om4iuqlfx"); // 	gvputs(job, " ellipse_path stroke\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dbmkwnmkz2w3ar1495wtjqo79
// static void psgen_bezier(GVJ_t * job, pointf * A, int n, int arrow_at_start, 	     int arrow_at_end, int filled) 
public static Object psgen_bezier(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9fpyhlu20gavb794dxeeud675"); // psgen_bezier(GVJ_t * job, pointf * A, int n, int arrow_at_start,
UNSUPPORTED("e5hr0515188mok0vrr6ri5swt"); // 	     int arrow_at_end, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bs0wcp6367dz1o5x166ec7l8"); //     int j;
UNSUPPORTED("2ream5c4u1f89xiwzlijg1ydk"); //     if (filled && job->obj->fillcolor.u.HSVA[3] > .5) {
UNSUPPORTED("ba0yi3he6a0tf9iisvs2r7lud"); // 	ps_set_color(job, &(job->obj->fillcolor));
UNSUPPORTED("eb3xzimrw2og0neoowz566azn"); // 	gvputs(job, "newpath ");
UNSUPPORTED("bjs7upob2hu49ojei4bhcfxz7"); // 	gvprintpointf(job, A[0]);
UNSUPPORTED("d8x140qupg29prem26ttu4koj"); // 	gvputs(job, " moveto\n");
UNSUPPORTED("8wzekl6ndy167eorf6dv1b631"); // 	for (j = 1; j < n; j += 3) {
UNSUPPORTED("3xyqg3mc833hr109cg8gvmv2t"); // 	    gvprintpointflist(job, &A[j], 3);
UNSUPPORTED("1m65lci1avkdlwjkxllj4m6gw"); // 	    gvputs(job, " curveto\n");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("i9jma968ecg2ludmdn9y0lee"); // 	gvputs(job, "closepath fill\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5745blic5ogwm4kppw7oweo2d"); //     if (job->obj->pencolor.u.HSVA[3] > .5) {
UNSUPPORTED("21kugn3mt0fvrcqbct2bxlb7t"); //         ps_set_pen_style(job);
UNSUPPORTED("axvgzd7tszpjn2lhenmufiumy"); //         ps_set_color(job, &(job->obj->pencolor));
UNSUPPORTED("eb3xzimrw2og0neoowz566azn"); // 	gvputs(job, "newpath ");
UNSUPPORTED("bjs7upob2hu49ojei4bhcfxz7"); // 	gvprintpointf(job, A[0]);
UNSUPPORTED("d8x140qupg29prem26ttu4koj"); // 	gvputs(job, " moveto\n");
UNSUPPORTED("8wzekl6ndy167eorf6dv1b631"); // 	for (j = 1; j < n; j += 3) {
UNSUPPORTED("3xyqg3mc833hr109cg8gvmv2t"); // 	    gvprintpointflist(job, &A[j], 3);
UNSUPPORTED("1m65lci1avkdlwjkxllj4m6gw"); // 	    gvputs(job, " curveto\n");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("87zw9vd7draxbwfkbyy134lil"); //         gvputs(job, "stroke\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3uv0mikukldcg9dyvrcagotsn
// static void psgen_polygon(GVJ_t * job, pointf * A, int n, int filled) 
public static Object psgen_polygon(Object... arg) {
UNSUPPORTED("4qcfsf0li4k9lm1473wfiyl8a"); // static void psgen_polygon(GVJ_t * job, pointf * A, int n, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bs0wcp6367dz1o5x166ec7l8"); //     int j;
UNSUPPORTED("2ream5c4u1f89xiwzlijg1ydk"); //     if (filled && job->obj->fillcolor.u.HSVA[3] > .5) {
UNSUPPORTED("ba0yi3he6a0tf9iisvs2r7lud"); // 	ps_set_color(job, &(job->obj->fillcolor));
UNSUPPORTED("eb3xzimrw2og0neoowz566azn"); // 	gvputs(job, "newpath ");
UNSUPPORTED("bjs7upob2hu49ojei4bhcfxz7"); // 	gvprintpointf(job, A[0]);
UNSUPPORTED("d8x140qupg29prem26ttu4koj"); // 	gvputs(job, " moveto\n");
UNSUPPORTED("d3tqpplutqsilsr0svy23b35k"); // 	for (j = 1; j < n; j++) {
UNSUPPORTED("de5j1zrilg6nolqeb004l2e18"); // 	    gvprintpointf(job, A[j]);
UNSUPPORTED("2u9oh4cykwgv0xspreoaonj0k"); // 	    gvputs(job, " lineto\n");
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("i9jma968ecg2ludmdn9y0lee"); // 	gvputs(job, "closepath fill\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5745blic5ogwm4kppw7oweo2d"); //     if (job->obj->pencolor.u.HSVA[3] > .5) {
UNSUPPORTED("21kugn3mt0fvrcqbct2bxlb7t"); //         ps_set_pen_style(job);
UNSUPPORTED("axvgzd7tszpjn2lhenmufiumy"); //         ps_set_color(job, &(job->obj->pencolor));
UNSUPPORTED("eb3xzimrw2og0neoowz566azn"); // 	gvputs(job, "newpath ");
UNSUPPORTED("bjs7upob2hu49ojei4bhcfxz7"); // 	gvprintpointf(job, A[0]);
UNSUPPORTED("d8x140qupg29prem26ttu4koj"); // 	gvputs(job, " moveto\n");
UNSUPPORTED("8d369cvojmdwbhvjfm3dy9wci"); //         for (j = 1; j < n; j++) {
UNSUPPORTED("de5j1zrilg6nolqeb004l2e18"); // 	    gvprintpointf(job, A[j]);
UNSUPPORTED("2u9oh4cykwgv0xspreoaonj0k"); // 	    gvputs(job, " lineto\n");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("94j5qi9ukl2l2ogh7z7dosq32"); //         gvputs(job, "closepath stroke\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 emshu7ex2sb49zftzpu7v7nl8
// static void psgen_polyline(GVJ_t * job, pointf * A, int n) 
public static Object psgen_polyline(Object... arg) {
UNSUPPORTED("dzdwhrdgsj0bxjp7f2uws63rf"); // static void psgen_polyline(GVJ_t * job, pointf * A, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bs0wcp6367dz1o5x166ec7l8"); //     int j;
UNSUPPORTED("5745blic5ogwm4kppw7oweo2d"); //     if (job->obj->pencolor.u.HSVA[3] > .5) {
UNSUPPORTED("21kugn3mt0fvrcqbct2bxlb7t"); //         ps_set_pen_style(job);
UNSUPPORTED("axvgzd7tszpjn2lhenmufiumy"); //         ps_set_color(job, &(job->obj->pencolor));
UNSUPPORTED("eb3xzimrw2og0neoowz566azn"); // 	gvputs(job, "newpath ");
UNSUPPORTED("bjs7upob2hu49ojei4bhcfxz7"); // 	gvprintpointf(job, A[0]);
UNSUPPORTED("d8x140qupg29prem26ttu4koj"); // 	gvputs(job, " moveto\n");
UNSUPPORTED("8d369cvojmdwbhvjfm3dy9wci"); //         for (j = 1; j < n; j++) {
UNSUPPORTED("de5j1zrilg6nolqeb004l2e18"); // 	    gvprintpointf(job, A[j]);
UNSUPPORTED("2u9oh4cykwgv0xspreoaonj0k"); // 	    gvputs(job, " lineto\n");
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("87zw9vd7draxbwfkbyy134lil"); //         gvputs(job, "stroke\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2gsov0kt2cm8uz4mmsiy83z46
// static void psgen_comment(GVJ_t * job, char *str) 
public static Object psgen_comment(Object... arg) {
UNSUPPORTED("45z5nair27wr5kf22brmgmms1"); // static void psgen_comment(GVJ_t * job, char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("64ek15nfld1t8q7vi9x33fpvy"); //     gvputs(job, "% ");
UNSUPPORTED("de7p50zdgdv1ycamcx5ibians"); //     gvputs(job, str);
UNSUPPORTED("b7eafy5bhxn361xe2c8x8553b"); //     gvputs(job, "\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 f2bghtv809eimejb28i73kzia
// static void psgen_library_shape(GVJ_t * job, char *name, pointf * A, int n, int filled) 
public static Object psgen_library_shape(Object... arg) {
UNSUPPORTED("dn0m6cdwn28bcu1u2vmoy1a2c"); // static void psgen_library_shape(GVJ_t * job, char *name, pointf * A, int n, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2ream5c4u1f89xiwzlijg1ydk"); //     if (filled && job->obj->fillcolor.u.HSVA[3] > .5) {
UNSUPPORTED("ba0yi3he6a0tf9iisvs2r7lud"); // 	ps_set_color(job, &(job->obj->fillcolor));
UNSUPPORTED("2p1j7dn4pu0imzbf77mre9k3s"); // 	gvputs(job, "[ ");
UNSUPPORTED("af44mg78omj8g3sx02euas8s"); //         gvprintpointflist(job, A, n);
UNSUPPORTED("8dk2likbqm43q0p28bspn3ux4"); //         gvputs(job, " ");
UNSUPPORTED("didklqh38lq2cm8zufhnkjy94"); //         gvprintpointf(job, A[0]);
UNSUPPORTED("8c4717nsqb9qh1m5pkqxu3nab"); // 	gvprintf(job, " ]  %d true %s\n", n, name);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5745blic5ogwm4kppw7oweo2d"); //     if (job->obj->pencolor.u.HSVA[3] > .5) {
UNSUPPORTED("21kugn3mt0fvrcqbct2bxlb7t"); //         ps_set_pen_style(job);
UNSUPPORTED("axvgzd7tszpjn2lhenmufiumy"); //         ps_set_color(job, &(job->obj->pencolor));
UNSUPPORTED("a9rnggimah1qzsbj25u9gxjvi"); //         gvputs(job, "[ ");
UNSUPPORTED("af44mg78omj8g3sx02euas8s"); //         gvprintpointflist(job, A, n);
UNSUPPORTED("8dk2likbqm43q0p28bspn3ux4"); //         gvputs(job, " ");
UNSUPPORTED("didklqh38lq2cm8zufhnkjy94"); //         gvprintpointf(job, A[0]);
UNSUPPORTED("ar45etwgai0wcnglwou25mka6"); //         gvprintf(job, " ]  %d false %s\n", n, name);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 4f0tedaxz1mg2o27rukecbmjl
// static gvrender_engine_t psgen_engine = 


//1 6mtxeu6btxy3ypxd3uwk9m4s5
// static gvrender_features_t render_features_ps = 


//1 4tcujj00n5zmmxavjrh1y4cv
// static gvdevice_features_t device_features_ps = 


//1 2fdbmmc7eh40yzthi2e79fs6v
// static gvdevice_features_t device_features_eps = 


//1 8ccgm6294yfcwokwubby9ui9t
// gvplugin_installed_t gvrender_ps_types[] = 


//1 40zb59jhewjfetnd2oqgm1wh7
// gvplugin_installed_t gvdevice_ps_types[] = 


}
