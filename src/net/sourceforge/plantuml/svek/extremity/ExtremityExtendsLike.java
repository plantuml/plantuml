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
 * Creator:  Hisashi Miyashita
 *
 * 
 */
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UBackground;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

abstract class ExtremityExtendsLike extends Extremity {
	private static final double XLEN = -19;// 8 * 2.4;
	private static final double HALF_WIDTH = 7;// 3 * 2.4;

	private final UPolygon trig;
	private final UBackground back;
	private final Point2D contact;

	@Override
	public Point2D somePoint() {
		return contact;
	}

	private static class Point {
		public double x;
		public double y;

		public void rotate(double theta) {
			double ct = Math.cos(theta);
			double st = -Math.sin(theta);
			double nx = x * ct - y * st;
			y = -x * st - y * ct;
			x = nx;
		}

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public UTranslate getPos(Point2D pt) {
			return new UTranslate(x + pt.getX(), y + pt.getY());
		}

		public void translate(Point2D pt) {
			x += pt.getX();
			y += pt.getY();
		}

		public void add(UPolygon p) {
			p.addPoint(x, y);
		}
	}

	static class Redefines extends ExtremityExtendsLike {
		private static final double XSUFFIX = XLEN * 1.2;
		private final UStroke barStroke = new UStroke(2.0);
		private final UTranslate pos;
		private final ULine bar;

		public Redefines(Point2D porig, double angle, HColor backgroundColor) {
			super(porig, angle, backgroundColor);

			Point p1 = new Point(XSUFFIX, -HALF_WIDTH);
			Point p2 = new Point(XSUFFIX, +HALF_WIDTH);
			p1.rotate(angle);
			p2.rotate(angle);
			this.bar = new ULine(p2.x - p1.x, p2.y - p1.y);
			this.pos = p1.getPos(porig);
		}

		public void drawU(UGraphic ug) {
			super.drawU(ug);
			ug.apply(barStroke).apply(pos).draw(bar);
		}
	}

	static class DefinedBy extends ExtremityExtendsLike {
		private static final double XSUFFIX = XLEN * 1.3;
		private static final double DOTHSIZE = 2;
		private final UTranslate pos1, pos2;
		private final UEllipse dot;

		private static UTranslate getDotPos(double x, double y, double angle, double size, Point2D porig) {
			Point p = new Point(x, y);
			p.rotate(angle);
			p.x -= size;
			p.y -= size;
			return p.getPos(porig);
		}

		public DefinedBy(Point2D porig, double angle, HColor backgroundColor) {
			super(porig, angle, backgroundColor);
			double w = HALF_WIDTH - DOTHSIZE;

			this.pos1 = getDotPos(XSUFFIX, -w, angle, DOTHSIZE, porig);
			this.pos2 = getDotPos(XSUFFIX, +w, angle, DOTHSIZE, porig);

			double s = DOTHSIZE + DOTHSIZE;
			this.dot = new UEllipse(s, s);
		}

		public void drawU(UGraphic ug) {
			super.drawU(ug);
			if (ug.getParam().getColor() != null) {
				ug = ug.apply(ug.getParam().getColor().bg());
			}
			ug.apply(pos1).draw(dot);
			ug.apply(pos2).draw(dot);
		}
	}

	private static void addTrigPoint(UPolygon up, double x, double y, double angle, Point2D porig) {
		Point p = new Point(x, y);
		p.rotate(angle);
		p.translate(porig);
		p.add(up);
	}

	private ExtremityExtendsLike(Point2D porig, double angle, HColor backgroundColor) {
		this.back = backgroundColor.bg();
		this.contact = new Point2D.Double(porig.getX(), porig.getY());
		angle = manageround(angle);

		this.trig = new UPolygon();
		trig.addPoint(porig);
		addTrigPoint(trig, XLEN, -HALF_WIDTH, angle, porig);
		addTrigPoint(trig, XLEN, +HALF_WIDTH, angle, porig);
		trig.addPoint(porig);
	}

	public void drawU(UGraphic ug) {
		ug.apply(back).draw(trig);
	}

}