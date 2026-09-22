/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2026, Jamison Jiang
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
 * Original Author:  Jamison Jiang
 *
 *
 */
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.svek.layout.SvekLayoutExpectations;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Path;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;
import net.sourceforge.plantuml.svek.layout.SvekLayoutValidation;

/**
 * Writes a neutral Svek layout result back onto the live Svek objects.
 *
 * The applier describes what the layout was asked to produce, validates the result against that
 * description, converts every coordinate into a placement, and only then mutates nodes, clusters
 * and edges.
 */
final class SvekLayoutResultApplier {

	private final DotStringFactory factory;
	private final Bibliotekon bibliotekon;
	private boolean prepared;

	SvekLayoutResultApplier(DotStringFactory factory) {
		if (factory == null)
			throw new NullPointerException("factory");
		this.factory = factory;
		this.bibliotekon = factory.getBibliotekon();
	}

	SvekLayoutValidation validate(SvekLayoutResult result) {
		return SvekLayoutValidation.validate(result, expectations());
	}

	/**
	 * Validation and all geometry conversion complete before mutation. Setter-side
	 * failures are not rolled back because edge application also updates decorations
	 * and lollipop state.
	 */
	SvekLayoutValidation apply(SvekLayoutResult result) {
		final PreparedLayout preparedLayout = prepare(result);
		if (preparedLayout.validation.isValid())
			preparedLayout.apply();
		return preparedLayout.validation;
	}

	synchronized PreparedLayout prepare(SvekLayoutResult result) {
		claimPrepare();
		final SvekLayoutValidation validation = validate(result);
		if (validation.isInvalid())
			return new PreparedLayout(validation, null, null, null);

		return new PreparedLayout(validation, nodePlacements(result), clusterPlacements(result),
				edgePlacements(result));
	}

	/** A rejected result consumes the single use too, so the same applier never sees a second result. */
	private void claimPrepare() {
		if (prepared)
			throw new IllegalStateException("Svek layout result applier may only apply once");
		prepared = true;
	}

	final class PreparedLayout {
		private final SvekLayoutValidation validation;
		private final List<NodePlacement> nodes;
		private final List<ClusterPlacement> clusters;
		private final List<EdgePlacement> edges;
		private boolean applied;

		private PreparedLayout(SvekLayoutValidation validation, List<NodePlacement> nodes,
				List<ClusterPlacement> clusters, List<EdgePlacement> edges) {
			this.validation = validation;
			this.nodes = nodes;
			this.clusters = clusters;
			this.edges = edges;
		}

		SvekLayoutValidation getValidation() {
			return validation;
		}

		synchronized void apply() {
			claimApply();
			requireValid();
			for (NodePlacement placement : nodes)
				placement.apply();
			for (ClusterPlacement placement : clusters)
				placement.apply();
			for (EdgePlacement placement : edges)
				placement.apply();
			factory.finishLayout();
		}

		/** Claimed before the validity check so a rejected layout cannot be applied twice either. */
		private void claimApply() {
			if (applied)
				throw new IllegalStateException("Prepared Svek layout may only apply once");
			applied = true;
		}

		private void requireValid() {
			if (validation.isInvalid())
				throw new IllegalStateException("Cannot apply invalid Svek layout: " + validation.getMessage());
		}
	}

	// Expectations: what the layout was asked to produce

	private SvekLayoutExpectations expectations() {
		final SvekLayoutExpectations expectations = new SvekLayoutExpectations();
		expectations.setDirection(factory.getLayoutDirection());
		collectNodes(expectations);
		collectClusters(expectations);
		collectContent(factory.getRootCluster(), expectations.rootNodeIds, expectations.rootClusterIds);
		collectEdges(expectations);
		return expectations;
	}

	/**
	 * The returned bounds must match the visible body, so the measured node size replaces the spec
	 * size, which may include ports, shielding or other layout-only padding.
	 */
	private void collectNodes(SvekLayoutExpectations expectations) {
		for (SvekNode node : bibliotekon.allNodes()) {
			final NodeSpec spec = node.toLayoutSpec();
			expectations.nodes.put(spec.id, new NodeSpec(spec.id, node.getWidth(), node.getHeight(), spec.shape,
					spec.bodyCellId, spec.cells));
		}
	}

	private void collectClusters(SvekLayoutExpectations expectations) {
		for (Cluster cluster : bibliotekon.allCluster()) {
			if (cluster.getGroup().isPacked())
				continue;
			final ClusterSpec spec = cluster.toLayoutSpec();
			final Set<String> nodeIds = new LinkedHashSet<>();
			final Set<String> childIds = new LinkedHashSet<>();
			collectContent(cluster, nodeIds, childIds);
			expectations.clusters.put(spec.id, spec);
			expectations.clusterNodeIds.put(spec.id, nodeIds);
			expectations.clusterClusterIds.put(spec.id, childIds);
		}
	}

	/** A packed cluster has no layout identity, so its content is folded into the enclosing one. */
	private static void collectContent(Cluster parent, Set<String> nodeIds, Set<String> clusterIds) {
		for (SvekNode node : parent.getNodes())
			nodeIds.add(node.toLayoutSpec().id);
		for (Cluster child : parent.getChildren())
			if (child.getGroup().isPacked())
				collectContent(child, nodeIds, clusterIds);
			else
				clusterIds.add(child.toLayoutSpec().id);
	}

	private void collectEdges(SvekLayoutExpectations expectations) {
		for (SvekEdge edge : bibliotekon.allLines()) {
			final EdgeSpec spec = edge.toLayoutSpec();
			expectations.edges.put(spec.id, spec);
			if (spec.visible)
				expectations.visibleEdgeIds.add(spec.id);
			expectations.requiredLabels.put(spec.id, labelPositions(spec));
		}
	}

	private static Set<LabelPosition> labelPositions(EdgeSpec spec) {
		final Set<LabelPosition> positions = new LinkedHashSet<>();
		for (LabelSpec label : spec.labels)
			positions.add(label.position);
		return positions;
	}

	// Placements: geometry converted while the Svek objects are still untouched

	private List<NodePlacement> nodePlacements(SvekLayoutResult result) {
		final Map<String, SvekNode> nodes = nodeMap();
		final List<NodePlacement> placements = new ArrayList<>();
		for (Map.Entry<String, NodeGeometry> entry : result.nodes.entrySet()) {
			final NodeGeometry geometry = entry.getValue();
			placements.add(new NodePlacement(nodes.get(entry.getKey()),
					new XPoint2D(geometry.bounds.minX, geometry.bounds.minY), copyPoints(geometry.polygon)));
		}
		return placements;
	}

	private List<ClusterPlacement> clusterPlacements(SvekLayoutResult result) {
		final Map<String, Cluster> clusters = clusterMap();
		final List<ClusterPlacement> placements = new ArrayList<>();
		for (Map.Entry<String, ClusterGeometry> entry : result.clusters.entrySet()) {
			final ClusterGeometry geometry = entry.getValue();
			placements.add(new ClusterPlacement(clusters.get(entry.getKey()),
					new XPoint2D(geometry.bounds.minX, geometry.bounds.minY),
					new XPoint2D(geometry.bounds.maxX, geometry.bounds.maxY), copy(geometry.title)));
		}
		return placements;
	}

	/** An edge without a path keeps the geometry it already has, labels included. */
	private List<EdgePlacement> edgePlacements(SvekLayoutResult result) {
		final Map<String, SvekEdge> edges = edgeMap();
		final List<EdgePlacement> placements = new ArrayList<>();
		for (Map.Entry<String, EdgeGeometry> entry : result.edges.entrySet()) {
			final EdgeGeometry geometry = entry.getValue();
			if (geometry.path == null)
				continue;
			placements.add(new EdgePlacement(edges.get(entry.getKey()), toDotPath(geometry.path), geometry.path.cubic,
					copy(geometry.mainLabel), copy(geometry.tailLabel), copy(geometry.headLabel)));
		}
		return placements;
	}

	private Map<String, SvekNode> nodeMap() {
		final Map<String, SvekNode> result = new LinkedHashMap<>();
		for (SvekNode node : bibliotekon.allNodes())
			result.put(node.toLayoutSpec().id, node);
		return result;
	}

	private Map<String, Cluster> clusterMap() {
		final Map<String, Cluster> result = new LinkedHashMap<>();
		for (Cluster cluster : bibliotekon.allCluster())
			if (cluster.getGroup().isPacked() == false)
				result.put(cluster.toLayoutSpec().id, cluster);
		return result;
	}

	private Map<String, SvekEdge> edgeMap() {
		final Map<String, SvekEdge> result = new LinkedHashMap<>();
		for (SvekEdge edge : bibliotekon.allLines())
			result.put(edge.toLayoutSpec().id, edge);
		return result;
	}

	// Coordinates

	private static List<XPoint2D> copyPoints(List<Point> source) {
		if (source == null)
			return null;
		final List<XPoint2D> result = new ArrayList<>();
		for (Point point : source)
			result.add(copy(point));
		return result;
	}

	private static XPoint2D copy(Point point) {
		return point == null ? null : new XPoint2D(point.x, point.y);
	}

	private static DotPath toDotPath(Path path) {
		return DotPath.fromBeziers(path.cubic ? cubicCurves(path.points) : polylineCurves(path.points));
	}

	private static List<XCubicCurve2D> cubicCurves(List<Point> points) {
		final List<XCubicCurve2D> curves = new ArrayList<>();
		for (int i = 0; i + 3 < points.size(); i += 3) {
			final Point a = points.get(i);
			final Point b = points.get(i + 1);
			final Point c = points.get(i + 2);
			final Point d = points.get(i + 3);
			curves.add(new XCubicCurve2D(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y));
		}
		return curves;
	}

	/** Svek only draws beziers, so a straight segment gets control points on its own ends. */
	private static List<XCubicCurve2D> polylineCurves(List<Point> points) {
		final List<XCubicCurve2D> curves = new ArrayList<>();
		for (int i = 1; i < points.size(); i++) {
			final Point a = points.get(i - 1);
			final Point b = points.get(i);
			curves.add(new XCubicCurve2D(a.x, a.y, a.x, a.y, b.x, b.y, b.x, b.y));
		}
		return curves;
	}


	private static final class NodePlacement {
		private final SvekNode node;
		private final XPoint2D position;
		private final List<XPoint2D> polygon;

		private NodePlacement(SvekNode node, XPoint2D position, List<XPoint2D> polygon) {
			this.node = node;
			this.position = position;
			this.polygon = polygon;
		}

		private void apply() {
			node.setPosition(position.getX(), position.getY());
			// A polygon is stored relative to the node origin.
			if (polygon != null)
				node.setPolygon(position.getX(), position.getY(), polygon);
		}
	}

	private static final class ClusterPlacement {
		private final Cluster cluster;
		private final XPoint2D min;
		private final XPoint2D max;
		private final XPoint2D title;

		private ClusterPlacement(Cluster cluster, XPoint2D min, XPoint2D max, XPoint2D title) {
			this.cluster = cluster;
			this.min = min;
			this.max = max;
			this.title = title;
		}

		private void apply() {
			cluster.setPosition(min, max);
			if (cluster.isLayoutTitleRequired())
				cluster.setTitlePosition(title);
		}
	}

	private static final class EdgePlacement {
		private final SvekEdge edge;
		private final DotPath path;
		private final boolean cubic;
		private final XPoint2D mainLabel;
		private final XPoint2D tailLabel;
		private final XPoint2D headLabel;

		private EdgePlacement(SvekEdge edge, DotPath path, boolean cubic, XPoint2D mainLabel, XPoint2D tailLabel,
				XPoint2D headLabel) {
			this.edge = edge;
			this.path = path;
			this.cubic = cubic;
			this.mainLabel = mainLabel;
			this.tailLabel = tailLabel;
			this.headLabel = headLabel;
		}

		private void apply() {
			edge.applyLayoutGeometry(path, null, mainLabel, tailLabel, headLabel, cubic, true);
		}
	}
}
