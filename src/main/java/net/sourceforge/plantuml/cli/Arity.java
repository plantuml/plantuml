/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.cli;

public enum Arity {

	/** Option without any value, e.g. -verbose */
	UNARY_BOOLEAN,

	/** Option without any value that break the usual behaviour, e.g. -help, -testdot */
	UNARY_IMMEDIATE_ACTION,

	/** Option with a key and an optional value, e.g. -ftp or -ftp:8080*/
	UNARY_OPTIONAL_COLON,

	/** Option with an argument or a key/value, e.g. -DSOME_FLAG or -DKEY=VALUE or -D SOME_FLAG or -D KEY=VALUE  */
	UNARY_INLINE_KEY_OR_KEY_VALUE,

	/** Option with a single value, e.g. -graphvizdot "foo.exe" */
	BINARY_NEXT_ARGUMENT_VALUE;
}