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
import static gen.lib.cgraph.attr__c.agattr;
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.refstr__c.aghtmlstr;
import static gen.lib.common.emit__c.init_xdot;
import static gen.lib.common.labels__c.make_label;
import static gen.lib.common.labels__c.strdup_and_subst_obj;
import static gen.lib.common.utils__c.late_double;
import static gen.lib.common.utils__c.late_int;
import static gen.lib.common.utils__c.late_nnstring;
import static gen.lib.common.utils__c.late_string;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.maptoken;
import static smetana.core.JUtils.atof;
import static smetana.core.JUtils.atoi;
import static smetana.core.JUtils.getenv;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.BOTTOM_IX;
import static smetana.core.Macro.DEFAULT_NODESEP;
import static smetana.core.Macro.DEFAULT_RANKSEP;
import static smetana.core.Macro.GD_border;
import static smetana.core.Macro.GD_charset;
import static smetana.core.Macro.GD_drawing;
import static smetana.core.Macro.GD_exact_ranksep;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_fontnames;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.GD_label_pos;
import static smetana.core.Macro.GD_nodesep;
import static smetana.core.Macro.GD_rankdir2;
import static smetana.core.Macro.GD_ranksep;
import static smetana.core.Macro.GD_showboxes;
import static smetana.core.Macro.GLOBAL;
import static smetana.core.Macro.GRAPH_LABEL;
import static smetana.core.Macro.GVBEGIN;
import static smetana.core.Macro.LABEL_AT_BOTTOM;
import static smetana.core.Macro.LABEL_AT_TOP;
import static smetana.core.Macro.LEFT_IX;
import static smetana.core.Macro.LOCAL;
import static smetana.core.Macro.LT_HTML;
import static smetana.core.Macro.LT_NONE;
import static smetana.core.Macro.MIN_NODESEP;
import static smetana.core.Macro.MYHUGE;
import static smetana.core.Macro.NOCLUST;
import static smetana.core.Macro.NODENAME_ESC;
import static smetana.core.Macro.PAD;
import static smetana.core.Macro.POINTS;
import static smetana.core.Macro.RANKDIR_LR;
import static smetana.core.Macro.RANKDIR_TB;
import static smetana.core.Macro.RIGHT_IX;
import static smetana.core.Macro.TOP_IX;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.agfindedgeattr;
import static smetana.core.Macro.agfindgraphattr;
import static smetana.core.Macro.agfindnodeattr;
import static smetana.core.Macro.streq;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.EN_fontname_kind;
import h.ST_Agraph_s;
import h.ST_layout_t;
import h.ST_pointf;
import smetana.core.CString;
import smetana.core.Globals;

public class input__c {



/* getdoubles2ptf:
 * converts a graph attribute in inches to a pointf in points.
 * If only one number is given, it is used for both x and y.
 * Returns true if the attribute ends in '!'.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/input.c", name="getdoubles2ptf", key="72no6ayfvjinlnupyn5jlmayg", definition="static boolean getdoubles2ptf(graph_t * g, char *name, pointf * result)")
public static boolean getdoubles2ptf(Globals zz, ST_Agraph_s g, CString name, ST_pointf result) {
ENTERING("72no6ayfvjinlnupyn5jlmayg","getdoubles2ptf");
try {
    CString p;
    int i;
    double xf, yf;
    char c = '\0';
    boolean rv = false;
    if ((p = agget(zz, g, name))!=null) {
UNSUPPORTED("21b2kes0vrizyai71yj9e2os3"); // 	i = sscanf(p, "%lf,%lf%c", &xf, &yf, &c);
UNSUPPORTED("9wua6uiybfvqd70huuo0yatcf"); // 	if ((i > 1) && (xf > 0) && (yf > 0)) {
UNSUPPORTED("8z2huopqt4m1rvfcd7vqatka4"); // 	    result->x = ((((xf)*72>=0)?(int)((xf)*72 + .5):(int)((xf)*72 - .5)));
UNSUPPORTED("cil4j0n3iq35gr2pfewi2qawz"); // 	    result->y = ((((yf)*72>=0)?(int)((yf)*72 + .5):(int)((yf)*72 - .5)));
UNSUPPORTED("9qnr8qmbz7pf3mmpebux0p08m"); // 	    if (c == '!')
UNSUPPORTED("dqyb6drzg8ig5ecb31fq5c1d4"); // 		rv = (!(0));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("8wtaqjit9awt7xd08vuifknry"); // 	    c = '\0';
UNSUPPORTED("705372l4htjtcvnq97l7i54g8"); // 	    i = sscanf(p, "%lf%c", &xf, &c);
UNSUPPORTED("4n9k1twwfmxyet8tokr7xnktj"); // 	    if ((i > 0) && (xf > 0)) {
UNSUPPORTED("8ui53rmpq7ao1p4yin0xqzszj"); // 		result->y = result->x = ((((xf)*72>=0)?(int)((xf)*72 + .5):(int)((xf)*72 - .5)));
UNSUPPORTED("1rflva1x66uhyqxr5zbpcsgnh"); // 		if (c == '!') rv = (!(0));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    return rv;
} finally {
LEAVING("72no6ayfvjinlnupyn5jlmayg","getdoubles2ptf");
}
}




@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/common/input.c", name="findCharset", key="9t08dr2ks9qz1pyfz99awla6x", definition="static int findCharset (graph_t * g)")
public static int findCharset(ST_Agraph_s g) {
ENTERING("9t08dr2ks9qz1pyfz99awla6x","findCharset");
try {
	return 0;
} finally {
LEAVING("9t08dr2ks9qz1pyfz99awla6x","findCharset");
}
}



/* setRatio:
 * Checks "ratio" attribute, if any, and sets enum type.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/input.c", name="setRatio", key="3bnmjpvynh1j9oh2p2vi0vh2m", definition="static void setRatio(graph_t * g)")
public static void setRatio(Globals zz, ST_Agraph_s g) {
ENTERING("3bnmjpvynh1j9oh2p2vi0vh2m","setRatio");
try {
    CString p;
    char c;
    double ratio;
    if ((p = agget(zz, g, new CString("ratio")))!=null && ((c = p.charAt(0))!='\0')) {
UNSUPPORTED("7rk995hpmaqbbasmi40mqg0yw"); // 	switch (c) {
UNSUPPORTED("2v5u3irq50r1n2ccuna0y09lk"); // 	case 'a':
UNSUPPORTED("3jv8xrrloj92axkpkgolzwgo6"); // 	    if ((*(p)==*("auto")&&!strcmp(p,"auto")))
UNSUPPORTED("8bdbsrt9sk4hnj3wm6z100qm"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_AUTO;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("f3lyz2cejs6yn5fyckhn7ba1"); // 	case 'c':
UNSUPPORTED("1v3jyjziibgnha1glbymorwg1"); // 	    if ((*(p)==*("compress")&&!strcmp(p,"compress")))
UNSUPPORTED("coprfqf41n6byzz3nfneke6a"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_COMPRESS;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2fzjr952o6hmcz3ad5arl2n8d"); // 	case 'e':
UNSUPPORTED("5s06nikh994hgncpwni2p4rwq"); // 	    if ((*(p)==*("expand")&&!strcmp(p,"expand")))
UNSUPPORTED("eanijnkdjj1f6q7su4gmmijpj"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_EXPAND;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8jntw084f69528np3kisw5ioc"); // 	case 'f':
UNSUPPORTED("105p0jwfnsptmrweig5mhpkn9"); // 	    if ((*(p)==*("fill")&&!strcmp(p,"fill")))
UNSUPPORTED("eknfh3axjhorf2rfb914hdgbd"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_FILL;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("e4fr8djxwn615yr0rj46vtdbd"); // 	    ratio = atof(p);
UNSUPPORTED("43a0ik2dkpg3y58orisgkn32q"); // 	    if (ratio > 0.0) {
UNSUPPORTED("azv56xi8njootl2n9l5bm1udc"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio_kind = R_VALUE;
UNSUPPORTED("ch5o67mezsw0v6iwxylb98myn"); // 		(((Agraphinfo_t*)(((Agobj_t*)(g))->data))->drawing)->ratio = ratio;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
    }
} finally {
LEAVING("3bnmjpvynh1j9oh2p2vi0vh2m","setRatio");
}
}




static CString rankname[] = new CString[] { new CString("local"), new CString("global"), new CString("none"), null };
static int rankcode[] = { LOCAL, GLOBAL, NOCLUST, LOCAL };
static CString fontnamenames[] = new CString[] {new CString("gd"),new CString("ps"),new CString("svg"), null};
static int fontnamecodes[] = {EN_fontname_kind.NATIVEFONTS.ordinal(), EN_fontname_kind.PSFONTS.ordinal(), EN_fontname_kind.SVGFONTS.ordinal(),-1};

@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/common/input.c", name="graph_init", key="8gzdr3oil2d0e2o7m84wsszfg", definition="void graph_init(graph_t * g, boolean use_rankdir)")
public static void graph_init(Globals zz, ST_Agraph_s g, boolean use_rankdir) {
ENTERING("8gzdr3oil2d0e2o7m84wsszfg","graph_init");
try {
    CString p;
    double xf;
    int rankdir;
    GD_drawing(g, new ST_layout_t());
    /* set this up fairly early in case any string sizes are needed */
    if ((p = agget(zz, g, new CString("fontpath")))!=null || (p = getenv(new CString("DOTFONTPATH")))!=null) {
UNSUPPORTED("81bz3jcukzyotxiqgrlhn9cbq"); // 	/* overide GDFONTPATH in local environment if dot
UNSUPPORTED("6jgl7atk1m9yeam4auh127azw"); // 	 * wants its own */
UNSUPPORTED("dyk0vc64gdzy1uwvsc2jqnjdw"); // 	static char *buf = 0;
UNSUPPORTED("8dywgree8jdjmj2ll2whbekhe"); // 	buf = grealloc(buf, strlen("GDFONTPATH=") + strlen(p) + 1);
UNSUPPORTED("d9ej6bo2s49vpstu3pql6tkrx"); // 	strcpy(buf, "GDFONTPATH=");
UNSUPPORTED("1s2jcd2h3eok7j6pclv20gyi2"); // 	strcat(buf, p);
UNSUPPORTED("abkxekvux4nramryfw2e8vcru"); // 	putenv(buf);
    }
    
    
    GD_charset(g, findCharset (g));
    GD_drawing(g).quantum =
 	late_double(g, agfindgraphattr(zz, g, "quantum"), 0.0, 0.0);
    
    /* setting rankdir=LR is only defined in dot,
     * but having it set causes shape code and others to use it. 
     * The result is confused output, so we turn it off unless requested.
     * This effective rankdir is stored in the bottom 2 bits of g->u.rankdir.
     * Sometimes, the code really needs the graph's rankdir, e.g., neato -n
     * with record shapes, so we store the real rankdir in the next 2 bits.
     */
    rankdir = RANKDIR_TB;
    if ((p = agget(zz, g, new CString("rankdir")))!=null) {
    	if (streq(p, "LR"))
    	    rankdir = RANKDIR_LR;
//UNSUPPORTED("ry8itlrmblmuegdwk1iu1t0x"); // 	else if ((*(p)==*("BT")&&!strcmp(p,"BT")))
//UNSUPPORTED("5hno0xn18yt443qg815w3c2s2"); // 	    rankdir = 2;
//UNSUPPORTED("aal39mi047mhafrsrxoutcffk"); // 	else if ((*(p)==*("RL")&&!strcmp(p,"RL")))
//UNSUPPORTED("7vlda224wrgcdhr0ts3mndh5q"); // 	    rankdir = 3;
    }
    if (use_rankdir)
	GD_rankdir2(g, (rankdir << 2) | rankdir);
    else
	GD_rankdir2(g, (rankdir << 2));
    
    xf = late_double(g, agfindgraphattr(zz, g, "nodesep"),
    	DEFAULT_NODESEP, MIN_NODESEP);
    GD_nodesep(g, POINTS(xf));
    
    
    p = late_string(g, agfindgraphattr(zz, g, "ranksep"), null);
    if (p!=null) {
    	xf = atof(p);
    	if (xf < 0.02)
    		xf = 0.02;

	if (p.isSame(new CString("equally")))
	    GD_exact_ranksep(g, 1);
    } else
	xf = DEFAULT_RANKSEP;
    GD_ranksep(g, POINTS(xf));
    
    
    GD_showboxes(g, late_int(g, agfindgraphattr(zz, g, "showboxes"), 0, 0));
    p = late_string(g, agfindgraphattr(zz, g, "fontnames"), null);
    GD_fontnames(g, maptoken(p, fontnamenames, fontnamecodes));
    
    
    setRatio(zz, g);
    GD_drawing(g).filled = 
	getdoubles2ptf(zz, g, new CString("size"), GD_drawing(g).size);
    getdoubles2ptf(zz, g, new CString("page"), GD_drawing(g).page);
    
    
    GD_drawing(g).centered = mapbool(agget(zz, g, new CString("center")));
    
    
    if ((p = agget(zz, g, new CString("rotate")))!=null)
	GD_drawing(g).landscape= (atoi(p) == 90);
    else if ((p = agget(zz, g, new CString("orientation")))!=null)
	GD_drawing(g).landscape= ((p.charAt(0) == 'l') || (p.charAt(0) == 'L'));
    else if ((p = agget(zz, g, new CString("landscape")))!=null)
	GD_drawing(g).landscape= mapbool(p);
    
    
    p = agget(zz, g, new CString("clusterrank"));
    zz.CL_type = maptoken(p, rankname, rankcode);
    p = agget(zz, g, new CString("concentrate"));
    zz.Concentrate = mapbool(p);
    zz.State = GVBEGIN;
    zz.EdgeLabelsDone = 0;
    
    
    GD_drawing(g).dpi = 0.0;
    if (((p = agget(zz, g, new CString("dpi")))!=null && p.charAt(0)!='\0')
	|| ((p = agget(zz, g, new CString("resolution")))!=null && p.charAt(0)!='\0'))
	GD_drawing(g).dpi = atof(p);
    
    
    do_graph_label(zz, g);
    
    
    zz.Initial_dist = MYHUGE;
    
    zz.G_ordering = agfindgraphattr(zz, g, "ordering");
    zz.G_gradientangle = agfindgraphattr(zz, g,"gradientangle");
    zz.G_margin = agfindgraphattr(zz, g, "margin");

    /* initialize nodes */
    zz.N_height = agfindnodeattr(zz, g, "height");
    zz.N_width = agfindnodeattr(zz, g, "width");
    zz.N_shape = agfindnodeattr(zz, g, "shape");
    zz.N_color = agfindnodeattr(zz, g, "color");
    zz.N_fillcolor = agfindnodeattr(zz, g, "fillcolor");
    zz.N_style = agfindnodeattr(zz, g, "style");
    zz.N_fontsize = agfindnodeattr(zz, g, "fontsize");
    zz.N_fontname = agfindnodeattr(zz, g, "fontname");
    zz.N_fontcolor = agfindnodeattr(zz, g, "fontcolor");
    zz.N_label = agfindnodeattr(zz, g, "label");
    if ((zz.N_label) == null)
	zz.N_label = agattr(zz, g, AGNODE, new CString("label"), new CString(NODENAME_ESC));
    zz.N_xlabel = agfindnodeattr(zz, g, "xlabel");
    zz.N_showboxes = agfindnodeattr(zz, g, "showboxes");
    zz.N_penwidth = agfindnodeattr(zz, g, "penwidth");
    zz.N_ordering = agfindnodeattr(zz, g, "ordering");
    zz.N_margin = agfindnodeattr(zz, g, "margin");
    
    
    /* attribs for polygon shapes */
    zz.N_sides = agfindnodeattr(zz, g, "sides");
    zz.N_peripheries = agfindnodeattr(zz, g, "peripheries");
    zz.N_skew = agfindnodeattr(zz, g, "skew");
    zz.N_orientation = agfindnodeattr(zz, g, "orientation");
    zz.N_distortion = agfindnodeattr(zz, g, "distortion");
    zz.N_fixed = agfindnodeattr(zz, g, "fixedsize");
    zz.N_imagescale = agfindnodeattr(zz, g, "imagescale");
    zz.N_nojustify = agfindnodeattr(zz, g, "nojustify");
    zz.N_layer = agfindnodeattr(zz, g, "layer");
    zz.N_group = agfindnodeattr(zz, g, "group");
    zz.N_comment = agfindnodeattr(zz, g, "comment");
    zz.N_vertices = agfindnodeattr(zz, g, "vertices");
    zz.N_z = agfindnodeattr(zz, g, "z");
    zz.N_gradientangle = agfindnodeattr(zz, g,"gradientangle");
    
    
    /* initialize edges */
    zz.E_weight = agfindedgeattr(zz, g, "weight");
    zz.E_color = agfindedgeattr(zz, g, "color");
    zz.E_fillcolor = agfindedgeattr(zz, g, "fillcolor");
    zz.E_fontsize = agfindedgeattr(zz, g, "fontsize");
    zz.E_fontname = agfindedgeattr(zz, g, "fontname");
    zz.E_fontcolor = agfindedgeattr(zz, g, "fontcolor");
    zz.E_label = agfindedgeattr(zz, g, "label");
    zz.E_xlabel = agfindedgeattr(zz, g, "xlabel");
    zz.E_label_float = agfindedgeattr(zz, g, "labelfloat");
    /* vladimir */
    zz.E_dir = agfindedgeattr(zz, g, "dir");
    zz.E_arrowhead = agfindedgeattr(zz, g, "arrowhead");
    zz.E_arrowtail = agfindedgeattr(zz, g, "arrowtail");
    zz.E_headlabel = agfindedgeattr(zz, g, "headlabel");
    zz.E_taillabel = agfindedgeattr(zz, g, "taillabel");
    zz.E_labelfontsize = agfindedgeattr(zz, g, "labelfontsize");
    zz.E_labelfontname = agfindedgeattr(zz, g, "labelfontname");
    zz.E_labelfontcolor = agfindedgeattr(zz, g, "labelfontcolor");
    zz.E_labeldistance = agfindedgeattr(zz, g, "labeldistance");
    zz.E_labelangle = agfindedgeattr(zz, g, "labelangle");
    /* end vladimir */
    zz.E_minlen = agfindedgeattr(zz, g, "minlen");
    zz.E_showboxes = agfindedgeattr(zz, g, "showboxes");
    zz.E_style = agfindedgeattr(zz, g, "style");
    zz.E_decorate = agfindedgeattr(zz, g, "decorate");
    zz.E_arrowsz = agfindedgeattr(zz, g, "arrowsize");
    zz.E_constr = agfindedgeattr(zz, g, "constraint");
    zz.E_layer = agfindedgeattr(zz, g, "layer");
    zz.E_comment = agfindedgeattr(zz, g, "comment");
    zz.E_tailclip = agfindedgeattr(zz, g, "tailclip");
    zz.E_headclip = agfindedgeattr(zz, g, "headclip");
    zz.E_penwidth = agfindedgeattr(zz, g, "penwidth");
    
    
    /* background */
    GD_drawing(g).xdots = init_xdot (zz, g);
    
    
    /* initialize id, if any */
    
    if ((p = agget(zz, g, new CString("id")))!=null && p.charAt(0)!='\0')
	GD_drawing(g).id = strdup_and_subst_obj(p, g);
} finally {
LEAVING("8gzdr3oil2d0e2o7m84wsszfg","graph_init");
}
}







/* do_graph_label:
 * Set characteristics of graph label if it exists.
 * 
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/input.c", name="do_graph_label", key="5vks1zdadu5vjinaivs0j2bkb", definition="void do_graph_label(graph_t * sg)")
public static void do_graph_label(Globals zz, ST_Agraph_s  sg) {
ENTERING("5vks1zdadu5vjinaivs0j2bkb","do_graph_label");
try {
    CString str, pos, just;
    int pos_ix;
    
    /* it would be nice to allow multiple graph labels in the future */
    if ((str = agget(zz, sg, new CString("label")))!=null && (str.charAt(0) != '\0')) {
	char pos_flag=0;
	final ST_pointf dimen = new ST_pointf();
	
	GD_has_labels(sg.root, GD_has_labels(sg.root) | GRAPH_LABEL);
	
	GD_label(sg, make_label(zz, sg, str, (aghtmlstr(zz, str)!=0 ? LT_HTML : LT_NONE),
	    late_double(sg, (agattr(zz, sg,AGRAPH,new CString("fontsize"),null)),
			14.0, 1.0),
	    late_nnstring(sg, (agattr(zz, sg,AGRAPH,new CString("fontname"),null)),
			new CString("Times-Roman")),
	    late_nnstring(sg, (agattr(zz, sg,AGRAPH,new CString("fontcolor"),null)),
			new CString("black"))));
	
	/* set label position */
	pos = agget(zz, sg, new CString("labelloc"));
	if ((sg != agroot(sg))) {
	    if (pos!=null && (pos.charAt(0) == 'b'))
		pos_flag = LABEL_AT_BOTTOM;
	    else
		pos_flag = LABEL_AT_TOP;
	} else {
UNSUPPORTED("601b6yrqr391vnfpa74d7fec7"); // 	    if (pos && (pos[0] == 't'))
UNSUPPORTED("bxai2kktsidvda3696ctyk63c"); // 		pos_flag = 1;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("6m5sy5ew8izdy8i10zb5o2dvu"); // 		pos_flag = 0;
	}
	just = agget(zz, sg, new CString("labeljust"));
	if (just!=null) {
UNSUPPORTED("3gxohpfqzahytaf7f9apn58az"); // 	    if (just[0] == 'l')
UNSUPPORTED("ch7sydr4cg29o8ky9fbk5vnlg"); // 		pos_flag |= 2;
UNSUPPORTED("336to8kpmovx00pexhhenz74b"); // 	    else if (just[0] == 'r')
UNSUPPORTED("evu9w6pw3kkh7z8w7t4rx4qxc"); // 		pos_flag |= 4;
	}
	GD_label_pos(sg, pos_flag);
	
	
	if (sg == agroot(sg))
	    return;
	
	
	/* Set border information for cluster labels to allow space
	 */
	
	dimen.___(GD_label(sg).dimen);
	PAD(dimen);
	if (!GD_flip(agroot(sg))) {
	    if ((GD_label_pos(sg) & LABEL_AT_TOP)!=0)
		pos_ix = TOP_IX;
	    else
		pos_ix = BOTTOM_IX;
	    GD_border(sg)[pos_ix].___(dimen);
	} else {
	    /* when rotated, the labels will be restored to TOP or BOTTOM  */
	    if ((GD_label_pos(sg) & LABEL_AT_TOP)!=0)
		pos_ix = RIGHT_IX;
	    else
		pos_ix = LEFT_IX;
	    GD_border(sg)[pos_ix].x = dimen.y;
	    GD_border(sg)[pos_ix].y = dimen.x;	}
    }
} finally {
LEAVING("5vks1zdadu5vjinaivs0j2bkb","do_graph_label");
}
}





}
