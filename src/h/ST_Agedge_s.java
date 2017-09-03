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

import h.ST_Agrec_s.Amp;
import smetana.core.HardcodedStruct;
import smetana.core.OFFSET;
import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_Agedge_s extends UnsupportedStructAndPtr implements HardcodedStruct {

	private final ST_Agobj_s base = new ST_Agobj_s(this);
	private final ST_dtlink_s id_link = new ST_dtlink_s(this);
	private final ST_dtlink_s seq_link = new ST_dtlink_s(this);
	private ST_Agnode_s node;

	@Override
	public void copyDataFrom(__ptr__ arg) {
		if (arg instanceof Amp) {
			arg = ((Amp) arg).getObject();
		}
		ST_Agedge_s this2 = (ST_Agedge_s) arg;
		this.base.copyDataFrom((__struct__) this2.base);
		this.id_link.copyDataFrom((__struct__) this2.id_link);
		this.seq_link.copyDataFrom((__struct__) this2.seq_link);
		this.node = this2.node;
	}

	private final StarStruct parent;

	public ST_Agedge_s() {
		this(null);
	}

	public ST_Agedge_s(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		if (other instanceof Amp) {
			Amp other2 = (Amp) other;
			return this == other2.me;
		}
		ST_Agedge_s other2 = (ST_Agedge_s) other;
		return this == other2;
	}

	@Override
	public StarStruct amp() {
		return new Amp(this);
	}

	public class Amp extends UnsupportedStarStruct {

		private final ST_Agedge_s me;

		public Amp(ST_Agedge_s me) {
			this.me = me;
		}

		public ST_Agedge_s getObject() {
			return me;
		}

		@Override
		public boolean isSameThan(StarStruct other) {
			if (other instanceof ST_Agedge_s) {
				ST_Agedge_s other2 = (ST_Agedge_s) other;
				return this.me == other2;
			}
			Amp other2 = (Amp) other;
			return this.me == other2.me;
		}

		@Override
		public __ptr__ castTo(Class dest) {
			return ST_Agedge_s.this.castTo(dest);
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return ST_Agedge_s.this.setPtr(fieldName, newData);
		}

		@Override
		public void copyDataFrom(__struct__ other) {
			ST_Agedge_s.this.copyDataFrom(other);
		}

		@Override
		public void copyDataFrom(__ptr__ other) {
			ST_Agedge_s.this.copyDataFrom(other);
		}

		@Override
		public __ptr__ getPtr(String fieldName) {
			return ST_Agedge_s.this.getPtr(fieldName);
		}

		@Override
		public __struct__ getStruct(String fieldName) {
			return ST_Agedge_s.this.getStruct(fieldName);
		}

		@Override
		public __ptr__ plus(int pointerMove) {
			ST_Agedgepair_s pair = (ST_Agedgepair_s) me.parent;
			// Order=out, in
			if (pair.out == me && pointerMove == 1) {
				return pair.in;
			}
			if (pair.in == me && pointerMove == -1) {
				return pair.out;
			}
			return super.plus(pointerMove);
		}

		@Override
		public Object addVirtualBytes(int virtualBytes) {
			if (virtualBytes == 0) {
				return this;
			}
			OFFSET offset = OFFSET.fromInt(virtualBytes);
			if (offset.toString().equals("h.Agedge_s::seq_link")) {
				return seq_link;
			}
			if (offset.toString().equals("h.Agedge_s::id_link")) {
				return id_link;
			}
			System.err.println("offset1=" + offset);
			return super.addVirtualBytes(virtualBytes);
		}

	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.Agedge_s::seq_link")) {
			return seq_link;
		}
		if (offset.toString().equals("h.Agedge_s::id_link")) {
			return id_link;
		}
		System.err.println("offset2=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	@Override
	public __ptr__ plus(int pointerMove) {
		ST_Agedgepair_s pair = (ST_Agedgepair_s) parent;
		// Order=out, in
		if (pair.out == this && pointerMove == 1) {
			return pair.in;
		}
		if (pair.in == this && pointerMove == -1) {
			return pair.out;
		}
		return super.plus(pointerMove);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Agobj_s.class) {
			return base;
		}
		if (dest == Agedge_s.class) {
			return this;
		}
		return super.castTo(dest);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("node")) {
			if (newData instanceof ST_Agnode_s.Amp) {
				this.node = ((ST_Agnode_s.Amp) newData).getObject();
			} else {
				this.node = (ST_Agnode_s) newData;
			}
			return node;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("node")) {
			return node;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("base")) {
			return base;
		}
		return super.getStruct(fieldName);
	}

	public StarStruct from_seq_link(ST_dtlink_s from) {
		if (from == seq_link) {
			return amp();
		}
		throw new IllegalArgumentException();
	}

	public StarStruct from_id_link(ST_dtlink_s from) {
		if (from == id_link) {
			return amp();
		}
		throw new IllegalArgumentException();
	}

	public static size_t sizeof_starstar_empty(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return STStarArrayOfPointer.malloc(nb);
			}

			@Override
			public Object realloc(Object old) {
				if (old instanceof STStarArrayOfPointer) {
					STStarArrayOfPointer old2 = (STStarArrayOfPointer) old;
					old2.realloc(nb);
					return old2;
				}
				// if (old instanceof StarArrayOfPtr) {
				// StarArrayOfPtr old2 = (StarArrayOfPtr) old;
				// old2.realloc(nb);
				// return old2;
				// }
				return super.realloc(old);
			}

			@Override
			public int getInternalNb() {
				return nb;
			}
		};
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