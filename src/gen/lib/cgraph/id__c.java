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
import static gen.lib.cgraph.imap__c.aginternalmapinsert;
import static gen.lib.cgraph.imap__c.aginternalmaplookup;
import static gen.lib.cgraph.imap__c.aginternalmapprint;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.refstr__c.agstrbind;
import static gen.lib.cgraph.refstr__c.agstrdup;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGCLOS_id;
import static smetana.core.Macro.AGDISC_id;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ASINT;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agdisc_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class id__c {


//3 a0a2zxsu8n019hzm1rwf1jc7f
// static void *idopen(Agraph_t * g, Agdisc_t* disc) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="", key="a0a2zxsu8n019hzm1rwf1jc7f", definition="static void *idopen(Agraph_t * g, Agdisc_t* disc)")
public static Object idopen(ST_Agraph_s g, ST_Agdisc_s disc) {
ENTERING("a0a2zxsu8n019hzm1rwf1jc7f","idopen");
try {
	return g;
} finally {
LEAVING("a0a2zxsu8n019hzm1rwf1jc7f","idopen");
}
}




//3 lsl0c1gejls1wv04ga6xy2cf
// static long idmap(void *state, int objtype, char *str, unsigned long *id, 		  int createflag) 
//static int ctr = 1;
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="", key="", definition="")
public static int idmap(Object state, int objtype, CString str, int id[], boolean createflag) {
ENTERING("lsl0c1gejls1wv04ga6xy2cf","idmap");
try {
    CString s;
    if (str!=null) {
	ST_Agraph_s g;
	g = (ST_Agraph_s) state;
	if (createflag)
	    s = agstrdup(g, str);
	else
	    s = agstrbind(g, str);
	id[0] = Memory.identityHashCode(s);
    } else {
	id[0] = Z.z().ctr;
	Z.z().ctr += 2;
    }
    return ASINT(N(0));
} finally {
LEAVING("lsl0c1gejls1wv04ga6xy2cf","idmap");
}
}




//3 8ynmf2fueegi7vjejal3ri1ax
// static long idalloc(void *state, int objtype, unsigned long request) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="idalloc", key="8ynmf2fueegi7vjejal3ri1ax", definition="static long idalloc(void *state, int objtype, unsigned long request)")
public static Object idalloc(Object... arg) {
UNSUPPORTED("1z2o91qjhxg0zcs8vgzyl9bf1"); // static long idalloc(void *state, int objtype, unsigned long request)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2s0qc9g3dasd7eqa3rhtlxrae"); //     (void) state;
UNSUPPORTED("x0ltcg0hfp8jlgbjde43bdwj"); //     (void) objtype;
UNSUPPORTED("6xs9bwnce34njm5w424uwon6d"); //     (void) request;
UNSUPPORTED("297p5iu8oro94tdg9v29bbgiw"); //     return (0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5fsdlq8w38bfd7gtwz1z8arad
// static void idfree(void *state, int objtype, unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="idfree", key="5fsdlq8w38bfd7gtwz1z8arad", definition="static void idfree(void *state, int objtype, unsigned long id)")
public static Object idfree(Object... arg) {
UNSUPPORTED("adq5fviqjzpkxrjt37qxo1ywh"); // static void idfree(void *state, int objtype, unsigned long id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("x0ltcg0hfp8jlgbjde43bdwj"); //     (void) objtype;
UNSUPPORTED("e3dd233viwus8xrkad68a1qhr"); //     if (id % 2 == 0)
UNSUPPORTED("69x6bjndheh46syz632mlu192"); // 	agstrfree((Agraph_t *) state, (char *) id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Difficult
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/id.c", name="", key="8143j507ej7uqqjzw5i32xej5", definition="static char *idprint(void *state, int objtype, unsigned long id)")
public static CString idprint(__ptr__ state, int objtype, int id) {
ENTERING("8143j507ej7uqqjzw5i32xej5","idprint");
try {
    if (id % 2 == 0)
	return (CString) Memory.fromIdentityHashCode(id);
    else
	return null;
} finally {
LEAVING("8143j507ej7uqqjzw5i32xej5","idprint");
}
}




//3 44seyu1scoubb1wsuhwlghwyz
// static void idclose(void *state) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="idclose", key="44seyu1scoubb1wsuhwlghwyz", definition="static void idclose(void *state)")
public static Object idclose(Object... arg) {
UNSUPPORTED("18oh21h7t6fg06ozg64u87nyu"); // static void idclose(void *state)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2s0qc9g3dasd7eqa3rhtlxrae"); //     (void) state;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5bjqo0ihl0x25vaspoiehmwzk
// static void idregister(void *state, int objtype, void *obj) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="idregister", key="5bjqo0ihl0x25vaspoiehmwzk", definition="static void idregister(void *state, int objtype, void *obj)")
public static void idregister(Object state, int objtype, Object obj) {
ENTERING("5bjqo0ihl0x25vaspoiehmwzk","idregister");
try {
 
} finally {
LEAVING("5bjqo0ihl0x25vaspoiehmwzk","idregister");
}
}


//1 cxrk51474ugltvklkcvp3v2ly
// Agiddisc_t AgIdDisc = 
/*public final static __struct__<Agiddisc_s> AgIdDisc = JUtils.from(Agiddisc_s.class);
static {
	AgIdDisc.setPtr("open", function(id__c.class, "idopen"));
	AgIdDisc.setPtr("map", function(id__c.class, "idmap"));
	AgIdDisc.setPtr("alloc", function(id__c.class, "idalloc"));
	AgIdDisc.setPtr("free", function(id__c.class, "idfree"));
	AgIdDisc.setPtr("print", function(id__c.class, "idprint"));
	AgIdDisc.setPtr("close", function(id__c.class, "idclose"));
	AgIdDisc.setPtr("idregister", function(id__c.class, "idregister"));
}*/



//3 aq30wwcj4ugatsgx0zdtdmeed
// int agmapnametoid(Agraph_t * g, int objtype, char *str, 		  unsigned long *result, int createflag) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="agmapnametoid", key="aq30wwcj4ugatsgx0zdtdmeed", definition="int agmapnametoid(Agraph_t * g, int objtype, char *str, 		  unsigned long *result, int createflag)")
public static int agmapnametoid(ST_Agraph_s g, int objtype, CString str, int result[], boolean createflag) {
ENTERING("aq30wwcj4ugatsgx0zdtdmeed","agmapnametoid");
try {
    int rv;
    if (str!=null && (str.charAt(0) != '%')) {
    	rv = (Integer) AGDISC_id(g).map.exe(AGCLOS_id(g), objtype, str, result, createflag);
	if (rv!=0)
	    return rv;
    }
    /* either an internal ID, or disc. can't map strings */
    if (str!=null) {
	rv = aginternalmaplookup(g, objtype, str, result);
	if (rv!=0)
	    return rv;
    } else
	rv = 0;
    if (createflag) {
	/* get a new anonymous ID, and store in the internal map */
	rv = (Integer) AGDISC_id(g).map.exe(AGCLOS_id(g), objtype, null, result,
				createflag);
	if (rv!=0 && str!=null)
	    aginternalmapinsert(g, objtype, str, result[0]);
    }
    return rv;
} finally {
LEAVING("aq30wwcj4ugatsgx0zdtdmeed","agmapnametoid");
}
}




//3 dwufsd296z6lfmtm7fp4e3tk7
// int agallocid(Agraph_t * g, int objtype, unsigned long request) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="agallocid", key="dwufsd296z6lfmtm7fp4e3tk7", definition="int agallocid(Agraph_t * g, int objtype, unsigned long request)")
public static Object agallocid(Object... arg) {
UNSUPPORTED("5i7l75ugdm5j5c4xtqqk9atdd"); // int agallocid(Agraph_t * g, int objtype, unsigned long request)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dlf21dkccrftu3zc0t5amuwu4"); //     return ((g)->clos->disc.id)->alloc(((g)->clos->state.id), objtype, request);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cfub4osftc8djs88cq592feu3
// void agfreeid(Agraph_t * g, int objtype, unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/id.c", name="agfreeid", key="cfub4osftc8djs88cq592feu3", definition="void agfreeid(Agraph_t * g, int objtype, unsigned long id)")
public static Object agfreeid(Object... arg) {
UNSUPPORTED("4hpxv4j7rcvj3lrbvu2e0hus4"); // void agfreeid(Agraph_t * g, int objtype, unsigned long id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3s1vo79t7lt6fm1bimxdpbqj"); //     (void) aginternalmapdelete(g, objtype, id);
UNSUPPORTED("dm45ri059viqxsb08rmvo60y1"); //     (((g)->clos->disc.id)->free) (((g)->clos->state.id), objtype, id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




/* agnameof:
 * Return string representation of object.
 * In general, returns the name of node or graph,
 * and the key of an edge. If edge is anonymous, returns NULL.
 * Uses static buffer for anonymous graphs.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/id.c", name="", key="", definition="")
public static CString agnameof(__ptr__ obj) {
ENTERING("cctsybrl54fy799aynfej4iiy","agnameof");
try {
    ST_Agraph_s g;
    CString rv;
    /* perform internal lookup first */
    g = agraphof(obj);
    if ((rv = aginternalmapprint(g, AGTYPE(obj), AGID(obj)))!=null)
	return rv;
    if (AGDISC_id(g).print!=null) {
	if ((rv =
	     (CString) AGDISC_id(g).print.exe(AGCLOS_id(g), AGTYPE(obj), AGID(obj)))!=null)
	    return rv;
    }
    if (AGTYPE(obj) != AGEDGE) {
      rv = new CString("%"+AGID(obj));
    }
    else
	rv = null;
    return rv;
} finally {
LEAVING("cctsybrl54fy799aynfej4iiy","agnameof");
}
}








@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/id.c", name="agregister", key="emt63ldde99jnwe2vvjal9kt9", definition="void agregister(Agraph_t * g, int objtype, void *obj)")
public static void agregister(ST_Agraph_s g, int objtype, Object obj) {
ENTERING("emt63ldde99jnwe2vvjal9kt9","agregister");
try {
	AGDISC_id(g).idregister.exe(AGCLOS_id(g), objtype, obj);
} finally {
LEAVING("emt63ldde99jnwe2vvjal9kt9","agregister");
}
}


}
