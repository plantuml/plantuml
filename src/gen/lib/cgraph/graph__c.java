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
import static gen.lib.cdt.dtextract__c.dtextract;
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static gen.lib.cdt.dtsize__c.dtsize_;
import static gen.lib.cgraph.attr__c.agraphattr_init;
import static gen.lib.cgraph.edge__c.agsubrep;
import static gen.lib.cgraph.id__c.agmapnametoid;
import static gen.lib.cgraph.id__c.agregister;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agmethod_init;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.subg__c.agparent;
import static gen.lib.cgraph.utils__c.agdtopen;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ASINT;
import static smetana.core.Macro.N;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agclos_s;
import h.ST_Agdesc_s;
import h.ST_Agdisc_s;
import h.ST_Agmemdisc_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsubnode_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtlink_s;
import smetana.core.ACCESS;
import smetana.core.CString;
import smetana.core.STARSTAR;
import smetana.core.Z;
import smetana.core.__ptr__;

public class graph__c {
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


//1 ix101dcoysqmkv8bgsxsq8u1
// Agraph_t *Ag_G_global


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


//1 ewderv8bx65jlxf61iw8en77s
// const char AgraphVersion[] = 




//3 bb2bu9iyqx0u6xx44l282vmch
// static Agclos_t *agclos(Agdisc_t * proto) 
public static ST_Agclos_s agclos(ST_Agdisc_s proto) {
ENTERING("bb2bu9iyqx0u6xx44l282vmch","agclos");
try {
	ST_Agmemdisc_s memdisc;
		__ptr__  memclosure;
		ST_Agclos_s rv;
		/* establish an allocation arena */
		memdisc = (ST_Agmemdisc_s) ((proto != null && proto.mem != null) ? proto.mem : Z.z().AgMemDisc);
		memclosure = (__ptr__) memdisc.open.exe(proto);
		rv = (ST_Agclos_s) memdisc.alloc.exe(memclosure, sizeof(ST_Agclos_s.class));
		rv.disc.setPtr("mem", memdisc);
		rv.state.setPtr("mem", memclosure);
		rv.disc.setPtr("id", ((proto != null && proto.id != null) ? proto.id : Z.z().AgIdDisc));
		// Translation bug in next line: should be AgIoDisc and not AgIdDisc
		// rv.disc.setPtr("io", ((proto != null && proto.getPtr("io") != null) ? proto.getPtr("io") : Z.z().AgIoDisc));
		rv.callbacks_enabled = (N(0));
		return rv;
} finally {
LEAVING("bb2bu9iyqx0u6xx44l282vmch","agclos");
}
}




//3 d5yqn56yii8cdoahswt4n6bug
// Agraph_t *agopen(char *name, Agdesc_t desc, Agdisc_t * arg_disc) 
public static ST_Agraph_s agopen(CString name, final ST_Agdesc_s desc, ST_Agdisc_s arg_disc) {
// WARNING!! STRUCT
return agopen_w_(name, (ST_Agdesc_s) desc.copy(), arg_disc);
}
private static ST_Agraph_s agopen_w_(CString name, final ST_Agdesc_s desc, ST_Agdisc_s arg_disc) {
ENTERING("d5yqn56yii8cdoahswt4n6bug","agopen");
try {
		ST_Agraph_s g;
		ST_Agclos_s clos;
		int gid[] = new int[1];
		clos = agclos(arg_disc);
		g = (ST_Agraph_s) clos.disc.mem.alloc.exe(clos.state.mem, sizeof(ST_Agraph_s.class));
    	AGTYPE(g, AGRAPH);
		g.setPtr("clos", clos);
		g.setStruct("desc", desc);
		((ST_Agdesc_s)g.desc).maingraph = ASINT((N(0)));
		g.setPtr("root", g);
		g.clos.state.setPtr("id", (__ptr__) g.clos.disc.id.open.exe(g, arg_disc));
		 if (agmapnametoid(g, AGRAPH, name, gid, (N(0)))!=0)
		   AGID(g, gid[0]);
		// /* else AGID(g) = 0 because we have no alternatives */
		g = agopen1(g);
		agregister(g, AGRAPH, g);
		return g;
} finally {
LEAVING("d5yqn56yii8cdoahswt4n6bug","agopen");
}
}




//3 8jyhwfdfm0a877qfz8cjlb8rk
// Agraph_t *agopen1(Agraph_t * g) 
public static ST_Agraph_s agopen1(ST_Agraph_s g) {
ENTERING("8jyhwfdfm0a877qfz8cjlb8rk","agopen1");
try {
    ST_Agraph_s par;
    g.setPtr("n_seq", agdtopen(g, Z.z().Ag_subnode_seq_disc, Z.z().Dttree));
    g.setPtr("n_id", agdtopen(g, Z.z().Ag_subnode_id_disc, Z.z().Dttree));
    g.setPtr("e_seq", agdtopen(g, EQ(g, agroot(g))? Z.z().Ag_mainedge_seq_disc : Z.z().Ag_subedge_seq_disc, Z.z().Dttree));
    g.setPtr("e_id", agdtopen(g, EQ(g, agroot(g))? Z.z().Ag_mainedge_id_disc : Z.z().Ag_subedge_id_disc, Z.z().Dttree));
    g.setPtr("g_dict", agdtopen(g, Z.z().Ag_subgraph_id_disc, Z.z().Dttree));
    par = agparent(g);
    if (par!=null) {
	AGSEQ(g, agnextseq(par, AGRAPH));
  	par.g_dict.searchf.exe(par.g_dict,g,0000001);
    }				/* else AGSEQ=0 */
    if (N(par) || ((ST_Agdesc_s)par.desc).has_attrs!=0)
	agraphattr_init(g);
    agmethod_init(g, g);
    return g;
} finally {
LEAVING("8jyhwfdfm0a877qfz8cjlb8rk","agopen1");
}
}




//3 dmhavsadfjootm2o8lnwizndm
// int agclose(Agraph_t * g) 
public static Object agclose(Object... arg) {
UNSUPPORTED("afwvyph6n53ckvxm8d8h7mfb8"); // int agclose(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bf3nmka0aaoswb2zwm0qee15o"); //     Agraph_t *subg, *next_subg, *par;
UNSUPPORTED("7qo66ph77ke8gsbowf6kwqjff"); //     Agnode_t *n, *next_n;
UNSUPPORTED("d7yov7q4cj5xaglc5czdcnix5"); //     par = agparent(g);
UNSUPPORTED("1u7cs2kb7x42iwswgskdrpk5m"); //     if ((par == ((Agraph_t*)0)) && (((g)->clos->disc.mem)->close)) {
UNSUPPORTED("6yjypc20njwrfp5bsnjhiiabf"); // 	/* free entire heap */
UNSUPPORTED("cmba0hmq318rx9h0jefkyen70"); // 	agmethod_delete(g, g);	/* invoke user callbacks */
UNSUPPORTED("dbiair9ce3vkfb8s9l08pfx6w"); // 	agfreeid(g, AGRAPH, AGID(g));
UNSUPPORTED("2bdjy9rtybb0v767umxcnz6rs"); // 	((g)->clos->disc.mem)->close(((g)->clos->state.mem));	/* whoosh */
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2hk83bjq106e9rdcpxqbv9nnl"); //     for (subg = agfstsubg(g); subg; subg = next_subg) {
UNSUPPORTED("608ihpc2s8xb39yo654s19zxd"); // 	next_subg = agnxtsubg(subg);
UNSUPPORTED("3noc43t4fqi4gollim1ygyuqh"); // 	agclose(subg);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6r3mocyf2tlkysyu64nxw0u9h"); //     for (n = agfstnode(g); n; n = next_n) {
UNSUPPORTED("3d2h3vjjw6x8w1joyvc3qlruy"); // 	next_n = agnxtnode(g, n);
UNSUPPORTED("3xjgsp211uvaug1aa3mvpdlnc"); // 	agdelnode(g, n);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1ia10h9dh09qjfarfdjx452gf"); //     aginternalmapclose(g);
UNSUPPORTED("ddezjv0si4sjtexy5kqfwqg9n"); //     agmethod_delete(g, g);
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5d72jwytwy7gvtmqynj5ndpyr"); //     if (agdtclose(g, g->n_id)) return -1;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("324yaisi4aejlbofpo08bx36u"); //     if (agdtclose(g, g->n_seq)) return -1;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("34xteu7bflgwy03788khpb2gb"); //     if (agdtclose(g, g->e_id)) return -1;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("6bwledkxe6algose4ob82o61"); //     if (agdtclose(g, g->e_seq)) return -1;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("d7yg3wo8tmofx0anjr742k191"); //     if (agdtclose(g, g->g_dict)) return -1;
UNSUPPORTED("l0tanhlxt2jokflxnd061z3y"); //     if (g->desc.has_attrs)
UNSUPPORTED("9jhznqh28rajdovcc58834278"); // 	if (agraphattr_delete(g)) return -1;
UNSUPPORTED("a5y6rvdlz9o09pphxz38sbtna"); //     agrecclose((Agobj_t *) g);
UNSUPPORTED("crfg36z5yetflihtijtubwo8y"); //     agfreeid(g, AGRAPH, AGID(g));
UNSUPPORTED("et9lswrohfsxngrn2xcefry4q"); //     if (par) {
UNSUPPORTED("dzboxj0ijphtqrm463tpbvkhx"); // 	agdelsubg(par, g);
UNSUPPORTED("e7v29f5dzfhzrj9v4shzbcywi"); // 	agfree(par, g);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("5y8tww8901b1ro7bgu6r58vgr"); // 	Agmemdisc_t *memdisc;
UNSUPPORTED("6ozvxgrg2q3sua4w4cnwun2hd"); // 	void *memclos, *clos;
UNSUPPORTED("9i0i3wyt8alu21zy4mtvxxhj7"); // 	while (g->clos->cb)
UNSUPPORTED("1y6k38rbnyl26lquznq5kass6"); // 	    agpopdisc(g, g->clos->cb->f);
UNSUPPORTED("8qcjv2uq7ztij51cy8b5r7yqr"); // 	((g)->clos->disc.id)->close(((g)->clos->state.id));
UNSUPPORTED("px95fp6paiia8ts33pk4tph1"); // 	if (agstrclose(g)) return -1;
UNSUPPORTED("2kp9gdc0xn3li7ibgz4x4lnmz"); // 	memdisc = ((g)->clos->disc.mem);
UNSUPPORTED("8lo1wjoiak85adsa9fwxo62zl"); // 	memclos = ((g)->clos->state.mem);
UNSUPPORTED("8n6pjmho9f28hof6v2v1lruyo"); // 	clos = g->clos;
UNSUPPORTED("ets9jwr303m5yl6eqowve1loh"); // 	(memdisc->free) (memclos, g);
UNSUPPORTED("d0v33cnucd6c13avcqkwy4wzc"); // 	(memdisc->free) (memclos, clos);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 axmdmml95l55vlp1vqmh0v5sn
// unsigned long agnextseq(Agraph_t * g, int objtype) 
public static int agnextseq(ST_Agraph_s g, int objtype) {
ENTERING("axmdmml95l55vlp1vqmh0v5sn","agnextseq");
try {
	int tmp = g.clos.seq[objtype];
	tmp++;
	g.clos.seq[objtype]=tmp;
	return tmp;
} finally {
LEAVING("axmdmml95l55vlp1vqmh0v5sn","agnextseq");
}
}




//3 688euygrkbl10cveflgwalo2n
// int agnnodes(Agraph_t * g) 
public static int agnnodes(ST_Agraph_s g) {
ENTERING("688euygrkbl10cveflgwalo2n","agnnodes");
try {
    return dtsize_((ST_dt_s)g.n_id);
} finally {
LEAVING("688euygrkbl10cveflgwalo2n","agnnodes");
}
}




//3 8zjne7uv8rfpmbv5t96zhnr4u
// int agnedges(Agraph_t * g) 
public static int agnedges(ST_Agraph_s g) {
ENTERING("8zjne7uv8rfpmbv5t96zhnr4u","agnedges");
try {
    ST_Agnode_s n;
    int rv = 0;
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n))
	rv += agdegree(g, n, (false), (N(0)));	/* must use OUT to get self-arcs */
    return rv;
} finally {
LEAVING("8zjne7uv8rfpmbv5t96zhnr4u","agnedges");
}
}




//3 e1ndua2eo29tb0z93wrmamm3g
// int agnsubg(Agraph_t * g) 
public static Object agnsubg(Object... arg) {
UNSUPPORTED("5auwvgl2zekzvzu6p5413tqd0"); // int agnsubg(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("18u3kziious357fry7i0r4kg2"); // 	return dtsize(g->g_dict);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 blvn1w3v0icnucu5m5xvbrba1
// int agisdirected(Agraph_t * g) 
public static boolean agisdirected(ST_Agraph_s g) {
ENTERING("blvn1w3v0icnucu5m5xvbrba1","agisdirected");
try {
    return ((ST_Agdesc_s)g.desc).directed!=0;
} finally {
LEAVING("blvn1w3v0icnucu5m5xvbrba1","agisdirected");
}
}




//3 8thgds4eioot64flko26m8ns0
// int agisundirected(Agraph_t * g) 
public static boolean agisundirected(ST_Agraph_s g) {
ENTERING("8thgds4eioot64flko26m8ns0","agisundirected");
try {
    return (NOT(agisdirected(g)));
} finally {
LEAVING("8thgds4eioot64flko26m8ns0","agisundirected");
}
}




//3 9qgdebmdfrcfjm394bg59a7y5
// int agisstrict(Agraph_t * g) 
public static boolean agisstrict(ST_Agraph_s g) {
ENTERING("9qgdebmdfrcfjm394bg59a7y5","agisstrict");
try {
    return ((ST_Agdesc_s)g.desc).strict!=0;
} finally {
LEAVING("9qgdebmdfrcfjm394bg59a7y5","agisstrict");
}
}




//3 4zw0onm78e3x5anx7snfpe40m
// int agissimple(Agraph_t * g) 
public static Object agissimple(Object... arg) {
UNSUPPORTED("5khld3f380yzb9kztjfa00b7t"); // int agissimple(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9u553zzb7in07zdd55sdea2an"); //     return (g->desc.strict && g->desc.no_loop);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 abaldeo2ie6zi60cazxp7rv47
// static int cnt(Dict_t * d, Dtlink_t ** set) 
public static int cnt(ST_dt_s d, STARSTAR<ST_dtlink_s> set) {
ENTERING("abaldeo2ie6zi60cazxp7rv47","cnt");
try {
	int rv;
    dtrestore(d, set.getMe());
    rv = dtsize_(d);
    set.setMe(dtextract(d));
	return rv;
} finally {
LEAVING("abaldeo2ie6zi60cazxp7rv47","cnt");
}
}




//3 crupee5rve7q7m335ngnqsb39
// int agcountuniqedges(Agraph_t * g, Agnode_t * n, int want_in, int want_out) 
public static Object agcountuniqedges(Object... arg) {
UNSUPPORTED("47c9iab9p596xa2xrkcgmepw0"); // int agcountuniqedges(Agraph_t * g, Agnode_t * n, int want_in, int want_out)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("36vshotvjkc5iodgg7nq6qa2r"); //     Agedge_t *e;
UNSUPPORTED("2llbfi4jrmre7cyhu90pgcm72"); //     Agsubnode_t *sn;
UNSUPPORTED("en7ch189nkys76f42mlo1s5zz"); //     int rv = 0;
UNSUPPORTED("b0wzl2qtz6anq1dhlxtmvwvgn"); //     sn = agsubrep(g, n);
UNSUPPORTED("e0cr7vhmu27121z5m8qtchlwn"); //     if (want_out) rv = cnt(g->e_seq,&(sn->out_seq));
UNSUPPORTED("4gu3qg6aqwexl6ysrfrqko4z8"); //     if (want_in) {
UNSUPPORTED("3r5t38hbcwvc5hpus6062r7ic"); // 		if (!want_out) rv += cnt(g->e_seq,&(sn->in_seq));	/* cheap */
UNSUPPORTED("6p5yaaxfj7183iw2v0uuruh56"); // 		else {	/* less cheap */
UNSUPPORTED("7jxxmlwkqih7nv6yrum6qhfe0"); // 			for (e = agfstin(g, n); e; e = agnxtin(g, e))
UNSUPPORTED("73lyxs2pp1e0s95qdz9vgc5iy"); // 				if (e->node != n) rv++;  /* don't double count loops */
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2bz40qf0qo7pd6er1ut25gthp
// int agdegree(Agraph_t * g, Agnode_t * n, int want_in, int want_out) 
public static int agdegree(ST_Agraph_s g, ST_Agnode_s n, boolean want_in, boolean want_out) {
ENTERING("2bz40qf0qo7pd6er1ut25gthp","agdegree");
try {
	ST_Agsubnode_s sn;
    int rv = 0;
    sn = agsubrep(g, n);
    final ST_Agsubnode_s sn1 = sn;
    if (sn!=null) {
    	if (want_out) rv += cnt((ST_dt_s)g.e_seq,
    			STARSTAR.amp(new ACCESS<ST_dtlink_s>() {
    				public ST_dtlink_s get() {
    					return (ST_dtlink_s) sn1.out_seq;
    				}
    				public void set(ST_dtlink_s obj) {
    					sn1.setPtr("out_seq", obj);
    				}})
    			);
    	if (want_in) rv += cnt((ST_dt_s)g.e_seq,
    			STARSTAR.amp(new ACCESS<ST_dtlink_s>() {
    				public ST_dtlink_s get() {
    					return (ST_dtlink_s) sn1.in_seq;
    				}
    				public void set(ST_dtlink_s obj) {
    					sn1.setPtr("in_seq", obj);
    				}})
    			);
    }
	return rv;
} finally {
LEAVING("2bz40qf0qo7pd6er1ut25gthp","agdegree");
}
}




//3 dhbtfzzp8n5yygqmhmluo9bxl
// int agraphidcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc) 
public static int agraphidcmpf(ST_dt_s d, ST_Agraph_s arg0, ST_Agraph_s arg1, ST_dtdisc_s disc) {
ENTERING("dhbtfzzp8n5yygqmhmluo9bxl","agraphidcmpf");
try {
    int v;
    ST_Agraph_s sg0, sg1;
    sg0 = (ST_Agraph_s) arg0;
    sg1 = (ST_Agraph_s) arg1;
    v = (AGID(sg0) - AGID(sg1));
    return ((v==0)?0:(v<0?-1:1));
} finally {
LEAVING("dhbtfzzp8n5yygqmhmluo9bxl","agraphidcmpf");
}
}




//3 llkcwaxuse8jc2ri7r9n6t0c
// int agraphseqcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc) 
public static Object agraphseqcmpf(Object... arg) {
UNSUPPORTED("97lu4ei4gjam66ku5pz8dn7il"); // int agraphseqcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ccl2joincnprtk47hwmpz1o7n"); //     long	v;
UNSUPPORTED("73bfha5x7xhgp9p6wxa9jap6j"); //     Agraph_t *sg0, *sg1;
UNSUPPORTED("e8rx1ahgpoym3u3v0jgarn58y"); //     sg0 = (Agraph_t *) arg0;
UNSUPPORTED("bc6x70wml3jh4l4ana92njtid"); //     sg1 = (Agraph_t *) arg1;
UNSUPPORTED("4afy6g5l0jng6m6l3abdyuk80"); //     v = (AGSEQ(sg0) - AGSEQ(sg1));
UNSUPPORTED("2tgj1svqq4v5mqo7525nw7icj"); //     return ((v==0)?0:(v<0?-1:1));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 cqgilvgau98cgaulohsii8vx4
// Dtdisc_t Ag_subgraph_id_disc = 
/*static final public __struct__<_dtdisc_s> Ag_subgraph_id_disc = JUtils.from(_dtdisc_s.class);
static {
	Ag_subgraph_id_disc.setInt("key", 0);
	Ag_subgraph_id_disc.setInt("size", 0);
	Ag_subgraph_id_disc.setInt("link", OFFSET.create(Agraph_s.class, "link").toInt()); // link is the third field in Agraph_t
	Ag_subgraph_id_disc.setPtr("makef", null);
	Ag_subgraph_id_disc.setPtr("freef", null);
	Ag_subgraph_id_disc.setPtr("comparf", function(graph__c.class, "agraphidcmpf"));
	Ag_subgraph_id_disc.setPtr("hashf", null);
	Ag_subgraph_id_disc.setPtr("memoryf", function(utils__c.class, "agdictobjmem"));
	Ag_subgraph_id_disc.setPtr("eventf", null);
}*/

//1 98aldesvg4i0qxoidbuanebv7
// Agdesc_t Agdirected = 
/*static final public __struct__<Agdesc_s> Agdirected = JUtils.from(Agdesc_s.class);
static {
	Agdirected.setInt("directed", 1);
	Agdirected.setInt("strict", 0);
	Agdirected.setInt("no_loop", 0);
	Agdirected.setInt("maingraph", 1);
}*/

//1 4fbe4dfrxvwi5l1l4rb30s9o8
// Agdesc_t Agstrictdirected = 


//1 5rysra3mrm6tscdrjbg5rhyuu
// Agdesc_t Agundirected = 


//1 2x0008zd99c6pdbwdqnv7yjcz
// Agdesc_t Agstrictundirected = 


//1 biws2qqe0e0xqmwdmfuvdopo3
// Agdisc_t AgDefaultDisc = 




//3 4rhqd5bl4tiypdakk2hhpsj7s
// void scndump(Agraph_t *g, char *file) 
public static Object scndump(Object... arg) {
UNSUPPORTED("7e937yycgb0eiorckxpq4qqwo"); // void scndump(Agraph_t *g, char *file)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("38goe38ctv7d6jktnwysyagy8"); // 	FILE * f = fopen(file,"w");
UNSUPPORTED("7luati80gyuf0ex40qi3bjkkp"); // 	if (f) {agwrite(g,f); fclose(f);}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
