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

//2 48gvd3f5a7qs6f6eid493tvyl

public interface format_type extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef enum",
"{",
"FORMAT_PNG_XDOT, FORMAT_GIF_XDOT, FORMAT_JPEG_XDOT, FORMAT_SVG_XDOT, FORMAT_PS_XDOT,     FORMAT_PNG_DOT,  FORMAT_GIF_DOT,  FORMAT_JPEG_DOT,  FORMAT_SVG_DOT,  FORMAT_PS_DOT,     FORMAT_PNG_MAP,  FORMAT_GIF_MAP,  FORMAT_JPEG_MAP,  FORMAT_SVG_MAP,  FORMAT_PS_MAP,     FORMAT_PNG_SVG,  FORMAT_GIF_SVG,  FORMAT_JPEG_SVG,  FORMAT_SVG_SVG,     FORMAT_PNG_FIG,  FORMAT_GIF_FIG,  FORMAT_JPEG_FIG,     FORMAT_PNG_VRML, FORMAT_GIF_VRML, FORMAT_JPEG_VRML,     FORMAT_PS_PS, FORMAT_PSLIB_PS,      FORMAT_PNG_VML, FORMAT_GIF_VML, FORMAT_JPEG_VML,      FORMAT_GIF_TK,",
"}",
"format_type");
}

// typedef enum {
//     FORMAT_PNG_XDOT, FORMAT_GIF_XDOT, FORMAT_JPEG_XDOT, FORMAT_SVG_XDOT, FORMAT_PS_XDOT,
//     FORMAT_PNG_DOT,  FORMAT_GIF_DOT,  FORMAT_JPEG_DOT,  FORMAT_SVG_DOT,  FORMAT_PS_DOT,
//     FORMAT_PNG_MAP,  FORMAT_GIF_MAP,  FORMAT_JPEG_MAP,  FORMAT_SVG_MAP,  FORMAT_PS_MAP,
//     FORMAT_PNG_SVG,  FORMAT_GIF_SVG,  FORMAT_JPEG_SVG,  FORMAT_SVG_SVG,
//     FORMAT_PNG_FIG,  FORMAT_GIF_FIG,  FORMAT_JPEG_FIG,
//     FORMAT_PNG_VRML, FORMAT_GIF_VRML, FORMAT_JPEG_VRML,
//     FORMAT_PS_PS, FORMAT_PSLIB_PS, 
//     FORMAT_PNG_VML, FORMAT_GIF_VML, FORMAT_JPEG_VML, 
//     FORMAT_GIF_TK,
// } format_type;