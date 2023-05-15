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
package net.sourceforge.plantuml.jcckit;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import jcckit.GraphicsPlotCanvas;
import jcckit.data.DataPlot;
import jcckit.util.ConfigParameters;
import jcckit.util.PropertiesBasedConfigData;
import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.security.SImageIO;

public class PSystemJcckit extends AbstractPSystem {
	// ::remove folder when __CORE__
	// ::remove folder when __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__

	private final PropertiesBasedConfigData prop;
	private final int width;
	private final int height;

	public PSystemJcckit(UmlSource source, Properties p, int width, int height) {
		super(source);
		this.width = width;
		this.height = height;
		prop = new PropertiesBasedConfigData(p);
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Sets up a Graphics2DPlotCanvas
		ConfigParameters config = new ConfigParameters(prop);
		GraphicsPlotCanvas plotCanvas = new GraphicsPlotCanvas(config, image);
		plotCanvas.connect(DataPlot.create(config));
		plotCanvas.paint();

		// Writes the off-screen image into a PNG file
		SImageIO.write(image, "png", os);

		return new ImageDataSimple(width, height);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(JCCKit)");
	}

}
