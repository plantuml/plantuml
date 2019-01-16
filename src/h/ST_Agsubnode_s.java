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

import smetana.core.HardcodedStruct;
import smetana.core.OFFSET;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_Agsubnode_s extends UnsupportedStructAndPtr implements HardcodedStruct {

	public final ST_dtlink_s seq_link = new ST_dtlink_s(this);
	public final ST_dtlink_s id_link = new ST_dtlink_s(this);
	public ST_Agnode_s node;
	public ST_dtlink_s in_id;
	public ST_dtlink_s out_id;
	public ST_dtlink_s in_seq;
	public ST_dtlink_s out_seq;

	public ST_Agsubnode_s() {
		this(null);
	}

	public ST_Agsubnode_s(StarStruct parent) {
	}

	public ST_Agsubnode_s from_id_link(ST_dtlink_s from) {
		if (from == id_link) {
			return this;
		}
		throw new IllegalArgumentException();
	}

	public ST_Agsubnode_s from_seq_link(ST_dtlink_s from) {
		if (from == seq_link) {
			return this;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		return this == (ST_Agsubnode_s) other;
	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		final OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.ST_Agsubnode_s::id_link")) {
			return id_link;
		}
		if (offset.toString().equals("h.ST_Agsubnode_s::seq_link")) {
			return seq_link;
		}
		System.err.println("virtualBytes=" + virtualBytes);
		System.err.println("offset=" + offset);
		return super.addVirtualBytes(virtualBytes);
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agsubnode_s.class) {
			return ST_Agsubnode_s.this;
		}
		System.err.println("dest=" + dest);
		return super.castTo(dest);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("node")) {
			this.node = (ST_Agnode_s) newData;
			return node;
		}
		if (fieldName.equals("in_id")) {
			this.in_id = (h.ST_dtlink_s) newData;
			return in_id;
		}
		if (fieldName.equals("out_seq")) {
			this.out_seq = (h.ST_dtlink_s) newData;
			return out_seq;
		}
		if (fieldName.equals("out_id")) {
			this.out_id = (h.ST_dtlink_s) newData;
			return out_id;
		}
		if (fieldName.equals("seq_link")) {
			this.seq_link.copyDataFrom(newData);
			return seq_link;
		}
		if (fieldName.equals("in_seq")) {
			this.in_seq = (h.ST_dtlink_s) newData;
			return in_seq;
		}
		return super.setPtr(fieldName, newData);
	}


	// public interface ST_Agsubnode_s extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agsubnode_s",
	// "{",
	// "Dtlink_t seq_link",
	// "Dtlink_t id_link",
	// "Agnode_t *node",
	// "Dtlink_t *in_id, *out_id",
	// "Dtlink_t *in_seq, *out_seq",
	// "}");
}

// struct Agsubnode_s { /* the node-per-graph-or-subgraph record */
// Dtlink_t seq_link; /* must be first */
// Dtlink_t id_link;
// Agnode_t *node; /* the object */
// Dtlink_t *in_id, *out_id; /* by node/ID for random access */
// Dtlink_t *in_seq, *out_seq; /* by node/sequence for serial access */
// };