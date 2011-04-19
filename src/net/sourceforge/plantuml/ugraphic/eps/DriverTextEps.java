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
 */
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.PathIterator;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.g2d.DriverTextG2d;

public class DriverTextEps implements UDriver<EpsGraphics> {

	private final StringBounder stringBounder;
	private final ClipContainer clipContainer;
	private final FontRenderContext fontRenderContext;
	private final Graphics2D g2dummy;

	public DriverTextEps(Graphics2D g2dummy, ClipContainer clipContainer) {
		this.stringBounder = StringBounderUtils.asStringBounder(g2dummy);
		this.clipContainer = clipContainer;
		this.fontRenderContext = g2dummy.getFontRenderContext();
		this.g2dummy = g2dummy;
	}

	public void draw(UShape ushape, double x, double y, UParam param, EpsGraphics eps) {

		final UClip clip = clipContainer.getClip();
		if (clip != null && clip.isInside(x, y) == false) {
			return;
		}

		final UText shape = (UText) ushape;
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		final Font font = fontConfiguration.getFont();

		final TextLayout t = new TextLayout(shape.getText(), font, fontRenderContext);
		eps.setStrokeColor(fontConfiguration.getColor());
		drawPathIterator(eps, x, y, t.getOutline(null).getPathIterator(null));

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			final Color extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(extended);
			}
			final Dimension2D dim = DriverTextG2d.calculateDimension(stringBounder, font, shape.getText());
			eps.setStrokeWidth("1.1", 0, 0);
			eps.epsLine(x, y + 1.5, x + dim.getWidth(), y + 1.5);
			eps.setStrokeWidth("1", 0, 0);
		}
		if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
			final Dimension2D dim = DriverTextG2d.calculateDimension(stringBounder, font, shape.getText());
			final int ypos = (int) (y + 2.5) - 1;
			final Color extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(extended);
			}
			eps.setStrokeWidth("1.1", 0, 0);
			for (int i = (int) x; i < x + dim.getWidth() - 5; i += 6) {
				eps.epsLine(i, ypos - 0, i + 3, ypos + 1);
				eps.epsLine(i + 3, ypos + 1, i + 6, ypos - 0);
			}
			eps.setStrokeWidth("1", 0, 0);
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final Color extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(extended);
			}
			final Dimension2D dim = DriverTextG2d.calculateDimension(stringBounder, font, shape.getText());
			final FontMetrics fm = g2dummy.getFontMetrics(font);
			final int ypos = (int) (y - fm.getDescent() - 0.5);
			eps.setStrokeWidth("1.3", 0, 0);
			eps.epsLine(x, ypos, x + dim.getWidth(), ypos);
			eps.setStrokeWidth("1", 0, 0);
		}

	}

	static void drawPathIterator(EpsGraphics eps, double x, double y, PathIterator path) {

		eps.newpath();
		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			if (code == PathIterator.SEG_MOVETO) {
				eps.moveto(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_LINETO) {
				eps.lineto(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_CLOSE) {
				eps.closepath();
			} else if (code == PathIterator.SEG_CUBICTO) {
				eps.curveto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
			} else if (code == PathIterator.SEG_QUADTO) {
				eps.quadto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y);
			} else {
				throw new UnsupportedOperationException("code=" + code);
			}

			path.next();
		}

		eps.fill(path.getWindingRule());

	}
}
