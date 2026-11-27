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

/**
 * A node of a DOT graph.
 * <p>
 * A node is created the first time it is mentioned, either in a node
 * statement or as an edge end. It receives the node defaults visible at that
 * moment in the subgraph where it is mentioned; later mentions only merge the
 * attributes given explicitly.
 */
public final class DotNode {

	private final String id;
	private final int index;
	private final DotSubgraph owner;
	private final DotAttributes attributes;

	DotNode(String id, int index, DotSubgraph owner, DotAttributes attributes) {
		this.id = id;
		this.index = index;
		this.owner = owner;
		this.attributes = attributes;
	}

	public String getId() {
		return id;
	}

	/**
	 * Creation order of the node in the whole graph, starting at 0.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * The subgraph in which the node was created (the root graph if it was
	 * created at the top level).
	 */
	public DotSubgraph getOwner() {
		return owner;
	}

	public DotAttributes getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return id;
	}

}
