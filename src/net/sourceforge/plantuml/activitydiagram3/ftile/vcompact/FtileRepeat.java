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
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileRepeat extends AbstractFtile {

	private final Ftile repeat;

	private final TextBlock test;

	private final HtmlColor borderColor;
	private final HtmlColor backColor;

	private FtileRepeat(Ftile repeat, Display test, HtmlColor borderColor, HtmlColor backColor, UFont font,
			SpriteContainer spriteContainer) {
		super(repeat.shadowing());
		this.repeat = repeat;
		this.borderColor = borderColor;
		this.backColor = backColor;
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		this.test = TextBlockUtils.create(test, fc, HorizontalAlignment.LEFT, spriteContainer);
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

	public static Ftile create(Ftile repeat, Display test, HtmlColor borderColor, HtmlColor backColor, UFont font,
			HtmlColor arrowColor, HtmlColor endRepeatLinkColor, SpriteContainer spriteContainer) {
		final FtileRepeat result = new FtileRepeat(repeat, test, borderColor, backColor, font, spriteContainer);
		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionIn(LinkRendering.getColor(repeat.getInLinkRendering(), arrowColor)));
		conns.add(result.new ConnectionBack(arrowColor));
		conns.add(result.new ConnectionOut(LinkRendering.getColor(endRepeatLinkColor, arrowColor)));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection {
		private final HtmlColor arrowColor;

		public ConnectionIn(HtmlColor arrowColor) {
			super(null, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate translate = getTranslate(stringBounder);

			final Point2D pIn = translate.getTranslated(repeat.getPointIn(stringBounder));
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			snake.addPoint(dimTotal.getWidth() / 2, 2 * Diamond.diamondHalfSize);
			snake.addPoint(pIn.getX(), pIn.getY());

			ug.draw(snake);
		}
	}

	class ConnectionBack extends AbstractConnection {
		private final HtmlColor arrowColor;

		public ConnectionBack(HtmlColor arrowColor) {
			super(null, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Snake snake = new Snake(arrowColor, Arrows.asToLeft());
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			snake.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, dimTotal.getHeight()
					- Diamond.diamondHalfSize);
			snake.addPoint(dimTotal.getWidth() - Diamond.diamondHalfSize, dimTotal.getHeight()
					- Diamond.diamondHalfSize);
			snake.addPoint(dimTotal.getWidth() - Diamond.diamondHalfSize, Diamond.diamondHalfSize);
			snake.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize);

			ug = ug.apply(new UChangeColor(arrowColor)).apply(new UChangeBackColor(arrowColor));
			ug.apply(new UTranslate(dimTotal.getWidth() - Diamond.diamondHalfSize, dimTotal.getHeight() / 2)).draw(
					Arrows.asToUp());
			ug.apply(new UStroke(1.5)).draw(snake);
		}
	}

	class ConnectionOut extends AbstractConnection {
		private final HtmlColor arrowColor;

		public ConnectionOut(HtmlColor arrowColor) {
			super(null, null);
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate translate = getTranslate(stringBounder);

			final Point2D pIn = translate.getTranslated(repeat.getPointOut(stringBounder));
			final Snake snake = new Snake(arrowColor, Arrows.asToDown());
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			snake.addPoint(pIn.getX(), pIn.getY());
			snake.addPoint(dimTotal.getWidth() / 2, dimTotal.getHeight() - 2 * Diamond.diamondHalfSize);

			ug.draw(snake);
		}
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				ug.apply(getTranslate(stringBounder)).draw(repeat);

				final double xDiamond = (dimTotal.getWidth() - 2 * Diamond.diamondHalfSize) / 2;
				drawDiamond(ug, xDiamond, 0);
				drawDiamond(ug, xDiamond, dimTotal.getHeight() - 2 * Diamond.diamondHalfSize);

				final Dimension2D dimTest = test.calculateDimension(stringBounder);
				test.drawU(ug.apply(new UTranslate(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, dimTotal
						.getHeight() - Diamond.diamondHalfSize - dimTest.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}

		};
	}

	public boolean isKilled() {
		return false;
	}

	private void drawDiamond(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		ug.apply(new UChangeColor(borderColor)).apply(new UStroke(1.5)).apply(new UChangeBackColor(backColor))
				.apply(new UTranslate(xTheoricalPosition, yTheoricalPosition)).draw(Diamond.asPolygon(shadowing()));
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D dim = repeat.asTextBlock().calculateDimension(stringBounder);
		dim = Dimension2DDouble.delta(dim, 2 * getDeltaX(stringBounder), 8 * Diamond.diamondHalfSize);
		return dim;
	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		return new UTranslate(getDeltaX(stringBounder), 4 * Diamond.diamondHalfSize);
	}

	private double getDeltaX(StringBounder stringBounder) {
		final double w = test.calculateDimension(stringBounder).getWidth();
		if (w < 2 * Diamond.diamondHalfSize) {
			return 2 * Diamond.diamondHalfSize;
		}
		return w + Diamond.diamondHalfSize;
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
