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
package net.sourceforge.plantuml.graph;

import net.sourceforge.plantuml.geom.LineSegmentInt;

public class KenavoCostComputer implements CostComputer {

	public double getCost(Board board) {
		double result = 0;
		for (ALink link1 : board.getLinks()) {
			for (ALink link2 : board.getLinks()) {
				result += getCost(board, link1, link2);
			}
		}
		return result;
	}

	LineSegmentInt getLineSegment(Board board, ALink link) {
		final ANode n1 = link.getNode1();
		final ANode n2 = link.getNode2();
		return new LineSegmentInt(board.getCol(n1), n1.getRow(), board.getCol(n2), n2.getRow());
	}

	private double getCost(Board board, ALink link1, ALink link2) {
		final LineSegmentInt seg1 = getLineSegment(board, link1);
		final LineSegmentInt seg2 = getLineSegment(board, link2);

		final double len1 = getLength(link1, seg1, board);
		final double len2 = getLength(link2, seg2, board);

		return len1 * len2 * Math.exp(-seg1.getDistance(seg2));
	}

	private double getLength(ALink link, final LineSegmentInt seg, Board board) {
		double coef = 1;
		if (link.getNode1().getRow() == link.getNode2().getRow()
				&& board.getDirection(link) != board.getInitialDirection(link)) {
			coef = 1.1;
		}

		return seg.getLength() * coef;
	}

}
