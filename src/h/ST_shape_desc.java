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

import smetana.core.CString;
import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_shape_desc extends UnsupportedStructAndPtr implements HardcodedStruct {

	public ST_shape_desc(StarStruct parent) {
	}

	public ST_shape_desc() {
		this(null);
	}

	public CString name;
	public ST_shape_functions fns;
	public ST_polygon_t polygon;
	public boolean usershape;

	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("name")) {
			this.name = (CString) newData;
			return newData;
		}
		if (fieldName.equals("fns")) {
			this.fns = (ST_shape_functions) newData;
			return newData;
		}
		if (fieldName.equals("polygon")) {
			this.polygon = (ST_polygon_t) newData;
			return newData;
		}
		return super.setPtr(fieldName, newData);
	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct shape_desc",
	// "{",
	// "char *name",
	// "shape_functions *fns",
	// "polygon_t *polygon",
	// "boolean usershape",
	// "}",
	// "shape_desc");
}

// typedef struct shape_desc { /* read-only shape descriptor */
// char *name; /* as read from graph file */
// shape_functions *fns;
// polygon_t *polygon; /* base polygon info */
// boolean usershape;
// } shape_desc;