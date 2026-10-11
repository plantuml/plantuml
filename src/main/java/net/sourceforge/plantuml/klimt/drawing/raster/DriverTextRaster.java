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
 */
package net.sourceforge.plantuml.klimt.drawing.raster;

import java.util.List;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.text.StyledString;

/**
 * As {@code DriverTextG2d}.
 */
public class DriverTextRaster implements UDriver<UText, RasterCanvas> {

	private final StringBounder stringBounder;

	public DriverTextRaster(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	@Override
	public void draw(UText shape, double x, double y, ColorMapper mapper, UParam param, RasterCanvas canvas) {
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		if (fontConfiguration.getColor().isTransparent())
			return;

		final List<StyledString> strings = StyledString.build(shape.getText());
		for (StyledString styledString : strings) {
			final FontConfiguration fc = styledString.getStyle() == FontStyle.BOLD ? fontConfiguration.bold()
					: fontConfiguration;
			x += printSingleText(canvas, fc, styledString.getText(), x, y, mapper, shape.getOrientation());
		}
	}

	private double printSingleText(RasterCanvas canvas, FontConfiguration fontConfiguration, String text, double x,
			double y, ColorMapper mapper, int orientation) {
		final UFont font = fontConfiguration.getFont();
		final HColor extended = fontConfiguration.getExtendedColor();

		final XDimension2D dim = stringBounder.calculateDimension(font, text);
		final double height = Math.max(10, dim.getHeight());
		final double width = dim.getWidth();

		if (orientation == 90) {
			canvas.drawString(text, font, x, y, -Math.PI / 2);
		} else if (orientation == 270) {
			canvas.drawString(text, font, x, y, Math.PI / 2);
		} else if (orientation == 0) {
			if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
				final RasterPath area = RasterPath.rectangle(x, y - height + 1.5, width, height, 0, 0);
				if (RasterPaints.isGradient(extended)) {
					RasterPaints.setGradient(canvas, x, y, mapper, width, height, extended);
					canvas.fill(area);
				} else if (extended.toColor(mapper) != null) {
					canvas.setColor(RasterPaints.argb(extended, mapper));
					canvas.fill(area);
				}
			}
			canvas.setColor(RasterPaints.argb(fontConfiguration.getColor(), mapper));
			// Graphics2D.drawString(String, float, float)
			canvas.drawString(text, font, (float) x, (float) y, 0);

			if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
				if (extended != null)
					canvas.setColor(RasterPaints.argb(extended, mapper));
				final UStroke stroke = fontConfiguration.getUnderlineStroke();
				if (stroke.getThickness() > 0) {
					RasterPaints.setStroke(stroke, canvas);
					final int ypos = (int) (y + 2.5);
					canvas.stroke(RasterPath.line((int) x, ypos, (int) (x + width), ypos));
					canvas.setStroke(1, 0, 0);
				}
			}
			if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
				final int ypos = (int) (y + 2.5) - 1;
				if (extended != null)
					canvas.setColor(RasterPaints.argb(extended, mapper));
				for (int i = (int) x; i < x + width - 5; i += 6) {
					canvas.stroke(RasterPath.line(i, ypos - 0, i + 3, ypos + 1));
					canvas.stroke(RasterPath.line(i + 3, ypos + 1, i + 6, ypos - 0));
				}
			}
			if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
				// FontMetrics.getDescent() of Java2D: the descent rounded up
				final int descent = (int) (0.95 + stringBounder.getDescent(font, text));
				final int ypos = (int) (y - descent - 0.5);
				if (extended != null)
					canvas.setColor(RasterPaints.argb(extended, mapper));
				canvas.setStroke(1.5, 0, 0);
				canvas.stroke(RasterPath.line((int) x, ypos, (int) (x + width), ypos));
				canvas.setStroke(1, 0, 0);
			}
		}
		return width;
	}

}
