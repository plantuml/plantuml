/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2022, Arnaud Roques
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

import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.CFunction;


//typedef struct Agraphinfo_t {
//Agrec_t hdr;
///* to generate code */
//layout_t *drawing;
//textlabel_t *label; /* if the cluster has a title */
//boxf bb; /* bounding box */
//pointf border[4]; /* sizes of margins for graph labels */
//unsigned char gui_state; /* Graph state for GUI ops */
//unsigned char has_labels;
//boolean has_images;
//unsigned char charset; /* input character set */
//int rankdir;
//double ht1, ht2; /* below and above extremal ranks */
//unsigned short flags;
//void *alg;
//GVC_t *gvc; /* context for "globals" over multiple graphs */
//void (*cleanup) (graph_t * g); /* function to deallocate layout-specific data */
//
//
///* to place nodes */
//node_t **neato_nlist;
//int move;
//double **dist, **spring, **sum_t, ***t;
//unsigned short ndim;
//unsigned short odim;
//
//
///* to have subgraphs */
//int n_cluster;
//graph_t **clust; /* clusters are in clust[1..n_cluster] !!! */
//graph_t *dotroot;
//node_t *nlist;
//rank_t *rank;
//graph_t *parent; /* containing cluster (not parent subgraph) */
//int level; /* cluster nesting level (not node level!) */
//node_t *minrep, *maxrep; /* set leaders for min and max rank */
//
///* fast graph node list */
//nlist_t comp;
///* connected components */
//node_t *minset, *maxset; /* set leaders */
//long n_nodes;
///* includes virtual */
//short minrank, maxrank;
//
///* various flags */
//boolean has_flat_edges;
//boolean has_sourcerank;
//boolean has_sinkrank;
//unsigned char showboxes;
//fontname_kind fontnames; /* to override mangling in SVG */
//
//int nodesep, ranksep;
//node_t *ln, *rn; /* left, right nodes of bounding box */
//
///* for clusters */
//node_t *leader, **rankleader;
//boolean expanded;
//char installed;
//char set_type;
//char label_pos;
//boolean exact_ranksep;
//
//
//} Agraphinfo_t;
final public class ST_Agraphinfo_t extends ST_Agrec_s {

	// /* to generate code */
	public ST_layout_t drawing;
	public ST_textlabel_t label; /* if the cluster has a title */
	public final ST_boxf bb = new ST_boxf(); /* bounding box */
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
	public CArrayOfStar<ST_Agraph_s> clust;
	public ST_Agraph_s dotroot;
	public ST_Agnode_s nlist;
	public CArray<ST_rank_t> rank;
	public ST_Agraph_s parent; /* containing cluster (not parent subgraph) */
	// int level; /* cluster nesting level (not node level!) */
	// node_t *minrep, *maxrep; /* set leaders for min and max rank */
	//
	// /* fast graph node list */
	public final ST_nlist_t comp = new ST_nlist_t();
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
	public CArrayOfStar<ST_Agnode_s> rankleader;
	public boolean expanded;
	public int installed;
	public int set_type;
	public int label_pos;
	public int exact_ranksep;

}

