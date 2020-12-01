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

import smetana.core.__struct__;

final public class ST_Agedgeinfo_t extends ST_Agrec_s {

	public ST_splines spl;
	public final ST_port tail_port = new ST_port(), head_port = new ST_port();
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
	public void ___(__struct__ other) {
		ST_Agedgeinfo_t this2 = (ST_Agedgeinfo_t) other;
		this.name = this2.name;
		this.next = this2.next;
		this.spl = this2.spl;
		this.tail_port.___((__struct__) this2.tail_port);
		this.head_port.___((__struct__) this2.head_port);
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