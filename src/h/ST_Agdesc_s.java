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
package h;

import smetana.core.UnsupportedStarStruct;
import smetana.core.__struct__;

final public class ST_Agdesc_s extends UnsupportedStarStruct {
	public int directed; /* if edges are asymmetric */
	public int strict; /* if multi-edges forbidden */
	public int no_loop; /* if no loops */
	public int maingraph; /* if this is the top level graph */
	public int flatlock; /* if sets are flattened into lists in cdt */
	public int no_write; /* if a temporary subgraph */
	public int has_attrs; /* if string attr tables should be initialized */
	public int has_cmpnd; /* if may contain collapsed nodes */

	@Override
	public void ___(__struct__ other) {
		ST_Agdesc_s other2 = (ST_Agdesc_s) other;
		directed = other2.directed;
		strict = other2.strict;
		no_loop = other2.no_loop;
		maingraph = other2.maingraph;
		flatlock = other2.flatlock;
		no_write = other2.no_write;
		has_attrs = other2.has_attrs;
		has_cmpnd = other2.has_cmpnd;
	}

	@Override
	public __struct__ copy() {
		final ST_Agdesc_s result = new ST_Agdesc_s();
		result.directed = directed;
		result.strict = strict;
		result.no_loop = no_loop;
		result.maingraph = maingraph;
		result.flatlock = flatlock;
		result.no_write = no_write;
		result.has_attrs = has_attrs;
		result.has_cmpnd = has_cmpnd;
		return result;
	}
	

}

// struct Agdesc_s { /* graph descriptor */
// unsigned directed:1; /* if edges are asymmetric */
// unsigned strict:1; /* if multi-edges forbidden */
// unsigned no_loop:1; /* if no loops */
// unsigned maingraph:1; /* if this is the top level graph */
// unsigned flatlock:1; /* if sets are flattened into lists in cdt */
// unsigned no_write:1; /* if a temporary subgraph */
// unsigned has_attrs:1; /* if string attr tables should be initialized */
// unsigned has_cmpnd:1; /* if may contain collapsed nodes */
// };