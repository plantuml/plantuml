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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.amiga.StarStruct;

public class ST_triangle_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_triangle_t() {
		this(null);
	}

	public ST_triangle_t(StarStruct parent) {
		this.parent = parent;
	}

	public static class Array {

		private final List<ST_triangle_t> data;
		private final int pos;

		public Array(int size) {
			this.data = new ArrayList<ST_triangle_t>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_triangle_t());
			}
		}

		public Array reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_triangle_t());
			}
			return this;
		}

		private Array(List<ST_triangle_t> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public ST_triangle_t get(int i) {
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

	}

	// "typedef struct triangle_t",
	// "{",
	public int mark;

	public final ST_tedge_t e[] = new ST_tedge_t[] { new ST_tedge_t(), new ST_tedge_t(), new ST_tedge_t() };

	// "struct tedge_t e[3]",
	// "}",
	// "triangle_t");

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("mark")) {
			this.mark = data;
			return;
		}
		super.setInt(fieldName, data);
	}

}

// typedef struct triangle_t {
// int mark;
// struct tedge_t e[3];
// } triangle_t;