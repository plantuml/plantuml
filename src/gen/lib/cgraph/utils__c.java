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
package gen.lib.cgraph;
import static gen.lib.cdt.dtdisc__c.dtdisc;
import static gen.lib.cdt.dtopen__c.dtopen;
import static smetana.core.JUtils.NEQ;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.dtdelete;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agraph_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtmethod_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.Z;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class utils__c {
	
public static CFunction agdictobjmem = new CFunctionAbstract("agdictobjmem") {
	
	public Object exe(Object... args) {
		return agdictobjmem((ST_dt_s)args[0], args[1], (size_t)args[2], (ST_dtdisc_s)args[3]);
	}};
		
@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdictobjmem", key="7dkudp41c9byhicatk2sxtxqk", definition="void *agdictobjmem(Dict_t * dict, void * p, size_t size, Dtdisc_t * disc)")
public static Object agdictobjmem(ST_dt_s dict, Object p, size_t size, ST_dtdisc_s disc) {
ENTERING("7dkudp41c9byhicatk2sxtxqk","agdictobjmem");
try {
	return size.malloc();
//	ST_Agraph_s g;
//    g = Z.z().Ag_dictop_G;
//    if (g!=null) {
//	if (p!=null)
//	    agfree(g, p);
//	else
//	    return agalloc(g, size);
//    } else {
//	if (p!=null)
//	    Memory.free(p);
//	else
//	    return size.malloc();
//    }
//    return null;
} finally {
LEAVING("7dkudp41c9byhicatk2sxtxqk","agdictobjmem");
}
}



public static CFunction agdictobjfree = new CFunctionAbstract("agdictobjfree") {
	
	public Object exe(Object... args) {
		return agdictobjfree(args);
	}};
	
@Unused
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdictobjfree", key="5xdfwxth4q1dm3180qzuf51sn", definition="void agdictobjfree(Dict_t * dict, void * p, Dtdisc_t * disc)")
public static Object agdictobjfree(Object... arg_) {
UNSUPPORTED("bsemnw6m2qx5a4hk13xep80nh"); // void agdictobjfree(Dict_t * dict, void * p, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("nzl57bfl5onx7q9ge19n2k9i"); //     (void) dict;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("ah8bddgh4pyntvi4o9xx91fkm"); //     g = Ag_dictop_G;
UNSUPPORTED("5skdegxoz3mwfvm59pbtvqtiq"); //     if (g)
UNSUPPORTED("akbizhd9c9w4syt1qpirn7xjy"); // 	agfree(g, p);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("cy5x5dma0v4hiepir7lrfuo17"); // 	free(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtopen", key="48ox0bg1qmasrer8np51uwsyk", definition="Dict_t *agdtopen(Agraph_t * g, Dtdisc_t * disc, Dtmethod_t * method)")
public static ST_dt_s agdtopen(ST_Agraph_s g, ST_dtdisc_s disc, ST_dtmethod_s method) {
ENTERING("48ox0bg1qmasrer8np51uwsyk","agdtopen");
try {
	CFunction memf;
    ST_dt_s d;
    memf = disc.memoryf;
    disc.memoryf = utils__c.agdictobjmem;
    Z.z().Ag_dictop_G = g;
    d = dtopen(disc, method);
    disc.memoryf = (CFunction) memf;
    Z.z().Ag_dictop_G = null;
    return d;
} finally {
LEAVING("48ox0bg1qmasrer8np51uwsyk","agdtopen");
}
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtdelete", key="6pbz2fsmebq8iy7if4way3ct2", definition="long agdtdelete(Agraph_t * g, Dict_t * dict, void *obj)")
public static __ptr__ agdtdelete(ST_Agraph_s g, ST_dt_s dict, __ptr__ obj) {
ENTERING("6pbz2fsmebq8iy7if4way3ct2","agdtdelete");
try {
	Z.z().Ag_dictop_G = g;
	return (__ptr__) dtdelete(dict, obj);
} finally {
LEAVING("6pbz2fsmebq8iy7if4way3ct2","agdtdelete");
}
}




@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtdisc", key="cym72wvu6zffc0vzoa93zha8", definition="void agdtdisc(Agraph_t * g, Dict_t * dict, Dtdisc_t * disc)")
public static void agdtdisc(ST_Agraph_s g, ST_dt_s dict, ST_dtdisc_s disc) {
ENTERING("cym72wvu6zffc0vzoa93zha8","agdtdisc");
try {
    if (disc!=null && NEQ(dtdisc(dict, null, 0), disc)) {
	dtdisc(dict, disc, 0);
    }
    /* else unchanged, disc is same as old disc */
} finally {
LEAVING("cym72wvu6zffc0vzoa93zha8","agdtdisc");
}
}


}
