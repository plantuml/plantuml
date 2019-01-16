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
import static gen.lib.cdt.dtdisc__c.dtdisc;
import static gen.lib.cdt.dtopen__c.dtopen;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.mem__c.agfree;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.function;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.Dtmemory_f;
import h.ST_Agraph_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtmethod_s;
import smetana.core.Memory;
import smetana.core.Z;
import smetana.core.size_t;

public class utils__c {
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


//1 2xwmpggwpggfyyfj0vuw3pmfg
// static Agraph_t *Ag_dictop_G
//static public Agraph_s Ag_dictop_G;



//3 7dkudp41c9byhicatk2sxtxqk
// void *agdictobjmem(Dict_t * dict, void * p, size_t size, Dtdisc_t * disc) 
public static Object agdictobjmem(ST_dt_s dict, Object p, size_t size, ST_dtdisc_s disc) {
ENTERING("7dkudp41c9byhicatk2sxtxqk","agdictobjmem");
try {
	ST_Agraph_s g;
    g = Z.z().Ag_dictop_G;
    if (g!=null) {
	if (p!=null)
	    agfree(g, p);
	else
	    return agalloc(g, size);
    } else {
	if (p!=null)
	    Memory.free(p);
	else
	    return size.malloc();
    }
    return null;
} finally {
LEAVING("7dkudp41c9byhicatk2sxtxqk","agdictobjmem");
}
}




//3 5xdfwxth4q1dm3180qzuf51sn
// void agdictobjfree(Dict_t * dict, void * p, Dtdisc_t * disc) 
public static Object agdictobjfree(Object... arg) {
UNSUPPORTED("bsemnw6m2qx5a4hk13xep80nh"); // void agdictobjfree(Dict_t * dict, void * p, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("nzl57bfl5onx7q9ge19n2k9i"); //     (void) dict;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("ah8bddgh4pyntvi4o9xx91fkm"); //     g = Ag_dictop_G;
UNSUPPORTED("5skdegxoz3mwfvm59pbtvqtiq"); //     if (g)
UNSUPPORTED("akbizhd9c9w4syt1qpirn7xjy"); // 	agfree(g, p);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("cy5x5dma0v4hiepir7lrfuo17"); // 	free(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 48ox0bg1qmasrer8np51uwsyk
// Dict_t *agdtopen(Agraph_t * g, Dtdisc_t * disc, Dtmethod_t * method) 
public static ST_dt_s agdtopen(ST_Agraph_s g, ST_dtdisc_s disc, ST_dtmethod_s method) {
ENTERING("48ox0bg1qmasrer8np51uwsyk","agdtopen");
try {
    Dtmemory_f memf;
    ST_dt_s d;
    memf = (Dtmemory_f) disc.memoryf;
    disc.setPtr("memoryf", function(utils__c.class, "agdictobjmem"));
    Z.z().Ag_dictop_G = g;
    d = dtopen(disc, method);
    disc.setPtr("memoryf", memf);
    Z.z().Ag_dictop_G = null;
    return d;
} finally {
LEAVING("48ox0bg1qmasrer8np51uwsyk","agdtopen");
}
}




//3 6pbz2fsmebq8iy7if4way3ct2
// long agdtdelete(Agraph_t * g, Dict_t * dict, void *obj) 
public static Object agdtdelete(Object... arg) {
UNSUPPORTED("216ju3s3n4ltlcsntcuo0fg5p"); // long agdtdelete(Agraph_t * g, Dict_t * dict, void *obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("id2cse8d1e37coxkbocjgjt4"); //     Ag_dictop_G = g;
UNSUPPORTED("1ii7n9w3quq15wnwynuuwg395"); //     return (long) (*(((Dt_t*)(dict))->searchf))((dict),(void*)(obj),0000002);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8a6i39x23joa467bqbo4b25ng
// int agobjfinalize(void * obj) 
public static Object agobjfinalize(Object... arg) {
UNSUPPORTED("74745f6w9shsg8hps5dn9cunv"); // int agobjfinalize(void * obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dnx10u35ynobkoiuo7v5k2u49"); //     agfree(Ag_dictop_G, obj);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4x6nykawwls34vi6jc9gk1y29
// int agdtclose(Agraph_t * g, Dict_t * dict) 
public static Object agdtclose(Object... arg) {
UNSUPPORTED("8snkovnbu003p9w5cgbn12c73"); // int agdtclose(Agraph_t * g, Dict_t * dict)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ac1z24z48td8pa6zl9az4bqpd"); //     Dtmemory_f memf;
UNSUPPORTED("cawsyfy3xa5ktlemxuyyhd376"); //     Dtdisc_t *disc;
UNSUPPORTED("95hw7dddoneu0z8zzcnb1uslv"); //     disc = dtdisc(dict, ((Dtdisc_t *)0), 0);
UNSUPPORTED("9grut2uf2ma1mtzeq2sfg7p3f"); //     memf = disc->memoryf;
UNSUPPORTED("1jsr5jo041ushyjrdwyazntu7"); //     disc->memoryf = agdictobjmem;
UNSUPPORTED("id2cse8d1e37coxkbocjgjt4"); //     Ag_dictop_G = g;
UNSUPPORTED("32zal75ij4wrh56hfv01bu7as"); //     if (dtclose(dict))
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("9qqgn587jlvxto7mbtmfh3o00"); //     disc->memoryf = memf;
UNSUPPORTED("dgvsuq3309uz08ww03zstdg8g"); //     Ag_dictop_G = ((Agraph_t*)0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cym72wvu6zffc0vzoa93zha8
// void agdtdisc(Agraph_t * g, Dict_t * dict, Dtdisc_t * disc) 
public static void agdtdisc(ST_Agraph_s g, ST_dt_s dict, ST_dtdisc_s disc) {
ENTERING("cym72wvu6zffc0vzoa93zha8","agdtdisc");
try {
    if (disc!=null && NEQ(dtdisc(dict, null, 0), disc)) {
	dtdisc(dict, disc, 0);
    }
    /* else unchanged, disc is same as old disc */
} finally {
LEAVING("cym72wvu6zffc0vzoa93zha8","agdtdisc");
}
}


}
