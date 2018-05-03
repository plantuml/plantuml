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
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_spline_info_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_spline_info_t() {
		this(null);
	}

	public ST_spline_info_t(StarStruct parent) {
		this.parent = parent;
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct",
	// "{",
	private int LeftBound, RightBound, Splinesep, Multisep;
	private StarArrayOfPtr Rank_box;

	// "}",
	// "spline_info_t");

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {
		@Override
		public int getInt(String fieldName) {
			return ST_spline_info_t.this.getInt(fieldName);
		}
		
		@Override
		public __ptr__ getPtr(String fieldName) {
			return ST_spline_info_t.this.getPtr(fieldName);
		}

	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("Rank_box")) {
			this.Rank_box = (StarArrayOfPtr) newData;
			return this.Rank_box;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("Rank_box")) {
			return this.Rank_box;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("LeftBound")) {
			return this.LeftBound;
		}
		if (fieldName.equals("RightBound")) {
			return this.RightBound;
		}
		if (fieldName.equals("Splinesep")) {
			return this.Splinesep;
		}
		if (fieldName.equals("Multisep")) {
			return this.Multisep;
		}
		return super.getInt(fieldName);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("LeftBound")) {
			this.LeftBound = data;
			return;
		}
		if (fieldName.equals("RightBound")) {
			this.RightBound = data;
			return;
		}
		if (fieldName.equals("Splinesep")) {
			this.Splinesep = data;
			return;
		}
		if (fieldName.equals("Multisep")) {
			this.Multisep = data;
			return;
		}
		super.setInt(fieldName, data);
	}

}

// typedef struct {
// int LeftBound, RightBound, Splinesep, Multisep;
// boxf* Rank_box;
// } spline_info_t;