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
package net.sourceforge.plantuml.utils;

public class CharInspectorImpl implements CharInspector {
	// ::remove file when __HAXE__

	final private BlocLines data;
	final private boolean insertNewlines;
	private int line = 0;
	private int pos = 0;

	CharInspectorImpl(BlocLines input, boolean insertNewlines) {
		this.data = input;
		this.insertNewlines = insertNewlines;
	}

	@Override
	public char peek(int ahead) {
		if (line == -1)
			return 0;
		final String currentLine = getCurrentLine();
		if (pos + ahead >= currentLine.length())
			return '\0';
		return currentLine.charAt(pos + ahead);
	}

	private String getCurrentLine() {
		if (insertNewlines)
			return data.getAt(line).getTrimmed().getString() + "\n";
		return data.getAt(line).getTrimmed().getString();
	}

	@Override
	public void jump() {
		if (line == -1)
			throw new IllegalStateException();
		pos++;
		if (pos >= getCurrentLine().length()) {
			line++;
			pos = 0;
		}
		while (line < data.size() && getCurrentLine().length() == 0)
			line++;
		if (line >= data.size())
			line = -1;
	}
}
