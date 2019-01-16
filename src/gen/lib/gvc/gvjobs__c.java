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

public class gvjobs__c {
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


//1 1pjfdgts3f7oydy0y9jhjugta
// static GVJ_t *output_filename_job


//1 7y5n04mdy4v0ksvy1f8u1ufu1
// static GVJ_t *output_langname_job




//3 39qttb09ieneoczohoe18m73o
// void gvjobs_output_filename(GVC_t * gvc, const char *name) 
public static Object gvjobs_output_filename(Object... arg) {
UNSUPPORTED("3ryvg5oev9sja9lw3ofgqed7w"); // void gvjobs_output_filename(GVC_t * gvc, const char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cv040o08cv8281cfqhv8854yq"); //     if (!gvc->jobs) {
UNSUPPORTED("a9cgmkhzy1dikep3roojjbcte"); // 	output_filename_job = gvc->job = gvc->jobs = zmalloc(sizeof(GVJ_t));
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("d3hw0xdpchwiy1k2rcc44xv4j"); // 	if (!output_filename_job) {
UNSUPPORTED("36c32ldaxz6vogmj5nhf92h83"); // 	    output_filename_job = gvc->jobs;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("485550438nef76qt5kg2ly349"); // 	    if (!output_filename_job->next) {
UNSUPPORTED("5oixw6s3swg9hznwx4czokjbk"); // 		output_filename_job->next = zmalloc(sizeof(GVJ_t));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("b8inpu32tu6zqo000moq75wy0"); // 	    output_filename_job = output_filename_job->next;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8rlxv3o0lru4utlh7jl0f0vue"); //     output_filename_job->output_filename = name;
UNSUPPORTED("62fru6gff40i2nhkhk1o43tsh"); //     output_filename_job->gvc = gvc;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4i5tkl55fx3u0wrjngy8rlrlf
// boolean gvjobs_output_langname(GVC_t * gvc, const char *name) 
public static Object gvjobs_output_langname(Object... arg) {
UNSUPPORTED("7pknulb4k1s97jbz2490bhjil"); // boolean gvjobs_output_langname(GVC_t * gvc, const char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cv040o08cv8281cfqhv8854yq"); //     if (!gvc->jobs) {
UNSUPPORTED("bo2bep4t13b563p9bkmze288q"); // 	output_langname_job = gvc->job = gvc->jobs = zmalloc(sizeof(GVJ_t));
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("3llf13j0vc8xk1fxkw9pyxdby"); // 	if (!output_langname_job) {
UNSUPPORTED("8sydwjd6sltqbn798ugzschoj"); // 	    output_langname_job = gvc->jobs;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("cyn28cpa3wbtakiskqjpzycm"); // 	    if (!output_langname_job->next) {
UNSUPPORTED("aichf642t6sfhx7rmhieyx9ds"); // 		output_langname_job->next = zmalloc(sizeof(GVJ_t));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("35r3hch2gu4t2k2cwybo9zg6"); // 	    output_langname_job = output_langname_job->next;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("89g459qqeg1bo774abkcni1lt"); //     output_langname_job->output_langname = name;
UNSUPPORTED("d5cmms7hxpqitvmv6grhco7a9"); //     output_langname_job->gvc = gvc;
UNSUPPORTED("9j8seo67dfwx0qo2t0owy5ss3"); //     /* load it now to check that it exists */
UNSUPPORTED("9c0elq6msr2m6yc2h6gmdodzo"); //     if (gvplugin_load(gvc, API_device, name))
UNSUPPORTED("bp2y18pqq5n09006utwifdyxo"); // 	return NOT(0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e8pwlap2rcl8cv9ofdfafjvno
// GVJ_t *gvjobs_first(GVC_t * gvc) 
public static Object gvjobs_first(Object... arg) {
UNSUPPORTED("ai610ye6dr7wwxocu8id01h8k"); // GVJ_t *gvjobs_first(GVC_t * gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5v9dnf37wu1p4ydy7jec1vsdp"); //     return (gvc->job = gvc->jobs);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 306mhd1v2f9ubaamkhjsvd8qp
// GVJ_t *gvjobs_next(GVC_t * gvc) 
public static Object gvjobs_next(Object... arg) {
UNSUPPORTED("cl5sw4h8jxwka40ye3iclxeye"); // GVJ_t *gvjobs_next(GVC_t * gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a78h71dhtlrepn6wv5z3kjyua"); //     GVJ_t *job = gvc->job->next;
UNSUPPORTED("4ugafys4138jbf2cwbngr1rmf"); //     if (job) {
UNSUPPORTED("c2ja6xkql14tp4abs4rjoczap"); // 	/* if langname not specified, then repeat previous value */
UNSUPPORTED("9pe1w1tplonkmhbh6oxtgvk0m"); // 	if (!job->output_langname)
UNSUPPORTED("7meekiuuvso01zylb5f9yhxhn"); // 	    job->output_langname = gvc->job->output_langname;
UNSUPPORTED("b4q9cevqaqfulbq7fxg62domu"); // 	/* if filename not specified, then leave NULL to indicate stdout */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1qgmwakcokc36ya4c4e9x0slz"); //     return (gvc->job = job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 792fo3o9fsxvoz433ctlnc4fc
// gv_argvlist_t *gvNEWargvlist(void) 
public static Object gvNEWargvlist(Object... arg) {
UNSUPPORTED("6zo529p4sml722e6ll9gxyt7l"); // gv_argvlist_t *gvNEWargvlist(void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2pnc0kxk2813dwiy45sgvw48"); //     return (gv_argvlist_t*)zmalloc(sizeof(gv_argvlist_t));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1vrgzg9tosokrv5epywnpck6p
// void gv_argvlist_set_item(gv_argvlist_t *list, int index, char *item) 
public static Object gv_argvlist_set_item(Object... arg) {
UNSUPPORTED("8bargfhdrutc1xv5dwu2chxuv"); // void gv_argvlist_set_item(gv_argvlist_t *list, int index, char *item)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3fw3kxowrs2t9drym6ls89uy5"); //     if (index >= list->alloc) {
UNSUPPORTED("1uc5g7t5w5e108ipxs91oph56"); // 	list->alloc = index + 10;
UNSUPPORTED("c5yzzd37ss3fgbr0b3a0aq6z7"); // 	list->argv = grealloc(list->argv, (list->alloc)*(sizeof(char*)));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bl6ru62mgdiir5470o069hfd1"); //     list->argv[index] = item;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d2b5k3hlhil0de7fa1hu7l94h
// void gv_argvlist_reset(gv_argvlist_t *list) 
public static Object gv_argvlist_reset(Object... arg) {
UNSUPPORTED("850opb4ojq26l924543kc1gok"); // void gv_argvlist_reset(gv_argvlist_t *list)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("doj8fyzeqsl0r4wgj9lh430ox"); //     if (list->argv)
UNSUPPORTED("xlt093t6b7j3pnv9hu2vt66f"); // 	free(list->argv);
UNSUPPORTED("ap2mjy4r8fpmayf9uuxp4jf89"); //     list->argv = NULL;
UNSUPPORTED("4sqkjh7d3c62r110mi2a83svb"); //     list->alloc = 0;
UNSUPPORTED("7ro2etssmfzc4a1isfbbx6eed"); //     list->argc = 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5mgwysotnvkta4thc1xg5fi54
// void gv_argvlist_free(gv_argvlist_t *list) 
public static Object gv_argvlist_free(Object... arg) {
UNSUPPORTED("ea3c0vjqyvj4dxsejfvmh35gb"); // void gv_argvlist_free(gv_argvlist_t *list)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("doj8fyzeqsl0r4wgj9lh430ox"); //     if (list->argv)
UNSUPPORTED("xlt093t6b7j3pnv9hu2vt66f"); // 	free(list->argv);
UNSUPPORTED("77gdlnaiah901ax125wm0qh80"); //     free(list);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2wvv6fkvq52bzf9nxkfkegeaq
// void gvjobs_delete(GVC_t * gvc) 
public static Object gvjobs_delete(Object... arg) {
UNSUPPORTED("crqxrbflqt2g954zn4tcroo9w"); // void gvjobs_delete(GVC_t * gvc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("97ra1ormlmahj8vge2eunl30v"); //     GVJ_t *job, *j;
UNSUPPORTED("3pmzbzpokb2qak70z519miwt2"); //     job = gvc->jobs;
UNSUPPORTED("eaj0v3cv4vpxfa9s4a9encgvb"); //     while ((j = job)) {
UNSUPPORTED("c3q9r9u9bkmzersvod3mnf57w"); // 	job = job->next;
UNSUPPORTED("1cdz5buzwqhtha92lvuuza7np"); // 	gv_argvlist_reset(&(j->selected_obj_attributes));
UNSUPPORTED("5zd7n7b1fs7wf1p5gxhwc18tq"); // 	gv_argvlist_reset(&(j->selected_obj_type_name));
UNSUPPORTED("7v6blrn64uyfyf00wiegwiolr"); // 	if (j->active_tooltip)
UNSUPPORTED("13harrcujsq9tfn9l9si9smra"); // 	    free(j->active_tooltip);
UNSUPPORTED("3szbju1px4sq4hkqtrrzspao7"); // 	if (j->selected_href)
UNSUPPORTED("cpy4ywcq3ss7v5o2ymb3x26bb"); // 	    free(j->selected_href);
UNSUPPORTED("6vry3qvmafu1jm517mnutt1f7"); // 	free(j);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4q0duus5k3hueqb940j1h6tc0"); //     gvc->jobs = gvc->job = gvc->active_jobs = output_filename_job = output_langname_job =
UNSUPPORTED("5miekn5jg1682zqyvjvhc1h25"); // 	NULL;
UNSUPPORTED("77k27xankrzoe9n1nj8zbr2j0"); //     gvc->common.viewNum = 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
