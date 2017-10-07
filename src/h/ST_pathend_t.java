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

import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_struct__;
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
	private final ST_boxf nb = new ST_boxf(this);
	private final ST_pointf np = new ST_pointf(this);
	private int sidemask;
	private int boxn;

	private final ST_boxf boxes[] = new ST_boxf[] { new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(),
			new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(),
			new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(), new ST_boxf(),
			new ST_boxf(), new ST_boxf() };

	// "boxf boxes[20]",
	// "}",
	// "pathend_t");

	class ArrayOfTwenty extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfTwenty(int pos) {
			this.pos = pos;
		}

		@Override
		public __array_of_struct__ plus(int delta) {
			return new ArrayOfTwenty(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return boxes[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			boxes[pos].copyDataFrom(value);
		}

		@Override
		public double getDouble(String fieldName) {
			return getStruct().getDouble(fieldName);
		}

	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {
		@Override
		public void setStruct(String fieldName, __struct__ newData) {
			ST_pathend_t.this.setStruct(fieldName, newData);
		}

		@Override
		public __array_of_struct__ getArrayOfStruct(String fieldName) {
			return ST_pathend_t.this.getArrayOfStruct(fieldName);
		}

		@Override
		public int getInt(String fieldName) {
			return ST_pathend_t.this.getInt(fieldName);
		}

		@Override
		public void setInt(String fieldName, int data) {
			ST_pathend_t.this.setInt(fieldName, data);
		}

		@Override
		public __struct__ getStruct(String fieldName) {
			return ST_pathend_t.this.getStruct(fieldName);
		}

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
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("nb")) {
			return this.nb;
		}
		if (fieldName.equals("np")) {
			return this.np;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("boxn")) {
			return this.boxn;
		}
		if (fieldName.equals("sidemask")) {
			return this.sidemask;
		}
		return super.getInt(fieldName);
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

	@Override
	public __array_of_struct__ getArrayOfStruct(String fieldName) {
		if (fieldName.equals("boxes")) {
			return new ArrayOfTwenty(0);
		}
		return super.getArrayOfStruct(fieldName);
	}

}

// typedef struct pathend_t {
// boxf nb; /* the node box */
// pointf np; /* node port */
// int sidemask;
// int boxn;
// boxf boxes[20];
// } pathend_t;