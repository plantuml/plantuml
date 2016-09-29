/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileIfDown extends AbstractFtile {

	private final Ftile thenBlock;
	private final Ftile diamond1;
	private final Ftile diamond2;
	private final Ftile optionalStop;

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>(thenBlock.getSwimlanes());
		result.add(getSwimlaneIn());
		return result;
	}

	public Swimlane getSwimlaneIn() {
		return diamond1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		if (optionalStop == null) {
			return getSwimlaneIn();
		}
		return thenBlock.getSwimlaneOut();
	}

	private FtileIfDown(Ftile thenBlock, Ftile diamond1, Ftile diamond2, Ftile optionalStop) {
		super(thenBlock.skinParam());
		this.thenBlock = thenBlock;
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
		this.optionalStop = optionalStop;
	}

	public static Ftile create(Ftile diamond1, Ftile diamond2, Swimlane swimlane, Ftile thenBlock, Rainbow arrowColor,
			FtileFactory ftileFactory, Ftile optionalStop, Rainbow elseColor) {

		elseColor = elseColor.withDefault(arrowColor);
		final FtileIfDown result = new FtileIfDown(thenBlock, diamond1, optionalStop == null ? diamond2
				: new FtileEmpty(ftileFactory.skinParam()), optionalStop);

		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionIn(thenBlock.getInLinkRendering().getRainbow(arrowColor)));
		final boolean hasPointOut1 = thenBlock.calculateDimension(ftileFactory.getStringBounder()).hasPointOut();
		if (optionalStop == null) {
			if (hasPointOut1) {
				conns.add(result.new ConnectionElse(elseColor));
			} else {
				conns.add(result.new ConnectionElseNoDiamond(elseColor));
			}
		}
		conns.add(result.new ConnectionOut(thenBlock.getOutLinkRendering().getRainbow(arrowColor)));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionIn(Rainbow arrowColor) {
			super(diamond1, thenBlock);
			this.arrowColor = arrowColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(
					getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateForThen(stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}
	}

	class ConnectionOut extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionOut(Rainbow arrowColor) {
			super(thenBlock, diamond2);
			this.arrowColor = arrowColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateForThen(stringBounder).getTranslated(
					getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			if (getFtile1().calculateDimension(ug.getStringBounder()).hasPointOut() == false) {
				return;
			}

			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {

			if (getFtile1().calculateDimension(ug.getStringBounder()).hasPointOut() == false) {
				return;
			}

			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			ug.draw(snake);
		}
	}

	class ConnectionElse extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionElse(Rainbow endInlinkColor) {
			super(diamond1, diamond2);
			this.endInlinkColor = endInlinkColor;
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final double x = dimDiamond1.getWidth();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			return getTranslateDiamond1(stringBounder)
					.getTranslated(new Point2D.Double(x, dimDiamond1.getInY() + half));
		}

		protected Point2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			final double half = (dimDiamond2.getOutY() - dimDiamond2.getInY()) / 2;
			return getTranslateDiamond2(stringBounder)
					.getTranslated(new Point2D.Double(x, dimDiamond2.getInY() + half));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(endInlinkColor, Arrows.asToLeft());
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			snake.addPoint(x1, y1);

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Diamond.diamondHalfSize, getTranslateForThen(stringBounder).getDx()
					+ thenGeom.getWidth());

			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);
			snake.emphasizeDirection(Direction.DOWN);
			ug.apply(new UTranslate(x2, y2 - Diamond.diamondHalfSize)).draw(new UEmpty(5, Diamond.diamondHalfSize));

			ug.draw(snake);

		}

	}

	class ConnectionElseNoDiamond extends ConnectionElse {

		public ConnectionElseNoDiamond(Rainbow endInlinkColor) {
			super(endInlinkColor);
		}

		@Override
		protected Point2D getP2(final StringBounder stringBounder) {
			return calculateDimension(stringBounder).getPointOut();
		}

	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.apply(getTranslateForThen(stringBounder)).draw(thenBlock);
		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		if (optionalStop == null) {
			ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
		} else {
			ug.apply(getTranslateOptionalStop(stringBounder)).draw(optionalStop);
		}
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final FtileGeometry geoDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry geoThen = thenBlock.calculateDimension(stringBounder);
		final FtileGeometry geoDiamond2 = diamond2.calculateDimension(stringBounder);
		final FtileGeometry geo = geoDiamond1.appendBottom(geoThen).appendBottom(geoDiamond2);
		final double height = geo.getHeight() + 4 * Diamond.diamondHalfSize;
		return new FtileGeometry(geo.getWidth() + Diamond.diamondHalfSize, height, geo.getLeft(), geoDiamond1.getInY(),
				height);

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == thenBlock) {
			return getTranslateForThen(stringBounder);
		}
		if (child == diamond1) {
			return getTranslateDiamond1(stringBounder);
		}
		if (child == optionalStop) {
			return getTranslateOptionalStop(stringBounder);
		}
		if (child == diamond2) {
			return getTranslateDiamond2(stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslateForThen(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);

		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimThen = thenBlock.calculateDimension(stringBounder);

		final double y = dimDiamond1.getHeight()
				+ (dimTotal.getHeight() - dimDiamond1.getHeight() - dimDiamond2.getHeight() - dimThen.getHeight()) / 2;

		final double x = dimTotal.getLeft() - dimThen.getLeft();
		return new UTranslate(x, y);

	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double y1 = 0;
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateOptionalStop(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double y1 = 0;
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft() + dimDiamond1.getWidth();
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double x2 = dimTotal.getLeft() - dimDiamond2.getLeft();
		return new UTranslate(x2, y2);
	}

}
