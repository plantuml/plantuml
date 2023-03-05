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
package gen.lib.common;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_GVC_s;
import h.ST_dt_s;
import h.ST_pointf;
import h.ST_textspan_t;

public class textspan__c {

//3 n8tcl06mifdn779rzenam44z
// pointf textspan_size(GVC_t *gvc, textspan_t * span) 
@Unused
@Original(version="2.38.0", path="lib/common/textspan.c", name="textspan_size", key="n8tcl06mifdn779rzenam44z", definition="pointf textspan_size(GVC_t *gvc, textspan_t * span)")
public static ST_pointf textspan_size(ST_GVC_s gvc, ST_textspan_t span) {
// WARNING!! STRUCT
return textspan_size_w_(gvc, span).copy();
}
private static ST_pointf textspan_size_w_(ST_GVC_s gvc, ST_textspan_t span) {
ENTERING("n8tcl06mifdn779rzenam44z","textspan_size");
try {
	System.err.println("Warning:textspan_size "+span);
	span.size.x = 30;
	span.size.y = 20;
    return span.size.copy();
} finally {
LEAVING("n8tcl06mifdn779rzenam44z","textspan_size");
}
}



//3 9mfrgcpzz2d9f7nxfgx4nxj2q
// Dt_t * textfont_dict_open(GVC_t *gvc) 
@Unused
@Original(version="2.38.0", path="lib/common/textspan.c", name="textfont_dict_open", key="9mfrgcpzz2d9f7nxfgx4nxj2q", definition="Dt_t * textfont_dict_open(GVC_t *gvc)")
public static ST_dt_s textfont_dict_open(ST_GVC_s gvc) {
ENTERING("9mfrgcpzz2d9f7nxfgx4nxj2q","textfont_dict_open");
try {
	return null;
//UNSUPPORTED("nexd6tbei8przmonjwzag8uf"); // Dt_t * textfont_dict_open(GVC_t *gvc)
//UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
//UNSUPPORTED("cdeb412fjgrtibum4qt0yxhc7"); //     ( (&(gvc->textfont_disc))->key = (0), (&(gvc->textfont_disc))->size = (sizeof(textfont_t)), (&(gvc->textfont_disc))->link = (-1), (&(gvc->textfont_disc))->makef = (textfont_makef), (&(gvc->textfont_disc))->freef = (textfont_freef), (&(gvc->textfont_disc))->comparf = (textfont_comparf), (&(gvc->textfont_disc))->hashf = (NULL), (&(gvc->textfont_disc))->memoryf = (NULL), (&(gvc->textfont_disc))->eventf = (NULL) );
//UNSUPPORTED("d1t3xr23spgfbbggquvg4nodm"); //     gvc->textfont_dt = dtopen(&(gvc->textfont_disc), Dtoset);
//UNSUPPORTED("6ynzkfpi9sy9wbln45o4jajhp"); //     return gvc->textfont_dt;
//UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
} finally {
LEAVING("9mfrgcpzz2d9f7nxfgx4nxj2q","textfont_dict_open");
}
}



}
