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
 * (C) Copyright 2009-2020, Arnaud Roques
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
package gen.lib.common;
import static smetana.core.JUtils.memset;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import smetana.core.CString;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class memory__c {


//3 6hfkgng9qf75cucpojc4r8x6w
// void *zmalloc(size_t nbytes) 
public static __ptr__ zmalloc(size_t nbytes) {
ENTERING("6hfkgng9qf75cucpojc4r8x6w","zmalloc");
try {
    __ptr__ rv;
    if (nbytes.isZero())
	return null;
    rv = gmalloc(nbytes);
    memset(rv, 0, nbytes);
    return rv;
} finally {
LEAVING("6hfkgng9qf75cucpojc4r8x6w","zmalloc");
}
}




//3 dn6c3bthm7yuhtrxx3o2je19z
// void *zrealloc(void *ptr, size_t size, size_t elt, size_t osize) 
public static Object zrealloc(Object... arg) {
UNSUPPORTED("50do65rl7k8poomk5tdkjw6k2"); // void *zrealloc(void *ptr, size_t size, size_t elt, size_t osize)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2esc94j9py77lfd4f23nk7w5x"); //     void *p = realloc(ptr, size * elt);
UNSUPPORTED("e13q5hvkbkekp23xt0oxo1nsb"); //     if (p == NULL && size) {
UNSUPPORTED("4t1y5iinm4310lkpvbal1spve"); // 	fprintf(stderr, "out of memory\n");
UNSUPPORTED("68kasxgknec72r19lohbk6n3q"); // 	return p;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2wr2qtb30ktteuohi1vgzxugz"); //     if (osize < size)
UNSUPPORTED("egt7kitgze0mw7g867jd7p6cq"); // 	memset((char *) p + (osize * elt), '\0', (size - osize) * elt);
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CString gmalloc(int nbytes) {
return new CString(nbytes);
}
//3 4mfikqpmxyxrke46i5xakatmc
//void *gmalloc(size_t nbytes) 
public static __ptr__ gmalloc(size_t nbytes) {
ENTERING("4mfikqpmxyxrke46i5xakatmc","gmalloc");
try {
    __ptr__ rv;
    if (nbytes.isZero())
	return null;
    rv = (__ptr__) nbytes.malloc();
    if (rv == null) {
	System.err.println("out of memory");
    }
    return rv;
} finally {
LEAVING("4mfikqpmxyxrke46i5xakatmc","gmalloc");
}
}




//3 1ed55yig6d18nhtbyqlf37jik
// void *grealloc(void *ptr, size_t size) 
public static __ptr__ grealloc(__ptr__ ptr, size_t size) {
ENTERING("1ed55yig6d18nhtbyqlf37jik","grealloc");
try {
    __ptr__ p = (__ptr__) size.realloc(ptr);
    return p;
} finally {
LEAVING("1ed55yig6d18nhtbyqlf37jik","grealloc");
}
}


}
