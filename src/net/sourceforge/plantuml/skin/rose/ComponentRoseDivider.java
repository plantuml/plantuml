/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseDivider extends AbstractTextualComponent {

	// private final int outMargin = 5;
	private final HtmlColor background;

	public ComponentRoseDivider(HtmlColor fontColor, UFont font, HtmlColor background,
			List<? extends CharSequence> stringsToDisplay) {
		super(stringsToDisplay, fontColor, font, HorizontalAlignement.CENTER, 4, 4, 4);
		this.background = background;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final double textWidth = getTextWidth(stringBounder);
		final double textHeight = getTextHeight(stringBounder);

		final double deltaX = 6;
		final double xpos = (dimensionToUse.getWidth() - textWidth - deltaX) / 2;
		final double ypos = (dimensionToUse.getHeight() - textHeight) / 2;

		ug.getParam().setColor(HtmlColor.BLACK);
		ug.draw(0, dimensionToUse.getHeight() / 2 - 1, new ULine(dimensionToUse.getWidth(), 0));
		ug.draw(0, dimensionToUse.getHeight() / 2 + 2, new ULine(dimensionToUse.getWidth(), 0));

		ug.getParam().setColor(HtmlColor.BLACK);
		ug.getParam().setBackcolor(background);

		ug.getParam().setStroke(new UStroke(2));
		ug.draw(xpos, ypos, new URectangle(textWidth + deltaX, textHeight));
		ug.getParam().setStroke(new UStroke());

		textBlock.drawU(ug, xpos + deltaX, ypos + getMarginY());
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + 30;
	}

}
