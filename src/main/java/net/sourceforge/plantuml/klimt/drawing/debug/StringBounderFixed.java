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

import java.util.Locale;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderFixed implements StringBounder {

	private static final double[] WIDTH = { 3.3, 3.3, 4.3, 6.7, 6.7, 10.7, 8.0, 2.3, 4.0, 4.0, 4.7, 7.0, 3.3, 4.0, 3.3,
			3.3, 6.7, 6.7, 6.7, 6.7, 6.7, 6.7, 6.7, 6.7, 6.7, 6.7, 3.3, 3.3, 7.0, 7.0, 7.0, 6.7, 12.2, 8.0, 8.0, 8.7,
			8.7, 8.0, 7.3, 9.3, 8.7, 3.3, 6.0, 8.0, 6.7, 10.0, 8.7, 9.3, 8.0, 9.3, 8.7, 8.0, 7.3, 8.7, 8.0, 11.3, 8.0,
			8.0, 7.3, 3.3, 3.3, 3.3, 5.6, 6.7, 4.0, 6.7, 6.7, 6.0, 6.7, 6.7, 3.3, 6.7, 6.7, 2.7, 2.7, 6.0, 2.7, 10.0,
			6.7, 6.7, 6.7, 6.7, 4.0, 6.0, 3.3, 6.7, 6.0, 8.7, 6.0, 6.0, 6.0, 4.0, 3.1, 4.0, 7.0, 6.0, };

	private final FileFormat ff;

	public StringBounderFixed(FileFormat fileFormat) {
		this.ff = fileFormat;
	}

	@Override
	public FileFormat getFileFormat() {
		return ff;
	}

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		final double size = font.getSize2D();
		final double factor = size / 12.0;
		final double height = size;
		double width = 0;
		for (int i = 0; i < text.length(); i++)
			width += getCharWidth(font, text.charAt(i));

		return new XDimension2D(width * factor, height);
	}

	private double getCharWidth(UFont font, char c) {
		if (c >= 32 && c <= 127)
			return WIDTH[c - 32];
		return 13;
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

	public static void main(String[] args) {

		final StringBuilder result = new StringBuilder("private static final double[] WIDTH = {");

		final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();
		final UFont font = UFontFactory.sansSerif(12);
		for (int a = 32; a < 128; a++) {
			final String s = "" + ((char) a);
			final double w = stringBounder.calculateDimension(font, s).getWidth();
			result.append(String.format(Locale.US, "%3.1f", w));
			result.append(", ");
		}
		result.append("};");

		System.err.println(result);
	}

}
