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

import smetana.core.UnsupportedStructAndPtr;
import smetana.core.amiga.StarStruct;

public class ST_aspect_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_aspect_t() {
		this(null);
	}

	public ST_aspect_t(StarStruct parent) {
		this.parent = parent;
	}

	// "typedef struct aspect_t",
	// "{",
	// "double targetAR",
	// "double combiAR",
	public int prevIterations;
	public int curIterations;
	public int nextIter;
	public int nPasses;
	public int badGraph;

	// "}",
	// "aspect_t");

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("nextIter")) {
			this.nextIter = data;
			return;
		}
		if (fieldName.equals("badGraph")) {
			this.badGraph = data;
			return;
		}
		if (fieldName.equals("nPasses")) {
			this.nPasses = data;
			return;
		}
		super.setInt(fieldName, data);
	}

}

// typedef struct aspect_t {
// double targetAR; /* target aspect ratio */
// double combiAR;
// int prevIterations; /* no. of iterations in previous pass */
// int curIterations; /* no. of iterations in current pass */
// int nextIter; /* dynamically adjusted no. of iterations */
// int nPasses; /* bound on no. of top-level passes */
// int badGraph; /* hack: set if graph is disconnected or has
// * clusters. If so, turn off aspect */
// } aspect_t;