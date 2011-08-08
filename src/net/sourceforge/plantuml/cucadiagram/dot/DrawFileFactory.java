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
 * Revision $Revision: 3977 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class DrawFileFactory {

	public static DrawFile create(final ColorMapper colorMapper, final UDrawable drawable, final double width, final double height,
			final double dpiFactor, final Color backgground, Object signature) {

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final File png = FileUtils.createTempFile("visi", ".png");
				final EmptyImageBuilder builder = new EmptyImageBuilder(width * dpiFactor, height * dpiFactor,
						backgground);
				final BufferedImage im = builder.getBufferedImage();
				drawable.drawU(new UGraphicG2d(colorMapper, builder.getGraphics2D(), im, dpiFactor));
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File eps = FileUtils.createTempFile("visi", ".eps");
				UGraphicEps.copyEpsToFile(colorMapper, drawable, eps);
				return eps;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(colorMapper, drawable);
			}
		};

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

}
