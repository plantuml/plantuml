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

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseReference extends AbstractTextualComponent {

	// private final int outMargin = 5;
	private final Color background;

	public ComponentRoseReference(Color fontColor, Font font, Color background,
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

		ug.getParam().setColor(getFontColor());
		ug.getParam().setBackcolor(background);

		ug.getParam().setStroke(new UStroke(5, 5, 1));
		ug.draw(1, 1, new URectangle(dimensionToUse.getWidth() - 2, dimensionToUse.getHeight() - 2));
		ug.getParam().setStroke(new UStroke());

		final UShape box = new URectangle(8, 8);
		ug.draw(5, 5, box);
		final ULine lineH = new ULine(4, 0);
		final ULine lineV = new ULine(0, 4);
		ug.draw(5 + 2, 5 + 4, lineH);
		ug.draw(5 + 4, 5 + 2, lineV);

		textBlock.drawU(ug, 20, getMarginY());
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 0;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + 26;
	}

}
