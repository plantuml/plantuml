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

import java.util.ArrayList;
import java.util.List;

import smetana.core.UnsupportedArrayOfStruct2;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarStruct;

public class ST_xlabel_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_xlabel_t() {
		this(null);
	}

	public ST_xlabel_t(StarStruct parent) {
		this.parent = parent;
	}

	// "typedef struct",
	// "{",
	final public ST_pointf sz = new ST_pointf(this);
	final public ST_pointf pos = new ST_pointf(this);

	// "void *lbl",
	public ST_textlabel_t lbl;
	public int set;

	// "unsigned char set",
	// "}",
	// "xlabel_t");
	
	public static class Array extends UnsupportedArrayOfStruct2 implements __ptr__, __array_of_ptr__{

		private final List<ST_xlabel_t> data;
		private final int pos_;
		
		@Override
		public void setInt(String fieldName, int data) {
			get(0).setInt(fieldName, data);
		}
		
		@Override
		public void setStruct(__struct__ value) {
			get(0).___(value);
		}
		
		@Override
		public Array asPtr() {
			return this;
		}
		
		@Override
		public void setDouble(String fieldName, double value) {
			get(0).setDouble(fieldName, value);
		}
		
		@Override
		public ST_xlabel_t getStruct() {
			return get(0).getStruct();
		}
		
		@Override
		public __ptr__ getPtr() {
			return get(0);
		}

		public Array(int size) {
			this.data = new ArrayList<ST_xlabel_t>();
			this.pos_ = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_xlabel_t());
			}
		}

		public Array reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_xlabel_t());
			}
			return this;
		}
		
		public Array plus(int delta) {
			return plusJ(delta);
		}

		private Array(List<ST_xlabel_t> data, int pos) {
			this.data = data;
			this.pos_ = pos;
		}

		public ST_xlabel_t get(int i) {
			return this.data.get(pos_ + i);
		}

		public Array plusJ(int i) {
			return new Array(data, pos_ + i);
		}

		public int minus(Array other) {
			if (this.data != other.data) {
				throw new IllegalArgumentException();
			}
			return this.pos_ - other.pos_;
		}

		public Array move(int delta) {
			throw new UnsupportedOperationException(getClass().toString());
		}


		public void realloc(size_t nb) {
			throw new UnsupportedOperationException(getClass().toString());
		}

		public int comparePointerInternal(__array_of_ptr__ other) {
			throw new UnsupportedOperationException(getClass().toString());
		}


	}
	

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("set")) {
			this.set = data;
			return;
		}
		super.setInt(fieldName, data);
	}
	
	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("lbl")) {
			this.lbl = (ST_textlabel_t) newData;
			return this.lbl;
		}
		return super.setPtr(fieldName, newData);
	}
	
	@Override
	public ST_xlabel_t getStruct() {
		return this;
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("sz")) {
			this.sz.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("pos")) {
			this.pos.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

//	public static size_t sizeof(final int nb) {
//		return new UnsupportedSize_t(nb) {
//			@Override
//			public Object malloc() {
//				return new StarArrayOfPtr(new STArray<ST_xlabel_t>(nb, 0, ST_xlabel_t.class));
//			}
//
//			@Override
//			public int getInternalNb() {
//				return nb;
//			}
//
//			@Override
//			public Object realloc(Object old) {
//				StarArrayOfPtr old2 = (StarArrayOfPtr) old;
//				old2.realloc(nb);
//				return old2;
//			}
//		};
//	}

}

// typedef struct {
// pointf sz; /* Size of label (input) */
// pointf pos; /* Position of lower-left corner of label (output) */
// void *lbl; /* Pointer to label in the graph */
// unsigned char set; /* True if the position has been set (input/output) */
// } xlabel_t;