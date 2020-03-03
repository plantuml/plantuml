/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 */
package net.sourceforge.plantuml.tim;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.tim.iterator.CodePosition;

public class ExecutionContextForeach {

	private final String varname;
	private final JsonArray jsonArray;
	private final CodePosition codePosition;
	private boolean skipMe;
	private int currentIndex;

	private ExecutionContextForeach(String varname, JsonArray jsonArray, CodePosition codePosition) {
		this.varname = varname;
		this.jsonArray = jsonArray;
		this.codePosition = codePosition;
	}

	public static ExecutionContextForeach fromValue(String varname, JsonArray jsonArray, CodePosition codePosition) {
		return new ExecutionContextForeach(varname, jsonArray, codePosition);
	}

	public void skipMeNow() {
		skipMe = true;
	}

	public final boolean isSkipMe() {
		return skipMe;
	}

	public CodePosition getStartForeach() {
		return codePosition;
	}

	public final int currentIndex() {
		return currentIndex;
	}

	public final void inc() {
		this.currentIndex++;
		if (currentIndex >= jsonArray.size()) {
			this.skipMe = true;
		}
	}

	public final String getVarname() {
		return varname;
	}

	public final JsonArray getJsonArray() {
		return jsonArray;
	}

}
