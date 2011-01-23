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
 * Revision $Revision: 4173 $
 *
 */
package net.sourceforge.plantuml.svg;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramFileMaker;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public final class SvgTitler {

	private final Color textColor;
	private final List<String> text;
	// private final int fontSize;
	// private final String fontFamily;
	private final HorizontalAlignement horizontalAlignement;
	private final VerticalPosition verticalPosition;
	private final int margin;
	private final TextBlock textBloc;

	public SvgTitler(Color textColor, List<String> text, int fontSize, String fontFamily,
			HorizontalAlignement horizontalAlignement, VerticalPosition verticalPosition, int margin) {
		this.textColor = textColor;
		this.text = text;
		// this.fontSize = fontSize;
		// this.fontFamily = fontFamily;
		this.horizontalAlignement = horizontalAlignement;
		this.verticalPosition = verticalPosition;
		this.margin = margin;
		if (text == null || text.size() == 0) {
			textBloc = null;
		} else {
			final Font normalFont = new Font(fontFamily, Font.PLAIN, fontSize);
			textBloc = TextBlockUtils.create(text, new FontConfiguration(normalFont, textColor),
					HorizontalAlignement.LEFT);
		}
	}

	public double getHeight() {
		if (textBloc == null) {
			return 0;
		}
		return textBloc.calculateDimension(new UGraphicSvg(false).getStringBounder()).getHeight() + margin;
	}

	public String addTitleSvg(String svg, double width, double height) throws IOException {
		if (text == null || text.size() == 0) {
			return svg;
		}

		final UGraphicSvg uGraphicSvg = new UGraphicSvg(false);
		final Dimension2D dimText = textBloc.calculateDimension(uGraphicSvg.getStringBounder());
		final double xpos;

		if (horizontalAlignement == HorizontalAlignement.LEFT) {
			xpos = 2;
		} else if (horizontalAlignement == HorizontalAlignement.RIGHT) {
			xpos = width - dimText.getWidth() - 2;
		} else if (horizontalAlignement == HorizontalAlignement.CENTER) {
			xpos = (width - dimText.getWidth()) / 2;
		} else {
			xpos = 0;
			assert false;
		}

		final double yText;

		if (verticalPosition == VerticalPosition.TOP) {
			yText = 0;
		} else {
			yText = height + margin;
		}

		textBloc.drawU(uGraphicSvg, xpos, yText);
		String svgTitle = CucaDiagramFileMaker.getSvg(uGraphicSvg);
		svgTitle = svgTitle.replaceFirst("(?i)<g>", "<g transform=\"translate(0 0)\">");

		if (verticalPosition == VerticalPosition.TOP) {
			final Pattern p = Pattern.compile("(?i)translate\\((\\d+)\\s+(\\d+)");
			final Matcher m = p.matcher(svg);

			final StringBuffer sb = new StringBuffer();
			while (m.find()) {
				final int tx = Integer.parseInt(m.group(1));
				final int ty = Integer.parseInt(m.group(2)) + (int) (dimText.getHeight()) + margin;
				m.appendReplacement(sb, "translate(" + tx + " " + ty);
			}
			m.appendTail(sb);
			svg = sb.toString();
		}

		final int x = svg.indexOf("<g ");
		if (x == -1) {
			throw new IllegalStateException();
		}
		svg = svg.substring(0, x) + svgTitle + svg.substring(x);

		return svg;
	}

}
