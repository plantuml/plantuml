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
package net.sourceforge.plantuml.svek.layout;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Shape;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Bounds;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Path;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;

/**
 * Checks a layout result against what was asked for, so Svek never draws geometry it cannot honour.
 *
 * Every check returns the first failure with a message naming the offending element; the caller
 * turns that message into a decline reason.
 */
public final class SvekLayoutValidation {

	/** Rounding slack for "inside" tests, small enough that a real overlap still fails. */
	private static final double CONTAINMENT_TOLERANCE = 0.000001;

	/** A node may be rounded to a hundredth of a pixel, but not resized. */
	private static final double DIMENSION_TOLERANCE = 0.01;

	private final boolean valid;
	private final String message;

	private SvekLayoutValidation(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}

	public static SvekLayoutValidation valid() {
		return new SvekLayoutValidation(true, null);
	}

	public static SvekLayoutValidation invalid(String message) {
		return new SvekLayoutValidation(false, message);
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isInvalid() {
		return valid == false;
	}

	public String getMessage() {
		return message;
	}

	public static SvekLayoutValidation validate(SvekLayoutResult result, SvekLayoutExpectations expected) {
		if (expected == null || expected.getDirection() == null)
			return invalid("layout expectations are null");
		final SvekLayoutValidation shape = validateShape(result, expected);
		if (shape.isInvalid())
			return shape;
		if (result.direction != expected.getDirection())
			return invalid("layout direction does not match expected direction");
		final SvekLayoutValidation content = validateContent(result, expected);
		if (content.isInvalid())
			return content;
		return validateClusterAndRootContainment(result, expected.clusterClusterIds, expected.rootNodeIds,
				expected.rootClusterIds);
	}

	/** The result is well-formed on its own terms: finite geometry, matching IDs, usable paths. */
	private static SvekLayoutValidation validateShape(SvekLayoutResult result, SvekLayoutExpectations expected) {
		if (result == null)
			return invalid("layout result is null");
		if (validBounds(result.graphBounds) == false)
			return invalid("graph bounds must be finite and positive");
		if (result.nodes == null || result.clusters == null || result.edges == null)
			return invalid("layout geometry maps must not be null");
		final SvekLayoutValidation nodeIds = exactIds("node", expected.nodes.keySet(), result.nodes.keySet());
		if (nodeIds.isInvalid())
			return nodeIds;
		final SvekLayoutValidation clusterIds = exactIds("cluster", expected.clusters.keySet(),
				result.clusters.keySet());
		if (clusterIds.isInvalid())
			return clusterIds;
		final SvekLayoutValidation geometry = validateNodeAndClusterGeometry(result);
		if (geometry.isInvalid())
			return geometry;
		final SvekLayoutValidation containment = validateClusterNodeContainment(result, expected.clusterNodeIds);
		if (containment.isInvalid())
			return containment;
		return validateEdgeGeometry(result, expected.visibleEdgeIds, expected.requiredLabels);
	}

	/** The result describes the elements that were emitted, at the sizes that were emitted. */
	private static SvekLayoutValidation validateContent(SvekLayoutResult result, SvekLayoutExpectations expected) {
		final SvekLayoutValidation edges = validateExpectedEdges(result, expected.edges);
		if (edges.isInvalid())
			return edges;
		final SvekLayoutValidation nodes = validateExpectedNodes(result, expected.nodes);
		if (nodes.isInvalid())
			return nodes;
		return validateExpectedClusters(result, expected.clusters, expected.clusterNodeIds,
				expected.clusterClusterIds);
	}

	private static SvekLayoutValidation validateExpectedEdges(SvekLayoutResult result,
			Map<String, EdgeSpec> expectedEdges) {
		for (Map.Entry<String, EdgeGeometry> entry : result.edges.entrySet()) {
			final EdgeSpec expected = expectedEdges.get(entry.getKey());
			if (expected == null)
				return invalid("edge expectation is null: " + entry.getKey());
			final EdgeGeometry edge = entry.getValue();
			if (expected.tailId.equals(edge.tailId) == false || expected.headId.equals(edge.headId) == false)
				return invalid("edge endpoints do not match expected endpoints: " + entry.getKey());
		}
		return valid();
	}

	private static SvekLayoutValidation validateExpectedNodes(SvekLayoutResult result,
			Map<String, NodeSpec> expectedNodes) {
		for (Map.Entry<String, NodeGeometry> entry : result.nodes.entrySet()) {
			final NodeGeometry node = entry.getValue();
			final NodeSpec expected = expectedNodes.get(entry.getKey());
			if (expected == null)
				return invalid("node expectation is null: " + entry.getKey());
			if (Math.abs(node.bounds.width() - expected.width) > DIMENSION_TOLERANCE
					|| Math.abs(node.bounds.height() - expected.height) > DIMENSION_TOLERANCE)
				return invalid("node dimensions do not match expected visible body: " + entry.getKey());
			final SvekLayoutValidation polygon = validatePolygon(entry.getKey(), node.bounds, node.polygon,
					expected.shape);
			if (polygon.isInvalid())
				return polygon;
		}
		return valid();
	}

	private static SvekLayoutValidation validateExpectedClusters(SvekLayoutResult result,
			Map<String, ClusterSpec> expectedClusters, Map<String, Set<String>> clusterNodeIds,
			Map<String, Set<String>> clusterClusterIds) {
		for (Map.Entry<String, ClusterGeometry> entry : result.clusters.entrySet()) {
			final ClusterSpec expected = expectedClusters.get(entry.getKey());
			if (expected == null)
				return invalid("cluster expectation is null: " + entry.getKey());
			final ClusterGeometry cluster = entry.getValue();
			final SvekLayoutValidation title = validateTitle(entry.getKey(), cluster, expected);
			if (title.isInvalid())
				return title;
			if (expected.contentTopPadding > 0 && cluster.title != null) {
				final SvekLayoutValidation protection = validateTitleProtection(entry.getKey(), cluster, expected,
						result, clusterNodeIds, clusterClusterIds);
				if (protection.isInvalid())
					return protection;
			}
		}
		return valid();
	}

	/** A titled cluster must reserve a rectangle for its title inside its own bounds. */
	private static SvekLayoutValidation validateTitle(String id, ClusterGeometry cluster, ClusterSpec expected) {
		if (expected.titleWidth <= 0 || expected.titleHeight <= 0) {
			if (cluster.title != null && finite(cluster.title) == false)
				return invalid("cluster title must be finite: " + id);
			return valid();
		}
		if (finite(cluster.title) == false)
			return invalid("cluster title is missing or non-finite: " + id);
		final Bounds titleBounds = new Bounds(cluster.title.x, cluster.title.y,
				cluster.title.x + expected.titleWidth, cluster.title.y + expected.titleHeight);
		if (contains(cluster.bounds, titleBounds) == false)
			return invalid("cluster title rectangle is not contained: " + id);
		return valid();
	}

	private static SvekLayoutValidation validateTitleProtection(String id, ClusterGeometry cluster,
			ClusterSpec expected, SvekLayoutResult result, Map<String, Set<String>> clusterNodeIds,
			Map<String, Set<String>> clusterClusterIds) {
		// Direct children must stay below the title and its content padding.
		final double protectedBottom = cluster.title.y + expected.titleHeight + expected.contentTopPadding;
		final Set<String> directNodeIds = clusterNodeIds.get(id);
		if (directNodeIds != null)
			for (String nodeId : directNodeIds) {
				final NodeGeometry node = result.nodes.get(nodeId);
				if (node != null && node.bounds.minY + CONTAINMENT_TOLERANCE < protectedBottom)
					return invalid("cluster title protection overlaps direct node: " + id + " -> " + nodeId);
			}
		final Set<String> directClusterIds = clusterClusterIds.get(id);
		if (directClusterIds != null)
			for (String childId : directClusterIds) {
				final ClusterGeometry child = result.clusters.get(childId);
				if (child != null && child.bounds.minY + CONTAINMENT_TOLERANCE < protectedBottom)
					return invalid("cluster title protection overlaps direct cluster: " + id + " -> " + childId);
			}
		return valid();
	}

	private static SvekLayoutValidation validateClusterAndRootContainment(SvekLayoutResult result,
			Map<String, Set<String>> clusterClusterIds, Set<String> rootNodeIds, Set<String> rootClusterIds) {
		final SvekLayoutValidation nested = validateNestedClusters(result, clusterClusterIds);
		if (nested.isInvalid())
			return nested;
		for (String nodeId : rootNodeIds) {
			final NodeGeometry node = result.nodes.get(nodeId);
			if (node == null || contains(result.graphBounds, node.bounds) == false)
				return invalid("graph bounds do not contain root node: " + nodeId);
		}
		for (String clusterId : rootClusterIds) {
			final ClusterGeometry cluster = result.clusters.get(clusterId);
			if (cluster == null || contains(result.graphBounds, cluster.bounds) == false)
				return invalid("graph bounds do not contain root cluster: " + clusterId);
		}
		return valid();
	}

	private static SvekLayoutValidation validateNestedClusters(SvekLayoutResult result,
			Map<String, Set<String>> clusterClusterIds) {
		for (Map.Entry<String, Set<String>> entry : clusterClusterIds.entrySet()) {
			final ClusterGeometry cluster = result.clusters.get(entry.getKey());
			if (cluster == null || entry.getValue() == null)
				return invalid("cluster child membership is invalid: " + entry.getKey());
			for (String childId : entry.getValue()) {
				final ClusterGeometry child = result.clusters.get(childId);
				if (child == null || contains(cluster.bounds, child.bounds) == false)
					return invalid("cluster does not contain child cluster: " + entry.getKey() + " -> " + childId);
			}
		}
		return valid();
	}

	private static SvekLayoutValidation validateNodeAndClusterGeometry(SvekLayoutResult result) {
		for (Map.Entry<String, NodeGeometry> entry : result.nodes.entrySet()) {
			final NodeGeometry node = entry.getValue();
			if (node == null)
				return invalid("node geometry is null: " + entry.getKey());
			if (entry.getKey() == null || entry.getKey().equals(node.id) == false)
				return invalid("node ID does not match map key: " + entry.getKey());
			if (validBounds(node.bounds) == false)
				return invalid("node bounds must be finite and positive: " + entry.getKey());
		}
		for (Map.Entry<String, ClusterGeometry> entry : result.clusters.entrySet()) {
			final ClusterGeometry cluster = entry.getValue();
			if (cluster == null)
				return invalid("cluster geometry is null: " + entry.getKey());
			if (entry.getKey() == null || entry.getKey().equals(cluster.id) == false)
				return invalid("cluster ID does not match map key: " + entry.getKey());
			if (validBounds(cluster.bounds) == false)
				return invalid("cluster bounds must be finite and positive: " + entry.getKey());
		}
		return valid();
	}

	private static SvekLayoutValidation validateClusterNodeContainment(SvekLayoutResult result,
			Map<String, Set<String>> clusterNodeIds) {
		for (Map.Entry<String, Set<String>> entry : clusterNodeIds.entrySet()) {
			final ClusterGeometry cluster = result.clusters.get(entry.getKey());
			if (cluster == null || entry.getValue() == null)
				return invalid("cluster node membership is invalid: " + entry.getKey());
			for (String nodeId : entry.getValue()) {
				final NodeGeometry node = result.nodes.get(nodeId);
				if (node == null || contains(cluster.bounds, node.bounds) == false)
					return invalid("cluster does not contain direct node: " + entry.getKey() + " -> " + nodeId);
			}
		}
		return valid();
	}

	private static SvekLayoutValidation validateEdgeGeometry(SvekLayoutResult result, Set<String> visibleEdgeIds,
			Map<String, Set<LabelPosition>> requiredLabels) {
		final SvekLayoutValidation edgeIds = edgeIds(result, visibleEdgeIds, requiredLabels);
		if (edgeIds.isInvalid())
			return edgeIds;
		for (Map.Entry<String, EdgeGeometry> entry : result.edges.entrySet()) {
			final EdgeGeometry edge = entry.getValue();
			if (edge == null)
				return invalid("edge geometry is null: " + entry.getKey());
			if (entry.getKey() == null || entry.getKey().equals(edge.id) == false)
				return invalid("edge ID does not match map key: " + entry.getKey());
			final SvekLayoutValidation path = validatePath(entry.getKey(), edge, visibleEdgeIds);
			if (path.isInvalid())
				return path;
			final SvekLayoutValidation labels = validateLabels(entry.getKey(), edge, requiredLabels);
			if (labels.isInvalid())
				return labels;
		}
		return valid();
	}

	/** Labels are required for every laid out edge, so their key set is the full edge set. */
	private static SvekLayoutValidation edgeIds(SvekLayoutResult result, Set<String> visibleEdgeIds,
			Map<String, Set<LabelPosition>> requiredLabels) {
		for (String edgeId : result.edges.keySet())
			if (requiredLabels.containsKey(edgeId) == false)
				return invalid("unknown edge ID: " + edgeId);
		for (String edgeId : visibleEdgeIds)
			if (result.edges.containsKey(edgeId) == false)
				return invalid("missing visible edge ID: " + edgeId);
		return valid();
	}

	/** An invisible edge may be laid out without a path; a visible one may not. */
	private static SvekLayoutValidation validatePath(String id, EdgeGeometry edge, Set<String> visibleEdgeIds) {
		if (edge.path == null) {
			if (visibleEdgeIds.contains(id))
				return invalid("visible edge path is null: " + id);
			return valid();
		}
		final String failure = pathFailure(edge.path);
		if (failure != null)
			return invalid(failure + ": " + id);
		return valid();
	}

	private static SvekLayoutValidation validateLabels(String id, EdgeGeometry edge,
			Map<String, Set<LabelPosition>> requiredLabels) {
		final Set<LabelPosition> labels = requiredLabels.get(id);
		if (labels == null)
			return invalid("required labels are null: " + id);
		for (LabelPosition label : labels)
			if (label == null || finite(anchor(edge, label)) == false)
				return invalid(String.valueOf(label) + " label is missing or non-finite: " + id);
		return valid();
	}

	private static SvekLayoutValidation validatePolygon(String id, Bounds bounds, List<Point> points, Shape shape) {
		final int required = shape == Shape.OCTAGON ? 8 : shape == Shape.HEXAGON ? 6 : 0;
		if (required > 0 && points == null)
			return invalid("node polygon is required: " + id);
		if (points == null)
			return valid();
		if (required > 0 && points.size() != required)
			return invalid("node polygon must contain exactly " + required + " points: " + id);
		for (Point point : points) {
			if (finite(point) == false)
				return invalid("node polygon contains a null or non-finite point: " + id);
			if (point.x < bounds.minX - CONTAINMENT_TOLERANCE || point.x > bounds.maxX + CONTAINMENT_TOLERANCE
					|| point.y < bounds.minY - CONTAINMENT_TOLERANCE
					|| point.y > bounds.maxY + CONTAINMENT_TOLERANCE)
				return invalid("absolute polygon point is outside node bounds: " + id);
		}
		return valid();
	}

	private static SvekLayoutValidation exactIds(String type, Set<String> expected, Set<String> actual) {
		for (String id : expected)
			if (actual.contains(id) == false)
				return invalid("missing " + type + " ID: " + id);
		for (String id : actual)
			if (expected.contains(id) == false)
				return invalid("unknown " + type + " ID: " + id);
		return valid();
	}

	private static String pathFailure(Path path) {
		if (path.points == null)
			return "path contains null points";
		for (Point point : path.points)
			if (finite(point) == false)
				return "path contains null or non-finite points";
		// A cubic path is a start point followed by triples of control, control, end.
		if (path.cubic && (path.points.size() < 4 || (path.points.size() - 1) % 3 != 0))
			return "cubic path must contain 1 + 3n points";
		if (path.cubic == false && path.points.size() < 2)
			return "polyline path must contain at least two points";
		return null;
	}

	private static Point anchor(EdgeGeometry edge, LabelPosition position) {
		if (position == LabelPosition.MAIN)
			return edge.mainLabel;
		if (position == LabelPosition.TAIL)
			return edge.tailLabel;
		return edge.headLabel;
	}

	private static boolean contains(Bounds outer, Bounds inner) {
		return inner.minX >= outer.minX - CONTAINMENT_TOLERANCE
				&& inner.minY >= outer.minY - CONTAINMENT_TOLERANCE
				&& inner.maxX <= outer.maxX + CONTAINMENT_TOLERANCE
				&& inner.maxY <= outer.maxY + CONTAINMENT_TOLERANCE;
	}

	private static boolean validBounds(Bounds bounds) {
		return bounds != null && finite(bounds.minX) && finite(bounds.minY) && finite(bounds.maxX)
				&& finite(bounds.maxY) && bounds.width() > 0 && bounds.height() > 0;
	}

	private static boolean finite(Point point) {
		return point != null && finite(point.x) && finite(point.y);
	}

	private static boolean finite(double value) {
		return Double.isNaN(value) == false && Double.isInfinite(value) == false;
	}
}
