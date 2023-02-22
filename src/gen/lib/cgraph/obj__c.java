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
import static gen.lib.cgraph.edge__c.agsubedge;
import static gen.lib.cgraph.node__c.agidnode;
import static gen.lib.cgraph.pend__c.agrecord_callback;
import static smetana.core.Macro.AGINEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGOUTEDGE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.CB_UPDATE;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agcbstack_s;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agsym_s;
import smetana.core.Globals;
import smetana.core.__ptr__;

public class obj__c {

@Unused
@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agdelete", key="6wm1l0y857iajfoa6ywpotkld", definition = "int agdelete(Agraph_t * g, void *obj)")
public static Object agdelete(Object... arg_) {
UNSUPPORTED("26js2ch8px4mwz3gqvjehanq1"); // int agdelete(Agraph_t * g, void *obj)
throw new UnsupportedOperationException();
}






@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agmethod_init", key="c4ft3rxx9au29a2ns2nhod4dn", definition = "void agmethod_init(Agraph_t * g, void *obj)")
public static void agmethod_init(ST_Agraph_s g, __ptr__ obj) {
ENTERING("c4ft3rxx9au29a2ns2nhod4dn","agmethod_init");
try {
    if (g.clos.callbacks_enabled)
	aginitcb(g, obj, g.clos.cb);
    else
	agrecord_callback(g, obj, 100, null);
} finally {
LEAVING("c4ft3rxx9au29a2ns2nhod4dn","agmethod_init");
}
}




@Original(version="2.38.0", path="lib/cgraph/obj.c", name="aginitcb", key="eobcsheti70b9gzoi3z968zev", definition = "void aginitcb(Agraph_t * g, void *obj, Agcbstack_t * cbstack)")
public static void aginitcb(ST_Agraph_s g, __ptr__ obj, ST_Agcbstack_s cbstack) {
ENTERING("eobcsheti70b9gzoi3z968zev","aginitcb");
try {
    __ptr__ fn;
    if (cbstack == null)
	return;
UNSUPPORTED("cv6tr3wc0y2e3s7hrj040fbgz"); //     aginitcb(g, obj, cbstack->prev);
UNSUPPORTED("ugu810574xlbrchajuiqvlbj"); //     fn = ((agobjfn_t)0);
UNSUPPORTED("afk5q8b9fd4sednpczh6r1eg9"); //     switch (((((Agobj_t*)(obj))->tag).objtype)) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("33rj0o79bxra63omicn33shh1"); // 	fn = cbstack->f->graph.ins;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("43opp5hvwaad6urofp737fx95"); // 	fn = cbstack->f->node.ins;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("7k5xv2n0vdcq7e1h7c511n2vt"); // 	fn = cbstack->f->edge.ins;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b9a2u7luojz68ys0qfhdssxc5"); //     if (fn)
UNSUPPORTED("d6swsvu9o2h2ajgawq3fidg54"); // 	fn(g, obj, cbstack->state);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("eobcsheti70b9gzoi3z968zev","aginitcb");
}
}


@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agmethod_upd", key="29p743rx2pw81slkoaayfeael", definition = "void agmethod_upd(Agraph_t * g, void *obj, Agsym_t * sym)")
public static void agmethod_upd(ST_Agraph_s g, __ptr__ obj, ST_Agsym_s sym) {
ENTERING("29p743rx2pw81slkoaayfeael","agmethod_upd");
try {
    if (g.clos.callbacks_enabled)
	agupdcb(g, obj, sym, g.clos.cb);
    else
	agrecord_callback(g, obj, CB_UPDATE, sym);
} finally {
LEAVING("29p743rx2pw81slkoaayfeael","agmethod_upd");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agupdcb", key="8t9rkcpdvmxph6krjvfmz3s51", definition="void agupdcb(Agraph_t * g, void *obj, Agsym_t * sym, Agcbstack_t * cbstack)")
public static void agupdcb(ST_Agraph_s g, __ptr__ obj, ST_Agsym_s sym, ST_Agcbstack_s cbstack) {
ENTERING("8t9rkcpdvmxph6krjvfmz3s51","agupdcb");
try {
    __ptr__ fn;
    if (cbstack == null)
	return;
UNSUPPORTED("7xps60r7235mbe5tshsk48gqu"); //     agupdcb(g, obj, sym, cbstack->prev);
UNSUPPORTED("coxarw2y9j5pc184wun1hzqh4"); //     fn = ((agobjupdfn_t)0);
UNSUPPORTED("afk5q8b9fd4sednpczh6r1eg9"); //     switch (((((Agobj_t*)(obj))->tag).objtype)) {
UNSUPPORTED("70xjc0sbkjvexfar5luzibcgf"); //     case 0:
UNSUPPORTED("edztg04z181ml2fb23vg86p4"); // 	fn = cbstack->f->graph.mod;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("d0gk15gzj4wz8nv54zbr285hm"); //     case 1:
UNSUPPORTED("dt0aez1qarpjppkqak7liv45r"); // 	fn = cbstack->f->node.mod;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4u5xz2u3urj13y0aw30fdyup5"); //     case 2:
UNSUPPORTED("dw5h49n9x5t8rmlgpk6lvanem"); // 	fn = cbstack->f->edge.mod;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b9a2u7luojz68ys0qfhdssxc5"); //     if (fn)
UNSUPPORTED("ecckhw6badvki2tacvj1ch4bu"); // 	fn(g, obj, cbstack->state, sym);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("8t9rkcpdvmxph6krjvfmz3s51","agupdcb");
}
}





@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agroot", key="53858x47ifwq7ldf9ukvpdc5r", definition = "Agraph_t *agroot(void* obj)")
public static ST_Agraph_s agroot(ST_Agobj_s obj) {
ENTERING("53858x47ifwq7ldf9ukvpdc5r","agroot");
try {
    switch (obj.tag.objtype) {
    case AGINEDGE:
    case AGOUTEDGE:
	return (ST_Agraph_s) ((ST_Agedge_s)obj).node.root;
    case AGNODE:
	return (ST_Agraph_s) ((ST_Agnode_s)obj).root;
    case AGRAPH:
	return (ST_Agraph_s) ((ST_Agraph_s)obj).root;
    default:			/* actually can't occur if only 2 bit tags */
	throw new UnsupportedOperationException("agroot of a bad object");
    }
} finally {
LEAVING("53858x47ifwq7ldf9ukvpdc5r","agroot");
}
}




@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agraphof", key="brxx6qho8cw09dg7o27lc7c6z", definition = "Agraph_t *agraphof(void *obj)")
@Reviewed(when = "10/11/2020")
public static ST_Agraph_s agraphof(ST_Agobj_s obj) {
ENTERING("brxx6qho8cw09dg7o27lc7c6z","agraphof");
try {
    switch (obj.tag.objtype) {
    case AGINEDGE:
    case AGOUTEDGE:
    return (ST_Agraph_s) ((ST_Agedge_s)obj).node.root;
    case AGNODE:
    return (ST_Agraph_s) ((ST_Agnode_s)obj).root;
    case AGRAPH:
	return (ST_Agraph_s) obj;
    default:			/* actually can't occur if only 2 bit tags */
	System.err.println("agraphof a bad object");
	return null;
    }
} finally {
LEAVING("brxx6qho8cw09dg7o27lc7c6z","agraphof");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agcontains", key="91ej8cxcc0kzgkg2yk3pdiifs", definition = "int agcontains(Agraph_t* g, void* obj)")
public static boolean agcontains(Globals zz, ST_Agraph_s g, ST_Agobj_s obj) {
ENTERING("91ej8cxcc0kzgkg2yk3pdiifs","agcontains");
try {
    ST_Agraph_s subg;
    if ((agroot(g) != agroot(obj))) return false;
    switch (obj.tag.objtype) {
    case AGRAPH:
UNSUPPORTED("5fyr1r26q15uog4pl9eo2iohc"); // 	subg = (Agraph_t *) obj;
UNSUPPORTED("8vxyvy38lzpbd83cu26nejaan"); // 	do {
UNSUPPORTED("dqlpdwxfm3o0e4atzaam04f9m"); // 	    if (subg == g) return 1;
UNSUPPORTED("4oqg7vqjjx3n3761fp7f2xld9"); // 	} while ((subg = agparent (subg)));
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
    case AGNODE: 
        return (agidnode(zz, g, obj.tag.id, 0) != null);
    default:
        return (agsubedge(zz, g, (ST_Agedge_s) obj, false) != null);
    }
} finally {
LEAVING("91ej8cxcc0kzgkg2yk3pdiifs","agcontains");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/obj.c", name="agobjkind", key="bbe1e9wqmcr8dz9pswpxff0fr", definition = "int agobjkind(void *arg)")
public static int agobjkind(ST_Agobj_s arg) {
ENTERING("bbe1e9wqmcr8dz9pswpxff0fr","agobjkind");
try {
	return arg.tag.objtype;
} finally {
LEAVING("bbe1e9wqmcr8dz9pswpxff0fr","agobjkind");
}
}


}
