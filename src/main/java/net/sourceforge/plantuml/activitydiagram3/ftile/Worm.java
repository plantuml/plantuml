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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.decoration.HtmlColorAndStyle;
import net.sourceforge.plantuml.decoration.LinkStyle;
import net.sourceforge.plantuml.klimt.Arrows;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.compress.CompressionMode;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XLine2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.Direction;

public class Worm {

	private final List<XPoint2D> points;
	private final Style style;
	private final Arrows arrows;
	private UTranslate tr;

	public Worm move(double dx, double dy) {
		final Worm result = cloneEmpty();
		if (this.tr == null)
			result.tr = new UTranslate(dx, dy);
		else
			result.tr = this.tr.compose(new UTranslate(dx, dy));

		result.points.addAll(points);
		return result;
	}

	public Worm(Style style, Arrows arrows) {
		this(style, arrows, 10);

	}

	private Worm(Style style, Arrows arrows, int initSize) {
		this.style = style;
		this.arrows = arrows;
		this.points = new ArrayList<>(Math.max(initSize, 10));
	}

	public boolean isPureHorizontal() {
		return size() == 2 && getPoint(0).getY() == getPoint(1).getY();
	}

	private boolean ignoreForCompression;

	public Worm cloneEmpty() {
		final Worm result = new Worm(style, arrows, size());
		result.ignoreForCompression = this.ignoreForCompression;
		return result;
	}

	public final void setIgnoreForCompression() {
		if (size() > 0)
			throw new IllegalStateException();

		this.ignoreForCompression = true;
	}

	public void drawInternalOneColor(UPolygon startDecoration, UGraphic ug, HtmlColorAndStyle colorAndStyle,
			double strokeValue, Direction emphasizeDirection, UPolygon endDecoration) {
		final HColor arrowColor = Objects.requireNonNull(colorAndStyle.getArrowColor());
		final LinkStyle linkStyle = colorAndStyle.getStyle();
		if (linkStyle.isInvisible())
			return;

		ug = ug.apply(arrowColor);
		ug = ug.apply(arrowColor.bg());
		if (linkStyle.isNormal())
			ug = ug.apply(style.getStroke());
		else
			ug = ug.apply(linkStyle.goThickness(strokeValue).getStroke3());

		boolean drawn = false;
		for (int i = 0; i < size() - 1; i++) {
			final XPoint2D p1 = getPoint(i);
			final XPoint2D p2 = getPoint(i + 1);
			final XLine2D line = XLine2D.line(p1, p2);
			if (drawn == false && emphasizeDirection != null && Direction.fromVector(p1, p2) == emphasizeDirection) {
				drawLine(ug, line, emphasizeDirection);
				drawn = true;
			} else {
				drawLine(ug, line, null);
			}
		}

		final HColor arrowHeadColor = colorAndStyle.getArrowHeadColor();
		if (arrowHeadColor == null)
			throw new IllegalStateException();

//		if (arrowHeadColor == null || arrowHeadColor.equals(HColorUtils.transparent())) {
//			return;
//		}
		ug = ug.apply(arrowHeadColor);
		ug = ug.apply(arrowHeadColor.bg());

		if (startDecoration != null) {
			ug = ug.apply(UStroke.withThickness(1.5));
			final XPoint2D start = getPoint(0);
			if (ignoreForCompression)
				startDecoration.setCompressionMode(CompressionMode.ON_X);

			ug.apply(UTranslate.point(start)).apply(UStroke.simple()).draw(startDecoration);
		}
		if (endDecoration != null) {
			ug = ug.apply(UStroke.withThickness(1.5));
			final XPoint2D end = getPoint(size() - 1);
			if (ignoreForCompression)
				endDecoration.setCompressionMode(CompressionMode.ON_X);

			ug.apply(UTranslate.point(end)).apply(UStroke.simple()).draw(endDecoration);
		}
	}

	private void drawLine(UGraphic ug, XLine2D line, Direction direction) {
		drawLine(ug, line.getX1(), line.getY1(), line.getX2(), line.getY2(), direction);
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2, Direction direction) {
		ug = ug.apply(new UTranslate(x1, y1));
		if (direction != null)
			ug.apply(new UTranslate((x2 - x1) / 2, (y2 - y1) / 2)).draw(arrows.asTo(direction));

		ug.draw(new ULine(x2 - x1, y2 - y1));
	}

	public Worm moveFirstPoint(UTranslate move) {
		final double dx = move.getDx();
		final double dy = move.getDy();
		if (dx != 0 && dy != 0)
			throw new IllegalArgumentException("move=" + move);

		final Worm result = new Worm(style, arrows, size());
		double x0 = getPoint(0).getX();
		double y0 = getPoint(0).getY();
		double x1 = getPoint(1).getX();
		double y1 = getPoint(1).getY();

		if (dx != 0 && x0 == x1)
			x1 += dx;

		if (dy != 0 && y0 == y1)
			y1 += dy;

		x0 += dx;
		y0 += dy;

		result.addPoint(x0, y0);
		result.addPoint(x1, y1);
		for (int i = 2; i < size(); i++)
			result.addPoint(getPoint(i));

		return result;
	}

	public Worm moveLastPoint(UTranslate move) {
		final double dx = move.getDx();
		final double dy = move.getDy();
		if (dx != 0 && dy != 0)
			throw new IllegalArgumentException("move=" + move);

		final Worm result = new Worm(style, arrows, size());
		double x8 = getPoint(size() - 2).getX();
		double y8 = getPoint(size() - 2).getY();
		double x9 = getPoint(size() - 1).getX();
		double y9 = getPoint(size() - 1).getY();

		if (dx != 0 && x8 == x9)
			x8 += dx;

		if (dy != 0 && y8 == y9)
			y8 += dy;

		x9 += dx;
		y9 += dy;

		for (int i = 0; i < size() - 2; i++)
			result.addPoint(getPoint(i));

		result.addPoint(x8, y8);
		result.addPoint(x9, y9);
		return result;
	}

//	@Override
//	public String toString() {
//		final StringBuilder result = new StringBuilder();
//		for (int i = 0; i < size() - 1; i++)
//			result.append(getDirectionAtPoint(i)).append(" ");
//
//		return result + points.toString();
//	}

	public void addPoint(double x, double y) {
		if (tr != null) {
			x -= tr.getDx();
			y -= tr.getDy();
		}
		if (Double.isNaN(x))
			throw new IllegalArgumentException();

		if (Double.isNaN(y))
			throw new IllegalArgumentException();

		if (size() > 0) {
			final XPoint2D last = getLast();
			if (last.getX() == x && last.getY() == y)
				return;
		}

		this.points.add(new XPoint2D(x, y));
	}

	public void addPoint(XPoint2D pt) {
		this.addPoint(pt.getX(), pt.getY());
	}

	SnakeDirection getDirection() {
		if (size() < 2)
			throw new IllegalStateException();

		return SnakeDirection.getDirection(getPoint(0), getPoint(1));
	}

	String getDirectionsCode() {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < size() - 1; i++) {
			final Direction direction = Direction.fromVector(getPoint(i), getPoint(i + 1));
			result.append(direction.getShortCode());
		}
		return result.toString();
	}

	private List<Direction> getPatternAt(int i) {
		return Arrays.asList(getDirectionAtPoint(i), getDirectionAtPoint(i + 1), getDirectionAtPoint(i + 2),
				getDirectionAtPoint(i + 3));
	}

	private boolean isForwardAndBackwardAt(int i) {
		return getDirectionAtPoint(i) == getDirectionAtPoint(i + 1).getInv();
	}

	private Direction getDirectionAtPoint(int i) {
		return Direction.fromVector(getPoint(i), getPoint(i + 1));
	}

	public boolean doesHorizontalCross(MinMax area) {
		for (int i = 0; i < size() - 1; i++) {
			final XPoint2D pt1 = getPoint(i);
			final XPoint2D pt2 = getPoint(i + 1);
			if (pt1.getY() == pt2.getY() && area.doesHorizontalCross(pt1, pt2))
				return true;

		}
		return false;
	}

	public int size() {
		return points.size();
	}

	public XPoint2D getPoint(int i) {
		return resolve(points.get(i));
	}

	private XPoint2D resolve(XPoint2D point) {
		if (tr == null)
			return point;
		return tr.getTranslated(point);
	}

	public XPoint2D getFirst() {
		return getPoint(0);
	}

	public XPoint2D getLast() {
		return getPoint(size() - 1);
	}

	public double getMinX() {
		double result = getPoint(0).getX();
		for (XPoint2D pt : points)
			result = Math.min(result, resolve(pt).getX());
		return result;
	}

	public double getMaxX() {
		double result = getPoint(0).getX();
		for (XPoint2D pt : points)
			result = Math.max(result, resolve(pt).getX());
		return result;
	}

	public double getMaxY() {
		double result = getPoint(0).getY();
		for (XPoint2D pt : points)
			result = Math.max(result, resolve(pt).getY());
		return result;
	}

	public Worm merge(Worm other, MergeStrategy merge) {
		if (Snake.same(this.getLast(), other.getFirst()) == false)
			throw new IllegalArgumentException();

		final Worm result = new Worm(style, arrows, size() + other.size());
		for (XPoint2D pt : this.points)
			result.addPoint(this.resolve(pt));
		for (XPoint2D pt : other.points)
			result.addPoint(other.resolve(pt));
		result.mergeMe(merge);
		return result;
	}

	private void mergeMe(MergeStrategy merge) {
		if (tr != null)
			throw new UnsupportedOperationException();
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
			if (merge == MergeStrategy.FULL)
				change = change || removePattern8();

		} while (change);
	}

	private boolean removeNullVector() {
		for (int i = 0; i < size() - 1; i++) {
			final Direction dir = getDirectionAtPoint(i);
			if (dir == null) {
				points.remove(i);
				return true;
			}
		}
		return false;
	}

	private boolean removeRedondantDirection() {
		for (int i = 0; i < size() - 2; i++) {
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
		for (int i = 0; i < size() - 5; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT).equals(patternAt)
					|| Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
							.equals(patternAt)) {
				final XPoint2D newPoint = new XPoint2D(getPoint(i + 1).x, getPoint(i + 3).y);
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
		if (size() > 4) {
			final int i = 0;
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.DOWN).equals(patternAt)
					&& getPoint(i + 3).x > getPoint(i).x) {
				final XPoint2D newPoint = new XPoint2D(getPoint(i + 3).x, getPoint(i).y);
				points.remove(i + 2);
				points.remove(i + 1);
				points.add(i + 1, newPoint);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern2() {
		for (int i = 0; i < size() - 5; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.UP).equals(patternAt)
					|| Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.UP).equals(patternAt)) {
				final XPoint2D newPoint = new XPoint2D(getPoint(i + 3).x, getPoint(i + 1).y);
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
		for (int i = 0; i < size() - 4; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.RIGHT).equals(patternAt)
					|| Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.LEFT)
							.equals(patternAt)) {
				final XPoint2D newPoint = new XPoint2D(getPoint(i + 1).x, getPoint(i + 3).y);
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
		final int i = size() - 5;
		if (i >= 0) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT).equals(patternAt)) {
				final XPoint2D p1 = getPoint(i + 1);
				final XPoint2D p4 = getPoint(i + 4);
				if (p4.x > p1.x) {
					final XPoint2D newPoint = new XPoint2D(getPoint(i + 1).x, getPoint(i + 3).y);
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
		final int i = size() - 5;
		if (i >= 0) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT).equals(patternAt)) {
				final XPoint2D p1 = getPoint(i + 1);
				final XPoint2D p4 = getPoint(i + 4);
				if (p4.x + 4 < p1.x) {
					final XPoint2D newPoint = new XPoint2D(getPoint(i + 1).x, getPoint(i + 3).y);
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
		for (int i = 0; i < size() - 2; i++) {
			if (isForwardAndBackwardAt(i)) {
				points.remove(i + 1);
				return true;
			}
		}
		return false;
	}

	private boolean removePattern8() {
		for (int i = 0; i < size() - 4; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.DOWN).equals(patternAt)
					|| Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN)
							.equals(patternAt)) {
				final XPoint2D newPoint = new XPoint2D(getPoint(i + 3).x, getPoint(i + 1).y);
				points.remove(i + 3);
				points.remove(i + 2);
				points.remove(i + 1);
				points.add(i + 1, newPoint);
				return true;
			}
		}
		return false;
	}

	public Style getStyle() {
		return style;
	}

}
