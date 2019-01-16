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
import static smetana.core.Macro.UNSUPPORTED;

public class gvloadimage__c {
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




//3 ep6jmhv0is26drryogw6fohi3
// static int gvloadimage_select(GVJ_t * job, char *str) 
public static Object gvloadimage_select(Object... arg) {
UNSUPPORTED("5bb69p9z62wipd3lyplljm2v4"); // static int gvloadimage_select(GVJ_t * job, char *str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5ssvxydvbzuxmb74t0hvdbmws"); //     gvplugin_available_t *plugin;
UNSUPPORTED("9q2rkohjybr7oxouto8gnuzsb"); //     gvplugin_installed_t *typeptr;
UNSUPPORTED("5yly7xo50onlrn1nqjqrr9wo9"); //     plugin = gvplugin_load(job->gvc, API_loadimage, str);
UNSUPPORTED("3qzhu1d1ev2sbxdqyn2rujv5y"); //     if (plugin) {
UNSUPPORTED("9n3145wffxmm0g88h1ajkns50"); //         typeptr = plugin->typeptr;
UNSUPPORTED("2ui2g5j7vqtvf4ojbk62b0iqw"); //         job->loadimage.engine = (gvloadimage_engine_t *) (typeptr->engine);
UNSUPPORTED("cfrvp22dhfgc4junmqcolizhe"); //         job->loadimage.id = typeptr->id;
UNSUPPORTED("51t151ipzzvnbstd9b6wdf6hc"); //         return 300;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bel174mlivqazll07qqj29en"); //     return 999;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a1485vryn659qrl2rhyrgd6dt
// void gvloadimage(GVJ_t * job, usershape_t *us, boxf b, boolean filled, const char *target) 
public static Object gvloadimage(Object... arg) {
UNSUPPORTED("21k2magil01siu25ovmraoqbg"); // void gvloadimage(GVJ_t * job, usershape_t *us, boxf b, boolean filled, const char *target)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e3pb3haf1s6pn0amul7acf5km"); //     gvloadimage_engine_t *gvli;
UNSUPPORTED("ccf1hpkthzbxg1bqw930patsh"); //     char type[128];
UNSUPPORTED("1nk5slg1h855ktp6d9uxli7u3"); //     assert(job);
UNSUPPORTED("7gqi0ait1bhc4v9zl8rnn0wbs"); //     assert(us);
UNSUPPORTED("xlffgd4fqdckxg2nbs0mxtlj"); //     assert(us->name);
UNSUPPORTED("fk4kdjctgb13swd2h14t0yvh"); //     assert(us->name[0]);
UNSUPPORTED("2pbo4rf9sxizguooweyns5dan"); //     strcpy(type, us->stringtype);
UNSUPPORTED("19k2ef632wrs9x9vtnv1ijff0"); //     strcat(type, ":");
UNSUPPORTED("aeq1ythwox76trv969mshtfdj"); //     strcat(type, target);
UNSUPPORTED("42tfcob96fcxdwuvlm1jq2rxv"); //     if (gvloadimage_select(job, type) == 999)
UNSUPPORTED("d5epmnbww8kj1vdjo9mwj8qpo"); // 	    agerr (AGWARN, "No loadimage plugin for \"%s\"\n", type);
UNSUPPORTED("1idhakd1v5ijtt2ohczpakl6n"); //     if ((gvli = job->loadimage.engine) && gvli->loadimage)
UNSUPPORTED("8vlzrv54d9rjiv3pxxvhx7wht"); // 	gvli->loadimage(job, us, b, filled);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
