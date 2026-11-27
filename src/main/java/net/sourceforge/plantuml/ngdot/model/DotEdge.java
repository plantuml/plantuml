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
 * An edge of a DOT graph.
 * <p>
 * The ports written in an edge statement ({@code a:p1:n -> b}) are stored as
 * the {@code tailport} and {@code headport} attributes, as Graphviz does.
 */
public final class DotEdge {

	private final DotNode tail;
	private final DotNode head;
	private final int index;
	private final DotSubgraph owner;
	private final DotAttributes attributes;

	DotEdge(DotNode tail, DotNode head, int index, DotSubgraph owner, DotAttributes attributes) {
		this.tail = tail;
		this.head = head;
		this.index = index;
		this.owner = owner;
		this.attributes = attributes;
	}

	public DotNode getTail() {
		return tail;
	}

	public DotNode getHead() {
		return head;
	}

	/**
	 * Creation order of the edge in the whole graph, starting at 0.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * The subgraph in which the edge statement was written.
	 */
	public DotSubgraph getOwner() {
		return owner;
	}

	public DotAttributes getAttributes() {
		return attributes;
	}

	public String getTailPort() {
		return attributes.getString("tailport");
	}

	public String getHeadPort() {
		return attributes.getString("headport");
	}

	@Override
	public String toString() {
		return tail + "->" + head;
	}

}
