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

//2 drqmj6wswekevi9i4ghve61wj

public interface Agdesc_s extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct Agdesc_s",
"{",
"unsigned directed:1",
"unsigned strict:1",
"unsigned no_loop:1",
"unsigned maingraph:1",
"unsigned flatlock:1",
"unsigned no_write:1",
"unsigned has_attrs:1",
"unsigned has_cmpnd:1",
"}");
}

// struct Agdesc_s {		/* graph descriptor */
//     unsigned directed:1;	/* if edges are asymmetric */
//     unsigned strict:1;		/* if multi-edges forbidden */
//     unsigned no_loop:1;		/* if no loops */
//     unsigned maingraph:1;	/* if this is the top level graph */
//     unsigned flatlock:1;	/* if sets are flattened into lists in cdt */
//     unsigned no_write:1;	/* if a temporary subgraph */
//     unsigned has_attrs:1;	/* if string attr tables should be initialized */
//     unsigned has_cmpnd:1;	/* if may contain collapsed nodes */
// };