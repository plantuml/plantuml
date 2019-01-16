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
import static gen.lib.cgraph.attr__c.agattr;
import static gen.lib.common.textspan__c.textfont_dict_open;
import static gen.lib.gvc.gvcontext__c.gvNEWcontext;
import static gen.lib.gvc.gvtextlayout__c.gvtextlayout_select;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_GVC_s;
import smetana.core.CString;

public class gvc__c {
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




//3 f3vdhir2c7dz3pvmx9d3m4lx1
// GVC_t *gvContext(void) 
public static ST_GVC_s gvContext(Object... arg) {
ENTERING("f3vdhir2c7dz3pvmx9d3m4lx1","gvContext");
try {
	ST_GVC_s gvc;
    agattr(null, AGNODE, new CString("label"), new CString("\\N"));
    /* default to no builtins, demand loading enabled */
    gvc = (ST_GVC_s) gvNEWcontext(null, (NOT(0)));
    /* builtins don't require LTDL */
    gvc.setInt("config_found", 0);
    gvtextlayout_select(gvc);   /* choose best available textlayout plugin immediately */
    textfont_dict_open(gvc);    /* initialize font dict */
    return gvc;
} finally {
LEAVING("f3vdhir2c7dz3pvmx9d3m4lx1","gvContext");
}
}




//3 71uhxq27gz2h6tw2jii3pv0m1
// GVC_t *gvContextPlugins(const lt_symlist_t *builtins, int demand_loading) 
public static Object gvContextPlugins(Object... arg) {
UNSUPPORTED("ed1o9q8yu974j7v5vbap4dlsk"); // GVC_t *gvContextPlugins(const lt_symlist_t *builtins, int demand_loading)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8f3ds00y6y855lum5r1q8iguu"); //     GVC_t *gvc;
UNSUPPORTED("8808fnx5fweuiso27n5bmlach"); //     agattr(NULL, AGNODE, "label", "\\N");
UNSUPPORTED("1n7k6h7whkgv2pc7hm2vxrtlz"); //     gvc = gvNEWcontext(builtins, demand_loading);
UNSUPPORTED("36nskub1lmc5t5yi3kt1v69ot"); //     gvconfig(gvc, 0); /* configure for available plugins */
UNSUPPORTED("8vhd87hbzyu5ic6asaqlo81d5"); //     return gvc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dnud6ljljblwvlx76imgoo8i8
// int gvLayout(GVC_t *gvc, graph_t *g, const char *engine) 
public static Object gvLayout(Object... arg) {
UNSUPPORTED("6y1to7xw4qcx9wxk34th6ze7q"); // int gvLayout(GVC_t *gvc, graph_t *g, const char *engine)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9cn2lsc78g1edtf7fifdb4iqx"); //     char buf[256];
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("ajvhg377bzgyjw1u7fc6ynufe"); //     rc = gvlayout_select(gvc, engine);
UNSUPPORTED("5wvj0ph8uqfgg8jl3g39jsf51"); //     if (rc == 999) {
UNSUPPORTED("bqcqxlaqnxiukaofkbaeohlrc"); //         agerr (AGERR, "Layout type: \"%s\" not recognized. Use one of:%s\n",
UNSUPPORTED("6vb5xjowxadh06keqoi8xkixl"); //                 engine, gvplugin_list(gvc, API_layout, engine));
UNSUPPORTED("f3a98gxettwtewduvje9y3524"); //         return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2zgcvtw13j4ii0vfdebphrdmh"); //     if (gvLayoutJobs(gvc, g) == -1)
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("1cg49iv90v5mueolzbu69xy3q"); // /* set bb attribute for basic layout.
UNSUPPORTED("a7i33czrx785isv8bnwlpc39h"); //  * doesn't yet include margins, scaling or page sizes because
UNSUPPORTED("25x3oqnrohvx47gdi47wjsvje"); //  * those depend on the renderer being used. */
UNSUPPORTED("2cxu41gtx0x2822685tf09ctd"); //     if (GD_drawing(g)->landscape)
UNSUPPORTED("bw86vkkrgie6ys9mgl56wng55"); //         sprintf(buf, "%d %d %d %d",
UNSUPPORTED("bejqbur89cau7q2a7x8pbuhd7"); //                 ROUND(GD_bb(g).LL.y), ROUND(GD_bb(g).LL.x),
UNSUPPORTED("3ey1j2uf8t8xsknqe7zba77pt"); //                 ROUND(GD_bb(g).UR.y), ROUND(GD_bb(g).UR.x));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("bw86vkkrgie6ys9mgl56wng55"); //         sprintf(buf, "%d %d %d %d",
UNSUPPORTED("7r04i6r8wgv29cf9sh4x0os5v"); //                 ROUND(GD_bb(g).LL.x), ROUND(GD_bb(g).LL.y),
UNSUPPORTED("buvo4ybvfnr1ki5uxao1rrf74"); //                 ROUND(GD_bb(g).UR.x), ROUND(GD_bb(g).UR.y));
UNSUPPORTED("dpbq2928p9qeg5a464f8e2yjy"); //     agsafeset(g, "bb", buf, "");
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2kh108ndzn77vjo1ch0gt1bak
// int gvRender(GVC_t *gvc, graph_t *g, const char *format, FILE *out) 
public static Object gvRender(Object... arg) {
UNSUPPORTED("e2g1sf67k7u629a0lf4qtd4w8"); // int gvRender(GVC_t *gvc, graph_t *g, const char *format, FILE *out)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("1ag9dz4apxn0w3cz8w2bfm6se"); //     GVJ_t *job;
UNSUPPORTED("8msotrfl0cngiua3j57ylm26b"); //     g = g->root;
UNSUPPORTED("exts51afuertju5ed5v7pdpg7"); //     /* create a job for the required format */
UNSUPPORTED("dn6z1r1bbrtmr58m8dnfgfnm0"); //     rc = gvjobs_output_langname(gvc, format);
UNSUPPORTED("5apijrijm2r8b1g2l4x7iee7s"); //     job = gvc->job;
UNSUPPORTED("5wvj0ph8uqfgg8jl3g39jsf51"); //     if (rc == 999) {
UNSUPPORTED("4lkoedjryn54aff3fyrsewwu5"); //         agerr (AGERR, "Format: \"%s\" not recognized. Use one of:%s\n",
UNSUPPORTED("2pjgp86rkudo6mihbako5yps2"); //                 format, gvplugin_list(gvc, API_device, format));
UNSUPPORTED("f3a98gxettwtewduvje9y3524"); //         return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ect62lxc3zm51lhzifift55m"); //     job->output_lang = gvrender_select(job, job->output_langname);
UNSUPPORTED("ewlceg1k4gs2e6syq4ear5kzo"); //     if (!(agbindrec(g, "Agraphinfo_t", 0, NOT(0)) && GD_drawing(g)) && !(job->flags & (1<<26))) {
UNSUPPORTED("3yo4xyapbp7osp8uyz4kff98s"); // 	agerrorf( "Layout was not done\n");
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2ai20uylya195fbdqwjy9bz0n"); //     job->output_file = out;
UNSUPPORTED("10kpqi6pvibjsxjyg0g76lix3"); //     if (out == NULL)
UNSUPPORTED("d47ukby9krmz2k8ycmzzynnfr"); // 	job->flags |= (1<<27);
UNSUPPORTED("9szsye4q9jykqvtk0bc1r91d0"); //     rc = gvRenderJobs(gvc, g);
UNSUPPORTED("7l8ugws8ptgtlxc1ymmh3cf18"); //     gvrender_end_job(job);
UNSUPPORTED("a9p7yonln7g91ge7xab3xf9dr"); //     gvjobs_delete(gvc);
UNSUPPORTED("5bc9k4vsl6g7wejc5xefc5964"); //     return rc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2e8uwmja1nq8v3iktli5cq851
// int gvRenderFilename(GVC_t *gvc, graph_t *g, const char *format, const char *filename) 
public static Object gvRenderFilename(Object... arg) {
UNSUPPORTED("9xxpjotl5x2p5wtwqunca24kv"); // int gvRenderFilename(GVC_t *gvc, graph_t *g, const char *format, const char *filename)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("1ag9dz4apxn0w3cz8w2bfm6se"); //     GVJ_t *job;
UNSUPPORTED("8msotrfl0cngiua3j57ylm26b"); //     g = g->root;
UNSUPPORTED("exts51afuertju5ed5v7pdpg7"); //     /* create a job for the required format */
UNSUPPORTED("dn6z1r1bbrtmr58m8dnfgfnm0"); //     rc = gvjobs_output_langname(gvc, format);
UNSUPPORTED("5apijrijm2r8b1g2l4x7iee7s"); //     job = gvc->job;
UNSUPPORTED("5wvj0ph8uqfgg8jl3g39jsf51"); //     if (rc == 999) {
UNSUPPORTED("cpui2f5hfk3cihua2ie59746n"); // 	agerr(AGERR, "Format: \"%s\" not recognized. Use one of:%s\n",
UNSUPPORTED("2pjgp86rkudo6mihbako5yps2"); //                 format, gvplugin_list(gvc, API_device, format));
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ect62lxc3zm51lhzifift55m"); //     job->output_lang = gvrender_select(job, job->output_langname);
UNSUPPORTED("ewlceg1k4gs2e6syq4ear5kzo"); //     if (!(agbindrec(g, "Agraphinfo_t", 0, NOT(0)) && GD_drawing(g)) && !(job->flags & (1<<26))) {
UNSUPPORTED("3yo4xyapbp7osp8uyz4kff98s"); // 	agerrorf( "Layout was not done\n");
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("23iaqajrvbemalclzlgih1a09"); //     gvjobs_output_filename(gvc, filename);
UNSUPPORTED("9szsye4q9jykqvtk0bc1r91d0"); //     rc = gvRenderJobs(gvc, g);
UNSUPPORTED("7l8ugws8ptgtlxc1ymmh3cf18"); //     gvrender_end_job(job);
UNSUPPORTED("dql0bth0nzsrpiu9vnffonrhf"); //     gvdevice_finalize(job);
UNSUPPORTED("a9p7yonln7g91ge7xab3xf9dr"); //     gvjobs_delete(gvc);
UNSUPPORTED("5bc9k4vsl6g7wejc5xefc5964"); //     return rc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e73ju97kxz883q0hp1udq4rd7
// int gvRenderContext(GVC_t *gvc, graph_t *g, const char *format, void *context) 
public static Object gvRenderContext(Object... arg) {
UNSUPPORTED("6bxfu9f9cshxn0i97berfl9bw"); // int gvRenderContext(GVC_t *gvc, graph_t *g, const char *format, void *context)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("1ag9dz4apxn0w3cz8w2bfm6se"); //     GVJ_t *job;
UNSUPPORTED("8msotrfl0cngiua3j57ylm26b"); //     g = g->root;
UNSUPPORTED("exts51afuertju5ed5v7pdpg7"); //     /* create a job for the required format */
UNSUPPORTED("dn6z1r1bbrtmr58m8dnfgfnm0"); //     rc = gvjobs_output_langname(gvc, format);
UNSUPPORTED("5apijrijm2r8b1g2l4x7iee7s"); //     job = gvc->job;
UNSUPPORTED("5wvj0ph8uqfgg8jl3g39jsf51"); //     if (rc == 999) {
UNSUPPORTED("8r1a6szpsnku0jhatqkh0qo75"); // 		agerr(AGERR, "Format: \"%s\" not recognized. Use one of:%s\n",
UNSUPPORTED("2pj79j8toe6bactkaedt54xcv"); // 			  format, gvplugin_list(gvc, API_device, format));
UNSUPPORTED("b0epxudfxjm8kichhaautm2qi"); // 		return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ect62lxc3zm51lhzifift55m"); //     job->output_lang = gvrender_select(job, job->output_langname);
UNSUPPORTED("ewlceg1k4gs2e6syq4ear5kzo"); //     if (!(agbindrec(g, "Agraphinfo_t", 0, NOT(0)) && GD_drawing(g)) && !(job->flags & (1<<26))) {
UNSUPPORTED("3yo4xyapbp7osp8uyz4kff98s"); // 	agerrorf( "Layout was not done\n");
UNSUPPORTED("b0epxudfxjm8kichhaautm2qi"); // 		return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ex1rhur9nlj950oe8r621uxxk"); //     job->context = context;
UNSUPPORTED("3hvm1mza6yapsb3hi7bkw03cs"); //     job->external_context = NOT(0);
UNSUPPORTED("9szsye4q9jykqvtk0bc1r91d0"); //     rc = gvRenderJobs(gvc, g);
UNSUPPORTED("7l8ugws8ptgtlxc1ymmh3cf18"); //     gvrender_end_job(job);
UNSUPPORTED("dql0bth0nzsrpiu9vnffonrhf"); //     gvdevice_finalize(job);
UNSUPPORTED("a9p7yonln7g91ge7xab3xf9dr"); //     gvjobs_delete(gvc);
UNSUPPORTED("5bc9k4vsl6g7wejc5xefc5964"); //     return rc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2p4eja1ksp0cdz8qxl7wpzez9
// int gvRenderData(GVC_t *gvc, graph_t *g, const char *format, char **result, unsigned int *length) 
public static Object gvRenderData(Object... arg) {
UNSUPPORTED("epzew3wavf5f9mykc38usb6r9"); // int gvRenderData(GVC_t *gvc, graph_t *g, const char *format, char **result, unsigned int *length)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1bh3yj957he6yv2dkeg4pzwdk"); //     int rc;
UNSUPPORTED("1ag9dz4apxn0w3cz8w2bfm6se"); //     GVJ_t *job;
UNSUPPORTED("8msotrfl0cngiua3j57ylm26b"); //     g = g->root;
UNSUPPORTED("exts51afuertju5ed5v7pdpg7"); //     /* create a job for the required format */
UNSUPPORTED("dn6z1r1bbrtmr58m8dnfgfnm0"); //     rc = gvjobs_output_langname(gvc, format);
UNSUPPORTED("5apijrijm2r8b1g2l4x7iee7s"); //     job = gvc->job;
UNSUPPORTED("5wvj0ph8uqfgg8jl3g39jsf51"); //     if (rc == 999) {
UNSUPPORTED("cpui2f5hfk3cihua2ie59746n"); // 	agerr(AGERR, "Format: \"%s\" not recognized. Use one of:%s\n",
UNSUPPORTED("2pjgp86rkudo6mihbako5yps2"); //                 format, gvplugin_list(gvc, API_device, format));
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ect62lxc3zm51lhzifift55m"); //     job->output_lang = gvrender_select(job, job->output_langname);
UNSUPPORTED("ewlceg1k4gs2e6syq4ear5kzo"); //     if (!(agbindrec(g, "Agraphinfo_t", 0, NOT(0)) && GD_drawing(g)) && !(job->flags & (1<<26))) {
UNSUPPORTED("3yo4xyapbp7osp8uyz4kff98s"); // 	agerrorf( "Layout was not done\n");
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e2razcqp20hymeukedg9bannu"); // /* page size on Linux, Mac OS X and Windows */
UNSUPPORTED("78h54wwr6x96fwwes4nhn1tnu"); //     if(!result || !(*result = malloc(4096))) {
UNSUPPORTED("48p1xzjf31myn1slku76g3c8j"); // 	agerr(AGERR, "failure malloc'ing for result string");
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f3ljfuaa9qaj4cd1xhojgsd7d"); //     job->output_data = *result;
UNSUPPORTED("619t5sjk3c37ujk3ndu91wamv"); //     job->output_data_allocated = 4096;
UNSUPPORTED("dbvvz39zyfsbhofeha2hb1wpf"); //     job->output_data_position = 0;
UNSUPPORTED("9szsye4q9jykqvtk0bc1r91d0"); //     rc = gvRenderJobs(gvc, g);
UNSUPPORTED("7l8ugws8ptgtlxc1ymmh3cf18"); //     gvrender_end_job(job);
UNSUPPORTED("78jlu7v0o8b8itbx8ri5ptkc7"); //     if (rc == 0) {
UNSUPPORTED("2w0i2qgohz8ogcqrg43r585dh"); // 	*result = job->output_data;
UNSUPPORTED("cnqap0ow188zdpvtqu6bfrmyr"); // 	*length = job->output_data_position;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a9p7yonln7g91ge7xab3xf9dr"); //     gvjobs_delete(gvc);
UNSUPPORTED("5bc9k4vsl6g7wejc5xefc5964"); //     return rc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9dmqkq749nve1m6f0uz279w5x
// void gvFreeRenderData (char* data) 
public static Object gvFreeRenderData(Object... arg) {
UNSUPPORTED("4zoblerqjutmiyqrcnymyvo8"); // void gvFreeRenderData (char* data)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("23jg3jufd8h5n6iqw6jh5trop"); //     free (data);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 63riwkqx5e0b3n3yyjalaveuy
// void gvAddLibrary(GVC_t *gvc, gvplugin_library_t *lib) 
public static Object gvAddLibrary(Object... arg) {
UNSUPPORTED("31dpjbojh15wnjppy2p096exn"); // void gvAddLibrary(GVC_t *gvc, gvplugin_library_t *lib)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eawa3cjrrmhe2wb24kkblkv6k"); //     gvconfig_plugin_install_from_library(gvc, NULL, lib);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bpzhymnb55ana0o9q691it06
// char **gvcInfo(GVC_t* gvc) 
public static Object gvcInfo(Object... arg) {
UNSUPPORTED("4o912yqrgsuc4t2uym0i2siv3"); // char **gvcInfo(GVC_t* gvc) { return gvc->common.info; }

throw new UnsupportedOperationException();
}




//3 6gnk937rtz89xtqqmf7nb8mod
// char *gvcVersion(GVC_t* gvc) 
public static Object gvcVersion(Object... arg) {
UNSUPPORTED("34219rcx2cc7lx28xxwdo2zic"); // char *gvcVersion(GVC_t* gvc) { return gvc->common.info[1]; }

throw new UnsupportedOperationException();
}




//3 if7qohbqqln8g7vnhx128b1k
// char *gvcBuildDate(GVC_t* gvc) 
public static Object gvcBuildDate(Object... arg) {
UNSUPPORTED("7r5j1w6y3ov7ta18s257h2rqv"); // char *gvcBuildDate(GVC_t* gvc) { return gvc->common.info[2]; }

throw new UnsupportedOperationException();
}


}
