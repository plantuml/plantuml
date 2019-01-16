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

public class dtrenew__c {
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




//3 dpntyaoee84n51hi5hei3meki
// void* dtrenew(Dt_t* dt, register void* obj)      
public static Object dtrenew(Object... arg) {
UNSUPPORTED("c1icavxc4bdl41jaglud7ov9y"); // void* dtrenew(Dt_t* dt, register void* obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("75g1y88zfpqk2mdy0p3bzcp5u"); // 	register void*	key;
UNSUPPORTED("7hzc11j4z5915otom5ec9sc1p"); // 	register Dtlink_t	*e, *t, **s;
UNSUPPORTED("ab9fv18fhjzwp3wngyyqarrns"); // 	register Dtdisc_t*	disc = dt->disc;
UNSUPPORTED("ckjxpmega2z5oju8xk72wsy0q"); // 	((dt->data->type&010000) ? dtrestore(dt,((Dtlink_t*)0)) : 0);
UNSUPPORTED("afazqxuj3nf5t37agp5loypap"); // 	if(!(e = dt->data->here) || (disc->link < 0 ? ((Dthold_t*)(e))->obj : (void*)((char*)(e) - disc->link) ) != obj)
UNSUPPORTED("5p6q7ip4om6y4nrsjz07ua456"); // 		return ((void*)0);
UNSUPPORTED("epetks7tchxd027a5fogh5s8y"); // 	if(dt->data->type&(0000040|0000100|0000020))
UNSUPPORTED("62u47ehg5sw7ibh04rvqqdaws"); // 		return obj;
UNSUPPORTED("d0a3msvhdhl5w8ezkp8afz1ay"); // 	else if(dt->data->type&(0000004|0000010) )
UNSUPPORTED("6nk9lof9sqibzqlvn7z3v0adk"); // 	{	if(!e->right )	/* make left child the new root */
UNSUPPORTED("ca72p6wo0wlrje8mc1clzkgd"); // 			dt->data->here = e->hl._left;
UNSUPPORTED("9kvzz8qhw1tc87iizefkx7jux"); // 		else		/* make right child the new root */
UNSUPPORTED("bw0zxd3ks2tqu4oa88193x30s"); // 		{	dt->data->here = e->right;
UNSUPPORTED("7xxvhxwnjmtln4k5iqxwas7gy"); // 			/* merge left subtree to right subtree */
UNSUPPORTED("b5hfkdgjlasjtdi2d8j9cnsfl"); // 			if(e->hl._left)
UNSUPPORTED("7qj77o9upzr06q7w290ppiijf"); // 			{	for(t = e->right; t->hl._left; t = t->hl._left)
UNSUPPORTED("6f76030mwgybzczalesd54a9q"); // 					;
UNSUPPORTED("31m2jyxntk5bvmxrlz7e1060n"); // 				t->hl._left = e->hl._left;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9jvkhuwh0wij26qmyu1sft73s"); // 	else /*if(dt->data->type&(DT_SET|DT_BAG))*/
UNSUPPORTED("av4lr5g8w88ulq2cwdubl498l"); // 	{	s = dt->data->hh._htab + ((e->hl._hash)&((dt->data->ntab)-1));
UNSUPPORTED("blraorobd8eq0hg3b3w3uxkwt"); // 		if((t = *s) == e)
UNSUPPORTED("6bxfg7yydrmhsw5oysk7cxytq"); // 			*s = e->right;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("z3sto7b7d7g3tgy1pobqnftq"); // 		{	for(; t->right != e; t = t->right)
UNSUPPORTED("8c0hnggfgcchqp0vr4bwobm4t"); // 				;
UNSUPPORTED("2bmnk8u0o0gw61p068qqsvd35"); // 			t->right = e->right;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7i9supotcfsya189dptbk9mg6"); // 		key = (void*)(disc->size < 0 ? *((char**)((char*)(obj)+disc->key)) : ((char*)(obj)+disc->key));
UNSUPPORTED("6k8ldpnvi2limcrix2xyqga8a"); // 		e->hl._hash = (disc->hashf ? (*disc->hashf)(dt,key,disc) : dtstrhash(0,key,disc->size) );
UNSUPPORTED("6vkn7padspfbtju9g5b65b34w"); // 		dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("zjdphdwef22jifi7kl4wial9"); // 	dt->data->size -= 1;
UNSUPPORTED("27sbkwn3jowh7b7zt5tzb2v4q"); // 	return (*dt->meth->searchf)(dt,(void*)e,0000040) ? obj : ((void*)0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
