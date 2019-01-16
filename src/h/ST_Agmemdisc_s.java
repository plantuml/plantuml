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
import smetana.core.UnsupportedStructAndPtr;

public class ST_Agmemdisc_s extends UnsupportedStructAndPtr {

	public CFunction open;
	public CFunction alloc;
	public CFunction resize;
	public CFunction free;
	public CFunction close;

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agmemdisc_s",
	// "{",
	// "void *(*open) (Agdisc_t*)",
	// "void *(*alloc) (void *state, size_t req)",
	// "void *(*resize) (void *state, void *ptr, size_t old, size_t req)",
	// "void (*free) (void *state, void *ptr)",
	// "void (*close) (void *state)",
	// "}");
}

// struct Agmemdisc_s { /* memory allocator */
// void *(*open) (Agdisc_t*); /* independent of other resources */
// void *(*alloc) (void *state, size_t req);
// void *(*resize) (void *state, void *ptr, size_t old, size_t req);
// void (*free) (void *state, void *ptr);
// void (*close) (void *state);
// };