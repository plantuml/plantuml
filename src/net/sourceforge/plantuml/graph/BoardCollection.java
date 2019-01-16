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

import java.util.ArrayList;
import java.util.List;

public class BoardCollection {

	static class Entry implements Comparable<Entry> {
		final private Board board;
		final private double cost;
		private boolean explored;

		public Entry(Board b, CostComputer costComputer) {
			this.board = b;
			if (costComputer == null) {
				this.cost = 0;
			} else {
				this.cost = costComputer.getCost(b);
			}
		}

		public int compareTo(Entry other) {
			return (int) Math.signum(this.cost - other.cost);
		}

		@Override
		public boolean equals(Object obj) {
			final Entry other = (Entry) obj;
			return board.equals(other.board);
		}

		@Override
		public int hashCode() {
			return board.hashCode();
		}

	}

	private final SortedCollection<Entry> all = new SortedCollectionArrayList<Entry>();

	private final CostComputer costComputer;

	public BoardCollection(CostComputer costComputer) {
		this.costComputer = costComputer;
	}

	public int size() {
		return all.size();
	}

	public Board getAndSetExploredSmallest() {
		for (Entry ent : all) {
			if (ent.explored == false) {
				ent.explored = true;
				assert costComputer.getCost(ent.board) == ent.cost;
				// Log.println("Peeking " + ent.cost);
				return ent.board;
			}
		}
		return null;
	}

	public double getBestCost() {
		for (Entry ent : all) {
			return ent.cost;
		}
		return 0;
	}

	public Board getBestBoard() {
		for (Entry ent : all) {
			return ent.board;
		}
		return null;
	}

	public List<Double> getCosts() {
		final List<Double> result = new ArrayList<Double>();
		for (Entry ent : all) {
			result.add(costComputer.getCost(ent.board));
		}
		return result;
	}

	public void add(Board b) {
		all.add(new Entry(b, costComputer));
	}

	public boolean contains(Board b) {
		return all.contains(new Entry(b, null));
	}

}
