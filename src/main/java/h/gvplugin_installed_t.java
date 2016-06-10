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

//2 dcmpl8ruksxab5p8161qcvxcw

public interface gvplugin_installed_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"int id",
"const char *type",
"int quality",
"void *engine",
"void *features",
"}",
"gvplugin_installed_t");
}

// typedef struct {
// 	int id;         /* an id that is only unique within a package 
// 			of plugins of the same api.
// 			A renderer-type such as "png" in the cairo package
// 			has an id that is different from the "ps" type
// 			in the same package */
// 	const char *type;	/* a string name, such as "png" or "ps" that
// 			distinguishes different types withing the same
// 			 (renderer in this case) */
// 	int quality;    /* an arbitrary integer used for ordering plugins of
// 			the same type from different packages */
// 	void *engine;   /* pointer to the jump table for the plugin */
// 	void *features; /* pointer to the feature description 
// 				void* because type varies by api */
//     } gvplugin_installed_t;