/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

public enum Symbol {

	LITTERAL, //

	// https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form
	DEFINITION, // =
	CONCATENATION, // ,
	TERMINATION, // ;
	ALTERNATION, // |
	OPTIONAL_OPEN, // [
	OPTIONAL_CLOSE, // ]
	OPTIONAL, //
	REPETITION_SYMBOL, // *
	REPETITION_OPEN, // {
	REPETITION_CLOSE, // }
	REPETITION_MINUS_CLOSE, // }
	REPETITION_ZERO_OR_MORE, //
	REPETITION_ONE_OR_MORE, //
	GROUPING_OPEN, // (
	GROUPING_CLOSE, // )
	TERMINAL_STRING1, // " "
	TERMINAL_STRING2, // ' '
	COMMENT_TOKEN, // (* *)
	COMMENT_BELOW, // (* *)
	COMMENT_ABOVE, // (* *)
	SPECIAL_SEQUENCE, // ? ?
	EXCEPTION; // -

	public int getPriority() {
		switch (this) {
		case REPETITION_SYMBOL:
			return 3;
		case CONCATENATION:
			return 2;
		case ALTERNATION:
			return 1;
		}
		throw new UnsupportedOperationException();
	}

	boolean isOperator() {
		return this == CONCATENATION || this == ALTERNATION || this == REPETITION_SYMBOL;
	}

	boolean isFunction() {
		return this == OPTIONAL || this == REPETITION_ZERO_OR_MORE;
	}

}
