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
 *
 */
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;

public class Verbs {

	public static IRegex are() {
		return new RegexLeaf("are");
	}

	public static IRegex areColored() {
		return new RegexLeaf("are[%s]+colou?red");
	}

	public static IRegex displayOnSameRowAs() {
		return new RegexLeaf("displays?[%s]+on[%s]+same[%s]+row[%s]+as");
	}

	public static IRegex ends() {
		return new RegexLeaf("ends");
	}

	public static IRegex ends2() {
		return new RegexLeaf("ends[%s]*(the[%s]*|on[%s]*|at[%s]*)*");
	}

	public static IRegex happens() {
		return new RegexLeaf("happens?[%s]*(at[%s]*|the[%s]*|on[%s]*)*");
	}

	public static IRegex pauses() {
		return new RegexLeaf("pauses?[%s]*(at[%s]*|the[%s]*|on[%s]*|from[%s]*)*");
	}

	public static IRegex isDeleted() {
		return new RegexLeaf("is[%s]+deleted");
	}

	public static IRegex is() {
		return new RegexLeaf("is");
	}

	public static IRegex isColored() {
		return new RegexLeaf("is[%s]+colou?red");
	}

	public static IRegex isColoredForCompletion() {
		return new RegexLeaf("is[%s]+colou?red[%s]+for[%s]+completion");
	}

	public static IRegex isOff() {
		return new RegexConcat(new RegexLeaf("is"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("off"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexLeaf("on"), //
						new RegexLeaf("for"), //
						new RegexLeaf("the"), //
						new RegexLeaf("at") //
				));
	}

	public static IRegex isOn() {
		return new RegexConcat(new RegexLeaf("is"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("on"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexLeaf("on"), //
						new RegexLeaf("for"), //
						new RegexLeaf("the"), //
						new RegexLeaf("at") //
				) //
		);
	}

	public static IRegex isOrAre() {
		return new RegexLeaf("(is|are)");
	}

	public static IRegex isOrAreNamed() {
		return new RegexLeaf("(is|are)[%s]+named");
	}

	public static IRegex lasts() {
		return new RegexLeaf("lasts");
	}

	public static IRegex linksTo() {
		return new RegexLeaf("links to");
	}

	public static IRegex occurs() {
		return new RegexLeaf("occurs?");
	}

	public static IRegex starts3() {
		return new RegexLeaf("starts[%s]*(the[%s]*|on[%s]*|at[%s]*)*");
	}

	public static IRegex starts2() {
		return new RegexLeaf("starts");
	}

	public static IRegex starts() {
		return new RegexConcat(new RegexLeaf("start"), //
				new RegexOptional(new RegexLeaf("s")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexOr(//
						new RegexLeaf("on"), //
						new RegexLeaf("for"), //
						new RegexLeaf("the"), //
						new RegexLeaf("at") //
				)) //
		);
	}

}
