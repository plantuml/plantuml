/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Hideable;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.entity.EntityImpl;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.svek.image.AbstractEntityImageBorder;
import net.sourceforge.plantuml.svek.image.EntityImageDescription;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterface;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class Node implements Positionable, IShapePseudo, Hideable {

	private final ShapeType type;
	private final double width;
	private final double height;

	private final String uid;
	private final int color;

	private double minX;
	private double minY;
	private final Margins shield;

	private final EntityPosition entityPosition;
	private final IEntityImage image;

	public EntityPosition getEntityPosition() {
		return entityPosition;
	}

	private Cluster cluster;

	private final boolean top;

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

	private final ILeaf leaf;
	private final IGroup group;

	Node(ILeaf ent, IEntityImage image, ColorSequence colorSequence, StringBounder stringBounder) {
		final Dimension2D dim = image.calculateDimension(stringBounder);
		this.entityPosition = ent.getEntityPosition();
		this.image = image;
		this.top = ent.isTop();
		this.type = image.getShapeType();
		this.width = dim.getWidth();
		this.height = dim.getHeight();
		this.color = colorSequence.getValue();
		this.uid = String.format("sh%04d", color);
		this.shield = image.getShield(stringBounder);
		if (shield.isZero() == false && type != ShapeType.RECTANGLE && type != ShapeType.RECTANGLE_HTML_FOR_PORTS
				&& type != ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE) {
			throw new IllegalArgumentException();
		}

		if (((EntityImpl) ent).getOriginalGroup() == null) {
			this.group = null;
			this.leaf = ent;
		} else {
			this.group = ((EntityImpl) ent).getOriginalGroup();
			this.leaf = null;
		}
	}

	public final ShapeType getType() {
		return type;
	}

	public final double getWidth() {
		return width;
	}

	public final double getHeight() {
		return height;
	}

	public void appendShape(StringBuilder sb, StringBounder stringBounder) {
		if (type == ShapeType.RECTANGLE_HTML_FOR_PORTS) {
			appendLabelHtmlSpecialForLink(sb, stringBounder);
			return;
		}
		if (type == ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE) {
			appendHtml(sb);
			return;
		}
		if (type == ShapeType.RECTANGLE && shield.isZero() == false) {
			appendHtml(sb);
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
		sb.append("color=\"" + DotStringFactory.sharp000000(color) + "\"");
		sb.append("];");
		SvekUtils.println(sb);
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
		appendTd(sb, 1, shield.getY1());
		appendTd(sb);
		sb.append("</TR>");
		sb.append("<TR>");
		appendTd(sb, shield.getX1(), 1);
		sb.append("<TD BGCOLOR=\"" + DotStringFactory.sharp000000(color) + "\"");
		sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + getWidth() + "\" HEIGHT=\"" + getHeight() + "\"");
		sb.append(" PORT=\"h\">");
		sb.append("</TD>");
		appendTd(sb, shield.getX2(), 1);
		sb.append("</TR>");
		sb.append("<TR>");
		appendTd(sb);
		appendTd(sb, 1, shield.getY2());
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
		sb.append("<TABLE BGCOLOR=\"" + DotStringFactory.sharp000000(color)
				+ "\" BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		double position = 0;
		for (Map.Entry<String, PortGeometry> ent : ports.getAllWithEncodedPortId().entrySet()) {
			final String portId = ent.getKey();
			final PortGeometry geom = ent.getValue();
			final double missing = geom.getPosition() - position;
			appendTr(sb, null, missing);
			appendTr(sb, portId, geom.getHeight());
			position = geom.getLastY();
		}
		appendTr(sb, null, getHeight() - position);
		sb.append("</TABLE>");
		sb.append(">");
		sb.append("];");
		SvekUtils.println(sb);
	}

	private void appendTr(StringBuilder sb, String portId, double height) {
		if (height <= 0) {
			return;
		}
		sb.append("<TR>");
		sb.append("<TD ");
		sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + getWidth() + "\" HEIGHT=\"" + height + "\"");
		if (portId != null) {
			sb.append(" PORT=\"" + portId + "\"");
		}
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
		if (type == ShapeType.RECTANGLE && shield.isZero() == false) {
			throw new UnsupportedOperationException();
		} else if (type == ShapeType.RECTANGLE || type == ShapeType.RECTANGLE_WITH_CIRCLE_INSIDE
				|| type == ShapeType.FOLDER) {
			sb.append("shape=rect");
		} else if (type == ShapeType.RECTANGLE_HTML_FOR_PORTS) {
			throw new UnsupportedOperationException();
		} else if (type == ShapeType.OCTAGON) {
			sb.append("shape=octagon");
		} else if (type == ShapeType.DIAMOND) {
			sb.append("shape=diamond");
		} else if (type == ShapeType.CIRCLE) {
			sb.append("shape=circle");
		} else if (type == ShapeType.CIRCLE_IN_RECT) {
			sb.append("shape=circle");
		} else if (type == ShapeType.OVAL) {
			sb.append("shape=ellipse");
		} else if (type == ShapeType.ROUND_RECTANGLE) {
			sb.append("shape=rect,style=rounded");
		} else {
			throw new IllegalStateException(type.toString());
		}
	}

	public final String getUid() {
		if (uid == null) {
			throw new IllegalStateException();
		}
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

	public final boolean isTop() {
		return top;
	}

	public Point2D getPosition() {
		return new Point2D.Double(minX, minY);
	}

	public Dimension2D getSize() {
		return new Dimension2DDouble(width, height);
	}

	public ClusterPosition getClusterPosition() {
		return new ClusterPosition(minX, minY, minX + width, minY + height);
	}

	public boolean isShielded() {
		return shield.isZero() == false;
	}

	public void moveSvek(double deltaX, double deltaY) {
		this.minX += deltaX;
		this.minY += deltaY;
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		if (image instanceof AbstractEntityImageBorder) {
			final AbstractEntityImageBorder im = (AbstractEntityImageBorder) image;
			return im.getMaxWidthFromLabelForEntryExit(stringBounder);
		} else {
			final Dimension2D dim = image.calculateDimension(stringBounder);
			return dim.getWidth();
		}
	}

	public boolean isHidden() {
		return image.isHidden();
	}

	private Shadowable octagon;

	public void setOctagon(double minX, double minY, List<Point2D.Double> points) {
		this.octagon = new UPolygon(points).translate(-minX, -minY);
	}

	public Shadowable getOctagon() {
		return octagon;
	}

	public Point2D getPoint2D(double x, double y) {
		return new Point2D.Double(minX + x, minY + y);
	}

	public Point2D projection(Point2D pt, StringBounder stringBounder) {
		if (getType() != ShapeType.FOLDER) {
			return pt;
		}
		final ClusterPosition clusterPosition = new ClusterPosition(minX, minY, minX + width, minY + height);
		if (clusterPosition.isPointJustUpper(pt)) {
			final Dimension2D dimName = ((EntityImageDescription) image).getNameDimension(stringBounder);
			if (pt.getX() < minX + dimName.getWidth()) {
				return pt;
			}
			return new Point2D.Double(pt.getX(), pt.getY() + dimName.getHeight() + 4);
		}
		return pt;
	}

	public double getOverscanX(StringBounder stringBounder) {
		return image.getOverscanX(stringBounder);
	}

	public void addImpact(double angle) {
		((EntityImageLollipopInterface) image).addImpact(angle);
	}
}
