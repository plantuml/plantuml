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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.classdiagram.command.GenericRegexProducer;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;

public class NameAndCodeParser {

	private static final String DISPLAY = "[%g]([^%g]*)[%g]";

	private static final String DISPLAY_WITH_GENERIC = "[%g](.+?)(?:\\<(" + GenericRegexProducer.PATTERN + ")\\>)?[%g]";
	private static final String CODE = "[^%s{}%g<>]+";
	private static final String CODE_NO_DOTDOT = "[^%s{}%g<>:]+";

	public static IRegex nameAndCodeForClassWithGeneric() {
		return new RegexOr(//
				new RegexConcat(//
						new RegexLeaf(2, "DISPLAY1", DISPLAY_WITH_GENERIC), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "CODE1", "(" + CODE + ")")), //
				new RegexConcat(//
						new RegexLeaf(1, "CODE2", "(" + CODE + ")"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(2, "DISPLAY2", DISPLAY_WITH_GENERIC)), //
				new RegexLeaf(1, "CODE3", "(" + CODE + ")"), //
				new RegexLeaf(1, "CODE4", "[%g]([^%g]+)[%g]")); //

	}

	public static IRegex nameAndCode() {
		return new RegexOr(//
				new RegexConcat(//
						new RegexLeaf(1, "DISPLAY1", DISPLAY), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "CODE1", "(" + CODE + ")")), //
				new RegexConcat(//
						new RegexLeaf(1, "CODE2", "(" + CODE + ")"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "DISPLAY2", DISPLAY)), //
				new RegexLeaf(1, "CODE3", "(" + CODE + ")"), //
				new RegexLeaf(1, "CODE4", "[%g]([^%g]+)[%g]")); //

	}

	public static IRegex codeForClass() {
		return new RegexLeaf(1, "CODE", "(" + NameAndCodeParser.CODE + "|[%g][^%g]+[%g])");
	}

	public static IRegex codeWithMemberForClass() {
		return new RegexLeaf(2,
				"CODE", "(" + NameAndCodeParser.CODE_NO_DOTDOT + "|[%g][^%g]+[%g])::([%g][^%g]+[%g]|[^%s]+)");
	}

}
