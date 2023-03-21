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

final public class ST_Agedge_s extends ST_Agobj_s {

	public ST_Agedge_s PREV;
	public ST_Agedge_s NEXT;

	public final ST_Agobj_s base = this;
	public final ST_dtlink_s id_link = new ST_dtlink_s(this);
	public final ST_dtlink_s seq_link = new ST_dtlink_s(this);
	public ST_Agnode_s node;

	public String NAME;

//	@Override
//	public String toString() {
//		return NAME;
//	}

	@Override
	public void ___(__struct__ arg) {
		ST_Agedge_s this2 = (ST_Agedge_s) arg;
		this.tag.___(this2.tag);
		this.data = this2.data;
		this.id_link.___(this2.id_link);
		this.seq_link.___(this2.seq_link);
		this.node = this2.node;
	}

	@Override
	public boolean isSameThan(__ptr__ other) {
		ST_Agedge_s other2 = (ST_Agedge_s) other;
		return this == other2;
	}

	@Override
	public Object getTheField(FieldOffset offset) {
		if (offset == null || offset.getSign() == 0) {
			return this;
		}
		if (offset == FieldOffset.seq_link)
			return seq_link;

		if (offset == FieldOffset.id_link)
			return id_link;

		throw new UnsupportedOperationException();
	}

	public ST_Agedge_s plus_(int pointerMove) {
		if (pointerMove == 1 && NEXT != null) {
			return NEXT;
		}
		if (pointerMove == -1 && PREV != null) {
			return PREV;
		}
		throw new UnsupportedOperationException();
	}
}

// struct Agedge_s {
// Agobj_t base;
// Dtlink_t id_link; /* main graph only */
// Dtlink_t seq_link;
// Agnode_t *node; /* the endpoint node */
// };