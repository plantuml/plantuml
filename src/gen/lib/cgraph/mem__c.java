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
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agdisc_s;
import h.ST_Agraph_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.Globals;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class mem__c {

public static CFunction memopen = new CFunctionAbstract("memopen") {
	
	public Object exe(Globals zz, Object... args) {
		return memopen((ST_Agdisc_s)args[0]);
	}};
	
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="memopen", key="akq0jgwdspf75ypeatgcnfn8w", definition="static void *memopen(Agdisc_t* disc)")
public static Object memopen(ST_Agdisc_s disc) {
ENTERING("akq0jgwdspf75ypeatgcnfn8w","memopen");
try {
	return null;
} finally {
LEAVING("akq0jgwdspf75ypeatgcnfn8w","memopen");
}
}



public static CFunction memalloc = new CFunctionAbstract("memalloc") {
	
	public Object exe(Globals zz, Object... args) {
		return memalloc((__ptr__)args[0], (size_t)args[1]);
	}};
	
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



public static CFunction memresize = new CFunctionAbstract("memresize") {
	
	public Object exe(Globals zz, Object... args) {
		return memresize((__ptr__)args[0], (__ptr__)args[1], (size_t)args[2], (size_t)args[3]);
	}};
	
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



public static CFunction memfree = new CFunctionAbstract("memfree") {
	
	public Object exe(Globals zz, Object... args) {
		return memfree(args);
	}};
	
@Unused
@Original(version="2.38.0", path="lib/cgraph/mem.c", name="memfree", key="c320bstcg5nctel3onh2pserl", definition="static void memfree(void *heap, void *ptr)")
public static Object memfree(Object... arg_) {
UNSUPPORTED("5yxdf2sc5xnic9d5j24m0a7yf"); // static void memfree(void *heap, void *ptr)
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


}
