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

import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_integer__;
import smetana.core.__ptr__;
import smetana.core.amiga.StarArrayOfInteger;
import smetana.core.amiga.StarStruct;

public class ST_adjmatrix_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_adjmatrix_t() {
		this(null);
	}

	public ST_adjmatrix_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	// "typedef struct adjmatrix_t",
	// "{",
	private int nrows, ncols;

	// "char *data",
	private StarArrayOfInteger data;

	// "}",
	// "adjmatrix_t");

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("ncols")) {
			this.ncols = data;
			return;
		}
		if (fieldName.equals("nrows")) {
			this.nrows = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("nrows")) {
			return this.nrows;
		}
		if (fieldName.equals("ncols")) {
			return this.ncols;
		}
		return super.getInt(fieldName);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("data")) {
			this.data = (StarArrayOfInteger) newData;
			return data;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __array_of_integer__ getArrayOfInteger(String fieldName) {
		if (fieldName.equals("data")) {
			return data.getInternalArray();
		}
		return super.getArrayOfInteger(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("data")) {
			return data;
		}
		return super.getPtr(fieldName);
	}
}

// typedef struct adjmatrix_t {
// int nrows, ncols;
// char *data;
// } adjmatrix_t;