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

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public class ST_Branch_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	// Warning : could be a "ST_Leaf_t" from C Version
	public final ST_Rect_t rect = new ST_Rect_t(this);
	public ST_Node_t___or_object_t child; // "data" : ST_object_t

	public ST_Branch_t() {
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		ST_Branch_t this2 = (ST_Branch_t) other;
		this.rect.copyDataFrom((__struct__) this2.rect);
		this.child = this2.child;
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Branch_t.class) {
			return ST_Branch_t.this;
		}
		if (dest == ST_Rect_t.class) {
			return rect;
		}
		return super.castTo(dest);
	}

	public __struct__ getStruct() {
		return this;
	}

	@Override
	public void ___(__struct__ other) {
		this.copyDataFrom(other);
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("rect")) {
			this.rect.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public ST_Node_t___or_object_t setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("child")) {
			this.child = (ST_Node_t___or_object_t) newData;
			return this.child;
		}
		throw new UnsupportedOperationException();
	}

	// typedef struct Branch {
	// Rect_t rect;
	// struct Node *child;
	// } Branch_t;
}
