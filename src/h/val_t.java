/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the Eclipse Public License.
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
 */
package h;
import java.util.Arrays;
import java.util.List;

import smetana.core.__ptr__;

//2 ay78pm8zwkesyc85l5d5hpf1p

public interface val_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef union s",
"{",
"Agnode_t  *n",
"Agraph_t  *subg",
"Agedge_t  *e",
"Agsym_t   *asym",
"char   *name",
"struct item_s *list",
"}",
"val_t");
}

// typedef union s {					/* possible items in generic list */
// 		Agnode_t		*n;
// 		Agraph_t		*subg;
// 		Agedge_t		*e;
// 		Agsym_t			*asym;	/* bound attribute */
// 		char			*name;	/* unbound attribute */
// 		struct item_s	*list;	/* list-of-lists (for edgestmt) */
// } val_t;