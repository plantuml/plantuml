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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockEmpty;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public abstract class AbstractTextualComponent extends AbstractComponent {

	private final TextBlock textBlock;

	private final ClockwiseTopRightBottomLeft padding;

	public AbstractTextualComponent(Style style, LineBreakStrategy maxMessageSize, ClockwiseTopRightBottomLeft padding,
			ISkinParam skinParam, CharSequence label) {
		this(style, style, maxMessageSize, padding, skinParam,
				Display.getWithNewlines(skinParam.getPragma(), label == null ? "" : label.toString()), false);
	}

	public AbstractTextualComponent(Style style, LineBreakStrategy maxMessageSize, ClockwiseTopRightBottomLeft padding,
			ISkinParam skinParam, Display display, boolean enhanced) {
		this(style, style, maxMessageSize, padding, skinParam, display, enhanced);
	}

	public AbstractTextualComponent(Style style, Style stereo, LineBreakStrategy maxMessageSize,
			ClockwiseTopRightBottomLeft padding, ISkinParam skinParam, Display display, boolean enhanced) {
		super(style, skinParam);

		// this.padding = ClockwiseTopRightBottomLeft.topRightBottomLeft(marginY,
		// marginX2, marginY, marginX1);
		this.padding = padding;

		final FontConfiguration fc = getFontConfiguration();
		final HorizontalAlignment horizontalAlignment = getHorizontalAlignment();
		final UFont fontForStereotype = stereo.getUFont();
		final HColor htmlColorForStereotype = stereo.value(PName.FontColor).asColor(getIHtmlColorSet());
		display = display.withoutStereotypeIfNeeded(style);

		if (display.size() == 1 && display.get(0).length() == 0)
			textBlock = new TextBlockEmpty();
		else if (enhanced)
			textBlock = BodyFactory.create3(display, skinParam, horizontalAlignment, fc, maxMessageSize, style);
		else
			textBlock = display.create0(fc, horizontalAlignment, skinParam, maxMessageSize, CreoleMode.FULL,
					fontForStereotype, htmlColorForStereotype, padding.getLeft(), padding.getRight());

	}

	protected TextBlock getTextBlock() {
		return textBlock;
	}

	protected double getPureTextWidth(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final XDimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getWidth();
	}

	final public double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + getOldPaddingX1() + getOldPaddingX2();
	}

	final protected double getTextHeight(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final XDimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getHeight() + padding.getTop() + padding.getBottom();
	}

	final protected double getOldPaddingX1() {
		return padding.getLeft();
	}

	final protected double getOldPaddingX2() {
		return padding.getRight();
	}

	final protected double getOldPaddingY() {
		return padding.getTop();
	}

	final protected ClockwiseTopRightBottomLeft getPadding() {
		return padding;
	}

}
