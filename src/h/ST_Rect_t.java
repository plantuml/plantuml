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

public class ST_Rect_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	public final int[] boundary = new int[2 * 2];

	public ST_Rect_t(StarStruct parent) {
	}

	public ST_Rect_t() {
	}

	@Override
	public void ___(__struct__ other) {
		this.copyDataFrom(other);
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		ST_Rect_t other2 = (ST_Rect_t) other;
		this.boundary[0] = other2.boundary[0];
		this.boundary[1] = other2.boundary[1];
		this.boundary[2] = other2.boundary[2];
		this.boundary[3] = other2.boundary[3];
	}

	// typedef struct Rect {
	// int boundary[2*2];
	// } Rect_t;
}
