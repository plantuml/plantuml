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

import smetana.core.CFunction;
import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_dt_s extends UnsupportedStructAndPtr implements HardcodedStruct {

	public CFunction searchf;/* search function */

	public ST_dtdisc_s disc; /* method to manipulate objs */
	public ST_dtdata_s data; /* sharable data */
	public CFunction memoryf;/* function to alloc/free memory */
	public ST_dtmethod_s meth; /* dictionary method */

	public int type; /* type information */
	public int nview; /* number of parent view dictionaries */
	public ST_dt_s view; /* next on viewpath */
	public ST_dt_s walk; /* dictionary being walked */
	public __ptr__ user; /* for user's usage */

	public ST_dt_s() {
		this(null);
	}

	public ST_dt_s(StarStruct parent) {
	}

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("searchf")) {
			this.searchf = (CFunction) newData;
			return searchf;
		}
		if (fieldName.equals("meth")) {
			this.meth = (ST_dtmethod_s) newData;
			return meth;
		}
		if (fieldName.equals("disc")) {
			this.disc = (ST_dtdisc_s) newData;
			return disc;
		}
		if (fieldName.equals("memoryf")) {
			this.memoryf = (CFunction) newData;
			return memoryf;
		}
		if (fieldName.equals("view")) {
			this.view = (ST_dt_s) newData;
			return view;
		}
		if (fieldName.equals("walk")) {
			this.walk = (ST_dt_s) newData;
			return walk;
		}
		if (fieldName.equals("user")) {
			this.user = newData;
			return user;
		}
		if (fieldName.equals("data")) {
			this.data = (ST_dtdata_s) newData;
			return data;
		}
		return super.setPtr(fieldName, newData);
	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("type")) {
			this.type = data;
			return;
		}
		if (fieldName.equals("nview")) {
			this.nview = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public boolean isSameThan(StarStruct other) {
		ST_dt_s other2 = (ST_dt_s) other;
		return this == other2;
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "struct _dt_s",
	// "{",
	// "Dtsearch_f searchf",
	// "Dtdisc_t* disc",
	// "Dtdata_t* data",
	// "Dtmemory_f memoryf",
	// "Dtmethod_t* meth",
	// "int  type",
	// "int  nview",
	// "Dt_t*  view",
	// "Dt_t*  walk",
	// "void*  user",
	// "}");
}

// struct _dt_s
// { Dtsearch_f searchf;/* search function */
// Dtdisc_t* disc; /* method to manipulate objs */
// Dtdata_t* data; /* sharable data */
// Dtmemory_f memoryf;/* function to alloc/free memory */
// Dtmethod_t* meth; /* dictionary method */
// int type; /* type information */
// int nview; /* number of parent view dictionaries */
// Dt_t* view; /* next on viewpath */
// Dt_t* walk; /* dictionary being walked */
// void* user; /* for user's usage */
// };