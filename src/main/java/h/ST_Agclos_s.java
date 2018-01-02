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

import java.util.ArrayList;
import java.util.List;

import smetana.core.ArrayOfInteger;
import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_integer__;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Agclos_s extends UnsupportedStructAndPtr {
	public final ST_Agdisc_s disc = new ST_Agdisc_s(this); /* resource discipline functions */
	public final ST_Agdstate_s state = new ST_Agdstate_s(this); /* resource closures */
	public ST_dt_s strdict;
	private final int[] seq = new int[3];
	// "unsigned long seq[3]",
	public ST_Agcbstack_s cb;
	public boolean callbacks_enabled; /* issue user callbacks or hold them? */

	// "Dict_t *lookup_by_name[3]",
	// "Dict_t *lookup_by_id[3]",
	private final ArrayOfThreePtrDict_t lookup_by_id = new ArrayOfThreePtrDict_t();

	static class ArrayOfThreePtrDict_t extends UnsupportedArrayOfPtr implements __array_of_ptr__ {
		private final List<Dict_t> list;
		private final int pos;

		private ArrayOfThreePtrDict_t(List<Dict_t> list, int pos) {
			this.list = list;
			this.pos = pos;
		}

		ArrayOfThreePtrDict_t() {
			this(new ArrayList<Dict_t>(), 0);
			for (int i = 0; i < 3; i++) {
				list.add(null);
			}
		}

		@Override
		public __array_of_ptr__ plus(int delta) {
			return new ArrayOfThreePtrDict_t(list, pos + delta);
		}

		@Override
		public __ptr__ getPtr() {
			return list.get(0);
		}

	}

	private final StarStruct parent;

	public ST_Agclos_s() {
		this(null);
	}

	public ST_Agclos_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("callbacks_enabled")) {
			return callbacks_enabled;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("strdict")) {
			return strdict;
		}
		if (fieldName.equals("cb")) {
			return cb;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("strdict")) {
			this.strdict = (ST_dt_s) newData;
			return strdict;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("disc")) {
			return disc;
		}
		if (fieldName.equals("state")) {
			return state;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public void setBoolean(String fieldName, boolean data) {
		if (fieldName.equals("callbacks_enabled")) {
			this.callbacks_enabled = data;
			return;
		}
		super.setBoolean(fieldName, data);
	}

	@Override
	public __array_of_integer__ getArrayOfInteger(String fieldName) {
		if (fieldName.equals("seq")) {
			return new ArrayOfInteger(seq, 0);
		}
		return super.getArrayOfInteger(fieldName);
	}

	@Override
	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		if (fieldName.equals("lookup_by_id")) {
			return lookup_by_id;
		}
		return super.getArrayOfPtr(fieldName);
	}

	// public interface ST_Agclos_s extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agclos_s",
	// "{",
	// "Agdisc_t disc",
	// "Agdstate_t state",
	// "Dict_t *strdict",
	// "unsigned long seq[3]",
	// "Agcbstack_t *cb",
	// "unsigned char callbacks_enabled",
	// "Dict_t *lookup_by_name[3]",
	// "Dict_t *lookup_by_id[3]",
	// "}");
}

// struct Agclos_s {
// Agdisc_t disc; /* resource discipline functions */
// Agdstate_t state; /* resource closures */
// Dict_t *strdict; /* shared string dict */
// unsigned long seq[3]; /* local object sequence number counter */
// Agcbstack_t *cb; /* user and system callback function stacks */
// unsigned char callbacks_enabled; /* issue user callbacks or hold them? */
// Dict_t *lookup_by_name[3];
// Dict_t *lookup_by_id[3];
// };