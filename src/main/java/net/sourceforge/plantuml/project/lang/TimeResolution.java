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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.project.time.Month;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;

public abstract class TimeResolution {

	public static IRegex toRegexA_DD_MONTH_YYYY(String year, String month, String day) {
		return new RegexConcat( //
				new RegexLeaf(1, day, "([\\d]{1,2})"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(1, month, "(" + Month.getRegexString() + ")"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(1, year, "([\\d]{1,4})"));
	}

	public static IRegex toRegexB_YYYY_MM_DD(String year, String month, String day) {
		return new RegexConcat( //
				new RegexLeaf(1, year, "([\\d]{1,4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf(1, month, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf(1, day, "([\\d]{1,2})"));
	}

	public static IRegex toRegexC_MONTH_DD_YYYY(String year, String month, String day) {
		return new RegexConcat( //
				new RegexLeaf(1, month, "(" + Month.getRegexString() + ")"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(1, day, "([\\d]{1,2})"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(1, year, "([\\d]{1,4})"));
	}

}
