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
import static gen.lib.cdt.dtdisc__c.dtdisc;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import h.ST_dt_s;
import h.ST_dtdata_s;
import h.ST_dtdisc_s;
import h.ST_dtmethod_s;
import smetana.core.Memory;

public class dtopen__c {
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


//1 emxrtwweklud1b14cstc9z3bb
// static char*     Version = 




//3 34nbfbdgwe34hb4vmfr5p6kbh
// Dt_t* dtopen(Dtdisc_t* disc, Dtmethod_t* meth)      
public static ST_dt_s dtopen(ST_dtdisc_s disc, ST_dtmethod_s meth) {
ENTERING("34nbfbdgwe34hb4vmfr5p6kbh","dtopen");
try {
	ST_dt_s		dt = null;
	int		e;
	ST_dtdata_s	data;
	if(N(disc) || N(meth))
		return null;
	/* allocate space for dictionary */
	if(N(dt = new ST_dt_s()))
		return null;
//	/* initialize all absolutely private data */
	dt.setPtr("searchf", null);
	dt.setPtr("meth", null);
	dt.setPtr("disc", null);
	dtdisc(dt,disc,0);
	dt.setInt("type", 0);
	dt.setInt("nview", 0);
	dt.setPtr("view", null);
	dt.setPtr("walk", null);
	dt.setPtr("user", null);
	if(disc.eventf!=null)
	{	/* if shared/persistent dictionary, get existing data */
		throw new UnsupportedOperationException();
//		data = ((Dtdata_t*)0);
//		if((e = (*disc->eventf)(dt,1,(void*)(&data),disc)) < 0)
//			goto err_open;
//		else if(e > 0)
//		{	if(data)
//			{	if(data->type&meth->type)
//					goto done;
//				else	goto err_open;
//			}
//			if(!disc->memoryf)
//				goto err_open;
//			free((void*)dt);
//			if(!(dt = (*disc->memoryf)(0, 0, sizeof(Dt_t), disc)) )
//				return ((Dt_t*)0);
//			dt->searchf = ((Dtsearch_f)0);
//			dt->meth = ((Dtmethod_t*)0);
//			dt->disc = ((Dtdisc_t*)0);
//			dtdisc(dt,disc,0);
//			dt->type = 1;
//			dt->nview = 0;
//			dt->view = dt->walk = ((Dt_t*)0);
//		}
	}
	/* allocate sharable data */
	if(N(data = (ST_dtdata_s) (dt.memoryf.exe(dt,null,sizeof(ST_dtdata_s.class),disc)) ))
	{ err_open:
		Memory.free(dt);
		return (null);
	}
	data.type = meth.type;
	data.here = null;
	data._htab = null;
	data.ntab = 0;
	data.size = 0;
	data.loop = 0;
	data.minp = 0;
//done:
	dt.data = data;
	dt.searchf = meth.searchf;
	dt.meth = meth;
	if(disc.eventf!=null)
		throw new UnsupportedOperationException();
//		(*disc->eventf)(dt, 5, (void*)dt, disc);
	return dt;
} finally {
LEAVING("34nbfbdgwe34hb4vmfr5p6kbh","dtopen");
}
}


}
