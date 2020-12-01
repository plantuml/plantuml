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

import static smetana.core.Macro.ND_order;
import static smetana.core.Macro.ND_rank;

import smetana.core.__ptr__;
import smetana.core.__struct__;

final public class ST_Agnode_s extends ST_Agobj_s {

	public final ST_Agobj_s base = this;
	public ST_Agraph_s root;
	public final ST_Agsubnode_s mainsub = new ST_Agsubnode_s();

	public String NAME;

	@Override
	public String toString() {
		try {
			return NAME + " rank=" + ND_rank(this) + " order=" + ND_order(this);
		} catch (Exception e) {
			return NAME;
		}
	}

	@Override
	public void ___(__struct__ arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameThan(__ptr__ other) {
		ST_Agnode_s other2 = (ST_Agnode_s) other;
		return this == other2;
	}

}

// struct Agnode_s {
// Agobj_t base;
// Agraph_t *root;
// Agsubnode_t mainsub; /* embedded for main graph */
// };