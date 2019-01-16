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

public class ST_XLabels_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_XLabels_t() {
		this(null);
	}

	public ST_XLabels_t(StarStruct parent) {
		this.parent = parent;
	}

	// "typedef struct XLabels_s",
	// "{",
	public ST_object_t.Array /*ST_object_t*/ objs;
	public int n_objs;
	public ST_xlabel_t.Array /*ST_xlabel_t*/ lbls;
	public int n_lbls;
	public ST_label_params_t /*ST_label_params_t*/ params;
	public ST_dt_s hdx;
	public ST_RTree spdx;
	// "}",
	// "XLabels_t");

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("n_objs")) {
			this.n_objs = data;
			return;
		}
		if (fieldName.equals("n_lbls")) {
			this.n_lbls = data;
			return;
		}
		super.setInt(fieldName, data);
	}


	
	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("hdx")) {
			this.hdx = (ST_dt_s) newData;
			return this.hdx;
		}
		if (fieldName.equals("spdx")) {
			this.spdx = (ST_RTree) newData;
			return this.spdx;
		}
//		if (fieldName.equals("objs")) {
//			this.objs = newData;
//			return this.objs;
//		}
//		if (fieldName.equals("lbls")) {
//			this.lbls = newData;
//			return this.lbls;
//		}
		if (fieldName.equals("params")) {
			this.params = (ST_label_params_t) newData;
			return this.params;
		}
		return super.setPtr(fieldName, newData);
	}

}

// typedef struct XLabels_s {
// object_t *objs;
// int n_objs;
// xlabel_t *lbls;
// int n_lbls;
// label_params_t *params;
//
// Dt_t *hdx; // splay tree keyed with hilbert spatial codes
// RTree_t *spdx; // rtree
//
// } XLabels_t;