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
package gen.lib.ortho;
import static smetana.core.Macro.UNSUPPORTED;

public class sgraph__c {
//1 2digov3edok6d5srhgtlmrycs
// extern lt_symlist_t lt_preloaded_symbols[]


//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


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




//3 do1p8q9fgspbwjclhfas1e31i
// void gsave (sgraph* G) 
public static Object gsave(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("53tz1kawkoq5faz7kymp6ptpo"); // gsave (sgraph* G)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("4a3idvuwsn45q081062uxj30b"); //     G->save_nnodes = G->nnodes;
UNSUPPORTED("dvjft3jbvipb8kx48kn2cxq4d"); //     G->save_nedges = G->nedges;
UNSUPPORTED("ehil2x3e5xjd504di5xq03ix2"); //     for (i = 0; i < G->nnodes; i++)
UNSUPPORTED("8phdmwqejnwfe17clzvzqusic"); // 	G->nodes[i].save_n_adj =  G->nodes[i].n_adj;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4bd3mhrsaxr0us4mbdlnz3fva
// void  reset(sgraph* G) 
public static Object reset(Object... arg) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("o6ryffoh62mg3jqikb1bcvcn"); // reset(sgraph* G)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("4z92iyiytnarfwg69q6q1qxdo"); //     G->nnodes = G->save_nnodes;
UNSUPPORTED("gi9g609ppu0efm44uexae3lg"); //     G->nedges = G->save_nedges;
UNSUPPORTED("ehil2x3e5xjd504di5xq03ix2"); //     for (i = 0; i < G->nnodes; i++)
UNSUPPORTED("amo8o2h5u6ikoe5ouawwnxttv"); // 	G->nodes[i].n_adj = G->nodes[i].save_n_adj;
UNSUPPORTED("395pz5pbzjp227kygyayer2m0"); //     for (; i < G->nnodes+2; i++)
UNSUPPORTED("1w65z5djtqk8i6nk3cu9qavs9"); // 	G->nodes[i].n_adj = 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2uooy3bnw1kc4mm4ns09blzvd
// void initSEdges (sgraph* g, int maxdeg) 
public static Object initSEdges(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("3t8ixyxav6ee8tjumnj6c4on7"); // initSEdges (sgraph* g, int maxdeg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("584qbup3c7sdxo8kave8mllal"); //     int* adj = (int*)zmalloc((6*g->nnodes + 2*maxdeg)*sizeof(int));
UNSUPPORTED("dvc1rp1rnjfmjnxvwpm004stb"); //     g->edges = (sedge*)zmalloc((3*g->nnodes + maxdeg)*sizeof(sedge));
UNSUPPORTED("eavilhxlidc3wjniw4glglbnt"); //     for (i = 0; i < g->nnodes; i++) {
UNSUPPORTED("ckt5vl01ya7v4i84i9yxcipv0"); // 	g->nodes[i].adj_edge_list = adj;
UNSUPPORTED("dzxhiwbj9xcvedfridh0okcez"); // 	adj += 6;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dgkzkhndgc3xziilvfagce4xi"); //     for (; i < g->nnodes+2; i++) {
UNSUPPORTED("ckt5vl01ya7v4i84i9yxcipv0"); // 	g->nodes[i].adj_edge_list = adj;
UNSUPPORTED("68si7kljcmnbf59kgy3rk0m6z"); // 	adj += maxdeg;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4lws7mi89ekzzcc99wuw9r7l8
// sgraph* createSGraph (int nnodes) 
public static Object createSGraph(Object... arg) {
UNSUPPORTED("9xadge294rqhy06asmqycka8m"); // sgraph*
UNSUPPORTED("eqjwxhjyhqmefxsvpknl1bnfx"); // createSGraph (int nnodes)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dwa606y0aknngzxrrsnswltbr"); //     sgraph* g = (sgraph*)zmalloc(sizeof(sgraph));
UNSUPPORTED("1i02j60f9yupm02tj9rvzps0o"); // 	/* create the nodes vector in the search graph */
UNSUPPORTED("3yhlfn7hpvrjkdva5gida8jqo"); //     g->nnodes = 0;
UNSUPPORTED("47bxz2s175s5k1snvid5o4hve"); //     g->nodes = (snode*)zmalloc((nnodes)*sizeof(snode));
UNSUPPORTED("2syri7q5tc0jyvwq8ecyfo3vr"); //     return g;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4jwt9radjtehigwninuxfni66
// snode* createSNode (sgraph* g) 
public static Object createSNode(Object... arg) {
UNSUPPORTED("7qyamsdkopruu23xaccl0bcd2"); // snode*
UNSUPPORTED("753vtsohiclqv7nl3y14twf19"); // createSNode (sgraph* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hdbk7wtbmf5ysy77ka3v1wv1"); //     snode* np = g->nodes+g->nnodes;
UNSUPPORTED("12345aoqngizwar08ubsjkjql"); //     np->index = g->nnodes;
UNSUPPORTED("1pdvfvgbmueo5t1u1q4rbj9t4"); //     g->nnodes++;
UNSUPPORTED("8iwc9afkw6pes2mgfhktuxzj8"); //     return np;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 49zscu3ughv1sfoes00tmvzew
// static void addEdgeToNode (snode* np, sedge* e, int idx) 
public static Object addEdgeToNode(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("dpg266aklzv95plhgmmdgdl2h"); // addEdgeToNode (snode* np, sedge* e, int idx)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3bjz47ii1rlzjcxhnphx8bepr"); //     np->adj_edge_list[np->n_adj] = idx;
UNSUPPORTED("2vd3mg5b94lmz0n3qsamkbrbk"); //     np->n_adj++;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 25bbgi67jwpg5nrx87bctkn5s
// sedge* createSEdge (sgraph* g, snode* v1, snode* v2, double wt) 
public static Object createSEdge(Object... arg) {
UNSUPPORTED("de30fcx2rpq95h8prggzz38de"); // sedge*
UNSUPPORTED("e67qstzz1cjy6tm1gp72tqiy9"); // createSEdge (sgraph* g, snode* v1, snode* v2, double wt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5us6bymu4crkiqypwh57332hj"); //     sedge* e;
UNSUPPORTED("3edhazo5e0eznk99j763w7wvk"); //     int idx = g->nedges++;
UNSUPPORTED("12o3iah7k1tllf6ehoks5ez9r"); //     e = g->edges + idx;
UNSUPPORTED("742bl52l6dlhwl8vdmr0gi6mt"); //     e->v1 = v1->index;
UNSUPPORTED("eo17frg8yipb9e15ykaocxv8"); //     e->v2 = v2->index;
UNSUPPORTED("3tbomhde001xxpsgrqrxaj5qq"); //     e->weight = wt;
UNSUPPORTED("bdajav43vx6s0dqy0k4qzz34r"); //     e->cnt = 0;
UNSUPPORTED("2fxbnv4tmis57zol548d8t69o"); //     addEdgeToNode (v1, e, idx);
UNSUPPORTED("8fhmkjmzcgsaedp55s0lgcnzf"); //     addEdgeToNode (v2, e, idx);
UNSUPPORTED("2bswif6w6ot01ynlvkimntfly"); //     return e;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cmavbm512p00aze5hpa7ewqnf
// void freeSGraph (sgraph* g) 
public static Object freeSGraph(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("9hthkvnsrcbkqzxbng2mlgcr"); // freeSGraph (sgraph* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5xfei00jwgg7b7kejqmwtve51"); //     free (g->nodes[0].adj_edge_list);
UNSUPPORTED("apli015280vdkava7kzfu45gt"); //     free (g->nodes);
UNSUPPORTED("aobfrb6yk5ivr8l2memu6iegu"); //     free (g->edges);
UNSUPPORTED("dwuc0y4whcauryjdz2g3rdyey"); //     free (g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1rcimcpgyvapk639i1ln4ubi5
// static snode* adjacentNode(sgraph* g, sedge* e, snode* n) 
public static Object adjacentNode(Object... arg) {
UNSUPPORTED("1mqfssg5cquehb5bdk2189gy2"); // static snode*
UNSUPPORTED("d7hrrud3osvm08rbt95y6nc7v"); // adjacentNode(sgraph* g, sedge* e, snode* n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aapvzqekocoy044mxh46608wo"); //     if (e->v1==n->index)
UNSUPPORTED("62g358pvqoywxqbeb39tjulp2"); // 	return (&(g->nodes[e->v2]));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("dkxx6omcntqirmu5yygun7kop"); // 	return (&(g->nodes[e->v1]));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9giqtyb1anron57zz1mdcuwr0
// int shortPath (sgraph* g, snode* from, snode* to) 
public static Object shortPath(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("2d7cnx1fuelcu110onyrglta1"); // shortPath (sgraph* g, snode* from, snode* to)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2azqjiolcsah2sn6lnkbcv8q6"); //     snode* n;
UNSUPPORTED("5us6bymu4crkiqypwh57332hj"); //     sedge* e;
UNSUPPORTED("a4iu3ng9cbut3oe6j3w1hhtlw"); //     snode* adjn;
UNSUPPORTED("5azgw3jpyk2yccpp3p4s3q817"); //     int d;
UNSUPPORTED("1ouxm7u78s3lne6g97el7yirj"); //     int   x, y;
UNSUPPORTED("cj7nzqwbchsm0mvo2wmynwq5u"); //     for (x = 0; x<g->nnodes; x++) {
UNSUPPORTED("7d5ytzu7ryaoy73g9z0g89g37"); // 	snode* temp;
UNSUPPORTED("ed9j6kqbny3qsxz8e8dk4vxe6"); // 	temp = &(g->nodes[x]);
UNSUPPORTED("78pvwvoqedjj594q1og8f58lt"); // 	(temp)->n_val = INT_MIN;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6ig938tzt63g78nuq5ozjoxia"); //     PQinit();
UNSUPPORTED("d1qoqypb9nj64lzirszjt4ips"); //     if (PQ_insert (from)) return 1;
UNSUPPORTED("5jv8ecdm6aag2bo5uj5oaqdmn"); //     (from)->n_dad = NULL;
UNSUPPORTED("ddhvyzr3vort1bce9uxacz0cy"); //     (from)->n_val = 0;
UNSUPPORTED("58d6z37ovsflm1901h2priinl"); //     while ((n = PQremove())) {
UNSUPPORTED("83j13xloeu1fjo8bdrw4zv4ot"); // 	(n)->n_val *= -1;
UNSUPPORTED("62t0o3w85qn5ull41imvtkl2v"); // 	if (n == to) break;
UNSUPPORTED("2yuzydrscmjwya9vwcjaub1bw"); // 	for (y=0; y<n->n_adj; y++) {
UNSUPPORTED("7dykckcqwi0r01sokehzys07u"); // 	    e = &(g->edges[n->adj_edge_list[y]]);
UNSUPPORTED("ee5sii5zq78njilg2b84rcedx"); // 	    adjn = adjacentNode(g, e, n);
UNSUPPORTED("dcr46uov2a9y4x1d805di1vkj"); // 	    if ((adjn)->n_val < 0) {
UNSUPPORTED("ean1jlrfbqj5db693w8atob75"); // 		d = -((n)->n_val + (e->weight));
UNSUPPORTED("5i12njirmr3egi8u7dyh2x5jq"); // 		if ((adjn)->n_val == INT_MIN) {
UNSUPPORTED("7fyonsun390xwjwz5nvv6387t"); // 		    (adjn)->n_val = d;
UNSUPPORTED("17otcakp576j9myi0y8j2018l"); // 		    if (PQ_insert(adjn)) return 1;
UNSUPPORTED("cr4e7c9poda05whkcy9htuvi4"); // 		    (adjn)->n_dad = n;
UNSUPPORTED("4ag21fj7gx3dj7pmwqjl89ucr"); // 		    (adjn)->n_edge = e;
UNSUPPORTED("1vo9ytkxoezj3oe6k9py3kdc4"); //             	}
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("ddue9dplflq1i5vnoxj8kmr5q"); // 		    if ((adjn)->n_val < d) {
UNSUPPORTED("btt3je7wg001msmvi1swe6o2i"); // 			PQupdate(adjn, d);
UNSUPPORTED("dkztzg2evj6hhccujfulxxaif"); // 			(adjn)->n_dad = n;
UNSUPPORTED("7jm2ji6pc40tpai19vzx2ewqf"); // 			(adjn)->n_edge = e;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7a6hw5yb8rm8wg0rs40229cw4"); //     /* PQfree(); */
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
