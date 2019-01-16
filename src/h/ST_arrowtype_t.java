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
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__struct__;

public class ST_arrowtype_t extends UnsupportedStructAndPtr {

	public int type;
	public double lenfact;
	public CFunction gen;

	@Override
	public void ___(__struct__ other) {
		ST_arrowtype_t other2 = (ST_arrowtype_t) other;
		type = other2.type;
		lenfact = other2.lenfact;
		gen = other2.gen;
	}
	
//	@Override
//	public int getInt(String fieldName) {
//		if (fieldName.equals("type")) {
//			return type;
//		}
//		return super.getInt(fieldName);
//	}
//	
//	@Override
//	public double getDouble(String fieldName) {
//		if (fieldName.equals("lenfact")) {
//			return lenfact;
//		}
//		return super.getDouble(fieldName);
//	}
//	
//	@Override
//	public __ptr__ getPtr(String fieldName) {
//		if (fieldName.equals("gen")) {
//			return gen;
//		}
//		return super.getPtr(fieldName);
//	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct arrowtype_t",
	// "{",
	// "int type",
	// "double lenfact",
	// "void (*gen) (GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)",
	// "}",
	// "arrowtype_t");
}

// typedef struct arrowtype_t {
// int type;
// double lenfact; /* ratio of length of this arrow type to standard arrow */
// void (*gen) (GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag); /* generator function for
// type */
// } arrowtype_t;