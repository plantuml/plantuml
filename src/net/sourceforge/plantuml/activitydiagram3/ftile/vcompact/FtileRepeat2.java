/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.MathUtils;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondFoo1;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileRepeat2 extends AbstractFtile {

	private final Ftile repeat;
	private final Ftile diamond1;
	private final Ftile diamond2;
	private final TextBlock tbTest;

	private FtileRepeat2(Ftile repeat, Ftile diamond1, Ftile diamond2, TextBlock tbTest) {
		super(repeat.shadowing());
		this.repeat = repeat;
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
		this.tbTest = tbTest;
	}

	public Swimlane getSwimlaneIn() {
		return repeat.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	public Set<Swimlane> getSwimlanes() {
		return repeat.getSwimlanes();
	}

	public static Ftile create(Swimlane swimlane, Ftile repeat, Display test, HtmlColor borderColor,
			HtmlColor backColor, UFont font, HtmlColor arrowColor, HtmlColor endRepeatLinkColor,
			ConditionStyle conditionStyle, SpriteContainer spriteContainer) {

		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		final TextBlock tbTest = TextBlockUtils.create(test, fc, HorizontalAlignment.LEFT, spriteContainer);

		final Ftile diamond1 = new FtileDiamond(repeat.shadowing(), backColor, borderColor, swimlane);
		final FtileRepeat2 result;
		if (conditionStyle == ConditionStyle.INSIDE) {
			final Ftile diamond2 = new FtileDiamondInside(repeat.shadowing(), backColor, borderColor, swimlane, tbTest);
			result = new FtileRepeat2(repeat, diamond1, diamond2, TextBlockUtils.empty(0, 0));
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			final Ftile diamond2 = new FtileDiamond(repeat.shadowing(), backColor, borderColor, swimlane)
					.withEast(tbTest);
			result = new FtileRepeat2(repeat, diamond1, diamond2, tbTest);
		} else if (conditionStyle == ConditionStyle.FOO1) {
			final Ftile diamond2 = new FtileDiamondFoo1(repeat.shadowing(), backColor, borderColor, swimlane, tbTest);
			result = new FtileRepeat2(repeat, diamond1, diamond2, TextBlockUtils.empty(0, 0));
		} else {
			throw new IllegalStateException();
		}

		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionIn(LinkRendering.getColor(repeat.getInLinkRendering(), arrowColor)));
		conns.add(result.new ConnectionBack(arrowColor));
		conns.add(result.new ConnectionOut(LinkRendering.getColor(endRepeatLinkColor, arrowColor)));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection {
		private final HtmlColor arrowColor;

		public ConnectionIn(HtmlColor arrowColor) {
			super(diamond1, repeat);
			this.arrowColor = arrowColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateDiamond1(stringBounder).getTranslated(getFtile1().getPointOut(stringBounder));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateForRepeat(stringBounder).getTranslated(getFtile2().getPointIn(stringBounder));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}
	}

	class ConnectionOut extends AbstractConnection {
		private final HtmlColor arrowColor;

		public ConnectionOut(HtmlColor arrowColor) {
			super(repeat, diamond2);
			this.arrowColor = arrowColor;
		}

		private Point2D getP1(final StringBounder stringBounder) {
			return getTranslateForRepeat(stringBounder).getTranslated(getFtile1().getPointOut(stringBounder));
		}

		private Point2D getP2(final StringBounder stringBounder) {
			return getTranslateDiamond2(stringBounder).getTranslated(getFtile2().getPointIn(stringBounder));
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			snake.addPoint(getP1(stringBounder));
			snake.addPoint(getP2(stringBounder));

			ug.draw(snake);
		}
	}

	class ConnectionBack extends AbstractConnection {
		private final HtmlColor arrowColor;

		public ConnectionBack(HtmlColor arrowColor) {
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
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowColor, Arrows.asToLeft());
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			final Point2D p1 = getP1(stringBounder);
			final Point2D p2 = getP2(stringBounder);
			final Dimension2D dimDiamond1 = diamond1.asTextBlock().calculateDimension(stringBounder);
			final Dimension2D dimDiamond2 = diamond2.asTextBlock().calculateDimension(stringBounder);
			final double x1 = p1.getX() + dimDiamond2.getWidth();
			final double y1 = p1.getY() + dimDiamond2.getHeight() / 2;
			final double x2 = p2.getX() + dimDiamond1.getWidth();
			final double y2 = p2.getY() + dimDiamond1.getHeight() / 2;

			snake.addPoint(x1, y1);
			final double yy = dimTotal.getWidth() - Diamond.diamondHalfSize;
			snake.addPoint(yy, y1);
			snake.addPoint(yy, y2);
			snake.addPoint(x2, y2);

			ug.draw(snake);
			ug = ug.apply(new UChangeColor(arrowColor)).apply(new UChangeBackColor(arrowColor));
			ug.apply(new UTranslate(yy, dimTotal.getHeight() / 2)).draw(Arrows.asToUp());
		}

	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				ug.apply(getTranslateForRepeat(stringBounder)).draw(repeat);
				ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
				ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}
		};
	}

	public boolean isKilled() {
		return false;
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimDiamond1 = diamond1.asTextBlock().calculateDimension(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.asTextBlock().calculateDimension(stringBounder);
		final double w = tbTest.calculateDimension(stringBounder).getWidth();

		final Dimension2D dim = Dimension2DDouble.atLeast(repeat.asTextBlock().calculateDimension(stringBounder), 2 * w
				+ 2 * Diamond.diamondHalfSize, 0);
		final double width = MathUtils.max(dimDiamond1.getWidth(), dim.getWidth(), dimDiamond2.getWidth());
		final double height = dimDiamond1.getHeight() + dim.getHeight() + dimDiamond2.getHeight();

		final Dimension2D result = new Dimension2DDouble(width, height);
		return Dimension2DDouble.delta(result, 4 * Diamond.diamondHalfSize, 8 * Diamond.diamondHalfSize);

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
		final Dimension2D dimDiamond1 = diamond1.asTextBlock().calculateDimension(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.asTextBlock().calculateDimension(stringBounder);
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimRepeat = repeat.asTextBlock().calculateDimension(stringBounder);

		final double x = (dimTotal.getWidth() - dimRepeat.getWidth()) / 2;
		final double y = (dimTotal.getHeight() - dimDiamond1.getHeight() - dimDiamond2.getHeight() - dimRepeat
				.getHeight()) / 2;
		return new UTranslate(x, y);

	}

	private UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimDiamond1 = diamond1.asTextBlock().calculateDimension(stringBounder);

		final double x1 = (dimTotal.getWidth() - dimDiamond1.getWidth()) / 2;
		final double y1 = 0;
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimDiamond2 = diamond2.asTextBlock().calculateDimension(stringBounder);

		final double x2 = (dimTotal.getWidth() - dimDiamond2.getWidth()) / 2;
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		return new UTranslate(x2, y2);
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, 0);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, dimTotal.getHeight());
	}

}
