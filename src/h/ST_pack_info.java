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

public class ST_pack_info extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_pack_info() {
		this(null);
	}

	public ST_pack_info(StarStruct parent) {
		this.parent = parent;
	}

	// "typedef struct",
	// "{",
	// "float aspect",
	public int sz;
	public int margin;
	private int doSplines;
	// "pack_mode mode",
	public int mode;
	private __ptr__ fixed;
	// "boolean *fixed",
	// "packval_t* vals",
	public __ptr__ vals;
	public int flags;

	// "}",
	// "pack_info");

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("flags")) {
			this.flags = data;
			return;
		}
		if (fieldName.equals("mode")) {
			this.mode = data;
			return;
		}
		if (fieldName.equals("sz")) {
			this.sz = data;
			return;
		}
		if (fieldName.equals("margin")) {
			this.margin = data;
			return;
		}
		if (fieldName.equals("doSplines")) {
			this.doSplines = data;
			return;
		}
		if (fieldName.equals("fixed") && data == 0) {
			this.fixed = null;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("vals") && newData == null) {
			this.vals = newData;
			return vals;
		}
		return super.setPtr(fieldName, newData);
	}
}

// typedef struct {
// float aspect; /* desired aspect ratio */
// int sz; /* row/column size size */
// unsigned int margin; /* margin left around objects, in points */
// int doSplines; /* use splines in constructing graph shape */
// pack_mode mode; /* granularity and method */
// boolean *fixed; /* fixed[i] == true implies g[i] should not be moved */
// packval_t* vals; /* for arrays, sort numbers */
// int flags;
// } pack_info;