
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
package net.sourceforge.plantuml.genericdiagram;

/**
 * The enum represents the types of elements
 * as provided by the language specification
 * check https://plantuml.com/en/sitemap-language-specification
 */
public enum GenericEntityType {
	// default for all not yet realized things
	UNKNOWN,
	// class diagram
	ABSTRACT,
	ABSTRACT_CLASS,
	ANNOTATION,
	CIRCLE,
	CLASS,
	DIAMOND,
	ENTITY,
	ENUM,
	INTERFACE,
	NOTE,
	POINT_FOR_ASSOCIATION,
	// component diagram grouping elements
	NAMESPACE,
	PACKAGE,
	NODE,
	RECTANGLE,
	FOLDER,
	FRAME,
	CLOUD,
	DATABASE,
	// component diagram
	// plantUML maps port to PORTIN
	// this is apparently only an indication for drawing IN on top, OUT on bottom of component
	PORT_IN,
	PORT_OUT,
	// object diagram
	OBJECT,
	// deployment diagram
	ACTOR,
	ACTOR_SLASH,
	AGENT,
	ARTIFACT,
	BOUNDARY,
	CARD,
	// CIRCLE, CLOUD already defined
	COLLECTIONS,
	COMPONENT,
	CONTROL,
	// DATABASE, ENTITY already defined
	FILE,
	// FOLDER, FRAME, already defined
	HEXAGON,
	// INTERFACE already defined
	LABEL,
	// NODE, PACKAGE already defined
	PERSON,
	QUEUE,
	// RECTANGLE alreday defined
	STACK,
	STORAGE,
	USECASE,
	USECASE_SLASH,
	// Additional Elements added to fit the generic model
	// for class members
	FIELD,
	METHOD,
	UNKNOWN_MEMBER,
	// for stereotypes
	STEREOTYPE,
	// for text groups in grouping elements
	LONG_DESCRIPTION,
	// Hackish add entity types for edges and links
	EDGE,
	LINK,
	DIAGRAM,
}
