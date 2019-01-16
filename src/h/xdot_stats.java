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

//2 ckvi0csmdepbsh2fme77kpwds

public interface xdot_stats extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"int cnt",
"int n_ellipse",
"int n_polygon",
"int n_polygon_pts",
"int n_polyline",
"int n_polyline_pts",
"int n_bezier",
"int n_bezier_pts",
"int n_text",
"int n_font",
"int n_style",
"int n_color",
"int n_image",
"int n_gradcolor",
"int n_fontchar",
"}",
"xdot_stats");
}

// typedef struct {
//     int cnt;  /* no. of xdot ops */
//     int n_ellipse;
//     int n_polygon;
//     int n_polygon_pts;
//     int n_polyline;
//     int n_polyline_pts;
//     int n_bezier;
//     int n_bezier_pts;
//     int n_text;
//     int n_font;
//     int n_style;
//     int n_color;
//     int n_image;
//     int n_gradcolor;
//     int n_fontchar;
// } xdot_stats;