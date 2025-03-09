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

import smetana.core.CArrayOfStar;
import smetana.core.CString;
import smetana.core.UnsupportedStarStruct;
import smetana.core.__struct__;

final public class ST_field_t extends UnsupportedStarStruct implements SHAPE_INFO {

	public final ST_pointf size = new ST_pointf(); /* its dimension */
	public final ST_boxf b = new ST_boxf(); /* its placement in node's coordinates */
	public int n_flds;
	public ST_textlabel_t lp; /* n_flds == 0 */
	// struct field_t **fld; /* n_flds > 0 */
	public CArrayOfStar<ST_field_t> fld;
	public CString id; /* user's identifier */
	public boolean LR; /* if box list is horizontal (left to right) */
	public int sides; /* sides of node exposed to field */

	@Override
	public String toString() {
		return id + " sides=" + sides + " n_flds=" + n_flds + " lp=" + lp + " fld=" + fld;
	}

	@Override
	public ST_field_t castTo(Class dest) {
		if (dest == ST_field_t.class) {
			return this;
		}
		return (ST_field_t) super.castTo(dest);
	}

	@Override
	public void ___(__struct__ other) {
		ST_field_t other2 = (ST_field_t) other;
		this.size.___(other2.size);
		this.b.___(other2.b);
		this.n_flds = other2.n_flds;
		this.lp = other2.lp;
		this.fld = other2.fld;
		this.id = other2.id;
		this.LR = other2.LR;
		this.sides = other2.sides;
	}

}
// typedef struct field_t {
// pointf size;		/* its dimension */
// boxf b;			/* its placement in node's coordinates */
// int n_flds;
// textlabel_t *lp;	/* n_flds == 0 */
// struct field_t **fld;	/* n_flds > 0 */
// char *id;		/* user's identifier */
// unsigned char LR;	/* if box list is horizontal (left to right) */
// unsigned char sides;    /* sides of node exposed to field */
// } field_t;
