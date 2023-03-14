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
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.text.BackSlash;

public class PSystemDitaa extends AbstractPSystem {
	// ::remove folder when __CORE__

	// private ProcessingOptions processingOptions;
	private Object processingOptions;
	private final boolean dropShadows;
	private final String data;
	private final float scale;
	private final boolean transparentBackground;
	private final Font font;
	private final boolean forceFontSize;
	private final boolean performSeparationOfCommonEdges;
	private final boolean allCornersAreRound;

	public PSystemDitaa(UmlSource source, String data, boolean performSeparationOfCommonEdges, boolean dropShadows,
			boolean allCornersAreRound, boolean transparentBackground, float scale, Font font, boolean forceFontSize) {
		super(source);
		this.data = data;
		this.dropShadows = dropShadows;
		this.performSeparationOfCommonEdges = performSeparationOfCommonEdges;
		this.allCornersAreRound = allCornersAreRound;
		try {
			this.processingOptions = Class.forName("org.stathissideris.ascii2image.core.ProcessingOptions")
					.newInstance();
			// this.processingOptions.setPerformSeparationOfCommonEdges(performSeparationOfCommonEdges);
			this.processingOptions.getClass().getMethod("setPerformSeparationOfCommonEdges", boolean.class)
					.invoke(this.processingOptions, performSeparationOfCommonEdges);
			this.processingOptions.getClass().getMethod("setAllCornersAreRound", boolean.class)
					.invoke(this.processingOptions, allCornersAreRound);
		} catch (Exception e) {
			Logme.error(e);
			this.processingOptions = null;
		}
		this.transparentBackground = transparentBackground;
		this.scale = scale;
		this.font = font;
		this.forceFontSize = forceFontSize;
	}

	PSystemDitaa add(String line) {
		return new PSystemDitaa(getSource(), data + line + BackSlash.NEWLINE, performSeparationOfCommonEdges,
				dropShadows, allCornersAreRound, transparentBackground, scale, font, forceFontSize);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Ditaa)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {
		if (fileFormat.getFileFormat() == FileFormat.ATXT) {
			os.write(getSource().getPlainString(BackSlash.lineSeparator()).getBytes());
			return ImageDataSimple.ok();
		}

		// ditaa can only export png so file format is mostly ignored
		try {
			// ditaa0_9.jar
			// final ConversionOptions options = new ConversionOptions();
			final Object options = Class.forName("org.stathissideris.ascii2image.core.ConversionOptions").newInstance();

			// final RenderingOptions renderingOptions = options.renderingOptions;
			final Field f_renderingOptions = options.getClass().getField("renderingOptions");
			final Object renderingOptions = f_renderingOptions.get(options);

			// renderingOptions.setBackgroundColor(font);
			final Method setBackgroundColor = renderingOptions.getClass().getMethod("setBackgroundColor", Color.class);
			setBackgroundColor.invoke(renderingOptions, transparentBackground ? new Color(0, 0, 0, 0) : Color.WHITE);

			// renderingOptions.setFont(font);
			final Method setFont = renderingOptions.getClass().getMethod("setFont", Font.class);
			setFont.invoke(renderingOptions, font);

			// renderingOptions.setForceFontSize(font);
			final Method setForceFontSize = renderingOptions.getClass().getMethod("setForceFontSize", boolean.class);
			setForceFontSize.invoke(renderingOptions, forceFontSize);

			// renderingOptions.setScale(scale);
			final Method setScale = renderingOptions.getClass().getMethod("setScale", float.class);
			setScale.invoke(renderingOptions, scale);

			// options.setDropShadows(dropShadows);
			final Method setDropShadows = options.getClass().getMethod("setDropShadows", boolean.class);
			setDropShadows.invoke(options, dropShadows);

			// final TextGrid grid = new TextGrid();
			final Object grid = Class.forName("org.stathissideris.ascii2image.text.TextGrid").newInstance();

			// grid.initialiseWithText(data, null);
			final Method initialiseWithText = grid.getClass().getMethod("initialiseWithText", String.class,
					Class.forName("org.stathissideris.ascii2image.core.ProcessingOptions"));
			initialiseWithText.invoke(grid, data, null);

			// final Diagram diagram = new Diagram(grid, options, processingOptions);
			final Class<?> clDiagram = Class.forName("org.stathissideris.ascii2image.graphics.Diagram");
			clDiagram.getConstructor(grid.getClass(), options.getClass(), processingOptions.getClass())
					.newInstance(grid, options, processingOptions);
			final Object diagram = clDiagram
					.getConstructor(grid.getClass(), options.getClass(), processingOptions.getClass())
					.newInstance(grid, options, processingOptions);

			// final BitmapRenderer bitmapRenderer = new BitmapRenderer();
			final Object bitmapRenderer = Class.forName("org.stathissideris.ascii2image.graphics.BitmapRenderer")
					.newInstance();

			// final BufferedImage image = (BufferedImage)
			// bitmapRenderer.renderToImage(diagram, renderingOptions);
			final Method renderToImage = bitmapRenderer.getClass().getMethod("renderToImage", diagram.getClass(),
					renderingOptions.getClass());
			final BufferedImage image = (BufferedImage) renderToImage.invoke(bitmapRenderer, diagram, renderingOptions);

			SImageIO.write(image, "png", os);
			final int width = image.getWidth();
			final int height = image.getHeight();
			return new ImageDataSimple(width, height);
		} catch (Throwable e) {
			final List<String> strings = new ArrayList<>();
			strings.add("DITAA has crashed");
			strings.add(" ");
			GraphvizCrash.youShouldSendThisDiagram(strings);
			strings.add(" ");
			UmlDiagram.exportDiagramError(os, e, new FileFormatOption(FileFormat.PNG), seed(), null, null, strings);
			return ImageDataSimple.error();
		}

	}

}
