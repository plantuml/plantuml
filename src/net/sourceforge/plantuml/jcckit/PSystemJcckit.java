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
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.jcckit;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

public class PSystemJcckit extends AbstractPSystem {

	private final PropertiesBasedConfigData prop;
	private final int width;
	private final int height;

	public PSystemJcckit(Properties p, int width, int height) {
		this.width = width;
		this.height = height;
		prop = new PropertiesBasedConfigData(p);
	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat)
			throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Sets up a Graphics2DPlotCanvas
		ConfigParameters config = new ConfigParameters(prop);
		GraphicsPlotCanvas plotCanvas = new GraphicsPlotCanvas(config, image);
		plotCanvas.connect(DataPlot.create(config));
		plotCanvas.paint();

		// Writes the off-screen image into a PNG file
		ImageIO.write(image, "png", os);
	}

	public String getDescription() {
		return "(JCCKit)";
	}

}
