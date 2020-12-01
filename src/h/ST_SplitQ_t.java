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

import smetana.core.UnsupportedStarStruct;

final public class ST_SplitQ_t extends UnsupportedStarStruct {


	// Sorry guys :-)
	public final ST_Branch_t BranchBuf[] = new ST_Branch_t[] { new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(), new ST_Branch_t(),
			new ST_Branch_t(), new ST_Branch_t() };

	public final ST_Rect_t CoverSplit = new ST_Rect_t();
	public int CoverSplitArea;

	public final ST_PartitionVars Partitions[] = new ST_PartitionVars[] { new ST_PartitionVars() };



	@Override
	public ST_Rect_t castTo(Class dest) {
		if (dest == ST_Rect_t.class) {
			return CoverSplit;
		}
		throw new UnsupportedOperationException();
	}



	// typedef struct split_q_s {
	// struct Branch BranchBuf[64 + 1];
	// struct Rect CoverSplit;
	// unsigned int CoverSplitArea;
	// struct PartitionVars Partitions[1];
	// } SplitQ_t;
}
