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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class PSystemMath extends AbstractPSystem {

	private String math = "";
	private float scale = 1;
	private Color color = Color.BLACK;
	private Color backColor = Color.WHITE;

	public PSystemMath() {
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Math)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final ScientificEquationSafe asciiMath = ScientificEquationSafe.fromAsciiMath(math);
		return asciiMath.export(os, fileFormat, scale, color, backColor);
	}

	public void doCommandLine(String line) {
		final String lineLower = StringUtils.trin(StringUtils.goLowerCase(line));
		final String colorParam = "color ";
		final String backParam = "backgroundcolor ";
		if (lineLower.startsWith(colorParam)) {
			final Color col3 = getColor(line.substring(colorParam.length()));
			if (col3 != null) {
				color = col3;
			}
		} else if (lineLower.startsWith(backParam)) {
			final Color col3 = getColor(line.substring(backParam.length()));
			if (col3 != null) {
				backColor = col3;
			}
		} else if (lineLower.startsWith("scale ")) {
			final String value = line.substring("scale ".length());
			try {
				final float scale1 = Float.parseFloat(value);
				if (scale1 > 0) {
					scale = scale1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (lineLower.startsWith("dpi ")) {
			final String value = line.substring("dpi ".length());
			try {
				final float dpi1 = Float.parseFloat(value);
				if (dpi1 > 0) {
					scale = dpi1 / 96;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.math = line;

		}
	}

	private Color getColor(final String col) {
		final HColor col2 = HColorSet.instance().getColorIfValid(col);
		final Color col3 = new ColorMapperIdentity().toColor(col2);
		return col3;
	}

}
