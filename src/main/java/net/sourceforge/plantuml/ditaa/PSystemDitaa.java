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
 */
package net.sourceforge.plantuml.ditaa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.graphics.BitmapRenderer;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.CrashReportHandler;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.utils.BlocLines;

public class PSystemDitaa extends AbstractPSystem {
	// ::remove folder when __CORE__

	private final ConversionOptions options = new ConversionOptions();
	private final List<String> data = new ArrayList<>();
	private int nbStartingSpace = Integer.MAX_VALUE;

	public PSystemDitaa(UmlSource source, boolean performSeparationOfCommonEdges, boolean dropShadows,
			boolean allCornersAreRound, boolean transparentBackground, float scale,
			PreprocessingArtifact preprocessing) {
		super(source, preprocessing);

		options.setDropShadows(dropShadows);
		options.renderingOptions.setBackgroundColor(transparentBackground ? new Color(0, 0, 0, 0) : Color.WHITE);
		options.renderingOptions.setScale(scale);
		options.processingOptions.setPerformSeparationOfCommonEdges(performSeparationOfCommonEdges);
		options.processingOptions.setAllCornersAreRound(allCornersAreRound);

	}

	void add(String line) {
		data.add(line);
		nbStartingSpace = Math.min(nbStartingSpace, BlocLines.nbStartingSpace(line));
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Ditaa)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {

		try {
			final TextGrid grid = new TextGrid();

			final ArrayList<StringBuilder> lines = new ArrayList<StringBuilder>();
			for (String s : data)
				lines.add(new StringBuilder(s.substring(nbStartingSpace)));

			grid.initialiseWithLines(lines, null);

			final Diagram diagram = new Diagram(grid, options);
			final BitmapRenderer bitmapRenderer = new BitmapRenderer();

			final BufferedImage image = (BufferedImage) bitmapRenderer.renderToImage(diagram, options.renderingOptions);

			if (fileFormat.getFileFormat() == FileFormat.ATXT) {
				os.write(getSource().getPlainString(BackSlash.lineSeparator()).getBytes());
				return ImageDataSimple.ok();
			}

			SImageIO.write(image, "png", os);
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		} catch (Throwable e) {
			final CrashReportHandler report = new CrashReportHandler(e, null, null);
			report.add("DITAA has crashed");
			report.addEmptyLine();
			report.youShouldSendThisDiagram();
			report.addEmptyLine();
			report.exportDiagramError(fileFormat, seed(), os);
			return ImageDataSimple.error();
		}
	}
}
