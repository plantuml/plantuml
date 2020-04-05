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
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondFoo1;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

class FtileRepeat extends AbstractFtile {

	private final Ftile repeat;
	private final Ftile diamond1;
	private final Ftile diamond2;
	private final Ftile backward;
	private final TextBlock tbTest;

	@Override
	public Collection<Ftile> getMyChildren() {
		return Arrays.asList(repeat, diamond1, diamond2);
	}

	private FtileRepeat(Ftile repeat, Ftile diamond1, Ftile diamond2, TextBlock tbTest, Ftile backward) {
		super(repeat.skinParam());
		this.repeat = repeat;
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
		this.tbTest = tbTest;
		this.backward = backward;
	}

	public Swimlane getSwimlaneIn() {
		return repeat.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return diamond2.getSwimlaneOut();
	}

	public Set<Swimlane> getSwimlanes() {
		return repeat.getSwimlanes();
	}

	public static Ftile create(LinkRendering backRepeatLinkRendering, Swimlane swimlane, Swimlane swimlaneOut,
			Ftile entry, Ftile repeat, Display test, Display yes, Display out, HColor borderColor,
			HColor diamondColor, Rainbow arrowColor, Rainbow endRepeatLinkColor, ConditionStyle conditionStyle,
			ISkinSimple spriteContainer, FontConfiguration fcDiamond, FontConfiguration fcArrow, Ftile backward,
			boolean noOut) {

		final FontConfiguration fontConfiguration1 = conditionStyle == ConditionStyle.INSIDE ? fcDiamond : fcArrow;

		final TextBlock tbTest = (Display.isNull(test) || test.isWhite()) ? TextBlockUtils.empty(0, 0) : test.create(
				fontConfiguration1, repeat.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
				spriteContainer);
		final TextBlock yesTb = yes.create(fcArrow, HorizontalAlignment.LEFT, spriteContainer);
		final TextBlock outTb = out.create(fcArrow, HorizontalAlignment.LEFT, spriteContainer);

		final Ftile diamond1;
		// assert swimlane == repeat.getSwimlaneIn();
		if (entry == null) {
			diamond1 = new FtileDiamond(repeat.skinParam(), diamondColor, borderColor, repeat.getSwimlaneIn());
		} else {
			diamond1 = entry;
		}
		final FtileRepeat result;
		if (conditionStyle == ConditionStyle.INSIDE) {
			final Ftile diamond2;
			if (noOut && Display.isNull(test)) {
				diamond2 = new FtileEmpty(repeat.skinParam());
			} else {
				diamond2 = new FtileDiamondInside(repeat.skinParam(), diamondColor, borderColor, swimlaneOut, tbTest)
						.withEast(yesTb).withSouth(outTb);
			}
			result = new FtileRepeat(repeat, diamond1, diamond2, TextBlockUtils.empty(0, 0), backward);
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			final Ftile diamond2 = new FtileDiamond(repeat.skinParam(), diamondColor, borderColor, swimlane)
					.withEast(tbTest);
			result = new FtileRepeat(repeat, diamond1, diamond2, tbTest, backward);
		} else if (conditionStyle == ConditionStyle.FOO1) {
			final Ftile diamond2 = new FtileDiamondFoo1(repeat.skinParam(), diamondColor, borderColor, swimlane, tbTest);
			result = new FtileRepeat(repeat, diamond1, diamond2, TextBlockUtils.empty(0, 0), backward);
		} else {
			throw new IllegalStateException();
		}

		final List<Connection> conns = new ArrayList<Connection>();
		final Display in1 = repeat.getInLinkRendering().getDisplay();
		final TextBlock tbin1 = in1 == null ? null : in1.create7(fcArrow, HorizontalAlignment.LEFT, spriteContainer,
				CreoleMode.SIMPLE_LINE);
		conns.add(result.new ConnectionIn(repeat.getInLinkRendering().getRainbow(arrowColor), tbin1));

		final Display backLink1 = backRepeatLinkRendering.getDisplay();
		final TextBlock tbbackLink1 = backLink1 == null ? null : backLink1.create7(fcArrow, HorizontalAlignment.LEFT,
				spriteContainer, CreoleMode.SIMPLE_LINE);
		if (repeat.getSwimlaneIn() == swimlaneOut) {
			if (backward == null) {
				conns.add(result.new ConnectionBackSimple(backRepeatLinkRendering.getRainbow(arrowColor), tbbackLink1));
			} else {
				conns.add(result.new ConnectionBackBackward1(backRepeatLinkRendering.getRainbow(arrowColor),
						tbbackLink1));
				conns.add(result.new ConnectionBackBackward2(backRepeatLinkRendering.getRainbow(arrowColor)));
			}
		} else {
			conns.add(result.new ConnectionBackComplex1(backRepeatLinkRendering.getRainbow(arrowColor)));
			conns.add(result.new ConnectionBackComplexHorizontalOnly(backRepeatLinkRendering.getRainbow(arrowColor),
					tbbackLink1));
		}

		final Display out1 = repeat.getOutLinkRendering().getDisplay();
		final TextBlock tbout1 = out1 == null ? null : out1.create7(fcArrow, HorizontalAlignment.LEFT, spriteContainer,
				CreoleMode.SIMPLE_LINE);

		final Rainbow tmpColor = endRepeatLinkColor.withDefault(arrowColor);
		conns.add(result.new ConnectionOut(tmpColor, tbout1));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection {
		private final Rainbow arrowColor;
		private final TextBlock tbin;

		public ConnectionIn(Rainbow arrowColor, TextBlock tbin) {
			super(diamond1, repeat);
			this.arrowColor = arrowColor;
			this.tbin = tbin;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getFtile1().calculateDimension(stringBounder).translate(getTranslateDiamond1(stringBounder))
					.getPointOut();
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getFtile2().calculateDimension(stringBounder).translate(getTranslateForRepeat(stringBounder))
					.getPointIn();
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.setLabel(tbin);
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}
	}

	class ConnectionOut extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock tbout;

		public ConnectionOut(Rainbow arrowColor, TextBlock tbout) {
			super(repeat, diamond2);
			this.arrowColor = arrowColor;
			this.tbout = tbout;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateForRepeat(stringBounder).getTranslated(
					getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(
					getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			if (getFtile1().calculateDimension(stringBounder).hasPointOut() == false) {
				return;
			}

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			snake.setLabel(tbout);
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			if (getFtile1().calculateDimension(stringBounder).hasPointOut() == false) {
				return;
			}
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor);
			snake.setLabel(tbout);
			final Point2D mp1a = translate1.getTranslated(getP1(stringBounder));
			final Point2D mp2b = translate2.getTranslated(getP2(stringBounder));
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			// snake.addPoint(mp2b);
			ug.draw(snake);

			final Snake small = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			small.addPoint(mp2b.getX(), middle);
			small.addPoint(mp2b);
			ug.draw(small);

		}

	}

	class ConnectionBackComplex1 extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionBackComplex1(Rainbow arrowColor) {
			super(diamond2, repeat);
			this.arrowColor = arrowColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		public void drawU(UGraphic ug) {
			// throw new UnsupportedOperationException();
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D dimRepeat = repeat.calculateDimension(stringBounder);

			Point2D p1 = getP1(stringBounder);
			Point2D p2 = getP2(stringBounder);
			p1 = translate1.getTranslated(p1);
			p2 = translate2.getTranslated(p2);
			final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			final double xmax = p1.getX() + dimDiamond2.getWidth() / 2 + dimRepeat.getWidth() / 2
					+ Diamond.diamondHalfSize;

			UPolygon arrow = Arrows.asToLeft();
			if (xmax < x2) {
				arrow = Arrows.asToRight();
				x2 = p2.getX();
			}
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, arrow);
			snake.emphasizeDirection(Direction.UP);

			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	class ConnectionBackComplexHorizontalOnly extends AbstractConnection {
		private final Rainbow arrowColor;
		private final TextBlock tbback;

		public ConnectionBackComplexHorizontalOnly(Rainbow arrowColor, TextBlock tbback) {
			super(diamond2, diamond2);
			this.arrowColor = arrowColor;
			this.tbback = tbback;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, null);
			snake.setLabel(tbback);
			final Dimension2D dimRepeat = repeat.calculateDimension(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double xmax = p1.getX() + dimDiamond2.getWidth() / 2 + dimRepeat.getWidth() / 2
					+ Diamond.diamondHalfSize;
			snake.addPoint(xmax, y1);
			ug.draw(snake);
		}

	}

	class ConnectionBackBackward1 extends AbstractConnection {
		private final Rainbow arrowColor;
		private final TextBlock tbback;

		public ConnectionBackBackward1(Rainbow arrowColor, TextBlock tbback) {
			super(diamond2, backward);
			this.arrowColor = arrowColor;
			this.tbback = tbback;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dim = backward.calculateDimension(stringBounder);
			return getTranslateBackward(stringBounder).getTranslated(new Point2D.Double(dim.getLeft(), dim.getOutY()));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToUp());
			snake.setLabel(tbback);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	class ConnectionBackBackward2 extends AbstractConnection {
		private final Rainbow arrowColor;

		public ConnectionBackBackward2(Rainbow arrowColor) {
			super(backward, diamond1);
			this.arrowColor = arrowColor;
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

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToLeft());
			snake.emphasizeDirection(Direction.UP);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	class ConnectionBackSimple extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock tbback;

		public ConnectionBackSimple(Rainbow arrowColor, TextBlock tbback) {
			super(diamond2, repeat);
			this.arrowColor = arrowColor;
			this.tbback = tbback;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new Point2D.Double(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToLeft());
			snake.setLabel(tbback);
			snake.emphasizeDirection(Direction.UP);
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double xmax = dimTotal.getWidth() - Diamond.diamondHalfSize;
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToLeft());
			snake.setLabel(tbback);
			snake.emphasizeDirection(Direction.UP);
			final Dimension2D dimRepeat = repeat.calculateDimension(stringBounder);

			Point2D p1 = getP1(stringBounder);
			Point2D p2 = getP2(stringBounder);
			p1 = translate1.getTranslated(p1);
			p2 = translate2.getTranslated(p2);
			final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double xmax = p1.getX() + dimDiamond2.getWidth() / 2 + dimRepeat.getWidth() / 2
					+ Diamond.diamondHalfSize;
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.apply(getTranslateForRepeat(stringBounder)).draw(repeat);
		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
		if (backward != null) {
			ug.apply(getTranslateBackward(stringBounder)).draw(backward);
		}

	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new FtileGeometry(dimTotal, getLeft(stringBounder), 0, dimTotal.getHeight());
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final Dimension2D dimRepeat = repeat.calculateDimension(stringBounder);

		final double w = tbTest.calculateDimension(stringBounder).getWidth();

		double width = getLeft(stringBounder) + getRight(stringBounder);
		width = Math.max(width, w + 2 * Diamond.diamondHalfSize);
		if (backward != null) {
			width += backward.calculateDimension(stringBounder).getWidth();
		}
		final double height = dimDiamond1.getHeight() + dimRepeat.getHeight() + dimDiamond2.getHeight() + 8
				* Diamond.diamondHalfSize;
		return new Dimension2DDouble(width + 2 * Diamond.diamondHalfSize, height);

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == repeat) {
			return getTranslateForRepeat(stringBounder);
		}
		if (child == diamond1) {
			return getTranslateDiamond1(stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslateForRepeat(StringBounder stringBounder) {

		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimRepeat = repeat.calculateDimension(stringBounder);
		final double space = dimTotal.getHeight() - dimDiamond1.getHeight() - dimDiamond2.getHeight()
				- dimRepeat.getHeight();
		final double y = dimDiamond1.getHeight() + space / 2;
		final double left = getLeft(stringBounder);
		return new UTranslate(left - repeat.calculateDimension(stringBounder).getLeft(), y);

	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double left = getLeft(stringBounder);
		return UTranslate.dx(left - dimDiamond1.getWidth() / 2);
	}

	private UTranslate getTranslateBackward(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimBackward = backward.calculateDimension(stringBounder);
		final double x = dimTotal.getWidth() - dimBackward.getWidth();
		final double y = (dimTotal.getHeight() - dimBackward.getHeight()) / 2;

		return new UTranslate(x, y);
	}

	private UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double left = getLeft(stringBounder);
		return new UTranslate(left - dimDiamond2.getWidth() / 2, y2);
	}

	private double getLeft(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		double left1 = repeat.calculateDimension(stringBounder).getLeft();
		left1 = Math.max(left1, dimDiamond1.getWidth() / 2);
		double left2 = repeat.calculateDimension(stringBounder).getLeft();
		left2 = Math.max(left2, dimDiamond2.getWidth() / 2);
		return Math.max(left1, left2);
	}

	private double getRight(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final Dimension2D dimRepeat = repeat.calculateDimension(stringBounder);
		double right1 = dimRepeat.getWidth() - repeat.calculateDimension(stringBounder).getLeft();
		right1 = Math.max(right1, dimDiamond1.getWidth() / 2);
		double right2 = dimRepeat.getWidth() - repeat.calculateDimension(stringBounder).getLeft();
		right2 = Math.max(right2, dimDiamond2.getWidth() / 2);
		return Math.max(right1, right2);
	}

}
