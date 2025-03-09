/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import smetana.core.FieldOffset;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.debug.SmetanaDebug;

final public class ST_Agraph_s extends ST_Agobj_s {

	public final ST_Agobj_s base = this;
	public final ST_Agdesc_s desc = new ST_Agdesc_s();
	public final ST_dtlink_s link = new ST_dtlink_s(this);

	public ST_dt_s n_seq; /* the node set in sequence */
	public ST_dt_s n_id; /* the node set indexed by ID */
	public ST_dt_s e_seq; /* holders for edge sets */
	public ST_dt_s e_id; /* holders for edge sets */
	public ST_dt_s g_dict; /* subgraphs - descendants */
	public ST_Agraph_s parent; /* subgraphs - ancestors */
	public ST_Agraph_s root; /* subgraphs - ancestors */
	public ST_Agclos_s clos; /* shared resources */

	public String NAME;
	private static int CPT = 0;

//	@Override
//	public String toString() {
//		return super.toString() + " " + NAME;
//	}

	public ST_Agraph_s() {
		this.NAME = "G" + CPT;
		CPT++;
		SmetanaDebug.LOG("creation " + this);
	}

	@Override
	public Object getTheField(FieldOffset offset) {
		if (offset == null || offset.getSign() == 0)
			return this;

		if (offset == FieldOffset.link)
			return link;

		throw new UnsupportedOperationException();
	}

	@Override
	public void ___(__struct__ arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameThan(__ptr__ other) {
		ST_Agraph_s other2 = (ST_Agraph_s) other;
		return this == other2;
	}

}

// struct Agraph_s {
// Agobj_t base;
// Agdesc_t desc;
// Dtlink_t link;
// Dict_t *n_seq; /* the node set in sequence */
// Dict_t *n_id; /* the node set indexed by ID */
// Dict_t *e_seq, *e_id; /* holders for edge sets */
// Dict_t *g_dict; /* subgraphs - descendants */
// Agraph_t *parent, *root; /* subgraphs - ancestors */
// Agclos_t *clos; /* shared resources */
// };