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
import java.util.TreeMap;

/**
 * A subgraph of a DOT graph, or the root graph itself.
 * <p>
 * Three sets of attributes live here, following Graphviz (cgraph):
 * <ul>
 * <li>the graph attributes of this subgraph ({@code graph [...]} or
 * {@code name=value} statements). A new subgraph starts with a copy of the
 * current graph attributes of its parent: this is why a {@code label} set at
 * the top of a graph shows up on every cluster defined after it;</li>
 * <li>the node defaults and edge defaults written in this subgraph
 * ({@code node [...]}, {@code edge [...]}). They are a view over the
 * defaults of the parent: a lookup not found here goes up the chain, and it
 * is done when a node or edge is created.</li>
 * </ul>
 * Nodes and edges are listed in their global creation order, as cgraph
 * iterates them, whatever the order in which they entered this subgraph.
 */
public final class DotSubgraph {

	private final String id;
	private final boolean anonymous;
	private final DotSubgraph parent;
	private final List<DotSubgraph> children = new ArrayList<DotSubgraph>();

	private final DotAttributes attributes;
	private final DotAttributes nodeDefaults = new DotAttributes();
	private final DotAttributes edgeDefaults = new DotAttributes();

	private final Map<Integer, DotNode> nodes = new TreeMap<Integer, DotNode>();
	private final Map<Integer, DotEdge> edges = new TreeMap<Integer, DotEdge>();

	DotSubgraph(String id, boolean anonymous, DotSubgraph parent) {
		this.id = id;
		this.anonymous = anonymous;
		this.parent = parent;
		if (parent == null)
			this.attributes = new DotAttributes();
		else
			this.attributes = new DotAttributes(parent.attributes);
	}

	/**
	 * The name of the subgraph. An anonymous subgraph ({@code { a b }}) gets a
	 * generated name starting with {@code %}, as in Graphviz. The root graph
	 * may have a {@code null} id.
	 */
	public String getId() {
		return id;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * A subgraph whose name starts with {@code cluster} is drawn as a box
	 * around its nodes. The test is case-sensitive, as in Graphviz 2.38.
	 */
	public boolean isCluster() {
		return parent != null && id != null && id.startsWith("cluster");
	}

	public DotSubgraph getParent() {
		return parent;
	}

	public List<DotSubgraph> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public DotAttributes getAttributes() {
		return attributes;
	}

	/**
	 * The node defaults written in this subgraph only (not inherited).
	 */
	public DotAttributes getLocalNodeDefaults() {
		return nodeDefaults;
	}

	/**
	 * The edge defaults written in this subgraph only (not inherited).
	 */
	public DotAttributes getLocalEdgeDefaults() {
		return edgeDefaults;
	}

	/**
	 * Every node of this subgraph, including the nodes of its own subgraphs.
	 */
	public List<DotNode> getNodes() {
		return Collections.unmodifiableList(new ArrayList<DotNode>(nodes.values()));
	}

	public boolean contains(DotNode node) {
		return nodes.get(node.getIndex()) == node;
	}

	/**
	 * Every edge of this subgraph, including the edges of its own subgraphs.
	 */
	public List<DotEdge> getEdges() {
		return Collections.unmodifiableList(new ArrayList<DotEdge>(edges.values()));
	}

	@Override
	public String toString() {
		return "subgraph " + id;
	}

	// Package-private: used by DotGraphBuilder

	void addChild(DotSubgraph child) {
		children.add(child);
	}

	DotAttributes resolveNodeDefaults() {
		final DotAttributes result = parent == null ? new DotAttributes() : parent.resolveNodeDefaults();
		result.putAll(nodeDefaults);
		return result;
	}

	DotAttributes resolveEdgeDefaults() {
		final DotAttributes result = parent == null ? new DotAttributes() : parent.resolveEdgeDefaults();
		result.putAll(edgeDefaults);
		return result;
	}

	void addNode(DotNode node) {
		for (DotSubgraph s = this; s != null; s = s.parent)
			s.nodes.put(node.getIndex(), node);
	}

	void addEdge(DotEdge edge) {
		for (DotSubgraph s = this; s != null; s = s.parent)
			s.edges.put(edge.getIndex(), edge);
	}

}
