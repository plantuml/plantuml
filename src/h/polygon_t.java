/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

//2 23el548cy5m8f5wz8b2x5tihx

public interface polygon_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct polygon_t",
"{",
"int regular",
"int peripheries",
"int sides",
"double orientation",
"double distortion",
"double skew",
"int option",
"pointf *vertices",
"}",
"polygon_t");
}

// typedef struct polygon_t {	/* mutable shape information for a node */
// 	int regular;		/* true for symmetric shapes */
// 	int peripheries;	/* number of periphery lines */
// 	int sides;		/* number of sides */
// 	double orientation;	/* orientation of shape (+ve degrees) */
// 	double distortion;	/* distortion factor - as in trapezium */
// 	double skew;		/* skew factor - as in parallelogram */
// 	int option;		/* ROUNDED, DIAGONAL corners, etc. */
// 	pointf *vertices;	/* array of vertex points */
//     } polygon_t;