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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agdisc_s;
import h.ST_Agraph_s;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class mem__c {


@Original(version="2.38.0", path="lib/cgraph/mem.c", name="memopen", key="akq0jgwdspf75ypeatgcnfn8w", definition="static void *memopen(Agdisc_t* disc)")
public static Object memopen(ST_Agdisc_s disc) {
ENTERING("akq0jgwdspf75ypeatgcnfn8w","memopen");
try {
	return null;
} finally {
LEAVING("akq0jgwdspf75ypeatgcnfn8w","memopen");
}
}




@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="memalloc", key="9mtjrx0vjzwuecjwpxylr9tag", definition="static void *memalloc(void *heap, size_t request)")
public static __ptr__ memalloc(__ptr__ heap, size_t request) {
ENTERING("9mtjrx0vjzwuecjwpxylr9tag","memalloc");
try {
    __ptr__ rv;
    rv = (__ptr__) request.malloc();
    return rv;
} finally {
LEAVING("9mtjrx0vjzwuecjwpxylr9tag","memalloc");
}
}




@Original(version="2.38.0", path="lib/cgraph/mem.c", name="memresize", key="18v2hhjculhnb3b7fc4tx3yjw", definition="static void *memresize(void *heap, void *ptr, size_t oldsize, 		       size_t request)")
public static __ptr__ memresize(__ptr__ heap, __ptr__ ptr, size_t oldsize, size_t request) {
ENTERING("18v2hhjculhnb3b7fc4tx3yjw","memresize");
try {
	request.realloc(ptr);
	return ptr;
} finally {
LEAVING("18v2hhjculhnb3b7fc4tx3yjw","memresize");
}
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="memfree", key="c320bstcg5nctel3onh2pserl", definition="static void memfree(void *heap, void *ptr)")
public static Object memfree(Object... arg) {
UNSUPPORTED("5yxdf2sc5xnic9d5j24m0a7yf"); // static void memfree(void *heap, void *ptr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("74rq74mh7lnfr9i3qmwsbx2hd"); //     (void) heap;
UNSUPPORTED("f0evk2zajcoprskea22bm18e8"); //     free(ptr);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}



@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="agalloc", key="7newv1hmzvt4vtttc9cxdxfpn", definition="void *agalloc(Agraph_t * g, size_t size)")
public static __ptr__ agalloc(ST_Agraph_s g, size_t size) {
ENTERING("7newv1hmzvt4vtttc9cxdxfpn","agalloc");
try {
	return (__ptr__) size.malloc();
//	__ptr__ mem;
//    mem =  (__ptr__) g.clos.disc.mem.alloc.exe(g.clos.state.mem, size);
//    if (mem == null)
//	 System.err.println("memory allocation failure");
//    return mem;
} finally {
LEAVING("7newv1hmzvt4vtttc9cxdxfpn","agalloc");
}
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="agrealloc", key="55lm0cse6lsgqblx6puxpjs3j", definition="void *agrealloc(Agraph_t * g, void *ptr, size_t oldsize, size_t size)")
public static Object agrealloc(Object... arg) {
UNSUPPORTED("910gd4otiivsz2zpsiwlsy00v"); // void *agrealloc(Agraph_t * g, void *ptr, size_t oldsize, size_t size)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("11l1m9u5ne2xf2nff6278od59"); //     void *mem;
UNSUPPORTED("b9ag6d7eml860kbycrkuz14b7"); //     if (size > 0) {
UNSUPPORTED("zjrd9sttelcubi228vbizqq0"); // 	if (ptr == 0)
UNSUPPORTED("vr97hnk6c4k8muqake3c3c46"); // 	    mem = agalloc(g, size);
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2n0yfzx569kr1oinsronhmsus"); // 	    mem =
UNSUPPORTED("agphdu4vmb8hu0s57ry4i4axp"); // 		((g)->clos->disc.mem)->resize(((g)->clos->state.mem), ptr, oldsize, size);
UNSUPPORTED("60qvwgrubred6pojjs425ctzr"); // 	if (mem == ((void *)0))
UNSUPPORTED("9vomh5w83j5mf3src00h8g8g0"); // 	     agerr(AGERR,"memory re-allocation failure");
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("6jbj3fx0j7m0vyvwn0za7bxle"); // 	mem = ((void *)0);
UNSUPPORTED("a5guhlttwqpai3dhdhdx6shnu"); //     return mem;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="5cy6dl95ayyuzq0m35179g1a1", key="5cy6dl95ayyuzq0m35179g1a1", definition="void agfree(Agraph_t * g, void *ptr)")
public static Object agfree(Object... arg) {
UNSUPPORTED("4i7lm2j8h5unocyz6c4isbh2f"); // void agfree(Agraph_t * g, void *ptr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("72fdcloikkmdo51qrcdovcy5v"); //     if (ptr)
UNSUPPORTED("efvuftmcvfsswtq39k8vdrgmd"); // 	(((g)->clos->disc.mem)->free) (((g)->clos->state.mem), ptr);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
