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
package net.sourceforge.plantuml.ugraphic.g2d;

import static java.lang.Math.max;
import static net.sourceforge.plantuml.graphic.TextBlockUtils.createTextLayout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class DriverTextAsPathG2d implements UDriver<UText, Graphics2D> {

	private final EnsureVisible visible;
	private final StringBounder stringBounder;

	public DriverTextAsPathG2d(EnsureVisible visible, StringBounder stringBounder) {
		this.visible = visible;
		this.stringBounder = stringBounder;
	}

	private static void printFont() {
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final String fontNames[] = ge.getAvailableFontFamilyNames();
		final int j = fontNames.length;
		for (int i = 0; i < j; i++) {
			Log.info("Available fonts: " + fontNames[i]);
		}
	}

	public void draw(UText shape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();

		final UFont font = fontConfiguration.getFont();
		final Dimension2D dim = stringBounder.calculateDimension(font, shape.getText());
		final double height = max(10, dim.getHeight());
		final double width = dim.getWidth();
		
		if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
			final Color extended = mapper.toColor(fontConfiguration.getExtendedColor());
			if (extended != null) {
				g2d.setColor(extended);
				g2d.setBackground(extended);
				g2d.fill(new Rectangle2D.Double(x, y - height + 1.5, width, height));
			}
		}
		visible.ensureVisible(x, y - height + 1.5);
		visible.ensureVisible(x + width, y + 1.5);

		g2d.setFont(font.getUnderlayingFont());
		g2d.setColor(mapper.toColor(fontConfiguration.getColor()));
		final TextLayout t = createTextLayout(font, shape.getText());
		g2d.translate(x, y);
		g2d.fill(t.getOutline(null));
		g2d.translate(-x, -y);

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			final HColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				g2d.setColor(mapper.toColor(extended));
			}
			final int ypos = (int) (y + 2.5);
			g2d.setStroke(new BasicStroke((float) 1));
			g2d.drawLine((int) x, ypos, (int) (x + width), ypos);
			g2d.setStroke(new BasicStroke());
		}
		if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
			final int ypos = (int) (y + 2.5) - 1;
			final HColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				g2d.setColor(mapper.toColor(extended));
			}
			for (int i = (int) x; i < x + width - 5; i += 6) {
				g2d.drawLine(i, ypos - 0, i + 3, ypos + 1);
				g2d.drawLine(i + 3, ypos + 1, i + 6, ypos - 0);
			}
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final FontMetrics fm = g2d.getFontMetrics(font.getUnderlayingFont());
			final int ypos = (int) (y - fm.getDescent() - 0.5);
			final HColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				g2d.setColor(mapper.toColor(extended));
			}
			g2d.setStroke(new BasicStroke((float) 1.5));
			g2d.drawLine((int) x, ypos, (int) (x + width), ypos);
			g2d.setStroke(new BasicStroke());
		}
	}
}
