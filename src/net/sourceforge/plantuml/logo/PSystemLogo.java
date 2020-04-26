/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.logo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class PSystemLogo extends AbstractPSystem {

	private final List<String> lines = new ArrayList<String>();

	public PSystemLogo() {
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final int width = 640;
		final int height = 480;
		final EmptyImageBuilder builder = new EmptyImageBuilder(fileFormat.getWatermark(), width, height, Color.WHITE);
		final BufferedImage im = builder.getBufferedImage();
		final UGraphic ug = new UGraphicG2d(new ColorMapperIdentity(), builder.getGraphics2D(), 1.0);
		((UGraphicG2d) ug).setBufferedImage(im);

		final TurtleGraphicsPane turtleGraphicsPane = new TurtleGraphicsPane(width, height);
		final TinyJavaLogo tinyJavaLogo = new TinyJavaLogo(turtleGraphicsPane);
		for (String line : lines) {
			tinyJavaLogo.doCommandLine(line);
		}
		turtleGraphicsPane.paint(ug);
		PngIO.write(im, os, 96);
		return new ImageDataSimple(im.getWidth(), im.getHeight());
	}

	// private GraphicStrings getGraphicStrings() throws IOException {
	// final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
	// final GraphicStrings result = new GraphicStrings(strings, font,
	// HtmlColorUtils.BLACK, HtmlColorUtils.WHITE,
	// image,
	// GraphicPosition.BOTTOM, false);
	// result.setMinWidth(200);
	// return result;
	// }

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Logo)");
	}

	public void doCommandLine(String line) {
		lines.add(line);
	}

}
