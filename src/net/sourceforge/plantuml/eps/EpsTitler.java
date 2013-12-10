/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
package net.sourceforge.plantuml.eps;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;

public final class EpsTitler {

	private final List<? extends CharSequence> text;
	private final HorizontalAlignment horizontalAlignment;
	private final VerticalPosition verticalPosition;
	private final int margin;
	private final TextBlock textBloc;
	private final EpsStrategy epsStrategy;
	private final ColorMapper colorMapper;

	public EpsTitler(ColorMapper colorMapper, EpsStrategy epsStrategy, HtmlColor textColor,
			List<? extends CharSequence> text, int fontSize, String fontFamily,
			HorizontalAlignment horizontalAlignment, VerticalPosition verticalPosition, int margin) {
		this.text = text;
		this.colorMapper = colorMapper;
		this.epsStrategy = epsStrategy;
		this.horizontalAlignment = horizontalAlignment;
		this.verticalPosition = verticalPosition;
		this.margin = margin;
		if (text == null || text.size() == 0) {
			textBloc = null;
		} else {
			final UFont normalFont = new UFont(fontFamily, Font.PLAIN, fontSize);
			textBloc = TextBlockUtils.create(new Display(text), new FontConfiguration(normalFont, textColor),
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		}
	}

	public double getHeight() {
		if (textBloc == null) {
			return 0;
		}
		return textBloc.calculateDimension(new UGraphicEps(colorMapper, epsStrategy).getStringBounder()).getHeight()
				+ margin;
	}

	public String addTitleEps(String eps) throws IOException {
		if (text == null || text.size() == 0) {
			return eps;
		}

		final int idx = eps.indexOf("%%BoundingBox:");
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		final StringTokenizer st = new StringTokenizer(eps.substring(idx + "%%BoundingBox:".length()), " \n\t\r");
		final int x1 = Integer.parseInt(st.nextToken());
		final int y1 = Integer.parseInt(st.nextToken());
		final int x2 = Integer.parseInt(st.nextToken());
		final int y2 = Integer.parseInt(st.nextToken());
		assert x2 >= x1;
		assert y2 >= y1;

		final double width = x2 - x1;
		final double height = y2 - y1;

		final UGraphicEps uGraphicEps = new UGraphicEps(colorMapper, epsStrategy);
		final Dimension2D dimText = textBloc.calculateDimension(uGraphicEps.getStringBounder());
		final double xpos;

		if (horizontalAlignment == HorizontalAlignment.LEFT) {
			xpos = 2;
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			xpos = width - dimText.getWidth() - 2;
		} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
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

		textBloc.drawU(uGraphicEps.apply(new UTranslate(xpos, yText)));

		final double yImage;
		if (verticalPosition == VerticalPosition.TOP) {
			yImage = dimText.getHeight();
		} else {
			yImage = 0;
		}

		uGraphicEps.drawEps(eps, 0, yImage);

		uGraphicEps.close();
		return uGraphicEps.getEPSCode();

		// String svgTitle = CucaDiagramFileMaker.getSvg(uGraphicSvg);
		// svgTitle = svgTitle.replaceFirst("(?i)<g>", "<g
		// transform=\"translate(0 0)\">");
		//
		// if (verticalPosition == VerticalPosition.TOP) {
		// final Pattern p =
		// Pattern.compile("(?i)translate\\((\\d+)\\s+(\\d+)");
		// final Matcher m = p.matcher(svg);
		//
		// final StringBuffer sb = new StringBuffer();
		// while (m.find()) {
		// final int tx = Integer.parseInt(m.group(1));
		// final int ty = Integer.parseInt(m.group(2)) + (int)
		// (dimText.getHeight()) + margin;
		// m.appendReplacement(sb, "translate(" + tx + " " + ty);
		// }
		// m.appendTail(sb);
		// svg = sb.toString();
		// }
		//
		// final int x = svg.indexOf("<g ");
		// if (x == -1) {
		// throw new IllegalStateException();
		// }
		// svg = svg.substring(0, x) + svgTitle + svg.substring(x);
		//
		// return svg;
	}

}
