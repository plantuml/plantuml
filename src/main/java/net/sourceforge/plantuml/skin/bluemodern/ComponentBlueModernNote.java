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
package net.sourceforge.plantuml.skin.bluemodern;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

final public class ComponentBlueModernNote extends AbstractTextualComponent {

	private final int shadowview = 4;
	private final int cornersize = 10;
	private final HtmlColor back;
	private final HtmlColor foregroundColor;

	public ComponentBlueModernNote(HtmlColor back, HtmlColor foregroundColor, FontConfiguration font,
			Display strings, ISkinSimple spriteContainer) {
		super(LineBreakStrategy.NONE, strings, font, HorizontalAlignment.LEFT, 6, 15, 5, spriteContainer, false,
				null, null);
		this.back = back;
		this.foregroundColor = foregroundColor;
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder) + 2 * getPaddingX();
		return result;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY();
	}

	@Override
	public double getPaddingX() {
		return 9;
	}

	@Override
	public double getPaddingY() {
		return 9;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double textHeight = getTextHeight(stringBounder);

		final double textWidth = getTextWidth(stringBounder);

		final ShadowShape shadowShape = new ShadowShape(textWidth, textHeight, 3);
		shadowShape.drawU(ug.apply(new UTranslate(shadowview, shadowview)));

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, textHeight);
		polygon.addPoint(textWidth, textHeight);
		polygon.addPoint(textWidth, cornersize);
		polygon.addPoint(textWidth - cornersize, 0);
		polygon.addPoint(0, 0);

		ug = ug.apply(new UChangeBackColor(back));
		ug = ug.apply(new UChangeColor(foregroundColor));
		ug.draw(polygon);

		ug.apply(new UTranslate(textWidth - cornersize, 0)).draw(new ULine(0, cornersize));
		ug.apply(new UTranslate(textWidth, cornersize)).draw(new ULine(-cornersize, 0));

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));

	}

}
