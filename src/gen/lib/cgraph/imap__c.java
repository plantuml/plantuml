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
 * (C) Copyright 2009-2022, Arnaud Roques
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
package gen.lib.cgraph;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agraph_s;
import h.ST_IMapEntry_t;
import h.ST_dt_s;
import smetana.core.CString;

public class imap__c {



//3 79n6elfqk1vw36hmv7bxlrb0v
// static int idcmpf(Dict_t * d, void *arg_p0, void *arg_p1, Dtdisc_t * disc) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="idcmpf", key="79n6elfqk1vw36hmv7bxlrb0v", definition="static int idcmpf(Dict_t * d, void *arg_p0, void *arg_p1, Dtdisc_t * disc)")
public static Object idcmpf(Object... arg) {
UNSUPPORTED("1r7psgafk53qtogr4ft1z3lze"); // static int idcmpf(Dict_t * d, void *arg_p0, void *arg_p1, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ntp67b3zenb7lwin6la68y9g"); //     IMapEntry_t *p0, *p1;
UNSUPPORTED("6z29omss9ay00bqf6xael7t6t"); //     (void) d;
UNSUPPORTED("44n3fpcv1bzssspskdg8kbbz4"); //     p0 = arg_p0;
UNSUPPORTED("4tgwqnnain0i2lv7it6su8k8q"); //     p1 = arg_p1;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("49gg5v29upcoktnre7tua6o3j"); //     return (p0->id - p1->id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aadw62b6y0d22xf4720pzdiyz
// static int namecmpf(Dict_t * d, void *arg_p0, void *arg_p1, 		    Dtdisc_t * disc) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="namecmpf", key="aadw62b6y0d22xf4720pzdiyz", definition="static int namecmpf(Dict_t * d, void *arg_p0, void *arg_p1, 		    Dtdisc_t * disc)")
public static Object namecmpf(Object... arg) {
UNSUPPORTED("7ouzah61jyng2a8u8dfdxeekw"); // static int namecmpf(Dict_t * d, void *arg_p0, void *arg_p1,
UNSUPPORTED("3hfqv3wxw19wel6xzpj1kbshb"); // 		    Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ntp67b3zenb7lwin6la68y9g"); //     IMapEntry_t *p0, *p1;
UNSUPPORTED("6z29omss9ay00bqf6xael7t6t"); //     (void) d;
UNSUPPORTED("44n3fpcv1bzssspskdg8kbbz4"); //     p0 = arg_p0;
UNSUPPORTED("4tgwqnnain0i2lv7it6su8k8q"); //     p1 = arg_p1;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("eb7z4ayiwked6cpuntm496002"); //     return (p0->str - p1->str);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 cvijg1ekkl36sildxxf28vhug
// static Dtdisc_t LookupByName = 


//1 dkzjlpsv4zk993r3iyo7msr8n
// static Dtdisc_t LookupById = 




//3 mx2krtbgfhcihopw9rw8kcv3
// int aginternalmaplookup(Agraph_t * g, int objtype, char *str, 			unsigned long *result) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="aginternalmaplookup", key="mx2krtbgfhcihopw9rw8kcv3", definition="int aginternalmaplookup(Agraph_t * g, int objtype, char *str, 			unsigned long *result)")
public static int aginternalmaplookup(ST_Agraph_s g, int objtype, CString str, int result[]) {
ENTERING("mx2krtbgfhcihopw9rw8kcv3","aginternalmaplookup");
try {
 UNSUPPORTED("9xuzgjxqveawe6v2n4x48r93l"); // int aginternalmaplookup(Agraph_t * g, int objtype, char *str,
UNSUPPORTED("a9jw0mphzrt0q739cxcgk2hxw"); // 			unsigned long *result)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("l4y6zpshfefue2m18wlswfkp"); //     Dict_t *d;
UNSUPPORTED("6ich6qfkkifpsux1v4vgzhiyb"); //     IMapEntry_t *sym, template;
UNSUPPORTED("4uffdlbjda8w15jyto7gd77bw"); //     char *search_str;
UNSUPPORTED("84sccu12ven74lipf2dljgik4"); //     if (objtype == AGINEDGE)
UNSUPPORTED("5q9qhv35w1rsuiuzqkwgshm3p"); // 	objtype = AGEDGE;
UNSUPPORTED("drm2n6i20x3uimml5ooxm9u25"); //     if ((d = g->clos->lookup_by_name[objtype])) {
UNSUPPORTED("9ysphludc93c139uov8ximaj2"); // 	if ((search_str = agstrbind(g, str))) {
UNSUPPORTED("73apfmwxngxpf2twqiokd75ph"); // 	    template.str = search_str;
UNSUPPORTED("1r11yngj3z66q9h8k7rx0ifra"); // 	    sym = (IMapEntry_t *) (*(((Dt_t*)(d))->searchf))((d),(void*)(&template),0000004);
UNSUPPORTED("8watgmdse1o9uhfuhoexemnl2"); // 	    if (sym) {
UNSUPPORTED("68xn6zrkilfqqsosou3z2ym7o"); // 		*result = sym->id;
UNSUPPORTED("a1a1uhff21noh1htwzn6yp831"); // 		return (!(0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("297p5iu8oro94tdg9v29bbgiw"); //     return (0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("mx2krtbgfhcihopw9rw8kcv3","aginternalmaplookup");
}
}




//3 ce8fo5gya95enhgssezqs3vav
// void aginternalmapinsert(Agraph_t * g, int objtype, char *str, 			 unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="aginternalmapinsert", key="ce8fo5gya95enhgssezqs3vav", definition="void aginternalmapinsert(Agraph_t * g, int objtype, char *str, 			 unsigned long id)")
public static Object aginternalmapinsert(Object... arg) {
UNSUPPORTED("bk4ucrzua03gr9lak6zfm3orp"); // void aginternalmapinsert(Agraph_t * g, int objtype, char *str,
UNSUPPORTED("18mfx819yhg1vg5xkrfjr96x2"); // 			 unsigned long id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bh4b0zdn246m225u6zmdk75zs"); //     IMapEntry_t *ent;
UNSUPPORTED("84hwqfygghmcbo2exzo01t93o"); //     Dict_t *d_name_to_id, *d_id_to_name;
UNSUPPORTED("7wtz0h3ahdkrul4dbb0b7y0l4"); //     ent = ((IMapEntry_t*)(agalloc(g,sizeof(IMapEntry_t))));
UNSUPPORTED("6nwwse4s1ba0m9jfptthwwjbj"); //     ent->id = id;
UNSUPPORTED("d8vvc9mhtu43xfmzodcja9bu9"); //     ent->str = agstrdup(g, str);
UNSUPPORTED("84sccu12ven74lipf2dljgik4"); //     if (objtype == AGINEDGE)
UNSUPPORTED("5q9qhv35w1rsuiuzqkwgshm3p"); // 	objtype = AGEDGE;
UNSUPPORTED("cdo42je1dwhjo7hka7tk4bu20"); //     if ((d_name_to_id = g->clos->lookup_by_name[objtype]) == ((Dict_t *)0))
UNSUPPORTED("7tkuaa0dee3i2mkwj7ba9bmyj"); // 	d_name_to_id = g->clos->lookup_by_name[objtype] =
UNSUPPORTED("5em5y2jcl61223aawnzfqj4a0"); // 	    agdtopen(g, &LookupByName, Dttree);
UNSUPPORTED("4x4ql6no5kg3qbek3o45fn7nx"); //     if ((d_id_to_name = g->clos->lookup_by_id[objtype]) == ((Dict_t *)0))
UNSUPPORTED("7tmtjlukhbvxyxkd08ijr2m47"); // 	d_id_to_name = g->clos->lookup_by_id[objtype] =
UNSUPPORTED("bnhc1g7rce6un1du4m54v8m5r"); // 	    agdtopen(g, &LookupById, Dttree);
UNSUPPORTED("1y4a6gz63nnj0k1ip32krystr"); //     (*(((Dt_t*)(d_name_to_id))->searchf))((d_name_to_id),(void*)(ent),0000001);
UNSUPPORTED("6d4vd73oyoit1sj5kt1otroy9"); //     (*(((Dt_t*)(d_id_to_name))->searchf))((d_id_to_name),(void*)(ent),0000001);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="", key="3r16pkjiksv8i7o961ltxyge6", definition="static IMapEntry_t *find_isym(Agraph_t * g, int objtype, unsigned long id)")
public static ST_IMapEntry_t find_isym(ST_Agraph_s g, int objtype, int id) {
ENTERING("3r16pkjiksv8i7o961ltxyge6","find_isym");
try {
    ST_dt_s d;
    ST_IMapEntry_t isym, itemplate = new ST_IMapEntry_t();
    if (objtype == AGINEDGE)
	objtype = AGEDGE;
    if ((d = g.clos.lookup_by_id[objtype])!=null) {
    UNSUPPORTED("itemplate.id = id;");
	isym = (ST_IMapEntry_t) UNSUPPORTED("(IMapEntry_t *) (*(((Dt_t*)(d))->searchf))((d),(void*)(&itemplate),0000004)");
    } else
	isym = null;
    return isym;
} finally {
LEAVING("3r16pkjiksv8i7o961ltxyge6","find_isym");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="", key="foe6bvtujfevsc0f3m8aqln8", definition="char *aginternalmapprint(Agraph_t * g, int objtype, unsigned long id)")
public static CString aginternalmapprint(ST_Agraph_s g, int objtype, int id) {
ENTERING("foe6bvtujfevsc0f3m8aqln8","aginternalmapprint");
try {
	ST_IMapEntry_t isym;
    if ((isym = find_isym(g, objtype, id))!=null)
	return isym.str;
    return null;
} finally {
LEAVING("foe6bvtujfevsc0f3m8aqln8","aginternalmapprint");
}
}




//3 5tlg05avf32knqysibbic9jou
// int aginternalmapdelete(Agraph_t * g, int objtype, unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="aginternalmapdelete", key="5tlg05avf32knqysibbic9jou", definition="int aginternalmapdelete(Agraph_t * g, int objtype, unsigned long id)")
public static Object aginternalmapdelete(Object... arg) {
UNSUPPORTED("19jjvbvuhgwuct5d89a9klzdn"); // int aginternalmapdelete(Agraph_t * g, int objtype, unsigned long id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4s0k0w2gqymjv4rw9e0p94o4e"); //     IMapEntry_t *isym;
UNSUPPORTED("84sccu12ven74lipf2dljgik4"); //     if (objtype == AGINEDGE)
UNSUPPORTED("5q9qhv35w1rsuiuzqkwgshm3p"); // 	objtype = AGEDGE;
UNSUPPORTED("60rw9rftet6cvrlc5sv6cdvzo"); //     if ((isym = find_isym(g, objtype, id))) {
UNSUPPORTED("6nag2b59botfmnvg4y14s6nez"); // 	(*(((Dt_t*)(g->clos->lookup_by_name[objtype]))->searchf))((g->clos->lookup_by_name[objtype]),(void*)(isym),0000002);
UNSUPPORTED("dsg36gwsles2ud2bgcuw50yi3"); // 	(*(((Dt_t*)(g->clos->lookup_by_id[objtype]))->searchf))((g->clos->lookup_by_id[objtype]),(void*)(isym),0000002);
UNSUPPORTED("avjuzl03gu3mel3wyty6geq69"); // 	agstrfree(g, isym->str);
UNSUPPORTED("6pld2z1bhfc2yevvzc4kxx508"); // 	agfree(g, isym);
UNSUPPORTED("3adr32h5e1fehu4g7j2u24asz"); // 	return (!(0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("297p5iu8oro94tdg9v29bbgiw"); //     return (0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2wanqmzppni4zd3mlb6pkou1k
// void aginternalmapclearlocalnames(Agraph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="aginternalmapclearlocalnames", key="2wanqmzppni4zd3mlb6pkou1k", definition="void aginternalmapclearlocalnames(Agraph_t * g)")
public static Object aginternalmapclearlocalnames(Object... arg) {
UNSUPPORTED("bkfxi88xlzuzwi4a9iqo8pn28"); // void aginternalmapclearlocalnames(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("9lx77yi3grxo0pchpg8wba5f6"); //     IMapEntry_t *sym, *nxt;
UNSUPPORTED("1y1imymtcoka6zqsrmg5hocbt"); //     Dict_t **d_name;
UNSUPPORTED("1lwdd78io6jcv1vyvj9qa9xwd"); //     /* Dict_t **d_id; */
UNSUPPORTED("6qvjz1ziwr3nwocahqvfzw14t"); //     Ag_G_global = g;
UNSUPPORTED("e68mfs18sqlfouc92k24w2fz7"); //     d_name = g->clos->lookup_by_name;
UNSUPPORTED("1upc73ikk3g0k2b325lrtornr"); //     /* d_id = g->clos->lookup_by_id; */
UNSUPPORTED("11oml6vi9s4la6fgcck9ta2y8"); //     for (i = 0; i < 3; i++) {
UNSUPPORTED("6h2zc4egoha1kvb86bjeoan7b"); // 	if (d_name[i]) {
UNSUPPORTED("252siiega2i0vhnempk3jj5gq"); // 	    for (sym = (*(((Dt_t*)(d_name[i]))->searchf))((d_name[i]),(void*)(0),0000200); sym; sym = nxt) {
UNSUPPORTED("9a5rzjs7wyb31qrzau41skcch"); // 		nxt = (*(((Dt_t*)(d_name[i]))->searchf))((d_name[i]),(void*)(sym),0000010);
UNSUPPORTED("620abwca2qrecak9ujprv3erj"); // 		if (sym->str[0] == '%')
UNSUPPORTED("2ys9vkqvwbp3l4dlnzxecpufu"); // 		    aginternalmapdelete(g, i, sym->id);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 exx9lnei1gnd60cuddc52z7i9
// static void closeit(Dict_t ** d) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="closeit", key="exx9lnei1gnd60cuddc52z7i9", definition="static void closeit(Dict_t ** d)")
public static Object closeit(Object... arg) {
UNSUPPORTED("dg5p1tjrd085naw1mo4ichi6q"); // static void closeit(Dict_t ** d)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("11oml6vi9s4la6fgcck9ta2y8"); //     for (i = 0; i < 3; i++) {
UNSUPPORTED("dx4pjim9y4f2jtcu836j38fq8"); // 	if (d[i]) {
UNSUPPORTED("2zjj5fsuyj57whze7pm4bef4e"); // 	    dtclose(d[i]);
UNSUPPORTED("5z84h9gs3klhayayencls3cd3"); // 	    d[i] = ((Dict_t *)0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5dw1m5wgietdxy22txx0l20ph
// void aginternalmapclose(Agraph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/imap.c", name="aginternalmapclose", key="5dw1m5wgietdxy22txx0l20ph", definition="void aginternalmapclose(Agraph_t * g)")
public static Object aginternalmapclose(Object... arg) {
UNSUPPORTED("bdxp6h1xs4z2wt83fv2a3w0y2"); // void aginternalmapclose(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6qvjz1ziwr3nwocahqvfzw14t"); //     Ag_G_global = g;
UNSUPPORTED("9mn11z09mil44jhvfaju1n7i5"); //     closeit(g->clos->lookup_by_name);
UNSUPPORTED("74lp49loz9ng7u2mjovdv1wd5"); //     closeit(g->clos->lookup_by_id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
