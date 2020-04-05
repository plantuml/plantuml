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
package net.sourceforge.plantuml.jcckit;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

import jcckit.GraphicsPlotCanvas;
import jcckit.data.DataPlot;
import jcckit.util.ConfigParameters;
import jcckit.util.PropertiesBasedConfigData;
import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;

public class PSystemJcckit extends AbstractPSystem {

	private final PropertiesBasedConfigData prop;
	private final int width;
	private final int height;

	public PSystemJcckit(Properties p, int width, int height) {
		this.width = width;
		this.height = height;
		prop = new PropertiesBasedConfigData(p);
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Sets up a Graphics2DPlotCanvas
		ConfigParameters config = new ConfigParameters(prop);
		GraphicsPlotCanvas plotCanvas = new GraphicsPlotCanvas(config, image);
		plotCanvas.connect(DataPlot.create(config));
		plotCanvas.paint();

		// Writes the off-screen image into a PNG file
		ImageIO.write(image, "png", os);

		return new ImageDataSimple(width, height);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(JCCKit)");
	}

}
