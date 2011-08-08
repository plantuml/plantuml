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
package net.sourceforge.plantuml.skin.bluemodern;

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

public class ComponentBlueModernDivider extends AbstractTextualComponent {

	private final HtmlColor background1;
	private final HtmlColor background2;
	private final HtmlColor borderColor;

	public ComponentBlueModernDivider(HtmlColor fontColor, UFont font, HtmlColor background1, HtmlColor background2,
			HtmlColor borderColor, List<? extends CharSequence> stringsToDisplay) {
		super(stringsToDisplay, fontColor, font, HorizontalAlignement.CENTER, 4, 4, 4);
		this.background1 = background1;
		this.background2 = background2;
		this.borderColor = borderColor;
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
		ug.getParam().setBackcolor(HtmlColor.BLACK);

		ug.getParam().setStroke(new UStroke(2));

		ug.draw(0, dimensionToUse.getHeight() / 2 - 1, new ULine(dimensionToUse.getWidth(), 0));
		ug.draw(0, dimensionToUse.getHeight() / 2 + 2, new ULine(dimensionToUse.getWidth(), 0));

		final FillRoundShape shape = new FillRoundShape(textWidth + deltaX, textHeight, background1, background2, 5);
		ug.translate(xpos, ypos);
		shape.drawU(ug);
		ug.translate(-xpos, -ypos);

		ug.getParam().setColor(borderColor);
		ug.getParam().setBackcolor(null);
		ug.draw(xpos, ypos, new URectangle(textWidth + deltaX, textHeight, 5, 5));
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
