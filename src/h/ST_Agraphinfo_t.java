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

import smetana.core.CFunction;
import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_Agraphinfo_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	public final ST_Agrec_s hdr = new ST_Agrec_s(this);
	// /* to generate code */
	public ST_layout_t drawing;
	public ST_textlabel_t label; /* if the cluster has a title */
	public final ST_boxf bb = new ST_boxf(this); /* bounding box */
	// pointf border[4]; /* sizes of margins for graph labels */
	public final ST_pointf border[] = new ST_pointf[] { new ST_pointf(), new ST_pointf(), new ST_pointf(),
			new ST_pointf() };
	// unsigned char gui_state; /* Graph state for GUI ops */
	public int has_labels;
	// boolean has_images;
	public int charset; /* input character set */
	public int rankdir;
	public double ht1, ht2; /* below and above extremal ranks */
	public int flags;
	// void *alg;
	public ST_GVC_s gvc; /* context for "globals" over multiple graphs */
	public CFunction cleanup;
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
	public int n_cluster;
	// graph_t **clust; /* clusters are in clust[1..n_cluster] !!! */
	public ST_Agraph_s.Array clust;
	public ST_Agraph_s dotroot;
	public ST_Agnode_s nlist;
	public ST_rank_t.Array2 rank;
	public ST_Agraph_s parent; /* containing cluster (not parent subgraph) */
	// int level; /* cluster nesting level (not node level!) */
	// node_t *minrep, *maxrep; /* set leaders for min and max rank */
	//
	// /* fast graph node list */
	public final ST_nlist_t comp = new ST_nlist_t(this);
	// /* connected components */
	public ST_Agnode_s minset, maxset; /* set leaders */
	public int n_nodes;
	// /* includes virtual */
	public int minrank, maxrank;
	//
	// /* various flags */
	public int has_flat_edges;
	// boolean has_sourcerank;
	// boolean has_sinkrank;
	public int showboxes;
	public int fontnames; /* to override mangling in SVG */
	//
	public int nodesep, ranksep;
	public ST_Agnode_s ln, rn; /* left, right nodes of bounding box */
	//
	// /* for clusters */
	// node_t *leader, **rankleader;
	public ST_Agnode_s leader;
	public ST_Agnode_s.Array rankleader;
	public boolean expanded;
	public int installed;
	public int set_type;
	public int label_pos;
	public int exact_ranksep;

	private final StarStruct parent_;

	public ST_Agraphinfo_t() {
		this(null);
	}

	public ST_Agraphinfo_t(StarStruct parent) {
		this.parent_ = parent;
	}

	class ArrayOfFour extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfFour(int pos) {
			this.pos = pos;
		}

		public ArrayOfFour plus(int delta) {
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

	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agrec_s.class) {
			return hdr;
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
			this.parent = (ST_Agraph_s) newData;
			return parent;
		}
		if (fieldName.equals("clust")) {
			this.clust = (ST_Agraph_s.Array) newData;
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
			this.nlist = (ST_Agnode_s) newData;
			return nlist;
		}
		if (fieldName.equals("leader")) {
			this.leader = (ST_Agnode_s) newData;
			return leader;
		}
		if (fieldName.equals("rankleader")) {
			this.rankleader = (ST_Agnode_s.Array) newData;
			return rankleader;
		}
		if (fieldName.equals("rank")) {
			this.rank = (ST_rank_t.Array2) newData;
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