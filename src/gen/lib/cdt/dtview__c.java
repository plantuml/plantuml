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
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.function;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.UNSUPPORTED_INT;
import h.Dtcompar_f;
import h.ST_dt_s;
import smetana.core.CFunction;
import smetana.core.CString;
import smetana.core.__ptr__;

public class dtview__c {
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




//3 6spidg45w8teb64726drdswaa
// static void* dtvsearch(Dt_t* dt, register void* obj, register int type)       
public static __ptr__ dtvsearch(ST_dt_s dt, __ptr__ obj, int type) {
ENTERING("6spidg45w8teb64726drdswaa","dtvsearch");
try {
	ST_dt_s		d, p;
	__ptr__		o=null, n, ok, nk;
	int		cmp, lk, sz, ky;
	Dtcompar_f	cmpf;
	/* these operations only happen at the top level */
	if ((type&(0000001|0000002|0000100|0000040))!=0)
		return (__ptr__) dt.meth.searchf.exe(dt, obj, type);
	if(((type&(0001000|0000004))!=0) || /* order sets first/last done below */
	   (((type&(0000200|0000400))!=0) && N(dt.meth.type&(0000010|0000004)) ) )
	{	for(d = dt; d!=null; d = (ST_dt_s) d.view)
			if((o = (__ptr__) d.meth.searchf.exe(d,obj,type))!=null )
				break;
		dt.setPtr("walk", d);
		return o;
	}
	if((dt.meth.type & (0000010|0000004) )!=0)
	{	if(N(type & (0000200|0000400|0000010|0000020)) )
			return null;
		n = nk = null; p = null;
		for(d = dt; d!=null; d = (ST_dt_s) d.view)
		{	if(N(o = (__ptr__) d.meth.searchf.exe(d, obj, type) ))
				continue;
			ky = d.disc.key;
			sz = d.disc.size;
			lk = d.disc.link;
			cmpf = (Dtcompar_f) d.disc.comparf;
			ok = (__ptr__) (sz < 0 ? ((__ptr__)o).addVirtualBytes(ky) : ((__ptr__)o).addVirtualBytes(ky));
			if(n!=null) /* get the right one among all dictionaries */
			{	cmp = (cmpf!=null ? (Integer)((CFunction)cmpf).exe(d,ok,nk,d.disc) : (sz <= 0 ? strcmp((CString)ok,(CString)nk) : UNSUPPORTED_INT("memcmp(ok,nk,sz)")) );
				if(((type & (0000010|0000200))!=0 && cmp < 0) ||
				   ((type & (0000020|0000400))!=0 && cmp > 0) )
UNSUPPORTED("5o3u9aaanyd9yh74sjfkkofmo"); // 					goto a_dj;
			}
			else /* looks good for now */
			{ a_dj: p  = d;
				n  = o;
				nk = ok;
			}
		}
		dt.setPtr("walk", p);
		return n;
	}
	/* non-ordered methods */
UNSUPPORTED("36qdhpbcwst6tc1gvwcyvg91u"); // 	if(!(type & (0000010|0000020)) )
UNSUPPORTED("5p6q7ip4om6y4nrsjz07ua456"); // 		return ((void*)0);
UNSUPPORTED("bx84jj9durkqzcrq4l9h1b0za"); // 	if(!dt->walk || obj != (dt->walk->disc->link < 0 ? ((Dthold_t*)(dt->walk->data->here))->obj : (void*)((char*)(dt->walk->data->here) - dt->walk->disc->link) ) )
UNSUPPORTED("eh58afn12udc5q8yzr25advls"); // 	{	for(d = dt; d; d = d->view)
UNSUPPORTED("de5yx4s0nsbgshd9seabcy2g9"); // 			if((o = (*(d->meth->searchf))(d, obj, 0000004)) )
UNSUPPORTED("1dhrv6aj5eq8ntuvb7qbs8aot"); // 				break;
UNSUPPORTED("66mzv36wy2mflr2u2a5pwa2vg"); // 		dt->walk = d;
UNSUPPORTED("d4pllgr7b2ohssrhtxxtd1fbb"); // 		if(!(obj = o) )
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6he3c07r1xjfepuku37yav920"); // 	for(d = dt->walk, obj = (*d->meth->searchf)(d, obj, type);; )
UNSUPPORTED("d7hiatjof3gfyx3ab27oq4d74"); // 	{	while(obj) /* keep moving until finding an uncovered object */
UNSUPPORTED("44tdztkynd140cqbaafbbxvqn"); // 		{	for(p = dt; ; p = p->view)
UNSUPPORTED("72t51pabtpfsnn4qrcjvd6gkb"); // 			{	if(p == d) /* adjacent object is uncovered */	
UNSUPPORTED("4an2jpd7xs9lm1jlfrbualsrv"); // 					return obj;
UNSUPPORTED("bndt77eukkw8dnhl8sce4ts3d"); // 				if((*(p->meth->searchf))(p, obj, 0000004) )
UNSUPPORTED("6ioth986rfbv208dp59shjy15"); // 					break;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("5bx9ax8tembk4pweu41m5yw43"); // 			obj = (*d->meth->searchf)(d, obj, type);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3diupilxwbi5nroolxiy7e8b0"); // 		if(!(d = dt->walk = d->view) ) /* move on to next dictionary */
UNSUPPORTED("aihzmr4oo3tuh6kkxwtn9tlbd"); // 			return ((void*)0);
UNSUPPORTED("esje2igec9cwwteta36lbrdvc"); // 		else if(type&0000010)
UNSUPPORTED("aeh2pn8gp2xj4lehv52n4hp6b"); // 			obj = (*(d->meth->searchf))(d,((void*)0),0000200);
UNSUPPORTED("1lfqka4p8e7w15b16wquy0vjx"); // 		else	obj = (*(d->meth->searchf))(d,((void*)0),0000400);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("6spidg45w8teb64726drdswaa","dtvsearch");
}
}




//3 dfryioch2xz35w8nq6lxbk5kh
// Dt_t* dtview(register Dt_t* dt, register Dt_t* view)      
public static ST_dt_s dtview(ST_dt_s dt, ST_dt_s view) {
ENTERING("dfryioch2xz35w8nq6lxbk5kh","dtview");
try {
	ST_dt_s	d;
	if ((dt.data.type&010000)!=0) dtrestore(dt,null);
	if(view!=null)
	{	if ((view.data.type&010000)!=0) dtrestore(view,null);
		if(NEQ(view.meth, dt.meth)) /* must use the same method */
			UNSUPPORTED("return null;");
	}
	/* make sure there won't be a cycle */
	for(d = view; d!=null; d = (ST_dt_s)d.view)
		if(EQ(d, dt))
			return null;
	/* no more viewing lower dictionary */
	if((d = (ST_dt_s)dt.view)!=null )
		d.setInt("nview", d.nview-1);
	dt.setPtr("walk", null);
	dt.setPtr("view", null);
	if(N(view))
	{	dt.setPtr("searchf", dt.meth.searchf);
		return d;
	}
	/* ok */
	dt.setPtr("view", view);
	dt.setPtr("searchf", function(dtview__c.class, "dtvsearch"));
	view.setInt("nview", view.nview+1 );
	return view;
} finally {
LEAVING("dfryioch2xz35w8nq6lxbk5kh","dtview");
}
}


}
