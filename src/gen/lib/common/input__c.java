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
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
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
import static smetana.core.Macro.LOCAL;
import static smetana.core.Macro.LT_HTML;
import static smetana.core.Macro.LT_NONE;
import static smetana.core.Macro.MIN_NODESEP;
import static smetana.core.Macro.MYHUGE;
import static smetana.core.Macro.N;
import static smetana.core.Macro.NOCLUST;
import static smetana.core.Macro.NODENAME_ESC;
import static smetana.core.Macro.PAD;
import static smetana.core.Macro.POINTS;
import static smetana.core.Macro.RANKDIR_TB;
import static smetana.core.Macro.TOP_IX;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.agfindedgeattr;
import static smetana.core.Macro.agfindgraphattr;
import static smetana.core.Macro.agfindnodeattr;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.EN_fontname_kind;
import h.ST_Agraph_s;
import h.ST_layout_t;
import h.ST_pointf;
import smetana.core.CString;
import smetana.core.Z;

public class input__c {



/* getdoubles2ptf:
 * converts a graph attribute in inches to a pointf in points.
 * If only one number is given, it is used for both x and y.
 * Returns true if the attribute ends in '!'.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/input.c", name="getdoubles2ptf", key="72no6ayfvjinlnupyn5jlmayg", definition="static boolean getdoubles2ptf(graph_t * g, char *name, pointf * result)")
public static boolean getdoubles2ptf(ST_Agraph_s g, CString name, ST_pointf result) {
ENTERING("72no6ayfvjinlnupyn5jlmayg","getdoubles2ptf");
try {
    CString p;
    int i;
    double xf, yf;
    char c = '\0';
    boolean rv = false;
    if ((p = agget(g, name))!=null) {
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
public static void setRatio(ST_Agraph_s g) {
ENTERING("3bnmjpvynh1j9oh2p2vi0vh2m","setRatio");
try {
    CString p;
    char c;
    double ratio;
    if ((p = agget(g, new CString("ratio")))!=null && ((c = p.charAt(0))!='\0')) {
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
public static void graph_init(ST_Agraph_s g, boolean use_rankdir) {
ENTERING("8gzdr3oil2d0e2o7m84wsszfg","graph_init");
try {
    CString p;
    double xf;
    int rankdir;
    GD_drawing(g, new ST_layout_t());
    /* set this up fairly early in case any string sizes are needed */
    if ((p = agget(g, new CString("fontpath")))!=null || (p = getenv(new CString("DOTFONTPATH")))!=null) {
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
 	late_double(g, agfindgraphattr(g, "quantum"), 0.0, 0.0);
    
    /* setting rankdir=LR is only defined in dot,
     * but having it set causes shape code and others to use it. 
     * The result is confused output, so we turn it off unless requested.
     * This effective rankdir is stored in the bottom 2 bits of g->u.rankdir.
     * Sometimes, the code really needs the graph's rankdir, e.g., neato -n
     * with record shapes, so we store the real rankdir in the next 2 bits.
     */
    rankdir = RANKDIR_TB;
    if ((p = agget(g, new CString("rankdir")))!=null) {
UNSUPPORTED("sp7zcza7w0dn7t66aj8rf4wn"); // 	if ((*(p)==*("LR")&&!strcmp(p,"LR")))
UNSUPPORTED("bjd2vk1jssqehllmgnqv601qd"); // 	    rankdir = 1;
UNSUPPORTED("ry8itlrmblmuegdwk1iu1t0x"); // 	else if ((*(p)==*("BT")&&!strcmp(p,"BT")))
UNSUPPORTED("5hno0xn18yt443qg815w3c2s2"); // 	    rankdir = 2;
UNSUPPORTED("aal39mi047mhafrsrxoutcffk"); // 	else if ((*(p)==*("RL")&&!strcmp(p,"RL")))
UNSUPPORTED("7vlda224wrgcdhr0ts3mndh5q"); // 	    rankdir = 3;
    }
    if (use_rankdir)
	GD_rankdir2(g, (rankdir << 2) | rankdir);
    else
	GD_rankdir2(g, (rankdir << 2));
    
    xf = late_double(g, agfindgraphattr(g, "nodesep"),
    	DEFAULT_NODESEP, MIN_NODESEP);
    GD_nodesep(g, POINTS(xf));
    
    
    p = late_string(g, agfindgraphattr(g, "ranksep"), null);
    if (p!=null) {
    	xf = atof(p);
    	if (xf < 0.02)
    		xf = 0.02;

	if (p.isSame(new CString("equally")))
	    GD_exact_ranksep(g, 1);
    } else
	xf = DEFAULT_RANKSEP;
    GD_ranksep(g, POINTS(xf));
    
    
    GD_showboxes(g, late_int(g, agfindgraphattr(g, "showboxes"), 0, 0));
    p = late_string(g, agfindgraphattr(g, "fontnames"), null);
    GD_fontnames(g, maptoken(p, fontnamenames, fontnamecodes));
    
    
    setRatio(g);
    GD_drawing(g).filled = 
	getdoubles2ptf(g, new CString("size"), GD_drawing(g).size);
    getdoubles2ptf(g, new CString("page"), GD_drawing(g).page);
    
    
    GD_drawing(g).centered = mapbool(agget(g, new CString("center")));
    
    
    if ((p = agget(g, new CString("rotate")))!=null)
	GD_drawing(g).landscape= (atoi(p) == 90);
    else if ((p = agget(g, new CString("orientation")))!=null)
	GD_drawing(g).landscape= ((p.charAt(0) == 'l') || (p.charAt(0) == 'L'));
    else if ((p = agget(g, new CString("landscape")))!=null)
	GD_drawing(g).landscape= mapbool(p);
    
    
    p = agget(g, new CString("clusterrank"));
    Z.z().CL_type = maptoken(p, rankname, rankcode);
    p = agget(g, new CString("concentrate"));
    Z.z().Concentrate = mapbool(p);
    Z.z().State = GVBEGIN;
    Z.z().EdgeLabelsDone = 0;
    
    
    GD_drawing(g).dpi = 0.0;
    if (((p = agget(g, new CString("dpi")))!=null && p.charAt(0)!='\0')
	|| ((p = agget(g, new CString("resolution")))!=null && p.charAt(0)!='\0'))
	GD_drawing(g).dpi = atof(p);
    
    
    do_graph_label(g);
    
    
    Z.z().Initial_dist = MYHUGE;
    
    Z.z().G_ordering = agfindgraphattr(g, "ordering");
    Z.z().G_gradientangle = agfindgraphattr(g,"gradientangle");
    Z.z().G_margin = agfindgraphattr(g, "margin");

    /* initialize nodes */
    Z.z().N_height = agfindnodeattr(g, "height");
    Z.z().N_width = agfindnodeattr(g, "width");
    Z.z().N_shape = agfindnodeattr(g, "shape");
    Z.z().N_color = agfindnodeattr(g, "color");
    Z.z().N_fillcolor = agfindnodeattr(g, "fillcolor");
    Z.z().N_style = agfindnodeattr(g, "style");
    Z.z().N_fontsize = agfindnodeattr(g, "fontsize");
    Z.z().N_fontname = agfindnodeattr(g, "fontname");
    Z.z().N_fontcolor = agfindnodeattr(g, "fontcolor");
    Z.z().N_label = agfindnodeattr(g, "label");
    if (N(Z.z().N_label))
	Z.z().N_label = agattr(g, AGNODE, new CString("label"), new CString(NODENAME_ESC));
    Z.z().N_xlabel = agfindnodeattr(g, "xlabel");
    Z.z().N_showboxes = agfindnodeattr(g, "showboxes");
    Z.z().N_penwidth = agfindnodeattr(g, "penwidth");
    Z.z().N_ordering = agfindnodeattr(g, "ordering");
    Z.z().N_margin = agfindnodeattr(g, "margin");
    
    
    /* attribs for polygon shapes */
    Z.z().N_sides = agfindnodeattr(g, "sides");
    Z.z().N_peripheries = agfindnodeattr(g, "peripheries");
    Z.z().N_skew = agfindnodeattr(g, "skew");
    Z.z().N_orientation = agfindnodeattr(g, "orientation");
    Z.z().N_distortion = agfindnodeattr(g, "distortion");
    Z.z().N_fixed = agfindnodeattr(g, "fixedsize");
    Z.z().N_imagescale = agfindnodeattr(g, "imagescale");
    Z.z().N_nojustify = agfindnodeattr(g, "nojustify");
    Z.z().N_layer = agfindnodeattr(g, "layer");
    Z.z().N_group = agfindnodeattr(g, "group");
    Z.z().N_comment = agfindnodeattr(g, "comment");
    Z.z().N_vertices = agfindnodeattr(g, "vertices");
    Z.z().N_z = agfindnodeattr(g, "z");
    Z.z().N_gradientangle = agfindnodeattr(g,"gradientangle");
    
    
    /* initialize edges */
    Z.z().E_weight = agfindedgeattr(g, "weight");
    Z.z().E_color = agfindedgeattr(g, "color");
    Z.z().E_fillcolor = agfindedgeattr(g, "fillcolor");
    Z.z().E_fontsize = agfindedgeattr(g, "fontsize");
    Z.z().E_fontname = agfindedgeattr(g, "fontname");
    Z.z().E_fontcolor = agfindedgeattr(g, "fontcolor");
    Z.z().E_label = agfindedgeattr(g, "label");
    Z.z().E_xlabel = agfindedgeattr(g, "xlabel");
    Z.z().E_label_float = agfindedgeattr(g, "labelfloat");
    /* vladimir */
    Z.z().E_dir = agfindedgeattr(g, "dir");
    Z.z().E_arrowhead = agfindedgeattr(g, "arrowhead");
    Z.z().E_arrowtail = agfindedgeattr(g, "arrowtail");
    Z.z().E_headlabel = agfindedgeattr(g, "headlabel");
    Z.z().E_taillabel = agfindedgeattr(g, "taillabel");
    Z.z().E_labelfontsize = agfindedgeattr(g, "labelfontsize");
    Z.z().E_labelfontname = agfindedgeattr(g, "labelfontname");
    Z.z().E_labelfontcolor = agfindedgeattr(g, "labelfontcolor");
    Z.z().E_labeldistance = agfindedgeattr(g, "labeldistance");
    Z.z().E_labelangle = agfindedgeattr(g, "labelangle");
    /* end vladimir */
    Z.z().E_minlen = agfindedgeattr(g, "minlen");
    Z.z().E_showboxes = agfindedgeattr(g, "showboxes");
    Z.z().E_style = agfindedgeattr(g, "style");
    Z.z().E_decorate = agfindedgeattr(g, "decorate");
    Z.z().E_arrowsz = agfindedgeattr(g, "arrowsize");
    Z.z().E_constr = agfindedgeattr(g, "constraint");
    Z.z().E_layer = agfindedgeattr(g, "layer");
    Z.z().E_comment = agfindedgeattr(g, "comment");
    Z.z().E_tailclip = agfindedgeattr(g, "tailclip");
    Z.z().E_headclip = agfindedgeattr(g, "headclip");
    Z.z().E_penwidth = agfindedgeattr(g, "penwidth");
    
    
    /* background */
    GD_drawing(g).xdots = init_xdot (g);
    
    
    /* initialize id, if any */
    
    if ((p = agget(g, new CString("id")))!=null && p.charAt(0)!='\0')
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
public static void do_graph_label(ST_Agraph_s  sg) {
ENTERING("5vks1zdadu5vjinaivs0j2bkb","do_graph_label");
try {
    CString str, pos, just;
    int pos_ix;
    
    /* it would be nice to allow multiple graph labels in the future */
    if ((str = agget(sg, new CString("label")))!=null && (str.charAt(0) != '\0')) {
	char pos_flag=0;
	final ST_pointf dimen = new ST_pointf();
	
	GD_has_labels(sg.root, GD_has_labels(sg.root) | GRAPH_LABEL);
	
	GD_label(sg, make_label(sg, str, (aghtmlstr(str)!=0 ? LT_HTML : LT_NONE),
	    late_double(sg, (agattr(sg,AGRAPH,new CString("fontsize"),null)),
			14.0, 1.0),
	    late_nnstring(sg, (agattr(sg,AGRAPH,new CString("fontname"),null)),
			new CString("Times-Roman")),
	    late_nnstring(sg, (agattr(sg,AGRAPH,new CString("fontcolor"),null)),
			new CString("black"))));
	
	/* set label position */
	pos = agget(sg, new CString("labelloc"));
	if (NEQ(sg, agroot(sg))) {
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
	just = agget(sg, new CString("labeljust"));
	if (just!=null) {
UNSUPPORTED("3gxohpfqzahytaf7f9apn58az"); // 	    if (just[0] == 'l')
UNSUPPORTED("ch7sydr4cg29o8ky9fbk5vnlg"); // 		pos_flag |= 2;
UNSUPPORTED("336to8kpmovx00pexhhenz74b"); // 	    else if (just[0] == 'r')
UNSUPPORTED("evu9w6pw3kkh7z8w7t4rx4qxc"); // 		pos_flag |= 4;
	}
	GD_label_pos(sg, pos_flag);
	
	
	if (EQ(sg, agroot(sg)))
	    return;
	
	
	/* Set border information for cluster labels to allow space
	 */
	
	dimen.___(GD_label(sg).dimen);
	PAD(dimen);
	if (N(GD_flip(agroot(sg)))) {
	    if ((GD_label_pos(sg) & LABEL_AT_TOP)!=0)
		pos_ix = TOP_IX;
	    else
		pos_ix = BOTTOM_IX;
	    GD_border(sg)[pos_ix].___(dimen);
	} else {
	    /* when rotated, the labels will be restored to TOP or BOTTOM  */
UNSUPPORTED("cabz6xbjdvz5vmjulzrhlxh48"); // 	    if ((((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->label_pos) & 1)
UNSUPPORTED("dx7v6663o9o0x1j5r8z4wumxb"); // 		pos_ix = 1;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("97dtv6k7yw1qvfzgs65cj2v0l"); // 		pos_ix = 3;
UNSUPPORTED("21iuie8b11x65je8vampstgt6"); // 	    (((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->border)[pos_ix].x = dimen.y;
UNSUPPORTED("8cawl3kik853hkvgm39y34urs"); // 	    (((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->border)[pos_ix].y = dimen.x;
	}
    }
} finally {
LEAVING("5vks1zdadu5vjinaivs0j2bkb","do_graph_label");
}
}





}
