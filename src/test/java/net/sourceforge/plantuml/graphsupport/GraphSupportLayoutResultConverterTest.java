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
package net.sourceforge.plantuml.graphsupport;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.graphper.api.Graphviz;
import org.graphper.api.Line;
import org.graphper.api.Node;
import org.graphper.api.ext.RegularPolylinePropCalc;
import org.graphper.def.FlatPoint;
import org.graphper.draw.ClusterDrawProp;
import org.graphper.draw.DrawGraph;
import org.graphper.draw.LineDrawProp;
import org.graphper.draw.NodeDrawProp;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.svek.layout.SvekLayoutBuilder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.CellSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.GraphSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Routing;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Shape;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Path;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;

class GraphSupportLayoutResultConverterTest {
	private DrawGraph capturedDrawGraph;

	@Test
	void convertsGraphNodeAndBodyCellBounds() {
		final SvekLayoutBuilder builder = capturingBuilder(Routing.SPLINE);
		builder.node(node("plain", 72, 36, Shape.RECTANGLE));
		builder.node(new NodeSpec("shield", 20, 10, Shape.RECTANGLE, "body",
				asList(new CellSpec("body", 3, 2, 12, 6), new CellSpec("extra", 16, 0, 4, 10))));

		final SvekLayoutResponse response = builder.layout();

		assertTrue(response.isSuccess());
		assertTrue(response.result.graphBounds.width() > 0);
		assertTrue(response.result.graphBounds.height() > 0);
		assertBounds(response.result.nodes.get("plain"), 72, 36);
		assertBounds(response.result.nodes.get("shield"), 12, 6);
		final Node shield = node(capturedDrawGraph.getGraphviz(), "shield");
		final Node body = shield.nodeAttrs().getAssemble().getCells().iterator().next();
		final NodeDrawProp bodyProp = capturedDrawGraph.getNodeDrawProp(body);
		// The result is expressed relative to the graph's own top left corner.
		assertEquals(bodyProp.getLeftBorder() - originX(), response.result.nodes.get("shield").bounds.minX);
		assertEquals(bodyProp.getUpBorder() - originY(), response.result.nodes.get("shield").bounds.minY);
	}

	@Test
	void convertsSplinePathAndAllLabelTopLeftAnchors() {
		final SvekLayoutBuilder builder = capturingBuilder(Routing.SPLINE);
		builder.node(node("a", 8, 6, Shape.RECTANGLE));
		builder.node(node("b", 9, 7, Shape.RECTANGLE));
		builder.edge(EdgeSpec.builder("e", "a", "b").labels(asList(
				new LabelSpec(LabelPosition.MAIN, 13, 5),
				new LabelSpec(LabelPosition.TAIL, 3, 2),
				new LabelSpec(LabelPosition.HEAD, 4, 3))).build());

		final SvekLayoutResponse response = builder.layout();
		final EdgeGeometry edge = response.result.edges.get("e");

		assertTrue(edge.path.cubic);
		assertEquals("a", edge.tailId);
		assertEquals("b", edge.headId);
		assertEquals(Direction.TOP_TO_BOTTOM, response.result.direction);
		assertTrue(edge.path.points.size() >= 4);
		assertEquals(0, (edge.path.points.size() - 1) % 3);
		assertTailToHead(edge.path, response.result.nodes.get("a"), response.result.nodes.get("b"));
		assertFinite(edge.mainLabel);
		assertFinite(edge.tailLabel);
		assertFinite(edge.headLabel);
		final LineDrawProp lineProp = capturedDrawGraph.lines().iterator().next();
		assertTrue(lineProp.getFloatLabelFlatCenters().isEmpty(), "assembled labels are positioned through cells");
	}

	@Test
	void convertsPolylineAndOrthoPathsAsLineSegments() {
		for (Routing routing : asList(Routing.POLYLINE, Routing.ORTHO)) {
			final SvekLayoutBuilder builder = builder(routing);
			builder.node(node("a", 8, 6, Shape.RECTANGLE));
			builder.node(node("b", 9, 7, Shape.RECTANGLE));
			builder.edge(edge("e", true));

			final Path path = builder.layout().result.edges.get("e").path;

			assertFalse(path.cubic, routing.name());
			assertTrue(path.points.size() >= 2, routing.name());
		}
	}

	@Test
	void preservesEffectiveLeftToRightDirection() {
		final SvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(new GraphSpec(Direction.LEFT_TO_RIGHT, Routing.SPLINE, 18, 36));
		builder.node(node("a", 8, 6, Shape.RECTANGLE));

		assertEquals(Direction.LEFT_TO_RIGHT, builder.layout().result.direction);
	}

	@Test
	void convertsClusterTitleCenterToTopLeft() {
		final SvekLayoutBuilder builder = capturingBuilder(Routing.SPLINE);
		builder.beginCluster(ClusterSpec.builder("titled").title(14, 6).build());
		builder.node(node("inside", 8, 6, Shape.RECTANGLE));
		builder.endCluster();
		builder.beginCluster(ClusterSpec.builder("untitled").build());
		builder.node(node("other", 8, 6, Shape.RECTANGLE));
		builder.endCluster();

		final SvekLayoutResponse response = builder.layout();

		assertTrue(response.result.clusters.get("titled").bounds.width() > 0);
		assertFinite(response.result.clusters.get("titled").title);
		ClusterDrawProp prop = null;
		for (ClusterDrawProp candidate : capturedDrawGraph.clusters())
			if (candidate.getLabelCenter() != null)
				prop = candidate;
		assertNotNull(prop);
		assertEquals(prop.getLabelCenter().getX() - 7 - originX(), response.result.clusters.get("titled").title.x);
		assertEquals(prop.getLabelCenter().getY() - 3 - originY(), response.result.clusters.get("titled").title.y);
		assertNull(response.result.clusters.get("untitled").title);
	}

	@Test
	void nestedClusterDoesNotOverlapParentTitleFootprint() {
		final SvekLayoutBuilder builder = capturingBuilder(Routing.SPLINE);
		builder.beginCluster(ClusterSpec.builder("outer").title(90, 17).margins(20, 20).contentTopPadding(15).build());
		builder.beginCluster(ClusterSpec.builder("inner").title(64, 17).margins(20, 20).build());
		builder.node(node("inside", 100, 40, Shape.RECTANGLE));
		builder.endCluster();
		builder.endCluster();

		final SvekLayoutResult result = builder.layout().result;
		final ClusterGeometry outer = result.clusters.get("outer");
		final ClusterGeometry inner = result.clusters.get("inner");

		assertTrue(inner.bounds.minY - 10 >= outer.title.y + 17);
	}

	@Test
	void copiesExactGraphperOctagonAndHexagonPointsForNonSquareBounds() {
		final SvekLayoutBuilder builder = capturingBuilder(Routing.SPLINE);
		builder.node(node("oct", 80, 40, Shape.OCTAGON));
		builder.node(node("hex", 60, 30, Shape.HEXAGON));
		builder.node(node("diamond", 50, 20, Shape.DIAMOND));

		final SvekLayoutResponse response = builder.layout();
		final NodeGeometry octagon = response.result.nodes.get("oct");
		final NodeGeometry hexagon = response.result.nodes.get("hex");

		assertPolygonEqualsGraphper(octagon,
				capturedDrawGraph.getNodeDrawProp(node(capturedDrawGraph.getGraphviz(), "oct")));
		assertPolygonEqualsGraphper(hexagon,
				capturedDrawGraph.getNodeDrawProp(node(capturedDrawGraph.getGraphviz(), "hex")));
		assertNull(response.result.nodes.get("diamond").polygon);
	}

	@Test
	void keepsInvisibleEdgeWithNullablePath() {
		final SvekLayoutBuilder builder = builder(Routing.SPLINE);
		builder.node(node("a", 8, 6, Shape.RECTANGLE));
		builder.node(node("b", 9, 7, Shape.RECTANGLE));
		builder.edge(edge("hidden", false));

		final EdgeGeometry edge = builder.layout().result.edges.get("hidden");

		assertNotNull(edge);
		assertNull(edge.path);
	}

	@Test
	void productionConverterReadsHeadStartAndReversesCubicSegments() {
		final LineDrawProp prop = lineProp(true, point(0, 0), point(1, 1), point(2, 2), point(3, 3),
				point(4, 4), point(5, 5), point(6, 6));
		prop.setIsHeadStart(prop.getLine().head());

		final Path path = convert(prop).path;

		assertTrue(path.cubic);
		assertPoints(path.points, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0);
		assertThrows(UnsupportedOperationException.class, () -> path.points.add(new Point(7, 7)));
	}

	@Test
	void productionConverterReadsHeadStartAndReversesPolyline() {
		final LineDrawProp prop = lineProp(false, point(0, 1), point(2, 3), point(4, 5));
		prop.setIsHeadStart(prop.getLine().head());

		final Path path = convert(prop).path;

		assertFalse(path.cubic);
		assertPoints(path.points, 4, 5, 2, 3, 0, 1);
	}

	@Test
	void rejectsMalformedCubicAsInternalError() {
		final LineDrawProp prop = lineProp(true, point(0, 0), point(1, 1), point(2, 2));

		final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> convert(prop));
		assertTrue(exception.getMessage().contains("cubic"));
	}

	@Test
	void rejectsNonFinitePathCoordinatesAsInternalError() {
		final LineDrawProp prop = lineProp(false, point(0, 0), point(Double.NaN, 2));

		final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> convert(prop));
		assertTrue(exception.getMessage().contains("non-finite"));
	}

	private SvekLayoutBuilder capturingBuilder(Routing routing) {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder((drawGraph, build) -> {
			capturedDrawGraph = drawGraph;
			return build.convert(drawGraph);
		});
		builder.graph(new GraphSpec(Direction.TOP_TO_BOTTOM, routing, 18, 36));
		return builder;
	}

	private static SvekLayoutBuilder builder(Routing routing) {
		final SvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(new GraphSpec(Direction.TOP_TO_BOTTOM, routing, 18, 36));
		return builder;
	}

	private static NodeSpec node(String id, double width, double height, Shape shape) {
		return new NodeSpec(id, width, height, shape, null, emptyList());
	}

	private static EdgeSpec edge(String id, boolean visible) {
		return EdgeSpec.builder(id, "a", "b").visible(visible).build();
	}

	private static FlatPoint point(double x, double y) {
		return new FlatPoint(x, y);
	}

	private static LineDrawProp lineProp(boolean cubic, FlatPoint... points) {
		final Node tail = Node.builder().id("tail").build();
		final Node head = Node.builder().id("head").build();
		final Line line = Line.builder(tail, head).id("edge").build();
		final DrawGraph drawGraph = new DrawGraph(Graphviz.digraph().addLine(line).build());
		drawGraph.setLeftBorder(0);
		drawGraph.setUpBorder(0);
		drawGraph.setRightBorder(10);
		drawGraph.setDownBorder(10);
		final LineDrawProp prop = new LineDrawProp(line, line.lineAttrs(), drawGraph);
		if (cubic)
			prop.markIsBesselCurve();
		else
			prop.markIsLineSegment();
		for (FlatPoint point : points)
			prop.addAndNotRefreshDrawGraph(point);
		drawGraph.linePut(line, prop);
		return prop;
	}

	private static EdgeGeometry convert(LineDrawProp prop) {
		final DrawGraph drawGraph = propDrawGraph(prop);
		final Map<Line, String> lineIds = singletonMap(prop.getLine(), "edge");
		final GraphSupportSvekLayoutBuilder.BuildResult build = new GraphSupportSvekLayoutBuilder.BuildResult(null,
				Direction.TOP_TO_BOTTOM, emptyMap(), emptyMap(), lineIds, emptyMap(), emptyMap(), emptyMap(),
				emptyMap(), emptyMap());
		return GraphSupportLayoutResultConverter.convert(drawGraph, build).edges.get("edge");
	}

	private static DrawGraph propDrawGraph(LineDrawProp prop) {
		final DrawGraph drawGraph = new DrawGraph(Graphviz.digraph().addLine(prop.getLine()).build());
		drawGraph.setLeftBorder(0);
		drawGraph.setUpBorder(0);
		drawGraph.setRightBorder(10);
		drawGraph.setDownBorder(10);
		drawGraph.linePut(prop.getLine(), prop);
		return drawGraph;
	}

	private static void assertBounds(NodeGeometry node, double width, double height) {
		assertEquals(width, node.bounds.width());
		assertEquals(height, node.bounds.height());
	}

	private static void assertFinite(Point point) {
		assertNotNull(point);
		assertTrue(Double.isFinite(point.x));
		assertTrue(Double.isFinite(point.y));
	}

	private void assertPolygonEqualsGraphper(NodeGeometry node, NodeDrawProp prop) {
		assertNotEquals(prop.getHeight(), prop.getWidth());
		final RegularPolylinePropCalc calc = assertInstanceOf(RegularPolylinePropCalc.class,
				prop.nodeAttrs().getShape().getShapePropCalc());
		final List<FlatPoint> expected = calc.calcPoints(prop);
		assertEquals(expected.size(), node.polygon.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).getX() - originX(), node.polygon.get(i).x);
			assertEquals(expected.get(i).getY() - originY(), node.polygon.get(i).y);
		}
	}

	private double originX() {
		return capturedDrawGraph.getLeftBorder();
	}

	private double originY() {
		return capturedDrawGraph.getUpBorder();
	}

	private static Node node(Graphviz graphviz, String id) {
		for (Node node : graphviz.nodes())
			if (id.equals(node.nodeAttrs().getId()))
				return node;
		throw new AssertionError("Missing node " + id);
	}

	private static void assertTailToHead(Path path, NodeGeometry tail, NodeGeometry head) {
		final Point first = path.points.get(0);
		final Point last = path.points.get(path.points.size() - 1);
		assertTrue(distance(first, tail) < distance(first, head));
		assertTrue(distance(last, head) < distance(last, tail));
	}

	private static double distance(Point point, NodeGeometry node) {
		final double centerX = (node.bounds.minX + node.bounds.maxX) / 2.0;
		final double centerY = (node.bounds.minY + node.bounds.maxY) / 2.0;
		return Math.hypot(point.x - centerX, point.y - centerY);
	}

	private static void assertPoints(List<Point> points, double... coordinates) {
		assertEquals(coordinates.length / 2, points.size());
		for (int i = 0; i < points.size(); i++) {
			assertEquals(coordinates[i * 2], points.get(i).x);
			assertEquals(coordinates[i * 2 + 1], points.get(i).y);
		}
	}
}
