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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_BestPos_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public int n;
	public double area;
	public final ST_pointf pos = new ST_pointf(this);

	public ST_BestPos_t() {
		this(null);
	}

	public ST_BestPos_t(StarStruct parent) {
		this.parent = parent;
	}

	@Override
	public __struct__ copy() {
		final ST_BestPos_t result = new ST_BestPos_t();
		result.n = this.n;
		result.area = this.area;
		result.pos.copyDataFrom((__struct__) this.pos);
		return result;
	}
	
	public void ___(__struct__ other) {
		ST_BestPos_t this2 = (ST_BestPos_t) other;
		this.n = this2.n;
		this.area = this2.area;
		this.pos.copyDataFrom((__struct__) this2.pos);
	}


	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("pos")) {
			pos.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("n")) {
			this.n = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("area")) {
			this.area = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	// typedef struct best_p_s {
	// int n;
	// double area;
	// pointf pos;
	// } BestPos_t;
}
