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
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.geom__c.ccwrotatepf;
import static gen.lib.common.geom__c.cwrotatepf;
import static gen.lib.common.htmltable__c.html_port;
import static gen.lib.common.labels__c.make_label;
import static gen.lib.common.utils__c.late_double;
import static gen.lib.common.utils__c.late_int;
import static gen.lib.common.utils__c.late_string;
import static gen.lib.common.utils__c.mapbool;
import static gen.lib.common.utils__c.safefile;
import static h.ST_pointf.pointfof;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.abs;
import static smetana.core.JUtils.atan2;
import static smetana.core.JUtils.cos;
import static smetana.core.JUtils.sin;
import static smetana.core.JUtils.sqrt;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtils.strlen;
import static smetana.core.Macro.BETWEEN;
import static smetana.core.Macro.BOTTOM;
import static smetana.core.Macro.DIST2;
import static smetana.core.Macro.FIXEDSHAPE;
import static smetana.core.Macro.GD_drawing;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_rankdir;
import static smetana.core.Macro.GD_realflip;
import static smetana.core.Macro.INSIDE;
import static smetana.core.Macro.LEFT;
import static smetana.core.Macro.LT_HTML;
import static smetana.core.Macro.LT_NONE;
import static smetana.core.Macro.MAX;
import static smetana.core.Macro.M_PI;
import static smetana.core.Macro.N;
import static smetana.core.Macro.ND_coord;
import static smetana.core.Macro.ND_has_port;
import static smetana.core.Macro.ND_height;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_shape;
import static smetana.core.Macro.ND_shape_info;
import static smetana.core.Macro.ND_width;
import static smetana.core.Macro.NOT;
import static smetana.core.Macro.PAD;
import static smetana.core.Macro.PF2P;
import static smetana.core.Macro.POINTS;
import static smetana.core.Macro.PS2INCH;
import static smetana.core.Macro.RADIANS;
import static smetana.core.Macro.RIGHT;
import static smetana.core.Macro.ROUND;
import static smetana.core.Macro.SQR;
import static smetana.core.Macro.SQRT2;
import static smetana.core.Macro.TOP;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.fabs;
import static smetana.core.Macro.hypot;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Doc;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.EN_shape_kind;
import h.ST_Agnode_s;
import h.ST_boxf;
import h.ST_field_t;
import h.ST_inside_t;
import h.ST_point;
import h.ST_pointf;
import h.ST_polygon_t;
import h.ST_port;
import h.ST_shape_desc;
import h.ST_textlabel_t;
import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CString;
import smetana.core.Memory;
import smetana.core.Z;

public class shapes__c {




//3 eb4jyrh981apg1fy13fczexdl
// static int same_side(pointf p0, pointf p1, pointf L0, pointf L1) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="same_side", key="eb4jyrh981apg1fy13fczexdl", definition="static int same_side(pointf p0, pointf p1, pointf L0, pointf L1)")
public static boolean same_side(final ST_pointf p0, final ST_pointf p1, final ST_pointf L0, final ST_pointf L1) {
// WARNING!! STRUCT
return same_side_w_(p0.copy(), p1.copy(), L0.copy(), L1.copy());
}
private static boolean same_side_w_(final ST_pointf p0, final ST_pointf p1, final ST_pointf L0, final ST_pointf L1) {
ENTERING("eb4jyrh981apg1fy13fczexdl","same_side");
try {
    boolean s0, s1;
    double a, b, c;
    /* a x + b y = c */
    a = -(L1.y - L0.y);
    b = L1.x - L0.x;
    c = a * L0.x + b * L0.y;
    s0 = a * p0.x + b * p0.y - c >= 0;
    s1 = a * p1.x + b * p1.y - c >= 0;
    return (s0 == s1);
} finally {
LEAVING("eb4jyrh981apg1fy13fczexdl","same_side");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/shapes.c", name="shapeOf", key="5gahokttzv65lspm84ao1le37", definition="shape_kind shapeOf(node_t * n)")
public static EN_shape_kind shapeOf(ST_Agnode_s n) {
ENTERING("5gahokttzv65lspm84ao1le37","shapeOf");
try {
	ST_shape_desc sh = (ST_shape_desc) ND_shape(n);
    CFunction ifn; //void (*ifn) (node_t *);
    
    if (N(sh))
	return EN_shape_kind.SH_UNSET;
    ifn = ND_shape(n).fns.initfn;
    if (ifn.getName().equals("poly_init"))
	return EN_shape_kind.SH_POLY;
    if (ifn.getName().equals("record_init"))
	return EN_shape_kind.SH_RECORD;
UNSUPPORTED("cpzx2lwu889clk2f1d0k4c9jd"); //     else if (ifn == point_init)
UNSUPPORTED("f4x4vap21dff1trk1lrzzb8u5"); // 	return SH_POINT;
UNSUPPORTED("alkskrmw3fjn82qi1t2kyi6uh"); //     else if (ifn == epsf_init)
UNSUPPORTED("5hp3oli47xj0s4fk7yj1dairi"); // 	return SH_EPSF;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("7if5cqgy6h2m78kwe6gagv7p"); // 	return SH_UNSET;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("5gahokttzv65lspm84ao1le37","shapeOf");
}
}




//3 e8riwo21j5t1g1tewsbo39z48
// boolean isPolygon(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="isPolygon", key="e8riwo21j5t1g1tewsbo39z48", definition="boolean isPolygon(node_t * n)")
public static Object isPolygon(Object... arg) {
UNSUPPORTED("6tth154tjxb21b1rr8m3w5lbj"); // boolean isPolygon(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9uh2xgxrn4veetmkfkbe9iq7y"); //     return (ND_shape(n) && (ND_shape(n)->fns->initfn == poly_init));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


public static CFunction poly_init = new CFunctionAbstract("poly_init") {
	
	public Object exe(Object... args) {
		poly_init((ST_Agnode_s)args[0]);
		return null;
	}};
@Reviewed(when = "13/11/2020")
@Difficult
@Original(version="2.38.0", path="lib/common/shapes.c", name="poly_init", key="a11xv6duihbr3d6gkgo2ye2j5", definition="static void poly_init(node_t * n)")
public static void poly_init(ST_Agnode_s n) {
ENTERING("a11xv6duihbr3d6gkgo2ye2j5","poly_init");
try {
    final ST_pointf dimen = new ST_pointf(), min_bb = new ST_pointf(), bb = new ST_pointf();
    final ST_point imagesize = new ST_point();
    final ST_pointf P = new ST_pointf(), Q = new ST_pointf(), R = new ST_pointf();
    CArray<ST_pointf> vertices = null;
    CString p, sfile, fxd;
    double temp, alpha, beta, gamma;
    double orientation, distortion, skew;
    double sectorangle, sidelength, skewdist, gdistortion, gskew;
    double angle, sinx, cosx, xmax=0, ymax=0, scalex, scaley;
    double width=0, height=0, marginx, marginy, spacex;
    boolean regular; int peripheries, sides;
    boolean isBox; int i, j, outp;
    ST_polygon_t poly = new ST_polygon_t();
    
    
    regular = ND_shape(n).polygon.regular;
    peripheries = ND_shape(n).polygon.peripheries;
    sides = ND_shape(n).polygon.sides;
    orientation = ND_shape(n).polygon.orientation;
    skew = ND_shape(n).polygon.skew;
    distortion = ND_shape(n).polygon.distortion;
    regular |= mapbool(agget(n, new CString("regular")));
    
    /* all calculations in floating point POINTS */

    /* make x and y dimensions equal if node is regular
     *   If the user has specified either width or height, use the max.
     *   Else use minimum default value.
     * If node is not regular, use the current width and height.
     */
    if (regular) {
UNSUPPORTED("637wuscax8bj094m73fwq3n00"); // 	double sz = userSize(n);
UNSUPPORTED("6wtpde1bxbwd70jz3vuemuvqe"); // 	if (sz > 0.0)
UNSUPPORTED("c4cn28kuy1f86y4d8rbxy1a4g"); // 	    width = height = sz;
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("mafr2nmpj8xgeranqldzxhtb"); // 	    width = (((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->width);
UNSUPPORTED("l2a1m5p66005ftc6gdbosugj"); // 	    height = (((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->height);
UNSUPPORTED("61qxt5l8ums7d9os9ungn3rao"); // 	    width = height = ((((((width)<(height)?(width):(height)))*72>=0)?(int)((((width)<(height)?(width):(height)))*72 + .5):(int)((((width)<(height)?(width):(height)))*72 - .5)));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    } else {
	width = POINTS(ND_width(n));
	height = POINTS(ND_height(n));
    }
    
    
    peripheries = late_int(n, Z.z().N_peripheries, peripheries, 0);
    orientation += late_double(n, Z.z().N_orientation, 0.0, -360.0);
    if (sides == 0) {		/* not for builtins */
UNSUPPORTED("dd178b113cb8130tl6q70lcp3"); // 	skew = late_double(n, N_skew, 0.0, -100.0);
UNSUPPORTED("cp83hdn3dp0a7rp9bauc3bgki"); // 	sides = late_int(n, N_sides, 4, 0);
UNSUPPORTED("abmuc3vqirf3i48480fj0k14g"); // 	distortion = late_double(n, N_distortion, 0.0, -100.0);
    }
    
    
    /* get label dimensions */
    dimen.___(ND_label(n).dimen);
    
    
    /* minimal whitespace around label */
    if (ROUND(abs(dimen.x))!=0 || ROUND(abs(dimen.y))!=0) {
    	/* padding */
	if ((p = agget(n, new CString("margin")))!=null) {
UNSUPPORTED("4dlqwm3pklzgz2e777dm56n03"); // 	    marginx = marginy = 0;
UNSUPPORTED("r186dwelv54pq63p2yo4czig"); // 	    i = sscanf(p, "%lf,%lf", &marginx, &marginy);
UNSUPPORTED("bjp5a2wbzhormf75ov5fumqto"); // 	    if (marginx < 0)
UNSUPPORTED("3tsws28ifjzq7ju8xs3ye4x18"); // 		marginx = 0;
UNSUPPORTED("7gfecwqgqof787z8u1mgh7qoj"); // 	    if (marginy < 0)
UNSUPPORTED("1xpqznf0i4ljd2b5j81ipsvtg"); // 		marginy = 0;
UNSUPPORTED("1lcx62wzgnn34tk5li0sgoqwm"); // 	    if (i > 0) {
UNSUPPORTED("dau1s6m0w92gp7bvqz3f63wnp"); // 		dimen.x += 2 * ((((marginx)*72>=0)?(int)((marginx)*72 + .5):(int)((marginx)*72 - .5)));
UNSUPPORTED("79iqlz01of88ftxysvivw2hgw"); // 		if (i > 1)
UNSUPPORTED("6opppos2hcjet4cn76130ykba"); // 		    dimen.y += 2 * ((((marginy)*72>=0)?(int)((marginy)*72 + .5):(int)((marginy)*72 - .5)));
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("8bwwxn4jop0urcsfnygjofg9s"); // 		    dimen.y += 2 * ((((marginx)*72>=0)?(int)((marginx)*72 + .5):(int)((marginx)*72 - .5)));
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("87bdwkkwbzyswxnepdd9bj8mb"); // 		{((dimen).x += 4*4); ((dimen).y += 2*4);};
	} else
	    PAD(dimen);
    }
    spacex = dimen.x - ND_label(n).dimen.x;
    
    
    /* quantization */
    if ((temp = GD_drawing(agraphof(n)).quantum) > 0.0) {
UNSUPPORTED("3nqb0s5rkwj3igt71vooj8asd"); // 	temp = ((((temp)*72>=0)?(int)((temp)*72 + .5):(int)((temp)*72 - .5)));
UNSUPPORTED("5fxtqwy8liyvnx1yvsou5hb4o"); // 	dimen.x = quant(dimen.x, temp);
UNSUPPORTED("et885f1jcqpske6ip856arouv"); // 	dimen.y = quant(dimen.y, temp);
    }
    
    
    imagesize.x = imagesize.y = 0;
    if (ND_shape(n).usershape) {
	/* custom requires a shapefile
	 * not custom is an adaptable user shape such as a postscript
	 * function.
	 */
UNSUPPORTED("7jbvoylyb27di8f54ufxj4mbk"); // 	if ((*((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->name)==*("custom")&&!strcmp((((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->name,"custom"))) {
UNSUPPORTED("cnfv2ayyl46ohdl5p4pc75swz"); // 	    sfile = agget(n, "shapefile");
UNSUPPORTED("6llro6gigojo2r8oo6c4k320o"); // 	    imagesize = gvusershape_size(agraphof(n), sfile);
UNSUPPORTED("3ngt3ika8ppq3m9vbgf2q5lu1"); // 	    if ((imagesize.x == -1) && (imagesize.y == -1)) {
UNSUPPORTED("5l8jenkv77ax02t47zzxyv1k0"); // 		agerr(AGWARN,
UNSUPPORTED("7hgyav5bbs1v4kts1oocozork"); // 		      "No or improper shapefile=\"%s\" for node \"%s\"\n",
UNSUPPORTED("34s5a4xy12nydt3idmis4np67"); // 		      (sfile ? sfile : "<nil>"), agnameof(n));
UNSUPPORTED("apwtbaz0akr5pg5p6uwcroaan"); // 		imagesize.x = imagesize.y = 0;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("4xiqgw4br039sl4r32gg5jv6k"); // 		(((Agraphinfo_t*)(((Agobj_t*)(agraphof(n)))->data))->has_images) = (!(0));
UNSUPPORTED("3nl4wsbxuqlad4tcg8vmg99rx"); // 		imagesize.x += 2;	/* some fixed padding */
UNSUPPORTED("da00oaavfp6fwqwgshhrodz2r"); // 		imagesize.y += 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    } else if ((sfile = agget(n, new CString("image")))!=null && (sfile.charAt(0) != '\0')) {
UNSUPPORTED("76t38i30025xohbzg8w9z4pn9"); // 	imagesize = gvusershape_size(agraphof(n), sfile);
UNSUPPORTED("b8spvmvtuxcciaejq8j1xhu5s"); // 	if ((imagesize.x == -1) && (imagesize.y == -1)) {
UNSUPPORTED("cw5grwj6gbj94jcztvnp2ooyj"); // 	    agerr(AGWARN,
UNSUPPORTED("68jdsrao22ymfpb8e1rdezyez"); // 		  "No or improper image=\"%s\" for node \"%s\"\n",
UNSUPPORTED("5lmxoq1rboqrxrchjf5ubg6w5"); // 		  (sfile ? sfile : "<nil>"), agnameof(n));
UNSUPPORTED("7wmzbnczyvj4oocepujtghrka"); // 	    imagesize.x = imagesize.y = 0;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("71mfu0uflnm85dbt8g2oxs9rd"); // 	    (((Agraphinfo_t*)(((Agobj_t*)(agraphof(n)))->data))->has_images) = (!(0));
UNSUPPORTED("286u48muwmjkomlzqufoqm5cw"); // 	    imagesize.x += 2;	/* some fixed padding */
UNSUPPORTED("1x57knvrmlciu7odfroo3paso"); // 	    imagesize.y += 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    
    
    /* initialize node bb to labelsize */
    bb.x = MAX(dimen.x, imagesize.x);
    bb.y = MAX(dimen.y, imagesize.y);
    
    
    /* I don't know how to distort or skew ellipses in postscript */
    /* Convert request to a polygon with a large number of sides */
    if ((sides <= 2) && ((distortion != 0.) || (skew != 0.))) {
	sides = 120;
    }
    
    
    /* extra sizing depends on if label is centered vertically */
    p = agget(n, new CString("labelloc"));
    if (p!=null && (p.charAt(0) == 't' || p.charAt(0) == 'b'))
    ND_label(n).valign = p.charAt(0);
    else
    ND_label(n).valign = 'c';
    
    
    
    isBox = (sides == 4 && (ROUND(orientation) % 90) == 0
	     && distortion == 0. && skew == 0.);
    if (isBox) {
	/* for regular boxes the fit should be exact */
    } else if (ND_shape(n).polygon.vertices!=null) {
UNSUPPORTED("4adqsyjwqwzs50ggjp57ok6u7"); // 	poly_desc_t* pd = (poly_desc_t*)(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->polygon->vertices;
UNSUPPORTED("1fjwgzo5xkijo98ycmzhal8yv"); // 	bb = pd->size_gen(bb);
    } else {
	/* for all other shapes, compute a smallest ellipse
	 * containing bb centered on the origin, and then pad for that.
	 * We assume the ellipse is defined by a scaling up of bb.
	 */
	temp = bb.y * SQRT2;
	if (height > temp && ND_label(n).valign == 'c') {
	    /* if there is height to spare
	     * and the label is centered vertically
	     * then just pad x in proportion to the spare height */
	    bb.x *= sqrt(1. / (1. - SQR(bb.y / height)));
	} else {
	    bb.x *= SQRT2;
	    bb.y = temp;
	}
	if (sides > 2) {
	    temp = cos(M_PI / sides);
	    bb.x /= temp;
	    bb.y /= temp;
	    /* FIXME - for odd-sided polygons, e.g. triangles, there
	       would be a better fit with some vertical adjustment of the shape */
	}
    }
    
    
    
    /* at this point, bb is the minimum size of node that can hold the label */
    min_bb.___(bb);
    
    
    /* increase node size to width/height if needed */
    fxd = late_string(n, Z.z().N_fixed, new CString("false"));
    if ((fxd.charAt(0) == 's') && (N(strcmp(fxd,new CString("shape"))))) {
	bb.x = width;
	bb.y = height;
	poly.option |= FIXEDSHAPE;
    } else if (mapbool(fxd)) {
	/* check only label, as images we can scale to fit */
	if ((width < ND_label(n).dimen.x) || (height < ND_label(n).dimen.y))
	    System.err.println(
		  "node '%s', graph '%s' size too small for label\n");
		  //agnameof(n), agnameof(agraphof(n)));
	bb.x = width;
	bb.y = height;
    } else {
	bb.x = width = MAX(width, bb.x);
	bb.y = height = MAX(height, bb.y);
    }
    
    
    /* If regular, make dimensions the same.
     * Need this to guarantee final node size is regular.
     */
    if (regular) {
	width = height = bb.x = bb.y = MAX(bb.x, bb.y);
    }
    
    
    /* Compute space available for label.  Provides the justification borders */
    if (N(mapbool(late_string(n, Z.z().N_nojustify, new CString("false"))))) {
	if (isBox) {
	    ND_label(n).space.x = MAX(dimen.x,bb.x) - spacex;
	}
	else if (dimen.y < bb.y) {
	    temp = bb.x * sqrt(1.0 - SQR(dimen.y) / SQR(bb.y));
	    ND_label(n).space.x = MAX(dimen.x,temp) - spacex;
        }
	else
	    ND_label(n).space.x = dimen.x - spacex;
    } else {
	ND_label(n).space.x = dimen.x - spacex;
    }
    
    
    if ((poly.option & FIXEDSHAPE) == 0) {
	temp = bb.y - min_bb.y;
	if (dimen.y < imagesize.y)
	    temp += imagesize.y - dimen.y;
	ND_label(n).space.y = dimen.y + temp;
    }
    
    
    outp = peripheries;
    if (peripheries < 1)
	outp = 1;
    if (sides < 3) {		/* ellipses */
	sides = 2;
	vertices = CArray.<ST_pointf> ALLOC__(outp * sides, ST_pointf.class);
	P.x = bb.x / 2.;
	P.y = bb.y / 2.;
	vertices.get__(0).x = -P.x;
	vertices.get__(0).y = -P.y;
	vertices.get__(1).___(P);
//	vertices.plus(1).x = P.x;
//	vertices.plus(1).y = P.y;
	if (peripheries > 1) {
UNSUPPORTED("4ofenmfgj7cgyf624qmugcx77"); // 	    for (j = 1, i = 2; j < peripheries; j++) {
UNSUPPORTED("458w3r6n3nidn2j2b154phpzt"); // 		P.x += 4;
UNSUPPORTED("24bcrwtjsfswpmtwxnadf0cn1"); // 		P.y += 4;
UNSUPPORTED("byrwdiqkace7e10l3pibk54wg"); // 		vertices[i].x = -P.x;
UNSUPPORTED("bicdgzzy9pdopb03hn9l48yns"); // 		vertices[i].y = -P.y;
UNSUPPORTED("en9fjm2thtauyxn9t7v4j2xgl"); // 		i++;
UNSUPPORTED("2pejlnamuvmi1m7339vzctpnb"); // 		vertices[i].x = P.x;
UNSUPPORTED("dy64x78vvm5nufbuxsn0bi8ng"); // 		vertices[i].y = P.y;
UNSUPPORTED("en9fjm2thtauyxn9t7v4j2xgl"); // 		i++;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6g6b8zsanvqyc953c4jh1j7iy"); // 	    bb.x = 2. * P.x;
UNSUPPORTED("b5z0hw4dacenv33xsaex70g8d"); // 	    bb.y = 2. * P.y;
	}
    } else {
/*
 * FIXME - this code is wrong - it doesn't work for concave boundaries.
 *          (e.g. "folder"  or "promoter")
 *   I don't think it even needs sectorangle, or knowledge of skewed shapes.
 *   (Concepts that only work for convex regular (modulo skew/distort) polygons.)
 *
 *   I think it only needs to know inside v. outside (by always drawing
 *   boundaries clockwise, say),  and the two adjacent segments.
 *
 *   It needs to find the point where the two lines, parallel to
 *   the current segments, and outside by GAP distance, intersect.   
 */
	vertices = CArray.<ST_pointf>ALLOC__(outp * sides, ST_pointf.class);
	if (ND_shape(n).polygon.vertices!=null) {
UNSUPPORTED("3ghle84ieryaenfnlbzrfv7bw"); // 	    poly_desc_t* pd = (poly_desc_t*)(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->shape)->polygon->vertices;
UNSUPPORTED("227lpcg9dt83m2bm8yshb4djf"); // 	    pd->vertex_gen (vertices, &bb);
UNSUPPORTED("cc3jvnwvbhjhro4adeet363yd"); // 	    xmax = bb.x/2;
UNSUPPORTED("39rdmp8vl9muqtv7xs1xwtrwk"); // 	    ymax = bb.y/2;
	} else {
	    sectorangle = 2. * M_PI / sides;
	    sidelength = sin(sectorangle / 2.);
	    skewdist = hypot(fabs(distortion) + fabs(skew), 1.);
	    gdistortion = distortion * SQRT2 / cos(sectorangle / 2.);
	    gskew = skew / 2.;
	    angle = (sectorangle - M_PI) / 2.;
	    sinx = sin(angle); cosx = cos(angle);
	    R.x = .5 * cosx;
	    R.y = .5 * sinx;
	    xmax = ymax = 0.;
	    angle += (M_PI - sectorangle) / 2.;
	    
	    
	    
	    for (i = 0; i < sides; i++) {
	    	
	    /*next regular vertex */
		angle += sectorangle;
	    sinx = sin(angle); cosx = cos(angle);
		R.x += sidelength * cosx;
		R.y += sidelength * sinx;
	    
	    
	    /*distort and skew */
		P.x = R.x * (skewdist + R.y * gdistortion) + R.y * gskew;
		P.y = R.y;
	    
	    
	    /*orient P.x,P.y */
		alpha = RADIANS(orientation) + atan2(P.y, P.x);
	    sinx = sin(alpha); cosx = cos(alpha);
		P.x = P.y = hypot(P.x, P.y);
		P.x *= cosx;
		P.y *= sinx;
	    
	    
	    /*scale for label */
		P.x *= bb.x;
		P.y *= bb.y;
	    
	    
	    /*find max for bounding box */
		xmax = MAX(fabs(P.x), xmax);
		ymax = MAX(fabs(P.y), ymax);
		
		
	    /* store result in array of points */
		vertices.get__(i).___(P);
		if (isBox) { /* enforce exact symmetry of box */
			vertices.get__(1).x = -P.x;
			vertices.get__(1).y = P.y;
			vertices.get__(2).x = -P.x;
			vertices.get__(2).y = -P.y;
			vertices.get__(3).x = P.x;
			vertices.get__(3).y = -P.y;
		    break;
		}
	    }
	}
	
	
	
	/* apply minimum dimensions */
	xmax *= 2.;
	ymax *= 2.;
	bb.x = MAX(width, xmax);
	bb.y = MAX(height, ymax);
	scalex = bb.x / xmax;
	scaley = bb.y / ymax;
	
	
	for (i = 0; i < sides; i++) {
	    P.___(vertices.get__(i));
	    P.x *= scalex;
	    P.y *= scaley;
	    vertices.get__(i).___(P);
	}
	if (peripheries > 1) {
UNSUPPORTED("3x6t3unoi91ezbh3iz168cm2t"); // 	    Q = vertices[(sides - 1)];
UNSUPPORTED("8cm8js7jdmpakzujw3wo4h6jk"); // 	    R = vertices[0];
UNSUPPORTED("5zpv8twf25wr8n71ql3lh8ku2"); // 	    beta = atan2(R.y - Q.y, R.x - Q.x);
UNSUPPORTED("9mlrumbikcvketd18jx1ox7k7"); // 	    for (i = 0; i < sides; i++) {
UNSUPPORTED("gkm8nb6f6ispdzj0ausiv1fe"); // 		/*for each vertex find the bisector */
UNSUPPORTED("8tp61rvblb9bmqfwgyknlk906"); // 		P = Q;
UNSUPPORTED("1ls3xc8rwvn3763c32mx1wzsd"); // 		Q = R;
UNSUPPORTED("1zrelve2mvbnzah086dkomf6k"); // 		R = vertices[(i + 1) % sides];
UNSUPPORTED("bgx8ee996r89memnp0ea0b80m"); // 		alpha = beta;
UNSUPPORTED("5p9jzpcd51evtwqyugnwk50vf"); // 		beta = atan2(R.y - Q.y, R.x - Q.x);
UNSUPPORTED("dwskcoivmu9pc5kth75x0ersl"); // 		gamma = (alpha + 3.14159265358979323846 - beta) / 2.;
UNSUPPORTED("cmm5tvlcafe2aso9bkk3kl7of"); // 		/*find distance along bisector to */
UNSUPPORTED("bebwurfm1a1h1bywf9kf5ueug"); // 		/*intersection of next periphery */
UNSUPPORTED("1mtgr15b978d0tdunbpj2pkdp"); // 		temp = 4 / sin(gamma);
UNSUPPORTED("28mxt6c4230xruf63s6u415y9"); // 		/*convert this distance to x and y */
UNSUPPORTED("1fxw0fz2b6iq6p6qy58mx9mwu"); // 		*&sinx = sin((alpha - gamma)); *&cosx = cos((alpha - gamma));
UNSUPPORTED("8j7vx250v0icumolzos2p5qa8"); // 		sinx *= temp;
UNSUPPORTED("35ed1mrpnziq164g6cg4stt5w"); // 		cosx *= temp;
UNSUPPORTED("eu3ptwi3s2200v4253yk1x69t"); // 		/*save the vertices of all the */
UNSUPPORTED("7lc5jxgzj6z4lq7sd9y2b6vex"); // 		/*peripheries at this base vertex */
UNSUPPORTED("86nzalouete6viryy967d5g9u"); // 		for (j = 1; j < peripheries; j++) {
UNSUPPORTED("dautpj9jyj2qwa8jpujdh3436"); // 		    Q.x += cosx;
UNSUPPORTED("7p6tl6s20kdual1ysfoxl8wku"); // 		    Q.y += sinx;
UNSUPPORTED("1yw9xq85ss81cogn9jrg24ojc"); // 		    vertices[i + j * sides] = Q;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("9mlrumbikcvketd18jx1ox7k7"); // 	    for (i = 0; i < sides; i++) {
UNSUPPORTED("aa5s79go4kwos2as72rcsdrxf"); // 		P = vertices[i + (peripheries - 1) * sides];
UNSUPPORTED("7ee9ageu4efyramsg9jn6klpb"); // 		bb.x = ((2. * fabs(P.x))>(bb.x)?(2. * fabs(P.x)):(bb.x));
UNSUPPORTED("lklvdmn7xiqbxhpgdeufcvjj"); // 		bb.y = ((2. * fabs(P.y))>(bb.y)?(2. * fabs(P.y)):(bb.y));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
	}
    }
    poly.regular = regular;
    poly.peripheries = peripheries;
    poly.sides = sides;
    poly.orientation = orientation;
    poly.skew = skew;
    poly.distortion = distortion;
    poly.vertices = vertices;
    
    
    if ((poly.option & FIXEDSHAPE)!=0) {
	/* set width and height to reflect label and shape */
UNSUPPORTED("7kk8oru3b3copylmq3gssx6qx"); // 	(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->width) = ((((dimen.x)>(bb.x)?(dimen.x):(bb.x)))/(double)72);
UNSUPPORTED("8oouzms2x039fhfcfxm7yc4su"); // 	(((Agnodeinfo_t*)(((Agobj_t*)(n))->data))->height) = ((((dimen.y)>(bb.y)?(dimen.y):(bb.y)))/(double)72);
    } else {
	ND_width(n, PS2INCH(bb.x));
	ND_height(n, PS2INCH(bb.y));
    }
    ND_shape_info(n, poly);
} finally {
LEAVING("a11xv6duihbr3d6gkgo2ye2j5","poly_init");
}
}



public static CFunction poly_free = new CFunctionAbstract("poly_free") {
	
	public Object exe(Object... args) {
		return poly_free(args);
	}};
//3 63sj12avbdw6e27zf3sedls1r
// static void poly_free(node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="poly_free", key="63sj12avbdw6e27zf3sedls1r", definition="static void poly_free(node_t * n)")
public static Object poly_free(Object... arg_) {
UNSUPPORTED("cfl0ro4734avs9rtdlar7nbg8"); // static void poly_free(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5ha1ng0rlvg0kiui0qhgme4nb"); //     polygon_t *p = ND_shape_info(n);
UNSUPPORTED("3cvmixd2u1g2d9l03kuxyyxxw"); //     if (p) {
UNSUPPORTED("3cjbelr7499ch9kn6lbjaz7l7"); // 	free(p->vertices);
UNSUPPORTED("cy5x5dma0v4hiepir7lrfuo17"); // 	free(p);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}





public static CFunction poly_inside = new CFunctionAbstract("poly_inside") {
	
	public Object exe(Object... args) {
		return poly_inside((ST_inside_t)args[0], (ST_pointf)args[1]);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="poly_inside", key="570t4xovyyfqipaikkf63crmk", definition="tatic boolean poly_inside(inside_t * inside_context, pointf p)")
public static boolean poly_inside(ST_inside_t inside_context, ST_pointf p) {
// WARNING!! STRUCT
return poly_inside_w_(inside_context, p.copy());
}
private static boolean poly_inside_w_(ST_inside_t inside_context, final ST_pointf p) {
ENTERING("570t4xovyyfqipaikkf63crmk","poly_inside");
try {
    int i, i1, j;
    boolean s;
    final ST_pointf P = new ST_pointf(), Q = new ST_pointf(), R = new ST_pointf();
    ST_boxf bp = inside_context.s_bp;
    ST_Agnode_s n = inside_context.s_n;
    P.___(ccwrotatepf(p, 90 * GD_rankdir(agraphof(n))));
    /* Quick test if port rectangle is target */
    if (bp!=null) {
	final ST_boxf bbox = new ST_boxf();
	bbox.___(bp);
	return INSIDE(P, bbox);
    }
    if (NEQ(n, Z.z().lastn)) {
	double n_width = 0, n_height = 0;
	Z.z().poly = (ST_polygon_t) ND_shape_info(n);
	Z.z().vertex = Z.z().poly.vertices;
	Z.z().sides = Z.z().poly.sides;
	if ((Z.z().poly.option & (1 << 11))!=0) {
UNSUPPORTED("18yw1scg4sol8bhyf1vedj9kn"); // 	   boxf bb = polyBB(poly); 
UNSUPPORTED("7rz7vxyxao0efec2nvd6g19m1"); // 	    n_width = bb.UR.x - bb.LL.x;
UNSUPPORTED("4h0k2wroz3xqx1ljokdbaqaad"); // 	    n_height = bb.UR.y - bb.LL.y;
UNSUPPORTED("dgykcjw02yoka8uz5b7jdc2ct"); // 	    /* get point and node size adjusted for rankdir=LR */
UNSUPPORTED("75jifr4aucrxp2hvnsrcfunej"); // 	    if (GD_flip(agraphof(n))) {
UNSUPPORTED("e53876tm7q1oasuu013njtgx"); // 		ysize = n_width;
UNSUPPORTED("7wnmmcv8dfzi1bdwml4vcxf0w"); // 		xsize = n_height;
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("10ux82vu0kynxilmf6ak7x70q"); // 		xsize = n_width;
UNSUPPORTED("5xao1mdiugxzaq03na34mbl5w"); // 		ysize = n_height;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
	} else {
	    /* get point and node size adjusted for rankdir=LR */
	    if (GD_flip(agraphof(n))) {
UNSUPPORTED("dapvd4c0ggliaqcj08jvao221"); // 		ysize = ND_lw(n) + ND_rw(n);
UNSUPPORTED("8t3g4d9acruono62leh5a8hxh"); // 		xsize = ND_ht(n);
	    } else {
		Z.z().xsize = ND_lw(n) + ND_rw(n);
		Z.z().ysize = ND_ht(n);
	    }
	    n_width = (ROUND((ND_width(n))*72));
	    n_height = (ROUND((ND_height(n))*72));
	}
	/* scale */
	if (Z.z().xsize == 0.0)
	    Z.z().xsize = 1.0;
	if (Z.z().ysize == 0.0)
	    Z.z().ysize = 1.0;
	Z.z().scalex = n_width / Z.z().xsize;
	Z.z().scaley = n_height / Z.z().ysize;
	Z.z().box_URx = n_width / 2.0;
	Z.z().box_URy = n_height / 2.0;
	/* index to outer-periphery */
	Z.z().outp = (Z.z().poly.peripheries - 1) * Z.z().sides;
	if (Z.z().outp < 0)
	    Z.z().outp = 0;
	Z.z().lastn = (ST_Agnode_s) n;
    }
    /* scale */
    P.x = (P.x * Z.z().scalex);
    P.y = (P.y * Z.z().scaley);
    /* inside bounding box? */
    if ((fabs(P.x) > Z.z().box_URx) || (fabs(P.y) > Z.z().box_URy))
	return false;
    /* ellipses */
    if (Z.z().sides <= 2)
	return (hypot(P.x / Z.z().box_URx, P.y / Z.z().box_URy) < 1.);
    
    /* use fast test in case we are converging on a segment */
    i = Z.z().last % Z.z().sides;		/* in case last left over from larger polygon */
    i1 = (i + 1) % Z.z().sides;
    Q.___(Z.z().vertex.get__(i + Z.z().outp));
    R.___(Z.z().vertex.get__(i1 + Z.z().outp));
    if (N(same_side(P, Z.z().O, Q, R)))   /* false if outside the segment's face */
	return false;
    /* else inside the segment face... */
    if ((s = same_side(P, Q, R, Z.z().O)) && (same_side(P, R, Z.z().O, Q))) /* true if between the segment's sides */
	return NOT(0);
    /* else maybe in another segment */
    for (j = 1; j < Z.z().sides; j++) { /* iterate over remaining segments */
	if (s) { /* clockwise */
	    i = i1;
	    i1 = (i + 1) % Z.z().sides;
	} else { /* counter clockwise */
	    i1 = i;
	    i = (i + Z.z().sides - 1) % Z.z().sides;
	}
	if (N(same_side(P, Z.z().O, Z.z().vertex.get__(i + Z.z().outp), Z.z().vertex.get__(i1 + Z.z().outp)))) { /* false if outside any other segment's face */
	    Z.z().last = i;
	    return false;
	}
    }
    /* inside all segments' faces */
    Z.z().last = i;			/* in case next edge is to same side */
    return NOT(0);
} finally {
LEAVING("570t4xovyyfqipaikkf63crmk","poly_inside");
}
}



public static CFunction poly_path = new CFunctionAbstract("poly_path") {
	
	public Object exe(Object... args) {
		return poly_path((ST_Agnode_s)args[0], (ST_port)args[1], (Integer)args[2], (Object)args[3], (Object)args[4]);
	}};
//3 5mmuhvq40xadw0g9mzlauyztq
// static int poly_path(node_t * n, port * p, int side, boxf rv[], int *kptr) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="poly_path", key="5mmuhvq40xadw0g9mzlauyztq", definition="static int poly_path(node_t * n, port * p, int side, boxf rv[], int *kptr)")
public static int poly_path(ST_Agnode_s n, ST_port p, int side, Object rv, Object kptr) {
ENTERING("5mmuhvq40xadw0g9mzlauyztq","poly_path");
try {
    side = 0;
    if (ND_label(n).html && ND_has_port(n)) {
UNSUPPORTED("67g7bthntnw8syb6zd03ueg84"); // 	side = html_path(n, p, side, rv, kptr);
    }
    return side;
} finally {
LEAVING("5mmuhvq40xadw0g9mzlauyztq","poly_path");
}
}




//3 857i3hwbu9mbq4nwur2q7e7er
// static int invflip_side(int side, int rankdir) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="invflip_side", key="857i3hwbu9mbq4nwur2q7e7er", definition="static int invflip_side(int side, int rankdir)")
public static int invflip_side(int side, int rankdir) {
    switch (rankdir) {
    case 0:
	break;
    case 2:
UNSUPPORTED("o4wjkq58uh9dgs94m2vxettc"); // 	switch (side) {
UNSUPPORTED("a0zo28ne6fq7qm9hko3jwrsie"); // 	case (1<<2):
UNSUPPORTED("asl0z4i3qt99vpfphpr7hpk5"); // 	    side = 1<<0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5uxczmgv9jelovrky9lyqmqxn"); // 	case (1<<0):
UNSUPPORTED("aj9jgzaslnfuc2iy41yo6577i"); // 	    side = 1<<2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    case 1:
UNSUPPORTED("o4wjkq58uh9dgs94m2vxettc"); // 	switch (side) {
UNSUPPORTED("a0zo28ne6fq7qm9hko3jwrsie"); // 	case (1<<2):
UNSUPPORTED("csyxlzh6yvg14dkwm5h0q8l4e"); // 	    side = 1<<1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5uxczmgv9jelovrky9lyqmqxn"); // 	case (1<<0):
UNSUPPORTED("6ob9sb98jfamphtvv99f9nny7"); // 	    side = 1<<3;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3vvicpwbia6xzcxsn2qnkbzq8"); // 	case (1<<3):
UNSUPPORTED("aj9jgzaslnfuc2iy41yo6577i"); // 	    side = 1<<2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("ad90yo3mu0ffjurb9egult4pi"); // 	case (1<<1):
UNSUPPORTED("asl0z4i3qt99vpfphpr7hpk5"); // 	    side = 1<<0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    case 3:
UNSUPPORTED("o4wjkq58uh9dgs94m2vxettc"); // 	switch (side) {
UNSUPPORTED("a0zo28ne6fq7qm9hko3jwrsie"); // 	case (1<<2):
UNSUPPORTED("csyxlzh6yvg14dkwm5h0q8l4e"); // 	    side = 1<<1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5uxczmgv9jelovrky9lyqmqxn"); // 	case (1<<0):
UNSUPPORTED("6ob9sb98jfamphtvv99f9nny7"); // 	    side = 1<<3;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3vvicpwbia6xzcxsn2qnkbzq8"); // 	case (1<<3):
UNSUPPORTED("asl0z4i3qt99vpfphpr7hpk5"); // 	    side = 1<<0;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("ad90yo3mu0ffjurb9egult4pi"); // 	case (1<<1):
UNSUPPORTED("aj9jgzaslnfuc2iy41yo6577i"); // 	    side = 1<<2;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    }
    return side;
}




//3 72pzdmirzds0yer4ks1ooxvic
// static double invflip_angle(double angle, int rankdir) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="invflip_angle", key="72pzdmirzds0yer4ks1ooxvic", definition="static double invflip_angle(double angle, int rankdir)")
public static double invflip_angle(double angle, int rankdir) {
    switch (rankdir) {
    case 0:
	break;
    case 2:
UNSUPPORTED("e7qgsf2gzf7fv8r5lpdfqp2gp"); // 	angle *= -1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    case 1:
UNSUPPORTED("b5wrpw5rvhjh7999v3sqqlbo3"); // 	angle -= M_PI * 0.5;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    case 3:
UNSUPPORTED("536aocvem6ko7h9t50pllxla0"); // 	if (angle == M_PI)
UNSUPPORTED("kxow9q31jmisg5yv60fj9z3g"); // 	    angle = -0.5 * M_PI;
UNSUPPORTED("3hy3z7oxc494l61va60rwh9k3"); // 	else if (angle == M_PI * 0.75)
UNSUPPORTED("76t0zkyxc3q2wnpcajih9mf65"); // 	    angle = -0.25 * M_PI;
UNSUPPORTED("bd02ns5pweyapa70g9ozio3m4"); // 	else if (angle == M_PI * 0.5)
UNSUPPORTED("a0pp5xd6lligtfp0riunw38t3"); // 	    angle = 0;
UNSUPPORTED("8cqf9j5edmb4u2xnd8lkahkht"); // /* clang complains about self assignment of double
UNSUPPORTED("205i7xisgiaz1vhn9p93tsw5a"); // 	else if (angle == M_PI * 0.25)
UNSUPPORTED("76g7hlyzy67q9n7p5l89y4gxw"); // 	    angle = angle;
UNSUPPORTED("e5xwyhh2l2jm6g9w2ofnktaf6"); //  */
UNSUPPORTED("8pqjflzypl5wbdev1h4r6ee0e"); // 	else if (angle == 0)
UNSUPPORTED("3uy8u4gjki2ksohuj3gn6ewkj"); // 	    angle = M_PI * 0.5;
UNSUPPORTED("bqlwd51jj33yedz7tuck5hukd"); // 	else if (angle == M_PI * -0.25)
UNSUPPORTED("3s431nqj2tfm95djdmjfjig6h"); // 	    angle = M_PI * 0.75;
UNSUPPORTED("tl121swu8uuow1dlzumo1pyi"); // 	else if (angle == M_PI * -0.5)
UNSUPPORTED("aa92obzwij392if7nnjch6dtz"); // 	    angle = M_PI;
UNSUPPORTED("8cqf9j5edmb4u2xnd8lkahkht"); // /* clang complains about self assignment of double
UNSUPPORTED("2waz5md3krpirny5m7gagynkc"); // 	else if (angle == M_PI * -0.75)
UNSUPPORTED("76g7hlyzy67q9n7p5l89y4gxw"); // 	    angle = angle;
UNSUPPORTED("e5xwyhh2l2jm6g9w2ofnktaf6"); //  */
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    }
    return angle;
}




//3 54t5x4hsq6ie4hn83dix0fi3g
// static pointf compassPoint(inside_t * ictxt, double y, double x) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="compassPoint", key="54t5x4hsq6ie4hn83dix0fi3g", definition="static pointf compassPoint(inside_t * ictxt, double y, double x)")
public static Object compassPoint(Object... arg) {
UNSUPPORTED("1owp098dshhw9x2d86x61ho3n"); // static pointf compassPoint(inside_t * ictxt, double y, double x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5jw267n0iigspndf3p51uuoyt"); //     pointf curve[4];		/* bezier control points for a straight line */
UNSUPPORTED("2ol68djy9gbphj8kdfml5q1ej"); //     node_t *n = ictxt->s.n;
UNSUPPORTED("f121hhzfkpb97hn84g46lhxdh"); //     graph_t* g = agraphof(n);
UNSUPPORTED("347leky6wh51yiydoij5od0h2"); //     int rd = GD_rankdir(g);
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("saqn1396zzjkeo01vp1tskia"); //     p.x = x;
UNSUPPORTED("5jdhcgi82gtmvn690v78zmkpe"); //     p.y = y;
UNSUPPORTED("2imvfuepadgxdlfwq3qmsatju"); //     if (rd)
UNSUPPORTED("8gcpvoawmbrjuiq80lglpl2bn"); // 	p = cwrotatepf(p, 90 * rd);
UNSUPPORTED("b4ktwkbs8awubvwfgfeqzhlx0"); //     curve[0].x = curve[0].y = 0;
UNSUPPORTED("dcqc3vt7dwuvg73lixbbwd3dj"); //     curve[1] = curve[0];
UNSUPPORTED("ahj7ruzql6g6cm5nvomizsgcz"); //     curve[3] = curve[2] = p;
UNSUPPORTED("6wkk7v0v7iyai22oyhq16dcno"); //     bezier_clip(ictxt, ND_shape(n)->fns->insidefn, curve, 1);
UNSUPPORTED("2imvfuepadgxdlfwq3qmsatju"); //     if (rd)
UNSUPPORTED("ip6d55dog3nmeksqauqb1fyo"); // 	curve[0] = ccwrotatepf(curve[0], 90 * rd);
UNSUPPORTED("7jlv4v811jdfr56u2h3wdxxbm"); //     return curve[0];
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4oj0c3dwqqjei7u5u2ik9yyw1
// static int compassPort(node_t * n, boxf * bp, port * pp, char *compass, int sides, 	    inside_t * ictxt) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="compassPort", key="4oj0c3dwqqjei7u5u2ik9yyw1", definition="static int compassPort(node_t * n, boxf * bp, port * pp, char *compass, int sides, 	    inside_t * ictxt)")
public static boolean compassPort(ST_Agnode_s n, ST_boxf bp, ST_port pp, CString compass, int sides, ST_inside_t ictxt) {
    final ST_boxf b = new ST_boxf();
    final ST_pointf p = new ST_pointf(), ctr = new ST_pointf();
    int rv = 0;
    double theta = 0.0;
    boolean constrain = false;
    boolean dyna = false;
    int side = 0;
    boolean clip = NOT(0);
    boolean defined = false;
    double maxv;  /* sufficiently large value outside of range of node */
    if (bp!=null) {
	b.___(bp);
	p.___(pointfof((b.LL.x + b.UR.x) / 2, (b.LL.y + b.UR.y) / 2));
	defined = true;
    } else {
	p.x = p.y = 0.;
	if (GD_flip(agraphof(n))) {
UNSUPPORTED("e21k9f24vr25zdbgo37m5er48"); // 	    b.UR.x = ND_ht(n) / 2.;
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("7luuqd8n7bpffoa8v27jp7tn3"); // 	    b.UR.y = ND_lw(n);
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
	} else {
	    b.UR.y = ND_ht(n) / 2.;
	    b.LL.y = -b.UR.y;
	    b.UR.x = ND_lw(n);
	    b.LL.x = -b.UR.x;
	}
	defined = false;
    }
    maxv = MAX(b.UR.x,b.UR.y);
    maxv *= 4.0;
    ctr.___(p);
    if (compass!=null && compass.charAt(0)!=0) {
	final char TMP =compass.charAt(0);
	compass = compass.plus_(1);
    switch (TMP) {
	case 'e':
	    if (compass.charAt(0)!=0)
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
	    else {
                if (ictxt!=null)
UNSUPPORTED("8whok6jl4olniblvibxhrbbre"); //                     p = compassPoint(ictxt, ctr.y, maxv);
                else
		    p.x = b.UR.x;
		theta = 0.0;
		constrain = NOT(0);
		defined = NOT(0);
		clip = false;
		side = sides & RIGHT;
	    }
	    break;
	case 's':
	    p.y = b.LL.y;
	    constrain = NOT(0);
	    clip = false;
	    switch (compass.charAt(0)) {
	    case '\0':
		theta = -M_PI * 0.5;
		defined = NOT(0);
                if (ictxt!=null)
UNSUPPORTED("2iohu3tvlkzx2emq04ycxkhta"); //                     p = compassPoint(ictxt, -maxv, ctr.x);
                else
                    p.x = ctr.x;
		side = sides & BOTTOM;
		break;
	    case 'e':
UNSUPPORTED("avfplp4wadl774qo2yrqn2btg"); // 		theta = -M_PI * 0.25;
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("4qnqhz6577yhq6u9919ve4tjb"); // 		    p = compassPoint(ictxt, -maxv, maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("5f4jye7znkk6hbv6lv0l9l0hs"); // 		    p.x = b.UR.x;
UNSUPPORTED("b0weojc8y88qjfkoujifnu9ag"); // 		side = sides & ((1<<0) | (1<<1));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	    case 'w':
UNSUPPORTED("a6j042vifpt4pgkwczny2dy24"); // 		theta = -M_PI * 0.75;
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("c0hdr34iyaygjxcr6a65hns2g"); // 		    p = compassPoint(ictxt, -maxv, -maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("e2vcgqbz5sfyjwfyadlmm3s7n"); // 		    p.x = b.LL.x;
UNSUPPORTED("9yg4wc52hqtj6s3orou0nnbq4"); // 		side = sides & ((1<<0) | (1<<3));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("bt2g0yhsy3c7keqyftf3c98ut"); // 	    default:
UNSUPPORTED("c8if0ggdrakzyxyn4fwlc8z2j"); // 		p.y = ctr.y;
UNSUPPORTED("30qndpdx39k6rmlgid0k16w53"); // 		constrain = 0;
UNSUPPORTED("2uxoapmd0p84jvg4utlai18nj"); // 		clip = NOT(0);
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	    }
	    break;
	case 'w':
	    if (compass.charAt(0)!=0)
		rv = 1;
	    else {
                if (ictxt!=null)
UNSUPPORTED("dkdxl90pni5x4m9rsi9l4fkml"); //                     p = compassPoint(ictxt, ctr.y, -maxv);
                else
		    p.x = b.LL.x;
		theta = M_PI;
		constrain = true;
		defined = true;
		clip = false;
		side = sides & LEFT;
	    }
	    break;
	case 'n':
	    p.y = b.UR.y;
	    constrain = NOT(0);
	    clip = false;
	    switch (compass.charAt(0)) {
	    case '\0':
		defined = NOT(0);
		theta = M_PI * 0.5;
                if (ictxt!=null)
UNSUPPORTED("6l60lhko2eg8jry5mf4wpknho"); //                     p = compassPoint(ictxt, maxv, ctr.x);
                else
                    p.x = ctr.x;
		side = sides & TOP;
		break;
	    case 'e':
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("dpfvfzmxj8yxv0s9b2jrvy1dt"); // 		theta = M_PI * 0.25;
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("eaiok8sr9qt2m9t35bj1n33vk"); // 		    p = compassPoint(ictxt, maxv, maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("5f4jye7znkk6hbv6lv0l9l0hs"); // 		    p.x = b.UR.x;
UNSUPPORTED("7eo9yj1faco0zq3n56ljnckjl"); // 		side = sides & ((1<<2) | (1<<1));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	    case 'w':
UNSUPPORTED("bfouf47misaa32ulv25melpbm"); // 		defined = NOT(0);
UNSUPPORTED("b4rydjq1y842ljagzj3esvilf"); // 		theta = M_PI * 0.75;
UNSUPPORTED("e1jqt6v7gkr0w7anohkdvwzuz"); // 		if (ictxt)
UNSUPPORTED("aftpsq12rdaiypy81n10uki6g"); // 		    p = compassPoint(ictxt, maxv, -maxv);
UNSUPPORTED("7e1uy5mzei37p66t8jp01r3mk"); // 		else
UNSUPPORTED("e2vcgqbz5sfyjwfyadlmm3s7n"); // 		    p.x = b.LL.x;
UNSUPPORTED("46gsms8looi57wty5vza2s5el"); // 		side = sides & ((1<<2) | (1<<3));
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	    default:
UNSUPPORTED("c8if0ggdrakzyxyn4fwlc8z2j"); // 		p.y = ctr.y;
UNSUPPORTED("30qndpdx39k6rmlgid0k16w53"); // 		constrain = 0;
UNSUPPORTED("2uxoapmd0p84jvg4utlai18nj"); // 		clip = NOT(0);
UNSUPPORTED("en0rarvkx5srsxnlqpf6ja1us"); // 		rv = 1;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	    }
	    break;
	case '_':
	    dyna = NOT(0);
	    side = sides;
	    break;
	case 'c':
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
	default:
	    rv = 1;
	    break;
	}
    }
    p.___(cwrotatepf(p, 90 * GD_rankdir(agraphof(n))));
    if (dyna)
	pp.side = side;
    else
	pp.side = invflip_side(side, GD_rankdir(agraphof(n)));
    pp.bp = bp;
    PF2P(p, pp.p);
    pp.theta = invflip_angle(theta, GD_rankdir(agraphof(n)));
    if ((p.x == 0) && (p.y == 0))
	pp.order = 256 / 2;
    else {
	/* compute angle with 0 at north pole, increasing CCW */
	double angle = atan2(p.y, p.x) + 1.5 * M_PI;
	if (angle >= 2 * M_PI)
	    angle -= 2 * M_PI;
	pp.order = (int) ((256 * angle) / (2 * M_PI));
    }
    pp.constrained = constrain;
    pp.defined = defined;
    pp.clip = clip;
    pp.dyna = dyna;
    return rv != 0;
}



@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="unrecognized", key="a7copj498to9ai2kxtg728mex", definition="static void unrecognized(node_t * n, char *p)")
public static void unrecognized(ST_Agnode_s n, CString p) {
ENTERING("a7copj498to9ai2kxtg728mex","unrecognized");
try {
System.err.println("node %s, port %s unrecognized "+p);
}
finally {
LEAVING("a7copj498to9ai2kxtg728mex","unrecognized");
}
}

public static CFunction poly_port = new CFunctionAbstract("poly_port") {
	
	public Object exe(Object... args) {
		return poly_port((ST_Agnode_s)args[0], (CString)args[1], (CString)args[2]);
	}};
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/shapes.c", name="poly_port", key="5k2b9gfpwm2tj3zmzniuz9azt", definition="static port poly_port(node_t * n, char *portname, char *compass)")
public static ST_port poly_port(ST_Agnode_s n, CString portname, CString compass) {
// WARNING!! STRUCT
return poly_port_w_(n, portname, compass).copy();
}
private static ST_port poly_port_w_(ST_Agnode_s n, CString portname, CString compass) {
ENTERING("5k2b9gfpwm2tj3zmzniuz9azt","poly_port");
try {
    final ST_port rv= new ST_port();
    ST_boxf bp;
    int sides[] = new int[1];			/* bitmap of which sides the port lies along */
    
    if (portname.charAt(0) == '\0')
	return Z.z().Center;
    
    if (compass == null)
	compass = new CString("_");
    sides[0] = BOTTOM | RIGHT | TOP | LEFT;
    if ((ND_label(n).html) && ((bp = html_port(n, portname, sides))!=null)) {
UNSUPPORTED("dl6n43wu7irkeiaxb6wed3388"); // 	if (compassPort(n, bp, &rv, compass, sides, NULL)) {
UNSUPPORTED("cw5grwj6gbj94jcztvnp2ooyj"); // 	    agerr(AGWARN,
UNSUPPORTED("en2xpqtprfng8gmc77dzq7klv"); // 		  "node %s, port %s, unrecognized compass point '%s' - ignored\n",
UNSUPPORTED("cmo03yl2q1wgn0c1r45y1ay5e"); // 		  agnameof(n), portname, compass);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    } else {
	ST_inside_t ictxtp = null;
	final ST_inside_t ictxt = new ST_inside_t();
	
	if (IS_BOX(n))
	    ictxtp = null;
	else {
UNSUPPORTED("17pbmb7rfq2rdapm13ww6pefz"); // 	    ictxt.s.n = n;
UNSUPPORTED("etss3zom716xdeasxnytjb8db"); // 	    ictxt.s.bp = NULL;
UNSUPPORTED("89cj6b362bd80f627mp67yjh0"); // 	    ictxtp = &ictxt;
	}
	if (compassPort(n, null, rv, portname, sides[0], ictxtp))
		unrecognized(n, portname);
    }

return rv;
} finally {
LEAVING("5k2b9gfpwm2tj3zmzniuz9azt","poly_port");
}
}



private static boolean IS_BOX(ST_Agnode_s n) {
	return EQ(ND_shape(n).polygon, Z.z().p_box);
}


public static CFunction poly_gencode = new CFunctionAbstract("poly_gencode") {
	
	public Object exe(Object... args) {
		return poly_gencode(args);
	}};
//3 1tks71z165dy9pzfshnjejpx3
// static void poly_gencode(GVJ_t * job, node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="poly_gencode", key="1tks71z165dy9pzfshnjejpx3", definition="static void poly_gencode(GVJ_t * job, node_t * n)")
public static Object poly_gencode(Object... arg) {
UNSUPPORTED("p0x21cs921921juch0sv0bun"); // static void poly_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("7b0667dpeiekddi69gpywx92t"); //     polygon_t *poly;
UNSUPPORTED("behzd4x7hwrpj60ld9ydd6ldw"); //     double xsize, ysize;
UNSUPPORTED("avlmoeaaigyvssingomxrvja4"); //     int i, j, peripheries, sides, style;
UNSUPPORTED("1r39xvspssd187ru2ru0hw25i"); //     pointf P, *vertices;
UNSUPPORTED("behdcj4jfqh2lxeud7bvr9dxx"); //     static pointf *AF;
UNSUPPORTED("922k2c5xjbw7vuw4vfhavkll9"); //     static int A_size;
UNSUPPORTED("e26zsspincyfi747lhus7h41b"); //     boolean filled;
UNSUPPORTED("343gvjl2hbvjb2nrrtcqqetep"); //     boolean usershape_p;
UNSUPPORTED("55zxkmqgt42k3bgw1g1del41"); //     boolean pfilled;		/* true if fill not handled by user shape */
UNSUPPORTED("b80uijjl4g1zjdox5s5vdh8s5"); //     char *color, *name;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = obj->url || obj->explicit_tooltip;
UNSUPPORTED("7421ua6zgvtho3nwdlh9ypytf"); //     char* fillcolor=NULL;
UNSUPPORTED("39txqf5jgyh1q10jekeaemag6"); //     char* pencolor=NULL;
UNSUPPORTED("bhtcyodd9jiazat6sqhp9pm4x"); //     char* clrs[2];
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("e8a863hfpkzgw2w09pemrprir"); //     poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("44eync2gzhkt36aljp0pdxlws"); //     vertices = poly->vertices;
UNSUPPORTED("bt0ymhl3qyi2wkx6awwozl8pm"); //     sides = poly->sides;
UNSUPPORTED("axi5xtmkixooa3vai8uysr8y1"); //     peripheries = poly->peripheries;
UNSUPPORTED("3yzb2exxpwntmjik61bia8qin"); //     if (A_size < sides) {
UNSUPPORTED("6czsf4ed6c2x6qn10dz9vvpc2"); // 	A_size = sides + 5;
UNSUPPORTED("4fxnv89xcha2g2jkqjznbfhtl"); // 	AF = ALLOC(A_size, AF, pointf);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("65psnpx1lm1txgz684nsf5fy0"); //     /* nominal label position in the center of the node */
UNSUPPORTED("1bslo0pyyucx0zmdzt12sei6d"); //     ND_label(n)->pos = ND_coord(n);
UNSUPPORTED("8pq7sdzx1tcm5jiy7gk6k14ru"); //     xsize = (ND_lw(n) + ND_rw(n)) / (ROUND((ND_width(n))*72));
UNSUPPORTED("ebgzy2lbfiijt1acuci7zobbz"); //     ysize = ND_ht(n) / (ROUND((ND_height(n))*72));
UNSUPPORTED("6yjfupcwvts03fbmr493ea2ja"); //     style = stylenode(job, n);
UNSUPPORTED("92hvfvrwzs8dy1vdgk97mu8rm"); //     clrs[0] = NULL;
UNSUPPORTED("e5t9x8qxknm67g2irjuq09m0n"); //     if (ND_gui_state(n) & (1<<0)) {
UNSUPPORTED("bmfnw21ksvzdvbf1k6jhpy482"); // 	pencolor = late_nnstring(n, N_activepencolor, "#808080");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("3kou17p4mmlejrgnb4ubal4y0"); // 	    late_nnstring(n, N_activefillcolor, "#fcfcfc");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("9ihvjyvhnzzz36yb9vxt7ds0x"); //     } else if (ND_gui_state(n) & (1<<1)) {
UNSUPPORTED("aak3ib1vf3cr00erxujx1x1a2"); // 	pencolor =
UNSUPPORTED("1cimazkiwwo2m0abp23m3fnme"); // 	    late_nnstring(n, N_selectedpencolor, "#303030");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("28yl28qxl17kdj778ikor38xk"); // 	    late_nnstring(n, N_selectedfillcolor,
UNSUPPORTED("47h1lk49r1o0z3cv330dq6dx"); // 			  "#e8e8e8");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("1yfjih723r7l1aal6cgysntu9"); //     } else if (ND_gui_state(n) & (1<<3)) {
UNSUPPORTED("aak3ib1vf3cr00erxujx1x1a2"); // 	pencolor =
UNSUPPORTED("7ksdqin8o1wm9jzsj3vquwpn4"); // 	    late_nnstring(n, N_deletedpencolor, "#e0e0e0");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("bt3kkty4bxox77ydiwjgsxvdl"); // 	    late_nnstring(n, N_deletedfillcolor, "#f0f0f0");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("8zwfuofs5l5a6z3f4rvlihyw2"); //     } else if (ND_gui_state(n) & (1<<2)) {
UNSUPPORTED("aak3ib1vf3cr00erxujx1x1a2"); // 	pencolor =
UNSUPPORTED("ctvdbytqgb1rzge7ij5ocomx9"); // 	    late_nnstring(n, N_visitedpencolor, "#101010");
UNSUPPORTED("4m6zwbkh86axvr0iupq8yqbj"); // 	gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("cmymz070zao66wyx1s7tv8pha"); // 	color =
UNSUPPORTED("2ffts5ygp2gvce89s4zmac21o"); // 	    late_nnstring(n, N_visitedfillcolor, "#f8f8f8");
UNSUPPORTED("8jkw84z9v2sgxja8neagg70yn"); // 	gvrender_set_fillcolor(job, color);
UNSUPPORTED("wgi1jlomdsgec9gfae0fj8md"); // 	filled = 1;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("71lsnu3rvb8q4qjlg8ekkueb8"); // 	if (style & (1 << 0)) {
UNSUPPORTED("1ldzvmymblz8y4a6idvyxoj5t"); // 	    float frac;
UNSUPPORTED("e039lb3amkbtia1p5xid53g8f"); // 	    fillcolor = findFill (n);
UNSUPPORTED("5dnga3gh00f4sv4fk1n2iqdgu"); // 	    if (findStopColor (fillcolor, clrs, &frac)) {
UNSUPPORTED("12wjuz2zq45txyp39hhco78xu"); //         	gvrender_set_fillcolor(job, clrs[0]);
UNSUPPORTED("5o23oun5dlazsaicyjj530pp"); // 		if (clrs[1]) 
UNSUPPORTED("ct9w73vq2t9wsony60rgp0vuv"); // 		    gvrender_set_gradient_vals(job,clrs[1],late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("5v31mz0fdr0su096gqov41vyn"); // 		else 
UNSUPPORTED("5hcjieyymox6ih0mqxtesfkai"); // 		    gvrender_set_gradient_vals(job,"black",late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("cu80xxb02iidme5bgb4b9q03o"); // 		if (style & (1 << 1))
UNSUPPORTED("5jf506rwz9snq4d6ozpjvg3yg"); // 		    filled = 3;
UNSUPPORTED("7rknc7r0egcn3cw68mrvgow3v"); // 	 	else
UNSUPPORTED("7bikp52v1ey2yil3rybx6nris"); // 		    filled = 2;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("es2lu1zhy5wdeml1v1kmrcix3"); //         	gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("6w06em6l23suofe15du0wq9hb"); // 		filled = 1;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cbyq6e4yotsw91ihtsbpqk9n"); // 	else if (style & ((1 << 6)|(1 << 9)))  {
UNSUPPORTED("e039lb3amkbtia1p5xid53g8f"); // 	    fillcolor = findFill (n);
UNSUPPORTED("b39ijeotj91epdulx0zfawqg7"); //             /* gvrender_set_fillcolor(job, fillcolor); */
UNSUPPORTED("5op945vn3c1cyxwov5p8rj33t"); // 	    filled = NOT(0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("6hyckgrxm2nsg8cw4hffomldu"); // 	    filled = 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("7v8vwyf8talmtwk6o9fv16cu7"); // 	pencolor = penColor(job, n);	/* emit pen color */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dzz4jp9gamcvlyn9e3vzfb9m5"); //     pfilled = !ND_shape(n)->usershape || (*(ND_shape(n)->name)==*("custom")&&!strcmp(ND_shape(n)->name,"custom"));
UNSUPPORTED("867znru6ot29tjqobp8dlbw6z"); //     /* if no boundary but filled, set boundary color to transparent */
UNSUPPORTED("42p7y58vqzgaceefog269961h"); //     if ((peripheries == 0) && filled && pfilled) {
UNSUPPORTED("15ha366z6aj0vmrwy4kws0mqd"); // 	peripheries = 1;
UNSUPPORTED("9h0jwzscq5xyee6v8y9a84z5z"); // 	gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b5y5lqlrrc44k9t418m98208o"); //     /* draw peripheries first */
UNSUPPORTED("3ldxhnwdjmonz5bmmr7t8i5v6"); //     for (j = 0; j < peripheries; j++) {
UNSUPPORTED("bnlcutimilujroygrsjpbamec"); // 	for (i = 0; i < sides; i++) {
UNSUPPORTED("6jkqzav2wqsdxuy5nalny0l8v"); // 	    P = vertices[i + j * sides];
UNSUPPORTED("7cdu1dtqyaubntomiasv9qnoj"); // 	    AF[i].x = P.x * xsize + ND_coord(n).x;
UNSUPPORTED("e40xvnbar4dmi82ewqw5laa59"); // 	    AF[i].y = P.y * ysize + ND_coord(n).y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9dpfyah7h8cjesbm1tagc3qr2"); // 	if (sides <= 2) {
UNSUPPORTED("4iafj5ab7zhphfv75axr98xpm"); // 	    if ((style & (1 << 9)) && (j == 0) && (strchr(fillcolor,':'))) {
UNSUPPORTED("brwfdh2hmhcwxahcpjocmax54"); // 		int rv = wedgedEllipse (job, AF, fillcolor);
UNSUPPORTED("4195dkkxygfup9x2hevx5t0kt"); // 		if (rv > 1)
UNSUPPORTED("6d80sdeoci13p59wizsvnilpd"); // 		    agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("3zx9cyeiqls2js359g1ja8px8"); // 		filled = 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dzwn73f4njl5hkp0qrnncl2ff"); // 	    gvrender_ellipse(job, AF, sides, filled);
UNSUPPORTED("chb5tdwhi8a8xmy8ftheo6824"); // 	    if (style & (1 << 3)) {
UNSUPPORTED("efwhq15vj62j7hdj6evx064cg"); // 		Mcircle_hack(job, n);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("aci5r7yyn8mzrv3exe7znstcn"); // 	} else if (style & (1 << 6)) {
UNSUPPORTED("dgwuupvm0kjmgthk4ugim8woz"); // 	    if (j == 0) {
UNSUPPORTED("3x4ndf7fx76diabv9nfllk0b5"); // 		int rv = stripedBox (job, AF, fillcolor, 1);
UNSUPPORTED("4195dkkxygfup9x2hevx5t0kt"); // 		if (rv > 1)
UNSUPPORTED("6d80sdeoci13p59wizsvnilpd"); // 		    agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("717qv74rlec63ys8natmmpak9"); // 	    gvrender_polygon(job, AF, sides, 0);
UNSUPPORTED("5ueys9z3ukkzz7o4fr6z8tuk0"); // 	} else if (style & (1 << 10)) {
UNSUPPORTED("8ozii45lu97yd30cta30grmf8"); // 	    gvrender_set_pencolor(job, "transparent");
UNSUPPORTED("oe3tziy2rg7shg7dan61ilfq"); // 	    gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("9cgcmdbt8qdrnqnvs86u9cd53"); // 	    gvrender_set_pencolor(job, pencolor);
UNSUPPORTED("fft8g5x7554aunjp9t27mqx6"); // 	    gvrender_polyline(job, AF+2, 2);
UNSUPPORTED("8t4w6b2lracu2ee6rqqm6r915"); // 	} else if (((style) & ((1 << 2) | (1 << 3) | (127 << 24)))) {
UNSUPPORTED("858fovk41ca06eamq91gjw7tm"); // 	    round_corners(job, AF, sides, style, filled);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("oe3tziy2rg7shg7dan61ilfq"); // 	    gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1peuavyjb0rqm2z4fzpf2afzm"); // 	/* fill innermost periphery only */
UNSUPPORTED("arpfq2ay8oyluwsz8s1wp6tp4"); // 	filled = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("76vwep8b6qm5azc0fy66d98rw"); //     usershape_p = 0;
UNSUPPORTED("8fhwzyrc8mh95ap0b1g7e9nbq"); //     if (ND_shape(n)->usershape) {
UNSUPPORTED("2v9mlb5rtcmwqpcth7w27clk5"); // 	name = ND_shape(n)->name;
UNSUPPORTED("ad1u0yih0rcookfy0x1lsev4d"); // 	if ((*(name)==*("custom")&&!strcmp(name,"custom"))) {
UNSUPPORTED("7eg6kesbmod5ryqil85qa0nhh"); // 	    if ((name = agget(n, "shapefile")) && name[0])
UNSUPPORTED("avdrph3m5jvu0m9cldtioxy3f"); // 		usershape_p = NOT(0);
UNSUPPORTED("6to1esmb8qfrhzgtr7jdqleja"); // 	} else
UNSUPPORTED("cmpu4v9yae7spgt5x9vvwycqu"); // 	    usershape_p = NOT(0);
UNSUPPORTED("5i5g01dslsnkth7in6u6rbi99"); //     } else if ((name = agget(n, "image")) && name[0]) {
UNSUPPORTED("e220s4b8iyyeqjgxmlg5pcdrj"); // 	usershape_p = NOT(0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3btjgija7cfs4jgzrv91fjwpt"); //     if (usershape_p) {
UNSUPPORTED("9usktstdf8lawthhtrs6s58pm"); // 	/* get coords of innermost periphery */
UNSUPPORTED("bnlcutimilujroygrsjpbamec"); // 	for (i = 0; i < sides; i++) {
UNSUPPORTED("5dznk69haxedww8ugav5ykrld"); // 	    P = vertices[i];
UNSUPPORTED("7cdu1dtqyaubntomiasv9qnoj"); // 	    AF[i].x = P.x * xsize + ND_coord(n).x;
UNSUPPORTED("e40xvnbar4dmi82ewqw5laa59"); // 	    AF[i].y = P.y * ysize + ND_coord(n).y;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1xu7you106p030clm45rzsrgc"); // 	/* lay down fill first */
UNSUPPORTED("3sznkjp2q6eryoqsuxyw523pa"); // 	if (filled && pfilled) {
UNSUPPORTED("dur5g2omz2d8j499p5rr99e0g"); // 	    if (sides <= 2) {
UNSUPPORTED("ezipi4mltlppyq0tetpgbb2rn"); // 		if ((style & (1 << 9)) && (j == 0) && (strchr(fillcolor,':'))) {
UNSUPPORTED("an9w62svq9d61trsclgublxs4"); // 		    int rv = wedgedEllipse (job, AF, fillcolor);
UNSUPPORTED("4njt8ngwdhm5t0qj38vd4vx26"); // 		    if (rv > 1)
UNSUPPORTED("dimjpscq5rjb3aaiz8l8gia45"); // 			agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("14k7t5gy5xvy3m6y4lllccbyz"); // 		    filled = 0;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("4ey5tpwqvnf3eih51z1ar6wgg"); // 		gvrender_ellipse(job, AF, sides, filled);
UNSUPPORTED("7eygavzyy3od5lurlb1kyvq4q"); // 		if (style & (1 << 3)) {
UNSUPPORTED("53tsr41edfe2tdmq1vs4qmoh6"); // 		    Mcircle_hack(job, n);
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("79b2w9yvj1qj97vqjuf6ff9w0"); // 	    } else if (style & (1 << 6)) {
UNSUPPORTED("3x4ndf7fx76diabv9nfllk0b5"); // 		int rv = stripedBox (job, AF, fillcolor, 1);
UNSUPPORTED("4195dkkxygfup9x2hevx5t0kt"); // 		if (rv > 1)
UNSUPPORTED("6d80sdeoci13p59wizsvnilpd"); // 		    agerr (AGPREV, "in node %s\n", agnameof(n));
UNSUPPORTED("ctx2lp124btfhy4z6030o2gs"); // 		gvrender_polygon(job, AF, sides, 0);
UNSUPPORTED("89clftmmkfws4k288i4jas2yb"); // 	    } else if (style & ((1 << 2) | (1 << 3))) {
UNSUPPORTED("dk9vlsyutilnikpal5kjamo5x"); // 		round_corners(job, AF, sides, style, filled);
UNSUPPORTED("175pyfe8j8mbhdwvrbx3gmew9"); // 	    } else {
UNSUPPORTED("azv3esl3n2c27ol5b9dgx7yrz"); // 		gvrender_polygon(job, AF, sides, filled);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8bp2tc15gonvz3x38es3dcmqm"); // 	gvrender_usershape(job, name, AF, sides, filled,
UNSUPPORTED("4ob0y29flbn0mu1b6or1pikm"); // 			   late_string(n, N_imagescale, "false"));
UNSUPPORTED("cyozk4ozoaaqkwqvcr0wuavfb"); // 	filled = 0;		/* with user shapes, we have done the fill if needed */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("59de9ohjmjuxis5h2yvc2zjnx"); //     free (clrs[0]);
UNSUPPORTED("8r8t0lgzzpigm1odig9a9yg1c"); //     emit_label(job, EMIT_NLABEL, ND_label(n));
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//1 7tslf55o9g8v48j97pdsyich9
// static char *reclblp




//3 1dflsvfaih0mcg1gg4n23v1rg
// static void free_field(field_t * f) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="free_field", key="1dflsvfaih0mcg1gg4n23v1rg", definition="static void free_field(field_t * f)")
public static Object free_field(Object... arg) {
UNSUPPORTED("1w8vqjgpmm3wzxdg86sst9sna"); // static void free_field(field_t * f)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7zbyipqbl6t75m71to6vrvnmq"); //     for (i = 0; i < f->n_flds; i++) {
UNSUPPORTED("44t6o1rhsqwprcg98j3zgbzvz"); // 	free_field(f->fld[i]);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8mpeuez5fwrg7hufhlnvpzpk6"); //     free(f->id);
UNSUPPORTED("9mo450myxof5j4jin03aqpb9n"); //     free_label(f->lp);
UNSUPPORTED("6onriqqkoxktq7iqg9iiuw1zo"); //     free(f->fld);
UNSUPPORTED("a4v6veu7h0jl3a2wwlxwpdsuw"); //     free(f);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}



private final static int HASTEXT = 1;
private final static int HASPORT = 2;
private final static int HASTABLE = 4;
private final static int INTEXT = 8;
private final static int INPORT = 16;

//3 7zxlp1fmrq3zt4fprrtesdbg3
// static field_t *parse_reclbl(node_t * n, int LR, int flag, char *text) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="", key="7zxlp1fmrq3zt4fprrtesdbg3", definition="static field_t *parse_reclbl(node_t * n, int LR, int flag, char *text)")
public static ST_field_t parse_reclbl(ST_Agnode_s n, boolean LR, boolean flag, CString text) {
ENTERING("7zxlp1fmrq3zt4fprrtesdbg3","parse_reclbl");
try {
    ST_field_t fp;
    ST_field_t rv = new ST_field_t();
    CString tsp, psp=null, hstsp, hspsp=null, sp;
    CString tmpport = null;
    int maxf, cnt, mode, fi;
    boolean wflag, ishardspace;
    ST_textlabel_t lbl = ND_label(n);
    
    fp = null;
    for (maxf = 1, cnt = 0, sp = Z.z().reclblp; sp.charAt(0)!=0; sp = sp.plus_(1)) {
 	if (sp.charAt(0) == '\\') {
	    sp = sp.plus_(1);
	    if (sp.charAt(0)!=0
		&& (sp.charAt(0) == '{' || sp.charAt(0) == '}' || sp.charAt(0) == '|' || sp.charAt(0) == '\\'))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
 	}
 	if (sp.charAt(0) == '{')
 	    cnt++;
 	else if (sp.charAt(0) == '}')
 	    cnt--;
 	else if (sp.charAt(0) == '|' && cnt == 0)
 	    maxf++;
 	if (cnt < 0)
 	    break;
    }
	rv.fld = CArrayOfStar.<ST_field_t>ALLOC(maxf, ST_field_t.class);
    rv.LR = LR;
    mode = 0;
    fi = 0;
    hstsp = tsp = text;
    wflag = NOT(0);
    ishardspace = false;
    while (wflag) {
	if ((Z.z().reclblp.charAt(0) < ' ') && Z.z().reclblp.charAt(0)!=0) {    /* Ignore non-0 control characters */
		Z.z().reclblp = Z.z().reclblp.plus_(1);
	    continue;
	}
 	switch (Z.z().reclblp.charAt(0)) {
 	case '<':
	    if ((mode & (HASTABLE | HASPORT))!=0)
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
	    if (lbl.html)
UNSUPPORTED("75bwqdnezjvhazmryfatc4819"); // 		goto dotext;
	    mode |= (HASPORT | INPORT);
	    Z.z().reclblp = Z.z().reclblp.plus_(1);
	    hspsp = psp = text;
	    break;
 	case '>':
	    if (lbl.html)
UNSUPPORTED("75bwqdnezjvhazmryfatc4819"); // 		goto dotext;
	    if (N(mode & INPORT))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
 // 	    if (psp > text + 1 && psp - 1 != hspsp && *(psp - 1) == ' ')
	    if (psp.comparePointer(text.plus_(1)) > 0 && psp.plus_(-1).comparePointer(hspsp) != 0 && psp.charAt(-1) == ' ')
UNSUPPORTED("7v2hf4x5nsnlq1l025dplo0vo"); // 		psp--;
	    psp.setCharAt(0, '\000');
	    tmpport = text.strdup();
	    mode &= ~INPORT;
	    Z.z().reclblp = Z.z().reclblp.plus_(1);
	    break;
 	case '{':
	    Z.z().reclblp = Z.z().reclblp.plus_(1);
	    if (mode != 0 || N(Z.z().reclblp.charAt(0)))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
	    mode = 4;
	    rv.fld.set_(fi, parse_reclbl(n, NOT(LR), false, text));
	    if (N(rv.fld.get_(fi++)))
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
	    break;
 	case '}':
	case '|':
	case '\000':
	    if ((N(Z.z().reclblp.charAt(0)) && !flag) || (mode & INPORT)!=0)
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
	    if (N(mode & HASTABLE))
	    {
	    	fp = new ST_field_t();
	    	rv.fld.set_(fi++, fp);
	    }
	    if (tmpport!=null) {
		fp.id = tmpport;
		tmpport = null;
	    }
	    if (N(mode & (HASTEXT | HASTABLE)))
 		{mode |= 1; tsp.setCharAt(0, ' ');tsp=tsp.plus_(1); }
	    if ((mode & HASTEXT)!=0) {
		if (tsp.comparePointer(text.plus_(1))  > 0 &&
		    tsp.plus_(-1).comparePointer(hstsp) != 0 && tsp.charAt(-1) == ' ')
		    tsp = tsp.plus_(-1);
		tsp.setCharAt(0, '\000');
		fp.lp =
				make_label(n, text.strdup(),
						(lbl.html ? LT_HTML : LT_NONE),
						lbl.fontsize, lbl.fontname,
						lbl.fontcolor);
		fp.LR = NOT(0);
		hstsp = tsp = text;
	    }
	    if (Z.z().reclblp.charAt(0)!=0) {
		if (Z.z().reclblp.charAt(0) == '}') {
		    Z.z().reclblp = Z.z().reclblp.plus_(1);
		    rv.n_flds = fi;
		    return rv;
		}
		mode = 0;
		Z.z().reclblp = Z.z().reclblp.plus_(1);
	    } else
		wflag = false;
	    break;
	case '\\':
UNSUPPORTED("3vnixbvvmty9ydvf0l1929gle"); // 	    if (*(reclblp + 1)) {
UNSUPPORTED("bjtxv6n9c9aqzdkik1c6cqbvy"); // 		if (((*(reclblp + 1)) == '{' || (*(reclblp + 1)) == '}' || (*(reclblp + 1)) == '|' || (*(reclblp + 1)) == '<' || (*(reclblp + 1)) == '>'))
UNSUPPORTED("dcc90zmv0256yuz6jtriktl8s"); // 		    reclblp++;
UNSUPPORTED("c8cxvsbs7ae3wdjeflwbk3z6u"); // 		else if ((*(reclblp + 1) == ' ') && !lbl->html)
UNSUPPORTED("djkriuw8khnsxfne1jal3yysz"); // 		    ishardspace = NOT(0), reclblp++;
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("2qwaphvt2yekkogtyqq0omhut"); // 		    *tsp++ = '\\';
UNSUPPORTED("63p7706g22u4h7m9yealimr3g"); // 		    mode |= (8 | 1);
UNSUPPORTED("dcc90zmv0256yuz6jtriktl8s"); // 		    reclblp++;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dg5yg97t3vto8m73vvwj8jnb2"); // 	    /* falling through ... */
 	default:
//UNSUPPORTED("2d4vmvpowhgj7h9539m0qrxsy"); // 	  dotext:
	    if ((mode & HASTABLE)!=0 && Z.z().reclblp.charAt(0) != ' ')
UNSUPPORTED("7zw1csy7lc9a9gq1nhizs470m"); // 		return parse_error(rv, tmpport);
	    if (N(mode & (INTEXT | INPORT)) && Z.z().reclblp.charAt(0) != ' ')
		mode |= (INTEXT | HASTEXT);
	    if ((mode & INTEXT)!=0) {
		if (N
		    (Z.z().reclblp.charAt(0) == ' ' && !ishardspace && tsp.charAt(-1) == ' '
		     && N(lbl.html)))
		{
			tsp.setCharAt(0, Z.z().reclblp.charAt(0));
			tsp = tsp.plus_(1);
		}
		if (ishardspace)
UNSUPPORTED("atuokfvkmomvi6gvwvpbxggoz"); // 		    hstsp = tsp - 1;
	    } else if ((mode & INPORT)!=0) {
		if (!(Z.z().reclblp.charAt(0) == ' ' && !ishardspace &&
		      (psp == text || psp.charAt(-1) == ' ')))
		{
			psp.setCharAt(0, Z.z().reclblp.charAt(0));
			psp = psp.plus_(1);
		}
		if (ishardspace)
UNSUPPORTED("5u5h7cb6egued2g1q7w8yhb1n"); // 		    hspsp = psp - 1;
	    }
	    Z.z().reclblp = Z.z().reclblp.plus_(1);
	    while ((Z.z().reclblp.charAt(0) & 128)!=0)
UNSUPPORTED("86nc3qdu6nuyt7u67d0kblb9w"); // 		*tsp++ = *reclblp++;
	    break;
 	}
    }
    rv.n_flds = fi;
    return rv;
//UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
} finally {
LEAVING("7zxlp1fmrq3zt4fprrtesdbg3","poly_init");
}
}




//3 dwk0rh74bwfd7mky5hg9t1epj
// static pointf size_reclbl(node_t * n, field_t * f) 
@Unused
@Doc("?")
@Original(version="2.38.0", path="lib/common/shapes.c", name="size_reclbl", key="dwk0rh74bwfd7mky5hg9t1epj", definition="static pointf size_reclbl(node_t * n, field_t * f)")
public static ST_pointf size_reclbl(ST_Agnode_s n, ST_field_t f) {
	// WARNING!! STRUCT
	return size_reclbl_(n, f).copy();
}
private static ST_pointf size_reclbl_(ST_Agnode_s n, ST_field_t f) {
	ENTERING("dwk0rh74bwfd7mky5hg9t1epj","size_reclbl_");
	try {
    int i;
    CString p;
    double marginx, marginy;
    final ST_pointf d = new ST_pointf(), d0 = new ST_pointf();
    final ST_pointf dimen = new ST_pointf();
    if (f.lp != null) {
	dimen.___(f.lp.dimen);
	/* minimal whitespace around label */
	if ((dimen.x > 0.0) || (dimen.y > 0.0)) {
	    /* padding */
	    if ((p = agget(n, new CString("margin")))!=null) {
UNSUPPORTED("bfyy3iw4z9ebf4m89x69tn1eb"); // 		i = sscanf(p, "%lf,%lf", &marginx, &marginy);
UNSUPPORTED("ebo7omz8ev8wu69ub10b4o890"); // 		if (i > 0) {
UNSUPPORTED("efcgckeemzkbxh32pc2qcdv0d"); // 		    dimen.x += 2 * (ROUND((marginx)*72));
UNSUPPORTED("c755n9x3n7022hjjg8hanklib"); // 		    if (i > 1)
UNSUPPORTED("2az12nq89f7txcsfmqdj8tly1"); // 			dimen.y += 2 * (ROUND((marginy)*72));
UNSUPPORTED("9acag2yacl63g8rg6r1alu62x"); // 		    else
UNSUPPORTED("2az12nq89f7txcsfmqdj8tly1"); // 			dimen.y += 2 * (ROUND((marginy)*72));
UNSUPPORTED("738mi6h8ef0itznt34ngxe25o"); // 		} else
UNSUPPORTED("b12tl2a8tebl71ewuz3jms9jv"); // 		    {((dimen).x += 4*4); ((dimen).y += 2*4);};
	    } else
		{dimen.x += 4*4; dimen.y += 2*4;};
	}
	d.___(dimen);
    } else {
	d.x = d.y = 0;
	for (i = 0; i < f.n_flds; i++) {
	    d0.___(size_reclbl(n, f.fld.get_(i)));
	    if (f.LR) {
		d.x += d0.x;
		d.y = MAX(d.y, d0.y);
	    } else {
		d.y += d0.y;
		d.x = MAX(d.x, d0.x);
	    }
	}
    }
    f.size.___(d);
    return d;
} finally {
LEAVING("dwk0rh74bwfd7mky5hg9t1epj","size_reclbl");
}
}




//3 blo8etwhtlcsld8ox0vryznfw
// static void resize_reclbl(field_t * f, pointf sz, int nojustify_p) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="resize_reclbl", key="blo8etwhtlcsld8ox0vryznfw", definition="static void resize_reclbl(field_t * f, pointf sz, int nojustify_p)")
public static void resize_reclbl(ST_field_t f, final ST_pointf sz, boolean nojustify_p) {
	// WARNING!! STRUCT
	resize_reclbl_(f, sz.copy(), nojustify_p);
}
private static void resize_reclbl_(ST_field_t f, final ST_pointf sz, boolean nojustify_p) {
ENTERING("blo8etwhtlcsld8ox0vryznfw","resize_reclbl");
try {
    int i, amt;
    double inc = 0;
    final ST_pointf d = new ST_pointf();
    final ST_pointf newsz = new ST_pointf();
    ST_field_t sf;
    /* adjust field */
    d.x = sz.x - f.size.x;
    d.y = sz.y - f.size.y;
    f.size.___(sz);
    /* adjust text area */
    if (f.lp!=null && !nojustify_p) {
	f.lp.space.x += d.x;
	f.lp.space.y += d.y;
    }
    /* adjust children */
    if (f.n_flds!=0) {
	if (f.LR)
	    inc = d.x / f.n_flds;
	else
	    inc = d.y / f.n_flds;
	for (i = 0; i < f.n_flds; i++) {
	    sf = f.fld.get_(i);
	    amt = ((int) ((i + 1) * inc)) - ((int) (i * inc));
	    if (f.LR)
		newsz.___(pointfof(sf.size.x + amt, sz.y));
	    else
		newsz.___(pointfof(sz.x, sf.size.y + amt));
	    resize_reclbl(sf, newsz, nojustify_p);
	}
    }
	} finally {
		LEAVING("blo8etwhtlcsld8ox0vryznfw","resize_reclbl");
	}
}




//3 ds4v2i9xw0hm4y53ggbt8z2yk
// static void pos_reclbl(field_t * f, pointf ul, int sides) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="pos_reclbl", key="ds4v2i9xw0hm4y53ggbt8z2yk", definition="static void pos_reclbl(field_t * f, pointf ul, int sides)")
public static void pos_reclbl(ST_field_t f, final ST_pointf ul, int sides) {
	// WARNING!! STRUCT
	pos_reclbl_(f, ul.copy(), sides);
}
private static void pos_reclbl_(ST_field_t f, final ST_pointf ul, int sides) {
    int i, last, mask=0;
    f.sides = sides;
    f.b.LL.___(pointfof(ul.x, ul.y - f.size.y));
    f.b.UR.___(pointfof(ul.x + f.size.x, ul.y));
    last = f.n_flds - 1;
    for (i = 0; i <= last; i++) {
	if (sides!=0) {
	    if (f.LR) {
		if (i == 0) {
		    if (i == last)
			mask = TOP | BOTTOM | RIGHT | LEFT;
		    else
			mask = TOP | BOTTOM | LEFT;
		} else if (i == last)
		    mask = TOP | BOTTOM | RIGHT;
		else
		    mask = TOP | BOTTOM;
	    } else {
		if (i == 0) {
		    if (i == last)
UNSUPPORTED("rvq6ubzk0rezd88243ailv84"); // 			mask = TOP | BOTTOM | RIGHT | LEFT;
		    else
			mask = TOP | RIGHT | LEFT;
		} else if (i == last)
		    mask = LEFT | BOTTOM | RIGHT;
		else
		    mask = LEFT | RIGHT;
	    }
	} else
	    mask = 0;
	pos_reclbl(f.fld.get_(i), ul, sides & mask);
	if (f.LR)
	    ul.x = ul.x + f.fld.get_(i).size.x;
	else
	    ul.y = ul.y - f.fld.get_(i).size.y;
    }
}



public static CFunction record_init = new CFunctionAbstract("record_init") {
	
	public Object exe(Object... args) {
		record_init((ST_Agnode_s)args[0]);
		return null;
	}};
	
@Unused
@Doc("Init 'record' node")
@Reviewed(when = "02/12/2020")
@Original(version="2.38.0", path="lib/common/shapes.c", name="record_init", key="h2lcuthzwljbcjwdeidw1jiv", definition="static void record_init(node_t * n)")
public static void record_init(ST_Agnode_s n) {
ENTERING("h2lcuthzwljbcjwdeidw1jiv","record_init");
try {
	ST_field_t info;
	final ST_pointf ul = new ST_pointf(), sz = new ST_pointf();
    boolean flip;
    int len;
    CString textbuf;		/* temp buffer for storing labels */
    
	int sides = BOTTOM | RIGHT | TOP | LEFT;
	/* Always use rankdir to determine how records are laid out */
	flip = NOT(GD_realflip(agraphof(n)));
	Z.z().reclblp = ND_label(n).text;
    len = strlen(Z.z().reclblp);
    /* For some forgotten reason, an empty label is parsed into a space, so
     * we need at least two bytes in textbuf.
     */
     len = MAX(len, 1);
     textbuf = CString.gmalloc(len + 1);
    if (N(info = parse_reclbl(n, flip, NOT(0), textbuf))) {
UNSUPPORTED("7iezaksu9hyxhmv3r4cp4o529"); // 	agerr(AGERR, "bad label format %s\n", ND_label(n)->text);
UNSUPPORTED("8f1id7rqm71svssnxbjo0uwcu"); // 	reclblp = "\\N";
UNSUPPORTED("2wv3zfqhq53941rwk4vu9p9th"); // 	info = parse_reclbl(n, flip, NOT(0), textbuf);
    }
    Memory.free(textbuf);
    size_reclbl(n, info);
    sz.x = POINTS(ND_width(n));;
    sz.y = POINTS(ND_height(n));
    if (mapbool(late_string(n, Z.z().N_fixed, new CString("false")))) {
UNSUPPORTED("8iu51xbtntpdf5sc00g91djym"); // 	if ((sz.x < info->size.x) || (sz.y < info->size.y)) {
UNSUPPORTED("4vs5u30jzsrn6fpjd327xjf7r"); // /* should check that the record really won't fit, e.g., there may be no text.
UNSUPPORTED("7k6yytek9nu1ihxix2880667g"); // 			agerr(AGWARN, "node '%s' size may be too small\n", agnameof(n));
UNSUPPORTED("bnetqzovnscxile7ao44kc0qd"); // */
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    } else {
	sz.x = MAX(info.size.x, sz.x);
	sz.y = MAX(info.size.y, sz.y);
    }
    resize_reclbl(info, sz, mapbool(late_string(n, Z.z().N_nojustify, new CString("false"))));
    ul.___(pointfof(-sz.x / 2., sz.y / 2.));	/* FIXME - is this still true:    suspected to introduce ronding error - see Kluge below */
    pos_reclbl(info, ul, sides);
    ND_width(n, PS2INCH(info.size.x));
    ND_height(n, PS2INCH(info.size.y + 1));	/* Kluge!!  +1 to fix rounding diff between layout and rendering 
						   otherwise we can get -1 coords in output */
    ND_shape_info(n, info);
} finally {
LEAVING("h2lcuthzwljbcjwdeidw1jiv","poly_init");
}
}




public static CFunction record_free = new CFunctionAbstract("record_free") {
	
	public Object exe(Object... args) {
		return record_free(args);
	}}; 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="record_free", key="1lq2tksbz3nzqw9c3xqfs4ymf", definition="static void record_free(node_t * n)")
public static Object record_free(Object... arg_) {
UNSUPPORTED("8anx9p03jsmcuhguyzf7q6qe3"); // static void record_free(node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aiplewsp8j9h5b1bokpivfnqv"); //     field_t *p = ND_shape_info(n);
UNSUPPORTED("cn1q1h4lwj1gctn9nim9hdhpt"); //     free_field(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a005gfg4ujp2d29bpdrtowla0
// static field_t *map_rec_port(field_t * f, char *str) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="", key="a005gfg4ujp2d29bpdrtowla0", definition="static field_t *map_rec_port(field_t * f, char *str)")
public static ST_field_t map_rec_port(ST_field_t f, CString str) {
	ST_field_t rv = null;
    int sub;
    if (f.id!=null && f.id.isSame(str))
	rv = f;
    else {
	rv = null;
	for (sub = 0; sub < f.n_flds; sub++)
	    if ((rv = map_rec_port(f.fld.get_(sub), str))!=null)
		break;
    }
    return rv;
}





public static CFunction record_port = new CFunctionAbstract("record_port") {
	
	public Object exe(Object... args) {
		return record_port((ST_Agnode_s)args[0], (CString)args[1], (CString)args[2]);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="record_port", key="chsi0jlfodruvkjj5dlrv5ur3", definition="static port record_port(node_t * n, char *portname, char *compass)")
public static ST_port record_port(ST_Agnode_s n, CString portname, CString compass) {
    ST_field_t f;
    ST_field_t subf;
    final ST_port rv = new ST_port();
    int sides;			/* bitmap of which sides the port lies along */
    if (portname.charAt(0) == '\0')
	return Z.z().Center;
    sides = BOTTOM | RIGHT | TOP | LEFT;
    if (compass == null)
	compass = new CString("_");
    f = (ST_field_t) ND_shape_info(n);
    if ((subf = map_rec_port(f, portname))!=null) {
	if (compassPort(n, subf.b, rv, compass, subf.sides, null)) {
UNSUPPORTED("cw5grwj6gbj94jcztvnp2ooyj"); // 	    agerr(AGWARN,
UNSUPPORTED("en2xpqtprfng8gmc77dzq7klv"); // 		  "node %s, port %s, unrecognized compass point '%s' - ignored\n",
UNSUPPORTED("cmo03yl2q1wgn0c1r45y1ay5e"); // 		  agnameof(n), portname, compass);
	}
    } else if (compassPort(n, f.b, rv, portname, sides, null)) {
    	unrecognized(n, portname);
    }
    return rv;
}




public static CFunction record_inside = new CFunctionAbstract("record_inside") {
	
	public Object exe(Object... args) {
		return record_inside((ST_inside_t)args[0], (ST_pointf)args[1]);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="record_inside", key="1f7b6eq3csywqv96raw75jqxr", definition="static boolean record_inside(inside_t * inside_context, pointf p)")
public static boolean record_inside(ST_inside_t inside_context, ST_pointf p) {
	// WARNING!! STRUCT
	return record_inside_(inside_context, p.copy());
}
private static boolean record_inside_(ST_inside_t inside_context, final ST_pointf p) {
ENTERING("1f7b6eq3csywqv96raw75jqxr","record_inside_");
try {
    ST_field_t fld0;
    ST_boxf bp = inside_context.s_bp;
    ST_Agnode_s n = inside_context.s_n;
    final ST_boxf bbox = new ST_boxf();
    /* convert point to node coordinate system */
    p.___(ccwrotatepf(p, 90 * GD_rankdir(agraphof(n))));
    if (bp == null) {
	fld0 = (ST_field_t) ND_shape_info(n);
	bbox.___(fld0.b);
    } else
	bbox.___(bp);
    return INSIDE(p, bbox);
} finally {
LEAVING("1f7b6eq3csywqv96raw75jqxr","record_inside_");
}
}




public static CFunction record_path = new CFunctionAbstract("record_path") {
	
	public Object exe(Object... args) {
		return record_path((ST_Agnode_s)args[0], (ST_port)args[1], (Integer)args[2], (ST_boxf)args[3], (int[])args[4]);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="record_path", key="3p54k8x2kyueort8kj41qvkty", definition="static int record_path(node_t * n, port * prt, int side, boxf rv[], 		       int *kptr)")
public static int record_path(ST_Agnode_s n, ST_port prt, int side, ST_boxf rv, int[] kptr) {
    int i, ls=0, rs=0;
    final ST_pointf p = new ST_pointf();
    ST_field_t info;
    if (N(prt.defined))
	return 0;
    p.___(prt.p);
    info = (ST_field_t) ND_shape_info(n);
    for (i = 0; i < info.n_flds; i++) {
	if (N(GD_flip(agraphof(n)))) {
	    ls = (int) info.fld.get_(i).b.LL.x;
	    rs = (int) info.fld.get_(i).b.UR.x;
	} else {
UNSUPPORTED("dm9w81fxfdqc5bhtaimpbisvl"); // 	    ls = info->fld[i]->b.LL.y;
UNSUPPORTED("3sqtp996aa7m19wv9gwkrvav1"); // 	    rs = info->fld[i]->b.UR.y;
	}
	if (BETWEEN(ls, p.x, rs)) {
	    /* FIXME: I don't understand this code */
	    if (GD_flip(agraphof(n))) {
UNSUPPORTED("8p9z8b0nypgkzi1b3k7sx0fyz"); // 		rv[0] = flip_rec_boxf(info->fld[i]->b, ND_coord(n));
	    } else {
		rv.LL.x = ND_coord(n).x + ls;
		rv.LL.y = ND_coord(n).y - (ND_ht(n) / 2);
		rv.UR.x = ND_coord(n).x + rs;
	    }
	    rv.UR.y = ND_coord(n).y + (ND_ht(n) / 2);
	    kptr[0] = 1;
	    break;
	}
    }
    return side;
}




public static CFunction record_gencode = new CFunctionAbstract("record_gencode") {
	
	public Object exe(Object... args) {
		return record_gencode(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="record_gencode", key="3bum3y2gmowozskwp7e492wm7", definition="static void record_gencode(GVJ_t * job, node_t * n)")
public static Object record_gencode(Object... arg) {
UNSUPPORTED("cpq4ylwlb0lwi7ibim51gndor"); // static void record_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("bzz7vodjegzgwxp8jzgkq3uti"); //     boxf BF;
UNSUPPORTED("en6q26cyrg17g6yd6el73b3ns"); //     pointf AF[4];
UNSUPPORTED("b89hspuulkkzgmrj59tfy2fus"); //     int style;
UNSUPPORTED("30fmp9xlabtd67je318axlfiy"); //     field_t *f;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = obj->url || obj->explicit_tooltip;
UNSUPPORTED("3ml0gugucwlbwt5mbcdlymm8b"); //     int filled;
UNSUPPORTED("bhtcyodd9jiazat6sqhp9pm4x"); //     char* clrs[2];
UNSUPPORTED("9xovezi85vdgw8han4h0wr87s"); //     f = (field_t *) ND_shape_info(n);
UNSUPPORTED("arohpr2hcj50a0nm6wiegz75n"); //     BF = f->b;
UNSUPPORTED("9dwww64wl2oaucxyyhoa2u5op"); //     BF.LL.x += ND_coord(n).x;
UNSUPPORTED("eqak8167f3whj617r6180val"); //     BF.LL.y += ND_coord(n).y;
UNSUPPORTED("3u5f15d4i1cs3igvot9majw8n"); //     BF.UR.x += ND_coord(n).x;
UNSUPPORTED("18gannqx4rafy1juoif3uog1p"); //     BF.UR.y += ND_coord(n).y;
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("6yjfupcwvts03fbmr493ea2ja"); //     style = stylenode(job, n);
UNSUPPORTED("5qxdje5wxqq1c9786htlyohkx"); //     penColor(job, n);
UNSUPPORTED("92hvfvrwzs8dy1vdgk97mu8rm"); //     clrs[0] = NULL;
UNSUPPORTED("a0xb2wsthoxt62j0aks4aht13"); //     if (style & (1 << 0)) {
UNSUPPORTED("64vz86w7mg90duu37ik1bcm8m"); // 	char* fillcolor = findFill (n);
UNSUPPORTED("4xv0cmpfa4sol0pqmfumr0rnm"); // 	float frac;
UNSUPPORTED("dily1m3rwbo5mniq7aneh3qhu"); // 	if (findStopColor (fillcolor, clrs, &frac)) {
UNSUPPORTED("5m1l4f0yk2x1r9n00p7xoarhk"); //             gvrender_set_fillcolor(job, clrs[0]);
UNSUPPORTED("850qgpdnne96gxnh244hf2rh2"); // 	    if (clrs[1]) 
UNSUPPORTED("m1ck996y4kjzra9yxa5gif68"); // 		gvrender_set_gradient_vals(job,clrs[1],late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("f3qa0cv737ikcre1vpqlkukio"); // 	    else 
UNSUPPORTED("72n9vguy2n416qggkz5tpz279"); // 		gvrender_set_gradient_vals(job,"black",late_int(n,N_gradientangle,0,0), frac);
UNSUPPORTED("5dn7m0lqq174sxj9ezr6p8anp"); // 	    if (style & (1 << 1))
UNSUPPORTED("s4xfcz4il9k9jw0w0dh9lzpj"); // 		filled = 3;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("1ijl60mqfpjns1tss115yw4zp"); // 		filled = 2;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("7ek7aftv8z293izx886r01oqm"); // 	    filled = 1;
UNSUPPORTED("pufcu1p86jfo891eaibok4yb"); //             gvrender_set_fillcolor(job, fillcolor);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("53gluhbrz2oi6qw7sff0fb0hj"); //     else filled = 0;
UNSUPPORTED("a80jadmfy336hxiquc1baf16m"); //     if ((*(ND_shape(n)->name)==*("Mrecord")&&!strcmp(ND_shape(n)->name,"Mrecord")))
UNSUPPORTED("6iazzglp38g7uxmnloiwk5ilq"); // 	style |= (1 << 2);
UNSUPPORTED("gn97uo130dzjs4b5bnhnvlsq"); //     if (((style) & ((1 << 2) | (1 << 3) | (127 << 24)))) {
UNSUPPORTED("5rrbml0v0bc8c6x2ddgjh75p1"); // 	AF[0] = BF.LL;
UNSUPPORTED("8ctty3poiybj8vyrg3fy6s4ju"); // 	AF[2] = BF.UR;
UNSUPPORTED("bqdx8e632ko1pofmr5b91xpmh"); // 	AF[1].x = AF[2].x;
UNSUPPORTED("7gb7yo735gfv67doxjnyl8av7"); // 	AF[1].y = AF[0].y;
UNSUPPORTED("7w69hwqpw5l9f1rsaolr1ytmx"); // 	AF[3].x = AF[0].x;
UNSUPPORTED("cg5ir4ssc1l9d4x56swq1rw0k"); // 	AF[3].y = AF[2].y;
UNSUPPORTED("7gm0bhmoegfvu3uf7hnwfae67"); // 	round_corners(job, AF, 4, style, filled);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("5sf771cxqfrvdu2vzl3t1687e"); // 	gvrender_box(job, BF, filled);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9rksrkk1y26l0lgodpusjgg6r"); //     gen_fields(job, n, f);
UNSUPPORTED("ovdkxg0m1si7d9k8lawdnq"); //     if (clrs[0]) free (clrs[0]);
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 7unoy39g6rhro6he8kci7oh4n
// static shape_desc **UserShape


//1 94927xsjiykujshql95ma97vb
// static int N_UserShape




//3 35sn43hohjmtc7uvkjrx6u7jt
// shape_desc *find_user_shape(const char *name) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="", key="35sn43hohjmtc7uvkjrx6u7jt", definition="shape_desc *find_user_shape(const char *name)")
public static Object find_user_shape(Object... arg) {
UNSUPPORTED("dn82ttgu4gvl5nnzl8cu29o63"); // shape_desc *find_user_shape(const char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("757gomzjey403egq882hclnn0"); //     if (UserShape) {
UNSUPPORTED("30x6ygp0c6pjoq410g7sbl3lv"); // 	for (i = 0; i < N_UserShape; i++) {
UNSUPPORTED("3ka0imewegdrxvt7cdk37mqgj"); // 	    if ((*(UserShape[i]->name)==*(name)&&!strcmp(UserShape[i]->name,name)))
UNSUPPORTED("5eh2ibmiqg7qx9z5fvoxfnfyo"); // 		return UserShape[i];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("o68dp934ebg4cplebgc5hv4v"); //     return NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/shapes.c", name="", key="bmt148wdf0a7gslm7hmp043jy", definition="shape_desc *bind_shape(char *name, node_t * np)")
public static ST_shape_desc bind_shape(CString name, ST_Agnode_s np) {
ENTERING("bmt148wdf0a7gslm7hmp043jy","bind_shape");
try {
	ST_shape_desc rv = null;
    CString str;
    
    str = safefile(agget(np, new CString("shapefile")));
    /* If shapefile is defined and not epsf, set shape = custom */
    if (str!=null && UNSUPPORTED("!(*(name)==*(\"epsf\")&&!strcmp(name,\"epsf\"))")!=null)
	name = new CString("custom");
    if (N(name.charAt(0)=='c' && N(strcmp(name,new CString("custom"))))) {
	for (ST_shape_desc ptr : Z.z().Shapes) {
	    if ((N(strcmp(ptr.name,name)))) {
		rv = ptr;
		break;
	    }
	}
    }
    if (rv == null)
UNSUPPORTED("7funuix8h6nhe6fqrjsec3kvk"); // 	rv = user_shape(name);
    return rv;
} finally {
LEAVING("bmt148wdf0a7gslm7hmp043jy","bind_shape");
}
}




//3 9n2zfdpzi6zgvnhcb3kz7nw1u
// static boolean epsf_inside(inside_t * inside_context, pointf p) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="epsf_inside", key="9n2zfdpzi6zgvnhcb3kz7nw1u", definition="static boolean epsf_inside(inside_t * inside_context, pointf p)")
public static Object epsf_inside(Object... arg) {
UNSUPPORTED("cq9kgtgzrb9sazy7y2fpt859x"); // static boolean epsf_inside(inside_t * inside_context, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7lh87lvufqsd73q9difg0omei"); //     pointf P;
UNSUPPORTED("9ikeydfq03qx7m09iencqsk36"); //     double x2;
UNSUPPORTED("d8oppi8gt9b4eaonkdgb7a54l"); //     node_t *n = inside_context->s.n;
UNSUPPORTED("823iiqtx9pt0gijqrohrd3zx7"); //     P = ccwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("6uktb6bwhvglg7v3nygillmqx"); //     x2 = ND_ht(n) / 2;
UNSUPPORTED("3gki5ta81e51de9h4b5nvmoij"); //     return ((P.y >= -x2) && (P.y <= x2) && (P.x >= -ND_lw(n))
UNSUPPORTED("3bzok6rkdjzamkk155dcqc8n2"); // 	    && (P.x <= ND_rw(n)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6xv85fky6n2v03mt0dbvpz05e
// static void epsf_gencode(GVJ_t * job, node_t * n) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="epsf_gencode", key="6xv85fky6n2v03mt0dbvpz05e", definition="static void epsf_gencode(GVJ_t * job, node_t * n)")
public static Object epsf_gencode(Object... arg) {
UNSUPPORTED("4mtkoc5bwv0wkraw1xv9ptjlo"); // static void epsf_gencode(GVJ_t * job, node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("31b47kcwg6z2ds4cugdfq5hft"); //     epsf_t *desc;
UNSUPPORTED("6ciz320nm1jdjxir808cycx3t"); //     int doMap = obj->url || obj->explicit_tooltip;
UNSUPPORTED("7wygkmvhwjn2l2fmpw5bj1o6g"); //     desc = (epsf_t *) (ND_shape_info(n));
UNSUPPORTED("c98tv4jn3ode5so0mefrwcut7"); //     if (!desc)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("7pfkga2nn8ltabo2ycvjgma6o"); //     if (doMap && !(job->flags & (1<<2)))
UNSUPPORTED("6e7g66eeo7n8h8mq556pt3xxy"); // 	gvrender_begin_anchor(job,
UNSUPPORTED("8g7o4dsbwgp9ggtiktgt2m41t"); // 			      obj->url, obj->tooltip, obj->target,
UNSUPPORTED("c8tk2e711ojwsnar0y39a73cf"); // 			      obj->id);
UNSUPPORTED("4i1fd7rw5klkjsnyehf6v44a3"); //     if (desc)
UNSUPPORTED("8yueq6sa0qe98f00ykgedfrzl"); // 	fprintf(job->output_file,
UNSUPPORTED("aqf73hied952lsirjjyf0hfr4"); // 		"%.5g %.5g translate newpath user_shape_%d\n",
UNSUPPORTED("afxenk7cqa80e074cox3d04n5"); // 		ND_coord(n).x + desc->offset.x,
UNSUPPORTED("57mec07ttst0x3aspieywssni"); // 		ND_coord(n).y + desc->offset.y, desc->macro_id);
UNSUPPORTED("1bslo0pyyucx0zmdzt12sei6d"); //     ND_label(n)->pos = ND_coord(n);
UNSUPPORTED("8r8t0lgzzpigm1odig9a9yg1c"); //     emit_label(job, EMIT_NLABEL, ND_label(n));
UNSUPPORTED("amrlpbo0f5svfvv7e9lzhfzj9"); //     if (doMap) {
UNSUPPORTED("4drs7w0v5mk7ys9aylmo5lnq8"); // 	if (job->flags & (1<<2))
UNSUPPORTED("12436nj34of615tb94t3cw2h0"); // 	    gvrender_begin_anchor(job,
UNSUPPORTED("2rwb38hipr5rxkwxfdzzwkdmy"); // 				  obj->url, obj->tooltip, obj->target,
UNSUPPORTED("4x188hxybttaubn1tt4tf710k"); // 				  obj->id);
UNSUPPORTED("e3o6yrnsv8lko5fql4f8a9gly"); // 	gvrender_end_anchor(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d0jsei60yky7c36q8bja8q58d
// static pointf star_size (pointf sz0) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="star_size", key="d0jsei60yky7c36q8bja8q58d", definition="static pointf star_size (pointf sz0)")
public static Object star_size(Object... arg) {
UNSUPPORTED("6bl2ntfn97yev6qvlwplor61o"); // static pointf star_size (pointf sz0)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3zlnn621zia9mss7z1ay24myc"); //     pointf sz;
UNSUPPORTED("5u7lf36burm76yokjuxgcd4tn"); //     double r0, r, rx, ry;
UNSUPPORTED("9ya7hg30u0pmebfvhy4ba5kfp"); //     rx = sz0.x/(2*cos((M_PI/10.0)));
UNSUPPORTED("y09869s34d94qtdcsuuz4mjy"); //     ry = sz0.y/(sin((M_PI/10.0)) + sin((3*(M_PI/10.0))));
UNSUPPORTED("1qn336ppz1ubj5d9vmolmwhfa"); //     r0 = MAX(rx,ry);
UNSUPPORTED("99spig8n4dowh045zi2u054cf"); //     r = (r0*sin((2*(2*(M_PI/10.0))))*cos((2*(M_PI/10.0))))/(cos((M_PI/10.0))*cos((2*(2*(M_PI/10.0)))));
UNSUPPORTED("3h9e5okkzg8gzypvpzok96ikc"); //     sz.x = 2*r*cos((M_PI/10.0));
UNSUPPORTED("19ba70prhdthsxh7ukqn07tw9"); //     sz.y = r*(1 + sin((3*(M_PI/10.0))));
UNSUPPORTED("ban3s2canux7qwxava1n2e4v2"); //     return sz;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a7r80ro5nb15ttgfpqwayycmf
// static void star_vertices (pointf* vertices, pointf* bb) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="star_vertices", key="a7r80ro5nb15ttgfpqwayycmf", definition="static void star_vertices (pointf* vertices, pointf* bb)")
public static Object star_vertices(Object... arg) {
UNSUPPORTED("8p40gvc5ocryzfeoybiuc0tzd"); // static void star_vertices (pointf* vertices, pointf* bb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("6ebg0h9irk3pcisrj710o7d79"); //     pointf sz = *bb;
UNSUPPORTED("daheewjo1ertfvnwnfdg2fcxr"); //     double offset, a, aspect = (1 + sin((3*(M_PI/10.0))))/(2*cos((M_PI/10.0)));
UNSUPPORTED("6ir3jujwrh6dpiqug6p2v3ttj"); //     double r, r0, theta = M_PI/10.0;
UNSUPPORTED("3dcxsdbybxzvk7jsod9d2ubvm"); //     /* Scale up width or height to required aspect ratio */
UNSUPPORTED("o422759cptua4yuo9guk3367"); //     a = sz.y/sz.x;
UNSUPPORTED("4czf228z5owjh6ew3vh3ugvdv"); //     if (a > aspect) {
UNSUPPORTED("97gq966jokpf07dc3wv6hgk45"); // 	sz.x = sz.y/aspect;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7dnql7ghevwkt7vxe0s4wndha"); //     else if (a < aspect) {
UNSUPPORTED("aeoxa8vdht2x8kc1xojdwlz3j"); // 	sz.y = sz.x*aspect;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a8zuo1xnr4jnza3araqzl5q43"); //     /* for given sz, get radius */
UNSUPPORTED("5j3gmpqii05zcp2ncicxs2si"); //     r = sz.x/(2*cos((M_PI/10.0)));
UNSUPPORTED("214oro38cddbf9fk06d0m6duf"); //     r0 = (r*cos((M_PI/10.0))*cos((2*(2*(M_PI/10.0)))))/(sin((2*(2*(M_PI/10.0))))*cos((2*(M_PI/10.0))));
UNSUPPORTED("4rot7vm0whb5r2oo8ne4pn1j4"); //     /* offset is the y shift of circle center from bb center */
UNSUPPORTED("aa1u9d9ckbucmn1eyvyyijwkf"); //     offset = (r*(1 - sin((3*(M_PI/10.0)))))/2;
UNSUPPORTED("5zsqst1ddsdoai9yogpi1mnfl"); //     for (i = 0; i < 10; i += 2) {
UNSUPPORTED("dy5yk8kfoxfn3h4wby7vyciqz"); // 	vertices[i].x = r*cos(theta);
UNSUPPORTED("a3uapptgvfngiztwa4vm4pbuu"); // 	vertices[i].y = r*sin(theta) - offset;
UNSUPPORTED("7z0zntmu5ddcj6evxm9imjmv8"); // 	theta += (2*(M_PI/10.0));
UNSUPPORTED("da5vtvcsngi7wqtllzq8l190t"); // 	vertices[i+1].x = r0*cos(theta);
UNSUPPORTED("9zr96c70zwnim4wjqf6zn7p68"); // 	vertices[i+1].y = r0*sin(theta) - offset;
UNSUPPORTED("7z0zntmu5ddcj6evxm9imjmv8"); // 	theta += (2*(M_PI/10.0));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("58zowxx0q5742vxn8iad1i1xe"); //     *bb = sz;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5sbhhjvptmhgl2v2zc12aemgv
// static boolean star_inside(inside_t * inside_context, pointf p) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="star_inside", key="5sbhhjvptmhgl2v2zc12aemgv", definition="static boolean star_inside(inside_t * inside_context, pointf p)")
public static Object star_inside(Object... arg) {
UNSUPPORTED("2s46vczrfqrysl35qtk55j8dq"); // static boolean star_inside(inside_t * inside_context, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4ogz1m3q9xn7z7hiecjp98bmt"); //     static node_t *lastn;	/* last node argument */
UNSUPPORTED("ehzu6nig1i3kg2wnd7f7k9j5n"); //     static polygon_t *poly;
UNSUPPORTED("cy02ifkuodmjjlsu0kxnyjpoh"); //     static int outp, sides;
UNSUPPORTED("53wr032f7cpvhrjze3ml553bu"); //     static pointf *vertex;
UNSUPPORTED("c173x9hgi0epjtbq9crz665t6"); //     static pointf O;		/* point (0,0) */
UNSUPPORTED("4rtja2mn137n7wcxryrmo12ko"); //     boxf *bp = inside_context->s.bp;
UNSUPPORTED("d8oppi8gt9b4eaonkdgb7a54l"); //     node_t *n = inside_context->s.n;
UNSUPPORTED("eu67sekaddiid7bjwclyd9lpq"); //     pointf P, Q, R;
UNSUPPORTED("dk1ablxthh1rqusv958glmv1k"); //     int i, outcnt;
UNSUPPORTED("823iiqtx9pt0gijqrohrd3zx7"); //     P = ccwrotatepf(p, 90 * GD_rankdir(agraphof(n)));
UNSUPPORTED("9nc5qvx5xechvyre5wvhjqpjk"); //     /* Quick test if port rectangle is target */
UNSUPPORTED("8ix20ei8mhm5e1r57koylhxmw"); //     if (bp) {
UNSUPPORTED("48wucupbjgeu51wy1djengl4f"); // 	boxf bbox = *bp;
UNSUPPORTED("b87pzpk1bdd2rzscbmza3pxyu"); // 	return INSIDE(P, bbox);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8rl2cn4oxr94675yld5eohkie"); //     if (n != lastn) {
UNSUPPORTED("a7zf42vgzubszo05gyqjhr4bb"); // 	poly = (polygon_t *) ND_shape_info(n);
UNSUPPORTED("2y1ov1roe3ma4wlkdj2w8r3sg"); // 	vertex = poly->vertices;
UNSUPPORTED("98ormfm5j66dmbja3sdsx38az"); // 	sides = poly->sides;
UNSUPPORTED("d41xba93s17axh19qsbhg0x8a"); // 	/* index to outer-periphery */
UNSUPPORTED("bmmroksk9aecg8ik0z1sxpzie"); // 	outp = (poly->peripheries - 1) * sides;
UNSUPPORTED("47l17pa0edzmfnlr8ysqs0qh4"); // 	if (outp < 0)
UNSUPPORTED("jyf75douzxhfzxfyrq3kes6e"); // 	    outp = 0;
UNSUPPORTED("dz5401vppes7iz7b0c6pzkge6"); // 	lastn = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4zcxdh0y0cit31t1myzksbyc"); //     outcnt = 0;
UNSUPPORTED("bs8ipj0v83bijiw6u6kpz14s1"); //     for (i = 0; i < sides; i += 2) {
UNSUPPORTED("cmwbnui44mpmy3kjz18pxp1cd"); // 	Q = vertex[i + outp];
UNSUPPORTED("4oudcajkxkcstsh2bvjaheadi"); // 	R = vertex[((i+4) % sides) + outp];
UNSUPPORTED("b4anc6i6r4xczgkhjcjudktb"); // 	if (!(same_side(P, O, Q, R))) {
UNSUPPORTED("b291xvw4hm8vcmlaoxcl8dj94"); // 	    outcnt++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3mpbcjrh8r4u6u2twxvyqx9v9"); // 	if (outcnt == 2) {
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8fwlqtemsmckleh6946lyd8mw"); //     return NOT(0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 7nso0aprwwsa0je3az7h9nlue
static private CString side_port[] = { new CString("s"), new CString("e"), new CString("n"), new CString("w") };




//3 8hx6dn19tost35djnvvnzh92y
// static point cvtPt(pointf p, int rankdir) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="cvtPt", key="8hx6dn19tost35djnvvnzh92y", definition="static point cvtPt(pointf p, int rankdir)")
public static ST_point cvtPt(final ST_pointf p, int rankdir) {
	return (ST_point) cvtPt_(p.copy(), rankdir).copy();
}
private static ST_point cvtPt_(final ST_pointf p, int rankdir) {
    final ST_pointf q = new ST_pointf(); // { 0, 0 };
    final ST_point Q = new ST_point();
    switch (rankdir) {
    case 0:
	q.___(p);
	break;
    case 2:
UNSUPPORTED("drh1t5heo8w8z199n0vydnon7"); // 	q.x = p.x;
UNSUPPORTED("1sp6xbp6wduyl3r6q3ki03lj"); // 	q.y = -p.y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    case 1:
UNSUPPORTED("aqxuqmimmi2id7ukk2b64x1in"); // 	q.y = p.x;
UNSUPPORTED("djyedvti0u3rb22lyp3mp7i8n"); // 	q.x = -p.y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    case 3:
UNSUPPORTED("aqxuqmimmi2id7ukk2b64x1in"); // 	q.y = p.x;
UNSUPPORTED("7d33c84ojx2qc6awisfs88pf5"); // 	q.x = p.y;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
    }
    PF2P(q, Q);
    return Q;
}




//3 cmt4wr13jgcd9ihg14t972aam
// static char *closestSide(node_t * n, node_t * other, port * oldport) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="", key="cmt4wr13jgcd9ihg14t972aam", definition="static char *closestSide(node_t * n, node_t * other, port * oldport)")
public static CString closestSide(ST_Agnode_s n, ST_Agnode_s other, ST_port oldport) {
    final ST_boxf b = new ST_boxf();
    int rkd = GD_rankdir(agraphof(n).root);
    final ST_point p = new ST_point(); // { 0, 0 };
    final ST_point pt = cvtPt(ND_coord(n), rkd);
    final ST_point opt = cvtPt(ND_coord(other), rkd);
    int sides = oldport.side;
    CString rv = null;
    int i, d, mind = 0;
    if ((sides == 0) || (sides == (TOP | BOTTOM | LEFT | RIGHT)))
	return rv;		/* use center */
    if (oldport.bp != null) {
	b.___(oldport.bp);
    } else {
UNSUPPORTED("ek9a7u2yx8w4r9x5k7somxuup"); // 	if (GD_flip(agraphof(n))) {
UNSUPPORTED("5m0qxjiybs5ei0xyt8rztghk5"); // 	    b.UR.x = ND_ht(n) / 2;
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("7luuqd8n7bpffoa8v27jp7tn3"); // 	    b.UR.y = ND_lw(n);
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("2kqd0a7y22hequs0ypjfw2ltw"); // 	    b.UR.y = ND_ht(n) / 2;
UNSUPPORTED("922vazdrkwhoxxy4yw5axu6i7"); // 	    b.LL.y = -b.UR.y;
UNSUPPORTED("59beisnsabbp6eavnuxrqch2d"); // 	    b.UR.x = ND_lw(n);
UNSUPPORTED("1i4y4dgrig36gh0dq2jn8kde"); // 	    b.LL.x = -b.UR.x;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    for (i = 0; i < 4; i++) {
	if ((sides & (1 << i)) == 0)
	    continue;
	switch (i) {
	case 0:
	    p.y = (int) b.LL.y;
	    p.x = (int) (b.LL.x + b.UR.x) / 2;
	    break;
	case 1:
	    p.x = (int) b.UR.x;
	    p.y = (int) (b.LL.y + b.UR.y) / 2;
	    break;
	case 2:
	    p.y = (int) b.UR.y;
	    p.x = (int) (b.LL.x + b.UR.x) / 2;
	    break;
	case 3:
	    p.x = (int) b.LL.x;
	    p.y = (int) (b.LL.y + b.UR.y) / 2;
	    break;
	}
	p.x += pt.x;
	p.y += pt.y;
	d = (int) DIST2(p, opt);
	if (N(rv) || (d < mind)) {
	    mind = d;
	    rv = side_port[i];
	}
    }
    return rv;
}




//3 ckbg1dyu9jzx7g0c9dbriez7r
// port resolvePort(node_t * n, node_t * other, port * oldport) 
@Unused
@Doc("Choose closestSide of a node")
@Original(version="2.38.0", path="lib/common/shapes.c", name="resolvePort", key="ckbg1dyu9jzx7g0c9dbriez7r", definition="port resolvePort(node_t * n, node_t * other, port * oldport)")
public static ST_port resolvePort(ST_Agnode_s n, ST_Agnode_s other, ST_port oldport) {
// WARNING!! STRUCT
return resolvePort_w_(n, other, oldport).copy();
}
private static ST_port resolvePort_w_(ST_Agnode_s n, ST_Agnode_s other, ST_port oldport) {
ENTERING("ckbg1dyu9jzx7g0c9dbriez7r","resolvePort");
try {
    final ST_port rv = new ST_port();
    CString compass = closestSide(n, other, oldport);
    /* transfer name pointer; all other necessary fields will be regenerated */
    rv.name = oldport.name;
    compassPort(n, oldport.bp, rv, compass, oldport.side, null);
    return rv;
} finally {
LEAVING("ckbg1dyu9jzx7g0c9dbriez7r","resolvePort");
}
}




//3 9ttd9vkih0mogy1ps3khfjum6
// void resolvePorts(edge_t * e) 
@Unused
@Original(version="2.38.0", path="lib/common/shapes.c", name="resolvePorts", key="9ttd9vkih0mogy1ps3khfjum6", definition="void resolvePorts(edge_t * e)")
public static Object resolvePorts(Object... arg) {
UNSUPPORTED("ceen1bdr2y10gl9g3stj9dq13"); // void resolvePorts(edge_t * e)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1avf6c49h37pc64khn45b3zla"); //     if (ED_tail_port(e).dyna)
UNSUPPORTED("bjgkohc8n22pf9yf5anfmfjdl"); // 	ED_tail_port(e) =
UNSUPPORTED("c5phu7zavynmooq4ykt058d6t"); // 	    resolvePort(agtail(e), aghead(e), &ED_tail_port(e));
UNSUPPORTED("56ff4qr7o1xsl73k68f4kjmd1"); //     if (ED_head_port(e).dyna)
UNSUPPORTED("d4aylrk5xwagx7so633xn35ug"); // 	ED_head_port(e) =
UNSUPPORTED("ctvcevp7oejtitu1hunh3yj02"); // 	    resolvePort(aghead(e), agtail(e), &ED_head_port(e));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
