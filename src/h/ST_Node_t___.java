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

import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Node_t___ extends UnsupportedStructAndPtr implements ST_Node_t___or_object_t {

	private final StarStruct parent;
	public int count;
	public int level;
	// Sorry guys :-)
	public final ST_Branch_t branch[] = new ST_Branch_t[] { new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t() };

	public ST_Node_t___() {
		this(null);
	}

	public ST_Node_t___(StarStruct parent) {
		this.parent = parent;
	}

	class ArrayOfSixtyFor extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfSixtyFor(int pos) {
			this.pos = pos;
		}

		public ArrayOfSixtyFor plus(int delta) {
			return new ArrayOfSixtyFor(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return branch[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			branch[pos].copyDataFrom(value);
		}

	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("count")) {
			this.count = data;
			return;
		}
		if (fieldName.equals("level")) {
			this.level = data;
			return;
		}
		super.setInt(fieldName, data);
	}


	// "typedef struct Node",
	// "{",
	// "int count",
	// "int level",
	// "struct Branch branch[64]",
	// "}",
	// "Node_t");
}

// typedef struct Node {
// int count;
// int level; /* 0 is leaf, others positive */
// struct Branch branch[64];
// } Node_t;