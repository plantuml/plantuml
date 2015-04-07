/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 10298 $
 *
 */
package net.sourceforge.plantuml.sudoku;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class PSystemSudokuFactory extends PSystemSingleLineFactory {

	final private static Pattern p = MyPattern.cmpile("(?i)^sudoku(?:[%s]+([0-9a-zA-Z]+))?[%s]*$");

	@Override
	protected AbstractPSystem executeLine(String line) {
		final Matcher m = p.matcher(line);
		if (m.find() == false) {
			return null;
		}

		if (m.group(1) == null) {
			return new PSystemSudoku(null);
		}
		return new PSystemSudoku(Long.parseLong(StringUtils.goLowerCase(m.group(1)), 36));
	}

}
