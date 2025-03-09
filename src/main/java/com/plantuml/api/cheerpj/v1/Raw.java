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
package com.plantuml.api.cheerpj.v1;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import com.plantuml.api.cheerpj.JsonResult;
import com.plantuml.api.cheerpj.Utils;
import com.plantuml.api.cheerpj.WasmLog;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.g2d.UGraphicG2d;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.preproc.Defines;

public class Raw {

	private static final int MAX = 2000;
	static private BufferedImage im;
	static private Graphics2D g2d;

	public static Object convertToBlob(String mode, String text, String pathOut) {
		final long start = System.currentTimeMillis();
		WasmLog.start = start;
		WasmLog.log("Starting processing");

		try {
			text = Utils.cleanText(text);
			final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
					Defines.createEmpty(), new StringReader(text), null, "string");
			List<BlockUml> blocks = builder.getBlockUmls();

			if (blocks.size() == 0)
				return JsonResult.noDataFound(start);

			WasmLog.log("...loading data...");

			final Diagram system = blocks.get(0).getDiagram();
			if (system instanceof PSystemError) {
				final ErrorUml error = ((PSystemError) system).getFirstError();
				WasmLog.log("[" + error.getPosition() + "] " + error.getError());
				return JsonResult.fromError(start, (PSystemError) system);
			}

			WasmLog.log("...processing...");

			final boolean dark = "dark".equalsIgnoreCase(mode);

			final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();

			if (im == null) {
				final EmptyImageBuilder imageBuilder = new EmptyImageBuilder(null, MAX, MAX, Color.WHITE,
						stringBounder);
				im = imageBuilder.getBufferedImage();
				g2d = im.createGraphics();
			}

			final HColor back;
			final ColorMapper mapper;
			if (dark) {
				back = HColors.simple(Color.BLACK);
				mapper = ColorMapper.DARK_MODE;
			} else {
				back = HColors.simple(Color.WHITE);
				mapper = ColorMapper.IDENTITY;
			}
			final UGraphicG2d ug = new UGraphicG2d(back, mapper, stringBounder, g2d, 1.0, FileFormat.RAW);
			WasmLog.log("...cleaning...");
			ug.apply(back).apply(back.bg()).draw(URectangle.build(MAX, MAX));
			ug.resetMax();
			WasmLog.log("...drawing...");

			system.exportDiagramGraphic(ug, new FileFormatOption(FileFormat.PNG));

			final int width = (int) (2 + ug.getMaxX());
			final int height = (int) (2 + ug.getMaxY());
			WasmLog.log("...size is " + width + " x " + height + " ...");

			final FileOutputStream fos = new FileOutputStream(new File(pathOut));

			fos.write(((width & 0xFF00) >> 8));
			fos.write((width & 0xFF));
			fos.write(((height & 0xFF00) >> 8));
			fos.write((height & 0xFF));

			for (int j = 0; j < height; j++)
				for (int i = 0; i < width; i++) {
					int pixel = im.getRGB(i, j);
					fos.write(((pixel & 0xFF0000) >> 16));
					fos.write(((pixel & 0x00FF00) >> 8));
					fos.write((pixel & 0xFF));
					fos.write((255));
//				// line.scanline[j++] = (((sample & 0xFF000000) >> 24) & 0xFF); // A
				}

			WasmLog.log("done!");
			final ImageData imageData = new ImageDataSimple(width, height);
			return JsonResult.ok(start, imageData, system);

		} catch (Throwable t) {
			WasmLog.log("Fatal error " + t);
			return JsonResult.fromCrash(start, t);
		}
	}

	public static Object convert(String mode, String text) {
		final long start = System.currentTimeMillis();
		WasmLog.start = start;
		WasmLog.log("Starting processing");

		try {
			text = Utils.cleanText(text);
			final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
					Defines.createEmpty(), new StringReader(text), null, "string");
			List<BlockUml> blocks = builder.getBlockUmls();

			if (blocks.size() == 0)
				return JsonResult.noDataFound(start);

			WasmLog.log("...loading data...");

			final Diagram system = blocks.get(0).getDiagram();
			if (system instanceof PSystemError) {
				final ErrorUml error = ((PSystemError) system).getFirstError();
				WasmLog.log("[" + error.getPosition() + "] " + error.getError());
				return JsonResult.fromError(start, (PSystemError) system);
			}

			WasmLog.log("...processing...");

			final boolean dark = "dark".equalsIgnoreCase(mode);

			final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();

			if (im == null) {
				final EmptyImageBuilder imageBuilder = new EmptyImageBuilder(null, MAX, MAX, Color.WHITE,
						stringBounder);
				im = imageBuilder.getBufferedImage();
				g2d = im.createGraphics();
			}

			final HColor back;
			final ColorMapper mapper;
			if (dark) {
				back = HColors.simple(Color.BLACK);
				mapper = ColorMapper.DARK_MODE;
			} else {
				back = HColors.simple(Color.WHITE);
				mapper = ColorMapper.IDENTITY;
			}
			final UGraphicG2d ug = new UGraphicG2d(back, mapper, stringBounder, g2d, 1.0, FileFormat.RAW);
			WasmLog.log("...cleaning...");
			ug.apply(back).apply(back.bg()).draw(URectangle.build(MAX, MAX));
			ug.resetMax();
			WasmLog.log("...drawing...");

			system.exportDiagramGraphic(ug, new FileFormatOption(FileFormat.PNG));

			final int width = (int) (2 + ug.getMaxX());
			final int height = (int) (2 + ug.getMaxY());
			WasmLog.log("...size is " + width + " x " + height + " ...");

			final byte[] data = new byte[width * height * 4];
			WasmLog.log("...allocating array...");

			int pos = 0;
			data[pos++] = (byte) ((width & 0xFF00) >> 8);
			data[pos++] = (byte) (width & 0xFF);
			data[pos++] = (byte) ((height & 0xFF00) >> 8);
			data[pos++] = (byte) (height & 0xFF);

			for (int j = 0; j < height; j++)
				for (int i = 0; i < width; i++) {
					int pixel = im.getRGB(i, j);
					data[pos++] = (byte) ((pixel & 0xFF0000) >> 16);
					data[pos++] = (byte) ((pixel & 0x00FF00) >> 8);
					data[pos++] = (byte) (pixel & 0xFF);
					data[pos++] = (byte) (255);
//					// line.scanline[j++] = (((sample & 0xFF000000) >> 24) & 0xFF); // A
				}

			WasmLog.log("done!");

			return data;

		} catch (Throwable t) {
			WasmLog.log("Fatal error " + t);
			return JsonResult.fromCrash(start, t);
		}
	}

}
