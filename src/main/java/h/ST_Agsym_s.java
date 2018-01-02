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
import smetana.core.HardcodedStruct;
import smetana.core.OFFSET;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

//2 38c2s12koxcpi2c7vwl72qrsp

public class ST_Agsym_s extends UnsupportedStructAndPtr implements HardcodedStruct {
	public final ST_dtlink_s link = new ST_dtlink_s(this);

	public CString name; /* attribute's name */
	public CString defval; /* its default value for initialization */

	public int id; /* its index in attr[] */
	public int kind; /* referent object type */
	public int fixed; /* immutable value */
	public int print; /* always print */

	public ST_Agsym_s(StarStruct parent) {
	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		final OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.Agsym_s::name")) {
			return name;
		}
		if (offset.toString().equals("h.Agsym_s::link")) {
			return link;
		}
		System.err.println("virtualBytes=" + virtualBytes);
		System.err.println("offset=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	public ST_Agsym_s() {
		this(null);
	}

	public class Amp extends UnsupportedStarStruct {

		private final ST_Agsym_s me;

		public Amp(ST_Agsym_s me) {
			this.me = me;
		}

		@Override
		public boolean isSameThan(StarStruct other) {
			Amp other2 = (Amp) other;
			return this.me == other2.me;
		}

		@Override
		public Object addVirtualBytes(int virtualBytes) {
			if (virtualBytes == 0) {
				return this;
			}
			final OFFSET offset = OFFSET.fromInt(virtualBytes);
			if (offset.toString().equals("h.Agsym_s::name")) {
				return name;
			}
			System.err.println("virtualBytes=" + virtualBytes);
			System.err.println("offset=" + offset);
			return super.addVirtualBytes(virtualBytes);
		}

		@Override
		public int getInt(String fieldName) {
			return ST_Agsym_s.this.getInt(fieldName);
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return ST_Agsym_s.this.setPtr(fieldName, newData);
		}

		@Override
		public CString getCString(String fieldName) {
			return ST_Agsym_s.this.getCString(fieldName);
		}
	}

	@Override
	public StarStruct amp() {
		return new Amp(this);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("name")) {
			this.name = (CString) newData;
			return name;
		}
		if (fieldName.equals("defval")) {
			this.defval = (CString) newData;
			return defval;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("kind")) {
			this.kind = data;
			return;
		}
		if (fieldName.equals("id")) {
			this.id = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("kind")) {
			return kind;
		}
		if (fieldName.equals("id")) {
			return id;
		}
		return super.getInt(fieldName);
	}

	@Override
	public CString getCString(String fieldName) {
		if (fieldName.equals("defval")) {
			return defval;
		}
		if (fieldName.equals("name")) {
			return name;
		}
		return super.getCString(fieldName);
	}

	public StarStruct from_link(ST_dtlink_s from) {
		if (from == link) {
			return amp();
		}
		throw new IllegalArgumentException();
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agsym_s",
	// "{",
	// "Dtlink_t link",
	// "char *name",
	// "char *defval",
	// "int id",
	// "unsigned char kind",
	// "unsigned char fixed",
	// "unsigned char print",
	// "}");
}

// struct Agsym_s { /* symbol in one of the above dictionaries */
// Dtlink_t link;
// char *name; /* attribute's name */
// char *defval; /* its default value for initialization */
// int id; /* its index in attr[] */
// unsigned char kind; /* referent object type */
// unsigned char fixed; /* immutable value */
// unsigned char print; /* always print */
// };