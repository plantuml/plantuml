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

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

/**
 * As {@code DriverEllipseG2d}, without shadow nor pattern for now.
 */
public class DriverEllipseRaster implements UDriver<UEllipse, RasterCanvas> {

	@Override
	public void draw(UEllipse ellipse, double x, double y, ColorMapper mapper, UParam param, RasterCanvas canvas) {
		canvas.setStroke(param.getStroke().getThickness(), 0, 0);
		final HColor color = param.getColor();
		if (ellipse.getStart() == 0 && ellipse.getExtend() == 0) {
			final RasterPath shape = RasterPath.ellipse(x, y, ellipse.getWidth(), ellipse.getHeight());
			final HColor back = param.getBackcolor();
			if (RasterPaints.isGradient(back)) {
				RasterPaints.setGradient(canvas, x, y, mapper, ellipse.getWidth(), ellipse.getHeight(), back);
				canvas.fill(shape);
				RasterPaints.drawBorder(param, color, mapper, ellipse.getWidth(), ellipse.getHeight(), shape, canvas,
						x, y);
			} else {
				if (back.isTransparent() == false) {
					canvas.setColor(RasterPaints.argb(back, mapper));
					canvas.fill(shape);
				}
				if (color.isTransparent() == false && color.equals(back) == false)
					RasterPaints.drawBorder(param, color, mapper, ellipse.getWidth(), ellipse.getHeight(), shape,
							canvas, x, y);
			}
		} else {
			final RasterPath arc = RasterPath.arc(x, y, ellipse.getWidth(), ellipse.getHeight(), ellipse.getStart(),
					ellipse.getExtend());
			if (color.isTransparent() == false) {
				canvas.setColor(RasterPaints.argb(color, mapper));
				canvas.stroke(arc);
			}
		}
	}

}
