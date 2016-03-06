/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
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
import java.util.Arrays;
import java.util.List;

import smetana.core.__ptr__;

//2 2mtqmob86ayzjvuxgvhlg4ctl

public interface Agraphinfo_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct Agraphinfo_t",
"{",
"Agrec_t hdr",
"layout_t *drawing",
"textlabel_t *label",
"boxf bb",
"pointf border[4]",
"unsigned char gui_state",
"unsigned char has_labels",
"boolean has_images",
"unsigned char charset",
"int rankdir",
"double ht1, ht2",
"unsigned short flags",
"void *alg",
"GVC_t *gvc",
"void (*cleanup) (graph_t * g)",
"node_t **neato_nlist",
"int move",
"double **dist, **spring, **sum_t, ***t",
"unsigned short ndim",
"unsigned short odim",
"int n_cluster",
"graph_t **clust",
"graph_t *dotroot",
"node_t *nlist",
"rank_t *rank",
"graph_t *parent",
"int level",
"node_t *minrep, *maxrep",
"nlist_t comp",
"node_t *minset, *maxset",
"long n_nodes",
"short minrank, maxrank",
"boolean has_flat_edges",
"boolean has_sourcerank",
"boolean has_sinkrank",
"unsigned char showboxes",
"fontname_kind fontnames",
"int nodesep, ranksep",
"node_t *ln, *rn",
"node_t *leader, **rankleader",
"boolean expanded",
"char installed",
"char set_type",
"char label_pos",
"boolean exact_ranksep",
"}",
"Agraphinfo_t");
}

// typedef struct Agraphinfo_t {
// 	Agrec_t hdr;
// 	/* to generate code */
// 	layout_t *drawing;
// 	textlabel_t *label;	/* if the cluster has a title */
// 	boxf bb;			/* bounding box */
// 	pointf border[4];	/* sizes of margins for graph labels */
// 	unsigned char gui_state; /* Graph state for GUI ops */
// 	unsigned char has_labels;
// 	boolean has_images;
// 	unsigned char charset; /* input character set */
// 	int rankdir;
// 	double ht1, ht2;	/* below and above extremal ranks */
// 	unsigned short flags;
// 	void *alg;
// 	GVC_t *gvc;	/* context for "globals" over multiple graphs */
// 	void (*cleanup) (graph_t * g);   /* function to deallocate layout-specific data */
// 
// 
// 	/* to place nodes */
// 	node_t **neato_nlist;
// 	int move;
// 	double **dist, **spring, **sum_t, ***t;
// 	unsigned short ndim;
// 	unsigned short odim;
// 
// 
// 	/* to have subgraphs */
// 	int n_cluster;
// 	graph_t **clust;	/* clusters are in clust[1..n_cluster] !!! */
// 	graph_t *dotroot;
// 	node_t *nlist;
// 	rank_t *rank;
// 	graph_t *parent;        /* containing cluster (not parent subgraph) */
// 	int level;		/* cluster nesting level (not node level!) */
// 	node_t	*minrep, *maxrep;	/* set leaders for min and max rank */
// 
// 	/* fast graph node list */
// 	nlist_t comp;
// 	/* connected components */
// 	node_t *minset, *maxset;	/* set leaders */
// 	long n_nodes;
// 	/* includes virtual */
// 	short minrank, maxrank;
// 
// 	/* various flags */
// 	boolean has_flat_edges;
// 	boolean has_sourcerank;
// 	boolean has_sinkrank;
// 	unsigned char	showboxes;
// 	fontname_kind fontnames;		/* to override mangling in SVG */
// 
// 	int nodesep, ranksep;
// 	node_t *ln, *rn;	/* left, right nodes of bounding box */
// 
// 	/* for clusters */
// 	node_t *leader, **rankleader;
// 	boolean expanded;
// 	char installed;
// 	char set_type;
// 	char label_pos;
// 	boolean exact_ranksep;
// 
// 
//     } Agraphinfo_t;