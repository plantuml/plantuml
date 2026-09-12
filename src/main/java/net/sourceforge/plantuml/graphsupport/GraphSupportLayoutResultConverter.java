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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.graphper.api.Cluster;
import org.graphper.api.Line;
import org.graphper.api.Node;
import org.graphper.api.ext.Box;
import org.graphper.api.ext.RegularPolylinePropCalc;
import org.graphper.api.ext.ShapePropCalc;
import org.graphper.def.FlatPoint;
import org.graphper.draw.ClusterDrawProp;
import org.graphper.draw.DrawGraph;
import org.graphper.draw.LineDrawProp;
import org.graphper.draw.NodeDrawProp;

import net.sourceforge.plantuml.graphsupport.GraphSupportSvekLayoutBuilder.BuildResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Shape;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Bounds;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.ClusterGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.EdgeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.NodeGeometry;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Path;
import net.sourceforge.plantuml.svek.layout.SvekLayoutResult.Point;

/**
 * Reads geometry from a laid out Graphper graph and rebuilds it as the neutral Svek result.
 *
 * The builder keeps the mapping between Svek identifiers and Graphper objects, so this class only
 * has to look each object up and translate its coordinates.
 */
final class GraphSupportLayoutResultConverter {

	private final DrawGraph drawGraph;
	private final BuildResult build;

	/**
	 * Graphper leaves the graph wherever the algorithm puts it, which for a left to right layout is
	 * above the origin, while Svek draws from the origin down. Every coordinate read here is
	 * therefore expressed relative to the graph's own top left corner.
	 */
	private final double originX;
	private final double originY;

	private GraphSupportLayoutResultConverter(DrawGraph drawGraph, BuildResult build) {
		this.drawGraph = drawGraph;
		this.build = build;
		this.originX = finite(drawGraph.getLeftBorder());
		this.originY = finite(drawGraph.getUpBorder());
	}

	static SvekLayoutResult convert(DrawGraph drawGraph, BuildResult build) {
		return new GraphSupportLayoutResultConverter(drawGraph, build).convert();
	}

	private SvekLayoutResult convert() {
		return new SvekLayoutResult(bounds(drawGraph), build.direction, nodes(), clusters(), edges());
	}

	// Nodes

	private Map<String, NodeGeometry> nodes() {
		final Map<String, NodeGeometry> result = new LinkedHashMap<>();
		for (Map.Entry<Node, String> entry : build.nodeIds.entrySet()) {
			final String id = entry.getValue();
			final NodeSpec spec = build.nodeSpecs.get(id);
			// Scaffolding nodes only shape the layout and have no Svek counterpart.
			if (spec.layoutOnly == false)
				result.put(id, node(id, spec, entry.getKey()));
		}
		return result;
	}

	private NodeGeometry node(String id, NodeSpec spec, Node node) {
		final NodeDrawProp prop = drawGraph.getNodeDrawProp(geometryOwner(spec, node));
		return new NodeGeometry(id, bounds(prop), corners(spec, prop));
	}

	/**
	 * Cell-backed nodes carry their geometry on the body cell, but polygons keep it on the node
	 * itself because the corner calculator runs on the outer shape.
	 */
	private Node geometryOwner(NodeSpec spec, Node node) {
		final Node bodyCell = build.bodyCells.get(spec.id);
		return bodyCell == null || isPolygon(spec) ? node : bodyCell;
	}

	private static boolean isPolygon(NodeSpec spec) {
		return spec != null && (spec.shape == Shape.OCTAGON || spec.shape == Shape.HEXAGON);
	}

	private List<Point> corners(NodeSpec spec, NodeDrawProp prop) {
		if (isPolygon(spec) == false)
			return null;

		final ShapePropCalc calc = prop.nodeAttrs().getShape().getShapePropCalc();
		if (calc instanceof RegularPolylinePropCalc == false)
			throw new IllegalStateException("Graph-support polygon shape has no regular polygon calculator");

		final List<Point> corners = new ArrayList<>();
		for (FlatPoint corner : ((RegularPolylinePropCalc) calc).calcPoints(prop))
			corners.add(point(corner));
		return corners;
	}

	// Clusters

	private Map<String, ClusterGeometry> clusters() {
		final Map<String, ClusterGeometry> result = new LinkedHashMap<>();
		for (Map.Entry<Cluster, String> entry : build.clusterIds.entrySet()) {
			final String id = entry.getValue();
			final ClusterSpec spec = build.clusterSpecs.get(id);
			if (spec.layoutOnly == false)
				result.put(id, cluster(id, spec, entry.getKey()));
		}
		return result;
	}

	private ClusterGeometry cluster(String id, ClusterSpec spec, Cluster cluster) {
		final ClusterDrawProp prop = drawGraph.getClusterDrawProp(cluster);
		return new ClusterGeometry(id, bounds(prop), titleTopLeft(prop.getLabelCenter(), spec));
	}

	/** Graphper centers a cluster title inside the reserved header band. */
	private Point titleTopLeft(FlatPoint center, ClusterSpec spec) {
		if (center == null || spec == null || spec.titleWidth <= 0 || spec.titleHeight <= 0)
			return null;
		return point(center.getX() - spec.titleWidth / 2.0,
				center.getY() - (spec.titleHeight + spec.contentTopPadding) / 2.0);
	}

	// Edges

	private Map<String, EdgeGeometry> edges() {
		final Map<String, EdgeGeometry> result = new LinkedHashMap<>();
		for (Map.Entry<Line, String> entry : build.lineIds.entrySet())
			result.put(entry.getValue(), edge(entry.getValue(), entry.getKey()));
		return result;
	}

	private EdgeGeometry edge(String id, Line line) {
		final LineDrawProp prop = drawGraph.getLineDrawProp(line);
		return new EdgeGeometry(id, build.nodeIds.get(line.tail()), build.nodeIds.get(line.head()), path(prop),
				mainLabelTopLeft(prop, build.labelSpecs.get(line)), cellTopLeft(line, LabelPosition.TAIL),
				cellTopLeft(line, LabelPosition.HEAD));
	}

	private Path path(LineDrawProp prop) {
		if (prop == null || prop.isEmpty())
			return null;

		final boolean cubic = prop.isBesselCurve();
		if (cubic && (prop.size() < 4 || (prop.size() - 1) % 3 != 0))
			throw new IllegalStateException("Malformed cubic path with " + prop.size() + " points");

		final List<Point> points = new ArrayList<>(prop.size());
		for (FlatPoint flatPoint : prop) {
			if (flatPoint == null)
				throw new IllegalStateException("Path contains a null point");
			points.add(point(flatPoint));
		}
		// Graphper may emit the path head first; Svek always expects tail to head.
		if (prop.isHeadStart())
			Collections.reverse(points);
		return new Path(cubic, points);
	}

	/** A main edge label is laid out as a table, so only its center is known. */
	private Point mainLabelTopLeft(LineDrawProp prop, Map<LabelPosition, LabelSpec> specs) {
		if (prop == null || specs == null)
			return null;

		final LabelSpec spec = specs.get(LabelPosition.MAIN);
		final FlatPoint center = prop.getLabelCenter();
		if (spec == null || center == null)
			return null;
		return point(center.getX() - spec.width / 2.0, center.getY() - spec.height / 2.0);
	}

	/** Endpoint labels are laid out as real cells, so their box is read directly. */
	private Point cellTopLeft(Line line, LabelPosition position) {
		final Map<LabelPosition, Node> cells = build.labelCells.get(line);
		if (cells == null || cells.get(position) == null)
			return null;

		final NodeDrawProp prop = drawGraph.getNodeDrawProp(cells.get(position));
		return prop == null ? null : point(prop.getLeftBorder(), prop.getUpBorder());
	}

	// Coordinates

	private Bounds bounds(Box box) {
		return new Bounds(x(box.getLeftBorder()), y(box.getUpBorder()), x(box.getRightBorder()),
				y(box.getDownBorder()));
	}

	private Point point(FlatPoint point) {
		return point == null ? null : point(point.getX(), point.getY());
	}

	private Point point(double x, double y) {
		return new Point(x(x), y(y));
	}

	private double x(double value) {
		return finite(value) - originX;
	}

	private double y(double value) {
		return finite(value) - originY;
	}

	/** Svek cannot place a shape from a non-finite coordinate, so fail instead of drawing garbage. */
	private static double finite(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value))
			throw new IllegalStateException("Graph-support returned a non-finite coordinate");
		return value;
	}
}
