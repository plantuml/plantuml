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
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.graphper.api.Cluster;
import org.graphper.api.Graphviz;
import org.graphper.api.Line;
import org.graphper.api.Node;
import org.graphper.api.Subgraph;
import org.graphper.api.attributes.Rankdir;
import org.graphper.api.attributes.Splines;
import org.graphper.api.attributes.Dir;
import org.graphper.api.attributes.LineStyle;
import org.graphper.api.attributes.Labeljust;
import org.graphper.api.attributes.NodeShapeEnum;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.svek.layout.SvekLayoutBuilder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Alignment;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.CellSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.GraphSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Rank;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.RankSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Routing;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Shape;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;

class GraphSupportSvekLayoutBuilderTest {

	@Test
	void laysOutSimpleTbSplineGraphWithStableIds() {
		final SvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("a"));
		builder.node(node("b"));
		builder.edge(edge("ab", "a", "b", null, null));

		final SvekLayoutResponse response = builder.layout();

		assertTrue(response.isSuccess());
		assertEquals(new HashSet<>(asList("a", "b")), response.result.nodes.keySet());
		assertEquals(new HashSet<>(asList("ab")), response.result.edges.keySet());
		assertTrue(response.result.graphBounds.width() > 0);
		assertEquals(72.0, response.result.nodes.get("a").bounds.width());
		assertEquals(36.0, response.result.nodes.get("a").bounds.height());
	}

	@Test
	void convertsNeutralPixelsExactlyOnce() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(new GraphSpec(Direction.LEFT_TO_RIGHT, Routing.POLYLINE, 36, 72));
		builder.node(node("a"));

		final Graphviz graphviz = builder.buildGraphviz();
		final Node node = graphviz.directNodes().iterator().next();

		assertEquals(Rankdir.LR, graphviz.graphAttrs().getRankdir());
		assertEquals(Splines.POLYLINE, graphviz.graphAttrs().getSplines());
		assertEquals(36.0, graphviz.graphAttrs().getNodeSep());
		assertEquals(72.0, graphviz.graphAttrs().getRankSep());
		assertEquals(72.0, node.nodeAttrs().getWidth());
		assertEquals(36.0, node.nodeAttrs().getHeight());
		assertEquals(Boolean.TRUE, node.nodeAttrs().getFixedSize());
	}

	@Test
	void mapsDefaultPlantUmlSplineRoutingToRoundedGraphperLines() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());

		assertEquals(Splines.ROUNDED, builder.buildGraphviz().graphAttrs().getSplines());
	}

	@Test
	void declinesInvalidNeutralInputInsteadOfThrowing() {
		assertDeclined(builderWithDuplicateNode(), "invalid-input");

		final SvekLayoutBuilder unbalanced = new GraphSupportSvekLayoutBuilder();
		unbalanced.graph(graph());
		unbalanced.beginCluster(ClusterSpec.builder("c").title(10, 10).build());
		assertDeclined(unbalanced, "invalid-input");

		final SvekLayoutBuilder unknownEndpoint = new GraphSupportSvekLayoutBuilder();
		unknownEndpoint.graph(graph());
		unknownEndpoint.node(node("a"));
		unknownEndpoint.edge(edge("ab", "a", "missing", null, null));
		assertDeclined(unknownEndpoint, "unknown-endpoint");

		final SvekLayoutBuilder unknownCell = new GraphSupportSvekLayoutBuilder();
		unknownCell.graph(graph());
		unknownCell.node(nodeWithCell("a", "h"));
		unknownCell.node(node("b"));
		unknownCell.edge(edge("ab", "a", "b", "missing", null));
		assertDeclined(unknownCell, "unknown-cell");

		final SvekLayoutBuilder invalidOrder = new GraphSupportSvekLayoutBuilder();
		invalidOrder.node(node("a"));
		invalidOrder.graph(graph());
		assertDeclined(invalidOrder, "invalid-order");

		final SvekLayoutBuilder graphTwice = new GraphSupportSvekLayoutBuilder();
		graphTwice.graph(graph());
		graphTwice.graph(graph());
		assertDeclined(graphTwice, "graph-already-set");
	}

	@Test
	void internalCellIdsCannotAliasNeutralOrDelimitedIds() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("__plantuml_internal_0"));
		builder.node(node("a:h"));
		builder.beginCluster(ClusterSpec.builder("__plantuml_internal_1").title(10, 10).build());
		builder.node(new NodeSpec("a", 72, 36, Shape.RECTANGLE, "h",
				asList(new CellSpec("h", 0, 0, 36, 36), new CellSpec("b:c", 36, 0, 36, 36))));
		builder.endCluster();
		builder.node(nodeWithCell("a:b", "c"));
		builder.edge(edge("__plantuml_internal_2", "a:b", "a", "c", "b:c"));

		final Graphviz graphviz = builder.buildGraphviz();
		final Line line = graphviz.directLines().iterator().next();
		final java.util.List<String> graphperIds = new java.util.ArrayList<>();
		for (Node outer : graphviz.nodes()) {
			graphperIds.add(outer.nodeAttrs().getId());
			if (outer.nodeAttrs().getAssemble() != null)
				for (Node cell : outer.nodeAttrs().getAssemble().getCells())
					graphperIds.add(cell.nodeAttrs().getId());
		}

		assertEquals(graphperIds.size(), new HashSet<>(graphperIds).size());
		assertNotEquals(line.lineAttrs().getHeadCell(), line.lineAttrs().getTailCell());
		assertFalse(line.lineAttrs().getTailCell().contains("a:b:c"));
		assertFalse(asList("__plantuml_internal_0", "__plantuml_internal_1", "__plantuml_internal_2")
				.contains(line.lineAttrs().getTailCell()));
		assertFalse(asList("__plantuml_internal_0", "__plantuml_internal_1", "__plantuml_internal_2")
				.contains(line.lineAttrs().getHeadCell()));
		final SvekLayoutResponse response = builder.layout();
		assertTrue(response.isSuccess());
		assertEquals(new HashSet<>(asList("__plantuml_internal_0", "a:h", "a", "a:b")),
				response.result.nodes.keySet());
		assertEquals(new HashSet<>(asList("__plantuml_internal_1")), response.result.clusters.keySet());
		assertEquals(new HashSet<>(asList("__plantuml_internal_2")), response.result.edges.keySet());
		response.result.nodes.values().forEach(geometry -> assertNotNull(geometry.bounds));
	}

	@Test
	void cellBackedNodeUsesPlainFixedOuterSize() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(new NodeSpec("small", 20, 10, Shape.DIAMOND, "body",
				singletonList(new CellSpec("body", 0, 0, 20, 10))));

		final Node outer = builder.buildGraphviz().directNodes().iterator().next();
		final Node bodyCell = outer.nodeAttrs().getAssemble().getCells().iterator().next();

		assertEquals(NodeShapeEnum.PLAIN, outer.nodeAttrs().getShape());
		assertEquals(NodeShapeEnum.RECT, bodyCell.nodeAttrs().getShape());
		assertEquals(20.0, outer.nodeAttrs().getWidth());
		assertEquals(10.0, outer.nodeAttrs().getHeight());
		assertEquals(Boolean.TRUE, outer.nodeAttrs().getFixedSize());
	}

	@Test
	void allMalformedRecordingsDeclineAsInvalidInput() {
		final SvekLayoutBuilder crossKind = builder();
		crossKind.node(node("same"));
		crossKind.edge(edge("same", "same", "same", null, null));
		assertDeclined(crossKind, "invalid-input");

		final SvekLayoutBuilder duplicateCells = builder();
		duplicateCells.node(new NodeSpec("a", 20, 10, Shape.RECTANGLE, "h",
				asList(new CellSpec("h", 0, 0, 10, 10), new CellSpec("h", 10, 0, 10, 10))));
		assertDeclined(duplicateCells, "invalid-input");

		final SvekLayoutBuilder missingBody = builder();
		missingBody.node(new NodeSpec("a", 20, 10, Shape.RECTANGLE, "missing",
				singletonList(new CellSpec("h", 0, 0, 20, 10))));
		assertDeclined(missingBody, "invalid-input");

		final SvekLayoutBuilder duplicateEdge = builder();
		duplicateEdge.node(node("a"));
		duplicateEdge.edge(edge("e", "a", "a", null, null));
		duplicateEdge.edge(edge("e", "a", "a", null, null));
		assertDeclined(duplicateEdge, "invalid-input");

		final SvekLayoutBuilder duplicateCluster = builder();
		duplicateCluster.beginCluster(ClusterSpec.builder("c").title(10, 10).build());
		duplicateCluster.endCluster();
		duplicateCluster.beginCluster(ClusterSpec.builder("c").title(10, 10).build());
		duplicateCluster.endCluster();
		assertDeclined(duplicateCluster, "invalid-input");

		final SvekLayoutBuilder nullLabel = builder();
		nullLabel.node(node("a"));
		nullLabel.edge(EdgeSpec.builder("e", "a", "a").labels(singletonList(null)).build());
		assertDeclined(nullLabel, "invalid-input");

		final SvekLayoutBuilder invalidRank = builder();
		invalidRank.node(node("a"));
		invalidRank.rankGroup(new RankSpec(Rank.SAME, singletonList("missing")));
		assertDeclined(invalidRank, "invalid-input");

		final SvekLayoutBuilder extraEnd = builder();
		extraEnd.endCluster();
		assertDeclined(extraEnd, "invalid-input");
	}

	@Test
	void declinesInvalidClusterTitleDimensions() {
		for (double width : asList(Double.NaN, Double.POSITIVE_INFINITY, -1.0)) {
			final SvekLayoutBuilder builder = builder();
			builder.beginCluster(ClusterSpec.builder("c").title(width, 10).build());
			builder.endCluster();
			assertDeclined(builder, "invalid-input");
		}
		for (double height : asList(Double.NaN, Double.POSITIVE_INFINITY, -1.0)) {
			final SvekLayoutBuilder builder = builder();
			builder.beginCluster(ClusterSpec.builder("c").title(10, height).build());
			builder.endCluster();
			assertDeclined(builder, "invalid-input");
		}
	}

	@Test
	void laysOutCellBackedRegularPolygons() {
		for (Shape shape : asList(Shape.OCTAGON, Shape.HEXAGON)) {
			final SvekLayoutBuilder builder = builder();
			builder.node(new NodeSpec(shape.name(), 72, 36, shape, "body",
					singletonList(new CellSpec("body", 0, 0, 72, 36))));

			final SvekLayoutResponse response = builder.layout();

			assertTrue(response.isSuccess(), shape.name());
			assertEquals(shape == Shape.OCTAGON ? 8 : 6,
					response.result.nodes.get(shape.name()).polygon.size(), shape.name());
		}
	}

	@Test
	void laysOutRegularPolygonsWithoutCells() {
		for (Shape shape : asList(Shape.OCTAGON, Shape.HEXAGON)) {
			final SvekLayoutBuilder builder = builder();
			builder.node(new NodeSpec(shape.name(), 72, 36, shape, null, emptyList()));

			assertTrue(builder.layout().isSuccess(), shape.name());
		}
	}

	@Test
	void clusterRankOnlyAcceptsDirectlyOwnedNodes() {
		final SvekLayoutBuilder sibling = builder();
		sibling.node(node("root"));
		sibling.beginCluster(ClusterSpec.builder("left").title(10, 10).build());
		sibling.node(node("leftNode"));
		sibling.endCluster();
		sibling.beginCluster(ClusterSpec.builder("right").title(10, 10).build());
		sibling.node(node("rightNode"));
		sibling.rankGroup(new RankSpec(Rank.SAME, singletonList("leftNode")));
		sibling.endCluster();
		assertDeclined(sibling, "invalid-input");

		final SvekLayoutBuilder root = builder();
		root.node(node("root"));
		root.beginCluster(ClusterSpec.builder("c").title(10, 10).build());
		root.node(node("inside"));
		root.rankGroup(new RankSpec(Rank.SAME, singletonList("root")));
		root.endCluster();
		assertDeclined(root, "invalid-input");

		final SvekLayoutBuilder direct = builder();
		direct.beginCluster(ClusterSpec.builder("c").title(10, 10).build());
		direct.node(node("inside"));
		direct.rankGroup(new RankSpec(Rank.SAME, singletonList("inside")));
		direct.endCluster();
		assertTrue(direct.layout().isSuccess());
	}

	@Test
	void assembledTailAndHeadLabelsReturnCellGeometry() {
		final SvekLayoutBuilder builder = builder();
		builder.node(node("a"));
		builder.node(node("b"));
		builder.edge(EdgeSpec.builder("e", "a", "b")
				.labels(asList(new LabelSpec(LabelPosition.TAIL, 17, 9), new LabelSpec(LabelPosition.HEAD, 19, 11)))
				.build());
		final Line line = ((GraphSupportSvekLayoutBuilder) builder).buildGraphviz().directLines().iterator().next();
		final Node tailCell = line.lineAttrs().getFloatLabels()[0].getAssemble().getCells().iterator().next();
		final Node headCell = line.lineAttrs().getFloatLabels()[1].getAssemble().getCells().iterator().next();

		final SvekLayoutResponse response = builder.layout();

		assertEquals(17.0, tailCell.nodeAttrs().getWidth());
		assertEquals(9.0, tailCell.nodeAttrs().getHeight());
		assertEquals(NodeShapeEnum.RECT, tailCell.nodeAttrs().getShape());
		assertEquals(19.0, headCell.nodeAttrs().getWidth());
		assertEquals(11.0, headCell.nodeAttrs().getHeight());
		assertEquals(NodeShapeEnum.RECT, headCell.nodeAttrs().getShape());
		assertNotEquals(headCell.nodeAttrs().getId(), tailCell.nodeAttrs().getId());
		assertTrue(response.isSuccess());
		assertNotNull(response.result.edges.get("e").tailLabel);
		assertNotNull(response.result.edges.get("e").headLabel);
		assertTrue(Double.isFinite(response.result.edges.get("e").tailLabel.x));
		assertTrue(Double.isFinite(response.result.edges.get("e").tailLabel.y));
		assertTrue(Double.isFinite(response.result.edges.get("e").headLabel.x));
		assertTrue(Double.isFinite(response.result.edges.get("e").headLabel.y));
	}

	@Test
	void unexpectedConverterFailurePropagates() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder((drawGraph, build) -> {
			throw new IllegalStateException("converter bug");
		});
		builder.graph(graph());
		builder.node(node("a"));

		final IllegalStateException exception = assertThrows(IllegalStateException.class, builder::layout);
		assertEquals("converter bug", exception.getMessage());
	}

	@Test
	void scopesSameNeutralCellIdByEndpointNode() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(nodeWithCell("a", "h"));
		builder.node(nodeWithCell("b", "h"));
		builder.edge(edge("ab", "a", "b", "h", "h"));

		final Graphviz graphviz = builder.buildGraphviz();
		final Line line = graphviz.directLines().iterator().next();

		assertNotEquals(line.lineAttrs().getHeadCell(), line.lineAttrs().getTailCell());
		final List<String> cellIds = stream(graphviz.directNodes().spliterator(), false)
				.flatMap(node -> stream(node.nodeAttrs().getAssemble().getCells().spliterator(), false))
				.map(node -> node.nodeAttrs().getId()).collect(toList());
		assertTrue(cellIds.containsAll(asList(line.lineAttrs().getTailCell(), line.lineAttrs().getHeadCell())));
	}

	@Test
	void mapsStableInvisibleEdgeSemantics() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("a"));
		builder.node(node("b"));
		builder.edge(EdgeSpec.builder("ab", "a", "b").minlen(3).visible(false).build());

		final Line line = builder.buildGraphviz().directLines().iterator().next();

		assertEquals("ab", line.lineAttrs().getId());
		assertEquals(Dir.NONE, line.lineAttrs().getDir());
		assertEquals(3, line.lineAttrs().getMinlen());
		assertIterableEquals(singletonList(LineStyle.INVIS), line.lineAttrs().getStyles());
	}

	@Test
	void mapsConstraintAndSameEndpointGroups() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("a"));
		builder.node(node("b"));
		builder.edge(EdgeSpec.builder("ab", "a", "b").constraint(false).same("tail-group", "head-group").build());

		final Line line = builder.buildGraphviz().directLines().iterator().next();

		assertEquals(Boolean.FALSE, line.lineAttrs().getConstraint());
		assertEquals("tail-group", line.lineAttrs().getSameTail());
		assertEquals("head-group", line.lineAttrs().getSameHead());
	}

	@Test
	void leavesAnUnconflictedZeroMinlenChainAtItsOriginalWeight() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		for (String id : asList("security", "rest", "service", "implementation", "repository"))
			builder.node(node(id));
		builder.edge(flatEdge("security-rest", "security", "rest", true));
		builder.edge(flatEdge("rest-service", "rest", "service", true));
		builder.edge(flatEdge("service-implementation", "service", "implementation", true));
		builder.edge(flatEdge("implementation-repository", "implementation", "repository", true));

		final Map<String, Double> weights = weightsById(builder.buildGraphviz());

		assertEquals(1.0, weights.get("security-rest"));
		assertEquals(1.0, weights.get("rest-service"));
		assertEquals(1.0, weights.get("service-implementation"));
		assertEquals(1.0, weights.get("implementation-repository"));
	}

	@Test
	void buildsALongZeroMinlenChainWithAPositiveConflictWithoutRecursion() {
		final int nodeCount = 20000;
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		for (int i = 0; i < nodeCount; i++)
			builder.node(node("n" + i));
		for (int i = 0; i < nodeCount - 1; i++)
			builder.edge(flatEdge("e" + i, "n" + i, "n" + (i + 1), true));
		builder.edge(edge("conflict", "n0", "n" + (nodeCount - 1), null, null));

		final Map<String, Double> weights = weightsById(builder.buildGraphviz());

		assertEquals(nodeCount, weights.size());
		final double biasStep = .001 / (nodeCount + 1);
		for (int i = 0; i < nodeCount - 1; i++)
			assertEquals(1 + biasStep * (nodeCount - 2 - i), weights.get("e" + i), "e" + i);
		assertEquals(1.0, weights.get("conflict"));
	}

	@Test
	void leavesCyclesAndNonConstraintEdgesAtTheirOriginalWeight() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		for (String id : asList("a", "b", "c", "d", "x", "y"))
			builder.node(node(id));
		builder.edge(flatEdge("a-b", "a", "b", true));
		builder.edge(flatEdge("b-a", "b", "a", true));
		builder.edge(flatEdge("b-c", "b", "c", true));
		builder.edge(flatEdge("c-d", "c", "d", true));
		builder.edge(flatEdge("x-y", "x", "y", false));
		builder.edge(edge("inside-cycle", "a", "b", null, null));
		builder.edge(edge("conflict", "a", "d", null, null));

		final Map<String, Double> weights = weightsById(builder.buildGraphviz());

		assertEquals(1.0, weights.get("a-b"));
		assertEquals(1.0, weights.get("b-a"));
		assertEquals(1 + .001 / 6, weights.get("b-c"));
		assertEquals(1.0, weights.get("c-d"));
		assertEquals(1.0, weights.get("x-y"));
		assertEquals(1.0, weights.get("inside-cycle"));
		assertEquals(1.0, weights.get("conflict"));
	}

	@Test
	void usesTheLongestZeroMinlenSuffixAtBranchesAndPreservesBaseWeights() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		for (String id : asList("a", "b", "c", "d", "e"))
			builder.node(node(id));
		builder.edge(EdgeSpec.builder("a-b", "a", "b").minlen(0).weight(5).build());
		builder.edge(flatEdge("b-c", "b", "c", true));
		builder.edge(flatEdge("b-d", "b", "d", true));
		builder.edge(flatEdge("d-e", "d", "e", true));
		builder.edge(EdgeSpec.builder("a-e", "a", "e").build());

		final Map<String, Double> weights = weightsById(builder.buildGraphviz());

		assertTrue(weights.get("a-b") > weights.get("b-d") + 4);
		assertEquals(1.0, weights.get("b-c"));
		assertTrue(weights.get("b-d") > weights.get("d-e"));
		assertEquals(1.0, weights.get("d-e"));
		assertTrue(weights.get("a-b") < 5.001);
	}

	@Test
	void movesConflictingZeroMinlenSlackToTheEndOfTheChain() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		for (String id : asList("security", "rest", "service", "implementation", "repository"))
			builder.node(node(id));
		builder.edge(flatEdge("security-rest", "security", "rest", true));
		builder.edge(flatEdge("rest-service", "rest", "service", true));
		builder.edge(flatEdge("service-implementation", "service", "implementation", true));
		builder.edge(flatEdge("implementation-repository", "implementation", "repository", true));
		builder.edge(EdgeSpec.builder("security-repository", "security", "repository").build());

		final SvekLayoutResponse response = builder.layout();
		final double securityY = centerY(response, "security");

		assertEquals(securityY, centerY(response, "rest"));
		assertEquals(securityY, centerY(response, "service"));
		assertEquals(securityY, centerY(response, "implementation"));
		assertTrue(centerY(response, "repository") > securityY);
	}

	@Test
	void mainEdgeLabelUsesTheSameFixedHtmlTableModelAsNativeDot() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("a"));
		builder.node(node("b"));
		builder.edge(EdgeSpec.builder("ab", "a", "b")
				.labels(asList(new LabelSpec(LabelPosition.MAIN, 54, 17))).build());

		final Line line = builder.buildGraphviz().directLines().iterator().next();

		assertNotNull(line.lineAttrs().getTable());
		assertEquals(54.0, line.lineAttrs().getTable().getWidth());
		assertEquals(17.0, line.lineAttrs().getTable().getHeight());
		assertNull(line.lineAttrs().getAssemble());
	}

	@Test
	void visibleZeroMinlenEdgeStillGetsARoute() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("note"));
		builder.node(node("target"));
		builder.edge(EdgeSpec.builder("note-link", "note", "target").minlen(0).build());

		final SvekLayoutResponse response = builder.layout();

		assertTrue(response.isSuccess());
		assertNotNull(response.result.edges.get("note-link").path);
	}

	@Test
	void visibleZeroMinlenEdgeToClusterChildGetsARoute() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(new NodeSpec("note", 129, 59, Shape.RECTANGLE, null, emptyList()));
		builder.beginCluster(ClusterSpec.builder("namespace").title(80, 20).contentTopPadding(15).build());
		builder.node(new NodeSpec("target", 99, 50, Shape.ROUNDED_RECTANGLE, null, emptyList()));
		builder.endCluster();
		builder.edge(EdgeSpec.builder("note-link", "note", "target").minlen(0).build());

		final SvekLayoutResponse response = builder.layout();

		assertTrue(response.isSuccess());
		assertNotNull(response.result.edges.get("note-link").path);
	}

	@Test
	void layoutOnlyPointNodesRouteEdgesWithoutLeakingGeometry() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.beginCluster(ClusterSpec.builder("left").title(40, 20).build());
		builder.node(new NodeSpec("za-left", .72, .72, Shape.POINT, null, emptyList(), true));
		builder.endCluster();
		builder.beginCluster(ClusterSpec.builder("right").title(40, 20).build());
		builder.node(new NodeSpec("za-right", .72, .72, Shape.POINT, null, emptyList(), true));
		builder.endCluster();
		builder.edge(EdgeSpec.builder("group-edge", "za-left", "za-right").build());

		final SvekLayoutResponse response = builder.layout();

		assertTrue(response.isSuccess());
		assertTrue(response.result.nodes.isEmpty());
		assertNotNull(response.result.edges.get("group-edge").path);
	}

	@Test
	void buildsNestedClustersAndRanksWithoutFlatteningMembership() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("root"));
		builder.beginCluster(ClusterSpec.builder("outer").title(80, 20).build());
		builder.node(node("a"));
		builder.beginCluster(ClusterSpec.builder("inner").title(60, 16).build());
		builder.node(node("b"));
		builder.endCluster();
		builder.rankGroup(new RankSpec(Rank.SAME, singletonList("a")));
		builder.endCluster();
		builder.rankGroup(new RankSpec(Rank.MIN, singletonList("root")));
		builder.edge(edge("rb", "root", "b", null, null));

		final Graphviz graphviz = builder.buildGraphviz();
		final Cluster outer = graphviz.clusters().get(0);
		final Cluster inner = outer.clusters().get(0);
		final Subgraph outerRank = outer.subgraphs().get(0);

		assertTrue(stream(graphviz.nodes().spliterator(), false)
				.anyMatch(node -> "root".equals(node.nodeAttrs().getId())));
		assertTrue(stream(outer.nodes().spliterator(), false).map(node -> node.nodeAttrs().getId())
				.collect(toList()).containsAll(asList("a", "b")));
		assertIterableEquals(singletonList("b"), stream(inner.directNodes().spliterator(), false)
				.map(node -> node.nodeAttrs().getId()).collect(toList()));
		assertIterableEquals(singletonList("a"), stream(outerRank.nodes().spliterator(), false)
				.map(node -> node.nodeAttrs().getId()).collect(toList()));
		assertSame(outer, graphviz.father(inner));
		assertIterableEquals(singletonList("rb"), stream(graphviz.directLines().spliterator(), false)
				.map(line -> line.lineAttrs().getId()).collect(toList()));
	}

	@Test
	void mapsPlantUmlContainmentMarginsToGraphperClusters() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.beginCluster(ClusterSpec.builder("decorated").title(80, 20).margins(20, 24).build());
		builder.node(node("a"));
		builder.endCluster();

		final Cluster cluster = builder.buildGraphviz().clusters().get(0);

		assertEquals(20.0, cluster.clusterAttrs().getMargin().getWidth());
		assertEquals(24.0, cluster.clusterAttrs().getMargin().getHeight());
	}

	@Test
	void mapsLayoutOnlyClustersAlignmentAndScaffoldWeights() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.node(node("root"));
		builder.beginCluster(ClusterSpec.builder("layout-group").title(0, 0, Alignment.LEFT).layoutOnly(true).build());
		builder.node(node("member"));
		builder.endCluster();
		builder.edge(EdgeSpec.builder("scaffold", "root", "member").visible(false).weight(999).layoutOnly(true)
				.build());

		final Graphviz graphviz = builder.buildGraphviz();
		final Cluster cluster = graphviz.clusters().get(0);
		final Line line = graphviz.directLines().iterator().next();

		assertEquals(Labeljust.LEFT, cluster.clusterAttrs().getLabeljust());
		assertEquals(999.0, line.lineAttrs().getWeight());
		final SvekLayoutResponse response = builder.layout();
		assertTrue(response.isSuccess());
		assertTrue(response.result.clusters.isEmpty());
		assertTrue(response.result.edges.isEmpty());
	}

	@Test
	void parentRankMayReferenceNodesInsideLayoutOnlyChildCluster() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		builder.beginCluster(ClusterSpec.builder("parent").build());
		builder.rankGroup(new RankSpec(Rank.SAME, asList("a", "b")));
		builder.beginCluster(ClusterSpec.builder("together").layoutOnly(true).build());
		builder.node(node("a"));
		builder.node(node("b"));
		builder.endCluster();
		builder.endCluster();

		assertTrue(builder.layout().isSuccess());
	}

	@Test
	void rejectsInvalidPlantUmlClusterSpacing() {
		for (ClusterSpec cluster : asList(
				ClusterSpec.builder("horizontal").title(10, 10).margins(Double.NaN, 10).build(),
				ClusterSpec.builder("vertical").title(10, 10).margins(10, -1).build(),
				ClusterSpec.builder("padding").title(10, 10).contentTopPadding(Double.POSITIVE_INFINITY).build())) {
			final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
			builder.graph(graph());
			builder.beginCluster(cluster);
			builder.node(node("inside"));
			builder.endCluster();

			assertEquals("invalid-input", builder.layout().declineCode);
		}
	}

	@Test
	void mapsEveryNeutralRankKindForDirectlyOwnedNodes() {
		final GraphSupportSvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		for (Rank rank : Rank.values()) {
			final String id = rank.name().toLowerCase();
			builder.node(node(id));
			builder.rankGroup(new RankSpec(rank, singletonList(id)));
		}

		final Graphviz graphviz = builder.buildGraphviz();

		assertEquals(Rank.values().length, graphviz.subgraphs().size());
		for (int i = 0; i < Rank.values().length; i++) {
			final Subgraph subgraph = graphviz.subgraphs().get(i);
			assertEquals(Rank.values()[i].name(), subgraph.getRank().name());
			assertIterableEquals(singletonList(Rank.values()[i].name().toLowerCase()),
					stream(subgraph.nodes().spliterator(), false).map(node -> node.nodeAttrs().getId()).collect(toList()));
		}
		assertTrue(builder.layout().isSuccess());
	}

	private static SvekLayoutBuilder builderWithDuplicateNode() {
		final SvekLayoutBuilder builder = builder();
		builder.node(node("a"));
		builder.node(node("a"));
		return builder;
	}

	private static SvekLayoutBuilder builder() {
		final SvekLayoutBuilder builder = new GraphSupportSvekLayoutBuilder();
		builder.graph(graph());
		return builder;
	}

	private static void assertDeclined(SvekLayoutBuilder builder, String code) {
		final SvekLayoutResponse response = builder.layout();
		assertFalse(response.isSuccess());
		assertEquals(code, response.declineCode);
	}

	private static GraphSpec graph() {
		return new GraphSpec(Direction.TOP_TO_BOTTOM, Routing.SPLINE, 18, 36);
	}

	private static NodeSpec node(String id) {
		return new NodeSpec(id, 72, 36, Shape.RECTANGLE, null, emptyList());
	}

	private static NodeSpec nodeWithCell(String id, String cellId) {
		return new NodeSpec(id, 72, 36, Shape.RECTANGLE, cellId,
				singletonList(new CellSpec(cellId, 0, 0, 72, 36)));
	}

	private static EdgeSpec edge(String id, String tail, String head, String tailCell, String headCell) {
		return EdgeSpec.builder(id, tail, head).cells(tailCell, headCell).build();
	}

	private static EdgeSpec flatEdge(String id, String tail, String head, boolean constraint) {
		return EdgeSpec.builder(id, tail, head).minlen(0).constraint(constraint).build();
	}

	private static Map<String, Double> weightsById(Graphviz graphviz) {
		final Map<String, Double> result = new LinkedHashMap<>();
		for (Line line : graphviz.directLines())
			result.put(line.lineAttrs().getId(), line.lineAttrs().getWeight());
		return result;
	}

	private static double centerY(SvekLayoutResponse response, String nodeId) {
		return (response.result.nodes.get(nodeId).bounds.minY + response.result.nodes.get(nodeId).bounds.maxY) / 2;
	}
}
