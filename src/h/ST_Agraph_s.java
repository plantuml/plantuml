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

import smetana.core.OFFSET;
import smetana.core.UnsupportedArrayOfStruct2;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarStruct;

public class ST_Agraph_s extends UnsupportedStructAndPtr {

	public final ST_Agobj_s base = new ST_Agobj_s(this);
	public final ST_Agdesc_s desc = new ST_Agdesc_s(this);
	public final ST_dtlink_s link = new ST_dtlink_s(this);

	public ST_dt_s n_seq; /* the node set in sequence */
	public ST_dt_s n_id; /* the node set indexed by ID */
	public ST_dt_s e_seq; /* holders for edge sets */
	public ST_dt_s e_id; /* holders for edge sets */
	public ST_dt_s g_dict; /* subgraphs - descendants */
	public ST_Agraph_s parent; /* subgraphs - ancestors */
	// Agraph_t *parent, *root; /* subgraphs - ancestors */
	public ST_Agraph_s root; /* subgraphs - ancestors */
	public ST_Agclos_s clos; /* shared resources */

	private final StarStruct _parent;

	public ST_Agraph_s() {
		this(null);
	}

	public ST_Agraph_s(StarStruct parent) {
		this._parent = parent;
	}

	public StarStruct getParent() {
		return _parent;
	}

	@Override
	public ST_Agraph_s getStruct() {
		return this;
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("desc")) {
			desc.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public ST_Agraph_s getPtr() {
		return this;
	}

	public static class Array extends UnsupportedArrayOfStruct2 implements __ptr__, __array_of_ptr__ {

		private final List<ST_Agraph_s> data;
		private final int pos;

		@Override
		public void setStruct(String fieldName, __struct__ data) {
			getStruct().setStruct(fieldName, data);
		}

		@Override
		public __ptr__ castTo(Class dest) {
			return getStruct().castTo(dest);
		}

		@Override
		public void setPtr(__ptr__ value) {
			this.data.set(pos, (ST_Agraph_s) value);
		}

		@Override
		public ST_Agraph_s getPtr() {
			return this.data.get(pos);
		}

		@Override
		public Array asPtr() {
			return this;
		}

		@Override
		public ST_Agraph_s getStruct() {
			return data.get(pos);
		}

		public Array(int size) {
			this.data = new ArrayList<ST_Agraph_s>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(null);
			}
		}

		public Array reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(null);
			}
			return this;
		}

		public Array plus(int delta) {
			return plusJ(delta);
		}

		private Array(List<ST_Agraph_s> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public ST_Agraph_s get(int i) {
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

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		OFFSET offset = OFFSET.fromInt(virtualBytes);
		// if (offset.toString().equals("h.Agedge_s::seq_link")) {
		// return seq_link;
		// }
		if (offset.toString().equals("h.ST_Agraph_s::link")) {
			return link;
		}
		System.err.println("offset156=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("clos")) {
			this.clos = (ST_Agclos_s) newData;
			return clos;
		}
		if (fieldName.equals("root")) {
			this.root = (ST_Agraph_s) newData;
			return root;
		}
		if (fieldName.equals("n_seq")) {
			this.n_seq = (ST_dt_s) newData;
			return n_seq;
		}
		if (fieldName.equals("n_id")) {
			this.n_id = (ST_dt_s) newData;
			return n_id;
		}
		if (fieldName.equals("e_seq")) {
			this.e_seq = (ST_dt_s) newData;
			return e_seq;
		}
		if (fieldName.equals("e_id")) {
			this.e_id = (ST_dt_s) newData;
			return e_id;
		}
		if (fieldName.equals("g_dict")) {
			this.g_dict = (ST_dt_s) newData;
			return g_dict;
		}
		if (fieldName.equals("parent")) {
			this.parent = (ST_Agraph_s) newData;
			return parent;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agobj_s.class) {
			return base;
		}
		if (dest == ST_Agraph_s.class) {
			return this;
		}
		return super.castTo(dest);
	}

	public ST_Agobj_s castTo_ST_Agobj_s() {
		return base;
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		ST_Agraph_s other2 = (ST_Agraph_s) other;
		return this == other2;
	}

	public StarStruct from_link(ST_dtlink_s from) {
		if (from == link) {
			return this;
		}
		throw new IllegalArgumentException();
	}

	//
	// public interface ST_Agraph_s extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agraph_s",
	// "{",
	// "Agobj_t base",
	// "Agdesc_t desc",
	// "Dtlink_t link",
	// "Dict_t *n_seq",
	// "Dict_t *n_id",
	// "Dict_t *e_seq, *e_id",
	// "Dict_t *g_dict",
	// "Agraph_t *parent, *root",
	// "Agclos_t *clos",
	// "}");
}

// struct Agraph_s {
// Agobj_t base;
// Agdesc_t desc;
// Dtlink_t link;
// Dict_t *n_seq; /* the node set in sequence */
// Dict_t *n_id; /* the node set indexed by ID */
// Dict_t *e_seq, *e_id; /* holders for edge sets */
// Dict_t *g_dict; /* subgraphs - descendants */
// Agraph_t *parent, *root; /* subgraphs - ancestors */
// Agclos_t *clos; /* shared resources */
// };