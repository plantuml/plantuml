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

import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_bezier extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_bezier() {
		this(null);
	}

	public ST_bezier(StarStruct parent) {
		this.parent = parent;
	}

	// "{",
	// "pointf *list",
	private StarArrayOfPtr list;

	private int size;
	private int sflag, eflag;

	private final ST_pointf sp = new ST_pointf(this), ep = new ST_pointf(this);

	// "}",
	// "bezier");

	@Override
	public void ___(__struct__ other) {
		ST_bezier this2 = (ST_bezier) other;
		this.list = this2.list;
		this.size = this2.size;
		this.sflag = this2.sflag;
		this.eflag = this2.eflag;
		this.sp.copyDataFrom((__struct__) this2.sp);
		this.ep.copyDataFrom((__struct__) this2.ep);
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

		@Override
		public __ptr__ getPtr(String fieldName) {
			return ST_bezier.this.getPtr(fieldName);
		}

		@Override
		public int getInt(String fieldName) {
			return ST_bezier.this.getInt(fieldName);
		}
		
		@Override
		public __array_of_ptr__ getArrayOfPtr(String fieldName) {
			return ST_bezier.this.getArrayOfPtr(fieldName);
		}
		
		@Override
		public boolean getBoolean(String fieldName) {
			return ST_bezier.this.getBoolean(fieldName);
		}
		
		@Override
		public __struct__ getStruct(String fieldName) {
			return ST_bezier.this.getStruct(fieldName);
		}

	}

	@Override
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("sflag")) {
			return this.sflag != 0;
		}
		if (fieldName.equals("eflag")) {
			return this.eflag != 0;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("list")) {
			return list;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("list")) {
			this.list = (StarArrayOfPtr) newData;
			return list;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("sp")) {
			this.sp.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("ep")) {
			this.ep.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("size")) {
			this.size = data;
			return;
		}
		if (fieldName.equals("sflag")) {
			this.sflag = data;
			return;
		}
		if (fieldName.equals("eflag")) {
			this.eflag = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("size")) {
			return this.size;
		}
		if (fieldName.equals("sflag")) {
			return this.sflag;
		}
		if (fieldName.equals("eflag")) {
			return this.eflag;
		}
		return super.getInt(fieldName);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("sp")) {
			return this.sp;
		}
		if (fieldName.equals("ep")) {
			return this.ep;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		if (fieldName.equals("list")) {
			return this.list.getInternalArray();
		}
		return super.getArrayOfPtr(fieldName);
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_bezier>(nb, 0, ST_bezier.class));
			}

			@Override
			public int getInternalNb() {
				return nb;
			}

			@Override
			public Object realloc(Object old) {
				StarArrayOfPtr old2 = (StarArrayOfPtr) old;
				old2.realloc(nb);
				return old2;
			}
		};
	}

}

// typedef struct bezier {
// pointf *list;
// int size;
// int sflag, eflag;
// pointf sp, ep;
// } bezier;