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
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.UImage;

/**
 * As {@code DriverImageG2d}: the image is drawn at the resolution of the
 * output, on the device grid.
 */
public class DriverImageRaster implements UDriver<UImage, RasterCanvas> {

	private final double dpiFactor;

	public DriverImageRaster(double dpiFactor) {
		this.dpiFactor = dpiFactor;
	}

	@Override
	public void draw(UImage shape, double x, double y, ColorMapper mapper, UParam param, RasterCanvas canvas) {
		final PortableImage image = shape.getImage(dpiFactor);
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int[] argb = new int[width * height];
		for (int j = 0; j < height; j++)
			for (int i = 0; i < width; i++)
				argb[j * width + i] = image.getRGB(i, j);

		if (dpiFactor == 1)
			canvas.drawImage(argb, width, height, (int) x, (int) y, 1);
		else
			// The canvas is scaled by dpiFactor: one pixel of the image is one device pixel
			canvas.drawImage(argb, width, height, (int) (x * dpiFactor) / dpiFactor, (int) (y * dpiFactor) / dpiFactor,
					1 / dpiFactor);
	}

}
