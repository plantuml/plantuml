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
package h;

import smetana.core.CString;
import smetana.core.UnsupportedStructAndPtr;
import smetana.core.__ptr__;
import smetana.core.amiga.StarStruct;

public class ST_gvplugin_installed_t extends UnsupportedStructAndPtr {

	private final StarStruct parent;

	public ST_gvplugin_installed_t() {
		this(null);
	}

	public ST_gvplugin_installed_t(StarStruct parent) {
		this.parent = parent;
	}

	public int id;
	public CString type;
	public int quality;

	public ST_gvlayout_engine_s engine;
	public ST_gvlayout_features_t features;

	@Override
	public void setInt(String fieldName, int data) {
		if (fieldName.equals("id")) {
			this.id = data;
			return;
		}
		if (fieldName.equals("quality")) {
			this.quality = data;
			return;
		}
		super.setInt(fieldName, data);
	}


	@Override
	public __ptr__ setPtr(String fieldName, __ptr__ newData) {
		if (fieldName.equals("type")) {
			this.type = (CString) newData;
			return newData;
		}
		if (fieldName.equals("engine")) {
			this.engine = (ST_gvlayout_engine_s) newData;
			return newData;
		}
		if (fieldName.equals("features")) {
			this.features = (ST_gvlayout_features_t) newData;
			return newData;
		}
		return super.setPtr(fieldName, newData);
	}

}

// typedef struct {
// int id; /* an id that is only unique within a package
// of plugins of the same api.
// A renderer-type such as "png" in the cairo package
// has an id that is different from the "ps" type
// in the same package */
// const char *type; /* a string name, such as "png" or "ps" that
// distinguishes different types withing the same
// (renderer in this case) */
// int quality; /* an arbitrary integer used for ordering plugins of
// the same type from different packages */
// void *engine; /* pointer to the jump table for the plugin */
// void *features; /* pointer to the feature description
// void* because type varies by api */
// } gvplugin_installed_t;