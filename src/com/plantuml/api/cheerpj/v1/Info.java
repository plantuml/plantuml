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

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import com.plantuml.api.cheerpj.JsonResult;
import com.plantuml.api.cheerpj.Utils;
import com.plantuml.api.cheerpj.WasmLog;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.Defines;

//::revert when __CORE__
//import com.leaningtech.client.Global;
//::done

public class Info {

	public static Object decode(String text) {
		String result;
		try {
			result = TranscoderUtil.getDefaultTranscoder().decode(text);
		} catch (NoPlantumlCompressionException e) {
			e.printStackTrace();
			result = "";
		}
		// ::revert when __CORE__
		return result;
		// return Global.JSString(result);
		// ::done
	}

	public static Object encode(String text) {
		String result;
		try {
			result = TranscoderUtil.getDefaultTranscoder().encode(text);
		} catch (IOException e) {
			e.printStackTrace();
			result = "";
		}
		// ::revert when __CORE__
		return result;
		// return Global.JSString(result);
		// ::done
	}

	public static Object syntaxCheck(String text) {
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

			final Diagram system = blocks.get(0).getDiagram();
			if (system instanceof PSystemError) {
				final ErrorUml error = ((PSystemError) system).getFirstError();
				WasmLog.log("[" + error.getPosition() + "] " + error.getError());
				return JsonResult.fromError(start, (PSystemError) system);
			}

			return JsonResult.ok(start, null, system);

		} catch (Throwable t) {
			WasmLog.log("Fatal error " + t);
			return JsonResult.fromCrash(start, t);
		}
	}

}
