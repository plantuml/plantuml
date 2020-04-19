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
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorBackground;

public class ComponentRoseGroupingHeader extends AbstractTextualComponent {

	private final int cornersize = 10;
	private final int commentMargin = 0; // 8;

	private final TextBlock commentTextBlock;

	private final HColor background;
	private final SymbolContext symbolContext;
	private final SymbolContext symbolContextCorner;
	private final double roundCorner;

	public ComponentRoseGroupingHeader(Style style, Style styleHeader, HColor background,
			SymbolContext symbolContext, FontConfiguration bigFont, FontConfiguration smallFont2, Display strings,
			ISkinSimple spriteContainer, double roundCorner) {
		super(styleHeader, LineBreakStrategy.NONE, strings.get(0), bigFont, HorizontalAlignment.LEFT, 15, 30, 1,
				spriteContainer, null, null);

		if (SkinParam.USE_STYLES()) {
			this.roundCorner = style.value(PName.RoundCorner).asInt();
			this.background = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
			this.symbolContext = style.getSymbolContext(getIHtmlColorSet());
			this.symbolContextCorner = styleHeader.getSymbolContext(getIHtmlColorSet());
			bigFont = style.getFontConfiguration(getIHtmlColorSet());
			smallFont2 = style.getFontConfiguration(getIHtmlColorSet());
		} else {
			this.roundCorner = roundCorner;
			this.background = background;
			this.symbolContext = symbolContext;
			this.symbolContextCorner = symbolContext;
		}
		if (strings.size() == 1 || strings.get(1) == null) {
			this.commentTextBlock = null;
		} else {
			final Display display = Display.getWithNewlines("[" + strings.get(1) + "]");
			// final FontConfiguration smallFont2 = bigFont.forceFont(smallFont, null);
			this.commentTextBlock = display.create(smallFont2, HorizontalAlignment.LEFT, spriteContainer);
		}
		if (this.background == null) {
			throw new IllegalArgumentException();
		}
	}

	// new FontConfiguration(smallFont, bigFont.getColor(),
	// bigFont.getHyperlinkColor(),
	// bigFont.useUnderlineForHyperlink());

	private double getSuppHeightForComment(StringBounder stringBounder) {
		if (commentTextBlock == null) {
			return 0;
		}
		final double height = commentTextBlock.calculateDimension(stringBounder).getHeight();
		if (height > 15) {
			return height - 15;
		}
		return 0;

	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double sup;
		if (commentTextBlock == null) {
			sup = commentMargin * 2;
		} else {
			final Dimension2D size = commentTextBlock.calculateDimension(stringBounder);
			sup = getMarginX1() + commentMargin + size.getWidth();

		}
		return getTextWidth(stringBounder) + sup;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY() + getSuppHeightForComment(stringBounder);
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		if (background instanceof HColorBackground) {
			return;
		}
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = symbolContext.applyStroke(ug).apply(symbolContext.getForeColor());
		final URectangle rect = new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight())
				.rounded(roundCorner);
		rect.setDeltaShadow(symbolContext.getDeltaShadow());
		ug.apply(background.bg()).draw(rect);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int textWidth = (int) getTextWidth(stringBounder);
		final int textHeight = (int) getTextHeight(stringBounder);

		if (SkinParam.USE_STYLES()) {
			symbolContextCorner.apply(ug).draw(getCorner(textWidth, textHeight));
		} else {
			symbolContextCorner.applyColors(ug).draw(getCorner(textWidth, textHeight));
		}

		ug = symbolContext.applyStroke(ug).apply(symbolContext.getForeColor());
		final URectangle rect = new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight())
				.rounded(roundCorner);
		ug.draw(rect);

		ug = ug.apply(new UStroke());

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));

		if (commentTextBlock != null) {
			final int x1 = getMarginX1() + textWidth;
			final int y2 = getMarginY() + 1;

			commentTextBlock.drawU(ug.apply(new UTranslate(x1 + commentMargin, y2)));
		}
	}

	private UPath getCorner(final double width, final double height) {
		final UPath polygon = new UPath();
		if (roundCorner == 0) {
			polygon.moveTo(0, 0);
			polygon.lineTo(width, 0);

			polygon.lineTo(width, height - cornersize);
			polygon.lineTo(width - cornersize, height);

			polygon.lineTo(0, height);
			polygon.lineTo(0, 0);
		} else {
			polygon.moveTo(roundCorner / 2, 0);
			polygon.lineTo(width, 0);

			polygon.lineTo(width, height - cornersize);
			polygon.lineTo(width - cornersize, height);

			polygon.lineTo(0, height);
			polygon.lineTo(0, roundCorner / 2);
		}
		return polygon;
	}

}
