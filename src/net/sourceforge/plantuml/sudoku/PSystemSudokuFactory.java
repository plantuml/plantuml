/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6341 $
 *
 */
package net.sourceforge.plantuml.sudoku;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemSudokuFactory implements PSystemBasicFactory {

	private PSystemSudoku system;

	public PSystemSudokuFactory() {
		reset();
	}

	public void reset() {
	}

	public PSystemSudoku getSystem() {
		return system;
	}

	final private static Pattern p = Pattern.compile("(?i)^sudoku(?:\\s+([0-9a-zA-Z]+))?\\s*$");

	public boolean executeLine(String line) {
		final Matcher m = p.matcher(line);
		if (m.find() == false) {
			return false;
		}

		if (m.group(1) == null) {
			system = new PSystemSudoku(null);
		} else {
			system = new PSystemSudoku(Long.parseLong(m.group(1).toLowerCase(), 36));
		}
		return true;

	}
	
	public DiagramType getDiagramType() {
		return DiagramType.UML;
	}

}
