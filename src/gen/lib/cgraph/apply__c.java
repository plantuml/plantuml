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
import static gen.lib.cgraph.subg__c.agfstsubg;
import static gen.lib.cgraph.subg__c.agnxtsubg;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.__ptr__;

public class apply__c {




public static CFunction subgraph_search = new CFunctionAbstract("subgraph_search") {
	
	public Object exe(Object... args) {
		return subgraph_search((ST_Agraph_s)args[0], (ST_Agobj_s)args[1]);
	}};
@Unused
@Original(version="2.38.0", path="lib/cgraph/apply.c", name="", key="95y4aknoddh42lieikrb72vxw", definition="static Agobj_t *subgraph_search(Agraph_t * sub, Agobj_t * g)")
public static ST_Agobj_s subgraph_search(ST_Agraph_s sub, ST_Agobj_s g) {
ENTERING("95y4aknoddh42lieikrb72vxw","subgraph_search");
try {
    return (ST_Agobj_s) sub.base;
} finally {
LEAVING("95y4aknoddh42lieikrb72vxw","subgraph_search");
}
}




//3 8s9l15wqucf1glmbeb6fmya8e
// static void rec_apply(Agraph_t * g, Agobj_t * obj, agobjfn_t fn, void *arg, 		      agobjsearchfn_t objsearch, int preorder) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/apply.c", name="rec_apply", key="8s9l15wqucf1glmbeb6fmya8e", definition="static void rec_apply(Agraph_t * g, Agobj_t * obj, agobjfn_t fn, void *arg, 		      agobjsearchfn_t objsearch, int preorder)")
public static void rec_apply(ST_Agraph_s g, ST_Agobj_s obj, CFunction fn, __ptr__ arg, CFunction objsearch, boolean preorder) {
ENTERING("8s9l15wqucf1glmbeb6fmya8e","rec_apply");
try {
    ST_Agraph_s sub;
    ST_Agobj_s subobj;
    if (preorder)
	fn.exe(g, obj, arg);
    for (sub = agfstsubg(g); sub!=null; sub = agnxtsubg(sub)) {
    if ((subobj = (ST_Agobj_s) objsearch.exe(sub, obj))!=null)
	    rec_apply(sub, subobj, fn, arg, objsearch, preorder);
    }
    if (N(preorder))
	fn.exe(g, obj, arg);
} finally {
LEAVING("8s9l15wqucf1glmbeb6fmya8e","rec_apply");
}
}




//3 9hqql178zpl8iudlf6sgnv7aj
// int agapply(Agraph_t * g, Agobj_t * obj, agobjfn_t fn, void *arg, 	    int preorder) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/apply.c", name="agapply", key="9hqql178zpl8iudlf6sgnv7aj", definition="int agapply(Agraph_t * g, Agobj_t * obj, agobjfn_t fn, void *arg, 	    int preorder)")
public static int agapply(ST_Agraph_s g, ST_Agobj_s obj, CFunction fn, __ptr__ arg, boolean preorder) {
ENTERING("9hqql178zpl8iudlf6sgnv7aj","agapply");
try {
	ST_Agobj_s subobj;
    CFunction objsearch=null;
    switch (AGTYPE(obj)) {
    case AGRAPH:
 	objsearch = apply__c.subgraph_search;
	break;
    case AGNODE:
UNSUPPORTED("arkoj4niyfqe213zut6szzeji"); // 	objsearch = subnode_search;
	break;
    case AGOUTEDGE:
    case AGINEDGE:
UNSUPPORTED("3h8kzrrsobdp839772gupdrbf"); // 	objsearch = subedge_search;
	break;
    default:
UNSUPPORTED("2pc67byzirrkhe1cmdoguh6i1"); // 	agerr(AGERR, "agapply: unknown object type %d\n", AGTYPE(obj));
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
	break;
    }
    if ((subobj = (ST_Agobj_s) objsearch.exe(g, obj))!=null) {
	rec_apply(g, subobj, fn, arg, objsearch, preorder);
	return 0;
    } else
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("9hqql178zpl8iudlf6sgnv7aj","agapply");
}
}


}
