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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;
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

class SvekLayoutValidationTest {

	@Test
	void acceptsInvisibleEdgeOmittedOrPresentWithNullPath() {
		final Input omitted = validInput();
		omitted.requiredLabels.put("hidden", Collections.<LabelPosition>emptySet());
		assertTrue(omitted.validate().isValid());

		final Input present = validInput();
		present.requiredLabels.put("hidden", Collections.<LabelPosition>emptySet());
		present.edgeSpecs.put("hidden", EdgeSpec.builder("hidden", "tail", "head").visible(false).build());
		present.edges.put("hidden", new EdgeGeometry("hidden", "tail", "head", null, null, null, null));
		assertTrue(present.validate().isValid());
	}

	@Test
	void rejectsVisibleEdgeOmittedOrPresentWithNullPath() {
		final Input omitted = validInput();
		omitted.edges.remove("edge");
		assertTrue(omitted.validate().getMessage().contains("missing visible edge"));

		final Input nullPath = validInput();
		nullPath.edges.put("edge", new EdgeGeometry("edge", "tail", "head", null, null, null, null));
		assertTrue(nullPath.validate().getMessage().contains("visible edge path is null"));
	}

	@Test
	void rejectsMissingUnknownAndMismatchedNodeGeometry() {
		final Input missing = validInput();
		missing.nodes.clear();
		assertTrue(missing.validate().getMessage().contains("missing node"));

		final Input unknown = validInput();
		unknown.nodes.put("other", node("other", 1, 1, 4, 4));
		assertTrue(unknown.validate().getMessage().contains("unknown node"));

		final Input mismatch = validInput();
		mismatch.nodes.put("node", node("node", 2, 2, 6, 5));
		assertTrue(mismatch.validate().getMessage().contains("node dimensions"));

		final Input tolerance = validInput();
		tolerance.nodes.put("node", node("node", 2, 2, 5.005, 5.005));
		assertTrue(tolerance.validate().isValid());
	}

	@Test
	void rejectsUnknownClusterAndEdgeIds() {
		final Input cluster = validInput();
		cluster.clusters.put("other", cluster("other", 0, 0, 20, 20, null));
		assertTrue(cluster.validate().getMessage().contains("unknown cluster"));

		final Input edge = validInput();
		edge.edges.put("other", edge("other", cubicPath()));
		assertTrue(edge.validate().getMessage().contains("unknown edge"));
	}

	@Test
	void rejectsNonFiniteBadBoundsAndMalformedPaths() {
		final double[] values = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY };
		for (double value : values) {
			final Input graph = validInput();
			graph.graphBounds = new Bounds(value, 0, 100, 100);
			assertTrue(graph.validate().getMessage().contains("graph bounds"));
		}
		final Input badNode = validInput();
		badNode.nodes.put("node", node("node", 2, 2, 2, 5));
		assertTrue(badNode.validate().getMessage().contains("node bounds"));

		final Input cubic = validInput();
		cubic.edges.put("edge", edge("edge", new Path(true, asList(new Point(0, 0), new Point(1, 1)))));
		assertTrue(cubic.validate().getMessage().contains("cubic"));

		final Input polyline = validInput();
		polyline.edges.put("edge", edge("edge", new Path(false, Collections.singletonList(new Point(0, 0)))));
		assertTrue(polyline.validate().getMessage().contains("polyline"));
	}

	@Test
	void returnsInvalidForNullGeometryAndExpectationEntries() {
		final Input geometry = validInput();
		geometry.nodes.put("node", null);
		assertTrue(geometry.validate().getMessage().contains("node geometry is null"));

		final Input nodeSpec = validInput();
		nodeSpec.nodeSpecs.put("node", null);
		assertTrue(nodeSpec.validate().getMessage().contains("node expectation is null"));

		final Input clusterSpec = validInput();
		clusterSpec.clusterSpecs.put("cluster", null);
		assertTrue(clusterSpec.validate().getMessage().contains("cluster expectation is null"));
	}

	@Test
	void validatesDirectNestedAndRootContainment() {
		final Input direct = validInput();
		direct.nodes.put("node", node("node", -0.01, 2, 2.99, 5));
		assertTrue(direct.validate().getMessage().contains("direct node"));

		final Input nested = nestedInput();
		nested.clusters.put("child", cluster("child", -0.01, 1, 5, 5, null));
		assertTrue(nested.validate().getMessage().contains("child cluster"));

		final Input rootNode = validInput();
		rootNode.clusterNodeIds.get("cluster").clear();
		rootNode.rootNodeIds.add("node");
		rootNode.nodes.put("node", node("node", 99, 2, 102, 5));
		final String nodeMessage = rootNode.validate().getMessage();
		assertTrue(nodeMessage.contains("graph bounds"));
		assertTrue(nodeMessage.contains("node"));

		final Input rootCluster = validInput();
		rootCluster.rootClusterIds.add("cluster");
		rootCluster.clusters.put("cluster", cluster("cluster", 0, 0, 101, 20, null));
		final String clusterMessage = rootCluster.validate().getMessage();
		assertTrue(clusterMessage.contains("graph bounds"));
		assertTrue(clusterMessage.contains("cluster"));
	}

	@Test
	void validatesRequiredPolygonCardinalityAndAbsoluteBounds() {
		final Input missing = validInput();
		missing.nodeSpecs.put("node", nodeSpec("node", 3, 3, Shape.OCTAGON));
		final String missingMessage = missing.validate().getMessage();
		assertTrue(missingMessage.contains("polygon"));
		assertTrue(missingMessage.contains("required"));

		final Input wrongCount = polygonInput(Shape.OCTAGON, 6);
		assertTrue(wrongCount.validate().getMessage().contains("8 points"));

		final Input hexagon = polygonInput(Shape.HEXAGON, 6);
		assertTrue(hexagon.validate().isValid());

		final Input outside = polygonInput(Shape.OCTAGON, 8);
		outside.nodes.put("node", new NodeGeometry("node", new Bounds(2, 2, 5, 5),
				asList(new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2),
						new Point(5, 3), new Point(5, 4), new Point(4, 5), new Point(2, 5))));
		final String outsideMessage = outside.validate().getMessage();
		assertTrue(outsideMessage.contains("polygon point"));
		assertTrue(outsideMessage.contains("bounds"));

		final Input optional = validInput();
		optional.nodes.put("node", new NodeGeometry("node", new Bounds(2, 2, 5, 5),
				asList(new Point(2, 2), new Point(5, 2), new Point(5, 5), new Point(2, 5))));
		assertTrue(optional.validate().isValid());
	}

	@Test
	void validatesRequiredClusterTitleRectangle() {
		final Input missing = validInput();
		missing.clusterSpecs.put("cluster", ClusterSpec.builder("cluster").title(8, 4).build());
		final String missingMessage = missing.validate().getMessage();
		assertTrue(missingMessage.contains("title"));
		assertTrue(missingMessage.contains("missing"));

		final Input outside = validInput();
		outside.clusterSpecs.put("cluster", ClusterSpec.builder("cluster").title(8, 4).build());
		outside.clusters.put("cluster", cluster("cluster", 0, 0, 20, 20, new Point(15, 18)));
		final String outsideMessage = outside.validate().getMessage();
		assertTrue(outsideMessage.contains("title rectangle"));
		assertTrue(outsideMessage.contains("contained"));

		final Input inside = validInput();
		inside.clusterSpecs.put("cluster", ClusterSpec.builder("cluster").title(8, 4).build());
		inside.clusters.put("cluster", cluster("cluster", 0, 0, 20, 20, new Point(3, 4)));
		assertTrue(inside.validate().isValid());
	}

	@Test
	void rejectsDirectChildrenInsideClusterTitleProtectionArea() {
		final Input nodeOverlap = validInput();
		nodeOverlap.clusterSpecs.put("cluster",
				ClusterSpec.builder("cluster").title(8, 4).contentTopPadding(5).build());
		nodeOverlap.clusters.put("cluster", cluster("cluster", 0, 0, 20, 20, new Point(3, 1)));
		nodeOverlap.nodes.put("node", node("node", 2, 8, 5, 11));
		final String nodeMessage = nodeOverlap.validate().getMessage();
		assertTrue(nodeMessage.contains("title protection"));
		assertTrue(nodeMessage.contains("node"));

		final Input childOverlap = nestedInput();
		childOverlap.clusterSpecs.put("cluster",
				ClusterSpec.builder("cluster").title(8, 4).contentTopPadding(5).build());
		childOverlap.clusters.put("cluster", cluster("cluster", 0, 0, 20, 20, new Point(3, 1)));
		childOverlap.clusters.put("child", cluster("child", 1, 8, 5, 12, null));
		final String clusterMessage = childOverlap.validate().getMessage();
		assertTrue(clusterMessage.contains("title protection"));
		assertTrue(clusterMessage.contains("cluster"));

		final Input separated = validInput();
		separated.clusterSpecs.put("cluster", ClusterSpec.builder("cluster").title(8, 4).contentTopPadding(5).build());
		separated.clusters.put("cluster", cluster("cluster", 0, 0, 20, 20, new Point(3, 1)));
		separated.nodes.put("node", node("node", 2, 10, 5, 13));
		assertTrue(separated.validate().isValid());
	}

	@Test
	void validatesRequiredLabels() {
		for (LabelPosition position : LabelPosition.values()) {
			final Input input = validInput();
			input.requiredLabels.put("edge", set(position));
			final String message = input.validate().getMessage();
			assertTrue(message.contains(position.name()));
			assertTrue(message.contains("label"));
		}
	}

	@Test
	void rejectsSwappedEdgeEndpoints() {
		final Input input = validInput();
		input.edges.put("edge", new EdgeGeometry("edge", "head", "tail", cubicPath(), null, null, null));

		final String message = input.validate().getMessage();
		assertTrue(message.contains("endpoint"));
		assertTrue(message.contains("edge"));
	}

	@Test
	void rejectsResultDirectionDifferentFromExpectedDirection() {
		final Input input = validInput();
		input.direction = Direction.LEFT_TO_RIGHT;

		assertTrue(input.validate().getMessage().contains("direction"));
	}

	private static Input polygonInput(Shape shape, int count) {
		final Input input = validInput();
		input.nodeSpecs.put("node", nodeSpec("node", 3, 3, shape));
		final java.util.List<Point> points = new java.util.ArrayList<>();
		for (int i = 0; i < count; i++)
			points.add(new Point(2 + i % 3, 2 + i / 3));
		input.nodes.put("node", new NodeGeometry("node", new Bounds(2, 2, 5, 5), points));
		return input;
	}

	private static Input nestedInput() {
		final Input input = validInput();
		input.clusterSpecs.put("child", ClusterSpec.builder("child").build());
		input.clusters.put("child", cluster("child", 1, 1, 5, 5, null));
		input.clusterClusterIds.put("cluster", set("child"));
		input.clusterClusterIds.put("child", Collections.<String>emptySet());
		input.clusterNodeIds.put("child", Collections.<String>emptySet());
		return input;
	}

	private static Input validInput() {
		final Input input = new Input();
		input.graphBounds = new Bounds(0, 0, 100, 100);
		input.nodeSpecs.put("node", nodeSpec("node", 3, 3, Shape.RECTANGLE));
		input.clusterSpecs.put("cluster", ClusterSpec.builder("cluster").build());
		input.nodes.put("node", node("node", 2, 2, 5, 5));
		input.clusters.put("cluster", cluster("cluster", 0, 0, 20, 20, null));
		input.edges.put("edge", edge("edge", cubicPath()));
		input.edgeSpecs.put("edge", EdgeSpec.builder("edge", "tail", "head").build());
		input.clusterNodeIds.put("cluster", set("node"));
		input.clusterClusterIds.put("cluster", Collections.<String>emptySet());
		input.visibleEdgeIds.add("edge");
		input.requiredLabels.put("edge", Collections.<LabelPosition>emptySet());
		return input;
	}

	private static NodeSpec nodeSpec(String id, double width, double height, Shape shape) {
		return new NodeSpec(id, width, height, shape, null,
				Collections.<SvekLayoutModel.CellSpec>emptyList());
	}

	private static NodeGeometry node(String id, double minX, double minY, double maxX, double maxY) {
		return new NodeGeometry(id, new Bounds(minX, minY, maxX, maxY), null);
	}

	private static ClusterGeometry cluster(String id, double minX, double minY, double maxX, double maxY,
			Point title) {
		return new ClusterGeometry(id, new Bounds(minX, minY, maxX, maxY), title);
	}

	private static EdgeGeometry edge(String id, Path path) {
		return new EdgeGeometry(id, "tail", "head", path, null, null, null);
	}

	private static Path cubicPath() {
		return new Path(true, asList(new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)));
	}

	@SafeVarargs
	private static <T> Set<T> set(T... values) {
		return new LinkedHashSet<>(asList(values));
	}

	private static final class Input {
		private Bounds graphBounds;
		private Direction direction = Direction.TOP_TO_BOTTOM;
		private final Map<String, NodeGeometry> nodes = new LinkedHashMap<>();
		private final Map<String, ClusterGeometry> clusters = new LinkedHashMap<>();
		private final Map<String, EdgeGeometry> edges = new LinkedHashMap<>();
		private final Map<String, NodeSpec> nodeSpecs = new LinkedHashMap<>();
		private final Map<String, ClusterSpec> clusterSpecs = new LinkedHashMap<>();
		private final Map<String, EdgeSpec> edgeSpecs = new LinkedHashMap<>();
		private final Map<String, Set<String>> clusterNodeIds = new LinkedHashMap<>();
		private final Map<String, Set<String>> clusterClusterIds = new LinkedHashMap<>();
		private final Set<String> rootNodeIds = new LinkedHashSet<>();
		private final Set<String> rootClusterIds = new LinkedHashSet<>();
		private final Set<String> visibleEdgeIds = new LinkedHashSet<>();
		private final Map<String, Set<LabelPosition>> requiredLabels = new LinkedHashMap<>();

		private SvekLayoutValidation validate() {
			return SvekLayoutValidation.validate(new SvekLayoutResult(graphBounds, direction, nodes, clusters, edges),
					expectations());
		}

		private SvekLayoutExpectations expectations() {
			final SvekLayoutExpectations expected = new SvekLayoutExpectations();
			expected.setDirection(Direction.TOP_TO_BOTTOM);
			expected.nodes.putAll(nodeSpecs);
			expected.clusters.putAll(clusterSpecs);
			expected.edges.putAll(edgeSpecs);
			expected.clusterNodeIds.putAll(clusterNodeIds);
			expected.clusterClusterIds.putAll(clusterClusterIds);
			expected.rootNodeIds.addAll(rootNodeIds);
			expected.rootClusterIds.addAll(rootClusterIds);
			expected.visibleEdgeIds.addAll(visibleEdgeIds);
			expected.requiredLabels.putAll(requiredLabels);
			return expected;
		}
	}
}
