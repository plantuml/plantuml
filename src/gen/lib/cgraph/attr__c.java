/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.dtinsert;
import static smetana.core.Macro.dtsearch;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import java.util.ArrayList;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
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
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.ZType;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class attr__c {


private static final int MINATTR = 4;


//1 67blfrj9x850g8ccpo9qjer2
// static char DataDictName[] = 
public final static CString DataDictName = new CString("_AG_datadict");


@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agdatadict", key="4bm10isw1qq1eqcse8afbxee3", definition="Agdatadict_t *agdatadict(Agraph_t * g, int cflag)")
public static ST_Agdatadict_s agdatadict(ST_Agraph_s g, boolean cflag) {
ENTERING("4bm10isw1qq1eqcse8afbxee3","agdatadict");
try {
    ST_Agdatadict_s rv;
    rv = (ST_Agdatadict_s) aggetrec(g, DataDictName, false);
    if (rv!=null || !cflag)
	return rv;
    init_all_attrs(g);
    rv = (ST_Agdatadict_s) aggetrec(g, DataDictName, false);
    return rv;
} finally {
LEAVING("4bm10isw1qq1eqcse8afbxee3","agdatadict");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agdictof", key="2b2cg0am9e1lwc0nqikl2wczb", definition="Dict_t *agdictof(Agraph_t * g, int kind)")
public static ST_dt_s agdictof(ST_Agraph_s g, int kind) {
ENTERING("2b2cg0am9e1lwc0nqikl2wczb","agdictof");
try {
    ST_Agdatadict_s dd;
    ST_dt_s dict;
    dd = agdatadict(g, false);
    if (dd!=null)
	switch (kind) {
	case AGRAPH:
	    dict = dd.dict_g;
	    break;
	case AGNODE:
	    dict = dd.dict_n;
	    break;
	case AGINEDGE:
	case AGOUTEDGE:
	    dict = dd.dict_e;
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




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agnewsym", key="dbhw2q2jfsz9qwawchy0hxj4i", definition="Agsym_t *agnewsym(Agraph_t * g, char *name, char *value, int id, int kind)")
public static ST_Agsym_s agnewsym(Globals zz, ST_Agraph_s g, CString name, CString value, int id, int kind) {
ENTERING("dbhw2q2jfsz9qwawchy0hxj4i","agnewsym");
try {
    ST_Agsym_s sym;
    sym = (ST_Agsym_s) agalloc(g, new size_t(ZType.ST_Agsym_s));
    sym.kind = kind;
    sym.name = agstrdup(zz, g, name);
    sym.defval = agstrdup(zz, g, value);
    sym.id = id;
    return sym;
} finally {
LEAVING("dbhw2q2jfsz9qwawchy0hxj4i","agnewsym");
}
}




//3 5s4tpjeh3jwf722izjq6cm6rq
// static void agcopydict(Dict_t * src, Dict_t * dest, Agraph_t * g, int kind) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agcopydict", key="5s4tpjeh3jwf722izjq6cm6rq", definition="static void agcopydict(Dict_t * src, Dict_t * dest, Agraph_t * g, int kind)")
public static Object agcopydict(Object... arg_) {
UNSUPPORTED("5x2q9spbx7y0k6l59z6oy8cuc"); // static void agcopydict(Dict_t * src, Dict_t * dest, Agraph_t * g, int kind)

throw new UnsupportedOperationException();
}




@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agmakedatadict", key="alb1d5x6huba3q44ms1wt0rr5", definition="static Agdatadict_t *agmakedatadict(Agraph_t * g)")
public static ST_Agdatadict_s agmakedatadict(Globals zz, ST_Agraph_s g) {
ENTERING("alb1d5x6huba3q44ms1wt0rr5","agmakedatadict");
try {
    ST_Agraph_s par;
    ST_Agdatadict_s parent_dd, dd;
    dd = (ST_Agdatadict_s) agbindrec(zz, g, DataDictName, new size_t(ZType.ST_Agdatadict_s),
				    false);
    dd.dict_n = agdtopen(zz, g, zz.AgDataDictDisc, zz.Dttree);
    dd.dict_e = agdtopen(zz, g, zz.AgDataDictDisc,zz. Dttree);
    dd.dict_g = agdtopen(zz, g, zz.AgDataDictDisc, zz.Dttree);
    if ((par = agparent(g))!=null) {
	parent_dd = agdatadict(par, false);
	dtview((ST_dt_s)dd.dict_n, (ST_dt_s)parent_dd.dict_n);
	dtview((ST_dt_s)dd.dict_e, (ST_dt_s)parent_dd.dict_e);
	dtview((ST_dt_s)dd.dict_g, (ST_dt_s)parent_dd.dict_g);
    } else {
	if (zz.ProtoGraph!=null && (g != zz.ProtoGraph)) {
	    /* it's not ok to dtview here for several reasons. the proto
	       graph could change, and the sym indices don't match */
	    parent_dd = agdatadict(zz.ProtoGraph, false);
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




@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agdictsym", key="50wfzq5wy8wc7vuyvs3mrx5ct", definition="Agsym_t *agdictsym(Dict_t * dict, char *name)")
public static ST_Agsym_s agdictsym(Globals zz, ST_dt_s dict, CString name) {
ENTERING("50wfzq5wy8wc7vuyvs3mrx5ct","agdictsym");
try {
    final ST_Agsym_s key = new ST_Agsym_s();
    key.name = name;
    return  (ST_Agsym_s) dtsearch(zz, dict, key);
} finally {
LEAVING("50wfzq5wy8wc7vuyvs3mrx5ct","agdictsym");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="aglocaldictsym", key="4wy4ggu70d7harhix8xnh5w4l", definition="Agsym_t *aglocaldictsym(Dict_t * dict, char *name)")
public static ST_Agsym_s aglocaldictsym(Globals zz, ST_dt_s dict, CString name) {
ENTERING("4wy4ggu70d7harhix8xnh5w4l","aglocaldictsym");
try {
    ST_Agsym_s rv;
    ST_dt_s view;
    view = dtview(dict, null);
    rv = agdictsym(zz, dict, name);
    dtview(dict, view);
    return rv;
} finally {
LEAVING("4wy4ggu70d7harhix8xnh5w4l","aglocaldictsym");
}
}



@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agattrsym", key="8hy9sl3zmwobwm960jz466ufe", definition="Agsym_t *agattrsym(void *obj, char *name)")
public static ST_Agsym_s agattrsym(Globals zz, __ptr__ obj, CString name) {
ENTERING("8hy9sl3zmwobwm960jz466ufe","agattrsym");
try {
	ST_Agattr_s data;
    ST_Agsym_s rv;
    CString arg = name;
    
    data = agattrrec(obj);
    if (data!=null)
	rv = agdictsym(zz, (ST_dt_s)data.dict, arg);
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



@Original(version="2.38.0", path="lib/cgraph/attr.c", name="topdictsize", key="6az8xu0sgu1d6abu0xfpd89hi", definition="static int topdictsize(Agobj_t * obj)")
public static int topdictsize(ST_Agobj_s obj) {
ENTERING("6az8xu0sgu1d6abu0xfpd89hi","topdictsize");
try {
    ST_dt_s d;
    d = agdictof(agroot(agraphof(obj)), obj.tag.objtype);
    return d!=null ? dtsize_(d) : 0;
} finally {
LEAVING("6az8xu0sgu1d6abu0xfpd89hi","topdictsize");
}
}




@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agmakeattrs", key="3wjrlyjdlz8k9nfxenxsfiqmj", definition="static Agrec_t *agmakeattrs(Agraph_t * context, void *obj)")
public static ST_Agrec_s agmakeattrs(Globals zz, ST_Agraph_s context, ST_Agobj_s obj) {
ENTERING("3wjrlyjdlz8k9nfxenxsfiqmj","agmakeattrs");
try {
    int sz;
    ST_Agattr_s rec;
    ST_Agsym_s sym;
    ST_dt_s datadict;
    rec = (ST_Agattr_s) agbindrec(zz, obj, AgDataRecName, new size_t(ZType.ST_Agattr_s), false);
    datadict = agdictof(context, obj.tag.objtype);


    if (rec.dict == null) {
	rec.dict = agdictof(agroot(context), obj.tag.objtype);
	/* don't malloc(0) */
	sz = topdictsize((ST_Agobj_s) obj);
	if (sz < 4)
	    sz = 4;
	rec.str = new ArrayList<CString>(); for (int i=0; i<sz; i++) rec.str.add(null);
	/* doesn't call agxset() so no obj-modified callbacks occur */
	for (sym = (ST_Agsym_s) ((__ptr__)datadict.searchf.exe(zz, datadict,null,0000200)); sym!=null;
	     sym = (ST_Agsym_s) ((__ptr__)datadict.searchf.exe(zz, datadict,sym,0000010)))
	    rec.str.set(sym.id, agstrdup(zz, agraphof(obj), sym.defval));
    } else {
    }
    return (ST_Agrec_s) rec;
} finally {
LEAVING("3wjrlyjdlz8k9nfxenxsfiqmj","agmakeattrs");
}
}


public static CFunction freesym = new CFunctionAbstract("freesym") {
	
	public Object exe(Globals zz, Object... args) {
		freesym((ST_dt_s)args[0], args[1], (ST_dtdisc_s)args[2]);
		return null;
	}};
	

@Unused
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="freesym", key="6b3c2syvj23lbf7kz0aal8vmc", definition="static void freesym(Dict_t * d, void * obj, Dtdisc_t * disc)")
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




@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agattrrec", key="13sfx74lme08ur04vkrqta25j", definition="Agattr_t *agattrrec(void *obj)")
public static ST_Agattr_s agattrrec(__ptr__ obj) {
ENTERING("13sfx74lme08ur04vkrqta25j","agattrrec");
try {
    return (ST_Agattr_s) aggetrec(obj, AgDataRecName, false);
} finally {
LEAVING("13sfx74lme08ur04vkrqta25j","agattrrec");
}
}

public static CFunction addattr = new CFunctionAbstract("addattr") {
	
	public Object exe(Globals zz, Object... args) {
		addattr(zz, (ST_Agraph_s)args[0], (ST_Agobj_s)args[1], (ST_Agsym_s)args[2]);
		return null;
	}};
@Reviewed(when = "12/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="addattr", key="2io7b26wq70e7kwdlzsh6bw7f", definition="static void addattr(Agraph_t * g, Agobj_t * obj, Agsym_t * sym)")
public static void addattr(Globals zz, ST_Agraph_s g, ST_Agobj_s obj, ST_Agsym_s sym) {
ENTERING("2io7b26wq70e7kwdlzsh6bw7f","addattr");
try {
	ST_Agattr_s attr;
    attr = (ST_Agattr_s) agattrrec(obj);

    if (sym.id >= MINATTR)
//	attr.str = (StarArrayOfCString) g.clos.disc.mem.call("resize", g.clos.state.mem,
//						     attr.str,
//						     sizeof("char*", sym.id),
//						     sizeof("char*", sym.id + 1));
    attr.str.add(null);
	attr.str.set(sym.id, agstrdup(zz, g, sym.defval));
    /* agmethod_upd(g,obj,sym);  JCE and GN didn't like this. */
} finally {
LEAVING("2io7b26wq70e7kwdlzsh6bw7f","addattr");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="setattr", key="a3qr8ug1rkxp6ocieyp41ly3o", definition="static Agsym_t *setattr(Agraph_t * g, int kind, char *name, char *value)")
public static ST_Agsym_s setattr(Globals zz, ST_Agraph_s g, int kind, CString name, CString value) {
ENTERING("a3qr8ug1rkxp6ocieyp41ly3o","setattr");
try {
    ST_Agdatadict_s dd;
    ST_dt_s ldict, rdict;
    ST_Agsym_s lsym, psym, rsym, rv;
    ST_Agraph_s root;
    ST_Agnode_s n;
    ST_Agedge_s e;
   
    root = agroot(g);
    dd = agdatadict(g, true);	/* force initialization of string attributes */
    ldict = agdictof(g, kind);
    lsym = aglocaldictsym(zz, ldict, name);
    if (lsym!=null) {			/* update old local definiton */
	agstrfree(zz, g, lsym.defval);
	lsym.defval = agstrdup(zz, g, value);
	rv = lsym;
    } else {
	psym = agdictsym(zz, ldict, name);	/* search with viewpath up to root */
	if (psym!=null) {		/* new local definition */
	    lsym = agnewsym(zz, g, name, value, psym.id, kind);
	    dtinsert(zz, ldict, lsym);
	    rv = lsym;
	} else {		/* new global definition */
	    rdict = agdictof(root, kind);
	    rsym = agnewsym(zz, g, name, value, dtsize_(rdict), kind);
	    dtinsert(zz, rdict, rsym);
	    switch (kind) {
	    case AGRAPH:
		agapply(zz, root, root.base, attr__c.addattr,
			rsym, true);
		break;
	    case AGNODE:
		for (n = agfstnode(zz, root); n!=null; n = agnxtnode(zz, root, n))
		    addattr(zz, g, n, rsym);
		break;
	    case AGINEDGE:
	    case AGOUTEDGE:
		for (n = agfstnode(zz, root); n!=null; n = agnxtnode(zz, root, n))
		    for (e = agfstout(zz, root, n); e!=null; e = agnxtout(zz, root, e))
			addattr(zz, g, e, rsym);
		break;
	    }
	    rv = rsym;
	}
    }
    if (rv!=null && (kind == AGRAPH))
	agxset(zz, g, rv, value);
    agmethod_upd(g, g, rv);	/* JCE and GN wanted this */
    return rv;
} finally {
LEAVING("a3qr8ug1rkxp6ocieyp41ly3o","setattr");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="getattr", key="8f80aahwb8cqc2t9592v47ttd", definition="static Agsym_t *getattr(Agraph_t * g, int kind, char *name)")
public static ST_Agsym_s getattr(Globals zz, ST_Agraph_s g, int kind, CString name) {
ENTERING("8f80aahwb8cqc2t9592v47ttd","getattr");
try {
    ST_Agsym_s rv = null;
    ST_dt_s dict;
    dict = agdictof(g, kind);
    if (dict!=null)
	rv = agdictsym(zz, dict, name);	/* viewpath up to root */
    return rv;
} finally {
LEAVING("8f80aahwb8cqc2t9592v47ttd","getattr");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agattr", key="blr3drm2hxuzwd6gpeeb84yyg", definition="Agsym_t *agattr(Agraph_t * g, int kind, char *name, char *value)")
public static ST_Agsym_s agattr(Globals zz, ST_Agraph_s g, int kind, CString name, CString value) {
ENTERING("blr3drm2hxuzwd6gpeeb84yyg","agattr");
try {
    ST_Agsym_s rv;
    if (g == null) {
	if (zz.ProtoGraph == null)
	    zz.ProtoGraph = agopen(zz, null, (ST_Agdesc_s) zz.ProtoDesc.copy(), null);
	g = zz.ProtoGraph;
    }
    if (value!=null)
	rv = setattr(zz, g, kind, name, value);
    else
	rv = getattr(zz, g, kind, name);
    return rv;
} finally {
LEAVING("blr3drm2hxuzwd6gpeeb84yyg","agattr");
}
}



@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agraphattr_init", key="ex2qtmuwz0o2e5svkujdbux78", definition="void agraphattr_init(Agraph_t * g)")
public static void agraphattr_init(Globals zz, ST_Agraph_s g) {
ENTERING("ex2qtmuwz0o2e5svkujdbux78","agraphattr_init");
try {
    /* Agdatadict_t *dd; */
    /* Agrec_t                      *attr; */
    ST_Agraph_s context;
    ((ST_Agdesc_s)g.desc).has_attrs = 1;
    /* dd = */ agmakedatadict(zz, g);
    if ((context = agparent(g)) == null)
	context = g;
    /* attr = */ agmakeattrs(zz, context, g);
} finally {
LEAVING("ex2qtmuwz0o2e5svkujdbux78","agraphattr_init");
}
}




@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agnodeattr_init", key="2pp4ot3pqy71jpfvu3g398y4y", definition="void agnodeattr_init(Agraph_t * g, Agnode_t * n)")
public static void agnodeattr_init(Globals zz, ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("2pp4ot3pqy71jpfvu3g398y4y","agnodeattr_init");
try {
	ST_Agattr_s data;
    data = (ST_Agattr_s) agattrrec(n);
    if (((data) == null) || ((data.dict) == null))
	agmakeattrs(zz, g, n);
} finally {
LEAVING("2pp4ot3pqy71jpfvu3g398y4y","agnodeattr_init");
}
}





@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agedgeattr_init", key="ckfzmnxfi0jiiayxmq38giw0r", definition="void agedgeattr_init(Agraph_t * g, Agedge_t * e)")
public static void agedgeattr_init(Globals zz, ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("ckfzmnxfi0jiiayxmq38giw0r","agedgeattr_init");
try {
	ST_Agattr_s data;
    data = agattrrec(e);
    if (((data) == null) || ((data.dict) == null))
	agmakeattrs(zz, g, e);
} finally {
LEAVING("ckfzmnxfi0jiiayxmq38giw0r","agedgeattr_init");
}
}



@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agget", key="eydjyhexv5jr6vi7uhk0cgphv", definition="char *agget(void *obj, char *name)")
public static CString agget(Globals zz, __ptr__ obj, CString name) {
ENTERING("eydjyhexv5jr6vi7uhk0cgphv","agget");
try {
    ST_Agsym_s sym;
    ST_Agattr_s data;
    CString rv = null;
    
    sym = agattrsym(zz, obj, name);
    if (sym == null)
	rv = null;			/* note was "", but this provides more info */
    else {
	data = agattrrec(obj);
	rv = data.str.get(sym.id);
    }
    return rv;
} finally {
LEAVING("eydjyhexv5jr6vi7uhk0cgphv","agget");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agxget", key="9h5oymhfkp6k34zl0fonn10k9", definition="char *agxget(void *obj, Agsym_t * sym)")
public static CString agxget(__ptr__ obj,  ST_Agsym_s sym) {
ENTERING("9h5oymhfkp6k34zl0fonn10k9","agxget");
try {
	ST_Agattr_s data;
    CString rv;
    data = agattrrec(obj);

    rv = data.str.get(sym.id);
    return rv;
} finally {
LEAVING("9h5oymhfkp6k34zl0fonn10k9","agxget");
}
}




@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agxset", key="4q8xvstvl6fbijhucfd1endxc", definition="int agxset(void *obj, Agsym_t * sym, char *value)")
public static int agxset(Globals zz, ST_Agobj_s obj, ST_Agsym_s sym, CString value) {
ENTERING("4q8xvstvl6fbijhucfd1endxc","agxset");
try {
    ST_Agraph_s g;
    ST_Agobj_s hdr;
    ST_Agattr_s data;
    ST_Agsym_s lsym;
    g = agraphof(obj);
    hdr = (ST_Agobj_s) obj;
    data = agattrrec(hdr);

    agstrfree(zz, g, data.str.get(sym.id));
    data.str.set(sym.id, agstrdup(zz, g, value));
    if (((ST_Agtag_s)hdr.tag).objtype == AGRAPH) {
	/* also update dict default */
	ST_dt_s dict;
	dict = (ST_dt_s) agdatadict(g, false).dict_g;
	if ((lsym = aglocaldictsym(zz, dict, sym.name))!=null) {
	    agstrfree(zz, g, lsym.defval);
	    lsym.defval = agstrdup(zz, g, value);
	} else {
	    lsym = agnewsym(zz, g, sym.name, value, sym.id, hdr.tag.objtype);
	    dict.searchf.exe(zz, dict, lsym, 0000001);
	}
    }
    agmethod_upd(g, obj, sym);
    return 0;
} finally {
LEAVING("4q8xvstvl6fbijhucfd1endxc","agxset");
}
}




@Original(version="2.38.0", path="lib/cgraph/attr.c", name="agsafeset", key="9b7vn95cin8o7mb2f21exh1qr", definition="int agsafeset(void *obj, char *name, char *value, char *def)")
public static int agsafeset(Globals zz, ST_Agobj_s obj, CString name, CString value, CString def) {
ENTERING("9b7vn95cin8o7mb2f21exh1qr","agsafeset");
try {
    ST_Agsym_s a;
    a = agattr(zz, agraphof(obj), obj.tag.objtype, name, null);
    if ((a) == null)
	a = agattr(zz, agraphof(obj), obj.tag.objtype, name, def);
    return agxset(zz, obj, a, value);
} finally {
LEAVING("9b7vn95cin8o7mb2f21exh1qr","agsafeset");
}
}




//3 6gjlgo4s6r0bu7gjazfee6qv8
// static void init_all_attrs(Agraph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/attr.c", name="init_all_attrs", key="6gjlgo4s6r0bu7gjazfee6qv8", definition="static void init_all_attrs(Agraph_t * g)")
public static Object init_all_attrs(Object... arg_) {
UNSUPPORTED("bir8xur87cl8inhyrgimkboqq"); // static void init_all_attrs(Agraph_t * g)
throw new UnsupportedOperationException();
}




}
