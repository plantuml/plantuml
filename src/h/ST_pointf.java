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

import smetana.core.CString;
import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedArrayOfStruct2;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.Area;
import smetana.core.amiga.StarStruct;

public class ST_pointf extends UnsupportedStructAndPtr implements HardcodedStruct {

	public double x;
	public double y;
	
	@Override
	public boolean isSameThan(StarStruct other) {
		return this==(ST_pointf)other;
	}

	public static class Array extends UnsupportedArrayOfStruct2 implements __ptr__,
			__array_of_ptr__ {

		private final List<ST_pointf> data;
		private final int pos;

		@Override
		public void setStruct(__struct__ value) {
			getStruct().___(value);
		}

		@Override
		public Array asPtr() {
			return this;
		}

		@Override
		public void setDouble(String fieldName, double value) {
			getStruct().setDouble(fieldName, value);
		}

		@Override
		public ST_pointf getStruct() {
			return get(0).getStruct();
		}

		@Override
		public ST_pointf getPtr() {
			return get(0);
		}

		public Array(int size) {
			this.data = new ArrayList<ST_pointf>();
			this.pos = 0;
			for (int i = 0; i < size; i++) {
				data.add(new ST_pointf());
			}
		}

		public Array reallocJ(int newsize) {
			while (data.size() < newsize) {
				data.add(new ST_pointf());
			}
			return this;
		}

		public Array plus(int delta) {
			return plusJ(delta);
		}

		private Array(List<ST_pointf> data, int pos) {
			this.data = data;
			this.pos = pos;
		}

		public ST_pointf get(int i) {
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

		public void setCString(CString value) {
			throw new UnsupportedOperationException(getClass().toString());
		}

	}

	public static ST_pointf[] malloc(int nb) {
		final ST_pointf result[] = new ST_pointf[nb];
		for (int i = 0; i < nb; i++) {
			result[i] = new ST_pointf();
		}
		return result;
	}

	public static ST_pointf[] realloc(ST_pointf[] old, int nb) {
		if (nb <= old.length) {
			return old;
		}
		final ST_pointf result[] = new ST_pointf[nb];
		for (int i = 0; i < nb; i++) {
			result[i] = i < old.length ? old[i] : new ST_pointf();
		}
		return result;
	}

	public ST_pointf() {
		this(null);
	}


	@Override
	public ST_pointf copy() {
		final ST_pointf result = new ST_pointf();
		result.x = this.x;
		result.y = this.y;
		return result;
	}

	@Override
	public void setStruct(__struct__ value) {
		final ST_pointf other2 = (ST_pointf) value;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void copyDataFrom(__ptr__ value) {
		final ST_pointf other2 = (ST_pointf) value;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public ST_pointf getStruct() {
		return this;
	}

	@Override
	public void memcopyFrom(Area source) {
//		if (source instanceof StarArrayOfPtr) {
//			final Amp other2 = (Amp) ((StarArrayOfPtr) source).getPtr();
//			this.x = other2.getX();
//			this.y = other2.getY();
//			return;
//
//		}
		final ST_pointf other2 = (ST_pointf) source;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void ___(__struct__ other) {
		final ST_pointf other2 = (ST_pointf) other;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void ____(__ptr__ other) {
		if (other instanceof ST_pointf.Array) {
			ST_pointf.Array other2 = (ST_pointf.Array) other;
			this.x = other2.get(0).x;
			this.y = other2.get(0).y;
			return;
		}
//		if (other instanceof __array_of_ptr__) {
//			Amp other2 = (Amp) other.getPtr();
//			this.x = other2.getX();
//			this.y = other2.getY();
//			return;
//		}
//		if (other instanceof StarArrayOfPtr) {
//			Amp other2 = (Amp) other.getPtr();
//			this.x = other2.getX();
//			this.y = other2.getY();
//			return;
//		}
//		if (other instanceof Amp) {
//			Amp other2 = (Amp) other;
//			this.x = other2.getX();
//			this.y = other2.getY();
//			return;
//		}
		if (other instanceof ST_pointf) {
			ST_pointf other2 = (ST_pointf) other;
			this.x = other2.x;
			this.y = other2.y;
			return;
		}
		System.err.println("other=" + other.getClass());
		System.err.println("other=" + other.getPtr().getClass());
		super.____(other);
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		final ST_pointf other2 = (ST_pointf) other;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("x")) {
			this.x = data;
			return;
		}
		if (fieldName.equals("y")) {
			this.y = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	public ST_pointf(StarStruct parent) {
	}

	// public interface ST_pointf extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct pointf_s",
	// "{",
	// "double x, y",
	// "}",
	// "pointf");
}

// typedef struct pointf_s { double x, y; } pointf;