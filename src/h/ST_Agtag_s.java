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

import smetana.core.UnsupportedStarStruct;
import smetana.core.__struct__;

final public class ST_Agtag_s extends UnsupportedStarStruct {
	public int objtype;
	public int mtflock;
	public int attrwf;
	public int seq;
	public int id;

	@Override
	public String toString() {
		return "id=" + id + " " + super.toString();
	}

	@Override
	public ST_Agtag_s copy() {
		final ST_Agtag_s result = new ST_Agtag_s();
		result.objtype = objtype;
		result.mtflock = mtflock;
		result.attrwf = attrwf;
		result.seq = seq;
		result.id = id;
		return result;
	}

	@Override
	public void ___(__struct__ other) {
		final ST_Agtag_s other2 = (ST_Agtag_s) other;
		objtype = other2.objtype;
		mtflock = other2.mtflock;
		attrwf = other2.attrwf;
		seq = other2.seq;
		id = other2.id;
	}

}

// struct Agtag_s {
// unsigned objtype:2; /* see literal tags below */
// unsigned mtflock:1; /* move-to-front lock, see above */
// unsigned attrwf:1; /* attrs written (parity, write.c) */
// unsigned seq:(sizeof(unsigned) * 8 - 4); /* sequence no. */
// unsigned long id; /* client ID */
// };