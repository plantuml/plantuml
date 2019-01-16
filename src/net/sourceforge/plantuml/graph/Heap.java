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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Heap {

	private final Map<String, ANode> nodes = new LinkedHashMap<String, ANode>();
	private final Map<ANode, LinkedHashMap<ANode, ALink>> directChildren = new LinkedHashMap<ANode, LinkedHashMap<ANode, ALink>>();
	private final List<ALink> links = new ArrayList<ALink>();

	public boolean isEmpty() {
		if (links.isEmpty()) {
			assert nodes.isEmpty();
			assert directChildren.isEmpty();
			return true;
		}
		return false;
	}

	public void importing(ANode under, ANode otherRoot, Heap otherHeap, int diffHeight, Object userData) {
		assert this.directChildren.keySet().contains(under);
		assert this.nodes.values().contains(under);
		assert otherHeap.nodes.values().contains(otherRoot);
		assert otherHeap.directChildren.keySet().contains(otherRoot);
		assert this.nodes.values().contains(otherRoot) == false;
		assert this.directChildren.keySet().contains(otherRoot) == false;
		assert otherHeap.directChildren.keySet().contains(under) == false;
		final int oldSize = this.nodes.size();
		assert oldSize == this.directChildren.size();
		this.nodes.putAll(otherHeap.nodes);
		this.directChildren.putAll(otherHeap.directChildren);
		final ALinkImpl link = new ALinkImpl(under, otherRoot, diffHeight, userData);
		this.links.add(link);
		this.links.addAll(otherHeap.links);
		assert oldSize + otherHeap.nodes.size() == this.nodes.size();
		assert oldSize + otherHeap.directChildren.size() == this.directChildren.size();

		addUnderMe(under, otherRoot, link);
	}

	public void computeRows() {
		for (ANode n : nodes.values()) {
			n.setRow(Integer.MIN_VALUE);
		}
		nodes.values().iterator().next().setRow(0);
		boolean changed;
		do {
			onePass();
			changed = false;
			for (ANode n : nodes.values()) {
				if (n.getRow() != Integer.MIN_VALUE) {
					continue;
				}
				final Map.Entry<ANode, ALink> smallestRowOfChildren = getSmallestRowOfChildren(n);
				if (smallestRowOfChildren != null) {
					n.setRow(getStartingRow(smallestRowOfChildren));
				}
				changed = true;
			}
		} while (changed);

		minToZero();
	}

	private int getStartingRow(Map.Entry<ANode, ALink> ent) {
		assert ent.getValue().getNode2() == ent.getKey();
		return ent.getValue().getNode2().getRow() - ent.getValue().getDiffHeight();
	}

	private void minToZero() {
		int min = Integer.MAX_VALUE;
		for (ANode n : nodes.values()) {
			min = Math.min(min, n.getRow());
		}
		if (min == Integer.MIN_VALUE) {
			throw new IllegalStateException();
		}
		if (min != 0) {
			for (ANode n : nodes.values()) {
				n.setRow(n.getRow() - min);
			}
		}

	}

	private Map.Entry<ANode, ALink> getSmallestRowOfChildren(ANode n) {
		assert n.getRow() == Integer.MIN_VALUE;
		Map.Entry<ANode, ALink> result = null;
		for (Map.Entry<ANode, ALink> ent : directChildren.get(n).entrySet()) {
			final ANode child = ent.getKey();
			if (child.getRow() == Integer.MIN_VALUE) {
				continue;
			}
			if (result == null || getStartingRow(ent) < getStartingRow(result)) {
				result = ent;
			}
		}
		// assert result != null;
		return result;
	}

	private void onePass() {
		boolean changed;
		do {
			changed = false;
			for (ANode n : nodes.values()) {
				final int row = n.getRow();
				if (row == Integer.MIN_VALUE) {
					continue;
				}
				for (Map.Entry<ANode, ALink> ent : directChildren.get(n).entrySet()) {
					final ANode child = ent.getKey();
					final int diffHeight = ent.getValue().getDiffHeight();
					if (child.getRow() == Integer.MIN_VALUE || child.getRow() < row + diffHeight) {
						child.setRow(row + diffHeight);
						changed = true;
					}
				}
			}
		} while (changed);
	}

	private ANode getNode(String code) {
		ANode result = nodes.get(code);
		if (result == null) {
			result = createNewNode(code);
		}
		return result;
	}

	private ANode createNewNode(String code) {
		final ANode result = new ANodeImpl(code);
		directChildren.put(result, new LinkedHashMap<ANode, ALink>());
		nodes.put(code, result);
		assert directChildren.size() == nodes.size();
		return result;
	}

	public ANode getExistingNode(String code) {
		return nodes.get(code);
	}

	public List<ALink> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public List<ANode> getNodes() {
		return Collections.unmodifiableList(new ArrayList<ANode>(nodes.values()));
	}

	HashSet<ANode> getAllChildren(ANode n) {
		final HashSet<ANode> result = new HashSet<ANode>(directChildren.get(n).keySet());
		int size = 0;
		do {
			size = result.size();
			for (ANode other : new HashSet<ANode>(result)) {
				result.addAll(getAllChildren(other));
			}
		} while (result.size() != size);
		return result;
	}

	public void addLink(String stringLink, int diffHeight, Object userData) {
		final LinkString l = new LinkString(stringLink);
		final ANode n1 = getNode(l.getNode1());
		final ANode n2 = getNode(l.getNode2());
		if (n1 == n2) {
			return;
		}
		final ALinkImpl link = new ALinkImpl(n1, n2, diffHeight, userData);
		links.add(link);

		if (getAllChildren(n2).contains(n1)) {
			addUnderMe(n2, n1, link);
		} else {
			addUnderMe(n1, n2, link);
		}
	}

	public ANode addNode(String code) {
		if (nodes.containsKey(code)) {
			throw new IllegalArgumentException();
		}
		return createNewNode(code);
	}

	private void addUnderMe(final ANode n1, final ANode n2, final ALinkImpl link) {
		assert getAllChildren(n2).contains(n1) == false;
		directChildren.get(n1).put(n2, link);
		assert getAllChildren(n1).contains(n2);
		assert getAllChildren(n2).contains(n1) == false;
	}

	public int getRowMax() {
		int max = Integer.MIN_VALUE;
		for (ANode n : nodes.values()) {
			max = Math.max(max, n.getRow());
		}
		return max;
	}

}
