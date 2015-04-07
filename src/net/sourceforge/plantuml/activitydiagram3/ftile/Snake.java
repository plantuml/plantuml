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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.CompressionTransform;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Snake implements UShape {

	private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();
	private final UPolygon endDecoration;
	private final HtmlColor color;
	private TextBlock textBlock;
	private boolean mergeable = true;
	private Direction emphasizeDirection;

	public Snake transformX(CompressionTransform compressionTransform) {
		final Snake result = new Snake(color, endDecoration);
		result.textBlock = this.textBlock;
		result.mergeable = this.mergeable;
		result.emphasizeDirection = this.emphasizeDirection;
		for (Point2D.Double pt : points) {
			final double x = compressionTransform.transform(pt.x);
			final double y = pt.y;
			result.addPoint(x, y);
		}
		return result;
	}

	public Snake(HtmlColor color, UPolygon endDecoration) {
		this.endDecoration = endDecoration;
		this.color = color;
	}

	public Snake(HtmlColor color) {
		this(color, null);
	}

	public void setLabel(TextBlock label) {
		this.textBlock = label;
	}

	public Snake move(double dx, double dy) {
		final Snake result = new Snake(color, endDecoration);
		for (Point2D pt : points) {
			result.addPoint(pt.getX() + dx, pt.getY() + dy);
		}
		result.textBlock = this.textBlock;
		result.mergeable = this.mergeable;
		result.emphasizeDirection = this.emphasizeDirection;
		return result;
	}

	public Snake translate(UTranslate translate) {
		return move(translate.getDx(), translate.getDy());
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < points.size() - 1; i++) {
			result.append(getDirectionAtPoint(i) + " ");
		}
		return result + points.toString();
	}

	SnakeDirection getDirection() {
		if (points.size() < 2) {
			throw new IllegalStateException();
		}
		return SnakeDirection.getDirection(points.get(0), points.get(1));
	}

	public void addPoint(double x, double y) {
		this.points.add(new Point2D.Double(x, y));
	}

	public void addPoint(Point2D p) {
		addPoint(p.getX(), p.getY());
	}

	public void drawInternal(UGraphic ug) {
		ug = ug.apply(new UChangeColor(color));
		ug = ug.apply(new UChangeBackColor(color));
		ug = ug.apply(new UStroke(1.5));
		boolean drawn = false;
		for (int i = 0; i < points.size() - 1; i++) {
			final java.awt.geom.Point2D.Double p1 = points.get(i);
			final java.awt.geom.Point2D.Double p2 = points.get(i + 1);
			final Line2D line = new Line2D.Double(p1, p2);
			if (drawn == false && emphasizeDirection != null && Direction.fromVector(p1, p2) == emphasizeDirection) {
				drawLine(ug, line, emphasizeDirection);
				drawn = true;
			} else {
				drawLine(ug, line, null);
			}
		}
		if (endDecoration != null) {
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
		return pt1.distance(pt2) < 0.001;
		// return pt1.getX() == pt2.getX() && pt1.getY() == pt2.getY();
	}

	public Snake merge(Snake other) {
		if (mergeable == false || other.mergeable == false) {
			return null;
		}
		if (same(this.getLast(), other.getFirst())) {
			final UPolygon oneOf = endDecoration == null ? other.endDecoration : endDecoration;
			final Snake result = new Snake(color, oneOf);
			result.textBlock = textBlock == null ? other.textBlock : textBlock;
			result.emphasizeDirection = emphasizeDirection == null ? other.emphasizeDirection : emphasizeDirection;
			result.points.addAll(this.points);
			result.points.addAll(other.points);
			result.mergeMe();
			return result;
		}
		if (same(this.getFirst(), other.getLast())) {
			return other.merge(this);
		}
		return null;
	}

	private void mergeMe() {
		boolean change = false;
		do {
			change = false;
			change = change || removeNullVector();
			change = change || removeRedondantDirection();
			change = change || removePattern1();
			change = change || removePattern2();
			change = change || removePattern3();
			change = change || removePattern4();
			change = change || removePattern5();
			change = change || removePattern6();
			change = change || removePattern7();
		} while (change);
	}

	private boolean removeNullVector() {
		for (int i = 0; i < points.size() - 1; i++) {
			final Direction dir = getDirectionAtPoint(i);
			if (dir == null) {
				points.remove(i);
				return true;
			}
		}
		return false;
	}

	private boolean removeRedondantDirection() {
		for (int i = 0; i < points.size() - 2; i++) {
			final Direction dir1 = getDirectionAtPoint(i);
			final Direction dir2 = getDirectionAtPoint(i + 1);
			if (dir1 == dir2) {
				points.remove(i + 1);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern1() {
		for (int i = 0; i < points.size() - 5; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT).equals(patternAt)
					|| Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT).equals(patternAt)) {
				final Point2D.Double newPoint = new Point2D.Double(points.get(i + 1).x, points.get(i + 3).y);
				points.remove(i + 3);
				points.remove(i + 2);
				points.remove(i + 1);
				points.add(i + 1, newPoint);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern7() {
		if (points.size() > 4) {
			final int i = 0;
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.DOWN).equals(patternAt)
					&& points.get(i + 3).x > points.get(i).x) {
				final Point2D.Double newPoint = new Point2D.Double(points.get(i + 3).x, points.get(i).y);
				points.remove(i + 2);
				points.remove(i + 1);
				points.add(i + 1, newPoint);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern2() {
		for (int i = 0; i < points.size() - 5; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.UP).equals(patternAt)
					|| Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.UP).equals(patternAt)) {
				final Point2D.Double newPoint = new Point2D.Double(points.get(i + 3).x, points.get(i + 1).y);
				points.remove(i + 3);
				points.remove(i + 2);
				points.remove(i + 1);
				points.add(i + 1, newPoint);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern3() {
		for (int i = 0; i < points.size() - 4; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.RIGHT).equals(patternAt)
					|| Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.LEFT).equals(patternAt)) {
				final Point2D.Double newPoint = new Point2D.Double(points.get(i + 1).x, points.get(i + 3).y);
				points.remove(i + 3);
				points.remove(i + 2);
				points.remove(i + 1);
				points.add(i + 1, newPoint);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern4() {
		final int i = points.size() - 5;
		if (i >= 0) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT).equals(patternAt)) {
				final Point2D.Double p1 = points.get(i + 1);
				final Point2D.Double p4 = points.get(i + 4);
				if (p4.x > p1.x) {
					final Point2D.Double newPoint = new Point2D.Double(points.get(i + 1).x, points.get(i + 3).y);
					points.remove(i + 3);
					points.remove(i + 2);
					points.remove(i + 1);
					points.add(i + 1, newPoint);
					return true;
				}
			}
		}
		return false;
	}

	private boolean removePattern5() {
		final int i = points.size() - 5;
		if (i >= 0) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT).equals(patternAt)) {
				final Point2D.Double p1 = points.get(i + 1);
				final Point2D.Double p4 = points.get(i + 4);
				if (p4.x < p1.x) {
					final Point2D.Double newPoint = new Point2D.Double(points.get(i + 1).x, points.get(i + 3).y);
					points.remove(i + 3);
					points.remove(i + 2);
					points.remove(i + 1);
					points.add(i + 1, newPoint);
					return true;
				}
			}
		}
		return false;
	}

	private boolean removePattern6() {
		for (int i = 0; i < points.size() - 2; i++) {
			if (isForwardAndBackwardAt(i)) {
				points.remove(i + 1);
				return true;
			}
		}
		return false;
	}

	private List<Direction> getPatternAt(int i) {
		return Arrays.asList(getDirectionAtPoint(i), getDirectionAtPoint(i + 1), getDirectionAtPoint(i + 2),
				getDirectionAtPoint(i + 3));
	}

	private boolean isForwardAndBackwardAt(int i) {
		return getDirectionAtPoint(i) == getDirectionAtPoint(i + 1).getInv();
	}

	private Direction getDirectionAtPoint(int i) {
		return Direction.fromVector(points.get(i), points.get(i + 1));
	}

	private void drawLine(UGraphic ug, Line2D line, Direction direction) {
		drawLine(ug, line.getX1(), line.getY1(), line.getX2(), line.getY2(), direction);
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2, Direction direction) {
		ug = ug.apply(new UTranslate(x1, y1));
		if (direction != null) {
			ug.apply(new UTranslate((x2 - x1) / 2, (y2 - y1) / 2)).draw(Arrows.asTo(direction));
		}
		ug.draw(new ULine(x2 - x1, y2 - y1));
	}

	public void goUnmergeable() {
		this.mergeable = false;
	}

	public void emphasizeDirection(Direction direction) {
		this.emphasizeDirection = direction;
	}

}
