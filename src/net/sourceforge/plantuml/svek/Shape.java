/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.posimo.Positionable;

public class Shape implements Positionable {

	private final ShapeType type;
	private final double width;
	private final double height;

	private final String uid;
	private final int color;

	private double minX;
	private double minY;
	private final int shield;

	private Cluster cluster;

	private final boolean top;

	public final Cluster getCluster() {
		return cluster;
	}

	public final void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Shape(IEntityImage image, ShapeType type, double width, double height, ColorSequence colorSequence, boolean top, int shield) {
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
		// if (type == ShapeType.CIRCLE_IN_RECT) {
		// sb.append("subgraph clusterlol" + uid + " {");
		// DotStringFactory.println(sb);
		// }
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
		// if (type == ShapeType.CIRCLE_IN_RECT) {
		// sb.append("}");
		// DotStringFactory.println(sb);
		// }

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
		// System.err.println("shield=" + shield);
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

	public final void setMinX(double minX) {
		// if (minX < 0) {
		// minX = 0;
		// }
		this.minX = minX;
	}

	public final double getMinY() {
		return minY;
	}

	public final void setMinY(double minY) {
		if (minY < 0) {
			minY = 0;
		}
		this.minY = minY;
	}

	private final IEntityImage image;

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

	// public boolean contains(Point2D pt) {
	// return pt.getX() >= minX && pt.getX() <= minX + width && pt.getY() >=
	// minY && pt.getY() <= minY + height;
	// }
	//
	// public boolean intersect(Point2D pt, Dimension2D dim) {
	// if (contains(pt)) {
	// return true;
	// }
	// if (contains(new Point2D.Double(pt.getX() + dim.getWidth(), pt.getY())))
	// {
	// return true;
	// }
	// if (contains(new Point2D.Double(pt.getX() + dim.getWidth(), pt.getY() +
	// dim.getHeight()))) {
	// return true;
	// }
	// if (contains(new Point2D.Double(pt.getX(), pt.getY() + dim.getHeight())))
	// {
	// return true;
	// }
	// return false;
	// }
	//	
	// public boolean intersect(Positionable p) {
	// return intersect(p.getPosition(), p.getSize());
	// }

	public boolean isShielded() {
		return shield > 0;
	}

	public String getCoords(double deltaX, double deltaY) {
		final int x1 = (int) (getMinX() + deltaX);
		final int y1 = (int) (getMinY() + deltaY);
		final int x2 = (int) (getMinX() + getWidth() + deltaX);
		final int y2 = (int) (getMinY() + getHeight() + deltaY);
		return "" + x1 + "," + y1 + "," + x2 + "," + y2;
	}

	public void moveSvek(double deltaX, double deltaY) {
		this.minX += deltaX;
		this.minY += deltaY;
	}
	
	
	public static IEntityImage printEntity(IEntity ent, DotData dotData) {

		final IEntityImage image;
		if (ent.getSvekImage() == null) {
			image = createEntityImageBlock(dotData, ent);
		} else {
			image = ent.getSvekImage();
		}
		return image;
	}
	
	private static IEntityImage createEntityImageBlock(DotData dotData, IEntity ent) {
		if (ent.getType() == EntityType.CLASS || ent.getType() == EntityType.ABSTRACT_CLASS
				|| ent.getType() == EntityType.INTERFACE || ent.getType() == EntityType.ENUM) {
			return new EntityImageClass(ent, dotData.getSkinParam(), dotData);
		}
		if (ent.getType() == EntityType.NOTE) {
			return new EntityImageNote(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.ACTIVITY) {
			return new EntityImageActivity(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.STATE) {
			return new EntityImageState(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.CIRCLE_START) {
			return new EntityImageCircleStart(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.CIRCLE_END) {
			return new EntityImageCircleEnd(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.USECASE) {
			return new EntityImageUseCase(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.BRANCH) {
			return new EntityImageBranch(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.LOLLIPOP) {
			return new EntityImageLollipopInterface(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.ACTOR) {
			return new EntityImageActor(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.COMPONENT) {
			return new EntityImageComponent(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.OBJECT) {
			return new EntityImageObject(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.SYNCHRO_BAR) {
			return new EntityImageSynchroBar(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.CIRCLE_INTERFACE) {
			return new EntityImageCircleInterface(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.POINT_FOR_ASSOCIATION) {
			return new EntityImageAssociationPoint(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.GROUP) {
			return new EntityImageGroup(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.EMPTY_PACKAGE) {
			return new EntityImageEmptyPackage(ent, dotData.getSkinParam());
		}
		if (ent.getType() == EntityType.ASSOCIATION) {
			return new EntityImageAssociation(ent, dotData.getSkinParam());
		}
		throw new UnsupportedOperationException(ent.getType().toString());
	}
}
