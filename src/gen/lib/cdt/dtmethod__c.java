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

public class dtmethod__c {
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




//3 5y8zard8q0t7wd1fznlyiu0is
// Dtmethod_t* dtmethod(Dt_t* dt, Dtmethod_t* meth)      
public static Object dtmethod(Object... arg) {
UNSUPPORTED("3km1xhxqsi1kc6xhl15kcri1f"); // Dtmethod_t* dtmethod(Dt_t* dt, Dtmethod_t* meth)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bf4mmzf6u2tqotgqbd6k8eqp0"); // 	register Dtlink_t	*list, *r;
UNSUPPORTED("ab9fv18fhjzwp3wngyyqarrns"); // 	register Dtdisc_t*	disc = dt->disc;
UNSUPPORTED("crjx4y1aesvbz4wkqg5642nj4"); // 	register Dtmethod_t*	oldmeth = dt->meth;
UNSUPPORTED("5c6whtsf2a3glrlh1kczzpqe9"); // 	if(!meth || meth->type == oldmeth->type)
UNSUPPORTED("8ttmhoaftdijn5bkmfimd9x7"); // 		return oldmeth;
UNSUPPORTED("8gsk9kzxnlwgo3aceze9c6gl7"); // 	if(disc->eventf &&
UNSUPPORTED("fwq3340fa0pnag7j0f4o7wzm"); // 	   (*disc->eventf)(dt,4,(void*)meth,disc) < 0)
UNSUPPORTED("2igwwp2cx3n29p50m7m8ioyv2"); // 		return ((Dtmethod_t*)0);
UNSUPPORTED("2eqdiqvcsxis68sccmci5sslk"); // 	dt->data->minp = 0;
UNSUPPORTED("3lnvvw51l9ruzql33ygokb3l9"); // 	/* get the list of elements */
UNSUPPORTED("458eivbagnsewjl8xhw519qcg"); // 	list = dtflatten(dt);
UNSUPPORTED("9qekjyedz55m5wna2o1oggk12"); // 	if(dt->data->type&(0000020|0000040|0000100) )
UNSUPPORTED("3v40gkacc2m6me0zor7nhpjmw"); // 		dt->data->hh._head = ((Dtlink_t*)0);
UNSUPPORTED("9l8vi2w86i0txe17utistljqu"); // 	else if(dt->data->type&(0000001|0000002) )
UNSUPPORTED("hysmhouy1ud6hsdj0cubiudt"); // 	{	if(dt->data->ntab > 0)
UNSUPPORTED("4ugmcpi8vkb013vuo4wykn0a3"); // 			(*dt->memoryf)(dt,(void*)dt->data->hh._htab,0,disc);
UNSUPPORTED("daqygvp6d794yl5eu8d06g3gi"); // 		dt->data->ntab = 0;
UNSUPPORTED("1wghucytlosmvwxh9hs06gqnv"); // 		dt->data->hh._htab = ((Dtlink_t**)0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bl1pmhqf3x1in1wvwhq1v00u2"); // 	dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("762s6dooy1oj4wiqk2j8mtcs7"); // 	dt->data->type = (dt->data->type&~(0000377|010000)) | meth->type;
UNSUPPORTED("62j00tqtfsxxmirwb05ur7z0t"); // 	dt->meth = meth;
UNSUPPORTED("3hek07go1qr5flyhweyvb88hh"); // 	if(dt->searchf == oldmeth->searchf)
UNSUPPORTED("7d7hyoujq87agx8focnjapazd"); // 		dt->searchf = meth->searchf;
UNSUPPORTED("c0iupu78ptdjv7tkp4fp5eoem"); // 	if(meth->type&(0000020|0000040|0000100) )
UNSUPPORTED("10dxbmrp9w60lb4wyi4c6ze8a"); // 	{	if(!(oldmeth->type&(0000020|0000040|0000100)) )
UNSUPPORTED("c6eshgqlcm9am6oih7mqy27cx"); // 		{	if((r = list) )
UNSUPPORTED("cxw84g4fps5l02s4epfyl6n2r"); // 			{	register Dtlink_t*	t;
UNSUPPORTED("et4fiw7yaciidg54t59if0gz"); // 				for(t = r->right; t; r = t, t = t->right )
UNSUPPORTED("6y5abi7x957jjhe2rscd9ufjx"); // 					t->hl._left = r;
UNSUPPORTED("8nfo3btdkjap25oecr3r2t9zz"); // 				list->hl._left = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7duc1evkuyrgnx1k6sqnyoc1e"); // 		dt->data->hh._head = list;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("99xc5unqmtom8dp50z8en6k79"); // 	else if(meth->type&(0000004|0000010))
UNSUPPORTED("dejooawjmch41k0n8wz35280p"); // 	{	dt->data->size = 0;
UNSUPPORTED("dzuwpmnfrpq395y1ou6zll3u2"); // 		while(list)
UNSUPPORTED("4b3ko2bznm8f1y9qeju4acfua"); // 		{	r = list->right;
UNSUPPORTED("bdgaa5aljhk83mgc5oupnofue"); // 			(*meth->searchf)(dt,(void*)list,0000040);
UNSUPPORTED("eoujol9u634bcw14kwkb2lm5x"); // 			list = r;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5i0ckqkt3yzykzquvm6nd08wf"); // 	else if(!((meth->type&0000002) && (oldmeth->type&0000001)) )
UNSUPPORTED("34wwvljvfvcvyh3obzyhgmnpo"); // 	{	int	rehash;
UNSUPPORTED("6akycohy5byonwsy0g7a3l1nk"); // 		if((meth->type&(0000001|0000002)) && !(oldmeth->type&(0000001|0000002)))
UNSUPPORTED("4o1gcrc4m2r1348akifhsvudt"); // 			rehash = 1;
UNSUPPORTED("1byx9oyi0ig37sdx42lv59xv"); // 		else	rehash = 0;
UNSUPPORTED("4zu0hy7teh1cxc74ifeho8ej3"); // 		dt->data->size = dt->data->loop = 0;
UNSUPPORTED("dzuwpmnfrpq395y1ou6zll3u2"); // 		while(list)
UNSUPPORTED("4b3ko2bznm8f1y9qeju4acfua"); // 		{	r = list->right;
UNSUPPORTED("20x8nlwk3x08qy0vwwzj6qtad"); // 			if(rehash)
UNSUPPORTED("30b8etf9nx2grv2w7rsytmgxk"); // 			{	register void* key = (disc->link < 0 ? ((Dthold_t*)(list))->obj : (void*)((char*)(list) - disc->link) );
UNSUPPORTED("8h5rhllokrgydtbfodokfa8tk"); // 				key = (void*)(disc->size < 0 ? *((char**)((char*)(key)+disc->key)) : ((char*)(key)+disc->key));
UNSUPPORTED("t4uwrmevpnx4doh1anexa33y"); // 				list->hl._hash = (disc->hashf ? (*disc->hashf)(dt,key,disc) : dtstrhash(0,key,disc->size) );
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("9dj4v9f2xppzoxbumhwauuzk6"); // 			(void)(*meth->searchf)(dt,(void*)list,0000040);
UNSUPPORTED("eoujol9u634bcw14kwkb2lm5x"); // 			list = r;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("37bs415tc2fi47jgdzwr9kyeu"); // 	return oldmeth;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
