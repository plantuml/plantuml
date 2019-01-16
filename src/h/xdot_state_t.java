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

//2 dhiu7hb5hm946g0tfbnpowdu5

public interface xdot_state_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"attrsym_t *g_draw",
"attrsym_t *g_l_draw",
"attrsym_t *n_draw",
"attrsym_t *n_l_draw",
"attrsym_t *e_draw",
"attrsym_t *h_draw",
"attrsym_t *t_draw",
"attrsym_t *e_l_draw",
"attrsym_t *hl_draw",
"attrsym_t *tl_draw",
"unsigned char buf[(EMIT_HLABEL+1)][BUFSIZ]",
"unsigned short version",
"char* version_s",
"}",
"xdot_state_t");
}

// typedef struct {
//     attrsym_t *g_draw;
//     attrsym_t *g_l_draw;
//     attrsym_t *n_draw;
//     attrsym_t *n_l_draw;
//     attrsym_t *e_draw;
//     attrsym_t *h_draw;
//     attrsym_t *t_draw;
//     attrsym_t *e_l_draw;
//     attrsym_t *hl_draw;
//     attrsym_t *tl_draw;
//     unsigned char buf[(EMIT_HLABEL+1)][BUFSIZ];
//     unsigned short version;
//     char* version_s;
// } xdot_state_t;