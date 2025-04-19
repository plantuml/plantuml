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
package net.sourceforge.plantuml.skin.rose;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ComponentRoseGroupingHeader extends AbstractTextualComponent {

	private final int cornersize = 10;
	private final int commentMargin = 0; // 8;

	private final TextBlock commentTextBlock;

	private final HColor background;
	private final Fashion symbolContext;
	private final Fashion symbolContextCorner;
	private final double roundCorner;

	public ComponentRoseGroupingHeader(boolean teoz, Style style, Style styleHeader, Display strings,
			ISkinParam skinParam) {
		super(styleHeader, LineBreakStrategy.NONE, 15, 30, 1, skinParam, strings.get(0));

		this.roundCorner = style.value(PName.RoundCorner).asInt(false);
		this.background = teoz ? HColors.transparent() : style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
		this.symbolContext = style.getSymbolContext(getIHtmlColorSet());
		this.symbolContextCorner = styleHeader.getSymbolContext(getIHtmlColorSet());

		final FontConfiguration smallFont2 = style.getFontConfiguration(getIHtmlColorSet());

		if (strings.size() == 1 || strings.get(1) == null) {
			this.commentTextBlock = null;
		} else {
			final Display display = Display.getWithNewlines(skinParam.getPragma(), "[" + strings.get(1) + "]");
			this.commentTextBlock = display.create(smallFont2, HorizontalAlignment.LEFT, skinParam);
		}
		Objects.requireNonNull(this.background);
	}

	private double getSuppHeightForComment(StringBounder stringBounder) {
		if (commentTextBlock == null)
			return 0;

		final double height = commentTextBlock.calculateDimension(stringBounder).getHeight();
		if (height > 15)
			return height - 15;

		return 0;

	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double sup;
		if (commentTextBlock == null) {
			sup = commentMargin * 2;
		} else {
			final XDimension2D size = commentTextBlock.calculateDimension(stringBounder);
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
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		ug = symbolContext.applyStroke(ug).apply(symbolContext.getForeColor());
		final URectangle rect = URectangle.build(dimensionToUse.getWidth(), dimensionToUse.getHeight())
				.rounded(roundCorner);
		rect.setDeltaShadow(symbolContext.getDeltaShadow());
		ug.apply(background.bg()).draw(rect);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final double textWidth = getTextWidth(stringBounder);
		final double textHeight = getTextHeight(stringBounder);

		symbolContextCorner.apply(ug).draw(getCorner(textWidth, textHeight));

		ug = symbolContext.applyStroke(ug).apply(symbolContext.getForeColor());
		final URectangle rect = URectangle.build(dimensionToUse.getWidth(), dimensionToUse.getHeight())
				.rounded(roundCorner);
		ug.draw(rect);

		ug = ug.apply(UStroke.simple());

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));

		if (commentTextBlock != null) {
			final double x1 = getMarginX1() + textWidth;
			final double y2 = getMarginY() + 1;

			commentTextBlock.drawU(ug.apply(new UTranslate(x1 + commentMargin, y2)));
		}
	}

	private UPath getCorner(final double width, final double height) {
		final UPath polygon = UPath.none();
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

			polygon.arcTo(roundCorner / 2, roundCorner / 2, 0, 0, 1, roundCorner / 2, 0);

		}
		return polygon;
	}

}
