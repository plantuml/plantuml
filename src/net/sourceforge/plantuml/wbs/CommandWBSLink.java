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
package net.sourceforge.plantuml.wbs;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandWBSLink extends SingleLineCommand2<WBSDiagram> {
    // ::remove folder when __HAXE__

	public CommandWBSLink() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandWBSLink.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("CODE1", "([%pLN_]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("LINK", "[.-]+\\>"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("CODE2", "([%pLN_]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf("LABEL_LINK", "(?::[%s]*(.+))?"), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	@Override
	protected CommandExecutionResult executeArg(WBSDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String code1 = arg.get("CODE1", 0);
		final String code2 = arg.get("CODE2", 0);
//		final String message = arg.get("LABEL_LINK", 0);

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
//		link.setColors(color);
//		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		Stereotype stereotype = null;
		if (arg.get("STEREOTYPE", 0) != null) {
			stereotype = Stereotype.build(arg.get("STEREOTYPE", 0));
		}

		return diagram.link(code1, code2, colors, stereotype);
	}

}
