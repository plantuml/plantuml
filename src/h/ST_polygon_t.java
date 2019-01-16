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

public class ST_polygon_t extends UnsupportedStructAndPtr implements HardcodedStruct {

	public int regular;
	public int peripheries;
	public int sides;
	public double orientation;
	public double distortion;
	public double skew;
	public int option;
	public ST_pointf.Array vertices;

	// "pointf *vertices",

	public ST_polygon_t() {
		this(null);
	}

	public ST_polygon_t(StarStruct parent) {
	}



	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("regular")) {
			this.regular = data;
			return;
		}
		if (fieldName.equals("peripheries")) {
			this.peripheries = data;
			return;
		}
		if (fieldName.equals("sides")) {
			this.sides = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("orientation")) {
			this.orientation = data;
			return;
		}
		if (fieldName.equals("distortion")) {
			this.distortion = data;
			return;
		}
		if (fieldName.equals("skew")) {
			this.skew = data;
			return;
		}
		super.setDouble(fieldName, data);
	}


	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct polygon_t",
	// "{",
	// "int regular",
	// "int peripheries",
	// "int sides",
	// "double orientation",
	// "double distortion",
	// "double skew",
	// "int option",
	// "pointf *vertices",
	// "}",
	// "polygon_t");
}

// typedef struct polygon_t { /* mutable shape information for a node */
// int regular; /* true for symmetric shapes */
// int peripheries; /* number of periphery lines */
// int sides; /* number of sides */
// double orientation; /* orientation of shape (+ve degrees) */
// double distortion; /* distortion factor - as in trapezium */
// double skew; /* skew factor - as in parallelogram */
// int option; /* ROUNDED, DIAGONAL corners, etc. */
// pointf *vertices; /* array of vertex points */
// } polygon_t;