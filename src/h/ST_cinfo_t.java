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
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_cinfo_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	public final ST_boxf bb = new ST_boxf(this);
	public ST_object_t.Array objp;

	public ST_cinfo_t(StarStruct parent) {
	}

	public ST_cinfo_t() {
	}
	
	@Override
	public void ___(__struct__ value) {
		final ST_cinfo_t other = (ST_cinfo_t) value;
		this.bb.setStruct(other.bb);
		this.objp = other.objp;
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("bb")) {
			this.bb.setStruct(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}
	
	
	@Override
	public ST_cinfo_t copy() {
		final ST_cinfo_t result = new ST_cinfo_t();
		result.bb.copyDataFrom((__struct__) this.bb);
		result.objp = this.objp;
		return result;
	}




}

// typedef struct {
// boxf bb;
// object_t* objp;
// } cinfo_t;