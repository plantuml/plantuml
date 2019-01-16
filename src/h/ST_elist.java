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

import java.util.ArrayList;
import java.util.List;

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_elist extends UnsupportedStructAndPtr implements HardcodedStruct {

	public int size;
	public List<ST_Agedge_s> list;

	public ST_elist() {
		this(null);
	}

	public ST_elist(StarStruct parent) {
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		ST_elist other2 = (ST_elist) other;
		this.size = other2.size;
		this.list = other2.list;
	}

	@Override
	public void setStruct(__struct__ value) {
		copyDataFrom(value);
	}

	@Override
	public void ___(__struct__ other) {
		ST_elist other2 = (ST_elist) other;
		this.size = other2.size;
		this.list = other2.list;
	}

	@Override
	public ST_elist copy() {
		final ST_elist result = new ST_elist();
		result.size = this.size;
		result.list = this.list;
		return result;
	}

	public boolean listNotNull() {
		return list != null;
	}

	public void mallocEmpty(int nb) {
		list = new ArrayList<ST_Agedge_s>();
		while (list.size() < nb) {
			list.add(null);
		}
	}


	public void realloc(int nb) {
		if (list == null) {
			list = new ArrayList<ST_Agedge_s>();
		}
		while (list.size() < nb) {
			list.add(null);
		}
	}


	public void free() {
		list = null;
	}

	public void setInList(int idx, Object value) {
		list.set(idx, (ST_Agedge_s) value);
	}

	public __ptr__ getFromList(int i) {
		return (__ptr__) list.get(i);
	}


	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct elist",
	// "{",
	// "edge_t **list",
	// "int size",
	// "}",
	// "elist");
}

// typedef struct elist {
// edge_t **list;
// int size;
// } elist;