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
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.abel.Together;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutBuilder;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.GraphSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.RankSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResponse;

/** Materializes the Svek model before invoking a provider, preserving container and edge order. */
final class SvekLayoutEmitter {
	// Graphviz uses an 8-point CL_OFFSET for the p0/p1 protection clusters. Graphper
	// applies margins to nested layout-only clusters slightly differently, so these
	// values reproduce the same effective clearance around visible PlantUML clusters.
	private static final double PROTECTION_OUTER_HORIZONTAL = 7.5;
	private static final double PROTECTION_OUTER_VERTICAL = 7;
	private static final double PROTECTION_INNER_HORIZONTAL = 6.5;
	private static final double PROTECTION_INNER_VERTICAL = 5.25;
	private final DotStringFactory dot;
	private final DiagramType type;
	private final StringBounder stringBounder;

	SvekLayoutEmitter(DotStringFactory dot, DiagramType type, StringBounder stringBounder) {
		this.dot = dot;
		this.type = type;
		this.stringBounder = stringBounder;
	}

	SvekLayoutResponse emit(SvekLayoutBuilder builder) {
		final EmissionPlan plan = materializePlan();
		builder.graph(plan.graph);
		replayCluster(plan.root, builder);
		for (EdgeSpec edge : plan.edges)
			builder.edge(edge);
		return builder.layout();
	}

	private EmissionPlan materializePlan() {
		final GraphSpec graph = dot.toLayoutGraphSpec(stringBounder);
		final SwimlaneScaffold swimlanes = new SwimlaneScaffold();
		final ClusterPlan root = materializeCluster(dot.getRootCluster(), true, swimlanes);
		addRootScaffold(root, swimlanes);
		return new EmissionPlan(graph, root, materializeEdges(swimlanes));
	}

	private void addRootScaffold(ClusterPlan root, SwimlaneScaffold swimlanes) {
		// Lane boundary points must share ranks across sibling containers.
		root.nodes.addAll(swimlanes.rootNodes);
		if (swimlanes.minPoints.isEmpty() == false)
			root.ranks.add(new RankSpec(SvekLayoutModel.Rank.MIN, swimlanes.minPoints));
		if (swimlanes.maxPoints.isEmpty() == false)
			root.ranks.add(new RankSpec(SvekLayoutModel.Rank.MAX, swimlanes.maxPoints));
	}

	private List<EdgeSpec> materializeEdges(SwimlaneScaffold swimlanes) {
		final List<EdgeSpec> edges = new ArrayList<>();
		for (SvekEdge edge : dot.getBibliotekon().allLines())
			edges.add(edge.toLayoutSpec());
		edges.addAll(swimlanes.edges);
		return edges;
	}

	private ClusterPlan materializeCluster(Cluster cluster, boolean root, SwimlaneScaffold swimlanes) {
		final ClusterSpec spec = root ? null : cluster.toLayoutSpec();
		final ClusterPlan plan = new ClusterPlan(spec);
		final TogetherPlans togetherPlans = new TogetherPlans(plan, cluster.getClusterId());
		addNodes(cluster, plan, togetherPlans);
		if (root == false)
			addCenterNode(cluster, plan);
		plan.ranks.addAll(cluster.toLayoutRankSpecs(dot.getBibliotekon().allLines()));
		if (root == false)
			addClusterScaffold(cluster, plan, swimlanes);
		addChildren(cluster, swimlanes, togetherPlans);
		return plan;
	}

	private void addNodes(Cluster cluster, ClusterPlan plan, TogetherPlans togetherPlans) {
		for (SvekNode node : cluster.getNodes())
			togetherPlans.of(node.getTogether()).nodes.add(node.toLayoutSpec());
	}

	private void addCenterNode(Cluster cluster, ClusterPlan plan) {
		final NodeSpec center = cluster.toLayoutCenterNodeSpec(dot.getBibliotekon().allLines());
		if (center != null)
			plan.nodes.add(center);
	}

	private void addClusterScaffold(Cluster cluster, ClusterPlan plan, SwimlaneScaffold swimlanes) {
		if (cluster.hasLayoutPorts()) {
			addPortChain(cluster.getLayoutNodeIds(EntityPosition.getInputs()), cluster, swimlanes);
			addPortChain(cluster.getLayoutNodeIds(EntityPosition.getOutputs()), cluster, swimlanes);
		}
		if (cluster.usesSwimlanes(type))
			addSwimlaneScaffold(cluster, plan, swimlanes);
	}

	private void addChildren(Cluster cluster, SwimlaneScaffold swimlanes, TogetherPlans togetherPlans) {
		for (Cluster child : cluster.getChildren()) {
			final ClusterPlan owner = togetherPlans.of(child.getTogether());
			final ClusterPlan childPlan = materializeCluster(child, false, swimlanes);
			if (child.isPackedForLayout())
				owner.flatten(childPlan);
			else
				owner.children.add(child.needsLayoutProtection(type) ? protect(childPlan) : childPlan);
		}
	}

	private ClusterPlan protect(ClusterPlan child) {
		// Keep layout anchors outside the inner clearance wrapper; only visible nodes move inside.
		final ClusterPlan inner = new ClusterPlan(ClusterSpec.builder(child.spec.id + "-p1")
				.margins(PROTECTION_INNER_HORIZONTAL, PROTECTION_INNER_VERTICAL).layoutOnly(true).build());
		for (NodeSpec node : new ArrayList<>(child.nodes))
			if (node.layoutOnly == false) {
				child.nodes.remove(node);
				inner.nodes.add(node);
			}
		inner.ranks.addAll(child.ranks);
		child.ranks.clear();
		inner.children.addAll(child.children);
		child.children.clear();
		child.children.add(inner);

		final ClusterPlan outer = new ClusterPlan(ClusterSpec.builder(child.spec.id + "-p0")
				.margins(PROTECTION_OUTER_HORIZONTAL, PROTECTION_OUTER_VERTICAL).layoutOnly(true).build());
		outer.children.add(child);
		return outer;
	}

	private void addSwimlaneScaffold(Cluster cluster, ClusterPlan plan, SwimlaneScaffold swimlanes) {
		final String min = cluster.getMinPoint(type);
		final String max = cluster.getMaxPoint(type);
		final String source = cluster.getSourceInPoint(type);
		final String sink = cluster.getSinkInPoint(type);
		swimlanes.rootNodes.add(point(min));
		swimlanes.rootNodes.add(point(max));
		swimlanes.minPoints.add(min);
		swimlanes.maxPoints.add(max);
		plan.nodes.add(point(source));
		plan.nodes.add(point(sink));
		plan.ranks.add(new RankSpec(SvekLayoutModel.Rank.SOURCE, Collections.singletonList(source)));
		plan.ranks.add(new RankSpec(SvekLayoutModel.Rank.SINK, Collections.singletonList(sink)));
		swimlanes.edges.add(scaffoldEdge("swim-min-" + min, min, source, 999));
		swimlanes.edges.add(scaffoldEdge("swim-max-" + max, sink, max, 999));
	}

	private void addPortChain(List<String> ids, Cluster cluster, SwimlaneScaffold swimlanes) {
		for (int i = 1; i < ids.size(); i++)
			swimlanes.edges.add(scaffoldEdge("swim-chain-" + cluster.getClusterId() + '-' + ids.get(i),
					ids.get(i - 1), ids.get(i), 1));
		if (ids.isEmpty() == false)
			swimlanes.edges.add(scaffoldEdge("swim-center-" + cluster.getClusterId() + '-' + ids.get(0),
					ids.get(ids.size() - 1), Cluster.getSpecialPointId(cluster.getGroup()), 1));
	}

	private static NodeSpec point(String id) {
		return new NodeSpec(id, .72, .72, SvekLayoutModel.Shape.POINT, null,
				Collections.emptyList(), true);
	}

	private static EdgeSpec scaffoldEdge(String id, String tail, String head, double weight) {
		return EdgeSpec.builder(id, tail, head).visible(false).weight(weight).layoutOnly(true).build();
	}

	/**
	 * Members of a {@code together} block share an invisible wrapper so the engine keeps them adjacent.
	 * Nested blocks nest their wrappers, and every wrapper is created once per cluster.
	 */
	private static final class TogetherPlans {
		private final Map<Together, ClusterPlan> plans = new IdentityHashMap<>();
		private final ClusterPlan base;
		private final String clusterId;
		private int created;

		private TogetherPlans(ClusterPlan base, String clusterId) {
			this.base = base;
			this.clusterId = clusterId;
		}

		private ClusterPlan of(Together together) {
			if (together == null)
				return base;

			final ClusterPlan known = plans.get(together);
			if (known != null)
				return known;

			final ClusterPlan parent = of(together.getParent());
			final ClusterPlan result = new ClusterPlan(
					ClusterSpec.builder(clusterId + "t" + created++).layoutOnly(true).build());
			plans.put(together, result);
			parent.children.add(result);
			return result;
		}
	}

	private void replayCluster(ClusterPlan cluster, SvekLayoutBuilder builder) {
		if (cluster.spec != null)
			builder.beginCluster(cluster.spec);
		for (NodeSpec node : cluster.nodes)
			builder.node(node);
		for (RankSpec rank : cluster.ranks)
			builder.rankGroup(rank);
		for (ClusterPlan child : cluster.children)
			replayCluster(child, builder);
		if (cluster.spec != null)
			builder.endCluster();
	}

	private static final class EmissionPlan {
		private final GraphSpec graph;
		private final ClusterPlan root;
		private final List<EdgeSpec> edges;

		private EmissionPlan(GraphSpec graph, ClusterPlan root, List<EdgeSpec> edges) {
			this.graph = graph;
			this.root = root;
			this.edges = Collections.unmodifiableList(new ArrayList<>(edges));
		}
	}

	private static final class ClusterPlan {
		private final ClusterSpec spec;
		private final List<NodeSpec> nodes = new ArrayList<>();
		private final List<RankSpec> ranks = new ArrayList<>();
		private final List<ClusterPlan> children = new ArrayList<>();

		private ClusterPlan(ClusterSpec spec) {
			this.spec = spec;
		}

		private void flatten(ClusterPlan packed) {
			// Packed wrappers are not rendered, but their contents and constraints still participate.
			nodes.addAll(packed.nodes);
			ranks.addAll(packed.ranks);
			children.addAll(packed.children);
		}
	}

	private static final class SwimlaneScaffold {
		private final List<NodeSpec> rootNodes = new ArrayList<>();
		private final List<String> minPoints = new ArrayList<>();
		private final List<String> maxPoints = new ArrayList<>();
		private final List<EdgeSpec> edges = new ArrayList<>();
	}
}
