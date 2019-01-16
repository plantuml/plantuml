/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.sudoku;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class PSystemSudokuFactory extends PSystemSingleLineFactory {

	final private static Pattern2 p = MyPattern.cmpile("(?i)^sudoku(?:[%s]+([0-9a-zA-Z]+))?[%s]*$");

	@Override
	protected AbstractPSystem executeLine(String line) {
		final Matcher2 m = p.matcher(line);
		if (m.find() == false) {
			return null;
		}

		if (m.group(1) == null) {
			return new PSystemSudoku(null);
		}
		return new PSystemSudoku(Long.parseLong(StringUtils.goLowerCase(m.group(1)), 36));
	}

}
