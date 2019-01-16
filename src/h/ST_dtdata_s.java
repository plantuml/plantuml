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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_dtdata_s extends UnsupportedStructAndPtr {

	private final StarStruct parent;
	
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


	public ST_dtdata_s() {
		this(null);
	}

	public ST_dtdata_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("type")) {
			this.type = data;
			return;
		}
		if (fieldName.equals("ntab")) {
			this.ntab = data;
			return;
		}
		if (fieldName.equals("size")) {
			this.size = data;
			return;
		}
		if (fieldName.equals("loop")) {
			this.loop = data;
			return;
		}
		if (fieldName.equals("minp")) {
			this.minp = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	// public interface ST_dtdata_s extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "struct _dtdata_s",
	// "{",
	// "int  type",
	// "Dtlink_t* here",
	// "union",
	// "{",
	// "Dtlink_t** _htab",
	// "Dtlink_t* _head",
	// "}",
	// "hh",
	// "int  ntab",
	// "int  size",
	// "int  loop",
	// "int  minp",
	// "}");
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