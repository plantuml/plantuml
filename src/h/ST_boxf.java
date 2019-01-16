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
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_boxf extends UnsupportedStructAndPtr implements HardcodedStruct {

	public final ST_pointf LL = new ST_pointf(this);
	public final ST_pointf UR = new ST_pointf(this);

	public ST_boxf(StarStruct parent) {
	}

	public ST_boxf() {
	}

	// @Override
	// public void setStruct(__struct__ value) {
	// final ST_boxf other = (ST_boxf) value;
	// this.LL.setStruct(other.LL);
	// this.UR.setStruct(other.UR);
	// }

	public static ST_boxf[] malloc(int nb) {
		final ST_boxf result[] = new ST_boxf[nb];
		for (int i = 0; i < nb; i++) {
			result[i] = new ST_boxf();
		}
		return result;
	}

	@Override
	public __struct__ getStruct() {
		return this;
	}

	@Override
	public void setStruct(__struct__ value) {
		copyDataFrom(value);
	}

	public __struct__ getStructInternal(String fieldName) {
		if (fieldName.equals("LL")) {
			return LL;
		}
		if (fieldName.equals("UR")) {
			return UR;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("LL")) {
			this.LL.setStruct(newData);
			return;
		}
		if (fieldName.equals("UR")) {
			this.UR.setStruct(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	@Override
	public ST_boxf copy() {
		final ST_boxf result = new ST_boxf();
		result.LL.copyDataFrom((__struct__) this.LL);
		result.UR.copyDataFrom((__struct__) this.UR);
		return result;
	}

	@Override
	public void copyDataFrom(__struct__ value) {
		final ST_boxf other = (ST_boxf) value;
		this.LL.setStruct(other.LL);
		this.UR.setStruct(other.UR);
	}

	@Override
	public void ___(__struct__ value) {
		final ST_boxf other = (ST_boxf) value;
		this.LL.setStruct(other.LL);
		this.UR.setStruct(other.UR);
	}



	// public interface ST_boxf extends __ptr__ {
	// public static List<String> DEFINITION = Arrays.asList(
	// "typedef struct",
	// "{",
	// "pointf LL, UR",
	// "}",
	// "boxf");
}

// typedef struct { pointf LL, UR; } boxf;