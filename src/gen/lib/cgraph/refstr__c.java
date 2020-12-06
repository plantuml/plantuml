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
import static gen.lib.cgraph.utils__c.agdtdelete;
import static gen.lib.cgraph.utils__c.agdtopen;
import static smetana.core.JUtils.EQ;
import static smetana.core.Macro.dtinsert;
import static smetana.core.Macro.dtsearch;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_Agraph_s;
import h.ST_dt_s;
import h.ST_refstr_t;
import smetana.core.ACCESS;
import smetana.core.CStarStar;
import smetana.core.CString;
import smetana.core.Z;

public class refstr__c {



@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="refdict", key="f1nwss2xoaub1hyord232ugoj", definition = "static Dict_t *refdict(Agraph_t * g)")
@Reviewed(when = "10/11/2020")
private static ST_dt_s refdict(final ST_Agraph_s g) {
ENTERING("f1nwss2xoaub1hyord232ugoj","refdict");
try {
		final CStarStar<ST_dt_s> dictref;
		if (g != null)
			dictref = CStarStar.<ST_dt_s> BUILD(new ACCESS<ST_dt_s>() {
				public ST_dt_s get() {
					return (ST_dt_s) g.clos.strdict;
				}
				public void set(ST_dt_s obj) {
					g.clos.strdict = obj;
				}
			});
		else
			dictref = CStarStar.<ST_dt_s> BUILD(new ACCESS<ST_dt_s>() {
				public ST_dt_s get() {
					return Z.z().Refdict_default;
				}
				public void set(ST_dt_s obj) {
					Z.z().Refdict_default = obj;
				}
			});
    if (dictref.star() == null) {
	dictref.star(agdtopen(g, Z.z().Refstrdisc, Z.z().Dttree));
	Z.z().HTML_BIT = 1 << 31;
	Z.z().CNT_BITS = ~Z.z().HTML_BIT;
	}
    return dictref.star();
} finally {
LEAVING("f1nwss2xoaub1hyord232ugoj","refdict");
}
}






@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="refsymbind", key="9ts4wqhw2xafdv3tlcilneewq", definition = "static refstr_t *refsymbind(Dict_t * strdict, char *s)")
@Reviewed(when = "10/11/2020")
private static ST_refstr_t refsymbind(ST_dt_s strdict, CString s) {
ENTERING("9ts4wqhw2xafdv3tlcilneewq","refsymbind");
try {
    final ST_refstr_t key = new ST_refstr_t();
    ST_refstr_t r;
    // key.s = s.duplicate());
    key.setString(s);
    r = (ST_refstr_t) dtsearch(strdict, key);
    return r;
} finally {
LEAVING("9ts4wqhw2xafdv3tlcilneewq","refsymbind");
}
}





@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="refstrbind", key="1scntgo71z7c2v15zapiyw59w", definition = "static char *refstrbind(Dict_t * strdict, char *s)")
private static CString refstrbind(ST_dt_s strdict, CString s) {
ENTERING("1scntgo71z7c2v15zapiyw59w","refstrbind");
try {
    ST_refstr_t r;
    r = refsymbind(strdict, s);
    if (r!=null)
	return r.s;
    else
	return null;
} finally {
LEAVING("1scntgo71z7c2v15zapiyw59w","refstrbind");
}
}




@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="agstrbind", key="bb8aqjshw3ecae2lsmhigd0mc", definition = "char *agstrbind(Agraph_t * g, char *s)")
public static CString agstrbind(ST_Agraph_s g, CString s) {
ENTERING("bb8aqjshw3ecae2lsmhigd0mc","agstrbind");
try {
    return refstrbind(refdict(g), s);
} finally {
LEAVING("bb8aqjshw3ecae2lsmhigd0mc","agstrbind");
}
}




@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="agstrdup", key="86oznromwhn9qeym0k7pih73q", definition = "char *agstrdup(Agraph_t * g, char *s)")
@Reviewed(when = "10/11/2020")
public static CString agstrdup(ST_Agraph_s g, CString s) {
ENTERING("86oznromwhn9qeym0k7pih73q","agstrdup");
try {
	ST_refstr_t r;
    ST_dt_s strdict;
    // size_t sz;
    
    if (s == null)
	 return null;
    strdict = refdict(g);
    r = (ST_refstr_t) refsymbind(strdict, s);
    if (r!=null)
	r.refcnt++;
    else {
//	sz = sizeof(ST_refstr_t.class).plus(s.length());
//	if (g!=null)
//	    r = (ST_refstr_t) agalloc(g, sz);
//	else
	    r = new ST_refstr_t();
	r.refcnt = 1;
	r.setString(s.duplicate());
//	strcpy(r->store, s);
//	r->s = r->store;
	dtinsert(strdict, r);
    }
	return r.s;
} finally {
LEAVING("86oznromwhn9qeym0k7pih73q","agstrdup");
}
}



@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="agstrfree", key="enhn1ajfo86a19dgm4b8lduz7", definition = "int agstrfree(Agraph_t * g, char *s)")
public static int agstrfree(ST_Agraph_s g, CString s) {
ENTERING("enhn1ajfo86a19dgm4b8lduz7","agstrfree");
try {
    ST_refstr_t r;
    ST_dt_s strdict;
    if (s == null)
	 return -1;
    strdict = refdict(g);
    r = (ST_refstr_t) refsymbind(strdict, s);
    if (r!=null && (EQ(r.s, s))) {
	r.refcnt--;
	if ((r.refcnt!=0 && Z.z().CNT_BITS!=0) == false) {
	    agdtdelete(g, strdict, r);
	    /*
	       if (g) agfree(g,r);
	       else free(r);
	     */
	}
    }
    if (r == null)
	return -1;
    return 0;
} finally {
LEAVING("enhn1ajfo86a19dgm4b8lduz7","agstrfree");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/refstr.c", name="aghtmlstr", key="3em4wzjnpajd5d3igb90l3rml", definition = "int aghtmlstr(char *s)")
public static int aghtmlstr(CString s) {
ENTERING("3em4wzjnpajd5d3igb90l3rml","aghtmlstr");
try {
    ST_refstr_t key;
    if (s == null)
	return 0;
	key = (ST_refstr_t) s.getParent();
    return (key.refcnt & Z.z().HTML_BIT);
} finally {
LEAVING("3em4wzjnpajd5d3igb90l3rml","aghtmlstr");
}
}



}
