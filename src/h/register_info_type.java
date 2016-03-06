/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.util.Arrays;
import java.util.List;

import smetana.core.__ptr__;

//2 32aq50hgdk3miqrwc4915dj1o

public interface register_info_type extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef union",
"{",
"fail_stack_elt_t word",
"struct",
"{",
"unsigned match_null_string_p : 2",
"unsigned is_active : 1",
"unsigned matched_something : 1",
"unsigned ever_matched_something : 1",
"}",
"bits",
"}",
"register_info_type");
}

// typedef union
// {
//     fail_stack_elt_t word;
//     struct
//     {
//         /* This field is one if this group can match the empty string,
//            zero if not.  If not yet determined,  `MATCH_NULL_UNSET_VALUE'.  */
// 
//         unsigned match_null_string_p : 2;
//         unsigned is_active : 1;
//         unsigned matched_something : 1;
//         unsigned ever_matched_something : 1;
//     } bits;
// } register_info_type;