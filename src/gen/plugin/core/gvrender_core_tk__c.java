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

public class gvrender_core_tk__c {
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




//3 vnba6ctep0vnompe5n04xa3l
// static char *tkgen_string(char *s) 
public static Object tkgen_string(Object... arg) {
UNSUPPORTED("aa32h5bkponzvoqtod1mva7ps"); // static char *tkgen_string(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dplaa6pgfnvndktf7zizb0dd9
// static void tkgen_print_color(GVJ_t * job, gvcolor_t color) 
public static Object tkgen_print_color(Object... arg) {
UNSUPPORTED("2038ixzt93lz5npkbq5uh0qwr"); // static void tkgen_print_color(GVJ_t * job, gvcolor_t color)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hn9tht7vq6fnuzbw3mgpwl6i"); //     switch (color.type) {
UNSUPPORTED("8d4tzwdd3f608e3jl4lzyugu0"); //     case COLOR_STRING:
UNSUPPORTED("13cycml8lfb1s4abkvnuhd8dh"); // 	gvputs(job, color.u.string);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1ke9k1couoeb4od2nwibgg0xu"); //     case RGBA_BYTE:
UNSUPPORTED("bk6hmzzce8zu1dqkcetedyx8a"); // 	if (color.u.rgba[3] == 0) /* transparent */
UNSUPPORTED("cqkxybfy03p6sdzaw403x4anw"); // 	    gvputs(job, "\"\"");
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("7jq54scftd6wr5k5aefwuq5r4"); // 	    gvprintf(job, "#%02x%02x%02x",
UNSUPPORTED("e0jr1wda5b2t6n9g1aas984rf"); // 		color.u.rgba[0], color.u.rgba[1], color.u.rgba[2]);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("3yz4iw4v7mm470if4vb6yaack"); // 	assert(0);		/* internal error */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7fftf2s66s7nkjfvfwqxq2wri
// static void tkgen_print_tags(GVJ_t *job) 
public static Object tkgen_print_tags(Object... arg) {
UNSUPPORTED("5rwzkk8kjevk7s1e5igx8wikf"); // static void tkgen_print_tags(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6fmxub3a5s4yqhi6k71lbbewx"); //     char *ObjType;
UNSUPPORTED("36c1s92uv6tfwjm89w624ibxa"); //     unsigned int ObjId;
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("e6yyipvortaqhjfzpnfvdaea6"); //     int ObjFlag;
UNSUPPORTED("9kqeoqv8k67h4o1r0zmzcpja4"); //     switch (obj->emit_state) {
UNSUPPORTED("edn2txxwkd2kmwwo2dgazo6hg"); //     case EMIT_NDRAW:
UNSUPPORTED("9wmdny2syd27ay1uhzyj36k2k"); // 	ObjType = "node";
UNSUPPORTED("3x8av8kig5q0u8qayofbq7ao5"); // 	ObjFlag = 1;
UNSUPPORTED("4lrn04rxio5vdym9mvu96uakv"); //         ObjId = AGSEQ(obj->u.n);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3dkus0mrhy1dxovvhot9s7zc4"); //     case EMIT_NLABEL:
UNSUPPORTED("9wmdny2syd27ay1uhzyj36k2k"); // 	ObjType = "node";
UNSUPPORTED("arrey7e5x7t7ug3fcbir0h99f"); // 	ObjFlag = 0;
UNSUPPORTED("4lrn04rxio5vdym9mvu96uakv"); //         ObjId = AGSEQ(obj->u.n);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("ddijyz6xr6xxiji0xwi84qsq1"); //     case EMIT_EDRAW:
UNSUPPORTED("2zes9o0ykhutqmeeyeizb6sw6"); //     case EMIT_TDRAW:
UNSUPPORTED("f4ax0wnfz42m0mvpo8ov4uwgj"); //     case EMIT_HDRAW:
UNSUPPORTED("ekq5izr299kjgtxohtoayjuay"); // 	ObjType = "edge";
UNSUPPORTED("3x8av8kig5q0u8qayofbq7ao5"); // 	ObjFlag = 1;
UNSUPPORTED("2n6su1jan1lfwl3bv3vpvtf2m"); //         ObjId = AGSEQ(obj->u.e);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("82u5g3c6sjjwnob1gwc3bumud"); //     case EMIT_ELABEL:
UNSUPPORTED("5a42wk0iyr9uwqezh67ujkaci"); //     case EMIT_TLABEL:
UNSUPPORTED("bgniadjg8rn6697mbbbrtdj46"); //     case EMIT_HLABEL:
UNSUPPORTED("ekq5izr299kjgtxohtoayjuay"); // 	ObjType = "edge";
UNSUPPORTED("arrey7e5x7t7ug3fcbir0h99f"); // 	ObjFlag = 0;
UNSUPPORTED("2n6su1jan1lfwl3bv3vpvtf2m"); //         ObjId = AGSEQ(obj->u.e);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("eoday7rklu6k6yz10yospl4fm"); //     case EMIT_GDRAW:
UNSUPPORTED("8kzghiwhkdmm0vv0aph5lnl24"); // 	ObjType = "graph";
UNSUPPORTED("3x8av8kig5q0u8qayofbq7ao5"); // 	ObjFlag = 1;
UNSUPPORTED("caivixbej5pdp5nezbwnt2ncp"); // 	ObjId = -1;  /* hack! */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("bs2bx2mdpt81ogx6xg3uu2tmf"); //     case EMIT_GLABEL:
UNSUPPORTED("arrey7e5x7t7ug3fcbir0h99f"); // 	ObjFlag = 0;
UNSUPPORTED("3gtg1b09awmbttihqzq7we0yl"); // 	ObjType = "graph label";
UNSUPPORTED("caivixbej5pdp5nezbwnt2ncp"); // 	ObjId = -1;  /* hack! */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("5mi2jlcl1mkxh27aq4swcrvsd"); //     case EMIT_CDRAW:
UNSUPPORTED("8kzghiwhkdmm0vv0aph5lnl24"); // 	ObjType = "graph";
UNSUPPORTED("3x8av8kig5q0u8qayofbq7ao5"); // 	ObjFlag = 1;
UNSUPPORTED("3019wv5sfqax6te4ocvlvdik2"); // 	ObjId = AGSEQ(obj->u.sg);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("2g9uft4wkkmqft2kfgbtkig71"); //     case EMIT_CLABEL:
UNSUPPORTED("8kzghiwhkdmm0vv0aph5lnl24"); // 	ObjType = "graph";
UNSUPPORTED("arrey7e5x7t7ug3fcbir0h99f"); // 	ObjFlag = 0;
UNSUPPORTED("3019wv5sfqax6te4ocvlvdik2"); // 	ObjId = AGSEQ(obj->u.sg);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("b844if1dn4kjcevyd28kssy2e"); // 	assert (0);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("55znmxgknag5qirkz1z6lpksh"); //     gvprintf(job, " -tags {%d%s%d}", ObjFlag, ObjType, ObjId);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6oyiwbg0tt965z8czwrybx7sk
// static void tkgen_canvas(GVJ_t * job) 
public static Object tkgen_canvas(Object... arg) {
UNSUPPORTED("3jvfz8f5qzwiglrakhy83umfp"); // static void tkgen_canvas(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9h891dv6xzcbe003pt2da6zaa"); //    if (job->external_context) 
UNSUPPORTED("5qodxedfoew7wk6n6u7a55eoy"); // 	gvputs(job, job->imagedata);
UNSUPPORTED("axam71d21fg4esy4zqklfrwm4"); //    else
UNSUPPORTED("2vrpkklffbp4w437thim0u8b7"); // 	gvputs(job, "$c");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 234xlyukcgtfrl83jcxd9lago
// static void tkgen_comment(GVJ_t * job, char *str) 
public static Object tkgen_comment(Object... arg) {
UNSUPPORTED("2280389c7mo073ymgoiqe9zc7"); // static void tkgen_comment(GVJ_t * job, char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ea77c3tz79kbzzy6ixp1d2kh4"); //     gvputs(job, "# ");
UNSUPPORTED("5q62ijbcsw35n264ivjfulqvt"); //     gvputs(job, tkgen_string(str));
UNSUPPORTED("b7eafy5bhxn361xe2c8x8553b"); //     gvputs(job, "\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9fhmin70b5qf04bp9t3gp6ql5
// static void tkgen_begin_job(GVJ_t * job) 
public static Object tkgen_begin_job(Object... arg) {
UNSUPPORTED("7099eur0jsw3fx3t04i009op4"); // static void tkgen_begin_job(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8phrhbpt3gwyqfay75nnt01sn"); //     gvputs(job, "# Generated by ");
UNSUPPORTED("4fmowhw6woxnz0yfmf7ihnv5w"); //     gvputs(job, tkgen_string(job->common->info[0]));
UNSUPPORTED("cx22tlhx29vhu1t7qu0qa4qq7"); //     gvputs(job, " version ");
UNSUPPORTED("71t1hukev4nj9d5vgu67aecnu"); //     gvputs(job, tkgen_string(job->common->info[1]));
UNSUPPORTED("85rsmmnqjhjgun71czh20mlqf"); //     gvputs(job, " (");
UNSUPPORTED("eq1ksgr2y9k7jkdl3l5fx2dxr"); //     gvputs(job, tkgen_string(job->common->info[2]));
UNSUPPORTED("2e9opn4z98non8389n3x7cvfj"); //     gvputs(job, ")\n");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bidf6nln7jad77uyvyjiwsahj
// static void tkgen_begin_graph(GVJ_t * job) 
public static Object tkgen_begin_graph(Object... arg) {
UNSUPPORTED("c66a3xz9acxwt162a2639p0kf"); // static void tkgen_begin_graph(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("bf5z85r4dnnsytsyyw7t3gx6o"); //     gvputs(job, "#");
UNSUPPORTED("a140rb3spyp62aewcsebcj6lz"); //     if (agnameof(obj->u.g)[0]) {
UNSUPPORTED("dzpnxf48rus9qbb0ks0h8dqd1"); //         gvputs(job, " Title: ");
UNSUPPORTED("aqk6vyc91z34ikxpps386o2gb"); // 	gvputs(job, tkgen_string(agnameof(obj->u.g)));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9zpqzsstpmkb5v40kv0rt86rx"); //     gvprintf(job, " Pages: %d\n", job->pagesArraySize.x * job->pagesArraySize.y);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 578fx153hsm0thk9yst8xsav
// static int first_periphery




//3 3y0ogli6wn5tiuel1z0cdvpmp
// static void tkgen_begin_node(GVJ_t * job) 
public static Object tkgen_begin_node(Object... arg) {
UNSUPPORTED("65n9qvpouyi5n69pxqhy3ly5j"); // static void tkgen_begin_node(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ekl6yorur1ahi50axky7lyawh"); // 	first_periphery = 1;     /* FIXME - this is an ugly hack! */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 48o8qk8mdvwx737vbohxu80s6
// static void tkgen_begin_edge(GVJ_t * job) 
public static Object tkgen_begin_edge(Object... arg) {
UNSUPPORTED("4jelx905puajzw3pw79wr399g"); // static void tkgen_begin_edge(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8rousoe1k1rgca46ysx8k11n9"); // 	first_periphery = -1;     /* FIXME - this is an ugly ugly hack!  Need this one for arrowheads. */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 elok7zvfwcod280ol1ewx7xi6
// static void tkgen_textspan(GVJ_t * job, pointf p, textspan_t * span) 
public static Object tkgen_textspan(Object... arg) {
UNSUPPORTED("ctbqa72rfic93u34n0vcy070n"); // static void tkgen_textspan(GVJ_t * job, pointf p, textspan_t * span)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("2eltcrenufen27upw0otuj5rg"); //     const char *font;
UNSUPPORTED("7pxhx3kruwsackat2315zxaxh"); //     PostscriptAlias *pA;
UNSUPPORTED("cav0e7ncloqss0hsvc4fyfi7s"); //     int size;
UNSUPPORTED("t8jc65c5jcpqdkfpg4fjb8u4"); //     if (obj->pen != PEN_NONE) {
UNSUPPORTED("jmu515qulcvrabiwjcvrcc3z"); // 	/* determine font size */
UNSUPPORTED("27qmq4a14tm7p6a47yf7m6bwl"); // 	/* round fontsize down, better too small than too big */
UNSUPPORTED("d6wfbdo0cro4b8en0ny4b4zn0"); // 	size = (int)(span->font->size * job->zoom);
UNSUPPORTED("2jso8vydmg40ljyk6riyesqpe"); // 	/* don't even bother if fontsize < 1 point */
UNSUPPORTED("4ek995evzufhzop2kinw7g53o"); // 	if (size)  {
UNSUPPORTED("9g1m3v4tgit2cudtd1mdwt6ui"); //             tkgen_canvas(job);
UNSUPPORTED("efw4q0zbq8bz1zbek9qh8r362"); //             gvputs(job, " create text ");
UNSUPPORTED("6z6wkidq5hq6evjicc4xdclir"); //             p.y -= size * 0.55; /* cl correction */
UNSUPPORTED("2v1yrsn7wzpnae01zojw8d7no"); //             gvprintpointf(job, p);
UNSUPPORTED("786rbjesnuuzbso73vd0nop0m"); //             gvputs(job, " -text {");
UNSUPPORTED("3rqoe86bgahp689yo06e13tg9"); //             gvputs(job, span->str);
UNSUPPORTED("c5daf8oxiupwtbg0xqvnmhcja"); //             gvputs(job, "}");
UNSUPPORTED("eyrt0ozexqiogwfvx6afjau6i"); //             gvputs(job, " -fill ");
UNSUPPORTED("2g5b6wr0r6zs9h8szn82kzlwh"); //             tkgen_print_color(job, obj->pencolor);
UNSUPPORTED("2f348xbokrc8ke8cqvx1n8r8t"); //             gvputs(job, " -font {");
UNSUPPORTED("9sp1k6nk7qgspuoh1fsykad1d"); // 	    /* tk doesn't like PostScript font names like "Times-Roman" */
UNSUPPORTED("3fgue2b4or4v2ea9156z83s40"); // 	    /*    so use family names */
UNSUPPORTED("8hnpk6w2tpf5af6930s0fwjzv"); // 	    pA = span->font->postscript_alias;
UNSUPPORTED("25qpoikp60eudd2s2rx82m4nz"); // 	    if (pA)
UNSUPPORTED("8jg0mf1p0gcs81yus1o9buib3"); // 	        font = pA->family;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("blasxdo35r0ck273xhl4rt9aq"); // 		font = span->font->name;
UNSUPPORTED("eec8oj7b6e89hy8dovtjq79l3"); //             gvputs(job, "\"");
UNSUPPORTED("bfjoee7kusanzqqsijx9fv8e3"); //             gvputs(job, font);
UNSUPPORTED("eec8oj7b6e89hy8dovtjq79l3"); //             gvputs(job, "\"");
UNSUPPORTED("3iim7kn92kmgyv6y3ksj64yts"); // 	    /* use -ve fontsize to indicate pixels  - see "man n font" */
UNSUPPORTED("1npabltzbojkssc7y6zaax68z"); //             gvprintf(job, " %d}", size);
UNSUPPORTED("aa77x3rwsgl9w2j4emr2slvvg"); //             switch (span->just) {
UNSUPPORTED("dva9utlw6mjaqopti10jmbumi"); //             case 'l':
UNSUPPORTED("3axfyb1v0b6zx9rpx0zk823t8"); //                 gvputs(job, " -anchor w");
UNSUPPORTED("d1pumbibe8xz2i7gr1wj6zdak"); //                 break;
UNSUPPORTED("8t43r13dsz4v58jm5myew417n"); //             case 'r':
UNSUPPORTED("21sml808kswrqs8l2ia7cc6lw"); //                 gvputs(job, " -anchor e");
UNSUPPORTED("d1pumbibe8xz2i7gr1wj6zdak"); //                 break;
UNSUPPORTED("84cl4r6uveabh1lahtyw0ptb8"); //             default:
UNSUPPORTED("9szdbf50c5ed9xajhvikui4o6"); //             case 'n':
UNSUPPORTED("d1pumbibe8xz2i7gr1wj6zdak"); //                 break;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("2il2s87ajd09islkh7e2kirpo"); //             tkgen_print_tags(job);
UNSUPPORTED("5uq09elejy2ot3s8x5d13dxs9"); //             gvputs(job, "\n");
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3g3c5t15h39grskbujipsb3n7
// static void tkgen_ellipse(GVJ_t * job, pointf * A, int filled) 
public static Object tkgen_ellipse(Object... arg) {
UNSUPPORTED("5092mqkkvnba8iben6dh14iz5"); // static void tkgen_ellipse(GVJ_t * job, pointf * A, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("cvexv13y9fq49v0j4d5t4cm9f"); //     pointf r;
UNSUPPORTED("t8jc65c5jcpqdkfpg4fjb8u4"); //     if (obj->pen != PEN_NONE) {
UNSUPPORTED("dm4570wcyjb3rox4itsi86l1q"); //     /* A[] contains 2 points: the center and top right corner. */
UNSUPPORTED("5s8l5jhamrhlmzhbxbollx0ce"); //         r.x = A[1].x - A[0].x;
UNSUPPORTED("5xe23krn77wrf3q2cnb8tid6u"); //         r.y = A[1].y - A[0].y;
UNSUPPORTED("42w8f4syzoodosc4lqh7zdhf6"); //         A[0].x -= r.x;
UNSUPPORTED("8v0iqqzghk3op67s3lqog6ehb"); //         A[0].y -= r.y;
UNSUPPORTED("cpyq52t64pblc9nfne0q9tzvv"); //         tkgen_canvas(job);
UNSUPPORTED("897mq76heyo6hgxh4e95wy9zi"); //         gvputs(job, " create oval ");
UNSUPPORTED("cxoxxyz7qmjvft5cr1rcjstyo"); //         gvprintpointflist(job, A, 2);
UNSUPPORTED("cfzbaouamgyduscf63lup4oww"); //         gvputs(job, " -fill ");
UNSUPPORTED("2r3o5gud8djix2uwr2zk2pvtr"); //         if (filled)
UNSUPPORTED("in3qxhb278ephlrwa1dj9da8"); //             tkgen_print_color(job, obj->fillcolor);
UNSUPPORTED("1zsbuj1o3uaqqaqgzt5q0kcs"); //         else if (first_periphery)
UNSUPPORTED("1q9z405druym0tb73iu7wgsbj"); // 	    /* tk ovals default to no fill, some fill
UNSUPPORTED("aeswylfea9yhqvz6vm9o2vu8l"); //              * is necessary else "canvas find overlapping" doesn't
UNSUPPORTED("mj4s48s0gyalklfrcrgpxdol"); //              * work as expected, use white instead */
UNSUPPORTED("vh2rv9mnbc28spfzs8cyaoar"); // 	    gvputs(job, "white");
UNSUPPORTED("df5svfeo1imlc8qm6azeqkkmh"); // 	else 
UNSUPPORTED("cqkxybfy03p6sdzaw403x4anw"); // 	    gvputs(job, "\"\"");
UNSUPPORTED("67pmynbmmb156i27d3hh6z2lm"); // 	if (first_periphery == 1)
UNSUPPORTED("beq2s7wwrxfrqoschrxojlq3"); // 	    first_periphery = 0;
UNSUPPORTED("5d33cl4ogs78yvw2i75icg9g6"); //         gvputs(job, " -width ");
UNSUPPORTED("9lzl69uuazscmin8fqgbic818"); //         gvprintdouble(job, obj->penwidth);
UNSUPPORTED("4cpwr3m94g1ys1g7qno3p7770"); //         gvputs(job, " -outline ");
UNSUPPORTED("b1kdoymo0peu76atwvwlvppxm"); // 	tkgen_print_color(job, obj->pencolor);
UNSUPPORTED("7ljsiyfswf1fyq7u5u5meig9b"); //         if (obj->pen == PEN_DASHED)
UNSUPPORTED("48ujmq20gr1bcmnb9x7qstuon"); // 	    gvputs(job, " -dash 5");
UNSUPPORTED("qz536d4xx8biptvgrkpjg3wo"); //         if (obj->pen == PEN_DOTTED)
UNSUPPORTED("nxz0zcvg2vp69h2k4vz0zxjo"); // 	    gvputs(job, " -dash 2");
UNSUPPORTED("7xbsyhpezhbss8vlckn2p77i7"); //         tkgen_print_tags(job);
UNSUPPORTED("427r2p9r3ymmxp5p69rt99ymp"); //         gvputs(job, "\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eeajnb6p79ncip1c7g31tb6ig
// static void tkgen_bezier(GVJ_t * job, pointf * A, int n, int arrow_at_start, 	      int arrow_at_end, int filled) 
public static Object tkgen_bezier(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3iwjodya57audhxb7vhl6mtnt"); // tkgen_bezier(GVJ_t * job, pointf * A, int n, int arrow_at_start,
UNSUPPORTED("77j21vz8ekimnj6b6uefavtz2"); // 	      int arrow_at_end, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("t8jc65c5jcpqdkfpg4fjb8u4"); //     if (obj->pen != PEN_NONE) {
UNSUPPORTED("cpyq52t64pblc9nfne0q9tzvv"); //         tkgen_canvas(job);
UNSUPPORTED("bb6nsdbnte9p73wmrdar55mb7"); //         gvputs(job, " create line ");
UNSUPPORTED("af44mg78omj8g3sx02euas8s"); //         gvprintpointflist(job, A, n);
UNSUPPORTED("cfzbaouamgyduscf63lup4oww"); //         gvputs(job, " -fill ");
UNSUPPORTED("8lreipxfzo9h2hdub9byfimmx"); //         tkgen_print_color(job, obj->pencolor);
UNSUPPORTED("5d33cl4ogs78yvw2i75icg9g6"); //         gvputs(job, " -width ");
UNSUPPORTED("9lzl69uuazscmin8fqgbic818"); //         gvprintdouble(job, obj->penwidth);
UNSUPPORTED("7ljsiyfswf1fyq7u5u5meig9b"); //         if (obj->pen == PEN_DASHED)
UNSUPPORTED("48ujmq20gr1bcmnb9x7qstuon"); // 	    gvputs(job, " -dash 5");
UNSUPPORTED("qz536d4xx8biptvgrkpjg3wo"); //         if (obj->pen == PEN_DOTTED)
UNSUPPORTED("nxz0zcvg2vp69h2k4vz0zxjo"); // 	    gvputs(job, " -dash 2");
UNSUPPORTED("3rjk6ae9vk516ajz9hz4fr70p"); //         gvputs(job, " -smooth bezier ");
UNSUPPORTED("7xbsyhpezhbss8vlckn2p77i7"); //         tkgen_print_tags(job);
UNSUPPORTED("427r2p9r3ymmxp5p69rt99ymp"); //         gvputs(job, "\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d9qc2t2e84r1dyjrjh9lvaaqc
// static void tkgen_polygon(GVJ_t * job, pointf * A, int n, int filled) 
public static Object tkgen_polygon(Object... arg) {
UNSUPPORTED("17wb4p46hmuuq5j0pxtx7ui5x"); // static void tkgen_polygon(GVJ_t * job, pointf * A, int n, int filled)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("t8jc65c5jcpqdkfpg4fjb8u4"); //     if (obj->pen != PEN_NONE) {
UNSUPPORTED("cpyq52t64pblc9nfne0q9tzvv"); //         tkgen_canvas(job);
UNSUPPORTED("1sccvd9iuqc3n3fkzk810g0y"); //         gvputs(job, " create polygon ");
UNSUPPORTED("af44mg78omj8g3sx02euas8s"); //         gvprintpointflist(job, A, n);
UNSUPPORTED("cfzbaouamgyduscf63lup4oww"); //         gvputs(job, " -fill ");
UNSUPPORTED("2r3o5gud8djix2uwr2zk2pvtr"); //         if (filled)
UNSUPPORTED("in3qxhb278ephlrwa1dj9da8"); //             tkgen_print_color(job, obj->fillcolor);
UNSUPPORTED("1zsbuj1o3uaqqaqgzt5q0kcs"); //         else if (first_periphery)
UNSUPPORTED("b9wx5iz0yfgk23y63056w64g8"); //             /* tk polygons default to black fill, some fill
UNSUPPORTED("85jmgncgec9epxj3v03qzl4x6"); // 	     * is necessary else "canvas find overlapping" doesn't
UNSUPPORTED("aorsjpcyvmasihruyarjm4f8p"); // 	     * work as expected, use white instead */
UNSUPPORTED("3jjudqx9sb6o5u51zdnpfjf9q"); //             gvputs(job, "white");
UNSUPPORTED("35nw1pbiz2p3s6qwlam5eoo3m"); //         else
UNSUPPORTED("cys9b9aepuo25ntuuyhiqb6ln"); //             gvputs(job, "\"\"");
UNSUPPORTED("94eif945asnu8gb1psb6wspvz"); // 	if (first_periphery == 1) 
UNSUPPORTED("beq2s7wwrxfrqoschrxojlq3"); // 	    first_periphery = 0;
UNSUPPORTED("5d33cl4ogs78yvw2i75icg9g6"); //         gvputs(job, " -width ");
UNSUPPORTED("9lzl69uuazscmin8fqgbic818"); //         gvprintdouble(job, obj->penwidth);
UNSUPPORTED("4cpwr3m94g1ys1g7qno3p7770"); //         gvputs(job, " -outline ");
UNSUPPORTED("b1kdoymo0peu76atwvwlvppxm"); // 	tkgen_print_color(job, obj->pencolor);
UNSUPPORTED("7ljsiyfswf1fyq7u5u5meig9b"); //         if (obj->pen == PEN_DASHED)
UNSUPPORTED("48ujmq20gr1bcmnb9x7qstuon"); // 	    gvputs(job, " -dash 5");
UNSUPPORTED("qz536d4xx8biptvgrkpjg3wo"); //         if (obj->pen == PEN_DOTTED)
UNSUPPORTED("nxz0zcvg2vp69h2k4vz0zxjo"); // 	    gvputs(job, " -dash 2");
UNSUPPORTED("7xbsyhpezhbss8vlckn2p77i7"); //         tkgen_print_tags(job);
UNSUPPORTED("427r2p9r3ymmxp5p69rt99ymp"); //         gvputs(job, "\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6c5hyqf18vqv7zjvhkfe6ti35
// static void tkgen_polyline(GVJ_t * job, pointf * A, int n) 
public static Object tkgen_polyline(Object... arg) {
UNSUPPORTED("at98nvxsdu2h0dornar4y15ul"); // static void tkgen_polyline(GVJ_t * job, pointf * A, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("t8jc65c5jcpqdkfpg4fjb8u4"); //     if (obj->pen != PEN_NONE) {
UNSUPPORTED("cpyq52t64pblc9nfne0q9tzvv"); //         tkgen_canvas(job);
UNSUPPORTED("bb6nsdbnte9p73wmrdar55mb7"); //         gvputs(job, " create line ");
UNSUPPORTED("af44mg78omj8g3sx02euas8s"); //         gvprintpointflist(job, A, n);
UNSUPPORTED("cfzbaouamgyduscf63lup4oww"); //         gvputs(job, " -fill ");
UNSUPPORTED("8lreipxfzo9h2hdub9byfimmx"); //         tkgen_print_color(job, obj->pencolor);
UNSUPPORTED("7ljsiyfswf1fyq7u5u5meig9b"); //         if (obj->pen == PEN_DASHED)
UNSUPPORTED("48ujmq20gr1bcmnb9x7qstuon"); // 	    gvputs(job, " -dash 5");
UNSUPPORTED("qz536d4xx8biptvgrkpjg3wo"); //         if (obj->pen == PEN_DOTTED)
UNSUPPORTED("nxz0zcvg2vp69h2k4vz0zxjo"); // 	    gvputs(job, " -dash 2");
UNSUPPORTED("7xbsyhpezhbss8vlckn2p77i7"); //         tkgen_print_tags(job);
UNSUPPORTED("427r2p9r3ymmxp5p69rt99ymp"); //         gvputs(job, "\n");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 2zi5rrmhj45i69kzgpbc74bsu
// gvrender_engine_t tkgen_engine = 


//1 89ucnxk8hokecx6wfztqf0uyc
// gvrender_features_t render_features_tk = 


//1 bl8y4t7jlhz6ojwhbki4xl65i
// gvdevice_features_t device_features_tk = 


//1 9u0gr6vcp1rk5yi3h4xw8lvbp
// gvplugin_installed_t gvrender_tk_types[] = 


//1 2ge2ai9gnykik4apwbrsp6qua
// gvplugin_installed_t gvdevice_tk_types[] = 


}
