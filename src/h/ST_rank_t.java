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

import smetana.core.CArrayOfStar;
import smetana.core.UnsupportedStarStruct;
import smetana.core.__struct__;

//typedef struct rank_t {
//int n; /* number of nodes in this rank */
//node_t **v; /* ordered list of nodes in rank */
//int an; /* globally allocated number of nodes */
//node_t **av; /* allocated list of nodes in rank */
//double ht1, ht2; /* height below/above centerline */
//double pht1, pht2; /* as above, but only primitive nodes */
//boolean candidate; /* for transpose () */
//boolean valid;
//int cache_nc; /* caches number of crossings */
//adjmatrix_t *flat;
//} rank_t;

final public class ST_rank_t extends UnsupportedStarStruct {

	public int n;
	public CArrayOfStar<ST_Agnode_s> v;
	public int an;
	public CArrayOfStar<ST_Agnode_s> av;

	public double ht1, ht2;
	public double pht1, pht2;
	public boolean candidate;
	public int valid;

	public int cache_nc;
	public ST_adjmatrix_t flat;

	@Override
	public String toString() {
		return "RANK n=" + n + " v=" + v + " an=" + an + " av=" + av;
	}

	@Override
	public void ___(__struct__ other) {
		ST_rank_t this2 = (ST_rank_t) other;
		this.n = this2.n;
		this.v = this2.v;
		this.an = this2.an;
		this.av = this2.av;
		this.ht1 = this2.ht1;
		this.ht2 = this2.ht2;
		this.pht1 = this2.pht1;
		this.pht2 = this2.pht2;
		this.candidate = this2.candidate;
		this.valid = this2.valid;
		this.cache_nc = this2.cache_nc;
		this.flat = this2.flat;
	}

}
