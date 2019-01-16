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
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_rank_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_rank_t() {
		this(null);
	}

	public ST_rank_t(StarStruct parent) {
		this.parent = parent;
	}

	public int n;

	// "node_t **v",
	public ST_Agnode_s.ArrayOfStar v;
	public int an;
	// "node_t **av",
	public ST_Agnode_s.ArrayOfStar av;

	public double ht1, ht2;
	public double pht1, pht2;
	public boolean candidate;
	public int valid;

	public int cache_nc;
	public ST_adjmatrix_t flat;

	// "}",
	// "rank_t");
	
	
	public static class Array2 extends UnsupportedArrayOfPtr implements __ptr__, __array_of_ptr__ {

		private final List<ST_rank_t> data;
		private final int pos;

		public Array2(int size) {
			this.data = new ArrayList<ST_rank_t>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_rank_t());
			}
		}
		
		@Override
		public void setInt(String fieldName, int data) {
			getPtr().setInt(fieldName, data);
		}
		
		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return getPtr().setPtr(fieldName, newData);
		}
		
		@Override
		public void setDouble(String fieldName, double data) {
			getPtr().setDouble(fieldName, data);
		}
		
		public ST_rank_t get(int i) {
			return plus(i).getPtr();
		}
		
		public void swap(int i, int j) {
			ST_rank_t e1 = data.get(i);
			ST_rank_t e2 = data.get(j);
			data.set(i, e2);
			data.set(j, e1);
		}

		public Array2(List<ST_rank_t> data, int pos) {
			this.data = data;
			this.pos = pos;
		}
		
		public Array2 reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_rank_t());
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
//			if (value instanceof Amp) {
//				value = value.getPtr();
//			}
			this.data.set(pos, (ST_rank_t) value);
		}
		
		@Override
		public ST_rank_t getPtr() {
			return this.data.get(pos);
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


	@Override
	public void setStruct(__struct__ value) {
		ST_rank_t this2 = (ST_rank_t) value;
		this.n = this2.n;
		this.v = this2.v;
		this.an = this2.an;
		this.av = this2.av;
		this.ht1 = this2.ht1;
		this.ht2 = this2.ht2;
		this.pht1 = this2.pht1;
		this.pht2 = this2.pht2;
		this.candidate = this2.candidate;
		this.valid = this2.valid;
		this.cache_nc = this2.cache_nc;
		this.flat = this2.flat;
	}


	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("n")) {
			this.n = data;
			return;
		}
		if (fieldName.equals("an")) {
			this.an = data;
			return;
		}
		if (fieldName.equals("valid")) {
			this.valid = data;
			return;
		}
		if (fieldName.equals("cache_nc")) {
			this.cache_nc = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("pht1")) {
			this.pht1 = data;
			return;
		}
		if (fieldName.equals("pht2")) {
			this.pht2 = data;
			return;
		}
		if (fieldName.equals("ht1")) {
			this.ht1 = data;
			return;
		}
		if (fieldName.equals("ht2")) {
			this.ht2 = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("v")) {
			this.v = (ST_Agnode_s.ArrayOfStar) newData;
			return v;
		}
		if (fieldName.equals("av")) {
			this.av = (ST_Agnode_s.ArrayOfStar) newData;
			return av;
		}
		if (fieldName.equals("flat")) {
			this.flat = (ST_adjmatrix_t) newData;
			return flat;
		}
		return super.setPtr(fieldName, newData);
	}

//	public static size_t sizeof(final int nb) {
//		return new UnsupportedSize_t(nb) {
//			@Override
//			public Array2 malloc() {
//				return new Array2(nb);
//			}
//
//			@Override
//			public int getInternalNb() {
//				return nb;
//			}
//
//			@Override
//			public Array2 realloc(Object old) {
//				Array2 old2 = (Array2) old;
//				old2.reallocJ(nb);
//				return old2;
//			}
//		};
//	}

}

// typedef struct rank_t {
// int n; /* number of nodes in this rank */
// node_t **v; /* ordered list of nodes in rank */
// int an; /* globally allocated number of nodes */
// node_t **av; /* allocated list of nodes in rank */
// double ht1, ht2; /* height below/above centerline */
// double pht1, pht2; /* as above, but only primitive nodes */
// boolean candidate; /* for transpose () */
// boolean valid;
// int cache_nc; /* caches number of crossings */
// adjmatrix_t *flat;
// } rank_t;