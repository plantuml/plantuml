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
import smetana.core.UnsupportedStarStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__struct__;
import smetana.core.amiga.Area;
import smetana.core.amiga.AreaInt;
import smetana.core.amiga.InternalData;
import smetana.core.amiga.StarStruct;

public class ST_Agtag_s extends UnsupportedStructAndPtr implements HardcodedStruct {
	public int objtype;
	public int mtflock;
	public int attrwf;
	public int seq;
	public int id;

	public ST_Agtag_s(StarStruct parent) {
	}

	public ST_Agtag_s() {
		this(null);
	}

	@Override
	public Class getRealClass() {
		return ST_Agtag_s.class;
	}

	@Override
	public __struct__ copy() {
		final ST_Agtag_s result = new ST_Agtag_s();
		result.objtype = objtype;
		result.mtflock = mtflock;
		result.attrwf = attrwf;
		result.seq = seq;
		result.id = id;
		return result;
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		// if (other instanceof ST_Agtag_s) {
		final ST_Agtag_s other2 = (ST_Agtag_s) other;
		objtype = other2.objtype;
		mtflock = other2.mtflock;
		attrwf = other2.attrwf;
		seq = other2.seq;
		id = other2.id;
		// } else {
		// objtype = other.getInt("objtype");
		// mtflock = other.getInt("mtflock");
		// attrwf = other.getInt("attrwf");
		// seq = other.getInt("seq");
		// id = other.getInt("id");
		// }
	}

	@Override
	public void memcopyFrom(Area source) {
		final ST_Agtag_s other2 = (ST_Agtag_s) source;
		objtype = other2.objtype;
		mtflock = other2.mtflock;
		attrwf = other2.attrwf;
		seq = other2.seq;
		id = other2.id;
	}

	@Override
	public void ___(__struct__ other) {
		copyDataFrom(other);
	}

	@Override
	public Area getArea(String name) {
		final AreaInt result = new AreaInt();
		if (name.equals("objtype")) {
			result.setInternal(objtype);
			return result;
		}
		if (name.equals("mtflock")) {
			result.setInternal(mtflock);
			return result;
		}
		if (name.equals("attrwf")) {
			result.setInternal(attrwf);
			return result;
		}
		if (name.equals("seq")) {
			result.setInternal(seq);
			return result;
		}
		if (name.equals("id")) {
			result.setInternal(id);
			return result;
		}
		return super.getArea(name);
	}

	public class MyInternalData extends UnsupportedStarStruct implements InternalData {

		@Override
		public Area getArea(String name) {
			final AreaInt result = new AreaInt();
			if (name.equals("objtype")) {
				result.setInternal(objtype);
				return result;
			}
			if (name.equals("mtflock")) {
				result.setInternal(mtflock);
				return result;
			}
			if (name.equals("attrwf")) {
				result.setInternal(attrwf);
				return result;
			}
			if (name.equals("seq")) {
				result.setInternal(seq);
				return result;
			}
			if (name.equals("id")) {
				result.setInternal(id);
				return result;
			}
			return super.getArea(name);
		}

	}

	@Override
	public StarStruct getInternalData() {
		return new MyInternalData();
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct Agtag_s",
	// "{",
	// "unsigned objtype:2",
	// "unsigned mtflock:1",
	// "unsigned attrwf:1",
	// "unsigned seq:(sizeof(unsigned) * 8 - 4)",
	// "unsigned long id",
	// "}");
}

// struct Agtag_s {
// unsigned objtype:2; /* see literal tags below */
// unsigned mtflock:1; /* move-to-front lock, see above */
// unsigned attrwf:1; /* attrs written (parity, write.c) */
// unsigned seq:(sizeof(unsigned) * 8 - 4); /* sequence no. */
// unsigned long id; /* client ID */
// };