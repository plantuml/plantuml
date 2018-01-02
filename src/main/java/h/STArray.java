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

import smetana.core.UnsupportedArrayOfPtr;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.Area;
import smetana.core.amiga.StarArrayOfPtr;

public class STArray<O extends UnsupportedStructAndPtr> extends UnsupportedArrayOfPtr {

	private final int pos;
	private final List<O> list;
	private final Class clazz;

	public STArray(int pos, List<O> list, Class clazz) {
		this.pos = pos;
		this.list = list;
		this.clazz = clazz;
	}

	@Override
	public int comparePointerInternal(__array_of_ptr__ other) {
		STArray<O> other2 = (STArray<O>) other;
		if (this.list != other2.list) {
			throw new IllegalArgumentException();
		}
		return this.pos - other2.pos;
	}

	public STArray(int size, int pos, Class clazz) {
		this.pos = pos;
		this.list = new ArrayList<O>();
		this.clazz = clazz;
		for (int i = 0; i < size; i++) {
			try {
				this.list.add((O) clazz.newInstance());
			} catch (Exception e) {
				throw new IllegalStateException(e.toString());
			}
		}
	}

	@Override
	public void realloc(size_t nb) {
		realloc(nb.getInternalNb());
	}

	@Override
	public void realloc(int nb) {
		while (list.size() < nb) {
			try {
				this.list.add((O) clazz.newInstance());
			} catch (Exception e) {
				throw new IllegalStateException(e.toString());
			}
		}
	}

	@Override
	public __array_of_ptr__ plus(int delta) {
		return new STArray(pos + delta, this.list, clazz);
	}

	@Override
	public __array_of_ptr__ move(int delta) {
		return new STArray(pos + delta, this.list, clazz);
	}

	@Override
	public void setStruct(__struct__ value) {
		list.get(pos).setStruct(value);
	}

	@Override
	public __struct__ getStruct() {
		return list.get(pos).getStruct();
		// return list.get(pos);
	}

	@Override
	public __struct__ getStruct(String fieldName) {
		return list.get(pos).getStruct(fieldName);
	}

	@Override
	public __ptr__ getPtr() {
		return list.get(pos).amp();
	}

	@Override
	public __ptr__ asPtr() {
		return new StarArrayOfPtr(this);
	}

	@Override
	public void setPtr(__ptr__ value) {
		list.set(pos, (O) value.getStruct());
	}

	@Override
	public Area getInternal(final int idx) {
		return list.get(pos + idx);
	}

}
