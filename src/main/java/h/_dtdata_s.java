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

//2 2ncfjhm10yijctdn20pwnelos

public interface _dtdata_s extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct _dtdata_s",
"{",
"int  type",
"Dtlink_t* here",
"union",
"{",
"Dtlink_t** _htab",
"Dtlink_t* _head",
"}",
"hh",
"int  ntab",
"int  size",
"int  loop",
"int  minp",
"}");
}

// struct _dtdata_s
// {	int		type;	/* type of dictionary			*/
// 	Dtlink_t*	here;	/* finger to last search element	*/
// 	union
// 	{ Dtlink_t**	_htab;	/* hash table				*/
// 	  Dtlink_t*	_head;	/* linked list				*/
// 	} hh;
// 	int		ntab;	/* number of hash slots			*/
// 	int		size;	/* number of objects			*/
// 	int		loop;	/* number of nested loops		*/
// 	int		minp;	/* min path before splay, always even	*/
// 				/* for hash dt, > 0: fixed table size 	*/
// };