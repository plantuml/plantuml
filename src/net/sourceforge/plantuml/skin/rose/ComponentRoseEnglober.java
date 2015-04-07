/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * Revision $Revision: 4258 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseEnglober extends AbstractTextualComponent {

	private final HtmlColor borderColor;
	private final HtmlColor backColor;

	public ComponentRoseEnglober(HtmlColor borderColor, HtmlColor backColor, Display strings, HtmlColor fontColor,
			HtmlColor hyperlinkColor, boolean useUnderlineForHyperlink, UFont font, ISkinSimple spriteContainer) {
		super(strings, fontColor, hyperlinkColor, useUnderlineForHyperlink, font, HorizontalAlignment.CENTER, 3, 3, 1,
				spriteContainer, 0, false, null, null);
		this.borderColor = borderColor;
		this.backColor = backColor;
	}

	@Override
	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		ug = ug.apply(new UChangeBackColor(backColor)).apply(new UChangeColor(borderColor));
		ug.draw(new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));
		final double xpos = (dimensionToUse.getWidth() - getPureTextWidth(ug.getStringBounder())) / 2;
		getTextBlock().drawU(ug.apply(new UTranslate(xpos, 0)));
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
