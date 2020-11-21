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

import smetana.core.HardcodedStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.__struct__;
import smetana.core.amiga.Area;
import smetana.core.amiga.StarStruct;

public class ST_pointf extends UnsupportedStructAndPtr implements HardcodedStruct {
	
	public static ST_pointf pointfof(double x, double y) {
		final ST_pointf result = new ST_pointf();
		result.x = x;
		result.y = y;
		return result;
	}
	
	public static ST_pointf add_pointf(final ST_pointf p, final ST_pointf q) {
		final ST_pointf result = new ST_pointf();
		result.x = p.x + q.x;
		result.y = p.y + q.y;
		return result;
	}



	public double x;
	public double y;
	
	@Override
	public boolean isSameThan(StarStruct other) {
		return this==(ST_pointf)other;
	}


	public ST_pointf() {
		this(null);
	}

	
	@Override
	public ST_pointf copy() {
		final ST_pointf result = new ST_pointf();
		result.x = this.x;
		result.y = this.y;
		return result;
	}

	@Override
	public void setStruct(__struct__ value) {
		final ST_pointf other2 = (ST_pointf) value;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void copyDataFrom(__ptr__ value) {
		final ST_pointf other2 = (ST_pointf) value;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public ST_pointf getStruct() {
		return this;
	}

	@Override
	public void memcopyFrom(Area source) {
		final ST_pointf other2 = (ST_pointf) source;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void ___(__struct__ other) {
		final ST_pointf other2 = (ST_pointf) other;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void ____(__ptr__ other) {

		if (other instanceof ST_pointf) {
			ST_pointf other2 = (ST_pointf) other;
			this.x = other2.x;
			this.y = other2.y;
			return;
		}
		System.err.println("other=" + other.getClass());
		System.err.println("other=" + other.getPtr().getClass());
		super.____(other);
	}

	@Override
	public void copyDataFrom(__struct__ other) {
		final ST_pointf other2 = (ST_pointf) other;
		this.x = other2.x;
		this.y = other2.y;
	}

	@Override
	public void setDouble(String fieldName, double data) {
		if (fieldName.equals("x")) {
			this.x = data;
			return;
		}
		if (fieldName.equals("y")) {
			this.y = data;
			return;
		}
		super.setDouble(fieldName, data);
	}

	public ST_pointf(StarStruct parent) {
	}

	// public interface ST_pointf extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct pointf_s",
	// "{",
	// "double x, y",
	// "}",
	// "pointf");
}

// typedef struct pointf_s { double x, y; } pointf;