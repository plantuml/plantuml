/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondSquare;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.utils.Direction;

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
		final Set<Swimlane> result = new HashSet<>(repeat.getSwimlanes());
		result.addAll(diamond1.getSwimlanes());
		result.addAll(diamond2.getSwimlanes());
		if (backward != null)
			result.addAll(backward.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public static Ftile create(Swimlane swimlane, Swimlane swimlaneOut, Ftile entry, Ftile repeat, Display test,
			Display yes, Display out, HColor borderColor, HColor diamondColor, Rainbow arrowColor,
			Rainbow endRepeatLinkColor, ConditionStyle conditionStyle, ISkinSimple spriteContainer,
			FontConfiguration fcDiamond, FontConfiguration fcArrow, Ftile backward, boolean noOut,
			LinkRendering incoming1, LinkRendering incoming2) {

		final FontConfiguration fontConfiguration1 = conditionStyle == ConditionStyle.INSIDE_HEXAGON ? fcDiamond
				: fcArrow;

		final TextBlock tbTest = (Display.isNull(test) || test.isWhite()) ? TextBlockUtils.empty(0, 0)
				: test.create(fontConfiguration1, repeat.skinParam().getDefaultTextAlignment(HorizontalAlignment.LEFT),
						spriteContainer);
		final TextBlock yesTb = yes.create(fcArrow, HorizontalAlignment.LEFT, spriteContainer);
		final TextBlock outTb = out.create(fcArrow, HorizontalAlignment.LEFT, spriteContainer);

		final Ftile diamond1;
		// assert swimlane == repeat.getSwimlaneIn();
		if (entry == null)
			diamond1 = new FtileDiamond(repeat.skinParam(), diamondColor, borderColor, swimlane);
		else
			diamond1 = entry;

		final FtileRepeat result;
		if (conditionStyle == ConditionStyle.INSIDE_HEXAGON) {
			final Ftile diamond2;
			if (noOut && Display.isNull(test))
				diamond2 = new FtileEmpty(repeat.skinParam());
			else
				diamond2 = new FtileDiamondInside(tbTest, repeat.skinParam(), diamondColor, borderColor, swimlaneOut)
						.withEast(yesTb).withSouth(outTb);

			result = new FtileRepeat(repeat, diamond1, diamond2, TextBlockUtils.empty(0, 0), backward);
		} else if (conditionStyle == ConditionStyle.EMPTY_DIAMOND) {
			final Ftile diamond2 = new FtileDiamond(repeat.skinParam(), diamondColor, borderColor, swimlane)
					.withEast(tbTest);
			result = new FtileRepeat(repeat, diamond1, diamond2, tbTest, backward);
		} else if (conditionStyle == ConditionStyle.INSIDE_DIAMOND) {
			final Ftile diamond2 = new FtileDiamondSquare(tbTest, repeat.skinParam(), diamondColor, borderColor,
					swimlane);
			result = new FtileRepeat(repeat, diamond1, diamond2, TextBlockUtils.empty(0, 0), backward);
		} else {
			throw new IllegalStateException();
		}

		final List<Connection> conns = new ArrayList<>();
		final Display in1 = repeat.getInLinkRendering().getDisplay();
		final TextBlock tbin1 = in1 == null ? null
				: in1.create7(fcArrow, HorizontalAlignment.LEFT, spriteContainer, CreoleMode.SIMPLE_LINE);
		conns.add(result.new ConnectionIn(repeat.getInLinkRendering().getRainbow(arrowColor), tbin1));

		final TextBlock incomingText;
		if (incoming1 == null || incoming1.getDisplay() == null)
			incomingText = null;
		else
			incomingText = incoming1.getDisplay().create7(fcArrow, HorizontalAlignment.LEFT, spriteContainer,
					CreoleMode.SIMPLE_LINE);

		if (backward != null) {
			final Rainbow rainbow1 = incoming1.getRainbow(arrowColor);
			conns.add(result.new ConnectionBackBackward1(rainbow1, incomingText));
			final TextBlock backArrowLabel = incoming2 == null || incoming2.getDisplay() == null ? null
					: incoming2.getDisplay().create(fcArrow, HorizontalAlignment.LEFT, spriteContainer);
			final Rainbow rainbow2 = incoming2.getRainbow(arrowColor);
			conns.add(result.new ConnectionBackBackward2(rainbow2, backArrowLabel));
		} else if (repeat.getSwimlaneIn() == null || repeat.getSwimlaneIn() == swimlaneOut) {
			if (repeat.getSwimlaneIn() != null && repeat.getSwimlaneIn().isSmallerThanAllOthers(repeat.getSwimlanes()))
				conns.add(result.new ConnectionBackSimple1(incoming1.getRainbow(arrowColor), incomingText));
			else
				conns.add(result.new ConnectionBackSimple2(incoming1.getRainbow(arrowColor), incomingText));

		} else {
			conns.add(result.new ConnectionBackComplex1(incoming1.getRainbow(arrowColor)));
		}

		final Display out1 = repeat.getOutLinkRendering().getDisplay();
		final TextBlock tbout1 = out1 == null ? null
				: out1.create7(fcArrow, HorizontalAlignment.LEFT, spriteContainer, CreoleMode.SIMPLE_LINE);

		final Rainbow tmpColor = endRepeatLinkColor.withDefault(arrowColor);
		conns.add(result.new ConnectionOut(tmpColor, tbout1));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock tbin;

		public ConnectionIn(Rainbow arrowColor, TextBlock tbin) {
			super(diamond1, repeat);
			this.arrowColor = arrowColor;
			this.tbin = tbin;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getFtile1().calculateDimension(stringBounder).translate(getTranslateDiamond1(stringBounder))
					.getPointOut();
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getFtile2().calculateDimension(stringBounder).translate(getTranslateForRepeat(stringBounder))
					.getPointIn();
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			drawSnake(ug, p1, p2);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XPoint2D p1 = translate1.getTranslated(getP1(stringBounder));
			final XPoint2D p2 = translate2.getTranslated(getP2(stringBounder));

			drawSnake(ug, p1, p2);
		}

		private void drawSnake(UGraphic ug, final XPoint2D p1, final XPoint2D p2) {
			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown()).withLabel(tbin,
					arrowHorizontalAlignment());
			snake.addPoint(p1);
			if (p1.getX() != p2.getX()) {
				final double my = (p1.getY() + p2.getY()) / 2;
				snake.addPoint(new XPoint2D(p1.getX(), my));
				snake.addPoint(new XPoint2D(p2.getX(), my));
			}
			snake.addPoint(p2);

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

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateForRepeat(stringBounder)
					.getTranslated(getFtile1().calculateDimension(stringBounder).getPointOut());
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder)
					.getTranslated(getFtile2().calculateDimension(stringBounder).getPointIn());
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			if (getFtile1().calculateDimension(stringBounder).hasPointOut() == false)
				return;

			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown()).withLabel(tbout,
					arrowHorizontalAlignment());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			if (getFtile1().calculateDimension(stringBounder).hasPointOut() == false)
				return;

			final Snake snake = Snake.create(skinParam(), arrowColor);
			final XPoint2D mp1a = translate1.getTranslated(getP1(stringBounder));
			final XPoint2D mp2b = translate2.getTranslated(getP2(stringBounder));
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			ug.draw(snake);

			final Snake small = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToDown()).withLabel(tbout,
					arrowHorizontalAlignment());
			small.addPoint(mp2b.getX(), middle);
			small.addPoint(mp2b);
			ug.draw(small);

		}

	}

	class ConnectionBackComplex1 extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;

		public ConnectionBackComplex1(Rainbow arrowColor) {
			super(diamond2, diamond1);
			this.arrowColor = arrowColor;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			drawSnake(ug, p1, p2);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = translate1.getTranslated(getP1(stringBounder));
			final XPoint2D p2 = translate2.getTranslated(getP2(stringBounder));
			drawSnake(ug, p1, p2);
		}

		private void drawSnake(UGraphic ug, XPoint2D p1, XPoint2D p2) {
			final StringBounder stringBounder = ug.getStringBounder();

			final XDimension2D dimRepeat = repeat.calculateDimension(stringBounder);

			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			final double x1_a = p1.getX() + dimDiamond2.getWidth();
			final double x1_b = p1.getX() + dimDiamond2.getWidth() / 2 + dimRepeat.getWidth() / 2
					+ Hexagon.hexagonHalfSize;

			final Snake snake;
			if (x2 < x1_a) {
				snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToLeft())
						.emphasizeDirection(Direction.UP);
				snake.addPoint(x1_a, y1);
				if (x1_a < x1_b) {
					snake.addPoint(x1_b, y1);
					snake.addPoint(x1_b, y2);
				} else {
					snake.addPoint(x1_a + 10, y1);
					snake.addPoint(x1_a + 10, y2);
				}
			} else {
				x2 = p2.getX();
				snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToRight())
						.emphasizeDirection(Direction.UP);
				snake.addPoint(x1_a, y1);
				final double middle = x1_a / 4 + x2 * 3 / 4;
				snake.addPoint(middle, y1);
				snake.addPoint(middle, y2);
			}
			snake.addPoint(x2, y2);
			ug.draw(snake);
		}

	}

	class ConnectionBackBackward1 extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock tbback;

		public ConnectionBackBackward1(Rainbow arrowColor, TextBlock tbback) {
			super(diamond2, backward);
			this.arrowColor = arrowColor;
			this.tbback = tbback;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			final FtileGeometry dim = backward.calculateDimension(stringBounder);
			return getTranslateBackward(stringBounder).getTranslated(new XPoint2D(dim.getLeft(), dim.getOutY()));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			drawSnake(ug, p1, p2);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XPoint2D p1 = translate1.getTranslated(getP1(stringBounder));
			final XPoint2D p2 = translate2.getTranslated(getP2(stringBounder));
			drawSnake(ug, p1, p2);
		}

		private void drawSnake(UGraphic ug, final XPoint2D p1, final XPoint2D p2) {
			final XDimension2D dimDiamond2 = diamond2.calculateDimension(ug.getStringBounder());
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToUp()).withLabel(tbback,
					arrowHorizontalAlignment());

			snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	class ConnectionBackBackward2 extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock label;

		public ConnectionBackBackward2(Rainbow arrowColor, TextBlock label) {
			super(backward, diamond1);
			this.label = label;
			this.arrowColor = arrowColor;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			final FtileGeometry dim = backward.calculateDimension(stringBounder);
			return getTranslateBackward(stringBounder).getTranslated(new XPoint2D(dim.getLeft(), dim.getInY()));
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();

			XPoint2D p1 = getP1(stringBounder);
			XPoint2D p2 = getP2(stringBounder);
			p1 = translate1.getTranslated(p1);
			p2 = translate2.getTranslated(p2);

			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			double x2 = p2.getX();
			if (x2 < x1)
				x2 += dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			Snake snake = Snake.create(skinParam(), arrowColor,
					x2 < x1 ? skinParam().arrows().asToLeft() : skinParam().arrows().asToRight());
			if (label != null)
				snake = snake.withLabel(label, arrowHorizontalAlignment());

			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);

		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToLeft());
			if (label != null)
				snake = snake.withLabel(label, arrowHorizontalAlignment());

			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
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

	class ConnectionBackSimple1 extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock tbback;

		public ConnectionBackSimple1(Rainbow arrowColor, TextBlock tbback) {
			super(diamond2, diamond1);
			this.arrowColor = arrowColor;
			this.tbback = tbback;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToRight())
					.emphasizeDirection(Direction.UP).withLabel(tbback, arrowHorizontalAlignment());
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double xmin = -Hexagon.hexagonHalfSize;
			snake.addPoint(xmin, y1);
			snake.addPoint(xmin, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToLeft())
					.emphasizeDirection(Direction.UP).withLabel(tbback, arrowHorizontalAlignment());
			final XDimension2D dimRepeat = repeat.calculateDimension(stringBounder);

			XPoint2D p1 = getP1(stringBounder);
			XPoint2D p2 = getP2(stringBounder);
			p1 = translate1.getTranslated(p1);
			p2 = translate2.getTranslated(p2);
			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double xmax = p1.getX() + dimDiamond2.getWidth() / 2 + dimRepeat.getWidth() / 2
					+ Hexagon.hexagonHalfSize;
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	class ConnectionBackSimple2 extends AbstractConnection implements ConnectionTranslatable {
		private final Rainbow arrowColor;
		private final TextBlock tbback;

		public ConnectionBackSimple2(Rainbow arrowColor, TextBlock tbback) {
			super(diamond2, diamond1);
			this.arrowColor = arrowColor;
			this.tbback = tbback;
		}

		private XPoint2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		private XPoint2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(new XPoint2D(0, 0));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = Snake.create(skinParam(), arrowColor, skinParam().arrows().asToLeft())
					.emphasizeDirection(Direction.UP).withLabel(tbback, arrowHorizontalAlignment());
			final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
			final XPoint2D p1 = getP1(stringBounder);
			final XPoint2D p2 = getP2(stringBounder);
			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double xmax = dimTotal.getWidth() - Hexagon.hexagonHalfSize;
			snake.addPoint(xmax, y1);
			snake.addPoint(xmax, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

		@Override
		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			final StringBounder stringBounder = ug.getStringBounder();

			XPoint2D p1 = getP1(stringBounder);
			XPoint2D p2 = getP2(stringBounder);
			p1 = translate1.getTranslated(p1);
			p2 = translate2.getTranslated(p2);
			final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
			final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;

			final double x2a = p2.getX();
			final double x2b = p2.getX() + dimDiamond1.getWidth();
			final boolean isOnA = x1 < (x2a + x2b) / 2;

			final double x2 = isOnA ? x2a : x2b;
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			final UPolygon arrow = isOnA ? skinParam().arrows().asToRight() : skinParam().arrows().asToLeft();
			final Snake snake = Snake.create(skinParam(), arrowColor, arrow).emphasizeDirection(Direction.UP)
					.withLabel(tbback, arrowHorizontalAlignment());

			snake.addPoint(x1, y1);
			final double xmiddle = (x1 + x2) / 2;
			snake.addPoint(xmiddle, y1);
			snake.addPoint(xmiddle, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
		}

	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.apply(getTranslateForRepeat(stringBounder)).draw(repeat);
		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
		if (backward != null)
			ug.apply(getTranslateBackward(stringBounder)).draw(backward);

	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new FtileGeometry(dimTotal, getLeft(stringBounder), 0, dimTotal.getHeight());
	}

	private XDimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final XDimension2D dimRepeat = repeat.calculateDimension(stringBounder);

		final double w = tbTest.calculateDimension(stringBounder).getWidth();

		double width = getLeft(stringBounder) + getRight(stringBounder);
		width = Math.max(width, w + 2 * Hexagon.hexagonHalfSize);
		if (backward != null)
			width += backward.calculateDimension(stringBounder).getWidth();

		final double height = dimDiamond1.getHeight() + dimRepeat.getHeight() + dimDiamond2.getHeight()
				+ 8 * Hexagon.hexagonHalfSize;
		return new XDimension2D(width + 2 * Hexagon.hexagonHalfSize, height);

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == repeat)
			return getTranslateForRepeat(stringBounder);

		if (child == diamond1)
			return getTranslateDiamond1(stringBounder);

		throw new UnsupportedOperationException();
	}

	private UTranslate getTranslateForRepeat(StringBounder stringBounder) {

		final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final XDimension2D dimRepeat = repeat.calculateDimension(stringBounder);
		final double space = dimTotal.getHeight() - dimDiamond1.getHeight() - dimDiamond2.getHeight()
				- dimRepeat.getHeight();
		final double y = dimDiamond1.getHeight() + space / 2;
		final double left = getLeft(stringBounder);
		return new UTranslate(left - repeat.calculateDimension(stringBounder).getLeft(), y);

	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double left = getLeft(stringBounder);
		return UTranslate.dx(left - dimDiamond1.getWidth() / 2);
	}

	private UTranslate getTranslateBackward(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final XDimension2D dimBackward = backward.calculateDimension(stringBounder);
		final double x = dimTotal.getWidth() - dimBackward.getWidth();
		final double y = (dimTotal.getHeight() - dimBackward.getHeight()) / 2;

		return new UTranslate(x, y);
	}

	private UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final XDimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double left = getLeft(stringBounder);
		return new UTranslate(left - dimDiamond2.getWidth() / 2, y2);
	}

	private double getLeft(StringBounder stringBounder) {
		final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		double left1 = repeat.calculateDimension(stringBounder).getLeft();
		left1 = Math.max(left1, dimDiamond1.getWidth() / 2);
		double left2 = repeat.calculateDimension(stringBounder).getLeft();
		left2 = Math.max(left2, dimDiamond2.getWidth() / 2);
		return Math.max(left1, left2);
	}

	private double getRight(StringBounder stringBounder) {
		final XDimension2D dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final XDimension2D dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final XDimension2D dimRepeat = repeat.calculateDimension(stringBounder);
		double right1 = dimRepeat.getWidth() - repeat.calculateDimension(stringBounder).getLeft();
		right1 = Math.max(right1, dimDiamond1.getWidth() / 2);
		double right2 = dimRepeat.getWidth() - repeat.calculateDimension(stringBounder).getLeft();
		right2 = Math.max(right2, dimDiamond2.getWidth() / 2);
		return Math.max(right1, right2);
	}

}
