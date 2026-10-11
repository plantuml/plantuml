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
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

/**
 * As {@code DriverDotPathG2d}.
 */
public class DriverDotPathRaster implements UDriver<DotPath, RasterCanvas> {

	@Override
	public void draw(DotPath shape, double x, double y, ColorMapper mapper, UParam param, RasterCanvas canvas) {
		RasterPaints.setStroke(param.getStroke(), canvas);
		if (param.getColor().isTransparent())
			return;

		canvas.setColor(RasterPaints.argb(param.getColor(), mapper));
		// As DotPath.draw(Graphics2D): curves appended to a GeneralPath (float
		// coordinates), each one connected to the previous one
		final RasterPath p = new RasterPath();
		boolean first = true;
		double lastX = 0;
		double lastY = 0;
		for (XCubicCurve2D bez : shape.getBeziers()) {
			final double x1 = (float) (x + bez.getX1());
			final double y1 = (float) (y + bez.getY1());
			if (first)
				p.moveTo(x1, y1);
			else if (x1 != lastX || y1 != lastY)
				p.lineTo(x1, y1);
			lastX = (float) (x + bez.getX2());
			lastY = (float) (y + bez.getY2());
			p.cubicTo((float) (x + bez.getCtrlX1()), (float) (y + bez.getCtrlY1()), (float) (x + bez.getCtrlX2()),
					(float) (y + bez.getCtrlY2()), lastX, lastY);
			first = false;
		}
		canvas.stroke(p);
	}

}
