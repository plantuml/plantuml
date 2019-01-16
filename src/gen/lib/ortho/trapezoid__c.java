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

public class trapezoid__c {
//1 9sub06q78sfddgkymxfcy2c73
// static int q_idx


//1 6g6us29cvfladuvel3e1yesjo
// static int tr_idx


//1 d43x33t89dq1mez0urx9oiia6
// static int QSIZE


//1 bweoibjhrqkbu4mf0na0da9i4
// static int TRSIZE




//3 ezwgb38ifqfgyqxu3qqar9lpw
// static int newnode() 
public static Object newnode(Object... arg) {
UNSUPPORTED("7foww06xea9wyyn20fapyohtz"); // static int newnode()
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aqs9lgy5mghk1gc6qzg6yfy4s"); //     if (q_idx < QSIZE)
UNSUPPORTED("8djpsfvmvn0bc1er4s9kph9cj"); // 	return q_idx++;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("11efsmyeab3fqgi4nmlwej7ui"); // 	fprintf(stderr, "newnode: Query-table overflow\n");
UNSUPPORTED("2g1czwq8qs1hp7515i11qbn75"); // 	assert(0);
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4lbfds1s3bs19eoap1a3m3e6z
// static int newtrap(trap_t* tr) 
public static Object newtrap(Object... arg) {
UNSUPPORTED("5u9cievsb4s3x8zwuu8pspcam"); // static int newtrap(trap_t* tr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("d8y9k9n2vzw3s1hdo0dyfgusw"); //     if (tr_idx < TRSIZE) {
UNSUPPORTED("24d5qbi4y7dlh2alv9w6dspvq"); // 	tr[tr_idx].lseg = -1;
UNSUPPORTED("9zrdh7a878ko4cmpzev5ayinb"); // 	tr[tr_idx].rseg = -1;
UNSUPPORTED("74u8379xavsf8burpaazx8doy"); // 	tr[tr_idx].state = 1;
UNSUPPORTED("cp5hfaygeyrh9332itky24tau"); // 	return tr_idx++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("4zebr8hhs7o8tpx01k01mpds0"); // 	fprintf(stderr, "newtrap: Trapezoid-table overflow %d\n", tr_idx);
UNSUPPORTED("2g1czwq8qs1hp7515i11qbn75"); // 	assert(0);
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3vts8knsk95mverrgac2cq3yc
// static int _max (pointf *yval, pointf *v0, pointf *v1) 
public static Object _max(Object... arg) {
UNSUPPORTED("b70ixswaw70rop2i3a10o3usx"); // static int _max (pointf *yval, pointf *v0, pointf *v1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8sp05pzzul51jatoi7t38rnpp"); //   if (v0->y > v1->y + 1.0e-7)
UNSUPPORTED("180yy7z15hk29ewh0m4iwy075"); //     *yval = *v0;
UNSUPPORTED("2ty39vroyyu183wiz8ofw0rxa"); //   else if ((fabs(v0->y - v1->y) <= 1.0e-7))
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("72xrk5q82zxqso4bc60bh0en6"); //       if (v0->x > v1->x + 1.0e-7)
UNSUPPORTED("1cpd3nxmmss753abfkrjus62"); // 	*yval = *v0;
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("90n843sj6wbl3520yksspwwd7"); // 	*yval = *v1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8983svt6g1kt3l45bd6ju9mw6"); //   else
UNSUPPORTED("3alzaeg88yo110ikg55vztkke"); //     *yval = *v1;
UNSUPPORTED("bid671dovx1rdiquw5vm3fttj"); //   return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5wxt3huxqmnpq4koff0wywqeg
// static int _min (pointf *yval, pointf *v0, pointf *v1) 
public static Object _min(Object... arg) {
UNSUPPORTED("41dy0yxvmnu3g6nomhqjilah6"); // static int _min (pointf *yval, pointf *v0, pointf *v1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6kmfzgofw4kdg3jrtg4qesuzn"); //   if (v0->y < v1->y - 1.0e-7)
UNSUPPORTED("180yy7z15hk29ewh0m4iwy075"); //     *yval = *v0;
UNSUPPORTED("2ty39vroyyu183wiz8ofw0rxa"); //   else if ((fabs(v0->y - v1->y) <= 1.0e-7))
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("459y4uw84m4wk21vkdz3fp0q"); //       if (v0->x < v1->x)
UNSUPPORTED("1cpd3nxmmss753abfkrjus62"); // 	*yval = *v0;
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("90n843sj6wbl3520yksspwwd7"); // 	*yval = *v1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8983svt6g1kt3l45bd6ju9mw6"); //   else
UNSUPPORTED("3alzaeg88yo110ikg55vztkke"); //     *yval = *v1;
UNSUPPORTED("bid671dovx1rdiquw5vm3fttj"); //   return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1tgrd4zziresuq6qhk0xfoa92
// static int _greater_than_equal_to (pointf *v0, pointf *v1) 
public static Object _greater_than_equal_to(Object... arg) {
UNSUPPORTED("3x672w6uwysfps5nvhyqmwl95"); // static int _greater_than_equal_to (pointf *v0, pointf *v1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8sp05pzzul51jatoi7t38rnpp"); //   if (v0->y > v1->y + 1.0e-7)
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("cmmtmayq5o6ek8zgcrha6po9p"); //   else if (v0->y < v1->y - 1.0e-7)
UNSUPPORTED("297p5iu8oro94tdg9v29bbgiw"); //     return (0);
UNSUPPORTED("8983svt6g1kt3l45bd6ju9mw6"); //   else
UNSUPPORTED("ccnvf5snmgs9gvqzl6fmieioz"); //     return (v0->x >= v1->x);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ckf51i5h4qoeq20aso56vcc5h
// static int _less_than (pointf *v0, pointf *v1) 
public static Object _less_than(Object... arg) {
UNSUPPORTED("8ancjlh02pjhsn49e6a25wl6r"); // static int _less_than (pointf *v0, pointf *v1)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6kmfzgofw4kdg3jrtg4qesuzn"); //   if (v0->y < v1->y - 1.0e-7)
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("cg91nfse0xdez0vz4ak53mlc6"); //   else if (v0->y > v1->y + 1.0e-7)
UNSUPPORTED("297p5iu8oro94tdg9v29bbgiw"); //     return (0);
UNSUPPORTED("8983svt6g1kt3l45bd6ju9mw6"); //   else
UNSUPPORTED("5gqsmyi9rrv1qx6cgtd2xi44t"); //     return (v0->x < v1->x);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7q4v9wyz2nlemt8xq7v0184fv
// static int  init_query_structure(int segnum, segment_t* seg, trap_t* tr, qnode_t* qs) 
public static Object init_query_structure(Object... arg) {
UNSUPPORTED("d9cz56vtrl0ri6hz88cumukuf"); // static int 
UNSUPPORTED("80esr9k64zjsp5282c35kte4"); // init_query_structure(int segnum, segment_t* seg, trap_t* tr, qnode_t* qs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2esdkeckyaxmhthp1vzn695tv"); //   int i1, i2, i3, i4, i5, i6, i7, root;
UNSUPPORTED("1etoutqq3u32ryiyi4rlsmsro"); //   int t1, t2, t3, t4;
UNSUPPORTED("55wy1clqaecn10s0jetfvt04s"); //   segment_t *s = &seg[segnum];
UNSUPPORTED("c83t5z66410r47uvwhte8atg2"); //   i1 = newnode();
UNSUPPORTED("6tcn5be7qo2ntg03pl4u3r61e"); //   qs[i1].nodetype = 2;
UNSUPPORTED("c1r0tcnd1jjxieidujrboz04k"); //   _max(&qs[i1].yval, &s->v0, &s->v1); /* root */
UNSUPPORTED("f428tco5zyg6ypbzmd6nv8jxo"); //   root = i1;
UNSUPPORTED("1939agqcbtihe79feh1epnhqd"); //   qs[i1].right = i2 = newnode();
UNSUPPORTED("bclzn2z0bot5q64khoc3xo1eh"); //   qs[i2].nodetype = 3;
UNSUPPORTED("3wxpvt6gxhzk59qbw5yybfzok"); //   qs[i2].parent = i1;
UNSUPPORTED("50zz911fku71s62zxmf340erk"); //   qs[i1].left = i3 = newnode();
UNSUPPORTED("a2m2pfcpcs6m23o59w73a3ult"); //   qs[i3].nodetype = 2;
UNSUPPORTED("844hmc2g8n8v8oq1t3pu20waf"); //   _min(&qs[i3].yval, &s->v0, &s->v1); /* root */
UNSUPPORTED("6eqlqz24lil3mz951mbp9aigr"); //   qs[i3].parent = i1;
UNSUPPORTED("6umgkg0ppnkz3jqbvyaxe1m2i"); //   qs[i3].left = i4 = newnode();
UNSUPPORTED("2a5a7b307rm5psez1mdr9nc42"); //   qs[i4].nodetype = 3;
UNSUPPORTED("1qbi9f0emsed1xpkk2p6wqgr1"); //   qs[i4].parent = i3;
UNSUPPORTED("7ijfbel4e53xg0oyi1hdok8qp"); //   qs[i3].right = i5 = newnode();
UNSUPPORTED("d9lgcb4dl6oscs3faq8cipzdp"); //   qs[i5].nodetype = 1;
UNSUPPORTED("3ncb03lht8293piupe9raycfn"); //   qs[i5].segnum = segnum;
UNSUPPORTED("b3g3e6ii94dctnonsv4wd6pdl"); //   qs[i5].parent = i3;
UNSUPPORTED("2hrir3mvn61r4z0suya88l2nq"); //   qs[i5].left = i6 = newnode();
UNSUPPORTED("z8w28kgi1isfybx3itfos5s6"); //   qs[i6].nodetype = 3;
UNSUPPORTED("cu0goy732xo6v416gatn10gvh"); //   qs[i6].parent = i5;
UNSUPPORTED("8mrutb9o0h1by9tk58yfkoqth"); //   qs[i5].right = i7 = newnode();
UNSUPPORTED("4hxmyrxp7ehrl5bbgglxggb3v"); //   qs[i7].nodetype = 3;
UNSUPPORTED("7cv8pxpknvmffsmvootfn7huq"); //   qs[i7].parent = i5;
UNSUPPORTED("47um0g3rf0a66rfc8ix8q7vds"); //   t1 = newtrap(tr);		/* middle left */
UNSUPPORTED("dhqbu2kqod5qekt4mbkpcokkj"); //   t2 = newtrap(tr);		/* middle right */
UNSUPPORTED("9d3tauevh5y1ibiiwo6wp2v76"); //   t3 = newtrap(tr);		/* bottom-most */
UNSUPPORTED("8by6n8cunt2i39aek7qn4bxzm"); //   t4 = newtrap(tr);		/* topmost */
UNSUPPORTED("9ooybm01jfqc70193duq4ut3k"); //   tr[t1].hi = tr[t2].hi = tr[t4].lo = qs[i1].yval;
UNSUPPORTED("ahal1vcbrbvp2tots6ntov40m"); //   tr[t1].lo = tr[t2].lo = tr[t3].hi = qs[i3].yval;
UNSUPPORTED("awhuxi9kh5n3qn62td3x1rx2e"); //   tr[t4].hi.y = (double) (1<<30);
UNSUPPORTED("9yuy8hbx22hhqfluto46v4rb3"); //   tr[t4].hi.x = (double) (1<<30);
UNSUPPORTED("dogql1uip3yn8sty2gnmwevpt"); //   tr[t3].lo.y = (double) -1* (1<<30);
UNSUPPORTED("d2iv9aswbnku15auic7ves2fg"); //   tr[t3].lo.x = (double) -1* (1<<30);
UNSUPPORTED("8y0utktr8o66ckjdf97ig9bw3"); //   tr[t1].rseg = tr[t2].lseg = segnum;
UNSUPPORTED("8my9cfjyt5wyy7n9gqm30i6f"); //   tr[t1].u0 = tr[t2].u0 = t4;
UNSUPPORTED("78rq2swfcihc9nwji63nql4b3"); //   tr[t1].d0 = tr[t2].d0 = t3;
UNSUPPORTED("ebptwtro7kf1luhehrxb744p8"); //   tr[t4].d0 = tr[t3].u0 = t1;
UNSUPPORTED("amaflatnwoybz3paepojnqzjc"); //   tr[t4].d1 = tr[t3].u1 = t2;
UNSUPPORTED("cgyw8wailvfpayg8djskaqm0j"); //   tr[t1].sink = i6;
UNSUPPORTED("2sj6wyx5ww6w67v1loc3qhk4a"); //   tr[t2].sink = i7;
UNSUPPORTED("4h7l33kqm6unri8ilybcbjfcw"); //   tr[t3].sink = i4;
UNSUPPORTED("2wmmcp3dsa7xgu0t7iyv906s2"); //   tr[t4].sink = i2;
UNSUPPORTED("bxw0r5s14kl48z1l94rbm9ur"); //   tr[t1].state = tr[t2].state = 1;
UNSUPPORTED("5n86x6hksj9v0g2mlx1fxaafr"); //   tr[t3].state = tr[t4].state = 1;
UNSUPPORTED("2xueg60vz46kgccrmtjvwznoi"); //   qs[i2].trnum = t4;
UNSUPPORTED("6cc5iezoueqotgmhzeglj4pz9"); //   qs[i4].trnum = t3;
UNSUPPORTED("187dsocnulk5op8i2uxlll76g"); //   qs[i6].trnum = t1;
UNSUPPORTED("eu7m875ftlwx5wo4kxra1uhmp"); //   qs[i7].trnum = t2;
UNSUPPORTED("68vdbfxjp39ycnev1lw2fz5px"); //   s->is_inserted = (!(0));
UNSUPPORTED("8kli59ivt5ad0heic1p0r3dbs"); //   return root;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8ewy3ncs31dca68taarb3iw8t
// static int is_left_of (int segnum, segment_t* seg, pointf *v) 
public static Object is_left_of(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("5njshx2lwca6yvst4l5l2kvum"); // is_left_of (int segnum, segment_t* seg, pointf *v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("55wy1clqaecn10s0jetfvt04s"); //   segment_t *s = &seg[segnum];
UNSUPPORTED("42zj0zwvmsgnq8k1sof5wa9x2"); //   double area;
UNSUPPORTED("63r3u1i01cseh1k50rrs740v"); //   if ((((&s->v1)->y > (&s->v0)->y + 1.0e-7) ? (!(0)) : (((&s->v1)->y < (&s->v0)->y - 1.0e-7) ? (0) : ((&s->v1)->x > (&s->v0)->x)))) /* seg. going upwards */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("cbadnx7r0dofl81ka6xnnq6o7"); //       if ((fabs(s->v1.y - v->y) <= 1.0e-7))
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("cxi8g1p2jce4akg5ny6kbhrfj"); // 	  if (v->x < s->v1.x)
UNSUPPORTED("1fw3mcjmrjyug8inon5ce6dqp"); // 	    area = 1.0;
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("7jzu14omzk1dfuz500k0pmy3i"); // 	    area = -1.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3y16ysaprb1mi29myg2n7khc3"); //       else if ((fabs(s->v0.y - v->y) <= 1.0e-7))
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("blqfzreaz43r05gwx8e2g2mwl"); // 	  if (v->x < s->v0.x)
UNSUPPORTED("1fw3mcjmrjyug8inon5ce6dqp"); // 	    area = 1.0;
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("7jzu14omzk1dfuz500k0pmy3i"); // 	    area = -1.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("9j65co4mvzqq92gveemasixui"); // 	area = (((s->v1).x - (s->v0).x)*(((*v)).y - (s->v0).y) - ((s->v1).y - (s->v0).y)*(((*v)).x - (s->v0).x));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1ifob9844re1ifigjf8fkr37a"); //   else				/* v0 > v1 */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("cbadnx7r0dofl81ka6xnnq6o7"); //       if ((fabs(s->v1.y - v->y) <= 1.0e-7))
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("cxi8g1p2jce4akg5ny6kbhrfj"); // 	  if (v->x < s->v1.x)
UNSUPPORTED("1fw3mcjmrjyug8inon5ce6dqp"); // 	    area = 1.0;
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("7jzu14omzk1dfuz500k0pmy3i"); // 	    area = -1.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3y16ysaprb1mi29myg2n7khc3"); //       else if ((fabs(s->v0.y - v->y) <= 1.0e-7))
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("blqfzreaz43r05gwx8e2g2mwl"); // 	  if (v->x < s->v0.x)
UNSUPPORTED("1fw3mcjmrjyug8inon5ce6dqp"); // 	    area = 1.0;
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("7jzu14omzk1dfuz500k0pmy3i"); // 	    area = -1.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("4mok58gqh8sibhgfd7iko8c9v"); // 	area = (((s->v0).x - (s->v1).x)*(((*v)).y - (s->v1).y) - ((s->v0).y - (s->v1).y)*(((*v)).x - (s->v1).x));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("f01je1mbiagbodxwrd5uinsjb"); //   if (area > 0.0)
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("783dsehm4gdkhlghq59hccnzp"); //   else 
UNSUPPORTED("297p5iu8oro94tdg9v29bbgiw"); //     return (0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ds0k9vbvh7ed069gt2xxgoh8f
// static int inserted (int segnum, segment_t* seg, int whichpt) 
public static Object inserted(Object... arg) {
UNSUPPORTED("1hxgwm2z6apwm18im719wroya"); // static int inserted (int segnum, segment_t* seg, int whichpt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("178ivgdk5i7c18ldleui4zwx8"); //   if (whichpt == 1)
UNSUPPORTED("6ne7u4gw5ulsrhx8n7u6rkli4"); //     return seg[seg[segnum].prev].is_inserted;
UNSUPPORTED("8983svt6g1kt3l45bd6ju9mw6"); //   else
UNSUPPORTED("e67pnyvlna9gyn2uw744phhx8"); //     return seg[seg[segnum].next].is_inserted;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3sd5i6hidutg4bza9cb9iqpxj
// static int  locate_endpoint (pointf *v, pointf *vo, int r, segment_t* seg, qnode_t* qs) 
public static Object locate_endpoint(Object... arg) {
UNSUPPORTED("d9cz56vtrl0ri6hz88cumukuf"); // static int 
UNSUPPORTED("40jqc5hn9dtw9jy159l2bpv0g"); // locate_endpoint (pointf *v, pointf *vo, int r, segment_t* seg, qnode_t* qs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cmndkfofn727hmimgmjjqabmb"); //   qnode_t *rptr = &qs[r];
UNSUPPORTED("9no4uq8gm1skxdt3c5npacs5o"); //   switch (rptr->nodetype) {
UNSUPPORTED("33l7a58zp8vj6fuliwdkk2nkn"); //     case 3:
UNSUPPORTED("chlduh24i0dc156oi8zegnisk"); //       return rptr->trnum;
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("360mq4ndaxyqt1kvu3lx2g9iv"); //       if ((((v)->y > (&rptr->yval)->y + 1.0e-7) ? (!(0)) : (((v)->y < (&rptr->yval)->y - 1.0e-7) ? (0) : ((v)->x > (&rptr->yval)->x)))) /* above */
UNSUPPORTED("1rwlw98jlk3uq5eash5mq3kty"); // 	return locate_endpoint(v, vo, rptr->right, seg, qs);
UNSUPPORTED("2x6nvn1yiw8qe6y0l034l612c"); //       else if (((fabs((v)->y - (&rptr->yval)->y) <= 1.0e-7) && (fabs((v)->x - (&rptr->yval)->x) <= 1.0e-7))) /* the point is already */
UNSUPPORTED("e4032360lx81txp3ugx7q41pg"); // 	{			          /* inserted. */
UNSUPPORTED("5mpyvd6pkc7oidge31j3rtrt4"); // 	  if ((((vo)->y > (&rptr->yval)->y + 1.0e-7) ? (!(0)) : (((vo)->y < (&rptr->yval)->y - 1.0e-7) ? (0) : ((vo)->x > (&rptr->yval)->x)))) /* above */
UNSUPPORTED("9v669sdem5q69ss1a3sedpyhr"); // 	    return locate_endpoint(v, vo, rptr->right, seg, qs);
UNSUPPORTED("embybz3p65nxf0rx8b52bb0cj"); // 	  else 
UNSUPPORTED("4hct334tbu519smetk1d6vmf2"); // 	    return locate_endpoint(v, vo, rptr->left, seg, qs); /* below */	    
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("54lgz3gtv3i7kpaib8hyvgbfz"); // 	return locate_endpoint(v, vo, rptr->left, seg, qs); /* below */
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("9lajd4oadrk3ahujglee1gdtr"); //       if (((fabs((v)->y - (&seg[rptr->segnum].v0)->y) <= 1.0e-7) && (fabs((v)->x - (&seg[rptr->segnum].v0)->x) <= 1.0e-7)) || 
UNSUPPORTED("f48mzifmh37dv8tr0qtoo49je"); // 	       ((fabs((v)->y - (&seg[rptr->segnum].v1)->y) <= 1.0e-7) && (fabs((v)->x - (&seg[rptr->segnum].v1)->x) <= 1.0e-7)))
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("9z6xoklpdo4zj5pno4f5ce0h7"); // 	  if ((fabs(v->y - vo->y) <= 1.0e-7)) /* horizontal segment */
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("er0sr1b1xyl1koezju4fce9s9"); // 	      if (vo->x < v->x)
UNSUPPORTED("btfxao9lmldnm9ox00uhp9k89"); // 		return locate_endpoint(v, vo, rptr->left, seg, qs); /* left */
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("46b4vvmvw19uagy9lolvbjpva"); // 		return locate_endpoint(v, vo, rptr->right, seg, qs); /* right */
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7h5s2qfodilr20w0xkcpxkptk"); // 	  else if (is_left_of(rptr->segnum, seg, vo))
UNSUPPORTED("42gk6xyx9b3ob9l5n3r5csyt1"); // 	    return locate_endpoint(v, vo, rptr->left, seg, qs); /* left */
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("b95yqgth0rp8t6hd608cwibcv"); // 	    return locate_endpoint(v, vo, rptr->right, seg, qs); /* right */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9h1dd7wy66jgqp0wcr4kep5c2"); //       else if (is_left_of(rptr->segnum, seg, v))
UNSUPPORTED("2q0jgs1w9wo5aut83ngsh2kqr"); // 	return locate_endpoint(v, vo, rptr->left, seg, qs); /* left */
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("56jpzw5vjtdztvgezig0lr54s"); // 	return locate_endpoint(v, vo, rptr->right, seg, qs); /* right */	
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("7popo524s1mkw4my7n7tm8zwm"); //       fprintf(stderr, "unexpected case in locate_endpoint\n");
UNSUPPORTED("2kc7vwb5ge1ym5i0vk3t2ku3u"); //       assert (0);
UNSUPPORTED("aihsv59rc0q4ji937lr34lo8k"); //       break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1rl8ucq8uwd451ybs5xqlr67w"); //     return 1; /* stop warning */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dbmj7tlr6ld9ptryu0lsaq4tc
// static void merge_trapezoids (int segnum, int tfirst, int tlast, int side, trap_t* tr,     qnode_t* qs) 
public static Object merge_trapezoids(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("d3sy6vv9zv5n4mb41oxk1obz6"); // merge_trapezoids (int segnum, int tfirst, int tlast, int side, trap_t* tr,
UNSUPPORTED("3yigib6ow3jx4tobp06lwnbpn"); //     qnode_t* qs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7zesosg72mooutvqm92puylwn"); //   int t, tnext, cond;
UNSUPPORTED("srqa51ewat44yd9ccq23akdt"); //   int ptnext;
UNSUPPORTED("dumb9eoy9ae3crz585y5gm7jd"); //   /* First merge polys on the LHS */
UNSUPPORTED("8dfvttfe7lc7i6u3r5ecr1fu7"); //   t = tfirst;
UNSUPPORTED("4prw3c0s3s628lu72k6lantaz"); //   while ((t > 0) && _greater_than_equal_to(&tr[t].lo, &tr[tlast].lo))
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("e01nwr4jvd1tahpmnshgnq2jv"); //       if (side == 1)
UNSUPPORTED("clu6aa5l65xiz793joqfsuqpj"); // 	cond = ((((tnext = tr[t].d0) > 0) && (tr[tnext].rseg == segnum)) ||
UNSUPPORTED("58j0umsvte3xelv7t0rq6gjmz"); // 		(((tnext = tr[t].d1) > 0) && (tr[tnext].rseg == segnum)));
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("ay6y0octsk8vv6mxe73k52poq"); // 	cond = ((((tnext = tr[t].d0) > 0) && (tr[tnext].lseg == segnum)) ||
UNSUPPORTED("9giajatrisho4roj4jmi2mtdu"); // 		(((tnext = tr[t].d1) > 0) && (tr[tnext].lseg == segnum)));
UNSUPPORTED("9jg0aj4lchk2qzrqzcn6ou93"); //       if (cond)
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("4sfxjqv3pdk6ccs6trnltbq2f"); // 	  if ((tr[t].lseg == tr[tnext].lseg) &&
UNSUPPORTED("8waxrtn63q8iol80ocgfo5avl"); // 	      (tr[t].rseg == tr[tnext].rseg)) /* good neighbours */
UNSUPPORTED("4zrza3uxjr5k879ok6o3ajyq5"); // 	    {			              /* merge them */
UNSUPPORTED("de9a84mtxjfcb7d29uq6ysi80"); // 	      /* Use the upper node as the new node i.e. t */
UNSUPPORTED("5hnggs6fldg6n2aa0v0wb7mhs"); // 	      ptnext = qs[tr[tnext].sink].parent;
UNSUPPORTED("4aftlg6xged6gwbjcopvljfdk"); // 	      if (qs[ptnext].left == tr[tnext].sink)
UNSUPPORTED("f034psp1axv24k350wb0intx5"); // 		qs[ptnext].left = tr[t].sink;
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("erqbnxbh6c3ixq6cid3y15v6f"); // 		qs[ptnext].right = tr[t].sink;	/* redirect parent */
UNSUPPORTED("f51s02y9sf5wp5418x4hjsyr3"); // 	      /* Change the upper neighbours of the lower trapezoids */
UNSUPPORTED("4s40zetcif254u9y9h8shaf7j"); // 	      if ((tr[t].d0 = tr[tnext].d0) > 0) {
UNSUPPORTED("7kf24rblb1mya5dy87gfod71g"); // 		if (tr[tr[t].d0].u0 == tnext)
UNSUPPORTED("5ippcybc2d61q6we5qdy4i2qa"); // 		  tr[tr[t].d0].u0 = t;
UNSUPPORTED("4n5ukkxajjh25cyvxn2mfv7p5"); // 		else if (tr[tr[t].d0].u1 == tnext)
UNSUPPORTED("49bf3poo5ld3e7zfa7owoqwkt"); // 		  tr[tr[t].d0].u1 = t;
UNSUPPORTED("1rlnjstsgqacqe09a5425xsv7"); // 	      }
UNSUPPORTED("cx0c40s7cejokjr55q624jp8i"); // 	      if ((tr[t].d1 = tr[tnext].d1) > 0) {
UNSUPPORTED("6tk5voiwfy6257or9pk26rpls"); // 		if (tr[tr[t].d1].u0 == tnext)
UNSUPPORTED("8vwe6jjx4f0cxtyk1cjsred13"); // 		  tr[tr[t].d1].u0 = t;
UNSUPPORTED("faags7rxrk8rz9u528brw9w1"); // 		else if (tr[tr[t].d1].u1 == tnext)
UNSUPPORTED("c1g7dyxu9y9bu3cnqai6ffpai"); // 		  tr[tr[t].d1].u1 = t;
UNSUPPORTED("1rlnjstsgqacqe09a5425xsv7"); // 	      }
UNSUPPORTED("rqi8hsnl6a07uwsdllnklsov"); // 	      tr[t].lo = tr[tnext].lo;
UNSUPPORTED("alq15ri09p1ftxvstnhiy11pw"); // 	      tr[tnext].state = 2; /* invalidate the lower */
UNSUPPORTED("7g5imzgc3x4g8m1764txnxwyg"); // 				            /* trapezium */
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("22mnxtwfg5bxnxn9hi92d3s98"); // 	  else		    /* not good neighbours */
UNSUPPORTED("62cajrs5gvbcki7xpbugxr503"); // 	    t = tnext;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cy3s4c19cvi3od7ig0v5w1dau"); //       else		    /* do not satisfy the outer if */
UNSUPPORTED("9ndn482wriowrzfxei5dwe91s"); // 	t = tnext;
UNSUPPORTED("65mlofu9i9895kq8xkb6x103e"); //     } /* end-while */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 423adx9iu4aqsh6kmh9d6a9be
// static int  add_segment (int segnum, segment_t* seg, trap_t* tr, qnode_t* qs) 
public static Object add_segment(Object... arg) {
UNSUPPORTED("d9cz56vtrl0ri6hz88cumukuf"); // static int 
UNSUPPORTED("6kdmtyxbba7hab6oumuavm30n"); // add_segment (int segnum, segment_t* seg, trap_t* tr, qnode_t* qs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aywleif3lrmut4f9ns6y61k57"); //   segment_t s;
UNSUPPORTED("364wkucwxm1xu5s9elyid5hh4"); //   int tu, tl, sk, tfirst, tlast;
UNSUPPORTED("3sfv77g4x70yfiew8fyfwgniw"); //   int tfirstr, tlastr, tfirstl, tlastl;
UNSUPPORTED("hfh89kyneima7i4d8yp0hbwi"); //   int i1, i2, t, tn;
UNSUPPORTED("8z6gljvt2mmr0150xzv8dl25q"); //   pointf tpt;
UNSUPPORTED("dnwffbpvy39x5k4uyp0o8auxh"); //   int tritop = 0, tribot = 0, is_swapped;
UNSUPPORTED("94kgtpn18xur5803w9zeeemzl"); //   int tmptriseg;
UNSUPPORTED("1pyedo5b6xqv0d871wz5tos5c"); //   s = seg[segnum];
UNSUPPORTED("8gkp133xtnguno18r03aljy2z"); //   if ((((&s.v1)->y > (&s.v0)->y + 1.0e-7) ? (!(0)) : (((&s.v1)->y < (&s.v0)->y - 1.0e-7) ? (0) : ((&s.v1)->x > (&s.v0)->x)))) /* Get higher vertex in v0 */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("cwmbl7chjorytaifkyv8kht3c"); //       int tmp;
UNSUPPORTED("5luhts0zxuhi3potzediaro1z"); //       tpt = s.v0;
UNSUPPORTED("3zornvro5dwtu8yscak3thc3j"); //       s.v0 = s.v1;
UNSUPPORTED("7xkhhzhpalzk4r57usnt0ei5g"); //       s.v1 = tpt;
UNSUPPORTED("bqhqxuz448ejhe47314s7ywqn"); //       tmp = s.root0;
UNSUPPORTED("2re8ujw6s9kgcmuthp39hguph"); //       s.root0 = s.root1;
UNSUPPORTED("75kknuapl2tfu08nxm6s55sm1"); //       s.root1 = tmp;
UNSUPPORTED("9eflrb5pudqbmm3wm6qeiwwsz"); //       is_swapped = (!(0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("kf5313k7om91mo87n2t6l6mk"); //   else is_swapped = (0);
UNSUPPORTED("9fmss7ubwydpupsrikix6nafv"); //   if ((is_swapped) ? !inserted(segnum, seg, 2) :
UNSUPPORTED("6hcg7juc159xaz2xirhecwc5n"); //        !inserted(segnum, seg, 1))     /* insert v0 in the tree */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("c8xc5g4gm8dkiy87rty8nml52"); //       int tmp_d;
UNSUPPORTED("2qpiovudynlersguqz1by6yg8"); //       tu = locate_endpoint(&s.v0, &s.v1, s.root0, seg, qs);
UNSUPPORTED("3vk36g0d9b4pv8frx4h1q7url"); //       tl = newtrap(tr);		/* tl is the new lower trapezoid */
UNSUPPORTED("1lylt0f9mi470jwcjjrujr0lv"); //       tr[tl].state = 1;
UNSUPPORTED("e36yrv3eo2yxyqvkbbxaji21d"); //       tr[tl] = tr[tu];
UNSUPPORTED("9wngpyofpofpk3iw7x1nua784"); //       tr[tu].lo.y = tr[tl].hi.y = s.v0.y;
UNSUPPORTED("kayts5od8vhhpy0xbhr9dkxg"); //       tr[tu].lo.x = tr[tl].hi.x = s.v0.x;
UNSUPPORTED("5boxd28ndhyga6l1be8q33e4q"); //       tr[tu].d0 = tl;      
UNSUPPORTED("40mjz1kftaduly7n34bovhzoa"); //       tr[tu].d1 = 0;
UNSUPPORTED("8ohfd1sq3vt575ufy2sejpq49"); //       tr[tl].u0 = tu;
UNSUPPORTED("er5qcoyhp181kav6eod7im5xk"); //       tr[tl].u1 = 0;
UNSUPPORTED("dmb9ggnvbbi4oj28pzhxe0i4s"); //       if (((tmp_d = tr[tl].d0) > 0) && (tr[tmp_d].u0 == tu))
UNSUPPORTED("7dung2mkyltlpc6rn6xg2w2qa"); // 	tr[tmp_d].u0 = tl;
UNSUPPORTED("6mzktvtijtpt77jiqbxio9zos"); //       if (((tmp_d = tr[tl].d0) > 0) && (tr[tmp_d].u1 == tu))
UNSUPPORTED("4hk19hnhoihfn3k52lnb5wnmi"); // 	tr[tmp_d].u1 = tl;
UNSUPPORTED("50ybcyf9j71skb8bxbrvd4wfo"); //       if (((tmp_d = tr[tl].d1) > 0) && (tr[tmp_d].u0 == tu))
UNSUPPORTED("7dung2mkyltlpc6rn6xg2w2qa"); // 	tr[tmp_d].u0 = tl;
UNSUPPORTED("burj10cn6158fzm20uvqf635h"); //       if (((tmp_d = tr[tl].d1) > 0) && (tr[tmp_d].u1 == tu))
UNSUPPORTED("4hk19hnhoihfn3k52lnb5wnmi"); // 	tr[tmp_d].u1 = tl;
UNSUPPORTED("511t3099c7ezfx13h8n2yb4qc"); //       /* Now update the query structure and obtain the sinks for the */
UNSUPPORTED("c9xihvs3g1nqpo1l2de28bsb"); //       /* two trapezoids */ 
UNSUPPORTED("csfd7w5pm87z8w5eoyoa2movw"); //       i1 = newnode();		/* Upper trapezoid sink */
UNSUPPORTED("564u4l90sl4dmr5g58s7iqs8x"); //       i2 = newnode();		/* Lower trapezoid sink */
UNSUPPORTED("7oz11ubssufczo1a50yljtcvy"); //       sk = tr[tu].sink;
UNSUPPORTED("b677gy1yh3616vmj895bab5b"); //       qs[sk].nodetype = 2;
UNSUPPORTED("k2rmuj3cg2b11t520einze0k"); //       qs[sk].yval = s.v0;
UNSUPPORTED("bxfy7g6fyf60ts626mpqxu6se"); //       qs[sk].segnum = segnum;	/* not really reqd ... maybe later */
UNSUPPORTED("89s0xn0ow2pfjhij4atarki22"); //       qs[sk].left = i2;
UNSUPPORTED("41dfb6dqvo0ska2nsf8cg7mlp"); //       qs[sk].right = i1;
UNSUPPORTED("e9qlif497bsjdb7r26vnxr6zv"); //       qs[i1].nodetype = 3;
UNSUPPORTED("20z4ufoaz5upwcj5cwcfdbsxt"); //       qs[i1].trnum = tu;
UNSUPPORTED("3sg533hzyg1vg1p3yue1tnscq"); //       qs[i1].parent = sk;
UNSUPPORTED("b7ik0xdl2ccze3lyok2lk1u8i"); //       qs[i2].nodetype = 3;
UNSUPPORTED("dw3di9wgg1xfyl4volkqwvr2a"); //       qs[i2].trnum = tl;
UNSUPPORTED("1aovaau4d0p4kaw0ceoit4mk8"); //       qs[i2].parent = sk;
UNSUPPORTED("bv09rvz94qyrsm8yxn6ib3gjz"); //       tr[tu].sink = i1;
UNSUPPORTED("edgs0wz1q5yh76ulwbge7k0y5"); //       tr[tl].sink = i2;
UNSUPPORTED("6lodj5exd6zj9qf7jjt4ajwcb"); //       tfirst = tl;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9ai90pnlwcnwugiwlrp31rno1"); //   else				/* v0 already present */
UNSUPPORTED("1na80phemhx8r2c3bejhbvi9q"); //     {       /* Get the topmost intersecting trapezoid */
UNSUPPORTED("bux5mye3492eeh39qlfwu4h0i"); //       tfirst = locate_endpoint(&s.v0, &s.v1, s.root0, seg, qs);
UNSUPPORTED("9sgryoanil25eur6sx2wvst7w"); //       tritop = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("73p090oxqw3wxq9qn6hpedl6r"); //   if ((is_swapped) ? !inserted(segnum, seg, 1) :
UNSUPPORTED("4nw2s0j85w6cpo3ch1vo46vxr"); //        !inserted(segnum, seg, 2))     /* insert v1 in the tree */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("c8xc5g4gm8dkiy87rty8nml52"); //       int tmp_d;
UNSUPPORTED("8k5whoux68dwuz2ebzjwdz2lh"); //       tu = locate_endpoint(&s.v1, &s.v0, s.root1, seg, qs);
UNSUPPORTED("3vk36g0d9b4pv8frx4h1q7url"); //       tl = newtrap(tr);		/* tl is the new lower trapezoid */
UNSUPPORTED("1lylt0f9mi470jwcjjrujr0lv"); //       tr[tl].state = 1;
UNSUPPORTED("e36yrv3eo2yxyqvkbbxaji21d"); //       tr[tl] = tr[tu];
UNSUPPORTED("7dp0os3ywo9qpt13od74hlz32"); //       tr[tu].lo.y = tr[tl].hi.y = s.v1.y;
UNSUPPORTED("b9h60u19dl3vo6dnfe9rl89ki"); //       tr[tu].lo.x = tr[tl].hi.x = s.v1.x;
UNSUPPORTED("5boxd28ndhyga6l1be8q33e4q"); //       tr[tu].d0 = tl;      
UNSUPPORTED("40mjz1kftaduly7n34bovhzoa"); //       tr[tu].d1 = 0;
UNSUPPORTED("8ohfd1sq3vt575ufy2sejpq49"); //       tr[tl].u0 = tu;
UNSUPPORTED("er5qcoyhp181kav6eod7im5xk"); //       tr[tl].u1 = 0;
UNSUPPORTED("dmb9ggnvbbi4oj28pzhxe0i4s"); //       if (((tmp_d = tr[tl].d0) > 0) && (tr[tmp_d].u0 == tu))
UNSUPPORTED("7dung2mkyltlpc6rn6xg2w2qa"); // 	tr[tmp_d].u0 = tl;
UNSUPPORTED("6mzktvtijtpt77jiqbxio9zos"); //       if (((tmp_d = tr[tl].d0) > 0) && (tr[tmp_d].u1 == tu))
UNSUPPORTED("4hk19hnhoihfn3k52lnb5wnmi"); // 	tr[tmp_d].u1 = tl;
UNSUPPORTED("50ybcyf9j71skb8bxbrvd4wfo"); //       if (((tmp_d = tr[tl].d1) > 0) && (tr[tmp_d].u0 == tu))
UNSUPPORTED("7dung2mkyltlpc6rn6xg2w2qa"); // 	tr[tmp_d].u0 = tl;
UNSUPPORTED("burj10cn6158fzm20uvqf635h"); //       if (((tmp_d = tr[tl].d1) > 0) && (tr[tmp_d].u1 == tu))
UNSUPPORTED("4hk19hnhoihfn3k52lnb5wnmi"); // 	tr[tmp_d].u1 = tl;
UNSUPPORTED("511t3099c7ezfx13h8n2yb4qc"); //       /* Now update the query structure and obtain the sinks for the */
UNSUPPORTED("c9xihvs3g1nqpo1l2de28bsb"); //       /* two trapezoids */ 
UNSUPPORTED("csfd7w5pm87z8w5eoyoa2movw"); //       i1 = newnode();		/* Upper trapezoid sink */
UNSUPPORTED("564u4l90sl4dmr5g58s7iqs8x"); //       i2 = newnode();		/* Lower trapezoid sink */
UNSUPPORTED("7oz11ubssufczo1a50yljtcvy"); //       sk = tr[tu].sink;
UNSUPPORTED("b677gy1yh3616vmj895bab5b"); //       qs[sk].nodetype = 2;
UNSUPPORTED("b27knotih9bj8ve0japzl9l9v"); //       qs[sk].yval = s.v1;
UNSUPPORTED("bxfy7g6fyf60ts626mpqxu6se"); //       qs[sk].segnum = segnum;	/* not really reqd ... maybe later */
UNSUPPORTED("89s0xn0ow2pfjhij4atarki22"); //       qs[sk].left = i2;
UNSUPPORTED("41dfb6dqvo0ska2nsf8cg7mlp"); //       qs[sk].right = i1;
UNSUPPORTED("e9qlif497bsjdb7r26vnxr6zv"); //       qs[i1].nodetype = 3;
UNSUPPORTED("20z4ufoaz5upwcj5cwcfdbsxt"); //       qs[i1].trnum = tu;
UNSUPPORTED("3sg533hzyg1vg1p3yue1tnscq"); //       qs[i1].parent = sk;
UNSUPPORTED("b7ik0xdl2ccze3lyok2lk1u8i"); //       qs[i2].nodetype = 3;
UNSUPPORTED("dw3di9wgg1xfyl4volkqwvr2a"); //       qs[i2].trnum = tl;
UNSUPPORTED("1aovaau4d0p4kaw0ceoit4mk8"); //       qs[i2].parent = sk;
UNSUPPORTED("bv09rvz94qyrsm8yxn6ib3gjz"); //       tr[tu].sink = i1;
UNSUPPORTED("edgs0wz1q5yh76ulwbge7k0y5"); //       tr[tl].sink = i2;
UNSUPPORTED("n3654qaak8jk8d5on2gwnw3f"); //       tlast = tu;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("71qg7qvginab7d0qflim4y82r"); //   else				/* v1 already present */
UNSUPPORTED("e56qskbf1f1spsk53sle0yg9x"); //     {       /* Get the lowermost intersecting trapezoid */
UNSUPPORTED("f5e0c0cb0tzted54oxk1lg8so"); //       tlast = locate_endpoint(&s.v1, &s.v0, s.root1, seg, qs);
UNSUPPORTED("19802pxxzzbhz30pxvxhuauza"); //       tribot = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6or2wr1m9e6t9eswiywjcnari"); //   /* Thread the segment into the query tree creating a new X-node */
UNSUPPORTED("953uyi8s6j0kjt6kii0ifi05n"); //   /* First, split all the trapezoids which are intersected by s into */
UNSUPPORTED("2ec1eki04zl9bzcirfgpxwz1d"); //   /* two */
UNSUPPORTED("7vltbln6jmb4dcnt727aotqu"); //   t = tfirst;			/* topmost trapezoid */
UNSUPPORTED("86huck5mq1n2xe2bgd5h0g4qh"); //   while ((t > 0) && 
UNSUPPORTED("9a2osatsmh7im166493j5z9ql"); // 	 _greater_than_equal_to(&tr[t].lo, &tr[tlast].lo))
UNSUPPORTED("8q0qvfyu6b98xudyzf1jey121"); // 				/* traverse from top to bot */
UNSUPPORTED("6ld19omy1z68vprfzbhrjqr2z"); //     {
UNSUPPORTED("c361t9ie1jl34waoz1qx7qglt"); //       int t_sav, tn_sav;
UNSUPPORTED("735i6dbhhrwalqp0rdzf1plzu"); //       sk = tr[t].sink;
UNSUPPORTED("11cd0f59j2rlnu33mg33fy21u"); //       i1 = newnode();		/* left trapezoid sink */
UNSUPPORTED("8iq31y998euwxxys7lz4i0di5"); //       i2 = newnode();		/* right trapezoid sink */
UNSUPPORTED("eki95gcvgxhhxodpx7hhb2ms"); //       qs[sk].nodetype = 1;
UNSUPPORTED("bic1vt1c10xyb4dh0jvmlwitc"); //       qs[sk].segnum = segnum;
UNSUPPORTED("1oop28nporjqqttdge2bzb9yr"); //       qs[sk].left = i1;
UNSUPPORTED("9j0y61zaus712wgthf8xl2bjs"); //       qs[sk].right = i2;
UNSUPPORTED("a2vaedsgrsasf749gokb7o6zn"); //       qs[i1].nodetype = 3;	/* left trapezoid (use existing one) */
UNSUPPORTED("d3wvuzqx7whtc7lkr3a1c6x1e"); //       qs[i1].trnum = t;
UNSUPPORTED("3sg533hzyg1vg1p3yue1tnscq"); //       qs[i1].parent = sk;
UNSUPPORTED("4f8cdthira5gw458qa3g8n1pw"); //       qs[i2].nodetype = 3;	/* right trapezoid (allocate new) */
UNSUPPORTED("72zn9njck31ow5p4cd7x2vhdp"); //       qs[i2].trnum = tn = newtrap(tr);
UNSUPPORTED("cs7pcwh6y85gsxjbjcd3fp4th"); //       tr[tn].state = 1;
UNSUPPORTED("1aovaau4d0p4kaw0ceoit4mk8"); //       qs[i2].parent = sk;
UNSUPPORTED("8tii09oyev9eg5v8tjs5xqnzi"); //       if (t == tfirst)
UNSUPPORTED("c7vntoti83d7nzm1pez02dojb"); // 	tfirstr = tn;
UNSUPPORTED("4lrakx12kpsm7brfxuacfvepm"); //       if (((fabs((&tr[t].lo)->y - (&tr[tlast].lo)->y) <= 1.0e-7) && (fabs((&tr[t].lo)->x - (&tr[tlast].lo)->x) <= 1.0e-7)))
UNSUPPORTED("dyxxoq2rclqc042m49pabo5tu"); // 	tlastr = tn;
UNSUPPORTED("3m5dvd9h55xvklqd7bt4yyg18"); //       tr[tn] = tr[t];
UNSUPPORTED("4m7mubrux1t63hm4v0mbdoft0"); //       tr[t].sink = i1;
UNSUPPORTED("34b00t67vxmsuf1t0urk7kfni"); //       tr[tn].sink = i2;
UNSUPPORTED("5ilkt84avyzqnbgenie56bfpa"); //       t_sav = t;
UNSUPPORTED("1ndtxyyz47mtzmcxjr2kg61ci"); //       tn_sav = tn;
UNSUPPORTED("8fi9adh8qsrwfrgcxhrxnygb5"); //       /* error */
UNSUPPORTED("9ufele7a3qptly4g8e4gzkr4u"); //       if ((tr[t].d0 <= 0) && (tr[t].d1 <= 0)) /* case cannot arise */
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("2j1bqg7boruaga6kpx00agtvb"); // 	  fprintf(stderr, "add_segment: error\n");
UNSUPPORTED("5tzm1n3i8u3c1oms5ri2fiqbt"); // 	  break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("787ro5xblsfkpjif4xda30sc2"); //       /* only one trapezoid below. partition t into two and make the */
UNSUPPORTED("5j474wt6fmynj8yasccaob1py"); //       /* two resulting trapezoids t and tn as the upper neighbours of */
UNSUPPORTED("aqtupurtnt0dzlj5jafgtirwc"); //       /* the sole lower trapezoid */
UNSUPPORTED("ewm9o917kyj2ae62y3e5bdvqf"); //       else if ((tr[t].d0 > 0) && (tr[t].d1 <= 0))
UNSUPPORTED("9cj5nz12eopffdahw5emn4c08"); // 	{			/* Only one trapezoid below */
UNSUPPORTED("6a1dcxf2rsr9zq5pxrpuecig1"); // 	  if ((tr[t].u0 > 0) && (tr[t].u1 > 0))
UNSUPPORTED("4vladne5kzv3xueclq0sxcjho"); // 	    {			/* continuation of a chain from abv. */
UNSUPPORTED("bwewd7qprc37qhwxz3ko1y4uq"); // 	      if (tr[t].usave > 0) /* three upper neighbours */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("esaqksoargwy10mkyzhmv2p9v"); // 		  if (tr[t].uside == 1)
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("82gucb8vt172fhq2ug560dgje"); // 		      tr[tn].u0 = tr[t].u1;
UNSUPPORTED("lgzjorwqs1rmmyj7q8jzt16e"); // 		      tr[t].u1 = -1;
UNSUPPORTED("6wku8ik0lpiqemv9dmew6dodf"); // 		      tr[tn].u1 = tr[t].usave;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("30jz3yn7hygbk25oykenqo2tp"); // 		      tr[tr[tn].u0].d0 = tn;
UNSUPPORTED("ehcdy9xx8x9l0nz0oybez912h"); // 		      tr[tr[tn].u1].d0 = tn;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6cf2pwwuzbumwpxili1otryjb"); // 		  else		/* intersects in the right */
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("1zxekgfse665xkba3vu2wy0c0"); // 		      tr[tn].u1 = -1;
UNSUPPORTED("82gucb8vt172fhq2ug560dgje"); // 		      tr[tn].u0 = tr[t].u1;
UNSUPPORTED("dedip96pnjatmrxwr3szb2ltb"); // 		      tr[t].u1 = tr[t].u0;
UNSUPPORTED("be5obz58np1ogzt1m8welxd6c"); // 		      tr[t].u0 = tr[t].usave;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("2d4udv8v764lhooiia78atj4b"); // 		      tr[tr[t].u1].d0 = t;
UNSUPPORTED("72euqk1bum10xtl1bm12ug86r"); // 		      tr[tr[tn].u0].d0 = tn;		      
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("d52osx7vrtw97wm9ut9vfa5wj"); // 		  tr[t].usave = tr[tn].usave = 0;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("91694gf7sahoxvmiw82owvef2"); // 	      else		/* No usave.... simple case */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("e2zdzb3rlpq6pieuayb44oijq"); // 		  tr[tn].u0 = tr[t].u1;
UNSUPPORTED("6291oktyj53xp9yslzj405wgb"); // 		  tr[t].u1 = tr[tn].u1 = -1;
UNSUPPORTED("ay4xwmue8kz8uzp2wgge6752p"); // 		  tr[tr[tn].u0].d0 = tn;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("embybz3p65nxf0rx8b52bb0cj"); // 	  else 
UNSUPPORTED("2auoskyvfrf0wu8ekti5fs1n4"); // 	    {			/* fresh seg. or upward cusp */
UNSUPPORTED("2hrv5rm87wox9rp79f6wpn619"); // 	      int tmp_u = tr[t].u0;
UNSUPPORTED("8zaj2aem34dupb1plqsm8uwk5"); // 	      int td0, td1;
UNSUPPORTED("6f3f5nfgz2txzlmdcntsu2yyx"); // 	      if (((td0 = tr[tmp_u].d0) > 0) && 
UNSUPPORTED("7ed0ylnawdzj4xmtq0ih0wt7o"); // 		  ((td1 = tr[tmp_u].d1) > 0))
UNSUPPORTED("8dh5ldf8m83e9ngo1yqfbo7m1"); // 		{		/* upward cusp */
UNSUPPORTED("62v7ooft04km5zqvbpql7o5ps"); // 		  if ((tr[td0].rseg > 0) &&
UNSUPPORTED("4hx3tu7xyeswnab7x0q6yoefl"); // 		      !is_left_of(tr[td0].rseg, seg, &s.v1))
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("201vn0jou9fcf0n2tjih472mz"); // 		      tr[t].u0 = tr[t].u1 = tr[tn].u1 = -1;
UNSUPPORTED("rg3tngg7tj2qgf4erhpbcau8"); // 		      tr[tr[tn].u0].d1 = tn;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("crxpsdr3uqrfkq55ecsr4zuxi"); // 		  else		/* cusp going leftwards */
UNSUPPORTED("bkwn0jotk953x4wcx60j0p47f"); // 		    { 
UNSUPPORTED("e9ql35kajykty3zsmtldh0wzb"); // 		      tr[tn].u0 = tr[tn].u1 = tr[t].u1 = -1;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dayzocp22vmevtvo1awep6h63"); // 	      else		/* fresh segment */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("1i8cfmzh5xxisiuekr63cd7hs"); // 		  tr[tr[t].u0].d0 = t;
UNSUPPORTED("694mejpoguhaimtc4oglzp817"); // 		  tr[tr[t].u0].d1 = tn;
UNSUPPORTED("5m5bo8wuz7zrobc77883wcpw1"); // 		}	      
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("e0kb482en9la96mrxyisdyquw"); // 	  if ((fabs(tr[t].lo.y - tr[tlast].lo.y) <= 1.0e-7) && 
UNSUPPORTED("8d08n1tz1fajvu4tkkmyvqto3"); // 	      (fabs(tr[t].lo.x - tr[tlast].lo.x) <= 1.0e-7) && tribot)
UNSUPPORTED("2pqges4a601rkdgoh9ay4fwkc"); // 	    {		/* bottom forms a triangle */
UNSUPPORTED("74q36498zx96m389wznj126jh"); // 	      if (is_swapped)	
UNSUPPORTED("c6l0dqua1wi4sm2nno83tqjpu"); // 		tmptriseg = seg[segnum].prev;
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("cr8el00rgafuidxf7dkqmprk9"); // 		tmptriseg = seg[segnum].next;
UNSUPPORTED("7m5vjpo3bvkkv7dehsu8lk1i0"); // 	      if ((tmptriseg > 0) && is_left_of(tmptriseg, seg, &s.v0))
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("vgbcetfcktd1hjnqclhp7w01"); // 				/* L-R downward cusp */
UNSUPPORTED("5ippcybc2d61q6we5qdy4i2qa"); // 		  tr[tr[t].d0].u0 = t;
UNSUPPORTED("ejo4cyfym8ojporxegfwpinht"); // 		  tr[tn].d0 = tr[tn].d1 = -1;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("eldt3ci812qmne3db4ojeuhz7"); // 				/* R-L downward cusp */
UNSUPPORTED("8g2jcubbcwsaaenx674f63kth"); // 		  tr[tr[tn].d0].u1 = tn;
UNSUPPORTED("3c4euuu782207qdjnwrjswbyy"); // 		  tr[t].d0 = tr[t].d1 = -1;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("cad2a1rc10cprtvojmol6lcxf"); // 	      if ((tr[tr[t].d0].u0 > 0) && (tr[tr[t].d0].u1 > 0))
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("6ilx5r7u9y1m64f57y23qxlii"); // 		  if (tr[tr[t].d0].u0 == t) /* passes thru LHS */
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("5my98yb1h8davodq05wl96zgu"); // 		      tr[tr[t].d0].usave = tr[tr[t].d0].u1;
UNSUPPORTED("5lykreqcj4w4ugg2fshp1085g"); // 		      tr[tr[t].d0].uside = 1;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("12hbppj0gv84xkilzofj6ohze"); // 		  else
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("azh2ilm9el27ve1j7t3o8lo3k"); // 		      tr[tr[t].d0].usave = tr[tr[t].d0].u0;
UNSUPPORTED("2wjhisme7ontii2rgbvel3qza"); // 		      tr[tr[t].d0].uside = 2;
UNSUPPORTED("ctzn5o8xqkywd73gcu6ofjsb7"); // 		    }		    
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3vlkssstu9yp43651wjntawrq"); // 	      tr[tr[t].d0].u0 = t;
UNSUPPORTED("dy31y8zhzxmyas9wde5g3vvlh"); // 	      tr[tr[t].d0].u1 = tn;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("er72bs1z1fiixf6x2m2ck5218"); // 	  t = tr[t].d0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3vmznfuejkuwxfecdhhksjlqn"); //       else if ((tr[t].d0 <= 0) && (tr[t].d1 > 0))
UNSUPPORTED("9cj5nz12eopffdahw5emn4c08"); // 	{			/* Only one trapezoid below */
UNSUPPORTED("6a1dcxf2rsr9zq5pxrpuecig1"); // 	  if ((tr[t].u0 > 0) && (tr[t].u1 > 0))
UNSUPPORTED("4vladne5kzv3xueclq0sxcjho"); // 	    {			/* continuation of a chain from abv. */
UNSUPPORTED("bwewd7qprc37qhwxz3ko1y4uq"); // 	      if (tr[t].usave > 0) /* three upper neighbours */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("esaqksoargwy10mkyzhmv2p9v"); // 		  if (tr[t].uside == 1)
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("82gucb8vt172fhq2ug560dgje"); // 		      tr[tn].u0 = tr[t].u1;
UNSUPPORTED("lgzjorwqs1rmmyj7q8jzt16e"); // 		      tr[t].u1 = -1;
UNSUPPORTED("6wku8ik0lpiqemv9dmew6dodf"); // 		      tr[tn].u1 = tr[t].usave;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("30jz3yn7hygbk25oykenqo2tp"); // 		      tr[tr[tn].u0].d0 = tn;
UNSUPPORTED("ehcdy9xx8x9l0nz0oybez912h"); // 		      tr[tr[tn].u1].d0 = tn;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6cf2pwwuzbumwpxili1otryjb"); // 		  else		/* intersects in the right */
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("1zxekgfse665xkba3vu2wy0c0"); // 		      tr[tn].u1 = -1;
UNSUPPORTED("82gucb8vt172fhq2ug560dgje"); // 		      tr[tn].u0 = tr[t].u1;
UNSUPPORTED("dedip96pnjatmrxwr3szb2ltb"); // 		      tr[t].u1 = tr[t].u0;
UNSUPPORTED("be5obz58np1ogzt1m8welxd6c"); // 		      tr[t].u0 = tr[t].usave;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("2d4udv8v764lhooiia78atj4b"); // 		      tr[tr[t].u1].d0 = t;
UNSUPPORTED("72euqk1bum10xtl1bm12ug86r"); // 		      tr[tr[tn].u0].d0 = tn;		      
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("d52osx7vrtw97wm9ut9vfa5wj"); // 		  tr[t].usave = tr[tn].usave = 0;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("91694gf7sahoxvmiw82owvef2"); // 	      else		/* No usave.... simple case */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("e2zdzb3rlpq6pieuayb44oijq"); // 		  tr[tn].u0 = tr[t].u1;
UNSUPPORTED("6291oktyj53xp9yslzj405wgb"); // 		  tr[t].u1 = tr[tn].u1 = -1;
UNSUPPORTED("ay4xwmue8kz8uzp2wgge6752p"); // 		  tr[tr[tn].u0].d0 = tn;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("embybz3p65nxf0rx8b52bb0cj"); // 	  else 
UNSUPPORTED("2auoskyvfrf0wu8ekti5fs1n4"); // 	    {			/* fresh seg. or upward cusp */
UNSUPPORTED("2hrv5rm87wox9rp79f6wpn619"); // 	      int tmp_u = tr[t].u0;
UNSUPPORTED("8zaj2aem34dupb1plqsm8uwk5"); // 	      int td0, td1;
UNSUPPORTED("6f3f5nfgz2txzlmdcntsu2yyx"); // 	      if (((td0 = tr[tmp_u].d0) > 0) && 
UNSUPPORTED("7ed0ylnawdzj4xmtq0ih0wt7o"); // 		  ((td1 = tr[tmp_u].d1) > 0))
UNSUPPORTED("8dh5ldf8m83e9ngo1yqfbo7m1"); // 		{		/* upward cusp */
UNSUPPORTED("62v7ooft04km5zqvbpql7o5ps"); // 		  if ((tr[td0].rseg > 0) &&
UNSUPPORTED("4hx3tu7xyeswnab7x0q6yoefl"); // 		      !is_left_of(tr[td0].rseg, seg, &s.v1))
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("201vn0jou9fcf0n2tjih472mz"); // 		      tr[t].u0 = tr[t].u1 = tr[tn].u1 = -1;
UNSUPPORTED("rg3tngg7tj2qgf4erhpbcau8"); // 		      tr[tr[tn].u0].d1 = tn;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("e77o5ieo3c02dom0f04cj7inv"); // 		  else 
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("e9ql35kajykty3zsmtldh0wzb"); // 		      tr[tn].u0 = tr[tn].u1 = tr[t].u1 = -1;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dayzocp22vmevtvo1awep6h63"); // 	      else		/* fresh segment */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("1i8cfmzh5xxisiuekr63cd7hs"); // 		  tr[tr[t].u0].d0 = t;
UNSUPPORTED("694mejpoguhaimtc4oglzp817"); // 		  tr[tr[t].u0].d1 = tn;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("e0kb482en9la96mrxyisdyquw"); // 	  if ((fabs(tr[t].lo.y - tr[tlast].lo.y) <= 1.0e-7) && 
UNSUPPORTED("8d08n1tz1fajvu4tkkmyvqto3"); // 	      (fabs(tr[t].lo.x - tr[tlast].lo.x) <= 1.0e-7) && tribot)
UNSUPPORTED("2pqges4a601rkdgoh9ay4fwkc"); // 	    {		/* bottom forms a triangle */
UNSUPPORTED("3xb6452d73nnzbrvxea9r7avd"); // 	      /* int tmpseg; */
UNSUPPORTED("74q36498zx96m389wznj126jh"); // 	      if (is_swapped)	
UNSUPPORTED("c6l0dqua1wi4sm2nno83tqjpu"); // 		tmptriseg = seg[segnum].prev;
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("cr8el00rgafuidxf7dkqmprk9"); // 		tmptriseg = seg[segnum].next;
UNSUPPORTED("c1pnfnnei4xmduu3ik7cdc40k"); // 	      /* if ((tmpseg > 0) && is_left_of(tmpseg, seg, &s.v0)) */
UNSUPPORTED("7m5vjpo3bvkkv7dehsu8lk1i0"); // 	      if ((tmptriseg > 0) && is_left_of(tmptriseg, seg, &s.v0))
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("7its0v1anhryibc6kbl7e3rih"); // 		  /* L-R downward cusp */
UNSUPPORTED("8vwe6jjx4f0cxtyk1cjsred13"); // 		  tr[tr[t].d1].u0 = t;
UNSUPPORTED("ejo4cyfym8ojporxegfwpinht"); // 		  tr[tn].d0 = tr[tn].d1 = -1;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("2j9f9vrzjshiti7k2l10sx84u"); // 		  /* R-L downward cusp */
UNSUPPORTED("e1gz5epvwn4p0q5i8fveabsz8"); // 		  tr[tr[tn].d1].u1 = tn;
UNSUPPORTED("3c4euuu782207qdjnwrjswbyy"); // 		  tr[t].d0 = tr[t].d1 = -1;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("1qf8h2an6bk6wlk3fyq6i22ad"); // 	    }		
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("dp2662k6f23gqdw8zett9yvym"); // 	      if ((tr[tr[t].d1].u0 > 0) && (tr[tr[t].d1].u1 > 0))
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("77i22g6l292mg0okyxnajy9gb"); // 		  if (tr[tr[t].d1].u0 == t) /* passes thru LHS */
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("42sveu7omg4ras47bkv0d1q9l"); // 		      tr[tr[t].d1].usave = tr[tr[t].d1].u1;
UNSUPPORTED("3u8ltfhvqrsqd7ghc6mp90sk9"); // 		      tr[tr[t].d1].uside = 1;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("12hbppj0gv84xkilzofj6ohze"); // 		  else
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("4pcz02ip4pmu4wcvx249zt3hg"); // 		      tr[tr[t].d1].usave = tr[tr[t].d1].u0;
UNSUPPORTED("16fb5zagcitmzonkbzu89irjk"); // 		      tr[tr[t].d1].uside = 2;
UNSUPPORTED("ctzn5o8xqkywd73gcu6ofjsb7"); // 		    }		    
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6s5143onio335hfkk9om9l2a9"); // 	      tr[tr[t].d1].u0 = t;
UNSUPPORTED("6y4fvgf18azjngp3ph603viqk"); // 	      tr[tr[t].d1].u1 = tn;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5ltskdvhhim7914v7bcvcdu1y"); // 	  t = tr[t].d1;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("d8ibg8ps7psb492dez8t19wv3"); //       /* two trapezoids below. Find out which one is intersected by */
UNSUPPORTED("cpnw42brz1nujz70qenh15r33"); //       /* this segment and proceed down that one */
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("98gvqspn5y1bfyr14rwoaqk67"); // 	{
UNSUPPORTED("aeo0l7hd0tz385ecxm03tsqb1"); // 	  /* int tmpseg = tr[tr[t].d0].rseg; */
UNSUPPORTED("21dtwfwxm34d3el5rfez2ddio"); // 	  double y0, yt;
UNSUPPORTED("ds4ikd3wmue9s0ynmtxz1aw74"); // 	  pointf tmppt;
UNSUPPORTED("ah7kj1y9wo89l5ql0wxxjuqlt"); // 	  int tnext, i_d0, i_d1;
UNSUPPORTED("9l5ekv5nqwnh36g4gcr1x82aj"); // 	  i_d0 = i_d1 = (0);
UNSUPPORTED("4z75kl9vosft7cj42ce1arxdo"); // 	  if ((fabs(tr[t].lo.y - s.v0.y) <= 1.0e-7))
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("c8cramjmaogq40iauxa5mwm71"); // 	      if (tr[t].lo.x > s.v0.x)
UNSUPPORTED("18rqvljf5nebs454zlt2irs1i"); // 		i_d0 = (!(0));
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("v34oktxyq713ipav88bmcfn6"); // 		i_d1 = (!(0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("cm85lvkbze3joxoa0qgvj5d42"); // 	  else
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("db0whoy6j8f6uwqf5v4ilkh4v"); // 	      tmppt.y = y0 = tr[t].lo.y;
UNSUPPORTED("e5hekltsjclzspvdy92oh7kbu"); // 	      yt = (y0 - s.v0.y)/(s.v1.y - s.v0.y);
UNSUPPORTED("f30z7uxjf30rbkx5rdbcrwsm4"); // 	      tmppt.x = s.v0.x + yt * (s.v1.x - s.v0.x);
UNSUPPORTED("b4znl042gb9xqbe7w3hs5cz0y"); // 	      if (_less_than(&tmppt, &tr[t].lo))
UNSUPPORTED("18rqvljf5nebs454zlt2irs1i"); // 		i_d0 = (!(0));
UNSUPPORTED("3aqvmk5i0k4ue9zqfwhex7t14"); // 	      else
UNSUPPORTED("v34oktxyq713ipav88bmcfn6"); // 		i_d1 = (!(0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9hbp7chg2crbrb1u4rnba7190"); // 	  /* check continuity from the top so that the lower-neighbour */
UNSUPPORTED("81ywl919vnapbffcabfhs0ntz"); // 	  /* values are properly filled for the upper trapezoid */
UNSUPPORTED("6a1dcxf2rsr9zq5pxrpuecig1"); // 	  if ((tr[t].u0 > 0) && (tr[t].u1 > 0))
UNSUPPORTED("4vladne5kzv3xueclq0sxcjho"); // 	    {			/* continuation of a chain from abv. */
UNSUPPORTED("bwewd7qprc37qhwxz3ko1y4uq"); // 	      if (tr[t].usave > 0) /* three upper neighbours */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("esaqksoargwy10mkyzhmv2p9v"); // 		  if (tr[t].uside == 1)
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("82gucb8vt172fhq2ug560dgje"); // 		      tr[tn].u0 = tr[t].u1;
UNSUPPORTED("lgzjorwqs1rmmyj7q8jzt16e"); // 		      tr[t].u1 = -1;
UNSUPPORTED("6wku8ik0lpiqemv9dmew6dodf"); // 		      tr[tn].u1 = tr[t].usave;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("30jz3yn7hygbk25oykenqo2tp"); // 		      tr[tr[tn].u0].d0 = tn;
UNSUPPORTED("ehcdy9xx8x9l0nz0oybez912h"); // 		      tr[tr[tn].u1].d0 = tn;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6cf2pwwuzbumwpxili1otryjb"); // 		  else		/* intersects in the right */
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("1zxekgfse665xkba3vu2wy0c0"); // 		      tr[tn].u1 = -1;
UNSUPPORTED("82gucb8vt172fhq2ug560dgje"); // 		      tr[tn].u0 = tr[t].u1;
UNSUPPORTED("dedip96pnjatmrxwr3szb2ltb"); // 		      tr[t].u1 = tr[t].u0;
UNSUPPORTED("be5obz58np1ogzt1m8welxd6c"); // 		      tr[t].u0 = tr[t].usave;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("2d4udv8v764lhooiia78atj4b"); // 		      tr[tr[t].u1].d0 = t;
UNSUPPORTED("72euqk1bum10xtl1bm12ug86r"); // 		      tr[tr[tn].u0].d0 = tn;		      
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("d52osx7vrtw97wm9ut9vfa5wj"); // 		  tr[t].usave = tr[tn].usave = 0;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("91694gf7sahoxvmiw82owvef2"); // 	      else		/* No usave.... simple case */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("e2zdzb3rlpq6pieuayb44oijq"); // 		  tr[tn].u0 = tr[t].u1;
UNSUPPORTED("ddpnvdcma38disf8tyw74xp3g"); // 		  tr[tn].u1 = -1;
UNSUPPORTED("dqxje9ppj2ffgivrfpyid7ffd"); // 		  tr[t].u1 = -1;
UNSUPPORTED("ay4xwmue8kz8uzp2wgge6752p"); // 		  tr[tr[tn].u0].d0 = tn;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("embybz3p65nxf0rx8b52bb0cj"); // 	  else 
UNSUPPORTED("2auoskyvfrf0wu8ekti5fs1n4"); // 	    {			/* fresh seg. or upward cusp */
UNSUPPORTED("2hrv5rm87wox9rp79f6wpn619"); // 	      int tmp_u = tr[t].u0;
UNSUPPORTED("8zaj2aem34dupb1plqsm8uwk5"); // 	      int td0, td1;
UNSUPPORTED("6f3f5nfgz2txzlmdcntsu2yyx"); // 	      if (((td0 = tr[tmp_u].d0) > 0) && 
UNSUPPORTED("7ed0ylnawdzj4xmtq0ih0wt7o"); // 		  ((td1 = tr[tmp_u].d1) > 0))
UNSUPPORTED("8dh5ldf8m83e9ngo1yqfbo7m1"); // 		{		/* upward cusp */
UNSUPPORTED("62v7ooft04km5zqvbpql7o5ps"); // 		  if ((tr[td0].rseg > 0) &&
UNSUPPORTED("4hx3tu7xyeswnab7x0q6yoefl"); // 		      !is_left_of(tr[td0].rseg, seg, &s.v1))
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("201vn0jou9fcf0n2tjih472mz"); // 		      tr[t].u0 = tr[t].u1 = tr[tn].u1 = -1;
UNSUPPORTED("rg3tngg7tj2qgf4erhpbcau8"); // 		      tr[tr[tn].u0].d1 = tn;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("e77o5ieo3c02dom0f04cj7inv"); // 		  else 
UNSUPPORTED("4tol1smeoe31nndhs3aq3vdlq"); // 		    {
UNSUPPORTED("e9ql35kajykty3zsmtldh0wzb"); // 		      tr[tn].u0 = tr[tn].u1 = tr[t].u1 = -1;
UNSUPPORTED("1h87mqh0ff7hg3ogzwu8daiai"); // 		      tr[tr[t].u0].d0 = t;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dayzocp22vmevtvo1awep6h63"); // 	      else		/* fresh segment */
UNSUPPORTED("3lflizih274xjqgv1g0wjdgeq"); // 		{
UNSUPPORTED("1i8cfmzh5xxisiuekr63cd7hs"); // 		  tr[tr[t].u0].d0 = t;
UNSUPPORTED("694mejpoguhaimtc4oglzp817"); // 		  tr[tr[t].u0].d1 = tn;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("e0kb482en9la96mrxyisdyquw"); // 	  if ((fabs(tr[t].lo.y - tr[tlast].lo.y) <= 1.0e-7) && 
UNSUPPORTED("8d08n1tz1fajvu4tkkmyvqto3"); // 	      (fabs(tr[t].lo.x - tr[tlast].lo.x) <= 1.0e-7) && tribot)
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("4i449ebwuk75bwvj3njk3c62q"); // 	      /* this case arises only at the lowest trapezoid.. i.e.
UNSUPPORTED("chl5iuvrjyna1uuc1kjo3qe67"); // 		 tlast, if the lower endpoint of the segment is
UNSUPPORTED("9x1ytb8hzdseof3w011rw1vo6"); // 		 already inserted in the structure */
UNSUPPORTED("3vlkssstu9yp43651wjntawrq"); // 	      tr[tr[t].d0].u0 = t;
UNSUPPORTED("m24vo48yuozp20pg0jewoocj"); // 	      tr[tr[t].d0].u1 = -1;
UNSUPPORTED("bqsxgabpkfrz4qsvsfyt54x4d"); // 	      tr[tr[t].d1].u0 = tn;
UNSUPPORTED("5pcabowqh0osy2bara83fy53g"); // 	      tr[tr[t].d1].u1 = -1;
UNSUPPORTED("9akd96bko966x16jg4ghdofjt"); // 	      tr[tn].d0 = tr[t].d1;
UNSUPPORTED("ez9y4mtzds5q3em5b2h4m0dj"); // 	      tr[t].d1 = tr[tn].d1 = -1;
UNSUPPORTED("7zlu4fl3iisiyyfhc8efkikq2"); // 	      tnext = tr[t].d1;	      
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("7ekeyr3kxnx2g4xkar2e97ujg"); // 	  else if (i_d0)
UNSUPPORTED("df98v04w3tfu8ysuydsf7y85a"); // 				/* intersecting d0 */
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("3vlkssstu9yp43651wjntawrq"); // 	      tr[tr[t].d0].u0 = t;
UNSUPPORTED("dy31y8zhzxmyas9wde5g3vvlh"); // 	      tr[tr[t].d0].u1 = tn;
UNSUPPORTED("bqsxgabpkfrz4qsvsfyt54x4d"); // 	      tr[tr[t].d1].u0 = tn;
UNSUPPORTED("5pcabowqh0osy2bara83fy53g"); // 	      tr[tr[t].d1].u1 = -1;
UNSUPPORTED("f4j0by9ay0t0diwmyfz091taa"); // 	      /* new code to determine the bottom neighbours of the */
UNSUPPORTED("62pe8dmdbu3mmvgi9x43iqe6b"); // 	      /* newly partitioned trapezoid */
UNSUPPORTED("6qcsr9br9c9peep8mzd4eldpu"); // 	      tr[t].d1 = -1;
UNSUPPORTED("7n0btjlxfoym1gc1tkc5zrbdg"); // 	      tnext = tr[t].d0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("et6v36d4wyvf6eg7htev3s7m9"); // 	  else			/* intersecting d1 */
UNSUPPORTED("6dbei3uox5ql5a1vaaguh0xzp"); // 	    {
UNSUPPORTED("3vlkssstu9yp43651wjntawrq"); // 	      tr[tr[t].d0].u0 = t;
UNSUPPORTED("m24vo48yuozp20pg0jewoocj"); // 	      tr[tr[t].d0].u1 = -1;
UNSUPPORTED("6s5143onio335hfkk9om9l2a9"); // 	      tr[tr[t].d1].u0 = t;
UNSUPPORTED("6y4fvgf18azjngp3ph603viqk"); // 	      tr[tr[t].d1].u1 = tn;
UNSUPPORTED("f4j0by9ay0t0diwmyfz091taa"); // 	      /* new code to determine the bottom neighbours of the */
UNSUPPORTED("62pe8dmdbu3mmvgi9x43iqe6b"); // 	      /* newly partitioned trapezoid */
UNSUPPORTED("9akd96bko966x16jg4ghdofjt"); // 	      tr[tn].d0 = tr[t].d1;
UNSUPPORTED("41y3k6fjtt5tbdx3goo3bnr1i"); // 	      tr[tn].d1 = -1;
UNSUPPORTED("da00twqjxr6q0clcep0wcdlql"); // 	      tnext = tr[t].d1;
UNSUPPORTED("1dgkp19ct6hczd1425v8q1dct"); // 	    }	    
UNSUPPORTED("6fwmq6tx1y4cibphzm0f4wakm"); // 	  t = tnext;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e44ec6lx2mkb3zz7hmhjx90rq"); //       tr[t_sav].rseg = tr[tn_sav].lseg  = segnum;
UNSUPPORTED("65mlofu9i9895kq8xkb6x103e"); //     } /* end-while */
UNSUPPORTED("8rkpqhwjgyuqdygp3b2o580ff"); //   /* Now combine those trapezoids which share common segments. We can */
UNSUPPORTED("cklqh43b0korps6nmg2eefd70"); //   /* use the pointers to the parent to connect these together. This */
UNSUPPORTED("nzam565p5xaq9ashe9ekcjks"); //   /* works only because all these new trapezoids have been formed */
UNSUPPORTED("81u7z601i80he54qa00v17ami"); //   /* due to splitting by the segment, and hence have only one parent */
UNSUPPORTED("cn4qmoxsrclu3q3mbcc9ff7fw"); //   tfirstl = tfirst; 
UNSUPPORTED("duhakllu2zu2zvohf6zxwdnqc"); //   tlastl = tlast;
UNSUPPORTED("36mff9r0jqhoc1chz9zqbk3x1"); //   merge_trapezoids(segnum, tfirstl, tlastl, 1, tr, qs);
UNSUPPORTED("aldx6ji29f73y5modmaxg9zs9"); //   merge_trapezoids(segnum, tfirstr, tlastr, 2, tr, qs);
UNSUPPORTED("5e3z4o423d4ob6d3adzxdeipj"); //   seg[segnum].is_inserted = (!(0));
UNSUPPORTED("bid671dovx1rdiquw5vm3fttj"); //   return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7afsay7ejf6pnu6atsg7glt53
// static void find_new_roots(int segnum, segment_t* seg, trap_t* tr, qnode_t* qs) 
public static Object find_new_roots(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("hytfu5b4w9vy61qxrhvzemj3"); // find_new_roots(int segnum, segment_t* seg, trap_t* tr, qnode_t* qs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("55wy1clqaecn10s0jetfvt04s"); //   segment_t *s = &seg[segnum];
UNSUPPORTED("13js9c1rj1roq5meh748h5zjy"); //   if (s->is_inserted) return;
UNSUPPORTED("3v99ch3hok3ae2fl5leqyzkd7"); //   s->root0 = locate_endpoint(&s->v0, &s->v1, s->root0, seg, qs);
UNSUPPORTED("7xdyqtj001z8eqv93gzye5r0k"); //   s->root0 = tr[s->root0].sink;
UNSUPPORTED("azljsp4ofo8uf3sqfwo50lv7y"); //   s->root1 = locate_endpoint(&s->v1, &s->v0, s->root1, seg, qs);
UNSUPPORTED("4cgieg4q0okhq82lczulvj2ok"); //   s->root1 = tr[s->root1].sink;  
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ftshkxhp22o0xgh3kuc3sile
// static int math_logstar_n(int n) 
public static Object math_logstar_n(Object... arg) {
UNSUPPORTED("3a94rmq48cptlea8l9svm2vdf"); // static int math_logstar_n(int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9cyat6ft4tmgpumn70l9fwydy"); //   register int i;
UNSUPPORTED("6om7utuy6ofppg87j05edy4cy"); //   double v;
UNSUPPORTED("2r6ldqvvcn5lqu4qdj5pias2v"); //   for (i = 0, v = (double) n; v >= 1; i++)
UNSUPPORTED("1d99ficfyx27epymfs7pc5c4"); //       v = (log(v)/log(2));
UNSUPPORTED("17678qqlxjn7yfcjfzl0pk4ao"); //   return (i - 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 20alp7vy21dvsx8ums8pezh3o
// static int math_N(int n, int h) 
public static Object math_N(Object... arg) {
UNSUPPORTED("dd4ge3rbi53dw0wh8a3si1jh7"); // static int math_N(int n, int h)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9cyat6ft4tmgpumn70l9fwydy"); //   register int i;
UNSUPPORTED("6om7utuy6ofppg87j05edy4cy"); //   double v;
UNSUPPORTED("ujffxxrs7gz2vawl37d9jslq"); //   for (i = 0, v = (int) n; i < h; i++)
UNSUPPORTED("1d99ficfyx27epymfs7pc5c4"); //       v = (log(v)/log(2));
UNSUPPORTED("enxjvr50hyamoic6g6lwxie70"); //   return (int) ceil((double) 1.0*n/v);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9jpwujop3smqitn3gmtcrcixm
// int construct_trapezoids(int nseg, segment_t* seg, int* permute, int ntraps,   trap_t* tr) 
public static Object construct_trapezoids(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("cywexw0hiyl2gthuawhdixmym"); // construct_trapezoids(int nseg, segment_t* seg, int* permute, int ntraps,
UNSUPPORTED("benaopemb2t6pehss4gcdybzb"); //   trap_t* tr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("c4yv5wsz9md0n246eno8qs5et"); //     int root, h;
UNSUPPORTED("1c0phmc3iq7tb3sotrpcjoup9"); //     int segi = 1;
UNSUPPORTED("7jamhwzh4w6lx0bpq57thdtpt"); //     qnode_t* qs;
UNSUPPORTED("1yqtu5d20wep1ymxy1y0nv1g0"); //     QSIZE = 2*ntraps;
UNSUPPORTED("3mcur34pgw6444857byhvmgs5"); //     TRSIZE = ntraps;
UNSUPPORTED("alj695bdzyptc3wnccsnyuano"); //     qs = (qnode_t*)zmalloc((2*ntraps)*sizeof(qnode_t));
UNSUPPORTED("eroaf3tk5y6euf62it6cxn758"); //     q_idx = tr_idx = 1;
UNSUPPORTED("873fgjnxtke0s60r4k3bn0se8"); //     memset((void *)tr, 0, ntraps*sizeof(trap_t));
UNSUPPORTED("dznan6lx9lxucsy4yr6edbkas"); //   /* Add the first segment and get the query structure and trapezoid */
UNSUPPORTED("acrntweri3uun3ahwdudmq1bc"); //   /* list initialised */
UNSUPPORTED("57gkh96bcrh9z87hoeeqq9nw4"); //     root = init_query_structure(permute[segi++], seg, tr, qs);
UNSUPPORTED("56oij2ngkqcxgpihju021plmr"); //     for (i = 1; i <= nseg; i++)
UNSUPPORTED("137jp8s4g6ip477ywoxna6hr4"); // 	seg[i].root0 = seg[i].root1 = root;
UNSUPPORTED("56osqlqd8y3slj5zr13d4grc7"); //     for (h = 1; h <= math_logstar_n(nseg); h++) {
UNSUPPORTED("tmjwiqe8s4q112nev98zolt7"); // 	for (i = math_N(nseg, h -1) + 1; i <= math_N(nseg, h); i++)
UNSUPPORTED("bk608pqmg2xw7fpucgxkgwhmo"); // 	    add_segment(permute[segi++], seg, tr, qs);
UNSUPPORTED("9lu6zl26144vbdgsqc1mc6wwz"); //       /* Find a new root for each of the segment endpoints */
UNSUPPORTED("d1y51tnl70njrosh5iimn6xmx"); // 	for (i = 1; i <= nseg; i++)
UNSUPPORTED("cfk5n685oegsos34urlk5307z"); // 	    find_new_roots(i, seg, tr, qs);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4sfugdwxcf71d498tnurlvs5"); //     for (i = math_N(nseg, math_logstar_n(nseg)) + 1; i <= nseg; i++)
UNSUPPORTED("eqduqz9al6zdpuhe8fsvnn2en"); // 	add_segment(permute[segi++], seg, tr, qs);
UNSUPPORTED("5olrlwcgauazrftmytyls44zs"); //     free (qs);
UNSUPPORTED("awblwd52926w1zhpsot3aqld9"); //     return tr_idx;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
