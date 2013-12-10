/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Snake implements UShape {

	private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();
	private final UPolygon endDecoration;
	private final HtmlColor color;
	private final boolean mergeable;
	private TextBlock textBlock;

	public Snake(HtmlColor color, UPolygon endDecoration, boolean mergeable) {
		this.endDecoration = endDecoration;
		this.color = color;
		this.mergeable = mergeable;
	}

	public void setLabel(TextBlock label) {
		this.textBlock = label;
	}

	public Snake move(double dx, double dy) {
		final Snake result = new Snake(color, endDecoration, mergeable);
		for (Point2D pt : points) {
			result.addPoint(pt.getX() + dx, pt.getY() + dy);
		}
		result.textBlock = this.textBlock;
		return result;
	}

	public Snake translate(UTranslate translate) {
		return move(translate.getDx(), translate.getDy());
	}

	@Override
	public String toString() {
		return points.toString();
	}

	SnakeDirection getDirection() {
		if (points.size() < 2) {
			throw new IllegalStateException();
		}
		return SnakeDirection.getDirection(points.get(0), points.get(1));
	}

	public Snake(HtmlColor color, UPolygon endDecoration) {
		this(color, endDecoration, false);
	}

	public Snake(HtmlColor color) {
		this(color, null, false);
	}

	public Snake(HtmlColor color, boolean mergeable) {
		this(color, null, mergeable);
	}

	public void addPoint(double x, double y) {
		if (mergeable && this.points.size() == 3) {
			final SnakeDirection direction = getDirection();
			final Point2D first = this.points.get(0);
			this.points.remove(2);
			this.points.remove(1);
			if (direction == SnakeDirection.VERTICAL_THEN_HORIZONTAL) {
				this.points.add(new Point2D.Double(first.getX(), y));
			} else {
				this.points.add(new Point2D.Double(x, first.getY()));
			}

		}
		this.points.add(new Point2D.Double(x, y));
	}

	public void addPoint(Point2D p) {
		addPoint(p.getX(), p.getY());
	}

	public void drawInternal(UGraphic ug) {
		ug = ug.apply(new UChangeColor(color));
		ug = ug.apply(new UStroke(1.5));
		for (int i = 0; i < points.size() - 1; i++) {
			final Line2D line = new Line2D.Double(points.get(i), points.get(i + 1));
			drawLine(ug, line);
		}
		if (endDecoration != null) {
			ug = ug.apply(new UChangeBackColor(color));
			final Point2D end = points.get(points.size() - 1);
			ug.apply(new UTranslate(end)).apply(new UStroke()).draw(endDecoration);
		}
		if (textBlock != null) {
			final Point2D position = getTextBlockPosition(ug.getStringBounder());
			// double max = getMaxX(ug.getStringBounder());
			// ug.apply(new UChangeBackColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UTranslate(0,
			// position.getY())).draw(new URectangle(max, 10));
			textBlock.drawU(ug.apply(new UTranslate(position)));
		}
	}

	public double getMaxX(StringBounder stringBounder) {
		double result = -Double.MAX_VALUE;
		for (Point2D pt : points) {
			result = Math.max(result, pt.getX());
		}
		if (textBlock != null) {
			final Point2D position = getTextBlockPosition(stringBounder);
			final Dimension2D dim = textBlock.calculateDimension(stringBounder);
			result = Math.max(result, position.getX() + dim.getWidth());
		}
		return result;
	}

	private Point2D getTextBlockPosition(StringBounder stringBounder) {
		final Point2D pt1 = points.get(0);
		final Point2D pt2 = points.get(1);
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		final double y = (pt1.getY() + pt2.getY()) / 2 - dim.getHeight() / 2;
		return new Point2D.Double(Math.max(pt1.getX(), pt2.getX()) + 4, y);
	}

	public List<Line2D> getHorizontalLines() {
		final List<Line2D> result = new ArrayList<Line2D>();
		for (int i = 0; i < points.size() - 1; i++) {
			final Point2D pt1 = points.get(i);
			final Point2D pt2 = points.get(i + 1);
			if (pt1.getY() == pt2.getY()) {
				final Line2D line = new Line2D.Double(pt1, pt2);
				result.add(line);
			}
		}
		return result;

	}

	private Point2D getFirst() {
		return points.get(0);
	}

	public Point2D getLast() {
		return points.get(points.size() - 1);
	}

	private boolean same(Point2D pt1, Point2D pt2) {
		return pt1.getX() == pt2.getX() && pt1.getY() == pt2.getY();
	}

	public Snake merge(Snake other) {
		if (same(this.getLast(), other.getFirst())) {
			final Snake result = new Snake(color, endDecoration, mergeable);
			result.addPoint(getFirst());
			result.addPoint(getFirst().getX(), other.getLast().getY());
			result.addPoint(other.getLast());
			result.textBlock = this.textBlock;
			return result;
		}
		if (same(this.getFirst(), other.getLast())) {
			throw new UnsupportedOperationException();
			// final Snake result = new Snake(color, endDecoration, mergeable);
			// result.addPoint(getFirst());
			// result.addPoint(getFirst().getX(), other.getLast().getY());
			// result.addPoint(other.getLast());
			// return result;
		}
		return null;
	}

	private void drawLine(UGraphic ug, Line2D line) {
		drawLine(ug, line.getX1(), line.getY1(), line.getX2(), line.getY2());
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		ug = ug.apply(new UTranslate(x1, y1));
		ug.draw(new ULine(x2 - x1, y2 - y1));
	}

	public final boolean isMergeable() {
		return mergeable;
	}

}
