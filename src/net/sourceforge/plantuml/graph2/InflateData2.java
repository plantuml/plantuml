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
package net.sourceforge.plantuml.graph2;

public class InflateData2 implements Comparable<InflateData2> {

	private final double pos;
	private final double inflation;

	public InflateData2(double pos, double inflation) {
		this.pos = pos;
		this.inflation = inflation;
	}

	public final double getPos() {
		return pos;
	}

	public final double getInflation() {
		return inflation;
	}

	public int compareTo(InflateData2 other) {
		return -Double.compare(this.pos, other.pos);
	}

	// public Point2D inflateX(Point2D pt) {
	// if (pt.getX() < pos) {
	// return pt;
	// }
	// if (pt.getX() == pos) {
	// return GeomUtils.translate(pt, inflation / 2, 0);
	// }
	// return GeomUtils.translate(pt, inflation, 0);
	// }
	//
	public double inflateAt(double v) {
		if (v == pos) {
			return inflation / 2;
		}

		if (v < pos) {
			return 0;
		}
		return inflation;
	}

	// public Line2D.Double inflateXAlpha(Line2D.Double line) {
	//
	// if (GeomUtils.isHorizontal(line)) {
	// return new Line2D.Double(inflateX(line.getP1()), inflateX(line.getP2()));
	// }
	// if (line.x1 == pos && line.x2 == pos) {
	// return new Line2D.Double(GeomUtils.translate(line.getP1(), inflation / 2,
	// 0), GeomUtils.translate(line
	// .getP2(), inflation / 2, 0));
	// }
	// if (line.x1 <= pos && line.x2 <= pos) {
	// return line;
	// }
	// if (line.x1 >= pos && line.x2 >= pos) {
	// return new Line2D.Double(GeomUtils.translate(line.getP1(), inflation, 0),
	// GeomUtils.translate(line.getP2(),
	// inflation, 0));
	// }
	// throw new UnsupportedOperationException();
	// }
	//
	// public Line2D.Double inflateYAlpha(Line2D.Double line) {
	// if (GeomUtils.isVertical(line)) {
	// return new Line2D.Double(inflateY(line.getP1()), inflateY(line.getP2()));
	// }
	// if (line.y1 == pos && line.y2 == pos) {
	// return new Line2D.Double(GeomUtils.translate(line.getP1(), 0, inflation /
	// 2), GeomUtils.translate(line
	// .getP2(), 0, inflation / 2));
	// }
	// if (line.y1 <= pos && line.y2 <= pos) {
	// return line;
	// }
	// if (line.y1 >= pos && line.y2 >= pos) {
	// return new Line2D.Double(GeomUtils.translate(line.getP1(), 0, inflation),
	// GeomUtils.translate(line.getP2(),
	// 0, inflation));
	// }
	// throw new UnsupportedOperationException();
	// }

	@Override
	public String toString() {
		return "" + pos + " (" + inflation + ")";
	}
}
