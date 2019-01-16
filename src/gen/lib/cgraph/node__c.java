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
import static smetana.core.JUtils.LOG2;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agattr_s;
import h.ST_Agdesc_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsubnode_s;
import h.ST_Agtag_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import smetana.core.CString;
import smetana.core.Z;
import smetana.core.__ptr__;

public class node__c {
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




//private static __struct__<Agsubnode_s> template = JUtils.from(Agsubnode_s.class);
//private static __struct__<Agnode_s> dummy = JUtils.from(Agnode_s.class);
//3 4w89du6uel405pm3vxsr3ayxt
// Agnode_t *agfindnode_by_id(Agraph_t * g, unsigned long id) 
public static ST_Agnode_s agfindnode_by_id(ST_Agraph_s g, int id) {
ENTERING("4w89du6uel405pm3vxsr3ayxt","agfindnode_by_id");
try {
    ST_Agsubnode_s sn;
    ((ST_Agtag_s)Z.z().dummy.base.tag).id = id;
    Z.z().template.node = Z.z().dummy;
    sn = (ST_Agsubnode_s) (g.n_id.searchf.exe(g.n_id, Z.z().template,0000004));
    return (ST_Agnode_s) (sn!=null ? sn.node : null);
} finally {
LEAVING("4w89du6uel405pm3vxsr3ayxt","agfindnode_by_id");
}
}




//3 1ibow5tsw9y9hfbt65y10nw0r
// Agnode_t *agfindnode_by_name(Agraph_t * g, char *name) 
public static Object agfindnode_by_name(Object... arg) {
UNSUPPORTED("jjckyz5rvj2kpvd0vw02o8yj"); // Agnode_t *agfindnode_by_name(Agraph_t * g, char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5v0qr6wzw47z083l6jupv94gw"); //     unsigned long id;
UNSUPPORTED("7xzjyxv5eprg0vhj8q61h9d84"); //     if (agmapnametoid(g, AGNODE, name, &id, (0)))
UNSUPPORTED("5rhbsviec1b9h1qedfo3hrgt0"); // 	return agfindnode_by_id(g, id);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("afujljwagn2n2w7aqkq94dyud"); // 	return ((Agnode_t*)0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 55wopi2gd93zpmycxoywlxm0y
// Agnode_t *agfstnode(Agraph_t * g) 
public static ST_Agnode_s agfstnode(ST_Agraph_s g) {
ENTERING("55wopi2gd93zpmycxoywlxm0y","agfstnode");
try {
	ST_Agsubnode_s sn;
    sn = (ST_Agsubnode_s) g.n_seq.searchf.exe(g.n_seq,null,0000200);
    return sn!=null ? (ST_Agnode_s) sn.node : null;
} finally {
LEAVING("55wopi2gd93zpmycxoywlxm0y","agfstnode");
}
}




//3 bek79ccvjys1j9q404i3y6oh8
// Agnode_t *agnxtnode(Agraph_t * g, Agnode_t * n) 
public static int NB = 0;
public static ST_Agnode_s agnxtnode(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("bek79ccvjys1j9q404i3y6oh8","agnxtnode");
try {
	//ZOOTO
	if (NB==0) {
	//StructureDefinition.from(IMapEntry_t.class);
	}
	NB++;
    LOG2("NB="+NB);
    ST_Agsubnode_s sn;
    sn = agsubrep(g, n);
    LOG2("sn1="+sn);
    if (sn!=null) sn = (ST_Agsubnode_s) g.n_seq.searchf.exe(g.n_seq,sn,0000010);
    LOG2("sn2="+sn);
    final __ptr__ result = sn!=null ? sn.node : null;
    LOG2("result="+result);
	return (ST_Agnode_s) result;
} finally {
LEAVING("bek79ccvjys1j9q404i3y6oh8","agnxtnode");
}
}




//3 17tu6ipvtgbjfrggkvyz3nasf
// Agnode_t *aglstnode(Agraph_t * g) 
public static Object aglstnode(Object... arg) {
UNSUPPORTED("4lnse8d2e11zapjwbkulyywtz"); // Agnode_t *aglstnode(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2llbfi4jrmre7cyhu90pgcm72"); //     Agsubnode_t *sn;
UNSUPPORTED("37449tqynatc8j0u8sohjqujf"); //     sn = (Agsubnode_t *) (*(((Dt_t*)(g->n_seq))->searchf))((g->n_seq),(void*)(0),0000400);
UNSUPPORTED("b550764xq8bvu8hoqv0fe2noi"); //     return sn ? sn->node : ((Agnode_t*)0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3qloij26jbl7m0ftyb0ouesq4
// Agnode_t *agprvnode(Agraph_t * g, Agnode_t * n) 
public static Object agprvnode(Object... arg) {
UNSUPPORTED("8ichcmu1fmaap5w9hqfiohi13"); // Agnode_t *agprvnode(Agraph_t * g, Agnode_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2llbfi4jrmre7cyhu90pgcm72"); //     Agsubnode_t *sn;
UNSUPPORTED("b0wzl2qtz6anq1dhlxtmvwvgn"); //     sn = agsubrep(g, n);
UNSUPPORTED("8efe1mjxltxjuin6v0msyzwfb"); //     if (sn) sn = ((Agsubnode_t *) (*(((Dt_t*)(g->n_seq))->searchf))((g->n_seq),(void*)(sn),0000020));
UNSUPPORTED("b550764xq8bvu8hoqv0fe2noi"); //     return sn ? sn->node : ((Agnode_t*)0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dzb7m0p5xsngvtyr8zs912og4
// static Agnode_t *newnode(Agraph_t * g, unsigned long id, unsigned long seq) 
public static ST_Agnode_s newnode(ST_Agraph_s g, int id, int seq) {
ENTERING("dzb7m0p5xsngvtyr8zs912og4","newnode");
try {
    ST_Agnode_s n;
    n = (ST_Agnode_s) ((__ptr__)agalloc(g, sizeof(ST_Agnode_s.class))).castTo(ST_Agnode_s.class);
    AGTYPE(n, AGNODE);
    AGID(n, id);
    AGSEQ(n, seq);
    n.setPtr("root", agroot(g));
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
public static void installnode(ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("4m26dpgaiw44hcleugjy71eus","installnode");
try {
	ST_Agsubnode_s sn;
    int osize;
    osize = dtsize_((ST_dt_s)g.n_id);
    if (EQ(g, agroot(g))) sn = (ST_Agsubnode_s) n.mainsub;
    else sn = (ST_Agsubnode_s) ((__ptr__)agalloc(g, sizeof(ST_Agsubnode_s.class))).castTo(ST_Agsubnode_s.class);
    sn.setPtr("node", n);
    g.n_id.searchf.exe(g.n_id,sn,0000001);
    g.n_seq.searchf.exe(g.n_seq,sn,0000001);
} finally {
LEAVING("4m26dpgaiw44hcleugjy71eus","installnode");
}
}




//3 3mfxjcaeepn8nitirs3yoqaed
// static void installnodetoroot(Agraph_t * g, Agnode_t * n) 
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




//3 1m6sl9df2yaolmufyq5i577a3
// Agnode_t *agidnode(Agraph_t * g, unsigned long id, int cflag) 
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




//3 acahmq5kvzn3o31mluqgw7q9p
// void agdelnodeimage(Agraph_t * g, Agnode_t * n, void *ignored) 
public static Object agdelnodeimage(Object... arg) {
UNSUPPORTED("elm2o1y1nn2deregqtwfd0fm"); // void agdelnodeimage(Agraph_t * g, Agnode_t * n, void *ignored)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("109gqpvjmuv5lwcih97x4uwqa"); //     Agedge_t *e, *f;
UNSUPPORTED("2v9cpnwjfya0wz9qq2q8rqx02"); //     static Agsubnode_t template;
UNSUPPORTED("8cy87pxkco1cies0ck9zpn66"); //     template.node = n;
UNSUPPORTED("4pgl4pn1cad2whf242bntmjre"); //     (void) ignored;
UNSUPPORTED("8dskgcobu9u3m4ejmwjq00r5m"); //     for (e = agfstedge(g, n); e; e = f) {
UNSUPPORTED("8tmx79zo3pcrz4238v132mjqg"); // 	f = agnxtedge(g, e, n);
UNSUPPORTED("ezikicsrlgi4g2anmg80iyxy5"); // 	agdeledgeimage(g, e, 0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5x2emh00yohuaush1f65cqnx3"); //     /* If the following lines are switched, switch the discpline using
UNSUPPORTED("35l6nf11aakz6sn2g7wfh60xr"); //      * free_subnode below.
UNSUPPORTED("5xkmfp82pyue09k1egerh5ezz"); //      */ 
UNSUPPORTED("a3umom4df7zkjo9g37dn0xnnl"); //     (*(((Dt_t*)(g->n_id))->searchf))((g->n_id),(void*)(&template),0000002);
UNSUPPORTED("enu2k7akluqzw4eos6263usdr"); //     (*(((Dt_t*)(g->n_seq))->searchf))((g->n_seq),(void*)(&template),0000002);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d7hac3hpizqk8mmx6oxiv6d3q
// int agdelnode(Agraph_t * g, Agnode_t * n) 
public static Object agdelnode(Object... arg) {
UNSUPPORTED("5vrhjcls5tltlk3dn4ssxzusq"); // int agdelnode(Agraph_t * g, Agnode_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("109gqpvjmuv5lwcih97x4uwqa"); //     Agedge_t *e, *f;
UNSUPPORTED("3ly27irmwairjdexym3up87uk"); //     if (!agfindnode_by_id(g, AGID(n)))
UNSUPPORTED("a1gf07w8a7uwaryezfqx6en21"); // 	return -1;		/* bad arg */
UNSUPPORTED("ackx3cor82a94trjk4owh3083"); //     if (g == agroot(g)) {
UNSUPPORTED("7n4rwpvryjg5anpy2d43bthxh"); // 	for (e = agfstedge(g, n); e; e = f) {
UNSUPPORTED("98oagw83x0w96uuzccetdi9ws"); // 	    f = agnxtedge(g, e, n);
UNSUPPORTED("7m6mvhicrmgkuyrwkvblruld0"); // 	    agdeledge(g, e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5247bml3o0pwzg9fc9q1xhhnc"); // 	if (g->desc.has_attrs)
UNSUPPORTED("d4mqrcccn3toqvhii6rjrrwwu"); // 	    agnodeattr_delete(n);
UNSUPPORTED("7zcf3kp28b1wgy3i2on67h98u"); // 	agmethod_delete(g, n);
UNSUPPORTED("eqkdptzmrk2vxj9fe3y4eb24l"); // 	agrecclose((Agobj_t *) n);
UNSUPPORTED("d6jhz9spbq2ywt2efhyikkcdi"); // 	agfreeid(g, AGNODE, AGID(n));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("86yxjnabv1fuv7zhtuhgex4gx"); //     if (agapply (g, (Agobj_t *) n, (agobjfn_t) agdelnodeimage, ((Agnode_t*)0), (0)) == 0) {
UNSUPPORTED("6tlwlx478gb1clm2fykihi2zk"); // 	if (g == agroot(g))
UNSUPPORTED("d4sehv0200tcylmbpt9pqc7h1"); // 	    agfree(g, n);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dytehp1u14cb4j9zsmlesojkq
// static void dict_relabel(Agnode_t * n, void *arg) 
public static Object dict_relabel(Object... arg) {
UNSUPPORTED("44mem1e9kck28s208xgn5g04k"); // static void dict_relabel(Agnode_t * n, void *arg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("8hr8p3jy96bfcwujkauwdvd92"); //     unsigned long new_id;
UNSUPPORTED("38tgkes5dhr4oloxpg73baq10"); //     g = agraphof(n);
UNSUPPORTED("5jv37sfftjuyu9m95lz2avmjk"); //     new_id = *(unsigned long *) arg;
UNSUPPORTED("90u0hwihh4q8uosu25ewbzhox"); //     (*(((Dt_t*)(g->n_id))->searchf))((g->n_id),(void*)(n),0000002);	/* wrong, should be subrep */
UNSUPPORTED("9d38am0gg0kj6jhq5tri5ac34"); //     AGID(n) = new_id;
UNSUPPORTED("3qkq6d6yv4tvsurangttsbn0z"); //     (*(((Dt_t*)(g->n_id))->searchf))((g->n_id),(void*)(n),0000001);	/* also wrong */
UNSUPPORTED("dwcdffxfxvt11kvrq2e8l9dg9"); //     /* because all the subgraphs share the same node now, this
UNSUPPORTED("8sf7kiafqj2eexpzvr30nmsle"); //        now requires a separate deletion and insertion phase */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a29io0pb5tx5bwevwjtr1hg1r
// int agrelabel_node(Agnode_t * n, char *newname) 
public static Object agrelabel_node(Object... arg) {
UNSUPPORTED("838qr3zz1vpfb75cfio36192j"); // int agrelabel_node(Agnode_t * n, char *newname)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("8hr8p3jy96bfcwujkauwdvd92"); //     unsigned long new_id;
UNSUPPORTED("5c0nzsud433f31yaxkbl4z4gs"); //     g = agroot(agraphof(n));
UNSUPPORTED("1vy801jhp4mbvo2tujvg565wz"); //     if (agfindnode_by_name(g, newname))
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("3swbd27n6ds70cn294m0ef8f5"); //     if (agmapnametoid(g, AGNODE, newname, &new_id, (!(0)))) {
UNSUPPORTED("5xgainykf6klfsmk4014aw0e2"); // 	if (agfindnode_by_id(agroot(g), new_id) == ((Agnode_t*)0)) {
UNSUPPORTED("5rjwy8imxqiwj9ia955vyoh9l"); // 	    agfreeid(g, AGNODE, AGID(n));
UNSUPPORTED("asbts6liah2fjm74ps7do0e1m"); // 	    agapply(g, (Agobj_t *) n, (agobjfn_t) dict_relabel,
UNSUPPORTED("9trirys0q5ojk3sb1jgw8tmdf"); // 		    (void *) &new_id, (0));
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("7w7v3j2z1voanzyg3oghk1o2t"); // 	    agfreeid(g, AGNODE, new_id);	/* couldn't use it after all */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eaoz5g3p9152utcqjz5d2fgdf"); //         /* obj* is unchanged, so no need to re agregister() */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8azkpi8o0wzdufa90lw8hpt6q"); //     return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d5farp22buvesyi4pydjam4g2
// Agnode_t *agsubnode(Agraph_t * g, Agnode_t * n0, int cflag) 
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




//3 awwiazixy9c76hvyxlkvvb3vo
// int agsubnodeidcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc) 
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




//3 41fjseux0nxzpr0aq7igym9ux
// int agsubnodeseqcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc) 
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




//3 a7tb3b1kvq6ykrxzhbaduvg9r
// static void free_subnode (Dt_t* d, Agsubnode_t* sn, Dtdisc_t * disc) 
public static Object free_subnode(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9e4h6d4hxsvsnaiuubzlmccsm"); // free_subnode (Dt_t* d, Agsubnode_t* sn, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eiac02o593gy0a55vv1w8mkmi"); //    if (!AGSNMAIN(sn)) 
UNSUPPORTED("263bmzd9ilyyeb9w34squ7iw8"); // 	agfree (sn->node->root, sn);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 us7d1n3fefkf0qyr6thv1sai
// Dtdisc_t Ag_subnode_id_disc = 
/*public static final __struct__<_dtdisc_s> Ag_subnode_id_disc = JUtils.from(_dtdisc_s.class);
static {
	Ag_subnode_id_disc.setInt("key", 0);
	Ag_subnode_id_disc.setInt("size", 0);
	Ag_subnode_id_disc.setInt("link", OFFSET.create(Agsubnode_s.class, "id_link").toInt()); // id_link is the second field in Agsubnode_t
	Ag_subnode_id_disc.setPtr("makef", null);
	Ag_subnode_id_disc.setPtr("freef", null);
	Ag_subnode_id_disc.setPtr("comparf", function(node__c.class, "agsubnodeidcmpf"));
	Ag_subnode_id_disc.setPtr("hashf", null);
	Ag_subnode_id_disc.setPtr("memoryf", function(utils__c.class, "agdictobjmem"));
	Ag_subnode_id_disc.setPtr("eventf", null);
}*/

//1 3gqjvodjfsv6wz1tk75zy19p9
// Dtdisc_t Ag_subnode_seq_disc = 
/*public static final __struct__<_dtdisc_s> Ag_subnode_seq_disc = JUtils.from(_dtdisc_s.class);
static {
	Ag_subnode_seq_disc.setInt("key", 0);
	Ag_subnode_seq_disc.setInt("size", 0);
	Ag_subnode_seq_disc.setInt("link", OFFSET.create(Agsubnode_s.class, "seq_link").toInt()); // link is the first field in Agsubnode_t
	Ag_subnode_seq_disc.setPtr("makef", null);
	Ag_subnode_seq_disc.setPtr("freef", function(node__c.class, "free_subnode"));
	Ag_subnode_seq_disc.setPtr("comparf", function(node__c.class, "agsubnodeseqcmpf"));
	Ag_subnode_seq_disc.setPtr("hashf", null);
	Ag_subnode_seq_disc.setPtr("memoryf", function(utils__c.class, "agdictobjmem"));
	Ag_subnode_seq_disc.setPtr("eventf", null);
}*/

}
