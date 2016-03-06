/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 19109 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernGroupingHeader extends AbstractTextualComponent {

	private final int cornersize = 10;
	private final int commentMargin = 0; // 8;

	private final TextBlock commentTextBlock;

	private final HtmlColor headerBackgroundColor;
	private final HtmlColor generalBackgroundColor;
	private final HtmlColor borderColor;

	public ComponentBlueModernGroupingHeader(HtmlColor headerBackgroundColor, HtmlColor generalBackgroundColor,
			HtmlColor borderColor, HtmlColor fontColor2, FontConfiguration bigFont, UFont smallFont, Display strings,
			ISkinSimple spriteContainer) {
		super(strings.get(0), bigFont, HorizontalAlignment.LEFT, 15, 30, 1, spriteContainer, 0, null, null);
		this.headerBackgroundColor = headerBackgroundColor;
		this.generalBackgroundColor = generalBackgroundColor;
		this.borderColor = borderColor;
		if (strings.size() == 1 || strings.get(1) == null) {
			this.commentTextBlock = null;
		} else {
			final FontConfiguration fontConfiguration = new FontConfiguration(smallFont, fontColor2,
					bigFont.getHyperlinkColor(), bigFont.useUnderlineForHyperlink(), spriteContainer.getTabSize());
			this.commentTextBlock = Display.create("[" + strings.get(1) + "]").create(fontConfiguration,
					HorizontalAlignment.LEFT, spriteContainer);
		}
	}

	// @Override
	// public double getPaddingY() {
	// return 6;
	// }

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
		return getTextHeight(stringBounder) + 2 * getPaddingY();
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug.apply(new UChangeColor(borderColor)).apply(new UStroke(2))
				.apply(new UChangeBackColor(generalBackgroundColor))
				.draw(new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(new UChangeColor(borderColor));
		ug.apply(new UStroke(2)).draw(new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));

		final StringBounder stringBounder = ug.getStringBounder();
		final int textWidth = (int) getTextWidth(stringBounder);
		final int textHeight = (int) getTextHeight(stringBounder);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(textWidth, 0);

		polygon.addPoint(textWidth, textHeight - cornersize);
		polygon.addPoint(textWidth - cornersize, textHeight);

		polygon.addPoint(0, textHeight);
		polygon.addPoint(0, 0);

		ug = ug.apply(new UStroke(2));
		ug = ug.apply(new UChangeBackColor(headerBackgroundColor));
		ug.draw(polygon);
		ug.draw(new ULine(dimensionToUse.getWidth(), 0));

		final double heightWithoutPadding = dimensionToUse.getHeight() - getPaddingY();

		ug.apply(new UTranslate(dimensionToUse.getWidth(), 0)).draw(new ULine(0, heightWithoutPadding));
		ug.apply(new UTranslate(0, textHeight)).draw(new ULine(0, heightWithoutPadding - textHeight));
		ug = ug.apply(new UStroke());

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));

		if (commentTextBlock != null) {
			final int x1 = getMarginX1() + textWidth;
			final int y2 = getMarginY() + 1;

			commentTextBlock.drawU(ug.apply(new UChangeColor(generalBackgroundColor)).apply(
					new UTranslate(x1 + commentMargin, y2)));
		}
	}

}
