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

public class dtlist__c {
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




//3 dyc3bu1thij9v7t35zhwd5nsq
// static void* dtlist(register Dt_t* dt, register void* obj, register int type)       
public static Object dtlist(Object... arg) {
UNSUPPORTED("2zck894ixnu9hdr876kp5kkil"); // static void* dtlist(register Dt_t* dt, register void* obj, register int type)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("660kbsr1gj2c3whxtgelujyj1"); // 	register int		lk, sz, ky;
UNSUPPORTED("d7uh8kuvwiwc4dkgh3xeqa7o"); // 	register Dtcompar_f	cmpf;
UNSUPPORTED("boylfimuhvbo4nqzby3ew5tld"); // 	register Dtdisc_t*	disc;
UNSUPPORTED("3wo04u3bwxjm2adizil0k6dlz"); // 	register Dtlink_t	*r, *t;
UNSUPPORTED("323rs06etsryh0uezhkjtbd04"); // 	register void	*key, *k;
UNSUPPORTED("ckjxpmega2z5oju8xk72wsy0q"); // 	((dt->data->type&010000) ? dtrestore(dt,((Dtlink_t*)0)) : 0);
UNSUPPORTED("a9vwj28gsu58hp4ocpvno985d"); // 	disc = dt->disc; (ky = disc->key, sz = disc->size, lk = disc->link, cmpf = disc->comparf);
UNSUPPORTED("dpg99ryukgexcc5n31wdbvraz"); // 	dt->type &= ~0100000;
UNSUPPORTED("e4rxlgclvydwuznpw7rk0rksa"); // 	if(!obj)
UNSUPPORTED("9vsb4m488mn6r0xd65c3b0ngc"); // 	{	if(type&(0000400|0000200) )
UNSUPPORTED("34qzq70onioczf5s82mjobdhc"); // 		{	if((r = dt->data->hh._head) )
UNSUPPORTED("a1ikx5viqbw7iuszqxrdkfa5j"); // 			{	if(type&0000400)
UNSUPPORTED("aiwt2i7u6w149zbfq2c4f66g9"); // 					r = r->hl._left;
UNSUPPORTED("c1wfwlzje3qf7cn9yrd58r8km"); // 				dt->data->here = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("1ccr1wu363pwoqxfhz7hj4sho"); // 			return r ? (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) ) : ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("71r0p0szovteqd7xetljof9k9"); // 		else if(type&(0000002|0010000))
UNSUPPORTED("rrsc72yw16kb4a9padzlgblh"); // 		{	if((dt->data->type&(0000020|0000200)) || !(r = dt->data->hh._head))
UNSUPPORTED("aa9atfxr38jo3q71l1xw0bq6k"); // 				return ((void*)0);
UNSUPPORTED("4btdyuasa2zps0lqshubsiulz"); // 			else	goto dt_delete;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("bg2msoyh5n8nbv3jrv98xl6jr"); // 		else if(type&0000100)
UNSUPPORTED("8ema02xdl88j1cg663kqjjmoy"); // 		{	if(disc->freef || disc->link < 0)
UNSUPPORTED("8yt6l4wvynb2dh7majp6twtg0"); // 			{	for(r = dt->data->hh._head; r; r = t)
UNSUPPORTED("5hywksyjo83f15ogrt3lyqs9e"); // 				{	t = r->right;
UNSUPPORTED("9la822tbyggyiqojilsi237uo"); // 					if(disc->freef)
UNSUPPORTED("eglki07oisy1jcm5hyv0ot6so"); // 						(*disc->freef)(dt,(lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) ),disc);
UNSUPPORTED("7hdvwe89xp5vg9m4l8lpmgvap"); // 					if(disc->link < 0)
UNSUPPORTED("dy8c0o748cjxxm1qbvkm09kh7"); // 						(*dt->memoryf)(dt,(void*)r,0,disc);
UNSUPPORTED("cysnuxd51taci3hbg5lifz8ce"); // 				}
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("340j9mmtga6x1r9zokywny8a5"); // 			dt->data->hh._head = dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("196oh3ma81y2lao0qwcvuwvga"); // 			dt->data->size = 0;
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("buskbis6zqn7fgycezsnmb765"); // 		else	return ((void*)0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("17p4kgmskj2wc3ay0ttkyyj16"); // 	if(type&(0000001|0004000))
UNSUPPORTED("69mih5ilj4h72bp4vqpelae3s"); // 	{	if(disc->makef && (type&0000001) &&
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
UNSUPPORTED("9xarci6m8xoxbhm19aicfu6op"); // 		if(dt->data->type&0000200)
UNSUPPORTED("4oa8mskcvinqc3ghe4qb3z2dn"); // 		{	if(type&0020000)
UNSUPPORTED("6x0vnmjxngk10oi72383wrqdf"); // 				goto dt_queue;
UNSUPPORTED("2nk49e7v4apz4sgkwea1rocq3"); // 			else	goto dt_stack;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("d772otym2gfcniiga03pmsruf"); // 		else if(dt->data->type&0000020)
UNSUPPORTED("4oa8mskcvinqc3ghe4qb3z2dn"); // 		{	if(type&0020000)
UNSUPPORTED("4ojwnzwt744lvh40g1sz5nks1"); // 			{	if(!(t = dt->data->here) || !t->right)
UNSUPPORTED("e3u46nsizxj0zzm1yuz6mkm5t"); // 					goto dt_queue;
UNSUPPORTED("90e3icond38fe55d3t9mohnek"); // 				r->right = t->right;
UNSUPPORTED("esezu2zro83gvkxdm9xhz9ogd"); // 				r->right->hl._left = r;
UNSUPPORTED("2221i8wr1imn1ch4agvlotbvu"); // 				r->hl._left = t;
UNSUPPORTED("bdrsml91tltn4duaj0fics6nd"); // 				r->hl._left->right = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("cqgi8f4d37bqva8z6bx5rvn7w"); // 			else
UNSUPPORTED("2k0cdvd207renm6g59nxxjqs8"); // 			{	if(!(t = dt->data->here) || t == dt->data->hh._head)
UNSUPPORTED("8b34roshgo8gwffhg9ma3gsi5"); // 					goto dt_stack;
UNSUPPORTED("8g4y8anmfv0cidynh11kl3g24"); // 				r->hl._left = t->hl._left;
UNSUPPORTED("bdrsml91tltn4duaj0fics6nd"); // 				r->hl._left->right = r;
UNSUPPORTED("bpwbkzd8dztbberjy54bqwr9r"); // 				r->right = t;
UNSUPPORTED("esezu2zro83gvkxdm9xhz9ogd"); // 				r->right->hl._left = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("22wam4b54ielgtcvtdx3fxy3r"); // 		else if(dt->data->type&0000040)
UNSUPPORTED("7eyhwlknuhbf62yfdojjgchdk"); // 		{ dt_stack:
UNSUPPORTED("4ikujhrb5r2ufyhi6857ek42v"); // 			r->right = t = dt->data->hh._head;
UNSUPPORTED("9onfbw0e4egk5q69g1khqbzd"); // 			if(t)
UNSUPPORTED("35wihezut0mc3f7nshp29ate5"); // 			{	r->hl._left = t->hl._left;
UNSUPPORTED("638235mxuepku23w4hxkl3ep6"); // 				t->hl._left = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("2du87rzew8rt0830ob1tnqxo6"); // 			else	r->hl._left = r;
UNSUPPORTED("7u6umzefjt96rok3qemy1f717"); // 			dt->data->hh._head = r;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("5tttas5l0qfvco787vns2uan4"); // 		else /* if(dt->data->type&DT_QUEUE) */
UNSUPPORTED("ew3f4m8ld77ai8s7ndfdee9v4"); // 		{ dt_queue:
UNSUPPORTED("1aeq93f540gclms5vcjs7rg6a"); // 			if((t = dt->data->hh._head) )
UNSUPPORTED("4pksv1ru7zqlljrja5f514o1j"); // 			{	t->hl._left->right = r;
UNSUPPORTED("8g4y8anmfv0cidynh11kl3g24"); // 				r->hl._left = t->hl._left;
UNSUPPORTED("638235mxuepku23w4hxkl3ep6"); // 				t->hl._left = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("cqgi8f4d37bqva8z6bx5rvn7w"); // 			else
UNSUPPORTED("4rj7250ci10tq7lo05uezv8w4"); // 			{	dt->data->hh._head = r;
UNSUPPORTED("dg1qkalat0zmys95gxdc4j24o"); // 				r->hl._left = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("my81njr3zdj3gj3b86jcc8o3"); // 			r->right = ((Dtlink_t*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("4p7dqfnzg979k37w3hvqbjhbq"); // 		if(dt->data->size >= 0)
UNSUPPORTED("1ot5ghc6sf3uiepe9suupzcy4"); // 			dt->data->size += 1;
UNSUPPORTED("7nqs3a1rtwdoaksqwklk6h9mr"); // 		dt->data->here = r;
UNSUPPORTED("6q9hi4nb5yoxs4rcb8mon9vdb"); // 		return (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) );
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("69r6haab9votyq47nu1m8m9mo"); // 	if((type&0001000) || !(r = dt->data->here) || (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) ) != obj)
UNSUPPORTED("dow6c5q06mynnixn0dg629l8s"); // 	{	key = (type&0001000) ? obj : (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
UNSUPPORTED("5o1yjgbu0qjlg99lqs1h06iyv"); // 		for(r = dt->data->hh._head; r; r = r->right)
UNSUPPORTED("9d15odjmf2hxhhdsf9s2up2fd"); // 		{	k = (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) ); k = (void*)(sz < 0 ? *((char**)((char*)(k)+ky)) : ((char*)(k)+ky));
UNSUPPORTED("dnok81nrhi3pajt355zkqm76w"); // 			if((cmpf ? (*cmpf)(dt,key,k,disc) : (sz <= 0 ? strcmp(key,k) : memcmp(key,k,sz)) ) == 0)
UNSUPPORTED("1dhrv6aj5eq8ntuvb7qbs8aot"); // 				break;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("be1db0r6fggnry37s9bjem8so"); // 	if(!r)
UNSUPPORTED("5p6q7ip4om6y4nrsjz07ua456"); // 		return ((void*)0);
UNSUPPORTED("esjg4ndwtf9ryvxfle5f1awdw"); // 	dt->type |= 0100000;
UNSUPPORTED("2ptdjk4ijgvrjv9sjqrx5b08n"); // 	if(type&(0000002|0010000))
UNSUPPORTED("83b3sp1f1vvsa2w5nqdh831ux"); // 	{ dt_delete:
UNSUPPORTED("3ohzd40a39b2h609ckmcpsxvw"); // 		if(r->right)
UNSUPPORTED("2p8p0mpv6dnqncm0ewg9gtn53"); // 			r->right->hl._left = r->hl._left;
UNSUPPORTED("2ankbgvq7yr3z93tl1xnbfgpr"); // 		if(r == (t = dt->data->hh._head) )
UNSUPPORTED("8ee45f3kh06ajncucnq4d4zn3"); // 		{	dt->data->hh._head = r->right;
UNSUPPORTED("58dd1wmd3q6hu7jmyttutbtjp"); // 			if(dt->data->hh._head)
UNSUPPORTED("24pdk7685q5i3eyip2b52xc1a"); // 				dt->data->hh._head->hl._left = t->hl._left;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("uo5j3uk44ezbi34a4lez0vko"); // 		{	r->hl._left->right = r->right;
UNSUPPORTED("122rcml8ecn8royyv655mfxp7"); // 			if(r == t->hl._left)
UNSUPPORTED("crlanj0wz66t4lpcauaoonqsc"); // 				t->hl._left = r->hl._left;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("2550dhcfkngbylvb1phmq8ebq"); // 		dt->data->here = r == dt->data->here ? r->right : ((Dtlink_t*)0);
UNSUPPORTED("bhx4ntuz9q54vz0qyvxhuu0vd"); // 		dt->data->size -= 1;
UNSUPPORTED("5z333zdlm5fisvkd28wdk08lb"); // 		obj = (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) );
UNSUPPORTED("e1emcft87tamqtb3x6zxbes7w"); // 		if(disc->freef && (type&0000002))
UNSUPPORTED("e4inyabba5878wlc02vhj5bos"); // 			(*disc->freef)(dt,obj,disc);
UNSUPPORTED("azvf369rtz6gyaywwhk7fgjk2"); // 		if(disc->link < 0)
UNSUPPORTED("16tb09hfz1sv26gfi60t8ji16"); // 			(*dt->memoryf)(dt,(void*)r,0,disc);
UNSUPPORTED("62u47ehg5sw7ibh04rvqqdaws"); // 		return obj;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5k3wkmt1521n2igrbw361wdxw"); // 	else if(type&0000010)
UNSUPPORTED("4zcwolmq8zlneya8ew49t1qc4"); // 		r = r->right;
UNSUPPORTED("eeak67ajn02aq4hfkpb1houzf"); // 	else if(type&0000020)
UNSUPPORTED("80ghi58dnl87w9e87qt2lw3jn"); // 		r = r == dt->data->hh._head ? ((Dtlink_t*)0) : r->hl._left;
UNSUPPORTED("1osns9frhkriafbjyob0sbz8z"); // 	dt->data->here = r;
UNSUPPORTED("cij46y936bg1ikv3ztksloy91"); // 	return r ? (lk < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - lk) ) : ((void*)0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 9hopil17a8eg1agt37070u73q
// Dtmethod_t _Dtlist  = 


//1 1fxnvjagcf621oxjj3ciypwfy
// Dtmethod_t _Dtdeque  = 


//1 40f33oiu7l21l1jxw9wfoux8f
// Dtmethod_t _Dtstack = 


//1 14hp9ggjclgs43wn0uwmfw77d
// Dtmethod_t _Dtqueue = 


//1 b1q50g67csjki72iuef5of8zy
// Dtmethod_t* Dtlist = &_Dtlist


//1 62b2csa6dv66okdkjb7evy47o
// Dtmethod_t* Dtdeque = &_Dtdeque


//1 4e88n3c3ml5i5mf3oqr7si856
// Dtmethod_t* Dtstack = &_Dtstack


//1 zroiycxvakotau1jgb16sjzz
// Dtmethod_t* Dtqueue = &_Dtqueue


}
