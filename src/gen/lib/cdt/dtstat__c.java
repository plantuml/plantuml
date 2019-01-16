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

public class dtstat__c {
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




//3 91c7rdavgvzhuigvve5ibdpn0
// static void dttstat(Dtstat_t* ds, Dtlink_t* root, int depth, int* level)        
public static Object dttstat(Object... arg) {
UNSUPPORTED("96759a5l0ygg0om48m2a6t0ng"); // static void dttstat(Dtstat_t* ds, Dtlink_t* root, int depth, int* level)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("56ka5rmkoppf3hruu8g6m0xja"); // 	if(root->hl._left)
UNSUPPORTED("cid9xp3s0qj3aabaka0l9zv7t"); // 		dttstat(ds,root->hl._left,depth+1,level);
UNSUPPORTED("9s9ahe7p2w6fq5emj7g8wbjfr"); // 	if(root->right)
UNSUPPORTED("87cqywe08opun9vcdypm5sngz"); // 		dttstat(ds,root->right,depth+1,level);
UNSUPPORTED("1cpnp52f4qqoepwe6x0k1xfba"); // 	if(depth > ds->dt_n)
UNSUPPORTED("7ufge640ert57jo7j225ab7pe"); // 		ds->dt_n = depth;
UNSUPPORTED("3pj5kfo9kjuutf0og8g2j8geg"); // 	if(level)
UNSUPPORTED("9gine8rpyhyyk5yrtmvl56o2p"); // 		level[depth] += 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a8o5okh4tw99gnboco3s2dmxd
// static void dthstat(register Dtdata_t* data, Dtstat_t* ds, register int* count)       
public static Object dthstat(Object... arg) {
UNSUPPORTED("48ylybwp2yevlr561pyv3jui"); // static void dthstat(register Dtdata_t* data, Dtstat_t* ds, register int* count)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bqowb8dyq0iy0kow4ic3v69rm"); // 	register Dtlink_t*	t;
UNSUPPORTED("1kv2h7mwh5yurbekfwfiewdhy"); // 	register int		n, h;
UNSUPPORTED("286ioif0j041zmjzncvg5xoll"); // 	for(h = data->ntab-1; h >= 0; --h)
UNSUPPORTED("3q5ha8vx2tajjl4ufu2w0no4p"); // 	{	n = 0;
UNSUPPORTED("csnxwylxkitnmc1zga3njlqgd"); // 		for(t = data->hh._htab[h]; t; t = t->right)
UNSUPPORTED("90p2oslbn5buyc5k0tkflj50d"); // 			n += 1;
UNSUPPORTED("1pfuj1amofbpxyeubend5yxr5"); // 		if(count)
UNSUPPORTED("5nwitx1rnfie4jd6ny8ssadzg"); // 			count[n] += 1;
UNSUPPORTED("3w8in45yqqsl7kb6o0suodbq0"); // 		else if(n > 0)
UNSUPPORTED("ck4u5h04gsnndyuthxg9kuixs"); // 		{	ds->dt_n += 1;
UNSUPPORTED("487tl3ivbjnaxewh91pw7xeuh"); // 			if(n > ds->dt_max)
UNSUPPORTED("arga2k65qq28lxquyhcmmii6p"); // 				ds->dt_max = n;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1kbkj84h5jc45cmi7lzd45dp
// int dtstat(register Dt_t* dt, Dtstat_t* ds, int all)       
public static Object dtstat(Object... arg) {
UNSUPPORTED("ao2i3bpekp72m9gi7y5ckz4rk"); // int dtstat(register Dt_t* dt, Dtstat_t* ds, int all)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("44m1eek6bje0mjqjlsecsorod"); // 	register int		i;
UNSUPPORTED("26pvlo4hyfclsuk4h43qetsjw"); // 	static int	*Count, Size;
UNSUPPORTED("ckjxpmega2z5oju8xk72wsy0q"); // 	((dt->data->type&010000) ? dtrestore(dt,((Dtlink_t*)0)) : 0);
UNSUPPORTED("ec11eijzw85083qye8hp5eex0"); // 	ds->dt_n = ds->dt_max = 0;
UNSUPPORTED("3tdmcypiwfwu1pn4i6zzf500z"); // 	ds->dt_count = ((int*)0);
UNSUPPORTED("azymim1m9qe68uhp1mftu7fe1"); // 	ds->dt_size = dtsize(dt);
UNSUPPORTED("av3q24lmuasi6ojxa3wldv6ta"); // 	ds->dt_meth = dt->data->type&0000377;
UNSUPPORTED("7wb0sc472yga8vixhnog657ny"); // 	if(!all)
UNSUPPORTED("5izxoao5ryte71964f8yjfd5y"); // 		return 0;
UNSUPPORTED("32ow9kmsxf47y1h17vyj3ef11"); // 	if(dt->data->type&(0000001|0000002))
UNSUPPORTED("3h1ytwsfcbphbuuhvj29vqlpn"); // 	{	dthstat(dt->data,ds,((int*)0));
UNSUPPORTED("cg0ykqnfk3ou03i5e9ykd3m5n"); // 		if(ds->dt_max+1 > Size)
UNSUPPORTED("4ebwk82nv6oujn3zi64gvqqr7"); // 		{	if(Size > 0)
UNSUPPORTED("4dxxr85kxn2tyyzxi82w0iqak"); // 				free(Count);
UNSUPPORTED("3a6ksysaw3s4hfdjvet2q2qxc"); // 			if(!(Count = (int*)malloc((ds->dt_max+1)*sizeof(int))) )
UNSUPPORTED("6pxdbvdu6loxn1ex6nm9i93gb"); // 				return -1;
UNSUPPORTED("13xjtjclxu8ornn37l48jz2rv"); // 			Size = ds->dt_max+1;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("36u41cy59sloyiha209o4m5qy"); // 		for(i = ds->dt_max; i >= 0; --i)
UNSUPPORTED("8aibp56k7f0yagfchektytbwy"); // 			Count[i] = 0;
UNSUPPORTED("b5vi0pdbdr81ex0fk7lepptgm"); // 		dthstat(dt->data,ds,Count);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("av63gjdiqp6xi9w4vjwqpn5oo"); // 	else if(dt->data->type&(0000004|0000010))
UNSUPPORTED("bld1jk0owgrtf4hcqqhwoccpy"); // 	{	if(dt->data->here)
UNSUPPORTED("31bytekoo4eleuggsetk4m6de"); // 		{	dttstat(ds,dt->data->here,0,((int*)0));
UNSUPPORTED("7hz6680zzj4qr9s3dm6v435sk"); // 			if(ds->dt_n+1 > Size)
UNSUPPORTED("aq4shezrsmlv37xas7p8ho5sg"); // 			{	if(Size > 0)
UNSUPPORTED("3y929awdu2jrmw3aer6dqygxb"); // 					free(Count);
UNSUPPORTED("ex3430euevoine0wgb82be0rc"); // 				if(!(Count = (int*)malloc((ds->dt_n+1)*sizeof(int))) )
UNSUPPORTED("9qiuahtnvh744qet6fj8wk596"); // 					return -1;
UNSUPPORTED("e80mtvhrb447fuzvxw7tmlk79"); // 				Size = ds->dt_n+1;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("ce7lqmqeaeqmtpznl57wppd5n"); // 			for(i = ds->dt_n; i >= 0; --i)
UNSUPPORTED("6j9e4nc2e8eecvywkpfm2ut9m"); // 				Count[i] = 0;
UNSUPPORTED("7vm7v7g848394ourjpirftcq7"); // 			dttstat(ds,dt->data->here,0,Count);
UNSUPPORTED("ce7lqmqeaeqmtpznl57wppd5n"); // 			for(i = ds->dt_n; i >= 0; --i)
UNSUPPORTED("2t8ajn7ynd4ll8m6rug9gxbsx"); // 				if(Count[i] > ds->dt_max)
UNSUPPORTED("8d36wfd4xwyzw0stap7i7dv8y"); // 					ds->dt_max = Count[i];
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cdj1cmsf0q45eayjhj4n0zpsn"); // 	ds->dt_count = Count;
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
