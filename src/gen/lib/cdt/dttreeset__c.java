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

public class dttreeset__c {
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




//3 wsyenh3pav82eqg8t6irqmpa
// static Dtlink_t* treebalance(Dtlink_t* list, int size)      
public static Object treebalance(Object... arg) {
UNSUPPORTED("9upl561wy34u2kgottk3knhw7"); // static Dtlink_t* treebalance(Dtlink_t* list, int size)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e3svy3s8lap0wa9dpy4q3ppie"); // 	int		n;
UNSUPPORTED("4euymk8i174zc1zk143wb5qlq"); // 	Dtlink_t	*l, *mid;
UNSUPPORTED("8hf6osd37j483sgahmda5xn1j"); // 	if(size <= 2)
UNSUPPORTED("4fl66qhxrdfm7i88fvwjalk6v"); // 		return list;
UNSUPPORTED("7cjc57w4211pyb7xn4u5mv2p3"); // 	for(l = list, n = size/2 - 1; n > 0; n -= 1)
UNSUPPORTED("dcdhqqjy6kk46tsxs923vyy6k"); // 		l = l->right;
UNSUPPORTED("y7f7c2k7x27uc6ph132zknhi"); // 	mid = l->right; l->right = ((Dtlink_t*)0);
UNSUPPORTED("6z99uyzu8i3gvzeo5lrxnw4vo"); // 	mid->hl._left  = treebalance(list, (n = size/2) );
UNSUPPORTED("7pvd4214bcxozaimgklaq3qif"); // 	mid->right = treebalance(mid->right, size - (n + 1));
UNSUPPORTED("7zdak9tmoe5cam875dr3zz3wn"); // 	return mid;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7iha6xdoo376u861k6wv8c5pr
// int dttreeset(Dt_t* dt, int minp, int balance)       
public static Object dttreeset(Object... arg) {
UNSUPPORTED("3t7ugtrfg37d90rb22ee962hg"); // int dttreeset(Dt_t* dt, int minp, int balance)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dtghok0zzu4g8fnpx91sx0xav"); // 	int	size;
UNSUPPORTED("4mt1ak6xs18a3xe6dccf6hf3m"); // 	if(dt->meth->type != 0000004)
UNSUPPORTED("b0epxudfxjm8kichhaautm2qi"); // 		return -1;
UNSUPPORTED("e1eh9pmpa4fopxse0olzgtcum"); // 	size = dtsize(dt);
UNSUPPORTED("aafwg5rqya20bgn3vhz3t2iqb"); // 	if(minp < 0)
UNSUPPORTED("9ox4x4zosos0jeacd6v18u8ak"); // 	{	for(minp = 0; minp < (sizeof(size_t)*8 - 2); ++minp)
UNSUPPORTED("4a30x5e8menohy668n1iozj52"); // 			if((1 << minp) >= size)
UNSUPPORTED("1dhrv6aj5eq8ntuvb7qbs8aot"); // 				break;
UNSUPPORTED("7v4oowgea39cogwcgkt7np8iv"); // 		if(minp <= (sizeof(size_t)*8 - 2)-4)	/* use log(size) + 4 */
UNSUPPORTED("aahg7tgohnhh704ric6b2h83i"); // 			minp += 4;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("80ideyp40y4dgch5lhho631ig"); // 	if((dt->data->minp = minp + (minp%2)) > (sizeof(size_t)*8 - 2))
UNSUPPORTED("c19ehve53a2rtc95cfpgwc2ua"); // 		dt->data->minp = (sizeof(size_t)*8 - 2);
UNSUPPORTED("s52ssf9x1ga1f1e63jhd0caj"); // 	if(balance)
UNSUPPORTED("3nozetyxtt4d04pcxdwbatake"); // 		dt->data->here = treebalance(dtflatten(dt), size);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
