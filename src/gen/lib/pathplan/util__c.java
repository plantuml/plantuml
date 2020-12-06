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
package gen.lib.pathplan;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_Ppoly_t;
import h.ST_pointf;
import smetana.core.CArray;
import smetana.core.Z;

public class util__c {


//3 ct6tszngugakbl42zkaqrt7p1
// void make_polyline(Ppolyline_t line, Ppolyline_t* sline) 
@Unused
@Original(version="2.38.0", path="lib/pathplan/util.c", name="make_polyline", key="ct6tszngugakbl42zkaqrt7p1", definition="void make_polyline(Ppolyline_t line, Ppolyline_t* sline)")
public static void make_polyline(ST_Ppoly_t line, ST_Ppoly_t sline) {
	make_polyline_(line.copy(), sline);
}


private static void make_polyline_(ST_Ppoly_t line, ST_Ppoly_t sline) {
ENTERING("ct6tszngugakbl42zkaqrt7p1","make_polyline_");
try {

    int i, j;
    int npts = 4 + 3*(line.pn-2);

    if (npts > Z.z().isz) {
	Z.z().ispline = CArray.<ST_pointf>REALLOC__(npts, Z.z().ispline, ST_pointf.class); 
	Z.z().isz = npts;
    }

    j = i = 0;
    Z.z().ispline.get__(j+1).___(line.ps.get__(i));
    Z.z().ispline.get__(j  ).___(line.ps.get__(i));
    j += 2;
    i++;
    for (; i < line.pn-1; i++) {
        Z.z().ispline.get__(j+2).___(line.ps.get__(i));
        Z.z().ispline.get__(j+1).___(line.ps.get__(i));
        Z.z().ispline.get__(j  ).___(line.ps.get__(i));
	j += 3;
    }
    Z.z().ispline.get__(j+1).___(line.ps.get__(i));
    Z.z().ispline.get__(j  ).___(line.ps.get__(i));
    sline.pn = npts;
    sline.ps = Z.z().ispline;

} finally {
LEAVING("ct6tszngugakbl42zkaqrt7p1","make_polyline_");
}
}


}
