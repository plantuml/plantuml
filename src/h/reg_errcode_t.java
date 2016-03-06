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

//2 ap7c4ii9ux7nlggs68fm76fy6

public interface reg_errcode_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef enum",
"{",
"REG_NOERROR = 0,",
"REG_NOMATCH,",
"REG_BADPAT,",
"REG_ECOLLATE,",
"REG_ECTYPE,",
"REG_EESCAPE,",
"REG_ESUBREG,",
"REG_EBRACK,",
"REG_EPAREN,",
"REG_EBRACE,",
"REG_BADBR,",
"REG_ERANGE,",
"REG_ESPACE,",
"REG_BADRPT,",
"REG_EEND,",
"REG_ESIZE,",
"REG_ERPAREN",
"}",
"reg_errcode_t");
}

// typedef enum
//     {
//         REG_NOERROR = 0,        /* Success.  */
//         REG_NOMATCH,            /* Didn't find a match (for regexec).  */
// 
//         /* POSIX regcomp return error codes.  (In the order listed in the
//            standard.)  */
//         REG_BADPAT,             /* Invalid pattern.  */
//         REG_ECOLLATE,           /* Not implemented.  */
//         REG_ECTYPE,             /* Invalid character class name.  */
//         REG_EESCAPE,            /* Trailing backslash.  */
//         REG_ESUBREG,            /* Invalid back reference.  */
//         REG_EBRACK,             /* Unmatched left bracket.  */
//         REG_EPAREN,             /* Parenthesis imbalance.  */
//         REG_EBRACE,             /* Unmatched \{.  */
//         REG_BADBR,              /* Invalid contents of \{\}.  */
//         REG_ERANGE,             /* Invalid range end.  */
//         REG_ESPACE,             /* Ran out of memory.  */
//         REG_BADRPT,             /* No preceding re for repetition op.  */
// 
//         /* Error codes we've added.  */
//         REG_EEND,               /* Premature end.  */
//         REG_ESIZE,              /* Compiled pattern bigger than 2^16 bytes.  */
//         REG_ERPAREN             /* Unmatched ) or \); not returned from regcomp.  */
//     } reg_errcode_t;