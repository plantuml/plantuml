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
 * (C) Copyright 2009-2017, Arnaud Roques
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

import java.util.ArrayList;
import java.util.List;

import smetana.core.UnsupportedC;
import smetana.core.__array_of_integer__;
import smetana.core.__ptr__;
import smetana.core.amiga.Area;

public class STStarArrayOfPointer extends UnsupportedC {

	private final int pos;
	private final List<__ptr__> data;

	public void realloc(int nb) {
		while (data.size() < nb) {
			data.add(null);
		}
	}

	public static STStarArrayOfPointer malloc(int size) {
		List<__ptr__> data = new ArrayList<__ptr__>();
		for (int i = 0; i < size; i++) {
			data.add(null);
		}
		return new STStarArrayOfPointer(0, data);
	}

	private STStarArrayOfPointer(int pos, List<__ptr__> data) {
		this.pos = pos;
		this.data = data;
	}

	@Override
	public int comparePointer(__ptr__ other) {
		final STStarArrayOfPointer this2 = (STStarArrayOfPointer) other;
		if (this.data != this2.data) {
			throw new IllegalArgumentException();
		}
		return this.pos - this2.pos;
	}

	@Override
	public __ptr__ plus(int pointerMove) {
		return new STStarArrayOfPointer(pos + pointerMove, data);
	}

	@Override
	public void setPtr(__ptr__ value) {
		data.set(pos, value);
	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		return data.get(pos).getPtr(fieldName);
	}

	@Override
	public __ptr__ getPtr() {
		return data.get(pos);
	}

	public void swap(int i, int j) {
		__ptr__ e1 = data.get(i);
		__ptr__ e2 = data.get(j);
		data.set(i, e2);
		data.set(j, e1);

	}

}
