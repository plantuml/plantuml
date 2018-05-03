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
import smetana.core.OFFSET;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_refstr_t extends UnsupportedStructAndPtr /* implements WithParent */{

	public final ST_dtlink_s link = new ST_dtlink_s(this);
	public int refcnt;
	public CString s;

	private final StarStruct parent;

	public ST_refstr_t() {
		this(null);
	}

	public ST_refstr_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("refcnt")) {
			this.refcnt = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("refcnt")) {
			return refcnt;
		}
		return super.getInt(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("s")) {
			return s;
		}
		return super.getPtr(fieldName);
	}

	public class Amp extends UnsupportedStarStruct {

		@Override
		public Object addVirtualBytes(int virtualBytes) {
			if (virtualBytes == 0) {
				return this;
			}
			final OFFSET offset = OFFSET.fromInt(virtualBytes);
			if (offset.toString().equals("h.refstr_t::s")) {
				return s;
			}
			System.err.println("virtualBytes=" + virtualBytes);
			System.err.println("offset=" + offset);
			return super.addVirtualBytes(virtualBytes);
		}

	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		final OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.refstr_t::s")) {
			return s;
		}
		System.err.println("virtualBytes=" + virtualBytes);
		System.err.println("offset=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		// if (dest == refstr_t.class) {
		// return this;
		// }
		if (dest == _dtlink_s.class) {
			return link;
		}
		return super.castTo(dest);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("s")) {
			this.s = (CString) newData;
			this.s.setMyFather(this);
			return s;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public CString getCString(String fieldName) {
		if (fieldName.equals("s")) {
			return s;
		}
		return super.getCString(fieldName);
	}

	public CString to_s(ST_dtlink_s from) {
		if (from == link) {
			return s;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Class getRealClass() {
		return refstr_t.class;
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct refstr_t",
	// "{",
	// "Dtlink_t link",
	// "unsigned long refcnt",
	// "char *s",
	// "char store[1]",
	// "}",
	// "refstr_t");
}

// typedef struct refstr_t {
// Dtlink_t link;
// unsigned long refcnt;
// char *s;
// char store[1]; /* this is actually a dynamic array */
// } refstr_t;