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
package gen.lib.pathplan;
import static smetana.core.Macro.UNSUPPORTED;

public class inpoly__c {
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD




//3 8npqmbhzypa3vnvez32eenjfm
// int in_poly(Ppoly_t poly, Ppoint_t q) 
public static Object in_poly(Object... arg) {
UNSUPPORTED("591yna7lwmr02zotphxiz9jfy"); // int in_poly(Ppoly_t poly, Ppoint_t q)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3i49psuyz9205y6sp22yi0vg6"); //     int i, i1;			/* point index; i1 = i-1 mod n */
UNSUPPORTED("5jiiwgyq1pv7khw5ygfo33wmr"); //     int n;
UNSUPPORTED("ds3u5c0i2zcpyxll28zg5jfxz"); //     Ppoint_t *P;
UNSUPPORTED("aseexj0pf1b85gzltie980nx4"); //     P = poly.ps;
UNSUPPORTED("5wttw208juz89to1dojh5ke2v"); //     n = poly.pn;
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("esgbg5oqpjx4h4cvtcp2vu9ar"); // 	i1 = (i + n - 1) % n;
UNSUPPORTED("40aader15dux12c5hbpxegrwb"); // 	if (wind(P[i1],P[i],q) == 1) return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2mmsh4mer8e3bkt2jk4gf4cyq"); //     return ((!(0)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
