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
package net.sourceforge.plantuml.bpm;


public class CleanerMoveBlock implements GridCleaner {

	public boolean clean(Grid grid) {
		// System.err.println("CleanerMoveBlock");
		for (Line line : grid.lines().toList()) {
			tryGrid(grid, line);
		}
		return false;
	}

	private void tryGrid(Grid grid, Line line) {
		// System.err.println("TRYING LINE " + line);
		for (Col col1 : grid.cols().toList()) {
			final Placeable cell1 = grid.getCell(line, col1).getData();
			if (cell1 instanceof ConnectorPuzzleEmpty == false) {
				continue;
			}
			final ConnectorPuzzleEmpty puzzle1 = (ConnectorPuzzleEmpty) cell1;
			if (puzzle1.checkDirections("NS") == false) {
				continue;
			}
			final Navigator<Col> it2 = grid.cols().navigator(col1);
			int cpt = 0;
			while (true) {
				final Col col2 = it2.next();
				cpt++;
				if (col2 == null) {
					break;
				}
				if (col1 == col2) {
					continue;
				}
				final Placeable cell2 = grid.getCell(line, col2).getData();
				if (cell2 == null) {
					continue;
				}
				if (cell2 instanceof ConnectorPuzzleEmpty == false) {
					break;
				}
				final ConnectorPuzzleEmpty puzzle2 = (ConnectorPuzzleEmpty) cell2;
				if (puzzle2.checkDirections("NS") == false) {
					continue;
				}
				if (cpt > 1) {
					tryBridge(line, col1, col2);
				}
				break;
			}
		}

	}

	private void tryBridge(Line line, Col col1, final Col col2) {
		// System.err.println("LINE=" + line + " " + col1 + " " + col2 + " ");
	}
}
