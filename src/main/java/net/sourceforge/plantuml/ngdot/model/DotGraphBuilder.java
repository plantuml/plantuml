/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.ngdot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a {@link DotGraph} statement after statement, applying the DOT
 * semantics of Graphviz (cgraph and its grammar):
 * <ul>
 * <li>a node is created at its first mention, with the node defaults visible
 * at that moment in the current subgraph; mentioning it again inside another
 * subgraph only adds it to that subgraph;</li>
 * <li>an edge gets the edge defaults visible in the current subgraph, then
 * the attributes of its statement;</li>
 * <li>in a {@code strict} graph, a second edge between the same two nodes (in
 * either direction for an undirected graph) is the same edge: only its
 * attributes are merged;</li>
 * <li>reopening a named subgraph in the same parent continues it.</li>
 * </ul>
 */
public final class DotGraphBuilder {

	private final boolean strict;
	private final boolean directed;
	private final DotSubgraph root;
	private DotSubgraph current;

	private final List<DotNode> nodes = new ArrayList<DotNode>();
	private final Map<String, DotNode> nodesById = new HashMap<String, DotNode>();
	private final List<DotEdge> edges = new ArrayList<DotEdge>();
	private final Map<String, DotEdge> strictEdges = new HashMap<String, DotEdge>();
	private final Map<DotSubgraph, Map<String, DotSubgraph>> namedChildren = new HashMap<DotSubgraph, Map<String, DotSubgraph>>();
	private int anonymousCounter;

	public DotGraphBuilder(boolean strict, boolean directed, String id) {
		this.strict = strict;
		this.directed = directed;
		this.root = new DotSubgraph(id, false, null);
		this.current = root;
	}

	public boolean isDirected() {
		return directed;
	}

	public DotSubgraph getCurrent() {
		return current;
	}

	/**
	 * Enters a subgraph. With a {@code null} id, a new anonymous subgraph is
	 * created. With an id already used by a child of the current subgraph, that
	 * child is reopened.
	 */
	public DotSubgraph openSubgraph(String id) {
		DotSubgraph result = null;
		Map<String, DotSubgraph> byName = namedChildren.get(current);
		if (byName == null) {
			byName = new LinkedHashMap<String, DotSubgraph>();
			namedChildren.put(current, byName);
		}
		if (id != null)
			result = byName.get(id);

		if (result == null) {
			final boolean anonymous = id == null;
			final String name = anonymous ? "%" + (++anonymousCounter) : id;
			result = new DotSubgraph(name, anonymous, current);
			current.addChild(result);
			byName.put(name, result);
		}
		current = result;
		return result;
	}

	public void closeSubgraph() {
		if (current.getParent() == null)
			throw new IllegalStateException();
		current = current.getParent();
	}

	public void setGraphAttribute(String name, DotValue value) {
		current.getAttributes().put(name, value);
	}

	public void setNodeDefault(String name, DotValue value) {
		current.getLocalNodeDefaults().put(name, value);
	}

	public void setEdgeDefault(String name, DotValue value) {
		current.getLocalEdgeDefaults().put(name, value);
	}

	/**
	 * Mentions a node in the current subgraph, creating it if needed.
	 */
	public DotNode mentionNode(String id) {
		DotNode result = nodesById.get(id);
		if (result == null) {
			result = new DotNode(id, nodes.size(), current, current.resolveNodeDefaults());
			nodes.add(result);
			nodesById.put(id, result);
		}
		current.addNode(result);
		return result;
	}

	public void setNodeAttributes(DotNode node, DotAttributes attributes) {
		node.getAttributes().putAll(attributes);
	}

	/**
	 * Creates (or, in a strict graph, finds) the edge {@code tail -> head} in
	 * the current subgraph.
	 *
	 * @param tailPort the port written after the tail node, or {@code null}
	 * @param headPort the port written after the head node, or {@code null}
	 */
	public DotEdge addEdge(DotNode tail, String tailPort, DotNode head, String headPort, DotAttributes attributes) {
		DotEdge result = null;
		final String key = strictKey(tail, head);
		if (strict)
			result = strictEdges.get(key);

		if (result == null) {
			result = new DotEdge(tail, head, edges.size(), current, current.resolveEdgeDefaults());
			edges.add(result);
			if (strict)
				strictEdges.put(key, result);
		}
		result.getAttributes().putAll(attributes);
		// As in Graphviz, the ports written in the statement win over the attributes
		if (tailPort != null)
			result.getAttributes().put("tailport", DotValue.of(tailPort));
		if (headPort != null)
			result.getAttributes().put("headport", DotValue.of(headPort));

		current.addEdge(result);
		return result;
	}

	private String strictKey(DotNode tail, DotNode head) {
		int a = tail.getIndex();
		int b = head.getIndex();
		if (directed == false && a > b) {
			final int tmp = a;
			a = b;
			b = tmp;
		}
		return a + "/" + b;
	}

	public DotGraph build() {
		if (current != root)
			throw new IllegalStateException("Unclosed subgraph " + current.getId());
		return new DotGraph(strict, directed, root, nodes, nodesById, edges);
	}

}
