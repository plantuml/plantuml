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
 * (C) Copyright 2009-2024, Arnaud Roques
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

final public class CString extends UnsupportedC implements __ptr__ {

	private static int UID = 100;

	// private final Throwable creation = new Throwable();
	private final StringBuilder data;
	private final int currentStart;

	private final int uid;

	public boolean isSameThan(CString other) {
		if (this.data != other.data) {
			throw new UnsupportedOperationException();
		}
		return this.currentStart == other.currentStart;

	}

	public CString(String string) {
		this(null, 0);
		this.data.append(string);
		this.data.append('\0');
	}

	public CString duplicate() {

		// return new CString(new ArrayList<>(this.data2), currentStart);
		return new CString(new StringBuilder(this.data.toString()), currentStart);

	}

	public CString strdup() {
		return duplicate();
	}

	public static CString gmalloc(int nbytes) {
		return new CString(nbytes);
	}

	public CString(int size) {
		this(null, 0);
		for (int i = 0; i < size; i++)
			data.append('\0');

	}

	private CString(StringBuilder data, int currentStart) {
		if (data == null)
			this.data = new StringBuilder();
		else
			this.data = data;
		this.currentStart = currentStart;
		this.uid = UID;
		UID += 2;
	}

	public __ptr__ getTheField(FieldOffset bytes) {
		JUtils.LOG("CString::addVirtualBytes " + bytes);
		JUtils.LOG("AM " + this);
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

	public CString plus_(int pointerMove) {
		return new CString(data, currentStart + pointerMove);
	}

	public int comparePointer(__ptr__ other) {
		final CString this2 = (CString) other;
		if (this.data != this2.data)
			throw new IllegalArgumentException();

		return this.currentStart - this2.currentStart;
	}

	@Override
	public String toString() {
//		final StringBuilder sb = new StringBuilder();
//		for (Character c : getData()) {
//			if (c == '\0') {
//				sb.append("(0)");
//			} else {
//				sb.append(c);
//			}
//		}
		return "CString:" + getData2();
	}

	private String getData2() {
		return data.toString().substring(currentStart);
		// return data2.subList(currentStart, data2.size());
	}

	public String getContent() {
		String res = getData2();
		int x = res.indexOf('\0');
		if (x == -1)
			throw new UnsupportedOperationException();
		return res.substring(0, x);
//		return data.toString().substring(currentStart);
//		final StringBuilder sb = new StringBuilder();
//		for (Character c : getData()) {
//			if (c == '\0') {
//				return sb.toString();
//			} else {
//				sb.append(c);
//			}
//		}
//		throw new UnsupportedOperationException();
	}

	public char charAt(int i) {
		return data.charAt(currentStart + i);
//		if (i >= getData().size()) {
//			throw new UnsupportedOperationException();
//			// return '\0';
//		}
//		return data2.get(currentStart + i);
		// when i<0
		// return data2.subList(currentStart, data2.size()).get(i);
	}

	public char setCharAt(int i, char c) {
		data.setCharAt(currentStart + i, c);
		// getData().set(i, c);
		return c;
	}

	public int length() {
		int len = 0;
		while (true) {
			if (charAt(len) == '\0')
				return len;
			len++;
		}
	}

	public int strcmp(CString other) {
		for (int i = 0; i < data.length() - currentStart; i++) {
			final int diff = this.charAt(i) - other.charAt(i);
			if (this.charAt(i) == '\0' || diff != 0)
				return diff;

		}
		throw new IllegalStateException();
	}

	public int strcmp(CString other, int num) {
		for (int i = 0; i < data.length() - currentStart && i < num; i++) {
			final int diff = this.charAt(i) - other.charAt(i);
			if (this.charAt(i) == '\0' || diff != 0)
				return diff;

		}
		return 0;
	}

	public void copyFrom(CString source, int nb) {
		for (int i = 0; i < source.length() + 1 && i < nb; i++) {
			setCharAt(i, source.charAt(i));
		}
	}

	public CString strchr(char c) {
		for (int i = currentStart; i < data.length(); i++)
			if (data.charAt(i) == c)
				return new CString(data, i);

		return null;
	}

	public boolean isSame(CString other) {
		return getContent().equals(other.getContent());
	}

	public int getUid() {
		return uid;
	}

}
