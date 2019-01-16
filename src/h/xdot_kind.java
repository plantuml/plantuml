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

//2 4a4izg5kkwsn1z1946vyklb3b

public interface xdot_kind extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef enum",
"{",
"xd_filled_ellipse, xd_unfilled_ellipse,     xd_filled_polygon, xd_unfilled_polygon,     xd_filled_bezier,  xd_unfilled_bezier,     xd_polyline,       xd_text,     xd_fill_color,     xd_pen_color, xd_font, xd_style, xd_image,     xd_grad_fill_color,     xd_grad_pen_color,     xd_fontchar",
"}",
"xdot_kind");
}

// typedef enum {
//     xd_filled_ellipse, xd_unfilled_ellipse,
//     xd_filled_polygon, xd_unfilled_polygon,
//     xd_filled_bezier,  xd_unfilled_bezier,
//     xd_polyline,       xd_text,
//     xd_fill_color,     xd_pen_color, xd_font, xd_style, xd_image,
//     xd_grad_fill_color,     xd_grad_pen_color,
//     xd_fontchar
// } xdot_kind;