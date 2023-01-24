
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 *
 */

package net.sourceforge.plantuml.graphml;

public class GraphMLKeyDefinition {

	// Common keys for all elements
	public final static String GML_KEY_ID = "id";
	public final static String GML_KEY_LABEL = "label";
	public final static String GML_KEY_TYPE = "type";
	public final static String GML_KEY_JSON = "json";
	public final static String GML_KEY_ENTITY_TYPE = "entityType";
	public final static String GML_KEY_PUML_ID = "pumlId";
	public final static String GML_KEY_PUML_PATH = "pumlPath";

	// Keys for links
	public final static String GML_KEY_LINK_STYLE = "style";
	public final static String GML_KEY_LINK_SOURCE_DECOR = "sourceDecor";
	public final static String GML_KEY_LINK_TARGET_DECOR = "targetDecor";
	public final static String GML_KEY_LINK_MIDDLE_DECOR = "middleDecor";
	public final static String GML_KEY_LINK_SOURCE_LABEL = "sourceLabel";
	public final static String GML_KEY_LINK_TARGET_LABEL = "targetLabel";
	public final static String GML_KEY_LINK_DIRECTION = "direction";

	// Keys for Members (Fields, Methods)
	public final static String GML_KEY_MEMBER_STATIC = "static";
	public final static String GML_KEY_MEMBER_ABSTRACT = "abstract";
	public final static String GML_KEY_MEMBER_VISIBILITY = "visibility";

	// Keys for Edge Attributes in GRAPHML
	public final static String GML_KEY_EDGE_TYPE = "edgeType";

	// Keys for Diagram Attributes
	public final static String GML_KEY_DIAG_TITLE = "title";
	public final static String GML_KEY_DIAG_HEADER = "header";
	public final static String GML_KEY_DIAG_FOOTER = "footer";
	public final static String GML_KEY_DIAG_CAPTION = "caption";
	public final static String GML_KEY_DIAG_TYPE = "diagramType";
	public final static String GML_KEY_DIAG_SOURCE_FILE = "sourceFile";


}
