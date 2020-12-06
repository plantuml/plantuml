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
import static gen.lib.cdt.dtextract__c.dtextract;
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static gen.lib.cgraph.attr__c.AgDataRecName;
import static gen.lib.cgraph.attr__c.agedgeattr_init;
import static gen.lib.cgraph.graph__c.agisstrict;
import static gen.lib.cgraph.graph__c.agisundirected;
import static gen.lib.cgraph.graph__c.agnextseq;
import static gen.lib.cgraph.id__c.agmapnametoid;
import static gen.lib.cgraph.id__c.agregister;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.node__c.agsubnode;
import static gen.lib.cgraph.obj__c.agmethod_init;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.rec__c.agbindrec;
import static gen.lib.cgraph.subg__c.agparent;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGHEAD;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGMKIN;
import static smetana.core.Macro.AGMKOUT;
import static smetana.core.Macro.AGOPP;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTAG;
import static smetana.core.Macro.AGTAIL;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.dtfirst;
import static smetana.core.Macro.dtinsert;
import static smetana.core.Macro.dtnext;
import static smetana.core.Macro.dtsearch;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agattr_s;
import h.ST_Agdesc_s;
import h.ST_Agedge_s;
import h.ST_Agedgepair_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsubnode_s;
import h.ST_Agtag_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtlink_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CStarStar;
import smetana.core.CString;
import smetana.core.Z;
import smetana.core.__ptr__;

public class edge__c {

@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="9vamtktowqtk4955i546z9obw", definition="Agedge_t *agfstout(Agraph_t * g, Agnode_t * n)")
public static ST_Agedge_s agfstout(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("9vamtktowqtk4955i546z9obw","agfstout");
try {
	ST_Agsubnode_s sn;
	ST_Agedge_s e = null;
	
    sn = agsubrep(g, n);
    if (sn!=null) {
		dtrestore(g.e_seq, sn.out_seq);
		e = (ST_Agedge_s) dtfirst(g.e_seq);
		sn.out_seq = dtextract(g.e_seq);
	}
    return e;
} finally {
LEAVING("9vamtktowqtk4955i546z9obw","agfstout");
}
}




/* return outedge that follows <e> of <n> */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="1qh7mgqwomkdqvczauv4ex1lu", definition="Agedge_t *agnxtout(Agraph_t * g, Agedge_t * e)")
public static ST_Agedge_s agnxtout(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("1qh7mgqwomkdqvczauv4ex1lu","agnxtout");
try {
    ST_Agnode_s n;
    ST_Agsubnode_s sn;
    ST_Agedge_s f = null;
    
    n = AGTAIL(e);
    sn = agsubrep(g, n);
    if (sn!=null) {
		dtrestore(g.e_seq, sn.out_seq);
		f = (ST_Agedge_s) dtnext(g.e_seq, e);
		sn.out_seq = dtextract(g.e_seq);
	}
    return f;
} finally {
LEAVING("1qh7mgqwomkdqvczauv4ex1lu","agnxtout");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="c60qt3ycq0xweabgtqt16xe93", definition="Agedge_t *agfstin(Agraph_t * g, Agnode_t * n)")
public static ST_Agedge_s agfstin(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("c60qt3ycq0xweabgtqt16xe93","agfstin");
try {
	ST_Agsubnode_s sn;
	ST_Agedge_s e = null;
	
    sn = agsubrep(g, n);
	if (sn!=null) {
		dtrestore(g.e_seq, sn.in_seq);
		e = (ST_Agedge_s) dtfirst(g.e_seq);
		sn.in_seq = dtextract(g.e_seq);
	}
    return e;
} finally {
LEAVING("c60qt3ycq0xweabgtqt16xe93","agfstin");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="f2af4x97mqn16npd6alsw7avs", definition="Agedge_t *agnxtin(Agraph_t * g, Agedge_t * e)")
public static ST_Agedge_s agnxtin(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("f2af4x97mqn16npd6alsw7avs","agnxtin");
try {
    ST_Agnode_s n;
    ST_Agsubnode_s sn;
    ST_Agedge_s f = null;
    
    n = AGHEAD(e);
    sn = agsubrep(g, n);
	if (sn!=null) {
		dtrestore(g.e_seq, sn.in_seq);
		f = (ST_Agedge_s) dtnext(g.e_seq, e);
		sn.in_seq = dtextract(g.e_seq);
	}
	return f;
} finally {
LEAVING("f2af4x97mqn16npd6alsw7avs","agnxtin");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="6nwyo5bklramr0d093aa1h25o", definition="Agedge_t *agfstedge(Agraph_t * g, Agnode_t * n)")
public static ST_Agedge_s agfstedge(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("6nwyo5bklramr0d093aa1h25o","agfstedge");
try {
    ST_Agedge_s rv;
    rv = agfstout(g, n);
    if (rv == null)
	rv = agfstin(g, n);
    return rv;
} finally {
LEAVING("6nwyo5bklramr0d093aa1h25o","agfstedge");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="8zy2u6gsi2xzv2ffv8o4v4uvf", definition="Agedge_t *agnxtedge(Agraph_t * g, Agedge_t * e, Agnode_t * n)")
public static ST_Agedge_s agnxtedge(ST_Agraph_s g, ST_Agedge_s e, ST_Agnode_s n) {
ENTERING("8zy2u6gsi2xzv2ffv8o4v4uvf","agnxtedge");
try {
    ST_Agedge_s rv;
    
    if (AGTYPE(e) == AGOUTEDGE) {
	rv = agnxtout(g, e);
	if (rv == null) {
	    do {
		rv = N(rv) ? agfstin(g, n) : agnxtin(g,rv);
	    } while (rv!=null && EQ(rv.node, n));
	}
    } else {
	do {
	    rv = agnxtin(g, e);		/* so that we only see each edge once, */
		e = rv;
	} while (rv!=null && EQ(rv.node, n));	/* ignore loops as in-edges */
    }
    return rv;
} finally {
LEAVING("8zy2u6gsi2xzv2ffv8o4v4uvf","agnxtedge");
}
}




/* internal edge set lookup */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="c175o6j61jqmfnl4o1g1h1mie", definition="static Agedge_t *agfindedge_by_key(Agraph_t * g, Agnode_t * t, Agnode_t * h, 			    Agtag_t key)")
public static ST_Agedge_s agfindedge_by_key(ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h,  final ST_Agtag_s key) {
// WARNING!! STRUCT
return agfindedge_by_key_w_(g, t, h, key.copy());
}
private static ST_Agedge_s agfindedge_by_key_w_(ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h,  final ST_Agtag_s key) {
ENTERING("c175o6j61jqmfnl4o1g1h1mie","agfindedge_by_key");
try {
	ST_Agedge_s e;
    final ST_Agedge_s template = new ST_Agedge_s();
    ST_Agsubnode_s sn;
    
    if ((t == null) || (h == null))
	return null;
    template.base.tag.___(key);
    template.node = t;		/* guess that fan-in < fan-out */
    sn = agsubrep(g, h);
    if (N(sn)) e = null;
    else {
	    dtrestore(g.e_id, sn.in_id);
	    e = (ST_Agedge_s) dtsearch(g.e_id, template);
	    sn.in_id = dtextract(g.e_id);
    }
    return e;
} finally {
LEAVING("c175o6j61jqmfnl4o1g1h1mie","agfindedge_by_key");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="agsubrep", key="b32ssm6ex1pdz1b3nt4fwlhul", definition="Agsubnode_t *agsubrep(Agraph_t * g, Agnode_t * n)")
public static ST_Agsubnode_s agsubrep(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("b32ssm6ex1pdz1b3nt4fwlhul","agsubrep");
try {
	ST_Agsubnode_s sn;
    final ST_Agsubnode_s template = new ST_Agsubnode_s();
	if (EQ(g, n.root)) sn = n.mainsub;
	else {
			template.node = n;
			sn = (ST_Agsubnode_s) dtsearch(g.n_id, template);
	}
    return sn;
} finally {
LEAVING("b32ssm6ex1pdz1b3nt4fwlhul","agsubrep");
}
}



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="ins", key="6u0niow33w9gva780waluva4n", definition="static void ins(Dict_t * d, Dtlink_t ** set, Agedge_t * e)")
public static void ins(ST_dt_s d, CStarStar<ST_dtlink_s> set, ST_Agedge_s e) {
ENTERING("6u0niow33w9gva780waluva4n","ins");
try {
    dtrestore(d, set.star());
    dtinsert(d, e);
    set.star(dtextract(d));
} finally {
LEAVING("6u0niow33w9gva780waluva4n","ins");
}
}





@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="installedge", key="8kizmg7gziussfgx8zs3qvkfw", definition="static void installedge(Agraph_t * g, Agedge_t * e)")
public static void installedge(ST_Agraph_s g, ST_Agedge_s e) {
ENTERING("8kizmg7gziussfgx8zs3qvkfw","installedge");
try {
    ST_Agnode_s t, h;
    ST_Agedge_s out, in;
    ST_Agsubnode_s sn;
    
    
    out = AGMKOUT(e);
    in = AGMKIN(e);
    t = agtail(e);
    h = aghead(e);
    while (g!=null) {
	if (agfindedge_by_key(g, t, h, AGTAG(e))!=null) break;
	sn = agsubrep(g, t);
	ins(g.e_seq, sn.out_seq_AMP(), out);
	ins(g.e_id, sn.out_id_AMP(), out);
	sn = agsubrep(g, h); 
	ins(g.e_seq, sn.in_seq_AMP(), in);
	ins(g.e_id, sn.in_id_AMP(), in);
	g = agparent(g);
    }
} finally {
LEAVING("8kizmg7gziussfgx8zs3qvkfw","installedge");
}
}




//3 2vtt6zb0n3oru23okvw4pxasg
// static void subedge(Agraph_t * g, Agedge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="subedge", key="2vtt6zb0n3oru23okvw4pxasg", definition="static void subedge(Agraph_t * g, Agedge_t * e)")
public static Object subedge(Object... arg_) {
UNSUPPORTED("1qslen16fp6w3yse2y311vtsf"); // static void subedge(Agraph_t * g, Agedge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3rvuc9rdxdwmvq3dtwv2hqwjo"); //     installedge(g, e);
UNSUPPORTED("4d70e4y79ekvafnx88hgwdrna"); //     /* might an init method call be needed here? */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4rzjui6oo0k009o64bxwgjmvq
// static Agedge_t *newedge(Agraph_t * g, Agnode_t * t, Agnode_t * h, 			 unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="4rzjui6oo0k009o64bxwgjmvq", definition="static Agedge_t *newedge(Agraph_t * g, Agnode_t * t, Agnode_t * h, 			 unsigned long id)")
public static ST_Agedge_s newedge(ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h, int id) {
ENTERING("4rzjui6oo0k009o64bxwgjmvq","newedge");
try {
	ST_Agedgepair_s e2;
    ST_Agedge_s in, out;
    int seq;
    agsubnode(g,t,(N(0)));
    agsubnode(g,h,(N(0)));
    e2 = (ST_Agedgepair_s) agalloc(g, sizeof(ST_Agedgepair_s.class));
    in = (ST_Agedge_s) e2.in;
    out = (ST_Agedge_s) e2.out;
    seq = agnextseq(g, AGEDGE);
    AGTYPE(in, AGINEDGE);
    AGTYPE(out, AGOUTEDGE);
    AGID(out, id);
    AGID(in, id);
    AGSEQ(in, seq);
    AGSEQ(out, seq);
    in.node = t;
    out.node = h;
    installedge(g, out);
    if (((ST_Agdesc_s)g.desc).has_attrs!=0) {
	  agbindrec(out, AgDataRecName, sizeof(ST_Agattr_s.class), false);
	  agedgeattr_init(g, out);
    }
    agmethod_init(g, out);
    return out;
} finally {
LEAVING("4rzjui6oo0k009o64bxwgjmvq","newedge");
}
}




//3 1ufxhg5xnmll1pe5339477823
// static int ok_to_make_edge(Agraph_t * g, Agnode_t * t, Agnode_t * h) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="ok_to_make_edge", key="1ufxhg5xnmll1pe5339477823", definition="static int ok_to_make_edge(Agraph_t * g, Agnode_t * t, Agnode_t * h)")
public static boolean ok_to_make_edge(ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h) {
ENTERING("1ufxhg5xnmll1pe5339477823","ok_to_make_edge");
try {
    final ST_Agtag_s key = new ST_Agtag_s();
    /* protect against self, multi-edges in strict graphs */
    if (agisstrict(g)) {
	if (g.desc.no_loop!=0 && (EQ(t, h))) /* simple graphs */
	    return false;
	key.___(Z.z().Tag);
	key.objtype = 0;	/* wild card */
	if (agfindedge_by_key(g, t, h, key)!=null)
	    return false;
    }
    return (N(0));
} finally {
LEAVING("1ufxhg5xnmll1pe5339477823","ok_to_make_edge");
}
}





//3 4361pvzr3ozft2ov0fgx6t8bo
// Agedge_t *agedge(Agraph_t * g, Agnode_t * t, Agnode_t * h, char *name, 		 int cflag) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="4361pvzr3ozft2ov0fgx6t8bo", definition="Agedge_t *agedge(Agraph_t * g, Agnode_t * t, Agnode_t * h, char *name, 		 int cflag)")
public static ST_Agedge_s agedge(ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h, CString name, boolean cflag) {
ENTERING("4361pvzr3ozft2ov0fgx6t8bo","agedge");
try {
    ST_Agedge_s e;
    int id[] = new int[1];
    int have_id;
    have_id = agmapnametoid(g, AGEDGE, name, id, false);
    if (have_id!=0 || ((name == null) && ((NOT(cflag)) || agisstrict(g)))) {
	/* probe for pre-existing edge */
	final ST_Agtag_s key = new ST_Agtag_s();
	key.___(Z.z().Tag);
	if (have_id!=0) {
	    key.id = id[0];
	    key.objtype = AGEDGE;
	} else {
	    key.id = 0;
	    key.objtype = 0;
	}
	/* might already exist locally */
	e = agfindedge_by_key(g, t, h, key);
	if ((e == null && agisundirected(g)))
	    e = agfindedge_by_key(g, h, t, key);
	if (e!=null)
	    return e;
	if (cflag) {
	    e = agfindedge_by_key(agroot(g), t, h, key);
	    if ((e == null) && agisundirected(g))
		e = agfindedge_by_key(agroot(g), h, t, key);
	    if (e!=null) {
		subedge(g,e);
		return e;
	    }
 	}
    }
    if (cflag && ok_to_make_edge(g, t, h)
	&& (agmapnametoid(g, AGEDGE, name, id, (N(0))))!=0) { /* reserve id */
	e = newedge(g, t, h, id[0]);
	agregister(g, AGEDGE, e); /* register new object in external namespace */
    }
    else
	e = null;
    return e;
} finally {
LEAVING("4361pvzr3ozft2ov0fgx6t8bo","agedge");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="30v8z3tlda81fbqbkzx6m9fkn", definition="Agedge_t *agsubedge(Agraph_t * g, Agedge_t * e, int cflag)")
public static ST_Agedge_s agsubedge(ST_Agraph_s g, ST_Agedge_s e, boolean cflag) {
ENTERING("30v8z3tlda81fbqbkzx6m9fkn","agsubedge");
try {
    ST_Agnode_s t, h;
    ST_Agedge_s rv;
    
    rv = null;
    t = agsubnode(g, AGTAIL(e), cflag);
    h = agsubnode(g, AGHEAD(e), cflag);
    if (t!=null && h!=null) {
	rv = agfindedge_by_key(g, t, h, AGTAG(e));
	if (cflag && (rv == null)) {
	installedge(g, e);
	rv = e;
	}
	if (rv!=null && (AGTYPE(rv) != AGTYPE(e)))
	    rv = AGOPP(rv);
    }
    return rv;
} finally {
LEAVING("30v8z3tlda81fbqbkzx6m9fkn","agsubedge");
}
}


public static CFunction agedgeidcmpf = new CFunctionAbstract("agedgeidcmpf") {
	
	public Object exe(Object... args) {
		return agedgeidcmpf((ST_dt_s)args[0], (ST_Agedge_s)args[1], (ST_Agedge_s)args[2], (ST_dtdisc_s)args[3]);
	}};
	

/* edge comparison.  OBJTYPE(e) == 0 means ID is a wildcard. */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="agedgeidcmpf", key="avk47eh26r45qk2dtoipwiqvz", definition="int agedgeidcmpf(Dict_t * d, void *arg_e0, void *arg_e1, Dtdisc_t * disc)")
public static int agedgeidcmpf(ST_dt_s d, ST_Agedge_s arg_e0, ST_Agedge_s arg_e1, ST_dtdisc_s disc) {
ENTERING("avk47eh26r45qk2dtoipwiqvz","agedgeidcmpf");
try {
    int v;
    ST_Agedge_s e0, e1;
    e0 = (ST_Agedge_s) arg_e0;
    e1 = (ST_Agedge_s) arg_e1;
    v = AGID(e0.node) - AGID(e1.node);
    if (v == 0) {		/* same node */
	if ((AGTYPE(e0) == 0) || (AGTYPE(e1) == 0))
	    v = 0;
	else
	    v = AGID(e0) - AGID(e1);
    }
    return ((v==0)?0:(v<0?-1:1));
} finally {
LEAVING("avk47eh26r45qk2dtoipwiqvz","agedgeidcmpf");
}
}


public static CFunction agedgeseqcmpf = new CFunctionAbstract("agedgeseqcmpf") {
	
	public Object exe(Object... args) {
		return agedgeseqcmpf((ST_dt_s)args[0], (ST_Agedge_s)args[1], (ST_Agedge_s)args[2], (ST_dtdisc_s)args[3]);
	}};
	

/* edge comparison.  for ordered traversal. */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="agedgeseqcmpf", key="b6jhzc16xvrknu4e7jp6zx0ue", definition="int agedgeseqcmpf(Dict_t * d, void *arg_e0, void *arg_e1, Dtdisc_t * disc)")
public static int agedgeseqcmpf(ST_dt_s d, ST_Agedge_s arg_e0, ST_Agedge_s arg_e1, ST_dtdisc_s disc) {
ENTERING("b6jhzc16xvrknu4e7jp6zx0ue","agedgeseqcmpf");
try {
    int v;
    ST_Agedge_s e0, e1;
    e0 = (ST_Agedge_s) arg_e0;
    e1 = (ST_Agedge_s) arg_e1;
	if (NEQ(e0.node, e1.node)) v = AGSEQ(e0.node) - AGSEQ(e1.node);
	else v = (AGSEQ(e0) - AGSEQ(e1));
    return ((v==0)?0:(v<0?-1:1));
} finally {
LEAVING("b6jhzc16xvrknu4e7jp6zx0ue","agedgeseqcmpf");
}
}


@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="ceexs6t1q4jxwz6h0g8fszxp4", definition="Agnode_t *agtail(Agedge_t * e)")
public static ST_Agnode_s agtail(ST_Agedge_s e) {
ENTERING("ceexs6t1q4jxwz6h0g8fszxp4","agtail");
try {
    return AGTAIL(e);
} finally {
LEAVING("ceexs6t1q4jxwz6h0g8fszxp4","agtail");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="3tj9gj3dvrpox6grrdd3rftd8", definition="Agnode_t *aghead(Agedge_t * e)")
public static ST_Agnode_s aghead(__ptr__ e) {
ENTERING("3tj9gj3dvrpox6grrdd3rftd8","aghead");
try {
    return AGHEAD((ST_Agedge_s) e);
} finally {
LEAVING("3tj9gj3dvrpox6grrdd3rftd8","aghead");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/edge.c", name="", key="15e6s5bh5hey2u79yoebir59w", definition="Agedge_t *agopp(Agedge_t * e)")
public static ST_Agedge_s agopp(__ptr__ e) {
ENTERING("15e6s5bh5hey2u79yoebir59w","agopp");
try {
    return AGOPP((ST_Agedge_s) e);
} finally {
LEAVING("15e6s5bh5hey2u79yoebir59w","agopp");
}
}


}
