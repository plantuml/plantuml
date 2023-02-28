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
package net.sourceforge.plantuml.eggs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UMotif;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.g2d.UGraphicG2d;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.png.PngIO;

public class GraphicsPath {
	// ::remove file when __CORE__

	private final String path;
	private final ColorMapper colorMapper;

	public GraphicsPath(ColorMapper colorMapper, String path) {
		this.path = path;
		this.colorMapper = colorMapper;
	}

	public ImageData writeImage(OutputStream os) throws IOException {
		final BufferedImage im = createImage();
		PngIO.write(im, colorMapper, os, null, 96);
		return new ImageDataSimple(im.getWidth(), im.getHeight());
	}

	private BufferedImage createImage() {
		final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();
		final EmptyImageBuilder builder = new EmptyImageBuilder(null, 50, 50, Color.WHITE, stringBounder);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		final UGraphicG2d ug = new UGraphicG2d(HColors.WHITE, colorMapper, stringBounder, g2d, 1.0, FileFormat.PNG);
		ug.setBufferedImage(im);
		final UMotif motif = new UMotif(path);
		motif.drawHorizontal(ug.apply((UChange) HColors.BLACK), 20, 20, 1);

		g2d.dispose();
		return im;
	}

}
