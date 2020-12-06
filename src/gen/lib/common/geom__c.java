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
package gen.lib.common;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_pointf;

public class geom__c {



//3 3aiyj7urv33rvps5ds204tciu
// static pointf rotatepf(pointf p, int cwrot) 
@Unused
@Original(version="2.38.0", path="lib/common/geom.c", name="rotatepf", key="3aiyj7urv33rvps5ds204tciu", definition="static pointf rotatepf(pointf p, int cwrot)")
public static ST_pointf rotatepf(final ST_pointf p, int cwrot) {
// WARNING!! STRUCT
return rotatepf_w_(p.copy(), cwrot).copy();
}
private static ST_pointf rotatepf_w_(final ST_pointf p, int cwrot) {
ENTERING("3aiyj7urv33rvps5ds204tciu","rotatepf");
try {
 UNSUPPORTED("adzi0wztceimu4ni3aonznmq7"); // static pointf rotatepf(pointf p, int cwrot)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bvmbf4zjo22hbkaarrfpdlocf"); //     static double sina, cosa;
UNSUPPORTED("2q61ok3mvkrnszcasq86sa47u"); //     static int last_cwrot;
UNSUPPORTED("7lh87lvufqsd73q9difg0omei"); //     pointf P;
UNSUPPORTED("apr20mshcgdjbln509cnpuysv"); //     /* cosa is initially wrong for a cwrot of 0
UNSUPPORTED("7chgrmqliof6d9xytud69tz1u"); //      * this caching only works because we are never called for 0 rotations */
UNSUPPORTED("bbm4jlwljjo7wmvr5ma5c3ybf"); //     if (cwrot != last_cwrot) {
UNSUPPORTED("djdw08yi87cxa9gld79itcxte"); // 	sincos(cwrot / (2 * M_PI), &sina, &cosa);
UNSUPPORTED("1p92a46pieij11gut3g3w5c8a"); // 	last_cwrot = cwrot;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5ebfpnhj3mdplyf7cdm05fvqt"); //     P.x = p.x * cosa - p.y * sina;
UNSUPPORTED("87v4w9w5q8h1qv8g0mktgna71"); //     P.y = p.y * cosa + p.x * sina;
UNSUPPORTED("57gdhsck3pq8wsbtv00wvc7ca"); //     return P;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("3aiyj7urv33rvps5ds204tciu","rotatepf");
}
}



//3 5q8h2tm3jifiasn423wrm0y60
@Reviewed(when = "01/12/2020")
@Original(version="2.38.0", path="lib/common/geom.c", name="cwrotatepf", key="5q8h2tm3jifiasn423wrm0y60", definition="pointf cwrotatepf(pointf p, int cwrot)")
public static ST_pointf cwrotatepf(final ST_pointf p, int cwrot) {
// WARNING!! STRUCT
return cwrotatepf_w_(p.copy(), cwrot).copy();
}
private static ST_pointf cwrotatepf_w_(final ST_pointf p, int cwrot) {
ENTERING("5q8h2tm3jifiasn423wrm0y60","cwrotatepf");
try {
    double x = p.x, y = p.y;
    switch (cwrot) {
    case 0:
	break;
    case 90:
	p.x = y;
	p.y = -x;
	break;
    case 180:
	p.x = x;
	p.y = -y;
	break;
    case 270:
	p.x = y;
	p.y = x;
	break;
    default:
	if (cwrot < 0)
	    return ccwrotatepf(p, -cwrot);
        if (cwrot > 360)
	    return cwrotatepf(p, cwrot%360);
	return rotatepf(p, cwrot);
    }
    return p;
} finally {
LEAVING("5q8h2tm3jifiasn423wrm0y60","cwrotatepf");
}
}






//3 6np74e9pfmv8uek8irqru2tma
// pointf ccwrotatepf(pointf p, int ccwrot) 
@Unused
@Original(version="2.38.0", path="lib/common/geom.c", name="ccwrotatepf", key="6np74e9pfmv8uek8irqru2tma", definition="pointf ccwrotatepf(pointf p, int ccwrot)")
public static ST_pointf ccwrotatepf(final ST_pointf p, int ccwrot) {
// WARNING!! STRUCT
return ccwrotatepf_w_(p.copy(), ccwrot).copy();
}
private static ST_pointf ccwrotatepf_w_(final ST_pointf p, int ccwrot) {
ENTERING("6np74e9pfmv8uek8irqru2tma","ccwrotatepf");
try {
    double x = p.x, y = p.y;
    switch (ccwrot) {
    case 0:
	break;
    case 90:
	p.x = -y;
	p.y = x;
	break;
    case 180:
	p.x = x;
	p.y = -y;
	break;
    case 270:
	p.x = y;
	p.y = x;
	break;
    default:
	if (ccwrot < 0)
	    return cwrotatepf(p, -ccwrot);
        if (ccwrot > 360)
	    return ccwrotatepf(p, ccwrot%360);
	return rotatepf(p, 360-ccwrot);
    }
    return p;
} finally {
LEAVING("6np74e9pfmv8uek8irqru2tma","ccwrotatepf");
}
}





//3 90k9l4o3khu3dw0fzkrcd97nk
// double ptToLine2 (pointf a, pointf b, pointf p) 
@Unused
@Original(version="2.38.0", path="lib/common/geom.c", name="ptToLine2", key="90k9l4o3khu3dw0fzkrcd97nk", definition="double ptToLine2 (pointf a, pointf b, pointf p)")
public static double ptToLine2(final ST_pointf a, final ST_pointf b, final ST_pointf p) {
// WARNING!! STRUCT
return ptToLine2_w_(a.copy(), b.copy(), p.copy());
}
private static double ptToLine2_w_(final ST_pointf a, final ST_pointf b, final ST_pointf p) {
ENTERING("90k9l4o3khu3dw0fzkrcd97nk","ptToLine2");
try {
  double dx = b.x-a.x;
  double dy = b.y-a.y;
  double a2 = (p.y-a.y)*dx - (p.x-a.x)*dy;
  a2 *= a2;   /* square - ensures that it is positive */
  if (a2 < 0.0000000001) return 0.;  /* avoid 0/0 problems */
  return a2 / (dx*dx + dy*dy);
} finally {
LEAVING("90k9l4o3khu3dw0fzkrcd97nk","ptToLine2");
}
}





}
