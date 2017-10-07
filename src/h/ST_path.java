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
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_path extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_path() {
		this(null);
	}

	public ST_path(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	// "typedef struct path",
	// "{",
	final ST_port start = new ST_port(this), end = new ST_port(this);
	private int nbox;
	// "boxf *boxes",
	private StarArrayOfPtr boxes;

	private ST_Agedge_s.Amp data;

	// "void *data",
	// "}",
	// "path");

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("boxes")) {
			this.boxes = (StarArrayOfPtr) newData;
			return this.boxes;
		}
		if (fieldName.equals("data")) {
			if (newData instanceof ST_Agedge_s) {
				newData = ((ST_Agedge_s) newData).amp();
			}
			this.data = (ST_Agedge_s.Amp) newData;
			return this.data;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("data")) {
			return this.data;
		}
		if (fieldName.equals("boxes")) {
			return this.boxes;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("start")) {
			return start;
		}
		if (fieldName.equals("end")) {
			return end;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		if (fieldName.equals("boxes")) {
			return boxes.getInternalArray();
		}
		return super.getArrayOfPtr(fieldName);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("nbox")) {
			return this.nbox;
		}
		return super.getInt(fieldName);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("nbox")) {
			this.nbox = data;
			return;
		}
		super.setInt(fieldName, data);
	}

}

// typedef struct path { /* internal specification for an edge spline */
// port start, end;
// int nbox; /* number of subdivisions */
// boxf *boxes; /* rectangular regions of subdivision */
// void *data;
// } path;