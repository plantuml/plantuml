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
 * Revision $Revision: 5757 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UText;

class TileText implements Tile {

	private final String text;
	private final FontConfiguration fontConfiguration;

	public TileText(String text, FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
		this.text = text;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D rect = stringBounder.calculateDimension(fontConfiguration.getFont(), text);
		Log.debug("g2d=" + rect);
		Log.debug("Size for " + text + " is " + rect);
		double h = rect.getHeight();
		if (h < 10) {
			h = 10;
		}
		final double width = text.indexOf('\t') == -1 ? rect.getWidth() : getWidth(stringBounder);
		return new Dimension2DDouble(width, h);
	}

	public double getFontSize2D() {
		return fontConfiguration.getFont().getSize2D();
	}

	public void draw(Graphics2D g2d, double x, double y) {
		// TO be removed
		g2d.setFont(fontConfiguration.getFont());
		g2d.setPaint(fontConfiguration.getColor());
		g2d.drawString(text, (float) x, (float) y);

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d));
			final int ypos = (int) (y + 2.5);
			g2d.setStroke(new BasicStroke((float) 1.3));
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
			g2d.setStroke(new BasicStroke());
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d));
			final FontMetrics fm = g2d.getFontMetrics(fontConfiguration.getFont());
			final int ypos = (int) (y - fm.getDescent() - 0.5);
			g2d.setStroke(new BasicStroke((float) 1.5));
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
			g2d.setStroke(new BasicStroke());
		}
	}

	double getTabSize(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fontConfiguration.getFont(), "        ").getWidth();
	}

	public void drawU(UGraphic ug, double x, double y) {
		ug.getParam().setColor(fontConfiguration.getColor());

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
					ug.draw(x, y, utext);
					final Dimension2D dim = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), s);
					x += dim.getWidth();
				}
			}
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
