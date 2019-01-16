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

import smetana.core.CString;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_textfont_t extends UnsupportedStructAndPtr {

	public CString name;
	public double size;

	public ST_textfont_t() {
		this(null);
	}

	public ST_textfont_t(StarStruct parent) {
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("name")) {
			this.name = (CString) newData;
			return name;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("size")) {
			this.size = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	// public interface ST_textfont_t extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct",
	// "{",
	// "char*  name",
	// "char*  color",
	// "PostscriptAlias *postscript_alias",
	// "double size",
	// "unsigned int flags:7",
	// "unsigned int cnt:(sizeof(unsigned int) * 8 - 7)",
	// "}",
	// "textfont_t");
}

// typedef struct {
// char* name;
// char* color;
// PostscriptAlias *postscript_alias;
// double size;
// unsigned int flags:7; /* HTML_UL, HTML_IF, HTML_BF, etc. */
// unsigned int cnt:(sizeof(unsigned int) * 8 - 7); /* reference count */
// } textfont_t;