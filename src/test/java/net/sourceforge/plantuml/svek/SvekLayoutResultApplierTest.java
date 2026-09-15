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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.Positionable;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XLine2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Bounds;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Path;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;

class SvekLayoutResultApplierTest {

	@Test
	void opaleShortcutDetectsBlockingRectangles() {
		final RectangleArea blocker = new RectangleArea(40, 40, 60, 60);

		assertTrue(SvekEdge.intersectsForOpale(blocker, new XLine2D(0, 50, 100, 50)));
		assertFalse(SvekEdge.intersectsForOpale(blocker, new XLine2D(0, 10, 100, 10)));
		assertFalse(SvekEdge.intersectsForOpale(null, new XLine2D(0, 10, 100, 10)));
	}

	@Test
	void appliesNodesClustersCubicPathPolygonAndLabels() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Group {", "class First", "}", "class Second",
				"First \"tail\" -- \"head\" Second : main", "@enduml");
		final SvekNode first = fixture.nodes.get(0);
		final Cluster cluster = fixture.factory.getRootCluster().getChildren().get(0);
		final SvekEdge edge = fixture.factory.getBibliotekon().allLines().get(0);
		final SvekLayoutResult result = result(fixture, true);

		final net.sourceforge.plantuml.svek.layout.SvekLayoutValidation validation =
				new SvekLayoutResultApplier(fixture.factory).apply(result);
		assertTrue(validation.isValid(), validation.getMessage());

		assertEquals(10, first.getMinX());
		assertEquals(40, first.getMinY());
		assertIterableEquals(asList(new XPoint2D(0, 0),
				new XPoint2D(first.getWidth(), 0), new XPoint2D(first.getWidth(), first.getHeight()),
				new XPoint2D(0, first.getHeight())), ((UPolygon) first.getPolygon()).getPoints());
		assertEquals(0, cluster.getRectangleArea().getMinX());
		assertEquals(90, cluster.getRectangleArea().getMaxY());
		assertClusterTitle(cluster, 3, 4);
		final List<net.sourceforge.plantuml.klimt.geom.XCubicCurve2D> cubicCurves = edge.getDotPath().getBeziers();
		assertEquals(2, cubicCurves.size());
		if (cubicCurves.get(0).getP1().equals(new XPoint2D(20, 200))) {
			assertCurve(cubicCurves.get(0), 20, 200, 25, 200, 30, 200, 35, 200);
			assertCurve(cubicCurves.get(1), 35, 200, 40, 200, 45, 200, 50, 200);
		} else {
			assertCurve(cubicCurves.get(0), 50, 200, 45, 200, 40, 200, 35, 200);
			assertCurve(cubicCurves.get(1), 35, 200, 30, 200, 25, 200, 20, 200);
		}
		assertLabelPositioned(edge, "labelXY", 200, 240);
		assertLabelPositioned(edge, "startTailLabelXY", 220, 240);
		assertLabelPositioned(edge, "endHeadLabelXY", 240, 240);
	}

	@Test
	void convertsPolylineSegmentsToDegenerateCubics() throws Exception {
		final Fixture fixture = fixture("@startuml", "class First", "class Second", "First -- Second", "@enduml");
		final SvekLayoutResult base = result(fixture, false);
		final String edgeId = fixture.factory.getBibliotekon().allLines().get(0).getLayoutId();
		final Map<String, EdgeGeometry> edges = new LinkedHashMap<>(base.edges);
		final net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec spec = fixture.factory.getBibliotekon()
				.allLines().get(0).toLayoutSpec();
		edges.put(edgeId, new EdgeGeometry(edgeId, spec.tailId, spec.headId,
				new Path(false, asList(new Point(0, 200), new Point(5, 206), new Point(9, 210))), null, null, null));

		final net.sourceforge.plantuml.svek.layout.SvekLayoutValidation validation =
				new SvekLayoutResultApplier(fixture.factory)
						.apply(new SvekLayoutResult(base.graphBounds, base.direction, base.nodes, base.clusters, edges));
		assertTrue(validation.isValid(), validation.getMessage());

		final List<net.sourceforge.plantuml.klimt.geom.XCubicCurve2D> curves = fixture.factory.getBibliotekon()
				.allLines().get(0).getDotPath().getBeziers();
		assertEquals(2, curves.size());
		curves.forEach(curve -> {
			assertEquals(curve.getP1(), curve.getCtrlP1());
			assertEquals(curve.getP2(), curve.getCtrlP2());
		});
		if (curves.get(0).getP1().equals(new XPoint2D(0, 200))) {
			assertCurve(curves.get(0), 0, 200, 0, 200, 5, 206, 5, 206);
			assertCurve(curves.get(1), 5, 206, 5, 206, 9, 210, 9, 210);
		} else {
			assertCurve(curves.get(0), 9, 210, 9, 210, 5, 206, 5, 206);
			assertCurve(curves.get(1), 5, 206, 5, 206, 0, 200, 0, 200);
		}
	}

	@Test
	void invalidResultLeavesEverySvekObjectUnchanged() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Group {", "class First", "}", "class Second",
				"First --> Second", "@enduml");
		final Map<SvekNode, XPoint2D> nodePositions = new LinkedHashMap<>();
		final Map<SvekNode, Object> nodePolygons = new LinkedHashMap<>();
		for (SvekNode node : fixture.nodes) {
			nodePositions.put(node, node.getPosition());
			nodePolygons.put(node, node.getPolygon());
		}
		final SvekNode node = fixture.nodes.get(0);
		final Cluster cluster = fixture.factory.getRootCluster().getChildren().get(0);
		final SvekEdge edge = fixture.factory.getBibliotekon().allLines().get(0);
		final Object oldClusterBounds = cluster.getRectangleArea();
		final Object oldClusterTitle = field(cluster, "xyTitle");
		final Object oldMain = field(edge, "labelXY");
		final Object oldTail = field(edge, "startTailLabelXY");
		final Object oldHead = field(edge, "endHeadLabelXY");
		final SvekLayoutResult valid = result(fixture, false);
		final Map<String, NodeGeometry> missingNode = new LinkedHashMap<>(valid.nodes);
		missingNode.remove(node.getUid());

		final SvekLayoutResultApplier applier = new SvekLayoutResultApplier(fixture.factory);
		assertTrue(applier.apply(new SvekLayoutResult(valid.graphBounds, valid.direction, missingNode, valid.clusters,
				valid.edges)).isInvalid());

		for (SvekNode fixtureNode : fixture.nodes) {
			assertEquals(nodePositions.get(fixtureNode), fixtureNode.getPosition());
			assertSame(nodePolygons.get(fixtureNode), fixtureNode.getPolygon());
		}
		assertSame(oldClusterBounds, cluster.getRectangleArea());
		assertSame(oldClusterTitle, field(cluster, "xyTitle"));
		assertSame(oldMain, field(edge, "labelXY"));
		assertSame(oldTail, field(edge, "startTailLabelXY"));
		assertSame(oldHead, field(edge, "endHeadLabelXY"));
		assertThrows(NullPointerException.class, edge::getDotPath, "expected null edge path");
	}

	@Test
	void applierIsSingleUseAndValidateMayRepeatBeforeApply() throws Exception {
		final Fixture fixture = fixture("@startuml", "class First", "class Second", "First --> Second", "@enduml");
		final SvekLayoutResult result = result(fixture, false);
		final SvekLayoutResultApplier applier = new SvekLayoutResultApplier(fixture.factory);
		assertTrue(applier.validate(result).isValid(), applier.validate(result).getMessage());
		assertTrue(applier.validate(result).isValid());
		assertTrue(applier.apply(result).isValid());
		final XPoint2D position = fixture.nodes.get(0).getPosition();
		final int segments = fixture.factory.getBibliotekon().allLines().get(0).getDotPath().getBeziers().size();

		assertEquals("Svek layout result applier may only apply once",
				assertThrows(IllegalStateException.class, () -> applier.apply(result)).getMessage());
		assertEquals(position, fixture.nodes.get(0).getPosition());
		assertEquals(segments, fixture.factory.getBibliotekon().allLines().get(0).getDotPath().getBeziers().size());
	}

	@Test
	void prepareCopiesAllGeometryWithoutMutatingSvekObjects() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Group {", "class First", "}", "class Second",
				"First --> Second", "@enduml");
		final SvekNode node = fixture.nodes.get(0);
		final Cluster cluster = fixture.factory.getRootCluster().getChildren().get(0);
		final SvekEdge edge = fixture.factory.getBibliotekon().allLines().get(0);
		final XPoint2D oldNodePosition = node.getPosition();
		final Object oldPolygon = node.getPolygon();
		final Object oldClusterBounds = cluster.getRectangleArea();
		final Object oldClusterTitle = field(cluster, "xyTitle");

		final SvekLayoutResultApplier.PreparedLayout prepared =
				new SvekLayoutResultApplier(fixture.factory).prepare(result(fixture, false));

		assertEquals(oldNodePosition, node.getPosition());
		assertSame(oldPolygon, node.getPolygon());
		assertSame(oldClusterBounds, cluster.getRectangleArea());
		assertSame(oldClusterTitle, field(cluster, "xyTitle"));
		assertThrows(NullPointerException.class, edge::getDotPath, "expected null edge path");
		prepared.apply();
		assertNotEquals(oldNodePosition, node.getPosition());
	}

	@Test
	void preparedLayoutMayOnlyApplyOnce() throws Exception {
		final Fixture fixture = fixture("@startuml", "class First", "class Second", "First --> Second", "@enduml");
		final SvekLayoutResultApplier.PreparedLayout prepared =
				new SvekLayoutResultApplier(fixture.factory).prepare(result(fixture, false));

		prepared.apply();

		assertEquals("Prepared Svek layout may only apply once",
				assertThrows(IllegalStateException.class, prepared::apply).getMessage());
	}

	@Test
	void concurrentPrepareAllowsExactlyOneCaller() throws Exception {
		final Fixture fixture = fixture("@startuml", "class First", "class Second", "First --> Second", "@enduml");
		final SvekLayoutResult result = result(fixture, false);
		final SvekLayoutResultApplier applier = new SvekLayoutResultApplier(fixture.factory);
		assertTrue(Modifier.isSynchronized(SvekLayoutResultApplier.class
				.getDeclaredMethod("prepare", SvekLayoutResult.class).getModifiers()));

		final List<Outcome> outcomes = runConcurrently(new ConcurrentAction() {
			public void run() {
				applier.prepare(result);
			}
		});

		assertSingleUseOutcomes(outcomes, "Svek layout result applier may only apply once");
	}

	@Test
	void concurrentPreparedApplyAllowsExactlyOneCaller() throws Exception {
		final Fixture fixture = fixture("@startuml", "class First", "class Second", "First --> Second", "@enduml");
		final SvekLayoutResultApplier.PreparedLayout prepared =
				new SvekLayoutResultApplier(fixture.factory).prepare(result(fixture, false));
		assertTrue(Modifier.isSynchronized(prepared.getClass().getDeclaredMethod("apply").getModifiers()));

		final List<Outcome> outcomes = runConcurrently(new ConcurrentAction() {
			public void run() {
				prepared.apply();
			}
		});

		assertSingleUseOutcomes(outcomes, "Prepared Svek layout may only apply once");
		assertEquals(new XPoint2D(10, 40), fixture.nodes.get(0).getPosition());
		assertEquals(2, fixture.factory.getBibliotekon().allLines().get(0).getDotPath().getBeziers().size());
	}

	private static List<Outcome> runConcurrently(final ConcurrentAction action) throws Exception {
		final CyclicBarrier start = new CyclicBarrier(3);
		final CountDownLatch finished = new CountDownLatch(2);
		final List<Outcome> outcomes = Collections.synchronizedList(new ArrayList<>());
		for (int i = 0; i < 2; i++) {
			final Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						start.await();
						action.run();
						outcomes.add(Outcome.success());
					} catch (Throwable failure) {
						outcomes.add(Outcome.failure(failure));
					} finally {
						finished.countDown();
					}
				}
			}, "svek-layout-single-use-" + i);
			thread.start();
		}
		start.await();
		assertTrue(finished.await(10, TimeUnit.SECONDS), "concurrent calls completed");
		return outcomes;
	}

	private static void assertSingleUseOutcomes(List<Outcome> outcomes, String message) {
		assertEquals(2, outcomes.size());
		assertEquals(1, outcomes.stream().filter(outcome -> outcome.failure == null).count());
		assertIterableEquals(Collections.singletonList(message), outcomes.stream()
				.filter(outcome -> outcome.failure instanceof IllegalStateException)
				.map(outcome -> outcome.failure.getMessage()).collect(Collectors.toList()));
	}

	private interface ConcurrentAction {
		void run() throws Exception;
	}

	private static final class Outcome {
		private final Throwable failure;

		private Outcome(Throwable failure) {
			this.failure = failure;
		}

		private static Outcome success() {
			return new Outcome(null);
		}

		private static Outcome failure(Throwable failure) {
			return new Outcome(failure);
		}
	}

	private static void assertLabelPositioned(SvekEdge edge, String fieldName, double x, double y) throws Exception {
		final Field field = SvekEdge.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		final Positionable label = (Positionable) field.get(edge);
		assertNotNull(label);
		assertEquals(new XPoint2D(x, y), label.getPosition());
	}

	private static Object field(Object owner, String fieldName) throws Exception {
		final Field field = owner.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(owner);
	}

	private static void assertCurve(net.sourceforge.plantuml.klimt.geom.XCubicCurve2D curve, double x1, double y1,
			double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
		assertEquals(new XPoint2D(x1, y1), curve.getP1());
		assertEquals(new XPoint2D(cx1, cy1), curve.getCtrlP1());
		assertEquals(new XPoint2D(cx2, cy2), curve.getCtrlP2());
		assertEquals(new XPoint2D(x2, y2), curve.getP2());
	}

	private static void assertClusterTitle(Cluster cluster, double x, double y) throws Exception {
		final Field field = Cluster.class.getDeclaredField("xyTitle");
		field.setAccessible(true);
		assertEquals(new XPoint2D(x, y), field.get(cluster));
	}

	private static SvekLayoutResult result(Fixture fixture, boolean labels) {
		final Map<String, NodeGeometry> nodes = new LinkedHashMap<>();
		for (int i = 0; i < fixture.nodes.size(); i++) {
			final SvekNode node = fixture.nodes.get(i);
			final double x = 10 + i * 40;
			final double y = node.getCluster() == null ? 15 : 40;
			final double width = node.getWidth();
			final double height = node.getHeight();
			final List<Point> polygon = i == 0
					? asList(new Point(x, y), new Point(x + width, y), new Point(x + width, y + height),
							new Point(x, y + height))
					: null;
			nodes.put(node.getUid(),
					new NodeGeometry(node.getUid(), new Bounds(x, y, x + width, y + height), polygon));
		}
		final Map<String, ClusterGeometry> clusters = new LinkedHashMap<>();
		for (Cluster cluster : fixture.factory.getBibliotekon().allCluster())
			if (cluster.getGroup().isPacked() == false)
				clusters.put(cluster.getClusterId(),
						new ClusterGeometry(cluster.getClusterId(), new Bounds(0, 0, 100, 90), new Point(3, 4)));

		final Map<String, EdgeGeometry> edges = new LinkedHashMap<>();
		for (SvekEdge edge : fixture.factory.getBibliotekon().allLines()) {
			final Path path = new Path(true, asList(new Point(20, 200), new Point(25, 200), new Point(30, 200),
					new Point(35, 200), new Point(40, 200), new Point(45, 200), new Point(50, 200)));
			final net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec spec = edge.toLayoutSpec();
			edges.put(edge.getLayoutId(), new EdgeGeometry(edge.getLayoutId(), spec.tailId, spec.headId, path,
					labels ? new Point(200, 240) : null, labels ? new Point(220, 240) : null,
					labels ? new Point(240, 240) : null));
		}
		return new SvekLayoutResult(new Bounds(0, 0, 500, 300), Direction.TOP_TO_BOTTOM, nodes, clusters, edges);
	}

	private static Fixture fixture(String... source) throws Exception {
		final SourceStringReader reader = new SourceStringReader(String.join("\n", source));
		final List<BlockUml> blocks = reader.getBlocks();
		final Diagram parsed = blocks.get(0).getDiagram();
		final CucaDiagram diagram = (CucaDiagram) parsed;
		final StringBounder stringBounder = new FileFormatOption(FileFormat.SVG)
				.getDefaultStringBounder(diagram.getSkinParam(), diagram.getPragma());
		final List<Link> links = new ArrayList<>(diagram.getLinks());
		final Bibliotekon bibliotekon = new Bibliotekon(links);
		final Cluster root = new Cluster(diagram.getRootGroup().getLocation(), diagram, bibliotekon.getColorSequence(),
				diagram.getRootGroup());
		final ClusterManager clusterManager = new ClusterManager(bibliotekon, root);
		final DotStringFactory factory = new DotStringFactory(bibliotekon, root, diagram.getDiagramType(),
				diagram.getSkinParam());
		final DotData dotData = new DotData(diagram, diagram.getRootGroup(), links, diagram.leafs(), diagram, diagram);
		final GraphvizImageBuilder imageBuilder = new GraphvizImageBuilder(dotData, diagram.getSource(),
				diagram.getPragma(), diagram.getDiagramType().getStyleName(), DotMode.NORMAL, factory, clusterManager);
		imageBuilder.buildModel(stringBounder);
		return new Fixture(factory, new ArrayList<>(bibliotekon.allNodes()));
	}

	private static final class Fixture {
		private final DotStringFactory factory;
		private final List<SvekNode> nodes;

		private Fixture(DotStringFactory factory, List<SvekNode> nodes) {
			this.factory = factory;
			this.nodes = nodes;
		}
	}
}
