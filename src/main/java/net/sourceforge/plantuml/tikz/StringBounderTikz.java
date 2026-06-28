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
package net.sourceforge.plantuml.tikz;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderTikz implements StringBounder {

	private final LatexTextMetrics latexManager;
	private final FileFormat fileFormat;

	public StringBounderTikz(LatexEngine latexEngine, TikzFontDistortion tikzFontDistortion, FileFormat fileFormat) {
		this.fileFormat = fileFormat;
		this.latexManager = new LatexTextMetrics(latexEngine, tikzFontDistortion.getTexPreamble());
	}

	@Override
	public FileFormat getFileFormat() {
		return fileFormat;
	}

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		final double[] widthHeightDepth = latexManager.getWidthHeightDepth(styleText(font, text));
		double height = widthHeightDepth[1] + widthHeightDepth[2];
		if (height == 0.0 && text.trim().isEmpty()) {
			// avoid return 0 height for space, otherwise cause exception, case in #1259
			height = latexManager.getWidthHeightDepth(styleText(font, " "))[0];
		}
		return new XDimension2D(widthHeightDepth[0], height);
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		return "TIKZ".equalsIgnoreCase(propertyName);
	}

	@Override
	public double getDescent(UFont font, String text) {
		final double[] widthHeightDepth = latexManager.getWidthHeightDepth(styleText(font, text));
		return widthHeightDepth[2];
	}

	protected String styleText(UFont font, String text) {
		if (font == null)
			return "$" + text + "$";

		final StringBuilder sb = new StringBuilder();
		final boolean italic = font.getFontFace().isItalic();
		final boolean bold = font.getFontFace().isBold();

		if (italic)
			sb.append("\\textit{");

		if (bold)
			sb.append("\\textbf{");

		sb.append(LatexTextMetrics.protectText(text));
		if (bold)
			sb.append("}");

		if (italic)
			sb.append("}");

		return sb.toString();
	}

}
