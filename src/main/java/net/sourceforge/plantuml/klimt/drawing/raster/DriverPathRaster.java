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
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.USegment;
import net.sourceforge.plantuml.klimt.geom.USegmentType;

/**
 * As {@code DriverPathG2d}, without shadow for now.
 */
public class DriverPathRaster implements UDriver<UPath, RasterCanvas> {

	@Override
	public void draw(UPath shape, double x, double y, ColorMapper mapper, UParam param, RasterCanvas canvas) {
		RasterPaints.setStroke(param.getStroke(), canvas);
		final HColor back = param.getBackcolor();
		final HColor color = param.getColor();

		final RasterPath p = new RasterPath();
		MinMax minMax = MinMax.getEmpty(false);
		minMax = minMax.addPoint(x, y);
		for (USegment seg : shape) {
			final USegmentType type = seg.getSegmentType();
			final double coord[] = seg.getCoord();
			if (type == USegmentType.SEG_MOVETO) {
				p.moveTo(x + coord[0], y + coord[1]);
				minMax = minMax.addPoint(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_LINETO) {
				p.lineTo(x + coord[0], y + coord[1]);
				minMax = minMax.addPoint(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_CUBICTO) {
				p.cubicTo(x + coord[0], y + coord[1], x + coord[2], y + coord[3], x + coord[4], y + coord[5]);
				minMax = minMax.addPoint(x + coord[4], y + coord[5]);
			} else if (type == USegmentType.SEG_ARCTO) {
				p.arcTo(coord[0], coord[1], coord[2], coord[3] != 0, coord[4] != 0, x + coord[5], y + coord[6]);
			} else if (type == USegmentType.SEG_CLOSE) {
				p.closePath();
			} else {
				throw new UnsupportedOperationException();
			}
		}

		if (RasterPaints.isGradient(back)) {
			final double width = minMax.getMaxX() - minMax.getMinX();
			final double height = minMax.getMaxY() - minMax.getMinY();
			RasterPaints.setGradient(canvas, minMax.getMinX(), minMax.getMinY(), mapper, width, height, back);
			canvas.fill(p);
		} else if (back.isTransparent() == false) {
			canvas.setColor(RasterPaints.argb(back, mapper));
			canvas.fill(p);
		}

		if (color.isTransparent() == false && color.equals(back) == false) {
			canvas.setColor(RasterPaints.argb(color, mapper));
			canvas.stroke(p);
		}
	}

}
