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

import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_rank_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_rank_t() {
		this(null);
	}

	public ST_rank_t(StarStruct parent) {
		this.parent = parent;
	}

	private int n;

	// "node_t **v",
	private STStarArrayOfPointer v;
	private int an;
	// "node_t **av",
	private STStarArrayOfPointer av;

	private double ht1, ht2;
	private double pht1, pht2;
	private boolean candidate;
	private int valid;

	private int cache_nc;
	private ST_adjmatrix_t flat;

	// "}",
	// "rank_t");

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
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {
		@Override
		public int getInt(String fieldName) {
			return ST_rank_t.this.getInt(fieldName);
		}

		@Override
		public __array_of_ptr__ getArrayOfPtr(String fieldName) {
			return ST_rank_t.this.getArrayOfPtr(fieldName);
		}

		@Override
		public __ptr__ getPtr(String fieldName) {
			return ST_rank_t.this.getPtr(fieldName);
		}

		@Override
		public __struct__ getStruct() {
			return ST_rank_t.this.getStruct();
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return ST_rank_t.this.setPtr(fieldName, newData);
		}

		@Override
		public void setBoolean(String fieldName, boolean data) {
			ST_rank_t.this.setBoolean(fieldName, data);
		}

		@Override
		public boolean getBoolean(String fieldName) {
			return ST_rank_t.this.getBoolean(fieldName);
		}

		@Override
		public void setInt(String fieldName, int data) {
			ST_rank_t.this.setInt(fieldName, data);
		}

		@Override
		public double getDouble(String fieldName) {
			return ST_rank_t.this.getDouble(fieldName);
		}

		@Override
		public void setStruct(__struct__ value) {
			ST_rank_t.this.setStruct(value);
		}

	}

	@Override
	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		if (fieldName.equals("v")) {
			return new ArrayOfPtr(v, 0);
		}
		return super.getArrayOfPtr(fieldName);
	}

	static class ArrayOfPtr extends UnsupportedArrayOfPtr {

		private final int pos;
		private final STStarArrayOfPointer tab;

		private ArrayOfPtr(STStarArrayOfPointer tab, int pos) {
			this.pos = pos;
			this.tab = tab;
		}

		@Override
		public __array_of_ptr__ plus(int delta) {
			return new ArrayOfPtr(tab, pos + delta);
		}

		@Override
		public void setPtr(__ptr__ value) {
			tab.plus(pos).setPtr(value);
		}

		@Override
		public __ptr__ getPtr() {
			return tab.plus(pos).getPtr();
		}

		@Override
		public __ptr__ asPtr() {
			if (pos == 0) {
				return tab;
			}
			return super.asPtr();
		}

	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("n")) {
			return n;
		}
		if (fieldName.equals("an")) {
			return an;
		}
		if (fieldName.equals("cache_nc")) {
			return cache_nc;
		}
		return super.getInt(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("v")) {
			return v;
		}
		if (fieldName.equals("av")) {
			return av;
		}
		if (fieldName.equals("flat")) {
			return flat;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public void setBoolean(String fieldName, boolean data) {
		if (fieldName.equals("candidate")) {
			this.candidate = data;
			return;
		}
		if (fieldName.equals("valid")) {
			this.valid = data ? 1 : 0;
			return;
		}
		super.setBoolean(fieldName, data);
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
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("valid")) {
			return valid != 0;
		}
		if (fieldName.equals("candidate")) {
			return candidate;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("pht1")) {
			return pht1;
		}
		if (fieldName.equals("pht2")) {
			return pht2;
		}
		if (fieldName.equals("ht1")) {
			return ht1;
		}
		if (fieldName.equals("ht2")) {
			return ht2;
		}
		return super.getDouble(fieldName);
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
			this.v = (STStarArrayOfPointer) newData;
			return v;
		}
		if (fieldName.equals("av")) {
			this.av = (STStarArrayOfPointer) newData;
			return av;
		}
		if (fieldName.equals("flat")) {
			this.flat = (ST_adjmatrix_t) newData;
			return flat;
		}
		return super.setPtr(fieldName, newData);
	}

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_rank_t>(nb, 0, ST_rank_t.class));
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