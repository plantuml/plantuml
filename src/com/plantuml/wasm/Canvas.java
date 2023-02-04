/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package com.plantuml.wasm;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.URectangle;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class Canvas {

	public static Frame frame;
	public static Graphics2D g2d;
	private static int frameWidth;
	private static int frameHeight;

	public static int initCanvas(int width, int height) throws IOException {
		WasmLog.start = System.currentTimeMillis();
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

	public static int convertCanvas(int width, int height, String text) throws IOException {
		WasmLog.start = System.currentTimeMillis();

		WasmLog.log("1frame= " + frame);
		if (g2d == null) {
			frameWidth = width;
			frameHeight = height;
			frame = new Frame();
			frame.setUndecorated(true);
			frame.setSize(frameWidth, frameHeight);
			frame.setLayout(null);
			frame.setVisible(true);
			g2d = (Graphics2D) frame.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			WasmLog.log("3frame= " + frame);
			return 45;
		}

		final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
				Defines.createEmpty(), new StringReader(text), null, "string");
		List<BlockUml> blocks = builder.getBlockUmls();

		WasmLog.log("...loading data...");

		final Diagram system = blocks.get(0).getDiagram();

		if (system instanceof PSystemError) {
			final ErrorUml error = ((PSystemError) system).getFirstError();
			WasmLog.log("[" + error.getPosition() + "] " + error.getError());
			return -242;
		}
		WasmLog.log("...processing...");

		final HColor back = HColors.simple(Color.WHITE);
		final StringBounder stringBounder = new StringBounderCanvas(g2d);
		final UGraphicG2d ug = new UGraphicG2d(back, ColorMapper.IDENTITY, stringBounder, g2d, 1.0);
		// ug.apply(back).apply(back.bg()).draw(new URectangle(frameWidth,
		// frameHeight));
		ug.apply(HColors.RED).apply(back.bg()).draw(new URectangle(frameWidth, frameHeight));
		WasmLog.log("system= " + system.getClass().getName());

		system.exportDiagramGraphic(ug);

		return 43;
	}

}
