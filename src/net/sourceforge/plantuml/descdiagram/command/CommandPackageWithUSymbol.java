/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackageWithUSymbol extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageWithUSymbol() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("SYMBOL",
						"(package|rectangle|node|artifact|folder|file|frame|cloud|database|storage|component|card|together)"), //
				new RegexLeaf("[%s]+"), //
				new RegexOr(//
						new RegexConcat( //
								new RegexLeaf("DISPLAY1", "([%g][^%g]+[%g])"), //
								new RegexLeaf("STEREOTYPE1", "(?:[%s]+(\\<\\<.+\\>\\>))?"), //
								new RegexLeaf("[%s]*as[%s]+"), //
								new RegexLeaf("CODE1", "([^#%s{}]+)") //
						), //
						new RegexConcat( //
								new RegexLeaf("CODE2", "([^#%s{}%g]+)"), //
								new RegexLeaf("STEREOTYPE2", "(?:[%s]+(\\<\\<.+\\>\\>))?"), //
								new RegexLeaf("[%s]*as[%s]+"), //
								new RegexLeaf("DISPLAY2", "([%g][^%g]+[%g])") //
						), //
						new RegexConcat( //
								new RegexLeaf("DISPLAY3", "([^#%s{}%g]+)"), //
								new RegexLeaf("STEREOTYPE3", "(?:[%s]+(\\<\\<.+\\>\\>))?"), //
								new RegexLeaf("[%s]*as[%s]+"), //
								new RegexLeaf("CODE3", "([^#%s{}%g]+)") //
						), //
						new RegexLeaf("CODE8", "([%g][^%g]+[%g])"), //
						new RegexLeaf("CODE9", "([^#%s{}%g]*)") //
				), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("TAGS", Stereotag.pattern() + "?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*\\{$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, RegexResult arg) {
		final String codeRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("CODE", 0));
		final String displayRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("DISPLAY", 0));
		final Code code;
		final String display;
		if (codeRaw.length() == 0) {
			code = UniqueSequence.getCode("##");
			display = null;
		} else {
			code = Code.of(codeRaw);
			if (displayRaw == null) {
				display = code.getFullName();
			} else {
				display = displayRaw;
			}
		}

		final IGroup currentPackage = diagram.getCurrentGroup();
		diagram.gotoGroup2(code, Display.getWithNewlines(display), GroupType.PACKAGE, currentPackage,
				NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();
		p.setUSymbol(USymbol.getFromString(arg.get("SYMBOL", 0)));
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype, false));
		}
		CommandCreateClassMultilines.addTags(p, arg.get("TAGS", 0));
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		p.setColors(colors);
		return CommandExecutionResult.ok();
	}

}
