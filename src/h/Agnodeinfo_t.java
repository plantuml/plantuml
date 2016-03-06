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

//2 da1kcmh9tvz6n7xaqw78dn4h8

public interface Agnodeinfo_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct Agnodeinfo_t",
"{",
"Agrec_t hdr",
"shape_desc *shape",
"void *shape_info",
"pointf coord",
"double width, height",
"boxf bb",
"double ht, lw, rw",
"textlabel_t *label",
"textlabel_t *xlabel",
"void *alg",
"char state",
"unsigned char gui_state",
"boolean clustnode",
"unsigned char pinned",
"int id, heapindex, hops",
"double *pos, dist",
"unsigned char showboxes",
"boolean  has_port",
"node_t* rep",
"node_t *set",
"char node_type, mark, onstack",
"char ranktype, weight_class",
"node_t *next, *prev",
"elist in, out, flat_out, flat_in, other",
"graph_t *clust",
"int UF_size",
"node_t *UF_parent",
"node_t *inleaf, *outleaf",
"int rank, order",
"double mval",
"elist save_in, save_out",
"elist tree_in, tree_out",
"edge_t *par",
"int low, lim",
"int priority",
"double pad[1]",
"}",
"Agnodeinfo_t");
}

// typedef struct Agnodeinfo_t {
// 	Agrec_t hdr;
// 	shape_desc *shape;
// 	void *shape_info;
// 	pointf coord;
// 	double width, height;  /* inches */
// 	boxf bb;
// 	double ht, lw, rw;
// 	textlabel_t *label;
// 	textlabel_t *xlabel;
// 	void *alg;
// 	char state;
// 	unsigned char gui_state; /* Node state for GUI ops */
// 	boolean clustnode;
// 
// 
// 	unsigned char pinned;
// 	int id, heapindex, hops;
// 	double *pos, dist;
// 
// 
// 	unsigned char showboxes;
// 	boolean  has_port;
// 	node_t* rep;
// 	node_t *set;
// 
// 	/* fast graph */
// 	char node_type, mark, onstack;
// 	char ranktype, weight_class;
// 	node_t *next, *prev;
// 	elist in, out, flat_out, flat_in, other;
// 	graph_t *clust;
// 
// 	/* for union-find and collapsing nodes */
// 	int UF_size;
// 	node_t *UF_parent;
// 	node_t *inleaf, *outleaf;
// 
// 	/* for placing nodes */
// 	int rank, order;	/* initially, order = 1 for ordered edges */
// 	double mval;
// 	elist save_in, save_out;
// 
// 	/* for network-simplex */
// 	elist tree_in, tree_out;
// 	edge_t *par;
// 	int low, lim;
// 	int priority;
// 
// 	double pad[1];
// 
// 
//     } Agnodeinfo_t;