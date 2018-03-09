/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.ugraphic.UFont;

public abstract class AbstractTextualComponent extends AbstractComponent {

	private final Display strings;

	private final int marginX1;
	private final int marginX2;
	private final int marginY;

	private final TextBlock textBlock;

	private final UFont font;
	private final HtmlColor fontColor;

	public AbstractTextualComponent(LineBreakStrategy maxMessageSize, CharSequence label, FontConfiguration font,
			HorizontalAlignment horizontalAlignment, int marginX1, int marginX2, int marginY,
			ISkinSimple spriteContainer, UFont fontForStereotype, HtmlColor htmlColorForStereotype) {
		this(maxMessageSize, Display.getWithNewlines(label == null ? "" : label.toString()), font, horizontalAlignment,
				marginX1, marginX2, marginY, spriteContainer, false, fontForStereotype, htmlColorForStereotype);
	}

	public AbstractTextualComponent(LineBreakStrategy maxMessageSize, Display strings, FontConfiguration font,
			HorizontalAlignment horizontalAlignment, int marginX1, int marginX2, int marginY,
			ISkinSimple spriteContainer, boolean enhanced, UFont fontForStereotype, HtmlColor htmlColorForStereotype) {
		this.font = font.getFont();
		this.fontColor = font.getColor();
		this.marginX1 = marginX1;
		this.marginX2 = marginX2;
		this.marginY = marginY;
		this.strings = strings;

		if (strings.size() == 1 && strings.get(0).length() == 0) {
			textBlock = new TextBlockEmpty();
		} else if (enhanced) {
			textBlock = new BodyEnhanced2(strings, FontParam.NOTE, spriteContainer, HorizontalAlignment.LEFT, font,
					maxMessageSize);
		} else {
			textBlock = strings.create(font, horizontalAlignment, spriteContainer, maxMessageSize, CreoleMode.FULL,
					fontForStereotype, htmlColorForStereotype);
		}
	}

	final protected TextBlock getTextBlock() {
		return textBlock;
	}

	protected double getPureTextWidth(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getWidth();
	}

	final public double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + marginX1 + marginX2;
	}

	// // For cache
	// private Dimension2D size;
	//
	// private Dimension2D getSize(StringBounder stringBounder, final TextBlock textBlock) {
	// if (size == null) {
	// size = textBlock.calculateDimension(stringBounder);
	// }
	// return size;
	// }

	final protected double getTextHeight(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getHeight() + 2 * marginY;
	}

	final protected Display getLabels() {
		return strings;
	}

	final protected int getMarginX1() {
		return marginX1;
	}

	final protected int getMarginX2() {
		return marginX2;
	}

	final protected int getMarginY() {
		return marginY;
	}

	final protected UFont getFont() {
		return font;
	}

	protected HtmlColor getFontColor() {
		return fontColor;
	}

}
