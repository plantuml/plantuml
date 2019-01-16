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
import static gen.lib.cdt.dtsize__c.dtsize_;
import static gen.lib.cdt.dtview__c.dtview;
import static gen.lib.cgraph.apply__c.agapply;
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agmethod_upd;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.rec__c.agbindrec;
import static gen.lib.cgraph.rec__c.aggetrec;
import static gen.lib.cgraph.refstr__c.agstrdup;
import static gen.lib.cgraph.refstr__c.agstrfree;
import static gen.lib.cgraph.subg__c.agparent;
import static gen.lib.cgraph.utils__c.agdtopen;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.function;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agattr_s;
import h.ST_Agdatadict_s;
import h.ST_Agdesc_s;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agrec_s;
import h.ST_Agsym_s;
import h.ST_Agtag_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;

import java.util.ArrayList;

import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.__ptr__;

public class attr__c {
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


//1 cwbgwzo8cprw4eobs3iji59dp
// Dtdisc_t AgDataDictDisc = 
/*static public final __struct__<_dtdisc_s> AgDataDictDisc = JUtils.from(_dtdisc_s.class);
static {
	AgDataDictDisc.setInt("key", OFFSET.create(Agsym_s.class, "name").toInt());
	AgDataDictDisc.setInt("size", -1);
	AgDataDictDisc.setInt("link", OFFSET.create(Agsym_s.class, "link").toInt());
	AgDataDictDisc.setPtr("makef", null);
	AgDataDictDisc.setPtr("freef", function(attr__c.class, "freesym"));
	AgDataDictDisc.setPtr("comparf", null);
	AgDataDictDisc.setPtr("hashf", null);
}*/

//1 67blfrj9x850g8ccpo9qjer2
// static char DataDictName[] = 
public final static CString DataDictName = new CString("_AG_datadict");
//1 1qn6s7dwoq08ugdjnmsvdgj6u
// static Agdesc_t ProtoDesc = 
/*static final public __struct__<Agdesc_s> ProtoDesc = JUtils.from(Agdesc_s.class);
static {
	ProtoDesc.setInt("directed", 1);
	ProtoDesc.setInt("strict", 0);
	ProtoDesc.setInt("no_loop", 1);
	ProtoDesc.setInt("maingraph", 0);
	ProtoDesc.setInt("flatlock", 1);
	ProtoDesc.setInt("no_write", 1);
}*/


//1 30ftenxl879wmnziizunr5vt1
// static Agraph_t *ProtoGraph
//static public Agraph_s ProtoGraph;



//3 4bm10isw1qq1eqcse8afbxee3
// Agdatadict_t *agdatadict(Agraph_t * g, int cflag) 
public static ST_Agdatadict_s agdatadict(ST_Agraph_s g, boolean cflag) {
ENTERING("4bm10isw1qq1eqcse8afbxee3","agdatadict");
try {
    ST_Agdatadict_s rv;
    rv = (ST_Agdatadict_s) aggetrec(g, DataDictName, false).castTo(ST_Agdatadict_s.class);
    if (rv!=null || N(cflag))
	return rv;
    init_all_attrs(g);
    rv = (ST_Agdatadict_s) aggetrec(g, DataDictName, false).castTo(ST_Agdatadict_s.class);
    return rv;
} finally {
LEAVING("4bm10isw1qq1eqcse8afbxee3","agdatadict");
}
}




//3 2b2cg0am9e1lwc0nqikl2wczb
// Dict_t *agdictof(Agraph_t * g, int kind) 
public static ST_dt_s agdictof(ST_Agraph_s g, int kind) {
ENTERING("2b2cg0am9e1lwc0nqikl2wczb","agdictof");
try {
    ST_Agdatadict_s dd;
    ST_dt_s dict;
    dd = agdatadict(g, false);
    if (dd!=null)
	switch (kind) {
	case AGRAPH:
	    dict = (ST_dt_s) dd.dict_g;
	    break;
	case AGNODE:
	    dict = (ST_dt_s) dd.dict_n;
	    break;
	case AGINEDGE:
	case AGOUTEDGE:
	    dict = (ST_dt_s) dd.dict_e;
	    break;
	default:
	    System.err.println("agdictof: unknown kind "+ kind);
	    dict = null;
	    throw new UnsupportedOperationException();
    } else
	dict = null;
    return dict;
} finally {
LEAVING("2b2cg0am9e1lwc0nqikl2wczb","agdictof");
}
}




//3 dbhw2q2jfsz9qwawchy0hxj4i
// Agsym_t *agnewsym(Agraph_t * g, char *name, char *value, int id, int kind) 
public static ST_Agsym_s agnewsym(ST_Agraph_s g, CString name, CString value, int id, int kind) {
ENTERING("dbhw2q2jfsz9qwawchy0hxj4i","agnewsym");
try {
    ST_Agsym_s sym;
    sym = (ST_Agsym_s) agalloc(g, sizeof(ST_Agsym_s.class));
    sym.setInt("kind", kind);
    sym.setPtr("name", agstrdup(g, name));
    sym.setPtr("defval", agstrdup(g, value));
    sym.setInt("id", id);
    return sym;
} finally {
LEAVING("dbhw2q2jfsz9qwawchy0hxj4i","agnewsym");
}
}




//3 5s4tpjeh3jwf722izjq6cm6rq
// static void agcopydict(Dict_t * src, Dict_t * dest, Agraph_t * g, int kind) 
public static Object agcopydict(Object... arg) {
UNSUPPORTED("5x2q9spbx7y0k6l59z6oy8cuc"); // static void agcopydict(Dict_t * src, Dict_t * dest, Agraph_t * g, int kind)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1y11aotql5lts9njnreyq9t6r"); //     Agsym_t *sym, *newsym;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5wsq1ipnwwy8yumrluztmye7f"); //     for (sym = (Agsym_t *) (*(((Dt_t*)(src))->searchf))((src),(void*)(0),0000200); sym;
UNSUPPORTED("aqq7o7hh4nwqvvtcdqetmidkg"); // 	 sym = (Agsym_t *) (*(((Dt_t*)(src))->searchf))((src),(void*)(sym),0000010)) {
UNSUPPORTED("2astc0oxlvew45mitrflnx0ar"); // 	newsym = agnewsym(g, sym->name, sym->defval, sym->id, kind);
UNSUPPORTED("36os9hvg0e59rrhe68di2b5r3"); // 	newsym->print = sym->print;
UNSUPPORTED("77sre49xpjwlyjqhek659u3tq"); // 	newsym->fixed = sym->fixed;
UNSUPPORTED("3rc58hrkem2xey59d9ptss1sx"); // 	(*(((Dt_t*)(dest))->searchf))((dest),(void*)(newsym),0000001);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 alb1d5x6huba3q44ms1wt0rr5
// static Agdatadict_t *agmakedatadict(Agraph_t * g) 
public static ST_Agdatadict_s agmakedatadict(ST_Agraph_s g) {
ENTERING("alb1d5x6huba3q44ms1wt0rr5","agmakedatadict");
try {
    ST_Agraph_s par;
    ST_Agdatadict_s parent_dd, dd;
    dd = (ST_Agdatadict_s) agbindrec(g, DataDictName, sizeof(ST_Agdatadict_s.class),
				    false).castTo(ST_Agdatadict_s.class);
    dd.setPtr("dict.n", agdtopen(g, Z.z().AgDataDictDisc, Z.z().Dttree));
    dd.setPtr("dict.e", agdtopen(g, Z.z().AgDataDictDisc,Z.z(). Dttree));
    dd.setPtr("dict.g", agdtopen(g, Z.z().AgDataDictDisc, Z.z().Dttree));
    if ((par = agparent(g))!=null) {
	parent_dd = agdatadict(par, false);
	dtview((ST_dt_s)dd.dict_n, (ST_dt_s)parent_dd.dict_n);
	dtview((ST_dt_s)dd.dict_e, (ST_dt_s)parent_dd.dict_e);
	dtview((ST_dt_s)dd.dict_g, (ST_dt_s)parent_dd.dict_g);
    } else {
	if (Z.z().ProtoGraph!=null && NEQ(g, Z.z().ProtoGraph)) {
	    /* it's not ok to dtview here for several reasons. the proto
	       graph could change, and the sym indices don't match */
	    parent_dd = agdatadict(Z.z().ProtoGraph, false);
	    agcopydict(parent_dd.dict_n, dd.dict_n, g, AGNODE);
	    agcopydict(parent_dd.dict_e, dd.dict_e, g, AGEDGE);
	    agcopydict(parent_dd.dict_g, dd.dict_g, g, AGRAPH);
	}
    }
    return dd;
} finally {
LEAVING("alb1d5x6huba3q44ms1wt0rr5","agmakedatadict");
}
}




//3 50wfzq5wy8wc7vuyvs3mrx5ct
// Agsym_t *agdictsym(Dict_t * dict, char *name) 
public static ST_Agsym_s agdictsym(ST_dt_s dict, CString name) {
ENTERING("50wfzq5wy8wc7vuyvs3mrx5ct","agdictsym");
try {
    ST_Agsym_s key = (ST_Agsym_s) Memory.malloc(ST_Agsym_s.class);
    key.setPtr("name", name);
    return  (ST_Agsym_s) dict.searchf.exe((dict),key,0000004);
} finally {
LEAVING("50wfzq5wy8wc7vuyvs3mrx5ct","agdictsym");
}
}




//3 4wy4ggu70d7harhix8xnh5w4l
// Agsym_t *aglocaldictsym(Dict_t * dict, char *name) 
public static ST_Agsym_s aglocaldictsym(ST_dt_s dict, CString name) {
ENTERING("4wy4ggu70d7harhix8xnh5w4l","aglocaldictsym");
try {
    ST_Agsym_s rv;
    ST_dt_s view;
    view = dtview(dict, null);
    rv = agdictsym(dict, name);
    dtview(dict, view);
    return rv;
} finally {
LEAVING("4wy4ggu70d7harhix8xnh5w4l","aglocaldictsym");
}
}




//3 8hy9sl3zmwobwm960jz466ufe
// Agsym_t *agattrsym(void *obj, char *name) 
public static ST_Agsym_s agattrsym(__ptr__ obj, CString name) {
ENTERING("8hy9sl3zmwobwm960jz466ufe","agattrsym");
try {
	ST_Agattr_s data;
    ST_Agsym_s rv;
    CString arg = name;
    data = agattrrec(obj.castTo(ST_Agobj_s.class));
    if (data!=null)
	rv = agdictsym((ST_dt_s)data.dict, arg);
    else
	rv = null;
    return rv;
} finally {
LEAVING("8hy9sl3zmwobwm960jz466ufe","agattrsym");
}
}


//1 covyqfvabl7igx9g5rvlhgngz
// char *AgDataRecName = 
public final static CString AgDataRecName = new CString("_AG_strdata");



//3 6az8xu0sgu1d6abu0xfpd89hi
// static int topdictsize(Agobj_t * obj) 
public static int topdictsize(ST_Agobj_s obj) {
ENTERING("6az8xu0sgu1d6abu0xfpd89hi","topdictsize");
try {
    ST_dt_s d;
    d = agdictof(agroot(agraphof(obj)), AGTYPE(obj));
    return d!=null ? dtsize_(d) : 0;
} finally {
LEAVING("6az8xu0sgu1d6abu0xfpd89hi","topdictsize");
}
}




//3 3wjrlyjdlz8k9nfxenxsfiqmj
// static Agrec_t *agmakeattrs(Agraph_t * context, void *obj) 
public static ST_Agrec_s agmakeattrs(ST_Agraph_s context, __ptr__ obj) {
ENTERING("3wjrlyjdlz8k9nfxenxsfiqmj","agmakeattrs");
try {
    int sz;
    ST_Agattr_s rec;
    ST_Agsym_s sym;
    ST_dt_s datadict;
    rec = (ST_Agattr_s) agbindrec(obj, AgDataRecName, sizeof(ST_Agattr_s.class), false).castTo(ST_Agattr_s.class);
    datadict = agdictof(context, AGTYPE(obj));


    if (rec.dict == null) {
	rec.dict = agdictof(agroot(context), AGTYPE(obj));
	/* don't malloc(0) */
	sz = topdictsize((ST_Agobj_s) obj.castTo(ST_Agobj_s.class));
	if (sz < 4)
	    sz = 4;
	rec.str = new ArrayList<CString>(); for (int i=0; i<sz; i++) rec.str.add(null);
	/* doesn't call agxset() so no obj-modified callbacks occur */
	for (sym = (ST_Agsym_s) ((__ptr__)datadict.searchf.exe(datadict,null,0000200)); sym!=null;
	     sym = (ST_Agsym_s) ((__ptr__)datadict.searchf.exe(datadict,sym,0000010)))
	    rec.str.set(sym.id, agstrdup(agraphof(obj), sym.defval));
    } else {
    }
    return (ST_Agrec_s) rec.castTo(ST_Agrec_s.class);
} finally {
LEAVING("3wjrlyjdlz8k9nfxenxsfiqmj","agmakeattrs");
}
}




//3 50md6kgbmmjiwsq00tdvtqrom
// static void freeattr(Agobj_t * obj, Agattr_t * attr) 
public static Object freeattr(Object... arg) {
UNSUPPORTED("b7w7ts75503jg2pb69wdgviy6"); // static void freeattr(Agobj_t * obj, Agattr_t * attr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("22kc1nt4b0bllq26fmw8ytgp2"); //     int i, sz;
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("8pfuk9ua4x9bh68zk1kzwc5t9"); //     g = agraphof(obj);
UNSUPPORTED("65sc7rva5ncw0awo92jglve6w"); //     sz = topdictsize(obj);
UNSUPPORTED("cl9683xent4h3vyf21r0ct6a1"); //     for (i = 0; i < sz; i++)
UNSUPPORTED("4so817anjuwmq6rj2hjrk4wlx"); // 	agstrfree(g, attr->str[i]);
UNSUPPORTED("5t1oinkxblld9xyoaz469i6dz"); //     agfree(g, attr->str);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6b3c2syvj23lbf7kz0aal8vmc
// static void freesym(Dict_t * d, void * obj, Dtdisc_t * disc) 
public static void freesym(ST_dt_s d, Object obj, ST_dtdisc_s disc) {
ENTERING("6b3c2syvj23lbf7kz0aal8vmc","freesym");
try {
	UNSUPPORTED("bf9av4xbx61835st3og3wfqr8"); // static void freesym(Dict_t * d, void * obj, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("av59ae8c4mws46sf5dumz1k0s"); //     Agsym_t *sym;
UNSUPPORTED("6z29omss9ay00bqf6xael7t6t"); //     (void) d;
UNSUPPORTED("3wnut6i7v07q7n0fa538dyp7a"); //     sym = (Agsym_t *) obj;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("aka2fcwk9snppcvd59dzewx38"); //     agstrfree(Ag_G_global, sym->name);
UNSUPPORTED("89enhovojps3wt6zo6s4xqlu7"); //     agstrfree(Ag_G_global, sym->defval);
UNSUPPORTED("2ttw1xr5hp7fxq8w5bofu7cg9"); //     agfree(Ag_G_global, sym);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("6b3c2syvj23lbf7kz0aal8vmc","freesym");
}
}




//3 13sfx74lme08ur04vkrqta25j
// Agattr_t *agattrrec(void *obj) 
public static ST_Agattr_s agattrrec(__ptr__ obj) {
ENTERING("13sfx74lme08ur04vkrqta25j","agattrrec");
try {
    return (ST_Agattr_s) aggetrec(obj, AgDataRecName, false).castTo(ST_Agattr_s.class);
} finally {
LEAVING("13sfx74lme08ur04vkrqta25j","agattrrec");
}
}




//3 2io7b26wq70e7kwdlzsh6bw7f
// static void addattr(Agraph_t * g, Agobj_t * obj, Agsym_t * sym) 
public static void addattr(ST_Agraph_s g, ST_Agobj_s obj, ST_Agsym_s sym) {
ENTERING("2io7b26wq70e7kwdlzsh6bw7f","addattr");
try {
	ST_Agattr_s attr;
    attr = (ST_Agattr_s) agattrrec(obj);

    if (sym.id >= 4)
//	attr.str = (StarArrayOfCString) g.clos.disc.mem.call("resize", g.clos.state.mem,
//						     attr.str,
//						     sizeof("char*", sym.id),
//						     sizeof("char*", sym.id + 1));
    attr.str.add(null);
	attr.str.set(sym.id, agstrdup(g, sym.defval));
    /* agmethod_upd(g,obj,sym);  JCE and GN didn't like this. */
} finally {
LEAVING("2io7b26wq70e7kwdlzsh6bw7f","addattr");
}
}




//3 a3qr8ug1rkxp6ocieyp41ly3o
// static Agsym_t *setattr(Agraph_t * g, int kind, char *name, char *value) 
public static ST_Agsym_s setattr(ST_Agraph_s g, int kind, CString name, CString value) {
ENTERING("a3qr8ug1rkxp6ocieyp41ly3o","setattr");
try {
    ST_Agdatadict_s dd;
    ST_dt_s ldict, rdict;
    ST_Agsym_s lsym, psym, rsym, rv;
    ST_Agraph_s root;
    ST_Agnode_s n;
    ST_Agedge_s e;
   
    root = agroot(g);
    dd = agdatadict(g, (N(0)));	/* force initialization of string attributes */
    ldict = agdictof(g, kind);
    lsym = aglocaldictsym(ldict, name);
    if (lsym!=null) {			/* update old local definiton */
	agstrfree(g, lsym.defval);
	lsym.setPtr("defval", agstrdup(g, value));
	rv = lsym;
    } else {
	psym = agdictsym(ldict, name);	/* search with viewpath up to root */
	if (psym!=null) {		/* new local definition */
	    lsym = agnewsym(g, name, value, psym.id, kind);
	    ldict.searchf.exe(ldict,lsym,0000001);
	    rv = lsym;
	} else {		/* new global definition */
	    rdict = agdictof(root, kind);
	    rsym = agnewsym(g, name, value, dtsize_(rdict), kind);
	    rdict.searchf.exe(rdict,rsym,0000001);
	    switch (kind) {
	    case AGRAPH:
		agapply(root, (ST_Agobj_s) root.castTo(ST_Agobj_s.class), function(attr__c.class, "addattr"),
			rsym, (N(0)));
		break;
	    case AGNODE:
		for (n = agfstnode(root); n!=null; n = agnxtnode(root, n))
		    addattr(g, (ST_Agobj_s) n.castTo(ST_Agobj_s.class), rsym);
		break;
	    case AGINEDGE:
	    case AGOUTEDGE:
		for (n = agfstnode(root); n!=null; n = agnxtnode(root, n))
		    for (e = agfstout(root, n); e!=null; e = agnxtout(root, e))
			addattr(g, (ST_Agobj_s) e.castTo(ST_Agobj_s.class), rsym);
		break;
	    }
	    rv = rsym;
	}
    }
    if (rv!=null && (kind == 0))
	agxset(g, rv, value);
    agmethod_upd(g, g, rv);	/* JCE and GN wanted this */
    return rv;
} finally {
LEAVING("a3qr8ug1rkxp6ocieyp41ly3o","setattr");
}
}




//3 8f80aahwb8cqc2t9592v47ttd
// static Agsym_t *getattr(Agraph_t * g, int kind, char *name) 
public static ST_Agsym_s getattr(ST_Agraph_s g, int kind, CString name) {
ENTERING("8f80aahwb8cqc2t9592v47ttd","getattr");
try {
    ST_Agsym_s rv = null;
    ST_dt_s dict;
    dict = agdictof(g, kind);
    if (dict!=null)
	rv = agdictsym(dict, name);	/* viewpath up to root */
    return rv;
} finally {
LEAVING("8f80aahwb8cqc2t9592v47ttd","getattr");
}
}




//3 blr3drm2hxuzwd6gpeeb84yyg
// Agsym_t *agattr(Agraph_t * g, int kind, char *name, char *value) 
public static ST_Agsym_s agattr(ST_Agraph_s g, int kind, CString name, CString value) {
ENTERING("blr3drm2hxuzwd6gpeeb84yyg","agattr");
try {
    ST_Agsym_s rv;
    if (g == null) {
	if (Z.z().ProtoGraph == null)
	    Z.z().ProtoGraph = agopen(null, (ST_Agdesc_s) Z.z().ProtoDesc.copy(), null);
	g = Z.z().ProtoGraph;
    }
    if (value!=null)
	rv = setattr(g, kind, name, value);
    else
	rv = getattr(g, kind, name);
    return rv;
} finally {
LEAVING("blr3drm2hxuzwd6gpeeb84yyg","agattr");
}
}




//3 9medmidrd61ljmzlswpxwuxjm
// Agsym_t *agnxtattr(Agraph_t * g, int kind, Agsym_t * attr) 
public static Object agnxtattr(Object... arg) {
UNSUPPORTED("1nquny99ik5rdqpdzl1efzzw5"); // Agsym_t *agnxtattr(Agraph_t * g, int kind, Agsym_t * attr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("l4y6zpshfefue2m18wlswfkp"); //     Dict_t *d;
UNSUPPORTED("ekzntpxwtsedcg8uzmlsfbb5h"); //     Agsym_t *rv;
UNSUPPORTED("786h6jfmnfzp39fkdwa7fzz71"); //     if ((d = agdictof(g, kind))) {
UNSUPPORTED("4ftgdafqwlor792t6qye23xle"); // 	if (attr)
UNSUPPORTED("ealluogofs9ig6hmoveplut40"); // 	    rv = (Agsym_t *) (*(((Dt_t*)(d))->searchf))((d),(void*)(attr),0000010);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("8yv728l38cvhrasbkgvce3bsb"); // 	    rv = (Agsym_t *) (*(((Dt_t*)(d))->searchf))((d),(void*)(0),0000200);
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("40zoypg37zonwb7nuewxdti29"); // 	rv = 0;
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ex2qtmuwz0o2e5svkujdbux78
// void agraphattr_init(Agraph_t * g) 
public static void agraphattr_init(ST_Agraph_s g) {
ENTERING("ex2qtmuwz0o2e5svkujdbux78","agraphattr_init");
try {
    /* Agdatadict_t *dd; */
    /* Agrec_t                      *attr; */
    ST_Agraph_s context;
    ((ST_Agdesc_s)g.desc).has_attrs = 1;
    /* dd = */ agmakedatadict(g);
    if (N(context = agparent(g)))
	context = g;
    /* attr = */ agmakeattrs(context, g);
} finally {
LEAVING("ex2qtmuwz0o2e5svkujdbux78","agraphattr_init");
}
}




//3 ccqww3yvujvy82bw6i1m0rv96
// int agraphattr_delete(Agraph_t * g) 
public static Object agraphattr_delete(Object... arg) {
UNSUPPORTED("4chc6o30l6nehselkvpo4xtao"); // int agraphattr_delete(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eq0gq1d7pmvj94j5nxbexze8p"); //     Agdatadict_t *dd;
UNSUPPORTED("4e7iq4f4pk0x2v3zvdc2tdbd1"); //     Agattr_t *attr;
UNSUPPORTED("6qvjz1ziwr3nwocahqvfzw14t"); //     Ag_G_global = g;
UNSUPPORTED("4t3qv06n62hquaihi33zqc8bi"); //     if ((attr = agattrrec(g))) {
UNSUPPORTED("31flnlx452d07y9bjvvk0nygk"); // 	freeattr((Agobj_t *) g, attr);
UNSUPPORTED("ag6tzein6gkpxcarnafbxws91"); // 	agdelrec(g, attr->h.name);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4p9dsuje96h48qnpgn0m02aij"); //     if ((dd = agdatadict(g, (0)))) {
UNSUPPORTED("6ubrug572yhktadcj02muyr5e"); // 	if (agdtclose(g, dd->dict.n)) return 1;
UNSUPPORTED("3msb0eqyxef8ye31vjnzxbkh6"); // 	if (agdtclose(g, dd->dict.e)) return 1;
UNSUPPORTED("597gu9wfswglr8tbdrbqpr27p"); // 	if (agdtclose(g, dd->dict.g)) return 1;
UNSUPPORTED("dhn07bfi40opq2qmakfkosz77"); // 	agdelrec(g, dd->h.name);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2pp4ot3pqy71jpfvu3g398y4y
// void agnodeattr_init(Agraph_t * g, Agnode_t * n) 
public static void agnodeattr_init(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("2pp4ot3pqy71jpfvu3g398y4y","agnodeattr_init");
try {
	ST_Agattr_s data;
    data = (ST_Agattr_s) agattrrec(n);
    if ((N(data)) || (N(data.dict)))
	agmakeattrs(g, n);
} finally {
LEAVING("2pp4ot3pqy71jpfvu3g398y4y","agnodeattr_init");
}
}




//3 7fbhjqpb2w17ahde2ie2l22n5
// void agnodeattr_delete(Agnode_t * n) 
public static Object agnodeattr_delete(Object... arg) {
UNSUPPORTED("72jlvkhc0kqxzf6infgwjj96n"); // void agnodeattr_delete(Agnode_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cxk0x5bmjhvgv8b1uv960lx4"); //     Agattr_t *rec;
UNSUPPORTED("96w8m9uhl8904g83fqzl5a0fl"); //     if ((rec = agattrrec(n))) {
UNSUPPORTED("34er8hi05mpmz6cld4lt2pzyd"); // 	freeattr((Agobj_t *) n, rec);
UNSUPPORTED("3vr5ktefaene3xr7wprole0q4"); // 	agdelrec(n, AgDataRecName);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ckfzmnxfi0jiiayxmq38giw0r
// void agedgeattr_init(Agraph_t * g, Agedge_t * e) 
public static void agedgeattr_init(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("ckfzmnxfi0jiiayxmq38giw0r","agedgeattr_init");
try {
	ST_Agattr_s data;
    data = agattrrec(e);
    if ((N(data)) || (N(data.dict)))
	agmakeattrs(g, e);
} finally {
LEAVING("ckfzmnxfi0jiiayxmq38giw0r","agedgeattr_init");
}
}




//3 3cm42f5o83187rwf4l7j7ie1k
// void agedgeattr_delete(Agedge_t * e) 
public static Object agedgeattr_delete(Object... arg) {
UNSUPPORTED("97nvxy8b8p4rmo31kk1qkusns"); // void agedgeattr_delete(Agedge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cxk0x5bmjhvgv8b1uv960lx4"); //     Agattr_t *rec;
UNSUPPORTED("5n6u4cfiymhx4a88tj9vpz5z8"); //     if ((rec = agattrrec(e))) {
UNSUPPORTED("1ct1lua1igkf5stpj1vcd2z0h"); // 	freeattr((Agobj_t *) e, rec);
UNSUPPORTED("67nly1q6jgpe4ozoidte7zl4v"); // 	agdelrec(e, AgDataRecName);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eydjyhexv5jr6vi7uhk0cgphv
// char *agget(void *obj, char *name) 
public static CString agget(__ptr__ obj, CString name) {
ENTERING("eydjyhexv5jr6vi7uhk0cgphv","agget");
try {
    ST_Agsym_s sym;
    ST_Agattr_s data;
    CString rv = null;
    sym = agattrsym(obj, name);
    if (sym == null)
	rv = null;			/* note was "", but this provides more info */
    else {
	data = agattrrec(obj.castTo(ST_Agobj_s.class));
	rv = data.str.get(sym.id);
    }
    return rv;
} finally {
LEAVING("eydjyhexv5jr6vi7uhk0cgphv","agget");
}
}




//3 9h5oymhfkp6k34zl0fonn10k9
// char *agxget(void *obj, Agsym_t * sym) 
public static CString agxget(__ptr__ obj,  ST_Agsym_s sym) {
ENTERING("9h5oymhfkp6k34zl0fonn10k9","agxget");
try {
	ST_Agattr_s data;
    CString rv;
    data = agattrrec(obj.castTo(ST_Agobj_s.class));

    rv = data.str.get(sym.id);
    return rv;
} finally {
LEAVING("9h5oymhfkp6k34zl0fonn10k9","agxget");
}
}




//3 alc2i3vy4lm57qoc7qn69ppgr
// int agset(void *obj, char *name, char *value) 
public static Object agset(Object... arg) {
UNSUPPORTED("dw46ysqbvbb1syq3h2su8khpt"); // int agset(void *obj, char *name, char *value)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("av59ae8c4mws46sf5dumz1k0s"); //     Agsym_t *sym;
UNSUPPORTED("ecz4e03zumggc8tfymqvirexq"); //     int rv;
UNSUPPORTED("a5zpawq5lfz2h4k7xzaf5qtcl"); //     sym = agattrsym(obj, name);
UNSUPPORTED("4htetkykqre1tnc0i1ksxdqmx"); //     if (sym == ((Agsym_t*)0))
UNSUPPORTED("71xur63dbv1df4fp5xw1bru2i"); // 	rv = -1;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("932tqrc73k3hieetx14jtfgk0"); // 	rv = agxset(obj, sym, value);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4q8xvstvl6fbijhucfd1endxc
// int agxset(void *obj, Agsym_t * sym, char *value) 
public static int agxset(__ptr__ obj, ST_Agsym_s sym, CString value) {
ENTERING("4q8xvstvl6fbijhucfd1endxc","agxset");
try {
    ST_Agraph_s g;
    ST_Agobj_s hdr;
    ST_Agattr_s data;
    ST_Agsym_s lsym;
    g = agraphof(obj);
    hdr = (ST_Agobj_s) obj.castTo(ST_Agobj_s.class);
    data = agattrrec(hdr);

    agstrfree(g, data.str.get(sym.id));
    data.str.set(sym.id, agstrdup(g, value));
    if (((ST_Agtag_s)hdr.tag).objtype == AGRAPH) {
	/* also update dict default */
	ST_dt_s dict;
	dict = (ST_dt_s) agdatadict(g, false).dict_g;
	if ((lsym = aglocaldictsym(dict, sym.name))!=null) {
	    agstrfree(g, lsym.defval);
	    lsym.setPtr("defval", agstrdup(g, value));
	} else {
	    lsym = agnewsym(g, sym.name, value, sym.id, AGTYPE(hdr));
	    dict.searchf.exe(dict, lsym, 0000001);
	}
    }
    agmethod_upd(g, obj, sym);
    return 0;
} finally {
LEAVING("4q8xvstvl6fbijhucfd1endxc","agxset");
}
}




//3 9b7vn95cin8o7mb2f21exh1qr
// int agsafeset(void *obj, char *name, char *value, char *def) 
public static int agsafeset(__ptr__ obj, CString name, CString value, CString def) {
ENTERING("9b7vn95cin8o7mb2f21exh1qr","agsafeset");
try {
    ST_Agsym_s a;
    a = agattr(agraphof(obj), AGTYPE(obj), name, null);
    if (N(a))
	a = agattr(agraphof(obj), AGTYPE(obj), name, def);
    return agxset(obj, a, value);
} finally {
LEAVING("9b7vn95cin8o7mb2f21exh1qr","agsafeset");
}
}




//3 6gjlgo4s6r0bu7gjazfee6qv8
// static void init_all_attrs(Agraph_t * g) 
public static Object init_all_attrs(Object... arg) {
UNSUPPORTED("bir8xur87cl8inhyrgimkboqq"); // static void init_all_attrs(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2hs0004nnparj6tt7elslt4zj"); //     Agraph_t *root;
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("36vshotvjkc5iodgg7nq6qa2r"); //     Agedge_t *e;
UNSUPPORTED("bnybowlbrgjx7x160vaxt6eok"); //     root = agroot(g);
UNSUPPORTED("et7b6czk7vef12o9ct0zlil6b"); //     agapply(root, (Agobj_t *) root, (agobjfn_t) agraphattr_init,
UNSUPPORTED("a1lzdxitcghqk0l63zsudijhj"); // 	    ((Agdisc_t *)0), (!(0)));
UNSUPPORTED("8uyptmsbxy8cnb2yc5e1zjy93"); //     for (n = agfstnode(root); n; n = agnxtnode(root, n)) {
UNSUPPORTED("149yfxxc7rsqn6goirepjpcf2"); // 	agnodeattr_init(g, n);
UNSUPPORTED("1qcg6rx46tkna823mgf1786us"); // 	for (e = agfstout(root, n); e; e = agnxtout(root, e)) {
UNSUPPORTED("555dsk5o3bvij5uz01q0cs74w"); // 	    agedgeattr_init(g, e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2fmwbgdlz116bdnivfr2gbst7
// int agcopyattr(void *oldobj, void *newobj) 
public static Object agcopyattr(Object... arg) {
UNSUPPORTED("9hxxxad2s159e1mpaqdq32p1j"); // int agcopyattr(void *oldobj, void *newobj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("av59ae8c4mws46sf5dumz1k0s"); //     Agsym_t *sym;
UNSUPPORTED("e0it3jbt5zhk1nsy4jog99dpu"); //     Agsym_t *newsym;
UNSUPPORTED("6bpbm2s85uk2e5o6cm49boc4w"); //     char* val;
UNSUPPORTED("85pgv5dfzc1lewmaoiherq9nf"); //     char* nval;
UNSUPPORTED("4lyqrf8bj8y0fbafzxuae0xqa"); //     int r = 1;
UNSUPPORTED("enr7x1oa4c50ne66ts6hvz0dx"); //     g = agraphof(oldobj);
UNSUPPORTED("6qj4ytcakj7t70xil9hxyy5c9"); //     if (AGTYPE(oldobj) != AGTYPE(newobj))
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("5btwvtgpjz0hx5y9ouikwsrc4"); //     sym = 0;
UNSUPPORTED("886wv340pnju68k6aejj7o3x0"); //     while ((sym = agnxtattr(g, AGTYPE(oldobj), sym))) {
UNSUPPORTED("cino0o0155s1h3gsr2l2uz7z"); // 	newsym = agattrsym(newobj, sym->name);
UNSUPPORTED("daq5urcl5ihipluxk5sf4ccdx"); // 	if (!newsym)
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
UNSUPPORTED("aao8dsiq1evom55a3f4w4rha3"); // 	val = agxget(oldobj, sym);
UNSUPPORTED("a078bakz1z0utl11856f4vk7w"); // 	r = agxset(newobj, newsym, val);
UNSUPPORTED("cpxrqzz637g381bs344b2sj0u"); // 	/* FIX(?): Each graph has its own string cache, so a whole new refstr is possibly
UNSUPPORTED("d41ayxc51x2hyvj9eo15qmgun"); // 	 * allocated. If the original was an html string, make sure the new one is as well.
UNSUPPORTED("3kp67yo7cfslm3l0o4e5cdnd8"); // 	 * If cgraph goes to single string table, this can be removed.
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("ez0g3o1tj7ommhxsolcr2gsr"); // 	if (aghtmlstr (val)) {
UNSUPPORTED("do57srwwkvwu30y5dd2pr6tkp"); // 	    nval = agxget (newobj, newsym);
UNSUPPORTED("9nzu0ckgyrqklwagcbye9wtx9"); // 	    agmarkhtmlstr (nval);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
