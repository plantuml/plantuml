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
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.utils__c.agdtdelete;
import static gen.lib.cgraph.utils__c.agdtopen;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agraph_s;
import h.ST_dt_s;
import h.ST_refstr_t;
import smetana.core.ACCESS;
import smetana.core.CString;
import smetana.core.STARSTAR;
import smetana.core.Z;
import smetana.core.size_t;

public class refstr__c {
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


//1 8bj6ivnd4go7wt4pvzqgk8mlr
// static unsigned long HTML_BIT
//static public int HTML_BIT;

//1 dqn77l82bfu071bv703e77jmg
// static unsigned long CNT_BITS
//static public int CNT_BITS;

//1 boyxdmkhstn4i64pqf6sv1mi7
// static Dtdisc_t Refstrdisc = 
/*static public final __struct__<_dtdisc_s> Refstrdisc = JUtils.from(_dtdisc_s.class);
static {
	Refstrdisc.setInt("key", OFFSET.create(refstr_t.class, "s").toInt()); // *s is the third field in refstr_t
	Refstrdisc.setInt("size", -1);
	Refstrdisc.setInt("link", 0);
	Refstrdisc.setPtr("makef", null);
	Refstrdisc.setPtr("freef", function(utils__c.class, "agdictobjfree"));
	Refstrdisc.setPtr("comparf", null);
	Refstrdisc.setPtr("hashf", null);
	Refstrdisc.setPtr("memoryf", function(utils__c.class, "agdictobjmem"));
	Refstrdisc.setPtr("eventf", null);
}*/

//1 2e0tdcdyjc9zq54xt1nzgwvn3
// static Dict_t *Refdict_default
//static public _dt_s Refdict_default;



//3 f1nwss2xoaub1hyord232ugoj
// static Dict_t *refdict(Agraph_t * g) 
public static ST_dt_s refdict(final ST_Agraph_s g) {
ENTERING("f1nwss2xoaub1hyord232ugoj","refdict");
try {
		STARSTAR<ST_dt_s> dictref;
		if (g != null)
			dictref = STARSTAR.amp(new ACCESS<ST_dt_s>() {
				public ST_dt_s get() {
					return (ST_dt_s) g.clos.strdict;
				}
				public void set(ST_dt_s obj) {
					g.clos.setPtr("strdict", obj);
				}
			});
		else
			dictref = STARSTAR.amp(new ACCESS<ST_dt_s>() {
				public ST_dt_s get() {
					return Z.z().Refdict_default;
				}
				public void set(ST_dt_s obj) {
					Z.z().Refdict_default = obj;
				}
			});
    if (dictref.getMe() == null) {
	dictref.setMe(agdtopen(g, Z.z().Refstrdisc, Z.z().Dttree));
		}
	Z.z().HTML_BIT = 1 << 31;
	Z.z().CNT_BITS = ~Z.z().HTML_BIT;
    return dictref.getMe();
} finally {
LEAVING("f1nwss2xoaub1hyord232ugoj","refdict");
}
}




//3 9aajykl8nuymg60zukycquawa
// int agstrclose(Agraph_t * g) 
public static Object agstrclose(Object... arg) {
UNSUPPORTED("c2l353zz5jt7jlmbhjbbt3m7v"); // int agstrclose(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("208rcf0f70q2wxwsa6po42oqq"); //     return agdtclose(g, refdict(g));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9ts4wqhw2xafdv3tlcilneewq
// static refstr_t *refsymbind(Dict_t * strdict, char *s) 
public static ST_refstr_t refsymbind(ST_dt_s strdict, CString s) {
ENTERING("9ts4wqhw2xafdv3tlcilneewq","refsymbind");
try {
    final ST_refstr_t key = new ST_refstr_t();
    ST_refstr_t r;
    // key.setPtr("s", s.duplicate());
    key.setString(s);
    r = (ST_refstr_t) strdict.searchf.exe(strdict, key, 0000004);
    return r;
} finally {
LEAVING("9ts4wqhw2xafdv3tlcilneewq","refsymbind");
}
}




//3 1scntgo71z7c2v15zapiyw59w
// static char *refstrbind(Dict_t * strdict, char *s) 
public static CString refstrbind(ST_dt_s strdict, CString s) {
ENTERING("1scntgo71z7c2v15zapiyw59w","refstrbind");
try {
    ST_refstr_t r;
    r = refsymbind(strdict, s);
    if (r!=null)
	return r.s;
    else
	return null;
} finally {
LEAVING("1scntgo71z7c2v15zapiyw59w","refstrbind");
}
}




//3 bb8aqjshw3ecae2lsmhigd0mc
// char *agstrbind(Agraph_t * g, char *s) 
public static CString agstrbind(ST_Agraph_s g, CString s) {
ENTERING("bb8aqjshw3ecae2lsmhigd0mc","agstrbind");
try {
    return refstrbind(refdict(g), s);
} finally {
LEAVING("bb8aqjshw3ecae2lsmhigd0mc","agstrbind");
}
}




//3 86oznromwhn9qeym0k7pih73q
// char *agstrdup(Agraph_t * g, char *s) 
public static CString agstrdup(ST_Agraph_s g, CString s) {
ENTERING("86oznromwhn9qeym0k7pih73q","agstrdup");
try {
	ST_refstr_t r;
    ST_dt_s strdict;
    size_t sz;
    if (s == null)
	 return null;
    strdict = refdict(g);
    r = (ST_refstr_t) refsymbind(strdict, s);
    if (r!=null)
	r.refcnt++;
    else {
	sz = sizeof(ST_refstr_t.class).plus(s.length());
	if (g!=null)
	    r = (ST_refstr_t) agalloc(g, sz);
	else
	    r = new ST_refstr_t();
	r.refcnt = 1;
	r.setString(s.duplicate());
//	strcpy(r->store, s);
//	r->s = r->store;
	strdict.searchf.exe(strdict,r,0000001);
    }
	return r.s;
} finally {
LEAVING("86oznromwhn9qeym0k7pih73q","agstrdup");
}
}




//3 1ovgpnv6r8ru6iz51ic91zzal
// char *agstrdup_html(Agraph_t * g, char *s) 
public static Object agstrdup_html(Object... arg) {
UNSUPPORTED("6679vrn0j0vkzernsn2rlottw"); // char *agstrdup_html(Agraph_t * g, char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1uvxutp09oluiacpgn0f76bgu"); //     refstr_t *r;
UNSUPPORTED("czgqod5ni1s5av81qa3n0ghgr"); //     Dict_t *strdict;
UNSUPPORTED("55x2wgzchv0157f4g74693oaq"); //     size_t sz;
UNSUPPORTED("a5abfeqtsa4i5x739edpwuxin"); //     if (s == ((char *)0))
UNSUPPORTED("xp8okoaicybpovkenntpd857"); // 	 return ((char *)0);
UNSUPPORTED("bo3fdoot7ldevx250qweitj6z"); //     strdict = refdict(g);
UNSUPPORTED("12vt0u4w3q0jht9f9vsaybntn"); //     r = refsymbind(strdict, s);
UNSUPPORTED("67y4tszu7dmeves31gr9ydmpi"); //     if (r)
UNSUPPORTED("5gybhadmtbc77f5wf9adyemj7"); // 	r->refcnt++;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("9llv1u64vbj6q8loctnrowtm5"); // 	sz = sizeof(refstr_t) + strlen(s);
UNSUPPORTED("7tmc6a514rv2k24wg5o8qpvyp"); // 	if (g)
UNSUPPORTED("asjj8bj1b02f70rfr41ayipxy"); // 	    r = (refstr_t *) agalloc(g, sz);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("bp5rr6mkh94826cbgdwglvpk9"); // 	    r = (refstr_t *) malloc(sz);
UNSUPPORTED("6sl9ejza97inawt8uprd120h6"); // 	r->refcnt = 1 | HTML_BIT;
UNSUPPORTED("dadamob0ot3fpofdm1ey34srj"); // 	strcpy(r->store, s);
UNSUPPORTED("1cyhds1lm0ee8rtp7k7h5cqfw"); // 	r->s = r->store;
UNSUPPORTED("b2zaf5uj8gofpyc40hl0ziymh"); // 	(*(((Dt_t*)(strdict))->searchf))((strdict),(void*)(r),0000001);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("lxjgfic7zk869xczsgazw3sx"); //     return r->s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 enhn1ajfo86a19dgm4b8lduz7
// int agstrfree(Agraph_t * g, char *s) 
public static int agstrfree(ST_Agraph_s g, CString s) {
ENTERING("enhn1ajfo86a19dgm4b8lduz7","agstrfree");
try {
    ST_refstr_t r;
    ST_dt_s strdict;
    if (s == null)
	 return -1;
    strdict = refdict(g);
    r = (ST_refstr_t) refsymbind(strdict, s);
    if (r!=null && (EQ(r.s, s))) {
	r.refcnt--;
	if ((r.refcnt!=0 && Z.z().CNT_BITS!=0) == false) {
	    agdtdelete(g, strdict, r);
	    /*
	       if (g) agfree(g,r);
	       else free(r);
	     */
	}
    }
    if (r == null)
	return -1;
    return 0;
} finally {
LEAVING("enhn1ajfo86a19dgm4b8lduz7","agstrfree");
}
}




//3 3em4wzjnpajd5d3igb90l3rml
// int aghtmlstr(char *s) 
public static int aghtmlstr(CString s) {
ENTERING("3em4wzjnpajd5d3igb90l3rml","aghtmlstr");
try {
    ST_refstr_t key;
    if (s == null)
	return 0;
	key = (ST_refstr_t) s.getParent();
    return (key.refcnt & Z.z().HTML_BIT);
} finally {
LEAVING("3em4wzjnpajd5d3igb90l3rml","aghtmlstr");
}
}




//3 ap2ebebypq6vmwle0hicv6tmj
// void agmarkhtmlstr(char *s) 
public static Object agmarkhtmlstr(Object... arg) {
UNSUPPORTED("8oc24oz62ej815sjwuwuj9bmt"); // void agmarkhtmlstr(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("164ww6fcxh6v2wmxj0v8aqviy"); //     refstr_t *key;
UNSUPPORTED("8quozj18vjguewxdpv9w14yjn"); //     if (s == NULL)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("9cmt4vbkm95fqftevdqyfvslr"); //     key = (refstr_t *) (s - ((int)(&(((refstr_t*)0)->store[0]))));
UNSUPPORTED("68mcf5kr6xw538zkdk8b50aeb"); //     key->refcnt |= HTML_BIT;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
