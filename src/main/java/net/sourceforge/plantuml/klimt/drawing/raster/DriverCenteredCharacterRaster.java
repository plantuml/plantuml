/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 */
package net.sourceforge.plantuml.klimt.drawing.raster;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;

/**
 * As {@code DriverCenteredCharacterG2d}, without {@code UnusedSpace} (which
 * needs AWT to rasterize the glyph): the glyph is centered on the box of its
 * ink, given by the canvas.
 *
 * <p>
 * UnusedSpace centers the smallest circle around the ink pixels of the glyph
 * drawn at integer coordinates: for one glyph, that is about the center of its
 * ink box minus half a pixel. Then DriverCenteredCharacterG2d draws at (x -
 * centerX, y - centerY - 0.5), that is (x - boxCenterX + 0.5, y - boxCenterY).
 */
public class DriverCenteredCharacterRaster implements UDriver<UCenteredCharacter, RasterCanvas> {

	@Override
	public void draw(UCenteredCharacter shape, double x, double y, ColorMapper mapper, UParam param,
			RasterCanvas canvas) {
		final String text = "" + shape.getChar();
		final UFont font = shape.getFont();
		final double[] ink = canvas.getInkBounds(text, font);
		final double centerX = ink[0] + ink[2] / 2;
		final double centerY = ink[1] + ink[3] / 2;

		canvas.setColor(RasterPaints.argb(param.getColor(), mapper));
		canvas.drawString(text, font, x - centerX + 0.5, y - centerY, 0);
	}

}
