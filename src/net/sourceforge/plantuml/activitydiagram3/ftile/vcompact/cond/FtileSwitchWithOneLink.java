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

public class FtileSwitchWithOneLink extends FtileSwitchWithDiamonds {

	private final Rainbow arrowColor;

	public FtileSwitchWithOneLink(List<Ftile> tiles, List<Branch> branches, Swimlane in, Ftile diamond1,
			Ftile diamond2, StringBounder stringBounder, Rainbow arrowColor) {
		super(tiles, branches, in, diamond1, diamond2, stringBounder);
		this.arrowColor = arrowColor;
	}

	class ConnectionVerticalTop extends AbstractConnection {

		private final Branch branch;

		public ConnectionVerticalTop(Ftile tile, Branch branch) {
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
			// snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			return getTranslateDiamond1(stringBounder).getTranslated(dimDiamond1.getPointC());
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
			final Point2D p2 = getP2(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final Snake snake = new Snake(null, arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			// snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			return getTranslateOf(getFtile1(), stringBounder).getTranslated(
					getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private Point2D getP2(StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			return getTranslateDiamond2(stringBounder).getTranslated(dimDiamond2.getPointA());
		}
	}

	public Ftile addLinks() {
		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(new ConnectionVerticalTop(tiles.get(0), branches.get(0)));
		conns.add(new ConnectionVerticalBottom(tiles.get(0)));

		return FtileUtils.addConnection(this, conns);
	}

}
