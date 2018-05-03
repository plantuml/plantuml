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
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_nodequeue extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_nodequeue() {
		this(null);
	}

	public ST_nodequeue(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	// "typedef struct nodequeue",
	// "{",
	// "node_t **store, **limit, **head, **tail",
	private StarArrayOfPtr store;
	private StarArrayOfPtr tail;
	private StarArrayOfPtr head;
	private StarArrayOfPtr limit;

	// "}",
	// "nodequeue");

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("store")) {
			this.store = (StarArrayOfPtr) newData;
			return this.store;
		}
		if (fieldName.equals("tail")) {
			this.tail = (StarArrayOfPtr) newData;
			return this.tail;
		}
		if (fieldName.equals("head")) {
			this.head = (StarArrayOfPtr) newData;
			return this.head;
		}
		if (fieldName.equals("limit")) {
			this.limit = (StarArrayOfPtr) newData;
			return this.limit;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		if (fieldName.equals("store")) {
			return this.store.getInternalArray();
		}
		if (fieldName.equals("tail")) {
			return this.tail.getInternalArray();
		}
		if (fieldName.equals("head")) {
			return this.head.getInternalArray();
		}
		if (fieldName.equals("limit")) {
			return this.limit.getInternalArray();
		}
		return super.getArrayOfPtr(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("store")) {
			return this.store;
		}
		if (fieldName.equals("tail")) {
			return this.tail;
		}
		if (fieldName.equals("head")) {
			return this.head;
		}
		if (fieldName.equals("limit")) {
			return this.limit;
		}
		return super.getPtr(fieldName);
	}
}

// typedef struct nodequeue {
// node_t **store, **limit, **head, **tail;
// } nodequeue;