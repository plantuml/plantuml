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
import java.util.Arrays;
import java.util.List;

import smetana.core.__ptr__;

//2 9k0huyjtpb77v42bdkmmy7zo5

public interface attr_state_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"attrsym_t* E_constr",
"attrsym_t* E_samehead",
"attrsym_t* E_sametail",
"attrsym_t* E_weight",
"attrsym_t* E_minlen",
"attrsym_t* E_fontcolor",
"attrsym_t* E_fontname",
"attrsym_t* E_fontsize",
"attrsym_t* E_headclip",
"attrsym_t* E_headlabel",
"attrsym_t* E_label",
"attrsym_t* E_label_float",
"attrsym_t* E_labelfontcolor",
"attrsym_t* E_labelfontname",
"attrsym_t* E_labelfontsize",
"attrsym_t* E_tailclip",
"attrsym_t* E_taillabel",
"attrsym_t* E_xlabel",
"attrsym_t* N_height",
"attrsym_t* N_width",
"attrsym_t* N_shape",
"attrsym_t* N_style",
"attrsym_t* N_fontsize",
"attrsym_t* N_fontname",
"attrsym_t* N_fontcolor",
"attrsym_t* N_label",
"attrsym_t* N_xlabel",
"attrsym_t* N_showboxes",
"attrsym_t* N_ordering",
"attrsym_t* N_sides",
"attrsym_t* N_peripheries",
"attrsym_t* N_skew",
"attrsym_t* N_orientation",
"attrsym_t* N_distortion",
"attrsym_t* N_fixed",
"attrsym_t* N_nojustify",
"attrsym_t* N_group",
"attrsym_t* G_ordering",
"int        State",
"}",
"attr_state_t");
}

// typedef struct {
//     attrsym_t* E_constr;
//     attrsym_t* E_samehead;
//     attrsym_t* E_sametail;
//     attrsym_t* E_weight;
//     attrsym_t* E_minlen;
//     attrsym_t* E_fontcolor;
//     attrsym_t* E_fontname;
//     attrsym_t* E_fontsize;
//     attrsym_t* E_headclip;
//     attrsym_t* E_headlabel;
//     attrsym_t* E_label;
//     attrsym_t* E_label_float;
//     attrsym_t* E_labelfontcolor;
//     attrsym_t* E_labelfontname;
//     attrsym_t* E_labelfontsize;
//     attrsym_t* E_tailclip;
//     attrsym_t* E_taillabel;
//     attrsym_t* E_xlabel;
// 
//     attrsym_t* N_height;
//     attrsym_t* N_width;
//     attrsym_t* N_shape;
//     attrsym_t* N_style;
//     attrsym_t* N_fontsize;
//     attrsym_t* N_fontname;
//     attrsym_t* N_fontcolor;
//     attrsym_t* N_label;
//     attrsym_t* N_xlabel;
//     attrsym_t* N_showboxes;
//     attrsym_t* N_ordering;
//     attrsym_t* N_sides;
//     attrsym_t* N_peripheries;
//     attrsym_t* N_skew;
//     attrsym_t* N_orientation;
//     attrsym_t* N_distortion;
//     attrsym_t* N_fixed;
//     attrsym_t* N_nojustify;
//     attrsym_t* N_group;
// 
//     attrsym_t* G_ordering;
//     int        State;
// } attr_state_t;