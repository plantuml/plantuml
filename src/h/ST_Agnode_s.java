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

import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedArrayOfStruct2;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarStruct;

public class ST_Agnode_s extends UnsupportedStructAndPtr {

	public final ST_Agobj_s base = new ST_Agobj_s(this);
	public ST_Agraph_s root;
	public final ST_Agsubnode_s mainsub = new ST_Agsubnode_s(this);

	private final StarStruct parent;

	public static class ArrayOfStar extends UnsupportedArrayOfPtr implements __ptr__, __array_of_ptr__ {

		private final List<ST_Agnode_s> data;
		private final int pos;

		public ArrayOfStar(int size) {
			this.data = new ArrayList<ST_Agnode_s>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(null);
			}
		}

		public void swap(int i, int j) {
			ST_Agnode_s e1 = data.get(i);
			ST_Agnode_s e2 = data.get(j);
			data.set(i, e2);
			data.set(j, e1);
		}

		public ArrayOfStar(List<ST_Agnode_s> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public ArrayOfStar reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(null);
			}
			return this;
		}

		@Override
		public ArrayOfStar plus(int delta) {
			return new ArrayOfStar(data, pos + delta);
		}

		@Override
		public ArrayOfStar asPtr() {
			return this;
		}

		@Override
		public void setPtr(__ptr__ value) {
			this.data.set(pos, (ST_Agnode_s) value);
		}
		
		public ST_Agnode_s get(int i) {
			return plus(i).getPtr();
		}

		@Override
		public ST_Agnode_s getPtr() {
			return this.data.get(pos);
		}

		@Override
		public int comparePointer(__ptr__ other) {
			final ArrayOfStar this2 = (ArrayOfStar) other;
			if (this.data != this2.data) {
				throw new IllegalArgumentException();
			}
			return this.pos - this2.pos;
		}

		public boolean isSameThan2(ArrayOfStar other) {
			if (this.data != other.data) {
				throw new IllegalArgumentException();
			}
			return this.pos == other.pos;
		}
	}

	public static class Array extends UnsupportedArrayOfStruct2 implements __ptr__, __array_of_ptr__ {

		private final List<ST_Agnode_s> data;
		private final int pos;

		@Override
		public void setStruct(__struct__ value) {
			get(0).___(value);
		}

		@Override
		public Array asPtr() {
			return this;
		}

		public Array(int size) {
			this.data = new ArrayList<ST_Agnode_s>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_Agnode_s());
			}
		}

		public Array reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_Agnode_s());
			}
			return this;
		}

		public Array plus(int delta) {
			return plusJ(delta);
		}

		@Override
		public void setPtr(__ptr__ value) {
			this.data.set(pos, (ST_Agnode_s) value);
		}

		@Override
		public ST_Agnode_s getPtr() {
			return this.data.get(pos);
		}

		private Array(List<ST_Agnode_s> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public ST_Agnode_s get(int i) {
			return this.data.get(pos + i);
		}

		public Array plusJ(int i) {
			return new Array(data, pos + i);
		}

		public int minus(Array other) {
			if (this.data != other.data) {
				throw new IllegalArgumentException();
			}
			return this.pos - other.pos;
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

	public ST_Agnode_s() {
		this(null);
	}

	public ST_Agnode_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agnode_s.class) {
			return this;
		}
		if (dest == ST_Agobj_s.class) {
			return base;
		}
		return super.castTo(dest);
	}
	
	public ST_Agobj_s castTo_ST_Agobj_s() {
		return base;
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		ST_Agnode_s other2 = (ST_Agnode_s) other;
		return this == other2;
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("root")) {
			this.root = (ST_Agraph_s) newData;
			return root;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public ST_Agnode_s getStruct() {
		return this;
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agnode_s",
	// "{",
	// "Agobj_t base",
	// "Agraph_t *root",
	// "Agsubnode_t mainsub",
	// "}");
}

// struct Agnode_s {
// Agobj_t base;
// Agraph_t *root;
// Agsubnode_t mainsub; /* embedded for main graph */
// };