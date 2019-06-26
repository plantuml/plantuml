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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileIfDown extends AbstractFtile {

	private final Ftile thenBlock;
	private final Ftile diamond1;
	private final Ftile diamond2;
	private final Ftile optionalStop;
	private final ConditionEndStyle conditionEndStyle;

	@Override
	public Collection<Ftile> getMyChildren() {
		if (optionalStop == null) {
			return Arrays.asList(thenBlock, diamond1, diamond2);
		}
		return Arrays.asList(thenBlock, diamond1, diamond2, optionalStop);
	}

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

	private FtileIfDown(Ftile thenBlock, Ftile diamond1, Ftile diamond2, Ftile optionalStop,
			ConditionEndStyle conditionEndStyle) {
		super(thenBlock.skinParam());
		this.thenBlock = thenBlock;
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
		this.optionalStop = optionalStop;
		this.conditionEndStyle = conditionEndStyle;
	}

	public static Ftile create(Ftile diamond1, Ftile diamond2, Swimlane swimlane, Ftile thenBlock, Rainbow arrowColor,
			ConditionEndStyle conditionEndStyle, FtileFactory ftileFactory, Ftile optionalStop, Rainbow elseColor) {

		elseColor = elseColor.withDefault(arrowColor);
		final FtileIfDown result = new FtileIfDown(thenBlock, diamond1, optionalStop == null ? diamond2
				: new FtileEmpty(ftileFactory.skinParam()), optionalStop, conditionEndStyle);

		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionIn(thenBlock.getInLinkRendering().getRainbow(arrowColor)));
		final boolean hasPointOut1 = thenBlock.calculateDimension(ftileFactory.getStringBounder()).hasPointOut();
		if (optionalStop == null) {
			if (hasPointOut1) {
				if (conditionEndStyle == ConditionEndStyle.DIAMOND) {
					conns.add(result.new ConnectionElse(elseColor));
				} else if (conditionEndStyle == ConditionEndStyle.HLINE) {
					conns.add(result.new ConnectionElseHline(elseColor));
					conns.add(result.new ConnectionHline(elseColor));
				}
			} else {
				conns.add(result.new ConnectionElseNoDiamond(elseColor));
			}
		} else {
			conns.add(result.new ConnectionHorizontal(elseColor));
		}
		conns.add(result.new ConnectionOut(thenBlock.getOutLinkRendering().getRainbow(arrowColor)));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionHorizontal extends AbstractConnection {

		private final Rainbow color;

		public ConnectionHorizontal(Rainbow color) {
			super(diamond1, optionalStop);
			this.color = color;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			// p2 = new Point2D.Double(p2.getX(), p1.getY());

			final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToRight());
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		private Point2D getP1(StringBounder stringBounder) {
			final FtileGeometry dimDiamond1 = getFtile1().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(dimDiamond1.getWidth(),
					(dimDiamond1.getInY() + dimDiamond1.getOutY()) / 2);

			return getTranslateDiamond1(stringBounder).getTranslated(p);
		}

		private Point2D getP2(StringBounder stringBounder) {
			final Dimension2D dimStop = getFtile2().calculateDimension(stringBounder);
			final Point2D p = new Point2D.Double(0, dimStop.getHeight() / 2);
			return getTranslateOptionalStop(stringBounder).getTranslated(p);
		}

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

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
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

		private Point2D getP2hline(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = getFtile2().calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			final double half = (dimDiamond2.getOutY() - dimDiamond2.getInY()) / 2;
			return getTranslateDiamond2(stringBounder)
					.getTranslated(new Point2D.Double(x, dimDiamond2.getInY() + half));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			if (getFtile1().calculateDimension(ug.getStringBounder()).hasPointOut() == false) {
				return;
			}

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.addPoint(getP1(stringBounder));

			if (conditionEndStyle == ConditionEndStyle.DIAMOND) {
				snake.addPoint(getP2(stringBounder));
			} else if (conditionEndStyle == ConditionEndStyle.HLINE) {
				snake.addPoint(getP2hline(stringBounder));
			}

			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {

			if (getFtile1().calculateDimension(ug.getStringBounder()).hasPointOut() == false) {
				return;
			}

			final StringBounder stringBounder = ug.getStringBounder();
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
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

		protected Point2D getP1(StringBounder stringBounder) {
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

			final Point2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false) {
				return;
			}
			final Point2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Diamond.diamondHalfSize, getTranslateForThen(stringBounder).getDx()
					+ thenGeom.getWidth());

			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToLeft());
			snake.addPoint(x1, y1);
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);
			snake.emphasizeDirection(Direction.DOWN);
			ug.apply(new UTranslate(x2, y2 - Diamond.diamondHalfSize)).draw(new UEmpty(5, Diamond.diamondHalfSize));
			ug.draw(snake);

		}

	}

	class ConnectionElseHline extends ConnectionElse {
		private final Rainbow endInlinkColor;

		public ConnectionElseHline(Rainbow endInlinkColor) {
			super(endInlinkColor);
			this.endInlinkColor = endInlinkColor;
		}

		@Override
		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Point2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false) {
				return;
			}
			final Point2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Diamond.diamondHalfSize, getTranslateForThen(stringBounder).getDx()
					+ thenGeom.getWidth());

			/*
			 * if( conditionEndStyle == ConditionEndStyle.DIAMOND ) { final Snake snake = new
			 * Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToLeft()); snake.addPoint(x1, y1);
			 * snake.addPoint(xmax, y1); snake.addPoint(xmax, y2); snake.addPoint(x2, y2);
			 * snake.emphasizeDirection(Direction.DOWN); ug.apply(new UTranslate(x2, y2 -
			 * Diamond.diamondHalfSize)).draw(new UEmpty(5, Diamond.diamondHalfSize)); ug.draw(snake); }
			 */
			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToDown());
			snake.addPoint(x1, y1);
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			ug.apply(new UTranslate(xmax, y2 - Diamond.diamondHalfSize)).draw(new UEmpty(5, Diamond.diamondHalfSize));
			ug.draw(snake);
			/*
			 * final Snake snake2 = new Snake(arrowHorizontalAlignment(), endInlinkColor); snake2.addPoint(xmax, y2);
			 * snake2.addPoint(x2, y2); ug.draw(snake2);
			 */

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

	// copied from FtileIfLongHorizontal to use with ConditionEndStyle.HLINE
	class ConnectionHline extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionHline(Rainbow endInlinkColor) {
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

		// the bottom or south point of the diamond that we omitted
		protected Point2D getP3(final StringBounder stringBounder) {
			final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x = dimDiamond2.getWidth();
			return getTranslateDiamond2(stringBounder).getTranslated(new Point2D.Double(x, dimDiamond2.getOutY()));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Point2D p1 = getP1(stringBounder);
			if (calculateDimension(stringBounder).hasPointOut() == false) {
				return;
			}
			final Point2D p2 = getP2(stringBounder);
			final Point2D p3 = getP3(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final double x3 = p3.getX();
			final double y3 = p3.getY();

			final FtileGeometry thenGeom = thenBlock.calculateDimension(stringBounder);
			final double xmax = Math.max(x1 + Diamond.diamondHalfSize, getTranslateForThen(stringBounder).getDx()
					+ thenGeom.getWidth());

			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor);
			snake.addPoint(xmax, y2);
			// ug.apply(new UTranslate(xmax, y2 - Diamond.diamondHalfSize)).draw(new UEmpty(5,
			// Diamond.diamondHalfSize));
			snake.addPoint(x2, y2);
			snake.addPoint(x3, y3);
			snake.goUnmergeable(MergeStrategy.NONE);
			ug.draw(snake);

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

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry geoDiamond1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry geoThen = thenBlock.calculateDimension(stringBounder);
		final FtileGeometry geoDiamond2 = diamond2.calculateDimension(stringBounder);
		final FtileGeometry geo = geoDiamond1.appendBottom(geoThen).appendBottom(geoDiamond2);
		final double height = geo.getHeight() + 3 * Diamond.diamondHalfSize
				+ Math.max(Diamond.diamondHalfSize * 1, getSouthLabelHeight(stringBounder));
		double width = geo.getWidth() + Diamond.diamondHalfSize;
		if (optionalStop != null) {
			width += optionalStop.calculateDimension(stringBounder).getWidth() + getAdditionalWidth(stringBounder);
		}
		final FtileGeometry result = new FtileGeometry(width, height, geo.getLeft(), geoDiamond1.getInY(), height);
		if (geoThen.hasPointOut() == false && optionalStop != null) {
			return result.withoutPointOut();
		}
		return result;

	}

	private double getAdditionalWidth(StringBounder stringBounder) {
		final FtileGeometry dimStop = optionalStop.calculateDimension(stringBounder);
		final double val1 = getEastLabelWidth(stringBounder);
		final double stopWidth = dimStop.getWidth();
		return Math.max(stopWidth, val1 + stopWidth / 2);
	}

	private double getSouthLabelHeight(StringBounder stringBounder) {
		if (diamond1 instanceof FtileDiamondInside) {
			return ((FtileDiamondInside) diamond1).getSouthLabelHeight(stringBounder);
		}
		if (diamond1 instanceof FtileDiamond) {
			return ((FtileDiamond) diamond1).getSouthLabelHeight(stringBounder);
		}
		return 0;
	}

	private double getEastLabelWidth(StringBounder stringBounder) {
		if (diamond1 instanceof FtileDiamondInside) {
			return ((FtileDiamondInside) diamond1).getEastLabelWidth(stringBounder);
		}
		if (diamond1 instanceof FtileDiamond) {
			return ((FtileDiamond) diamond1).getEastLabelWidth(stringBounder);
		}
		return 0;
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
		final FtileGeometry dimStop = optionalStop.calculateDimension(stringBounder);
		final double labelNorth = dimDiamond1.getInY();
		final double y1 = labelNorth + (dimDiamond1.getHeight() - labelNorth - dimStop.getHeight()) / 2;
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft() + dimDiamond1.getWidth()
				+ getAdditionalWidth(stringBounder);
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
