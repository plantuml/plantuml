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
 * This translation is distributed under the same Licence as the original C program.
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

package smetana.core;

import h.ST_refstr_t;

import java.util.ArrayList;
import java.util.List;

import smetana.core.amiga.Area;

public class CString extends UnsupportedC implements __ptr__, Area {

	private static int UID = 100;

	private final Throwable creation = new Throwable();
	private final List<Character> data2;
	private final int currentStart;

	private final int uid;

	public boolean isSameThan(CString other) {
		if (this.data2 != other.data2) {
			throw new UnsupportedOperationException();
		}
		return this.currentStart == other.currentStart;

	}

	public void memcopyFrom(Area source) {
		final CString other = (CString) source;
		this.data2.clear();
		this.data2.addAll(other.data2);
	}

	public CString(String string) {
		this(new ArrayList<Character>(), 0);
		for (int i = 0; i < string.length(); i++) {
			data2.add(string.charAt(i));
		}
		data2.add('\0');
	}

	public CString duplicate() {
		// return this;

		return new CString(new ArrayList<Character>(this.data2), currentStart);

		// final CString result = new CString(this.data.size());
		// for (int i = 0; i < result.data.size(); i++) {
		// result.data.set(i, this.data.get(i));
		// }
		// return result;
	}

	public CString strdup() {
		return duplicate();
	}

	public CString(int size) {
		this(new ArrayList<Character>(), 0);
		for (int i = 0; i < size; i++) {
			data2.add('\0');
		}
	}

	private CString(List<Character> data2, int currentStart) {
		this.data2 = data2;
		this.currentStart = currentStart;
		this.uid = UID;
		UID += 2;
		creation.fillInStackTrace();
	}

	public __ptr__ addVirtualBytes(int bytes) {
		JUtils.LOG("CString::addVirtualBytes " + bytes);
		JUtils.LOG("AM " + this);
		// if (bytes < 0) {
		// OFFSET offset = OFFSET.fromInt(-bytes);
		// JUtils.LOG("offset=" + offset);
		// for (__ptr__ f : fathers) {
		// JUtils.LOG("f=" + f);
		// if (f instanceof StarStruct && ((StarStruct) f).getRealClass() == offset.getTheClass()) {
		// JUtils.LOG("FOUND1!!");
		// if (f.getPtr(offset.getField()).equals(this)) {
		// JUtils.LOG("FOUND2!!");
		// return f;
		// }
		// }
		// }
		// } else {
		// JUtils.LOG("father=" + fathers);
		// JUtils.LOG("CString created on the following place:");
		// creation.printStackTrace();
		// }
		throw new UnsupportedOperationException();
	}

	private ST_refstr_t parent;

	public ST_refstr_t getParent() {
		if (parent != null) {
			return parent;
		}
		throw new UnsupportedOperationException();
	}

	public void setParent(ST_refstr_t struct) {
//		if (parent != null && parent != struct) {
//			throw new IllegalStateException();
//		}
		if (struct == null) {
			throw new IllegalStateException();
		}
		this.parent = struct;
	}

	@Override
	public CString plus(int pointerMove) {
		return new CString(data2, currentStart + pointerMove);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (Character c : getData()) {
			if (c == '\0') {
				sb.append("(0)");
			} else {
				sb.append(c);
			}
		}
		return "CString:" + sb;
	}

	private List<Character> getData() {
		return data2.subList(currentStart, data2.size());
	}

	public String getContent() {
		final StringBuilder sb = new StringBuilder();
		for (Character c : getData()) {
			if (c == '\0') {
				return sb.toString();
			} else {
				sb.append(c);
			}
		}
		throw new UnsupportedOperationException();
	}

	public char charAt(int i) {
		if (i >= getData().size()) {
			return '\0';
		}
		return getData().get(i);
	}

	public char setCharAt(int i, char c) {
		getData().set(i, c);
		return c;
	}

	public int length() {
		int len = 0;
		for (Character c : getData()) {
			if (c == '\0') {
				return len;
			}
			len++;
		}
		throw new IllegalStateException();
	}

	public int compareTo(CString other) {
		for (int i = 0; i < data2.size() - currentStart; i++) {
			final int diff = this.charAt(i) - other.charAt(i);
			if (this.charAt(i) == '\0' || diff != 0) {
				return diff;
			}
		}
		throw new IllegalStateException();
	}

	public int compareTo(CString other, int num) {
		for (int i = 0; i < data2.size() - currentStart && i < num; i++) {
			final int diff = this.charAt(i) - other.charAt(i);
			if (this.charAt(i) == '\0' || diff != 0) {
				return diff;
			}
		}
		return 0;
	}

	public void copyFrom(CString source, int nb) {
		for (int i = 0; i < source.length() + 1 && i < nb; i++) {
			setCharAt(i, source.charAt(i));
		}
	}

	public CString strchr(char c) {
		for (int i = currentStart; i < data2.size(); i++) {
			if (data2.get(i).charValue() == c) {
				return new CString(data2, i);
			}
		}
		return null;
	}

	public boolean isSame(CString other) {
		return getContent().equals(other.getContent());
	}

	public int getUid() {
		return uid;
	}

}
