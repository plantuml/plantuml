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
import static smetana.core.Macro.UNSUPPORTED;

public class flatten__c {
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




//3 69ucljsqnzi8rzbvuqpjp4hfx
// static void agflatten_elist(Dict_t * d, Dtlink_t ** lptr, int flag) 
public static Object agflatten_elist(Object... arg) {
UNSUPPORTED("bpx3af0xjz85367rnoh2ipdrn"); // static void agflatten_elist(Dict_t * d, Dtlink_t ** lptr, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ksyblegh2kh8j0mzdddj80zr"); //     dtrestore(d, *lptr);
UNSUPPORTED("ctwl7b6yxhzl4sj91zup9at33"); //     dtmethod(d, flag? Dtlist : Dtoset);
UNSUPPORTED("du4za15ppq4ntkttytf8tpzkk"); //     *lptr = dtextract(d);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c4syafe2mgpp13hj8vel7hdwh
// void agflatten_edges(Agraph_t * g, Agnode_t * n, int flag) 
public static Object agflatten_edges(Object... arg) {
UNSUPPORTED("4ttg1k6ryqoueza8gtu0qesmc"); // void agflatten_edges(Agraph_t * g, Agnode_t * n, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2llbfi4jrmre7cyhu90pgcm72"); //     Agsubnode_t *sn;
UNSUPPORTED("2g1n9rzdmt5pucspjmto1jwvn"); //     Dtlink_t **tmp;
UNSUPPORTED("1wwtmjdvl73j75h8fp92i9yk5"); // 	sn = agsubrep(g,n);
UNSUPPORTED("cvsy8oi25qamrmokl6dumfc2n"); //     tmp = &(sn->out_seq); /* avoiding - "dereferencing type-punned pointer will break strict-aliasing rules" */
UNSUPPORTED("9robj8jesxzc7bpyw78z8i80l"); //     agflatten_elist(g->e_seq, tmp, flag);
UNSUPPORTED("7no705grsz21vady4u118n7tz"); //     tmp = &(sn->in_seq);
UNSUPPORTED("9robj8jesxzc7bpyw78z8i80l"); //     agflatten_elist(g->e_seq, tmp, flag);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aswf367vm4ypgb50vecx6oy0e
// void agflatten(Agraph_t * g, int flag) 
public static Object agflatten(Object... arg) {
UNSUPPORTED("epc3qpwcrvpagpod79vqkiktz"); // void agflatten(Agraph_t * g, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("6cb8mzhjyrkhua0olueodre85"); //     if (flag) {
UNSUPPORTED("3kgshdurj1k3861cmk0j6459q"); // 	if (g->desc.flatlock == (0)) {
UNSUPPORTED("biacx609ekjlj7vzv20kr0twk"); // 	    dtmethod(g->n_seq,Dtlist);
UNSUPPORTED("feknc9jq0v1n93q583pigog"); // 	    for (n = agfstnode(g); n; n = agnxtnode(g,n))
UNSUPPORTED("4ufjmlytpm2l2msb8zuxor8s2"); // 		agflatten_edges(g, n, flag);
UNSUPPORTED("1r4fzaa446otf0jautzt92b4r"); // 	    g->desc.flatlock = (!(0));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("bacueyt9l2eudlsl4z5n0t9l7"); // 	if (g->desc.flatlock) {
UNSUPPORTED("b0wcdyp5hit8sc1jbi4asmalp"); // 	    dtmethod(g->n_seq,Dtoset);
UNSUPPORTED("feknc9jq0v1n93q583pigog"); // 	    for (n = agfstnode(g); n; n = agnxtnode(g,n))
UNSUPPORTED("4ufjmlytpm2l2msb8zuxor8s2"); // 		agflatten_edges(g, n, flag);
UNSUPPORTED("1mvsjktpob2pzvo0s5wp5hbl0"); // 	    g->desc.flatlock = (0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 602i6cqy4kebb7g16m580nxsd
// void agnotflat(Agraph_t * g) 
public static Object agnotflat(Object... arg) {
UNSUPPORTED("cu099fjwopup8fb0dz3ob3yui"); // void agnotflat(Agraph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1whie4ln05zs17jprvbhgrw0o"); //     if (g->desc.flatlock)
UNSUPPORTED("bsprgtbeyh48e6897xrl9q004"); // 	agerr(AGERR, "flat lock broken");
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
