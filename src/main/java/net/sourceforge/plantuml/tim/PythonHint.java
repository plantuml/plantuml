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
package net.sourceforge.plantuml.tim;

import org.luaj.vm2.python.Interp;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;

/**
 * The hint added to a legacy error on a line that would be a Python block
 * opener if it ended with a colon (section 11.4 of PYTHON_PREPROCESSOR.md).
 * <code>!if</code>, <code>!while</code> and <code>!else</code> exist in both
 * languages, and a missing colon makes the line legacy; its error is then
 * confusing ("Unknown built-in function len").
 */
final class PythonHint {

	static final String HINT = " (looks like Python, is the ':' missing?)";

	private PythonHint() {
	}

	/** The hint for an error found on this line, or null. Only called when there is an error. */
	static String hint(StringLocated line) {
		final TLineType type = line.getType();
		if (type != TLineType.IF && type != TLineType.WHILE && type != TLineType.ELSE)
			return null;
		final String statement = statement(line.getString());
		if (statement.endsWith(":"))
			return null;
		// With the colon, does it read as Python? An else needs an if before it.
		final String code = type == TLineType.ELSE ? "if x:\n    pass\n" + statement + ":\n    pass\n"
				: statement + ":\n    pass\n";
		try {
			Interp.compile(code, "<hint>");
			return HINT;
		} catch (RuntimeException e) {
			return null;
		}
	}

	/** The text of a "!" line without the bang and the blanks around. */
	private static String statement(String line) {
		final String trimmed = line.trim();
		return trimmed.startsWith("!") ? trimmed.substring(1).trim() : trimmed;
	}
}
