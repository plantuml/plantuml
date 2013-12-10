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
import java.util.Set;

import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorateIn;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
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
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileWhile extends AbstractFtile {

	private final Ftile whileBlock;

	public Set<Swimlane> getSwimlanes() {
		return whileBlock.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return whileBlock.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	private FtileWhile(Ftile whileBlock) {
		super(whileBlock.shadowing());
		this.whileBlock = whileBlock;

	}

	private static TextBlock createLabel1(Display test, Display yes, UFont font, SpriteContainer spriteContainer) {
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		final TextBlock tmpb = TextBlockUtils.create(yes, fc, HorizontalAlignment.LEFT, spriteContainer);
		if (test == null) {
			return tmpb;
		}
		return TextBlockUtils.mergeTB(TextBlockUtils.create(test, fc, HorizontalAlignment.LEFT, spriteContainer), tmpb,
				HorizontalAlignment.CENTER);
	}

	public static Ftile create(Ftile whileBlock, Display test, HtmlColor borderColor, HtmlColor backColor,
			HtmlColor arrowColor, Display yes, Display out2, UFont font, HtmlColor endInlinkColor,
			LinkRendering afterEndwhile, FtileFactory ftileFactory) {

		final TextBlock label1 = createLabel1(test, yes, font, ftileFactory);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		final TextBlock out = TextBlockUtils.create(out2, fc, HorizontalAlignment.LEFT, ftileFactory);

		final Ftile diamond = new FtileDiamond(whileBlock.shadowing(), backColor, borderColor,
				whileBlock.getSwimlaneOut()).withNorth(label1).withWest(out);
		final FtileWhile wh = new FtileWhile(whileBlock);
		HtmlColor afterEndwhileColor = arrowColor;
		if (afterEndwhile != null && afterEndwhile.getColor() != null) {
			afterEndwhileColor = afterEndwhile.getColor();
		}

		Ftile result = ftileFactory.assembly(diamond, new FtileDecorateIn(wh, whileBlock.getInLinkRendering()));
		result = FtileUtils.addConnection(result, new ConnectionBack(result, diamond, endInlinkColor));
		result = new FtileAssemblySimple(result, new FtileEmpty(wh.shadowing(), 0, 2 * Diamond.diamondHalfSize, null,
				result.getSwimlaneOut()));
		result = FtileUtils.addConnection(result, new ConnectionOut(result, diamond, afterEndwhileColor));

		return FtileUtils.withSwimlaneOut(result, result.getSwimlaneIn());
	}

	static class ConnectionBack extends AbstractConnection {
		final private Ftile full;
		private final HtmlColor endInlinkColor;

		public ConnectionBack(Ftile result, Ftile diamond, HtmlColor endInlinkColor) {
			super(diamond, diamond);
			this.full = result;
			this.endInlinkColor = endInlinkColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();

			final Point2D pout = full.getPointOut(stringBounder);

			final Snake snake = new Snake(endInlinkColor, Arrows.asToLeft());
			final Dimension2D dimTotal = full.asTextBlock().calculateDimension(stringBounder);

			final double posY1 = pout.getY();
			snake.addPoint(pout.getX(), posY1);
			snake.addPoint(pout.getX(), posY1 + Diamond.diamondHalfSize);
			final double posX = dimTotal.getWidth() + Diamond.diamondHalfSize;
			snake.addPoint(posX, posY1 + Diamond.diamondHalfSize);
			final double posY2 = Diamond.diamondHalfSize;
			snake.addPoint(posX, posY2);
			snake.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, posY2);

			ug = ug.apply(new UChangeColor(endInlinkColor)).apply(new UChangeBackColor(endInlinkColor));
			ug.apply(new UTranslate(posX, posY2 + (posY1 - posY2) / 2)).draw(Arrows.asToUp());
			ug.draw(snake);

			ug.apply(new UTranslate(dimTotal.getWidth() / 2, posY1 + Diamond.diamondHalfSize)).draw(
					new UEmpty(5, Diamond.diamondHalfSize));

		}
	}

	static class ConnectionOut extends AbstractConnection {
		private final HtmlColor afterEndwhileColor;
		private final Ftile full;

		public ConnectionOut(Ftile full, Ftile diamond, HtmlColor afterEndwhileColor) {
			super(diamond, diamond);
			this.afterEndwhileColor = afterEndwhileColor;
			this.full = full;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Snake snake = new Snake(afterEndwhileColor);
			final Dimension2D dimTotal = full.asTextBlock().calculateDimension(stringBounder);
			final double posY1 = Diamond.diamondHalfSize;
			snake.addPoint(dimTotal.getWidth() / 2 - Diamond.diamondHalfSize, posY1);
			final double posX = -Diamond.diamondHalfSize;
			snake.addPoint(posX, posY1);
			final double posY2 = dimTotal.getHeight();
			snake.addPoint(posX, posY2);
			snake.addPoint(dimTotal.getWidth() / 2, posY2);

			ug = ug.apply(new UChangeColor(afterEndwhileColor)).apply(new UChangeBackColor(afterEndwhileColor));
			ug.apply(new UTranslate(posX, posY1 + (posY2 - posY1) / 2)).draw(Arrows.asToDown());
			ug.draw(snake);
		}
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				ug.draw(whileBlock);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return whileBlock.asTextBlock().calculateDimension(stringBounder);
			}
		};
	}

	public boolean isKilled() {
		return false;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, 0);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, dimTotal.getHeight());
	}

}
