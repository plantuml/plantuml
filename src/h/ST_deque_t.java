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
import smetana.core.amiga.StarStruct;

public class ST_deque_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	// ---------------
	public ST_pointnlink_t pnlps[];

	public boolean malloc(int newdqn) {
		this.pnlps = new ST_pointnlink_t[newdqn];
		return true;
	}

	public boolean realloc(int newdqn) {
		if (pnlps.length >= newdqn) {
			return true;
		}
		ST_pointnlink_t pnlps2[] = new ST_pointnlink_t[newdqn];
		for (int i = 0; i < pnlps.length; i++) {
			pnlps2[i] = pnlps[i];
		}
		this.pnlps = pnlps2;
		return true;
	}
	// ---------------

	
	// "pointnlink_t **pnlps",
//	public __ptr__ pnlps;
	public int pnlpn, fpnlpi, lpnlpi, apex;

	public ST_deque_t() {
		this(null);
	}

	public ST_deque_t(StarStruct parent) {
	}

//	@Override
//	public __ptr__ getPtr(String fieldName) {
//		if (fieldName.equals("pnlps")) {
//			return pnlps;
//		}
//		return super.getPtr(fieldName);
//	}
//
//	@Override
//	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
//		if (fieldName.equals("pnlps")) {
//			pnlps = newData;
//			return pnlps;
//		}
//		return super.setPtr(fieldName, newData);
//	}

//	@Override
//	public int getInt(String fieldName) {
//		if (fieldName.equals("pnlpn")) {
//			return pnlpn;
//		}
//		if (fieldName.equals("fpnlpi")) {
//			return fpnlpi;
//		}
//		if (fieldName.equals("lpnlpi")) {
//			return lpnlpi;
//		}
//		if (fieldName.equals("apex")) {
//			return apex;
//		}
//		return super.getInt(fieldName);
//	}

//	@Override
//	public void setInt(String fieldName, int data) {
//		if (fieldName.equals("pnlpn")) {
//			pnlpn = data;
//			return;
//		}
//		if (fieldName.equals("fpnlpi")) {
//			fpnlpi = data;
//			return;
//		}
//		if (fieldName.equals("lpnlpi")) {
//			lpnlpi = data;
//			return;
//		}
//		if (fieldName.equals("apex")) {
//			apex = data;
//			return;
//		}
//		super.setInt(fieldName, data);
//	}

	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct deque_t",
	// "{",
	// "pointnlink_t **pnlps",
	// "int pnlpn, fpnlpi, lpnlpi, apex",
	// "}",
	// "deque_t");
}

// typedef struct deque_t {
// pointnlink_t **pnlps;
// int pnlpn, fpnlpi, lpnlpi, apex;
// } deque_t;