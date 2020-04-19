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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TileText extends AbstractTextBlock implements TextBlock {

	private final String text;
	private final FontConfiguration fontConfiguration;
	private final Url url;

	public TileText(String text, FontConfiguration fontConfiguration, Url url) {
		this.fontConfiguration = fontConfiguration;
		this.text = text;
		this.url = url;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D rect = stringBounder.calculateDimension(fontConfiguration.getFont(), text);
		final int spaceBottom = Math.abs(fontConfiguration.getSpace());
		Log.debug("g2d=" + rect);
		Log.debug("Size for " + text + " is " + rect);
		double h = rect.getHeight();
		if (h < 10) {
			h = 10;
		}
		final double width = text.indexOf('\t') == -1 ? rect.getWidth() : getWidth(stringBounder);
		return new Dimension2DDouble(width, h + spaceBottom);
	}

	public double getFontSize2D() {
		return fontConfiguration.getFont().getSize2D();
	}

	double getTabSize(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fontConfiguration.getFont(), "        ").getWidth();
	}

	public void drawU(UGraphic ug) {
		double x = 0;
		if (url != null) {
			ug.startUrl(url);
		}
		ug = ug.apply(fontConfiguration.getColor());

		final StringTokenizer tokenizer = new StringTokenizer(text, "\t", true);

		if (tokenizer.hasMoreTokens()) {
			final double tabSize = getTabSize(ug.getStringBounder());
			while (tokenizer.hasMoreTokens()) {
				final String s = tokenizer.nextToken();
				if (s.equals("\t")) {
					final double remainder = x % tabSize;
					x += tabSize - remainder;
				} else {
					final UText utext = new UText(s, fontConfiguration);
					final Dimension2D dim = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), s);
					final int space = fontConfiguration.getSpace();
					final double ypos;
					if (space < 0) {
						ypos = space /*- getFontSize2D() - space*/;
					} else {
						ypos = space;
					}
					ug.apply(new UTranslate(x, ypos)).draw(utext);
					x += dim.getWidth();
				}
			}
		}
		if (url != null) {
			ug.closeAction();
		}
	}

	double getWidth(StringBounder stringBounder) {
		final StringTokenizer tokenizer = new StringTokenizer(text, "\t", true);
		final double tabSize = getTabSize(stringBounder);
		double x = 0;
		while (tokenizer.hasMoreTokens()) {
			final String s = tokenizer.nextToken();
			if (s.equals("\t")) {
				final double remainder = x % tabSize;
				x += tabSize - remainder;
			} else {
				final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), s);
				x += dim.getWidth();
			}
		}
		return x;
	}

}
