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
import static smetana.core.Macro.DT_BAG;
import static smetana.core.Macro.DT_FLATTEN;
import static smetana.core.Macro.DT_OBAG;
import static smetana.core.Macro.DT_OSET;
import static smetana.core.Macro.DT_SET;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_dt_s;
import h.ST_dtlink_s;
import smetana.core.CFunction;

public class dtrestore__c {

/*	Restore dictionary from given tree or list of elements.
**	There are two cases. If called from within, list is nil.
**	From without, list is not nil and data->size must be 0.
**
**	Written by Kiem-Phong Vo (5/25/96)
*/
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cdt/dtrestore.c", name="dtrestore", key="8dfut8799tvg4tjvn2yz48d7h", definition="int dtrestore(register Dt_t* dt, register Dtlink_t* list)")
public static int dtrestore(ST_dt_s dt, ST_dtlink_s list) {
ENTERING("8dfut8799tvg4tjvn2yz48d7h","dtrestore");
try {
	ST_dtlink_s	t;
	//Dtlink_t	**s, **ends;
	int		type;
	CFunction	searchf = dt.meth.searchf;
	
	
	type = dt.data.type&DT_FLATTEN;
	if(N(list)) /* restoring a flattened dictionary */
	{	if(N(type))
			return -1;
		list = dt.data.here;
	}
	else	/* restoring an extracted list of elements */
	{	if(dt.data.size != 0)
			return -1;
		type = 0;
	}
	dt.data.type &= ~DT_FLATTEN;

	if((dt.data.type&(DT_SET|DT_BAG))!=0)
	{	//dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("4xawc48hce5sov89n8h4j7xw0"); // 		if(type) /* restoring a flattened dictionary */
UNSUPPORTED("3yrjmgus9a9415ocrrtj1e733"); // 		{	for(ends = (s = dt->data->hh._htab) + dt->data->ntab; s < ends; ++s)
UNSUPPORTED("7s6h8lg0p1wwil85e1p2msogy"); // 			{	if((t = *s) )
UNSUPPORTED("2j3e40zcb5znwl73hbk12t6va"); // 				{	*s = list;
UNSUPPORTED("ef469bao0q1pw0jwv7pmoly31"); // 					list = t->right;
UNSUPPORTED("29gliugc08l5pj89nfqoctwqi"); // 					t->right = ((Dtlink_t*)0);
UNSUPPORTED("cysnuxd51taci3hbg5lifz8ce"); // 				}
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dri3t8bbbtcxexw436q0kqnxd"); // 		else	/* restoring an extracted list of elements */
UNSUPPORTED("efiynrcqevfta9kp0bfbujmn5"); // 		{	dt->data->size = 0;
UNSUPPORTED("afqkqg6k8jxzgjyj7tb6kw1fc"); // 			while(list)
UNSUPPORTED("4rgxhjb5r159n4fh7vilshms9"); // 			{	t = list->right;
UNSUPPORTED("6fq6k0xv0ofue3nmsm3tcbmxc"); // 				(*searchf)(dt,(void*)list,0000040);
UNSUPPORTED("8dsqgg8k8zwg5ae8ltd5xs9yc"); // 				list = t;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
	}
	else
	{	if((dt.data.type&(DT_OSET|DT_OBAG))!=0)
			dt.data.here = list;
		else /*if(dt->data->type&(DT_LIST|DT_STACK|DT_QUEUE))*/
{UNSUPPORTED("79wu0m7edfdq02msgoqkzb32"); // 		{	dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("c8q10nh3f6o4rjxtacmbgjxqj"); // 			dt->data->hh._head = list;
		}
		if(N(type))
			dt.data.size = -1;
	}
	
	return 0;
} finally {
LEAVING("8dfut8799tvg4tjvn2yz48d7h","dtrestore");
}
}


}
