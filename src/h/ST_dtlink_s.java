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

import smetana.core.OFFSET;
import smetana.core.UnsupportedStarStruct;
import smetana.core.__ptr__;
import smetana.core.__struct__;

public class ST_dtlink_s extends UnsupportedStarStruct {

	public ST_dtlink_s right;
	public ST_dtlink_s _left;
	private final __ptr__ container;

	@Override
	public void ___(__struct__ other) {
		ST_dtlink_s this2 = (ST_dtlink_s) other;
		this.right = this2.right;
		this._left = this2._left;
	}

	public ST_dtlink_s() {
		this(null);
	}

	public ST_dtlink_s(__ptr__ parent) {
		this.container = parent;
	}

	public __ptr__ getParent() {
		return container;
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_dtlink_s.class) {
			return this;
		}
		if (dest == ST_dthold_s.class && getParent() instanceof ST_dthold_s) {
			return (ST_dthold_s) getParent();

		}
		System.err.println("dest=" + dest);
		return super.castTo(dest);
	}



	@Override
	public Object getTheField(OFFSET offset) {
		if (offset == null || offset.getSign() == 0) {
			return this;
		}
		if (offset.getField().equals("s") && container instanceof ST_refstr_t) {
			return ((ST_refstr_t) container).s;

		}
		// Negative because we go back to the parent
		offset = offset.negative();

		return container;
//		if (offset.getKey().equals("h.ST_Agsubnode_s::id_link")) {
//			return ((ST_Agsubnode_s) parent);
//		}
//		if (offset.getKey().equals("h.ST_Agsubnode_s::seq_link")) {
//			return ((ST_Agsubnode_s) parent);
//		}
//		if (offset.getKey().equals("h.ST_Agsym_s::link")) {
//			return ((ST_Agsym_s) parent);
//		}
//		if (offset.getKey().equals("h.ST_Agedge_s::seq_link")) {
//			return ((ST_Agedge_s) parent);
//		}
//		if (offset.getKey().equals("h.ST_Agedge_s::id_link")) {
//			return ((ST_Agedge_s) parent);
//		}
//		if (offset.getKey().equals("h.ST_Agraph_s::link")) {
//			return ((ST_Agraph_s) parent);
//		}
	}

}

// struct _dtlink_s
// { Dtlink_t* right; /* right child */
// union
// { unsigned int _hash; /* hash value */
// Dtlink_t* _left; /* left child */
// } hl;
// };