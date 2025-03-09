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
package com.plantuml.api.cheerpj;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;

//::revert when __CORE__
//import com.leaningtech.client.Global;
//::done

public class JsonResult {

	private final StringBuilder sb = new StringBuilder();

	private JsonResult(long startingTime) {
		sb.append("{");
		this.append("duration", System.currentTimeMillis() - startingTime);
	}

	private Object done() {
		sb.append("}");
		// ::revert when __CORE__
		return sb.toString();
		// return Global.JSString(sb.toString());
		// ::done
	}

	public static Object noDataFound(long startingTime) {
		final JsonResult res = new JsonResult(startingTime);
		res.append("status", "No data found");
		return res.done();
	}

	public static Object fromCrash(long startingTime, Throwable t) {
		final JsonResult res = new JsonResult(startingTime);
		res.append("status", "General failure");
		res.append("exception", t.toString());
		return res.done();
	}

	public static Object ok(long startingTime, ImageData imageData, Diagram diagram) {
		final JsonResult res = new JsonResult(startingTime);
		res.append("status", "ok");
		if (imageData != null) {
			res.append("width", imageData.getWidth());
			res.append("height", imageData.getHeight());
		}
		res.append("description", diagram.getDescription().getDescription());
		return res.done();
	}

	public static Object fromError(long startingTime, PSystemError system) {
		final JsonResult res = new JsonResult(startingTime);
		res.append("status", "Parsing error");

		final ErrorUml err = system.getErrorsUml().iterator().next();
		res.append("line", err.getPosition());
		res.append("error", err.getError());

		return res.done();
	}

	private void append(String key, String value) {
		appendKeyOnly(key);
		sb.append('\"');
		sb.append(value);
		sb.append('\"');
	}

	private void append(String key, long value) {
		appendKeyOnly(key);
		sb.append(value);
	}

	protected void appendKeyOnly(String key) {
		if (sb.length() > 1)
			sb.append(',');
		sb.append('\"');
		sb.append(key);
		sb.append('\"');
		sb.append(':');
	}

}
