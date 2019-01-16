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
import smetana.core.amiga.StarStruct;

public class ST_HDict_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public final ST_dtlink_s link = new ST_dtlink_s(this);
	public int key;
	public final ST_Branch_t d = new ST_Branch_t(); /* Should be ST_Leaf_t */
	
	public ST_HDict_t() {
		this(null);
	}

	public ST_HDict_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public Object addVirtualBytes(int virtualBytes) {
		if (virtualBytes == 0) {
			return this;
		}
		OFFSET offset = OFFSET.fromInt(virtualBytes);
		if (offset.toString().equals("h.ST_HDict_t::key")) {
			return key;
		}
		System.err.println("offset="+offset);

		// TODO Auto-generated method stub
		return super.addVirtualBytes(virtualBytes);
	}

//	@Override
//	public __ptr__ getPtr(String fieldName) {
//		if (fieldName.equals("d")) {
//			return this.d;
//		}
//		return super.getPtr(fieldName);
//	}
	
	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("key")) {
			this.key = data;
			return;
		}
		super.setInt(fieldName, data);
	}
	
	
	// "typedef struct obyh",
	// "{",
	// "Dtlink_t link",
	// "int key",
	// "Leaf_t d",
	// "}",
	// "HDict_t");
}

// typedef struct obyh {
// Dtlink_t link;
// int key;
// Leaf_t d;
// } HDict_t;