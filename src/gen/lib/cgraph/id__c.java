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
package gen.lib.cgraph;
import static gen.lib.cgraph.imap__c.aginternalmapinsert;
import static gen.lib.cgraph.imap__c.aginternalmaplookup;
import static gen.lib.cgraph.imap__c.aginternalmapprint;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.refstr__c.agstrbind;
import static gen.lib.cgraph.refstr__c.agstrdup;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ASINT;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agdisc_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class id__c {
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


//1 6ayavpu39aihwyojkx093pcy3
// extern Agraph_t *Ag_G_global


//1 871mxtg9l6ffpxdl9kniwusf7
// extern char *AgDataRecName


//1 c0o2kmml0tn6hftuwo0u4shwd
// extern Dtdisc_t Ag_subnode_id_disc


//1 8k15pyu256unm2kpd9zf5pf7k
// extern Dtdisc_t Ag_subnode_seq_disc


//1 e3d820y06gpeusn6atgmj8bzd
// extern Dtdisc_t Ag_mainedge_id_disc


//1 cbr0772spix9h1aw7h5v7dv9j
// extern Dtdisc_t Ag_subedge_id_disc


//1 akd0c3v0j7m2npxcb9acit1fa
// extern Dtdisc_t Ag_mainedge_seq_disc


//1 12d8la07351ww7vwfzucjst8m
// extern Dtdisc_t Ag_subedge_seq_disc


//1 29eokk7v88e62g8o6lizmo967
// extern Dtdisc_t Ag_subgraph_id_disc


//1 4xd9cbgy6hk5g6nhjcbpzkx14
// extern Agcbdisc_t AgAttrdisc




//3 a0a2zxsu8n019hzm1rwf1jc7f
// static void *idopen(Agraph_t * g, Agdisc_t* disc) 
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
public static Object idfree(Object... arg) {
UNSUPPORTED("adq5fviqjzpkxrjt37qxo1ywh"); // static void idfree(void *state, int objtype, unsigned long id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("x0ltcg0hfp8jlgbjde43bdwj"); //     (void) objtype;
UNSUPPORTED("e3dd233viwus8xrkad68a1qhr"); //     if (id % 2 == 0)
UNSUPPORTED("69x6bjndheh46syz632mlu192"); // 	agstrfree((Agraph_t *) state, (char *) id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8143j507ej7uqqjzw5i32xej5
// static char *idprint(void *state, int objtype, unsigned long id) 
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
public static Object idclose(Object... arg) {
UNSUPPORTED("18oh21h7t6fg06ozg64u87nyu"); // static void idclose(void *state)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2s0qc9g3dasd7eqa3rhtlxrae"); //     (void) state;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5bjqo0ihl0x25vaspoiehmwzk
// static void idregister(void *state, int objtype, void *obj) 
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
public static int agmapnametoid(ST_Agraph_s g, int objtype, CString str, int result[], boolean createflag) {
ENTERING("aq30wwcj4ugatsgx0zdtdmeed","agmapnametoid");
try {
    int rv;
    if (str!=null && (str.charAt(0) != '%')) {
    	rv = (Integer) g.clos.disc.id.map.exe(g.clos.state.id, objtype, str, result, createflag);
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
	rv = (Integer) g.clos.disc.id.map.exe(g.clos.state.id, objtype, null, result,
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
public static Object agallocid(Object... arg) {
UNSUPPORTED("5i7l75ugdm5j5c4xtqqk9atdd"); // int agallocid(Agraph_t * g, int objtype, unsigned long request)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dlf21dkccrftu3zc0t5amuwu4"); //     return ((g)->clos->disc.id)->alloc(((g)->clos->state.id), objtype, request);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cfub4osftc8djs88cq592feu3
// void agfreeid(Agraph_t * g, int objtype, unsigned long id) 
public static Object agfreeid(Object... arg) {
UNSUPPORTED("4hpxv4j7rcvj3lrbvu2e0hus4"); // void agfreeid(Agraph_t * g, int objtype, unsigned long id)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3s1vo79t7lt6fm1bimxdpbqj"); //     (void) aginternalmapdelete(g, objtype, id);
UNSUPPORTED("dm45ri059viqxsb08rmvo60y1"); //     (((g)->clos->disc.id)->free) (((g)->clos->state.id), objtype, id);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cctsybrl54fy799aynfej4iiy
// char *agnameof(void *obj) 
// private static char buf[32];
public static CString agnameof(__ptr__ obj) {
ENTERING("cctsybrl54fy799aynfej4iiy","agnameof");
try {
    ST_Agraph_s g;
    CString rv;
    /* perform internal lookup first */
    g = agraphof(obj);
    if ((rv = aginternalmapprint(g, AGTYPE(obj), AGID(obj)))!=null)
	return rv;
    if (g.clos.disc.id.print!=null) {
	if ((rv =
	     (CString) g.clos.disc.id.print.exe(g.clos.state.id,
	     AGTYPE(obj), 
	     AGID(obj)))!=null)
	    return rv;
    }
    if (AGTYPE(obj) != AGEDGE) {
      rv = new CString("%"+((ST_Agobj_s)obj.castTo(ST_Agobj_s.class)).tag.id);
    }
    else
	rv = null;
    return rv;
} finally {
LEAVING("cctsybrl54fy799aynfej4iiy","agnameof");
}
}




//3 emt63ldde99jnwe2vvjal9kt9
// void agregister(Agraph_t * g, int objtype, void *obj) 
public static void agregister(ST_Agraph_s g, int objtype, Object obj) {
ENTERING("emt63ldde99jnwe2vvjal9kt9","agregister");
try {
	g.clos.disc.id.idregister.exe(g.clos.state.id, objtype, obj);
} finally {
LEAVING("emt63ldde99jnwe2vvjal9kt9","agregister");
}
}


}
