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

//2 39c66ffd2kgahvaaf8e61dspv

public interface cdata extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef struct",
"{",
"union",
"{",
"Agraph_t *g",
"Agnode_t *np",
"}",
"orig",
"int flags",
"node_t *parent",
"block_t *block",
"union",
"{",
"struct",
"{",
"node_t *next",
"int val",
"int low_val",
"}",
"bc",
"node_t *clone",
"struct",
"{",
"node_t *tparent",
"node_t *first",
"node_t *second",
"int fdist",
"int sdist",
"}",
"t",
"struct",
"{",
"int pos",
"double psi",
"}",
"f",
"}",
"u",
"}",
"cdata");
}

// typedef struct {
//     union {			/* Pointer to node/cluster in original graph */
// 	Agraph_t *g;
// 	Agnode_t *np;
//     } orig;
//     int flags;
//     node_t *parent;		/* parent in block-cutpoint traversal (1,2,4) */
//     block_t *block;		/* Block containing node (1,2,3,4) */
//     union {
// 	struct {		/* Pass  1 */
// 	    node_t *next;	/* used for stack */
// 	    int val;
// 	    int low_val;
// 	} bc;
// 	node_t *clone;		/* Cloned node (3a) */
// 	struct {		/* Spanning tree and longest path (3b) */
// 	    node_t *tparent;	/* Parent in tree */
// 	    node_t *first;	/* Leaf on longest path from node */
// 	    node_t *second;	/* Leaf on 2nd longest path from node */
// 	    int fdist;		/* Length of longest path from node */
// 	    int sdist;		/* Length of 2nd longest path from node */
// 	} t;
// 	struct {
// 	    int pos;		/* Index of node in block circle (3c,4) */
// 	    double psi;		/* Offset angle of children (4) */
// 	} f;
//     } u;
// } cdata;