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
import static gen.lib.cdt.dtsize__c.dtsize_;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import smetana.core.Memory;

public class dtclose__c {
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




//3 7ggbhlblmrfr1wq1k20npwyxb
// int dtclose(register Dt_t* dt)     
public static int dtclose(ST_dt_s dt) {
ENTERING("7ggbhlblmrfr1wq1k20npwyxb","dtclose");
	try {
 	ST_dtdisc_s	disc;
 	int		ev = 0;
 	if(N(dt) || dt.nview > 0 ) /* can't close if being viewed */
 		return -1;
 	/* announce the close event to see if we should continue */
 	disc = (ST_dtdisc_s) dt.disc;
 	if(disc.eventf!=null &&
 	   (ev = (Integer)disc.eventf.exe(dt, 2, null, disc)) < 0)
 		return -1;
 	if(dt.view!=null)	/* turn off viewing */
UNSUPPORTED("1xbo7gf92fmqmu2tzpfeuc0wk"); // 		dtview(dt,((Dt_t*)0));
 	if(ev == 0) /* release all allocated data */ {
 		dt.meth.searchf.exe(dt, null, 0000100);
 		if(dtsize_(dt) > 0)
 			return -1;
if(dt.data.ntab > 0)
UNSUPPORTED("4ugmcpi8vkb013vuo4wykn0a3"); // 			(*dt->memoryf)(dt,(void*)dt->data->hh._htab,0,disc);
dt.memoryf.exe(dt, dt.data, null, disc);
 	}
 	if(dt.type == 0)
 		Memory.free(dt);
 	else if(ev == 0 && dt.type == 1)
UNSUPPORTED("6wqxv1f7tzvjk0lwmqdrcq8la"); // 		(*dt->memoryf)(dt, (void*)dt, 0, disc);
 	if(disc.eventf!=null)
UNSUPPORTED("8bce06bdd64ypwb17ddloqzu8"); // 		(void)(*disc->eventf)(dt, 6, ((void*)0), disc);
 	return 0;
} finally {
LEAVING("7ggbhlblmrfr1wq1k20npwyxb","dtclose");
}
}


}
