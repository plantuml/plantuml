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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A parsed DOT graph: the root subgraph, plus every node and edge in creation
 * order. Built by {@link DotGraphBuilder}.
 */
public final class DotGraph {

	private final boolean strict;
	private final boolean directed;
	private final DotSubgraph root;
	private final List<DotNode> nodes;
	private final Map<String, DotNode> nodesById;
	private final List<DotEdge> edges;

	DotGraph(boolean strict, boolean directed, DotSubgraph root, List<DotNode> nodes, Map<String, DotNode> nodesById,
			List<DotEdge> edges) {
		this.strict = strict;
		this.directed = directed;
		this.root = root;
		this.nodes = Collections.unmodifiableList(new ArrayList<DotNode>(nodes));
		this.nodesById = nodesById;
		this.edges = Collections.unmodifiableList(new ArrayList<DotEdge>(edges));
	}

	public boolean isStrict() {
		return strict;
	}

	/**
	 * {@code true} for a {@code digraph}, {@code false} for a {@code graph}.
	 */
	public boolean isDirected() {
		return directed;
	}

	/**
	 * The name of the graph ({@code digraph G {...}}), or {@code null}.
	 */
	public String getId() {
		return root.getId();
	}

	public DotSubgraph getRoot() {
		return root;
	}

	/**
	 * The graph attributes of the root graph.
	 */
	public DotAttributes getAttributes() {
		return root.getAttributes();
	}

	public List<DotNode> getNodes() {
		return nodes;
	}

	public DotNode getNode(String id) {
		return nodesById.get(id);
	}

	public List<DotEdge> getEdges() {
		return edges;
	}

	/**
	 * Every subgraph of the graph (not the root), depth first, in creation order.
	 */
	public List<DotSubgraph> getAllSubgraphs() {
		final List<DotSubgraph> result = new ArrayList<DotSubgraph>();
		addSubgraphs(root, result);
		return Collections.unmodifiableList(result);
	}

	private static void addSubgraphs(DotSubgraph parent, List<DotSubgraph> result) {
		for (DotSubgraph child : parent.getChildren()) {
			result.add(child);
			addSubgraphs(child, result);
		}
	}

}
