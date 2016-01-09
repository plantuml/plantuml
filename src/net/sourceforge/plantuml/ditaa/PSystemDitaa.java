/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;

import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.core.ProcessingOptions;
import org.stathissideris.ascii2image.graphics.BitmapRenderer;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

public class PSystemDitaa extends AbstractPSystem {

	private final ProcessingOptions processingOptions = new ProcessingOptions();
	private final boolean dropShadows;
	private final String data;

	public PSystemDitaa(String data, boolean performSeparationOfCommonEdges, boolean dropShadows) {
		this.data = data;
		this.dropShadows = dropShadows;
		this.processingOptions.setPerformSeparationOfCommonEdges(performSeparationOfCommonEdges);
	}

	PSystemDitaa add(String line) {
		return new PSystemDitaa(data + line + "\n", processingOptions.performSeparationOfCommonEdges(), dropShadows);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Ditaa)", getClass());
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		if (fileFormat.getFileFormat() == FileFormat.ATXT) {
			os.write(getSource().getPlainString().getBytes());
			return new ImageDataSimple();
		}
		// ditaa can only export png so file format is mostly ignored
		final ConversionOptions options = new ConversionOptions();
		options.setDropShadows(dropShadows);
		final TextGrid grid = new TextGrid();
		grid.initialiseWithText(data, null);
		final Diagram diagram = new Diagram(grid, options, processingOptions);
		final BufferedImage image = (BufferedImage) new BitmapRenderer().renderToImage(diagram,
				options.renderingOptions);
		ImageIO.write(image, "png", os);
		return new ImageDataSimple(image.getWidth(), image.getHeight());

	}

}
