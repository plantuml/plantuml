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
import java.awt.Frame;
import java.awt.Graphics2D;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import com.plantuml.api.cheerpj.JsonResult;
import com.plantuml.api.cheerpj.StringBounderCanvas;
import com.plantuml.api.cheerpj.Utils;
import com.plantuml.api.cheerpj.WasmLog;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.g2d.UGraphicG2d;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.preproc.Defines;

public class DirectDraw {

	public static Frame frame;
	public static Graphics2D g2d;
	private static int frameWidth;
	private static int frameHeight;

	public static Object initArea(int width, int height) {
		final long start = System.currentTimeMillis();
		WasmLog.start = start;
		WasmLog.log("initCanvas");
		if (g2d == null) {
			frameWidth = width;
			frameHeight = height;
			frame = new Frame();
			frame.setUndecorated(true);
			frame.setSize(frameWidth, frameHeight);
			frame.setLayout(null);
			frame.setVisible(true);
			g2d = (Graphics2D) frame.getGraphics();
			WasmLog.log("initCanvas done = " + frame);
			return 45;
		}
		WasmLog.log("initCanvas skipped because it has already been done");
		return 47;
	}

	public static Object draw(String mode, String text) {
		final long start = System.currentTimeMillis();
		WasmLog.start = start;

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

			final HColor back = HColors.simple(Color.WHITE);
			final StringBounder stringBounder = new StringBounderCanvas(g2d);
			final UGraphicG2d ug = new UGraphicG2d(back, ColorMapper.IDENTITY, stringBounder, g2d, 1.0, FileFormat.PNG);
			WasmLog.log("...cleaning...");
			ug.apply(back).apply(back.bg()).draw(URectangle.build(frameWidth, frameHeight));
			WasmLog.log("...drawing...");

			system.exportDiagramGraphic(ug, new FileFormatOption(FileFormat.PNG));

			WasmLog.log("done!");

			return JsonResult.ok(start, null, system);

		} catch (Throwable t) {
			WasmLog.log("Fatal error " + t);
			return JsonResult.fromCrash(start, t);
		}

	}

}
