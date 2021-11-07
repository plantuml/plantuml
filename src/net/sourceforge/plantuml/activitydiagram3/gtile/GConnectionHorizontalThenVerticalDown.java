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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Hexagon;
import net.sourceforge.plantuml.activitydiagram3.ftile.MergeStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GConnectionHorizontalThenVerticalDown extends GAbstractConnection {

	private final TextBlock textBlock;
	private final UTranslate pos1;
	private final UTranslate pos2;

	public GConnectionHorizontalThenVerticalDown(UTranslate pos1, GPoint gpoint1, UTranslate pos2, GPoint gpoint2,
			TextBlock textBlock) {
		super(gpoint1, gpoint2);
		this.textBlock = textBlock;
		this.pos1 = pos1;
		this.pos2 = pos2;
		// See FtileFactoryDelegatorAssembly
	}

	@Override
	public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
//		final Snake snake = Snake.create(getInLinkRenderingColor(), Arrows.asToDown()).withLabel(textBlock,
//				HorizontalAlignment.LEFT);
		Point2D p1 = pos1.getTranslated(gpoint1.getPoint2D());
		Point2D p2 = pos2.getTranslated(gpoint2.getPoint2D());

		final Direction originalDirection = Direction.leftOrRight(p1, p2);
		p1 = translate1.getTranslated(p1);
		p2 = translate2.getTranslated(p2);
		final Direction newDirection = Direction.leftOrRight(p1, p2);
		if (originalDirection != newDirection) {
			final double delta = (originalDirection == Direction.RIGHT ? -1 : 1) * Hexagon.hexagonHalfSize;
			// final Dimension2D dimDiamond1 =
			// diamond1.calculateDimension(ug.getStringBounder());
			final Dimension2D dimDiamond1 = new Dimension2DDouble(0, 0);
			final Snake small = Snake.create(getInLinkRenderingColor()).withLabel(textBlock, HorizontalAlignment.LEFT);
			small.addPoint(p1);
			small.addPoint(p1.getX() + delta, p1.getY());
			small.addPoint(p1.getX() + delta, p1.getY() + dimDiamond1.getHeight() * .75);
			ug.draw(small);
			p1 = small.getLast();
		}
		UPolygon usingArrow = /* branch.isEmpty() ? null : */ Arrows.asToDown();

		final Snake snake = Snake.create(getInLinkRenderingColor(), usingArrow)
				.withLabel(textBlock, HorizontalAlignment.LEFT).withMerge(MergeStrategy.LIMITED);
		snake.addPoint(p1);
		snake.addPoint(p2.getX(), p1.getY());
		snake.addPoint(p2);
		ug.draw(snake);

	}

	@Override
	public void drawU(UGraphic ug) {
		final Snake snake = Snake.create(getInLinkRenderingColor(), Arrows.asToDown()).withLabel(textBlock,
				HorizontalAlignment.LEFT);
		final Point2D p1 = pos1.getTranslated(gpoint1.getPoint2D());
		final Point2D p2 = pos2.getTranslated(gpoint2.getPoint2D());
		snake.addPoint(p1);
		snake.addPoint(new Point2D.Double(p2.getX(), p1.getY()));
		snake.addPoint(p2);
		ug.draw(snake);
	}

//	public double getMaxX(StringBounder stringBounder) {
//		return getSimpleSnake().getMaxX(stringBounder);
//	}

	private Rainbow getInLinkRenderingColor() {
		Rainbow color;
		color = Rainbow.build(gpoint1.getGtile().skinParam());
//		final LinkRendering linkRendering = tile.getInLinkRendering();
//		if (linkRendering == null) {
//			if (UseStyle.useBetaStyle()) {
//				final Style style = getDefaultStyleDefinitionArrow()
//						.getMergedStyle(skinParam().getCurrentStyleBuilder());
//				return Rainbow.build(style, skinParam().getIHtmlColorSet(), skinParam().getThemeStyle());
//			} else {
//				color = Rainbow.build(skinParam());
//			}
//		} else {
//			color = linkRendering.getRainbow();
//		}
//		if (color.size() == 0) {
//			if (UseStyle.useBetaStyle()) {
//				final Style style = getDefaultStyleDefinitionArrow()
//						.getMergedStyle(skinParam().getCurrentStyleBuilder());
//				return Rainbow.build(style, skinParam().getIHtmlColorSet(), skinParam().getThemeStyle());
//			} else {
//				color = Rainbow.build(skinParam());
//			}
//		}
		return color;
	}

//	@Override
//	public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
//		final Snake snake = Snake.create(color, Arrows.asToDown()).withLabel(textBlock, HorizontalAlignment.LEFT);
//		final Point2D mp1a = translate1.getTranslated(p1);
//		final Point2D mp2b = translate2.getTranslated(p2);
//		final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
//		snake.addPoint(mp1a);
//		snake.addPoint(mp1a.getX(), middle);
//		snake.addPoint(mp2b.getX(), middle);
//		snake.addPoint(mp2b);
//		ug.draw(snake);
//
//	}

}