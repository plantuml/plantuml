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
import static smetana.core.JUtils.function;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_GVC_s;
import h.lt_symlist_t;
import smetana.core.__ptr__;

public class gvcontext__c {
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


//1 a3rqagofsgraie6mx0krzkgsy
// extern int graphviz_errors


//1 9xccaaawyqf5atj8e59ems07n
// static char *LibInfo[] = 
//public static java.util.List<CString> LibInfo = java.util.Arrays.asList(new CString("graphviz"),new CString("PACKAGE_VERSION"),new CString("BUILDDATE"));
public static __ptr__ LibInfo = null;



//3 8jwauh4lo3kcvxhomy40s94b
// GVC_t *gvNEWcontext(const lt_symlist_t *builtins, int demand_loading) 
public static ST_GVC_s gvNEWcontext(lt_symlist_t builtins, boolean demand_loading) {
ENTERING("8jwauh4lo3kcvxhomy40s94b","gvNEWcontext");
try {
	ST_GVC_s gvc = new ST_GVC_s();
    if (gvc!=null) {
	gvc.common.setPtr("info", LibInfo);
	gvc.common.setPtr("errorfn", function(gen.lib.cgraph.agerror__c.class, "agerrorf"));
	gvc.common.setPtr("builtins", builtins);
	gvc.common.demand_loading = demand_loading;
    }
    return gvc;
} finally {
LEAVING("8jwauh4lo3kcvxhomy40s94b","gvNEWcontext");
}
}




//3 7i8lj0jlmprrj5kmbegtkh46n
// void gvFinalize(GVC_t * gvc) 
public static Object gvFinalize(Object... arg) {
UNSUPPORTED("1dio7yofln4lzxco3d0eucybt"); // void gvFinalize(GVC_t * gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("72t0pmaautwuw3vb1xwa2grxz"); //     if (gvc->active_jobs)
UNSUPPORTED("dtzx65g0vmdvgplajdnqct44o"); // 	gvrender_end_job(gvc->active_jobs);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ctg42qeu1yvs2g9r5wcv4zzrl
// int gvFreeContext(GVC_t * gvc) 
public static Object gvFreeContext(Object... arg) {
UNSUPPORTED("cdc5p7gur1o2qu1rfaiwrhdyj"); // int gvFreeContext(GVC_t * gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5hwchhks73n6p0p7iu2dhstho"); //     GVG_t *gvg, *gvg_next;
UNSUPPORTED("adwgvl59jz4qsr1d7xxq5rsng"); //     gvplugin_package_t *package, *package_next;
UNSUPPORTED("arvwlcmlxzvpwvk7s223e2w8"); //     emit_once_reset();
UNSUPPORTED("eutrfc45t7pr7snzd0yrns61e"); //     gvg_next = gvc->gvgs;
UNSUPPORTED("egtqgis45r50r0vpmklawt86c"); //     while ((gvg = gvg_next)) {
UNSUPPORTED("een7xpunmqq9r51mgh694gxvi"); // 	gvg_next = gvg->next;
UNSUPPORTED("4iv5fzitlc63rdpf954w6dbax"); // 	free(gvg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("aemtpukkty8b8p02pq9n1pe12"); //     package_next = gvc->packages;
UNSUPPORTED("6bhwk4v49u06ll2918aiuelsp"); //     while ((package = package_next)) {
UNSUPPORTED("17d6nsylkka56jdgieafhw3cl"); // 	package_next = package->next;
UNSUPPORTED("37qv8a4u1yiip7ecavr8qkzkm"); // 	free(package->path);
UNSUPPORTED("7pxw8ghhb5xxlpdd1jv7osfhz"); // 	free(package->name);
UNSUPPORTED("aimh83a3k7a6ayfix3ppi8nqx"); // 	free(package);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a9p7yonln7g91ge7xab3xf9dr"); //     gvjobs_delete(gvc);
UNSUPPORTED("7rlh9wuy2qrh0ehtb3gdalado"); //     if (gvc->config_path)
UNSUPPORTED("757mqdb4lingk1bymuktrnevh"); // 	free(gvc->config_path);
UNSUPPORTED("85h1etabgqi4o3qi83c7cprtf"); //     if (gvc->input_filenames)
UNSUPPORTED("58u4di4bdc3xnuj9x373q03z"); // 	free(gvc->input_filenames);
UNSUPPORTED("2wnuuoxkpo409qiqqaihby1z9"); //     textfont_dict_close(gvc);
UNSUPPORTED("beuj2gz4oz0esqo5psm1l653c"); //     free(gvc);
UNSUPPORTED("41ok9shf9o21b8omrjo4s42gl"); //     return (graphviz_errors + agerrors());
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
