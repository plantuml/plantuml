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

//2 71w78cxaan0929s365t8kncr6

public interface port extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct port",
"{",
"pointf p",
"double theta",
"boxf *bp",
"boolean defined",
"boolean constrained",
"boolean clip",
"boolean dyna",
"unsigned char order",
"unsigned char side",
"char *name",
"}",
"port");
}

// typedef struct port {	/* internal edge endpoint specification */
// 	pointf p;		/* aiming point relative to node center */
// 	double theta;		/* slope in radians */
// 	boxf *bp;		/* if not null, points to bbox of 
// 				 * rectangular area that is port target
// 				 */
// 	boolean	defined;        /* if true, edge has port info at this end */
// 	boolean	constrained;    /* if true, constraints such as theta are set */
// 	boolean clip;           /* if true, clip end to node/port shape */
// 	boolean dyna;           /* if true, assign compass point dynamically */
// 	unsigned char order;	/* for mincross */
// 	unsigned char side;	/* if port is on perimeter of node, this
//                                  * contains the bitwise OR of the sides (TOP,
//                                  * BOTTOM, etc.) it is on. 
//                                  */
// 	char *name;		/* port name, if it was explicitly given, otherwise NULL */
//     } port;