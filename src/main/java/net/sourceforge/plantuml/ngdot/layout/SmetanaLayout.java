/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.ngdot.layout;

import static gen.lib.cgraph.attr__c.agsafeset;
import static gen.lib.cgraph.edge__c.agedge;
import static gen.lib.cgraph.graph__c.agopen;
import static gen.lib.cgraph.node__c.agnode;
import static gen.lib.cgraph.node__c.agsubnode;
import static gen.lib.cgraph.subg__c.agparent;
import static gen.lib.cgraph.subg__c.agsubg;
import static gen.lib.gvc.gvc__c.gvContext;
import static gen.lib.gvc.gvlayout__c.gvLayoutJobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_GVC_s;
import h.ST_bezier;
import h.ST_pointf;
import h.ST_textlabel_t;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.ngdot.attr.DotNumbers;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult.ClusterBox;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult.EdgeRoute;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult.NodeBox;
import net.sourceforge.plantuml.ngdot.model.DotAttributes;
import net.sourceforge.plantuml.ngdot.model.DotEdge;
import net.sourceforge.plantuml.ngdot.model.DotGraph;
import net.sourceforge.plantuml.ngdot.model.DotNode;
import net.sourceforge.plantuml.ngdot.model.DotSubgraph;
import net.sourceforge.plantuml.svek.SvekUtils;
import net.sourceforge.plantuml.teavm.TeaVM;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.Macro;

/**
 * Lays out a {@link DotGraph} with Smetana.
 * <p>
 * Smetana receives a simplified copy of the graph: nodes become boxes or
 * ellipses of the size computed by PlantUML, with generated names (a DOT name
 * starting with {@code %} would otherwise hit an unported part of cgraph), and
 * only the attributes that Smetana really implements are forwarded (see the
 * inventory in PLAN-NGDOT.md, phase 0). Everything else is drawn by PlantUML
 * from the result.
 */
public final class SmetanaLayout {

	// Same reason as in CucaDiagramFileMakerSmetana: layout runs are serialized,
	// and no monitor in the TeaVM C build.
	private static final Object LOCK = new Object();

	private static final List<String> COMPASS_POINTS = Arrays.asList("n", "ne", "e", "se", "s", "sw", "w", "nw", "c",
			"_");

	private final DotGraph graph;
	private final LayoutInput input;
	private final Rankdir rankdir;

	private Globals zz;
	private ST_Agraph_s root;
	private final Map<DotNode, ST_Agnode_s> nodes = new LinkedHashMap<DotNode, ST_Agnode_s>();
	private final Map<DotEdge, ST_Agedge_s> edges = new LinkedHashMap<DotEdge, ST_Agedge_s>();
	private final Map<DotSubgraph, ST_Agraph_s> clusters = new LinkedHashMap<DotSubgraph, ST_Agraph_s>();
	private final Map<DotSubgraph, ST_Agraph_s> ranksets = new LinkedHashMap<DotSubgraph, ST_Agraph_s>();

	private SmetanaLayout(DotGraph graph, LayoutInput input) {
		this.graph = graph;
		this.input = input;
		this.rankdir = Rankdir.fromString(graph.getAttributes().getString("rankdir"));
	}

	public static LayoutResult layout(DotGraph graph, LayoutInput input) {
		final SmetanaLayout layout = new SmetanaLayout(graph, input);
		if (TeaVM.isC())
			return layout.run();
		synchronized (LOCK) {
			return layout.run();
		}
	}

	private LayoutResult run() {
		if (graph.getNodes().size() == 0)
			return new LayoutResult(new XDimension2D(0, 0));

		zz = Globals.open();
		try {
			root = agopen(zz, new CString("g"), zz.Agdirected, null);
			setGraphAttributes();
			createClusters(graph.getRoot(), root);
			createNodes();
			fillRanksets();
			createEdges();
			final ST_GVC_s gvc = gvContext(zz);
			gvLayoutJobs(zz, gvc, root);
			return extract();
		} finally {
			Globals.close();
		}
	}

	private void set(ST_Agraph_s g, String name, String value) {
		agsafeset(zz, g, new CString(name), new CString(value), new CString(""));
	}

	private void set(ST_Agnode_s n, String name, String value) {
		agsafeset(zz, n, new CString(name), new CString(value), new CString(""));
	}

	private void set(ST_Agedge_s e, String name, String value) {
		agsafeset(zz, e, new CString(name), new CString(value), new CString(""));
	}

	private static String labelDim(XDimension2D dim) {
		return Macro.createHackInitDimensionFromLabel((int) Math.ceil(dim.getWidth()), (int) Math.ceil(dim.getHeight()))
				.getContent();
	}

	private void setGraphAttributes() {
		final DotAttributes attributes = graph.getAttributes();
		if (rankdir.isHorizontal())
			set(root, "rankdir", "LR");

		final Double nodesep = DotNumbers.parseFirst(attributes.getString("nodesep"));
		if (nodesep != null)
			set(root, "nodesep", SvekUtils.pixelToInches(Math.max(0.02, nodesep) * 72));

		final Double ranksep = DotNumbers.parseFirst(attributes.getString("ranksep"));
		if (ranksep != null)
			set(root, "ranksep", SvekUtils.pixelToInches(Math.max(0.02, ranksep) * 72));
	}

	/**
	 * Only clusters are given to Smetana: the other subgraphs are a way to group
	 * statements (or to set {@code rank}, which Smetana does not implement yet).
	 * A cluster nested in a plain subgraph is attached to the nearest enclosing
	 * cluster.
	 */
	private void createClusters(DotSubgraph parent, ST_Agraph_s smetanaParent) {
		for (DotSubgraph child : parent.getChildren()) {
			if (child.isCluster() && child.getNodes().size() > 0) {
				final ST_Agraph_s cluster = agsubg(zz, smetanaParent, new CString("cluster_" + clusters.size()), true);
				clusters.put(child, cluster);
				final XDimension2D label = input.getClusterLabel(child);
				if (label != null) {
					set(cluster, "label", labelDim(label));
					// Smetana reserves the space of the label at the top or at the bottom;
					// labeljust is not ported and is handled by the drawing
					final String labelloc = child.getAttributes().getString("labelloc");
					if (labelloc != null && labelloc.trim().toLowerCase().startsWith("b"))
						set(cluster, "labelloc", "b");
				}
				createClusters(child, cluster);
			} else {
				final String rank = rankKind(child);
				if (rank != null) {
					final ST_Agraph_s rankset = agsubg(zz, smetanaParent, new CString("rank_" + ranksets.size()),
							true);
					set(rankset, "rank", rank);
					ranksets.put(child, rankset);
				}
				createClusters(child, smetanaParent);
			}
		}
	}

	/**
	 * The {@code rank} of a plain subgraph, when it is one that Graphviz knows
	 * ({@code same}, {@code min}, {@code max}, {@code source}, {@code sink}).
	 */
	private static String rankKind(DotSubgraph subgraph) {
		final String rank = subgraph.getAttributes().getString("rank");
		if (rank == null)
			return null;
		final String s = rank.trim().toLowerCase();
		if (s.equals("same") || s.equals("min") || s.equals("max") || s.equals("source") || s.equals("sink"))
			return s;
		return null;
	}

	/**
	 * Puts the nodes of the {@code rank} subgraphs in their Smetana copy. As in
	 * Graphviz, a rank set only works inside one cluster: a node that belongs to
	 * another cluster than the one holding the subgraph is left out.
	 */
	private void fillRanksets() {
		for (Map.Entry<DotSubgraph, ST_Agraph_s> ent : ranksets.entrySet()) {
			final ST_Agraph_s parent = agparent(ent.getValue());
			for (DotNode node : ent.getKey().getNodes())
				if (ownerCluster(node) == parent)
					agsubnode(zz, ent.getValue(), nodes.get(node), true);
		}
	}

	/**
	 * A node belongs to at most one cluster chain: as in Graphviz, a node listed
	 * in two sibling clusters stays in the first one.
	 */
	private ST_Agraph_s ownerCluster(DotNode node) {
		ST_Agraph_s result = root;
		DotSubgraph current = null;
		for (Map.Entry<DotSubgraph, ST_Agraph_s> ent : clusters.entrySet()) {
			final DotSubgraph cluster = ent.getKey();
			if (cluster.contains(node) == false)
				continue;
			if (current == null || isAncestor(current, cluster)) {
				current = cluster;
				result = ent.getValue();
			}
		}
		return result;
	}

	private static boolean isAncestor(DotSubgraph ancestor, DotSubgraph child) {
		for (DotSubgraph s = child.getParent(); s != null; s = s.getParent())
			if (s == ancestor)
				return true;
		return false;
	}

	private void createNodes() {
		for (DotNode node : graph.getNodes()) {
			final ST_Agnode_s n = agnode(zz, ownerCluster(node), new CString("n" + nodes.size()), true);
			nodes.put(node, n);
			final XDimension2D size = input.getNodeSize(node);
			set(n, "shape", input.getNodeOutline(node) == LayoutInput.Outline.BOX ? "box" : "ellipse");
			// Without an explicit empty label, Smetana sizes the node for its name
			set(n, "label", "");
			set(n, "fixedsize", "true");
			set(n, "width", SvekUtils.pixelToInches(size.getWidth()));
			set(n, "height", SvekUtils.pixelToInches(size.getHeight()));
			final String group = node.getAttributes().getString("group");
			if (group != null && group.length() > 0)
				set(n, "group", group);
		}
	}

	private void createEdges() {
		for (DotEdge edge : graph.getEdges()) {
			final ST_Agedge_s e = agedge(zz, root, nodes.get(edge.getTail()), nodes.get(edge.getHead()), null, true);
			edges.put(edge, e);
			// The arrows are clipped by BezierClip and drawn by PlantUML. Both ends must
			// be set: Smetana's arrow_flags puts a normal arrow at both ends by default.
			set(e, "arrowhead", "none");
			set(e, "arrowtail", "none");

			final DotAttributes attributes = edge.getAttributes();
			final Double weight = DotNumbers.parseFirst(attributes.getString("weight"));
			if (weight != null)
				set(e, "weight", "" + Math.max(0, Math.round(weight)));

			final Double minlen = DotNumbers.parseFirst(attributes.getString("minlen"));
			if (minlen != null)
				set(e, "minlen", "" + Math.max(0, Math.round(minlen)));

			final String constraint = attributes.getString("constraint");
			if (constraint != null && DotNumbers.isFalse(constraint))
				set(e, "constraint", "false");

			final XDimension2D label = input.getEdgeLabel(edge);
			if (label != null)
				set(e, "label", labelDim(label));

			// The ports of a loop would need selfTop/selfLeft/selfBottom, which are not
			// ported yet: a loop is always drawn on the right of its node
			if (edge.getTail() != edge.getHead()) {
				final String tailport = compass(edge.getTailPort());
				if (tailport != null)
					set(e, "tailport", tailport);
				final String headport = compass(edge.getHeadPort());
				if (headport != null)
					set(e, "headport", headport);
			}

			// Shapes unknown to Smetana: route up to the center, PlantUML clips
			if (input.getContour(edge.getTail()) != null)
				set(e, "tailclip", "false");
			if (input.getContour(edge.getHead()) != null)
				set(e, "headclip", "false");
		}
	}

	/**
	 * The compass point of a port ({@code "ne"}, {@code "p1:ne"}...), or
	 * {@code null}. Port names (records, HTML labels) are not supported yet.
	 * As BT and RL are computed as TB and LR then mirrored, their compass points
	 * are mirrored first.
	 */
	private String compass(String port) {
		if (port == null)
			return null;
		String s = port.trim().toLowerCase();
		final int colon = s.lastIndexOf(':');
		if (colon != -1)
			s = s.substring(colon + 1);
		if (COMPASS_POINTS.contains(s) == false)
			return null;
		if (rankdir == Rankdir.BT)
			s = s.replace('n', 'x').replace('s', 'n').replace('x', 's');
		else if (rankdir == Rankdir.RL)
			s = s.replace('e', 'x').replace('w', 'e').replace('x', 'w');
		return s;
	}

	// ------------------------------------------------------------------
	// Reading the result back

	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	private XPoint2D transform(double x, double y) {
		// Smetana has Y going up. TB is drawn mirrored, BT as is; RL is LR mirrored.
		double tx = x - minX;
		double ty = maxY - y;
		if (rankdir == Rankdir.BT)
			ty = y - minY;
		if (rankdir == Rankdir.RL)
			tx = maxX - x;
		return new XPoint2D(tx, ty);
	}

	private XPoint2D transform(ST_pointf pt) {
		return transform(pt.x, pt.y);
	}

	private LayoutResult extract() {
		final ST_Agraphinfo_t info = (ST_Agraphinfo_t) root.data;
		minX = info.bb.LL.x;
		minY = info.bb.LL.y;
		maxX = info.bb.UR.x;
		maxY = info.bb.UR.y;

		final LayoutResult result = new LayoutResult(new XDimension2D(maxX - minX, maxY - minY));

		for (Map.Entry<DotNode, ST_Agnode_s> ent : nodes.entrySet()) {
			final ST_Agnodeinfo_t data = (ST_Agnodeinfo_t) ent.getValue().data;
			final XPoint2D center = transform(data.coord.x, data.coord.y);
			result.put(ent.getKey(), new NodeBox(center, new XDimension2D(data.width * 72, data.height * 72)));
		}

		for (Map.Entry<DotEdge, ST_Agedge_s> ent : edges.entrySet()) {
			final EdgeRoute route = extractEdge(ent.getKey(), (ST_Agedgeinfo_t) ent.getValue().data, result);
			if (route != null)
				result.put(ent.getKey(), route);
		}

		for (Map.Entry<DotSubgraph, ST_Agraph_s> ent : clusters.entrySet()) {
			final ST_Agraphinfo_t data = (ST_Agraphinfo_t) ent.getValue().data;
			final XPoint2D a = transform(data.bb.LL.x, data.bb.LL.y);
			final XPoint2D b = transform(data.bb.UR.x, data.bb.UR.y);
			final XPoint2D topLeft = new XPoint2D(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
			final XDimension2D size = new XDimension2D(Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()));
			XPoint2D labelCenter = null;
			final ST_textlabel_t label = data.label;
			if (label != null)
				labelCenter = transform(label.pos);
			result.put(ent.getKey(), new ClusterBox(topLeft, size, labelCenter));
		}
		return result;
	}

	private EdgeRoute extractEdge(DotEdge edge, ST_Agedgeinfo_t data, LayoutResult result) {
		if (data.spl == null || data.spl.size == 0)
			return null;

		final ST_bezier bezier = data.spl.list.get__(0);
		if (bezier.size < 4)
			return null;

		List<XPoint2D> points = new ArrayList<XPoint2D>();
		for (int i = 0; i < bezier.size; i++)
			points.add(transform(bezier.list.get__(i)));

		final NodeContour tailContour = input.getContour(edge.getTail());
		if (tailContour != null)
			points = BezierClip.clipStartToContour(points, tailContour,
					result.getNodes().get(edge.getTail()).getCenter());
		final NodeContour headContour = input.getContour(edge.getHead());
		if (headContour != null)
			points = BezierClip.clipEndToContour(points, headContour,
					result.getNodes().get(edge.getHead()).getCenter());

		XPoint2D tailTip = null;
		XPoint2D headTip = null;
		final Double tailArrow = input.getTailArrow(edge);
		if (tailArrow != null) {
			tailTip = points.get(0);
			points = BezierClip.clipStart(points, tailArrow);
		}
		final Double headArrow = input.getHeadArrow(edge);
		if (headArrow != null) {
			headTip = points.get(points.size() - 1);
			points = BezierClip.clipEnd(points, headArrow);
		}

		XPoint2D labelCenter = null;
		if (data.label != null && input.getEdgeLabel(edge) != null)
			labelCenter = transform(data.label.pos);

		return new EdgeRoute(points, tailTip, headTip, labelCenter);
	}

}
