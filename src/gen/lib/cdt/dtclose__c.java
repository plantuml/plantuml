/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
public static Object dtclose(Object... arg) {
UNSUPPORTED("5o7j3hhxxjdvmz0k3eg98i8in"); // int dtclose(register Dt_t* dt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2fi55lg37rcjprso1i0385cj5"); // 	Dtdisc_t	*disc;
UNSUPPORTED("pz52m7579heh6vdbqc3dt84v"); // 	int		ev = 0;
UNSUPPORTED("3iyrv07qx7n0s211y8m7mopsz"); // 	if(!dt || dt->nview > 0 ) /* can't close if being viewed */
UNSUPPORTED("b0epxudfxjm8kichhaautm2qi"); // 		return -1;
UNSUPPORTED("9y2zao3al22pjv2p480p4klcc"); // 	/* announce the close event to see if we should continue */
UNSUPPORTED("70rampeezpyz05ynxa49umbrz"); // 	disc = dt->disc;
UNSUPPORTED("8gsk9kzxnlwgo3aceze9c6gl7"); // 	if(disc->eventf &&
UNSUPPORTED("ho436wfj1mjidd6obd19mhll"); // 	   (ev = (*disc->eventf)(dt,2,((void*)0),disc)) < 0)
UNSUPPORTED("b0epxudfxjm8kichhaautm2qi"); // 		return -1;
UNSUPPORTED("66fqrcqd57dw3fzb30i176w0r"); // 	if(dt->view)	/* turn off viewing */
UNSUPPORTED("1xbo7gf92fmqmu2tzpfeuc0wk"); // 		dtview(dt,((Dt_t*)0));
UNSUPPORTED("9vl9psbe52rphrxhc41erj2qd"); // 	if(ev == 0) /* release all allocated data */
UNSUPPORTED("8fubw8ssxlx99qjv7n52cf3ru"); // 	{	(void)(*(dt->meth->searchf))(dt,((void*)0),0000100);
UNSUPPORTED("5rbpcw29jwi3l66b11are2xd5"); // 		if(dtsize(dt) > 0)
UNSUPPORTED("896vcxnvc07fbkh09vojp66fv"); // 			return -1;
UNSUPPORTED("8po7fioot1issl5obmk5hqndd"); // 		if(dt->data->ntab > 0)
UNSUPPORTED("4ugmcpi8vkb013vuo4wykn0a3"); // 			(*dt->memoryf)(dt,(void*)dt->data->hh._htab,0,disc);
UNSUPPORTED("1bguaull3m0kl140obilq4ucp"); // 		(*dt->memoryf)(dt,(void*)dt->data,0,disc);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4wjw4hx0hptydzyvkohwgzoo"); // 	if(dt->type == 0)
UNSUPPORTED("9jx9g23tuicm5x7f5cvznr3rt"); // 		free((void*)dt);
UNSUPPORTED("4r6n1aai6737i36g78f9z4apm"); // 	else if(ev == 0 && dt->type == 1)
UNSUPPORTED("6wqxv1f7tzvjk0lwmqdrcq8la"); // 		(*dt->memoryf)(dt, (void*)dt, 0, disc);
UNSUPPORTED("oxrmf64nqi0a580iiczqkmky"); // 	if(disc->eventf)
UNSUPPORTED("8bce06bdd64ypwb17ddloqzu8"); // 		(void)(*disc->eventf)(dt, 6, ((void*)0), disc);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
