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
 * (C) Copyright 2009-2022, Arnaud Roques
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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_xlabel_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_xlabel_t() {
		this(null);
	}

	public ST_xlabel_t(StarStruct parent) {
		this.parent = parent;
	}

	final public ST_pointf sz = new ST_pointf(this);
	final public ST_pointf pos = new ST_pointf(this);

	public ST_textlabel_t lbl;
	public int set;

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("set")) {
			this.set = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("lbl")) {
			this.lbl = (ST_textlabel_t) newData;
			return this.lbl;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public ST_xlabel_t getStruct() {
		return this;
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("sz")) {
			this.sz.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("pos")) {
			this.pos.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

}

// typedef struct {
// pointf sz; /* Size of label (input) */
// pointf pos; /* Position of lower-left corner of label (output) */
// void *lbl; /* Pointer to label in the graph */
// unsigned char set; /* True if the position has been set (input/output) */
// } xlabel_t;