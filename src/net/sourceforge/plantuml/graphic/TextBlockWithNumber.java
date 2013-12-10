/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 11154 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockWithNumber extends TextBlockSimple {

	private final TextBlock numText;

	public TextBlockWithNumber(String number, Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer, double maxMessageSize) {
		super(texts, fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize);
		this.numText = TextBlockUtils.create(Display.asList(number), fontConfiguration, HorizontalAlignment.LEFT,
				spriteContainer);
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double widthNum = getNumberWithAndMargin(stringBounder);
		final double heightNum = numText.calculateDimension(stringBounder).getHeight();

		final Dimension2D dim = super.calculateDimension(stringBounder);
		return new Dimension2DDouble(dim.getWidth() + widthNum, Math.max(heightNum, dim.getHeight()));
	}

	private double getNumberWithAndMargin(StringBounder stringBounder) {
		final double widthNum = numText.calculateDimension(stringBounder).getWidth();
		return widthNum + 4.0;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double heightNum = numText.calculateDimension(stringBounder).getHeight();
		
		final double deltaY = calculateDimension(stringBounder).getHeight() - heightNum;
		
		numText.drawU(ug.apply(new UTranslate(0, deltaY / 2.0)));
		super.drawU(ug.apply(new UTranslate(getNumberWithAndMargin(stringBounder), 0)));
	}


}
