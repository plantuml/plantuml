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

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import com.plantuml.api.cheerpj.Base64OutputStream;
import com.plantuml.api.cheerpj.JsonResult;
import com.plantuml.api.cheerpj.Utils;
import com.plantuml.api.cheerpj.WasmLog;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.preproc.Defines;

//::revert when __CORE__
//import com.leaningtech.client.Global;
//::done

public class Png {

	public static Object convertToBlob(String mode, String text, String pathOut) {
		final long start = System.currentTimeMillis();
		WasmLog.start = start;
		WasmLog.log("Starting processing");

		try {
			FileFormatOption format = new FileFormatOption(FileFormat.PNG);
			if ("dark".equalsIgnoreCase(mode))
				format = format.withColorMapper(ColorMapper.DARK_MODE);
			text = Utils.cleanText(text);
			final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
					Defines.createEmpty(), new StringReader(text), null, "string");
			List<BlockUml> blocks = builder.getBlockUmls();

			if (blocks.size() == 0)
				return JsonResult.noDataFound(start);

			final Diagram system = blocks.get(0).getDiagram();
			if (system instanceof PSystemError) {
				final ErrorUml error = ((PSystemError) system).getFirstError();
				WasmLog.log("[" + error.getPosition() + "] " + error.getError());
				return JsonResult.fromError(start, (PSystemError) system);
			}

			WasmLog.log("...processing...");

			final FileOutputStream fos = new FileOutputStream(new File(pathOut));
			WasmLog.log("...loading data...");

			final ImageData imageData = system.exportDiagram(fos, 0, format);

			WasmLog.log("Done!");
			fos.close();

			return JsonResult.ok(start, imageData, system);

		} catch (Throwable t) {
			WasmLog.log("Fatal error " + t);
			return JsonResult.fromCrash(start, t);
		}
	}

	public static Object convertToBase64(String mode, String text) {
		final long start = System.currentTimeMillis();
		WasmLog.start = start;
		WasmLog.log("Starting processing");

		try {
			FileFormatOption format = new FileFormatOption(FileFormat.PNG);
			if ("dark".equalsIgnoreCase(mode))
				format = format.withColorMapper(ColorMapper.DARK_MODE);
			text = Utils.cleanText(text);
			final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
					Defines.createEmpty(), new StringReader(text), null, "string");
			List<BlockUml> blocks = builder.getBlockUmls();

			if (blocks.size() == 0)
				return JsonResult.noDataFound(start);

			final Diagram system = blocks.get(0).getDiagram();
			if (system instanceof PSystemError) {
				final ErrorUml error = ((PSystemError) system).getFirstError();
				WasmLog.log("[" + error.getPosition() + "] " + error.getError());
				return JsonResult.fromError(start, (PSystemError) system);
			}

			WasmLog.log("...processing...");

			final Base64OutputStream os64 = new Base64OutputStream();
			WasmLog.log("...loading data...");

			final ImageData imageData = system.exportDiagram(os64, 0, format);

			WasmLog.log("Done!");
			os64.close();

			final String base64 = os64.toString();
			// ::revert when __CORE__
			return base64;
			// return Global.JSString(base64);
			// ::done

		} catch (Throwable t) {
			WasmLog.log("Fatal error " + t);
			return JsonResult.fromCrash(start, t);
		}
	}

}
