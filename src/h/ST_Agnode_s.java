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

import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_Agnode_s extends UnsupportedStructAndPtr {

	private final ST_Agobj_s base = new ST_Agobj_s(this);
	private ST_Agraph_s root;
	private ST_Agsubnode_s mainsub = new ST_Agsubnode_s(this);

	private final StarStruct parent;

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
	public StarStruct amp() {
		return new Amp(this);
	}

	public class Amp extends UnsupportedStarStruct {

		private final ST_Agnode_s me;

		public Amp(ST_Agnode_s me) {
			this.me = me;
		}

		@Override
		public __ptr__ castTo(Class dest) {
			if (dest == Agobj_s.class) {
				return base.amp();
			}
			if (dest == Agnode_s.class) {
				return me;
			}
			return super.castTo(dest);
		}

		@Override
		public __struct__ getStruct() {
			return ST_Agnode_s.this.getStruct();
		}

		@Override
		public __ptr__ getPtr(String fieldName) {
			return ST_Agnode_s.this.getPtr(fieldName);
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return ST_Agnode_s.this.setPtr(fieldName, newData);
		}

		public ST_Agnode_s getObject() {
			return me;
		}

	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Agnode_s.class) {
			return this;
		}
		if (dest == Agobj_s.class) {
			return base;
		}
		return super.castTo(dest);
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		if (other instanceof Amp) {
			Amp other2 = (Amp) other;
			return this == other2.me;
		}
		ST_Agnode_s other2 = (ST_Agnode_s) other;
		return this == other2;
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("base")) {
			return base;
		}
		if (fieldName.equals("mainsub")) {
			return mainsub;
		}
		return super.getStruct(fieldName);
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
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("root")) {
			return root;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

	public static size_t sizeof_starstar_empty(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return STStarArrayOfPointer.malloc(nb);
			}

			@Override
			public int getInternalNb() {
				return nb;
			}
		};
	}

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_Agnode_s>(nb, 0, ST_Agnode_s.class));
			}

			@Override
			public int getInternalNb() {
				return nb;
			}

			@Override
			public Object realloc(Object old) {
				if (old instanceof STStarArrayOfPointer) {
					STStarArrayOfPointer old2 = (STStarArrayOfPointer) old;
					old2.realloc(nb);
					return old2;
				}
				if (old instanceof StarArrayOfPtr) {
					StarArrayOfPtr old2 = (StarArrayOfPtr) old;
					old2.realloc(nb);
					return old2;
				}
				return super.realloc(old);
			}
		};
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