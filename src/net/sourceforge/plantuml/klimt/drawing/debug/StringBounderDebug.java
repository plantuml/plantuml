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
package net.sourceforge.plantuml.klimt.drawing.debug;

import java.util.Random;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.font.StringBounderRaw;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderDebug extends StringBounderRaw {
    // ::remove folder when __HAXE__
	// ::remove folder when __CORE__

	public StringBounderDebug() {
		super(FileFormat.gg.getFontRenderContext());
	}

	@Override
	protected XDimension2D calculateDimensionInternal(UFont font, String text) {
		final Random rnd = new Random(StringUtils.seed(text));
		// We want a random factor between 80% et 130%
		final double factor = 0.8 + 0.5 * rnd.nextDouble();
		final double size = font.getSize2D();
		final double height = size;
		final double width = size * text.length() * factor;
		return new XDimension2D(width, height);
	}

	@Override
	public double getDescent(UFont font, String text) {
		final double descent = font.getSize2D() / 4.5;
		return descent;
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		return false;
	}

}
