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
import smetana.core.amiga.StarStruct;

public class ST_Agdatadict_s extends UnsupportedStructAndPtr {

	private final ST_Agrec_s h = new ST_Agrec_s(this); /* installed in list of graph recs */
	public ST_dt_s n;
	public ST_dt_s e;
	public ST_dt_s g;

	private final StarStruct parent;

	public ST_Agdatadict_s() {
		this(null);
	}

	public ST_Agdatadict_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("dict.n")) {
			this.n = (ST_dt_s) newData;
			return n;
		}
		if (fieldName.equals("dict.e")) {
			this.e = (ST_dt_s) newData;
			return e;
		}
		if (fieldName.equals("dict.g")) {
			this.g = (ST_dt_s) newData;
			return g;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("dict.n")) {
			return n;
		}
		if (fieldName.equals("dict.e")) {
			return e;
		}
		if (fieldName.equals("dict.g")) {
			return g;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Agrec_s.class) {
			return h.amp();
		}
		return super.castTo(dest);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agdatadict_s",
	// "{",
	// "Agrec_t h",
	// "struct",
	// "{",
	// "Dict_t *n, *e, *g",
	// "}",
	// "dict",
	// "}");
}

// struct Agdatadict_s { /* set of dictionaries per graph */
// Agrec_t h; /* installed in list of graph recs */
// struct {
// Dict_t *n, *e, *g;
// } dict;
// };