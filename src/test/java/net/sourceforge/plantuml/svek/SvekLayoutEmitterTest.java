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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPort;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.svek.layout.SvekLayoutBuilder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Alignment;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.GraphSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.RankSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Rank;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Shape;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;

class SvekLayoutEmitterTest {
	@Test
	void shieldedEntityPortExposesBodyCell() {
		final EntityPort port = EntityPort.create("nodeUid:h", null);

		assertEquals("nodeUid", port.getPrefix());
		assertEquals("h", port.getPortId());
	}

	@Test
	void emitsCompletedModelWithoutDotSerialization() throws Exception {
		final Fixture fixture = fixture(
				"@startuml",
				"package Group {",
				"  class First",
				"}",
				"class Second",
				"First --> Second",
				"@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		final SvekLayoutResponse response = new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(),
				fixture.stringBounder).emit(recording);

		assertFalse(response.isSuccess());
		assertEquals("recording", response.declineCode);
		assertEquals(Direction.TOP_TO_BOTTOM, recording.graph.direction);
		assertEquals(35, recording.graph.nodeSep);
		assertEquals(60, recording.graph.rankSep);
		assertEquals(2, recording.nodes.size());
		assertEquals(recording.nodes.size(), recording.nodeIds().size());
		assertEquals(1, recording.clusters.stream().filter(cluster -> cluster.layoutOnly == false).count());
		assertEquals(1, recording.edges.size());
		assertTrue(recording.nodeIds().contains(recording.edges.get(0).tailId));
		assertTrue(recording.nodeIds().contains(recording.edges.get(0).headId));
		assertEquals(fixture.diagram.getLinks().iterator().next().getUid(), recording.edges.get(0).id);
		assertEquals(Math.max(0, fixture.diagram.getLinks().iterator().next().getLength() - 1),
				recording.edges.get(0).minlen);
		assertTrue(recording.edges.get(0).visible);
		assertEquals(recording.endCount, recording.beginCount);
	}

	@Test
	void emitsLabelFootprintAndConfiguredSeparation() throws Exception {
		final Fixture fixture = fixture("@startuml", "skinparam nodesep 42", "skinparam ranksep 73", "class First",
				"class Second", "First ---> Second : main label", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		assertEquals(42, recording.graph.nodeSep);
		assertEquals(73, recording.graph.rankSep);
		assertEquals(Math.max(0, fixture.diagram.getLinks().iterator().next().getLength() - 1),
				recording.edges.get(0).minlen);
		assertTrue(recording.edges.get(0).labels.stream().anyMatch(label -> label.position == LabelPosition.MAIN
				&& Double.compare(label.width, 0) > 0 && Double.compare(label.height, 0) > 0));
	}

	@Test
	void noteConnectorRequiresAVisibleLayoutRoute() throws Exception {
		final Fixture fixture = fixture("@startuml", "state CustomerPage", "note left of CustomerPage",
				"  session details", "end note", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		assertEquals(1, recording.edges.size());
		final EdgeSpec edge = recording.edges.get(0);
		assertTrue(edge.visible);
		assertEquals(0, edge.minlen);
		assertNotEquals(edge.headId, edge.tailId);
		assertNull(edge.tailCellId);
		assertNull(edge.headCellId);
		assertEquals(2, recording.nodes.size());
		for (NodeSpec node : recording.nodes) {
			assertTrue(node.cells.isEmpty());
			assertNull(node.bodyCellId);
		}
	}

	@Test
	void groupEndpointsUseLayoutOnlyCenterNodes() throws Exception {
		final Fixture fixture = fixture("@startuml", "rectangle Left {", " class A", "}", "rectangle Right {",
				" class B", "}", "Left --> Right", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		final List<NodeSpec> layoutNodes = recording.nodes.stream().filter(node -> node.layoutOnly)
				.collect(Collectors.toList());
		assertEquals(2, layoutNodes.size());
		for (NodeSpec node : layoutNodes) {
			assertEquals(Shape.POINT, node.shape);
			assertTrue(node.id.startsWith(Cluster.CENTER_ID));
		}
		assertEquals(1, recording.edges.size());
		assertTrue(recording.edges.get(0).tailId.startsWith(Cluster.CENTER_ID));
		assertTrue(recording.edges.get(0).headId.startsWith(Cluster.CENTER_ID));
	}

	@Test
	void qualifiedAssociationTargetsShieldBodyCell() throws Exception {
		final Fixture fixture = fixture("@startuml", "class Shop", "class Customer",
				"Shop [customerId: long] ---> Customer", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		final NodeSpec shielded = recording.nodes.stream().filter(node -> "h".equals(node.bodyCellId)).findFirst().get();
		assertEquals(1, recording.edges.size());
		final EdgeSpec edge = recording.edges.get(0);
		if (shielded.id.equals(edge.tailId))
			assertEquals("h", edge.tailCellId);
		else {
			assertEquals(shielded.id, edge.headId);
			assertEquals("h", edge.headCellId);
		}
		assertTrue(recording.nodes.stream().anyMatch(node -> "h".equals(node.bodyCellId)
				&& node.cells.size() == 1 && "h".equals(node.cells.get(0).id)));
	}

	@Test
	void buildModelIsIdempotent() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Group {", "class First", "}", "class Second",
				"First --> Second", "@enduml");
		final int nodes = fixture.factory.getBibliotekon().allNodes().size();
		final int edges = fixture.factory.getBibliotekon().allLines().size();
		final int clusters = fixture.factory.getBibliotekon().allCluster().size();
		final List<Integer> memberships = clusterMemberships(fixture.root);

		fixture.imageBuilder.buildModel(fixture.stringBounder);

		assertEquals(nodes, fixture.factory.getBibliotekon().allNodes().size());
		assertEquals(edges, fixture.factory.getBibliotekon().allLines().size());
		assertEquals(clusters, fixture.factory.getBibliotekon().allCluster().size());
		assertEquals(memberships, clusterMemberships(fixture.root));
	}

	@Test
	void failedModelBuildRethrowsOriginalThenRejectsRetry() throws Exception {
		final IllegalArgumentException failure = new IllegalArgumentException("model fixture failed");
		final Fixture fixture = fixtureWithoutBuild(new GraphvizImageBuilder.ModelBuildHook() {
			public void beforeBuild() {
				throw failure;
			}
		}, "@startuml", "class First", "class Second", "@enduml");

		assertSame(failure, assertThrows(IllegalArgumentException.class,
				() -> fixture.imageBuilder.buildModel(fixture.stringBounder)));
		assertEquals("previous Svek model build failed", assertThrows(IllegalStateException.class,
				() -> fixture.imageBuilder.buildModel(fixture.stringBounder)).getMessage());
	}

	@Test
	void reentrantModelBuildIsRejectedAndMarksBuildFailed() throws Exception {
		final GraphvizImageBuilder[] builder = new GraphvizImageBuilder[1];
		final StringBounder[] bounder = new StringBounder[1];
		final Fixture fixture = fixtureWithoutBuild(new GraphvizImageBuilder.ModelBuildHook() {
			public void beforeBuild() {
				builder[0].buildModel(bounder[0]);
			}
		}, "@startuml", "class First", "class Second", "@enduml");
		builder[0] = fixture.imageBuilder;
		bounder[0] = fixture.stringBounder;

		assertEquals("Svek model build is already in progress", assertThrows(IllegalStateException.class,
				() -> builder[0].buildModel(bounder[0])).getMessage());
		assertEquals("previous Svek model build failed", assertThrows(IllegalStateException.class,
				() -> builder[0].buildModel(bounder[0])).getMessage());
	}

	@Test
	void clusterTitleHeightUsesFullPlantUmlFootprint() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Group {", "class First", "}", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();
		final Cluster cluster = fixture.root.getChildren().get(0);

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		final List<ClusterSpec> clusters = recording.clusters.stream().filter(spec -> spec.layoutOnly == false)
				.collect(Collectors.toList());
		assertEquals(1, clusters.size());
		assertEquals(cluster.getTitleAndAttributeHeight(), clusters.get(0).titleHeight);
		assertEquals(15, clusters.get(0).contentTopPadding);
	}

	@Test
	void decoratedClusterUsesExtraContainmentMargin() throws Exception {
		final Fixture fixture = fixture("@startuml", "node Cloud {", "node Web {", "artifact app", "}", "}",
				"@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		final List<ClusterSpec> clusters = recording.clusters.stream().filter(cluster -> cluster.layoutOnly == false)
				.collect(Collectors.toList());
		assertEquals(2, clusters.size());
		for (ClusterSpec cluster : clusters) {
			assertEquals(20, cluster.horizontalMargin);
			assertEquals(20, cluster.verticalMargin);
		}
		for (ClusterSpec cluster : clusters)
			assertEquals(15, cluster.contentTopPadding);
	}

	@Test
	void conversionFailureDoesNotPartiallyEmitBuilderEvents() throws Exception {
		final Fixture fixture = fixtureWithoutBuild(null, "@startuml", "class First", "class Second", "@enduml");
		final Entity first = fixture.diagram.leafs().iterator().next();
		first.setSvekImage(new GraphvizImageBuilder.EntityImageSimpleEmpty(null) {
			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				throw new IllegalStateException("node conversion failed");
			}
		});
		fixture.imageBuilder.buildModel(fixture.stringBounder);
		final RecordingBuilder recording = new RecordingBuilder();

		assertEquals("node conversion failed", assertThrows(IllegalStateException.class,
				() -> new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(),
						fixture.stringBounder).emit(recording)).getMessage());
		assertEquals(0, recording.eventCount);
	}

	@Test
	void validNestedClustersHaveBalancedBeginAndEndEvents() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Outer {", "package Inner {", "class First", "}", "}",
				"@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		assertEquals(6, recording.beginCount);
		assertEquals(recording.beginCount, recording.endCount);
	}

	@Test
	void emitsConstraintFalseWithoutDeclining() throws Exception {
		final Fixture fixture = fixture("@startuml", "class First", "class Second", "First --> Second", "@enduml");
		fixture.diagram.getLinks().iterator().next().setConstraint(false);
		final RecordingBuilder recording = new RecordingBuilder();

		final SvekLayoutResponse response = new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(),
				fixture.stringBounder).emit(recording);

		assertEquals("recording", response.declineCode);
		assertTrue(recording.layoutCalled);
		assertEquals(1, recording.edges.size());
		assertFalse(recording.edges.get(0).constraint);
	}

	@Test
	void emitsNestedTogetherBlocksAsLayoutOnlyClusters() throws Exception {
		final Fixture fixture = fixture("@startuml", "together {", "class A", "together {", "class B",
				"class C", "}", "}", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		assertEquals(2, recording.clusters.stream()
				.filter(cluster -> cluster.layoutOnly && cluster.id.matches(".*t[0-9]+")).count());
		assertEquals(3, recording.nodes.size());
	}

	@Test
	void flattensAutomaticallyPackedWrapperClusters() throws Exception {
		final Fixture fixture = fixture("@startuml", "package Outer {", "package Inner {", "class A", "class B",
				"}", "}", "@enduml");
		fixture.root.getChildren().iterator().next().getGroup().setPacked(true);
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		assertTrue(fixture.root.getChildren().stream().anyMatch(cluster -> cluster.isPackedForLayout()));
		assertEquals(1, recording.clusters.stream().filter(cluster -> cluster.layoutOnly == false).count());
	}

	@Test
	void mapsPackageTitleAlignment() throws Exception {
		final Fixture fixture = fixture("@startuml", "skinparam packageTitleAlignment left", "package P {",
				"class A", "}", "@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		final List<ClusterSpec> clusters = recording.clusters.stream().filter(cluster -> cluster.layoutOnly == false)
				.collect(Collectors.toList());
		assertEquals(1, clusters.size());
		assertEquals(Alignment.LEFT, clusters.get(0).titleAlignment);
	}

	@Test
	void emitsLegacySwimlaneRankScaffolding() throws Exception {
		final Fixture fixture = fixture("@startuml", "skinparam swimlane true", "partition Sales {",
				"(*) --> Receive", "}", "partition Finance {", "Receive --> Approve", "Approve --> (*)", "}",
				"@enduml");
		final RecordingBuilder recording = new RecordingBuilder();

		new SvekLayoutEmitter(fixture.factory, fixture.diagram.getDiagramType(), fixture.stringBounder).emit(recording);

		assertTrue(recording.nodes.stream().anyMatch(node -> node.id != null && node.id.startsWith("minPoint")));
		assertTrue(recording.nodes.stream().anyMatch(node -> node.id != null && node.id.startsWith("maxPoint")));
		final List<Rank> ranks = recording.ranks.stream().map(rank -> rank.rank).collect(Collectors.toList());
		assertTrue(ranks.contains(Rank.MIN));
		assertTrue(ranks.contains(Rank.MAX));
		assertTrue(ranks.contains(Rank.SOURCE));
		assertTrue(ranks.contains(Rank.SINK));
		assertEquals(4, recording.edges.stream().filter(edge -> edge.layoutOnly && edge.weight == 999).count());
	}

	private static Fixture fixture(String... source) throws Exception {
		final Fixture fixture = fixtureWithoutBuild(null, source);
		fixture.imageBuilder.buildModel(fixture.stringBounder);
		return fixture;
	}

	private static Fixture fixtureWithoutBuild(GraphvizImageBuilder.ModelBuildHook hook, String... source)
			throws Exception {
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
				diagram.getPragma(), diagram.getDiagramType().getStyleName(), DotMode.NORMAL, factory, clusterManager, hook);
		return new Fixture(diagram, factory, root, imageBuilder, stringBounder);
	}

	private static List<Integer> clusterMemberships(Cluster cluster) {
		final List<Integer> result = new ArrayList<>();
		result.add(cluster.getNodes().size());
		for (Cluster child : cluster.getChildren())
			result.addAll(clusterMemberships(child));
		return result;
	}

	private static final class Fixture {
		private final CucaDiagram diagram;
		private final DotStringFactory factory;
		private final Cluster root;
		private final GraphvizImageBuilder imageBuilder;
		private final StringBounder stringBounder;

		private Fixture(CucaDiagram diagram, DotStringFactory factory, Cluster root,
				GraphvizImageBuilder imageBuilder, StringBounder stringBounder) {
			this.diagram = diagram;
			this.factory = factory;
			this.root = root;
			this.imageBuilder = imageBuilder;
			this.stringBounder = stringBounder;
		}
	}

	private static final class RecordingBuilder implements SvekLayoutBuilder {
		private GraphSpec graph;
		private final List<NodeSpec> nodes = new ArrayList<>();
		private final List<ClusterSpec> clusters = new ArrayList<>();
		private final List<RankSpec> ranks = new ArrayList<>();
		private final List<EdgeSpec> edges = new ArrayList<>();
		private boolean layoutCalled;
		private int beginCount;
		private int endCount;
		private int eventCount;

		public void graph(GraphSpec graph) {
			eventCount++;
			this.graph = graph;
		}

		public void node(NodeSpec node) {
			eventCount++;
			nodes.add(node);
		}

		public void beginCluster(ClusterSpec cluster) {
			eventCount++;
			beginCount++;
			clusters.add(cluster);
		}

		public void endCluster() {
			eventCount++;
			endCount++;
		}

		public void rankGroup(RankSpec rank) {
			eventCount++;
			ranks.add(rank);
		}

		public void edge(EdgeSpec edge) {
			eventCount++;
			edges.add(edge);
		}

		public SvekLayoutResponse layout() {
			eventCount++;
			layoutCalled = true;
			return SvekLayoutResponse.declined("recording", "test builder");
		}

		private Set<String> nodeIds() {
			final Set<String> result = new LinkedHashSet<>();
			for (NodeSpec node : nodes)
				result.add(node.id);
			return Collections.unmodifiableSet(result);
		}
	}
}
