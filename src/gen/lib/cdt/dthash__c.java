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
package gen.lib.cdt;
import static smetana.core.Macro.UNSUPPORTED;

public class dthash__c {
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




//3 7akctm4caciv9haxdk4zlrwu2
// static void dthtab(Dt_t* dt)     
public static Object dthtab(Object... arg) {
UNSUPPORTED("2d2m2vzj4zvzgd4r2zn2s1kqn"); // static void dthtab(Dt_t* dt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dmm9rj1e4gzk6xftk0ezcsact"); // 	register Dtlink_t	*t, *r, *p, **s, **hs, **is, **olds;
UNSUPPORTED("54n8pasotrjhynrhaueagwuhz"); // 	int		n, k;
UNSUPPORTED("axpkyw9uz8rvnmklligok2e2f"); // 	if(dt->data->minp > 0 && dt->data->ntab > 0) /* fixed table size */
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("2eqdiqvcsxis68sccmci5sslk"); // 	dt->data->minp = 0;
UNSUPPORTED("74tln6sb3yrn1nfwgllf1vfj1"); // 	n = dt->data->ntab;
UNSUPPORTED("6bgqjnz4t47feeoy1br9ew29p"); // 	if(dt->disc && dt->disc->eventf &&
UNSUPPORTED("cr75vmjji1xsei24agv44wqxh"); // 	   (*dt->disc->eventf)(dt, 7, &n, dt->disc) > 0 )
UNSUPPORTED("4jwe6txgxjvpban5zstx09mx8"); // 	{	if(n < 0) /* fix table size */
UNSUPPORTED("7chn136gsjv69n13yovn16f3b"); // 		{	dt->data->minp = 1;
UNSUPPORTED("4dlk72qfzjf2r39ukyq7yzfyp"); // 			if(dt->data->ntab > 0 )
UNSUPPORTED("49xixhkg85008801z87qmxkgl"); // 				return;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7yuj23uvw15fcmez2o1pzkjrl"); // 		else /* set a particular size */
UNSUPPORTED("14rrn3w4adtaiolijjyulmjsn"); // 		{	for(k = 2; k < n; k *= 2)
UNSUPPORTED("8c0hnggfgcchqp0vr4bwobm4t"); // 				;
UNSUPPORTED("dvmpnz9ncerzmb5v8gidr7eis"); // 			n = k;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("932vaq51fbf5l8i420kesqfg0"); // 	else	n = 0;
UNSUPPORTED("bh1ire4hwludcleecmxv0smag"); // 	/* compute new table size */
UNSUPPORTED("abhbgzrg4q5z2vkoed72su98y"); // 	if(n <= 0)
UNSUPPORTED("dab3gyaa9l6c1zsdv75gwesrh"); // 	{	if((n = dt->data->ntab) == 0)
UNSUPPORTED("4021uenum9y6sgeuboex8d0on"); // 			n = (256);
UNSUPPORTED("c1uorlajtkwlvx1a2h6ajr0bp"); // 		while(dt->data->size > ((n) << 1))
UNSUPPORTED("99wvpps4p28ykwrtq32whiwrd"); // 			n = ((n) << 1);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("b2ppghcwec8d3i5y20q5cibn2"); // 	if(n == dt->data->ntab)
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("ui70t5x7lzm4cg4q5hqswo74"); // 	/* allocate new table */
UNSUPPORTED("710t2p2pi32z07r1o6a8imf9j"); // 	olds = dt->data->ntab == 0 ? ((Dtlink_t**)0) : dt->data->hh._htab;
UNSUPPORTED("400z1yutimeajug4gd8t0opt6"); // 	if(!(s = (Dtlink_t**)(*dt->memoryf)(dt,olds,n*sizeof(Dtlink_t*),dt->disc)) )
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("a4y1edr0vk9beqab7n7td6s29"); // 	olds = s + dt->data->ntab;
UNSUPPORTED("2rycm855gyv5lww7scwom17j3"); // 	dt->data->hh._htab = s;
UNSUPPORTED("duhcyhub67fpfsay0dkflcxt6"); // 	dt->data->ntab = n;
UNSUPPORTED("7ksrj2u284dwkngf2km9jg4xj"); // 	/* rehash elements */
UNSUPPORTED("1hz692vbawik1ma02xl4o6i21"); // 	for(hs = s+n-1; hs >= olds; --hs)
UNSUPPORTED("s5sbwingw26r7aehxbc0ii4z"); // 		*hs = ((Dtlink_t*)0);
UNSUPPORTED("9ymwe2szhthzd4w5m4zv4jag6"); // 	for(hs = s; hs < olds; ++hs)
UNSUPPORTED("clyaoquug0nyscqmgfstnd3um"); // 	{	for(p = ((Dtlink_t*)0), t = *hs; t; t = r)
UNSUPPORTED("ccbd2da5hrf94txyeadl64rj4"); // 		{	r = t->right;
UNSUPPORTED("84b31s29uevgnrutqi5beo35a"); // 			if((is = s + ((t->hl._hash)&((n)-1))) == hs)
UNSUPPORTED("59zqwaqjkez5xrkcft9rm1ddg"); // 				p = t;
UNSUPPORTED("9cm22gi5ztcly2xp8i8bnnrq0"); // 			else	/* move to a new chain */
UNSUPPORTED("apvgwu04dgxcsjo6z3uw5k0mn"); // 			{	if(p)
UNSUPPORTED("boivlg1vuin099locovgrmxuy"); // 					p->right = r;
UNSUPPORTED("340ftcld884g4rid6m0z4sco1"); // 				else	*hs = r;
UNSUPPORTED("8tchzpmze0ll3y75ehijf6rye"); // 				t->right = *is; *is = t;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 78zfgu6jzim09d5xvj1qs9bk0
// static void* dthash(Dt_t* dt, register void* obj, int type)       
public static Object dthash(Object... arg) {
UNSUPPORTED("95hr4hrc6nbqqdqdce5tg8l5p"); // static void* dthash(Dt_t* dt, register void* obj, int type)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dfpfp02iblqj4mzvjx0fueyhf"); // 	register Dtlink_t	*t, *r, *p;
UNSUPPORTED("283qljx9jxfsqlak87k7jg5ny"); // 	register void	*k, *key;
UNSUPPORTED("59o4ibfersjospolmfoujpzrx"); // 	register unsigned int	hsh;
UNSUPPORTED("660kbsr1gj2c3whxtgelujyj1"); // 	register int		lk, sz, ky;
UNSUPPORTED("d7uh8kuvwiwc4dkgh3xeqa7o"); // 	register Dtcompar_f	cmpf;
UNSUPPORTED("boylfimuhvbo4nqzby3ew5tld"); // 	register Dtdisc_t*	disc;
UNSUPPORTED("9sfwn5sp00p82cc2ivzw480ta"); // 	register Dtlink_t	**s, **ends;
UNSUPPORTED("ckjxpmega2z5oju8xk72wsy0q"); // 	((dt->data->type&010000) ? dtrestore(dt,((Dtlink_t*)0)) : 0);
UNSUPPORTED("5ggzdi5wqxinikmv0anb4peay"); // 	/* initialize discipline data */
UNSUPPORTED("a9vwj28gsu58hp4ocpvno985d"); // 	disc = dt->disc; (ky = disc->key, sz = disc->size, lk = disc->link, cmpf = disc->comparf);
UNSUPPORTED("dpg99ryukgexcc5n31wdbvraz"); // 	dt->type &= ~0100000;
UNSUPPORTED("e4rxlgclvydwuznpw7rk0rksa"); // 	if(!obj)
UNSUPPORTED("8htaux93kinbjvm9ooz891eng"); // 	{	if(type&(0000010|0000020))
UNSUPPORTED("6hku2pc4tqgui5evf317yqvfm"); // 			goto end_walk;
UNSUPPORTED("1v2lqr5htt1euj04jfa98gjdv"); // 		if(dt->data->size <= 0 || !(type&(0000100|0000200|0000400)) )
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("24ilchev6xpf5y6z34hqa9glk"); // 		ends = (s = dt->data->hh._htab) + dt->data->ntab;
UNSUPPORTED("92nrnv2rh044z4gtldl61vus0"); // 		if(type&0000100)
UNSUPPORTED("dhfqwls50q72zxh2fxlvobk33"); // 		{	/* clean out all objects */
UNSUPPORTED("5vi45e0h8c9ymrwgqotzsu9t1"); // 			for(; s < ends; ++s)
UNSUPPORTED("6pj3i74r6sowp2uu910mr4yg"); // 			{	t = *s;
UNSUPPORTED("6155l1hofhfyspasyb3ahizbd"); // 				*s = ((Dtlink_t*)0);
UNSUPPORTED("9jtw72qjx20vdh59dfe1phhyr"); // 				if(!disc->freef && disc->link >= 0)
UNSUPPORTED("6cgu609p2i9fp9atj1fw3s5cy"); // 					continue;
UNSUPPORTED("4y5ojoi4p0ic91exb1rfs9s86"); // 				while(t)
UNSUPPORTED("amls96qnaqczr04p9eg8uyu6l"); // 				{	r = t->right;
UNSUPPORTED("9la822tbyggyiqojilsi237uo"); // 					if(disc->freef)
UNSUPPORTED("3idn1ydm0vaxkb4b0mv2tez2s"); // 						(*disc->freef)(dt,(lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ),disc);
UNSUPPORTED("7hdvwe89xp5vg9m4l8lpmgvap"); // 					if(disc->link < 0)
UNSUPPORTED("6km3uqqb8gzn7n83vngsax7up"); // 						(*dt->memoryf)(dt,(void*)t,0,disc);
UNSUPPORTED("3fq4aglnc1p3fjid7sxse643h"); // 					t = r;
UNSUPPORTED("cysnuxd51taci3hbg5lifz8ce"); // 				}
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("3joovo5f92ue2fnvked57dbbi"); // 			dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("196oh3ma81y2lao0qwcvuwvga"); // 			dt->data->size = 0;
UNSUPPORTED("8kl1r7cphdbs40t2grhkyhall"); // 			dt->data->loop = 0;
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("9sifrmemn97t14u5b9ib87dh3"); // 		else	/* computing the first/last object */
UNSUPPORTED("2oievk2z0s9n8fqc9ug1sbb6p"); // 		{	t = ((Dtlink_t*)0);
UNSUPPORTED("ems59hbo02yf35j3szsgvnu3p"); // 			while(s < ends && !t )
UNSUPPORTED("6smbnv0v11g3t6zcnn0gw1cb1"); // 				t = (type&0000400) ? *--ends : *s++;
UNSUPPORTED("7kk99glk5rm9xs40c8htgo3k"); // 			if(t && (type&0000400))
UNSUPPORTED("40b46zexoifo1zv4zz3pqip7m"); // 				for(; t->right; t = t->right)
UNSUPPORTED("6f76030mwgybzczalesd54a9q"); // 					;
UNSUPPORTED("194sa4x6u6hxxh2wes9ola1wt"); // 			dt->data->loop += 1;
UNSUPPORTED("cqadxs0pgykzd2tkrn18wef8"); // 			dt->data->here = t;
UNSUPPORTED("2txpn83o34o6qwamy2wb7757o"); // 			return t ? (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ) : ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7klmk5043gbxzy2n25b5d1dz6"); // 	/* allow apps to delete an object "actually" in the dictionary */
UNSUPPORTED("dt29n4tw5mutzyr4fm34jru8t"); // 	if(dt->meth->type == 0000002 && (type&(0000002|0010000)) )
UNSUPPORTED("7sbjzi3lqgrnk2dukj4saoqhr"); // 	{	if(!(*(((Dt_t*)(dt))->searchf))((dt),(void*)(obj),0000004) )
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("eq489drc6t4nw9jr6scwaf47r"); // 		s = dt->data->hh._htab + ((dt->data->here->hl._hash)&((dt->data->ntab)-1));
UNSUPPORTED("716ly4m21se00kv1jixe0abvv"); // 		r = ((Dtlink_t*)0);
UNSUPPORTED("ee5wwcf1ozo1pwrw5cqvta4ye"); // 		for(p = ((Dtlink_t*)0), t = *s; t; p = t, t = t->right)
UNSUPPORTED("8s0kw91pqx35e9cb8bnmjfpxd"); // 		{	if((lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ) == obj) /* delete this specific object */
UNSUPPORTED("cazdlksn7swkecr1zwj7u20e7"); // 				goto do_delete;
UNSUPPORTED("9zezhzqukdf30pddcygujrvm4"); // 			if(t == dt->data->here)
UNSUPPORTED("e8vu7jceswis3y29jjwhq9ng9"); // 				r = p;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("eqykdmanwamw75kiyy2znwojp"); // 		/* delete some matching object */
UNSUPPORTED("3jvj5fms81a41732hc1ygnn8l"); // 		p = r; t = dt->data->here;
UNSUPPORTED("8a5ic396b1zd3ypknh2ni33pl"); // 		goto do_delete;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("ctx8452ml6zkglwpzkrei4r8p"); // 	if(type&(0001000|0000004|0000001|0004000) )
UNSUPPORTED("dow6c5q06mynnixn0dg629l8s"); // 	{	key = (type&0001000) ? obj : (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
UNSUPPORTED("ki4jtqbenojcbm9xo3cw0acj"); // 		hsh = (disc->hashf ? (*disc->hashf)(dt,key,disc) : dtstrhash(0,key,sz) );
UNSUPPORTED("5qfstwhbyczhb45awkns4w6s2"); // 		goto do_search;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("17mzyz618jog9kxscctj849qn"); // 	else if(type&(0000040|0002000) )
UNSUPPORTED("a1tr8ku0sxlizc02em1xpti9m"); // 	{	r = (Dtlink_t*)obj;
UNSUPPORTED("5z333zdlm5fisvkd28wdk08lb"); // 		obj = (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) );
UNSUPPORTED("4mu735y1gatwqfcwvw32kyv4b"); // 		key = (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
UNSUPPORTED("9nsf0jvmpu3ekpif3tgtn9bfj"); // 		hsh = r->hl._hash;
UNSUPPORTED("5qfstwhbyczhb45awkns4w6s2"); // 		goto do_search;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6demt9oump237iqswn7lymci2"); // 	else /*if(type&(DT_DELETE|DT_DETACH|DT_NEXT|DT_PREV))*/
UNSUPPORTED("8ftvpvpaincodxzo3trgtpjw6"); // 	{	if((t = dt->data->here) && (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ) == obj)
UNSUPPORTED("9081zc00zn4etyvhw0gwp2pr1"); // 		{	hsh = t->hl._hash;
UNSUPPORTED("q62hm6hsn7w01e8d040wd36q"); // 			s = dt->data->hh._htab + ((hsh)&((dt->data->ntab)-1));
UNSUPPORTED("bq7yqk7ac7ar48epr0ntxlu34"); // 			p = ((Dtlink_t*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("9e1xg9ecdgp2asux0oagyok6z"); // 		{	key = (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
UNSUPPORTED("7iiuizt4e3k2puwkokgnwk9fl"); // 			hsh = (disc->hashf ? (*disc->hashf)(dt,key,disc) : dtstrhash(0,key,sz) );
UNSUPPORTED("2w8sqwcszvntntnfpandwatxf"); // 		do_search:
UNSUPPORTED("904ox6qqmbvtgeqaalo2krcxq"); // 			t = dt->data->ntab <= 0 ? ((Dtlink_t*)0) :
UNSUPPORTED("anysrymqqdewimlodxh4vnnf0"); // 			 	*(s = dt->data->hh._htab + ((hsh)&((dt->data->ntab)-1)));
UNSUPPORTED("1urtk6yxjydqmp6auwlxz1mry"); // 			for(p = ((Dtlink_t*)0); t; p = t, t = t->right)
UNSUPPORTED("a94c3oi4jicbke9656azbdmxw"); // 			{	if(hsh == t->hl._hash)
UNSUPPORTED("aba8e4nanbm4v7i73vtichn3a"); // 				{	k = (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ); k = (void*)(sz < 0 ? *((char**)((char*)(k)+ky)) : ((char*)(k)+ky));
UNSUPPORTED("6q8ct40por7odz1fi4kqawhu6"); // 					if((cmpf ? (*cmpf)(dt,key,k,disc) : (sz <= 0 ? strcmp(key,k) : memcmp(key,k,sz)) ) == 0)
UNSUPPORTED("3s0v3cbomg51jgxsi6nrpjbab"); // 						break;
UNSUPPORTED("cysnuxd51taci3hbg5lifz8ce"); // 				}
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("12o3l2uuqwkn6zu5n9ag5b6tv"); // 	if(t) /* found matching object */
UNSUPPORTED("b2cn93r7r83xjuylu9s3s7srd"); // 		dt->type |= 0100000;
UNSUPPORTED("a99qjy1ylbnckiqdhzekz7iaj"); // 	if(type&(0001000|0000004|0002000))
UNSUPPORTED("afvq5mw03wvx0n3mvvbrqldkw"); // 	{	if(!t)
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("4lorj98ma2upixa5rr5xwzij7"); // 		if(p && (dt->data->type&0000001) && dt->data->loop <= 0)
UNSUPPORTED("e12cfa3i62c3w5e93s2snz2io"); // 		{	/* move-to-front heuristic */
UNSUPPORTED("3rwbnud1fhq4g3dgtggb8ivmp"); // 			p->right = t->right;
UNSUPPORTED("7w9vk40tp93lbthf68mymuvl7"); // 			t->right = *s;
UNSUPPORTED("47dhsbny95udoaz2m518jipnd"); // 			*s = t;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3yx8gco1u2rbh7pp98bmhshcv"); // 		dt->data->here = t;
UNSUPPORTED("38zm3lu1kmess9k4fq9vw7307"); // 		return (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) );
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5s9uhnjsikxyxacq0a5avxqlu"); // 	else if(type&(0000001|0004000))
UNSUPPORTED("m0dd3fl2j1h8l4v5qcbx8pe3"); // 	{	if(t && (dt->data->type&0000001) )
UNSUPPORTED("2gqu6oymjvcnv26ghvpj8uw8q"); // 		{	dt->data->here = t;
UNSUPPORTED("7y3981d1t51r85enkkeisu5o"); // 			return (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) );
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("by4sdf3z0t5wzasnh0mn7qynq"); // 		if(disc->makef && (type&0000001) &&
UNSUPPORTED("819jcvpr9y2bgzfligpibwpw3"); // 		   !(obj = (*disc->makef)(dt,obj,disc)) )
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("293ajmk1i2zgdsq0l7383qwh3"); // 		if(lk >= 0)
UNSUPPORTED("3zy0ha2od6p4le2e9q0z8fp5t"); // 			r = ((Dtlink_t*)((char*)(obj) + lk) );
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("98qmqsabqfk3u0p0lk7ea0p5t"); // 		{	r = (Dtlink_t*)(*dt->memoryf)
UNSUPPORTED("2t8zht6j7v4lkeafea2xf5hyg"); // 				(dt,((void*)0),sizeof(Dthold_t),disc);
UNSUPPORTED("cd514pac1r4g4n22vahs1hysz"); // 			if(r)
UNSUPPORTED("9t49z1jdnrgb6bhq6nkkue1ic"); // 				((Dthold_t*)r)->obj = obj;
UNSUPPORTED("cqgi8f4d37bqva8z6bx5rvn7w"); // 			else
UNSUPPORTED("1v509jpumhakfypnj2ti3gu5c"); // 			{	if(disc->makef && disc->freef && (type&0000001))
UNSUPPORTED("9mpf4sqfgdx785iln3tc53xtk"); // 					(*disc->freef)(dt,obj,disc);
UNSUPPORTED("aa9atfxr38jo3q71l1xw0bq6k"); // 				return ((void*)0);
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("s0qtzqe9ppt5wefo9x8w939l"); // 		r->hl._hash = hsh;
UNSUPPORTED("3bloaq1kau0kxrhfjsbw3ww7t"); // 		/* insert object */
UNSUPPORTED("94p1b6yx4z1w4h646vfezwz82"); // 	do_insert:
UNSUPPORTED("nkb78kwk99oj3n3899azwlch"); // 		if((dt->data->size += 1) > ((dt->data->ntab) << 1) && dt->data->loop <= 0 )
UNSUPPORTED("9airb3ddrtasz9ic8vmjmmb3d"); // 			dthtab(dt);
UNSUPPORTED("atoexyb2y1uyfcvz9fsj28sgk"); // 		if(dt->data->ntab == 0)
UNSUPPORTED("dod3zpy4g6u8jgmxup50qqys9"); // 		{	dt->data->size -= 1;
UNSUPPORTED("bzc6n7f7vqj21bnxsl91xkwby"); // 			if(disc->freef && (type&0000001))
UNSUPPORTED("6tpn7nvqe9d7xas7x5oayr89e"); // 				(*disc->freef)(dt,obj,disc);
UNSUPPORTED("4jky808nj7nqoi0xqghkq71hu"); // 			if(disc->link < 0)
UNSUPPORTED("7c6rtwyp4m40lrj88mpgnpb21"); // 				(*disc->memoryf)(dt,(void*)r,0,disc);
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("4vk743730me6fvf2qfsyeksur"); // 		s = dt->data->hh._htab + ((hsh)&((dt->data->ntab)-1));
UNSUPPORTED("xlqibqplgtj358b7ra1xzef6"); // 		if(t)
UNSUPPORTED("7o4qhqtysg0296gjwygjc528s"); // 		{	r->right = t->right;
UNSUPPORTED("dkl4v64gxr1lec2474603kdmj"); // 			t->right = r;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("bzix5h9vod9ss1htxqdqpq1de"); // 		{	r->right = *s;
UNSUPPORTED("4ihgxtxxn05e6la642e1lt0de"); // 			*s = r;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7nqs3a1rtwdoaksqwklk6h9mr"); // 		dt->data->here = r;
UNSUPPORTED("62u47ehg5sw7ibh04rvqqdaws"); // 		return obj;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5k3wkmt1521n2igrbw361wdxw"); // 	else if(type&0000010)
UNSUPPORTED("9he18g2ou1k5oep0depme6a0l"); // 	{	if(t && !(p = t->right) )
UNSUPPORTED("dda3cpvj9gd3ckayd4vtx06jw"); // 		{	for(ends = dt->data->hh._htab+dt->data->ntab, s += 1; s < ends; ++s)
UNSUPPORTED("2f8x3x8q9d4l3zxkrdo3tgqug"); // 				if((p = *s) )
UNSUPPORTED("6ioth986rfbv208dp59shjy15"); // 					break;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6fbjgrqdc634wlamdggxuyx1y"); // 		goto done_adj;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eeak67ajn02aq4hfkpb1houzf"); // 	else if(type&0000020)
UNSUPPORTED("83v3fyui557cjpsucdnk29bcy"); // 	{	if(t && !p)
UNSUPPORTED("19279rks7tm3qlocyny8mcg8s"); // 		{	if((p = *s) != t)
UNSUPPORTED("9bho5o6g8z5l0obq611t4vwmt"); // 			{	while(p->right != t)
UNSUPPORTED("3vlcvd5o5mws86v5elocljl8c"); // 					p = p->right;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("cqgi8f4d37bqva8z6bx5rvn7w"); // 			else
UNSUPPORTED("2l2cjmve22qwyzxs3nemslsrm"); // 			{	p = ((Dtlink_t*)0);
UNSUPPORTED("7x0y3os4rhu1qmyy6cyigcbxn"); // 				for(s -= 1, ends = dt->data->hh._htab; s >= ends; --s)
UNSUPPORTED("2qzit70v2wvo0hfwhzxndjyet"); // 				{	if((p = *s) )
UNSUPPORTED("9cd19hzlnsuko7qjdica4avwk"); // 					{	while(p->right)
UNSUPPORTED("cz431qj39zg4imh6mbgcczxqs"); // 							p = p->right;
UNSUPPORTED("3s0v3cbomg51jgxsi6nrpjbab"); // 						break;
UNSUPPORTED("7qewsve9tto7ge11dkittrbpg"); // 					}
UNSUPPORTED("cysnuxd51taci3hbg5lifz8ce"); // 				}
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7h31eyrxx5hm6w9t1georb00j"); // 	done_adj:
UNSUPPORTED("706rjqsb7a7mnl0viqsd8nsuq"); // 		if(!(dt->data->here = p) )
UNSUPPORTED("e2m9axtrsvwabp4prolti6gfx"); // 		{ end_walk:
UNSUPPORTED("8mrt3rbp7uw89ql159auu6mmj"); // 			if((dt->data->loop -= 1) < 0)
UNSUPPORTED("c7fwpv4db3vo9qpsu295z0oxy"); // 				dt->data->loop = 0;
UNSUPPORTED("7zu532dfsu93magh8miv6p5k7"); // 			if(dt->data->size > ((dt->data->ntab) << 1) && dt->data->loop <= 0)
UNSUPPORTED("9ev3kbmw9lx3kf7go6zdr7joo"); // 				dthtab(dt);
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("3y0vfsjtwsjvd3iwojbfm5b7p"); // 		{	dt->data->type |= 020000;
UNSUPPORTED("1lts79855usdqaw6d8govltbd"); // 			return (lk < 0 ? ((Dthold_t*)(p))->obj : (void*)((char*)(p) - lk) );
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9z9fbich950cta699a2c3p0as"); // 	else if(type&0000040)
UNSUPPORTED("bupkrulrc2t9aibwzcu7vup7w"); // 	{	if(!t || (dt->data->type&0000002) )
UNSUPPORTED("1xk47swi87x8a68r3ieehnit6"); // 			goto do_insert;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("9lkixvm4kipsvy4n793m09ss3"); // 		{	if(disc->freef)
UNSUPPORTED("6tpn7nvqe9d7xas7x5oayr89e"); // 				(*disc->freef)(dt,obj,disc);
UNSUPPORTED("4jky808nj7nqoi0xqghkq71hu"); // 			if(disc->link < 0)
UNSUPPORTED("9jdzpj2hhktj69wgcvdr063s"); // 				(*dt->memoryf)(dt,(void*)r,0,disc);
UNSUPPORTED("2txpn83o34o6qwamy2wb7757o"); // 			return t ? (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ) : ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3mg6hhndwfct91qjn7bg6gadw"); // 	else /*if(type&(DT_DELETE|DT_DETACH))*/
UNSUPPORTED("aqnn8gxdjwjn8bj1jmn893w4c"); // 	{	/* take an element out of the dictionary */
UNSUPPORTED("3lpfu6n28scyhfqq2c0zpty59"); // 	do_delete:
UNSUPPORTED("22j08xpxd771hlal238zomv7l"); // 		if(!t)
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("1wcdapl55tiv1q3u4xsoltmsb"); // 		else if(p)
UNSUPPORTED("3rwbnud1fhq4g3dgtggb8ivmp"); // 			p->right = t->right;
UNSUPPORTED("blm4o9icjcy4qqrndqfpqqskz"); // 		else if((p = *s) == t)
UNSUPPORTED("4it0csugmcnk8ky4nutyv355f"); // 			p = *s = t->right;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("ab3bg0ss7wkmrlthm6d2cwuau"); // 		{	while(p->right != t)
UNSUPPORTED("27m25gnbutgbfo7ek5u2t0l5w"); // 				p = p->right;
UNSUPPORTED("3rwbnud1fhq4g3dgtggb8ivmp"); // 			p->right = t->right;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("hy98ituujoflafzec4f3ggok"); // 		obj = (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) );
UNSUPPORTED("bhx4ntuz9q54vz0qyvxhuu0vd"); // 		dt->data->size -= 1;
UNSUPPORTED("c8l936qbhd0ofjozm9bngu6sg"); // 		dt->data->here = p;
UNSUPPORTED("e1emcft87tamqtb3x6zxbes7w"); // 		if(disc->freef && (type&0000002))
UNSUPPORTED("e4inyabba5878wlc02vhj5bos"); // 			(*disc->freef)(dt,obj,disc);
UNSUPPORTED("azvf369rtz6gyaywwhk7fgjk2"); // 		if(disc->link < 0)
UNSUPPORTED("3tov74p8twbpi30dtilg9m6my"); // 			(*dt->memoryf)(dt,(void*)t,0,disc);
UNSUPPORTED("62u47ehg5sw7ibh04rvqqdaws"); // 		return obj;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 4n0vhm7fg6h57qkecnis5gtj3
// static Dtmethod_t	_Dtset = 


//1 9sqlu54dfqsrwxxtvmwmq2jpg
// static Dtmethod_t	_Dtbag = 


//1 879dwfn818v9vidj17x5zmfgb
// Dtmethod_t* Dtset = &_Dtset


//1 5m11g50hbrc4sn2ajk23ydg4l
// Dtmethod_t* Dtbag = &_Dtbag


//1 9e6zvz6pkjqohy0uo1p257pj6
// Dtmethod_t		_Dthash = 


//1 bagavrxgzbv4by8jydwtj88mn
// Dtmethod_t* Dthash = &_Dthash


}
