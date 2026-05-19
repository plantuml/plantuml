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
 *
 */
package net.sourceforge.plantuml;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontImpl;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.text.SvgCharSizeHack;

public class FF4 implements StringBounder {

	private final SvgCharSizeHack charSizeHack;
	private final FontRenderContext fontRenderContext = FileFormat.gg.getFontRenderContext();
	private final FileFormat ff;

	@Override
	public FileFormat getFileFormat() {
		return ff;
	}

	public FF4(FileFormat ff, final SvgCharSizeHack charSizeHack) {
		this.ff = ff;
		this.charSizeHack = charSizeHack;
	}

	public String toString() {
		return "FileFormat::getSvgStringBounder";
	}

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		text = charSizeHack.transformStringForSizeHack(text);
		return FileFormat.getJavaDimension(font, text);
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		return "SVG".equalsIgnoreCase(propertyName);
	}

	@Override
	public double getDescent(UFont font, String text) {
		final LineMetrics lineMetrics = UFontImpl.getUnderlayingFont(font, text).getLineMetrics(text,
				fontRenderContext);
		final double descent = lineMetrics.getDescent();
		return descent;
	}

}
