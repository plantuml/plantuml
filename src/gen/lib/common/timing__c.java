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
import static smetana.core.Macro.UNSUPPORTED;

public class timing__c {
//1 9rcpctijr9b8r0p971d3f9t9h
// static mytime_t T




//3 5651jzaik1he1ffxyznovdahf
// void start_timer(void) 
public static Object start_timer(Object... arg) {
UNSUPPORTED("69qokxdamqf1bxhaqbcnpwkw1"); // void start_timer(void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ectdmh5it8savad5lltnu8r3w"); //     times(&(T));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 11o22f2clw6ou6c0hmn76feu4
// double elapsed_sec(void) 
public static Object elapsed_sec(Object... arg) {
UNSUPPORTED("ap51jenuew5js107vu8srvn3x"); // double elapsed_sec(void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8jn78w98k16lwmikzd1lk5qx4"); //     mytime_t S;
UNSUPPORTED("d7i4mzmfmp4ps6n8cbfzvrlcf"); //     double rv;
UNSUPPORTED("bh6n36r9g26g7oq07lzcngnm3"); //     times(&(S));
UNSUPPORTED("cu10zpf5lls0sjd3425blqug9"); //     rv = ((S.tms_utime + S.tms_stime - T.tms_utime - T.tms_stime)/(double)60);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
