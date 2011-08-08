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
 */
package net.sourceforge.plantuml.ditaa;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;

import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.core.ProcessingOptions;
import org.stathissideris.ascii2image.graphics.BitmapRenderer;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

public class PSystemDitaa extends AbstractPSystem {

	private final TextGrid grid = new TextGrid();
	private final ProcessingOptions processingOptions = new ProcessingOptions();

	public PSystemDitaa(String data, boolean performSeparationOfCommonEdges) throws UnsupportedEncodingException {
		grid.initialiseWithText(data, null);
		processingOptions.setPerformSeparationOfCommonEdges(performSeparationOfCommonEdges);
	}

	public String getDescription() {
		return "(Ditaa)";
	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormatOption)
			throws IOException {

		final ConversionOptions options = new ConversionOptions();
		final Diagram diagram = new Diagram(grid, options, processingOptions);
		final BufferedImage image = (BufferedImage) new BitmapRenderer().renderToImage(diagram,
				options.renderingOptions);
		ImageIO.write(image, "png", os);

	}

}
