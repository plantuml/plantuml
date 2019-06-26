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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileSwitchWithManyLinks extends FtileSwitchWithDiamonds {

	private final Rainbow arrowColor;
	private final double margin = 10;

	public FtileSwitchWithManyLinks(List<Ftile> tiles, List<Branch> branches, Swimlane in, Ftile diamond1,
			Ftile diamond2, StringBounder stringBounder, Rainbow arrowColor) {
		super(tiles, branches, in, diamond1, diamond2, stringBounder);
		this.arrowColor = arrowColor;
	}

	class ConnectionHorizontalThenVertical extends AbstractConnection {

		private final Branch branch;

		public ConnectionHorizontalThenVertical(Ftile tile, Branch branch) {
			super(diamond1, tile);
			this.branch = branch;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final Snake snake = new Snake(null, arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.setLabel(getLabelPositive(branch));
			snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final Point2D pt;
			if (getFtile2() == tiles.get(0)) {
				pt = dimDiamond1.getPointD();
			} else if (getFtile2() == tiles.get(tiles.size() - 1)) {
				pt = dimDiamond1.getPointB();
			} else {
				throw new IllegalStateException();
			}
			return getTranslateDiamond1(stringBounder).getTranslated(pt);
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateOf(getFtile2(), stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());
		}

	}

	class ConnectionVerticalThenHorizontal extends AbstractConnection {

		public ConnectionVerticalThenHorizontal(Ftile tile) {
			super(tile, diamond2);
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry geo = getFtile1().calculateDimension(stringBounder);
			if (geo.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final UPolygon arrow = x2 > x1 ? Arrows.asToRight() : Arrows.asToLeft();
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, arrow);
			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			return getTranslateOf(getFtile1(), stringBounder).getTranslated(
					getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private Point2D getP2(StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final Point2D pt;
			if (getFtile1() == tiles.get(0)) {
				pt = dimDiamond2.getPointD();
			} else if (getFtile1() == tiles.get(tiles.size() - 1)) {
				pt = dimDiamond2.getPointB();
			} else {
				throw new IllegalStateException();
			}
			return getTranslateDiamond2(stringBounder).getTranslated(pt);

		}

	}

	// protected UTranslate getTranslateOf(Ftile tile, StringBounder stringBounder) {
	// return getTranslateNude(tile, stringBounder).compose(getTranslateMain(stringBounder));
	//
	// }

	class ConnectionVerticalTop extends AbstractConnection {

		private final Branch branch;

		public ConnectionVerticalTop(Ftile tile, Branch branch) {
			super(diamond1, tile);
			this.branch = branch;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final UTranslate translateDiamond1 = getTranslateDiamond1(stringBounder);
			final Point2D p1b = translateDiamond1.getTranslated(dimDiamond1.getPointB());
			final Point2D p1c = translateDiamond1.getTranslated(dimDiamond1.getPointC());
			final Point2D p1d = translateDiamond1.getTranslated(dimDiamond1.getPointD());

			final Point2D p2 = getP2(stringBounder);
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final Snake snake = new Snake(null, arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.setLabel(getLabelPositive(branch), "BOTTOM");
			if (x2 < p1d.getX() - margin || x2 > p1b.getX() + margin) {
				snake.addPoint(x2, p1d.getY());
				snake.addPoint(x2, y2);
			} else {
				final double x1 = p1c.getX();
				final double y1 = p1c.getY();

				final double ym = (y1 * 2 + y2) / 3;
				snake.addPoint(x1, y1);
				snake.addPoint(x1, ym);
				snake.addPoint(x2, ym);
				snake.addPoint(x2, y2);
			}
			ug.draw(snake);
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateOf(getFtile2(), stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());
		}

	}

	class ConnectionVerticalBottom extends AbstractConnection {

		public ConnectionVerticalBottom(Ftile tile) {
			super(tile, diamond2);
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final UTranslate translateDiamond2 = getTranslateDiamond2(stringBounder);
			final Point2D p2a = translateDiamond2.getTranslated(dimDiamond2.getPointA());
			final Point2D p2b = translateDiamond2.getTranslated(dimDiamond2.getPointB());
			final Point2D p2d = translateDiamond2.getTranslated(dimDiamond2.getPointD());

			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final UTranslate translateDiamond1 = getTranslateDiamond1(stringBounder);
			final Point2D p1b = translateDiamond1.getTranslated(dimDiamond1.getPointB());
			final Point2D p1c = translateDiamond1.getTranslated(dimDiamond1.getPointC());
			final Point2D p1d = translateDiamond1.getTranslated(dimDiamond1.getPointD());

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2a.getX();
			final double y2 = p2a.getY();

			final double ym = (y1 + y2) / 2;

			final Snake snake = new Snake(null, arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());

			if (x1 < p1d.getX() - margin || x1 > p1b.getX() + margin) {
				snake.addPoint(x1, y1);
				snake.addPoint(x1, p2d.getY());
			} else {
				snake.addPoint(x1, y1);
				snake.addPoint(x1, ym);
				snake.addPoint(x2, ym);
				snake.addPoint(x2, y2);
			}

			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			return getTranslateOf(getFtile1(), stringBounder).getTranslated(
					getFtile1().calculateDimension(stringBounder).getPointOut());
		}

	}

	public double getYdelta1a(StringBounder stringBounder) {
		double max = 10;
		for (Branch branch : branches) {
			max = Math.max(max, getLabelPositive(branch).calculateDimension(stringBounder).getHeight());
		}
		return max + 10;
	}

	public Ftile addLinks() {
		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(new ConnectionHorizontalThenVertical(tiles.get(0), branches.get(0)));
		conns.add(new ConnectionHorizontalThenVertical(tiles.get(tiles.size() - 1), branches.get(tiles.size() - 1)));
		conns.add(new ConnectionVerticalThenHorizontal(tiles.get(0)));
		conns.add(new ConnectionVerticalThenHorizontal(tiles.get(tiles.size() - 1)));
		for (int i = 1; i < tiles.size() - 1; i++) {
			conns.add(new ConnectionVerticalTop(tiles.get(i), branches.get(i)));
			conns.add(new ConnectionVerticalBottom(tiles.get(i)));
		}

		return FtileUtils.addConnection(this, conns);
	}

}
