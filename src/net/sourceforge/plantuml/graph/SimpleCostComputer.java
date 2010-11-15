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
 * Revision $Revision: 3833 $
 *
 */
package net.sourceforge.plantuml.graph;

public class SimpleCostComputer implements CostComputer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.plantuml.graph.CostComputer#getCost(net.sourceforge.plantuml.graph.Board)
	 */
	public double getCost(Board board) {
		double result = 0;
		for (ALink link : board.getLinks()) {
			final ANode n1 = link.getNode1();
			final ANode n2 = link.getNode2();
			final int x1 = board.getCol(n1);
			final int y1 = n1.getRow();
			final int x2 = board.getCol(n2);
			final int y2 = n2.getRow();
			result += Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

		}
		return result;
	}

}
