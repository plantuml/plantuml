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
package net.sourceforge.plantuml.ngdot.draw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.ngdot.attr.DotColors;
import net.sourceforge.plantuml.ngdot.attr.DotNumbers;
import net.sourceforge.plantuml.ngdot.attr.DotStyle;
import net.sourceforge.plantuml.ngdot.layout.LayoutInput;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult.ClusterBox;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult.EdgeRoute;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult.NodeBox;
import net.sourceforge.plantuml.ngdot.layout.LayoutResult;
import net.sourceforge.plantuml.ngdot.layout.NodeContour;
import net.sourceforge.plantuml.ngdot.layout.SmetanaLayout;
import net.sourceforge.plantuml.ngdot.model.DotAttributes;
import net.sourceforge.plantuml.ngdot.model.DotEdge;
import net.sourceforge.plantuml.ngdot.model.DotGraph;
import net.sourceforge.plantuml.ngdot.model.DotNode;
import net.sourceforge.plantuml.ngdot.model.DotSubgraph;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;
import net.sourceforge.plantuml.style.StyleQuery;

/**
 * Draws a {@link DotGraph}: the size of every node and label is measured with
 * the {@link StringBounder}, the graph is laid out by Smetana
 * ({@link SmetanaLayout}), then everything is drawn with UGraphic.
 * <p>
 * The look comes from the PlantUML style ({@code dotDiagram} in the style
 * sheet); the DOT attributes that are supported override it.
 */
public final class DotRenderer implements TextBlock {

	/** Graphviz "pad": the space around the drawing. */
	private static final double PAD = 4;

	/** Graphviz PORT_LABEL_DISTANCE and PORT_LABEL_ANGLE, for head and tail labels. */
	private static final double PORT_LABEL_DISTANCE = 10;
	private static final double PORT_LABEL_ANGLE = -25;

	/** Graphviz default fill of a filled cluster without any color. */
	private static final HColor LIGHTGREY = HColors.simple(new XColor(0xD3, 0xD3, 0xD3));

	private final DotGraph graph;
	private final ISkinParam skinParam;
	private final Style nodeStyle;
	private final Style edgeStyle;
	private final Style clusterStyle;

	private final Map<DotNode, NodeLook> nodeLooks = new HashMap<DotNode, NodeLook>();
	private final Map<DotEdge, EdgeLook> edgeLooks = new HashMap<DotEdge, EdgeLook>();
	private final Map<DotSubgraph, ClusterLook> clusterLooks = new HashMap<DotSubgraph, ClusterLook>();
	private DotLabel graphLabel;

	private LayoutResult layout;
	private Frame frame;

	public DotRenderer(DotGraph graph, ISkinParam skinParam) {
		this.graph = graph;
		this.skinParam = skinParam;
		this.nodeStyle = style(SName.node);
		this.edgeStyle = style(SName.arrow);
		this.clusterStyle = style(SName.group);
	}

	private Style style(SName name) {
		final StyleQuery query = StyleQueries.ELEMENT.add(SName.dotDiagram, name);
		return skinParam.getCurrentStyleBuilder().getMergedStyle(query);
	}

	private HColorSet colorSet() {
		return skinParam.getIHtmlColorSet();
	}

	private HColor color(DotAttributes attributes, String name) {
		return DotColors.parse(attributes.getString(name), colorSet());
	}

	private static HColor firstNotNull(HColor... colors) {
		for (HColor c : colors)
			if (c != null)
				return c;
		return null;
	}

	private FontConfiguration font(Style style, DotAttributes attributes, String sizeName, String colorName) {
		FontConfiguration result = style.getFontConfiguration(colorSet());
		Double fontsize = DotNumbers.parseFirst(attributes.getString(sizeName));
		if (fontsize == null && sizeName.equals("fontsize") == false)
			fontsize = DotNumbers.parseFirst(attributes.getString("fontsize"));
		if (fontsize != null && fontsize > 0)
			result = result.changeSize(fontsize.floatValue());
		HColor fontcolor = color(attributes, colorName);
		if (fontcolor == null && colorName.equals("fontcolor") == false)
			fontcolor = color(attributes, "fontcolor");
		if (fontcolor != null)
			result = result.changeColor(fontcolor);
		return result;
	}

	private DotLabel label(String raw, FontConfiguration font) {
		if (raw == null || raw.length() == 0)
			return null;
		final DotLabel result = DotLabel.create(raw, font, skinParam);
		return result.isEmpty() ? null : result;
	}

	private String graphName() {
		return graph.getId() == null ? "" : graph.getId();
	}

	// ------------------------------------------------------------------
	// What is drawn for each element, once its attributes are resolved

	static final class NodeLook {
		NodeShape shape;
		DotLabel label;
		int peripheries;
		HColor line;
		HColor fill;
		UStroke stroke;
		DotStyle style;
	}

	static final class EdgeLook {
		HColor line;
		HColor fill;
		UStroke stroke;
		DotStyle style;
		double arrowsize;
		ArrowShape headArrow;
		ArrowShape tailArrow;
		DotLabel label;
		DotLabel headLabel;
		DotLabel tailLabel;
	}

	static final class ClusterLook {
		HColor line;
		HColor fill;
		UStroke stroke;
		DotStyle style;
		DotLabel label;
		char labeljust;
	}

	// ------------------------------------------------------------------
	// Layout

	private LayoutResult getLayout(StringBounder stringBounder) {
		if (layout == null)
			layout = SmetanaLayout.layout(graph, prepare(stringBounder));
		return layout;
	}

	private LayoutInput prepare(StringBounder stringBounder) {
		final LayoutInput input = new LayoutInput();
		for (DotNode node : graph.getNodes())
			prepareNode(input, node, stringBounder);
		for (DotEdge edge : graph.getEdges())
			prepareEdge(input, edge, stringBounder);
		for (DotSubgraph subgraph : graph.getAllSubgraphs())
			if (subgraph.isCluster())
				prepareCluster(input, subgraph, stringBounder);

		final DotAttributes attributes = graph.getAttributes();
		graphLabel = label(DotLabel.substitute(attributes.getString("label"), null, graphName(), null, null, null),
				font(clusterStyle, attributes, "fontsize", "fontcolor"));
		return input;
	}

	private void prepareNode(LayoutInput input, DotNode node, StringBounder stringBounder) {
		final DotAttributes attributes = node.getAttributes();
		final NodeLook look = new NodeLook();
		final String shapeName = attributes.getString("shape");
		look.shape = NodeShape.fromName(shapeName);
		if (look.shape == null)
			look.shape = NodeShape.ELLIPSE;
		look.style = DotStyle.of(attributes);

		XDimension2D labelDim = new XDimension2D(0, 0);
		if (look.shape.hasLabel()) {
			final String text = DotLabel.substitute(attributes.getString("label", "\\N"), node.getId(), graphName(),
					null, null, null);
			look.label = label(text, font(nodeStyle, attributes, "fontsize", "fontcolor"));
			if (look.label != null)
				labelDim = look.label.calculateDimension(stringBounder);
		}

		final Double peripheries = DotNumbers.parseFirst(attributes.getString("peripheries"));
		look.peripheries = peripheries == null ? look.shape.defaultPeripheries()
				: Math.max(0, (int) Math.round(peripheries));

		// Colors: the theme, overridden by "color" (outline) and, when filled,
		// by "fillcolor" (or "color" if there is no fillcolor)
		final HColor color = color(attributes, "color");
		look.line = firstNotNull(color, nodeStyle.value(PName.LineColor).asColor(colorSet()));
		final HColor themeFill = nodeStyle.value(PName.BackGroundColor).asColor(colorSet());
		if (look.shape == NodeShape.POINT)
			look.fill = firstNotNull(color(attributes, "fillcolor"), color, look.line);
		else if (look.style.isFilled())
			look.fill = firstNotNull(color(attributes, "fillcolor"), color, themeFill);
		else if (look.shape == NodeShape.PLAINTEXT)
			look.fill = null;
		else
			look.fill = themeFill;
		look.stroke = look.style.getStroke(nodeStyle.getStroke());
		nodeLooks.put(node, look);

		final boolean square = "square".equalsIgnoreCase(shapeName);
		final XDimension2D inner = look.shape.computeSize(labelDim, attributes, square);
		final XDimension2D outer = NodeShape.withPeripheries(inner, look.peripheries);
		input.setNode(node, outer, look.shape.getOutline());
		if (look.shape.hasOwnContour()) {
			final NodeShape shape = look.shape;
			input.setContour(node, new NodeContour() {
				public boolean contains(double dx, double dy) {
					return shape.contains(dx, dy, outer);
				}
			});
		}
	}

	private void prepareEdge(LayoutInput input, DotEdge edge, StringBounder stringBounder) {
		final DotAttributes attributes = edge.getAttributes();
		final EdgeLook look = new EdgeLook();
		look.style = DotStyle.of(attributes);
		final HColor color = color(attributes, "color");
		look.line = firstNotNull(color, edgeStyle.value(PName.LineColor).asColor(colorSet()));
		look.fill = firstNotNull(color(attributes, "fillcolor"), look.line);
		look.stroke = look.style.getStroke(edgeStyle.getStroke());
		final double arrowsize = DotNumbers.parse(attributes.getString("arrowsize"), 1);
		look.arrowsize = arrowsize > 0 ? arrowsize : 1;

		final String dir = dir(edge);
		if (dir.equals("forward") || dir.equals("both"))
			look.headArrow = arrow(attributes.getString("arrowhead"));
		if (dir.equals("back") || dir.equals("both"))
			look.tailArrow = arrow(attributes.getString("arrowtail"));

		final String tail = edge.getTail().getId();
		final String head = edge.getHead().getId();
		final FontConfiguration labelFont = font(edgeStyle, attributes, "fontsize", "fontcolor");
		look.label = label(DotLabel.substitute(attributes.getString("label"), null, graphName(), tail, head,
				edgeName(edge)), labelFont);
		final FontConfiguration portFont = font(edgeStyle, attributes, "labelfontsize", "labelfontcolor");
		look.headLabel = label(DotLabel.substitute(attributes.getString("headlabel"), null, graphName(), tail, head,
				edgeName(edge)), portFont);
		look.tailLabel = label(DotLabel.substitute(attributes.getString("taillabel"), null, graphName(), tail, head,
				edgeName(edge)), portFont);
		edgeLooks.put(edge, look);

		if (look.label != null)
			input.setEdgeLabel(edge, look.label.calculateDimension(stringBounder));
		if (look.headArrow != null)
			input.setHeadArrow(edge, look.headArrow.getLength(look.arrowsize));
		if (look.tailArrow != null)
			input.setTailArrow(edge, look.tailArrow.getLength(look.arrowsize));
	}

	private static ArrowShape arrow(String name) {
		final ArrowShape result = ArrowShape.parse(name);
		return result.isNone() ? null : result;
	}

	private void prepareCluster(LayoutInput input, DotSubgraph cluster, StringBounder stringBounder) {
		final DotAttributes attributes = cluster.getAttributes();
		final ClusterLook look = new ClusterLook();
		look.style = DotStyle.of(attributes);
		final HColor color = color(attributes, "color");
		look.line = firstNotNull(color(attributes, "pencolor"), color,
				clusterStyle.value(PName.LineColor).asColor(colorSet()));
		final HColor bgcolor = color(attributes, "bgcolor");
		if (look.style.isFilled())
			look.fill = firstNotNull(color(attributes, "fillcolor"), color, bgcolor, LIGHTGREY);
		else
			look.fill = firstNotNull(bgcolor, clusterStyle.value(PName.BackGroundColor).asColor(colorSet()));
		look.stroke = look.style.getStroke(clusterStyle.getStroke());
		look.labeljust = firstChar(attributes.getString("labeljust"), 'c');
		look.label = label(DotLabel.substitute(attributes.getString("label"), null, graphName(), null, null, null),
				font(clusterStyle, attributes, "fontsize", "fontcolor"));
		clusterLooks.put(cluster, look);
		if (look.label != null)
			input.setClusterLabel(cluster, look.label.calculateDimension(stringBounder));
	}

	private static char firstChar(String value, char defaultValue) {
		if (value == null || value.trim().length() == 0)
			return defaultValue;
		return Character.toLowerCase(value.trim().charAt(0));
	}

	private String edgeName(DotEdge edge) {
		return edge.getTail().getId() + (graph.isDirected() ? "->" : "--") + edge.getHead().getId();
	}

	private String dir(DotEdge edge) {
		final String result = edge.getAttributes().getString("dir");
		if (result == null)
			return graph.isDirected() ? "forward" : "none";
		return result.trim().toLowerCase();
	}

	// ------------------------------------------------------------------
	// The frame: what is outside the layout box (head and tail labels, graph
	// label) and where the layout box goes

	static final class Frame {
		double offsetX;
		double offsetY;
		XDimension2D dimension;
		XPoint2D graphLabelTopLeft;
		final Map<DotEdge, XPoint2D> headLabels = new HashMap<DotEdge, XPoint2D>();
		final Map<DotEdge, XPoint2D> tailLabels = new HashMap<DotEdge, XPoint2D>();
	}

	private Frame getFrame(StringBounder stringBounder) {
		if (frame != null)
			return frame;
		final LayoutResult result = getLayout(stringBounder);
		final Frame f = new Frame();
		double minX = 0;
		double minY = 0;
		double maxX = result.getDimension().getWidth();
		double maxY = result.getDimension().getHeight();

		for (Map.Entry<DotEdge, EdgeRoute> ent : result.getEdges().entrySet()) {
			final EdgeLook look = edgeLooks.get(ent.getKey());
			final DotAttributes attributes = ent.getKey().getAttributes();
			for (int side = 0; side < 2; side++) {
				final boolean head = side == 0;
				final DotLabel label = head ? look.headLabel : look.tailLabel;
				if (label == null)
					continue;
				final XPoint2D center = portLabelCenter(ent.getValue(), head, attributes);
				final XDimension2D dim = label.calculateDimension(stringBounder);
				final XPoint2D topLeft = new XPoint2D(center.getX() - dim.getWidth() / 2,
						center.getY() - dim.getHeight() / 2);
				(head ? f.headLabels : f.tailLabels).put(ent.getKey(), topLeft);
				minX = Math.min(minX, topLeft.getX());
				minY = Math.min(minY, topLeft.getY());
				maxX = Math.max(maxX, topLeft.getX() + dim.getWidth());
				maxY = Math.max(maxY, topLeft.getY() + dim.getHeight());
			}
		}

		if (graphLabel != null) {
			final XDimension2D dim = graphLabel.calculateDimension(stringBounder);
			final DotAttributes attributes = graph.getAttributes();
			final boolean top = firstChar(attributes.getString("labelloc"), 'b') == 't';
			final char just = firstChar(attributes.getString("labeljust"), 'c');
			final double extra = dim.getWidth() - (maxX - minX);
			if (extra > 0) {
				minX -= extra / 2;
				maxX += extra / 2;
			}
			final double x;
			if (just == 'l')
				x = minX;
			else if (just == 'r')
				x = maxX - dim.getWidth();
			else
				x = (minX + maxX - dim.getWidth()) / 2;
			final double y;
			if (top) {
				minY -= dim.getHeight();
				y = minY;
			} else {
				y = maxY;
				maxY += dim.getHeight();
			}
			f.graphLabelTopLeft = new XPoint2D(x, y);
		}

		f.offsetX = PAD - minX;
		f.offsetY = PAD - minY;
		f.dimension = new XDimension2D(maxX - minX + 2 * PAD, maxY - minY + 2 * PAD);
		frame = f;
		return f;
	}

	/**
	 * Where Graphviz puts a head or tail label ({@code place_portlabel}): at
	 * {@code labeldistance * 10} from the end of the edge, turned by
	 * {@code labelangle} (-25 degrees by default) from the edge direction.
	 */
	private static XPoint2D portLabelCenter(EdgeRoute route, boolean head, DotAttributes attributes) {
		final List<XPoint2D> points = route.getPoints();
		final XPoint2D pe;
		final XPoint2D pf;
		if (head) {
			if (route.getHeadTip() != null) {
				pe = route.getHeadTip();
				pf = points.get(points.size() - 1);
			} else {
				final int n = points.size();
				pe = points.get(n - 1);
				pf = bezier(points.get(n - 4), points.get(n - 3), points.get(n - 2), pe, 0.9);
			}
		} else {
			if (route.getTailTip() != null) {
				pe = route.getTailTip();
				pf = points.get(0);
			} else {
				pe = points.get(0);
				pf = bezier(pe, points.get(1), points.get(2), points.get(3), 0.1);
			}
		}
		// Graphviz works with Y going up
		final double angle = Math.atan2(-(pf.getY() - pe.getY()), pf.getX() - pe.getX())
				+ Math.toRadians(DotNumbers.parse(attributes.getString("labelangle"), PORT_LABEL_ANGLE));
		final double dist = PORT_LABEL_DISTANCE * Math.max(0, DotNumbers.parse(attributes.getString("labeldistance"), 1));
		return new XPoint2D(pe.getX() + dist * Math.cos(angle), pe.getY() - dist * Math.sin(angle));
	}

	private static XPoint2D bezier(XPoint2D p0, XPoint2D p1, XPoint2D p2, XPoint2D p3, double t) {
		final double u = 1 - t;
		final double a = u * u * u;
		final double b = 3 * u * u * t;
		final double c = 3 * u * t * t;
		final double d = t * t * t;
		return new XPoint2D(a * p0.getX() + b * p1.getX() + c * p2.getX() + d * p3.getX(),
				a * p0.getY() + b * p1.getY() + c * p2.getY() + d * p3.getY());
	}

	// ------------------------------------------------------------------
	// TextBlock

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getFrame(stringBounder).dimension;
	}

	@Override
	public void drawU(UGraphic ug) {
		final Frame f = getFrame(ug.getStringBounder());
		final LayoutResult result = getLayout(ug.getStringBounder());

		final HColor bgcolor = color(graph.getAttributes(), "bgcolor");
		if (bgcolor != null)
			ug.apply(HColors.none()).apply(bgcolor.bg()).draw(URectangle.build(f.dimension));

		if (graphLabel != null)
			graphLabel.drawU(ug.apply(new UTranslate(f.graphLabelTopLeft.getX() + f.offsetX,
					f.graphLabelTopLeft.getY() + f.offsetY)));

		ug = ug.apply(new UTranslate(f.offsetX, f.offsetY));

		for (Map.Entry<DotSubgraph, ClusterBox> ent : result.getClusters().entrySet())
			drawCluster(ug, clusterLooks.get(ent.getKey()), ent.getValue());

		for (Map.Entry<DotEdge, EdgeRoute> ent : result.getEdges().entrySet())
			drawEdge(ug, edgeLooks.get(ent.getKey()), ent.getValue());

		for (Map.Entry<DotNode, NodeBox> ent : result.getNodes().entrySet())
			drawNode(ug, nodeLooks.get(ent.getKey()), ent.getValue());

		for (Map.Entry<DotEdge, EdgeRoute> ent : result.getEdges().entrySet())
			drawEdgeLabels(ug, f, ent.getKey(), ent.getValue());
	}

	private void drawCluster(UGraphic ug, ClusterLook look, ClusterBox box) {
		if (look == null || look.style.isInvisible())
			return;
		final UGraphic ugBox = ug.apply(look.line).apply(look.fill.bg()).apply(look.stroke)
				.apply(new UTranslate(box.getTopLeft().getX(), box.getTopLeft().getY()));
		if (look.style.isRounded())
			ugBox.draw(URectangle.build(box.getSize()).rounded(24));
		else
			ugBox.draw(URectangle.build(box.getSize()));

		if (look.label == null || box.getLabelCenter() == null)
			return;
		final XDimension2D dim = look.label.calculateDimension(ug.getStringBounder());
		// Smetana centers the label (at the top or at the bottom, see labelloc in
		// SmetanaLayout); labeljust moves it
		double x = box.getLabelCenter().getX() - dim.getWidth() / 2;
		final double y = box.getLabelCenter().getY() - dim.getHeight() / 2;
		final double margin = NodeShape.GAP * 2;
		if (look.labeljust == 'l')
			x = box.getTopLeft().getX() + margin;
		else if (look.labeljust == 'r')
			x = box.getTopLeft().getX() + box.getSize().getWidth() - margin - dim.getWidth();
		look.label.drawU(ug.apply(new UTranslate(x, y)));
	}

	private void drawNode(UGraphic ug, NodeLook look, NodeBox box) {
		if (look.style.isInvisible())
			return;
		final XPoint2D topLeft = box.getTopLeft();
		look.shape.draw(ug.apply(look.line).apply(look.stroke).apply(new UTranslate(topLeft.getX(), topLeft.getY())),
				look.fill, box.getSize(), look.peripheries, look.style.isRounded(), look.style.isDiagonals());
		if (look.label != null)
			drawCentered(ug, look.label, box.getCenter());
	}

	private void drawEdge(UGraphic ug, EdgeLook look, EdgeRoute route) {
		if (look.style.isInvisible())
			return;
		final UGraphic ugLine = ug.apply(look.line).apply(HColors.none().bg()).apply(look.stroke);

		final List<XPoint2D> points = route.getPoints();
		final UPath path = UPath.none();
		path.moveTo(points.get(0));
		for (int i = 1; i + 2 < points.size(); i += 3)
			path.cubicTo(points.get(i), points.get(i + 1), points.get(i + 2));
		ugLine.draw(path);

		// Graphviz draws the arrows with a solid pen
		final UGraphic ugArrow = ug.apply(look.line).apply(UStroke.withThickness(look.stroke.getThickness()));
		if (look.headArrow != null && route.getHeadTip() != null)
			look.headArrow.draw(ugArrow, look.fill, route.getHeadTip(), points.get(points.size() - 1),
					look.arrowsize);
		if (look.tailArrow != null && route.getTailTip() != null)
			look.tailArrow.draw(ugArrow, look.fill, route.getTailTip(), points.get(0), look.arrowsize);
	}

	private void drawEdgeLabels(UGraphic ug, Frame f, DotEdge edge, EdgeRoute route) {
		final EdgeLook look = edgeLooks.get(edge);
		if (look.style.isInvisible())
			return;
		if (look.label != null && route.getLabelCenter() != null)
			drawCentered(ug, look.label, route.getLabelCenter());
		final XPoint2D head = f.headLabels.get(edge);
		if (head != null)
			look.headLabel.drawU(ug.apply(new UTranslate(head.getX(), head.getY())));
		final XPoint2D tail = f.tailLabels.get(edge);
		if (tail != null)
			look.tailLabel.drawU(ug.apply(new UTranslate(tail.getX(), tail.getY())));
	}

	private void drawCentered(UGraphic ug, TextBlock block, XPoint2D center) {
		final XDimension2D dim = block.calculateDimension(ug.getStringBounder());
		block.drawU(ug.apply(new UTranslate(center.getX() - dim.getWidth() / 2, center.getY() - dim.getHeight() / 2)));
	}

}
