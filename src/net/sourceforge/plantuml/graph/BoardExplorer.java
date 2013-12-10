/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.util.HashSet;
import java.util.Set;

public class BoardExplorer {

	private final BoardCollection all = new BoardCollection(new KenavoCostComputer());

	public BoardExplorer(Board init) {
		all.add(init);
	}

	public double getBestCost() {
		return all.getBestCost();
	}

	public Board getBestBoard() {
		return all.getBestBoard();
	}

	public int collectionSize() {
		return all.size();
	}

	public boolean onePass() {
		final Board smallest = all.getAndSetExploredSmallest();
		if (smallest == null) {
			return true;
		}
		final Set<Board> moves = nextBoards(smallest);
		for (Board newBoard : moves) {
			if (all.contains(newBoard)) {
				continue;
			}
			all.add(newBoard);
		}
		return false;
	}

	public Set<Board> nextBoards(Board board) {
		final Set<Board> result = new HashSet<Board>();
		for (Move m : board.getAllPossibleMoves()) {
			final Board copy = board.copy();
			copy.applyMove(m);
			result.add(copy);
		}
		return result;
	}

}
