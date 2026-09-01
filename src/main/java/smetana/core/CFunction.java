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
 * This translation is distributed under the same License as the original C program.
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

public interface CFunction extends __ptr__ {
	
	public Object exe(Globals zz, Object... args);

	// Fast path for dictionary comparators: no Object[] varargs allocation and
	// no boxed Integer return on the dtsearch hot path. Non-comparators keep
	// the boxing fallback from CFunctionAbstract.
	public int exeCmpInt(Globals zz, Object a0, Object a1, Object a2, Object a3);

	// Fast path for dictionary search functions: no Object[] varargs
	// allocation and no boxing of the DT_* type flag on every dtsearch/
	// dtinsert/dtnext call. Overridden by dttree__c.dttree.
	public Object exeSearch(Globals zz, Object a0, Object a1, int type);

	public String getName();


}
