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

import h.ST_pathend_t.ArrayOfTwenty;
import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_struct__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_triangle_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_triangle_t() {
		this(null);
	}

	public ST_triangle_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	// "typedef struct triangle_t",
	// "{",
	private int mark;

	private final ST_tedge_t e[] = new ST_tedge_t[] { new ST_tedge_t(), new ST_tedge_t(), new ST_tedge_t() };

	class ArrayOfThree extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfThree(int pos) {
			this.pos = pos;
		}

		@Override
		public __array_of_struct__ plus(int delta) {
			return new ArrayOfThree(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return e[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			e[pos].copyDataFrom(value);
		}

		@Override
		public double getDouble(String fieldName) {
			return getStruct().getDouble(fieldName);
		}

	}

	// "struct tedge_t e[3]",
	// "}",
	// "triangle_t");

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("mark")) {
			return this.mark;
		}
		return super.getInt(fieldName);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("mark")) {
			this.mark = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("mark")) {
			return this.mark != 0;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public __array_of_struct__ getArrayOfStruct(String fieldName) {
		if (fieldName.equals("e")) {
			return new ArrayOfThree(0);
		}
		return super.getArrayOfStruct(fieldName);
	}

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_triangle_t>(nb, 0, ST_triangle_t.class));
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

// typedef struct triangle_t {
// int mark;
// struct tedge_t e[3];
// } triangle_t;