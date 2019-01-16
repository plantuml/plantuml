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
import h.ST_dt_s;
import h.ST_dtlink_s;

public class dtflatten__c {
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




//3 5zkzkycgp4e90pajuq4tczdbu
// Dtlink_t* dtflatten(Dt_t* dt)     
public static ST_dtlink_s dtflatten(ST_dt_s dt) {
UNSUPPORTED("1u9gaaespn5nsfpluy23yon7s"); // Dtlink_t* dtflatten(Dt_t* dt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("82qvflv3akadrlktyouix927i"); // 	register Dtlink_t	*t, *r, *list, *last, **s, **ends;
UNSUPPORTED("3ku8njvyle1rle6f9ipn7rggg"); // 	/* already flattened */
UNSUPPORTED("88rit68bon46m42ei6oby73oo"); // 	if(dt->data->type&010000 )
UNSUPPORTED("d43szif4rveu1dyhv72xv101u"); // 		return dt->data->here;
UNSUPPORTED("1se1h7w1gasp0ag8tisjl5pox"); // 	list = last = ((Dtlink_t*)0);
UNSUPPORTED("32ow9kmsxf47y1h17vyj3ef11"); // 	if(dt->data->type&(0000001|0000002))
UNSUPPORTED("9tn2ho5yddz96m6l7hzk1m1v7"); // 	{	for(ends = (s = dt->data->hh._htab) + dt->data->ntab; s < ends; ++s)
UNSUPPORTED("5n6odtt738etawc5nprf2q25r"); // 		{	if((t = *s) )
UNSUPPORTED("7bsf06vm0raykokk6wn5303yf"); // 			{	if(last)
UNSUPPORTED("9albzpygbxkhbrb2e3af7z7b9"); // 					last->right = t;
UNSUPPORTED("3flwmlcisbdjifpiw5azq841e"); // 				else	list = last = t;
UNSUPPORTED("1pn5heoey2lil0muuixlrz154"); // 				while(last->right)
UNSUPPORTED("14t2izprf2nq8tnxy4yre0zso"); // 					last = last->right;
UNSUPPORTED("bgkf4krgjdc0kzlz7de3kbj2u"); // 				*s = last;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2lqxby7q5n5w5w5uuujup7o8a"); // 	else if(dt->data->type&(0000020|0000040|0000100) )
UNSUPPORTED("13uczemwz6rongidozoryhb8e"); // 		list = dt->data->hh._head;
UNSUPPORTED("6amw1s4wntui27fzf3qls02u5"); // 	else if((r = dt->data->here) ) /*if(dt->data->type&(DT_OSET|DT_OBAG))*/
UNSUPPORTED("solulu57qiizpqhr11tmch0h"); // 	{	while((t = r->hl._left) )
UNSUPPORTED("em247yaxylt9wh0lfmqk531iy"); // 			(((r)->hl._left = (t)->right, (t)->right = (r)), (r) = (t));
UNSUPPORTED("6ggr0z59qqyosbseo6wdmvkc3"); // 		for(list = last = r, r = r->right; r; last = r, r = r->right)
UNSUPPORTED("2jldxtvoid7uy4yqdouts2ysu"); // 		{	if((t = r->hl._left) )
UNSUPPORTED("ecb7vosojs9ov68ty9m0hzegp"); // 			{	do	(((r)->hl._left = (t)->right, (t)->right = (r)), (r) = (t));
UNSUPPORTED("f2e7di1g7bvt88336jnz0ogd5"); // 				while((t = r->hl._left) );
UNSUPPORTED("dol9o0lco748q0ap9l7afdbcs"); // 				last->right = r;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e880ypqj4mv8ru940eyjo5ene"); // 	dt->data->here = list;
UNSUPPORTED("8hv4u1n328awal8yfoqykdhxi"); // 	dt->data->type |= 010000;
UNSUPPORTED("bq8fdnp0ptlj82raupu7egafd"); // 	return list;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
