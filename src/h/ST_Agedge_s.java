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

import smetana.core.HardcodedStruct;
import smetana.core.OFFSET;
import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Agedge_s extends UnsupportedStructAndPtr implements HardcodedStruct {

	private final StarStruct parent;

	public final ST_Agobj_s base = new ST_Agobj_s(this);
	public final ST_dtlink_s id_link = new ST_dtlink_s(this);
	public final ST_dtlink_s seq_link = new ST_dtlink_s(this);
	public ST_Agnode_s node;

	public static class ArrayOfStar extends UnsupportedArrayOfPtr implements __ptr__, __array_of_ptr__ {

		private final List<ST_Agedge_s> data;
		private final int pos;

		public ArrayOfStar(int size) {
			this.data = new ArrayList<ST_Agedge_s>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(null);
			}
		}

		public void swap(int i, int j) {
			ST_Agedge_s e1 = data.get(i);
			ST_Agedge_s e2 = data.get(j);
			data.set(i, e2);
			data.set(j, e1);
		}

		public ArrayOfStar(List<ST_Agedge_s> data, int pos) {
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
		
		public ST_Agedge_s get(int i) {
			return plus(i).getPtr();
		}


		@Override
		public ArrayOfStar asPtr() {
			return this;
		}

		@Override
		public void setPtr(__ptr__ value) {
			this.data.set(pos, (ST_Agedge_s) value);
		}

		@Override
		public ST_Agedge_s getPtr() {
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

	@Override
	public void copyDataFrom(__ptr__ arg) {
		ST_Agedge_s this2 = (ST_Agedge_s) arg;
		this.base.copyDataFrom((__struct__) this2.base);
		this.id_link.copyDataFrom((__struct__) this2.id_link);
		this.seq_link.copyDataFrom((__struct__) this2.seq_link);
		this.node = this2.node;
	}

	public ST_Agedge_s() {
		this(null);
	}

	public ST_Agedge_s(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		ST_Agedge_s other2 = (ST_Agedge_s) other;
		return this == other2;
	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.ST_Agedge_s::seq_link")) {
			return seq_link;
		}
		if (offset.toString().equals("h.ST_Agedge_s::id_link")) {
			return id_link;
		}
		System.err.println("offset2=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	@Override
	public ST_Agedge_s plus(int pointerMove) {
		ST_Agedgepair_s pair = (ST_Agedgepair_s) parent;
		// Order=out, in
		if (pair.out == this && pointerMove == 1) {
			return pair.in;
		}
		if (pair.in == this && pointerMove == -1) {
			return pair.out;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agobj_s.class) {
			return base;
		}
		if (dest == ST_Agedge_s.class) {
			return this;
		}
		return super.castTo(dest);
	}
	
	public ST_Agobj_s castTo_ST_Agobj_s() {
		return base;
	}


	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("node")) {
			this.node = (ST_Agnode_s) newData;
			return node;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public ST_Agedge_s getPtr() {
		return this;
	}

	public StarStruct from_seq_link(ST_dtlink_s from) {
		if (from == seq_link) {
			return this;
		}
		throw new IllegalArgumentException();
	}

	public StarStruct from_id_link(ST_dtlink_s from) {
		if (from == id_link) {
			return this;
		}
		throw new IllegalArgumentException();
	}


	// public interface ST_Agedge_s extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agedge_s",
	// "{",
	// "Agobj_t base",
	// "Dtlink_t id_link",
	// "Dtlink_t seq_link",
	// "Agnode_t *node",
	// "}");
}

// struct Agedge_s {
// Agobj_t base;
// Dtlink_t id_link; /* main graph only */
// Dtlink_t seq_link;
// Agnode_t *node; /* the endpoint node */
// };