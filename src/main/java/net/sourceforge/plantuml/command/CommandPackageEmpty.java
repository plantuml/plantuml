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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPackageEmpty extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageEmpty() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackageEmpty.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("package"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "DISPLAY", "([%g][^%g]+[%g]|[^#%s{}]*)"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "CODE", "([%pLN_.]+)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "COLOR", "(#[0-9a-fA-F]{6}|#?\\w+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\}"), //
				RegexLeaf.end()); //
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// 'package Name {}' declares an empty package, opened and closed on
		// the same line; an empty name creates an anonymous one, like in
		// executeArg.
		final String display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
		final String code = arg.get("CODE", 0);
		if (code != null)
			sb.append("Declaring the empty package '").append(code).append("' displayed as \"").append(display)
					.append("\"");
		else if (display.length() == 0)
			sb.append("Declaring an anonymous empty package");
		else
			sb.append("Declaring the empty package '").append(display).append("'");

		if (arg.get("COLOR", 0) != null)
			sb.append(", background color ").append(arg.get("COLOR", 0));

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final String idShort;
		final String display;
		if (arg.get("CODE", 0) == null) {
			if (StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0)).length() == 0) {
				idShort = diagram.getUniqueSequence("##");
				display = null;
			} else {
				idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
				display = idShort;
			}
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
			idShort = arg.get("CODE", 0);
		}
		final Quark<Entity> quark = diagram.quarkInContext(false, diagram.cleanId(idShort));
		final CommandExecutionResult status = diagram.gotoGroup(location, quark,
				Display.getWithNewlines(diagram.getPragma(), display), GroupType.PACKAGE);
		if (status.isOk() == false)
			return status;
		final Entity p = diagram.getCurrentGroup();
		final String color = arg.get("COLOR", 0);
		if (color != null)
			p.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColor(color));

		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}
