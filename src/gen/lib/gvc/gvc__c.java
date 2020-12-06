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
package gen.lib.gvc;
import static gen.lib.cgraph.attr__c.agattr;
import static gen.lib.common.textspan__c.textfont_dict_open;
import static gen.lib.gvc.gvcontext__c.gvNEWcontext;
import static gen.lib.gvc.gvtextlayout__c.gvtextlayout_select;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.NOT;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_GVC_s;
import smetana.core.CString;

public class gvc__c {


//3 f3vdhir2c7dz3pvmx9d3m4lx1
// GVC_t *gvContext(void) 
@Unused
@Original(version="2.38.0", path="lib/gvc/gvc.c", name="", key="f3vdhir2c7dz3pvmx9d3m4lx1", definition="GVC_t *gvContext(void)")
public static ST_GVC_s gvContext(Object... arg_) {
ENTERING("f3vdhir2c7dz3pvmx9d3m4lx1","gvContext");
try {
	ST_GVC_s gvc;
    agattr(null, AGNODE, new CString("label"), new CString("\\N"));
    /* default to no builtins, demand loading enabled */
    gvc = (ST_GVC_s) gvNEWcontext(null, (NOT(0)));
    /* builtins don't require LTDL */
    gvc.config_found = 0;
    gvtextlayout_select(gvc);   /* choose best available textlayout plugin immediately */
    textfont_dict_open(gvc);    /* initialize font dict */
    return gvc;
} finally {
LEAVING("f3vdhir2c7dz3pvmx9d3m4lx1","gvContext");
}
}


}
