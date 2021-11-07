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

import java.awt.geom.Point2D;

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

public class GConnectionVerticalDownThenHorizontal extends GAbstractConnection {

	private final TextBlock textBlock;
	private final UTranslate pos1;
	private final UTranslate pos2;

	public GConnectionVerticalDownThenHorizontal(UTranslate pos1, GPoint gpoint1, UTranslate pos2, GPoint gpoint2,
			TextBlock textBlock) {
		super(gpoint1, gpoint2);
		this.textBlock = textBlock;
		this.pos1 = pos1;
		this.pos2 = pos2;
		// See FtileFactoryDelegatorAssembly
	}

	@Override
	public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
		Point2D p1 = pos1.getTranslated(gpoint1.getPoint2D());
		Point2D p2 = pos2.getTranslated(gpoint2.getPoint2D());

		final Direction originalDirection = Direction.leftOrRight(p1, p2);

//		p1 = translate1.getTranslated(p1);
//		p2 = translate2.getTranslated(p2);

		final double x1 = p1.getX();
		final double x2 = p2.getX();
		final Point2D mp1a = translate1.getTranslated(p1);
		final Point2D mp2b = translate2.getTranslated(p2);
		final Direction newDirection = Direction.leftOrRight(mp1a, mp2b);
		final UPolygon arrow = x2 > x1 ? Arrows.asToRight() : Arrows.asToLeft();
		if (originalDirection == newDirection) {
			final double delta = (x2 > x1 ? -1 : 1) * 1.5 * Hexagon.hexagonHalfSize;
			final Point2D mp2bc = new Point2D.Double(mp2b.getX() + delta, mp2b.getY());
			final Snake snake = Snake.create(getInLinkRenderingColor()).withMerge(MergeStrategy.LIMITED);
			final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2bc.getX(), middle);
			snake.addPoint(mp2bc);
			ug.draw(snake);
			final Snake small = Snake.create(getInLinkRenderingColor(), arrow).withMerge(MergeStrategy.LIMITED);
			small.addPoint(mp2bc);
			small.addPoint(mp2bc.getX(), mp2b.getY());
			small.addPoint(mp2b);
			ug.draw(small);
		} else {
			final double delta = (x2 > x1 ? -1 : 1) * 1.5 * Hexagon.hexagonHalfSize;
			final Point2D mp2bb = new Point2D.Double(mp2b.getX() + delta, mp2b.getY() - 1.5 * Hexagon.hexagonHalfSize);
			final Snake snake = Snake.create(getInLinkRenderingColor()).withMerge(MergeStrategy.LIMITED);
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), mp2bb.getY());
			snake.addPoint(mp2bb);
			ug.draw(snake);
			final Snake small = Snake.create(getInLinkRenderingColor(), arrow).withMerge(MergeStrategy.LIMITED);
			small.addPoint(mp2bb);
			small.addPoint(mp2bb.getX(), mp2b.getY());
			small.addPoint(mp2b);
			ug.draw(small);

		}

	}

	@Override
	public void drawU(UGraphic ug) {
		final Point2D p1 = pos1.getTranslated(gpoint1.getPoint2D());
		final Point2D p2 = pos2.getTranslated(gpoint2.getPoint2D());
		final UPolygon arrow = p1.getX() < p2.getX() ? Arrows.asToRight() : Arrows.asToLeft();
		final Snake snake = Snake.create(getInLinkRenderingColor(), arrow).withLabel(textBlock,
				HorizontalAlignment.LEFT);
		snake.addPoint(p1);
		snake.addPoint(new Point2D.Double(p1.getX(), p2.getY()));
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