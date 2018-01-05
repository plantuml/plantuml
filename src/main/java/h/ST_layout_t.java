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
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_layout_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_layout_t() {
		this(null);
	}

	public ST_layout_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {
	}

	private double quantum;
	private double scale;
	private double ratio;
	private double dpi;

	private ST_pointf margin = new ST_pointf(this);
	private ST_pointf page = new ST_pointf(this);
	private ST_pointf size = new ST_pointf(this);

	private boolean filled;
	private boolean landscape;
	private boolean centered;

	// "ratio_t ratio_kind",
	private int ratio_kind;
	// "void* xdots",
	// "char* id",

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("xdots") && newData == null) {
			return null;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("margin")) {
			return margin;
		}
		if (fieldName.equals("page")) {
			return page;
		}
		if (fieldName.equals("size")) {
			return size;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public void setBoolean(String fieldName, boolean data) {
		if (fieldName.equals("filled")) {
			this.filled = data;
			return;
		}
		if (fieldName.equals("landscape")) {
			this.landscape = data;
			return;
		}
		if (fieldName.equals("centered")) {
			this.centered = data;
			return;
		}
		super.setBoolean(fieldName, data);
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("quantum")) {
			return this.quantum;
		}
		if (fieldName.equals("scale")) {
			return this.scale;
		}
		if (fieldName.equals("ratio")) {
			return this.ratio;
		}
		if (fieldName.equals("dpi")) {
			return this.dpi;
		}
		return super.getDouble(fieldName);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("quantum")) {
			this.quantum = data;
			return;
		}
		if (fieldName.equals("scale")) {
			this.scale = data;
			return;
		}
		if (fieldName.equals("ratio")) {
			this.ratio = data;
			return;
		}
		if (fieldName.equals("dpi")) {
			this.dpi = data;
			return;
		}
		super.setDouble(fieldName, data);
	}
	
	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("ratio_kind")) {
			return this.ratio_kind;
		}
		return super.getInt(fieldName);
	}
}

// typedef struct layout_t {
// double quantum;
// double scale;
// double ratio; /* set only if ratio_kind == R_VALUE */
// double dpi;
// pointf margin;
// pointf page;
// pointf size;
// boolean filled;
// boolean landscape;
// boolean centered;
// ratio_t ratio_kind;
// void* xdots;
// char* id;
// } layout_t;