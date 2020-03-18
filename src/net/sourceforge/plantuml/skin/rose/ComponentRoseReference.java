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
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ComponentRoseReference extends AbstractTextualComponent {

	private final int cornersize = 10;
	private final TextBlock textHeader;
	private final double heightFooter = 5;
	private final double xMargin = 2;
	private final HorizontalAlignment position;
	private final SymbolContext symbolContextHeader;
	private final SymbolContext symbolContextBody;

	public ComponentRoseReference(Style style, Style styleHeader, FontConfiguration font, SymbolContext symbolContext,
			FontConfiguration fcHeader, Display stringsToDisplay, HorizontalAlignment position,
			ISkinSimple spriteContainer, HColor background) {
		super(style, LineBreakStrategy.NONE, stringsToDisplay.subList(1, stringsToDisplay.size()), font,
				HorizontalAlignment.LEFT, 4, 4, 4, spriteContainer, false, null, null);
		if (SkinParam.USE_STYLES()) {
			this.symbolContextHeader = styleHeader.getSymbolContext(getIHtmlColorSet());
			this.symbolContextBody = style.getSymbolContext(getIHtmlColorSet());
			fcHeader = styleHeader.getFontConfiguration(getIHtmlColorSet());
			this.position = style.getHorizontalAlignment();
		} else {
			this.symbolContextHeader = symbolContext;
			this.symbolContextBody = symbolContextHeader.withBackColor(background);
			this.position = position;
		}

		this.textHeader = stringsToDisplay.subList(0, 1).create(fcHeader, HorizontalAlignment.LEFT, spriteContainer);

	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeaderWidth = (int) (getHeaderWidth(stringBounder));
		final int textHeaderHeight = (int) (getHeaderHeight(stringBounder));

		final URectangle rect = new URectangle(dimensionToUse.getWidth() - xMargin * 2
				- symbolContextHeader.getDeltaShadow(), dimensionToUse.getHeight() - heightFooter);
		rect.setDeltaShadow(symbolContextHeader.getDeltaShadow());
		ug = symbolContextBody.apply(ug);
		ug.apply(UTranslate.dx(xMargin)).draw(rect);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(textHeaderWidth, 0);

		polygon.addPoint(textHeaderWidth, textHeaderHeight - cornersize);
		polygon.addPoint(textHeaderWidth - cornersize, textHeaderHeight);

		polygon.addPoint(0, textHeaderHeight);
		polygon.addPoint(0, 0);

		ug = symbolContextHeader.apply(ug);
		ug.apply(UTranslate.dx(xMargin)).draw(polygon);

		ug = ug.apply(new UStroke());

		textHeader.drawU(ug.apply(new UTranslate(15, 2)));
		final double textPos;
		if (position == HorizontalAlignment.CENTER) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = (dimensionToUse.getWidth() - textWidth) / 2;
		} else if (position == HorizontalAlignment.RIGHT) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = dimensionToUse.getWidth() - textWidth - getMarginX2() - xMargin;
		} else {
			textPos = getMarginX1() + xMargin;
		}
		getTextBlock().drawU(ug.apply(new UTranslate(textPos, (getMarginY() + textHeaderHeight))));
	}

	private double getHeaderHeight(StringBounder stringBounder) {
		final Dimension2D headerDim = textHeader.calculateDimension(stringBounder);
		return headerDim.getHeight() + 2 * 1;
	}

	private double getHeaderWidth(StringBounder stringBounder) {
		final Dimension2D headerDim = textHeader.calculateDimension(stringBounder);
		return headerDim.getWidth() + 30 + 15;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getHeaderHeight(stringBounder) + heightFooter;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(getTextWidth(stringBounder), getHeaderWidth(stringBounder)) + xMargin * 2
				+ symbolContextHeader.getDeltaShadow();
	}

}
