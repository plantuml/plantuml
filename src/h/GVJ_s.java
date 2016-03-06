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

//2 dg8cqsmppn0zl04sycueci9yw

public interface GVJ_s extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"struct GVJ_s",
"{",
"GVC_t *gvc",
"GVJ_t *next",
"GVJ_t *next_active",
"GVCOMMON_t *common",
"obj_state_t *obj",
"char *input_filename",
"int graph_index",
"const char *layout_type",
"const char *output_filename",
"FILE *output_file",
"char *output_data",
"unsigned int output_data_allocated",
"unsigned int output_data_position",
"const char *output_langname",
"int output_lang",
"gvplugin_active_render_t render",
"gvplugin_active_device_t device",
"gvplugin_active_loadimage_t loadimage",
"gvdevice_callbacks_t *callbacks",
"pointf device_dpi",
"boolean device_sets_dpi",
"void *display",
"int screen",
"void *context",
"boolean external_context",
"char *imagedata",
"int flags",
"int numLayers",
"int layerNum",
"point  pagesArraySize",
"point pagesArrayFirst",
"point pagesArrayMajor",
"point pagesArrayMinor",
"point pagesArrayElem",
"int numPages",
"boxf    bb",
"pointf  pad",
"boxf    clip",
"boxf pageBox",
"pointf pageSize",
"pointf  focus",
"double  zoom",
"int rotation",
"pointf  view",
"boxf canvasBox",
"pointf  margin",
"pointf dpi",
"unsigned int width",
"unsigned int height",
"box     pageBoundingBox",
"box     boundingBox",
"pointf  scale",
"pointf  translation",
"pointf  devscale",
"boolean fit_mode,   needs_refresh,   click,   has_grown,   has_been_rendered",
"unsigned char button",
"pointf pointer",
"pointf oldpointer",
"void *current_obj",
"void *selected_obj",
"char *active_tooltip",
"char *selected_href",
"gv_argvlist_t selected_obj_type_name",
"gv_argvlist_t selected_obj_attributes",
"void *window",
"gvevent_key_binding_t *keybindings",
"int numkeys",
"void *keycodes",
"}");
}

// struct GVJ_s {
// 	GVC_t *gvc;		/* parent gvc */
// 	GVJ_t *next;		/* linked list of jobs */
// 	GVJ_t *next_active;	/* linked list of active jobs (e.g. multiple windows) */
// 
// 	GVCOMMON_t *common;
// 
// 	obj_state_t *obj;	/* objects can be nested (at least clusters can)
// 					so keep object state on a stack */
// 	char *input_filename;
// 	int graph_index;
// 
// 	const char *layout_type;
// 
// 	const char *output_filename;
// 	FILE *output_file;
// 	char *output_data;
// 	unsigned int output_data_allocated;
// 	unsigned int output_data_position;
// 
// 	const char *output_langname;
// 	int output_lang;
// 
// 	gvplugin_active_render_t render;
// 	gvplugin_active_device_t device;
// 	gvplugin_active_loadimage_t loadimage;
// 	gvdevice_callbacks_t *callbacks;
// 	pointf device_dpi;
// 	boolean device_sets_dpi;
// 
// 	void *display;
// 	int screen;
// 
// 	void *context;		/* gd or cairo surface */
// 	boolean external_context;	/* context belongs to caller */
// 	char *imagedata;	/* location of imagedata */
// 
//         int flags;		/* emit_graph flags */
// 
// 	int numLayers;		/* number of layers */
// 	int layerNum;		/* current layer - 1 based*/
// 
// 	point 	pagesArraySize; /* 2D size of page array */
// 	point	pagesArrayFirst;/* 2D starting corner in */
// 	point	pagesArrayMajor;/* 2D major increment */
// 	point	pagesArrayMinor;/* 2D minor increment */
// 	point	pagesArrayElem; /* 2D coord of current page - 0,0 based */
//         int	numPages;	/* number of pages */
// 
// 	boxf    bb;		/* graph bb with padding - graph units */
// 	pointf  pad;		/* padding around bb - graph units */
// 	boxf    clip;		/* clip region in graph units */
// 	boxf	pageBox;	/* current page in graph units */
// 	pointf	pageSize;	/* page size in graph units */
// 	pointf  focus;		/* viewport focus - graph units */
// 
// 	double  zoom;		/* viewport zoom factor (points per graph unit) */
// 	int	rotation;	/* viewport rotation (degrees)  0=portrait, 90=landscape */
// 
// 	pointf  view;		/* viewport size - points */
// 	boxf	canvasBox;	/* viewport area - points */
//         pointf  margin;		/* job-specific margin - points */
// 
// 	pointf	dpi;		/* device resolution device-units-per-inch */
// 
//         unsigned int width;     /* device width - device units */
//         unsigned int height;    /* device height - device units */
// 	box     pageBoundingBox;/* rotated boundingBox - device units */
// 	box     boundingBox;    /* cumulative boundingBox over all pages - device units */
// 
// 	pointf  scale;		/* composite device to graph units (zoom and dpi) */
// 	pointf  translation;    /* composite translation */
// 	pointf  devscale;	/* composite device to points: dpi, y_goes_down */
// 
// 	boolean	fit_mode,
// 		needs_refresh,
// 		click,
// 		has_grown,
// 		has_been_rendered;
// 
// 	unsigned char button;   /* active button */
// 	pointf pointer;		/* pointer position in device units */
// 	pointf oldpointer;	/* old pointer position in device units */
// 
// 	void *current_obj;      /* graph object that pointer is in currently */
// 
// 	void *selected_obj;      /* graph object that has been selected */
// 					/* (e.g. button 1 clicked on current obj) */
// 	char *active_tooltip;		/* tooltip of active object - or NULL */
// 	char *selected_href;		/* href of selected object - or NULL */
// 	gv_argvlist_t selected_obj_type_name; /* (e.g. "edge" "node3" "e" "->" "node5" "") */
// 	gv_argvlist_t selected_obj_attributes; /* attribute triplets: name, value, type */
// 				/* e.g. "color", "red", GVATTR_COLOR,
// 					"style", "filled", GVATTR_BOOL, */
// 
// 	void *window;		/* display-specific data for gvrender plugin */
// 
//         /* keybindings for keyboard events */
// 	gvevent_key_binding_t *keybindings;
// 	int numkeys;
// 	void *keycodes;
//     };