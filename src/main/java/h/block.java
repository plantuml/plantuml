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

//2 aeiesb8xknt6qv58gj7tdny3l

public interface block extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct block",
"{",
"Agnode_t *child",
"block_t *next",
"Agraph_t *sub_graph",
"double radius",
"double rad0",
"nodelist_t *circle_list",
"blocklist_t children",
"double parent_pos",
"int flags",
"}");
}

// struct block {
// 	Agnode_t *child;	/* if non-null, points to node in parent block */
// 	block_t *next;		/* sibling block */
// 	Agraph_t *sub_graph;	/* nodes and edges in this block */
// 	double radius;		/* radius of block and subblocks */
// 	double rad0;		/* radius of block */
// 	nodelist_t *circle_list;	/* ordered list of nodes in block */
// 	blocklist_t children;	/* child blocks */
// 	double parent_pos;	/* if block has 1 node, angle to place parent */
// 	int flags;
//     };