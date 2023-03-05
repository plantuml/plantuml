/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2022, Arnaud Roques
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
import static smetana.core.Macro.DT_CLEAR;
import static smetana.core.Macro.DT_DELETE;
import static smetana.core.Macro.DT_FIRST;
import static smetana.core.Macro.DT_INSERT;
import static smetana.core.Macro.DT_LAST;
import static smetana.core.Macro.DT_MATCH;
import static smetana.core.Macro.DT_NEXT;
import static smetana.core.Macro.DT_OBAG;
import static smetana.core.Macro.DT_OSET;
import static smetana.core.Macro.DT_PREV;
import static smetana.core.Macro.DT_RENEW;
import static smetana.core.Macro.DT_SEARCH;
import static smetana.core.Macro.UNFLATTEN;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro._DTCMP;
import static smetana.core.Macro._DTKEY;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_dt_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.FieldOffset;
import smetana.core.Globals;
import smetana.core.__ptr__;

public class dtview__c {

public static CFunction dtvsearch = new CFunctionAbstract("dtvsearch") {
	
	public Object exe(Globals zz, Object... args) {
		return dtvsearch(zz, (ST_dt_s)args[0], (__ptr__)args[1], (Integer)args[2]);
	}};

/*	Set a view path from dict to view.
**
**	Written by Kiem-Phong Vo (5/25/96)
*/
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cdt/dtview.c", name="dtvsearch", key="6spidg45w8teb64726drdswaa", definition="static void* dtvsearch(Dt_t* dt, register void* obj, register int type)")
public static __ptr__ dtvsearch(Globals zz, ST_dt_s dt, __ptr__ obj, int type) {
ENTERING("6spidg45w8teb64726drdswaa","dtvsearch");
try {
	ST_dt_s		d, p;
	__ptr__		o=null, n, ok, nk;
	int		cmp, sz; FieldOffset lk, ky;
	CFunction	cmpf;
	
	
	/* these operations only happen at the top level */
	if ((type&(DT_INSERT|DT_DELETE|DT_CLEAR|DT_RENEW))!=0)
		return (__ptr__) dt.meth.searchf.exe(zz, dt, obj, type);
	
	
	if(((type&(DT_MATCH|DT_SEARCH))!=0) || /* order sets first/last done below */
	   (((type&(DT_FIRST|DT_LAST))!=0) && (dt.meth.type&(DT_OBAG|DT_OSET)) == 0 ) )
	{	for(d = dt; d!=null; d = d.view)
			if((o = (__ptr__) d.meth.searchf.exe(zz, d,obj,type))!=null )
				break;
		dt.walk = d;
		return o;
	}
	
	
	if((dt.meth.type & (DT_OBAG|DT_OSET) )!=0)
	{	if((type & (DT_FIRST|DT_LAST|DT_NEXT|DT_PREV)) == 0 )
			return null;
	
	
		n = nk = null; p = null;
		for(d = dt; d!=null; d = d.view)
		{	if((o = (__ptr__) d.meth.searchf.exe(zz, d, obj, type) ) == null)
				continue;
			ky = d.disc.key;
			sz = d.disc.size;
			lk = d.disc.link;
			cmpf = d.disc.comparf;
			ok = _DTKEY(o, ky, sz);
			
			
			if(n!=null) /* get the right one among all dictionaries */
			{	cmp = _DTCMP(zz, d, ok, nk, d.disc, cmpf, sz);
				if(((type & (DT_NEXT|DT_FIRST))!=0 && cmp < 0) ||
				   ((type & (DT_PREV|DT_LAST))!=0 && cmp > 0) )
UNSUPPORTED("5o3u9aaanyd9yh74sjfkkofmo"); // 					goto a_dj;
			}
			else /* looks good for now */
			{ a_dj: p  = d;
				n  = o;
				nk = ok;
			}
		}
		dt.walk = p;
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





@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cdt/dtview.c", name="dtview", key="dfryioch2xz35w8nq6lxbk5kh", definition="Dt_t* dtview(register Dt_t* dt, register Dt_t* view)")
public static ST_dt_s dtview(ST_dt_s dt, ST_dt_s view) {
ENTERING("dfryioch2xz35w8nq6lxbk5kh","dtview");
try {
	ST_dt_s	d;
	
	UNFLATTEN(dt);
	if(view!=null)
	{	UNFLATTEN(view);
		if(view.meth != dt.meth) /* must use the same method */
			UNSUPPORTED("return null;");
	}
	
	
	/* make sure there won't be a cycle */
	for(d = view; d!=null; d = (ST_dt_s)d.view)
		if(d == dt)
			return null;
	
	
	/* no more viewing lower dictionary */
	if((d = dt.view)!=null )
		d.nview -= 1;
	dt.view = dt.walk = null;

	if((view) == null)
	{	dt.searchf = dt.meth.searchf;
		return d;
	}
	
	/* ok */
	dt.view = view;
	dt.searchf = dtview__c.dtvsearch;
	view.nview += 1;
	
	
	return view;
} finally {
LEAVING("dfryioch2xz35w8nq6lxbk5kh","dtview");
}
}






}
