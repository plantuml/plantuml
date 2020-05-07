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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.graphic.HtmlColorAndStyle;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.comp.CompressionMode;

public class Worm implements Iterable<Point2D.Double> {

	private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();

	public boolean isPureHorizontal() {
		return points.size() == 2 && points.get(0).getY() == points.get(1).getY();
	}

	private boolean ignoreForCompression;

	public final void setIgnoreForCompression() {
		this.ignoreForCompression = true;
	}

	public void drawInternalOneColor(UPolygon startDecoration, UGraphic ug, HtmlColorAndStyle colorAndStyle,
			double stroke, Direction emphasizeDirection, UPolygon endDecoration) {
		final HColor arrowColor = colorAndStyle.getArrowColor();
		if (arrowColor == null) {
			throw new IllegalArgumentException();
		}
		final LinkStyle style = colorAndStyle.getStyle();
		if (style.isInvisible()) {
			return;
		}
		ug = ug.apply(arrowColor);
		ug = ug.apply(arrowColor.bg());
		if (style.isNormal()) {
			ug = ug.apply(new UStroke(stroke));
		} else {
			ug = ug.apply(style.goThickness(stroke).getStroke3());
		}
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

		final HColor arrowHeadColor = colorAndStyle.getArrowHeadColor();
		if (arrowHeadColor == null) {
			throw new IllegalStateException();
		}
//		if (arrowHeadColor == null || arrowHeadColor.equals(HColorUtils.transparent())) {
//			return;
//		}
		ug = ug.apply(arrowHeadColor);
		ug = ug.apply(arrowHeadColor.bg());

		if (startDecoration != null) {
			ug = ug.apply(new UStroke(1.5));
			final Point2D start = points.get(0);
			if (ignoreForCompression) {
				startDecoration.setCompressionMode(CompressionMode.ON_X);
			}
			ug.apply(new UTranslate(start)).apply(new UStroke()).draw(startDecoration);
		}
		if (endDecoration != null) {
			ug = ug.apply(new UStroke(1.5));
			final Point2D end = points.get(points.size() - 1);
			if (ignoreForCompression) {
				endDecoration.setCompressionMode(CompressionMode.ON_X);
			}
			ug.apply(new UTranslate(end)).apply(new UStroke()).draw(endDecoration);
		}
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

	public Worm move(double dx, double dy) {
		final Worm result = new Worm();
		for (Point2D pt : points) {
			result.addPoint(pt.getX() + dx, pt.getY() + dy);
		}
		return result;
	}

	public Worm moveFirstPoint(UTranslate move) {
		final double dx = move.getDx();
		final double dy = move.getDy();
		if (dx != 0 && dy != 0) {
			throw new IllegalArgumentException("move=" + move);
		}
		final Worm result = new Worm();
		double x0 = this.points.get(0).getX();
		double y0 = this.points.get(0).getY();
		double x1 = this.points.get(1).getX();
		double y1 = this.points.get(1).getY();

		if (dx != 0 && x0 == x1) {
			x1 += dx;
		}
		if (dy != 0 && y0 == y1) {
			y1 += dy;
		}
		x0 += dx;
		y0 += dy;

		result.addPoint(x0, y0);
		result.addPoint(x1, y1);
		for (int i = 2; i < this.points.size(); i++) {
			result.addPoint(this.points.get(i));
		}
		return result;
	}

	public Worm moveLastPoint(UTranslate move) {
		final double dx = move.getDx();
		final double dy = move.getDy();
		if (dx != 0 && dy != 0) {
			throw new IllegalArgumentException("move=" + move);
		}
		final Worm result = new Worm();
		double x8 = this.points.get(this.points.size() - 2).getX();
		double y8 = this.points.get(this.points.size() - 2).getY();
		double x9 = this.points.get(this.points.size() - 1).getX();
		double y9 = this.points.get(this.points.size() - 1).getY();

		if (dx != 0 && x8 == x9) {
			x8 += dx;
		}
		if (dy != 0 && y8 == y9) {
			y8 += dy;
		}
		x9 += dx;
		y9 += dy;

		for (int i = 0; i < this.points.size() - 2; i++) {
			result.addPoint(this.points.get(i));
		}
		result.addPoint(x8, y8);
		result.addPoint(x9, y9);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < points.size() - 1; i++) {
			result.append(getDirectionAtPoint(i) + " ");
		}
		return result + points.toString();
	}

	public void addPoint(double x, double y) {
		if (Double.isNaN(x)) {
			throw new IllegalArgumentException();
		}
		if (Double.isNaN(y)) {
			throw new IllegalArgumentException();
		}
		if (points.size() > 0) {
			final Point2D last = getLast();
			if (last.getX() == x && last.getY() == y) {
				return;
			}
		}
		this.points.add(new Point2D.Double(x, y));
	}

	public void addPoint(Point2D pt) {
		this.addPoint(pt.getX(), pt.getY());
	}

	public Worm translate(UTranslate translate) {
		return move(translate.getDx(), translate.getDy());
	}

	SnakeDirection getDirection() {
		if (points.size() < 2) {
			throw new IllegalStateException();
		}
		return SnakeDirection.getDirection(points.get(0), points.get(1));
	}

	String getDirectionsCode() {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < points.size() - 1; i++) {
			final Direction direction = Direction.fromVector(points.get(i), points.get(i + 1));
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
		return Direction.fromVector(points.get(i), points.get(i + 1));
	}

	public Iterator<Point2D.Double> iterator() {
		return Collections.unmodifiableCollection(points).iterator();
	}

	public boolean doesHorizontalCross(MinMax area) {
		for (int i = 0; i < points.size() - 1; i++) {
			final Point2D.Double pt1 = get(i);
			final Point2D.Double pt2 = get(i + 1);
			if (pt1.getY() == pt2.getY() && area.doesHorizontalCross(pt1, pt2)) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return this.points.size();
	}

	public Point2D.Double get(int i) {
		return this.points.get(i);
	}

	public void addAll(Worm other) {
		this.points.addAll(other.points);
	}

	public void remove(int i) {
		this.points.remove(i);
	}

	public void add(int i, Point2D.Double pt) {
		this.points.add(i, pt);
	}

	private Point2D getFirst() {
		return points.get(0);
	}

	public Point2D getLast() {
		return points.get(points.size() - 1);
	}

	public Worm merge(Worm other, MergeStrategy merge) {
		if (Snake.same(this.getLast(), other.getFirst()) == false) {
			throw new IllegalArgumentException();
		}
		final Worm result = new Worm();
		result.points.addAll(this.points);
		result.points.addAll(other.points);
		result.mergeMe(merge);
		return result;
	}

	private void mergeMe(MergeStrategy merge) {
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
			if (merge == MergeStrategy.FULL) {
				change = change || removePattern8();
			}
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
					|| Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
							.equals(patternAt)) {
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
					|| Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.LEFT)
							.equals(patternAt)) {
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
				if (p4.x + 4 < p1.x) {
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

	private boolean removePattern8() {
		for (int i = 0; i < points.size() - 4; i++) {
			final List<Direction> patternAt = getPatternAt(i);
			if (Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.DOWN).equals(patternAt)
					|| Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN)
							.equals(patternAt)) {
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

}
