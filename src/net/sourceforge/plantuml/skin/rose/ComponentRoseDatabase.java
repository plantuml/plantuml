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
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ComponentRoseDatabase extends AbstractTextualComponent {

	private final TextBlock stickman;
	private final boolean head;

	public ComponentRoseDatabase(Style style, Style stereo, SymbolContext biColor, FontConfiguration font,
			Display stringsToDisplay, boolean head, ISkinSimple spriteContainer, UFont fontForStereotype,
			HColor htmlColorForStereotype) {
		super(style, stereo, LineBreakStrategy.NONE, stringsToDisplay, font, HorizontalAlignment.CENTER, 3, 3, 0,
				spriteContainer, false, fontForStereotype, htmlColorForStereotype);
		this.head = head;
		if (SkinParam.USE_STYLES()) {
			biColor = style.getSymbolContext(getIHtmlColorSet());
		}

		final SymbolContext symbolContext = new SymbolContext(biColor.getBackColor(), biColor.getForeColor())
				.withStroke(new UStroke(1.5)).withShadow(biColor.getDeltaShadow());
		this.stickman = USymbol.DATABASE.asSmall(null, TextBlockUtils.empty(16, 17), TextBlockUtils.empty(0, 0),
				symbolContext, HorizontalAlignment.CENTER);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimStickman = stickman.calculateDimension(stringBounder);
		final double delta = (getPreferredWidth(stringBounder) - dimStickman.getWidth()) / 2;

		if (head) {
			textBlock.drawU(ug.apply(new UTranslate(getTextMiddlePostion(stringBounder), dimStickman.getHeight())));
			ug = ug.apply(UTranslate.dx(delta));
		} else {
			textBlock.drawU(ug.apply(UTranslate.dx(getTextMiddlePostion(stringBounder))));
			ug = ug.apply(new UTranslate(delta, getTextHeight(stringBounder)));
		}
		stickman.drawU(ug);
	}

	private double getTextMiddlePostion(StringBounder stringBounder) {
		return (getPreferredWidth(stringBounder) - getTextWidth(stringBounder)) / 2.0;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		final Dimension2D dimStickman = stickman.calculateDimension(stringBounder);
		return dimStickman.getHeight() + getTextHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		final Dimension2D dimStickman = stickman.calculateDimension(stringBounder);
		return Math.max(dimStickman.getWidth(), getTextWidth(stringBounder));
	}

}
