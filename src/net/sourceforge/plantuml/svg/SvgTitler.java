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

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramFileMaker;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public final class SvgTitler {

	private final List<? extends CharSequence> text;
	private final HorizontalAlignement horizontalAlignement;
	private final VerticalPosition verticalPosition;
	private final int margin;
	private final TextBlock textBloc;
	private final ColorMapper colorMapper;

	public SvgTitler(ColorMapper colorMapper, HtmlColor textColor, List<? extends CharSequence> text, int fontSize, String fontFamily,
			HorizontalAlignement horizontalAlignement, VerticalPosition verticalPosition, int margin) {
		this.text = text;
		this.colorMapper = colorMapper;
		this.horizontalAlignement = horizontalAlignement;
		this.verticalPosition = verticalPosition;
		this.margin = margin;
		if (text == null || text.size() == 0) {
			textBloc = null;
		} else {
			final UFont normalFont = new UFont(fontFamily, Font.PLAIN, fontSize);
			textBloc = TextBlockUtils.create(text, new FontConfiguration(normalFont, textColor),
					HorizontalAlignement.LEFT);
		}
	}

	public SvgData addTitle(SvgData svgData) throws IOException {
		if (text == null || text.size() == 0) {
			return svgData;
		}

		final UGraphicSvg uGraphicSvg = new UGraphicSvg(colorMapper, false);
		final Dimension2D dimText = textBloc.calculateDimension(uGraphicSvg.getStringBounder());
		final double xpos;

		final double width = svgData.getWidth();
		final double height = svgData.getHeight();

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

		double suppWidth = 0;
		if (dimText.getWidth() > width) {
			suppWidth = dimText.getWidth() - width;
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

		String newSvg = svgData.getSvg();

		if (verticalPosition == VerticalPosition.TOP) {
			final Pattern p = Pattern.compile("(?i)translate\\((\\d+)\\s+(\\d+)");
			final Matcher m = p.matcher(newSvg);

			final StringBuffer sb = new StringBuffer();
			while (m.find()) {
				final int tx = Integer.parseInt(m.group(1));
				final int ty = Integer.parseInt(m.group(2)) + (int) (dimText.getHeight()) + margin;
				m.appendReplacement(sb, "translate(" + tx + " " + ty);
			}
			m.appendTail(sb);
			newSvg = sb.toString();
		}

		final int x = newSvg.indexOf("<g ");
		if (x == -1) {
			throw new IllegalStateException();
		}
		newSvg = newSvg.substring(0, x) + svgTitle + newSvg.substring(x);

		return svgData.mutateFromSvgTitler(newSvg, dimText.getHeight() + margin, suppWidth);
	}
}
