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

import smetana.core.CString;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Agrec_s extends UnsupportedStructAndPtr implements WithParent {

	public CString name;
	public ST_Agrec_s.Amp next;

	private final StarStruct parent;

	@Override
	public void copyDataFrom(__struct__ other) {
		if (other instanceof ST_Agedgeinfo_t && parent instanceof ST_Agedgeinfo_t) {
			parent.copyDataFrom(other);
			return;
		}
		ST_Agrec_s this2 = (ST_Agrec_s) other;
		this.name = this2.name;
		this.next = this2.next;
	}

	public ST_Agrec_s() {
		this(null);
	}

	public ST_Agrec_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp(this);
	}

	public class Amp extends UnsupportedStarStruct {

		private final ST_Agrec_s me;

		public Amp(ST_Agrec_s me) {
			this.me = me;
		}

		@Override
		public boolean isSameThan(StarStruct other) {
			Amp other2 = (Amp) other;
			return this.me == other2.me;
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return ST_Agrec_s.this.setPtr(fieldName, newData);
		}

		@Override
		public __ptr__ getPtr(String fieldName) {
			return ST_Agrec_s.this.getPtr(fieldName);
		}

		@Override
		public __ptr__ castTo(Class dest) {
			return ST_Agrec_s.this.castTo(dest);
		}

		@Override
		public CString getCString(String fieldName) {
			return ST_Agrec_s.this.getCString(fieldName);
		}

		@Override
		public void copyDataFrom(__struct__ other) {
			ST_Agrec_s.this.copyDataFrom(other);
		}
	}

	@Override
	public CString getCString(String fieldName) {
		if (fieldName.equals("name")) {
			return name;
		}
		return super.getCString(fieldName);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Agdatadict_s.class && getParent() instanceof ST_Agdatadict_s) {
			return (ST_Agdatadict_s) getParent();
		}
		if (dest == Agattr_s.class && getParent() instanceof ST_Agattr_s) {
			return (ST_Agattr_s) getParent();
		}
		if (dest == Agraphinfo_t.class && getParent() instanceof ST_Agraphinfo_t) {
			return (ST_Agraphinfo_t) getParent();
		}
		if (dest == Agnodeinfo_t.class && getParent() instanceof ST_Agnodeinfo_t) {
			return (ST_Agnodeinfo_t) getParent();
		}
		if (dest == Agedgeinfo_t.class && getParent() instanceof ST_Agedgeinfo_t) {
			return (ST_Agedgeinfo_t) getParent();
		}
		System.err.println("dest=" + dest);
		System.err.println("getParent=" + getParent().getClass());
		return super.castTo(dest);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("name")) {
			this.name = (CString) newData;
			return name;
		}
		if (fieldName.equals("next")) {
			if (newData instanceof ST_Agrec_s) {
				this.next = (ST_Agrec_s.Amp) ((ST_Agrec_s) newData).amp();
			} else {
				this.next = (ST_Agrec_s.Amp) newData;
			}
			return next;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("next")) {
			return next;
		}
		return super.getPtr(fieldName);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agrec_s",
	// "{",
	// "char *name",
	// "Agrec_t *next",
	// "}");
}

// struct Agrec_s {
// char *name;
// Agrec_t *next;
// /* following this would be any programmer-defined data */
// };