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
import static gen.lib.cdt.dtflatten__c.dtflatten;
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static smetana.core.JUtils.function;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.Dtsearch_f;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtlink_s;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.size_t;

public class dtdisc__c {
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




//3 507t9jcy6v9twvl30rs9i2nwi
// static void* dtmemory(Dt_t* dt,void* addr,size_t size,Dtdisc_t* disc)        
public static Object dtmemory(ST_dt_s dt, Object addr, size_t size, ST_dtdisc_s disc) {
ENTERING("507t9jcy6v9twvl30rs9i2nwi","dtmemory");
try {
if(addr!=null) {
 		if(size == null) {
	Memory.free(addr);
 			return null;
 		}
UNSUPPORTED("9ed8imo9cbvwtwe92qmavoqko"); // 		else	return realloc(addr,size);
}
else	return size.isStrictPositive() ? size.malloc() : null;
throw new UnsupportedOperationException();
} finally {
LEAVING("507t9jcy6v9twvl30rs9i2nwi","dtmemory");
}
}




//3 axpvuswclmi9bx3qtlh4quyah
// Dtdisc_t* dtdisc(Dt_t* dt, Dtdisc_t* disc, int type)       
public static ST_dtdisc_s dtdisc(ST_dt_s dt, ST_dtdisc_s disc, int type) {
ENTERING("axpvuswclmi9bx3qtlh4quyah","dtdisc");
try {
	Dtsearch_f	searchf;
	ST_dtlink_s	r, t;
	CString	k;
	ST_dtdisc_s	old;
	if(N(old = (ST_dtdisc_s) dt.disc) )	/* initialization call from dtopen() */
	{	dt.setPtr("disc", disc);
		if(N(dt.setPtr("memoryf", disc.memoryf )))
			dt.setPtr("memoryf", function(dtdisc__c.class, "dtmemory"));
		return disc;
	}
	if(N(disc))	/* only want to know current discipline */
		return old;
 	searchf = (Dtsearch_f) dt.meth.searchf;
 	if((dt.data.type&010000)!=0) dtrestore(dt,null);
 	if(old.eventf!=null && ((Integer)old.eventf.exe(dt,3, disc,old)) < 0)
 		return null;
 	dt.setPtr("disc", disc);
 	if(N(dt.setPtr("memoryf", disc.memoryf)))
 		dt.setPtr("memoryf", function(dtdisc__c.class, "dtmemory"));
 	if((dt.data.type&(0000040|0000100|0000020))!=0)
UNSUPPORTED("e2tzh95k1lvjl6wbtpwizam8q"); // 		goto done;
 	else if((dt.data.type&0000002)!=0) {
UNSUPPORTED("3q5nyguq8mgdfwmm0yrzq2br8"); // 		if(type&0000002)
UNSUPPORTED("93q6zqzlgfz2qd0yl6koyw99c"); // 			goto done;
UNSUPPORTED("6d1rfacssm8768oz9fu5o66t8"); // 		else	goto dt_renew;
 	}
 	else if((dt.data.type&(0000001|0000002))!=0) {
UNSUPPORTED("8xmm1djjds55s86jodixkp72u"); // 		if((type&0000002) && (type&0000001))
UNSUPPORTED("93q6zqzlgfz2qd0yl6koyw99c"); // 			goto done;
UNSUPPORTED("6d1rfacssm8768oz9fu5o66t8"); // 		else	goto dt_renew;
 	}
 	else /*if(dt->data->type&(DT_OSET|DT_OBAG))*/ {
 		if((type&0000001)!=0)
UNSUPPORTED("93q6zqzlgfz2qd0yl6koyw99c"); // 			goto done;
// 	dt_renew:
 		r = dtflatten(dt);
UNSUPPORTED("1rry7yjzos90pgbf3li2qpa18"); // 		dt->data->type &= ~010000;
UNSUPPORTED("6vkn7padspfbtju9g5b65b34w"); // 		dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("2jfi30wa60xp7iqlk9yyf4k5j"); // 		dt->data->size = 0;
UNSUPPORTED("1i3oayy7gy36lar9kfhuq6rur"); // 		if(dt->data->type&(0000001|0000002))
UNSUPPORTED("ay51d19gimt3gpqjact2t0ypm"); // 		{	register Dtlink_t	**s, **ends;
UNSUPPORTED("5p6g054kk7snvpwuxudelseir"); // 			ends = (s = dt->data->hh._htab) + dt->data->ntab;
UNSUPPORTED("3zu1r6orkvmsvbjbzqqx9wedr"); // 			while(s < ends)
UNSUPPORTED("9wq8eycc78fg8sqi6bjce4q7f"); // 				*s++ = ((Dtlink_t*)0);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3rfhc462a0qx53yecw933hkk8"); // 		/* reinsert them */
UNSUPPORTED("ctmfjzioo5q7mzsmb6rf9mxoy"); // 		while(r)
UNSUPPORTED("9qxb0eqp3ujnnuum1bggqarjh"); // 		{	t = r->right;
UNSUPPORTED("ddltpk94i08fyy6x03ozyc7s1"); // 			if(!(type&0000002))	/* new hash value */
UNSUPPORTED("8mj2vd7idro90tjnvl6b9trnc"); // 			{	k = (char*)(disc->link < 0 ? ((Dthold_t*)(r))->obj : (void*)((char*)(r) - disc->link) );
UNSUPPORTED("1dvo2602az1wcigxx20czskv9"); // 				k = (void*)(disc->size < 0 ? *((char**)((char*)((void*)k)+disc->key)) : ((char*)((void*)k)+disc->key));
UNSUPPORTED("269t5qi8m2ujfjvmbqvyjvr1s"); // 				r->hl._hash = (disc->hashf ? (*disc->hashf)(dt,k,disc) : dtstrhash(0,k,disc->size) );
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("2e2tx3ch32oxo5y01bflgbf2h"); // 			(void)(*searchf)(dt,(void*)r,0000040);
UNSUPPORTED("8tob14cb9u9q0mnud0wovaioi"); // 			r = t;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cerydbb7i6l7c4pgeygvwoqk2"); // done:
UNSUPPORTED("bi0p581nen18ypj0ey48s6ete"); // 	return old;
 }

throw new UnsupportedOperationException();
} finally {
LEAVING("axpvuswclmi9bx3qtlh4quyah","dtdisc");
}
}


}
