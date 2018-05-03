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

import h.ST_Agclos_s.ArrayOfThreePtrDict_t;

import java.util.ArrayList;
import java.util.List;

import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.amiga.Area;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_tedge_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_tedge_t() {
		this(null);
	}

	public ST_tedge_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	// "typedef struct tedge_t",
	// "{",
	private __ptr__ pnl0p;
	private __ptr__ pnl1p;
	// private ST_pointnlink_t.Amp pnl0p;
	// private ST_pointnlink_t.Amp pnl1p;

	private StarArrayOfPtr ltp;
	private StarArrayOfPtr rtp;

	// "struct triangle_t *ltp",
	// "struct triangle_t *rtp",
	// "}",
	// "tedge_t");

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("pnl0p")) {
			return this.pnl0p;
		}
		if (fieldName.equals("pnl1p")) {
			return this.pnl1p;
		}
		if (fieldName.equals("ltp")) {
			return this.ltp;
		}
		if (fieldName.equals("rtp")) {
			return this.rtp;
		}
		return super.getPtr(fieldName);
	}

	// class Singleton extends UnsupportedArrayOfPtr implements __array_of_ptr__ {
	// private final ST_pointnlink_t.Amp data;
	//
	// Singleton(ST_pointnlink_t.Amp data) {
	// this.data = data;
	// }
	//
	// @Override
	// public __ptr__ getPtr() {
	// return data;
	// }
	//
	// @Override
	// public Area getInternal(int idx) {
	// if (idx == 0) {
	// return data;
	// }
	// System.err.println("idx=" + idx);
	// return super.getInternal(idx);
	// }
	//
	// @Override
	// public String toString() {
	// return super.toString() + " " + data + " " + data.getStruct();
	// }
	//
	// @Override
	// public int comparePointerInternal(__array_of_ptr__ other) {
	// System.err.println("other=" + other);
	// Singleton other2 = (Singleton) other;
	// System.err.println("other2.data=" + other2.data);
	// System.err.println("pnl0p=" + pnl0p);
	// System.err.println("pnl0p=" + foo(pnl0p));
	// System.err.println("pnl1p=" + pnl1p);
	// System.err.println("pnl0p=" + foo(pnl1p));
	// // TODO Auto-generated method stub
	// return super.comparePointerInternal(other);
	// }
	//
	// }

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		// if (fieldName.equals("pnl0p")) {
		// this.pnl0p = (ST_pointnlink_t.Amp) newData;
		// return this.pnl0p;
		// }
		if (fieldName.equals("pnl0p")) {
			if (newData instanceof ST_pointnlink_t.Amp) {
				this.pnl0p = (ST_pointnlink_t.Amp) newData;
				// newData = new StarArrayOfPtr(new Singleton((h.ST_pointnlink_t.Amp) newData));
			} else {
				this.pnl0p = (StarArrayOfPtr) newData;
			}
			return this.pnl0p;
		}
		if (fieldName.equals("pnl1p")) {
			if (newData instanceof ST_pointnlink_t.Amp) {
				// newData = new StarArrayOfPtr(new Singleton((h.ST_pointnlink_t.Amp) newData));
				this.pnl1p = (ST_pointnlink_t.Amp) newData;
			} else {
				this.pnl1p = (StarArrayOfPtr) newData;
			}
			return this.pnl1p;
		}
		// if (fieldName.equals("pnl1p")) {
		// this.pnl1p = (ST_pointnlink_t.Amp) newData;
		// return this.pnl1p;
		// }
		if (fieldName.equals("ltp")) {
			this.ltp = (StarArrayOfPtr) newData;
			return this.ltp;
		}
		if (fieldName.equals("rtp")) {
			this.rtp = (StarArrayOfPtr) newData;
			return this.rtp;
		}
		return super.setPtr(fieldName, newData);
	}

	public String foo(StarArrayOfPtr data) {
		__array_of_ptr__ array = data.getInternalArray();
		return array.toString();
	}
}

// typedef struct tedge_t {
// pointnlink_t *pnl0p;
// pointnlink_t *pnl1p;
// struct triangle_t *ltp;
// struct triangle_t *rtp;
// } tedge_t;