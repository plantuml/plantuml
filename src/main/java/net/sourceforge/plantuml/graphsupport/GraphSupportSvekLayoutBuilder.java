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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.graphper.api.Assemble;
import org.graphper.api.Assemble.AssembleBuilder;
import org.graphper.api.Cluster;
import org.graphper.api.Cluster.ClusterBuilder;
import org.graphper.api.FloatLabel;
import org.graphper.api.GraphContainer.GraphContainerBuilder;
import org.graphper.api.Graphviz;
import org.graphper.api.Graphviz.GraphvizBuilder;
import org.graphper.api.Html;
import org.graphper.api.Html.Table;
import org.graphper.api.Line;
import org.graphper.api.Line.LineBuilder;
import org.graphper.api.Node;
import org.graphper.api.Node.NodeBuilder;
import org.graphper.api.Subgraph;
import org.graphper.api.Subgraph.SubgraphBuilder;
import org.graphper.api.attributes.Dir;
import org.graphper.api.attributes.Labeljust;
import org.graphper.api.attributes.Layout;
import org.graphper.api.attributes.LineStyle;
import org.graphper.api.attributes.NodeShapeEnum;
import org.graphper.api.attributes.NodeStyle;
import org.graphper.api.attributes.Rankdir;
import org.graphper.api.attributes.Splines;
import org.graphper.api.attributes.Tend;
import org.graphper.draw.DrawGraph;
import org.graphper.draw.ExecuteException;

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

/**
 * Records a neutral Svek layout request and replays it on a Graphper graph.
 *
 * A malformed request is declined instead of throwing: the first problem is remembered and reported
 * by {@link #layout()}, so Svek can fall back to another layout provider.
 */
public final class GraphSupportSvekLayoutBuilder implements SvekLayoutBuilder {

	/** Whole weight budget shared by a flat chain, small enough to never outweigh a real weight. */
	private static final double ZERO_MINLEN_WEIGHT_EPSILON = .001;

	interface ResultConverter {
		SvekLayoutResponse convert(DrawGraph drawGraph, BuildResult build);
	}

	/** A recorded cluster with everything that was declared while it was open. */
	private static final class ClusterRecord {
		private final ClusterSpec spec;
		private final ClusterRecord parent;
		private final List<String> nodeIds = new ArrayList<>();
		private final List<ClusterRecord> children = new ArrayList<>();
		private final List<RankRecord> ranks = new ArrayList<>();

		private ClusterRecord(ClusterSpec spec, ClusterRecord parent) {
			this.spec = spec;
			this.parent = parent;
		}
	}

	/** A recorded rank group and the cluster that owns it, null at graph level. */
	private static final class RankRecord {
		private final RankSpec spec;
		private final ClusterRecord owner;

		private RankRecord(RankSpec spec, ClusterRecord owner) {
			this.spec = spec;
			this.owner = owner;
		}
	}

	private final Map<String, NodeSpec> nodes = new LinkedHashMap<>();
	private final List<String> rootNodeIds = new ArrayList<>();
	private final List<ClusterRecord> rootClusters = new ArrayList<>();
	private final List<RankRecord> rootRanks = new ArrayList<>();
	private final List<EdgeSpec> edges = new ArrayList<>();
	private final Deque<ClusterRecord> clusterStack = new ArrayDeque<>();
	private final Map<String, ClusterRecord> nodeOwners = new LinkedHashMap<>();
	private final Set<String> usedIds = new LinkedHashSet<>();
	private final ResultConverter resultConverter;
	private GraphSpec graph;
	private String declineCode;
	private String declineMessage;
	private boolean layoutCalled;

	/** Instantiated reflectively by {@code SvekLayoutBuilders#graphSupport()}, hence public. */
	public GraphSupportSvekLayoutBuilder() {
		this((drawGraph, build) -> build.convert(drawGraph));
	}

	GraphSupportSvekLayoutBuilder(ResultConverter resultConverter) {
		this.resultConverter = resultConverter;
	}

	// Recording

	public void graph(GraphSpec graph) {
		if (layoutCalled) {
			decline("invalid-order", "graph called after layout");
			return;
		}
		if (this.graph != null) {
			decline("graph-already-set", "graph may only be set once");
			return;
		}
		if (graph == null) {
			decline("invalid-input", "graph must not be null");
			return;
		}
		this.graph = graph;
	}

	public void node(NodeSpec node) {
		if (canAdd("node", node, "node must not be null") == false)
			return;
		if (reserveId(node.id, "node") == false)
			return;
		nodes.put(node.id, node);
		nodeOwners.put(node.id, clusterStack.peek());
		if (clusterStack.isEmpty())
			rootNodeIds.add(node.id);
		else
			clusterStack.peek().nodeIds.add(node.id);
	}

	public void beginCluster(ClusterSpec cluster) {
		if (canAdd("beginCluster", cluster, "cluster must not be null") == false)
			return;
		if (reserveId(cluster.id, "cluster") == false)
			return;
		final ClusterRecord record = new ClusterRecord(cluster, clusterStack.peek());
		if (clusterStack.isEmpty())
			rootClusters.add(record);
		else
			clusterStack.peek().children.add(record);
		clusterStack.push(record);
	}

	public void endCluster() {
		if (canAdd("endCluster") == false)
			return;
		if (clusterStack.isEmpty()) {
			decline("invalid-input", "endCluster has no matching beginCluster");
			return;
		}
		clusterStack.pop();
	}

	public void rankGroup(RankSpec rank) {
		if (canAdd("rankGroup", rank, "rank must not be null") == false)
			return;
		final ClusterRecord owner = clusterStack.peek();
		final RankRecord record = new RankRecord(rank, owner);
		if (owner == null)
			rootRanks.add(record);
		else
			owner.ranks.add(record);
	}

	public void edge(EdgeSpec edge) {
		if (canAdd("edge", edge, "edge must not be null") == false)
			return;
		if (reserveId(edge.id, "edge") == false)
			return;
		edges.add(edge);
	}

	private boolean canAdd(String operation, Object spec, String nullMessage) {
		// An out of order call is reported first, even when its spec is missing as well.
		if (canAdd(operation) == false)
			return false;
		if (spec == null) {
			decline("invalid-input", nullMessage);
			return false;
		}
		return true;
	}

	private boolean canAdd(String operation) {
		if (layoutCalled) {
			decline("invalid-order", operation + " called after layout");
			return false;
		}
		if (graph == null) {
			decline("invalid-order", operation + " called before graph");
			return false;
		}
		return true;
	}

	/** Nodes, clusters and edges share one Svek id space, so an id may only be taken once. */
	private boolean reserveId(String id, String kind) {
		if (validId(id) == false) {
			decline("invalid-input", kind + " id must not be empty");
			return false;
		}
		if (usedIds.add(id) == false) {
			decline("invalid-input", "duplicate id " + id);
			return false;
		}
		return true;
	}

	/** Only the first problem is kept: it is the one that explains why the request was refused. */
	private void decline(String code, String message) {
		if (declineCode == null) {
			declineCode = code;
			declineMessage = message;
		}
	}

	// Layout

	public SvekLayoutResponse layout() {
		if (layoutCalled)
			return SvekLayoutResponse.declined("invalid-order", "layout may only be called once");
		layoutCalled = true;
		validate();
		if (declineCode != null)
			return SvekLayoutResponse.declined(declineCode, declineMessage);

		try {
			final BuildResult build = new GraphAssembler().build();
			final DrawGraph drawGraph = Layout.DOT.getLayoutEngine().layout(build.graphviz);
			return resultConverter.convert(drawGraph, build);
		} catch (ExecuteException e) {
			return SvekLayoutResponse.declined("layout-error", e.getMessage());
		}
	}

	/** Test seam: the translated Graphper graph, without running the layout engine on it. */
	Graphviz buildGraphviz() {
		validate();
		if (declineCode != null)
			throw new IllegalStateException(declineCode + ": " + declineMessage);
		return new GraphAssembler().build().graphviz;
	}

	/** The Graphper graph to lay out, plus the mapping needed to read its geometry back. */
	static final class BuildResult {
		private final Graphviz graphviz;
		final Direction direction;
		final Map<Node, String> nodeIds;
		final Map<Cluster, String> clusterIds;
		final Map<Line, String> lineIds;
		final Map<String, Node> bodyCells;
		final Map<Line, Map<LabelPosition, Node>> labelCells;
		final Map<Line, Map<LabelPosition, LabelSpec>> labelSpecs;
		final Map<String, NodeSpec> nodeSpecs;
		final Map<String, ClusterSpec> clusterSpecs;

		BuildResult(Graphviz graphviz, Direction direction, Map<Node, String> nodeIds,
				Map<Cluster, String> clusterIds, Map<Line, String> lineIds,
				Map<String, Node> bodyCells, Map<Line, Map<LabelPosition, Node>> labelCells,
				Map<Line, Map<LabelPosition, LabelSpec>> labelSpecs, Map<String, NodeSpec> nodeSpecs,
				Map<String, ClusterSpec> clusterSpecs) {
			this.graphviz = graphviz;
			this.direction = direction;
			this.nodeIds = nodeIds;
			this.clusterIds = clusterIds;
			this.lineIds = lineIds;
			this.bodyCells = bodyCells;
			this.labelCells = labelCells;
			this.labelSpecs = labelSpecs;
			this.nodeSpecs = nodeSpecs;
			this.clusterSpecs = clusterSpecs;
		}

		SvekLayoutResponse convert(DrawGraph drawGraph) {
			return SvekLayoutResponse.success(GraphSupportLayoutResultConverter.convert(drawGraph, this));
		}
	}

	// Graph assembly

	/**
	 * Translates the recorded request into Graphper objects.
	 *
	 * The maps filled here are the contract with {@link GraphSupportLayoutResultConverter}: they are
	 * the only way back from a created Graphper object to the Svek identifier that asked for it.
	 */
	private final class GraphAssembler {
		private final InternalIds internalIds = allocateInternalIds();
		private final Map<String, Node> graphNodes = new LinkedHashMap<>();
		private final Map<Node, String> nodeIds = new LinkedHashMap<>();
		private final Map<String, Node> bodyCells = new LinkedHashMap<>();
		private final Map<Cluster, String> clusterIds = new LinkedHashMap<>();
		private final Map<String, ClusterSpec> clusterSpecs = new LinkedHashMap<>();
		private final Map<Line, String> lineIds = new LinkedHashMap<>();
		private final Map<Line, Map<LabelPosition, Node>> labelCells = new LinkedHashMap<>();
		private final Map<Line, Map<LabelPosition, LabelSpec>> labelSpecs = new LinkedHashMap<>();

		private BuildResult build() {
			// Every node exists before the containers claim it, because membership is by reference.
			addNodes();
			final GraphvizBuilder graphBuilder = newGraphBuilder();
			for (String nodeId : rootNodeIds)
				graphBuilder.addNode(graphNodes.get(nodeId));
			for (ClusterRecord cluster : rootClusters)
				graphBuilder.cluster(newCluster(cluster));
			addRanks(graphBuilder, rootRanks);
			addEdges(graphBuilder);
			return new BuildResult(graphBuilder.build(), graph.direction, nodeIds, clusterIds, lineIds, bodyCells,
					labelCells, labelSpecs, new LinkedHashMap<>(nodes), clusterSpecs);
		}

		private GraphvizBuilder newGraphBuilder() {
			return Graphviz.digraph().layout(Layout.DOT)
					.rankdir(graph.direction == Direction.TOP_TO_BOTTOM ? Rankdir.TB : Rankdir.LR)
					.splines(splines(graph.routing))
					.nodeSep(inches(graph.nodeSep))
					.rankSep(inches(graph.rankSep));
		}

		private void addNodes() {
			for (NodeSpec spec : nodes.values()) {
				final Node node = newNode(spec);
				graphNodes.put(spec.id, node);
				nodeIds.put(node, spec.id);
			}
		}

		private Node newNode(NodeSpec spec) {
			final NodeBuilder builder = Node.builder().id(spec.id).shape(outerShape(spec))
					.width(inches(spec.width)).height(inches(spec.height)).fixedSize(true);
			if (spec.cells.isEmpty() == false)
				builder.assemble(newCells(spec));
			if (spec.shape == Shape.ROUNDED_RECTANGLE)
				builder.style(NodeStyle.ROUNDED);
			return builder.build();
		}

		/** Cells draw the whole content of a node, so the outline is dropped unless Svek reads it back. */
		private NodeShapeEnum outerShape(NodeSpec spec) {
			if (spec.cells.isEmpty() || isPolygon(spec.shape))
				return shape(spec.shape);
			return NodeShapeEnum.PLAIN;
		}

		private Assemble newCells(NodeSpec spec) {
			final Map<String, String> cellIds = internalIds.cellIds.get(spec.id);
			final AssembleBuilder assemble = Assemble.builder()
					.width(inches(spec.width)).height(inches(spec.height));
			for (CellSpec cell : spec.cells) {
				final Node cellNode = rectangularCell(cellIds.get(cell.id), cell.width, cell.height);
				assemble.addCell(inches(cell.x), inches(cell.y), cellNode);
				if (cell.id.equals(spec.bodyCellId))
					bodyCells.put(spec.id, cellNode);
			}
			return assemble.build();
		}

		private Cluster newCluster(ClusterRecord record) {
			final ClusterSpec spec = record.spec;
			final ClusterBuilder builder = Cluster.builder().id(spec.id)
					.margin(inches(spec.horizontalMargin), inches(spec.verticalMargin))
					.labeljust(labeljust(spec.titleAlignment));
			// An empty assemble of the title size is how the header band is reserved above the content.
			if (spec.titleWidth > 0 && spec.titleHeight > 0)
				builder.assemble(footprint(spec.titleWidth, spec.titleHeight + spec.contentTopPadding));
			for (String nodeId : record.nodeIds)
				builder.addNode(graphNodes.get(nodeId));
			for (ClusterRecord child : record.children)
				builder.cluster(newCluster(child));
			addRanks(builder, record.ranks);
			final Cluster cluster = builder.build();
			clusterIds.put(cluster, spec.id);
			clusterSpecs.put(spec.id, spec);
			return cluster;
		}

		private void addRanks(GraphContainerBuilder<?, ?> builder, List<RankRecord> ranks) {
			for (RankRecord record : ranks) {
				final SubgraphBuilder subgraph = Subgraph.builder().rank(rank(record.spec.rank));
				for (String nodeId : record.spec.nodeIds)
					subgraph.addNode(graphNodes.get(nodeId));
				builder.subgraph(subgraph.build());
			}
		}

		private void addEdges(GraphvizBuilder graphBuilder) {
			final Map<String, Double> weights = effectiveEdgeWeights();
			for (EdgeSpec spec : edges) {
				final LineBuilder lineBuilder = newLine(spec, weights.get(spec.id));
				final Map<LabelPosition, Node> cells = addLabels(lineBuilder, spec.labels);
				final Line line = lineBuilder.build();
				graphBuilder.addLine(line);
				// Scaffolding lines only shape the layout and have no Svek counterpart.
				if (spec.layoutOnly == false) {
					lineIds.put(line, spec.id);
					labelCells.put(line, cells);
					labelSpecs.put(line, labelsByPosition(spec.labels));
				}
			}
		}

		/** Svek draws its own arrow heads and decorations, so a line is only asked for a route. */
		private LineBuilder newLine(EdgeSpec spec, double weight) {
			final LineBuilder builder = Line.builder(graphNodes.get(spec.tailId), graphNodes.get(spec.headId))
					.id(spec.id).dir(Dir.NONE).minlen(spec.minlen).constraint(spec.constraint).weight(weight);
			if (spec.sameTail != null)
				builder.sameTail(spec.sameTail);
			if (spec.sameHead != null)
				builder.sameHead(spec.sameHead);
			if (spec.visible == false)
				builder.style(LineStyle.INVIS);
			if (spec.tailCellId != null)
				builder.tailCell(internalIds.cellIds.get(spec.tailId).get(spec.tailCellId));
			if (spec.headCellId != null)
				builder.headCell(internalIds.cellIds.get(spec.headId).get(spec.headCellId));
			return builder;
		}

		/** Returns the endpoint label cells, the only labels whose box can be read back. */
		private Map<LabelPosition, Node> addLabels(LineBuilder builder, List<LabelSpec> specs) {
			final Map<LabelPosition, Node> cells = new EnumMap<>(LabelPosition.class);
			final List<FloatLabel> floats = new ArrayList<>();
			for (LabelSpec spec : specs) {
				if (spec.position == LabelPosition.MAIN)
					builder.table(mainLabel(spec));
				else {
					final Node cell = rectangularCell(internalIds.next(), spec.width, spec.height);
					floats.add(endpointLabel(spec, cell));
					cells.put(spec.position, cell);
				}
			}
			if (floats.isEmpty() == false)
				builder.floatLabels(floats.toArray(new FloatLabel[floats.size()]));
			return cells;
		}
	}

	/** A main label is a fixed size borderless table, the same model native dot uses. */
	private static Table mainLabel(LabelSpec spec) {
		return Html.table().border(0).cellBorder(0).cellSpacing(0).fixedSize(true)
				.width(spec.width).height(spec.height).tr(Html.td());
	}

	/** An endpoint label is assembled from a real cell, so its box survives the layout. */
	private static FloatLabel endpointLabel(LabelSpec spec, Node cell) {
		final Assemble assemble = Assemble.builder().width(inches(spec.width)).height(inches(spec.height))
				.addCell(0, 0, cell).build();
		return FloatLabel.builder().assemble(assemble)
				.tend(spec.position == LabelPosition.TAIL ? Tend.TAIL : Tend.HEAD).build();
	}

	private static Map<LabelPosition, LabelSpec> labelsByPosition(List<LabelSpec> specs) {
		final Map<LabelPosition, LabelSpec> labels = new EnumMap<>(LabelPosition.class);
		for (LabelSpec spec : specs)
			labels.put(spec.position, spec);
		return labels;
	}

	private static Node rectangularCell(String id, double width, double height) {
		return Node.builder().id(id).shape(NodeShapeEnum.RECT)
				.width(inches(width)).height(inches(height)).fixedSize(true).build();
	}

	/** Svek reads the real corners of these shapes back, so Graphper has to draw them itself. */
	private static boolean isPolygon(Shape shape) {
		return shape == Shape.OCTAGON || shape == Shape.HEXAGON;
	}

	// Internal identifiers

	/** Graphper needs an id for every generated cell, and those ids share the Svek id space. */
	private static final class InternalIds {
		private final Set<String> reserved;
		/** Cell ids are scoped by node, because two nodes may use the same Svek cell id. */
		private final Map<String, Map<String, String>> cellIds = new LinkedHashMap<>();
		private int sequence;

		private InternalIds(Set<String> reserved) {
			this.reserved = reserved;
		}

		private String next() {
			String candidate;
			do {
				candidate = "__plantuml_internal_" + sequence++;
			} while (reserved.add(candidate) == false);
			return candidate;
		}
	}

	private InternalIds allocateInternalIds() {
		final InternalIds result = new InternalIds(new LinkedHashSet<>(usedIds));
		for (NodeSpec node : nodes.values()) {
			final Map<String, String> nodeCellIds = new LinkedHashMap<>();
			for (CellSpec cell : node.cells)
				nodeCellIds.put(cell.id, result.next());
			result.cellIds.put(node.id, nodeCellIds);
		}
		return result;
	}

	// Edge weights

	/**
	 * A zero minlen edge asks for both endpoints on the same rank, which becomes impossible as soon
	 * as a longer constrained path also connects two members of the same flat chain. Biasing every
	 * chain edge by its distance to the conflicting target makes the layout stretch the far end of
	 * the chain, the way dot does, instead of an arbitrary edge in the middle.
	 */
	private Map<String, Double> effectiveEdgeWeights() {
		final Map<String, Double> weights = new LinkedHashMap<>();
		for (EdgeSpec spec : edges)
			weights.put(spec.id, spec.weight);
		if (hasZeroMinlenConflict() == false)
			return weights;

		final StronglyConnectedComponents components = new StronglyConnectedComponents(zeroMinlenGraph());
		final Map<String, Integer> biases = zeroMinlenBiases(components, componentGraph(components));
		final double biasStep = ZERO_MINLEN_WEIGHT_EPSILON / (components.count() + 1);
		for (EdgeSpec spec : edges) {
			final Integer bias = biases.get(spec.id);
			if (bias != null)
				weights.put(spec.id, spec.weight + biasStep * bias);
		}
		return weights;
	}

	private boolean hasZeroMinlenConflict() {
		boolean hasRankStep = false;
		boolean hasFlatEdge = false;
		for (EdgeSpec spec : edges) {
			if (isRankStepConstraint(spec))
				hasRankStep = true;
			if (isZeroMinlenConstraint(spec))
				hasFlatEdge = true;
		}
		return hasRankStep && hasFlatEdge;
	}

	private static boolean isRankStepConstraint(EdgeSpec spec) {
		return spec.constraint && spec.minlen > 0;
	}

	private static boolean isZeroMinlenConstraint(EdgeSpec spec) {
		return spec.constraint && spec.minlen == 0;
	}

	/** Adjacency of the edges that have to stay flat, over every recorded node. */
	private Map<String, List<String>> zeroMinlenGraph() {
		final Map<String, List<String>> result = new LinkedHashMap<>();
		for (String nodeId : nodes.keySet())
			result.put(nodeId, new ArrayList<>());
		for (EdgeSpec spec : edges)
			if (isZeroMinlenConstraint(spec))
				result.get(spec.tailId).add(spec.headId);
		return result;
	}

	/** One vertex per flat chain, one arc per flat edge that leaves its own chain. */
	private List<Set<Integer>> componentGraph(StronglyConnectedComponents components) {
		final List<Set<Integer>> result = new ArrayList<>();
		for (int i = 0; i < components.count(); i++)
			result.add(new LinkedHashSet<>());
		for (EdgeSpec spec : edges) {
			if (isZeroMinlenConstraint(spec) == false)
				continue;
			final int tail = components.component(spec.tailId);
			final int head = components.component(spec.headId);
			if (tail != head)
				result.get(tail).add(head);
		}
		return result;
	}

	private Map<String, Integer> zeroMinlenBiases(StronglyConnectedComponents components,
			List<Set<Integer>> componentGraph) {
		final Map<String, Integer> biases = new LinkedHashMap<>();
		for (EdgeSpec conflict : edges) {
			if (isRankStepConstraint(conflict) == false)
				continue;
			final int source = components.component(conflict.tailId);
			final int target = components.component(conflict.headId);
			if (source == target)
				continue;
			final int[] distances = distancesTo(target, componentGraph);
			// Nothing to break unless the conflicting endpoints are themselves joined by flat edges.
			if (distances[source] < 0)
				continue;
			addBiases(biases, components, reachableFrom(source, componentGraph), distances);
		}
		return biases;
	}

	/** A flat edge keeps the longest distance to a conflict target it can still reach. */
	private void addBiases(Map<String, Integer> biases, StronglyConnectedComponents components, boolean[] reachable,
			int[] distances) {
		for (EdgeSpec spec : edges) {
			if (isZeroMinlenConstraint(spec) == false)
				continue;
			final int tail = components.component(spec.tailId);
			final int head = components.component(spec.headId);
			if (tail == head || reachable[tail] == false || distances[head] < 0)
				continue;
			final Integer current = biases.get(spec.id);
			if (current == null || distances[head] > current)
				biases.put(spec.id, distances[head]);
		}
	}

	private static boolean[] reachableFrom(int source, List<Set<Integer>> graph) {
		final boolean[] reachable = new boolean[graph.size()];
		final Deque<Integer> pending = new ArrayDeque<>();
		pending.push(source);
		while (pending.isEmpty() == false) {
			final int component = pending.pop();
			if (reachable[component])
				continue;
			reachable[component] = true;
			for (Integer next : graph.get(component))
				pending.push(next);
		}
		return reachable;
	}

	/** Longest distance from every component to {@code target}, or -1 when it cannot reach it. */
	private static int[] distancesTo(int target, List<Set<Integer>> graph) {
		final int[] distances = new int[graph.size()];
		Arrays.fill(distances, -1);
		distances[target] = 0;
		boolean changed;
		do {
			changed = false;
			for (int component = 0; component < graph.size(); component++)
				for (Integer next : graph.get(component))
					if (distances[next] >= 0 && distances[component] < distances[next] + 1) {
						distances[component] = distances[next] + 1;
						changed = true;
					}
		} while (changed);
		return distances;
	}

	/**
	 * Tarjan components of the flat edges: every node of a component is forced onto the same rank.
	 *
	 * The depth first search is kept on an explicit stack because a diagram can chain far more nodes
	 * than the Java stack can nest recursive calls.
	 */
	private static final class StronglyConnectedComponents {
		private final Map<String, List<String>> graph;
		private final Map<String, Integer> indices = new LinkedHashMap<>();
		private final Map<String, Integer> lowLinks = new LinkedHashMap<>();
		private final Map<String, Integer> components = new LinkedHashMap<>();
		private final Set<String> onStack = new LinkedHashSet<>();
		private final Deque<String> stack = new ArrayDeque<>();
		private int nextIndex;
		private int componentCount;

		private StronglyConnectedComponents(Map<String, List<String>> graph) {
			this.graph = graph;
			for (String node : graph.keySet())
				if (indices.containsKey(node) == false)
					connect(node);
		}

		private void connect(String root) {
			final Deque<String> pending = new ArrayDeque<>();
			final Deque<Iterator<String>> successors = new ArrayDeque<>();
			pending.push(root);
			while (pending.isEmpty() == false) {
				final String node = pending.peek();
				if (indices.containsKey(node) == false)
					enter(node, successors);
				if (successors.peek().hasNext()) {
					follow(node, successors.peek().next(), pending);
					continue;
				}
				if (isComponentRoot(node))
					popComponent(node);
				pending.pop();
				successors.pop();
				propagateLowLink(node, pending);
			}
		}

		private void enter(String node, Deque<Iterator<String>> successors) {
			indices.put(node, nextIndex);
			lowLinks.put(node, nextIndex++);
			stack.push(node);
			onStack.add(node);
			successors.push(graph.get(node).iterator());
		}

		private void follow(String node, String next, Deque<String> pending) {
			if (indices.containsKey(next) == false)
				pending.push(next);
			else if (onStack.contains(next))
				lowLinks.put(node, Math.min(lowLinks.get(node), indices.get(next)));
		}

		private boolean isComponentRoot(String node) {
			return lowLinks.get(node).equals(indices.get(node));
		}

		private void popComponent(String root) {
			String member;
			do {
				member = stack.pop();
				onStack.remove(member);
				components.put(member, componentCount);
			} while (member.equals(root) == false);
			componentCount++;
		}

		/** Propagate the child low link when returning to its search parent. */
		private void propagateLowLink(String node, Deque<String> pending) {
			if (pending.isEmpty())
				return;
			final String parent = pending.peek();
			lowLinks.put(parent, Math.min(lowLinks.get(parent), lowLinks.get(node)));
		}

		private int count() {
			return componentCount;
		}

		private int component(String node) {
			return components.get(node);
		}
	}

	// Validation

	private void validate() {
		if (declineCode != null)
			return;
		// Each step assumes the earlier ones held, so the first failure has to stop the chain.
		if (validGraph() && validClusters() && validNodes() && validRanks())
			validEdges();
	}

	private boolean validGraph() {
		if (graph == null) {
			decline("invalid-order", "graph must be set before layout");
			return false;
		}
		if (graph.direction == null || graph.routing == null || finiteNonNegative(graph.nodeSep) == false
				|| finitePositive(graph.rankSep) == false) {
			decline("invalid-input", "invalid graph attributes");
			return false;
		}
		return true;
	}

	private boolean validClusters() {
		if (clusterStack.isEmpty() == false) {
			decline("invalid-input", "cluster stack is not balanced");
			return false;
		}
		for (ClusterRecord cluster : allClusters())
			if (validSpacing(cluster.spec) == false) {
				decline("invalid-input", "invalid cluster title dimensions for " + cluster.spec.id);
				return false;
			}
		return true;
	}

	private static boolean validSpacing(ClusterSpec spec) {
		return finiteNonNegative(spec.titleWidth) && finiteNonNegative(spec.titleHeight)
				&& finiteNonNegative(spec.horizontalMargin) && finiteNonNegative(spec.verticalMargin)
				&& finiteNonNegative(spec.contentTopPadding);
	}

	private boolean validNodes() {
		for (NodeSpec node : nodes.values()) {
			if (node.shape == null || finitePositive(node.width) == false || finitePositive(node.height) == false
					|| node.cells == null) {
				decline("invalid-input", "node cells must not be null");
				return false;
			}
			if (validCells(node) == false)
				return false;
		}
		return true;
	}

	private boolean validCells(NodeSpec node) {
		final Set<String> cellIds = new LinkedHashSet<>();
		for (CellSpec cell : node.cells)
			if (validCell(cell) == false || cellIds.add(cell.id) == false) {
				decline("invalid-input", "invalid or duplicate cell id in node " + node.id);
				return false;
			}
		if (node.bodyCellId != null && cellIds.contains(node.bodyCellId) == false) {
			decline("invalid-input", "unknown body cell " + node.bodyCellId + " in node " + node.id);
			return false;
		}
		return true;
	}

	private static boolean validCell(CellSpec cell) {
		return cell != null && validId(cell.id) && finiteNonNegative(cell.x) && finiteNonNegative(cell.y)
				&& finitePositive(cell.width) && finitePositive(cell.height);
	}

	private boolean validRanks() {
		for (RankRecord rank : allRanks()) {
			if (rank.spec.rank == null || rank.spec.nodeIds == null) {
				decline("invalid-input", "rank must have a kind and nodes");
				return false;
			}
			for (String nodeId : rank.spec.nodeIds)
				if (nodes.containsKey(nodeId) == false || isOwnedWithin(nodeOwners.get(nodeId), rank.owner) == false) {
					decline("invalid-input", "rank node is not directly owned by its container: " + nodeId);
					return false;
				}
		}
		return true;
	}

	private boolean validEdges() {
		for (EdgeSpec edge : edges) {
			if (edge.labels == null) {
				decline("invalid-input", "edge labels must not be null");
				return false;
			}
			if (validLabels(edge) == false)
				return false;
			if (edge.minlen < 0 || nodes.containsKey(edge.tailId) == false || nodes.containsKey(edge.headId) == false) {
				decline("unknown-endpoint", "unknown endpoint on edge " + edge.id);
				return false;
			}
			if (knownCell(edge.tailId, edge.tailCellId) == false || knownCell(edge.headId, edge.headCellId) == false) {
				decline("unknown-cell", "unknown endpoint cell on edge " + edge.id);
				return false;
			}
		}
		return true;
	}

	private boolean validLabels(EdgeSpec edge) {
		for (LabelSpec label : edge.labels)
			if (label == null || label.position == null || finitePositive(label.width) == false
					|| finitePositive(label.height) == false) {
				decline("invalid-input", "edge label must not be null");
				return false;
			}
		return true;
	}

	private boolean knownCell(String nodeId, String cellId) {
		if (cellId == null)
			return true;
		final NodeSpec node = nodes.get(nodeId);
		for (CellSpec cell : node.cells)
			if (cellId.equals(cell.id))
				return true;
		return false;
	}

	/** A rank group may only order nodes of its own cluster, or of a cluster nested in it. */
	private static boolean isOwnedWithin(ClusterRecord nodeOwner, ClusterRecord rankOwner) {
		if (rankOwner == null)
			return true;
		for (ClusterRecord current = nodeOwner; current != null; current = current.parent)
			if (current == rankOwner)
				return true;
		return false;
	}

	private List<ClusterRecord> allClusters() {
		final List<ClusterRecord> result = new ArrayList<>();
		for (ClusterRecord cluster : rootClusters)
			collectClusters(cluster, result);
		return result;
	}

	private static void collectClusters(ClusterRecord cluster, List<ClusterRecord> result) {
		result.add(cluster);
		for (ClusterRecord child : cluster.children)
			collectClusters(child, result);
	}

	private List<RankRecord> allRanks() {
		final List<RankRecord> result = new ArrayList<>(rootRanks);
		for (ClusterRecord cluster : rootClusters)
			collectRanks(cluster, result);
		return result;
	}

	private static void collectRanks(ClusterRecord cluster, List<RankRecord> result) {
		result.addAll(cluster.ranks);
		for (ClusterRecord child : cluster.children)
			collectRanks(child, result);
	}

	private static boolean validId(String id) {
		return id != null && id.length() > 0;
	}

	private static boolean finitePositive(double value) {
		return Double.isNaN(value) == false && Double.isInfinite(value) == false && value > 0;
	}

	private static boolean finiteNonNegative(double value) {
		return Double.isNaN(value) == false && Double.isInfinite(value) == false && value >= 0;
	}

	// Attribute mapping

	private static double inches(double pixels) {
		return pixels / Graphviz.PIXEL;
	}

	private static Assemble footprint(double width, double height) {
		return Assemble.builder().width(inches(width)).height(inches(height)).build();
	}

	private static Splines splines(Routing routing) {
		switch (routing) {
		case POLYLINE:
			return Splines.POLYLINE;
		case ORTHO:
			return Splines.ORTHO;
		default:
			return Splines.ROUNDED;
		}
	}

	private static NodeShapeEnum shape(Shape shape) {
		switch (shape) {
		case POINT:
			return NodeShapeEnum.POINT;
		case ELLIPSE:
			return NodeShapeEnum.ELLIPSE;
		case CIRCLE:
			return NodeShapeEnum.CIRCLE;
		case DIAMOND:
			return NodeShapeEnum.DIAMOND;
		case OCTAGON:
			return NodeShapeEnum.OCTAGON;
		case HEXAGON:
			return NodeShapeEnum.HEXAGON;
		default:
			return NodeShapeEnum.RECT;
		}
	}

	private static org.graphper.api.attributes.Rank rank(Rank rank) {
		return org.graphper.api.attributes.Rank.valueOf(rank.name());
	}

	private static Labeljust labeljust(Alignment alignment) {
		if (alignment == Alignment.LEFT)
			return Labeljust.LEFT;
		if (alignment == Alignment.RIGHT)
			return Labeljust.RIGHT;
		return Labeljust.CENTER;
	}
}
