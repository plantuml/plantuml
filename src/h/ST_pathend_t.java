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
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_pathend_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_pathend_t() {
		this(null);
	}

	public ST_pathend_t(StarStruct parent) {
		this.parent = parent;
	}

	// "typedef struct pathend_t",
	// "{",
	public final ST_boxf nb = new ST_boxf(this);
	public final ST_pointf np = new ST_pointf(this);
	public int sidemask;
	public int boxn;

	public final ST_boxf boxes[] = new ST_boxf[] { new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(),
			new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(),
			new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(),
			new ST_boxf(), new ST_boxf() };

	// "boxf boxes[20]",
	// "}",
	// "pathend_t");

	@Override
	public __ptr__ getPtr() {
		return this;
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("np")) {
			this.np.___(newData);
			return;
		}
		if (fieldName.equals("nb")) {
			this.nb.___(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("boxn")) {
			this.boxn = data;
			return;
		}
		if (fieldName.equals("sidemask")) {
			this.sidemask = data;
			return;
		}
		super.setInt(fieldName, data);
	}

}

// typedef struct pathend_t {
// boxf nb; /* the node box */
// pointf np; /* node port */
// int sidemask;
// int boxn;
// boxf boxes[20];
// } pathend_t;