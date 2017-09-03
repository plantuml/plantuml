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

public class ST_textlabel_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_textlabel_t() {
		this(null);
	}

	public ST_textlabel_t(StarStruct parent) {
		this.parent = parent;
	}

	private CString text, fontname, fontcolor;
	private int charset;
	private double fontsize;

	private final ST_pointf dimen = new ST_pointf(this);
	private final ST_pointf space = new ST_pointf(this);
	private final ST_pointf pos = new ST_pointf(this);

	// "union",
	// "{",
	// "struct",
	// "{",
	// private ST_textspan_t span;
	private __ptr__ span;
	private int nspans;

	// "}",
	// "txt",
	// "htmllabel_t *html",
	private final __ptr__ html = null;
	// "}",
	// "u",
	private int valign;

	private int set;

	// "boolean html",
	// "}",
	// "textlabel_t");

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("dimen")) {
			return dimen;
		}
		if (fieldName.equals("space")) {
			return space;
		}
		if (fieldName.equals("pos")) {
			return pos;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("space")) {
			space.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("pos")) {
			pos.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("text")) {
			this.text = (CString) newData;
			return text;
		}
		if (fieldName.equals("fontname")) {
			this.fontname = (CString) newData;
			return fontname;
		}
		if (fieldName.equals("fontcolor")) {
			this.fontcolor = (CString) newData;
			return fontcolor;
		}
		if (fieldName.equals("u.txt.span")) {
			this.span = newData;
			return span;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public CString getCString(String fieldName) {
		if (fieldName.equals("text")) {
			return text;
		}
		return super.getCString(fieldName);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("text")) {
			return text;
		}
		if (fieldName.equals("u.txt.span")) {
			return span;
		}
		if (fieldName.equals("fontname")) {
			return fontname;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("html")) {
			return html != null;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("fontsize")) {
			this.fontsize = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("fontsize")) {
			return this.fontsize;
		}
		return super.getDouble(fieldName);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("charset")) {
			this.charset = data;
			return;
		}
		if (fieldName.equals("valign")) {
			this.valign = data;
			return;
		}
		if (fieldName.equals("u.txt.nspans")) {
			this.nspans = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public void setBoolean(String fieldName, boolean data) {
		if (fieldName.equals("set")) {
			this.set = data ? 1 : 0;
			return;
		}
		super.setBoolean(fieldName, data);
	}
	
	@Override
	public boolean isSameThan(StarStruct other) {
		ST_textlabel_t other2 = (ST_textlabel_t) other;
		return this == other2;
	}


	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("charset")) {
			return this.charset;
		}
		if (fieldName.equals("valign")) {
			return this.valign;
		}
		if (fieldName.equals("u.txt.nspans")) {
			return this.nspans;
		}
		if (fieldName.equals("set")) {
			return this.set;
		}
		return super.getInt(fieldName);
	}
}

// typedef struct textlabel_t {
// char *text, *fontname, *fontcolor;
// int charset;
// double fontsize;
// pointf dimen; /* the diagonal size of the label (estimated by layout) */
// pointf space; /* the diagonal size of the space for the label */
// /* the rendered label is aligned in this box */
// /* space does not include pad or margin */
// pointf pos; /* the center of the space for the label */
// union {
// struct {
// textspan_t *span;
// short nspans;
// } txt;
// htmllabel_t *html;
// } u;
// char valign; /* 't' 'c' 'b' */
// boolean set; /* true if position is set */
// boolean html; /* true if html label */
// } textlabel_t;