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
import static gen.lib.cdt.dtsize__c.dtsize_;
import static gen.lib.cgraph.attr__c.AgDataRecName;
import static gen.lib.cgraph.attr__c.agnodeattr_init;
import static gen.lib.cgraph.edge__c.agsubrep;
import static gen.lib.cgraph.graph__c.agnextseq;
import static gen.lib.cgraph.id__c.agmapnametoid;
import static gen.lib.cgraph.id__c.agregister;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.obj__c.agmethod_init;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.rec__c.agbindrec;
import static gen.lib.cgraph.subg__c.agparent;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.dtfirst;
import static smetana.core.Macro.dtnext;
import static smetana.core.Macro.dtsearch;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agattr_s;
import h.ST_Agdesc_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsubnode_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CString;
import smetana.core.Z;
import smetana.core.__ptr__;

public class node__c {



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="", key="4w89du6uel405pm3vxsr3ayxt", definition="Agnode_t *agfindnode_by_id(Agraph_t * g, unsigned long id)")
public static ST_Agnode_s agfindnode_by_id(ST_Agraph_s g, int id) {
ENTERING("4w89du6uel405pm3vxsr3ayxt","agfindnode_by_id");
try {
    ST_Agsubnode_s sn;
    
    
    Z.z().dummy.base.tag.id = id;
    Z.z().template.node = Z.z().dummy;
    sn = (ST_Agsubnode_s) dtsearch(g.n_id, Z.z().template);
    return sn!=null ? sn.node : null;
} finally {
LEAVING("4w89du6uel405pm3vxsr3ayxt","agfindnode_by_id");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="agfstnode", key="55wopi2gd93zpmycxoywlxm0y", definition="Agnode_t *agfstnode(Agraph_t * g)")
public static ST_Agnode_s agfstnode(ST_Agraph_s g) {
ENTERING("55wopi2gd93zpmycxoywlxm0y","agfstnode");
try {
	ST_Agsubnode_s sn;
    sn = (ST_Agsubnode_s) dtfirst(g.n_seq);
    return sn!=null ? sn.node : null;
} finally {
LEAVING("55wopi2gd93zpmycxoywlxm0y","agfstnode");
}
}






@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="agnxtnode", key="bek79ccvjys1j9q404i3y6oh8", definition="Agnode_t *agnxtnode(Agraph_t * g, Agnode_t * n)")
public static ST_Agnode_s agnxtnode(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("bek79ccvjys1j9q404i3y6oh8","agnxtnode");
try {
    ST_Agsubnode_s sn;
    sn = agsubrep(g, n);
    if (sn!=null) sn = (ST_Agsubnode_s) dtnext(g.n_seq, sn);
    return sn!=null ? sn.node : null;
} finally {
LEAVING("bek79ccvjys1j9q404i3y6oh8","agnxtnode");
}
}



//3 dzb7m0p5xsngvtyr8zs912og4
// static Agnode_t *newnode(Agraph_t * g, unsigned long id, unsigned long seq) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/node.c", name="", key="dzb7m0p5xsngvtyr8zs912og4", definition="static Agnode_t *newnode(Agraph_t * g, unsigned long id, unsigned long seq)")
public static ST_Agnode_s newnode(ST_Agraph_s g, int id, int seq) {
ENTERING("dzb7m0p5xsngvtyr8zs912og4","newnode");
try {
    ST_Agnode_s n;
    n = (ST_Agnode_s) ((__ptr__)agalloc(g, sizeof(ST_Agnode_s.class)));
    AGTYPE(n, AGNODE);
    AGID(n, id);
    AGSEQ(n, seq);
    n.root = agroot(g);
    if (((ST_Agdesc_s)agroot(g).desc).has_attrs!=0)
	  agbindrec(n, AgDataRecName, sizeof(ST_Agattr_s.class), false);
    /* nodeattr_init and method_init will be called later, from the
     * subgraph where the node was actually created, but first it has
     * to be installed in all the (sub)graphs up to root. */
    return n;
} finally {
LEAVING("dzb7m0p5xsngvtyr8zs912og4","newnode");
}
}




//3 4m26dpgaiw44hcleugjy71eus
// static void installnode(Agraph_t * g, Agnode_t * n) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/node.c", name="installnode", key="4m26dpgaiw44hcleugjy71eus", definition="static void installnode(Agraph_t * g, Agnode_t * n)")
public static void installnode(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("4m26dpgaiw44hcleugjy71eus","installnode");
try {
	ST_Agsubnode_s sn;
    int osize;
    osize = dtsize_((ST_dt_s)g.n_id);
    if (EQ(g, agroot(g))) sn = (ST_Agsubnode_s) n.mainsub;
    else sn = (ST_Agsubnode_s) ((__ptr__)agalloc(g, sizeof(ST_Agsubnode_s.class))).castTo(ST_Agsubnode_s.class);
    sn.node = n;
    g.n_id.searchf.exe(g.n_id,sn,0000001);
    g.n_seq.searchf.exe(g.n_seq,sn,0000001);
} finally {
LEAVING("4m26dpgaiw44hcleugjy71eus","installnode");
}
}




//3 3mfxjcaeepn8nitirs3yoqaed
// static void installnodetoroot(Agraph_t * g, Agnode_t * n) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/node.c", name="installnodetoroot", key="3mfxjcaeepn8nitirs3yoqaed", definition="static void installnodetoroot(Agraph_t * g, Agnode_t * n)")
public static void installnodetoroot(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("3mfxjcaeepn8nitirs3yoqaed","installnodetoroot");
try {
    ST_Agraph_s par;
    installnode(g, n);
    if ((par = agparent(g))!=null)
	installnodetoroot(par, n);
} finally {
LEAVING("3mfxjcaeepn8nitirs3yoqaed","installnodetoroot");
}
}




//3 85bb9mezhsgtzar3kqz95mq1
// static void initnode(Agraph_t * g, Agnode_t * n) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/node.c", name="initnode", key="85bb9mezhsgtzar3kqz95mq1", definition="static void initnode(Agraph_t * g, Agnode_t * n)")
public static void initnode(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("85bb9mezhsgtzar3kqz95mq1","initnode");
try {
    if (((ST_Agdesc_s)agroot(g).desc).has_attrs!=0)
	agnodeattr_init(g,n);
    agmethod_init(g, n);
} finally {
LEAVING("85bb9mezhsgtzar3kqz95mq1","initnode");
}
}




/* external node constructor - create by id */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="", key="1m6sl9df2yaolmufyq5i577a3", definition="Agnode_t *agidnode(Agraph_t * g, unsigned long id, int cflag)")
public static ST_Agnode_s agidnode(ST_Agraph_s g, int id, int cflag) {
ENTERING("1m6sl9df2yaolmufyq5i577a3","agidnode");
try {
    ST_Agraph_s root;
    ST_Agnode_s n;
    
    n = agfindnode_by_id(g, id);
    if ((n == null) && cflag!=0) {
UNSUPPORTED("7zol2448bccu90sqoxkvnbuif"); // 	root = agroot(g);
UNSUPPORTED("1zcb29h7sxm7axw8qeuz9f38w"); // 	if ((g != root) && ((n = agfindnode_by_id(root, id))))	/*old */
UNSUPPORTED("9fusma9293koujpr79eyfhxn6"); // 	    agsubnode(g, n, (!(0)));	/* insert locally */
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("5pefvv55zys4ya7lgh0v8595s"); // 	    if (agallocid(g, AGNODE, id)) {	/* new */
UNSUPPORTED("dfn6cx0kwd44mv0ntbzf3p463"); // 		n = newnode(g, id, agnextseq(g, AGNODE));
UNSUPPORTED("6qcjp92a88ggl3ea7mxel9cn"); // 		installnodetoroot(g, n);
UNSUPPORTED("45uf6o7ubd9hy5s65g0f0bbgj"); // 		initnode(g, n);
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("5l8briolxhuic5avv0112aj90"); // 		n = ((Agnode_t*)0);	/* allocid for new node failed */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    /* else return probe result */
    return n;
} finally {
LEAVING("1m6sl9df2yaolmufyq5i577a3","agidnode");
}
}




//3 4yh1h1cwoitzb1t8869b79e3g
// Agnode_t *agnode(Agraph_t * g, char *name, int cflag) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/node.c", name="", key="4yh1h1cwoitzb1t8869b79e3g", definition="Agnode_t *agnode(Agraph_t * g, char *name, int cflag)")
public static ST_Agnode_s agnode(ST_Agraph_s g, CString name, boolean cflag) {
ENTERING("4yh1h1cwoitzb1t8869b79e3g","agnode");
try {
    ST_Agraph_s root;
    ST_Agnode_s n;
    int id[] = new int[1];
    root = agroot(g);
    /* probe for existing node */
    if (agmapnametoid(g, AGNODE, name, id, false)!=0) {
	if ((n = agfindnode_by_id(g, id[0]))!=null)
	    return n;
	/* might already exist globally, but need to insert locally */
	if (cflag && NEQ(g, root) && ((n = agfindnode_by_id(root, id[0])))!=null) {
	    return agsubnode(g, n, (N(0)));
    }
    }
    if (cflag && agmapnametoid(g, AGNODE, name, id, (N(0)))!=0) {	/* reserve id */
	n = newnode(g, id[0], agnextseq(g, AGNODE));
	installnodetoroot(g, n);
	initnode(g, n);

	agregister(g, AGNODE, n); /* register in external namespace */
	return n;
    }
    return null;
} finally {
LEAVING("4yh1h1cwoitzb1t8869b79e3g","agnode");
}
}



/* lookup or insert <n> in <g> */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="", key="d5farp22buvesyi4pydjam4g2", definition="Agnode_t *agsubnode(Agraph_t * g, Agnode_t * n0, int cflag)")
public static ST_Agnode_s agsubnode(ST_Agraph_s g, ST_Agnode_s n0, boolean cflag) {
ENTERING("d5farp22buvesyi4pydjam4g2","agsubnode");
try {
    ST_Agraph_s par;
    ST_Agnode_s n;
    
    if (NEQ(agroot(g), n0.root))
	return null;
    n = agfindnode_by_id(g, AGID(n0));
    if ((n == null) && cflag) {
	if ((par = agparent(g))!=null) {
	    n = agsubnode(par, n0, cflag);
	    installnode(g, n);
	    /* no callback for existing node insertion in subgraph (?) */
	}
	/* else impossible that <n> doesn't belong to <g> */
    }
    /* else lookup succeeded */
    return n;
} finally {
LEAVING("d5farp22buvesyi4pydjam4g2","agsubnode");
}
}


public static CFunction agsubnodeidcmpf = new CFunctionAbstract("agsubnodeidcmpf") {
	
	public Object exe(Object... args) {
		return agsubnodeidcmpf((ST_dt_s)args[0], (__ptr__)args[1], (__ptr__)args[2], (ST_dtdisc_s)args[3]);
	}};

@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="agsubnodeidcmpf", key="awwiazixy9c76hvyxlkvvb3vo", definition="int agsubnodeidcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc)")
public static int agsubnodeidcmpf(ST_dt_s d, __ptr__ arg0, __ptr__ arg1, ST_dtdisc_s disc) {
ENTERING("awwiazixy9c76hvyxlkvvb3vo","agsubnodeidcmpf");
try {
    int	v;
    ST_Agsubnode_s sn0, sn1;
    sn0 = (ST_Agsubnode_s) arg0.castTo(ST_Agsubnode_s.class);
    sn1 = (ST_Agsubnode_s) arg1.castTo(ST_Agsubnode_s.class);
    v = (AGID(sn0.node) - AGID(sn1.node));
    return ((v==0)?0:(v<0?-1:1));
} finally {
LEAVING("awwiazixy9c76hvyxlkvvb3vo","agsubnodeidcmpf");
}
}



public static CFunction agsubnodeseqcmpf = new CFunctionAbstract("agsubnodeseqcmpf") {
	
	public Object exe(Object... args) {
		return agsubnodeseqcmpf((ST_dt_s)args[0], (__ptr__)args[1], (__ptr__)args[2], (ST_dtdisc_s)args[3]);
	}};
	
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/node.c", name="agsubnodeseqcmpf", key="41fjseux0nxzpr0aq7igym9ux", definition="int agsubnodeseqcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc)")
public static int agsubnodeseqcmpf(ST_dt_s d, __ptr__ arg0, __ptr__ arg1, ST_dtdisc_s disc) {
ENTERING("41fjseux0nxzpr0aq7igym9ux","agsubnodeseqcmpf");
try {
	ST_Agsubnode_s sn0, sn1;
    int	v;
    sn0 = (ST_Agsubnode_s) arg0.castTo(ST_Agsubnode_s.class);
    sn1 = (ST_Agsubnode_s) arg1.castTo(ST_Agsubnode_s.class);
    v = (AGSEQ(sn0.node) - AGSEQ(sn1.node));
    return ((v==0)?0:(v<0?-1:1));
} finally {
LEAVING("41fjseux0nxzpr0aq7igym9ux","agsubnodeseqcmpf");
}
}




public static CFunction free_subnode = new CFunctionAbstract("free_subnode") {
	
	public Object exe(Object... args) {
		return free_subnode(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/cgraph/node.c", name="free_subnode", key="a7tb3b1kvq6ykrxzhbaduvg9r", definition="static void free_subnode (Dt_t* d, Agsubnode_t* sn, Dtdisc_t * disc)")
public static Object free_subnode(Object... arg_) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9e4h6d4hxsvsnaiuubzlmccsm"); // free_subnode (Dt_t* d, Agsubnode_t* sn, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eiac02o593gy0a55vv1w8mkmi"); //    if (!AGSNMAIN(sn)) 
UNSUPPORTED("263bmzd9ilyyeb9w34squ7iw8"); // 	agfree (sn->node->root, sn);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}



}
