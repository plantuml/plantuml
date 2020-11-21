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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_Agnode_s extends UnsupportedStructAndPtr {

	public final ST_Agobj_s base = new ST_Agobj_s(this);
	public ST_Agraph_s root;
	public final ST_Agsubnode_s mainsub = new ST_Agsubnode_s(this);

	private final StarStruct parent;
	public String NAME;
	
	@Override
	public String toString() {
		return super.toString()+" "+NAME;
	}


	public ST_Agnode_s() {
		this(null);
	}

	public ST_Agnode_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agnode_s.class) {
			return this;
		}
		if (dest == ST_Agobj_s.class) {
			return base;
		}
		return super.castTo(dest);
	}
	
	public ST_Agobj_s castTo_ST_Agobj_s() {
		return base;
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		ST_Agnode_s other2 = (ST_Agnode_s) other;
		return this == other2;
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("root")) {
			this.root = (ST_Agraph_s) newData;
			return root;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public ST_Agnode_s getStruct() {
		return this;
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agnode_s",
	// "{",
	// "Agobj_t base",
	// "Agraph_t *root",
	// "Agsubnode_t mainsub",
	// "}");
}

// struct Agnode_s {
// Agobj_t base;
// Agraph_t *root;
// Agsubnode_t mainsub; /* embedded for main graph */
// };