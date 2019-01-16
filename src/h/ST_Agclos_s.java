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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_Agclos_s extends UnsupportedStructAndPtr {
	private final StarStruct parent;

	public final ST_Agdisc_s disc = new ST_Agdisc_s(this); /* resource discipline functions */
	public final ST_Agdstate_s state = new ST_Agdstate_s(this); /* resource closures */
	public ST_dt_s strdict;
	public final int[] seq = new int[3];
	// "unsigned long seq[3]",
	public ST_Agcbstack_s cb;
	public boolean callbacks_enabled; /* issue user callbacks or hold them? */

	// "Dict_t *lookup_by_name[3]",
	// "Dict_t *lookup_by_id[3]",
	public final ST_dt_s[] lookup_by_id = new ST_dt_s[3];

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
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("strdict")) {
			this.strdict = (ST_dt_s) newData;
			return strdict;
		}
		return super.setPtr(fieldName, newData);
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