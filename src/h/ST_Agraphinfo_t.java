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

import smetana.core.CFunction;
import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_struct__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStruct;

public class ST_Agraphinfo_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	private final ST_Agrec_s hdr = new ST_Agrec_s(this);
	// /* to generate code */
	private ST_layout_t drawing;
	private ST_textlabel_t label; /* if the cluster has a title */
	private final ST_boxf bb = new ST_boxf(this); /* bounding box */
	// pointf border[4]; /* sizes of margins for graph labels */
	private final ST_pointf border[] = new ST_pointf[] { new ST_pointf(), new ST_pointf(), new ST_pointf(),
			new ST_pointf() };
	// unsigned char gui_state; /* Graph state for GUI ops */
	private int has_labels;
	// boolean has_images;
	private int charset; /* input character set */
	private int rankdir;
	private double ht1, ht2; /* below and above extremal ranks */
	private int flags;
	// void *alg;
	private ST_GVC_s gvc; /* context for "globals" over multiple graphs */
	private CFunction cleanup;
	// void (*cleanup) (graph_t * g); /* function to deallocate layout-specific data */
	//
	//
	// /* to place nodes */
	// node_t **neato_nlist;
	// int move;
	// double **dist, **spring, **sum_t, ***t;
	// unsigned short ndim;
	// unsigned short odim;
	//
	//
	// /* to have subgraphs */
	private int n_cluster;
	// graph_t **clust; /* clusters are in clust[1..n_cluster] !!! */
	private StarArrayOfPtr clust;
	private ST_Agraph_s dotroot;
	private ST_Agnode_s nlist;
	private StarArrayOfPtr rank;
	private ST_Agraph_s parent; /* containing cluster (not parent subgraph) */
	// int level; /* cluster nesting level (not node level!) */
	// node_t *minrep, *maxrep; /* set leaders for min and max rank */
	//
	// /* fast graph node list */
	private final ST_nlist_t comp = new ST_nlist_t(this);
	// /* connected components */
	private ST_Agnode_s minset, maxset; /* set leaders */
	private int n_nodes;
	// /* includes virtual */
	private int minrank, maxrank;
	//
	// /* various flags */
	private int has_flat_edges;
	// boolean has_sourcerank;
	// boolean has_sinkrank;
	private int showboxes;
	private int fontnames; /* to override mangling in SVG */
	//
	private int nodesep, ranksep;
	private ST_Agnode_s ln, rn; /* left, right nodes of bounding box */
	//
	// /* for clusters */
	// node_t *leader, **rankleader;
	private ST_Agnode_s leader;
	private STStarArrayOfPointer rankleader;
	private boolean expanded;
	private int installed;
	// char set_type;
	private int label_pos;
	private int exact_ranksep;

	private final StarStruct parent_;

	public ST_Agraphinfo_t() {
		this(null);
	}

	public ST_Agraphinfo_t(StarStruct parent) {
		this.parent_ = parent;
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public class Amp extends UnsupportedStarStruct {
	}

	class ArrayOfFour extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfFour(int pos) {
			this.pos = pos;
		}

		@Override
		public __array_of_struct__ plus(int delta) {
			return new ArrayOfFour(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return border[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			border[pos].copyDataFrom(value);
		}

		@Override
		public double getDouble(String fieldName) {
			return getStruct().getDouble(fieldName);
		}

	}

	@Override
	public __array_of_struct__ getArrayOfStruct(String fieldName) {
		if (fieldName.equals("border")) {
			return new ArrayOfFour(0);
		}
		return super.getArrayOfStruct(fieldName);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		if (fieldName.equals("comp")) {
			return this.comp;
		}
		if (fieldName.equals("bb")) {
			return this.bb;
		}
		return super.getStruct(fieldName);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == Agrec_s.class) {
			return hdr.amp();
		}
		return super.castTo(dest);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("gvc")) {
			this.gvc = (ST_GVC_s) newData;
			return gvc;
		}
		if (fieldName.equals("drawing")) {
			this.drawing = (ST_layout_t) newData;
			return drawing;
		}
		if (fieldName.equals("dotroot")) {
			this.dotroot = (ST_Agraph_s) newData;
			return dotroot;
		}
		if (fieldName.equals("parent")) {
			if (newData instanceof ST_Agraph_s.Amp) {
				this.parent = ((ST_Agraph_s.Amp) newData).getObject();
			} else {
				this.parent = (ST_Agraph_s) newData;
			}
			return parent;
		}
		if (fieldName.equals("clust")) {
			this.clust = (StarArrayOfPtr) newData;
			return clust;
		}
		if (fieldName.equals("label")) {
			this.label = (ST_textlabel_t) newData;
			return label;
		}
		if (fieldName.equals("maxset")) {
			this.maxset = (ST_Agnode_s) newData;
			return maxset;
		}
		if (fieldName.equals("minset")) {
			this.minset = (ST_Agnode_s) newData;
			return minset;
		}
		if (fieldName.equals("nlist")) {
			if (newData instanceof ST_Agnode_s.Amp) {
				this.nlist = ((ST_Agnode_s.Amp) newData).getObject();
			} else {
				this.nlist = (ST_Agnode_s) newData;
			}
			return nlist;
		}
		if (fieldName.equals("leader")) {
			this.leader = (ST_Agnode_s) newData;
			return leader;
		}
		if (fieldName.equals("rankleader")) {
			this.rankleader = (STStarArrayOfPointer) newData;
			return rankleader;
		}
		if (fieldName.equals("rank")) {
			this.rank = (StarArrayOfPtr) newData;
			return rank;
		}
		if (fieldName.equals("ln")) {
			this.ln = (ST_Agnode_s) newData;
			return ln;
		}
		if (fieldName.equals("rn")) {
			this.rn = (ST_Agnode_s) newData;
			return rn;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("drawing")) {
			return drawing;
		}
		if (fieldName.equals("gvc")) {
			return gvc;
		}
		if (fieldName.equals("parent")) {
			return parent;
		}
		if (fieldName.equals("dotroot")) {
			return dotroot;
		}
		if (fieldName.equals("clust")) {
			return clust;
		}
		if (fieldName.equals("label")) {
			return label;
		}
		if (fieldName.equals("maxset")) {
			return maxset;
		}
		if (fieldName.equals("minset")) {
			return minset;
		}
		if (fieldName.equals("nlist")) {
			return nlist;
		}
		if (fieldName.equals("leader")) {
			return leader;
		}
		if (fieldName.equals("rankleader")) {
			return rankleader;
		}
		if (fieldName.equals("rank")) {
			return rank;
		}
		if (fieldName.equals("ln")) {
			return ln;
		}
		if (fieldName.equals("rn")) {
			return rn;
		}
		if (fieldName.equals("cleanup")) {
			return cleanup;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public void setBoolean(String fieldName, boolean data) {
		if (fieldName.equals("expanded")) {
			this.expanded = data;
			return;
		}
		if (fieldName.equals("has_flat_edges")) {
			this.has_flat_edges = data ? 1 : 0;
			return;
		}
		super.setBoolean(fieldName, data);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("charset")) {
			this.charset = data;
			return;
		}
		if (fieldName.equals("rankdir")) {
			this.rankdir = data;
			return;
		}
		if (fieldName.equals("nodesep")) {
			this.nodesep = data;
			return;
		}
		if (fieldName.equals("ranksep")) {
			this.ranksep = data;
			return;
		}
		if (fieldName.equals("showboxes")) {
			this.showboxes = data;
			return;
		}
		if (fieldName.equals("fontnames")) {
			this.fontnames = data;
			return;
		}
		if (fieldName.equals("flags")) {
			this.flags = data;
			return;
		}
		if (fieldName.equals("has_labels")) {
			this.has_labels = data;
			return;
		}
		if (fieldName.equals("n_cluster")) {
			this.n_cluster = data;
			return;
		}
		if (fieldName.equals("label_pos")) {
			this.label_pos = data;
			return;
		}
		if (fieldName.equals("n_nodes")) {
			this.n_nodes = data;
			return;
		}
		if (fieldName.equals("maxrank")) {
			this.maxrank = data;
			return;
		}
		if (fieldName.equals("minrank")) {
			this.minrank = data;
			return;
		}
		if (fieldName.equals("installed")) {
			this.installed = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public boolean getBoolean(String fieldName) {
		if (fieldName.equals("expanded")) {
			return expanded;
		}
		return super.getBoolean(fieldName);
	}

	@Override
	public int getInt(String fieldName) {
		if (fieldName.equals("flags")) {
			return flags;
		}
		if (fieldName.equals("charset")) {
			return charset;
		}
		if (fieldName.equals("rankdir")) {
			return rankdir;
		}
		if (fieldName.equals("has_labels")) {
			return has_labels;
		}
		if (fieldName.equals("n_cluster")) {
			return n_cluster;
		}
		if (fieldName.equals("label_pos")) {
			return label_pos;
		}
		if (fieldName.equals("n_nodes")) {
			return n_nodes;
		}
		if (fieldName.equals("maxrank")) {
			return maxrank;
		}
		if (fieldName.equals("minrank")) {
			return minrank;
		}
		if (fieldName.equals("has_flat_edges")) {
			return has_flat_edges;
		}
		if (fieldName.equals("installed")) {
			return installed;
		}
		if (fieldName.equals("exact_ranksep")) {
			return exact_ranksep;
		}
		if (fieldName.equals("nodesep")) {
			return nodesep;
		}
		if (fieldName.equals("ranksep")) {
			return ranksep;
		}
		return super.getInt(fieldName);
	}

	@Override
	public double getDouble(String fieldName) {
		if (fieldName.equals("ht1")) {
			return ht1;
		}
		if (fieldName.equals("ht2")) {
			return ht2;
		}
		return super.getDouble(fieldName);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("ht1")) {
			this.ht1 = data;
			return;
		}
		if (fieldName.equals("ht2")) {
			this.ht2 = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct Agraphinfo_t",
	// "{",
	// "Agrec_t hdr",
	// "layout_t *drawing",
	// "textlabel_t *label",
	// "boxf bb",
	// "pointf border[4]",
	// "unsigned char gui_state",
	// "unsigned char has_labels",
	// "boolean has_images",
	// "unsigned char charset",
	// "int rankdir",
	// "double ht1, ht2",
	// "unsigned short flags",
	// "void *alg",
	// "GVC_t *gvc",
	// "void (*cleanup) (graph_t * g)",
	// "node_t **neato_nlist",
	// "int move",
	// "double **dist, **spring, **sum_t, ***t",
	// "unsigned short ndim",
	// "unsigned short odim",
	// "int n_cluster",
	// "graph_t **clust",
	// "graph_t *dotroot",
	// "node_t *nlist",
	// "rank_t *rank",
	// "graph_t *parent",
	// "int level",
	// "node_t *minrep, *maxrep",
	// "nlist_t comp",
	// "node_t *minset, *maxset",
	// "long n_nodes",
	// "short minrank, maxrank",
	// "boolean has_flat_edges",
	// "boolean has_sourcerank",
	// "boolean has_sinkrank",
	// "unsigned char showboxes",
	// "fontname_kind fontnames",
	// "int nodesep, ranksep",
	// "node_t *ln, *rn",
	// "node_t *leader, **rankleader",
	// "boolean expanded",
	// "char installed",
	// "char set_type",
	// "char label_pos",
	// "boolean exact_ranksep",
	// "}",
	// "Agraphinfo_t");
}

// typedef struct Agraphinfo_t {
// Agrec_t hdr;
// /* to generate code */
// layout_t *drawing;
// textlabel_t *label; /* if the cluster has a title */
// boxf bb; /* bounding box */
// pointf border[4]; /* sizes of margins for graph labels */
// unsigned char gui_state; /* Graph state for GUI ops */
// unsigned char has_labels;
// boolean has_images;
// unsigned char charset; /* input character set */
// int rankdir;
// double ht1, ht2; /* below and above extremal ranks */
// unsigned short flags;
// void *alg;
// GVC_t *gvc; /* context for "globals" over multiple graphs */
// void (*cleanup) (graph_t * g); /* function to deallocate layout-specific data */
//
//
// /* to place nodes */
// node_t **neato_nlist;
// int move;
// double **dist, **spring, **sum_t, ***t;
// unsigned short ndim;
// unsigned short odim;
//
//
// /* to have subgraphs */
// int n_cluster;
// graph_t **clust; /* clusters are in clust[1..n_cluster] !!! */
// graph_t *dotroot;
// node_t *nlist;
// rank_t *rank;
// graph_t *parent; /* containing cluster (not parent subgraph) */
// int level; /* cluster nesting level (not node level!) */
// node_t *minrep, *maxrep; /* set leaders for min and max rank */
//
// /* fast graph node list */
// nlist_t comp;
// /* connected components */
// node_t *minset, *maxset; /* set leaders */
// long n_nodes;
// /* includes virtual */
// short minrank, maxrank;
//
// /* various flags */
// boolean has_flat_edges;
// boolean has_sourcerank;
// boolean has_sinkrank;
// unsigned char showboxes;
// fontname_kind fontnames; /* to override mangling in SVG */
//
// int nodesep, ranksep;
// node_t *ln, *rn; /* left, right nodes of bounding box */
//
// /* for clusters */
// node_t *leader, **rankleader;
// boolean expanded;
// char installed;
// char set_type;
// char label_pos;
// boolean exact_ranksep;
//
//
// } Agraphinfo_t;