/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import smetana.core.__ptr__;

final public class ST_dtdata_s extends UnsupportedStarStruct {

	public int type; /* type of dictionary */
	public ST_dtlink_s here; /* finger to last search element */
	public __ptr__ _htab; /* hash table */
	public ST_dtlink_s _head = null;
	// Dtlink_t* _head; /* linked list */
	// } hh;
	public int ntab; /* number of hash slots */
	public int size; /* number of objects */
	public int loop; /* number of nested loops */
	public int minp; /* min path before splay, always even */


}

// struct _dtdata_s
// { int type; /* type of dictionary */
// Dtlink_t* here; /* finger to last search element */
// union
// { Dtlink_t** _htab; /* hash table */
// Dtlink_t* _head; /* linked list */
// } hh;
// int ntab; /* number of hash slots */
// int size; /* number of objects */
// int loop; /* number of nested loops */
// int minp; /* min path before splay, always even */
// /* for hash dt, > 0: fixed table size */
// };