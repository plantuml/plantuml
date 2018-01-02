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

import h.ST_tna_t.Amp;
import h.ST_triangle_t.ArrayOfThree;

import java.util.Arrays;
import java.util.List;

import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_struct__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_tna_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_tna_t() {
		this(null);
	}

	public ST_tna_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	// "typedef struct tna_t",
	// "{",
	// "Ppoint_t a[2]",
	// "}",
	// "tna_t");
	private double t;
	private final ST_pointf a[] = new ST_pointf[] { new ST_pointf(), new ST_pointf() };

	class ArrayOfTwo extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfTwo(int pos) {
			this.pos = pos;
		}

		@Override
		public __array_of_struct__ plus(int delta) {
			return new ArrayOfTwo(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return a[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			a[pos].copyDataFrom(value);
		}

		@Override
		public double getDouble(String fieldName) {
			return getStruct().getDouble(fieldName);
		}

	}

	@Override
	public __array_of_struct__ getArrayOfStruct(String fieldName) {
		if (fieldName.equals("a")) {
			return new ArrayOfTwo(0);
		}
		return super.getArrayOfStruct(fieldName);
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("t")) {
			return this.t;
		}
		return super.getDouble(fieldName);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("t")) {
			this.t = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_tna_t>(nb, 0, ST_tna_t.class));
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

// typedef struct tna_t {
// double t;
// Ppoint_t a[2];
// } tna_t;