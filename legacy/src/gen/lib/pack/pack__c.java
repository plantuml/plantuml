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
package gen.lib.pack;
import static gen.lib.cgraph.attr__c.agget;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.EN_pack_mode;
import h.ST_Agraph_s;
import h.ST_pack_info;
import smetana.core.CString;
import smetana.core.Globals;

public class pack__c {
    // ::remove folder when __HAXE__



/* parsePackModeInfo;
 * Return pack_mode of graph using "packmode" attribute.
 * If not defined, return dflt
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/pack/pack.c", name="parsePackModeInfo", key="dyb1n3lhbi0wnr9kvmu6onux9", definition="pack_mode  parsePackModeInfo(char* p, pack_mode dflt, pack_info* pinfo)")
public static EN_pack_mode parsePackModeInfo(CString p, EN_pack_mode dflt, ST_pack_info pinfo) {
ENTERING("dyb1n3lhbi0wnr9kvmu6onux9","parsePackModeInfo");
try {
    float v;
    int i;
    //assert (pinfo);
    pinfo.flags = 0;
    pinfo.mode = dflt;
    pinfo.sz = 0;
    pinfo.vals = null;
    if (p!=null && p.charAt(0)!='\0') {
UNSUPPORTED("2ybrtqq8efxouph8wjbrnowxz"); // 	switch (*p) {
UNSUPPORTED("2v5u3irq50r1n2ccuna0y09lk"); // 	case 'a':
UNSUPPORTED("4o68zbwd6291pxryyntjh2432"); // 	    if ((!strncmp(p,"array",(sizeof("array")/sizeof(char) - 1)))) {
UNSUPPORTED("3cxht114gc0fn4wl8xjy515bv"); // 		pinfo->mode = l_array;
UNSUPPORTED("edkaa80od81ojkn7d4h0q1zbu"); // 		p += (sizeof("array")/sizeof(char) - 1);
UNSUPPORTED("106zhcf9a6frvafctbbgrbws3"); // 		p = chkFlags (p, pinfo);
UNSUPPORTED("epac8gmlq3r1q3g6lh3fb9nzh"); // 		if ((sscanf (p, "%d", &i)>0) && (i > 0))
UNSUPPORTED("2e563kaxakuuou2t5kqvv33w5"); // 		    pinfo->sz = i;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("38dve3snq2grkwui6y1tpc6r1"); // 	    else if ((!strncmp(p,"aspect",(sizeof("aspect")/sizeof(char) - 1)))) {
UNSUPPORTED("ems4wqj0nbd0vi3sttf36tnir"); // 		pinfo->mode = l_aspect;
UNSUPPORTED("3ckdfsy55ba8os2xhymh002al"); // 		if ((sscanf (p + (sizeof("array")/sizeof(char) - 1), "%f", &v)>0) && (v > 0))
UNSUPPORTED("1i6xlg2sfqvjqjdu811xm5398"); // 		    pinfo->aspect = v;
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("b3tzhasiwa1p83l47nhl6k5x8"); // 		    pinfo->aspect = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f3lyz2cejs6yn5fyckhn7ba1"); // 	case 'c':
UNSUPPORTED("d6yjom2gtqleeq1vf5l72eazl"); // 	    if ((*(p)==*("cluster")&&!strcmp(p,"cluster")))
UNSUPPORTED("2xe2y3s32q27l74dshw3831hu"); // 		pinfo->mode = l_clust;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("e00kor2k58i044hvvxytt9dhg"); // 	case 'g':
UNSUPPORTED("8vzhimdinzz48u15tcx34gzgs"); // 	    if ((*(p)==*("graph")&&!strcmp(p,"graph")))
UNSUPPORTED("coafx7pn3ah1ulp0o7a0z119r"); // 		pinfo->mode = l_graph;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f187wptsr73liavtlyoyfovp3"); // 	case 'n':
UNSUPPORTED("aqi1ed010xc3cj8xoye8vqkqe"); // 	    if ((*(p)==*("node")&&!strcmp(p,"node")))
UNSUPPORTED("epewq8yin5pr1meeofxvntjes"); // 		pinfo->mode = l_node;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    //if (Verbose) {
	//fprintf (stderr, "pack info:\n");
	//fprintf (stderr, "  mode   %s\n", mode2Str(pinfo->mode));
	//if (pinfo->mode == l_aspect)
	//    fprintf (stderr, "  aspect %f\n", pinfo->aspect);
	//fprintf (stderr, "  size   %d\n", pinfo->sz);
	//fprintf (stderr, "  flags  %d\n", pinfo->flags);
    //}
    return pinfo.mode;
} finally {
LEAVING("dyb1n3lhbi0wnr9kvmu6onux9","parsePackModeInfo");
}
}




/* getPackModeInfo;
 * Return pack_mode of graph using "packmode" attribute.
 * If not defined, return dflt
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/pack/pack.c", name="getPackModeInfo", key="bnxqpsmvz6tyekstfjte4pzwj", definition="pack_mode  getPackModeInfo(Agraph_t * g, pack_mode dflt, pack_info* pinfo)")
public static EN_pack_mode getPackModeInfo(Globals zz, ST_Agraph_s g, EN_pack_mode dflt, ST_pack_info pinfo) {
ENTERING("bnxqpsmvz6tyekstfjte4pzwj","getPackModeInfo");
try {
    return parsePackModeInfo (agget(zz, g, new CString("packmode")), dflt, pinfo);
} finally {
LEAVING("bnxqpsmvz6tyekstfjte4pzwj","getPackModeInfo");
}
}





/* getPack:
 * Return "pack" attribute of g.
 * If not defined or negative, return not_def.
 * If defined but not specified, return dflt.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/pack/pack.c", name="getPack", key="ata97fmix5q1oikrmk5pezvrf", definition="int getPack(Agraph_t * g, int not_def, int dflt)")
public static int getPack(Globals zz, ST_Agraph_s g, int not_def, int dflt) {
ENTERING("ata97fmix5q1oikrmk5pezvrf","getPack");
try {
    CString p;
    int i;
    int v = not_def;
    if ((p = agget(zz, g, new CString("pack")))!=null) {
UNSUPPORTED("enpqar7c7okvibe7xhe0vjtfn"); // 	if ((sscanf(p, "%d", &i) == 1) && (i >= 0))
UNSUPPORTED("3puzxwczcmpxvlw8cvmeyio74"); // 	    v = i;
UNSUPPORTED("65w8fxtw319slbg2c6nvtmow8"); // 	else if ((*p == 't') || (*p == 'T'))
UNSUPPORTED("5lifdir9mxnvpi8ur34qo0jej"); // 	    v = dflt;
    }
    return v;
} finally {
LEAVING("ata97fmix5q1oikrmk5pezvrf","getPack");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/pack/pack.c", name="getPackInfo", key="ce4a70w8ddkj4l9efi74h61s6", definition="pack_mode  getPackInfo(Agraph_t * g, pack_mode dflt, int dfltMargin, pack_info* pinfo)")
public static EN_pack_mode getPackInfo(Globals zz, ST_Agraph_s g, EN_pack_mode dflt, int dfltMargin, ST_pack_info pinfo) {
ENTERING("ce4a70w8ddkj4l9efi74h61s6","getPackInfo");
try {
    pinfo.margin = getPack(zz, g, dfltMargin, dfltMargin);
    //if (Verbose) {
	//fprintf (stderr, "  margin %d\n", pinfo->margin);
    //}
    pinfo.doSplines = 0;
    pinfo.fixed = null;
    getPackModeInfo(zz, g, dflt, pinfo);
    return pinfo.mode;
} finally {
LEAVING("ce4a70w8ddkj4l9efi74h61s6","getPackInfo");
}
}


}
