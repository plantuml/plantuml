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
 * Revision $Revision: 4125 $
 *
 */
package net.sourceforge.plantuml.eggs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.UMotif;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class GraphicsPath {

	private final String path;

	// private final Font numberFont = new Font("SansSerif", Font.BOLD, 20);
	// private final Font font = new Font("SansSerif", Font.PLAIN, 11);

	public GraphicsPath(String path) {
		this.path = path;
	}

	public void writeImage(OutputStream os) throws IOException {
		final BufferedImage im = createImage();
		PngIO.write(im, os, 96);
	}

	private BufferedImage createImage() {
		final EmptyImageBuilder builder = new EmptyImageBuilder(50, 50,
				Color.WHITE);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();
		
		final UGraphicG2d ug = new UGraphicG2d(g2d, im, 1.0);
		ug.getParam().setColor(Color.BLACK);
		final UMotif motif = new UMotif(path);
		motif.drawHorizontal(ug, 20, 20, 1);

		g2d.dispose();
		return im;
	}

}
