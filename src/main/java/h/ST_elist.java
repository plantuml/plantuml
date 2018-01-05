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

import java.util.ArrayList;
import java.util.List;

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_elist extends UnsupportedStructAndPtr implements HardcodedStruct {

	public int size;
	private List list;

	public ST_elist() {
		this(null);
	}

	public ST_elist(StarStruct parent) {
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		ST_elist other2 = (ST_elist) other;
		this.size = other2.size;
		this.list = other2.list;
	}

	@Override
	public void ___(__struct__ other) {
		ST_elist other2 = (ST_elist) other;
		this.size = other2.size;
		this.list = other2.list;
	}

	@Override
	public ST_elist copy() {
		final ST_elist result = new ST_elist();
		result.size = this.size;
		result.list = this.list;
		return result;
	}

	public boolean listNotNull() {
		return list != null;
	}

	public void mallocEmpty(Class cl, int nb) {
		list = new ArrayList();
		while (list.size() < nb) {
			list.add(null);
		}
	}

	class ArrayOfPtr extends UnsupportedArrayOfPtr {

		private int pos;

		private ArrayOfPtr(int pos) {
			this.pos = pos;
		}

		@Override
		public __array_of_ptr__ plus(int delta) {
			return new ArrayOfPtr(pos + delta);
		}

		@Override
		public void setPtr(__ptr__ value) {
			list.set(pos, value);
		}

		@Override
		public __ptr__ getPtr() {
			return (__ptr__) list.get(pos);
		}

	}

	class Amp extends UnsupportedStarStruct {

		@Override
		public int getInt(String fieldName) {
			if (fieldName.equals("size")) {
				return size;
			}
			return ST_elist.this.getInt(fieldName);
		}

		@Override
		public void setInt(String fieldName, int data) {
			if (fieldName.equals("size")) {
				size = data;
				return;
			}
			ST_elist.this.setInt(fieldName, data);
		}

		@Override
		public __array_of_ptr__ getArrayOfPtr(String fieldName) {
			if (fieldName.equals("list")) {
				if (list == null) {
					return null;
				}
				return new ArrayOfPtr(0);
			}
			return ST_elist.this.getArrayOfPtr(fieldName);
		}

	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public void realloc(int nb) {
		if (list == null) {
			list = new ArrayList();
		}
		while (list.size() < nb) {
			list.add(null);
		}
	}

	class Ptr extends UnsupportedStructAndPtr {

		private final int pos;

		public Ptr(int pos) {
			this.pos = pos;
		}

		@Override
		public __ptr__ plus(int pointerMove) {
			return new Ptr(pos + pointerMove);
		}

		@Override
		public __ptr__ getPtr() {
			return (__ptr__) list.get(pos);
		}

	}

	public void free() {
		list = null;
	}

	public void setInList(int idx, Object value) {
		list.set(idx, value);
	}

	public __ptr__ getFromList(int i) {
		return (__ptr__) list.get(i);
	}

	public __ptr__ getTheList() {
		if (list == null) {
			throw new IllegalStateException();
		}
		return new Ptr(0);
	}

	public __array_of_ptr__ getTheArray() {
		if (list == null) {
			throw new IllegalStateException();
		}
		return new ArrayOfPtr(0);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct elist",
	// "{",
	// "edge_t **list",
	// "int size",
	// "}",
	// "elist");
}

// typedef struct elist {
// edge_t **list;
// int size;
// } elist;