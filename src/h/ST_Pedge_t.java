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

public class ST_Pedge_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_Pedge_t() {
		this(null);
	}

	public ST_Pedge_t(StarStruct parent) {
		this.parent = parent;
	}

	// "typedef struct Pedge_t",
	// "{",
	// "Ppoint_t a, b",
	public final ST_pointf a = new ST_pointf(this);
	public final ST_pointf b = new ST_pointf(this);

	// "}",
	// "Pedge_t");

	
	public static class Array extends UnsupportedArrayOfStruct2 implements __ptr__, __array_of_ptr__{

		private final List<ST_Pedge_t> data;
		private final int pos;
		
		@Override
		public void setStruct(String fieldName, __struct__ data) {
			getStruct().setStruct(fieldName, data);
		}
		
		@Override
		public Array asPtr() {
			return this;
		}
		
		@Override
		public ST_Pedge_t getStruct() {
			return data.get(pos);
		}
		
		public Array(int size) {
			this.data = new ArrayList<ST_Pedge_t>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_Pedge_t());
			}
		}

		public Array reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_Pedge_t());
			}
			return this;
		}
		
		public Array plus(int delta) {
			return plusJ(delta);
		}

		private Array(List<ST_Pedge_t> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public ST_Pedge_t get(int i) {
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
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("a")) {
			this.a.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("b")) {
			this.b.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}


}

// typedef struct Pedge_t {
// Ppoint_t a, b;
// } Pedge_t;