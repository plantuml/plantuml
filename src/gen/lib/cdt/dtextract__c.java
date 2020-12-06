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
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_dt_s;
import h.ST_dtlink_s;

public class dtextract__c {



/*	Extract objects of a dictionary.
**
**	Written by Kiem-Phong Vo (5/25/96).
*/
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cdt/dtextract.c", name="dtextract", key="ar89jjwhuitmbhut1o3ft3zpb", definition="Dtlink_t* dtextract(register Dt_t* dt)")
public static ST_dtlink_s dtextract(ST_dt_s dt) {
ENTERING("ar89jjwhuitmbhut1o3ft3zpb","dtextract");
try {
	ST_dtlink_s	list = null;
	ST_dtlink_s	s[], ends[];
	if((dt.data.type&(DT_OSET|DT_OBAG) )!=0)
		list = dt.data.here;
	else if((dt.data.type&(DT_SET|DT_BAG))!=0)
	{	UNSUPPORTED("list = dtflatten(dt)");
UNSUPPORTED("8i2mufw9f604gvj48u8nbdazp"); // 		for(ends = (s = dt->data->hh._htab) + dt->data->ntab; s < ends; ++s)
UNSUPPORTED("4sml4zdr938yth6x815jt1dlm"); // 			*s = ((Dtlink_t*)0);
	}
	else /*if(dt->data->type&(DT_LIST|DT_STACK|DT_QUEUE))*/
	{	list = dt.data._head;
		dt.data._head = null;
	}
	
	dt.data.type &= ~DT_FLATTEN;
	dt.data.size = 0;
	dt.data.here = null;
	return list;
} finally {
LEAVING("ar89jjwhuitmbhut1o3ft3zpb","dtextract");
}
}


}
