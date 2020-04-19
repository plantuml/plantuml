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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PathDrawerInterface implements PathDrawer {

	private final Rose rose;
	private final ISkinParam param;
	private final LinkType linkType;

	public static PathDrawerInterface create(ISkinParam param, LinkType linkType) {
		return new PathDrawerInterface(new Rose(), param, linkType);
	}

	private PathDrawerInterface(Rose rose, ISkinParam param, LinkType linkType) {
		this.rose = rose;
		this.param = param;
		this.linkType = linkType;
	}

	public void drawPathBefore(UGraphic ug, Positionable start, Positionable end, Path path) {
		// // final DotPath dotPath = path.getDotPath();
		// // goDash(ug);
		// // ug.draw(0, 0, dotPath);
		// // noDash(ug);
	}

	private void noDash(UGraphic ug) {
		// ug.getParam().resetStroke();
		throw new UnsupportedOperationException();
	}

	private void goDash(UGraphic ug) {
		// ug.getParam().setStroke(new UStroke(8, 8, 1.0));
		throw new UnsupportedOperationException();
	}

	public void drawPathAfter(UGraphic ug, Positionable start, Positionable end, Path path) {
		DotPath dotPath = path.getDotPath();
		final Racorder racorder = new RacorderOrthogonal();
		// final Racorder racorder = new RacorderInToCenter();
		// final Racorder racorder = new RacorderFollowTangeante();

		final Point2D endPath = dotPath.getEndPoint();
		final DotPath in = racorder.getRacordIn(PositionableUtils.convert(end), dotPath.getEndTangeante());
		// final Point2D inPoint = in.getFrontierIntersection(end);
		final Point2D inPoint = in.getEndPoint();
		// final double theta1_ = in.getEndAngle() + Math.PI / 2;
		// Log.println("theta1_=" + theta1_ + " " + theta1_ * 180 /
		// Math.PI);
		final double theta1 = atan2(endPath, inPoint);
		// Log.println("theta1=" + theta1 + " " + theta1 * 180 /
		// Math.PI);
		final Point2D middle1 = drawSymbol(ug, theta1, inPoint, linkType.getDecor1());

		final Point2D startPath = dotPath.getStartPoint();
		final DotPath out = racorder.getRacordOut(PositionableUtils.convert(start), dotPath.getStartTangeante());
		// final Point2D outPoint = out.getFrontierIntersection(start);
		final Point2D outPoint = out.getStartPoint();
		// final double theta2_ = out.getStartAngle() - Math.PI / 2;
		// Log.println("theta2_=" + theta2_ + " " + theta2_ * 180 /
		// Math.PI);
		final double theta2 = atan2(startPath, outPoint);
		// Log.println("theta2=" + theta2 + " " + theta2 * 180 /
		// Math.PI);
		final Point2D middle2 = drawSymbol(ug, theta2, outPoint, linkType.getDecor2());

		if (middle1 != null) {
			final CubicCurve2D.Double after = getLine(endPath, middle1);
			dotPath = dotPath.addAfter(after);
			// dotPath = dotPath.addAfter(in);
		}

		if (middle2 != null) {
			final CubicCurve2D.Double before = getLine(middle2, startPath);
			dotPath = dotPath.addBefore(before);
			// dotPath = dotPath.addBefore(out);
		}

		final LinkStyle style = linkType.getStyle();
//		if (style == LinkStyle.__toremove_INTERFACE_PROVIDER || style == LinkStyle.__toremove_INTERFACE_USER) {
//			final Decor decor = new DecorInterfaceProvider(style);
//			final Map<Point2D, Double> all = dotPath.somePoints();
//			final Point2D p = getFarest(outPoint, inPoint, all.keySet());
//
//			ug = ug.apply(UChangeBackColor.nnn(rose.getHtmlColor(param, ColorParam.background)));
//			ug = ug.apply(UChangeColor.nnn(rose.getHtmlColor(param, ColorParam.classBorder)));
//
//			decor.drawDecor(ug, p, all.get(p));
//		}

		throw new UnsupportedOperationException();
//		ug = ug.apply(UChangeColor.nnn(rose.getHtmlColor(param, ColorParam.classBorder)));
//		if (linkType.isDashed()) {
//			goDash(ug);
//		}
//		ug.draw(dotPath);
//		if (linkType.isDashed()) {
//			noDash(ug);
//		}
	}

	private double atan2(final Point2D endPath, final Point2D inPoint) {
		final double y = -endPath.getX() + inPoint.getX();
		final double x = endPath.getY() - inPoint.getY();
		final double angle = Math.atan2(y, x);
		// Log.println("x=" + x + " y=" + y + " angle=" + angle + " " + angle * 180.0 / Math.PI);
		return angle;
	}

	private Point2D drawSymbol(UGraphic ug, double theta, final Point2D position, LinkDecor decor) {
		// if (1==1) {
		// return null;
		// }
		// Point2D middle1 = null;
		// // final double theta = Math.atan2(
		// // -direction.getX() + position.getX(), direction.getY()
		// // - position.getY());
		// if (decor == LinkDecor.SQUARRE) {
		// middle1 = drawSquare(ug, position.getX(), position.getY());
		// } else if (decor == LinkDecor.EXTENDS) {
		// middle1 = drawExtends(ug, position.getX(), position.getY(), theta);
		// } else if (decor == LinkDecor.AGREGATION) {
		// ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.background));
		// ug.getParam().setColor(rose.getHtmlColor(param, ColorParam.classBorder));
		// middle1 = drawDiamond(ug, position.getX(), position.getY(), theta);
		// } else if (decor == LinkDecor.COMPOSITION) {
		// ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.classBorder));
		// ug.getParam().setColor(null);
		// middle1 = drawDiamond(ug, position.getX(), position.getY(), theta);
		// } else if (decor == LinkDecor.NONE) {
		// middle1 = position;
		// } else if (decor == LinkDecor.ARROW) {
		// ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.classBorder));
		// ug.getParam().setColor(rose.getHtmlColor(param, ColorParam.classBorder));
		// middle1 = drawArrow(ug, position.getX(), position.getY(), theta);
		// }
		// return middle1;
		throw new UnsupportedOperationException();
	}

	private CubicCurve2D.Double getLine(final Point2D p1, Point2D p2) {
		return new CubicCurve2D.Double(p1.getX(), p1.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p2.getX(),
				p2.getY());
	}

	private static Point2D getFarest(Point2D p1, Point2D p2, Collection<Point2D> all) {
		Point2D result = null;
		double farest = 0;
		for (Point2D p : all) {
			if (result == null) {
				result = p;
				farest = p1.distanceSq(result) + p2.distanceSq(result);
				continue;
			}
			final double candidat = p1.distanceSq(p) + p2.distanceSq(p);
			if (candidat < farest) {
				result = p;
				farest = candidat;
			}
		}
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	private Point2D drawSquare(UGraphic ug, double centerX, double centerY) {
		ug = ug.apply(rose.getHtmlColor(param, ColorParam.classBackground).bg());
		ug = ug.apply(rose.getHtmlColor(param, ColorParam.classBorder));
		final double width = 10;
		final double height = 10;
		ug.apply(new UTranslate(centerX - width / 2, centerY - height / 2)).draw(new URectangle(width, height));
		return new Point2D.Double(centerX, centerY);
	}

	Point2D drawExtends(UGraphic ug, double x, double y, double theta) {
		ug = ug.apply(rose.getHtmlColor(param, ColorParam.background).bg());
		ug = ug.apply(rose.getHtmlColor(param, ColorParam.classBorder));

		// final double theta = Math.atan2(-pathPoint.getX() + x,
		// pathPoint.getY() - y);

		final UPolygon triangle = new UPolygon();
		triangle.addPoint(0, 1);
		final double width = 18;
		final double height = 26;
		triangle.addPoint(-width / 2, height);
		triangle.addPoint(width / 2, height);
		triangle.rotate(theta);
		ug.apply(new UTranslate(x, y)).draw(triangle);

		final Point2D middle = BezierUtils.middle(triangle.getPoints().get(1), triangle.getPoints().get(2));
		middle.setLocation(middle.getX() + x, middle.getY() + y);
		return middle;
	}

	private Point2D drawDiamond(UGraphic ug, double x, double y, double theta) {

		// final double theta = Math.atan2(-pathPoint.getX() + x,
		// pathPoint.getY() - y);

		final UPolygon triangle = new UPolygon();
		triangle.addPoint(0, 0);
		final double width = 10;
		final double height = 14;
		triangle.addPoint(-width / 2, height / 2);
		triangle.addPoint(0, height);
		triangle.addPoint(width / 2, height / 2);
		triangle.rotate(theta);
		ug.apply(new UTranslate(x, y)).draw(triangle);

		final Point2D middle = triangle.getPoints().get(2);
		middle.setLocation(middle.getX() + x, middle.getY() + y);
		return middle;

	}

	private Point2D drawArrow(UGraphic ug, double x, double y, double theta) {

		// final double theta = Math.atan2(-pathPoint.getX() + x,
		// pathPoint.getY() - y);

		final UPolygon triangle = new UPolygon();
		triangle.addPoint(0, 0);
		final double width = 12;
		final double height = 10;
		triangle.addPoint(-width / 2, height);
		final double height2 = 4;
		triangle.addPoint(0, height2);
		triangle.addPoint(width / 2, height);
		triangle.rotate(theta);
		ug.apply(new UTranslate(x, y)).draw(triangle);

		final Point2D middle = triangle.getPoints().get(2);
		middle.setLocation(middle.getX() + x, middle.getY() + y);
		return middle;
	}

	private Point2D nullIfContained(Point2D p, Positionable start, Positionable end) {
		if (PositionableUtils.contains(start, p)) {
			return null;
		}
		if (PositionableUtils.contains(end, p)) {
			return null;
		}
		return p;
	}

	// private void drawPath(UGraphic ug, PointList points, Positionable start,
	// Positionable end) {
	// Decor decor = new DecorInterfaceProvider();
	// Point2D last = null;
	// final int nb = 10;
	// final double t1 =
	// points.getIntersectionDouble(PositionableUtils.convert(start));
	// final double t2 =
	// points.getIntersectionDouble(PositionableUtils.convert(end));
	// for (int i = 0; i <= nb; i++) {
	// final double d = t1 + (t2 - t1) * i / nb;
	// final Point2D cur = nullIfContained(points.getPoint(d), start, end);
	// if (last != null && cur != null) {
	// ug.draw(last.getX(), last.getY(), new ULine(cur.getX() - last.getX(),
	// cur.getY() - last.getY()));
	// if (decor != null) {
	// decor.drawLine(ug, last, cur);
	// decor = null;
	// }
	// }
	// last = cur;
	// }
	//
	// for (Point2D p : points.getPoints()) {
	// ug.draw(p.getX() - 1, p.getY() - 1, new UEllipse(2, 2));
	// }
	// }

}
