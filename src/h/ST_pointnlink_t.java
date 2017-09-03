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

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedSize_t;
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.size_t;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarArrayOfStruct;
import smetana.core.amiga.StarStruct;

public class ST_pointnlink_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	public ST_pointf.Amp pp;
	public ST_pointnlink_t.Amp link;

	public ST_pointnlink_t() {
		this(null);
	}

	public ST_pointnlink_t(StarStruct parent) {
	}

	public class Amp extends UnsupportedStarStruct {

		@Override
		public __ptr__ getPtr(String fieldName) {
			if (fieldName.equals("pp")) {
				return pp;
			}
			if (fieldName.equals("link")) {
				return link;
			}
			return super.getPtr(fieldName);
		}

		@Override
		public __struct__ getStruct() {
			return ST_pointnlink_t.this;
		}

		@Override
		public __ptr__ setPtr(String fieldName, __ptr__ newData) {
			return ST_pointnlink_t.this.setPtr(fieldName, newData);
		}

		@Override
		public boolean isSameThan(StarStruct other) {
			ST_pointnlink_t.Amp other2 = (Amp) other;
			return this.getStruct() == other2.getStruct();
		}

		@Override
		public Class getRealClass() {
			return ST_pointnlink_t.class;
		}

	}

	@Override
	public __ptr__ getPtr(String fieldName) {
		if (fieldName.equals("pp")) {
			return pp;
		}
		if (fieldName.equals("link")) {
			return link;
		}
		return super.getPtr(fieldName);
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("pp")) {
			if (newData instanceof StarArrayOfPtr) {
				StarArrayOfPtr tmp = (StarArrayOfPtr) newData;
				ST_pointf data = (ST_pointf) tmp.getStruct();
				pp = (ST_pointf.Amp) data.amp();
				return pp;
			}
			if (newData instanceof StarArrayOfStruct) {
				StarArrayOfStruct tmp = (StarArrayOfStruct) newData;
				ST_pointf data = (ST_pointf) tmp.getStruct();
				pp = (ST_pointf.Amp) data.amp();
				return pp;
			}
			System.err.println("newData1=" + newData.getClass());
			pp = (ST_pointf.Amp) newData;
			return pp;
		}
		if (fieldName.equals("link")) {
			if (newData == null) {
				link = null;
				return link;
			}
			if (newData instanceof StarArrayOfPtr) {
				StarArrayOfPtr tmp = (StarArrayOfPtr) newData;
				ST_pointnlink_t data = (ST_pointnlink_t) tmp.getStruct();
				link = (ST_pointnlink_t.Amp) data.amp();
				return link;
			}
			if (newData instanceof StarArrayOfStruct) {
				StarArrayOfStruct tmp = (StarArrayOfStruct) newData;
				ST_pointnlink_t data = (ST_pointnlink_t) tmp.getStruct();
				link = (ST_pointnlink_t.Amp) data.amp();
				return link;
			}
			if (newData instanceof ST_pointnlink_t.Amp) {
				ST_pointnlink_t.Amp tmp = (ST_pointnlink_t.Amp) newData;
				link = tmp;
				return link;
			}
			System.err.println("newData2=" + newData.getClass());
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public StarStruct amp() {
		return new Amp();
	}

	public static size_t sizeof(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return new StarArrayOfPtr(new STArray<ST_pointnlink_t>(nb, 0, ST_pointnlink_t.class));
			}

			@Override
			public int getInternalNb() {
				return nb;
			}
		};
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

	class StarStructAdaptor extends UnsupportedStarStruct {
		private final ST_pointnlink_t me;

		public StarStructAdaptor(ST_pointnlink_t me) {
			this.me = me;
		}

		@Override
		public boolean isSameThan(StarStruct other) {
			StarStructAdaptor other2 = (StarStructAdaptor) other;
			return this.me == other2.me;
		}
	}

	@Override
	public StarStruct getInternalData() {
		return new StarStructAdaptor(this);
	}

	public static size_t sizeof_starstar_empty(final int nb) {
		return new UnsupportedSize_t(nb) {
			@Override
			public Object malloc() {
				return STStarArrayOfPointer.malloc(nb);
			}

			@Override
			public int getInternalNb() {
				return nb;
			}
		};
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct pointnlink_t",
	// "{",
	// "Ppoint_t *pp",
	// "struct pointnlink_t *link",
	// "}",
	// "pointnlink_t");
}

// typedef struct pointnlink_t {
// Ppoint_t *pp;
// struct pointnlink_t *link;
// } pointnlink_t;