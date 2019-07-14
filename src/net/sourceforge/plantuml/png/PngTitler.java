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
package net.sourceforge.plantuml.png;

import java.awt.Font;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PngTitler {

	private final HtmlColor textColor;
	private final HtmlColor hyperlinkColor;
	private final DisplaySection text;
	private final int fontSize;
	private final String fontFamily;
	private final boolean useUnderlineForHyperlink;
	private final Style style;
	private final IHtmlColorSet set;
	private final ISkinSimple spriteContainer;

	public PngTitler(HtmlColor textColor, DisplaySection text, int fontSize, String fontFamily,
			HtmlColor hyperlinkColor, boolean useUnderlineForHyperlink, Style style, IHtmlColorSet set,
			ISkinSimple spriteContainer) {
		this.style = style;
		this.set = set;
		this.spriteContainer = spriteContainer;

		if (SkinParam.USE_STYLES()) {
			textColor = style.value(PName.FontColor).asColor(set);
			fontSize = style.value(PName.FontSize).asInt();
			fontFamily = style.value(PName.FontName).asString();
			hyperlinkColor = style.value(PName.HyperLinkColor).asColor(set);
		}
		this.textColor = textColor;
		this.text = text;
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
		this.hyperlinkColor = hyperlinkColor;
		this.useUnderlineForHyperlink = useUnderlineForHyperlink;

	}

	public Dimension2D getTextDimension(StringBounder stringBounder) {
		final TextBlock textBloc = getRibbonBlock();
		if (textBloc == null) {
			return null;
		}
		return textBloc.calculateDimension(stringBounder);
	}

	public TextBlock getRibbonBlock() {
		if (SkinParam.USE_STYLES()) {
			final Display display = text.getDisplay();
			if (display == null) {
				return null;
			}
			return style.createTextBlockBordered(display, set, spriteContainer);
		}
		final UFont normalFont = new UFont(fontFamily, Font.PLAIN, fontSize);
		return text.createRibbon(
				new FontConfiguration(normalFont, textColor, hyperlinkColor, useUnderlineForHyperlink),
				new SpriteContainerEmpty());
	}
}
