/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Hideable;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.StringUtils;

public class Shape implements Positionable, IShapePseudo, Hideable {

	private final ShapeType type;
	private final double width;
	private final double height;

	private final String uid;
	private final int color;

	private double minX;
	private double minY;
	private final int shield;

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

	public Shape(IEntityImage image, ShapeType type, double width, double height, ColorSequence colorSequence,
			boolean top, int shield, EntityPosition entityPosition) {
		this.entityPosition = entityPosition;
		this.image = image;
		this.top = top;
		this.type = type;
		this.width = width;
		this.height = height;
		this.color = colorSequence.getValue();
		this.uid = String.format("sh%04d", color);
		this.shield = shield;
		if (shield > 0 && type != ShapeType.RECTANGLE) {
			throw new IllegalArgumentException();
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

	public void appendShape(StringBuilder sb) {
		if (type == ShapeType.RECTANGLE && shield > 0) {
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
		sb.append("color=\"" + StringUtils.getAsHtml(color) + "\"");
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
		appendTd(sb, shield, shield);
		appendTd(sb);
		sb.append("</TR>");
		sb.append("<TR>");
		appendTd(sb, shield, shield);
		sb.append("<TD BGCOLOR=\"" + StringUtils.getAsHtml(color) + "\"");
		sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + getWidth() + "\" HEIGHT=\"" + getHeight() + "\"");
		sb.append(" PORT=\"h\">");
		sb.append("</TD>");
		appendTd(sb, shield, shield);
		sb.append("</TR>");
		sb.append("<TR>");
		appendTd(sb);
		appendTd(sb, shield, shield);
		appendTd(sb);
		sb.append("</TR>");
		sb.append("</TABLE>");
	}

	private void appendTd(StringBuilder sb, int w, int h) {
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
		if (type == ShapeType.RECTANGLE && shield > 0) {
			throw new UnsupportedOperationException();
		} else if (type == ShapeType.RECTANGLE) {
			sb.append("shape=rect");
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
		return shield > 0;
	}

	public void moveSvek(double deltaX, double deltaY) {
		this.minX += deltaX;
		this.minY += deltaY;
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		final EntityImageStateBorder im = (EntityImageStateBorder) image;
		return im.getMaxWidthFromLabelForEntryExit(stringBounder);
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

}
