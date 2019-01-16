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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {

	private final List<ALink> links;
	private final Map<ALink, Integer> initialDirection;

	private final Map<ANode, Integer> nodesCols = new LinkedHashMap<ANode, Integer>();

	private int hashcodeValue;
	private boolean hashcodeComputed = false;

	private Board(Board old) {
		this.links = old.links;
		this.initialDirection = old.initialDirection;
		this.nodesCols.putAll(old.nodesCols);
	}

	public Comparator<ALink> getLinkComparator() {
		return new LenghtLinkComparator(nodesCols);
	}

	public boolean equals(Object o) {
		final Board other = (Board) o;
		if (this.links != other.links) {
			return false;
		}
		final Iterator<Integer> it1 = this.nodesCols.values().iterator();
		final Iterator<Integer> it2 = other.nodesCols.values().iterator();
		assert this.nodesCols.size() == other.nodesCols.size();
		while (it1.hasNext()) {
			if (it1.next().equals(it2.next()) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		if (this.hashcodeComputed) {
			return this.hashcodeValue;
		}
		this.hashcodeValue = 13;
		for (Integer i : nodesCols.values()) {
			this.hashcodeValue = this.hashcodeValue * 17 + i;
		}
		this.hashcodeComputed = true;
		return this.hashcodeValue;
	}

	public void normalize() {
		int minRow = Integer.MAX_VALUE;
		int minCol = Integer.MAX_VALUE;
		int maxRow = Integer.MIN_VALUE;
		int maxCol = Integer.MIN_VALUE;
		for (Map.Entry<ANode, Integer> ent : nodesCols.entrySet()) {
			minRow = Math.min(minRow, ent.getKey().getRow());
			maxRow = Math.max(maxRow, ent.getKey().getRow());
			minCol = Math.min(minCol, ent.getValue());
			maxCol = Math.max(maxCol, ent.getValue());
		}
		for (Map.Entry<ANode, Integer> ent : nodesCols.entrySet()) {
			if (minRow != 0) {
				ent.getKey().setRow(ent.getKey().getRow() - minRow);
			}
			if (minCol != 0) {
				ent.setValue(ent.getValue() - minCol);
			}
		}
	}

	private void normalizeCol() {
		final int minCol = Collections.min(nodesCols.values());

		if (minCol != 0) {
			for (Map.Entry<ANode, Integer> ent : nodesCols.entrySet()) {
				ent.setValue(ent.getValue() - minCol);
			}
		}
	}

	void internalMove(String code, int newCol) {
		hashcodeComputed = false;
		for (ANode n : nodesCols.keySet()) {
			if (n.getCode().equals(code)) {
				nodesCols.put(n, newCol);
				return;
			}
		}
	}

	public Board copy() {
		return new Board(this);
	}

	public Board(List<ANode> nodes, List<ALink> links) {
		for (ANode n : nodes) {
			addInRow(n);
		}
		this.links = Collections.unmodifiableList(new ArrayList<ALink>(links));
		this.initialDirection = new HashMap<ALink, Integer>();
		for (ALink link : links) {
			this.initialDirection.put(link, getDirection(link));
		}
	}

	public int getInitialDirection(ALink link) {
		return initialDirection.get(link);
	}

	public int getDirection(ALink link) {
		return getCol(link.getNode2()) - getCol(link.getNode1());
	}

	private void addInRow(ANode n) {
		hashcodeComputed = false;
		int col = 0;
		while (true) {
			if (getNodeAt(n.getRow(), col) == null) {
				nodesCols.put(n, col);
				assert getNodeAt(n.getRow(), col) == n;
				return;
			}
			col++;
		}
	}

	public Collection<ANode> getNodes() {
		return Collections.unmodifiableCollection(nodesCols.keySet());
	}

	public Collection<ANode> getNodesInRow(int row) {
		final List<ANode> result = new ArrayList<ANode>();
		for (ANode n : nodesCols.keySet()) {
			if (n.getRow() == row) {
				result.add(n);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public final List<? extends ALink> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public int getCol(ANode n) {
		return nodesCols.get(n);
	}

	public void applyMove(Move move) {
		final ANode piece = getNodeAt(move.getRow(), move.getCol());
		if (piece == null) {
			throw new IllegalArgumentException();
		}
		final ANode piece2 = getNodeAt(move.getRow(), move.getNewCol());
		nodesCols.put(piece, move.getNewCol());
		if (piece2 != null) {
			nodesCols.put(piece2, move.getCol());
		}
		normalizeCol();
		hashcodeComputed = false;
	}

	public Collection<Move> getAllPossibleMoves() {
		final List<Move> result = new ArrayList<Move>();
		for (Map.Entry<ANode, Integer> ent : nodesCols.entrySet()) {
			final int row = ent.getKey().getRow();
			final int col = ent.getValue();
			result.add(new Move(row, col, -1));
			result.add(new Move(row, col, 1));
		}
		return result;
	}

	public ANode getNodeAt(int row, int col) {
		for (Map.Entry<ANode, Integer> ent : nodesCols.entrySet()) {
			if (ent.getKey().getRow() == row && ent.getValue().intValue() == col) {
				return ent.getKey();
			}
		}
		return null;
	}

	public Set<ANode> getConnectedNodes(ANode root, int level) {
		if (level < 0) {
			throw new IllegalArgumentException();
		}
		if (level == 0) {
			return Collections.singleton(root);
		}
		final Set<ANode> result = new HashSet<ANode>();
		if (level == 1) {
			for (ALink link : links) {
				if (link.getNode1() == root) {
					result.add(link.getNode2());
				} else if (link.getNode2() == root) {
					result.add(link.getNode1());
				}

			}
		} else {
			for (ANode n : getConnectedNodes(root, level - 1)) {
				result.addAll(getConnectedNodes(n, 1));
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public Set<ALink> getAllLinks(Set<ANode> nodes) {
		final Set<ALink> result = new HashSet<ALink>();
		for (ALink link : links) {
			if (nodes.contains(link.getNode1()) || nodes.contains(link.getNode2())) {
				result.add(link);
			}
		}
		return Collections.unmodifiableSet(result);
	}

}
