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

public class OFFSET {

	private final String field;
	private final int sign;

	public int getSign() {
		return sign;
	}

	public OFFSET(String field) {
		this.field = field;
		this.sign = 1;
	}

	private OFFSET(int sign) {
		this.field = null;
		this.sign = sign;
	}

	@Override
	public String toString() {
		if (field == null)
			return "[" + sign + "]";
		return "[" + field + "]";
	}

	public static OFFSET externalHolder() {
		return new OFFSET(-1);
	}

	public static OFFSET zero() {
		return new OFFSET(0);
	}

	public OFFSET negative() {
		// Just to know when we go negative
		return this;
	}

	public String getField() {
		return field;
	}

}
