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

import smetana.core.OFFSET;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_dtlink_s extends UnsupportedStructAndPtr implements WithParent {

	public ST_dtlink_s right;
	public ST_dtlink_s _left;
	private final StarStruct parent;

	@Override
	public void copyDataFrom(__struct__ other) {
		ST_dtlink_s this2 = (ST_dtlink_s) other;
		this.right = this2.right;
		this._left = this2._left;
	}

	public ST_dtlink_s() {
		this(null);
	}

	public ST_dtlink_s(StarStruct parent) {
		this.parent = parent;
	}

	public StarStruct getParent() {
		return parent;
	}

	// @Override
	// public __ptr__ castTo(Class dest) {
	// if (dest == ST_dtlink_s.class) {
	// // return amp();
	// return this;
	// }
	// if (dest == _dthold_s.class) {
	// return new LinkTo_dthold_s();
	// }
	// return super.castTo(dest);
	// }

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_dtlink_s.class) {
			return this;
		}
		// if (dest == refstr_t.class && getParent() instanceof ST_refstr_t) {
		// return (ST_refstr_t) getParent();
		// }
		if (dest == ST_dthold_s.class && getParent() instanceof ST_dthold_s) {
			// System.err.println("ITSME");
			// System.err.println("getParent()=" + getParent());
			return (ST_dthold_s) getParent();

		}
		System.err.println("dest=" + dest);
		return super.castTo(dest);
	}

	public ST_dthold_s castTo_ST_dthold_s() {
		if (getParent() instanceof ST_dthold_s) {
			return (ST_dthold_s) getParent();
		}
		throw new UnsupportedOperationException();
	}

	class LinkTo_dthold_s extends UnsupportedStructAndPtr {

	}

	class LinkTo_Obj extends UnsupportedStructAndPtr {

	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		if (virtualBytes < 0) {
			final OFFSET offset = OFFSET.fromInt(-virtualBytes);
			if (offset.toString().equals("h.ST_Agsubnode_s::id_link")) {
				return ((ST_Agsubnode_s) parent).from_id_link(ST_dtlink_s.this);
			}
			if (offset.toString().equals("h.ST_Agsubnode_s::seq_link")) {
				return ((ST_Agsubnode_s) parent).from_seq_link(ST_dtlink_s.this);
			}
			if (offset.toString().equals("h.ST_Agsym_s::link")) {
				return ((ST_Agsym_s) parent).from_link(ST_dtlink_s.this);
			}
			if (offset.toString().equals("h.ST_Agedge_s::seq_link")) {
				return ((ST_Agedge_s) parent).from_seq_link(ST_dtlink_s.this);
			}
			if (offset.toString().equals("h.ST_Agedge_s::id_link")) {
				return ((ST_Agedge_s) parent).from_id_link(ST_dtlink_s.this);
			}
			if (offset.toString().equals("h.ST_Agraph_s::link")) {
				return ((ST_Agraph_s) parent).from_link(ST_dtlink_s.this);
			}
			System.err.println("virtualBytes=" + virtualBytes);
			System.err.println("offset=" + offset);
			return super.addVirtualBytes(virtualBytes);
		}
		final OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.ST_refstr_t::s") && parent instanceof ST_refstr_t) {
			return ((ST_refstr_t) parent).to_s(ST_dtlink_s.this);

		}
		System.err.println("virtualBytes=" + virtualBytes);
		System.err.println("offset=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	// @Override
	// public __ptr__ setPtr(String fieldName, __ptr__ newData) {
	// if (fieldName.equals("hl._left")) {
	// this._left = (ST_dtlink_s) newData;
	// return _left;
	// }
	// if (fieldName.equals("right")) {
	// this.right = (ST_dtlink_s) newData;
	// return right;
	// }
	// return super.setPtr(fieldName, newData);
	// }

	// @Override
	// public __ptr__ getPtr(String fieldName) {
	// // if (fieldName.equals("hl._left")) {
	// // return _left;
	// // }
	// if (fieldName.equals("right")) {
	// return right;
	// }
	// return super.getPtr(fieldName);
	// }

	// public interface ST_dtlink_s extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "struct _dtlink_s",
	// "{",
	// "Dtlink_t* right",
	// "union",
	// "{",
	// "unsigned int _hash",
	// "Dtlink_t* _left",
	// "}",
	// "hl",
	// "}");
}

// struct _dtlink_s
// { Dtlink_t* right; /* right child */
// union
// { unsigned int _hash; /* hash value */
// Dtlink_t* _left; /* left child */
// } hl;
// };