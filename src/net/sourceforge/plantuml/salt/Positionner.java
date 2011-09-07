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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.salt.element.Element;

public class Positionner {

	private int row;
	private int col;

	private int maxRow;
	private int maxCol;

	private final Map<Element, Position> positions = new LinkedHashMap<Element, Position>();

	public void add(Terminated<Element> element) {
		positions.put(element.getElement(), new Position(row, col));
		updateMax();
		final Terminator terminator = element.getTerminator();
		if (terminator == Terminator.NEWCOL) {
			col++;
		} else {
			row++;
			col = 0;
		}
	}

	private void updateMax() {
		if (row > maxRow) {
			maxRow = row;
		}
		if (col > maxCol) {
			maxCol = col;
		}
	}

	public Map<Element, Position> getAll() {
		return Collections.unmodifiableMap(positions);
	}

	public final int getNbRows() {
		return maxRow + 1;
	}

	public final int getNbCols() {
		return maxCol + 1;
	}

}
