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
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseEnglober extends AbstractTextualComponent {

	private final SymbolContext symbolContext;
	private final double roundCorner;

	public ComponentRoseEnglober(Style style, SymbolContext symbolContext, Display strings, FontConfiguration font,
			ISkinSimple spriteContainer, double roundCorner) {
		super(style, LineBreakStrategy.NONE, strings, font, HorizontalAlignment.CENTER, 3, 3, 1, spriteContainer,
				false, null, null);
		if (SkinParam.USE_STYLES()) {
			roundCorner = style.value(PName.RoundCorner).asDouble();
			symbolContext = style.getSymbolContext(getIHtmlColorSet());
		}
		this.roundCorner = roundCorner;
		this.symbolContext = symbolContext;
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = symbolContext.transparentBackColorToNull().apply(ug);
		ug.draw(new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()).rounded(roundCorner));
		final double xpos = (dimensionToUse.getWidth() - getPureTextWidth(ug.getStringBounder())) / 2;
		getTextBlock().drawU(ug.apply(UTranslate.dx(xpos)));
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		// ug.getParam().setColor(Color.RED);
		// ug.getParam().setBackcolor(Color.YELLOW);
		// ug.draw(0, 0, new URectangle(dimensionToUse.getWidth(),
		// dimensionToUse.getHeight()));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 3;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}
}
