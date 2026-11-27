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
package net.sourceforge.plantuml.ngdot.layout;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.ngdot.model.DotEdge;
import net.sourceforge.plantuml.ngdot.model.DotNode;
import net.sourceforge.plantuml.ngdot.model.DotSubgraph;

/**
 * What the drawing side tells the layout: Smetana does not measure text, so
 * the size of every node and label is computed beforehand by PlantUML.
 * All sizes are in pixels (which Graphviz calls points).
 */
public final class LayoutInput {

	/** The outline used by Smetana to clip the edges at a node. */
	public enum Outline {
		BOX, ELLIPSE
	}

	private final Map<DotNode, XDimension2D> nodeSizes = new HashMap<DotNode, XDimension2D>();
	private final Map<DotNode, Outline> nodeOutlines = new HashMap<DotNode, Outline>();
	private final Map<DotEdge, XDimension2D> edgeLabels = new HashMap<DotEdge, XDimension2D>();
	private final Map<DotEdge, Double> tailArrows = new HashMap<DotEdge, Double>();
	private final Map<DotEdge, Double> headArrows = new HashMap<DotEdge, Double>();
	private final Map<DotSubgraph, XDimension2D> clusterLabels = new HashMap<DotSubgraph, XDimension2D>();
	private final Map<DotNode, NodeContour> contours = new HashMap<DotNode, NodeContour>();

	public void setNode(DotNode node, XDimension2D size, Outline outline) {
		nodeSizes.put(node, size);
		nodeOutlines.put(node, outline);
	}

	public XDimension2D getNodeSize(DotNode node) {
		return nodeSizes.get(node);
	}

	public Outline getNodeOutline(DotNode node) {
		final Outline result = nodeOutlines.get(node);
		return result == null ? Outline.ELLIPSE : result;
	}

	/**
	 * Declares that the edges must be clipped on the given outline rather than
	 * on the box or ellipse known by Smetana.
	 */
	public void setContour(DotNode node, NodeContour contour) {
		contours.put(node, contour);
	}

	public NodeContour getContour(DotNode node) {
		return contours.get(node);
	}

	public void setEdgeLabel(DotEdge edge, XDimension2D size) {
		edgeLabels.put(edge, size);
	}

	public XDimension2D getEdgeLabel(DotEdge edge) {
		return edgeLabels.get(edge);
	}

	/**
	 * Declares an arrow at the tail end of an edge.
	 *
	 * @param length the distance between the tip and the base of the arrow
	 */
	public void setTailArrow(DotEdge edge, double length) {
		tailArrows.put(edge, length);
	}

	public Double getTailArrow(DotEdge edge) {
		return tailArrows.get(edge);
	}

	/**
	 * Declares an arrow at the head end of an edge.
	 *
	 * @param length the distance between the tip and the base of the arrow
	 */
	public void setHeadArrow(DotEdge edge, double length) {
		headArrows.put(edge, length);
	}

	public Double getHeadArrow(DotEdge edge) {
		return headArrows.get(edge);
	}

	public void setClusterLabel(DotSubgraph cluster, XDimension2D size) {
		clusterLabels.put(cluster, size);
	}

	public XDimension2D getClusterLabel(DotSubgraph cluster) {
		return clusterLabels.get(cluster);
	}

}
