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
package h;
import java.util.Arrays;
import java.util.List;

import smetana.core.__ptr__;

//2 7rfvc65ygzocig64xmd4vm8m8

public interface obj_state_s extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct obj_state_s",
"{",
"obj_state_t *parent",
"obj_type type",
"union",
"{",
"graph_t *g",
"graph_t *sg",
"node_t *n",
"edge_t *e",
"}",
"u",
"emit_state_t emit_state",
"gvcolor_t pencolor, fillcolor, stopcolor",
"int gradient_angle",
"float gradient_frac",
"pen_type pen",
"fill_type fill",
"double penwidth",
"char **rawstyle",
"double z, tail_z, head_z",
"char *label",
"char *xlabel",
"char *taillabel",
"char *headlabel",
"char *url",
"char *id",
"char *labelurl",
"char *tailurl",
"char *headurl",
"char *tooltip",
"char *labeltooltip",
"char *tailtooltip",
"char *headtooltip",
"char *target",
"char *labeltarget",
"char *tailtarget",
"char *headtarget",
"int explicit_tooltip:1",
"int explicit_tailtooltip:1",
"int explicit_headtooltip:1",
"int explicit_labeltooltip:1",
"int explicit_tailtarget:1",
"int explicit_headtarget:1",
"int explicit_edgetarget:1",
"int explicit_tailurl:1",
"int explicit_headurl:1",
"map_shape_t url_map_shape",
"int url_map_n",
"pointf *url_map_p",
"int url_bsplinemap_poly_n",
"int *url_bsplinemap_n",
"pointf *url_bsplinemap_p",
"int tailendurl_map_n",
"pointf *tailendurl_map_p",
"int headendurl_map_n",
"pointf *headendurl_map_p",
"}");
}

// struct obj_state_s {
// 	obj_state_t *parent;
// 
// 	obj_type type;
// 	union {
// 	    graph_t *g;
// 	    graph_t *sg;  
// 	    node_t *n;
// 	    edge_t *e;
// 	} u;
// 
// 	emit_state_t emit_state; 
// 
// 	gvcolor_t pencolor, fillcolor, stopcolor;
// 	int gradient_angle;
// 	float gradient_frac;
// 	pen_type pen;
// 	fill_type fill;
// 	double penwidth;
// 	char **rawstyle;
// 
// 	double z, tail_z, head_z;   /* z depths for 2.5D renderers such as vrml */
// 
// 	/* fully substituted text strings */
// 	char *label;
// 	char *xlabel;
// 	char *taillabel;
// 	char *headlabel; 
// 
// 	char *url;              /* if GVRENDER_DOES_MAPS */
// 	char *id;
// 	char *labelurl;
// 	char *tailurl;
// 	char *headurl; 
// 
// 	char *tooltip;          /* if GVRENDER_DOES_TOOLTIPS */
// 	char *labeltooltip;
// 	char *tailtooltip;
// 	char *headtooltip; 
// 
// 	char *target;           /* if GVRENDER_DOES_TARGETS */
// 	char *labeltarget;
// 	char *tailtarget;
// 	char *headtarget; 
// 
// 	int explicit_tooltip:1;
// 	int explicit_tailtooltip:1;
// 	int explicit_headtooltip:1;
// 	int explicit_labeltooltip:1;
// 	int explicit_tailtarget:1;
// 	int explicit_headtarget:1;
// 	int explicit_edgetarget:1;
// 	int explicit_tailurl:1;
// 	int explicit_headurl:1;
// 
// 	/* primary mapped region - node shape, edge labels */
// 	map_shape_t url_map_shape; 
// 	int url_map_n;                  /* number of points for url map if GVRENDER_DOES_MAPS */
// 	pointf *url_map_p;
// 
// 	/* additonal mapped regions for edges */
// 	int url_bsplinemap_poly_n;      /* number of polygons in url bspline map
// 					 if GVRENDER_DOES_MAPS && GVRENDER_DOES_MAP_BSPLINES */
// 	int *url_bsplinemap_n;          /* array of url_bsplinemap_poly_n ints 
// 					 of number of points in each polygon */
// 	pointf *url_bsplinemap_p;       /* all the polygon points */
// 
// 	int tailendurl_map_n;           /* tail end intersection with node */
// 	pointf *tailendurl_map_p;
// 
// 	int headendurl_map_n;           /* head end intersection with node */
// 	pointf *headendurl_map_p;
//     };