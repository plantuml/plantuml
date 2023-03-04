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
import static gen.lib.cgraph.attr__c.agattr;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import smetana.core.CString;
import smetana.core.Globals;

public class sameport__c {




//3 eu2yvovb9xx4rzic3gllij2bv
// void dot_sameports(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/sameport.c", name="dot_sameports", key="eu2yvovb9xx4rzic3gllij2bv", definition="void dot_sameports(graph_t * g)")
public static void dot_sameports(Globals zz, ST_Agraph_s g) {
ENTERING("eu2yvovb9xx4rzic3gllij2bv","dot_sameports");
try {
    ST_Agnode_s n;
    ST_Agedge_s e;
    CString id;
    //same_t samehead[5];
    //same_t sametail[5];
    int n_samehead;		/* number of same_t groups on current node */
    int n_sametail;		/* number of same_t groups on current node */
    int i;
    zz.E_samehead = agattr(zz, g, AGEDGE, new CString("samehead"),null);
    zz.E_sametail = agattr(zz, g, AGEDGE, new CString("sametail"),null);
    if (!(zz.E_samehead!=null || zz.E_sametail!=null))
	return;
UNSUPPORTED("44thr6ep72jsj3fksjiwdx3yr"); //     for (n = agfstnode(g); n; n = agnxtnode(g, n)) {
UNSUPPORTED("4roxmr5lxkjz6gn1j9mndurq2"); // 	n_samehead = n_sametail = 0;
UNSUPPORTED("8oxob1qbbkbjh0jjcogk42jfl"); // 	for (e = agfstedge(g, n); e; e = agnxtedge(g, e, n)) {
UNSUPPORTED("4gy7rakqurxvound05crezka2"); // 	    if (aghead(e) == agtail(e)) continue;  /* Don't support same* for loops */
UNSUPPORTED("2r5fkddp1ey0fvpok2scgkk99"); // 	    if (aghead(e) == n && E_samehead &&
UNSUPPORTED("d38ofiemhq37ykyauh9wync84"); // 	        (id = agxget(e, E_samehead))[0])
UNSUPPORTED("18y7dy98psh7ultlx0jugsfu2"); // 		n_samehead = sameedge(samehead, n_samehead, n, e, id);
UNSUPPORTED("5snv0fee5roi91irdwv8x51xi"); // 	    else if (agtail(e) == n && E_sametail &&
UNSUPPORTED("8xvjbvzldkn1yksprzfexgsjs"); // 	        (id = agxget(e, E_sametail))[0])
UNSUPPORTED("cdn1fgq1pke9ekyar2b4r6e91"); // 		n_sametail = sameedge(sametail, n_sametail, n, e, id);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c96gtgvp5uw8ktp389l2s9l4u"); // 	for (i = 0; i < n_samehead; i++) {
UNSUPPORTED("b2s9feywib1q9pxw4h31yz6dk"); // 	    if (samehead[i].l.size > 1)
UNSUPPORTED("1xvsmwfz2hihjki8tsqiaa1g8"); // 		sameport(n, &samehead[i].l, samehead[i].arr_len);
UNSUPPORTED("cpzvkkchr60qet357b9gg1e5q"); // 	    free_list(samehead[i].l);
UNSUPPORTED("6cnsuklvjftdyhzat6za4qggi"); // 	    /* I sure hope I don't need to free the char* id */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9ktz8j6cseb5w7jkfhrj5r19t"); // 	for (i = 0; i < n_sametail; i++) {
UNSUPPORTED("5vcqm1irqrqcg4f6ldrqkikq8"); // 	    if (sametail[i].l.size > 1)
UNSUPPORTED("dcjzqns8bm1o766uqi8dy72qf"); // 		sameport(n, &sametail[i].l, sametail[i].arr_len);
UNSUPPORTED("13a7frulpyheo0h4ajbfja7ph"); // 	    free_list(sametail[i].l);
UNSUPPORTED("6cnsuklvjftdyhzat6za4qggi"); // 	    /* I sure hope I don't need to free the char* id */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("eu2yvovb9xx4rzic3gllij2bv","dot_sameports");
}
}




}
