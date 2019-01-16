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
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_shape_functions extends UnsupportedStructAndPtr {

	public CFunction initfn;
	public CFunction freefn;
	public CFunction portfn;
	public CFunction insidefn;
	public CFunction pboxfn;
	public CFunction codefn;

	public ST_shape_functions() {
		this(null);
	}

	public ST_shape_functions(StarStruct parent) {
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("initfn")) {
			this.initfn = (CFunction) newData;
			return newData;
		}
		if (fieldName.equals("freefn")) {
			this.freefn = (CFunction) newData;
			return newData;
		}
		if (fieldName.equals("portfn")) {
			this.portfn = (CFunction) newData;
			return newData;
		}
		if (fieldName.equals("insidefn")) {
			this.insidefn = (CFunction) newData;
			return newData;
		}
		if (fieldName.equals("pboxfn")) {
			this.pboxfn = (CFunction) newData;
			return newData;
		}
		if (fieldName.equals("codefn")) {
			this.codefn = (CFunction) newData;
			return newData;
		}
		return super.setPtr(fieldName, newData);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct shape_functions",
	// "{",
	// "void (*initfn) (node_t *)",
	// "void (*freefn) (node_t *)",
	// "port(*portfn) (node_t *, char *, char *)",
	// "boolean(*insidefn) (inside_t * inside_context, pointf)",
	// "int (*pboxfn)(node_t* n, port* p, int side, boxf rv[], int *kptr)",
	// "void (*codefn) (GVJ_t * job, node_t * n)",
	// "}",
	// "shape_functions");
}

// typedef struct shape_functions { /* read-only shape functions */
// void (*initfn) (node_t *); /* initializes shape from node u.shape_info structure */
// void (*freefn) (node_t *); /* frees shape from node u.shape_info structure */
// port(*portfn) (node_t *, char *, char *); /* finds aiming point and slope of port */
// boolean(*insidefn) (inside_t * inside_context, pointf); /* clips incident gvc->e spline on shape of gvc->n */
// int (*pboxfn)(node_t* n, port* p, int side, boxf rv[], int *kptr); /* finds box path to reach port */
// void (*codefn) (GVJ_t * job, node_t * n); /* emits graphics code for node */
// } shape_functions;