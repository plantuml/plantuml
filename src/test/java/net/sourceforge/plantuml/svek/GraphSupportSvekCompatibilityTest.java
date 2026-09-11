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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import net.sourceforge.plantuml.dot.GraphvizVersion;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Bounds;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;

@ResourceLock(value = GraphSupportIntegrationTestSupport.GRAPHVIZ_RESOURCE, mode = ResourceAccessMode.READ_WRITE)
class GraphSupportSvekCompatibilityTest {

	@TestFactory
	Stream<DynamicTest> rendersSupportedCompatibilityMatrixWithoutGraphviz() {
		return supportedCases().stream().map(testCase -> DynamicTest.dynamicTest(testCase.name, () -> {
			try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
				final String svg = fixture.render(testCase.source);

				for (String expected : testCase.expectedText)
					assertTrue(svg.contains(expected), expected);
				// Graphviz is unreachable and the pragma is explicit: no engine but graph-support
				// could have produced this image.
				assertFalse(svg.contains("Dot Executable"));
				assertFalse(svg.contains("layout declined"));
				final SvekLayoutResult result = fixture.layout(testCase.source);
				assertFinite(result);
				if (testCase.visiblePathsRequired)
					assertVisiblePaths(result);
			}
		}));
	}

	@Test
	void restoresGraphvizGlobals() throws Exception {
		final Object originalExecutable = GraphSupportIntegrationTestSupport.getGraphvizField("dotExecutable");
		final Object originalVersion = GraphSupportIntegrationTestSupport.getGraphvizField("dotVersion");
		@SuppressWarnings("unchecked")
		final Map<File, GraphvizVersion> originalCache =
				(Map<File, GraphvizVersion>) GraphSupportIntegrationTestSupport.getGraphvizField("map");
		final Map<File, GraphvizVersion> originalCacheContents =
				new HashMap<>(originalCache);
		final String sentinelExecutable = "/sentinel/dot";
		final String sentinelVersion = "sentinel-version";
		final Map<File, GraphvizVersion> cache = new HashMap<>();
		try {
			GraphSupportIntegrationTestSupport.setGraphvizField("dotExecutable", sentinelExecutable);
			GraphSupportIntegrationTestSupport.setGraphvizField("dotVersion", sentinelVersion);
			GraphSupportIntegrationTestSupport.setGraphvizField("map", cache);
			try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
				assertFalse(fixture.render(uml("class A", "class B", "A --> B")).contains("Dot Executable"));
			}
			assertEquals(sentinelExecutable, GraphSupportIntegrationTestSupport.getGraphvizField("dotExecutable"));
			assertEquals(sentinelVersion, GraphSupportIntegrationTestSupport.getGraphvizField("dotVersion"));
			assertSame(cache, GraphSupportIntegrationTestSupport.getGraphvizField("map"));
			assertTrue(cache.isEmpty());
		} finally {
			GraphSupportIntegrationTestSupport.setGraphvizField("dotExecutable", originalExecutable);
			GraphSupportIntegrationTestSupport.setGraphvizField("dotVersion", originalVersion);
			originalCache.clear();
			originalCache.putAll(originalCacheContents);
			GraphSupportIntegrationTestSupport.setGraphvizField("map", originalCache);
		}
	}

	@Test
	void capturesFiniteGeometryWithClusterContainmentAndRequiredLabelAnchors() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("package Group {", "class Source", "class Target",
				"Source \"tail\" --> \"head\" Target : main", "}"));

		assertFinite(result);
		assertEquals(1, result.clusters.size());
		final Bounds cluster = result.clusters.values().iterator().next().bounds;
		for (NodeGeometry node : result.nodes.values())
			assertContains(cluster, node.bounds);
		final EdgeGeometry edge = result.edges.values().iterator().next();
		assertNotNull(edge.mainLabel);
		assertNotNull(edge.tailLabel);
		assertNotNull(edge.headLabel);
	}

	@Test
	void nestedChildClusterIsContainedByParentCluster() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("package Outer {", "class A", "package Inner {",
				"class B", "}", "A --> B", "}"));
		assertEquals(2, result.clusters.size());
		final List<ClusterGeometry> clusters = new ArrayList<>(result.clusters.values());
		final Bounds first = clusters.get(0).bounds;
		final Bounds second = clusters.get(1).bounds;
		if (first.width() >= second.width() && first.height() >= second.height())
			assertContains(first, second);
		else
			assertContains(second, first);
	}

	@Test
	void htmlMemberCellPortRouteAssociatesWithEndpointNodes() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("class Source {", "outgoing", "}", "class Target {",
				"incoming", "}", "Source::outgoing --> Target::incoming"));
		final List<NodeGeometry> nodes = new ArrayList<>(result.nodes.values());
		final EdgeGeometry edge = result.edges.values().iterator().next();
		assertNotNull(edge.path);
		assertTrue(result.nodes.keySet().containsAll(Arrays.asList(edge.tailId, edge.headId)));
		assertTrue(edge.path.points.size() >= 2);
		assertEndpointAssociation(edge, nodes.get(0).bounds, nodes.get(1).bounds);
	}

	@Test
	void componentAndChenFixturesProduceExpectedNeutralTopology() throws Exception {
		final SvekLayoutResult component = captureGeometry(uml("component API", "component Worker",
				"API -right-> Worker : dispatch"));
		assertEquals(2, component.nodes.size());
		assertEquals(1, component.edges.size());

		final SvekLayoutResult chen = captureGeometry(chen("entity Person {", "}", "entity Team {", "}",
				"relationship Membership {", "}", "Person -N- Membership", "Membership -1- Team"));
		assertEquals(3, chen.nodes.size());
		assertEquals(2, chen.edges.size());
	}

	@Test
	void invisibleLayoutEdgeMayBeOmittedWhileVisibleRouteRemains() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("class A", "class B", "class C", "A -[hidden]-> B",
				"A --> C : visible"));
		assertTrue(result.edges.values().stream()
				.anyMatch(edge -> edge.path != null && edge.path.points.isEmpty() == false));
	}

	@Test
	void leftToRightGeometryProgressesPrimarilyOnXAxis() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("left to right direction", "class Left", "class Right",
				"Left --> Right"));
		assertEquals(Direction.LEFT_TO_RIGHT, result.direction);
		final List<NodeGeometry> nodes = new ArrayList<>(result.nodes.values());
		final Bounds left = nodes.get(0).bounds;
		final Bounds right = nodes.get(1).bounds;
		assertTrue(right.minX - left.minX > Math.abs(right.minY - left.minY));
	}

	@Test
	void horizontalLinkGeometryPlacesNodesOnTheSameRank() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("class Left", "class Right", "Left -right-> Right"));
		final List<NodeGeometry> nodes = new ArrayList<>(result.nodes.values());
		assertEquals(nodes.get(1).bounds.minY, nodes.get(0).bounds.minY);
	}

	@Test
	void orthogonalGeometryUsesAxisAlignedNeutralSegments() throws Exception {
		final String source = uml("skinparam linetype ortho", "class A", "class B", "A --> B : route");
		final SvekLayoutResult result = captureGeometry(source);
		final EdgeGeometry edge = result.edges.values().iterator().next();
		assertFalse(edge.path.cubic);
		for (int i = 1; i < edge.path.points.size(); i++) {
			final Point previous = edge.path.points.get(i - 1);
			final Point current = edge.path.points.get(i);
			assertTrue(current.x == previous.x || current.y == previous.y);
		}
		String svg;
		try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
			svg = fixture.render(source);
		}
		final Matcher path = Pattern.compile("<g class=\"link\".*?<path d=\"([^\"]+)\"", Pattern.DOTALL)
				.matcher(svg);
		assertTrue(path.find());
		final Matcher points = Pattern.compile("(?:M|C(?:\\s*[-0-9.]+,[-0-9.]+){2})\\s*([-0-9.]+),([-0-9.]+)")
				.matcher(path.group(1));
		double previousX = Double.NaN;
		double previousY = Double.NaN;
		while (points.find()) {
			final double x = Double.parseDouble(points.group(1));
			final double y = Double.parseDouble(points.group(2));
			if (Double.isNaN(previousX) == false)
				assertTrue(x == previousX || y == previousY);
			previousX = x;
			previousY = y;
		}
	}

	@Test
	void orthogonalEdgesMayConnectGroupsDirectly() throws Exception {
		final String source = uml("skinparam linetype ortho", "rectangle Left {", "  class A", "}",
				"rectangle Right {", "  class B", "}", "class Outside", "Left --> Right : group-group",
				"Right --> Outside : group-node");
		final String svg;
		final SvekLayoutResult result;
		try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
			svg = fixture.render(source);
			result = fixture.layout(source);
		}

		assertTrue(svg.contains("group-group"));
		assertTrue(svg.contains("group-node"));
		assertFalse(svg.contains("Dot Executable"));
		assertEquals(2, result.edges.size());
		result.edges.forEach((id, edge) -> {
			assertNotNull(edge.path);
			assertFalse(edge.path.cubic);
		});
	}

	@Test
	void polylineGeometryUsesNonCubicNeutralPaths() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("skinparam linetype polyline", "class A", "class B",
				"A --> B : route"));
		final EdgeGeometry edge = result.edges.values().iterator().next();
		assertFalse(edge.path.cubic);
		assertTrue(edge.path.points.size() >= 2);
	}

	@Test
	void simpleEdgePathTerminatesAtItsEndpointNodeBounds() throws Exception {
		final SvekLayoutResult result = captureGeometry(uml("class Source", "class Target", "Source --> Target"));
		final List<NodeGeometry> nodes = new ArrayList<>(result.nodes.values());
		final EdgeGeometry edge = result.edges.values().iterator().next();
		assertEquals(Direction.TOP_TO_BOTTOM, result.direction);
		assertTrue(result.nodes.keySet().containsAll(Arrays.asList(edge.tailId, edge.headId)));
		assertEndpointAssociation(edge, nodes.get(0).bounds, nodes.get(1).bounds);
	}

	@Test
	void edgeEnteringCompositeStateHasAnInwardHeadTangent() throws Exception {
		final String source = uml("[*] --> Draft", "Draft --> Review : submit", "state Review {",
				"[*] --> Automated", "Automated --> [*]", "}", "Review --> Draft : reject");
		final String svg;
		final SvekLayoutResult result;
		try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
			svg = fixture.render(source);
			result = fixture.layout(source);
		}
		// The composite state is drawn as one node, and it is the largest one of the outer graph.
		String reviewId = null;
		double reviewArea = -1;
		for (NodeGeometry node : result.nodes.values()) {
			final double area = node.bounds.width() * node.bounds.height();
			if (area > reviewArea) {
				reviewArea = area;
				reviewId = node.id;
			}
		}
		EdgeGeometry submit = null;
		for (EdgeGeometry edge : result.edges.values())
			if (reviewId.equals(edge.headId))
				submit = edge;
		assertNotNull(submit);
		final Bounds review = result.nodes.get(submit.headId).bounds;
		final List<Point> points = submit.path.points;
		final Point control = points.get(points.size() - 2);
		final Point end = points.get(points.size() - 1);
		final double beforeDistance = distance(control, review);
		final double endDistance = distance(end, review);

		assertTrue(endDistance < beforeDistance);
		final Matcher linkGroups = Pattern.compile("(?s)<g class=\"link\"[^>]*>.*?</g>").matcher(svg);
		String submitGroup = null;
		while (linkGroups.find())
			if (linkGroups.group().contains(">submit</text>")) {
				submitGroup = linkGroups.group();
				break;
			}
		assertNotNull(submitGroup);
		final Matcher path = Pattern.compile("<path d=\"([^\"]+)\"").matcher(submitGroup);
		assertTrue(path.find());
		final Matcher lastCurve = Pattern.compile(
				".*C\\s*[-0-9.]+,[-0-9.]+\\s+([-0-9.]+),([-0-9.]+)\\s+([-0-9.]+),([-0-9.]+)$")
				.matcher(path.group(1));
		assertTrue(lastCurve.matches());
		final double controlX = Double.parseDouble(lastCurve.group(1));
		final double controlY = Double.parseDouble(lastCurve.group(2));
		final double endX = Double.parseDouble(lastCurve.group(3));
		final double endY = Double.parseDouble(lastCurve.group(4));
		final Matcher reviewGroup = Pattern.compile(
				"(?s)<g class=\"entity\" data-qualified-name=\"Review\".*?<rect x=\"([-0-9.]+)\" y=\"([-0-9.]+)\" width=\"([-0-9.]+)\" height=\"([-0-9.]+)\"")
				.matcher(svg);
		assertTrue(reviewGroup.find());
		final double centerX = Double.parseDouble(reviewGroup.group(1))
				+ Double.parseDouble(reviewGroup.group(3)) / 2;
		final double centerY = Double.parseDouble(reviewGroup.group(2))
				+ Double.parseDouble(reviewGroup.group(4)) / 2;
		final double dot = (endX - controlX) * (centerX - endX) + (endY - controlY) * (centerY - endY);
		assertTrue(dot > 0);
	}

	@Test
	void selfLoopAndParallelEdgesHaveDistinctNonzeroRoutes() throws Exception {
		final SvekLayoutResult selfLoop = captureGeometry(uml("class \"Café 東京\" as Cafe", "Cafe --> Cafe : loop"));
		final EdgeGeometry loop = selfLoop.edges.values().iterator().next();
		assertTrue(extent(loop, true) > 0);
		assertTrue(extent(loop, false) > 0);

		final SvekLayoutResult parallel = captureGeometry(uml("class A", "class B", "A --> B : first",
				"A --> B : second"));
		assertEquals(2, parallel.edges.keySet().size());
		final List<EdgeGeometry> edges = new ArrayList<>(parallel.edges.values());
		assertNotEquals(points(edges.get(1)), points(edges.get(0)));
	}

	@Test
	void noteConnectorIsPreservedInGeometryAndFinalSvg() throws Exception {
		final String source = uml("state CustomerPage", "note left of CustomerPage", "  session details",
				"end note");
		final String svg;
		final SvekLayoutResult result;
		try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
			svg = fixture.render(source);
			result = fixture.layout(source);
		}

		assertEquals(1, result.edges.size());
		final EdgeGeometry connector = result.edges.values().iterator().next();
		assertNotNull(connector.path, String.format("tail=%s head=%s nodes=%s", connector.tailId, connector.headId,
				result.nodes.keySet()));
		assertTrue(svg.contains("session details"));
	}

	@Test
	void multiSegmentNoteConnectorUsesOpaleWhenDirectPathIsClear() throws Exception {
		final String source = uml(":User: as user", "(Load Balancer) as lb", "rectangle Servers {",
				"node Gateway as gateway", "node BackendOne as backend1", "node BackendTwo as backend2", "}",
				"user <-> lb", "lb <-> gateway", "gateway <..> backend1", "gateway <..> backend2",
				"backend1 <..> backend2", "note \"Long note rendered as an opale message\" as note1",
				"gateway .. note1");
		final String svg;
		final SvekLayoutResult result;
		try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
			svg = fixture.render(source);
			result = fixture.layout(source);
		}

		assertTrue(result.edges.values().stream()
				.anyMatch(edge -> edge.path != null && edge.path.points.size() > 4));
		assertEquals(5, count(svg, "class=\"link\""));
		assertTrue(svg.contains("Long note rendered as an opale message"));
	}

	private static List<CompatibilityCase> supportedCases() {
		return Arrays.asList(
				new CompatibilityCase("flat class with main, tail and head labels",
						uml("class Source", "class Target", "Source \"tail\" --> \"head\" Target : main"),
						new String[] { "Source", "Target", "main", "tail", "head" }),
				new CompatibilityCase("object", uml("object Request", "object Response", "Request --> Response"),
						new String[] { "Request", "Response" }),
				new CompatibilityCase("component directional labelled edges",
						uml("component API", "component Worker", "API -right-> Worker : dispatch"),
						new String[] { "API", "Worker", "dispatch" }),
				new CompatibilityCase("deployment nodes", uml("node Application", "node Database", "Application --> Database"),
						new String[] { "Application", "Database" }),
				new CompatibilityCase("actor and usecase", uml("actor User", "usecase Login", "User --> Login"),
						new String[] { "User", "Login" }),
				new CompatibilityCase("simple state", uml("state Idle", "state Running", "Idle --> Running"),
						new String[] { "Idle", "Running" }),
				new CompatibilityCase("state entry and exit ranks",
						uml("[*] --> Idle", "state Idle", "Idle --> [*]"),
						new String[] { "Idle" }),
				// The only link lives inside the composite state, which the render lays out as a graph
				// of its own, so the top-level graph is the single Outer node; the inner graph has the
				// shape of the "simple state" case above.
				new CompatibilityCase("nested state cluster",
						uml("state Outer {", "state Idle", "state Running", "Idle --> Running", "}"),
						new String[] { "Outer", "Idle", "Running" }, false),
				new CompatibilityCase("legacy activity",
						uml("(*) --> First", "First --> Second", "Second --> (*)"),
						new String[] { "First", "Second" }),
				new CompatibilityCase("Chen EER simple",
						chen("entity Person {", "}", "entity Team {", "}", "relationship Membership {", "}",
								"Person -N- Membership", "Membership -1- Team"),
						new String[] { "Person", "Team", "Membership" }),
				new CompatibilityCase("Chen EER left to right",
						chen("left to right direction", "entity Person {", "}", "entity Place {", "}",
								"relationship Birthplace {", "}", "Person -N- Birthplace", "Birthplace -1- Place"),
						new String[] { "Person", "Place", "Birthplace" }),
				new CompatibilityCase("nested package and component clusters",
						uml("package Platform {", "component API", "package Internal {", "component Worker", "}",
								"API --> Worker", "}"),
						new String[] { "Platform", "Internal", "API", "Worker" }),
				new CompatibilityCase("component ports",
						uml("component A {", "portout p1", "}", "component B {", "portin p2", "}", "p1 --> p2"),
						new String[] { "A", "B", "p1", "p2" }),
				new CompatibilityCase("HTML member cell ports",
						uml("class Source {", "outgoing", "}", "class Target {", "incoming", "}",
								"Source::outgoing --> Target::incoming : member route"),
						new String[] { "Source", "Target", "outgoing", "incoming", "member route" }),
				new CompatibilityCase("Unicode self-loop", uml("class \"Café 東京\" as Cafe", "Cafe --> Cafe : déjà vu"),
						new String[] { "Café 東京", "déjà vu" }),
				new CompatibilityCase("parallel edges remain renderable",
						uml("class A", "class B", "A --> B : first", "A --> B : second"),
						new String[] { "first", "second" }),
				new CompatibilityCase("orthogonal labelled edge",
						uml("skinparam linetype ortho", "class A", "class B", "A --> B : orthogonal"),
						new String[] { "A", "B", "orthogonal" }),
				new CompatibilityCase("polyline labelled edge",
						uml("skinparam linetype polyline", "class A", "class B", "A --> B : polyline"),
						new String[] { "A", "B", "polyline" }),
				new CompatibilityCase("left to right orientation",
						uml("left to right direction", "class Left", "class Right", "Left --> Right"),
						new String[] { "Left", "Right" }),
				new CompatibilityCase("invisible layout edge",
						uml("class A", "class B", "A -[hidden]-> B", "note top of A : visible"),
						new String[] { "A", "B", "visible" }, false),
				new CompatibilityCase("horizontal links generate same-rank semantics",
						uml("class Left", "class Right", "Left -right-> Right"),
						new String[] { "Left", "Right" }),
				new CompatibilityCase("direct group endpoint",
						uml("package A {", "class Inside", "}", "package B {", "class Other", "}", "A --> B"),
						new String[] { "A", "B", "Inside", "Other" }),
				new CompatibilityCase("constraint false norank edge",
						uml("class A", "class B", "class C", "A --> B", "B --> C", "C -[norank]-> A"),
						new String[] { "A", "B", "C" }),
				new CompatibilityCase("retained sametail",
						uml("skinparam groupInheritance 2", "class Parent", "class ChildOne", "class ChildTwo",
								"Parent <|-- ChildOne", "Parent <|-- ChildTwo"),
						new String[] { "Parent", "ChildOne", "ChildTwo" }),
				new CompatibilityCase("expanded component ports",
						uml("component A {", "portout customerEventsOutput", "}", "component B {",
								"portin customerEventsInput", "}", "customerEventsOutput --> customerEventsInput"),
						new String[] { "A", "B", "customerEventsOutput", "customerEventsInput" }),
				new CompatibilityCase("nested together groups",
						uml("class External", "together {", "class A", "together {", "class B", "class C", "}",
								"}", "External --> A", "B --> External"),
						new String[] { "External", "A", "B", "C" }),
				new CompatibilityCase("same-rank link inside together group",
						uml("together {", "class A", "class B", "}", "A -right-> B"),
						new String[] { "A", "B" }),
				new CompatibilityCase("automatic packed wrapper flattening",
						uml("!pragma useIntermediatePackages false", "package Outer {", "package Inner {", "class A",
								"class B", "A --> B", "}", "}"),
						new String[] { "A", "B" }),
				new CompatibilityCase("legacy activity swimlanes",
						uml("skinparam swimlane true", "partition Sales {", "(*) --> Receive", "}",
								"partition Finance {", "Receive --> Approve", "Approve --> (*)", "}"),
						new String[] { "Sales", "Finance", "Receive", "Approve" }));
	}

	private static String uml(String... body) {
		return source("@startuml", "@enduml", body);
	}

	private static int count(String text, String value) {
		int result = 0;
		for (int index = text.indexOf(value); index >= 0; index = text.indexOf(value, index + value.length()))
			result++;
		return result;
	}

	private static String chen(String... body) {
		return source("@startchen", "@endchen", body);
	}

	private static String source(String start, String end, String... body) {
		final StringBuilder result = new StringBuilder(start).append('\n').append("!pragma layout graph-support\n");
		for (String line : body)
			result.append(line).append('\n');
		return result.append(end).toString();
	}

	private static SvekLayoutResult captureGeometry(String source) throws Exception {
		try (GraphSupportIntegrationTestSupport fixture = new GraphSupportIntegrationTestSupport()) {
			return fixture.layout(source);
		}
	}

	private static void assertFinite(SvekLayoutResult result) {
		assertFinite(result.graphBounds);
		assertTrue(result.graphBounds.width() > 0);
		assertTrue(result.graphBounds.height() > 0);
		for (NodeGeometry node : result.nodes.values()) {
			assertFinite(node.bounds);
			assertTrue(node.bounds.width() > 0);
			assertTrue(node.bounds.height() > 0);
			if (node.polygon != null)
				for (Point point : node.polygon)
					assertFinite(point);
		}
		for (ClusterGeometry cluster : result.clusters.values()) {
			assertFinite(cluster.bounds);
			assertTrue(cluster.bounds.width() > 0);
			assertTrue(cluster.bounds.height() > 0);
			if (cluster.title != null)
				assertFinite(cluster.title);
		}
		for (EdgeGeometry edge : result.edges.values()) {
			if (edge.path != null)
				for (Point point : edge.path.points)
					assertFinite(point);
			if (edge.mainLabel != null)
				assertFinite(edge.mainLabel);
			if (edge.tailLabel != null)
				assertFinite(edge.tailLabel);
			if (edge.headLabel != null)
				assertFinite(edge.headLabel);
		}
	}

	private static double distance(Point point, Bounds bounds) {
		final double x = Math.max(bounds.minX - point.x, Math.max(0, point.x - bounds.maxX));
		final double y = Math.max(bounds.minY - point.y, Math.max(0, point.y - bounds.maxY));
		return Math.sqrt(x * x + y * y);
	}

	private static void assertEndpointAssociation(EdgeGeometry edge, Bounds firstNode, Bounds secondNode) {
		final Point first = edge.path.points.get(0);
		final Point last = edge.path.points.get(edge.path.points.size() - 1);
		assertTrue(distance(first, firstNode) + distance(last, secondNode)
				< distance(first, secondNode) + distance(last, firstNode));
	}

	private static void assertVisiblePaths(SvekLayoutResult result) {
		int edgeCount = 0;
		for (EdgeGeometry edge : result.edges.values()) {
			edgeCount++;
			assertNotNull(edge.path);
			assertFalse(edge.path.points.isEmpty());
		}
		assertTrue(edgeCount > 0);
	}

	private static void assertFinite(Bounds bounds) {
		assertTrue(Double.isFinite(bounds.minX));
		assertTrue(Double.isFinite(bounds.minY));
		assertTrue(Double.isFinite(bounds.maxX));
		assertTrue(Double.isFinite(bounds.maxY));
	}

	private static void assertFinite(Point point) {
		assertTrue(Double.isFinite(point.x));
		assertTrue(Double.isFinite(point.y));
	}

	private static void assertContains(Bounds outer, Bounds inner) {
		assertTrue(inner.minX >= outer.minX);
		assertTrue(inner.minY >= outer.minY);
		assertTrue(inner.maxX <= outer.maxX);
		assertTrue(inner.maxY <= outer.maxY);
	}

	private static double extent(EdgeGeometry edge, boolean xAxis) {
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (Point point : edge.path.points) {
			final double value = xAxis ? point.x : point.y;
			min = Math.min(min, value);
			max = Math.max(max, value);
		}
		return max - min;
	}

	private static List<String> points(EdgeGeometry edge) {
		final List<String> result = new ArrayList<>();
		for (Point point : edge.path.points)
			result.add(point.x + "," + point.y);
		return result;
	}

	private static final class CompatibilityCase {
		private final String name;
		private final String source;
		private final String[] expectedText;
		private final boolean visiblePathsRequired;

		private CompatibilityCase(String name, String source, String[] expectedText) {
			this(name, source, expectedText, true);
		}

		private CompatibilityCase(String name, String source, String[] expectedText, boolean visiblePathsRequired) {
			this.name = name;
			this.source = source;
			this.expectedText = expectedText;
			this.visiblePathsRequired = visiblePathsRequired;
		}
	}
}
