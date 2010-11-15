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
 * Revision $Revision: 4125 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class TextBlockWithNumber extends TextBlockSimple {

	private final TextBlock numText;

	public TextBlockWithNumber(String number, List<? extends CharSequence> texts, Font font, Color paint,
			HorizontalAlignement horizontalAlignement) {
		super(texts, font, paint, horizontalAlignement);
		this.numText = TextBlockUtils.create(Arrays.asList(number), font, paint, HorizontalAlignement.LEFT);
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
	public void drawTOBEREMOVED(Graphics2D g2d, double x, double y) {
		final StringBounder stringBounder = StringBounderUtils.asStringBounder(g2d);
		final double heightNum = numText.calculateDimension(stringBounder).getHeight();

		final double deltaY = calculateDimension(stringBounder).getHeight() - heightNum;

		numText.drawTOBEREMOVED(g2d, x, y + deltaY / 2.0);
		super.drawTOBEREMOVED(g2d, x + getNumberWithAndMargin(stringBounder), y);
	}
	
	@Override
	public void drawU(UGraphic ug, double x, double y) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double heightNum = numText.calculateDimension(stringBounder).getHeight();

		final double deltaY = calculateDimension(stringBounder).getHeight() - heightNum;

		numText.drawU(ug, x, y + deltaY / 2.0);
		super.drawU(ug, x + getNumberWithAndMargin(stringBounder), y);
	}

}
