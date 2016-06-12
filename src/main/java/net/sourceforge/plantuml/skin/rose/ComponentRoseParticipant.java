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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseParticipant extends AbstractTextualComponent {

	private final HtmlColor back;
	private final HtmlColor foregroundColor;
	private final double deltaShadow;
	private final double roundCorner;
	private final UStroke stroke;

	public ComponentRoseParticipant(SymbolContext biColor, FontConfiguration font, Display stringsToDisplay,
			ISkinSimple spriteContainer, double roundCorner, UFont fontForStereotype,
			HtmlColor htmlColorForStereotype) {
		super(stringsToDisplay, font, HorizontalAlignment.CENTER, 7, 7, 7, spriteContainer, 0, false,
				fontForStereotype, htmlColorForStereotype);
		this.back = biColor.getBackColor();
		this.roundCorner = roundCorner;
		this.deltaShadow = biColor.getDeltaShadow();
		this.foregroundColor = biColor.getForeColor();
		this.stroke = biColor.getStroke();
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UChangeBackColor(back)).apply(new UChangeColor(foregroundColor));
		ug = ug.apply(stroke);
		final URectangle rect = new URectangle(getTextWidth(stringBounder), getTextHeight(stringBounder), roundCorner,
				roundCorner);
		rect.setDeltaShadow(deltaShadow);
		ug.draw(rect);
		ug = ug.apply(new UStroke());
		final TextBlock textBlock = getTextBlock();
		textBlock.drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + deltaShadow + 1;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + deltaShadow;
	}
}
