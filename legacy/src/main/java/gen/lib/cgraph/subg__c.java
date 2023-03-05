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
package gen.lib.cgraph;
import static gen.lib.cgraph.graph__c.agopen1;
import static gen.lib.cgraph.id__c.agmapnametoid;
import static gen.lib.cgraph.id__c.agregister;
import static gen.lib.cgraph.mem__c.agalloc;
import static gen.lib.cgraph.utils__c.agdtdisc;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.dtfirst;
import static smetana.core.Macro.dtnext;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agdesc_s;
import h.ST_Agraph_s;
import h.ST_dt_s;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.ZType;
import smetana.core.size_t;
import smetana.core.debug.SmetanaDebug;

public class subg__c {

//3 11ezyrsjsotjz9b3cyvb4ie8p
// static Agraph_t *agfindsubg_by_id(Agraph_t * g, unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/subg.c", name="", key="11ezyrsjsotjz9b3cyvb4ie8p", definition="static Agraph_t *agfindsubg_by_id(Agraph_t * g, unsigned long id)")
public static ST_Agraph_s agfindsubg_by_id(Globals zz, ST_Agraph_s g, int id) {
ENTERING("11ezyrsjsotjz9b3cyvb4ie8p","agfindsubg_by_id");
try {
    final ST_Agraph_s template = new ST_Agraph_s();
    agdtdisc(zz, g, (ST_dt_s) g.g_dict, zz.Ag_subgraph_id_disc);
    AGID(template, id);
    return (ST_Agraph_s) g.g_dict.searchf.exe(zz, g.g_dict, template, 0000004);
} finally {
LEAVING("11ezyrsjsotjz9b3cyvb4ie8p","agfindsubg_by_id");
}
}




//3 44saycxbfbr9lou0itlyewkb4
// static Agraph_t *localsubg(Agraph_t * g, unsigned long id) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/subg.c", name="", key="44saycxbfbr9lou0itlyewkb4", definition="static Agraph_t *localsubg(Agraph_t * g, unsigned long id)")
public static ST_Agraph_s localsubg(Globals zz, ST_Agraph_s g, int id) {
ENTERING("44saycxbfbr9lou0itlyewkb4","localsubg");
try {
    ST_Agraph_s subg;
    subg = agfindsubg_by_id(zz, g, id);
    if (subg!=null)
	return subg;
    subg = (ST_Agraph_s) agalloc(g, new size_t(ZType.ST_Agraph_s));
    subg.clos = g.clos;
    subg.desc.___(g.desc);
    ((ST_Agdesc_s)subg.desc).maingraph = 0;
    subg.parent = g;
    subg.root = g.root;
    AGID(subg, id);
    return agopen1(zz, subg);
} finally {
LEAVING("44saycxbfbr9lou0itlyewkb4","localsubg");
}
}




//3 a24jd4r2sdyb4lb2hyababrda
// Agraph_t *agsubg(Agraph_t * g, char *name, int cflag) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/subg.c", name="", key="a24jd4r2sdyb4lb2hyababrda", definition="Agraph_t *agsubg(Agraph_t * g, char *name, int cflag)")
public static ST_Agraph_s agsubg(Globals zz, ST_Agraph_s g, CString name, boolean cflag) {
ENTERING("a24jd4r2sdyb4lb2hyababrda","agsubg");
try {
    final int id[] = new int[]{0};
    SmetanaDebug.LOG("agsubg g=" + g + " name=" + name);
    ST_Agraph_s subg;
    if (name!=null && agmapnametoid(zz, g, AGRAPH, name, id, false)!=0) {
    SmetanaDebug.LOG("might already exist");
	/* might already exist */
	if ((subg = agfindsubg_by_id(zz, g, id[0]))!=null) {
	    SmetanaDebug.LOG("yes returning "+subg);
	    return subg;}
    }
    if (cflag && agmapnametoid(zz, g, AGRAPH, name, id, true)!=0) {	/* reserve id */
	subg = localsubg(zz, g, id[0]);
	agregister(zz, g, AGRAPH, subg);
	SmetanaDebug.LOG("reserve id "+subg);
	return subg;
    }
    SmetanaDebug.LOG("return null");
    return null;
} finally {
LEAVING("a24jd4r2sdyb4lb2hyababrda","agsubg");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/subg.c", name="", key="51eksrs0lhkgohunejlpwyc4k", definition="Agraph_t *agfstsubg(Agraph_t * g)")
public static ST_Agraph_s agfstsubg(Globals zz, ST_Agraph_s g) {
ENTERING("51eksrs0lhkgohunejlpwyc4k","agfstsubg");
try {
	return (ST_Agraph_s) dtfirst(zz, g.g_dict);
} finally {
LEAVING("51eksrs0lhkgohunejlpwyc4k","agfstsubg");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/subg.c", name="", key="85c1qisrein0tzm2regoe61t", definition="Agraph_t *agnxtsubg(Agraph_t * subg)")
public static ST_Agraph_s agnxtsubg(Globals zz, ST_Agraph_s subg) {
ENTERING("85c1qisrein0tzm2regoe61t","agnxtsubg");
try {
    ST_Agraph_s g;
    
    g = agparent(subg);
    return g!=null? (ST_Agraph_s) dtnext(zz, g.g_dict, subg) : null;
} finally {
LEAVING("85c1qisrein0tzm2regoe61t","agnxtsubg");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/subg.c", name="", key="7kbp6j03hd7u6nnlivi0vt3ja", definition="Agraph_t *agparent(Agraph_t * g)")
public static ST_Agraph_s agparent(ST_Agraph_s g) {
ENTERING("7kbp6j03hd7u6nnlivi0vt3ja","agparent");
try {
	return g.parent;
} finally {
LEAVING("7kbp6j03hd7u6nnlivi0vt3ja","agparent");
}
}



}
