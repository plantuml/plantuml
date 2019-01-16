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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agdisc_s;
import h.ST_Agraph_s;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class mem__c {
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




//3 akq0jgwdspf75ypeatgcnfn8w
// static void *memopen(Agdisc_t* disc) 
public static Object memopen(ST_Agdisc_s disc) {
ENTERING("akq0jgwdspf75ypeatgcnfn8w","memopen");
try {
	return null;
} finally {
LEAVING("akq0jgwdspf75ypeatgcnfn8w","memopen");
}
}




//3 9mtjrx0vjzwuecjwpxylr9tag
// static void *memalloc(void *heap, size_t request) 
public static __ptr__ memalloc(__ptr__ heap, size_t request) {
ENTERING("9mtjrx0vjzwuecjwpxylr9tag","memalloc");
try {
    __ptr__ rv;
    rv = (__ptr__) request.malloc();
    return rv;
} finally {
LEAVING("9mtjrx0vjzwuecjwpxylr9tag","memalloc");
}
}




//3 18v2hhjculhnb3b7fc4tx3yjw
// static void *memresize(void *heap, void *ptr, size_t oldsize, 		       size_t request) 
public static __ptr__ memresize(__ptr__ heap, __ptr__ ptr, size_t oldsize, size_t request) {
ENTERING("18v2hhjculhnb3b7fc4tx3yjw","memresize");
try {
	request.realloc(ptr);
	return ptr;
} finally {
LEAVING("18v2hhjculhnb3b7fc4tx3yjw","memresize");
}
//UNSUPPORTED("1s6udii0nias7f8g4vimpkefh"); // static void *memresize(void *heap, void *ptr, size_t oldsize,
//UNSUPPORTED("8zs6530gai5ogf503wd0333qh"); // 		       size_t request)
//UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
//UNSUPPORTED("5ccnu5m92hidffpixzo964tna"); //     void *rv;
//UNSUPPORTED("74rq74mh7lnfr9i3qmwsbx2hd"); //     (void) heap;
//UNSUPPORTED("ebomd3babnm180zhyrfeg59wi"); //     rv = realloc(ptr, request);
//UNSUPPORTED("bzgpl0js1bzsovafg9g24v4ya"); //     if (request > oldsize)
//UNSUPPORTED("9cjvc6kya9bwic7bue6mcj8yf"); // 	memset((char *) rv + oldsize, 0, request - oldsize);
//UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
//UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
//
//throw new UnsupportedOperationException();
}




//3 c320bstcg5nctel3onh2pserl
// static void memfree(void *heap, void *ptr) 
public static Object memfree(Object... arg) {
UNSUPPORTED("5yxdf2sc5xnic9d5j24m0a7yf"); // static void memfree(void *heap, void *ptr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("74rq74mh7lnfr9i3qmwsbx2hd"); //     (void) heap;
UNSUPPORTED("f0evk2zajcoprskea22bm18e8"); //     free(ptr);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 1cni5q244gsprpvtjsq7gs17m
// Agmemdisc_t AgMemDisc =     
/*public static final __struct__<Agmemdisc_s> AgMemDisc = JUtils.from(Agmemdisc_s.class);
static {
	AgMemDisc.setPtr("open", function(mem__c.class, "memopen"));
	AgMemDisc.setPtr("alloc", function(mem__c.class, "memalloc"));
	AgMemDisc.setPtr("resize", function(mem__c.class, "memresize"));
	AgMemDisc.setPtr("free", function(mem__c.class, "memfree"));
	AgMemDisc.setPtr("close", null);
}*/



//3 7newv1hmzvt4vtttc9cxdxfpn
// void *agalloc(Agraph_t * g, size_t size) 
public static __ptr__ agalloc(ST_Agraph_s g, size_t size) {
ENTERING("7newv1hmzvt4vtttc9cxdxfpn","agalloc");
try {
	__ptr__ mem;
    mem =  (__ptr__) g.clos.disc.mem.alloc.exe(g.clos.state.mem, size);
    if (mem == null)
	 System.err.println("memory allocation failure");
    return mem;
} finally {
LEAVING("7newv1hmzvt4vtttc9cxdxfpn","agalloc");
}
}




//3 55lm0cse6lsgqblx6puxpjs3j
// void *agrealloc(Agraph_t * g, void *ptr, size_t oldsize, size_t size) 
public static Object agrealloc(Object... arg) {
UNSUPPORTED("910gd4otiivsz2zpsiwlsy00v"); // void *agrealloc(Agraph_t * g, void *ptr, size_t oldsize, size_t size)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("11l1m9u5ne2xf2nff6278od59"); //     void *mem;
UNSUPPORTED("b9ag6d7eml860kbycrkuz14b7"); //     if (size > 0) {
UNSUPPORTED("zjrd9sttelcubi228vbizqq0"); // 	if (ptr == 0)
UNSUPPORTED("vr97hnk6c4k8muqake3c3c46"); // 	    mem = agalloc(g, size);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2n0yfzx569kr1oinsronhmsus"); // 	    mem =
UNSUPPORTED("agphdu4vmb8hu0s57ry4i4axp"); // 		((g)->clos->disc.mem)->resize(((g)->clos->state.mem), ptr, oldsize, size);
UNSUPPORTED("60qvwgrubred6pojjs425ctzr"); // 	if (mem == ((void *)0))
UNSUPPORTED("9vomh5w83j5mf3src00h8g8g0"); // 	     agerr(AGERR,"memory re-allocation failure");
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("6jbj3fx0j7m0vyvwn0za7bxle"); // 	mem = ((void *)0);
UNSUPPORTED("a5guhlttwqpai3dhdhdx6shnu"); //     return mem;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5cy6dl95ayyuzq0m35179g1a1
// void agfree(Agraph_t * g, void *ptr) 
public static Object agfree(Object... arg) {
UNSUPPORTED("4i7lm2j8h5unocyz6c4isbh2f"); // void agfree(Agraph_t * g, void *ptr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("72fdcloikkmdo51qrcdovcy5v"); //     if (ptr)
UNSUPPORTED("efvuftmcvfsswtq39k8vdrgmd"); // 	(((g)->clos->disc.mem)->free) (((g)->clos->state.mem), ptr);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
