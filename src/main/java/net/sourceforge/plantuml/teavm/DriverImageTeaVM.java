/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.UImage;

/**
 * TeaVM driver for rendering images in SVG. Converts PortableImage to PNG data
 * URL using browser's canvas API, then embeds as an SVG image element.
 */
public class DriverImageTeaVM implements UDriver<UImage, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverImageTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UImage shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		final PortableImage image = shape.getImage(1.0);
		final int width = image.getWidth();
		final int height = image.getHeight();

		// Convert image to PNG data URL via canvas
		final String dataUrl = image.toPngDataUrl();

		// Draw as embedded image in SVG
		svg.drawImage(dataUrl, x, y, width, height);
	}
}
