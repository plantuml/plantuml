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

//2 7uiwhqnseej0oaqiv35vi47k1

public interface _dtdisc_s extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct _dtdisc_s",
"{",
"int  key",
"int  size",
"int  link",
"Dtmake_f makef",
"Dtfree_f freef",
"Dtcompar_f comparf",
"Dthash_f hashf",
"Dtmemory_f memoryf",
"Dtevent_f eventf",
"}");
}

// struct _dtdisc_s
// {	int		key;	/* where the key begins in an object	*/
// 	int		size;	/* key size and type			*/
// 	int		link;	/* offset to Dtlink_t field		*/
// 	Dtmake_f	makef;	/* object constructor			*/
// 	Dtfree_f	freef;	/* object destructor			*/
// 	Dtcompar_f	comparf;/* to compare two objects		*/
// 	Dthash_f	hashf;	/* to compute hash value of an object	*/
// 	Dtmemory_f	memoryf;/* to allocate/free memory		*/
// 	Dtevent_f	eventf;	/* to process events			*/
// };