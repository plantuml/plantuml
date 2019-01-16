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

import java.util.List;

import smetana.core.CString;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_Agattr_s extends UnsupportedStructAndPtr {

	private final ST_Agrec_s h = new ST_Agrec_s(this); /* common data header */
	public ST_dt_s dict; /* shared dict to interpret attr field */
	public List<CString> str; /* the attribute string values */

	private final StarStruct parent;

	public ST_Agattr_s() {
		this(null);
	}

	public ST_Agattr_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agrec_s.class) {
			return h;
			// return h.amp();
		}
		return super.castTo(dest);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agattr_s",
	// "{",
	// "Agrec_t h",
	// "Dict_t *dict",
	// "char **str",
	// "}");
}

// struct Agattr_s { /* dynamic string attributes */
// Agrec_t h; /* common data header */
// Dict_t *dict; /* shared dict to interpret attr field */
// char **str; /* the attribute string values */
// };