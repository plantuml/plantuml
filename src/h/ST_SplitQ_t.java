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

import smetana.core.UnsupportedArrayOfStruct;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__struct__;
import smetana.core.amiga.StarStruct;

public class ST_SplitQ_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	// typedef struct split_q_s {
	// struct Branch BranchBuf[64 + 1];
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

	public final ST_Rect_t CoverSplit = new ST_Rect_t(this);
	public int CoverSplitArea;

	public final ST_PartitionVars Partitions[] = new ST_PartitionVars[] { new ST_PartitionVars() };

	// struct PartitionVars Partitions[1];
	// } SplitQ_t;

	public ST_SplitQ_t() {
		this(null);
	}

	@Override
	public ST_Rect_t castTo(Class dest) {
		if (dest == ST_Rect_t.class) {
			return CoverSplit;
		}
		throw new UnsupportedOperationException();
	}

	public ST_SplitQ_t(StarStruct parent) {
		this.parent = parent;
	}

	class ArrayOfSixtyFive extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfSixtyFive(int pos) {
			this.pos = pos;
		}

		public ArrayOfSixtyFive plus(int delta) {
			return new ArrayOfSixtyFive(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return BranchBuf[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			BranchBuf[pos].copyDataFrom(value);
		}

	}

	class ArrayOfOne extends UnsupportedArrayOfStruct {

		final private int pos;

		public ArrayOfOne(int pos) {
			this.pos = pos;
		}

		public ArrayOfOne plus(int delta) {
			return new ArrayOfOne(pos + delta);
		}

		@Override
		public __struct__ getStruct() {
			return Partitions[pos];
		}

		@Override
		public void setStruct(__struct__ value) {
			Partitions[pos].copyDataFrom(value);
		}

	}

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("CoverSplitArea")) {
			this.CoverSplitArea = data;
			return;
		}
		super.setInt(fieldName, data);
	}

	@Override
	public void setStruct(String fieldName, __struct__ newData) {
		if (fieldName.equals("CoverSplit")) {
			this.CoverSplit.copyDataFrom(newData);
			return;
		}
		super.setStruct(fieldName, newData);
	}

	// typedef struct split_q_s {
	// struct Branch BranchBuf[64 + 1];
	// struct Rect CoverSplit;
	// unsigned int CoverSplitArea;
	// struct PartitionVars Partitions[1];
	// } SplitQ_t;
}
