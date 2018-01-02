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
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Agnodeinfo_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_Agnodeinfo_t() {
		this(null);
	}

	public ST_Agnodeinfo_t(StarStruct parent) {
		this.parent = parent;
	}

	private final ST_Agrec_s hdr = new ST_Agrec_s(this);

	private ST_shape_desc shape;
	private ST_polygon_t shape_info;
	private final ST_pointf coord = new ST_pointf(this);
	private double width, height;

	// "boxf bb",
	private double ht, lw, rw;
	private ST_textlabel_t label;
	private ST_textlabel_t xlabel;
	// "void *alg",
	private ST_Agedge_s alg = null;
	// "char state",
	// "unsigned char gui_state",
	// "boolean clustnode",
	// "unsigned char pinned",
	private int id, heapindex, hops;
	// "double *pos, dist",
	private int showboxes;

	private boolean has_port;
	// "node_t* rep",
	// "node_t *set",
	private int node_type, mark, onstack;
	private int ranktype, weight_class;
	private ST_Agnode_s next;
	private ST_Agnode_s prev;
	// "elist in, out, flat_out, flat_in, other",
	private final ST_elist in = new ST_elist(this);
	private final ST_elist out = new ST_elist(this);
	private final ST_elist flat_out = new ST_elist(this);
	private final ST_elist flat_in = new ST_elist(this);
	private final ST_elist other = new ST_elist(this);
	private ST_Agraph_s.Amp clust;
	private int UF_size;

	private ST_Agnode_s UF_parent;

	private ST_Agnode_s inleaf, outleaf;
	private int rank, order;
	private double mval;
	private final ST_elist save_in = new ST_elist(this);
	private final ST_elist save_out = new ST_elist(this);
	private final ST_elist tree_in = new ST_elist(this);
	private final ST_elist tree_out = new ST_elist(this);
	private ST_Agedge_s par;
	private int low, lim;
	private int priority;

	// "double pad[1]",
	// "}",
	// "Agnodeinfo_t");

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {

	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Agrec_s.class) {
			return hdr;
		}
		return super.castTo(dest);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("width")) {
			this.width = data;
			return;
		}
		if (fieldName.equals("height")) {
			this.height = data;
			return;
		}
		if (fieldName.equals("ht")) {
			this.ht = data;
			return;
		}
		if (fieldName.equals("lw")) {
			this.lw = data;
			return;
		}
		if (fieldName.equals("rw")) {
			this.rw = data;
			return;
		}
		if (fieldName.equals("mval")) {
			this.mval = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("width")) {
			return this.width;
		}
		if (fieldName.equals("height")) {
			return this.height;
		}
		if (fieldName.equals("ht")) {
			return this.ht;
		}
		if (fieldName.equals("lw")) {
			return this.lw;
		}
		if (fieldName.equals("rw")) {
			return this.rw;
		}
		if (fieldName.equals("mval")) {
			return this.mval;
		}
		return super.getDouble(fieldName);
	}

	@Override
	public void setBoolean(String fieldName, boolean data) {
		if (fieldName.equals("mark")) {
			this.mark = data ? 1 : 0;
			return;
		}
		if (fieldName.equals("onstack")) {
			this.onstack = data ? 1 : 0;
			return;
		}
		super.setBoolean(fieldName, data);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("showboxes")) {
			this.showboxes = data;
			return;
		}
		if (fieldName.equals("UF_size")) {
			this.UF_size = data;
			return;
		}
		if (fieldName.equals("mark")) {
			this.mark = data;
			return;
		}
		if (fieldName.equals("onstack")) {
			this.onstack = data;
			return;
		}
		if (fieldName.equals("priority")) {
			this.priority = data;
			return;
		}
		if (fieldName.equals("node_type")) {
			this.node_type = data;
			return;
		}
		if (fieldName.equals("rank")) {
			this.rank = data;
			return;
		}
		if (fieldName.equals("order")) {
			this.order = data;
			return;
		}
		if (fieldName.equals("ranktype")) {
			this.ranktype = data;
			return;
		}
		if (fieldName.equals("low")) {
			this.low = data;
			return;
		}
		if (fieldName.equals("lim")) {
			this.lim = data;
			return;
		}
		if (fieldName.equals("weight_class")) {
			this.weight_class = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("UF_size")) {
			return this.UF_size;
		}
		if (fieldName.equals("ranktype")) {
			return this.ranktype;
		}
		if (fieldName.equals("mark")) {
			return this.mark;
		}
		if (fieldName.equals("onstack")) {
			return this.onstack;
		}
		if (fieldName.equals("priority")) {
			return this.priority;
		}
		if (fieldName.equals("node_type")) {
			return this.node_type;
		}
		if (fieldName.equals("rank")) {
			return this.rank;
		}
		if (fieldName.equals("order")) {
			return this.order;
		}
		if (fieldName.equals("ranktype")) {
			return this.ranktype;
		}
		if (fieldName.equals("low")) {
			return this.low;
		}
		if (fieldName.equals("lim")) {
			return this.lim;
		}
		if (fieldName.equals("weight_class")) {
			return this.weight_class;
		}
		if (fieldName.equals("id")) {
			return this.id;
		}
		if (fieldName.equals("heapindex")) {
			return this.heapindex;
		}
		if (fieldName.equals("hops")) {
			return this.hops;
		}
		return super.getInt(fieldName);
	}

	@Override
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("onstack")) {
			return this.onstack != 0;
		}
		if (fieldName.equals("has_port")) {
			return this.has_port;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("shape")) {
			this.shape = (ST_shape_desc) newData;
			return shape;
		}
		if (fieldName.equals("shape_info")) {
			this.shape_info = (ST_polygon_t) newData;
			return shape_info;
		}
		if (fieldName.equals("label")) {
			this.label = (ST_textlabel_t) newData;
			return label;
		}
		if (fieldName.equals("clust")) {
			if (newData instanceof ST_Agraph_s) {
				this.clust = (h.ST_Agraph_s.Amp) ((ST_Agraph_s) newData).amp();
			} else {
				this.clust = (ST_Agraph_s.Amp) newData;
			}
			return clust;
		}
		if (fieldName.equals("next")) {
			this.next = (ST_Agnode_s) newData;
			return next;
		}
		if (fieldName.equals("prev")) {
			if (newData instanceof ST_Agnode_s.Amp) {
				this.prev = ((ST_Agnode_s.Amp) newData).getObject();
			} else {
				this.prev = (ST_Agnode_s) newData;
			}
			return prev;
		}
		if (fieldName.equals("UF_parent")) {
			this.UF_parent = (ST_Agnode_s) newData;
			return UF_parent;
		}
		if (fieldName.equals("par")) {
			if (newData instanceof ST_Agedge_s.Amp) {
				this.par = ((ST_Agedge_s.Amp) newData).getObject();
			} else {
				this.par = (ST_Agedge_s) newData;
			}
			return par;
		}
		if (fieldName.equals("alg")) {
			this.alg = (ST_Agedge_s) alg;
			return alg;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("shape")) {
			return shape;
		}
		if (fieldName.equals("label")) {
			return label;
		}
		if (fieldName.equals("UF_parent")) {
			return UF_parent;
		}
		if (fieldName.equals("next")) {
			return next;
		}
		if (fieldName.equals("prev")) {
			return prev;
		}
		if (fieldName.equals("clust")) {
			return clust;
		}
		if (fieldName.equals("inleaf")) {
			return inleaf;
		}
		if (fieldName.equals("outleaf")) {
			return outleaf;
		}
		if (fieldName.equals("alg")) {
			return alg;
		}
		if (fieldName.equals("par")) {
			return par;
		}
		if (fieldName.equals("xlabel")) {
			return xlabel;
		}
		if (fieldName.equals("shape_info")) {
			return shape_info;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("in")) {
			return in;
		}
		if (fieldName.equals("out")) {
			return out;
		}
		if (fieldName.equals("flat_out")) {
			return flat_out;
		}
		if (fieldName.equals("flat_in")) {
			return flat_in;
		}
		if (fieldName.equals("other")) {
			return other;
		}
		if (fieldName.equals("save_in")) {
			return save_in;
		}
		if (fieldName.equals("save_out")) {
			return save_out;
		}
		if (fieldName.equals("tree_in")) {
			return tree_in;
		}
		if (fieldName.equals("tree_out")) {
			return tree_out;
		}
		if (fieldName.equals("coord")) {
			return coord;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("save_in")) {
			save_in.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("save_out")) {
			save_out.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("out")) {
			out.copyDataFrom(newData);
			return;
		}
		if (fieldName.equals("in")) {
			in.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}
}

// typedef struct Agnodeinfo_t {
// Agrec_t hdr;
// shape_desc *shape;
// void *shape_info;
// pointf coord;
// double width, height; /* inches */
// boxf bb;
// double ht, lw, rw;
// textlabel_t *label;
// textlabel_t *xlabel;
// void *alg;
// char state;
// unsigned char gui_state; /* Node state for GUI ops */
// boolean clustnode;
//
//
// unsigned char pinned;
// int id, heapindex, hops;
// double *pos, dist;
//
//
// unsigned char showboxes;
// boolean has_port;
// node_t* rep;
// node_t *set;
//
// /* fast graph */
// char node_type, mark, onstack;
// char ranktype, weight_class;
// node_t *next, *prev;
// elist in, out, flat_out, flat_in, other;
// graph_t *clust;
//
// /* for union-find and collapsing nodes */
// int UF_size;
// node_t *UF_parent;
// node_t *inleaf, *outleaf;
//
// /* for placing nodes */
// int rank, order; /* initially, order = 1 for ordered edges */
// double mval;
// elist save_in, save_out;
//
// /* for network-simplex */
// elist tree_in, tree_out;
// edge_t *par;
// int low, lim;
// int priority;
//
// double pad[1];
//
//
// } Agnodeinfo_t;