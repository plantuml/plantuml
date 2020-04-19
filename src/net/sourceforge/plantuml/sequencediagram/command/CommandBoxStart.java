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
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandBoxStart extends SingleLineCommand2<SequenceDiagram> {

	public CommandBoxStart() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandBoxStart.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("box"), //
				new RegexOptional(new RegexOr( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("NAME1", "[%g]([^%g]+)[%g]")), //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("NAME2", "([^#]+)")))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREO", "(\\<\\<.*\\>\\>)?"), //
				color().getRegex(), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg) {
		if (diagram.isBoxPending()) {
			return CommandExecutionResult.error("Box cannot be nested");
		}
		final String argTitle = arg.getLazzy("NAME", 0);
		final String argColor = arg.get("COLOR", 0);

		final String stereo = arg.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereo != null) {
			final ISkinParam skinParam = diagram.getSkinParam();
			stereotype = new Stereotype(stereo);
		}

		// final HtmlColor color = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(argColor);
		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		final String title = argTitle == null ? "" : argTitle;
		diagram.boxStart(Display.getWithNewlines(title), colors.getColor(ColorType.BACK), stereotype);
		return CommandExecutionResult.ok();
	}

}
