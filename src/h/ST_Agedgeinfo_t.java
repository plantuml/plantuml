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
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Agedgeinfo_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_Agedgeinfo_t() {
		this(null);
	}

	public ST_Agedgeinfo_t(StarStruct parent) {
		this.parent = parent;
	}

	public final ST_Agrec_s hdr = new ST_Agrec_s(this);

	public ST_splines spl;
	public final ST_port tail_port = new ST_port(this), head_port = new ST_port(this);
	public ST_textlabel_t label, head_label, tail_label, xlabel;
	public int edge_type;
	public int adjacent;
	public boolean label_ontop;
	// "unsigned char gui_state",
	public ST_Agedge_s to_orig;
	// "void *alg",
	// "double factor",
	public double dist;
	// "Ppolyline_t path",
	public int showboxes;
	public boolean conc_opp_flag;
	public int xpenalty;
	public int weight;
	public int cutvalue, tree_index;
	public int count;
	public int minlen;

	public ST_Agedge_s to_virt;

	@Override
	public void copyDataFrom(__struct__ other) {
		ST_Agedgeinfo_t this2 = (ST_Agedgeinfo_t) other;
		this.hdr.copyDataFrom((__struct__) this2.hdr);
		this.spl = this2.spl;
		this.tail_port.copyDataFrom((__struct__) this2.tail_port);
		this.head_port.copyDataFrom((__struct__) this2.head_port);
		this.label = this2.label;
		this.head_label = this2.head_label;
		this.tail_label = this2.tail_label;
		this.xlabel = this2.xlabel;
		this.edge_type = this2.edge_type;
		this.adjacent = this2.adjacent;
		this.label_ontop = this2.label_ontop;
		this.to_orig = this2.to_orig;
		this.dist = this2.dist;
		this.showboxes = this2.showboxes;
		this.conc_opp_flag = this2.conc_opp_flag;
		this.xpenalty = this2.xpenalty;
		this.weight = this2.weight;
		this.cutvalue = this2.cutvalue;
		this.tree_index = this2.tree_index;
		this.count = this2.count;
		this.minlen = this2.minlen;
		this.to_virt = this2.to_virt;
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agrec_s.class) {
			return hdr;
		}
		if (dest == ST_Agedgeinfo_t.class) {
			return this;
		}
		return super.castTo(dest);
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("tail_port")) {
			this.tail_port.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("head_port")) {
			this.head_port.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("dist")) {
			this.dist = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("minlen")) {
			this.minlen = data;
			return;
		}
		if (fieldName.equals("weight")) {
			this.weight = data;
			return;
		}
		if (fieldName.equals("cutvalue")) {
			this.cutvalue = data;
			return;
		}
		if (fieldName.equals("tree_index")) {
			this.tree_index = data;
			return;
		}
		if (fieldName.equals("count")) {
			this.count = data;
			return;
		}
		if (fieldName.equals("xpenalty")) {
			this.xpenalty = data;
			return;
		}
		if (fieldName.equals("showboxes")) {
			this.showboxes = data;
			return;
		}
		if (fieldName.equals("edge_type")) {
			this.edge_type = data;
			return;
		}
		if (fieldName.equals("adjacent")) {
			this.adjacent = data;
			return;
		}
		super.setInt(fieldName, data);
	}


	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("to_virt")) {
				this.to_virt = (ST_Agedge_s) newData;
			return this.to_virt;
		}
		if (fieldName.equals("to_orig")) {
				this.to_orig = (ST_Agedge_s) newData;
			return this.to_orig;
		}
		if (fieldName.equals("spl")) {
			this.spl = (ST_splines) newData;
			return this.spl;
		}
		if (fieldName.equals("label")) {
			this.label = (ST_textlabel_t) newData;
			return this.label;
		}
		if (fieldName.equals("head_label")) {
			this.head_label = (ST_textlabel_t) newData;
			return this.head_label;
		}
		if (fieldName.equals("tail_label")) {
			this.tail_label = (ST_textlabel_t) newData;
			return this.tail_label;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

}

// typedef struct Agedgeinfo_t {
// Agrec_t hdr;
// splines *spl;
// port tail_port, head_port;
// textlabel_t *label, *head_label, *tail_label, *xlabel;
// char edge_type;
// char adjacent; /* true for flat edge with adjacent nodes */
// char label_ontop;
// unsigned char gui_state; /* Edge state for GUI ops */
// edge_t *to_orig; /* for dot's shapes.c */
// void *alg;
//
//
// double factor;
// double dist;
// Ppolyline_t path;
//
//
// unsigned char showboxes;
// boolean conc_opp_flag;
// short xpenalty;
// int weight;
// int cutvalue, tree_index;
// short count;
// unsigned short minlen;
// edge_t *to_virt;
//
// } Agedgeinfo_t;