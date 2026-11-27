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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.ngdot.model.DotEdge;
import net.sourceforge.plantuml.ngdot.model.DotNode;
import net.sourceforge.plantuml.ngdot.model.DotSubgraph;

/**
 * The geometry computed by Smetana, copied into plain Java objects so that the
 * drawing does not depend on Smetana any more.
 * <p>
 * Coordinates are final drawing coordinates: origin at the top-left corner of
 * the graph bounding box, Y going down, {@code rankdir} already applied.
 */
public final class LayoutResult {

	private final XDimension2D dimension;
	private final Map<DotNode, NodeBox> nodes = new LinkedHashMap<DotNode, NodeBox>();
	private final Map<DotEdge, EdgeRoute> edges = new LinkedHashMap<DotEdge, EdgeRoute>();
	private final Map<DotSubgraph, ClusterBox> clusters = new LinkedHashMap<DotSubgraph, ClusterBox>();

	LayoutResult(XDimension2D dimension) {
		this.dimension = dimension;
	}

	public XDimension2D getDimension() {
		return dimension;
	}

	public Map<DotNode, NodeBox> getNodes() {
		return Collections.unmodifiableMap(nodes);
	}

	public Map<DotEdge, EdgeRoute> getEdges() {
		return Collections.unmodifiableMap(edges);
	}

	public Map<DotSubgraph, ClusterBox> getClusters() {
		return Collections.unmodifiableMap(clusters);
	}

	void put(DotNode node, NodeBox box) {
		nodes.put(node, box);
	}

	void put(DotEdge edge, EdgeRoute route) {
		edges.put(edge, route);
	}

	void put(DotSubgraph cluster, ClusterBox box) {
		clusters.put(cluster, box);
	}

	/**
	 * A node: its center and its size.
	 */
	public static final class NodeBox {

		private final XPoint2D center;
		private final XDimension2D size;

		NodeBox(XPoint2D center, XDimension2D size) {
			this.center = center;
			this.size = size;
		}

		public XPoint2D getCenter() {
			return center;
		}

		public XDimension2D getSize() {
			return size;
		}

		public XPoint2D getTopLeft() {
			return new XPoint2D(center.getX() - size.getWidth() / 2, center.getY() - size.getHeight() / 2);
		}

	}

	/**
	 * An edge: a piecewise cubic Bezier curve ({@code 1 + 3n} points), the tips
	 * of its arrows when it has some, and the center of its label.
	 * <p>
	 * When an end has an arrow, the curve stops at the base of the arrow and the
	 * tip is given apart, as in Graphviz.
	 */
	public static final class EdgeRoute {

		private final List<XPoint2D> points;
		private final XPoint2D tailTip;
		private final XPoint2D headTip;
		private final XPoint2D labelCenter;

		EdgeRoute(List<XPoint2D> points, XPoint2D tailTip, XPoint2D headTip, XPoint2D labelCenter) {
			this.points = Collections.unmodifiableList(points);
			this.tailTip = tailTip;
			this.headTip = headTip;
			this.labelCenter = labelCenter;
		}

		public List<XPoint2D> getPoints() {
			return points;
		}

		/**
		 * The tip of the arrow at the tail end, or {@code null}.
		 */
		public XPoint2D getTailTip() {
			return tailTip;
		}

		/**
		 * The tip of the arrow at the head end, or {@code null}.
		 */
		public XPoint2D getHeadTip() {
			return headTip;
		}

		/**
		 * The center of the label, or {@code null}.
		 */
		public XPoint2D getLabelCenter() {
			return labelCenter;
		}

	}

	/**
	 * A cluster: its bounding box and the center of its label.
	 */
	public static final class ClusterBox {

		private final XPoint2D topLeft;
		private final XDimension2D size;
		private final XPoint2D labelCenter;

		ClusterBox(XPoint2D topLeft, XDimension2D size, XPoint2D labelCenter) {
			this.topLeft = topLeft;
			this.size = size;
			this.labelCenter = labelCenter;
		}

		public XPoint2D getTopLeft() {
			return topLeft;
		}

		public XDimension2D getSize() {
			return size;
		}

		/**
		 * The center of the label, or {@code null}.
		 */
		public XPoint2D getLabelCenter() {
			return labelCenter;
		}

	}

}
