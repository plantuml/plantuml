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

import h.ST_pointf.Array;

import java.util.ArrayList;
import java.util.List;

import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
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
	public ST_pointf.Array list;

	public int size;
	public int sflag, eflag;

	public final ST_pointf sp = new ST_pointf(this), ep = new ST_pointf(this);

	public static class Array2 extends UnsupportedArrayOfPtr implements __ptr__, __array_of_ptr__ {

		private final List<ST_bezier> data;
		private final int pos;

		public Array2(int size) {
			this.data = new ArrayList<ST_bezier>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_bezier());
			}
		}

		@Override
		public ST_bezier getStruct() {
			return data.get(pos);
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return getStruct().setPtr(fieldName, newData);
		}

		@Override
		public void setStruct(String fieldName, __struct__ newData) {
			getStruct().setStruct(fieldName, newData);
		}

		@Override
		public void setInt(String fieldName, int data) {
			getStruct().setInt(fieldName, data);
		}

		// public void swap(int i, int j) {
		// ST_bezier e1 = data.get(i);
		// ST_bezier e2 = data.get(j);
		// data.set(i, e2);
		// data.set(j, e1);
		// }

		public Array2(List<ST_bezier> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public Array2 reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_bezier());
			}
			return this;
		}

		@Override
		public Array2 plus(int delta) {
			return new Array2(data, pos + delta);
		}
		
		@Override
		public Array2 asPtr() {
			return this;
		}

		@Override
		public void setPtr(__ptr__ value) {
			this.data.set(pos, (ST_bezier) value);
		}

		@Override
		public ST_bezier getPtr() {
			return this.data.get(pos);
		}
		
		public ST_bezier get(int i) {
			return this.plus(i).getPtr();
		}

		@Override
		public int comparePointer(__ptr__ other) {
			final Array2 this2 = (Array2) other;
			if (this.data != this2.data) {
				throw new IllegalArgumentException();
			}
			return this.pos - this2.pos;
		}

		public boolean isSameThan2(Array2 other) {
			if (this.data != other.data) {
				throw new IllegalArgumentException();
			}
			return this.pos == other.pos;
		}
	}

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
	public void ____(__ptr__ other) {
		___(((ST_bezier) other).getStruct());
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
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("list")) {
			this.list = (Array) newData;
			return this.list;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public ST_bezier getStruct() {
		return this;
	}

	@Override
	public ST_bezier getPtr() {
		return this;
	}

}

// typedef struct bezier {
// pointf *list;
// int size;
// int sflag, eflag;
// pointf sp, ep;
// } bezier;