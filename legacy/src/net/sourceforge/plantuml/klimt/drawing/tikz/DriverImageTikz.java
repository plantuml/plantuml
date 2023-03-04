/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.klimt.drawing.tikz;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.tikz.TikzGraphics;

public class DriverImageTikz implements UDriver<UImage, TikzGraphics> {

	public void draw(UImage shape, double x, double y, ColorMapper mapper, UParam param, TikzGraphics tikz) {
		final String rawFileName = shape.getRawFileName();
		if (rawFileName != null) {
			final double scale = shape.getScale();
			final String raw;
			if (scale == 1)
				raw = "\\includegraphics{" + rawFileName + "}";
			else
				raw = "\\includegraphics[scale=" + TikzGraphics.format(scale) + "]{" + rawFileName + "}";
			tikz.appendRaw(x, y, raw);
			return;
		}
		final String formula = shape.getFormula();
		if (formula != null) {
			tikz.appendRaw(x, y, formula);
		}
	}
}
