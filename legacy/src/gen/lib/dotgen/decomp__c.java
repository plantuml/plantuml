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
package gen.lib.dotgen;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.common.utils__c.UF_find;
import static smetana.core.Macro.GD_comp;
import static smetana.core.Macro.GD_n_nodes;
import static smetana.core.Macro.GD_nlist;
import static smetana.core.Macro.GD_rankleader;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_flat_in;
import static smetana.core.Macro.ND_flat_out;
import static smetana.core.Macro.ND_in;
import static smetana.core.Macro.ND_mark;
import static smetana.core.Macro.ND_next;
import static smetana.core.Macro.ND_out;
import static smetana.core.Macro.ND_prev;
import static smetana.core.Macro.ND_rank;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_elist;
import smetana.core.CArrayOfStar;
import smetana.core.Globals;
import smetana.core.ZType;

public class decomp__c {



@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/decomp.c", name="begin_component", key="7ggrwt0f912kp1marrxdjq155", definition="static void  begin_component(void)")
public static void begin_component(Globals zz) {
ENTERING("7ggrwt0f912kp1marrxdjq155","begin_component");
try {
    GD_nlist(zz.G_decomp, null);
    zz.Last_node_decomp = null;
} finally {
LEAVING("7ggrwt0f912kp1marrxdjq155","begin_component");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/decomp.c", name="add_to_component", key="7icc6b2pvnj6te1yndbel47gg", definition="static void  add_to_component(node_t * n)")
public static void add_to_component(Globals zz, ST_Agnode_s n) {
ENTERING("7icc6b2pvnj6te1yndbel47gg","add_to_component");
try {
    GD_n_nodes(zz.G_decomp, GD_n_nodes(zz.G_decomp)+1);
    ND_mark(n, zz.Cmark);
    if (zz.Last_node_decomp!=null) {
	ND_prev(n, zz.Last_node_decomp);
	ND_next(zz.Last_node_decomp, n);
    } else {
	ND_prev(n, null);
	GD_nlist(zz.G_decomp, n);
    }
    zz.Last_node_decomp = n;
    ND_next(n, null);
} finally {
LEAVING("7icc6b2pvnj6te1yndbel47gg","add_to_component");
}
}



@Difficult
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/decomp.c", name="end_component", key="5o8hxpr6ppi15pinuy79m7u04", definition="static void  end_component(void)")
public static void end_component(Globals zz) {
ENTERING("5o8hxpr6ppi15pinuy79m7u04","end_component");
try {
    int i;
    
    i = GD_comp(zz.G_decomp).size++;    
    GD_comp(zz.G_decomp).list = CArrayOfStar.<ST_Agnode_s>REALLOC(GD_comp(zz.G_decomp).size, GD_comp(zz.G_decomp).list, ZType.ST_Agnode_s);
    GD_comp(zz.G_decomp).list.set_(i, GD_nlist(zz.G_decomp));
} finally {
LEAVING("5o8hxpr6ppi15pinuy79m7u04","end_component");
}
}



@Difficult
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/decomp.c", name="search_component", key="c5u5lnfbu0pmlk6vsvyrdj8ep", definition="static void search_component(graph_t * g, node_t * n)")
public static void search_component(Globals zz, ST_Agraph_s g, ST_Agnode_s n) {
ENTERING("c5u5lnfbu0pmlk6vsvyrdj8ep","search_component");
try {
    int c, i;
    final ST_elist vec[] = new ST_elist[] {new ST_elist(),new ST_elist(),new ST_elist(),new ST_elist()};
    ST_Agnode_s other;
    ST_Agedge_s e;
    
    add_to_component(zz, n);
    vec[0].___(ND_out(n));
    vec[1].___(ND_in(n));
    vec[2].___(ND_flat_out(n));
    vec[3].___(ND_flat_in(n));
    
    for (c = 0; c <= 3; c++) {
    	if (vec[c].list != null)
    	    for (i = 0; (e = (ST_Agedge_s) vec[c].list.get_(i))!=null; i++) {
		if ((other = aghead(e))== n)
		    other = agtail(e);
		if ((ND_mark(other) != zz.Cmark) && (other == UF_find(other)))
		    search_component(zz, g, other);
	    }
    }
} finally {
LEAVING("c5u5lnfbu0pmlk6vsvyrdj8ep","search_component");
}
}



@Difficult
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/dotgen/decomp.c", name="decompose", key="2t7r964kqtl5qrl7i57i22tqy", definition="void decompose(graph_t * g, int pass)")
public static void decompose(Globals zz, ST_Agraph_s g, int pass) {
ENTERING("2t7r964kqtl5qrl7i57i22tqy","decompose");
try {
    ST_Agraph_s subg;
    ST_Agnode_s n, v;
    
    
    zz.G_decomp = g;
    if (++zz.Cmark == 0)
	zz.Cmark = 1;
    GD_comp(g).size = 0;
    GD_n_nodes(g, 0);
    for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n)) {
	v = n;
	if ((pass > 0) && (subg = ND_clust(v))!=null)
	    v = GD_rankleader(subg).get_(ND_rank(v));
	else if (v != UF_find(v))
	    continue;
	if (ND_mark(v) != zz.Cmark) {
	    begin_component(zz);
	    search_component(zz, g, v);
	    end_component(zz);
	}
    }
} finally {
LEAVING("2t7r964kqtl5qrl7i57i22tqy","decompose");
}
}


}
