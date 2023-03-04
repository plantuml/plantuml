/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import smetana.core.CString;
import smetana.core.FieldOffset;
import smetana.core.UnsupportedStarStruct;
import smetana.core.__ptr__;

//2 38c2s12koxcpi2c7vwl72qrsp

final public class ST_Agsym_s extends UnsupportedStarStruct {
	public final ST_dtlink_s link = new ST_dtlink_s(this);
	public CString name; /* attribute's name */
	public CString defval; /* its default value for initialization */

	public int id; /* its index in attr[] */
	public int kind; /* referent object type */
	public int fixed; /* immutable value */
	public int print; /* always print */

	@Override
	public Object getTheField(FieldOffset offset) {
		if (offset == null || offset.getSign() == 0) 
			return this;
		
		if (offset == FieldOffset.name) 
			return name;
		
		if (offset == FieldOffset.link) 
			return link;
		
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameThan(__ptr__ other) {
		ST_Agsym_s other2 = (ST_Agsym_s) other;
		return this == other2;
	}

}

// struct Agsym_s { /* symbol in one of the above dictionaries */
// Dtlink_t link;
// char *name; /* attribute's name */
// char *defval; /* its default value for initialization */
// int id; /* its index in attr[] */
// unsigned char kind; /* referent object type */
// unsigned char fixed; /* immutable value */
// unsigned char print; /* always print */
// };