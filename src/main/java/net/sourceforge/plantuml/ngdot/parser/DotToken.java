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
package net.sourceforge.plantuml.ngdot.parser;

public final class DotToken {

	/** How an {@link DotTokenType#ID} token was written. */
	public enum IdKind {
		PLAIN, NUMBER, QUOTED, HTML
	}

	private final DotTokenType type;
	private final String text;
	private final IdKind idKind;
	private final int line;
	private final int column;

	DotToken(DotTokenType type, String text, IdKind idKind, int line, int column) {
		this.type = type;
		this.text = text;
		this.idKind = idKind;
		this.line = line;
		this.column = column;
	}

	public DotTokenType getType() {
		return type;
	}

	/**
	 * For an ID, its value (quotes and escapes of a quoted string resolved,
	 * brackets of an HTML string removed). For other tokens, the source text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * How an ID was written, {@code null} for other tokens.
	 */
	public IdKind getIdKind() {
		return idKind;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	/**
	 * The token as it could be shown in an error message.
	 */
	public String describe() {
		if (type == DotTokenType.EOF)
			return "end of text";
		if (idKind == IdKind.QUOTED)
			return "\"" + text + "\"";
		if (idKind == IdKind.HTML)
			return "<" + text + ">";
		return "'" + text + "'";
	}

	@Override
	public String toString() {
		return type + "(" + text + ")@" + line + ":" + column;
	}

}
