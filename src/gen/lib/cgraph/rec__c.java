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
import static gen.lib.cgraph.edge__c.agopp;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.refstr__c.agstrdup;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGDATA;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ASINT;
import static smetana.core.Macro.N;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agedge_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agrec_s;
import h.ST_Agtag_s;
import smetana.core.CString;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class rec__c {
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




//3 62z9z5vraa2as0c9t108j9xaf
// static void set_data(Agobj_t * obj, Agrec_t * data, int mtflock) 
public static void set_data(ST_Agobj_s obj, ST_Agrec_s data, int mtflock) {
ENTERING("62z9z5vraa2as0c9t108j9xaf","set_data");
try {
    ST_Agedge_s e;
    obj.setPtr("data", data);
    ((ST_Agtag_s)obj.tag).mtflock = mtflock;
    if ((AGTYPE(obj) == AGINEDGE) || (AGTYPE(obj) == AGOUTEDGE)) {
	e = (ST_Agedge_s) agopp(obj.castTo(ST_Agedge_s.class));
	AGDATA(e, data);
	((ST_Agtag_s)e.base.tag).mtflock = mtflock;
    }
} finally {
LEAVING("62z9z5vraa2as0c9t108j9xaf","set_data");
}
}




//3 7p2ne3oknmyclvsw4lh3axtd8
// Agrec_t *aggetrec(void *obj, char *name, int mtf) 
public static ST_Agrec_s aggetrec(__ptr__ obj, CString name, boolean mtf) {
ENTERING("7p2ne3oknmyclvsw4lh3axtd8","aggetrec");
try {
    ST_Agobj_s hdr;
    ST_Agrec_s d, first;
    hdr = (ST_Agobj_s) obj.castTo(ST_Agobj_s.class);
    first = d = (ST_Agrec_s) hdr.data;
    while (d!=null) {
	if (N(strcmp(name,d.name)))
	    break;
	d = (ST_Agrec_s) d.next;
	if (EQ(d, first)) {
	    d = null;
	    break;
	}
    }
    if (d!=null) {
	if (((ST_Agtag_s)hdr.tag).mtflock!=0) {
	    if (mtf && NEQ(hdr.data, d))
		System.err.println("move to front lock inconsistency");
	} else {
	    if (NEQ(d, first) || (mtf != ((((ST_Agtag_s)hdr.tag).mtflock)!=0)))
		set_data(hdr, d, ASINT(mtf));	/* Always optimize */
	}
    }
    return d;
} finally {
LEAVING("7p2ne3oknmyclvsw4lh3axtd8","aggetrec");
}
}




//3 7sk4k5ipm2jnd244556g1kr6
// static void objputrec(Agraph_t * g, Agobj_t * obj, void *arg) 
public static void objputrec(ST_Agraph_s g, ST_Agobj_s obj, Object arg) {
ENTERING("7sk4k5ipm2jnd244556g1kr6","objputrec");
try {
	ST_Agrec_s firstrec, newrec;
    newrec = (ST_Agrec_s) arg;
    firstrec = (ST_Agrec_s) obj.data;
    if (firstrec == null)
	newrec.next = newrec;	/* 0 elts */
    else {
	if (EQ(firstrec.next, firstrec)) {
	    firstrec.next = newrec;	/* 1 elt */
	    newrec.next = firstrec;
	} else {
	    newrec.next = firstrec.next;
	    firstrec.next = newrec;
	}
    }
    if (NOT(((ST_Agtag_s)obj.tag).mtflock))
	set_data(obj, newrec, (0));
} finally {
LEAVING("7sk4k5ipm2jnd244556g1kr6","objputrec");
}
}




//3 dmh5i83l15mnn1pnu6f5dfv8l
// void *agbindrec(void *arg_obj, char *recname, unsigned int recsize, 		int mtf) 
public static __ptr__ agbindrec(__ptr__ arg_obj, CString recname, size_t recsize, boolean mtf) {
ENTERING("dmh5i83l15mnn1pnu6f5dfv8l","agbindrec");
try {
    ST_Agraph_s g;
    ST_Agobj_s obj;
    ST_Agrec_s rec;
    obj = (ST_Agobj_s) arg_obj.castTo(ST_Agobj_s.class);
    g = agraphof(obj);
    rec = aggetrec(obj, recname, false);
    if ((rec == null && recsize.isStrictPositive())) {
	rec = (ST_Agrec_s) ((__ptr__)agalloc(g, recsize)).castTo(ST_Agrec_s.class);
    // rec = (Agrec_s) Memory.malloc(Agrec_s.class);
	rec.setPtr("name", agstrdup(g, recname));
	switch (((ST_Agtag_s)obj.tag).objtype) {
	case AGRAPH:
	    objputrec(g, obj, rec);
	    break;
	case AGNODE:
	    objputrec(g, obj, rec);
	    break;
	case AGINEDGE:
	case AGOUTEDGE:
	    objputrec(g, obj, rec);
	    break;
	}
    }
    if (mtf)
	aggetrec(arg_obj, recname, (N(0)));
    return rec;
} finally {
LEAVING("dmh5i83l15mnn1pnu6f5dfv8l","agbindrec");
}
}




//3 7wkmd0z867cfhifu5sx3f9qk0
// static void objdelrec(Agraph_t * g, Agobj_t * obj, void *arg_rec) 
public static Object objdelrec(Object... arg) {
UNSUPPORTED("50zcyap75kyq5dged87b27eux"); // static void objdelrec(Agraph_t * g, Agobj_t * obj, void *arg_rec)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("emb5aoborhmx7xgff2s0f32r7"); //     Agrec_t *rec = (Agrec_t *) arg_rec, *newrec;
UNSUPPORTED("a3ypwtn30jl38mmwqqe7cncxj"); //     if (obj->data == rec) {
UNSUPPORTED("69qy6kkaiflhbig0kizfx2tti"); // 	if (rec->next == rec)
UNSUPPORTED("xd727mslmxprqxq2rm02c5d8"); // 	    newrec = ((Agrec_t *)0);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("6w6e40pynrhr4cg19g4qnwmeg"); // 	    newrec = rec->next;
UNSUPPORTED("boyfn5td5ma9qwuqx4rb00zxn"); // 	set_data(obj, newrec, (0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9lrcdtq3amx34ixpjad1kj9w1
// static void listdelrec(Agobj_t * obj, Agrec_t * rec) 
public static Object listdelrec(Object... arg) {
UNSUPPORTED("5vg9c18o4hibwg5a9p47ad9i"); // static void listdelrec(Agobj_t * obj, Agrec_t * rec)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8e7x54av78ukk2gkru2uv7n5t"); //     Agrec_t *prev;
UNSUPPORTED("acnbs96yxc5n81gdoim603six"); //     prev = obj->data;
UNSUPPORTED("j2ssgapam83gwl5s62grl1fg"); //     while (prev->next != rec) {
UNSUPPORTED("4l4pl9wqsuoczyfdlvmzj1gli"); // 	prev = prev->next;
UNSUPPORTED("bt8qt5d94yexozgdbv5sspmm4"); // 	;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1uagqrrdyb18p3s9qlqg8jwjj"); //     /* following is a harmless no-op if the list is trivial */
UNSUPPORTED("erdqdxkn41xuppe0chxcrx73"); //     prev->next = rec->next;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dr7dc2ebvb106hcsaxz6elhu9
// int agdelrec(void *arg_obj, char *name) 
public static Object agdelrec(Object... arg) {
UNSUPPORTED("8scieaa3q398qeq2uv44asga1"); // int agdelrec(void *arg_obj, char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("14h5et4t79wqu2qo8divf5tkt"); //     Agobj_t *obj;
UNSUPPORTED("146agbqco6st2hn1ajcek08r6"); //     Agrec_t *rec;
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("ami3me63drun9ofwwmxainjy5"); //     obj = (Agobj_t *) arg_obj;
UNSUPPORTED("8pfuk9ua4x9bh68zk1kzwc5t9"); //     g = agraphof(obj);
UNSUPPORTED("dhmaidfkewgpedp1d73ef9yx5"); //     rec = aggetrec(obj, name, (0));
UNSUPPORTED("epwrmjxhdofey6itrewqzilu0"); //     if (rec) {
UNSUPPORTED("q964zfpibwk1nuufmhbvhvz1"); // 	listdelrec(obj, rec);	/* zap it from the circular list */
UNSUPPORTED("bxainz8fp03zcbgxzia4t1ta2"); // 	switch (obj->tag.objtype) {	/* refresh any stale pointers */
UNSUPPORTED("9t6es77h0301xk4n035emz6o"); // 	case AGRAPH:
UNSUPPORTED("39koq493kdonzfnv7y451gzku"); // 	    objdelrec(g, obj, rec);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4ih7nalu307xv8wvdpmgy537r"); // 	case AGNODE:
UNSUPPORTED("f0mphr0n2ielt8cpkw0djd9s9"); // 	case AGINEDGE:
UNSUPPORTED("3zbev1w97o9vgmm1ifhakk5g6"); // 	case AGOUTEDGE:
UNSUPPORTED("9ixqdoo4u64vgpl11kitdhiwj"); // 	    agapply(agroot(g), obj, objdelrec, rec, (0));
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("b0419fuh4e7iigkr04vrhwoqw"); // 	agstrfree(g, rec->name);
UNSUPPORTED("9w293r9fg650cqzb27hlm4gm6"); // 	agfree(g, rec);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 61hbvi8qf9sf7fp8zpov061px
// static void simple_delrec(Agraph_t * g, Agobj_t * obj, void *rec_name) 
public static Object simple_delrec(Object... arg) {
UNSUPPORTED("7jivm8vgxrik7o19yqyy4iuoj"); // static void simple_delrec(Agraph_t * g, Agobj_t * obj, void *rec_name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3lu3vc6o8prg1gr2vk05e0eko"); //     agdelrec(obj, rec_name);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e1pnypxtha6b6f6gdnys37746
// void aginit(Agraph_t * g, int kind, char *rec_name, int arg_rec_size, int mtf) 
public static Object aginit(Object... arg) {
UNSUPPORTED("2pn3c5mfp7ik08786jysv3lbs"); // void aginit(Agraph_t * g, int kind, char *rec_name, int arg_rec_size, int mtf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("36vshotvjkc5iodgg7nq6qa2r"); //     Agedge_t *e;
UNSUPPORTED("8a4qjwfuqqw6rztukk1hbffvl"); //     Agraph_t *s;
UNSUPPORTED("abzwllf3yvcaghy0hkur9oxfd"); // 	int		 rec_size;
UNSUPPORTED("5vb74w15wh9g0ykij86iwtxeb"); // 	int		 recur;	/* if recursive on subgraphs */
UNSUPPORTED("4nlgepehelmt97qydjqp7ecah"); // 	if (arg_rec_size < 0) {recur = 1; rec_size = -arg_rec_size;}
UNSUPPORTED("bevbs1i4v9cvng7nnrs2yqiku"); // 	else {recur = 0; rec_size= arg_rec_size;}
UNSUPPORTED("elb9jvgldg2htymh7c74smj7o"); //     switch (kind) {
UNSUPPORTED("eyna33dobiebmtd0nihpgura4"); //     case AGRAPH:
UNSUPPORTED("9k3pxutrp61xdp73x92pxsv8j"); // 	agbindrec(g, rec_name, rec_size, mtf);
UNSUPPORTED("ufmow0oztf5sdbx36wf4v0e5"); // 	if (recur)
UNSUPPORTED("4oknzxynn26rak0dbvijyz5t"); // 		for (s = agfstsubg(g); s; s = agnxtsubg(s))
UNSUPPORTED("3hu696w8d3t3l7tygoq4twr4x"); // 			aginit(s,kind,rec_name,arg_rec_size,mtf);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("6x7ztvlgv763oeop84udp1egg"); //     case AGNODE:
UNSUPPORTED("a6ls4lkgjoheqwo2e7yqt9zz8"); //     case AGOUTEDGE:
UNSUPPORTED("c9o8hr6x8n5dty1y3eej1fept"); //     case AGINEDGE:
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("2kxod9hw7p0o5crbt6xh2gcwy"); // 	    if (kind == AGNODE)
UNSUPPORTED("7lbtjiuf91mfv1rfh26gr9rnf"); // 		agbindrec(n, rec_name, rec_size, mtf);
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("5qry6mw56jhkh965gr6si5sjz"); // 		for (e = agfstout(g, n); e; e = agnxtout(g, e))
UNSUPPORTED("3isk98kl2hyz50xh5nk9e79ur"); // 		    agbindrec(e, rec_name, rec_size, mtf);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d8dlatsnpytjohjptji50kek2
// void agclean(Agraph_t * g, int kind, char *rec_name) 
public static Object agclean(Object... arg) {
UNSUPPORTED("2u4lls7xvbemdliclfn1jjlv3"); // void agclean(Agraph_t * g, int kind, char *rec_name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("36vshotvjkc5iodgg7nq6qa2r"); //     Agedge_t *e;
UNSUPPORTED("elb9jvgldg2htymh7c74smj7o"); //     switch (kind) {
UNSUPPORTED("eyna33dobiebmtd0nihpgura4"); //     case AGRAPH:
UNSUPPORTED("p5rnonv5qza6uss4lurbzcru"); // 	agapply(g, (Agobj_t *) g, simple_delrec, rec_name, (!(0)));
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("6x7ztvlgv763oeop84udp1egg"); //     case AGNODE:
UNSUPPORTED("a6ls4lkgjoheqwo2e7yqt9zz8"); //     case AGOUTEDGE:
UNSUPPORTED("c9o8hr6x8n5dty1y3eej1fept"); //     case AGINEDGE:
UNSUPPORTED("eg21iwn9eqyjsoisl58nl8i36"); // 	for (n = agfstnode(g); n; n = agnxtnode(g, n))
UNSUPPORTED("2kxod9hw7p0o5crbt6xh2gcwy"); // 	    if (kind == AGNODE)
UNSUPPORTED("an2xd4k44p94rcg7ynge2g1nq"); // 		agdelrec(n, rec_name);
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("5qry6mw56jhkh965gr6si5sjz"); // 		for (e = agfstout(g, n); e; e = agnxtout(g, e))
UNSUPPORTED("7k5tz238egfs4u82cpy7lk8dn"); // 		    agdelrec(e, rec_name);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1s9p443oxpnfk2w28k6bgn1y0
// void agrecclose(Agobj_t * obj) 
public static Object agrecclose(Object... arg) {
UNSUPPORTED("f28etp6uks02tqgelaos87f96"); // void agrecclose(Agobj_t * obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("4d3axgxxzgvvs9r07npfgyqqs"); //     Agrec_t *rec, *nrec;
UNSUPPORTED("8pfuk9ua4x9bh68zk1kzwc5t9"); //     g = agraphof(obj);
UNSUPPORTED("7mfrjfyuzo92p41ycl0k8l0ux"); //     if ((rec = obj->data)) {
UNSUPPORTED("8vxyvy38lzpbd83cu26nejaan"); // 	do {
UNSUPPORTED("ezzhxi7n4n97pmh7g0zo4wprj"); // 	    nrec = rec->next;
UNSUPPORTED("e1l0oyl4p7njuyrgfuf1nph9b"); // 	    agstrfree(g, rec->name);
UNSUPPORTED("3yrrykwn17vvbliut3f5a174w"); // 	    agfree(g, rec);
UNSUPPORTED("66id2vgqklsr5nz9a84lor5eb"); // 	    rec = nrec;
UNSUPPORTED("aqvgta0a2vlgkvf7yvufyzvt5"); // 	} while (rec != obj->data);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2ila9b4kx11rvt5yy16n3myks"); //     obj->data = ((Agrec_t *)0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
