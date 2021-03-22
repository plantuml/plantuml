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
package net.sourceforge.plantuml.graphic;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.text.RichText;
import net.sourceforge.plantuml.text.StyledString;
import net.sourceforge.plantuml.ugraphic.UFont;

public abstract class StringBounderRaw implements StringBounder {

	public final Dimension2D calculateDimension(UFont font, String text) {
		if (RichText.isRich(text)) {
			double width = 0;
			double height = 0;
			for (StyledString s : StyledString.build(text)) {
				final UFont newFont = s.getStyle().mutateFont(font);
				final Dimension2D rect = calculateDimensionInternal(newFont, s.getText());
				width += rect.getWidth();
				height = Math.max(height, rect.getHeight());
			}
			return new Dimension2DDouble(width, height);
		}
		return calculateDimensionInternal(font, text);
	}

	protected abstract Dimension2D calculateDimensionInternal(UFont font, String text);

	public double getDescent(UFont font, String text) {
		final FontRenderContext frc = FileFormat.gg.getFontRenderContext();
		final LineMetrics lineMetrics = font.getUnderlayingFont().getLineMetrics(text, frc);
		final double descent = lineMetrics.getDescent();
		return descent;
	}

}
