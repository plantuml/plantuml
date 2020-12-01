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

import smetana.core.CFunction;
import smetana.core.UnsupportedStarStruct;
import smetana.core.__ptr__;

final public class ST_dt_s extends UnsupportedStarStruct {

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



	@Override
	public boolean isSameThan(__ptr__ other) {
		ST_dt_s other2 = (ST_dt_s) other;
		return this == other2;
	}


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