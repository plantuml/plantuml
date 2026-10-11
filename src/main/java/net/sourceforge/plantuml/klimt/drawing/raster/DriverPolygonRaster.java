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
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

/**
 * As {@code DriverPolygonG2d}, without shadow nor pattern for now.
 */
public class DriverPolygonRaster implements UDriver<UPolygon, RasterCanvas> {

	@Override
	public void draw(UPolygon shape, double x, double y, ColorMapper mapper, UParam param, RasterCanvas canvas) {
		canvas.setStroke(param.getStroke().getThickness(), 0, 0);
		// java.awt.geom.GeneralPath uses float coordinates
		final RasterPath path = new RasterPath();
		boolean first = true;
		for (XPoint2D pt : shape.getPoints()) {
			final double xp = (float) (pt.getX() + x);
			final double yp = (float) (pt.getY() + y);
			if (first)
				path.moveTo(xp, yp);
			else
				path.lineTo(xp, yp);
			first = false;
		}
		if (first == false)
			path.closePath();

		final HColor back = param.getBackcolor();
		if (RasterPaints.isGradient(back)) {
			RasterPaints.setGradient(canvas, x, y, mapper, shape.getWidth(), shape.getHeight(), back);
			canvas.fill(path);
		} else if (back.isTransparent() == false) {
			canvas.setColor(RasterPaints.argb(back, mapper));
			canvas.fill(path);
		}

		if (param.getColor().isTransparent() == false) {
			canvas.setColor(RasterPaints.argb(param.getColor(), mapper));
			RasterPaints.setStroke(param.getStroke(), canvas);
			canvas.stroke(path);
		}
	}

}
