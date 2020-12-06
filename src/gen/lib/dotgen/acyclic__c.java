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
package gen.lib.dotgen;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.dotgen.fastgr__c.delete_fast_edge;
import static gen.lib.dotgen.fastgr__c.find_fast_edge;
import static gen.lib.dotgen.fastgr__c.merge_oneway;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_onstack;
import static smetana.core.Macro.ND_out;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;

public class acyclic__c {


//3 9hm902ya6q6bq246ewuh67h38
// void reverse_edge(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/acyclic.c", name="reverse_edge", key="9hm902ya6q6bq246ewuh67h38", definition="void reverse_edge(edge_t * e)")
public static void reverse_edge(ST_Agedge_s e) {
ENTERING("9hm902ya6q6bq246ewuh67h38","reverse_edge");
try {
    ST_Agedge_s f;
    delete_fast_edge(e);
    if ((f = find_fast_edge(aghead(e), agtail(e)))!=null)
	merge_oneway(e, f);
    else
	virtual_edge(aghead(e), agtail(e), e);
} finally {
LEAVING("9hm902ya6q6bq246ewuh67h38","reverse_edge");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/acyclic.c", name="dfs", key="e9h7n52fs8rucrug9tr0zebe2", definition="static void  dfs(node_t * n)")
public static void dfs(ST_Agnode_s n) {
ENTERING("e9h7n52fs8rucrug9tr0zebe2","dfs");
try {
    int i;
    ST_Agedge_s e;
    ST_Agnode_s w;
    
    if (ND_mark(n)!=0)
	return;
    ND_mark(n, 1);
    ND_onstack(n, 1);
    for (i = 0; (e = (ST_Agedge_s) ND_out(n).list.get_(i))!=null; i++) {
	w = aghead(e);
	if (ND_onstack(w)) {
	    reverse_edge(e);
	    i--;
	} else {
	    if (ND_mark(w) == 0)
		dfs(w);
	}
    }
    ND_onstack(n, 0);
} finally {
LEAVING("e9h7n52fs8rucrug9tr0zebe2","dfs");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/acyclic.c", name="acyclic", key="1ejgnwd7ek344caegjwg46n6h", definition="void acyclic(graph_t * g)")
public static void acyclic_(ST_Agraph_s g) {
ENTERING("1ejgnwd7ek344caegjwg46n6h","acyclic_");
try {
    int c;
    ST_Agnode_s n;
    
    for (c = 0; c < GD_comp(g).size; c++) {
    	GD_nlist(g, GD_comp(g).list.get_(c));
	for (n = GD_nlist(g); n!=null; n = ND_next(n))
	    ND_mark(n, 0);
	for (n = GD_nlist(g); n!=null; n = ND_next(n))
	    dfs(n);
    }
} finally {
LEAVING("1ejgnwd7ek344caegjwg46n6h","acyclic_");
}
}


}
