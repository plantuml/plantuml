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
import static gen.lib.cgraph.edge__c.agopp;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.refstr__c.agstrdup;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.strcmp;
import static smetana.core.Macro.AGDATA;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.ASINT;
import static smetana.core.Macro.N;
import static smetana.core.Macro.NOT;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_Agedge_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agrec_s;
import h.ST_Agtag_s;
import smetana.core.CString;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class rec__c {


@Original(version="2.38.0", path="lib/cgraph/rec.c", name="set_data", key="62z9z5vraa2as0c9t108j9xaf", definition = "static void set_data(Agobj_t * obj, Agrec_t * data, int mtflock)")
@Reviewed(when = "10/11/2020")
public static void set_data(ST_Agobj_s obj, ST_Agrec_s data, int mtflock) {
ENTERING("62z9z5vraa2as0c9t108j9xaf","set_data");
try {
    ST_Agedge_s e;
    obj.data = data;
    (obj.tag).mtflock = mtflock;
    if ((AGTYPE(obj) == AGINEDGE) || (AGTYPE(obj) == AGOUTEDGE)) {
	e = (ST_Agedge_s) agopp(obj);
	AGDATA(e, data);
	(e.base.tag).mtflock = mtflock;
    }
} finally {
LEAVING("62z9z5vraa2as0c9t108j9xaf","set_data");
}
}




@Original(version="2.38.0", path="lib/cgraph/rec.c", name="aggetrec", key="7p2ne3oknmyclvsw4lh3axtd8", definition = "Agrec_t *aggetrec(void *obj, char *name, int mtf)")
@Reviewed(when = "10/11/2020")
public static ST_Agrec_s aggetrec(__ptr__ obj, CString name, boolean mtf) {
ENTERING("7p2ne3oknmyclvsw4lh3axtd8","aggetrec");
try {
    ST_Agobj_s hdr;
    ST_Agrec_s d, first;
    hdr = (ST_Agobj_s) obj;
    first = d = (ST_Agrec_s) hdr.data;
    while (d!=null) {
	if (N(strcmp(name,d.name)))
	    break;
	d = (ST_Agrec_s) d.next;
	if (EQ(d, first)) {
	    d = null;
	    break;
	}
    }
    if (d!=null) {
	if (((ST_Agtag_s)hdr.tag).mtflock!=0) {
	    if (mtf && NEQ(hdr.data, d))
		System.err.println("move to front lock inconsistency");
	} else {
	    if (NEQ(d, first) || (mtf != ((((ST_Agtag_s)hdr.tag).mtflock)!=0)))
		set_data(hdr, d, ASINT(mtf));	/* Always optimize */
	}
    }
    return d;
} finally {
LEAVING("7p2ne3oknmyclvsw4lh3axtd8","aggetrec");
}
}




@Original(version="2.38.0", path="lib/cgraph/rec.c", name="objputrec", key="7sk4k5ipm2jnd244556g1kr6", definition = "static void objputrec(Agraph_t * g, Agobj_t * obj, void *arg)")
@Reviewed(when = "10/11/2020")
private static void objputrec(ST_Agraph_s g, ST_Agobj_s obj, ST_Agrec_s arg) {
ENTERING("7sk4k5ipm2jnd244556g1kr6","objputrec");
try {
	ST_Agrec_s firstrec, newrec;
    newrec = arg;
    firstrec = obj.data;
    if (firstrec == null)
	newrec.next = newrec;	/* 0 elts */
    else {
	if (EQ(firstrec.next, firstrec)) {
	    firstrec.next = newrec;	/* 1 elt */
	    newrec.next = firstrec;
	} else {
	    newrec.next = firstrec.next;
	    firstrec.next = newrec;
	}
    }
    if (NOT(((ST_Agtag_s)obj.tag).mtflock))
	set_data(obj, newrec, (0));
} finally {
LEAVING("7sk4k5ipm2jnd244556g1kr6","objputrec");
}
}




@Original(version="2.38.0", path="lib/cgraph/rec.c", name="agbindrec", key="dmh5i83l15mnn1pnu6f5dfv8l", definition = "void *agbindrec(void *arg_obj, char *recname, unsigned int recsize, int mtf)")
@Reviewed(when = "10/11/2020")
public static __ptr__ agbindrec(__ptr__ arg_obj, CString recname, size_t recsize, boolean mtf) {
ENTERING("dmh5i83l15mnn1pnu6f5dfv8l","agbindrec");
try {
    ST_Agraph_s g;
    ST_Agobj_s obj;
    ST_Agrec_s rec;
    obj = (ST_Agobj_s) arg_obj;
    g = agraphof(obj);
    rec = aggetrec(obj, recname, false);
    if ((rec == null && recsize.isStrictPositive())) {
    rec = (ST_Agrec_s) ((__ptr__)recsize.malloc());
	// rec = (ST_Agrec_s) ((__ptr__)agalloc(g, recsize)).castTo(ST_Agrec_s.class);
    // rec = (Agrec_s) Memory.malloc(Agrec_s.class);
	rec.name = agstrdup(g, recname);
	switch (((ST_Agtag_s)obj.tag).objtype) {
	case AGRAPH:
	    objputrec(g, obj, rec);
	    break;
	case AGNODE:
	    objputrec(g, obj, rec);
	    break;
	case AGINEDGE:
	case AGOUTEDGE:
	    objputrec(g, obj, rec);
	    break;
	}
    }
    if (mtf)
	aggetrec(arg_obj, recname, (N(0)));
    return rec;
} finally {
LEAVING("dmh5i83l15mnn1pnu6f5dfv8l","agbindrec");
}
}


}
