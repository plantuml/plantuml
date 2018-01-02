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

import java.util.ArrayList;
import java.util.List;

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_nlist_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	// public __ptr__ list;
	public int size;
	private final List data = new ArrayList();

	public ST_nlist_t(StarStruct parent) {
	}

	public ST_nlist_t() {
		this(null);
	}

	public void reallocEmpty(int n_nodes, Class<Agnode_s> class1) {
		while (data.size() < n_nodes) {
			data.add(null);
		}
	}

	public void allocEmpty(int n_nodes, Class<Agnode_s> class1) {
		data.clear();
		reallocEmpty(n_nodes, class1);
	}

	public void setInList(int idx, Agnode_s value) {
		data.set(idx, value);
	}

	public __ptr__ getFromList(int i) {
		return (__ptr__) data.get(i);
	}

	public void resetList() {
		data.clear();
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct nlist_t",
	// "{",
	// "node_t **list",
	// "int size",
	// "}",
	// "nlist_t");
}

// typedef struct nlist_t {
// node_t **list;
// int size;
// } nlist_t;