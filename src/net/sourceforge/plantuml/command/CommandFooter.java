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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;

public class CommandFooter extends SingleLineCommand2<TitledDiagram> {

	public CommandFooter() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandFooter.class.getName(), //
				RegexLeaf.start(), //
				new RegexOptional(new RegexLeaf("POSITION", "(left|right|center)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("footer"), //
				new RegexOr( //
						new RegexConcat(RegexLeaf.spaceZeroOrMore(), new RegexLeaf(":"), RegexLeaf.spaceZeroOrMore()), //
						RegexLeaf.spaceOneOrMore()), //
				new RegexLeaf("LABEL", "(.*[\\p{L}0-9_.].*)"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram diagram, LineLocation location, RegexResult arg) {
		final String align = arg.get("POSITION", 0);
		HorizontalAlignment ha = HorizontalAlignment.fromString(align, HorizontalAlignment.CENTER);
		if (SkinParam.USE_STYLES() && align == null) {
			ha = FontParam.FOOTER.getStyleDefinition()
					.getMergedStyle(((UmlDiagram) diagram).getSkinParam().getCurrentStyleBuilder())
					.getHorizontalAlignment();
		}
		diagram.getFooter().putDisplay(Display.getWithNewlines(arg.get("LABEL", 0)), ha);

		
		return CommandExecutionResult.ok();
	}
}
