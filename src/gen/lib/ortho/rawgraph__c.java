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

public class rawgraph__c {
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




//3 b9uhij2qxmaueiwxbfjbx7j7c
// rawgraph* make_graph(int n) 
public static Object make_graph(Object... arg) {
UNSUPPORTED("dcc13k9x44a7gckftbejv58dc"); // rawgraph*
UNSUPPORTED("2zmrv5ohq7e29e3qiy4b3r2kf"); // make_graph(int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7z0gl9a9knlb9h763yg0hs2bz"); //     rawgraph* g = (rawgraph*)zmalloc(sizeof(rawgraph));
UNSUPPORTED("7hsfp0cjk2t70uwawb8cklsz8"); //     g->nvs = n;
UNSUPPORTED("8o0aimsmyrmbv62ryv7vejhv1"); //     g->vertices = (vertex*)zmalloc((n)*sizeof(vertex));
UNSUPPORTED("dudyytiqbhk3xyognwkwx5tt1"); //     for(i=0;i<n;i++) {
UNSUPPORTED("bn57eyqn4zijfxzh8e6ywudec"); //         g->vertices[i].adj_list = openIntSet ();
UNSUPPORTED("1anncbcbrd6gx9hlo6mg1ha7d"); //         g->vertices[i].color = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2syri7q5tc0jyvwq8ecyfo3vr"); //     return g;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5qv1mxxuhkzua2w2dqem19lny
// void free_graph(rawgraph* g) 
public static Object free_graph(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("6cqftcuhpqak3hl5c518hyr4e"); // free_graph(rawgraph* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("31v9apskm8ur1w7f890v55btk"); //     for(i=0;i<g->nvs;i++)
UNSUPPORTED("mfqz9m45ul5js6fnn03ol8j5"); //         dtclose(g->vertices[i].adj_list);
UNSUPPORTED("4836xd1juguptr6qebt07jlrh"); //     free (g->vertices);
UNSUPPORTED("dwuc0y4whcauryjdz2g3rdyey"); //     free (g);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 186nbf92ja5ttnrgvj422eqf5
// void  insert_edge(rawgraph* g, int v1, int v2) 
public static Object insert_edge(Object... arg) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("jldraubzdwc00et6agtk6726"); // insert_edge(rawgraph* g, int v1, int v2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2rjvy9cij6zo2nowb4w37hnjt"); //     intitem obj;
UNSUPPORTED("bmm6cyaytc4kc2ri4gey846yp"); //     obj.id = v2;
UNSUPPORTED("a1hlf5bwzya3m4ujh9zokvj3a"); //     (*(((Dt_t*)(g->vertices[v1].adj_list))->searchf))((g->vertices[v1].adj_list),(void*)(&obj),0000001);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1r1ufpzdilo5438zjdky0uy2w
// void remove_redge(rawgraph* g, int v1, int v2) 
public static Object remove_redge(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("a9gk586ljggidricdp6evpxau"); // remove_redge(rawgraph* g, int v1, int v2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2rjvy9cij6zo2nowb4w37hnjt"); //     intitem obj;
UNSUPPORTED("bmm6cyaytc4kc2ri4gey846yp"); //     obj.id = v2;
UNSUPPORTED("14tyn9y8xz91kr6dfeier49r"); //     (*(((Dt_t*)(g->vertices[v1].adj_list))->searchf))((g->vertices[v1].adj_list),(void*)(&obj),0000002);
UNSUPPORTED("c4j1680th3r5qcpns3u4zjale"); //     obj.id = v1;
UNSUPPORTED("9oqxwhwpfvzwxbnltjdoq1oga"); //     (*(((Dt_t*)(g->vertices[v2].adj_list))->searchf))((g->vertices[v2].adj_list),(void*)(&obj),0000002);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3ff6zoop8jhts0qsvj91k9dfk
// int edge_exists(rawgraph* g, int v1, int v2) 
public static Object edge_exists(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("4urd9z39jr49sen0w7s573505"); // edge_exists(rawgraph* g, int v1, int v2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e2phkxwl9hjxom6accmixx6p2"); //     return ((*(((Dt_t*)(g->vertices[v1].adj_list))->searchf))((g->vertices[v1].adj_list),(void*)(&v2),0001000) != 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1zivcgw1xwk6pq169yqc75oh1
// static stack* mkStack (int i) 
public static Object mkStack(Object... arg) {
UNSUPPORTED("9cla0obmp67i5rjsjxiqdeimj"); // static stack*
UNSUPPORTED("ci2xjox5enk1600klp4wnc4d0"); // mkStack (int i)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8bsg9hgmxo09dmbp0rqikzip9"); //     stack* sp = (stack*)zmalloc(sizeof(stack));
UNSUPPORTED("dgvcpjjkop19ctj9c6as7irgh"); //     sp->vals = (int*)zmalloc((i)*sizeof(int));
UNSUPPORTED("6ak6snmn7x9mv08cxv1uhqto0"); //     sp->top = -1;
UNSUPPORTED("c31qqlge2adqev6kmlr0iuaea"); //     return sp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 56jaryc2ndgkphreacgutgkko
// static void freeStack (stack* s) 
public static Object freeStack(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("1tmwfqmwfffw7layx1b9l774l"); // freeStack (stack* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ivw0yolzv64dlaqawvv30x5v"); //     free (s->vals);
UNSUPPORTED("f1430a029xzg8cabffg2k9l6j"); //     free (s);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 40jmhsu64yd114q5sxnbwirxh
// static void pushStack (stack* s, int i) 
public static Object pushStack(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("efz1du2gap9f44f5qfs4oa0ga"); // pushStack (stack* s, int i)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1a9i725yadesextx7inlkz10x"); //     s->top++;
UNSUPPORTED("5pdmgjwfiz1iusmq5fydnblap"); //     s->vals[s->top] = i;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4oxcr6qfkyvzxsr9y5oo4ok34
// static int popStack (stack* s) 
public static Object popStack(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("9blpzh0xe07qxynad8t5mqz8x"); // popStack (stack* s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6m5stt3wg7jgpdcvvk37onm1s"); //     int v;
UNSUPPORTED("ceenb9p2929863jeh6inli97k"); //     if (s->top == -1) return -1;
UNSUPPORTED("efrvy47jf2m4lt70c0u4yxt67"); //     v = s->vals[s->top];
UNSUPPORTED("owa601uw5gl8igjnyr2lfsq7"); //     s->top--;
UNSUPPORTED("dpci52ct1zm8k1aasm170ru2j"); //     return v;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2jkcimgyhf4lzdolm41833uv1
// static int DFS_visit(rawgraph* g, int v, int time, stack* sp) 
public static Object DFS_visit(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("9tjvxtubftvfbyn9yg9s2ukk5"); // DFS_visit(rawgraph* g, int v, int time, stack* sp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("efg3j1wpsky3c1yns7rcddd1g"); //     Dt_t* adj;
UNSUPPORTED("6i10bzt8ybhoodrtwofkpcavu"); //     Dtlink_t* link;
UNSUPPORTED("9uf2icnp9lntl8gmbrqtn59cu"); //     int id;
UNSUPPORTED("b69yoemu1fzv6up7qzrryu9z1"); //     vertex* vp;
UNSUPPORTED("eoyjtfht0qiqsx0g6j3shub2r"); //     vp = g->vertices + v;
UNSUPPORTED("1bpaugmlepecgk8uiqqd6lhih"); //     vp->color = 1;
UNSUPPORTED("17rwtc50qcliu6s4uujnjxlfn"); //     /* g->vertices[v].d = time; */
UNSUPPORTED("c8ss949bn9k3dw76vpp186lei"); //     adj = vp->adj_list;
UNSUPPORTED("5mtxa718isij6bd88jbwav09r"); //     time = time + 1;
UNSUPPORTED("683z4jufz8k2kssqjqpceyjzm"); //     for(link = dtflatten (adj); link; link = (((Dtlink_t*)(link))->right)) {
UNSUPPORTED("2aeor5mnjzhwqpcgc294oq70m"); //         id = ((intitem*)(((Dt_t*)(adj))->disc->link < 0 ? ((Dthold_t*)((link)))->obj : (void*)((char*)((link)) - ((Dt_t*)(adj))->disc->link) ))->id;
UNSUPPORTED("1fci76oeaq8sz2pvp4p6npq73"); //         if(g->vertices[id].color == 0)
UNSUPPORTED("94vjlul0tyxbd7e0q4ing2z1h"); //             time = DFS_visit(g, id, time, sp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2rts8n24enk3adhy8owm41yh9"); //     vp->color = 2;
UNSUPPORTED("c4n0lojve186vzv3wqq6dk1pl"); //     /* g->vertices[v].f = time; */
UNSUPPORTED("axvith90awf9705pbyhu4nx38"); //     pushStack (sp, v);
UNSUPPORTED("9pkzxm2gxhkcw8p51ercqleh1"); //     return (time + 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9r0jqlhxs5buqs5hsdqnbj84j
// void top_sort(rawgraph* g) 
public static Object top_sort(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("4yot0xeux4j9xntlfs27gwtcf"); // top_sort(rawgraph* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b9q08fv1def3y3mbkfcttoygi"); //     int i, v;
UNSUPPORTED("c3xjd5j8j3bgmchyoisbyw64b"); //     int time = 0;
UNSUPPORTED("551funk1bxiemly3silcqtngw"); //     int count = 0;
UNSUPPORTED("c7rls4c8tdfpn2b8sp15reiyh"); //     stack* sp;
UNSUPPORTED("9n2rm2xd87zhiqudy5248ij10"); //     if (g->nvs == 0) return;
UNSUPPORTED("5j8ivb7xr19v4nmq7wd60cj5p"); //     if (g->nvs == 1) {
UNSUPPORTED("5p60crx1qes9x8eijukw93td3"); //         g->vertices[0].topsort_order = count;
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dlug72rf2lqioskxr8kgz0r1f"); //     sp = mkStack (g->nvs);
UNSUPPORTED("89rud9nlbi3f8k7rz0vv5vtd8"); //     for(i=0;i<g->nvs;i++) {
UNSUPPORTED("9lczna1336ncq8zrahl9nzymo"); //         if(g->vertices[i].color == 0)
UNSUPPORTED("6ln2w0gjr0kz3163kops6o4p1"); //             time = DFS_visit(g, i, time, sp);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a9tz9ejeuh2cz8adl3rtuxvth"); //     while((v = popStack(sp)) >= 0) {
UNSUPPORTED("9m1c27tt4l4z6f1503uugs6ak"); //         g->vertices[v].topsort_order = count;
UNSUPPORTED("4221wbi45004vm34bns3e6zhv"); //         count++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("71qiyws5c2bt08glef0zb8cfv"); //     freeStack (sp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
