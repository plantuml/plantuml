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
package net.sourceforge.plantuml.gitlog;

public class CursorPosition {

	private final int x;
	private final int y;
	private final GitTextArea source;

	public CursorPosition(GitTextArea source, int x, int y) {
		this.source = source;
		this.x = x;
		this.y = y;
	}

	private String getCurrentLine() {
		return source.getLine(y);
	}

	@Override
	public String toString() {
		return "(" + (x + 1) + "," + (y + 1) + ")";
	}

	public CursorPosition move(int dx, int dy) {
		return new CursorPosition(source, x + dx, y + dy);
	}

	public boolean matches(String prefix) {
		if (x < 0)
			return false;

		final String line = getCurrentLine();
		if (x > line.length())
			return false;

		return line.substring(x).startsWith(prefix);
	}

	public String getCommentInLine() {
		final String line = getCurrentLine();
		final int x = line.indexOf("*");
		if (x == -1)
			return null;

		final int y = line.indexOf("(", x);
		if (y == -1)
			return null;

		final int z = line.indexOf(")", y);
		if (z == -1)
			return null;

		return line.substring(y + 1, z);
	}

	public static String getCommitNameInLine(String s) {
		final int x = s.indexOf("*");
		if (x == -1)
			return null;

		s = s.replaceAll("[-.*|/\\\\]", "").trim();
		final int space = s.indexOf(" ");
		if (space == -1)
			return s;

		final String name = s.substring(0, space);
		return name;
	}

	public String getCommitDefinition() {
		return getCommitNameInLine(getCurrentLine().substring(x));
	}

	public Commit getCommit() {
		return new Commit(getCommitDefinition(), this);
	}

	public CursorPosition getDownFromHere() {
		return getDownFromInternal(this);
	}

	private static CursorPosition getDownFromInternal(CursorPosition current) {
		while (true) {
			if (current.matches("* "))
				return current;

			if (current.matches("/") && current.move(-2, 0).matches("_|/")) {
				current = current.move(-2, 0);
				continue;
			}
			if (current.matches("_") && current.move(-2, 0).matches("_|_")) {
				current = current.move(-2, 0);
				continue;
			}
			if (current.matches("_") && current.move(-2, 1).matches("/")) {
				current = current.move(-2, 1);
				continue;
			}
			if (current.matches("|") && current.move(0, 1).matches("* ")) {
				current = current.move(0, 1);
				continue;
			}
			if (current.matches("|") && current.move(0, 1).matches("|")) {
				current = current.move(0, 1);
				continue;
			}
			if (current.matches("| ") && current.move(0, 1).matches(" \\") && current.move(2, 2).matches("|")) {
				current = current.move(2, 2);
				continue;
			}
			if (current.matches("| ") && current.move(0, 1).matches(" \\") && current.move(2, 2).matches("\\")) {
				current = current.move(2, 2);
				continue;
			}
			if (current.matches("| ") && current.move(-1, 1).matches("/ ") && current.move(-2, 2).matches("* ")) {
				current = current.move(-2, 2);
				continue;
			}
			if (current.matches("| ") && current.move(-3, 1).matches("_|/ ")) {
				current = current.move(-3, 1);
				continue;
			}
			if (current.matches("| ") && current.move(-1, 1).matches("/ ") && current.move(-2, 2).matches("| ")) {
				current = current.move(-2, 2);
				continue;
			}
			if (current.matches("| ") && current.move(-3, 1).matches("_|/")) {
				current = current.move(-3, 1);
				continue;
			}
			if (current.matches("| ") && current.move(0, 1).matches(" \\") && current.move(1, 2).matches("/ ")) {
				current = current.move(1, 2);
				continue;
			}
			if (current.matches("| ") && current.move(0, 1).matches(" \\") && current.move(2, 2).matches("* ")) {
				current = current.move(2, 2);
				continue;
			}
			if (current.matches("\\ ") && current.move(1, 1).matches("* ")) {
				current = current.move(1, 1);
				continue;
			}
			if (current.matches(" \\")) {
				current = current.move(2, 1);
				continue;
			}
			if (current.matches("\\ ") && current.move(0, 1).matches("/ ")) {
				current = current.move(0, 1);
				continue;
			}
			if (current.matches("\\ ") && current.move(1, 1).matches("\\ ")) {
				current = current.move(1, 1);
				continue;
			}
			if (current.matches("\\ ") && current.move(1, 1).matches("|")) {
				current = current.move(1, 1);
				continue;
			}
			if (current.matches("/ ") && current.move(-1, 1).matches("/ ")) {
				current = current.move(-1, 1);
				continue;
			}
			if (current.matches("/ ") && current.move(-2, 1).matches("/| ")) {
				current = current.move(-2, 1);
				continue;
			}
			if (current.matches("/") && current.move(-1, 1).matches("* ")) {
				current = current.move(-1, 1);
				continue;
			}
			if (current.matches("/") && current.move(-1, 1).matches("|")) {
				current = current.move(-1, 1);
				continue;
			}
			System.err.println("this=" + current);
			throw new UnsupportedOperationException(current.toString());
		}
	}

}
