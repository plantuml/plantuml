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

//2 7sm9vcfcserrygyo079543gdf

public interface ellipse_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"double cx, cy",
"double a, b",
"double theta, cosTheta, sinTheta",
"double eta1, eta2",
"double x1, y1, x2, y2",
"double xF1, yF1,  xF2, yF2",
"double xLeft",
"double yUp",
"double width, height",
"double f, e2, g, g2",
"}",
"ellipse_t");
}

// typedef struct {
//     double cx, cy;		/* center */
//     double a, b;		/* semi-major and -minor axes */
// 
//   /* Orientation of the major axis with respect to the x axis. */
//     double theta, cosTheta, sinTheta;
// 
//   /* Start and end angles of the arc. */
//     double eta1, eta2;
// 
//   /* Position of the start and end points. */
//     double x1, y1, x2, y2;
// 
//   /* Position of the foci. */
//     double xF1, yF1,  xF2, yF2;
// 
//   /* x of the leftmost point of the arc. */
//     double xLeft;
// 
//   /* y of the highest point of the arc. */
//     double yUp;
// 
//   /* Horizontal width and vertical height of the arc. */
//     double width, height;
// 
//     double f, e2, g, g2;
// } ellipse_t;