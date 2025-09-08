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
package net.sourceforge.plantuml.svek;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.Together;
import net.sourceforge.plantuml.cruise.XNode;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterface;
import net.sourceforge.plantuml.svek.image.EntityImagePort;
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;
import net.sourceforge.plantuml.utils.Direction;

public class SvekNode implements XNode {

	private final ShapeType type;
	private XDimension2D dimImage;

	private final String uid;
	private final int color;

	private double minX;
	private double minY;
	private Margins shield;

	private final EntityPosition entityPosition;
	private final IEntityImage image;
	private final StringBounder stringBounder;

	public EntityPosition getEntityPosition() {
		return entityPosition;
	}

	private Cluster cluster;

	public final Cluster getCluster() {
		return cluster;
	}

	public final void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public String toString() {
		return super.toString() + " " + image + " " + type;
	}

	private final Entity leaf;

	public final Together getTogether() {
		if (leaf == null)
			return null;
		return leaf.getTogether();
	}

	SvekNode(Entity ent, IEntityImage image, ColorSequence colorSequence, StringBounder stringBounder) {
		this.stringBounder = stringBounder;
		this.entityPosition = ent.getEntityPosition();
		this.image = image;
		this.type = image.getShapeType();

		this.color = colorSequence.getValue();
		this.uid = String.format("sh%04d", color);
		this.leaf = ent;
	}

	private XDimension2D getDimImage() {
		if (dimImage == null)
			this.dimImage = image.calculateDimension(stringBounder);
		return dimImage;
	}

	public final ShapeType getType() {
		return type;
	}

	public final double getWidth() {
		return getDimImage().getWidth();
	}

	public final double getHeight() {
		return getDimImage().getHeight();
	}

	public void appendShape(StringBuilder sb, StringBounder stringBounder) {
		if (type == ShapeType.RECTANGLE_HTML_FOR_PORTS) {
			appendLabelHtmlSpecialForLink(sb, stringBounder);
			SvekUtils.println(sb);
			return;
		}
		if (type == ShapeType.RECTANGLE_PORT) {
			appendLabelHtmlSpecialForPort(sb, stringBounder);
			SvekUtils.println(sb);
			return;
		}
		if (type == ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE) {
			appendHtml(sb);
			SvekUtils.println(sb);
			return;
		}
		if (type == ShapeType.RECTANGLE && isShielded()) {
			appendHtml(sb);
			SvekUtils.println(sb);
			return;
		}
		sb.append(uid);
		sb.append(" [");
		appendShapeInternal(sb);
		sb.append(",");
		sb.append("label=\"\"");
		sb.append(",");
		sb.append("width=" + SvekUtils.pixelToInches(getWidth()));
		sb.append(",");
		sb.append("height=" + SvekUtils.pixelToInches(getHeight()));
		sb.append(",");
		sb.append("color=\"" + StringUtils.sharp000000(color) + "\"");
		sb.append("];");
		SvekUtils.println(sb);
	}

	private double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		if (image instanceof EntityImagePort) {
			final EntityImagePort im = (EntityImagePort) image;
			return im.getMaxWidthFromLabelForEntryExit(stringBounder);
		}
		if (image instanceof EntityImageStateBorder) {
			final EntityImageStateBorder im = (EntityImageStateBorder) image;
			return im.getMaxWidthFromLabelForEntryExit(stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private void appendLabelHtmlSpecialForPort(StringBuilder sb, StringBounder stringBounder) {
		final int width1 = (int) getWidth();
		final int width2 = (int) getMaxWidthFromLabelForEntryExit(stringBounder);
		if (width2 > 40)
			appendLabelHtmlSpecialForPortHtml(sb, stringBounder, width2 - 40);
		else
			appendLabelHtmlSpecialForPortBasic(sb, stringBounder);
	}

	private void appendLabelHtmlSpecialForPortHtml(StringBuilder sb, StringBounder stringBounder, int fullWidth) {
		if (fullWidth < 10)
			fullWidth = 10;
		sb.append(uid);
		sb.append(" [");
		sb.append("shape=plaintext");
		sb.append(",");
		sb.append("label=<");
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		sb.append("<TR><TD WIDTH=\"" + fullWidth + "\" HEIGHT=\"1\" COLSPAN=\"3\"></TD></TR>");
		sb.append("<TR><TD></TD><TD FIXEDSIZE=\"TRUE\" PORT=\"P\"  BORDER=\"1\" COLOR=\""
				+ StringUtils.sharp000000(color) + "\" WIDTH=\"" + (int) getWidth() + "\" HEIGHT=\"" + (int) getHeight()
				+ "\"></TD><TD></TD></TR>");
		sb.append("<TR><TD WIDTH=\"" + fullWidth + "\" HEIGHT=\"1\" COLSPAN=\"3\"></TD></TR>");
		sb.append("</TABLE>");
		sb.append(">];");
	}

	private void appendLabelHtmlSpecialForPortBasic(StringBuilder sb, StringBounder stringBounder) {
		sb.append(uid);
		sb.append(" [");
		sb.append("shape=rect");
		sb.append(",");
		sb.append("label=\"\"");
		sb.append(",");
		sb.append("width=" + SvekUtils.pixelToInches(getWidth()));
		sb.append(",");
		sb.append("height=" + SvekUtils.pixelToInches(getHeight()));
		sb.append(",");
		sb.append("color=\"" + StringUtils.sharp000000(color) + "\"");
		sb.append("];");
	}

	private Margins shield() {
		if (shield == null) {
			this.shield = image.getShield(stringBounder);
			if (!shield.isZero() && type != ShapeType.RECTANGLE && type != ShapeType.RECTANGLE_HTML_FOR_PORTS
					&& type != ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE)
				throw new IllegalStateException();
		}

		return shield;
	}

	private void appendHtml(StringBuilder sb) {
		sb.append(uid);
		sb.append(" [");
		sb.append("shape=plaintext,");
		sb.append("label=<");
		appendLabelHtml(sb);
		sb.append(">");
		sb.append("];");
		SvekUtils.println(sb);

	}

	private void appendLabelHtml(StringBuilder sb) {
		// Log.println("shield=" + shield);
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		sb.append("<TR>");
		appendTd(sb);
		appendTd(sb, 1, shield().getY1());
		appendTd(sb);
		sb.append("</TR>");
		sb.append("<TR>");
		appendTd(sb, shield().getX1(), 1);
		sb.append("<TD BGCOLOR=\"" + StringUtils.sharp000000(color) + "\"");
		sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + getWidth() + "\" HEIGHT=\"" + getHeight() + "\"");
		sb.append(" PORT=\"h\">");
		sb.append("</TD>");
		appendTd(sb, shield().getX2(), 1);
		sb.append("</TR>");
		sb.append("<TR>");
		appendTd(sb);
		appendTd(sb, 1, shield().getY2());
		appendTd(sb);
		sb.append("</TR>");
		sb.append("</TABLE>");
	}

	private void appendLabelHtmlSpecialForLink(StringBuilder sb, StringBounder stringBounder) {
		final Ports ports = ((WithPorts) this.image).getPorts(stringBounder);

		sb.append(uid);
		sb.append(" [");
		sb.append("shape=plaintext,");
		// sb.append("color=\"" + StringUtils.getAsHtml(color) + "\",");
		sb.append("label=<");
		sb.append("<TABLE BGCOLOR=\"" + StringUtils.sharp000000(color)
				+ "\" BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		int sum = 0;
		for (PortGeometry geom : ports.getAllPortGeometry()) {
			final String portId = geom.getId();
			final int missing = (int) (geom.getPosition() - sum);

			sum += missing;
			appendTr(sb, null, missing);
			int intHeight = (int) geom.getHeight();

			appendTr(sb, portId, intHeight);
			sum += intHeight;
		}
		final double diff = getHeight() - sum;
		appendTr(sb, null, (int) diff);

		sb.append("</TABLE>");
		sb.append(">");
		sb.append("];");
		SvekUtils.println(sb);
	}

	private void appendTr(StringBuilder sb, String portId, int height) {
		if (height <= 0)
			return;

		sb.append("<TR>");
		sb.append("<TD ");
		sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + getWidth() + "\" HEIGHT=\"" + height + "\"");
		if (portId != null)
			sb.append(" PORT=\"" + portId + "\"");

		sb.append(">");
		sb.append("</TD>");
		sb.append("</TR>");
	}

	private void appendTd(StringBuilder sb, double w, double h) {
		sb.append("<TD");
		sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + w + "\" HEIGHT=\"" + h + "\"");
		sb.append(">");
		sb.append("</TD>");
	}

	private void appendTd(StringBuilder sb) {
		sb.append("<TD>");
		sb.append("</TD>");
	}

	private void appendShapeInternal(StringBuilder sb) {
		if (type == ShapeType.RECTANGLE && isShielded())
			throw new UnsupportedOperationException();
		else if (type == ShapeType.RECTANGLE || type == ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE
				|| type == ShapeType.FOLDER)
			sb.append("shape=rect");
		else if (type == ShapeType.RECTANGLE_HTML_FOR_PORTS)
			throw new UnsupportedOperationException();
		else if (type == ShapeType.OCTAGON)
			sb.append("shape=octagon");
		else if (type == ShapeType.HEXAGON)
			sb.append("shape=hexagon");
		else if (type == ShapeType.DIAMOND)
			sb.append("shape=diamond");
		else if (type == ShapeType.CIRCLE)
			sb.append("shape=circle");
		else if (type == ShapeType.OVAL)
			sb.append("shape=ellipse");
		else if (type == ShapeType.ROUND_RECTANGLE)
			sb.append("shape=rect,style=rounded");
		else
			throw new IllegalStateException(type.toString());

	}

	public final String getUid() {
		if (uid == null)
			throw new IllegalStateException();

		return uid;
	}

	public final double getMinX() {
		return minX;
	}

	public final double getMinY() {
		return minY;
	}

	public IEntityImage getImage() {
		return image;
	}

	public XPoint2D getPosition() {
		return new XPoint2D(minX, minY);
	}

	public XDimension2D getSize() {
		return getDimImage();
	}

	public RectangleArea getRectangleArea() {
		return new RectangleArea(minX, minY, minX + getWidth(), minY + getHeight());
	}

	public boolean isShielded() {
		// Avoid calculating "shield" size through this.shield() before finishing
		// creation of all SvekEdges (https://github.com/plantuml/plantuml/issues/1467)
		// Instead, only check if we will have a shield (size is irrelevant here)
		// This node will have a shield if it is target of a qualified association (will
		// have a qualifier label
		// placed besides this type's bounding box.)
		for (Link link : this.leaf.getDiagram().getLinks())
			if ((this.leaf == link.getEntity1() && link.hasKal1())
					|| (this.leaf == link.getEntity2() && link.hasKal2()))
				return true;

		return shield().isZero() == false;
	}

	public void resetMove() {
		this.minX = 0;
		this.minY = 0;
	}

	public void moveDelta(double deltaX, double deltaY) {
		this.minX += deltaX;
		this.minY += deltaY;
	}

	public boolean isHidden() {
		return image.isHidden();
	}

	private Shadowable polygon;

	public void setPolygon(double minX, double minY, List<XPoint2D> points) {
		this.polygon = new UPolygon(points).translate(-minX, -minY);
	}

	public Shadowable getPolygon() {
		return polygon;
	}

	public XPoint2D getPoint2D(double x, double y) {
		return new XPoint2D(minX + x, minY + y);
	}

	public double getOverscanX(StringBounder stringBounder) {
		return image.getOverscanX(stringBounder);
	}

	public void addImpact(double angle) {
		((EntityImageLollipopInterface) image).addImpact(angle);
	}

	public void drawKals(UGraphic ug) {
		if (leaf instanceof Entity == false)
			return;

		drawList(ug, leaf.getKals(Direction.DOWN));
		drawList(ug, leaf.getKals(Direction.UP));
		drawList(ug, leaf.getKals(Direction.LEFT));
		drawList(ug, leaf.getKals(Direction.RIGHT));

	}

	public void fixOverlap() {
		if (leaf instanceof Entity == false)
			return;

		fixHoverlap(leaf.getKals(Direction.DOWN));
		fixHoverlap(leaf.getKals(Direction.UP));
	}

	private void fixHoverlap(final List<Kal> list) {
		final LineOfSegments los = new LineOfSegments();
		for (Kal kal : list)
			los.addSegment(kal.getX1(), kal.getX2());

		final double[] res = los.solveOverlaps();
		for (int i = 0; i < list.size(); i++) {
			final Kal kal = list.get(i);
			final double diff = res[i] - kal.getX1();
			kal.moveX(diff);
		}
	}

	private void drawList(UGraphic ug, final List<Kal> list) {
		for (Kal kal : list)
			kal.drawU(ug);
	}

//	public XPoint2D projection(XPoint2D pt, StringBounder stringBounder) {
//	if (getType() != ShapeType.FOLDER)
//		return pt;
//
////	final ClusterPosition clusterPosition = new ClusterPosition(minX, minY, minX + getWidth(), minY + getHeight());
////	if (clusterPosition.isPointJustUpper(pt)) {
////		final XDimension2D dimName = ((EntityImageDescription) image).getNameDimension(stringBounder);
////		if (pt.getX() < minX + dimName.getWidth())
////			return pt;
////
////		return new XPoint2D(pt.getX(), pt.getY() + dimName.getHeight() + 4);
////	}
//	return pt;
//}

	public MagneticBorder getMagneticBorder() {
		return new MagneticBorder() {
			public UTranslate getForceAt(StringBounder stringBounder, XPoint2D position) {
				final MagneticBorder orig = image.getMagneticBorder();
				return orig.getForceAt(stringBounder, position.move(-minX, -minY));
			}
		};
//		return image.getMagneticBorder();
//		if (getType() != ShapeType.FOLDER)
//			return new MagneticBorderNone();
//
//		return new MagneticBorder() {
//			@Override
//			public UTranslate getForceAt(XPoint2D pt) {
//				if ((pt.getX() >= minX && pt.getX() <= minX + getWidth() && pt.getY() <= minY)) {
//					final XDimension2D dimName = ((EntityImageDescription) image).getNameDimension(stringBounder);
//					if (pt.getX() < minX + dimName.getWidth())
//						return UTranslate.none();
//
//					return new UTranslate(0, dimName.getHeight() + 4);
//				}
//				return UTranslate.none();
//			}
//		};
	}
	
	
	public final CharSequence getBestMatch(CharSequence candidate) {
		return leaf.getBestMatch(candidate);

	}


}
