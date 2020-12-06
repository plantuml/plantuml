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
import static gen.lib.cgraph.attr__c.agxget;
import static gen.lib.cgraph.edge__c.agfstout;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agnxtout;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.common.utils__c.UF_find;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.dotgen.cluster__c.mark_clusters;
import static gen.lib.dotgen.fastgr__c.find_fast_edge;
import static gen.lib.dotgen.fastgr__c.merge_oneway;
import static gen.lib.dotgen.fastgr__c.virtual_edge;
import static gen.lib.dotgen.fastgr__c.virtual_node;
import static gen.lib.dotgen.position__c.make_aux_edge;
import static smetana.core.JUtils.EQ;
import static smetana.core.Macro.ED_minlen;
import static smetana.core.Macro.ED_to_orig;
import static smetana.core.Macro.ED_to_virt;
import static smetana.core.Macro.ED_weight;
import static smetana.core.Macro.GD_leader;
import static smetana.core.Macro.ND_clust;
import static smetana.core.Macro.ND_node_type;
import static smetana.core.Macro.ND_rank;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import smetana.core.CString;
import smetana.core.Z;

public class class1__c {


@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/class1.c", name="nonconstraint_edge", key="2luyof8ca7ewf9r08z3os3lk7", definition="int nonconstraint_edge(edge_t * e)")
public static boolean nonconstraint_edge(ST_Agedge_s e) {
ENTERING("2luyof8ca7ewf9r08z3os3lk7","nonconstraint_edge");
try {
    CString constr;
    
    if (Z.z().E_constr!=null && (constr = agxget(e, Z.z().E_constr))!=null) {
	if (constr.charAt(0)!='\0' && mapbool(constr) == false)
	    return true;
    }
    return false;
} finally {
LEAVING("2luyof8ca7ewf9r08z3os3lk7","nonconstraint_edge");
}
}




//3 dpimuv55sylui7jx8fh3ic1qc
// static void  interclust1(graph_t * g, node_t * t, node_t * h, edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/class1.c", name="interclust1", key="dpimuv55sylui7jx8fh3ic1qc", definition="static void  interclust1(graph_t * g, node_t * t, node_t * h, edge_t * e)")
public static void interclust1(ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h, ST_Agedge_s e) {
ENTERING("dpimuv55sylui7jx8fh3ic1qc","interclust1");
try {
    ST_Agnode_s v, t0, h0;
    int offset, t_len, h_len, t_rank, h_rank;
    ST_Agedge_s rt, rh;
    if (ND_clust(agtail(e))!=null)
	t_rank = ND_rank(agtail(e)) - ND_rank(GD_leader(ND_clust(agtail(e))));
    else
	t_rank = 0;
    if (ND_clust(aghead(e))!=null)
	h_rank = ND_rank(aghead(e)) - ND_rank(GD_leader(ND_clust(aghead(e))));
    else
	h_rank = 0;
    offset = ED_minlen(e) + t_rank - h_rank;
    if (offset > 0) {
	t_len = 0;
	h_len = offset;
    } else {
	t_len = -offset;
	h_len = 0;
    }
    v = virtual_node(g);
    ND_node_type(v, 2);
    t0 = UF_find(t);
    h0 = UF_find(h);
    rt = make_aux_edge(v, t0, t_len, 10 * ED_weight(e));
    rh = make_aux_edge(v, h0, h_len, ED_weight(e));
    ED_to_orig(rt, e);
    ED_to_orig(rh, e);
} finally {
LEAVING("dpimuv55sylui7jx8fh3ic1qc","interclust1");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/dotgen/class1.c", name="class1", key="acy5ct6402jgf0ga5oeeskx5m", definition="void class1(graph_t * g)")
public static void class1_(ST_Agraph_s g) {
ENTERING("acy5ct6402jgf0ga5oeeskx5m","class1_");
try {
    ST_Agnode_s n, t, h;
    ST_Agedge_s e, rep;
    
    mark_clusters(g);
    for (n = agfstnode(g); n!=null; n = agnxtnode(g, n)) {
	for (e = agfstout(g, n); e!=null; e = agnxtout(g, e)) {
		
	    /* skip edges already processed */
	    if (ED_to_virt(e)!=null)
		continue;
	    
	    /* skip edges that we want to ignore in this phase */
	    if (nonconstraint_edge(e))
		continue;
	    
	    t = UF_find(agtail(e));
	    
	    h = UF_find(aghead(e));
	    /* skip self, flat, and intra-cluster edges */
	    if (EQ(t, h))
		continue;
	    
	    
	    /* inter-cluster edges require special treatment */
	    if (ND_clust(t)!=null || ND_clust(h)!=null) {
		interclust1(g, agtail(e), aghead(e), e);
		continue;
	    }
	    
	    
	    if ((rep = find_fast_edge(t, h))!=null)
		merge_oneway(e, rep);
	    else
		virtual_edge(t, h, e);
	}
    }
} finally {
LEAVING("acy5ct6402jgf0ga5oeeskx5m","class1_");
}
}


}
