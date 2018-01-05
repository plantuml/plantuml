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

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.Area;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarArrayOfStruct;
import smetana.core.amiga.StarStruct;

public class ST_pointf extends UnsupportedStructAndPtr implements HardcodedStruct {

	public double x;
	public double y;

	public ST_pointf() {
		this(null);
	}

	public class Amp extends UnsupportedStarStruct {

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public void setX(double value) {
			x = value;
		}

		public void setY(double value) {
			y = value;
		}

		@Override
		public __struct__ getStruct() {
			return ST_pointf.this;
		}

		@Override
		public double getDouble(String fieldName) {
			return ST_pointf.this.getDouble(fieldName);
		}

		@Override
		public void setStruct(__struct__ value) {
			ST_pointf.this.setStruct(value);
		}

		@Override
		public boolean isSameThan(StarStruct other) {
			ST_pointf.Amp other2 = (Amp) other;
			return this.getStruct() == other2.getStruct();
		}
	}

	@Override
	public __struct__ copy() {
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
	public __struct__ getStruct() {
		return this;
	}

	@Override
	public void memcopyFrom(Area source) {
		if (source instanceof StarArrayOfPtr) {
			final Amp other2 = (Amp) ((StarArrayOfPtr) source).getPtr();
			this.x = other2.getX();
			this.y = other2.getY();
			return;

		}
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
		if (other instanceof __array_of_ptr__) {
			Amp other2 = (Amp) other.getPtr();
			this.x = other2.getX();
			this.y = other2.getY();
			return;
		}
		if (other instanceof StarArrayOfPtr) {
			Amp other2 = (Amp) other.getPtr();
			this.x = other2.getX();
			this.y = other2.getY();
			return;
		}
		if (other instanceof StarArrayOfStruct) {
			Amp other2 = (Amp) other.getPtr();
			this.x = other2.getX();
			this.y = other2.getY();
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
	public StarStruct amp() {
		return new Amp();
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("x")) {
			return x;
		}
		if (fieldName.equals("y")) {
			return y;
		}
		return super.getDouble(fieldName);
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

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_pointf>(nb, 0, ST_pointf.class));
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

	// public interface ST_pointf extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct pointf_s",
	// "{",
	// "double x, y",
	// "}",
	// "pointf");
}

// typedef struct pointf_s { double x, y; } pointf;