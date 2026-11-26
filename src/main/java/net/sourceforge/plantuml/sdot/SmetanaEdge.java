/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.sdot;


import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_bezier;
import h.ST_pointf;
import h.ST_splines;
import h.ST_textlabel_t;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkStrategy;
import net.sourceforge.plantuml.cruise.XAbstractEdge;
import net.sourceforge.plantuml.cruise.XEdge;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;
import net.sourceforge.plantuml.style.StyleQuery;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.RoleLabels;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.svek.extremity.Extremity;
import net.sourceforge.plantuml.svek.extremity.ExtremityArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.Direction;

public class SmetanaEdge extends XAbstractEdge implements XEdge, UDrawable {

	private final ST_Agedge_s edge;
	private final YMirror ymirror;
	private final TextBlock label;
	private final EndpointLabel headLabel;
	private final EndpointLabel tailLabel;
	private final boolean orthogonal;
	private DotPath orthogonalPath;
	// Set once the routing budget of the diagram is spent. Looking for a free
	// place for the label of a spline costs more than the search it replaces.
	private boolean keepsLayoutLabel;
	private UTranslate labelPosition;
	private UTranslate headLabelPosition;
	private UTranslate tailLabelPosition;

	public SmetanaEdge(Link link, ST_Agedge_s edge, YMirror ymirror, TextBlock label,
			TextBlock tailQuantifier, TextBlock headQuantifier, TextBlock tailRole, TextBlock headRole,
			Bibliotekon bibliotekon, ISkinParam skinParam, boolean orthogonal) {
		super(link, skinParam, bibliotekon);
		this.edge = edge;
		this.ymirror = ymirror;
		this.label = label;
		this.orthogonal = orthogonal;
		this.tailLabel = new EndpointLabel(tailQuantifier, tailRole, orthogonal);
		this.headLabel = new EndpointLabel(headQuantifier, headRole, orthogonal);
	}

	private LinkStrategy getLinkStrategy() {
		return link.getLinkStrategy();
	}

	public void drawU(UGraphic ug) {

		if (link.isHidden())
			return;

		final UGroup group = new UGroup();
		group.put(UGroupType.CLASS, "link");
		group.put(UGroupType.ID, "link_" + link.getEntity1().getName() + "_" + link.getEntity2().getName());
		group.put(UGroupType.DATA_UID, link.getUid());
		group.put(UGroupType.DATA_ENTITY_1, link.getEntity1().getName());
		group.put(UGroupType.DATA_ENTITY_2, link.getEntity2().getName());
		group.put(UGroupType.DATA_ENTITY_1_UID, link.getEntity1().getUid());
		group.put(UGroupType.DATA_ENTITY_2_UID, link.getEntity2().getUid());
		final String linkTypeName = link.getType().getLinkTypeName();
		if (linkTypeName != null)
			group.put(UGroupType.DATA_LINK_TYPE, linkTypeName);
		ug.startGroup(group);

		final DotPath dotPath = orthogonalPath == null ? getDisplayedPath(ug.getStringBounder()) : orthogonalPath.copy();
		if (dotPath != null) {
			final Style styleLine = getStyle();

			final Rainbow rainbow = Rainbow.build(styleLine, skinParam.getIHtmlColorSet());
			HColor arrowHeadColor = rainbow.getArrowHeadColor();
			HColor color = rainbow.getColor();

			// A color set on the link itself applies to its decorations as well.
			if (this.link.getColors() != null) {
				final HColor newColor = this.link.getColors().getColor(ColorType.ARROW, ColorType.LINE);
				if (newColor != null) {
					color = newColor;
					arrowHeadColor = color;
				}
			} else if (this.link.getSpecificColor() != null) {
				color = this.link.getSpecificColor();
				arrowHeadColor = color;
			}

			ug = ug.apply(HColors.none().bg()).apply(color);
			final UStroke stroke = getLineStroke(styleLine);

			final Url url = link.getUrl();
			if (url != null)
				ug.startUrl(url);

			printExtremityAtStart(dotPath, ug, stroke, color, arrowHeadColor);
			printExtremityAtEnd(dotPath, ug, stroke, color, arrowHeadColor);
			ug.apply(stroke).apply(color).draw(dotPath);

			if (url != null)
				ug.closeUrl();

		}
		drawLabel(ug, label, labelPosition, "label");
		drawLabel(ug, headLabel.text, headLabelPosition, "head_label");
		drawLabel(ug, tailLabel.text, tailLabelPosition, "tail_label");
		if (dotPath != null && orthogonal == false
				&& (tailLabel.hasPair() || headLabel.hasPair())) {
			final DotPath pathForRoles = getMirroredPath();
			drawSplineRole(ug, tailLabel, "tail_label", pathForRoles, true);
			drawSplineRole(ug, headLabel, "head_label", pathForRoles, false);
		}

		ug.closeGroup();
	}

	private DotPath getMirroredPath() {
		final DotPath path = getDotPathInternal();
		return path == null || ymirror == null ? path : ymirror.getMirrored(path);
	}

	private DotPath getDisplayedPath(StringBounder stringBounder) {
		DotPath dotPath = getMirroredPath();
		if (dotPath == null)
			return null;

		RectangleArea rectangleArea1 = null;
		RectangleArea rectangleArea2 = null;
		if (link.getEntity1().isGroup()) {
			final Cluster cluster1 = bibliotekon.getCluster(link.getEntity1());
			rectangleArea1 = cluster1.getRectangleArea();
		}
		if (link.getEntity2().isGroup()) {
			final Cluster cluster2 = bibliotekon.getCluster(link.getEntity2());
			rectangleArea2 = cluster2.getRectangleArea();
		}

		dotPath = dotPath.simulateCompound(rectangleArea2, rectangleArea1);

		// Attach the clipped path and its arrowheads to the visible symbol border.
		// A package folder, for example, has a title tab above the body border.
		// SvekEdge uses the same magnetic border for Graphviz.
		if (rectangleArea1 != null) {
			final Cluster cluster1 = bibliotekon.getCluster(link.getEntity1());
			final UTranslate force = cluster1.getMagneticBorder().getForceAt(stringBounder,
					dotPath.getStartPoint());
			dotPath.moveStartPoint(force);
		}
		if (rectangleArea2 != null) {
			final Cluster cluster2 = bibliotekon.getCluster(link.getEntity2());
			final UTranslate force = cluster2.getMagneticBorder().getForceAt(stringBounder,
					dotPath.getEndPoint());
			dotPath.moveEndPoint(force);
		}
		return dotPath;
	}

	/**
	 * Returns the path that will be painted, including a spline fallback.
	 * Orthogonal runs become tracks that subsequent edges can route alongside.
	 */
	DotPath routeOrthogonally(StringBounder stringBounder, Map<Entity, RectangleArea> nodeAreas,
			List<RectangleArea> nodes, List<RectangleArea> clusters, List<RectangleArea> usedTracks,
			OrthogonalPath.SearchBudget budget) {
		final DotPath spline = getDisplayedPath(stringBounder);
		if (spline == null)
			return null;
		if (budget.isSpent()) {
			keepsLayoutLabel = true;
			return spline;
		}
		// The edge enters the clusters around its ends. Any other cluster is as solid as a node.
		final List<RectangleArea> obstacles = new ArrayList<>(nodes);
		for (RectangleArea cluster : clusters)
			if (cluster.contains(spline.getStartPoint()) == false && cluster.contains(spline.getEndPoint()) == false)
				obstacles.add(cluster);
		final Entity source = link.getEntity1();
		final Entity target = link.getEntity2();
		final OrthogonalPath.Endpoint tail = endpoint(source, nodeAreas.get(source), link.getType().getDecor2(),
				stringBounder);
		final OrthogonalPath.Endpoint head = endpoint(target, nodeAreas.get(target), link.getType().getDecor1(),
				stringBounder);
		orthogonalPath = OrthogonalPath.route(spline, tail, head, obstacles, usedTracks, budget);
		if (orthogonalPath == null)
			return spline;
		for (XCubicCurve2D segment : orthogonalPath.getBeziers())
			usedTracks.add(RectangleArea.build(segment.getP1(), segment.getP2()));
		return orthogonalPath;
	}

	private OrthogonalPath.Endpoint endpoint(Entity entity, RectangleArea area, LinkDecor decor,
			StringBounder stringBounder) {
		final EntityPosition position = entity.getEntityPosition();
		final OrthogonalPath.Endpoint.Shape shape;
		if (position.isPort())
			shape = OrthogonalPath.Endpoint.Shape.BOX;
		else if (position.usePortP())
			shape = OrthogonalPath.Endpoint.Shape.CIRCLE;
		else
			shape = OrthogonalPath.Endpoint.Shape.FIXED;
		return new OrthogonalPath.Endpoint(area, shape, decorationExtent(decor, stringBounder));
	}

	private double decorationExtent(LinkDecor decor, StringBounder stringBounder) {
		// The outline above a downward-facing attachment.
		return -measureDecoration(decor, Math.PI / 2, stringBounder).getMinY();
	}

	private double decorationHalfWidth(LinkDecor decor, StringBounder stringBounder) {
		// The outline on either side of a rightward-facing attachment.
		final MinMax bounds = measureDecoration(decor, 0, stringBounder);
		return Math.max(-bounds.getMinY(), bounds.getMaxY());
	}

	private MinMax measureDecoration(LinkDecor decor, double angle, StringBounder stringBounder) {
		final ExtremityFactory factory = decor.getExtremityFactoryComplete(skinParam.getBackgroundColor());
		if (factory == null)
			return MinMax.getEmpty(true);
		// LimitFinder pads polygons horizontally, so only the vertical extent is exact.
		try {
			final UDrawable decoration = factory.createUDrawable(new XPoint2D(0, 0), angle, null);
			if (decoration != null)
				return TextBlockUtils.getMinMax(decoration, stringBounder, true);
		} catch (UnsupportedOperationException e) {
			// The normal drawing path also handles unsupported decorations.
		}
		return MinMax.getEmpty(true);
	}

	void reserveConnector(OrthogonalLabelPlacement placement, DotPath path, StringBounder stringBounder) {
		placement.addPath(path, getLineStroke(getStyle()).getThickness());
		reserveDecoration(placement, link.getType().getDecor2(), path.getStartPoint(), path.getStartAngle() + Math.PI,
				stringBounder);
		reserveDecoration(placement, link.getType().getDecor1(), path.getEndPoint(), path.getEndAngle(), stringBounder);
	}

	private void reserveDecoration(OrthogonalLabelPlacement placement, LinkDecor decor, XPoint2D tip, double angle,
			StringBounder stringBounder) {
		final double length = decorationExtent(decor, stringBounder);
		if (length == 0)
			return;
		final double halfWidth = decorationHalfWidth(decor, stringBounder);
		final XPoint2D base = new XPoint2D(tip.x - length * Math.cos(angle), tip.y - length * Math.sin(angle));
		final double marginX = halfWidth * Math.abs(Math.sin(angle));
		final double marginY = halfWidth * Math.abs(Math.cos(angle));
		placement.addDecoration(new RectangleArea(Math.min(tip.x, base.x) - marginX, Math.min(tip.y, base.y) - marginY,
				Math.max(tip.x, base.x) + marginX, Math.max(tip.y, base.y) + marginY));
	}

	/** A note draws the connector of its link itself, straight between both ends. */
	void reserveNoteConnector(OrthogonalLabelPlacement placement) {
		final XPoint2D start = getStartPoint();
		final XPoint2D end = getEndPoint();
		placement.addPath(new DotPath().addCurve(start, start, end, end), UStroke.simple().getThickness());
	}

	void placeEndpointLabels(OrthogonalLabelPlacement placement, DotPath path, StringBounder stringBounder) {
		final XPoint2D start = path.getStartPoint();
		final XPoint2D end = path.getEndPoint();
		if (isBlank(link.getQuantifier2(), link.getRole2()) == false)
			headLabelPosition = headLabel.place(placement, end, start, stringBounder);
		if (isBlank(link.getQuantifier1(), link.getRole1()) == false)
			tailLabelPosition = tailLabel.place(placement, start, end, stringBounder);
	}

	void placeMiddleLabel(OrthogonalLabelPlacement placement, DotPath path, StringBounder stringBounder) {
		final UTranslate original = getLabelRectangleTranslate("label");
		if (original == null || (link.getNote() == null && link.getLabel().isWhite()))
			return;
		final XDimension2D dimension = label.calculateDimension(stringBounder);
		if (keepsLayoutLabel)
			labelPosition = placement.keepLabel(original, dimension);
		else if (orthogonalPath == null)
			labelPosition = placement.placeSplineLabel(path, original, dimension);
		else
			labelPosition = placement.placeMidpoint(orthogonalPath, dimension);
	}

	// Blank labels keep the position from the layout and leave their room to visible ones.
	private static boolean isBlank(String... texts) {
		for (String text : texts)
			if (text != null && text.trim().isEmpty() == false)
				return false;
		return true;
	}

	private void drawLabel(UGraphic ug, TextBlock text, UTranslate routedPosition, String fieldName) {
		final UTranslate position = routedPosition == null ? getLabelRectangleTranslate(fieldName) : routedPosition;
		if (position != null)
			text.drawU(ug.apply(position));
	}

	private void drawSplineRole(UGraphic ug, EndpointLabel label, String fieldName, DotPath path, boolean tail) {
		if (label.hasPair() == false)
			return;
		final UTranslate position = getLabelRectangleTranslate(fieldName);
		if (position != null)
			drawRoleLabel(ug, label.role, label.quantifier, new XPoint2D(position.getDx(), position.getDy()),
					tail ? path.getStartPoint() : path.getEndPoint(),
					tail ? path.getEndPoint() : path.getStartPoint(), path.sample());
	}

	private static final class EndpointLabel {
		private final TextBlock quantifier;
		private final TextBlock role;
		private final TextBlock text;

		EndpointLabel(TextBlock quantifier, TextBlock role, boolean orthogonal) {
			this.quantifier = quantifier;
			this.role = role;
			if (orthogonal && hasPair())
				this.text = TextBlockUtils.mergeTB(quantifier, role, HorizontalAlignment.CENTER);
			else
				this.text = quantifier != null ? quantifier : role;
		}

		boolean hasPair() {
			return quantifier != null && role != null;
		}

		UTranslate place(OrthogonalLabelPlacement placement, XPoint2D end, XPoint2D other,
				StringBounder stringBounder) {
			if (text == null)
				return null;

			double rowOffset = 0;
			if (hasPair()) {
				// Each row's center is offset from the block's center by
				// half the height of the other row.
				final TextBlock adjacentRow = other.y > end.y ? quantifier : role;
				rowOffset = adjacentRow.calculateDimension(stringBounder).getHeight() / 2;
			}
			return placement.placeEndpoint(end, other, text.calculateDimension(stringBounder), rowOffset);
		}
	}

	private Style getStyle() {
		final StyleQuery query = StyleQueries.ARROW.add(diagramType().getStyleName())
				.withStereotype(link.getStereotype());
		return skinParam.getCurrentStyleBuilder().getMergedStyle(query);
	}

	private UStroke getLineStroke(Style style) {
		if (link.getColors() != null && link.getColors().getSpecificLineStroke() != null)
			return link.getColors().getSpecificLineStroke();

		final LinkType type = link.getType();
		final UStroke suggested = style.getStroke();
		if (suggested != null && type.getStyle().isNormal())
			return type.getStroke3(suggested);

		return type.getStroke3(skinParam.getThickness(LineParam.arrow, null));
	}

	public XPoint2D getStartPoint() {
		final DotPath dotPath = getDotPathInternal();
		XPoint2D pt = dotPath.getStartPoint();
		if (ymirror != null)
			pt = ymirror.getMirrored(pt);

		return pt;
	}

	public XPoint2D getEndPoint() {
		final DotPath dotPath = getDotPathInternal();
		XPoint2D pt = dotPath.getEndPoint();
		if (ymirror != null)
			pt = ymirror.getMirrored(pt);

		return pt;
	}

	// Same rules as SvekEdge. The style of the arrow can give the decorations
	// their own color, and a transparent one leaves only the line of an arrow.
	private void drawExtremity(UGraphic ug, Extremity extremity, LinkDecor decor, UStroke stroke, HColor color,
			HColor headColor) {
		if (headColor.isTransparent()) {
			// Only arrows get here, see isHidden().
			((ExtremityArrow) extremity).drawLineIfTransparent(ug.apply(color).apply(stroke.onlyThickness()));
			return;
		}
		// Filled decorations take the connector color as their fill.
		// Hollow ones keep the transparent background of the connector.
		UGraphic ugHead = ug.apply(headColor).apply(stroke.onlyThickness());
		if (decor.isFill())
			ugHead = ugHead.apply(color.bg());
		extremity.drawU(ugHead);
	}

	// A transparent decoration other than an arrow paints nothing, so the
	// connector keeps its full length instead of making room for it.
	private boolean isHidden(Extremity extremity, HColor headColor) {
		return headColor.isTransparent() && extremity instanceof ExtremityArrow == false;
	}

	private void printExtremityAtStart(DotPath dotPath, UGraphic ug, UStroke stroke, HColor color,
			HColor headColor) {
		final ExtremityFactory extremityFactory2 = link.getType().getDecor2()
				.getExtremityFactoryComplete(skinParam.getBackgroundColor());
		if (extremityFactory2 == null)
			return;

		final XPoint2D p0 = dotPath.getStartPoint();
		final double startAngle = dotPath.getStartAngle() + Math.PI;

		try {
			final Extremity extremity2 = (Extremity) extremityFactory2.createUDrawable(p0, startAngle, null);
			if (extremity2 != null && isHidden(extremity2, headColor) == false) {
				if (getLinkStrategy() == LinkStrategy.SIMPLEST) {
					final double decorationLength = decorationLength(dotPath, extremity2, 0);
					dotPath.moveStartPoint(new UTranslate(decorationLength, 0).rotate(startAngle - Math.PI));
				}
				drawExtremity(ug, extremity2, link.getType().getDecor2(), stroke, color, headColor);
			}

		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			System.err.println("CANNOT DRAW printExtremityAtStart");
		}
	}

	private void printExtremityAtEnd(DotPath dotPath, UGraphic ug, UStroke stroke, HColor color, HColor headColor) {
		final ExtremityFactory extremityFactory1 = link.getType().getDecor1()
				.getExtremityFactoryComplete(skinParam.getBackgroundColor());
		if (extremityFactory1 == null)
			return;

		final XPoint2D p0 = dotPath.getEndPoint();
		final double endAngle = dotPath.getEndAngle();

		try {
			final Extremity extremity1 = (Extremity) extremityFactory1.createUDrawable(p0, endAngle, null);
			if (extremity1 != null && isHidden(extremity1, headColor) == false) {
				if (getLinkStrategy() == LinkStrategy.SIMPLEST) {
					final double decorationLength = decorationLength(dotPath, extremity1, dotPath.getBeziers().size() - 1);
					dotPath.moveEndPoint(new UTranslate(decorationLength, 0).rotate(endAngle - Math.PI));
				}
				drawExtremity(ug, extremity1, link.getType().getDecor1(), stroke, color, headColor);
			}

		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			System.err.println("CANNOT DRAW printExtremityAtEnd");
		}
	}

	private double decorationLength(DotPath path, Extremity extremity, int segment) {
		final double length = extremity.getDecorationLength();
		if (orthogonalPath == null)
			return length;
		// A decoration can consume the whole last run. Keep the shortened edge on
		// its original axis instead of moving the endpoint past its nearest bend.
		final XCubicCurve2D line = path.getBeziers().get(segment);
		return Math.min(length, Math.max(0, line.getP1().distance(line.getP2()) - 0.01));
	}

//	private void printDebug(UGraphic ug) {
//		ug = ug.apply(HColors.BLUE).apply(HColors.BLUE.bg());
//		final ST_splines splines = getSplines(edge);
//		final ST_bezier beziers = splines.list.get__(0);
//		for (int i = 0; i < beziers.size; i++) {
//			XPoint2D pt = getPoint(splines, i);
//			if (ymirror != null)
//				pt = ymirror.getMirrored(pt);
//
//			ug.apply(UTranslate.point(pt).compose(new UTranslate(-1, -1))).draw(UEllipse.build(3, 3));
//		}
//		if (getLabelRectangleTranslate("label") != null && getLabelURectangle() != null) {
//			ug = ug.apply(HColors.BLUE).apply(HColors.none().bg());
//			ug.apply(getLabelRectangleTranslate("label")).draw(getLabelURectangle());
//		}
//
//	}

	private URectangle getLabelURectangle() {
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) edge.data.castTo(ST_Agedgeinfo_t.class);
		ST_textlabel_t label = (ST_textlabel_t) data.label;
		if (label == null)
			return null;

		final BoxInfo boxInfo = BoxInfo.fromTextlabel(label);
		return URectangle.build(boxInfo.getDimension());
	}

	private UTranslate getLabelRectangleTranslate(String fieldName) {
		// final String fieldName = "label";
		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) edge.data;
		ST_textlabel_t label = null;
		if (fieldName.equals("label"))
			label = data.label;
		else if (fieldName.equals("head_label"))
			label = data.head_label;
		else if (fieldName.equals("tail_label"))
			label = data.tail_label;

		if (label == null)
			return null;

		final BoxInfo boxInfo = BoxInfo.fromTextlabel(label);

		if (ymirror == null)
			return new UTranslate(boxInfo.getLowerLeft().getX(), boxInfo.getUpperRight().getY());

		return ymirror.getMirrored(UTranslate.point(boxInfo.getLowerLeft()));
	}

	// private DotPath dotPath;

	private DotPath getDotPathInternal() {
//		if (dotPath != null)
//			return dotPath;

		final ST_Agedgeinfo_t data = (ST_Agedgeinfo_t) edge.data;
		final ST_splines splines = data.spl;

		DotPath dotPath = new DotPath();
		final ST_bezier beziers = (ST_bezier) splines.list.get__(0);
		final XPoint2D pt1 = getPoint(splines, 0);
		final XPoint2D pt2 = getPoint(splines, 1);
		final XPoint2D pt3 = getPoint(splines, 2);
		final XPoint2D pt4 = getPoint(splines, 3);
		dotPath = dotPath.addCurve(pt1, pt2, pt3, pt4);
		final int n = beziers.size;
		for (int i = 4; i < n; i += 3) {
			final XPoint2D ppt2 = getPoint(splines, i);
			final XPoint2D ppt3 = getPoint(splines, i + 1);
			final XPoint2D ppt4 = getPoint(splines, i + 2);
			dotPath = dotPath.addCurve(ppt2, ppt3, ppt4);
		}

		return dotPath;
	}

	private XPoint2D getPoint(ST_splines splines, int i) {
		final ST_bezier beziers = (ST_bezier) splines.list.get__(0);
		final ST_pointf pt = beziers.list.get__(i);
		return new XPoint2D(pt.x, pt.y);
	}

	/**
	 * The areas of the two nodes of the link, when they are already placed.
	 */
	private List<RectangleArea> getNodeAreas() {
		final List<RectangleArea> result = new ArrayList<>();
		for (Entity entity : new Entity[] { link.getEntity1(), link.getEntity2() }) {
			final SvekNode node = bibliotekon.getNode(entity);
			if (node != null)
				result.add(node.getRectangleArea());
		}
		return result;
	}

	private void drawRoleLabel(UGraphic ug, TextBlock role, TextBlock quantifier,
			XPoint2D quantifierPos, XPoint2D thisEndpoint, XPoint2D otherEndpoint, Set<XPoint2D> pathSamples) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XPoint2D pos = RoleLabels.getPosition(quantifier.calculateDimension(stringBounder),
				role.calculateDimension(stringBounder), quantifierPos, thisEndpoint, otherEndpoint, pathSamples,
				getNodeAreas());
		role.drawU(ug.apply(new UTranslate(pos.getX(), pos.getY())));
	}

	@Override
	public void moveDelta(double deltaX, double deltaY) {
		throw new UnsupportedOperationException("refactor in progress");
		
	}

	@Override
	public boolean isHidden() {
		throw new UnsupportedOperationException("refactor in progress");
	}

	@Override
	public Direction getArrowDirection() {
		throw new UnsupportedOperationException("refactor in progress");
	}

	@Override
	public double getArrowDirectionInRadian() {
		throw new UnsupportedOperationException("refactor in progress");
	}

}
