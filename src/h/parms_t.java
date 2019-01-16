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

//2 9cd2h3321diwjh8ip8pl0j7no

public interface parms_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"int useGrid",
"int useNew",
"long seed",
"int numIters",
"int maxIters",
"int unscaled",
"double C",
"double Tfact",
"double K",
"double T0",
"int smode",
"double Cell",
"double Cell2",
"double K2",
"double Wd",
"double Ht",
"double Wd2",
"double Ht2",
"int pass1",
"int loopcnt",
"}",
"parms_t");
}

// typedef struct {
//     int useGrid;	/* use grid for speed up */
//     int useNew;		/* encode x-K into attractive force */
//     long seed;		/* seed for position RNG */
//     int numIters;	/* actual iterations in layout */
//     int maxIters;	/* max iterations in layout */
//     int unscaled;	/* % of iterations used in pass 1 */
//     double C;		/* Repulsion factor in xLayout */
//     double Tfact;	/* scale temp from default expression */
//     double K;		/* spring constant; ideal distance */
//     double T0;          /* initial temperature */
//     int smode;          /* seed mode */
//     double Cell;	/* grid cell size */
//     double Cell2;	/* Cell*Cell */
//     double K2;		/* K*K */
//     double Wd;		/* half-width of boundary */
//     double Ht;		/* half-height of boundary */
//     double Wd2;		/* Wd*Wd */
//     double Ht2;		/* Ht*Ht */
//     int pass1;		/* iterations used in pass 1 */
//     int loopcnt;        /* actual iterations in this pass */
// } parms_t;