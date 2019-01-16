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

//2 8n228pyy8v750bhs0ubxnx0l9

public interface _xdot_op extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct _xdot_op",
"{",
"xdot_kind kind",
"union",
"{",
"xdot_rect ellipse",
"xdot_polyline polygon",
"xdot_polyline polyline",
"xdot_polyline bezier",
"xdot_text text",
"xdot_image image",
"char* color",
"xdot_color grad_color",
"xdot_font font",
"char* style",
"unsigned int fontchar",
"}",
"u",
"drawfunc_t drawfunc",
"}");
}

// struct _xdot_op {
//     xdot_kind kind;
//     union {
//       xdot_rect ellipse;       /* xd_filled_ellipse, xd_unfilled_ellipse */
//       xdot_polyline polygon;   /* xd_filled_polygon, xd_unfilled_polygon */
//       xdot_polyline polyline;  /* xd_polyline */
//       xdot_polyline bezier;    /* xd_filled_bezier,  xd_unfilled_bezier */
//       xdot_text text;          /* xd_text */
//       xdot_image image;        /* xd_image */
//       char* color;             /* xd_fill_color, xd_pen_color */
//       xdot_color grad_color;   /* xd_grad_fill_color, xd_grad_pen_color */
//       xdot_font font;          /* xd_font */
//       char* style;             /* xd_style */
//       unsigned int fontchar;   /* xd_fontchar */
//     } u;
//     drawfunc_t drawfunc;
// };