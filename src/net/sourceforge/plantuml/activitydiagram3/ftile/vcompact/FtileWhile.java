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
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondFoo1;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

class FtileWhile extends AbstractFtile {

	private final Ftile whileBlock;
	private final Ftile diamond1;
	private final Ftile specialOut;
	private final Ftile backward;

	@Override
	public Collection<Ftile> getMyChildren() {
		if (specialOut == null) {
			return Arrays.asList(whileBlock, diamond1);
		}
		return Arrays.asList(whileBlock, diamond1, specialOut);
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>(whileBlock.getSwimlanes());
		result.add(getSwimlaneIn());
		return result;
	}

	public Swimlane getSwimlaneIn() {
		return diamond1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	private FtileWhile(Ftile whileBlock, Ftile diamond1, Ftile specialOut, Ftile backward) {
		super(whileBlock.skinParam());
		this.whileBlock = whileBlock;
		this.diamond1 = diamond1;
		this.specialOut = specialOut;
		this.backward = backward;
	}

	public static Ftile create(Swimlane swimlane, Ftile whileBlock, Display test, HColor borderColor, HColor backColor,
			Rainbow arrowColor, Display yes, Display out2, Rainbow endInlinkColor, LinkRendering afterEndwhile,
			FontConfiguration fontArrow, FtileFactory ftileFactory, ConditionStyle conditionStyle,
			FontConfiguration fcTest, Instruction specialOut, Ftile backward) {

		final TextBlock yesTb = yes.create(fontArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam());
		final TextBlock testTb = test.isWhite() ? TextBlockUtils.empty(0, 0)
				: test.create(fcTest, whileBlock.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
						ftileFactory.skinParam());
		final TextBlock out = out2.create(fontArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam());

		final Ftile diamond1;
		if (conditionStyle == ConditionStyle.INSIDE) {
			diamond1 = new FtileDiamondInside(whileBlock.skinParam(), backColor, borderColor, swimlane, testTb)
					.withNorth(yesTb).withWest(out);
		} else if (conditionStyle == ConditionStyle.FOO1) {
			diamond1 = new FtileDiamondFoo1(whileBlock.skinParam(), backColor, borderColor, swimlane, testTb)
					.withNorth(yesTb).withWest(out);
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			diamond1 = new FtileDiamond(whileBlock.skinParam(), backColor, borderColor, swimlane).withNorth(testTb)
					.withSouth(yesTb).withWest(out);
		} else {
			throw new IllegalStateException();
		}

		final Ftile special = specialOut == null ? null : specialOut.createFtile(ftileFactory);

		final FtileWhile result = new FtileWhile(whileBlock, diamond1, special, backward);
		Rainbow afterEndwhileColor = arrowColor;
		if (afterEndwhile != null && afterEndwhile.getRainbow() != null && afterEndwhile.getRainbow().size() != 0) {
			afterEndwhileColor = afterEndwhile.getRainbow();
		}

		final Dimension2D dim = whileBlock.calculateDimension(ftileFactory.getStringBounder());
		final Display backDisplay = whileBlock.getOutLinkRendering().getDisplay();
		final TextBlock back = backDisplay.create(fontArrow, HorizontalAlignment.LEFT, ftileFactory.skinParam());

		final List<Connection> conns = new ArrayList<Connection>();
		if (dim.getWidth() == 0 || dim.getHeight() == 0) {
			conns.add(result.new ConnectionBackEmpty(endInlinkColor));
		} else {
			conns.add(result.new ConnectionIn(whileBlock.getInLinkRendering().getRainbow(arrowColor)));
			if (backward == null) {
				conns.add(result.new ConnectionBackSimple(endInlinkColor, back));
			} else {
				conns.add(result.new ConnectionBackBackward1(endInlinkColor, back));
				conns.add(result.new ConnectionBackBackward2(endInlinkColor));
			}
		}
		if (specialOut == null) {
			conns.add(result.new ConnectionOut(afterEndwhileColor));
		} else {
			conns.add(result.new ConnectionOutSpecial(afterEndwhileColor));
		}
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionIn(Rainbow arrowColor) {
			super(diamond1, whileBlock);
			this.arrowColor = arrowColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder)
					.getTranslated(getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateForWhile(stringBounder)
					.getTranslated(getFtile2().calculateDimension(stringBounder).getPointIn());
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
			snake.goUnmergeable(MergeStrategy.LIMITED);
			ug.draw(snake);
		}
	}

	class ConnectionBackSimple extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow endInlinkColor;
		private final TextBlock back;

		public ConnectionBackSimple(Rainbow endInlinkColor, TextBlock back) {
			super(whileBlock, diamond1);
			this.endInlinkColor = endInlinkColor;
			this.back = back;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
			if (geo.hasPointOut() == false) {
				return null;
			}
			return getTranslateForWhile(stringBounder).getTranslated(geo.getPointOut());
		}

		private double getBottom(final StringBounder stringBounder) {
			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
			return getTranslateForWhile(stringBounder).getDy() + geo.getHeight();
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToLeft());
			final Dimension2D dimTotal = calculateDimension(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			if (p1 == null) {
				return;
			}
			final Point2D p2 = getP2(stringBounder);
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			final double y2 = p2.getY() + dimDiamond1.getInY() + half;

			snake.setLabel(back);
			snake.addPoint(x1, y1);
			final double y1bis = Math.max(y1, getBottom(stringBounder)) + Diamond.diamondHalfSize;
			snake.addPoint(x1, y1bis);
			final double xx = dimTotal.getWidth();
			snake.addPoint(xx, y1bis);
			snake.addPoint(xx, y2);
			snake.addPoint(x2, y2);
			snake.emphasizeDirection(Direction.UP);

			ug.draw(snake);
			ug.apply(new UTranslate(x1, y1bis)).draw(new UEmpty(5, Diamond.diamondHalfSize));

		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToLeft());
			final Dimension2D dimTotal = calculateDimension(stringBounder);
			final Point2D ap1 = getP1(stringBounder);
			final Point2D ap2 = getP2(stringBounder);
			final Point2D p1 = translate1.getTranslated(ap1);
			final Point2D p2 = translate2.getTranslated(ap2);

			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			final double y2 = p2.getY() + dimDiamond1.getInY() + half;

			snake.addPoint(x1, y1);
			snake.addPoint(x1, y1 + Diamond.diamondHalfSize);
			final double xx = Math.max(translate1.getDx(), translate2.getDx()) + dimTotal.getWidth();
			snake.addPoint(xx, y1 + Diamond.diamondHalfSize);
			snake.addPoint(xx, y2);
			snake.addPoint(x2, y2);
			snake.goUnmergeable(MergeStrategy.LIMITED);

			ug.draw(snake);

			ug.apply(new UTranslate(x1, y1 + Diamond.diamondHalfSize)).draw(new UEmpty(5, Diamond.diamondHalfSize));

			ug = ug.apply(endInlinkColor.getColor()).apply(endInlinkColor.getColor().bg());
			ug.apply(new UTranslate(xx, (y1 + y2) / 2)).draw(Arrows.asToUp());

		}

	}

	class ConnectionBackBackward1 extends AbstractConnection {
		private final Rainbow endInlinkColor;
		private final TextBlock back;

		public ConnectionBackBackward1(Rainbow endInlinkColor, TextBlock back) {
			super(whileBlock, backward);
			this.endInlinkColor = endInlinkColor;
			this.back = back;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
			if (geo.hasPointOut() == false) {
				return null;
			}
			return getTranslateForWhile(stringBounder).getTranslated(geo.getPointOut());
		}

		private double getBottom(final StringBounder stringBounder) {
			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
			return getTranslateForWhile(stringBounder).getDy() + geo.getHeight();
		}

		private Point2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dim = backward.calculateDimension(stringBounder);
			return getTranslateBackward(stringBounder).getTranslated(new Point2D.Double(dim.getLeft(), dim.getOutY()));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToUp());
			final Point2D p1 = getP1(stringBounder);
			if (p1 == null) {
				return;
			}
			final Point2D p2 = getP2(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			snake.setLabel(back);
			snake.addPoint(x1, y1);
			final double y1bis = Math.max(y1, getBottom(stringBounder)) + Diamond.diamondHalfSize;
			snake.addPoint(x1, y1bis);
			snake.addPoint(x2, y1bis);
			snake.addPoint(x2, y2);

			ug.draw(snake);
			ug.apply(new UTranslate(x1, y1bis)).draw(new UEmpty(5, Diamond.diamondHalfSize));
		}
	}

	class ConnectionBackBackward2 extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionBackBackward2(Rainbow endInlinkColor) {
			super(backward, diamond1);
			this.endInlinkColor = endInlinkColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			final FtileGeometry dim = backward.calculateDimension(stringBounder);
			return getTranslateBackward(stringBounder).getTranslated(new Point2D.Double(dim.getLeft(), dim.getInY()));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToLeft());
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			final double y2 = p2.getY() + dimDiamond1.getInY() + half;

			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}
	}

	class ConnectionBackEmpty extends AbstractConnection {
		private final Rainbow endInlinkColor;

		public ConnectionBackEmpty(Rainbow endInlinkColor) {
			super(diamond1, diamond1);
			this.endInlinkColor = endInlinkColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder)
					.getTranslated(diamond1.calculateDimension(stringBounder).getPointOut());
		}

		private double getBottom(final StringBounder stringBounder) {
			final FtileGeometry geo = whileBlock.calculateDimension(stringBounder);
			return getTranslateForWhile(stringBounder).getDy() + geo.getHeight();
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), endInlinkColor, Arrows.asToLeft());
			final Dimension2D dimTotal = calculateDimension(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			// final double y2 = p2.getY() + dimDiamond1.getOutY() / 2;
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			final double y2 = p2.getY() + dimDiamond1.getInY() + half;

			snake.addPoint(x1, y1);
			final double y1bis = Math.max(y1, getBottom(stringBounder)) + Diamond.diamondHalfSize;
			snake.addPoint(x1, y1bis);
			final double xx = dimTotal.getWidth();
			snake.addPoint(xx, y1bis);
			snake.addPoint(xx, y2);
			snake.addPoint(x2, y2);
			snake.emphasizeDirection(Direction.UP);

			ug.draw(snake);

			ug.apply(new UTranslate(x1, y1bis)).draw(new UEmpty(5, Diamond.diamondHalfSize));

		}

	}

	class ConnectionOut extends AbstractConnection {
		private final Rainbow afterEndwhileColor;

		public ConnectionOut(Rainbow afterEndwhileColor) {
			super(diamond1, null);
			this.afterEndwhileColor = afterEndwhileColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dimTotal = calculateDimension(stringBounder);
			return new Point2D.Double(dimTotal.getLeft(), dimTotal.getHeight());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), afterEndwhileColor);

			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			final double y1 = p1.getY() + dimDiamond1.getInY() + half;
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			snake.addPoint(x1, y1);
			snake.addPoint(Diamond.diamondHalfSize, y1);
			snake.addPoint(Diamond.diamondHalfSize, y2);
			snake.emphasizeDirection(Direction.DOWN);
			snake.goUnmergeable(MergeStrategy.LIMITED);

			ug.draw(snake);

			final Snake snake2 = new Snake(arrowHorizontalAlignment(), afterEndwhileColor);
			snake2.addPoint(Diamond.diamondHalfSize, y2);
			snake2.addPoint(x2, y2);
			// snake2.goUnmergeable(MergeStrategy.LIMITED);
			ug.draw(snake2);

		}
	}

	class ConnectionOutSpecial extends AbstractConnection {
		private final Rainbow afterEndwhileColor;

		public ConnectionOutSpecial(Rainbow afterEndwhileColor) {
			super(diamond1, specialOut);
			this.afterEndwhileColor = afterEndwhileColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateForSpecial(stringBounder)
					.getTranslated(specialOut.calculateDimension(stringBounder).getPointIn());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), afterEndwhileColor, Arrows.asToDown());

			final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);

			final double x1 = p1.getX();
			final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
			final double y1 = p1.getY() + dimDiamond1.getInY() + half;
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);

		}
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.apply(getTranslateForWhile(stringBounder)).draw(whileBlock);
		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		if (specialOut != null) {
			ug.apply(getTranslateForSpecial(stringBounder)).draw(specialOut);
		}
		if (backward != null) {
			ug.apply(getTranslateBackward(stringBounder)).draw(backward);
		}
	}

	private UTranslate getTranslateBackward(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionFtile(stringBounder);
		final Dimension2D dimBackward = backward.calculateDimension(stringBounder);
		final double x = dimTotal.getWidth() - dimBackward.getWidth();
		final double y = (dimTotal.getHeight() - dimBackward.getHeight()) / 2;

		return new UTranslate(x, y);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry geoDiamond1 = diamond1.calculateDimension(stringBounder);
		FtileGeometry geoWhile = whileBlock.calculateDimension(stringBounder);
		final double diff = -geoWhile.getWidth();
		if (diff > 0) {
			geoWhile = geoWhile.addMarginX(diff / 2);
			assert false;
		}
		final FtileGeometry geo = geoDiamond1.appendBottom(geoWhile);
		final double height = geo.getHeight() + 4 * Diamond.diamondHalfSize;
		final double dx = 2 * Diamond.diamondHalfSize;
		double backwardWidth = 0;
		if (backward != null) {
			backwardWidth += backward.calculateDimension(stringBounder).getWidth();
		}
		return new FtileGeometry(
				xDeltaBecauseSpecial(stringBounder) + geo.getWidth() + dx + Diamond.diamondHalfSize + backwardWidth,
				height, xDeltaBecauseSpecial(stringBounder) + geo.getLeft() + dx, geoDiamond1.getInY(), height);

	}

	private double xDeltaBecauseSpecial(StringBounder stringBounder) {
		if (specialOut == null) {
			return 0;
		}
		return specialOut.calculateDimension(stringBounder).getWidth();
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == whileBlock) {
			return getTranslateForWhile(stringBounder);
		}
		if (child == diamond1) {
			return getTranslateDiamond1(stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslateForWhile(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);

		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimWhile = whileBlock.calculateDimension(stringBounder);

		final double y = dimDiamond1.getHeight()
				+ (dimTotal.getHeight() - dimDiamond1.getHeight() - dimWhile.getHeight()) / 2;

		final double x = dimTotal.getLeft() - dimWhile.getLeft();
		return new UTranslate(x, y);

	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimension(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);

		final double y1 = 0;
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateForSpecial(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double half = (dimDiamond1.getOutY() - dimDiamond1.getInY()) / 2;
		final double y1 = Math.max(3 * half, 4 * Diamond.diamondHalfSize);
		final double xWhile = getTranslateForWhile(stringBounder).getDx() - Diamond.diamondHalfSize;
		final double xDiamond = getTranslateDiamond1(stringBounder).getDx();
		// final double x1 = xWhile - xDeltaBecauseSpecial(stringBounder);
		final double x1 = Math.min(xWhile, xDiamond) - xDeltaBecauseSpecial(stringBounder);
		// final double x1 = getTranslateForWhile(stringBounder).getDx() -
		// dimDiamond1.getWidth()
		// - xDeltaBecauseSpecial(stringBounder);
		return new UTranslate(x1, y1);
	}

}
