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
 *
 */

/**
 * Provides classes used to compose regex partials.
 * 
 * <p>
 * PlantUML parses text using regular expressions. To aid in readability, these
 * are sepecified as trees of {@link RegexComposed} branches and
 * {@link RegexLeaf} leaves.
 * 
 * <p>
 * Before a {@link RegexComposed} can be matched, it must first have each
 * of its constituent parts concatenated into one large regex string using
 * {@link RegexComposed#getFullSlow}. This string is then transformed by
 * {@link MyPattern#transform} to replace some macros (e.g. %s
 * for whitespace, %q for single quote) and compiled using
 * {@link java.util.regex.Pattern#compile}.
 */
package net.sourceforge.plantuml.regex;
