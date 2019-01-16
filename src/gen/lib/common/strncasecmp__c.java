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
package gen.lib.common;
import static smetana.core.Macro.UNSUPPORTED;

public class strncasecmp__c {


//3 6fpqvqq5eso7d44vai4lz77jd
// int strncasecmp(const char *s1, const char *s2, unsigned int n) 
public static Object strncasecmp(Object... arg) {
UNSUPPORTED("41sf831iel4ggk6nxgerc7lrz"); // int strncasecmp(const char *s1, const char *s2, unsigned int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f0os7tzuki1s9mllsml3zu2fd"); //     if (n == 0)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("18ggb7ihy0resf5qhplc7cqol"); //     while ((n-- != 0)
UNSUPPORTED("7tdf84gz2hakxs756j3v0w4iv"); // 	   && (tolower(*(unsigned char *) s1) ==
UNSUPPORTED("co97u0db3a8mz1pp77hkxq0h3"); // 	       tolower(*(unsigned char *) s2))) {
UNSUPPORTED("2jr0cuzm9i39xecgxx0ih0ez4"); // 	if (n == 0 || *s1 == '\0' || *s2 == '\0')
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("2hh1h5gydepd3ut3g43bzn51g"); // 	s1++;
UNSUPPORTED("7x8zh0pm8zj83pbc2d812jz90"); // 	s2++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1jrt0skm3a5djo3vfej4kwffc"); //     return tolower(*(unsigned char *) s1) - tolower(*(unsigned char *) s2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
