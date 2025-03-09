/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.yaml.parser;

public enum YamlLineType {
	
	EMPTY_LINE,
	
	
	NO_KEY_ONLY_TEXT,
	
	
	/**
	 * Indicates that no value was provided.
	 */
	KEY_ONLY,
	
	/**
	 * Indicates a simple regular scalar value.
	 */
	KEY_AND_VALUE,
	
	/**
	 * Indicates an inline list (e.g. ["a", "b", "c"]).
	 */
	KEY_AND_FLOW_SEQUENCE,
	
	/**
	 * Indicates a block style scalar value (using the '|' indicator).
	 */
	KEY_AND_BLOCK_STYLE,
	
	/**
	 * Indicates a folded style scalar value (using the '>' indicator).
	 */
	KEY_AND_FOLDED_STYLE,
	
	/**
	 * Indicates a plain element in a list (e.g.  - red )
	 */
	PLAIN_ELEMENT_LIST,
	
	/**
	 * Indicates a plain dash (e.g.  - )
	 */
	PLAIN_DASH,
	
}

