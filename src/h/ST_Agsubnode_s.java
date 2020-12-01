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

import smetana.core.ACCESS;
import smetana.core.CStarStar;
import smetana.core.OFFSET;
import smetana.core.UnsupportedStarStruct;
import smetana.core.__ptr__;

final public class ST_Agsubnode_s extends UnsupportedStarStruct {

	public final ST_dtlink_s seq_link = new ST_dtlink_s(this);
	public final ST_dtlink_s id_link = new ST_dtlink_s(this);
	public ST_Agnode_s node;

	public ST_dtlink_s in_id;
	public CStarStar<ST_dtlink_s> in_id_AMP() {
		return CStarStar.<ST_dtlink_s>BUILD(new ACCESS<ST_dtlink_s>() {
			public ST_dtlink_s get() {
				return in_id;
			}

			public void set(ST_dtlink_s data) {
				in_id = data;
			}
		});
	}

	public ST_dtlink_s out_id;
	public CStarStar<ST_dtlink_s> out_id_AMP() {
		return CStarStar.<ST_dtlink_s>BUILD(new ACCESS<ST_dtlink_s>() {
			public ST_dtlink_s get() {
				return out_id;
			}

			public void set(ST_dtlink_s data) {
				out_id = data;
			}
		});
	}

	public ST_dtlink_s in_seq;
	public CStarStar<ST_dtlink_s> in_seq_AMP() {
		return CStarStar.<ST_dtlink_s>BUILD(new ACCESS<ST_dtlink_s>() {
			public ST_dtlink_s get() {
				return in_seq;
			}

			public void set(ST_dtlink_s data) {
				in_seq = data;
			}
		});
	}

	public ST_dtlink_s out_seq;
	public CStarStar<ST_dtlink_s> out_seq_AMP() {
		return CStarStar.<ST_dtlink_s>BUILD(new ACCESS<ST_dtlink_s>() {
			public ST_dtlink_s get() {
				return out_seq;
			}

			public void set(ST_dtlink_s data) {
				out_seq = data;
			}
		});
	}


	@Override
	public boolean isSameThan(__ptr__ other) {
		return this == (ST_Agsubnode_s) other;
	}

	@Override
	public Object getTheField(OFFSET offset) {
		if (offset == null || offset.getSign()==0) {
			return this;
		}
		if (offset.getField().equals("id_link")) {
			return id_link;
		}
		if (offset.getField().equals("seq_link")) {
			return seq_link;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public __ptr__ castTo(Class dest) {
		if (dest == ST_Agsubnode_s.class) {
			return ST_Agsubnode_s.this;
		}
		System.err.println("dest=" + dest);
		return super.castTo(dest);
	}


}

// struct Agsubnode_s { /* the node-per-graph-or-subgraph record */
// Dtlink_t seq_link; /* must be first */
// Dtlink_t id_link;
// Agnode_t *node; /* the object */
// Dtlink_t *in_id, *out_id; /* by node/ID for random access */
// Dtlink_t *in_seq, *out_seq; /* by node/sequence for serial access */
// };