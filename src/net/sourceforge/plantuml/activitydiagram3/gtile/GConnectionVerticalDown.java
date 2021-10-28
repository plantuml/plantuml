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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GConnectionVerticalDown extends GtileAssemblySimple implements ConnectionTranslatable {

	private final TextBlock textBlock;

	public GConnectionVerticalDown(Gtile tile1, Gtile tile2, LinkRendering linkRendering) {
		super(tile1, tile2);
		this.textBlock = getTextBlock(linkRendering.getDisplay());
		// See FtileFactoryDelegatorAssembly
	}

	@Override
	protected UTranslate supplementaryMove() {
		final double height = 30 + textBlock.calculateDimension(stringBounder).getHeight();
		System.err.println("height=" + height);
		return new UTranslate(0, height);
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	protected final TextBlock getTextBlock(Display display) {
		// DUP3945
		if (Display.isNull(display)) {
			return TextBlockUtils.EMPTY_TEXT_BLOCK;
		}
		final FontConfiguration fontConfiguration;
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			fontConfiguration = style.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
		} else {
			fontConfiguration = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}
		return display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	@Override
	public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
		throw new UnsupportedOperationException();

	}

//	public void drawU(UGraphic ug) {
//		ug.draw(getSimpleSnake());
//	}
//
//	public double getMaxX(StringBounder stringBounder) {
//		return getSimpleSnake().getMaxX(stringBounder);
//	}
//
//	private Snake getSimpleSnake() {
//		final Snake snake = Snake.create(color, Arrows.asToDown()).withLabel(textBlock, HorizontalAlignment.LEFT);
////		snake.addPoint(p1);
////		snake.addPoint(p2);
//		return snake;
//	}
//
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