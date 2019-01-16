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
import static gen.lib.cgraph.graph__c.agopen1;
import static gen.lib.cgraph.id__c.agmapnametoid;
import static gen.lib.cgraph.id__c.agregister;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.utils__c.agdtdisc;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Agdesc_s;
import h.ST_Agraph_s;
import h.ST_dt_s;
import smetana.core.CString;
import smetana.core.Z;
import smetana.core.__ptr__;

public class subg__c {
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




//3 11ezyrsjsotjz9b3cyvb4ie8p
// static Agraph_t *agfindsubg_by_id(Agraph_t * g, unsigned long id) 
public static ST_Agraph_s agfindsubg_by_id(ST_Agraph_s g, int id) {
ENTERING("11ezyrsjsotjz9b3cyvb4ie8p","agfindsubg_by_id");
try {
    final ST_Agraph_s template = new ST_Agraph_s();
    agdtdisc(g, (ST_dt_s) g.g_dict, Z.z().Ag_subgraph_id_disc);
    AGID(template, id);
    return (ST_Agraph_s) g.g_dict.searchf.exe(g.g_dict, template, 0000004);
} finally {
LEAVING("11ezyrsjsotjz9b3cyvb4ie8p","agfindsubg_by_id");
}
}




//3 44saycxbfbr9lou0itlyewkb4
// static Agraph_t *localsubg(Agraph_t * g, unsigned long id) 
public static ST_Agraph_s localsubg(ST_Agraph_s g, int id) {
ENTERING("44saycxbfbr9lou0itlyewkb4","localsubg");
try {
    ST_Agraph_s subg;
    subg = agfindsubg_by_id(g, id);
    if (subg!=null)
	return subg;
    subg = (ST_Agraph_s) agalloc(g, sizeof(ST_Agraph_s.class));
    subg.setPtr("clos", g.clos);
    subg.setStruct("desc", g.desc);
    ((ST_Agdesc_s)subg.desc).maingraph = 0;
    subg.setPtr("parent", g);
    subg.setPtr("root", g.root);
    AGID(subg, id);
    return agopen1(subg);
} finally {
LEAVING("44saycxbfbr9lou0itlyewkb4","localsubg");
}
}




//3 6wqxmivgp34bobzqacmsj7lcv
// Agraph_t *agidsubg(Agraph_t * g, unsigned long id, int cflag) 
public static Object agidsubg(Object... arg) {
UNSUPPORTED("9vhen1flll6k35g72uuroky0z"); // Agraph_t *agidsubg(Agraph_t * g, unsigned long id, int cflag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c3tourf5k7groqyh71yjd5xec"); //     Agraph_t *subg;
UNSUPPORTED("elpselocwvpfk3o27nejqo6dl"); //     subg = agfindsubg_by_id(g, id);
UNSUPPORTED("cf9t4fxh9z5rmv58441ne0ysa"); //     if ((subg == ((Agraph_t*)0)) && cflag && agallocid(g, AGRAPH, id))
UNSUPPORTED("ecrqdk8b7debftn6d8bbmh0s4"); // 	subg = localsubg(g, id);
UNSUPPORTED("17jmp11l2jzc1v9qpaqkzkauj"); //     return subg;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a24jd4r2sdyb4lb2hyababrda
// Agraph_t *agsubg(Agraph_t * g, char *name, int cflag) 
public static ST_Agraph_s agsubg(ST_Agraph_s g, CString name, boolean cflag) {
ENTERING("a24jd4r2sdyb4lb2hyababrda","agsubg");
try {
    int id[] = new int[]{0};
    ST_Agraph_s subg;
    if (name!=null && agmapnametoid(g, AGRAPH, name, id, false)!=0) {
	/* might already exist */
	if ((subg = agfindsubg_by_id(g, id[0]))!=null)
	    return subg;
    }
    if (cflag && agmapnametoid(g, AGRAPH, name, id, (N(false)))!=0) {	/* reserve id */
	subg = localsubg(g, id[0]);
	agregister(g, AGRAPH, subg);
	return subg;
    }
    return null;
} finally {
LEAVING("a24jd4r2sdyb4lb2hyababrda","agsubg");
}
}




//3 51eksrs0lhkgohunejlpwyc4k
// Agraph_t *agfstsubg(Agraph_t * g) 
public static ST_Agraph_s agfstsubg(ST_Agraph_s g) {
ENTERING("51eksrs0lhkgohunejlpwyc4k","agfstsubg");
try {
	__ptr__ tmp = (__ptr__)g.g_dict.searchf.exe(g.g_dict,null,0000200);
	if (tmp!=null) tmp = tmp.castTo(ST_Agraph_s.class);
	return (ST_Agraph_s) tmp;
} finally {
LEAVING("51eksrs0lhkgohunejlpwyc4k","agfstsubg");
}
}




//3 85c1qisrein0tzm2regoe61t
// Agraph_t *agnxtsubg(Agraph_t * subg) 
public static ST_Agraph_s agnxtsubg(ST_Agraph_s subg) {
ENTERING("85c1qisrein0tzm2regoe61t","agnxtsubg");
try {
    ST_Agraph_s g;
    g = agparent(subg);
    if (g==null) return null;
    __ptr__ tmp = (__ptr__) g.g_dict.searchf.exe(g.g_dict, subg, 0000010);
    if (tmp==null) return null;
    return (ST_Agraph_s) tmp.getPtr();
} finally {
LEAVING("85c1qisrein0tzm2regoe61t","agnxtsubg");
}
}




//3 7kbp6j03hd7u6nnlivi0vt3ja
// Agraph_t *agparent(Agraph_t * g) 
public static ST_Agraph_s agparent(ST_Agraph_s g) {
ENTERING("7kbp6j03hd7u6nnlivi0vt3ja","agparent");
try {
	return (ST_Agraph_s) g.parent;
} finally {
LEAVING("7kbp6j03hd7u6nnlivi0vt3ja","agparent");
}
}




//3 37trxrsv69a3pl08f5awwj3tq
// long agdelsubg(Agraph_t * g, Agraph_t * subg) 
public static Object agdelsubg(Object... arg) {
UNSUPPORTED("bjcimcpi0qag1hc37no67mct5"); // long agdelsubg(Agraph_t * g, Agraph_t * subg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dugmh24ptzgxuj3204p7n39m4"); //     return (long) (*(((Dt_t*)(g->g_dict))->searchf))((g->g_dict),(void*)(subg),0000002);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
