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

//2 c1b3tf6cmbwk2cvu1u6j2rduc

public interface Agedgeinfo_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct Agedgeinfo_t",
"{",
"Agrec_t hdr",
"splines *spl",
"port tail_port, head_port",
"textlabel_t *label, *head_label, *tail_label, *xlabel",
"char edge_type",
"char adjacent",
"char label_ontop",
"unsigned char gui_state",
"edge_t *to_orig",
"void *alg",
"double factor",
"double dist",
"Ppolyline_t path",
"unsigned char showboxes",
"boolean conc_opp_flag",
"short xpenalty",
"int weight",
"int cutvalue, tree_index",
"short count",
"unsigned short minlen",
"edge_t *to_virt",
"}",
"Agedgeinfo_t");
}

// typedef struct Agedgeinfo_t {
// 	Agrec_t hdr;
// 	splines *spl;
// 	port tail_port, head_port;
// 	textlabel_t *label, *head_label, *tail_label, *xlabel;
// 	char edge_type;
// 	char adjacent;          /* true for flat edge with adjacent nodes */
// 	char label_ontop;
// 	unsigned char gui_state; /* Edge state for GUI ops */
// 	edge_t *to_orig;	/* for dot's shapes.c    */
// 	void *alg;
// 
// 
// 	double factor;
// 	double dist;
// 	Ppolyline_t path;
// 
// 
// 	unsigned char showboxes;
// 	boolean conc_opp_flag;
// 	short xpenalty;
// 	int weight;
// 	int cutvalue, tree_index;
// 	short count;
// 	unsigned short minlen;
// 	edge_t *to_virt;
// 
//     } Agedgeinfo_t;