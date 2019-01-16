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
import static gen.lib.cgraph.subg__c.agfstsubg;
import static gen.lib.cgraph.subg__c.agnxtsubg;
import static smetana.core.JUtils.function;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import smetana.core.CFunction;
import smetana.core.__ptr__;

public class apply__c {
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




//3 dups4iqoegzha9tq6pupoim08
// static Agobj_t *subnode_search(Agraph_t * sub, Agobj_t * n) 
public static Object subnode_search(Object... arg) {
UNSUPPORTED("pxacbw8fd49n7yuf74ww3m4o"); // static Agobj_t *subnode_search(Agraph_t * sub, Agobj_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5h609thtbczs8d02vo4tkt5av"); //     if (agraphof(n) == sub)
UNSUPPORTED("bp96fem54xcxrw16cmnlpell9"); // 	return n;
UNSUPPORTED("bdb4i0co0gl0r87nhwlfqsbah"); //     return (Agobj_t *) agsubnode(sub, (Agnode_t *) n, (0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1qi7zit7howdl3n270k4whbgh
// static Agobj_t *subedge_search(Agraph_t * sub, Agobj_t * e) 
public static Object subedge_search(Object... arg) {
UNSUPPORTED("4hoc59s32d3n083o2rhe085zx"); // static Agobj_t *subedge_search(Agraph_t * sub, Agobj_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3y411cm763tnv8mvrigf8pg8y"); //     if (agraphof(e) == sub)
UNSUPPORTED("8ihpc010r3nccyfagedpc4nlv"); // 	return e;
UNSUPPORTED("bha9r1zngfg6nmzgqmku0ji52"); //     return (Agobj_t *) agsubedge(sub, (Agedge_t *) e, (0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 95y4aknoddh42lieikrb72vxw
// static Agobj_t *subgraph_search(Agraph_t * sub, Agobj_t * g) 
public static ST_Agobj_s subgraph_search(ST_Agraph_s sub, ST_Agobj_s g) {
ENTERING("95y4aknoddh42lieikrb72vxw","subgraph_search");
try {
    return (ST_Agobj_s) sub.castTo_ST_Agobj_s();
} finally {
LEAVING("95y4aknoddh42lieikrb72vxw","subgraph_search");
}
}




//3 8s9l15wqucf1glmbeb6fmya8e
// static void rec_apply(Agraph_t * g, Agobj_t * obj, agobjfn_t fn, void *arg, 		      agobjsearchfn_t objsearch, int preorder) 
public static void rec_apply(ST_Agraph_s g, ST_Agobj_s obj, CFunction fn, __ptr__ arg, CFunction objsearch, boolean preorder) {
ENTERING("8s9l15wqucf1glmbeb6fmya8e","rec_apply");
try {
    ST_Agraph_s sub;
    ST_Agobj_s subobj;
    if (preorder)
	fn.exe(g, obj, arg);
    for (sub = agfstsubg(g); sub!=null; sub = agnxtsubg(sub)) {
    if ((subobj = (ST_Agobj_s) objsearch.exe(sub, obj))!=null)
	    rec_apply(sub, subobj, fn, arg, objsearch, preorder);
    }
    if (N(preorder))
	fn.exe(g, obj, arg);
} finally {
LEAVING("8s9l15wqucf1glmbeb6fmya8e","rec_apply");
}
}




//3 9hqql178zpl8iudlf6sgnv7aj
// int agapply(Agraph_t * g, Agobj_t * obj, agobjfn_t fn, void *arg, 	    int preorder) 
public static int agapply(ST_Agraph_s g, ST_Agobj_s obj, CFunction fn, __ptr__ arg, boolean preorder) {
ENTERING("9hqql178zpl8iudlf6sgnv7aj","agapply");
try {
	ST_Agobj_s subobj;
    CFunction objsearch=null;
    switch (AGTYPE(obj)) {
    case AGRAPH:
 	objsearch = function(apply__c.class, "subgraph_search");
	break;
    case AGNODE:
UNSUPPORTED("arkoj4niyfqe213zut6szzeji"); // 	objsearch = subnode_search;
	break;
    case AGOUTEDGE:
    case AGINEDGE:
UNSUPPORTED("3h8kzrrsobdp839772gupdrbf"); // 	objsearch = subedge_search;
	break;
    default:
UNSUPPORTED("2pc67byzirrkhe1cmdoguh6i1"); // 	agerr(AGERR, "agapply: unknown object type %d\n", AGTYPE(obj));
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
	break;
    }
    if ((subobj = (ST_Agobj_s) objsearch.exe(g, obj))!=null) {
	rec_apply(g, subobj, fn, arg, objsearch, preorder);
	return 0;
    } else
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("9hqql178zpl8iudlf6sgnv7aj","agapply");
}
}


}
