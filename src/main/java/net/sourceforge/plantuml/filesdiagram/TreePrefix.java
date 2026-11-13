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
package net.sourceforge.plantuml.filesdiagram;

/**
 * The tree drawing in front of an entry name: blanks, vertical bars
 * ({@code │ ┃ ¦ |}) and at most one branch ({@code ├── └── ┣━━ ┗━━ +--- \---
 * |-- `--}).
 */
final class TreePrefix {

	private final int column;
	private final boolean folder;

	private TreePrefix(int column, boolean folder) {
		this.column = column;
		this.folder = folder;
	}

	static boolean isBar(char c) {
		return c == '│' || c == '┃' || c == '¦' || c == '|' || c == '║' || c == '┆'
				|| c == '┊';
	}

	static boolean isBranch(char c) {
		return c == '├' || c == '└' || c == '┣' || c == '┗' || c == '╠' || c == '╚'
				|| c == '┠' || c == '┖' || c == '+' || c == '\\' || c == '`' || c == '|';
	}

	static boolean isDash(char c) {
		return c == '─' || c == '━' || c == '═' || c == '-';
	}

	/**
	 * @param line a line with tabs already expanded
	 */
	static TreePrefix parse(String line) {
		int i = 0;
		while (i < line.length()) {
			final char c = line.charAt(i);
			if (c == ' ') {
				i++;
				continue;
			}
			if (isBranch(c)) {
				int dashes = 0;
				while (i + 1 + dashes < line.length() && isDash(line.charAt(i + 1 + dashes)))
					dashes++;
				if (dashes > 0) {
					i += 1 + dashes;
					// Windows "tree" only draws a branch in front of folders, and glues the
					// name to it: "+---bin" or "├───bin". GNU tree puts a blank there,
					// in front of files as well as folders: "├── bin".
					final boolean windows = dashes == 3 && i < line.length() && line.charAt(i) != ' ';
					while (i < line.length() && line.charAt(i) == ' ')
						i++;
					return new TreePrefix(i, windows);
				}
			}
			if (isBar(c)) {
				i++;
				continue;
			}
			break;
		}
		return new TreePrefix(i, false);
	}

	public int getColumn() {
		return column;
	}

	public boolean isFolder() {
		return folder;
	}

}
